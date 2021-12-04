package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class SpuStruct1c implements MemoryRef {
  private final Value ref;

  public final BoolRef used_00;
  public final UnsignedShortRef _02;
  public final Pointer<MrgFile> soundMrgPtr_04;
  public final UnsignedIntRef ptr_08;
  public final UnsignedIntRef ptr_0c;
  public final ShortRef playableSoundIndex_10;

  public final UnsignedIntRef spuRamOffset_14;
  public final UnsignedByteRef _18;

  public SpuStruct1c(final Value ref) {
    this.ref = ref;

    this.used_00 = ref.offset(2, 0x00L).cast(BoolRef::new);
    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.soundMrgPtr_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, MrgFile::new));
    this.ptr_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.ptr_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.playableSoundIndex_10 = ref.offset(2, 0x10L).cast(ShortRef::new);

    this.spuRamOffset_14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this._18 = ref.offset(1, 0x18L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
