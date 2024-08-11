package legend.game.input;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;
import legend.core.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;

public class JamepadController extends Controller {
  private static final Logger LOGGER = LogManager.getFormatterLogger(JamepadController.class);

  private final ControllerIndex controller;

  private float rumbleBigCurrentIntensity;
  private float rumbleSmallCurrentIntensity;
  private float rumbleBigStartingIntensity;
  private float rumbleSmallStartingIntensity;
  private float rumbleBigEndingIntensity;
  private float rumbleSmallEndingIntensity;
  private long rumbleLerpStart;
  private long rumbleLerpDuration;

  public JamepadController(final ControllerIndex controller) {
    this.controller = controller;
    this.addBinding(ControllerButton.DPAD_UP, InputAction.DPAD_UP, InputType.GAMEPAD_HAT);
    this.addBinding(ControllerButton.DPAD_DOWN, InputAction.DPAD_DOWN, InputType.GAMEPAD_HAT);
    this.addBinding(ControllerButton.DPAD_LEFT, InputAction.DPAD_LEFT, InputType.GAMEPAD_HAT);
    this.addBinding(ControllerButton.DPAD_RIGHT, InputAction.DPAD_RIGHT, InputType.GAMEPAD_HAT);
    this.addBinding(ControllerButton.Y, InputAction.BUTTON_NORTH, InputType.GAMEPAD_BUTTON);
    this.addBinding(ControllerButton.A, InputAction.BUTTON_SOUTH, InputType.GAMEPAD_BUTTON);
    this.addBinding(ControllerButton.B, InputAction.BUTTON_EAST, InputType.GAMEPAD_BUTTON);
    this.addBinding(ControllerButton.X, InputAction.BUTTON_WEST, InputType.GAMEPAD_BUTTON);
    this.addBinding(ControllerButton.BACK, InputAction.BUTTON_CENTER_1, InputType.GAMEPAD_BUTTON);
    this.addBinding(ControllerButton.START, InputAction.BUTTON_CENTER_2, InputType.GAMEPAD_BUTTON);
    this.addBinding(ControllerButton.LEFTBUMPER, InputAction.BUTTON_SHOULDER_LEFT_1, InputType.GAMEPAD_BUTTON);
    this.addBinding(ControllerButton.RIGHTBUMPER, InputAction.BUTTON_SHOULDER_RIGHT_1, InputType.GAMEPAD_BUTTON);
    this.addBinding(ControllerAxis.TRIGGERLEFT, InputAction.BUTTON_SHOULDER_LEFT_2, InputType.GAMEPAD_AXIS_BUTTON_POSITIVE);
    this.addBinding(ControllerAxis.TRIGGERRIGHT, InputAction.BUTTON_SHOULDER_RIGHT_2, InputType.GAMEPAD_AXIS_BUTTON_POSITIVE);
    this.addBinding(ControllerButton.LEFTSTICK, InputAction.BUTTON_THUMB_1, InputType.GAMEPAD_BUTTON);
    this.addBinding(ControllerButton.RIGHTSTICK, InputAction.BUTTON_THUMB_2, InputType.GAMEPAD_BUTTON);
    this.addBinding(ControllerAxis.LEFTX, InputAction.JOYSTICK_LEFT_X, InputType.GAMEPAD_AXIS);
    this.addBinding(ControllerAxis.LEFTY, InputAction.JOYSTICK_LEFT_Y, InputType.GAMEPAD_AXIS);
    this.addBinding(ControllerAxis.LEFTY, InputAction.JOYSTICK_LEFT_BUTTON_UP, InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE);
    this.addBinding(ControllerAxis.LEFTY, InputAction.JOYSTICK_LEFT_BUTTON_DOWN, InputType.GAMEPAD_AXIS_BUTTON_POSITIVE);
    this.addBinding(ControllerAxis.LEFTX, InputAction.JOYSTICK_LEFT_BUTTON_LEFT, InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE);
    this.addBinding(ControllerAxis.LEFTX, InputAction.JOYSTICK_LEFT_BUTTON_RIGHT, InputType.GAMEPAD_AXIS_BUTTON_POSITIVE);
    this.addBinding(ControllerAxis.RIGHTX, InputAction.JOYSTICK_RIGHT_X, InputType.GAMEPAD_AXIS);
    this.addBinding(ControllerAxis.RIGHTY, InputAction.JOYSTICK_RIGHT_Y, InputType.GAMEPAD_AXIS);
    this.addBinding(ControllerAxis.RIGHTY, InputAction.JOYSTICK_RIGHT_BUTTON_UP, InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE);
    this.addBinding(ControllerAxis.RIGHTY, InputAction.JOYSTICK_RIGHT_BUTTON_DOWN, InputType.GAMEPAD_AXIS_BUTTON_POSITIVE);
    this.addBinding(ControllerAxis.RIGHTX, InputAction.JOYSTICK_RIGHT_BUTTON_LEFT, InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE);
    this.addBinding(ControllerAxis.RIGHTX, InputAction.JOYSTICK_RIGHT_BUTTON_RIGHT, InputType.GAMEPAD_AXIS_BUTTON_POSITIVE);
  }

  private void addBinding(final ControllerButton button, final InputAction action, final InputType type) {
    try {
      if(this.controller.isButtonAvailable(button)) {
        final InputBinding binding = new InputBinding(action, this, type);
        binding.setButtonCode(button.ordinal());
        this.bindings.add(binding);
      }
    } catch(final ControllerUnpluggedException ignored) { }
  }

  private void addBinding(final ControllerAxis axis, final InputAction action, final InputType type) {
    try {
      if(this.controller.isAxisAvailable(axis)) {
        final InputBinding binding = new InputBinding(action, this, type);
        binding.setButtonCode(axis.ordinal());
        this.bindings.add(binding);
      }
    } catch(final ControllerUnpluggedException ignored) { }
  }

  public int getId() {
    try {
      return this.controller.getDeviceInstanceID();
    } catch(final ControllerUnpluggedException e) {
      LOGGER.error("Controller disconnected");
      return -1;
    }
  }

  public boolean isConnected() {
    return this.controller.isConnected();
  }

  @Override
  public void poll() {
    for(final InputBinding binding : this.bindings) {
      binding.poll();
    }

    if(this.rumbleLerpStart != 0) {
      final long time = System.nanoTime() - this.rumbleLerpStart;
      final float ratio = MathHelper.clamp(time / (float)this.rumbleLerpDuration, 0.0f, 1.0f);
      final float big = Math.lerp(this.rumbleBigStartingIntensity, this.rumbleBigEndingIntensity, ratio);
      final float small = Math.lerp(this.rumbleSmallStartingIntensity, this.rumbleSmallEndingIntensity, ratio);
//      LOGGER.info("%d / %d = %f", time, this.rumbleLerpDuration, ratio);
//      LOGGER.info("Big %f -> %f -> %f", this.rumbleBigCurrentIntensity, big, this.rumbleBigEndingIntensity);
//      LOGGER.info("Small %f -> %f -> %f", this.rumbleSmallCurrentIntensity, small, this.rumbleSmallEndingIntensity);
      this.rumble(big, small, 0);

      if(time >= this.rumbleLerpDuration) {
        this.rumbleLerpStart = 0;
      }
    }
  }

  @Override
  public boolean readButton(final int input) {
    try {
      return this.controller.isButtonPressed(ControllerButton.values()[input]);
    } catch(final ControllerUnpluggedException e) {
      LOGGER.error("Controller disconnected");
      return false;
    }
  }

  @Override
  public float readAxis(final int input) {
    try {
      return this.controller.getAxisState(ControllerAxis.values()[input]);
    } catch(final ControllerUnpluggedException e) {
      LOGGER.error("Controller disconnected");
      return 0.0f;
    }
  }

  @Override
  public boolean readHat(final int input, final int hatIndex) {
    try {
      return this.controller.isButtonPressed(ControllerButton.values()[input]);
    } catch(final ControllerUnpluggedException e) {
      LOGGER.error("Controller disconnected");
      return false;
    }
  }

  @Override
  public String getName() {
    try {
      return this.controller.getName();
    } catch(final ControllerUnpluggedException e) {
      LOGGER.error("Controller disconnected");
      return "Disconnected";
    }
  }

  @Override
  public String getGuid() {
    return this.getName();
  }

  @Override
  public void rumble(final float bigIntensity, final float smallIntensity, final int ms) {
    this.rumbleBigCurrentIntensity = bigIntensity;
    this.rumbleSmallCurrentIntensity = smallIntensity;

    try {
      this.controller.doVibration(bigIntensity, smallIntensity, ms);
    } catch(final ControllerUnpluggedException e) {
      LOGGER.error("Controller disconnected");
    }
  }

  @Override
  public void adjustRumble(final float bigIntensity, final float smallIntensity, final int ms) {
    this.rumbleBigStartingIntensity = this.rumbleBigCurrentIntensity;
    this.rumbleSmallStartingIntensity = this.rumbleSmallCurrentIntensity;
    this.rumbleBigEndingIntensity = bigIntensity;
    this.rumbleSmallEndingIntensity = smallIntensity;
    this.rumbleLerpStart = System.nanoTime();
    this.rumbleLerpDuration = ms * 1_000_000L;
  }

  @Override
  public boolean equals(final Object obj) {
    if(obj instanceof final JamepadController other) {
      try {
        return this.controller.getDeviceInstanceID() == other.controller.getDeviceInstanceID();
      } catch(final ControllerUnpluggedException e) {
        LOGGER.error("Controller disconnected");
        return false;
      }
    }

    return super.equals(obj);
  }
}
