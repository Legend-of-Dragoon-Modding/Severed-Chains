package legend.game.types;

import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import legend.game.unpacker.FileData;

public class ClutAnimation {
  public final short clutIndex_02;
  public final short[] dataStream_04;

  public ClutAnimation(final FileData data) {
    this.clutIndex_02 = data.readShort(0x2);

    final ShortList shorts = new ShortArrayList();
    for(int i = 0; ; i++) {
      final short val = data.readShort(0x4 + i * 0x2);
      shorts.add(val);

      if(val == -1) {
        break;
      }
    }

    this.dataStream_04 = shorts.toShortArray();
  }
}
