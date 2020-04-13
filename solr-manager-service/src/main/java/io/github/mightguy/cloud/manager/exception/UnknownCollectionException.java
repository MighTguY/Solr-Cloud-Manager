
package io.github.mightguy.cloud.manager.exception;

/**
 * {@code UnknownCollectionException}  is  thrown when an application attempts to connect to a
 * unknown collection on SOLR. And try to perform some operations on this unknown collection/core.
 */
public class UnknownCollectionException extends SolrCommonsException {

  private static final long serialVersionUID = -4280085342564267331L;

  public UnknownCollectionException(
      ExceptionCode exceptionCode, String exceptionMessage) {
    super(exceptionCode, exceptionMessage);
  }

  public UnknownCollectionException(
      ExceptionCode exceptionCode) {
    super(exceptionCode);
  }

  public UnknownCollectionException(
      ExceptionCode exceptionCode, Exception ex) {
    super(exceptionCode, ex);
  }

  public UnknownCollectionException(Exception ex,
      ExceptionCode exceptionCode, String exceptionMessage) {
    super(ex, exceptionCode, exceptionMessage);
  }
}
