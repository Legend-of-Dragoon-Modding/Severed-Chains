package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;

public class SnowEffect implements MemoryRef {
  private final Value ref;

  public final ShortRef _00;

  public final IntRef _08;
  public final IntRef _0c;
  public final IntRef _10;
  public final ShortRef size_14;
  public final ShortRef x_16;
  public final ShortRef y_18;

  public final IntRef xStep_1c;
  public final IntRef yStep_20;
  public final IntRef xAccumulator_24;
  public final IntRef yAccumulator_28;

  public final ShortRef colour_34;

  public final Pointer<SnowEffect> next_38;

  public SnowEffect(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);

    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this.size_14 = ref.offset(2, 0x14L).cast(ShortRef::new);
    this.x_16 = ref.offset(2, 0x16L).cast(ShortRef::new);
    this.y_18 = ref.offset(2, 0x18L).cast(ShortRef::new);

    this.xStep_1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.yStep_20 = ref.offset(4, 0x20L).cast(IntRef::new);
    this.xAccumulator_24 = ref.offset(4, 0x24L).cast(IntRef::new);
    this.yAccumulator_28 = ref.offset(4, 0x28L).cast(IntRef::new);

    this.colour_34 = ref.offset(2, 0x34L).cast(ShortRef::new);

    this.next_38 = ref.offset(4, 0x38L).cast(Pointer.deferred(4, SnowEffect::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
