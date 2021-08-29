package legend.game;

import legend.core.DebugHelper;
import legend.core.cdrom.CdlCOMMAND;
import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.CdlLOC;
import legend.core.cdrom.CdlMODE;
import legend.core.dma.DmaChannelType;
import legend.core.gpu.RECT;
import legend.core.memory.Method;
import legend.core.memory.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.DMA;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.core.mdec.Mdec.MDEC_REG0;
import static legend.core.mdec.Mdec.MDEC_REG1;
import static legend.game.SMap._800d1cc0;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bcc8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bda4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c150;
import static legend.game.Scus94491BpeSegment_8002.setCdMix;
import static legend.game.Scus94491BpeSegment_8003.ClearImage;
import static legend.game.Scus94491BpeSegment_8003.DsControl;
import static legend.game.Scus94491BpeSegment_8003.FUN_80030a10;
import static legend.game.Scus94491BpeSegment_8003.FUN_80030b20;
import static legend.game.Scus94491BpeSegment_8003.FUN_80030bb0;
import static legend.game.Scus94491BpeSegment_8003.FUN_80030ca0;
import static legend.game.Scus94491BpeSegment_8003.FUN_80030d80;
import static legend.game.Scus94491BpeSegment_8003.FUN_80035b90;
import static legend.game.Scus94491BpeSegment_8003.FUN_80036f20;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.ResetCallback;
import static legend.game.Scus94491BpeSegment_8003.SetDispMask;
import static legend.game.Scus94491BpeSegment_8003.SetDmaInterruptCallback;
import static legend.game.Scus94491BpeSegment_8003.disableCdromDmaCallbacksAndClearDataFifo;
import static legend.game.Scus94491BpeSegment_8003.getCdlPacketIndex800bf700;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004cdbc;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
import static legend.game.Scus94491BpeSegment_8004.setMainVolume;
import static legend.game.Scus94491BpeSegment_8005._80052d6c;
import static legend.game.Scus94491BpeSegment_8005._80052d7c;
import static legend.game.Scus94491BpeSegment_800b.CdlFILE_800bb4c8;
import static legend.game.Scus94491BpeSegment_800b._800bed94;
import static legend.game.Scus94491BpeSegment_800b._800bf55c;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800d._800d1cc4;
import static legend.game.Scus94491BpeSegment_800d._800d1cc6;
import static legend.game.Scus94491BpeSegment_800d._800d1cc8;

public final class SStrm {
  private SStrm() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SStrm.class);

  public static final Value _800fcd24 = MEMORY.ref(4, 0x800fcd24L);
  public static final Value _800fcd28 = MEMORY.ref(4, 0x800fcd28L);
  public static final Value _800fcd2c = MEMORY.ref(1, 0x800fcd2cL);

  public static final Value mdecQuantTableHeader_800fdb3c = MEMORY.ref(4, 0x800fdb3cL);
  public static final Value _800fdb8c = MEMORY.ref(4, 0x800fdb8cL); // Ref to the middle of the quant table...?
  public static final Value mdecScaleTableHeader_800fdbc0 = MEMORY.ref(4, 0x800fdbc0L);

  public static final Value _800fe794 = MEMORY.ref(2, 0x800fe794L);

  public static final Value _800fe7a1 = MEMORY.ref(1, 0x800fe7a1L);

  public static final Value resetMdec_800fe7a4 = MEMORY.ref(4, 0x800fe7a4L);
  public static final Value _800fe7a8 = MEMORY.ref(4, 0x800fe7a8L);

  public static final Value _800fe7b0 = MEMORY.ref(1, 0x800fe7b0L);

  public static final Value _8010f7b0 = MEMORY.ref(2, 0x8010f7b0L);
  public static final Value _8010f7b2 = MEMORY.ref(2, 0x8010f7b2L);
  public static final Value _8010f7b4 = MEMORY.ref(2, 0x8010f7b4L);

  public static final Value _8010f7b8 = MEMORY.ref(2, 0x8010f7b8L);

  public static final Value _8010f7ba = MEMORY.ref(2, 0x8010f7baL);

  public static final RECT _8010f7c0 = MEMORY.ref(8, 0x8010f7c0L, RECT::new);
  public static final Value _8010f7c8 = MEMORY.ref(2, 0x8010f7c8L);
  public static final Value _8010f7ca = MEMORY.ref(2, 0x8010f7caL);
  public static final Value _8010f7cc = MEMORY.ref(2, 0x8010f7ccL);
  public static final Value _8010f7ce = MEMORY.ref(2, 0x8010f7ceL);
  public static final Value linkedListEntry_8010f7d0 = MEMORY.ref(4, 0x8010f7d0L);
  public static final Value mdecInDoubleBufferFrame0_8010f7d4 = MEMORY.ref(4, 0x8010f7d4L);
  public static final Value mdecInDoubleBufferFrame1_8010f7d8 = MEMORY.ref(4, 0x8010f7d8L);
  public static final Value mdecOutDoubleBufferFrame0_8010f7dc = MEMORY.ref(4, 0x8010f7dcL);
  public static final Value mdecOutDoubleBufferFrame1_8010f7e0 = MEMORY.ref(4, 0x8010f7e0L);
  public static final Value mdecInDoubleBufferFrame_8010f7e4 = MEMORY.ref(4, 0x8010f7e4L);
  public static final Value mdecOutDoubleBufferFrame_8010f7e8 = MEMORY.ref(4, 0x8010f7e8L);
  public static final Value doubleBufferFrame_8010f7ec = MEMORY.ref(4, 0x8010f7ecL);
  public static final Value _8010f7f0 = MEMORY.ref(4, 0x8010f7f0L);
  public static final Value _8010f7f4 = MEMORY.ref(4, 0x8010f7f4L);
  public static final Value _8010f7f8 = MEMORY.ref(4, 0x8010f7f8L);

  @Method(0x800fb7ccL)
  public static void FUN_800fb7cc(final long a0, final long a1) {
    linkedListEntry_8010f7d0.setu(addToLinkedListTail(0x1_0000L));
    FUN_800fba6c(a0);

    final long v1 = _80052d7c.offset(4, drgnBinIndex_800bc058.get() * 4).get();
    final long v0 = _80052d6c.offset(4, (drgnBinIndex_800bc058.get() - 1) * 4).get();
    final CdlFILE file = CdlFILE_800bb4c8.get((int)MEMORY.ref(2, v1).offset((a1 - v0) * 8).get());
    FUN_800fc038(file.pos, a0);
    FUN_800fc244(_800fe7b0.getAddress());
    FUN_800fc0b4(_800fe7b0.getAddress());
    resetMdec_800fe7a4.setu(0);
    _800fe7a8.setu(0);
    FUN_8004cdbc(0x1L, 0x1L);
    setCdVolume(0x7fL, 0x7fL);
    setCdMix(0x3fL);
    setMainVolume(0x7fL, 0x7fL);

    if(a1 == 0) {
      FUN_8004cdbc(0x1L, 0x1L);
      setCdVolume(0x7aL, 0x7aL);
      setCdMix(0x3dL);
      setMainVolume(0x7aL, 0x7aL);
    }

    //LAB_800fb8f4
  }

  @Method(0x800fb90cL)
  public static long FUN_800fb90c() {
    if(_8010f7f4.get() != 0) {
      _800fe7a1.setu(0x1L);
      final long v0 = _800fe794.get() * 2;
      final long mult = v0 * 0x5555_5556L;
      _800fe794.setu((mult >>> 32) - (v0 >> 31L));
    }

    //LAB_800fb970
    if(resetMdec_800fe7a4.get() == 0x1L) {
      if(_800fe7a8.get() == 0x2L) {
        return 0x1L;
      }

      reset(true);
      _800fe7a8.addu(0x1L);
    }

    //LAB_800fb9b4
    final long a1;
    if(_8010f7f4.get() == 0) {
      a1 = 0x2L;
    } else {
      a1 = 0x3L;
    }

    //LAB_800fb9e0
    FUN_800fc48c(mdecInDoubleBufferFrame0_8010f7d4.offset(mdecInDoubleBufferFrame_8010f7e4.get() * 4).get(), a1);

    final long mult = _8010f7c0.w.get() * _8010f7c0.h.get();
    startMdecOut(mdecOutDoubleBufferFrame0_8010f7dc.offset(mdecOutDoubleBufferFrame_8010f7e8.get() * 4).get(), (mult & 0xffff_ffffL) / 2);

    if(FUN_800fc0b4(_800fe7b0.getAddress()) >= 0) {
      FUN_800fc144(_800fe7b0.getAddress());
      return 0;
    }

    //LAB_800fba48
    reset(true);

    //LAB_800fba50
    //LAB_800fba54
    return 0x1L;
  }

  @Method(0x800fba6cL)
  public static void FUN_800fba6c(final long a0) {
    mdecInDoubleBufferFrame0_8010f7d4.setu(addToLinkedListTail(0x2_5800L));
    mdecInDoubleBufferFrame1_8010f7d8.setu(addToLinkedListTail(0x2_5800L));
    mdecOutDoubleBufferFrame0_8010f7dc.setu(addToLinkedListTail(0x3000L));
    mdecOutDoubleBufferFrame1_8010f7e0.setu(addToLinkedListTail(0x3000L));
    mdecInDoubleBufferFrame_8010f7e4.setu(doubleBufferFrame_800bb108);
    mdecOutDoubleBufferFrame_8010f7e8.setu(doubleBufferFrame_800bb108);
    doubleBufferFrame_8010f7ec.setu(doubleBufferFrame_800bb108);
    _8010f7f0.setu(0);

    _8010f7f4.setu(MEMORY.ref(4, a0));
    _8010f7f8.setu(MEMORY.ref(4, a0).offset(0x4L));

    _8010f7b0.setu(MEMORY.ref(2, a0).offset(0x8L)); // used in mdecOutDmaCallback
    _8010f7b2.setu(MEMORY.ref(2, a0).offset(0xaL)); // used in mdecOutDmaCallback
    _8010f7b8.setu(MEMORY.ref(2, a0).offset(0x8L));
    _8010f7ba.setu(MEMORY.ref(2, a0).offset(0xaL).get() + 0xf0L);
    _8010f7c0.x.set((short)MEMORY.ref(2, a0).offset(0x8L).get());
    _8010f7c0.y.set((short)MEMORY.ref(2, a0).offset(0xaL).get());
    _8010f7c0.w.set((short)(MEMORY.ref(4, a0).get() == 0 ? 0x10 : 0x18));
    _8010f7c8.setu(MEMORY.ref(2, a0).offset(0x8L));
    _8010f7ca.setu(MEMORY.ref(2, a0).offset(0xaL));
    _8010f7cc.setu(MEMORY.ref(2, a0).offset(0xcL));
    _8010f7ce.setu(MEMORY.ref(2, a0).offset(0xeL));
  }

  private static long fmvTimer;

  @Method(0x800fbbbcL)
  public static void mdecOutDmaCallback() {
    //TODO this time is not a good solution, but it makes the FMV timing issues very slightly better
    while(System.nanoTime() < fmvTimer) {
      DebugHelper.sleep(0);
    }

    fmvTimer = System.nanoTime() + 3_200_000L;

    if(_8010f7f4.get() != 0 && _800bf55c.get() != 0) {
      FUN_80030d80();
      _800bf55c.setu(0);
    }

    //LAB_800fbc04
    //LAB_800fbc08
    final RECT rect = new RECT().set(_8010f7c0);
    final long s0 = mdecOutDoubleBufferFrame_8010f7e8.get();
    _8010f7c0.x.set((short)(_8010f7c0.x.get() + _8010f7c0.w.get()));
    mdecOutDoubleBufferFrame_8010f7e8.setu(s0 == 0 ? 1 : 0);

    if(_8010f7c0.x.get() < _8010f7b0.offset(doubleBufferFrame_8010f7ec.get() * 8).get() + _8010f7b4.offset(doubleBufferFrame_8010f7ec.get() * 8).get()) {
      startMdecOut(mdecOutDoubleBufferFrame0_8010f7dc.offset(mdecOutDoubleBufferFrame_8010f7e8.get() * 4).get(), _8010f7c0.w.get() * _8010f7c0.h.get() / 2);
    } else {
      //LAB_800fbcbc
      _8010f7f0.setu(0x1L);
      doubleBufferFrame_8010f7ec.setu(doubleBufferFrame_800bb108.get() ^ 0x1L);
      _8010f7c0.x.set((short)_8010f7b0.offset(doubleBufferFrame_8010f7ec.get() * 8).get());
      _8010f7c0.y.set((short)_8010f7b2.offset(doubleBufferFrame_8010f7ec.get() * 8).get());
    }

    //LAB_800fbcfc
    LoadImage(rect, mdecOutDoubleBufferFrame0_8010f7dc.offset(s0 * 4).get());
  }

  @Method(0x800fbd2cL)
  public static long FUN_800fbd2c(final long a0) {
    final Value sp18 = new Value(4);
    final Value sp1c = new Value(4);

    //LAB_800fbd4c
    long s0 = 2000L;
    while(FUN_80030ca0(sp18, sp1c)) {
      s0--;
      if(s0 == 0) {
        //TODO
        LOGGER.error("STREAM TIMEOUT");
//        assert false: "Timeout";
        return 0;
      }

      DebugHelper.sleep(1);
    }

    //LAB_800fbd6c
    // This seems to be a trigger to stop at MDEC sector sequence number 0x54c
    if(MEMORY.ref(4, sp1c.get()).offset(0x8L).get() >= MEMORY.ref(4, a0).offset(0x1_1048L).get()) {
      resetMdec_800fe7a4.setu(0x1L);
    }

    //LAB_800fbda0
    if(_800bed94.get() == 0) {
      FUN_8002c150(0);
    }

    //LAB_800fbdbc
    //LAB_800fbdc4
    for(int s1 = 0; s1 < 1000; s1++) {
      final long v1 = _800d1cc0.offset(s1 * 12).getSigned();

      if(v1 >= 0) {
        if(v1 == MEMORY.ref(4, sp1c.get()).offset(0x8L).get()) {
          FUN_8002bcc8(0, _800d1cc4.offset(s1 * 12L).get());
          FUN_8002bda4(0, _800d1cc6.offset(s1 * 12L).get(), _800d1cc8.offset(s1 * 12L).get());
        }
      }

      //LAB_800fbe08
    }

    if(_800fcd24.get() != MEMORY.ref(2, sp1c.get()).offset(0x10L).get() || _800fcd28.get() != MEMORY.ref(2, sp1c.get()).offset(0x12L).get()) {
      //LAB_800fbe4c
      final RECT rect = new RECT();
      if(MEMORY.ref(4, a0).offset(0x1_1044L).get() == 0) { // Must be bpp
        //LAB_800fbe88
        rect.w.set((short)MEMORY.ref(2, a0).offset(0x1_101cL).get());
      } else {
        rect.w.set((short)(MEMORY.ref(2, a0).offset(0x1_101cL).get() * 3 / 2));
      }

      //LAB_800fbe8c
      rect.h.set((short)511);
      ClearImage(rect, (byte)0, (byte)0, (byte)0);

      _800fcd24.setu(MEMORY.ref(2, sp1c.get()).offset(0x10L));
      _800fcd28.setu(MEMORY.ref(2, sp1c.get()).offset(0x12L));
    }

    //LAB_800fbecc
    final long v1;
    if(MEMORY.ref(4, a0).offset(0x1_1044L).get() == 0) { // Must be bpp
      //LAB_800fbf08
      v1 = _800fcd24.get();
    } else {
      v1 = _800fcd24.get() * 3 / 2;
    }

    //LAB_800fbf10
    MEMORY.ref(2, a0).offset(0x1_100cL).setu(v1);
    MEMORY.ref(2, a0).offset(0x1_1004L).setu(v1);
    MEMORY.ref(2, a0).offset(0x1_100eL).setu(_800fcd28);
    MEMORY.ref(2, a0).offset(0x1_1006L).setu(_800fcd28);
    MEMORY.ref(2, a0).offset(0x1_1016L).setu(_800fcd28);

    //LAB_800fbf38
    return sp18.get();
  }

  @Method(0x800fbf50L)
  public static void stopFmv(final long a0) {
    SetDispMask(0);
    setCdVolume(0, 0);
    reset(false);
    registerMdecOutDmaCallback(0);
    disableCdromDmaCallbacksAndClearDataFifo();

    //LAB_800fbf88
    while(!DsControl(CdlCOMMAND.PAUSE_09, 0, 0)) {
      DebugHelper.sleep(1);
    }

    removeFromLinkedList(mdecInDoubleBufferFrame0_8010f7d4.get());
    removeFromLinkedList(mdecInDoubleBufferFrame1_8010f7d8.get());
    removeFromLinkedList(mdecOutDoubleBufferFrame0_8010f7dc.get());
    removeFromLinkedList(mdecOutDoubleBufferFrame1_8010f7e0.get());
    removeFromLinkedList(linkedListEntry_8010f7d0.get());
    FUN_8004cdbc(0x1L, 0x1L);
    setCdVolume(0x7fL, 0x7fL);
    setCdMix(0x3fL);
    setMainVolume(0x7fL, 0x7fL);
  }

  @Method(0x800fc038L)
  public static void FUN_800fc038(final CdlLOC pos, final long a1) {
    reset(false);
    registerMdecOutDmaCallback(getMethodAddress(SStrm.class, "mdecOutDmaCallback"));
    FUN_80030a10(linkedListEntry_8010f7d0.get(), 0x20L); //TODO 0x20 might be the frame header size
    FUN_80030b20(MEMORY.ref(4, a1).get(), 0x2L, 0xffff_ffffL, 0, 0);
    FUN_800fc1ec(pos);
  }

  @Method(0x800fc0b4L)
  public static long FUN_800fc0b4(final long a0) {
    //LAB_800fc0d0
    for(int s1 = 0; s1 < 2000; s1++) {
      final long s0 = FUN_800fbd2c(a0);

      if(s0 != 0) {
        final long v0 = MEMORY.ref(4, a0).offset(0x1_1034L).get() < 0x1L ? 1 : 0;
        MEMORY.ref(4, a0).offset(0x1_1034L).set(v0);
        FUN_800fc9c4(s0, MEMORY.ref(4, a0).offset(0x1_1024L).offset(v0 * 4).get());
        FUN_80030bb0(s0);
        return 0;
      }

      //LAB_800fc120
    }

    //LAB_800fc12c
    return -0x1L;
  }

  @Method(0x800fc144L)
  public static void FUN_800fc144(final long a0) {
    if(MEMORY.ref(4, a0).offset(0x1_1040L).get() == 0) {
      final long a2 = doubleBufferFrame_800bb108.get() ^ 0x1L;

      //LAB_800fc184
      long sp00 = 0x80_0000L;
      do {
        sp00--;
        if(sp00 == 0) {
          MEMORY.ref(4, a0).offset(0x1_103cL).setu(a2);
          MEMORY.ref(4, a0).offset(0x1_1040L).setu(0x1L);
          MEMORY.ref(2, a0).offset(0x1_1010L).setu(MEMORY.ref(2, a0).offset(0x1_1000L).offset(a2 * 8));
          MEMORY.ref(2, a0).offset(0x1_1012L).setu(MEMORY.ref(2, a0).offset(0x1_1002L).offset(a2 * 8));
        }

        //LAB_800fc1cc
      } while(MEMORY.ref(4, a0).offset(0x1_1040L).get() == 0);
    }

    //LAB_800fc1dc
    MEMORY.ref(4, a0).offset(0x1_1040L).setu(0);
  }

  @Method(0x800fc1ecL)
  public static void FUN_800fc1ec(final CdlLOC pos) {
    while(getCdlPacketIndex800bf700() != 0) {
      DebugHelper.sleep(1);
    }

    while(FUN_80036f20() != 1) {
      DebugHelper.sleep(1);
    }

    final CdlMODE mode = new CdlMODE().doubleSpeed().sendAdpcmToSpu().readEntireSector().unknownExtraBit();
    while(FUN_80035b90(pos, mode) == 0) {
      DebugHelper.sleep(1);
    }
  }

  @Method(0x800fc244L)
  public static void FUN_800fc244(long a0) {
    long a1;
    long a2;
    long a3;
    long v0;
    long v1;

    v1 = 0;
    a3 = _800fcd2c.getAddress();
    a2 = a0;

    //LAB_800fc264
    do {
      v0 = MEMORY.ref(1, a3).get();
      a1 = v0 & 0xffL;
      a3++;
      if(a1 < 0xf0L) {
        if(v1 != 0) {
          if(a1 < 0) {
            a1 = 0x4L;
            continue;
          }

          //LAB_800fc28c
          do {
            v0 = a2 - v1;
            v0 = MEMORY.ref(1, v0).get();
            a1--;
            MEMORY.ref(1, a2).setu(v0);
            a2++;
          } while(a1 >= 0);

          a1 = 0x4L;
          continue;
        }

        //LAB_800fc2ac
        if(a1 < 0) {
          a1 = 0x4L;
          continue;
        }

        //LAB_800fc2b4
        do {
          v0 = MEMORY.ref(1, a3).get();
          a3++;
          a1--;
          MEMORY.ref(1, a2).setu(v0);
          a2++;
        } while(a1 >= 0);

        a1 = 0x4L;
        continue;
      }

      //LAB_800fc2d4
      v1 = 0;
      if(a1 == 0xf0L) {
        a1 = 0x4L;
        continue;
      }

      v1 = MEMORY.ref(1, a3).get();
      a3++;
      v0 = a1 << 0x8L;
      v0 |= v1;
      v1 = v0 - 0xf0ffL;

      //LAB_800fc2f0
      a1 = 0x4L;
    } while(v1 != 0xf00L);

    a0 += 0x8L;

    //LAB_800fc300
    do {
      v0 = MEMORY.ref(2, a0).get();
      v1 = MEMORY.ref(2, a0).offset(-0x8L).get();
      a1++;
      v0 ^= v1;
      MEMORY.ref(2, a0).setu(v0);
      a0 += 0x2L;
    } while(a1 < 0x8800L);
  }

  @Method(0x800fc334L)
  public static void reset(final boolean softReset) {
    if(!softReset) {
      ResetCallback();
    }

    //LAB_800fc350
    mdecReset(softReset);
  }

  @Method(0x800fc48cL)
  public static void FUN_800fc48c(final long a0, final long a1) {
    long v0;

    if((a1 & 0x1L) == 0) {
      //LAB_800fc4b0
      v0 = MEMORY.ref(4, a0).get() | 0x800_0000L;
    } else {
      v0 = MEMORY.ref(4, a0).get() & 0xf7ff_ffffL;
    }

    //LAB_800fc4bc
    MEMORY.ref(4, a0).setu(v0);

    if((a1 & 0x2L) == 0) {
      //LAB_800fc4d8
      v0 = MEMORY.ref(4, a0).get() & 0xfdff_ffffL;
    } else {
      v0 = MEMORY.ref(4, a0).get() | 0x200_0000L;
    }

    //LAB_800fc4e8
    MEMORY.ref(4, a0).setu(v0);
    startMdecIn(a0, MEMORY.ref(2, a0).get());
  }

  @Method(0x800fc508L)
  public static void startMdecOut(final long address, final long size) {
    mdecOutSync();

    DMA.mdecIn.enable();
    DMA.mdecOut.enable();

    DMA.mdecOut.CHCR.setu(0);
    DMA.mdecOut.MADR.setu(address); // Set dest address
    DMA.mdecOut.BCR.setu(size / 0x20 << 16 | 0x20); // Number of blocks << 16 | block size
    DMA.mdecOut.CHCR.setu(0x100_0200L);  // Start/busy; sync blocks to DMA requests; to main RAM
  }

  @Method(0x800fc5d0L)
  public static void registerMdecOutDmaCallback(final long callback) {
    SetDmaInterruptCallback(DmaChannelType.MDEC_OUT, callback);
  }

  @Method(0x800fc5f4L)
  public static void mdecReset(final boolean softReset) {
    MDEC_REG1.setu(0x8000_0000L); // Reset MDEC
    DMA.mdecIn.CHCR.setu(0);
    DMA.mdecOut.CHCR.setu(0);
    MDEC_REG1.setu(0x6000_0000L); // Enable data-in request, enable data-out request

    if(!softReset) {
      startMdecIn(mdecQuantTableHeader_800fdb3c.getAddress(), 0x20L);
      startMdecIn(mdecScaleTableHeader_800fdbc0.getAddress(), 0x20L);
    }
  }

  @Method(0x800fc6e4L)
  public static void startMdecIn(final long address, final long size) {
    mdecInSync();

    DMA.mdecIn.enable();
    DMA.mdecOut.enable();

    DMA.mdecIn.MADR.setu(address + 0x4L); // Set source address
    assert size / 0x20 > 0 : "Size was " + size;
    DMA.mdecIn.BCR.setu(size / 0x20 << 16 | 0x20); // Number of blocks << 16 | block size
    MDEC_REG0.setu(MEMORY.ref(4, address)); // Send command
    DMA.mdecIn.CHCR.setu(0x100_0201L); // Start/busy; sync blocks to DMA requests; from main RAM
  }

  @Method(0x800fc800L)
  public static long mdecInSync() {
    long timeoutCounter = 0x10_0000L;

    //LAB_800fc830
    while(MDEC_REG1.get(0b10_0000_0000_0000_0000_0000_0000_0000L) != 0) { // Command busy
      if(timeoutCounter-- == 0) {
        mdecTimeout("MDEC_in_sync");
        return -0x1L;
      }

      DebugHelper.sleep(1);
    }

    //LAB_800fc884
    return 0;
  }

  @Method(0x800fc894L)
  public static long mdecOutSync() {
    long timeoutCounter = 0x10_0000L;

    //LAB_800fc8c4
    while(DMA.mdecOut.channelControl.isBusy()) {
      if(timeoutCounter-- == 0) {
        mdecTimeout("MDEC_out_sync");
        return -0x1L;
      }

      DebugHelper.sleep(1);
    }

    //LAB_800fc918
    return 0;
  }

  @Method(0x800fc940L)
  public static void mdecTimeout(final String functionName) {
    LOGGER.error("%s timeout:", functionName);
    MDEC_REG1.setu(0x8000_0000L); // Reset MDEC
    DMA.mdecIn.CHCR.setu(0);
    DMA.mdecOut.CHCR.setu(0);
    MDEC_REG1.setu(0x6000_0000L); // Enable data-in request, enable data-out request
  }

  @Method(0x800fc9c4L)
  public static void FUN_800fc9c4(long a0, long a1) {
    long s0;

    final long s2 = a1;
    MEMORY.ref(2, s2).setu(MEMORY.ref(2, a0));
    MEMORY.ref(2, s2).offset(0x2L).setu(MEMORY.ref(2, a0).offset(0x2L));
    long v1 = (int)(MEMORY.ref(2, a0).offset(0x4L).get() + 0xfL) / 16;
    long v0 = (int)(MEMORY.ref(2, a0).offset(0x6L).get() + 0xfL) / 16;
    v1 = v1 * v0 & 0xffff_ffffL;
    s0 = a0 + 0xaL;
    long s1 = v1 * 6;
    final long[] arr = new long[(int)(v1 * 12 + 14 & ~0b111L)];
    FUN_800fcc50(arr, s0, v1 * 12);
    s0 += MEMORY.ref(2, a0).offset(0x8L).get();
    a1 = 0x10L;
    long t1 = 0;
    long t3 = s1;
    long t0 = s2 + 0x4L;
    s1--;
    long a2 = MEMORY.ref(2, s0).get() << 16 | MEMORY.ref(2, s0).offset(0x2L).get();
    s0 += 0x4L;

    //LAB_800fca8c
    while(s1 != -0x1L) {
      MEMORY.ref(2, t0).setu(arr[(int)t1] << 8 | arr[(int)t3]);
      t3++;
      t1++;
      t0 += 0x2L;

      //LAB_800fcaac
      while(true) {
        CPU.MTC2(a2, 0x1eL);
        a0 = a2 >>> 0x1dL;
        long a3;
        if((int)a2 < 0) {
          a3 = 0x3L;
          if(a0 < 0x6L) {
            MEMORY.ref(2, t0).setu(0xfe00L);
            t0 += 0x2L;
            a1 -= 0x2L;
            a2 = a2 << 0x2L & 0xffff_ffffL;
            if((int)a1 <= 0) {
              a1 += 0x10L;
              a2 |= MEMORY.ref(2, s0).get() << 0x10L - a1;
              s0 += 0x2L;
            }

            break;
          }

          //LAB_800fcaf8
          if(a0 == 0x6L) {
            v1 = 0x1L;
          } else {
            v1 = 0x3ffL;
          }

          //LAB_800fcb08
          MEMORY.ref(2, t0).setu(v1);
          t0 += 0x2L;
        } else {
          //LAB_800fcb14
          a0 = CPU.MFC2(0x1fL);
          a3 = a0 << 0x8L;
          boolean takeBranch = false;
          if(a2 >>> 0x17L == 0) {
            a1 -= 0x9L;
            a2 = a2 << 0x9L & 0xffff_ffffL;
            if((int)a1 <= 0) {
              a1 += 0x10L;
              a2 |= MEMORY.ref(2, s0).get() << 0x10L - a1;
              s0 += 0x2L;
            }

            //LAB_800fcb48
            a0 -= 0x9L;
            takeBranch = true;
          }

          //LAB_800fcb50
          if(a0 == 0x5L) {
            a1 -= 0x6L;
            a2 = a2 << 0x6L & 0xffff_ffffL;
            if((int)a1 <= 0) {
              a1 += 0x10L;
              a2 |= MEMORY.ref(2, s0).get() << 0x10L - a1;
              s0 += 0x2L;
            }

            //LAB_800fcb80
            MEMORY.ref(2, t0).setu(a2 >>> 0x10L);
            t0 += 0x2L;
            a3 = 0x10L;
          } else {
            takeBranch = true;
          }

          if(takeBranch) {
            //LAB_800fcb94
            v1 = _800fdb8c.offset(a3 + (a2 >>> 0x17L - a0 & 0xfcL)).get();
            MEMORY.ref(2, t0).setu(v1);
            t0 += 0x2L;
            a3 = v1 >>> 0x1aL;
          }
        }

        //LAB_800fcbc0
        a1 -= a3;
        a2 = a2 << a3 & 0xffff_ffffL;
        if((int)a1 <= 0) {
          a1 += 0x10L;
          a2 |= MEMORY.ref(2, s0).get() << 0x10L - a1;
          s0 += 0x2L;
        }
      }

      //LAB_800fcbe8
      s1--;
    }

    //LAB_800fcbf8
    //LAB_800fcc18
    while(t0 < s2 + (MEMORY.ref(2, s2).get() + 1) * 0x4L) {
      MEMORY.ref(2, t0).setu(0xfe00L);
      t0 += 0x2L;
    }

    //LAB_800fcc2c
  }

  /**
   * Returns an array rather than using a0 as an output
   *
   * I have verified that at least the first invocation of this method returns the correct array
   *
   * TODO verify
   */
  @Method(0x800fcc50L)
  public static long[] FUN_800fcc50(final long[] out, long a1, long a2) {
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long v0;
    long v1;

    if(a2 == 0) {
      return out;
    }

    t1 = 0;

    //LAB_800fcc5c
    do {
      t3 = MEMORY.ref(1, a1).get();
      a1++;
      t2 = 0x7L;

      //LAB_800fcc68
      do {
        if(a2 == 0) {
          return out;
        }

        v1 = MEMORY.ref(1, a1).get();
        a1++;
        if((t3 & 0x1L) == 0) {
          out[(int)t1] = v1;
          t1++;
          a2--;
        } else {
          //LAB_800fcc90
          t0 = v1 & 0xffL;
          a3 = MEMORY.ref(1, a1).get();
          v0 = a3 & 0x80L;
          a1++;
          if(v0 == 0) {
            a3 &= 0x7fL;
          } else {
            //LAB_800fccb0
            v1 = MEMORY.ref(1, a1).get();
            a1++;
            v0 = a3 & 0x7fL;
            v0 <<= 0x8L;
            a3 = v0 | v1;
          }

          //LAB_800fccc4
          t0 += 0x3L;
          a3++;
          a3 = t1 - a3;
          if(a2 < t0) {
            t0 = a2;
          }

          //LAB_800fccdc
          a2 -= t0;
          v1 = t0;
          t0--;

          //LAB_800fccec
          while(v1 != 0) {
            v1 = t0;
            t0--;
            out[(int)t1] = out[(int)a3];
            t1++;
            a3++;
          }
        }

        //LAB_800fcd08
        t2--;
        t3 >>= 0x1L;
      } while(t2 != -0x1L);
    } while(a2 != 0);

    //LAB_800fcd1c
    return out;
  }
}
