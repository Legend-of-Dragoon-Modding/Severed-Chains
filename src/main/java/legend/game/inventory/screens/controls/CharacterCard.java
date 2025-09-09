package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.types.ActiveStatsa0;

import static legend.game.SItem.characterNames_801142dc;
import static legend.game.SItem.getXpToNextLevel;
import static legend.game.SItem.renderCharacterStatusEffect;
import static legend.game.SItem.renderFraction;
import static legend.game.SItem.renderHp;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;

public class CharacterCard extends Control {
  private int charId;

  private final Glyph background;
  private final Glyph overlay;
  private final Label name;
  private final CharacterPortrait portrait;

  public CharacterCard(final int charId) {
    this.charId = charId;

    this.setSize(174, 64);

    this.overlay = this.addControl(Glyph.uiElement(74, 74));
    this.overlay.getRenderable().x_40 += 8;
    this.overlay.getRenderable().z_3c = 33;

    this.background = this.addControl(Glyph.uiElement(153, 153));
    this.background.getRenderable().x_40 += 8;

    this.name = this.addControl(new Label(""));
    this.name.setPos(57, 3);

    this.portrait = this.addControl(new CharacterPortrait());
    this.portrait.setPos(8, 8);

    this.setCharId(charId);
  }

  public void setCharId(final int charId) {
    this.charId = charId;

    this.portrait.setCharId(charId);

    final boolean visible = charId != -1;
    this.background.setVisibility(visible);
    this.overlay.setVisibility(visible);
    this.name.setVisibility(visible);
    this.portrait.setVisibility(visible);

    if(visible) {
      this.name.setText(characterNames_801142dc[this.charId]);
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.charId != -1) {
      final ActiveStatsa0 stats = stats_800be5f8[this.charId];
      this.renderNumber(x + 162, y + 6, stats.level_0e, 2);
      this.renderNumber(x + 120, y + 17, stats.dlevel_0f, 2);
      this.renderNumber(x + 156, y + 17, stats.sp_08, 3);
      renderHp(x + this.getWidth(), y + 28, stats.hp_04, stats.maxHp_66);
      renderFraction(x + this.getWidth(), y + 39, stats.mp_06, stats.maxMp_6e);
      renderFraction(x + this.getWidth(), y + 50, gameState_800babc8.charData_32c[this.charId].xp_00, getXpToNextLevel(this.charId));

      this.name.setVisibility(!renderCharacterStatusEffect(x + 54, y + 3, this.charId));
    }
  }
}
