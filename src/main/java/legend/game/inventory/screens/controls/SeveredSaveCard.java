package legend.game.inventory.screens.controls;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.platform.input.InputAction;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.InputPropagation;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.SeveredSavedGame;
import legend.game.types.Renderable58;

import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.getTimestampPart;
import static legend.game.SItem.renderFourDigitHp;
import static legend.game.Text.renderText;
import static legend.game.sound.Audio.playMenuSound;

public class SeveredSaveCard extends BlankSaveCard {
  private static final FontOptions TINY = new FontOptions().set(UI_TEXT).size(0.4f);

  private final CharacterPortrait[] portraits = new CharacterPortrait[3];
  private final Glyph highlight;

  private final SeveredSavedGame savedGame;
  private final IntList charIds = new IntArrayList();

  private int scroll;
  private int selectedCharacter;

  public SeveredSaveCard(final SeveredSavedGame savedGame) {
    this.savedGame = savedGame;

    this.charIds.addAll(savedGame.charIds);

    for(int i = 0; i < savedGame.charStats.size(); i++) {
      if(!this.charIds.contains(i) && (savedGame.charStats.get(i).flags & 0x1) != 0) {
        this.charIds.add(i);
      }
    }

    this.highlight = this.addControl(Glyph.glyph(0x83));
    this.highlight.setPos(26, 8);

    final DragoonSpirits dragoonSpirits = this.addControl(new DragoonSpirits(this.savedGame.goodsIds));
    dragoonSpirits.setPos(205, 27);

    for(int i = 0; i < this.portraits.length; i++) {
      this.portraits[i] = this.addControl(new CharacterPortrait());
      this.portraits[i].setPos(18 + i * 52, 8);
      this.portraits[i].setCharId(i < this.charIds.size() ? this.charIds.getInt(i) : -1);
    }
  }

  public void setSelectedCharacter(final int index) {
    this.selectedCharacter = index;
    this.highlight.setX(26 + (index - this.scroll) * 52);

    for(int i = 0; i < this.portraits.length; i++) {
      this.portraits[i].setCharId(this.scroll + i < this.charIds.size() ? this.charIds.getInt(this.scroll + i) : -1);
    }
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(this.savedGame != null) {
      if(action == CoreMod.INPUT_ACTION_MENU_LEFT.get()) {
        if(this.selectedCharacter > 0) {
          if(this.selectedCharacter - this.scroll - 1 < 0) {
            this.scroll--;
          }

          playMenuSound(1);
          this.setSelectedCharacter(this.selectedCharacter - 1);
        }

        return InputPropagation.HANDLED;
      }

      if(action == CoreMod.INPUT_ACTION_MENU_RIGHT.get()) {
        if(this.selectedCharacter < this.charIds.size() - 1) {
          if(this.selectedCharacter - this.scroll + 1 > 2) {
            this.scroll++;
          }

          playMenuSound(1);
          this.setSelectedCharacter(this.selectedCharacter + 1);
        }

        return InputPropagation.HANDLED;
      }
    }

    return super.inputActionPressed(action, repeat);
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.savedGame != null) {
      renderText(this.savedGame.version, x - 3, y + 2, TINY);

      //LAB_80108ba0
      renderText(this.savedGame.locationName, x + 258, y + 47, UI_TEXT_CENTERED);

      if(this.selectedCharacter < this.charIds.size()) {
        final int charId = this.charIds.getInt(this.selectedCharacter);
        final SeveredSavedGame.CharStats stats = this.savedGame.charStats.get(charId);
        this.renderNumber(224, y + 6, stats.level, 2);
        this.renderNumber(269, y + 6, stats.dlevel, 2);
        renderFourDigitHp(302, y + 6, stats.hp, stats.maxHp, Renderable58.FLAG_DELETE_AFTER_RENDER);
        this.renderNumber(332, y + 6, stats.maxHp, 4);
        this.renderNumber(245, y + 17, this.savedGame.gold, 8);
        this.renderNumber(306, y + 17, getTimestampPart(this.savedGame.timestamp, 0), 3);
        this.renderCharacter(324, y + 17, 10);
        this.renderNumber(330, y + 17, getTimestampPart(this.savedGame.timestamp, 1), 2, 0x1);
        this.renderCharacter(342, y + 17, 10);
        this.renderNumber(348, y + 17, getTimestampPart(this.savedGame.timestamp, 2), 2, 0x1);
        this.renderNumber(344, y + 34, this.savedGame.stardust, 2);
      }
    }
  }
}
