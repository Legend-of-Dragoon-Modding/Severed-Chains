package legend.game.combat.types;

import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.scripting.ScriptState;

public class EffectData98 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef scriptIndex_00;
  public final IntRef scriptIndex_04;
  public final EffectData98Inner24 _08;

  public final Pointer<TmdObjTable> tmd_30;
  public final UnsignedShortRef _34;
  public final UnsignedShortRef _36;

  public final UnsignedShortRef count_50;
  public final UnsignedShortRef _52;
  public final UnsignedShortRef _54;
  public final UnsignedShortRef tpage_56;
  public final UnsignedShortRef u_58;
  public final UnsignedShortRef v_5a;
  public final UnsignedShortRef clut_5c;
  public final UnsignedByteRef w_5e;
  public final UnsignedByteRef h_5f;
  public final ByteRef _60;
  public final ByteRef _61;

  /** Size in bytes of following array of structs */
  public final UnsignedIntRef size_64;
  public final Pointer<UnboundedArrayRef<EffectData98Sub94>> _68;
  public final ByteRef _6c;

  public final VECTOR vec_70;
  public final IntRef _80;
  public final Pointer<TriConsumerRef<EffectManagerData6c, EffectData98, EffectData98Sub94>> _84;
  public final Pointer<QuadConsumerRef<ScriptState<EffectManagerData6c>, EffectManagerData6c, EffectData98, EffectData98Sub94>> _88;
  public final Pointer<QuadConsumerRef<EffectManagerData6c, EffectData98, EffectData98Sub94, EffectData98Inner24>> _8c;
  public final Pointer<QuadConsumerRef<ScriptState<EffectManagerData6c>, EffectManagerData6c, EffectData98, EffectData98Sub94>> _90;
  public final Pointer<EffectData98> _94;

  public EffectData98(final Value ref) {
    this.ref = ref;

    this.scriptIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.scriptIndex_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(EffectData98Inner24::new);

    this.tmd_30 = ref.offset(4, 0x30L).cast(Pointer.deferred(4, TmdObjTable::new));
    this._34 = ref.offset(2, 0x34L).cast(UnsignedShortRef::new);
    this._36 = ref.offset(2, 0x36L).cast(UnsignedShortRef::new);

    this.count_50 = ref.offset(2, 0x50L).cast(UnsignedShortRef::new);
    this._52 = ref.offset(2, 0x52L).cast(UnsignedShortRef::new);
    this._54 = ref.offset(2, 0x54L).cast(UnsignedShortRef::new);
    this.tpage_56 = ref.offset(2, 0x56L).cast(UnsignedShortRef::new);
    this.u_58 = ref.offset(2, 0x58L).cast(UnsignedShortRef::new);
    this.v_5a = ref.offset(2, 0x5aL).cast(UnsignedShortRef::new);
    this.clut_5c = ref.offset(2, 0x5cL).cast(UnsignedShortRef::new);
    this.w_5e = ref.offset(1, 0x5eL).cast(UnsignedByteRef::new);
    this.h_5f = ref.offset(1, 0x5fL).cast(UnsignedByteRef::new);
    this._60 = ref.offset(1, 0x60L).cast(ByteRef::new);
    this._61 = ref.offset(1, 0x61L).cast(ByteRef::new);

    this.size_64 = ref.offset(4, 0x64L).cast(UnsignedIntRef::new);
    this._68 = ref.offset(4, 0x68L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x94, EffectData98Sub94::new)));
    this._6c = ref.offset(1, 0x6cL).cast(ByteRef::new);

    this.vec_70 = ref.offset(4, 0x70L).cast(VECTOR::new);
    this._80 = ref.offset(4, 0x80L).cast(IntRef::new);
    this._84 = ref.offset(4, 0x84L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this._88 = ref.offset(4, 0x88L).cast(Pointer.deferred(4, QuadConsumerRef::new));
    this._8c = ref.offset(4, 0x8cL).cast(Pointer.deferred(4, QuadConsumerRef::new));
    this._90 = ref.offset(4, 0x90L).cast(Pointer.deferred(4, QuadConsumerRef::new));
    this._94 = ref.offset(4, 0x94L).cast(Pointer.deferred(4, EffectData98::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
