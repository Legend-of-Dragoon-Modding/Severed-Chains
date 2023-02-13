package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

/** 0x30 bytes long */
public class SmallerStruct implements MemoryRef {
  private final Value ref;

  public final Pointer<CContainerSubfile1> tmdExt_00;
  public final ArrayRef<UnsignedByteRef> uba_04;
  public final ArrayRef<ShortRef> sa_08;
  public final ArrayRef<ShortRef> sa_10;
  public final ArrayRef<ShortRef> sa_18;
  public final ArrayRef<Pointer<TmdSubExtension>> tmdSubExtensionArr_20;

  public SmallerStruct(final Value ref) {
    this.ref = ref;

    this.tmdExt_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, CContainerSubfile1::new));
    this.uba_04 = ref.offset(1, 0x04L).cast(ArrayRef.of(UnsignedByteRef.class, 4, 1, UnsignedByteRef::new));
    this.sa_08 = ref.offset(2, 0x08L).cast(ArrayRef.of(ShortRef.class, 4, 2, ShortRef::new));
    this.sa_10 = ref.offset(2, 0x10L).cast(ArrayRef.of(ShortRef.class, 4, 2, ShortRef::new));
    this.sa_18 = ref.offset(2, 0x18L).cast(ArrayRef.of(ShortRef.class, 4, 2, ShortRef::new));
    this.tmdSubExtensionArr_20 = ref.offset(4, 0x20L).cast(ArrayRef.of(Pointer.classFor(TmdSubExtension.class), 4, 4, Pointer.deferred(4, TmdSubExtension::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
