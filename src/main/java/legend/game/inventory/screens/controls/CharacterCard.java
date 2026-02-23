package legend.game.inventory.screens.controls;

import legend.game.characters.VitalsStat;
import legend.game.inventory.screens.Control;
import legend.game.types.CharacterData2c;

import static legend.game.SItem.characterNames_801142dc;
import static legend.game.SItem.getXpToNextLevel;
import static legend.game.SItem.renderCharacterStatusEffect;
import static legend.game.SItem.renderFraction;
import static legend.game.SItem.renderHp;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MP_STAT;
import static legend.lodmod.LodMod.SP_STAT;

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

  public int getCharId() {
    return this.charId;
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
      final CharacterData2c character = gameState_800babc8.charData_32c.get(this.charId);
      final VitalsStat hp = character.stats.getStat(HP_STAT.get());
      final VitalsStat mp = character.stats.getStat(MP_STAT.get());
      final VitalsStat sp = character.stats.getStat(SP_STAT.get());
      this.renderNumber(x + 162, y + 6, character.level_12, 2);
      this.renderNumber(x + 120, y + 17, character.dlevel_13, 2);
      this.renderNumber(x + 156, y + 17, sp.getCurrent(), 3);
      renderHp(x + this.getWidth(), y + 28, hp.getCurrent(), hp.getMax());
      renderFraction(x + this.getWidth(), y + 39, mp.getCurrent(), mp.getMax());
      renderFraction(x + this.getWidth(), y + 50, character.xp_00, getXpToNextLevel(this.charId));

      this.name.setVisibility(!renderCharacterStatusEffect(x + 54, y + 3, this.charId));
    }
  }
}
