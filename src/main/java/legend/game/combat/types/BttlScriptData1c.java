package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData1c implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef _00;
  public final UnsignedShortRef addition_02;
  public final UnsignedIntRef _04;
  public final UnsignedIntRef _08;
  public final UnsignedByteRef _0c;
  public final UnsignedIntRef _10;
  public final Pointer<QuadConsumerRef<BttlScriptData1c, Long, Long, Long>> _14;
  /** TODO ptr */
  public final UnsignedIntRef _18;

  public BttlScriptData1c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.addition_02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(Pointer.deferred(4, QuadConsumerRef::new));
    this._18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
