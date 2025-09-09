package legend.game.scripting;

public class NotImplementedException extends RuntimeException {
  public NotImplementedException() {
    super();
  }

  public NotImplementedException(final String message) {
    super(message);
  }

  public NotImplementedException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public NotImplementedException(final Throwable cause) {
    super(cause);
  }

  protected NotImplementedException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
