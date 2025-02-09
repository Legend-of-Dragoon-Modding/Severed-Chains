package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.combat.bent.PlayerBattleEntity;

public class ArcherSpEvent extends BattleEvent {
  public final PlayerBattleEntity bent;
  public int sp;

  public ArcherSpEvent(final PlayerBattleEntity bent, final int sp) {
    this.bent = bent;
    this.sp = sp;
  }
}
