package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.types.MenuGlyph06;
import legend.game.types.Renderable58;

import static legend.game.Menus.allocateManualRenderable;
import static legend.game.Menus.uploadRenderable;
import static legend.game.SItem.initGlyph;

public class DragoonSpirits extends Control {
  private static final int[] dragoonSpiritGoodsBits_800fbabc = {0, 2, 5, 6, 4, 1, 3, 7};

  private final Renderable58[] renderables = new Renderable58[8];

  public DragoonSpirits(final int spirits) {
    this.setSpirits(spirits);
  }

  public void setSpirits(final int spirits) {
    for(int spiritIndex = 0; spiritIndex < 8; spiritIndex++) {
      final int bit = dragoonSpiritGoodsBits_800fbabc[spiritIndex];
      if((spirits & 0x1 << bit) != 0) {
        final Renderable58 renderable = allocateManualRenderable();
        final MenuGlyph06 glyph = new MenuGlyph06(13 + spiritIndex, spiritIndex * 12, 0);
        initGlyph(renderable, glyph);
        renderable.z_3c = 33;
        this.renderables[spiritIndex] = renderable;
      } else {
        this.renderables[spiritIndex] = null;
      }
    }
  }

  @Override
  protected void render(final int x, final int y) {
    for(final Renderable58 renderable : this.renderables) {
      if(renderable != null) {
        uploadRenderable(renderable, x, y);
      }
    }
  }
}
