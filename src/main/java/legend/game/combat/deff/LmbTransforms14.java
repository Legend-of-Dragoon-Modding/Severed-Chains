package legend.game.combat.deff;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class LmbTransforms14 implements MemoryRef {
  private final Value ref;

  public final SVECTOR scale_00;
  public final SVECTOR trans_06;
  public final SVECTOR rot_0c;

  public LmbTransforms14(final Value ref) {
    this.ref = ref;

    this.scale_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
    this.trans_06 = ref.offset(2, 0x06L).cast(SVECTOR::new);
    this.rot_0c = ref.offset(2, 0x0cL).cast(SVECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
