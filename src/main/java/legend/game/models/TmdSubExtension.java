package legend.game.models;

import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import legend.game.unpacker.FileData;

public class TmdSubExtension {
  public final short s_02;
  public final short[] sourceYOffset_04;

  public TmdSubExtension(final FileData data) {
    this.s_02 = data.readShort(0x2);

    final ShortList shorts = new ShortArrayList();
    for(int i = 0; ; i++) {
      final short val = data.readShort(0x4 + i * 0x2);
      shorts.add(val);

      if(val == -1) {
        break;
      }
    }

    this.sourceYOffset_04 = shorts.toShortArray();
  }
}
