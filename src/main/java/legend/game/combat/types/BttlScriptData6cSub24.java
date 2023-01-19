package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6cSub24 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  /** TODO */
  public final UnsignedIntRef ptr_00;
  public final IntRef count_04;
  public final IntRef _08;
  public final IntRef _0c;
  public final IntRef _10;
  public final IntRef _14;
  public final IntRef _18;
  public final IntRef _1c;
  public final IntRef _20;

  public BttlScriptData6cSub24(final Value ref) {
    this.ref = ref;

    this.ptr_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.count_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this._1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this._20 = ref.offset(4, 0x20L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
