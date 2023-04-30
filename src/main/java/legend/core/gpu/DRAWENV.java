package legend.core.gpu;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class DRAWENV implements MemoryRef {
  private final Value ref;

  /**
   * 0x0 Drawing area<br>
   * 0x0 x<br>
   * 0x2 y<br>
   * 0x4 w<br>
   * 0x6 h
   */
  public final RECT clip;
  /**
   * 0x8 Drawing offset<br>
   * 0x8 offset 1<br>
   * 0xa offset 2<br>
   */
  public final ArrayRef<ShortRef> ofs;

  public DRAWENV(final Value ref) {
    this.ref = ref;

    this.clip = new RECT(ref.offset(2, 0x0L));
    this.ofs = ref.offset(2, 0x8L).cast(ArrayRef.of(ShortRef.class, 2, 2, ShortRef::new));
  }

  public DRAWENV set(final DRAWENV other) {
    this.clip.set(other.clip);
    this.ofs.get(0).set(other.ofs.get(0));
    this.ofs.get(1).set(other.ofs.get(1));
    return this;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
