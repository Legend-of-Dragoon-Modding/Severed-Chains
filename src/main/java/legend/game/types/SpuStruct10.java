package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;

public class SpuStruct10 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef _00;

  public final Pointer<MrgFile> mrg_04;
  public final Pointer<SssqFile> sssq_08;
  public final ShortRef channelIndex_0c;

  public SpuStruct10(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);

    this.mrg_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, MrgFile::new));
    this.sssq_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, SssqFile::new));
    this.channelIndex_0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
