package legend.core.platform.input;

public enum InputAxisDirection {
  POSITIVE,
  NEGATIVE,
  ;

  public static InputAxisDirection getDirection(final int val) {
    return values()[val >>> 31];
  }
}
