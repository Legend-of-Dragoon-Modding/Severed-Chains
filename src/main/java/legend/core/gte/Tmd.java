package legend.core.gte;

import java.util.Arrays;

public class Tmd {
  public final TmdHeader header;
  public final TmdObjTable1c[] objTable;

  public Tmd(final byte[] data, final int offset) {
    this.header = new TmdHeader(data, offset);
    this.objTable = new TmdObjTable1c[this.header.nobj];
    Arrays.setAll(this.objTable, i -> new TmdObjTable1c(data, offset + 0x8 + i * 0x1c));
  }
}
