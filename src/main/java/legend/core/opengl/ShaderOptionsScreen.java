package legend.core.opengl;

public class ShaderOptionsScreen implements ShaderOptions<ShaderOptionsScreen> {
  private final Shader<ShaderOptionsScreen>.UniformInt enableCrt;
  private final Shader<ShaderOptionsScreen>.UniformFloat time;
  private final Shader<ShaderOptionsScreen>.UniformFloat scanlinesOpacity;
  private final Shader<ShaderOptionsScreen>.UniformFloat scanlinesWidth;
  private final Shader<ShaderOptionsScreen>.UniformFloat grilleOpacity;
  private final Shader<ShaderOptionsScreen>.UniformVec2 resolution;
  private final Shader<ShaderOptionsScreen>.UniformInt pixelate;
  private final Shader<ShaderOptionsScreen>.UniformInt roll;
  private final Shader<ShaderOptionsScreen>.UniformFloat rollSpeed;
  private final Shader<ShaderOptionsScreen>.UniformFloat rollSize;
  private final Shader<ShaderOptionsScreen>.UniformFloat rollVariation;
  private final Shader<ShaderOptionsScreen>.UniformFloat distortIntensity;
  private final Shader<ShaderOptionsScreen>.UniformFloat noiseOpacity;
  private final Shader<ShaderOptionsScreen>.UniformFloat noiseSpeed;
  private final Shader<ShaderOptionsScreen>.UniformFloat staticIntensity;
  private final Shader<ShaderOptionsScreen>.UniformFloat aberration;
  private final Shader<ShaderOptionsScreen>.UniformFloat brightness;
  private final Shader<ShaderOptionsScreen>.UniformInt discolour;
  private final Shader<ShaderOptionsScreen>.UniformFloat warpAmount;
  private final Shader<ShaderOptionsScreen>.UniformFloat vignetteIntensity;
  private final Shader<ShaderOptionsScreen>.UniformFloat vignetteOpacity;

  public ShaderOptionsScreen(final Shader<ShaderOptionsScreen>.UniformInt enableCrt, final Shader<ShaderOptionsScreen>.UniformFloat time, final Shader<ShaderOptionsScreen>.UniformFloat scanlinesOpacity, final Shader<ShaderOptionsScreen>.UniformFloat scanlinesWidth, final Shader<ShaderOptionsScreen>.UniformFloat grilleOpacity, final Shader<ShaderOptionsScreen>.UniformVec2 resolution, final Shader<ShaderOptionsScreen>.UniformInt pixelate, final Shader<ShaderOptionsScreen>.UniformInt roll, final Shader<ShaderOptionsScreen>.UniformFloat rollSpeed, final Shader<ShaderOptionsScreen>.UniformFloat rollSize, final Shader<ShaderOptionsScreen>.UniformFloat rollVariation, final Shader<ShaderOptionsScreen>.UniformFloat distortIntensity, final Shader<ShaderOptionsScreen>.UniformFloat noiseOpacity, final Shader<ShaderOptionsScreen>.UniformFloat noiseSpeed, final Shader<ShaderOptionsScreen>.UniformFloat staticIntensity, final Shader<ShaderOptionsScreen>.UniformFloat aberration, final Shader<ShaderOptionsScreen>.UniformFloat brightness, final Shader<ShaderOptionsScreen>.UniformInt discolour, final Shader<ShaderOptionsScreen>.UniformFloat warpAmount, final Shader<ShaderOptionsScreen>.UniformFloat vignetteIntensity, final Shader<ShaderOptionsScreen>.UniformFloat vignetteOpacity) {
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

  @Override
  public void apply() {

  }
}
