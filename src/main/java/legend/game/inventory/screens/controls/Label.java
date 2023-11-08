package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.TextColour;
import legend.game.types.LodString;

import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment_8002.textHeight;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;

public class Label extends Control {
  private String text;
  private LodString lodString;
  private int textWidth;
  private int textHeight;
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
      this.setSize(this.textWidth, this.textHeight);
    }
  }

  public boolean getAutoSize() {
    return this.autoSize;
  }

  public void setText(final String text) {
    this.text = text;
    this.lodString = new LodString(text);
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
    final int offsetX = switch(this.horizontalAlign) {
      case LEFT -> 0;
      case CENTRE -> (this.getWidth() - this.textWidth) / 2;
      case RIGHT -> this.getWidth() - this.textWidth;
    };

    final int offsetY = switch(this.verticalAlign) {
      case TOP -> 0;
      case CENTRE -> (this.getHeight() - this.textHeight) / 2;
      case BOTTOM -> this.getHeight() - this.textHeight;
    };

    final int oldZ = textZ_800bdf00.get();
    textZ_800bdf00.set(this.getZ() - 1);
    renderText(this.lodString, x + offsetX, y + offsetY, TextColour.BROWN);
    textZ_800bdf00.set(oldZ);
  }

  public enum HorizontalAlign {
    LEFT, CENTRE, RIGHT,
  }

  public enum VerticalAlign {
    TOP, CENTRE, BOTTOM,
  }
}
