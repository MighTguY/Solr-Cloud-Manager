
package io.github.mightguy.cloud.solr.commons.request;


import io.github.mightguy.cloud.solr.commons.config.RemoteSolrClientFactory;
import io.github.mightguy.cloud.solr.commons.config.SolrClientFactory;
import io.github.mightguy.cloud.solr.commons.config.SolrConfigruationProperties;
import io.github.mightguy.cloud.solr.commons.config.ZkConfiguration;
import io.github.mightguy.cloud.solr.commons.exception.ExceptionCode;
import io.github.mightguy.cloud.solr.commons.exception.UnknownCollectionException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * {@code RequestManager} is the parent class, which is responsible to create request to the
 * collection.
 * <p>
 * This bootstraps all the solrClientFactory so that this can be used to create a request on any of
 * them.
 */
@Slf4j
@Component
@ConditionalOnBean(ZkConfiguration.class)
@DependsOn("solrClientManager")
public class RequestUtil {

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  protected SolrConfigruationProperties solrConfigruationProperties;

  @Getter
  protected Map<String, SolrClientFactory> solrClientFactoryMap = new HashMap<>();

  protected Map<String, String> selectQueryCollectionMap = new HashMap<>();

  /**
   * To initialize solrClientFactoryMap and selectQueryCollectionMap by collection name vs {@link
   * SolrClientFactory} and collection name vs
   * {@link io.github.mightguy.cloud.solr.commons.utils.Constants}
   * of Request Handler name.
   */
  @PostConstruct
  protected void init() {
    if (solrConfigruationProperties.getSolrCoresData() == null) {
      return;
    }
    solrConfigruationProperties.getSolrCoresData().entrySet().forEach(solrCoreEntry -> {
      solrClientFactoryMap.put(solrCoreEntry.getKey().toLowerCase(),
          (RemoteSolrClientFactory) applicationContext.getBean(solrCoreEntry.getKey()));
      selectQueryCollectionMap.put(solrCoreEntry.getKey().toLowerCase(),
          solrCoreEntry.getValue().getSearchHandler());
    });
  }

  /**
   * To get {@link SolrClientFactory} by name of collection from solrClientFactoryMap In case no
   * collection with requested name found, this will throws {@code UnknownCollectionException}
   *
   * @param collectionName {@link String}
   * @return {@link SolrClientFactory}
   */
  public SolrClientFactory getSolrClientByCollectionName(String collectionName) {
    if (solrClientFactoryMap.containsKey(collectionName.toLowerCase())) {
      return solrClientFactoryMap.get(collectionName.toLowerCase());
    } else {
      log.error("Unknown Collection Name");
      throw new UnknownCollectionException(ExceptionCode.UNKNOWN_COLLECTION);
    }
  }

  /**
   * To get requestSelectionPath {@link String} by name of collection from solrClientFactoryMap. In
   * case no collection with requested name found, this will throws {@code
   * UnknownCollectionException}
   *
   * @param collectionName {@link String}
   * @return requesthandler {@link String}
   */
  public String getselectPathByCollectionName(String collectionName) {
    if (solrClientFactoryMap.containsKey(collectionName.toLowerCase())) {
      return selectQueryCollectionMap.get(collectionName.toLowerCase());
    } else {
      log.error("Unknown Collection Name");
      throw new UnknownCollectionException(ExceptionCode.UNKNOWN_COLLECTION);
    }
  }

  public SolrClient getAnySolrClient() {
    return this.solrClientFactoryMap.entrySet().iterator().next().getValue().getClient();
  }
}
