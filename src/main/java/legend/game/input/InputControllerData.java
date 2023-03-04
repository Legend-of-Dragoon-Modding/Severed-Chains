package legend.game.input;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickHats;

public class InputControllerData {

  private final String glfwJoystickName;
  private final String glfwJoystickGUID;
  private final int glfwControllerId;
  private FloatBuffer axis;
  private ByteBuffer hats;
  private ByteBuffer buttons;
  private int playerSlot = -1;

  public InputControllerData(final String glfwJoystickName, final String glfwJoystickGUID, final int glfwControllerId) {
    this.glfwJoystickName = glfwJoystickName;
    this.glfwJoystickGUID = glfwJoystickGUID;
    this.glfwControllerId = glfwControllerId;
  }

  public void updateState() {
    this.axis = glfwGetJoystickAxes(this.glfwControllerId);
    this.hats = glfwGetJoystickHats(this.glfwControllerId);
    this.buttons = glfwGetJoystickButtons(this.glfwControllerId);

  }



  public boolean hasAnyButtonActivity() {
    for(int i = 0; i < 10; i++) {
      if(this.checkButton(i)) {
        return true;
      }
    }
    return false;
  }

  public boolean checkButton(final int glfwCode) {
    if(this.buttons == null || this.buttons.remaining() == 0 || glfwCode == -1) {
      return false;
    }

    try {
      final byte button = this.buttons.get(glfwCode);
      return button != 0;
    } catch(final IndexOutOfBoundsException ex) {
      //LOGGER.error("Attempting to reach out of bounds with button glfwcode: " + glfwCode);
      return false;
    }

  }

  public float checkAxis(final int glfwCode) {
    if(this.axis == null || this.axis.remaining() == 0 || glfwCode == -1) {
      return 0;
    }
    try {
      return this.axis.get(glfwCode);
    } catch(final IndexOutOfBoundsException ex) {
      //LOGGER.error("Attempting to reach out of bounds with axis using " + glfwCode);
      return 0;
    }
  }

  public boolean checkHat(final int glfwCode, final int hatIndex) {
    if(this.hats == null || this.hats.remaining() == 0 || glfwCode == -1) {
      return false;
    }
    try {
      final int hat = this.hats.get(hatIndex);
      return (hat & glfwCode) != 0;
    } catch(final IndexOutOfBoundsException ex) {
      //LOGGER.error("Attempting to reach out of bounds with hat:" + hatIndex + " glfwcode:" + glfwCode);
      return false;
    }
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
