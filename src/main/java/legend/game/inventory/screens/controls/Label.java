package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.TextColour;
import legend.game.inventory.screens.TextRenderable;
import legend.game.inventory.screens.TextRenderer;

public class Label extends Control {
  private String text;
  private TextRenderable textRenderable;
  private HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;
  private VerticalAlign verticalAlign = VerticalAlign.TOP;
  private boolean autoSize = true;

  public Label(final String text) {
    this.setText(text);
  }

  public void setHorizontalAlign(final HorizontalAlign horizontalAlign) {
    this.horizontalAlign = horizontalAlign;
  }

  public HorizontalAlign getHorizontalAlign() {
    return this.horizontalAlign;
  }

  public void setVerticalAlign(final VerticalAlign verticalAlign) {
    this.verticalAlign = verticalAlign;
  }

  public VerticalAlign getVerticalAlign() {
    return this.verticalAlign;
  }

  public void setAutoSize(final boolean autoSize) {
    this.autoSize = autoSize;

    if(this.autoSize) {
      this.setSize(this.textRenderable.width, this.textRenderable.height);
    }
  }

  public boolean getAutoSize() {
    return this.autoSize;
  }

  public void setText(final String text) {
    this.text = text;
    this.textRenderable = TextRenderer.prepareShadowText(text, 0, 0, TextColour.BROWN);

    if(this.autoSize) {
      this.setSize(this.textRenderable.width, this.textRenderable.height);
    }
  }

  public String getText() {
    return this.text;
  }

  @Override
  protected void render(final int x, final int y) {
    final int offsetX = switch(this.horizontalAlign) {
      case LEFT -> 0;
      case CENTRE -> (this.getWidth() - this.textRenderable.width) / 2;
      case RIGHT -> this.getWidth() - this.textRenderable.width;
    };

    final int offsetY = switch(this.verticalAlign) {
      case TOP -> 0;
      case CENTRE -> (this.getHeight() - this.textRenderable.height) / 2;
      case BOTTOM -> this.getHeight() - this.textRenderable.height;
    };

    this.textRenderable.render(x + offsetX, y + offsetY, this.getZ() - 1);
  }

  public enum HorizontalAlign {
    LEFT, CENTRE, RIGHT,
  }

  public enum VerticalAlign {
    TOP, CENTRE, BOTTOM,
  }
}
