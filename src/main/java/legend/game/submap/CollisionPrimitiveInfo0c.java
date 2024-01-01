package legend.game.submap;

import legend.game.unpacker.FileData;

public class CollisionPrimitiveInfo0c {
  /** The number of vertices in this primitive */
  public int vertexCount_00;
  public boolean bool_01;
  public int _02;
  /** The offset into the primitives array for this primitive */
  public int primitiveOffset_04;
  public int _08;

  public CollisionPrimitiveInfo0c(final FileData data) {
    this.vertexCount_00 = data.readUByte(0x00);
    this.bool_01 = data.readByte(0x01) != 0;
    this._02 = data.readUShort(0x02);
    this.primitiveOffset_04 = data.readInt(0x04);
    this._08 = data.readInt(0x08);
  }
}
