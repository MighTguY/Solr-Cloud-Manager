package io.github.mightguy.cloud.manager.config;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Data
public class AppConfig {

  private Alias alias;
  private Suffix collectionSuffix;
  private Backup backupInfo;
  private String configOutPath;
  private Map<String, ZkCluster> clusters;
  private Map<String, String> defaultClusterAliasSuffixMap;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ZkCluster {

    private String zkUrl;
    private String zkChRoot;
    private Integer zkConnectTimeout;
    private Integer zkClientTimeout;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Backup {

    String backupSuffix;
    String restoreSuffix;
    String backupLocation;
  }

  @Data
  public static class Suffix {

    private String active;
    private String passive;
  }

  @Data
  public static class Alias {

    private List<String> disabledFor;
    private Boolean enabled;
    private Suffix suffix;
  }
}
