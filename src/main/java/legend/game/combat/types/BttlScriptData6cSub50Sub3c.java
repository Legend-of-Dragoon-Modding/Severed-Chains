package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub50Sub3c implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _02;
  public final UnsignedByteRef _03;
  public final ShortRef x_04;
  public final ShortRef y_06;
  public final UnsignedShortRef _08;

  public final UnsignedShortRef _0c;
  public final UnsignedShortRef _0e;
  public final UnsignedShortRef _10;

  public final UnsignedShortRef _14;
  public final UnsignedShortRef _16;
  public final UnsignedShortRef _18;

  public final UnsignedShortRef _28;

  public final ShortRef _2e;
  public final ShortRef _30;
  public final UnsignedShortRef _32;
  public final UnsignedShortRef _34;

  public BttlScriptData6cSub50Sub3c(final Value ref) {
    this.ref = ref;

    this._02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.x_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.y_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);

    this._0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this._10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);

    this._14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this._16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
    this._18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);

    this._28 = ref.offset(2, 0x28L).cast(UnsignedShortRef::new);

    this._2e = ref.offset(2, 0x2eL).cast(ShortRef::new);
    this._30 = ref.offset(2, 0x30L).cast(ShortRef::new);
    this._32 = ref.offset(2, 0x32L).cast(UnsignedShortRef::new);
    this._34 = ref.offset(2, 0x34L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
