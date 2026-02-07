package legend.game.combat.postbattleactions;

import legend.game.combat.Battle;
import legend.game.inventory.WhichMenu;
import legend.game.scripting.RunningScript;
import legend.lodmod.LodEngineStateTypes;

import static legend.game.EngineStates.postBattleEngineState_800bc91c;
import static legend.game.Menus.whichMenu_800bdc38;
import static legend.game.sound.Audio.sssqFadeOut;

public class GameOverPostBattleAction extends PostBattleAction<GameOverPostBattleActionInstance, GameOverPostBattleAction> {
  @Override
  protected int getTotalDuration(final Battle battle, final GameOverPostBattleActionInstance inst) {
    return 65;
  }

  @Override
  protected int getFadeDuration(final Battle battle, final GameOverPostBattleActionInstance inst) {
    return 45;
  }

  @Override
  protected void onCameraFadeoutStart(final Battle battle, final GameOverPostBattleActionInstance inst) {
    battle.cameraScriptMainTableJumpIndex_800c6748 = 211;
    battle.cameraFocusedBent_800c6914 = battle.currentTurnBent_800c66c8;
  }

  @Override
  protected void onCameraFadeoutFinish(final Battle battle, final GameOverPostBattleActionInstance inst) {
    sssqFadeOut((short)43);
  }

  @Override
  protected void performAction(final Battle battle, final GameOverPostBattleActionInstance inst) {
    //LAB_800c8534
    postBattleEngineState_800bc91c = LodEngineStateTypes.GAME_OVER.get();
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
