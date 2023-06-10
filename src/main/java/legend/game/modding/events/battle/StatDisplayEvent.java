package legend.game.modding.events.battle;

import legend.game.combat.bobj.BattleEvent;
import legend.game.combat.bobj.PlayerBattleObject;

public class StatDisplayEvent extends BattleEvent {
  public final int charSlot;
  public final PlayerBattleObject player;

  public StatDisplayEvent(final int charSlot, final PlayerBattleObject player) {
    this.charSlot = charSlot;
    this.player = player;
  }
}
