package legend.game.input;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickHats;

public class GlfwController extends Controller {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GlfwController.class);
  private static final Marker INPUT_MARKER = MarkerManager.getMarker("INPUT");
  private static final Marker CONTROLLER_VERBOSE_MARKER = MarkerManager.getMarker("CONTROLLER_VERBOSE");

  private final String glfwJoystickName;
  private final String glfwJoystickGuid;
  private final int glfwControllerId;
  private final ControllerIndex rumbleController;

  private FloatBuffer axis;
  private ByteBuffer hats;
  private ByteBuffer buttons;

  private int maxAxis;
  private int maxButtons;
  private int maxHats;

  public GlfwController(final String glfwJoystickName, final String glfwJoystickGuid, final int glfwControllerId, final ControllerIndex rumbleController) {
    this.glfwJoystickName = glfwJoystickName;
    this.glfwJoystickGuid = glfwJoystickGuid;
    this.glfwControllerId = glfwControllerId;
    this.rumbleController = rumbleController;

    this.probeInputs();

    this.bindings.addAll(ControllerDatabase.createBindings(this.getGuid(), this));
  }

  public String getName() {
    return this.glfwJoystickName;
  }

  @Override
  public String getGuid() {
    return this.glfwJoystickGuid;
  }

  public int getId() {
    return this.glfwControllerId;
  }

  @Override
  public void poll() {
    if(this.glfwControllerId != -1) {
      this.axis = glfwGetJoystickAxes(this.glfwControllerId);
      this.hats = glfwGetJoystickHats(this.glfwControllerId);
      this.buttons = glfwGetJoystickButtons(this.glfwControllerId);
    }

    for(final InputBinding binding : this.bindings) {
      binding.poll();
    }
  }

  @Override
  public boolean readButton(final int glfwCode) {
    if(this.buttons == null || this.buttons.remaining() == 0 || glfwCode == -1 || glfwCode >= this.maxButtons) {
      return false;
    }

    try {
      final byte button = this.buttons.get(glfwCode);
      return button != 0;
    } catch(final IndexOutOfBoundsException ex) {
      LOGGER.error(INPUT_MARKER, "Attempting to reach out of bounds with button using glfwcode %d", glfwCode);
      return false;
    }

  }

  @Override
  public float readAxis(final int glfwCode) {
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

  @Override
  public boolean readHat(final int glfwCode, final int hatIndex) {
    if(this.hats == null || this.hats.remaining() == 0 || glfwCode == -1 || hatIndex >= this.maxHats) {
      return false;
    }

    try {
      final int hat = this.hats.get(hatIndex);
      return (hat & glfwCode) != 0;
    } catch(final IndexOutOfBoundsException ex) {
      LOGGER.error(INPUT_MARKER, "Attempting to reach out of bounds with hat index %d glfwcode %d", hatIndex, glfwCode);
      return false;
    }
  }

  private void probeInputs() {
    this.poll();

    this.probeButtons();
    this.probeAxes();
    this.probeHats();
  }

  private void probeButtons() {
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

  private void probeAxes() {
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

  private void probeHats() {
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

  @Override
  public void rumble(final int ms) {
    if(this.rumbleController != null) {
      try {
        this.rumbleController.doVibration(0.5f, 0.5f, ms);
      } catch(final ControllerUnpluggedException ignored) { }
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if(obj instanceof final GlfwController other) {
      return this.glfwJoystickGuid.equals(other.glfwJoystickGuid) || this.glfwControllerId == other.glfwControllerId;
    }

    return super.equals(obj);
  }
}
