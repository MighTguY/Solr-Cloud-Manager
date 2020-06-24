
package io.github.mightguy.cloud.manager;

import io.github.mightguy.cloud.solr.commons.config.CoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CoreConfiguration.class)
@DependsOn("solrClientManager")
public class InitialiserMainApplication {

  public static void main(String[] args) {
    SpringApplication.run(InitialiserMainApplication.class, args);
  }
}
