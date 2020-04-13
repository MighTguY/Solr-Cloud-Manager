
package io.github.mightguy.cloud.manager.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class SolrCollection {
  String collectionName;
  Map<String, String> aliasesVsCollectionMap;

  public SolrCollection(String collectionName) {
    this.collectionName = collectionName;
    aliasesVsCollectionMap = new HashMap<>();
  }
}
