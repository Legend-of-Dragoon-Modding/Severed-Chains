package legend.game.tmd;

import legend.core.gte.ModelPart10;
import legend.core.memory.Method;
import legend.game.unpacker.FileData;

public class TmdWithId {
  public final long id;
  public final Tmd tmd;

  public TmdWithId(final String name, final FileData data) {
    this.id = data.readInt(0x0);
    this.tmd = new Tmd(name, data.slice(0x4));
  }

  @Method(0x800de76cL)
  public TmdObjTable1c optimisePacketsIfNecessary(final int objIndex) {
    if((this.tmd.header.flags & 0x2) == 0) {
      final ModelPart10 dobj2 = new ModelPart10();
      dobj2.tmd_08 = this.tmd.objTable[objIndex];
      return dobj2.tmd_08;
    }

    //LAB_800de7a0
    //LAB_800de7b4
    return this.tmd.objTable[objIndex];
  }
}
