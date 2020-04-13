package io.github.mightguy.cloud.manager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InitializerConfig {

  private String gitUser;
  private String gitPassword;
  private String folder;
  private String cluster;
  private String gitBranch;

  @Builder.Default
  private String collection = "ALL";

  private boolean override;
  private boolean deleteOldCollections;
  private boolean uploadZkConf;

  @Builder.Default
  private boolean createAlias = true;
  @Builder.Default
  private boolean reload = true;
  @Builder.Default
  private boolean gitPull = false;
  @Builder.Default
  private Integer replicationFactor = null;
  @Builder.Default
  private Integer shardCount = null;
}
