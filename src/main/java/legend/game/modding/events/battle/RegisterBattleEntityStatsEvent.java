package legend.game.modding.events.battle;

import legend.game.characters.StatType;
import legend.game.combat.bent.BattleEntityType;
import org.legendofdragoon.modloader.events.Event;

import java.util.Set;

public class RegisterBattleEntityStatsEvent extends Event {
  public final BattleEntityType type;
  private final Set<StatType> stats;

  public RegisterBattleEntityStatsEvent(final BattleEntityType type, final Set<StatType> stats) {
    this.type = type;
    this.stats = stats;
  }

  public void addStat(final StatType statType) {
    this.stats.add(statType);
  }
}
