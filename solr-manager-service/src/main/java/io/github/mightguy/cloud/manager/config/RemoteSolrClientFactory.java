
package io.github.mightguy.cloud.manager.config;

import io.github.mightguy.cloud.manager.util.Constants;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.StringUtils;

/**
 * The class {@code RemoteSolrClientFactory} holds the responsibility of creating the {@link
 * SolrClient}, which can be used to query/connect/perform operations on SOLR.
 *
 * {@link SolrClient} is the SOLRJ connector. It can be of 2 types : 1. {@link HttpSolrClient} 2.
 * {@link CloudSolrClient}
 *
 * This depends on the connectionType value of the properties.
 *
 * This class takes the reponsibility of setting collection on the SOLRJ, by appending alias suffix
 * if it is present
 */
@Slf4j
public class RemoteSolrClientFactory implements SolrClientFactory {

  private SolrClient solrClient;

  /**
   * Factory that create solrj LB client.
   */
  public RemoteSolrClientFactory(String collectionName, SolrConfigruationProperties config,
      ZkConfiguration zkConfig) {

    List<String> disabledFor = config.getDisabledFor();
    Boolean aliasEnabled = disabledFor.contains(collectionName)
        ? Boolean.FALSE
        : config.getAlias().getEnabled();
    String active = config.getSuffix().getActive();
    String solrHost = config.getSolrHost();
    String connectionType = config.getConnectType();

    String zkHost = zkConfig.getZkUrl();
    zkHost = (StringUtils.isEmpty(zkHost)) ? config.getZk().getZkUrl() : zkHost;

    String zkChroot = zkConfig.getChRoot();
    zkChroot = (StringUtils.isEmpty(zkChroot)) ? config.getZk().getChroot() : zkChroot;

    Integer zkClientTimeout = zkConfig.getZkClientTimeout();
    zkClientTimeout =
        (zkClientTimeout == null) ? config.getZk().getZkClientTimeout() : zkClientTimeout;

    Integer zkConnectTimeout = zkConfig.getZkConnectTimeout();
    zkConnectTimeout =
        (zkConnectTimeout == null) ? config.getZk().getZkConnectTimeout() : zkConnectTimeout;

    String collectionNameToUse =
        aliasEnabled ? collectionName + active : collectionName;

    connectionType =
        StringUtils.isEmpty(connectionType) ? Constants.ZK_MODE_CLIENT : connectionType;

    switch (connectionType) {
      case Constants.HTTP_MODE_CLIENT:
        solrClient = new HttpSolrClient.Builder().withBaseSolrUrl("http://" + solrHost + "/solr"
            + "/" + collectionNameToUse).build();
        break;
      case Constants.ZK_MODE_CLIENT:
      default:
        solrClient = new CloudSolrClient.Builder(Collections.singletonList(zkHost),
            Optional.of(zkChroot))
            .build();
        ((CloudSolrClient) solrClient).setDefaultCollection(collectionNameToUse);
        ((CloudSolrClient) solrClient).setZkClientTimeout(zkClientTimeout);
        ((CloudSolrClient) solrClient).setZkConnectTimeout(zkConnectTimeout);
    }
  }


  public RemoteSolrClientFactory(ZkConfiguration zkConfig) {
    String zkHost = zkConfig.getZkUrl();
    String zkChroot = zkConfig.getChRoot();
    Integer zkClientTimeout = zkConfig.getZkClientTimeout();
    Integer zkConnectTimeout = zkConfig.getZkConnectTimeout();
    Integer socketTimeout =
        zkConfig.getSolrSOTimeout() == null ? zkClientTimeout : zkConfig.getSolrSOTimeout();
    solrClient = new CloudSolrClient.Builder(Collections.singletonList(zkHost),
        Optional.of(zkChroot))
        .withSocketTimeout(socketTimeout)
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
