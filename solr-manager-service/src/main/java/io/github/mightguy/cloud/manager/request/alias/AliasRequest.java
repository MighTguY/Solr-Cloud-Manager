package io.github.mightguy.cloud.manager.request.alias;

import io.github.mightguy.cloud.manager.request.ManageRequest;
import lombok.Data;

@Data
public class AliasRequest extends ManageRequest {

  private AliasAction aliasAction;
  private static final String COLLECTION_ALIAS_MAP = "collection_alias_map";
}
