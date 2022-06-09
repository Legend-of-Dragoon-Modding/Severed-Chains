package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlStruct50 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final UnsignedIntRef _04;
  public final UnsignedIntRef _08;
  public final UnsignedIntRef _0c;
  public final ArrayRef<UnsignedShortRef> _10;

  public BttlStruct50(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this._10 = ref.offset(2, 0x10L).cast(ArrayRef.of(UnsignedShortRef.class, 32, 2, UnsignedShortRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
