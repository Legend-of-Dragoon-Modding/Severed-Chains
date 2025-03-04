package legend.core.platform.input;

public class InputBinding<T extends InputActivation> {
  public final InputAction action;
  public final T activation;

  public InputBinding(final InputAction action, final T activation) {
    this.action = action;
    this.activation = activation;
  }
}
