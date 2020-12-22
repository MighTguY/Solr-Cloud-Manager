package io.github.mightguy.cloud.manager.model.core;

import lombok.Data;

@Data
public class SolrCollection {

  public SolrCollection(String name) {
    this.name = name;
  }

  private String name;
  private Alias alias;
}
