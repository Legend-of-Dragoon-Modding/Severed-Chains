package legend.game.combat.effects;

import legend.core.gte.VECTOR;

public class GenericSpriteEffectAnimState30 {
  public final int index;

  /** bytes */
  public int _00;
  public int stepCount_01;

  public int _04;
  public int _08;
  public int _0c;
  public int _10;
  public final VECTOR translation_14 = new VECTOR();

  /** shorts */
  public int r_24;
  public int g_26;
  public int b_28;
  public int stepR_2a;
  public int stepG_2c;
  public int stepB_2e;
  
  public GenericSpriteEffectAnimState30(final int index) {
    this.index = index;
  }
}
