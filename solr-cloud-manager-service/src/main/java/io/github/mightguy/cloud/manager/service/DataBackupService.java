package io.github.mightguy.cloud.manager.service;

import static io.github.mightguy.cloud.manager.util.Constants.PATH_DELIM;

import io.github.mightguy.cloud.manager.components.SolrManagerContext;
import io.github.mightguy.cloud.manager.components.SolrManagerHelper;
import io.github.mightguy.cloud.manager.config.AppConfig;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import io.github.mightguy.cloud.manager.util.Constants;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.solr.common.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DataBackupService {

  private final SolrManagerContext solrManagerContext;
  private final AppConfig appConfig;

  private final SolrManagerHelper solrManagerHelper;

  public DataBackupService(
      SolrManagerContext solrManagerContext,
      AppConfig appConfig, SolrManagerHelper solrManagerHelper) {
    this.solrManagerContext = solrManagerContext;
    this.appConfig = appConfig;
    this.solrManagerHelper = solrManagerHelper;
  }

  public Response listAllBackUp(String cluster) {
    String path =
        appConfig.getBackupInfo().getBackupLocation()
            + PATH_DELIM + cluster;

    Map<String, List<String>> backupRepo = CloudInitializerUtils.dirContentList(path)
        .parallelStream()
        .collect(
            Collectors.toMap(location -> location,
                location -> listAllCollectionsInRepo(cluster, location)));
    return new Response(HttpStatus.OK, null, backupRepo);
  }


  private List<String> listAllCollectionsInRepo(String cluster, String repoName) {
    return CloudInitializerUtils.dirContentList(
        appConfig.getBackupInfo().getBackupLocation()
            + PATH_DELIM + cluster + PATH_DELIM + repoName);
  }


  public Response backUpAllCollection(String cluster) {
    CloudInitializerUtils.createIfNotExist(
        appConfig.getBackupInfo().getBackupLocation());
    String timeStamp = CloudInitializerUtils.getCurrentTimeStampForDIR();
    String backupPath =
        appConfig.getBackupInfo().getBackupLocation()
            + Constants.PATH_DELIM
            + cluster + PATH_DELIM
            + timeStamp;
    CloudInitializerUtils.createIfNotExist(backupPath);
    solrManagerContext.getSolrCollections(cluster)
        .forEach(collection -> solrManagerHelper
            .createBackup(solrManagerContext.getSolrClient(cluster), collection.getName(),
                backupPath, timeStamp, appConfig.getBackupInfo().getBackupSuffix()));
    return new Response(HttpStatus.CREATED, Constants.BACKUP_CREATED, timeStamp);
  }


  public Response backUpCollection(String cluster, String collectionName) {
    String timeStamp = CloudInitializerUtils.getCurrentTimeStampForDIR();
    String backupPath =
        appConfig.getBackupInfo().getBackupLocation()
            + Constants.PATH_DELIM
            + cluster + PATH_DELIM
            + timeStamp;
    CloudInitializerUtils.createIfNotExist(backupPath);
    solrManagerHelper
        .createBackup(solrManagerContext.getSolrClient(cluster), collectionName,
            backupPath, timeStamp, appConfig.getBackupInfo().getBackupSuffix());
    return new Response(HttpStatus.CREATED, "Backup done for Collection " + collectionName,
        timeStamp);
  }

  public Response restoreAllBackups(String cluster, String repo, boolean deleteOriginal,
      String suffix) {
    String repoPath = CloudInitializerUtils
        .getBackupRepoPath(appConfig.getBackupInfo().getBackupLocation(), cluster, repo);
    CloudInitializerUtils.dirContentList(repoPath).forEach(backupName -> {
      restoreBackup(cluster, repoPath, backupName, deleteOriginal, suffix);
    });
    return new Response(HttpStatus.OK, Constants.BACKUP_RESTORED);
  }

  public Response restoreForBackup(String cluster, String backupName, String repo,
      boolean deleteOriginal,
      String suffix) {
    String repoPath = CloudInitializerUtils
        .getBackupRepoPath(appConfig.getBackupInfo().getBackupLocation(), cluster, repo);
    restoreBackup(cluster, repoPath, backupName, deleteOriginal, suffix);
    return new Response(HttpStatus.OK, Constants.BACKUP_RESTORED);
  }

  private void restoreBackup(String cluster, String repoPath, String backupName,
      boolean deleteOriginal, String suffix) {

    String collName;
    if (StringUtils.isEmpty(suffix)) {
      collName = backupName.replace(appConfig.getBackupInfo().getBackupSuffix(),
          appConfig.getBackupInfo().getRestoreSuffix());
    } else {
      collName = backupName.replace(appConfig.getBackupInfo().getBackupSuffix(), suffix);
    }
    if (deleteOriginal) {
      solrManagerHelper.deleteCollection(collName, solrManagerContext.getSolrClient(cluster));
    }
    solrManagerHelper
        .restoreBackup(solrManagerContext.getSolrClient(cluster), repoPath, backupName, collName);
  }
}
