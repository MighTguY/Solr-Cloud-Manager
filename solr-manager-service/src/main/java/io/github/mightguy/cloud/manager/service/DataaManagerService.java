package io.github.mightguy.cloud.manager.service;

import io.github.mightguy.cloud.manager.manager.DataBackupManager;
import io.github.mightguy.cloud.manager.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataaManagerService {

  @Autowired
  private DataBackupManager dataBackupManager;

  public Response backUpAllCollection(String cluster) {
    return dataBackupManager.createBackup(cluster);
  }

  public Response restoreAllCollection(String cluster, String repoPath, boolean deleteOrignal,
      String suffix) {
    return dataBackupManager.restoreFullBackup(cluster, repoPath, deleteOrignal, suffix);
  }

  public Response listAllBackUp(String cluster) {
    return dataBackupManager.listBackups(cluster);
  }

  public Response backUpCollection(String cluster, String collectionName) {
    return dataBackupManager.createBackup(cluster, collectionName, null, null);
  }

  public Response restoreCollection(String cluster, String repo, String collectionName,
      boolean deleteOrignal, String suffix) {
    return dataBackupManager.restoreBackup(cluster, repo, collectionName, deleteOrignal, suffix);
  }

}
