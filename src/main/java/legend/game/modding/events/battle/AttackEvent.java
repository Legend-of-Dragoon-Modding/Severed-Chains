package legend.game.modding.events.battle;

import legend.game.combat.bobj.BattleObject27c;
import legend.game.combat.types.AttackType;

public class AttackEvent extends BattleEvent {
  public final BattleObject27c attacker;
  public final BattleObject27c defender;
  public final AttackType attackType;
  public int damage;

  public AttackEvent(final BattleObject27c attacker, final BattleObject27c defender, final AttackType attackType, final int damage) {
    this.attacker = attacker;
    this.defender = defender;
    this.attackType = attackType;
    this.damage = damage;
  }
}
