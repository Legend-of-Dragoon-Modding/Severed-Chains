package legend.game.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.CString;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6c implements MemoryRef {
  private final Value ref;

  public final Pointer<BttlScriptData6c> _00;
  public final UnsignedIntRef _04;

  public final UnsignedIntRef _10;
  public final VECTOR vec_14;

  public final UnsignedIntRef _44; //TODO ptr
  public final Pointer<TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c>> _48;
  public final Pointer<TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c>> _4c;

  public final ShortRef _50;
  public final ShortRef scriptIndex_52;
  public final ShortRef scriptIndex_54;
  public final ShortRef scriptIndex_56;
  public final Pointer<BttlScriptData6cSubBase> _58;
  public final CString _5c;

  public BttlScriptData6c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, BttlScriptData6c::new));
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);

    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.vec_14 = ref.offset(4, 0x14L).cast(VECTOR::new);

    this._44 = ref.offset(4, 0x44L).cast(UnsignedIntRef::new);
    this._48 = ref.offset(4, 0x48L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this._4c = ref.offset(4, 0x4cL).cast(Pointer.deferred(4, TriConsumerRef::new));

    this._50 = ref.offset(2, 0x50L).cast(ShortRef::new);
    this.scriptIndex_52 = ref.offset(2, 0x52L).cast(ShortRef::new);
    this.scriptIndex_54 = ref.offset(2, 0x54L).cast(ShortRef::new);
    this.scriptIndex_56 = ref.offset(2, 0x56L).cast(ShortRef::new);
    this._58 = ref.offset(4, 0x58L).cast(Pointer.deferred(4, BttlScriptData6cSubBase::new));
    this._5c = ref.offset(0x10, 0x5cL).cast(CString::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
