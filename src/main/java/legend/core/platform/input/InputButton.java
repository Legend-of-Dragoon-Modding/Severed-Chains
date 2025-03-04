package legend.core.platform.input;

public enum InputButton {
  DPAD_UP(InputCodepoints.DPAD_UP),
  DPAD_DOWN(InputCodepoints.DPAD_DOWN),
  DPAD_LEFT(InputCodepoints.DPAD_LEFT),
  DPAD_RIGHT(InputCodepoints.DPAD_RIGHT),
  A(InputCodepoints.A),
  B(InputCodepoints.B),
  X(InputCodepoints.X),
  Y(InputCodepoints.Y),
  START(InputCodepoints.START),
  SELECT(InputCodepoints.SELECT),
  LEFT_BUMPER(InputCodepoints.LEFT_BUMPER),
  RIGHT_BUMPER(InputCodepoints.RIGHT_BUMPER),
  LEFT_STICK(InputCodepoints.LEFT_STICK),
  RIGHT_STICK(InputCodepoints.RIGHT_STICK),
  ;

  public final char codepoint;

  InputButton(final char codepoint) {
    this.codepoint = codepoint;
  }
}
