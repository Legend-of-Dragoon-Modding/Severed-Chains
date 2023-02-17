package legend.game.types;

import legend.game.unpacker.FileData;

public class TmdSubExtension {
  public final short s_02;
  public final short[] sa_04;

  public TmdSubExtension(final FileData data) {
    this.s_02 = data.readShort(0x2);
    this.sa_04 = new short[0];
  }
}
