package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

/** Dragoon addition data? */
public class EffeScriptData1c implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;

  public final UnsignedShortRef _02;
  public final UnsignedByteRef _04;
  public final UnsignedByteRef _05;
  public final UnsignedByteRef _06;
  public final UnsignedByteRef _07;
  public final ArrayRef<UnsignedByteRef> _08;
  public final UnsignedByteRef _0d;
  public final UnsignedByteRef _0e;
  public final UnsignedByteRef _0f;
  public final UnsignedByteRef _10;
  public final UnsignedByteRef _11;
  public final UnsignedByteRef _12;
  public final UnsignedByteRef _13;
  public final UnsignedByteRef _14;

  public final UnsignedIntRef _18;

  public EffeScriptData1c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this._04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this._05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this._06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);
    this._07 = ref.offset(1, 0x07L).cast(UnsignedByteRef::new);
    this._08 = ref.offset(1, 0x08L).cast(ArrayRef.of(UnsignedByteRef.class, 5, 1, UnsignedByteRef::new));
    this._0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);
    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this._0f = ref.offset(1, 0x0fL).cast(UnsignedByteRef::new);
    this._10 = ref.offset(1, 0x10L).cast(UnsignedByteRef::new);
    this._11 = ref.offset(1, 0x11L).cast(UnsignedByteRef::new);
    this._12 = ref.offset(1, 0x12L).cast(UnsignedByteRef::new);
    this._13 = ref.offset(1, 0x13L).cast(UnsignedByteRef::new);
    this._14 = ref.offset(1, 0x14L).cast(UnsignedByteRef::new);

    this._18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
