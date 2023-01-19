package legend.game;

import legend.core.gpu.Box;
import legend.core.gpu.DISPENV;
import legend.core.gpu.DRAWENV;
import legend.core.gpu.RECT;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedShortRef;
import legend.core.spu.Spu;
import legend.game.types.GsOffsetType;
import legend.game.types.PlayableSoundStruct;
import legend.game.types.SpuStruct124;
import legend.game.types.SpuStruct44;
import legend.game.types.SpuStruct66;
import legend.game.types.SshdFile;
import legend.game.types.SshdStruct10;

import static legend.core.GameEngine.MEMORY;

public final class Scus94491BpeSegment_800c {
  private Scus94491BpeSegment_800c() { }

  public static final Value _800c3410 = MEMORY.ref(4, 0x800c3410L);

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
  public static int PSDCNT_800c34d0;
  /** Double buffer index */
  public static final UnsignedShortRef PSDIDX_800c34d4 = MEMORY.ref(2, 0x800c34d4L, UnsignedShortRef::new);
  public static final EnumRef<GsOffsetType> doubleBufferOffsetMode_800c34d6 = MEMORY.ref(2, 0x800c34d6L, EnumRef.of(GsOffsetType::getValue, GsOffsetType.values()));
  public static final Value _800c34d8 = MEMORY.ref(4, 0x800c34d8L);
  public static final Value lightMode_800c34dc = MEMORY.ref(4, 0x800c34dcL);
  public static final Value _800c34e0 = MEMORY.ref(4, 0x800c34e0L);

  public static final MATRIX lightDirectionMatrix_800c34e8 = MEMORY.ref(4, 0x800c34e8L, MATRIX::new);
  public static final MATRIX lightColourMatrix_800c3508 = MEMORY.ref(4, 0x800c3508L, MATRIX::new);
  public static final MATRIX matrix_800c3528 = MEMORY.ref(4, 0x800c3528L, MATRIX::new);
  public static final MATRIX worldToScreenMatrix_800c3548 = MEMORY.ref(4, 0x800c3548L, MATRIX::new);
  public static final MATRIX identityMatrix_800c3568 = MEMORY.ref(4, 0x800c3568L, MATRIX::new);
  /** Includes aspect scale */
  public static final MATRIX identityAspectMatrix_800c3588 = MEMORY.ref(4, 0x800c3588L, MATRIX::new);

  public static final GsCOORDINATE2[] coord2s_800c35a8 = new GsCOORDINATE2[31];

  /** 0x990 bytes long, I think these map to voices, not channels */
  public static final ArrayRef<SpuStruct66> _800c3a40 = MEMORY.ref(2, 0x800c3a40L, ArrayRef.of(SpuStruct66.class, 24, 0x66, SpuStruct66::new));
  /** 0x5f4 bytes long */
  public static final ArrayRef<PlayableSoundStruct> playableSoundPtrArr_800c43d0 = MEMORY.ref(4, 0x800c43d0L, ArrayRef.of(PlayableSoundStruct.class, 127, 0xc, PlayableSoundStruct::new));

  /** NOTE: this pointer can be misaligned, hence being a Value */
  public static final Value sssqPtr_800c4aa4 = MEMORY.ref(4, 0x800c4aa4L);
  public static final Value _800c4aa8 = MEMORY.ref(4, 0x800c4aa8L);
  public static final Value _800c4aac = MEMORY.ref(4, 0x800c4aacL);
  public static final Value _800c4ab0 = MEMORY.ref(4, 0x800c4ab0L);
  public static final Value _800c4ab4 = MEMORY.ref(4, 0x800c4ab4L);
  public static final Value _800c4ab8 = MEMORY.ref(4, 0x800c4ab8L);
  public static final Value _800c4abc = MEMORY.ref(4, 0x800c4abcL);
  public static final Pointer<SshdFile> sshdPtr_800c4ac0 = MEMORY.ref(4, 0x800c4ac0L, Pointer.deferred(4, SshdFile::new));
  public static final Pointer<Spu> voicePtr_800c4ac4 = MEMORY.ref(4, 0x800c4ac4L, Pointer.deferred(4, ref -> {throw new RuntimeException("Can't instantiate");}));
  public static final ArrayRef<SpuStruct124> _800c4ac8 = MEMORY.ref(4, 0x800c4ac8L, ArrayRef.of(SpuStruct124.class, 24, 0x124, SpuStruct124::new));
  public static Runnable spuDmaCompleteCallback_800c6628;

  public static final SpuStruct44 _800c6630 = MEMORY.ref(4, 0x800c6630L, SpuStruct44::new);
  public static final Value _800c6674 = MEMORY.ref(4, 0x800c6674L);
  public static final Pointer<SshdStruct10> sshd10Ptr_800c6678 = MEMORY.ref(4, 0x800c6678L, Pointer.deferred(1, SshdStruct10::new));
  public static final Value sssqPtr_800c667c = MEMORY.ref(4, 0x800c667cL);
  public static final Value sssqDataPointer_800c6680 = MEMORY.ref(4, 0x800c6680L);

  public static final Value timHeader_800c6748 = MEMORY.ref(4, 0x800c6748L);
}
