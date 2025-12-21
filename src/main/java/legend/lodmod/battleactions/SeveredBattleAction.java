package legend.lodmod.battleactions;

import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.game.combat.Battle;
import legend.game.combat.ui.BattleAction;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.combat.ui.BattleHud.ICON_SIZE;

public abstract class SeveredBattleAction extends BattleAction {
  private static final FontOptions FONT = new FontOptions().size(0.67f).colour(TextColour.WHITE).shadowColour(TextColour.BLACK).horizontalAlign(HorizontalAlign.CENTRE);

  private final int iconIndex;

  protected SeveredBattleAction(final int iconIndex) {
    this.iconIndex = iconIndex;
  }

  @Override
  public void draw(final Battle battle, final int index, final boolean selected) {
    final BattleMenuStruct58 menu = battle.hud.battleMenu_800c6c34;
    final int menuElementBaseX = menu.x_06 - menu.xShiftOffset_0a + index * 19;
    final int menuElementBaseY = menu.y_08 - 16;

    final MV transforms = new MV();
    transforms.scaling(16.0f, 16.0f, 1.0f);
    transforms.transfer.set(menuElementBaseX, menuElementBaseY, 123.8f);

    final int iconStride = battle.hud.battleIconsTexture.width / 16;
    final float iconU = this.iconIndex % iconStride * ICON_SIZE / (float)battle.hud.battleIconsTexture.width;
    final float iconV = this.iconIndex / iconStride * ICON_SIZE / (float)battle.hud.battleIconsTexture.height;

    RENDERER.queueOrthoModel(battle.hud.battleIconQuad, transforms, QueuedModelStandard.class)
      .uvOffset(iconU, iconV)
      .texture(battle.hud.battleIconsTexture)
      .useTextureAlpha();

    if(selected && menu.renderSelectedIconText_40) {
      final int oldZ = textZ_800bdf00;
      textZ_800bdf00 = 124;
      renderText(I18n.translate(this), menuElementBaseX + ICON_SIZE / 2.0f, menuElementBaseY - 8.0f, FONT);
      textZ_800bdf00 = oldZ;
    }
  }
}
