package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class RadialElectricityEffect38 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedShortRef boltCount_00;

  public final IntRef currentColourFadeStep_04;
  public final IntRef scriptIndex_08;
  public final IntRef numColourFadeSteps_0c;
  public final IntRef _10;
  public final IntRef _14;
  public final IntRef _18;
  /** The lower the value, the wider the angle in which the bolt can be drawn */
  public final ShortRef boltAngleRangeCutoff_1c;
  public final ShortRef _1e;
  public final ShortRef callbackIndex_20;
  public final UnsignedByteRef _22;
  /** If 0, colour will be progressively faded for each successive segment. */
  public final UnsignedByteRef fadeSuccessiveSegments_23;
  public final ByteRef _24;

  /**
   * Bit 7 of effect flag, multiplied by random value to determine whether
   * segment angle should change each segment
   */
  public final UnsignedShortRef varyBoltSegmentAngle_26;
  public final UnsignedByteRef boltSegmentCount_28;
  /** 0 = render monochrome base triangles */
  public final UnsignedByteRef hasMonochromeBase_29;
  /** Effect is only meant to send new render commands every other frame if manager._10._24 != 0 */
  public final UnsignedByteRef frameNum_2a;

  public final Pointer<QuadConsumerRef<EffectManagerData6c, RadialElectricityEffect38, LightningBoltEffect14, Integer>> callback_2c;

  public final Pointer<UnboundedArrayRef<LightningBoltEffect14>> bolts_34;

  public RadialElectricityEffect38(final Value ref) {
    this.ref = ref;

    this.boltCount_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);

    this.currentColourFadeStep_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.scriptIndex_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.numColourFadeSteps_0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this.boltAngleRangeCutoff_1c = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(ShortRef::new);
    this.callbackIndex_20 = ref.offset(2, 0x20L).cast(ShortRef::new);
    this._22 = ref.offset(1, 0x22L).cast(UnsignedByteRef::new);
    this.fadeSuccessiveSegments_23 = ref.offset(1, 0x23L).cast(UnsignedByteRef::new);
    this._24 = ref.offset(1, 0x24L).cast(ByteRef::new);

    this.varyBoltSegmentAngle_26 = ref.offset(2, 0x26L).cast(UnsignedShortRef::new);
    this.boltSegmentCount_28 = ref.offset(1, 0x28L).cast(UnsignedByteRef::new);
    this.hasMonochromeBase_29 = ref.offset(1, 0x29L).cast(UnsignedByteRef::new);
    this.frameNum_2a = ref.offset(1, 0x2aL).cast(UnsignedByteRef::new);

    this.callback_2c = ref.offset(4, 0x2cL).cast(Pointer.deferred(4, QuadConsumerRef::new));
    this.bolts_34 = ref.offset(4, 0x34L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x14, LightningBoltEffect14::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
