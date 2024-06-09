package legend.game.input;

public enum InputAction {
  DPAD_UP(0x1000, '\u0111'),
  DPAD_DOWN(0x4000, '\u0112'),
  DPAD_LEFT(0x8000, '\u0113'),
  DPAD_RIGHT(0x2000, '\u0114'),

  BUTTON_NORTH(0x10, '\u0120'),
  BUTTON_SOUTH(0x20, '\u011d'),
  BUTTON_EAST(0x40, '\u011e'),
  BUTTON_WEST(0x80, '\u011f'),

  BUTTON_CENTER_1(0x100, '\u0116'),
  BUTTON_CENTER_2(0x800, '\u0115'),
  BUTTON_SHOULDER_LEFT_1(0x4, '\u0117'),
  BUTTON_SHOULDER_RIGHT_1(0x8, '\u011a'),
  BUTTON_SHOULDER_LEFT_2(0x1, '\u0118'),
  BUTTON_SHOULDER_RIGHT_2(0x2, '\u011b'),
  BUTTON_THUMB_1(0x200, '\u0119'),
  BUTTON_THUMB_2(0x400, '\u011c'),

  JOYSTICK_LEFT_X(-1, '\0'),
  JOYSTICK_LEFT_Y(-1, '\0'),
  JOYSTICK_LEFT_BUTTON_UP(0x1000, '\u0111'),
  JOYSTICK_LEFT_BUTTON_DOWN(0x4000, '\u0112'),
  JOYSTICK_LEFT_BUTTON_LEFT(0x8000, '\u0113'),
  JOYSTICK_LEFT_BUTTON_RIGHT(0x2000, '\u0114'),

  JOYSTICK_RIGHT_X(-1, '\0'),
  JOYSTICK_RIGHT_Y(-1, '\0'),
  JOYSTICK_RIGHT_BUTTON_UP(-1, '\0'),
  JOYSTICK_RIGHT_BUTTON_DOWN(-1, '\0'),
  JOYSTICK_RIGHT_BUTTON_LEFT(-1, '\0'),
  JOYSTICK_RIGHT_BUTTON_RIGHT(-1, '\0'),
  ;

  public final int hexCode;
  public final char icon;

  InputAction(final int hexCode, final char icon) {
    this.hexCode = hexCode;
    this.icon = icon;
  }
}
