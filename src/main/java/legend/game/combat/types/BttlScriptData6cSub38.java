package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub38 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedShortRef count_00;

  public final IntRef _04;
  public final IntRef scriptIndex_08;
  public final IntRef _0c;
  public final IntRef _10;
  public final IntRef _14;
  public final IntRef _18;
  public final ShortRef _1c;
  public final ShortRef _1e;
  public final ShortRef _20;
  public final UnsignedByteRef _22;
  public final UnsignedByteRef _23;
  public final ByteRef _24;

  public final UnsignedShortRef _26;
  public final UnsignedByteRef _28;
  public final UnsignedByteRef _29;
  public final UnsignedByteRef _2a;

  public final Pointer<QuadConsumerRef<EffectManagerData6c, BttlScriptData6cSub38, BttlScriptData6cSub38Sub14, Integer>> callback_2c;

  public final Pointer<UnboundedArrayRef<BttlScriptData6cSub38Sub14>> _34;

  public BttlScriptData6cSub38(final Value ref) {
    this.ref = ref;

    this.count_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);

    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.scriptIndex_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this._1c = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(ShortRef::new);
    this._20 = ref.offset(2, 0x20L).cast(ShortRef::new);
    this._22 = ref.offset(1, 0x22L).cast(UnsignedByteRef::new);
    this._23 = ref.offset(1, 0x23L).cast(UnsignedByteRef::new);
    this._24 = ref.offset(1, 0x24L).cast(ByteRef::new);

    this._26 = ref.offset(2, 0x26L).cast(UnsignedShortRef::new);
    this._28 = ref.offset(1, 0x28L).cast(UnsignedByteRef::new);
    this._29 = ref.offset(1, 0x29L).cast(UnsignedByteRef::new);
    this._2a = ref.offset(1, 0x2aL).cast(UnsignedByteRef::new);

    this.callback_2c = ref.offset(4, 0x2cL).cast(Pointer.deferred(4, QuadConsumerRef::new));
    this._34 = ref.offset(4, 0x34L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x14, BttlScriptData6cSub38Sub14::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
