package legend.core.gte;

import legend.core.IoHelper;

/** 0x8 bytes long */
public class TmdHeader {
  public final long flags;
  public final int nobj;

  public TmdHeader(final byte[] data, final int offset) {
    this.flags = IoHelper.readUInt(data, offset);
    this.nobj = IoHelper.readInt(data, offset + 0x4);

    if((this.flags & 0x1) != 0) {
      throw new RuntimeException("fixp not supported");
    }
  }
}
