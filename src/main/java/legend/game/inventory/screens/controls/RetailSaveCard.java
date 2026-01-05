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
  final CharacterPortrait[] portraits = new CharacterPortrait[3];
  final DragoonSpirits dragoonSpirits;

  private final RetailSavedGame savedGame;

  public RetailSaveCard(final RetailSavedGame savedGame) {
    this.savedGame = savedGame;

    this.dragoonSpirits = this.addControl(new DragoonSpirits(new GoodsInventory()));
    this.dragoonSpirits.setPos(205, 27);
    this.dragoonSpirits.setGoods(this.savedGame.state.goods_19c);

    for(int i = 0; i < 3; i++) {
      this.portraits[i] = this.addControl(new CharacterPortrait());
      this.portraits[i].setPos(18 + i * 52, 8);
      this.portraits[i].setCharId(savedGame.state.charIds_88[i]);
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.savedGame != null) {
      //LAB_80108ba0
      renderText(this.savedGame.locationName, x + 258, y + 47, UI_TEXT_CENTERED);

      final GameState52c state = this.savedGame.state;

      int firstCharId = 0;
      for(int i = 0; i < state.charIds_88.length; i++) {
        if(state.charIds_88[i] != -1) {
          firstCharId = state.charIds_88[i];
          break;
        }
      }

      final CharacterData2c char0 = state.charData_32c[firstCharId];
      this.renderNumber(224, y + 6, char0.level_12, 2); // Level
      this.renderNumber(269, y + 6, char0.dlevel_13, 2); // Dragoon level
      renderFourDigitHp(302, y + 6, char0.hp_08, this.savedGame.maxHp, Renderable58.FLAG_DELETE_AFTER_RENDER); // Current HP
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
