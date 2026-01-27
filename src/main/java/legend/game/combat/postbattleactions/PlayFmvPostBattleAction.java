package legend.game.combat.postbattleactions;

import legend.game.EngineStateEnum;
import legend.game.combat.Battle;
import legend.game.fmv.Fmv;
import legend.game.inventory.WhichMenu;
import legend.game.scripting.RunningScript;

import static legend.game.Menus.whichMenu_800bdc38;

public class PlayFmvPostBattleAction extends PostBattleAction<PlayFmvPostBattleActionInstance, PlayFmvPostBattleAction> {
  @Override
  protected int getTotalDuration(final Battle battle, final PlayFmvPostBattleActionInstance inst) {
    return 10;
  }

  @Override
  protected int getFadeDuration(final Battle battle, final PlayFmvPostBattleActionInstance inst) {
    return 45;
  }

  @Override
  protected void onCameraFadeoutStart(final Battle battle, final PlayFmvPostBattleActionInstance inst) {
    battle.cameraScriptMainTableJumpIndex_800c6748 = 211;
  }

  @Override
  protected void onCameraFadeoutFinish(final Battle battle, final PlayFmvPostBattleActionInstance inst) {

  }

  @Override
  protected void performAction(final Battle battle, final PlayFmvPostBattleActionInstance inst) {
    Fmv.playCurrentFmv(inst.fmv, EngineStateEnum.FINAL_FMV_11);
    whichMenu_800bdc38 = WhichMenu.NONE_0;
  }

  @Override
  public PlayFmvPostBattleActionInstance inst(final RunningScript<?> script) {
    return this.inst(script.params_20[1].get());
  }

  public PlayFmvPostBattleActionInstance inst(final int fmv) {
    return new PlayFmvPostBattleActionInstance(this, fmv);
  }
}
