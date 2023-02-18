package legend.core.gte;

import legend.game.unpacker.FileData;

import java.util.Arrays;

public class Tmd {
  public final TmdHeader header;
  public final TmdObjTable1c[] objTable;

  public Tmd(final FileData data) {
    this.header = new TmdHeader(data);
    this.objTable = new TmdObjTable1c[this.header.nobj];

    final FileData objTableOffset = data.slice(0x8);
    Arrays.setAll(this.objTable, i -> new TmdObjTable1c(objTableOffset.slice(i * 0x1c), objTableOffset));
  }
}
