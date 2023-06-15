package legend.game;

import legend.core.gpu.RECT;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.game.sound.Instrument;
import legend.game.sound.InstrumentLayer10;
import legend.game.sound.InstrumentsSubfile;
import legend.game.sound.PatchList;
import legend.game.sound.PlayableSound0c;
import legend.game.sound.PlayingNote66;
import legend.game.sound.SequenceData124;
import legend.game.sound.SoundEnv44;
import legend.game.sound.Sshd;
import legend.game.sound.Sssq;
import legend.game.sound.SssqReader;
import legend.game.sound.Sssqish;
import legend.game.sound.VolumeRamp;
import legend.game.sound.WaveformList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SPU;

public final class Scus94491BpeSegment_800c {
  private Scus94491BpeSegment_800c() { }

  public static final Value _800c3410 = MEMORY.ref(4, 0x800c3410L);

  public static final RECT displayRect_800c34c8 = MEMORY.ref(8, 0x800c34c8L, RECT::new);
  /** Incremented with each frame - overflows to 1 */
  public static int PSDCNT_800c34d0;

  public static final MATRIX lightDirectionMatrix_800c34e8 = MEMORY.ref(4, 0x800c34e8L, MATRIX::new);
  public static final MATRIX lightColourMatrix_800c3508 = MEMORY.ref(4, 0x800c3508L, MATRIX::new);
  public static final MATRIX matrix_800c3528 = MEMORY.ref(4, 0x800c3528L, MATRIX::new);
  public static final MATRIX worldToScreenMatrix_800c3548 = MEMORY.ref(4, 0x800c3548L, MATRIX::new);
  public static final MATRIX identityMatrix_800c3568 = MEMORY.ref(4, 0x800c3568L, MATRIX::new);
  /** Includes aspect scale */
  public static final MATRIX identityAspectMatrix_800c3588 = MEMORY.ref(4, 0x800c3588L, MATRIX::new);

  public static final GsCOORDINATE2[] coord2s_800c35a8 = new GsCOORDINATE2[31];

  /** 0x990 bytes long, one per voice */
  public static final PlayingNote66[] playingNotes_800c3a40 = new PlayingNote66[SPU.voices.length];
  static {
    Arrays.setAll(playingNotes_800c3a40, i -> new PlayingNote66());
  }
  /** 0x5f4 bytes long */
  public static final Queue<PlayableSound0c> playableSounds_800c43d0 = new LinkedList<>();

  public static InstrumentsSubfile instruments_800c4aa8;
  public static Sssqish sssqish_800c4aa8;
  public static VolumeRamp volumeRamp_800c4ab0;
  public static WaveformList waveforms_800c4ab8;
  public static PatchList patchList_800c4abc;
  public static Sshd sshdPtr_800c4ac0;
  /** One per loaded sequence */
  public static final SequenceData124[] sequenceData_800c4ac8 = new SequenceData124[24];
  static {
    Arrays.setAll(sequenceData_800c4ac8, i -> new SequenceData124());
  }
  public static Runnable spuDmaCompleteCallback_800c6628;

  public static final SoundEnv44 soundEnv_800c6630 = new SoundEnv44();
  public static Instrument instrument_800c6674;
  public static InstrumentLayer10[] instrumentLayers_800c6678;
  public static InstrumentLayer10 instrumentLayer_800c6678;
  public static int instrumentLayerIndex_800c6678;
  public static SssqReader sssqReader_800c667c;
  public static Sssq.ChannelInfo sssqChannelInfo_800C6680;

  public static final Value timHeader_800c6748 = MEMORY.ref(4, 0x800c6748L);
}
