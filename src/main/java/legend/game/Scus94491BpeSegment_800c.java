package legend.game;

import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.SyncCode;
import legend.core.gpu.Box;
import legend.core.gpu.DISPENV;
import legend.core.gpu.DRAWENV;
import legend.core.gpu.GpuPacket;
import legend.core.gpu.RECT;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.spu.SpuDmaTransfer;
import legend.game.types.JoyData;

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
  public static final Value _800c34d0 = MEMORY.ref(4, 0x800c34d0L);
  public static final Value doubleBufferFrame_800c34d4 = MEMORY.ref(2, 0x800c34d4L);
  public static final Value doubleBufferOffsetMode_800c34d6 = MEMORY.ref(2, 0x800c34d6L);
  public static final Value _800c34d8 = MEMORY.ref(4, 0x800c34d8L);
  public static final Value _800c34dc = MEMORY.ref(4, 0x800c34dcL);
  public static final Value _800c34e0 = MEMORY.ref(4, 0x800c34e0L);

  public static final MATRIX lightDirectionMatrix_800c34e8 = MEMORY.ref(4, 0x800c34e8L, MATRIX::new);
  public static final MATRIX lightColourMatrix_800c3508 = MEMORY.ref(4, 0x800c3508L, MATRIX::new);
  public static final MATRIX matrix_800c3528 = MEMORY.ref(4, 0x800c3528L, MATRIX::new);
  public static final MATRIX matrix_800c3548 = MEMORY.ref(4, 0x800c3548L, MATRIX::new);
  public static final MATRIX matrix_800c3568 = MEMORY.ref(4, 0x800c3568L, MATRIX::new);
  public static final MATRIX matrix_800c3588 = MEMORY.ref(4, 0x800c3588L, MATRIX::new);

  public static final Value _800c35a4 = MEMORY.ref(4, 0x800c35a4L);
  /** TODO array of pointers to GsCOORDINATE2 */
  public static final Value _800c35a8 = MEMORY.ref(4, 0x800c35a8L);

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
  /**
   * A 102-byte struct? Repeats 24 times (SPU) (0x990 long)
   */
  public static final Value _800c3a40 = MEMORY.ref(2, 0x800c3a40L);
  public static final Value _800c3a62 = MEMORY.ref(2, 0x800c3a62L);
  /**
   * A 12-byte struct? Repeats 127 times
   */
  public static final Value _800c43d0 = MEMORY.ref(4, 0x800c43d0L);
  public static final Value _800c43d4 = MEMORY.ref(4, 0x800c43d4L);
  public static final Value _800c43d8 = MEMORY.ref(4, 0x800c43d8L);

  /** TODO it appears you can queue up 0x20 of these, but that would clobber a bunch of data... only room for 0x10 */
  public static final ArrayRef<SpuDmaTransfer> queuedSpuDmaTransferArray_800c49d0 = MEMORY.ref(4, 0x800c49d0L, ArrayRef.of(SpuDmaTransfer.class, 0x10, 0xc, SpuDmaTransfer::new));

  public static final Value dmaSpuMadrPtr_800c4a90 = MEMORY.ref(4, 0x800c4a90L);
  public static final Value dmaSpuBcrPtr_800c4a94 = MEMORY.ref(4, 0x800c4a94L);
  public static final Value dmaSpuChcrPtr_800c4a98 = MEMORY.ref(4, 0x800c4a98L);
  public static final Value dmaDpcrPtr_800c4a9c = MEMORY.ref(4, 0x800c4a9cL);
  public static final Value dmaSpuDelayPtr_800c4aa0 = MEMORY.ref(4, 0x800c4aa0L);
  public static final Value voice00LeftPtr_800c4aa4 = MEMORY.ref(4, 0x800c4aa4L);
  public static final Value _800c4aa8 = MEMORY.ref(4, 0x800c4aa8L);
  public static final Value _800c4aac = MEMORY.ref(4, 0x800c4aacL);
  public static final Value _800c4ab0 = MEMORY.ref(4, 0x800c4ab0L);
  public static final Value _800c4ab4 = MEMORY.ref(4, 0x800c4ab4L);
  public static final Value _800c4ab8 = MEMORY.ref(4, 0x800c4ab8L);
  public static final Value _800c4abc = MEMORY.ref(4, 0x800c4abcL);
  public static final Value _800c4ac0 = MEMORY.ref(4, 0x800c4ac0L);
  public static final Value _800c4ac4 = MEMORY.ref(4, 0x800c4ac4L);
  public static final Value _800c4ac8 = MEMORY.ref(1, 0x800c4ac8L);
  public static final Value _800c4ac9 = MEMORY.ref(1, 0x800c4ac9L);
  public static final Value _800c4aca = MEMORY.ref(1, 0x800c4acaL);

  public static final Value _800c4ad4 = MEMORY.ref(4, 0x800c4ad4L);

  public static final Value _800c4af0 = MEMORY.ref(1, 0x800c4af0L);
  public static final Value _800c4af1 = MEMORY.ref(1, 0x800c4af1L);
  public static final Value _800c4af2 = MEMORY.ref(1, 0x800c4af2L);

  public static final Value _800c4af4 = MEMORY.ref(4, 0x800c4af4L);

  public static final Value _800c4aff = MEMORY.ref(1, 0x800c4affL);

  public static final Value _800c4b01 = MEMORY.ref(1, 0x800c4b01L);

  public static final Value _800c4ba6 = MEMORY.ref(2, 0x800c4ba6L);
  public static final Value _800c4ba8 = MEMORY.ref(2, 0x800c4ba8L);
  public static final Value _800c4baa = MEMORY.ref(2, 0x800c4baaL);
  public static final Value _800c4bac = MEMORY.ref(2, 0x800c4bacL);
  public static final Value _800c4bae = MEMORY.ref(1, 0x800c4baeL);
  public static final Value _800c4baf = MEMORY.ref(1, 0x800c4bafL);

  public static final Value _800c4bb1 = MEMORY.ref(1, 0x800c4bb1L);

  public static final Value _800c4bb6 = MEMORY.ref(2, 0x800c4bb6L);
  public static final Value _800c4bb8 = MEMORY.ref(2, 0x800c4bb8L);

  public static final Value _800c4bcc = MEMORY.ref(1, 0x800c4bccL);
  public static final Value _800c4bcd = MEMORY.ref(1, 0x800c4bcdL);

  public static final Value _800c4bd0 = MEMORY.ref(2, 0x800c4bd0L);

  public static final Value _800c4be0 = MEMORY.ref(4, 0x800c4be0L);

  public static final Pointer<RunnableRef> _800c6628 = MEMORY.ref(4, 0x800c6628L, Pointer.of(4, RunnableRef::new));

  public static final Value _800c6630 = MEMORY.ref(1, 0x800c6630L);

  public static final Value _800c6633 = MEMORY.ref(1, 0x800c6633L);
  public static final Value _800c6634 = MEMORY.ref(1, 0x800c6634L);

  public static final Value _800c6638 = MEMORY.ref(4, 0x800c6638L);

  public static final Value _800c663d = MEMORY.ref(1, 0x800c663dL);

  public static final Value _800c6642 = MEMORY.ref(2, 0x800c6642L);

  public static final Value _800c6646 = MEMORY.ref(2, 0x800c6646L);

  public static final Value eventSpuIrq_800c664c = MEMORY.ref(4, 0x800c664cL);
  public static final BoolRef spuDmaTransferInProgress_800c6650 = MEMORY.ref(2, 0x800c6650L, BoolRef::new);

  public static final Value _800c665a = MEMORY.ref(1, 0x800c665aL);
  public static final Value _800c665b = MEMORY.ref(1, 0x800c665bL);
  public static final Value _800c665c = MEMORY.ref(2, 0x800c665cL);
  public static final Value _800c665e = MEMORY.ref(2, 0x800c665eL);
  public static final Value _800c6660 = MEMORY.ref(2, 0x800c6660L);
  public static final Value _800c6662 = MEMORY.ref(2, 0x800c6662L);
  public static final Value _800c6664 = MEMORY.ref(2, 0x800c6664L);
  public static final Value spuMono_800c6666 = MEMORY.ref(2, 0x800c6666L);
  public static final Value _800c6668 = MEMORY.ref(1, 0x800c6668L);
  public static final Value spuDmaIndex_800c6669 = MEMORY.ref(1, 0x800c6669L);
  public static final Value _800c666a = MEMORY.ref(2, 0x800c666aL);
  public static final Value _800c666c = MEMORY.ref(2, 0x800c666cL);
  public static final Value _800c666e = MEMORY.ref(2, 0x800c666eL);
  public static final Value _800c6670 = MEMORY.ref(2, 0x800c6670L);

  public static final Value _800c6672 = MEMORY.ref(2, 0x800c6672L);

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

  public static final Value _800ca734 = MEMORY.ref(4, 0x800ca734L);
}
