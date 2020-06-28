package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.service.DataaManagerService;
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
 * The class {@code DataManagerController} is the Controller  for the Solr Cloud Data backup and
 * restore core level, This will be responsible for performing Data management
 */
@Api(
    tags = {"Data  Management"}
)
@RestController
@Validated
@RequestMapping({"${microservice.contextPath}" + "/data-manager"})
public class DataManagerController {

  @Autowired
  DataaManagerService dataaManagerService;


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
  @PostMapping("/backup/{cluster}/all")
  public Response backupAll(@PathVariable("cluster") String cluster) {

    return dataaManagerService.backUpAllCollection(cluster);
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
  @PostMapping("/backup/{cluster}/{collectionName}")
  public Response backupCollection(@PathVariable("cluster") String cluster,
      @PathVariable("collectionName") String collectionName) {

    return dataaManagerService.backUpCollection(cluster, collectionName);
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
  @PostMapping("/restore/{cluster}/all")
  public Response restoreAll(@PathVariable("cluster") String cluster,
      @RequestParam(name = "repo") String repo,
      @RequestParam(name = "deleteOrignal", defaultValue = "false") boolean deleteOrignal,
      @RequestParam(name = "suffix", required = false, defaultValue = "restored") String suffix
  ) {

    return dataaManagerService.restoreAllCollection(cluster, repo, deleteOrignal, suffix);
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
  @PostMapping("/restore/{cluster}/{collectionName}")
  public Response restoreCollection(
      @PathVariable("collectionName") String collectionName,
      @PathVariable("cluster") String cluster,
      @RequestParam(name = "repo") String repo,
      @RequestParam(name = "deleteOrignal", defaultValue = "false") boolean deleteOrignal,
      @RequestParam(name = "suffix", required = false, defaultValue = "restored") String suffix) {

    return dataaManagerService
        .restoreCollection(cluster, repo, collectionName, deleteOrignal, suffix);
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
  @GetMapping("/{cluster}/backup")
  public Response listAllbackup(@PathVariable("cluster") String cluster) {

    return dataaManagerService.listAllBackUp(cluster);
  }
}