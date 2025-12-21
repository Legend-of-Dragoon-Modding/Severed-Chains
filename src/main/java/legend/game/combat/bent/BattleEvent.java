package legend.game.combat.bent;

import legend.game.combat.Battle;
import org.legendofdragoon.modloader.events.Event;

public class BattleEvent extends Event {
  public final Battle battle;

  public BattleEvent(final Battle battle) {
    this.battle = battle;
  }
}
