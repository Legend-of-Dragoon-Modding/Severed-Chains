package legend.game.combat.environment;

import legend.game.types.GsRVIEW2;
import org.joml.Vector3f;

public class BattleCamera {
  public static final int UPDATE_VIEWPOINT = 0x1;
  public static final int UPDATE_REFPOINT = 0x2;

  public final GsRVIEW2 rview2_00 = new GsRVIEW2();
  /** 8-bit fixed-point */
  public final Vector3f vec_20 = new Vector3f();  // related to refpoint
  /** 8-bit fixed-point */
  public float z_2c;
  /** For vec_60, 8-bit fixed-point */
  public float stepZ_30;

  /** 8-bit fixed-point */
  public float x_38;
  /** For vec_20, 8-bit fixed-point */
  public float stepX_3c;  // refpoint X factor?
  /** For vec_20/stepZ_30, 8-bit fixed-point */
  public float stepZAcceleration_40;
  /** 8-bit fixed-point */
  public float y_44;
  /** For vec_20, 8-bit fixed-point */
  public float stepY_48;  // refpoint Y factor?

  /** For vec_20, 8-bit fixed-point */
  public float stepZ_54;  // refpoint Z factor?

  /** TODO should this be a float? */
  public int refpointTicksRemaining_5c;  // refpoint step count?
  /** 8-bit fixed-point */
  public final Vector3f vec_60 = new Vector3f();
  public float stepZ_6c;
  public float stepZAcceleration_70;
  public final Vector3f vec_74 = new Vector3f();
  public int bobjIndex_80;  // refpoint bobj index?

  public int callbackIndex_88;  // refpoint callback?

  /** 8-bit fixed-point */
  public final Vector3f vec_94 = new Vector3f();  // related to viewpoint
  public float z_a0; // something z
  /** For z_a0, 8-bit fixed-point */
  public float stepZ_a4; // camera rotation Z step?

  /** 8-bit fixed-point */
  public float x_ac; // something x
  /** For vec_94, 8-bit fixed-point */
  public float stepX_b0; // something x
  /** For zStep_a4, 8-bit fixed-point */
  public float stepZAcceleration_b4; // camera rotation Z step acceleration?
  /** 8-bit fixed-point */
  public float y_b8; // something y
  /** For vec_94, 8-bit fixed-point */
  public float stepY_bc; // something y

  /** For vec_94, 8-bit fixed-point */
  public float stepZ_c8; // something z

  /** TODO should this be a float? */
  public int viewpointTicksRemaining_d0;  // viewpoint step count?
  /** 8-bit fixed-point */
  public final Vector3f _d4 = new Vector3f();  // camera rotation?
  /** For _d4, 8-bit fixed-point */
  public float stepZ_e0;
  /** For _d4, 8-bit fixed-point */
  public float stepZAcceleration_e4;
  /** 8-bit fixed-point */
  public final Vector3f _e8 = new Vector3f();  // viewpoint factor?
  public int bobjIndex_f4;  // viewpoint bobj index?

  public int callbackIndex_fc;  // viewpoint callback?
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
}
