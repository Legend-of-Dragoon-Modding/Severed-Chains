package legend.game.combat.effects;

import legend.core.gte.TmdObjTable1c;
import legend.game.combat.deff.Lmb;
import legend.game.combat.deff.LmbTransforms14;

public class LmbAnimationEffect5c implements Effect {
  public int lmbType_00;
  public int _04;
  public int keyframeCount_08;
  public Lmb lmb_0c;
  /** May have two copies of the LMB table for some reason */
  public LmbTransforms14[] lmbTransforms_10;
  public final int[] _14 = new int[8];
  /**
   * <ul>
   *   <li>0x4 - colour</li>
   *   <li>0x8 - scale</li>
   * </ul>
   */
  public int flags_34;
  public int _38;
  public int _3c;
  public int _40;

  public int deffTmdFlags_48;
  public TmdObjTable1c deffTmdObjTable_4c;
  public int deffSpriteFlags_50;
  public final SpriteMetrics08 metrics_54 = new SpriteMetrics08();
}
