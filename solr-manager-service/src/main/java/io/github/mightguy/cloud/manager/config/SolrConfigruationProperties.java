
package io.github.mightguy.cloud.manager.config;

import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * The class {@code SolrConfigruationProperties}, is the POJO for the solr-core.yml. All the
 * properties from that file will be assigned to the pojo, By {@link CoreConfiguration}
 */
@Data
@ConfigurationProperties("solr")
public class SolrConfigruationProperties {

  private Alias alias;
  private List<String> disabledFor;
  private Suffix suffix;
  private ZK zk;
  private Map<String, SolrCore> solrCoresData;
  private boolean optimize;
  private boolean softCommit;
  private String chain;
  private int batchSize;
  private String connectType;
  private String solrHost;
  private boolean isLazy = false;

  @Data
  public static class Alias {

    private Boolean enabled;
  }

  @Data
  public static class Suffix {

    private String active;
    private String passive;
  }

  @Data
  public static class ZK {

    private String chroot;
    private int zkConnectTimeout;
    private int zkClientTimeout;
    private String zkUrl;
  }

  @Data
  public static class SolrCore {

    private String coreName;
    private String searchHandler;
  }

}
