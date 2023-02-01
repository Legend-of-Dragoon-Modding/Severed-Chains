package legend.game;

import legend.core.gpu.Box;
import legend.core.gpu.DISPENV;
import legend.core.gpu.DRAWENV;
import legend.core.gpu.RECT;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedShortRef;
import legend.core.spu.Spu;
import legend.game.sound.PatchList;
import legend.game.sound.PlayableSound0c;
import legend.game.sound.SequenceData124;
import legend.game.sound.SpuStruct44;
import legend.game.sound.SpuStruct66;
import legend.game.sound.Sshd;
import legend.game.sound.InstrumentLayer10;
import legend.game.sound.Sssq;
import legend.game.sound.SssqReader;
import legend.game.sound.Sssqish;
import legend.game.sound.Instrument;
import legend.game.sound.InstrumentsSubfile;
import legend.game.sound.VolumeRamp;
import legend.game.sound.WaveformList;
import legend.game.types.GsOffsetType;

import java.util.Arrays;

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
  public static final SpuStruct66[] _800c3a40 = new SpuStruct66[24];
  static {
    Arrays.setAll(_800c3a40, i -> new SpuStruct66());
  }
  /** 0x5f4 bytes long */
  public static final PlayableSound0c[] playableSounds_800c43d0 = new PlayableSound0c[127];
  static {
    Arrays.setAll(playableSounds_800c43d0, i -> new PlayableSound0c());
  }

  public static InstrumentsSubfile instruments_800c4aa8;
  public static Sssqish sssqish_800c4aa8;
  public static VolumeRamp volumeRamp_800c4ab0;
  public static WaveformList waveforms_800c4ab8;
  public static PatchList patchList_800c4abc;
  public static Sshd sshdPtr_800c4ac0;
  public static final Pointer<Spu> voicePtr_800c4ac4 = MEMORY.ref(4, 0x800c4ac4L, Pointer.deferred(4, ref -> {throw new RuntimeException("Can't instantiate");}));
  /** One per voice */
  public static final SequenceData124[] sequenceData_800c4ac8 = new SequenceData124[24];
  static {
    Arrays.setAll(sequenceData_800c4ac8, i -> new SequenceData124());
  }
  public static Runnable spuDmaCompleteCallback_800c6628;

  public static final SpuStruct44 _800c6630 = new SpuStruct44();
  public static Instrument instrument_800c6674;
  public static InstrumentLayer10[] instrumentLayers_800c6678;
  public static InstrumentLayer10 instrumentLayer_800c6678;
  public static int instrumentLayerIndex_800c6678;
  public static SssqReader sssqReader_800c667c;
  public static Sssq.ChannelInfo sssqChannelInfo_800C6680;

  public static final Value timHeader_800c6748 = MEMORY.ref(4, 0x800c6748L);
}
