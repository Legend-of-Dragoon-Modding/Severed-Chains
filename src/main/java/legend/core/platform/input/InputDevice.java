package legend.core.platform.input;

public abstract class InputDevice {
  public final InputClass classifier;

  protected InputDevice(final InputClass classifier) {
    this.classifier = classifier;
  }
}
