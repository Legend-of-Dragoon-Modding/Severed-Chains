package legend.game.modding;

public class ModNotLoadedException extends RuntimeException {
  public ModNotLoadedException(final String message) {
    super(message);
  }

  public ModNotLoadedException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ModNotLoadedException(final Throwable cause) {
    super(cause);
  }

  protected ModNotLoadedException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
