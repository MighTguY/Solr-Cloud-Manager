package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.constraints.ValidCluster;
import io.github.mightguy.cloud.manager.constraints.ValidCollectionName;
import io.github.mightguy.cloud.manager.constraints.ValidCollectionNameForAlias;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.service.AliasManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The class {@code AliasmanagerController} is the Controller  for the Solr Cloud Aliases, This will
 * be responsible for performing  Alias creation, deletion & switching
 */
@RestController
@Validated
@Api(
    tags = {"Alias Management"}
)
@RequestMapping("/alias")
public class AliasManagerController {

  @Autowired
  private AliasManagerService aliasManagerService;

  @ApiOperation(
      value = "API for fetching  alias mapping for SOLR collection",
      notes =
          "For a given CollectionName, fetch both live and shadow aliases",
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
  @GetMapping("/cloud/{cluster}/{collectionName}")
  public Response getAliasesFromSolrForCollection(
      @ValidCluster @PathVariable("cluster") String cluster,
      @ValidCollectionNameForAlias @PathVariable("collectionName") String collectionName) {

    return aliasManagerService.getAlias(cluster, collectionName);
  }

  @ApiOperation(
      value = "API for fetching  alias mapping for all SOLR collections",
      notes =
          "For a given cluster, fetch both live and shadow aliases",
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
  @GetMapping("/cloud/{cluster}")
  public Response getAllAliasesFromSolr(@ValidCluster @PathVariable("cluster") String cluster) {
    return aliasManagerService.getAlias(cluster, "all");
  }

  @ApiOperation(
      value = "API for switching  alias mapping for SOLR collection",
      notes =
          "For a given CollectionName, switch both live and shadow aliases"
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
  @PutMapping("/cloud/{cluster}/{collectionName}/switch")
  @ResponseStatus(HttpStatus.OK)
  public Response aliasSwtich(@ValidCluster @PathVariable("cluster") String cluster,
      @ValidCollectionNameForAlias @PathVariable("collectionName") String collectionName,
      @RequestParam(name = "reload", defaultValue = "true") boolean reload
  ) {

    return aliasManagerService.switchAlias(cluster, collectionName, reload);
  }

  @ApiOperation(
      value = "API for switching  alias mapping for all SOLR collections",
      notes =
          "For all Collections, switch both live and shadow aliases",

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
  @PutMapping("/cloud/{cluster}/switch")
  @ResponseStatus(HttpStatus.OK)
  public Response aliasSwtichAll(@ValidCluster @PathVariable("cluster") String cluster,
      @RequestParam(name = "reload", defaultValue = "false") boolean reload
  ) {

    return aliasManagerService.switchAlias(cluster, "all", reload);
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
  @DeleteMapping("/cloud/{cluster}/deleteAll")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Response deleteAliases(@ValidCluster @PathVariable("cluster") String cluster) {

    return aliasManagerService.deleteAllAliases(cluster);
  }

  @ApiOperation(
      value = "API for Creating an Alias for a SOLR collection",
      notes =
          "For a given CollectionName, create  the alias for  collections",
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
  @DeleteMapping("/cloud/{cluster}/{collectionName}/create")
  @ResponseStatus(HttpStatus.CREATED)
  public Response createAlias(@ValidCluster @PathVariable("cluster") String cluster,
      @ValidCollectionName @PathVariable("collectionName") String collectionName,
      @RequestParam(name = "alias", required = true) String alias) {

    return aliasManagerService.createAlias(cluster, collectionName, alias);
  }


}
