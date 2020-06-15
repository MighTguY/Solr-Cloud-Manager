package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.model.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The class {@code CDCRmanagerController} is the Controller for the Solr Cloud CDCR, This will be
 * responsible for performing CDCR management
 */
@RestController
@Validated
@Api(
    tags = {"CDCR Management"}
)
@RequestMapping("/cdcr")
public class CdcrManagerController {


  @ApiOperation(
      value = "API for restoring Starting of a SOLR collection CDCR",
      notes =
          "For a given CollectionName, start the CDCR for the mentioned collection",
      code = 200,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @PostMapping("/{cluster}/{collectionName}/start")
  public Response startCDCR(
      @PathVariable("collectionName") String collectionName,
      @PathVariable("cluster") String cluster) {
    return null;
  }

  @ApiOperation(
      value = "API for restoring Stopping of a SOLR collection CDCR",
      notes =
          "For a given CollectionName, stop the CDCR for the mentioned collection",
      code = 200,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @PostMapping("/{cluster}/{collectionName}/stop")
  public Response stopCDCR(
      @PathVariable("collectionName") String collectionName,
      @PathVariable("cluster") String cluster) {
    return null;
  }


  @ApiOperation(
      value = "API for restoring ENABLEBUFFER of a SOLR collection CDCR",
      notes =
          "For a given CollectionName, ENABLEBUFFER the CDCR for the mentioned collection",
      code = 200,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @PostMapping("/{cluster}/{collectionName}/buffer/enable")
  public Response enableBufferOnCDCRUpdates(
      @PathVariable("collectionName") String collectionName,
      @PathVariable("cluster") String cluster) {
    return null;
  }


  @ApiOperation(
      value = "API for restoring disbale buffer of a SOLR collection CDCR",
      notes =
          "For a given CollectionName, disbale the CDCR buffer for the mentioned collection",
      code = 200,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @PostMapping("/{cluster}/{collectionName}/buffer/disbale")
  public Response disableBufferOnCDCRUpdates(
      @PathVariable("collectionName") String collectionName,
      @PathVariable("cluster") String cluster) {
    return null;
  }

  @ApiOperation(
      value = "API for fetching the status of a SOLR collection CDCR",
      notes =
          "For a given CollectionName, fetch status the CDCR  for the mentioned collection",
      code = 200,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @GetMapping("/{cluster}/{collectionName}/status")
  public Response getCurrentState(
      @PathVariable("collectionName") String collectionName,
      @PathVariable("cluster") String cluster) {
    return null;
  }

  @ApiOperation(
      value = "API for fetching the status of a SOLR collection CDCR",
      notes =
          "For a given CollectionName, fetch status the CDCR  for the mentioned collection",
      code = 200,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @GetMapping("/{cluster}/{collectionName}/status/errors")
  public Response monitorCDCRErrors(
      @PathVariable("collectionName") String collectionName,
      @PathVariable("cluster") String cluster) {
    return null;
  }

  @ApiOperation(
      value = "API for fetching the queue logs stats of a SOLR collection CDCR",
      notes =
          "For a given CollectionName, fetch queue  stats the CDCR  for the mentioned collection",
      code = 200,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @GetMapping("/{cluster}/{collectionName}/stats/queue")
  public Response monitorCDCRQueueStats(
      @PathVariable("collectionName") String collectionName,
      @PathVariable("cluster") String cluster) {
    return null;
  }

  @ApiOperation(
      value = "API for fetching the Replication logs stats of a SOLR collection CDCR",
      notes =
          "For a given CollectionName, fetch Replication logs stats the CDCR "
              + " for the mentioned collection",
      code = 200,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @GetMapping("/{cluster}/{collectionName}/stats/replication")
  public Response monitorCDCRReplicationLogs(
      @PathVariable("collectionName") String collectionName,
      @PathVariable("cluster") String cluster) {
    return null;
  }


}