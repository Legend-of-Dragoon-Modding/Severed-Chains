package legend.game.additions;

/** Same as AdditionHitProperties20 in BattlePreloadedEntities_18cb0, except fields are bytes */
public class AdditionHitProperties10 {
  private static final AdditionSound NO_SOUND = new AdditionSound(-1, 0);

  /** Always 0xc0 except 0xe0 for first hit of ultimate addition. Appears to be unused. */
  public int flags_00;
  /**
   * The total number of frames for this hit
   * <ul>
   *   <li>Usually {@link #overlayHitFrameOffset_02} + {@link #totalSuccessFrames_03} + 1</li>
   *   <li>Increasing it past this amount will increase the amount of time after the button press</li>
   *   <li>Decreasing it below {@link #overlayHitFrameOffset_02} + {@link #totalSuccessFrames_03} + 1 will have no effect, it will still wait that long before continuing</li>
   *   <li>If the animation is too short, the player will stay on the last frame of the animation until the hit finishes</li>
   * </ul>
   */
  public int totalFrames_01;
  /** The number of frames until the player has to press the addition button. Higher values make the button press later. */
  public int overlayHitFrameOffset_02;
  /** The number of frames the player has to press the addition button before it's considered a miss */
  public int totalSuccessFrames_03;
  public int damageMultiplier_04;
  public int sp_05;
  public int audioFile_06;
  public int isFinalHit_07;
  public int cameraMovementX_08;
  public int cameraMovementZ_09;
  public int cameraMovementTicks_0a;
  /** The amount to push the enemy back on hit */
  public int hitDistanceFromTarget_0b;
  /** Controls how long it takes for the player to get to the enemy. Lower values move faster, higher values move slower. Retail additions have values for all hits, but it only seems to have an effect on the first hit. */
  public int framesToHitPosition_0c;
  public int _0d; // always 32 (except for a few for Haschel), not used in PCS
  /** If non-zero, this animation will be used if this hit is missed */
  public int additionFailAnimationIndex_0e;
  public int overlayStartingFrameOffset_0f;

  public final AdditionSound[] sounds;

  public AdditionHitProperties10(final int flags, final int totalFrames, final int overlayHitFrameOffset, final int totalSuccessFrames, final int damageMultiplier, final int sp, final int audioFile, final int isFinalHit, final int cameraMovementX, final int cameraMovementZ, final int cameraMovementTicks, final int hitDistanceFromTarget, final int framesToHitPosition, final int _0d, final int additionFailAnimationIndex, final int overlayStartingFrameOffset, final AdditionSound... sounds) {
    this.flags_00 = flags;
    this.totalFrames_01 = totalFrames;
    this.overlayHitFrameOffset_02 = overlayHitFrameOffset;
    this.totalSuccessFrames_03 = totalSuccessFrames;
    this.damageMultiplier_04 = damageMultiplier;
    this.sp_05 = sp;
    this.audioFile_06 = audioFile;
    this.isFinalHit_07 = isFinalHit;
    this.cameraMovementX_08 = cameraMovementX;
    this.cameraMovementZ_09 = cameraMovementZ;
    this.cameraMovementTicks_0a = cameraMovementTicks;
    this.hitDistanceFromTarget_0b = hitDistanceFromTarget;
    this.framesToHitPosition_0c = framesToHitPosition;
    this._0d = _0d;
    this.additionFailAnimationIndex_0e = additionFailAnimationIndex;
    this.overlayStartingFrameOffset_0f = overlayStartingFrameOffset;
    this.sounds = sounds;
  }

  public int get(final int index) {
    if(index >= 16) {
      final int soundIndex = (index - 16) / 2;
      final AdditionSound sound;

      if(soundIndex < this.sounds.length) {
        sound = this.sounds[soundIndex];
      } else {
        sound = NO_SOUND;
      }

      return switch(index & 0x1) {
        case 0 -> sound.soundIndex;
        case 1 -> sound.initialDelay;
        default -> throw new IllegalStateException("Unexpected value: " + (index & 0x1));
      };
    }

    return switch(index) {
      case 0 -> this.flags_00;
      case 1 -> this.totalFrames_01;
      case 2 -> this.overlayHitFrameOffset_02;
      case 3 -> this.totalSuccessFrames_03;
      case 4 -> this.damageMultiplier_04;
      case 5 -> this.sp_05;
      case 6 -> this.audioFile_06;
      case 7 -> this.isFinalHit_07;
      case 8 -> this.cameraMovementX_08;
      case 9 -> this.cameraMovementZ_09;
      case 10 -> this.cameraMovementTicks_0a;
      case 11 -> this.hitDistanceFromTarget_0b;
      case 12 -> this.framesToHitPosition_0c;
      case 13 -> this._0d;
      case 14 -> this.additionFailAnimationIndex_0e;
      case 15 -> this.overlayStartingFrameOffset_0f;
      default -> throw new IllegalArgumentException("Invalid property index " + index);
    };
  }
}
