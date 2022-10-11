package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;

/** Example - horses running in new game cutscene */
public class DustRenderData54 implements MemoryRef {
  private final Value ref;

  public final ShortRef renderMode_00;
  public final ShortRef textureIndex_02;
  public final ShortRef _04;
  public final ShortRef _06;
  public final IntRef _08;
  public final IntRef _0c;
  public final IntRef _10;

  public final IntRef x_18;
  public final IntRef y_1c;
  public final SVECTOR v0_20;
  public final SVECTOR v1_28;
  public final SVECTOR v2_30;
  public final SVECTOR v3_38;
  /** 16.16 fixed-point */
  public final IntRef colourStep_40;
  /** 16.16 fixed-point */
  public final IntRef colourAccumulator_44;
  public final IntRef colour_48;
  public final IntRef z_4c;
  public final Pointer<DustRenderData54> next_50;

  public DustRenderData54(final Value ref) {
    this.ref = ref;

    this.renderMode_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.textureIndex_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);

    this.x_18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this.y_1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.v0_20 = ref.offset(4, 0x20L).cast(SVECTOR::new);
    this.v1_28 = ref.offset(4, 0x28L).cast(SVECTOR::new);
    this.v2_30 = ref.offset(4, 0x30L).cast(SVECTOR::new);
    this.v3_38 = ref.offset(4, 0x38L).cast(SVECTOR::new);
    this.colourStep_40 = ref.offset(4, 0x40L).cast(IntRef::new);
    this.colourAccumulator_44 = ref.offset(4, 0x44L).cast(IntRef::new);
    this.colour_48 = ref.offset(4, 0x48L).cast(IntRef::new);
    this.z_4c = ref.offset(4, 0x4cL).cast(IntRef::new);
    this.next_50 = ref.offset(4, 0x50L).cast(Pointer.deferred(4, DustRenderData54::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
