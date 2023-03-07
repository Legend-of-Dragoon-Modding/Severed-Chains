package legend.game.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickHats;

public class InputControllerData {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker INPUT_MARKER = MarkerManager.getMarker("INPUT");
  private static final Marker CONTROLLER_VERBOSE_MARKER = MarkerManager.getMarker("CONTROLLER_VERBOSE");
  private final String glfwJoystickName;
  private final String glfwJoystickGUID;
  private final int glfwControllerId;
  private FloatBuffer axis;
  private ByteBuffer hats;
  private ByteBuffer buttons;
  private int playerSlot = -1;

  private int maxAxis;
  private int maxButtons;
  private int maxHats;

  public InputControllerData(final String glfwJoystickName, final String glfwJoystickGUID, final int glfwControllerId) {
    this.glfwJoystickName = glfwJoystickName;
    this.glfwJoystickGUID = glfwJoystickGUID;
    this.glfwControllerId = glfwControllerId;

    this.pollControllerForAvailableInputs();
  }

  public void updateState() {
    if(this.glfwControllerId == -1) {
      return;
    }

    this.axis = glfwGetJoystickAxes(this.glfwControllerId);
    this.hats = glfwGetJoystickHats(this.glfwControllerId);
    this.buttons = glfwGetJoystickButtons(this.glfwControllerId);
  }



  public boolean hasAnyButtonActivity() {
    for(int i = 0; i < this.maxButtons; i++) {
      if(this.checkButton(i)) {
        return true;
      }
    }
    return false;
  }

  public boolean checkButton(final int glfwCode) {
    if(this.buttons == null || this.buttons.remaining() == 0 || glfwCode == -1  || glfwCode >= this.maxButtons) {
      return false;
    }

    try {
      final byte button = this.buttons.get(glfwCode);
      return button != 0;
    } catch(final IndexOutOfBoundsException ex) {
      LOGGER.error(INPUT_MARKER,"Attempting to reach out of bounds with button using glfwcode %d",glfwCode);
      return false;
    }

  }

  public float checkAxis(final int glfwCode) {
    if(this.axis == null || this.axis.remaining() == 0 || glfwCode == -1 || glfwCode >= this.maxAxis) {
      return 0;
    }
    try {
      return this.axis.get(glfwCode);
    } catch(final IndexOutOfBoundsException ex) {
      LOGGER.error(INPUT_MARKER, "Attempting to reach out of bounds with axis using glfwcode %d", glfwCode);
      return 0;
    }
  }

  public boolean checkHat(final int glfwCode, final int hatIndex) {
    if(this.hats == null || this.hats.remaining() == 0 || glfwCode == -1 || hatIndex >= this.maxHats) {
      return false;
    }
    try {
      final int hat = this.hats.get(hatIndex);
      return (hat & glfwCode) != 0;
    } catch(final IndexOutOfBoundsException ex) {
      LOGGER.error(INPUT_MARKER,"Attempting to reach out of bounds with hat index %d glfwcode %d",hatIndex,glfwCode);
      return false;
    }
  }

  private void pollControllerForAvailableInputs() {
    this.updateState();

    this.pollControllerForButtonHardware();
    this.pollControllerForAxisHardware();
    this.pollControllerForHatHardware();
  }

  private void pollControllerForButtonHardware() {
    this.maxButtons = 0;
    if(this.buttons == null) {
      return;
    }
    for(int i = 0; i < 100; i++) {
      try {
        this.buttons.get(i);
        this.maxButtons++;
        LOGGER.info(CONTROLLER_VERBOSE_MARKER, "Found button on controller Index: %d", i);
      } catch(final IndexOutOfBoundsException ignored) {
      }
    }


    LOGGER.info(INPUT_MARKER, "Max Buttons %d", this.maxButtons);
  }

  private void pollControllerForAxisHardware() {
    this.maxAxis = 0;

    if(this.axis == null) {
      return;
    }

    for(int i = 0; i < 100; i++) {
      try {
        this.axis.get(i);
        this.maxAxis++;
        LOGGER.info(CONTROLLER_VERBOSE_MARKER, "Found axis on controller Index %d", i);
      } catch(final IndexOutOfBoundsException ignored) {
      }
    }

    LOGGER.info(INPUT_MARKER, "Max Axis: %d", this.maxAxis);
  }

  private void pollControllerForHatHardware() {
    this.maxHats = 0;

    if(this.hats == null) {
      return;
    }
    for(int i = 0; i < 100; i++) {
      try {
        this.hats.get(i);
        this.maxHats++;
        LOGGER.info(CONTROLLER_VERBOSE_MARKER, "Found axis on controller Index %d", i);
      } catch(final IndexOutOfBoundsException ignored) {
      }
    }

    LOGGER.info(INPUT_MARKER, "Max Hats: %d", this.maxHats);
  }

  public String getGlfwJoystickName() {
    return this.glfwJoystickName;
  }

  public String getGlfwJoystickGUID() {
    return this.glfwJoystickGUID;
  }

  public int getGlfwControllerId() {
    return this.glfwControllerId;
  }

  public int getPlayerSlot() {
    return this.playerSlot;
  }

  public void setPlayerSlot(final int playerSlot) {
    this.playerSlot = playerSlot;
  }

  public String getInfoString() {
    return "id: " + this.glfwControllerId + ' ' + this.glfwJoystickName + " Guid: " + this.glfwJoystickGUID;
  }

}
