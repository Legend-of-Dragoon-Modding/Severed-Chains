package legend.game.models;

import legend.game.unpacker.FileData;

import java.util.Arrays;

public class ClutAnimationFile {
  public final TmdSubExtension[] tmdSubExtensionArr_00 = new TmdSubExtension[4];

  public ClutAnimationFile(final FileData data) {
    Arrays.setAll(this.tmdSubExtensionArr_00, i -> new TmdSubExtension(data.slice(data.readInt(i * 0x4))));
  }
}
