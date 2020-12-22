
package io.github.mightguy.cloud.manager.util.client;

import io.github.mightguy.cloud.manager.config.AppConfig.ZkCluster;
import java.util.Collections;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
 * The class {@code RemoteSolrClientFactory} holds the responsibility of creating the {@link
 * SolrClient}, which can be used to query/connect/perform operations on SOLR.
 * <p>
 * {@link SolrClient} is the SOLRJ connector. It can be of 2 types : 1. {@link HttpSolrClient} 2.
 * {@link CloudSolrClient}
 * <p>
 * This depends on the connectionType value of the properties.
 * <p>
 * This class takes the reponsibility of setting collection on the SOLRJ, by appending alias suffix
 * if it is present
 */
@Slf4j
public class RemoteSolrClientFactory implements SolrClientFactory {

  private SolrClient solrClient;

  /**
   * Factory that create solrj LB client.
   */
  public RemoteSolrClientFactory(String collectionName, ZkCluster zkCluster) {

    String zkHost = zkCluster.getZkUrl();

    String zkChroot = zkCluster.getZkChRoot();

    Integer zkClientTimeout = zkCluster.getZkClientTimeout();

    Integer zkConnectTimeout = zkCluster.getZkConnectTimeout();

    solrClient = new CloudSolrClient.Builder(Collections.singletonList(zkHost),
        Optional.of(zkChroot))
        .build();
    ((CloudSolrClient) solrClient).setZkClientTimeout(zkClientTimeout);
    ((CloudSolrClient) solrClient).setZkConnectTimeout(zkConnectTimeout);
  }


  public RemoteSolrClientFactory(ZkCluster zkCluster) {
    String zkHost = zkCluster.getZkUrl();
    String zkChRoot = zkCluster.getZkChRoot();
    Integer zkClientTimeout = zkCluster.getZkClientTimeout();
    Integer zkConnectTimeout = zkCluster.getZkConnectTimeout();
    solrClient = new CloudSolrClient.Builder(Collections.singletonList(zkHost),
        Optional.of(zkChRoot))
        .build();
    ((CloudSolrClient) solrClient).setZkClientTimeout(zkClientTimeout);
    ((CloudSolrClient) solrClient).setZkConnectTimeout(zkConnectTimeout);
  }

  /**
   * @return instance of {@link SolrClient}
   */
  @Override
  public SolrClient getClient() {
    return solrClient;
  }
}
