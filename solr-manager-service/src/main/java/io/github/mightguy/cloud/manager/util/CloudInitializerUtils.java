
package io.github.mightguy.cloud.manager.util;

import io.github.mightguy.cloud.manager.config.AppConfig.ZkCluster;
import io.github.mightguy.cloud.manager.config.ZkConfiguration;
import java.util.Collections;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;

@Slf4j
public final class CloudInitializerUtils {

  private CloudInitializerUtils() {
  }


  public static ZkConfiguration preprareZkConfiguration(ZkCluster zkCluster) {
    return ZkConfiguration.builder()
        .chRoot(zkCluster.getZkChRoot())
        .zkClientTimeout(zkCluster.getZkClientTimeout())
        .zkConnectTimeout(zkCluster.getZkConnectTimeout())
        .zkUrl(zkCluster.getZkUrl()).build();
  }

  public static SolrClient getSolrClient(ZkConfiguration zkConfig) {
    String zkHost = zkConfig.getZkUrl();
    String zkChroot = zkConfig.getChRoot();
    Integer zkClientTimeout = zkConfig.getZkClientTimeout();
    Integer zkConnectTimeout = zkConfig.getZkConnectTimeout();
    Integer socketTimeout =
        zkConfig.getSolrSOTimeout() == null ? zkClientTimeout : zkConfig.getSolrSOTimeout();
    CloudSolrClient solrClient = new CloudSolrClient.Builder(Collections.singletonList(zkHost),
        Optional.of(zkChroot))
        .withSocketTimeout(socketTimeout)
        .build();
    solrClient.setZkClientTimeout(zkClientTimeout);
    solrClient.setZkConnectTimeout(zkConnectTimeout);
    return solrClient;
  }

  public static SolrClient getSolrClient(ZkCluster zkCluster) {
    return getSolrClient(preprareZkConfiguration(zkCluster));
  }

}
