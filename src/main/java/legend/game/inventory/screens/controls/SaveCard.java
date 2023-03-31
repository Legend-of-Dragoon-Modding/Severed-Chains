package legend.game.inventory.screens.controls;

import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.Pointer;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.TextColour;
import legend.game.saves.SavedGame;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.types.LodString;

import javax.annotation.Nullable;

import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.levelStuff_800fbd30;
import static legend.game.SItem.renderCentredText;
import static legend.game.SItem.submapNames_8011c108;
import static legend.game.SItem.worldMapNames_8011c1ec;
import static legend.game.Scus94491BpeSegment_8002.getTimestampPart;

public class SaveCard extends Control {
  final CharacterPortrait[] portraits = new CharacterPortrait[3];
  final DragoonSpirits dragoonSpirits;

  private SavedGame saveData;

  public SaveCard() {
    this.addControl(Glyph.uiElement(76, 76)).setPos(0, 0);
    this.addControl(Glyph.uiElement(77, 77)).setPos(176, 0);

    this.dragoonSpirits = this.addControl(new DragoonSpirits(0));
    this.dragoonSpirits.setPos(205, 27);

    for(int i = 0; i < 3; i++) {
      this.portraits[i] = this.addControl(new CharacterPortrait());
      this.portraits[i].setPos(18 + i * 52, 8);
    }
  }

  public void setSaveData(@Nullable final SavedGame saveData) {
    this.saveData = saveData;

    if(saveData != null) {
      this.dragoonSpirits.setSpirits(saveData.state().goods_19c[0]);

      for(int i = 0; i < 3; i++) {
        this.portraits[i].setCharId(saveData.state().charIds_88[i]);
      }
    } else {
      this.dragoonSpirits.setSpirits(0);

      for(int i = 0; i < 3; i++) {
        this.portraits[i].setCharId(-1);
      }
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.saveData != null) {
      final ArrayRef<Pointer<LodString>> locationNames;
      if(this.saveData.locationType() == 1) {
        //LAB_80108b5c
        locationNames = worldMapNames_8011c1ec;
      } else if(this.saveData.locationType() == 3) {
        //LAB_80108b78
        locationNames = chapterNames_80114248;
      } else {
        //LAB_80108b90
        locationNames = submapNames_8011c108;
      }

      //LAB_80108ba0
      renderCentredText(locationNames.get(this.saveData.locationIndex()).deref(), x + 258, y + 47, TextColour.BROWN); // Location text

      final GameState52c state = this.saveData.state();
      final CharacterData2c char0 = state.charData_32c[0];
      this.renderNumber(224, y + 6, char0.level_12, 2); // Level
      this.renderNumber(269, y + 6, char0.dlevel_13, 2); // Dragoon level
      this.renderNumber(302, y + 6, char0.hp_08, 4); // Current HP
      this.renderNumber(332, y + 6, levelStuff_800fbd30.get(state.charIds_88[0]).deref().get(char0.level_12).hp_00.get(), 4); // Max HP
      this.renderNumber(245, y + 17, state.gold_94, 8); // Gold
      this.renderNumber(306, y + 17, getTimestampPart(state.timestamp_a0, 0), 3); // Time played hour
      this.renderCharacter(324, y + 17, 10); // Hour-minute colon
      this.renderNumber(330, y + 17, getTimestampPart(state.timestamp_a0, 1), 2, 0x1); // Time played minute
      this.renderCharacter(342, y + 17, 10); // Minute-second colon
      this.renderNumber(348, y + 17, getTimestampPart(state.timestamp_a0, 2), 2, 0x1); // Time played second
      this.renderNumber(344, y + 34, state.stardust_9c, 2); // Stardust
//      renderDragoonSpirits(state.goods_19c[0], 223, y + 27);
    }
  }
}
