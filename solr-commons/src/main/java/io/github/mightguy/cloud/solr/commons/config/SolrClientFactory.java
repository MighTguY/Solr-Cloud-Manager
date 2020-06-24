
package io.github.mightguy.cloud.solr.commons.config;

import org.apache.solr.client.solrj.SolrClient;

/**
 * The  {@code SolrClientFactory} is the a factory Interface,
 * Those who all inherit this class, will have the responsibility of creating an impl of
 * SolrClient on various configurations.
 *
 * One of the subclass is {@link RemoteSolrClientFactory}
 */
public interface SolrClientFactory {

  SolrClient getClient();
}
