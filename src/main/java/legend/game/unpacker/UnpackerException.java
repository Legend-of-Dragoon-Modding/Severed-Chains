package legend.game.unpacker;

public class UnpackerException extends Exception {
  public UnpackerException() {
    super();
  }

  public UnpackerException(final String message) {
    super(message);
  }

  public UnpackerException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public UnpackerException(final Throwable cause) {
    super(cause);
  }

  protected UnpackerException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
