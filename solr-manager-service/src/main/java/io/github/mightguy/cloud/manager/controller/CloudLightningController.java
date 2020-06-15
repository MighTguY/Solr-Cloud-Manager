package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.model.InitializerConfig;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.services.CloudManagerService;
import io.github.mightguy.cloud.manager.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
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
 * The class {@code CloudLightningController} is the Controller facade for the Solr Cloud, This will
 * be responsible for performing Cloud management
 */
@Api(tags = {"Solr Cloud Core Management"})
@RestController
@Validated
@RequestMapping({"/cloud"})
public class CloudLightningController {

  @Autowired
  CloudManagerService cloudManagerService;

  @ApiOperation(
      value = "API for initilaising SOLR cloud collections",
      notes =
          "For a given SolrCloud, fetch the configurations "
              + "and upload them to cloud, and also reload all indexes",
      code = 200,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message =
              "Solr Cloud Initialization is Completed")
      })
  @PostMapping("/{cluster}/initialize")
  @ResponseStatus(HttpStatus.CREATED)
  public Response doInitializeSolrCloud(
      @PathVariable("cluster") String cluster,
      InitializerConfig initializerConfig) {

    initializerConfig.setCluster(cluster);
    return cloudManagerService.initialize(initializerConfig);
  }

  @ApiOperation(
      value = "API for initilaising SOLR cloud collection",
      notes =
          "For a given SolrCloud, fetch the configurations "
              + "and upload them to cloud, and also reload all indexes of that collection",
      code = 200,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message =
              "Solr Cloud Initialization is Completed")
      })
  @PostMapping("/{cluster}/{collection}/initialize")
  @ResponseStatus(HttpStatus.CREATED)
  public Response doInitializeSolrCollection(
      @PathVariable("cluster") String cluster,
      @PathVariable("collection") String collection,
      InitializerConfig initializerConfig) {
    return cloudManagerService.initializeCollection(initializerConfig);
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
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @PutMapping("/{cluster}/reload")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Response reloadCollection(@PathVariable("cluster") String cluster,
      @RequestParam(name = "collectionName") String collectionName,
      @RequestParam(name = "onlyShadow", required = false) boolean onlyShadowReload) {

    return cloudManagerService.reload(cluster);
  }

  @ApiOperation(
      value = "API for getting all the  SOLR collections",
      notes =
          "For a given CollectionName, get all the active and passive collection"
              + "based on the flag onlyShadow, by default both will get reloaded",
      code = 202,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @GetMapping("/{cluster}/collections")
  public Response getCollections(@PathVariable("cluster") String cluster) {
    return cloudManagerService.getCollections(cluster);
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
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 202, response = Response.class, message = "All Collections Deleted")
      })
  @ResponseStatus(HttpStatus.ACCEPTED)
  @DeleteMapping("/{cluster}/deleteAll")
  public Response deleteAllCollections(@PathVariable("cluster") String cluster) {
    return cloudManagerService.deleteCollection(cluster, Constants.ALL);
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
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 202, response = Response.class, message = "")
      })
  @DeleteMapping("/{cluster}/{collection}/delete")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Response deleteCollection(@PathVariable("cluster") String cluster,
      @PathVariable("collection") String collection
  ) {

    return cloudManagerService.deleteCollection(cluster, collection);
  }


  @ApiOperation(
      value = "API for listing all  SOLR Clusters ",
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
  @GetMapping("/clusters")
  public Response listAllClusters() {

    return cloudManagerService.listAllClusters();
  }


  @ApiOperation(
      value = "API for pushing config file data for SOLR collection",
      notes =
          "For a given CollectionName, push to  both live and shadow aliases"
              + "for information on all Collection Aliases, use all in collecionName",
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
  @PostMapping("/{cluster}/config/{collectionName}/push")
  @ResponseStatus(HttpStatus.OK)
  public Response pushConfig(@PathVariable("cluster") String cluster,
      @PathVariable("collectionName") String collectionName,
      @RequestParam(name = "reload", defaultValue = "false") boolean reload,
      @RequestParam(name = "configName") String configName,
      @RequestParam(name = "appendDir", required = false) String appendDir,
      @RequestBody Set<String> configLines
  ) {

    return null;
  }

  @ApiOperation(
      value = "API for deleting data from SOLR collection",
      notes =
          "For a given CollectionName, delete data from the "
              + "collecion of a cluster",
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
  @DeleteMapping("/{cluster}/{collectionName}/deleteData")
  @ResponseStatus(HttpStatus.OK)
  public Response deleteData(@PathVariable("cluster") String cluster,
      @PathVariable("collectionName") String collectionName,
      @RequestParam(name = "commit", defaultValue = "false") boolean commit) {
    return cloudManagerService.deleteCollectionData(cluster, collectionName, commit);
  }

//
//  @ApiOperation(
//      value = "API for Change Config data from SOLR collection",
//      notes =
//          "For a given CollectionName, change config from the "
//              + "collecion of a cluster",
//      code = 200,
//      response = Response.class)
//  @ApiResponses(
//      value = {
//          @ApiResponse(
//              code = 400,
//              message = "Solr Cloud Manager exceptions",
//              response = Response.class),
//          @ApiResponse(code = 200, response = Response.class, message = "")
//      })
//  @PostMapping("/{cluster}/{collectionName}/uploadConfig")
//  @ResponseStatus(HttpStatus.OK)
//  public Response changeConfigSet(@PathVariable("cluster") String cluster,
//      @PathVariable("collectionName") String collectionName,
//      @RequestParam(name = "configName") String configName,
//      @RequestParam(name = "configValue") String value) {
//    return cloudManagerService.deleteCollectionData(cluster, collectionName, commit);;
//  }

}