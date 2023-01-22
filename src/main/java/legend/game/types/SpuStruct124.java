package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class SpuStruct124 implements MemoryRef {
  private final Value ref;

  /** Upper nibble is message, lower nibble is channel */
  public final UnsignedByteRef command_000;
  public final UnsignedByteRef _001;
  public final UnsignedByteRef _002;
  public final UnsignedByteRef _003;

  public final UnsignedByteRef _005;

  public final UnsignedIntRef sssqOffset_00c;
  public final Pointer<SssqFile> sssqPtr_010;

  public final UnsignedIntRef _018;

  public final UnsignedByteRef _01e;

  public final UnsignedShortRef playableSoundIndex_020;
  public final UnsignedShortRef _022;
  public final UnsignedShortRef _024;
  public final UnsignedByteRef _026;
  public final UnsignedByteRef _027;
  /** Union */
  public final UnsignedIntRef _028_4b;
  public final UnsignedByteRef _028;
  public final UnsignedByteRef _029;
  public final UnsignedByteRef _02a;

  public final UnsignedIntRef _02c;

  public final UnsignedByteRef _035;

  public final UnsignedByteRef _037;

  public final UnsignedByteRef _039;
  public final UnsignedByteRef _03a;

  public final UnsignedByteRef _03c;

  public final ArrayRef<ArrayRef<UnsignedByteRef>> _03e;
  public final UnsignedShortRef keyOnLo_0de;
  public final UnsignedShortRef keyOnHi_0e0;
  public final UnsignedShortRef keyOffLo_0e2;
  public final UnsignedShortRef keyOffHi_0e4;
  public final UnsignedByteRef _0e6;
  public final UnsignedByteRef _0e7;
  public final UnsignedByteRef _0e8;
  public final UnsignedByteRef pitchShifted_0e9;
  public final UnsignedByteRef reverbEnabled_0ea;

  /** 12-bit fixed-point - 0x1000 is normal pitch */
  public final UnsignedShortRef pitch_0ec;
  /** 12-bit fixed-point */
  public final ShortRef pitchShiftVolLeft_0ee;
  /** 12-bit fixed-point */
  public final ShortRef pitchShiftVolRight_0f0;

  public final UnsignedByteRef _104;
  public final UnsignedByteRef _105;

  public final UnsignedShortRef tempo_108;
  public final UnsignedShortRef deltaTime_10a;
  public final UnsignedByteRef _10c;

  public final IntRef _110;
  public final UnsignedIntRef _114;
  public final UnsignedIntRef _118;
  public final UnsignedByteRef _11c;
  public final UnsignedByteRef _11d;
  public final UnsignedByteRef _11e;
  public final UnsignedByteRef _11f;
  public final UnsignedByteRef _120;

  public final UnsignedShortRef _122;

  public SpuStruct124(final Value ref) {
    this.ref = ref;

    this.command_000 = ref.offset(1, 0x000L).cast(UnsignedByteRef::new);
    this._001 = ref.offset(1, 0x001L).cast(UnsignedByteRef::new);
    this._002 = ref.offset(1, 0x002L).cast(UnsignedByteRef::new);
    this._003 = ref.offset(1, 0x003L).cast(UnsignedByteRef::new);

    this._005 = ref.offset(1, 0x005L).cast(UnsignedByteRef::new);

    this.sssqOffset_00c = ref.offset(4, 0x00cL).cast(UnsignedIntRef::new);
    this.sssqPtr_010 = ref.offset(4, 0x010L).cast(Pointer.deferred(1, SssqFile::new));

    this._018 = ref.offset(4, 0x018L).cast(UnsignedIntRef::new);

    this._01e = ref.offset(1, 0x01eL).cast(UnsignedByteRef::new);

    this.playableSoundIndex_020 = ref.offset(2, 0x020L).cast(UnsignedShortRef::new);
    this._022 = ref.offset(2, 0x022L).cast(UnsignedShortRef::new);
    this._024 = ref.offset(2, 0x024L).cast(UnsignedShortRef::new);
    this._026 = ref.offset(1, 0x026L).cast(UnsignedByteRef::new);
    this._027 = ref.offset(1, 0x027L).cast(UnsignedByteRef::new);
    this._028_4b = ref.offset(4, 0x028L).cast(UnsignedIntRef::new);
    this._028 = ref.offset(1, 0x028L).cast(UnsignedByteRef::new);
    this._029 = ref.offset(1, 0x029L).cast(UnsignedByteRef::new);
    this._02a = ref.offset(1, 0x02aL).cast(UnsignedByteRef::new);

    this._02c = ref.offset(4, 0x02cL).cast(UnsignedIntRef::new);

    this._035 = ref.offset(1, 0x035L).cast(UnsignedByteRef::new);

    this._037 = ref.offset(1, 0x037L).cast(UnsignedByteRef::new);

    this._039 = ref.offset(1, 0x039L).cast(UnsignedByteRef::new);
    this._03a = ref.offset(1, 0x03aL).cast(UnsignedByteRef::new);

    this._03c = ref.offset(1, 0x03cL).cast(UnsignedByteRef::new);

    this._03e = ref.offset(1, 0x03eL).cast(ArrayRef.of(ArrayRef.classFor(UnsignedByteRef.class), 10, 16, ArrayRef.of(UnsignedByteRef.class, 16, 1, UnsignedByteRef::new)));
    this.keyOnLo_0de = ref.offset(2, 0x0deL).cast(UnsignedShortRef::new);
    this.keyOnHi_0e0 = ref.offset(2, 0x0e0L).cast(UnsignedShortRef::new);
    this.keyOffLo_0e2 = ref.offset(2, 0x0e2L).cast(UnsignedShortRef::new);
    this.keyOffHi_0e4 = ref.offset(2, 0x0e4L).cast(UnsignedShortRef::new);
    this._0e6 = ref.offset(1, 0x0e6L).cast(UnsignedByteRef::new);
    this._0e7 = ref.offset(1, 0x0e7L).cast(UnsignedByteRef::new);
    this._0e8 = ref.offset(1, 0x0e8L).cast(UnsignedByteRef::new);
    this.pitchShifted_0e9 = ref.offset(1, 0x0e9L).cast(UnsignedByteRef::new);
    this.reverbEnabled_0ea = ref.offset(1, 0x0eaL).cast(UnsignedByteRef::new);

    this.pitch_0ec = ref.offset(2, 0x0ecL).cast(UnsignedShortRef::new);
    this.pitchShiftVolLeft_0ee = ref.offset(2, 0x0eeL).cast(ShortRef::new);
    this.pitchShiftVolRight_0f0 = ref.offset(2, 0x0f0L).cast(ShortRef::new);

    this._104 = ref.offset(1, 0x104L).cast(UnsignedByteRef::new);
    this._105 = ref.offset(1, 0x105L).cast(UnsignedByteRef::new);

    this.tempo_108 = ref.offset(2, 0x108L).cast(UnsignedShortRef::new);
    this.deltaTime_10a = ref.offset(2, 0x10aL).cast(UnsignedShortRef::new);
    this._10c = ref.offset(1, 0x10cL).cast(UnsignedByteRef::new);

    this._110 = ref.offset(4, 0x110L).cast(IntRef::new);
    this._114 = ref.offset(4, 0x114L).cast(UnsignedIntRef::new);
    this._118 = ref.offset(4, 0x118L).cast(UnsignedIntRef::new);
    this._11c = ref.offset(1, 0x11cL).cast(UnsignedByteRef::new);
    this._11d = ref.offset(1, 0x11dL).cast(UnsignedByteRef::new);
    this._11e = ref.offset(1, 0x11eL).cast(UnsignedByteRef::new);
    this._11f = ref.offset(1, 0x11fL).cast(UnsignedByteRef::new);
    this._120 = ref.offset(1, 0x120L).cast(UnsignedByteRef::new);

    this._122 = ref.offset(2, 0x122L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
