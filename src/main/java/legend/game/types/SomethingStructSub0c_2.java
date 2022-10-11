package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class SomethingStructSub0c_2 implements MemoryRef {
  private final Value ref;

  public final ShortRef _00;
  public final ShortRef _02;
  public final IntRef _04;
  public final IntRef _08;

  public SomethingStructSub0c_2(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
