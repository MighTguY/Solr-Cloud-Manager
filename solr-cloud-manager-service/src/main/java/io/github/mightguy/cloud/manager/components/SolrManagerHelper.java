package io.github.mightguy.cloud.manager.components;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrCommonsException;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.NoOpResponseParser;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.StringUtils;
import org.apache.solr.common.cloud.ZkConfigManager;
import org.apache.solr.common.params.CollectionParams.CollectionAction;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SolrManagerHelper {

  private SolrQueryComponent solrQueryComponent;

  public SolrManagerHelper(
      SolrQueryComponent solrQueryComponent) {
    this.solrQueryComponent = solrQueryComponent;
  }

  public Set<String> fetchCollections(SolrClient solrClient) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("action", CollectionAction.LIST.toString());
    SolrRequest<QueryResponse> request = new QueryRequest(params);
    request.setPath("/admin/collections");
    QueryResponse queryResponse = solrQueryComponent.getQueryReponse(request, solrClient);
    if (queryResponse == null) {
      return new HashSet<>();
    }
    return new HashSet<>(((List<String>) queryResponse.getResponse().get("collections")));
  }

  public Map<String, String> fetchAliases(SolrClient solrClient) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("action", CollectionAction.LISTALIASES.toString());
    SolrRequest<QueryResponse> request = new QueryRequest(params);
    request.setPath("/admin/collections");
    QueryResponse queryResponse = solrQueryComponent.getQueryReponse(request, solrClient);
    if (queryResponse == null) {
      log.error("No Aliases found");
      return new HashMap<>();
    }
    return (HashMap<String, String>) queryResponse.getResponse().get("aliases");
  }

  public void deleteAlias(String alias, SolrClient solrClient) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("name", alias);
    params.set("action", CollectionAction.DELETEALIAS.toString());
    QueryRequest request = new QueryRequest(params);
    request.setPath("/admin/collections");
    solrQueryComponent.getQueryReponse(request, solrClient);
  }

  public void deleteCollection(String collectionName, SolrClient solrClient) {
    try {
      final CollectionAdminRequest.Delete deleteCollectionRequest =
          CollectionAdminRequest.deleteCollection(collectionName);
      deleteCollectionRequest
          .process(solrClient);
      log.info("Collection deleted " + collectionName);
    } catch (IOException | SolrServerException e) {
      throw new SolrCommonsException(e, ExceptionCode.SOLR_EXCEPTION,
          "Deletion Failed [" + collectionName + "]");
    }
  }

  public void uploadConfigs(Map<String, Path> configNameToLocationMap,
      ZkConfigManager zkConfigManager) {
    log.info("Config sets uploading started.");
    configNameToLocationMap.entrySet().forEach(collectionPathEntry -> {
      try {
        log.info("Uploading configuration set :" + collectionPathEntry.getKey());
        zkConfigManager
            .uploadConfigDir(collectionPathEntry.getValue(), collectionPathEntry.getKey());
        log.info("Configuration set successfully uploaded:" + collectionPathEntry.getKey());
      } catch (IOException e) {
        log.error("Exception during configuration upload.", e);
        throw new SolrCommonsException(ExceptionCode.SOLR_CLOUD_INVALID_CONFIG, e);
      }
    });
    log.info("Config sets uploading finished.");
  }

  public void verifyHealthyStateForClusterCollection(CloudSolrClient cloudSolrClient,
      String collectionName)
      throws SolrServerException, IOException {

    NoOpResponseParser responseParser = new NoOpResponseParser();
    responseParser.setWriterType("json");
    ResponseParser responseParserOrignal = cloudSolrClient.getParser();
    cloudSolrClient.setParser(responseParser);
    CollectionAdminRequest.ClusterStatus collectionAdminRequest =
        new CollectionAdminRequest.ClusterStatus();
    String str = (String) cloudSolrClient.request(collectionAdminRequest)
        .get("response");
    cloudSolrClient.setParser(responseParserOrignal);
    DocumentContext jsonContext = JsonPath.parse(str,
        Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build());

    JSONArray jsonArray = jsonContext
        .read("$.cluster.collections." + collectionName + ".shards.*.replicas.*.state");

    for (Object obj : jsonArray) {
      if (!((String) obj).equals("active")) {
        log.info("Cluster not in healthy State  collection : {}.", collectionName);
        throw new SolrCommonsException(ExceptionCode.CLUSTER_UNHEALTHY);
      }
    }
  }

  public void createBackup(SolrClient solrClient, String collectionName,
      String backupPath, String timeStamp, String backupSuffix) {
    try {
      if (StringUtils.isEmpty(backupPath) || StringUtils.isEmpty(timeStamp)) {
        throw new SolrCommonsException(ExceptionCode.SOLR_EXCEPTION,
            "Backup path/timestamp should not be empty");
      }
      CloudInitializerUtils.createIfNotExist(backupPath);
      CollectionAdminRequest
          .backupCollection(collectionName,
              collectionName + backupSuffix)
          .setLocation(backupPath)
          .process(solrClient);

    } catch (IOException | SolrServerException ex) {
      throw new SolrCommonsException(ex, ExceptionCode.SOLR_EXCEPTION,
          "BACKUP FAILURE " + collectionName);
    }
  }

  public void restoreBackup(SolrClient solrClient, String repoPath,
      String backupName, String newCollectionName) {
    try {

      CollectionAdminRequest.Restore restore = CollectionAdminRequest
          .restoreCollection(
              newCollectionName,
              backupName)
          .setLocation(repoPath);
      int status = restore.process(solrClient).getStatus();
      log.warn("Status is " + status);
    } catch (IOException | SolrServerException ex) {
      throw new SolrCommonsException(ex, ExceptionCode.SOLR_EXCEPTION,
          "RESTORE FAILURE " + newCollectionName);
    }
  }
}
