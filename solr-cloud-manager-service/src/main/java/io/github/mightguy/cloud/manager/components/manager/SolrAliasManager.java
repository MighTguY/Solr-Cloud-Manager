package io.github.mightguy.cloud.manager.components.manager;

import io.github.mightguy.cloud.manager.components.SolrManagerContext;
import io.github.mightguy.cloud.manager.components.SolrManagerHelper;
import io.github.mightguy.cloud.manager.config.AppConfig;
import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrCommonsException;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.model.core.Alias;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import io.github.mightguy.cloud.manager.util.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SolrAliasManager {

  private final SolrManagerContext solrManagerContext;
  private final AppConfig appConfig;
  private final SolrManagerHelper solrManagerHelper;

  public SolrAliasManager(SolrManagerContext solrManagerContext, AppConfig appConfig,
      SolrManagerHelper solrManagerHelper) {
    this.solrManagerContext = solrManagerContext;
    this.appConfig = appConfig;
    this.solrManagerHelper = solrManagerHelper;
  }

  public void createAlias(String cluster, String collectionName, String alias) {
    try {
      log.info("Creating alias : " + alias + " for collection : " + collectionName);
      CollectionAdminRequest.createAlias(alias, collectionName)
          .process(solrManagerContext.getSolrClient(cluster));
      log.info("Alias: " + collectionName + " for collection: " + alias
          + " is successfully created");
    } catch (SolrServerException | IOException ex) {
      log.error("Exception during collection or alias creation. ", ex);
      throw new SolrCommonsException(ExceptionCode.UNABLE_TO_CREATE_COLLECTION_ALIAS, ex);
    }
  }

  public Collection<Alias> getAlias(String cluster) {
    return solrManagerContext.getSolrAlias(cluster);
  }

  public Collection<Alias> getAlias(String cluster, String collectionName) {
    Collection<Alias> aliases = new ArrayList<>();
    aliases.add(solrManagerContext.getSolrAlias(cluster, collectionName));
    aliases.add(solrManagerContext.getSolrAlias(cluster,
        collectionName.concat(appConfig.getAlias().getSuffix().getPassive())));
    aliases.add(solrManagerContext.getSolrAlias(cluster,
        collectionName.concat(appConfig.getAlias().getSuffix().getActive())));
    return aliases.stream().filter(Objects::nonNull).collect(Collectors.toList());
  }

  public Response deleteAllAliases(String cluster) {
    solrManagerContext.getSolrAlias(cluster).stream()
        .forEach(alias -> deleteAlias(cluster, alias.getAlias()));
    return new Response(HttpStatus.ACCEPTED, Constants.ALL_ALIASES_DELETED);
  }

  public Response deleteAlias(String cluster, String alias) {
    solrManagerHelper.deleteAlias(alias, solrManagerContext.getSolrClient(cluster));
    return new Response(HttpStatus.ACCEPTED, Constants.COLLECTION_ALIAS_DELETED);
  }

  public Collection<Alias> switchAlias(String cluster, String collectionName, boolean reload) {
    try {
      solrManagerContext.getSolrCollections(cluster);
      if (collectionName.equalsIgnoreCase(Constants.ALL_COLLECTIONS)) {
        for (String collection : solrManagerContext
            .getSolrCollectionNameWithoutSuffixes(cluster)) {
          runAliasSwitch(cluster, collection, reload, false);
        }
      } else {
        runAliasSwitch(cluster, collectionName, reload, false);
      }
      solrManagerContext.reloadContext(cluster);
    } catch (IOException | SolrServerException ex) {
      log.error("Exception while alias switching", ex);
      throw new SolrCommonsException(ex, ExceptionCode.ALIAS_SWITCH_FAILED, collectionName);
    }
    return getAlias(cluster, collectionName);
  }

  public void runAliasSwitch(String cluster, String collectionName, boolean reload,
      boolean originalCollectionName)
      throws IOException, SolrServerException {

    Alias shadowAlias = solrManagerContext.getSolrCollection(cluster, CloudInitializerUtils
        .collectionNameWithSuffix(collectionName, appConfig.getCollectionSuffix().getPassive(),
            originalCollectionName)).getAlias();
    Alias liveAlias = solrManagerContext.getSolrCollection(cluster, CloudInitializerUtils
        .collectionNameWithSuffix(collectionName, appConfig.getCollectionSuffix().getActive(),
            originalCollectionName)).getAlias();

    solrManagerHelper
        .verifyHealthyStateForClusterCollection(CloudInitializerUtils
                .checkCloudSolrClientInstance(solrManagerContext.getSolrClient(cluster)),
            shadowAlias.getCollections().getName());
    solrManagerHelper
        .verifyHealthyStateForClusterCollection(CloudInitializerUtils
                .checkCloudSolrClientInstance(solrManagerContext.getSolrClient(cluster)),
            liveAlias.getCollections().getName());

    log.info("Replacing existing live alias : {} to point on collection : {}.",
        liveAlias.getAlias(),
        shadowAlias.getCollections().getName());
    CollectionAdminRequest.createAlias(liveAlias.getAlias(), shadowAlias.getCollections().getName())
        .process(solrManagerContext.getSolrClient(cluster));
    log.info("Replacing existing live alias : {} to point on collection : {}.",
        shadowAlias.getAlias(),
        liveAlias.getCollections().getName());
    CollectionAdminRequest.createAlias(shadowAlias.getAlias(), liveAlias.getCollections().getName())
        .process(solrManagerContext.getSolrClient(cluster));

    if (reload) {
      log.info("Reloading collection {} against possibly new configuration set.", collectionName);
      solrManagerContext.getSolrClient(cluster)
          .request(CollectionAdminRequest.reloadCollection(liveAlias.getCollections().getName()));
    }
    log.info(
        "Successfully switched aliases for collection : {} .", collectionName);

  }

}
