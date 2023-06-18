package legend.game.combat.effects;

import legend.core.gte.USCOLOUR;
import legend.core.gte.VECTOR;

public class LightningBoltEffectSegment30 {
  public final int index;

  public final VECTOR origin_00 = new VECTOR();
  /** Narrower gradient of bolt effect, renders below outer */
  public final USCOLOUR innerColour_10 = new USCOLOUR();
  /** Wider gradient of bolt effect, renders above inner */
  public final USCOLOUR outerColour_16 = new USCOLOUR();
  public final USCOLOUR innerColourFadeStep_1c = new USCOLOUR();
  public final USCOLOUR outerColourFadeStep_22 = new USCOLOUR();
  /** ubyte */
  public int scaleMultiplier_28;

  /** short; Incremented while rendering, but never used for anything. */
  public int unused_2a;
  /** short */
  public int originTranslationMagnitude_2c;
  /** short */
  public int baseVertexTranslationScale_2e;

  public LightningBoltEffectSegment30(final int index) {
    this.index = index;
  }
}
