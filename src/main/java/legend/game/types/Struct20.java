package legend.game.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;

public class Struct20 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final UnsignedIntRef _04;
  public final UnsignedIntRef _08;
  public final VECTOR transfer;
  public final ShortRef _18;

  public final Pointer<Struct20> next_1c;

  public Struct20(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.transfer = ref.offset(4, 0x0cL).cast(VECTOR::new);
    this._18 = ref.offset(2, 0x18L).cast(ShortRef::new);

    this.next_1c = ref.offset(4, 0x1cL).cast(Pointer.deferred(4, Struct20::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
