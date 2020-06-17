
package io.github.mightguy.cloud.solr.commons.exception;


/**
 * {@code SolrCommonsException}  is  the parent class for all the exception which we are created by
 * this AppModule.
 */
public class SolrCommonsException extends RuntimeException {

  private static final long serialVersionUID = -645928954493402932L;
  protected final ExceptionCode exceptionCode;
  protected final String exceptionMessage;

  public SolrCommonsException(ExceptionCode exceptionCode, String exceptionMessage) {
    super(combineExceptionCodeAndMessage(exceptionCode, exceptionMessage));
    this.exceptionCode = exceptionCode;
    this.exceptionMessage = exceptionMessage;
  }

  public SolrCommonsException(ExceptionCode exceptionCode) {
    super(exceptionCode.getErrorMessage());
    this.exceptionCode = exceptionCode;
    this.exceptionMessage = "";
  }

  public SolrCommonsException(ExceptionCode exceptionCode, Exception ex) {
    super(exceptionCode.getErrorMessage(), ex);
    this.exceptionCode = exceptionCode;
    this.exceptionMessage = "";
  }

  public SolrCommonsException(Exception ex,
      ExceptionCode exceptionCode, String exceptionMessage) {
    super(combineExceptionCodeAndMessage(exceptionCode, exceptionMessage), ex);
    this.exceptionCode = exceptionCode;
    this.exceptionMessage = exceptionMessage;
  }

  public static String combineExceptionCodeAndMessage(ExceptionCode exceptionCode,
      String exceptionMessage) {
    return exceptionCode.getErrorMessage() + " [" + exceptionMessage + "]";
  }

  @Override
  public String getMessage() {
    return combineExceptionCodeAndMessage(this.exceptionCode, this.exceptionMessage);
  }

  public ExceptionCode getExceptionCode() {
    return exceptionCode;
  }

}
