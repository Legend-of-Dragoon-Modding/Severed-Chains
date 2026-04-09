package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.FontOptions;
import legend.game.saves.RetailSavedGame;
import legend.game.types.Renderable58;

import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.getTimestampPart;
import static legend.game.SItem.renderFourDigitHp;
import static legend.game.Text.renderText;

public class RetailSaveCard extends BlankSaveCard {
  private static final FontOptions TINY = new FontOptions().set(UI_TEXT).size(0.4f);

  private final RetailSavedGame savedGame;

  public RetailSaveCard(final RetailSavedGame savedGame) {
    this.savedGame = savedGame;

    final DragoonSpirits dragoonSpirits = this.addControl(new DragoonSpirits(this.savedGame.goodsIds));
    dragoonSpirits.setPos(205, 27);

    for(int i = 0; i < 3; i++) {
      final CharacterPortrait[] portraits = new CharacterPortrait[3];
      portraits[i] = this.addControl(new CharacterPortrait());
      portraits[i].setPos(18 + i * 52, 8);
      portraits[i].setCharId(i < savedGame.charIds.size() ? savedGame.charIds.getInt(i) : -1);
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.savedGame != null) {
      renderText(this.savedGame.version, x - 3, y + 2, TINY);

      //LAB_80108ba0
      renderText(this.savedGame.locationName, x + 258, y + 47, UI_TEXT_CENTERED);

      final int firstCharId = this.savedGame.charIds.getInt(0);
      final RetailSavedGame.CharStats character = this.savedGame.charStats[firstCharId];
      this.renderNumber(224, y + 6, character.level, 2); // Level
      this.renderNumber(269, y + 6, character.dlevel, 2); // Dragoon level
      renderFourDigitHp(302, y + 6, character.hp, this.savedGame.maxHp, Renderable58.FLAG_DELETE_AFTER_RENDER); // Current HP
      this.renderNumber(332, y + 6, this.savedGame.maxHp, 4); // Max HP
      this.renderNumber(245, y + 17, this.savedGame.gold, 8); // Gold
      this.renderNumber(306, y + 17, getTimestampPart(this.savedGame.timestamp, 0), 3); // Time played hour
      this.renderCharacter(324, y + 17, 10); // Hour-minute colon
      this.renderNumber(330, y + 17, getTimestampPart(this.savedGame.timestamp, 1), 2, 0x1); // Time played minute
      this.renderCharacter(342, y + 17, 10); // Minute-second colon
      this.renderNumber(348, y + 17, getTimestampPart(this.savedGame.timestamp, 2), 2, 0x1); // Time played second
      this.renderNumber(344, y + 34, this.savedGame.stardust, 2); // Stardust
    }
  }
}
