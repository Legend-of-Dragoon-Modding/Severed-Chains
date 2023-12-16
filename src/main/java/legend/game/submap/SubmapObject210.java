package legend.game.submap;

import legend.game.types.Model124;
import org.joml.Vector3f;

public class SubmapObject210 {
  public final Model124 model_00;

  public int s_128;
  public boolean disableAnimation_12a;
  public boolean animationFinished_12c;
  public int sobjIndex_12e;
  /** The script index of this sobj */
  public int sobjIndex_130;
  public int animIndex_132;
  /** Forced movement Y step */
  public float movementStepY_134;
  /** Forced movement destination */
  public final Vector3f movementDestination_138 = new Vector3f();
  /** Number of frames for forced movement */
  public int movementTicks_144;
  /** Forced movement step */
  public final Vector3f movementStep_148 = new Vector3f();
//  /** Same as {@link #movementStep_148} but .16 */
//  public final Vector3f movementStep12_154 = new Vector3f();
//  /** The total distance moved (.16) */
//  public final Vector3f movementDistanceMoved12_160 = new Vector3f();
  public int ui_16c;
  public int us_170;
  public int s_172;
  public int s_174;

  /** Only one sobj may have this value set at a time */
  public boolean cameraAttached_178;

  /** The amount to rotate this sobj by each frame for {@link SubmapObject210#rotationFrames_188} frames */
  public final Vector3f rotationAmount_17c = new Vector3f();
  /** Number of frames to apply rotation for */
  public int rotationFrames_188;
  public float movementStepAccelerationY_18c;
  /**
   * Example flags for the player sobj - 0x120_0001
   *
   * Collision flags are in pairs. Each collider flag collides with the following collidee flag.
   *
   * <ul>
   *   <li>0x1 - player</li>
   *   <li>0x10_0000 - player can collide with</li>
   *   <li>0x20_0000 - collider something</li>
   *   <li>0x40_0000 - collidee something</li>
   *   <li>0x80_0000 - collider something</li>
   *   <li>0x100_0000 - collidee something</li>
   *   <li>0x200_0000 - collider something</li>
   *   <li>0x400_0000 - collidee something</li>
   *   <li>0x800_0000 - collider something</li>
   *   <li>0x1000_0000 - collidee something</li>
   *   <li>0x2000_0000 - switch to idle animation when current animation is finished</li>
   *   <li>0x4000_0000 - disable animation when current animation is finished</li>
   *   <li>0x8000_0000 - doesn't seem to be set anywhere, looks like it disables forced movement or something like that
   *   </li>
   * </ul>
   */
  public int flags_190;
  /** The yellow &lt;!&gt; */
  public boolean showAlertIndicator_194;
  public int alertIndicatorOffsetY_198;
  /** The sobj that this sobj is currently collided with (unknown how this differs from _1a8) */
  public int collidedWithSobjIndex_19c;
  public int collisionSizeHorizontal_1a0;
  public int collisionSizeVertical_1a4;
  /** The sobj that this sobj is currently collided with (unknown how this differs from _19c) */
  public int collidedWithSobjIndex_1a8;
  public int collisionSizeHorizontal_1ac;
  public int collisionSizeVertical_1b0;
  /** Not exactly sure what the point of this is... it extends the collision box in the direction the sobj is facing */
  public int collisionReach_1b4;
  public int playerCollisionSizeHorizontal_1b8;
  public int playerCollisionSizeVertical_1bc;
  /** Not exactly sure what the point of this is... it extends the collision box in the direction the sobj is facing */
  public int playerCollisionReach_1c0;
  public boolean flatLightingEnabled_1c4;
  public int flatLightRed_1c5;
  public int flatLightGreen_1c6;
  public int flatLightBlue_1c7;
  public boolean ambientColourEnabled_1c8;

  public final Vector3f ambientColour_1ca = new Vector3f();
  public final BigSubStruct _1d0 = new BigSubStruct();

  public SubmapObject210(final String name) {
    this.model_00 = new Model124(name);
  }

  @Override
  public String toString() {
    return this.model_00.toString();
  }
}
