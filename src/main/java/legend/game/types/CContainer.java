package legend.game.types;

import legend.core.gte.TmdWithId;
import legend.game.unpacker.FileData;

/**
 * @see <a href="https://github.com/Legend-of-Dragoon-Modding/Legend-of-Dragoon-Java/issues/2">more information</a>
 */
public class CContainer {
  public final TmdWithId tmdPtr_00;
  public final CContainerClutAnimations clutAnimations_04;
  public final CContainerSubfile2 ptr_08;

  public CContainer(final String name, final FileData data) {
    this(name, data, 7);
  }

  public CContainer(final String name, final FileData data, final int subfileSize) {
    this.tmdPtr_00 = new TmdWithId(name, data.slice(data.readInt(0x0)));

    final int offset04 = data.readInt(0x4);
    if(offset04 != 0) {
      this.clutAnimations_04 = new CContainerClutAnimations(data.slice(offset04));
    } else {
      this.clutAnimations_04 = null;
    }

    final int offset08 = data.readInt(0x8);
    if(offset08 != 0) {
      this.ptr_08 = new CContainerSubfile2(data.slice(offset08), subfileSize);
    } else {
      this.ptr_08 = null;
    }
  }
}
