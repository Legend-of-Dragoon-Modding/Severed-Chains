package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub98 extends BttlScriptData6cSubBase1 {
  public final BttlScriptData6cSub98Inner24 _08;

  public final UnsignedIntRef _24;

  public final UnsignedShortRef _34;
  public final UnsignedShortRef _36;

  public final UnsignedShortRef _50;
  public final UnsignedShortRef _52;
  public final UnsignedShortRef _54;

  public final UnsignedShortRef _58;
  public final UnsignedShortRef _5a;
  public final UnsignedShortRef clut_5c;
  public final UnsignedByteRef _5e;
  public final UnsignedByteRef _5f;
  public final ByteRef _60;
  public final ByteRef _61;

  /** Size in bytes of following array of structs */
  public final UnsignedIntRef size_64;
  public final Pointer<UnboundedArrayRef<BttlScriptData6cSub98Sub94>> _68;
  public final ByteRef _6c;

  public final VECTOR vec_70;

  public final IntRef _80;
  public final Pointer<TriConsumerRef<BttlScriptData6c, BttlScriptData6cSub98, BttlScriptData6cSub98Sub94>> _84;
  public final Pointer<QuadConsumerRef<Long, BttlScriptData6c, BttlScriptData6cSub98, BttlScriptData6cSub98Sub94>> _88;
  public final Pointer<QuadConsumerRef<BttlScriptData6c, BttlScriptData6cSub98, BttlScriptData6cSub98Sub94, BttlScriptData6cSub98Inner24>> _8c;
  public final Pointer<QuadConsumerRef<Long, BttlScriptData6c, BttlScriptData6cSub98, BttlScriptData6cSub98Sub94>> _90;
  public final Pointer<BttlScriptData6cSub98> _94;

  public BttlScriptData6cSub98(final Value ref) {
    super(ref);

    this._08 = ref.offset(4, 0x08L).cast(BttlScriptData6cSub98Inner24::new);

    this._24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);

    this._34 = ref.offset(2, 0x34L).cast(UnsignedShortRef::new);
    this._36 = ref.offset(2, 0x36L).cast(UnsignedShortRef::new);

    this._50 = ref.offset(2, 0x50L).cast(UnsignedShortRef::new);
    this._52 = ref.offset(2, 0x52L).cast(UnsignedShortRef::new);
    this._54 = ref.offset(2, 0x54L).cast(UnsignedShortRef::new);

    this._58 = ref.offset(2, 0x58L).cast(UnsignedShortRef::new);
    this._5a = ref.offset(2, 0x5aL).cast(UnsignedShortRef::new);
    this.clut_5c = ref.offset(2, 0x5cL).cast(UnsignedShortRef::new);
    this._5e = ref.offset(1, 0x5eL).cast(UnsignedByteRef::new);
    this._5f = ref.offset(1, 0x5fL).cast(UnsignedByteRef::new);
    this._60 = ref.offset(1, 0x60L).cast(ByteRef::new);
    this._61 = ref.offset(1, 0x61L).cast(ByteRef::new);

    this.size_64 = ref.offset(4, 0x64L).cast(UnsignedIntRef::new);
    this._68 = ref.offset(4, 0x68L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x94, BttlScriptData6cSub98Sub94::new)));
    this._6c = ref.offset(1, 0x6cL).cast(ByteRef::new);

    this.vec_70 = ref.offset(4, 0x70L).cast(VECTOR::new);

    this._80 = ref.offset(4, 0x80L).cast(IntRef::new);
    this._84 = ref.offset(4, 0x84L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this._88 = ref.offset(4, 0x88L).cast(Pointer.deferred(4, QuadConsumerRef::new));
    this._8c = ref.offset(4, 0x8cL).cast(Pointer.deferred(4, QuadConsumerRef::new));
    this._90 = ref.offset(4, 0x90L).cast(Pointer.deferred(4, QuadConsumerRef::new));
    this._94 = ref.offset(4, 0x94L).cast(Pointer.deferred(4, BttlScriptData6cSub98::new));
  }
}
