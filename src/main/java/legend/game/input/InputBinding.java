package legend.game.input;

import legend.core.Config;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
public class InputBinding {
  private final InputAction inputAction; // Identification + Action
  private int hexCode; // Action Retail
  private int glfwKeyCode; // Actual hardware we need to check
  private boolean keyPressed;
  private int hatIndex;
  private InputBindingState bindingState;
  private InputType inputType;
  private final float controllerDeadzone;
  private Controller targetController;
  private final float[] pulseTimings = {0.5f, 0.1f};
  private int pulseTimingsIndex;
  private double lastPressedTriggerTime;

  public InputBinding(final InputAction inputAction) {
    this.inputAction = inputAction;
    this.hexCode = -1;
    this.glfwKeyCode = -1;
    this.inputType = InputType.GAMEPAD_BUTTON;

    this.bindingState = InputBindingState.NO_INPUT;
    this.controllerDeadzone = Config.controllerDeadzone();
  }

  public InputBinding(final InputAction inputAction, final int glfwKeyCode, final int hexCode, final InputType inputType) {
    this.inputAction = inputAction;
    this.hexCode = hexCode;
    this.glfwKeyCode = glfwKeyCode;
    this.inputType = inputType;

    this.bindingState = InputBindingState.NO_INPUT;
    this.controllerDeadzone = Config.controllerDeadzone();
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
    if(this.bindingState.pressed) {
      this.handleNoInputState();
    }
  }

  private void pollGamepadButtons() {
    if(this.targetController.readButton(this.glfwKeyCode)) {
      this.handlePositiveState();
    } else {
      this.handleNoInputState();
    }
  }

  private void pollGamepadAxis() {
    final float axisValue = this.targetController.readAxis(this.glfwKeyCode);

    if(this.inputType == InputType.GAMEPAD_AXIS_BUTTON_POSITIVE) {
      if(axisValue > this.controllerDeadzone) {
        this.handlePositiveState();
      } else {
        this.handleNoInputState();
      }
    } else if(this.inputType == InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE) {
      if(axisValue < -this.controllerDeadzone) {
        this.handlePositiveState();
      } else {
        this.handleNoInputState();
      }
    }
  }

  private void pollGamepadHats() {
    if(this.targetController.readHat(this.glfwKeyCode, this.hatIndex)) {
      this.handlePositiveState();
    } else {
      this.handleNoInputState();
    }
  }

  private void handlePositiveState() {
    if(!this.bindingState.pressed) {
      this.bindingState = InputBindingState.PRESSED_THIS_FRAME;
      this.lastPressedTriggerTime = glfwGetTime();
      this.pulseTimingsIndex = 0;
    } else if(this.bindingState == InputBindingState.PRESSED_THIS_FRAME || this.bindingState == InputBindingState.PRESSED_REPEAT) {
      this.bindingState = InputBindingState.PRESSED;
    } else if(this.bindingState == InputBindingState.PRESSED) {
      final double targetTimeToBeat = this.lastPressedTriggerTime + this.pulseTimings[this.pulseTimingsIndex];
      if(glfwGetTime() >= targetTimeToBeat) {
        this.lastPressedTriggerTime = glfwGetTime();
        this.bindingState = InputBindingState.PRESSED_REPEAT;
        if(this.pulseTimingsIndex + 1 < this.pulseTimings.length) {
          this.pulseTimingsIndex++;
        }
      }
    }
  }

  private void handleNoInputState() {
    if(this.bindingState.pressed) {
      this.bindingState = InputBindingState.RELEASED_THIS_FRAME;
    } else if(this.bindingState == InputBindingState.RELEASED_THIS_FRAME) {
      this.bindingState = InputBindingState.NO_INPUT;
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

  public InputAction getInputAction() { return this.inputAction; }
  public int getHexCode() { return this.hexCode; }
  public InputBindingState getState() { return this.bindingState; }
  public InputType getInputType() { return this.inputType; }
  public int getGlfwKeyCode() { return this.glfwKeyCode; }
  public Controller getTargetController() { return this.targetController; }
  public void setTargetController(final Controller targetController) { this.targetController = targetController; }
  public void setHexCode(final int hexCode) { this.hexCode = hexCode; }

  public void setGlfwKeyCode(final int glfwKeyCode) { this.glfwKeyCode = glfwKeyCode;  }

  public void setInputType(final InputType inputType) { this.inputType = inputType;  }

  public void setHatIndex(final int hatIndex) { this.hatIndex = hatIndex; }
}
