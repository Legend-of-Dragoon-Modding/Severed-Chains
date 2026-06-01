package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.types.MenuGlyph06;
import legend.game.types.Renderable58;

import static legend.game.Menus.allocateManualRenderable;
import static legend.game.Menus.uploadRenderable;
import static legend.game.SItem.initGlyph;

public class Background extends Control {
  private final Renderable58 left;
  private final Renderable58 right;

  public Background() {
    this.setSize(371, 240);
    this.ignoreInput();

    this.left = allocateManualRenderable();
    this.right = allocateManualRenderable();
    initGlyph(this.left, new MenuGlyph06(0x45, 0, 0));
    initGlyph(this.right, new MenuGlyph06(0x46, 188, 0));

    this.left.z_3c = 100;
    this.right.z_3c = 100;
  }

  @Override
  protected void render(final int x, final int y) {
    uploadRenderable(this.left, x, y);
    uploadRenderable(this.right, x, y);
  }
}
