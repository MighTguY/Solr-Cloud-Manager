
package io.github.mightguy.cloud.manager.exception;

/**
 * {@code SolrException}  is  thrown when an application attempts to
 * update/commit on SOLR & SolrException/IOException occured while connecting to SOLR .
 *
 * This exception will also be thrown, in cases when client/client manager is unable to
 * generate objects using solr-config properties.
 *
 */
public class SolrException extends SolrCommonsException {

  private static final long serialVersionUID = 8896510871851029121L;

  public SolrException(ExceptionCode exceptionCode, String exceptionMessage) {
    super(exceptionCode, exceptionMessage);
  }

  public SolrException(ExceptionCode exceptionCode) {
    super(exceptionCode);
  }

  public SolrException(ExceptionCode exceptionCode, Exception ex) {
    super(exceptionCode, ex);
  }

  public SolrException(Exception ex,
      ExceptionCode exceptionCode, String exceptionMessage) {
    super(ex, exceptionCode, exceptionMessage);
  }
}
