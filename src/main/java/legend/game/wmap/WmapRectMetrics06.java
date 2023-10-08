package legend.game.wmap;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class WmapRectMetrics06 implements MemoryRef {
  public final Value ref;

  public final UnsignedByteRef x_00;
  public final UnsignedByteRef y_01;
  public final UnsignedByteRef u_02;
  public final UnsignedByteRef v_03;
  public final UnsignedByteRef w_04;
  public final UnsignedByteRef h_05;

  public WmapRectMetrics06(final Value ref) {
    this.ref = ref;

    this.x_00 = ref.offset(1, 0x00).cast(UnsignedByteRef::new);
    this.y_01 = ref.offset(1, 0x01).cast(UnsignedByteRef::new);
    this.u_02 = ref.offset(1, 0x02).cast(UnsignedByteRef::new);
    this.v_03 = ref.offset(1, 0x03).cast(UnsignedByteRef::new);
    this.w_04 = ref.offset(1, 0x04).cast(UnsignedByteRef::new);
    this.h_05 = ref.offset(1, 0x05).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
