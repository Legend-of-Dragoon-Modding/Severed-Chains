package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class AdditionOverlaysSquare0e implements MemoryRef {
  private final Value ref;

  public final ByteRef _00;

  public final ShortRef _02; // something to do with angle
  public final UnsignedByteRef r_04;
  public final UnsignedByteRef g_05;
  public final UnsignedByteRef b_06;

  public final ShortRef _08;
  public final ShortRef _0a;
  public final ByteRef _0c;
  public final ByteRef _0d;

  public AdditionOverlaysSquare0e(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(ByteRef::new);

    this._02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.r_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.g_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.b_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);

    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(1, 0x0cL).cast(ByteRef::new);
    this._0d = ref.offset(1, 0x0dL).cast(ByteRef::new);
  }

  @Override
  public long getAddress() { return this.ref.getAddress(); }
}
