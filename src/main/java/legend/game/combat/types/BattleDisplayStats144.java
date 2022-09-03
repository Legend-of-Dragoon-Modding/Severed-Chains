package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BattleDisplayStats144 implements MemoryRef {
  private final Value ref;

  public final ShortRef x_00;
  public final ShortRef y_02;
  public final ArrayRef<ArrayRef<BattleDisplayStats144Sub10>> _04;

  public BattleDisplayStats144(final Value ref) {
    this.ref = ref;

    this.x_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.y_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this._04 = ref.offset(4, 0x04L).cast(ArrayRef.of(ArrayRef.classFor(BattleDisplayStats144Sub10.class), 5, 0x40, ArrayRef.of(BattleDisplayStats144Sub10.class, 4, 0x10, BattleDisplayStats144Sub10::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
