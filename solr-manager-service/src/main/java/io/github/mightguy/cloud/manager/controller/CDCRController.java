package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.model.Response;

public interface CDCRController {


  public Response startCDCR(String cluster, String collectionName);

  public Response stopCDCR(String cluster, String collectionName);

  public Response enableBufferOnCDCRUpdates(String cluster, String collectionName);

  public Response disableBufferOnCDCRUpdates(String cluster, String collectionName);

  public Response getCurrentState(String cluster, String collectionName);

  public Response monitorCDCRErrors(String cluster, String collectionName);

  public Response monitorCDCRQueueStats(String cluster, String collectionName);

  public Response monitorCDCRReplicationLogs(String cluster, String collectionName);
}
