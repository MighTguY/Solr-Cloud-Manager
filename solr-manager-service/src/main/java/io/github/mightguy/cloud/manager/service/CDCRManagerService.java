package io.github.mightguy.cloud.manager.service;

import io.github.mightguy.cloud.manager.manager.CDCRManager;
import io.github.mightguy.cloud.manager.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This Service class {@code CDCRManagerService} is responsible for propogating all the CDCR
 * Opeartions to the CDCRManager
 */
@Service
public class CDCRManagerService {

  @Autowired
  private CDCRManager cdcrManager;

  public Response statsQueue(String cluster, String collectionName) {
    return cdcrManager.statsQueue(cluster, collectionName);
  }

  public Response statsReplication(String cluster, String collectionName) {
    return cdcrManager.statsReplication(cluster, collectionName);
  }

  public Response errors(String cluster, String collectionName) {
    return cdcrManager.errors(cluster, collectionName);
  }

  public Response status(String cluster, String collectionName) {
    return cdcrManager.status(cluster, collectionName);
  }

  public Response enableBuffer(String cluster, String collectionName, boolean action) {
    return cdcrManager.enableBuffer(cluster, collectionName, action);
  }

  public Response stopCDCR(String cluster, String collectionName) {
    return cdcrManager.stopCDCR(cluster, collectionName);
  }

  public Response startCDCR(String cluster, String collectionName) {
    return cdcrManager.startCDCR(cluster, collectionName);
  }
}
