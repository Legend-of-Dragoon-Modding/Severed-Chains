package legend.game.types;

import legend.core.IoHelper;
import legend.core.gte.TmdWithId;

/**
 * @see <a href="https://github.com/Legend-of-Dragoon-Modding/Legend-of-Dragoon-Java/issues/2">more information</a>
 */
public class CContainer {
  public final TmdWithId tmdPtr_00;
  public final CContainerSubfile1 ext_04;
  public final CContainerSubfile2 ptr_08;

  public CContainer(final byte[] data, final int offset) {
    this.tmdPtr_00 = new TmdWithId(data, offset + IoHelper.readInt(data, offset));
    this.ext_04 = new CContainerSubfile1(data, offset + IoHelper.readInt(data, offset + 0x4));
    this.ptr_08 = new CContainerSubfile1(data, offset + IoHelper.readInt(data, offset + 0x8));
  }
}
