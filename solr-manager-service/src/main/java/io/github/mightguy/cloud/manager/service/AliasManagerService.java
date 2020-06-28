
package io.github.mightguy.cloud.manager.service;

import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.manager.AliasManager;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.util.Constants;
import io.github.mightguy.cloud.solr.commons.exception.ExceptionCode;
import io.github.mightguy.cloud.solr.commons.exception.SolrException;
import io.github.mightguy.cloud.solr.commons.exception.UnknownCollectionException;
import java.io.IOException;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * This Service class {@code AliasManagerService} is responsible for propogating all the Alias
 * Opeartions to the AliasManager
 */
@Slf4j
@Service
public class AliasManagerService {

  @Autowired
  LightningContext lightningContext;

  @Autowired
  AliasManager aliasManager;

  /**
   * Method to getAliases for either all collections  or a specific collection
   *
   * @return {@link Response}
   */
  public Response getAlias(String cluster, String collectionName) {
    Response response = new Response(HttpStatus.OK, null);
    if (Constants.ALL_COLLECTIONS.equalsIgnoreCase(collectionName)) {
      response.setResp(aliasManager.fetchAllCollectionAliases(cluster));
    } else {
      response.setResp(getAliasForCollection(cluster, collectionName));
    }
    return response;
  }

  /**
   * Method to switch alias on the input  collectionName, It can have either a collectionName, or
   * {@code Constants.ALL_COLLECTIONS}
   *
   * @return {@link Response}
   */
  public Response switchAlias(String cluster, String collectionName, boolean reload) {
    try {
      HashMap aliasMap = ((HashMap) aliasManager.fetchAllCollectionAliases(cluster));
      if (collectionName.equalsIgnoreCase(Constants.ALL_COLLECTIONS)) {
        aliasManager.switchAliasAll(cluster, reload);
      } else if (aliasMap.containsKey(collectionName)) {
        aliasManager.runAliasSwitch(cluster, collectionName, reload);
      } else {
        throw new UnknownCollectionException(ExceptionCode.UNKNOWN_COLLECTION, collectionName);
      }
      lightningContext.reload(cluster);
    } catch (IOException | SolrServerException ex) {
      log.error("Exception while alias switching", ex);
      throw new SolrException(ex, ExceptionCode.ALIAS_SWITCH_FAILED, collectionName);
    }
    return getAlias(cluster, collectionName);
  }

  /**
   * Get Alias for the  collectionN
   *
   * @return {@code SolrCollection}
   */
  private Object getAliasForCollection(String cluster, String collectionName) {
    HashMap aliasMap = ((HashMap) aliasManager.fetchAllCollectionAliases(cluster));
    if (aliasMap.containsKey(collectionName)) {
      return aliasMap.get(collectionName);
    } else {
      throw new UnknownCollectionException(ExceptionCode.UNKNOWN_COLLECTION, collectionName);
    }
  }


  public Response createAlias(String cluster, String collectionName, String alias) {
    aliasManager.createAlias(cluster, collectionName, alias);
    return new Response(HttpStatus.CREATED, "Alias Creation Successfull");
  }

  public Response deleteAllAliases(String cluster) {
    aliasManager.deleteAlias(cluster);
    return new Response(HttpStatus.ACCEPTED, "Alias Deletion Successfull");
  }

  public Response deleteAlias(String cluster, String collectionName, String alias) {
    aliasManager.deleteAlias(cluster, collectionName);
    return new Response(HttpStatus.ACCEPTED, "Alias Deletion Successfull");
  }
}
