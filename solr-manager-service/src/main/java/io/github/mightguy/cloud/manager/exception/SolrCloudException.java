
package io.github.mightguy.cloud.manager.exception;

public class SolrCloudException extends SolrException {

  public SolrCloudException(ExceptionCode exceptionCode, String exceptionMessage) {
    super(exceptionCode, exceptionMessage);
  }

  public SolrCloudException(ExceptionCode exceptionCode) {
    super(exceptionCode);
  }

  public SolrCloudException(ExceptionCode exceptionCode, Exception ex) {
    super(exceptionCode, ex);
  }

  public SolrCloudException(Exception ex,
      ExceptionCode exceptionCode, String exceptionMessage) {
    super(ex, exceptionCode, exceptionMessage);
  }
}
