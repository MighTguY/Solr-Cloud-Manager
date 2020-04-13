
package io.github.mightguy.cloud.manager.config;

import io.github.mightguy.cloud.manager.config.AppConfig.ZkCluster;
import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrException;
import io.github.mightguy.cloud.manager.request.QueryRequestManager;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.cloud.ZkConfigManager;
import org.apache.solr.common.params.CollectionParams.CollectionAction;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * This class {@code LightningContext} holds all the config from the curernt module and the other
 * modules, It act as a common context for all the API.
 */
@Slf4j
@Component
@Data
public class LightningContext {

  @Autowired
  AppConfig appConfig;

  @Autowired
  @Qualifier("defaultSolrClient")
  SolrClientFactory defaultSolrClient;

  @Autowired
  QueryRequestManager queryRequestManager;

  @Autowired
  SolrConfigruationProperties solrConfigruationProperties;

  private Map<String, Map<String, String>> solrAliasToCollectionClusterMap;
  private Map<String, Map<String, String>> solrCollectionToAliasClusterMap;
  private Map<String, List<String>> collectionListClusterMap;
  private Map<String, SolrClient> solrClientClusterMap;

  /*
  Not used for cluster slection, usage is only for validation
   */
  private String dummyActiveCluster;


  LightningContext() {
    solrAliasToCollectionClusterMap = new HashMap<>();
    solrCollectionToAliasClusterMap = new HashMap<>();
    collectionListClusterMap = new HashMap<>();
    solrClientClusterMap = new HashMap<>();
  }

  @PostConstruct
  public void init() {
    appConfig.getClusters().forEach(this::initializeContext);
  }

  private void initializeContext(String clusterName, ZkCluster zkCluster) {
    SolrClient solrClient = null;
    if (clusterName.equals("default")) {
      solrClient = defaultSolrClient.getClient();
    } else {
      solrClient = CloudInitializerUtils.getSolrClient(zkCluster);
    }

    solrClientClusterMap.put(clusterName, solrClient);
    reloadCollectionList(clusterName);
    reloadAliasMap(clusterName);
  }

  private void reloadCollectionList(String activeCluster) {
    SolrClient solrClient = solrClientClusterMap.get(activeCluster);
    List<String> collectionList = new ArrayList<>();
    collectionList.addAll(fetchCollectionList(solrClient));
    collectionListClusterMap.put(activeCluster, collectionList);
  }

  private void reloadAliasMap(String activeCluster) {
    SolrClient solrClient = solrClientClusterMap.get(activeCluster);
    Map<String, String> solrAliasToCollectionMap = fetchAliasMap(solrClient);
    Map<String, String> solrCollectionToAliasMap
        = solrAliasToCollectionMap.entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (key1, key2) -> {
          log.error("duplicate key found!");
          return key1;
        }));

    solrAliasToCollectionClusterMap.put(activeCluster, solrAliasToCollectionMap);
    solrCollectionToAliasClusterMap.put(activeCluster, solrCollectionToAliasMap);
  }

  public void reload(String activeCluster) {
    reloadAliasMap(activeCluster);
    reloadCollectionList(activeCluster);
  }

  public HttpSolrClient getHttpSolrClient(String liveNodeUrl) {
    return new HttpSolrClient.Builder(liveNodeUrl).build();
  }


  public Map<String, String> getSolrAliasToCollectionMap(String activeCluster) {
    return solrAliasToCollectionClusterMap.get(activeCluster);
  }

  public Map<String, String> getSolrCollectionToAliasMap(String activeCluster) {
    return solrCollectionToAliasClusterMap.get(activeCluster);
  }

  public List<String> getCollectionList(String activeCluster) {
    return collectionListClusterMap.get(activeCluster);
  }

  public ZkConfigManager getConfigManager(String activeCluster) {
    SolrClient solrClient = solrClientClusterMap.get(activeCluster);
    if (solrClient instanceof CloudSolrClient) {
      return ((CloudSolrClient) solrClient).getZkStateReader().getConfigManager();
    }
    throw new SolrException(ExceptionCode.SOLR_CLOUD_SUPPORTED_ONLY,
        "SolrCloudClient is only supported");
  }

  public SolrClient getSolrClient(String activeCluster) {
    return solrClientClusterMap.get(activeCluster);
  }

  public List<String> fetchCollectionList(SolrClient solrClient) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("action", CollectionAction.LIST.toString());
    SolrRequest request = new QueryRequest(params);
    request.setPath("/admin/collections");
    QueryResponse queryResponse = queryRequestManager.getQueryReponse(request, solrClient);
    if (queryResponse == null) {
      return new ArrayList<>();
    }
    return (List<String>) queryResponse.getResponse().get("collections");
  }

  public Map<String, String> fetchAliasMap(SolrClient solrClient) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("action", CollectionAction.LISTALIASES.toString());
    SolrRequest request = new QueryRequest(params);
    request.setPath("/admin/collections");
    QueryResponse queryResponse = queryRequestManager.getQueryReponse(request, solrClient);
    if (queryResponse == null) {
      return new HashMap<>();
    }
    return (HashMap<String, String>) queryResponse.getResponse().get("aliases");
  }
}
