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

  public RenderTextProperties(final String text, final float x, final float y, final TextColour colour, final int trim, float scaleX, float scaleY, float scaleZ) {
    this.text = text;
    this.x = x;
    this.y = y;
    this.colour = colour;
    this.trim = trim;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    this.scaleZ = scaleZ;
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
}
