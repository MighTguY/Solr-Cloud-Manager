package io.github.mightguy.cloud.manager.service;

import io.github.mightguy.cloud.manager.components.Validator;
import io.github.mightguy.cloud.manager.components.manager.SolrClusterManager;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.model.request.ClusterInitializationType;
import io.github.mightguy.cloud.manager.model.request.InitializationRequestDetails;
import io.github.mightguy.cloud.manager.util.Constants;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AdminLightningService {

  final Validator validator;
  private final SolrClusterManager solrClusterManager;

  public AdminLightningService(Validator validator,
      SolrClusterManager solrClusterManager) {
    this.validator = validator;
    this.solrClusterManager = solrClusterManager;
  }

  public Response initializeCloud(String cluster, boolean deleteOldCollections,
      boolean uploadZkConf, ClusterInitializationType type,
      InitializationRequestDetails payload) {

    validator
        .validateInitializeCloudRequest(cluster, deleteOldCollections, uploadZkConf, type, payload);
    solrClusterManager
        .initializeSolrCluster(cluster, deleteOldCollections, uploadZkConf, type, payload);
    return new Response(HttpStatus.CREATED, Constants.SOLR_CLOUD_SUCCESSFULLY_INITIALIZED);
  }

  public Response listClusters() {
    return solrClusterManager.listAllClusters();
  }

  public Response deleteData(String cluster, String collectionName, boolean commit) {
    return solrClusterManager.deleteCollectionData(cluster, collectionName, commit);
  }

  public Response deleteCluster(String cluster) {
    return solrClusterManager.deleteCollections(cluster);
  }

  public Response deleteCollections(String cluster, String collectionName) {
    return solrClusterManager.deleteCollection(cluster, collectionName);
  }

  public Response reloadCollection(String cluster, String collectionName, boolean onlyPassive) {
    solrClusterManager
        .reloadCollections(cluster, Collections.singletonList(collectionName), onlyPassive);
    return new Response(HttpStatus.ACCEPTED, Constants.COLLECTION_RELOADED + collectionName);
  }

  public Response reloadCluster(String cluster, boolean onlyPassive) {
    solrClusterManager
        .reloadCluster(cluster, onlyPassive);
    return new Response(HttpStatus.ACCEPTED, Constants.CLUSTER_RELOADED);
  }

  public Response listCollections(String cluster) {
    return solrClusterManager.getCollections(cluster);
  }

  public void refresh() {
    solrClusterManager.reloadApp();
  }
}
