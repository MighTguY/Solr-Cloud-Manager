package io.github.mightguy.cloud.manager.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InitializerConfig {

  private String gitUser;
  private String gitPassword;
  private String folder;
  private String gitBranch;

  @JsonIgnore
  private String cluster;

  @NotBlank
  @NotNull
  private String collection;

  private boolean override = false;
  private boolean deleteOldCollections = false;
  private boolean uploadZkConf = false;

  private boolean createAlias = true;
  private boolean reload = true;
  private boolean gitPull = false;

  @Min(1)
  private int replicationFactor = 1;
  @Min(1)
  private int shardCount = 1;
}