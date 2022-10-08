package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class AnmSpriteMetrics14 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef u_00;
  public final UnsignedByteRef v_01;
  public final UnsignedByteRef ofs_x_02;
  public final UnsignedByteRef ofs_y_03;
  public final UnsignedShortRef cba_04;
  public final UnsignedShortRef flag_06;
  public final UnsignedShortRef w_08;
  public final UnsignedShortRef h_0a;
  public final UnsignedShortRef rot_0c;
  public final UnsignedShortRef flag2_0e;
  public final UnsignedShortRef x_10;
  public final UnsignedShortRef y_12;

  public AnmSpriteMetrics14(final Value ref) {
    this.ref = ref;

    this.u_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.v_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this.ofs_x_02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this.ofs_y_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.cba_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.flag_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this.w_08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this.h_0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this.rot_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this.flag2_0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this.x_10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this.y_12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
