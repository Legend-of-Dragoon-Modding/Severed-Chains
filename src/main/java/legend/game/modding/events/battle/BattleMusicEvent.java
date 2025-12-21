package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;
import legend.game.combat.encounters.Encounter;

public class BattleMusicEvent extends BattleEvent {
  public int victoryType;
  public int musicIndex;
  public final Encounter encounter;

  public BattleMusicEvent(final Battle battle, final int victoryType, final int musicIndex, final Encounter encounter) {
    super(battle);
    this.victoryType = victoryType;
    this.musicIndex = musicIndex;
    this.encounter = encounter;
  }
}
