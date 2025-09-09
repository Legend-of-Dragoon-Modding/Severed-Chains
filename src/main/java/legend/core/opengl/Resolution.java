package legend.core.opengl;

public enum Resolution {
  ORIGINAL(240),
  _480(480),
  _720(720),
  _1080(1080),
  _1440(1440),
  _4K(2160),
  _8K(4320),
  NATIVE(-1),
  ;

  public final int verticalResolution;

  Resolution(final int verticalResolution) {
    this.verticalResolution = verticalResolution;
  }
}
