package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;

import java.nio.file.Path;

public class LoadDeffEvent extends BattleEvent {
  public Path tims;
  public Path deff;

  public LoadDeffEvent(final Battle battle, final Path tims, final Path deff) {
    super(battle);
    this.tims = tims;
    this.deff = deff;
  }
}
