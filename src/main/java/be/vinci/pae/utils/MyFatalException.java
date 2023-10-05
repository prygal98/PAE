package be.vinci.pae.utils;

/**
 * class that englobe non-stopping exception (ex: SQLException).
 */
public class MyFatalException extends RuntimeException {

  /**
   * a message for the error.
   */
  private String message;

  /**
   * the exception that was thrown.
   */
  private Exception exception;

  /**
   * Constructor for MyFatalException.
   */
  public MyFatalException() {

  }

  /**
   * Constructor for MyFatalException.
   *
   * @param message a message for the error.
   */
  public MyFatalException(String message) {
    this.message = message;
  }

  /**
   * Constructor for MyFatalException.
   *
   * @param exception the exception thrown.
   */
  public MyFatalException(Exception exception) {
    this.exception = exception;
  }

  /**
   * Constructor for MyFatalException.
   *
   * @param exception the exception thrown.
   * @param message   a message for the error.
   */
  public MyFatalException(Exception exception, String message) {
    this.message = message;
    this.exception = exception;
  }

  /**
   * getter for message.
   *
   * @return message.
   */
  @Override
  public String getMessage() {
    return message;
  }

  /**
   * setter for message.
   *
   * @param message the message.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * getter for Exception.
   *
   * @return Exception.
   */
  public Exception getException() {
    return exception;
  }

  /**
   * setter for Exception.
   *
   * @param exception the Exception.
   */
  public void setException(Exception exception) {
    this.exception = exception;
  }
}
