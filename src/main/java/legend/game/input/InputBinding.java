package legend.game.input;

import legend.core.Config;

public class InputBinding {
  private final InputAction inputAction; // Identification + Action
  private int hexCode; // Action Retail
  private int glfwKeyCode; // Actual hardware we need to check
  private int hatIndex;
  private InputBindingState bindingState;
  private InputType inputType;
  private float axisValue;
  private final float controllerDeadzone;
  private boolean keyboardInputSetThisFrame;
  private InputControllerData targetController;

  public InputBinding(final InputAction inputAction)
  {
    this.inputAction = inputAction;
    this.hexCode = -1;
    this.glfwKeyCode = -1;
    this.inputType = InputType.GAMEPAD_BUTTON;
    // Maybe Call DB functions here?
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

  public void update() {
    if(this.inputType == InputType.GAMEPAD_BUTTON) {
      //LOGGER.info("About to update gamepad buttons");
      this.updateGamepadButtons();
    }
    if(this.inputType == InputType.GAMEPAD_AXIS || this.inputType == InputType.GAMEPAD_AXIS_BUTTON_POSITIVE || this.inputType == InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE) {
      //LOGGER.info("About to update gamepad axis");
      this.updateGamepadAxis();
    }
    if(this.inputType == InputType.GAMEPAD_HAT) {
      //LOGGER.info("About to update gamepad hats");
      this.updateGamepadHats();
    }
    if(this.inputType == InputType.KEYBOARD) {
      //LOGGER.info("About to update keyboard");
      this.updateKeyboardInput();
    }
  }


  private void updateGamepadButtons() {
    // Are we pressed this frame?
    if(this.targetController.checkButton(this.glfwKeyCode)) {
      this.handlePositiveState();
    } else {
      this.handleNoInputState();
    }
  }

  private void updateGamepadAxis() {
    this.axisValue = this.targetController.checkAxis(this.glfwKeyCode);

    if(this.inputType == InputType.GAMEPAD_AXIS_BUTTON_POSITIVE) {
      if(this.axisValue > this.controllerDeadzone) {
        this.handlePositiveState();
      } else {
        this.handleNoInputState();
      }
    } else if(this.inputType == InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE) {
      if(this.axisValue < -this.controllerDeadzone) {
        this.handlePositiveState();
      } else {
        this.handleNoInputState();
      }
    }
  }

  private void updateGamepadHats() {
    if(this.targetController.checkHat(this.glfwKeyCode,this.hatIndex)) {
      this.handlePositiveState();
    } else {
      this.handleNoInputState();
    }
  }

  private void handlePositiveState() {
    if(this.bindingState == InputBindingState.NO_INPUT || this.bindingState == InputBindingState.RELEASED_THIS_FRAME) {
      this.bindingState = InputBindingState.PRESSED_THIS_FRAME;
      this.axisValue = 1;
    } else if(this.bindingState == InputBindingState.PRESSED_THIS_FRAME) {
      this.bindingState = InputBindingState.PRESSED;
    }
  }

  private void handleNoInputState() {
    if(this.bindingState == InputBindingState.PRESSED || this.bindingState == InputBindingState.PRESSED_THIS_FRAME) {
      this.bindingState = InputBindingState.RELEASED_THIS_FRAME;
      this.axisValue = 0;
    } else if(this.bindingState == InputBindingState.RELEASED_THIS_FRAME) {
      this.bindingState = InputBindingState.NO_INPUT;
    }
  }

  private void updateKeyboardInput() {
    if(this.keyboardInputSetThisFrame == false) {
      if(this.bindingState == InputBindingState.PRESSED_THIS_FRAME) {
        this.bindingState = InputBindingState.PRESSED;
      } else if(this.bindingState == InputBindingState.RELEASED_THIS_FRAME) {
        this.bindingState = InputBindingState.NO_INPUT;
      }
    } else {
      this.keyboardInputSetThisFrame = false;
    }
  }

  public void setPressedForKeyboardInput() {
    this.bindingState = InputBindingState.PRESSED_THIS_FRAME;
    this.axisValue = 1;
    this.keyboardInputSetThisFrame = true;
  }

  public void setReleasedForKeyboardInput() {
    this.bindingState = InputBindingState.RELEASED_THIS_FRAME;
    this.axisValue = 0;
    this.keyboardInputSetThisFrame = true;
  }

  public InputAction getInputAction() {
    return this.inputAction;
  }
  public int getHexCode() {
    return this.hexCode;
  }
  public InputBindingState getState() {
    return this.bindingState;
  }
  public InputType getInputType() {
    return this.inputType;
  }
  public float getAxisValue() { return this.axisValue; }
  public int getGlfwKeyCode() {
    return this.glfwKeyCode;
  }
  public InputControllerData getTargetController() { return this.targetController; }
  public void setTargetController(final InputControllerData targetController) { this.targetController = targetController; }
  public void setHexCode(final int hexCode) { this.hexCode = hexCode; }

  public void setGlfwKeyCode(final int glfwKeyCode) { this.glfwKeyCode = glfwKeyCode;  }

  public void setInputType(final InputType inputType) { this.inputType = inputType;  }

  public void setHatIndex(final int hatIndex) { this.hatIndex = hatIndex; }
}
