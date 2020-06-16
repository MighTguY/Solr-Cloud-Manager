package io.github.mightguy.cloud.manager.config;

import io.github.mightguy.cloud.manager.config.AppConfig.ZkCluster;
import io.github.mightguy.cloud.manager.exception.SolrCloudInitializerException;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import io.github.mightguy.cloud.manager.util.QueryRequestUtils;
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
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CollectionParams.CollectionAction;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
public class LightningContext {

  @Autowired
  private AppConfig appConfig;

  private Map<String, Map<String, String>> solrAliasToCollectionClusterMap;
  private Map<String, Map<String, String>> solrCollectionToAliasClusterMap;
  private Map<String, List<String>> collectionListClusterMap;
  private Map<String, SolrClient> solrClientClusterMap;

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
    SolrClient solrClient = CloudInitializerUtils.getSolrClient(zkCluster);
    solrClientClusterMap.put(clusterName, solrClient);
    reloadCollectionList(clusterName, solrClient);
    reloadAliasMap(clusterName, solrClient);
  }

  public void reloadAliasMap(String activeCluster) {
    reloadCollectionList(activeCluster, solrClientClusterMap.get(activeCluster));
  }

  private void reloadAliasMap(String activeCluster, SolrClient solrClient) {
    if (solrClient == null) {
      throw new SolrCloudInitializerException("No SolrClient Configured for this cluster");
    }
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

  private void reloadCollectionList(String activeCluster, SolrClient solrClient) {
    collectionListClusterMap.put(activeCluster, new ArrayList<>(fetchCollectionList(solrClient)));
  }


  private void reloadCollectionList(String activeCluster) {
    SolrClient solrClient = solrClientClusterMap.get(activeCluster);
    reloadCollectionList(activeCluster, solrClient);
  }

  public List<String> fetchCollectionList(SolrClient solrClient) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("action", CollectionAction.LIST.toString());
    SolrRequest<QueryResponse> request = new QueryRequest(params);
    request.setPath("/admin/collections");
    QueryResponse queryResponse = QueryRequestUtils.getQueryResponse(request, solrClient);
    return (List<String>) queryResponse.getResponse().get("collections");
  }

  public Map<String, String> fetchAliasMap(SolrClient solrClient) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("action", CollectionAction.LISTALIASES.toString());
    SolrRequest<QueryResponse> request = new QueryRequest(params);
    request.setPath("/admin/collections");
    QueryResponse queryResponse = QueryRequestUtils.getQueryResponse(request, solrClient);
    return (HashMap<String, String>) queryResponse.getResponse().get("aliases");
  }


  public SolrClient getSolrCientForCluster(String cluster) {
    SolrClient solrClient = solrClientClusterMap.getOrDefault(cluster, null);
    if (solrClient == null) {
      throw new SolrCloudInitializerException("No Such cluster configured");
    }
    return solrClient;
  }
}
