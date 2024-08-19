package legend.game.types;

import legend.game.inventory.screens.TextColour;

public class RenderTextProperties {
  private final String text;
  private final float x;
  private final float y;
  private final TextColour colour;
  private final int trim;
  private final float scaleX;
  private final float scaleY;
  private final float scaleZ;
  private final float scaleTextSpacing;

  public RenderTextProperties(final String text, final float x, final float y, final TextColour colour, final int trim, final float scaleX, final float scaleY, final float scaleZ, final float scaleTextSpacing) {
    this.text = text;
    this.x = x;
    this.y = y;
    this.colour = colour;
    this.trim = trim;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.scaleZ = scaleZ;
    this.scaleTextSpacing = scaleTextSpacing;
  }

  public String getText() {
    return text;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public TextColour getColour() {
    return colour;
  }

  public int getTrim() {
    return trim;
  }

  public float getScaleX() {
    return this.scaleX;
  }

  public float getScaleY() {
    return this.scaleY;
  }

  public float getScaleZ() {
    return this.scaleZ;
  }

  public float getScaleTextSpacing() {
    return this.scaleTextSpacing;
  }
}
