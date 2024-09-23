package legend.game.scripting;

import org.legendofdragoon.modloader.registries.RegistryId;

public class ScriptStateVarRegistryIdParam extends Param {
  private final ScriptState<?> state;
  private final int storIndex;

  public ScriptStateVarRegistryIdParam(final ScriptState<?> state, final int storIndex) {
    this.state = state;
    this.storIndex = storIndex;
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
    return this.state.registryIds[this.state.storage_44[this.storIndex]];
  }

  @Override
  public Param set(final RegistryId id) {
    this.state.registryIds[this.state.storage_44[this.storIndex]] = id;
    return this;
  }

  @Override
  public String toString() {
    return "script[%1$d].reg[script[%1$d].stor[%2$d]] %3$s".formatted(this.state.index, this.storIndex, this.getRegistryId());
  }
}
