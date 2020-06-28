package io.github.mightguy.cloud.manager.manager;

import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.solr.commons.exception.ExceptionCode;
import io.github.mightguy.cloud.solr.commons.exception.SolrException;
import io.github.mightguy.cloud.solr.commons.request.QueryRequestManager;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.handler.CdcrParams.CdcrAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * This class {@code CDCRManager} is responsible for handing all the CDCR api Operations to SOLR.
 */
@Slf4j
@Component
public class CDCRManager {

  @Autowired
  LightningContext lightningContext;

  @Autowired
  QueryRequestManager queryRequestManager;

  public Response statsQueue(String cluster, String collectionName) {

    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set(CommonParams.ACTION, CdcrAction.QUEUES.toString());
    SolrRequest request = new QueryRequest(params);
    QueryResponse queryResponse = getQueryResponse(lightningContext.getSolrClient(cluster),
        collectionName, request);
    return new Response(HttpStatus.OK, "Stats For QUEUES", queryResponse.getResponse());
  }

  public Response statsReplication(String cluster, String collectionName) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set(CommonParams.ACTION, CdcrAction.START.toString());
    SolrRequest request = new QueryRequest(params);
    QueryResponse queryResponse = getQueryResponse(lightningContext.getSolrClient(cluster),
        collectionName, request);
    return new Response(HttpStatus.OK, "Started Replication");
  }

  public Response errors(String cluster, String collectionName) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set(CommonParams.ACTION, CdcrAction.ERRORS.toString());
    SolrRequest request = new QueryRequest(params);
    QueryResponse queryResponse = getQueryResponse(lightningContext.getSolrClient(cluster),
        collectionName, request);
    return new Response(HttpStatus.OK, "ERRORS",
        queryResponse.getResponse().get("errors"));
  }

  public Response status(String cluster, String collectionName) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set(CommonParams.ACTION, CdcrAction.STATUS.toString());
    SolrRequest request = new QueryRequest(params);
    QueryResponse queryResponse = getQueryResponse(lightningContext.getSolrClient(cluster),
        collectionName, request);
    return new Response(HttpStatus.OK, "STATUS FOR CDCR",
        queryResponse.getResponse().get("status"));
  }

  public Response enableBuffer(String cluster, String collectionName, boolean toEnable) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    if (toEnable) {
      params.set(CommonParams.ACTION, CdcrAction.ENABLEBUFFER.toString());
    } else {
      params.set(CommonParams.ACTION, CdcrAction.DISABLEBUFFER.toString());
    }
    SolrRequest request = new QueryRequest(params);
    QueryResponse queryResponse = getQueryResponse(lightningContext.getSolrClient(cluster),
        collectionName, request);
    return new Response(HttpStatus.OK, "BUFFER STATE CHANGE",
        queryResponse.getResponse().get("status"));
  }

  public Response stopCDCR(String cluster, String collectionName) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set(CommonParams.ACTION, CdcrAction.STOP.toString());
    SolrRequest request = new QueryRequest(params);
    QueryResponse queryResponse = getQueryResponse(lightningContext.getSolrClient(cluster),
        collectionName, request);
    return new Response(HttpStatus.OK, "STOPPING CDCR",
        queryResponse.getResponse().get("status"));
  }

  public Response startCDCR(String cluster, String collectionName) {
    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set(CommonParams.ACTION, CdcrAction.START.toString());
    SolrRequest request = new QueryRequest(params);
    QueryResponse queryResponse = getQueryResponse(lightningContext.getSolrClient(cluster),
        collectionName, request);
    return new Response(HttpStatus.OK, "STARTING CDCR",
        queryResponse.getResponse().get("status"));
  }

  private QueryResponse getQueryResponse(SolrClient solrClient, String collectionName,
      SolrRequest request) {
    try {
      return new QueryResponse(solrClient.request(request, collectionName), solrClient);
    } catch (IOException | SolrServerException ex) {
      throw new SolrException(ex, ExceptionCode.SOLR_EXCEPTION, "CDCR  EXCEPTION OCCURED");
    }
  }
}
