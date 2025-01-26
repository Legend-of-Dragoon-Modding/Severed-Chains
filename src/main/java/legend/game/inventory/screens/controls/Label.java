package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.TextColour;

import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textHeight;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;

public class Label extends Control {
  private String text;
  private int textWidth;
  private int textHeight;
  private VerticalAlign verticalAlign = VerticalAlign.TOP;
  private boolean autoSize = true;
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);

  public Label(final String text) {
    this.setText(text);
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
      this.setSize(this.textWidth, this.textHeight);
    }
  }

  public boolean getAutoSize() {
    return this.autoSize;
  }

  public FontOptions getFontOptions() {
    return this.fontOptions;
  }

  public void setText(final String text) {
    this.text = text;
    this.textWidth = textWidth(text);
    this.textHeight = textHeight(text);

    if(this.autoSize) {
      this.setSize(this.textWidth, this.textHeight);
    }
  }

  public String getText() {
    return this.text;
  }

  @Override
  protected void render(final int x, final int y) {
    final int offsetX = switch(this.fontOptions.getHorizontalAlign()) {
      case LEFT -> 0;
      case CENTRE -> this.getWidth() / 2;
      case RIGHT -> this.getWidth();
    };

    final int offsetY = switch(this.verticalAlign) {
      case TOP -> 0;
      case CENTRE -> (this.getHeight() - this.textHeight) / 2;
      case BOTTOM -> this.getHeight() - this.textHeight;
    };

    final int oldZ = textZ_800bdf00;
    textZ_800bdf00 = this.getZ() - 1;
    renderText(this.text, x + offsetX, y + offsetY, this.fontOptions);
    textZ_800bdf00 = oldZ;
  }

  public enum VerticalAlign {
    TOP, CENTRE, BOTTOM,
  }
}
