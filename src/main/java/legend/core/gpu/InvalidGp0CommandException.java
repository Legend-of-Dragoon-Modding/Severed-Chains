package legend.core.gpu;

public class InvalidGp0CommandException extends Exception {
  public InvalidGp0CommandException() {
    super();
  }

  public InvalidGp0CommandException(final String message) {
    super(message);
  }

  public InvalidGp0CommandException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public InvalidGp0CommandException(final Throwable cause) {
    super(cause);
  }

  protected InvalidGp0CommandException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
