package legend.game.input;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import legend.core.opengl.Window;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.input.InputPressedEvent;
import legend.game.modding.events.input.InputReleasedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_800b.analogAngle_800bee9c;
import static legend.game.Scus94491BpeSegment_800b.analogInput_800beebc;
import static legend.game.Scus94491BpeSegment_800b.analogMagnitude_800beeb4;
import static legend.game.Scus94491BpeSegment_800b.input_800bee90;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.repeat_800bee98;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LEFT_X;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y;

public final class Input {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Input.class);

  public static final ControllerManager controllerManager = new GlfwControllerManager(Input::onControllerConnected, Input::onControllerDisconnected);
  private static Controller activeController;

  private static final Object2BooleanMap<InputBinding> held = new Object2BooleanOpenHashMap<>();
  private static final Object2BooleanMap<InputBinding> pressedThisFrame = new Object2BooleanOpenHashMap<>();

  private Input() { }

  public static void update() {
    if(!MODS.isReady(CoreMod.MOD_ID)) {
      return;
    }

    if(!RENDERER.window().hasFocus() && !CONFIG.getConfig(CoreMod.RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG.get()) || activeController == null) {
      return;
    }

    controllerManager.update();
    activeController.poll();

    for(int i = 0; i < activeController.bindings.size(); i++) {
      final InputBinding binding = activeController.bindings.get(i);

      if(binding.getState().pressed) {
        if(!held.containsKey(binding)) {
          pressedThisFrame.put(binding, true);
          held.put(binding, true);
          EVENTS.postEvent(new InputPressedEvent(binding.getInputAction()));
        }
      } else if(held.containsKey(binding)) {
        held.removeBoolean(binding);
        EVENTS.postEvent(new InputReleasedEvent(binding.getInputAction()));
      }
    }

    RENDERER.events().callInputEvents(activeController);
  }

  public static void updateLegacyInput() {
    input_800bee90 = 0;
    analogAngle_800bee9c = 0;
    analogMagnitude_800beeb4 = 0;

    if(!MODS.isReady(CoreMod.MOD_ID)) {
      return;
    }

    if(!RENDERER.window().hasFocus() && !CONFIG.getConfig(CoreMod.RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG.get()) || activeController == null) {
      return;
    }

    for(final var entry : held.object2BooleanEntrySet()) {
      if(entry.getBooleanValue()) {
        final InputAction inputAction = entry.getKey().getInputAction();

        if(inputAction.hexCode != -1) {
          input_800bee90 |= inputAction.hexCode;
        }

        if(inputAction == InputAction.JOYSTICK_LEFT_X || inputAction == InputAction.JOYSTICK_LEFT_Y) {
          final float x = activeController.readAxis(GLFW_GAMEPAD_AXIS_LEFT_X);
          final float y = activeController.readAxis(GLFW_GAMEPAD_AXIS_LEFT_Y);

          final float deadzone = CONFIG.getConfig(CoreMod.CONTROLLER_DEADZONE_CONFIG.get());

          // Map discrete axis values (-1.0..1.0) to angle and magnitude that LOD expects
          final int angle = Math.floorMod((int)(Math.atan2(y, x) * 0x800 / Math.PI) + 0x400, 0x1000); // 0..0x1000, clockwise, up=0
          final int mag = (int)((Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) - deadzone) / (1.0f - deadzone) * 0xff); // 0..0xff

          analogInput_800beebc = 1;
          analogAngle_800bee9c = angle;
          analogMagnitude_800beeb4 = mag;
        }
      }
    }

    for(final var entry : pressedThisFrame.object2BooleanEntrySet()) {
      final int hexCode = entry.getKey().getInputAction().hexCode;

      if(hexCode != -1) {
        if(entry.getBooleanValue()) {
          input_800bee90 |= hexCode;
          press_800bee94 |= hexCode;
          repeat_800bee98 |= hexCode;
        } else {
          input_800bee90 &= ~hexCode;
          press_800bee94 &= ~hexCode;
          repeat_800bee98 &= ~hexCode;
        }
      }
    }
  }

  public static void clearLegacyInput() {
    pressedThisFrame.clear();

    press_800bee94 = 0;
    repeat_800bee98 = 0;
  }

  public static void init() {
    RENDERER.events().onKeyPress(Input::keyPress);
    RENDERER.events().onKeyRelease(Input::keyRelease);
    RENDERER.events().onLostFocus(Input::lostFocus);

    useController(null);

    controllerManager.init();
  }

  public static void destroy() {
    controllerManager.destroy();
  }

  public static boolean pressedThisFrame(final InputAction targetKey) {
    for(final var entry : pressedThisFrame.object2BooleanEntrySet()) {
      if(entry.getKey().getInputAction() == targetKey) {
        return entry.getBooleanValue();
      }
    }

    return false;
  }

  public static boolean pressedWithRepeatPulse(final InputAction targetKey) {
    for(final InputBinding inputBinding : activeController.bindings) {
      if(inputBinding.getInputAction() == targetKey) {
        if(inputBinding.getState() == InputState.PRESSED_THIS_FRAME || inputBinding.getState() == InputState.PRESSED_REPEAT) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean getButtonState(final InputAction targetInput) {
    for(final InputBinding inputBinding : activeController.bindings) {
      if(inputBinding.getInputAction() == targetInput) {
        if(inputBinding.getState().pressed) {
          return true;
        }
      }
    }

    return false;
  }

  private static void keyPress(final Window window, final int key, final int scancode, final int mods) {
    final int keyWithMods = key | mods << 9;

    for(final InputBinding inputBinding : activeController.bindings) {
      if(inputBinding.getInputType() == InputType.KEYBOARD && CONFIG.getConfig(CoreMod.KEYBIND_CONFIGS.get(inputBinding.getInputAction()).get()).contains(keyWithMods)) {
        inputBinding.setPressedForKeyboardInput();
      }
    }
  }

  private static void keyRelease(final Window window, final int key, final int scancode, final int mods) {
    final int keyWithMods = key | mods << 9;

    for(final InputBinding inputBinding : activeController.bindings) {
      if(inputBinding.getInputType() == InputType.KEYBOARD && CONFIG.getConfig(CoreMod.KEYBIND_CONFIGS.get(inputBinding.getInputAction()).get()).contains(keyWithMods)) {
        inputBinding.setReleasedForKeyboardInput();
      }
    }
  }

  private static void lostFocus(final Window window) {
    for(final InputBinding inputBinding : activeController.bindings) {
      inputBinding.release();
    }
  }

  public static void useController(@Nullable final Controller controller) {
    if(activeController != null) {
      for(final InputBinding binding : activeController.bindings) {
        held.removeBoolean(binding);
        pressedThisFrame.removeBoolean(binding);
      }
    }

    if(controller != null) {
      activeController = controller;
    } else {
      activeController = new DummyController();
      addKeyboardBindings(activeController);
    }
  }

  public static Controller getController() {
    return activeController;
  }

  public static void rumble(final float bigIntensity, final float smallIntensity, final int ms) {
    activeController.rumble(bigIntensity, smallIntensity, ms);
  }

  public static void rumble(final float intensity, final int ms) {
    activeController.rumble(intensity, ms);
  }

  public static void adjustRumble(final float bigIntensity, final float smallIntensity, final int ms) {
    activeController.adjustRumble(bigIntensity, smallIntensity, ms);
  }

  public static void adjustRumble(final float intensity, final int ms) {
    activeController.adjustRumble(intensity, ms);
  }

  public static void stopRumble() {
    activeController.stopRumble();
  }

  private static void onControllerConnected(final Controller controller) {
    LOGGER.info("Controller %s (%s) connected", controller.getName(), controller.getGuid());

    addKeyboardBindings(controller);

    final String controllerFromConfig = CONFIG.getConfig(CoreMod.CONTROLLER_CONFIG.get());

    if(controllerFromConfig.isBlank() || controllerFromConfig.equals(controller.getGuid())) {
      useController(controller);
      CONFIG.setConfig(CoreMod.CONTROLLER_CONFIG.get(), controller.getGuid());

      if(CONFIG.getConfig(CoreMod.DISABLE_MOUSE_INPUT_CONFIG.get())) {
        RENDERER.window().hideCursor();
      }
    }
  }

  private static void onControllerDisconnected(final Controller controller) {
    LOGGER.info("Controller %s (%s) disconnected", controller.getName(), controller.getGuid());

    if(activeController == controller) {
      useController(null);
      RENDERER.window().showCursor();
    }
  }

  private static void addKeyboardBindings(final Controller controller) {
    controller.addBinding(new InputBinding(InputAction.BUTTON_CENTER_1, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_THUMB_1, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_THUMB_2, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_CENTER_2, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.DPAD_UP, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.DPAD_RIGHT, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.DPAD_DOWN, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.DPAD_LEFT, controller, InputType.KEYBOARD));

    controller.addBinding(new InputBinding(InputAction.BUTTON_SHOULDER_LEFT_2, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_SHOULDER_RIGHT_2, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_SHOULDER_LEFT_1, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_SHOULDER_RIGHT_1, controller, InputType.KEYBOARD));

    controller.addBinding(new InputBinding(InputAction.BUTTON_NORTH, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_EAST, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_SOUTH, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BUTTON_WEST, controller, InputType.KEYBOARD));

    controller.addBinding(new InputBinding(InputAction.BATTLE_DRAGOON, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BATTLE_SPECIAL, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BATTLE_ESCAPE, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BATTLE_GUARD, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.BATTLE_ITEMS, controller, InputType.KEYBOARD));

    controller.addBinding(new InputBinding(InputAction.SPEED_UP, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.SLOW_DOWN, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.DEBUGGER, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.PAUSE, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.FRAME_ADVANCE, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.FRAME_ADVANCE_HOLD, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.KILL_STUCK_SOUNDS, controller, InputType.KEYBOARD));
    controller.addBinding(new InputBinding(InputAction.TOGGLE_FULL_SCREEN, controller, InputType.KEYBOARD));
  }
}
