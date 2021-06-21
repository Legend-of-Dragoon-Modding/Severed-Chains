package legend.game;

import legend.core.DebugHelper;
import legend.core.kernel.PriorityChainEntry;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.UnsignedIntRef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.GATE;
import static legend.core.Hardware.MEMORY;
import static legend.core.InterruptController.I_MASK;
import static legend.core.InterruptController.I_STAT;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.core.Timers.TMR_SYSCLOCK_MAX;
import static legend.core.Timers.TMR_SYSCLOCK_MODE;
import static legend.core.Timers.TMR_SYSCLOCK_VAL;
import static legend.core.input.MemoryCard.JOY_MCD_BAUD;
import static legend.core.input.MemoryCard.JOY_MCD_CTRL;
import static legend.core.input.MemoryCard.JOY_MCD_DATA;
import static legend.core.input.MemoryCard.JOY_MCD_MODE;
import static legend.core.input.MemoryCard.JOY_MCD_STAT;
import static legend.core.kernel.Bios.EnterCriticalSection;
import static legend.core.kernel.Bios.ExitCriticalSection;
import static legend.core.spu.Spu.CD_VOL_L;
import static legend.core.spu.Spu.CD_VOL_R;
import static legend.core.spu.Spu.SPU_CTRL_REG_CPUCNT;
import static legend.game.Scus94491BpeSegment_8002.SysDeqIntRP;
import static legend.game.Scus94491BpeSegment_8002.SysEnqIntRP;
import static legend.game.Scus94491BpeSegment_8003.ChangeClearRCnt;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8005._8005952c;
import static legend.game.Scus94491BpeSegment_8005._8005953c;
import static legend.game.Scus94491BpeSegment_8005._8005954c;
import static legend.game.Scus94491BpeSegment_8005._80059550;
import static legend.game.Scus94491BpeSegment_8005._80059554;
import static legend.game.Scus94491BpeSegment_8005._8005955c;
import static legend.game.Scus94491BpeSegment_8005._80059560;
import static legend.game.Scus94491BpeSegment_8005._80059564;
import static legend.game.Scus94491BpeSegment_8005._80059570;
import static legend.game.Scus94491BpeSegment_8005._800595a0;
import static legend.game.Scus94491BpeSegment_8005._800595d4;
import static legend.game.Scus94491BpeSegment_8005._800595d8;
import static legend.game.Scus94491BpeSegment_8005._800595dc;
import static legend.game.Scus94491BpeSegment_8005._800595e0;
import static legend.game.Scus94491BpeSegment_8005._800595e8;
import static legend.game.Scus94491BpeSegment_8005._800595ec;
import static legend.game.Scus94491BpeSegment_8005._800595f0;
import static legend.game.Scus94491BpeSegment_8005._800595f4;
import static legend.game.Scus94491BpeSegment_8005._800595f8;
import static legend.game.Scus94491BpeSegment_8005._800595fc;
import static legend.game.Scus94491BpeSegment_8005._80059608;
import static legend.game.Scus94491BpeSegment_8005._8005960c;
import static legend.game.Scus94491BpeSegment_8005._80059614;
import static legend.game.Scus94491BpeSegment_8005._80059618;
import static legend.game.Scus94491BpeSegment_8005._8005961c;
import static legend.game.Scus94491BpeSegment_8005._80059620;
import static legend.game.Scus94491BpeSegment_8005._80059624;
import static legend.game.Scus94491BpeSegment_8005._80059628;
import static legend.game.Scus94491BpeSegment_8005._8005962c;
import static legend.game.Scus94491BpeSegment_8005._80059634;
import static legend.game.Scus94491BpeSegment_8005._80059644;
import static legend.game.Scus94491BpeSegment_8005._80059654;
import static legend.game.Scus94491BpeSegment_8005._8005965c;
import static legend.game.Scus94491BpeSegment_800c._800c3658;
import static legend.game.Scus94491BpeSegment_800c._800c37a4;
import static legend.game.Scus94491BpeSegment_800c._800c37b8;
import static legend.game.Scus94491BpeSegment_800c._800c37f4;
import static legend.game.Scus94491BpeSegment_800c._800c37f8;
import static legend.game.Scus94491BpeSegment_800c._800c38a8;
import static legend.game.Scus94491BpeSegment_800c._800c38e4;
import static legend.game.Scus94491BpeSegment_800c._800c38e8;
import static legend.game.Scus94491BpeSegment_800c._800c3998;
import static legend.game.Scus94491BpeSegment_800c._800c39bb;
import static legend.game.Scus94491BpeSegment_800c._800c39e0;
import static legend.game.Scus94491BpeSegment_800c._800c3a03;
import static legend.game.Scus94491BpeSegment_800c._800c3a2c;
import static legend.game.Scus94491BpeSegment_800c._800c3a30;
import static legend.game.Scus94491BpeSegment_800c._800c3a34;
import static legend.game.Scus94491BpeSegment_800c._800c3a38;
import static legend.game.Scus94491BpeSegment_800c._800c3a3c;
import static legend.game.Scus94491BpeSegment_800c.spuMono_800c6666;

public final class Scus94491BpeSegment_8004 {
  private Scus94491BpeSegment_8004() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8004.class);

  public static final Value _8004dd24 = MEMORY.ref(4, 0x8004dd24L);

  public static final Value _8004e31c = MEMORY.ref(4, 0x8004e31cL);

  public static final ArrayRef<UnsignedIntRef> _8004e41c = MEMORY.ref(0x80, 0x8004e41cL, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));

  public static final ArrayRef<UnsignedIntRef> _8004e59c = MEMORY.ref(0x80, 0x8004e59cL, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));

  public static final Value _8004e61c = MEMORY.ref(4, 0x8004e61cL);

  public static final Value _8004e69c = MEMORY.ref(4, 0x8004e69cL);

  public static final ArrayRef<UnsignedIntRef> _8004e71c = MEMORY.ref(0x80, 0x8004e71cL, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));

  public static final Value _8004e91c = MEMORY.ref(4, 0x8004e91cL);

  public static final Value _8004e990 = MEMORY.ref(4, 0x8004e990L);

  public static final ArrayRef<UnsignedIntRef> _8004ea1c = MEMORY.ref(0x80, 0x8004ea1cL, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> _8004ea9c = MEMORY.ref(0x38, 0x8004ea9cL, ArrayRef.of(UnsignedIntRef.class, 14, 4, UnsignedIntRef::new));

  public static final Value _8004eb1c = MEMORY.ref(4, 0x8004eb1cL);

  public static final Value _8004eb9c = MEMORY.ref(4, 0x8004eb9cL);

  public static final Value _8004ec1c = MEMORY.ref(4, 0x8004ec1cL);

  public static final Value _8004ec9c = MEMORY.ref(4, 0x8004ec9cL);

  public static final ArrayRef<UnsignedIntRef> _8004ed1c = MEMORY.ref(0x80, 0x8004ed1cL, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));

  public static final Value _8004ed9c = MEMORY.ref(4, 0x8004ed9cL);

  public static final Value _8004ee1c = MEMORY.ref(4, 0x8004ee1cL);

  public static final Value _8004ee9c = MEMORY.ref(4, 0x8004ee9cL);

  public static final Value _8004ef1c = MEMORY.ref(4, 0x8004ef1cL);

  public static final Value _8004ef9c = MEMORY.ref(4, 0x8004ef9cL);

  public static final Value _8004f01c = MEMORY.ref(1, 0x8004f01cL);

  public static final Value _8004f09c = MEMORY.ref(1, 0x8004f09cL);

  /**
   * I think this is patch_missing_cop0r13_in_exception_handler (no$)
   *
   * Shouldn't be necessary since we aren't actually emulating the CPU
   */
  @Method(0x80040d10L)
  public static void patchC0TableAgain() {
    LOGGER.warn("Skipping bios patch");
  }

  @Method(0x80041be0L)
  public static int FUN_80041be0() {
    if(I_MASK.get(0x1L) == 0) {
      return 0;
    }

    if(I_STAT.get(0x1L) != 0) {
      return 1;
    }

    //LAB_80041c18
    return 0;
  }

  @Method(0x80041c60L)
  public static void FUN_80041c60(final int a0) {
    if(_80059550.get() != 0) {
      if(_800c37a4.get() != 0x11L || _8005955c.addu(0x1L).get() >= 0x3L) {
        //LAB_80041d18
        _8005954c.setu(0x11L);
        _80059550.setu(0);
        _8005955c.setu(0);
        I_STAT.setu(0xffff_ff7fL);
        I_MASK.and(0xffff_ff7fL);
        JOY_MCD_CTRL.setu(0);
        SysDeqIntRP(0x1, _8005953c);
        _800595a0.setu(0);
        JOY_MCD_CTRL.setu(0x40L);
        JOY_MCD_BAUD.setu(0x88L);
        JOY_MCD_MODE.setu(0xdL);
        JOY_MCD_CTRL.setu(0);
        return;
      }

      _8005954c.setu(_800c37a4);
      FUN_80041e08(_80059554.get());
      _8005954c.setu(0x2L);
      JOY_MCD_CTRL.setu(0);
      I_STAT.setu(0xffff_ff7fL);
      I_MASK.and(0xffff_ff7fL);
      _80059550.setu(0);
      SysDeqIntRP(0x1, _8005953c);
    }

    //LAB_80041d98
    if(_8005954c.get(0x1L) != 0) {
      return;
    }

    SysDeqIntRP(0x1, _8005953c);
    SysEnqIntRP(0x1, _8005953c);
    FUN_800421a0();
    I_STAT.setu(0xffff_ff7fL);
    I_MASK.oru(0x80L);

    //LAB_80041df8
  }

  @Method(0x80041e08L)
  public static boolean FUN_80041e08(final long a0) {
    if(_8005954c.get(0x1L) != 0) {
      return false;
    }

    _80059550.setu(0);
    _80059554.setu(a0);

    _80059560.setu(0);
    _80059564.setu(0);

    _800c3658.setu(0);

    return true;
  }

  @Method(0x80042090L)
  public static void FUN_80042090() {
    EnterCriticalSection();
    SysDeqIntRP(0x2, _8005952c);
    FUN_80042140(0x2, _8005952c);

    I_STAT.setu(0xffff_fffe);
    I_MASK.oru(0x1L);

    ChangeClearRCnt(0x3, false);
    ExitCriticalSection();
  }

  @Method(0x80042140L)
  public static void FUN_80042140(final int priority, final PriorityChainEntry struct) {
    GATE.acquire();

    PriorityChainEntry entry = _80059570.deref().deref().get(priority).deref();

    if(!entry.next.isNull()) {
      //LAB_80042170
      //LAB_80042178
      while(!entry.next.isNull()) {
        entry = entry.next.deref();
      }
    }

    entry.next.set(struct);

    //LAB_8004218c
    struct.next.clear();

    GATE.release();
  }

  @Method(0x800421a0L)
  public static void FUN_800421a0() {
    assert false;
    //TODO
  }

  @Method(0x80042b60L)
  public static void FUN_80042b60(final long a0, final long a1, final long a2) {
    final long ret = _800595e8.deref().run(a0);
    MEMORY.ref(4, ret).offset(0x28L).setu(a1);
    MEMORY.ref(1, ret).offset(0x34L).setu(a2);
  }

  @Method(0x80043120L)
  public static void FUN_80043120() {
    _8005960c.setu(0);

    EnterCriticalSection();

    SysDeqIntRP(2, _80059634);
    SysEnqIntRP(2, _80059634);

    I_STAT.setu(-0x2L);
    I_MASK.oru(0x1L);

    ChangeClearRCnt(3, false);

    ExitCriticalSection();

    _800595d8.deref().run(_80059608.get());
    _800595d8.deref().run(_80059608.get() + 0xf0L);

    _800c3a38.setu(0);
    _800c3a3c.setu(0);
    _8005960c.setu(0x1L);
  }

  @Method(0x80043230L)
  public static void FUN_80043230(final long a0, final long a1) {
    _8005960c.setu(0);
    _80059620.setu(0);

    FUN_800432e0();

    _80059608.deref(4).offset(0x30L).setu(a0);
    _80059608.deref(4).offset(0x120L).setu(a1);

    long a2 = _80059608.get();
    long a4 = a2 + 0x30L;
    for(int a3 = 0; a3 < 2; a3++) {
      //LAB_8004327c
      MEMORY.ref(4, a4).offset(-0x24L).setu(0);
      MEMORY.ref(4, a4).offset(-0x20L).setu(a2);
      MEMORY.ref(4, a4).deref(1).setu(0xffL);
      MEMORY.ref(4, a4).deref(1).offset(0x1L).setu(0);

      long a5 = a2 + 0x5dL;
      for(int v1 = 0; v1 < 5; v1++) {
        //LAB_8004329c
        MEMORY.ref(1, a5).setu(0xffL);
        a5++;
      }

      a4 += 0xf0L;
      a2 += 0xf0L;
    }

    _8005960c.set(0x1L);
  }

  @Method(0x800432e0L)
  public static void FUN_800432e0() {
    bzero(_800c37b8.getAddress(), 480);

    _800595d4.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80043438", long.class), FunctionRef::new));
    _800595d8.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_800433d0", long.class), ConsumerRef::new));
    _800595dc.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_8004353c", long.class), FunctionRef::new));
    _800595e0.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_800435f8", long.class), FunctionRef::new));
    _800595e8.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80043874", long.class), FunctionRef::new));
    _800595ec.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80043894", long.class), FunctionRef::new));
    _800595f0.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80043cf0", long.class), FunctionRef::new));
    _800595f4.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_800439a4", long.class), FunctionRef::new));
    _800595f8.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_8004352c", long.class), FunctionRef::new));

    _80059608.setu(_800c37b8.getAddress());
    _800c37f4.setu(_800c3998.getAddress());
    _800c37f8.setu(_800c39e0.getAddress());
    _800c38e4.setu(_800c39bb.getAddress());
    _800c38e8.setu(_800c3a03.getAddress());
  }

  @Method(0x800433d0L)
  public static void FUN_800433d0(final long a0) {
    if(MEMORY.ref(1, a0).offset(0x49L).get() == 0) {
      return;
    }

    MEMORY.ref(1, a0).offset(0x49L).setu(0);
    MEMORY.ref(1, a0).offset(0x46L).setu(0);
    MEMORY.ref(2, a0).offset(0xe6L).setu(0);
    MEMORY.ref(4, a0).offset(0x14L).setu(0);
    MEMORY.ref(4, a0).offset(0x18L).setu(0);
    MEMORY.ref(1, a0).offset(0xe3L).setu(0);
    MEMORY.ref(1, a0).offset(0xe4L).setu(0);
    MEMORY.ref(2, a0).offset(0xe6L).setu(0);
    MEMORY.ref(1, a0).offset(0xe9L).setu(0);
    MEMORY.ref(1, a0).offset(0xeaL).setu(0);
    MEMORY.ref(4, a0).offset(0x00L).setu(0);
    MEMORY.ref(4, a0).offset(0x04L).setu(0);
    MEMORY.ref(4, a0).offset(0x08L).setu(0);

    //LAB_80043420
    for(int i = 0; i < 6; i++) {
      MEMORY.ref(1, a0).offset(0x5d).offset(i).setu(0xffL);
    }
  }

  @Method(0x80043438L)
  public static long FUN_80043438(long a0) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x8004352cL)
  public static long FUN_8004352c(long a0) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x8004353cL)
  public static long FUN_8004353c(long a0) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x800435f8L)
  public static long FUN_800435f8(long a0) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x80043874L)
  public static long FUN_80043874(final long a0) {
    if((a0 & 0xf0) == 0) {
      return _800c38a8.getAddress();
    }

    return _800c37b8.getAddress();
  }

  @Method(0x80043894L)
  public static long FUN_80043894(final long a0) {
    if(MEMORY.ref(4, a0).offset(0x3cL).deref(1).get() == 0xf3L) {
      if(MEMORY.ref(1, a0).offset(0xe8L).get() == 0 || MEMORY.ref(1, a0).offset(0x46L).get() == 0xffL) {
        FUN_80045144(a0, 0);
        return 0;
      }

      if(MEMORY.ref(1, a0).offset(0x49L).get() == 0x2L) {
        _800595d8.deref().run(a0);
      }
    }

    //LAB_80043900
    final long v1 = MEMORY.ref(1, a0).offset(0x46L).get();
    if(v1 == 0 || v1 == 0xffL) {
      return 0;
    }

    if(v1 == 0x1L) {
      //LAB_80043940
      FUN_80045144(a0, 0x1L);
      return 0;
    }

    if(v1 == 0xfeL) {
      //LAB_80043954
      //LAB_80043958
      FUN_80045144(a0, 0);
      return 0;
    }

    //LAB_80043968
    final long v0 = MEMORY.ref(4, a0).offset(0x14L).get();
    if(v0 == 0) {
      //LAB_80043988
      FUN_800448a4(a0);
    } else {
      MEMORY.ref(4, v0).call(a0);
    }

    //LAB_80043994
    return 0;
  }

  @Method(0x800439a4L)
  public static long FUN_800439a4(long a0) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x80043cf0L)
  public static long FUN_80043cf0(final long a0) {
    if(MEMORY.ref(4, a0).offset(0xe6L).get() != 0 && MEMORY.ref(4, a0).offset(0x46L).get() == 0xffL) {
      return 0;
    }

    return 0x1L;
  }

  @Method(0x80043d20L)
  public static int FUN_80043d20() {
    if(I_MASK.get(0x1L) == 0 || I_STAT.get(0x1L) == 0) {
      return 0;
    }

    if(!_800595fc.isNull()) {
      _800595fc.deref().run();
    }

    return 1;
  }

  @Method(0x80043d88L)
  public static void FUN_80043d88(final int a0) {
    if(JOY_MCD_CTRL.get(0x2L) != 0) {
      JOY_MCD_CTRL.setu(0);
      return;
    }

    //LAB_80043db4
    _80059644.setu(0x1L);

    if(_80059624.get() != 0) {
      if(_800c3a38.get() < 0x96L) {
        _800c3a38.addu(0x1L);
      }
    }

    //LAB_80043dec
    if(_80059628.get() == 0) {
      if(_800c3a3c.get() < 0x96L) {
        _800c3a3c.addu(0x1L);
      }
    }

    //LAB_80043e20
    if(_8005960c.get() == 0 || _80059628.get() < _80059624.get()) {
      return;
    }

    _80059618.setu(0);
    _80059614.setu(_80059624);
    if(FUN_80043f18(_80059608.get() + _80059624.get() * 240) == 0) {
      _800595d4.deref().run(0xffffL);
    }

    //LAB_80043e98
    _8005961c.setu(0);

    //LAB_80043ebc
    while(_80059628.get() >= _80059614.get()) {
      FUN_8004424c(_80059608.get() + _80059614.get() * 240);
    }

    //LAB_80043ef8
    JOY_MCD_BAUD.setu(0x88L);

    //LAB_80043f08
  }

  @Method(0x80043f18L)
  public static long FUN_80043f18(final long a0) {
    JOY_MCD_CTRL.setu(0x40L);
    JOY_MCD_CTRL.setu(0);
    JOY_MCD_MODE.setu(0xdL);
    JOY_MCD_BAUD.setu(0x88L);

    if(MEMORY.ref(1, a0).offset(0xe8L).get() == 0x8L) {
      FUN_800451ec(0x50L);
    } else {
      FUN_800451ec(0x91L);
    }

    //LAB_80043f64
    if(_80059614.get() == 0) {
      JOY_MCD_CTRL.setu(0x1003L);
    } else {
      JOY_MCD_CTRL.setu(0x3003L);
    }

    //LAB_80043f88
    final long v0 = _8005962c.offset(_80059614.get() * 4).get();
    if((int)v0 >= 0) {
      if((int)v0 > 0) {
        //LAB_80043fb4
        do {
          _8005962c.offset(_80059614.get() * 4).subu(0x1L);
          _800595f4.deref().run(MEMORY.ref(4, a0).offset(0xcL).get() + _8005962c.offset(_80059614.get() * 4).get() * 240);
        } while((int)_8005962c.offset(_80059614.get() * 4).get() > 0);
      }

      //LAB_80044020
      if(_8005962c.offset(_80059614.get() * 4).get() == 0) {
        _8005962c.offset(_80059614.get() * 4).setu(0xffff_ffffL);
        _800595f4.deref().run(a0);
        _800595f8.deref().run(a0);
      }
    }

    //LAB_80044074
    if(JOY_MCD_STAT.get(0x200L) != 0) {
      JOY_MCD_CTRL.oru(0x10L);

      if(JOY_MCD_STAT.get(0x200L) != 0) {
        //LAB_800440b8
        while(!FUN_8004520c()) {
          DebugHelper.sleep(1);
        }

        JOY_MCD_DATA.setu(0x1L);
        FUN_800451ec(2000L);
        if(FUN_800447dc() == 0) {
          return 0;
        }

        FUN_8004486c();
        FUN_800451ec(430L);

        //LAB_80044114
        while(I_STAT.get(0x80L) == 0) {
          if(FUN_8004520c()) {
            return 0;
          }
        }

        JOY_MCD_DATA.setu(0x42L);
        FUN_800451ec(0x3cL);
        if(FUN_800447dc() == 0) {
          return 0;
        }

        FUN_8004486c();
        FUN_800451ec(430L);

        //LAB_80044190
        while(I_STAT.get(0x80L) == 0) {
          if(FUN_8004520c()) {
            return 0;
          }
        }

        JOY_MCD_DATA.setu(0x1L);
        FUN_800451ec(60L);
        if(FUN_800447dc() == 0) {
          return 0;
        }

        FUN_8004486c();
        return 0;
      }

      //LAB_80044204
      I_STAT.setu(0xffff_ff7fL);
    }

    //LAB_80044214
    if(MEMORY.ref(1, a0).offset(0x50L).get() == 0) {
      return 0x1L;
    }

    if(MEMORY.ref(1, a0).offset(0x37L).get() == 0) {
      return 0x1L;
    }

    //LAB_80044238
    return 0;
  }

  @Method(0x8004424cL)
  public static void FUN_8004424c(final long a0) {
    final long ret = _8005965c.get((int)_80059618.get()).deref().run(a0);
    _80059618.addu(0x1L);

    if((int)ret < 0) {
      //LAB_80044318
      _800595d4.deref().run(ret);
      return;
    }

    if(_80059618.get() != 0 && (_80059618.get() != 0x3L || MEMORY.ref(4, a0).offset(0x3cL).deref(1).get() != 0x80L)) {
      //LAB_800442c8
      FUN_800451ec(0x3cL);

      if(FUN_800447dc() == 0) {
        _800595d4.deref().run(-0x3L);
      }
    }

    //LAB_80044300
    if(_80059618.get() >= 0x5L) {
      _80059618.subu(0x1L);
    }

    //LAB_8004432c
  }

  @Method(0x8004433cL)
  public static long FUN_8004433c(final long a0, final long a1) {
    if((int)a1 < 0) {
      final long v1 = MEMORY.ref(4, a0).offset(0x40L).get();
      MEMORY.ref(1, a0).offset(0x44L).setu(0xffL);
      MEMORY.ref(1, a0).offset(0x45L).setu(0x1L);
      MEMORY.ref(1, v1).setu(~a1);
      final long s1 = JOY_MCD_DATA.get() & 0xffL;

      //LAB_800443a8
      while(JOY_MCD_STAT.get(0x1L) == 0) {
        DebugHelper.sleep(1);
      }

      //LAB_800443bc
      while(!FUN_8004520c()) {
        DebugHelper.sleep(1);
      }

      JOY_MCD_DATA.setu(~a1);
      return s1;
    }

    //LAB_800443e4
    final long s2;
    if(MEMORY.ref(4, a0).offset(0x3cL).deref(4).get() >> 0x4L == 0x8L && MEMORY.ref(1, a0).offset(0x44L).get() > 0x8L) {
      s2 = 0x22L;
    } else {
      s2 = 0x88L;
    }

    //LAB_80044418
    //LAB_8004441c
    _800c3a30.setu(430L);
    _800c3a2c.setu(TMR_SYSCLOCK_VAL);
    _800c3a34.setu(TMR_SYSCLOCK_MODE);

    if(JOY_MCD_STAT.get(0x2L) == 0) {
      //LAB_80044464
      while(JOY_MCD_STAT.get(0x2L) == 0) {
        DebugHelper.sleep(1);
      }
    }

    //LAB_80044478
    JOY_MCD_BAUD.setu(s2);
    final long s1 = a0 & 0xffL;

    //LAB_800444a4
    while(I_STAT.get(0x80L) == 0) {
      if(FUN_8004520c()) {
        return 0xffff_ffecL;
      }

      DebugHelper.sleep(1);
    }

    //LAB_800444d4
    JOY_MCD_DATA.setu(a1);
    if(s2 == 0x22L) {
      I_STAT.setu(0xffff_ff7fL);
      JOY_MCD_CTRL.oru(0x10L);
    }

    //LAB_80044514
    MEMORY.ref(4, a0).offset(0x3cL).deref(4).offset(MEMORY.ref(1, a0).offset(0x44L)).setu(s1);
    MEMORY.ref(1, a0).offset(0x44L).addu(0x1L);
    MEMORY.ref(1, a0).offset(0x45L).addu(0x1L);

    //LAB_80044544
    return s1;
  }

  @Method(0x800447dcL)
  public static long FUN_800447dc() {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x8004486cL)
  public static void FUN_8004486c() {
    while(JOY_MCD_STAT.get(0x2L) == 0) {
      DebugHelper.sleep(1);
    }
  }

  @Method(0x800448a4L)
  public static void FUN_800448a4(final long a0) {
    final long v1 = MEMORY.ref(1, a0).offset(0x46L).get();

    if(v1 == 0x2L) {
      FUN_80045164(a0);
    }

    if(v1 == 0x3L) {
      FUN_80045178(a0, MEMORY.ref(1, a0).offset(0xe4L).get());
    }

    if(v1 == 0x4L) {
      FUN_800451b8(a0, MEMORY.ref(1, a0).offset(0x47L).get());
    }
  }

  @Method(0x80045144L)
  public static void FUN_80045144(final long a0, final long a1) {
    MEMORY.ref(1, a0).offset(0x24L).setu(a1);
    MEMORY.ref(4, a0).offset(0x2cL).setu(a0 + 0x24L);
    MEMORY.ref(1, a0).offset(0x36L).setu(0x1L);
    MEMORY.ref(1, a0).offset(0x37L).setu(0x43L);
  }

  @Method(0x80045164L)
  public static void FUN_80045164(final long a0) {
    MEMORY.ref(4, a0).offset(0x2cL).setu(0);
    MEMORY.ref(1, a0).offset(0x36L).setu(0);
    MEMORY.ref(1, a0).offset(0x37L).setu(0x45L);
  }

  @Method(0x80045178L)
  public static void FUN_80045178(final long a0, final long a1) {
    MEMORY.ref(1, a0).offset(0x24L).setu(a1);
    MEMORY.ref(4, a0).offset(0x2cL).setu(a0 + 0x24L);
    MEMORY.ref(1, a0).offset(0x36L).setu(0x1L);
    MEMORY.ref(1, a0).offset(0x37L).setu(0x4cL);
  }

  @Method(0x800451b8L)
  public static void FUN_800451b8(final long a0, final long a1) {
    MEMORY.ref(1, a0).offset(0x24L).setu(a1);
    MEMORY.ref(4, a0).offset(0x2cL).setu(a0 + 0x24L);
    MEMORY.ref(1, a0).offset(0x36L).setu(0x1L);
    MEMORY.ref(1, a0).offset(0x37L).setu(0x47L);
  }

  @Method(0x800451ecL)
  public static void FUN_800451ec(final long a0) {
    _800c3a2c.setu(TMR_SYSCLOCK_VAL);
    _800c3a30.setu(a0);
  }

  @Method(0x8004520cL)
  public static boolean FUN_8004520c() {
    long a0 = TMR_SYSCLOCK_VAL.get(0xffffL);

    if(a0 < _800c3a2c.get()) {
      if(TMR_SYSCLOCK_MAX.get() == 0) {
        a0 += 0x1L;
      } else {
        a0 += TMR_SYSCLOCK_MAX.get();
      }
    }

    //LAB_80045254
    long v0 = a0 - _800c3a2c.get();

    if(TMR_SYSCLOCK_MODE.get(0x200L) == 0) {
      v0 /= 8;
    }

    //LAB_800452a0
    return _800c3a30.get() <= v0;
  }

  @Method(0x800452acL)
  public static long FUN_800452ac(final long a0) {
    _80059654.setu(_800595ec.deref().run(a0));
    MEMORY.ref(4, a0).offset(0x3cL).deref(4).setu(0);
    return FUN_8004433c(a0, 0xffff_fffeL);
  }

  @Method(0x8004b5bcL)
  public static long maxShort(final long a, final long b) {
    return Math.max((short)(a & 0xffffL), (short)(b & 0xffffL));
  }

  @Method(0x8004cdbcL)
  public static void FUN_8004cdbc(final long a0, final long a1) {
    if(a0 << 0x10L != 0) {
      if(a1 << 0x10L != 0) {
        SPU_CTRL_REG_CPUCNT.oru(0b1L); // CD audio enable
        return;
      }

      //LAB_8004cdec
      SPU_CTRL_REG_CPUCNT.oru(0b10L); // External audio enable
      return;
    }

    //LAB_8004ce08
    if(a1 << 0x10L != 0) {
      SPU_CTRL_REG_CPUCNT.and(0b1111_1111_1111_1110L); // CD audio disable
      return;
    }

    //LAB_8004ce2c
    SPU_CTRL_REG_CPUCNT.and(0b1111_1111_1111_1101L); // External audio disable
  }

  @Method(0x8004ced4L)
  public static void setCdVolume(long left, long right) {
    final long l;
    final long r;

    if(spuMono_800c6666.get() == 0) {
      l = left << 0x8L;
      r = right << 0x8L;
    } else {
      left <<= 0x18L;
      right <<= 0x18L;
      left >>= 0x10L;
      right >>= 0x10L;
      l = maxShort(left, right);
      r = l;
    }

    LOGGER.info("CD volume set to %04x, %04x", l, r);

    //LAB_8004cf0c
    CD_VOL_L.setu(l);
    CD_VOL_R.setu(r);
  }
}
