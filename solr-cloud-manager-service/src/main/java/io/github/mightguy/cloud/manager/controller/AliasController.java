package io.github.mightguy.cloud.manager.controller;

import io.github.mightguy.cloud.manager.model.Response;

public interface AliasController {

  public Response getAliasesFromSolrForCollection(String cluster, String collectionName);

  public Response getAllAliasesFromSolr(String cluster);

  public Response aliasSwitch(String cluster, String collectionName, boolean reload);

  public Response aliasSwitchAll(String cluster, boolean reload);

  public Response deleteAliases(String cluster);

  public Response createAlias(String cluster, String collectionName, String alias);

  public Response deleteAlias(String cluster, String collectionName, String alias);
}
