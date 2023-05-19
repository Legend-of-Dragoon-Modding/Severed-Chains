package legend.game.input;

import legend.core.Config;
import legend.core.opengl.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static legend.core.GameEngine.GPU;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

public class ControllerManager {
  private final Consumer<GlfwController> onConnect;
  private final Consumer<GlfwController> onDisconnect;

  private final List<GlfwController> connectedControllers = new ArrayList<>();

  public ControllerManager(final Consumer<GlfwController> onConnect, final Consumer<GlfwController> onDisconnect) {
    this.onConnect = onConnect;
    this.onDisconnect = onDisconnect;

    ControllerDatabase.loadControllerDb();

    GPU.window().events.onControllerConnected(this::onControllerConnected);
    GPU.window().events.onControllerDisconnected(this::onControllerDisconnected);
  }

  public void init() {
    final String controllerGuidFromConfig = Config.controllerGuid();

    for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
      if(glfwJoystickPresent(i)) {
        final String controllerGuid = glfwGetJoystickGUID(i);
        final GlfwController controller = new GlfwController(glfwGetJoystickName(i), controllerGuid, i);

        this.connectedControllers.add(controller);

        this.onConnect.accept(controller);

        if(controllerGuidFromConfig.isBlank() || controllerGuidFromConfig.equals(controllerGuid)) {
          Input.useController(controller);
        }
      }
    }
  }

  public List<GlfwController> getConnectedControllers() {
    return this.connectedControllers;
  }

  private void onControllerConnected(final Window window, final int id) {
    for(final GlfwController controller : this.connectedControllers) {
      if(controller.getGuid().equals(glfwGetJoystickGUID(id))) {
        this.onConnect.accept(controller);
      }
    }
  }

  private void onControllerDisconnected(final Window window, final int id) {
    for(final GlfwController controller : this.connectedControllers) {
      if(controller.getId() == id) {
        this.onDisconnect.accept(controller);
      }
    }
  }
}
