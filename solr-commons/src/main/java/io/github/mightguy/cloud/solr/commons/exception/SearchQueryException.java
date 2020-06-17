
package io.github.mightguy.cloud.solr.commons.exception;

/**
 * {@code SearchQueryException}  is  thrown when an application attempts to
 * query on SOLR & SolrException/IOException occured while connecting to SOLR or
 * Query got malformed.
 */
public class SearchQueryException extends SolrCommonsException {

  private static final long serialVersionUID = 8584239055248829772L;

  public SearchQueryException(ExceptionCode exceptionCode, String exceptionMessage) {
    super(exceptionCode, exceptionMessage);
  }

  public SearchQueryException(ExceptionCode exceptionCode) {
    super(exceptionCode);
  }

  public SearchQueryException(ExceptionCode exceptionCode, Exception ex) {
    super(exceptionCode, ex);
  }

  public SearchQueryException(Exception ex,
      ExceptionCode exceptionCode, String exceptionMessage) {
    super(ex, exceptionCode, exceptionMessage);
  }
}
