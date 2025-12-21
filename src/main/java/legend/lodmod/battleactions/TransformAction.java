package legend.lodmod.battleactions;

import legend.core.QueuedModelStandard;
import legend.game.combat.Battle;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;
import legend.game.types.Translucency;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.combat.ui.BattleHud.ICON_SIZE;
import static legend.game.combat.ui.BattleHud.battleMenuIconHeights_800fb6bc;
import static legend.game.combat.ui.BattleMenuStruct58.battleMenuIconMetrics_800fb674;
import static legend.lodmod.LodGoods.DIVINE_DRAGOON_SPIRIT;

public class TransformAction extends RetailBattleAction {
  private static final FontOptions FONT = new FontOptions().size(0.67f).colour(TextColour.WHITE).shadowColour(TextColour.BLACK).horizontalAlign(HorizontalAlign.CENTRE);

  public TransformAction() {
    super(2);
  }

  @Override
  public void draw(final Battle battle, final int index, final boolean selected) {
    final BattleMenuStruct58 menu = battle.hud.battleMenu_800c6c34;

    final int iconId = 1;
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

    if(selected && menu.renderSelectedIconText_40) {
      FONT.colour(menu.player_04.getElement().colour);

      final int oldZ = textZ_800bdf00;
      textZ_800bdf00 = 124;
      renderText(I18n.translate(this), menuElementBaseX + ICON_SIZE / 2.0f, menu.y_08 - 24.0f, FONT);
      textZ_800bdf00 = oldZ;
    }

    // Combat menu icons
    //LAB_800f6d70
    menu.transforms.transfer.set(menuElementBaseX, menuElementBaseY, 123.8f);

    final QueuedModelStandard model = RENDERER.queueOrthoModel(menu.menuObj, menu.transforms, QueuedModelStandard.class)
      .translucency(Translucency.of(battleMenuIconMetrics_800fb674[1].translucencyMode_06));

    if(menu.player_04.charId_272 != 0 || !gameState_800babc8.goods_19c.has(DIVINE_DRAGOON_SPIRIT)) {
      model.vertices(menu.actionDragoonIconObjOffset + menu.player_04.charId_272 * 12 + iconState * 4, 4);
    } else {
      model.vertices(menu.actionDragoonIconObjOffset + 9 * 12 + iconState * 4, 4);

      if(iconState != 0) {
        // Divine dragoon spirit overlay
        //LAB_800f6de0
        RENDERER.queueOrthoModel(menu.menuObj, menu.transforms, QueuedModelStandard.class)
          .vertices(menu.divineSpiritObjOffset + (iconState - 1) * 4, 4)
          .translucency(Translucency.B_PLUS_F);
      }
    }
  }
}
