package legend.game.submap;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;

public class AlertIndicator14 implements MemoryRef {
  public final Value ref;

  public final ShortRef x0_00;
  public final ShortRef x1_02;
  public final ShortRef y0_04;
  public final ShortRef y1_06;
  public final UnsignedShortRef u0_08;
  public final UnsignedShortRef u1_0a;
  public final UnsignedShortRef v0_0c;
  public final UnsignedShortRef v1_0e;

  public AlertIndicator14(final Value ref) {
    this.ref = ref;

    this.x0_00 = ref.offset(2, 0x00).cast(ShortRef::new);
    this.x1_02 = ref.offset(2, 0x02).cast(ShortRef::new);
    this.y0_04 = ref.offset(2, 0x04).cast(ShortRef::new);
    this.y1_06 = ref.offset(2, 0x06).cast(ShortRef::new);
    this.u0_08 = ref.offset(2, 0x08).cast(UnsignedShortRef::new);
    this.u1_0a = ref.offset(2, 0x0a).cast(UnsignedShortRef::new);
    this.v0_0c = ref.offset(2, 0x0c).cast(UnsignedShortRef::new);
    this.v1_0e = ref.offset(2, 0x0e).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
