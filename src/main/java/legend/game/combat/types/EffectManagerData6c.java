package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.types.ScriptState;

public class EffectManagerData6c extends BattleScriptDataBase {
  public final UnsignedIntRef _04;
  public final UnsignedIntRef size_08;
  public final ByteRef scriptIndex_0c;
  public final ByteRef coord2Index_0d;
  public final ByteRef scriptIndex_0e;

  public final BttlScriptData6cInner _10;
  public final Pointer<BttlScriptData6cSubBase1> effect_44;
  public final Pointer<TriConsumerRef<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c>> _48;
  public final Pointer<TriConsumerRef<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c>> destructor_4c;
  public final ShortRef parentScriptIndex_50;
  public final ShortRef childScriptIndex_52;
  /** If replacing a child, this is the old child's ID */
  public final ShortRef oldChildScriptIndex_54;
  /** If replaced as a child, this is the new child's ID */
  public final ShortRef newChildScriptIndex_56;
  public final Pointer<BttlScriptData6cSubBase2> _58;
  public final CString type_5c;

  public EffectManagerData6c(final Value ref) {
    super(ref);

    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.size_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.scriptIndex_0c = ref.offset(1, 0x0cL).cast(ByteRef::new);
    this.coord2Index_0d = ref.offset(1, 0x0dL).cast(ByteRef::new);
    this.scriptIndex_0e = ref.offset(1, 0x0eL).cast(ByteRef::new);

    this._10 = ref.offset(4, 0x10L).cast(BttlScriptData6cInner::new);
    this.effect_44 = ref.offset(4, 0x44L).cast(Pointer.deferred(4, value -> {throw new RuntimeException("Can't be instantiated");}));
    this._48 = ref.offset(4, 0x48L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this.destructor_4c = ref.offset(4, 0x4cL).cast(Pointer.deferred(4, TriConsumerRef::new));
    this.parentScriptIndex_50 = ref.offset(2, 0x50L).cast(ShortRef::new);
    this.childScriptIndex_52 = ref.offset(2, 0x52L).cast(ShortRef::new);
    this.oldChildScriptIndex_54 = ref.offset(2, 0x54L).cast(ShortRef::new);
    this.newChildScriptIndex_56 = ref.offset(2, 0x56L).cast(ShortRef::new);
    this._58 = ref.offset(4, 0x58L).cast(Pointer.deferred(4, value -> {throw new RuntimeException("Can't be instantiated");}));
    this.type_5c = ref.offset(0x10, 0x5cL).cast(CString::new);
  }
}
