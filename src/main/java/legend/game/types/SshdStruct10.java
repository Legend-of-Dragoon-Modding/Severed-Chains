package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class SshdStruct10 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;
  public final UnsignedByteRef _01;
  public final UnsignedByteRef _02;
  public final ByteRef _03;
  public final UnsignedShortRef _04;
  public final UnsignedShortRef _06;
  public final UnsignedShortRef _08;
  public final UnsignedByteRef _0a;
  public final UnsignedByteRef _0b;
  public final UnsignedByteRef _0c;
  public final UnsignedByteRef _0d;
  public final UnsignedByteRef _0e;
  public final UnsignedByteRef _0f;

  public SshdStruct10(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this._01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this._02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this._03 = ref.offset(1, 0x03L).cast(ByteRef::new);
    this._04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this._0a = ref.offset(1, 0x0aL).cast(UnsignedByteRef::new);
    this._0b = ref.offset(1, 0x0bL).cast(UnsignedByteRef::new);
    this._0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this._0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);
    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this._0f = ref.offset(1, 0x0fL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
