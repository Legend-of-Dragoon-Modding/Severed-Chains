package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;
import legend.game.combat.bent.PlayerBattleEntity;

public class ArcherSpEvent extends BattleEvent {
  public final PlayerBattleEntity bent;
  public int sp;

  public ArcherSpEvent(final Battle battle, final PlayerBattleEntity bent, final int sp) {
    super(battle);
    this.bent = bent;
    this.sp = sp;
  }
}
