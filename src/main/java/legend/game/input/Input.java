package legend.game.input;

import legend.core.Config;
import legend.core.opengl.Window;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.GPU;
import static legend.game.unpacker.Unpacker.LOGGER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F9;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;

public final class Input {
  private static InputMapping playerOne = new InputMapping();
  private static final List<InputMapping> controllers = new ArrayList<>();

  public static void update() {

    InputPlayerUtility.update();

    if(InputControllerAssigner.isAssigningControllers()) {
      InputControllerAssigner.update();
      return;
    }

    for(final InputMapping controller : controllers) {
      controller.update();
    }

    GPU.window().events.callInputEvents(playerOne);
  }

  public static void init() {
    LOGGER.info("Input Init Called");
    GPU.window().events.onControllerConnected((window, id) -> onControllerConnected(id));
    GPU.window().events.onControllerDisconnected((window, id) -> onControllerDisconnected(id));

    InputControllerAssigner.init();

    GPU.window().events.onKeyPress((window, key, scancode, mods) -> {
      if(key == GLFW_KEY_F9) {
        reassignControllers();
      }
    });

    GPU.window().events.onKeyPress(Input::keyPress);
    GPU.window().events.onKeyRelease(Input::keyRelease);
  }

  private static void reassignControllers() {
    LOGGER.info("--- Reassigning Controllers ----");
    InputControllerAssigner.reassignSequence();
  }
  private static final float controllerDeadzone = Config.controllerDeadzone();
  public static boolean pressedThisFrame(final InputAction targetKey) {

    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputAction() == targetKey) {
        return inputBinding.getState() == InputBindingState.PRESSED_THIS_FRAME;
      }
    }
    return false;
  }

  public static boolean releasedThisFrame(final InputAction targetKey) {

    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputAction() == targetKey) {
        return inputBinding.getState() == InputBindingState.RELEASED_THIS_FRAME;
      }
    }
    return false;
  }

  public static boolean getButtonState(final InputAction targetKey) {

    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputAction() == targetKey) {
        if(inputBinding.getState() == InputBindingState.PRESSED || inputBinding.getState() == InputBindingState.PRESSED_THIS_FRAME) {
          return true;
        }

        if(inputBinding.getInputType() == InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE) {
          final InputAxisState axisState = getAxisState(inputBinding.getInputAction());
          if(axisState == InputAxisState.AXIS_NEGATIVE) {
            return true;
          }
        }

        if(inputBinding.getInputType() == InputType.GAMEPAD_AXIS_BUTTON_POSITIVE) {
          final InputAxisState axisState = getAxisState(inputBinding.getInputAction());
          if(axisState == InputAxisState.AXIS_POSITIVE) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static InputAxisState getAxisState(final InputAction targetKey) {
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

  public static float getAxisStateRaw(final InputAction targetKey) {
    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputAction() == targetKey) {
        return inputBinding.getAxisValue();
      }
    }
    return 0;
  }

  public static boolean hasActivityThisFrame() {
    return playerOne.hasActivityThisFrame();
  }

  public static boolean hasActivity() {
    return playerOne.hasActivity();
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

  public static void refreshControllers() {
    LOGGER.info("Refreshing Controllers...");
    controllers.clear();
    for(final InputControllerData inputControllerData : InputControllerAssigner.getAssignedControllers()) {
      final InputMapping playerInputMapping = new InputMapping();
      playerInputMapping.setControllerData(inputControllerData);
      controllers.add(playerInputMapping);
    }

    playerOne = controllers.get(0);

  }

  private static void onControllerConnected(final int id) {
    if(playerOne.getControllerData().getGlfwJoystickGUID().equals(glfwGetJoystickGUID(id))) {
      LOGGER.info("Player 1 has been reconnected");
    }

  }

  private static void onControllerDisconnected(final int id) {

    if(playerOne.getControllerData().getGlfwControllerId() == id) {
      // Player one has been disconnected
      LOGGER.info("Player 1 has been disconnected please reconnect or switch to a different controller using F9");
    }


  }

}
