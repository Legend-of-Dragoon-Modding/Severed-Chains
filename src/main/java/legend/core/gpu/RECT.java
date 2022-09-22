package legend.core.gpu;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

import javax.annotation.Nullable;

public class RECT implements MemoryRef {
  @Nullable
  private final Value ref;

  public final ShortRef x;
  public final ShortRef y;
  public final ShortRef w;
  public final ShortRef h;

  public RECT() {
    this.ref = null;
    this.x = new ShortRef();
    this.y = new ShortRef();
    this.w = new ShortRef();
    this.h = new ShortRef();
  }

  public RECT(final short x, final short y, final short w, final short h) {
    this();
    this.set(x, y, w, h);
  }

  public RECT(final Value ref) {
    this.ref = ref;
    this.x = new ShortRef(ref.offset(2, 0x0L));
    this.y = new ShortRef(ref.offset(2, 0x2L));
    this.w = new ShortRef(ref.offset(2, 0x4L));
    this.h = new ShortRef(ref.offset(2, 0x6L));
  }

  public RECT(final Value ref, final short x, final short y, final short w, final short h) {
    this(ref);
    this.set(x, y, w, h);
  }

  public RECT set(final short x, final short y, final short w, final short h) {
    this.x.set(x);
    this.y.set(y);
    this.w.set(w);
    this.h.set(h);
    return this;
  }

  public RECT set(final RECT other) {
    return this.set(other.x.get(), other.y.get(), other.w.get(), other.h.get());
  }

  public RECT clear() {
    return this.set((short)0, (short)0, (short)0, (short)0);
  }

  @Override
  public String toString() {
    return "RECT {" + this.x.get() + ", " + this.y.get() + ", " + this.w.get() + ", " + this.h.get() + '}' + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }

  @Override
  public long getAddress() {
    if(this.ref != null) {
      return this.ref.getAddress();
    }

    return 0;
  }
}
