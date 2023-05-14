package legend.game.input;

import legend.core.Config;
import legend.core.opengl.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;

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
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

public final class Input {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker INPUT_MARKER = MarkerManager.getMarker("INPUT");

  public static final ControllerManager controllerManager = new ControllerManager(Input::onControllerConnected, Input::onControllerDisconnected);
  private static Controller playerOne;

  private Input() {
  }

  public static void update() {
    if(!GPU.window().hasFocus() && !Config.receiveInputOnInactiveWindow() || playerOne == null) {
      return;
    }

    playerOne.poll();

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
    GPU.window().events.onKeyPress(Input::keyPress);
    GPU.window().events.onKeyRelease(Input::keyRelease);
    GPU.window().events.onLostFocus(Input::lostFocus);

    useController(null);

    controllerManager.init();
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

  public static boolean getButtonState(final InputAction targetInput) {
    for(final InputBinding inputBinding : playerOne.bindings) {
      if(inputBinding.getInputAction() == targetInput) {
        if(inputBinding.getState().pressed) {
          return true;
        }
      }
    }

    return false;
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

  public static void useController(@Nullable final Controller controller) {
    if(controller != null) {
      playerOne = controller;
    } else {
      playerOne = new DummyController();
      addKeyboardBindings(playerOne);
    }
  }

  private static void onControllerConnected(final GlfwController controller) {
    addKeyboardBindings(controller);

    if(playerOne.equals(controller)) {
      LOGGER.info(INPUT_MARKER,"Player 1 has been reconnected");
    }
  }

  private static void onControllerDisconnected(final GlfwController controller) {
    if(playerOne.equals(controller)) {
      LOGGER.info(INPUT_MARKER, "Player 1's controller has been disconnected. Please reconnect the controller, or switch to a different controller using F9.");
    }
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
  }}
