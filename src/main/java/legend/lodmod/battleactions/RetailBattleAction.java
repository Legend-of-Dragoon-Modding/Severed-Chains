package legend.lodmod.battleactions;

import legend.core.QueuedModelStandard;
import legend.game.combat.Battle;
import legend.game.combat.ui.BattleAction;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;
import legend.game.types.Translucency;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.combat.ui.BattleHud.ICON_SIZE;
import static legend.game.combat.ui.BattleHud.battleMenuIconHeights_800fb6bc;
import static legend.game.combat.ui.BattleMenuStruct58.battleMenuIconMetrics_800fb674;

public class RetailBattleAction extends BattleAction {
  private static final FontOptions FONT = new FontOptions().size(0.67f).colour(TextColour.WHITE).shadowColour(TextColour.BLACK).horizontalAlign(HorizontalAlign.CENTRE);

  protected static final int[] battleMenuIconStates_800c71e4 = {0, 1, 2, 1};

  private final int icon;

  public RetailBattleAction(final int icon) {
    this.icon = icon;
  }

  @Override
  public void draw(final Battle battle, final int index, final boolean selected) {
    final BattleMenuStruct58 menu = battle.hud.battleMenu_800c6c34;

    final int iconId = this.icon - 1;
    final int iconState;
    if(selected) {
      iconState = battleMenuIconStates_800c71e4[menu.iconStateIndex_26];
    } else {
      //LAB_800f6c88
      iconState = 0;
    }

    //LAB_800f6c90
    final int menuElementBaseX = menu.x_06 - menu.xShiftOffset_0a + index * 19;
    final int menuElementBaseY = menu.y_08 - battleMenuIconHeights_800fb6bc[iconId][iconState];

    //LAB_800f6d70
    menu.transforms.transfer.set(menuElementBaseX, menuElementBaseY, 123.8f);

    RENDERER.queueOrthoModel(menu.menuObj, menu.transforms, QueuedModelStandard.class)
      .vertices(menu.actionIconObjOffset + iconId * 12 + iconState * 4, 4)
      .translucency(Translucency.of(battleMenuIconMetrics_800fb674[iconId].translucencyMode_06));

    if(selected && menu.renderSelectedIconText_40) {
      final int oldZ = textZ_800bdf00;
      textZ_800bdf00 = 124;
      renderText(I18n.translate(this), menuElementBaseX + ICON_SIZE / 2.0f, menu.y_08 - 24.0f, FONT);
      textZ_800bdf00 = oldZ;
    }
  }
}
