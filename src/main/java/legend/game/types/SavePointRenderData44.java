package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class SavePointRenderData44 implements MemoryRef {
  private final Value ref;

  public final SVECTOR vert0_00;
  public final SVECTOR vert1_08;
  public final SVECTOR vert2_10;
  public final SVECTOR vert3_18;
  public final IntRef screenOffsetX_20;
  public final IntRef screenOffsetY_24;
  public final IntRef rotation_28;
  public final IntRef fadeAmount_2c;
  /** 16.16 fixed-point */
  public final IntRef fadeAccumulator_30;
  public final IntRef colour_34;
  /** 0: fade in, 1: fade out */
  public final ShortRef fadeState_38;

  public final IntRef z_40;

  public SavePointRenderData44(final Value ref) {
    this.ref = ref;

    this.vert0_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
    this.vert1_08 = ref.offset(2, 0x08L).cast(SVECTOR::new);
    this.vert2_10 = ref.offset(2, 0x10L).cast(SVECTOR::new);
    this.vert3_18 = ref.offset(2, 0x18L).cast(SVECTOR::new);
    this.screenOffsetX_20 = ref.offset(4, 0x20L).cast(IntRef::new);
    this.screenOffsetY_24 = ref.offset(4, 0x24L).cast(IntRef::new);
    this.rotation_28 = ref.offset(4, 0x28L).cast(IntRef::new);
    this.fadeAmount_2c = ref.offset(4, 0x2cL).cast(IntRef::new);
    this.fadeAccumulator_30 = ref.offset(4, 0x30L).cast(IntRef::new);
    this.colour_34 = ref.offset(4, 0x34L).cast(IntRef::new);
    this.fadeState_38 = ref.offset(2, 0x38L).cast(ShortRef::new);

    this.z_40 = ref.offset(4, 0x40L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
