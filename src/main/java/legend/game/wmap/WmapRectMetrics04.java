package legend.game.wmap;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class WmapRectMetrics04 implements MemoryRef {
  public final Value ref;

  public final UnsignedByteRef u_00;
  public final UnsignedByteRef v_01;
  public final UnsignedByteRef w_02;
  public final UnsignedByteRef h_03;

  public WmapRectMetrics04(final Value ref) {
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
