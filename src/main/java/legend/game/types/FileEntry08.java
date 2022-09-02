package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.CString;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;

public class FileEntry08 implements MemoryRef {
  private final Value ref;

  public final ShortRef fileIndex_00;
  public final ShortRef _02;
  public final Pointer<CString> name_04;

  public FileEntry08(final Value ref) {
    this.ref = ref;

    this.fileIndex_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.name_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, CString.maxLength(20)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
