package legend.game.combat.postbattleactions;

import legend.game.combat.Battle;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.PostBattleScreen;
import legend.game.scripting.RunningScript;

import static legend.game.Menus.whichMenu_800bdc38;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.combat.bent.BattleEntity27c.FLAG_CURRENT_TURN;

public class VictoryPostBattleAction extends PostBattleAction<VictoryPostBattleActionInstance, VictoryPostBattleAction> {
  @Override
  protected int getTotalDuration(final Battle battle, final VictoryPostBattleActionInstance inst) {
    return 82;
  }

  @Override
  protected int getFadeDuration(final Battle battle, final VictoryPostBattleActionInstance inst) {
    return 30;
  }

  @Override
  protected void onCameraFadeoutStart(final Battle battle, final VictoryPostBattleActionInstance inst) {
    battle.cameraScriptMainTableJumpIndex_800c6748 = 195;

    //LAB_800c8180
    for(int i = 0; i < battleState_8006e398.getPlayerCount(); i++) {
      battleState_8006e398.playerBents_e40.get(i).setFlag(FLAG_CURRENT_TURN);
    }
  }

  @Override
  protected void onCameraFadeoutFinish(final Battle battle, final VictoryPostBattleActionInstance inst) {

  }

  @Override
  protected void performAction(final Battle battle, final VictoryPostBattleActionInstance inst) {
    whichMenu_800bdc38 = WhichMenu.RENDER_NEW_MENU;
    menuStack.pushScreen(new PostBattleScreen());
  }

  @Override
  public VictoryPostBattleActionInstance inst(final RunningScript<?> script) {
    return this.inst();
  }

  public VictoryPostBattleActionInstance inst() {
    return new VictoryPostBattleActionInstance(this);
  }
}
