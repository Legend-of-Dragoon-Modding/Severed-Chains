package legend.game.unpacker.scripts;

public class InvalidPatchListException extends Exception {
  public InvalidPatchListException(final String message) {
    super(message);
  }

  public InvalidPatchListException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public InvalidPatchListException(final Throwable cause) {
    super(cause);
  }

  protected InvalidPatchListException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
