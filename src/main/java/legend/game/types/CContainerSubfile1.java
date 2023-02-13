package legend.game.types;

import legend.core.IoHelper;

import java.util.Arrays;

public class CContainerSubfile1 {
  public final TmdSubExtension[] tmdSubExtensionArr_00 = new TmdSubExtension[4];

  public CContainerSubfile1(final byte[] data, final int offset) {
    Arrays.setAll(this.tmdSubExtensionArr_00, i -> new TmdSubExtension(data, offset + IoHelper.readInt(data, offset + i * 0x4)));
  }
}
