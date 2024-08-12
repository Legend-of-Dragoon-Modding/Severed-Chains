package legend.game.combat.types;

import legend.game.unpacker.FileData;

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
  public int framesPostFailure_0e;
  public int overlayStartingFrameOffset_0f;

  public AdditionHitProperties10(final int flags, final int totalFrames, final int overlayHitFrameOffset, final int totalSuccessFrames, final int damageMultiplier, final int spValue, final int audioFile, final int isFinalHit, final int panDistance_08, final int _09, final int _0a, final int hitDistanceFromTarget, final int framesToHitPosition, final int _0d, final int framesPostFailure, final int overlayStartingFrameOffset) {
    this.flags_00 = flags;
    this.totalFrames_01 = totalFrames;
    this.overlayHitFrameOffset_02 = overlayHitFrameOffset;
    this.totalSuccessFrames_03 = totalSuccessFrames;
    this.damageMultiplier_04 = damageMultiplier;
    this.spValue_05 = spValue;
    this.audioFile_06 = audioFile;
    this.isFinalHit_07 = isFinalHit;
    this.panDistance_08 = panDistance_08;
    this._09 = _09;
    this._0a = _0a;
    this.hitDistanceFromTarget_0b = hitDistanceFromTarget;
    this.framesToHitPosition_0c = framesToHitPosition;
    this._0d = _0d;
    this.framesPostFailure_0e = framesPostFailure;
    this.overlayStartingFrameOffset_0f = overlayStartingFrameOffset;
  }

  public static AdditionHitProperties10 fromFile(final int additionIndex, final int additionHitIndex, final FileData data) {
    final int temporaryDistance = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 8);

    final int flags = data.readUByte((additionIndex * 128) + (additionHitIndex * 16));
    final int totalFrames = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 1);
    final int overlayHitFrameOffset = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 2);
    final int totalSuccessFrames = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 3);
    final int damageMultiplier = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 4);
    final int spValue = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 5);
    final int audioFile = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 6);
    final int isFinalHit = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 7);
    final int panDistance = temporaryDistance > 127 ? temporaryDistance - 255 : temporaryDistance;
    final int unknown_09 = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 9);
    final int unknown_0a = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 10);
    final int hitDistance = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 11);
    final int frameToHit = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 12);
    final int unknown_0d = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 13);
    final int framesPostFailure = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 14);
    final int overlayFrameOffset = data.readUByte((additionIndex * 128) + (additionHitIndex * 16) + 15);

    return new AdditionHitProperties10(flags, totalFrames, overlayHitFrameOffset, totalSuccessFrames, damageMultiplier, spValue, audioFile, isFinalHit, panDistance, unknown_09, unknown_0a, hitDistance, frameToHit, unknown_0d, framesPostFailure, overlayFrameOffset);
  }

  public int getHitProperty(final int propertyIndex) {
    return switch(propertyIndex) {
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
      case 14 -> this.framesPostFailure_0e;
      case 15 -> this.overlayStartingFrameOffset_0f;
      default -> throw new IllegalArgumentException("Invalid property index " + propertyIndex);
    };
  }
}
