package legend.game.scripting;

import org.legendofdragoon.modloader.registries.RegistryId;

public class ScriptStateNullRegistryIdParam extends Param {
  @Override
  public int get() {
    throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used as registry ID");
  }

  @Override
  public Param set(final int val) {
    throw new IllegalStateException(this.getClass().getSimpleName() + " can not be set");
  }

  @Override
  public Param array(final int index) {
    throw new IllegalStateException(this.getClass().getSimpleName() + " can not be used as array");
  }

  @Override
  public boolean isRegistryId() {
    return true;
  }

  @Override
  public RegistryId getRegistryId() {
    return null;
  }

  @Override
  public Param set(final RegistryId id) {
    throw new IllegalStateException(this.getClass().getSimpleName() + " can not be set");
  }

  @Override
  public String toString() {
    return "null";
  }
}
