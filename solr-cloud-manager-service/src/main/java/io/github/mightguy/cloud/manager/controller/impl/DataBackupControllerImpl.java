package io.github.mightguy.cloud.manager.controller.impl;

import io.github.mightguy.cloud.manager.controller.DataBackupController;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.service.DataBackupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
public class DataBackupControllerImpl implements DataBackupController {

  private final DataBackupService service;

  public DataBackupControllerImpl(DataBackupService service) {
    this.service = service;
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
  @Override
  public Response backupAll(@PathVariable("cluster") String cluster) {
    return service.backUpAllCollection(cluster);
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
  @Override
  public Response backupCollection(@PathVariable("cluster") String cluster,
      @PathVariable("collectionName") String collectionName) {
    return service.backUpCollection(cluster, collectionName);
  }

  @ApiOperation(
      value = "API for restoring  al SOLR collections from a repo",
      notes =
          "For a given Repo name, restore all the collections",
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
  @Override
  public Response restoreAllCollections(@PathVariable("cluster") String cluster,
      @RequestParam(name = "repo") String repo,
      @RequestParam(name = "delete_original", defaultValue = "false") boolean deleteOriginal,
      @RequestParam(name = "suffix", required = false, defaultValue = "restored") String suffix) {
    return service.restoreAllBackups(cluster, repo, deleteOriginal, suffix);
  }

  @ApiOperation(
      value = "API for restoring backup of a SOLR collection",
      notes =
          "For a given CollectionName, restore the collection from the mentioned repo",
      response = Response.class)
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 200, response = Response.class, message = "")
      })
  @PostMapping("/restore/{cluster}")
  @Override
  public Response restoreCollection(
      @PathVariable("cluster") String cluster,
      @RequestParam("backup_name") String backupName,
      @RequestParam(name = "repo") String repo,
      @RequestParam(name = "delete_original", defaultValue = "false") boolean deleteOriginal,
      @RequestParam(name = "suffix", required = false, defaultValue = "restored") String suffix) {
    return service.restoreForBackup(cluster, backupName, repo, deleteOriginal, suffix);
  }

  @ApiOperation(
      value = "API for listing all  SOLR backups ",
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
  @Override
  public Response listAllBackup(@PathVariable("cluster") String cluster) {
    return service.listAllBackUp(cluster);
  }
}
