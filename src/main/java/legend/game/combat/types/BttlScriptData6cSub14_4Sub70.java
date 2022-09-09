package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub14_4Sub70 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;

  public final UnsignedShortRef _02;
  public final UnsignedShortRef _04;

  public final IntRef _08;
  public final IntRef _0c;
  public final IntRef _10;

  public final IntRef _1c;

  public final UnsignedByteRef _38;
  public final UnsignedByteRef _39;
  public final UnsignedByteRef _3a;

  public final IntRef _3c;
  public final IntRef _40;
  public final IntRef _44;
  public final IntRef _48;
  public final ShortRef _4c;

  public final IntRef _50;
  public final IntRef _54;
  public final IntRef _58;
  public final IntRef _5c;
  public final IntRef _60;
  public final ShortRef _64;
  public final UnsignedShortRef _66;
  public final UnsignedShortRef _68;
  public final UnsignedShortRef _6a;
  public final UnsignedShortRef _6c;
  public final ShortRef _6e;

  public BttlScriptData6cSub14_4Sub70(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);

    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);

    this._1c = ref.offset(4, 0x10L).cast(IntRef::new);

    this._38 = ref.offset(1, 0x38L).cast(UnsignedByteRef::new);
    this._39 = ref.offset(1, 0x39L).cast(UnsignedByteRef::new);
    this._3a = ref.offset(1, 0x3aL).cast(UnsignedByteRef::new);

    this._3c = ref.offset(4, 0x3cL).cast(IntRef::new);
    this._40 = ref.offset(4, 0x40L).cast(IntRef::new);
    this._44 = ref.offset(4, 0x44L).cast(IntRef::new);
    this._48 = ref.offset(4, 0x48L).cast(IntRef::new);
    this._4c = ref.offset(2, 0x4cL).cast(ShortRef::new);

    this._50 = ref.offset(4, 0x50L).cast(IntRef::new);
    this._54 = ref.offset(4, 0x54L).cast(IntRef::new);
    this._58 = ref.offset(4, 0x58L).cast(IntRef::new);
    this._5c = ref.offset(4, 0x5cL).cast(IntRef::new);
    this._60 = ref.offset(4, 0x60L).cast(IntRef::new);
    this._64 = ref.offset(2, 0x64L).cast(ShortRef::new);
    this._66 = ref.offset(2, 0x66L).cast(UnsignedShortRef::new);
    this._68 = ref.offset(2, 0x68L).cast(UnsignedShortRef::new);
    this._6a = ref.offset(2, 0x6aL).cast(UnsignedShortRef::new);
    this._6c = ref.offset(2, 0x6cL).cast(UnsignedShortRef::new);
    this._6e = ref.offset(2, 0x6eL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
