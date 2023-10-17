package legend.game.types;

public enum TextboxType {
  NO_BACKGROUND,
  NORMAL,
  ANIMATE_IN_OUT,
  ;

  public static TextboxType fromInt(final int mode) {
    return values()[mode];
  }
}
