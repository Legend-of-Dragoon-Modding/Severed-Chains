package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.TextColour;
import legend.game.types.ActiveStatsa0;

import static legend.game.SItem.characterNames_801142dc;
import static legend.game.SItem.getXpToNextLevel;
import static legend.game.SItem.renderCharacterStatusEffect;
import static legend.game.SItem.renderFraction;
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;

public class CharacterCard extends Control {
  private int charId;

  private final CharacterPortrait portrait;

  public CharacterCard(final int charId) {
    this.charId = charId;

    this.setSize(174, 64);

    final Glyph linesText = Glyph.uiElement(74, 74);
    linesText.getRenderable().x_40 += 8;
    linesText.getRenderable().z_3c = 33;
    this.addControl(linesText);

    this.addControl(Glyph.uiElement(153, 153)).getRenderable().x_40 += 8; // Background

    this.portrait = this.addControl(new CharacterPortrait());
    this.portrait.setCharId(charId);
    this.portrait.setPos(8, 8);
  }

  public void setCharId(final int charId) {
    this.charId = charId;
    this.portrait.setCharId(charId);
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.charId != -1) {
      final ActiveStatsa0 stats = stats_800be5f8.get(this.charId);
      this.renderNumber(x + 162, y + 6, stats.level_0e.get(), 2);
      this.renderNumber(x + 120, y + 17, stats.dlevel_0f.get(), 2);
      this.renderNumber(x + 156, y + 17, stats.sp_08.get(), 3);
      renderFraction(x + this.getWidth(), y + 28, stats.hp_04.get(), stats.maxHp_66.get());
      renderFraction(x + this.getWidth(), y + 39, stats.mp_06.get(), stats.maxMp_6e.get());
      renderFraction(x + this.getWidth(), y + 50, gameState_800babc8.charData_32c[this.charId].xp_00, getXpToNextLevel(this.charId));

      if(!renderCharacterStatusEffect(x + 48, y + 3, this.charId)) {
        renderText(characterNames_801142dc.get(this.charId).deref(), x + 56, y + 3, TextColour.BROWN);
      }
    }
  }
}
