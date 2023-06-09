package legend.game.saves;

public class SaveFailedException extends Exception {
  public SaveFailedException(final String message) {
    super(message);
  }

  public SaveFailedException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public SaveFailedException(final Throwable cause) {
    super(cause);
  }

  protected SaveFailedException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
