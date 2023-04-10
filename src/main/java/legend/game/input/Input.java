package legend.game.input;

import legend.core.Config;
import legend.core.opengl.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.GPU;
import static legend.game.Scus94491BpeSegment.keyRepeat;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b._800bee94;
import static legend.game.Scus94491BpeSegment_800b._800bee98;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;

public final class Input {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker INPUT_MARKER = MarkerManager.getMarker("INPUT");

  private static Controller playerOne = new Controller();
  private static final List<Controller> controllers = new ArrayList<>();
  private static final float controllerDeadzone = Config.controllerDeadzone();

  private static final ControllerManager controllerManager = new ControllerManager();

  private Input() {
  }

  public static void poll() {

  }

  public static void update() {
    if(!GPU.window().hasFocus() && !Config.receiveInputOnInactiveWindow()) {
      return;
    }

    if(controllerManager.isAssigningControllers()) {
      controllerManager.lookForInputOnIdleControllers();
      return;
    }

    controllerManager.pollAssignedControllers();

    for(final Controller controller : controllers) {
      controller.poll();
    }

    for(final InputBinding binding : playerOne.bindings) {
      updateLegacyInput(binding);
    }

    InputPlayerUtility.update();

    GPU.window().events.callInputEvents(playerOne);
  }

  private static void updateLegacyInput(final InputBinding binding) {
    final int hexCode = binding.getHexCode();
    if(hexCode == -1) {
      return;
    }

    if(binding.getState() == InputBindingState.PRESSED_THIS_FRAME) {
      _800bee90.or(hexCode);
      _800bee94.or(hexCode);
      _800bee98.or(hexCode);

      keyRepeat.put(hexCode, 0);
    } else if(binding.getState() == InputBindingState.RELEASED_THIS_FRAME) {
      _800bee90.and(~hexCode);
      _800bee94.and(~hexCode);
      _800bee98.and(~hexCode);

      keyRepeat.remove(hexCode);
    }
  }

  public static void init() {
    GPU.window().events.onControllerConnected(Input::onControllerConnected);
    GPU.window().events.onControllerDisconnected(Input::onControllerDisconnected);

    GPU.window().events.onKeyPress((window, key, scancode, mods) -> {
      if(key == GLFW_KEY_F9) {
        reassignControllers();
      }
    });

    GPU.window().events.onKeyPress(Input::keyPress);
    GPU.window().events.onKeyRelease(Input::keyRelease);
    GPU.window().events.onLostFocus(Input::lostFocus);

    controllerManager.init();
  }

  private static void reassignControllers() {
    LOGGER.info(INPUT_MARKER,"--- Reassigning Controllers ----");
    controllerManager.reassignSequence();
  }

  public static boolean pressedThisFrame(final InputAction targetKey) {
    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputAction() == targetKey) {
        if(inputBinding.getState() == InputBindingState.PRESSED_THIS_FRAME) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean pressedWithRepeatPulse(final InputAction targetKey) {
    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputAction() == targetKey) {
        if(inputBinding.getState() == InputBindingState.PRESSED_THIS_FRAME || inputBinding.getState() == InputBindingState.PRESSED_REPEAT) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean getButtonState(final InputAction targetKey) {
    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputAction() == targetKey) {
        if(inputBinding.getState().pressed) {
          return true;
        }

        final InputAxisState axisState = getAxisDirection(inputBinding.getInputAction());
        if(inputBinding.getInputType() == InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE) {
          if(axisState == InputAxisState.AXIS_NEGATIVE) {
            return true;
          }
        }

        if(inputBinding.getInputType() == InputType.GAMEPAD_AXIS_BUTTON_POSITIVE) {
          if(axisState == InputAxisState.AXIS_POSITIVE) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static InputAxisState getAxisDirection(final InputAction targetKey) {
    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputAction() == targetKey) {
        if(inputBinding.getAxisValue() > controllerDeadzone) {
          return InputAxisState.AXIS_POSITIVE;
        } else if(inputBinding.getAxisValue() < -controllerDeadzone) {
          return InputAxisState.AXIS_NEGATIVE;
        }
      }
    }
    return InputAxisState.AXIS_CENTERED;
  }

  public static float getAxisValue(final InputAction targetKey) {
    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputAction() == targetKey) {
        final float axisValue = inputBinding.getAxisValue();
        if(axisValue != 0) {
          return axisValue;
        }
      }
    }
    return 0;
  }

  public static boolean hasActivityThisFrame() {
    return playerOne.hasActivityThisFrame();
  }

  private static void keyPress(final Window window, final int key, final int scancode, final int mods) {
    if(mods != 0) {
      return;
    }

    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputType() == InputType.KEYBOARD && inputBinding.getGlfwKeyCode() == key) {
        inputBinding.setPressedForKeyboardInput();
      }
    }
  }

  private static void keyRelease(final Window window, final int key, final int scancode, final int mods) {
    if(mods != 0) {
      return;
    }

    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputType() == InputType.KEYBOARD && inputBinding.getGlfwKeyCode() == key) {
        inputBinding.setReleasedForKeyboardInput();
      }
    }
  }

  private static void lostFocus(final Window window) {
    for(final InputBinding inputBinding : playerOne.bindings) {
      inputBinding.release();
    }
  }

  public static void refreshControllers() {
    LOGGER.info(INPUT_MARKER, "Refreshing Controllers...");
    controllers.clear();

    for(final HardwareController hardwareController : controllerManager.getAssignedControllers()) {
      final Controller playerController = new Controller();
      playerController.setHardwareController(hardwareController);
      controllers.add(playerController);
    }

    playerOne = controllers.get(0);
    addKeyboardBindings(playerOne);
  }

  private static void addKeyboardBindings(final Controller controller) {
    controller.addBinding(new InputBinding(InputAction.BUTTON_CENTER_1, GLFW_KEY_SPACE, 0x100, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_THUMB_1, GLFW_KEY_Z, 0x200, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_THUMB_2, GLFW_KEY_C, 0x400, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_CENTER_2, GLFW_KEY_ENTER, 0x800, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.DPAD_UP, GLFW_KEY_UP, 0x1000, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.DPAD_RIGHT, GLFW_KEY_RIGHT, 0x2000, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.DPAD_DOWN, GLFW_KEY_DOWN, 0x4000, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.DPAD_LEFT, GLFW_KEY_LEFT, 0x8000, InputType.KEYBOARD));

    controller.addBinding(new InputBinding(InputAction.BUTTON_SHOULDER_LEFT_2, GLFW_KEY_1, 0x01, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_SHOULDER_RIGHT_2, GLFW_KEY_3, 0x02, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_SHOULDER_LEFT_1, GLFW_KEY_Q, 0x04, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_SHOULDER_RIGHT_1, GLFW_KEY_E, 0x08, InputType.KEYBOARD));

    controller.addBinding(new InputBinding(InputAction.BUTTON_NORTH, GLFW_KEY_W, 0x10, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_EAST, GLFW_KEY_D, 0x40, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_SOUTH, GLFW_KEY_S, 0x20, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_WEST, GLFW_KEY_A, 0x80, InputType.KEYBOARD));

    controller.addBinding(new InputBinding(InputAction.BUTTON_EAST, GLFW_KEY_ESCAPE, 0x40, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_SOUTH, GLFW_KEY_ENTER, 0x20, InputType.KEYBOARD));
  }

  private static void onControllerConnected(final Window window, final int id) {
    if(playerOne.getGuid().equals(glfwGetJoystickGUID(id))) {
      LOGGER.info(INPUT_MARKER,"Player 1 has been reconnected");
    }
  }

  private static void onControllerDisconnected(final Window window, final int id) {
    try {
      if(playerOne.getId() == id) {
        LOGGER.info(INPUT_MARKER, "Player 1's controller has been disconnected. Please reconnect the controller, or switch to a different controller using F9.");
      }
    } catch(final NullPointerException exception) {
      LOGGER.error(INPUT_MARKER, "NPE on controller disconnection", exception);
      controllerManager.reassignSequence();
    }
  }
}
