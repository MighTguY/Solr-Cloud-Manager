
package io.github.mightguy.cloud.manager.util;

import static io.github.mightguy.cloud.manager.util.Constants.PATH_DELIM;

import io.github.mightguy.cloud.manager.config.AppConfig.ZkCluster;
import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrCommonsException;
import io.github.mightguy.cloud.manager.model.Response;
import io.github.mightguy.cloud.manager.model.request.InitializationRequestDetails.GithubDetails;
import io.github.mightguy.cloud.manager.util.client.RemoteSolrClientFactory;
import io.github.mightguy.cloud.manager.util.client.SolrClientFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

@Slf4j
public final class CloudInitializerUtils {

  private CloudInitializerUtils() {
  }

  public static boolean areStringsEqual(String v1, String v2) {
    if (v1 == null && v2 == null) {
      return true;
    }
    if (StringUtils.isEmpty(v1) && StringUtils.isEmpty(v2)) {
      return true;
    }
    if (StringUtils.isEmpty(v1)) {
      return false;
    }
    if (StringUtils.isEmpty(v2)) {
      return false;
    }
    return v1.trim().toLowerCase().equals(v2.trim().toLowerCase());
  }

  public static void extractGitLocation(String outPath, GithubDetails githubDetails) {
    try {
      File outputDir = new File(outPath);
      if (outputDir.exists() || !githubDetails.forcePull) {
        log.info("Repo exist , will not be doing anything");
        return;
      }
      Git.cloneRepository()
          .setURI(githubDetails.githubRepoURL)
          .setDirectory(new File(outPath))
          .setCloneAllBranches(true)
          .setBranch("refs/heads/" + githubDetails.githubBranch)
          .setCredentialsProvider(
              new UsernamePasswordCredentialsProvider(githubDetails.githubUsername,
                  githubDetails.githubPassword))
          .call();
    } catch (GitAPIException ex) {
      throw new SolrCommonsException(ExceptionCode.UNKNOWN_TO_LOAD_CONFIG, ex);
    }
  }

  /**
   * This method is responsible for loading configurations from the given directory path.
   */
  public static Map<String, Path> readConfigNameLocations(String locationFromConfig) {

    File directoryFromConfig = getFileFromResourceOrPath(locationFromConfig);
    if (!(directoryFromConfig.exists() && directoryFromConfig.isDirectory())) {
      log.error("Invalid config set location :" + locationFromConfig);
      throw new SolrCommonsException(ExceptionCode.SOLR_CLOUD_INVALID_CONFIG, locationFromConfig);
    }
    log.info("Using configuration sets from location : " + directoryFromConfig.getPath());
    return Arrays.stream(directoryFromConfig.listFiles())
        .filter(File::isDirectory)
        .filter(file -> (new File(file, "resources")).exists())
        .collect(Collectors
            .toMap(File::getName, coreDir -> new File(coreDir, "resources").toPath()));
  }

  private static File getFileFromResourceOrPath(String location) {
    try {
      return ResourceUtils.getFile("classpath:" + location);
    } catch (FileNotFoundException ex) {
      log.error("File not present on classpath; reading from absolute");
      return new File(location);
    }
  }


  public static void extractLocalLocation(String path) {
    File file = new File(path);
    if (!file.exists()) {
      throw new SolrCommonsException(ExceptionCode.UNKNOWN_TO_LOAD_CONFIG);
    }
  }

  public static String collectionNameWithSuffix(String collectionName, String suffix,
      boolean originalCollectionName) {
    if (originalCollectionName) {
      return collectionName;
    }
    return collectionName.concat("_").concat(suffix);
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

  public static String getBackupRepoPath(String location, String cluster, String repo) {
    return location + PATH_DELIM + cluster + PATH_DELIM + repo;
  }


  public static SolrClient getSolrClient(ZkCluster zkCluster) {
    SolrClientFactory solrClientFactory = new RemoteSolrClientFactory(zkCluster);
    return solrClientFactory.getClient();
  }

  public static CloudSolrClient checkCloudSolrClientInstance(SolrClient solrClient) {
    if (!(solrClient instanceof CloudSolrClient)) {
      throw new SolrCommonsException(ExceptionCode.SOLR_CLOUD_SUPPORTED_ONLY,
          "SolrCloudClient is only supported");
    }
    return (CloudSolrClient) solrClient;
  }

  public static void deleteDir(String path) {
    try {
      FileUtils.deleteDirectory(new File(path));
    } catch (IOException ex) {
      log.error("Unable to delete directory " + ex.getMessage());
      throw new SolrCommonsException(ExceptionCode.SOLR_EXCEPTION,
          "Unable to delete directory " + ex.getMessage());
    }
  }


}
