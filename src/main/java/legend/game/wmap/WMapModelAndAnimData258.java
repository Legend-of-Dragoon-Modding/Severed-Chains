package legend.game.wmap;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.game.types.Model124;
import legend.game.unpacker.FileData;
import org.joml.Vector3f;

public class WMapModelAndAnimData258 {
  public enum FadeAnimationType {
    NONE_0(0),
    FADE_IN_1(1),
    FADE_OUT_2(2),
    ;

    public final int typeIndex;

    FadeAnimationType(final int typeIndex) {
      this.typeIndex = typeIndex;
    }
  }

  public enum FadeState {
    START_FADE_0,
    FADE_1,
    END_FADE_2,
  }

  public enum FastTravelTransitionMode {
    NONE_0,
    TELEPORT_1,
    OPEN_COOLON_MAP_2,
    COOLON_ARRIVAL_3,
  }

  /**
   * Retail state equivalent:
   * <ol start="0">
   *  <li>LOCAL</li>
   *  <li>CONTINENT</li>
   *  <li>TRANSITION_MODEL_OUT</li>
   *  <li>TRANSITION_MODEL_OUT</li>
   *  <li>WORLD</li>
   *  <li>TRANSITION_MODEL_IN</li>
   *  <li>CONTINENT</li>
   * </ol>
   */
  public enum ZoomState {
    LOCAL_0,
    CONTINENT_1,
    TRANSITION_MODEL_OUT_2,
    WORLD_3,
    TRANSITION_MODEL_IN_4,
  }

  public enum CoolonWarpState {
    NONE_0(0),
    ASCENT_1(1),
    INIT_WORLD_MAP_2(2),
    MAIN_LOOP_3(3),
    INIT_PROMPT_4(4),
    PROMPT_LOOP_5(5),
    FLY_ANIM_6(6),
    INIT_DEST_7(7),
    INIT_DESCENT_10(10),
    PAN_MAP_11(11),
    DESCENT_12(12),
    RESTORE_DART_NEG_1(-1),
    ;

    public final int state;

    CoolonWarpState(final int state) {
      this.state = state;
    }
  }

  public enum TeleportAnimationState {
    INIT_ANIM_0,
    RENDER_ANIM_1,
    INIT_FADE_2,
    FADE_OUT_3,
  }

  public int fadeAnimationTicks_00;
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
  public FadeAnimationType fadeAnimationType_05;

  public WMapTmdRenderingData18 tmdRendering_08;
  public final Model124[] models_0c = new Model124[4];
  public WaterAnimation20 textureAnimation_1c;
  /** Used for brightness of the map name and the map textures overall (short) */
  public float mapTextureBrightness_20;

  public Obj mapContinentNameObj;
  public final MV mapOverlayTransforms = new MV();

  public MapMarker mapArrow;
  public MapMarker coolonPlaceMarker;

  public WMapAtmosphericEffectInstance60[] atmosphericEffectInstances_24;
  public Obj[] atmosphericEffectSprites;

  public float clutYIndex_28;
  public FileData imageData_2c;
  public FileData imageData_30;
  /** Used as the camera position, only translation is used */
  public final GsCOORDINATE2 coord2_34 = new GsCOORDINATE2();
  public final Vector3f prevPlayerPos_84 = new Vector3f();
  public final Vector3f currPlayerPos_94 = new Vector3f();
  public final Vector3f playerRotation_a4 = new Vector3f();
  /**
   * <ol start="2">
   *   <li>Idle</li>
   *   <li>Walk</li>
   *   <li>Run</li>
   * </ol>
   */
  public int prevAnimIndex_ac;
  /**
   * <ol start="2">
   *   <li>Idle</li>
   *   <li>Walk</li>
   *   <li>Run</li>
   * </ol>
   */
  public int currAnimIndex_b0;
  public final PlayerModelTmdFileData[] playerModelTmdFileData_b4 = {new PlayerModelTmdFileData(), new PlayerModelTmdFileData(), new PlayerModelTmdFileData(), new PlayerModelTmdFileData()};

  public Obj shadowObj;
  public final MV shadowTransforms = new MV();
  /**
   * <ol start="0">
   *   <li>Dart</li>
   *   <li>Queen Fury</li>
   *   <li>Coolon</li>
   *   <li>Teleporter</li>
   * </ol>
   */
  public int modelIndex_1e4;

  // Zoom attributes
  public final Vector3f mapPosition_1e8 = new Vector3f();
  public final Vector3f mapZoomStep_1f0 = new Vector3f();
  /** ubyte */
  public ZoomState zoomState_1f8;
  /** ubyte */
  public int zoomAnimationTick_1f9;
  public ZoomOverlay zoomOverlay;

  // Coolon attributes
  /** Highlight refactored into WmapPromptPopup */
  // public WmapMenuTextHighlight40 coolonTravelMenuSelectorHighlight_1fc;
  public final Vector3f mapPos_200 = new Vector3f();
  public final Vector3f playerPos_208 = new Vector3f();

  public int coolonTravelAnimationTick_218;
  /** ushort */
  public float playerRotation_21c;
  /** ushort */
  public float mapRotation_21e;
  /** byte
   * <ol start="-1">
   *   <li>Restore Dart</li>
   *   <li>Store data and clear text</li>
   *   <li>Coolon ascent</li>
   *   <li>Initialize map</li>
   *   <li>Main loop</li>
   *   <li>Initialize prompt</li>
   *   <li>Prompt loop</li>
   *   <li>Fly to destination</li>
   *   <li>Initialize destination</li>
   *   <li value="10">Initialize descent animation</li>
   *   <li>Pan map model</li>
   *   <li>Coolon descent</li>
   * </ol>
   */
  public CoolonWarpState coolonWarpState_220;
  /** ubyte */
  public int coolonOriginIndex_221;
  /**
   * ubyte
   * <ol>
   *   <li>Lohan</li>
   *   <li>Bale</li>
   *   <li>Fletz</li>
   *   <li>Donau</li>
   *   <li>Fueno</li>
   *   <li>Furni</li>
   *   <li>Deningrad</li>
   *   <li>Ulara</li>
   *   <li>Zenebatos</li>
   * </ol>
   */
  public int coolonDestIndex_222;
  /** ubyte */
  public int coolonPromptIndex_223;


  // _224 through _244 are used for rendering the Queen Fury's wake, though they are
  // initialized regardless.
  public Vector3f[] wakeSpreadsArray_224;
  public Vector3f[] shipPositionsArray_228;
  public int[] wakeSegmentNumArray_22c;
  public int currShipPositionIndex_230;
  public int prevShipPositionIndex_234;
  public int shipPositionsCount_238;
  public int wakeSegmentCount;
  public int wakeSegmentStride_23c;
  public int tickNum_240;
  /** byte */
  public boolean shipPositionsUninitialized_244;
  public TeleportAnimationState teleportAnimationState_248;

  public int teleportAnimationTick_24c;
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
  public FastTravelTransitionMode fastTravelTransitionMode_250;
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
