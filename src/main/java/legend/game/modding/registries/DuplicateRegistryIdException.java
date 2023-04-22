package legend.game.modding.registries;

public class DuplicateRegistryIdException extends RuntimeException {
  public DuplicateRegistryIdException(final String message) {
    super(message);
  }

  public DuplicateRegistryIdException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public DuplicateRegistryIdException(final Throwable cause) {
    super(cause);
  }

  protected DuplicateRegistryIdException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
