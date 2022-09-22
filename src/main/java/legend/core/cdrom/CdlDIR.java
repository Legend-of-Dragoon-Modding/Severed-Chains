package legend.core.cdrom;

import legend.core.memory.Value;
import legend.core.memory.types.CString;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class CdlDIR implements MemoryRef {
  private final Value ref;

  public UnsignedIntRef id;
  public UnsignedIntRef parentId;
  public UnsignedIntRef lba;
  public CString name;

  public CdlDIR(final Value ref) {
    this.ref = ref;

    this.id = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.parentId = ref.offset(4, 0x4L).cast(UnsignedIntRef::new);
    this.lba = ref.offset(4, 0x8L).cast(UnsignedIntRef::new);
    this.name = ref.offset(0x20, 0xcL).cast(CString::new);
  }

  @Override
  public String toString() {
    return this.name.get() + " (id: " + this.id.get() + ", parent: " + this.parentId.get() + ", lba: " + this.lba.get() + ')';
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
