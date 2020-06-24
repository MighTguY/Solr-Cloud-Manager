
package io.github.mightguy.cloud.manager.service;

import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.manager.DataBackupManager;
import io.github.mightguy.cloud.manager.manager.SolrCloudManager;
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
public class LightningServiceFacade {

  @Autowired
  private AliasManagerService aliasManagerService;

  @Autowired
  private SolrCloudManager solrCloudManager;

  @Autowired
  private DataBackupManager dataBackupManager;

  @Autowired
  private LightningContext lightningContext;

  public Response getAlias(String cluster, String collectionName) {
    return aliasManagerService.getAlias(cluster, collectionName);
  }

  public Response switchAlias(String cluster, String collectionName, boolean reload) {
    return aliasManagerService.switchAlias(cluster, collectionName, reload);
  }

  public Response pushConfig(String cluster, String collectionName, boolean reload,
      String configName, Set<String> data, String appendDir) {
    return solrCloudManager
        .pushConfig(cluster, collectionName, reload, configName, data, appendDir);
  }

  public Response initlializeCloudSolr(String cluster, boolean override,
      boolean deleteOldCollections,
      boolean uploadZkConf, String user, String password, String branch) {
    solrCloudManager
        .initializeSolrCloud(cluster, override, deleteOldCollections, uploadZkConf, user, password,
            branch);
    Response response = getAlias(cluster, Constants.ALL_COLLECTIONS);
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

  public Response deleteAllAliases(String cluster) {
    return solrCloudManager.deleteAllAliases(cluster);
  }

  public Response backUpAllCollection(String cluster) {
    return dataBackupManager.createBackup(cluster);
  }

  public Response restoreAllCollection(String cluster, String repoPath) {
    return dataBackupManager.restoreFullBackup(cluster, repoPath);
  }

  public Response listAllBackUp(String cluster) {
    return dataBackupManager.listBackups(cluster);
  }

  public Response backUpCollection(String cluster, String collectionName) {
    return dataBackupManager.createBackup(cluster, collectionName, null, null);
  }

  public Response restoreCollection(String cluster, String repo, String collectionName) {
    return dataBackupManager.restoreBackup(cluster, repo, collectionName);
  }

  public Response listAllClusters() {
    return solrCloudManager.listAllClusters();
  }

  public Response deleteCollectionData(String cluster, String collectionName, boolean commit) {
    return solrCloudManager.deleteCollectionData(cluster, collectionName, commit);
  }
}
