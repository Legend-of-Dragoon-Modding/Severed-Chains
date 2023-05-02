package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class AdditionOverlaysSquare0e implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;

  public final UnsignedShortRef _02;
  public final UnsignedByteRef r_04;
  public final UnsignedByteRef g_05;
  public final UnsignedByteRef b_06;

  public final UnsignedShortRef _08;
  public final UnsignedShortRef _0a;
  public final UnsignedByteRef _0c;
  public final UnsignedByteRef _0d;

  public AdditionOverlaysSquare0e(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.r_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.g_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.b_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);

    this._08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this._0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this._0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() { return this.ref.getAddress(); }
}
