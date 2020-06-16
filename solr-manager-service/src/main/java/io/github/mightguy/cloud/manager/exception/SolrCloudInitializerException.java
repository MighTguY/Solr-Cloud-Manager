package io.github.mightguy.cloud.manager.exception;

public class SolrCloudInitializerException extends RuntimeException {

  public SolrCloudInitializerException() {
    super();
  }

  public SolrCloudInitializerException(String message) {
    super(message);
  }

  public SolrCloudInitializerException(String message, Throwable cause) {
    super(message, cause);
  }

  public SolrCloudInitializerException(Throwable cause) {
    super(cause);
  }

  protected SolrCloudInitializerException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
