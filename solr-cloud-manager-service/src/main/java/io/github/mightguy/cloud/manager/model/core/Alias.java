package io.github.mightguy.cloud.manager.model.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Alias {

  private String alias;
  private String collection;
  private boolean isActive;
  private boolean isPassive;
}
