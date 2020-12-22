package io.github.mightguy.cloud.manager.components;

import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrCommonsException;
import io.github.mightguy.cloud.manager.model.request.ClusterInitializationType;
import io.github.mightguy.cloud.manager.model.request.InitializationRequestDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class Validator {

  public void validateInitializeCloudRequest(String cluster, boolean deleteOldCollections,
      boolean uploadZkConf, ClusterInitializationType type,
      InitializationRequestDetails payload) {
    if (
        (type.equals(ClusterInitializationType.GIT)
            && (payload.getGithubDetails() == null
            || StringUtils.isEmpty(payload.getGithubDetails().getGithubProjectName())
            || StringUtils.isEmpty(payload.getGithubDetails().getGithubPassword())
            || StringUtils.isEmpty(payload.getGithubDetails().getGithubUsername())
            || StringUtils.isEmpty(payload.getGithubDetails().getGithubRepoURL())))
            ||
            (type.equals(ClusterInitializationType.LOCAL)) && (
                payload.getLocalDetails() == null
                    || StringUtils.isEmpty(payload.getLocalDetails().getLocalFilePath())
            )) {
      throw new SolrCommonsException(ExceptionCode.INVALID_REQUEST);
    }
  }
}
