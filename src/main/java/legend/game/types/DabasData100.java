package legend.game.types;

import legend.game.unpacker.FileData;

import java.util.Arrays;

public class DabasData100 {
  public int chapterIndex_00;

  public final int[] items_14 = new int[6];
  public int specialItem_2c;

  public int gold_34;
  public int _38;
  public int _3c;

  public DabasData100(final FileData data) {
    this.chapterIndex_00 = data.readInt(0x0);

    Arrays.setAll(this.items_14, i -> data.readInt(0x14 + i * 0x4));
    this.specialItem_2c = data.readInt(0x2c);

    this.gold_34 = data.readInt(0x34);
    this._38 = data.readInt(0x38);
    this._3c = data.readInt(0x3c);
  }
}
