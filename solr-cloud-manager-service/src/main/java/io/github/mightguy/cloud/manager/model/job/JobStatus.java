package io.github.mightguy.cloud.manager.model.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class JobStatus implements Serializable {

  private static final long serialVersionUID = -1266330044847223592L;
  private String jobId;
  private String jobName;
  private String requestCluster;
  private String description;
  private JobState jobState;
}
