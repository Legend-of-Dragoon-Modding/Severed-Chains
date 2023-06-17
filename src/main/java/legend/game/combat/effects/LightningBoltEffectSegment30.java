package legend.game.combat.effects;

import legend.core.gte.USCOLOUR;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class LightningBoltEffectSegment30 implements MemoryRef {
  private final Value ref;

  public final VECTOR origin_00;
  /** Narrower gradient of bolt effect, renders below outer */
  public final USCOLOUR innerColour_10;
  /** Wider gradient of bolt effect, renders above inner */
  public final USCOLOUR outerColour_16;
  public final USCOLOUR innerColourFadeStep_1c;
  public final USCOLOUR outerColourFadeStep_22;
  public final ByteRef scaleMultiplier_28;

  /** incremented while rendering, but never used for anything */
  public final ShortRef unused_2a;
  public final ShortRef originTranslationMagnitude_2c;
  public final ShortRef baseVertexTranslationScale_2e;

  public LightningBoltEffectSegment30(final Value ref) {
    this.ref = ref;

    this.origin_00 = ref.offset(4, 0x00L).cast(VECTOR::new);
    this.innerColour_10 = ref.offset(2, 0x10L).cast(USCOLOUR::new);
    this.outerColour_16 = ref.offset(2, 0x16L).cast(USCOLOUR::new);
    this.innerColourFadeStep_1c = ref.offset(2, 0x1cL).cast(USCOLOUR::new);
    this.outerColourFadeStep_22 = ref.offset(2, 0x22L).cast(USCOLOUR::new);
    this.scaleMultiplier_28 = ref.offset(1, 0x28L).cast(ByteRef::new);

    this.unused_2a = ref.offset(2, 0x2aL).cast(ShortRef::new);
    this.originTranslationMagnitude_2c = ref.offset(2, 0x2cL).cast(ShortRef::new);
    this.baseVertexTranslationScale_2e = ref.offset(2, 0x2eL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
