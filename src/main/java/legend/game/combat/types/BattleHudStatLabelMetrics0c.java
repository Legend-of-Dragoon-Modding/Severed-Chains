package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class BattleHudStatLabelMetrics0c implements MemoryRef {
  private final Value ref;

  public final ShortRef x_00;
  public final ShortRef y_02;
  public final UnsignedByteRef u_04;

  public final UnsignedByteRef v_06;

  public final ShortRef w_08;
  public final ShortRef h_0a;

  public BattleHudStatLabelMetrics0c(final Value ref) {
    this.ref = ref;

    this.x_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.y_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.u_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);

    this.v_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);

    this.w_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.h_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
