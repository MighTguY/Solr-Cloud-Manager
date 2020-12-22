package io.github.mightguy.cloud.manager.controller.impl;

import io.github.mightguy.cloud.manager.controller.CDCRController;
import io.github.mightguy.cloud.manager.model.Response;

public class CDCRControllerImpl implements CDCRController {

  @Override
  public Response startCDCR(String cluster, String collectionName) {
    return null;
  }

  @Override
  public Response stopCDCR(String cluster, String collectionName) {
    return null;
  }

  @Override
  public Response enableBufferOnCDCRUpdates(String cluster, String collectionName) {
    return null;
  }

  @Override
  public Response disableBufferOnCDCRUpdates(String cluster, String collectionName) {
    return null;
  }

  @Override
  public Response getCurrentState(String cluster, String collectionName) {
    return null;
  }

  @Override
  public Response monitorCDCRErrors(String cluster, String collectionName) {
    return null;
  }

  @Override
  public Response monitorCDCRQueueStats(String cluster, String collectionName) {
    return null;
  }

  @Override
  public Response monitorCDCRReplicationLogs(String cluster, String collectionName) {
    return null;
  }
}
