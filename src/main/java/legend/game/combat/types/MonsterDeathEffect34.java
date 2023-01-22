package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class MonsterDeathEffect34 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final ShortRef _00;
  public final UnsignedShortRef _02;
  public final UnsignedShortRef animCount_04;
  public final UnsignedShortRef _06;
  public final IntRef scriptIndex_08;
  public final BattleStruct24 _0c;

  /** TODO ptr */
  public final UnsignedIntRef ptr_30;

  public MonsterDeathEffect34(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.animCount_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this.scriptIndex_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(BattleStruct24::new);

    this.ptr_30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
