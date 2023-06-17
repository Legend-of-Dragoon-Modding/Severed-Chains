package legend.game.wmap;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;
import legend.game.types.LodString;

public class Place0c implements MemoryRef {
  private final Value ref;

  public final Pointer<LodString> name_00;
  public final UnsignedByteRef fileIndex_04;
  /** Things a place offers like inn, shops, etc. */
  public final UnsignedByteRef services_05;
  public final ArrayRef<ByteRef> soundIndices_06;

  public Place0c(final Value ref) {
    this.ref = ref;

    this.name_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, LodString::new));
    this.fileIndex_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.services_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.soundIndices_06 = ref.offset(1, 0x06L).cast(ArrayRef.of(ByteRef.class, 4, 1, ByteRef::new));
  }

  @Override
  public String toString() {
    if(this.name_00.isNull()) {
      return "Place (Empty)";
    }

    return "Place (%s)".formatted(this.name_00.deref().get());
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
