
package io.github.mightguy.cloud.manager.manager;

import static io.github.mightguy.cloud.manager.util.CloudInitializerUtils.checkCloudSolrCLientInstance;
import static io.github.mightguy.cloud.manager.util.CloudInitializerUtils.deleteDir;

import io.github.mightguy.cloud.manager.config.AppConfig.GitInfo;
import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import io.github.mightguy.cloud.manager.util.Constants;
import io.github.mightguy.cloud.solr.commons.exception.ExceptionCode;
import io.github.mightguy.cloud.solr.commons.exception.SolrCloudException;
import io.github.mightguy.cloud.solr.commons.exception.SolrException;
import io.github.mightguy.cloud.solr.commons.exception.UnknownCollectionException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.StringUtils;
import org.apache.solr.common.cloud.ZkConfigManager;
import org.apache.solr.common.params.CollectionParams.CollectionAction;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * This class {@code SolrCloudInitializerManager} is responsible for initializing the SOLR cloud.
 */
@Slf4j
@Component
public class SolrCloudManager {

  @Autowired
  LightningContext lightningContext;

  @Autowired
  SolrCloudmanagerHelper solrCloudmanagerHelper;

  @Autowired
  AliasManager aliasManager;


  public void initializeSolrCloud(String cluster, boolean override, boolean deleteOldCollections,
      boolean uploadZkConf, String user, String pass, String folder) {
    GitInfo gitInfo = lightningContext.getAppConfig().getGitInfo();
    String gitUrl = gitInfo.getUrl();
    String gitOutPath = gitInfo.getClonepath();
    String branchName = gitInfo.getBranch();

    if (deleteOldCollections) {
      deleteCollections(cluster);
      lightningContext.reload(cluster);
    }
    if (override) {
      solrCloudmanagerHelper
          .extractGitRepo(gitUrl, gitOutPath, branchName, override, user, pass);
    }
    Map<String, Path> confPath = solrCloudmanagerHelper
        .readConfigNameLocations(gitOutPath + Constants.SLASH + folder);
    if (uploadZkConf) {
      solrCloudmanagerHelper.uploadConfigs(confPath, lightningContext.getConfigManager(cluster));
    }
    List<String> collections = createCollections(cluster, confPath);
    aliasManager.createAlias(cluster, collections);
    lightningContext.reload(cluster);
    reloadCollections(cluster, collections, false);
    if (override) {
      deleteDir(gitOutPath);
    }
  }


  public List<String> createCollections(String cluster, Map<String, Path> confPath) {

    String activeAliasSuffix = lightningContext.getSolrConfigruationProperties().getSuffix()
        .getActive();
    String passiveAliasSuffix = lightningContext.getSolrConfigruationProperties().getSuffix()
        .getPassive();
    List<String> relodCollectionNames = new ArrayList<>();
    for (Map.Entry<String, Path> entry : confPath.entrySet()) {
      String configSetName = entry.getKey();
      String activeAliasName = configSetName + activeAliasSuffix;
      String activeCollectionSuffix = CloudInitializerUtils
          .getCurrentCollectionSuffix(cluster, lightningContext, true,
              activeAliasName);
      String passiveAliasName = configSetName + passiveAliasSuffix;
      String passiveCollectionSuffix = CloudInitializerUtils
          .getCurrentCollectionSuffix(cluster, lightningContext, false,
              passiveAliasName);

      if (!lightningContext.getSolrAliasToCollectionMap(cluster).containsKey(activeAliasName)) {
        createCollection(cluster, configSetName,
            activeCollectionSuffix, lightningContext.getSolrClient(cluster));
        relodCollectionNames.add(configSetName + activeCollectionSuffix);
      }
      if (!lightningContext.getSolrAliasToCollectionMap(cluster).containsKey(passiveAliasName)) {
        createCollection(cluster, configSetName,
            passiveCollectionSuffix, lightningContext.getSolrClient(cluster));
        relodCollectionNames.add(configSetName + passiveCollectionSuffix);
      } else {
        relodCollectionNames.add(configSetName + passiveCollectionSuffix);
        relodCollectionNames.add(configSetName + activeCollectionSuffix);

      }
    }
    return relodCollectionNames;
  }


  public void createCollection(String cluster, String collectionName,
      String collectionSuffix,
      SolrClient solrClient
  ) {
    CloudSolrClient cloudSolrClient = checkCloudSolrCLientInstance(solrClient);
    try {
      String collectionNameToCreate = collectionName + collectionSuffix;
      if (lightningContext.getCollectionList(cluster).contains(collectionNameToCreate)) {
        log.warn("Collection already present " + collectionNameToCreate);
        return;
      }
      log.info("Creating collection : " + collectionNameToCreate);
      CollectionAdminRequest
          .createCollection(collectionNameToCreate, collectionName, 1,
              cloudSolrClient.getZkStateReader().getClusterState()
                  .getLiveNodes().size())
          .process(cloudSolrClient);
    } catch (SolrServerException | IOException e) {
      log.error("Exception during collection or alias creation. ", e);
      throw new SolrCloudException(ExceptionCode.UNABLE_TO_CREATE_COLLECTION_ALIAS, e);
    }
  }

  public void reloadCollections(String cluster, List<String> reloadCollections,
      boolean onlyShadow) {
    for (String collection : reloadCollections) {
      String collectionAlias = lightningContext.getSolrCollectionToAliasMap(cluster).get(collection)
          .stream().findFirst().get();
      if (collectionAlias.endsWith(
          lightningContext.getSolrConfigruationProperties().getSuffix().getPassive())
          || !onlyShadow) {
        reloadCollection(cluster, collection);
      }
    }
    log.info("Collections Succesfully Reloaded " + reloadCollections);
  }

  public Response reloadCollection(String cluster, String collectionName,
      boolean reloadOnlyShadow) {
    String activeAlias =
        collectionName + lightningContext.getSolrConfigruationProperties().getSuffix()
            .getActive();
    String passiveAlias =
        collectionName + lightningContext.getSolrConfigruationProperties().getSuffix()
            .getPassive();

    if (!(lightningContext.getSolrAliasToCollectionMap(cluster).containsKey(activeAlias)
        && lightningContext.getSolrAliasToCollectionMap(cluster).containsKey(passiveAlias))) {
      throw new UnknownCollectionException(ExceptionCode.UNKNOWN_COLLECTION, collectionName);
    }
    reloadCollection(cluster,
        lightningContext.getSolrAliasToCollectionMap(cluster).get(passiveAlias));
    if (!reloadOnlyShadow) {
      reloadCollection(cluster,
          lightningContext.getSolrAliasToCollectionMap(cluster).get(activeAlias));
    }
    return new Response(HttpStatus.ACCEPTED, Constants.COLLECTION_RELOADED + collectionName);
  }

  private CollectionAdminResponse reloadCollection(String cluster, String collectionName) {
    try {
      log.info("Reloading collection: " + collectionName);
      CollectionAdminResponse response = CollectionAdminRequest.reloadCollection(collectionName)
          .process(lightningContext.getSolrClient(cluster));
      log.info("Reloading collection :" + collectionName + " finished");
      return response;
    } catch (SolrServerException | IOException ex) {
      log.error("Exception during collection reload. ", ex);
      throw new SolrCloudException(ex, ExceptionCode.UNABLE_TO_RELOAD_COLLECTION, collectionName);
    }
  }

  public Response getCollections(String cluster) {
    return new Response(HttpStatus.OK,
        null,
        lightningContext.getCollectionList(cluster));
  }

  public Response deleteCollections(String cluster) {
    deleteAllAliases(cluster);
    lightningContext.getCollectionList(cluster)
        .forEach(collection -> deleteCollection(cluster, collection));
    return new Response(HttpStatus.ACCEPTED, Constants.ALL_COLLECTION_DELETED);
  }

  public Response deleteCollection(String cluster, String collectionName) {
    lightningContext.reload(cluster);
    if (!collectionExists(cluster, collectionName)) {
      return CloudInitializerUtils.createErrorResponse("NO SUCH COLLECTION " + collectionName);
    }
    aliasManager.deleteAlias(cluster,
        lightningContext.getSolrCollectionToAliasMap(cluster).get(collectionName));
    try {
      final CollectionAdminRequest.Delete deleteCollectionRequest =
          CollectionAdminRequest.deleteCollection(collectionName);
      deleteCollectionRequest
          .process(lightningContext.getSolrClient(cluster));
      log.info("Collection deleted " + collectionName);
      return new Response(HttpStatus.ACCEPTED, Constants.COLLECTION_DELETED);
    } catch (IOException | SolrServerException e) {
      throw new SolrException(e, ExceptionCode.SOLR_EXCEPTION,
          "Deletion Failed [" + collectionName + "]");
    }
  }

  private boolean collectionExists(String cluster, String collectionName) {
    return lightningContext.getCollectionList(cluster).contains(collectionName);
  }

  private void deleteAlias(String cluster, String alias) {
    try {
      if (!lightningContext.getSolrAliasToCollectionMap(cluster).containsKey(alias)) {
        return;
      }
      ModifiableSolrParams params = new ModifiableSolrParams();
      params.set("name", alias);
      params.set("action", CollectionAction.DELETEALIAS.toString());
      QueryRequest request = new QueryRequest(params);
      request.setPath("/admin/collections");
      lightningContext.getSolrClient(cluster).request(request);
    } catch (IOException | SolrServerException ex) {
      throw new SolrException(ex, ExceptionCode.SOLR_EXCEPTION,
          "Alias Deletion Failed [" + alias + "]");
    }
  }


  public Response deleteAllAliases(String cluster) {
    lightningContext.getSolrAliasToCollectionMap(cluster).keySet()
        .forEach(alias -> deleteAlias(cluster, alias));
    return new Response(HttpStatus.ACCEPTED, Constants.ALL_ALIASES_DELETED);
  }

  public Response listAllClusters() {
    Response response = new Response(HttpStatus.OK, "");
    response.setResp(lightningContext.getAppConfig().getClusters());
    return response;
  }

  public Response pushConfig(String cluster, String collectionName, boolean reload,
      String configName, Set<String> contents, String appendDir) {

    ZkConfigManager zkConfigManager = lightningContext.getConfigManager(cluster);
    File dir = new File("/tmp/managed-content-intitializer/" + collectionName);
    deleteDir(dir.getAbsolutePath());
    if (dir.mkdirs()) {
      log.info("Managed content dir created");
    }
    String configPath = dir.getAbsolutePath();
    if (!StringUtils.isEmpty(appendDir)) {
      configPath = configPath + Constants.SLASH + appendDir;
      new File(configPath).mkdirs();
    }

    File file = new File(configPath + Constants.SLASH + configName);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {

      for (String data : contents) {
        writer.append(data).append("\n");
      }
      writer.close();
      Map<String, Path> dataPath = Collections.singletonMap(collectionName, dir.toPath());
      solrCloudmanagerHelper.uploadConfigs(dataPath, zkConfigManager);
      if (reload) {
        reloadCollection(cluster, collectionName, false);
      }
      return new Response(HttpStatus.OK,
          "Config [" + configName + "] pushed to SOLR collection[" + collectionName + "]");
    } catch (IOException e) {
      log.error("Exception during configuration upload.", e);
      throw new SolrCloudException(ExceptionCode.SOLR_CLOUD_INVALID_CONFIG, e);
    }


  }

  public Response deleteCollectionData(String cluster, String collectionName, boolean commit) {
    try {
      log.info("Deleting data from collection: " + collectionName);
      SolrClient solrClient = lightningContext.getSolrClient(cluster);
      UpdateResponse updateResponse = solrClient.deleteByQuery(collectionName, "*:*");
      if (commit) {
        solrClient.commit(collectionName);
      }
      log.info("Deleting data from collection :" + collectionName + " finished");
      return new Response(HttpStatus.OK,
          "Deletion for [" + collectionName + "] of [" + cluster + "] successfull elapsed ["
              + updateResponse.getElapsedTime() + "]");
    } catch (SolrServerException | IOException ex) {
      log.error("Exception during collection reload. ", ex);
      throw new SolrCloudException(ex, ExceptionCode.UNABLE_TO_RELOAD_COLLECTION, collectionName);
    }
  }
}
