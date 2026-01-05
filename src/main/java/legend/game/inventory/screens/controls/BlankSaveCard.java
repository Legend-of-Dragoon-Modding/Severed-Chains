package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;

public class BlankSaveCard extends Control {
  public BlankSaveCard() {
    this.addControl(Glyph.uiElement(76, 76)).setPos(0, 0);
    this.addControl(Glyph.uiElement(77, 77)).setPos(176, 0);
  }

  @Override
  protected void render(final int x, final int y) {

  }
}
