package legend.game.input;

public enum InputState {
  NO_INPUT(false),
  PRESSED_THIS_FRAME(true),
  RELEASED_THIS_FRAME(false),
  PRESSED(true),
  PRESSED_REPEAT(true),
  ;

  public final boolean pressed;

  InputState(final boolean pressed) {
    this.pressed = pressed;
  }
}
