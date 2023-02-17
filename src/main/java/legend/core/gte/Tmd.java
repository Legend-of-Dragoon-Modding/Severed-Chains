package legend.core.gte;

import legend.game.unpacker.FileData;

import java.util.Arrays;

public class Tmd {
  public final TmdHeader header;
  public final TmdObjTable1c[] objTable;

  public Tmd(final FileData data) {
    this.header = new TmdHeader(data);
    this.objTable = new TmdObjTable1c[this.header.nobj];
    Arrays.setAll(this.objTable, i -> new TmdObjTable1c(data.slice(0x8 + i * 0x1c), data.slice(0x8)));
  }
}
