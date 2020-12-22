
package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.model.request.InitializationRequestDetails;


public interface AdminLightningController {


  Response doInitializeSolrCloud(String cluster, boolean deleteOldCollections,
      boolean uploadZkConf, InitializationRequestDetails payload);


  Response reloadCollection(String cluster, String collectionName,
      boolean onlyShadowReload);


  Response reloadCluster(String cluster, boolean onlyShadowReload);


  Response getCollections(String cluster);


  Response deleteAllCollections(String cluster);


  Response deleteCollection(String cluster, String collection);


  Response listAllClusters();


  Response deleteData(String cluster, String collectionName, boolean commit);
}
