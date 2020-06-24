
package io.github.mightguy.cloud.manager.util;

import io.github.mightguy.cloud.manager.config.AppConfig.ZkCluster;
import io.github.mightguy.cloud.manager.config.LightningContext;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.solr.commons.config.RemoteSolrClientFactory;
import io.github.mightguy.cloud.solr.commons.config.SolrClientFactory;
import io.github.mightguy.cloud.solr.commons.config.ZkConfiguration;
import io.github.mightguy.cloud.solr.commons.exception.ExceptionCode;
import io.github.mightguy.cloud.solr.commons.exception.SolrException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.StringUtils;
import org.springframework.http.HttpStatus;

@Slf4j
public final class CloudInitializerUtils {

  private CloudInitializerUtils() {
  }

  public static String getCurrentCollectionSuffix(String cluster, LightningContext lightningContext,
      boolean active,
      String collectionAlias) {

    String collectionName = lightningContext.getSolrAliasToCollectionMap(cluster)
        .get(collectionAlias);
    if (StringUtils.isEmpty(collectionName)) {
      return active ? Constants.FIRST_COLLECTION_SUFFIX : Constants.SECOND_COLLECTION_SUFFIX;
    }
    return collectionName.substring(collectionName.lastIndexOf('_'));
  }

  public static String getCurrentAliasSuffix(String cluster, LightningContext lightningContext,
      String collectionName) {

    String collectionLemma = collectionName;
    if (collectionName.contains("_")) {
      collectionLemma = collectionName.substring(0, collectionName.lastIndexOf('_'));
    }
    if (!lightningContext.getSolrCollectionToAliasMap(cluster).containsKey(collectionName)) {
      boolean active = collectionName.endsWith(Constants.FIRST_COLLECTION_SUFFIX);
      return active ? collectionLemma + lightningContext.getSolrConfigruationProperties()
          .getSuffix().getActive() :
          collectionLemma + lightningContext.getSolrConfigruationProperties().getSuffix()
              .getPassive();
    } else {
      return lightningContext.getSolrCollectionToAliasMap(cluster).get(collectionName);
    }
  }

  public static Response createErrorResponse(String message) {
    return new Response(HttpStatus.BAD_REQUEST, message);
  }

  public static String getCurrentTimeStampForDIR() {
    SimpleDateFormat sdfDate = new SimpleDateFormat(
        "yyyy_MM_dd_HH.mm.ss.SSS");// dd/MM/yyyy
    Date now = new Date();
    return sdfDate.format(now);
  }

  public static void createIfNotExist(String directoryName) {
    File directory = new File(directoryName);
    if (!directory.exists()) {
      directory.mkdirs();
    }
  }

  public static List<String> dirContentList(String dir) {
    File location = new File(dir);
    if (!location.exists()) {
      location.mkdirs();
    }
    return Arrays.asList(location.list());
  }

  public static ZkConfiguration preprareZkConfiguration(ZkCluster zkCluster) {
    return ZkConfiguration.builder()
        .chRoot(zkCluster.getZkChRoot())
        .zkClientTimeout(zkCluster.getZkClientTimeout())
        .zkConnectTimeout(zkCluster.getZkConnectTimeout())
        .zkUrl(zkCluster.getZkUrl()).build();
  }

  public static SolrClient getSolrClient(ZkConfiguration zkConfiguration) {
    SolrClientFactory solrClientFactory = new RemoteSolrClientFactory(zkConfiguration);
    return solrClientFactory.getClient();
  }

  public static SolrClient getSolrClient(ZkCluster zkCluster) {
    return getSolrClient(preprareZkConfiguration(zkCluster));
  }

  public static CloudSolrClient checkCloudSolrCLientInstance(SolrClient solrClient) {
    if (!(solrClient instanceof CloudSolrClient)) {
      throw new SolrException(ExceptionCode.SOLR_CLOUD_SUPPORTED_ONLY,
          "SolrCloudClient is only supported");
    }
    return (CloudSolrClient) solrClient;
  }

  public static void deleteDir(String path) {
    try {
      FileUtils.deleteDirectory(new File(path));
    } catch (IOException ex) {
      log.error("Unable to delete directory " + ex.getMessage());
      throw new SolrException(ExceptionCode.SOLR_EXCEPTION,
          "Unable to delete directory " + ex.getMessage());
    }
  }

}
