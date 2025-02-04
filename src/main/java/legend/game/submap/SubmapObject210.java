package legend.game.submap;

import legend.core.opengl.Texture;
import legend.game.types.Model124;
import org.joml.Vector3f;

import static legend.game.submap.AttachedSobjEffect.AttachedSobjEffectData40;

public class SubmapObject210 {
  public final Model124 model_00;

  public boolean hidden_128;
  public boolean disableAnimation_12a;
  /** We need to hold the "animation finished" state for long enough for the script engine to tick and read it, so we set it to N frames and count back down to 0 */
  public int animationFinishedFrames_12c;
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
  public int collidedPrimitiveIndex_16c;
  /**
   * Forced movement types
   * <ul>
   *   <li>0 - none</li>
   *   <li>1 - linear</li>
   *   <li>2 - non-linear</li>
   * </ul>
   */
  public int movementType_170;
  /** Forced movement geometry collision detection */
  public int ignoreCollision_172;
  /** Used to restore ignoreCollision_172 after SMap::scriptSobjMoveAlongArc2 */
  public int ignoreCollisionMemory_174;

  public final Vector3f interpMovementStart = new Vector3f();
  public final Vector3f interpMovementDest = new Vector3f();
  public int interpMovementTicks;
  public int interpMovementTicksTotal;
  public int lastMovementTick = Integer.MIN_VALUE;
  public float interpRotationStartX;
  public float interpRotationStartY;
  public float interpRotationStartZ;
  public float interpRotationDestX;
  public float interpRotationDestY;
  public float interpRotationDestZ;
  public int interpRotationTicksX;
  public int interpRotationTicksY;
  public int interpRotationTicksZ;
  public int interpRotationTicksTotalX;
  public int interpRotationTicksTotalY;
  public int interpRotationTicksTotalZ;
  public long lastRotationTickX = Integer.MIN_VALUE;
  public long lastRotationTickY = Integer.MIN_VALUE;
  public long lastRotationTickZ = Integer.MIN_VALUE;

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
  public final AttachedSobjEffectData40 attachedEffectData_1d0 = new AttachedSobjEffectData40();

  public Texture texture;

  public SubmapObject210(final String name) {
    this.model_00 = new Model124(name);
  }

  public void finishInterpolatedMovement() {
    if(this.interpMovementTicksTotal != 0) {
      this.model_00.coord2_14.coord.transfer.set(this.interpMovementDest);
      this.interpMovementTicksTotal = 0;
      this.lastMovementTick = Integer.MIN_VALUE;
    }
  }

  public void finishInterpolatedRotationX() {
    if(this.interpRotationTicksTotalX != 0) {
      this.model_00.coord2_14.transforms.rotate.x = this.interpRotationDestX;
      this.interpRotationTicksTotalX = 0;
      this.lastRotationTickX = Integer.MIN_VALUE;
    }
  }

  public void finishInterpolatedRotationY() {
    if(this.interpRotationTicksTotalY != 0) {
      this.model_00.coord2_14.transforms.rotate.y = this.interpRotationDestY;
      this.interpRotationTicksTotalY = 0;
      this.lastRotationTickY = Integer.MIN_VALUE;
    }
  }

  public void finishInterpolatedRotationZ() {
    if(this.interpRotationTicksTotalZ != 0) {
      this.model_00.coord2_14.transforms.rotate.z = this.interpRotationDestZ;
      this.interpRotationTicksTotalZ = 0;
      this.lastRotationTickZ = Integer.MIN_VALUE;
    }
  }

  @Override
  public String toString() {
    return this.model_00.toString();
  }
}
