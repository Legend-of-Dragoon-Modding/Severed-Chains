package legend.game.input;

import legend.game.modding.coremod.CoreMod;

import static legend.core.GameEngine.CONFIG;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
public class InputBinding {
  private final InputAction inputAction; // Identification + Action
  private int buttonCode; // Actual hardware we need to check
  private boolean keyPressed;
  private int hatIndex;
  private InputState state;
  private InputType inputType;
  private final Controller targetController;
  private final float[] pulseTimings = {0.5f, 0.1f};
  private int pulseTimingsIndex;
  private double lastPressedTriggerTime;

  public InputBinding(final InputAction inputAction, final Controller controller) {
    this.inputAction = inputAction;
    this.targetController = controller;
    this.buttonCode = -1;
    this.inputType = InputType.GAMEPAD_BUTTON;

    this.state = InputState.NO_INPUT;
  }

  public InputBinding(final InputAction inputAction, final Controller controller, final InputType inputType) {
    this.inputAction = inputAction;
    this.targetController = controller;
    this.inputType = inputType;

    this.state = InputState.NO_INPUT;
  }

  public void poll() {
    if(this.inputType == InputType.GAMEPAD_BUTTON) {
      this.pollGamepadButtons();
    }

    if(this.inputType == InputType.GAMEPAD_AXIS || this.inputType == InputType.GAMEPAD_AXIS_BUTTON_POSITIVE || this.inputType == InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE) {
      this.pollGamepadAxis();
    }

    if(this.inputType == InputType.GAMEPAD_HAT) {
      this.pollGamepadHats();
    }

    if(this.inputType == InputType.KEYBOARD) {
      this.pollKeyboardInput();
    }
  }

  public void release() {
    if(this.state.pressed) {
      this.handleNoInputState();
    }
  }

  private void pollGamepadButtons() {
    if(this.targetController.readButton(this.buttonCode)) {
      this.handlePositiveState();
    } else {
      this.handleNoInputState();
    }
  }

  private void pollGamepadAxis() {
    final float axisValue = this.targetController.readAxis(this.buttonCode);

    if(this.inputType == InputType.GAMEPAD_AXIS) {
      if(Math.abs(axisValue) > CONFIG.getConfig(CoreMod.CONTROLLER_DEADZONE_CONFIG.get())) {
        this.state = InputState.PRESSED;
      } else {
        this.state = InputState.NO_INPUT;
      }
    } else if(this.inputType == InputType.GAMEPAD_AXIS_BUTTON_POSITIVE) {
      if(axisValue > CONFIG.getConfig(CoreMod.CONTROLLER_DEADZONE_CONFIG.get())) {
        this.handlePositiveState();
      } else {
        this.handleNoInputState();
      }
    } else if(this.inputType == InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE) {
      if(axisValue < -CONFIG.getConfig(CoreMod.CONTROLLER_DEADZONE_CONFIG.get())) {
        this.handlePositiveState();
      } else {
        this.handleNoInputState();
      }
    }
  }

  private void pollGamepadHats() {
    if(this.targetController.readHat(this.buttonCode, this.hatIndex)) {
      this.handlePositiveState();
    } else {
      this.handleNoInputState();
    }
  }

  private void handlePositiveState() {
    if(!this.state.pressed) {
      this.state = InputState.PRESSED_THIS_FRAME;
      this.lastPressedTriggerTime = glfwGetTime();
      this.pulseTimingsIndex = 0;
    } else if(this.state == InputState.PRESSED_THIS_FRAME || this.state == InputState.PRESSED_REPEAT) {
      this.state = InputState.PRESSED;
    } else if(this.state == InputState.PRESSED) {
      final double targetTimeToBeat = this.lastPressedTriggerTime + this.pulseTimings[this.pulseTimingsIndex];
      if(glfwGetTime() >= targetTimeToBeat) {
        this.lastPressedTriggerTime = glfwGetTime();
        this.state = InputState.PRESSED_REPEAT;
        if(this.pulseTimingsIndex + 1 < this.pulseTimings.length) {
          this.pulseTimingsIndex++;
        }
      }
    }
  }

  private void handleNoInputState() {
    if(this.state.pressed) {
      this.state = InputState.RELEASED_THIS_FRAME;
    } else if(this.state == InputState.RELEASED_THIS_FRAME) {
      this.state = InputState.NO_INPUT;
    }
  }

  private void pollKeyboardInput() {
    if(this.keyPressed) {
      this.handlePositiveState();
    } else {
      this.handleNoInputState();
    }
  }

  public void setPressedForKeyboardInput() {
    this.keyPressed = true;
  }

  public void setReleasedForKeyboardInput() {
    this.keyPressed = false;
  }

  @Override
  public String toString() {
    return "[" + this.inputType + ':' + this.inputAction + ':' + this.state + ']';
  }

  public InputAction getInputAction() { return this.inputAction; }
  public InputState getState() { return this.state; }
  public InputType getInputType() { return this.inputType; }

  public void setButtonCode(final int buttonCode) { this.buttonCode = buttonCode; }

  public void setInputType(final InputType inputType) { this.inputType = inputType;  }

  public void setHatIndex(final int hatIndex) { this.hatIndex = hatIndex; }
}
