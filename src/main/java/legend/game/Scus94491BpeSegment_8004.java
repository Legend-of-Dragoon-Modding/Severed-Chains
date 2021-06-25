package legend.game;

import legend.core.DebugHelper;
import legend.core.dma.DmaChannel;
import legend.core.dma.DmaChannelType;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.kernel.PriorityChainEntry;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.SupplierRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.spu.SpuDmaTransfer;
import legend.core.spu.Voice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

import static legend.core.Hardware.DMA;
import static legend.core.Hardware.GATE;
import static legend.core.Hardware.MEMORY;
import static legend.core.Hardware.SPU;
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
import static legend.core.kernel.Kernel.EvMdNOINTR;
import static legend.core.kernel.Kernel.EvSpCOMP;
import static legend.core.kernel.Kernel.HwSPU;
import static legend.core.memory.segments.MemoryControl1Segment.SPU_DELAY;
import static legend.core.spu.Spu.CD_VOL_L;
import static legend.core.spu.Spu.CD_VOL_R;
import static legend.core.spu.Spu.SOUND_RAM_DATA_TRANSFER_ADDR;
import static legend.core.spu.Spu.SOUND_RAM_DATA_TRANSFER_CTRL;
import static legend.core.spu.Spu.SOUND_RAM_REVERB_WORK_ADDR;
import static legend.core.spu.Spu.SPU_CTRL_REG_CPUCNT;
import static legend.core.spu.Spu.SPU_MAIN_VOL_L;
import static legend.core.spu.Spu.SPU_MAIN_VOL_R;
import static legend.core.spu.Spu.SPU_REVERB_OUT_L;
import static legend.core.spu.Spu.SPU_REVERB_OUT_R;
import static legend.core.spu.Spu.SPU_VOICE_CHN_NOISE_MODE;
import static legend.core.spu.Spu.SPU_VOICE_CHN_REVERB_MODE;
import static legend.core.spu.Spu.SPU_VOICE_KEY_OFF;
import static legend.core.spu.Spu.SPU_VOICE_KEY_ON;
import static legend.game.Scus94491BpeSegment._80011db0;
import static legend.game.Scus94491BpeSegment_8002.EnableEvent;
import static legend.game.Scus94491BpeSegment_8002.OpenEvent;
import static legend.game.Scus94491BpeSegment_8002.SysDeqIntRP;
import static legend.game.Scus94491BpeSegment_8002.SysEnqIntRP;
import static legend.game.Scus94491BpeSegment_8003.ChangeClearRCnt;
import static legend.game.Scus94491BpeSegment_8003.SetDmaInterruptCallback;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8005._80054d0c;
import static legend.game.Scus94491BpeSegment_8005._80058d0c;
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
import static legend.game.Scus94491BpeSegment_8005._80059f3c;
import static legend.game.Scus94491BpeSegment_8005._80059f7c;
import static legend.game.Scus94491BpeSegment_8005._80059f7e;
import static legend.game.Scus94491BpeSegment_8005._8005a1ce;
import static legend.game.Scus94491BpeSegment_8005._8005a1d0;
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
import static legend.game.Scus94491BpeSegment_800c._800c3a40;
import static legend.game.Scus94491BpeSegment_800c._800c3a62;
import static legend.game.Scus94491BpeSegment_800c._800c43d0;
import static legend.game.Scus94491BpeSegment_800c._800c43d4;
import static legend.game.Scus94491BpeSegment_800c._800c43d8;
import static legend.game.Scus94491BpeSegment_800c._800c4aa4;
import static legend.game.Scus94491BpeSegment_800c._800c4ab4;
import static legend.game.Scus94491BpeSegment_800c._800c4ab8;
import static legend.game.Scus94491BpeSegment_800c._800c4ac0;
import static legend.game.Scus94491BpeSegment_800c._800c4ac4;
import static legend.game.Scus94491BpeSegment_800c._800c4ac8;
import static legend.game.Scus94491BpeSegment_800c._800c4ac9;
import static legend.game.Scus94491BpeSegment_800c._800c4aca;
import static legend.game.Scus94491BpeSegment_800c._800c4ad4;
import static legend.game.Scus94491BpeSegment_800c._800c4af0;
import static legend.game.Scus94491BpeSegment_800c._800c4af1;
import static legend.game.Scus94491BpeSegment_800c._800c4af2;
import static legend.game.Scus94491BpeSegment_800c._800c4af4;
import static legend.game.Scus94491BpeSegment_800c._800c4aff;
import static legend.game.Scus94491BpeSegment_800c._800c4b01;
import static legend.game.Scus94491BpeSegment_800c._800c4ba6;
import static legend.game.Scus94491BpeSegment_800c._800c4ba8;
import static legend.game.Scus94491BpeSegment_800c._800c4baa;
import static legend.game.Scus94491BpeSegment_800c._800c4bac;
import static legend.game.Scus94491BpeSegment_800c._800c4bae;
import static legend.game.Scus94491BpeSegment_800c._800c4baf;
import static legend.game.Scus94491BpeSegment_800c._800c4bb1;
import static legend.game.Scus94491BpeSegment_800c._800c4bb6;
import static legend.game.Scus94491BpeSegment_800c._800c4bb8;
import static legend.game.Scus94491BpeSegment_800c._800c4bcc;
import static legend.game.Scus94491BpeSegment_800c._800c4bcd;
import static legend.game.Scus94491BpeSegment_800c._800c4bd0;
import static legend.game.Scus94491BpeSegment_800c._800c4be0;
import static legend.game.Scus94491BpeSegment_800c._800c6628;
import static legend.game.Scus94491BpeSegment_800c._800c6630;
import static legend.game.Scus94491BpeSegment_800c._800c6633;
import static legend.game.Scus94491BpeSegment_800c._800c6634;
import static legend.game.Scus94491BpeSegment_800c._800c663d;
import static legend.game.Scus94491BpeSegment_800c._800c6642;
import static legend.game.Scus94491BpeSegment_800c._800c6646;
import static legend.game.Scus94491BpeSegment_800c._800c665a;
import static legend.game.Scus94491BpeSegment_800c._800c665b;
import static legend.game.Scus94491BpeSegment_800c._800c665c;
import static legend.game.Scus94491BpeSegment_800c._800c665e;
import static legend.game.Scus94491BpeSegment_800c._800c6660;
import static legend.game.Scus94491BpeSegment_800c._800c6662;
import static legend.game.Scus94491BpeSegment_800c._800c6664;
import static legend.game.Scus94491BpeSegment_800c._800c6668;
import static legend.game.Scus94491BpeSegment_800c._800c666a;
import static legend.game.Scus94491BpeSegment_800c._800c666c;
import static legend.game.Scus94491BpeSegment_800c._800c666e;
import static legend.game.Scus94491BpeSegment_800c._800c6670;
import static legend.game.Scus94491BpeSegment_800c._800c6672;
import static legend.game.Scus94491BpeSegment_800c._800c667c;
import static legend.game.Scus94491BpeSegment_800c._800c6680;
import static legend.game.Scus94491BpeSegment_800c.eventSpuIrq_800c664c;
import static legend.game.Scus94491BpeSegment_800c.queuedSpuDmaTransferArray_800c49d0;
import static legend.game.Scus94491BpeSegment_800c.spuDmaIndex_800c6669;
import static legend.game.Scus94491BpeSegment_800c.spuDmaTransferInProgress_800c6650;
import static legend.game.Scus94491BpeSegment_800c.spuMono_800c6666;

public final class Scus94491BpeSegment_8004 {
  private Scus94491BpeSegment_8004() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8004.class);

  public static final Value _8004db58 = MEMORY.ref(2, 0x8004db58L);

  public static final Value _8004db88 = MEMORY.ref(2, 0x8004db88L);

  public static final Value callback_8004dbc0 = MEMORY.ref(4, 0x8004dbc0L);
  public static final Value _8004dbc4 = MEMORY.ref(4, 0x8004dbc4L);
  public static final Value _8004dbc8 = MEMORY.ref(4, 0x8004dbc8L);

  public static final Value _8004dbcd = MEMORY.ref(1, 0x8004dbcdL);

  public static final Value _8004dd04 = MEMORY.ref(4, 0x8004dd04L);
  public static final Value _8004dd08 = MEMORY.ref(4, 0x8004dd08L);
  public static final Value _8004dd0c = MEMORY.ref(4, 0x8004dd0cL);
  public static final Value _8004dd10 = MEMORY.ref(4, 0x8004dd10L);
  public static final Value _8004dd14 = MEMORY.ref(4, 0x8004dd14L);
  public static final Value _8004dd18 = MEMORY.ref(4, 0x8004dd18L);
  public static final Value _8004dd1c = MEMORY.ref(2, 0x8004dd1cL);
  public static final Value _8004dd1e = MEMORY.ref(1, 0x8004dd1eL);

  public static final Value _8004dd20 = MEMORY.ref(4, 0x8004dd20L);
  public static final Value _8004dd24 = MEMORY.ref(4, 0x8004dd24L);
  public static final Value _8004dd28 = MEMORY.ref(4, 0x8004dd28L);

  public static final Value _8004dd30 = MEMORY.ref(4, 0x8004dd30L);
  public static final Value _8004dd34 = MEMORY.ref(2, 0x8004dd34L);
  public static final Value _8004dd36 = MEMORY.ref(2, 0x8004dd36L);
  public static final Value _8004dd38 = MEMORY.ref(2, 0x8004dd38L);

  public static final Pointer<RunnableRef> callback_8004dd3c = MEMORY.ref(4, 0x8004dd3cL, Pointer.of(RunnableRef::new));
  public static final Pointer<RunnableRef> callback_8004dd40 = MEMORY.ref(4, 0x8004dd40L, Pointer.of(RunnableRef::new));

  public static final Value _8004dd48 = MEMORY.ref(2, 0x8004dd48L);

  public static final Value _8004dd80 = MEMORY.ref(2, 0x8004dd80L);

  /**
   * \SECT\DRGN21.BIN, also gets changed in SInitBin
   */
  public static final Value _8004dd88 = MEMORY.ref(1, 0x8004dd88L);

  public static final Value _8004dda0 = MEMORY.ref(2, 0x8004dda0L);
  public static final Value fileNamePtr_8004dda4 = MEMORY.ref(4, 0x8004dda4L);

  public static final Value callbackIndex_8004ddc4 = MEMORY.ref(4, 0x8004ddc4L);
  public static final Value fileCount_8004ddc8 = MEMORY.ref(4, 0x8004ddc8L);
  public static final Value _8004ddcc = MEMORY.ref(1, 0x8004ddccL);
  public static final Value _8004ddd0 = MEMORY.ref(4, 0x8004ddd0L);
  public static final Value _8004ddd4 = MEMORY.ref(4, 0x8004ddd4L);
  public static final Value _8004ddd8 = MEMORY.ref(4, 0x8004ddd8L);

  public static final ArrayRef<Pointer<SupplierRef<Long>>> callbackArray_8004dddc = (ArrayRef<Pointer<SupplierRef<Long>>>)MEMORY.ref(0x70, 0x8004dddcL, ArrayRef.of(Pointer.class, 28, 4, (Function)Pointer.of(SupplierRef::new)));

  public static final Value _8004de4c = MEMORY.ref(4, 0x8004de4cL);

  public static final Value _8004de58 = MEMORY.ref(4, 0x8004de58L);

  /** This is the world's largest jump table */
  public static final ArrayRef<Pointer<FunctionRef<Long, Long>>> _8004e098 = (ArrayRef<Pointer<FunctionRef<Long, Long>>>)MEMORY.ref(0x1200, 0x8004e098L, ArrayRef.of(Pointer.class, 0x480, 4, (Function)Pointer.of(FunctionRef::new)));

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

  public static final Value _8004f65c = MEMORY.ref(2, 0x8004f65cL);

  public static final Value _8004f664 = MEMORY.ref(1, 0x8004f664L);

  public static final Value _8004f6a4 = MEMORY.ref(4, 0x8004f6a4L);

  public static final Value _8004f6e4 = MEMORY.ref(4, 0x8004f6e4L);
  public static final Value _8004f6e8 = MEMORY.ref(4, 0x8004f6e8L);
  public static final Value _8004f6ec = MEMORY.ref(4, 0x8004f6ecL);

  @Method(0x80040010L)
  public static void FUN_80040010(final SVECTOR vector, final MATRIX matrix) {
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long t8;
    long t9;

    t7 = vector.getX();
    t9 = t7 & 0xfffL;

    if(t7 < 0) {
      t7 = -t7;
      t7 &= 0xfffL;

      //LAB_8004002c
      t8 = t7 << 0x2L;
      t9 = _80054d0c.offset(t8).get();
      t6 = (short)(t9 & 0xffffL);
      t3 = -t6;
    } else {
      //LAB_80040054
      t8 = t9 << 0x2L;
      t9 = _80054d0c.offset(t8).get();
      t3 = (short)(t9 & 0xffffL);
    }

    t0 = t9 >> 0x10L;

    //LAB_80040074
    t7 = vector.getY();
    t9 = t7 & 0xfffL;

    if(t7 < 0) {
      t7 = -t7;
      t7 &= 0xfffL;

      //LAB_80040090
      t8 = t7 << 0x2L;
      t9 = _80054d0c.offset(t8).get();
      t6 = (short)(t9 & 0xffffL);
      t4 = -t6;
    } else {
      //LAB_800400b8
      t8 = t9 << 0x2L;
      t9 = _80054d0c.offset(t8).get();
      t4 = (short)(t9 & 0xffffL);
      t6 = -t4;
    }

    t1 = t9 >> 0x10L;

    //LAB_800400dc
    t7 = vector.getZ();
    matrix.set(2, 0, (short)t6);
    t8 = t3 * t1 & 0xffffffffL;
    t6 = t8 >> 0xcL;
    matrix.set(2, 1, (short)t6);
    t9 = t7 & 0xfffL;

    if(t7 < 0) {
      t8 = t0 * t1 & 0xffffffffL;
      t6 = t8 >> 0xcL;
      matrix.set(2, 2, (short)t6);
      t7 = -t7;
      t7 &= 0xfffL;

      //LAB_8004011c
      t8 = t7 << 0x2L;
      t9 = _80054d0c.offset(t8).get();
      t8 = (short)(t9 & 0xffffL);
      t5 = -t8;
    } else {
      //LAB_80040144
      t7 = t0 * t1 & 0xffffffffL;
      t6 = t7 >> 0xcL;
      matrix.set(2, 2, (short)t6);
      t8 = t9 << 0x2L;
      t9 = _80054d0c.offset(t8).get();
      t5 = (short)(t9 & 0xffffL);
    }

    t2 = t9 >> 0x10L;

    //LAB_80040170
    t7 = t1 * t2 & 0xffffffffL;
    t6 = t7 >> 0xcL;
    matrix.set(0, 0, (short)t6);
    t7 = t5 * t1 & 0xffffffffL;
    t6 = t7 >> 0xcL;
    matrix.set(1, 0, (short)t6);
    t7 = t3 * t4 & 0xffffffffL;
    t8 = t7 >> 0xcL;
    t7 = t8 * t2 & 0xffffffffL;
    t6 = t7 >> 0xcL;
    t7 = t5 * t0 & 0xffffffffL;
    t9 = t7 >> 0xcL;
    t7 = t6 - t9;
    matrix.set(0, 1, (short)t7);
    t6 = t0 * t2 & 0xffffffffL;
    t7 = t6 >> 0xcL;
    t6 = t8 * t5 & 0xffffffffL;
    t9 = t6 >> 0xcL;
    t6 = t9 + t7;
    matrix.set(1, 1, (short)t6);
    t7 = t4 * t0 & 0xffffffffL;
    t8 = t7 >> 0xcL;
    t7 = t8 * t2 & 0xffffffffL;
    t6 = t7 >> 0xcL;
    t7 = t3 * t5 & 0xffffffffL;
    t9 = t7 >> 0xcL;
    t7 = t6 + t9;
    matrix.set(0, 2, (short)t7);
    t6 = t3 * t2 & 0xffffffffL;
    t7 = t6 >> 0xcL;
    t6 = t8 * t5 & 0xffffffffL;
    t9 = t6 >> 0xcL;
    t6 = t9 - t7;
    matrix.set(1, 2, (short)t6);
  }

  @Method(0x800402a0L)
  public static void FUN_800402a0(final long a0, final MATRIX a1) {
    final long t1;
    final long t9;

    if(a0 < 0) {
      //LAB_800402bc
      t9 = _80054d0c.offset((-a0 & 0xfffL) * 4).get();
      t1 = -(short)t9;
    } else {
      //LAB_800402e4
      t9 = _80054d0c.offset((a0 & 0xfffL) * 4).get();
      t1 = (short)t9;
    }

    final long t0 = t9 >> 0x10L;

    //LAB_80040304
    final long m10 = a1.get(1, 0);
    final long m11 = a1.get(1, 1);
    final long m12 = a1.get(1, 2);
    final long m20 = a1.get(2, 0);
    final long m21 = a1.get(2, 1);
    final long m22 = a1.get(2, 2);

    a1.set(1, 0, (short)((t0 * m10 - t1 * m20) / 4096));
    a1.set(1, 1, (short)((t0 * m11 - t1 * m21) / 4096));
    a1.set(1, 2, (short)((t0 * m12 - t1 * m22) / 4096));
    a1.set(2, 0, (short)((t1 * m10 + t0 * m20) / 4096));
    a1.set(2, 1, (short)((t1 * m11 + t0 * m21) / 4096));
    a1.set(2, 2, (short)((t1 * m12 + t0 * m22) / 4096));
  }

  @Method(0x80040440L)
  public static void FUN_80040440(final long a0, final MATRIX a1) {
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t8;
    long lo;

    long t7 = a0;
    long t9 = t7 & 0xfffL;
    if(t7 < 0) {
      t7 = -t7;
      t7 &= 0xfffL;

      //LAB_8004045c
      t8 = t7 << 0x2L;
      t9 = _80054d0c.offset(t8).get();
      t1 = (int)t9;
      t0 = (int)t9 >> 16;
    } else {
      //LAB_80040480
      t8 = t9 * 4L;
      t9 = _80054d0c.offset(t8).get();
      t7 = (int)t9;
      t1 = -t7;
      t0 = t9 >> 16;
    }

    //LAB_800404a4
    t2 = a1.get(0);
    t5 = a1.get(6);
    lo = t0 * t2;
    t3 = a1.get(1);
    t6 = a1.get(7);
    t8 = lo;
    t4 = a1.get(1);
    t7 = a1.get(8);
    lo = t1 * t5;
    t9 = lo;
    t9 = t8 - t9;
    t8 = t9 >> 0xcL;
    lo = t0 * t3;
    a1.set(0, (short)t8);
    t8 = lo;
    lo = t1 * t6;
    t9 = lo;
    t9 = t8 - t9;
    t8 = t9 >> 0xcL;
    lo = t0 * t4;
    a1.set(1, (short)t8);
    t8 = lo;
    lo = t1 * t7;
    t9 = lo;
    t9 = t8 - t9;
    t8 = t9 >> 0xcL;
    lo = t1 * t2;
    a1.set(2, (short)t8);
    t8 = lo;
    lo = t0 * t5;
    t9 = lo;
    t9 = t8 + t9;
    t8 = t9 >> 0xcL;
    lo = t1 * t3;
    a1.set(6, (short)t8);
    t8 = lo;
    lo = t0 * t6;
    t9 = lo;
    t9 = t8 + t9;
    t8 = t9 >> 0xcL;
    lo = t1 * t4;
    a1.set(7, (short)t8);
    t8 = lo;
    lo = t0 * t7;
    t9 = lo;
    t9 = t8 + t9;
    t8 = t9 >> 0xcL;
    a1.set(8, (short)t8);
  }

  @Method(0x800405e0L)
  public static void FUN_800405e0(final long a0, final MATRIX a1) {
    final long t1;
    final long t9;

    if(a0 < 0) {
      //LAB_800405fc
      t9 = _80054d0c.offset((-a0 & 0xfffL) * 4).get();
      t1 = -(short)(t9 & 0xffffL);
    } else {
      //LAB_80040624
      t9 = _80054d0c.offset((a0 & 0xfffL) * 4).get();
      t1 = (short)(t9 & 0xffffL);
    }

    final long t0 = t9 >> 0x10L;

    //LAB_80040644
    final long m00 = a1.get(0, 0);
    final long m01 = a1.get(0, 1);
    final long m02 = a1.get(0, 2);
    final long m10 = a1.get(1, 0);
    final long m11 = a1.get(1, 1);
    final long m12 = a1.get(1, 2);

    a1.set(0, 0, (short)((t0 * m00 - t1 * m11) / 4096));
    a1.set(0, 1, (short)((t0 * m10 - t1 * m02) / 4096));
    a1.set(0, 2, (short)((t0 * m01 - t1 * m12) / 4096));
    a1.set(1, 0, (short)((t1 * m00 + t0 * m11) / 4096));
    a1.set(1, 1, (short)((t1 * m10 + t0 * m02) / 4096));
    a1.set(1, 2, (short)((t1 * m01 + t0 * m12) / 4096));
  }

  @Method(0x80040b90L)
  public static long FUN_80040b90(long a0, long a1) {
    final long v0;
    long v1;

    long a2 = 0;
    long a3 = 0;

    if(a1 < 0) {
      a2 = 0x1L;
      a1 = -a1;
    }

    //LAB_80040ba4
    if(a0 < 0) {
      a3 = 0x1L;
      a0 = -a0;
    }

    //LAB_80040bb4
    if(a1 == 0) {
      if(a0 >= a1) {
        return 0;
      }
    }

    //LAB_80040bc8
    if(a0 < a1) {
      if((0x7fe0L & a0) == 0) {
        //LAB_80040c10
        //LAB_80040c3c
        a0 = Math.floorDiv(a0 << 0xaL, a1);
      } else {
        a0 = Math.floorDiv(a0, a1 >> 0xaL);
      }

      v0 = a0 << 0x1L;

      //LAB_80040c44
      v1 = _80058d0c.offset(v0).get();
    } else {
      //LAB_80040c58
      if((0x7fe0L & a1) == 0) {
        //LAB_80040c98
        //LAB_80040cc4
        a0 = Math.floorDiv(a1 << 0xaL, a0);
      } else {
        //LAB_80040c8c
        a0 = Math.floorDiv(a1, a0 >> 0xaL);
      }

      v0 = a0 << 0x1L;

      //LAB_80040ccc
      v1 = 0x400L - _80058d0c.offset(v0).get();
    }

    //LAB_80040ce0
    if(a2 != 0) {
      v1 = 0x800L - v1;
    }

    //LAB_80040cec
    if(a3 == 0) {
      return v1;
    }

    //LAB_80040cfc
    return -v1;
  }

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

  @Method(0x80042ba0L)
  public static long FUN_80042ba0(final long a0, final long a1) {
    final long s0 = _800595e8.deref().run(a0);

    if(_800595f0.deref().run(s0) != 0) {
      return 0;
    }

    MEMORY.ref(1, s0).offset(0x46L).setu(0x1L);
    MEMORY.ref(4, s0).offset(0x14L).setu(getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80042c1c"));
    MEMORY.ref(4, s0).offset(0x20L).setu(a1);
    MEMORY.ref(4, s0).offset(0x18L).setu(getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80042c44"));
    return 1;
  }

  @Method(0x80042d10L)
  public static boolean FUN_80042d10(final long a0, final long a1, final long a2) {
    final Value v1 = new Value(4);

    final long s0 = _800595e8.deref().run(a0);

    if(_800595f0.deref().run(s0) != 0) {
      return false;
    }

    MEMORY.ref(1, s0).offset(0x46L).setu(0x1L);
    MEMORY.ref(4, s0).offset(0x14L).setu(getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80042dac"));

    MEMORY.ref(1, s0).offset(0x51L).setu(a1);
    MEMORY.ref(1, s0).offset(0x52L).setu(a2);
    MEMORY.ref(4, s0).offset(0x18L).setu(getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80042e04"));

    v1.setu(MEMORY.ref(1, s0).offset(0xe4));
    v1.xoru(a1);
    v1.setu(v1.get() < 0x1L ? 1 : 0);
    MEMORY.ref(1, s0).offset(0x53L).setu(v1);

    //LAB_80042d94
    return true;
  }

  @Method(0x80042e70L)
  public static long FUN_80042e70(long a0) {
    final long v1 = _800595e8.deref().run(a0);

    if(MEMORY.ref(1, v1).offset(0x37L).get() == 0) {
      if(MEMORY.ref(1, v1).offset(0x38L).get() == 0) {
        if(MEMORY.ref(4, v1).offset(0x10L).get() == v1 || MEMORY.ref(1, v1).offset(0x39L).get() == 0) {
          //LAB_80042ecc
          if(MEMORY.ref(4, v1).offset(0x30L).deref(1).get() == 0) {
            return MEMORY.ref(1, v1).offset(0x49L).get();
          }
        }
      }
    }

    //LAB_80042ee4
    a0 = MEMORY.ref(1, v1).offset(0x49).get();

    if(a0 == 0x3L) {
      return 0x1L;
    }

    if(a0 >= 0x4) {
      //LAB_80042f0c
      if(a0 == 0x6L) {
        return 0x4L;
      }
    }

    if(a0 == 0x2L) {
      return 0x1L;
    }

    //LAB_80042f28
    return MEMORY.ref(1, v1).offset(0x49L).get();
  }

  @Method(0x80042f40L)
  public static long FUN_80042f40(final long a0, final long a1, final long a2) {
    final long v1 = _800595e8.deref().run(a0);

    if(a1 == 0x3L) {
      //LAB_80042fc8
      return MEMORY.ref(1, v1).offset(0xe4L).get();
    }

    if(a1 < 0x4L) {
      if(a1 == 0x1L) {
        //LAB_80042fb0
        return MEMORY.ref(1, v1).offset(0xe8L).get();
      }

      if(a1 == 0x2L) {
        //LAB_80042fbc
        return MEMORY.ref(2, v1).offset(0xe6L).get();
      }

      return 0;
    }

    //LAB_80042f94
    if(a1 != 0x4L) {
      if(a1 == 0x64) {
        return MEMORY.ref(4, v1).offset(0x4cL).get();
      }

      return 0;
    }

    //LAB_80042fd4
    if(a2 < 0) {
      return MEMORY.ref(1, v1).offset(0xe3L).get();
    }

    //LAB_80042fe8
    if(a2 >= MEMORY.ref(1, v1).offset(0xe3L).get()) {
      //LAB_80043020
      return 0;
    }

    //LAB_80043024
    return MEMORY.ref(4, v1).deref(2).offset(a2 * 2L).get();
  }

  @Method(0x80043040L)
  public static long FUN_80043040(final long a0, final long a1, final long a2) {
    long v1 = _800595e8.deref().run(a0);

    if(a1 < 0) {
      return MEMORY.ref(1, v1).offset(0xe9L).get();
    }

    //LAB_80043078
    if(a1 >= MEMORY.ref(1, v1).offset(0xe9L).get()) {
      return 0;
    }

    v1 = MEMORY.ref(4, v1).offset(0x4L).get() + a1 * 5L;

    return switch((int)a2) {
      case 1 -> MEMORY.ref(1, v1).get();
      case 2 -> MEMORY.ref(1, v1).offset(0x1L).get();
      case 3 -> MEMORY.ref(1, v1).offset(0x2L).get();
      case 4 -> MEMORY.ref(1, v1).offset(0x3L).get();
      case 5 -> MEMORY.ref(1, v1).offset(0x4L).get();
      default -> throw new RuntimeException("Bad switch");
    };
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

  @Method(0x80045cb8L)
  public static void FUN_80045cb8() {
    FUN_8004b7c0(0x1L);
    FUN_8004a8b8();

    LAB_80045d04:
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if(_800c4af0.offset(voiceIndex * 0x124L).get() == 0x1L || _800c4af2.offset(voiceIndex * 0x124L).get() == 0x1L) {
        //LAB_80045d24
        FUN_80047b38(voiceIndex);

        if(_800c4be0.offset(voiceIndex * 0x124L).get() == 0) {
          //LAB_80045d40
          do {
            FUN_8004a7ec(voiceIndex);

            if((FUN_80047bd0(voiceIndex) & 0xffffL) == 0) {
              _800c4ad4.offset(voiceIndex * 0x124L).addu(0x3L);
              _800c6634.setu(0);
            } else {
              //LAB_80045d7c
              _800c6634.setu(0x1L);
              final long v1 = _800c4ac8.offset(voiceIndex * 0x124L).get(0xf0L);
              if(v1 == 0xb0L) {
                //LAB_80045e60
                //switchD
                switch((int)_800c4aca.offset(voiceIndex * 0x124L).get()) {
                  case 0x1:
                    FUN_8004906c(voiceIndex);
                    break;

                  case 0x2:
                    FUN_80049250(voiceIndex);
                    break;

                  case 0x6:
                    FUN_80049f14(voiceIndex);
                    break;

                  case 0x7:
                    FUN_80049638(voiceIndex);
                    break;

                  case 0xa:
                    if(spuMono_800c6666.get() == 0) {
                      //LAB_80045f44
                      FUN_80049980(voiceIndex);
                    } else if(_800c4af0.offset(voiceIndex * 0x124L).get() == 0) {
                      //LAB_80045f30
                      _800c4ad4.offset(voiceIndex * 0x124L).addu(0x6L);
                    } else {
                      _800c6680.deref(4).offset(0x4L).setu(_800c4ad4.offset(voiceIndex * 0x124L).offset(_800c4aa4).offset(0x2L));
                      _800c4ad4.offset(voiceIndex * 0x124L).addu(0x3L);
                    }

                    break;

                  case 0x40:
                    FUN_80049cbc(voiceIndex);
                    break;

                  case 0x41:
                    FUN_80049480(voiceIndex);
                    break;

                  case 0x60:
                    FUN_80049e2c(voiceIndex);
                    FUN_8004a5e0(voiceIndex);
                    break LAB_80045d04;

                  case 0x62:
                    FUN_8004a2c0(voiceIndex);
                    break;

                  case 0x63:
                    FUN_8004a34c(voiceIndex);
                    break;
                }
              } else if(v1 >= 0xb1L) {
                //LAB_80045dd4
                if(v1 == 0xe0L) {
                  //LAB_80045fc8
                  FUN_800486d4(voiceIndex);
                } else if(v1 >= 0xe1L) {
                  //LAB_80045df8
                  if(v1 == 0xf0L) {
                    if(_800c4aca.offset(voiceIndex * 0x124L).get() == 0x2fL) {
                      //LAB_80045e24
                      FUN_80048eb8(voiceIndex);
                      break;
                    }

                    if(_800c4aca.offset(voiceIndex * 0x124L).get() == 0x51L) {
                      //LAB_80045e38
                      FUN_80048f98(voiceIndex);
                    }
                  }
                } else if(v1 == 0xc0L) {
                  //LAB_80045e4c
                  FUN_80048fec(voiceIndex);
                }
              } else if(v1 == 0x90L) {
                //LAB_80046004
                FUN_80046a04(voiceIndex);
              } else if(v1 >= 0x91L) {
                //LAB_80045dc0
                if(v1 == 0xa0L) {
                  //LAB_80045ff0
                  FUN_80046224(voiceIndex);
                }
              } else if(v1 == 0x80L) {
                //LAB_80045fdc
                FUN_800486d4(voiceIndex);
              }
            }

            //caseD_3
            FUN_8004a5e0(voiceIndex);
          } while(_800c4be0.offset(voiceIndex * 0x124L).get() == 0);
        }

        //LAB_8004602c
        if(_800c6634.get() != 0) {
          _800c666a.oru(_800c4ba6.offset(voiceIndex * 0x124L));
          _800c666c.oru(_800c4ba8.offset(voiceIndex * 0x124L));
          _800c666e.oru(_800c4baa.offset(voiceIndex * 0x124L));
          _800c6670.oru(_800c4bac.offset(voiceIndex * 0x124L));

          _800c4ba6.offset(voiceIndex * 0x124L).setu(0);
          _800c4ba8.offset(voiceIndex * 0x124L).setu(0);
          _800c4baa.offset(voiceIndex * 0x124L).setu(0);
          _800c4bac.offset(voiceIndex * 0x124L).setu(0);

          _800c6634.setu(0);
        }

        //LAB_800460a0
        if(_800c4bd0.offset(voiceIndex * 0x124L).get() != 0 || _800c4af2.offset(voiceIndex * 0x124L).get() == 0x1L) {
          //LAB_800460c0
          if(_800c4be0.offset(voiceIndex * 0x124L).get() != 0) {
            _800c4be0.offset(voiceIndex * 0x124L).subu(0x1L);
          }
        }

        //LAB_800460d4
        if(_800c4aff.offset(voiceIndex * 0x124L).get() != 0) {
          _800c4ac8.offset(voiceIndex * 0x124L).setu(_800c4b01.offset(voiceIndex * 0x124L));
          _800c4aff.offset(voiceIndex * 0x124L).setu(0);
          _800c4ac9.offset(voiceIndex * 0x124L).setu(_800c4b01.offset(voiceIndex * 0x124L));
          _800c4ad4.offset(voiceIndex * 0x124L).setu(_800c4af4.offset(voiceIndex * 0x124L));

          if(_800c4bae.offset(voiceIndex * 0x124L).get() == 0) {
            //LAB_80046118
            _800c4af0.offset(voiceIndex * 0x124L).setu(0x1L);
            _800c4be0.offset(voiceIndex * 0x124L).setu(0);
          } else {
            _800c4bae.offset(voiceIndex * 0x124L).setu(0);
          }
        }
      }

      //LAB_80046120
      FUN_8004af98(voiceIndex);
    }

    SPU_VOICE_CHN_NOISE_MODE.setu(_800c6646);
    SPU_VOICE_CHN_REVERB_MODE.setu(_800c6642);

    if(_800c666e.get() != 0) {
      SPU_VOICE_KEY_OFF.setu(_800c666e);
    }

    //LAB_800461b0
    if(_800c666a.get() != 0) {
      SPU_VOICE_KEY_ON.setu(_800c666a);
    }

    //LAB_800461e0
    _800c666a.setu(0);
    _800c666c.setu(0);
    _800c666e.setu(0);
    _800c6670.setu(0);

    FUN_800470fc();
    FUN_8004b2c4();
    FUN_8004b7c0(0);
  }

  @Method(0x80046224L)
  public static void FUN_80046224(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80046a04L)
  public static void FUN_80046a04(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x800470fcL)
  public static void FUN_800470fc() {
    long v0;
    long v1;

    long a0;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long div;

    s3 = _800c4ac8.getAddress();
    s6 = _800c6630.getAddress();
    s2 = 0;
    s4 = _800c3a40.getAddress();
    s5 = 0x1L;
    v1 = s2 << 0x10L;

    //LAB_80047144
    do {
      v1 >>= 0x10L;
      v0 = v1 * 3;
      v1 = v0 << 0x4L;
      v0 += v1;
      v0 <<= 0x1L;
      a0 = v0 + s4;

      v0 = s2 + 0x1L;
      if(MEMORY.ref(2, a0).get() == s5) {
        v0 = MEMORY.ref(2, a0).offset(0x6L).get();
        v1 = v0 << 0x3L;
        v1 += v0;
        v1 <<= 0x3L;
        v1 += v0;
        v1 <<= 0x2L;
        v0 = MEMORY.ref(2, a0).offset(0x14L).get();

        s3 += v1;
        v1 >>= 0x10L;
        if(v0 == s5 && MEMORY.ref(2, a0).offset(0x44L).get() == s5 && MEMORY.ref(1, s3).offset(0x104L).get() == s5) {
          //LAB_800471d0
          v1 = s2 << 0x10L;

          //LAB_800471d4
          v1 >>= 0x10L;
          v0 = v1 << 0x1L;
          v0 += v1;
          v1 = v0 << 0x4L;
          v0 += v1;
          v0 <<= 0x1L;
          v1 = v0 + s4;

          a2 = MEMORY.ref(2, v1).offset(0x36L).get();
          a1 = MEMORY.ref(2, v1).offset(0x02L).get();
          t2 = MEMORY.ref(2, v1).offset(0x40L).get();
          a3 = MEMORY.ref(2, v1).offset(0x38L).get();
          v0 = MEMORY.ref(2, v1).offset(0x14L).get();
          t3 = MEMORY.ref(2, v1).offset(0x3aL).get();

          if(v0 == s5 || MEMORY.ref(2, v1).offset(0x44L).get() == s5) {
            //LAB_80047220
            v1 = t2 & 0xffffL;
            v0 = a1 & 0xffffL;
            if(v0 < v1) {
              v0 = t2 - a1;

              //LAB_80047244
              t2 = v0 + 0x78L;
            } else {
              v1 = a1 - t2;
              v0 = 0x78L;
              t2 = v0 - v1;
            }

            //LAB_80047248
            v1 = s2 << 0x10L;
            v1 >>= 0x10L;
            v0 = v1 << 0x1L;
            v0 += v1;
            v1 = v0 << 0x4L;
            v0 += v1;
            v0 <<= 0x1L;
            a0 = v0 + s4;
            v0 = MEMORY.ref(2, a0).offset(0x14L).get();

            if(v0 != 0) {
              if(MEMORY.ref(2, s6).offset(0x42L).get() != 0x3cL) {
                //LAB_800472cc
                v1 = s2 << 0x10L;

                //LAB_800472d0
                v1 >>= 0x10L;
                v0 = v1 << 0x1L;
                v0 += v1;
                v1 = v0 << 0x4L;
                v0 += v1;
                v0 += s4;
                v1 = MEMORY.ref(2, v0).offset(0x3cL).get();
                a0 = MEMORY.ref(2, v0).offset(0x12L).get();
                v1 &= 0xfffL;
                a0 += v1;
                MEMORY.ref(2, v0).offset(0x12L).setu(a0);
              } else {
                v1 = MEMORY.ref(2, a0).offset(0x3cL).get();
                a1 = v1 & 0xfffL;
                if(a1 == 0x78L) {
                  v0 = v1 & 0xf000L;
                  if(v0 != 0) {
                    v0 -= 0x1000L;
                    v1 = MEMORY.ref(2, a0).offset(0x12L).get();
                    v0 |= a1;
                    MEMORY.ref(2, a0).offset(0x3cL).setu(v0);

                    v0 &= 0xfffL;
                    v1 += v0;
                    MEMORY.ref(2, a0).offset(0x12L).setu(v1);
                  } else {
                    //LAB_800472c0
                    v0 = v1 | 0x6000L;
                    MEMORY.ref(2, a0).offset(0x3cL).setu(v0);
                  }
                } else {
                  //LAB_800472cc
                  v1 = s2 << 0x10L;

                  //LAB_800472d0
                  v1 >>= 0x10L;
                  v0 = v1 << 0x1L;
                  v0 += v1;
                  v1 = v0 << 0x4L;
                  v0 += v1;
                  v0 += s4;
                  v1 = MEMORY.ref(2, v0).offset(0x3cL).get();
                  a0 = MEMORY.ref(2, v0).offset(0x12L).get();
                  v1 &= 0xfffL;
                  a0 += v1;
                  MEMORY.ref(2, v0).offset(0x12L).setu(a0);
                }
              }

              //LAB_80047300
              a0 = MEMORY.ref(2, s3).offset(0x20L).get();
              v0 = a0 << 0x1L;
              v0 += a0;
              v0 <<= 0x2L;
              v0 += v1;
              a0 = MEMORY.ref(4, v0).offset(0x4L).get();
              v0 = a0 & 0x3L;
              a3 = 0x80L;
              if(v0 != 0) {
                //LAB_800473d4
                v1 = s2 << 0x10L;
              } else {
                v0 = MEMORY.ref(4, a0).offset(0x18L).get();
                a1 = a0 + v0;
                _800c4ac0.setu(a0);
                if(a1 == v0) {
                  //LAB_800473d4
                  v1 = s2 << 0x10L;
                } else {
                  v0 = a1 & 0x1L;
                  v1 = s2 << 0x10L;
                  if(v0 == 0) {
                    v1 >>= 0x10L;
                    v0 = v1 << 0x1L;
                    v0 += v1;
                    v1 = v0 << 0x4L;
                    v0 += v1;
                    v0 <<= 0x1L;
                    a0 = v0 + s4;
                    v0 = MEMORY.ref(2, a0).offset(0x12L).get();
                    _800c4ab4.setu(a1);
                    _800c4ab8.setu(a1);
                    if(v0 >= 0xf0L) {
                      v0 = MEMORY.ref(2, a0).offset(0x3cL).get();
                      v0 &= 0xfffL;
                      v0 >>= 0x1L;
                      MEMORY.ref(2, a0).offset(0x12L).setu(v0);
                    }

                    //LAB_800473a0
                    v0 = MEMORY.ref(2, a0).offset(0x10L).get();
                    v1 = _800c4ab4.get();
                    a0 = MEMORY.ref(2, a0).offset(0x12L).get();
                    v0 <<= 0x1L;
                    v0 += v1;
                    a0 >>>= 0x2L;
                    v0 = MEMORY.ref(2, v0).offset(0x2L).get();
                    v1 = _800c4ab8.get();
                    v0 += a0;
                    v1 += v0;
                    a3 = MEMORY.ref(1, v1).get();
                    v1 = s2 << 0x10L;
                  }
                }
              }

              //LAB_800473d8
              v1 >>= 0x10L;
              v0 = v1 << 0x1L;
              v0 += v1;
              v1 = v0 << 0x4L;
              v0 += v1;
              v0 <<= 0x1L;
              a0 = v0 + s4;
              v0 = MEMORY.ref(2, a0).offset(0x1cL).get();
              a1 = MEMORY.ref(2, a0).offset(0x4eL).get();
              if(v0 == 0) {
                v0 = 0x40L;
                if(MEMORY.ref(2, a0).offset(0x38L).get() >= 0x40L) {
                  v0 = MEMORY.ref(2, a0).offset(0x38L).get();
                  v1 = MEMORY.ref(2, a0).offset(0x3aL).get();
                  v0 -= 0x40L;
                  v0 = v0 * v1 & 0xffffffffL;
                  v1 = v0;
                  if((int)v0 < 0) {
                    v1 = v0 + 0x3fL;
                  }

                  //LAB_80047438
                  v1 >>= 0x6L;
                  a1 += v1;
                  if((int)v0 < 0) {
                    v0 += 0x3L;
                  }

                  //LAB_80047448
                  v0 >>= 0x2L;
                  v1 += 0x4L;
                } else {
                  //LAB_80047454
                  v1 = MEMORY.ref(2, a0).offset(0x38L).get();
                  a0 = MEMORY.ref(2, a0).offset(0x3aL).get();
                  v0 -= v1;
                  v0 = v0 * a0 & 0xffffffffL;
                  v1 = v0;
                  if((int)v0 < 0) {
                    v1 = v0 + 0x3fL;
                  }

                  //LAB_80047474
                  a0 = v1 >> 0x6L;
                  a1 -= a0;
                  if((int)v0 < 0) {
                    v0 += 0x3L;
                  }

                  //LAB_80047484
                  v1 = v0 >> 0x2L;
                  v0 = a0 << 0x4L;
                }

                //LAB_8004748c
                v0 -= v1;
                a2 += v0;
                t3 = 0x1L;
              }

              //LAB_80047498
              v1 = s2 << 0x10L;
              v1 >>= 0x10L;
              v0 = v1 << 0x1L;
              v0 += v1;
              v1 = v0 << 0x4L;
              v0 += v1;
              v0 <<= 0x1L;
              v0 += s4;
              v0 = MEMORY.ref(2, v0).offset(0x16L).get();

              v1 = a3 * v0 & 0xffffffffL;

              a0 = 0x80808081;
              final long mult = v1 * a0;
              v0++;
              v0 >>= 0x1L;
              v0 -= 0x40L;
              t0 = mult >>> 0x20L;
              v1 += t0;
              v1 >>= 0x7L;
              a3 = v1 - v0;
            }

            //LAB_800474f0
            v1 = s2 << 0x10L;
            v1 >>= 0x10L;
            v0 = v1 << 0x1L;
            v0 += v1;
            v1 = v0 << 0x4L;
            v0 += v1;
            v0 <<= 0x1L;
            t0 = v0 + s4;
            v0 = MEMORY.ref(2, t0).offset(0x44L).get();
            v1 = s2 << 0x10L;

            if(v0 != 0 && MEMORY.ref(2, t0).offset(0x62L).get() != 0) {
              v0--;
              v1 &= 0x80L;
              MEMORY.ref(2, t0).offset(0x62L).setu(v0);

              if(v1 != 0) {
                a0 = v0 & 0xffffL;
                a1 = MEMORY.ref(2, t0).offset(0x64L).get();
                v0 = MEMORY.ref(1, t0).offset(0x60L).get();
                a0 = a1 - a0;
                v0 = -v0;
                v0 &= 0xffL;
                v0 = a0 * v0 & 0xffffffffL;
                v1 = v0 << 0x1L;
                v1 += v0;
                v1 <<= 0x6L;
                v0 = a1 << 0x4L;
                v0 -= a1;
                v0 <<= 0x3L;
                div = Math.floorDiv(v1, v0);

                //LAB_800475a4
                t1 = div;
                v1 = MEMORY.ref(2, t0).offset(0x60L).get();
                v0 = 0x100L - v1;
                a0 = a0 * v0 & 0xffffffffL;
                v1 = a0 * 0x66666667L >>> 0x20L;
                a0 >>= 0x1fL;
                v0 = v1 >> 0x2L;
                v0 -= a0;
                div = Math.floorDiv(v0, a1);

                //LAB_80047600
                v0 = div;
                v1 = MEMORY.ref(2, t0).offset(0x4eL).get();
                a0 = t1;
                a1 = v1 - v0;
                if(t1 < 0) {
                  a0 = t1 + 0xfL;
                }

                //LAB_80047618
                v0 = a0 >> 0x4L;
                v0 <<= 0x4L;
                v0 = t1 - v0;
                a2 -= v0;
              } else {
                //LAB_8004762c
                a1 = MEMORY.ref(2, t0).offset(0x64L).get();
                a0 = MEMORY.ref(2, t0).offset(0x62L).get();
                v0 = MEMORY.ref(2, t0).offset(0x60L).get();
                a0 = a1 - a0;
                a0 = a0 * v0 & 0xffffffffL;
                v0 = a1 * 120;
                v1 = a0 * 192;
                div = Math.floorDiv(v1, v0);

                //LAB_80047684
                t1 = div;
                v1 = a0 * 0x66666667L >> 0x20L;
                a0 >>= 0x1fL;
                v0 = v1 >> 0x2L;
                v0 -= a0;
                div = Math.floorDiv(v0, a1);

                //LAB_800476cc
                v0 = div;
                v1 = MEMORY.ref(2, t0).offset(0x4eL).get();
                a0 = t1;
                a1 = v1 + v0;
                if(t1 < 0) {
                  a0 = t1 + 0xfL;
                }

                //LAB_800476e4
                v0 = a0 >> 0x4L;
                v0 <<= 0x4L;
                v0 = t1 - v0;
                a2 += v0;
              }

              //LAB_800476f4
              v0 = a1 & 0xffffL;
              if(v0 < 0xdL) {
                a1 = 0xcL;
                v0 = 0xcL;
              }

              //LAB_8004770c
              v1 = s2 << 0x10L;
              if(v0 >= 0xf3L) {
                a1 = 0xf3L;
              }

              //LAB_8004771c
              v1 >>= 0x10L;
              v0 = v1 << 0x1L;
              v0 += v1;
              v1 = v0 << 0x4L;
              v0 += v1;
              v0 <<= 0x1L;
              v1 = v0 + s4;
              MEMORY.ref(1, s3).offset(0x11cL).setu(a1);

              v0 = MEMORY.ref(2, v1).offset(0x62L).get();
              if(v0 == 0) {
                MEMORY.ref(2, v1).offset(0x4eL).setu(a1);
                MEMORY.ref(2, v1).offset(0x44L).setu(0);
              }

              //LAB_80047754
              v1 = s2 << 0x10L;
            }
          }

          v1 = s2 << 0x10L;

          //LAB_80047758
          v1 >>= 0x10L;
          v0 = v1 << 0x1L;
          v0 += v1;
          v1 = v0 << 0x4L;
          v0 += v1;
          v0 <<= 0x1L;
          v0 += s4;

          if(MEMORY.ref(2, v0).offset(0x42L).get() == s5 || MEMORY.ref(1, s3).offset(0x104L).get() == s5) {
            //LAB_80047794
            s0 = MEMORY.ref(2, s3).offset(0xecL).get();
          } else {
            s0 = 0x1000L;
            //LAB_800477a0
          }

          //LAB_800477a4
          v0 = FUN_80048998(t2 & 0xffffL, a1 & 0xffffL, (short)a2, a3 & 0xffffL, t3);
          v0 &= 0xffffL;
          t4 = s0 * v0 & 0xffffffffL;
          SPU.voices[(short)s2 / 0x1000].pitch = (short)(t4 / 0x1000);
          v1 = (short)s2;
        }

        //LAB_800477ec
        v0 = v1 << 0x1L;
        v0 += v1;
        v1 = v0 << 0x4L;
        v0 += v1;
        v0 <<= 0x1L;
        v1 = v0 + s4;

        if(MEMORY.ref(2, v1).offset(0x1aL).get() == s5) {
          if(MEMORY.ref(2, v1).offset(0x46L).get() == s5 || MEMORY.ref(2, v1).offset(0x48L).get() == s5 || MEMORY.ref(1, s3).offset(0x105L).get() == s5) {
            //LAB_80047844
            v1 = s2 << 0x10L;

            //LAB_80047848
            v1 >>= 0x10L;
            v0 = v1 << 0x1L;
            v0 += v1;
            v1 = v0 << 0x4L;
            v0 += v1;
            v0 <<= 0x1L;
            s0 = v0 + s4;
            v0 = MEMORY.ref(2, s0).offset(0x46L).get();
            v1 = s2 << 0x10L;
            if(v0 != 0) {
              v1 = MEMORY.ref(2, s0).offset(0x50L).get();
              v0 = MEMORY.ref(2, s0).offset(0x52L).get();

              if(v1 != v0) {
                v0 = MEMORY.ref(2, s0).offset(0x54L).get();
                if(v0 != 0) {
                  a0 = MEMORY.ref(2, s0).offset(0x50L).get();
                  a1 = MEMORY.ref(2, s0).offset(0x52L).get();
                  a2 = MEMORY.ref(2, s0).offset(0x56L).get();
                  a3 = MEMORY.ref(2, s0).offset(0x54L).get();
                  v0 = FUN_8004af3c(a0, a1, a2, a3);
                  v1 = MEMORY.ref(2, s0).offset(0x54L).get();
                  v0 &= 0xffL;
                  MEMORY.ref(2, s0).offset(0x2cL).setu(v0);
                  v1--;
                  MEMORY.ref(2, s0).offset(0x54L).setu(v1);
                }

                //LAB_800478c8
                MEMORY.ref(2, s0).offset(0x2cL).setu(v1);
              }

              //LAB_800478cc
              MEMORY.ref(2, s0).offset(0x46L).setu(0);

              //LAB_800478d0
              v1 = s2 << 0x10L;
            }

            //LAB_800478d4
            v1 >>= 0x10L;
            v0 = v1 << 0x1L;
            v0 += v1;
            v1 = v0 << 0x4L;
            v0 += v1;
            v0 <<= 0x1L;
            s0 = v0 + s4;
            v0 = MEMORY.ref(2, s0).offset(0x48L).get();
            if(v0 != 0) {
              v1 = MEMORY.ref(2, s0).offset(0x58L).get();
              v0 = MEMORY.ref(2, s0).offset(0x5aL).get();

              if(v1 == v0) {
                MEMORY.ref(2, s0).offset(0x48L).setu(0);
              } else {
                //LAB_8004791c
                v0 = MEMORY.ref(2, s0).offset(0x5cL).get();
                if(v0 != 0) {
                  a0 = MEMORY.ref(1, s0).offset(0x58L).get();
                  a1 = MEMORY.ref(1, s0).offset(0x5aL).get();
                  a2 = MEMORY.ref(1, s0).offset(0x5eL).get();
                  a3 = MEMORY.ref(1, s0).offset(0x5cL).get();
                  v0 = FUN_8004af3c(a0, a1, a2, a3);
                  v1 = MEMORY.ref(2, s0).offset(0x5cL).get();
                  v0 &= 0xffL;
                  MEMORY.ref(2, s0).offset(0x4cL).setu(v0);
                  v1--;
                  MEMORY.ref(2, s0).offset(0x5cL).setu(v1);
                } else {
                  //LAB_8004795c
                  MEMORY.ref(2, s0).offset(0x4cL).setu(v1);
                  MEMORY.ref(2, s0).offset(0x48L).setu(0);
                }

                //LAB_80047964
                v0 = s2 << 0x10L;
                v0 >>= 0x10L;
                v1 = v0 << 0x1L;
                v1 += v0;
                v0 = v1 << 0x4L;
                v1 += v0;
                v1 <<= 0x1L;
                v1 += s4;
                v0 = MEMORY.ref(2, v1).offset(0x4cL).get();
                a1 = _80059f3c.getAddress();
                v0 >>>= 0x2L;
                v0 <<= 0x1L;
                v0 += a1;
                a0 = MEMORY.ref(1, v0).get();
                v0 = MEMORY.ref(2, v1).offset(0x4cL).get();
                v0 >>>= 0x2L;
                v0 <<= 0x1L;
                v0 += a1;
                MEMORY.ref(2, v1).offset(0x30L).setu(a0);
                v0 = MEMORY.ref(1, v0).offset(0x1L).get();
                MEMORY.ref(2, v1).offset(0x32L).setu(v0);
              }
            }

            //LAB_800479c4
            s0 = s2 & 0xffffL;
            s1 = FUN_8004ae94(s0, 0);
            v0 = FUN_8004ae94(s0, 0x1L);
            a0 = s2 << 0x10L >> 0x10L;
            v1 = a0 << 0x1L;
            v1 += a0;
            a0 = v1 << 0x4L;
            v1 += a0;
            v1 <<= 0x1L;
            v1 += s4;
            v1 = MEMORY.ref(2, v1).offset(0x42L).get();
            s0 = v0;
            if(v1 == s5 || MEMORY.ref(1, s3).offset(0x105L).get() == s5) {
              //LAB_80047a24
              s1 = FUN_8004b644(s1 & 0xffffL, MEMORY.ref(2, s3).offset(0xeeL).get());
              s0 = FUN_8004b644(s0 & 0xffffL, MEMORY.ref(2, s3).offset(0xf0L).get());
            }

            //LAB_80047a44
            if(MEMORY.ref(2, s6).offset(0x36L).get() != 0) {
              s0 = maxShort(s1 << 0x10L >> 0x10L, s0 << 0x10L >> 0x10L);
              s1 = s0;
            }

            //LAB_80047a6c
            SPU.voices[(short)s2 >> 12].volumeLeft.set(s1);
            SPU.voices[(short)s2 >> 12].volumeRight.set(s0);

            //LAB_80047a88
            v1 = s2 << 0x10L;
            v1 >>= 0x10L;
          }
        } else {
          //LAB_80047a88
          v1 = s2 << 0x10L;
          v1 >>= 0x10L;
        }

        //LAB_80047a90
        v0 = v1 * 3;
        v1 = v0 << 0x4L;
        v0 += v1;
        v0 <<= 0x1L;
        v0 += s4;
        v1 = MEMORY.ref(2, v0).offset(0x6L).get();
        v0 = v1 * 72;
        v0 += v1;
        v0 <<= 0x2L;
        s3 -= v0;
        v0 = s2 + 0x1L;
      }

      //LAB_80047acc
      s2 = v0;
      v0 <<= 0x10L;
      v0 >>= 0x10L;
      v1 = s2 << 0x10L;
    } while(v0 < 0x18L);
  }

  @Method(0x80047b38L)
  public static void FUN_80047b38(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80047bd0L)
  public static long FUN_80047bd0(final long voiceIndex) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x800486d4L)
  public static void FUN_800486d4(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x800488d4L)
  public static void FUN_800488d4(final long a0, final long a1) {
    assert false;
    //TODO
  }

  @Method(0x80048998L)
  public static long FUN_80048998(final long a0, final long a1, final long a2, final long a3, final long a4) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x80048eb8L)
  public static void FUN_80048eb8(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80048f98L)
  public static void FUN_80048f98(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80048fecL)
  public static void FUN_80048fec(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x8004906cL)
  public static void FUN_8004906c(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80049250L)
  public static void FUN_80049250(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80049480L)
  public static void FUN_80049480(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80049638L)
  public static void FUN_80049638(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80049980L)
  public static void FUN_80049980(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80049cbcL)
  public static void FUN_80049cbc(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80049e2cL)
  public static void FUN_80049e2c(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x80049f14L)
  public static void FUN_80049f14(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x8004a2c0L)
  public static void FUN_8004a2c0(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x8004a34cL)
  public static void FUN_8004a34c(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x8004a5e0L)
  public static void FUN_8004a5e0(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x8004a7ecL)
  public static void FUN_8004a7ec(final long voiceIndex) {
    assert false;
    //TODO
  }

  @Method(0x8004a8b8L)
  public static void FUN_8004a8b8() {
    final long t3 = _800c6630.getAddress();
    final long t0 = _800c3a40.getAddress();
    long a1 = _800c4ac8.getAddress() + 0x29L;

    //LAB_8004a8dc
    for(int a3 = 0; a3 < 0x18; a3++) {
      Outer:
      if(MEMORY.ref(1, a1).offset(0xbeL).get() == 0x1L && MEMORY.ref(1, a1).get() == 0x1L) {
        //LAB_8004a908
        for(int i = 0; i < 0x18; i++) {
          if(MEMORY.ref(2, t0).offset(i * 0x66L).offset(0x6L).get() == a3) {
            break Outer;
          }
        }

        MEMORY.ref(4, a1).offset(0xefL).setu(0);
        MEMORY.ref(1, a1).offset(0xbeL).setu(0);
        MEMORY.ref(2, a1).offset(0xc7L).setu(0);
        MEMORY.ref(2, a1).offset(0xc5L).setu(0);
        MEMORY.ref(1, a1).offset(0xdbL).setu(0);
        MEMORY.ref(1, a1).offset(0xdcL).setu(0);
        MEMORY.ref(2, a1).offset(-0x9L).setu(0);
        MEMORY.ref(1, a1).offset(0x01L).setu(0);
        MEMORY.ref(1, a1).offset(0x00L).setu(0);
        MEMORY.ref(1, t3).offset(0x22L).setu(0);
      }

      //LAB_8004a96c
      a3++;
      a1 += 0x124L;
    }

    final long t2 = _800c3a40.getAddress();

    //LAB_8004a99c
    for(int i = 0; i < 0x18L; i++) {
      if((SPU.voices[i].adsrVolume & 0x7fffL) < 0x10L) {
        if(MEMORY.ref(2, t2).offset(i * 0x66L).offset(0x8L).get() == 0x1L) {
          if(MEMORY.ref(2, t2).offset(i * 0x66L).offset(0x1aL).get() != 0) {
            if(MEMORY.ref(1, t3).offset(0xdL).get() > 0) {
              MEMORY.ref(1, t3).offset(0xdL).subu(0x1L);
            }
          }

          //LAB_8004aa04;
          //LAB_8004aa0c
          for(int n = 0; n < 0x18; n++) {
            final long a4 = MEMORY.ref(2, t2).offset(n * 0x66L).offset(0xaL).get();
            if(a4 > MEMORY.ref(2, t2).offset(n * 0x66L).offset(0xaL).get() && a4 != 0xffffL) {
              MEMORY.ref(2, t2).offset(n & 0x66L).offset(0xaL).setu(a4 - 0x1L);
            }
          }

          //LAB_8004aa7c
          for(int n = 0; n < 0x33; n++) {
            MEMORY.ref(2, t2).offset(n * 0x66L).offset(n * 2L).setu(0);
          }

          MEMORY.ref(2, t2).offset(i * 0x66L).offset(0x06L).setu(0xffffL);
          MEMORY.ref(2, t2).offset(i * 0x66L).offset(0x26L).setu(0xffffL);
          MEMORY.ref(2, t2).offset(i * 0x66L).offset(0x24L).setu(0xffffL);
          MEMORY.ref(2, t2).offset(i * 0x66L).offset(0x22L).setu(0xffffL);
          MEMORY.ref(2, t2).offset(i * 0x66L).offset(0x0aL).setu(0xffffL);
          MEMORY.ref(2, t2).offset(i * 0x66L).offset(0x4eL).setu(0x78L);

          if(MEMORY.ref(1, t3).get() > 0) {
            MEMORY.ref(1, t3).subu(0x1L);
          }
        }
      }

      //LAB_8004aaec
    }
  }

  @Method(0x8004ab08L)
  public static void registerSpuDmaCallback(final long callback) {
    SetDmaInterruptCallback(DmaChannelType.SPU, callback);
  }

  @Method(0x8004ab2cL)
  public static void spuDmaCallback() {
    SPU_CTRL_REG_CPUCNT.and(0xffcfL);

    spuDmaTransferInProgress_800c6650.set(false);

    final SpuDmaTransfer firstTransfer = queuedSpuDmaTransferArray_800c49d0.get(0);

    //LAB_8004ab5c
    do {
      if(spuDmaIndex_800c6669.get() == 0) {
        //LAB_8004acfc
        if(_800c6668.get() != 0) {
          _800c6628.deref().run();
        }

        return;
      }

      if((firstTransfer.size.get() & 0x7fffffffL) != 0) {
        break;
      }

      //LAB_8004aba8
      for(int i1 = 0; i1 < spuDmaIndex_800c6669.get() - 1; i1++) {
        queuedSpuDmaTransferArray_800c49d0.get(i1).set(queuedSpuDmaTransferArray_800c49d0.get(i1 + 1));
      }

      //LAB_8004ac18
      spuDmaIndex_800c6669.subu(0x1L);
    } while(true);

    //LAB_8004ac2c
    if((firstTransfer.size.get() & 0x8000_0000) == 0) {
      //LAB_8004ac48
      spuDmaTransfer(DmaChannel.ChannelControl.TRANSFER_DIRECTION.FROM_MAIN_RAM, firstTransfer.ramAddress.get(), Math.abs(firstTransfer.size.get()), firstTransfer.soundBufferAddress.get());
    } else {
      spuDmaTransfer(DmaChannel.ChannelControl.TRANSFER_DIRECTION.TO_MAIN_RAM, firstTransfer.ramAddress.get(), Math.abs(firstTransfer.size.get()), firstTransfer.soundBufferAddress.get());
    }

    //LAB_8004ac78
    for(int i1 = 0; i1 < spuDmaIndex_800c6669.get() - 1; i1++) {
      queuedSpuDmaTransferArray_800c49d0.get(i1).set(queuedSpuDmaTransferArray_800c49d0.get(i1 + 1));
    }

    //LAB_8004ace8
    spuDmaIndex_800c6669.subu(0x1L);

    //LAB_8004ad1c
  }

  @Method(0x8004ad2cL)
  public static void FUN_8004ad2c(final long a0) {
    assert false;
    //TODO verify
  }

  @Method(0x8004ae94L)
  public static long FUN_8004ae94(final long a0, final long a1) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x8004af3cL)
  public static long FUN_8004af3c(long a0, long a1, long a2, long a3) {
    final long v0 = a0 & 0xffL;
    a1 &= 0xffL;
    a1 -= v0;
    a2 &= 0xffL;
    a3 &= 0xffL;

    //LAB_8004af84
    a0 += Math.floorDiv(a1 * a3, a2);
    return a0 & 0xffL;
  }

  @Method(0x8004af98L)
  public static void FUN_8004af98(final long voiceIndex) {
    long v0;
    long v1;

    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    long s4;

    s2 = _800c4ac8.getAddress();
    v1 = voiceIndex;
    v0 = v1 << 0x3L;
    v0 += v1;
    v0 <<= 0x3L;
    v0 += v1;
    v0 <<= 0x2L;
    s2 += v0;

    v0 = MEMORY.ref(1, s2).offset(0x3cL).get();
    s4 = 0;
    if(v0 == 0) {
      return;
    }

    v0 = MEMORY.ref(1, s2).offset(0x8eL).get();
    if(v0 == 0) {
      s1 = 0;
    } else {
      v1 = MEMORY.ref(4, s2).offset(0x10L).get();
      _800c667c.setu(v1);

      long a0 = MEMORY.ref(1, s2).offset(0x9eL).get();
      v0 = MEMORY.ref(1, v1).get();
      s1 = 0;
      if(v0 != a0) {
        v0 = MEMORY.ref(1, s2).offset(0xaeL).get();
        if(v0 != 0) {
          a0 &= 0xffL;
          a1 = MEMORY.ref(1, s2).offset(0xceL).get();
          a2 = MEMORY.ref(1, s2).offset(0xbeL).get();
          a3 = v0;
          v0 = FUN_8004af3c(a0, a1, a2, a3);
          v1 = _800c667c.get();
          MEMORY.ref(1, v1).setu(v0);
          MEMORY.ref(1, s2).offset(0xaeL).subu(0x1L);
        } else {
          //LAB_8004b064
          MEMORY.ref(1, v1).setu(a0);
        }

        //LAB_8004b068
        a0 = voiceIndex << 0x10L;
        v0 = _800c667c.get();
        a0 >>= 0x10L;
        a1 = MEMORY.ref(1, v0).get();
        s4++;
        FUN_8004c8dc(a0, a1);

        //LAB_8004b084
        s1 = 0;
      }
    }

    //LAB_8004b088
    v0 = MEMORY.ref(4, s2).offset(0x10L).get() + 0x10L;
    _800c6680.setu(v0);
    v0 = s1 & 0xffL;

    //LAB_8004b0a0
    do {
      s0 = s2 + v0;
      a1 = _800c6680.get();
      long a0 = MEMORY.ref(1, s0).offset(0x4eL).get();
      v0 = MEMORY.ref(1, a1).offset(0x3L).get();
      if(v0 != a0) {
        v1 = MEMORY.ref(1, s0).offset(0x3eL).get();
        if(v1 == 0x1L) {
          v0 = MEMORY.ref(1, s0).offset(0x5eL).get();
          if(v0 != 0) {
            a0 &= 0xffL;
            a1 = MEMORY.ref(1, s0).offset(0x7eL).get();
            a2 = MEMORY.ref(1, s0).offset(0x6eL).get();
            a3 = v0;
            v0 = FUN_8004af3c(a0, a1, a2, a3);
            v1 = _800c6680.get();
            MEMORY.ref(1, v1).offset(0x3L).setu(v0);

            MEMORY.ref(1, s0).offset(0x5eL).subu(0x1L);
          } else {
            //LAB_8004b110
            MEMORY.ref(1, a1).offset(0x3L).setu(a0);
          }

          //LAB_8004b114
          a0 = voiceIndex << 0x10L;
          a0 >>= 0x10L;
          v0 = _800c6680.get();
          a1 = s1 & 0xffL;
          a2 = MEMORY.ref(1, v0).offset(0x3L).get();
          s4++;
          FUN_8004b464(a0, a1, a2);
        }
      }

      //LAB_8004b130
      v0 = _800c6680.get();
      s1 += 0x1L;
      v0 += 0x10L;
      _800c6680.setu(v0);

      v0 = s1 & 0xffL;
    } while(v0 < 0x10L);

    v0 = s4 << 0x10;
    s1 = 0;
    if(v0 == 0) {
      //LAB_8004b15c
      do {
        v1 = 0;
        v0 = s1 & 0xffL;
        long a0 = v0 << 0x4L;
        v0 = v1 & 0xffL;

        //LAB_8004b16c
        do {
          v0 += a0;
          v0 += s2;
          MEMORY.ref(1, v0).offset(0x3eL).setu(0);
          v1++;
          v0 = v1 & 0xffL;
        } while(v0 < 0x10L);

        s1++;
        v0 = s1 & 0xffL;
      } while(v0 < 0xaL);

      v0 = MEMORY.ref(1, s2).offset(0x3aL).get();
      long a0 = voiceIndex << 0x10L;
      if(v0 != 0) {
        MEMORY.ref(1, s2).offset(0x3aL).setu(0);
        a0 >>= 0x10L;
        a1 = 0x1L;
        FUN_8004d034(a0, a1);
      }

      //LAB_8004b1c0
      MEMORY.ref(1, s2).offset(0x3cL).setu(0);
    }

    //LAB_8004b1c4
  }

  @Method(0x8004b2c4L)
  public static void FUN_8004b2c4() {
    long v0;
    long v1;

    long a0;
    long a1;

    if(_800c665a.get() != 0) {
      if(_8005a1ce.get() == _800c665c.get()) {
        _8005a1ce.setu(0);
        _800c665a.setu(0);
      } else {
        //LAB_8004b310
        v1 = _800c665e.get();
        v0 = _8005a1ce.get();

        v1 = v1 * v0 & 0xffffffffL;
        a0 = _800c665c.get();

        //LAB_8004b354
        a0 = Math.floorDiv(v1, a0);
        v0++;
        _8005a1ce.setu(v0);
        a0 <<= 0x10L;
        a0 >>= 0x10L;
        setMainVolume(a0, a0);
      }
    }

    //LAB_8004b370
    if(_800c665b.get() != 0) {
      if(_8005a1ce.get() == _800c665c.get()) {
        _8005a1ce.setu(0);
        _800c665b.setu(0);
      } else {
        //LAB_8004b3a0
        a1 = _800c665c.get();
        v0 = _800c6660.get();
        v1 = a1 - _8005a1ce.get();
        v0 = v0 * v1 & 0xffffffffL;

        a0 = Math.floorDiv(v0, a1);
        _800c6662.setu(v0);

        v0 = v0 * v1 & 0xffffffffL;

        a1 = Math.floorDiv(v0, a1);
        a0 <<= 0x10L;
        a0 >>= 0x10L;
        a1 <<= 0x10L;
        a1 >>= 0x10L;
        setMainVolume(a0, a1);

        _8005a1ce.addu(0x1L);
      }
    }

    //LAB_8004b450
  }

  @Method(0x8004b464L)
  public static void FUN_8004b464(final long a0, final long a1, final long a2) {
    assert false;
    //TODO
  }

  @Method(0x8004b5bcL)
  public static long maxShort(final long a, final long b) {
    return Math.max((short)(a & 0xffffL), (short)(b & 0xffffL));
  }

  @Method(0x8004b644L)
  public static long FUN_8004b644(final long a0, final long a1) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x8004b694L)
  public static long spuDmaTransfer(final DmaChannel.ChannelControl.TRANSFER_DIRECTION transferDirection, final long dmaAddress, final long dmaSize, final long addressInSoundBuffer) {
    final long soundRamTransferMode;
    final long spuDelay;
    if(transferDirection == DmaChannel.ChannelControl.TRANSFER_DIRECTION.FROM_MAIN_RAM) {
      //LAB_8004b6e0
      soundRamTransferMode = 0b10_0000L; //DMAwrite
      spuDelay = 0x2000_0000L;
    } else {
      soundRamTransferMode = 0b11_0000L; //DMAread
      spuDelay = 0x2200_0000L;
    }

    //LAB_8004b6ec
    final long oldSpuCtrl = SPU_CTRL_REG_CPUCNT.get();

    SOUND_RAM_DATA_TRANSFER_ADDR.setu(addressInSoundBuffer / 8L);
    spuDmaTransferInProgress_800c6650.set(true);

    // Apply new RAM transfer mode
    SPU_CTRL_REG_CPUCNT.setu(oldSpuCtrl & 0b1111_1111_1100_1111L | soundRamTransferMode);
    SPU_DELAY.and(0xf0ffffffL).or(spuDelay);

    DMA.spu.MADR.setu(dmaAddress);

    // Block size
    DMA.spu.BCR.setu(dmaSize / 64 << 16 | 0x10L);

    // Sync mode: sync blocks to DMA request
    // Start/busy: start/enable/busy
    DMA.spu.CHCR.setu(0b1_0000_0000_0000_0010_0000_0000L | transferDirection.ordinal());

    return dmaSize / 64;
  }

  @Method(0x8004b7c0L)
  public static long FUN_8004b7c0(final long a0) {
    if(a0 < 0x1L) {
      _8005a1d0.setu(a0);
    }

    //LAB_8004b7d0
    return _8005a1d0.get();
  }

  @Method(0x8004b7e0L)
  public static long FUN_8004b7e0() {
    //LAB_8004b7fc
    for(int s0 = 0; s0 < 0x8_9d00; s0++) {
      if(FUN_8004b7c0(0x2L) != 0) {
        return 0;
      }
    }

    return 0x1L;
  }

  @Method(0x8004b834L)
  public static void FUN_8004b834() {
    // Pre-optimisation
//    SPU_BASE_ADDRESS.offset(4, 0x0L).setu(_80011db0);
//    SPU_BASE_ADDRESS.offset(4, 0x4L).setu(_80011db4);
//    SPU_BASE_ADDRESS.offset(4, 0x8L).setu(_80011db8);
//    SPU_BASE_ADDRESS.offset(4, 0xcL).setu(_80011dbc);

    // Zero SPU stuff
    //LAB_8004b8ac
    // Pre-optimisation
//    MEMORY.zero(VOICE_00_LEFT.getAddress(), 0x200);

    SOUND_RAM_DATA_TRANSFER_CTRL.setu(0b100L); // Normal

    EnterCriticalSection();
    registerSpuDmaCallback(getMethodAddress(Scus94491BpeSegment_8004.class, "spuDmaCallback"));

    final int eventId = OpenEvent(HwSPU, EvSpCOMP, EvMdNOINTR, 0);
    eventSpuIrq_800c664c.setu(eventId);
    EnableEvent(eventId);
    ExitCriticalSection();

    DMA.spu.enable();
    DMA.spu.setPriority(3);

    _800c6633.setu(0x8L);
    _800c6630.setu(0);
    _800c663d.setu(0);
    _800c6672.setu(0x3cL);

    // SPU enable; unmute
    SPU_CTRL_REG_CPUCNT.setu(0b1100_0000_0000_0000L);

    // Pre-optimisation
//    queueRamToSpuDmaTransfer(SPU_BASE_ADDRESS.getAddress(), 0x1010L, 0x10L);

    SPU.directWrite(0x1010, _80011db0.getAddress(), 0x10);

    //LAB_8004b9c0
    while(isSpuDmaTransferInProgress()) {
      DebugHelper.sleep(1);
    }

    //LAB_8004b9e8
    for(final Voice voice : SPU.voices) {
      voice.reset();
    }

    //TODO see no$ SPU voice flags 1F801D88h - setting this needs to do some stuff
    // Start attack/decay/sustain (ADSR) envelope; automatically initializes ADSR Volume to zero, and copies Voice Start Address to Voice Repeat Address
    SPU_VOICE_KEY_ON.setu(0xff_ffffL);

    // Start release
    SPU_VOICE_KEY_OFF.setu(0xff_ffffL);

    //LAB_8004ba58
    for(int a1 = 0; a1 < 24; a1++) {
      //LAB_8004ba74
      for(int a0 = 0; a0 < 50; a0++) {
        _800c3a40.offset(a1 * 102).offset(a0 * 2).setu(0);
      }
    }

    //LAB_8004bab8
    for(int a1 = 0; a1 < 127; a1++) {
      //LAB_8004bacc
      for(int a0 = 0; a0 < 3; a0++) {
        _800c43d0.offset(a1 * 12).offset(a0 * 4).setu(0);
      }
    }

    //LAB_8004bb14
    for(int i = 0; i < 24; i++) {
      _800c3a40.offset(0x4eL).offset(i * 102).setu(120);
    }
  }

  @Method(0x8004be6cL)
  public static boolean isSpuDmaTransferInProgress() {
    return spuDmaTransferInProgress_800c6650.get();
  }

  @Method(0x8004be7cL)
  public static void FUN_8004be7c(final long a0) {
    if(a0 == 0) {
      _800c6668.setu(0);
    } else {
      _800c6628.set(MEMORY.ref(4, a0).cast(RunnableRef::new));
      _800c6668.setu(0x1L);
    }
  }

  @Method(0x8004bea4L)
  public static long FUN_8004bea4(final long dmaAddress, final long a1, final long addressInSoundBuffer) {
    if((dmaAddress & 0x3L) != 0) {
      return 0xffffL;
    }

    if((a1 & 0x3L) != 0) {
      return 0xffffL;
    }

    if(addressInSoundBuffer > 0x8_0000L) {
      return 0xffffL;
    }

    if((addressInSoundBuffer & 0xfL) != 0) {
      return 0xffffL;
    }

    if(MEMORY.ref(4, a1).offset(0xcL).get() != 0x6468_5353L) { // SShd
      return 0xffffL;
    }

    final long dmaSize = MEMORY.ref(4, a1).offset(0x4L).get();

    if(spuDmaTransferInProgress_800c6650.get()) {
      if(spuDmaIndex_800c6669.get(0xffL) >= 0x20L) {
        //LAB_8004bfa4
        return 0xffffL;
      }

      final SpuDmaTransfer queuedTransfer = queuedSpuDmaTransferArray_800c49d0.get((int)spuDmaIndex_800c6669.get());
      queuedTransfer.ramAddress.setu(dmaAddress);
      queuedTransfer.size.setu(dmaSize);
      queuedTransfer.soundBufferAddress.setu(addressInSoundBuffer);
      spuDmaIndex_800c6669.addu(0x1L);
    } else {
      //LAB_8004bfac
      if(dmaSize == 0) {
        //LAB_8004bfdc
        spuDmaTransferInProgress_800c6650.set(false);
      } else {
        spuDmaTransfer(DmaChannel.ChannelControl.TRANSFER_DIRECTION.FROM_MAIN_RAM, dmaAddress, dmaSize, addressInSoundBuffer);
      }
    }

    //LAB_8004bfe0
    //LAB_8004bfe4
    //LAB_8004bff0
    for(int i = 0; i < 0x7f; i++) {
      if(_800c43d0.offset(i * 12).get() == 0) {
        //LAB_8004bfc8
        _800c43d0.offset(i * 12).setu(0x1L);
        _800c43d4.offset(i * 12).setu(a1);
        _800c43d8.offset(i * 12).setu(addressInSoundBuffer >>> 0x3L);
        break;
      }
    }

    //LAB_8004c024
    return (short)dmaAddress;
  }

  @Method(0x8004c114L)
  public static long FUN_8004c114(final long a0) {
    if((a0 & 0xff80L) != 0) {
      return -0x1L;
    }

    final long v0 = _800c43d0.offset(a0 * 12).getAddress();

    if(MEMORY.ref(4, v0).get() != 0x1L) {
      return -0x1L;
    }

    //LAB_8004c160
    for(int i = 0; i < 0x18; i++) {
      if(_800c3a40.offset(i * 0x66L).get() == 0x1L && _800c3a62.offset(i * 0x66L).get() == a0) {
        return -0x1L;
      }
    }

    _800c43d0.offset(a0 * 12L).setu(0);
    _800c43d4.offset(a0 * 12L).setu(0);
    _800c43d8.offset(a0 * 12L).setu(0);
    return 0;
  }

  @Method(0x8004c390L)
  public static long FUN_8004c390(final long a0) {
    final long v0 = _800c4af0.offset(a0 * 292).get();

    if(v0 == 0) {
      //LAB_8004c3d0
      _800c4ac8.offset(a0 * 292).setu(0);
      return 0;
    }

    //LAB_8004c3e4
    return v0 - 1;
  }

  @Method(0x8004c3f0L)
  public static long FUN_8004c3f0(final long a0) {
    if(a0 >= 0x19L) {
      return -0x1L;
    }

    long a1 = 0;

    //LAB_8004c420
    for(int a2 = 0; a2 < 0x18; a2++) {
      if(_800c3a40.offset(a2 * 102L).offset(0x1aL).get() != 0) {
        a1++;
      }

      a2++;
    }

    if(a0 < a1) {
      //LAB_8004c484
      return -0x1L;
    }

    _800c6633.setu(a0);

    //LAB_8004c488
    return 0;
  }

  @Method(0x8004c494L)
  public static void FUN_8004c494(final long a0) {
    _800c6664.setu(a0);

    if(a0 != 0) {
      SPU_VOICE_CHN_REVERB_MODE.setu(0);
      SPU_CTRL_REG_CPUCNT.oru(0x80L);
      SOUND_RAM_REVERB_WORK_ADDR.setu(_80059f7c.offset((a0 - 0x1L) * 66));

      // Clear out reverb registers
      //LAB_8004c4fc
      for(int a2 = 0; a2 < 0x20; a2++) {
        SPU.reverb[a2] = (short)_80059f7e.offset((a0 - 0x1L) * 66).offset(a2 * 2L).get();
        a2++;
      }

      return;
    }

    //LAB_8004c538
    SPU_VOICE_KEY_ON.setu(0);
    SPU_REVERB_OUT_L.setu(0);
    SPU_REVERB_OUT_R.setu(0);
    SPU_CTRL_REG_CPUCNT.addu(0xff7fL);
  }

  @Method(0x8004c558L)
  public static void FUN_8004c558(final long left, final long right) {
    if(_800c6664.get() == 0) {
      return;
    }

    if(left >= 0x80L || right >= 0x80L) {
      return;
    }

    final long l;
    final long r;
    if(spuMono_800c6666.get() == 0) {
      l = left << 0x8L;
      r = right << 0x8L;
    } else {
      l = maxShort(left << 0x18L >> 0x10L, right << 0x18L >> 0x10L);
      r = l;
    }

    //LAB_8004c5d0
    SPU_REVERB_OUT_L.setu(l);
    SPU_REVERB_OUT_R.setu(r);

    //LAB_8004c5d8
  }

  @Method(0x8004c894L)
  public static void setMainVolume(final long left, final long right) {
    final long l;
    if((left & 0x80L) == 0) {
      l = left << 0x7L;
    } else {
      l = (left << 0x7L) + 0x7fffL;
    }

    final long r;
    if((right & 0x80L) == 0) {
      r = right << 0x7L;
    } else {
      r = (right << 0x7L) + 0x7fffL;
    }

    LOGGER.info("Main volume set to %04x, %04x", l, r);

    SPU_MAIN_VOL_L.setu(l);
    SPU_MAIN_VOL_R.setu(r);
  }

  @Method(0x8004c8dcL)
  public static long FUN_8004c8dc(long a0, long a1) {
    long s0;

    final long a3 = a0;
    final long a2 = a1;
    a0 = (short)a0;
    a1 = (short)a1;

    if(a0 >= 0x18L || a1 >= 0x80L || _800c4ac8.offset(a0 * 292L).offset(0x27L).get() == 0) {
      //LAB_8004ca64
      return -0x1L;
    }

    final long v1 = _800c4ac8.offset(4, a0 * 292L).offset(0x10L).get();
    _800c667c.setu(v1);
    final long s2 = MEMORY.ref(1, v1).get();
    MEMORY.ref(1, v1).setu(a2);
    _800c6680.setu(_800c4ac8.offset(4, a0 * 292L).offset(0x10L).get() + 0x10L);

    //LAB_8004c97c
    for(s0 = 0; s0 < 16; s0++) {
      _800c6680.deref(1).offset(0xeL).setu((_800c6680.deref(1).offset(0x3L).get() * a1 & 0xffff_ffffL) >> 0x7L);
      _800c6680.addu(0x10L);
    }

    //LAB_8004c9d8
    for(s0 = 0; s0 < 0x18; s0++) {
      if(_800c3a40.offset(s0 * 102).get() == 0x1L) {
        if(_800c3a40.offset(s0 * 102).offset(0x22L).get() == _800c4ac8.offset(2, a0 * 292L).offset(0x20L).get()) {
          if(_800c3a40.offset(s0 * 102).offset(0x1aL).get() == 0) {
            if(_800c3a40.offset(s0 * 102).offset(0x6L).get() == (short)a3) {
              FUN_8004ad2c(s0);
            }
          }
        }
      }

      //LAB_8004ca44
    }

    //LAB_8004ca6c
    return (short)s2;
  }

  @Method(0x8004ccb0L)
  public static long FUN_8004ccb0(final long a0, final long a1) {
    if((short)a0 < 0x100L && (short)a1 < 0x80L && _800c665b.get() == 0) {
      setMainVolume(0, 0);
      setMainVolume(0x7f, 0x7f); //TODO temp
      _800c665a.setu(0x1L);
      _800c665c.setu(a0);
      _800c665e.setu(a1);
      return 0;
    }

    //LAB_8004cd30
    //LAB_8004cd34
    return -0x1L;
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

  /**
   * TODO verify (there's absolutely no way I got this right)
   */
  @Method(0x8004d034L)
  public static void FUN_8004d034(long a0, final long a1) {
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long v0;
    long v1;
    long a2;

    s5 = 0;
    s1 = _800c4ac8.offset(a0 * 292).getAddress();
    v0 = _800c43d0.offset(MEMORY.ref(2, s1).offset(0x20L).get() * 12).getAddress();
    v1 = MEMORY.ref(4, v0).offset(0x4L).get();
    _800c4ac0.setu(v1);
    a2 = MEMORY.ref(1, s1).offset(0x27L).get();
    s3 = a0;
    if(a2 != 0x1L) {
      return;
    }

    if(MEMORY.ref(4, v1).offset(0x10L).getSigned() == -0x1L) {
      return;
    }

    v1 = MEMORY.ref(2, s1).offset(0x20L).get();
    a0 = _800c43d0.offset(v1 * 12).get();
    if(a2 != a0) {
      return;
    }

    if(a1 == a0) {
      //LAB_8004d134
      s5 = 0x1L;

      //LAB_8004d13c
      MEMORY.ref(4, s1).offset(0xcL).setu(0x110L);
      MEMORY.ref(1, s1).offset(0x28L).setu(0);
      MEMORY.ref(4, s1).offset(0x18L).setu(a0);
      MEMORY.ref(1, s1).offset(0xe8L).setu(0);
      MEMORY.ref(1, s1).offset(0x35L).setu(0);
      s0 = 0;
    } else if(a1 < 0x2L) {
      if(a1 != 0) {
        return;
      }

      //LAB_8004d13c
      MEMORY.ref(4, s1).offset(0xcL).setu(0x110L);
      MEMORY.ref(1, s1).offset(0x28L).setu(0);
      MEMORY.ref(4, s1).offset(0x18L).setu(a0);
      MEMORY.ref(1, s1).offset(0xe8L).setu(0);
      MEMORY.ref(1, s1).offset(0x35L).setu(0);
      s0 = 0;
    } else {
      //LAB_8004d11c
      if(a1 != 0x2L) {
        if(a1 == 0x3L) {
          //LAB_8004d188
          if(MEMORY.ref(1, s1).offset(0x28L).get() == 0) {
            if(MEMORY.ref(4, s1).offset(0x18L).get() != a0) {
              MEMORY.ref(1, s1).offset(0x28L).setu(a0);

              //LAB_8004d1ac
              MEMORY.ref(1, s1).offset(0xe8L).setu(0);
            }
          } else {
            //LAB_8004d1b4
            MEMORY.ref(1, s1).offset(0x28L).setu(0);
            MEMORY.ref(1, s1).offset(0xe8L).setu(a0);
          }
        }

        return;
      }

      //LAB_8004d154
      if(MEMORY.ref(1, s1).offset(0x28L).get() == 0) {
        //LAB_8004d170
        s0 = 0;
        if(MEMORY.ref(4, s1).offset(0x18L).get() != a0) {
          MEMORY.ref(1, s1).offset(0x28L).setu(a0);
          //LAB_8004d1ac
          MEMORY.ref(1, s1).offset(0xe8L).setu(0);
          return;
        }
      } else {
        MEMORY.ref(1, s1).offset(0x28L).setu(0);
        MEMORY.ref(1, s1).offset(0xe8L).setu(a0);

        //LAB_8004d1c0
        s0 = 0;
      }
    }

    //LAB_8004d1c4
    s4 = (short)s3;
    s2 = s0 & 0xffffL;

    //LAB_8004d1d8
    do {
      v1 = _800c3a40.offset(s2 * 102).getAddress();
      if(MEMORY.ref(2, v1).offset(0x6L).get() == s4) {
        if(MEMORY.ref(2, v1).offset(0x1aL).get() == 0) {
          MEMORY.ref(2, v1).offset(0x8L).setu(0x1L);
          MEMORY.ref(2, v1).setu(0);
          MEMORY.ref(2, v1).offset(0x38L).setu(0x40L);
          MEMORY.ref(2, v1).offset(0x14L).setu(0);
          MEMORY.ref(2, v1).offset(0x16L).setu(0);
          FUN_800488d4(s3, s2);

          if(s5 != 0) {
            v1 = _800c4ac4.get() + s2 * 16;
            MEMORY.ref(2, v1).offset(0x8L).setu(0);
            MEMORY.ref(2, v1).offset(0xaL).setu(0);
          }
        }
      }

      //LAB_8004d254
      s0++;
      v0 = s0 & 0xffffL;
      s2 = s0 & 0xffffL;
    } while(v0 < 0x18L);

    s0 = 0;
    v0 = MEMORY.ref(4, s1).offset(0x10L).get();
    v0 += 0x10L;
    _800c6680.setu(v0);

    //LAB_8004d27c
    do {
      v0 = _800c6680.get();
      MEMORY.ref(1, v0).offset(0x9L).setu(0);
      v0 = _800c6680.get();
      v0 += 0x10L;
      _800c6680.setu(v0);
      s0++;
      v0 = s0 & 0xffffL;
    } while(v0 < 0x10L);

    _800c4ac4.deref(2).offset(0x18cL).setu(MEMORY.ref(2, s1).offset(0xe2L));
    _800c4ac4.deref(2).offset(0x18eL).setu(MEMORY.ref(2, s1).offset(0xe4L));
    MEMORY.ref(2, s1).offset(0xe4L).setu(0);
    MEMORY.ref(2, s1).offset(0xe2L).setu(0);

    //LAB_8004d2d4
  }

  @Method(0x8004d648L)
  public static long FUN_8004d648(final long a0, final long a1, final long a2) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x8004d6a8L)
  public static long FUN_8004d6a8(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x8004d91cL)
  public static void FUN_8004d91c(final long a0) {
    if(FUN_8004b7e0() == 0) {
      //LAB_8004d96c
      for(int s1 = 0; s1 < 0x18; s1++) {
        if(_800c3a40.offset(s1 * 102L).offset(0x1aL).get() != 0) {
          _800c3a40.offset(s1 * 102L).offset(0x8L).setu(0x1L);

          if(a0 != 0) {
            SPU.voices[s1 * 16].adsr.lo = 0;
            SPU.voices[s1 * 16].adsr.hi = 0;
            _800c3a40.offset(s1 * 102L).setu(0);
          }

          //LAB_8004d9b8
          if(s1 < 0x10L) {
            SPU_VOICE_KEY_OFF.offset(2, 0x0L).setu(0x1L << s1);
          } else {
            //LAB_8004d9d4
            SPU_VOICE_KEY_OFF.offset(2, 0x2L).setu(0x1L << s1 - 0x10L);
          }

          //LAB_8004d9e8
          //wasteSomeCycles(2);
        }

        //LAB_8004d9f0
        if(_800c4af1.offset(s1 * 0x124L).get() != 0) {
          _800c4af1.offset(s1 * 0x124L).setu(0);
          _800c4af2.offset(s1 * 0x124L).setu(0);

          _800c4baf.offset(s1 * 0x124L).setu(0);

          _800c4bb1.offset(s1 * 0x124L).setu(0);

          _800c4bb6.offset(s1 * 0x124L).setu(0);
          _800c4bb8.offset(s1 * 0x124L).setu(0);

          _800c4bcc.offset(s1 * 0x124L).setu(0);
          _800c4bcd.offset(s1 * 0x124L).setu(0);

          _800c4be0.offset(s1 * 0x124L).setu(0);
        }

        //LAB_8004da24
      }
    }

    //LAB_8004da38
  }
}
