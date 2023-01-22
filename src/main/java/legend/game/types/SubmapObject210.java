package legend.game.types;

import legend.core.gte.VECTOR;

public class SubmapObject210 {
  public final Model124 model_00 = new Model124();

  public int s_128;
  public int us_12a;
  public int us_12c;
  public int sobjIndex_12e;
  /** The script index of this sobj */
  public int sobjIndex_130;
  public int animIndex_132;
  public int s_134;

  public final VECTOR vec_138 = new VECTOR();
  public int i_144;
  public final VECTOR vec_148 = new VECTOR();
  public final VECTOR vec_154 = new VECTOR();
  public final VECTOR vec_160 = new VECTOR();
  public int ui_16c;
  public int us_170;
  public int s_172;
  public int s_174;

  public int s_178;

  /** The amount to rotate this sobj by each frame for {@link SubmapObject210#rotationFrames_188} frames */
  public final VECTOR rotationAmount_17c = new VECTOR();
  /** Number of frames to apply rotation for */
  public int rotationFrames_188;
  public int ui_18c;
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
   *   <li>0x2000_0000 - ?</li>
   *   <li>0x4000_0000 - ?</li>
   *   <li>0x8000_0000 - ?</li>
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

  public int ambientRed_1ca;
  public int ambientGreen_1cc;
  public int ambientBlue_1ce;
  public final BigSubStruct _1d0 = new BigSubStruct();
}
