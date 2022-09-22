package legend.core.memory;

public class MisalignedAccessException extends RuntimeException {
  public MisalignedAccessException() {
    super();
  }

  public MisalignedAccessException(final String message) {
    super(message);
  }

  public MisalignedAccessException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public MisalignedAccessException(final Throwable cause) {
    super(cause);
  }

  protected MisalignedAccessException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
