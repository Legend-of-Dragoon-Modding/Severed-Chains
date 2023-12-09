package legend.game.wmap;

import legend.core.gte.GsCOORDINATE2;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsRVIEW2;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.Arrays;

public class WMapCameraAndLights19c0 {
  public enum LightsUpdateState {
    INIT_DIMMING_0,
    DIM_1,
    INIT_BRIGHTENING_2,
    BRIGHTEN_3,
  }

  public enum CameraUpdateState {
    AWAIT_INPUT_0,
    ZOOM_OUT_1,
    ZOOM_IN_2,
  }

  public enum MapRotationState {
    MAIN_LOOP_0,
    INIT_SUBMAP_ZOOM_1,
    SUBMAP_ZOOM_2,
  }

  public enum ProjectionDistanceState {
    SELECT_0,
    INIT_VIEW_NEAR_1,
    MAIN_LOOP_NEAR_2,
    INIT_VIEW_FAR_3,
    MAIN_LOOP_FAR_4,
  }

  public final GsRVIEW2 currRview2_00 = new GsRVIEW2();
  /** Parent coordinate system of camera; controls movement */
  public final GsCOORDINATE2 coord2_20 = new GsCOORDINATE2();
  /** Not in retail */
  public int mapRotationDirection;
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
  /**
   * <ol start="0">
   *   <li>Init dimming</li>
   *   <li>Dim</li>
   *   <li>Init brightening</li>
   *   <li>Brighten</li>
   * </ol>
   */
  public LightsUpdateState lightsUpdateState_88;
  public final Vector3i[] lightsColours_8c = {new Vector3i(), new Vector3i(), new Vector3i()};
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
  public boolean zoomStateIsLocal;
  /**
   * ubyte
   * <ol start="0">
   *   <li>Await input</li>
   *   <li>Zoom out</li>
   *   <li>Zoom in</li>
   * </ol>
   */
  public CameraUpdateState cameraUpdateState_c5;

  public final GsRVIEW2 originalRview2_c8 = new GsRVIEW2();

  // Next 4 fields are used for little swoop during fade-to-submap camera animation on top of the rotation
  public float viewpointSwoopStepY_ec;
  public float viewpointSwoopStepZ_f0;

  public float refpointSwoopStepY_f8;
  public float refpointSwoopStepZ_fc;

  /** short */
  public float finalCameraRotation_108;
  /** short */
  public float originalCameraRotation_10a;
  /** short */
  public float cameraRotationStep_10c;
  /** ushort */
  public int fadeOutZoomTick_10e;
  /**
   * ubyte
   * <ol start="0">
   *   <li>Main loop</li>
   *   <li>Init submap zoom</li>
   *   <li>Submap zoom</li>
   * </ol>
   */
  public MapRotationState mapRotationState_110;

  public int projectionPlaneZoomTick_114;
  /** short */
  public float projectionPlaneDistance_118;
  /**
   * ubyte
   * <ol start="0">
   *   <li>Case selection</li>
   *   <li>Close view main loop</li>
   *   <li>Zooming in</li>
   *   <li>Far view main loop</li>
   *   <li>Zooming out</li>
   * </ol>
   */
  public ProjectionDistanceState projectionDistanceState_11a;

  public final GsF_LIGHT[] lights_11c = {new GsF_LIGHT(), new GsF_LIGHT(), new GsF_LIGHT()};
  public final Vector3f ambientLight_14c = new Vector3f();
  public final LocationDistance18[] locationDistances_154 = new LocationDistance18[0x101];

  // _196c, _1970, and _1974 are never used, and _19a8 and _19ae each store 3 copies of a single unchanging value.

  public WMapCameraAndLights19c0() {
    Arrays.setAll(this.locationDistances_154, i -> new LocationDistance18());
  }
}
