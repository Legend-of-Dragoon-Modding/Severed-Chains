package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class TriangleIndicator44 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final IntRef sequence_04;
  public final IntRef time_08;
  public final IntRef _0c;
  public final IntRef _10;
  public final IntRef sequenceCount_14;
  public final UnsignedShortRef tpage_18;
  public final UnsignedShortRef clut_1a;
  public final IntRef u_1c;
  public final IntRef v_20;
  public final UnsignedByteRef r_24;
  public final UnsignedByteRef g_25;
  public final UnsignedByteRef b_26;

  public final IntRef _28;
  public final IntRef _2c;
  public final IntRef _30;
  public final IntRef x_34;
  public final IntRef y_38;
  public final IntRef _3c;

  public TriangleIndicator44(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.sequence_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.time_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this.sequenceCount_14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this.tpage_18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);
    this.clut_1a = ref.offset(2, 0x1aL).cast(UnsignedShortRef::new);
    this.u_1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.v_20 = ref.offset(4, 0x20L).cast(IntRef::new);
    this.r_24 = ref.offset(1, 0x24L).cast(UnsignedByteRef::new);
    this.g_25 = ref.offset(1, 0x25L).cast(UnsignedByteRef::new);
    this.b_26 = ref.offset(1, 0x26L).cast(UnsignedByteRef::new);

    this._28 = ref.offset(4, 0x28L).cast(IntRef::new);
    this._2c = ref.offset(4, 0x2cL).cast(IntRef::new);
    this._30 = ref.offset(4, 0x30L).cast(IntRef::new);
    this.x_34 = ref.offset(4, 0x34L).cast(IntRef::new);
    this.y_38 = ref.offset(4, 0x38L).cast(IntRef::new);
    this._3c = ref.offset(4, 0x3cL).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
