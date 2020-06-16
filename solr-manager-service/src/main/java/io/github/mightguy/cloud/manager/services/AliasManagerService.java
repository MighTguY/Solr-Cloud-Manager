package io.github.mightguy.cloud.manager.services;

import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.model.Response;
import java.util.Map;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliasManagerService {

  @Autowired
  private LightningContext lightningContext;

  public Response deleteAllAliases(String cluster) {
    return null;
  }

  public Response createAlias(String cluster, String collectionName, String alias) {
    return null;
  }

  public Response switchAlias(String cluster, String all, boolean reload) {
    return null;
  }

  public Response getAlias(String cluster, String collection) {
    lightningContext.reloadAliasMap(cluster);
    Map<String, String> aliasMap = lightningContext.getSolrCollectionToAliasClusterMap()
        .get(collection);
  }
}
