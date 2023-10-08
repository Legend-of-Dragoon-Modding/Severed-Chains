package legend.game.combat.environment;

import legend.core.memory.Value;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.MemoryRef;

public class BattleMenuBackgroundUvMetrics04 implements MemoryRef {
  public final Value ref;

  public UnsignedByteRef u_00;
  public UnsignedByteRef v_01;
  public UnsignedByteRef w_02;
  public UnsignedByteRef h_03;

  public BattleMenuBackgroundUvMetrics04(final Value ref) {
    this.ref = ref;

    this.u_00 = ref.offset(1, 0x00).cast(UnsignedByteRef::new);
    this.v_01 = ref.offset(1, 0x01).cast(UnsignedByteRef::new);
    this.w_02 = ref.offset(1, 0x02).cast(UnsignedByteRef::new);
    this.h_03 = ref.offset(1, 0x03).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
