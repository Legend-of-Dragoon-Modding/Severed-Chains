package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class NewRootStruct implements MemoryRef {
  private final Value ref;

  public final ArrayRef<NewRootEntryStruct> entries_0000;
  public final ArrayRef<UnsignedIntRef> uia_2000;

  public NewRootStruct(final Value ref) {
    this.ref = ref;

    this.entries_0000 = ref.offset(4, 0x0000L).cast(ArrayRef.of(NewRootEntryStruct.class, 0x400, 8, NewRootEntryStruct::new));
    this.uia_2000 = ref.offset(4, 0x2000L).cast(ArrayRef.of(UnsignedIntRef.class, 0x63c, 4, UnsignedIntRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
