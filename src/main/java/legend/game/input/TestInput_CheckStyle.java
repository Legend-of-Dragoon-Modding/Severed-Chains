package legend.game.input;

import static legend.game.unpacker.Unpacker.LOGGER;

public final class TestInput_CheckStyle {
  private static final boolean DISABLE_LOGGING = true;

  public static void update() {
    if(DISABLE_LOGGING) {
      return;
    }

    if(Input.getButtonState(InputKeyCode.BUTTON_NORTH)) {
      //LOGGER.info("NORTH BUTTON PRESSED, Will spam logger");
    }

    if(Input.pressedThisFrame(InputKeyCode.BUTTON_NORTH)) {
      LOGGER.info("NORTH BUTTON PRESSED THIS FRAME ");
    }
    if(Input.pressedThisFrame(InputKeyCode.BUTTON_SOUTH)) {
      LOGGER.info("SOUTH BUTTON PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.BUTTON_EAST)) {
      LOGGER.info("EAST BUTTON PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.BUTTON_WEST)) {
      LOGGER.info("WEST BUTTON PRESSED THIS FRAME");
    }

    if(Input.releasedThisFrame(InputKeyCode.BUTTON_NORTH)) {
      LOGGER.info("NORTH BUTTON RE-LEASED THIS FRAME");
    }
    if(Input.releasedThisFrame(InputKeyCode.BUTTON_SOUTH)) {
      LOGGER.info("SOUTH BUTTON RE-LEASED THIS FRAME");
    }
    if(Input.releasedThisFrame(InputKeyCode.BUTTON_EAST)) {
      LOGGER.info("EAST BUTTON RE-LEASED THIS FRAME");
    }
    if(Input.releasedThisFrame(InputKeyCode.BUTTON_WEST)) {
      LOGGER.info("WEST BUTTON RE-LEASED THIS FRAME");
    }

    // Center area (Select, Start, Xbox Button)
    if(Input.pressedThisFrame(InputKeyCode.BUTTON_CENTER_1)) {
      LOGGER.info("CENTER 1 PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.BUTTON_CENTER_2)) {
      LOGGER.info("CENTER 2 PRESSED THIS FRAME");
    }

    // L1, L2, R1, R2
    if(Input.pressedThisFrame(InputKeyCode.BUTTON_SHOULDER_LEFT_1)) {
      LOGGER.info("SHOULDER LEFT 1 PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.BUTTON_SHOULDER_LEFT_2)) {
      LOGGER.info("SHOULDER LEFT 2 PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.BUTTON_SHOULDER_RIGHT_1)) {
      LOGGER.info("SHOULDER RIGHT 1 PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.BUTTON_SHOULDER_RIGHT_2)) {
      LOGGER.info("SHOULDER RIGHT 2 PRESSED THIS FRAME");
    }

    // L3, R3, (Joystick Click)
    if(Input.pressedThisFrame(InputKeyCode.BUTTON_THUMB_1)) {
      LOGGER.info("THUMB STICK 1 PRESSED THIS FRAME");
    }

    if(Input.pressedThisFrame(InputKeyCode.BUTTON_THUMB_2)) {
      LOGGER.info("THUMB STICK 2 PRESSED THIS FRAME");
    }

    logDpad();

    // Can check if the axis is positive, negative, or centered
    // logLeftStickAxisState();
    // logRightStickAxisState();

    // You can also treat the joysticks as buttons
    // Meaning you can take advantage of the PressedThisFrame and
    // ReleasedThisFrame
    logLeftStickAxisAsButton(); // LeftStick
    logRightStickAxisAsButton_PressedThisFrame(); // RightStick

    // If you want access to the axis float value
    // LOGGER.info("RIGHT STICK X VALUE: " + Input.getAxisStateRaw(InputKeyCode.JOYSTICK_RIGHT_X));

    // Nothing new to show, just for testing the other stick
    //logLeftStickAxisAsButton_PressedThisFrame();
    //logRightStickAxisAsButton();
  }


  private static void logDpad() {
    if(Input.pressedThisFrame(InputKeyCode.DPAD_UP)) {
      LOGGER.info("DPAD UP PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.DPAD_DOWN)) {
      LOGGER.info("DPAD DOWN PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.DPAD_LEFT)) {
      LOGGER.info("DPAD LEFT PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.DPAD_RIGHT)) {
      LOGGER.info("DPAD RIGHT PRESSED THIS FRAME");
    }

    if(Input.getButtonState(InputKeyCode.DPAD_LEFT) && Input.getButtonState(InputKeyCode.DPAD_UP)) {
      LOGGER.info("DPAD LEFT UP PRESSED (DIAGONAL)");
    }
  }

  private static void logLeftStickAxisAsButton() {
    if(Input.getButtonState(InputKeyCode.JOYSTICK_LEFT_BUTTON_UP)) {
      LOGGER.info("UP ON JOYSTICK LEFT TREATED AS BUTTON");
    }
    if(Input.getButtonState(InputKeyCode.JOYSTICK_LEFT_BUTTON_DOWN)) {
      LOGGER.info("DOWN ON JOYSTICK LEFT TREATED AS BUTTON");
    }
    if(Input.getButtonState(InputKeyCode.JOYSTICK_LEFT_BUTTON_LEFT)) {
      LOGGER.info("LEFT ON JOYSTICK LEFT TREATED AS BUTTON");
    }
    if(Input.getButtonState(InputKeyCode.JOYSTICK_LEFT_BUTTON_RIGHT)) {
      LOGGER.info("RIGHT ON JOYSTICK LEFT TREATED AS BUTTON");
    }
  }

  private static void logRightStickAxisAsButton() {
    if(Input.getButtonState(InputKeyCode.JOYSTICK_RIGHT_BUTTON_UP)) {
      LOGGER.info("UP ON JOYSTICK RIGHT TREATED AS BUTTON");
    }
    if(Input.getButtonState(InputKeyCode.JOYSTICK_RIGHT_BUTTON_DOWN)) {
      LOGGER.info("DOWN ON JOYSTICK RIGHT TREATED AS BUTTON");
    }
    if(Input.getButtonState(InputKeyCode.JOYSTICK_RIGHT_BUTTON_LEFT)) {
      LOGGER.info("LEFT ON JOYSTICK RIGHT TREATED AS BUTTON");
    }
    if(Input.getButtonState(InputKeyCode.JOYSTICK_RIGHT_BUTTON_RIGHT)) {
      LOGGER.info("RIGHT ON JOYSTICK RIGHT TREATED AS BUTTON");
    }
  }

  private static void logLeftStickAxisAsButton_PressedThisFrame() {
    if(Input.pressedThisFrame(InputKeyCode.JOYSTICK_LEFT_BUTTON_UP)) {
      LOGGER.info("UP ON JOYSTICK LEFT TREATED AS BUTTON - PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.JOYSTICK_LEFT_BUTTON_DOWN)) {
      LOGGER.info("DOWN ON JOYSTICK LEFT TREATED AS BUTTON - PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.JOYSTICK_LEFT_BUTTON_LEFT)) {
      LOGGER.info("LEFT ON JOYSTICK LEFT TREATED AS BUTTON - PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.JOYSTICK_LEFT_BUTTON_RIGHT)) {
      LOGGER.info("RIGHT ON JOYSTICK LEFT TREATED AS BUTTON - PRESSED THIS FRAME");
    }
  }


  private static void logLeftStickAxisState() {
    if(Input.getAxisState(InputKeyCode.JOYSTICK_LEFT_X) == InputAxisState.AXIS_POSITIVE) {
      LOGGER.info("LEFT STICK X POSITIVE");
    }
    if(Input.getAxisState(InputKeyCode.JOYSTICK_LEFT_X) == InputAxisState.AXIS_NEGATIVE) {
      LOGGER.info("LEFT STICK X NEGATIVE");
    }
    if(Input.getAxisState(InputKeyCode.JOYSTICK_LEFT_X) == InputAxisState.AXIS_CENTERED) {
      //LOGGER.info("LEFT STICK X CENTERED, will spam logger");
    }

    if(Input.getAxisState(InputKeyCode.JOYSTICK_LEFT_Y) == InputAxisState.AXIS_POSITIVE) {
      LOGGER.info("LEFT STICK Y POSITIVE");
    }
    if(Input.getAxisState(InputKeyCode.JOYSTICK_LEFT_Y) == InputAxisState.AXIS_NEGATIVE) {
      LOGGER.info("LEFT STICK Y NEGATIVE");
    }
    if(Input.getAxisState(InputKeyCode.JOYSTICK_LEFT_Y) == InputAxisState.AXIS_CENTERED) {
      //LOGGER.info("LEFT STICK Y CENTERED, will spam logger");
    }

  }

  private static void logRightStickAxisState() {
    if(Input.getAxisState(InputKeyCode.JOYSTICK_RIGHT_X) == InputAxisState.AXIS_POSITIVE) {
      LOGGER.info("RIGHT STICK X POSITIVE");
    }
    if(Input.getAxisState(InputKeyCode.JOYSTICK_RIGHT_X) == InputAxisState.AXIS_NEGATIVE) {
      LOGGER.info("RIGHT STICK X NEGATIVE");
    }
    if(Input.getAxisState(InputKeyCode.JOYSTICK_RIGHT_X) == InputAxisState.AXIS_CENTERED) {
      //LOGGER.info("RIGHT STICK X CENTERED, will spam logger");
    }

    if(Input.getAxisState(InputKeyCode.JOYSTICK_RIGHT_Y) == InputAxisState.AXIS_POSITIVE) {
      LOGGER.info("RIGHT STICK Y  POSITIVE");
    }
    if(Input.getAxisState(InputKeyCode.JOYSTICK_RIGHT_Y) == InputAxisState.AXIS_NEGATIVE) {
      LOGGER.info("RIGHT STICK Y NEGATIVE");
    }
    if(Input.getAxisState(InputKeyCode.JOYSTICK_RIGHT_Y) == InputAxisState.AXIS_CENTERED) {
      //LOGGER.info("RIGHT STICK Y CENTERED, will spam logger");
    }
  }

  private static void logRightStickAxisAsButton_PressedThisFrame() {
    if(Input.pressedThisFrame(InputKeyCode.JOYSTICK_RIGHT_BUTTON_UP)) {
      LOGGER.info("UP ON JOYSTICK RIGHT TREATED AS BUTTON - PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.JOYSTICK_RIGHT_BUTTON_DOWN)) {
      LOGGER.info("DOWN ON JOYSTICK RIGHT TREATED AS BUTTON - PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.JOYSTICK_RIGHT_BUTTON_LEFT)) {
      LOGGER.info("LEFT ON JOYSTICK RIGHT TREATED AS BUTTON - PRESSED THIS FRAME");
    }
    if(Input.pressedThisFrame(InputKeyCode.JOYSTICK_RIGHT_BUTTON_RIGHT)) {
      LOGGER.info("RIGHT ON JOYSTICK RIGHT TREATED AS BUTTON - PRESSED THIS FRAME");
    }
  }

  private TestInput_CheckStyle() {
  }
}
