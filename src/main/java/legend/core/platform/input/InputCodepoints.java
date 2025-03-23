package legend.core.platform.input;

import java.util.List;

import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.RENDERER;

/** Uses the first Unicode private area */
public final class InputCodepoints {
  private InputCodepoints() { }

  public static final char DPAD_UP = 0xe000;
  public static final char DPAD_DOWN = 0xe001;
  public static final char DPAD_LEFT = 0xe002;
  public static final char DPAD_RIGHT = 0xe003;
  public static final char SELECT = 0xe004;
  public static final char START = 0xe005;
  public static final char LEFT_BUMPER = 0xe006;
  public static final char LEFT_TRIGGER = 0xe007;
  public static final char LEFT_STICK = 0xe008;
  public static final char RIGHT_BUMPER = 0xe009;
  public static final char RIGHT_TRIGGER = 0xe00a;
  public static final char RIGHT_STICK = 0xe00b;
  public static final char A = 0xe00c;
  public static final char B = 0xe00d;
  public static final char X = 0xe00e;
  public static final char Y = 0xe00f;
  public static final char LEFT_AXIS_X = 0xe010;
  public static final char LEFT_AXIS_Y = 0xe011;
  public static final char RIGHT_AXIS_X = 0xe012;
  public static final char RIGHT_AXIS_Y = 0xe013;

  public static final char XBOX_BUTTON_BACK = 0xe100;
  /** Three lines */
  public static final char XBOX_BUTTON_MENU = 0xe101;
  /** Two squares */
  public static final char XBOX_BUTTON_VIEW = 0xe102;

  public static final char PS_BUTTON_CROSS = 0xe200;
  public static final char PS_BUTTON_CIRCLE = 0xe201;
  public static final char PS_BUTTON_SQUARE = 0xe202;
  public static final char PS_BUTTON_TRIANGLE = 0xe203;

  /** Adjusts codepoints for the given controller (e.g. converts A->cross for PS controllers) */
  public static char getCodepoint(final InputGamepadType type, final InputButton button) {
    return getCodepoint(type, button.codepoint);
  }

  /** Adjusts codepoints for the given controller (e.g. converts A->cross for PS controllers) */
  public static char getCodepoint(final InputGamepadType type, final char codepoint) {
    switch(type) {
      case XBOX_360 -> {
        if(codepoint == SELECT) {
          return XBOX_BUTTON_BACK;
        }
      }

      case XBOX_ONE -> {
        switch(codepoint) {
          case START -> {
            return XBOX_BUTTON_MENU;
          }

          case SELECT -> {
            return XBOX_BUTTON_VIEW;
          }
        }
      }

      case PLAYSTATION -> {
        switch(codepoint) {
          case A -> {
            return PS_BUTTON_CROSS;
          }

          case B -> {
            return PS_BUTTON_CIRCLE;
          }

          case X -> {
            return PS_BUTTON_SQUARE;
          }

          case Y -> {
            return PS_BUTTON_TRIANGLE;
          }
        }
      }
    }

    return codepoint;
  }

  public static String getActionName(final InputAction action) {
    final InputClass type = RENDERER.window().getInputClass();
    final List<InputActivation> activations = InputBindings.getActivationsForAction(action);

    for(int i = 0; i < activations.size(); i++) {
      final InputActivation activation = activations.get(i);

      if(type == InputClass.GAMEPAD) {
        if(activation instanceof final ButtonInputActivation button) {
          return String.valueOf(button.button.codepoint);
        }

        if(activation instanceof final AxisInputActivation axis) {
          return String.valueOf(axis.axis.codepoint);
        }
      }

      if(type == InputClass.KEYBOARD) {
        if(activation instanceof final KeyInputActivation key) {
          return PLATFORM.getKeyName(key.key);
        }

        if(activation instanceof final ScancodeInputActivation scancode) {
          return PLATFORM.getKeyName(scancode.key);
        }
      }
    }

    return "<unbound>";
  }
}
