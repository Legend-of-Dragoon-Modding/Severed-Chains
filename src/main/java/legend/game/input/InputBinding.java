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


  /*
  private void updateDpad(ByteBuffer hats)
  {
    int hat = hats.get(0);

    if((hat & GLFW_HAT_UP) != 0)
    {
      dpad = DirectionalPadEnum.DPAD_UP;
    }
    else if((hat & GLFW_HAT_DOWN) != 0)
    {
      dpad = DirectionalPadEnum.DPAD_DOWN;
    }
    else if((hat & GLFW_HAT_LEFT) != 0)
    {
      dpad = DirectionalPadEnum.DPAD_LEFT;
    }
    else if((hat & GLFW_HAT_RIGHT) != 0)
    {
      dpad = DirectionalPadEnum.DPAD_RIGHT;
    }
    else if((hat & GLFW_HAT_LEFT_UP) != 0)
    {
      dpad = DirectionalPadEnum.DPAD_LEFT_UP;
    }
    else if((hat & GLFW_HAT_LEFT_DOWN) != 0)
    {
      dpad = DirectionalPadEnum.DPAD_LEFT_DOWN;
    }
    else if((hat & GLFW_HAT_RIGHT_UP) != 0)
    {
      dpad = DirectionalPadEnum.DPAD_RIGHT_UP;
    }
    else if((hat & GLFW_HAT_RIGHT_DOWN) != 0)
    {
      dpad = DirectionalPadEnum.DPAD_RIGHT_DOWN;
    }
    else
    {
      dpad = DirectionalPadEnum.DPAD_CENTERED_NO_INPUT;
    }

  }
   */
}
