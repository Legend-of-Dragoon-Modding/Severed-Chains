package legend.game.combat.environment;

import legend.core.MathHelper;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.memory.types.ComponentFunction;
import legend.core.memory.types.FloatRef;
import legend.game.combat.types.BattleObject;
import legend.game.types.GsRVIEW2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.annotation.Nullable;

import static legend.core.GameEngine.GTE;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2L;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.combat.Battle.ZERO;

public class BattleCamera {
  private static final Logger LOGGER = LogManager.getFormatterLogger(BattleCamera.class);
  private static final Marker CAMERA = MarkerManager.getMarker("CAMERA");

  public static final int UPDATE_VIEWPOINT = 0x1;
  public static final int UPDATE_REFPOINT = 0x2;

  public final GsRVIEW2 rview2_00 = new GsRVIEW2();

  // Refpoint stuff
  /** 8-bit fixed-point */
  public final Vector3f refpointBaseTranslation_20 = new Vector3f();
  /** 8-bit fixed-point */
  public float refpointDeltaMagnitude_2c;
  /** For vec_60/refpointDeltaMagnitude_2c, 8-bit fixed-point */
  public float stepZ_30;

  /** 8-bit fixed-point */
  public float refpointAngleX_38;
  /** For refpointBaseTranslation_20/refpointAngleX_38, 8-bit fixed-point */
  public float stepX_3c;
  /** For vec_60/stepZ_30, 8-bit fixed-point */
  public float stepZAcceleration_40;
  /** 8-bit fixed-point */
  public float refpointAngleY_44;
  /** For refpointBaseTranslation_20/refpointAngleY_44, 8-bit fixed-point */
  public float stepY_48;

  /** For refpointBaseTranslation_20/refpointDeltaMagnitude_2c, 8-bit fixed-point */
  public float stepZ_54;

  /** TODO should this be a float? */
  public int refpointTicksRemaining_5c;
  /** XY rotation, Z magnitude, 8-bit fixed-point */
  public final Vector3f vec_60 = new Vector3f();
  /** For vec_60 */
  public float stepZ_6c;
  /** for vec_60/stepZ_6c */
  public float stepZAcceleration_70;
  public final Vector3f refpointTargetTranslation_74 = new Vector3f();
  public BattleObject refpointBobj_80;

  public int refpointCallbackIndex_88;
  //

  // Viewpoint stuff
  /** 8-bit fixed-point */
  public final Vector3f viewpointBaseTranslation_94 = new Vector3f();
  /** 8-bit fixed-point */
  public float viewpointDeltaMagnitude_a0;
  /** For vec_d4/viewpointDeltaMagnitude_a0, 8-bit fixed-point */
  public float stepZ_a4; // camera rotation Z step?

  /** 8-bit fixed-point */
  public float viewpointAngleX_ac;
  /** For viewpointBaseTranslation_94/viewpointAngleX_ac, 8-bit fixed-point */
  public float stepX_b0;
  /** For vec_d4/stepZ_a4, 8-bit fixed-point */
  public float stepZAcceleration_b4; // camera rotation Z step acceleration?
  /** 8-bit fixed-point */
  public float viewpointAngleY_b8;
  /** For viewpointBaseTranslation_94/viewpointAngleY_b8, 8-bit fixed-point */
  public float stepY_bc;

  /** For viewpointBaseTranslation_94/viewpointDeltaMagnitude_a0, 8-bit fixed-point */
  public float stepZ_c8;

  /** TODO should this be a float? */
  public int viewpointTicksRemaining_d0;
  /** XY rotation, Z magnitude, 8-bit fixed-point */
  public final Vector3f vec_d4 = new Vector3f();  // camera rotation?
  /** For vec_d4, 8-bit fixed-point */
  public float stepZ_e0;
  /** For vec_d4/stepZ_e0, 8-bit fixed-point */
  public float stepZAcceleration_e4;
  /** 8-bit fixed-point */
  public final Vector3f viewpointTargetTranslation_e8 = new Vector3f();
  public BattleObject viewpointBobj_f4;

  public int viewpointCallbackIndex_fc;
  //

  public float projectionPlaneDistance_100;
  public float newProjectionPlaneDistance_104;
  public int projectionPlaneChangeFrames_108;
  public float projectionPlaneDistanceStep_10c;
  public float projectionPlaneDistanceStepAcceleration_110;
  public int projectionPlaneMovementDirection_114;
  /** ubyte*/
  public boolean projectionPlaneChanging_118;

  /**
   * <ul>
   *   <li>0x1 - {@link BattleCamera#UPDATE_VIEWPOINT}</li>
   *   <li>0x2 - {@link BattleCamera#UPDATE_REFPOINT}</li>
   * </ul>
   */
  public int flags_11c;
  /** ubyte*/
  public int viewpointCallbackIndex_120;
  /** ubyte*/
  public int refpointCallbackIndex_121;
  /** ubyte*/
  public boolean viewpointMoving_122;
  /** ubyte */
  public boolean refpointMoving_123;

  private final MV cameraTransformMatrix_800c6798 = new MV();

  public final Vector2f screenOffset_800c67bc = new Vector2f();

  private int wobbleFramesRemaining_800c67c4;

  private int framesUntilWobble_800c67d4;

  private int cameraWobbleOffsetX_800c67e4;
  private int cameraWobbleOffsetY_800c67e8;

  private final Vector3f cameraRotationVector_800fab98 = new Vector3f();
  private final Vector3f temp1_800faba0 = new Vector3f();
  private final Vector3f temp2_800faba8 = new Vector3f();

  private boolean useCameraWobble_800fabb8;

  public void setWobble(final int frames, final int framesUntil, final int offsetX, final int offsetY) {
    this.wobbleFramesRemaining_800c67c4 = frames;
    this.framesUntilWobble_800c67d4 = framesUntil;
    this.cameraWobbleOffsetX_800c67e4 = offsetX;
    this.cameraWobbleOffsetY_800c67e8 = offsetY;
    this.useCameraWobble_800fabb8 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d47dcL)
  public void FUN_800d47dc(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.viewpointBaseTranslation_94.set(this.rview2_00.viewpoint_00);

    if(a5 == 0) {
      //LAB_800d4854
      this.viewpointTicksRemaining_d0 = ticks;
      this.stepX_b0 = (x - this.viewpointBaseTranslation_94.x) / ticks;
      this.stepY_bc = (y - this.viewpointBaseTranslation_94.y) / ticks;
      this.stepZ_c8 = (z - this.viewpointBaseTranslation_94.z) / ticks;
    } else assert a5 != 1 : "Undefined t0/t1";

    //LAB_800d492c
    //LAB_800d4934
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 8;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d496cL)
  public void FUN_800d496c(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.viewpointAngleX_ac = this.calculateCameraValue(false, 1, 0, null); // Angle from 0 to viewpoint X
    this.viewpointAngleY_b8 = this.calculateCameraValue(false, 1, 1, null); // Angle from 0 to viewpoint Y
    this.viewpointDeltaMagnitude_a0 = this.calculateCameraValue(false, 1, 2, null); // Angle from 0 to viewpoint Z

    if(a5 == 0) {
      //LAB_800d4a24
      this.viewpointTicksRemaining_d0 = ticks;
      this.stepX_b0 = this.calculateStep(0, this.viewpointAngleX_ac, x, ticks, stepType & 3);
      this.stepY_bc = this.calculateStep(1, this.viewpointAngleY_b8, y, ticks, stepType >> 2 & 3);
      this.stepZ_c8 = this.calculateStep(2, this.viewpointDeltaMagnitude_a0, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d4a7c
      final float x2 = this.calculateDifference(0, this.viewpointAngleX_ac, x, stepType & 3);
      final float y2 = this.calculateDifference(1, this.viewpointAngleY_b8, y, stepType >> 2 & 3);
      final float z2 = this.calculateDifference(2, this.viewpointDeltaMagnitude_a0, z, 0);
      this.viewpointTicksRemaining_d0 = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      this.stepX_b0 = this.calculateStep(0, this.viewpointAngleX_ac, x, this.viewpointTicksRemaining_d0, stepType & 3);
      this.stepY_bc = this.calculateStep(1, this.viewpointAngleY_b8, y, this.viewpointTicksRemaining_d0, stepType >> 2 & 3);
      this.stepZ_c8 = this.calculateStep(2, this.viewpointDeltaMagnitude_a0, z, this.viewpointTicksRemaining_d0, 0);
    }

    //LAB_800d4b68
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 9;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d4bacL)
  public void FUN_800d4bac(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.viewpointBaseTranslation_94.x = this.calculateCameraValue(false, 4, 0, null);
    this.viewpointBaseTranslation_94.y = this.calculateCameraValue(false, 4, 1, null);
    this.viewpointBaseTranslation_94.z = this.calculateCameraValue(false, 4, 2, null);

    if(a5 == 0) {
      //LAB_800d4c5c
      this.viewpointTicksRemaining_d0 = ticks;
      this.stepX_b0 = (x - this.viewpointBaseTranslation_94.x) / ticks;
      this.stepY_bc = (y - this.viewpointBaseTranslation_94.y) / ticks;
      this.stepZ_c8 = (z - this.viewpointBaseTranslation_94.z) / ticks;
    } else assert a5 != 1 : "Undefined s3/s5";

    //LAB_800d4d34
    //LAB_800d4d3c
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 12;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d4d7cL)
  public void FUN_800d4d7c(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.viewpointAngleX_ac = this.calculateCameraValue(false, 5, 0, null);
    this.viewpointAngleY_b8 = this.calculateCameraValue(false, 5, 1, null);
    this.viewpointDeltaMagnitude_a0 = this.calculateCameraValue(false, 5, 2, null);

    if(a5 == 0) {
      //LAB_800d4e34
      this.viewpointTicksRemaining_d0 = ticks;
      this.stepX_b0 = this.calculateStep(0, this.viewpointAngleX_ac, x, ticks, stepType & 3);
      this.stepY_bc = this.calculateStep(1, this.viewpointAngleY_b8, y, ticks, stepType >> 2 & 3);
      this.stepZ_c8 = this.calculateStep(2, this.viewpointDeltaMagnitude_a0, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d4e8c
      final float x2 = this.calculateDifference(0, this.viewpointAngleX_ac, x, stepType & 3);
      final float y2 = this.calculateDifference(1, this.viewpointAngleY_b8, y, stepType >> 2 & 3);
      final float z2 = this.calculateDifference(2, this.viewpointDeltaMagnitude_a0, z, 0);
      this.viewpointTicksRemaining_d0 = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      this.stepX_b0 = this.calculateStep(0, this.viewpointAngleX_ac, x, this.viewpointTicksRemaining_d0, stepType & 3);
      this.stepY_bc = this.calculateStep(1, this.viewpointAngleY_b8, y, this.viewpointTicksRemaining_d0, stepType >> 2 & 3);
      this.stepZ_c8 = this.calculateStep(2, this.viewpointDeltaMagnitude_a0, z, this.viewpointTicksRemaining_d0, 0);
    }

    //LAB_800d4f78
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 13;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d4fbcL)
  public void FUN_800d4fbc(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.viewpointBobj_f4 = bobj;
    this.viewpointBaseTranslation_94.x = this.calculateCameraValue(false, 6, 0, bobj);
    this.viewpointBaseTranslation_94.y = this.calculateCameraValue(false, 6, 1, bobj);
    this.viewpointBaseTranslation_94.z = this.calculateCameraValue(false, 6, 2, bobj);

    if(a5 == 0) {
      //LAB_800d5078
      this.viewpointTicksRemaining_d0 = ticks;

      if(ticks != 0) {
        this.stepX_b0 = (x - this.viewpointBaseTranslation_94.x) / ticks;
        this.stepY_bc = (y - this.viewpointBaseTranslation_94.y) / ticks;
        this.stepZ_c8 = (z - this.viewpointBaseTranslation_94.z) / ticks;
      } else {
        this.stepX_b0 = -1;
        this.stepY_bc = -1;
        this.stepZ_c8 = -1;
      }
    } else if(a5 == 1) {
      //LAB_800d50c4
      final float x2 = x - this.viewpointBaseTranslation_94.x;
      final float y2 = y - this.viewpointBaseTranslation_94.y;
      final float z2 = z - this.viewpointBaseTranslation_94.z;
      final float v0 = Math.sqrt(z2 * z2 + y2 * y2 + x2 * x2) / ticks;
      this.viewpointTicksRemaining_d0 = (int)v0;

      if(v0 != 0) {
        this.stepX_b0 = (x - this.viewpointBaseTranslation_94.x) / v0;
        this.stepY_bc = (y - this.viewpointBaseTranslation_94.y) / v0;
        this.stepZ_c8 = (z - this.viewpointBaseTranslation_94.z) / v0;
      } else {
        this.stepX_b0 = -1;
        this.stepY_bc = -1;
        this.stepZ_c8 = -1;
      }
    }

    //LAB_800d5150
    //LAB_800d5158
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 14;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d519cL)
  public void FUN_800d519c(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.viewpointBobj_f4 = bobj;
    this.viewpointAngleX_ac = this.calculateCameraValue(false, 7, 0, bobj);
    this.viewpointAngleY_b8 = this.calculateCameraValue(false, 7, 1, bobj);
    this.viewpointDeltaMagnitude_a0 = this.calculateCameraValue(false, 7, 2, bobj);

    if(a5 == 0) {
      //LAB_800d525c
      this.viewpointTicksRemaining_d0 = ticks;
      this.stepX_b0 = this.calculateStep(0, this.viewpointAngleX_ac, x, ticks, stepType & 3);
      this.stepY_bc = this.calculateStep(1, this.viewpointAngleY_b8, y, ticks, stepType >> 2 & 3);
      this.stepZ_c8 = this.calculateStep(2, this.viewpointDeltaMagnitude_a0, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d52b4
      final float x2 = this.calculateDifference(0, this.viewpointAngleX_ac, x, stepType & 3);
      final float y2 = this.calculateDifference(1, this.viewpointAngleY_b8, y, stepType >> 2 & 3);
      final float z2 = this.calculateDifference(2, this.viewpointDeltaMagnitude_a0, z, 0);
      this.viewpointTicksRemaining_d0 = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      this.stepX_b0 = this.calculateStep(0, this.viewpointAngleX_ac, x, this.viewpointTicksRemaining_d0, stepType & 3);
      this.stepY_bc = this.calculateStep(1, this.viewpointAngleY_b8, y, this.viewpointTicksRemaining_d0, stepType >> 2 & 3);
      this.stepZ_c8 = this.calculateStep(2, this.viewpointDeltaMagnitude_a0, z, this.viewpointTicksRemaining_d0, 0);
    }

    //LAB_800d53a0
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 15;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d53e4L)
  public void FUN_800d53e4(final float x, final float y, final float z, final float initialStepZ, final float finalStepZ, final int stepType, final BattleObject bobj) {
    final float dx = x - this.rview2_00.viewpoint_00.x;
    final float dy = y - this.rview2_00.viewpoint_00.y;
    final float dz = z - this.rview2_00.viewpoint_00.z;
    this.vec_d4.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    this.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    final float ticks = this.vec_d4.z * 2.0f / (finalStepZ + initialStepZ);
    final float zAccel = (finalStepZ - initialStepZ) / ticks;
    this.vec_d4.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    this.stepZ_a4 = initialStepZ;
    this.viewpointTargetTranslation_e8.set(x, y, z);
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 16;
    this.viewpointTicksRemaining_d0 = (int)ticks;
    this.stepZAcceleration_b4 = zAccel;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d5ec8L)
  public void FUN_800d5ec8(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final float dx = x - this.rview2_00.viewpoint_00.x;
    final float dy = y - this.rview2_00.viewpoint_00.y;
    final float dz = z - this.rview2_00.viewpoint_00.z;
    this.viewpointTicksRemaining_d0 = ticks;
    this.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    this.vec_d4.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    this.vec_d4.z = Math.sqrt(dx * dx + dy * dy + dz * dz);

    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_d4.z, ticks, initialStepZ, finalStepZ);

    //LAB_800d6030
    //LAB_800d6038
    this.stepZ_a4 = initialStepZ.get();
    this.stepZAcceleration_b4 = (finalStepZ.get() - initialStepZ.get()) / this.viewpointTicksRemaining_d0;
    this.viewpointTargetTranslation_e8.set(x, y, z);
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 16;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d60b0L)
  public void FUN_800d60b0(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    this.viewpointAngleX_ac = this.calculateCameraValue(false, 1, 0, null);
    this.viewpointAngleY_b8 = this.calculateCameraValue(false, 1, 1, null);
    this.viewpointDeltaMagnitude_a0 = this.calculateCameraValue(false, 1, 2, null);
    final float deltaX = this.calculateDifference(0, this.viewpointAngleX_ac, x, stepType & 3);
    final float deltaY = this.calculateDifference(1, this.viewpointAngleY_b8, y, stepType >> 2 & 3);
    this.viewpointTicksRemaining_d0 = ticks;
    this.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    this.vec_d4.y = 0.0f;
    this.vec_d4.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_d4.z, ticks, initialStepZ, finalStepZ);

    //LAB_800d6238
    //LAB_800d6240
    this.stepZ_e0 = finalStepZ.get();
    this.viewpointTargetTranslation_e8.set(x, y, z);
    this.stepZAcceleration_e4 = (initialStepZ.get() - finalStepZ.get()) / this.viewpointTicksRemaining_d0;
    this.stepZ_a4 = this.calculateDifference(2, this.viewpointDeltaMagnitude_a0, z, 0) / this.viewpointTicksRemaining_d0;
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 17;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d62d8L)
  public void FUN_800d62d8(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    this.viewpointBaseTranslation_94.x = this.calculateCameraValue(false, 4, 0, null);
    this.viewpointBaseTranslation_94.y = this.calculateCameraValue(false, 4, 1, null);
    this.viewpointBaseTranslation_94.z = this.calculateCameraValue(false, 4, 2, null);
    final float dx = x - this.viewpointBaseTranslation_94.x;
    final float dy = y - this.viewpointBaseTranslation_94.y;
    final float dz = z - this.viewpointBaseTranslation_94.z;
    this.viewpointTicksRemaining_d0 = ticks;
    this.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    this.vec_d4.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    this.vec_d4.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_d4.z, ticks, initialStepZ, finalStepZ);
    this.viewpointTargetTranslation_e8.set(x, y, z);
    this.stepZ_a4 = initialStepZ.get();
    this.stepZAcceleration_b4 = (finalStepZ.get() - initialStepZ.get()) / this.viewpointTicksRemaining_d0;
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 20;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d64e4L)
  public void FUN_800d64e4(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    this.viewpointAngleX_ac = this.calculateCameraValue(false, 5, 0, null);
    this.viewpointAngleY_b8 = this.calculateCameraValue(false, 5, 1, null);
    this.viewpointDeltaMagnitude_a0 = this.calculateCameraValue(false, 5, 2, null);
    final float deltaX = this.calculateDifference(0, this.viewpointAngleX_ac, x, stepType & 3);
    final float deltaY = this.calculateDifference(1, this.viewpointAngleY_b8, y, stepType >> 2 & 3);
    this.viewpointTicksRemaining_d0 = ticks;
    this.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    this.vec_d4.y = 0.0f;
    this.vec_d4.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_d4.z, ticks, initialStepZ, finalStepZ);

    //LAB_800d666c
    //LAB_800d6674
    this.stepZ_e0 = initialStepZ.get();
    this.viewpointTargetTranslation_e8.set(x, y, z);
    this.stepZAcceleration_e4 = (finalStepZ.get() - initialStepZ.get()) / this.viewpointTicksRemaining_d0;
    this.stepZ_a4 = this.calculateDifference(2, this.viewpointDeltaMagnitude_a0, z, 0) / this.viewpointTicksRemaining_d0;
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 21;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d670cL)
  public void FUN_800d670c(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    this.viewpointBobj_f4 = bobj;
    this.viewpointBaseTranslation_94.x = this.calculateCameraValue(false, 6, 0, bobj);
    this.viewpointBaseTranslation_94.y = this.calculateCameraValue(false, 6, 1, bobj);
    this.viewpointBaseTranslation_94.z = this.calculateCameraValue(false, 6, 2, bobj);
    final float dx = x - this.viewpointBaseTranslation_94.x;
    final float dy = y - this.viewpointBaseTranslation_94.y;
    final float dz = z - this.viewpointBaseTranslation_94.z;
    this.viewpointTicksRemaining_d0 = ticks;
    this.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    this.vec_d4.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    this.vec_d4.z = Math.sqrt(dx * dx + dy * dy + dz * dz);

    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_d4.z, ticks, initialStepZ, finalStepZ);

    //LAB_800d68e0
    //LAB_800d68e8
    this.stepZ_a4 = initialStepZ.get();
    this.viewpointTargetTranslation_e8.set(x, y, z);
    this.stepZAcceleration_b4 = (finalStepZ.get() - initialStepZ.get()) / this.viewpointTicksRemaining_d0;
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 22;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d6960L)
  public void FUN_800d6960(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    this.viewpointBobj_f4 = bobj;
    this.viewpointAngleX_ac = this.calculateCameraValue(false, 7, 0, bobj);
    this.viewpointAngleY_b8 = this.calculateCameraValue(false, 7, 1, bobj);
    this.viewpointDeltaMagnitude_a0 = this.calculateCameraValue(false, 7, 2, bobj);
    final float s1 = this.calculateDifference(0, this.viewpointAngleX_ac, x, stepType & 3);
    final float s0 = this.calculateDifference(1, this.viewpointAngleY_b8, y, stepType >> 2 & 3);
    this.viewpointTicksRemaining_d0 = ticks;
    this.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(s0, s1), MathHelper.TWO_PI);
    this.vec_d4.y = 0.0f;
    this.vec_d4.z = Math.sqrt(s1 * s1 + s0 * s0);
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_d4.z, ticks, initialStepZ, finalStepZ);

    //LAB_800d6af0
    //LAB_800d6af8
    this.stepZ_e0 = initialStepZ.get();
    this.viewpointTargetTranslation_e8.set(x, y, z);
    this.stepZAcceleration_e4 = (finalStepZ.get() - initialStepZ.get()) / this.viewpointTicksRemaining_d0;
    this.stepZ_a4 = this.calculateDifference(2, this.viewpointDeltaMagnitude_a0, z, 0) / this.viewpointTicksRemaining_d0;
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 23;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d6b90L)
  public void FUN_800d6b90(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.refpointBaseTranslation_20.set(this.rview2_00.refpoint_0c);

    if(a5 == 0) {
      //LAB_800d6c04
      this.refpointTicksRemaining_5c = ticks;

      // Retail bug: divide by 0 is possible here - the processor sets LO to -1 in this case
      if(ticks != 0) {
        this.stepX_3c = (x - this.refpointBaseTranslation_20.x) / ticks;
        this.stepY_48 = (y - this.refpointBaseTranslation_20.y) / ticks;
        this.stepZ_54 = (z - this.refpointBaseTranslation_20.z) / ticks;
      } else {
        this.stepX_3c = -1;
        this.stepY_48 = -1;
        this.stepZ_54 = -1;
      }
    } else if(a5 == 1) {
      throw new RuntimeException("t0/t1 undefined");
    }

    //LAB_800d6cdc
    //LAB_800d6ce4
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 8;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d6d18L)
  public void FUN_800d6d18(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.refpointAngleX_38 = this.calculateCameraValue(true, 1, 0, null);
    this.refpointAngleY_44 = this.calculateCameraValue(true, 1, 1, null);
    this.refpointDeltaMagnitude_2c = this.calculateCameraValue(true, 1, 2, null);

    if(a5 == 0) {
      //LAB_800d6dd0
      this.refpointTicksRemaining_5c = ticks;
      this.stepX_3c = this.calculateStep(0, this.refpointAngleX_38, x, ticks, stepType & 3);
      this.stepY_48 = this.calculateStep(1, this.refpointAngleY_44, y, ticks, stepType >> 2 & 3);
      this.stepZ_54 = this.calculateStep(2, this.refpointDeltaMagnitude_2c, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d6e28
      final float x2 = this.calculateDifference(0, this.refpointAngleX_38, x, stepType & 3);
      final float y2 = this.calculateDifference(1, this.refpointAngleY_44, y, stepType >> 2 & 3);
      final float z2 = this.calculateDifference(2, this.refpointDeltaMagnitude_2c, z, 0);
      this.refpointTicksRemaining_5c = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      this.stepX_3c = this.calculateStep(0, this.refpointAngleX_38, x, this.refpointTicksRemaining_5c, stepType & 3);
      this.stepY_48 = this.calculateStep(1, this.refpointAngleY_44, y, this.refpointTicksRemaining_5c, stepType >> 2 & 3);
      this.stepZ_54 = this.calculateStep(2, this.refpointDeltaMagnitude_2c, z, this.refpointTicksRemaining_5c, 0);
    }

    //LAB_800d6f14
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 9;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d6f58L)
  public void FUN_800d6f58(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.refpointBaseTranslation_20.x = this.calculateCameraValue(true, 2, 0, null);
    this.refpointBaseTranslation_20.y = this.calculateCameraValue(true, 2, 1, null);
    this.refpointBaseTranslation_20.z = this.calculateCameraValue(true, 2, 2, null);

    if(a5 == 0) {
      //LAB_800d7008
      this.refpointTicksRemaining_5c = ticks;
      this.stepX_3c = (x - this.refpointBaseTranslation_20.x) / ticks;
      this.stepY_48 = (y - this.refpointBaseTranslation_20.y) / ticks;
      this.stepZ_54 = (z - this.refpointBaseTranslation_20.z) / ticks;
    } else if(a5 == 1) {
      throw new RuntimeException("Broken code");
    }

    //LAB_800d70e0
    //LAB_800d70e8
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 10;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7128L)
  public void FUN_800d7128(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.refpointAngleX_38 = this.calculateCameraValue(true, 3, 0, null);
    this.refpointAngleY_44 = this.calculateCameraValue(true, 3, 1, null);
    this.refpointDeltaMagnitude_2c = this.calculateCameraValue(true, 3, 2, null);

    if(a5 == 0) {
      //LAB_800d71e0
      this.refpointTicksRemaining_5c = ticks;
      this.stepX_3c = this.calculateStep(0, this.refpointAngleX_38, x, ticks, stepType & 3);
      this.stepY_48 = this.calculateStep(1, this.refpointAngleY_44, y, ticks, stepType >> 2 & 3);
      this.stepZ_54 = this.calculateStep(2, this.refpointDeltaMagnitude_2c, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d7238
      final float x2 = this.calculateDifference(0, this.refpointAngleX_38, x, stepType & 3);
      final float y2 = this.calculateDifference(1, this.refpointAngleY_44, y, stepType >> 2 & 3);
      final float z2 = this.calculateDifference(2, this.refpointDeltaMagnitude_2c, z, 0);
      this.refpointTicksRemaining_5c = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      this.stepX_3c = this.calculateStep(0, this.refpointAngleX_38, x, this.refpointTicksRemaining_5c, stepType & 3);
      this.stepY_48 = this.calculateStep(1, this.refpointAngleY_44, y, this.refpointTicksRemaining_5c, stepType >> 2 & 3);
      this.stepZ_54 = this.calculateStep(2, this.refpointDeltaMagnitude_2c, z, this.refpointTicksRemaining_5c, 0);
    }

    //LAB_800d7324
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 11;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7368L)
  public void FUN_800d7368(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.refpointBobj_80 = bobj;
    this.refpointBaseTranslation_20.x = this.calculateCameraValue(true, 6, 0, bobj);
    this.refpointBaseTranslation_20.y = this.calculateCameraValue(true, 6, 1, bobj);
    this.refpointBaseTranslation_20.z = this.calculateCameraValue(true, 6, 2, bobj);

    if(a5 == 0) {
      //LAB_800d7424
      this.refpointTicksRemaining_5c = ticks;
      this.stepX_3c = MathHelper.safeDiv(x - this.refpointBaseTranslation_20.x, ticks);
      this.stepY_48 = MathHelper.safeDiv(y - this.refpointBaseTranslation_20.y, ticks);
      this.stepZ_54 = MathHelper.safeDiv(z - this.refpointBaseTranslation_20.z, ticks);
    } else if(a5 == 1) {
      throw new RuntimeException("Undefined s5/s6");
    }

    //LAB_800d74fc
    //LAB_800d7504
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 14;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7548L)
  public void FUN_800d7548(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    this.refpointBobj_80 = bobj;
    this.refpointAngleX_38 = this.calculateCameraValue(true, 7, 0, bobj);
    this.refpointAngleY_44 = this.calculateCameraValue(true, 7, 1, bobj);
    this.refpointDeltaMagnitude_2c = this.calculateCameraValue(true, 7, 2, bobj);

    if(a5 == 0) {
      //LAB_800d7608
      this.refpointTicksRemaining_5c = ticks;
      this.stepX_3c = this.calculateStep(0, this.refpointAngleX_38, x, ticks, stepType & 3);
      this.stepY_48 = this.calculateStep(1, this.refpointAngleY_44, y, ticks, stepType >> 2 & 3);
      this.stepZ_54 = this.calculateStep(2, this.refpointDeltaMagnitude_2c, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d7660
      final float x2 = this.calculateDifference(0, this.refpointAngleX_38, x, stepType & 3);
      final float y2 = this.calculateDifference(1, this.refpointAngleY_44, y, stepType >> 2 & 3);
      final float z2 = this.calculateDifference(2, this.refpointDeltaMagnitude_2c, z, 0);
      this.refpointTicksRemaining_5c = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      this.stepX_3c = this.calculateStep(0, this.refpointAngleX_38, x, this.refpointTicksRemaining_5c, stepType & 3);
      this.stepY_48 = this.calculateStep(1, this.refpointAngleY_44, y, this.refpointTicksRemaining_5c, stepType >> 2 & 3);
      this.stepZ_54 = this.calculateStep(2, this.refpointDeltaMagnitude_2c, z, this.refpointTicksRemaining_5c, 0);
    }

    //LAB_800d774c
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 15;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7790L)
  public void FUN_800d7790(final float x, final float y, final float z, final float initialStepZ, final float finalStepZ, final int stepType, final BattleObject bobj) {
    final float dx = x - this.rview2_00.refpoint_0c.x;
    final float dy = y - this.rview2_00.refpoint_0c.y;
    final float dz = z - this.rview2_00.refpoint_0c.z;
    this.vec_60.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    this.vec_60.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    this.vec_60.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    this.stepZ_30 = initialStepZ;
    this.refpointTargetTranslation_74.set(x, y, z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 16;
    this.refpointTicksRemaining_5c = (int)(this.vec_60.z * 2.0f / (finalStepZ + initialStepZ));

    if(this.refpointTicksRemaining_5c > 0) {
      this.stepZAcceleration_40 = (finalStepZ - initialStepZ) / this.refpointTicksRemaining_5c;
    } else {
      this.stepZAcceleration_40 = -1;
    }
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7920L)
  public void FUN_800d7920(final float x, final float y, final float z, final float initialStepZ, final float finalStepZ, final int stepType, final BattleObject bobj) {
    this.refpointAngleX_38 = this.calculateCameraValue(true, 1, 0, null);
    this.refpointAngleY_44 = this.calculateCameraValue(true, 1, 1, null);
    this.refpointDeltaMagnitude_2c = this.calculateCameraValue(true, 1, 2, null);
    final float deltaX = this.calculateDifference(0, this.refpointAngleX_38, x, stepType & 3);
    final float deltaY = this.calculateDifference(1, this.refpointAngleY_44, y, stepType >> 2 & 3);
    this.vec_60.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    this.vec_60.y = 0.0f;
    this.vec_60.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    this.stepZ_6c = initialStepZ;
    this.refpointTargetTranslation_74.set(x, y, z);
    this.refpointTicksRemaining_5c = (int)(this.vec_60.z * 2.0f / (finalStepZ + initialStepZ));
    this.refpointCallbackIndex_121 = 17;
    this.flags_11c |= UPDATE_REFPOINT;

    if(this.refpointTicksRemaining_5c > 0) {
      this.stepZ_30 = this.calculateDifference(2, this.refpointDeltaMagnitude_2c, z, 0) / this.refpointTicksRemaining_5c;
      this.stepZAcceleration_70 = (finalStepZ - initialStepZ) / this.refpointTicksRemaining_5c;
    } else {
      this.stepZ_30 = -1;
      this.stepZAcceleration_70 = -1;
    }
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7aecL)
  public void FUN_800d7aec(final float x, final float y, final float z, final float initialStepZ, final float finalStepZ, final int stepType, final BattleObject bobj) {
    this.refpointBaseTranslation_20.x = this.calculateCameraValue(true, 2, 0, null);
    this.refpointBaseTranslation_20.y = this.calculateCameraValue(true, 2, 1, null);
    this.refpointBaseTranslation_20.z = this.calculateCameraValue(true, 2, 2, null);
    final float dx = x - this.refpointBaseTranslation_20.x;
    final float dy = y - this.refpointBaseTranslation_20.y;
    final float dz = z - this.refpointBaseTranslation_20.z;
    this.vec_60.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    this.vec_60.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    this.vec_60.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    this.stepZ_30 = initialStepZ;
    this.refpointTargetTranslation_74.set(x, y, z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 18;
    this.refpointTicksRemaining_5c = (int)(this.vec_60.z * 2.0f / (finalStepZ + initialStepZ));

    if(this.refpointTicksRemaining_5c > 0) {
      this.stepZAcceleration_40 = (finalStepZ - initialStepZ) / this.refpointTicksRemaining_5c;
    } else {
      this.stepZAcceleration_40 = -1;
    }
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7cdcL)
  public void FUN_800d7cdc(final float x, final float y, final float z, final float initialStepZ, final float finalStepZ, final int stepType, final BattleObject bobj) {
    this.refpointAngleX_38 = this.calculateCameraValue(true, 3, 0, null);
    this.refpointAngleY_44 = this.calculateCameraValue(true, 3, 1, null);
    this.refpointDeltaMagnitude_2c = this.calculateCameraValue(true, 3, 2, null);
    final float deltaX = this.calculateDifference(0, this.refpointAngleX_38, x, stepType & 0x3);
    final float deltaY = this.calculateDifference(1, this.refpointAngleY_44, y, stepType >> 2 & 0x3);
    this.vec_60.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    this.vec_60.y = 0.0f;
    this.vec_60.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    this.refpointTicksRemaining_5c = (int)(this.vec_60.z * 2.0f / (finalStepZ + initialStepZ));
    this.stepZ_6c = initialStepZ;
    this.refpointTargetTranslation_74.set(x, y, z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 19;

    if(this.refpointTicksRemaining_5c > 0) {
      this.stepZ_30 = this.calculateDifference(2, this.refpointDeltaMagnitude_2c, z, 0) / this.refpointTicksRemaining_5c;
      this.stepZAcceleration_70 = (finalStepZ - initialStepZ) / this.refpointTicksRemaining_5c;
    } else {
      this.stepZ_30 = -1;
      this.stepZAcceleration_70 = -1;
    }
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d8274L)
  public void FUN_800d8274(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final float dx = x - this.rview2_00.refpoint_0c.x;
    final float dy = y - this.rview2_00.refpoint_0c.y;
    final float dz = z - this.rview2_00.refpoint_0c.z;
    this.refpointTicksRemaining_5c = ticks;
    this.vec_60.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    this.vec_60.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    this.vec_60.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_60.z, this.refpointTicksRemaining_5c, initialStepZ, finalStepZ);
    this.stepZ_30 = initialStepZ.get();
    this.stepZAcceleration_40 = (finalStepZ.get() - initialStepZ.get()) / this.refpointTicksRemaining_5c;
    this.refpointTargetTranslation_74.set(x, y, z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 16;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d8424L)
  public void FUN_800d8424(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    this.refpointAngleX_38 = this.calculateCameraValue(true, 1, 0, null);
    this.refpointAngleY_44 = this.calculateCameraValue(true, 1, 1, null);
    this.refpointDeltaMagnitude_2c = this.calculateCameraValue(true, 1, 2, null);
    final float deltaX = this.calculateDifference(0, this.refpointAngleX_38, x, stepType & 3);
    final float deltaY = this.calculateDifference(1, this.refpointAngleY_44, y, stepType >> 2 & 3);
    this.refpointTicksRemaining_5c = ticks;
    this.vec_60.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    this.vec_60.y = 0.0f;
    this.vec_60.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_60.z, ticks, initialStepZ, finalStepZ);
    this.stepZ_6c = initialStepZ.get();
    this.stepZAcceleration_70 = (finalStepZ.get() - initialStepZ.get()) / this.refpointTicksRemaining_5c;
    this.refpointTargetTranslation_74.set(x, y, z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 17;
    this.stepZ_30 = this.calculateDifference(2, this.refpointDeltaMagnitude_2c, z, 0) / this.refpointTicksRemaining_5c;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d8614L)
  public void FUN_800d8614(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    this.refpointBaseTranslation_20.x = this.calculateCameraValue(true, 2, 0, null);
    this.refpointBaseTranslation_20.y = this.calculateCameraValue(true, 2, 1, null);
    this.refpointBaseTranslation_20.z = this.calculateCameraValue(true, 2, 2, null);
    final float dx = x - this.refpointBaseTranslation_20.x;
    final float dy = y - this.refpointBaseTranslation_20.y;
    final float dz = z - this.refpointBaseTranslation_20.z;
    this.vec_60.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    this.vec_60.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    this.vec_60.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    this.refpointTicksRemaining_5c = ticks;
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_60.z, ticks, initialStepZ, finalStepZ);
    this.stepZ_30 = initialStepZ.get();
    this.stepZAcceleration_40 = (finalStepZ.get() - initialStepZ.get()) / this.refpointTicksRemaining_5c;
    this.refpointTargetTranslation_74.set(x, y, z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 18;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d8808L)
  public void FUN_800d8808(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    this.refpointAngleX_38 = this.calculateCameraValue(true, 3, 0, null);
    this.refpointAngleY_44 = this.calculateCameraValue(true, 3, 1, null);
    this.refpointDeltaMagnitude_2c = this.calculateCameraValue(true, 3, 2, null);
    final float deltaX = this.calculateDifference(0, this.refpointAngleX_38, x, stepType & 3);
    final float deltaY = this.calculateDifference(1, this.refpointAngleY_44, y, stepType >> 2 & 3);
    this.vec_60.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    this.vec_60.y = 0.0f;
    this.vec_60.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    this.refpointTicksRemaining_5c = ticks;
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_60.z, ticks, initialStepZ, finalStepZ);
    this.stepZ_30 = this.calculateDifference(2, this.refpointDeltaMagnitude_2c, z, 0) / this.refpointTicksRemaining_5c;
    this.stepZ_6c = initialStepZ.get();
    this.stepZAcceleration_70 = (finalStepZ.get() - initialStepZ.get()) / this.refpointTicksRemaining_5c;
    this.refpointTargetTranslation_74.set(x, y, z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 19;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d89f8L)
  public void FUN_800d89f8(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    this.refpointBobj_80 = bobj;
    this.refpointBaseTranslation_20.x = this.calculateCameraValue(true, 6, 0, bobj);
    this.refpointBaseTranslation_20.y = this.calculateCameraValue(true, 6, 1, bobj);
    this.refpointBaseTranslation_20.z = this.calculateCameraValue(true, 6, 2, bobj);
    final float dx = x - this.refpointBaseTranslation_20.x;
    final float dy = y - this.refpointBaseTranslation_20.y;
    final float dz = z - this.refpointBaseTranslation_20.z;
    this.refpointTicksRemaining_5c = ticks;
    this.vec_60.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    this.vec_60.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    this.vec_60.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_60.z, ticks, initialStepZ, finalStepZ);
    this.refpointTargetTranslation_74.set(x, y, z);
    this.stepZ_30 = initialStepZ.get();
    this.stepZAcceleration_40 = (finalStepZ.get() - initialStepZ.get()) / this.refpointTicksRemaining_5c;
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 22;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d8bf4L)
  public void FUN_800d8bf4(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    this.refpointBobj_80 = bobj;
    this.refpointAngleX_38 = this.calculateCameraValue(true, 7, 0, bobj);
    this.refpointAngleY_44 = this.calculateCameraValue(true, 7, 1, bobj);
    this.refpointDeltaMagnitude_2c = this.calculateCameraValue(true, 7, 2, bobj);
    final float s2 = this.calculateDifference(0, this.refpointAngleX_38, x, stepType & 3);
    final float s0 = this.calculateDifference(1, this.refpointAngleY_44, y, stepType >> 2 & 3);
    this.vec_60.x = MathHelper.floorMod(MathHelper.atan2(s0, s2), MathHelper.TWO_PI);
    this.vec_60.y = 0.0f;
    this.vec_60.z = Math.sqrt(s2 * s2 + s0 * s0);
    this.refpointTicksRemaining_5c = ticks;
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    this.setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, this.vec_60.z, ticks, initialStepZ, finalStepZ);
    this.stepZ_30 = this.calculateDifference(2, this.refpointDeltaMagnitude_2c, z, 0) / this.refpointTicksRemaining_5c;
    this.refpointTargetTranslation_74.set(x, y, z);
    this.stepZ_6c = initialStepZ.get();
    this.stepZAcceleration_70 = (finalStepZ.get() - initialStepZ.get()) / this.refpointTicksRemaining_5c;
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 23;
  }

  @Method(0x800d8f10L)
  public void updateBattleCamera() {
    if((this.flags_11c & UPDATE_VIEWPOINT) != 0) {
      LOGGER.info(CAMERA, "[CAMERA] Array=_800facbc, FUN index=%d", this.viewpointCallbackIndex_120);
      this.cameraViewpointMethods_800facbc[this.viewpointCallbackIndex_120].run();
    }

    if((this.flags_11c & UPDATE_REFPOINT) != 0) {
      LOGGER.info(CAMERA, "[CAMERA] Array=_800fad1c, FUN index=%d", this.refpointCallbackIndex_121);
      this.cameraRefpointMethods_800fad1c[this.refpointCallbackIndex_121].run();
    }

    GsSetRefView2L(this.rview2_00);
    this.wobbleCamera();
    this.tickProjectionPlaneChange();
  }

  @Method(0x800d8fe0L)
  public void tickProjectionPlaneChange() {
    if(this.projectionPlaneChanging_118 && this.projectionPlaneChangeFrames_108 == 0) {
      setProjectionPlaneDistance(this.projectionPlaneDistance_100);
      this.projectionPlaneChanging_118 = false;
    }

    //LAB_800d9028
    if(this.projectionPlaneChanging_118 && this.projectionPlaneChangeFrames_108 != 0) {
      if(this.projectionPlaneMovementDirection_114 == 0) {
        this.projectionPlaneDistance_100 += this.projectionPlaneDistanceStep_10c;
      } else {
        //LAB_800d906c
        this.projectionPlaneDistance_100 -= this.projectionPlaneDistanceStep_10c;
      }

      //LAB_800d907c
      this.projectionPlaneDistanceStep_10c += this.projectionPlaneDistanceStepAcceleration_110;
      setProjectionPlaneDistance((int)this.projectionPlaneDistance_100);

      this.projectionPlaneChangeFrames_108--;
      if(this.projectionPlaneChangeFrames_108 == 0) {
        this.projectionPlaneChanging_118 = false;
      }
    }
    //LAB_800d90b8
  }

  @Method(0x800d90c8L)
  public void FUN_800d90c8() {
    this.viewpointBaseTranslation_94.x += this.stepX_b0;
    this.viewpointBaseTranslation_94.y += this.stepY_bc;
    this.viewpointBaseTranslation_94.z += this.stepZ_c8;
    this.setViewpoint(this.viewpointBaseTranslation_94.x, this.viewpointBaseTranslation_94.y, this.viewpointBaseTranslation_94.z);

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.flags_11c &= ~UPDATE_VIEWPOINT;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d9144
  }

  @Method(0x800d9154L)
  public void FUN_800d9154() {
    this.viewpointAngleX_ac += this.stepX_b0;
    this.viewpointAngleY_b8 += this.stepY_bc;
    this.viewpointDeltaMagnitude_a0 += this.stepZ_c8;
    final Vector3f v1 = new Vector3f(this.viewpointAngleX_ac, this.viewpointAngleY_b8, this.viewpointDeltaMagnitude_a0);
    this.FUN_800dcc94(ZERO, v1);
    this.setViewpoint(v1.x, v1.y, v1.z);

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.flags_11c &= ~UPDATE_VIEWPOINT;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d9210
  }

  @Method(0x800d9220L)
  public void FUN_800d9220() {
    this.viewpointBaseTranslation_94.x += this.stepX_b0;
    this.viewpointBaseTranslation_94.y += this.stepY_bc;
    this.viewpointBaseTranslation_94.z += this.stepZ_c8;

    this.setViewpoint(
      this.rview2_00.refpoint_0c.x + this.viewpointBaseTranslation_94.x,
      this.rview2_00.refpoint_0c.y + this.viewpointBaseTranslation_94.y,
      this.rview2_00.refpoint_0c.z + this.viewpointBaseTranslation_94.z
    );

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.viewpointCallbackIndex_120 = 4;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d92ac
  }

  @Method(0x800d92bcL)
  public void FUN_800d92bc() {
    this.viewpointDeltaMagnitude_a0 += this.stepZ_c8;
    this.viewpointAngleX_ac += this.stepX_b0;
    this.viewpointAngleY_b8 += this.stepY_bc;

    final Vector3f v1 = new Vector3f(this.viewpointAngleX_ac, this.viewpointAngleY_b8, this.viewpointDeltaMagnitude_a0);
    this.FUN_800dcc94(this.rview2_00.refpoint_0c, v1);
    this.setViewpoint(v1.x, v1.y, v1.z);

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.viewpointCallbackIndex_120 = 5;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d9370
  }

  @Method(0x800d9380L)
  public void FUN_800d9380() {
    this.viewpointBaseTranslation_94.x += this.stepX_b0;
    this.viewpointBaseTranslation_94.y += this.stepY_bc;
    this.viewpointBaseTranslation_94.z += this.stepZ_c8;

    final Vector3f pos = this.viewpointBobj_f4.getPosition();

    this.setViewpoint(
      pos.x + this.viewpointBaseTranslation_94.x,
      pos.y + this.viewpointBaseTranslation_94.y,
      pos.z + this.viewpointBaseTranslation_94.z
    );

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.viewpointCallbackIndex_120 = 6;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d9428
  }

  @Method(0x800d9438L)
  public void FUN_800d9438() {
    this.viewpointAngleX_ac += this.stepX_b0;
    this.viewpointAngleY_b8 += this.stepY_bc;
    this.viewpointDeltaMagnitude_a0 += this.stepZ_c8;
    final Vector3f v1 = new Vector3f(this.viewpointAngleX_ac, this.viewpointAngleY_b8, this.viewpointDeltaMagnitude_a0);
    this.FUN_800dcc94(this.viewpointBobj_f4.getPosition(), v1);
    this.setViewpoint(v1.x, v1.y, v1.z);

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.viewpointCallbackIndex_120 = 7;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d9508
  }

  @Method(0x800d9518L)
  public void FUN_800d9518() {
    this.cameraRotationVector_800fab98.x = this.vec_d4.x;
    this.cameraRotationVector_800fab98.y = this.vec_d4.y;
    this.cameraRotationVector_800fab98.z = 0.0f;
    this.cameraTransformMatrix_800c6798.rotationXYZ(this.cameraRotationVector_800fab98);
    this.stepZ_a4 += this.stepZAcceleration_b4;
    this.vec_d4.z -= this.stepZ_a4;
    this.temp1_800faba0.set(0.0f, 0.0f, this.vec_d4.z).mul(this.cameraTransformMatrix_800c6798, this.temp2_800faba8);
    this.temp2_800faba8.add(this.cameraTransformMatrix_800c6798.transfer);
    this.viewpointBaseTranslation_94.x = this.viewpointTargetTranslation_e8.x - this.temp2_800faba8.z;
    this.viewpointBaseTranslation_94.y = this.viewpointTargetTranslation_e8.y - this.temp2_800faba8.x;
    this.viewpointBaseTranslation_94.z = this.viewpointTargetTranslation_e8.z + this.temp2_800faba8.y;

    this.setViewpoint(this.viewpointBaseTranslation_94.x, this.viewpointBaseTranslation_94.y, this.viewpointBaseTranslation_94.z);

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.flags_11c &= ~UPDATE_VIEWPOINT;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d9638
  }

  @Method(0x800d9650L)
  public void FUN_800d9650() {
    final Vector3f v1 = new Vector3f(this.vec_d4);
    this.FUN_800dcc94(ZERO, v1);
    v1.x += this.viewpointTargetTranslation_e8.x;
    v1.y = v1.z + this.viewpointTargetTranslation_e8.y;
    this.viewpointDeltaMagnitude_a0 += this.stepZ_a4;
    v1.z = this.viewpointDeltaMagnitude_a0;
    this.FUN_800dcc94(ZERO, v1);
    this.setViewpoint(v1.x, v1.y, v1.z);
    this.stepZ_e0 += this.stepZAcceleration_e4;
    this.vec_d4.z -= this.stepZ_e0;

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.flags_11c &= ~UPDATE_VIEWPOINT;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d976c
  }

  @Method(0x800d9788L)
  public void FUN_800d9788() {
    this.cameraRotationVector_800fab98.x = this.vec_d4.x;
    this.cameraRotationVector_800fab98.y = this.vec_d4.y;
    this.cameraRotationVector_800fab98.z = 0.0f;
    this.cameraTransformMatrix_800c6798.rotationXYZ(this.cameraRotationVector_800fab98);
    this.stepZ_a4 += this.stepZAcceleration_b4;
    this.vec_d4.z -= this.stepZ_a4;
    this.temp1_800faba0.set(0.0f, 0.0f, this.vec_d4.z).mul(this.cameraTransformMatrix_800c6798, this.temp2_800faba8);
    this.temp2_800faba8.add(this.cameraTransformMatrix_800c6798.transfer);

    this.viewpointBaseTranslation_94.x = this.viewpointTargetTranslation_e8.x - this.temp2_800faba8.z;
    this.viewpointBaseTranslation_94.y = this.viewpointTargetTranslation_e8.y - this.temp2_800faba8.x;
    this.viewpointBaseTranslation_94.z = this.viewpointTargetTranslation_e8.z + this.temp2_800faba8.y;

    this.setViewpoint(
      this.rview2_00.refpoint_0c.x + this.viewpointBaseTranslation_94.x,
      this.rview2_00.refpoint_0c.y + this.viewpointBaseTranslation_94.y,
      this.rview2_00.refpoint_0c.z + this.viewpointBaseTranslation_94.z
    );

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.viewpointCallbackIndex_120 = 4;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d98b8
  }

  @Method(0x800d98d0L)
  public void FUN_800d98d0() {
    final Vector3f v1 = new Vector3f(this.vec_d4);
    this.FUN_800dcc94(ZERO, v1);
    this.viewpointDeltaMagnitude_a0 += this.stepZ_a4;
    v1.x += this.viewpointTargetTranslation_e8.x;
    v1.y = v1.z + this.viewpointTargetTranslation_e8.y;
    v1.z = this.viewpointDeltaMagnitude_a0;
    this.FUN_800dcc94(ZERO, v1);
    this.setViewpoint(this.rview2_00.refpoint_0c.x + v1.x, this.rview2_00.refpoint_0c.y + v1.y, this.rview2_00.refpoint_0c.z + v1.z);
    this.stepZ_e0 += this.stepZAcceleration_e4;
    this.vec_d4.z -= this.stepZ_e0;

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.viewpointAngleX_ac = this.calculateCameraValue(false, 5, 0, null);
      this.viewpointAngleY_b8 = this.calculateCameraValue(false, 5, 1, null);
      this.viewpointDeltaMagnitude_a0 = this.calculateCameraValue(false, 5, 2, null);
      this.viewpointCallbackIndex_120 = 5;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d9a4c
  }

  @Method(0x800d9a68L)
  public void FUN_800d9a68() {
    this.cameraRotationVector_800fab98.x = this.vec_d4.x;
    this.cameraRotationVector_800fab98.y = this.vec_d4.y;
    this.cameraRotationVector_800fab98.z = 0.0f;
    this.cameraTransformMatrix_800c6798.rotationXYZ(this.cameraRotationVector_800fab98);
    this.stepZ_a4 += this.stepZAcceleration_b4;
    this.vec_d4.z -= this.stepZ_a4;
    this.temp1_800faba0.set(0.0f, 0.0f, this.vec_d4.z).mul(this.cameraTransformMatrix_800c6798, this.temp2_800faba8);
    this.temp2_800faba8.add(this.cameraTransformMatrix_800c6798.transfer);
    this.viewpointBaseTranslation_94.x = this.viewpointTargetTranslation_e8.x - this.temp2_800faba8.z;
    this.viewpointBaseTranslation_94.y = this.viewpointTargetTranslation_e8.y - this.temp2_800faba8.x;
    this.viewpointBaseTranslation_94.z = this.viewpointTargetTranslation_e8.z + this.temp2_800faba8.y;

    final Vector3f pos = this.viewpointBobj_f4.getPosition();
    this.setViewpoint(pos.x + this.viewpointBaseTranslation_94.x, pos.y + this.viewpointBaseTranslation_94.y, pos.z + this.viewpointBaseTranslation_94.z);

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.viewpointCallbackIndex_120 = 6;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d9bb8
  }

  /** TODO I might have messed this up */
  @Method(0x800d9bd4L)
  public void FUN_800d9bd4() {
    final Vector3f ref = new Vector3f(this.vec_d4);
    this.FUN_800dcc94(ZERO, ref);
    this.viewpointDeltaMagnitude_a0 += this.stepZ_a4;
    ref.x += this.viewpointTargetTranslation_e8.x;
    ref.y = ref.z + this.viewpointTargetTranslation_e8.y;
    ref.z = this.viewpointDeltaMagnitude_a0;
    this.FUN_800dcc94(ZERO, ref);
    final Vector3f pos = this.viewpointBobj_f4.getPosition();
    this.setViewpoint(pos.x + ref.x, pos.y + ref.y, pos.z + ref.z);
    this.stepZ_e0 += this.stepZAcceleration_e4;
    this.vec_d4.z -= this.stepZ_e0;

    this.viewpointTicksRemaining_d0--;
    if(this.viewpointTicksRemaining_d0 <= 0) {
      this.calculate3dAngleOrMagnitude(pos, this.rview2_00.viewpoint_00, ref);
      this.viewpointAngleX_ac = ref.x;
      this.viewpointAngleY_b8 = ref.y;
      this.viewpointDeltaMagnitude_a0 = ref.z;
      this.viewpointCallbackIndex_120 = 7;
      this.viewpointMoving_122 = false;
    }
    //LAB_800d9d7c
  }

  @Method(0x800d9da0L)
  public void FUN_800d9da0() {
    this.refpointBaseTranslation_20.x += this.stepX_3c;
    this.refpointBaseTranslation_20.y += this.stepY_48;
    this.refpointBaseTranslation_20.z += this.stepZ_54;
    this.setRefpoint(this.refpointBaseTranslation_20.x, this.refpointBaseTranslation_20.y, this.refpointBaseTranslation_20.z);

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.flags_11c &= ~UPDATE_REFPOINT;
      this.refpointMoving_123 = false;
    }
    //LAB_800d9e1c
  }

  @Method(0x800d9e2cL)
  public void FUN_800d9e2c() {
    this.refpointAngleX_38 += this.stepX_3c;
    this.refpointAngleY_44 += this.stepY_48;
    this.refpointDeltaMagnitude_2c += this.stepZ_54;

    final Vector3f v1 = new Vector3f(this.refpointAngleX_38, this.refpointAngleY_44, this.refpointDeltaMagnitude_2c);
    this.FUN_800dcc94(ZERO, v1);
    this.setRefpoint(v1.x, v1.y, v1.z);

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.flags_11c &= ~UPDATE_REFPOINT;
      this.refpointMoving_123 = false;
    }
    //LAB_800d9ee8
  }

  @Method(0x800d9ef8L)
  public void FUN_800d9ef8() {
    this.refpointBaseTranslation_20.x += this.stepX_3c;
    this.refpointBaseTranslation_20.y += this.stepY_48;
    this.refpointBaseTranslation_20.z += this.stepZ_54;

    this.setRefpoint(
      this.rview2_00.viewpoint_00.x + this.refpointBaseTranslation_20.x,
      this.rview2_00.viewpoint_00.y + this.refpointBaseTranslation_20.y,
      this.rview2_00.viewpoint_00.z + this.refpointBaseTranslation_20.z
    );

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.refpointCallbackIndex_121 = 2;
      this.refpointMoving_123 = false;
    }
    //LAB_800d9f84
  }

  @Method(0x800d9f94L)
  public void FUN_800d9f94() {
    this.refpointAngleX_38 += this.stepX_3c;
    this.refpointAngleY_44 += this.stepY_48;
    this.refpointDeltaMagnitude_2c += this.stepZ_54;
    final Vector3f v1 = new Vector3f(this.refpointAngleX_38, this.refpointAngleY_44, this.refpointDeltaMagnitude_2c);
    this.FUN_800dcc94(this.rview2_00.viewpoint_00, v1);
    this.setRefpoint(v1.x, v1.y, v1.z);

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.refpointCallbackIndex_121 = 3;
      this.refpointMoving_123 = false;
    }
    //LAB_800da048
  }

  @Method(0x800da058L)
  public void FUN_800da058() {
    this.refpointBaseTranslation_20.x += this.stepX_3c;
    this.refpointBaseTranslation_20.y += this.stepY_48;
    this.refpointBaseTranslation_20.z += this.stepZ_54;

    final Vector3f pos = this.refpointBobj_80.getPosition();
    this.setRefpoint(pos.x + this.refpointBaseTranslation_20.x, pos.y + this.refpointBaseTranslation_20.y, pos.z + this.refpointBaseTranslation_20.z);

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.refpointCallbackIndex_121 = 6;
      this.refpointMoving_123 = false;
    }
    //LAB_800da100
  }

  @Method(0x800da110L)
  public void FUN_800da110() {
    this.refpointAngleX_38 += this.stepX_3c;
    this.refpointAngleY_44 += this.stepY_48;
    this.refpointDeltaMagnitude_2c += this.stepZ_54;

    final Vector3f v1 = new Vector3f(this.refpointAngleX_38, this.refpointAngleY_44, this.refpointDeltaMagnitude_2c);
    this.FUN_800dcc94(this.refpointBobj_80.getPosition(), v1);
    this.setRefpoint(v1.x, v1.y, v1.z);

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.refpointCallbackIndex_121 = 7;
      this.refpointMoving_123 = false;
    }
    //LAB_800da1e0
  }

  @Method(0x800da1f0L)
  public void FUN_800da1f0() {
    this.cameraRotationVector_800fab98.x = this.vec_60.x;
    this.cameraRotationVector_800fab98.y = this.vec_60.y;
    this.cameraRotationVector_800fab98.z = 0.0f;
    this.cameraTransformMatrix_800c6798.rotationXYZ(this.cameraRotationVector_800fab98);
    this.stepZ_30 += this.stepZAcceleration_40;
    this.vec_60.z -= this.stepZ_30;
    this.temp1_800faba0.set(0.0f, 0.0f, this.vec_60.z).mul(this.cameraTransformMatrix_800c6798, this.temp2_800faba8);
    this.temp2_800faba8.add(this.cameraTransformMatrix_800c6798.transfer);
    this.refpointBaseTranslation_20.x = this.refpointTargetTranslation_74.x - this.temp2_800faba8.z;
    this.refpointBaseTranslation_20.y = this.refpointTargetTranslation_74.y - this.temp2_800faba8.x;
    this.refpointBaseTranslation_20.z = this.refpointTargetTranslation_74.z + this.temp2_800faba8.y;
    this.setRefpoint(this.refpointBaseTranslation_20.x, this.refpointBaseTranslation_20.y, this.refpointBaseTranslation_20.z);

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.flags_11c &= ~UPDATE_REFPOINT;
      this.refpointMoving_123 = false;
    }
    //LAB_800da310
  }

  @Method(0x800da328L)
  public void FUN_800da328() {
    final Vector3f v1 = new Vector3f(this.vec_60);
    this.FUN_800dcc94(ZERO, v1);

    this.refpointDeltaMagnitude_2c += this.stepZ_30;
    v1.x += this.refpointTargetTranslation_74.x;
    v1.y = v1.z + this.refpointTargetTranslation_74.y;
    v1.z = this.refpointDeltaMagnitude_2c;
    this.FUN_800dcc94(ZERO, v1);
    this.setRefpoint(v1.x, v1.y, v1.z);

    this.stepZ_6c += this.stepZAcceleration_70;
    this.vec_60.z -= this.stepZ_6c;
    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.flags_11c &= ~UPDATE_REFPOINT;
      this.refpointMoving_123 = false;
    }
    //LAB_800da444
  }

  @Method(0x800da460L)
  public void FUN_800da460() {
    this.cameraRotationVector_800fab98.x = this.vec_60.x;
    this.cameraRotationVector_800fab98.y = this.vec_60.y;
    this.cameraRotationVector_800fab98.z = 0.0f;

    this.cameraTransformMatrix_800c6798.rotationXYZ(this.cameraRotationVector_800fab98);

    this.stepZ_30 += this.stepZAcceleration_40;
    this.vec_60.z -= this.stepZ_30;
    this.temp1_800faba0.set(0.0f, 0.0f, this.vec_60.z).mul(this.cameraTransformMatrix_800c6798, this.temp2_800faba8);
    this.temp2_800faba8.add(this.cameraTransformMatrix_800c6798.transfer);

    this.refpointBaseTranslation_20.x = this.refpointTargetTranslation_74.x - this.temp2_800faba8.z;
    this.refpointBaseTranslation_20.y = this.refpointTargetTranslation_74.y - this.temp2_800faba8.x;
    this.refpointBaseTranslation_20.z = this.refpointTargetTranslation_74.z + this.temp2_800faba8.y;

    this.setRefpoint(
      this.rview2_00.viewpoint_00.x + this.refpointBaseTranslation_20.x,
      this.rview2_00.viewpoint_00.y + this.refpointBaseTranslation_20.y,
      this.rview2_00.viewpoint_00.z + this.refpointBaseTranslation_20.z
    );

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.refpointCallbackIndex_121 = 2;
      this.refpointMoving_123 = false;
    }
    //LAB_800da594
  }

  @Method(0x800da5b0L)
  public void FUN_800da5b0() {
    final Vector3f v1 = new Vector3f(this.vec_60);
    this.FUN_800dcc94(ZERO, v1);

    this.refpointDeltaMagnitude_2c += this.stepZ_30;
    v1.x += this.refpointTargetTranslation_74.x;
    v1.y = v1.z + this.refpointTargetTranslation_74.y;
    v1.z = this.refpointDeltaMagnitude_2c;
    this.FUN_800dcc94(ZERO, v1);
    this.setRefpoint(this.rview2_00.viewpoint_00.x + v1.x, this.rview2_00.viewpoint_00.y + v1.y, this.rview2_00.viewpoint_00.z + v1.z);

    this.stepZ_6c += this.stepZAcceleration_70;
    this.vec_60.z -= this.stepZ_6c;

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.refpointAngleX_38 = this.calculateCameraValue(true, 3, 0, null);
      this.refpointAngleY_44 = this.calculateCameraValue(true, 3, 1, null);
      this.refpointDeltaMagnitude_2c = this.calculateCameraValue(true, 3, 2, null);
      this.refpointCallbackIndex_121 = 3;
      this.refpointMoving_123 = false;
    }
    //LAB_800da730
  }

  @Method(0x800da750L)
  public void FUN_800da750() {
    this.stepZ_30 += this.stepZAcceleration_40;
    this.vec_60.z -= this.stepZ_30;

    this.cameraRotationVector_800fab98.x = this.vec_60.x;
    this.cameraRotationVector_800fab98.y = this.vec_60.y;
    this.cameraRotationVector_800fab98.z = 0.0f;

    this.cameraTransformMatrix_800c6798.rotationXYZ(this.cameraRotationVector_800fab98);

    this.temp1_800faba0.set(0.0f, 0.0f, this.vec_60.z).mul(this.cameraTransformMatrix_800c6798, this.temp2_800faba8);
    this.temp2_800faba8.add(this.cameraTransformMatrix_800c6798.transfer);

    this.refpointBaseTranslation_20.x = this.refpointTargetTranslation_74.x - this.temp2_800faba8.z;
    this.refpointBaseTranslation_20.y = this.refpointTargetTranslation_74.y - this.temp2_800faba8.x;
    this.refpointBaseTranslation_20.z = this.refpointTargetTranslation_74.z + this.temp2_800faba8.y;

    final Vector3f pos = this.refpointBobj_80.getPosition();
    this.setRefpoint(
      pos.x + this.refpointBaseTranslation_20.x,
      pos.y + this.refpointBaseTranslation_20.y,
      pos.z + this.refpointBaseTranslation_20.z
    );

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.refpointCallbackIndex_121 = 6;
      this.refpointMoving_123 = false;
    }
    //LAB_800da8a0
  }

  @Method(0x800da8bcL)
  public void FUN_800da8bc() {
    final Vector3f ref = new Vector3f().set(this.vec_60);
    this.FUN_800dcc94(ZERO, ref);

    this.refpointDeltaMagnitude_2c += this.stepZ_30;
    ref.x += this.refpointTargetTranslation_74.x;
    ref.y = ref.z + this.refpointTargetTranslation_74.y;
    ref.z = this.refpointDeltaMagnitude_2c;
    this.FUN_800dcc94(ZERO, ref);

    final Vector3f pos = this.refpointBobj_80.getPosition();
    this.setRefpoint(pos.x + ref.x, pos.y + ref.y, pos.z + ref.z);

    this.stepZ_6c += this.stepZAcceleration_70;
    this.vec_60.z -= this.stepZ_6c;

    this.refpointTicksRemaining_5c--;
    if(this.refpointTicksRemaining_5c <= 0) {
      this.calculate3dAngleOrMagnitude(pos, this.rview2_00.refpoint_0c, ref);
      this.refpointAngleX_38 = ref.x;
      this.refpointAngleY_44 = ref.y;
      this.refpointDeltaMagnitude_2c = ref.z;
      this.refpointCallbackIndex_121 = 7;
      this.refpointMoving_123 = false;
    }
    //LAB_800daa60
  }

  @Method(0x800daa80L)
  public void wobbleCamera() {
    if(this.useCameraWobble_800fabb8) {
      if(this.framesUntilWobble_800c67d4 != 0) {
        this.framesUntilWobble_800c67d4--;
        return;
      }

      //LAB_800daabc
      final int x;
      final int y;
      final int type = tickCount_800bb0fc & 0x3;
      if(type == 0) {
        //LAB_800dab04
        x = this.cameraWobbleOffsetX_800c67e4;
        y = this.cameraWobbleOffsetY_800c67e8 * 2;
      } else if(type == 1) {
        //LAB_800dab1c
        x = -this.cameraWobbleOffsetX_800c67e4 * 2;
        y = -this.cameraWobbleOffsetY_800c67e8;
        //LAB_800daaec
      } else if(type == 2) {
        //LAB_800dab3c
        x = this.cameraWobbleOffsetX_800c67e4 * 2;
        y = this.cameraWobbleOffsetY_800c67e8;
      } else {
        //LAB_800dab54
        x = -this.cameraWobbleOffsetX_800c67e4;
        y = -this.cameraWobbleOffsetY_800c67e8 * 2;
      }

      //LAB_800dab70
      //LAB_800dab78
      GTE.setScreenOffset(this.screenOffset_800c67bc.x + x, this.screenOffset_800c67bc.y + y);

      this.wobbleFramesRemaining_800c67c4--;
      if(this.wobbleFramesRemaining_800c67c4 <= 0) {
        this.useCameraWobble_800fabb8 = false;
        GTE.setScreenOffset(this.screenOffset_800c67bc.x, this.screenOffset_800c67bc.y);
      }
    }

    //LAB_800dabb8
  }

  @Method(0x800dabecL)
  public void resetCameraMovement() {
    this.projectionPlaneChangeFrames_108 = 0;
    this.projectionPlaneChanging_118 = false;
    this.flags_11c = 0;
    this.viewpointCallbackIndex_120 = 0;
    this.refpointCallbackIndex_121 = 0;
    this.viewpointMoving_122 = false;
    this.refpointMoving_123 = false;
  }

  @Method(0x800dac70L)
  public void FUN_800dac70(final int index, final float x, final float y, final float z, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fabbc, FUN index=%d, x=%f, y=%f, z=%f, bobj=%s", index, x, y, z, bobj);
    this.viewpointSetFromScriptMethods_800fabbc[index].accept(x, y, z, bobj);
    this.viewpointCallbackIndex_fc = index;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dacc4L)
  public void setViewpointFromScriptTranslation(final float x, final float y, final float z, final BattleObject bobj) {
    this.viewpointBaseTranslation_94.set(x, y, z);
    this.setViewpoint(x, y, z);
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 0;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dad14L)
  public void setViewpointFromScriptAngle(final float x, final float y, final float z, final BattleObject bobj) {
    this.viewpointAngleX_ac = x;
    this.viewpointAngleY_b8 = y;
    this.viewpointDeltaMagnitude_a0 = z;
    final Vector3f v1 = new Vector3f(x, y, z);
    this.FUN_800dcc94(ZERO, v1);
    this.setViewpoint(v1.x, v1.y, v1.z);

    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 1;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dadc0L)
  public void setViewpointFromScriptTranslationNoOp(final float x, final float y, final float z, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dadc8L)
  public void setViewpointFromScriptAngleNoOp(final float x, final float y, final float z, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dadd0L)
  public void setViewpointFromScriptTranslationRelativeToRefpoint(final float x, final float y, final float z, final BattleObject bobj) {
    this.viewpointBaseTranslation_94.set(x, y, z);

    this.setViewpoint(
      this.rview2_00.refpoint_0c.x + x,
      this.rview2_00.refpoint_0c.y + y,
      this.rview2_00.refpoint_0c.z + z
    );

    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 4;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dae3cL)
  public void setViewpointFromScriptAngleRelativeToRefpoint(final float x, final float y, final float z, final BattleObject bobj) {
    this.viewpointDeltaMagnitude_a0 = z;
    this.viewpointAngleX_ac = x;
    this.viewpointAngleY_b8 = y;
    final Vector3f v1 = new Vector3f(x, y, z);
    this.FUN_800dcc94(this.rview2_00.refpoint_0c, v1);
    this.setViewpoint(v1.x, v1.y, v1.z);
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 5;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800daedcL)
  public void setViewpointFromScriptTranslationRelativeToObject(final float x, final float y, final float z, final BattleObject bobj) {
    this.viewpointBaseTranslation_94.set(x, y, z);

    final Vector3f v0 = bobj.getPosition();

    this.setViewpoint(
      v0.x + this.viewpointBaseTranslation_94.x,
      v0.y + this.viewpointBaseTranslation_94.y,
      v0.z + this.viewpointBaseTranslation_94.z
    );

    this.viewpointBobj_f4 = bobj;
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 6;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800daf6cL)
  public void setViewpointFromScriptAngleRelativeToObject(final float x, final float y, final float z, final BattleObject bobj) {
    this.viewpointAngleX_ac = x;
    this.viewpointAngleY_b8 = y;
    this.viewpointDeltaMagnitude_a0 = z;
    final Vector3f v1 = new Vector3f(x, y, z);
    this.FUN_800dcc94(bobj.getPosition(), v1);
    this.setViewpoint(v1.x, v1.y, v1.z);
    this.viewpointBobj_f4 = bobj;
    this.flags_11c |= UPDATE_VIEWPOINT;
    this.viewpointCallbackIndex_120 = 7;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db084L)
  public void FUN_800db084(final int index, final float x, final float y, final float z, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fabdc, FUN index=%d, x=%f, y=%f, z=%f, bobj=%s", index, x, y, z, bobj);
    this.refpointSetFromScriptMethods_800fabdc[index].accept(x, y, z, bobj);
    this.refpointCallbackIndex_88 = index;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db0d8L)
  public void setRefpointFromScriptTranslation(final float x, final float y, final float z, final BattleObject bobj) {
    this.refpointBaseTranslation_20.set(x, y, z);
    this.setRefpoint(x, y, z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 0;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db128L)
  public void setRefpointFromScriptAngle(final float x, final float y, final float z, final BattleObject bobj) {
    this.refpointDeltaMagnitude_2c = z;
    this.refpointAngleX_38 = x;
    this.refpointAngleY_44 = y;
    final Vector3f v1 = new Vector3f(x, y, z);
    this.FUN_800dcc94(ZERO, v1);
    this.setRefpoint(v1.x, v1.y, v1.z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 1;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db1d4L)
  public void setRefpointFromScriptTranslationRelativeToViewpoint(final float x, final float y, final float z, final BattleObject bobj) {
    this.refpointBaseTranslation_20.set(x, y, z);
    this.setRefpoint(
      this.rview2_00.viewpoint_00.x + x,
      this.rview2_00.viewpoint_00.y + y,
      this.rview2_00.viewpoint_00.z + z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 2;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db240L)
  public void setRefpointFromScriptAngleRelativeToViewpoint(final float x, final float y, final float z, final BattleObject bobj) {
    this.refpointAngleX_38 = x;
    this.refpointAngleY_44 = y;
    this.refpointDeltaMagnitude_2c = z;
    final Vector3f v1 = new Vector3f(this.refpointAngleX_38, this.refpointAngleY_44, this.refpointDeltaMagnitude_2c);
    this.FUN_800dcc94(this.rview2_00.viewpoint_00, v1);
    this.setRefpoint(v1.x, v1.y, v1.z);
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 3;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db2e0L)
  public void setRefpointFromScriptTranslationNoOp(final float x, final float y, final float z, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db2e8L)
  public void setRefpointFromScriptAngleNoOp(final float x, final float y, final float z, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db2f0L)
  public void setRefpointFromScriptTranslationRelativeToObject(final float x, final float y, final float z, final BattleObject bobj) {
    this.refpointBaseTranslation_20.set(x, y, z);

    final Vector3f v0 = bobj.getPosition();
    this.setRefpoint(
      v0.x + x,
      v0.y + y,
      v0.z + z
    );

    this.refpointBobj_80 = bobj;
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 6;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db398L)
  public void setRefpointFromScriptAngleRelativeToObject(final float x, final float y, final float z, final BattleObject bobj) {
    this.refpointAngleX_38 = x;
    this.refpointAngleY_44 = y;
    this.refpointDeltaMagnitude_2c = z;
    final Vector3f v1 = new Vector3f(x, y, z);
    this.FUN_800dcc94(bobj.getPosition(), v1);
    this.setRefpoint(v1.x, v1.y, v1.z);
    this.refpointBobj_80 = bobj;
    this.flags_11c |= UPDATE_REFPOINT;
    this.refpointCallbackIndex_121 = 7;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db4ecL)
  public void FUN_800db4ec(final int callbackIndex, final float x, final float y, final float z, final int a4, final int ticks, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fabfc, FUN index=%d, x=%f, y=%f, z=%f, a4=%d, ticks=%d, stepType=%d, bobj=%s", callbackIndex, x, y, z, a4, ticks, stepType, bobj);
    this._800fabfc[callbackIndex].accept(x, y, z, ticks, stepType, a4, bobj);
    this.viewpointCallbackIndex_fc = callbackIndex;
    this.viewpointMoving_122 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db564L)
  public void FUN_800db564(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db56cL)
  public void FUN_800db56c(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    // no-op
  }

  @Method(0x800db600L)
  public void FUN_800db600(final int callbackIndex, final float x, final float y, final float z, final int a4, final int ticks, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fac5c, FUN index=%d, x=%f, y=%f, z=%f, ticks=%d, stepType=%d, a4=%d, bobj=%s", callbackIndex, x, y, z, ticks, stepType, a4, bobj);
    this._800fac5c[callbackIndex].accept(x, y, z, ticks, stepType, a4, bobj);
    this.refpointCallbackIndex_88 = callbackIndex;
    this.refpointMoving_123 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db678L)
  public void FUN_800db678(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db680L)
  public void FUN_800db680(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db714L)
  public void FUN_800db714(final int callbackIndex, final float x, final float y, final float z, final float initialStepZ, final float finalStepZ, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fac1c, FUN index=%d, x=%f, y=%f, z=%f, initialStepZ=%f, finalStepZ=%f, stepType=%d, bobj=%s", callbackIndex, x, y, z, initialStepZ, finalStepZ, stepType, bobj);
    this._800fac1c[callbackIndex].accept(x, y, z, initialStepZ, finalStepZ, stepType, bobj);
    this.viewpointCallbackIndex_fc = callbackIndex;
    this.viewpointMoving_122 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db828L)
  public void FUN_800db828(final int callbackIndex, final float x, final float y, final float z, final float initialStepZ, final float finalStepZ, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fac7c, FUN index=%d, x=%f, y=%f, z=%f, initialStepZ=%f, finalStepZ=%f, stepType=%d, scriptIndex=%d", callbackIndex, x, y, z, initialStepZ, finalStepZ, stepType, bobj);
    this._800fac7c[callbackIndex].accept(x, y, z, initialStepZ, finalStepZ, stepType, bobj);
    this.refpointCallbackIndex_88 = callbackIndex;
    this.refpointMoving_123 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db950L)
  public void FUN_800db950(final int callbackIndex, final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fac3c, FUN index=%d, x=%f, y=%f, z=%f, ticks=%d, stepSmoothingMode=%d, stepZ=%f, stepType=%d, bobj=%s", callbackIndex, x, y, z, ticks, stepSmoothingMode, stepZ, stepType, bobj);
    this._800fac3c[callbackIndex].accept(x, y, z, ticks, stepSmoothingMode, stepZ, stepType, bobj);
    this.viewpointCallbackIndex_fc = callbackIndex;
    this.viewpointMoving_122 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db9d0L)
  public void FUN_800db9d0(final float x, final float y, final float z, final int ticks, final int a4, final float a5, final int stepType, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db9d8L)
  public void FUN_800db9d8(final float x, final float y, final float z, final int ticks, final int a4, final float a5, final int stepType, final BattleObject bobj) {
    // no-op
  }

  @Method(0x800dba80L)
  public void FUN_800dba80(final int callbackIndex, final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fac9c, FUN index=%d, x=%f, y=%f, z=%f, ticks=%d, stepSmoothingMode=%d, stepZ=%f, stepType=%d, bobj=%s", callbackIndex, x, y, z, ticks, stepSmoothingMode, stepZ, stepType, bobj);
    this._800fac9c[callbackIndex].accept(x, y, z, ticks, stepSmoothingMode, stepZ, stepType, bobj);
    this.refpointCallbackIndex_88 = callbackIndex;
    this.refpointMoving_123 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dbb00L)
  public void FUN_800dbb00(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dbb08L)
  public void FUN_800dbb08(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    // no-op
  }

  @Method(0x800dbe40L)
  public void FUN_800dbe40() {
    this.flags_11c &= ~UPDATE_VIEWPOINT;
    this.viewpointMoving_122 = false;
  }

  @Method(0x800dbe60L)
  public void FUN_800dbe60() {
    this.flags_11c &= ~UPDATE_VIEWPOINT;
    this.viewpointMoving_122 = false;
  }

  @Method(0x800dbe80L)
  public void FUN_800dbe80() {
    this.viewpointMoving_122 = false;
  }

  @Method(0x800dbe8cL)
  public void FUN_800dbe8c() {
    this.viewpointMoving_122 = false;
  }

  @Method(0x800dbe98L)
  public void FUN_800dbe98() {
    this.viewpointMoving_122 = false;

    this.setViewpoint(
      this.rview2_00.refpoint_0c.x + this.viewpointBaseTranslation_94.x,
      this.rview2_00.refpoint_0c.y + this.viewpointBaseTranslation_94.y,
      this.rview2_00.refpoint_0c.z + this.viewpointBaseTranslation_94.z
    );
  }

  @Method(0x800dbef0L)
  public void FUN_800dbef0() {
    this.viewpointMoving_122 = false;
    final Vector3f v1 = new Vector3f(this.viewpointAngleX_ac, this.viewpointAngleY_b8, this.viewpointDeltaMagnitude_a0);
    this.FUN_800dcc94(this.rview2_00.refpoint_0c, v1);
    this.setViewpoint(v1.x, v1.y, v1.z);
  }

  @Method(0x800dbf70L)
  public void FUN_800dbf70() {
    this.viewpointMoving_122 = false;

    final Vector3f v0 = this.viewpointBobj_f4.getPosition();
    this.setViewpoint(v0.x + this.viewpointBaseTranslation_94.x, v0.y + this.viewpointBaseTranslation_94.y, v0.z + this.viewpointBaseTranslation_94.z);
  }

  @Method(0x800dbfd4L)
  public void FUN_800dbfd4() {
    this.viewpointMoving_122 = false;

    final Vector3f v1 = new Vector3f(this.viewpointAngleX_ac, this.viewpointAngleY_b8, this.viewpointDeltaMagnitude_a0);
    this.FUN_800dcc94(this.viewpointBobj_f4.getPosition(), v1);
    this.setViewpoint(v1.x, v1.y, v1.z);
  }

  @Method(0x800dc070L)
  public void FUN_800dc070() {
    // no-op
  }

  @Method(0x800dc078L)
  public void FUN_800dc078() {
    // no-op
  }

  @Method(0x800dc080L)
  public void FUN_800dc080() {
    // no-op
  }

  @Method(0x800dc088L)
  public void FUN_800dc088() {
    // no-op
  }

  @Method(0x800dc090L)
  public void FUN_800dc090() {
    this.flags_11c &= ~UPDATE_REFPOINT;
    this.refpointMoving_123 = false;
  }

  @Method(0x800dc0b0L)
  public void FUN_800dc0b0() {
    this.flags_11c &= ~UPDATE_REFPOINT;
    this.refpointMoving_123 = false;
  }

  @Method(0x800dc0d0L)
  public void FUN_800dc0d0() {
    this.refpointMoving_123 = false;

    this.setRefpoint(
      this.rview2_00.viewpoint_00.x + this.refpointBaseTranslation_20.x,
      this.rview2_00.viewpoint_00.y + this.refpointBaseTranslation_20.y,
      this.rview2_00.viewpoint_00.z + this.refpointBaseTranslation_20.z
    );
  }

  @Method(0x800dc128L)
  public void FUN_800dc128() {
    this.refpointMoving_123 = false;

    final Vector3f v1 = new Vector3f(this.refpointAngleX_38, this.refpointAngleY_44, this.refpointDeltaMagnitude_2c);
    this.FUN_800dcc94(this.rview2_00.viewpoint_00, v1);
    this.setRefpoint(v1.x, v1.y, v1.z);
  }

  @Method(0x800dc1a8L)
  public void FUN_800dc1a8() {
    // no-op
  }

  @Method(0x800dc1b0L)
  public void FUN_800dc1b0() {
    // no-op
  }

  @Method(0x800dc1b8L)
  public void FUN_800dc1b8() {
    this.refpointMoving_123 = false;

    final Vector3f v0 = this.refpointBobj_80.getPosition();
    this.setRefpoint(
      v0.x + this.refpointBaseTranslation_20.x,
      v0.y + this.refpointBaseTranslation_20.y,
      v0.z + this.refpointBaseTranslation_20.z
    );
  }

  @Method(0x800dc21cL)
  public void FUN_800dc21c() {
    this.refpointMoving_123 = false;

    final Vector3f v1 = new Vector3f(this.refpointAngleX_38, this.refpointAngleY_44, this.refpointDeltaMagnitude_2c);
    this.FUN_800dcc94(this.refpointBobj_80.getPosition(), v1);
    this.setRefpoint(v1.x, v1.y, v1.z);
  }

  @Method(0x800dc2b8L)
  public static void FUN_800dc2b8() {
    // no-op
  }

  @Method(0x800dc2c0L)
  public static void FUN_800dc2c0() {
    // no-op
  }

  @Method(0x800dc2c8L)
  public static void FUN_800dc2c8() {
    // no-op
  }

  @Method(0x800dc2d0L)
  public static void FUN_800dc2d0() {
    // no-op
  }

  /**
   * @param callbackIndex <ol start="0">
   *                        <li>Raw component</li>
   *                        <li>Angle from 0 to raw component</li>
   *                        <li>Delta from viewpoint to component (only for viewpoint)</li>
   *                        <li>Angle from viewpoint to component (only for viewpoint)</li>
   *                        <li>Delta from refpoint to component (only for refpoint)</li>
   *                        <li>Angle from refpoint to component (only for refpoint)</li>
   *                        <li>Delta from scripted object to component</li>
   *                        <li>Angle from scripted object to component</li>
   *                      </ol>
   */
  @Method(0x800dc384L)
  public float calculateCameraValue(final boolean useRefpoint, final int callbackIndex, final int component, @Nullable final BattleObject bobj) {
    final Vector3f point;
    final ComponentFunction[] componentMethod;
    if(useRefpoint) {
      point = this.rview2_00.refpoint_0c;
      componentMethod = this.viewpointComponentMethods_800fad9c;
    } else {
      point = this.rview2_00.viewpoint_00;
      componentMethod = this.refpointComponentMethods_800fad7c;
    }

    return componentMethod[callbackIndex].apply(component, bobj, point);
  }

  @Method(0x800dc408L)
  public float refpointRawComponent(final int component, final BattleObject bobj, final Vector3f point) {
    if(component == 0) {
      return point.x;
    }

    if(component == 1) {
      return point.y;
    }

    if(component == 2) {
      return point.z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc45cL)
  public float refpointAngleFrom0ToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return this.calculate3dAngleOrMagnitude(ZERO, point, component);
  }

  @Method(0x800dc504L)
  public float refpointNoop1(final int component, final BattleObject bobj, final Vector3f point) {
    // no-op
    return 0;
  }

  @Method(0x800dc50cL)
  public float refpointNoop2(final int component, final BattleObject bobj, final Vector3f point) {
    // no-op
    return 0;
  }

  @Method(0x800dc514L)
  public float refpointDeltaFromRefpointToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    if(component == 0) {
      return point.x - this.rview2_00.refpoint_0c.x;
    }

    if(component == 1) {
      return point.y - this.rview2_00.refpoint_0c.y;
    }

    if(component == 2) {
      return point.z - this.rview2_00.refpoint_0c.z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc580L)
  public float refpointAngleFromRefpointToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return this.calculate3dAngleOrMagnitude(this.rview2_00.refpoint_0c, point, component);
  }

  @Method(0x800dc630L)
  public float refpointDeltaFromScriptedObjToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    final Vector3f vec = bobj.getPosition();

    if(component == 0) {
      return point.x - vec.x;
    }

    if(component == 1) {
      return point.y - vec.y;
    }

    if(component == 2) {
      return point.z - vec.z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc6d8L)
  public float refpointAngleFromScriptedObjToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return this.calculate3dAngleOrMagnitude(bobj.getPosition(), point, component);
  }

  @Method(0x800dc798L)
  public float viewpointRawComponent(final int component, final BattleObject bobj, final Vector3f point) {
    if(component == 0) {
      return point.x;
    }

    if(component == 1) {
      return point.y;
    }

    if(component == 2) {
      return point.z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc7ecL)
  public float viewpointAngleFrom0ToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return this.calculate3dAngleOrMagnitude(ZERO, point, component);
  }

  @Method(0x800dc894L)
  public float viewpointDeltaFromViewpointToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    if(component == 0) {
      return point.x - this.rview2_00.viewpoint_00.x;
    }

    if(component == 1) {
      return point.y - this.rview2_00.viewpoint_00.y;
    }

    if(component == 2) {
      return point.z - this.rview2_00.viewpoint_00.z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc900L)
  public float viewpointAngleFromViewpointToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return this.calculate3dAngleOrMagnitude(this.rview2_00.viewpoint_00, point, component);
  }

  @Method(0x800dc9b0L)
  public float viewpointNoop1(final int component, final BattleObject bobj, final Vector3f point) {
    // no-op
    return 0;
  }

  @Method(0x800dc9b8L)
  public float viewpointNoop2(final int component, final BattleObject bobj, final Vector3f point) {
    // no-op
    return 0;
  }

  @Method(0x800dc9c0L)
  public float viewpointDeltaFromScriptedObjToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    final Vector3f objPos = bobj.getPosition();

    if(component == 0) {
      return point.x - objPos.x;
    }

    if(component == 1) {
      return point.y - objPos.y;
    }

    if(component == 2) {
      return point.z - objPos.z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dca68L)
  public float viewpointAngleFromScriptedObjToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return this.calculate3dAngleOrMagnitude(bobj.getPosition(), point, component);
  }

  @Method(0x800dcc64L)
  public void setViewpoint(final float x, final float y, final float z) {
    this.rview2_00.viewpoint_00.set(x, y, z);
  }

  @Method(0x800dcc7cL)
  public void setRefpoint(final float x, final float y, final float z) {
    this.rview2_00.refpoint_0c.set(x, y, z);
  }

  @Method(0x800dcc94L)
  public void FUN_800dcc94(final Vector3f v0, final Vector3f v1) {
    this.cameraRotationVector_800fab98.set(v1.x, v1.y, 0.0f);
    this.cameraTransformMatrix_800c6798.rotationXYZ(this.cameraRotationVector_800fab98);
    this.temp1_800faba0.set(0.0f, 0.0f, v1.z).mul(this.cameraTransformMatrix_800c6798, this.temp2_800faba8);
    this.temp2_800faba8.add(this.cameraTransformMatrix_800c6798.transfer);
    v1.x = v0.x - this.temp2_800faba8.z;
    v1.y = v0.y - this.temp2_800faba8.x;
    v1.z = v0.z + this.temp2_800faba8.y;
  }

  /**
   * @return Component 0/1 are angles, 2 is the magnitude of the delta vector
   */
  @Method(0x800dcd9cL)
  public float calculate3dAngleOrMagnitude(final Vector3f pos0, final Vector3f pos1, final int component) {
    final float dx = pos0.x - pos1.x;
    final float dy = pos0.y - pos1.y;
    final float dz = pos0.z - pos1.z;
    final float horizontalHypotenuse = Math.sqrt(dx * dx + dz * dz);

    return switch(component) {
      case 0 -> MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI); // Angle between vector and +X axis
      case 1 -> MathHelper.floorMod(MathHelper.atan2(dy, horizontalHypotenuse), MathHelper.TWO_PI); // Angle between vector and +Y axis
      case 2 -> Math.sqrt(dx * dx + dy * dy + dz * dz); // Angle between vector and +Z axis?
      default -> throw new IllegalStateException("Illegal component " + component);
    };
  }

  /**
   * @param pos1 Returned x/y are angles, z is the magnitude of the delta vector
   */
  public void calculate3dAngleOrMagnitude(final Vector3f pos0, final Vector3f pos1, final Vector3f out) {
    final float dx = pos0.x - pos1.x;
    final float dy = pos0.y - pos1.y;
    final float dz = pos0.z - pos1.z;
    final float horizontalHypotenuse = Math.sqrt(dx * dx + dz * dz);

    out.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI); // Angle between vector and +X axis
    out.y = MathHelper.floorMod(MathHelper.atan2(dy, horizontalHypotenuse), MathHelper.TWO_PI); // Angle between vector and +Y axis
    out.z = Math.sqrt(dx * dx + dy * dy + dz * dz); // Angle between vector and +Z axis?
  }

  @Method(0x800dcebcL)
  public void setInitialAndFinalCameraVelocities(final int mode, final float stepZ1, final float stepZ2, final int divisor, final FloatRef initialStepZ, final FloatRef finalStepZ) {
    if(mode == 0) {
      //LAB_800dcedc
      initialStepZ.set(stepZ1);
      finalStepZ.set(stepZ2 * 2.0f / divisor - stepZ1);
    } else if(mode == 1) {
      //LAB_800dcef8
      initialStepZ.set(stepZ2 * 2.0f / divisor - stepZ1);
      finalStepZ.set(stepZ1);
    } else {
      throw new IllegalArgumentException("Invalid mode " + mode);
    }
  }

  /**
   * @param stepType <ul>
   *                   <li>0 - no step (always 0)</li>
   *                   <li>1 - positive step</li>
   *                   <li>other - negative step</li>
   *                 </ol>
   */
  @Method(0x800dcf10L)
  public float calculateStep(final int component, final float val1, final float val2, final int divisor, final int stepType) {
    if(component == 2) {
      //LAB_800dcfa4
      //LAB_800dcfb0
      return (val2 - val1) / divisor;
    }

    //LAB_800dcf38
    if(stepType == 0) {
      return 0;
    }

    //LAB_800dcf48
    if(stepType == 1) {
      return (val2 - val1) / divisor;
    }

    //LAB_800dcf58
    return (val2 - val1) / divisor;
  }

  /**
   * @param stepType <ul>
   *                   <li>0 - no step (always 0)</li>
   *                   <li>1 - positive step</li>
   *                   <li>other - negative step</li>
   *                 </ol>
   *
   * @return 8-bit fixed-point
   */
  @Method(0x800dcfb8L)
  public float calculateDifference(final int component, final float val1, final float val2, final int stepType) {
    if(component == 2) {
      //LAB_800dd020
      //LAB_800dd024
      return val2 - val1;
    }

    //LAB_800dcfdc
    if(stepType == 0) {
      return 0;
    }

    //LAB_800dcfec
    if(stepType == 1) {
      if(val2 >= val1) {
        //LAB_800dcffc
        return val2 - val1;
      }

      //LAB_800dd004
      //LAB_800dd008
      return val2 - val1 + MathHelper.TWO_PI;
    }

    //LAB_800dd010
    if(val2 < val1) {
      return val2 - val1;
    }

    return val2 - (val1 + MathHelper.TWO_PI);
  }

  @Method(0x800dd0d4L)
  public float calculateYAngleFromRefpointToViewpoint() {
    return this.refpointComponentMethods_800fad7c[5].apply(1, null, this.rview2_00.viewpoint_00);
  }

  @Method(0x800dd118L)
  public float calculateXAngleFromRefpointToViewpoint() {
    return this.refpointComponentMethods_800fad7c[5].apply(0, null, this.rview2_00.viewpoint_00);
  }

  /**
   * <ol start="0">
   *   <li>{@link #setViewpointFromScriptTranslation}</li>
   *   <li>{@link #setViewpointFromScriptAngle}</li>
   *   <li>{@link #setViewpointFromScriptTranslationNoOp}</li>
   *   <li>{@link #setViewpointFromScriptAngleNoOp}</li>
   *   <li>{@link #setViewpointFromScriptTranslationRelativeToRefpoint}</li>
   *   <li>{@link #setViewpointFromScriptAngleRelativeToRefpoint}</li>
   *   <li>{@link #setViewpointFromScriptTranslationRelativeToObject}</li>
   *   <li>{@link #setViewpointFromScriptAngleRelativeToObject}</li>
   * </ol>
   */
  private final CameraQuadParamCallback[] viewpointSetFromScriptMethods_800fabbc = new CameraQuadParamCallback[8];
  {
    this.viewpointSetFromScriptMethods_800fabbc[0] = this::setViewpointFromScriptTranslation;
    this.viewpointSetFromScriptMethods_800fabbc[1] = this::setViewpointFromScriptAngle;
    this.viewpointSetFromScriptMethods_800fabbc[2] = this::setViewpointFromScriptTranslationNoOp;
    this.viewpointSetFromScriptMethods_800fabbc[3] = this::setViewpointFromScriptAngleNoOp;
    this.viewpointSetFromScriptMethods_800fabbc[4] = this::setViewpointFromScriptTranslationRelativeToRefpoint;
    this.viewpointSetFromScriptMethods_800fabbc[5] = this::setViewpointFromScriptAngleRelativeToRefpoint;
    this.viewpointSetFromScriptMethods_800fabbc[6] = this::setViewpointFromScriptTranslationRelativeToObject;
    this.viewpointSetFromScriptMethods_800fabbc[7] = this::setViewpointFromScriptAngleRelativeToObject;
  }
  /**
   * <ol start="0">
   *   <li>{@link #setRefpointFromScriptTranslation}</li>
   *   <li>{@link #setRefpointFromScriptAngle}</li>
   *   <li>{@link #setRefpointFromScriptTranslationRelativeToViewpoint}</li>
   *   <li>{@link #setRefpointFromScriptAngleRelativeToViewpoint}</li>
   *   <li>{@link #setRefpointFromScriptTranslationNoOp}</li>
   *   <li>{@link #setRefpointFromScriptAngleNoOp}</li>
   *   <li>{@link #setRefpointFromScriptTranslationRelativeToObject}</li>
   *   <li>{@link #setRefpointFromScriptAngleRelativeToObject}</li>
   * </ol>
   */
  private final CameraQuadParamCallback[] refpointSetFromScriptMethods_800fabdc = new CameraQuadParamCallback[8];
  {
    this.refpointSetFromScriptMethods_800fabdc[0] = this::setRefpointFromScriptTranslation;
    this.refpointSetFromScriptMethods_800fabdc[1] = this::setRefpointFromScriptAngle;
    this.refpointSetFromScriptMethods_800fabdc[2] = this::setRefpointFromScriptTranslationRelativeToViewpoint;
    this.refpointSetFromScriptMethods_800fabdc[3] = this::setRefpointFromScriptAngleRelativeToViewpoint;
    this.refpointSetFromScriptMethods_800fabdc[4] = this::setRefpointFromScriptTranslationNoOp;
    this.refpointSetFromScriptMethods_800fabdc[5] = this::setRefpointFromScriptAngleNoOp;
    this.refpointSetFromScriptMethods_800fabdc[6] = this::setRefpointFromScriptTranslationRelativeToObject;
    this.refpointSetFromScriptMethods_800fabdc[7] = this::setRefpointFromScriptAngleRelativeToObject;
  }
  /**
   * <ol start="0">
   *   <li>{@link #FUN_800d47dc}</li>
   *   <li>{@link #FUN_800d496c}</li>
   *   <li>{@link #FUN_800db564}</li>
   *   <li>{@link #FUN_800db56c}</li>
   *   <li>{@link #FUN_800d4bac}</li>
   *   <li>{@link #FUN_800d4d7c}</li>
   *   <li>{@link #FUN_800d4fbc}</li>
   *   <li>{@link #FUN_800d519c}</li>
   * </ol>
   */
  private final CameraSeptParamCallback[] _800fabfc = new CameraSeptParamCallback[8];
  {
    this._800fabfc[0] = this::FUN_800d47dc;
    this._800fabfc[1] = this::FUN_800d496c;
    this._800fabfc[2] = this::FUN_800db564;
    this._800fabfc[3] = this::FUN_800db56c;
    this._800fabfc[4] = this::FUN_800d4bac;
    this._800fabfc[5] = this::FUN_800d4d7c;
    this._800fabfc[6] = this::FUN_800d4fbc;
    this._800fabfc[7] = this::FUN_800d519c;
  }
  /**
   * <ol start="0">
   *   <li>{@link #FUN_800d53e4}</li>
   *   <li>{@link #FUN_800d5574}</li>
   *   <li>{@link #FUN_800db78c}</li>
   *   <li>{@link #FUN_800db794}</li>
   *   <li>{@link #FUN_800d5740}</li>
   *   <li>{@link #FUN_800d5930}</li>
   *   <li>{@link #FUN_800d5afc}</li>
   *   <li>{@link #FUN_800d5cf4}</li>
   * </ol>
   */
  private final CameraStepParamCallback[] _800fac1c = new CameraStepParamCallback[8];
  {
    this._800fac1c[0] = this::FUN_800d53e4;
    this._800fac1c[1] = null;
    this._800fac1c[2] = null;
    this._800fac1c[3] = null;
    this._800fac1c[4] = null;
    this._800fac1c[5] = null;
    this._800fac1c[6] = null;
    this._800fac1c[7] = null;
  }
  /**
   * <ol start="0">
   *   <li>{@link #FUN_800d5ec8}</li>
   *   <li>{@link #FUN_800d60b0}</li>
   *   <li>{@link #FUN_800db9d0}</li>
   *   <li>{@link #FUN_800db9d8}</li>
   *   <li>{@link #FUN_800d62d8}</li>
   *   <li>{@link #FUN_800d64e4}</li>
   *   <li>{@link #FUN_800d670c}</li>
   *   <li>{@link #FUN_800d6960}</li>
   * </ol>
   */
  private final CameraOctParamCallback[] _800fac3c = new CameraOctParamCallback[8];
  {
    this._800fac3c[0] = this::FUN_800d5ec8;
    this._800fac3c[1] = this::FUN_800d60b0;
    this._800fac3c[2] = this::FUN_800db9d0;
    this._800fac3c[3] = this::FUN_800db9d8;
    this._800fac3c[4] = this::FUN_800d62d8;
    this._800fac3c[5] = this::FUN_800d64e4;
    this._800fac3c[6] = this::FUN_800d670c;
    this._800fac3c[7] = this::FUN_800d6960;
  }
  /**
   * <ol start="0">
   *   <li>{@link #FUN_800d6b90}</li>
   *   <li>{@link #FUN_800d6d18}</li>
   *   <li>{@link #FUN_800d6f58}</li>
   *   <li>{@link #FUN_800d7128}</li>
   *   <li>{@link #FUN_800db678}</li>
   *   <li>{@link #FUN_800db680}</li>
   *   <li>{@link #FUN_800d7368}</li>
   *   <li>{@link #FUN_800d7548}</li>
   * </ol>
   */
  private final CameraSeptParamCallback[] _800fac5c = new CameraSeptParamCallback[8];
  {
    this._800fac5c[0] = this::FUN_800d6b90;
    this._800fac5c[1] = this::FUN_800d6d18;
    this._800fac5c[2] = this::FUN_800d6f58;
    this._800fac5c[3] = this::FUN_800d7128;
    this._800fac5c[4] = this::FUN_800db678;
    this._800fac5c[5] = this::FUN_800db680;
    this._800fac5c[6] = this::FUN_800d7368;
    this._800fac5c[7] = this::FUN_800d7548;
  }
  /**
   * <ol start="0">
   *   <li>{@link #FUN_800d7790}</li>
   *   <li>{@link #FUN_800d7920}</li>
   *   <li>{@link #FUN_800d7aec}</li>
   *   <li>{@link #FUN_800d7cdc}</li>
   *   <li>{@link #FUN_800db8a0}</li>
   *   <li>{@link #FUN_800db8a8}</li>
   *   <li>{@link #FUN_800d7ea8}</li>
   *   <li>{@link #FUN_800d80a0}</li>
   * </ol>
   */
  private final CameraStepParamCallback[] _800fac7c = new CameraStepParamCallback[8];
  {
    this._800fac7c[0] = this::FUN_800d7790;
    this._800fac7c[1] = this::FUN_800d7920;
    this._800fac7c[2] = this::FUN_800d7aec;
    this._800fac7c[3] = this::FUN_800d7cdc;
    this._800fac7c[4] = null;
    this._800fac7c[5] = null;
    this._800fac7c[6] = null;
    this._800fac7c[7] = null;
  }
  /**
   * <ol start="0">
   *   <li>{@link #FUN_800d8274}</li>
   *   <li>{@link #FUN_800d8424}</li>
   *   <li>{@link #FUN_800d8614}</li>
   *   <li>{@link #FUN_800d8808}</li>
   *   <li>{@link #FUN_800dbb00}</li>
   *   <li>{@link #FUN_800dbb08}</li>
   *   <li>{@link #FUN_800d89f8}</li>
   *   <li>{@link #FUN_800d8bf4}</li>
   * </ol>
   */
  private final CameraOctParamCallback[] _800fac9c = new CameraOctParamCallback[8];
  {
    this._800fac9c[0] = this::FUN_800d8274;
    this._800fac9c[1] = this::FUN_800d8424;
    this._800fac9c[2] = this::FUN_800d8614;
    this._800fac9c[3] = this::FUN_800d8808;
    this._800fac9c[4] = this::FUN_800dbb00;
    this._800fac9c[5] = this::FUN_800dbb08;
    this._800fac9c[6] = this::FUN_800d89f8;
    this._800fac9c[7] = this::FUN_800d8bf4;
  }
  /**
   * <ol start="0">
   *   <li>{@link #FUN_800dbe40}</li>
   *   <li>{@link #FUN_800dbe60}</li>
   *   <li>{@link #FUN_800dbe80}</li>
   *   <li>{@link #FUN_800dbe8c}</li>
   *   <li>{@link #FUN_800dbe98}</li>
   *   <li>{@link #FUN_800dbef0}</li>
   *   <li>{@link #FUN_800dbf70}</li>
   *   <li>{@link #FUN_800dbfd4}</li>
   *   <li>{@link #FUN_800d90c8}</li>
   *   <li>{@link #FUN_800d9154}</li>
   *   <li>{@link #FUN_800dc070}</li>
   *   <li>{@link #FUN_800dc078}</li>
   *   <li>{@link #FUN_800d9220}</li>
   *   <li>{@link #FUN_800d92bc}</li>
   *   <li>{@link #FUN_800d9380}</li>
   *   <li>{@link #FUN_800d9438}</li>
   *   <li>{@link #FUN_800d9518}</li>
   *   <li>{@link #FUN_800d9650}</li>
   *   <li>{@link #FUN_800dc080}</li>
   *   <li>{@link #FUN_800dc088}</li>
   *   <li>{@link #FUN_800d9788}</li>
   *   <li>{@link #FUN_800d98d0}</li>
   *   <li>{@link #FUN_800d9a68}</li>
   *   <li>{@link #FUN_800d9bd4}</li>
   * </ol>
   */
  private final Runnable[] cameraViewpointMethods_800facbc = new Runnable[24];
  {
    this.cameraViewpointMethods_800facbc[ 0] = this::FUN_800dbe40;
    this.cameraViewpointMethods_800facbc[ 1] = this::FUN_800dbe60;
    this.cameraViewpointMethods_800facbc[ 2] = this::FUN_800dbe80;
    this.cameraViewpointMethods_800facbc[ 3] = this::FUN_800dbe8c;
    this.cameraViewpointMethods_800facbc[ 4] = this::FUN_800dbe98;
    this.cameraViewpointMethods_800facbc[ 5] = this::FUN_800dbef0;
    this.cameraViewpointMethods_800facbc[ 6] = this::FUN_800dbf70;
    this.cameraViewpointMethods_800facbc[ 7] = this::FUN_800dbfd4;
    this.cameraViewpointMethods_800facbc[ 8] = this::FUN_800d90c8;
    this.cameraViewpointMethods_800facbc[ 9] = this::FUN_800d9154;
    this.cameraViewpointMethods_800facbc[10] = this::FUN_800dc070;
    this.cameraViewpointMethods_800facbc[11] = this::FUN_800dc078;
    this.cameraViewpointMethods_800facbc[12] = this::FUN_800d9220;
    this.cameraViewpointMethods_800facbc[13] = this::FUN_800d92bc;
    this.cameraViewpointMethods_800facbc[14] = this::FUN_800d9380;
    this.cameraViewpointMethods_800facbc[15] = this::FUN_800d9438;
    this.cameraViewpointMethods_800facbc[16] = this::FUN_800d9518;
    this.cameraViewpointMethods_800facbc[17] = this::FUN_800d9650;
    this.cameraViewpointMethods_800facbc[18] = this::FUN_800dc080;
    this.cameraViewpointMethods_800facbc[19] = this::FUN_800dc088;
    this.cameraViewpointMethods_800facbc[20] = this::FUN_800d9788;
    this.cameraViewpointMethods_800facbc[21] = this::FUN_800d98d0;
    this.cameraViewpointMethods_800facbc[22] = this::FUN_800d9a68;
    this.cameraViewpointMethods_800facbc[23] = this::FUN_800d9bd4;
  }
  /**
   * <ol start="0">
   *   <li>{@link #FUN_800dc090}</li>
   *   <li>{@link #FUN_800dc0b0}</li>
   *   <li>{@link #FUN_800dc0d0}</li>
   *   <li>{@link #FUN_800dc128}</li>
   *   <li>{@link #FUN_800dc1a8}</li>
   *   <li>{@link #FUN_800dc1b0}</li>
   *   <li>{@link #FUN_800dc1b8}</li>
   *   <li>{@link #FUN_800dc21c}</li>
   *   <li>{@link #FUN_800d9da0}</li>
   *   <li>{@link #FUN_800d9e2c}</li>
   *   <li>{@link #FUN_800d9ef8}</li>
   *   <li>{@link #FUN_800d9f94}</li>
   *   <li>{@link #FUN_800dc2b8}</li>
   *   <li>{@link #FUN_800dc2c0}</li>
   *   <li>{@link #FUN_800da058}</li>
   *   <li>{@link #FUN_800da110}</li>
   *   <li>{@link #FUN_800da1f0}</li>
   *   <li>{@link #FUN_800da328}</li>
   *   <li>{@link #FUN_800da460}</li>
   *   <li>{@link #FUN_800da5b0}</li>
   *   <li>{@link #FUN_800dc2c8}</li>
   *   <li>{@link #FUN_800dc2d0}</li>
   *   <li>{@link #FUN_800da750}</li>
   *   <li>{@link #FUN_800da8bc}</li>
   * </ol>
   */
  private final Runnable[] cameraRefpointMethods_800fad1c = new Runnable[24];
  {
    this.cameraRefpointMethods_800fad1c[ 0] = this::FUN_800dc090;
    this.cameraRefpointMethods_800fad1c[ 1] = this::FUN_800dc0b0;
    this.cameraRefpointMethods_800fad1c[ 2] = this::FUN_800dc0d0;
    this.cameraRefpointMethods_800fad1c[ 3] = this::FUN_800dc128;
    this.cameraRefpointMethods_800fad1c[ 4] = this::FUN_800dc1a8;
    this.cameraRefpointMethods_800fad1c[ 5] = this::FUN_800dc1b0;
    this.cameraRefpointMethods_800fad1c[ 6] = this::FUN_800dc1b8;
    this.cameraRefpointMethods_800fad1c[ 7] = this::FUN_800dc21c;
    this.cameraRefpointMethods_800fad1c[ 8] = this::FUN_800d9da0;
    this.cameraRefpointMethods_800fad1c[ 9] = this::FUN_800d9e2c;
    this.cameraRefpointMethods_800fad1c[10] = this::FUN_800d9ef8;
    this.cameraRefpointMethods_800fad1c[11] = this::FUN_800d9f94;
    this.cameraRefpointMethods_800fad1c[12] = BattleCamera::FUN_800dc2b8;
    this.cameraRefpointMethods_800fad1c[13] = BattleCamera::FUN_800dc2c0;
    this.cameraRefpointMethods_800fad1c[14] = this::FUN_800da058;
    this.cameraRefpointMethods_800fad1c[15] = this::FUN_800da110;
    this.cameraRefpointMethods_800fad1c[16] = this::FUN_800da1f0;
    this.cameraRefpointMethods_800fad1c[17] = this::FUN_800da328;
    this.cameraRefpointMethods_800fad1c[18] = this::FUN_800da460;
    this.cameraRefpointMethods_800fad1c[19] = this::FUN_800da5b0;
    this.cameraRefpointMethods_800fad1c[20] = BattleCamera::FUN_800dc2c8;
    this.cameraRefpointMethods_800fad1c[21] = BattleCamera::FUN_800dc2d0;
    this.cameraRefpointMethods_800fad1c[22] = this::FUN_800da750;
    this.cameraRefpointMethods_800fad1c[23] = this::FUN_800da8bc;
  }
  /**
   * <ol start="0">
   *   <li>{@link #refpointRawComponent}</li>
   *   <li>{@link #refpointAngleFrom0ToComponent}</li>
   *   <li>{@link #refpointNoop1}</li>
   *   <li>{@link #refpointNoop2}</li>
   *   <li>{@link #refpointDeltaFromRefpointToComponent}</li>
   *   <li>{@link #refpointAngleFromRefpointToComponent}</li>
   *   <li>{@link #refpointDeltaFromScriptedObjToComponent}</li>
   *   <li>{@link #refpointAngleFromScriptedObjToComponent}</li>
   * </ol>
   */
  private final ComponentFunction[] refpointComponentMethods_800fad7c = new ComponentFunction[8];
  {
    this.refpointComponentMethods_800fad7c[0] = this::refpointRawComponent;
    this.refpointComponentMethods_800fad7c[1] = this::refpointAngleFrom0ToComponent;
    this.refpointComponentMethods_800fad7c[2] = this::refpointNoop1;
    this.refpointComponentMethods_800fad7c[3] = this::refpointNoop2;
    this.refpointComponentMethods_800fad7c[4] = this::refpointDeltaFromRefpointToComponent;
    this.refpointComponentMethods_800fad7c[5] = this::refpointAngleFromRefpointToComponent;
    this.refpointComponentMethods_800fad7c[6] = this::refpointDeltaFromScriptedObjToComponent;
    this.refpointComponentMethods_800fad7c[7] = this::refpointAngleFromScriptedObjToComponent;
  }
  /**
   * <ol start="0">
   *   <li>{@link #viewpointRawComponent}</li>
   *   <li>{@link #viewpointAngleFrom0ToComponent}</li>
   *   <li>{@link #viewpointDeltaFromViewpointToComponent}</li>
   *   <li>{@link #viewpointAngleFromViewpointToComponent}</li>
   *   <li>{@link #viewpointNoop1}</li>
   *   <li>{@link #viewpointNoop2}</li>
   *   <li>{@link #viewpointDeltaFromScriptedObjToComponent}</li>
   *   <li>{@link #viewpointAngleFromScriptedObjToComponent}</li>
   * </ol>
   */
  private final ComponentFunction[] viewpointComponentMethods_800fad9c = new ComponentFunction[8];
  {
    this.viewpointComponentMethods_800fad9c[0] = this::viewpointRawComponent;
    this.viewpointComponentMethods_800fad9c[1] = this::viewpointAngleFrom0ToComponent;
    this.viewpointComponentMethods_800fad9c[2] = this::viewpointDeltaFromViewpointToComponent;
    this.viewpointComponentMethods_800fad9c[3] = this::viewpointAngleFromViewpointToComponent;
    this.viewpointComponentMethods_800fad9c[4] = this::viewpointNoop1;
    this.viewpointComponentMethods_800fad9c[5] = this::viewpointNoop2;
    this.viewpointComponentMethods_800fad9c[6] = this::viewpointDeltaFromScriptedObjToComponent;
    this.viewpointComponentMethods_800fad9c[7] = this::viewpointAngleFromScriptedObjToComponent;
  }
}
