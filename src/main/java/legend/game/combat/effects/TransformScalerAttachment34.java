package legend.game.combat.effects;

import legend.core.gte.VECTOR;
import legend.game.combat.types.BattleObject;

public class TransformScalerAttachment34 extends EffectAttachment {
  /** 12-bit fixed-point */
  public final VECTOR value_0c = new VECTOR();
  /** 12-bit fixed-point */
  public final VECTOR velocity_18 = new VECTOR();
  /** 12-bit fixed-point */
  public final VECTOR acceleration_24 = new VECTOR();
  public BattleObject parent_30;
  public int ticksRemaining_32;
}
