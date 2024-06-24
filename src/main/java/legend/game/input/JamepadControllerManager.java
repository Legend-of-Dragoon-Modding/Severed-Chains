package legend.game.input;

import com.studiohartman.jamepad.Configuration;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JamepadControllerManager implements ControllerManager {
  private final com.studiohartman.jamepad.ControllerManager jamepad;

  private final Consumer<Controller> onConnect;
  private final Consumer<Controller> onDisconnect;

  private final List<Controller> connectedControllers = new ArrayList<>();

  public JamepadControllerManager(final String mappingsPath, final Consumer<Controller> onConnect, final Consumer<Controller> onDisconnect) {
    this.onConnect = onConnect;
    this.onDisconnect = onDisconnect;
    final Configuration jamepadConfig = new Configuration();
    jamepadConfig.maxNumControllers = 16;
    this.jamepad = new com.studiohartman.jamepad.ControllerManager(jamepadConfig, mappingsPath);
  }

  @Override
  public void init() {
    this.jamepad.initSDLGamepad();
    this.jamepad.update();

    for(int i = 0; i < this.jamepad.getNumControllers(); i++) {
      this.addController(i);
    }
  }

  @Override
  public void destroy() {
    this.jamepad.quitSDLGamepad();
  }

  @Override
  public void update() {
    if(this.jamepad.update()) {
      final List<Controller> newControllers = new ArrayList<>();

      for(int i = 0; i < this.jamepad.getNumControllers(); i++) {
        final ControllerIndex controller = this.jamepad.getControllerIndex(i);
        int newId;
        try {
          newId = controller.getDeviceInstanceID();
        } catch(final ControllerUnpluggedException e) {
          newId = -1;
        }

        Controller existingController = null;
        for(final Controller oldController : this.connectedControllers) {
          final JamepadController jamepadController = (JamepadController)oldController;
          if(jamepadController.isConnected() && jamepadController.getId() == newId) {
            existingController = oldController;
            break;
          }
        }

        if(existingController != null) {
          newControllers.add(existingController);
        } else {
          newControllers.add(this.addController(i));
        }
      }

      for(final Controller controller : this.connectedControllers) {
        if(!newControllers.contains(controller)) {
          this.onDisconnect.accept(controller);
        }
      }

      this.connectedControllers.clear();
      this.connectedControllers.addAll(newControllers);
    }
  }

  @Override
  public List<Controller> getConnectedControllers() {
    return this.connectedControllers;
  }

  @Override
  public Controller getControllerByGuid(final String guid) {
    for(final Controller controller : this.connectedControllers) {
      if(guid.equals(controller.getGuid())) {
        return controller;
      }
    }

    return null;
  }

  private Controller addController(final int index) {
    final Controller controller = new JamepadController(this.jamepad.getControllerIndex(index));

    this.connectedControllers.add(controller);
    this.onConnect.accept(controller);

    return controller;
  }
}
