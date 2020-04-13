
package io.github.mightguy.cloud.manager.manager;

import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrCloudException;
import io.github.mightguy.cloud.manager.exception.SolrException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.common.cloud.ZkConfigManager;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Slf4j
@Component
public class SolrCloudmanagerHelper {


  /**
   * This method is responsible for loading configuration git repo and extracting it to a directory
   */
  public void extractGitRepo(String gitUrl, String outPath, String branchName,
      boolean override, String user, String pass) {
    try {
      File outputDir = new File(outPath);
      if (outputDir.exists() || !override) {
        log.info("Repo exist , will not be doing anything");
        return;
      }
      Git.cloneRepository()
          .setURI(gitUrl)
          .setDirectory(new File(outPath))
          .setCloneAllBranches(true)
          .setBranch("refs/heads/" + branchName)
          .setCredentialsProvider(
              new UsernamePasswordCredentialsProvider(user,
                  pass))
          .call();
    } catch (GitAPIException ex) {
      throw new SolrException(ExceptionCode.UNKNOWN_TO_LOAD_CONFIG, ex);
    }
  }


  /**
   * This method is responsible for loading configurations from the given directory path.
   */
  public Map<String, Path> readConfigNameLocations(String locationFromConfig) {

    File directoryFromConfig = getFileFromResourceOrPath(locationFromConfig);
    if (!(directoryFromConfig.exists() && directoryFromConfig.isDirectory())) {
      log.error("Invalid config set location :" + locationFromConfig);
      throw new SolrCloudException(ExceptionCode.SOLR_CLOUD_INVALID_CONFIG, locationFromConfig);
    }
    log.info("Using configuration sets from location : " + directoryFromConfig.getPath());
    return Arrays.stream(directoryFromConfig.listFiles())
        .filter(File::isDirectory)
        .filter(file -> (new File(file, "src/main/resources")).exists())
        .collect(Collectors
            .toMap(File::getName, coreDir -> new File(coreDir, "src/main/resources").toPath()));
  }

  public void uploadConfigs(Map<String, Path> configNameToLocationMap,
      ZkConfigManager zkConfigManager) {
    log.info("Config sets uploading started.");
    configNameToLocationMap.entrySet().forEach(collectionPathEntry -> {
      try {
        log.info("Uploading configuration set :" + collectionPathEntry.getKey());
        zkConfigManager
            .uploadConfigDir(collectionPathEntry.getValue(), collectionPathEntry.getKey());
        log.info("Configuration set successfully uploaded:" + collectionPathEntry.getKey());
      } catch (IOException e) {
        log.error("Exception during configuration upload.", e);
        throw new SolrCloudException(ExceptionCode.SOLR_CLOUD_INVALID_CONFIG, e);
      }
    });
    log.info("Config sets uploading finished.");
  }


  private File getFileFromResourceOrPath(String location) {
    try {
      return ResourceUtils.getFile("classpath:" + location);
    } catch (FileNotFoundException ex) {
      log.error("File not present on classpath; reading from absolute");
      return new File(location);
    }
  }


}
