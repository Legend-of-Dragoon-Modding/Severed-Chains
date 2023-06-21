package legend.game.combat.effects;

import legend.core.gte.VECTOR;

public class MonsterDeathEffectObjectDestructor30 {
  public final int index;

  /** byte; -1 = visible, 0 = invisible, 1 = fading */
  public int destructionState_00;
  /** byte */
  public int stepCount_01;

  public int scaleModifier_04;
  public int scaleModifierVelocity_08;
  public int angleModifier_0c;
  public int angleModifierVelocity_10;
  public final VECTOR translation_14 = new VECTOR();

  /** shorts */
  public int r_24;
  public int g_26;
  public int b_28;
  public int stepR_2a;
  public int stepG_2c;
  public int stepB_2e;
  
  public MonsterDeathEffectObjectDestructor30(final int index) {
    this.index = index;
  }
}
