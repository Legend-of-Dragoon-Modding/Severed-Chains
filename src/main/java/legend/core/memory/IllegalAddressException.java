package legend.core.memory;

public class IllegalAddressException extends RuntimeException {
  public IllegalAddressException() {
    super();
  }

  public IllegalAddressException(final String message) {
    super(message);
  }

  public IllegalAddressException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public IllegalAddressException(final Throwable cause) {
    super(cause);
  }

  protected IllegalAddressException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
