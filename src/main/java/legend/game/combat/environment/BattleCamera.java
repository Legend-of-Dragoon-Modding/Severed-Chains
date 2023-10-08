package legend.game.combat.environment;

import legend.game.combat.types.BattleObject;
import legend.game.types.GsRVIEW2;
import org.joml.Vector3f;

public class BattleCamera {
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
}
