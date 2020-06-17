
package io.github.mightguy.cloud.manager.util;

import io.github.mightguy.cloud.manager.config.LightningContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class SolrCloudClusterInfo  implements InfoContributor {

  @Autowired
  LightningContext lightningContext;

  @Override
  public void contribute(Builder builder) {
    builder.withDetail("clusters", lightningContext.getAppConfig().getClusters());
  }
}
