package legend.game.combat.postbattleactions;

import legend.game.combat.Battle;
import legend.game.inventory.WhichMenu;
import legend.game.scripting.RunningScript;

import static legend.game.Menus.whichMenu_800bdc38;

public class MerchantPostBattleAction extends PostBattleAction<MerchantPostBattleActionInstance, MerchantPostBattleAction> {
  @Override
  protected int getTotalDuration(final Battle battle, final MerchantPostBattleActionInstance inst) {
    return 15;
  }

  @Override
  protected int getFadeDuration(final Battle battle, final MerchantPostBattleActionInstance inst) {
    return 30;
  }

  @Override
  protected void onCameraFadeoutStart(final Battle battle, final MerchantPostBattleActionInstance inst) {

  }

  @Override
  protected void onCameraFadeoutFinish(final Battle battle, final MerchantPostBattleActionInstance inst) {

  }

  @Override
  protected void performAction(final Battle battle, final MerchantPostBattleActionInstance inst) {
    whichMenu_800bdc38 = WhichMenu.NONE_0;
  }

  @Override
  public MerchantPostBattleActionInstance inst(final RunningScript<?> script) {
    return this.inst();
  }

  public MerchantPostBattleActionInstance inst() {
    return new MerchantPostBattleActionInstance(this);
  }
}
