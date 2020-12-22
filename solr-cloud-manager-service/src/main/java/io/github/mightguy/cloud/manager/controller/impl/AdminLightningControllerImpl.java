
package io.github.mightguy.cloud.manager.controller.impl;

import static io.github.mightguy.cloud.manager.util.Constants.CLUSTER_NAME;
import static io.github.mightguy.cloud.manager.util.Constants.COLLECTION_NAME;
import static io.github.mightguy.cloud.manager.util.Constants.DELETE_OLD_COLLECTIONS;
import static io.github.mightguy.cloud.manager.util.Constants.INITIALIZATION_TYPE;
import static io.github.mightguy.cloud.manager.util.Constants.UPLOAD_CONFIG_TO_ZOOKEEPER;

import io.github.mightguy.cloud.manager.controller.AdminLightningController;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.model.request.ClusterInitializationType;
import io.github.mightguy.cloud.manager.model.request.InitializationRequestDetails;
import io.github.mightguy.cloud.manager.service.AdminLightningService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The class {@code AdminLightningController} is the Controller facade for the Solr Cloud, This will
 * be responsible for performing 1. Cloud Initialize 2. Config update & reload collection 3. Alias
 * creation & switching
 */
@RestController
@Validated
@RequestMapping({"${microservice.contextPath}"})
public class AdminLightningControllerImpl implements AdminLightningController {

  final AdminLightningService lightningService;

  public AdminLightningControllerImpl(
      AdminLightningService lightningService) {
    this.lightningService = lightningService;
  }


  @ApiOperation(
      value = "API for initialising SOLR cloud collections from github/local repository",
      notes =
          "For a given SolrCloud, fetch the configurations "
              + "and upload them to cloud, and also reload all indexes",
      code = 201,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(
              code = 500,
              message = "Internal server error",
              response = Response.class),
          @ApiResponse(code = 201, response = Response.class, message =
              "Solr Cloud Initialization is Completed")
      })
  @PostMapping("/cloud/{cluster}/initialize")
  @ResponseStatus(HttpStatus.CREATED)
  public Response doInitializeSolrCloud(
      @PathVariable(CLUSTER_NAME) String cluster,
      @RequestParam(name = DELETE_OLD_COLLECTIONS, required = false) boolean deleteOldCollections,
      @RequestParam(name = UPLOAD_CONFIG_TO_ZOOKEEPER, required = false) boolean uploadZkConf,
      @RequestParam(name = INITIALIZATION_TYPE) ClusterInitializationType type,
      @RequestBody InitializationRequestDetails payload) {

    return lightningService
        .initializeCloud(cluster, deleteOldCollections, uploadZkConf, type, payload);
  }


  @ApiOperation(
      value = "API for reloading  SOLR collection",
      notes =
          "For a given CollectionName, reload all the active and passive collection"
              + "based on the flag onlyShadow, by default both will get reloaded",
      code = 202,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 500,
              message = "Internal server error",
              response = Response.class),
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @PutMapping("/cloud/{cluster}/{collection}/reload")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Response reloadCollection(@PathVariable(CLUSTER_NAME) String cluster,
      @PathVariable(name = COLLECTION_NAME) String collectionName,
      @RequestParam(name = "only_passive", required = false) boolean onlyPassiveReload) {

    return lightningService.reloadCollection(cluster, collectionName, onlyPassiveReload);
  }

  @ApiOperation(
      value = "API for reloading  SOLR collection",
      notes =
          "For a given CollectionName, reload all the active and passive collection"
              + "based on the flag onlyShadow, by default both will get reloaded",
      code = 202,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 500,
              message = "Internal server error",
              response = Response.class),
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @PutMapping("/cloud/{cluster}/reload")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Response reloadCluster(@PathVariable(CLUSTER_NAME) String cluster,
      @RequestParam(name = "only_passive", required = false) boolean onlyPassiveReload) {

    return lightningService.reloadCluster(cluster, onlyPassiveReload);
  }

  @ApiOperation(
      value = "API for getting SOLR collection",
      notes =
          "For a given CollectionName, reload all the active and passive collection"
              + "based on the flag onlyShadow, by default both will get reloaded",
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 500,
              message = "Internal server error",
              response = Response.class),
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @GetMapping("/cloud/{cluster}/collections")
  public Response getCollections(@PathVariable(CLUSTER_NAME) String cluster) {
    return lightningService.listCollections(cluster);
  }

  @ApiOperation(
      value = "API for deleting all  SOLR collections",
      notes =
          "For a given Cluster, delete all the active and passive collections",
      code = 202,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 500,
              message = "Internal server error",
              response = Response.class),
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 202, response = Response.class, message = "All Collections Deleted")
      })
  @ResponseStatus(HttpStatus.ACCEPTED)
  @DeleteMapping("/cloud/{cluster}/deleteAll")
  public Response deleteAllCollections(@PathVariable(CLUSTER_NAME) String cluster) {
    return lightningService.deleteCluster(cluster);
  }


  @ApiOperation(
      value = "API for deleting a SOLR collection",
      notes =
          "For a given CollectionName, delete  the active and passive collections",
      code = 202,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 500,
              message = "Internal server error",
              response = Response.class),
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 202, response = Response.class, message = "")
      })
  @DeleteMapping("/cloud/{cluster}/{collection}/delete")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Response deleteCollection(@PathVariable(CLUSTER_NAME) String cluster,
      @PathVariable("collection") String collection
  ) {
    return lightningService.deleteCollections(cluster, collection);
  }


  @ApiOperation(
      value = "API for listing all  SOLR Clusters ",
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @GetMapping("/cloud/clusters")
  public Response listAllClusters() {
    return lightningService.listClusters();
  }


  @ApiOperation(
      value = "API for deleting data from SOLR collection",
      notes =
          "For a given CollectionName, delete data from the "
              + "collection of a cluster",
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @DeleteMapping("/cloud/{cluster}/{collectionName}/deleteData")
  @ResponseStatus(HttpStatus.OK)
  public Response deleteData(@PathVariable(CLUSTER_NAME) String cluster,
      @PathVariable("collectionName") String collectionName,
      @RequestParam(name = "commit", defaultValue = "false") boolean commit) {
    return lightningService.deleteData(cluster, collectionName, commit);
  }

}
