package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class JoyStruct implements MemoryRef {
  private final Value ref;

  public final ArrayRef<JoyStruct2> joyStruct2Arr00;

  public final UnsignedShortRef s12;
  public final UnsignedShortRef s14;
  public final UnsignedShortRef s16;

  public final ArrayRef<UnsignedIntRef> iArr20;
  public final ArrayRef<UnsignedIntRef> iArr28;
  public final IntRef i30;
  public final UnsignedByteRef b34;
  public final UnsignedByteRef b35;
  public final UnsignedByteRef b36;

  public final ArrayRef<UnsignedIntRef> iArr38;
  public final ArrayRef<UnsignedIntRef> iArr40;
  public final ArrayRef<ShortRef> sArr48;
  public final ArrayRef<UnsignedShortRef> sArr50;
  public final ArrayRef<UnsignedShortRef> sArr54;
  public final ArrayRef<UnsignedByteRef> bArr58;
  public final UnsignedShortRef s5c;
  public final ArrayRef<UnsignedByteRef> b5e;
  public final ArrayRef<ByteRef> b62;

  public final ByteRef b80;
  public final ByteRef b81;
  public final ByteRef b82;
  public final ByteRef b83;
  public final ByteRef b84;

  public final UnsignedIntRef i88;
  public final UnsignedByteRef b8c;

  public JoyStruct(final Value ref) {
    this.ref = ref;

    this.joyStruct2Arr00 = ref.offset(2, 0x00L).cast(ArrayRef.of(JoyStruct2.class, 2, 0x10, JoyStruct2::new));

    this.s12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this.s14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this.s16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);

    this.iArr20 = ref.offset(4, 0x20L).cast(ArrayRef.of(UnsignedIntRef.class, 2, 4, UnsignedIntRef::new));
    this.iArr28 = ref.offset(4, 0x28L).cast(ArrayRef.of(UnsignedIntRef.class, 2, 4, UnsignedIntRef::new));
    this.i30 = ref.offset(4, 0x30L).cast(IntRef::new);
    this.b34 = ref.offset(1, 0x34L).cast(UnsignedByteRef::new);
    this.b35 = ref.offset(1, 0x35L).cast(UnsignedByteRef::new);
    this.b36 = ref.offset(1, 0x36L).cast(UnsignedByteRef::new);

    this.iArr38 = ref.offset(4, 0x38L).cast(ArrayRef.of(UnsignedIntRef.class, 2, 4, UnsignedIntRef::new));
    this.iArr40 = ref.offset(4, 0x40L).cast(ArrayRef.of(UnsignedIntRef.class, 2, 4, UnsignedIntRef::new));
    this.sArr48 = ref.offset(2, 0x48L).cast(ArrayRef.of(ShortRef.class, 4, 2, ShortRef::new));
    this.sArr50 = ref.offset(2, 0x50L).cast(ArrayRef.of(UnsignedShortRef.class, 2, 2, UnsignedShortRef::new));
    this.sArr54 = ref.offset(2, 0x54L).cast(ArrayRef.of(UnsignedShortRef.class, 2, 2, UnsignedShortRef::new));
    this.bArr58 = ref.offset(1, 0x58L).cast(ArrayRef.of(UnsignedByteRef.class, 4, 1, UnsignedByteRef::new));
    this.s5c = ref.offset(2, 0x5cL).cast(UnsignedShortRef::new);
    this.b5e = ref.offset(1, 0x5eL).cast(ArrayRef.of(UnsignedByteRef.class, 4, 1, UnsignedByteRef::new));
    this.b62 = ref.offset(1, 0x62L).cast(ArrayRef.of(ByteRef.class, 4, 1, ByteRef::new));

    this.b80 = ref.offset(1, 0x80L).cast(ByteRef::new);
    this.b81 = ref.offset(1, 0x81L).cast(ByteRef::new);
    this.b82 = ref.offset(1, 0x82L).cast(ByteRef::new);
    this.b83 = ref.offset(1, 0x83L).cast(ByteRef::new);
    this.b84 = ref.offset(1, 0x84L).cast(ByteRef::new);

    this.i88 = ref.offset(4, 0x88L).cast(UnsignedIntRef::new);
    this.b8c = ref.offset(1, 0x8cL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  public static class JoyStruct2 implements MemoryRef {
    private final Value ref;

    public final UnsignedShortRef s00;
    public final UnsignedShortRef s02;
    public final UnsignedShortRef s04;
    public final UnsignedShortRef s06;
    public final ShortRef s08;
    public final UnsignedShortRef s0a;
    public final UnsignedShortRef s0c;
    public final UnsignedShortRef s0e;

    public JoyStruct2(final Value ref) {
      this.ref = ref;

      this.s00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
      this.s02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
      this.s04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
      this.s06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
      this.s08 = ref.offset(2, 0x08L).cast(ShortRef::new);
      this.s0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
      this.s0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
      this.s0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }
}
