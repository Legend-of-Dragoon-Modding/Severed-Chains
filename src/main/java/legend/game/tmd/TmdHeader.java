package legend.game.tmd;

import legend.game.unpacker.FileData;

/** 0x8 bytes long */
public class TmdHeader {
  public final long flags;
  public final int nobj;

  public TmdHeader(final FileData data) {
    this.flags = data.readUInt(0x0);
    this.nobj = data.readInt(0x4);

    if((this.flags & 0x1) != 0) {
      throw new RuntimeException("fixp not supported");
    }

    if((this.flags & 0x2) != 0) {
      throw new RuntimeException("Found CTMD");
    }
  }
}
