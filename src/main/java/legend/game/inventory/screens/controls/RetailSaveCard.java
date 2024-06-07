package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.TextColour;
import legend.game.saves.SavedGame;
import legend.game.saves.types.RetailSaveDisplay;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;

import javax.annotation.Nullable;

import static legend.game.SItem.renderCentredText;
import static legend.game.Scus94491BpeSegment_8002.getTimestampPart;

public class RetailSaveCard extends SaveCard<RetailSaveDisplay> {
  final CharacterPortrait[] portraits = new CharacterPortrait[3];
  final DragoonSpirits dragoonSpirits;

  private SavedGame<RetailSaveDisplay> saveData;

  private final Label invalidSave;

  public RetailSaveCard() {
    this.addControl(Glyph.uiElement(76, 76)).setPos(0, 0);
    this.addControl(Glyph.uiElement(77, 77)).setPos(176, 0);

    this.dragoonSpirits = this.addControl(new DragoonSpirits(0));
    this.dragoonSpirits.setPos(205, 27);

    for(int i = 0; i < 3; i++) {
      this.portraits[i] = this.addControl(new CharacterPortrait());
      this.portraits[i].setPos(18 + i * 52, 8);
    }

    this.invalidSave = this.addControl(new Label("Invalid save"));
    this.invalidSave.setPos(258, 47);
    this.invalidSave.setWidth(0);
    this.invalidSave.setHorizontalAlign(Label.HorizontalAlign.CENTRE);
  }

  @Override
  public void setSaveData(@Nullable final SavedGame<RetailSaveDisplay> saveData) {
    this.saveData = saveData;

    if(saveData != null && saveData.isValid()) {
      this.invalidSave.setVisibility(false);
      this.dragoonSpirits.setSpirits(saveData.state.goods_19c[0]);

      for(int i = 0; i < 3; i++) {
        this.portraits[i].setCharId(saveData.state.charIds_88[i]);
      }
    } else {
      this.invalidSave.setVisibility(saveData != null);

      this.dragoonSpirits.setSpirits(0);

      for(int i = 0; i < 3; i++) {
        this.portraits[i].setCharId(-1);
      }
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.saveData != null) {
      if(this.saveData.isValid()) {
        //LAB_80108ba0
        renderCentredText(this.saveData.display.location, x + 258, y + 47, TextColour.BROWN); // Location text

        final GameState52c state = this.saveData.state;

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
        this.renderNumber(302, y + 6, char0.hp_08, 4); // Current HP
        this.renderNumber(332, y + 6, this.saveData.display.maxHp, 4); // Max HP
        this.renderNumber(245, y + 17, state.gold_94, 8); // Gold
        this.renderNumber(306, y + 17, getTimestampPart(state.timestamp_a0, 0), 3); // Time played hour
        this.renderCharacter(324, y + 17, 10); // Hour-minute colon
        this.renderNumber(330, y + 17, getTimestampPart(state.timestamp_a0, 1), 2, 0x1); // Time played minute
        this.renderCharacter(342, y + 17, 10); // Minute-second colon
        this.renderNumber(348, y + 17, getTimestampPart(state.timestamp_a0, 2), 2, 0x1); // Time played second
        this.renderNumber(348, y + 34, state.stardust_9c, 2); // Stardust
      }
    }
  }
}
