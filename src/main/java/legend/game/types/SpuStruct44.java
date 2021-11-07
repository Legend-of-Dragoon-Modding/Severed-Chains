package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class SpuStruct44 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;

  public final UnsignedByteRef _03;
  public final UnsignedByteRef _04;

  public final Pointer<SshdFile> sshdPtr_08;

  public final UnsignedByteRef _0c;
  public final UnsignedByteRef _0d;

  public final ShortRef _10;
  public final UnsignedShortRef _12;
  public final UnsignedShortRef _14;
  public final UnsignedShortRef _16;
  public final UnsignedShortRef _18;
  public final UnsignedIntRef eventSpuIrq_1c;
  public final BoolRef spuDmaTransferInProgress_20;
  public final UnsignedByteRef _22;
  public final UnsignedByteRef _23;
  public final UnsignedShortRef _24;
  public final UnsignedShortRef _26;
  public final UnsignedShortRef _28;
  public final UnsignedByteRef _2a;
  public final UnsignedByteRef _2b;
  public final UnsignedShortRef _2c;
  public final UnsignedShortRef _2e;
  public final UnsignedShortRef _30;
  public final UnsignedShortRef _32;
  public final UnsignedShortRef _34;
  public final UnsignedShortRef mono_36;
  public final UnsignedByteRef _38;
  public final UnsignedByteRef dmaIndex_39;
  public final UnsignedShortRef _3a;
  public final UnsignedShortRef _3c;
  public final UnsignedShortRef _3e;
  public final UnsignedShortRef _40;
  public final UnsignedShortRef _42;

  public SpuStruct44(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this._04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);

    this.sshdPtr_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, SshdFile::new));

    this._0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this._0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);

    this._10 = ref.offset(2, 0x10L).cast(ShortRef::new);
    this._12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this._14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this._16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
    this._18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);
    this.eventSpuIrq_1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.spuDmaTransferInProgress_20 = ref.offset(2, 0x20L).cast(BoolRef::new);
    this._22 = ref.offset(1, 0x22L).cast(UnsignedByteRef::new);
    this._23 = ref.offset(1, 0x23L).cast(UnsignedByteRef::new);
    this._24 = ref.offset(2, 0x24L).cast(UnsignedShortRef::new);
    this._26 = ref.offset(2, 0x26L).cast(UnsignedShortRef::new);
    this._28 = ref.offset(2, 0x28L).cast(UnsignedShortRef::new);
    this._2a = ref.offset(1, 0x2aL).cast(UnsignedByteRef::new);
    this._2b = ref.offset(1, 0x2bL).cast(UnsignedByteRef::new);
    this._2c = ref.offset(2, 0x2cL).cast(UnsignedShortRef::new);
    this._2e = ref.offset(2, 0x2eL).cast(UnsignedShortRef::new);
    this._30 = ref.offset(2, 0x30L).cast(UnsignedShortRef::new);
    this._32 = ref.offset(2, 0x32L).cast(UnsignedShortRef::new);
    this._34 = ref.offset(2, 0x34L).cast(UnsignedShortRef::new);
    this.mono_36 = ref.offset(2, 0x36L).cast(UnsignedShortRef::new);
    this._38 = ref.offset(1, 0x38L).cast(UnsignedByteRef::new);
    this.dmaIndex_39 = ref.offset(1, 0x39L).cast(UnsignedByteRef::new);
    this._3a = ref.offset(2, 0x3aL).cast(UnsignedShortRef::new);
    this._3c = ref.offset(2, 0x3cL).cast(UnsignedShortRef::new);
    this._3e = ref.offset(2, 0x3eL).cast(UnsignedShortRef::new);
    this._40 = ref.offset(2, 0x40L).cast(UnsignedShortRef::new);
    this._42 = ref.offset(2, 0x42L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
