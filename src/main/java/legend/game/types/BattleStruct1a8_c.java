package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BattleStruct1a8_c implements MemoryRef {
  private final Value ref;

  /** This can either be a pointer to a BPE, or 2-byte x, 1-byte Y, 1-byte H */
  public final UnsignedIntRef bpe_00;
  public final UnsignedShortRef x_00;
  public final UnsignedByteRef y_02;
  public final UnsignedByteRef h_03;
  // Overlap ends here

  public final ShortRef _04;
  public final ShortRef _06;
  public final ByteRef _08;
  public final ByteRef _09;
  public final UnsignedByteRef _0a;
  public final UnsignedByteRef _0b;

  public BattleStruct1a8_c(final Value ref) {
    this.ref = ref;

    this.bpe_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.x_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.y_02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this.h_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);

    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(1, 0x08L).cast(ByteRef::new);
    this._09 = ref.offset(1, 0x09L).cast(ByteRef::new);
    this._0a = ref.offset(1, 0x0aL).cast(UnsignedByteRef::new);
    this._0b = ref.offset(1, 0x0bL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
