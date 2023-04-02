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

  public CharacterPortrait() {
    this.setSize(48, 48);

    this.renderable = allocateManualRenderable(uiFile_800bdc3c.portraits_cfac(), null);
    initGlyph(this.renderable, glyph_801142d4);
    this.renderable.glyph_04 = -1;
    this.renderable.tpage_2c++;
    this.renderable.x_40 = 8;
    this.renderable.y_44 = 0;
    this.renderable.z_3c = 33;
  }

  public void setCharId(final int id) {
    this.renderable.glyph_04 = id;
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.renderable.glyph_04 != -1) {
      uploadRenderable(this.renderable, x, y);
    }
  }
}
