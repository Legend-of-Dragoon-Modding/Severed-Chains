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
  public int _08; // related to camera or voice? index into array?
  public int _09; // related to camera or voice? index into array?
  public int _0a; // related to camera? index into array?
  public int hitDistanceFromTarget_0b;
  public int framesToHitPosition_0c;
  public int _0d; // always 32 (except for a few for Haschel), could be length of properties array
  public int framesPostFailure_0e;
  public int overlayStartingFrameOffset_0f;

  public AdditionHitProperties10() {
    for(int i = 0; i < 16; i++) {
      this.set(i, 0);
    }
  }

  public AdditionHitProperties10(final int flags, final int totalFrames, final int overlayHitFrameOffset, final int totalSuccessFrames, final int damageMultiplier, final int spValue, final int audioFile, final int isFinalHit, final int _08, final int _09, final int _0a, final int hitDistanceFromTarget, final int framesToHitPosition, final int _0d, final int framesPostFailure, final int overlayStartingFrameOffset) {
    this.flags_00 = flags;
    this.totalFrames_01 = totalFrames;
    this.overlayHitFrameOffset_02 = overlayHitFrameOffset;
    this.totalSuccessFrames_03 = totalSuccessFrames;
    this.damageMultiplier_04 = damageMultiplier;
    this.spValue_05 = spValue;
    this.audioFile_06 = audioFile;
    this.isFinalHit_07 = isFinalHit;
    this._08 = _08;
    this._09 = _09;
    this._0a = _0a;
    this.hitDistanceFromTarget_0b = hitDistanceFromTarget;
    this.framesToHitPosition_0c = framesToHitPosition;
    this._0d = _0d;
    this.framesPostFailure_0e = framesPostFailure;
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
      case 8 -> this._08;
      case 9 -> this._09;
      case 10 -> this._0a;
      case 11 -> this.hitDistanceFromTarget_0b;
      case 12 -> this.framesToHitPosition_0c;
      case 13 -> this._0d;
      case 14 -> this.framesPostFailure_0e;
      case 15 -> this.overlayStartingFrameOffset_0f;
      default -> throw new IllegalArgumentException("Invalid property index " + index);
    };
  }

  public int set(final int propertyIndex, final int value) {
    return switch(propertyIndex) {
      case 0 -> this.flags_00 = value;
      case 1 -> this.totalFrames_01 = value;
      case 2 -> this.overlayHitFrameOffset_02 = value;
      case 3 -> this.totalSuccessFrames_03 = value;
      case 4 -> this.damageMultiplier_04 = value;
      case 5 -> this.spValue_05 = value;
      case 6 -> this.audioFile_06 = value;
      case 7 -> this.isFinalHit_07 = value;
      case 8 -> this._08 = value;
      case 9 -> this._09 = value;
      case 10 -> this._0a = value;
      case 11 -> this.hitDistanceFromTarget_0b = value;
      case 12 -> this.framesToHitPosition_0c = value;
      case 13 -> this._0d = value;
      case 14 -> this.framesPostFailure_0e = value;
      case 15 -> this.overlayStartingFrameOffset_0f = value;
      default -> throw new IllegalArgumentException("Invalid property index " + propertyIndex);
    };
  }
}
