package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class SobjPos14 implements MemoryRef {
  private final Value ref;

  public final VECTOR pos_00;
  public final SVECTOR rot_0c;

  public SobjPos14(final Value ref) {
    this.ref = ref;

    this.pos_00 = ref.offset(4, 0x00L).cast(VECTOR::new);
    this.rot_0c = ref.offset(2, 0x0cL).cast(SVECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
