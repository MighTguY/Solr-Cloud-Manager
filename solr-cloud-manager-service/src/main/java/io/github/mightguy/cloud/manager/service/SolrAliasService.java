package io.github.mightguy.cloud.manager.service;

import io.github.mightguy.cloud.manager.components.manager.SolrAliasManager;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SolrAliasService {

  private final SolrAliasManager solrAliasManager;

  public SolrAliasService(SolrAliasManager solrAliasManager) {
    this.solrAliasManager = solrAliasManager;
  }

  public Response createAlias(String cluster, String collectionName, String alias) {
    solrAliasManager.createAlias(cluster, collectionName, alias);
    return new Response(HttpStatus.CREATED, "Alias Creation Successful");
  }

  public Response getAlias(String cluster, String collectionName) {
    Response response = new Response(HttpStatus.OK, null);
    if (Constants.ALL_COLLECTIONS.equalsIgnoreCase(collectionName)) {
      response.setResp(solrAliasManager.getAlias(cluster));
    } else {
      response.setResp(solrAliasManager.getAlias(cluster, collectionName));
    }
    return response;
  }

  public Response deleteAllAliases(String cluster) {
    return solrAliasManager.deleteAllAliases(cluster);
  }

  public Response deleteAlias(String cluster, String alias) {
    return solrAliasManager.deleteAlias(cluster, alias);
  }

  public Response switchAlias(String cluster, String collectionName, boolean reload) {
    return new Response(HttpStatus.OK, "Alias Creation Successful",
        solrAliasManager.switchAlias(cluster, collectionName, reload));
  }

  public void refresh() {
    solrAliasManager.refresh();
  }
}
