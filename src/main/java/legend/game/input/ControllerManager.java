package legend.game.input;

import legend.core.opengl.Window;
import legend.game.modding.coremod.CoreMod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.RENDERER;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

public class ControllerManager {
  private final com.studiohartman.jamepad.ControllerManager jamepad;

  private final Consumer<Controller> onConnect;
  private final Consumer<Controller> onDisconnect;

  private final List<Controller> connectedControllers = new ArrayList<>();

  public ControllerManager(final com.studiohartman.jamepad.ControllerManager jamepad, final Consumer<Controller> onConnect, final Consumer<Controller> onDisconnect) {
    this.jamepad = jamepad;
    this.onConnect = onConnect;
    this.onDisconnect = onDisconnect;

    ControllerDatabase.loadControllerDb();

    RENDERER.events().onControllerConnected(this::onControllerConnected);
    RENDERER.events().onControllerDisconnected(this::onControllerDisconnected);
  }

  public void init() {
    for(int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
      if(glfwJoystickPresent(i)) {
        this.addController(i);
      }
    }
  }

  private Controller addController(final int index) {
    final String controllerGuid = glfwGetJoystickGUID(index);
    final Controller controller = new GlfwController(glfwGetJoystickName(index), controllerGuid, index, this.jamepad.getControllerIndex(index));

    this.connectedControllers.add(controller);

    this.onConnect.accept(controller);

    final String controllerFromConfig = CONFIG.getConfig(CoreMod.CONTROLLER_CONFIG.get());

    if(controllerFromConfig.isBlank() || controllerFromConfig.equals(controllerGuid)) {
      Input.useController(controller);
      CONFIG.setConfig(CoreMod.CONTROLLER_CONFIG.get(), controller.getGuid());
    }

    return controller;
  }

  public List<Controller> getConnectedControllers() {
    return this.connectedControllers;
  }

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

    final Controller controller = this.addController(id);
    this.onConnect.accept(controller);
  }

  private void onControllerDisconnected(final Window window, final int id) {
    for(final Controller controller : this.connectedControllers) {
      if(controller.getGuid().equals(glfwGetJoystickGUID(id))) {
        this.connectedControllers.remove(controller);
        this.onDisconnect.accept(controller);
        break;
      }
    }
  }
}
