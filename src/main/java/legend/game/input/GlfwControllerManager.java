package legend.game.input;

import legend.core.opengl.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static legend.core.GameEngine.RENDERER;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

public class GlfwControllerManager implements ControllerManager {
  private final Consumer<Controller> onConnect;
  private final Consumer<Controller> onDisconnect;

  private final List<Controller> connectedControllers = new ArrayList<>();

  public GlfwControllerManager(final Consumer<Controller> onConnect, final Consumer<Controller> onDisconnect) {
    this.onConnect = onConnect;
    this.onDisconnect = onDisconnect;

    ControllerDatabase.loadControllerDb();

    RENDERER.events().onControllerConnected(this::onControllerConnected);
    RENDERER.events().onControllerDisconnected(this::onControllerDisconnected);
  }

  @Override
  public void init() {
    for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
      if(glfwJoystickPresent(i)) {
        this.addController(i);
      }
    }
  }

  @Override
  public void destroy() {

  }

  @Override
  public void update() {

  }

  private Controller addController(final int index) {
    final String controllerGuid = glfwGetJoystickGUID(index);
    final Controller controller = new GlfwController(glfwGetJoystickName(index), controllerGuid, index);

    this.connectedControllers.add(controller);
    this.onConnect.accept(controller);

    return controller;
  }

  @Override
  public List<Controller> getConnectedControllers() {
    return this.connectedControllers;
  }

  @Override
  public Controller getControllerByGuid(final String guid) {
    for(final Controller controller : this.connectedControllers) {
      if(controller.getGuid().equals(guid)) {
        return controller;
      }
    }

    return null;
  }

  private void onControllerConnected(final Window window, final int id) {
    for(final Controller controller : this.connectedControllers) {
      if(controller.getGuid().equals(glfwGetJoystickGUID(id))) {
        this.onConnect.accept(controller);
        break;
      }
    }

    this.addController(id);
  }

  private void onControllerDisconnected(final Window window, final int id) {
    for(final Controller controller : this.connectedControllers) {
      if(((GlfwController)controller).getGlfwControllerId() == id) {
        this.connectedControllers.remove(controller);
        this.onDisconnect.accept(controller);
        break;
      }
    }
  }
}
