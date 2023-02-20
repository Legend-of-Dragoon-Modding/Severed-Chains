package legend.game.input;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LEFT_X;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_A;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_B;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_BACK;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_START;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_X;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_Y;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_UP;

public class InputMapping {
  public List<InputBinding> bindings = new ArrayList<>();
  private boolean anyActivity;

  public InputMapping() {
    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_NORTH, GLFW_GAMEPAD_BUTTON_Y, 0x10, InputTypeEnum.GAMEPAD_BUTTON));
    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_SOUTH, GLFW_GAMEPAD_BUTTON_A, 0x20, InputTypeEnum.GAMEPAD_BUTTON));
    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_EAST, GLFW_GAMEPAD_BUTTON_B, 0x40, InputTypeEnum.GAMEPAD_BUTTON));
    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_WEST, GLFW_GAMEPAD_BUTTON_X, 0x80, InputTypeEnum.GAMEPAD_BUTTON));

    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_CENTER_1, GLFW_GAMEPAD_BUTTON_BACK, 0x100, InputTypeEnum.GAMEPAD_BUTTON));
    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_CENTER_2, GLFW_GAMEPAD_BUTTON_START, 0x800, InputTypeEnum.GAMEPAD_BUTTON));

    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_THUMB_1, 8, 0x200, InputTypeEnum.GAMEPAD_BUTTON));
    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_THUMB_2, 9, 0x400, InputTypeEnum.GAMEPAD_BUTTON));

    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_SHOULDER_LEFT_1, GLFW_GAMEPAD_BUTTON_LEFT_BUMPER, 0x4, InputTypeEnum.GAMEPAD_BUTTON));
    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_SHOULDER_LEFT_2, GLFW_GAMEPAD_AXIS_LEFT_TRIGGER, 0x1, InputTypeEnum.GAMEPAD_AXIS_BUTTON_POSITIVE));

    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_SHOULDER_RIGHT_1, GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER, 0x8, InputTypeEnum.GAMEPAD_BUTTON));
    this.bindings.add(new InputBinding(InputKeyCode.BUTTON_SHOULDER_RIGHT_2, GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER, 0x2, InputTypeEnum.GAMEPAD_AXIS_BUTTON_POSITIVE));

    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_LEFT_X, GLFW_GAMEPAD_AXIS_LEFT_X, -1, InputTypeEnum.GAMEPAD_AXIS));
    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_LEFT_Y, GLFW_GAMEPAD_AXIS_LEFT_Y, -1, InputTypeEnum.GAMEPAD_AXIS));

    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_RIGHT_X, GLFW_GAMEPAD_AXIS_RIGHT_X, -1, InputTypeEnum.GAMEPAD_AXIS));
    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_RIGHT_Y, GLFW_GAMEPAD_AXIS_RIGHT_Y, -1, InputTypeEnum.GAMEPAD_AXIS));

    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_LEFT_BUTTON_UP, GLFW_GAMEPAD_AXIS_LEFT_Y, 0x1000, InputTypeEnum.GAMEPAD_AXIS_BUTTON_NEGATIVE));
    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_LEFT_BUTTON_DOWN, GLFW_GAMEPAD_AXIS_LEFT_Y, 0x4000, InputTypeEnum.GAMEPAD_AXIS_BUTTON_POSITIVE));
    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_LEFT_BUTTON_LEFT, GLFW_GAMEPAD_AXIS_LEFT_X, 0x8000, InputTypeEnum.GAMEPAD_AXIS_BUTTON_NEGATIVE));
    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_LEFT_BUTTON_RIGHT, GLFW_GAMEPAD_AXIS_LEFT_X, 0x2000, InputTypeEnum.GAMEPAD_AXIS_BUTTON_POSITIVE));

    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_RIGHT_BUTTON_UP, GLFW_GAMEPAD_AXIS_RIGHT_Y, -1, InputTypeEnum.GAMEPAD_AXIS_BUTTON_NEGATIVE));
    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_RIGHT_BUTTON_DOWN, GLFW_GAMEPAD_AXIS_RIGHT_Y, -1, InputTypeEnum.GAMEPAD_AXIS_BUTTON_POSITIVE));
    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_RIGHT_BUTTON_LEFT, GLFW_GAMEPAD_AXIS_RIGHT_X, -1, InputTypeEnum.GAMEPAD_AXIS_BUTTON_NEGATIVE));
    this.bindings.add(new InputBinding(InputKeyCode.JOYSTICK_RIGHT_BUTTON_RIGHT, GLFW_GAMEPAD_AXIS_RIGHT_X, -1, InputTypeEnum.GAMEPAD_AXIS_BUTTON_POSITIVE));


    this.bindings.add(new InputBinding(InputKeyCode.DPAD_UP, GLFW_HAT_UP, 0x1000, InputTypeEnum.GAMEPAD_HAT));
    this.bindings.add(new InputBinding(InputKeyCode.DPAD_DOWN, GLFW_HAT_DOWN, 0x4000, InputTypeEnum.GAMEPAD_HAT));
    this.bindings.add(new InputBinding(InputKeyCode.DPAD_LEFT, GLFW_HAT_LEFT, 0x8000, InputTypeEnum.GAMEPAD_HAT));
    this.bindings.add(new InputBinding(InputKeyCode.DPAD_RIGHT, GLFW_HAT_RIGHT, 0x2000, InputTypeEnum.GAMEPAD_HAT));

  }

  public void update() {
    this.anyActivity = false;
    for(final InputBinding binding : this.bindings) {
      binding.update();
      if(binding.getState() == InputBindingStateEnum.PRESSED_THIS_FRAME) {
        this.anyActivity = true;
      }
    }
  }

  public boolean hasActivityThisFrame() {
    return this.anyActivity;
  }
}
