package legend.game.inventory.screens.controls;

import legend.game.inventory.GoodsInventory;
import legend.game.saves.RetailSavedGame;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.types.Renderable58;

import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.getTimestampPart;
import static legend.game.SItem.renderFourDigitHp;
import static legend.game.Text.renderText;

public class RetailSaveCard extends BlankSaveCard {
  private final RetailSavedGame savedGame;

  public RetailSaveCard(final RetailSavedGame savedGame) {
    this.savedGame = savedGame;

    final DragoonSpirits dragoonSpirits = this.addControl(new DragoonSpirits(new GoodsInventory()));
    dragoonSpirits.setPos(205, 27);
    dragoonSpirits.setGoods(this.savedGame.gameState.goods_19c);

    for(int i = 0; i < 3; i++) {
      final CharacterPortrait[] portraits = new CharacterPortrait[3];
      portraits[i] = this.addControl(new CharacterPortrait());
      portraits[i].setPos(18 + i * 52, 8);
      portraits[i].setCharId(i < savedGame.gameState.charIds_88.size() ? savedGame.gameState.charIds_88.getInt(i) : -1);
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.savedGame != null) {
      //LAB_80108ba0
      renderText(this.savedGame.locationName, x + 258, y + 47, UI_TEXT_CENTERED);

      final GameState52c state = this.savedGame.gameState;

      final int firstCharId = state.charIds_88.getInt(0);
      final CharacterData2c character = state.charData_32c[firstCharId];
      this.renderNumber(224, y + 6, character.level_12, 2); // Level
      this.renderNumber(269, y + 6, character.dlevel_13, 2); // Dragoon level
      renderFourDigitHp(302, y + 6, character.hp_08, this.savedGame.maxHp, Renderable58.FLAG_DELETE_AFTER_RENDER); // Current HP
      this.renderNumber(332, y + 6, this.savedGame.maxHp, 4); // Max HP
      this.renderNumber(245, y + 17, state.gold_94, 8); // Gold
      this.renderNumber(306, y + 17, getTimestampPart(state.timestamp_a0, 0), 3); // Time played hour
      this.renderCharacter(324, y + 17, 10); // Hour-minute colon
      this.renderNumber(330, y + 17, getTimestampPart(state.timestamp_a0, 1), 2, 0x1); // Time played minute
      this.renderCharacter(342, y + 17, 10); // Minute-second colon
      this.renderNumber(348, y + 17, getTimestampPart(state.timestamp_a0, 2), 2, 0x1); // Time played second
      this.renderNumber(344, y + 34, state.stardust_9c, 2); // Stardust
    }
  }
}
