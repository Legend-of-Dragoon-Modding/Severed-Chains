package legend.game;

import legend.core.InterruptType;
import legend.core.kernel.PriorityChainEntry;
import legend.core.memory.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.Hardware.CPU;
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
import static legend.game.Scus94491BpeSegment.functionVectorA_000000a0;
import static legend.game.Scus94491BpeSegment.functionVectorB_000000b0;
import static legend.game.Scus94491BpeSegment.functionVectorC_000000c0;
import static legend.game.Scus94491BpeSegment_8003.CdMix;
import static legend.game.Scus94491BpeSegment_8003.SetVsyncInterruptCallback;
import static legend.game.Scus94491BpeSegment_8003.VSync;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042b60;
import static legend.game.Scus94491BpeSegment_8004.FUN_80043230;
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
import static legend.game.Scus94491BpeSegment_800b._800bed60;
import static legend.game.Scus94491BpeSegment_800b._800bed62;
import static legend.game.Scus94491BpeSegment_800b._800bed64;
import static legend.game.Scus94491BpeSegment_800b._800bed66;
import static legend.game.Scus94491BpeSegment_800b._800bed6c;
import static legend.game.Scus94491BpeSegment_800b._800bed6e;
import static legend.game.Scus94491BpeSegment_800b._800beda8;
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
import static legend.game.Scus94491BpeSegment_800b._800bf0a8;
import static legend.game.Scus94491BpeSegment_800b._800bf0ac;
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
import static legend.game.Scus94491BpeSegment_800e.main;
import static legend.game.Scus94491BpeSegment_800e.ramSize_800e6f04;
import static legend.game.Scus94491BpeSegment_800e.stackSize_800e6f08;

public final class Scus94491BpeSegment_8002 {
  private Scus94491BpeSegment_8002() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8002.class);

  private static final Object[] EMPTY_OBJ_ARRAY = {};

  @Method(0x80021facL)
  public static void setScreenOffset(final int x, final int y) {
    // cop2r56, OFX - Screen offset X
    CPU.CTC2(x << 0x10, 0x18);
    // cop2r57, OFY - Screen offset Y
    CPU.CTC2(y << 0x10, 0x19);
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
  public static void FUN_8002ae0c(long a0) {
    assert false : "Unimplemented";
    //TODO
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
