
package io.github.mightguy.cloud.manager.manager;

import static io.github.mightguy.cloud.manager.util.Constants.BACKUP_CREATED;
import static io.github.mightguy.cloud.manager.util.Constants.BACKUP_RESTORED;
import static io.github.mightguy.cloud.manager.util.Constants.PATH_DELIM;

import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import io.github.mightguy.cloud.solr.commons.exception.ExceptionCode;
import io.github.mightguy.cloud.solr.commons.exception.SolrException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * This class {@code DataBackupManager} is responsible for handing all the DataBackup and Backup
 * Retrieval api Operations to SOLR.
 */
@Slf4j
@Component
public class DataBackupManager {


  @Autowired
  SolrCloudManager solrCloudManager;

  @Autowired
  LightningContext lightningContext;


  public Response createBackup(String cluster) {
    CloudInitializerUtils.createIfNotExist(
        lightningContext.getAppConfig().getBackupInfo().getBackupLocation());
    String timeStamp = CloudInitializerUtils.getCurrentTimeStampForDIR();
    String backupPath =
        lightningContext.getAppConfig().getBackupInfo().getBackupLocation()
            + PATH_DELIM
            + cluster + PATH_DELIM
            + timeStamp;

    CloudInitializerUtils.createIfNotExist(backupPath);
    lightningContext.getCollectionList(cluster)
        .forEach(collection -> createBackup(cluster, collection, backupPath, timeStamp));

    return new Response(HttpStatus.CREATED, BACKUP_CREATED, timeStamp);
  }

  public Response createBackup(String cluster, String collectionName,
      String backupPath, String timeStamp) {
    try {
      if (StringUtils.isEmpty(backupPath)) {
        timeStamp = CloudInitializerUtils.getCurrentTimeStampForDIR();
        backupPath =
            lightningContext.getAppConfig().getBackupInfo().getBackupLocation()
                + PATH_DELIM
                + cluster + PATH_DELIM
                + timeStamp;

      }
      CloudInitializerUtils.createIfNotExist(backupPath);
      CollectionAdminRequest
          .backupCollection(collectionName,
              collectionName + lightningContext.getAppConfig().getBackupInfo()
                  .getBackupSuffix())
          .setLocation(backupPath)
          .process(lightningContext.getSolrClient(cluster));
    } catch (IOException | SolrServerException ex) {
      throw new SolrException(ex, ExceptionCode.SOLR_EXCEPTION, "BACKUP FAILURE " + collectionName);
    }
    return new Response(HttpStatus.CREATED, "Backup done for Collection " + collectionName,
        timeStamp);

  }

  private List<String> listAllCollectionsInRepo(String cluster, String repoName) {
    return CloudInitializerUtils.dirContentList(
        lightningContext.getAppConfig().getBackupInfo().getBackupLocation()
            + PATH_DELIM + cluster + PATH_DELIM + repoName);
  }

  public Response listBackups(String cluster) {
    String path =
        lightningContext.getAppConfig().getBackupInfo().getBackupLocation()
            + PATH_DELIM + cluster;
    Map<String, List<String>> backupRepo = CloudInitializerUtils.dirContentList(path)
        .parallelStream()
        .collect(
            Collectors.toMap(location -> location,
                location -> listAllCollectionsInRepo(cluster, location)));
    return new Response(HttpStatus.OK, null, backupRepo);
  }

  public Response restoreFullBackup(String cluster, String repoName, boolean deleteOrignal,
      String suffix) {
    String repoPath =
        lightningContext.getAppConfig().getBackupInfo().getBackupLocation()
            + PATH_DELIM + cluster + PATH_DELIM + repoName;
    CloudInitializerUtils.dirContentList(repoPath).forEach(bkupCollection -> {
      String collName = bkupCollection.replace(
          lightningContext.getAppConfig().getBackupInfo().getBackupSuffix(),
          "");
      restoreBackup(cluster, repoName, collName, deleteOrignal, suffix);
    });
    return new Response(HttpStatus.OK, BACKUP_RESTORED);
  }

  public Response restoreBackup(String cluster, String repoName,
      String collectionName, boolean deleteOrignal,
      String suffix) {
    String repoPath =
        lightningContext.getAppConfig().getBackupInfo().getBackupLocation()
            + PATH_DELIM + cluster + PATH_DELIM + repoName;
    try {
      if (deleteOrignal) {
        solrCloudManager.deleteCollection(cluster, collectionName);
      }
      if (StringUtils.isEmpty(suffix)) {
        suffix = lightningContext.getAppConfig().getBackupInfo()
            .getRestoreSuffix();
      }
      CollectionAdminRequest.Restore restore = CollectionAdminRequest
          .restoreCollection(
              collectionName + suffix,
              collectionName + lightningContext.getAppConfig().getBackupInfo()
                  .getBackupSuffix())
          .setLocation(repoPath);
      int status = restore.process(lightningContext.getSolrClient(cluster)).getStatus();
      log.warn("Status is " + status);
    } catch (IOException | SolrServerException ex) {
      throw new SolrException(ex, ExceptionCode.SOLR_EXCEPTION,
          "Unable to restore collection " + collectionName + " "
              + "from repo " + repoPath);
    }
    return new Response(HttpStatus.OK,
        "Collection succesfully restored to " + collectionName + lightningContext.getAppConfig()
            .getBackupInfo().getRestoreSuffix());
  }


}
