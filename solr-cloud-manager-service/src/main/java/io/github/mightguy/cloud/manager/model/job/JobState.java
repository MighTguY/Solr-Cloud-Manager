package io.github.mightguy.cloud.manager.model.job;

public enum JobState {
  QUEUED,
  SUBMITTED,
  STARTING,
  RUNNING,
  FAILED,
  DEAD,
  FINISHED,
  KILLED
}
