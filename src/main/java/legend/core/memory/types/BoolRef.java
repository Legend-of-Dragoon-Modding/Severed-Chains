package legend.core.memory.types;

import legend.core.memory.Value;

public class BoolRef implements MemoryRef {
  private final Value ref;

  public BoolRef(final Value ref) {
    this.ref = ref;
  }

  public boolean get() {
    return this.ref.get() != 0;
  }

  public void set(final boolean val) {
    this.ref.setu(val ? 1 : 0);
  }

  public void set(final BoolRef val) {
    this.set(val.get());
  }

  public void not() {
    this.set(!this.get());
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  @Override
  public String toString() {
    return this.get() + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
