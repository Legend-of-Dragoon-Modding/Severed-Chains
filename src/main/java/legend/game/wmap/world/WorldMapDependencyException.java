package legend.game.wmap.world;

/** Missing external content is recoverable; invalid graph/rule code is not classified as absence. */
public final class WorldMapDependencyException extends IllegalArgumentException {
  private static final long serialVersionUID = 1L;

  public WorldMapDependencyException(final String message) { super(message); }
  public WorldMapDependencyException(final String message, final Throwable cause) { super(message, cause); }
}
