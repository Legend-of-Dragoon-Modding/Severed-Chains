package legend.game.input;

import legend.core.Config;

public class InputBinding {
  private final InputKeyCode inputKeyCode; // Identification + Action
  private final int hexCode; // Action Retail
  private final int glfwKeyCode; // Actual hardware we need to check
  private InputBindingStateEnum bindingState;
  private final InputTypeEnum inputType;
  private float axisValue;

  private final float controllerDeadzone;
  private boolean keyboardInputSetThisFrame;

  public InputBinding(final InputKeyCode inputKeyCode, final int glfwKeyCode, final int hexCode, final InputTypeEnum inputType) {
    this.inputKeyCode = inputKeyCode;
    this.hexCode = hexCode;
    this.glfwKeyCode = glfwKeyCode;
    this.inputType = inputType;
    this.bindingState = InputBindingStateEnum.NO_INPUT;

    this.controllerDeadzone = Config.controllerDeadzone();
  }

  public void update() {
    if(this.inputType == InputTypeEnum.GAMEPAD_BUTTON) {
      this.updateGamepadButtons();
    }
    if(this.inputType == InputTypeEnum.GAMEPAD_AXIS || this.inputType == InputTypeEnum.GAMEPAD_AXIS_BUTTON_POSITIVE || this.inputType == InputTypeEnum.GAMEPAD_AXIS_BUTTON_NEGATIVE) {
      this.updateGamepadAxis();
    }
    if(this.inputType == InputTypeEnum.GAMEPAD_HAT) {
      this.updateGamepadHats();
    }
    if(this.inputType == InputTypeEnum.KEYBOARD) {
      this.updateKeyboardInput();
    }
  }


  private void updateGamepadButtons() {
    // Are we pressed this frame?
    if(GlfwState.checkButton(this.glfwKeyCode)) {
      this.handlePositiveState();
    } else {
      this.handleNoInputState();
    }
  }

  private void updateGamepadAxis() {
    this.axisValue = GlfwState.checkAxis(this.glfwKeyCode);

    if(this.inputType == InputTypeEnum.GAMEPAD_AXIS_BUTTON_POSITIVE) {
      if(this.axisValue > this.controllerDeadzone) {
        this.handlePositiveState();
      } else {
        this.handleNoInputState();
      }
    } else if(this.inputType == InputTypeEnum.GAMEPAD_AXIS_BUTTON_NEGATIVE) {
      if(this.axisValue < -this.controllerDeadzone) {
        this.handlePositiveState();
      } else {
        this.handleNoInputState();
      }
    }
  }

  private void updateGamepadHats() {
    if(GlfwState.checkHat(this.glfwKeyCode)) {
      this.handlePositiveState();
    } else {
      this.handleNoInputState();
    }
  }

  private void handlePositiveState() {
    if(this.bindingState == InputBindingStateEnum.NO_INPUT || this.bindingState == InputBindingStateEnum.RELEASED_THIS_FRAME) {
      this.bindingState = InputBindingStateEnum.PRESSED_THIS_FRAME;

    } else if(this.bindingState == InputBindingStateEnum.PRESSED_THIS_FRAME) {
      this.bindingState = InputBindingStateEnum.PRESSED;
    }
  }

  private void handleNoInputState() {
    if(this.bindingState == InputBindingStateEnum.PRESSED || this.bindingState == InputBindingStateEnum.PRESSED_THIS_FRAME) {
      this.bindingState = InputBindingStateEnum.RELEASED_THIS_FRAME;
    } else if(this.bindingState == InputBindingStateEnum.RELEASED_THIS_FRAME) {
      this.bindingState = InputBindingStateEnum.NO_INPUT;
    }
  }

  private void updateKeyboardInput() {
    if(this.keyboardInputSetThisFrame == false) {
      if(this.bindingState == InputBindingStateEnum.PRESSED_THIS_FRAME) {
        this.bindingState = InputBindingStateEnum.PRESSED;
      } else if(this.bindingState == InputBindingStateEnum.RELEASED_THIS_FRAME) {
        this.bindingState = InputBindingStateEnum.NO_INPUT;
      }
    } else {
      this.keyboardInputSetThisFrame = false;
    }
  }

  public void setPressedForKeyboardInput() {
    this.bindingState = InputBindingStateEnum.PRESSED_THIS_FRAME;
    this.keyboardInputSetThisFrame = true;
  }

  public void setReleasedForKeyboardInput() {
    this.bindingState = InputBindingStateEnum.RELEASED_THIS_FRAME;
    this.keyboardInputSetThisFrame = true;
  }

  public InputKeyCode getInputKeyCode() {
    return this.inputKeyCode;
  }

  public int getHexCode() {
    return this.hexCode;
  }

  public InputBindingStateEnum getState() {
    return this.bindingState;
  }

  public InputTypeEnum getInputType() {
    return this.inputType;
  }

  public float getAxisValue() {
    return this.axisValue;
  }

  public int getGlfwKeyCode() {
    return this.glfwKeyCode;
  }
}
