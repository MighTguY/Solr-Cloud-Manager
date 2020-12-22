package io.github.mightguy.cloud.manager.model.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Alias {

  private String alias;
  private SolrCollection collections;
  private boolean isActive;
  private boolean isPassive;
}
