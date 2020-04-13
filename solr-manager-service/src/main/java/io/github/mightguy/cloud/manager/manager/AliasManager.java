
package io.github.mightguy.cloud.manager.manager;

import static io.github.mightguy.cloud.manager.util.Constants.FIRST_COLLECTION_SUFFIX;
import static io.github.mightguy.cloud.manager.util.Constants.RESTORE_COLLECTION_SUFFIX;
import static io.github.mightguy.cloud.manager.util.Constants.SECOND_COLLECTION_SUFFIX;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrCloudException;
import io.github.mightguy.cloud.manager.exception.SolrException;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import io.github.mightguy.cloud.manager.util.Constants;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.NoOpResponseParser;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.common.params.CollectionParams.CollectionAction;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


/**
 * This class {@code AliasManager} is responsible for managing all the Aliases related operations.
 * 1. Creating alias 2. Switching alias
 */
@Slf4j
@Component
public class AliasManager {

  @Autowired
  LightningContext lightningContext;

  /**
   * This method is responsible for running alias switch for a specific collection
   */
  public void runAliasSwitch(String cluster, String collectionName, boolean reload)
      throws IOException, SolrServerException {

    String liveCollectionAliasName =
        collectionName + lightningContext.getSolrConfigruationProperties().getSuffix().getActive();
    String shadowCollectionAliasName =
        collectionName + lightningContext.getSolrConfigruationProperties().getSuffix().getPassive();
    String oldShadowCollectionName = lightningContext.getSolrAliasToCollectionMap(cluster)
        .get(shadowCollectionAliasName);
    String newShadowCollectionName = lightningContext.getSolrAliasToCollectionMap(cluster)
        .get(liveCollectionAliasName);

    verifyHealthyStateForCluseterCollection(cluster, oldShadowCollectionName);
    verifyHealthyStateForCluseterCollection(cluster, newShadowCollectionName);

    log.info("Replacing existing live alias : {} to point on collection : {}.",
        liveCollectionAliasName,
        oldShadowCollectionName);
    CollectionAdminRequest.createAlias(liveCollectionAliasName, oldShadowCollectionName)
        .process(lightningContext.getSolrClient(cluster));
    //at this moment both shadow and live alias are pointing to the same new live collection.
    log.info("Replacing existing shadow alias : {} to point on collection : {}.",
        shadowCollectionAliasName,
        newShadowCollectionName);
    CollectionAdminRequest.createAlias(shadowCollectionAliasName, newShadowCollectionName)
        .process(lightningContext.getSolrClient(cluster));
    if (reload) {
      log.info("Reloading collection {} against possibly new configuration set.", collectionName);
      lightningContext.getSolrClient(cluster)
          .request(CollectionAdminRequest.reloadCollection(newShadowCollectionName));
    }
    log.info(
        "Successfully switched aliases for collection : {} .", collectionName);
  }

  /**
   * This method is responsible for running alias switch for all the solr collections
   */
  public void switchAliasAll(String cluster, boolean reload)
      throws IOException, SolrServerException {

    log.info("Alias switching started.");
    Set<String> congigNames = lightningContext.getCollectionList(cluster)
        .parallelStream()
        .map(s ->
            s.replaceAll(FIRST_COLLECTION_SUFFIX, "")
                .replaceAll(SECOND_COLLECTION_SUFFIX, "")
                .replaceAll(RESTORE_COLLECTION_SUFFIX, ""))
        .collect(Collectors.toSet());
    for (String configSetName : congigNames) {
      runAliasSwitch(cluster, configSetName, reload);
    }
  }

  /**
   * This method is responsible for creating alias  for all the solr collections. 1. Collection
   * ending with _A , will become Live. 2. Collection ending with _B, will become Shadow.
   */
  public void createAlias(String cluster, List<String> collections) {
    try {
      for (String collection : collections) {
        String collectionAlias = CloudInitializerUtils
            .getCurrentAliasSuffix(cluster, lightningContext, collection);
        if (lightningContext.getSolrAliasToCollectionMap(cluster).containsKey(collectionAlias)) {
          log.warn("Alias already present " + collectionAlias);
          continue;
        }
        log.info("Creating alias : " + collectionAlias + " for collection : " + collectionAlias);
        CollectionAdminRequest.createAlias(collectionAlias, collection)
            .process(lightningContext.getSolrClient(cluster));
        log.info("Alias: " + collectionAlias + " for collection: " + collectionAlias
            + " is successfully created");
      }
    } catch (SolrServerException | IOException ex) {
      log.error("Exception during collection or alias creation. ", ex);
      throw new SolrCloudException(ExceptionCode.UNABLE_TO_CREATE_COLLECTION_ALIAS, ex);
    }
  }

  private void verifyHealthyStateForCluseterCollection(String cluster, String collectionName)
      throws SolrServerException, IOException {

    NoOpResponseParser responseParser = new NoOpResponseParser();
    responseParser.setWriterType("json");
    ResponseParser responseParserOrignal = ((CloudSolrClient) lightningContext
        .getSolrClient(cluster)).getParser();
    ((CloudSolrClient) lightningContext.getSolrClient(cluster)).setParser(responseParser);
    CollectionAdminRequest.ClusterStatus collectionAdminRequest =
        new CollectionAdminRequest.ClusterStatus();
    String str = (String) lightningContext.getSolrClient(cluster).request(collectionAdminRequest)
        .get("response");
    ((CloudSolrClient) lightningContext.getSolrClient(cluster)).setParser(responseParserOrignal);
    DocumentContext jsonContext = JsonPath.parse(str,
        Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build());

    JSONArray jsonArray = jsonContext
        .read("$.cluster.collections." + collectionName + ".shards.*.replicas.*.state");

    for (Object obj : jsonArray) {
      if (!((String) obj).equals("active")) {
        log.info("Cluster not in healthy State cluster : {}  -> collection : {}.",
            cluster,
            collectionName);
        throw new SolrCloudException(
            ExceptionCode.CLUSTER_UNHEALTHY);
      }
    }
  }

  public Response deleteAllAliases(String cluster) {
    lightningContext.getSolrAliasToCollectionMap(cluster).keySet()
        .forEach(alias -> deleteAlias(cluster, alias));
    return new Response(HttpStatus.ACCEPTED, Constants.ALL_ALIASES_DELETED);
  }

  private void deleteAlias(String cluster, String alias) {
    try {
      if (!lightningContext.getSolrAliasToCollectionMap(cluster).containsKey(alias)) {
        return;
      }
      ModifiableSolrParams params = new ModifiableSolrParams();
      params.set("name", alias);
      params.set("action", CollectionAction.DELETEALIAS.toString());
      QueryRequest request = new QueryRequest(params);
      request.setPath("/admin/collections");
      lightningContext.getSolrClient(cluster).request(request);
    } catch (IOException | SolrServerException ex) {
      throw new SolrException(ex, ExceptionCode.SOLR_EXCEPTION,
          "Alias Deletion Failed [" + alias + "]");
    }
  }


  public Response createAliasForCollection(String cluster, String collection, String aliasName) {
    try {
      CollectionAdminRequest.createAlias(aliasName, collection)
          .process(lightningContext.getSolrClient(cluster));
      return new Response(HttpStatus.CREATED,
          "ALIAS Created " + aliasName + " for collection " + collection);
    } catch (SolrServerException | IOException ex) {
      log.error("Exception during collection or alias creation. ", ex);
      throw new SolrCloudException(ExceptionCode.UNABLE_TO_CREATE_COLLECTION_ALIAS, ex);
    }
  }
}
