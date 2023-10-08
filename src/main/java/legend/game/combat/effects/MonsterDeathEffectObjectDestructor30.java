package legend.game.combat.effects;

import org.joml.Vector3f;

public class MonsterDeathEffectObjectDestructor30 {
  public final int index;

  /** byte; -1 = visible, 0 = invisible, 1 = fading */
  public int destructionState_00;
  /** byte */
  public int stepCount_01;

  public float scaleModifier_04;
  public float scaleModifierVelocity_08;
  public float angleModifier_0c;
  public float angleModifierVelocity_10;
  public final Vector3f translation_14 = new Vector3f();

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
