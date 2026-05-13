package legend.game.combat.bent;

import legend.game.combat.Battle;

public class SetBattleEntityStatEvent extends BattleEvent {
  public final BattleEntity27c bent;
  public final BattleEntityStat stat;
  public int value;

  public SetBattleEntityStatEvent(final Battle battle, final BattleEntity27c bent, final BattleEntityStat stat, final int value) {
    super(battle);
    this.bent = bent;
    this.stat = stat;
    this.value = value;
  }
}
