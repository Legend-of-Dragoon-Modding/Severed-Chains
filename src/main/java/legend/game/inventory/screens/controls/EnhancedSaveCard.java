package legend.game.inventory.screens.controls;

import legend.core.opengl.Texture;
import legend.game.input.InputAction;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.TextColour;
import legend.game.saves.SavedGame;
import legend.game.saves.types.EnhancedSaveDisplay;
import legend.game.unpacker.FileData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static legend.game.SItem.renderCentredText;
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment_8002.getTimestampPart;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;

public class EnhancedSaveCard extends SaveCard<EnhancedSaveDisplay> {
  private final Image[] portraits = new Image[3];
  private Image[] spirits;
  private final Glyph highlight;

  private SavedGame<EnhancedSaveDisplay> saveData;

  private final Label invalidSave;

  private Texture texture;

  private int scroll;
  private int selectedCharacter;
  private final List<EnhancedSaveDisplay.Char> chars = new ArrayList<>();

  public EnhancedSaveCard() {
    this.addControl(Glyph.uiElement(76, 76)).setPos(0, 0);
    this.addControl(Glyph.uiElement(77, 77)).setPos(176, 0);

    this.highlight = this.addControl(Glyph.glyph(0x83));
    this.highlight.setPos(26, 8);

    for(int i = 0; i < 3; i++) {
      this.portraits[i] = this.addControl(new Image());
      this.portraits[i].setPos(18 + i * 52, 8);
      this.portraits[i].setZ(124);
      this.portraits[i].setSize(48, 48);
      this.portraits[i].setTranslucent(true);
    }

    this.invalidSave = this.addControl(new Label("Invalid save"));
    this.invalidSave.setPos(258, 47);
    this.invalidSave.setWidth(0);
    this.invalidSave.setHorizontalAlign(Label.HorizontalAlign.CENTRE);
  }

  private void setTexture(final FileData data) {
    if(this.texture != null) {
      this.texture.delete();
    }

    this.texture = Texture.png(data.toByteBuffer());

    for(int i = 0; i < this.portraits.length; i++) {
      this.portraits[i].setTexture(this.texture);
    }
  }

  @Override
  public void setSaveData(@Nullable final SavedGame<EnhancedSaveDisplay> saveData) {
    this.saveData = saveData;

    if(saveData != null && saveData.isValid()) {
      this.invalidSave.setVisibility(false);
      this.setTexture(saveData.display.icons);

      for(final int party : saveData.display.party) {
        this.chars.add(saveData.display.chars.get(party));
      }

      for(final EnhancedSaveDisplay.Char chr : saveData.display.chars) {
        if(!this.chars.contains(chr)) {
          this.chars.add(chr);
        }
      }

      this.setSelectedCharacter(0);

      if(this.spirits != null) {
        for(int i = 0; i < this.spirits.length; i++) {
          this.removeControl(this.spirits[i]);
        }
      }

      this.spirits = new Image[saveData.display.dragoons.size()];
      for(int i = 0; i < saveData.display.dragoons.size(); i++) {
        final EnhancedSaveDisplay.Dragoon dragoon = saveData.display.dragoons.get(i);

        if(dragoon.iconW != 0 && dragoon.iconH != 0) {
          this.spirits[i] = this.addControl(new Image());
          this.spirits[i].setPos(199 + i * 12, 33);
          this.spirits[i].setZ(124);
          this.spirits[i].setSize(11, 9);
          this.spirits[i].setTranslucent(true);
          this.spirits[i].setTexture(this.texture);
          this.spirits[i].setUv(dragoon.iconU, dragoon.iconV, dragoon.iconW, dragoon.iconH);
        }
      }
    } else {
      this.invalidSave.setVisibility(saveData != null);
      this.highlight.setVisibility(false);

      if(this.spirits != null) {
        for(int i = 0; i < this.spirits.length; i++) {
          if(this.spirits[i] != null) {
            this.removeControl(this.spirits[i]);
          }
        }
      }

      for(int i = 0; i < this.portraits.length; i++) {
        this.portraits[i].hide();
      }
    }
  }

  public void setSelectedCharacter(final int index) {
    this.selectedCharacter = index;
    this.highlight.setX(26 + (index - this.scroll) * 52);

    for(int i = 0; i < this.portraits.length; i++) {
      if(i < this.saveData.display.chars.size()) {
        final EnhancedSaveDisplay.Char chr = this.chars.get(this.scroll + i);
        this.portraits[i].setUv(chr.iconU, chr.iconV, chr.iconW, chr.iconH);
        this.portraits[i].show();
      } else {
        this.portraits[i].hide();
      }
    }
  }

  @Override
  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(this.saveData != null) {
      if(inputAction == InputAction.DPAD_LEFT) {
        if(this.selectedCharacter > 0) {
          if(this.selectedCharacter - this.scroll - 1 < 0) {
            this.scroll--;
          }

          playMenuSound(1);
          this.setSelectedCharacter(this.selectedCharacter - 1);
        }

        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.DPAD_RIGHT) {
        if(this.selectedCharacter < this.saveData.display.chars.size() - 1) {
          if(this.selectedCharacter - this.scroll + 1 > 2) {
            this.scroll++;
          }

          playMenuSound(1);
          this.setSelectedCharacter(this.selectedCharacter + 1);
        }

        return InputPropagation.HANDLED;
      }
    }

    return super.pressedWithRepeatPulse(inputAction);
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.saveData != null) {
      if(this.saveData.isValid()) {
        final EnhancedSaveDisplay display = this.saveData.display;
        final EnhancedSaveDisplay.Char char0 = this.chars.get(this.selectedCharacter);

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
        this.renderNumber(348, y + 34, display.stardust, 2); // Stardust

        for(int i = this.scroll; i < display.party.size(); i++) {
          final int oldTextZ = textZ_800bdf00;
          textZ_800bdf00 = 30;
          renderText("*", x + 56 + (i - this.scroll) * 52, y + 50, TextColour.GREEN);
          textZ_800bdf00 = oldTextZ;
        }
      }
    }
  }

  @Override
  protected void delete() {
    super.delete();

    if(this.texture != null) {
      this.texture.delete();
    }
  }
}
