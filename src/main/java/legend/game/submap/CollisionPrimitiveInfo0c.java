package legend.game.submap;

import legend.game.unpacker.FileData;

public class CollisionPrimitiveInfo0c {
  /** The number of vertices in this primitive */
  public int vertexCount_00;
  /**
   * Not 100% sure this name is accurate, but the only places I've seen it have been steep (tops of ladders, the top of the tree outside Hellena).
   * Not quite the same as blocked off collision. If you bump into this you stick to it instead of sliding along it. That said, I have yet to see
   * a place in the game where you can actually run into one of these pieces of geometry.
   */
  public boolean flatEnoughToWalkOn_01;
  /** Offset into the vertex info array (i.e. the sum of all previous primitives' vertex counts) */
  public int vertexInfoOffset_02;
  /** The offset into the primitives array for this primitive */
  public int primitiveOffset_04;
  public int _08;

  public CollisionPrimitiveInfo0c(final FileData data) {
    this.vertexCount_00 = data.readUByte(0x00);
    this.flatEnoughToWalkOn_01 = data.readByte(0x01) != 0;
    this.vertexInfoOffset_02 = data.readUShort(0x02);
    this.primitiveOffset_04 = data.readInt(0x04);
    this._08 = data.readInt(0x08);
  }
}
