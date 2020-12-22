package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.model.Response;

public interface DataBackupController {

  public Response backupAll(String cluster);

  public Response backupCollection(String cluster, String collectionName);

  public Response restoreAllCollections(String cluster, String repo,
      boolean deleteOriginal, String suffix);


  public Response restoreCollection(String cluster, String collectionName, String repo,
      boolean deleteOriginal, String suffix);

  public Response listAllBackup(String cluster);

}
