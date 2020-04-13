
package io.github.mightguy.cloud.manager.util;


import io.github.mightguy.cloud.manager.config.LightningContext;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SolrCloudHealthCheck extends AbstractHealthIndicator {

  @Autowired
  LightningContext lightningContext;

  @Override
  protected void doHealthCheck(Builder builder) throws Exception {
    int status = 0;
    try {
      for (Map.Entry<String, SolrClient> solrClientEntry : lightningContext
          .getSolrClientClusterMap().entrySet()) {
        builder.withDetail(solrClientEntry.getKey(),
            ((CloudSolrClient) solrClientEntry.getValue()).getZkStateReader().getZkClient()
                .getZkServerAddress());
        CoreAdminRequest request = new CoreAdminRequest();
        request.setAction(CoreAdminParams.CoreAdminAction.STATUS);
        CoreAdminResponse response = request.process(solrClientEntry.getValue());
        if (response.getStatus() != 0) {
          status = response.getStatus();
          break;
        }
      }
      if (status == 0) {
        builder.up();
      } else {
        builder.down();
      }

    } catch (SolrServerException | IOException ex) {
      log.error("Solr connection error " + ex);
      builder.down();
    }
  }
}
