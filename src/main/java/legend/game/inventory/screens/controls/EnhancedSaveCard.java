package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.TextColour;
import legend.game.saves.SavedGame;
import legend.game.saves.types.EnhancedSaveDisplay;

import javax.annotation.Nullable;

import static legend.game.SItem.renderCentredText;
import static legend.game.Scus94491BpeSegment_8002.getTimestampPart;

public class EnhancedSaveCard extends SaveCard<EnhancedSaveDisplay> {
  final CharacterPortrait[] portraits = new CharacterPortrait[3];
  final DragoonSpirits dragoonSpirits;

  private SavedGame<EnhancedSaveDisplay> saveData;

  private final Label invalidSave;

  public EnhancedSaveCard() {
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
  public void setSaveData(@Nullable final SavedGame<EnhancedSaveDisplay> saveData) {
    this.saveData = saveData;

    if(saveData != null && saveData.isValid()) {
      this.invalidSave.setVisibility(false);
      this.dragoonSpirits.setSpirits(saveData.state.goods_19c[0]); //TODO

      for(int i = 0; i < 3; i++) {
        if(i < saveData.display.party.size()) {
          this.portraits[i].setCharId(saveData.display.party.getInt(i));
        } else {
          this.portraits[i].setCharId(-1);
        }
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
        final EnhancedSaveDisplay display = this.saveData.display;
        final EnhancedSaveDisplay.Char char0 = display.chars.get(display.party.getInt(0));

        renderCentredText(display.location, x + 258, y + 47, TextColour.BROWN); // Location text
        this.renderNumber(224, y + 6, char0.lvl, 2); // Level
        this.renderNumber(269, y + 6, char0.dlvl, 2); // Dragoon level
        this.renderNumber(302, y + 6, char0.hp, 4); // Current HP
        this.renderNumber(332, y + 6, char0.maxHp, 4); // Max HP
        this.renderNumber(245, y + 17, display.gold, 8); // Gold
        this.renderNumber(306, y + 17, getTimestampPart(display.time, 0), 3); // Time played hour
        this.renderCharacter(324, y + 17, 10); // Hour-minute colon
        this.renderNumber(330, y + 17, getTimestampPart(display.time, 1), 2, 0x1); // Time played minute
        this.renderCharacter(342, y + 17, 10); // Minute-second colon
        this.renderNumber(348, y + 17, getTimestampPart(display.time, 2), 2, 0x1); // Time played second
        this.renderNumber(344, y + 34, display.stardust, 2); // Stardust
      }
    }
  }
}
