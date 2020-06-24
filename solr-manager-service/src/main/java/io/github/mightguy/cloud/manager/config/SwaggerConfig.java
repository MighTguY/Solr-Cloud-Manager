
package io.github.mightguy.cloud.manager.config;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The class {@code SwaggerConfig} is responsible for the swagger configration, It is used for
 * creating docket & creating metadata for swagger API
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Value("${microservice.contextPath}")
  String contextPath;

  @Autowired
  AppConfig appConfig;

  @Bean
  public Docket productApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select().apis(RequestHandlerSelectors.basePackage("io.github.mightguy.cloud.manager"))
        .paths(PathSelectors.regex(contextPath + ".*"))
        .build()
        .apiInfo(metaData());
  }

  private ApiInfo metaData() {
    return new ApiInfo(
        "Spring Boot REST API",
        "Spring Boot REST API for Managing Solr Cloud,  Clusters " + appConfig.getClusters()
            .keySet(),
        "1.0",
        "Terms of service",
        new Contact("Lucky Sharma.", "", "iamluckysharma.0910@gmail.com"),
        "",
        "",
        new ArrayList<>());
  }

}
