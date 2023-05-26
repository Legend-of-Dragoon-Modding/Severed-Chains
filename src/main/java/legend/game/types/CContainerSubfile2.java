package legend.game.types;

import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import legend.game.unpacker.FileData;

public class CContainerSubfile2 {
  /** Sometimes 7, sometimes 10 elements */
  public final short[][] _00 = new short[10][];

  public CContainerSubfile2(final FileData data, final int count) {
    for(int i = 0; i < count; i++) {
      final int subOffset = data.readInt(i * 0x4);

      final ShortList shorts = new ShortArrayList();

      for(int n = 0; ; n++) {
        final short val = data.readShort(subOffset + n * 0x2);
        shorts.add(val);

        if(val == -1) {
          break;
        }
      }

      this._00[i] = shorts.toShortArray();
    }
  }
}
