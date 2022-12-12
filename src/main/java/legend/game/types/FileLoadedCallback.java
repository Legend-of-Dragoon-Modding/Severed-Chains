package legend.game.types;

@FunctionalInterface
public interface FileLoadedCallback<Param> {
  void onLoad(final long address, final int size, final Param param);
}
