package io.github.mightguy.cloud.manager.controller.impl;

import io.github.mightguy.cloud.manager.controller.AliasController;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.service.SolrAliasService;
import io.github.mightguy.cloud.manager.util.Constants;
import io.swagger.annotations.Api;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The class {@code AliasControllerImpl} is the Controller  for the Solr Cloud Aliases, This will be
 * responsible for performing  Alias creation, deletion & switching
 */

@RestController
@Validated
@Api(
    tags = {"Alias Management"}
)
@RequestMapping({"${microservice.contextPath}" + "/alias"})
public class AliasControllerImpl implements AliasController {

  final SolrAliasService solrAliasService;

  public AliasControllerImpl(SolrAliasService solrAliasService) {
    this.solrAliasService = solrAliasService;
  }

  @ApiOperation(
      value = "API for fetching  alias mapping for SOLR collection",
      notes =
          "For a given CollectionName, fetch both live and shadow aliases",
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
  @GetMapping("/{cluster}/{collectionName}")
  @Override
  public Response getAliasesFromSolrForCollection(
      @PathVariable("cluster") String cluster,
      @PathVariable("collectionName") String collectionName) {
    return solrAliasService.getAlias(cluster, collectionName);
  }

  @ApiOperation(
      value = "API for fetching  alias mapping for all SOLR collections",
      notes =
          "For a given cluster, fetch both live and shadow aliases",
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
  @GetMapping("/{cluster}")
  @Override
  public Response getAllAliasesFromSolr(@PathVariable("cluster") String cluster) {
    return solrAliasService.getAlias(cluster, Constants.ALL_COLLECTIONS);
  }

  @ApiOperation(
      value = "API for switching  alias mapping for SOLR collection",
      notes =
          "For a given CollectionName, switch both live and shadow aliases"
              + "for information on all Collection Aliases, use all in collecionName",
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
  @PutMapping("/{cluster}/{collectionName}/switch")
  @ResponseStatus(HttpStatus.OK)
  @Override
  public Response aliasSwitch(
      @PathVariable("collectionName") String cluster,
      @PathVariable("cluster") String collectionName,
      @RequestParam(name = "reload", defaultValue = "true") boolean reload) {
    return null;
  }

  @ApiOperation(
      value = "API for switching  alias mapping for all SOLR collections",
      notes =
          "For all Collections, switch both live and shadow aliases",

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
  @PutMapping("/{cluster}/switch")
  @ResponseStatus(HttpStatus.OK)
  @Override
  public Response aliasSwitchAll(
      @PathVariable("cluster") String cluster,
      @RequestParam(name = "reload", defaultValue = "false") boolean reload) {
    return solrAliasService.switchAlias(cluster, Constants.ALL_COLLECTIONS, reload);
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
  @DeleteMapping("/{cluster}/deleteAll")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Override
  public Response deleteAliases(@PathVariable("cluster") String cluster) {
    return solrAliasService.deleteAllAliases(cluster);
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
              code = 500,
              message = "Internal server error",
              response = Response.class),
          @ApiResponse(
              code = 400,
              message = "Solr Cloud Manager exceptions",
              response = Response.class),
          @ApiResponse(code = 201, response = Response.class, message = "")
      })
  @PostMapping("/{cluster}/{collectionName}/create")
  @ResponseStatus(HttpStatus.CREATED)
  @Override
  public Response createAlias(
      @PathVariable("cluster") String cluster,
      @PathVariable("collectionName") String collectionName,
      @RequestParam(name = "alias") String alias) {
    return solrAliasService.createAlias(cluster, collectionName, alias);
  }

  @ApiOperation(
      value = "API for Deleting an Alias for a SOLR collection",
      notes =
          "For a given CollectionName, delete  the alias for  collections",
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
  @DeleteMapping("/{cluster}/{collectionName}/delete")
  @ResponseStatus(HttpStatus.CREATED)
  @Override
  public Response deleteAlias(
      @PathVariable("cluster") String cluster,
      @PathVariable("collectionName") String collectionName,
      @RequestParam(name = "alias") String alias) {
    return solrAliasService.deleteAlias(cluster, alias);
  }
}
