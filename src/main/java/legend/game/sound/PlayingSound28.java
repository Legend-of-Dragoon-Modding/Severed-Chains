package legend.game.sound;

import legend.game.combat.types.BattleObject27c;

public class PlayingSound28 {
  /**
   * <ol start="0">
   *   <li>unused</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>sounds?</li>
   *   <li>?</li>
   * </ul>
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
  public int _20;
  /** short */
  public int _22;
  /** short */
  public int _24;

  public PlayingSound28 set(final PlayingSound28 other) {
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
    this._20 = other._20;
    this._22 = other._22;
    this._24 = other._24;
    return this;
  }
}
