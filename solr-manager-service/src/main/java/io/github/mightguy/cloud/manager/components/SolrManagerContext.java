package io.github.mightguy.cloud.manager.components;

import io.github.mightguy.cloud.manager.config.AppConfig;
import io.github.mightguy.cloud.manager.config.AppConfig.ZkCluster;
import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrCommonsException;
import io.github.mightguy.cloud.manager.model.core.Alias;
import io.github.mightguy.cloud.manager.model.core.SolrCollection;
import io.github.mightguy.cloud.manager.util.CloudInitializerUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.StringUtils;
import org.apache.solr.common.cloud.ZkConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolrManagerContext {

  private final AppConfig appConfig;
  private final SolrManagerHelper solrManagerHelper;

  private final Map<String, Map<String, SolrCollection>> clusterToCollectionMap = new HashMap<>();
  private final Map<String, Map<String, Alias>> clusterToAliasMap = new HashMap<>();
  private final Map<String, SolrClient> clusterToSolrClientMap = new HashMap<>();

  @Autowired
  public SolrManagerContext(AppConfig appConfig,
      SolrManagerHelper solrManagerHelper) {
    this.appConfig = appConfig;
    this.solrManagerHelper = solrManagerHelper;
    appConfig.getClusters().forEach(this::initializeContext);
  }

  public void reloadContext() {
    appConfig.getClusters().keySet().forEach(this::reloadContext);
  }

  public void reloadContext(String cluster) {
    Map<String, SolrCollection> collectionMap = solrManagerHelper
        .fetchCollections(clusterToSolrClientMap.get(cluster)).stream()
        .map(SolrCollection::new)
        .collect(Collectors
            .toMap(SolrCollection::getName, solrCollection -> solrCollection, (a1, a2) -> a1));

    List<Alias> aliases = solrManagerHelper
        .fetchAliases(clusterToSolrClientMap.get(cluster)).entrySet().stream()
        .map(entry -> {
          boolean isActive = entry.getKey().endsWith(appConfig.getAlias().getSuffix().getActive());
          boolean isPassive = entry.getKey()
              .endsWith(appConfig.getAlias().getSuffix().getPassive());

          Alias alias = new Alias(
              entry.getKey(),
              collectionMap.get(entry.getValue()).getName(),
              isActive,
              isPassive);
          collectionMap.get(entry.getValue()).setAlias(alias);
          return alias;
        })
        .collect(Collectors.toList());

    clusterToAliasMap
        .put(cluster, aliases.stream().collect(
                Collectors.toMap(alias -> alias.getAlias(), alias -> alias, (a1, a2) -> a1)));
    clusterToCollectionMap.put(cluster, collectionMap);

  }

  public void initializeContext(String clusterName, ZkCluster zkCluster) {
    SolrClient solrClient = CloudInitializerUtils.getSolrClient(zkCluster);
    clusterToSolrClientMap.put(clusterName, solrClient);
    reloadContext(clusterName);
  }

  public ZkConfigManager getConfigManager(String activeCluster) {
    SolrClient solrClient = clusterToSolrClientMap.get(activeCluster);
    if (solrClient instanceof CloudSolrClient) {
      return ((CloudSolrClient) solrClient).getZkStateReader().getConfigManager();
    }
    throw new SolrCommonsException(ExceptionCode.SOLR_CLOUD_SUPPORTED_ONLY,
        "SolrCloudClient is only supported");
  }

  public Collection<SolrCollection> getSolrCollections(String cluster) {
    return clusterToCollectionMap.get(cluster).values();
  }

  public Collection<Alias> getSolrAlias(String cluster) {
    return clusterToAliasMap.get(cluster).values();
  }

  public Alias getSolrAlias(String cluster, String collection) {
    SolrCollection solrCollection = clusterToCollectionMap.get(cluster)
        .getOrDefault(collection, null);
    if (null == solrCollection) {
      return null;
    }
    return solrCollection.getAlias();
  }

  public boolean isCollectionAlreadyPresent(String cluster, String collection) {
    return clusterToCollectionMap.get(cluster).values().stream()
        .anyMatch(core -> CloudInitializerUtils.areStringsEqual(collection, core
            .getName()));
  }

  public boolean isAliasAlreadyPresent(String cluster, String collection, String alias) {
    return clusterToCollectionMap.get(cluster).values().stream()
        .anyMatch(core ->
            CloudInitializerUtils.areStringsEqual(alias, core.getAlias().getAlias())
                && CloudInitializerUtils.areStringsEqual(collection, core.getName())
        );
  }

  public boolean isAliasPresent(String cluster, String alias) {
    return clusterToAliasMap.get(cluster).containsKey(alias);
  }

  public SolrCollection getSolrCollection(String cluster, String collectionName) {
    SolrCollection solrCollection = clusterToCollectionMap.get(cluster)
        .getOrDefault(collectionName, null);
    if (solrCollection == null) {
      throw new SolrCommonsException(ExceptionCode.UNKNOWN_COLLECTION,
          "Unknown collection ".concat(collectionName));
    }
    return solrCollection;
  }

  public SolrClient getSolrClient(String cluster) {
    return clusterToSolrClientMap.get(cluster);
  }

  public List<String> getSolrCollectionNameWithoutSuffixes(String cluster) {
    return clusterToCollectionMap.get(cluster).keySet().stream().map(col -> {
      if (col.endsWith(appConfig.getCollectionSuffix().getActive())) {
        return col.replace(appConfig.getCollectionSuffix().getActive(), "");
      } else if (col.endsWith(appConfig.getCollectionSuffix().getPassive())) {
        return col.replace(appConfig.getCollectionSuffix().getPassive(), "");
      }
      return col;
    }).collect(Collectors.toList());
  }

  public boolean isValidCluster(String value) {
    return !StringUtils.isEmpty(value) && appConfig.getClusters().containsKey(value);
  }
}
