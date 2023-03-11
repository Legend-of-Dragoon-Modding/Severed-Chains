package legend.game.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ControllerDatabase {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker CONTROLLER_DB_MARKER = MarkerManager.getMarker("CONTROLLER_DB");
  private static final Marker CONTROLLER_VERBOSE_MARKER = MarkerManager.getMarker("CONTROLLER_VERBOSE");
  private static List<String> databaseEntries = new ArrayList<>();

  private ControllerDatabase() {
  }

  public static void loadControllerDb() {
    try {
      databaseEntries = Files.readAllLines(Path.of("gamecontrollerdb.txt"));
      LOGGER.info(CONTROLLER_DB_MARKER,"Found and Loaded Controller Database File.");
    } catch(final IOException exception) {
      LOGGER.error(CONTROLLER_DB_MARKER,"Controller database file not found! gamecontrollerdb.txt");
    }
  }

  public static List<InputBinding> getBindings(final String targetControllerGUID) {
    final List<InputBinding> returnedBindings = new ArrayList<>();

    if("Keyboard-Mouse Only".equals(targetControllerGUID)) {
      return returnedBindings;
    }

    final String dbMap = getDbMap(targetControllerGUID);

    for(final InputAction inputAction : InputAction.values()) {
      final InputBinding newBinding = new InputBinding(inputAction);
      getExistingBindingData(newBinding, dbMap);
      modifyForSpecialCases(newBinding);
      returnedBindings.add(newBinding);
    }
    return returnedBindings;
  }

  public static void getExistingBindingData(final InputBinding inputBinding, final String dbMap) {
    inputBinding.setHexCode(getHexCode(inputBinding.getInputAction()));

    final String textLabel = getTextCodeLabel(inputBinding.getInputAction());
    final String[] mapInfo = dbMap.split(",");
    for(final String mapEntry : mapInfo) {
      final String[] mapLabelAndData = mapEntry.split(":");
      if(mapLabelAndData.length > 1 && textLabel.equals(mapLabelAndData[0])) {
        LOGGER.info(CONTROLLER_VERBOSE_MARKER, "Found a match for text label %s with data %s", textLabel, mapLabelAndData[1]);
        setBindingFromText(inputBinding,mapLabelAndData[1]);
        return;
      }
    }
  }

  private static void setBindingFromText(final InputBinding inputBinding, final String bind) {
    if(bind.length() <= 1) {
      inputBinding.setGlfwKeyCode(-1);
      return;
    }

    if(bind.charAt(0) == 'b') {
      inputBinding.setInputType(InputType.GAMEPAD_BUTTON);
      inputBinding.setGlfwKeyCode(parseIntFromBindText(bind.substring(1)));
      return;
    } else if(bind.charAt(0) == 'a') {
      inputBinding.setInputType(InputType.GAMEPAD_AXIS);
      inputBinding.setGlfwKeyCode(parseIntFromBindText(bind.substring(1)));
      return;
    } else if(bind.charAt(0) == 'h') {
      inputBinding.setInputType(InputType.GAMEPAD_HAT);
      final String dataOnly = bind.substring(1);
      final String[] hatIndexAndCode = dataOnly.split("\\.");
      if(hatIndexAndCode.length > 1) {
        inputBinding.setHatIndex(parseIntFromBindText(hatIndexAndCode[0]));
        inputBinding.setGlfwKeyCode(parseIntFromBindText(hatIndexAndCode[1]));
        return;
      }
    }
    LOGGER.error(CONTROLLER_DB_MARKER, "Bad data failed to bind with text %s", bind);
    inputBinding.setGlfwKeyCode(-1);
  }

  private static int parseIntFromBindText(final String bindText) {
    try {
      return Integer.parseInt(bindText);
    } catch(final NumberFormatException exception) {
      LOGGER.error(CONTROLLER_DB_MARKER, "Bad data failed to convert text to number: %s", bindText);
    }
    return -1;
  }

  private static void modifyForSpecialCases(final InputBinding inputBinding) {

    switch(inputBinding.getInputAction()) {
      case JOYSTICK_LEFT_BUTTON_DOWN, JOYSTICK_LEFT_BUTTON_RIGHT, JOYSTICK_RIGHT_BUTTON_DOWN, JOYSTICK_RIGHT_BUTTON_RIGHT -> inputBinding.setInputType(InputType.GAMEPAD_AXIS_BUTTON_POSITIVE);
      case JOYSTICK_LEFT_BUTTON_UP, JOYSTICK_LEFT_BUTTON_LEFT, JOYSTICK_RIGHT_BUTTON_UP, JOYSTICK_RIGHT_BUTTON_LEFT -> inputBinding.setInputType(InputType.GAMEPAD_AXIS_BUTTON_NEGATIVE);
    }

    if(inputBinding.getInputType() == InputType.GAMEPAD_AXIS) {
      switch(inputBinding.getInputAction()) {
        case BUTTON_SHOULDER_LEFT_1, BUTTON_SHOULDER_LEFT_2, BUTTON_SHOULDER_RIGHT_1, BUTTON_SHOULDER_RIGHT_2 -> inputBinding.setInputType(InputType.GAMEPAD_AXIS_BUTTON_POSITIVE);
      }
    }
  }

  private static String getDbMap(final String controllerGUID) {
    final List<String> matchesInDb = new ArrayList<>();

    for(final String dbMap : databaseEntries) {
      final String[] mapInfo = dbMap.split(",");
      if(mapInfo[0].equals(controllerGUID)) {
        matchesInDb.add(dbMap);
      }
    }

    if(!matchesInDb.isEmpty()) {
      LOGGER.info(CONTROLLER_DB_MARKER,"Controller found in database with mapping of...");
      for(final String match : matchesInDb) {
        LOGGER.info(match);
      }
      return matchesInDb.get(0);
    }

    LOGGER.warn(CONTROLLER_DB_MARKER,"Controller not found in supported database for %s", controllerGUID);
    final String defaultMap = "00000000000000000000000000000000,GLFW Default Controller,a:b0,b:b1,back:b6,dpdown:h0.4,dpleft:h0.8,dpright:h0.2,dpup:h0.1,leftshoulder:b4,leftstick:b9,lefttrigger:a2,leftx:a0,lefty:a1,rightshoulder:b5,rightstick:b10,righttrigger:a5,rightx:a3,righty:a4,start:b7,x:b2,y:b3,platform:Windows,";
    LOGGER.warn(CONTROLLER_DB_MARKER,"Loading default map of... \n" + defaultMap );
    return defaultMap;
  }

  private static int getHexCode(final InputAction targetInputAction) {
    return switch(targetInputAction) {
      case BUTTON_NORTH -> 0x10;
      case BUTTON_SOUTH -> 0x20;
      case BUTTON_EAST -> 0x40;
      case BUTTON_WEST -> 0x80;

      case BUTTON_CENTER_1 -> 0x100;
      case BUTTON_CENTER_2 -> 0x800;

      case BUTTON_THUMB_1 -> 0x200;
      case BUTTON_THUMB_2 -> 0x400;

      case BUTTON_SHOULDER_LEFT_1 -> 0x4;
      case BUTTON_SHOULDER_LEFT_2 -> 0x1;

      case BUTTON_SHOULDER_RIGHT_1 -> 0x8;
      case BUTTON_SHOULDER_RIGHT_2 -> 0x2;

      case JOYSTICK_LEFT_BUTTON_UP, DPAD_UP -> 0x1000;
      case JOYSTICK_LEFT_BUTTON_DOWN, DPAD_DOWN -> 0x4000;
      case JOYSTICK_LEFT_BUTTON_LEFT, DPAD_LEFT -> 0x8000;
      case JOYSTICK_LEFT_BUTTON_RIGHT, DPAD_RIGHT -> 0x2000;

      default -> -1;
    };
  }

  private static String getTextCodeLabel(final InputAction targetInputAction) {
    return switch(targetInputAction) {
      case BUTTON_NORTH -> "y";
      case BUTTON_SOUTH -> "a";
      case BUTTON_EAST -> "b";
      case BUTTON_WEST -> "x";

      case BUTTON_CENTER_1 -> "back";
      case BUTTON_CENTER_2 -> "start";

      case BUTTON_THUMB_1 -> "leftstick";
      case BUTTON_THUMB_2 -> "rightstick";

      case BUTTON_SHOULDER_LEFT_1 -> "leftshoulder";
      case BUTTON_SHOULDER_LEFT_2 -> "lefttrigger";

      case BUTTON_SHOULDER_RIGHT_1 -> "rightshoulder";
      case BUTTON_SHOULDER_RIGHT_2 -> "righttrigger";

      case DPAD_UP -> "dpup";
      case DPAD_DOWN -> "dpdown";
      case DPAD_LEFT -> "dpleft";
      case DPAD_RIGHT -> "dpright";

      case JOYSTICK_LEFT_X, JOYSTICK_LEFT_BUTTON_LEFT, JOYSTICK_LEFT_BUTTON_RIGHT -> "leftx";
      case JOYSTICK_LEFT_Y,JOYSTICK_LEFT_BUTTON_UP,JOYSTICK_LEFT_BUTTON_DOWN -> "lefty";

      case JOYSTICK_RIGHT_X,  JOYSTICK_RIGHT_BUTTON_LEFT, JOYSTICK_RIGHT_BUTTON_RIGHT -> "rightx";
      case JOYSTICK_RIGHT_Y,JOYSTICK_RIGHT_BUTTON_UP, JOYSTICK_RIGHT_BUTTON_DOWN-> "righty";

      default -> "nobinding";
    };
  }
}
