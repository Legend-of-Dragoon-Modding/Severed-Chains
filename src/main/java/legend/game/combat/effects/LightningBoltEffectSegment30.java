package legend.game.combat.effects;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class LightningBoltEffectSegment30 {
  public final int index;

  public final Vector3f origin_00 = new Vector3f();
  /** Narrower gradient of bolt effect, renders below outer */
  public final Vector3i innerColour_10 = new Vector3i();
  /** Wider gradient of bolt effect, renders above inner */
  public final Vector3i outerColour_16 = new Vector3i();
  public final Vector3i innerColourFadeStep_1c = new Vector3i();
  public final Vector3i outerColourFadeStep_22 = new Vector3i();
  /** ubyte */
  public float scaleMultiplier_28;

  /** short; Incremented while rendering, but never used for anything. */
  public float unused_2a;
  /** short */
  public float originTranslationMagnitude_2c;
  /** Was 8-bit fixed-point short */
  public float baseVertexTranslationScale_2e;

  public LightningBoltEffectSegment30(final int index) {
    this.index = index;
  }
}
