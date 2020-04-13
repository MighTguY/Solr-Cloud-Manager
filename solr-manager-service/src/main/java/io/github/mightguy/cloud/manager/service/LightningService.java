
package io.github.mightguy.cloud.manager.service;


import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.manager.DataBackupManagerService;
import io.github.mightguy.cloud.manager.manager.SolrCloudManager;
import io.github.mightguy.cloud.manager.model.InitializerConfig;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.util.Constants;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * This class {@code LightningService}  will act as a facade for all the API/Solr cloud operations
 */
@Service
@Scope("prototype")
public class LightningService {

  @Autowired
  private AliasManagerService aliasManagerService;

  @Autowired
  private SolrCloudManager solrCloudManager;

  @Autowired
  private LightningContext lightningContext;


  public Response pushConfig(String cluster, String collectionName, boolean reload,
      String configName, Set<String> data, String appendDir) {
    return solrCloudManager
        .pushConfig(cluster, collectionName, reload, configName, data, appendDir);
  }

  public Response initlializeCloudSolr(InitializerConfig initializerConfig) {
    solrCloudManager
        .initializeSolrCloud(initializerConfig);
    Response response = aliasManagerService
        .getAlias(initializerConfig.getCluster(), Constants.ALL_COLLECTIONS);
    response.setMessage(Constants.SOLR_CLOUD_SUCCESSFULLY_INITIALIZED);
    return response;
  }

  public Response reloadCollection(String cluster, String collectionName,
      boolean onlyShadowReload) {
    return solrCloudManager.reloadCollection(cluster, collectionName, onlyShadowReload);
  }

  public Response getCollections(String cluster) {
    return solrCloudManager.getCollections(cluster);
  }

  public Response deleteCollections(String cluster) {
    return solrCloudManager.deleteCollections(cluster);
  }

  public Response deleteCollection(String cluster, String collectionName) {
    return solrCloudManager.deleteCollection(cluster, collectionName);
  }


  public Response listAllClusters() {
    return solrCloudManager.listAllClusters();
  }

  public Response deleteCollectionData(String cluster, String collectionName, boolean commit) {
    return solrCloudManager.deleteCollectionData(cluster, collectionName, commit);
  }

  public Response changeConfigSet(String cluster, String collectionName, String configName,
      String value) {
    return solrCloudManager.changeConfigSet(cluster, collectionName, configName, value);
  }
}
