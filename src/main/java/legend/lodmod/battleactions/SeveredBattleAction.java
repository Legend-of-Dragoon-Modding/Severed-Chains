package legend.lodmod.battleactions;

import legend.core.QueuedModelStandard;
import legend.game.combat.Battle;
import legend.game.combat.ui.BattleAction;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;

import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.combat.ui.BattleHud.ICON_SIZE;

public abstract class SeveredBattleAction extends BattleAction {
  private static final FontOptions FONT = new FontOptions().size(0.67f).colour(TextColour.WHITE).shadowColour(TextColour.BLACK).horizontalAlign(HorizontalAlign.CENTRE);

  private final int iconIndex;
  private final int frameCount;
  private int frameIndex;

  protected SeveredBattleAction(final int iconIndex, final int frameCount) {
    this.iconIndex = iconIndex;
    this.frameCount = frameCount;
  }

  protected String getTextTranslationKey() {
    return this.getTranslationKey();
  }

  @Override
  public void draw(final Battle battle, final int index, final boolean selected) {
    if(!selected) {
      this.frameIndex = 0;
    }

    final BattleMenuStruct58 menu = battle.hud.battleMenu_800c6c34;
    final int menuElementBaseX = menu.x_06 - menu.xShiftOffset_0a + index * 19;
    final int menuElementBaseY = menu.y_08 - 16;

    menu.transforms.scaling(16.0f, 16.0f, 1.0f);
    menu.transforms.transfer.set(menuElementBaseX, menuElementBaseY, 123.8f);

    final float iconU = this.frameIndex * ICON_SIZE / (float)battle.hud.battleIconsTexture.width;
    final float iconV = this.iconIndex * ICON_SIZE / (float)battle.hud.battleIconsTexture.height;

    if(tickCount_800bb0fc % 4 == 0) {
      this.frameIndex++;
      this.frameIndex %= this.frameCount;
    }

    RENDERER.queueOrthoModel(battle.hud.battleIconQuad, menu.transforms, QueuedModelStandard.class)
      .uvOffset(iconU, iconV)
      .texture(battle.hud.battleIconsTexture)
      .useTextureAlpha();

    if(selected && menu.renderSelectedIconText_40) {
      final int oldZ = textZ_800bdf00;
      textZ_800bdf00 = 124;
      renderText(I18n.translate(this.getTextTranslationKey()), menuElementBaseX + ICON_SIZE / 2.0f, menuElementBaseY - 8.0f, FONT);
      textZ_800bdf00 = oldZ;
    }
  }
}
