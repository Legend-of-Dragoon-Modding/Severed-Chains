package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;

public class ScriptLoadDeffEvent extends BattleEvent {
  public int flagsAndIndex;
  public int bentIndex;
  public int p2;
  public int scriptEntrypoint;
  public int type;

  public ScriptLoadDeffEvent(final Battle battle, final int flagsAndIndex, final int bentIndex, final int p2, final int scriptEntrypoint, final int type) {
    super(battle);
    this.flagsAndIndex = flagsAndIndex;
    this.bentIndex = bentIndex;
    this.p2 = p2;
    this.scriptEntrypoint = scriptEntrypoint;
    this.type = type;
  }
}
