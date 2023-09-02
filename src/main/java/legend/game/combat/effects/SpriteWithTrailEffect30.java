package legend.game.combat.effects;

import legend.core.gte.VECTOR;

import java.util.Arrays;

public class SpriteWithTrailEffect30 implements Effect {
  /**
   * <ul>
   *   <li>0x4 - use colour</li>
   *   <li>0x8 - use scale</li>
   * </ul>
   */
  public int colourAndScaleFlags_00;
  public int effectFlag_04;
  public int countCopies_08;
  public int countTransformSteps_0c;
  public int colourAndScaleTransformModifier_10;
  /** This value is always modified when indexing to remain within the range of count */
  public int translationIndexBase_14;
  public VECTOR[] instanceTranslations_18;
  public Sub subEffect_1c;

  public SpriteWithTrailEffect30(final int count) {
    this.countCopies_08 = count;
    if(count != 0) {
      this.instanceTranslations_18 = new VECTOR[count];
      Arrays.setAll(this.instanceTranslations_18, i -> new VECTOR());
    } else {
      this.instanceTranslations_18 = null;
    }
  }

  public static class Sub {
    public int flags_00;
  }
}
