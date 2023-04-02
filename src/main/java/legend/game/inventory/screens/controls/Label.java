package legend.game.inventory.screens.controls;

import legend.game.SItem;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.TextColour;
import legend.game.types.LodString;

public class Label extends Control {
  private LodString text;

  public Label(final LodString text) {
    this.text = text;
  }

  public Label(final String text) {
    this(new LodString(text));
  }

  public void setText(final String text) {
    this.text = new LodString(text);
  }

  public void setText(final LodString text) {
    this.text = text;
  }

  @Override
  protected void render(final int x, final int y) {
    final int offsetY = this.getHeight() == 0 ? 0 : (this.getHeight() - 12) / 2;
    SItem.renderText(this.text, x, y + offsetY, TextColour.BROWN);
  }
}
