package legend.game.input;

import legend.core.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

public final class InputControllerAssigner {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker INPUT_MARKER = MarkerManager.getMarker("INPUT");
  private final static int desiredControllerCount = 1;

  private static final List<InputControllerData> assignedControllers = new ArrayList<>();
  private static final List<InputControllerData> connectedControllers = new ArrayList<>();
  private static final List<InputControllerData> idleControllers = new ArrayList<>();

  private static boolean isAssigningControllersBool;

  private InputControllerAssigner() {
  }

  public static void init() {
    logConnectedControllers();

    ControllerDatabase.loadControllerDb();
    final String controllerGuidFromConfig = Config.controllerGuid();

    if(isAnyControllerPresent()) {
      // Check for matching GUID
      for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
        if(controllerGuidFromConfig.equals(glfwGetJoystickGUID(i))) {
          final InputControllerData controllerData = new InputControllerData(glfwGetJoystickName(i), glfwGetJoystickGUID(i), i);
          controllerData.setPlayerSlot(1);
          connectedControllers.add(controllerData);
          assignedControllers.add(controllerData);
          Input.refreshControllers();
          LOGGER.info(INPUT_MARKER,"Last used controller found and connected");
          break;
        }
      }
    }
    else {
      LOGGER.info(INPUT_MARKER,"No controllers connected");

      final InputControllerData controllerData = new InputControllerData("Keyboard-Mouse Only", "Keyboard-Mouse Only", -1);
      controllerData.setPlayerSlot(1);
      connectedControllers.add(controllerData);
      assignedControllers.add(controllerData);
      Input.refreshControllers();

    }

  }

  private static void logConnectedControllers() {
    for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
      if(glfwJoystickPresent(i)) {
        LOGGER.info(INPUT_MARKER,"Controller Id: %d - GUID: %s",i ,glfwGetJoystickGUID(i));
      }
    }
  }

  private static boolean isAnyControllerPresent() {
    for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
      if(glfwJoystickPresent(i)) {
        return true;
      }
    }
    return false;
  }

  public static void reassignSequence() {
    connectedControllers.clear();
    idleControllers.clear();
    assignedControllers.clear();

    for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
      if(glfwJoystickPresent(i)) {
        LOGGER.info(INPUT_MARKER,"%d: %s (%d)",(i+1),glfwGetJoystickName(i),glfwGetJoystickGUID(i));
        final InputControllerData controllerData = new InputControllerData(glfwGetJoystickName(i), glfwGetJoystickGUID(i), i);
        connectedControllers.add(controllerData);
      }
    }

    LOGGER.info(INPUT_MARKER,"Found a total of %d",connectedControllers.size());

    if(connectedControllers.isEmpty()) {
      LOGGER.info(INPUT_MARKER,"No controllers connected");
    }
    if(connectedControllers.size() == 1) {

      LOGGER.info(INPUT_MARKER,"Only 1 controller connected so assigning by default. %s",connectedControllers.get(0).getInfoString());
      connectedControllers.get(0).setPlayerSlot(1);
      assignedControllers.add(connectedControllers.get(0));
      savePlayerOneToConfig();
      Input.refreshControllers();
    }
    if(connectedControllers.size() >= 2) {
      LOGGER.info(INPUT_MARKER,"Multiple controllers connected. Please press any of the buttons for the one you want to use");
      isAssigningControllersBool = true;
      idleControllers.addAll(connectedControllers);
    }
  }

  public static void update() {
    for(final InputControllerData controllerData : idleControllers) {
      controllerData.updateState();
      if(controllerData.hasAnyButtonActivity()) {
        LOGGER.info(INPUT_MARKER,"Button motion detected with controller %s",controllerData.getInfoString());
        assignedControllers.add(controllerData);
        idleControllers.remove(controllerData);

        LOGGER.info(INPUT_MARKER,"Assigning as player %d",assignedControllers.size());
        controllerData.setPlayerSlot(assignedControllers.size());
        if(assignedControllers.size() == 1) {
          savePlayerOneToConfig();
        }

        if(assignedControllers.size() < desiredControllerCount) {
          LOGGER.info(INPUT_MARKER,"Please press the buttons for player %d",assignedControllers.size() + 1);
        }

        break;
      }
    }

    if(assignedControllers.size() >= desiredControllerCount) {
      LOGGER.info(INPUT_MARKER,"Target controller count of %d reached",desiredControllerCount);
      isAssigningControllersBool = false;
      Input.refreshControllers();
    }

    if(idleControllers.isEmpty()) {
      LOGGER.info(INPUT_MARKER,"No more controllers detected. Continuing with the %d assigned controllers ",assignedControllers.size());
      isAssigningControllersBool = false;
      Input.refreshControllers();
    }

  }

  public static boolean isAssigningControllers() {
    return isAssigningControllersBool;
  }

  public static List<InputControllerData> getAssignedControllers() {
    return assignedControllers;
  }

  public static List<InputControllerData> getConnectedControllers() {
    return connectedControllers;
  }

  public static List<InputControllerData> getIdleControllers() {
    return idleControllers;
  }

  private static void savePlayerOneToConfig() {
    Config.controllerGuid(assignedControllers.get(0).getGlfwJoystickGUID());

    try {
      Config.save();
    } catch(final IOException e) {
      LOGGER.warn(INPUT_MARKER,"Failed to save config");
    }

  }

}
