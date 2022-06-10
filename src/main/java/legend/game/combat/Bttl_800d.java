package legend.game.combat;

import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.Scus94491BpeSegment_800c;
import legend.game.combat.types.BtldScriptData27c;
import legend.game.combat.types.BttlScriptData6cInner;
import legend.game.combat.types.BttlStruct50;
import legend.game.types.RunningScript;
import legend.game.types.ScriptState;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.game.Scus94491BpeSegment.FUN_80018a5c;
import static legend.game.Scus94491BpeSegment.FUN_80018d60;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003eba0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f210;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f990;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixX;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixY;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.combat.Bttl_800c._800c6798;
import static legend.game.combat.Bttl_800c._800c67b8;
import static legend.game.combat.Bttl_800c._800c67c4;
import static legend.game.combat.Bttl_800c._800c67d4;
import static legend.game.combat.Bttl_800c._800c67d8;
import static legend.game.combat.Bttl_800c._800c67dc;
import static legend.game.combat.Bttl_800c._800c67e0;
import static legend.game.combat.Bttl_800c._800c67e4;
import static legend.game.combat.Bttl_800c._800c67e8;
import static legend.game.combat.Bttl_800c._800c6878;
import static legend.game.combat.Bttl_800c._800c68ec;
import static legend.game.combat.Bttl_800c._800c6912;
import static legend.game.combat.Bttl_800c._800c6913;
import static legend.game.combat.Bttl_800c._800c6920;
import static legend.game.combat.Bttl_800c._800c693c;
import static legend.game.combat.Bttl_800c._800faaa0;
import static legend.game.combat.Bttl_800c._800fab98;
import static legend.game.combat.Bttl_800c._800faba0;
import static legend.game.combat.Bttl_800c._800faba8;
import static legend.game.combat.Bttl_800c._800fabb8;
import static legend.game.combat.Bttl_800c._800fabbc;
import static legend.game.combat.Bttl_800c._800fabdc;
import static legend.game.combat.Bttl_800c._800fabfc;
import static legend.game.combat.Bttl_800c._800fac3c;
import static legend.game.combat.Bttl_800c._800fac5c;
import static legend.game.combat.Bttl_800c._800fac9c;
import static legend.game.combat.Bttl_800c._800fad7c;
import static legend.game.combat.Bttl_800c._800fad90;
import static legend.game.combat.Bttl_800c._800fad9c;
import static legend.game.combat.Bttl_800c.rview2_800c67f0;
import static legend.game.combat.Bttl_800c.x_800c67bc;
import static legend.game.combat.Bttl_800c.y_800c67c0;
import static legend.game.combat.Bttl_800e.FUN_800e3e6c;

public final class Bttl_800d {
  private Bttl_800d() { }

  @Method(0x800d46d4L)
  public static long FUN_800d46d4(final Ref<Long>[] a0) {
    final long v1 = _800faaa0.offset(a0[0x8].get() * 0x6L).getAddress();

    final byte[] sp0x30 = {a0[0xc].get().byteValue(), a0[0xc].get().byteValue(), a0[0xc].get().byteValue()};

    if(MEMORY.ref(1, v1).offset(0x0L).get() == 0) {
      FUN_80018d60(a0[0x9].get().shortValue(), a0[0xa].get().shortValue(), MEMORY.ref(1, v1).offset(0x1L).get(), MEMORY.ref(1, v1).offset(0x2L).get(), MEMORY.ref(1, v1).offset(0x3L).get(), MEMORY.ref(1, v1).offset(0x4L).get(), MEMORY.ref(1, v1).offset(0x5L).get(), a0[0xb].get(), sp0x30, 0x1000L);
    } else {
      //LAB_800d4784
      FUN_80018a5c(a0[0x9].get().shortValue(), a0[0xa].get().shortValue(), MEMORY.ref(1, v1).offset(0x1L).get(), MEMORY.ref(1, v1).offset(0x2L).get(), MEMORY.ref(1, v1).offset(0x3L).get(), MEMORY.ref(1, v1).offset(0x4L).get(), MEMORY.ref(1, v1).offset(0x5L).get(), a0[0xb].get(), sp0x30, 0x1000L, 0x1000L);
    }

    //LAB_800d47cc
    return 0;
  }

  @Method(0x800d47dcL)
  public static void FUN_800d47dc(long a0, long a1, long a2, long a3, long a4, long a5, long a6) {
    long v0;
    long v1;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long lo;
    s1 = a3;
    s2 = 0x800c_0000L;
    v0 = MEMORY.ref(4, s2).offset(0x67f0L).get();
    a3 = a5;
    s4 = a0;
    s5 = a1;
    s3 = a2;
    s0 = s2 + 0x67f0L;
    v1 = MEMORY.ref(4, s0).offset(0x8L).get();
    a0 = v0 << 8;
    v0 = MEMORY.ref(4, s0).offset(0x4L).get();
    a1 = v1 << 8;
    MEMORY.ref(4, s0).offset(0x94L).setu(a0);
    MEMORY.ref(4, s0).offset(0x9cL).setu(a1);
    a2 = v0 << 8;
    MEMORY.ref(4, s0).offset(0x98L).setu(a2);
    if(a3 == 0) {
      //LAB_800d4854
      a0 = s4 - a0;
      lo = (int)a0 / (int)s1;
      a0 = lo;
      v1 = s5 - a2;

      lo = (int)v1 / (int)s1;
      v1 = lo;
      v0 = s3 - a1;

      lo = (int)v0 / (int)s1;
      v0 = lo;
      MEMORY.ref(4, s0).offset(0xd0L).setu(s1);
      MEMORY.ref(4, s0).offset(0xb0L).setu(a0);
      MEMORY.ref(4, s0).offset(0xbcL).setu(v1);
      MEMORY.ref(4, s0).offset(0xc8L).setu(v0);
    } else assert a3 != 0x1L : "Undefined t0/t1";

    //LAB_800d492c
    v0 = 0x800c_0000L;
    v0 = v0 + 0x67f0L;

    //LAB_800d4934
    v1 = MEMORY.ref(4, v0).offset(0x11cL).get();
    a0 = 0x8L;
    MEMORY.ref(1, v0).offset(0x120L).setu(a0);
    v1 = v1 | 0x1L;
    MEMORY.ref(4, v0).offset(0x11cL).setu(v1);
  }

  @Method(0x800d4bacL)
  public static void FUN_800d4bac(long a0, long a1, long a2, long a3, long a4, long a5, long a6) {
    long v0;
    long v1;
    long s0;
    long s1;
    long s2;
    long s6;
    long s7;
    long lo;
    s6 = a0;
    s7 = a1;
    s2 = a2;
    s1 = a3;
    a0 = 0;
    a1 = 0x4L;
    a2 = a0;
    a3 = a0;
    v0 = FUN_800dc384(a0, a1, a2, a3);
    a0 = 0;
    a1 = 0x4L;
    a2 = 0x1L;
    a3 = a0;
    v1 = 0x800c_0000L;
    s0 = v1 + 0x67f0L;
    v0 = v0 << 8;
    MEMORY.ref(4, s0).offset(0x94L).setu(v0);
    v0 = FUN_800dc384(a0, a1, a2, a3);
    a0 = 0;
    a1 = 0x4L;
    a2 = 0x2L;
    a3 = a0;
    v0 = v0 << 8;
    MEMORY.ref(4, s0).offset(0x98L).setu(v0);
    v0 = FUN_800dc384(a0, a1, a2, a3);
    v1 = v0 << 8;
    MEMORY.ref(4, s0).offset(0x9cL).setu(v1);
    if(a5 == 0) {
      //LAB_800d4c5c
      a0 = MEMORY.ref(4, s0).offset(0x94L).get();

      a0 = s6 - a0;
      lo = (int)a0 / (int)s1;
      a0 = lo;
      v1 = s2 - v1;

      lo = (int)v1 / (int)s1;
      v1 = lo;
      v0 = MEMORY.ref(4, s0).offset(0x98L).get();

      v0 = s7 - v0;
      lo = (int)v0 / (int)s1;
      v0 = lo;
      MEMORY.ref(4, s0).offset(0xd0L).setu(s1);
      MEMORY.ref(4, s0).offset(0xc8L).setu(v1);
      MEMORY.ref(4, s0).offset(0xb0L).setu(a0);
      MEMORY.ref(4, s0).offset(0xbcL).setu(v0);
    } else assert a5 != 0x1L : "Undefined s3/s5";

    //LAB_800d4d34
    v0 = 0x800c_0000L;
    v0 = v0 + 0x67f0L;

    //LAB_800d4d3c
    MEMORY.ref(1, v0).offset(0x120L).setu(0xcL);
    MEMORY.ref(4, v0).offset(0x11cL).oru(0x1L);
  }

  @Method(0x800d5ec8L)
  public static void FUN_800d5ec8(long a0, long a1, long a2, long a3, long a4, long a5, long a6, long a7) {
    long v0;
    long v1;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    long lo;
    long sp40;
    long sp38;
    long sp3c;
    v1 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v1).offset(0x67f0L).get();
    sp38 = a0;
    s3 = (int)a0 >> 8;
    s5 = v1 + 0x67f0L;
    s3 = s3 - v0;
    s2 = s3 >>> 31;
    s2 = s3 + s2;
    s2 = (int)s2 >> 1;
    lo = (long)(int)s2 * (int)s2 & 0xffff_ffffL;
    sp3c = a1;
    sp40 = a2;
    v0 = MEMORY.ref(4, s5).offset(0x4L).get();
    s4 = (int)a1 >> 8;
    s4 = s4 - v0;
    v0 = s4 >>> 31;
    s2 = lo;
    v0 = s4 + v0;
    v0 = (int)v0 >> 1;
    lo = (long)(int)v0 * (int)v0 & 0xffff_ffffL;
    v0 = MEMORY.ref(4, s5).offset(0x8L).get();
    s1 = (int)a2 >> 8;
    s1 = s1 - v0;
    s0 = s1 >>> 31;
    v1 = lo;
    s0 = s1 + s0;
    s0 = (int)s0 >> 1;
    lo = (long)(int)s0 * (int)s1 & 0xffff_ffffL;
    fp = a3;
    v1 = s2 + v1;
    a0 = lo;
    v0 = a0 >>> 31;
    a0 = a0 + v0;
    a0 = (int)a0 >> 1;
    a0 = v1 + a0;
    v0 = SquareRoot0(a0);
    a0 = s1;
    a1 = s3;
    v0 = v0 << 9;
    MEMORY.ref(4, s5).offset(0xdcL).setu(v0);
    v0 = ratan2(a0, a1);
    lo = (long)(int)s0 * (int)s0 & 0xffff_ffffL;
    v0 = v0 & 0xfffL;
    v0 = v0 << 8;
    MEMORY.ref(4, s5).offset(0xd4L).setu(v0);
    v1 = lo;
    a0 = s2 + v1;
    v0 = SquareRoot0(a0);
    a0 = s4;
    a1 = v0 << 1;
    v0 = ratan2(a0, a1);
    v0 = v0 & 0xfffL;
    v0 = v0 << 8;
    MEMORY.ref(4, s5).offset(0xd8L).setu(v0);
    MEMORY.ref(4, s5).offset(0xd0L).setu(fp);
    t0 = a4;

    if(t0 == 0) {
      //LAB_800d5ff0
      v0 = MEMORY.ref(4, s5).offset(0xdcL).get();

      v0 = v0 << 1;
      lo = (int)v0 / (int)fp;
      v0 = lo;
      s6 = a5;
      s7 = v0 - s6;
    } else if(t0 == 0x1L) {
      //LAB_800d6010
      v0 = MEMORY.ref(4, s5).offset(0xdcL).get();

      v0 = v0 << 1;
      lo = (int)v0 / (int)fp;
      v0 = lo;
      s7 = a5;
      s6 = v0 - s7;
    } else {
      throw new RuntimeException("Undefined s6/s7");
    }

    //LAB_800d6030
    v0 = 0x800c_0000L;
    v0 = v0 + 0x67f0L;

    //LAB_800d6038
    v1 = MEMORY.ref(4, v0).offset(0xd0L).get();
    a1 = s7 - s6;
    lo = (int)a1 / (int)v1;
    a1 = lo;
    MEMORY.ref(4, v0).offset(0xa4L).setu(s6);
    t0 = sp38;

    MEMORY.ref(4, v0).offset(0xe8L).setu(t0);
    t0 = sp3c;
    v1 = MEMORY.ref(4, v0).offset(0x11cL).get();
    a0 = 0x10L;
    MEMORY.ref(4, v0).offset(0xecL).setu(t0);
    t0 = sp40;
    v1 = v1 | 0x1L;
    MEMORY.ref(4, v0).offset(0xf0L).setu(t0);
    MEMORY.ref(1, v0).offset(0x120L).setu(a0);
    MEMORY.ref(4, v0).offset(0x11cL).setu(v1);
    MEMORY.ref(4, v0).offset(0xb4L).setu(a1);
  }

  @Method(0x800d62d8L)
  public static void FUN_800d62d8(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    long v0;
    long v1;
    long t1;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long lo;
    s2 = 0x800c_0000L;
    s2 = s2 + 0x67f0L;
    v0 = FUN_800dc384(0, 0x4L, 0, 0) * 0x100;
    MEMORY.ref(4, s2).offset(0x94L).setu(v0);
    _800c67d8.setu(v0);
    v0 = FUN_800dc384(0, 0x4L, 0x1L, 0) * 0x100;
    MEMORY.ref(4, s2).offset(0x98L).setu(v0);
    _800c67dc.setu(v0);
    v0 = FUN_800dc384(0, 0x4L, 0x2L, 0);
    s1 = a0 - MEMORY.ref(4, s2).offset(0x94L).get();
    s4 = (int)s1 >> 8;
    s1 = (int)s4 / 2;
    lo = ((long)(int)s1 * (int)s1) & 0xffff_ffffL;
    v1 = a1 - MEMORY.ref(4, s2).offset(0x98L).get();
    s5 = (int)v1 >> 8;
    s1 = lo;
    v1 = (int)s5 / 2;
    lo = ((long)(int)v1 * (int)v1) & 0xffff_ffffL;
    v0 = v0 << 8;
    s0 = a2 - v0;
    s3 = (int)s0 >> 8;
    t1 = lo;
    s0 = (int)s3 / 2;
    lo = ((long)(int)s0 * (int)s3) & 0xffff_ffffL;
    MEMORY.ref(4, s2).offset(0x9cL).setu(v0);
    _800c67e0.setu(v0);
    MEMORY.ref(4, s2).offset(0xdcL).setu(SquareRoot0(s1 + t1 + (int)lo / 2) * 0x200);
    MEMORY.ref(4, s2).offset(0xd4L).setu((ratan2(s3, s4) & 0xfffL) * 0x100);
    lo = ((long)(int)s0 * (int)s0) & 0xffff_ffffL;
    v1 = lo;
    v0 = ratan2(s5, SquareRoot0(s1 + v1) * 2) & 0xfffL;
    v0 = v0 << 8;
    MEMORY.ref(4, s2).offset(0xd8L).setu(v0);
    MEMORY.ref(4, s2).offset(0xd0L).setu(a3);
    final Ref<Long> sp0x18 = new Ref<>();
    final Ref<Long> sp0x1c = new Ref<>();
    FUN_800dcebc(a4, a5, MEMORY.ref(4, s2).offset(0xdcL).get(), a3, sp0x18, sp0x1c);
    MEMORY.ref(4, s2).offset(0xe8L).setu(a0);
    MEMORY.ref(4, s2).offset(0xecL).setu(a1);
    MEMORY.ref(4, s2).offset(0xf0L).setu(a2);
    MEMORY.ref(1, s2).offset(0x120L).setu(0x14L);
    MEMORY.ref(4, s2).offset(0xa4L).setu(sp0x18.get());
    MEMORY.ref(4, s2).offset(0x11cL).oru(0x1L);
    MEMORY.ref(4, s2).offset(0xb4L).setu((int)(sp0x1c.get() - sp0x18.get()) / (int)MEMORY.ref(4, s2).offset(0xd0L).get());
  }

  @Method(0x800d670cL)
  public static void FUN_800d670c(long a0, long a1, long a2, long a3, long a4, long a5, long a6, long a7) {
    long v0;
    long v1;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long lo;
    s5 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s5).offset(0xf4L).setu(a7);
    v0 = FUN_800dc384(0, 0x6L, 0, a7) << 8;
    MEMORY.ref(4, s5).offset(0x94L).setu(v0);
    v0 = FUN_800dc384(0, 0x6L, 1, a7) << 8;
    MEMORY.ref(4, s5).offset(0x98L).setu(v0);
    v0 = FUN_800dc384(0, 0x6L, 2, a7) << 8;
    s1 = a0 - MEMORY.ref(4, s5).offset(0x94L).get();
    s3 = (int)s1 >> 8;
    s1 = s3;
    s1 = (int)s1 / 2;
    lo = ((long)(int)s1 * (int)s1) & 0xffff_ffffL;
    s1 = lo;
    v1 = a1 - MEMORY.ref(4, s5).offset(0x98L).get();
    s4 = (int)v1 >> 8;
    v1 = s4;
    v1 = (int)v1 / 2;
    lo = ((long)(int)v1 * (int)v1) & 0xffff_ffffL;
    s2 = (int)(a2 - v0) >> 8;
    v1 = lo;
    s0 = (int)s2 / 2;
    lo = ((long)(int)s0 * (int)s2) & 0xffff_ffffL;
    MEMORY.ref(4, s5).offset(0x9cL).setu(v0);
    v1 = s1 + v1;
    MEMORY.ref(4, s5).offset(0xdcL).setu(SquareRoot0(v1 + (int)lo / 2) * 0x200);
    MEMORY.ref(4, s5).offset(0xd4L).setu((ratan2(s2, s3) & 0xfffL) * 0x100);
    lo = ((long)(int)s0 * (int)s0) & 0xffff_ffffL;
    v1 = lo;
    MEMORY.ref(4, s5).offset(0xd8L).setu((ratan2(s4, SquareRoot0(s1 + v1) * 2) & 0xfffL) * 0x100);
    MEMORY.ref(4, s5).offset(0xd0L).setu(a3);

    if(a4 == 0) {
      //LAB_800d68a0
      s6 = a5;
      s7 = (int)(MEMORY.ref(4, s5).offset(0xdcL).get() * 0x2L) / (int)a3 - s6;
    } else if(a4 == 0x1L) {
      //LAB_800d68c0
      s7 = a5;
      s6 = (int)(MEMORY.ref(4, s5).offset(0xdcL).get() * 0x2L) / (int)a3 - s7;
    } else {
      throw new RuntimeException("Undefined s6/s7");
    }

    //LAB_800d68e0
    v0 = rview2_800c67f0.getAddress();

    //LAB_800d68e8
    MEMORY.ref(4, v0).offset(0xa4L).setu(s6);
    MEMORY.ref(4, v0).offset(0xe8L).setu(a0);
    MEMORY.ref(4, v0).offset(0xecL).setu(a1);
    MEMORY.ref(4, v0).offset(0xf0L).setu(a2);
    MEMORY.ref(1, v0).offset(0x120L).setu(0x16L);
    MEMORY.ref(4, v0).offset(0x11cL).oru(0x1L);
    MEMORY.ref(4, v0).offset(0xb4L).setu((int)(s7 - s6) / (int)MEMORY.ref(4, v0).offset(0xd0L).get());
  }

  @Method(0x800d6b90L)
  public static void FUN_800d6b90(long a0, long a1, long a2, long a3, long a4, long a5, long a6) {
    long v0;
    long v1;
    long t0;
    long t1;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long lo;
    s3 = a0;
    s4 = a1;
    s2 = a2;
    s1 = a3;
    v0 = 0x800c_0000L;
    s0 = v0 + 0x67f0L;
    v0 = MEMORY.ref(4, s0).offset(0xcL).get();
    a3 = a5;
    v1 = MEMORY.ref(4, s0).offset(0x14L).get();
    a0 = v0 << 8;
    v0 = MEMORY.ref(4, s0).offset(0x10L).get();
    a1 = v1 << 8;
    MEMORY.ref(4, s0).offset(0x20L).setu(a0);
    MEMORY.ref(4, s0).offset(0x28L).setu(a1);
    a2 = v0 << 8;
    if(a3 == 0) {
      MEMORY.ref(4, s0).offset(0x24L).setu(a2);
      //LAB_800d6c04
      a0 = s3 - a0;
      lo = (int)a0 / (int)s1;
      a0 = lo;
      v1 = s4 - a2;

      lo = (int)v1 / (int)s1;
      v1 = lo;
      v0 = s2 - a1;

      lo = (int)v0 / (int)s1;
      v0 = lo;
      MEMORY.ref(4, s0).offset(0x5cL).setu(s1);
      MEMORY.ref(4, s0).offset(0x3cL).setu(a0);
      MEMORY.ref(4, s0).offset(0x48L).setu(v1);
      MEMORY.ref(4, s0).offset(0x54L).setu(v0);
    } else {
      MEMORY.ref(4, s0).offset(0x24L).setu(a2);
      v0 = 0x1L;
      if(a3 == v0) {
        //LAB_800d6c44
        //TODO undefined t0/t1
/*
        v0 = s2 - a1;
        lo = ((long)(int)v0 * (int)v0) & 0xffff_ffffL;
        v1 = lo;
        lo = ((long)(int)t0 * (int)t0) & 0xffff_ffffL;
        t0 = lo;
        lo = ((long)(int)t1 * (int)t1) & 0xffff_ffffL;
        a0 = v1 + t0;
        v0 = lo;
        a0 = a0 + v0;
        v0 = SquareRoot0(a0);
        lo = (int)v0 / (int)s1;
        v0 = lo;
        a1 = MEMORY.ref(4, s0).offset(0xcL).get();
        a1 = a1 << 8;
        a1 = s3 - a1;
        lo = (int)a1 / (int)v0;
        a1 = lo;
        a0 = MEMORY.ref(4, s0).offset(0x10L).get();
        a0 = a0 << 8;
        a0 = s4 - a0;
        lo = (int)a0 / (int)v0;
        a0 = lo;
        v1 = MEMORY.ref(4, s0).offset(0x14L).get();
        v1 = v1 << 8;
        v1 = s2 - v1;
        lo = (int)v1 / (int)v0;
        v1 = lo;
        MEMORY.ref(4, s0).offset(0x5cL).setu(v0);
        MEMORY.ref(4, s0).offset(0x3cL).setu(a1);
        MEMORY.ref(4, s0).offset(0x48L).setu(a0);
        MEMORY.ref(4, s0).offset(0x54L).setu(v1);
*/
      }
    }

    //LAB_800d6cdc
    v0 = 0x800c_0000L;
    v0 = v0 + 0x67f0L;

    //LAB_800d6ce4
    MEMORY.ref(1, v0).offset(0x121L).setu(0x8L);
    MEMORY.ref(4, v0).offset(0x11cL).oru(0x2L);
  }

  @Method(0x800d7368L)
  public static void FUN_800d7368(long a0, long a1, long a2, long a3, long a4, long a5, long a6) {
    long v0;
    long v1;
    long t1;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    long lo;
    s7 = a0;
    fp = a1;
    s3 = a2;
    s2 = a3;
    a0 = 0x1L;
    a1 = 0x6L;
    s0 = a6;
    a2 = 0;
    s4 = a5;
    v0 = 0x800c_0000L;
    s1 = v0 + 0x67f0L;
    a3 = s0;
    MEMORY.ref(4, s1).offset(0x80L).setu(s0);
    v0 = FUN_800dc384(a0, a1, a2, a3);
    a0 = 0x1L;
    a1 = 0x6L;
    a2 = a0;
    a3 = s0;
    v0 = v0 << 8;
    MEMORY.ref(4, s1).offset(0x20L).setu(v0);
    v0 = FUN_800dc384(a0, a1, a2, a3);
    a0 = 0x1L;
    a1 = 0x6L;
    a2 = 0x2L;
    a3 = s0;
    v0 = v0 << 8;
    MEMORY.ref(4, s1).offset(0x24L).setu(v0);
    v0 = FUN_800dc384(a0, a1, a2, a3);
    v1 = v0 << 8;
    if(s4 == 0) {
      MEMORY.ref(4, s1).offset(0x28L).setu(v1);
      //LAB_800d7424
      a0 = MEMORY.ref(4, s1).offset(0x20L).get();
      a0 = s7 - a0;
      lo = (int)a0 / (int)s2;
      a0 = lo;
      v1 = s3 - v1;
      lo = (int)v1 / (int)s2;
      v1 = lo;
      v0 = MEMORY.ref(4, s1).offset(0x24L).get();
      v0 = fp - v0;
      lo = (int)v0 / (int)s2;
      v0 = lo;
      MEMORY.ref(4, s1).offset(0x5cL).setu(s2);
      MEMORY.ref(4, s1).offset(0x54L).setu(v1);
      MEMORY.ref(4, s1).offset(0x3cL).setu(a0);
      MEMORY.ref(4, s1).offset(0x48L).setu(v0);
    } else {
      MEMORY.ref(4, s1).offset(0x28L).setu(v1);
      v0 = 0x1L;
      if(s4 == v0) {
        assert false : "Undefined s5/s6";
        s5 = 0;
        s6 = 0;
        //LAB_800d7470
        v0 = s3 - v1;
        lo = ((long)(int)v0 * (int)v0) & 0xffff_ffffL;
        v1 = lo;
        lo = ((long)(int)s5 * (int)s5) & 0xffff_ffffL;
        t1 = lo;
        lo = ((long)(int)s6 * (int)s6) & 0xffff_ffffL;
        a0 = v1 + t1;
        v0 = lo;
        a0 = a0 + v0;
        v0 = SquareRoot0(a0);
        lo = (int)v0 / (int)s2;
        v0 = lo;
        a1 = MEMORY.ref(4, s1).offset(0x20L).get();
        a1 = s7 - a1;
        lo = (int)a1 / (int)v0;
        a1 = lo;
        a0 = MEMORY.ref(4, s1).offset(0x24L).get();
        a0 = fp - a0;
        lo = (int)a0 / (int)v0;
        a0 = lo;
        v1 = MEMORY.ref(4, s1).offset(0x28L).get();
        v1 = s3 - v1;
        lo = (int)v1 / (int)v0;
        v1 = lo;
        MEMORY.ref(4, s1).offset(0x5cL).setu(v0);
        MEMORY.ref(4, s1).offset(0x3cL).setu(a1);
        MEMORY.ref(4, s1).offset(0x48L).setu(a0);
        MEMORY.ref(4, s1).offset(0x54L).setu(v1);
      }
    }

    //LAB_800d74fc
    v0 = 0x800c_0000L;
    v0 = v0 + 0x67f0L;

    //LAB_800d7504
    v1 = MEMORY.ref(4, v0).offset(0x11cL).get();
    a0 = 0xeL;
    MEMORY.ref(1, v0).offset(0x121L).setu(a0);
    v1 = v1 | 0x2L;
    MEMORY.ref(4, v0).offset(0x11cL).setu(v1);
  }

  @Method(0x800d8274L)
  public static void FUN_800d8274(long a0, long a1, long a2, long a3, long a4, long a5, long a6, long a7) {
    long v0;
    long v1;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    long lo;
    long sp4c;
    long sp50;
    long sp54;
    fp = a0;
    s1 = 0x800c_0000L;
    s1 = s1 + 0x67f0L;
    sp4c = a1;
    sp50 = a2;
    sp54 = a3;
    v0 = MEMORY.ref(4, s1).offset(0xcL).get();
    s4 = (int)fp >> 8;
    s4 = s4 - v0;
    s3 = s4 >>> 31;
    s3 = s4 + s3;
    s3 = (int)s3 >> 1;
    lo = (long)(int)s3 * (int)s3 & 0xffff_ffffL;
    v0 = MEMORY.ref(4, s1).offset(0x10L).get();
    s5 = (int)a1 >> 8;
    s5 = s5 - v0;
    v0 = s5 >>> 31;
    s3 = lo;
    v0 = s5 + v0;
    v0 = (int)v0 >> 1;
    lo = (long)(int)v0 * (int)v0 & 0xffff_ffffL;
    v0 = MEMORY.ref(4, s1).offset(0x14L).get();
    s2 = (int)a2 >> 8;
    s2 = s2 - v0;
    s0 = s2 >>> 31;
    v1 = lo;
    s0 = s2 + s0;
    s0 = (int)s0 >> 1;
    lo = (long)(int)s0 * (int)s2 & 0xffff_ffffL;
    s6 = a4;
    s7 = a5;
    v1 = s3 + v1;
    a0 = lo;
    v0 = a0 >>> 31;
    a0 = a0 + v0;
    a0 = (int)a0 >> 1;
    a0 = v1 + a0;
    v0 = SquareRoot0(a0);
    a0 = s2;
    a1 = s4;
    v0 = v0 << 9;
    MEMORY.ref(4, s1).offset(0x68L).setu(v0);
    v0 = ratan2(a0, a1);
    lo = (long)(int)s0 * (int)s0 & 0xffff_ffffL;
    v0 = v0 & 0xfffL;
    v0 = v0 << 8;
    MEMORY.ref(4, s1).offset(0x60L).setu(v0);
    v1 = lo;
    a0 = s3 + v1;
    v0 = SquareRoot0(a0);
    a0 = s5;
    a1 = v0 << 1;
    v0 = ratan2(a0, a1);
    a0 = s6;
    a1 = s7;
    v0 = v0 & 0xfffL;
    v0 = v0 << 8;
    MEMORY.ref(4, s1).offset(0x64L).setu(v0);
    t0 = sp54;
    MEMORY.ref(4, s1).offset(0x5cL).setu(t0);
    a2 = MEMORY.ref(4, s1).offset(0x68L).get();
    a3 = MEMORY.ref(4, s1).offset(0x5cL).get();
    final Ref<Long> sp0x18 = new Ref<>();
    final Ref<Long> sp0x1c = new Ref<>();
    FUN_800dcebc(a0, a1, a2, a3, sp0x18, sp0x1c);
    a1 = sp0x18.get();
    a0 = sp0x1c.get();
    v0 = MEMORY.ref(4, s1).offset(0x5cL).get();
    a0 = a0 - a1;
    lo = (int)a0 / (int)v0;
    a0 = lo;
    MEMORY.ref(4, s1).offset(0x74L).setu(fp);
    t0 = sp4c;
    v1 = MEMORY.ref(4, s1).offset(0x11cL).get();
    v0 = 0x10L;
    MEMORY.ref(4, s1).offset(0x78L).setu(t0);
    t0 = sp50;
    v1 = v1 | 0x2L;
    MEMORY.ref(4, s1).offset(0x7cL).setu(t0);
    MEMORY.ref(1, s1).offset(0x121L).setu(v0);
    MEMORY.ref(4, s1).offset(0x30L).setu(a1);
    MEMORY.ref(4, s1).offset(0x11cL).setu(v1);
    MEMORY.ref(4, s1).offset(0x40L).setu(a0);
  }

  @Method(0x800d8decL)
  public static long FUN_800d8dec(final RunningScript a0) {
    final long s3 = a0.params_20.get(0).deref().get();
    final long s0 = a0.params_20.get(1).deref().get();
    final long s1 = a0.params_20.get(2).deref().get();
    final long s4 = a0.params_20.get(3).deref().get();

    final long s2 = rview2_800c67f0.getAddress();
    MEMORY.ref(1, s2).offset(0x118L).setu(0x1L);
    MEMORY.ref(4, s2).offset(0x100L).setu(getProjectionPlaneDistance() << 16);
    MEMORY.ref(4, s2).offset(0x104L).setu(s0 << 16);
    if((int)s0 < getProjectionPlaneDistance()) {
      //LAB_800d8e64
      MEMORY.ref(4, s2).offset(0x114L).setu(0x1L);
    } else {
      MEMORY.ref(4, s2).offset(0x114L).setu(0);
    }

    //LAB_800d8e68
    final long a2 = Math.abs(s0 - getProjectionPlaneDistance()) << 16;
    if(s3 == 0) {
      MEMORY.ref(4, s2).offset(0x108L).setu(s1);
      MEMORY.ref(4, s2).offset(0x110L).setu(0);
      MEMORY.ref(4, s2).offset(0x10cL).setu((int)a2 / (int)s1);
    } else {
      //LAB_800d8ea0
      MEMORY.ref(4, s2).offset(0x108L).setu(s1);

      final Ref<Long> sp0x18 = new Ref<>();
      final Ref<Long> sp0x1c = new Ref<>();
      FUN_800dcebc(s3 - 0x1L, s4 << 8, a2, s1, sp0x18, sp0x1c);
      MEMORY.ref(4, s2).offset(0x10cL).setu(sp0x18.get());
      MEMORY.ref(4, s2).offset(0x110L).setu((sp0x1c.get() - sp0x18.get()) / s1);
    }

    //LAB_800d8eec
    return 0;
  }

  @Method(0x800d8f10L)
  public static void FUN_800d8f10() {
    long v0;
    long v1;
    long t0;
    long s0;
    v0 = 0x800c_0000L;
    s0 = v0 + 0x67f0L;
    v0 = 0x800c_0000L;
    t0 = v0 + 0x6dd4L;
    long a1 = MEMORY.ref(4, t0).offset(0x0L).get();
    long a2 = MEMORY.ref(4, t0).offset(0x4L).get();
    long sp10 = a1;
    long sp14 = a2;

    if(MEMORY.ref(4, s0).offset(0x11cL).get() != 0) {
      if((MEMORY.ref(4, s0).offset(0x11cL).get() & 0x1L) != 0) {
        v0 = 0x8010_0000L;
        v0 = v0 - 0x5344L;
        v1 = MEMORY.ref(1, s0).offset(0x120L).get() * 0x4L;
        v1 = v1 + v0;
        MEMORY.ref(4, v1).offset(0x0L).deref(4).call();
      }

      //LAB_800d8f80
      if((MEMORY.ref(4, s0).offset(0x11cL).get() & 0x2L) != 0) {
        v0 = 0x8010_0000L;
        v0 = v0 - 0x52e4L;
        v1 = MEMORY.ref(1, s0).offset(0x121L).get() * 0x4L;
        v1 = v1 + v0;
        MEMORY.ref(4, v1).offset(0x0L).deref(4).call();
      }
    }

    //LAB_800d8fb4
    GsSetRefView2(rview2_800c67f0);
    FUN_800daa80();
    FUN_800d8fe0();
  }

  @Method(0x800d8fe0L)
  public static void FUN_800d8fe0() {
    final long s0 = rview2_800c67f0.getAddress();

    if(MEMORY.ref(1, s0).offset(0x118L).get() != 0 && MEMORY.ref(4, s0).offset(0x108L).get() == 0) {
      setProjectionPlaneDistance((int)MEMORY.ref(2, s0).offset(0x102L).getSigned());
      MEMORY.ref(1, s0).offset(0x118L).setu(0);
    }

    //LAB_800d9028
    if(MEMORY.ref(1, s0).offset(0x118L).get() != 0) {
      if(MEMORY.ref(4, s0).offset(0x108L).get() != 0) {
        if(MEMORY.ref(4, s0).offset(0x114L).get() == 0) {
          MEMORY.ref(4, s0).offset(0x100L).addu(MEMORY.ref(4, s0).offset(0x10cL).get());
        } else {
          //LAB_800d906c
          MEMORY.ref(4, s0).offset(0x100L).subu(MEMORY.ref(4, s0).offset(0x10cL).get());
        }

        //LAB_800d907c
        MEMORY.ref(4, s0).offset(0x10cL).addu(MEMORY.ref(4, s0).offset(0x110L).get());
        setProjectionPlaneDistance((int)MEMORY.ref(2, s0).offset(0x102L).getSigned());

        MEMORY.ref(4, s0).offset(0x108L).subu(0x1L);
        if(MEMORY.ref(4, s0).offset(0x108L).get() == 0) {
          MEMORY.ref(1, s0).offset(0x118L).setu(0);
        }
      }
    }

    //LAB_800d90b8
  }

  @Method(0x800d90c8L)
  public static void FUN_800d90c8() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long s0;
    v0 = 0x800c_0000L;
    s0 = v0 + 0x67f0L;
    a0 = MEMORY.ref(4, s0).offset(0x94L).get();
    v0 = MEMORY.ref(4, s0).offset(0xb0L).get();
    a1 = MEMORY.ref(4, s0).offset(0x98L).get();
    a2 = MEMORY.ref(4, s0).offset(0x9cL).get();
    v1 = MEMORY.ref(4, s0).offset(0xc8L).get();
    a0 = a0 + v0;
    v0 = MEMORY.ref(4, s0).offset(0xbcL).get();
    a2 = a2 + v1;
    MEMORY.ref(4, s0).offset(0x94L).setu(a0);
    a0 = (int)a0 >> 8;
    MEMORY.ref(4, s0).offset(0x9cL).setu(a2);
    a2 = (int)a2 >> 8;
    a1 = a1 + v0;
    MEMORY.ref(4, s0).offset(0x98L).setu(a1);
    a1 = (int)a1 >> 8;
    setViewpoint((int)a0, (int)a1, (int)a2);
    v0 = MEMORY.ref(4, s0).offset(0xd0L).get();

    v0 = v0 - 0x1L;
    if((int)v0 > 0) {
      MEMORY.ref(4, s0).offset(0xd0L).setu(v0);
    } else {
      MEMORY.ref(4, s0).offset(0xd0L).setu(v0);
      v0 = MEMORY.ref(4, s0).offset(0x11cL).get();
      v1 = -0x2L;
      MEMORY.ref(1, s0).offset(0x122L).setu(0);
      v0 = v0 & v1;
      MEMORY.ref(4, s0).offset(0x11cL).setu(v0);
    }

    //LAB_800d9144
  }

  @Method(0x800d9220L)
  public static void FUN_800d9220() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long s0;
    v0 = 0x800c_0000L;
    s0 = v0 + 0x67f0L;
    a1 = MEMORY.ref(4, s0).offset(0x94L).get();
    v0 = MEMORY.ref(4, s0).offset(0xb0L).get();
    v1 = MEMORY.ref(4, s0).offset(0x98L).get();
    a2 = MEMORY.ref(4, s0).offset(0x9cL).get();
    a0 = MEMORY.ref(4, s0).offset(0xc8L).get();
    a1 = a1 + v0;
    a2 = a2 + a0;
    MEMORY.ref(4, s0).offset(0x94L).setu(a1);
    a1 = (int)a1 >> 8;
    MEMORY.ref(4, s0).offset(0x9cL).setu(a2);
    a2 = (int)a2 >> 8;
    v0 = MEMORY.ref(4, s0).offset(0xbcL).get();
    a0 = MEMORY.ref(4, s0).offset(0xcL).get();
    v1 = v1 + v0;
    MEMORY.ref(4, s0).offset(0x98L).setu(v1);
    v1 = (int)v1 >> 8;
    a0 = a0 + a1;
    a1 = MEMORY.ref(4, s0).offset(0x10L).get();
    v0 = MEMORY.ref(4, s0).offset(0x14L).get();
    a1 = a1 + v1;
    a2 = v0 + a2;
    setViewpoint((int)a0, (int)a1, (int)a2);
    v0 = MEMORY.ref(4, s0).offset(0xd0L).get();

    v0 = v0 - 0x1L;
    MEMORY.ref(4, s0).offset(0xd0L).setu(v0);
    if((int)v0 <= 0) {
      v0 = 0x4L;
      MEMORY.ref(1, s0).offset(0x122L).setu(0);
      MEMORY.ref(1, s0).offset(0x120L).setu(v0);
    }

    //LAB_800d92ac
  }

  @Method(0x800d9518L)
  public static void FUN_800d9518() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    a2 = 0x8010_0000L;
    a0 = _800fab98.getAddress();
    s2 = rview2_800c67f0.getAddress();
    MEMORY.ref(2, a0).offset(0x4L).setu(0);
    v0 = MEMORY.ref(4, s2).offset(0xd4L).get();
    v1 = MEMORY.ref(4, s2).offset(0xd8L).get();
    v0 = (int)v0 >> 8;
    v1 = (int)v1 >> 8;
    MEMORY.ref(2, a2).offset(-0x5468L).setu(v0);
    MEMORY.ref(2, a0).offset(0x2L).setu(v1);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    v0 = 0x8010_0000L;
    a0 = _800faba0.getAddress();
    s1 = 0x8010_0000L;
    s0 = _800faba8.getAddress();
    MEMORY.ref(2, v0).offset(-0x5460L).setu(0);
    MEMORY.ref(2, a0).offset(0x2L).setu(0);
    v1 = MEMORY.ref(4, s2).offset(0xa4L).get();
    a3 = MEMORY.ref(4, s2).offset(0xb4L).get();
    v0 = MEMORY.ref(4, s2).offset(0xdcL).get();
    v1 = v1 + a3;
    v0 = v0 - v1;
    MEMORY.ref(4, s2).offset(0xdcL).setu(v0);
    v0 = (int)v0 >> 8;
    MEMORY.ref(4, s2).offset(0xa4L).setu(v1);
    MEMORY.ref(2, a0).offset(0x4L).setu(v0);
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    v0 = MEMORY.ref(4, s0).offset(0x8L).get();
    a3 = MEMORY.ref(4, s2).offset(0xe8L).get();
    v1 = MEMORY.ref(4, s2).offset(0xecL).get();
    a2 = MEMORY.ref(4, s2).offset(0xf0L).get();
    v0 = v0 << 8;
    a3 = a3 - v0;
    v0 = MEMORY.ref(4, s1).offset(-0x5458L).get();
    a0 = (int)a3 >> 8;
    v0 = v0 << 8;
    v1 = v1 - v0;
    v0 = MEMORY.ref(4, s0).offset(0x4L).get();
    a1 = (int)v1 >> 8;
    MEMORY.ref(4, s2).offset(0x94L).setu(a3);
    MEMORY.ref(4, s2).offset(0x98L).setu(v1);
    v0 = v0 << 8;
    v0 = v0 + a2;
    a2 = (int)v0 >> 8;
    MEMORY.ref(4, s2).offset(0x9cL).setu(v0);
    setViewpoint((int)a0, (int)a1, (int)a2);
    v0 = MEMORY.ref(4, s2).offset(0xd0L).get();

    v0 = v0 - 0x1L;
    MEMORY.ref(4, s2).offset(0xd0L).setu(v0);
    if((int)v0 <= 0) {
      v0 = MEMORY.ref(4, s2).offset(0x11cL).get();
      v1 = -0x2L;
      MEMORY.ref(1, s2).offset(0x122L).setu(0);
      v0 = v0 & v1;
      MEMORY.ref(4, s2).offset(0x11cL).setu(v0);
    }

    //LAB_800d9638
  }

  @Method(0x800d9788L)
  public static void FUN_800d9788() {
    final long s2 = rview2_800c67f0.getAddress();
    _800fab98.setX((short)(MEMORY.ref(4, s2).offset(0xd4L).get() / 0x100));
    _800fab98.setY((short)(MEMORY.ref(4, s2).offset(0xd8L).get() / 0x100));
    _800fab98.setZ((short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    final long v1 = MEMORY.ref(4, s2).offset(0xa4L).get() + MEMORY.ref(4, s2).offset(0xb4L).get();
    MEMORY.ref(4, s2).offset(0xdcL).subu(v1);
    MEMORY.ref(4, s2).offset(0xa4L).setu(v1);
    _800faba0.setZ((short)(MEMORY.ref(4, s2).offset(0xdcL).get() / 0x100));
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);

    final long t0 = MEMORY.ref(4, s2).offset(0xe8L).get() - _800faba8.getZ() * 0x100;
    MEMORY.ref(4, s2).offset(0x94L).setu(t0);
    final long a3 = MEMORY.ref(4, s2).offset(0xecL).get() - _800faba8.getX() * 0x100;
    MEMORY.ref(4, s2).offset(0x98L).setu(a3);
    final long v0 = MEMORY.ref(4, s2).offset(0xf0L).get() + _800faba8.getY() * 0x100;
    MEMORY.ref(4, s2).offset(0x9cL).setu(v0);

    setViewpoint(
      (int)(MEMORY.ref(4, s2).offset(0x0cL).get() + (int)t0 / 0x100),
      (int)(MEMORY.ref(4, s2).offset(0x10L).get() + (int)a3 / 0x100),
      (int)(MEMORY.ref(4, s2).offset(0x14L).get() + (int)v0 / 0x100)
    );

    MEMORY.ref(4, s2).offset(0xd0L).subu(0x1L);
    if((int)MEMORY.ref(4, s2).offset(0xd0L).get() <= 0) {
      MEMORY.ref(1, s2).offset(0x122L).setu(0);
      MEMORY.ref(1, s2).offset(0x120L).setu(0x4L);
    }

    //LAB_800d98b8
  }

  @Method(0x800d9a68L)
  public static void FUN_800d9a68() {
    final long s3 = rview2_800c67f0.getAddress();
    final ScriptState<BtldScriptData27c> s2 = scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, s3).offset(0xf4L).get()).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    _800fab98.setX((short)(MEMORY.ref(4, s3).offset(0xd4L).getSigned() / 0x100));
    _800fab98.setY((short)(MEMORY.ref(4, s3).offset(0xd8L).getSigned() / 0x100));
    _800fab98.setZ((short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    MEMORY.ref(4, s3).offset(0xa4L).addu(MEMORY.ref(4, s3).offset(0xb4L).get());
    MEMORY.ref(4, s3).offset(0xdcL).subu(MEMORY.ref(4, s3).offset(0xa4L).get());
    _800faba0.setZ((short)(MEMORY.ref(4, s3).offset(0xdcL).get() / 0x100));
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    MEMORY.ref(4, s3).offset(0x94L).setu(MEMORY.ref(4, s3).offset(0xe8L).getSigned() - _800faba8.getZ() * 0x100);
    MEMORY.ref(4, s3).offset(0x98L).setu(MEMORY.ref(4, s3).offset(0xecL).getSigned() - _800faba8.getX() * 0x100);
    MEMORY.ref(4, s3).offset(0x9cL).setu(MEMORY.ref(4, s3).offset(0xf0L).getSigned() + _800faba8.getY() * 0x100);
    final BtldScriptData27c v0 = s2.innerStruct_00.deref();
    setViewpoint((int)(v0._148.coord2_14.coord.transfer.getX() + MEMORY.ref(4, s3).offset(0x94L).getSigned() / 0x100), (int)(v0._148.coord2_14.coord.transfer.getY() + MEMORY.ref(4, s3).offset(0x98L).getSigned() / 0x100), (int)(v0._148.coord2_14.coord.transfer.getZ() + MEMORY.ref(4, s3).offset(0x9cL).getSigned() / 0x100));

    MEMORY.ref(4, s3).offset(0xd0L).subu(0x1L);
    if((int)MEMORY.ref(4, s3).offset(0xd0L).get() <= 0) {
      MEMORY.ref(1, s3).offset(0x122L).setu(0);
      MEMORY.ref(1, s3).offset(0x120L).setu(0x6L);
    }

    //LAB_800d9bb8
  }

  @Method(0x800d9da0L)
  public static void FUN_800d9da0() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long s0;
    v0 = 0x800c_0000L;
    s0 = v0 + 0x67f0L;
    a0 = MEMORY.ref(4, s0).offset(0x20L).get();
    v0 = MEMORY.ref(4, s0).offset(0x3cL).get();
    a1 = MEMORY.ref(4, s0).offset(0x24L).get();
    a2 = MEMORY.ref(4, s0).offset(0x28L).get();
    v1 = MEMORY.ref(4, s0).offset(0x54L).get();
    a0 = a0 + v0;
    v0 = MEMORY.ref(4, s0).offset(0x48L).get();
    a2 = a2 + v1;
    MEMORY.ref(4, s0).offset(0x20L).setu(a0);
    a0 = (int)a0 >> 8;
    MEMORY.ref(4, s0).offset(0x28L).setu(a2);
    a2 = (int)a2 >> 8;
    a1 = a1 + v0;
    MEMORY.ref(4, s0).offset(0x24L).setu(a1);
    a1 = (int)a1 >> 8;
    setRefpoint((int)a0, (int)a1, (int)a2);
    v0 = MEMORY.ref(4, s0).offset(0x5cL).get();

    v0 = v0 - 0x1L;
    MEMORY.ref(4, s0).offset(0x5cL).setu(v0);
    if((int)v0 <= 0) {
      v0 = MEMORY.ref(4, s0).offset(0x11cL).get();
      v1 = -0x3L;
      MEMORY.ref(1, s0).offset(0x123L).setu(0);
      v0 = v0 & v1;
      MEMORY.ref(4, s0).offset(0x11cL).setu(v0);
    }

    //LAB_800d9e1c
  }

  @Method(0x800da058L)
  public static void FUN_800da058() {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0x20L).addu(MEMORY.ref(4, s0).offset(0x3cL).get());
    MEMORY.ref(4, s0).offset(0x24L).addu(MEMORY.ref(4, s0).offset(0x48L).get());
    MEMORY.ref(4, s0).offset(0x28L).addu(MEMORY.ref(4, s0).offset(0x54L).get());

    final ScriptState<?> a2 = scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, s0).offset(0x80L).get()).deref();
    final BtldScriptData27c v0 = a2.innerStruct_00.derefAs(BtldScriptData27c.class);
    setRefpoint((int)(v0._148.coord2_14.coord.transfer.getX() + MEMORY.ref(4, s0).offset(0x20L).getSigned() / 0x100), (int)(v0._148.coord2_14.coord.transfer.getY() + MEMORY.ref(4, s0).offset(0x24L).getSigned() / 0x100), (int)(v0._148.coord2_14.coord.transfer.getY() + MEMORY.ref(4, s0).offset(0x28L).getSigned() / 0x100));
    MEMORY.ref(4, s0).offset(0x5cL).subu(0x1L);
    if((int)MEMORY.ref(4, s0).offset(0x5cL).get() <= 0) {
      MEMORY.ref(1, s0).offset(0x123L).setu(0);
      MEMORY.ref(1, s0).offset(0x121L).setu(0x6L);
    }

    //LAB_800da100
  }

  @Method(0x800da1f0L)
  public static void FUN_800da1f0() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    a2 = 0x8010_0000L;
    v0 = 0x800c_0000L;
    s2 = v0 + 0x67f0L;
    _800fab98.setZ((short)0);
    v0 = MEMORY.ref(4, s2).offset(0x60L).get();
    v1 = MEMORY.ref(4, s2).offset(0x64L).get();
    v0 = (int)v0 >> 8;
    v1 = (int)v1 >> 8;
    MEMORY.ref(2, a2).offset(-0x5468L).setu(v0);
    _800fab98.setY((short)v1);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    v0 = 0x8010_0000L;
    s1 = 0x8010_0000L;
    s0 = _800faba8.getAddress();
    MEMORY.ref(2, v0).offset(-0x5460L).setu(0);
    _800faba0.setY((short)0);
    v1 = MEMORY.ref(4, s2).offset(0x30L).get();
    a3 = MEMORY.ref(4, s2).offset(0x40L).get();
    v0 = MEMORY.ref(4, s2).offset(0x68L).get();
    v1 = v1 + a3;
    v0 = v0 - v1;
    MEMORY.ref(4, s2).offset(0x68L).setu(v0);
    v0 = (int)v0 >> 8;
    MEMORY.ref(4, s2).offset(0x30L).setu(v1);
    _800faba0.setZ((short)v0);
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    v0 = MEMORY.ref(4, s0).offset(0x8L).get();
    a3 = MEMORY.ref(4, s2).offset(0x74L).get();
    v1 = MEMORY.ref(4, s2).offset(0x78L).get();
    a2 = MEMORY.ref(4, s2).offset(0x7cL).get();
    v0 = v0 << 8;
    a3 = a3 - v0;
    v0 = MEMORY.ref(4, s1).offset(-0x5458L).get();
    a0 = (int)a3 >> 8;
    v0 = v0 << 8;
    v1 = v1 - v0;
    v0 = MEMORY.ref(4, s0).offset(0x4L).get();
    a1 = (int)v1 >> 8;
    MEMORY.ref(4, s2).offset(0x20L).setu(a3);
    MEMORY.ref(4, s2).offset(0x24L).setu(v1);
    v0 = v0 << 8;
    v0 = v0 + a2;
    a2 = (int)v0 >> 8;
    MEMORY.ref(4, s2).offset(0x28L).setu(v0);
    setRefpoint((int)a0, (int)a1, (int)a2);
    v0 = MEMORY.ref(4, s2).offset(0x5cL).get();

    v0 = v0 - 0x1L;
    MEMORY.ref(4, s2).offset(0x5cL).setu(v0);
    if((int)v0 <= 0) {
      v0 = MEMORY.ref(4, s2).offset(0x11cL).get();
      v1 = -0x3L;
      MEMORY.ref(1, s2).offset(0x123L).setu(0);
      v0 = v0 & v1;
      MEMORY.ref(4, s2).offset(0x11cL).setu(v0);
    }

    //LAB_800da310
  }

  @Method(0x800daa80L)
  public static void FUN_800daa80() {
    if(_800fabb8.get() == 0x1L) { //1b
      if(_800c67d4.get() != 0) {
        _800c67d4.subu(0x1L);
        return;
      }

      //LAB_800daabc
      final long x;
      final long y;
      final long a0 = _800bb0fc.get() & 0x3L;
      if(a0 == 0) {
        //LAB_800dab04
        x = _800c67e4.get();
        y = _800c67e8.get() * 0x2L;
      } else if(a0 == 0x1L) {
        //LAB_800dab1c
        x = -_800c67e4.get() * 0x2L;
        y = -_800c67e8.get();
        //LAB_800daaec
      } else if(a0 == 0x2L) {
        //LAB_800dab3c
        x = _800c67e4.get() * 0x2L;
        y = _800c67e8.get();
      } else {
        //LAB_800dab54
        x = -_800c67e4.get();
        y = -_800c67e8.get() * 0x2L;
      }

      //LAB_800dab70
      //LAB_800dab78
      SetGeomOffset((int)(x_800c67bc.get() + x), (int)(y_800c67c0.get() + y));

      _800c67c4.subu(0x1L);
      if((int)_800c67c4.get() <= 0) {
        _800fabb8.setu(0); //1b
        SetGeomOffset((int)x_800c67bc.get(), (int)y_800c67c0.get());
      }
    }

    //LAB_800dabb8
  }

  @Method(0x800dabccL)
  public static long FUN_800dabcc(final RunningScript a0) {
    FUN_800dabec();
    return 0;
  }

  @Method(0x800dabecL)
  public static void FUN_800dabec() {
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(2, v0).offset(0x8cL).setu(0);
    MEMORY.ref(2, v0).offset(0x8eL).setu(0);
    MEMORY.ref(2, v0).offset(0x90L).setu(0);
    MEMORY.ref(4, v0).offset(0x108L).setu(0);
    MEMORY.ref(1, v0).offset(0x118L).setu(0);
    MEMORY.ref(4, v0).offset(0x11cL).setu(0);
    MEMORY.ref(1, v0).offset(0x120L).setu(0);
    MEMORY.ref(1, v0).offset(0x121L).setu(0);
    MEMORY.ref(1, v0).offset(0x122L).setu(0);
    MEMORY.ref(1, v0).offset(0x123L).setu(0);
  }

  @Method(0x800dac20L)
  public static long FUN_800dac20(final RunningScript a0) {
    FUN_800dac70(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get());
    return 0;
  }

  @Method(0x800dac70L)
  public static void FUN_800dac70(final long index, final long a1, final long a2, final long a3, final long a4) {
    _800fabbc.offset(index * 0x4L).deref(4).call(a1, a2, a3, a4);
    _800c68ec.setu(index);
  }

  @Method(0x800dacc4L)
  public static void FUN_800dacc4(final long x, final long y, final long z, final long a3) {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0x94L).setu(x);
    MEMORY.ref(4, s0).offset(0x98L).setu(y);
    MEMORY.ref(4, s0).offset(0x9cL).setu(z);
    setViewpoint((int)x >> 8, (int)y >> 8, (int)z >> 8);
    MEMORY.ref(1, s0).offset(0x120L).setu(0);
    MEMORY.ref(4, s0).offset(0x11cL).oru(0x1L);
  }

  @Method(0x800dad14L)
  public static void FUN_800dad14(final long x, final long y, final long z, final long a3) {
    final Ref<Long> refX = new Ref<>(x);
    final Ref<Long> refY = new Ref<>(y);
    final Ref<Long> refZ = new Ref<>(z);
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0xacL).setu(x);
    MEMORY.ref(4, s0).offset(0xb8L).setu(y);
    MEMORY.ref(4, s0).offset(0xa0L).setu(z);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setViewpoint(refX.get().intValue() >> 8, refY.get().intValue() >> 8, refZ.get().intValue() >> 8);

    MEMORY.ref(1, s0).offset(0x120L).setu(0x1L);
    MEMORY.ref(4, s0).offset(0x11cL).oru(0x1L);
  }

  @Method(0x800daedcL)
  public static void FUN_800daedc(long x, long y, long z, long a3) {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0x94L).setu(x);
    MEMORY.ref(4, s0).offset(0x98L).setu(y);
    MEMORY.ref(4, s0).offset(0x9cL).setu(z);
    final long v0 = FUN_800dd02c(a3);
    setViewpoint(
      (int)(MEMORY.ref(4, v0).offset(0x0L).get() + ((int)MEMORY.ref(4, s0).offset(0x94L).get() >> 8)),
      (int)(MEMORY.ref(4, v0).offset(0x4L).get() + ((int)MEMORY.ref(4, s0).offset(0x98L).get() >> 8)),
      (int)(MEMORY.ref(4, v0).offset(0x8L).get() + ((int)MEMORY.ref(4, s0).offset(0x9cL).get() >> 8))
    );
    MEMORY.ref(4, s0).offset(0xf4L).setu(a3);
    MEMORY.ref(1, s0).offset(0x120L).setu(0x6L);
    MEMORY.ref(4, s0).offset(0x11cL).oru(0x1L);
  }

  @Method(0x800db034L)
  public static long FUN_800db034(final RunningScript a0) {
    FUN_800db084(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get());
    return 0;
  }

  @Method(0x800db084L)
  public static void FUN_800db084(final long index, final long a1, final long a2, final long a3, final long a4) {
    _800fabdc.offset(index * 0x4L).deref(4).call(a1, a2, a3, a4);
    _800c6878.setu(index);
  }

  @Method(0x800db0d8L)
  public static void FUN_800db0d8(final long x, final long y, final long z, final long a3) {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0x20L).setu(x);
    MEMORY.ref(4, s0).offset(0x24L).setu(y);
    MEMORY.ref(4, s0).offset(0x28L).setu(z);
    setRefpoint((int)x >> 8, (int)y >> 8, (int)z >> 8);
    MEMORY.ref(1, s0).offset(0x121L).setu(0);
    MEMORY.ref(4, s0).offset(0x11cL).oru(0x2L);
  }

  @Method(0x800db128L)
  public static void FUN_800db128(final long x, final long y, final long z, final long a3) {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0x38L).setu(x);
    MEMORY.ref(4, s0).offset(0x44L).setu(y);
    MEMORY.ref(4, s0).offset(0x2cL).setu(z);
    final Ref<Long> refX = new Ref<>((long)((int)x >> 8));
    final Ref<Long> refY = new Ref<>((long)((int)y >> 8));
    final Ref<Long> refZ = new Ref<>((long)((int)z >> 8));
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setRefpoint(refX.get().intValue(), refY.get().intValue(), refZ.get().intValue());
    MEMORY.ref(1, s0).offset(0x121L).setu(0x1L);
    MEMORY.ref(4, s0).offset(0x11cL).oru(0x2L);
  }

  @Method(0x800db2f0L)
  public static long FUN_800db2f0(long a0, long a1, long a2, long a3) {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0x20L).setu(a0);
    MEMORY.ref(4, s0).offset(0x24L).setu(a1);
    MEMORY.ref(4, s0).offset(0x28L).setu(a2);

    final long v0 = FUN_800dd02c(a3);
    setRefpoint(
      (int)(MEMORY.ref(4, v0).offset(0x0L).get() + ((int)a0 >> 8)),
      (int)(MEMORY.ref(4, v0).offset(0x4L).get() + ((int)a1 >> 8)),
      (int)(MEMORY.ref(4, v0).offset(0x8L).get() + ((int)a2 >> 8))
    );

    MEMORY.ref(4, s0).offset(0x80L).setu(a3);
    MEMORY.ref(1, s0).offset(0x121L).setu(0x6L);
    MEMORY.ref(4, s0).offset(0x11cL).oru(0x2L);
    return MEMORY.ref(4, s0).offset(0x11cL).get();
  }

  @Method(0x800db460L)
  public static long FUN_800db460(final RunningScript a0) {
    FUN_800db4ec(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x800db4ecL)
  public static void FUN_800db4ec(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    _800fabfc.offset(a0 * 0x4L).deref(4).call(a1, a2, a3, a5, a6, a4, a7);
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, v0).offset(0xfcL).setu(a0);
    MEMORY.ref(1, v0).offset(0x122L).setu(0x1L);
  }

  @Method(0x800db574L)
  public static long FUN_800db574(final RunningScript a0) {
    FUN_800db600(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x800db600L)
  public static void FUN_800db600(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    _800fac5c.offset(a0 * 0x4L).deref(4).call(a1, a2, a3, a5, a6, a4, a7);
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, v0).offset(0x88L).setu(a0);
    MEMORY.ref(1, v0).offset(0x123L).setu(0x1L);
  }

  @Method(0x800db8b0L)
  public static long FUN_800db8b0(final RunningScript a0) {
    FUN_800db950(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get(), a0.params_20.get(8).deref().get());
    return 0;
  }

  @Method(0x800db950L)
  public static void FUN_800db950(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7, final long a8) {
    _800fac3c.offset(a0 * 0x4L).deref(4).call(a1, a2, a3, a4, a5, a6, a7, a8);
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, v0).offset(0xfcL).setu(a0);
    MEMORY.ref(1, v0).offset(0x122L).setu(0x1L);
  }

  @Method(0x800db9e0L)
  public static long FUN_800db9e0(final RunningScript a0) {
    FUN_800dba80(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get(), a0.params_20.get(8).deref().get());
    return 0;
  }

  @Method(0x800dba80L)
  public static void FUN_800dba80(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7, final long a8) {
    _800fac9c.offset(a0 * 0x4L).deref(4).call(a1, a2, a3, a4, a5, a6, a7, a8);
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, v0).offset(0x88L).setu(a0);
    MEMORY.ref(1, v0).offset(0x123L).setu(0x1L);
  }

  @Method(0x800dbb10L)
  public static long FUN_800dbb10(final RunningScript a0) {
    final long v1 = a0.params_20.get(0).deref().get();
    final long a1;
    if(v1 == 0) {
      //LAB_800dbb3c
      a1 = _800c6912.get();
    } else if(v1 == 0x1L) {
      //LAB_800dbb48
      a1 = _800c6913.get();
    } else {
      throw new RuntimeException("Undefined a1");
    }

    //LAB_800dbb50
    a0.params_20.get(1).deref().set((int)a1);
    return 0;
  }

  @Method(0x800dbe60L)
  public static void FUN_800dbe60() {
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(1, v0).offset(0x122L).setu(0);
    MEMORY.ref(4, v0).offset(0x11cL).and(0xffff_fffeL);
  }

  @Method(0x800dbe98L)
  public static void FUN_800dbe98() {
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(1, v0).offset(0x122L).setu(0);

    setViewpoint(
      (int)(MEMORY.ref(4, v0).offset(0x0cL).get() + (int)MEMORY.ref(4, v0).offset(0x94L).get() / 0x100),
      (int)(MEMORY.ref(4, v0).offset(0x10L).get() + (int)MEMORY.ref(4, v0).offset(0x98L).get() / 0x100),
      (int)(MEMORY.ref(4, v0).offset(0x14L).get() + (int)MEMORY.ref(4, v0).offset(0x9cL).get() / 0x100)
    );
  }

  @Method(0x800dbf70L)
  public static void FUN_800dbf70() {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(1, s0).offset(0x122L).setu(0);

    final long v0 = FUN_800dd02c(MEMORY.ref(4, s0).offset(0xf4L).get());
    setViewpoint(
      (int)(MEMORY.ref(4, v0).offset(0x0L).get() + MEMORY.ref(4, s0).offset(0x94L).get() / 0x100),
      (int)(MEMORY.ref(4, v0).offset(0x4L).get() + MEMORY.ref(4, s0).offset(0x98L).get() / 0x100),
      (int)(MEMORY.ref(4, v0).offset(0x8L).get() + MEMORY.ref(4, s0).offset(0x9cL).get() / 0x100)
    );
  }

  @Method(0x800dc090L)
  public static void FUN_800dc090() {
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(1, v0).offset(0x123L).setu(0);
    MEMORY.ref(4, v0).offset(0x11cL).and(0xffff_fffdL);
  }

  @Method(0x800dc0b0L)
  public static void FUN_800dc0b0() {
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(1, v0).offset(0x123L).setu(0);
    MEMORY.ref(4, v0).offset(0x11cL).and(0xffff_fffdL);
  }

  @Method(0x800dc1b8L)
  public static void FUN_800dc1b8() {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(1, s0).offset(0x123L).setu(0);

    final long v0 = FUN_800dd02c(MEMORY.ref(4, s0).offset(0x80L).get());
    setRefpoint(
      (int)(MEMORY.ref(4, v0).offset(0x0L).get() + ((int)MEMORY.ref(4, s0).offset(0x20L).get() >> 8)),
      (int)(MEMORY.ref(4, v0).offset(0x4L).get() + ((int)MEMORY.ref(4, s0).offset(0x24L).get() >> 8)),
      (int)(MEMORY.ref(4, v0).offset(0x8L).get() + ((int)MEMORY.ref(4, s0).offset(0x28L).get() >> 8))
    );
  }

  @Method(0x800dc2d8L)
  public static long FUN_800dc2d8(final RunningScript s0) {
    final long x;
    final long y;
    final long z;
    final long v1;
    if(s0.params_20.get(0).deref().get() == 0) {
      x = rview2_800c67f0.viewpoint_00.getX();
      y = rview2_800c67f0.viewpoint_00.getY();
      z = rview2_800c67f0.viewpoint_00.getZ();
      v1 = _800fad7c.getAddress();
    } else {
      //LAB_800dc32c
      x = rview2_800c67f0.refpoint_0c.getX();
      y = rview2_800c67f0.refpoint_0c.getY();
      z = rview2_800c67f0.refpoint_0c.getZ();
      v1 = _800fad9c.getAddress();
    }

    //LAB_800dc344
    s0.params_20.get(4).deref().set((int)(long)MEMORY.ref(4, v1).offset(s0.params_20.get(1).deref().get() * 0x4L).deref(4).call(s0.params_20.get(2).deref().get(), s0.params_20.get(3).deref().get(), x, y, z));
    return 0;
  }

  @Method(0x800dc384L)
  public static long FUN_800dc384(long a0, long a1, long a2, long a3) {
    long v0;
    long v1 = a2;
    long t0 = a3;
    if(a0 != 0) {
      v0 = 0x800c_0000L;
      v0 = v0 + 0x67f0L;
      a0 = v1;
      a2 = MEMORY.ref(4, v0).offset(0xcL).get();
      a3 = MEMORY.ref(4, v0).offset(0x10L).get();
      v0 = MEMORY.ref(4, v0).offset(0x14L).get();
      v1 = 0x8010_0000L;
      v1 = v1 - 0x5264L;
    } else {
      //LAB_800dc3bc
      v0 = 0x800c_0000L;
      a2 = MEMORY.ref(4, v0).offset(0x67f0L).get();
      v0 = v0 + 0x67f0L;
      a0 = v1;
      a3 = MEMORY.ref(4, v0).offset(0x4L).get();
      v0 = MEMORY.ref(4, v0).offset(0x8L).get();
      v1 = 0x8010_0000L;
      v1 = v1 - 0x5284L;
    }

    //LAB_800dc3dc
    return (long)MEMORY.ref(4, v1).offset(a1 * 0x4L).deref(4).call(a0, t0, a2, a3, v0);
  }

  @Method(0x800dc408L)
  public static long FUN_800dc408(final long a0, final long a1, final long x, final long y, final long z) {
    if(a0 == 0) {
      //LAB_800dc440
      return x;
    }

    if(a0 == 0x1L) {
      //LAB_800dc448
      return y;
    }

    //LAB_800dc42c
    if(a0 == 0x2L) {
      //LAB_800dc450
      return z;
    }

    if((int)a0 > 0x2L) {
      return 0x2L;
    }

    return 0x1L;
  }

  @Method(0x800dc45cL)
  public static long FUN_800dc45c(final long a0, final long a1, final long x, final long y, final long z) {
    final Ref<Long> refX = new Ref<>(x);
    final Ref<Long> refY = new Ref<>(y);
    final Ref<Long> refZ = new Ref<>(z);
    FUN_800dcd9c(0, 0, 0, refX, refY, refZ);

    if(a0 == 0) {
      //LAB_800dc4d8
      return refX.get();
    }

    if(a0 == 0x1L) {
      //LAB_800dc4e4
      return refY.get();
    }

    //LAB_800dc4c4
    if(a0 == 0x2L) {
      //LAB_800dc4f0
      return refZ.get();
    }

    if((int)a0 > 0x2L) {
      return 0x2L;
    }

    //LAB_800dc4f4
    return 0x1L;
  }

  @Method(0x800dc514L)
  public static long FUN_800dc514(final long a0, final long a1, final long x, final long y, final long z) {
    if(a0 == 0) {
      //LAB_800dc550
      return x - rview2_800c67f0.refpoint_0c.getX();
    }

    if(a0 == 0x1L) {
      //LAB_800dc560
      return y - rview2_800c67f0.refpoint_0c.getY();
    }

    //LAB_800dc53c
    if(a0 == 0x2L) {
      //LAB_800dc56c
      return z - rview2_800c67f0.refpoint_0c.getZ();
    }

    if((int)a0 > 0x2L) {
      return 0x2L;
    }

    return 0x1L;
  }

  @Method(0x800dc580L)
  public static long FUN_800dc580(final long a0, final long a1, final long x, final long y, final long z) {
    final Ref<Long> refX = new Ref<>(x);
    final Ref<Long> refY = new Ref<>(y);
    final Ref<Long> refZ = new Ref<>(z);
    FUN_800dcd9c(rview2_800c67f0.refpoint_0c.getX(), rview2_800c67f0.refpoint_0c.getY(), rview2_800c67f0.refpoint_0c.getZ(), refX, refY, refZ);

    if(a0 == 0) {
      //LAB_800dc604
      return refX.get();
    }

    if(a0 == 0x1L) {
      //LAB_800dc610
      return refY.get();
    }

    if(a0 == 0x2L) {
      //LAB_800dc61c
      return refZ.get();
    }

    if((int)a0 > 0x2L) {
      //LAB_800dc5f0
      return 0x2L;
    }

    //LAB_800dc620
    return 0x1L;
  }

  @Method(0x800dc630L)
  public static long FUN_800dc630(final long a0, final long a1, final long x, final long y, final long z) {
    final long v1 = FUN_800dd02c(a1);

    if(a0 == 0) {
      //LAB_800dc698
      return x - MEMORY.ref(4, v1).offset(0x0L).get();
    }

    if(a0 == 0x1L) {
      //LAB_800dc6a4
      return y - MEMORY.ref(4, v1).offset(0x4L).get();
    }

    if(a0 == 0x2L) {
      //LAB_800dc6b0
      return z - MEMORY.ref(4, v1).offset(0x8L).get();
    }

    if((int)a0 > 0x2L) {
      //LAB_800dc684
      return 2;
    }

    //LAB_800dc6c0
    return 1;
  }

  @Method(0x800dc798L)
  public static long FUN_800dc798(final long a0, final long a1, final long x, final long y, final long z) {
    if(a0 == 0) {
      //LAB_800dc7d0
      return x;
    }

    if(a0 == 0x1L) {
      //LAB_800dc7d8
      return y;
    }

    //LAB_800dc7bc
    if(a0 == 0x2L) {
      //LAB_800dc7e0
      return z;
    }

    if((int)a0 > 0x2L) {
      return 0x2L;
    }

    return 0x1L;
  }

  @Method(0x800dc7ecL)
  public static long FUN_800dc7ec(final long a0, final long a1, final long x, final long y, final long z) {
    final Ref<Long> refX = new Ref<>(x);
    final Ref<Long> refY = new Ref<>(y);
    final Ref<Long> refZ = new Ref<>(z);
    FUN_800dcd9c(0, 0, 0, refX, refY, refZ);

    if(a0 == 0) {
      //LAB_800dc868
      return refX.get();
    }

    if(a0 == 0x1L) {
      //LAB_800dc874
      return refY.get();
    }

    if(a0 == 0x2L) {
      //LAB_800dc880
      return refZ.get();
    }

    if((int)a0 > 0x2L) {
      //LAB_800dc854
      return 0x2L;
    }

    //LAB_800dc884
    return 0x1L;
  }

  @Method(0x800dc9c0L)
  public static long FUN_800dc9c0(final long a0, final long a1, final long x, final long y, final long z) {
    final long v1 = FUN_800dd02c(a1);

    if(a0 == 0) {
      //LAB_800dca28
      return x - MEMORY.ref(4, v1).offset(0x0L).get();
    }

    if(a0 == 0x1L) {
      //LAB_800dca34
      return y - MEMORY.ref(4, v1).offset(0x4L).get();
    }

    if(a0 == 0x2L) {
      //LAB_800dca40
      return z - MEMORY.ref(4, v1).offset(0x8L).get();
    }

    if((int)a0 > 0x2L) {
      //LAB_800dca14
      return 2;
    }

    //LAB_800dca50
    return 1;
  }

  @Method(0x800dcc64L)
  public static void setViewpoint(final int x, final int y, final int z) {
    rview2_800c67f0.viewpoint_00.setX(x);
    rview2_800c67f0.viewpoint_00.setY(y);
    rview2_800c67f0.viewpoint_00.setZ(z);
  }

  @Method(0x800dcc7cL)
  public static void setRefpoint(final int x, final int y, final int z) {
    rview2_800c67f0.refpoint_0c.setX(x);
    rview2_800c67f0.refpoint_0c.setY(y);
    rview2_800c67f0.refpoint_0c.setZ(z);
  }

  @Method(0x800dcc94L)
  public static void FUN_800dcc94(final long a0, final long a1, final long a2, final Ref<Long> x, final Ref<Long> y, final Ref<Long> z) {
    _800fab98.set(x.get().shortValue(), y.get().shortValue(), (short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    _800faba0.set((short)0, (short)0, z.get().shortValue());
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    x.set(a0 - _800faba8.getZ());
    y.set(a1 - _800faba8.getX());
    z.set(_800faba8.getY() + a2);
  }

  @Method(0x800dcd9cL)
  public static void FUN_800dcd9c(final long a0, final long a1, final long a2, final Ref<Long> x, final Ref<Long> y, final Ref<Long> z) {
    final long dx = a0 - x.get();
    final long dy = a1 - y.get();
    final long dz = a2 - z.get();

    long s1 = (int)dx / 2;
    s1 = s1 * s1;

    final long halfDx = (int)dy / 2;
    final long halfDy = (int)dz / 2;

    x.set(ratan2(dz, dx) & 0xfffL);
    y.set(ratan2(dy, SquareRoot0(s1 + halfDy * halfDy) * 0x2L) & 0xfffL);
    z.set(SquareRoot0(s1 + halfDx * halfDx + (int)(halfDy * dz) / 2) * 0x2L);
  }

  @Method(0x800dcebcL)
  public static void FUN_800dcebc(long a0, long a1, long a2, long a3, final Ref<Long> a4, final Ref<Long> a5) {
    long v0;
    long lo;
    if(a0 == 0) {
      //LAB_800dcedc
      v0 = a2 << 1;
      lo = (int)v0 / (int)a3;
      v0 = lo;
      a4.set(a1);
      v0 = v0 - a1;
      a5.set(v0);
      return;
    }
    v0 = 0x1L;
    if(a0 != v0) {
      return;
    }

    //LAB_800dcef8
    v0 = a2 << 1;
    lo = (int)v0 / (int)a3;
    v0 = lo;
    a5.set(a1);
    v0 = v0 - a1;
    a4.set(v0);
  }

  @Method(0x800dd02cL)
  public static long FUN_800dd02c(long a0) { //TODO
    long v0;
    v0 = 0x800c_0000L;
    v0 = v0 - 0x3e40L;
    a0 = a0 << 2;
    a0 = a0 + v0;
    v0 = MEMORY.ref(4, a0).offset(0x0L).get();

    a0 = MEMORY.ref(4, v0).offset(0x0L).get();
    v0 = MEMORY.ref(4, a0).offset(0x0L).get();
    if(v0 != 0x2020_4d45L) {
      return a0 + 0x174L;
    }

    return a0 + 0x14L;
  }

  @Method(0x800dd0d4L)
  public static long FUN_800dd0d4() {
    return (long)_800fad90.deref(4).call(1, 0, rview2_800c67f0.viewpoint_00.getX(), rview2_800c67f0.viewpoint_00.getY(), rview2_800c67f0.viewpoint_00.getZ());
  }

  @Method(0x800dd118L)
  public static long FUN_800dd118() {
    return (long)_800fad90.deref(4).call(0, 0, rview2_800c67f0.viewpoint_00.getX(), rview2_800c67f0.viewpoint_00.getY(), rview2_800c67f0.viewpoint_00.getZ());
  }

  @Method(0x800de3f4L)
  public static void FUN_800de3f4(final long a0, final BttlScriptData6cInner a1, final MATRIX a2) {
    long s0 = _800c693c.deref(4).offset(0x20L).get() & 0x4L;
    s0 = (s0 >> 1) | (s0 >> 2);

    final MATRIX sp0x10 = new MATRIX();
    if((a1._00.get() & 0x8L) != 0) {
      FUN_8003eba0(a2, sp0x10);
      GsSetLightMatrix(sp0x10);
    } else {
      //LAB_800de458
      GsSetLightMatrix(a2);
    }

    //LAB_800de45c
    FUN_8003f210(Scus94491BpeSegment_800c.matrix_800c3548, a2, sp0x10);
    if((a1._00.get() & 0x400_0000L) == 0) {
      RotMatrix_8003faf0(a1.svec_10, sp0x10);
      ScaleVectorL_SVEC(sp0x10, a1.svec_16);
    }

    //LAB_800de4a8
    if((s0 & (sp0x10.transfer.getZ() ^ _800bb0fc.get())) == 0 || sp0x10.transfer.getZ() - sp0x10.transfer.getX() >= -0x800L && sp0x10.transfer.getZ() + sp0x10.transfer.getX() >= -0x800L && sp0x10.transfer.getZ() - sp0x10.transfer.getY() >= -0x800L && sp0x10.transfer.getZ() + sp0x10.transfer.getY() >= -0x800L) {
      //LAB_800de50c
      setRotTransMatrix(sp0x10);
      final long[] sp0x30 = {a1._00.get(), 0, a0};
      FUN_800e3e6c(sp0x30);
    }

    //LAB_800de528
  }

  @Method(0x800de544L)
  public static SVECTOR FUN_800de544(final SVECTOR a0, final MATRIX a1) {
    final MATRIX sp0x10 = new MATRIX().set(a1);
    a0.setX((short)ratan2(-sp0x10.get(5), sp0x10.get(8)));
    RotMatrixX(a0.getX(), sp0x10);
    a0.setY((short)ratan2(sp0x10.get(2), sp0x10.get(8)));
    RotMatrixY(-a0.getY(), sp0x10);
    a0.setZ((short)ratan2(sp0x10.get(3), sp0x10.get(0)));
    return a0;
  }

  @Method(0x800de72cL)
  public static void ScaleVectorL_SVEC(final MATRIX a0, final SVECTOR a1) {
    ScaleMatrixL(a0, new VECTOR().set(a1));
  }

  @Method(0x800de76cL)
  public static long FUN_800de76c(final long a0, final int objIndex) {
    if((MEMORY.ref(4, a0).offset(0x4L).get() & 0x2L) == 0) {
      final Memory.TemporaryReservation tmp = MEMORY.temp(0x10);
      final GsDOBJ2 dobj2 = tmp.get().cast(GsDOBJ2::new);
      updateTmdPacketIlen(MEMORY.ref(4, a0 + 0xcL, UnboundedArrayRef.of(0x1c, TmdObjTable::new)), dobj2, objIndex); //TODO
      final long tmd = dobj2.tmd_08.getPointer();
      tmp.release();
      return tmd;
    }

    //LAB_800de7a0
    //LAB_800de7b4
    return a0 + objIndex * 0x1cL + 0xcL;
  }

  @Method(0x800de840L)
  public static long FUN_800de840(final long a0, final long a1, final long a2) {
    final BttlStruct50 a3 = _800c6920.deref();

    MEMORY.ref(4, a0).offset(0x0L).setu(a3._10.get((int)a3._04.get()).getAddress());
    long t1 = a1;

    //LAB_800de878
    while(a3._0c.get() < a2 / 2) {
      if((a3._08.get() & 0x100L) == 0) {
        a3._08.set(MEMORY.ref(1, t1).offset(0x0L).get() | 0xff00L);
        t1 = t1 + 0x1L;
      }

      //LAB_800de89c
      if((a3._08.get() & 0x1L) != 0) {
        a3._10.get((int)a3._00.get()).set((int)(MEMORY.ref(1, t1).offset(0x1L).get() << 8 | MEMORY.ref(1, t1).offset(0x0L).get()));
        a3._00.incr().and(0x1fL);
        a3._0c.incr();
        t1 = t1 + 0x2L;
      } else {
        //LAB_800de8ec
        long a1_0 = MEMORY.ref(1, t1).offset(0x0L).get();

        //LAB_800de904
        int i;
        for(i = 0; i < (a1_0 >>> 5) + 0x1L; i++) {
          a1_0 = a1_0 & 0x1fL;
          a3._10.get((int)a3._00.get()).set(a3._10.get((int)a1_0).get());
          a3._00.incr().and(0x1fL);
          a1_0 = a1_0 + 0x1L;
        }

        //LAB_800de940
        a3._0c.add(i);
        t1 = t1 + 0x1L;
      }

      //LAB_800de94c
      a3._08.div(2);
    }

    //LAB_800de968
    //LAB_800de970
    for(int i = 0; i < a2 / 2; i++) {
      MEMORY.ref(2, a0).offset(i * 0x2L).setu(a3._10.get((int)a3._04.get()).get());
      a3._04.add(0x1L).and(0x1fL);
      a3._0c.sub(0x1L);
    }

    //LAB_800de9b4
    return t1;
  }

  @Method(0x800de9bcL)
  public static long FUN_800de9bc(long a0, long a1, long a2, long a3) {
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
    long t8;
    long t9;
    long s8;
    t9 = a1;
    s8 = a2;
    v1 = 0xe100_0000L;
    v1 = v1 | 0x200L;
    v0 = 0x1f80_0000L;
    a2 = 0x1f80_0000L;
    t1 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = MEMORY.ref(2, a2).offset(0x3ecL).getSigned();
    a2 = a2 + 0x3ecL;
    t8 = MEMORY.ref(1, a2).offset(0x2L).get();
    s2 = v0 | v1;
    v1 = 0x8006_0000L;
    v0 = 0x800c_0000L;
    a1 = MEMORY.ref(4, v0).offset(-0x4ef8L).get();
    v1 = v1 - 0x5c90L;
    v0 = a1 << 2;
    v0 = v0 + a1;
    v0 = v0 << 2;
    v0 = v0 + v1;
    s1 = MEMORY.ref(4, v0).offset(0x4L).get();
    if(a3 == 0) {
      t3 = a0;
    } else {
      t3 = a0;
      s0 = t8 & 0x2L;
      t4 = 0xff_0000L;
      t4 = t4 | 0xffffL;
      a2 = a0 + 0x14L;
      a1 = t1 + 0x20L;

      //LAB_800dea38
      do {
        t5 = MEMORY.ref(2, t3).offset(0xaL).get();
        t6 = MEMORY.ref(2, t3).offset(0xeL).get();
        t7 = MEMORY.ref(2, t3).offset(0x12L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = t9 + t5;
        t6 = t9 + t6;
        t7 = t9 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t2 = CPU.CFC2(31);

        if((int)t2 >= 0) {
          CPU.COP2(0x1400006L);
          t2 = CPU.MFC2(24);

          if((int)t2 > 0 || s0 != 0 && t2 != 0) {
            //LAB_800deab8
            MEMORY.ref(4, t1).offset(0xcL).setu(CPU.MFC2(12));
            MEMORY.ref(4, t1).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t1).offset(0x1cL).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, a2).offset(0x2L).get();
            v0 = v0 << 3;
            v0 = t9 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x180001L);
            t2 = CPU.CFC2(31);
            v0 = t1 + 0x24L;
            MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));
            CPU.COP2(0x168002eL);
            t0 = CPU.MFC2(7);
            v0 = 0x1f80_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x3e8L).get();
            t0 = t0 + v0;
            t0 = (int)t0 >> 2;
            if((int)t0 >= 0xbL) {
              if((int)t0 >= 0xffeL) {
                t0 = 0xffeL;
              }
              a0 = t0 << 2;

              //LAB_800deb34
              a0 = s1 + a0;
              v0 = t3 + 0x4L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              t5 = MEMORY.ref(2, t3).offset(0x8L).get();
              t6 = MEMORY.ref(2, t3).offset(0xcL).get();
              t7 = MEMORY.ref(2, t3).offset(0x10L).get();
              t5 = t5 << 3;
              t6 = t6 << 3;
              t7 = t7 << 3;
              t5 = s8 + t5;
              t6 = s8 + t6;
              t7 = s8 + t7;
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
              CPU.COP2(0x118043fL);
              MEMORY.ref(4, t1).offset(0x8L).setu(CPU.MFC2(20));
              MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(21));
              MEMORY.ref(4, t1).offset(0x18L).setu(CPU.MFC2(22));
              MEMORY.ref(1, a1).offset(-0x15L).setu(t8);
              MEMORY.ref(4, a1).offset(-0x1cL).setu(s2);
              v0 = MEMORY.ref(4, a0).offset(0x0L).get();
              v1 = 0x900_0000L;
              v0 = v0 & t4;
              v0 = v0 | v1;
              MEMORY.ref(4, t1).offset(0x0L).setu(v0);
              v0 = MEMORY.ref(2, a2).offset(0x0L).get();
              v0 = v0 << 3;
              v0 = s8 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);
              v0 = t1 & t4;
              MEMORY.ref(4, a0).offset(0x0L).setu(v0);
              MEMORY.ref(4, a1).offset(0x0L).setu(CPU.MFC2(22));
              a1 = a1 + 0x28L;
              t1 = t1 + 0x28L;
            }
          }
        }

        //LAB_800debe8
        t3 = t3 + 0x18L;
        a2 = a2 + 0x18L;
      } while(a3 != 0);
    }

    //LAB_800debf4
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t1);
    return t3;
  }
}
