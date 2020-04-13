package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.constraints.ValidCluster;
import io.github.mightguy.cloud.manager.constraints.ValidCollectionName;
import io.github.mightguy.cloud.manager.manager.DataBackupManagerService;
import io.github.mightguy.cloud.manager.model.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The class {@code DataBackupController} is the Controller  for the Solr Cloud Data backup and
 * restore, This will be responsible for performing Data management
 */
@Api(
    tags = {"Data Restore Management"}
)
@RestController
@Validated
@RequestMapping("backup")
public class DataBackupController {


  @Autowired
  DataBackupManagerService dataBackupManagerService;

  @ApiOperation(
      value = "API for backing up all SOLR collections",
      notes =
          "backup all  the active and passive collections",
      code = 201,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 201, response = Response.class, message = "")
      })
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/cloud/{cluster}/all")
  public Response backupAll(@ValidCluster @PathVariable("cluster") String cluster) {

    return dataBackupManagerService.createBackup(cluster);
  }

  @ApiOperation(
      value = "API for backing up a SOLR collection",
      notes =
          "For a given CollectionName, backup  the collection",
      code = 201,
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 201, response = Response.class, message = "")
      })
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/cloud/{cluster}/{collectionName}")
  public Response backupCollection(@ValidCluster @PathVariable("cluster") String cluster,
      @ValidCollectionName @PathVariable("collectionName") String collectionName) {

    return dataBackupManagerService.createBackup(cluster, collectionName, null, null);
  }

  @ApiOperation(
      value = "API for restoring  al SOLR collections from a repo",
      notes =
          "For a given Repo name, restore all the collections",
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
  @PostMapping("/restore/cloud/{cluster}/all")
  public Response restoreAll(@ValidCluster @PathVariable("cluster") String cluster,
      @RequestParam(name = "repo") String repo) {

    return dataBackupManagerService.restoreFullBackup(cluster, repo);
  }

  @ApiOperation(
      value = "API for restoring backup of a SOLR collection",
      notes =
          "For a given CollectionName, restore the collection from the mentioned repo",
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
  @PostMapping("/restore/cloud/{cluster}/{collectionName}")
  public Response restoreCollection(
      @ValidCollectionName @PathVariable("collectionName") String collectionName,
      @ValidCluster @PathVariable("cluster") String cluster,
      @RequestParam(name = "repo") String repo) {

    return dataBackupManagerService.restoreBackup(cluster, repo, collectionName);
  }

  @ApiOperation(
      value = "API for listing all  SOLR backups ",
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
  @GetMapping("/cloud/{cluster}/backup")
  public Response listAllbackup(@ValidCluster @PathVariable("cluster") String cluster) {

    return dataBackupManagerService.listBackups(cluster);
  }


}
