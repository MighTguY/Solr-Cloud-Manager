
package io.github.mightguy.cloud.solr.commons.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The class {@code SolrClientManager}, is  reponsible of bootstraping all the beans of
 * SolrClientFactory so that it can be directly autowired in the system.
 */
@Slf4j
@ConditionalOnBean(ZkConfiguration.class)
@Configuration
public class SolrClientManager implements InitializingBean {

  @Autowired
  SolrConfigruationProperties config;

  @Autowired
  ZkConfiguration zkConfiguration;

  @Autowired
  ConfigurableApplicationContext configurableApplicationContext;


  @Override
  public void afterPropertiesSet() {
    if (config.isLazy() || (config.getSolrCoresData() == null)) {
      log.warn("Not initializing beans for Solr Collections");
      return;
    }
    SingletonBeanRegistry beanRegistry = configurableApplicationContext.getBeanFactory();
    config.getSolrCoresData().entrySet().forEach(stringSolrCoreEntry ->
        beanRegistry.registerSingleton(stringSolrCoreEntry.getKey(),
            new RemoteSolrClientFactory(stringSolrCoreEntry.getValue().getCoreName(), config,
                zkConfiguration))
    );
  }

  @Bean(name = "defaultSolrClient")
  public SolrClientFactory commonSolrClient(ZkConfiguration zkConfiguration) {
    return new RemoteSolrClientFactory(zkConfiguration);
  }

}
