package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class SpriteMetrics08 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef u_00;
  public final UnsignedShortRef v_02;
  public final UnsignedByteRef w_04;
  public final UnsignedByteRef h_05;
  public final UnsignedShortRef clut_06;

  public SpriteMetrics08() {
    this.ref = null;

    this.u_00 = new UnsignedShortRef();
    this.v_02 = new UnsignedShortRef();
    this.w_04 = new UnsignedByteRef();
    this.h_05 = new UnsignedByteRef();
    this.clut_06 = new UnsignedShortRef();
  }

  public SpriteMetrics08(final Value ref) {
    this.ref = ref;

    this.u_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.v_02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.w_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.h_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.clut_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
