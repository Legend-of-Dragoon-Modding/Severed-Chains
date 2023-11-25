package legend.game.wmap;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.opengl.MeshObj;
import legend.core.opengl.Obj;
import legend.game.types.Model124;
import legend.game.unpacker.FileData;
import org.joml.Vector3f;

public class WMapStruct258 {
  public enum TransitionAnimationType {
    NONE,
    FADE_IN,
    FADE_OUT
  }

  public enum FadeState {
    START_FADE,
    FADE,
    END_FADE
  }

  public enum MapTransitionDestinationMode {
    NONE,
    TELEPORT,
    SUBMAP,
    WORLD_MAP
  }

  /**
   * TODO Look more into zoom code before refactoring to use states, might be able
   *  to reduce number.
   */
  public enum ZoomState {
    LOCAL,
    CONTINENT_IN,
    TRANSITION_MODEL_OUT,
    TRANSITION_ARROW_SIZE,
    WORLD,
    TRANSITION_MODEL_IN,
    CONTINENT_OUT
  }

  public int transitionAnimationTicks_00;
  /**
   * ubyte
   * <ol start="0">
   *  <li>Start fade</li>
   *  <li>Fade</li>
   *  <li>End fade</li>
   * </ol>
   */
  public FadeState fadeState_04;
  /**
   * ubyte
   * <ol start="0">
   *  <li>None</li>
   *  <li>Fade in</li>
   *  <li>Fade out</li>
   * </ol>
   */
  public TransitionAnimationType transitionAnimationType_05;

  public WMapTmdRenderingStruct18 tmdRendering_08;
  public final Model124[] models_0c = new Model124[4];
  public TextureAnimation20 textureAnimation_1c;
  /** Used for brightness of the map name and the map textures overall (short) */
  public float mapTextureBrightness_20;

  public MeshObj mapContinentNameObj;
  public MeshObj[] zoomOverlayObjs = new MeshObj[7];
  public final MV mapOverlayTransforms = new MV();

  public MapMarker mapArrow;
  public MapMarker coolonPlaceMarker;

  public WMapAtmosphericEffectInstance60[] atmosphericEffectInstances_24;
  public MeshObj[] atmosphericEffectSprites;

  public float clutYIndex_28;
  public FileData imageData_2c;
  public FileData imageData_30;
  /** Used as the camera position, only translation is used */
  public final GsCOORDINATE2 coord2_34 = new GsCOORDINATE2();
  public final Vector3f prevPlayerPos_84 = new Vector3f();
  public final Vector3f currPlayerPos_94 = new Vector3f();
  public final Vector3f rotation_a4 = new Vector3f();
  public int currentAnimIndex_ac;
  public int animIndex_b0;
  public final WMapStruct258Sub40[] _b4 = {new WMapStruct258Sub40(), new WMapStruct258Sub40(), new WMapStruct258Sub40(), new WMapStruct258Sub40()};

  public Obj shadowObj;
  public final MV shadowTransforms = new MV();
  public int modelIndex_1e4;
  public final Vector3f svec_1e8 = new Vector3f();
  public final Vector3f svec_1f0 = new Vector3f();
  /** ubyte */
  public int zoomState_1f8;
  /** Not in retail */
  public int previousZoomLevel;
  /** ubyte */
  public int zoomAnimationTick_1f9;
  /** Highlight refactored into WmapPromptPopup */
  // public WmapMenuTextHighlight40 coolonTravelMenuSelectorHighlight_1fc;
  public final Vector3f svec_200 = new Vector3f();
  public final Vector3f svec_208 = new Vector3f();

  public int _218;
  /** ushort */
  public float angle_21c;
  /** ushort */
  public float angle_21e;
  /** byte */
  public int _220;
  /** ubyte */
  public int coolonWarpIndex_221;
  /** ubyte */
  public int coolonWarpIndex_222;
  /** ubyte */
  public int _223;


  // _224 through _248 are used for rendering the Queen Fury's wake, though they are
  // initialized regardless.
  public Vector3f[] wakeSpreadsArray_224;
  public Vector3f[] shipPositionsArray_228;
  public int[] wakeSegmentNumArray_22c;
  public int currShipPositionIndex_230;
  public int prevShipPositionIndex_234;
  public int shipPositionsCount_238;
  public int wakeSegmentStride_23c;
  public int tickNum_240;
  /** byte */
  public boolean shipPositionsUninitialized_244;
  public int _248;

  public int _24c;
  /**
   * Not totally sure what this should be called yet, but seems related to transitions
   * and transition animations (except combat).
   * <ol start="0">
   *   <li>No map animation</li>
   *   <li>Teleportation animation</li>
   *   <li>Transition to submap</li>
   *   <li>Transition to world map</li>
   * </ol>
   */
  public MapTransitionDestinationMode mapTransitionDestinationMode_250;
  public boolean usingCoolonFromZenebatos_254;

  public void deleteAtmosphericEffectObjs() {
    for(int i = 0; i < this.atmosphericEffectSprites.length; i++) {
      if(this.atmosphericEffectSprites[i] != null) {
        this.atmosphericEffectSprites[i].delete();
        this.atmosphericEffectSprites[i] = null;
      }
    }
  }

  public void deleteMapMarkers() {
    this.mapArrow.delete();
    this.mapArrow = null;
    this.coolonPlaceMarker.delete();
    this.coolonPlaceMarker = null;
  }
}
