package legend.game.combat.bent;

import legend.game.combat.Battle;
import legend.game.combat.types.AttackType;

public class AttackEvent extends BattleEvent {
  public final BattleEntity27c attacker;
  public final BattleEntity27c defender;
  public final AttackType attackType;
  public int damage;

  public AttackEvent(final Battle battle, final BattleEntity27c attacker, final BattleEntity27c defender, final AttackType attackType, final int damage) {
    super(battle);
    this.attacker = attacker;
    this.defender = defender;
    this.attackType = attackType;
    this.damage = damage;
  }
}
