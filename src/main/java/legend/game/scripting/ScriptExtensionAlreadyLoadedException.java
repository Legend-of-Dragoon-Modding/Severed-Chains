package legend.game.scripting;

public class ScriptExtensionAlreadyLoadedException extends RuntimeException {
  public ScriptExtensionAlreadyLoadedException(final String message) {
    super(message);
  }

  public ScriptExtensionAlreadyLoadedException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ScriptExtensionAlreadyLoadedException(final Throwable cause) {
    super(cause);
  }

  protected ScriptExtensionAlreadyLoadedException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
