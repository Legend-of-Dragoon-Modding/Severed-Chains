package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6c implements MemoryRef {
  private final Value ref;

  public final Pointer<BttlScriptData6c> _00;
  public final UnsignedIntRef _04;
  public final Pointer<FunctionRef<BttlScriptData6c, Long>> _08;

  public final Pointer<TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c>> _48;

  public final Pointer<BttlScriptData6c> _58;

  public BttlScriptData6c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, BttlScriptData6c::new));
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, FunctionRef::new));

    this._48 = ref.offset(4, 0x48L).cast(Pointer.deferred(4, TriConsumerRef::new));

    this._58 = ref.offset(4, 0x58L).cast(Pointer.deferred(4, BttlScriptData6c::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
