package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.types.ScriptState;

public class BttlScriptData6c extends BattleScriptDataBase {
  public final UnsignedIntRef _04;
  public final UnsignedIntRef size_08;
  public final ByteRef scriptIndex_0c;
  public final ByteRef coord2Index_0d;
  public final ByteRef scriptIndex_0e;

  public final BttlScriptData6cInner _10;
  public final Pointer<BttlScriptData6cSubBase1> _44;
  public final Pointer<TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c>> _48;
  public final Pointer<TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c>> _4c;
  public final ShortRef scriptIndex_50;
  public final ShortRef scriptIndex_52;
  public final ShortRef scriptIndex_54;
  public final ShortRef scriptIndex_56;
  public final Pointer<BttlScriptData6cSubBase2> _58;
  public final CString type_5c;

  public BttlScriptData6c(final Value ref) {
    super(ref);

    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.size_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.scriptIndex_0c = ref.offset(1, 0x0cL).cast(ByteRef::new);
    this.coord2Index_0d = ref.offset(1, 0x0dL).cast(ByteRef::new);
    this.scriptIndex_0e = ref.offset(1, 0x0eL).cast(ByteRef::new);

    this._10 = ref.offset(4, 0x10L).cast(BttlScriptData6cInner::new);
    this._44 = ref.offset(4, 0x44L).cast(Pointer.deferred(4, BttlScriptData6cSubBase1::new));
    this._48 = ref.offset(4, 0x48L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this._4c = ref.offset(4, 0x4cL).cast(Pointer.deferred(4, TriConsumerRef::new));
    this.scriptIndex_50 = ref.offset(2, 0x50L).cast(ShortRef::new);
    this.scriptIndex_52 = ref.offset(2, 0x52L).cast(ShortRef::new);
    this.scriptIndex_54 = ref.offset(2, 0x54L).cast(ShortRef::new);
    this.scriptIndex_56 = ref.offset(2, 0x56L).cast(ShortRef::new);
    this._58 = ref.offset(4, 0x58L).cast(Pointer.deferred(4, BttlScriptData6cSubBase2::new));
    this.type_5c = ref.offset(0x10, 0x5cL).cast(CString::new);
  }
}
