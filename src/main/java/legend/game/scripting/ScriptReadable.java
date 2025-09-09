package legend.game.scripting;

public interface ScriptReadable {
  default void read(final int index, final Param out) {
    throw new NotImplementedException("Reading index %d from %s is not implemented".formatted(index, this.getClass().getSimpleName()));
  }
}
