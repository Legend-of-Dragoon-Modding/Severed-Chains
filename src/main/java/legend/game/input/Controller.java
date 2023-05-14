package legend.game.input;

import java.util.ArrayList;
import java.util.List;

public abstract class Controller {
  public final List<InputBinding> bindings = new ArrayList<>();

  public abstract void poll();

  public abstract boolean readButton(final int glfwCode);
  public abstract float readAxis(final int glfwCode);
  public abstract boolean readHat(final int glfwCode, final int hatIndex);

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
