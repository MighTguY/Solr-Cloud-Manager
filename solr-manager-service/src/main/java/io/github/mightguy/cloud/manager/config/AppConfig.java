package io.github.mightguy.cloud.manager.config;

import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * The class {@code AppConfig} holds all the app related config
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "solrmanager")
public class AppConfig {

  private GitInfo gitInfo;
  private Backup backupInfo;
  private Map<String, ZkCluster> clusters;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ZkCluster {

    private String zkUrl;
    private String zkChRoot;
    private Integer zkConnectTimeout;
    private Integer zkClientTimeout;
    private Integer solrSoTimeout;
    private boolean isAuthEnabled;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class GitInfo {

    String clonepath;
    String url;
    String branch;
    String app;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Backup {

    String backupSuffix;
    String restoreSuffix;
    String backupLocation;
  }
}
