package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class SMapStruct44 implements MemoryRef {
  private final Value ref;

  public final SVECTOR svec_00;
  public final SVECTOR svec_08;
  public final SVECTOR svec_10;
  public final SVECTOR svec_18;
  public final IntRef screenOffsetX_20;
  public final IntRef screenOffsetY_24;
  public final UnsignedIntRef _28;
  public final UnsignedIntRef _2c;
  public final UnsignedIntRef _30;
  public final UnsignedIntRef _34;

  public final UnsignedShortRef _38;

  public final UnsignedIntRef sz3div4_40;

  public SMapStruct44(final Value ref) {
    this.ref = ref;

    this.svec_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
    this.svec_08 = ref.offset(2, 0x08L).cast(SVECTOR::new);
    this.svec_10 = ref.offset(2, 0x10L).cast(SVECTOR::new);
    this.svec_18 = ref.offset(2, 0x18L).cast(SVECTOR::new);
    this.screenOffsetX_20 = ref.offset(4, 0x20L).cast(IntRef::new);
    this.screenOffsetY_24 = ref.offset(4, 0x24L).cast(IntRef::new);
    this._28 = ref.offset(4, 0x28L).cast(UnsignedIntRef::new);
    this._2c = ref.offset(4, 0x2cL).cast(UnsignedIntRef::new);
    this._30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
    this._34 = ref.offset(4, 0x34L).cast(UnsignedIntRef::new);

    this._38 = ref.offset(2, 0x38L).cast(UnsignedShortRef::new);

    this.sz3div4_40 = ref.offset(4, 0x40L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
