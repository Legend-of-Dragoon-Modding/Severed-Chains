package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.TextColour;
import legend.game.types.LodString;

import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.renderCentredText;

public class Button extends Control {
  private final Glyph hover;
  private LodString text;
  private TextColour textColour = TextColour.BROWN;

  public Button(final String text) {
    this.setSize(59, 12);

    this.hover = this.addControl(Glyph.uiElement(122, 122));
    FUN_80104b60(this.hover.getRenderable());
    this.hover.getRenderable().x_40 += 8;
    this.hover.getRenderable().y_44 -= 2;
    this.hover.hide();

    this.setText(text);
  }

  public LodString getText() {
    return this.text;
  }

  public void setText(final LodString text) {
    this.text = text;
  }

  public void setText(final String text) {
    this.text = new LodString(text);
  }

  public TextColour getTextColour() {
    return this.textColour;
  }

  public void setTextColour(final TextColour colour) {
    this.textColour = colour;
  }

  @Override
  protected void hoverIn() {
    super.hoverIn();
    this.hover.show();
  }

  @Override
  protected void hoverOut() {
    super.hoverOut();
    this.hover.hide();
  }

  @Override
  protected void render(final int x, final int y) {
    renderCentredText(this.text, x + this.getWidth() / 2, y, this.isDisabled() ? TextColour.MIDDLE_BROWN : this.textColour);
  }
}
