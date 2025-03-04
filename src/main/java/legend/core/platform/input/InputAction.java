package legend.core.platform.input;

import org.legendofdragoon.modloader.registries.RegistryEntry;

public class InputAction extends RegistryEntry {
  public final boolean canBeEdited;
  public final boolean visible;

  private InputAction(final boolean canBeEdited, final boolean visible) {
    this.canBeEdited = canBeEdited;
    this.visible = visible;
  }

  public static InputAction editable() {
    return new InputAction(true, true);
  }

  public static InputAction fixed() {
    return new InputAction(false, true);
  }

  public static InputAction hidden() {
    return new InputAction(false, false);
  }
}
