package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class JoyStruct implements MemoryRef {
  private final Value ref;

  public final ArrayRef<ByteRef> b00;
  public final ShortRef s04;
  public final ArrayRef<ByteRef> b06;

  public final ByteRef b28;
  public final ByteRef b29;
  public final ByteRef b2a;
  public final ByteRef b2b;
  public final ByteRef b2c;

  public final IntRef i30;
  public final ByteRef b34;

  public final IntRef i38;

  public final IntRef i40;
  public final IntRef i44;
  public final IntRef i48;
  public final IntRef i4c;
  public final IntRef i50;
  public final IntRef i54;

  public final IntRef i5c;

  public final IntRef i64;

  public final IntRef i6c;

  public final IntRef i74;

  public final IntRef i7c;

  public JoyStruct(final Value ref) {
    this.ref = ref;

    this.b00 = ref.offset(4, 0x00L).cast(ArrayRef.of(ByteRef.class, 4, 1, ByteRef::new));
    this.s04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.b06 = ref.offset(2, 0x06L).cast(ArrayRef.of(ByteRef.class, 2, 1, ByteRef::new));

    this.b28 = ref.offset(1, 0x28L).cast(ByteRef::new);
    this.b29 = ref.offset(1, 0x29L).cast(ByteRef::new);
    this.b2a = ref.offset(1, 0x2aL).cast(ByteRef::new);
    this.b2b = ref.offset(1, 0x2bL).cast(ByteRef::new);
    this.b2c = ref.offset(1, 0x2cL).cast(ByteRef::new);

    this.i30 = ref.offset(4, 0x30L).cast(IntRef::new);
    this.b34 = ref.offset(1, 0x34L).cast(ByteRef::new);

    this.i38 = ref.offset(4, 0x38L).cast(IntRef::new);

    this.i40 = ref.offset(4, 0x40L).cast(IntRef::new);
    this.i44 = ref.offset(4, 0x44L).cast(IntRef::new);
    this.i48 = ref.offset(4, 0x48L).cast(IntRef::new);
    this.i4c = ref.offset(4, 0x4cL).cast(IntRef::new);
    this.i50 = ref.offset(4, 0x50L).cast(IntRef::new);
    this.i54 = ref.offset(4, 0x54L).cast(IntRef::new);

    this.i5c = ref.offset(4, 0x5cL).cast(IntRef::new);

    this.i64 = ref.offset(4, 0x64L).cast(IntRef::new);

    this.i6c = ref.offset(4, 0x6cL).cast(IntRef::new);

    this.i74 = ref.offset(4, 0x74L).cast(IntRef::new);

    this.i7c = ref.offset(4, 0x7cL).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
