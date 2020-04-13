
package io.github.mightguy.cloud.manager.service;


import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.manager.CDCRManager;
import io.github.mightguy.cloud.manager.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.handler.CdcrParams.CdcrAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * This Service class {@code AliasManagerService} is responsible for propogating all the Alias
 * Opeartions to the AliasManager
 */
@Slf4j
@Service
public class CdcrManagerService {

  @Autowired
  LightningContext lightningContext;

  @Autowired
  CDCRManager cdcrManager;

  public Response startCdcr(String cluster, String collection) {
    Response response = new Response(HttpStatus.OK, "CDCR Successfully Started");
    response
        .setResp(cdcrManager.performCDCRAction(lightningContext.getSolrClient(cluster), collection,
            CdcrAction.START));
    return response;
  }

  public Response stopCdcr(String cluster, String collection) {
    Response response = new Response(HttpStatus.OK, "CDCR Successfully Stopped");
    response
        .setResp(cdcrManager.performCDCRAction(lightningContext.getSolrClient(cluster), collection,
            CdcrAction.STOP));
    return response;
  }

  public Response currentStateCDCR(String cluster, String collection) {
    Response response = new Response(HttpStatus.OK, "CDCR Current State");
    response
        .setResp(cdcrManager.performCDCRAction(lightningContext.getSolrClient(cluster), collection,
            CdcrAction.STATUS));
    return response;
  }


  public Response changeBufferStateOnCDCRUpdates(String cluster, String collection,
      boolean bufferStatus) {
    Response response = new Response(HttpStatus.OK, "CDCR Buffer state Successfully Changed");
    if (bufferStatus) {
      response
          .setResp(
              cdcrManager.performCDCRAction(lightningContext.getSolrClient(cluster), collection,
                  CdcrAction.ENABLEBUFFER));
    } else {
      response
          .setResp(
              cdcrManager.performCDCRAction(lightningContext.getSolrClient(cluster), collection,
                  CdcrAction.DISABLEBUFFER));
    }
    return response;
  }

  public Response monitorCdcrQueueStats(String cluster, String collection) {
    Response response = new Response(HttpStatus.OK, "CDCR Queue Stats");
    response
        .setResp(cdcrManager.performCDCRAction(lightningContext.getSolrClient(cluster), collection,
            CdcrAction.QUEUES));
    return response;
  }

  public Response monitorCDCRReplicationLogs(String cluster, String collection) {
    Response response = new Response(HttpStatus.OK, "CDCR Replication Logs Stats");
    response
        .setResp(cdcrManager.performCDCRAction(lightningContext.getSolrClient(cluster), collection,
            CdcrAction.OPS));
    return response;
  }

  public Response monitorCdcrERRORStats(String cluster, String collection) {
    Response response = new Response(HttpStatus.OK, "CDCR ERROR Stats");
    response
        .setResp(cdcrManager.performCDCRAction(lightningContext.getSolrClient(cluster), collection,
            CdcrAction.ERRORS));
    return response;
  }

}
