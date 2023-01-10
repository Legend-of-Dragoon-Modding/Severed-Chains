package legend.game.types;

@FunctionalInterface
public interface SingleFileLoadedCallback<Param> {
  void onLoad(final byte[] fileData, final Param param);
}
