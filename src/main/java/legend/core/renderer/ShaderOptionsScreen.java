package legend.core.renderer;

public class ShaderOptionsScreen implements ShaderOptions {
  private final ShaderUniformInt enableCrt;
  private final ShaderUniformFloat time;
  private final ShaderUniformFloat scanlinesOpacity;
  private final ShaderUniformFloat scanlinesWidth;
  private final ShaderUniformFloat grilleOpacity;
  private final ShaderUniformVec2 resolution;
  private final ShaderUniformInt pixelate;
  private final ShaderUniformInt roll;
  private final ShaderUniformFloat rollSpeed;
  private final ShaderUniformFloat rollSize;
  private final ShaderUniformFloat rollVariation;
  private final ShaderUniformFloat distortIntensity;
  private final ShaderUniformFloat noiseOpacity;
  private final ShaderUniformFloat noiseSpeed;
  private final ShaderUniformFloat staticIntensity;
  private final ShaderUniformFloat aberration;
  private final ShaderUniformFloat brightness;
  private final ShaderUniformInt discolour;
  private final ShaderUniformFloat warpAmount;
  private final ShaderUniformFloat vignetteIntensity;
  private final ShaderUniformFloat vignetteOpacity;
  private final ShaderUniformFloat bloomIntensity;
  private final ShaderUniformFloat bloomThreshold;
  private final ShaderUniformFloat bloomRadius;
  private final ShaderUniformVec4 turnOrderBounds;

  public ShaderOptionsScreen(final ShaderUniformInt enableCrt, final ShaderUniformFloat time, final ShaderUniformFloat scanlinesOpacity, final ShaderUniformFloat scanlinesWidth, final ShaderUniformFloat grilleOpacity, final ShaderUniformVec2 resolution, final ShaderUniformInt pixelate, final ShaderUniformInt roll, final ShaderUniformFloat rollSpeed, final ShaderUniformFloat rollSize, final ShaderUniformFloat rollVariation, final ShaderUniformFloat distortIntensity, final ShaderUniformFloat noiseOpacity, final ShaderUniformFloat noiseSpeed, final ShaderUniformFloat staticIntensity, final ShaderUniformFloat aberration, final ShaderUniformFloat brightness, final ShaderUniformInt discolour, final ShaderUniformFloat warpAmount, final ShaderUniformFloat vignetteIntensity, final ShaderUniformFloat vignetteOpacity, final ShaderUniformFloat bloomIntensity, final ShaderUniformFloat bloomThreshold, final ShaderUniformFloat bloomRadius, final ShaderUniformVec4 turnOrderBounds) {
    this.enableCrt = enableCrt;
    this.time = time;
    this.scanlinesOpacity = scanlinesOpacity;
    this.scanlinesWidth = scanlinesWidth;
    this.grilleOpacity = grilleOpacity;
    this.resolution = resolution;
    this.pixelate = pixelate;
    this.roll = roll;
    this.rollSpeed = rollSpeed;
    this.rollSize = rollSize;
    this.rollVariation = rollVariation;
    this.distortIntensity = distortIntensity;
    this.noiseOpacity = noiseOpacity;
    this.noiseSpeed = noiseSpeed;
    this.staticIntensity = staticIntensity;
    this.aberration = aberration;
    this.brightness = brightness;
    this.discolour = discolour;
    this.warpAmount = warpAmount;
    this.vignetteIntensity = vignetteIntensity;
    this.vignetteOpacity = vignetteOpacity;
    this.bloomIntensity = bloomIntensity;
    this.bloomThreshold = bloomThreshold;
    this.bloomRadius = bloomRadius;
    this.turnOrderBounds = turnOrderBounds;
  }

  public ShaderOptionsScreen enableCrt(final boolean val) {
    this.enableCrt.set(val ? 1 : 0);
    return this;
  }

  public ShaderOptionsScreen time(final float val) {
    this.time.set(val);
    return this;
  }

  public ShaderOptionsScreen scanlinesOpacity(final float val) {
    this.scanlinesOpacity.set(val);
    return this;
  }

  public ShaderOptionsScreen scanlinesWidth(final float val) {
    this.scanlinesWidth.set(val);
    return this;
  }

  public ShaderOptionsScreen grilleOpacity(final float val) {
    this.grilleOpacity.set(val);
    return this;
  }

  public ShaderOptionsScreen resolution(final float width, final float height) {
    this.resolution.set(width, height);
    return this;
  }

  public ShaderOptionsScreen pixelate(final boolean val) {
    this.pixelate.set(val ? 1 : 0);
    return this;
  }

  public ShaderOptionsScreen roll(final boolean val) {
    this.roll.set(val ? 1 : 0);
    return this;
  }

  public ShaderOptionsScreen rollSpeed(final float val) {
    this.rollSpeed.set(val);
    return this;
  }

  public ShaderOptionsScreen rollSize(final float val) {
    this.rollSize.set(val);
    return this;
  }

  public ShaderOptionsScreen rollVariation(final float val) {
    this.rollVariation.set(val);
    return this;
  }

  public ShaderOptionsScreen distortionIntensity(final float val) {
    this.distortIntensity.set(val);
    return this;
  }

  public ShaderOptionsScreen noiseOpacity(final float val) {
    this.noiseOpacity.set(val);
    return this;
  }

  public ShaderOptionsScreen noiseSpeed(final float val) {
    this.noiseSpeed.set(val);
    return this;
  }

  public ShaderOptionsScreen staticIntensity(final float val) {
    this.staticIntensity.set(val);
    return this;
  }

  public ShaderOptionsScreen aberration(final float val) {
    this.aberration.set(val);
    return this;
  }

  public ShaderOptionsScreen brightness(final float val) {
    this.brightness.set(val);
    return this;
  }

  public ShaderOptionsScreen discolour(final boolean val) {
    this.discolour.set(val ? 1 : 0);
    return this;
  }

  public ShaderOptionsScreen warpAmount(final float val) {
    this.warpAmount.set(val);
    return this;
  }

  public ShaderOptionsScreen vignetteIntensity(final float val) {
    this.vignetteIntensity.set(val);
    return this;
  }

  public ShaderOptionsScreen vignetteOpacity(final float val) {
    this.vignetteOpacity.set(val);
    return this;
  }

  public ShaderOptionsScreen bloomIntensity(final float val) {
    this.bloomIntensity.set(val);
    return this;
  }

  public ShaderOptionsScreen bloomThreshold(final float val) {
    this.bloomThreshold.set(val);
    return this;
  }

  public ShaderOptionsScreen bloomRadius(final float val) {
    this.bloomRadius.set(val);
    return this;
  }

  public ShaderOptionsScreen turnOrderBounds(final float minX, final float minY, final float maxX, final float maxY) {
    this.turnOrderBounds.set(minX, minY, maxX, maxY);
    return this;
  }

  @Override
  public void apply() {

  }
}
