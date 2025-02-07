package legend.game.submap;

import legend.game.unpacker.FileData;

public class CollisionVertexInfo0c {
  public short x_00;
  public short z_02;
  public int _04;
  public boolean _08; // Out-of-bounds edge

  public CollisionVertexInfo0c(final FileData data) {
    this.x_00 = data.readShort(0x00);
    this.z_02 = data.readShort(0x02);
    this._04 = data.readInt(0x04);
    this._08 = data.readInt(0x08) != 0;
  }
}
