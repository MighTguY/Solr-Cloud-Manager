package io.github.mightguy.cloud.manager.services;

import io.github.mightguy.cloud.manager.config.AppConfig;
import io.github.mightguy.cloud.manager.model.InitializerConfig;
import io.github.mightguy.cloud.manager.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CloudManagerService {

  @Autowired
  AppConfig appConfig;


  public Response initialize(InitializerConfig initializerConfig) {
    return null;
  }

  public Response initializeCollection(InitializerConfig initializerConfig) {
    return null;
  }

  public Response reload(String cluster) {
    return null;
  }

  public Response getCollections(String cluster) {
    return null;
  }

  public Response deleteCollection(String cluster, String all) {
    return null;
  }

  public Response listAllClusters() {
    return null;
  }

  public Response deleteCollectionData(String cluster, String collectionName, boolean commit) {
    return null;
  }
}
