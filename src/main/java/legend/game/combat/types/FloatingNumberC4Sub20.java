package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class FloatingNumberC4Sub20 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final UnsignedIntRef _04;
  public final IntRef _08;
  public final ShortRef digit_0c;
  public final ShortRef x_0e;
  public final ShortRef _10;
  public final UnsignedShortRef u_12;
  public final UnsignedShortRef v_14;
  public final UnsignedShortRef texW_16;
  public final UnsignedShortRef texH_18;
  public final ShortRef _1a;
  public final UnsignedShortRef _1c;

  public FloatingNumberC4Sub20(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.digit_0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this.x_0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this._10 = ref.offset(2, 0x10L).cast(ShortRef::new);
    this.u_12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this.v_14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this.texW_16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
    this.texH_18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);
    this._1a = ref.offset(2, 0x1aL).cast(ShortRef::new);
    this._1c = ref.offset(2, 0x1cL).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
