package legend.game;

import legend.core.cdrom.CdlCOMMAND;
import legend.core.cdrom.CdlLOC;
import legend.core.cdrom.SyncCode;
import legend.core.gpu.DISPENV;
import legend.core.gpu.DRAWENV;
import legend.core.gte.MATRIX;
import legend.core.kernel.PriorityChainEntry;
import legend.core.kernel.jmp_buf;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.types.GsOT;
import legend.game.types.GsOT_TAG;
import legend.game.types.JoyData;

import java.util.function.Function;

import static legend.core.Hardware.GATE;
import static legend.core.Hardware.MEMORY;
import static legend.core.LibDs.DSL_MAX_RESULTS;

public final class Scus94491BpeSegment_8005 {
  private Scus94491BpeSegment_8005() { }

  public static final Value _80050068 = MEMORY.ref(2, 0x80050068L);

  public static final Value _800500e8 = MEMORY.ref(4, 0x800500e8L);

  public static final Value _800500f8 = MEMORY.ref(4, 0x800500f8L);

  public static final Value _80050274 = MEMORY.ref(4, 0x80050274L);

  public static final Value _800503b0 = MEMORY.ref(2, 0x800503b0L);

  public static final Value _800503d4 = MEMORY.ref(2, 0x800503d4L);

  public static final Value _800503f8 = MEMORY.ref(2, 0x800503f8L);

  public static final Value _80050424 = MEMORY.ref(2, 0x80050424L);

  public static final Value _80052ae0 = MEMORY.ref(2, 0x80052ae0L);

  public static final Value submapCut_80052c30 = MEMORY.ref(4, 0x80052c30L);
  public static final Value _80052c34 = MEMORY.ref(4, 0x80052c34L);
  public static final UnsignedIntRef index_80052c38 = MEMORY.ref(4, 0x80052c38L, UnsignedIntRef::new);
  public static final Value _80052c3c = MEMORY.ref(4, 0x80052c3cL);
  public static final Value _80052c40 = MEMORY.ref(4, 0x80052c40L);
  public static final Value _80052c44 = MEMORY.ref(4, 0x80052c44L);
  public static final Value _80052c48 = MEMORY.ref(4, 0x80052c48L);
  public static final Value _80052c4c = MEMORY.ref(2, 0x80052c4cL);

  public static final ArrayRef<UnsignedByteRef> _80052c5c = MEMORY.ref(1, 0x80052c5cL, ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new));

  /** Array */
  public static final Value _80052c64 = MEMORY.ref(1, 0x80052c64L);

  public static final Value _80052c74 = MEMORY.ref(2, 0x80052c74L);

  public static final Value _80052c94 = MEMORY.ref(2, 0x80052c94L);

  public static final Value _80052d6c = MEMORY.ref(2, 0x80052d6cL);

  public static final Value _80052d7c = MEMORY.ref(4, 0x80052d7cL);

  public static final Value _80052db0 = MEMORY.ref(2, 0x80052db0L);

  public static final Value _80052dbc = MEMORY.ref(4, 0x80052dbcL);
  public static final Value _80052dc0 = MEMORY.ref(4, 0x80052dc0L);

  public static final Value _80052e1c = MEMORY.ref(4, 0x80052e1cL);

  public static final Value _80052e2c = MEMORY.ref(4, 0x80052e2cL);
  public static final Value _80052e30 = MEMORY.ref(4, 0x80052e30L);

  public static final Value memcardEventIndex_80052e4c = MEMORY.ref(4, 0x80052e4cL);

  public static final Value _80052f24 = MEMORY.ref(4, 0x80052f24L);

  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> cdromCompleteInterruptCallbackPtr_80052f44 = MEMORY.ref(4, 0x80052f44L, Pointer.of(4, BiConsumerRef::new));
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> cdromDataInterruptCallbackPtr_80052f48 = MEMORY.ref(4, 0x80052f48L, Pointer.of(4, BiConsumerRef::new));
  public static final Value CD_debug_80052f4c = MEMORY.ref(4, 0x80052f4cL);
  public static final Value _80052f50 = MEMORY.ref(4, 0x80052f50L);
  public static final Value _80052f54 = MEMORY.ref(4, 0x80052f54L);
  public static final Value _80052f58 = MEMORY.ref(4, 0x80052f58L);
  public static final ArrayRef<ByteRef> cdromParamBuffer_80052f5c = MEMORY.ref(4, 0x80052f5cL, ArrayRef.of(ByteRef.class, 4, 1, ByteRef::new));
  public static final Value cdromMode_80052f60 = MEMORY.ref(1, 0x80052f60L);
  public static final EnumRef<CdlCOMMAND> previousCdromCommand_80052f61 = MEMORY.ref(1, 0x80052f61L, EnumRef.of(CdlCOMMAND::getCommand, CdlCOMMAND.values()));

  public static final Value usingLibDs_80052f64 = MEMORY.ref(4, 0x80052f64L);

  public static final ArrayRef<UnsignedIntRef> _80053008 = MEMORY.ref(4, 0x80053008L, ArrayRef.of(UnsignedIntRef.class, 0x20, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> _80053088 = MEMORY.ref(4, 0x80053088L, ArrayRef.of(UnsignedIntRef.class, 0x20, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> _80053108 = MEMORY.ref(4, 0x80053108L, ArrayRef.of(UnsignedIntRef.class, 0x20, 4, UnsignedIntRef::new));

  public static final EnumRef<SyncCode> cdromSyncCode_80053220 = MEMORY.ref(1, 0x80053220L, EnumRef.of(SyncCode.values()));
  public static final EnumRef<SyncCode> cdromDataSyncCode_80053221 = MEMORY.ref(1, 0x80053221L, EnumRef.of(SyncCode.values()));
  public static final EnumRef<SyncCode> cdromDataEndSyncCode_80053222 = MEMORY.ref(1, 0x80053222L, EnumRef.of(SyncCode.values()));

  public static final Value ptrCdromSyncCode_80053220_80053224 = MEMORY.ref(4, 0x80053224L);

  public static final Value _8005324c = MEMORY.ref(4, 0x8005324cL);

  public static final Value cdlPacketBatch_800532cc = MEMORY.ref(4, 0x800532ccL);

  public static final Value _800532e4 = MEMORY.ref(4, 0x800532e4L);
  public static final EnumRef<CdlCOMMAND> cdromCommand_800532e8 = MEMORY.ref(1, 0x800532e8L, EnumRef.of(CdlCOMMAND::getCommand, CdlCOMMAND.values()));
  public static final ArrayRef<ByteRef> cdromParams_800532e9 = MEMORY.ref(1, 0x800532e9L, ArrayRef.of(ByteRef.class, 4, 1, ByteRef::new));
  public static final Pointer<ArrayRef<ByteRef>> ptrCdromParams_800532f0 = MEMORY.ref(4, 0x800532f0L, Pointer.of(4, ArrayRef.of(ByteRef.class, 4, 1, ByteRef::new)));
  public static final ArrayRef<ByteRef> cdromResponse_800532f4 = MEMORY.ref(DSL_MAX_RESULTS, 0x800532f4L, ArrayRef.of(ByteRef.class, DSL_MAX_RESULTS, 1, ByteRef::new));
  public static final Value cdromStat_800532fc = MEMORY.ref(1, 0x800532fcL);
  public static final Value _80053300 = MEMORY.ref(4, 0x80053300L);
  public static final Value _80053304 = MEMORY.ref(4, 0x80053304L);
  public static final Value _80053308 = MEMORY.ref(4, 0x80053308L);
  public static final Value _8005330c = MEMORY.ref(4, 0x8005330cL);
  public static final EnumRef<CdlCOMMAND> cdromCommand_80053310 = MEMORY.ref(1, 0x80053310L, EnumRef.of(CdlCOMMAND::getCommand, CdlCOMMAND.values()));
  public static final ByteRef oldCdromParam_80053311 = MEMORY.ref(1, 0x80053311L, ByteRef::new);
  public static final CdlLOC cdlLoc_80053312 = MEMORY.ref(1, 0x80053312L, CdlLOC::new);
  public static final EnumRef<CdlCOMMAND> cdromCommand_80053316 = MEMORY.ref(1, 0x80053316L, EnumRef.of(CdlCOMMAND::getCommand, CdlCOMMAND.values()));
  public static final EnumRef<CdlCOMMAND> cdromCommand_80053317 = MEMORY.ref(1, 0x80053317L, EnumRef.of(CdlCOMMAND::getCommand, CdlCOMMAND.values()));
  public static final BoolRef isPlaying_80053318 = MEMORY.ref(1, 0x80053318L, BoolRef::new);
  public static final BoolRef isSeeking_80053319 = MEMORY.ref(1, 0x80053319L, BoolRef::new);
  public static final BoolRef isReading_8005331a = MEMORY.ref(1, 0x8005331aL, BoolRef::new);
  public static final BoolRef isMotorOn_8005331b = MEMORY.ref(1, 0x8005331bL, BoolRef::new);
  public static final Value _8005331c = MEMORY.ref(4, 0x8005331cL);
  public static final EnumRef<SyncCode> syncCode_80053320 = MEMORY.ref(4, 0x80053320L, EnumRef.of(SyncCode.values()));
  public static final Value _80053324 = MEMORY.ref(4, 0x80053324L);
  public static final Value _80053328 = MEMORY.ref(4, 0x80053328L);
  public static final Value _8005332c = MEMORY.ref(4, 0x8005332cL);
  public static final Value _80053330 = MEMORY.ref(4, 0x80053330L);
  public static final Value _80053334 = MEMORY.ref(1, 0x80053334L);
  public static final EnumRef<CdlCOMMAND> cdromCommand_80053335 = MEMORY.ref(1, 0x80053335L, EnumRef.of(CdlCOMMAND::getCommand, CdlCOMMAND.values()));

  /**
   * An array containing one int per command - unsure of purpose
   */
  public static final ArrayRef<UnsignedIntRef> cdromCommandSomethingArray_80053338 = MEMORY.ref(0x80, 0x80053338L, ArrayRef.of(UnsignedIntRef.class, 0x20, 4, UnsignedIntRef::new));

  /**
   * The response byte that holds the cdrom status
   */
  public static final ArrayRef<UnsignedIntRef> cdromCommandResponseStatOffsetArray_800533b8 = MEMORY.ref(0x80, 0x800533b8L, ArrayRef.of(UnsignedIntRef.class, 0x20, 4, UnsignedIntRef::new));

  public static final Value _80053438 = MEMORY.ref(4, 0x80053438L);

  public static final Value _80053448 = MEMORY.ref(4, 0x80053448L);

  public static final Value oldCdromDmaCallback_80053450 = MEMORY.ref(4, 0x80053450L);

  public static final BoolRef doingCdromDmaTransfer_8005345c = MEMORY.ref(4, 0x8005345cL, BoolRef::new);

  public static final Value cdromFilePointer_8005346c = MEMORY.ref(4, 0x8005346cL);
  public static final EnumRef<SyncCode> syncCode_80053470 = MEMORY.ref(4, 0x80053470L, EnumRef.of(SyncCode.values()));

  public static final Value cdromDmaPos_8005347c = MEMORY.ref(4, 0x8005347cL);
  public static final Value cdromPreviousDmaPos_80053480 = MEMORY.ref(4, 0x80053480L);
  public static final Value cdromDmaStatusCallback_80053484 = MEMORY.ref(4, 0x80053484L);
  public static final Value _80053488 = MEMORY.ref(4, 0x80053488L);
  public static final Value _8005348c = MEMORY.ref(4, 0x8005348cL);

  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> _80053490 = MEMORY.ref(4, 0x80053490L, Pointer.of(4, BiConsumerRef::new));
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> _80053494 = MEMORY.ref(4, 0x80053494L, Pointer.of(4, BiConsumerRef::new));
  public static final Value _80053498 = MEMORY.ref(4, 0x80053498L);
  public static final Value _8005349c = MEMORY.ref(4, 0x8005349cL);

  public static final Value _800534fc = MEMORY.ref(4, 0x800534fcL);
  public static final Value _80053500 = MEMORY.ref(4, 0x80053500L);

  public static final BoolRef interruptHandlersInitialized_80053564 = MEMORY.ref(2, 0x80053564L, BoolRef::new);
  public static final BoolRef inExceptionHandler_80053566 = MEMORY.ref(2, 0x80053566L, BoolRef::new);
  public static final Value interruptCallbacks_80053568 = MEMORY.ref(4, 0x80053568L);

  public static final Value _80053594 = MEMORY.ref(2, 0x80053594L);

  public static final jmp_buf jmp_buf_8005359c = MEMORY.ref(4, 0x8005359cL, jmp_buf::new);
  public static final Value _800535a0 = MEMORY.ref(4, 0x800535a0L);

  public static final Value _8005457c = MEMORY.ref(4, 0x8005457cL);

  public static final Value _800545ec = MEMORY.ref(4, 0x800545ecL);

  public static final Value _800545fc = MEMORY.ref(4, 0x800545fcL);

  public static final ArrayRef<Pointer<RunnableRef>> vsyncCallbacks_8005460c = MEMORY.ref(32, 0x8005460cL, ArrayRef.of(Pointer.classFor(RunnableRef.class), 8, 4, Pointer.of(4, RunnableRef::new)));

  public static final Value Vcount = MEMORY.ref(4, 0x8005462cL);

  public static final ArrayRef<Pointer<RunnableRef>> dmaCallbacks_80054640 = MEMORY.ref(28, 0x80054640L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 7, 4, Pointer.of(4, RunnableRef::new)));

  public static final Value _80054674 = MEMORY.ref(4, 0x80054674L);

  public static final Value _800546b4 = MEMORY.ref(4, 0x800546b4L);

  public static final Value _800546bc = MEMORY.ref(1, 0x800546bcL);
  public static final Value _800546bd = MEMORY.ref(1, 0x800546bdL);
  public static final Value gpu_debug = MEMORY.ref(1, 0x800546beL);
  public static final Value gpuReverseFlag_800546bf = MEMORY.ref(1, 0x800546bfL);

  /**
   * Max RECT width?
   */
  public static final Value _800546c0 = MEMORY.ref(2, 0x800546c0L);
  /**
   * Max RECT height?
   */
  public static final Value _800546c2 = MEMORY.ref(2, 0x800546c2L);
  public static final Value _800546c4 = MEMORY.ref(4, 0x800546c4L);
  public static final Value drawSyncCallback_800546c8 = MEMORY.ref(4, 0x800546c8L);

  public static final DRAWENV DRAWENV_800546cc = MEMORY.ref(92, 0x800546ccL, DRAWENV::new);
  public static final DISPENV DISPENV_80054728 = MEMORY.ref(20, 0x80054728L, DISPENV::new);

  public static final Value array_8005473c = MEMORY.ref(2, 0x8005473cL);
  public static final Value array_80054748 = MEMORY.ref(2, 0x80054748L);

  public static final Value _8005477c = MEMORY.ref(4, 0x8005477cL);

  public static final Value _80054790 = MEMORY.ref(2, 0x80054790L);
  public static final Value _80054792 = MEMORY.ref(2, 0x80054792L);

  public static final Value _800547bb = MEMORY.ref(1, 0x800547bbL);

  /**
   * Current index in the GPU queue
   */
  public static final Value gpuQueueIndex_800547e4 = MEMORY.ref(4, 0x800547e4L);
  /**
   * Total entries currently in the GPU queue
   */
  public static final Value gpuQueueTotal_800547e8 = MEMORY.ref(4, 0x800547e8L);
  public static final Value oldIMask_800547ec = MEMORY.ref(4, 0x800547ecL);
  public static final Value oldIMask_800547f0 = MEMORY.ref(4, 0x800547f0L);

  public static final Value _800547f4 = MEMORY.ref(4, 0x800547f4L);
  public static final Value _800547f8 = MEMORY.ref(4, 0x800547f8L);
  public static final Value _800547fc = MEMORY.ref(4, 0x800547fcL);

  public static final Value GsOUT_PACKET_P = MEMORY.ref(4, 0x8005480cL);

  public static final Value matrixStackIndex_80054a08 = MEMORY.ref(4, 0x80054a08L);
  public static final ArrayRef<MATRIX> matrixStack_80054a0c = MEMORY.ref(640, 0x80054a0cL, ArrayRef.of(MATRIX.class, 20, 32, MATRIX::new));

  /** Precomputed sin/cos table */
  public static final Value sin_cos_80054d0c = MEMORY.ref(4, 0x80054d0cL);

  public static final Value _80058d0c = MEMORY.ref(2, 0x80058d0cL);

  public static final PriorityChainEntry joypadVblankIrqHandler_8005952c = MEMORY.ref(4, 0x8005952cL, PriorityChainEntry::new);
  public static final PriorityChainEntry joypadPriorityChain_8005953c = MEMORY.ref(4, 0x8005953cL, PriorityChainEntry::new);
  public static final Value _8005954c = MEMORY.ref(4, 0x8005954cL);
  public static final Value _80059550 = MEMORY.ref(4, 0x80059550L);
  public static final Value _80059554 = MEMORY.ref(4, 0x80059554L);

  public static final Value _8005955c = MEMORY.ref(4, 0x8005955cL);
  public static final Value _80059560 = MEMORY.ref(4, 0x80059560L);
  public static final Value _80059564 = MEMORY.ref(4, 0x80059564L);

  static {
    GATE.acquire();
  }

  public static final Pointer<Pointer<ArrayRef<Pointer<PriorityChainEntry>>>> priorityChain_80059570 = MEMORY.ref(4, 0x80059570L, Pointer.of(4, Pointer.of(0x20, ArrayRef.of(Pointer.classFor(PriorityChainEntry.class), 4, 4, 8, Pointer.of(0x10, PriorityChainEntry::new)))));

  static {
    GATE.release();
  }

  public static final Value _800595a0 = MEMORY.ref(4, 0x800595a0L);

  public static final Pointer<FunctionRef<Long, Long>> _800595d4 = MEMORY.ref(4, 0x800595d4L, Pointer.of(4, FunctionRef::new));
  public static final Pointer<ConsumerRef<JoyData>> ptrClearJoyData_800595d8 = MEMORY.ref(4, 0x800595d8L, Pointer.of(4, ConsumerRef::new));
  public static final Pointer<BiFunctionRef<JoyData, Long, Integer>> getNextJoypadCommandParam_800595dc = MEMORY.ref(4, 0x800595dcL, Pointer.of(4, BiFunctionRef::new));
  public static final Pointer<ConsumerRef<JoyData>> _800595e0 = MEMORY.ref(4, 0x800595e0L, Pointer.of(4, ConsumerRef::new));

  public static final Pointer<FunctionRef<Integer, JoyData>> ptrGetJoyDataForPort_800595e8 = MEMORY.ref(4, 0x800595e8L, Pointer.of(4, FunctionRef::new));
  public static final Pointer<FunctionRef<JoyData, Long>> _800595ec = MEMORY.ref(4, 0x800595ecL, Pointer.of(4, FunctionRef::new));
  public static final Pointer<FunctionRef<JoyData, Long>> _800595f0 = MEMORY.ref(4, 0x800595f0L, Pointer.of(4, FunctionRef::new));
  public static final Pointer<ConsumerRef<JoyData>> _800595f4 = MEMORY.ref(4, 0x800595f4L, Pointer.of(4, ConsumerRef::new));
  public static final Pointer<FunctionRef<JoyData, Long>> _800595f8 = MEMORY.ref(4, 0x800595f8L, Pointer.of(4, FunctionRef::new));
  public static final Pointer<RunnableRef> _800595fc = MEMORY.ref(4, 0x800595fcL, Pointer.of(4, RunnableRef::new));
  public static final Pointer<RunnableRef> _80059600 = MEMORY.ref(4, 0x80059600L, Pointer.of(4, RunnableRef::new));
  public static final Pointer<RunnableRef> _80059604 = MEMORY.ref(4, 0x80059604L, Pointer.of(4, RunnableRef::new));
  public static final Pointer<ArrayRef<JoyData>> ptrArrJoyData_80059608 = MEMORY.ref(4, 0x80059608L, Pointer.deferred(4, ArrayRef.of(JoyData.class, 2, 240, JoyData::new)));
  public static final BoolRef joypadsReady_8005960c = MEMORY.ref(4, 0x8005960cL, BoolRef::new);
  public static final Value _80059610 = MEMORY.ref(4, 0x80059610L);
  public static final IntRef joyDataIndex_80059614 = MEMORY.ref(4, 0x80059614L, IntRef::new);
  public static final Value joypadCallbackIndex_80059618 = MEMORY.ref(4, 0x80059618L);
  public static final Value _8005961c = MEMORY.ref(4, 0x8005961cL);
  public static final Value _80059620 = MEMORY.ref(4, 0x80059620L);
  public static final Value _80059624 = MEMORY.ref(4, 0x80059624L);
  /** The number of joypads to use (0-indexed) (default: 1, changed to 0) */
  public static final Value maxJoypadIndex_80059628 = MEMORY.ref(4, 0x80059628L);
  public static final ArrayRef<IntRef> joySomething_8005962c = MEMORY.ref(8, 0x8005962cL, ArrayRef.of(IntRef.class, 2, 4, IntRef::new));

  public static final PriorityChainEntry joypadVblankIrqHandler_80059634 = MEMORY.ref(4, 0x80059634L, PriorityChainEntry::new);
  public static final Value _80059644 = MEMORY.ref(4, 0x80059644L);

  public static final Value _80059650 = MEMORY.ref(4, 0x80059650L);
  public static final Value _80059654 = MEMORY.ref(4, 0x80059654L);
  public static final Value _80059658 = MEMORY.ref(4, 0x80059658L);
  public static final ArrayRef<Pointer<FunctionRef<JoyData, Long>>> joypadCallbacks_8005965c = (ArrayRef<Pointer<FunctionRef<JoyData, Long>>>)MEMORY.ref(4, 0x8005965cL, ArrayRef.of(Pointer.class, 5, 4, (Function)Pointer.of(4, FunctionRef::new)));
  public static final Value _8005967c = MEMORY.ref(2, 0x8005967cL);

  public static final Value _80059b3c = MEMORY.ref(1, 0x80059b3cL);

  public static final Value _80059f3c = MEMORY.ref(1, 0x80059f3cL);

  /**
   * Start of a fairly large block of data - something to do with SPU reverb initialisation. Stride is 66 bytes. Unknown length.
   */
  public static final Value _80059f7c = MEMORY.ref(2, 0x80059f7cL);
  public static final Value _80059f7e = MEMORY.ref(2, 0x80059f7eL);

  public static final Value sssqFadeCurrent_8005a1ce = MEMORY.ref(2, 0x8005a1ceL);
  public static final Value _8005a1d0 = MEMORY.ref(4, 0x8005a1d0L);

  public static final Value _8005a1d8 = MEMORY.ref(4, 0x8005a1d8L);

  public static final Value _8005a1e0 = MEMORY.ref(4, 0x8005a1e0L);
  public static final Value _8005a1e4 = MEMORY.ref(4, 0x8005a1e4L);

  public static final Value _8005a1ea = MEMORY.ref(2, 0x8005a1eaL);

  public static final Value linkedListHead_8005a2a0 = MEMORY.ref(4, 0x8005a2a0L);
  public static final Value linkedListTail_8005a2a4 = MEMORY.ref(4, 0x8005a2a4L);
  /**
   * 12-byte struct, repeats 16 times? May start 4 bytes earlier
   */
  public static final Value _8005a2a8 = MEMORY.ref(4, 0x8005a2a8L);
  public static final Value _8005a2ac = MEMORY.ref(4, 0x8005a2acL);
  public static final Value _8005a2b0 = MEMORY.ref(4, 0x8005a2b0L);

  public static final ArrayRef<GsOT> orderingTables_8005a370 = MEMORY.ref(4, 0x8005a370L, ArrayRef.of(GsOT.class, 2, 0x14, GsOT::new));
  public static final ArrayRef<UnboundedArrayRef<GsOT_TAG>> _8005a398 = MEMORY.ref(4, 0x8005a398L, ArrayRef.of(UnboundedArrayRef.classFor(GsOT_TAG.class), 2, 0x1_0000, UnboundedArrayRef.of(4, GsOT_TAG::new)));
}
