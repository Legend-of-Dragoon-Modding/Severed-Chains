package legend.game.wmap;

import legend.core.gte.COLOUR;
import legend.core.gte.GsCOORDINATE2;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsRVIEW2;
import org.joml.Vector3f;

import java.util.Arrays;

public class WMapStruct19c0 {
  public final GsRVIEW2 rview2_00 = new GsRVIEW2();
  /** Parent coordinate system of camera; controls movement */
  public final GsCOORDINATE2 coord2_20 = new GsCOORDINATE2();
  /** Not in retail */
  public int mapRotationState;
  public final Vector3f currMapRotation_70 = new Vector3f();
  /** short */
  public float mapRotationStartAngle_78;
  /** short */
  public float mapRotationEndAngle_7a;
  /** short */
  public float mapRotationStep_7c;
  /** short */
  public int mapRotationCounter_7e;
  /** ubyte */
  public boolean mapRotating_80;

  public float lightsBrightness_84;
  public int lightsUpdateState_88;
  public final COLOUR[] lightsColours_8c = {new COLOUR(), new COLOUR(), new COLOUR()};
  /** short */
  public float finalMapRotation_98;
  /** short */
  public float originalMapRotation_9a;
  /** short */
  public float mapRotationStep_9c;
  /** short */
  public int finalCameraY_9e;
  /** short */
  public float cameraZoomTick_a0;
  public final Vector3f cameraZoomPosStep_a4 = new Vector3f();
  public final Vector3f currCameraZoomPos_b4 = new Vector3f();
  /** byte */
  public boolean hideAtmosphericEffect_c4;
  /** ubyte */
  public int cameraUpdateState_c5;

  public final GsRVIEW2 rview2_c8 = new GsRVIEW2();

  public float viewpointY_ec;
  public float viewpointZ_f0;

  public float refpointY_f8;
  public float refpointZ_fc;

  /** short */
  public float angle_108;
  /** short */
  public float angle_10a;
  /** short */
  public float angle_10c;
  /** ushort */
  public float _10e;
  /** ubyte */
  public int _110;

  public int _114;
  /** short */
  public float projectionPlaneDistance_118;
  /** related to projection distance somehow (ubyte) */
  public int _11a;

  public final GsF_LIGHT[] lights_11c = {new GsF_LIGHT(), new GsF_LIGHT(), new GsF_LIGHT()};
  public final Vector3f ambientLight_14c = new Vector3f();
  public final WMapSubStruct18[] _154 = new WMapSubStruct18[0x101];
  public int _196c;
  public int _1970;
  public int _1974;

  public final float[] _19a8 = new float[3];
  public final float[] _19ae = new float[3];

  public WMapStruct19c0() {
    Arrays.setAll(this._154, i -> new WMapSubStruct18());
  }
}
