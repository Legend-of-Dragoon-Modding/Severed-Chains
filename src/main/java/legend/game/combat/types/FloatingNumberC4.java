package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class FloatingNumberC4 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef state_00;
  public final UnsignedShortRef flags_02;
  /** Must be the bobj that the floating number is attached to */
  public final IntRef bobjIndex_04;
  public final UnsignedIntRef _08;
  /**
   * TODO figure out why backgrounds are not transparent
   * TODO not 100% sure if these are actually backwards (BGR), but pretty sure
   */
  public final UnsignedByteRef b_0c;
  public final UnsignedByteRef g_0d;
  public final UnsignedByteRef r_0e;
  public final UnsignedByteRef c_0f;
  public final UnsignedIntRef _10;
  public final IntRef _14;
  public final IntRef _18;
  public final IntRef x_1c;
  public final IntRef y_20;
  public final ArrayRef<FloatingNumberC4Sub20> digits_24;

  public FloatingNumberC4(final Value ref) {
    this.ref = ref;

    this.state_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.flags_02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.bobjIndex_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.b_0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this.g_0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);
    this.r_0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this.c_0f = ref.offset(1, 0x0fL).cast(UnsignedByteRef::new);
    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this.x_1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.y_20 = ref.offset(4, 0x20L).cast(IntRef::new);
    this.digits_24 = ref.offset(4, 0x24L).cast(ArrayRef.of(FloatingNumberC4Sub20.class, 5, 0x20, FloatingNumberC4Sub20::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
