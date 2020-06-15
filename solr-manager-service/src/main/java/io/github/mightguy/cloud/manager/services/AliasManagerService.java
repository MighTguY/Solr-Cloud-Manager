package io.github.mightguy.cloud.manager.services;

import io.github.mightguy.cloud.manager.model.Response;
import org.springframework.stereotype.Service;

@Service
public class AliasManagerService {

  public Response deleteAllAliases(String cluster) {
    return null;
  }

  public Response createAlias(String cluster, String collectionName, String alias) {
    return null;
  }

  public Response switchAlias(String cluster, String all, boolean reload) {
    return null;
  }

  public Response getAlias(String cluster, String all) {
    return null;
  }
}
