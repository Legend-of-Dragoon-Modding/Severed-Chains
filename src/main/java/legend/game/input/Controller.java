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

  public abstract String getGuid();

  public void rumble(final int ms) { }
}
