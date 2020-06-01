
package io.github.mightguy.cloud.manager.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

/**
 * The class {@code CoreConfiguration} is responsible to load all the SOLR related properties from
 * the solr-core.yml file
 */t
@Configuration
@EnableConfigurationProperties({SolrConfigruationProperties.class})
@ComponentScan(
    basePackages = {
        "io.github.mightguy.cloud.*"
    })
public class CoreConfiguration {

  @Bean
  public PropertySourcesPlaceholderConfigurer solrCoreProperties(Environment env) {
    PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer
        = new PropertySourcesPlaceholderConfigurer();
    YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
    yaml.setResources(new ClassPathResource("solr-core.yml"));
    PropertiesPropertySource yampProperties = new PropertiesPropertySource("yml", yaml.getObject());
    ((AbstractEnvironment) env).getPropertySources().addLast(yampProperties);
    propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
    return propertySourcesPlaceholderConfigurer;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
