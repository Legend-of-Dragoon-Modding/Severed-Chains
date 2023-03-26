package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.types.Renderable58;

import static legend.game.SItem.glyph_801142d4;
import static legend.game.SItem.initGlyph;
import static legend.game.Scus94491BpeSegment_8002.allocateManualRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderable;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;

public class CharacterPortrait extends Control {
  private final Renderable58 renderable;

  public CharacterPortrait(final int charId) {
    this.setSize(48, 48);

    this.renderable = allocateManualRenderable(uiFile_800bdc3c.portraits_cfac(), null);
    initGlyph(this.renderable, glyph_801142d4);
    this.renderable.glyph_04 = charId;
    this.renderable.tpage_2c++;
    this.renderable.x_40 = 8;
    this.renderable.y_44 = 0;
    this.renderable.z_3c = 33;
  }

  @Override
  protected void render(final int x, final int y) {
    uploadRenderable(this.renderable, x, y);
  }
}
