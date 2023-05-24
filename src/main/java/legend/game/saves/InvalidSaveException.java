package legend.game.saves;

public class InvalidSaveException extends Exception {
  public InvalidSaveException() {
    super();
  }

  public InvalidSaveException(final String message) {
    super(message);
  }

  public InvalidSaveException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public InvalidSaveException(final Throwable cause) {
    super(cause);
  }

  protected InvalidSaveException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
