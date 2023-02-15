package legend.game.types;

import legend.core.IoHelper;

public class CContainerSubfile2 {
  /** Sometimes 7, sometimes 10 elements */
  public final short[][] _00 = new short[10][];

  public CContainerSubfile2(final byte[] data, final int offset, final int count) {
    for(int i = 0; i < 10; i++) {
      final int subOffset = offset + IoHelper.readInt(data, offset + i * 0x4);
      this._00[i] = new short[count];

      for(int n = 0; n < count; n++) {
        this._00[i][n] = IoHelper.readShort(data, subOffset + n * 0x2);
      }
    }
  }
}
