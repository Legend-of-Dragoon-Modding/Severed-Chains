package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub98 extends BttlScriptData6cSubBase1 {
  public final UnsignedIntRef _24;

  public final UnsignedShortRef _50;
  public final UnsignedShortRef _52;
  public final UnsignedShortRef _54;

  public final UnsignedShortRef _58;
  public final UnsignedShortRef _5a;
  public final UnsignedShortRef _5c;
  public final UnsignedByteRef _5e;
  public final UnsignedByteRef _5f;
  public final ByteRef _60;

  public final UnsignedIntRef _68; //TODO struct
  public final ByteRef _6c;

  public final VECTOR vec_70;

  public final IntRef _80;
  public final Pointer<TriConsumerRef<BttlScriptData6c, BttlScriptData6cSub98, Long>> _84;
  public final Pointer<QuadConsumerRef<Long, BttlScriptData6c, BttlScriptData6cSub98, Long>> _88;

  public final Pointer<QuadConsumerRef<Long, BttlScriptData6c, BttlScriptData6cSub98, Long>> _90;

  public BttlScriptData6cSub98(final Value ref) {
    super(ref);

    this._24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);

    this._50 = ref.offset(2, 0x50L).cast(UnsignedShortRef::new);
    this._52 = ref.offset(2, 0x52L).cast(UnsignedShortRef::new);
    this._54 = ref.offset(2, 0x54L).cast(UnsignedShortRef::new);

    this._58 = ref.offset(2, 0x58L).cast(UnsignedShortRef::new);
    this._5a = ref.offset(2, 0x5aL).cast(UnsignedShortRef::new);
    this._5c = ref.offset(2, 0x5cL).cast(UnsignedShortRef::new);
    this._5e = ref.offset(1, 0x5eL).cast(UnsignedByteRef::new);
    this._5f = ref.offset(1, 0x5fL).cast(UnsignedByteRef::new);
    this._60 = ref.offset(1, 0x60L).cast(ByteRef::new);

    this._68 = ref.offset(4, 0x68L).cast(UnsignedIntRef::new);
    this._6c = ref.offset(1, 0x6cL).cast(ByteRef::new);

    this.vec_70 = ref.offset(4, 0x70L).cast(VECTOR::new);

    this._80 = ref.offset(4, 0x80L).cast(IntRef::new);
    this._84 = ref.offset(4, 0x84L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this._88 = ref.offset(4, 0x88L).cast(Pointer.deferred(4, QuadConsumerRef::new));

    this._90 = ref.offset(4, 0x90L).cast(Pointer.deferred(4, QuadConsumerRef::new));
  }
}
