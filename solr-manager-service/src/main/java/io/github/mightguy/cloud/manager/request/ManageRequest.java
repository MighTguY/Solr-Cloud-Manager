package io.github.mightguy.cloud.manager.request;

import java.util.HashMap;
import java.util.Map;

public class ManageRequest {

  protected String cluster;
  private String collection;
  protected boolean reload;
  protected Map<String, String> param = new HashMap<>();
}
