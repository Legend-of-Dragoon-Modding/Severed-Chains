package legend.game.inventory.screens.controls;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.platform.input.InputAction;
import legend.game.inventory.GoodsInventory;
import legend.game.inventory.screens.InputPropagation;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.SeveredSavedGame;
import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;
import legend.game.types.Renderable58;

import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.getTimestampPart;
import static legend.game.SItem.renderFourDigitHp;
import static legend.game.Text.renderText;
import static legend.game.sound.Audio.playMenuSound;

public class SeveredSaveCard extends BlankSaveCard {
  private final CharacterPortrait[] portraits = new CharacterPortrait[3];
  private final Glyph highlight;

  private final SeveredSavedGame savedGame;
  private final IntList charIds = new IntArrayList();

  private int scroll;
  private int selectedCharacter;

  public SeveredSaveCard(final SeveredSavedGame savedGame) {
    this.savedGame = savedGame;

    this.charIds.addAll(savedGame.gameState.charIds_88);

    for(int i = 0; i < savedGame.gameState.charData_32c.length; i++) {
      if(!this.charIds.contains(i) && (savedGame.gameState.charData_32c[i].partyFlags_04 & 0x1) != 0) {
        this.charIds.add(i);
      }
    }

    this.highlight = this.addControl(Glyph.glyph(0x83));
    this.highlight.setPos(26, 8);

    final DragoonSpirits dragoonSpirits = this.addControl(new DragoonSpirits(new GoodsInventory()));
    dragoonSpirits.setPos(205, 27);
    dragoonSpirits.setGoods(this.savedGame.gameState.goods_19c);

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
      //LAB_80108ba0
      renderText(this.savedGame.locationName, x + 258, y + 47, UI_TEXT_CENTERED);

      final GameState52c state = this.savedGame.gameState;

      final int charId = this.charIds.getInt(this.selectedCharacter);
      final CharacterData2c character = state.charData_32c[charId];
      final SeveredSavedGame.CharStats stats = this.savedGame.charStats.get(charId);
      this.renderNumber(224, y + 6, character.level_12, 2);
      this.renderNumber(269, y + 6, character.dlevel_13, 2);
      renderFourDigitHp(302, y + 6, character.hp_08, stats.maxHp, Renderable58.FLAG_DELETE_AFTER_RENDER);
      this.renderNumber(332, y + 6, stats.maxHp, 4);
      this.renderNumber(245, y + 17, state.gold_94, 8);
      this.renderNumber(306, y + 17, getTimestampPart(state.timestamp_a0, 0), 3);
      this.renderCharacter(324, y + 17, 10);
      this.renderNumber(330, y + 17, getTimestampPart(state.timestamp_a0, 1), 2, 0x1);
      this.renderCharacter(342, y + 17, 10);
      this.renderNumber(348, y + 17, getTimestampPart(state.timestamp_a0, 2), 2, 0x1);
      this.renderNumber(344, y + 34, state.stardust_9c, 2);
    }
  }
}
