package legend.game.combat.effects;

import legend.core.gte.TmdObjTable1c;
import legend.game.combat.deff.Lmb;
import legend.game.combat.deff.LmbTransforms14;
import legend.game.combat.types.SpriteMetrics08;

public class BttlScriptData6cSub5c implements BttlScriptData6cSubBase1 {
  public int lmbType_00;
  public int _04;
  public int _08;
  public Lmb lmb_0c;
  /** May have two copies of the LMB table for some reason */
  public LmbTransforms14[] ptr_10;
  public final int[] _14 = new int[8];
  public int _34;
  public int _38;
  public int _3c;
  public int _40;

  public int deffTmdFlags_48;
  public TmdObjTable1c deffTmdObjTable_4c;
  public int deffSpriteFlags_50;
  public final SpriteMetrics08 metrics_54 = new SpriteMetrics08();
}
