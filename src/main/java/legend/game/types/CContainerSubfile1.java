package legend.game.types;

import legend.game.unpacker.FileData;

import java.util.Arrays;

public class CContainerSubfile1 {
  public final TmdSubExtension[] tmdSubExtensionArr_00 = new TmdSubExtension[4];

  public CContainerSubfile1(final FileData data) {
    Arrays.setAll(this.tmdSubExtensionArr_00, i -> new TmdSubExtension(data.slice(data.readInt(i * 0x4))));
  }
}
