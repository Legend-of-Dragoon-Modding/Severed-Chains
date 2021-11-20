package legend.game;

import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.SyncCode;
import legend.core.gpu.Box;
import legend.core.gpu.DISPENV;
import legend.core.gpu.DRAWENV;
import legend.core.gpu.GpuPacket;
import legend.core.gpu.RECT;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.spu.SpuDmaTransfer;
import legend.game.types.GsOffsetType;
import legend.game.types.JoyData;
import legend.game.types.PlayableSoundStruct;
import legend.game.types.ScriptStruct;
import legend.game.types.SpuStruct124;
import legend.game.types.SpuStruct44;
import legend.game.types.SpuStruct66;
import legend.game.types.SshdFile;

import static legend.core.Hardware.MEMORY;

public final class Scus94491BpeSegment_800c {
  private Scus94491BpeSegment_800c() { }

  public static final Value _800c13a8 = MEMORY.ref(1, 0x800c13a8L);
  public static final Value _800c13a9 = MEMORY.ref(1, 0x800c13a9L);

  public static final Value _800c1434 = MEMORY.ref(4, 0x800c1434L);

  public static final Value _800c1ba8 = MEMORY.ref(4, 0x800c1ba8L);

  public static final Value _800c1bb0 = MEMORY.ref(4, 0x800c1bb0L);
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> cdromReadCompleteSubSubCallbackPtr_800c1bb4 = MEMORY.ref(4, 0x800c1bb4L, Pointer.of(4, BiConsumerRef::new));
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> _800c1bb8 = MEMORY.ref(4, 0x800c1bb8L, Pointer.of(4, BiConsumerRef::new));

  public static final GpuPacket _800c1bc0 = MEMORY.ref(4, 0x800c1bc0L, GpuPacket::new);

  public static final GpuPacket _800c1be8 = MEMORY.ref(4, 0x800c1be8L, GpuPacket::new);

  // GPU DMA callback structure (60h bytes, repeats 40h times)
  public static final Value gpuDmaCallback_800c1c10 = MEMORY.ref(4, 0x800c1c10L);
  public static final Value gpuDmaCallbackObjPtr_800c1c14 = MEMORY.ref(4, 0x800c1c14L);
  public static final Value gpuDmaCallbackSomething_800c1c18 = MEMORY.ref(4, 0x800c1c18L);
  public static final Value gpuDmaCallbackObj_800c1c1c = MEMORY.ref(4, 0x800c1c1cL);

  public static final Value _800c3410 = MEMORY.ref(4, 0x800c3410L);

  /** Note this is overlapped by {@link #_800c3423} */
  public static final Value _800c3420 = MEMORY.ref(4, 0x800c3420L);
  public static final Value _800c3423 = MEMORY.ref(1, 0x800c3423L);
  public static final Value _800c3424 = MEMORY.ref(1, 0x800c3424L);
  public static final Value _800c3425 = MEMORY.ref(1, 0x800c3425L);
  public static final Value _800c3426 = MEMORY.ref(1, 0x800c3426L);
  public static final Value _800c3427 = MEMORY.ref(1, 0x800c3427L);
  public static final Value _800c3428 = MEMORY.ref(2, 0x800c3428L);
  public static final Value _800c342a = MEMORY.ref(2, 0x800c342aL);
  public static final Value _800c342c = MEMORY.ref(2, 0x800c342cL);
  public static final Value _800c342e = MEMORY.ref(2, 0x800c342eL);

  public static final Value _800c3433 = MEMORY.ref(1, 0x800c3433L);

  public static final Value _800c3437 = MEMORY.ref(1, 0x800c3437L);

  public static final Box clip_800c3440 = MEMORY.ref(8, 0x800c3440L, Box::new);
  public static final Box clip_800c3448 = MEMORY.ref(8, 0x800c3448L, Box::new);
  public static final DRAWENV DRAWENV_800c3450 = MEMORY.ref(92, 0x800c3450L, DRAWENV::new);
  public static final DISPENV DISPENV_800c34b0 = MEMORY.ref(20, 0x800c34b0L, DISPENV::new);

  /**
   * Something to do with rendering/GTE
   */
  public static final Value _800c34c4 = MEMORY.ref(2, 0x800c34c4L);
  /**
   * Something to do with rendering/GTE
   */
  public static final Value _800c34c6 = MEMORY.ref(2, 0x800c34c6L);
  public static final RECT displayRect_800c34c8 = MEMORY.ref(8, 0x800c34c8L, RECT::new);
  /** Incremented with each frame - overflows to 1 */
  public static final Value PSDCNT_800c34d0 = MEMORY.ref(4, 0x800c34d0L);
  /** Double buffer index */
  public static final Value PSDIDX_800c34d4 = MEMORY.ref(2, 0x800c34d4L);
  public static final EnumRef<GsOffsetType> doubleBufferOffsetMode_800c34d6 = MEMORY.ref(2, 0x800c34d6L, EnumRef.of(GsOffsetType::getValue, GsOffsetType.values()));
  public static final Value _800c34d8 = MEMORY.ref(4, 0x800c34d8L);
  public static final Value _800c34dc = MEMORY.ref(4, 0x800c34dcL);
  public static final Value _800c34e0 = MEMORY.ref(4, 0x800c34e0L);

  public static final MATRIX lightDirectionMatrix_800c34e8 = MEMORY.ref(4, 0x800c34e8L, MATRIX::new);
  public static final MATRIX lightColourMatrix_800c3508 = MEMORY.ref(4, 0x800c3508L, MATRIX::new);
  public static final MATRIX matrix_800c3528 = MEMORY.ref(4, 0x800c3528L, MATRIX::new);
  public static final MATRIX matrix_800c3548 = MEMORY.ref(4, 0x800c3548L, MATRIX::new);
  public static final MATRIX identityMatrix_800c3568 = MEMORY.ref(4, 0x800c3568L, MATRIX::new);
  public static final MATRIX matrix_800c3588 = MEMORY.ref(4, 0x800c3588L, MATRIX::new);

  public static final Value _800c35a4 = MEMORY.ref(4, 0x800c35a4L);
  public static final UnboundedArrayRef<Pointer<GsCOORDINATE2>> coord2s_800c35a8 = MEMORY.ref(4, 0x800c35a8L, UnboundedArrayRef.of(4, Pointer.deferred(4, GsCOORDINATE2::new)));

  public static final Value _800c3658 = MEMORY.ref(4, 0x800c3658L);

  public static final Value _800c37a4 = MEMORY.ref(4, 0x800c37a4L);

  public static final ArrayRef<JoyData> joyData_800c37b8 = MEMORY.ref(480, 0x800c37b8L, ArrayRef.of(JoyData.class, 2, 240, JoyData::new));
  public static final ArrayRef<UnsignedByteRef> responseBuffer0_800c3998 = MEMORY.ref(1, 0x800c3998L, ArrayRef.of(UnsignedByteRef.class, 35, 1, UnsignedByteRef::new));
  public static final ArrayRef<UnsignedByteRef> responseBuffer1_800c39bb = MEMORY.ref(1, 0x800c39bbL, ArrayRef.of(UnsignedByteRef.class, 35, 1, UnsignedByteRef::new));
  public static final ArrayRef<ByteRef> inputBuffer_800c39e0 = MEMORY.ref(1, 0x800c39e0L, ArrayRef.of(ByteRef.class, 37, 1, ByteRef::new));
  public static final ArrayRef<ByteRef> inputBuffer_800c3a03 = MEMORY.ref(1, 0x800c3a03L, ArrayRef.of(ByteRef.class, 37, 1, ByteRef::new));
  public static final Value _800c3a28 = MEMORY.ref(4, 0x800c3a28L);
  public static final Value joypadTimeoutCurrentTime_800c3a2c = MEMORY.ref(4, 0x800c3a2cL);
  public static final Value joypadTimeoutTimeout_800c3a30 = MEMORY.ref(4, 0x800c3a30L);
  public static final Value joypadTimeoutMode_800c3a34 = MEMORY.ref(4, 0x800c3a34L);

  public static final Value _800c3a38 = MEMORY.ref(4, 0x800c3a38L);
  public static final Value _800c3a3c = MEMORY.ref(4, 0x800c3a3cL);
  /** 0x990 bytes long */
  public static final ArrayRef<SpuStruct66> _800c3a40 = MEMORY.ref(4, 0x800c3a40L, ArrayRef.of(SpuStruct66.class, 24, 0x66, SpuStruct66::new));
  /** 0x5f4 bytes long */
  public static final ArrayRef<PlayableSoundStruct> playableSoundPtrArr_800c43d0 = MEMORY.ref(4, 0x800c43d0L, ArrayRef.of(PlayableSoundStruct.class, 127, 0xc, PlayableSoundStruct::new));

  /** TODO it appears you can queue up 0x20 of these, but that would clobber a bunch of data... only room for 0x10 */
  public static final ArrayRef<SpuDmaTransfer> queuedSpuDmaTransferArray_800c49d0 = MEMORY.ref(4, 0x800c49d0L, ArrayRef.of(SpuDmaTransfer.class, 0x10, 0xc, SpuDmaTransfer::new));

  public static final Value dmaSpuMadrPtr_800c4a90 = MEMORY.ref(4, 0x800c4a90L);
  public static final Value dmaSpuBcrPtr_800c4a94 = MEMORY.ref(4, 0x800c4a94L);
  public static final Value dmaSpuChcrPtr_800c4a98 = MEMORY.ref(4, 0x800c4a98L);
  public static final Value dmaDpcrPtr_800c4a9c = MEMORY.ref(4, 0x800c4a9cL);
  public static final Value dmaSpuDelayPtr_800c4aa0 = MEMORY.ref(4, 0x800c4aa0L);
  /** NOTE: this pointer can be misaligned, hence being a Value */
  public static final Value sssqPtr_800c4aa4 = MEMORY.ref(4, 0x800c4aa4L);
  public static final Value _800c4aa8 = MEMORY.ref(4, 0x800c4aa8L);
  public static final Value _800c4aac = MEMORY.ref(4, 0x800c4aacL);
  public static final Value _800c4ab0 = MEMORY.ref(4, 0x800c4ab0L);
  public static final Value _800c4ab4 = MEMORY.ref(4, 0x800c4ab4L);
  public static final Value _800c4ab8 = MEMORY.ref(4, 0x800c4ab8L);
  public static final Value _800c4abc = MEMORY.ref(4, 0x800c4abcL);
  public static final Pointer<SshdFile> sshdPtr_800c4ac0 = MEMORY.ref(4, 0x800c4ac0L, Pointer.deferred(4, SshdFile::new));
  public static final Value voicePtr_800c4ac4 = MEMORY.ref(4, 0x800c4ac4L);
  public static final ArrayRef<SpuStruct124> _800c4ac8 = MEMORY.ref(4, 0x800c4ac8L, ArrayRef.of(SpuStruct124.class, 24, 0x124, SpuStruct124::new));
  public static final Pointer<RunnableRef> _800c6628 = MEMORY.ref(4, 0x800c6628L, Pointer.of(4, RunnableRef::new));

  public static final SpuStruct44 _800c6630 = MEMORY.ref(4, 0x800c6630L, SpuStruct44::new);
  public static final Value _800c6674 = MEMORY.ref(4, 0x800c6674L);
  public static final Value _800c6678 = MEMORY.ref(4, 0x800c6678L);
  public static final Value _800c667c = MEMORY.ref(4, 0x800c667cL);
  public static final Value _800c6680 = MEMORY.ref(4, 0x800c6680L);

  public static final Value _800c6688 = MEMORY.ref(4, 0x800c6688L);
  public static final CdlFILE fileSInitOvl_800c668c = MEMORY.ref(0x18, 0x800c668cL, CdlFILE::new);

  public static final Value SInitOvlData_800c66a4 = MEMORY.ref(4, 0x800c66a4L);

  public static final Value SInitOvlFileName_800c66ac = MEMORY.ref(1, 0x800c66acL);

  public static final Value sceaLogoTextureLoaded_800c672c = MEMORY.ref(4, 0x800c672cL);
  public static final Value sceaLogoDisplayTime_800c6730 = MEMORY.ref(4, 0x800c6730L);
  public static final Value sceaLogoAlpha_800c6734 = MEMORY.ref(4, 0x800c6734L);

  public static final Value _800c6740 = MEMORY.ref(1, 0x800c6740L);

  public static final Value timHeader_800c6748 = MEMORY.ref(4, 0x800c6748L);

  /** TODO this is in an overlay that we don't have yet */
  public static final FunctionRef<ScriptStruct, Long> scriptSubFunction_800ca734 = MEMORY.ref(4, 0x800ca734L, FunctionRef::new);
}
