package legend.game.combat.types;

/** Same as AdditionHitProperties20 in BattlePreloadedEntities_18cb0, except fields are bytes */
public class AdditionHitProperties10 {
  public int flags_00;
  public int totalFrames_01;
  public int overlayHitFrameOffset_02;
  public int totalSuccessFrames_03;
  public int damageMultiplier_04;
  public int spValue_05;
  public int audioFile_06;
  public int isFinalHit_07;
  public int panDistance_08; // related to camera or voice? index into array?
  public int _09; // related to camera or voice? index into array?
  public int _0a; // related to camera? index into array?
  public int hitDistanceFromTarget_0b;
  public int framesToHitPosition_0c;
  public int _0d; // always 32 (except for a few for Haschel), could be length of properties array
  /** Used in player_combat_script as an index into an animation table, 18 -> 24, 19 -> 25, all other values are 0 */
  public int _0e;
  public int overlayStartingFrameOffset_0f;

  public AdditionHitProperties10(final int flags, final int totalFrames, final int overlayHitFrameOffset, final int totalSuccessFrames, final int damageMultiplier, final int spValue, final int audioFile, final int isFinalHit, final int panDistance, final int _09, final int _0a, final int hitDistanceFromTarget, final int framesToHitPosition, final int _0d, final int _0e, final int overlayStartingFrameOffset) {
    this.flags_00 = flags;
    this.totalFrames_01 = totalFrames;
    this.overlayHitFrameOffset_02 = overlayHitFrameOffset;
    this.totalSuccessFrames_03 = totalSuccessFrames;
    this.damageMultiplier_04 = damageMultiplier;
    this.spValue_05 = spValue;
    this.audioFile_06 = audioFile;
    this.isFinalHit_07 = isFinalHit;
    this.panDistance_08 = panDistance;
    this._09 = _09;
    this._0a = _0a;
    this.hitDistanceFromTarget_0b = hitDistanceFromTarget;
    this.framesToHitPosition_0c = framesToHitPosition;
    this._0d = _0d;
    this._0e = _0e;
    this.overlayStartingFrameOffset_0f = overlayStartingFrameOffset;
  }

  public int get(final int index) {
    return switch(index) {
      case 0 -> this.flags_00;
      case 1 -> this.totalFrames_01;
      case 2 -> this.overlayHitFrameOffset_02;
      case 3 -> this.totalSuccessFrames_03;
      case 4 -> this.damageMultiplier_04;
      case 5 -> this.spValue_05;
      case 6 -> this.audioFile_06;
      case 7 -> this.isFinalHit_07;
      case 8 -> this.panDistance_08;
      case 9 -> this._09;
      case 10 -> this._0a;
      case 11 -> this.hitDistanceFromTarget_0b;
      case 12 -> this.framesToHitPosition_0c;
      case 13 -> this._0d;
      case 14 -> this._0e;
      case 15 -> this.overlayStartingFrameOffset_0f;
      default -> throw new IllegalArgumentException("Invalid property index " + index);
    };
  }
}
