package legend.game.inventory.screens;

public class FontOptions {
  private HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;
  private float size = 1.0f;
  private int trim;
  private float red;
  private float green;
  private float blue;
  private boolean shadow;
  private float shadowRed;
  private float shadowGreen;
  private float shadowBlue;

  public FontOptions() {
    this
      .colour(TextColour.BROWN)
    ;
  }

  public FontOptions set(final FontOptions other) {
    this.horizontalAlign = other.horizontalAlign;
    this.trim = other.trim;
    this.red = other.red;
    this.green = other.green;
    this.blue = other.blue;
    this.shadow = other.shadow;
    this.shadowRed = other.shadowRed;
    this.shadowGreen = other.shadowGreen;
    this.shadowBlue = other.shadowBlue;
    return this;
  }

  public FontOptions horizontalAlign(final HorizontalAlign align) {
    this.horizontalAlign = align;
    return this;
  }

  public HorizontalAlign getHorizontalAlign() {
    return this.horizontalAlign;
  }

  public FontOptions size(final float size) {
    this.size = size;
    return this;
  }

  public float getSize() {
    return this.size;
  }

  /**
   * @param trim Negative trims top, positive trims bottom
   */
  public FontOptions trim(final int trim) {
    this.trim = trim;
    return this;
  }

  public int getTrim() {
    return this.trim;
  }

  public FontOptions colour(final float red, final float green, final float blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
    return this;
  }

  public FontOptions colour(final TextColour colour) {
    return this.colour(colour.r / 255.0f, colour.g / 255.0f, colour.b / 255.0f);
  }

  public float getRed() {
    return this.red;
  }

  public float getGreen() {
    return this.green;
  }

  public float getBlue() {
    return this.blue;
  }

  public FontOptions noShadow() {
    this.shadow = false;
    return this;
  }

  public FontOptions shadowColour(final float red, final float green, final float blue) {
    this.shadow = true;
    this.shadowRed = red;
    this.shadowGreen = green;
    this.shadowBlue = blue;
    return this;
  }

  public FontOptions shadowColour(final TextColour colour) {
    return this.shadowColour(colour.r / 255.0f, colour.g / 255.0f, colour.b / 255.0f);
  }

  public boolean hasShadow() {
    return this.shadow;
  }

  public float getShadowRed() {
    return this.shadowRed;
  }

  public float getShadowGreen() {
    return this.shadowGreen;
  }

  public float getShadowBlue() {
    return this.shadowBlue;
  }
}
