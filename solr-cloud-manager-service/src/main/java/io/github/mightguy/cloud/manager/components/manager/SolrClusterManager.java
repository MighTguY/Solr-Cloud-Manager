package io.github.mightguy.cloud.manager.components.manager;

import static io.github.mightguy.cloud.manager.util.CloudInitializerUtils.checkCloudSolrClientInstance;
import static io.github.mightguy.cloud.manager.util.CloudInitializerUtils.collectionNameWithSuffix;
import static io.github.mightguy.cloud.manager.util.CloudInitializerUtils.deleteDir;
import static io.github.mightguy.cloud.manager.util.CloudInitializerUtils.extractGitLocation;
import static io.github.mightguy.cloud.manager.util.CloudInitializerUtils.extractLocalLocation;
import static io.github.mightguy.cloud.manager.util.CloudInitializerUtils.readConfigNameLocations;

import io.github.mightguy.cloud.manager.components.SolrManagerContext;
import io.github.mightguy.cloud.manager.components.SolrManagerHelper;
import io.github.mightguy.cloud.manager.config.AppConfig;
import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrCommonsException;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.model.core.Alias;
import io.github.mightguy.cloud.manager.model.core.SolrCollection;
import io.github.mightguy.cloud.manager.model.request.ClusterInitializationType;
import io.github.mightguy.cloud.manager.model.request.InitializationRequestDetails;
import io.github.mightguy.cloud.manager.model.request.InitializationRequestDetails.CoreCreationRequest;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import io.github.mightguy.cloud.manager.util.Constants;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SolrClusterManager {

  private final SolrManagerContext solrManagerContext;
  private final AppConfig appConfig;
  private final SolrManagerHelper solrManagerHelper;
  private final SolrAliasManager aliasManager;

  public SolrClusterManager(SolrManagerContext solrManagerContext, AppConfig appConfig,
      SolrManagerHelper solrManagerHelper, SolrAliasManager aliasManager) {
    this.solrManagerContext = solrManagerContext;
    this.solrManagerHelper = solrManagerHelper;
    this.appConfig = appConfig;
    this.aliasManager = aliasManager;
  }

  public void initializeSolrCluster(String cluster, boolean deleteOldCollections,
      boolean uploadZkConf, ClusterInitializationType type,
      InitializationRequestDetails payload) {
    String folderName;
    switch (type) {
      case GIT:
        extractGitLocation(appConfig.getConfigOutPath(), payload.getGithubDetails());
        folderName = payload.getGithubDetails().getGithubProjectName();
        break;
      case LOCAL:
        extractLocalLocation(payload.getLocalDetails().getLocalFilePath());
        folderName = payload.getLocalDetails().getLocalFolder();
        break;
      default:
        throw new SolrCommonsException(ExceptionCode.SOLR_CLOUD_INVALID_CONFIG);
    }

    Map<String, Path> confPath = readConfigNameLocations(
        appConfig.getConfigOutPath() + Constants.SLASH + folderName);

    if (uploadZkConf) {
      solrManagerHelper.uploadConfigs(confPath, solrManagerContext.getConfigManager(cluster));
    }
    List<String> collections = createCollections(cluster, confPath, true, payload.getCoreDetails(),
        deleteOldCollections);
    reloadApp();
    reloadCollections(cluster, collections, false);
    deleteDir(appConfig.getConfigOutPath() + Constants.SLASH + folderName);
  }

  public List<String> createCollections(String cluster, Map<String, Path> confPath,
      boolean createAlias, Map<String, CoreCreationRequest> coreCreationRequestMap,
      boolean deleteOldCollections) {

    List<String> reloadCollectionNames = new ArrayList<>();
    CoreCreationRequest defaultCoreCreationRequest = new CoreCreationRequest();
    for (Map.Entry<String, Path> entry : confPath.entrySet()) {
      String coreName = entry.getKey();
      String activeCollection = createCollectionCore(cluster, coreName,
          coreCreationRequestMap.getOrDefault(coreName, defaultCoreCreationRequest),
          appConfig.getCollectionSuffix().getActive(), solrManagerContext.getSolrClient(cluster),
          deleteOldCollections);
      String passiveCollection = createCollectionCore(cluster, coreName,
          coreCreationRequestMap.getOrDefault(coreName, defaultCoreCreationRequest),
          appConfig.getCollectionSuffix().getPassive(), solrManagerContext.getSolrClient(cluster),
          deleteOldCollections);

      if (createAlias) {
        aliasManager.createAlias(cluster, activeCollection,
            coreName.concat(appConfig.getAlias().getSuffix().getActive()));
        aliasManager.createAlias(cluster, passiveCollection,
            coreName.concat(appConfig.getAlias().getSuffix().getPassive()));
      }

      reloadCollectionNames.add(activeCollection);
      reloadCollectionNames.add(passiveCollection);
    }
    return reloadCollectionNames;
  }

  public String createCollectionCore(String cluster, String collectionName,
      CoreCreationRequest coreCreationRequest,
      String collectionSuffix,
      SolrClient solrClient,
      boolean deleteOldCollections) {
    CloudSolrClient cloudSolrClient = checkCloudSolrClientInstance(solrClient);
    try {
      if (coreCreationRequest.numOfReplicas == -1) {
        coreCreationRequest.numOfReplicas = cloudSolrClient.getZkStateReader().getClusterState()
            .getLiveNodes().size();
      }
      if (StringUtils.isEmpty(coreCreationRequest.configName)) {
        coreCreationRequest.configName = collectionName;
      }
      String collectionNameToCreate = collectionNameWithSuffix(collectionName, collectionSuffix,
          false);
      if (deleteOldCollections) {
        deleteCollection(cluster, collectionNameToCreate);
      }
      if (solrManagerContext.isCollectionAlreadyPresent(cluster, collectionNameToCreate)) {
        log.warn("Collection already present " + collectionNameToCreate);
        return collectionNameToCreate;
      }
      log.info("Creating collection : " + collectionNameToCreate);
      CollectionAdminRequest
          .createCollection(
              collectionNameToCreate,
              coreCreationRequest.configName,
              coreCreationRequest.numOfShards,
              coreCreationRequest.numOfReplicas
          )
          .process(cloudSolrClient);
      return collectionNameToCreate;
    } catch (SolrServerException | IOException e) {
      log.error("Exception during collection or alias creation. ", e);
      throw new SolrCommonsException(ExceptionCode.UNABLE_TO_CREATE_COLLECTION, e);
    }
  }

  public void reloadCollections(String cluster, List<String> reloadCollections,
      boolean onlyPassive) {
    for (String collection : reloadCollections) {
      Alias collectionAlias = solrManagerContext.getSolrAlias(cluster, collection);
      if (collectionAlias.isPassive() || !onlyPassive) {
        reloadCollection(cluster, collection);
      }
    }
    log.info("Collections Successfully Reloaded " + reloadCollections);
  }

  public void reloadCluster(String cluster, boolean onlyPassive) {
    for (SolrCollection collection : solrManagerContext.getSolrCollections(cluster)) {
      Alias collectionAlias = solrManagerContext.getSolrAlias(cluster, collection.getName());
      if (collectionAlias.isPassive() || !onlyPassive) {
        reloadCollection(cluster, collection.getName());
      }
    }
    log.info("Cluster Successfully Reloaded ");
  }

  private CollectionAdminResponse reloadCollection(String cluster, String collectionName) {
    try {
      log.info("Reloading collection: " + collectionName);
      CollectionAdminResponse response = CollectionAdminRequest.reloadCollection(collectionName)
          .process(solrManagerContext.getSolrClient(cluster));
      log.info("Reloading collection :" + collectionName + " finished");
      return response;
    } catch (SolrServerException | IOException ex) {
      log.error("Exception during collection reload. ", ex);
      throw new SolrCommonsException(ex, ExceptionCode.UNABLE_TO_RELOAD_COLLECTION, collectionName);
    }
  }

  public Response getCollections(String cluster) {
    return new Response(HttpStatus.OK,
        null,
        solrManagerContext.getSolrCollections(cluster));
  }

  public Response listAllClusters() {
    Response response = new Response(HttpStatus.OK, "");
    response.setResp(appConfig.getClusters());
    return response;
  }

  private boolean collectionExists(String cluster, String collectionName) {
    return solrManagerContext.isCollectionAlreadyPresent(cluster, collectionName);
  }


  public Response deleteCollectionData(String cluster, String collectionName, boolean commit) {
    try {
      log.info("Deleting data from collection: " + collectionName);
      SolrClient solrClient = solrManagerContext.getSolrClient(cluster);
      UpdateResponse updateResponse = solrClient.deleteByQuery(collectionName, "*:*");
      if (commit) {
        solrClient.commit(collectionName);
      }
      log.info("Deleting data from collection :" + collectionName + " finished");
      return new Response(HttpStatus.OK,
          "Deletion for [" + collectionName + "] of [" + cluster + "] successfully elapsed ["
              + updateResponse.getElapsedTime() + "]");
    } catch (SolrServerException | IOException ex) {
      log.error("Exception during collection reload. ", ex);
      throw new SolrCommonsException(ex, ExceptionCode.UNABLE_TO_DELETE_COLLECTION_DATA,
          collectionName);
    }
  }

  public Response deleteCollections(String cluster) {
    aliasManager.deleteAllAliases(cluster);
    solrManagerContext.getSolrCollections(cluster)
        .forEach(collection -> deleteCollection(cluster, collection.getName()));
    return new Response(HttpStatus.ACCEPTED, Constants.ALL_COLLECTION_DELETED);
  }

  public Response deleteCollection(String cluster, String collectionName) {
    if (!collectionExists(cluster, collectionName)) {
      return CloudInitializerUtils.createErrorResponse("NO SUCH COLLECTION " + collectionName);
    }
    Alias alias = solrManagerContext.getSolrAlias(cluster, collectionName);
    if (alias != null && !StringUtils.isEmpty(alias.getAlias())) {
      aliasManager.deleteAlias(cluster, alias.getAlias());
    }

    solrManagerHelper.deleteCollection(collectionName, solrManagerContext.getSolrClient(cluster));
    return new Response(HttpStatus.ACCEPTED, Constants.COLLECTION_DELETED);
  }

  public void reloadApp() {
    solrManagerContext.reloadContext();
  }

  public Response deleteAllAliases(String cluster) {
    return aliasManager.deleteAllAliases(cluster);
  }
}
