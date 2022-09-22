package legend.core.memory.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

/**
 * Nothing to see here (literally)
 */
public class VoidRef implements MemoryRef {
  private final Value ref;

  public VoidRef(final Value ref) {
    this.ref = ref;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
