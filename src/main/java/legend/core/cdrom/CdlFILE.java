package legend.core.cdrom;

import legend.core.memory.Value;
import legend.core.memory.types.FixedString;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

import javax.annotation.Nullable;

public class CdlFILE implements MemoryRef {
  @Nullable
  private final Value ref;

  public final legend.core.cdrom.CdlLOC pos;
  public final IntRef size;
  public final FixedString name;

  public CdlFILE() {
    this.ref = null;

    this.pos = new legend.core.cdrom.CdlLOC();
    this.size = new IntRef();
    this.name = new FixedString(16);
  }

  public CdlFILE(final Value ref) {
    this.ref = ref;

    this.pos = new CdlLOC(ref.offset(4, 0x0L));
    this.size = ref.offset(4, 0x4L).cast(IntRef::new);
    this.name = ref.offset(16, 0x8L).cast(FixedString::new);
  }

  public void set(final CdlFILE other) {
    this.pos.set(other.pos);
    this.size.set(other.size);
    this.name.set(other.name);
  }

  @Override
  public String toString() {
    return this.name + " (size: " + this.size.get() + ", pos: " + this.pos + ')';
  }

  @Override
  public long getAddress() {
    if(this.ref == null) {
      return 0;
    }

    return this.ref.getAddress();
  }
}
