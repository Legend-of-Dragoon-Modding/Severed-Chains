package legend.game;

import legend.core.InterruptType;
import legend.core.MathHelper;
import legend.core.cdrom.CdlFILE;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.kernel.PriorityChainEntry;
import legend.core.memory.Method;
import legend.core.memory.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.core.kernel.Bios.EnterCriticalSection;
import static legend.core.kernel.Bios.ExitCriticalSection;
import static legend.core.kernel.Kernel.EvMdINTR;
import static legend.core.kernel.Kernel.EvSpERROR;
import static legend.core.kernel.Kernel.EvSpIOE;
import static legend.core.kernel.Kernel.EvSpNEW;
import static legend.core.kernel.Kernel.EvSpTIMOUT;
import static legend.core.kernel.Kernel.HwCARD;
import static legend.core.kernel.Kernel.SwCARD;
import static legend.game.SInit.FUN_800fbec8;
import static legend.game.Scus94491BpeSegment.FUN_80015310;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001e29c;
import static legend.game.Scus94491BpeSegment.fillRects;
import static legend.game.Scus94491BpeSegment.functionVectorA_000000a0;
import static legend.game.Scus94491BpeSegment.functionVectorB_000000b0;
import static legend.game.Scus94491BpeSegment.functionVectorC_000000c0;
import static legend.game.Scus94491BpeSegment.rectArray28_80010770;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment_8003.CdMix;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.DsSearchFile;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c400;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003e5d0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f730;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f760;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.SetVsyncInterruptCallback;
import static legend.game.Scus94491BpeSegment_8003.VSync;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040010;
import static legend.game.Scus94491BpeSegment_8004.FUN_800402a0;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040440;
import static legend.game.Scus94491BpeSegment_8004.FUN_800405e0;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040b90;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042b60;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042ba0;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042d10;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042e70;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042f40;
import static legend.game.Scus94491BpeSegment_8004.FUN_80043040;
import static legend.game.Scus94491BpeSegment_8004.FUN_80043230;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c390;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d034;
import static legend.game.Scus94491BpeSegment_8004._8004dd08;
import static legend.game.Scus94491BpeSegment_8004._8004dd20;
import static legend.game.Scus94491BpeSegment_8004.callbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
import static legend.game.Scus94491BpeSegment_8005._80052ae0;
import static legend.game.Scus94491BpeSegment_8005._80052c4c;
import static legend.game.Scus94491BpeSegment_8005._80052c5c;
import static legend.game.Scus94491BpeSegment_8005._80052c64;
import static legend.game.Scus94491BpeSegment_8005._80052dbc;
import static legend.game.Scus94491BpeSegment_8005._80052dc0;
import static legend.game.Scus94491BpeSegment_8005._80052e1c;
import static legend.game.Scus94491BpeSegment_8005._8005a1d8;
import static legend.game.Scus94491BpeSegment_8005.memcardEventIndex_80052e4c;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpERROR_EventId_800bf264;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpIOE_EventId_800bf260;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpNEW_EventId_800bf26c;
import static legend.game.Scus94491BpeSegment_800b.HwCARD_EvSpTIMOUT_EventId_800bf268;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpERROR_EventId_800bf254;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpIOE_EventId_800bf250;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpNEW_EventId_800bf25c;
import static legend.game.Scus94491BpeSegment_800b.SwCARD_EvSpTIMOUT_EventId_800bf258;
import static legend.game.Scus94491BpeSegment_800b._800bd610;
import static legend.game.Scus94491BpeSegment_800b._800bd614;
import static legend.game.Scus94491BpeSegment_800b._800bd61c;
import static legend.game.Scus94491BpeSegment_800b._800bd7ac;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b._800bd7b4;
import static legend.game.Scus94491BpeSegment_800b._800bd7b8;
import static legend.game.Scus94491BpeSegment_800b._800bd80c;
import static legend.game.Scus94491BpeSegment_800b._800bdb88;
import static legend.game.Scus94491BpeSegment_800b._800bdea0;
import static legend.game.Scus94491BpeSegment_800b._800bdf00;
import static legend.game.Scus94491BpeSegment_800b._800bdf04;
import static legend.game.Scus94491BpeSegment_800b._800bdf08;
import static legend.game.Scus94491BpeSegment_800b._800bdf18;
import static legend.game.Scus94491BpeSegment_800b._800bdf38;
import static legend.game.Scus94491BpeSegment_800b._800be358;
import static legend.game.Scus94491BpeSegment_800b._800be5b8;
import static legend.game.Scus94491BpeSegment_800b._800be5bc;
import static legend.game.Scus94491BpeSegment_800b._800be5c0;
import static legend.game.Scus94491BpeSegment_800b._800be5c4;
import static legend.game.Scus94491BpeSegment_800b._800be5c8;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b._800be5d8;
import static legend.game.Scus94491BpeSegment_800b._800be5d9;
import static legend.game.Scus94491BpeSegment_800b._800be5da;
import static legend.game.Scus94491BpeSegment_800b._800be5db;
import static legend.game.Scus94491BpeSegment_800b._800be5dc;
import static legend.game.Scus94491BpeSegment_800b._800be5dd;
import static legend.game.Scus94491BpeSegment_800b._800be5de;
import static legend.game.Scus94491BpeSegment_800b._800be5df;
import static legend.game.Scus94491BpeSegment_800b._800be5e0;
import static legend.game.Scus94491BpeSegment_800b._800be5e1;
import static legend.game.Scus94491BpeSegment_800b._800be5e2;
import static legend.game.Scus94491BpeSegment_800b._800be5e3;
import static legend.game.Scus94491BpeSegment_800b._800be5e4;
import static legend.game.Scus94491BpeSegment_800b._800be5e5;
import static legend.game.Scus94491BpeSegment_800b._800be5e6;
import static legend.game.Scus94491BpeSegment_800b._800be5e7;
import static legend.game.Scus94491BpeSegment_800b._800be5e8;
import static legend.game.Scus94491BpeSegment_800b._800be5e9;
import static legend.game.Scus94491BpeSegment_800b._800be5ea;
import static legend.game.Scus94491BpeSegment_800b._800be5eb;
import static legend.game.Scus94491BpeSegment_800b._800be5ec;
import static legend.game.Scus94491BpeSegment_800b._800be5ed;
import static legend.game.Scus94491BpeSegment_800b._800be5ee;
import static legend.game.Scus94491BpeSegment_800b._800be5ef;
import static legend.game.Scus94491BpeSegment_800b._800be5f0;
import static legend.game.Scus94491BpeSegment_800b._800be5f1;
import static legend.game.Scus94491BpeSegment_800b._800be5f2;
import static legend.game.Scus94491BpeSegment_800b._800be5f3;
import static legend.game.Scus94491BpeSegment_800b._800be5f8;
import static legend.game.Scus94491BpeSegment_800b._800be5fc;
import static legend.game.Scus94491BpeSegment_800b._800be5fe;
import static legend.game.Scus94491BpeSegment_800b._800be600;
import static legend.game.Scus94491BpeSegment_800b._800be602;
import static legend.game.Scus94491BpeSegment_800b._800be604;
import static legend.game.Scus94491BpeSegment_800b._800be606;
import static legend.game.Scus94491BpeSegment_800b._800be607;
import static legend.game.Scus94491BpeSegment_800b._800be628;
import static legend.game.Scus94491BpeSegment_800b._800be62d;
import static legend.game.Scus94491BpeSegment_800b._800be62e;
import static legend.game.Scus94491BpeSegment_800b._800be636;
import static legend.game.Scus94491BpeSegment_800b._800be63e;
import static legend.game.Scus94491BpeSegment_800b._800be640;
import static legend.game.Scus94491BpeSegment_800b._800be642;
import static legend.game.Scus94491BpeSegment_800b._800be644;
import static legend.game.Scus94491BpeSegment_800b._800be646;
import static legend.game.Scus94491BpeSegment_800b._800be648;
import static legend.game.Scus94491BpeSegment_800b._800be64a;
import static legend.game.Scus94491BpeSegment_800b._800be64c;
import static legend.game.Scus94491BpeSegment_800b._800be64e;
import static legend.game.Scus94491BpeSegment_800b._800be650;
import static legend.game.Scus94491BpeSegment_800b._800be652;
import static legend.game.Scus94491BpeSegment_800b._800be654;
import static legend.game.Scus94491BpeSegment_800b._800be656;
import static legend.game.Scus94491BpeSegment_800b._800be658;
import static legend.game.Scus94491BpeSegment_800b._800be65a;
import static legend.game.Scus94491BpeSegment_800b._800be65c;
import static legend.game.Scus94491BpeSegment_800b._800be65e;
import static legend.game.Scus94491BpeSegment_800b._800be660;
import static legend.game.Scus94491BpeSegment_800b._800be661;
import static legend.game.Scus94491BpeSegment_800b._800be662;
import static legend.game.Scus94491BpeSegment_800b._800be663;
import static legend.game.Scus94491BpeSegment_800b._800be664;
import static legend.game.Scus94491BpeSegment_800b._800be665;
import static legend.game.Scus94491BpeSegment_800b._800be666;
import static legend.game.Scus94491BpeSegment_800b._800be667;
import static legend.game.Scus94491BpeSegment_800b._800be668;
import static legend.game.Scus94491BpeSegment_800b._800be669;
import static legend.game.Scus94491BpeSegment_800b._800be66a;
import static legend.game.Scus94491BpeSegment_800b._800be66b;
import static legend.game.Scus94491BpeSegment_800b._800be66c;
import static legend.game.Scus94491BpeSegment_800b._800be66e;
import static legend.game.Scus94491BpeSegment_800b._800be66f;
import static legend.game.Scus94491BpeSegment_800b._800be670;
import static legend.game.Scus94491BpeSegment_800b._800be671;
import static legend.game.Scus94491BpeSegment_800b._800be672;
import static legend.game.Scus94491BpeSegment_800b._800be673;
import static legend.game.Scus94491BpeSegment_800b._800be674;
import static legend.game.Scus94491BpeSegment_800b._800be675;
import static legend.game.Scus94491BpeSegment_800b._800be676;
import static legend.game.Scus94491BpeSegment_800b._800be677;
import static legend.game.Scus94491BpeSegment_800b._800be678;
import static legend.game.Scus94491BpeSegment_800b._800be679;
import static legend.game.Scus94491BpeSegment_800b._800be67a;
import static legend.game.Scus94491BpeSegment_800b._800be67b;
import static legend.game.Scus94491BpeSegment_800b._800be67c;
import static legend.game.Scus94491BpeSegment_800b._800be67e;
import static legend.game.Scus94491BpeSegment_800b._800be680;
import static legend.game.Scus94491BpeSegment_800b._800be682;
import static legend.game.Scus94491BpeSegment_800b._800be684;
import static legend.game.Scus94491BpeSegment_800b._800be686;
import static legend.game.Scus94491BpeSegment_800b._800be688;
import static legend.game.Scus94491BpeSegment_800b._800be68a;
import static legend.game.Scus94491BpeSegment_800b._800be68c;
import static legend.game.Scus94491BpeSegment_800b._800be68e;
import static legend.game.Scus94491BpeSegment_800b._800be690;
import static legend.game.Scus94491BpeSegment_800b._800be691;
import static legend.game.Scus94491BpeSegment_800b._800be692;
import static legend.game.Scus94491BpeSegment_800b._800be693;
import static legend.game.Scus94491BpeSegment_800b._800be694;
import static legend.game.Scus94491BpeSegment_800b._800be696;
import static legend.game.Scus94491BpeSegment_800b._800be697;
import static legend.game.Scus94491BpeSegment_800b._800bed60;
import static legend.game.Scus94491BpeSegment_800b._800bed62;
import static legend.game.Scus94491BpeSegment_800b._800bed64;
import static legend.game.Scus94491BpeSegment_800b._800bed66;
import static legend.game.Scus94491BpeSegment_800b._800bed6c;
import static legend.game.Scus94491BpeSegment_800b._800bed6e;
import static legend.game.Scus94491BpeSegment_800b._800bed72;
import static legend.game.Scus94491BpeSegment_800b._800bed74;
import static legend.game.Scus94491BpeSegment_800b._800bed76;
import static legend.game.Scus94491BpeSegment_800b._800bed94;
import static legend.game.Scus94491BpeSegment_800b._800bed9c;
import static legend.game.Scus94491BpeSegment_800b._800beda4;
import static legend.game.Scus94491BpeSegment_800b._800beda8;
import static legend.game.Scus94491BpeSegment_800b._800bedb2;
import static legend.game.Scus94491BpeSegment_800b._800bedb6;
import static legend.game.Scus94491BpeSegment_800b._800bedb8;
import static legend.game.Scus94491BpeSegment_800b._800bedb9;
import static legend.game.Scus94491BpeSegment_800b._800bedba;
import static legend.game.Scus94491BpeSegment_800b._800bedbb;
import static legend.game.Scus94491BpeSegment_800b._800bedbc;
import static legend.game.Scus94491BpeSegment_800b._800bedbe;
import static legend.game.Scus94491BpeSegment_800b._800bede0;
import static legend.game.Scus94491BpeSegment_800b._800bede1;
import static legend.game.Scus94491BpeSegment_800b._800bede2;
import static legend.game.Scus94491BpeSegment_800b._800bede3;
import static legend.game.Scus94491BpeSegment_800b._800bede4;
import static legend.game.Scus94491BpeSegment_800b._800bede8;
import static legend.game.Scus94491BpeSegment_800b._800bedec;
import static legend.game.Scus94491BpeSegment_800b._800bee48;
import static legend.game.Scus94491BpeSegment_800b._800bee4e;
import static legend.game.Scus94491BpeSegment_800b._800bee80;
import static legend.game.Scus94491BpeSegment_800b._800bee88;
import static legend.game.Scus94491BpeSegment_800b._800bee8c;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b._800bee94;
import static legend.game.Scus94491BpeSegment_800b._800bee98;
import static legend.game.Scus94491BpeSegment_800b._800bee9c;
import static legend.game.Scus94491BpeSegment_800b._800beea4;
import static legend.game.Scus94491BpeSegment_800b._800beeac;
import static legend.game.Scus94491BpeSegment_800b._800beeb4;
import static legend.game.Scus94491BpeSegment_800b._800beebc;
import static legend.game.Scus94491BpeSegment_800b._800beec4;
import static legend.game.Scus94491BpeSegment_800b._800bef44;
import static legend.game.Scus94491BpeSegment_800b._800befc4;
import static legend.game.Scus94491BpeSegment_800b._800bf044;
import static legend.game.Scus94491BpeSegment_800b._800bf064;
import static legend.game.Scus94491BpeSegment_800b._800bf068;
import static legend.game.Scus94491BpeSegment_800b._800bf06a;
import static legend.game.Scus94491BpeSegment_800b._800bf06c;
import static legend.game.Scus94491BpeSegment_800b._800bf06e;
import static legend.game.Scus94491BpeSegment_800b._800bf074;
import static legend.game.Scus94491BpeSegment_800b._800bf076;
import static legend.game.Scus94491BpeSegment_800b._800bf07a;
import static legend.game.Scus94491BpeSegment_800b._800bf07c;
import static legend.game.Scus94491BpeSegment_800b._800bf07e;
import static legend.game.Scus94491BpeSegment_800b._800bf0a8;
import static legend.game.Scus94491BpeSegment_800b._800bf0ac;
import static legend.game.Scus94491BpeSegment_800b._800bf0c0;
import static legend.game.Scus94491BpeSegment_800b._800bf0c4;
import static legend.game.Scus94491BpeSegment_800b._800bf0c8;
import static legend.game.Scus94491BpeSegment_800b._800bf0cc;
import static legend.game.Scus94491BpeSegment_800b._800bf0cd;
import static legend.game.Scus94491BpeSegment_800b._800bf0ce;
import static legend.game.Scus94491BpeSegment_800b._800bf0d8;
import static legend.game.Scus94491BpeSegment_800b._800bf160;
import static legend.game.Scus94491BpeSegment_800b._800bf164;
import static legend.game.Scus94491BpeSegment_800b._800bf170;
import static legend.game.Scus94491BpeSegment_800b._800bf174;
import static legend.game.Scus94491BpeSegment_800b._800bf178;
import static legend.game.Scus94491BpeSegment_800b._800bf17c;
import static legend.game.Scus94491BpeSegment_800b._800bf184;
import static legend.game.Scus94491BpeSegment_800b._800bf1b4;
import static legend.game.Scus94491BpeSegment_800b._800bf1b8;
import static legend.game.Scus94491BpeSegment_800b._800bf1bc;
import static legend.game.Scus94491BpeSegment_800b._800bf1c0;
import static legend.game.Scus94491BpeSegment_800b._800bf1c4;
import static legend.game.Scus94491BpeSegment_800b._800bf200;
import static legend.game.Scus94491BpeSegment_800b._800bf23c;
import static legend.game.Scus94491BpeSegment_800b._800bf270;
import static legend.game.Scus94491BpeSegment_800b._800bf274;
import static legend.game.Scus94491BpeSegment_800b._800bf278;
import static legend.game.Scus94491BpeSegment_800b._800bf27c;
import static legend.game.Scus94491BpeSegment_800b._800bf280;
import static legend.game.Scus94491BpeSegment_800b._800bf284;
import static legend.game.Scus94491BpeSegment_800b._800bf288;
import static legend.game.Scus94491BpeSegment_800b._800bf28c;
import static legend.game.Scus94491BpeSegment_800b.mono_800bb0a8;
import static legend.game.Scus94491BpeSegment_800b.vibrationEnabled_800bb0a9;
import static legend.game.Scus94491BpeSegment_800c._800c6688;
import static legend.game.Scus94491BpeSegment_800e.FUN_800e4018;
import static legend.game.Scus94491BpeSegment_800e.FUN_800e4708;
import static legend.game.Scus94491BpeSegment_800e.main;
import static legend.game.Scus94491BpeSegment_800e.ramSize_800e6f04;
import static legend.game.Scus94491BpeSegment_800e.stackSize_800e6f08;

public final class Scus94491BpeSegment_8002 {
  private Scus94491BpeSegment_8002() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8002.class);

  private static final Object[] EMPTY_OBJ_ARRAY = {};

  @Method(0x80020008L)
  public static void FUN_80020008() {
    FUN_8001ad18();
    FUN_8001e29c(0x1L);
    FUN_8001e29c(0x3L);
    FUN_8001e29c(0x4L);
    FUN_8001e29c(0x5L);
    FUN_8001e29c(0x6L);
    FUN_8001e29c(0x7L);
    FUN_800201c8(0x6L);
  }

  @Method(0x800201c8L)
  public static void FUN_800201c8(final long a0) {
    if(_800bd610.offset(a0 * 16).get() != 0) {
      FUN_8004d034(_800bd61c.offset(a0 * 16).get(), 0x1L);
      FUN_8004c390(_800bd61c.offset(a0 * 16).get());
      removeFromLinkedList(_800bd614.offset(a0 * 16).get());
      _800bd610.offset(a0 * 16).setu(0);
    }

    //LAB_80020220
  }

  @Method(0x80020460L)
  public static void FUN_80020460() {
    // empty
  }

  @Method(0x800212d8L)
  public static void FUN_800212d8(final long a0) {
    long s6 = MEMORY.ref(2, a0).offset(0xcaL).get();

    if(s6 == 0) {
      return;
    }

    long s5 = MEMORY.ref(4, a0).offset(0x94L).get();
    long s7 = MEMORY.ref(4, a0).get();

    //LAB_80021320
    for(; s6 > 0; s6--) {
      final long v1 = MEMORY.ref(4, s5).get();
      final long s1 = MEMORY.ref(4, s5).offset(0x4L).get();
      final long s3 = MEMORY.ref(4, s5).offset(0x8L).get();
      final long v0 = MEMORY.ref(4, s7).offset(0x4L).get();
      final long s0 = MEMORY.ref(4, v0).offset(0x44L).get();
      final MATRIX matrix = new MATRIX(MEMORY.ref(32, v0 + 0x4L));

      MEMORY.ref(2, s0).offset(0x10L).setu(v1);
      MEMORY.ref(2, s0).offset(0x12L).setu(v1 >>> 0x10L);
      MEMORY.ref(2, s0).offset(0x14L).setu(s1);

      FUN_80040010(new SVECTOR(MEMORY.ref(8, s0 + 0x10L)), matrix);

      MEMORY.ref(4, s0).offset(0x18L).setu(s1 >> 0x10L);
      MEMORY.ref(4, s0).offset(0x1cL).setu((short)(v0 & 0xffffL));
      MEMORY.ref(4, s0).offset(0x20L).setu(s3 >> 0x10L);

      FUN_8003f730(matrix, new VECTOR(MEMORY.ref(12, s0 + 0x18L)));

      s5 += 0xcL;
      s7 += 0x10L;
    }

    //LAB_80021390
    MEMORY.ref(4, a0).offset(0x94L).setu(s5);
  }

  @Method(0x80021584L)
  public static void FUN_80021584(final long a0, long a1) {
    MEMORY.ref(4, a0).offset(0x90L).setu(a1 + 0xcL);
    MEMORY.ref(2, a0).offset(0x98L).setu(MEMORY.ref(2, a1).offset(0xcL));
    MEMORY.ref(1, a0).offset(0x9cL).setu(0);
    MEMORY.ref(4, a0).offset(0x94L).setu(a1 + 0x10L);
    MEMORY.ref(4, a0).offset(0x90L).addu(0x4L);
    MEMORY.ref(2, a0).offset(0x9aL).setu(MEMORY.ref(2, a1).offset(0xeL));

    FUN_800212d8(a0);

    if(MEMORY.ref(1, a0).offset(0xa2L).get() == 0) {
      MEMORY.ref(2, a0).offset(0x9eL).setu(MEMORY.ref(2, a0).offset(0x9aL));
    } else {
      //LAB_800215e8
      MEMORY.ref(2, a0).offset(0x9eL).setu((MEMORY.ref(2, a0).offset(0x9aL).getSigned() + MEMORY.ref(2, a0).offset(0x9aL).get() >>> 0x1fL) / 2L);
    }

    //LAB_80021608
    MEMORY.ref(1, a0).offset(0x9cL).setu(0x1L);
    MEMORY.ref(4, a0).offset(0x94L).setu(MEMORY.ref(4, a0).offset(0x90L));
  }

  @Method(0x80021918L)
  public static void FUN_80021918(final long a0, final long a1, final long a2, final long a3, final long a4) {
    long v0 = (int)(a4 << 0x10L);
    final long s2 = v0 >> 0x18L;

    final long s5 = a4 & 0xffL;
    long s1 = FUN_80021b64(a0, s5);
    final long s0;
    final MATRIX s3;
    final VECTOR s000vec;
    final SVECTOR s010svec;
    final VECTOR s018vec;

    if(s1 == 0) {
      s1 = a4;
      s3 = new MATRIX();
      s000vec = new VECTOR();
      s010svec = new SVECTOR();
      s018vec = new VECTOR();
    } else {
      //LAB_80021984
      MEMORY.ref(4, s1).offset(0x4L).deref(4).setu(0);

      if(MEMORY.ref(4, s1).offset(0x4L).deref(4).offset(0x48L).get() == 0) {
        MEMORY.ref(4, s1).offset(0x4L).deref(4).offset(0x48L).setu(a2);
      }

      s0 = MEMORY.ref(4, s1).offset(0x4L).deref(4).offset(0x44L).get();
      s3 = new MATRIX(MEMORY.ref(4, s1).offset(0x4L).deref(4).offset(0x4L));
      s000vec = new VECTOR(MEMORY.ref(16, s0));
      s010svec = new SVECTOR(MEMORY.ref(8, s0).offset(0x10L));
      s018vec = new VECTOR(MEMORY.ref(16, s0 + 0x18L));
    }

    //LAB_800219ac
    final int s4 = (int)s2;
    if(s4 == 0x2L) {
      //LAB_80021a98
      if(a1 != 0) {
        FUN_8003e5d0(FUN_80021c58(a1, s5), s1, 0);
      }

      return;
    }

    if(s4 < 0x3L) {
      if(s4 == 0x1L) {
        //LAB_800219ec
        s010svec.x.set((short)11);
        s010svec.y.set((short)11);
        s010svec.z.set((short)11);

        final long v1 = MEMORY.ref(4, s1).offset(0x4L).get();
        MEMORY.ref(2, v1).offset(0x04L).setu(0x1000L);
        MEMORY.ref(2, v1).offset(0x0cL).setu(0x1000L);
        MEMORY.ref(2, v1).offset(0x14L).setu(0x1000L);

        v0 = MEMORY.ref(4, s1).offset(0x4L).get();
        MEMORY.ref(2, v0).offset(0x06L).setu(0);
        MEMORY.ref(2, v0).offset(0x08L).setu(0);
        MEMORY.ref(2, v0).offset(0x0aL).setu(0);
        MEMORY.ref(2, v0).offset(0x0eL).setu(0);
        MEMORY.ref(2, v0).offset(0x10L).setu(0);
        MEMORY.ref(2, v0).offset(0x12L).setu(0);

        FUN_800402a0(s010svec.x.get(), s3);
        FUN_80040440(s010svec.y.get(), s3);
        FUN_800405e0(s010svec.z.get(), s3);

        s000vec.x.set(0x1000);
        s000vec.y.set(0x1000);
        s000vec.z.set(0x1000);

        FUN_8003faf0(s010svec, s3);
        FUN_8003f760(s3, s000vec);

        s018vec.x.set(s4);
        s018vec.y.set(s4);
        s018vec.z.set(s4);

        FUN_8003f730(s3, s018vec);
        return;
      }

      return;
    }

    //LAB_800219d8
    //LAB_80021ac0
    if(s4 != 0x8L || a0 == 0) {
      return;
    }

    FUN_80021bac(a0, s5, (int)(a3 & 0xffffffffL));

    //LAB_80021ad8
  }

  @Method(0x80021b08L)
  public static void FUN_80021b08(final long a0, final long a1, long a2, long a3, final long a4) {
    MEMORY.ref(4, a0).offset(0x0L).setu(a1);
    MEMORY.ref(4, a0).offset(0x4L).setu(0);

    long t0 = a1;

    //LAB_80021b2c
    for(int i = 0; i < a4; i++) {
      MEMORY.ref(4, t0).offset(0x0L).setu(0x80000000L);
      MEMORY.ref(4, t0).offset(0x4L).setu(a2);
      MEMORY.ref(4, t0).offset(0x8L).setu(0);
      MEMORY.ref(4, t0).offset(0xcL).setu(0xffffffffL);
      MEMORY.ref(4, a2).offset(0x44L).setu(a3);
      t0 += 0x10L;
      a2 += 0x50L;
      a3 += 0x28L;
    }

    //LAB_80021b5c
  }

  @Method(0x80021b64L)
  public static long FUN_80021b64(final long a0, final long a1) {
    final long a2 = MEMORY.ref(4, a0).offset(0x4L).get();
    long a4 = MEMORY.ref(4, a0).offset(0x0L).get();

    if(a2 > 0) {
      long v1 = 0;
      final long v0 = a1 << 0x10L & 0xffffffffL;
      final long a3 = v0 >> 0x10L;

      //LAB_80021b80
      do {
        if(a3 == MEMORY.ref(4, a4).offset(0xcL).get()) {
          return a4;
        }

        //LAB_80021b98
        a4 += 0x10L;
        v1++;
      } while(v1 < a2);
    }

    //LAB_80021ba4
    return 0;
  }

  @Method(0x80021bacL)
  public static void FUN_80021bac(long a0, final long a1, final long a2) {
    final long a3 = a0;
    long v1 = 0;
    a0 = MEMORY.ref(4, a3).offset(0x4L).get();
    long s0 = MEMORY.ref(4, a3).offset(0x0L).get();

    //LAB_80021bd4
    long v0;
    while(v1 < a0) {
      v0 = MEMORY.ref(4, s0).offset(0xcL).get();
      if(v0 == -0x1L) {
        break;
      }

      v1++;
      s0 += 0x10L;
    }

    //LAB_80021bf4
    if(a0 <= 0 || v1 >= MEMORY.ref(4, a3).offset(0x4L).get()) {
      //LAB_80021c08
      if(v1 >= a2) {
        return;
      }

      v0 = MEMORY.ref(4, a3).offset(0x4L).get();
      s0 = MEMORY.ref(4, a3).offset(0x0L).get() + v0 * 16;
      v0++;
      MEMORY.ref(4, a3).offset(0x4L).setu(v0);
    }

    //LAB_80021c2c
    MEMORY.ref(4, s0).offset(0xcL).setu((short)a1);
    MEMORY.ref(4, s0).offset(0x0L).setu(0);
    FUN_8003c400(0, MEMORY.ref(4, s0).offset(0x4L).get());
    MEMORY.ref(4, s0).offset(0x8L).setu(0);

    //LAB_80021c48
  }

  @Method(0x80021c58L)
  public static long FUN_80021c58(long a0, final long a1) {
    a0 += 0x4L;
    long v1 = MEMORY.ref(4, a0).get();
    a0 += 0x4L;
    long a2 = 0x1L;
    //LAB_80021c74
    while(v1 > 0) {
      if((int)(a1 & 0xffffffffL) == a2) {
        break;
      }

      a0 += 0x1cL;
      v1--;
      a2++;
    }

    //LAB_80021c8c
    if(v1 != 0) {
      return a0;
    }

    //LAB_80021c98
    return 0;
  }

  @Method(0x80021ca0L)
  public static void FUN_80021ca0(final long a0, final long a1, final long a2, final long a3, final long a4) {
    long v1 = (int)(a4 & 0xffffffffL);
    long s3 = (int)(a3 & 0xffffffffL);
    long s1 = 0x801_0000L;

    //LAB_80021d08
    for(int s0 = 1; s0 < v1; s0++) {
      FUN_80021918(a0, a1, a2, s3, s1 / 0x1_0000L);
      s1 += 0x1_0000L;
    }

    //LAB_80021d3c
    v1 = (int)(a4 & 0xffffffffL);
    s3 = (int)(a3 & 0xffffffffL);
    long s2 = 0x101_0000L;
    s1 = 0x201_0000L;

    //LAB_80021d64
    for(int s0 = 1; s0 < v1; s0++) {
      FUN_80021918(a0, a1, a2, s3, s1 / 0x1_0000L);
      FUN_80021918(a0, a1, a2, s3, s2 / 0x1_0000L);
      s2 += 0x1_0000L;
      s1 += 0x1_0000L;
    }

    //LAB_80021db4
  }

  @Method(0x80021facL)
  public static void setScreenOffset(final int x, final int y) {
    // cop2r56, OFX - Screen offset X
    CPU.CTC2(x << 0x10, 0x18);
    // cop2r57, OFY - Screen offset Y
    CPU.CTC2(y << 0x10, 0x19);
  }

  @Method(0x8002246cL)
  public static void FUN_8002246c(long a0, long a1) {
    assert false;
    //TODO
  }

  @Method(0x80022518L)
  public static void FUN_80022518() {
    // Empty
  }

  @Method(0x8002a058L)
  public static void FUN_8002a058() {
    //LAB_8002a080
    for(int i = 0; i < 8; i++) {
      if(_800be358.offset(i * 0x4cL).get() != 0) {
        FUN_80025a04(i);
      }

      //LAB_8002a098
      if(_800bdf38.offset(i * 0x84L).get() != 0) {
        FUN_800264b0(i);
      }
    }

    FUN_80024994();
  }

  @Method(0x80020ed8L)
  public static void FUN_80020ed8() {
    if(_800bdb88.get() == 0x5L) {
      if(_8004dd08.get() == 0) {
        if(_800bd7b4.get() == 0x1L) {
          FUN_800e4708();
        }

        //LAB_80020f20
        FUN_8002aae8();
        FUN_800e4018();
      }
    }

    //LAB_80020f30
    //LAB_80020f34
    final long a0 = _800bdb88.get();
    _800bd7b4.setu(0);
    if(a0 != _8004dd20.get()) {
      _800bd80c.setu(a0);
      _800bdb88.setu(_8004dd20);

      if(_8004dd20.get() == 0x5L) {
        _800bd7b0.setu(0x2L);
        _800bd7b8.setu(0);

        if(a0 == 0x2L) {
          _800bd7b0.setu(0x9L);
        }

        //LAB_80020f84
        if(a0 == 0x6L) {
          _800bd7b0.setu(-0x4L);
          _800bd7b8.setu(0x1L);
        }

        //LAB_80020fa4
        if(a0 == 0x8L) {
          _800bd7b0.setu(0x3L);
        }
      }
    }

    //LAB_80020fb4
    //LAB_80020fb8
    if(_800bdb88.get() == 0x2L) {
      _800bd7ac.setu(0x1L);
    }

    //LAB_80020fd0
  }

  @Method(0x80024654L)
  public static void FUN_80024654() {
    FUN_800fbec8(_80052ae0.getAddress());
  }

  @Method(0x8002498cL)
  public static void FUN_8002498c() {
    // empty
  }

  @Method(0x80024994L)
  public static void FUN_80024994() {
    // empty
  }

  @Method(0x800249b4L)
  public static void FUN_800249b4(final Value a0, final long a1, final long a2) {
    final RECT[] rects = new RECT[28]; // image size, clut size, image size, clut size...

    for(int i = 0; i < 28; i++) {
      rects[i] = new RECT().set(rectArray28_80010770.get(i)); // Detach from the heap
    }

    fillRects(rects, 2, 0, 0x10L);
    rects[2].w.set((short)0x40);
    rects[2].h.set((short)0x10);

    final int[] indexOffsets = {0, 20, 22, 24, 26};

    //LAB_80024e88
    for(int s2 = 0; s2 < a0.deref(4).offset(0x4L).get(); s2++) {
      if(a0.deref(4).offset(s2 * 8L).offset(0xcL).get() != 0) {
        final TimHeader tim = parseTimHeader(a0.deref(4).offset(a0.deref(4).offset(s2 * 8L).offset(0x8L).get()).offset(0x4L));
        final int rectIndex = indexOffsets[s2];

        // Iteration 0 will count as >= 2
        if(MathHelper.unsign(s2 - 1, 4) >= 0x2L) {
          LoadImage(rects[rectIndex], tim.getImageAddress());
        }

        //LAB_80024efc
        if(s2 == 0x3L) {
          //LAB_80024f2c
          LoadImage(rects[s2 * 2 + 1], tim.getClutAddress());
        } else if(s2 < 0x4L) {
          //LAB_80024fac
          for(long s0 = 0; s0 < 4; s0++) {
            final RECT rect = new RECT().set(rects[rectIndex + 1]);
            rect.x.set((short)(rect.x.get() + s0 * 16));
            LoadImage(rect, tim.getClutAddress() + s0 * 0x80L);
          }
          //LAB_80024f1c
        } else if(s2 == 0x4L) {
          //LAB_80024f68
          LoadImage(rects[rectIndex + 1], tim.getClutAddress());
        }

        //LAB_80025000
        DrawSync(0);
      }

      //LAB_80025008
    }

    //LAB_80025018
    removeFromLinkedList(a0.get());
  }

  @Method(0x8002504cL)
  public static void FUN_8002504c() {
    FUN_80015310(0, 0x1a0dL, 0, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_800249b4", Value.class, long.class, long.class), 0, 0x4L);
    FUN_8002498c();

    _800bdf00.setu(0xdL);
    _800bdf04.setu(0);
    _800bdf08.setu(0);
    _800be5c4.setu(0);
    FUN_8002a6fc();

    //LAB_800250c0
    for(int i = 0; i < 8; i++) {
      FUN_80029920(i, 0);
    }

    //LAB_800250ec
    for(int i = 0; i < 8; i++) {
      _800be358.offset(i *  76L).setu(0);
      _800bdf38.offset(i * 132L).setu(0);
    }

    //LAB_80025118
    for(int i = 0; i < 8; i++) {
      _800bdf18.offset(i * 4L).setu(0);
    }

    _800be5b8.setu(0);
    _800be5bc.setu(0);
    _800be5c0.setu(0);
    _800be5c8.setu(0);
  }

  @Method(0x80025a04L)
  public static void FUN_80025a04(final long a0) {
    assert false;
    //TODO
  }

  @Method(0x80025f4cL)
  public static void FUN_80025f4c(final long a0) {
    assert false;
    //TODO
  }

  @Method(0x800264b0L)
  public static void FUN_800264b0(final long a0) {
    assert false;
    //TODO
  }

  @Method(0x800282acL)
  public static void FUN_800282ac(final long a0) {
    assert false;
    //TODO
  }

  @Method(0x80029920L)
  public static void FUN_80029920(final long a0, final long a1) {
    if(a1 == 0) {
      _800bdea0.offset(a0 * 12).setu(0);
    } else {
      //LAB_80029948
      _800bdea0.offset(a0 * 12).oru(0x1L);
    }

    //LAB_80029970
    final long v0 = _800be358.offset(a0 * 0x4c).getAddress();
    final long v1 = _800bdea0.offset(a0 * 0xc).getAddress();
    MEMORY.ref(2, v1).offset(0x4L).setu(_800be358.offset(a0 * 0x4c).offset(0x14L));
    MEMORY.ref(2, v1).offset(0x8L).setu(0);
    MEMORY.ref(2, v1).offset(0x6L).setu(MEMORY.ref(2, v0).offset(0x16L).get() + MEMORY.ref(2, v0).offset(0x1aL).get() * 6);
  }

  @Method(0x800299d4L)
  public static void FUN_800299d4(final long a0) {
    assert false;
    //TODO
  }

  @Method(0x8002a0e4L)
  public static void FUN_8002a0e4() {
    long s1 = _800be358.getAddress();
    long s2 = _800bdf38.getAddress();

    //LAB_8002a10c
    for(int s0 = 0; s0 < 0x8; s0++) {
      if(MEMORY.ref(4, s1).get() != 0 && MEMORY.ref(4, s1).offset(0x8L).getSigned() < 0) {
        FUN_80025f4c(s0);
      }

      //LAB_8002a134
      if(MEMORY.ref(4, s2).get() != 0) {
        FUN_800282ac(s0);
        FUN_800299d4(s0);
      }

      //LAB_8002a154
      s2 += 0x84L;
      s1 += 0x4cL;
    }
  }

  @Method(0x8002a6fcL)
  public static void FUN_8002a6fc() {
    long s0 = 0;

    //LAB_8002a730
    for(int s2 = 0; s2 < 9; s2++) {
      _800be5f8.offset(4, s0).setu(0);
      _800be5fc.offset(2, s0).setu(0);
      _800be5fe.offset(2, s0).setu(0);
      _800be600.offset(2, s0).setu(0);
      _800be602.offset(2, s0).setu(0);
      _800be604.offset(2, s0).setu(0);
      _800be606.offset(1, s0).setu(0);
      _800be607.offset(1, s0).setu(0);

      //LAB_8002a758
      for(int i = 0; i < 5; i++) {
        _800be628.offset(1, s0).offset(i).setu(0xffL);
      }

      _800be62d.offset(1, s0).setu(0);

      //LAB_8002a780;
      for(int i = 0; i < 8; i++) {
        _800be62e.offset(1, s0).offset(i).setu(0);
        _800be636.offset(1, s0).offset(i).setu(0);
      }

      _800be63e.offset(2, s0).setu(0);
      _800be640.offset(2, s0).setu(0);
      _800be642.offset(2, s0).setu(0);
      _800be644.offset(2, s0).setu(0);
      _800be646.offset(2, s0).setu(0);
      _800be648.offset(2, s0).setu(0);
      _800be64a.offset(2, s0).setu(0);
      _800be64c.offset(2, s0).setu(0);
      _800be64e.offset(2, s0).setu(0);
      _800be650.offset(2, s0).setu(0);
      _800be652.offset(2, s0).setu(0);
      _800be654.offset(2, s0).setu(0);
      _800be656.offset(2, s0).setu(0);
      _800be658.offset(2, s0).setu(0);
      _800be65a.offset(2, s0).setu(0);
      _800be65c.offset(2, s0).setu(0);
      _800be65e.offset(2, s0).setu(0);
      _800be660.offset(1, s0).setu(0);
      _800be661.offset(1, s0).setu(0);
      _800be662.offset(1, s0).setu(0);
      _800be663.offset(1, s0).setu(0);
      _800be664.offset(1, s0).setu(0);
      _800be665.offset(1, s0).setu(0);
      _800be666.offset(1, s0).setu(0);
      _800be667.offset(1, s0).setu(0);
      _800be668.offset(1, s0).setu(0);
      _800be669.offset(1, s0).setu(0);
      _800be66a.offset(1, s0).setu(0);
      _800be66b.offset(1, s0).setu(0);
      _800be66c.offset(1, s0).setu(0);
      FUN_8002a86c(s2);
      _800be694.offset(2, s0).setu(0);
      _800be696.offset(1, s0).setu(0);
      _800be697.offset(1, s0).setu(0);

      s0 += 160L;
    }

    FUN_8002a8f8();
    _800be5d0.setu(0);
  }

  @Method(0x8002a86cL)
  public static void FUN_8002a86c(final long a0) {
    final long v0 = a0 * 160;

    _800be66e.offset(1, v0).setu(0);
    _800be66f.offset(1, v0).setu(0);
    _800be670.offset(1, v0).setu(0);
    _800be671.offset(1, v0).setu(0);
    _800be672.offset(1, v0).setu(0);
    _800be673.offset(1, v0).setu(0);
    _800be674.offset(1, v0).setu(0);
    _800be675.offset(1, v0).setu(0);
    _800be676.offset(1, v0).setu(0);
    _800be677.offset(1, v0).setu(0);
    _800be678.offset(1, v0).setu(0);
    _800be679.offset(1, v0).setu(0);
    _800be67a.offset(1, v0).setu(0);
    _800be67b.offset(1, v0).setu(0);
    _800be67c.offset(1, v0).setu(0);
    _800be67e.offset(2, v0).setu(0);
    _800be680.offset(2, v0).setu(0);
    _800be682.offset(2, v0).setu(0);
    _800be684.offset(2, v0).setu(0);
    _800be686.offset(2, v0).setu(0);
    _800be688.offset(2, v0).setu(0);
    _800be68a.offset(2, v0).setu(0);
    _800be68c.offset(2, v0).setu(0);
    _800be68e.offset(2, v0).setu(0);
    _800be690.offset(1, v0).setu(0);
    _800be691.offset(1, v0).setu(0);
    _800be692.offset(1, v0).setu(0);
    _800be693.offset(1, v0).setu(0);
  }

  @Method(0x8002a8f8L)
  public static void FUN_8002a8f8() {
    _800be5d8.setu(0);
    _800be5d9.setu(0);
    _800be5da.setu(0);
    _800be5db.setu(0);
    _800be5dc.setu(0);
    _800be5dd.setu(0);
    _800be5de.setu(0);
    _800be5df.setu(0);
    _800be5e0.setu(0);
    _800be5e1.setu(0);
    _800be5e2.setu(0);
    _800be5e3.setu(0);
    _800be5e4.setu(0);
    _800be5e5.setu(0);
    _800be5e6.setu(0);
    _800be5e7.setu(0);
    _800be5e8.setu(0);
    _800be5e9.setu(0);
    _800be5ea.setu(0);
    _800be5eb.setu(0);
    _800be5ec.setu(0);
    _800be5ed.setu(0);
    _800be5ee.setu(0);
    _800be5ef.setu(0);
    _800be5f0.setu(0);
    _800be5f1.setu(0);
    _800be5f2.setu(0);
    _800be5f3.setu(0);
  }

  @Method(0x8002aae8L)
  public static void FUN_8002aae8() {
    assert false;
    //TODO
  }

  @Method(0x8002ac24L)
  public static void FUN_8002ac24() {
    FUN_800fbec8(_80052c4c.getAddress());
  }

  @Method(0x8002ac48L)
  public static long loadDRGN2xBIN() {
    //LAB_8002ac6c
    for(long attempts = 0; attempts < 10; attempts++) {
      //LAB_8002ac74
      for(long i = 0; i < 4; ) {
        i++;

        if(DsSearchFile(new CdlFILE(), String.format("\\SECT\\DRGN2%d.BIN;1", i)) != null) {
          return i;
        }
      }
    }

    //LAB_8002acbc
    return -0x1L;
  }

  @Method(0x8002acd8L)
  public static void FUN_8002acd8(final long a0, final long a1, final long a2) {
    for(int a3 = 0; a3 < 2; a3++) {
      //LAB_8002acfc
      _800bed60.offset(a0 * 144).offset(a3 * 0x10L).setu(0);
      _800bed62.offset(a0 * 144).offset(a3 * 0x10L).setu(0);
      _800bed64.offset(a0 * 144).offset(a3 * 0x10L).setu(0);
      _800bed66.offset(a0 * 144).offset(a3 * 0x10L).setu(0);
      _800bed6c.offset(a0 * 144).offset(a3 * 0x10L).setu(a1);
      _800bed6e.offset(a0 * 144).offset(a3 * 0x10L).setu(a2);

      _800bf068.offset(a0 * 32).offset(a3 * 0x10L).setu(0);
      _800bf06a.offset(a0 * 32).offset(a3 * 0x10L).setu(0);
      _800bf06c.offset(a0 * 32).offset(a3 * 0x10L).setu(0);
      _800bf06e.offset(a0 * 32).offset(a3 * 0x10L).setu(0);
      _800bf074.offset(a0 * 32).offset(a3 * 0x10L).setu(a1);
      _800bf076.offset(a0 * 32).offset(a3 * 0x10L).setu(a2);
    }

    for(int a3 = 0; a3 < 4; a3++) {
      //LAB_8002ad60
      _800beda8.offset(a0 * 144L).offset(a3 * 2L).set(0);
    }

    _800bede0.offset(a0 * 144L).setu(0);
    _800bede1.offset(a0 * 144L).setu(0);
    _800bede2.offset(a0 * 144L).setu(0);
    _800bede3.offset(a0 * 144L).setu(0);
    _800bede8.offset(a0 * 144L).setu(0);
    _800bedec.offset(a0 * 144L).setu(0);
    _800bedb8.offset(a0 * 144L).setu(0);
    _800bedb9.offset(a0 * 144L).setu(0);
    _800bedba.offset(a0 * 144L).setu(0);
    _800bedbb.offset(a0 * 144L).setu(0);
    _800bedbc.offset(a0 * 144L).setu(0);

    for(int a3 = 0; a3 < 0x20; a3++) {
      //LAB_8002adcc
      _800beec4.offset(a3 * 4L).set(0);
      _800bef44.offset(a3 * 4L).set(0);
      _800befc4.offset(a3 * 4L).set(0);
      _800bf044.offset(a3).set(0);
    }

    _800bee90.setu(0);
    _800bee94.setu(0);
    _800bee98.setu(0);
    _800bf064.setu(0);
  }

  @Method(0x8002ae0cL)
  public static long FUN_8002ae0c(final long a0) {
    long s0 = 0;

    for(int s2 = 0; s2 < 2; s2++) {
      long a1;
      long a2;

      do {
        //LAB_8002ae40:
        _800bede0.offset(s2 * 0x90L).setu(FUN_80042e70(s2 * 16L));
        _800bede1.offset(s2 * 0x90L).setu(FUN_80042f40(s2 * 16L, 0x1L, 0));
        _800bedbc.offset(s2 * 0x90L).setu(FUN_80042f40(s2 * 16L, 0x2L, 0));
        _800bede4.offset(s2 * 0x90L).setu(FUN_8002b0bc(_800bed60.offset(s2 * 0x90L).getAddress(), s2));

        s0 = FUN_8002b250((byte)_800bedbc.offset(s2 * 0x90L).getSigned(), _800bed60.offset(s2 * 0x90L).getAddress(), a0, s2);

        FUN_8002bf00(_800bed60.offset(s2 * 0x90L).getAddress());

        a1 = _800bedba.offset(s2 * 0x90L).get() + _800bedbb.offset(s2 * 0x90L).get();
        a2 = _800bedb8.offset(s2 * 0x90L).get() + _800bedb9.offset(s2 * 0x90L).get();

        if(a1 != 0 || a2 == 0) {
          break;
        }

        if(vibrationEnabled_800bb0a9.get() == 0) {
          break;
        }

        _800bede8.offset(s2 * 0x90L).setu(VSync(-1));
        _800bedec.offset(s2 * 0x90L).setu(0x1L);
      } while(true);

      //LAB_8002aef8
      //LAB_8002af00
      if(a1 != 0 && a2 == 0) {
        _800bedec.offset(s2 * 0x90L).setu(0);
      }

      //LAB_8002af0c
      if(_800bedec.offset(s2 * 0x90L).get() != 0) {
        if(Math.abs((int)(_800bede8.offset(s2 * 0x90L).get() - VSync(-1))) > 3600) {
          _800bedb8.offset(s2 * 0x90L).setu(0);
          _800bedb9.offset(s2 * 0x90L).setu(0);
        }
      }

      //LAB_8002af44
      _800bedba.offset(s2 * 0x90L).setu(_800bedb8.offset(s2 * 0x90L));
      _800bedbb.offset(s2 * 0x90L).setu(_800bedb9.offset(s2 * 0x90L));
    }

    if(_800bf0a8.get() == 0) {
      //LAB_8002b090
      FUN_8002b840();
      return s0;
    }

    if(_800bf0ac.get() > 0x20L) {
      _800bf0ac.setu(0x1f);
    }

    //LAB_8002af94
    long t0 = 0;
    long t1 = 0;
    long a3 = 0;
    s0 = 0;

    for(long i = 0; i < _800bf0ac.get(); i++) {
      //LAB_8002afc4
      long s2 = _800bf064.get() - 0x1L - i;

      if(s2 < 0) {
        s2 += 0x20;
      }

      //LAB_8002afd4
      a3 |= _800beec4.offset(s2 * 4).get();
      t0 |= _800bef44.offset(s2 * 4).get();
      t1 |= _800befc4.offset(s2 * 4).get();
      s0 |= _800bf044.offset(s2).get();
    }

    //LAB_8002b00c
    _800bee90.setu(a3);
    _800bee94.setu(t0);
    _800bee98.setu(t1);
    _800bf0a8.setu(0);
    _800bf0ac.setu(0);

    for(int i = 0; i < 2; i++) {
      //LAB_8002b040
      _800bee9c.offset(i * 4).setu(_800bedb2.offset(i * 0x90));
      _800beea4.offset(i * 4).setu(_800bed9c.offset(i * 0x90));
      _800beeac.offset(i * 4).setu(_800beda4.offset(i * 0x90));
      _800beeb4.offset(i * 4).setu(_800bedb6.offset(i * 0x90));
      _800beebc.offset(i * 4).setu(_800bede3.offset(i * 0x90));
    }

    //LAB_8002b09c
    return s0;
  }

  @Method(0x8002b0bcL)
  public static long FUN_8002b0bc(final long a0, final long a1) {
    _800bee80.offset(a1 * 4).setu(0);

    if(MEMORY.ref(1, a0).offset(0x80L).get() == 0) {
      return 0;
    }

    long s0 = 0;

    switch((int)MEMORY.ref(1, a0).offset(0x81L).get()) {
      case 1, 2, 3, 5, 6 -> s0 = -0x1L;
      case 7 -> s0 = 0x1L;
      case 4 -> _800bee80.offset(a1 * 4).setu(0x1L);
    }

    //caseD_8
    if(vibrationEnabled_800bb0a9.get() == 0 || _800bee80.get() != 0) {
      //LAB_8002b17c
      MEMORY.ref(1, a0).offset(0x58L).setu(0);
      MEMORY.ref(1, a0).offset(0x59L).setu(0);
    }

    //LAB_8002b184
    if(MEMORY.ref(1, a0).offset(0x80L).get() == 0x1L) {
      MEMORY.ref(1, a0).offset(0x82L).setu(0);
    }

    //LAB_8002b198
    if(MEMORY.ref(1, a0).offset(0x82L).get() != 0) {
      return s0;
    }

    if(MEMORY.ref(1, a0).offset(0x80L).get() == 0x6L) {
      if(_800bee8c.get() == 0) {
        if(FUN_80042d10(0, 0x1L, 0)) {
          _800bee8c.setu(0x2L);
        }

        return s0;
      }
    }

    //LAB_8002b1ec
    if(MEMORY.ref(1, a0).offset(0x80L).get() == 0x2L) {
      MEMORY.ref(1, a0).offset(0x82L).setu(0x1L);
    } else if(MEMORY.ref(1, a0).offset(0x80L).get() == 0x6L) {
      if(FUN_80043040(a1 * 16, a1, 4) < 60) {
        if(FUN_80042ba0(a1 * 16, _80052c5c.getAddress()) != 0) {
          //LAB_8002b22c
          MEMORY.ref(1, a0).offset(0x82L).setu(0x1L);
        }
      }
    }

    //LAB_8002b234
    //LAB_8002b238
    return s0;
  }

  @Method(0x8002b250L)
  private static long FUN_8002b250(final long a0, final long a1, final long a2, final long a3) {
    if(MEMORY.ref(1, a1).offset(0x5eL).get() != 0) {
      //LAB_8002b39c
      for(int i = 0; i < 2; i++) {
        //LAB_8002b3b0
        MEMORY.ref(8, a1).offset(i * 0x10L).setu(0);
        MEMORY.ref(1, a1).offset(i).offset(0x58L).setu(0);
        _800bf068.offset(8, a3 * 0x20L).offset(i * 0x10L).setu(0);
      }

      return 1;
    }

    if((int)a0 < 0) {
      //LAB_8002b340
      for(int i = 0; i < 2; i++) {
        //LAB_8002b354
        MEMORY.ref(8, a1).offset(i * 0x10L).setu(0);
        MEMORY.ref(1, a1).offset(i).offset(0x58L).setu(0);
        _800bf068.offset(8, a3 * 0x20L).offset(i * 0x10L).setu(0);
      }

      return 1;
    }

    MEMORY.ref(2, a1).setu(MEMORY.ref(2, a1).offset(0x2L));
    MEMORY.ref(2, a1).offset(0x2L).setu(MEMORY.ref(1, a1).offset(0x60L).get() << 8 | MEMORY.ref(1, a1).offset(0x61L).get());

    FUN_8002c23c(a1 + 0x2L);

    MEMORY.ref(2, a1).offset(0x10L).setu(MEMORY.ref(2, a1).offset(0x12L));

    FUN_8002b980(a1);

    MEMORY.ref(1, a1).offset(0x83L).setu(0x1L);

    if(a0 == 0) {
      MEMORY.ref(2, a1).offset(0x12L).setu(0xffffL);

      for(int i = 0; i < 4; i++) {
        //LAB_8002b2d4
        MEMORY.ref(2, i).offset(i * 2L).offset(0x48L).setu(0);
      }

      for(int i = 0; i < 2; i++) {
        //LAB_8002b2f0
        MEMORY.ref(4, a1).offset(i * 4L).offset(0x38L).setu(0);
        MEMORY.ref(4, a1).offset(i * 4L).offset(0x40L).setu(0);
        MEMORY.ref(2, a1).offset(i * 2L).offset(0x50L).setu(0);
        MEMORY.ref(2, a1).offset(i * 2L).offset(0x54L).setu(0);
      }

      MEMORY.ref(1, a1).offset(0x83L).setu(0);
    }

    //LAB_8002b318
    for(int i = 0; i < 2; i++) {
      //LAB_8002b31c
      FUN_8002c190(a1 + i * 0x10L, a2);
    }

    //LAB_8002b3f4
    return 0;
  }

  @Method(0x8002b40cL)
  public static void FUN_8002b40c(long a0, final long a1, long a2) {
    final byte[] sp10 = new byte[8];
    final byte[] sp18 = new byte[8];
    final byte[] sp20 = new byte[4];

    final long s8 = a0;
    if(MEMORY.ref(1, a0).offset(0x5eL).get() == 0) {
      if(MEMORY.ref(1, a1).offset(0x86L).getSigned() >= 0) {
        MEMORY.ref(2, a1).setu(MEMORY.ref(2, a1).offset(0x2L));
        MEMORY.ref(2, a1).offset(0x2L).setu(MEMORY.ref(1, s8).offset(0x60L).get() << 8 | MEMORY.ref(1, s8).offset(0x61L).get());
        FUN_8002c23c(a1 + 0x2L);

        MEMORY.ref(2, a1).offset(0x10L).setu(MEMORY.ref(2, a1).offset(0x12L));

        //LAB_8002b48c
        for(int i = 0; i < 4; i++) {
          a0 = MEMORY.ref(1, s8).offset(i).offset(0x62L).get() - 0x80L;

          if(Math.abs(a0) < 0x30L) {
            MathHelper.set(sp18, i * 2, 2, 0);
          } else {
            MathHelper.set(sp18, i * 2, 2, a0);
          }

          //LAB_8002b4b4
        }

        //LAB_8002b4dc
        long v1;
        for(int i = 0; i < 2; i++) {
          long v0 = 0x800L - FUN_80040b90(MathHelper.get(sp18, i * 4, 2), MathHelper.get(sp18, i * 4 + 2, 2));
          MathHelper.set(sp20, i * 2, 2, v0);
          if(MathHelper.get(sp18, i * 4, 2) != 0 || MathHelper.get(sp18, i * 4 + 2, 2) != 0) {
            //LAB_8002b51c
            a0 = MathHelper.get(sp20, i * 2, 2);
            v1 = a0 + 0x100L;
            if((int)v1 < 0) {
              v0 = a0 + 0x10ffL;
            } else {
              v0 = v1;
            }

            //LAB_8002b534
            v0 >>= 0xcL;
            v0 <<= 0xcL;
            v0 = v1 - v0;
            if((int)v0 < 0) {
              v0 += 0x1ffL;
            }

            //LAB_8002b54c
            v0 >>= 0x9L;
            v0 = _80052c64.offset(v0).deref(1).get();
          } else {
            v0 = 0xffL;
          }

          //LAB_8002b560
          MathHelper.set(sp10, i * 4, 4, v0);
        }

        long v0 = MathHelper.get(sp10, 4, 2);
        v1 = MathHelper.get(sp10, 0, 2);
        v0 <<= 0xcL;
        v1 <<= 0x4L;
        v0 |= v1;
        v0 |= 0xfL;
        MEMORY.ref(2, a1).offset(0x12L).setu(v0);
        if(MEMORY.ref(1, s8).offset(0x84L).get() == 0) {
          MEMORY.ref(2, a1).offset(0x12L).setu(0xffffL);
        }

        //LAB_8002b5b8
        //LAB_8002b5bc
        for(int i = 0; i < 2; i++) {
          FUN_8002c190(a1 + i * 0x10L, 0x1L);
        }

        return;
      }

      //LAB_8002b5e0
      a2 *= 32;

      //LAB_8002b5f4
      for(int i = 0; i < 2; i++) {
        a2 += 0x10L;
        MEMORY.ref(2, s8).offset(i * 0x10L).offset(0x0L).setu(0);
        MEMORY.ref(2, s8).offset(i * 0x10L).offset(0x2L).setu(0);
        MEMORY.ref(2, s8).offset(i * 0x10L).offset(0x4L).setu(0);
        MEMORY.ref(2, s8).offset(i * 0x10L).offset(0x6L).setu(0);
        MEMORY.ref(1, s8).offset(i).offset(0x58L).setu(0);
        _800bf068.offset(a2).setu(0);
        _800bf06a.offset(a2).setu(0);
        _800bf06c.offset(a2).setu(0);
        _800bf06e.offset(a2).setu(0);
      }

      return;
    }

    //LAB_8002b63c
    a2 *= 32;

    //LAB_8002b650
    for(int i = 0; i < 2; i++) {
      a2 += 0x10L;
      MEMORY.ref(2, s8).offset(i * 0x10L).offset(0x0L).setu(0);
      MEMORY.ref(2, s8).offset(i * 0x10L).offset(0x2L).setu(0);
      MEMORY.ref(2, s8).offset(i * 0x10L).offset(0x4L).setu(0);
      MEMORY.ref(2, s8).offset(i * 0x10L).offset(0x6L).setu(0);
      MEMORY.ref(1, s8).offset(i).offset(0x58L).setu(0);
      _800bf068.offset(a2).setu(0);
      _800bf06a.offset(a2).setu(0);
      _800bf06c.offset(a2).setu(0);
      _800bf06e.offset(a2).setu(0);
    }

    //LAB_8002b690
  }

  @Method(0x8002b6c0L)
  public static void FUN_8002b6c0() {
    final long[] stack = new long[6];

    long v0;
    long v1;
    for(int t2 = 0; t2 < 2; t2++) {
      //LAB_8002b6e0
      v0 = _800bf07a.offset(t2 * 0x20L).offset(0x12L).get(0xf90fL);
      v1 = _800bf06a.offset(t2 * 0x20L).offset(0x02L).get();
      stack[t2 * 3] = v1 | v0;

      v0 = _800bf07c.offset(t2 * 0x20L).offset(0x14L).get(0xf90fL);
      v1 = _800bf06c.offset(t2 * 0x20L).offset(0x04L).get();
      stack[t2 * 3 + 1] = v1 | v0;

      v0 = _800bf07e.offset(t2 * 0x20L).offset(0x16L).get(0xf90fL);
      v1 = _800bf06e.offset(t2 * 0x20L).offset(0x06L).get();
      stack[t2 * 3 + 2] = v1 | v0;

      for(int a3 = 0; a3 < 3; a3++) {
        //LAB_8002b724
        if((stack[t2 * 3 + a3] & 0x5000L) != 0) {
          stack[t2 * 3 + a3] &= 0xafffL;
        }

        if((stack[t2 * 3 + a3] & 0xa000L) != 0) {
          stack[t2 * 3 + a3] &= 0x5fffL;
        }

        //LAB_8002b75c
      }
    }

    _800beec4.offset(_800bf064.get() * 4).setu(stack[0]);
    _800bef44.offset(_800bf064.get() * 4).setu(stack[1]);
    _800befc4.offset(_800bf064.get() * 4).setu(stack[2]);

    if(_800bedbe.get() == 0) {
      _800bf044.offset(_800bf064).setu(0);
    } else {
      //LAB_8002b7f8
      _800bf044.offset(_800bf064).setu(0x1L);
    }

    //LAB_8002b808
    final long a0 = _800bf064.get();
    v1 = a0 + 0x1L;
    if((int)v1 < 0) {
      v0 = a0 + 0x20L;
    } else {
      v0 = v1;
    }

    //LAB_8002b828
    v0 >>= 0x5L;
    v0 <<= 0x5L;
    _800bf064.setu(v1 - v0);
  }

  @Method(0x8002b840L)
  public static void FUN_8002b840() {
    //TODO why is this two sets of 3 numbers when only the first set is used?
    final long[] values = new long[6];

    //LAB_8002b860
    for(int t2 = 0; t2 < 2; t2++) {
      values[t2 * 3    ] = (_800bed62.offset(t2 * 0x90).get() | _800bed72.offset(t2 * 0x90).get()) & 0b1111_1001_0000_1111L;
      values[t2 * 3 + 1] = (_800bed64.offset(t2 * 0x90).get() | _800bed74.offset(t2 * 0x90).get()) & 0b1111_1001_0000_1111L;
      values[t2 * 3 + 2] = (_800bed66.offset(t2 * 0x90).get() | _800bed76.offset(t2 * 0x90).get()) & 0b1111_1001_0000_1111L;

      //LAB_8002b8a4
      for(int t0 = 0; t0 < 3; t0++) {
        if((values[t2 * 3 + t0] & 0b0101_0000_0000_0000L) != 0) {
          values[t2 * 3 + t0] &= 0b1010_1111_1111_1111L;
        }

        //LAB_8002b8d0
        if((values[t2 * 3 + t0] & 0b1010_0000_0000_0000L) != 0) {
          values[t2 * 3 + t0] &= 0b0101_1111_1111_1111L;
        }

        //LAB_8002b8dc
      }
    }

    _800bee90.setu(values[0]);
    _800bee94.setu(values[1]);
    _800bee98.setu(values[2]);

    //LAB_8002b930
    for(long t0 = 0; t0 < 2; t0++) {
      _800bee9c.offset(t0 * 4).setu(_800bedb2.offset(2, t0 * 0x90));
      _800beea4.offset(t0 * 4).setu(_800bed9c.offset(4, t0 * 0x90));
      _800beeac.offset(t0 * 4).setu(_800beda4.offset(4, t0 * 0x90));
      _800beeb4.offset(t0 * 4).setu(_800bedb6.offset(2, t0 * 0x90));
      _800beebc.offset(t0 * 4).setu(_800bede3.offset(1, t0 * 0x90));
    }
  }

  @Method(0x8002b980L)
  public static void FUN_8002b980(final long a0) {
    assert false;
    //TODO
  }

  @Method(0x8002bcc8L)
  public static void FUN_8002bcc8(final long a0, final long a1) {
    assert false;
    //TODO
  }

  @Method(0x8002bda4L)
  public static void FUN_8002bda4(final long a0, final long a1, long a2) {
    assert false;
    //TODO
  }

  @Method(0x8002bf00L)
  private static void FUN_8002bf00(final long a0) {
    if(MEMORY.ref(1, a0).offset(0x34L).get() == 0) {
      return;
    }

    if(vibrationEnabled_800bb0a9.get() == 0 || _800bee80.get() != 0) {
      //LAB_8002bf30
      MEMORY.ref(1, a0).offset(0x58L).setu(0);
      MEMORY.ref(1, a0).offset(0x59L).setu(0);
      MEMORY.ref(1, a0).offset(0x34L).setu(0);
      return;
    }

    //LAB_8002bf40
    //LAB_8002bf44
    for(int a2 = 0; a2 < 2; a2++) {
      MEMORY.ref(4, a0).offset(a2 * 0x4L).setu(MEMORY.ref(4, a0).offset(a2 * 0x4L).offset(0x20L).get() + MEMORY.ref(4, a0).offset(a2 * 0x4L).offset(0x28L).get());
    }

    long a1 = MEMORY.ref(4, a0).offset(0x20L).get() / 0xc000L;

    if(a1 != 0) {
      a1 = 0x1L;
    }

    //LAB_8002bf90
    MEMORY.ref(1, a0).offset(0x58L).setu(a1);
    MEMORY.ref(1, a0).offset(0x59L).setu(MEMORY.ref(2, a0).offset(0x26L));
    MEMORY.ref(4, a0).offset(0x30L).subu(0x1L);

    if(MEMORY.ref(4, a0).offset(0x30L).getSigned() <= 0) {
      MEMORY.ref(1, a0).offset(0x34L).setu(0);
      MEMORY.ref(1, a0).offset(0x58L).setu(MEMORY.ref(1, a0).offset(0x35L));
      MEMORY.ref(1, a0).offset(0x59L).setu(MEMORY.ref(1, a0).offset(0x36L));
    }

    //LAB_8002bfc0
    if(MEMORY.ref(2, a0).offset(0x5cL).get() != 0) {
      return;
    }

    if((MEMORY.ref(1, a0).offset(0x58L).get() | MEMORY.ref(1, a0).offset(0x59L).get()) != 0) {
      MEMORY.ref(1, a0).offset(0x58L).setu(0x40L);
      MEMORY.ref(1, a0).offset(0x59L).setu(0x1L);
      return;
    }

    //LAB_80002bff8
    MEMORY.ref(1, a0).offset(0x58L).setu(0);
    MEMORY.ref(1, a0).offset(0x59L).setu(0);

    //LAB_8002c000
  }

  /**
   * This might be initializing controllers
   */
  @Method(0x8002c008L)
  public static void FUN_8002c008() {
    FUN_80043230(_800bedbe.getAddress(), _800bee4e.getAddress());

    for(int i = 0; i < 2; i++) {
      //LAB_8002c034
      FUN_8002acd8(i, 0x14L, 0x4L);
      _800bee80.offset(i * 4L).setu(0);
    }

    FUN_80042b60(0, _800bedb8.getAddress(), 0x2L);
    FUN_80042b60(0x10L, _800bee48.getAddress(), 0x2L);

    //TODO: disabling memcard handlers
    //FUN_80043120();
    LOGGER.warn("Skipping memcard stuff");

    vibrationEnabled_800bb0a9.setu((byte)0x1L);
    _800bee88.setu(0);
    _800bee8c.setu(0);
    _800bf0a8.setu(0);
    _800bf0ac.setu(0);
  }

  @Method(0x8002c0c8L)
  public static void FUN_8002c0c8() {
    //LAB_8002c0f0
    for(int i = 0; i < 2; i++) {
      FUN_8002b40c(_800bed60.offset(i * 0x90L).getAddress(), _800bf068.offset(i * 0x20L).getAddress(), i);
    }

    FUN_8002b6c0();

    _800bf0a8.setu(0x1L);
    _800bf0ac.addu(0x1L);
  }

  @Method(0x8002c150L)
  public static void FUN_8002c150(final long a0) {
    _800bed94.offset(a0 * 144).setu(0);
    _800bedb8.offset(a0 * 144).setu(0);
    _800bedb9.offset(a0 * 144).setu(0);
  }

  @Method(0x8002c190L)
  public static void FUN_8002c190(final long a0, final long a1) {
    MEMORY.ref(2, a0).offset(0x2L).notu();
    if(MEMORY.ref(2, a0).offset(0x2L).get(0x5000L) == 0x5000L) {
      MEMORY.ref(2, a0).offset(0x2L).setu(MEMORY.ref(2, a0).offset(0x2L).get(0xafffL));
    }

    //LAB_8002c1b4
    if(MEMORY.ref(2, a0).offset(0x2L).get(0xa000L) == 0xa000L) {
      MEMORY.ref(2, a0).offset(0x2L).setu(MEMORY.ref(2, a0).offset(0x2L).get(0x5fffL));
    }

    //LAB_8002c1cc
    final long v1 = MEMORY.ref(2, a0).offset(0x2L).get(~MEMORY.ref(2, a0).get());
    MEMORY.ref(2, a0).offset(0x4L).setu(v1);
    if(v1 != 0) {
      MEMORY.ref(2, a0).offset(0x6L).setu(v1);
      MEMORY.ref(2, a0).offset(0x8L).setu(MEMORY.ref(2, a0).offset(0xcL));
      return;
    }

    //LAB_8002c1f4
    if(MEMORY.ref(2, a0).offset(0x2L).get() == 0 || (int)(MEMORY.ref(2, a0).offset(0x8L).subu(a1).get() << 16) > 0) {
      //LAB_8002c220
      MEMORY.ref(2, a0).offset(0x6L).setu(0);
      return;
    }

    //LAB_8002c228
    MEMORY.ref(2, a0).offset(0x6L).setu(MEMORY.ref(2, a0).offset(0x2L));
    MEMORY.ref(2, a0).offset(0x8L).setu(MEMORY.ref(2, a0).offset(0xeL));
  }

  @Method(0x8002c23cL)
  public static void FUN_8002c23c(final long a0) {
    MEMORY.ref(2, a0).setu(MEMORY.ref(2, a0).get(0xff9fL) | MEMORY.ref(1, a0).get(0x40L) / 2 | MEMORY.ref(1, a0).get(0x20L) * 2);
  }

  @Method(0x8002c86cL)
  public static void FUN_8002c86c() {
    if(_800bf0cd.get() != 0) {
      _800bf0c0.addu(_800bf0c4);
      _800bf0ce.setu(_800bf0c0).shra(0x10L);
      _800bf0c8.subu(0x1L);

      if(_800bf0c8.getSigned() <= 0) {
        _800bf0cd.setu(0);
        _800bf0ce.setu(_800bf0cc);
      }

      //LAB_8002c8c4
      setCdVolume(_800bf0ce.get(), _800bf0ce.get());
      setCdMix(0x3fL);
    }

    //LAB_8002c8dc
    if(_800bf0d8.get() == 0x1L) {
      callbackIndex_8004ddc4.setu(0x15L);

      //TODO
//      LOGGER.error("NOTE: skipping intro FMV");
//      callbackIndex_8004ddc4.setu(0x18L);
//      _800bf0b4.setu(0x1L);
//      _800bf0d8.setu(0x4L);
//      FUN_8001e29c(8);
      //
    }

    //LAB_8002c8f4
  }

  @Method(0x8002c904L)
  public static void setCdMix(final long volume) {
    if(mono_800bb0a8.get() == 0) {
      //LAB_8002c95c
      CdMix(volume, 0, volume, 0);
    } else {
      final long mixedVol = volume * 70 / 100;
      CdMix(mixedVol, mixedVol, mixedVol, mixedVol);
    }
  }

  @Method(0x8002ced8L)
  public static void start(final int argc, final long argv) {
    for(int i = 0; i < 0x6c4b0; i += 4) {
      _8005a1d8.offset(i).setu(0);
    }

    _80052dc0.setu(ramSize_800e6f04.get() - 0x8L - stackSize_800e6f08.get() - _800c6688.getAddress());
    _80052dbc.setu(_800c6688.getAddress());

    main();

    assert false : "Shouldn't get here";
  }

  @Method(0x8002d060L)
  public static void _bu_init() {
    functionVectorA_000000a0.run(0x70L, EMPTY_OBJ_ARRAY);
  }

  @Method(0x8002d070L)
  public static void SetMem(final int size) {
    functionVectorA_000000a0.run(0x9fL, new Object[] {size});
  }

  @Method(0x8002d220L)
  public static int strcmp(final String s1, final String s2) {
    return (int)functionVectorA_000000a0.run(0x17L, new Object[] {s1, s2});
  }

  @Method(0x8002d230L)
  public static int strncmp(final String s1, final String s2, final int length) {
    return (int)functionVectorA_000000a0.run(0x18L, new Object[] {s1, s2, length});
  }

  @Method(0x8002d260L)
  public static int rand() {
    return (int)functionVectorA_000000a0.run(0x2fL, EMPTY_OBJ_ARRAY);
  }

  @Method(0x8002d280L)
  public static void initMemcard(final boolean sharedWithController) {
    initMemcard1(sharedWithController);
    initMemcard2();
    _bu_init();
  }

  @Method(0x8002d2d0L)
  public static void initMemcard1(final boolean sharedWithController) {
    ChangeClearPAD(false);
    VSync(0);

    final boolean enteredCriticalSection = EnterCriticalSection();

    final boolean s0;
    if(get80052e1c() == 0) {
      s0 = false;
    } else {
      s0 = sharedWithController;
    }

    //LAB_8002d310
    InitCARD(s0);

    LOGGER.warn("Skipping bios patches");
    //TODO
//    copyPatches();
//    early_card_irq_patch();
//    patch_card_specific_delay();
//    patch_card_info_step4();

    if(enteredCriticalSection) {
      ExitCriticalSection();
    }
  }

  @Method(0x8002d360L)
  public static void initMemcard2() {
    final boolean enteredCriticalSection = EnterCriticalSection();

    StartCARD();
    ChangeClearPAD(false);

    if(enteredCriticalSection) {
      ExitCriticalSection();
    }
  }

  @Method(0x8002d3f0L)
  public static boolean ChangeClearPAD(final boolean val) {
    return (boolean)functionVectorB_000000b0.run(0x5bL, new Object[] {val});
  }

  @Method(0x8002d40cL)
  public static long get80052e1c() {
    return _80052e1c.get();
  }

  @Method(0x8002d6c0L)
  public static long SysEnqIntRP(final int priority, final PriorityChainEntry struct) {
    return (long)functionVectorC_000000c0.run(0x2L, new Object[] {priority, struct});
  }

  @Method(0x8002d6d0L)
  @Nullable
  public static PriorityChainEntry SysDeqIntRP(final int priority, final PriorityChainEntry struct) {
    return (PriorityChainEntry)functionVectorC_000000c0.run(0x3L, new Object[] {priority, struct});
  }

  @Method(0x8002d800L)
  public static void InitCARD(final boolean pad_enable) {
    functionVectorB_000000b0.run(0x4aL, new Object[] {pad_enable});
  }

  @Method(0x8002d810L)
  public static int StartCARD() {
    return (int)functionVectorB_000000b0.run(0x4bL, EMPTY_OBJ_ARRAY);
  }

  @Method(0x8002db2cL)
  public static void FUN_8002db2c() {
    resetMemcardEventIndex();

    _800bf170.setu(0);
    _800bf174.setu(0);
    _800bf178.setu(0);
    _800bf17c.setu(0);
    _800bf184.setu(0xffff_ffffL);

    _800bf1b4.clear();
    _800bf1b8.setu(0x1L);
    _800bf1bc.setu(0x1L);
    _800bf1c0.setu(0);
    _800bf1c4.setu(0);

    setupMemcardEvents();
    SetVsyncInterruptCallback(InterruptType.CONTROLLER, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_8002f298"));
  }

  @Method(0x8002f298L)
  public static void FUN_8002f298() {
    if(FUN_8002f848() == 0) {
      FUN_8002f7dc();

      if(FUN_8002f848() != 0) {
        _800bf160.setu(_800bf170);
        _800bf164.setu(_800bf174);
        _800bf170.setu(0);
        _800bf174.setu(0);
        _800bf178.setu(0x1L);

        if(!_800bf1b4.isNull()) {
          _800bf1b4.deref().run(_800bf160.get(), _800bf164.get());
        }
      }
    }

    //LAB_8002f30c
    _800bf1c0.addu(0x1L);
    _800bf1c4.addu(0x1L);
  }

  @Method(0x8002f750L)
  public static void resetMemcardEventIndex() {
    memcardEventIndex_80052e4c.setu(0xffff_ffffL);
  }

  @Method(0x8002f7dcL)
  public static void FUN_8002f7dc() {
    final long v1 = memcardEventIndex_80052e4c.get();
    if((int)v1 >= 0) {
      if((long)_800bf23c.offset(v1 * 4).deref(4).call(_800bf200.offset(v1 * 16).getAddress()) != 0) {
        memcardEventIndex_80052e4c.subu(0x1L);
      }
    }
  }

  @Method(0x8002f848L)
  public static long FUN_8002f848() {
    return memcardEventIndex_80052e4c.get() >>> 0x1fL;
  }

  @Method(0x8002f860L)
  public static int handleCardDoneRead() {
    _800bf270.setu(0x1L);
    return 0;
  }

  @Method(0x8002f874L)
  public static int handleCardErrorWrite() {
    _800bf274.setu(0x1L);
    return 0;
  }

  @Method(0x8002f888L)
  public static int handleCardErrorBusy() {
    _800bf278.setu(0x1L);
    return 0;
  }

  @Method(0x8002f89cL)
  public static int handleCardErrorEject() {
    _800bf27c.setu(0x1L);
    return 0;
  }

  @Method(0x8002f8b0L)
  public static int handleCardFinishedOkay() {
    _800bf280.setu(0x1L);
    return 0;
  }

  @Method(0x8002f8c4L)
  public static int handleCardError8000() {
    _800bf284.setu(0x1L);
    return 0;
  }

  @Method(0x8002f8d8L)
  public static int handleCardErrorBusyLow() {
    _800bf288.setu(0x1L);
    return 0;
  }

  @Method(0x8002f8ecL)
  public static int handleCardError2000() {
    _800bf28c.setu(0x1L);
    return 0;
  }

  @Method(0x8002f934L)
  public static void setupMemcardEvents() {
    final boolean enteredCriticalSection = EnterCriticalSection();

    SwCARD_EvSpIOE_EventId_800bf250.setu(OpenEvent(SwCARD, EvSpIOE, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardDoneRead")));
    SwCARD_EvSpERROR_EventId_800bf254.setu(OpenEvent(SwCARD, EvSpERROR, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardErrorWrite")));
    SwCARD_EvSpTIMOUT_EventId_800bf258.setu(OpenEvent(SwCARD, EvSpTIMOUT, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardErrorBusy")));
    SwCARD_EvSpNEW_EventId_800bf25c.setu(OpenEvent(SwCARD, EvSpNEW, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardErrorEject")));
    HwCARD_EvSpIOE_EventId_800bf260.setu(OpenEvent(HwCARD, EvSpIOE, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardFinishedOkay")));
    HwCARD_EvSpERROR_EventId_800bf264.setu(OpenEvent(HwCARD, EvSpERROR, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardError8000")));
    HwCARD_EvSpTIMOUT_EventId_800bf268.setu(OpenEvent(HwCARD, EvSpTIMOUT, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardErrorBusyLow")));
    HwCARD_EvSpNEW_EventId_800bf26c.setu(OpenEvent(HwCARD, EvSpNEW, EvMdINTR, getMethodAddress(Scus94491BpeSegment_8002.class, "handleCardError2000")));

    EnableEvent((int)SwCARD_EvSpIOE_EventId_800bf250.get());
    EnableEvent((int)SwCARD_EvSpERROR_EventId_800bf254.get());
    EnableEvent((int)SwCARD_EvSpTIMOUT_EventId_800bf258.get());
    EnableEvent((int)SwCARD_EvSpNEW_EventId_800bf25c.get());
    EnableEvent((int)HwCARD_EvSpIOE_EventId_800bf260.get());
    EnableEvent((int)HwCARD_EvSpERROR_EventId_800bf264.get());
    EnableEvent((int)HwCARD_EvSpTIMOUT_EventId_800bf268.get());
    EnableEvent((int)HwCARD_EvSpNEW_EventId_800bf26c.get());

    testEvents();

    if(enteredCriticalSection) {
      ExitCriticalSection();
    }
  }

  @Method(0x8002fbe0L)
  public static void testEvents() {
    TestEvent((int)SwCARD_EvSpIOE_EventId_800bf250.get());
    TestEvent((int)SwCARD_EvSpERROR_EventId_800bf254.get());
    TestEvent((int)SwCARD_EvSpTIMOUT_EventId_800bf258.get());
    TestEvent((int)SwCARD_EvSpNEW_EventId_800bf25c.get());
    TestEvent((int)HwCARD_EvSpIOE_EventId_800bf260.get());
    TestEvent((int)HwCARD_EvSpERROR_EventId_800bf264.get());
    TestEvent((int)HwCARD_EvSpTIMOUT_EventId_800bf268.get());
    TestEvent((int)HwCARD_EvSpNEW_EventId_800bf26c.get());

    _800bf270.setu(0);
    _800bf274.setu(0);
    _800bf278.setu(0);
    _800bf27c.setu(0);
    _800bf280.setu(0);
    _800bf284.setu(0);
    _800bf288.setu(0);
    _800bf28c.setu(0);
  }

  @Method(0x8002ff10L)
  public static int OpenEvent(final long desc, final int spec, final int mode, final long func) {
    return (int)functionVectorB_000000b0.run(0x8L, new Object[] {desc, spec, mode, func});
  }

  @Method(0x8002ff30L)
  public static int TestEvent(final int event) {
    return (int)functionVectorB_000000b0.run(0xbL, new Object[] {event});
  }

  @Method(0x8002ff40L)
  public static boolean EnableEvent(final int event) {
    return (boolean)functionVectorB_000000b0.run(0xcL, new Object[] {event});
  }
}
