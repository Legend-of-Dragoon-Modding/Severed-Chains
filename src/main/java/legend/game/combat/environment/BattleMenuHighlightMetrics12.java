package legend.game.combat.environment;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BattleMenuHighlightMetrics12 implements MemoryRef {
  public final Value ref;

  public final ShortRef xBase;
  public final ShortRef yBase_02;
  public final ShortRef w_04;
  public final ShortRef h_06;
  public final ShortRef u_08;
  public final ShortRef v_0a;
  public final ShortRef uvW_0c;
  public final ShortRef uvH_0e;
  public final ShortRef uvShiftType_10;

  public BattleMenuHighlightMetrics12(final Value ref) {
    this.ref = ref;

    this.xBase = ref.offset(2, 0x00).cast(ShortRef::new);
    this.yBase_02 = ref.offset(2, 0x02).cast(ShortRef::new);
    this.w_04 = ref.offset(2, 0x04).cast(ShortRef::new);
    this.h_06 = ref.offset(2, 0x06).cast(ShortRef::new);
    this.u_08 = ref.offset(2, 0x08).cast(ShortRef::new);
    this.v_0a = ref.offset(2, 0x0a).cast(ShortRef::new);
    this.uvW_0c = ref.offset(2, 0x0c).cast(ShortRef::new);
    this.uvH_0e = ref.offset(2, 0x0e).cast(ShortRef::new);
    this.uvShiftType_10 = ref.offset(2, 0x10).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
