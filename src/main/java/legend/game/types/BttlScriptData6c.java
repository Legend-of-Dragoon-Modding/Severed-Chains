package legend.game.types;

import legend.core.gpu.RECT;
import legend.core.memory.Value;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6c implements MemoryRef {
  private final Value ref;

  public final Pointer<BttlScriptData6c> _00;
  public final UnsignedIntRef _04;
  public final Pointer<BiFunctionRef<BttlScriptData6c, BttlScriptData6c, Long>> _08;
  public final RECT rect_0c;

  public final ShortRef _12;
  public final UnsignedIntRef _14;
  public final UnsignedIntRef _18;

  public final UnsignedIntRef _44; //TODO ptr
  public final Pointer<TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c>> _48;
  public final Pointer<TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c>> _4c;

  public final ShortRef _50;
  public final ShortRef _52;
  public final ShortRef _54;
  public final ShortRef _56;
  public final Pointer<BttlScriptData6c> _58;

  public BttlScriptData6c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, BttlScriptData6c::new));
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, BiFunctionRef::new));
    this.rect_0c = ref.offset(4, 0x0cL).cast(RECT::new);

    this._12 = ref.offset(2, 0x12L).cast(ShortRef::new);
    this._14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);

    this._44 = ref.offset(4, 0x44L).cast(UnsignedIntRef::new);
    this._48 = ref.offset(4, 0x48L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this._4c = ref.offset(4, 0x4cL).cast(Pointer.deferred(4, TriConsumerRef::new));

    this._50 = ref.offset(2, 0x50L).cast(ShortRef::new);
    this._52 = ref.offset(2, 0x52L).cast(ShortRef::new);
    this._54 = ref.offset(2, 0x54L).cast(ShortRef::new);
    this._56 = ref.offset(2, 0x56L).cast(ShortRef::new);
    this._58 = ref.offset(4, 0x58L).cast(Pointer.deferred(4, BttlScriptData6c::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
