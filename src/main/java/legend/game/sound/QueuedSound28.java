package legend.game.sound;

import legend.game.combat.bobj.BattleObject27c;

public class QueuedSound28 {
  /**
   * <ol start="0">
   *   <li>unused</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>sounds?</li>
   *   <li>?</li>
   * </ol>
   * (ubyte)
   */
  public int type_00;

  public BattleObject27c bobj_04;
  public SoundFile soundFile_08;
  public int soundIndex_0c;
  /** I think if this has flag 0x80 set it enables reverb? I don't think it's ever used? (short) */
  public PlayableSound0c playableSound_10;
  /** short */
  public int patchIndex_12;
  /** short */
  public int sequenceIndex_14;
  /** short */
  public int pitchShiftVolRight_16;
  /** short */
  public int pitchShiftVolLeft_18;
  /** short */
  public int pitch_1a;
  public int _1c;
  /** short */
  public int repeatDelayTotal_20;
  /** short */
  public int repeatDelayCurrent_22;
  /** short */
  public int initialDelay_24;

  public QueuedSound28 set(final QueuedSound28 other) {
    this.type_00 = other.type_00;
    this.bobj_04 = other.bobj_04;
    this.soundFile_08 = other.soundFile_08;
    this.soundIndex_0c = other.soundIndex_0c;
    this.playableSound_10 = other.playableSound_10;
    this.patchIndex_12 = other.patchIndex_12;
    this.sequenceIndex_14 = other.sequenceIndex_14;
    this.pitchShiftVolRight_16 = other.pitchShiftVolRight_16;
    this.pitchShiftVolLeft_18 = other.pitchShiftVolLeft_18;
    this.pitch_1a = other.pitch_1a;
    this._1c = other._1c;
    this.repeatDelayTotal_20 = other.repeatDelayTotal_20;
    this.repeatDelayCurrent_22 = other.repeatDelayCurrent_22;
    this.initialDelay_24 = other.initialDelay_24;
    return this;
  }
}
