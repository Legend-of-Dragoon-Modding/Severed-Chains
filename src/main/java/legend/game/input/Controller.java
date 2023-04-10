package legend.game.input;

import java.util.ArrayList;
import java.util.List;

public class Controller {
  public final List<InputBinding> bindings = new ArrayList<>();
  private HardwareController hardwareController;
  private boolean anyActivityThisFrame;

  public void poll() {
    this.anyActivityThisFrame = false;

    for(final InputBinding binding : this.bindings) {
      binding.poll();

      if(binding.getState() == InputBindingState.PRESSED_THIS_FRAME) {
        this.anyActivityThisFrame = true;
      }
    }
  }

  public boolean hasActivityThisFrame() {
    return this.anyActivityThisFrame;
  }

  public String getGuid() {
    return this.hardwareController.getGlfwJoystickGuid();
  }

  public int getId() {
    return this.hardwareController.getGlfwControllerId();
  }

  public void setHardwareController(final HardwareController hardwareController) {
    this.hardwareController = hardwareController;
    this.bindings.clear();

    if(hardwareController.getGlfwControllerId() == -1) {
      return;
    }

    this.bindings.addAll(ControllerDatabase.createBindings(hardwareController.getGlfwJoystickGuid()));

    for(final InputBinding binding : this.bindings) {
      binding.setTargetController(hardwareController);
    }
  }

  public void addBinding(final InputBinding binding) {
    this.bindings.add(binding);
  }

  public void removeBinding(final InputBinding binding) {
    this.bindings.remove(binding);
  }

  public void removeBinding(final InputAction inputAction, final InputType inputType) {
    for(final InputBinding binding : this.bindings) {
      if(binding.getInputAction() == inputAction && binding.getInputType() == inputType) {
        this.bindings.remove(binding);
        break;
      }
    }
  }
}
