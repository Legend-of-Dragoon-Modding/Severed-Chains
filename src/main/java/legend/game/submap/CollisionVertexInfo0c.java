package legend.game.submap;

import legend.game.unpacker.FileData;

public class CollisionVertexInfo0c {
  public short x_00;
  public short z_02;
  public int _04;
  /** Used for out-of-bounds detection */
  public boolean boundary_08;

  public CollisionVertexInfo0c(final FileData data) {
    this.x_00 = data.readShort(0x00);
    this.z_02 = data.readShort(0x02);
    this._04 = data.readInt(0x04);
    this.boundary_08 = data.readInt(0x08) != 0;
  }
}
