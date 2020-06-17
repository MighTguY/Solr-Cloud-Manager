
package io.github.mightguy.cloud.manager.util;

/**
 * Common unit test exception
 */
public class BaseTestException extends RuntimeException {

  public BaseTestException() {
  }

  public BaseTestException(String message) {
    super(message);
  }

  public BaseTestException(String message, Throwable cause) {
    super(message, cause);
  }

}
