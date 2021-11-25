package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class SssqFile implements MemoryRef {
  public static final long MAGIC = 0x7173_5353L; //SSsq

  private final Value ref;

  public final UnsignedByteRef _00;

  public final UnsignedShortRef _02;
  public final UnsignedShortRef _04;

  public final UnsignedIntRef magic_0c;
  public final ArrayRef<SssqEntry> entries_10;
  public final UnboundedArrayRef<UnsignedByteRef> data_110;

  public SssqFile(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);

    this.magic_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.entries_10 = ref.offset(4, 0x10L).cast(ArrayRef.of(SssqEntry.class, 0x10, 0x10, SssqEntry::new));
    this.data_110 = ref.offset(1, 0x110L).cast(UnboundedArrayRef.of(1, UnsignedByteRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
