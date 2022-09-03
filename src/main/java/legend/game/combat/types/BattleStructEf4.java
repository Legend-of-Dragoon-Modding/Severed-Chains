package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class BattleStructEf4 implements MemoryRef {
  private final Value ref;

  public final ArrayRef<IntRef> _180;

  public final ArrayRef<IntRef> bobjIndices_d80;
  public final ArrayRef<BattleStructEf4Sub08> _d8c;
  public final ArrayRef<IntRef> bobjIndices_e0c;

  public final ArrayRef<IntRef> charBobjIndices_e40;
  public final ArrayRef<IntRef> bobjIndices_e50;

  public final ArrayRef<IntRef> bobjIndices_e78;

  public final ArrayRef<IntRef> bobjIndices_ebc;

  public final ByteRef _ee4;

  /** TODO ptr */
  public final UnsignedIntRef ptr_ee8;
  public final IntRef _eec;

  public BattleStructEf4(final Value ref) {
    this.ref = ref;

    this._180 = ref.offset(4, 0x180L).cast(ArrayRef.of(IntRef.class, 0x100, 4, IntRef::new));

    this.bobjIndices_d80 = ref.offset(4, 0xd80L).cast(ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
    this._d8c = ref.offset(4, 0xd8cL).cast(ArrayRef.of(BattleStructEf4Sub08.class, 16, 8, BattleStructEf4Sub08::new));
    this.bobjIndices_e0c = ref.offset(4, 0xe0cL).cast(ArrayRef.of(IntRef.class, 12, 4, IntRef::new));

    this.charBobjIndices_e40 = ref.offset(4, 0xe40L).cast(ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
    this.bobjIndices_e50 = ref.offset(4, 0xe50L).cast(ArrayRef.of(IntRef.class, 3, 4, IntRef::new));

    this.bobjIndices_e78 = ref.offset(4, 0xe78L).cast(ArrayRef.of(IntRef.class, 10, 4, IntRef::new));

    this.bobjIndices_ebc = ref.offset(4, 0xebcL).cast(ArrayRef.of(IntRef.class, 10, 4, IntRef::new));

    this._ee4 = ref.offset(1, 0xee4L).cast(ByteRef::new);

    this.ptr_ee8 = ref.offset(4, 0xee8L).cast(UnsignedIntRef::new);
    this._eec = ref.offset(4, 0xeecL).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
