package legend.game.types;

public enum BackgroundType {
  NO_BACKGROUND,
  NORMAL,
  ANIMATE_IN_OUT,
  ;

  public static BackgroundType fromInt(final int mode) {
    return values()[mode];
  }
}
