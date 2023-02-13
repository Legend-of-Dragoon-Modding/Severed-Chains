package legend.core.gte;

import legend.core.IoHelper;

public class TmdWithId {
  public final long id;
  public final Tmd tmd;

  public TmdWithId(final byte[] data, final int offset) {
    this.id = IoHelper.readInt(data, offset);
    this.tmd = new Tmd(data, offset + 0x4);
  }
}
