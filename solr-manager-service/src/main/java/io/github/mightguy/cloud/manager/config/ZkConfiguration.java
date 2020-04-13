package io.github.mightguy.cloud.manager.config;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@Data
public class ZkConfiguration {
  private String zkUrl;
  private String chRoot;
  private Integer zkConnectTimeout;
  private Integer zkClientTimeout;
  private Integer solrSOTimeout;
}
