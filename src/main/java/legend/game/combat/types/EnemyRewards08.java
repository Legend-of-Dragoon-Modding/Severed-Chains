package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class EnemyRewards08 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef xp_00;
  public final UnsignedShortRef gold_02;
  public final UnsignedByteRef itemChance_04;
  public final UnsignedByteRef itemDrop_05;
  public final UnsignedShortRef _06;

  public EnemyRewards08(final Value ref) {
    this.ref = ref;

    this.xp_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.gold_02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.itemChance_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.itemDrop_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this._06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
