package legend.game.input;

import java.util.ArrayList;
import java.util.List;

public abstract class Controller {
  public final List<InputBinding> bindings = new ArrayList<>();

  public abstract void poll();

  public abstract boolean readButton(final int input);
  public abstract float readAxis(final int input);
  public abstract boolean readHat(final int input, final int hatIndex);

  public void addBinding(final InputBinding binding) {
    this.bindings.add(binding);
  }

  public abstract String getName();
  public abstract String getGuid();

  public void rumble(final float bigIntensity, final float smallIntensity, final int ms) { }
  public void adjustRumble(final float bigIntensity, final float smallIntensity, final int ms) { }

  public void rumble(final float intensity, final int ms) {
    this.rumble(intensity, intensity, ms);
  }

  public void adjustRumble(final float intensity, final int ms) {
    this.adjustRumble(intensity, intensity, ms);
  }

  public void stopRumble() {
    this.rumble(0.0f, 0);
  }
}
