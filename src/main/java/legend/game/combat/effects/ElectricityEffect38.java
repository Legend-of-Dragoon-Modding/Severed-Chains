package legend.game.combat.effects;

import legend.core.memory.types.QuadConsumer;

import java.util.Arrays;

public class ElectricityEffect38 implements Effect {
  /** ushort */
  public int boltCount_00;

  public int currentColourFadeStep_04;
  public int scriptIndex_08;
  public int numColourFadeSteps_0c;
  public int boltAngleStep_10;
  /** ushort; If true, add origin translation of current segment to that of previous segment */
  public boolean addSuccessiveSegmentOriginTranslations_14;
  /** int; Related to which rendering branch to use in 80103db0 */
  public boolean type1RendererType_18;
  /** short; The lower the value, the wider the angle in which the bolt can be drawn */
  public int boltAngleRangeCutoff_1c;
  /** short; Length of hypotenuse of translation, most often added to segment origin */
  public int segmentOriginTranslationMagnitude_1e;
  /** short */
  public int callbackIndex_20;
  /** ubyte */
  public boolean colourShouldFade_22;
  /** ubyte; If true, colour will be progressively faded for each successive segment. */
  public boolean fadeSuccessiveSegments_23;
  /** byte; If true, re-call initializeRadialElectricityNodes in renderer */
  public boolean reinitializeNodes_24;

  /** ushort */
  public int segmentOriginTranslationModifier_26;
  /** ubyte */
  public int boltSegmentCount_28;
  /** ubyte; If true, render monochrome base triangles */
  public boolean hasMonochromeBase_29;
  /** ubyte; Effect is only meant to send new render commands every other frame if manager._10._24 != 0 */
  public int frameNum_2a;

  public QuadConsumer<EffectManagerData6c<EffectManagerData6cInner.ElectricityType>, ElectricityEffect38, LightningBoltEffect14, Integer> callback_2c;

  public LightningBoltEffect14[] bolts_34;

  public ElectricityEffect38(final int boltCount, final int boltSegmentCount) {
    this.boltCount_00 = boltCount;
    this.boltSegmentCount_28 = boltSegmentCount;
    this.bolts_34 = new LightningBoltEffect14[boltCount];
    Arrays.setAll(this.bolts_34, i -> new LightningBoltEffect14(i, boltSegmentCount));
  }
}
