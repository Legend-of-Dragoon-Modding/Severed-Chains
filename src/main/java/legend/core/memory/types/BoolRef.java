package legend.core.memory.types;

import legend.core.memory.Value;

import javax.annotation.Nullable;

public class BoolRef implements MemoryRef {
  @Nullable
  private final Value ref;

  private boolean val;

  public BoolRef() {
    this.ref = null;
  }

  public BoolRef(final Value ref) {
    this.ref = ref;
  }

  public boolean get() {
    if(this.ref == null) {
      return this.val;
    }

    return this.ref.get() != 0;
  }

  public void set(final boolean val) {
    if(this.ref == null) {
      this.val = val;
      return;
    }

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
    if(this.ref == null) {
      return 0;
    }

    return this.ref.getAddress();
  }

  @Override
  public String toString() {
    return this.get() + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
