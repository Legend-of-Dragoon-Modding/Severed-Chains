package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class DabasData100 implements MemoryRef {
  private final Value ref;

  public final IntRef chapterIndex_00;

  public final ArrayRef<IntRef> items_14;
  public final IntRef specialItem_2c;

  public final IntRef gold_34;

  public final IntRef _38;
  public final IntRef _3c;

  public DabasData100(final Value ref) {
    this.ref = ref;

    this.chapterIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);

    this.items_14 = ref.offset(4, 0x14L).cast(ArrayRef.of(IntRef.class, 6, 4, IntRef::new));
    this.specialItem_2c = ref.offset(4, 0x2cL).cast(IntRef::new);

    this.gold_34 = ref.offset(4, 0x34L).cast(IntRef::new);

    this._38 = ref.offset(4, 0x38L).cast(IntRef::new);
    this._3c = ref.offset(4, 0x3cL).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
