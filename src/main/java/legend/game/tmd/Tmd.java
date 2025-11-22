package legend.game.tmd;

import legend.game.unpacker.FileData;

import java.util.Arrays;

public class Tmd {
  public final String name;

  public final TmdHeader header;
  public final TmdObjTable1c[] objTable;

  public Tmd(final String name, final FileData data) {
    this.name = name;
    this.header = new TmdHeader(data);
    this.objTable = new TmdObjTable1c[this.header.nobj];

    final FileData objTableOffset = data.slice(0x8);
    Arrays.setAll(this.objTable, i -> new TmdObjTable1c(name + " obj " + i, objTableOffset.slice(i * 0x1c), objTableOffset));
  }

  @Override
  public String toString() {
    return this.name + ' ' + super.toString();
  }
}
