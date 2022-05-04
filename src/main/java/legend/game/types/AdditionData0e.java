package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class AdditionData0e implements MemoryRef {
  private final Value ref;

  public final ByteRef _00;
  public final UnsignedByteRef attacks_01;
  public final ArrayRef<ShortRef> sp_02;
  public final ShortRef damage_0c;

  public AdditionData0e(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(ByteRef::new);
    this.attacks_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this.sp_02 = ref.offset(2, 0x02L).cast(ArrayRef.of(ShortRef.class, 5, 0x2, ShortRef::new));
    this.damage_0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
