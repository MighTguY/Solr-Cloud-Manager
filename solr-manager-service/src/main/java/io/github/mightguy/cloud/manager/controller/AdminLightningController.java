
package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.constraints.ValidCluster;
import io.github.mightguy.cloud.manager.constraints.ValidCollectionName;
import io.github.mightguy.cloud.manager.constraints.ValidCollectionNameForAlias;
import io.github.mightguy.cloud.manager.constraints.ValidValue;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.service.LightningServiceFacade;
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
 * be responsible for performing 1. Cloud Initialize 2. Config update & reload collection 3. Alias
 * creation & switching
 */
@RestController
@Validated
@RequestMapping({"${microservice.contextPath}"})
public class AdminLightningController {

  @Autowired
  LightningServiceFacade lightningService;




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
  @PostMapping("/cloud/{cluster}/initialize")
  @ResponseStatus(HttpStatus.CREATED)
  public Response doInitializeSolrCloud(
      @ValidCluster @PathVariable("cluster") String cluster,
      @ValidValue @RequestParam(name = "git.user", required = false) String gitUser,
      @ValidValue @RequestParam(name = "git.password", required = false) String gitPassword,
      @ValidValue @RequestParam(name = "git.project", required = false) String gitBranch,
      @RequestParam(name = "git-pull", required = false) boolean override,
      @RequestParam(name = "delete_old_collections", required = false) boolean deleteOldCollections,
      @RequestParam(name = "upload_zk_config", required = false) boolean uploadZkConf) {

    return lightningService
        .initlializeCloudSolr(cluster, override, deleteOldCollections, uploadZkConf, gitUser,
            gitPassword, gitBranch);
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
  @PutMapping("/cloud/{cluster}/reload")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Response reloadCollection(@ValidCluster @PathVariable("cluster") String cluster,
      @ValidCollectionName @RequestParam(name = "collectionName") String collectionName,
      @RequestParam(name = "onlyShadow", required = false) boolean onlyShadowReload) {

    return lightningService.reloadCollection(cluster, collectionName, onlyShadowReload);
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
  @GetMapping("/cloud/{cluster}/collections")
  public Response getCollections(@ValidCluster @PathVariable("cluster") String cluster) {
    return lightningService.getCollections(cluster);
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
  @DeleteMapping("/cloud/{cluster}/deleteAll")
  public Response deleteAllCollections(@ValidCluster @PathVariable("cluster") String cluster) {
    return lightningService.deleteCollections(cluster);
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
  @DeleteMapping("/cloud/{cluster}/{collection}/delete")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Response deleteCollection(@ValidCluster @PathVariable("cluster") String cluster,
      @ValidCollectionName @PathVariable("collection") String collection
  ) {

    return lightningService.deleteCollection(cluster, collection);
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
  @GetMapping("/cloud/clusters")
  public Response listAllClusters() {

    return lightningService.listAllClusters();
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
  @PostMapping("/cloud/{cluster}/config/{collectionName}/push")
  @ResponseStatus(HttpStatus.OK)
  public Response pushConfig(@ValidCluster @PathVariable("cluster") String cluster,
      @ValidCollectionNameForAlias @PathVariable("collectionName") String collectionName,
      @RequestParam(name = "reload", defaultValue = "false") boolean reload,
      @RequestParam(name = "configName") String configName,
      @RequestParam(name = "appendDir", required = false) String appendDir,
      @RequestBody Set<String> configLines
  ) {

    return lightningService
        .pushConfig(cluster, collectionName, reload, configName, configLines, appendDir);
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
  @DeleteMapping("/cloud/{cluster}/{collectionName}/deleteData")
  @ResponseStatus(HttpStatus.OK)
  public Response deleteData(@ValidCluster @PathVariable("cluster") String cluster,
      @ValidCollectionName @PathVariable("collectionName") String collectionName,
      @RequestParam(name = "commit", defaultValue = "false") boolean commit) {
    return lightningService.deleteCollectionData(cluster, collectionName, commit);
  }

}
