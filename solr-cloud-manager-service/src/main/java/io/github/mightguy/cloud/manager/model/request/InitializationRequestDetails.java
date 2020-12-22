package io.github.mightguy.cloud.manager.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.mightguy.cloud.manager.config.AppConfig.Suffix;
import io.github.mightguy.cloud.manager.util.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class InitializationRequestDetails {

  @JsonProperty(Constants.INITIALIZATION_TYPE)
  private ClusterInitializationType type;

  @JsonProperty("github_src")
  private GithubDetails githubDetails;

  @JsonProperty("local_src")
  private LocalDetails localDetails;

  @JsonProperty("core_details")
  private Map<String, CoreCreationRequest> coreDetails = new HashMap<>();

  @JsonProperty("cores")
  private List<String> core;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class GithubDetails {

    //For Github
    @JsonProperty("github_username")
    public String githubUsername;
    @JsonProperty("github_repo")
    public String githubRepoURL;
    @JsonProperty("github_password")
    public String githubPassword;
    @JsonProperty("github_project_name")
    public String githubProjectName;
    @JsonProperty("github_force_pull")
    public boolean forcePull;
    @JsonProperty("github_branch")
    public String githubBranch = "master";
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class LocalDetails {

    //For Local
    @JsonProperty("local_file_path")
    public String localFilePath;
    @JsonProperty("local_folder")
    public String localFolder;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CoreCreationRequest {

    public int numOfShards = 1;
    public int numOfReplicas = -1;
    public Suffix collectionSuffix;
    public String configName;
    public String configSuffix = "_config";
  }


}
