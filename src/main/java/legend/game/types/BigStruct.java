package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

/** Might be 0x210 bytes long */
public class BigStruct implements MemoryRef {
  private final Value ref;

  /** Pointer to an address on the linked list */
  public final UnsignedIntRef ptr_ui_00;
  public final UnsignedIntRef ptr_ui_04;
  public final UnsignedIntRef ptr_ui_08;
  public final UnknownStruct unknown_ui_0c;

  // At least 4c bytes
  public final InnerBigStruct inner_14;

  // inner_14 + 18 public final UnsignedIntRef ui_2c;
  // inner_14 + 1c public final UnsignedIntRef ui_30;
  // inner_14 + 20 public final UnsignedIntRef ui_34;

  /** TODO part of InnerBigStruct? */
  public final UnsignedIntRef ui_58;

  /** TODO unknown type, maybe part of InnerBigStruct? */
  public final Value v_64;

  public final UnsignedShortRef us_74;
  public final UnsignedShortRef us_76;
  public final UnsignedShortRef us_78;

  public final UnsignedIntRef ui_8c;
  public final UnsignedIntRef ptr_ui_90;
  public final UnsignedIntRef ptr_ui_94;
  public final UnsignedShortRef us_98;
  public final UnsignedShortRef us_9a;
  public final UnsignedByteRef ub_9c;
  public final UnsignedByteRef ub_9d;
  public final UnsignedShortRef us_9e;
  public final UnsignedShortRef us_a0;
  public final UnsignedByteRef ub_a2;
  public final UnsignedByteRef ub_a3;
  /** Pointer to an address on the linked list */
  public final UnsignedIntRef addr_ui_a4;
  public final UnsignedIntRef ui_a8;

  public final ShortRef s_c8;
  public final UnsignedShortRef us_ca;
  public final UnsignedByteRef ub_cc;
  public final UnsignedByteRef ub_cd;

  /** 7 ints long */
  public final ArrayRef<UnsignedIntRef> aub_d0;
  /** 7 bytes long */
  public final ArrayRef<UnsignedByteRef> aub_ec;
  public final UnsignedIntRef ui_f4;
  public final UnsignedIntRef ui_f8;
  public final UnsignedIntRef ui_fc;
  public final UnsignedIntRef ui_100;
  public final UnsignedIntRef ui_104;
  public final UnsignedIntRef ui_108;
  public final UnsignedIntRef ui_10c;
  public final UnsignedIntRef ui_110;
  public final UnsignedIntRef ui_114;
  public final UnsignedIntRef ui_118;
  public final UnsignedIntRef ui_11c;
  public final UnsignedIntRef ui_120;
  public final UnsignedIntRef ui_124;
  public final UnsignedShortRef us_128;
  public final UnsignedShortRef us_12a;
  public final UnsignedShortRef us_12c;
  public final UnsignedShortRef us_12e;
  public final UnsignedShortRef us_130;
  public final UnsignedShortRef us_132;
  public final UnsignedShortRef us_134;

  public final UnsignedIntRef ui_144;

  public final UnsignedIntRef ui_16c;
  public final UnsignedShortRef us_170;
  public final UnsignedShortRef us_172;

  public final UnsignedShortRef us_178;

  public final UnsignedIntRef ui_188;
  public final UnsignedIntRef ui_18c;
  public final UnsignedIntRef ui_190;
  public final UnsignedIntRef ui_194;

  public final UnsignedIntRef ui_19c;
  public final UnsignedIntRef ui_1a0;
  public final UnsignedIntRef ui_1a4;
  public final UnsignedIntRef ui_1a8;
  public final UnsignedIntRef ui_1ac;
  public final UnsignedIntRef ui_1b0;
  public final UnsignedIntRef ui_1b4;
  public final UnsignedIntRef ui_1b8;
  public final UnsignedIntRef ui_1bc;
  public final UnsignedIntRef ui_1c0;
  public final UnsignedByteRef ub_1c4;
  public final UnsignedByteRef ub_1c5;
  public final UnsignedByteRef ub_1c6;
  public final UnsignedByteRef ub_1c7;

  /** TODO unknown type */
  public final Value v_1d0;

  public BigStruct(final Value ref) {
    this.ref = ref;

    this.ptr_ui_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.ptr_ui_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.ptr_ui_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.unknown_ui_0c = ref.offset(4, 0x0cL).cast(UnknownStruct::new);

    this.inner_14 = ref.offset(4, 0x14L).cast(InnerBigStruct::new);

    this.ui_58 = ref.offset(4, 0x58L).cast(UnsignedIntRef::new);

    this.v_64 = ref.offset(4, 0x64L);

    this.us_74 = ref.offset(2, 0x74L).cast(UnsignedShortRef::new);
    this.us_76 = ref.offset(2, 0x76L).cast(UnsignedShortRef::new);
    this.us_78 = ref.offset(2, 0x78L).cast(UnsignedShortRef::new);

    this.ui_8c = ref.offset(4, 0x8cL).cast(UnsignedIntRef::new);
    this.ptr_ui_90 = ref.offset(4, 0x90L).cast(UnsignedIntRef::new);
    this.ptr_ui_94 = ref.offset(4, 0x94L).cast(UnsignedIntRef::new);
    this.us_98 = ref.offset(2, 0x98L).cast(UnsignedShortRef::new);
    this.us_9a = ref.offset(2, 0x9aL).cast(UnsignedShortRef::new);
    this.ub_9c = ref.offset(1, 0x9cL).cast(UnsignedByteRef::new);
    this.ub_9d = ref.offset(1, 0x9dL).cast(UnsignedByteRef::new);
    this.us_9e = ref.offset(2, 0x9eL).cast(UnsignedShortRef::new);
    this.us_a0 = ref.offset(2, 0xa0L).cast(UnsignedShortRef::new);
    this.ub_a2 = ref.offset(1, 0xa2L).cast(UnsignedByteRef::new);
    this.ub_a3 = ref.offset(1, 0xa3L).cast(UnsignedByteRef::new);
    this.addr_ui_a4 = ref.offset(4, 0xa4L).cast(UnsignedIntRef::new);
    this.ui_a8 = ref.offset(4, 0xa8L).cast(UnsignedIntRef::new);

    this.s_c8 = ref.offset(2, 0xc8L).cast(ShortRef::new);
    this.us_ca = ref.offset(2, 0xcaL).cast(UnsignedShortRef::new);
    this.ub_cc = ref.offset(1, 0xccL).cast(UnsignedByteRef::new);
    this.ub_cd = ref.offset(1, 0xcdL).cast(UnsignedByteRef::new);

    this.aub_d0 = ref.offset(4, 0xd0L).cast(ArrayRef.of(UnsignedIntRef.class, 7, 4, UnsignedIntRef::new));
    this.aub_ec = ref.offset(1, 0xecL).cast(ArrayRef.of(UnsignedByteRef.class, 7, 1, UnsignedByteRef::new));
    this.ui_f4 = ref.offset(4, 0xf4L).cast(UnsignedIntRef::new);
    this.ui_f8 = ref.offset(4, 0xf8L).cast(UnsignedIntRef::new);
    this.ui_fc = ref.offset(4, 0xfcL).cast(UnsignedIntRef::new);
    this.ui_100 = ref.offset(4, 0x100L).cast(UnsignedIntRef::new);
    this.ui_104 = ref.offset(4, 0x104L).cast(UnsignedIntRef::new);
    this.ui_108 = ref.offset(4, 0x108L).cast(UnsignedIntRef::new);
    this.ui_10c = ref.offset(4, 0x10cL).cast(UnsignedIntRef::new);
    this.ui_110 = ref.offset(4, 0x110L).cast(UnsignedIntRef::new);
    this.ui_114 = ref.offset(4, 0x114L).cast(UnsignedIntRef::new);
    this.ui_118 = ref.offset(4, 0x118L).cast(UnsignedIntRef::new);
    this.ui_11c = ref.offset(4, 0x11cL).cast(UnsignedIntRef::new);
    this.ui_120 = ref.offset(4, 0x120L).cast(UnsignedIntRef::new);
    this.ui_124 = ref.offset(4, 0x124L).cast(UnsignedIntRef::new);
    this.us_128 = ref.offset(2, 0x128L).cast(UnsignedShortRef::new);
    this.us_12a = ref.offset(2, 0x12aL).cast(UnsignedShortRef::new);
    this.us_12c = ref.offset(2, 0x12cL).cast(UnsignedShortRef::new);
    this.us_12e = ref.offset(2, 0x12eL).cast(UnsignedShortRef::new);
    this.us_130 = ref.offset(2, 0x130L).cast(UnsignedShortRef::new);
    this.us_132 = ref.offset(2, 0x132L).cast(UnsignedShortRef::new);
    this.us_134 = ref.offset(2, 0x134L).cast(UnsignedShortRef::new);

    this.ui_144 = ref.offset(4, 0x144L).cast(UnsignedIntRef::new);

    this.ui_16c = ref.offset(4, 0x16cL).cast(UnsignedIntRef::new);
    this.us_170 = ref.offset(2, 0x170L).cast(UnsignedShortRef::new);
    this.us_172 = ref.offset(2, 0x172L).cast(UnsignedShortRef::new);

    this.us_178 = ref.offset(2, 0x178L).cast(UnsignedShortRef::new);

    this.ui_188 = ref.offset(4, 0x188L).cast(UnsignedIntRef::new);
    this.ui_18c = ref.offset(4, 0x18cL).cast(UnsignedIntRef::new);
    this.ui_190 = ref.offset(4, 0x190L).cast(UnsignedIntRef::new);
    this.ui_194 = ref.offset(4, 0x194L).cast(UnsignedIntRef::new);

    this.ui_19c = ref.offset(4, 0x19cL).cast(UnsignedIntRef::new);
    this.ui_1a0 = ref.offset(4, 0x1a0L).cast(UnsignedIntRef::new);
    this.ui_1a4 = ref.offset(4, 0x1a4L).cast(UnsignedIntRef::new);
    this.ui_1a8 = ref.offset(4, 0x1a8L).cast(UnsignedIntRef::new);
    this.ui_1ac = ref.offset(4, 0x1acL).cast(UnsignedIntRef::new);
    this.ui_1b0 = ref.offset(4, 0x1b0L).cast(UnsignedIntRef::new);
    this.ui_1b4 = ref.offset(4, 0x1b4L).cast(UnsignedIntRef::new);
    this.ui_1b8 = ref.offset(4, 0x1b8L).cast(UnsignedIntRef::new);
    this.ui_1bc = ref.offset(4, 0x1bcL).cast(UnsignedIntRef::new);
    this.ui_1c0 = ref.offset(4, 0x1c0L).cast(UnsignedIntRef::new);
    this.ub_1c4 = ref.offset(1, 0x1c4L).cast(UnsignedByteRef::new);
    this.ub_1c5 = ref.offset(1, 0x1c5L).cast(UnsignedByteRef::new);
    this.ub_1c6 = ref.offset(1, 0x1c6L).cast(UnsignedByteRef::new);
    this.ub_1c7 = ref.offset(1, 0x1c7L).cast(UnsignedByteRef::new);

    this.v_1d0 = ref.offset(4, 0x1d0L);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
