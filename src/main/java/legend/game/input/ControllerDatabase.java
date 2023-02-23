package legend.game.input;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static legend.game.unpacker.Unpacker.LOGGER;

public final class ControllerDatabase {
  private static List<String> databaseEntries = new ArrayList<>();
  private static final boolean logVerbose = false;

  public static void loadControllerDB() {
    final URL pathURL = ControllerDatabase.class.getResource("/input/gamecontrollerdb.txt");
    final Path path;
    try {
      final URI uri = pathURL.toURI();
      path = Paths.get(uri);

      try {
        databaseEntries = Files.readAllLines(path);
      } catch(final IOException ex) {
        LOGGER.error("Invalid path while trying to load controller database\n{}", path);
      }

    } catch(final URISyntaxException e) {
      LOGGER.error("Could not convert URL(Java) to URI");
    }
  }

  public static List<InputBinding> GetBindings(final String targetControllerGUID) {
    final List<InputBinding> returnedBindings = new ArrayList<>();

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
      if(textLabel.equals(mapLabelAndData[0])) {
        if(logVerbose) {
          LOGGER.info("Found a match for text label {} with data {}", textLabel, mapLabelAndData[1]);
        }
        if(mapLabelAndData[1].charAt(0) == 'b') {
          inputBinding.setInputType(InputType.GAMEPAD_BUTTON);
          inputBinding.setGlfwKeyCode(Integer.parseInt(mapLabelAndData[1].substring(1)));
        } else if(mapLabelAndData[1].charAt(0) == 'a') {
          inputBinding.setInputType(InputType.GAMEPAD_AXIS);
          inputBinding.setGlfwKeyCode(Integer.parseInt(mapLabelAndData[1].substring(1)));
        } else if(mapLabelAndData[1].charAt(0) == 'h') {
          inputBinding.setInputType(InputType.GAMEPAD_HAT);
          final String dataOnly = mapLabelAndData[1].substring(1);
          final String[] hatIndexAndCode = dataOnly.split("\\.");
          inputBinding.setHatIndex(Integer.parseInt(hatIndexAndCode[0]));
          inputBinding.setGlfwKeyCode(Integer.parseInt(hatIndexAndCode[1]));
        }
      }
    }
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

    if(matchesInDb.size() > 0) {
      LOGGER.info("Controller found in database with mapping of...");
      for(final String match : matchesInDb) {
        LOGGER.info(match);
      }
      return matchesInDb.get(0);
    }

    LOGGER.warn("Controller not found in supported database for {}", controllerGUID);
    final String defaultMap = "00000000000000000000000000000000,GLFW Default Controller,a:b0,b:b1,back:b6,dpdown:h0.4,dpleft:h0.8,dpright:h0.2,dpup:h0.1,leftshoulder:b4,leftstick:b9,lefttrigger:a2,leftx:a0,lefty:a1,rightshoulder:b5,rightstick:b10,righttrigger:a5,rightx:a3,righty:a4,start:b7,x:b2,y:b3,platform:Windows,";
    LOGGER.warn("Loading default map of... \n" + defaultMap );
    return defaultMap;
  }

  private static int getHexCode(final InputAction targetInputAction)
  {
    switch(targetInputAction)
    {
      case BUTTON_NORTH -> {return 0x10;}
      case BUTTON_SOUTH -> {return 0x20;}
      case BUTTON_EAST -> {return 0x80;}
      case BUTTON_WEST -> {return 0x40;}

      case BUTTON_CENTER_1 -> {return 0x100;}
      case BUTTON_CENTER_2 -> {return 0x800;}

      case BUTTON_THUMB_1 -> {return 0x200;}
      case BUTTON_THUMB_2 -> {return 0x400;}

      case BUTTON_SHOULDER_LEFT_1 -> {return 0x4;}
      case BUTTON_SHOULDER_LEFT_2 -> {return 0x1;}

      case BUTTON_SHOULDER_RIGHT_1 -> {return 0x8;}
      case BUTTON_SHOULDER_RIGHT_2 -> {return 0x2;}

      case JOYSTICK_LEFT_BUTTON_UP,DPAD_UP -> { return 0x1000;}
      case JOYSTICK_LEFT_BUTTON_DOWN,DPAD_DOWN -> { return 0x4000;}
      case JOYSTICK_LEFT_BUTTON_LEFT,DPAD_LEFT -> { return 0x8000;}
      case JOYSTICK_LEFT_BUTTON_RIGHT,DPAD_RIGHT -> { return 0x2000;}

      case JOYSTICK_LEFT_X, JOYSTICK_LEFT_Y, JOYSTICK_RIGHT_X, JOYSTICK_RIGHT_Y,
        JOYSTICK_RIGHT_BUTTON_UP, JOYSTICK_RIGHT_BUTTON_DOWN, JOYSTICK_RIGHT_BUTTON_LEFT, JOYSTICK_RIGHT_BUTTON_RIGHT -> {
        return -1;
      }
    }

    return -1;
  }

  private static String getTextCodeLabel(final InputAction targetInputAction) {
    switch(targetInputAction) {
      case BUTTON_NORTH -> {return "y";}
      case BUTTON_SOUTH -> {return "a";}
      case BUTTON_EAST -> {return "b";}
      case BUTTON_WEST -> {return "x";}

      case BUTTON_CENTER_1 -> {return "back";}
      case BUTTON_CENTER_2 -> {return "start";}

      case BUTTON_THUMB_1 -> {return "leftstick";}
      case BUTTON_THUMB_2 -> {return "rightstick";}

      case BUTTON_SHOULDER_LEFT_1 -> {return "leftshoulder";}
      case BUTTON_SHOULDER_LEFT_2 -> {return "lefttrigger";}

      case BUTTON_SHOULDER_RIGHT_1 -> {return "rightshoulder";}
      case BUTTON_SHOULDER_RIGHT_2 -> {return "righttrigger";}

      case DPAD_UP -> { return "dpup";}
      case DPAD_DOWN -> { return "dpdown";}
      case DPAD_LEFT -> { return "dpleft";}
      case DPAD_RIGHT -> { return "dpright";}

      case JOYSTICK_LEFT_X, JOYSTICK_LEFT_BUTTON_LEFT, JOYSTICK_LEFT_BUTTON_RIGHT -> {return "leftx";}
      case JOYSTICK_LEFT_Y,JOYSTICK_LEFT_BUTTON_UP,JOYSTICK_LEFT_BUTTON_DOWN -> {return "lefty";}

      case JOYSTICK_RIGHT_X,  JOYSTICK_RIGHT_BUTTON_LEFT, JOYSTICK_RIGHT_BUTTON_RIGHT -> {return "rightx";}
      case JOYSTICK_RIGHT_Y,JOYSTICK_RIGHT_BUTTON_UP, JOYSTICK_RIGHT_BUTTON_DOWN-> {return "righty";}
    }
    return null;
  }
}
