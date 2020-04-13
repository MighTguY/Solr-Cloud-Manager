
package io.github.mightguy.cloud.manager.config;

import io.github.mightguy.cloud.manager.config.AppConfig.ZkCluster;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
public class AppConfiguration {

  @Bean
  @Primary //may omit this if this is the only SomeBean defined/visible
  public ZkConfiguration zkConfiguration(AppConfig appConfig) {
    if (appConfig.getClusters().isEmpty() || appConfig.getClusters() == null) {
      log.error("Unable to Start application, cluster configuration is required");
      throw new IllegalArgumentException("Cluster Configuration is missing");
    }
    ZkCluster zkCluster = appConfig.getClusters().get("default");
    return CloudInitializerUtils.preprareZkConfiguration(zkCluster);
  }

}
