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

public class ControllerManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker INPUT_MARKER = MarkerManager.getMarker("INPUT");

  private static final int desiredControllerCount = 1;

  private final List<HardwareController> assignedControllers = new ArrayList<>();
  private final List<HardwareController> connectedControllers = new ArrayList<>();
  private final List<HardwareController> idleControllers = new ArrayList<>();

  private boolean assigningControllers;

  public ControllerManager() {
    ControllerDatabase.loadControllerDb();
  }

  public void pollAssignedControllers() {
    for(final HardwareController hardwareController : this.assignedControllers) {
      hardwareController.poll();
    }
  }

  public void init() {
    final String controllerGuidFromConfig = Config.controllerGuid();

    for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
      if(glfwJoystickPresent(i)) {
        final String controllerGuid = glfwGetJoystickGUID(i);
        final HardwareController controllerData = new HardwareController(glfwGetJoystickName(i), controllerGuid, i);

        this.connectedControllers.add(controllerData);

        if(controllerGuidFromConfig.equals(controllerGuid)) {
          controllerData.setPlayerSlot(1);
          this.assignedControllers.add(controllerData);
        } else {
          this.idleControllers.add(controllerData);
        }
      }
    }

    if(!this.assignedControllers.isEmpty()) {
      LOGGER.info(INPUT_MARKER, "Last controller used found and connected");
      Input.refreshControllers();
      return;
    }

    if(this.connectedControllers.isEmpty()) {
      LOGGER.info(INPUT_MARKER, "No controllers connected, Keyboard-Mouse Only");

      final HardwareController controllerData = new HardwareController();
      controllerData.setPlayerSlot(1);
      this.connectedControllers.add(controllerData);
      this.assignedControllers.add(controllerData);

      Input.refreshControllers();
      return;
    }

    this.reassignSequence();
  }

  public void reassignSequence() {
    this.connectedControllers.clear();
    this.idleControllers.clear();
    this.assignedControllers.clear();

    for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
      if(glfwJoystickPresent(i)) {
        LOGGER.info(INPUT_MARKER, "Controller Id: %d - GUID: %s ", i, glfwGetJoystickGUID(i));
        final HardwareController controllerData = new HardwareController(glfwGetJoystickName(i), glfwGetJoystickGUID(i), i);
        this.connectedControllers.add(controllerData);
      }
    }

    LOGGER.info(INPUT_MARKER,"Found a total of %d", this.connectedControllers.size());

    if(this.connectedControllers.isEmpty()) {
      LOGGER.info(INPUT_MARKER, "No controllers connected, Keyboard-Mouse Only");

      final HardwareController controllerData = new HardwareController();
      controllerData.setPlayerSlot(1);
      this.connectedControllers.add(controllerData);
      this.assignedControllers.add(controllerData);

      Input.refreshControllers();
      return;
    }

    if(this.connectedControllers.size() == 1) {
      LOGGER.info(INPUT_MARKER,"Only 1 controller connected so assigning by default. %s", this.connectedControllers.get(0).getInfoString());
      this.connectedControllers.get(0).setPlayerSlot(1);
      this.assignedControllers.add(this.connectedControllers.get(0));
      this.savePlayerOneToConfig();
      Input.refreshControllers();
    }

    if(this.connectedControllers.size() >= 2) {
      LOGGER.info(INPUT_MARKER,"Multiple controllers connected. Please press any of the buttons for the one you want to use");
      this.assigningControllers = true;
      this.idleControllers.addAll(this.connectedControllers);
    }
  }

  public void lookForInputOnIdleControllers() {
    for(final HardwareController controllerData : this.idleControllers) {
      controllerData.poll();

      if(controllerData.hasAnyButtonActivity()) {
        LOGGER.info(INPUT_MARKER, "Button motion detected with controller %s", controllerData.getInfoString());
        this.assignedControllers.add(controllerData);
        this.idleControllers.remove(controllerData);

        LOGGER.info(INPUT_MARKER, "Assigning as player %d", this.assignedControllers.size());
        controllerData.setPlayerSlot(this.assignedControllers.size());

        if(this.assignedControllers.size() == 1) {
          this.savePlayerOneToConfig();
        }

        if(this.assignedControllers.size() < desiredControllerCount) {
          LOGGER.info(INPUT_MARKER, "Please press the buttons for player %d", this.assignedControllers.size() + 1);
        }

        break;
      }
    }

    if(this.assignedControllers.size() >= desiredControllerCount) {
      LOGGER.info(INPUT_MARKER, "Target controller count of %d reached", desiredControllerCount);
      this.assigningControllers = false;
      Input.refreshControllers();
    }

    if(this.idleControllers.isEmpty()) {
      LOGGER.info(INPUT_MARKER, "No more controllers detected. Continuing with the %d assigned controllers ", this.assignedControllers.size());
      this.assigningControllers = false;
      Input.refreshControllers();
    }

  }

  public boolean isAssigningControllers() {
    return this.assigningControllers;
  }

  public List<HardwareController> getAssignedControllers() {
    return this.assignedControllers;
  }

  public List<HardwareController> getConnectedControllers() {
    return this.connectedControllers;
  }

  public List<HardwareController> getIdleControllers() {
    return this.idleControllers;
  }

  private void savePlayerOneToConfig() {
    Config.controllerGuid(this.assignedControllers.get(0).getGlfwJoystickGuid());

    try {
      Config.save();
    } catch(final IOException e) {
      LOGGER.warn(INPUT_MARKER, "Failed to save config");
    }
  }
}
