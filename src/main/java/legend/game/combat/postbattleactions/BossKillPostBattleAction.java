package legend.game.combat.postbattleactions;

import legend.game.combat.Battle;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.PostBattleScreen;
import legend.game.scripting.RunningScript;

import static legend.game.Menus.whichMenu_800bdc38;
import static legend.game.SItem.menuStack;

public class BossKillPostBattleAction extends PostBattleAction<BossKillPostBattleActionInstance, BossKillPostBattleAction> {
  @Override
  protected int getTotalDuration(final Battle battle, final BossKillPostBattleActionInstance inst) {
    return 15;
  }

  @Override
  protected int getFadeDuration(final Battle battle, final BossKillPostBattleActionInstance inst) {
    return 30;
  }

  @Override
  protected void onCameraFadeoutStart(final Battle battle, final BossKillPostBattleActionInstance inst) {

  }

  @Override
  protected void onCameraFadeoutFinish(final Battle battle, final BossKillPostBattleActionInstance inst) {

  }

  @Override
  protected void performAction(final Battle battle, final BossKillPostBattleActionInstance inst) {
    whichMenu_800bdc38 = WhichMenu.RENDER_NEW_MENU;
    menuStack.pushScreen(new PostBattleScreen());
  }

  @Override
  public BossKillPostBattleActionInstance inst(final RunningScript<?> script) {
    return this.inst();
  }

  public BossKillPostBattleActionInstance inst() {
    return new BossKillPostBattleActionInstance(this);
  }
}
