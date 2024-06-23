package legend.game.input;

import java.util.List;

public interface ControllerManager {
  void init();
  void destroy();
  void update();
  List<Controller> getConnectedControllers();
  Controller getControllerByGuid(String guid);
}
