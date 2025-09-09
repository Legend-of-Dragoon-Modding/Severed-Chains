package legend.core.platform.input;

import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.function.Supplier;

public class InputAction extends RegistryEntry {
  public final boolean canBeEdited;
  public final boolean visible;
  public final boolean useMovementDeadzone;

  private InputAction(final boolean canBeEdited, final boolean visible, final boolean useMovementDeadzone) {
    this.canBeEdited = canBeEdited;
    this.visible = visible;
    this.useMovementDeadzone = useMovementDeadzone;
  }

  public static InputAction editable() {
    return new InputAction(true, true, false);
  }

  public static InputAction fixed() {
    return new InputAction(false, true, false);
  }

  public static InputAction hidden() {
    return new InputAction(false, false, false);
  }

  public static Builder make() {
    return new Builder();
  }

  public static class Builder {
    private boolean canBeEdited;
    private boolean visible;
    private boolean useMovementDeadzone;

    private Builder() { }

    public Builder editable() {
      this.canBeEdited = true;
      return this;
    }

    public Builder visible() {
      this.visible = true;
      this.canBeEdited = true;
      return this;
    }

    public Builder useMovementDeadzone() {
      this.useMovementDeadzone = true;
      return this;
    }

    public Supplier<InputAction> build() {
      return () -> new InputAction(this.canBeEdited, this.visible, this.useMovementDeadzone);
    }
  }
}
