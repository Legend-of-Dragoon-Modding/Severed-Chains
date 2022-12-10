package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class StageData10 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;
  /** Something to do with audio */
  public final UnsignedByteRef _01;
  public final UnsignedByteRef _02;
  public final UnsignedByteRef _03;
  public final UnsignedByteRef _04;
  public final UnsignedByteRef _05;
  public final ShortRef _06;
  public final ShortRef _08;
  public final ShortRef _0a;
  public final ShortRef _0c;
  public final UnsignedShortRef _0e;

  public StageData10(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this._01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this._02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this._04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this._05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
