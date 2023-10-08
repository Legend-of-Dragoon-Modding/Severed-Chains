package legend.game.sound;

import legend.game.combat.bent.BattleEntity27c;

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

  public BattleEntity27c bent_04;
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
}
