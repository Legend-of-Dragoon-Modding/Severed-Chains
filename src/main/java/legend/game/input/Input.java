package legend.game.input;

import legend.core.Config;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.GPU;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickHats;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;

public final class Input {
  private static final InputMapping playerOne = new InputMapping();
  private static final List<InputChangedThisFrame> listeners = new ArrayList<>();

  public static void addInputPressedThisFrameListener(final InputChangedThisFrame listener) {
    listeners.add(listener);
  }


  public static void update() {
    //LOGGER.info("Input Update");
    handleControllerInput();
    GPU.window().events.callNewInputEvents(playerOne);
  }

  public static void init() {
    final String controllerGuid = Config.controllerGuid();
    for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
      if(controllerGuid.equals(glfwGetJoystickGUID(i))) {
        System.out.println("Using gamepad " + glfwGetJoystickName(i));
        controllerId = i;
        break;
      }
    }

    GPU.window().events.onControllerConnected((window, id) -> {
      if(controllerGuid.equals(glfwGetJoystickGUID(id))) {
        controllerId = id;
      }
    });

    GPU.window().events.onControllerDisconnected((window, id) -> {
      if(controllerId == id) {
        controllerId = -1;
      }
    });


    addInputPressedThisFrameListener(new TestInput_EventStyle());
  }

  private static final float controllerDeadzone = Config.controllerDeadzone();
  private static int controllerId = -1;
  private static final GlfwState glfwState = new GlfwState();

  public static boolean pressedThisFrame(final InputKeyCode targetKey) {

    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputKeyCode() == targetKey) {
        return inputBinding.getState() == InputBindingStateEnum.PRESSED_THIS_FRAME;
      }
    }
    return false;
  }

  public static boolean releasedThisFrame(final InputKeyCode targetKey) {

    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputKeyCode() == targetKey) {
        return inputBinding.getState() == InputBindingStateEnum.RELEASED_THIS_FRAME;
      }
    }
    return false;
  }

  public static boolean getButtonState(final InputKeyCode targetKey) {

    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputKeyCode() == targetKey) {
        if(inputBinding.getState() == InputBindingStateEnum.PRESSED || inputBinding.getState() == InputBindingStateEnum.PRESSED_THIS_FRAME) {
          return true;
        }

        if(inputBinding.getInputType() == InputTypeEnum.GAMEPAD_AXIS_BUTTON_NEGATIVE) {
          final InputAxisState axisState = getAxisState(inputBinding.getInputKeyCode());
          if(axisState == InputAxisState.AXIS_NEGATIVE) {
            return true;
          }
        }

        if(inputBinding.getInputType() == InputTypeEnum.GAMEPAD_AXIS_BUTTON_POSITIVE) {
          final InputAxisState axisState = getAxisState(inputBinding.getInputKeyCode());
          if(axisState == InputAxisState.AXIS_POSITIVE) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static InputAxisState getAxisState(final InputKeyCode targetKey) {
    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputKeyCode() == targetKey) {
        if(inputBinding.getAxisValue() > controllerDeadzone) {
          return InputAxisState.AXIS_POSITIVE;
        } else if(inputBinding.getAxisValue() < -controllerDeadzone) {
          return InputAxisState.AXIS_NEGATIVE;
        }
      }
    }
    return InputAxisState.AXIS_CENTERED;
  }

  public static float getAxisStateRaw(final InputKeyCode targetKey) {
    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputKeyCode() == targetKey) {
        return inputBinding.getAxisValue();
      }
    }
    return 0;
  }

  public static void handleControllerInput() {
    if(controllerId != -1) {
      final FloatBuffer axis = glfwGetJoystickAxes(controllerId);
      final ByteBuffer hats = glfwGetJoystickHats(controllerId);
      final ByteBuffer buttons = glfwGetJoystickButtons(controllerId);
      glfwState.UpdateState(axis, hats, buttons);

      playerOne.update();
    }
  }
}
