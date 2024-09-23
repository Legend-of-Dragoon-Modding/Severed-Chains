package legend.game.scripting;

import org.legendofdragoon.modloader.registries.RegistryId;

public class ScriptStateRegistryIdParam extends Param {
  private final ScriptState<?> state;
  private final int index;

  public ScriptStateRegistryIdParam(final ScriptState<?> state, final int index) {
    this.state = state;
    this.index = index;
  }

  @Override
  public int get() {
    throw new IllegalStateException(this.getClass().getSimpleName() + " can only store registry IDs");
  }

  @Override
  public Param set(final int val) {
    if(val == 0) {
      return this.set((RegistryId)null);
    }

    throw new IllegalStateException(this.getClass().getSimpleName() + " can only store registry IDs");
  }

  @Override
  public Param array(final int index) {
    throw new IllegalStateException(this.getClass().getSimpleName() + " can only store registry IDs");
  }

  @Override
  public boolean isRegistryId() {
    return true;
  }

  @Override
  public Param set(final Param other) {
    if(other.isRegistryId()) {
      return this.set(other.getRegistryId());
    }

    return super.set(other);
  }

  @Override
  public RegistryId getRegistryId() {
    return this.state.registryIds[this.index];
  }

  @Override
  public Param set(final RegistryId id) {
    this.state.registryIds[this.index] = id;
    return this;
  }

  @Override
  public String toString() {
    return "script[%d].reg[%d] %s".formatted(this.state.index, this.index, this.getRegistryId());
  }
}
