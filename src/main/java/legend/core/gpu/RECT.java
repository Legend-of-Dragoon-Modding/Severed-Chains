package legend.core.gpu;

import legend.core.memory.types.ShortRef;

public class RECT {
  public final ShortRef x;
  public final ShortRef y;
  public final ShortRef w;
  public final ShortRef h;

  public RECT() {
    this.x = new ShortRef();
    this.y = new ShortRef();
    this.w = new ShortRef();
    this.h = new ShortRef();
  }

  public RECT(final short x, final short y, final short w, final short h) {
    this();
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

  public boolean contains(final int x, final int y) {
    return x >= this.x.get() && x < this.x.get() + this.w.get() && y >= this.y.get() && y < this.y.get() + this.h.get();
  }

  @Override
  public String toString() {
    return "RECT {" + this.x.get() + ", " + this.y.get() + ", " + this.w.get() + ", " + this.h.get() + '}';
  }
}
