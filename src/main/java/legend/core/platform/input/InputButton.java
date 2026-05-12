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
  GUIDE(InputCodepoints.XBOX_BUTTON_GUIDE),
  TOUCHPAD(InputCodepoints.PS_BUTTON_TOUCHPAD),
  LEFT_PADDLE1(InputCodepoints.GENERIC_LEFT_PADDLE1),
  RIGHT_PADDLE1(InputCodepoints.GENERIC_RIGHT_PADDLE1),
  LEFT_PADDLE2(InputCodepoints.GENERIC_LEFT_PADDLE2),
  RIGHT_PADDLE2(InputCodepoints.GENERIC_RIGHT_PADDLE2),
  MISC1(InputCodepoints.GENERIC_MISC1),
  MISC2(InputCodepoints.GENERIC_MISC2),
  MISC3(InputCodepoints.GENERIC_MISC3),
  MISC4(InputCodepoints.GENERIC_MISC4),
  MISC5(InputCodepoints.GENERIC_MISC5),
  MISC6(InputCodepoints.GENERIC_MISC6),
  ;

  public final char codepoint;

  InputButton(final char codepoint) {
    this.codepoint = codepoint;
  }
}
