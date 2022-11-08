package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class Textbox4c implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final ShortRef _04;
  public final ShortRef _06;
  public final UnsignedIntRef _08;
  public final IntRef z_0c;
  public final UnsignedIntRef _10;
  public final ShortRef x_14;
  public final ShortRef y_16;
  public final ShortRef chars_18;
  public final ShortRef lines_1a;
  public final UnsignedShortRef width_1c;
  public final ShortRef height_1e;
  public final ShortRef _20;
  public final ShortRef _22;
  public final IntRef _24;
  public final IntRef _28;
  public final IntRef _2c;
  public final IntRef _30;
  public final IntRef _34;
  public final IntRef _38;
  public final IntRef _3c;
  public final IntRef _40;
  public final IntRef _44;
  public final IntRef _48;

  public Textbox4c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.z_0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.x_14 = ref.offset(2, 0x14L).cast(ShortRef::new);
    this.y_16 = ref.offset(2, 0x16L).cast(ShortRef::new);
    this.chars_18 = ref.offset(2, 0x18L).cast(ShortRef::new);
    this.lines_1a = ref.offset(2, 0x1aL).cast(ShortRef::new);
    this.width_1c = ref.offset(2, 0x1cL).cast(UnsignedShortRef::new);
    this.height_1e = ref.offset(2, 0x1eL).cast(ShortRef::new);
    this._20 = ref.offset(2, 0x20L).cast(ShortRef::new);
    this._22 = ref.offset(2, 0x22L).cast(ShortRef::new);
    this._24 = ref.offset(4, 0x24L).cast(IntRef::new);
    this._28 = ref.offset(4, 0x28L).cast(IntRef::new);
    this._2c = ref.offset(4, 0x2cL).cast(IntRef::new);
    this._30 = ref.offset(4, 0x30L).cast(IntRef::new);
    this._34 = ref.offset(4, 0x34L).cast(IntRef::new);
    this._38 = ref.offset(4, 0x38L).cast(IntRef::new);
    this._3c = ref.offset(4, 0x3cL).cast(IntRef::new);
    this._40 = ref.offset(4, 0x40L).cast(IntRef::new);
    this._44 = ref.offset(4, 0x44L).cast(IntRef::new);
    this._48 = ref.offset(4, 0x48L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
