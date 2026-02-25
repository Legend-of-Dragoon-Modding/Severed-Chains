package legend.game.inventory.screens.controls;

import legend.core.GameEngine;
import legend.game.characters.VitalsStat;
import legend.game.inventory.screens.Control;
import legend.game.types.CharacterData2c;

import javax.annotation.Nullable;

import static legend.game.SItem.renderCharacterStatusEffect;
import static legend.game.SItem.renderFraction;
import static legend.game.SItem.renderHp;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MP_STAT;
import static legend.lodmod.LodMod.SP_STAT;

public class CharacterCard extends Control {
  private CharacterData2c character;

  private final Glyph background;
  private final Glyph overlay;
  private final Label name;
  private final AtlasIcon portrait;

  public CharacterCard() {
    this.setSize(174, 64);

    this.overlay = this.addControl(Glyph.uiElement(74, 74));
    this.overlay.getRenderable().x_40 += 8;
    this.overlay.getRenderable().z_3c = 33;

    this.background = this.addControl(Glyph.uiElement(153, 153));
    this.background.getRenderable().x_40 += 8;

    this.name = this.addControl(new Label(""));
    this.name.setPos(57, 3);

    this.portrait = this.addControl(new AtlasIcon());
    this.portrait.setPos(8, 8);
    this.portrait.setSize(48, 48);
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);
    this.background.setZ(z);
    this.overlay.setZ(z);
    this.name.setZ(z);
    this.portrait.setZ(z);
  }

  public CharacterData2c getCharacter() {
    return this.character;
  }

  public void setCharacter(@Nullable final CharacterData2c character) {
    this.character = character;

    final boolean visible = character != null;

    this.portrait.setIcon(visible ? GameEngine.getTextureAtlas().getIcon(character.template.getRegistryId()) : null);

    this.background.setVisibility(visible);
    this.overlay.setVisibility(visible);
    this.name.setVisibility(visible);
    this.portrait.setVisibility(visible);

    if(visible) {
      this.name.setText(this.character.getName());
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.character != null) {
      final VitalsStat hp = this.character.stats.getStat(HP_STAT.get());
      final VitalsStat mp = this.character.stats.getStat(MP_STAT.get());
      final VitalsStat sp = this.character.stats.getStat(SP_STAT.get());
      this.renderNumber(x + 162, y + 6, this.character.level_12, 2);
      this.renderNumber(x + 120, y + 17, this.character.dlevel_13, 2);
      this.renderNumber(x + 156, y + 17, sp.getCurrent(), 3);
      renderHp(x + this.getWidth(), y + 28, hp.getCurrent(), hp.getMax());
      renderFraction(x + this.getWidth(), y + 39, mp.getCurrent(), mp.getMax());
      renderFraction(x + this.getWidth(), y + 50, this.character.xp_00, this.character.getXpToNextLevel());

      this.name.setVisibility(!renderCharacterStatusEffect(x + 54, y + 3, this.character));
    }
  }
}
