package legend.game.combat.environment;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BattleHudBorderMetrics14 implements MemoryRef {
  public final Value ref;

  public final ShortRef indexXy0_00;
  public final ShortRef indexXy1_02;
  public final ShortRef u_04;
  public final ShortRef v_06;
  public final ShortRef offsetX_08;
  public final ShortRef offsetY_0a;
  public final ShortRef w_0c;
  public final ShortRef h_0e;

  public BattleHudBorderMetrics14(final Value ref) {
    this.ref = ref;

    this.indexXy0_00 = ref.offset(2, 0x00).cast(ShortRef::new);
    this.indexXy1_02 = ref.offset(2, 0x02).cast(ShortRef::new);
    this.u_04 = ref.offset(2, 0x04).cast(ShortRef::new);
    this.v_06 = ref.offset(2, 0x06).cast(ShortRef::new);
    this.offsetX_08 = ref.offset(2, 0x08).cast(ShortRef::new);
    this.offsetY_0a = ref.offset(2, 0x0a).cast(ShortRef::new);
    this.w_0c = ref.offset(2, 0x0c).cast(ShortRef::new);
    this.h_0e = ref.offset(2, 0x0e).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
