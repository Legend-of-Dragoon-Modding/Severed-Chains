package legend.core.platform.input;

public enum InputAxis {
  LEFT_X(InputCodepoints.LEFT_AXIS_X),
  LEFT_Y(InputCodepoints.LEFT_AXIS_Y),
  RIGHT_X(InputCodepoints.RIGHT_AXIS_X),
  RIGHT_Y(InputCodepoints.RIGHT_AXIS_Y),
  LEFT_TRIGGER(InputCodepoints.LEFT_TRIGGER),
  RIGHT_TRIGGER(InputCodepoints.RIGHT_TRIGGER),
  ;

  public final char codepoint;

  InputAxis(final char codepoint) {
    this.codepoint = codepoint;
  }
}
