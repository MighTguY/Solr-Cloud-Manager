package io.github.mightguy.cloud.manager.exception;

import org.springframework.http.HttpStatus;

/**
 * {@code ExceptionCode} holds, all the exception messages which can be thrown by the module.
 * <p>
 * All custom exceptions must use these Messages to be thrown, so that this will be centralised.
 */
public enum ExceptionCode {

  INVALID_REQUEST("Exception while parsing request", HttpStatus.BAD_REQUEST),
  ALIAS_SWITCH_FAILED("Exception during alias switching for collection", HttpStatus.BAD_REQUEST),
  BATCH_SIZE_ERROR("Batch size is not valid", HttpStatus.BAD_REQUEST),
  SEARCH_QUERY_EXCEPTION("Search Query Exception from SOLR", HttpStatus.BAD_REQUEST),
  SOLR_CLIENT_BOOTSTRAP_EXCEPTION("Unable to Bootstrap Solr Client Exception occured",
      HttpStatus.BAD_REQUEST),
  SOLR_EXCEPTION("Solr Server Exception occured", HttpStatus.BAD_REQUEST),
  SOLR_EXCEPTION_INDEXING("Exception during indexing core", HttpStatus.BAD_REQUEST),
  SOLR_EXCEPTION_COMMITING_CORE("Exception during commiting core", HttpStatus.BAD_REQUEST),
  SOLR_CLOUD_SUPPORTED_ONLY("Only Solr Cloud Is Supported", HttpStatus.BAD_REQUEST),
  SOLR_CLOUD_INVALID_CONFIG("Invalid Config set location", HttpStatus.BAD_REQUEST),
  UNKNOWN_COLLECTION("Query To Unkown Collection", HttpStatus.BAD_REQUEST),
  UNKNOWN_TO_LOAD_CONFIG("Exception while loading configurations", HttpStatus.BAD_REQUEST),
  UNABLE_TO_CREATE_COLLECTION_ALIAS("Exception during collection or alias creation.",
      HttpStatus.BAD_REQUEST),
  UNABLE_TO_CREATE_COLLECTION("Exception during collection creation.",
      HttpStatus.BAD_REQUEST),
  UNABLE_TO_RELOAD_COLLECTION("Exception during reload collection", HttpStatus.BAD_REQUEST),
  UNABLE_TO_DELETE_COLLECTION_DATA("Exception during deleting collection data", HttpStatus.BAD_REQUEST),
  UNABLE_TO_DELETE_COLLECTION("Exception during deleting collection data", HttpStatus.BAD_REQUEST),
  CLUSTER_UNHEALTHY("Cluster is not in healthy state", HttpStatus.INTERNAL_SERVER_ERROR);

  private String errorMessage;
  private HttpStatus httpStatus;

  ExceptionCode(String errorMessage, HttpStatus httpStatus) {
    this.errorMessage = errorMessage;
    this.httpStatus = httpStatus;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
