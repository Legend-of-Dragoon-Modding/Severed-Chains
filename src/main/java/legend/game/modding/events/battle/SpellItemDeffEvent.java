package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;

public class SpellItemDeffEvent extends BattleEvent {
  public int scriptId;
  public int s0;

  public SpellItemDeffEvent(final Battle battle, final int scriptId, final int s0) {
    super(battle);
    this.scriptId = scriptId;
    this.s0 = s0;
  }
}
