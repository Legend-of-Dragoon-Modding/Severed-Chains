package legend.game.unpacker;

public class UnpackerStoppedRuntimeException extends RuntimeException {
  public UnpackerStoppedRuntimeException() {
    super();
  }

  public UnpackerStoppedRuntimeException(final String message) {
    super(message);
  }

  public UnpackerStoppedRuntimeException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public UnpackerStoppedRuntimeException(final Throwable cause) {
    super(cause);
  }

  protected UnpackerStoppedRuntimeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
