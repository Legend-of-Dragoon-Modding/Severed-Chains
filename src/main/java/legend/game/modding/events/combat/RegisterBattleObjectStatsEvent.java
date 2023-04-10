package legend.game.modding.events.combat;

import legend.game.characters.StatType;
import legend.game.combat.bobj.BattleObjectType;
import legend.game.modding.events.Event;

import java.util.Set;

public class RegisterBattleObjectStatsEvent extends Event {
  public final BattleObjectType type;
  private final Set<StatType> stats;

  public RegisterBattleObjectStatsEvent(final BattleObjectType type, final Set<StatType> stats) {
    this.type = type;
    this.stats = stats;
  }

  public void addStat(final StatType statType) {
    this.stats.add(statType);
  }
}
