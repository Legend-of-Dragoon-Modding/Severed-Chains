package legend.game.types;

import java.util.List;

@FunctionalInterface
public interface FileLoadedCallback2<Param> {
  void onLoad(final List<byte[]> fileData, final Param param);
}
