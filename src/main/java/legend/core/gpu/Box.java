package legend.core.gpu;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

import javax.annotation.Nullable;

public class Box implements MemoryRef {
  @Nullable
  private final Value ref;

  public final ShortRef x1;
  public final ShortRef x2;
  public final ShortRef y1;
  public final ShortRef y2;

  public Box() {
    this.ref = null;
    this.x1 = new ShortRef();
    this.x2 = new ShortRef();
    this.y1 = new ShortRef();
    this.y2 = new ShortRef();
  }

  public Box(final short x1, final short x2, final short y1, final short y2) {
    this();
    this.set(x1, x2, y1, y2);
  }

  public Box(final Value ref) {
    this.ref = ref;
    this.x1 = new ShortRef(ref.offset(2, 0x0L));
    this.x2 = new ShortRef(ref.offset(2, 0x2L));
    this.y1 = new ShortRef(ref.offset(2, 0x4L));
    this.y2 = new ShortRef(ref.offset(2, 0x6L));
  }

  public Box(final Value ref, final short x1, final short x2, final short y1, final short y2) {
    this(ref);
    this.set(x1, x2, y1, y2);
  }

  public Box set(final short x1, final short x2, final short y1, final short y2) {
    this.x1.set(x1);
    this.x2.set(x2);
    this.y1.set(y1);
    this.y2.set(y2);
    return this;
  }

  public Box clear() {
    return this.set((short)0, (short)0, (short)0, (short)0);
  }

  @Override
  public long getAddress() {
    if(this.ref != null) {
      return this.ref.getAddress();
    }

    return 0;
  }
}
