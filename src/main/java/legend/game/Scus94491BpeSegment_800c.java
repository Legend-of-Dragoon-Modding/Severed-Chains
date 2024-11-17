package legend.game;

import legend.core.gpu.Rect4i;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.game.sound.Instrument;
import legend.game.sound.InstrumentLayer10;
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
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static legend.core.GameEngine.SPU;

public final class Scus94491BpeSegment_800c {
  private Scus94491BpeSegment_800c() { }

  public static final Rect4i displayRect_800c34c8 = new Rect4i();
  /** Incremented with each frame - overflows to 1 */
  public static int PSDCNT_800c34d0;

  public static final Matrix3f lightDirectionMatrix_800c34e8 = new Matrix3f();
  public static final Matrix3f lightColourMatrix_800c3508 = new Matrix3f();

  public static final MV worldToScreenMatrix_800c3548 = new MV();
  public static final Matrix4f inverseWorldToScreenMatrix = new Matrix4f();
  /** Includes aspect scale */
  public static final MV identityAspectMatrix_800c3588 = new MV();

  public static final GsCOORDINATE2[] coord2s_800c35a8 = new GsCOORDINATE2[31];

  /** 0x990 bytes long, one per voice */
  public static final PlayingNote66[] playingNotes_800c3a40 = new PlayingNote66[SPU.voices.length];
  static {
    Arrays.setAll(playingNotes_800c3a40, i -> new PlayingNote66());
  }
  /** 0x5f4 bytes long */
  public static final Queue<PlayableSound0c> playableSounds_800c43d0 = new LinkedList<>();

  public static Sssqish sssqish_800c4aa8;
  public static VolumeRamp volumeRamp_800c4ab0;
  public static WaveformList waveforms_800c4ab8;
  public static Sshd sshdPtr_800c4ac0;
  /** One per loaded sequence */
  public static final SequenceData124[] sequenceData_800c4ac8 = new SequenceData124[24];
  static {
    Arrays.setAll(sequenceData_800c4ac8, i -> new SequenceData124());
  }

  public static final SoundEnv44 soundEnv_800c6630 = new SoundEnv44();
  public static Instrument instrument_800c6674;
  public static InstrumentLayer10[] instrumentLayers_800c6678;
  public static InstrumentLayer10 instrumentLayer_800c6678;
  public static int instrumentLayerIndex_800c6678;
  public static SssqReader sssqReader_800c667c;
  public static Sssq.ChannelInfo sssqChannelInfo_800C6680;
}
