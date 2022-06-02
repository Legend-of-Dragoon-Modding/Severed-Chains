package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

import javax.annotation.Nullable;

public class BattleStruct24 implements MemoryRef {
  @Nullable
  private final Value ref;

  public final UnsignedIntRef _00;
  public final ShortRef _04;
  public final ShortRef _06;
  public final UnsignedShortRef _08;
  public final UnsignedShortRef _0a;
  public final UnsignedShortRef _0c;
  public final UnsignedByteRef _0e;
  public final UnsignedByteRef _0f;
  public final UnsignedShortRef _10;
  public final UnsignedShortRef _12;
  public final UnsignedByteRef _14;
  public final UnsignedByteRef _15;
  public final UnsignedByteRef _16;

  public final ShortRef _1c;
  public final ShortRef _1e;
  public final IntRef _20;

  public BattleStruct24(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this._0f = ref.offset(1, 0x0fL).cast(UnsignedByteRef::new);
    this._10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this._12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this._14 = ref.offset(1, 0x14L).cast(UnsignedByteRef::new);
    this._15 = ref.offset(1, 0x15L).cast(UnsignedByteRef::new);
    this._16 = ref.offset(1, 0x16L).cast(UnsignedByteRef::new);

    this._1c = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(ShortRef::new);
    this._20 = ref.offset(4, 0x20L).cast(IntRef::new);
  }

  public BattleStruct24() {
    this.ref = null;

    this._00 = new UnsignedIntRef();
    this._04 = new ShortRef();
    this._06 = new ShortRef();
    this._08 = new UnsignedShortRef();
    this._0a = new UnsignedShortRef();
    this._0c = new UnsignedShortRef();
    this._0e = new UnsignedByteRef();
    this._0f = new UnsignedByteRef();
    this._10 = new UnsignedShortRef();
    this._12 = new UnsignedShortRef();
    this._14 = new UnsignedByteRef();
    this._15 = new UnsignedByteRef();
    this._16 = new UnsignedByteRef();

    this._1c = new ShortRef();
    this._1e = new ShortRef();
    this._20 = new IntRef();
  }

  @Override
  public long getAddress() {
    if(this.ref == null) {
      throw new RuntimeException("Can't get address of stack object");
    }

    return this.ref.getAddress();
  }
}
