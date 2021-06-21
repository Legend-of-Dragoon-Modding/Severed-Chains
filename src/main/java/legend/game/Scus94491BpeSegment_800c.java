package legend.game;

import legend.core.cdrom.SyncCode;
import legend.core.gpu.Box;
import legend.core.gpu.DISPENV;
import legend.core.gpu.DRAWENV;
import legend.core.gpu.GpuPacket;
import legend.core.gpu.RECT;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.Pointer;

import static legend.core.Hardware.MEMORY;

public final class Scus94491BpeSegment_800c {
  private Scus94491BpeSegment_800c() { }

  public static final Value _800c1bb0 = MEMORY.ref(4, 0x800c1bb0L);
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> cdromDmaInterruptSubSubCallbackPtr_800c1bb4 = MEMORY.ref(4, 0x800c1bb4L, Pointer.of(BiConsumerRef::new));
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> _800c1bb8 = MEMORY.ref(4, 0x800c1bb8L, Pointer.of(BiConsumerRef::new));

  public static final GpuPacket _800c1bc0 = MEMORY.ref(4, 0x800c1bc0L, GpuPacket::new);

  public static final GpuPacket _800c1be8 = MEMORY.ref(4, 0x800c1be8L, GpuPacket::new);

  // GPU DMA callback structure (60h bytes, repeats 40h times)
  public static final Value gpuDmaCallback_800c1c10 = MEMORY.ref(4, 0x800c1c10L);
  public static final Value gpuDmaCallbackObjPtr_800c1c14 = MEMORY.ref(4, 0x800c1c14L);
  public static final Value gpuDmaCallbackSomething_800c1c18 = MEMORY.ref(4, 0x800c1c18L);
  public static final Value gpuDmaCallbackObj_800c1c1c = MEMORY.ref(4, 0x800c1c1cL);

  public static final Value _800c3423 = MEMORY.ref(1, 0x800c3423L);

  public static final Value _800c3427 = MEMORY.ref(1, 0x800c3427L);

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

  public static final MATRIX matrix_800c34e8 = MEMORY.ref(4, 0x800c34e8L, MATRIX::new);
  public static final MATRIX matrix_800c3508 = MEMORY.ref(4, 0x800c3508L, MATRIX::new);

  public static final MATRIX matrix_800c3568 = MEMORY.ref(4, 0x800c3568L, MATRIX::new);
  public static final MATRIX matrix_800c3588 = MEMORY.ref(4, 0x800c3588L, MATRIX::new);

  public static final Value _800c3658 = MEMORY.ref(4, 0x800c3658L);

  public static final Value _800c37a4 = MEMORY.ref(4, 0x800c37a4L);

  public static final Value _800c37b8 = MEMORY.ref(1, 0x800c37b8L);

  public static final Value _800c37f4 = MEMORY.ref(4, 0x800c37f4L);
  public static final Value _800c37f8 = MEMORY.ref(4, 0x800c37f8L);

  public static final Value _800c38a8 = MEMORY.ref(1, 0x800c38a8L);

  public static final Value _800c38e4 = MEMORY.ref(4, 0x800c38e4L);
  public static final Value _800c38e8 = MEMORY.ref(4, 0x800c38e8L);

  public static final Value _800c3998 = MEMORY.ref(4, 0x800c3998L);

  public static final Value _800c39bb = MEMORY.ref(1, 0x800c39bbL);

  public static final Value _800c39e0 = MEMORY.ref(4, 0x800c39e0L);

  public static final Value _800c3a03 = MEMORY.ref(1, 0x800c3a03L);

  public static final Value _800c3a2c = MEMORY.ref(4, 0x800c3a2cL);
  public static final Value _800c3a30 = MEMORY.ref(4, 0x800c3a30L);
  public static final Value _800c3a34 = MEMORY.ref(4, 0x800c3a34L);

  public static final Value _800c3a38 = MEMORY.ref(4, 0x800c3a38L);
  public static final Value _800c3a3c = MEMORY.ref(4, 0x800c3a3cL);

  public static final Value spuMono_800c6666 = MEMORY.ref(2, 0x800c6666L);

  public static final Value _800c6688 = MEMORY.ref(4, 0x800c6688L);

  public static final Value timHeader_800c6748 = MEMORY.ref(4, 0x800c6748L);

  public static final Value _800ca734 = MEMORY.ref(4, 0x800ca734L);
}
