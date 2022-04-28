package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class RenderableMetrics14 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef u_00;
  public final UnsignedByteRef v_01;
  public final UnsignedByteRef x_02;
  public final UnsignedByteRef y_03;
  /** MSB enables raw textures */
  public final UnsignedShortRef clut_04;
  public final UnsignedShortRef tpage_06;
  public final UnsignedShortRef width_08;
  public final UnsignedShortRef height_0a;

  public final ShortRef _10;
  public final ShortRef _12;

  public RenderableMetrics14(final Value ref) {
    this.ref = ref;

    this.u_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.v_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this.x_02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this.y_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.clut_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.tpage_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this.width_08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this.height_0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);

    this._10 = ref.offset(2, 0x10L).cast(ShortRef::new);
    this._12 = ref.offset(2, 0x12L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
