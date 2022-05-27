package legend.game;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.TriConsumerRef;
import legend.game.types.BtldScriptData27c;
import legend.game.types.BttlScriptData6c;
import legend.game.types.BttlScriptData6cSub34;
import legend.game.types.DR_MODE;
import legend.game.types.RunningScript;
import legend.game.types.ScriptState;

import javax.annotation.Nullable;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Bttl.FUN_800c7488;
import static legend.game.Bttl.FUN_800cff54;
import static legend.game.Bttl.FUN_800de544;
import static legend.game.Bttl.FUN_800e80c4;
import static legend.game.Bttl.FUN_800e8d04;
import static legend.game.Bttl.FUN_800e8dd4;
import static legend.game.Bttl.FUN_800e95f0;
import static legend.game.Bttl.FUN_800eac58;
import static legend.game.Bttl._800c693c;
import static legend.game.Bttl._800c6948;
import static legend.game.Bttl._800fa754;
import static legend.game.Bttl._800fb954;
import static legend.game.Scus94491BpeSegment.FUN_80015d38;
import static legend.game.Scus94491BpeSegment.addToLinkedListHead;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.strcpy;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixLV;
import static legend.game.Scus94491BpeSegment_8003.GetClut;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMode;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040ec0;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;

public final class SEffe {
  private SEffe() { }

  private static final Value _800fb794 = MEMORY.ref(2, 0x800fb794L);

  private static final Value _800fb7bc = MEMORY.ref(1, 0x800fb7bcL);

  private static final Value _800fb7c0 = MEMORY.ref(1, 0x800fb7c0L);

  private static final Value _800fb7f0 = MEMORY.ref(1, 0x800fb7f0L);

  private static final SVECTOR _800fb94c = MEMORY.ref(2, 0x800fb94cL, SVECTOR::new);

  private static final Value _80119b7c = MEMORY.ref(4, 0x80119b7cL);

  private static final Value _80119b94 = MEMORY.ref(4, 0x80119b94L);

  private static final Value _80119bac = MEMORY.ref(4, 0x80119bacL);

  private static final Value _80119cb0 = MEMORY.ref(4, 0x80119cb0L);

  private static final Value _80119db4 = MEMORY.ref(4, 0x80119db4L);

  private static final Value _80119f41 = MEMORY.ref(1, 0x80119f41L);

  private static final Value _8011a008 = MEMORY.ref(4, 0x8011a008L);
  private static final Value _8011a00c = MEMORY.ref(4, 0x8011a00cL);
  private static final Value _8011a010 = MEMORY.ref(4, 0x8011a010L);
  private static final Value _8011a014 = MEMORY.ref(1, 0x8011a014L);

  @Method(0x800fe8b8L)
  public static void FUN_800fe8b8(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    assert false;
  }

  @Method(0x800fea70L)
  public static long FUN_800fea70(long a0, long a1, final long a2, final long a3) {
    long v0;
    long v1;
    long s0;
    long s2;
    long hi;
    v1 = 0xff_0000L;
    s2 = 0x8010_0000L;
    v0 = MEMORY.ref(4, s2).offset(-0x58acL).get();
    v1 = v1 | 0xf001L;
    v0 = v0 + 0x1L;
    s0 = v0 << 5;
    s0 = s0 + v0;
    s0 = s0 << 2;
    s0 = s0 - v0;
    s0 = s0 << 2;
    s0 = s0 - v0;
    s0 = s0 << 2;
    s0 = s0 + v0;
    v0 = s0 << 5;
    s0 = s0 + v0;
    hi = (s0 & 0xffff_ffffL) * (v1 & 0xffff_ffffL) >>> 32;
    MEMORY.ref(4, s2).offset(-0x58acL).setu(s0);
    v1 = hi;
    v1 = v1 >>> 4;
    v0 = v1 << 12;
    v0 = v0 + v1;
    s0 = s0 - v0;
    s0 = s0 << 16;
    s0 = (int)s0 >> 16;
    a0 = s0;
    v0 = rcos(a0);
    a1 = 0x6816_0000L;
    a0 = MEMORY.ref(4, s2).offset(-0x58acL).get();
    a1 = a1 | 0x8169L;
    a0 = a0 + 0x1L;
    v1 = a0 << 5;
    v1 = v1 + a0;
    v1 = v1 << 2;
    v1 = v1 - a0;
    v1 = v1 << 2;
    v1 = v1 - a0;
    v1 = v1 << 2;
    v1 = v1 + a0;
    a0 = v1 << 5;
    v1 = v1 + a0;
    hi = (v1 & 0xffff_ffffL) * (a1 & 0xffff_ffffL) >>> 32;
    v0 = (int)v0 >> 8;
    MEMORY.ref(2, a2).offset(0x58L).setu(v0);
    a0 = s0;
    MEMORY.ref(4, s2).offset(-0x58acL).setu(v1);
    a1 = hi;
    v0 = v1 - a1;
    v0 = v0 >>> 1;
    a1 = a1 + v0;
    a1 = a1 >>> 6;
    v0 = a1 << 1;
    v0 = v0 + a1;
    v0 = v0 << 3;
    v0 = v0 - a1;
    v0 = v0 << 2;
    v0 = v0 - a1;
    v1 = v1 - v0;
    v1 = v1 + 0xaL;
    v1 = -v1;
    MEMORY.ref(2, a2).offset(0x5aL).setu(v1);
    v0 = rsin(a0);
    a1 = 0x446f_0000L;
    v1 = MEMORY.ref(4, s2).offset(-0x58acL).get();
    a1 = a1 | 0x8657L;
    v1 = v1 + 0x1L;
    a0 = v1 << 5;
    a0 = a0 + v1;
    a0 = a0 << 2;
    a0 = a0 - v1;
    a0 = a0 << 2;
    a0 = a0 - v1;
    a0 = a0 << 2;
    a0 = a0 + v1;
    v1 = a0 << 5;
    a0 = a0 + v1;
    hi = (a0 & 0xffff_ffffL) * (a1 & 0xffff_ffffL) >>> 32;
    v1 = (int)v0 >> 8;
    v0 = s0;
    MEMORY.ref(2, a2).offset(0x5cL).setu(v1);
    MEMORY.ref(4, s2).offset(-0x58acL).setu(a0);
    a1 = hi;
    v1 = a0 - a1;
    v1 = v1 >>> 1;
    a1 = a1 + v1;
    a1 = a1 >>> 6;
    v1 = a1 << 1;
    v1 = v1 + a1;
    v1 = v1 << 3;
    v1 = v1 + a1;
    v1 = v1 << 2;
    v1 = v1 + a1;
    a0 = a0 - v1;
    a0 = a0 - 0x32L;
    a0 = a0 << 8;
    v1 = MEMORY.ref(2, a2).offset(0x84L).get();
    a1 = MEMORY.ref(2, a2).offset(0x88L).get();
    v1 = v1 + a0;
    MEMORY.ref(2, a2).offset(0x84L).setu(v1);
    v1 = MEMORY.ref(2, a2).offset(0x86L).get();
    a1 = a1 + a0;
    MEMORY.ref(2, a2).offset(0x88L).setu(a1);
    v1 = v1 + a0;
    MEMORY.ref(2, a2).offset(0x86L).setu(v1);
    return v0;
  }

  @Method(0x801012a0L)
  public static void FUN_801012a0(long a0, long a1) {
    assert false;
  }

  @Method(0x801012d4L)
  public static void FUN_801012d4(long a0, long a1) {
    assert false;
  }

  @Method(0x80101e84L)
  public static void FUN_80101e84(long a0, long a1, long a2, long a3) {
    MEMORY.ref(1, a0).offset(0x60L).setu(0);
    MEMORY.ref(2, a0).offset(0x54L).setu(0);

    if((MEMORY.ref(4, a0).offset(0x24L).get() & 0x6000_0000L) != 0) {
      long s2 = MEMORY.ref(4, a0).offset(0x68L).get();
      long v0 = (MEMORY.ref(4, a0).offset(0x24L).get() & 0x6000_0000L) >>> 27;
      MEMORY.ref(2, a0).offset(0x54L).setu(_800fb794.offset(2, v0).get());

      //LAB_80101f2c
      for(int i = 0; i < MEMORY.ref(2, a0).offset(0x50L).get(); i++) {
        long v1 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);
        MEMORY.ref(1, v1).offset(0x3L).setu(0x9L);
        MEMORY.ref(4, v1).offset(0x4L).setu(0x2c80_8080L);
        MEMORY.ref(4, s2).offset(0x80L).setu(addToLinkedListHead(MEMORY.ref(2, a0).offset(0x54L).get() * 0x10L));
        s2 = s2 + 0x94L;
      }
    }

    //LAB_80101f70
    if((a3 & 0xf_ff00L) == 0xf_ff00L) {
      long a0_0 = (a3 & 0xffL) * 0x8L;
      long v0 = _800c6948.get() + a0_0;
      MEMORY.ref(2, a0).offset(0x58L).setu(MEMORY.ref(2, v0).offset(0x0L).get());
      MEMORY.ref(2, a0).offset(0x5aL).setu(MEMORY.ref(2, v0).offset(0x2L).get());
      MEMORY.ref(1, a0).offset(0x5eL).setu(MEMORY.ref(1, v0).offset(0x4L).get());
      MEMORY.ref(1, a0).offset(0x5fL).setu(MEMORY.ref(1, v0).offset(0x5L).get());
      MEMORY.ref(2, a0).offset(0x5cL).setu(MEMORY.ref(2, v0).offset(0x6L).get());
    } else {
      //LAB_80101fec
      long v0 = FUN_800eac58(a3 | 0x400_0000L);
      v0 = v0 + MEMORY.ref(4, v0).offset(0x8L).get();
      MEMORY.ref(2, a0).offset(0x58L).setu(MEMORY.ref(2, v0).offset(0x0L).get());
      MEMORY.ref(2, a0).offset(0x5aL).setu(MEMORY.ref(2, v0).offset(0x2L).get());
      MEMORY.ref(1, a0).offset(0x5eL).setu(MEMORY.ref(1, v0).offset(0x4L).get() * 0x4L);
      MEMORY.ref(1, a0).offset(0x5fL).setu(MEMORY.ref(1, v0).offset(0x6L).get());
      MEMORY.ref(2, a0).offset(0x5cL).setu(GetClut(MEMORY.ref(2, v0).offset(0x8L).getSigned(), MEMORY.ref(2, v0).offset(0xaL).getSigned()));
    }

    //LAB_80102048
    MEMORY.ref(2, a0).offset(0x34L).setu(MEMORY.ref(1, a0).offset(0x5eL).get() >>> 1);
    MEMORY.ref(2, a0).offset(0x36L).setu(MEMORY.ref(1, a0).offset(0x5fL).get() >>> 1);
  }

  @Method(0x80102088L)
  public static long FUN_80102088(final RunningScript s2) {
    long v0 = (int)s2.params_20.get(2).deref().get() / 0x100000;
    long s6 = FUN_800e80c4(s2.scriptStateIndex_00.get(), 0x98L, null, _80119b7c.offset(v0 * 0x4L).deref(4).cast(TriConsumerRef::new), MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_800fe8b8", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));
    long v1 = s2.params_20.get(3).deref().get() & 0xffffL;
    long s0 = v1 * 0x94L;
    long s3 = scriptStatePtrArr_800bc1c0.get((int)s6).deref().innerStruct_00.getPointer(); //TODO
    long s1 = MEMORY.ref(4, s3).offset(0x44L).get();
    MEMORY.ref(2, s1).offset(0x50L).setu(s2.params_20.get(3).deref().get());
    MEMORY.ref(4, s1).offset(0x68L).setu(addToLinkedListHead(s0));

    if(_8011a00c.get() == 0) {
      _8011a00c.setu(s1);
    }

    //LAB_801021a8
    v0 = _8011a010.get();

    if(v0 != 0) {
      MEMORY.ref(4, v0).offset(0x94L).setu(s1);
    }

    //LAB_801021c0
    MEMORY.ref(4, s1).offset(0x64L).setu(s0);
    MEMORY.ref(4, s1).offset(0x4L).setu(s2.params_20.get(1).deref().get());
    MEMORY.ref(4, s1).offset(0x0L).setu(s6);
    MEMORY.ref(4, s1).offset(0x84L).setu(_80119bac.offset(s2.params_20.get(4).deref().get() * 0x4L).get());
    MEMORY.ref(4, s1).offset(0x88L).setu(_80119cb0.offset(s2.params_20.get(4).deref().get() * 0x4L).get());
    _8011a010.setu(s1);
    MEMORY.ref(2, s1).offset(0x52L).setu(0);
    MEMORY.ref(2, s1).offset(0x34L).setu(0);
    MEMORY.ref(2, s1).offset(0x36L).setu(0);
    MEMORY.ref(1, s1).offset(0x6cL).setu(0);
    MEMORY.ref(4, s1).offset(0x94L).setu(0);
    MEMORY.ref(4, s1).offset(0x8cL).setu(_80119db4.offset(s2.params_20.get(4).deref().get() * 0x4L).get());
    s2.params_20.get(0).deref().set(s6);

    //LAB_8010223c
    for(int i = 0; i < 9; i++) {
      MEMORY.ref(4, s1).offset(0x8L).offset(i * 0x4L).setu(s2.params_20.get(i).deref().get());
    }

    v0 = s2.params_20.get(3).deref().get() & 0xffffL;
    long s5 = s1 + 0x8L;

    //LAB_80102278
    long s2_0 = MEMORY.ref(4, s1).offset(0x68L).get();
    for(int i = 0; i < v0; i++) {
      _8011a008.setu(i);
      FUN_80101308(s6, s3, s1, s2_0, s5);
      memcpy(s2_0 + 0x3cL, s2_0 + 0x50L, 8);
      s2_0 = s2_0 + 0x94L;
    }

    //LAB_801022b4
    if(MEMORY.ref(1, s1).offset(0x61L).getSigned() != 0) {
      v0 = getMethodAddress(SEffe.class, "FUN_801012d4", long.class, long.class);
    } else {
      //LAB_801022cc
      v0 = getMethodAddress(SEffe.class, "FUN_801012a0", long.class, long.class);
    }

    //LAB_801022d4
    MEMORY.ref(4, s1).offset(0x90L).setu(v0);
    v0 = (int)s2.params_20.get(2).deref().get() / 0x100000;
    MEMORY.ref(2, s1).offset(0x54L).setu(0);
    _80119b94.offset(v0 * 4).deref(4).call(s1, s2_0, s1 + 0x8L, s2.params_20.get(2).deref().get());
    MEMORY.ref(4, s3).offset(0x10L).oru(0x5000_0000L);
    MEMORY.ref(4, s3).offset(0x4L).oru(0x4_0000L);
    FUN_80102534();
    return 0;
  }

  @Method(0x80101308L)
  public static void FUN_80101308(long a0, long a1, long a2, long a3, long a4) {
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    long hi;
    long lo;
    long sp3c;
    s6 = a4;
    fp = a2;
    sp3c = a1;
    a1 = MEMORY.ref(4, s6).offset(0x1cL).get();
    a2 = MEMORY.ref(2, s6).offset(0x14L).get();
    s0 = MEMORY.ref(2, s6).offset(0x20L).get();
    v0 = a1 & 0xffL;
    if(v0 != 0) {
      s3 = a3;
    } else {
      s3 = a3;
      a0 = -0x100L;
      v1 = 0x8012_0000L;
      v1 = v1 - 0x6814L;
      v0 = s0 << 2;
      v0 = v0 + v1;
      v0 = MEMORY.ref(1, v0).offset(0x2L).get();
      a0 = a1 & a0;
      a0 = a0 | v0;
      MEMORY.ref(4, s6).offset(0x1cL).setu(a0);
    }

    //LAB_8010137c
    v0 = MEMORY.ref(1, s6).offset(0x1dL).get();
    a1 = MEMORY.ref(4, s6).offset(0x1cL).get();
    if(v0 != 0) {
      s1 = 0xff_0000L;
    } else {
      s1 = 0xff_0000L;
      a0 = 0xffff_0000L;
      a0 = a0 | 0xffL;
      v1 = 0x8012_0000L;
      v1 = v1 - 0x6814L;
      v0 = s0 & 0xffffL;
      v0 = v0 << 2;
      v0 = v0 + v1;
      v0 = MEMORY.ref(1, v0).offset(0x1L).get();
      a0 = a1 & a0;
      v0 = v0 << 8;
      a0 = a0 | v0;
      MEMORY.ref(4, s6).offset(0x1cL).setu(a0);
      a1 = MEMORY.ref(4, s6).offset(0x1cL).get();
    }

    //LAB_801013c0
    v0 = a1 & s1;
    if(v0 != 0) {
      t7 = 0xff_0000L;
    } else {
      t7 = 0xff_0000L;
      a0 = 0xff00_0000L;
      a0 = a0 | 0xffffL;
      v1 = 0x8012_0000L;
      v1 = v1 - 0x6814L;
      v0 = s0 & 0xffffL;
      v0 = v0 << 2;
      v0 = v0 + v1;
      v0 = MEMORY.ref(1, v0).offset(0x0L).get();
      a0 = a1 & a0;
      v0 = v0 << 16;
      a0 = a0 | v0;
      MEMORY.ref(4, s6).offset(0x1cL).setu(a0);
    }

    //LAB_80101400
    t7 = t7 | 0xf001L;
    t3 = 0xfe0_0000L;
    v0 = 0x8012_0000L;
    v0 = v0 - 0x6814L;
    t4 = s0 & 0xffffL;
    t2 = t4 << 2;
    v0 = t2 + v0;
    t3 = t3 | 0x3f81L;
    t6 = 0x8010_0000L;
    a3 = 0x446f_0000L;
    a3 = a3 | 0x8657L;
    v0 = MEMORY.ref(1, v0).offset(0x3L).get();
    t2 = t2 + t4;
    MEMORY.ref(1, fp).offset(0x61L).setu(v0);
    v1 = MEMORY.ref(4, t6).offset(-0x58acL).get();
    a0 = MEMORY.ref(1, s6).offset(0x1dL).get();
    t1 = MEMORY.ref(4, s6).offset(0x1cL).get();
    v1 = v1 + 0x1L;
    v0 = v1 << 5;
    v0 = v0 + v1;
    v0 = v0 << 2;
    v0 = v0 - v1;
    v0 = v0 << 2;
    v0 = v0 - v1;
    v0 = v0 << 2;
    v0 = v0 + v1;
    v1 = v0 << 5;
    v0 = v0 + v1;
    v1 = a2 & 0xffffL;
    v1 = v1 + 0x1L;
    MEMORY.ref(4, t6).offset(-0x58acL).setu(v0);
    hi = (v0 & 0xffff_ffffL) % (v1 & 0xffff_ffffL);
    v1 = hi;
    v0 = 0x73L;
    MEMORY.ref(1, s3).offset(0x0L).setu(v0);
    v0 = 0x6dL;
    MEMORY.ref(1, s3).offset(0x1L).setu(v0);
    v0 = 0x6bL;
    MEMORY.ref(1, s3).offset(0x2L).setu(v0);
    v0 = MEMORY.ref(4, t6).offset(-0x58acL).get();
    t2 = t2 << 1;
    MEMORY.ref(2, s3).offset(0x54L).setu(0);
    MEMORY.ref(2, s3).offset(0x52L).setu(0);
    MEMORY.ref(2, s3).offset(0x50L).setu(0);
    MEMORY.ref(2, s3).offset(0x5cL).setu(0);
    MEMORY.ref(2, s3).offset(0x5aL).setu(0);
    MEMORY.ref(2, s3).offset(0x58L).setu(0);
    MEMORY.ref(2, s3).offset(0x8L).setu(0);
    MEMORY.ref(2, s3).offset(0x6L).setu(0);
    MEMORY.ref(2, s3).offset(0xcL).setu(0);
    MEMORY.ref(2, s3).offset(0xaL).setu(0);
    MEMORY.ref(2, s3).offset(0x64L).setu(0);
    MEMORY.ref(2, s3).offset(0x62L).setu(0);
    MEMORY.ref(2, s3).offset(0x60L).setu(0);
    MEMORY.ref(2, s3).offset(0x8eL).setu(0);
    MEMORY.ref(2, s3).offset(0x8cL).setu(0);
    v0 = v0 + 0x1L;
    a1 = v0 << 5;
    a1 = a1 + v0;
    a1 = a1 << 2;
    a1 = a1 - v0;
    a1 = a1 << 2;
    a1 = a1 - v0;
    a1 = a1 << 2;
    a1 = a1 + v0;
    v0 = a1 << 5;
    a1 = a1 + v0;
    hi = (a1 & 0xffff_ffffL) * (t7 & 0xffff_ffffL) >>> 32;
    MEMORY.ref(2, s3).offset(0x8aL).setu(0);
    t1 = t1 & s1;
    v0 = MEMORY.ref(4, s3).offset(0x90L).get();
    t1 = (int)t1 >> 16;
    MEMORY.ref(2, s3).offset(0x88L).setu(a0);
    MEMORY.ref(2, s3).offset(0x86L).setu(a0);
    MEMORY.ref(2, s3).offset(0x84L).setu(a0);
    MEMORY.ref(4, t6).offset(-0x58acL).setu(a1);
    v0 = v0 | 0x1L;
    MEMORY.ref(4, s3).offset(0x90L).setu(v0);
    v0 = a1 + 0x1L;
    t0 = hi;
    t0 = t0 >>> 4;
    a2 = t0 << 12;
    a2 = a2 + t0;
    a1 = a1 - a2;
    v1 = v1 + 0x1L;
    MEMORY.ref(2, s3).offset(0x4L).setu(v1);
    v1 = v0 << 5;
    v1 = v1 + v0;
    v1 = v1 << 2;
    v1 = v1 - v0;
    v1 = v1 << 2;
    v1 = v1 - v0;
    v1 = v1 << 2;
    v1 = v1 + v0;
    v0 = v1 << 5;
    v1 = v1 + v0;
    v0 = 0xff80_0000L;
    v0 = v0 | 0x3fe1L;
    hi = (v1 & 0xffff_ffffL) * (v0 & 0xffff_ffffL) >>> 32;
    v0 = v1 + 0x1L;
    a0 = v0 << 5;
    a0 = a0 + v0;
    a0 = a0 << 2;
    a0 = a0 - v0;
    a0 = a0 << 2;
    a0 = a0 - v0;
    a0 = a0 << 2;
    a0 = a0 + v0;
    v0 = a0 << 5;
    a0 = a0 + v0;
    MEMORY.ref(2, s3).offset(0xeL).setu(a1);
    MEMORY.ref(4, t6).offset(-0x58acL).setu(v1);
    MEMORY.ref(4, t6).offset(-0x58acL).setu(a0);
    t5 = hi;
    a1 = t5 >>> 9;
    v0 = a1 << 9;
    v0 = v0 + a1;
    v1 = v1 - v0;
    v1 = v1 - 0x100L;
    v0 = a0 + 0x1L;
    MEMORY.ref(2, s3).offset(0x10L).setu(v1);
    v1 = v0 << 5;
    v1 = v1 + v0;
    v1 = v1 << 2;
    v1 = v1 - v0;
    hi = (a0 & 0xffff_ffffL) * (t7 & 0xffff_ffffL) >>> 32;
    v1 = v1 << 2;
    v1 = v1 - v0;
    v1 = v1 << 2;
    v1 = v1 + v0;
    v0 = v1 << 5;
    v1 = v1 + v0;
    v0 = v1 + 0x1L;
    a1 = v0 << 5;
    a1 = a1 + v0;
    a1 = a1 << 2;
    a1 = a1 - v0;
    s2 = hi;
    a1 = a1 << 2;
    a1 = a1 - v0;
    hi = (v1 & 0xffff_ffffL) * (t7 & 0xffff_ffffL) >>> 32;
    a1 = a1 << 2;
    a1 = a1 + v0;
    v0 = a1 << 5;
    a1 = a1 + v0;
    MEMORY.ref(4, t6).offset(-0x58acL).setu(v1);
    MEMORY.ref(4, t6).offset(-0x58acL).setu(a1);
    a2 = s2 >>> 4;
    v0 = a2 << 12;
    v0 = v0 + a2;
    a0 = a0 - v0;
    MEMORY.ref(2, s3).offset(0x70L).setu(a0);
    t0 = hi;
    a0 = t0 >>> 4;
    v0 = a0 << 12;
    v0 = v0 + a0;
    hi = (a1 & 0xffff_ffffL) * (t7 & 0xffff_ffffL) >>> 32;
    v1 = v1 - v0;
    v0 = a1 + 0x1L;
    MEMORY.ref(2, s3).offset(0x72L).setu(v1);
    v1 = v0 << 5;
    v1 = v1 + v0;
    v1 = v1 << 2;
    v1 = v1 - v0;
    v1 = v1 << 2;
    v1 = v1 - v0;
    v1 = v1 << 2;
    v1 = v1 + v0;
    t5 = hi;
    v0 = v1 << 5;
    v1 = v1 + v0;
    hi = (v1 & 0xffff_ffffL) * (t3 & 0xffff_ffffL) >>> 32;
    v0 = v1 + 0x1L;
    a0 = v0 << 5;
    a0 = a0 + v0;
    a0 = a0 << 2;
    a0 = a0 - v0;
    a0 = a0 << 2;
    a0 = a0 - v0;
    a0 = a0 << 2;
    a0 = a0 + v0;
    v0 = a0 << 5;
    t0 = t5 >>> 4;
    t5 = hi;
    a0 = a0 + v0;
    MEMORY.ref(4, t6).offset(-0x58acL).setu(v1);
    hi = (a0 & 0xffff_ffffL) * (t3 & 0xffff_ffffL) >>> 32;
    a2 = t0 << 12;
    a2 = a2 + t0;
    a1 = a1 - a2;
    MEMORY.ref(2, s3).offset(0x74L).setu(a1);
    a1 = t5 >>> 3;
    v0 = a1 << 7;
    v0 = v0 + a1;
    v1 = v1 - v0;
    v1 = v1 - 0x40L;
    MEMORY.ref(2, s3).offset(0x78L).setu(v1);
    v1 = a0 + 0x1L;
    t3 = hi;
    v0 = t3 >>> 3;
    a1 = v0 << 7;
    a1 = a1 + v0;
    v0 = v1 << 5;
    v0 = v0 + v1;
    v0 = v0 << 2;
    v0 = v0 - v1;
    v0 = v0 << 2;
    v0 = v0 - v1;
    v0 = v0 << 2;
    v0 = v0 + v1;
    v1 = v0 << 5;
    a2 = v0 + v1;
    hi = (a2 & 0xffff_ffffL) * (a3 & 0xffff_ffffL) >>> 32;
    MEMORY.ref(2, s3).offset(0x7cL).setu(0);
    MEMORY.ref(2, s3).offset(0x12L).setu(t1);
    MEMORY.ref(4, t6).offset(-0x58acL).setu(a0);
    a0 = a0 - a1;
    a0 = a0 - 0x40L;
    v1 = MEMORY.ref(4, s3).offset(0x90L).get();
    v0 = -0x9L;
    MEMORY.ref(2, s3).offset(0x7aL).setu(a0);
    a0 = -0x7L;
    MEMORY.ref(4, t6).offset(-0x58acL).setu(a2);
    v1 = v1 & v0;
    a3 = hi;
    v0 = a2 - a3;
    v0 = v0 >>> 1;
    a3 = a3 + v0;
    a3 = a3 >>> 6;
    v0 = a3 << 1;
    v0 = v0 + a3;
    v0 = v0 << 3;
    v0 = v0 + a3;
    v0 = v0 << 2;
    v0 = v0 + a3;
    v0 = a2 - v0;
    v0 = v0 < 0x32L ? 1 : 0;
    v0 = v0 ^ 0x1L;
    v0 = v0 << 3;
    v1 = v1 | v0;
    v0 = MEMORY.ref(2, s3).offset(0x84L).get();
    v1 = v1 & a0;
    MEMORY.ref(4, s3).offset(0x90L).setu(v1);
    v1 = MEMORY.ref(2, s3).offset(0x88L).get();
    v0 = v0 << 8;
    MEMORY.ref(2, s3).offset(0x84L).setu(v0);
    v0 = MEMORY.ref(2, s3).offset(0x86L).get();
    v1 = v1 << 8;
    MEMORY.ref(2, s3).offset(0x88L).setu(v1);
    v0 = v0 << 8;
    MEMORY.ref(2, s3).offset(0x86L).setu(v0);
    v0 = 0x8012_0000L;
    v0 = v0 - 0x6710L;
    s5 = t2 + v0;
    v1 = MEMORY.ref(1, s5).offset(0x0L).get();
    v0 = 0x2L;
    if(v1 == v0) {
      s7 = s0;
      //LAB_801018c8
      v0 = a2 + 0x1L;
      a0 = v0 << 5;
      a0 = a0 + v0;
      a0 = a0 << 2;
      a0 = a0 - v0;
      a0 = a0 << 2;
      a0 = a0 - v0;
      a0 = a0 << 2;
      a0 = a0 + v0;
      v0 = a0 << 5;
      a0 = a0 + v0;
      v1 = a0 + 0x1L;
      v0 = v1 << 5;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v0 = v0 - v1;
      v0 = v0 << 2;
      v0 = v0 - v1;
      v0 = v0 << 2;
      v0 = v0 + v1;
      v1 = v0 << 5;
      s1 = MEMORY.ref(2, s6).offset(0x10L).get();
      v0 = v0 + v1;
      v1 = s1 & 0xffffL;
      v1 = v1 + 0x1L;
      hi = (v0 & 0xffff_ffffL) % (v1 & 0xffff_ffffL);
      s4 = hi;
      hi = (a0 & 0xffff_ffffL) * (t7 & 0xffff_ffffL) >>> 32;
      MEMORY.ref(4, t6).offset(-0x58acL).setu(v0);
      v1 = hi;
      v1 = v1 >>> 4;
      v0 = v1 << 12;
      v0 = v0 + v1;
      s2 = a0 - v0;
      a0 = s2;
      v0 = rcos(a0);
      lo = (long)(int)v0 * (int)s4 & 0xffff_ffffL;
      v0 = MEMORY.ref(2, s5).offset(0x2L).getSigned();
      a0 = s2;
      MEMORY.ref(2, s3).offset(0x52L).setu(0);
      v1 = lo;
      v0 = (int)v1 >> v0;
      MEMORY.ref(2, s3).offset(0x50L).setu(v0);
      v0 = rsin(a0);
      lo = (long)(int)v0 * (int)s4 & 0xffff_ffffL;
      v0 = MEMORY.ref(2, s5).offset(0x2L).getSigned();
      v1 = lo;
      v0 = (int)v1 >> v0;
      MEMORY.ref(2, s3).offset(0x54L).setu(v0);
    } else {
      s7 = s0;
      if((int)v1 >= 0x3L) {
        //LAB_80101824
        v0 = 0x3L;
        if(v1 == v0) {
          //LAB_80101990
          v1 = a2 + 0x1L;
          v0 = v1 << 5;
          v0 = v0 + v1;
          v0 = v0 << 2;
          v0 = v0 - v1;
          v0 = v0 << 2;
          v0 = v0 - v1;
          v0 = v0 << 2;
          v0 = v0 + v1;
          a0 = v0 << 5;
          v1 = MEMORY.ref(2, s5).offset(0x4L).getSigned();
          a1 = MEMORY.ref(2, s5).offset(0x2L).getSigned();
          v0 = v0 + a0;
          v1 = v1 - a1;
          v1 = v1 + 0x1L;
          hi = (v0 & 0xffff_ffffL) % (v1 & 0xffff_ffffL);
          a0 = hi;
          MEMORY.ref(4, t6).offset(-0x58acL).setu(v0);
          a0 = a0 + a1;
          MEMORY.ref(2, s3).offset(0x52L).setu(a0);
        } else {
          v0 = 0x4L;
          if(v1 == v0) {
            //LAB_801019e4
            v0 = a2 + 0x1L;
            a0 = v0 << 5;
            a0 = a0 + v0;
            a0 = a0 << 2;
            a0 = a0 - v0;
            a0 = a0 << 2;
            a0 = a0 - v0;
            a0 = a0 << 2;
            a0 = a0 + v0;
            v0 = a0 << 5;
            a0 = a0 + v0;
            hi = (a0 & 0xffff_ffffL) * (t7 & 0xffff_ffffL) >>> 32;
            a1 = 0x3f_0000L;
            a1 = a1 | 0xf801L;
            v1 = a0 + 0x1L;
            v0 = v1 << 5;
            v0 = v0 + v1;
            v0 = v0 << 2;
            v0 = v0 - v1;
            v0 = v0 << 2;
            v0 = v0 - v1;
            v0 = v0 << 2;
            v0 = v0 + v1;
            a2 = hi;
            v1 = v0 << 5;
            v0 = v0 + v1;
            hi = (v0 & 0xffff_ffffL) * (a1 & 0xffff_ffffL) >>> 32;
            s1 = MEMORY.ref(2, s6).offset(0x10L).get();
            MEMORY.ref(4, t6).offset(-0x58acL).setu(v0);
            a1 = a2 >>> 4;
            v1 = a1 << 12;
            v1 = v1 + a1;
            s2 = a0 - v1;
            t0 = hi;
            a0 = t0 >>> 1;
            v1 = a0 << 11;
            v1 = v1 + a0;
            s4 = v0 - v1;
            a0 = s2;
            v0 = rcos(a0);
            a0 = s4;
            s0 = v0;
            v0 = rsin(a0);
            lo = (long)(int)s0 * (int)v0 & 0xffff_ffffL;
            v0 = MEMORY.ref(2, s5).offset(0x2L).getSigned();
            v1 = lo;
            s1 = s1 & 0xffffL;
            v0 = (int)v1 >> v0;
            lo = (long)(int)v0 * (int)s1 & 0xffff_ffffL;
            a0 = s4;
            v0 = MEMORY.ref(2, s5).offset(0x4L).getSigned();
            v1 = lo;
            v0 = (int)v1 >> v0;
            MEMORY.ref(2, s3).offset(0x50L).setu(v0);
            v0 = rcos(a0);
            lo = (long)(int)v0 * (int)s1 & 0xffff_ffffL;
            a0 = s2;
            v0 = MEMORY.ref(2, s5).offset(0x4L).getSigned();
            v1 = lo;
            v0 = (int)v1 >> v0;
            MEMORY.ref(2, s3).offset(0x52L).setu(v0);
            v0 = rsin(a0);
            a0 = s4;
            s0 = v0;
            v0 = rsin(a0);
            lo = (long)(int)s0 * (int)v0 & 0xffff_ffffL;
            v0 = MEMORY.ref(2, s5).offset(0x2L).getSigned();
            v1 = lo;
            v0 = (int)v1 >> v0;
            lo = (long)(int)v0 * (int)s1 & 0xffff_ffffL;
            v0 = MEMORY.ref(2, s5).offset(0x4L).getSigned();

            //LAB_80101b04
            v1 = lo;
            v0 = (int)v1 >> v0;
            MEMORY.ref(2, s3).offset(0x54L).setu(v0);
          }
        }
      } else {
        v0 = 0x1L;
        if(v1 == v0) {
          //LAB_80101840
          v1 = a2 + 0x1L;
          v0 = v1 << 5;
          v0 = v0 + v1;
          v0 = v0 << 2;
          v0 = v0 - v1;
          v0 = v0 << 2;
          v0 = v0 - v1;
          v0 = v0 << 2;
          v0 = v0 + v1;
          v1 = v0 << 5;
          v0 = v0 + v1;
          hi = (v0 & 0xffff_ffffL) * (t7 & 0xffff_ffffL) >>> 32;
          s1 = MEMORY.ref(2, s6).offset(0x10L).get();
          MEMORY.ref(4, t6).offset(-0x58acL).setu(v0);
          v1 = hi;
          a0 = v1 >>> 4;
          v1 = a0 << 12;
          v1 = v1 + a0;
          s2 = v0 - v1;
          a0 = s2;
          v0 = rcos(a0);
          s0 = s1 & 0xffffL;
          lo = (long)(int)v0 * (int)s0 & 0xffff_ffffL;
          v0 = MEMORY.ref(2, s5).offset(0x2L).getSigned();
          a0 = s2;
          MEMORY.ref(2, s3).offset(0x52L).setu(0);
          v1 = lo;
          v0 = (int)v1 >> v0;
          MEMORY.ref(2, s3).offset(0x50L).setu(v0);
          v0 = rsin(a0);
          lo = (long)(int)v0 * (int)s0 & 0xffff_ffffL;
          v0 = MEMORY.ref(2, s5).offset(0x2L).getSigned();
          v1 = lo;
          v0 = (int)v1 >> v0;
          MEMORY.ref(2, s3).offset(0x54L).setu(v0);
        }
      }
    }

    //LAB_80101b10
    a1 = fp;
    a2 = s3;

    //LAB_80101b18
    a0 = sp3c;
    MEMORY.ref(4, a1).offset(0x8cL).deref(4).call(a0, a1, a2, s6);
    a0 = 0x8012_0000L;
    a0 = a0 - 0x6710L;
    v1 = s7 & 0xffffL;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    a1 = v0 + a0;
    v0 = MEMORY.ref(1, a1).offset(0x6L).get();
    a2 = 0x1L;
    if(v0 == a2) {
      v0 = MEMORY.ref(2, s3).offset(0x58L).getSigned();
      v1 = MEMORY.ref(2, s6).offset(0x18L).get();
      lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
      t0 = lo;
      v0 = MEMORY.ref(2, s3).offset(0x5aL).getSigned();
      lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
      a0 = lo;
      v0 = MEMORY.ref(2, s3).offset(0x5cL).getSigned();
      lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
      v0 = (int)t0 >> 8;
      MEMORY.ref(2, s3).offset(0x58L).setu(v0);
      v0 = (int)a0 >> 8;
      MEMORY.ref(2, s3).offset(0x5aL).setu(v0);
      v1 = lo;
      v0 = (int)v1 >> 8;
      MEMORY.ref(2, s3).offset(0x5cL).setu(v0);
    }

    //LAB_80101ba4
    v0 = MEMORY.ref(1, a1).offset(0x7L).get();

    if(v0 == a2) {
      a0 = 0x8010_0000L;
      v1 = MEMORY.ref(4, a0).offset(-0x58acL).get();
      v1 = v1 + 0x1L;
      v0 = v1 << 5;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v0 = v0 - v1;
      v0 = v0 << 2;
      v0 = v0 - v1;
      v0 = v0 << 2;
      v0 = v0 + v1;
      v1 = v0 << 5;
      v0 = v0 + v1;
      MEMORY.ref(4, a0).offset(-0x58acL).setu(v0);
      v1 = MEMORY.ref(1, a1).offset(0x9L).getSigned();
      a0 = MEMORY.ref(1, a1).offset(0x8L).getSigned();
      v1 = v1 - a0;
      v1 = v1 + 0x1L;
      hi = (v0 & 0xffff_ffffL) % (v1 & 0xffff_ffffL);
      v1 = hi;
      v1 = v1 + a0;
      v1 = v1 << 24;
      v1 = (int)v1 >> 24;
      MEMORY.ref(2, s3).offset(0xaL).setu(v1);
      MEMORY.ref(2, s3).offset(0xcL).setu(v1);
    }

    //LAB_80101c20
    v0 = MEMORY.ref(2, s3).offset(0x50L).get();
    v1 = MEMORY.ref(2, s3).offset(0x52L).get();
    a0 = MEMORY.ref(2, s3).offset(0x54L).get();
    MEMORY.ref(2, s3).offset(0x48L).setu(v0);
    MEMORY.ref(2, s3).offset(0x4aL).setu(v1);
    MEMORY.ref(2, s3).offset(0x4cL).setu(a0);
  }

  @Method(0x80102534L)
  public static void FUN_80102534() {
    long s0 = _8011a00c.get();

    //LAB_80102550
    while(s0 != 0) {
      final long s1 = addToLinkedListHead(MEMORY.ref(4, s0).offset(0x64L).get());

      if(s1 != 0) {
        final long a1 = MEMORY.ref(4, s0).offset(0x68L).get();

        if(s1 < a1) {
          memcpy(s1, a1, (int)MEMORY.ref(4, s0).offset(0x64L).get());
          removeFromLinkedList(MEMORY.ref(4, s0).offset(0x68L).get());
          MEMORY.ref(4, s0).offset(0x68L).setu(s1);
        } else {
          //LAB_801025a4
          removeFromLinkedList(s1);
        }
      }

      //LAB_801025b0
      s0 = MEMORY.ref(4, s0).offset(0x94L).get();
    }

    //LAB_801025c0
  }

  @Method(0x80105f98L)
  public static void FUN_80105f98(final int scriptIndex, final VECTOR a1, long a2) {
    final MATRIX sp0x10 = new MATRIX();
    final VECTOR sp0x30 = new VECTOR();

    final BtldScriptData27c v0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

    final GsCOORDINATE2 coord2;
    if(a2 == 0) {
      coord2 = v0._148.coord2ArrPtr_04.deref().get(1);
    } else {
      //LAB_80105fe4
      coord2 = v0._148.coord2_14;
    }

    //LAB_80105fec
    GsGetLw(coord2, sp0x10);
    a1.set(ApplyMatrixLV(sp0x10, sp0x30)).add(sp0x10.transfer);
  }

  @Method(0x80106050L)
  public static void FUN_80106050(final long a0, final long a1) {
    final long callback = getMethodAddress(Bttl.class, "FUN_800d46d4", Ref[].class);

    if(Math.abs((byte)a0) >= 0x2L) {
      FUN_800cff54(callback, 0x5L, 0x24L, 0x77L, 0x2bL, 0x1L, 0x80L);
      FUN_800cff54(callback, 0x5L, _800fb7bc.offset(1, 0x0L).offset(a1).get(), 0x73L, 0x30L, 0x1L, 0x80L);
    } else {
      //LAB_80106114
      FUN_800cff54(callback, 0x5L, 0x24L, 0x77L, 0x33L, 0x1L, 0x80L);
      FUN_800cff54(callback, 0x5L, _800fb7bc.offset(1, 0x2L).offset(a1).get(), 0x73L, 0x30L, 0x1L, 0x80L);
      FUN_800cff54(callback, 0x5L, 0x25L, 0x73L, 0x32L, 0x1L, 0x80L);
    }
  }

  @Method(0x801061bcL)
  public static long FUN_801061bc(final long a0, final long a1, final long a2, final long a3) {
    //LAB_80106264
    final long v0;
    if(a3 == 0x1L || a3 == 0x3L) {
      //LAB_80106274
      v0 = _800fb7c0.offset(a1 * 0x10L).offset(1, a2).get();
    } else {
      //LAB_8010628c
      v0 = FUN_800c7488(a0, a1, a2) & 0xffL;
    }

    //LAB_80106298
    return v0;
  }

  @Method(0x801062a8L)
  public static void FUN_801062a8(final long a0, final long a1, final long a2, final long a3) {
    long v0;
    long s3;
    long s6;
    long s7;
    long s5 = scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.getPointer();

    //LAB_8010633c
    for(s3 = 0; s3 < 8; s3++) {
      if((FUN_801061bc(MEMORY.ref(2, s5).offset(0x276L).getSigned(), s3, 0x1L, a3) & 0xffL) == 0) {
        break;
      }
    }

    //LAB_80106374
    v0 = s3 - 0x1L;
    MEMORY.ref(1, a2).offset(0x30L).setu(v0);
    MEMORY.ref(4, a2).offset(0x0L).setu(a0);
    MEMORY.ref(4, a2).offset(0x4L).setu(a1);
    MEMORY.ref(2, a2).offset(0x34L).setu(0);
    MEMORY.ref(1, a2).offset(0x31L).setu(0);
    MEMORY.ref(1, a2).offset(0x32L).setu(0);
    MEMORY.ref(1, a2).offset(0x38L).setu(0);
    MEMORY.ref(1, a2).offset(0x39L).setu(0);
    MEMORY.ref(1, a2).offset(0x3aL).setu(a3);
    MEMORY.ref(4, a2).offset(0x40L).setu(addToLinkedListTail((v0 & 0xffL) * 0x20L));
    s6 = FUN_801061bc(MEMORY.ref(2, s5).offset(0x276L).getSigned(), 0, 0xfL, a3) & 0xffL;
    MEMORY.ref(2, a2).offset(0x36L).setu(s6);

    if(MEMORY.ref(1, a2).offset(0x30L).get() != 0) {
      s7 = MEMORY.ref(4, a2).offset(0x40L).get();

      //LAB_801063f0
      for(s3 = 0; s3 < MEMORY.ref(1, a2).offset(0x30L).get(); s3++) {
        MEMORY.ref(1, s7).offset(0x0L).setu(0x1L);
        MEMORY.ref(1, s7).offset(0x1L).setu(0);
        MEMORY.ref(2, s7).offset(0x8L).setu(0);
        MEMORY.ref(2, s7).offset(0x10L).setu(s6 + 0x2L);
        _800fa754.setu((_800fa754.get() + 0x1L) * 0x10dcdL);
        MEMORY.ref(1, s7).offset(0x2L).setu(0x3L);
        MEMORY.ref(1, s7).offset(0x1cL).setu(0);
        _8011a014.offset(s3).offset(1, 0x0L).setu(0);
        v0 = FUN_801061bc(MEMORY.ref(2, s5).offset(0x276L).getSigned(), s3, 0x1L, a3) & 0xffL;
        s6 = s6 + v0;
        MEMORY.ref(2, s7).offset(0xaL).setu(v0);
        v0 = FUN_801061bc(MEMORY.ref(2, s5).offset(0x276L).getSigned(), s3, 0x2L, a3) & 0xffL;
        MEMORY.ref(2, s7).offset(0xcL).setu(v0);
        v0 = FUN_801061bc(MEMORY.ref(2, s5).offset(0x276L).getSigned(), s3, 0x3L, a3) & 0xffL;
        MEMORY.ref(2, s7).offset(0xeL).setu(v0);
        long a3_0 = MEMORY.ref(2, s7).offset(0x10L).get() + MEMORY.ref(2, s7).offset(0xcL).get();
        MEMORY.ref(2, s7).offset(0x10L).setu(a3_0 - (short)v0 / 2 + 1);
        MEMORY.ref(2, s7).offset(0x12L).setu(a3_0 + v0 - (short)MEMORY.ref(2, s7).offset(0xeL).get() / 2);

        a3_0 = addToLinkedListTail(0xeeL);
        MEMORY.ref(4, s7).offset(0x18L).setu(a3_0);

        //LAB_8010652c
        for(int i = 16; i >= 0; i--) {
          MEMORY.ref(2, a3_0).offset(0x8L).setu((18 - i) * 10);
          MEMORY.ref(1, a3_0).offset(0x0L).setu(0x1L);
          //LAB_8010656c
          //LAB_80106574
          MEMORY.ref(2, a3_0).offset(0x2L).setu((16 - i) * 0x80 + 0x200L);
          MEMORY.ref(1, a3_0).offset(0xcL).setu(0x5L);
          MEMORY.ref(1, a3_0).offset(0xdL).setu(0);
          MEMORY.ref(2, a3_0).offset(0xaL).setu(MEMORY.ref(2, s7).offset(0x10L).get() + (MEMORY.ref(2, s7).offset(0xeL).getSigned() - 0x1L) / 2 + i - 0x11L);
          MEMORY.ref(1, a3_0).offset(0x4L).setu(_800fb7f0.offset(1, MEMORY.ref(1, s7).offset(0x2L).getSigned() * 3).offset(0x0L).get());
          MEMORY.ref(1, a3_0).offset(0x5L).setu(_800fb7f0.offset(1, MEMORY.ref(1, s7).offset(0x2L).getSigned() * 3).offset(0x1L).get());
          MEMORY.ref(1, a3_0).offset(0x6L).setu(_800fb7f0.offset(1, MEMORY.ref(1, s7).offset(0x2L).getSigned() * 3).offset(0x2L).get());
          a3_0 = a3_0 + 0xeL;
        }

        a3_0 = a3_0 - 0xeL;

        //LAB_80106634
        for(int i = 0; i < 3; i++) {
          MEMORY.ref(2, a3_0).offset(0x8L).setu(0x14L - i * 0x2L);
          MEMORY.ref(1, a3_0).offset(0x0L).setu(0x1L);
          MEMORY.ref(2, a3_0).offset(0x2L).setu(0x200L);
          MEMORY.ref(1, a3_0).offset(0xcL).setu(0x11L);
          MEMORY.ref(1, a3_0).offset(0xdL).setu(0x1L);
          MEMORY.ref(2, a3_0).offset(0xaL).setu(MEMORY.ref(2, s7).offset(0x10L).get() - 0x11L);

          if(i != 0x1L) {
            MEMORY.ref(1, a3_0).offset(0x4L).setu(0x30L);
            MEMORY.ref(1, a3_0).offset(0x5L).setu(0x30L);
            MEMORY.ref(1, a3_0).offset(0x6L).setu(0x30L);
          } else {
            //LAB_80106680
            MEMORY.ref(1, a3_0).offset(0xdL).setu(0xffL);
          }

          //LAB_80106684
          a3_0 = a3_0 - 0xeL;
        }

        MEMORY.ref(4, s7).offset(0x14L).setu(a3_0);
        s7 = s7 + 0x20L;
      }
    }

    //LAB_801066c8
    FUN_80105f98((int)MEMORY.ref(4, a2).offset(0x0L).get(), MEMORY.ref(4, a2 + 0x10L, VECTOR::new), 0); //TODO

    final VECTOR sp0x10 = new VECTOR();
    FUN_80105f98((int)MEMORY.ref(4, a2).offset(0x4L).get(), sp0x10, 0x1L);

    final int a0_0 = (int)MEMORY.ref(4, a2).offset(0x40L).deref(4).offset(0x10L).getSigned();
    MEMORY.ref(4, a2).offset(0x20L).setu(sp0x10.getX() - MEMORY.ref(4, a2).offset(0x10L).getSigned() / a0_0);
    MEMORY.ref(4, a2).offset(0x24L).setu(sp0x10.getY() - MEMORY.ref(4, a2).offset(0x14L).getSigned() / a0_0);
    MEMORY.ref(4, a2).offset(0x28L).setu(sp0x10.getZ() - MEMORY.ref(4, a2).offset(0x18L).getSigned() / a0_0);
  }

  @Method(0x80106774L)
  public static long FUN_80106774(final long a0, final long a1) {
    long v1 = 0;
    long v0 = MEMORY.ref(1, a0).offset(0x4L).get() - a1;
    final long sp10;
    if((int)v0 > 0) {
      sp10 = v0;
    } else {
      sp10 = 0;
      v1++;
    }

    //LAB_801067b0
    v0 = MEMORY.ref(1, a0).offset(0x5L).get() - a1;
    final long sp14;
    if((int)v0 > 0) {
      sp14 = v0;
    } else {
      sp14 = 0;
      v1++;
    }

    //LAB_801067c4
    v0 = MEMORY.ref(1, a0).offset(0x6L).get() - a1;
    final long sp18;
    if((int)v0 > 0) {
      sp18 = v0;
    } else {
      sp18 = 0;
      v1++;
    }

    //LAB_801067d8
    MEMORY.ref(1, a0).offset(0x4L).setu(sp10);
    MEMORY.ref(1, a0).offset(0x5L).setu(sp14);
    MEMORY.ref(1, a0).offset(0x6L).setu(sp18);
    return v1;
  }

  @Method(0x80106808L)
  public static void FUN_80106808(long a0, long a1, long a2, ScriptState<BttlScriptData6c> a3, final BttlScriptData6c a4) {
    long v0;
    long t0;
    long s1;
    long s3;
    long s4;
    long s5;

    if((int)a4._10.get() >= 0) {
      long s6 = a2;
      v0 = MEMORY.ref(4, a1).offset(0x14L).get();
      s4 = v0 + 0x1cL;

      //LAB_8010685c
      for(s5 = 0; s5 < 2; s5++) {
        s1 = 0;
        s3 = MEMORY.ref(2, s4).offset(0x8L).getSigned() - s5 * 0x8L;

        //LAB_80106874
        final long[] sp0x18 = new long[8];
        for(int i = 0; i < 4; i++) {
          v0 = rcos(MEMORY.ref(2, s4).offset(0x2L).getSigned() + s1);
          t0 = ((long)(int)v0 * (int)s3) & 0xffff_ffffL;
          v0 = (int)t0 >> 12;
          sp0x18[i] = v0;
          v0 = rsin(MEMORY.ref(2, s4).offset(0x2L).getSigned() + s1);
          t0 = ((long)(int)v0 * (int)s3) & 0xffff_ffffL;
          v0 = (int)t0 >> 12;
          sp0x18[i + 1] = v0 + 0x1eL;
          s1 = s1 + 0x400L;
        }

        a0 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x18L);
        MEMORY.ref(4, a0).offset(0x4L).setu(0x2880_8080L);
        MEMORY.ref(1, a0).offset(0x3L).setu(0x5L);
        v0 = MEMORY.ref(1, a0).offset(0x7L).get() | 0x2L;
        v0 = v0 << 24;
        v0 = v0 | 0x80_8080L;
        MEMORY.ref(4, a0).offset(0x4L).setu(v0);
        if(s6 == 0x1L) {
          MEMORY.ref(1, a0).offset(0x4L).setu(0xffL);
          MEMORY.ref(1, a0).offset(0x5L).setu(0xffL);
          MEMORY.ref(1, a0).offset(0x6L).setu(0xffL);
          //LAB_80106918
        } else if(s6 != -0x2L) {
          //LAB_80106988
          MEMORY.ref(1, a0).offset(0x4L).setu(0x30L);
          MEMORY.ref(1, a0).offset(0x5L).setu(0x30L);
          MEMORY.ref(1, a0).offset(0x6L).setu(0x30L);
        } else if(MEMORY.ref(1, a1).offset(0x1cL).getSigned() != 0) {
          MEMORY.ref(1, a0).offset(0x4L).setu(MEMORY.ref(1, s4).offset(-0xaL).get() * 3);
          MEMORY.ref(1, a0).offset(0x5L).setu(MEMORY.ref(1, s4).offset(-0x9L).get());
          MEMORY.ref(1, a0).offset(0x6L).setu((MEMORY.ref(1, s4).offset(-0x8L).get() - 0x1L) * 0x8L);
        } else {
          //LAB_80106964
          MEMORY.ref(1, a0).offset(0x4L).setu(MEMORY.ref(1, s4).offset(-0xaL).get());
          MEMORY.ref(1, a0).offset(0x5L).setu(MEMORY.ref(1, s4).offset(-0x9L).get());
          MEMORY.ref(1, a0).offset(0x6L).setu(MEMORY.ref(1, s4).offset(-0x8L).get());
        }

        //LAB_80106994
        MEMORY.ref(2, a0).offset(0x8L).setu(sp0x18[0]);
        MEMORY.ref(2, a0).offset(0xaL).setu(sp0x18[1]);
        MEMORY.ref(2, a0).offset(0xcL).setu(sp0x18[2]);
        MEMORY.ref(2, a0).offset(0xeL).setu(sp0x18[3]);
        MEMORY.ref(2, a0).offset(0x10L).setu(sp0x18[6]);
        MEMORY.ref(2, a0).offset(0x12L).setu(sp0x18[7]);
        MEMORY.ref(2, a0).offset(0x14L).setu(sp0x18[4]);
        MEMORY.ref(2, a0).offset(0x16L).setu(sp0x18[5]);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, a0);
        a2 = 0;
      }

      SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, a2, a2), null);
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
      linkedListAddress_1f8003d8.addu(0xcL);
    }

    //LAB_80106a4c
  }

  @Method(0x80106ac4L)
  public static void FUN_80106ac4(long a0, long a1, long a2) {
    long v0;
    long s0;
    long s2;
    s2 = a1 + 0x400L;
    s0 = a2 - 0x1L;
    long sp10 = (rcos(a1) * s0) >> 12;
    long sp14 = (rsin(a1) * s0) >> 12;
    long sp18 = (rcos(s2) * s0) >> 12;
    long sp1c = (rsin(s2) * s0) >> 12;
    s0 = a2 - 0xbL;
    long sp20 = (rcos(a1) * s0) >> 12;
    long sp24 = (rsin(a1) * s0) >> 12;
    long sp28 = (rcos(s2) * s0) >> 12;
    long sp2c = (rsin(s2) * s0) >> 12;
    final long a1_0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x24L);
    MEMORY.ref(4, a1_0).offset(0x4L).setu(0x3880_8080L);
    MEMORY.ref(1, a1_0).offset(0x3L).setu(0x8L);
    MEMORY.ref(1, a1_0).offset(0x14L).setu(0);
    MEMORY.ref(1, a1_0).offset(0x15L).setu(0);
    MEMORY.ref(1, a1_0).offset(0x16L).setu(0);
    MEMORY.ref(1, a1_0).offset(0x1cL).setu(0);
    MEMORY.ref(1, a1_0).offset(0x1dL).setu(0);
    MEMORY.ref(1, a1_0).offset(0x1eL).setu(0);
    v0 = MEMORY.ref(1, a1_0).offset(0x7L).get() | 0x2L;
    v0 = v0 << 24;
    v0 = v0 | 0x80_8080L;
    final long a0_0 = MEMORY.ref(2, a0).offset(0x8L).getSigned() * 0x4L;
    MEMORY.ref(4, a1_0).offset(0x4L).setu(v0);
    MEMORY.ref(1, a1_0).offset(0x4L).setu(a0_0);
    MEMORY.ref(1, a1_0).offset(0x5L).setu(a0_0);
    MEMORY.ref(1, a1_0).offset(0x6L).setu(a0_0);
    MEMORY.ref(1, a1_0).offset(0xcL).setu(a0_0);
    MEMORY.ref(1, a1_0).offset(0xdL).setu(a0_0);
    MEMORY.ref(1, a1_0).offset(0xeL).setu(a0_0);
    MEMORY.ref(2, a1_0).offset(0x8L).setu(sp10);
    MEMORY.ref(2, a1_0).offset(0xaL).setu(sp14 + 0x1eL);
    MEMORY.ref(2, a1_0).offset(0x10L).setu(sp18);
    MEMORY.ref(2, a1_0).offset(0x12L).setu(sp1c + 0x1eL);
    MEMORY.ref(2, a1_0).offset(0x18L).setu(sp20);
    MEMORY.ref(2, a1_0).offset(0x1aL).setu(sp24 + 0x1eL);
    MEMORY.ref(2, a1_0).offset(0x20L).setu(sp28);
    MEMORY.ref(2, a1_0).offset(0x22L).setu(sp2c + 0x1eL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, a1_0);
  }

  @Method(0x80106cccL)
  public static void FUN_80106ccc(final long a0, final long a1, final long a2, final long a3, final ScriptState<BttlScriptData6c> a4) {
    long s7 = MEMORY.ref(4, a3).offset(0x18L).get();
    long sp28 = _8011a014.offset(a1).getAddress();
    long s3 = s7 + 0x2L;

    //LAB_80106d18
    for(long s6 = 0; s6 < 17; s6++) {
      if(MEMORY.ref(1, s7).offset(0x0L).getSigned() != 0) {
        if(MEMORY.ref(2, s3).offset(0x8L).getSigned() <= 0) {
          long s2 = MEMORY.ref(2, s3).offset(0x6L).getSigned();
          long sp20 = rcos(MEMORY.ref(2, s3).offset(0x0L).getSigned()) * s2 >> 12;
          long sp24 = (rsin(MEMORY.ref(2, s3).offset(0x0L).getSigned()) * s2 >> 12) + 0x1eL;

          //LAB_80106d80
          long s4 = 0;
          for(long s5 = 0; s5 < 4; s5++) {
            long s1 = linkedListAddress_1f8003d8.get();
            linkedListAddress_1f8003d8.addu(0x10L);
            MEMORY.ref(1, s1).offset(0x3L).setu(0x3L);
            MEMORY.ref(4, s1).offset(0x4L).setu(0x4080_8080L);
            long v1 = MEMORY.ref(1, s3).offset(0xbL).get();

            //LAB_80106dc0
            long v0;
            if(v1 != 0 && v1 != 0xffL || MEMORY.ref(1, sp28).offset(0x0L).getSigned() < 0) {
              //LAB_80106de8
              v0 = MEMORY.ref(1, s1).offset(0x7L).get() | 0x2L;
            } else {
              v0 = MEMORY.ref(1, s1).offset(0x7L).get() & 0xfdL;
            }

            //LAB_80106df4
            MEMORY.ref(4, s1).offset(0x4L).setu(v0 << 24 | 0x80_8080L);

            if(MEMORY.ref(1, a3).offset(0x1cL).getSigned() != 0 && s6 != 0x10L) {
              MEMORY.ref(1, s1).offset(0x4L).setu(MEMORY.ref(1, s3).offset(0x2L).get() * 3);
              MEMORY.ref(1, s1).offset(0x5L).setu(MEMORY.ref(1, s3).offset(0x3L).get());
              MEMORY.ref(1, s1).offset(0x6L).setu((MEMORY.ref(1, s3).offset(0x4L).get() + 1) / 8);
            } else {
              //LAB_80106e58
              MEMORY.ref(1, s1).offset(0x4L).setu(MEMORY.ref(1, s3).offset(0x2L).get());
              MEMORY.ref(1, s1).offset(0x5L).setu(MEMORY.ref(1, s3).offset(0x3L).get());
              MEMORY.ref(1, s1).offset(0x6L).setu(MEMORY.ref(1, s3).offset(0x4L).get());
            }

            //LAB_80106e74
            s4 = s4 + 0x400L;
            long sp18 = rcos(MEMORY.ref(2, s3).offset(0x0L).getSigned() + s4) * s2 >> 12;
            long sp1c = (rsin(MEMORY.ref(2, s3).offset(0x0L).getSigned() + s4) * s2 >> 12) + 0x1eL;
            MEMORY.ref(2, s1).offset(0x8L).setu(sp20);
            MEMORY.ref(2, s1).offset(0xaL).setu(sp24);
            MEMORY.ref(2, s1).offset(0xcL).setu(sp18);
            MEMORY.ref(2, s1).offset(0xeL).setu(sp1c);
            insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, s1);
            sp20 = sp18;
            sp24 = sp1c;
          }

          if(MEMORY.ref(1, s3).offset(0xbL).get() == 0) {
            FUN_80106ac4(a3, MEMORY.ref(2, s3).offset(0x0L).get() + s4         , s2);
            FUN_80106ac4(a3, MEMORY.ref(2, s3).offset(0x0L).get() + s4 + 0x400L, s2);
            FUN_80106ac4(a3, MEMORY.ref(2, s3).offset(0x0L).get() + s4 + 0x800L, s2);
            FUN_80106ac4(a3, MEMORY.ref(2, s3).offset(0x0L).get() + s4 + 0xc00L, s2);
          }
        }
      }

      //LAB_80106fac
      s3 = s3 + 0xeL;
      s7 = s7 + 0xeL;
    }

    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);

    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x2L, 0, 0), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);
  }

  @Method(0x80107088L)
  public static long FUN_80107088(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long s0;
    long s1;
    long s2;
    long s4;
    long s5;
    v1 = MEMORY.ref(2, a2).offset(0x34L).getSigned();
    v0 = MEMORY.ref(2, a3).offset(0x10L).getSigned() - 0x11L;
    s4 = 0;
    if((int)v1 >= (int)v0) {
      v0 = MEMORY.ref(2, a3).offset(0x8L).get() + 0x1L;
      MEMORY.ref(2, a3).offset(0x8L).setu(v0);
      v0 = v0 << 16;
      v0 = (int)v0 >> 16;
      if((int)v0 >= 0xeL) {
        MEMORY.ref(2, a3).offset(0x8L).setu(0xdL);
      }
    }

    //LAB_801070ec
    s1 = MEMORY.ref(4, a3).offset(0x18L).get();
    s2 = 0;
    v0 = 0x8012_0000L;
    v0 = v0 - 0x5fecL;
    s5 = a1 + v0;
    s0 = s1 + 0xcL;

    //LAB_80107104
    do {
      v0 = MEMORY.ref(1, s5).offset(0x0L).getSigned();

      if((int)v0 < 0) {
        v0 = MEMORY.ref(2, a3).offset(0x8L).get() - 0x3L;
        MEMORY.ref(2, a3).offset(0x8L).setu(v0);
        v0 = v0 << 16;
        if((int)v0 < 0) {
          MEMORY.ref(2, a3).offset(0x8L).setu(0);
        }

        //LAB_80107134
        if(FUN_80106774(s1, 0x20L) == 0x3L) {
          MEMORY.ref(1, s1).offset(0x0L).setu(0);
        }
      }

      //LAB_80107150
      if(MEMORY.ref(1, s1).offset(0x0L).getSigned() != 0) {
        v0 = MEMORY.ref(2, s0).offset(-0x2L).getSigned();
        v1 = MEMORY.ref(2, s0).offset(-0x2L).get();
        if((int)v0 > 0) {
          v0 = v1 - 0x1L;
          MEMORY.ref(2, s0).offset(-0x2L).setu(v0);
        } else {
          //LAB_80107178
          v1 = MEMORY.ref(1, s0).offset(0x1L).get();
          s4 = 0x1L;
          if(v1 != 0xffL) {
            v0 = v1 + 0x1L;
            MEMORY.ref(1, s0).offset(0x1L).setu(v0);
          }

          //LAB_80107190
          v0 = MEMORY.ref(1, s0).offset(0x0L).get() - 0x1L;
          MEMORY.ref(1, s0).offset(0x0L).setu(v0);
          v0 = v0 << 24;
          if(v0 == 0) {
            MEMORY.ref(1, s1).offset(0x0L).setu(0);
          }

          //LAB_801071b0
          if((int)s2 < 0xeL) {
            FUN_80106774(s1, 0x4eL);
          }
        }
      }

      //LAB_801071c0
      s2 = s2 + 0x1L;
      s0 = s0 + 0xeL;
      s1 = s1 + 0xeL;
    } while((int)s2 < 0x11L);

    return s4;
  }

  @Method(0x801071fcL)
  public static void FUN_801071fc(long a0, long a1, long a2) {
    long v0;
    long a3;
    long t0;
    v0 = 0x8012_0000L;
    t0 = v0 - 0x5fecL;
    v0 = a2 + t0;
    a3 = MEMORY.ref(1, v0).offset(0x0L).getSigned();
    MEMORY.ref(1, a0).offset(0x32L).setu(0x1L);

    a1 = a1 + 0x20L + 0xcL;

    //LAB_80107234
    a2 = a2 + 0x1L;
    for(; a2 < MEMORY.ref(1, a0).offset(0x30L).get(); a2++) {
      v0 = a2 + t0;
      MEMORY.ref(2, a1).offset(0x6L).setu(-0x1L);
      MEMORY.ref(2, a1).offset(0x4L).setu(-0x1L);
      MEMORY.ref(2, a1).offset(0x2L).setu(0);
      MEMORY.ref(2, a1).offset(0x0L).setu(0);
      MEMORY.ref(1, v0).offset(0x0L).setu(a3);
      a1 = a1 + 0x20L;
    }

    //LAB_80107264
  }

  @Method(0x8010726cL)
  public static void FUN_8010726c(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    long v1;
    long s0;
    long s1;
    final long s2 = data._44.get();

    if(MEMORY.ref(1, s2).offset(0x31L).get() != 0x1L) {
      if((int)data._10.get() >= 0) {
        s1 = MEMORY.ref(4, s2).offset(0x40L).get();

        //LAB_801072c4
        for(s0 = 0; s0 < MEMORY.ref(1, s2).offset(0x30L).get(); s0++) {
          FUN_80106ccc(MEMORY.ref(1, s1).offset(0x2L).getSigned(), s0, s2, s1, state);
          s1 = s1 + 0x20L;
        }

        //LAB_801072f4
        s1 = MEMORY.ref(4, s2).offset(0x40L).get();

        //LAB_8010730c
        for(s0 = 0; s0 < MEMORY.ref(1, s2).offset(0x30L).get(); s0++) {
          if(_8011a014.offset(1, s0).getSigned() == 0) {
            break;
          }

          s1 = s1 + 0x20L;
        }

        //LAB_80107330
        if((int)s0 < MEMORY.ref(1, s2).offset(0x30L).get()) {
          FUN_80106050((byte)(MEMORY.ref(2, s1).offset(0x10L).getSigned() + (MEMORY.ref(2, s1).offset(0x12L).getSigned() - MEMORY.ref(2, s1).offset(0x10L).getSigned()) / 2 - MEMORY.ref(1, s2).offset(0x34L).get() - 0x1L), MEMORY.ref(1, s1).offset(0x1cL).getSigned());

          v1 = MEMORY.ref(2, s2).offset(0x34L).getSigned();
          if(MEMORY.ref(2, s1).offset(0x10L).getSigned() <= (int)v1 && MEMORY.ref(2, s1).offset(0x12L).getSigned() >= (int)v1) {
            FUN_80106808(s2, s1, -0x2L, state, data);
          }
        }
      }
    }

    //LAB_801073b4
  }

  @Method(0x801073d4L)
  public static void FUN_801073d4(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    long v0;
    long v1;
    long a0;
    long s0;
    long s1;
    long s2;
    long s4;
    long s3 = data._44.get();
    v0 = MEMORY.ref(1, s3).offset(0x31L).get();

    if(v0 == 0) {
      s4 = 0x1L;
      s0 = 0;
      s2 = MEMORY.ref(4, s3).offset(0x40L).get();
      v0 = MEMORY.ref(2, s3).offset(0x34L).get();
      v1 = MEMORY.ref(1, s3).offset(0x30L).get();
      MEMORY.ref(2, s3).offset(0x34L).setu(v0 + 0x1L);
      if(v1 != 0) {
        v0 = 0x8012_0000L;
        s1 = v0 - 0x5fecL;

        //LAB_80107440
        do {
          if(MEMORY.ref(2, s3).offset(0x34L).getSigned() != MEMORY.ref(2, s2).offset(0x12L).getSigned() + 0x1L) {
            if(MEMORY.ref(1, s1).offset(0x0L).getSigned() == 0) {
              s4 = 0;
            }
          } else {
            if(MEMORY.ref(1, s1).offset(0x0L).getSigned() == 0) {
              MEMORY.ref(1, s1).offset(0x0L).setu(-0x2L);
              FUN_801071fc(s3, s2, s0);

              //LAB_80107478
              if(MEMORY.ref(1, s1).offset(0x0L).getSigned() == 0) {
                s4 = 0;
              }
            }
          }

          //LAB_8010748c
          s1 = s1 + 0x1L;
          s0 = s0 + 0x1L;
          v0 = MEMORY.ref(1, s3).offset(0x30L).get();
          s2 = s2 + 0x20L;
        } while((int)s0 < (int)v0);
      }

      //LAB_801074a8
      if(s4 != 0) {
        FUN_801071fc(s3, s2, s0);
      }

      //LAB_801074bc
      s1 = 0;
      v0 = MEMORY.ref(1, s3).offset(0x30L).get();
      s2 = MEMORY.ref(4, s3).offset(0x40L).get();
      if(v0 != 0) {
        s0 = 0;

        //LAB_801074d0
        do {
          v0 = FUN_80107088(MEMORY.ref(1, s2).offset(0x2L).getSigned(), s0, s3, s2);
          v1 = MEMORY.ref(1, s3).offset(0x30L).get();
          s1 = s1 + v0;
          s2 = s2 + 0x20L;
          s0 = s0 + 0x1L;
        } while((int)s0 < (int)v1);
      }

      //LAB_80107500
      if(s1 == 0 && MEMORY.ref(1, s3).offset(0x32L).getSigned() != 0) {
        v0 = 0x8012_0000L;
        MEMORY.ref(1, v0).offset(-0x60bfL).setu(0);
        v0 = MEMORY.ref(1, s3).offset(0x30L).get();
        s2 = MEMORY.ref(4, s3).offset(0x40L).get();
        if(v0 != 0) {
          s0 = 0;

          //LAB_8010752c
          do {
            a0 = MEMORY.ref(4, s2).offset(0x18L).get();
            s0 = s0 + 0x1L;
            removeFromLinkedList(a0);
            v0 = MEMORY.ref(1, s3).offset(0x30L).get();
            s2 = s2 + 0x20L;
          } while((int)s0 < (int)v0);
        }

        //LAB_80107554
        FUN_80015d38(index);
      } else {
        //LAB_8010756c
        v0 = MEMORY.ref(2, s3).offset(0x34L).getSigned();

        if((int)v0 >= 0x9L) {
          v1 = MEMORY.ref(1, s3).offset(0x30L).get();
          s2 = MEMORY.ref(4, s3).offset(0x40L).get();
          if(v1 != 0) {
            s0 = 0;

            //LAB_80107598
            do {
              v0 = _8011a014.offset(s0).getAddress();
              v0 = MEMORY.ref(1, v0).offset(0x0L).getSigned();

              if(v0 == 0) {
                break;
              }

              s0 = s0 + 0x1L;
              s2 = s2 + 0x20L;
            } while((int)s0 < (int)v1);

            //LAB_801075bc
            if((int)s0 < MEMORY.ref(1, s3).offset(0x30L).get()) {
              if(state.storage_44.get(8).get() != 0) {
                MEMORY.ref(1, s2).offset(0x1cL).setu(0x1L);
                state.storage_44.get(8).set(0);
              }

              //LAB_801075e8
              v1 = MEMORY.ref(1, s3).offset(0x3aL).get();

              v0 = v1 - 0x1L;
              if(v0 >= 0x2L) {
                v1 = v1 & 0xffL;
                //LAB_8010763c
                if(v1 != 0x1) {
                  if(v1 != 0x3L) {
                    if(MEMORY.ref(1, s2).offset(0x1cL).getSigned() == 0) {
                      a0 = 0x2L;
                    } else {
                      a0 = 0x4L;
                    }

                    //LAB_80107664
                    v0 = 0x8008_0000L;
                    v0 = MEMORY.ref(4, v0).offset(-0x5c68L).get();

                    v1 = v0 >>> 4;
                    v0 = v1 & 0x6L;
                    if(v0 != 0) {
                      _8011a014.offset(1, s0).offset(0x0L).setu(-0x1L);
                      if((v1 & a0) == 0 || (v1 & ~a0) != 0) {
                        //LAB_801076d8
                        //LAB_801076dc
                        v0 = 0x8012_0000L;
                        v0 = v0 - 0x5fecL;
                        v0 = s0 + v0;
                        MEMORY.ref(1, v0).offset(0x0L).setu(-0x3L);
                      } else {
                        v1 = MEMORY.ref(2, s3).offset(0x34L).getSigned();

                        if((int)v1 >= MEMORY.ref(2, s2).offset(0x10L).getSigned() && (int)v1 <= MEMORY.ref(2, s2).offset(0x12L).getSigned()) {
                          _8011a014.offset(1, s0).offset(0x0L).setu(0x1L);
                          MEMORY.ref(1, s2).offset(0x1L).setu(0x1L);
                        }
                      }

                      //LAB_801076f0
                      v0 = 0x8012_0000L;
                      v0 = v0 - 0x5fecL;
                      v0 = s0 + v0;
                      v0 = MEMORY.ref(1, v0).offset(0x0L).getSigned();

                      if((int)v0 < 0) {
                        FUN_801071fc(s3, s2, s0);
                      }

                      //LAB_80107718
                      //LAB_8010771c
                      MEMORY.ref(1, s3).offset(0x38L).setu(0x2L);
                      MEMORY.ref(1, s3).offset(0x39L).setu(s0);
                      MEMORY.ref(4, s3).offset(0x3cL).setu(s2);
                    }
                  }
                }
              } else {
                v1 = MEMORY.ref(2, s3).offset(0x34L).getSigned();

                if((int)v1 >= MEMORY.ref(2, s2).offset(0x10L).getSigned() && (int)v1 <= MEMORY.ref(2, s2).offset(0x12L).getSigned()) {
                  v1 = _8011a014.offset(s0).getAddress();
                  MEMORY.ref(1, v1).offset(0x0L).setu(0x1L);
                  MEMORY.ref(1, s2).offset(0x1L).setu(0x1L);
                  MEMORY.ref(1, s3).offset(0x38L).setu(0x2L);
                  MEMORY.ref(1, s3).offset(0x39L).setu(s0);
                  MEMORY.ref(4, s3).offset(0x3cL).setu(s2);
                }
              }
            }
          }

          //LAB_80107728
          v0 = MEMORY.ref(1, s3).offset(0x38L).get();

          if(v0 != 0) {
            v0 = v0 - 0x1L;
            MEMORY.ref(1, s3).offset(0x38L).setu(v0);
            v0 = 0x8012_0000L;
            v1 = MEMORY.ref(1, s3).offset(0x39L).get();
            v0 = v0 - 0x5fecL;
            v1 = v1 + v0;
            FUN_80106808(s3, MEMORY.ref(4, s3).offset(0x3cL).get(), MEMORY.ref(1, v1).offset(0x0L).getSigned(), state, data);
          }
        }
      }
    }

    //LAB_80107764
  }

  @Method(0x80107790L)
  public static void FUN_80107790(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    assert false;
  }

  @Method(0x801077e8L)
  public static long FUN_801077e8(final RunningScript s1) {
    final long s2 = FUN_800e80c4(
      s1.scriptStateIndex_00.get(),
      0x44L,
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801073d4", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_8010726c", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80107790", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new)
    );

    final ScriptState<BttlScriptData6c> v0 = scriptStatePtrArr_800bc1c0.get((int)s2).derefAs(ScriptState.classFor(BttlScriptData6c.class));
    FUN_801062a8(s1.params_20.get(0).deref().get(), s1.params_20.get(1).deref().get(), v0.innerStruct_00.deref()._44.get(), s1.params_20.get(2).deref().get());
    v0.storage_44.get(8).set(0);
    s1.params_20.get(4).deref().set(s2);
    _80119f41.setu(0x1L);
    return 0;
  }

  @Method(0x80110030L)
  public static VECTOR FUN_80110030(final int scriptIndex) {
    final long a0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.getPointer(); //TODO

    if(MEMORY.ref(4, a0).offset(0x0L).get() == 0x2020_4d45L) {
      return MEMORY.ref(4, a0 + 0x14L, VECTOR::new);
    }

    //LAB_8011006c
    return MEMORY.ref(4, a0 + 0x174L, VECTOR::new);
  }

  @Method(0x80110074L)
  public static SVECTOR FUN_80110074(final long a0) {
    final long data = scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.getPointer(); //TODO
    return MEMORY.ref(2, MEMORY.ref(4, data).offset(0x0L).get() != 0x2020_4d45L ? data + 0x1bcL : data + 0x20L, SVECTOR::new);
  }

  @Method(0x801100b8L)
  public static void FUN_801100b8(final int scriptIndex, final Ref<SVECTOR> a1, final Ref<VECTOR> a2) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();
    final long a3 = state.innerStruct_00.getPointer(); //TODO

    if(MEMORY.ref(4, a3).get() == 0x2020_4d45L) {
      a2.set(MEMORY.ref(4, a3 + 0x14L, VECTOR::new)); //TODO
      a1.set(MEMORY.ref(2, a3 + 0x20L, SVECTOR::new)); //TODO
      return;
    }

    //LAB_801100fc
    final BtldScriptData27c a3_0 = state.innerStruct_00.derefAs(BtldScriptData27c.class);
    a2.set(a3_0._148.coord2_14.coord.transfer);
    a1.set(a3_0._148.coord2Param_64.rotate);
  }

  @Method(0x80110228L)
  public static SVECTOR FUN_80110228(final SVECTOR s2, @Nullable VECTOR a3, final VECTOR a2) {
    if(a3 == null) {
      a3 = new VECTOR();
    }

    //LAB_80110258
    final VECTOR sp0x10 = new VECTOR().set(a2).sub(a3).negate();
    final SVECTOR sp0x30 = new SVECTOR();
    sp0x30.setY((short)ratan2(sp0x10.getX(), sp0x10.getZ()));
    final long s1 = rcos(-sp0x30.getY()) * sp0x10.getZ() - rsin(-sp0x30.getY()) * sp0x10.getX();

    //LAB_80110308
    sp0x30.setX((short)ratan2(-sp0x10.getY(), (int)s1 / 0x1000));

    final MATRIX sp0x38 = new MATRIX();
    RotMatrix_80040010(sp0x30, sp0x38);
    FUN_800de544(s2, sp0x38);

    return s2;
  }

  @Method(0x8011035cL)
  public static long FUN_8011035c(final int scriptIndex1, final int scriptIndex2, final VECTOR a2) {
    final VECTOR s0 = FUN_80110030(scriptIndex1);

    if(scriptIndex2 == -0x1L) {
      a2.set(s0);
    } else {
      //LAB_801103b8
      final Ref<SVECTOR> sp0x58 = new Ref<>();
      final Ref<VECTOR> sp0x5c = new Ref<>();
      FUN_801100b8(scriptIndex2, sp0x58, sp0x5c);

      final VECTOR sp0x18 = new VECTOR().set(s0).sub(sp0x5c.get());
      final MATRIX sp0x38 = new MATRIX();
      RotMatrix_8003faf0(sp0x58.get(), sp0x38);

      final VECTOR sp0x28 = new VECTOR();
      FUN_80040ec0(sp0x38, sp0x18, sp0x28);
      a2.set(sp0x28);
    }

    //LAB_80110450
    return scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.getPointer(); //TODO
  }

  @Method(0x801105ccL)
  public static void FUN_801105cc(final VECTOR a0, final int scriptIndex, final VECTOR a2) {
    final Ref<SVECTOR> sp0x30 = new Ref<>();
    final Ref<VECTOR> sp0x34 = new Ref<>();
    FUN_801100b8(scriptIndex, sp0x30, sp0x34);

    final MATRIX sp0x10 = new MATRIX();
    RotMatrix_8003faf0(sp0x30.get(), sp0x10);

    a0
      .set(ApplyMatrixLV(sp0x10, a2))
      .add(sp0x34.get());
  }

  @Method(0x8011066cL)
  public static BttlScriptData6c FUN_8011066c(final int scriptIndex1, final int scriptIndex2, final VECTOR a2) {
    final BttlScriptData6c data = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(BttlScriptData6c.class);
    long v1 = data._00.getPointer();
    if(v1 == 0x2020_4d45L && (data._04.get() & 0x2L) != 0) {
      FUN_800e8d04(data, 0x1L);
    }

    //LAB_801106dc
    final VECTOR a0_0 = FUN_80110030(scriptIndex1);
    if(scriptIndex2 == -1) {
      a0_0.set(a2);
    } else {
      //LAB_80110718
      FUN_801105cc(a0_0, scriptIndex2, a2);
    }

    //LAB_80110720
    return data;
  }

  @Method(0x80110740L)
  public static void FUN_80110740(BttlScriptData6c a0, BttlScriptData6cSub34 a1) {
    assert false;
  }

  @Method(0x801108fcL)
  public static BttlScriptData6cSub34 FUN_801108fc(final long a0, final long a1, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    final BttlScriptData6c s0 = scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.derefAs(BttlScriptData6c.class);
    if((s0._04.get() & 0x2L) != 0) {
      FUN_800e8d04(s0, 0x1L);
    }

    //LAB_80110980
    final BttlScriptData6cSub34 s2 = FUN_800e8dd4(s0, 0x1L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80110740", BttlScriptData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    s2._0c.set(s0.vec_14.getX() << 8, s0.vec_14.getY() << 8, s0.vec_14.getZ() << 8);
    s2._30.set(-1);
    s2._32.set((short)-1);

    final int transformedX1;
    final int transformedY1;
    final int transformedZ1;
    final int transformedX2;
    final int transformedY2;
    final int transformedZ2;
    if((int)a1 != -0x1L) {
      final MATRIX rotation = new MATRIX();
      RotMatrix_8003faf0(FUN_80110074(a1), rotation);
      final VECTOR sp0x38 = new VECTOR().set(x1, y1, z1);
      final VECTOR sp0x48 = new VECTOR();
      sp0x48.set(ApplyMatrixLV(rotation, sp0x38));
      transformedX1 = sp0x48.getX();
      transformedY1 = sp0x48.getY();
      transformedZ1 = sp0x48.getZ();
      sp0x38.set(x2, y2, z2);
      sp0x48.set(ApplyMatrixLV(rotation, sp0x38));
      transformedX2 = sp0x48.getX();
      transformedY2 = sp0x48.getY();
      transformedZ2 = sp0x48.getZ();
    } else {
      transformedX1 = x1;
      transformedY1 = y1;
      transformedZ1 = z1;
      transformedX2 = x2;
      transformedY2 = y2;
      transformedZ2 = z2;
    }

    //LAB_80110a5c
    s2._18.set(transformedX1, transformedY1, transformedZ1);
    s2._24.set(transformedX2, transformedY2, transformedZ2);
    return s2;
  }

  @Method(0x801115ecL)
  public static long FUN_801115ec(final RunningScript s0) {
    final VECTOR sp0x10 = new VECTOR();
    FUN_8011035c((int)s0.params_20.get(0).deref().get(), (int)s0.params_20.get(1).deref().get(), sp0x10);
    s0.params_20.get(2).deref().set(sp0x10.getX() & 0xffff_ffffL);
    s0.params_20.get(3).deref().set(sp0x10.getY() & 0xffff_ffffL);
    s0.params_20.get(4).deref().set(sp0x10.getZ() & 0xffff_ffffL);
    return 0;
  }

  @Method(0x80111c2cL)
  public static long FUN_80111c2c(final RunningScript a0) {
    FUN_801108fc(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), (int)a0.params_20.get(2).deref().get(), (int)a0.params_20.get(3).deref().get(), (int)a0.params_20.get(4).deref().get(), (int)a0.params_20.get(5).deref().get(), (int)a0.params_20.get(6).deref().get(), (int)a0.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x80114598L)
  public static long FUN_80114598(final RunningScript a0) {
    long a1 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO

    final SVECTOR a3;
    if(MEMORY.ref(4, a1).offset(0x0L).get() != 0x2020_4d45L) {
      a3 = new SVECTOR().set(_800fb94c);
    } else {
      a3 = MEMORY.ref(2, a1 + 0x2cL, SVECTOR::new);
    }

    //LAB_80114614
    if((int)a0.params_20.get(1).deref().get() == -0x1L) {
      a3.setX((short)a0.params_20.get(2).deref().get());
      a3.setY((short)a0.params_20.get(3).deref().get());
      a3.setZ((short)a0.params_20.get(4).deref().get());
    } else {
      //LAB_80114668
      a1 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(1).deref().get()).deref().innerStruct_00.getPointer(); //TODO

      final SVECTOR a2;
      if(MEMORY.ref(4, a1).offset(0x0L).get() != 0x2020_4d45L) {
        a2 = _800fb94c;
      } else {
        a2 = MEMORY.ref(2, a1 + 0x2cL, SVECTOR::new);
      }

      //LAB_8011469c
      a3.setX((short)(a0.params_20.get(2).deref().get() + a2.getX()));
      a3.setY((short)(a0.params_20.get(3).deref().get() + a2.getY()));
      a3.setZ((short)(a0.params_20.get(4).deref().get() + a2.getZ()));
    }

    //LAB_801146f0
    return 0;
  }

  @Method(0x80111ae4L)
  public static long FUN_80111ae4(final RunningScript a0) {
    final VECTOR sp0x10 = new VECTOR().set((int)a0.params_20.get(2).deref().get(), (int)a0.params_20.get(3).deref().get(), (int)a0.params_20.get(4).deref().get());
    FUN_8011066c((int)a0.params_20.get(0).deref().get(), (int)a0.params_20.get(1).deref().get(), sp0x10);
    return 0;
  }

  @Method(0x80112530L)
  public static long FUN_80112530(final long a0, final long a1, final SVECTOR a2) {
    final BttlScriptData6c data = scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.derefAs(BttlScriptData6c.class);

    if(data._00.getPointer() == 0x2020_4d45L && (data._04.get() & 0x4L) != 0) {
      FUN_800e8d04(data, 0x2L);
    }

    //LAB_8011259c
    final SVECTOR s0 = FUN_80110074(a0).set(a2);
    if((int)a1 != -0x1L) {
      //LAB_801125d8
      s0.add(FUN_80110074(a1));
    }

    //LAB_8011261c
    return 0;
  }

  @Method(0x80112770L)
  public static long FUN_80112770(final RunningScript a0) {
    FUN_80112530(
      a0.params_20.get(0).deref().get(),
      a0.params_20.get(1).deref().get(),
      new SVECTOR().set(
        (short)a0.params_20.get(2).deref().get(),
        (short)a0.params_20.get(3).deref().get(),
        (short)a0.params_20.get(4).deref().get()
      )
    );

    return 0;
  }

  @Method(0x80112900L)
  public static long FUN_80112900(final RunningScript s0) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_80110228(sp0x10, FUN_80110030((int)s0.params_20.get(0).deref().get()), FUN_80110030((int)s0.params_20.get(1).deref().get()));
    s0.params_20.get(2).deref().set(sp0x10.getX());
    s0.params_20.get(3).deref().set(sp0x10.getZ());
    s0.params_20.get(4).deref().set(sp0x10.getY());
    return 0;
  }

  @Method(0x801139d0L)
  public static long FUN_801139d0(final RunningScript a0) {
    final long t1 = a0.params_20.get(0).deref().get();
    final long a1 = a0.params_20.get(1).deref().get();
    final short a2 = (short)a0.params_20.get(2).deref().get();
    final short a3 = (short)a0.params_20.get(3).deref().get();
    final short t0 = (short)a0.params_20.get(4).deref().get();
    short sp4;
    short sp2;
    short sp0;
    if((int)a1 == -0x1L) {
      sp0 = a2;
      sp2 = a3;
      sp4 = t0;
    } else {
      //LAB_80113a28
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get((int)a1).deref();
      final long a1_0 = state.innerStruct_00.getPointer(); //TODO

      final SVECTOR v1;
      if(MEMORY.ref(4, a1_0).offset(0x0L).get() == 0x2020_4d45L) {
        v1 = MEMORY.ref(2, a1_0 + 0x26L, SVECTOR::new);
      } else {
        //LAB_80113a64
        v1 = new SVECTOR().set((short)MEMORY.ref(2, a1_0).offset(0x244L).get(), (short)MEMORY.ref(2, a1_0).offset(0x248L).get(), (short)MEMORY.ref(2, a1_0).offset(0x24cL).get());
      }

      //LAB_80113aa0
      //LAB_80113abc
      //LAB_80113ae0
      //LAB_80113b04
      sp0 = (short)(a2 * v1.getX() / 0x1000);
      sp2 = (short)(a3 * v1.getY() / 0x1000);
      sp4 = (short)(t0 * v1.getZ() / 0x1000);
    }

    //LAB_80113b0c
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get((int)t1).deref();
    final long a0_0 = state.innerStruct_00.getPointer(); //TODO
    if(MEMORY.ref(4, a0_0).offset(0x0L).get() == 0x2020_4d45L) {
      MEMORY.ref(2, a0_0).offset(0x26L).setu(sp0);
      MEMORY.ref(2, a0_0).offset(0x28L).setu(sp2);
      MEMORY.ref(2, a0_0).offset(0x2aL).setu(sp4);
    } else {
      //LAB_80113b64
      MEMORY.ref(4, a0_0).offset(0x244L).setu(sp0);
      MEMORY.ref(4, a0_0).offset(0x248L).setu(sp2);
      MEMORY.ref(4, a0_0).offset(0x24cL).setu(sp4);
    }

    //LAB_80113b94
    return 0;
  }

  @Method(0x80114e60L)
  public static long FUN_80114e60(final RunningScript a0) {
    final long v0 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(4, v0).offset(0x34L).offset(a0.params_20.get(1).deref().get() * 0x4L).setu(a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x80115690L)
  public static long FUN_80115690(final RunningScript a0) {
    final long s0 = a0.params_20.get(0).deref().get();
    loadScriptFile(s0, a0.scriptState_04.deref().scriptPtr_14.deref(), 0, "S_EFFE Script", 0); //TODO unknown size
    scriptStatePtrArr_800bc1c0.get((int)s0).deref().commandPtr_18.set(a0.params_20.get(1).deref());
    return 0;
  }

  @Method(0x801156f8L)
  public static long FUN_801156f8(final RunningScript a0) {
    final long v0 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(1, v0).offset(0xcL).setu(a0.params_20.get(1).deref().get());
    MEMORY.ref(1, v0).offset(0xdL).setu(a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x80115cacL)
  public static void FUN_80115cac(long a0) {
    assert false;
  }

  @Method(0x801186f8L)
  public static void FUN_801186f8(final long a0, final long a1) {
    MEMORY.ref(4, a0).offset(0x0L).setu(a1 | 0x300_0000L);
    if((a1 & 0xf_ff00L) == 0xf_ff00L) {
      MEMORY.ref(2, a0).offset(0x10L).setu(0x20L);
      MEMORY.ref(4, a0).offset(0x4L).setu(0);
      MEMORY.ref(4, a0).offset(0x8L).setu(_800c693c.deref(4).offset((a1 & 0xffL) * 0x4L).offset(0x2f8L).get());
    } else {
      //LAB_80118750
      long v0 = FUN_800eac58(a1 | 0x300_0000L);
      MEMORY.ref(4, a0).offset(0x4L).setu(v0);
      v0 = v0 + MEMORY.ref(4, v0).offset(0xcL).get();
      MEMORY.ref(4, a0).offset(0x8L).setu(v0 + 0x18L);
      MEMORY.ref(2, a0).offset(0x10L).setu((MEMORY.ref(4, v0).offset(0xcL).get() & 0xffff_0000L) >>> 11);
    }

    //LAB_80118780
  }

  @Method(0x80118e98L)
  public static void FUN_80118e98(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data6c) {
    assert false;
  }

  @Method(0x80119484L)
  public static long FUN_80119484(final RunningScript a0) {
    long s4 = a0.params_20.get(1).deref().get();
    long s2 = a0.params_20.get(2).deref().get();
    long s6 = a0.params_20.get(3).deref().get();
    long s0 = a0.params_20.get(4).deref().get();
    long s1 = a0.params_20.get(5).deref().get();

    long fp = FUN_800e80c4(
      a0.scriptStateIndex_00.get(),
      0x30L,
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801196bc", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80118e98", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80119788", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new)
    );

    ScriptState<BttlScriptData6c> v0 = scriptStatePtrArr_800bc1c0.get((int)fp).derefAs(ScriptState.classFor(BttlScriptData6c.class));
    final BttlScriptData6c data = v0.innerStruct_00.deref();
    strcpy(data._5c, _800fb954.get());

    final long s3 = data._44.get();
    MEMORY.ref(4, s3).offset(0x4L).setu(s4);
    MEMORY.ref(4, s3).offset(0x14L).setu(0);
    MEMORY.ref(4, s3).offset(0x0L).setu(s2);
    MEMORY.ref(4, s3).offset(0x8L).setu(s6);
    MEMORY.ref(4, s3).offset(0xcL).setu(s0);
    MEMORY.ref(4, s3).offset(0x10L).setu(s1);

    if(s6 != 0) {
      MEMORY.ref(4, s3).offset(0x18L).setu(addToLinkedListHead(s6 * 0x10L));
    } else {
      //LAB_80119568
      MEMORY.ref(4, s3).offset(0x18L).setu(0);
    }

    //LAB_8011956c
    long v1 = s4 & 0xff00_0000L;
    if(v1 != 0x300_0000L) {
      if(v1 <= 0x300_0000L) {
        if(v1 == 0) {
          //LAB_801195a8
          v1 = scriptStatePtrArr_800bc1c0.get((int)s4).deref().innerStruct_00.derefAs(BttlScriptData6c.class)._44.get();
          s4 = MEMORY.ref(4, v1).offset(0x0L).get();
          long a1 = s4 & 0xff00_0000L;
          MEMORY.ref(4, s3).offset(0x4L).setu(s4);
          if(a1 != 0x300_0000L) {
            if(a1 == 0x400_0000L) {
              MEMORY.ref(4, s3).offset(0x1cL).setu(MEMORY.ref(4, v1).offset(0x0L).get());
              MEMORY.ref(4, s3).offset(0x20L).setu(MEMORY.ref(4, v1).offset(0x4L).get());
              MEMORY.ref(4, s3).offset(0x24L).setu(MEMORY.ref(4, v1).offset(0x8L).get());
            }
          } else {
            //LAB_8011960c
            MEMORY.ref(4, s3).offset(0x1cL).setu(MEMORY.ref(4, v1).offset(0x0L).get());
            MEMORY.ref(4, s3).offset(0x20L).setu(MEMORY.ref(4, v1).offset(0x4L).get());
            MEMORY.ref(4, s3).offset(0x24L).setu(MEMORY.ref(4, v1).offset(0x8L).get());
            MEMORY.ref(4, s3).offset(0x28L).setu(MEMORY.ref(4, v1).offset(0xcL).get());
            MEMORY.ref(4, s3).offset(0x2cL).setu(MEMORY.ref(4, v1).offset(0x10L).get());
          }
        }
        //LAB_80119598
      } else if(v1 == 0x400_0000L) {
        //LAB_80119640
        FUN_800e95f0(s3 + 0x1cL, s4);
        data._10.or(0x5000_0000L).and(0xfbff_ffffL);
      }
    } else {
      //LAB_80119668
      FUN_801186f8(s3 + 0x1cL, s4);
      data._10.set(0x1400_0000L);
    }

    //LAB_8011967c
    a0.params_20.get(0).deref().set(fp);
    return 0;
  }

  @Method(0x801196bcL)
  public static void FUN_801196bc(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data6c) {
    assert false;
  }

  @Method(0x80119788L)
  public static void FUN_80119788(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data6c) {
    assert false;
  }
}
