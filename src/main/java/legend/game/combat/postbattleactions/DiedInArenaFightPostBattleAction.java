package legend.game.combat.postbattleactions;

import legend.game.combat.Battle;
import legend.game.inventory.WhichMenu;
import legend.game.modding.events.inventory.ScriptFlags2ChangedEvent;
import legend.game.scripting.RunningScript;

import static legend.core.GameEngine.EVENTS;
import static legend.game.Menus.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class DiedInArenaFightPostBattleAction extends GameOverPostBattleAction {
  @Override
  protected void performAction(final Battle battle, final GameOverPostBattleActionInstance inst) {
    //LAB_800c8514
    final boolean set = EVENTS.postEvent(new ScriptFlags2ChangedEvent(0x3bb, true)).set;
    gameState_800babc8.scriptFlags2_bc.set(29, 27, set); // Died in arena fight
    whichMenu_800bdc38 = WhichMenu.NONE_0;
  }

  @Override
  public GameOverPostBattleActionInstance inst(final RunningScript<?> script) {
    return this.inst();
  }

  public GameOverPostBattleActionInstance inst() {
    return new GameOverPostBattleActionInstance(this);
  }
}
