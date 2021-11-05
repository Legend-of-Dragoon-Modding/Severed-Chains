package legend.game;

import legend.core.DebugHelper;
import legend.core.dma.DmaChannel;
import legend.core.dma.DmaChannelType;
import legend.core.dma.DmaManager;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.kernel.PriorityChainEntry;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.SupplierRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.spu.SpuDmaTransfer;
import legend.core.spu.Voice;
import legend.game.types.CallbackStruct;
import legend.game.types.JoyData;
import legend.game.types.ScriptStruct;
import legend.game.types.SpuStruct0c;
import legend.game.types.SpuStruct124;
import legend.game.types.SpuStruct66;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.CPU;
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
import static legend.game.Scus94491BpeSegment_8005._80058d0c;
import static legend.game.Scus94491BpeSegment_8005._8005954c;
import static legend.game.Scus94491BpeSegment_8005._80059550;
import static legend.game.Scus94491BpeSegment_8005._80059554;
import static legend.game.Scus94491BpeSegment_8005._8005955c;
import static legend.game.Scus94491BpeSegment_8005._80059560;
import static legend.game.Scus94491BpeSegment_8005._80059564;
import static legend.game.Scus94491BpeSegment_8005._800595a0;
import static legend.game.Scus94491BpeSegment_8005._800595d4;
import static legend.game.Scus94491BpeSegment_8005._800595e0;
import static legend.game.Scus94491BpeSegment_8005._800595ec;
import static legend.game.Scus94491BpeSegment_8005._800595f0;
import static legend.game.Scus94491BpeSegment_8005._800595f4;
import static legend.game.Scus94491BpeSegment_8005._800595f8;
import static legend.game.Scus94491BpeSegment_8005._800595fc;
import static legend.game.Scus94491BpeSegment_8005._80059600;
import static legend.game.Scus94491BpeSegment_8005._80059604;
import static legend.game.Scus94491BpeSegment_8005._80059610;
import static legend.game.Scus94491BpeSegment_8005._8005961c;
import static legend.game.Scus94491BpeSegment_8005._80059620;
import static legend.game.Scus94491BpeSegment_8005._80059624;
import static legend.game.Scus94491BpeSegment_8005._80059644;
import static legend.game.Scus94491BpeSegment_8005._80059650;
import static legend.game.Scus94491BpeSegment_8005._80059654;
import static legend.game.Scus94491BpeSegment_8005._80059658;
import static legend.game.Scus94491BpeSegment_8005._8005967c;
import static legend.game.Scus94491BpeSegment_8005._80059b3c;
import static legend.game.Scus94491BpeSegment_8005._80059f3c;
import static legend.game.Scus94491BpeSegment_8005._80059f7c;
import static legend.game.Scus94491BpeSegment_8005._80059f7e;
import static legend.game.Scus94491BpeSegment_8005._8005a1ce;
import static legend.game.Scus94491BpeSegment_8005._8005a1d0;
import static legend.game.Scus94491BpeSegment_8005.getNextJoypadCommandParam_800595dc;
import static legend.game.Scus94491BpeSegment_8005.joyDataIndex_80059614;
import static legend.game.Scus94491BpeSegment_8005.joySomething_8005962c;
import static legend.game.Scus94491BpeSegment_8005.joypadCallbackIndex_80059618;
import static legend.game.Scus94491BpeSegment_8005.joypadCallbacks_8005965c;
import static legend.game.Scus94491BpeSegment_8005.joypadPriorityChain_8005953c;
import static legend.game.Scus94491BpeSegment_8005.joypadVblankIrqHandler_8005952c;
import static legend.game.Scus94491BpeSegment_8005.joypadVblankIrqHandler_80059634;
import static legend.game.Scus94491BpeSegment_8005.joypadsReady_8005960c;
import static legend.game.Scus94491BpeSegment_8005.maxJoypadIndex_80059628;
import static legend.game.Scus94491BpeSegment_8005.priorityChain_80059570;
import static legend.game.Scus94491BpeSegment_8005.ptrArrJoyData_80059608;
import static legend.game.Scus94491BpeSegment_8005.ptrClearJoyData_800595d8;
import static legend.game.Scus94491BpeSegment_8005.ptrGetJoyDataForPort_800595e8;
import static legend.game.Scus94491BpeSegment_8005.sin_cos_80054d0c;
import static legend.game.Scus94491BpeSegment_800c._800c3658;
import static legend.game.Scus94491BpeSegment_800c._800c37a4;
import static legend.game.Scus94491BpeSegment_800c._800c3a28;
import static legend.game.Scus94491BpeSegment_800c._800c3a38;
import static legend.game.Scus94491BpeSegment_800c._800c3a3c;
import static legend.game.Scus94491BpeSegment_800c._800c3a40;
import static legend.game.Scus94491BpeSegment_800c._800c43d0;
import static legend.game.Scus94491BpeSegment_800c._800c4aa8;
import static legend.game.Scus94491BpeSegment_800c._800c4aac;
import static legend.game.Scus94491BpeSegment_800c._800c4ab0;
import static legend.game.Scus94491BpeSegment_800c._800c4ab4;
import static legend.game.Scus94491BpeSegment_800c._800c4ab8;
import static legend.game.Scus94491BpeSegment_800c._800c4abc;
import static legend.game.Scus94491BpeSegment_800c._800c4ac0;
import static legend.game.Scus94491BpeSegment_800c._800c4ac4;
import static legend.game.Scus94491BpeSegment_800c._800c4ac8;
import static legend.game.Scus94491BpeSegment_800c._800c6628;
import static legend.game.Scus94491BpeSegment_800c._800c6630;
import static legend.game.Scus94491BpeSegment_800c._800c6633;
import static legend.game.Scus94491BpeSegment_800c._800c6634;
import static legend.game.Scus94491BpeSegment_800c._800c6638;
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
import static legend.game.Scus94491BpeSegment_800c._800c6674;
import static legend.game.Scus94491BpeSegment_800c._800c6678;
import static legend.game.Scus94491BpeSegment_800c._800c667c;
import static legend.game.Scus94491BpeSegment_800c._800c6680;
import static legend.game.Scus94491BpeSegment_800c.dmaDpcrPtr_800c4a9c;
import static legend.game.Scus94491BpeSegment_800c.dmaSpuBcrPtr_800c4a94;
import static legend.game.Scus94491BpeSegment_800c.dmaSpuChcrPtr_800c4a98;
import static legend.game.Scus94491BpeSegment_800c.dmaSpuDelayPtr_800c4aa0;
import static legend.game.Scus94491BpeSegment_800c.dmaSpuMadrPtr_800c4a90;
import static legend.game.Scus94491BpeSegment_800c.eventSpuIrq_800c664c;
import static legend.game.Scus94491BpeSegment_800c.inputBuffer_800c39e0;
import static legend.game.Scus94491BpeSegment_800c.inputBuffer_800c3a03;
import static legend.game.Scus94491BpeSegment_800c.joyData_800c37b8;
import static legend.game.Scus94491BpeSegment_800c.joypadTimeoutCurrentTime_800c3a2c;
import static legend.game.Scus94491BpeSegment_800c.joypadTimeoutMode_800c3a34;
import static legend.game.Scus94491BpeSegment_800c.joypadTimeoutTimeout_800c3a30;
import static legend.game.Scus94491BpeSegment_800c.queuedSpuDmaTransferArray_800c49d0;
import static legend.game.Scus94491BpeSegment_800c.responseBuffer0_800c3998;
import static legend.game.Scus94491BpeSegment_800c.responseBuffer1_800c39bb;
import static legend.game.Scus94491BpeSegment_800c.spuDmaIndex_800c6669;
import static legend.game.Scus94491BpeSegment_800c.spuDmaTransferInProgress_800c6650;
import static legend.game.Scus94491BpeSegment_800c.spuMono_800c6666;
import static legend.game.Scus94491BpeSegment_800c.voice00LeftPtr_800c4aa4;

public final class Scus94491BpeSegment_8004 {
  private Scus94491BpeSegment_8004() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8004.class);

  public static final Value _8004db58 = MEMORY.ref(2, 0x8004db58L);

  public static final Value _8004db88 = MEMORY.ref(2, 0x8004db88L);

  /**
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment_800e#executePregameLoadingStage()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#finalizePregameLoading()}</li>
   *   <li>{@link Ttle#executeTtleLoadingStage()}</li>
   *   <li>{@link Ttle#executeTtleUnloadingStage()}</li>
   *   <li>{@link SMap#FUN_800eaa88()}</li>
   *   <li>{@link SMap#executeSmapLoadingStage()} Sets up rendering and loads scene</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80018658()}</li>
   *   <li>{@link Ttle#FUN_800c75fc()}</li>
   *   <li>0x800cc738 (TODO)</li>
   *   <li>{@link SMap#executeSmapPregameLoadingStage()}</li>
   *   <li>{@link SMap#FUN_800d92a0()}</li>
   *   <li>{@link SMap#FUN_800d9e08()}</li>
   *   <li>{@link Ttle#FUN_800c6eb8()}</li>
   *   <li>0x800cab8c (TODO)</li>
   *   <li>null</li>
   *   <li>0x800c6978 (TODO)</li>
   *   <li>null</li>
   *   <li>0x800cdcdc (TODO)</li>
   *   <li>0x800cabd4 (TODO)</li>
   *   <li>null</li>
   * </ol>
   */
  public static final ArrayRef<CallbackStruct> callback_8004dbc0 = MEMORY.ref(4, 0x8004dbc0L, ArrayRef.of(CallbackStruct.class, 20, 0x10, CallbackStruct::new));

  public static final Value _8004dd04 = MEMORY.ref(4, 0x8004dd04L);
  public static final Value loadingSmapOvl_8004dd08 = MEMORY.ref(4, 0x8004dd08L);
  public static final Value _8004dd0c = MEMORY.ref(4, 0x8004dd0cL);
  public static final Value _8004dd10 = MEMORY.ref(4, 0x8004dd10L);
  public static final Value _8004dd14 = MEMORY.ref(4, 0x8004dd14L);
  public static final Value _8004dd18 = MEMORY.ref(4, 0x8004dd18L);
  public static final Value _8004dd1c = MEMORY.ref(2, 0x8004dd1cL);
  public static final Value loadingSstrmOvl_8004dd1e = MEMORY.ref(1, 0x8004dd1eL);

  public static final Value mainCallbackIndex_8004dd20 = MEMORY.ref(4, 0x8004dd20L);
  public static final Value _8004dd24 = MEMORY.ref(4, 0x8004dd24L);
  public static final Value _8004dd28 = MEMORY.ref(4, 0x8004dd28L);

  public static final Value _8004dd30 = MEMORY.ref(4, 0x8004dd30L);
  public static final Value width_8004dd34 = MEMORY.ref(2, 0x8004dd34L);
  /**
   * Bit 0 - Interlaced
   * Bit 1 - Height (0 -> 240, 1 -> 480)
   * Bit 2 - 24-bit colour
   */
  public static final Value renderFlags_8004dd36 = MEMORY.ref(2, 0x8004dd36L);
  public static final Value _8004dd38 = MEMORY.ref(2, 0x8004dd38L);

  public static final Pointer<RunnableRef> syncFrame_8004dd3c = MEMORY.ref(4, 0x8004dd3cL, Pointer.of(4, RunnableRef::new));
  public static final Pointer<RunnableRef> swapDisplayBuffer_8004dd40 = MEMORY.ref(4, 0x8004dd40L, Pointer.of(4, RunnableRef::new));
  public static final Value _8004dd44 = MEMORY.ref(4, 0x8004dd44L);
  public static final Value _8004dd48 = MEMORY.ref(2, 0x8004dd48L);

  public static final Value _8004dd80 = MEMORY.ref(2, 0x8004dd80L);

  /**
   * \SECT\DRGN21.BIN, also gets changed in SInitBin
   */
  public static final Value _8004dd88 = MEMORY.ref(1, 0x8004dd88L);

  public static final Value _8004dda0 = MEMORY.ref(2, 0x8004dda0L);
  public static final Value fileNamePtr_8004dda4 = MEMORY.ref(4, 0x8004dda4L);

  public static final Value _8004ddc0 = MEMORY.ref(4, 0x8004ddc0L);
  public static final Value callbackIndex_8004ddc4 = MEMORY.ref(4, 0x8004ddc4L);
  public static final Value fileCount_8004ddc8 = MEMORY.ref(4, 0x8004ddc8L);
  public static final Value _8004ddcc = MEMORY.ref(1, 0x8004ddccL);
  public static final Value _8004ddd0 = MEMORY.ref(4, 0x8004ddd0L);
  public static final Value _8004ddd4 = MEMORY.ref(4, 0x8004ddd4L);
  public static final Value _8004ddd8 = MEMORY.ref(4, 0x8004ddd8L);

  public static final ArrayRef<Pointer<SupplierRef<Long>>> callbackArray_8004dddc = MEMORY.ref(0x70, 0x8004dddcL, ArrayRef.of(Pointer.classFor(SupplierRef.classFor(Long.class)), 28, 4, Pointer.of(4, SupplierRef::new)));

  public static final Value index_8004de4c = MEMORY.ref(4, 0x8004de4cL);

  /**
   * Table of pointers to variables accessible by scripting engine
   *
   * <ol start="0">
   *   <li>{@link legend.game.Scus94491BpeSegment_8004#mainCallbackIndex_8004dd20}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#pregameLoadingStage_800bb10c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bb0fc}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bee90}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bee94}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac5c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800babd0}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8007#_8007a3a8}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bb104}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800babc0}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bb168}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#scriptEffect_800bb140#red0_20}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#scriptEffect_800bb140#green0_1c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#scriptEffect_800bb140#blue0_14}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#scriptEffect_800bb140#red1_18}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#scriptEffect_800bb140#green1_10}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#scriptEffect_800bb140#blue1_0c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac50}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac60}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac64}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac68}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac6c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac70}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac6c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac78}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8007#vsyncMode_8007a3b8}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bee98}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800beebc}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bee9c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800beea4}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800beeac}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800beeb4}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006f1a4}</li>
   *   <li>{@link legend.game.SMap#_800c66d0}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006f1d8}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800c#_800c677c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006f1e8}</li>
   *   <li>{@link legend.game.SMap#_800c6768}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006f27c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006f284}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bc978}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bc928}</li>
   *   <li>{@link legend.game.SMap#_800c66bc}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#submapScene_800bb0f8}</li>
   *   <li>{@link legend.game.SMap#bigStruct_800c6748}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e518}</li>
   *   <li>{@link legend.game.SMap#_800c6718}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#submapStage_800bb0f4}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006f210}</li>
   *   <li>{@link legend.game.SMap#_800c669c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006f244}</li>
   *   <li>{@link legend.game.SMap#_800c6760}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006f254}</li>
   *   <li>{@link legend.game.SMap#_800c6758}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006f288}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac7c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bac80}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bc974}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bc960}</li>
   *   <li>{@link legend.game.SMap#_800c66c8}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bc920}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bc95c}</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>{@link legend.game.SMap#index_800c6880}</li>
   *   <li>{@link legend.game.SMap#_800c6740}</li>
   *   <li>{@link legend.game.SMap#_800c6730}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bd7b0}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bda08}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8005#submapCut_80052c30}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8005#_80052c34}</li>
   *   <li>{@link legend.game.SMap#_800cb44c}</li>
   *   <li>{@link legend.game.SMap#_800c6ae8}</li>
   *   <li>{@link legend.game.SMap#_800c6970}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8004#_8004de54}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8004#_8004de50}</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>{@link legend.game.SMap#_800c6914}</li>
   *   <li>{@link legend.game.SMap#_800c6918}</li>
   *   <li>{@link legend.game.SMap#_800c67c8}</li>
   *   <li>{@link legend.game.SMap#_800c67cc}</li>
   *   <li>{@link legend.game.SMap#_800c67d0}</li>
   *   <li>{@link legend.game.SMap#_800c6710}</li>
   *   <li>{@link legend.game.SMap#_800c6780}</li>
   *   <li>{@link legend.game.SMap#_800c66a8}</li>
   *   <li>{@link legend.game.SMap#_800c6700}</li>
   *   <li>{@link legend.game.SMap#_800c6704}</li>
   *   <li>{@link legend.game.SMap#_800c66b0}</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>{@link legend.game.SMap#_800c6754}</li>
   *   <li>{@link legend.game.SMap#_800c66a4}</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>{@link legend.game.SMap#_800c6764}</li>
   *   <li>{@link legend.game.SMap#_800c6774}</li>
   *   <li>{@link legend.game.SMap#_800c6778}</li>
   *   <li>{@link legend.game.SMap#_800c676c}</li>
   *   <li>{@link legend.game.SMap#_800c6770}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800f#_800fa6dc}</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bad24}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bad44}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bad64}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800baef8}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800baf24}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800baf50}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800baf7c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bafa8}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bafd4}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bb000}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bb02c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bb058}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8005#_8005a368}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8007#_8007a3b4}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bad6c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_800b#_800bad8c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e398}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e3b8}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e3d8}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e3f8}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e418}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e438}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e458}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e478}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e498}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment_8006#_8006e4b8}</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<UnsignedIntRef>> scriptPtrs_8004de58 = MEMORY.ref(4, 0x8004de58L, ArrayRef.of(Pointer.classFor(UnsignedIntRef.class), 0x90, 4, Pointer.deferred(4, UnsignedIntRef::new)));

  /**
   * This is the world's largest jump table
   *
   * <ol start="0">
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptReturnOne}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptReturnTwo}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptDecrementIfPossible}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptCompare}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016744}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptMove}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016790}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptMemCopy}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016854}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016868}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_8001688c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_800168b0}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_800168d4}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016900}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptShiftLeft}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptShiftRightArithmetic}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptAdd}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptSubtract}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_800169b0}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptIncrementBy1}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptDecrementBy1}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016a14}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016a34}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptMultiply}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptDivide}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016ab0}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016adc}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016b04}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016b2c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016b5c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016b8c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016adc}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016b04}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptSquareRoot}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016c00}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016c4c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016c80}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016cb4}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptExecuteSubFunc}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptJump}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptConditionalJump}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptConditionalJump0}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016dec}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016e1c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptJumpAndLink}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptJumpReturn}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80016ffc}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_800170f4}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80017138}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80017160}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_800171c0}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80017234}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_800172c0}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_800172f4}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_800172fc}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_80017304}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#FUN_8001730c}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   *   <li>{@link legend.game.Scus94491BpeSegment#scriptNotImplemented}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<ScriptStruct, Long>>> _8004e098 = MEMORY.ref(4, 0x8004e098L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(ScriptStruct.class, Long.class)), 0x81, 4, Pointer.of(4, FunctionRef::new)));

  /**
   * <p>Actually this is</p>
   *
   * <p>All methods that are skipped are {@link Scus94491BpeSegment#scriptSubNotImplemented}</p>
   *
   * <p>Many methods are copied into this table at runtime.</p>
   *
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment#FUN_80017354}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80017374}</li>
   *   <li>{@link Scus94491BpeSegment#scriptSetGlobalFlag1}</li>
   *   <li>{@link Scus94491BpeSegment#scriptReadGlobalFlag1}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80017440}</li>
   *   <li>{@link Scus94491BpeSegment#scriptReadGlobalFlag2}</li>
   *   <li>{@link Scus94491BpeSegment#scriptStartEffect}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80017564}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80017584}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_800175b4}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80017648}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80017688}</li>
   *   <li>{@link SMap#FUN_800d9bc0}</li>
   *   <li>{@link SMap#FUN_800d9bf4}</li>
   *   <li>{@link SMap#FUN_800d9c1c}</li>
   *   <li>{@link SMap#FUN_800d9ce4}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_800176c0}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_800176ec}</li>
   *   <li>{@link SMap#FUN_800d9d20}</li>
   *   <li>{@link SMap#FUN_800d9d60}</li>
   * </ol>
   * ...
   * <ol start="128">
   *   <li>800cb3fc</li>
   *   <li>800cb468</li>
   *   <li>800cb4c8</li>
   *   <li>800cc7d8</li>
   *   <li>800cb534</li>
   *   <li>800cc948</li>
   *   <li>800cb578</li>
   *   <li>{@link Scus94491BpeSegment#scriptSubNotImplemented}</li>
   *   <li>800cb5d8</li>
   *   <li>{@link Scus94491BpeSegment#scriptSubNotImplemented}</li>
   *   <li>800cb618</li>
   *   <li>800cb674</li>
   *   <li>800cb6bc</li>
   *   <li>800cb764</li>
   *   <li>800cb76c</li>
   *   <li>800cb9b0</li>
   *   <li>800cb9f0</li>
   *   <li>800cba28</li>
   *   <li>800cba60</li>
   *   <li>800cbabc</li>
   *   <li>800cbb00</li>
   *   <li>800cbc14</li>
   *   <li>800cbde0</li>
   *   <li>800cbef8</li>
   *   <li>800cc0c8</li>
   *   <li>800cc1cc</li>
   *   <li>800cc364</li>
   *   <li>800cc46c</li>
   *   <li>800cc608</li>
   *   <li>800cc698</li>
   *   <li>800cc784</li>
   *   <li>800cc8f4</li>
   *   <li>800cca34</li>
   *   <li>{@link Scus94491BpeSegment#scriptSubNotImplemented}</li>
   *   <li>{@link Scus94491BpeSegment#scriptSubNotImplemented}</li>
   *   <li>{@link Scus94491BpeSegment#scriptSubNotImplemented}</li>
   *   <li>800ccb3c</li>
   *   <li>800ccb70</li>
   *   <li>800ccba4</li>
   *   <li>800cccf4</li>
   *   <li>800ccd34</li>
   *   <li>800cce04</li>
   *   <li>800ccf0c</li>
   *   <li>800ccec8</li>
   *   <li>800ccef8</li>
   *   <li>800ccf2c</li>
   *   <li>800cd0ec</li>
   *   <li>800cd078</li>
   *   <li>800cd160</li>
   *   <li>800cce70</li>
   *   <li>800ccda0</li>
   * </ol>
   * ...
   * <ol start="320">
   *   <li>800cc9d8</li>
   *   <li>{@link Scus94491BpeSegment#scriptSubNotImplemented}</li>
   *   <li>800cb84c</li>
   *   <li>800cb95c</li>
   *   <li>{@link Scus94491BpeSegment#scriptSubNotImplemented}</li>
   *   <li value="352">800cd3b4</li>
   *   <li>800ee210</li>
   *   <li>800cd468</li>
   *   <li>800cd4b0</li>
   *   <li>800cd4f0</li>
   *   <li>800cd52c</li>
   *   <li>800cd570</li>
   *   <li>800cda78</li>
   *   <li>800cd5b4</li>
   *   <li>800cd740</li>
   *   <li>800cd7a8</li>
   *   <li>800cd810</li>
   *   <li>800cd8a4</li>
   *   <li>800cd9fc</li>
   *   <li>800cda3c</li>
   *   <li>800cdb18</li>
   *   <li>800cdb44</li>
   *   <li>800cd910</li>
   *   <li>800cd958</li>
   *   <li>800cd998</li>
   *   <li>800cdb74</li>
   * </ol>
   * ...
   * <ol start="1023">
   *   <li>{@link Scus94491BpeSegment#scriptSubNotImplemented}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<ScriptStruct, Long>>> scriptSubFunctions_8004e29c = MEMORY.ref(4, 0x8004e29cL, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(ScriptStruct.class, Long.class)), 0x3ff, 4, Pointer.of(4, FunctionRef::new)));
  // 8004f29c end of jump table

  public static final Value _8004f5ac = MEMORY.ref(2, 0x8004f5acL);

  public static final Value _8004f65c = MEMORY.ref(2, 0x8004f65cL);

  public static final Value _8004f664 = MEMORY.ref(1, 0x8004f664L);

  public static final Value _8004f6a4 = MEMORY.ref(4, 0x8004f6a4L);

  public static final Value _8004f6e4 = MEMORY.ref(4, 0x8004f6e4L);
  public static final Value _8004f6e8 = MEMORY.ref(4, 0x8004f6e8L);
  public static final Value _8004f6ec = MEMORY.ref(4, 0x8004f6ecL);

  public static final Value _8004fa98 = MEMORY.ref(1, 0x8004fa98L);

  public static final Value _8004fa9a = MEMORY.ref(2, 0x8004fa9aL);
  public static final Value _8004fa9c = MEMORY.ref(4, 0x8004fa9cL);


  public static final Value _8004fb00 = MEMORY.ref(1, 0x8004fb00L);

  public static final Value _8004fb02 = MEMORY.ref(2, 0x8004fb02L);
  public static final Value _8004fb04 = MEMORY.ref(4, 0x8004fb04L);

  public static final Value _8004ff10 = MEMORY.ref(4, 0x8004ff10L);
  public static final Value _8004ff14 = MEMORY.ref(4, 0x8004ff14L);

  /** TODO This is probably one of the RotMatrix* methods */
  @Method(0x80040010L)
  public static MATRIX RotMatrix_80040010(final SVECTOR vector, final MATRIX matrix) {
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
    if(t7 < 0) {
      //LAB_8004002c
      t7 = -t7 * 0xfffL;
      t9 = sin_cos_80054d0c.offset(t7 * 0x4L).get();
      t6 = (short)t9;
      t3 = -t6;
    } else {
      //LAB_80040054
      t7 = t7 & 0xfffL;
      t9 = sin_cos_80054d0c.offset(t7 * 0x4L).get();
      t3 = (short)t9;
    }

    t0 = t9 >> 16;

    //LAB_80040074
    t7 = vector.getY();
    if(t7 < 0) {
      //LAB_80040090
      t7 = -t7 & 0xfffL;
      t9 = sin_cos_80054d0c.offset(t7 * 0x4L).get();
      t6 = (short)t9;
      t4 = -t6;
    } else {
      //LAB_800400b8
      t7 = t7 & 0xfffL;
      t9 = sin_cos_80054d0c.offset(t7 * 0x4L).get();
      t4 = (short)t9;
      t6 = -t4;
    }

    t1 = (int)t9 >> 16;

    //LAB_800400dc
    matrix.set(2, 0, (short)t6);
    matrix.set(2, 1, (short)((t3 * t1) >> 12));
    matrix.set(2, 2, (short)((t0 * t1) >> 12));

    t7 = vector.getZ();
    if(t7 < 0) {
      //LAB_8004011c
      t7 = -t7 & 0xfffL;
      t9 = sin_cos_80054d0c.offset(t7 * 0x4L).get();
      t5 = -(short)t9;
    } else {
      //LAB_80040144
      t7 = t7 & 0xfffL;
      t9 = sin_cos_80054d0c.offset(t7 * 0x4L).get();
      t5 = (short)t9;
    }

    t2 = (int)t9 >> 16;

    //LAB_80040170
    matrix.set(0, 0, (short)((t1 * t2) >> 12));
    matrix.set(1, 0, (short)((t5 * t1) >> 12));
    t8 = (t3 * t4) >> 12;
    matrix.set(0, 1, (short)(((t8 * t2) >> 12) - ((t5 * t0) >> 12)));
    matrix.set(1, 1, (short)(((t8 * t5) >> 12) + ((t0 * t2) >> 12)));
    t8 = (t4 * t0) >> 12;
    matrix.set(0, 2, (short)(((t8 * t2) >> 12) + ((t3 * t5) >> 12)));
    matrix.set(1, 2, (short)(((t8 * t5) >> 12) - ((t3 * t2) >> 12)));

    return matrix;
  }

  @Method(0x800402a0L)
  public static void RotMatrixX(final long r, final MATRIX matrix) {
    final long t1;
    final long t9;

    if(r < 0) {
      //LAB_800402bc
      t9 = sin_cos_80054d0c.offset((-r & 0xfffL) * 4).get();
      t1 = -(short)t9;
    } else {
      //LAB_800402e4
      t9 = sin_cos_80054d0c.offset((r & 0xfffL) * 4).get();
      t1 = (short)t9;
    }

    final long t0 = (int)t9 >> 16;

    //LAB_80040304
    final long m10 = matrix.get(1, 0);
    final long m11 = matrix.get(1, 1);
    final long m12 = matrix.get(1, 2);
    final long m20 = matrix.get(2, 0);
    final long m21 = matrix.get(2, 1);
    final long m22 = matrix.get(2, 2);

    matrix.set(1, 0, (short)((t0 * m10 - t1 * m20) >> 12));
    matrix.set(1, 1, (short)((t0 * m11 - t1 * m21) >> 12));
    matrix.set(1, 2, (short)((t0 * m12 - t1 * m22) >> 12));
    matrix.set(2, 0, (short)((t1 * m10 + t0 * m20) >> 12));
    matrix.set(2, 1, (short)((t1 * m11 + t0 * m21) >> 12));
    matrix.set(2, 2, (short)((t1 * m12 + t0 * m22) >> 12));
  }

  @Method(0x80040440L)
  public static void RotMatrixY(final long r, final MATRIX matrix) {
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t8;
    long lo;

    long t7 = r;
    long t9 = t7 & 0xfffL;
    if(t7 < 0) {
      t7 = -t7;
      t7 &= 0xfffL;

      //LAB_8004045c
      t8 = t7 << 0x2L;
      t9 = sin_cos_80054d0c.offset(t8).get();
      t1 = (short)t9;
      t0 = (int)t9 >> 16;
    } else {
      //LAB_80040480
      t8 = t9 * 4L;
      t9 = sin_cos_80054d0c.offset(t8).get();
      t7 = (short)t9;
      t1 = -t7;
      t0 = (int)t9 >> 16;
    }

    //LAB_800404a4
    t2 = matrix.get(0);
    t5 = matrix.get(6);
    lo = t0 * t2;
    t3 = matrix.get(1);
    t6 = matrix.get(7);
    t8 = lo;
    t4 = matrix.get(1);
    t7 = matrix.get(8);
    lo = t1 * t5;
    t9 = lo;
    t9 = t8 - t9;
    t8 = t9 >> 0xcL;
    lo = t0 * t3;
    matrix.set(0, (short)t8);
    t8 = lo;
    lo = t1 * t6;
    t9 = lo;
    t9 = t8 - t9;
    t8 = t9 >> 0xcL;
    lo = t0 * t4;
    matrix.set(1, (short)t8);
    t8 = lo;
    lo = t1 * t7;
    t9 = lo;
    t9 = t8 - t9;
    t8 = t9 >> 0xcL;
    lo = t1 * t2;
    matrix.set(2, (short)t8);
    t8 = lo;
    lo = t0 * t5;
    t9 = lo;
    t9 = t8 + t9;
    t8 = t9 >> 0xcL;
    lo = t1 * t3;
    matrix.set(6, (short)t8);
    t8 = lo;
    lo = t0 * t6;
    t9 = lo;
    t9 = t8 + t9;
    t8 = t9 >> 0xcL;
    lo = t1 * t4;
    matrix.set(7, (short)t8);
    t8 = lo;
    lo = t0 * t7;
    t9 = lo;
    t9 = t8 + t9;
    t8 = t9 >> 0xcL;
    matrix.set(8, (short)t8);
  }

  @Method(0x800405e0L)
  public static void RotMatrixZ(final long r, final MATRIX matrix) {
    final long t1;
    final long t9;

    if(r < 0) {
      //LAB_800405fc
      t9 = sin_cos_80054d0c.offset((-r & 0xfffL) * 4).get();
      t1 = -(short)t9;
    } else {
      //LAB_80040624
      t9 = sin_cos_80054d0c.offset((r & 0xfffL) * 4).get();
      t1 = (short)t9;
    }

    final long t0 = (int)t9 >> 16;

    //LAB_80040644
    final long m00 = matrix.get(0, 0);
    final long m01 = matrix.get(0, 1);
    final long m02 = matrix.get(0, 2);
    final long m10 = matrix.get(1, 0);
    final long m11 = matrix.get(1, 1);
    final long m12 = matrix.get(1, 2);

    matrix.set(0, 0, (short)((t0 * m00 - t1 * m10) >> 12));
    matrix.set(0, 1, (short)((t0 * m01 - t1 * m11) >> 12));
    matrix.set(0, 2, (short)((t0 * m02 - t1 * m12) >> 12));
    matrix.set(1, 0, (short)((t1 * m00 + t0 * m10) >> 12));
    matrix.set(1, 1, (short)((t1 * m01 + t0 * m11) >> 12));
    matrix.set(1, 2, (short)((t1 * m02 + t0 * m12) >> 12));
  }

  /** TODO RotMatrix_gte? */
  @Method(0x80040780L)
  public static void RotMatrix_80040780(SVECTOR vector, MATRIX matrix) {
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long a2;
    long a3;
    long at;
    long lo;

    t0 = vector.getZ();
    v1 = sin_cos_80054d0c.getAddress();
    t4 = vector.getXY();
    long a0;
    long a1;
    t3 = t0 >> 0x1fL;
    t0 += t3;
    t0 ^= t3;
    t0 <<= 2;
    t0 &= 0x3ffcL;
    t0 += v1;
    a2 = MEMORY.ref(4, t0).get();
    t0 = t4 >> 16;
    t2 = t0 >> 0x1f;
    t0 += t2;
    t0 ^= t2;
    t0 <<= 2;
    t0 &= 0x3ffcL;
    t0 += v1;
    a1 = MEMORY.ref(4, t0).get();
    t0 = (short)t4;
    t1 = t0 >> 0x1fL;
    t0 += t1;
    t0 ^= t1;
    t0 <<= 2;
    t0 &= 0x3ffcL;
    t0 += v1;
    a0 = MEMORY.ref(4, t0).get();
    at = a2 << 16;
    a2 &= 0xffff_0000L;
    at += t3;
    at ^= t3;
    at >>>= 16;
    a2 |= at;
    at = a1 << 16;
    a1 &= 0xffff_0000L;
    at += t2;
    at ^= t2;
    at >>>= 16;
    a1 |= at;
    at = a0 << 16;
    a0 &= 0xffff_0000L;
    at += t1;
    at ^= t1;
    at >>>= 16;
    a0 |= at;
    t0 = a0 >> 16;
    CPU.MTC2(t0, 8);
    a3 = (short)a1;
    CPU.MTC2(a3, 9);
    v1 = (short)a2;
    CPU.MTC2(v1, 10);
    CPU.MTC2(a2 >> 16, 11);
    CPU.COP2(0x198_003dL);
    lo = (a1 >> 16) * t0;
    t0 = CPU.MFC2(9);
    t1 = CPU.MFC2(10);
    t2 = CPU.MFC2(11);
    t6 = (short)a0;
    CPU.MTC2(t6, 8);
    CPU.MTC2(a3, 9);
    CPU.MTC2(v1, 10);
    CPU.MTC2(a2 >> 16, 11);
    CPU.COP2(0x198_003dL);
    matrix.set(8, (short)(lo >> 12));
    t3 = CPU.MFC2(9);
    t4 = CPU.MFC2(10);
    t5 = CPU.MFC2(11);

    CPU.MTC2(a2 >> 16, 8);
    at = a1 >> 16;
    CPU.MTC2(at, 9);
    CPU.MTC2(t3, 10);
    CPU.MTC2(t0, 11);
    CPU.COP2(0x198_003dL);
    a0 = CPU.MFC2(9);
    a1 = CPU.MFC2(10);
    a2 = CPU.MFC2(11);

    matrix.set(6, (short)(-a3 & 0xffffL));
    matrix.set(7, (short)(at * t6 / 0x1000L));
    matrix.set(0, (short)a0);
    matrix.set(1, (short)(a1 - t1));

    CPU.MTC2(at, 9);
    CPU.MTC2(v1, 8);
    CPU.MTC2(t3, 10);
    CPU.MTC2(t0, 11);
    CPU.COP2(0x198_003dL);
    at = CPU.MFC2(9);
    t6 = CPU.MFC2(10);
    t7 = CPU.MFC2(11);

    matrix.set(2, (short)(a2 + t4));
    matrix.set(3, (short)at);
    matrix.set(4, (short)(t6 + t2));
    matrix.set(5, (short)(t7 - t5));
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
      if((0x7fe0_0000L & a0) == 0) {
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
      if((0x7fe0_0000L & a1) == 0) {
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

  // Joypad handling code begins here

  private static boolean SKIP_JOYPAD_INTERRUPT_CHECKS = true;

  @Method(0x80041be0L)
  public static int joypadVblankIrqHandlerFirstFunction() {
    if(I_MASK.get(0x1L) == 0 || I_STAT.get(0x1L) == 0) {
      return 0;
    }

    return 1;
  }

  @Method(0x80041c60L)
  public static void joypadVblankIrqHandlerSecondFunction(final int firstFunctionReturn) {
    if(_80059550.get() != 0) {
      if(_800c37a4.get() != 0x11L || _8005955c.addu(0x1L).get() >= 0x3L) {
        //LAB_80041d18
        _8005954c.setu(0x11L);
        _80059550.setu(0);
        _8005955c.setu(0);
        I_STAT.setu(0xffff_ff7fL);
        I_MASK.and(0xffff_ff7fL);
        JOY_MCD_CTRL.setu(0);
        SysDeqIntRP(0x1, joypadPriorityChain_8005953c);
        _800595a0.setu(0);
        JOY_MCD_CTRL.setu(0x40L); // Reset
        JOY_MCD_BAUD.setu(0x88L);
        JOY_MCD_MODE.setu(0xdL); // 8-bit, no parity, MUL1
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
      SysDeqIntRP(0x1, joypadPriorityChain_8005953c);
    }

    //LAB_80041d98
    if(_8005954c.get(0x1L) == 0) {
      SysDeqIntRP(0x1, joypadPriorityChain_8005953c);
      SysEnqIntRP(0x1, joypadPriorityChain_8005953c);
      FUN_800421a0();
      I_STAT.setu(0xffff_ff7fL);
      I_MASK.oru(0x80L);
    }

    //LAB_80041df8
  }

  @Method(0x80041e08L)
  public static boolean FUN_80041e08(final long a0) {
    if(_8005954c.get(0x1L) == 0) {
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
  public static void registerJoypadVblankIrqHandler() {
    EnterCriticalSection();

    SysDeqIntRP(0x2, joypadVblankIrqHandler_8005952c);
    enqueueIntRP(0x2, joypadVblankIrqHandler_8005952c);

    I_STAT.setu(0xffff_fffe); // Clear VBLANK IRQ
    I_MASK.oru(0x1L); // Enable VBLANK IRQ

    ChangeClearRCnt(0x3, false); // Don't automatically acknowledge VBLANK

    ExitCriticalSection();
  }

  /**
   * Don't know why SysEnqIntRP isn't used
   */
  @Method(0x80042140L)
  public static void enqueueIntRP(final int priority, final PriorityChainEntry newEntry) {
    GATE.acquire();

    PriorityChainEntry currentEntry = priorityChain_80059570.deref().deref().get(priority).deref();

    //LAB_80042170
    //LAB_80042178
    while(!currentEntry.next.isNull()) {
      currentEntry = currentEntry.next.deref();
    }

    currentEntry.next.set(newEntry);

    //LAB_8004218c
    newEntry.next.clear();

    GATE.release();
  }

  @Method(0x800421a0L)
  public static void FUN_800421a0() {
    assert false;
  }

  @Method(0x80042b60L)
  public static void FUN_80042b60(final int port, final ArrayRef<UnsignedByteRef> bytePtr28, final long byte34) {
    final JoyData joyData = ptrGetJoyDataForPort_800595e8.deref().run(port);
    joyData.bytePtr28.set(bytePtr28);
    joyData.byte34.setUnsigned(byte34);
  }

  @Method(0x80042ba0L)
  public static long FUN_80042ba0(final int port, final ArrayRef<UnsignedByteRef> params) {
    final JoyData joyData = ptrGetJoyDataForPort_800595e8.deref().run(port);

    if(_800595f0.deref().run(joyData) != 0) {
      return 0;
    }

    joyData.commandParams_byteArrPtr20.set(params);

    joyData.commandSender.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "queueJoypadRumbleModeCommand", JoyData.class)).cast(ConsumerRef::new));
    joyData.responseHandler.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "handleJoypadRumbleModeResponse", JoyData.class)).cast(FunctionRef::new));

    joyData.byte46.set(1);

    return 1;
  }

  @Method(0x80042c1cL)
  public static void queueJoypadRumbleModeCommand(final JoyData joyData) {
    queueJoypadCommand(joyData, 0x4d, joyData.commandParams_byteArrPtr20.deref(), 0x6);
  }

  @Method(0x80042c44L)
  public static int handleJoypadRumbleModeResponse(final JoyData joyData) {
    //LAB_80042c5c
    long t2 = 0;
    int t0 = 0;

    while(t0 < joyData.bytee9.get()) {
      int a1 = 0;
      long a3 = 0;
      long v1 = 0x5L;

      //LAB_80042c68
      do {
        if(joyData.commandParams_byteArrPtr20.deref().get(a1).get() == t0) {
          a3++;
        }

        a1++;

        //LAB_80042c7c
        v1--;
      } while((int)v1 >= 0);

      final long v0 = joyData.int04.get() + t2;
      long t1 = MEMORY.ref(1, v0).offset(0x2L).get();
      if(t1 == 0) {
        t1 = 0x1L;
      }

      //LAB_80042ca8
      //LAB_80042cac
      for(int a2 = 0; a2 < 0x6L; a2++) {
        if(joyData.commandParams_byteArrPtr20.deref().get(a2).get() == t0) {
          if(a3 < t1) {
            joyData.getJoypadState46Params_byteArr5d.get(a2).set(0xff);
            a3--;
          } else {
            //LAB_80042cd4
            joyData.getJoypadState46Params_byteArr5d.get(a2).set(t0);
          }
        }

        //LAB_80042cd8
      }

      t0++;
      t2 += 0x5L;
    }

    //LAB_80042cfc
    joyData.byte46.set(0xfe);
    return 0;
  }

  @Method(0x80042d10L)
  public static boolean FUN_80042d10(final int port, final int val, final int sel) {
    final JoyData joyData = ptrGetJoyDataForPort_800595e8.deref().run(port);

    if(_800595f0.deref().run(joyData) != 0) {
      return false;
    }

    joyData.commandSender.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80042dac", JoyData.class)).cast(ConsumerRef::new));
    joyData.responseHandler.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80042e04", JoyData.class)).cast(FunctionRef::new));

    joyData.byte46.set(1);

    joyData.setAnalogState44Params_byteArr51.get(0).set(val);
    joyData.setAnalogState44Params_byteArr51.get(1).set(sel);
    joyData.byte53.setUnsigned((joyData.bytee4.get() ^ val) < 0x1L ? 1 : 0);

    //LAB_80042d94
    return true;
  }

  @Method(0x80042dacL)
  public static void FUN_80042dac(final JoyData joyData) {
    final long v1 = joyData.byte46.get();
    if(v1 == 0x2L) {
      //LAB_80042dd4
      queueJoypadCommand(joyData, 0x44, joyData.setAnalogState44Params_byteArr51, 0x2); // Set analog state
    } else if(v1 == 0x3L) {
      //LAB_80042de4
      queueJoypadCommand(joyData, 0x4d, joyData.getJoypadState46Params_byteArr5d, 0x6); // Get controller state
    }

    //LAB_80042dec
    //LAB_80042df4
  }

  @Method(0x80042e04L)
  public static int FUN_80042e04(final JoyData joyData) {
    if(joyData.byte53.getUnsigned() == 0) {
      //LAB_80042e3c
      ptrClearJoyData_800595d8.deref().run(joyData);
      return 0;
    }

    if(joyData.byte46.get() != 0x2L) {
      //LAB_80042e34
      joyData.byte46.set(0xfe);
      return 0;
    }

    return 1;
  }

  @Method(0x80042e70L)
  public static int FUN_80042e70(final int port) {
    final JoyData joyData = ptrGetJoyDataForPort_800595e8.deref().run(port);

    //LAB_80042ecc
    if(joyData.command_byte37.get() != 0 || joyData.byte38.get() != 0 || (joyData.joyDataPtr10.getPointer() != joyData.getAddress() && joyData.byte39.getUnsigned() != 0) || joyData.bytePtr30.deref().get(0).get() != 0) {//LAB_80042ee4
      final int byte49 = joyData.byte49.get();

      if(byte49 == 0x2 || byte49 == 0x3) {
        return 0x1;
      }

      //LAB_80042f0c
      if(byte49 == 0x6) {
        return 0x4;
      }

      //LAB_80042f28
    }

    return joyData.byte49.get();
  }

  /**
   * @param a1 The thing to return - 0 -> 0, 1 -> controller type, 2 -> ?, 3 -> ?, 4 -> ?, 64 -> ?
   */
  @Method(0x80042f40L)
  public static int FUN_80042f40(final int port, final long a1, final long a2) {
    final JoyData joyData = ptrGetJoyDataForPort_800595e8.deref().run(port);

    if(a1 == 0x0L) {
      return 0;
    }

    if(a1 == 0x1L) {
      //LAB_80042fb0
      return joyData.controllerType_bytee8.get();
    }

    if(a1 == 0x2L) {
      //LAB_80042fbc
      return joyData.shorte6.get();
    }

    if(a1 == 0x3L) {
      //LAB_80042fc8
      return joyData.bytee4.get();
    }

    //LAB_80042f94
    if(a1 == 0x4L) {
      //LAB_80042fd4
      if((int)a2 < 0) {
        return joyData.bytee3.get();
      }

      //LAB_80042fe8
      if((int)a2 < joyData.bytee3.get()) {
        return (int)MEMORY.ref(2, joyData.int00.get()).offset(a2 * 2L).get();
      }

      //LAB_80043020
      return 0;

      //LAB_80043024
    }

    if(a1 == 0x64L) {
      return joyData.counter_int4c.get();
    }

    return 0;
  }

  @Method(0x80043040L)
  public static long FUN_80043040(final int port, final int joypadIndex, final long a2) {
    final JoyData joyData = ptrGetJoyDataForPort_800595e8.deref().run(port);

    if(joypadIndex < 0) {
      return joyData.bytee9.get();
    }

    //LAB_80043078
    if(joypadIndex >= joyData.bytee9.get()) {
      return 0;
    }

    final long v1 = joyData.int04.get() + joypadIndex * 5L;

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
    joypadsReady_8005960c.set(false);

    EnterCriticalSection();

    SysDeqIntRP(2, joypadVblankIrqHandler_80059634);
    SysEnqIntRP(2, joypadVblankIrqHandler_80059634);

    I_STAT.setu(0xffff_fffeL); // Acknowledge VBLANK IRQ
    I_MASK.oru(0x1L); // Enable VBLANK IRQ

    ChangeClearRCnt(3, false); // Don't auto-ACK VBLANK IRQ

    ExitCriticalSection();

    ptrClearJoyData_800595d8.deref().run(ptrArrJoyData_80059608.deref().get(0));
    ptrClearJoyData_800595d8.deref().run(ptrArrJoyData_80059608.deref().get(1));

    _800c3a38.setu(0);
    _800c3a3c.setu(0);

    joypadsReady_8005960c.set(true);
  }

  @Method(0x80043230L)
  public static void FUN_80043230(final ArrayRef<UnsignedByteRef> port0Something, final ArrayRef<UnsignedByteRef> port1Something) {
    joypadsReady_8005960c.set(false);

    _80059620.setu(0);

    initJoypadDataAndCallbacks();

    ptrArrJoyData_80059608.deref().get(0).bytePtr30.set(port0Something);
    ptrArrJoyData_80059608.deref().get(1).bytePtr30.set(port1Something);

    for(int joypadIndex = 0; joypadIndex < 2; joypadIndex++) {
      final JoyData joyData = ptrArrJoyData_80059608.deref().get(joypadIndex);

      //LAB_8004327c
      joyData.joyDataPtr0c.clear();
      joyData.joyDataPtr10.set(joyData);
      joyData.bytePtr30.deref().get(0).set(0xff);
      joyData.bytePtr30.deref().get(1).set(0);

      for(int i = 0; i < 6; i++) {
        //LAB_8004329c
        joyData.getJoypadState46Params_byteArr5d.get(i).set(0xff);
      }
    }

    joypadsReady_8005960c.set(true);
  }

  @Method(0x800432e0L)
  public static void initJoypadDataAndCallbacks() {
    bzero(joyData_800c37b8.getAddress(), 480);

    //PATCH: only use one joypad
    maxJoypadIndex_80059628.setu(0);

    _800595d4.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80043438", long.class), FunctionRef::new));
    ptrClearJoyData_800595d8.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "clearJoyData", JoyData.class), ConsumerRef::new));
    getNextJoypadCommandParam_800595dc.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "getNextJoypadCommandParam", JoyData.class, long.class), BiFunctionRef::new));
    _800595e0.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_800435f8", JoyData.class), ConsumerRef::new));
    ptrGetJoyDataForPort_800595e8.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "getJoyDataForPort", int.class), FunctionRef::new));
    _800595ec.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80043894", JoyData.class), FunctionRef::new));
    _800595f0.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80043cf0", JoyData.class), FunctionRef::new));
    _800595f4.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_800439a4", JoyData.class), ConsumerRef::new));
    _800595f8.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_8004352c", JoyData.class), FunctionRef::new));

    ptrArrJoyData_80059608.set(joyData_800c37b8);
    joyData_800c37b8.get(0).responseBufferPtr_bytePtr3c.set(responseBuffer0_800c3998);
    joyData_800c37b8.get(0).bytePtr40.set(inputBuffer_800c39e0);
    joyData_800c37b8.get(1).responseBufferPtr_bytePtr3c.set(responseBuffer1_800c39bb);
    joyData_800c37b8.get(1).bytePtr40.set(inputBuffer_800c3a03);
  }

  @Method(0x800433d0L)
  public static void clearJoyData(final JoyData joyData) {
    if(joyData.byte49.get() == 0) {
      return;
    }

    joyData.byte49.set(0);
    joyData.byte46.set(0);
    joyData.shorte6.set(0);
    joyData.commandSender.clear();
    joyData.responseHandler.clear();
    joyData.bytee3.set(0);
    joyData.bytee4.set(0);
    joyData.bytee9.set(0);
    joyData.byteea.set(0);
    joyData.int00.set(0);
    joyData.int04.set(0);
    joyData.int08.set(0);

    //LAB_80043420
    for(int i = 0; i < 6; i++) {
      joyData.getJoypadState46Params_byteArr5d.get(i).set(0xff);
    }
  }

  @Method(0x80043438L)
  public static long FUN_80043438(final long a0) {
    long a1 = a0;
    long v0;

    //LAB_80043468
    do {
      final JoyData joyData = joyData_800c37b8.get(joyDataIndex_80059614.get());

      if((int)a1 != -9) {
        if(a1 == 0) {
          joySomething_8005962c.get(joyDataIndex_80059614.get()).set(0);
        } else {
          //LAB_8004349c
          FUN_80043c10(joyData);
          FUN_8004352c(joyData);
        }
      }

      //LAB_800434ac
      JOY_MCD_CTRL.setu(0);
      joyDataIndex_80059614.incr();
      joypadCallbackIndex_80059618.setu(0);

      if(joyDataIndex_80059614.get() <= maxJoypadIndex_80059628.get()) {
        v0 = FUN_80043f18(joyData_800c37b8.get(joyDataIndex_80059614.get()));
      } else {
        v0 = 0x1L;
      }

      //LAB_80043504
      a1 = 0xffffL;

      //LAB_80043508
    } while(v0 == 0);

    return v0;
  }

  @Method(0x8004352cL)
  public static void FUN_8004352c(final JoyData joyData) {
    joyData.byte38.set(joyData.command_byte37);
    joyData.command_byte37.set(0);
  }

  @Method(0x8004353cL)
  public static int getNextJoypadCommandParam(final JoyData joyData, final long unused) {
    final int command = joyData.command_byte37.get();
    final int paramIndex = joyData.responseBytesSent_byte45.get() - 3; // We start sending params in the 4th byte, so skip 3

    if(command == 0) {
      //LAB_80043564
      if(paramIndex < 0x6L && joyData.byteArr57.get(paramIndex).get() == 0) {
        return 0;
      }

      //LAB_80043580
      if(paramIndex >= joyData.byte34.getUnsigned()) {
        return 0;
      }

      //LAB_800435a0
      return joyData.bytePtr28.deref().get(paramIndex).get();
    }

    if(command == 0x4dL) { // Unlock rumble
      //LAB_800435ac
      if(paramIndex >= joyData.commandParamCount_byte36.get()) {
        return 0xff;
      }

      return joyData.ptrCommandParams_bytePtr2c.deref().get(paramIndex).get();
    }

    //LAB_800435cc
    if(paramIndex >= joyData.commandParamCount_byte36.get()) {
      return 0;
    }

    //LAB_800435f0
    return joyData.ptrCommandParams_bytePtr2c.deref().get(paramIndex).get();
  }

  @Method(0x800435f8L)
  public static void FUN_800435f8(final JoyData joyData) {
    bzero(joyData.byteArr57.getAddress(), 0x6);

    if(joyData.shorte6.get() == 0 || joyData.bytePtr28.isNull()) {
      //LAB_80043770
      final int controllerType = joyData.controllerType_bytee8.get();
      if(controllerType < 0x6 || controllerType == 0x7) { // Digital pad, analog stick, or analog pad
        //LAB_80043790
        if(joyData.shorte6.get() == 0 && joyData.byte34.getUnsigned() >= 0x2L) {
          if((joyData.bytePtr28.deref().get(0).get() & 0xc0L) != 0x40L) {
            return;
          }

          if((joyData.bytePtr28.deref().get(1).get() & 0x1L) == 0) {
            return;
          }

          if(_8005961c.get() + 0xaL >= 0x3dL) {
            return;
          }

          joyData.byteArr57.get(0).set(1);
          joyData.byteArr57.get(1).set(1);
          _8005961c.addu(0xaL);
          return;
        }
      }

      //LAB_80043824
      if(joyData.controllerType_bytee8.get() == 0x3) { // Konami light gun for some reason
        joyData.byteArr57.get(0).set(0x1);
        return;
      }

      //LAB_8004383c
      if(joyData.shorte6.get() == 0) {
        //LAB_80043854
        for(int i = 0; i < 6; i++) {
          joyData.byteArr57.get(i).set(0x1);
        }

        //LAB_80043864
      }
    } else {
      final long t1;
      if(joyData.byte34.getUnsigned() < 0x7L) {
        t1 = joyData.byte34.getUnsigned();
      } else {
        t1 = 0x6L;
      }

      //LAB_8004364c
      if(joyData.bytee9.get() == 0) {
        return;
      }

      long t2 = 0;

      //LAB_80043664
      for(int t0 = 0; t0 < joyData.bytee9.get(); t0++) {
        final long a3;
        if(MEMORY.ref(1, joyData.int04.get()).offset(t2).offset(0x2L).get() == 0) {
          a3 = 0x1L;
        } else {
          a3 = 0xffL;
        }

        //LAB_80043684
        long a2 = 0;

        //LAB_80043694
        for(int i = 0; i < t1; i++) {
          if(joyData.getJoypadState46Params_byteArr5d.get(i).get() == t0) {
            if((joyData.bytePtr28.deref().get(i).get() & a3) != 0) {
              //LAB_8004370c
              a2 = 0x1L;
              break;
            }
          }

          //LAB_800436b8
        }

        //LAB_800436cc
        if(a2 != 0) {
          final long v1 = MEMORY.ref(1, joyData.int04.get()).offset(t2).offset(0x3L).get() + _8005961c.get();

          if(v1 < 0x3dL) {
            _8005961c.setu(v1);
          } else {
            //LAB_80043714
            a2 = 0;
          }

          //LAB_80043718
          if(a2 != 0) {
            //LAB_80043730
            for(int i = 0; i < t1; i++) {
              if(joyData.getJoypadState46Params_byteArr5d.get(i).get() == t0) {
                joyData.byteArr57.get(i).set(0x1);
              }

              //LAB_80043744
            }
          }
        }

        //LAB_80043754
        t2 += 0x5L;
      }
    }
  }

  @Method(0x80043874L)
  public static JoyData getJoyDataForPort(final int port) {
    if((port & 0xf0) == 0) {
      return joyData_800c37b8.get(0);
    }

    return joyData_800c37b8.get(1);
  }

  @Method(0x80043894L)
  public static long FUN_80043894(final JoyData joyData) {
    if(joyData.responseBufferPtr_bytePtr3c.deref().get(0).get() == 0xf3L) {
      if(joyData.controllerType_bytee8.get() == 0 || joyData.byte46.get() == 0xffL) {
        queueJoypadCommand43Configure(joyData, false);
        return 0;
      }

      if(joyData.byte49.get() == 0x2L) {
        ptrClearJoyData_800595d8.deref().run(joyData);
      }
    }

    //LAB_80043900
    final long v1 = joyData.byte46.get();
    if(v1 == 0 || v1 == 0xffL) {
      return 0;
    }

    if(v1 == 0x1L) {
      //LAB_80043940
      queueJoypadCommand43Configure(joyData, true);
      return 0;
    }

    if(v1 == 0xfeL) {
      //LAB_80043954
      //LAB_80043958
      queueJoypadCommand43Configure(joyData, false);
      return 0;
    }

    //LAB_80043968
    if(joyData.commandSender.isNull()) {
      //LAB_80043988
      sendNextCommand(joyData);
    } else {
      joyData.commandSender.deref().run(joyData);
    }

    //LAB_80043994
    return 0;
  }

  @Method(0x800439a4L)
  public static void FUN_800439a4(final JoyData joyData) {
    if((joyData.responseBufferPtr_bytePtr3c.deref().get(0).get() & 0xf0L) == 0) { // Haven't read controller ID yet
      joyData.bytePtr30.deref().get(0).set(0xff);
      joyData.bytePtr30.deref().get(1).set(0);
      joyData.controllerType_bytee8.set(0);
      joyData.byte35.setUnsigned(0);
      ptrClearJoyData_800595d8.deref().run(joyData);
      return;
    }

    //LAB_80043a0c
    final int oldControllerType = joyData.controllerType_bytee8.get();
    final int newControllerType = joyData.responseBufferPtr_bytePtr3c.deref().get(0).get() >>> 4;

    if(newControllerType != 0xfL) { // If not in controller config mode
      joyData.controllerType_bytee8.set(newControllerType);

      //LAB_80043a2c
      joyData.bytePtr30.deref().get(0).set(0);
      joyData.bytePtr30.deref().get(1).set(joyData.responseBufferPtr_bytePtr3c.deref().get(0));
      joyData.byte35.setUnsigned(joyData.responseBufferIndex_byte44.get());

      //LAB_80043a60
      for(int a0 = 2; a0 < joyData.responseBufferIndex_byte44.get(); a0++) {
        joyData.bytePtr30.deref().get(a0).set(joyData.responseBufferPtr_bytePtr3c.deref().get(a0));
      }
    }

    //LAB_80043a8c
    //LAB_80043ac4
    //LAB_80043ad4
    if(joyData.responseBufferPtr_bytePtr3c.deref().get(1).get() == 0 && (joyData.byte46.get() != 0x1L || !joyData.commandSender.isNull()) && joyData.byte50.getUnsigned() == 0 || joyData.controllerType_bytee8.get() != oldControllerType) {
      //LAB_80043ae4
      ptrClearJoyData_800595d8.deref().run(joyData);
    }

    //LAB_80043af8
    joyData.byte4a.set(0);

    if(joyData.byte46.get() == 0xff) {
      return;
    }

    if(joyData.byte46.get() != 0 && joyData.command_byte37.get() == 0) {
      return;
    }

    //LAB_80043b24
    if((joyData.byte46.get() - 0x2 & 0xff) < 0xfc) {
      if(joyData.responseBufferPtr_bytePtr3c.deref().get(0).get() != 0xf3) {
        ptrClearJoyData_800595d8.deref().run(joyData);
        return;
      }
    }

    //LAB_80043b68
    final long v1 = joyData.byte46.get();
    if(v1 == 0x1L) {
      //LAB_80043bac
      joyData.byte47.set(0);
    } else if(v1 == 0) {
      //LAB_80043ba0
      joyData.byte49.set(0x1);
    //LAB_80043b90
    } else if(v1 == 0xfeL) {
      //LAB_80043bc0
      joyData.byte46.set(0xff);
      return;
    } else {
      //LAB_80043bc8
      final int ret;
      if(joyData.responseHandler.isNull()) {
        //LAB_80043be8
        ret = FUN_80044928(joyData);
      } else {
        ret = joyData.responseHandler.deref().run(joyData);
      }

      //LAB_80043bf0
      joyData.byte46.add(ret);
      return;
    }

    //LAB_80043bb4
    joyData.byte46.incr();

    //LAB_80043c00
  }

  @Method(0x80043c10L)
  public static void FUN_80043c10(final JoyData joyData) {
    joyData.counter_int4c.incr();

    if(joyData.byte46.get() != 0) {
      if(joyData.byte46.get() == 0x1) {
        if(joyData.byte4a.get() < 0xb) {
          joyData.byte4a.incr();
          return;
        }

        joyData.byte49.set(0x02);
        joyData.byte46.set(0xff);
        return;
      }

      //LAB_80043c68
      if(joyData.byte4a.get() < 0xb) {
        //LAB_80043c7c
        joyData.byte4a.incr();
        return;
      }

      //LAB_80043c84
      if(joyData.byte49.get() != 0) {
        ptrClearJoyData_800595d8.deref().run(joyData);
      }
    }

    //LAB_80043ca8
    if(joyData.responseBufferPtr_bytePtr3c.deref().get(0).get() != 0xf3) { // Not in config mode
      joyData.bytePtr30.deref().get(0).set(0xff);
      joyData.bytePtr30.deref().get(1).set(0);
      joyData.controllerType_bytee8.set(0);
      joyData.byte35.setUnsigned(0);
    }

    //LAB_80043ce0
  }

  @Method(0x80043cf0L)
  public static long FUN_80043cf0(final JoyData joyData) {
    if(joyData.shorte6.get() != 0 && joyData.byte46.get() == 0xff) {
      return 0;
    }

    return 0x1L;
  }

  /**
   * @see Scus94491BpeSegment_8005#joypadVblankIrqHandler_8005952c
   */
  @Method(0x80043d20L)
  public static int joypadVblankIrqHandlerFirstFunction_80043d20() {
    if(I_MASK.get(0x1L) == 0 || I_STAT.get(0x1L) == 0) {
      return 0;
    }

    if(!_800595fc.isNull()) {
      _800595fc.deref().run();
    }

    return 1;
  }

  /**
   * @see Scus94491BpeSegment_8005#joypadVblankIrqHandler_8005952c
   */
  @Method(0x80043d88L)
  public static void joypadVblankIrqHandlerSecondFunction_80043d88(final int firstFunctionReturn) {
    if(JOY_MCD_CTRL.get(0x2L) != 0) { // If JOYn isn't selected
      JOY_MCD_CTRL.setu(0);
      return;
    }

    //LAB_80043db4
    _80059644.setu(0x1L);

    if(_80059624.get() != 0 && _800c3a38.get() < 150L) {
      _800c3a38.addu(0x1L);
    }

    //LAB_80043dec
    if(maxJoypadIndex_80059628.get() == 0 && _800c3a3c.get() < 150L) {
      _800c3a3c.addu(0x1L);
    }

    //LAB_80043e20
    if(!joypadsReady_8005960c.get() || _80059624.get() > maxJoypadIndex_80059628.get()) {
      return;
    }

    joypadCallbackIndex_80059618.setu(0);
    joyDataIndex_80059614.set((int)_80059624.get()); // Always 0?

    if(FUN_80043f18(ptrArrJoyData_80059608.deref().get(joyDataIndex_80059614.get())) == 0) {
      _800595d4.deref().run(0xffffL);
    }

    //LAB_80043e98
    _8005961c.setu(0);

    //LAB_80043ebc
    while(joyDataIndex_80059614.get() <= maxJoypadIndex_80059628.get()) {
      executeJoypadCallback(ptrArrJoyData_80059608.deref().get(joyDataIndex_80059614.get()));
    }

    //LAB_80043ef8
    JOY_MCD_BAUD.setu(0x88L);

    //LAB_80043f08
  }

  @Method(0x80043f18L)
  public static long FUN_80043f18(final JoyData joyData) {
    JOY_MCD_CTRL.setu(0x40L); // Reset
    JOY_MCD_CTRL.setu(0);
    JOY_MCD_MODE.setu(0xdL);
    JOY_MCD_BAUD.setu(0x88L);

    if(joyData.controllerType_bytee8.get() == 0x8) { // Multitap
      setJoypadTimeout(80L);
    } else {
      setJoypadTimeout(145L);
    }

    //LAB_80043f64
    if(joyDataIndex_80059614.get() == 0) {
      JOY_MCD_CTRL.setu(0x1003L);
    } else {
      JOY_MCD_CTRL.setu(0x3003L);
    }

    //LAB_80043f88
    //LAB_80043fb4
    while(joySomething_8005962c.get(joyDataIndex_80059614.get()).get() > 0) {
      joySomething_8005962c.get(joyDataIndex_80059614.get()).decr();
      _800595f4.deref().run(joyData.joyDataPtr0c.deref().get(joySomething_8005962c.get(joyDataIndex_80059614.get()).get()));
    }

    //LAB_80044020
    if(joySomething_8005962c.get(joyDataIndex_80059614.get()).get() == 0) {
      joySomething_8005962c.get(joyDataIndex_80059614.get()).set(-1);
      _800595f4.deref().run(joyData);
      _800595f8.deref().run(joyData);
    }

    //LAB_80044074
    if(JOY_MCD_STAT.get(0x200L) != 0) { // Interrupt request
      JOY_MCD_CTRL.oru(0x10L); // ACK

      if(JOY_MCD_STAT.get(0x200L) != 0) { // Interrupt request
        //LAB_800440b8
        while(!checkJoypadTimeout()) {
          DebugHelper.sleep(1);
        }

        JOY_MCD_DATA.setu(0x1L); // Begin controller access

        setJoypadTimeout(2000L);
        if(!acknowledgeJoypadInterrupt()) {
          return 0;
        }

        waitForJoypadData();

        //LAB_80044114
        setJoypadTimeout(430L);
        while(!SKIP_JOYPAD_INTERRUPT_CHECKS && I_STAT.get(0x80L) == 0) {
          if(checkJoypadTimeout()) {
            assert false : "Joypad timeout";
            return 0;
          }
        }

        JOY_MCD_DATA.setu(0x42L);

        setJoypadTimeout(60L);
        if(!acknowledgeJoypadInterrupt()) {
          return 0;
        }

        waitForJoypadData();

        //LAB_80044190
        setJoypadTimeout(430L);
        while(!SKIP_JOYPAD_INTERRUPT_CHECKS && I_STAT.get(0x80L) == 0) {
          if(checkJoypadTimeout()) {
            assert false : "Joypad timeout";
            return 0;
          }
        }

        JOY_MCD_DATA.setu(0x1L);

        setJoypadTimeout(60L);
        if(!acknowledgeJoypadInterrupt()) {
          return 0;
        }

        waitForJoypadData();
        return 0;
      }

      //LAB_80044204
      I_STAT.setu(0xffff_ff7fL);
    }

    //LAB_80044214
    if(joyData.byte50.getUnsigned() == 0) {
      return 0x1L;
    }

    if(joyData.command_byte37.get() == 0) {
      return 0x1L;
    }

    //LAB_80044238
    return 0;
  }

  @Method(0x8004424cL)
  public static void executeJoypadCallback(final JoyData joyData) {
    LOGGER.debug("Executing joypad callback %d", joypadCallbackIndex_80059618.get());

    //NOTE: we need to increment before executing the callback - some callbacks set the value, and we don't want to overwrite that
    joypadCallbackIndex_80059618.addu(0x1L);
    final long ret = joypadCallbacks_8005965c.get((int)joypadCallbackIndex_80059618.get() - 1).deref().run(joyData);

    if((int)ret < 0) {
      //LAB_80044318
      _800595d4.deref().run(ret);
      return;
    }

    if(joypadCallbackIndex_80059618.get() != 0 && (joypadCallbackIndex_80059618.get() != 0x3L || joyData.responseBufferPtr_bytePtr3c.deref().get(0).get() != 0x80L)) {
      //LAB_800442c8
      setJoypadTimeout(60L);

      if(!acknowledgeJoypadInterrupt()) {
        _800595d4.deref().run(-0x3L);
      }
    }

    //LAB_80044300
    if(joypadCallbackIndex_80059618.get() > 4) {
      joypadCallbackIndex_80059618.subu(1);
    }

    //LAB_8004432c
  }

  /**
   * Command can be NOT'd ("~1/ffff_fffe" means send command "1" but the flow is slightly different, haven't fully figured it out yet)
   */
  @Method(0x8004433cL)
  public static int sendJoypadCommand(final JoyData joyData, final int command) {
    if(command < 0) {
      joyData.responseBufferIndex_byte44.set(-1);
      joyData.responseBytesSent_byte45.set(1);
      joyData.bytePtr40.deref().get(0).setUnsigned(~command);

      final int data = (int)JOY_MCD_DATA.get(0xffL);

      //LAB_800443a8
      while(JOY_MCD_STAT.get(0x1L) == 0) { // While not ready
        DebugHelper.sleep(1);
      }

      //LAB_800443bc
//      while(!checkJoypadTimeout()) {
//        DebugHelper.sleep(1);
//      }

      JOY_MCD_DATA.setu(~command);
      return data;
    }

    //LAB_800443e4
    //LAB_80044418
    //LAB_8004441c
    joypadTimeoutTimeout_800c3a30.setu(430L);
    joypadTimeoutCurrentTime_800c3a2c.setu(TMR_SYSCLOCK_VAL);
    joypadTimeoutMode_800c3a34.setu(TMR_SYSCLOCK_MODE);

    //LAB_80044464
    while(JOY_MCD_STAT.get(0x2L) == 0) {
      DebugHelper.sleep(1);
    }

    //LAB_80044478
    final int data = (int)JOY_MCD_DATA.get(0xffL);

    JOY_MCD_BAUD.setu(0x88L);

    //LAB_800444a4
    while(!SKIP_JOYPAD_INTERRUPT_CHECKS && I_STAT.get(0x80L) == 0) { // Wait for joypad interrupt
      //TODO Removable? Shouldn't need to wait
//      if(checkJoypadTimeout()) {
//        assert false : "Joypad timeout";
//        return 0xffff_ffecL;
//      }

      DebugHelper.sleep(1);
    }

    //LAB_800444d4
    JOY_MCD_DATA.setu(command);

    //LAB_80044514
    joyData.responseBufferPtr_bytePtr3c.deref().get(joyData.responseBufferIndex_byte44.get()).set(data);
    joyData.responseBufferIndex_byte44.incr();
    joyData.responseBytesSent_byte45.incr();

    //LAB_80044544
    return data;
  }

  @Method(0x80044560L)
  public static int sendJoypadData(final JoyData joyData, final int data) {
    //LAB_800445b4
    //LAB_800445c0
    while(JOY_MCD_STAT.get(0x2L) == 0) { // While RX FIFO empty
      DebugHelper.sleep(1);
    }

    setJoypadTimeout(400L);

    final int response = (int)JOY_MCD_DATA.get(0xffL);

    //LAB_8004460c
    JOY_MCD_BAUD.setu(0x88L);

    //LAB_8004461c
    //LAB_8004466c
    while(!SKIP_JOYPAD_INTERRUPT_CHECKS && I_STAT.get(0x80L) == 0) { // Controller/memcard byte received interrupt
//      TMR_SYSCLOCK_MODE.get(); // Read to nowhere - not sure why

//      long time = TMR_SYSCLOCK_VAL.get(0xffffL);
//      if(time < joypadTimeoutCurrentTime_800c3a2c.get()) {
//        if(TMR_SYSCLOCK_MAX.get() == 0) {
          //LAB_800446a4
//          time += 0x1_0000L;
//        } else {
//          time += TMR_SYSCLOCK_MAX.get();
//        }
//      }

      //LAB_800446a8
//      long timeout = time - joypadTimeoutCurrentTime_800c3a2c.get();

//      if(TMR_SYSCLOCK_MODE.get(0x200L) == 0) {
        //LAB_800446d0
//        timeout /= 8;
//      }

//      if(timeout >= joypadTimeoutTimeout_800c3a30.get()) {
//        LOGGER.error("Joypad timeout! Current time: %d, max time: %d", time, joypadTimeoutTimeout_800c3a30.get());
//        assert false : "Joypad timeout";
//        return -0x2L;
//      }

      DebugHelper.sleep(1);

      //LAB_800446e0
    }

    //LAB_800446f4
    if(joypadCallbackIndex_80059618.get() == 0x2L) {
      setJoypadTimeout(60L);

      //LAB_80044720
      while(!checkJoypadTimeout()) {
        DebugHelper.sleep(1);
      }
    }

    //LAB_80044730
    JOY_MCD_DATA.setu(data);

    if(joypadCallbackIndex_80059618.get() == 0x3L && response == 0x80) {
      I_STAT.setu(0xffff_ff7fL);
      JOY_MCD_CTRL.oru(0x10L); // ACK
    }

    //LAB_80044780
    joyData.responseBytesSent_byte45.incr();

    if(joyData.responseBufferIndex_byte44.get() != -1) {
      joyData.responseBufferPtr_bytePtr3c.deref().get(joyData.responseBufferIndex_byte44.get()).set(response);
    }

    //LAB_800447b0
    joyData.responseBufferIndex_byte44.incr();

    //LAB_800447c0
    return response;
  }

  @Method(0x800447dcL)
  public static boolean acknowledgeJoypadInterrupt() {
    I_STAT.setu(0xffff_ff7fL); // Acknowledge controller interrupt

    //LAB_80044810
    while(JOY_MCD_STAT.get(0x80L) != 0) { // While /ACK input level low
      if(checkJoypadTimeout()) {
        assert false : "Joypad timeout";
        return false;
      }
    }

    //LAB_80044840
    JOY_MCD_CTRL.oru(0x10L); // ACK

    //LAB_8004485c
    return true;
  }

  @Method(0x8004486cL)
  public static void waitForJoypadData() {
    while(JOY_MCD_STAT.get(0x2L) == 0) { // While RX FIFO not empty
      DebugHelper.sleep(1);
    }
  }

  @Method(0x80044894L)
  public static void queueJoypadCommand(final JoyData joyData, final int command, final ArrayRef<UnsignedByteRef> params, final int paramCount) {
    joyData.command_byte37.set(command);
    joyData.ptrCommandParams_bytePtr2c.set(params);
    joyData.commandParamCount_byte36.set(paramCount);
  }

  @Method(0x800448a4L)
  public static void sendNextCommand(final JoyData joyData) {
    final int v1 = joyData.byte46.get();

    if(v1 == 0x2) {
      queueJoypadCommand45GetStatus(joyData);
    }

    if(v1 == 0x3) {
      queueJoypadCommand4cUnknown(joyData, joyData.bytee4.get());
    }

    if(v1 == 0x4) {
      queueJoypadCommand47Unknown(joyData, joyData.byte47.get());
    }
  }

  @Method(0x80044928L)
  public static int FUN_80044928(final JoyData joyData) {
    final int v1 = joyData.byte46.get();

    if(v1 == 0x2L) {
      //LAB_80044974
      if(joyData.responseBufferPtr_bytePtr3c.deref().get(7).get() != 0) {
        return 0;
      }

      if(joyData.bytee3.get() == joyData.responseBufferPtr_bytePtr3c.deref().get(3).get() && joyData.bytee4.get() == joyData.responseBufferPtr_bytePtr3c.deref().get(4).get() && joyData.bytee9.get() == joyData.responseBufferPtr_bytePtr3c.deref().get(5).get() && joyData.byteea.get() == joyData.responseBufferPtr_bytePtr3c.deref().get(6).get()) {
        joyData.shortee.set(0);
      } else {
        //LAB_800449e4
        joyData.shortee.set(0xffff);
      }

      //LAB_800449e8
      joyData.bytee3.set(joyData.responseBufferPtr_bytePtr3c.deref().get(3));
      joyData.bytee4.set(joyData.responseBufferPtr_bytePtr3c.deref().get(4));
      joyData.shorte6.set(0);
      joyData.bytee9.set(joyData.responseBufferPtr_bytePtr3c.deref().get(5));
      joyData.byteea.set(joyData.responseBufferPtr_bytePtr3c.deref().get(6));
      joyData.shortec.set(0);

      if(joyData.shortee.get() != 0) {
        return 0;
      }

      joyData.byteeb.set(0);
      return 1;
    }

    if(v1 == 0x3) {
      //LAB_80044a34
      if(joyData.responseBufferPtr_bytePtr3c.deref().get(2).get() != 0) {
        return 0;
      }

      if(joyData.responseBufferPtr_bytePtr3c.deref().get(3).get() != 0) {
        return 0;
      }

      joyData.shorte6.set(joyData.responseBufferPtr_bytePtr3c.deref().get(4).get() << 0x8L | joyData.responseBufferPtr_bytePtr3c.deref().get(5).get());

      if(joyData.shortee.get() != joyData.shorte6.get()) {
        joyData.shortee.set(joyData.shorte6.get());

        //LAB_80044a80
        return 0;
      }

      //LAB_80044a88
      joyData.shortee.set(0xffff);
      joyData.byteeb.set(0);
      joyData.byte47.set(0);
      return 1;
    }

    //LAB_80044960
    if(v1 == 0x4) {
      //LAB_80044a9c
      if(joyData.responseBufferPtr_bytePtr3c.deref().get(2).get() != 0) {
        return 0;
      }

      if(joyData.responseBufferPtr_bytePtr3c.deref().get(3).get() != 0) {
        return 0;
      }

      joyData.byte47.incr();
      joyData.shortec.add(((joyData.responseBufferPtr_bytePtr3c.deref().get(4).get() + 0x3 & 0x1fc) + 0x8));

      if(joyData.byte47.get() < joyData.byteea.get()) {
        return 0;
      }

      if(FUN_80044b98(joyData) > 0x80L) {
        ptrClearJoyData_800595d8.deref().run(joyData);
        joyData.byte46.set(0xfe);
        joyData.byte49.set(0x2);
        return 0;
      }

      //LAB_80044b38
      if(joyData.shortee.get() != joyData.shortec.get()) {
        joyData.shortee.set(joyData.shortec.get());
        joyData.byte47.set(0);
        joyData.shortec.set(0);
        return 0;
      }

      //LAB_80044b5c
      joyData.shortee.set(0);
      joyData.byteeb.set(0);
      joyData.byte46.set(0xff);
      FUN_80044bd0(joyData, joyData.getAddress() + 0x63L /*TODO gotta figure out what +63 is*/);
      joyData.byte46.set(0x2);
      return 0;
    }

    //LAB_80044b84
    //LAB_80044b88
    return 1;
  }

  @Method(0x80044b98L)
  public static long FUN_80044b98(final JoyData joyData) {
    return
      (joyData.bytee3.get() + 1L) / 2L * 4L +
      (joyData.bytee9.get() * 5L + 0x3L & 0xffcL) +
      joyData.shortec.get() +
      0x4L;
  }

  @Method(0x80044bd0L)
  public static long FUN_80044bd0(final JoyData joyData, final long a1) {
    if(a1 == 0) {
      return 0;
    }

    if(joyData.int04.get() != 0) {
      return 0;
    }

    if(_800595f0.deref().run(joyData) != 0) {
      //LAB_80044c18
      return 0;
    }

    //LAB_80044c20
    joyData.byte47.set(0);
    joyData.byte46.set(0x1);
    joyData.byte49.set(0x4);

    joyData.commandSender.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80044ca4", JoyData.class)).cast(ConsumerRef::new));
    joyData.responseHandler.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8004.class, "FUN_80044d4c", JoyData.class)).cast(FunctionRef::new));

    long a = (a1 + 0x3L) / 4L * 4L;
    joyData.int00.set(a);

    a += (joyData.bytee3.get() + 1L) / 2L * 4L;
    joyData.int04.set(a);

    a += joyData.bytee9.get() * 5L + 0x3L & 0xffcL;
    joyData.int08.set(a);

    //LAB_80044c90
    return 0x1L;
  }

  @Method(0x80044ca4L)
  public static void FUN_80044ca4(final JoyData joyData) {
    final int v1 = joyData.byte46.get();

    if(v1 == 0x2) {
      //LAB_80044ce8
      queueJoypadCommand4cUnknown(joyData, joyData.byte47.get());
      return;
    }

    if(v1 == 0x3) {
      //LAB_80044cfc
      queueJoypadCommand46Unknown(joyData, joyData.byte47.get());
      return;
    }

    //LAB_80044cd4
    if(v1 == 0x4) {
      //LAB_80044d10
      if(joyData.byte48.get() == 0) {
        queueJoypadCommand47Unknown(joyData, joyData.byte47.get());
      } else {
        //LAB_80044d34
        queueJoypadCommand4bUnknown(joyData);
      }
    }

    //LAB_80044d3c
  }

  @Method(0x80044d4cL)
  public static int FUN_80044d4c(final JoyData joyData) {
    final long a0;

    final int v1 = joyData.byte46.get();

    if(v1 == 0x2) {
      //LAB_80044d8c
      if(joyData.responseBufferPtr_bytePtr3c.deref().get(2).get() != 0) {
        return 0;
      }

      if(joyData.responseBufferPtr_bytePtr3c.deref().get(3).get() != 0) {
        return 0;
      }

      a0 = joyData.int00.get() + joyData.byte47.get() * 2L;
      MEMORY.ref(2, a0).setu(joyData.responseBufferPtr_bytePtr3c.deref().get(4).get() << 8 | joyData.responseBufferPtr_bytePtr3c.deref().get(5).get());
      final long v0 = joyData.int00.get() + joyData.byte47.get() * 2L;
      if(joyData.shortee.get() != MEMORY.ref(2, v0).get()) {
        joyData.shortee.set((int)MEMORY.ref(2, v0).get());

        //LAB_80044e00
        return 0;
      }

      //LAB_80044e08
      joyData.shortee.set(0);
      joyData.byteeb.set(0);
      joyData.byte47.incr();
      if(joyData.byte47.get() < joyData.bytee3.get()) {
        return 0;
      }

      joyData.byte47.set(0);
      return 1;
    }

    if(v1 == 0x3) {
      //LAB_80044e38
      if(joyData.responseBufferPtr_bytePtr3c.deref().get(2).get() != 0) {
        return 0;
      }

      if(joyData.responseBufferPtr_bytePtr3c.deref().get(3).get() != 0) {
        return 0;
      }

      a0 = joyData.int04.get() + joyData.byte47.get() * 5L;
      if(
        MEMORY.ref(1, a0).offset(0x0L).get() == joyData.responseBufferPtr_bytePtr3c.deref().get(4).get() &&
        MEMORY.ref(1, a0).offset(0x1L).get() == (joyData.responseBufferPtr_bytePtr3c.deref().get(5).get() & 0x7f) &&
        MEMORY.ref(1, a0).offset(0x2L).get() == joyData.responseBufferPtr_bytePtr3c.deref().get(6).get() &&
        MEMORY.ref(1, a0).offset(0x3L).get() == joyData.responseBufferPtr_bytePtr3c.deref().get(7).get() &&
        MEMORY.ref(1, a0).offset(0x4L).get() == joyData.responseBufferPtr_bytePtr3c.deref().get(5).get() / 0x80
      ) {
        joyData.shortee.set(0);
      } else {
        joyData.shortee.set(0xffff);
      }

      //LAB_80044ee0
      MEMORY.ref(1, a0).offset(0x0L).setu(joyData.responseBufferPtr_bytePtr3c.deref().get(4).get());
      MEMORY.ref(1, a0).offset(0x1L).setu(joyData.responseBufferPtr_bytePtr3c.deref().get(5).get() & 0x7fL);
      MEMORY.ref(1, a0).offset(0x2L).setu(joyData.responseBufferPtr_bytePtr3c.deref().get(6).get());
      MEMORY.ref(1, a0).offset(0x3L).setu(joyData.responseBufferPtr_bytePtr3c.deref().get(7).get());
      MEMORY.ref(1, a0).offset(0x4L).setu(joyData.responseBufferPtr_bytePtr3c.deref().get(5).get() / 0x80);

      if(joyData.shortee.get() != 0) {
        return 0;
      }

      joyData.byteeb.set(0);
      joyData.byte47.incr();

      if(joyData.byte47.get() < joyData.bytee9.get()) {
        return 0;
      }

      joyData.byte47.set(0);
      joyData.byte48.set(0);
      return 1;
    }

    //LAB_80044d78
    if(v1 == 0x4L) {
      //LAB_80044f90
      if(joyData.responseBufferPtr_bytePtr3c.deref().get(2).get() != 0) {
        joyData.byte48.set(0);
        return 0;
      }

      //LAB_80044fb0
      final long t0 = joyData.int08.get() + joyData.byte47.get() * 8L;
      final int a0_1;

      if(joyData.byte48.get() == 0) {
        final int v0 = joyData.responseBufferPtr_bytePtr3c.deref().get(4).get();
        joyData.byte48.set(v0);
        MEMORY.ref(1, t0).setu(v0);

        final long v1_1;
        if(joyData.byte47.get() == 0) {
          v1_1 = joyData.int08.get() + joyData.byteea.get() * 8L;
        } else {
          //LAB_80044ffc
          v1_1 = MEMORY.ref(4, t0).offset(-0x4L).get() + (MEMORY.ref(1, t0).offset(-0x8L).get() + 0x3L & 0x1fcL);
        }

        //LAB_8004500c
        MEMORY.ref(4, t0).offset(0x4L).setu(v1_1);
        _800c3a28.setu(v1_1);

        a0_1 = 5;
      } else {
        //LAB_80045024
        a0_1 = 3;
      }

      outer:
      {
        //LAB_80045030
        //LAB_80045050
        for(int i = a0_1; i < 8; i++) {
          if(joyData.byte48.get() == 0) {
            break outer;
          }

          if(_800c3a28.get() >= joyData.bytee3.getAddress()) {
            //LAB_8004512c
            joyData.byte47.set(0);
            joyData.byte48.set(0);
            return 0;
          }

          if(_800c3a28.deref(1).get() != joyData.responseBufferPtr_bytePtr3c.deref().get(i).get()) {
            joyData.shortee.set(0xffff);
          }

          //LAB_80045090
          _800c3a28.deref(1).setu(joyData.responseBufferPtr_bytePtr3c.deref().get(i).get());
          _800c3a28.addu(0x1L);
          joyData.byte48.decr();
        }

        //LAB_800450bc
        if(joyData.byte48.get() != 0) {
          return 0;
        }
      }

      //LAB_800450cc
      if(joyData.shortee.get() != 0) {
        joyData.shortee.set(0);
        joyData.byte48.set(0);
        return 0;
      }

      //LAB_800450e8
      joyData.byte47.incr();

      if(joyData.byteea.get() <= joyData.byte47.get()) {
        joyData.byte49.set(0x6);
        joyData.byte46.set(0xfe);
        joyData.byteeb.set(0);
        return 0;
      }

      //LAB_80045120
      joyData.byte48.set(0);
      joyData.byteeb.set(0);

      //LAB_80045138
      //LAB_8004513c
      return 0;
    }

    return 1;
  }

  @Method(0x80045144L)
  public static void queueJoypadCommand43Configure(final JoyData joyData, final boolean enterConfigMode) {
    joyData.command_byte37.set(0x43);
    joyData.ptrCommandParams_bytePtr2c.set(joyData.commandParams_byteArr24);
    joyData.commandParams_byteArr24.get(0).set(enterConfigMode ? 1 : 0);
    joyData.commandParamCount_byte36.set(1);
  }

  @Method(0x80045164L)
  public static void queueJoypadCommand45GetStatus(final JoyData joyData) {
    joyData.command_byte37.set(0x45);
    joyData.ptrCommandParams_bytePtr2c.clear();
    joyData.commandParamCount_byte36.set(0);
  }

  @Method(0x80045178L)
  public static void queueJoypadCommand4cUnknown(final JoyData joyData, final int unknownParam) {
    joyData.command_byte37.set(0x4c);
    joyData.ptrCommandParams_bytePtr2c.set(joyData.commandParams_byteArr24);
    joyData.commandParams_byteArr24.get(0).set(unknownParam);
    joyData.commandParamCount_byte36.set(1);
  }

  @Method(0x80045198L)
  public static void queueJoypadCommand46Unknown(final JoyData joyData, final int unknownParam) {
    joyData.command_byte37.set(0x46);
    joyData.ptrCommandParams_bytePtr2c.set(joyData.commandParams_byteArr24);
    joyData.commandParams_byteArr24.get(0).set(unknownParam);
    joyData.commandParamCount_byte36.set(1);
  }

  @Method(0x800451b8L)
  public static void queueJoypadCommand47Unknown(final JoyData joyData, final int unknownParam) {
    joyData.command_byte37.set(0x47);
    joyData.ptrCommandParams_bytePtr2c.set(joyData.commandParams_byteArr24);
    joyData.commandParams_byteArr24.get(0).set(unknownParam);
    joyData.commandParamCount_byte36.set(1);
  }

  @Method(0x800451d8L)
  public static void queueJoypadCommand4bUnknown(final JoyData joyData) {
    joyData.command_byte37.set(0x4b);
    joyData.ptrCommandParams_bytePtr2c.clear();
    joyData.commandParamCount_byte36.set(0);
  }

  @Method(0x800451ecL)
  public static void setJoypadTimeout(final long timer) {
    joypadTimeoutCurrentTime_800c3a2c.setu(TMR_SYSCLOCK_VAL);
    joypadTimeoutTimeout_800c3a30.setu(timer);
  }

  @Method(0x8004520cL)
  public static boolean checkJoypadTimeout() {
    long time = TMR_SYSCLOCK_VAL.get(0xffffL);

    if(time < joypadTimeoutCurrentTime_800c3a2c.get()) {
      if(TMR_SYSCLOCK_MAX.get() == 0) {
        time += 0x1_0000L;
      } else {
        time += TMR_SYSCLOCK_MAX.get();
      }
    }

    //LAB_80045254
    long elapsed = time - joypadTimeoutCurrentTime_800c3a2c.get();

    if(TMR_SYSCLOCK_MODE.get(0x200L) == 0) { // If not already divided by 8
      elapsed /= 8;
    }

    //LAB_800452a0
    return joypadTimeoutTimeout_800c3a30.get() <= elapsed;
  }

  @Method(0x800452acL)
  public static long joypadCallback0(final JoyData joyData) {
    _80059654.setu(_800595ec.deref().run(joyData));
    joyData.responseBufferPtr_bytePtr3c.deref().get(0).set(0);
    return sendJoypadCommand(joyData, ~0x1);
  }

  @Method(0x800452f4L)
  public static long joypadCallback1(final JoyData joyData) {
    if(joyDataIndex_80059614.get() == _80059624.get() && _80059610.get() != 0) {
      _80059604.deref().run();
      _80059600.deref().run();
    }

    //LAB_80045354
    if(_80059654.get() != 0) {
      _800595ec.deref().run(joyData.joyDataPtr0c.deref().get(0));
      _800595ec.deref().run(joyData.joyDataPtr0c.deref().get(1));
    }

    //LAB_80045398
    final int data;
    if(joyData.command_byte37.get() == 0) {
      data = 0x42;
    } else {
      //LAB_800453b0
      data = joyData.command_byte37.get();
    }

    //LAB_800453b4
    return sendJoypadData(joyData, data);
  }

  @Method(0x800453ccL)
  public static long joypadCallback2(final JoyData joyData) {
    if(_80059654.get() != 0) {
      _800595ec.deref().run(joyData.joyDataPtr0c.deref().get(2));
      _800595ec.deref().run(joyData.joyDataPtr0c.deref().get(3));
    }

    //LAB_80045418
    final int command = joyData.command_byte37.get();

    //LAB_80045430
    int response = sendJoypadData(joyData, command == 0 ? (int)_80059620.get() : 0);
    if(response >= 0) {
      response &= 0xfL;

      if(response == 0) {
        _80059650.setu(0x20L);
      } else {
        _80059650.setu(response * 2);
      }

      return 0;
    }

    //LAB_80045468
    return response;
  }

  @Method(0x80045478L)
  public static long joypadCallback3(final JoyData joyData) {
    if(_80059620.get() != 0 && joyData.responseBufferPtr_bytePtr3c.deref().get(0).get() >>> 4 == 0x8L) {
      _80059658.setu(joyData.command_byte37.get() == 0 ? 1 : 0);
    } else {
      _80059658.setu(0);
    }

    //LAB_800454c0
    if(_80059658.get() == 0) {
      if(joyData.command_byte37.get() == 0 && joyData.byte38.get() == 0) {
        if(joyData.joyDataPtr10.getPointer() == joyData.getAddress() || joyData.byte39.getUnsigned() == 0) {
          //LAB_8004550c
          if(joyData.bytePtr30.deref().get(0).get() == 0) {
            _800595e0.deref().run(joyData);
          }
        }
      }
    }

    //LAB_80045538
    final int data = getNextJoypadCommandParam_800595dc.deref().run(joyData, _80059658.get());
    final int response = sendJoypadData(joyData, data);

    if(response != 0x5aL) {
      if(response > 0) {
        return -0x4L;
      }
    }

    //LAB_80045584
    return response;
  }

  @Method(0x80045594L)
  public static long joypadCallback4(final JoyData a0) {
    if(_80059658.get() != 0 && a0.command_byte37.get() == 0 && a0.byte38.get() == 0) {
      if(a0.getAddress() == a0.joyDataPtr10.getPointer() || a0.byte39.get() == 0) {
        //LAB_80045608
        if(a0.bytePtr30.deref().get(0).get() == 0) {
          _800595e0.deref().run(a0);
        }
      }
    }

    //LAB_80045634
    final long s5 = _80059658.get();
    if(s5 != 0) {
      //LAB_80045650
      int s0 = -1;
      do {
        _80059650.subu(0x1L);
        if(_80059650.getSigned() <= 0) {
          break;
        }

        if(s0 >= 0) {
          final JoyData a = a0.joyDataPtr0c.deref().get(s0);
          if(a.command_byte37.get() == 0 && a.byte38.get() == 0) {
            if(a.getAddress() == a.joyDataPtr10.getPointer() || a.byte39.getAddress() == 0) {
              //LAB_800456c0
              if(a.bytePtr30.deref().get(0).get() == 0) {
                _800595e0.deref().run(a);
              }
            }
          }

          //LAB_800456ec
        }

        //LAB_800456f0
        final int data = getNextJoypadCommandParam_800595dc.deref().run(a0, 0x1L);
        final int response = sendJoypadData(a0, data);
        if(response < 0) {
          return response;
        }

        I_STAT.setu(-0x81L);
        joypadTimeoutTimeout_800c3a30.setu(0x3cL);
        joypadTimeoutCurrentTime_800c3a2c.setu(TMR_SYSCLOCK_VAL);

        //LAB_8004575c;
        while(JOY_MCD_STAT.get(0x80L) != 0) {
          if(checkJoypadTimeout()) {
            assert false : "Joypad timeout";
            //LAB_800457a8
            return -0x3L;
          }
        }

        //LAB_8004578c
        JOY_MCD_CTRL.oru(0x10L); // ACK

        s0++;
      } while(s0 < 0x4L);
    }

    //LAB_800457bc
    if(_80059650.getSigned() >= 0x2L) {
      final int joyDataIndex = joyDataIndex_80059614.get() < 1 ? 1 : 0;

      ArrayRef<JoyData> joyDataArr = null;
      int lastJoyData = 0;

      //LAB_80045804
      do {
        final int a4 = joySomething_8005962c.get(joyDataIndex).get();
        if(a4 < 0) {
          break;
        }

        if(a4 > 0) {
          joyDataArr = ptrArrJoyData_80059608.deref().get(joyDataIndex).joyDataPtr0c.deref();
          lastJoyData = a4 - 1;
          _800595f4.deref().run(joyDataArr.get(lastJoyData));
        }

        //LAB_80045850
        final long v1 = joySomething_8005962c.get(joyDataIndex).get();
        if(v1 == 0x3L) {
          //LAB_80045898
          _800595f4.deref().run(joyDataArr.get(lastJoyData - 1));

          joySomething_8005962c.get(joyDataIndex).set(1);
        } else if(v1 >= 0x4L) {
          //LAB_80045884
          if(v1 == 0x4L) {
            joySomething_8005962c.get(joyDataIndex).set(3);
          }
        } else if((int)v1 < 0x2L && (int)v1 >= 0) {
          //LAB_800458b4
          joyDataArr = ptrArrJoyData_80059608.deref();
          lastJoyData = joyDataIndex;
          final JoyData joyData = joyDataArr.get(lastJoyData);
          _800595f4.deref().run(joyData);
          _800595f8.deref().run(joyData);

          //LAB_800458f0
          joySomething_8005962c.get(joyDataIndex).set(-1);
        }

        //LAB_800458f8
        final int command = getNextJoypadCommandParam_800595dc.deref().run(a0, s5);
        final int response = sendJoypadCommand(a0, command);

        if(response < 0) {
          return response;
        }

        I_STAT.setu(0xffff_ff7fL);
        joypadTimeoutTimeout_800c3a30.setu(0x3cL);
        joypadTimeoutCurrentTime_800c3a2c.setu(TMR_SYSCLOCK_VAL);

        //LAB_80045964
        while(JOY_MCD_STAT.get(0x80L) != 0) {
          if(checkJoypadTimeout()) {
            assert false : "Joypad timeout";
            //LAB_800459b0
            return -0x3L;
          }
        }

        //LAB_80045994
        JOY_MCD_CTRL.oru(0x10L); // ACK

        _80059650.subu(0x1L);
      } while(_80059650.getSigned() >= 0x2L);
    }

    //LAB_800459dc
    //LAB_80045a0c
    while(_80059650.subu(0x1L).getSigned() > 0) {
      final int command = getNextJoypadCommandParam_800595dc.deref().run(a0, s5);
      final int response = sendJoypadCommand(a0, command);

      if(response < 0) {
        return response;
      }

      if(JOY_MCD_BAUD.get() != 0x22L) {
        I_STAT.setu(-0x81L);
        joypadTimeoutTimeout_800c3a30.setu(0x3cL);
        joypadTimeoutCurrentTime_800c3a2c.setu(TMR_SYSCLOCK_VAL);

        //LAB_80045a84
        while(JOY_MCD_STAT.get(0x80L) != 0) {
          if(checkJoypadTimeout()) {
            assert false : "Joypad timeout";
            //LAB_80045ad0
            //LAB_80045ad8
            return -0x3L;
          }
        }

        //LAB_80045ab4
        JOY_MCD_CTRL.oru(0x10L); // ACK
      }

      //LAB_80045ae0
    }

    //LAB_80045b00
    //LAB_80045b0c
    while(JOY_MCD_STAT.get(0x2L) == 0) {
      DebugHelper.sleep(1);
    }

    a0.responseBufferPtr_bytePtr3c.deref().get(a0.responseBufferIndex_byte44.get()).set((int)JOY_MCD_DATA.get(0xff));
    a0.responseBufferIndex_byte44.incr();

    _800595d4.deref().run(0L);

    //LAB_80045b60
    return 0;
  }

  // End of joypad code

  // Start of SPU code

  @Method(0x80045cb8L)
  public static void FUN_80045cb8() {
    FUN_8004b7c0(0x1L);
    FUN_8004a8b8();

    LAB_80045d04:
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final SpuStruct124 spu124 = _800c4ac8.get(voiceIndex);

      if(spu124._028.get() == 0x1L || spu124._02a.get() == 0x1L) {
        //LAB_80045d24
        FUN_80047b38(voiceIndex);

        //LAB_80045d40
        while(spu124._118.get() == 0) {
          FUN_8004a7ec(voiceIndex);

          if((FUN_80047bd0(voiceIndex) & 0xffffL) == 0) {
            spu124._00c.add(0x3L);
            _800c6634.setu(0);
          } else {
            //LAB_80045d7c
            _800c6634.setu(0x1L);

            //TODO clean up this if chain
            final long v1 = spu124._000.get() & 0xf0L;
            if(v1 == 0xb0L) {
              //LAB_80045e60
              //switchD
              switch(spu124._002.get()) {
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
                  } else if(spu124._028.get() == 0) {
                    //LAB_80045f30
                    spu124._00c.add(0x6L);
                  } else {
                    _800c6680.deref(4).offset(0x4L).setu(voice00LeftPtr_800c4aa4.deref(2).offset(spu124._00c.get()).offset(0x2L));
                    spu124._00c.add(0x3L);
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
                  if(spu124._002.get() == 0x2fL) {
                    //LAB_80045e24
                    FUN_80048eb8(voiceIndex);
                    break;
                  }

                  if(spu124._002.get() == 0x51L) {
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
        }

        //LAB_8004602c
        if(_800c6634.get() != 0) {
          _800c666a.oru(spu124._0de.get());
          _800c666c.oru(spu124._0e0.get());
          _800c666e.oru(spu124._0e2.get());
          _800c6670.oru(spu124._0e4.get());

          spu124._0de.set(0);
          spu124._0e0.set(0);
          spu124._0e2.set(0);
          spu124._0e4.set(0);

          _800c6634.setu(0);
        }

        //LAB_800460a0
        if(spu124._108.get() != 0 || spu124._02a.get() == 0x1L) {
          //LAB_800460c0
          if(spu124._118.get() != 0) {
            spu124._118.decr();
          }
        }

        //LAB_800460d4
        if(spu124._037.get() != 0) {
          spu124._000.set(spu124._039);
          spu124._001.set(spu124._039);
          spu124._00c.set(spu124._02c);
          spu124._037.set(0);

          if(spu124._0e6.get() == 0) {
            //LAB_80046118
            spu124._028.set(1);
            spu124._118.set(0);
          } else {
            spu124._0e6.set(0);
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
  public static void FUN_80046224(final int voiceIndex) {
    long s1;
    long t0;
    long t1;
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;

    final SpuStruct124 s3 = _800c4ac8.get(voiceIndex);
    if(s3._003.get() == 0) {
      FUN_80048514(voiceIndex);
      return;
    }

    //TODO struct
    long s4 = _800c6630.getAddress();
    //LAB_8004629c
    long sp18 = s3._002.get() - _800c6674.deref(1).offset(0x6L).get();
    if((int)sp18 < 0) {
      return;
    }

    _800c6678.addu(sp18 * 0x10L);
    _800c6680.deref(1).offset(0xaL).setu(0x40L);
    final short s5 = (short)FUN_80048000(_800c6678.deref(1).offset(0x0L).get(), _800c6678.deref(1).offset(0x1L).get(), s3._020.get());
    if(s5 == -0x1L) {
      s3._00c.add(0x4L);
      MEMORY.ref(1, s4).offset(0x4L).setu(0);
      return;
    }

    //LAB_8004632c
    final SpuStruct66 struct66 = _800c3a40.get(s5);
    if(_800c6678.deref(1).offset(0xfL).get(0x1L) != 0) {
      struct66._08.set(0);
      struct66._0c.set(1);
    } else {
      //LAB_80046374
      struct66._08.set(1);
      struct66._0c.set(0);
    }

    //LAB_8004639c
    struct66._00.set(1);
    a1 = _800c6678.get();
    struct66._02.set(s3._002.get());
    struct66._04.set(s3._000.get() & 0xf);
    struct66._06.set(voiceIndex);
    struct66._0a.set((int)MEMORY.ref(1, s4).offset(0x0L).get());
    struct66._0e.set((int)sp18);
    struct66._12.set(0);
    struct66._18.set(0);
    struct66._1a.set(1);
    struct66._1c.set(4);
    struct66._1e.set((int)MEMORY.ref(1, a1).offset(0x1L).get());
    struct66._20.set((int)MEMORY.ref(1, a1).offset(0x0L).get());
    struct66._22.set(s3._020.get());
    struct66._24.set(s3._024.get());
    struct66._26.set(s3._022.get());
    struct66._28.set((int)_800c6680.deref(1).offset(0xeL).get());
    struct66._2a.set((int)_800c6674.deref(1).offset(0x1L).get());
    v0 = _800c4ab0.get() + s3._003.get();
    struct66._2c.set((int)MEMORY.ref(1, v0).offset(0x2L).get());
    struct66._2e.set((int)MEMORY.ref(1, a1).offset(0xbL).get());
    v0 = _80059f3c.offset((FUN_80048b90(0x4L, 0) / 2) & 0x7ffeL).getAddress();
    struct66._30.set((int)MEMORY.ref(1, v0).get());
    v0 = _80059f3c.offset((FUN_80048b90(0x4L, 0) / 2) & 0x7ffeL).getAddress();
    a1 = _800c6680.get();
    a0 = _800c6678.get();
    struct66._32.set((int)MEMORY.ref(1, v0).offset(0x1L).get());
    struct66._34.set((int)MEMORY.ref(1, a1).offset(0x3L).get());

    v1 = MEMORY.ref(1, a0).offset(0x3L).get();
    if((v1 & 0x80L) != 0) {
      v1 |= 0xff00L;
    }

    //LAB_80046500
    struct66._36.set((int)v1);
    struct66._38.set((int)MEMORY.ref(1, a1).offset(0xaL).get());
    struct66._3a.set((int)MEMORY.ref(1, a0).offset(0xdL).get());
    struct66._3c.set((int)MEMORY.ref(1, a1).offset(0xcL).get());
    struct66._3e.set(s3._005.get());
    struct66._40.set((int)MEMORY.ref(1, a0).offset(0x2L).get());
    struct66._44.set(0);
    struct66._4a.set((int)MEMORY.ref(1, a0).offset(0xaL).get());
    struct66._4c.set((int)MEMORY.ref(1, a0).offset(0xcL).get());
    struct66._4e.set(120);
    struct66._62.set(0);

    if(MEMORY.ref(1, s4).get() < 0x18L) {
      MEMORY.ref(1, s4).addu(0x1L);
    }

    v1 = _800c6678.deref(1).offset(0xfL).get();
    if((v1 & 0x20L) != 0) {
      if((v1 & 0x40L) != 0) {
        struct66._10.set((int)_800c6674.deref(1).offset(0x5L).get());
      } else {
        //LAB_800465a4
        struct66._10.set((int)_800c6678.deref(1).offset(0xeL).get());
      }

      //LAB_800465b0
      struct66._14.set(1);
      struct66._16.set(127);
    } else {
      //LAB_800465ec
      struct66._14.set(0);
    }

    //LAB_800465f0
    s1 = FUN_80048ab8(voiceIndex, FUN_80048b90(0x4L, 0) & 0xffffL, 0);
    final long s2 = FUN_80048ab8(voiceIndex, FUN_80048b90(0x4L, 0) & 0xffffL, 0x1L);

    if(_800c6678.deref(1).offset(0xfL).get(0x10L) != 0) {
      t0 = _800c6674.deref(1).offset(0x4L).get();
    } else {
      //LAB_80046668
      t0 = _800c6678.deref(1).offset(0xdL).get();
    }

    //LAB_8004666c
    if(s3._0e9.get() != 0) {
      a2 = _800c6678.deref(1).offset(0x3L).get();
      if((a2 & 0x80L) != 0) {
        a2 |= 0xffff_ff00L;
      }

      //LAB_8004669c
      v0 = FUN_80048998(_800c6678.deref(1).offset(0x2L).get(), s3._002.get(), a2, _800c6680.deref(1).offset(0xaL).get(), t0) & 0xffffL;
      t1 = v0 * s3._0ec.get();
      v0 = _800c4ac4.get() + s5 * 0x10L;
      MEMORY.ref(2, v0).offset(0x4L).setu((int)t1 >> 12);
      s1 = FUN_8004b644(s1 & 0xffffL, s3._0ee.get());
      a1 = FUN_8004b644(s2 & 0xffffL, s3._0f0.get());
      struct66._42.set(1);
    } else {
      //LAB_80046730
      a2 = _800c6678.deref(1).offset(0x3L).get();
      if((a2 & 0x80L) != 0) {
        a2 = 0xffff_ff00L | a2;
      }

      //LAB_80046750
      v0 = FUN_80048998(_800c6678.deref(1).offset(0x2L).get(), s3._002.get(), a2, _800c6680.deref(1).offset(0xaL).get(), t0);
      v1 = _800c4ac4.get() + s5 * 0x10L;
      MEMORY.ref(2, v1).offset(0x4L).setu(v0);
      s1 = FUN_8004b644(s1 & 0xffffL, 0x1000L);
      a1 = FUN_8004b644(s2 & 0xffffL, 0x1000L);
      struct66._42.set(0);
    }

    //LAB_800467c8
    if(MEMORY.ref(2, s4).offset(0x36L).get() != 0) {
      a1 = maxShort((short)s1, (short)a1);
      s1 = a1;
    }

    //LAB_800467f0
    a2 = _800c4ac4.get() + s5 * 0x10L;
    MEMORY.ref(2, a2).setu(s1);
    MEMORY.ref(2, a2).offset(0x2L).setu(a1);
    a1 = _800c6678.get();
    MEMORY.ref(2, a2).offset(0x6L).setu(MEMORY.ref(2, a1).offset(0x4L).get() + _800c43d0.get(s3._020.get()).soundBufferPtr_08.get());
    MEMORY.ref(2, a2).offset(0x8L).setu(MEMORY.ref(2, a1).offset(0x6L));
    MEMORY.ref(2, a2).offset(0xaL).setu(MEMORY.ref(2, a1).offset(0x8L));
    FUN_80048828(voiceIndex, s5 & 0xffffL);

    if(s3._0ea.get() != 0) {
      if(s5 < 0x10L) {
        //LAB_800468a8
        MEMORY.ref(2, s4).offset(0x12L).oru(0x1L << s5);
      } else {
        //LAB_800468c0
        MEMORY.ref(2, s4).offset(0x14L).oru(0x1L << s5 - 16);
      }
    } else {
      //LAB_80046884
      if(_800c6678.deref(1).offset(0xfL).get(0x80L) == 0) {
        //LAB_800468d8
        if(s5 < 0x10L) {
          MEMORY.ref(2, s4).offset(0x12L).and(~(0x1L << s5));
        } else {
          //LAB_800468f8
          MEMORY.ref(2, s4).offset(0x14L).setu(~(0x1L << s5 - 16));
        }
      } else {
        if(s5 < 0x10L) {
          //LAB_800468a8
          MEMORY.ref(2, s4).offset(0x12L).oru(0x1L << s5);
        } else {
          //LAB_800468bc
          //LAB_800468c0
          MEMORY.ref(2, s4).offset(0x14L).oru(0x1L << s5 - 16);
        }
      }
    }

    //LAB_80046914
    if(_800c6678.deref(1).offset(0xfL).get(0x2L) != 0) {
      FUN_8004888c(voiceIndex & 0xffffL, s5 & 0xffffL);
      _800c4ac4.deref(2).offset(0x1aaL).and(0xc0ffL).oru(_800c6678.deref(1).offset(0x2L).get() << 8);
    } else {
      //LAB_80046964
      if(s5 < 0x10L) {
        MEMORY.ref(2, s4).offset(0x16L).and(~(0x1L << s5));
      } else {
        //LAB_80046990
        MEMORY.ref(2, s4).offset(0x18L).and(~(0x1L << s5 - 16));
      }
    }

    //LAB_800469ac
    _800c6678.subu((short)sp18 << 4);
    s3._00c.sub(0x4L);

    //LAB_800469d4
  }

  @Method(0x80046a04L)
  public static void FUN_80046a04(final int voiceIndex) {
    long s0;
    long v0;
    long v1;
    long a2;
    long t0;

    final SpuStruct124 s2 = _800c4ac8.get(voiceIndex);
    if(s2._003.get() == 0) {
      FUN_800486d4(voiceIndex);
      return;
    }

    //LAB_80046a7c
    if(_800c6680.deref(1).offset(0x3L).get() != 0) {
      v1 = _800c6674.deref(1).get();
      if(v1 == 0xffL) {
        v0 = s2._002.get() - _800c6674.deref(1).offset(0x6L).get();
        s2._026.set((int)v0);
        s2._01e.set((int)v0);
        //LAB_80046acc
      } else if((v1 & 0x80L) != 0) {
        s2._026.set((int)(v1 + 0x80L));
        s2._01e.set(0);
        _800c6630.offset(0xcL).setu(0);
      } else {
        //LAB_80046ae8
        s2._026.set((int)v1);
        s2._01e.set(0);
        _800c6630.offset(0xcL).setu(0x1L);
      }

      //LAB_80046af8
      //LAB_80046b24
      for(int s7 = s2._01e.get(); s7 < s2._026.get() + 0x1L; s7++) {
        if(FUN_80048938(_800c6674.deref(1).get(), s7, s2._002.get())) {
          final short s5 = (short)FUN_80047e1c();
          if(s5 == -0x1L) {
            break;
          }

          final SpuStruct66 s1 = _800c3a40.get(s5);

          _800c6678.addu(s7 * 0x10L);
          if((_800c6678.deref(1).offset(0xfL).get() & 0x1L) != 0) {
            s1._08.set(0);
            s1._0c.set(1);
          } else {
            //LAB_80046bb4
            s1._08.set(1);
            s1._0c.set(0);
          }

          //LAB_80046bdc
          s1._00.set(1);
          s1._02.set(s2._002.get());
          s1._06.set(voiceIndex);
          s1._04.set(s2._000.get() & 0xf);
          s1._0e.set(s7);
          s1._12.set(0);
          s1._1a.set(0);
          s1._1c.set(0);
          s1._1e.set(0);
          s1._20.set(0);
          s1._0a.set((int)_800c6630.get());
          s1._22.set(s2._020.get());
          s1._28.set((int)_800c6680.deref(1).offset(0xeL).get());
          s1._2a.set((int)_800c6674.deref(1).offset(0x1L).get());
          _800c6678.deref(2).offset(0x2cL).setu(_800c4ab0.deref(1).offset(s2._003.get()).offset(0x2L));
          s1._2e.set((int)_800c6678.deref(1).offset(0xbL).get());
          s1._30.set((int)_80059f3c.offset((FUN_80048b90(0, 0) / 0x2L) & 0x7ffeL).get());
          s1._32.set((int)_80059f3c.offset((FUN_80048b90(0, 0) / 0x2L) & 0x7ffeL).offset(0x1L).get());
          s1._34.set((int)_800c6680.deref(1).offset(0x3L).get());
          v1 = _800c6678.deref(1).offset(0x3L).get();
          if((v1 & 0x80L) != 0) {
            v1 |= 0xff00L;
          }

          //LAB_80046d08
          s1._36.set((int)v1);
          s1._38.set((int)_800c6680.deref(1).offset(0xaL).get());
          s1._3a.set((int)_800c6678.deref(1).offset(0xdL).get());
          s1._3c.set((int)_800c6680.deref(1).offset(0xcL).get());
          s1._3e.set(s2._005.get());
          s1._40.set((int)_800c6678.deref(1).offset(0x2L).get());
          s1._42.set(0);
          s1._44.set(0);
          s1._4a.set((int)_800c6678.deref(1).offset(0xaL).get());
          s1._4c.set((int)_800c6680.deref(1).offset(0x4L).get());
          s1._4e.set(120);

          if(_800c6680.deref(1).offset(0xbL).get() == 0x7fL) {
            s1._18.set(1);
          }

          //LAB_80046d80
          if((_800c6678.deref(1).offset(0xfL).get() & 0x20L) == 0 || _800c6680.deref(1).offset(0x9L).get() == 0) {
            //LAB_80046e1c
            //LAB_80046e20
            s1._14.set(0);
            s1._16.set(0);
          } else {
            if((_800c6678.deref(1).offset(0xfL).get() & 0x40L) != 0) {
              s1._10.set((int)_800c6674.deref(1).offset(0x5L).get());
            } else {
              //LAB_80046dd0
              s1._10.set((int)_800c6678.deref(1).offset(0xeL).get());
            }

            //LAB_80046ddc
            s1._14.set(1);
            s1._16.set((int)_800c6680.deref(1).offset(0x9L).get());
          }

          //LAB_80046e4c
          if((_800c6678.deref(1).offset(0xfL).get() & 0x10L) != 0) {
            t0 = _800c6674.deref(1).offset(0x4L).get();
          } else {
            //LAB_80046e7c
            t0 = _800c6678.deref(1).offset(0xdL).get();
          }

          //LAB_80046e80
          a2 = _800c6678.deref(1).offset(0x3L).get();
          if((a2 & 0x80L) != 0) {
            a2 |= 0xffff_ff00L;
          }

          //LAB_80046ea0
          v0 = FUN_80048998(_800c6678.deref(1).offset(0x2L).get(), s2._002.get(), a2, _800c6680.deref(1).offset(0xaL).get(), t0);
          _800c4ac4.deref(2).offset(s5 * 0x10L).offset(0x4L).setu(v0);
          s0 = FUN_80048ab8(voiceIndex, FUN_80048b90(0, 0), 0);
          v0 = FUN_80048ab8(voiceIndex, FUN_80048b90(0, 0), 0x1L);
          if(_800c6630.offset(2, 0x36L).get() != 0) {
            v0 = maxShort(s0, v0);
            s0 = v0;
          }

          //LAB_80046f30
          a2 = _800c4ac4.get() + s5 * 0x10L;
          MEMORY.ref(2, a2).setu(s0);
          MEMORY.ref(2, a2).offset(0x2L).setu(v0);
          MEMORY.ref(2, a2).offset(0x6L).setu(_800c6678.deref(2).offset(0x4L).get() + _800c43d0.get(s2._020.get()).soundBufferPtr_08.get());
          MEMORY.ref(2, a2).offset(0x8L).setu(_800c6678.deref(2).offset(0x6L));
          MEMORY.ref(2, a2).offset(0xaL).setu(_800c6678.deref(2).offset(0x8L));
          FUN_80048828(voiceIndex, s5);

          if((_800c6678.deref(1).offset(0xfL).get() & 0x80L) != 0) {
            if(s5 < 0x10L) {
              _800c6630.offset(2, 0x12L).oru(0x1L << s5);
            } else {
              //LAB_80046fcc
              _800c6630.offset(2, 0x14L).oru(0x1L << (s5 - 0x10L));
            }
          } else {
            //LAB_80046fe8
            if(s5 < 0x10L) {
              _800c6630.offset(2, 0x12L).and(~(0x1L << s5));
            } else {
              //LAB_80047008
              _800c6630.offset(2, 0x14L).and(~(0x1L << (s5 - 0x10L)));
            }
          }

          //LAB_80047024
          if(s5 < 0x10L) {
            _800c6630.offset(2, 0x16L).and(~(0x1L << s5));
          } else {
            //LAB_80047050
            _800c6630.offset(2, 0x18L).and(~(0x1L << (s5 - 0x10L)));
          }

          //LAB_8004706c
          if(_800c6630.get() < 24) {
            _800c6630.addu(0x1L);
          }

          _800c6678.subu(s7 * 0x10L);

          if(_800c6630.offset(0xcL).get() != 0) {
            //LAB_80046ae0
            _800c6630.offset(0xcL).setu(0);
            break;
          }
        }

        //LAB_8004709c
      }
    }

    //LAB_800470bc
    s2._00c.add(0x3L);

    //LAB_800470cc
  }

  @Method(0x800470fcL)
  public static void FUN_800470fc() {
    long v0;
    long v1;

    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long t1;
    long t2;
    long t3;
    long t4;

    long s6 = _800c6630.getAddress();

    //LAB_80047144
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final SpuStruct66 spu66 = _800c3a40.get(voiceIndex);

      if(spu66._00.get() == 0x1L) {
        final SpuStruct124 s3 = _800c4ac8.get(spu66._06.get());
        v0 = spu66._14.get();
        if(v0 == 0x1L && spu66._44.get() == 0x1L && s3._104.get() == 0x1L) {
          //LAB_800471d0
          //LAB_800471d4
          a2 = spu66._36.get();
          a1 = spu66._02.get();
          t2 = spu66._40.get();
          a3 = spu66._38.get();
          v0 = spu66._14.get();
          t3 = spu66._3a.get();

          if(v0 == 0x1L || spu66._44.get() == 0x1L) {
            //LAB_80047220
            if(a1 < t2) {
              //LAB_80047244
              t2 = 0x78L + (t2 - a1);
            } else {
              t2 = 0x78L - (a1 - t2);
            }

            //LAB_80047248
            v0 = spu66._14.get();

            if(v0 != 0) {
              if(MEMORY.ref(2, s6).offset(0x42L).get() != 0x3cL) {
                //LAB_800472cc
                //LAB_800472d0
                spu66._12.add(spu66._3c.get() & 0xfff);
              } else {
                v1 = spu66._3c.get();
                a1 = v1 & 0xfffL;
                if(a1 == 0x78L) {
                  v0 = v1 & 0xf000L;
                  if(v0 != 0) {
                    v0 -= 0x1000L;
                    v1 = spu66._12.get();
                    v0 |= a1;
                    spu66._3c.set((int)v0);

                    v0 &= 0xfffL;
                    v1 += v0;
                    spu66._12.set((int)v1);
                  } else {
                    //LAB_800472c0
                    spu66._3c.or(0x6000);
                  }
                } else {
                  //LAB_800472cc
                  //LAB_800472d0
                  spu66._12.add(spu66._3c.get() & 0xfff);
                }
              }

              //LAB_80047300
              long a0 = _800c43d0.get(s3._020.get()).sshdPtr_04.get();
              a3 = 0x80L;
              if((a0 & 0x3L) == 0) {
                v0 = MEMORY.ref(4, a0).offset(0x18L).get();
                a1 = a0 + v0;
                _800c4ac0.setu(a0);
                if(a1 != v0 && (a1 & 0x1L) == 0) {
                  _800c4ab4.setu(a1);
                  _800c4ab8.setu(a1);
                  if(spu66._12.get() >= 0xf0L) {
                    spu66._12.set((spu66._3c.get() & 0xfff) / 2);
                  }

                  //LAB_800473a0
                  v0 = _800c4ab4.get() + spu66._10.get() * 0x2L;
                  v0 = MEMORY.ref(2, v0).offset(0x2L).get() + spu66._12.get() / 0x4L;
                  a3 = _800c4ab8.deref(1).offset(v0).get();
                }
              }

              //LAB_800473d4
              //LAB_800473d8
              a1 = spu66._4e.get();
              if(spu66._1c.get() == 0) {
                if(spu66._38.get() >= 0x40L) {
                  v0 = spu66._38.get() - 0x40L;
                  v0 = v0 * spu66._3a.get();
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
                  v0 = 0x40L - spu66._38.get();
                  v0 = v0 * spu66._3a.get();
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
              v0 = spu66._16.get();
              v1 = a3 * v0 / 255;

              v0++;
              v0 >>= 0x1L;
              v0 -= 0x40L;
              a3 = v1 - v0;
            }

            //LAB_800474f0
            if(spu66._44.get() != 0 && spu66._62.get() != 0) {
              spu66._62.decr();

              a1 = spu66._64.get();
              if((spu66._60.get() & 0x80L) != 0) {
                long a0 = a1 - spu66._62.get();
                v0 = -spu66._60.get() & 0xffL;

                //LAB_800475a4
                t1 = a0 * v0 * 192 / (a1 * 120);
                v0 = 0x100L - spu66._60.get();

                //LAB_80047600
                v0 = a0 * v0 / 10 / a1;
                a1 = spu66._4e.get() - v0;
                if(t1 < 0) {
                  a0 = t1 + 0xfL;
                } else {
                  a0 = t1;
                }

                //LAB_80047618
                a2 -= t1 - (a0 & ~0xfL);
              } else {
                //LAB_8004762c
                long a0 = (a1 - spu66._62.get()) * spu66._60.get();

                //LAB_80047684
                t1 = a0 * 192 / (a1 * 120);

                //LAB_800476cc
                v0 = a0 / 10 / a1;
                a1 = spu66._4e.get() + v0;
                if((int)t1 < 0) {
                  a0 = t1 + 0xfL;
                } else {
                  a0 = t1;
                }

                //LAB_800476e4
                a2 += t1 - (a0 & ~0xfL);
              }

              //LAB_800476f4
              v0 = a1 & 0xffffL;
              if(v0 < 0xdL) {
                a1 = 0xcL;
                v0 = 0xcL;
              }

              //LAB_8004770c
              if(v0 >= 0xf3L) {
                a1 = 0xf3L;
              }

              //LAB_8004771c
              s3._11c.set((int)a1);

              if(spu66._62.get() == 0) {
                spu66._4e.set((int)a1);
                spu66._44.set(0);
              }

              //LAB_80047754
            }
          }

          //LAB_80047758
          if(spu66._42.get() == 0x1L || s3._104.get() == 0x1L) {
            //LAB_80047794
            s0 = s3._0ec.get();
          } else {
            s0 = 0x1000L;
            //LAB_800477a0
          }

          //LAB_800477a4
          t4 = s0 * FUN_80048998(t2 & 0xffffL, a1 & 0xffffL, (short)a2, a3 & 0xffffL, t3);
          SPU.voices[(short)voiceIndex / 0x1000].pitch = (short)(t4 / 0x1000);
        }

        //LAB_800477ec
        if(spu66._1a.get() == 0x1L) {
          if(spu66._46.get() == 0x1L || spu66._48.get() == 0x1L || s3._105.get() == 0x1L) {
            //LAB_80047844
            //LAB_80047848
            jmp_LAB_800478d0:
            if(spu66._46.get() != 0) {
              if(spu66._50.get() != spu66._52.get()) {
                if(spu66._54.get() != 0) {
                  spu66._2c.set((int)FUN_8004af3c(spu66._50.get(), spu66._52.get(), spu66._56.get(), spu66._54.get()));
                  spu66._54.decr();
                  break jmp_LAB_800478d0;
                }

                //LAB_800478c8
                spu66._2c.set(spu66._50);
              }

              //LAB_800478cc
              spu66._46.set(0);
            }

            //LAB_800478d0
            //LAB_800478d4
            if(spu66._48.get() != 0) {
              if(spu66._58.get() == spu66._5a.get()) {
                spu66._48.set(0);
              } else {
                //LAB_8004791c
                if(spu66._5c.get() != 0) {
                  spu66._4c.set((int)FUN_8004af3c(spu66._58.get(), spu66._5a.get(), spu66._5e.get(), spu66._5c.get()));
                  spu66._5c.decr();
                } else {
                  //LAB_8004795c
                  spu66._4c.set(spu66._58);
                  spu66._48.set(0);
                }

                //LAB_80047964
                spu66._30.set((int)_80059f3c.offset(spu66._4c.get() / 0x4L * 0x2L).offset(0x0L).get());
                spu66._32.set((int)_80059f3c.offset(spu66._4c.get() / 0x4L * 0x2L).offset(0x1L).get());
              }
            }

            //LAB_800479c4
            s1 = FUN_8004ae94(voiceIndex, 0);
            s0 = FUN_8004ae94(voiceIndex, 0x1L);
            if(spu66._42.get() == 0x1L || s3._105.get() == 0x1L) {
              //LAB_80047a24
              s1 = FUN_8004b644(s1 & 0xffffL, s3._0ee.get());
              s0 = FUN_8004b644(s0 & 0xffffL, s3._0f0.get());
            }

            //LAB_80047a44
            if(MEMORY.ref(2, s6).offset(0x36L).get() != 0) {
              s0 = maxShort(s1, s0);
              s1 = s0;
            }

            //LAB_80047a6c
            SPU.voices[voiceIndex].volumeLeft.set(s1);
            SPU.voices[voiceIndex].volumeRight.set(s0);
          }
        }

        //LAB_80047a90
      }

      //LAB_80047acc
    }
  }

  @Method(0x80047b38L)
  public static void FUN_80047b38(final int voiceIndex) {
    final SpuStruct124 a1 = _800c4ac8.get(voiceIndex);
    final long sshdPtr = _800c43d0.get(a1._020.get()).sshdPtr_04.get();

    _800c6638.setu(sshdPtr);
    _800c4ac0.setu(sshdPtr);
    voice00LeftPtr_800c4aa4.setu(a1.voicePtr_010.get());
    _800c4ab0.setu(MEMORY.ref(4, sshdPtr).offset(0x14L).get() + sshdPtr);

    final long v1 = sshdPtr + MEMORY.ref(4, sshdPtr).offset(a1._02a.get() * 0x10L).offset(0x10L).get();

    _800c4aa8.setu(v1);
    _800c4aac.setu(v1);
  }

  @Method(0x80047bd0L)
  public static long FUN_80047bd0(final int voiceIndex) {
    final SpuStruct124 t2 = _800c4ac8.get(voiceIndex);

    if(MEMORY.ref(4, t2._028.getAddress()).get(0xff_00ffL) == 0x1_0000L) {
      if(_800c4ac0.deref(4).offset(0x20L).get() != -0x1L) {
        _800c6674.setu(_800c6630.offset(4, 0x8L).get() + _800c4ac0.deref(4).offset(0x20L).get() + _800c4aa8.deref(2).offset(voice00LeftPtr_800c4aa4.deref(1).offset(t2._00c.get()).offset(0x3L).get() * 0x2L).offset(0x192L).get() + 0x190L);
        _800c6678.setu(_800c6630.offset(4, 0x8L).get() + _800c4ac0.deref(4).offset(0x20L).get() + _800c4aa8.deref(2).offset(voice00LeftPtr_800c4aa4.deref(1).offset(t2._00c.get()).offset(0x3L).get() * 0x2L).offset(0x192L).get() + 0x198L);
        _800c667c.setu(_800c4ac0.deref(4).offset(0x20L).get() + _800c6630.offset(4, 0x8L).get());
        _800c6680.setu(_800c4ac0.deref(4).offset(0x20L).get() + _800c6630.offset(4, 0x8L).get() + (voiceIndex + 1) * 0x10L);
        return 0x1L;
      }
    }

    //LAB_80047cd0
    if(t2._028.get() == 0) {
      return 0;
    }

    if(t2._02a.get() == 0x1L) {
      //LAB_80047cf0
      return 0;
    }

    //LAB_80047cf8
    final long v1 = t2._000.get() & 0xfL;
    if(t2._000.get() < 0xa0L) {
      if(_800c4aa8.deref(2).offset(voice00LeftPtr_800c4aa4.deref(1).offset(v1 * 0x10L).offset(0x12L).get() * 0x2L).offset(0x2L).get() == 0xffffL) {
        return 0;
      }

      if(_800c4aa8.deref(2).get() < voice00LeftPtr_800c4aa4.deref(1).offset(v1 * 0x10L).offset(0x12L).get()) {
        return 0;
      }

      if(_800c4ac0.deref(4).offset(0x10L).getSigned() == -0x1L) {
        return 0;
      }

      return 0x1L;
    }

    //LAB_80047d7c
    //LAB_80047d80
    _800c6674.setu(_800c4ac0.deref(4).offset(0x10L).get() + _800c6630.offset(4, 0x8L).get() + _800c4aa8.deref(2).offset(voice00LeftPtr_800c4aa4.deref(1).offset(v1 * 0x10L).offset(0x12L).get() * 0x2L).offset(0x2L).get());
    _800c6678.setu(_800c4ac0.deref(4).offset(0x10L).get() + _800c6630.offset(4, 0x8L).get() + _800c4aa8.deref(2).offset(voice00LeftPtr_800c4aa4.deref(1).offset(v1 * 0x10L).offset(0x12L).get() * 0x2L).offset(0x2L).get() + 0x8L);
    _800c667c.setu(t2.voicePtr_010.get());
    _800c6680.setu(t2.voicePtr_010.get() + (v1 + 1) * 0x10L);

    //LAB_80047e14
    return 0x1L;
  }

  @Method(0x80047e1cL)
  public static long FUN_80047e1c() {
    //LAB_80047e34
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      short v1 = (short)(_800c6630.offset(2, 0x10L).get() + 0x1L);
      if(v1 >= 0x18L) {
        v1 = 0;
      }

      //LAB_80047e4c
      _800c6630.offset(2, 0x10L).setu(v1);
      if(_800c3a40.get(v1)._00.get() == 0) {
        return _800c6630.offset(2, 0x10L).getSigned();
      }
    }

    long a1 = 0x18L;

    //LAB_80047ea0
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if(_800c3a40.get(voiceIndex)._1a.get() == 0) {
        if(_800c3a40.get(voiceIndex)._08.get() == 0x1L) {
          final long v1 = _800c3a40.get(voiceIndex)._0a.get();
          if(a1 > v1) {
            a1 = v1;
            _800c6630.offset(2, 0x10L).setu(voiceIndex);
          }
        }
      }

      //LAB_80047ef4
    }

    outer:
    if(a1 == 0x18L) {
      //LAB_80047f28
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        if(_800c3a40.get(voiceIndex)._1a.get() == 0) {
          if(_800c3a40.get(voiceIndex)._0a.get() < 0x18L) {
            //LAB_80047f84
            a1 = _800c3a40.get(voiceIndex)._0a.get();
            _800c6630.offset(2, 0x10L).setu(voiceIndex);
            break outer;
          }
        }

        //LAB_80047f64
      }

      _800c6630.offset(2, 0x10L).setu(-0x1L);
      return _800c6630.offset(2, 0x10L).getSigned();
    }

    //LAB_80047f90
    //LAB_80047fa0
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if(a1 < _800c3a40.get(voiceIndex)._0a.get()) {
        _800c3a40.get(voiceIndex)._0a.decr();
      }

      //LAB_80047fd0
    }

    _800c6630.subu(0x1L);

    //LAB_80047ff4
    return _800c6630.offset(2, 0x10L).getSigned();
  }

  @Method(0x80048000L)
  public static long FUN_80048000(final long a0, final long a1, final long a2) {
    long v1;

    int t2 = 0;
    long t1 = 0x18L;
    long t3 = -0x1L;

    if((a0 & 0xffffL) != 0) {
      //LAB_8004802c
      for(int n = 0; n < 24; n++) {
        final SpuStruct66 spu66 = _800c3a40.get(n);

        if(spu66._1a.get() == 0x1L) {
          if(spu66._20.get() == (a0 & 0xffffL)) {
            if(spu66._22.get() == (a2 & 0xffffL)) {
              //LAB_80048080
              for(int i = 0; i < 24; i++) {
                v1 = spu66._0a.get();
                if(v1 < _800c3a40.get(i)._0a.get() && v1 != 0x40L) {
                  _800c3a40.get(i)._0a.decr();
                }

                //LAB_800480cc
              }

              //LAB_80048260
              _800c6630.subu(0x1L);
              return (short)n;
            }
          }
        }

        //LAB_800480f0
      }
    }

    //LAB_80048108
    if(_800c6630.offset(0xdL).get() >= _800c6630.offset(0x3L).get()) {
      //LAB_80048134
      for(int n = 0; n < 24; n++) {
        //LAB_80048144
        for(int i = 0; i < 24; i++) {
          if(_800c3a40.get(i)._1a.get() == 0x1L) {
            v1 = _800c3a40.get(i)._0a.get();

            if(v1 >= (short)n && v1 < (short)t1) {
              t1 = v1;
              t2 = i;
            }
          }

          //LAB_800481a0
        }

        if(_800c3a40.get(t2)._1e.get() <= (a1 & 0xffffL)) {
          //LAB_800481fc
          for(int i = 0; i < 24; i++) {
            v1 = _800c3a40.get(i)._0a.get();

            if(t1 < v1 && v1 != 0xffffL) {
              _800c3a40.get(i)._0a.decr();
            }

            //LAB_80048240
          }

          //LAB_80048260
          _800c6630.subu(0x1L);
          return (short)t2;
        }

        //LAB_8004826c
        t1 = 0x18L;
      }

      return -0x1L;
    }

    //LAB_800482a0
    //LAB_800482a8
    for(int i = 0; i < 24; i++) {
      v1 = _800c6630.offset(2, 0x10L).getSigned();
      v1++;
      if(v1 >= 0x18L) {
        v1 = 0;
      }

      //LAB_800482c0
      _800c6630.offset(2, 0x10L).setu(v1);

      if(_800c3a40.get((int)v1)._00.get() == 0) {
        //LAB_8004828c
        _800c6630.offset(0xdL).addu(0x1L);
        return _800c6630.offset(2, 0x10L).getSigned();
      }
    }

    //LAB_80048320
    jmp_80048478:
    {
      for(int n = 0; n < 24; n++) {
        if(_800c3a40.get(n)._08.get() == 0x1L && _800c3a40.get(n)._1a.get() != 0x1L) {
          //LAB_8004836c
          for(int i = n; i < 24; i++) {
            if(_800c3a40.get(i)._08.get() == 0x1L && _800c3a40.get(i)._1a.get() != 0x1L) {
              v1 = _800c3a40.get(i)._0a.get();

              if((short)v1 < (short)t1) {
                t1 = v1;
                t3 = i;
              }
            }

            //LAB_800483c8
          }

          break jmp_80048478;
        }

        //LAB_800483e8
      }

      //LAB_80048414
      for(int i = 0; i < 24; i++) {
        if(_800c3a40.get(i)._1a.get() != 0x1L) {
          v1 = _800c3a40.get(i)._0a.get();
          if((short)v1 < (short)t1) {
            t1 = v1;
            t3 = i;
          }
        }

        //LAB_80048460
      }
    }

    //LAB_80048478
    //LAB_8004847c
    //LAB_80048494
    for(int i = 0; i < 24; i++) {
      v1 = _800c3a40.get(i)._0a.get();
      if(v1 > t1 && v1 != 0xffffL) {
        _800c3a40.get(i)._0a.decr();
      }

      //LAB_800484d8
    }

    _800c6630.offset(0xdL).addu(0x1L);
    _800c6630.offset(0x0L).subu(0x1L);

    //LAB_80048508
    return (short)t3;
  }

  @Method(0x80048514L)
  public static void FUN_80048514(final int voiceIndex) {
    long s3 = 0;
    long a3 = 0;

    //LAB_8004857c
    for(int i = 0; i < 24; i++) {
      final SpuStruct66 spu66 = _800c3a40.get(i);
      if(spu66._00.get() == 0x1L) {
        if(spu66._1a.get() == 0x1L) {
          if(spu66._22.get() == _800c4ac8.get(voiceIndex)._020.get()) {
            if(spu66._3e.get() == _800c4ac8.get(voiceIndex)._005.get()) {
              if(spu66._02.get() == _800c4ac8.get(voiceIndex)._002.get()) {
                if(spu66._0c.get() == 0x1L) {
                  if(spu66._06.get() == voiceIndex) {
                    s3 |= 0x1L << i;
                  } else {
                    //LAB_8004861c
                    a3 |= 0x1L << i;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_80048620
    }

    if(s3 == 0) {
      s3 = a3;
    }

    //LAB_80048640
    //LAB_80048650
    for(int i = 0; i < 24; i++) {
      if((s3 & 0x1L << i) != 0) {
        FUN_800488d4(voiceIndex, i);
        _800c3a40.get(i)._02.decr();
      }

      //LAB_80048684
    }

    _800c4ac8.get(voiceIndex)._00c.add(0x4L);
  }

  @Method(0x800486d4L)
  public static long FUN_800486d4(final int voiceIndex) {
    final SpuStruct124 s1 = _800c4ac8.get(voiceIndex);

    //LAB_80048724
    for(int i = 0; i < 24; i++) {
      final SpuStruct66 spu66 = _800c3a40.get(i);
      if(spu66._00.get() == 0x1L) {
        if(spu66._1a.get() == 0) {
          if(spu66._06.get() == voiceIndex) {
            if(spu66._22.get() == s1._020.get()) {
              if(spu66._04.get() == (s1._000.get() & 0xfL)) {
                if(spu66._02.get() == s1._002.get()) {
                  if(spu66._0c.get() == 0) {
                    //LAB_800487d0
                    spu66._08.set(1);
                  } else if(spu66._18.get() == 0) {
                    spu66._08.set(1);
                  } else {
                    //LAB_800487d4
                    spu66._18.set(0);
                  }

                  //LAB_800487d8
                  FUN_800488d4(voiceIndex, i);
                }
              }
            }
          }
        }
      }

      //LAB_800487e4
    }

    s1._00c.add(0x3L);
    return voiceIndex;
  }

  @Method(0x80048828L)
  public static void FUN_80048828(final int voiceIndex, final long a1) {
    if(a1 < 0x10L) {
      _800c4ac8.get(voiceIndex)._0de.or(1 << a1);
    } else {
      //LAB_80048874
      _800c4ac8.get(voiceIndex)._0e0.or(1 << (a1 - 0x10L));
    }
  }

  @Method(0x8004888cL)
  public static void FUN_8004888c(final long a0, final long a1) {
    assert false;
  }

  @Method(0x800488d4L)
  public static void FUN_800488d4(final int voiceIndex, final long a1) {
    final SpuStruct124 a2 = _800c4ac8.get(voiceIndex);

    if(a1 < 0x10L) {
      a2._0e2.or(1 << a1);
    } else {
      //LAB_80048920
      a2._0e4.or(1 << (a1 - 16));
    }
  }

  @Method(0x80048938L)
  public static boolean FUN_80048938(final long a0, final long voiceIndex, final long a2) {
    if(a0 == 0xffL) {
      return true;
    }

    //LAB_80048950
    _800c6678.addu(voiceIndex * 0x10L);

    final boolean ret;
    if(a2 >= _800c6678.deref(1).get()) {
      ret = _800c6678.deref(1).offset(0x1L).get() >= a2;
    } else {
      ret = false;
    }

    //LAB_80048988
    _800c6678.subu(voiceIndex * 0x10L);

    //LAB_80048990
    return ret;
  }

  @Method(0x80048998L)
  public static long FUN_80048998(long a0, long a1, long a2, long a3, long a4) {
    long t0;
    long t1;
    long t3;
    long v0;
    long v1;

    t0 = a4;
    v1 = a1 & 0xffffL;
    a0 &= 0xffffL;
    t1 = a2;
    if(v1 < a0) {
      v0 = 0x2aaa_aaabL; //TODO
      a1 = a0 - v1;
      v1 = a1 * v0 >>> 32;
      v0 = a3 & 0xffffL;
      v0 -= 0x40L;
      t0 = t0 * v0;
      a2 = _8005967c.getAddress();
      a0 = v1 >> 1;
      v0 = a1 >> 31;
      a0 -= v0;
      v0 = a0 << 1;
      v0 += a0;
      v0 <<= 2;
      a1 -= v0;
      v1 = 0xcL;
      v1 -= a1;
      v1 <<= 4;
      v0 = t0 >> 2;
      v0 += 0xd0L;
      v1 += v0;
      v0 = (short)t1;
      v1 += v0;
      v1 <<= 1;
      v1 += a2;
      v0 = MEMORY.ref(2, v1).get();
      a0++;
      v0 = v0 >> a0;
      return v0 & 0xffffL;
    }

    //LAB_80048a38
    v0 = 0x2aaa_aaabL; //TODO
    v1 -= a0;
    t3 = v1 * v0 >>> 32;
    v0 = a3 & 0xffffL;
    v0 -= 0x40L;
    t0 = t0 * v0;
    a1 = _8005967c.getAddress();
    a0 = t3 >> 1;
    v0 = v1 >> 31;
    a0 -= v0;
    v0 = a0 << 1;
    v0 += a0;
    v0 <<= 2;
    v1 -= v0;
    v1 <<= 4;
    v0 = t0 >> 2;
    v0 += 0xd0L;
    v1 += v0;
    v1 += (short)t1;
    v1 <<= 1;
    v1 += a1;
    v0 = MEMORY.ref(2, v1).get() << a0;
    return v0 & 0xffffL;
  }

  @Method(0x80048ab8L)
  public static long FUN_80048ab8(final int voiceIndex, final long a1, final long a2) {
    final long t2 = _800c6680.deref(1).offset(0xeL).get() * _800c6674.deref(1).offset(0x1L).get() * _800c4ab0.deref(1).offset(_800c4ac8.get(voiceIndex)._003.get()).offset(0x2L).get() * _800c6678.deref(1).offset(0xbL).get();
    long v1 = _80059f3c.offset(a2).offset(a1 / 0x2L & 0xfffeL).get();
    long v0 = (int)t2 >> 14;
    final long a0 = (int)(v0 * v1) >> 7;
    if(_800c6678.deref(1).offset(0xaL).get() != 0) {
      v0 = _800c6678.deref(1).offset(0xaL).get();
      v1 = (short)a0 >> 7;
      v1 = v0 << 8 | v1;
    } else {
      v1 = a0;
    }

    //LAB_80048b88
    return v1 & 0xffffL;
  }

  @Method(0x80048b90L)
  public static long FUN_80048b90(final long a0, final long a1) {
    _800c6678.addu(a1 * 0x10L);

    final long a1_0;
    if(a0 == 0x4L) {
      a1_0 = _800c6678.deref(1).offset(0xcL).get();
    } else {
      //LAB_80048bc4
      final long v1 = _800c6674.deref(1).offset(0x2L).get() / 0x4L * 0x20L + _800c6678.deref(1).offset(0xcL).get() / 0x4L;
      final long v0 = _800c6680.deref(1).offset(0x4L).get() / 0x4L * 0x20L + _80059b3c.offset(1, v1).offset(0x0L).get() / 0x4L;
      a1_0 = _80059b3c.offset(1, v0).get();
    }

    //LAB_80048c1c
    _800c6678.subu(a1 * 0x10L);

    return a1_0 & 0xffffL;
  }

  @Method(0x80048c38L)
  public static long FUN_80048c38(int a0, long a1, long a2) {
    long t1;
    long t3;
    long v0;
    long v1;

    final SpuStruct0c spu0c = _800c43d0.get(a0);

    final long a3 = _800c6630.getAddress();
    final long sshd = spu0c.sshdPtr_04.get();
    MEMORY.ref(4, a3).offset(0x8L).setu(sshd);
    _800c4ac0.setu(sshd);
    v0 = MEMORY.ref(4, sshd).offset(0x1cL).get();
    t3 = MEMORY.ref(4, sshd).offset(0x20L).get();
    t1 = sshd + v0;
    _800c4abc.setu(t1);
    if(t3 == -0x1L) {
      return 0;
    }

    if((a0 & 0x80L) != 0) {
      return 0;
    }

    if(MEMORY.ref(1, a3).offset(0x3L).get() == 0) {
      return 0;
    }

    if(spu0c.used_00.get() != 0x1L) {
      return 0;
    }

    if(MEMORY.ref(2, t1).get() < a1) {
      return 0;
    }

    a1 = t1 + a1 * 0x2L;
    v1 = MEMORY.ref(2, a1).offset(0x2L).get();
    if(v1 == 0xffffL) {
      return 0;
    }

    v0 = t1 + (v1 & 0xfffeL);
    if(MEMORY.ref(2, v0).get() < a2) {
      return 0;
    }

    voice00LeftPtr_800c4aa4.setu(sshd + t3);
    v1 = MEMORY.ref(4, sshd).offset(0x14L).get();
    v0 = MEMORY.ref(2, a1).offset(0x2L).get();
    v1 += sshd;
    v0 >>>= 1;
    v0 += a2;
    v0 <<= 1;
    v0 += t1;
    _800c4ab0.setu(v1);
    v1 = MEMORY.ref(4, sshd).offset(0x1cL).get();
    v0 = MEMORY.ref(2, v0).offset(0x2L).get();
    v1 += sshd;
    v0 += v1;
    return v0;
  }

  @Method(0x80048d44L)
  public static long FUN_80048d44(final int a0, final long a1, final long a2) {
    final long ret = FUN_80048c38(a0, a1, a2);
    if(ret == 0) {
      return -0x1L;
    }

    final long s0 = _800c6630.getAddress();

    //LAB_80048dac
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      SpuStruct124 v1 = _800c4ac8.get(voiceIndex);

      if(v1._027.get() == 0) {
        if(v1._029.get() == 0) {
          v1._029.set(1);
          v1._02a.set(1);
          v1._028.set(0);
          v1._027.set(0);
          v1._118.set(0);
          v1._0e7.set(0);
          v1.voicePtr_010.set(ret);
          v1._00c.set(0);
          v1._020.set((int)a0);
          v1._024.set((int)a1);
          v1._022.set((int)a2);

          if(MEMORY.ref(1, s0).offset(0x23L).get() != 0) {
            v1._0ea.set(1);
            MEMORY.ref(1, s0).offset(0x23L).setu(0);
          }

          //LAB_80048e10
          v1._0e6.set(0);
          v1._035.set(0);
          v1._037.set(0);
          v1._0f0.set(0);
          v1._0ee.set(0);
          v1._0e9.set(0);

          if(MEMORY.ref(1, s0).offset(0x22L).get() != 0) {
            v1._0e9.set(1);
            v1._0ec.set((int)MEMORY.ref(2, s0).offset(0x24L).get());
            v1._0ee.set((int)MEMORY.ref(1, s0).offset(0x26L).get());
            v1._0f0.set((int)MEMORY.ref(2, s0).offset(0x28L).get());
            MEMORY.ref(2, s0).offset(0x22L).setu(0);
            MEMORY.ref(2, s0).offset(0x24L).setu(0);
            MEMORY.ref(2, s0).offset(0x28L).setu(0);
            MEMORY.ref(2, s0).offset(0x26L).setu(0);
          }

          return voiceIndex;
        }
      }

      //LAB_80048e74
    }

    //LAB_80048e8c
    //LAB_80048e90
    return -0x1L;
  }

  @Method(0x80048eb8L)
  public static void FUN_80048eb8(final int voiceIndex) {
    final SpuStruct124 a1 = _800c4ac8.get(voiceIndex);
    if(a1._02a.get() != 0) {
      a1._0e7.set(1);
      a1._118.set(0);
      a1._02a.set(0);
      a1._104.set(0);
      a1._105.set(0);
      a1._0e9.set(0);
      a1._0ea.set(0);
    } else {
      //LAB_80048f0c
      a1._00c.set(0x110L);
      a1._028.set(0);
    }

    //LAB_80048f18
    //LAB_80048f30
    for(int i = 0; i < 24; i++) {
      final SpuStruct66 spu66 = _800c3a40.get(i);
      if(spu66._06.get() == voiceIndex && spu66._1a.get() == 0) {
        spu66._14.set(0);
        spu66._38.set(64);
      }

      //LAB_80048f74
    }

    a1._001.set(a1._000);
  }

  @Method(0x80048f98L)
  public static void FUN_80048f98(final int voiceIndex) {
    final SpuStruct124 a1 = _800c4ac8.get(voiceIndex);
    long v1 = a1.voicePtr_010.get() + a1._00c.get();
    a1._00c.add(0x4L);
    v1 = MEMORY.ref(1, v1).offset(0x3L).get() << 8 | MEMORY.ref(1, v1).offset(0x2L).get();
    a1._108.set((int)v1);
  }

  @Method(0x80048fecL)
  public static void FUN_80048fec(final int voiceIndex) {
    final SpuStruct124 a1 = _800c4ac8.get(voiceIndex);
    if(a1._02a.get() == 0) {
      _800c6680.deref(1).offset(0x2L).setu(voice00LeftPtr_800c4aa4.deref(1).offset(a1._00c.get()).offset(0x1L));
      _800c6680.deref(1).offset(0xaL).setu(0x40L);
      _800c6680.deref(1).offset(0xbL).setu(0x40L);
    }

    //LAB_80049058
    a1._00c.add(0x2L);
  }

  @Method(0x8004906cL)
  public static void FUN_8004906c(final int voiceIndex) {
    assert false;
  }

  @Method(0x80049250L)
  public static void FUN_80049250(final int voiceIndex) {
    assert false;
  }

  @Method(0x80049480L)
  public static void FUN_80049480(final int voiceIndex) {
    assert false;
  }

  @Method(0x80049638L)
  public static void FUN_80049638(final int voiceIndex) {
    long v0;
    long v1;
    long a1;
    long a3;
    long t1;

    final SpuStruct124 s3 = _800c4ac8.get(voiceIndex);
    t1 = _800c6630.getAddress();
    if(s3._02a.get() != 0) {
      a3 = voice00LeftPtr_800c4aa4.get();

      //LAB_800496bc
      for(int s4 = 0; s4 < 24; s4++) {
        final SpuStruct66 spu66 = _800c3a40.get(s4);

        if(spu66._00.get() == 0x1L) {
          if(spu66._1a.get() == 0x1L) {
            if(spu66._22.get() == s3._020.get()) {
              a1 = a3 + s3._00c.get();
              if(spu66._3e.get() == MEMORY.ref(1, a1).offset(0x4L).get()) {
                if(spu66._02.get() == MEMORY.ref(1, a1).offset(0x5L).get()) {
                  if(spu66._06.get() == voiceIndex) {
                    spu66._46.set(1);
                    v0 = a3 + s3._00c.get();
                    v1 = spu66._2c.get();
                    v0 = MEMORY.ref(1, v0).offset(0x3L).get();
                    spu66._52.set((int)v1);
                    spu66._50.set((int)v0);
                    v0 = a3 + s3._00c.get();
                    v0 = MEMORY.ref(1, v0).offset(0x2L).get() / 0x4L;
                    v1 = MEMORY.ref(1, t1).offset(0x42L).get();
                    v0 = v0 * v1 / 60;
                    spu66._54.set((int)v0);
                    v0 = a3 + s3._00c.get();
                    v0 = MEMORY.ref(1, v0).offset(0x2L).get() / 0x4L;
                    v1 = MEMORY.ref(2, t1).offset(0x42L).get();
                    v0 = v0 * v1 / 60;
                    spu66._56.set((int)v0);
                  }
                }
              }
            }
          }
        }

        //LAB_800497dc
      }

      v0 = s3._00c.get() + 0x6L;
    } else {
      //LAB_800497fc
      v0 = voice00LeftPtr_800c4aa4.get() + s3._00c.get();
      _800c6680.deref(1).offset(0x3L).setu(MEMORY.ref(1, v0).offset(0x2L));
      _800c6680.deref(1).offset(0xeL).setu((_800c667c.deref(1).get() * _800c6680.deref(1).offset(0x3L).get()) >> 7);

      //LAB_8004985c
      for(int s4 = 0; s4 < 24; s4++) {
        final SpuStruct66 spu66 = _800c3a40.get(s4);
        if(spu66._00.get() == 0x1L) {
          if(spu66._04.get() == (s3._000.get() & 0xfL)) {
            if(s3._020.get() == spu66._22.get()) {
              if(spu66._08.get() != 0x1L) {
                if(spu66._06.get() == voiceIndex) {
                  s3._003.set(spu66._2c.get());
                  v0 = FUN_80048ab8(voiceIndex, FUN_80048b90(0, spu66._0e.get()), 0);
                  _800c4ac4.deref(2).offset(s4 * 0x10L).setu(v0);
                  v0 = FUN_80048ab8(voiceIndex, FUN_80048b90(0, spu66._0e.get()), 0x1L);
                  _800c4ac4.deref(2).offset(s4 * 0x10L).offset(0x2L).setu(v0);
                }
              }
            }
          }
        }

        //LAB_80049930
      }

      v0 = s3._00c.get() + 0x3L;
    }

    //LAB_80049950
    s3._00c.set(v0);
  }

  @Method(0x80049980L)
  public static void FUN_80049980(final int voiceIndex) {
    final SpuStruct124 s3 = _800c4ac8.get(voiceIndex);

    final long v0;
    if(s3._02a.get() != 0) {
      final long a3 = voice00LeftPtr_800c4aa4.get();

      //LAB_80049a08
      for(int i = 0; i < 24; i++) {
        final SpuStruct66 spu66 = _800c3a40.get(i);

        if(spu66._00.get() == 0x1L) {
          if(spu66._1a.get() == 0x1L) {
            if(spu66._22.get() == s3._020.get()) {
              long a1 = s3._00c.get() + a3;
              if(spu66._3e.get() == MEMORY.ref(1, a1).offset(0x4L).get()) {
                if(spu66._02.get() == MEMORY.ref(1, a1).offset(0x5L).get()) {
                  if(spu66._06.get() == voiceIndex) {
                    spu66._48.set(1);
                    spu66._5a.set(spu66._4c);
                    spu66._58.set((int)MEMORY.ref(1, a3).offset(s3._00c.get()).offset(0x3L).get());
                    spu66._5c.set((int)(MEMORY.ref(1, a3).offset(s3._00c.get()).offset(0x2L).get() * 0x4L * _800c6630.offset(2, 0x42L).get() / 60));
                    spu66._5e.set((int)(MEMORY.ref(1, a3).offset(s3._00c.get()).offset(0x2L).get() * 0x4L * _800c6630.offset(2, 0x42L).get() / 60));
                  }
                }
              }
            }
          }
        }

        //LAB_80049b28
      }

      v0 = s3._00c.get() + 0x6L;
    } else {
      //LAB_80049b48
      _800c6680.deref(1).offset(0x4L).setu(voice00LeftPtr_800c4aa4.deref(1).offset(s3._00c.get()).offset(0x2L));

      //LAB_80049b80
      for(int i = 0; i < 24; i++) {
        final SpuStruct66 spu66 = _800c3a40.get(i);

        if(spu66._04.get() == (s3._000.get() & 0xfL)) {
          if(spu66._22.get() == s3._020.get()) {
            if(spu66._06.get() == voiceIndex) {
              if(spu66._08.get() != 0x1L) {
                if(spu66._00.get() == 0x1L) {
                  spu66._4c.set((int)voice00LeftPtr_800c4aa4.deref(1).offset(s3._00c.get()).offset(0x2L).get());
                  s3._003.set(spu66._2c.get());
                  _800c4ac4.deref(2).offset(i * 0x10L).offset(0x0L).setu(FUN_80048ab8(voiceIndex, FUN_80048b90(0, spu66._0e.get()), 0));
                  _800c4ac4.deref(2).offset(i * 0x10L).offset(0x2L).setu(FUN_80048ab8(voiceIndex, FUN_80048b90(0, spu66._0e.get()), 0x1L));
                }
              }
            }
          }
        }

        //LAB_80049c68
      }

      v0 = s3._00c.get() + 0x3L;
    }

    //LAB_80049c88
    s3._00c.set(v0);
  }

  @Method(0x80049cbcL)
  public static void FUN_80049cbc(final int voiceIndex) {
    assert false;
  }

  @Method(0x80049e2cL)
  public static void FUN_80049e2c(final int voiceIndex) {
    assert false;
  }

  @Method(0x80049f14L)
  public static void FUN_80049f14(final int voiceIndex) {
    assert false;
  }

  @Method(0x8004a2c0L)
  public static void FUN_8004a2c0(final int voiceIndex) {
    assert false;
  }

  @Method(0x8004a34cL)
  public static void FUN_8004a34c(final int voiceIndex) {
    assert false;
  }

  @Method(0x8004a5e0L)
  public static void FUN_8004a5e0(final int voiceIndex) {
    final SpuStruct124 a1 = _800c4ac8.get(voiceIndex);

    //LAB_8004a618
    for(int i = 0; i < 4; i++) {
      final long a0 = voice00LeftPtr_800c4aa4.deref(1).offset(a1._00c.get()).get();
      a1._00c.incr();
      a1._118.shl(7);

      if((a0 & 0x80L) == 0) {
        //LAB_8004a720
        a1._118.or(a0);
        break;
      }

      a1._118.or(a0 & 0x7fL);
    }

    //LAB_8004a664
    if(a1._02a.get() != 0) {
      a1._108.set(60);
      a1._10a.set(480);
    }

    //LAB_8004a680
    if(a1._108.get() == 0) {
      return;
    }

    a1._114.set(a1._118.get() * 0xaL);

    //LAB_8004a700
    final long a2 = a1._108.get() * a1._10a.get() * 0xaL / (_800c6630.offset(2, 0x42L).get() * 0x3cL);

    final long v1;
    if(a1._10c.get() == 0) {
      //LAB_8004a72c
      v1 = a1._114.get() + a1._110.get();
    } else {
      a1._10c.set(0);
      v1 = a1._114.get() - (a2 - a1._110.get());
    }

    //LAB_8004a738
    //LAB_8004a760
    final long a0 = v1 % a2;
    if(a2 < a0 * 0x2L) {
      a1._114.set(a2 - a0 + v1);
      a1._10c.set(1);
    } else {
      //LAB_8004a78c
      a1._114.set(v1 - a0);
    }

    //LAB_8004a794
    //LAB_8004a7bc
    //LAB_8004a7d8
    a1._110.set((int)(v1 % a2));
    a1._118.set(a1._114.get() / a2);

    //LAB_8004a7e4
  }

  @Method(0x8004a7ecL)
  public static void FUN_8004a7ec(final int voiceIndex) {
    final SpuStruct124 a1 = _800c4ac8.get(voiceIndex);

    if((voice00LeftPtr_800c4aa4.deref(1).offset(a1._00c.get()).get() & 0x80L) == 0) {
      //LAB_8004a854
      a1._00c.decr();
      a1._000.set(a1._001);
    } else {
      a1._001.set((int)voice00LeftPtr_800c4aa4.deref(1).offset(a1._00c.get()).get());
      a1._000.set((int)voice00LeftPtr_800c4aa4.deref(1).offset(a1._00c.get()).get());
    }

    //LAB_8004a860
    a1._002.set((int)voice00LeftPtr_800c4aa4.deref(1).offset(a1._00c.get()).offset(0x1L).get());
    a1._003.set((int)voice00LeftPtr_800c4aa4.deref(1).offset(a1._00c.get()).offset(0x2L).get());
    a1._005.set((int)voice00LeftPtr_800c4aa4.deref(1).offset(a1._00c.get()).offset(0x3L).get());
  }

  @Method(0x8004a8b8L)
  public static void FUN_8004a8b8() {
    final long t3 = _800c6630.getAddress();

    //LAB_8004a8dc
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final SpuStruct124 a1 = _800c4ac8.get(voiceIndex);

      Outer:
      if(a1._0e7.get() == 0x1L && a1._029.get() == 0x1L) {
        //LAB_8004a908
        for(int i = 0; i < 24; i++) {
          if(_800c3a40.get(i)._06.get() == voiceIndex) {
            break Outer;
          }
        }

        a1._020.set(0);
        a1._029.set(0);
        a1._02a.set(0);
        a1._0e7.set(0);
        a1._0ee.set(0);
        a1._0f0.set(0);
        a1._104.set(0);
        a1._105.set(0);
        a1._118.set(0);

        MEMORY.ref(1, t3).offset(0x4bL).setu(0);
      }

      //LAB_8004a96c
      voiceIndex++;
    }

    //LAB_8004a99c
    for(int i = 0; i < 24; i++) {
      final SpuStruct66 spu66 = _800c3a40.get(i);

      if((SPU.voices[i].adsrVolume & 0x7fffL) < 0x10L) {
        if(spu66._08.get() == 0x1L) {
          if(spu66._1a.get() != 0) {
            if(MEMORY.ref(1, t3).offset(0xdL).get() > 0) {
              MEMORY.ref(1, t3).offset(0xdL).subu(0x1L);
            }
          }

          //LAB_8004aa04;
          //LAB_8004aa0c
          for(int n = 0; n < 24; n++) {
            if(_800c3a40.get(n)._0a.get() > spu66._0a.get() && _800c3a40.get(n)._0a.get() != 0xffffL) {
              _800c3a40.get(n)._0a.decr();
            }
          }

          //LAB_8004aa7c
          bzero(spu66.getAddress(), 0x66);
          spu66._06.set(0xffff);
          spu66._26.set(0xffff);
          spu66._24.set(0xffff);
          spu66._22.set(0xffff);
          spu66._0a.set(0xffff);
          spu66._4e.set(120);

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
  }

  @Method(0x8004ae94L)
  public static long FUN_8004ae94(final long a0, final long a1) {
    assert false;
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
  public static void FUN_8004af98(final int voiceIndex) {
    final SpuStruct124 s2 = _800c4ac8.get(voiceIndex);

    if(s2._03c.get() == 0) {
      return;
    }

    long s4 = 0;
    if(s2._03e.get(5).get(0).get() != 0) {
      final long voicePtr = s2.voicePtr_010.get();
      _800c667c.setu(voicePtr);

      final long a0 = s2._03e.get(6).get(0).get();
      if(MEMORY.ref(1, voicePtr).get() != a0) {
        if(s2._03e.get(7).get(0).get() != 0) {
          final long v0 = FUN_8004af3c(a0, s2._03e.get(9).get(0).get(), s2._03e.get(8).get(0).get(), s2._03e.get(7).get(0).get());
          _800c667c.deref(1).setu(v0);
          s2._03e.get(7).get(0).decr();
        } else {
          //LAB_8004b064
          MEMORY.ref(1, voicePtr).setu(a0);
        }

        //LAB_8004b068
        FUN_8004c8dc(voiceIndex, _800c667c.deref(1).get());

        //LAB_8004b084
        s4++;
      }
    }

    //LAB_8004b088
    _800c6680.setu(s2.voicePtr_010.get() + 0x10L);

    //LAB_8004b0a0
    for(int i = 0; i < 0x10L; i++) {
      final long a0 = s2._03e.get(1).get(i).get();
      if(_800c6680.deref(1).offset(0x3L).get() != a0) {
        if(s2._03e.get(0).get(i).get() == 0x1L) {
          if(s2._03e.get(2).get(i).get() != 0) {
            final long v0 = FUN_8004af3c(a0, s2._03e.get(4).get(i).get(), s2._03e.get(3).get(i).get(), s2._03e.get(2).get(i).get());
            _800c6680.deref(1).offset(0x3L).setu(v0);

            s2._03e.get(2).get(i).decr();
          } else {
            //LAB_8004b110
            _800c6680.deref(1).offset(0x3L).setu(a0);
          }

          //LAB_8004b114
          FUN_8004b464(voiceIndex, i, _800c6680.deref(1).offset(0x3L).get());
          s4++;
        }
      }

      //LAB_8004b130
      _800c6680.addu(0x10L);
    }

    if(s4 == 0) {
      //LAB_8004b15c
      for(int n = 0; n < 0xaL; n++) {
        //LAB_8004b16c
        for(int i = 0; i < 0x10L; i++) {
          s2._03e.get(n).get(i).set(0);
        }
      }

      if(s2._03a.get() != 0) {
        s2._03a.set(0);
        FUN_8004d034(voiceIndex, 0x1L);
      }

      //LAB_8004b1c0
      s2._03c.set(0);
    }

    //LAB_8004b1c4
  }

  @Method(0x8004b1e8L)
  public static long FUN_8004b1e8(final int voiceIndex, final long a1, long a2, final long a3) {
    final long v1;

    a2 &= 0xffffL;
    if(a2 == 0xffffL) {
      _800c667c.setu(_800c4ac8.get(voiceIndex).voicePtr_010.get());
      _800c4ac8.get(voiceIndex)._03e.get(5).get(0).set(1);
      _800c4ac8.get(voiceIndex)._03e.get(6).get(0).set((int)a3);
      _800c4ac8.get(voiceIndex)._03e.get(7).get(0).set((int)a1);
      _800c4ac8.get(voiceIndex)._03e.get(8).get(0).set((int)a1);
      _800c4ac8.get(voiceIndex)._03e.get(9).get(0).set((int)_800c667c.deref(1).get());
      v1 = _800c667c.deref(1).get();
    } else {
      //LAB_8004b268
      _800c6680.setu(_800c4ac8.get(voiceIndex).voicePtr_010.get() + (a2 + 0x1L) * 0x10L);
      _800c4ac8.get(voiceIndex)._03e.get(0).get((int)a2).set(1);
      _800c4ac8.get(voiceIndex)._03e.get(1).get((int)a2).set((int)a3);
      _800c4ac8.get(voiceIndex)._03e.get(2).get((int)a2).set((int)a1);
      _800c4ac8.get(voiceIndex)._03e.get(3).get((int)a2).set((int)a1);
      _800c4ac8.get(voiceIndex)._03e.get(4).get((int)a2).set((int)_800c6680.deref(1).offset(0x3L).get());
      v1 = _800c6680.deref(1).offset(0x3L).get();
    }

    //LAB_8004b2b8
    _800c4ac8.get(voiceIndex)._03c.set(1);
    return v1;
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
  public static void FUN_8004b464(final int voiceIndex, final long a1, final long a2) {
    assert false;
  }

  @Method(0x8004b5bcL)
  public static long maxShort(final long a, final long b) {
    return Math.max((short)a, (short)b);
  }

  @Method(0x8004b644L)
  public static long FUN_8004b644(final long a0, final long a1) {
    final long v0;
    long v1;
    if((a0 & 0x8000L) != 0) {
      v0 = (a0 & 0x7fL) << 7;
      v1 = a1 & 0x7fffL;
    } else {
      //LAB_8004b660
      v0 = a1 & 0x7fffL;
      v1 = a0 & 0xffffL;
    }

    //LAB_8004b668
    v1 = (int)(v1 * v0) >> 12;

    final long a0_0;
    if((a1 & 0x8000L) != 0) {
      a0_0 = 0x7fffL - v1;
    } else {
      a0_0 = v1;
    }

    //LAB_8004b688
    return (short)a0_0;
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
    final long blocks = dmaSize / 64 + ((dmaSize & 0x3fL) != 0 ? 1 : 0);
    DMA.spu.BCR.setu(blocks << 16 | 0x10L);

    // Sync mode: sync blocks to DMA request
    // Start/busy: start/enable/busy
    DMA.spu.CHCR.setu(0b1_0000_0000_0000_0010_0000_0000L | transferDirection.ordinal());

    return blocks;
  }

  @Method(0x8004b7c0L)
  public static long FUN_8004b7c0(final long a0) {
    if((int)a0 < 2) {
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

    dmaSpuMadrPtr_800c4a90.setu(DMA.spu.MADR.getAddress());
    dmaSpuBcrPtr_800c4a94.setu(DMA.spu.BCR.getAddress());
    dmaSpuChcrPtr_800c4a98.setu(DMA.spu.CHCR.getAddress());
    dmaDpcrPtr_800c4a9c.setu(DmaManager.DMA_DPCR.getAddress());
    dmaSpuDelayPtr_800c4aa0.setu(SPU_DELAY.getAddress());
    _800c4ac4.setu(SPU.voices[0].LEFT.getAddress());

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
      bzero(_800c3a40.get(a1).getAddress(), 0x64); //Not sure why this wasn't clearing the last one
    }

    //LAB_8004bab8
    for(int a1 = 0; a1 < 127; a1++) {
      //LAB_8004bacc
      _800c43d0.get(a1).used_00.set(0);
      _800c43d0.get(a1).sshdPtr_04.set(0);
      _800c43d0.get(a1).soundBufferPtr_08.set(0);
    }

    //LAB_8004bb14
    for(int i = 0; i < 24; i++) {
      _800c3a40.get(i)._4e.set(120);
    }
  }

  @Method(0x8004be6cL)
  public static boolean isSpuDmaTransferInProgress() {
    return spuDmaTransferInProgress_800c6650.get();
  }

  //TODO RunnableRef
  @Method(0x8004be7cL)
  public static void FUN_8004be7c(final long runnableRef) {
    if(runnableRef == 0) {
      _800c6668.setu(0);
    } else {
      _800c6628.set(MEMORY.ref(4, runnableRef).cast(RunnableRef::new));
      _800c6668.setu(0x1L);
    }
  }

  /** TODO sshdAddress -> object */
  @Method(0x8004bea4L)
  public static long FUN_8004bea4(final long dmaAddress, final long sshdAddress, final long addressInSoundBuffer) {
    if((dmaAddress & 0x3L) != 0) {
      return 0xffffL;
    }

    if((sshdAddress & 0x3L) != 0) {
      return 0xffffL;
    }

    if(addressInSoundBuffer > 0x8_0000L) {
      return 0xffffL;
    }

    if((addressInSoundBuffer & 0xfL) != 0) {
      return 0xffffL;
    }

    if(MEMORY.ref(4, sshdAddress).offset(0xcL).get() != 0x6468_5353L) { // SShd
      return 0xffffL;
    }

    final long dmaSize = MEMORY.ref(4, sshdAddress).offset(0x4L).get();

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
      final SpuStruct0c spu0c = _800c43d0.get(i);

      if(spu0c.used_00.get() == 0) {
        //LAB_8004bfc8
        spu0c.used_00.set(0x1L);
        spu0c.sshdPtr_04.set(sshdAddress);
        spu0c.soundBufferPtr_08.set(addressInSoundBuffer >>> 0x3L);
        return i;
      }
    }

    //LAB_8004c024
    return 0x80L;
  }

  @Method(0x8004c114L)
  public static long FUN_8004c114(final int a0) {
    if((a0 & 0xff80L) != 0) {
      return -0x1L;
    }

    final SpuStruct0c spu0c = _800c43d0.get(a0);

    if(spu0c.used_00.get() != 0x1L) {
      return -0x1L;
    }

    //LAB_8004c160
    for(int i = 0; i < 24; i++) {
      if(_800c3a40.get(i)._00.get() == 0x1L && _800c3a40.get(i)._22.get() == a0) {
        return -0x1L;
      }
    }

    spu0c.used_00.set(0);
    spu0c.sshdPtr_04.set(0);
    spu0c.soundBufferPtr_08.set(0);
    return 0;
  }

  @Method(0x8004c1f8L)
  public static long FUN_8004c1f8(final int a0, final long a1) {
    if((a1 & 0x3L) == 0) {
      if((a0 & 0xff80L) != 0 || MEMORY.ref(4, a1).offset(0xcL).get() != 0x7173_5353L) {
        return -0x1L;
      }

      //LAB_8004c258;
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final SpuStruct124 a3 = _800c4ac8.get(voiceIndex);

        if(a3._027.get() == 0 && a3._029.get() == 0) {
          if(_800c43d0.get(a0).used_00.get() == 0) {
            break;
          }

          voice00LeftPtr_800c4aa4.setu(a1);
          a3._027.set(1);
          a3._028.set(0);
          a3._029.set(0);
          a3._02a.set(0);
          a3._118.set(0);
          a3.voicePtr_010.set(a1);
          a3._00c.set(0x110L);
          a3._000.set((int)voice00LeftPtr_800c4aa4.deref(1).offset(0x110L).get());
          a3._002.set((int)voice00LeftPtr_800c4aa4.deref(1).offset(a3._00c.get()).offset(0x1L).get());
          a3._003.set((int)voice00LeftPtr_800c4aa4.deref(1).offset(a3._00c.get()).offset(0x2L).get());
          a3._020.set(a0);
          a3._001.set(a3._000.get());

          //LAB_8004c308
          for(int i = 0; i < 0x10L; i++) {
            voice00LeftPtr_800c4aa4.deref(1).offset(i * 0x10L).offset(0x1eL).setu((voice00LeftPtr_800c4aa4.deref(1).get() * voice00LeftPtr_800c4aa4.deref(1).offset(i * 0x10L).offset(0x13L).get()) >> 8);
          }

          a3._10a.set((int)MEMORY.ref(2, a1).offset(0x2L).get());
          a3._108.set((int)MEMORY.ref(2, a1).offset(0x4L).get());
          return voiceIndex;
        }

        //LAB_8004c364
      }
    }

    //LAB_8004c380
    //LAB_8004c384
    //LAB_8004c388
    return -0x1L;
  }

  @Method(0x8004c390L)
  public static long FUN_8004c390(final int voiceIndex) {
    final long v0 = _800c4ac8.get(voiceIndex)._028.get();

    if(v0 == 0) {
      //LAB_8004c3d0
      _800c4ac8.get(voiceIndex)._000.set(0);
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
      if(_800c3a40.get(a2)._1a.get() != 0) {
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

  @Method(0x8004c6f8L)
  public static void FUN_8004c6f8(final long a0) {
    assert false;
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
  public static long FUN_8004c8dc(final int voiceIndex, long a1) {
    final long a2 = a1;
    a1 = (short)a1;

    if(voiceIndex >= 0x18L || a1 >= 0x80L || _800c4ac8.get(voiceIndex)._027.get() == 0) {
      //LAB_8004ca64
      return -0x1L;
    }

    final long v1 = _800c4ac8.get(voiceIndex).voicePtr_010.get();
    _800c667c.setu(v1);
    final long s2 = MEMORY.ref(1, v1).get();
    MEMORY.ref(1, v1).setu(a2);
    _800c6680.setu(_800c4ac8.get(voiceIndex).voicePtr_010.get() + 0x10L);

    //LAB_8004c97c
    for(int s0 = 0; s0 < 16; s0++) {
      _800c6680.deref(1).offset(0xeL).setu((_800c6680.deref(1).offset(0x3L).get() * a1 & 0xffff_ffffL) >> 0x7L);
      _800c6680.addu(0x10L);
    }

    //LAB_8004c9d8
    for(int s0 = 0; s0 < 0x18; s0++) {
      final SpuStruct66 spu66 = _800c3a40.get(s0);

      if(spu66._00.get() == 0x1L) {
        if(spu66._22.get() == _800c4ac8.get(voiceIndex)._020.get()) {
          if(spu66._1a.get() == 0) {
            if(spu66._06.get() == (short)voiceIndex) {
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

  @Method(0x8004cb0cL)
  public static long FUN_8004cb0c(final int a0, final long a1) {
    final SpuStruct0c spu0c = _800c43d0.get(a0);

    _800c4ac0.setu(spu0c.sshdPtr_04.get());

    if(spu0c.used_00.get() != 0x1L) {
      return -0x1L;
    }

    if(_800c4ac0.deref(4).offset(0x20L).get() == -0x1L) {
      return -0x1L;
    }

    if((a0 & 0xff80L) != 0) {
      return -0x1L;
    }

    if(a1 < 0x80L) {
      return -0x1L;
    }

    final long v1 = spu0c.sshdPtr_04.get() + _800c4ac0.deref(4).offset(0x20L).get();
    _800c667c.setu(v1);
    final long s3 = MEMORY.ref(1, v1).get();
    MEMORY.ref(1, v1).setu(a1);
    _800c6680.setu(spu0c.sshdPtr_04.get() + _800c4ac0.deref(4).offset(0x20L).get() + 0x10L);

    //LAB_8004cbc8
    for(int i = 0; i < 24; i++) {
      final long v0 = _800c6680.deref(1).offset(0x3L).get() * a1 / 0x80L;
      _800c6680.deref(1).offset(0xeL).setu(v0);
      _800c6680.addu(0x10L);
    }

    //LAB_8004cc1c
    for(int i = 0; i < 24; i++) {
      final SpuStruct66 spu66 = _800c3a40.get(i);

      if(spu66._00.get() == 0x1L) {
        if(spu66._1a.get() == 0x1L) {
          if(spu66._22.get() == a0) {
            FUN_8004ad2c(i);
          }
        }
      }

      //LAB_8004cc6c
    }

    //LAB_8004cc8c
    //LAB_8004cc90
    return s3;
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

  @Method(0x8004cf8cL)
  public static void FUN_8004cf8c(final int voiceIndex) {
    final SpuStruct124 a1 = _800c4ac8.get(voiceIndex);
    final SpuStruct0c spu0c = _800c43d0.get(a1._020.get());

    _800c4ac0.setu(spu0c.sshdPtr_04.get());
    if(a1._027.get() == 0x1L) {
      if(_800c4ac0.deref(4).offset(0x10L).get() != -0x1L) {
        final long v0 = spu0c.used_00.get();
        if(v0 == 0x1L) {
          a1._028.set((int)v0);
          a1._0e8.set(0);
        }
      }
    }

    //LAB_8004d02c
    a1._018.set(0);
  }

  @Method(0x8004d034L)
  public static void FUN_8004d034(final int voiceIndex, final long a1) {
    final SpuStruct124 s1 = _800c4ac8.get(voiceIndex);
    final SpuStruct0c spu0c = _800c43d0.get(s1._020.get());

    _800c4ac0.setu(spu0c.sshdPtr_04.get());

    if(s1._027.get() != 0x1L) {
      return;
    }

    if(_800c4ac0.deref(4).offset(0x10L).get() == 0xffff_ffffL) {
      return;
    }

    if(spu0c.used_00.get() != 0x1L) {
      return;
    }

    final long s5;
    if(a1 == 0 || a1 == 0x1L) {
      //LAB_8004d134
      //LAB_8004d13c
      s1._00c.set(0x110L);
      s1._028.set(0);
      s1._018.set(0x1L);
      s1._0e8.set(0);
      s1._035.set(0);

      if(a1 == 0) {
        return;
      }

      s5 = 0x1L;

      //LAB_8004d11c
    } else if(a1 == 0x2L || a1 == 0x3L) {
      //LAB_8004d154
      if(s1._028.get() != 0) {
        s1._028.set(0);
        s1._0e8.set(1);
        //LAB_8004d170
      } else if(s1._018.get() != 0x1L) {
        s1._028.set(1);
        s1._0e8.set(0);
      }

      if(a1 == 0x3L) {
        return;
      }

      s5 = 0;
    } else {
      return;
    }

    //LAB_8004d1c0
    //LAB_8004d1c4
    //LAB_8004d1d8
    for(int i = 0; i < 0x18L; i++) {
      final SpuStruct66 spu66 = _800c3a40.get(i);

      if(spu66._06.get() == voiceIndex) {
        if(spu66._1a.get() == 0) {
          spu66._08.set(1);
          spu66._00.set(0);
          spu66._38.set(64);
          spu66._14.set(0);
          spu66._16.set(0);
          FUN_800488d4(voiceIndex, i);

          if(s5 != 0) {
            _800c4ac4.deref(2).offset(i * 0x10L).offset(0x8L).setu(0);
            _800c4ac4.deref(2).offset(i * 0x10L).offset(0xaL).setu(0);
          }
        }
      }

      //LAB_8004d254
    }

    _800c6680.setu(s1.voicePtr_010.get() + 0x10L);

    //LAB_8004d27c
    for(int i = 0; i < 0x10L; i++) {
      _800c6680.deref(1).offset(0x9L).setu(0);
      _800c6680.addu(0x10L);
    }

    _800c4ac4.deref(2).offset(0x18cL).setu(s1._0e2.get());
    //wasteSomeCycles(0x2L); //TODO why?
    _800c4ac4.deref(2).offset(0x18eL).setu(s1._0e4.get());
    s1._0e4.set(0);
    s1._0e2.set(0);

    //LAB_8004d2d4
  }

  @Method(0x8004d41cL)
  public static long FUN_8004d41c(final int voiceIndex, final long a1, final long a2) {
    if(a1 >= 0x100L) {
      //LAB_8004d49c
      return -0x1L;
    }

    if(a2 >= 0x80L) {
      return -0x1L;
    }

    if(_800c4ac8.get(voiceIndex)._028.get() == 0) {
      return -0x1L;
    }

    if(a2 == 0) {
      _800c4ac8.get(voiceIndex)._03a.set(1);
    }

    //LAB_8004d48c
    //LAB_8004d4a0
    //LAB_8004d4a4
    return (short)FUN_8004b1e8(voiceIndex, a1, 0xffffL, a2);
  }

  @Method(0x8004d4b4L)
  public static void FUN_8004d4b4(final int voiceIndex, final long a1) {
    if(a1 < 0x3c1L) {
      _800c4ac8.get(voiceIndex)._108.set((int)a1);
    }
  }

  @Method(0x8004d4f8L)
  public static long FUN_8004d4f8(final int voiceIndex) {
    return _800c4ac8.get(voiceIndex)._108.get();
  }

  @Method(0x8004d52cL)
  public static long FUN_8004d52c(final int voiceIndex) {
    long v0 = _800c4ac8.get(voiceIndex)._0e8.get() * 0x2L;
    long v1 = _800c4ac8.get(voiceIndex)._03c.get();
    long a2 = _800c4ac8.get(voiceIndex)._028.get() | v0;

    final long a3;
    if(v1 != 0) {
      if(v0 == 0) {
        v1 |= 0x4L;
      } else {
        v1 = a2;
      }

      //LAB_8004d58c
      a3 = v1 | _800c4ac8.get(voiceIndex)._03a.get() << 3;
    } else {
      a3 = a2;
    }

    //LAB_8004d59c
    return (short)(a3 | _800c6630.offset(0x2aL).get() << 4 | _800c6630.offset(0x2bL).get() << 5);
  }

  @Method(0x8004d648L)
  public static long FUN_8004d648(final int a0, final long a1, final long a2) {
    if(FUN_8004b7e0() == 0) {
      return (short)FUN_80048d44(a0, a1, a2);
    }

    //LAB_8004d68c
    //LAB_8004d690
    return -0x1L;
  }

  @Method(0x8004d6a8L)
  public static long FUN_8004d6a8(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5) {
    assert false;
    return 0;
  }

  @Method(0x8004d91cL)
  public static void FUN_8004d91c(final long a0) {
    if(FUN_8004b7e0() == 0) {
      //LAB_8004d96c
      for(int voiceIndex = 0; voiceIndex < 0x18; voiceIndex++) {
        final SpuStruct66 spu66 = _800c3a40.get(voiceIndex);

        if(spu66._1a.get() != 0) {
          spu66._08.set(1);

          if(a0 != 0) {
            SPU.voices[voiceIndex].adsr.lo = 0;
            SPU.voices[voiceIndex].adsr.hi = 0;
            spu66._00.set(0);
          }

          //LAB_8004d9b8
          if(voiceIndex < 0x10L) {
            SPU_VOICE_KEY_OFF.offset(2, 0x0L).setu(0x1L << voiceIndex);
          } else {
            //LAB_8004d9d4
            SPU_VOICE_KEY_OFF.offset(2, 0x2L).setu(0x1L << voiceIndex - 0x10L);
          }

          //LAB_8004d9e8
          //wasteSomeCycles(2);
        }

        //LAB_8004d9f0
        if(_800c4ac8.get(voiceIndex)._029.get() != 0) {
          _800c4ac8.get(voiceIndex)._029.set(0);
          _800c4ac8.get(voiceIndex)._02a.set(0);

          _800c4ac8.get(voiceIndex)._0e7.set(0);

          _800c4ac8.get(voiceIndex)._0e9.set(0);

          _800c4ac8.get(voiceIndex)._0ee.set(0);
          _800c4ac8.get(voiceIndex)._0f0.set(0);

          _800c4ac8.get(voiceIndex)._104.set(0);
          _800c4ac8.get(voiceIndex)._105.set(0);

          _800c4ac8.get(voiceIndex)._118.set(0);
        }

        //LAB_8004da24
      }
    }

    //LAB_8004da38
  }
}
