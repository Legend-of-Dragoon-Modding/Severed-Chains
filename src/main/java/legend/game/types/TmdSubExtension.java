package legend.game.types;

import legend.core.IoHelper;

public class TmdSubExtension {
  public final short s_02;
  public final short[] sa_04;

  public TmdSubExtension(final byte[] data, final int offset) {
    this.s_02 = IoHelper.readShort(data, offset + 0x2);
  }
}
