package legend.game.wmap;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.QueuedModelTmd;
import legend.core.gpu.Bpp;
import legend.core.gpu.Rect4i;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdWithId;
import legend.core.memory.Method;
import legend.core.memory.types.FloatRef;
import legend.core.memory.types.IntRef;
import legend.core.opengl.McqBuilder;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.TmdObjLoader;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.submap.EncounterRateMode;
import legend.game.tim.Tim;
import legend.game.types.CContainer;
import legend.game.types.GsF_LIGHT;
import legend.game.types.McqHeader;
import legend.game.types.Model124;
import legend.game.types.TextboxState;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.core.MathHelper.flEq;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadDrgnFileSync;
import static legend.game.Scus94491BpeSegment.loadLocationMenuSoundEffects;
import static legend.game.Scus94491BpeSegment.loadWmapMusic;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment.stopSound;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.animateModel;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.clearTextbox;
import static legend.game.Scus94491BpeSegment_8002.initInventoryMenu;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.initTextbox;
import static legend.game.Scus94491BpeSegment_8002.isTextboxInState6;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.resetSubmapToNewGame;
import static legend.game.Scus94491BpeSegment_8002.setTextAndTextboxesToUninitialized;
import static legend.game.Scus94491BpeSegment_8002.strcmp;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2L;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.previousEngineState_8004dd28;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8007.clearRed_8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.analogMagnitude_800beeb4;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.clearBlue_800babc0;
import static legend.game.Scus94491BpeSegment_800b.clearGreen_800bb104;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.soundFiles_800bcf80;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.textboxes_800be358;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.wmap.MapState100.ForcedMovementMode;
import static legend.game.wmap.MapState100.PathSegmentEndpointType;
import static legend.game.wmap.MapState100.PathSegmentEntering;
import static legend.game.wmap.WMapCameraAndLights19c0.CameraUpdateState;
import static legend.game.wmap.WMapCameraAndLights19c0.LightsUpdateState;
import static legend.game.wmap.WMapCameraAndLights19c0.MapRotationState;
import static legend.game.wmap.WMapCameraAndLights19c0.ProjectionDistanceState;
import static legend.game.wmap.WMapModelAndAnimData258.CoolonWarpState;
import static legend.game.wmap.WMapModelAndAnimData258.FadeAnimationType;
import static legend.game.wmap.WMapModelAndAnimData258.FadeState;
import static legend.game.wmap.WMapModelAndAnimData258.FastTravelTransitionMode;
import static legend.game.wmap.WMapModelAndAnimData258.TeleportAnimationState;
import static legend.game.wmap.WMapModelAndAnimData258.ZoomState;
import static legend.game.wmap.WmapStatics.coolonWarpDest_800ef228;
import static legend.game.wmap.WmapStatics.directionalPathSegmentData_800f2248;
import static legend.game.wmap.WmapStatics.encounterIds_800ef364;
import static legend.game.wmap.WmapStatics.loadWait;
import static legend.game.wmap.WmapStatics.locations_800f0e34;
import static legend.game.wmap.WmapStatics.mapPositions_800ef1a8;
import static legend.game.wmap.WmapStatics.negativeDirectionMovementMask_800f0210;
import static legend.game.wmap.WmapStatics.pathDotPosArr_800f591c;
import static legend.game.wmap.WmapStatics.pathSegmentLengths_800f5810;
import static legend.game.wmap.WmapStatics.placeIndices_800c84c8;
import static legend.game.wmap.WmapStatics.placePositionVectors_800c74b8;
import static legend.game.wmap.WmapStatics.places_800f0234;
import static legend.game.wmap.WmapStatics.playerAvatarVramSlots_800ef694;
import static legend.game.wmap.WmapStatics.positiveDirectionMovementMask_800f0204;
import static legend.game.wmap.WmapStatics.regions_800f01ec;
import static legend.game.wmap.WmapStatics.services_800f01cc;
import static legend.game.wmap.WmapStatics.teleportationEndpointIndices_800ef698;
import static legend.game.wmap.WmapStatics.teleportationLocations_800ef6c8;
import static legend.game.wmap.WmapStatics.tmdUvAdjustmentMetrics_800eee48;
import static legend.game.wmap.WmapStatics.waterClutYs_800ef348;
import static legend.game.wmap.WmapStatics.wmapDestinationMarkers_800f5a6c;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class WMap extends EngineState {
  private enum WorldMapState {
    UNINITIALIZED_0(0),
    UNUSED_1(1),
    LOAD_MODEL_2(2),
    INIT_MAP_ANIM_3(3),
    NOOP_4(4),
    RENDER_5(5),
    NOOP_6(6),
    UNUSED_DEALLOC_7(7),
    ;

    public final int state;

    WorldMapState(final int state) {
      this.state = state;
    }
  }

  private enum PlayerState {
    UNINITIALIZED_0(0),
    UNUSED_1(1),
    LOAD_MODEL_2(2),
    INIT_PLAYER_MODEL_3(3),
    NOOP_4(4),
    RENDER_5(5),
    NOOP_6(6),
    UNUSED_DEALLOC_7(7),
    ;

    public final int state;

    PlayerState(final int state) {
      this.state = state;
    }
  }

  /** 5 and 9 used when entering location, 6-8 used when canceling. */
  private enum MapTransitionState {
    INIT_0,
    LOAD_FILES_1,
    BUILD_PROMPT_2,
    MAIN_LOOP_3,
    UNUSED_4,
    ANIMATE_PROMPT_OUT_5,
    INIT_MOVEMENT_6,
    WAIT_7,
    END_MOVEMENT_8,
    SET_DEST_9,
  }

  private static final FontOptions UI_WHITE_SHADOWED = new FontOptions().colour(TextColour.WHITE).shadowColour(TextColour.BLACK).horizontalAlign(HorizontalAlign.CENTRE);

  private boolean reinitializingWmap_80052c6c;

  private int tickMainMenuOpenTransition_800c6690;

  private WorldMapState worldMapState_800c6698;
  private PlayerState playerState_800c669c;

  private int atmosphericEffectStage_800c66a4;
  private final WMapModelAndAnimData258 modelAndAnimData_800c66a8 = new WMapModelAndAnimData258();

  private final WMapCameraAndLights19c0 wmapCameraAndLights19c0_800c66b0 = new WMapCameraAndLights19c0();

  /**
   * <ul>
   *   <li>0x2 - general wmap textures</li>
   *   <li>0x4 - wmap mesh</li>
   * </ul>
   */
  private final AtomicInteger filesLoadedFlags_800c66b8 = new AtomicInteger();

  private McqHeader mcqHeader_800c6768;
  private final MV mcqTransforms = new MV();
  private Obj mcqObj;

  private float mcqColour_800c6794;

  public final MapState100 mapState_800c6798 = new MapState100();

  private int cancelLocationEntryDelayTick_800c68a0;
  /**
   * <ol start="0">
   *   <li>Init new path/prompt</li>
   *   <li>Load files</li>
   *   <li>Build prompt</li>
   *   <li>Main loop</li>
   *   <li></li>
   *   <li>Animate out prompt</li>
   *   <li>Init forced movement</li>
   *   <li>Wait for prompt to close</li>
   *   <li>End forced movement</li>
   *   <li>Set destination entered</li>
   * </ol>
   */
  private MapTransitionState mapTransitionState_800c68a4;
  private boolean startLocationLabelsActive_800c68a8;

  public int encounterAccumulator_800c6ae8;

  private int placeCount_800c86cc;

  private final int[] startButtonLabelStages_800c86d4 = new int[8];

  private int destinationLabelStage_800c86f0;

  private WmapSmokeInstance60[] smokeInstances_800c86f8;

  private final Rect4i storedEffectsRect_800c8700 = new Rect4i(576, 256, 128, 256);

  private final Vector3f shipWakeCrossVector_800c87d8 = new Vector3f(0.0f, 1.0f, 0.0f);

  public WmapState wmapState_800bb10c = WmapState.INIT_0;

  /**
   * <ol start="0">
   *   <li>{@link WMap#initWmap}</li>
   *   <li>{@link WMap#waitForWmapMusicToLoad}</li>
   *   <li>{@link WMap#initWmap2}</li>
   *   <li>{@link WMap#handleAndRenderWmap}</li>
   *   <li>{@link WMap#transitionToScreens}</li>
   *   <li>{@link WMap#renderWmapScreens}</li>
   *   <li>{@link WMap#restoreMapOnExitMainMenu}</li>
   *   <li>{@link WMap#transitionToSubmap}</li>
   *   <li>{@link WMap#transitionToCombat}</li>
   *   <li>{@link WMap#transitionToWorldMap}</li>
   *   <li>{@link WMap#unusedDeallocator}</li>
   *   <li>{@link WMap#noOpWmapState11Runnable}</li>
   *   <li>{@link WMap#setWmapStateToExitScreens}</li>
   *   <li>{@link WMap#transitionToTitle}</li>
   *   <li>{@link WMap#loadBackgroundObj}</li>
   * </ol>
   */
  private final Runnable[] wmapStates_800ef000 = {
    this::initWmap,
    this::waitForWmapMusicToLoad,
    this::initWmap2,
    this::handleAndRenderWmap,
    this::transitionToScreens,
    this::renderWmapScreens,
    this::restoreMapOnExitMainMenu,
    this::transitionToSubmap,
    this::transitionToCombat,
    this::transitionToWorldMap,
    this::unusedDeallocator,
    this::noOpWmapState11Runnable,
    this::setWmapStateToExitScreens,
    this::transitionToTitle,
    this::loadBackgroundObj,
  };

  private CoolonQueenFuryOverlay coolonQueenFuryOverlay;

  private float mcqBrightness_800ef1a4;

  /**
   * <ol start="0">
   *   <li>{@link WMap#renderDartShadow}</li>
   *   <li>{@link WMap#renderQueenFuryWake}</li>
   *   <li>{@link WMap#renderNoOp}</li>
   *   <li>{@link WMap#renderNoOp}</li>
   * </ul>
   */
  private final Runnable[] shadowRenderers_800ef684 = {
    this::renderDartShadow,
    this::renderQueenFuryWake,
    this::renderNoOp, // Coolon
    this::renderNoOp, // Teleporter
  };

  private final Runnable[] fadeTransitionTickers_800f01fc = new Runnable[2];
  {
    this.fadeTransitionTickers_800f01fc[0] = this::tickFadeInTransition;
    this.fadeTransitionTickers_800f01fc[1] = this::tickFadeOutTransition;
  }

  private int currentWmapEffect_800f6598;
  private int previousWmapEffect_800f659c;

  /**
   * Allocators for subsequent renderers
   * <ol start="0">
   *   <li>{@link WMap#noOpAllocate}</li>
   *   <li>{@link WMap#allocateClouds}</li>
   *   <li>{@link WMap#allocateSnow}</li>
   * </ol>
   */
  private final Runnable[] atmosphericEffectAllocators_800f65a4 = new Runnable[3];
  {
    this.atmosphericEffectAllocators_800f65a4[0] = this::noOpAllocate;
    this.atmosphericEffectAllocators_800f65a4[1] = this::allocateClouds;
    this.atmosphericEffectAllocators_800f65a4[2] = this::allocateSnow;
  }
  /**
   * These are probably effects that can be rendered over a place
   * <ol start="0">
   *   <li>{@link WMap#noOpRender}</li>
   *   <li>{@link WMap#renderClouds}</li>
   *   <li>{@link WMap#renderSnow}</li>
   * </ol>
   */
  private final Runnable[] atmosphericEffectRenderers_800f65b0 = new Runnable[3];
  {
    this.atmosphericEffectRenderers_800f65b0[0] = this::noOpRender;
    this.atmosphericEffectRenderers_800f65b0[1] = this::renderClouds;
    this.atmosphericEffectRenderers_800f65b0[2] = this::renderSnow;
  }

  private WmapPromptPopup wmapLocationPromptPopup;
  private WmapPromptPopup coolonPromptPopup;
  private final MV fastTravelTransforms = new MV();
  /** Temporary solution until text refactoring */
  private final String[] startLabelNames = new String[8];
  private final float[] startLabelXs = new float[8];
  private final float[] startLabelYs = new float[8];
  private String destLabelName;
  private int destLabelX;
  private int destLabelY;
  private boolean shouldSetDestLabelMetrics;
  private String coolonWarpDestLabelName;
  private int coolonWarpDestLabelX;
  private int coolonWarpDestLabelY;
  private boolean shouldSetCoolonWarpDestLabelMetrics;

  @Override
  public int tickMultiplier() {
    return 3;
  }

  @Override
  public boolean renderTextOnTopOfAllBoxes() {
    return false;
  }

  @Override
  public void restoreMusicAfterMenu() {
    unloadSoundFile(8);
  }

  @Method(0x800c925cL)
  private void renderWmapModel(final Model124 model) {
    final MV lw = new MV();

    tmdGp0Tpage_1f8003ec = model.tpage_108;

    //LAB_800c92c8
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPart10 dobj2 = model.modelParts_00[i];

      if((model.partInvisible_f4 & 1L << i) == 0) {
        GsGetLw(dobj2.coord2_04, lw);

        float screenOffsetY = 0.0f;
        if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.WORLD_3 || this.modelAndAnimData_800c66a8.coolonWarpState_220.state > 2) {
          screenOffsetY = -8.0f; // Needs adjustment since we shifted the world map MCQ 8 pixels down
        }

        RENDERER.queueModel(dobj2.obj, lw, QueuedModelTmd.class)
          .lightDirection(lightDirectionMatrix_800c34e8)
          .lightColour(lightColourMatrix_800c3508)
          .backgroundColour(GTE.backgroundColour)
          .screenspaceOffset(0, screenOffsetY)
          .tmdTranslucency(tmdGp0Tpage_1f8003ec >>> 5 & 0b11);
      }
    }

    //LAB_800c936c
  }

  @Override
  @Method(0x800cc738L)
  public void tick() {
    this.wmapStates_800ef000[this.wmapState_800bb10c.state].run();
  }

  @Override
  public void overlayTick() {
    if(this.wmapState_800bb10c == WmapState.PLAY_3) {
      if(this.worldMapState_800c6698.state > WorldMapState.INIT_MAP_ANIM_3.state && this.playerState_800c669c.state > PlayerState.INIT_PLAYER_MODEL_3.state) {
        if(this.modelAndAnimData_800c66a8.coolonWarpState_220 == CoolonWarpState.PROMPT_LOOP_5) {
          this.coolonPromptPopup.render();
        }

        if(this.startLocationLabelsActive_800c68a8 && this.modelAndAnimData_800c66a8.zoomState_1f8 != ZoomState.WORLD_3) {
          for(int i = 0; i < 8; i++) {
            if(this.startLabelNames[i] != null) {
              textZ_800bdf00 = textboxes_800be358[i].z_0c - 1;
              renderText(this.startLabelNames[i], this.startLabelXs[i], this.startLabelYs[i], UI_WHITE_SHADOWED);
            }
          }
        }

        if(this.destLabelName != null) {
          textZ_800bdf00 = textboxes_800be358[7].z_0c - 1;
          renderText(this.destLabelName, this.destLabelX, this.destLabelY, UI_WHITE_SHADOWED);
        }

        if(this.coolonWarpDestLabelName != null && this.destinationLabelStage_800c86f0 != 0) {
          textZ_800bdf00 = textboxes_800be358[7].z_0c - 1;
          renderText(this.coolonWarpDestLabelName, this.coolonWarpDestLabelX, this.coolonWarpDestLabelY, UI_WHITE_SHADOWED);
        }

        this.handleMapTransitions();
      }
    }
  }

  /** Just the inventory menu right now, but we might add more later */
  @Method(0x800cc758L)
  private void renderWmapScreens() {
    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      if(loadingNewGameState_800bdc34) { // This is part of a cut load game menu
        final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;

        //LAB_800cc7d0
        modelAndAnimData.imageData_2c = null;
        modelAndAnimData.imageData_30 = null;

        this.wmapState_800bb10c = gameState_800babc8.isOnWorldMap_4e4 ? WmapState.TRANSITION_TO_WORLD_MAP_9 : WmapState.TRANSITION_TO_SUBMAP_7;
      } else {
        //LAB_800cc804
        resizeDisplay(320, 240);
        loadWmapMusic(gameState_800babc8.chapterIndex_98);
        this.wmapState_800bb10c = WmapState.PRE_EXIT_SCREENS_12;
      }

      //LAB_800cc828
    } else if(whichMenu_800bdc38 == WhichMenu.QUIT) {
      this.wmapState_800bb10c = WmapState.TRANSITION_TO_TITLE_13;
    }

    //LAB_800cc82c
  }

  /** Checks for triangle press and transitions into the inv screen */
  @Method(0x800cc83cL)
  private void handleInventoryTransition() {
    if(Loader.getLoadingFileCount() == 0) {
      if(this.tickMainMenuOpenTransition_800c6690 == 0) {
        final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;

        if(cameraAndLights.cameraUpdateState_c5 == CameraUpdateState.AWAIT_INPUT_0) {
          if(cameraAndLights.zoomStateIsLocal_c4) {
            final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;

            if(modelAndAnimData.zoomState_1f8 == ZoomState.LOCAL_0) {
              if(modelAndAnimData.coolonWarpState_220 == CoolonWarpState.NONE_0) {
                if(
                  this.worldMapState_800c6698.state >= WorldMapState.INIT_MAP_ANIM_3.state ||
                    this.playerState_800c669c.state >= PlayerState.INIT_PLAYER_MODEL_3.state
                ) {
                  //LAB_800cc900
                  if(Input.pressedThisFrame(InputAction.BUTTON_NORTH)) {
                    if(this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc != PathSegmentEndpointType.TERMINAL_1) {
                      if(modelAndAnimData.fadeAnimationType_05 == FadeAnimationType.NONE_0) {
                        if(this.mapState_800c6798.queenFuryForceMovementMode_d8 == ForcedMovementMode.NONE_0) {
                          if(modelAndAnimData.fastTravelTransitionMode_250 == FastTravelTransitionMode.NONE_0) {
                            startFadeEffect(1, 15);
                            this.mapState_800c6798.disableInput_d0 = true;
                            this.tickMainMenuOpenTransition_800c6690 = 1;
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }

        return;
      }

      //LAB_800cc970
      this.modelAndAnimData_800c66a8.mapTextureBrightness_20 -= 0.5f / (3.0f / vsyncMode_8007a3b8);
      if(this.modelAndAnimData_800c66a8.mapTextureBrightness_20 < 0.0f) {
        this.modelAndAnimData_800c66a8.mapTextureBrightness_20 = 0.0f;
      }

      //LAB_800cc998
      this.tickMainMenuOpenTransition_800c6690++;
      if(this.tickMainMenuOpenTransition_800c6690 >= 45.0f / vsyncMode_8007a3b8) {
        this.wmapState_800bb10c = WmapState.TRANSITION_TO_SCREENS_4;
        initInventoryMenu();

        this.modelAndAnimData_800c66a8.imageData_2c = new FileData(new byte[0x1_0000]);
        this.modelAndAnimData_800c66a8.imageData_30 = new FileData(new byte[0x1_0000]);

        GPU.downloadData15(this.storedEffectsRect_800c8700, this.modelAndAnimData_800c66a8.imageData_2c);
        GPU.downloadData15(new Rect4i(320, 0, 64, 512), this.modelAndAnimData_800c66a8.imageData_30);
      }
    }
    //LAB_800cca5c
  }

  @Method(0x800cca74L)
  private void restoreMapOnExitMenu_() {
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    vsyncMode_8007a3b8 = 1;
    startFadeEffect(2, 15);
    GPU.uploadData15(this.storedEffectsRect_800c8700, modelAndAnimData.imageData_2c);
    GPU.uploadData15(new Rect4i(320, 0, 64, 512), modelAndAnimData.imageData_30);
    modelAndAnimData.imageData_2c = null;
    modelAndAnimData.imageData_30 = null;
    this.initLighting();

    if(modelAndAnimData.zoomState_1f8 == ZoomState.LOCAL_0) {
      this.mapState_800c6798.disableInput_d0 = false;
    }

    //LAB_800ccb6c
    this.tickMainMenuOpenTransition_800c6690 = 0;
    setProjectionPlaneDistance(1100);
    this.wmapState_800bb10c = WmapState.PLAY_3;
  }

  @Method(0x800ccbd8L)
  private void noOpWmapState11Runnable() {
    // no-op
  }

  @Method(0x800ccbe0L)
  private void initWmap() {
    resizeDisplay(320, 240);
    vsyncMode_8007a3b8 = 1;
    unloadSoundFile(9);
    loadWmapMusic(gameState_800babc8.chapterIndex_98);
    this.wmapState_800bb10c = WmapState.WAIT_FOR_MUSIC_TO_LOAD_1;
  }

  @Method(0x800ccc30L)
  private void waitForWmapMusicToLoad() {
    if((getLoadedDrgnFiles() & 0x80) == 0) {
      this.wmapState_800bb10c = WmapState.INIT2_2;
    }

    //LAB_800ccc54
  }

  @Method(0x800ccc64L)
  private void initWmap2() {
    setProjectionPlaneDistance(1100);

    //LAB_800ccc84
    for(int i = 0; i < 8; i++) {
      gameState_800babc8.scriptFlags1_13c.setRaw(i, 0);
    }

    this.initWmapAudioVisuals();
    this.tickMainMenuOpenTransition_800c6690 = 0;
    this.wmapState_800bb10c = WmapState.LOAD_BACKGROUND_OBJ_14;
  }

  private void loadBackgroundObj() {
    if((this.filesLoadedFlags_800c66b8.get() & 0x1) != 0) {
      this.mcqBrightness_800ef1a4 = 0.0f;
      this.mcqObj = new McqBuilder("World Map Background MCQ", this.mcqHeader_800c6768)
        .translucency(Translucency.B_PLUS_F)
        .vramOffset(320, 0)
        .build();

      this.wmapState_800bb10c = WmapState.PLAY_3;
    }
  }

  @Method(0x800cccbcL)
  private void handleAndRenderWmap() {
    this.handleAndRenderMapAndPlayer();
    this.handleInventoryTransition();
  }

  @Method(0x800ccce4L)
  private void transitionToScreens() {
    gameState_800babc8.directionalPathIndex_4de = this.mapState_800c6798.directionalPathIndex_12;
    gameState_800babc8.pathIndex_4d8 = this.mapState_800c6798.pathIndex_14;
    gameState_800babc8.dotIndex_4da = this.mapState_800c6798.dotIndex_16;
    gameState_800babc8.dotOffset_4dc = this.mapState_800c6798.dotOffset_18;
    gameState_800babc8.facing_4dd = this.mapState_800c6798.facing_1c;

    //LAB_800ccd30
    for(int i = 0; i < 8; i++) {
      setTextAndTextboxesToUninitialized(i, 0);
    }

    this.startLocationLabelsActive_800c68a8 = false;
    this.wmapState_800bb10c = WmapState.RENDER_SCREENS_5;
  }

  @Method(0x800ccd70L)
  private void restoreMapOnExitMainMenu() {
    if((getLoadedDrgnFiles() & 0x80) == 0) {
      this.restoreMapOnExitMenu_();
    }
    //LAB_800ccd94
  }

  @Method(0x800ccda4L)
  private void transitionToSubmap() {
    gameState_800babc8.directionalPathIndex_4de = this.mapState_800c6798.directionalPathIndex_12;
    gameState_800babc8.pathIndex_4d8 = this.mapState_800c6798.pathIndex_14;
    gameState_800babc8.dotIndex_4da = this.mapState_800c6798.dotIndex_16;
    gameState_800babc8.dotOffset_4dc = this.mapState_800c6798.dotOffset_18;
    gameState_800babc8.facing_4dd = this.mapState_800c6798.facing_1c;

    this.deallocate();

    this.reinitializingWmap_80052c6c = false;
    engineStateOnceLoaded_8004dd24 = EngineStateEnum.SUBMAP_05;
    vsyncMode_8007a3b8 = 2;
  }

  @Method(0x800cce1cL)
  private void transitionToCombat() {
    gameState_800babc8.directionalPathIndex_4de = this.mapState_800c6798.directionalPathIndex_12;
    gameState_800babc8.pathIndex_4d8 = this.mapState_800c6798.pathIndex_14;
    gameState_800babc8.dotIndex_4da = this.mapState_800c6798.dotIndex_16;
    gameState_800babc8.dotOffset_4dc = this.mapState_800c6798.dotOffset_18;
    gameState_800babc8.facing_4dd = this.mapState_800c6798.facing_1c;

    this.handleAndRenderMapAndPlayer();
    this.deallocate();

    this.reinitializingWmap_80052c6c = false;
    engineStateOnceLoaded_8004dd24 = EngineStateEnum.COMBAT_06;
    vsyncMode_8007a3b8 = 2;
  }

  @Method(0x800cce9cL)
  private void transitionToWorldMap() {
    this.deallocate();
    this.reinitializingWmap_80052c6c = true;
    this.wmapState_800bb10c = WmapState.INIT_0;
  }

  @Method(0x800cceccL)
  private void unusedDeallocator() {
    this.deallocate();
    this.wmapState_800bb10c = WmapState.NOOP_11;
  }

  @Method(0x800ccef4L)
  private void setWmapStateToExitScreens() {
    this.wmapState_800bb10c = WmapState.EXIT_SCREENS_6;
  }

  private void transitionToTitle() {
    this.handleAndRenderMapAndPlayer();
    this.deallocate();
    resetSubmapToNewGame();

    this.reinitializingWmap_80052c6c = false;
    engineStateOnceLoaded_8004dd24 = EngineStateEnum.TITLE_02;
    vsyncMode_8007a3b8 = 2;
    drgnBinIndex_800bc058 = 1;
  }

  private void initCoolonMovePrompt() {
    if(gameState_800babc8.scriptFlags2_bc.get(0x15a)) {
      this.coolonPromptPopup = new WmapPromptPopup()
        .setPrompt("Move?")
        .addOptionText("No")
        .addOptionText("Yes")
        .setHighlight(
          WmapPromptPopup.HighlightMode.SELECTOR,
          new WmapMenuTextHighlight40(
            0.5f,
            new Vector3f(1.0f, 0.0f, 0.0f),
            new Rect4i(198, 54, 84, 16),
            1,
            2,
            2,
            true,
            Translucency.B_PLUS_F,
            51.0f
          )
        );
      this.coolonPromptPopup.setTranslation(WmapPromptPopup.ObjFields.PROMPT, 240.0f, 41.0f, textZ_800bdf00 * 4.0f);
      this.coolonPromptPopup.setTranslation(WmapPromptPopup.ObjFields.OPTIONS, 240.0f, 57.0f, textZ_800bdf00 * 4.0f - 2.0f);
      this.coolonPromptPopup.setOptionSpacing(16.0f);
    }
  }

  private void initMapMarkers() {
    this.modelAndAnimData_800c66a8.mapArrow = new MapMarker("MapArrow", 8, 16.0f, 16, 32, false);
    this.modelAndAnimData_800c66a8.coolonPlaceMarker = new MapMarker("CoolonPlaceMarker", 3, 10.0f, 16, 0, true);
  }

  @Method(0x800ccf04L)
  private void initWmapAudioVisuals() {
    this.worldMapState_800c6698 = WorldMapState.LOAD_MODEL_2;
    this.playerState_800c669c = PlayerState.LOAD_MODEL_2;
    loadWait = 60;
    this.atmosphericEffectStage_800c66a4 = 2;
    this.filesLoadedFlags_800c66b8.set(0);
    zOffset_1f8003e8 = 0;
    tmdGp0Tpage_1f8003ec = 0x20;

    this.initTransitionAnimation(FadeAnimationType.FADE_IN_1);
    this.initFlagsPathsCutsAndPlaces();
    this.loadWmapTextures();
    this.initCameraAndLight();
    this.loadMapModelAssets();
    this.loadPlayerAvatarTextureAndModelFiles();
    this.allocateSmoke();
    this.loadMapMcq();
    this.coolonQueenFuryOverlay = new CoolonQueenFuryOverlay();
    this.initCoolonMovePrompt();
    this.initMapMarkers();
    this.modelAndAnimData_800c66a8.zoomOverlay = new ZoomOverlay();

    if(this.mapState_800c6798.continent_00.continentNum < Continent.ILLISA_BAY_3.continentNum) { // South Serdio, North Serdio, Tiberoa
      loadLocationMenuSoundEffects(1);
    } else {
      //LAB_800cd004
      loadLocationMenuSoundEffects(this.mapState_800c6798.continent_00.continentNum + 1);
    }
    //LAB_800cd020
  }

  @Method(0x800cd030L)
  private void handleAndRenderMapAndPlayer() {
    this.updateMapCameraAndLights();
    this.tickTransitionAnimation();

    switch(this.worldMapState_800c6698) {
      case LOAD_MODEL_2 -> {
        if((this.filesLoadedFlags_800c66b8.get() & 0x2) != 0 && (this.filesLoadedFlags_800c66b8.get() & 0x4) != 0) { // World map textures and mesh loaded
          for(int i = 0; i < this.modelAndAnimData_800c66a8.tmdRendering_08.dobj2s_00.length; i++) {
            //LAB_800e3d44
            this.modelAndAnimData_800c66a8.tmdRendering_08.dobj2s_00[i].tmd_08 = this.modelAndAnimData_800c66a8.tmdRendering_08.tmd_14.tmd.objTable[i];
            this.modelAndAnimData_800c66a8.tmdRendering_08.dobj2s_00[i].obj = TmdObjLoader.fromObjTable("WmapModel (obj " + i + ')', this.modelAndAnimData_800c66a8.tmdRendering_08.dobj2s_00[i].tmd_08);
          }

          this.worldMapState_800c6698 = WorldMapState.INIT_MAP_ANIM_3;
        }
      }

      //LAB_800cd0d4
      case INIT_MAP_ANIM_3 -> {
        this.initMapAnimation();
        this.worldMapState_800c6698 = WorldMapState.NOOP_4;
      }

      case NOOP_4 -> this.worldMapState_800c6698 = WorldMapState.RENDER_5;

      case RENDER_5 -> this.renderWorldMap();

      case NOOP_6 -> this.worldMapState_800c6698 = WorldMapState.UNUSED_DEALLOC_7;

      case UNUSED_DEALLOC_7 -> {
        this.deallocateWorldMap();
        this.worldMapState_800c6698 = WorldMapState.UNINITIALIZED_0;
      }
    }

    //LAB_800cd148
    switch(this.playerState_800c669c) {
      case UNINITIALIZED_0 -> loadWait = 60 / vsyncMode_8007a3b8;

      case LOAD_MODEL_2 -> {
        if((this.filesLoadedFlags_800c66b8.get() & 0x2a8) == 0x2a8 && (this.filesLoadedFlags_800c66b8.get() & 0x550) == 0x550) {
          this.playerState_800c669c = PlayerState.INIT_PLAYER_MODEL_3;
        }
      }

      //LAB_800cd1dc
      case INIT_PLAYER_MODEL_3 -> {
        if(loadWait-- > 30 / vsyncMode_8007a3b8) break;
        this.initPlayerModelAndAnimation();

        // Init OpenGL models
        for(int i = 0; i < 4; i++) {
          TmdObjLoader.fromModel("WmapEntityModel (index " + i + ')', this.modelAndAnimData_800c66a8.models_0c[i]);
        }

        this.playerState_800c669c = PlayerState.NOOP_4;
      }

      case NOOP_4 -> {
        if(loadWait-- > 0) break;
        this.playerState_800c669c = PlayerState.RENDER_5;
      }

      case RENDER_5 -> this.renderPlayer();
      case NOOP_6 -> this.playerState_800c669c = PlayerState.UNUSED_DEALLOC_7;

      case UNUSED_DEALLOC_7 -> {
        for(final Model124 model : this.modelAndAnimData_800c66a8.models_0c) {
          model.deleteModelParts();
        }

        this.unloadWmapPlayerModels();
        this.playerState_800c669c = PlayerState.UNINITIALIZED_0;
      }
    }

    //LAB_800cd250
    this.renderMapBackground();
    this.renderMapOverlay();
    this.handleSmokeAndAtmosphericEffects();
  }

  @Method(0x800cd278L)
  private void deallocate() {
    if(this.modelAndAnimData_800c66a8.mapContinentNameObj != null) {
      this.modelAndAnimData_800c66a8.mapContinentNameObj.delete();
      this.modelAndAnimData_800c66a8.mapContinentNameObj = null;
    }

    if(this.mcqObj != null) {
      this.mcqObj.delete();
      this.mcqObj = null;
    }

    this.coolonQueenFuryOverlay.deallocate();
    this.coolonQueenFuryOverlay = null;

    this.mapState_800c6798.pathDots.delete();

    if(this.coolonPromptPopup != null) {
      this.coolonPromptPopup.deallocate();
      this.coolonPromptPopup = null;
    }

    this.deallocateWorldMap();
    this.unloadWmapPlayerModels();
    this.modelAndAnimData_800c66a8.deleteMapMarkers();
    this.deallocateAtmosphericEffect();
    this.modelAndAnimData_800c66a8.zoomOverlay.delete();
    this.deallocateSmoke();
    textZ_800bdf00 = 13;

    //LAB_800cd2d4
    for(int i = 0; i < 8; i++) {
      //LAB_800cd2f0
      clearTextbox(i);
      setTextAndTextboxesToUninitialized(i, 0);
    }

    //LAB_800cd32c
    vsyncMode_8007a3b8 = 2;
  }

  @Method(0x800d177cL)
  private void initCameraAndLight() {
    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;

    GsInitCoordinate2(null, cameraAndLights.coord2_20);

    cameraAndLights.coord2_20.coord.transfer.set(0, 0, 0);
    cameraAndLights.currMapRotation_70.zero();
    cameraAndLights.currRview2_00.viewpoint_00.set(0.0f, -300.0f, -900.0f);
    cameraAndLights.currRview2_00.refpoint_0c.set(0.0f, 300.0f, 900.0f);
    cameraAndLights.currRview2_00.viewpointTwist_18 = 0;
    cameraAndLights.currRview2_00.super_1c = cameraAndLights.coord2_20;

    this.initCamera();
    this.initLighting();

    cameraAndLights.projectionPlaneZoomTick_114 = 0;
    cameraAndLights.projectionPlaneDistance_118 = 1100.0f;
    cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.SELECT_0;
  }

  @Method(0x800d1914L)
  private void initLighting() {
    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;

    clearRed_8007a3a8 = 0;
    clearGreen_800bb104 = 0;
    clearBlue_800babc0 = 0;

    this.calculateDistancesToPlaces();

    //LAB_800d1984
    for(int i = 0; i < 3; i++) {
      //LAB_800d19a0
      final GsF_LIGHT light = cameraAndLights.lights_11c[i];
      light.r_0c = 0.125f;
      light.g_0d = 0.125f;
      light.b_0e = 0.125f;
      light.direction_00.x = MathHelper.sin(0.2617994f);
      light.direction_00.y = MathHelper.cos(5.497787f);
      light.direction_00.z = MathHelper.cosFromSin(0.2617994f, light.direction_00.x);
      light.direction_00.set(0.24414062f, 0.024414062f, 0.0f);
      GsSetFlatLight(i, light);
    }

    //LAB_800d1c88
    cameraAndLights.ambientLight_14c.set(0.375f, 0.375f, 0.375f);
    GTE.setBackgroundColour(cameraAndLights.ambientLight_14c.x, cameraAndLights.ambientLight_14c.y, cameraAndLights.ambientLight_14c.z);
    cameraAndLights.lightsUpdateState_88 = LightsUpdateState.INIT_DIMMING_0;
  }

  @Method(0x800d1d28L)
  private void initCamera() {
    this.wmapCameraAndLights19c0_800c66b0.mapRotating_80 = false;
    this.wmapCameraAndLights19c0_800c66b0.mapRotationStep_7c = 0.0f;
    this.wmapCameraAndLights19c0_800c66b0.cameraUpdateState_c5 = CameraUpdateState.AWAIT_INPUT_0;
    this.wmapCameraAndLights19c0_800c66b0.zoomStateIsLocal_c4 = true;

    this.initCameraMovement();
  }

  @Method(0x800d1d88L)
  private void updateMapCameraAndLights() {
    this.calculateDistancesToPlaces();
    this.updateMapAndCamera();
    this.updateLights();
  }

  @Method(0x800d1db8L)
  private void calculateDistancesToPlaces() {
    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    final float x = modelAndAnimData.coord2_34.coord.transfer.x;
    final float y = modelAndAnimData.coord2_34.coord.transfer.y;
    final float z = modelAndAnimData.coord2_34.coord.transfer.z;

    //LAB_800d1e14
    int count = 0;
    for(int i = 0; i < this.mapState_800c6798.locationCount_08; i++) {
      //LAB_800d1e38
      if(places_800f0234[locations_800f0e34[i].placeIndex_02].name_00 != null) {
        //LAB_800d1e90
        if(this.checkLocationIsValidAndOptionallySetPathStart(i, 1, cameraAndLights.locationDistances_154[count].position_08) == 0) {
          //LAB_800d1ee0
          final float dx = x - cameraAndLights.locationDistances_154[count].position_08.x;
          final float dy = y - cameraAndLights.locationDistances_154[count].position_08.y;
          final float dz = z - cameraAndLights.locationDistances_154[count].position_08.z;

          cameraAndLights.locationDistances_154[count].locationIndex_00 = i;
          cameraAndLights.locationDistances_154[count].distanceFromPlayer_04 = dx * dx + dy * dy + dz * dz;

          count++;
        }
      }
      //LAB_800d2070
    }

    //LAB_800d2088
    Arrays.sort(cameraAndLights.locationDistances_154, 0, count, Comparator.comparingDouble(a -> a.distanceFromPlayer_04));
  }

  @Method(0x800d219cL)
  private void updateLights() {
    if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.LOCAL_0) {
      return;
    }

    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;

    //LAB_800d21cc
    if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.TRANSITION_MODEL_OUT_2 || this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.WORLD_3) {
      //LAB_800d2228
      switch(cameraAndLights.lightsUpdateState_88) {
        case INIT_DIMMING_0:
          //LAB_800d2258
          //LAB_800d225c
          for(int i = 0; i < 3; i++) {
            //LAB_800d2278
            cameraAndLights.lightsColours_8c[i].x = (int)(cameraAndLights.lights_11c[i].r_0c * 0x100);
            cameraAndLights.lightsColours_8c[i].y = (int)(cameraAndLights.lights_11c[i].g_0d * 0x100);
            cameraAndLights.lightsColours_8c[i].z = (int)(cameraAndLights.lights_11c[i].b_0e * 0x100);
          }

          //LAB_800d235c
          cameraAndLights.lightsBrightness_84 = 1.0f;
          cameraAndLights.lightsUpdateState_88 = LightsUpdateState.DIM_1;

        case DIM_1:
          //LAB_800d237c
          cameraAndLights.lightsBrightness_84 -= 0.140625f / (3.0f / vsyncMode_8007a3b8);

          if(cameraAndLights.lightsBrightness_84 < 0.25f) {
            cameraAndLights.lightsBrightness_84 = 0.125f;
            cameraAndLights.lightsUpdateState_88 = LightsUpdateState.INIT_BRIGHTENING_2;
          }

          //LAB_800d23e0
          //LAB_800d23e4
          for(int i = 0; i < 3; i++) {
            final GsF_LIGHT light = cameraAndLights.lights_11c[i];

            //LAB_800d2400
            //LAB_800d2464
            //LAB_800d24d0
            //LAB_800d253c
            light.r_0c = cameraAndLights.lightsColours_8c[i].x * cameraAndLights.lightsBrightness_84 / 0x100;
            light.g_0d = cameraAndLights.lightsColours_8c[i].y * cameraAndLights.lightsBrightness_84 / 0x100;
            light.b_0e = cameraAndLights.lightsColours_8c[i].z * cameraAndLights.lightsBrightness_84 / 0x100;
            GsSetFlatLight(i, cameraAndLights.lights_11c[i]);
          }

          break;
      }
    }

    //LAB_800d2590
    //LAB_800d2598
    if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.TRANSITION_MODEL_IN_4) {
      //LAB_800d25d8
      switch(cameraAndLights.lightsUpdateState_88) {
        case INIT_BRIGHTENING_2:
          //LAB_800d2608
          cameraAndLights.lightsBrightness_84 = 0.25f;
          cameraAndLights.lightsUpdateState_88 = LightsUpdateState.BRIGHTEN_3;

        case BRIGHTEN_3:
          //LAB_800d2628
          cameraAndLights.lightsBrightness_84 += 0.140625f / (3.0f / vsyncMode_8007a3b8);

          if(cameraAndLights.lightsBrightness_84 > 1.0f) {
            cameraAndLights.lightsBrightness_84 = 1.0f;
            cameraAndLights.lightsUpdateState_88 = LightsUpdateState.INIT_DIMMING_0;
          }

          //LAB_800d268c
          //LAB_800d2690
          for(int i = 0; i < 3; i++) {
            final GsF_LIGHT light = cameraAndLights.lights_11c[i];

            //LAB_800d26ac
            //LAB_800d2710
            //LAB_800d277c
            //LAB_800d27e8
            light.r_0c = cameraAndLights.lightsColours_8c[i].x * cameraAndLights.lightsBrightness_84 / 0x100;
            light.g_0d = cameraAndLights.lightsColours_8c[i].y * cameraAndLights.lightsBrightness_84 / 0x100;
            light.b_0e = cameraAndLights.lightsColours_8c[i].z * cameraAndLights.lightsBrightness_84 / 0x100;
            GsSetFlatLight(i, cameraAndLights.lights_11c[i]);
          }

          break;
      }
    }
    //LAB_800d283c
    //LAB_800d2844
  }

  @Method(0x800d2d90L)
  private void updateMapAndCamera() {
    this.updateProjectionPlaneDistance();

    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;

    this.rotateCoord2(cameraAndLights.currMapRotation_70, cameraAndLights.coord2_20);

    if(cameraAndLights.cameraUpdateState_c5 == CameraUpdateState.AWAIT_INPUT_0) {
      if(cameraAndLights.zoomStateIsLocal_c4) {
        if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.LOCAL_0) {
          if(this.modelAndAnimData_800c66a8.coolonWarpState_220 == CoolonWarpState.NONE_0) {
            cameraAndLights.coord2_20.coord.transfer.set(this.modelAndAnimData_800c66a8.coord2_34.coord.transfer);
          }
        }
      }
    }

    //LAB_800d2ec4
    GsSetRefView2L(cameraAndLights.currRview2_00);
    this.handleMapRotation();

    MathHelper.floorMod(cameraAndLights.currMapRotation_70, MathHelper.TWO_PI);
    cameraAndLights.mapRotationEndAngle_7a = MathHelper.floorMod(cameraAndLights.mapRotationEndAngle_7a, MathHelper.TWO_PI);
  }

  @Method(0x800d2fa8L)
  private void handleMapRotation() {
    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;

    if(this.modelAndAnimData_800c66a8.fastTravelTransitionMode_250 == FastTravelTransitionMode.TELEPORT_1) {
      //LAB_800d401c
      cameraAndLights.currMapRotation_70.y += MathHelper.psxDegToRad(8) / (3.0f / vsyncMode_8007a3b8);
    } else {
      //LAB_800d2fd4
      if(this.modelAndAnimData_800c66a8.fastTravelTransitionMode_250 == FastTravelTransitionMode.OPEN_COOLON_MAP_2 && this.modelAndAnimData_800c66a8.fadeAnimationType_05 == FadeAnimationType.NONE_0) {
        return;
      }

      //LAB_800d3014
      if(cameraAndLights.mapRotationStep_7c == 0.0f) {
        cameraAndLights.mapRotating_80 = false;
      }

      //LAB_800d3040
      switch(this.wmapCameraAndLights19c0_800c66b0.mapRotationState_110) {
        case MAIN_LOOP_0:
          if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.LOCAL_0) {
            if(cameraAndLights.zoomStateIsLocal_c4) {
              if(this.mapState_800c6798.continent_00 != Continent.ENDINESS_7) {
                if(!cameraAndLights.mapRotating_80) {
                  //LAB_800d30d8
                  if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_1)) { // R1
                    cameraAndLights.mapRotationDirection = (int)MathHelper.floorMod((cameraAndLights.mapRotationDirection + 1), 8);
                    this.startMapRotation(1);
                    cameraAndLights.mapRotating_80 = true;
                  }

                  //LAB_800d310c
                  if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_1)) { // L1
                    cameraAndLights.mapRotationDirection = (int)MathHelper.floorMod((cameraAndLights.mapRotationDirection - 1), 8);
                    this.startMapRotation(-1);
                    cameraAndLights.mapRotating_80 = true;
                  }

                  //LAB_800d3140
                } else {
                  //LAB_800d3148
                  cameraAndLights.currMapRotation_70.y += cameraAndLights.mapRotationStep_7c / (3.0f / vsyncMode_8007a3b8);
                  cameraAndLights.mapRotationCounter_7e++;

                  if(cameraAndLights.mapRotationCounter_7e >= 18.0f / vsyncMode_8007a3b8) {
                    cameraAndLights.currMapRotation_70.y = cameraAndLights.mapRotationEndAngle_7a;
                    cameraAndLights.mapRotating_80 = false;
                  }
                }
              }
            }
          }

          //LAB_800d31e8
          this.handleCameraZoom();

          return;

        case INIT_SUBMAP_ZOOM_1:
          //LAB_800d3250
          this.initCameraMovement();
          cameraAndLights.mapRotationState_110 = MapRotationState.SUBMAP_ZOOM_2;

        case SUBMAP_ZOOM_2:
          //LAB_800d3228
          //LAB_800d3268
          cameraAndLights.currRview2_00.viewpoint_00.y = cameraAndLights.originalRview2_c8.viewpoint_00.y + cameraAndLights.viewpointSwoopStepY_ec * cameraAndLights.fadeOutZoomTick_10e / (3.0f / vsyncMode_8007a3b8);
          cameraAndLights.currRview2_00.viewpoint_00.z = cameraAndLights.originalRview2_c8.viewpoint_00.z + cameraAndLights.viewpointSwoopStepZ_f0 * cameraAndLights.fadeOutZoomTick_10e / (3.0f / vsyncMode_8007a3b8);
          cameraAndLights.currRview2_00.refpoint_0c.y = cameraAndLights.originalRview2_c8.refpoint_0c.y + cameraAndLights.refpointSwoopStepY_f8 * cameraAndLights.fadeOutZoomTick_10e / (3.0f / vsyncMode_8007a3b8);
          cameraAndLights.currRview2_00.refpoint_0c.z = cameraAndLights.originalRview2_c8.refpoint_0c.z + cameraAndLights.refpointSwoopStepZ_fc * cameraAndLights.fadeOutZoomTick_10e / (3.0f / vsyncMode_8007a3b8);
          cameraAndLights.currMapRotation_70.y = cameraAndLights.originalCameraRotation_10a + cameraAndLights.cameraRotationStep_10c * cameraAndLights.fadeOutZoomTick_10e / (3.0f / vsyncMode_8007a3b8);

          cameraAndLights.fadeOutZoomTick_10e++;
          if(cameraAndLights.fadeOutZoomTick_10e >= 48.0f / vsyncMode_8007a3b8) {
            cameraAndLights.fadeOutZoomTick_10e = (int)(48.0f / vsyncMode_8007a3b8);
            cameraAndLights.currMapRotation_70.y = cameraAndLights.finalCameraRotation_108;
          }

          break;
      }
      //LAB_800d342c
      //LAB_800d35e4
      //LAB_800d35ec
    }
  }

  @Method(0x800d35fcL)
  private void handleCameraZoom() {
    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;

    switch(cameraAndLights.cameraUpdateState_c5) {
      case AWAIT_INPUT_0:
        //LAB_800d3654
        //LAB_800d3670
        //LAB_800d368c
        if(
          this.mapState_800c6798.continent_00 != Continent.ENDINESS_7 &&
            this.mapState_800c6798.queenFuryForceMovementMode_d8 == ForcedMovementMode.NONE_0 &&
            this.tickMainMenuOpenTransition_800c6690 == 0
        ) {
          //LAB_800d36a8
          if(this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc != PathSegmentEndpointType.TERMINAL_1) {
            if(!cameraAndLights.mapRotating_80) {
              if(this.modelAndAnimData_800c66a8.fadeAnimationType_05 == FadeAnimationType.NONE_0) {
                if(cameraAndLights.mapRotationState_110 == MapRotationState.MAIN_LOOP_0) {
                  if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_2)) { // R2
                    if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.LOCAL_0) {
                      playSound(0, 4, (short)0, (short)0);
                      cameraAndLights.finalCameraY_9e = -9000;
                      cameraAndLights.cameraUpdateState_c5 = CameraUpdateState.ZOOM_OUT_1;
                      cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.INIT_VIEW_NEAR_1;
                      this.initCameraZoomPositionAndRotationSteps(0);
                      this.mapState_800c6798.disableInput_d0 = true;
                      cameraAndLights.zoomStateIsLocal_c4 = false;
                    }
                  }

                  //LAB_800d37bc
                  if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_2)) { // L2
                    if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.CONTINENT_1) {
                      //LAB_800d3814
                      setTextAndTextboxesToUninitialized(7, 0);
                      playSound(0, 4, (short)0, (short)0);
                      cameraAndLights.finalCameraY_9e = -300;
                      cameraAndLights.cameraUpdateState_c5 = CameraUpdateState.ZOOM_IN_2;
                      this.initCameraZoomPositionAndRotationSteps(1);
                      cameraAndLights.zoomStateIsLocal_c4 = true;
                      this.modelAndAnimData_800c66a8.zoomState_1f8 = ZoomState.LOCAL_0;
                      //LAB_800d3898
                    } else if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.LOCAL_0) {
                      playSound(0, 0x28, (short)0, (short)0);
                    }
                  }
                }
              }
            }
          }
        }
        break;

      case ZOOM_OUT_1:
        //LAB_800d38dc
        cameraAndLights.currRview2_00.viewpoint_00.y -= 1450.0f / (3.0f / vsyncMode_8007a3b8);
        cameraAndLights.currRview2_00.refpoint_0c.y += 1450.0f / (3.0f / vsyncMode_8007a3b8);
        cameraAndLights.currMapRotation_70.y = cameraAndLights.originalMapRotation_9a + cameraAndLights.mapRotationStep_9c * cameraAndLights.cameraZoomTick_a0;
        cameraAndLights.currCameraZoomPos_b4.add(
          cameraAndLights.cameraZoomPosStep_a4.x / (3.0f / vsyncMode_8007a3b8),
          cameraAndLights.cameraZoomPosStep_a4.y / (3.0f / vsyncMode_8007a3b8),
          cameraAndLights.cameraZoomPosStep_a4.z / (3.0f / vsyncMode_8007a3b8)
        );
        cameraAndLights.coord2_20.coord.transfer.set(
          (this.modelAndAnimData_800c66a8.coord2_34.coord.transfer.x - cameraAndLights.currCameraZoomPos_b4.x),
          (this.modelAndAnimData_800c66a8.coord2_34.coord.transfer.y - cameraAndLights.currCameraZoomPos_b4.y),
          (this.modelAndAnimData_800c66a8.coord2_34.coord.transfer.z - cameraAndLights.currCameraZoomPos_b4.z)
        );
        cameraAndLights.cameraZoomTick_a0 += 1.0f / (3.0f / vsyncMode_8007a3b8);

        if(cameraAndLights.cameraZoomTick_a0 >= 6.0f) {
          cameraAndLights.currRview2_00.viewpoint_00.y = cameraAndLights.finalCameraY_9e;
          cameraAndLights.currRview2_00.refpoint_0c.y = -cameraAndLights.finalCameraY_9e;
          cameraAndLights.currMapRotation_70.y = cameraAndLights.finalMapRotation_98;
          cameraAndLights.coord2_20.coord.transfer.set(0, 0, 0);
          cameraAndLights.cameraUpdateState_c5 = CameraUpdateState.AWAIT_INPUT_0;
          this.modelAndAnimData_800c66a8.zoomState_1f8 = ZoomState.CONTINENT_1;
        }
        break;

      case ZOOM_IN_2:
        //LAB_800d3bd8
        if(this.modelAndAnimData_800c66a8.fadeAnimationType_05 == FadeAnimationType.NONE_0) {
          cameraAndLights.currRview2_00.viewpoint_00.y += 1450.0f / (3.0f / vsyncMode_8007a3b8);
          cameraAndLights.currRview2_00.refpoint_0c.y -= 1450.0f / (3.0f / vsyncMode_8007a3b8);
        } else {
          //LAB_800d3c44
          cameraAndLights.currRview2_00.viewpoint_00.y += 290.0f / (3.0f / vsyncMode_8007a3b8);
          cameraAndLights.currRview2_00.refpoint_0c.y -= 290.0f / (3.0f / vsyncMode_8007a3b8);
        }

        //LAB_800d3c8c
        cameraAndLights.currMapRotation_70.y = cameraAndLights.originalMapRotation_9a + cameraAndLights.mapRotationStep_9c * cameraAndLights.cameraZoomTick_a0;
        cameraAndLights.currCameraZoomPos_b4.add(
          cameraAndLights.cameraZoomPosStep_a4.x / (3.0f / vsyncMode_8007a3b8),
          cameraAndLights.cameraZoomPosStep_a4.y / (3.0f / vsyncMode_8007a3b8),
          cameraAndLights.cameraZoomPosStep_a4.z / (3.0f / vsyncMode_8007a3b8)
        );
        cameraAndLights.coord2_20.coord.transfer.set(cameraAndLights.currCameraZoomPos_b4);
        cameraAndLights.cameraZoomTick_a0 += 1.0f / (3.0f / vsyncMode_8007a3b8);

        boolean zoomComplete = false;
        if(this.modelAndAnimData_800c66a8.fadeAnimationType_05 == FadeAnimationType.NONE_0) {
          if(cameraAndLights.cameraZoomTick_a0 >= 6.0f) {
            zoomComplete = true;
          }
          //LAB_800d3e78
          //LAB_800d3e80
        } else if(cameraAndLights.cameraZoomTick_a0 >= 30.0f) {
          zoomComplete = true;
        }

        //LAB_800d3ea8
        if(zoomComplete) {
          cameraAndLights.currRview2_00.viewpoint_00.y = cameraAndLights.finalCameraY_9e;
          cameraAndLights.currRview2_00.refpoint_0c.y = -cameraAndLights.finalCameraY_9e;
          cameraAndLights.currMapRotation_70.y = cameraAndLights.finalMapRotation_98;
          cameraAndLights.coord2_20.coord.transfer.set(this.modelAndAnimData_800c66a8.coord2_34.coord.transfer);
          cameraAndLights.cameraUpdateState_c5 = CameraUpdateState.AWAIT_INPUT_0;
          this.mapState_800c6798.disableInput_d0 = false;
          this.modelAndAnimData_800c66a8.zoomState_1f8 = ZoomState.LOCAL_0;
        }
        break;
    }

    //LAB_800d38d4
    //LAB_800d3bd0
    //LAB_800d3fa4
    //LAB_800d3fac
    this.renderPlayerAndDestinationIndicators();
  }

  @Method(0x800d4058L)
  private void renderPlayerAndDestinationIndicators() {
    //LAB_800d4088
    if(
      this.wmapCameraAndLights19c0_800c66b0.zoomStateIsLocal_c4 ||
        this.wmapCameraAndLights19c0_800c66b0.cameraUpdateState_c5 != CameraUpdateState.AWAIT_INPUT_0
    ) {
      //LAB_800d41f0
      return;
    }

    //LAB_800d40ac
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    final ZoomState zoomState = modelAndAnimData.zoomState_1f8;

    if(zoomState == ZoomState.CONTINENT_1) {
      //LAB_800d4108
      this.destinationLabelStage_800c86f0 = 0;
    } else if(zoomState == ZoomState.WORLD_3) {
      //LAB_800d4170
      if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_2)) { // L2
        setTextAndTextboxesToUninitialized(7, 0);
        this.shouldSetDestLabelMetrics = false;
        this.destLabelName = null;
      }
      //LAB_800d4198
      //LAB_800d40e8
    } else if(zoomState == ZoomState.TRANSITION_MODEL_IN_4) {
      //LAB_800d41b0
      setTextAndTextboxesToUninitialized(7, 0);

      if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_2)) { // R2
        this.destinationLabelStage_800c86f0 = 0;
      }

      //LAB_800d41e0
      return;
    } else {
      return;
    }

    //LAB_800d41f8
    final MV wmapRotation = new MV();
    this.rotateCoord2(modelAndAnimData.tmdRendering_08.rotations_08[0], modelAndAnimData.tmdRendering_08.coord2s_04[0]);
    GsGetLs(modelAndAnimData.tmdRendering_08.coord2s_04[0], wmapRotation);
    GTE.setTransforms(wmapRotation);

    final Vector2f playerArrowXy = new Vector2f(); // sxy2
    perspectiveTransform(modelAndAnimData.coord2_34.coord.transfer, playerArrowXy);

    // Player arrow on map
    final int u = (int)(tickCount_800bb0fc / (3.0f / vsyncMode_8007a3b8)) & 0x7;
    float x = GPU.getOffsetX() + playerArrowXy.x - modelAndAnimData.mapArrow.getSize() / 2.0f;
    float y = GPU.getOffsetY() + playerArrowXy.y - modelAndAnimData.mapArrow.getSize() - (zoomState == ZoomState.WORLD_3 ? 8 : 0); // Needs adjustment since we shifted the world map MCQ 8 pixels down
    modelAndAnimData.mapArrow.render(u, 0, x, y, 100.0f);

    if(modelAndAnimData.zoomState_1f8 == ZoomState.WORLD_3) {
      //LAB_800d44d0
      int destinationIndex = 0;

      //LAB_800d44d8
      for(int i = 0; i < 49; i++) {
        //LAB_800d4518
        if(gameState_800babc8.scriptFlags2_bc.get(wmapDestinationMarkers_800f5a6c[i].packedFlag_00)) {
          //LAB_800d45cc
          destinationIndex = i;
        }
        //LAB_800d45d8
      }

      //LAB_800d45f0
      if(destinationIndex != 0) {
        //LAB_800d4608
        // Destination arrow on map
        x = GPU.getOffsetX() + wmapDestinationMarkers_800f5a6c[destinationIndex].x_24 - 160;
        y = GPU.getOffsetY() + wmapDestinationMarkers_800f5a6c[destinationIndex].y_26 - 120;
        modelAndAnimData.mapArrow.render(u, 1, x, y, 100.0f);

        final String placeName = places_800f0234[wmapDestinationMarkers_800f5a6c[destinationIndex].placeIndex_28].name_00;
        if(placeName != null) {
          //LAB_800d4878
          final int textboxX = wmapDestinationMarkers_800f5a6c[destinationIndex].x_24;
          final int textboxY = wmapDestinationMarkers_800f5a6c[destinationIndex].y_26 - 8;

          final IntRef width = new IntRef();
          final IntRef lines = new IntRef();
          this.measureText(placeName, width, lines);

          final int labelStage = this.destinationLabelStage_800c86f0;
          textboxes_800be358[7].chars_18 = Math.max(width.get(), 4);
          textboxes_800be358[7].lines_1a = lines.get();
          //LAB_800d4974
          if(labelStage == 0) {
            //LAB_800d4988
            initTextbox(7, false, textboxX, textboxY, width.get() - 1, lines.get() - 1);

            //LAB_800d49e4
            this.destinationLabelStage_800c86f0 = 2;
          } else if(labelStage == 1) {
            //LAB_800d49e4
            this.destinationLabelStage_800c86f0 = 2;
          } else if(labelStage == 2) {
            //LAB_800d4a40
            //LAB_800d4a6c
            textboxes_800be358[7].width_1c = textboxes_800be358[7].chars_18 * 9 / 2;
            textboxes_800be358[7].height_1e = textboxes_800be358[7].lines_1a * 6 + 4;
            textboxes_800be358[7].x_14 = textboxX;
            textboxes_800be358[7].y_16 = textboxY - 4;
          }

          //LAB_800d4aec
          textboxes_800be358[7].z_0c = 27;

          if(this.shouldSetDestLabelMetrics) {
            this.shouldSetDestLabelMetrics = false;
            this.destLabelName = placeName;
            this.destLabelX = textboxX;
            this.destLabelY = textboxY - lines.get() * 7 - 3;
          }
        }
      }
    }
  }

  @Method(0x800d4bc8L)
  private void initCameraZoomPositionAndRotationSteps(final int zoomMode) {
    final float angleOffset;
    final float ccwAngle;
    final float cwAngle;
    final float finalAngle;

    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;

    if(zoomMode == 0) {
      cameraAndLights.originalMapRotation_9a = cameraAndLights.currMapRotation_70.y;
      cameraAndLights.finalMapRotation_98 = 0;
      cwAngle = cameraAndLights.finalMapRotation_98 - cameraAndLights.originalMapRotation_9a;
      ccwAngle = cameraAndLights.finalMapRotation_98 - (cameraAndLights.originalMapRotation_9a - MathHelper.TWO_PI);
    } else {
      //LAB_800d4c80
      cameraAndLights.finalMapRotation_98 = cameraAndLights.originalMapRotation_9a;
      cameraAndLights.originalMapRotation_9a = cameraAndLights.currMapRotation_70.y;

      if(cameraAndLights.originalMapRotation_9a < cameraAndLights.finalMapRotation_98) {
        angleOffset = -MathHelper.TWO_PI;
      } else {
        //LAB_800d4cf8
        angleOffset = MathHelper.TWO_PI;
      }

      //LAB_800d4d00
      cwAngle = cameraAndLights.finalMapRotation_98 - cameraAndLights.originalMapRotation_9a;
      ccwAngle = cameraAndLights.finalMapRotation_98 + angleOffset - cameraAndLights.originalMapRotation_9a;
    }

    //LAB_800d4d64
    final Vector3f transfer = this.modelAndAnimData_800c66a8.coord2_34.coord.transfer;
    cameraAndLights.cameraZoomPosStep_a4.x = transfer.x / 6.0f;
    cameraAndLights.cameraZoomPosStep_a4.y = transfer.y / 6.0f;
    cameraAndLights.cameraZoomPosStep_a4.z = transfer.z / 6.0f;
    cameraAndLights.currCameraZoomPos_b4.zero();

    if(Math.abs(ccwAngle) < Math.abs(cwAngle)) {
      finalAngle = ccwAngle;
    } else {
      finalAngle = cwAngle;
    }

    //LAB_800d4e88
    cameraAndLights.mapRotationStep_9c = finalAngle / 6.0f;
    cameraAndLights.cameraZoomTick_a0 = 0;
  }

  @Method(0x800d4ed8L)
  private void startMapRotation(final int direction) {
    final float angleDelta = MathHelper.TWO_PI / 8.0f;

    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;
    cameraAndLights.mapRotationCounter_7e = 0;
    cameraAndLights.mapRotationStartAngle_78 = cameraAndLights.currMapRotation_70.y;
    cameraAndLights.mapRotationEndAngle_7a = cameraAndLights.mapRotationDirection * angleDelta;
    final float cwAngle = -direction * angleDelta;
    final float ccwAngle = cwAngle + MathHelper.TWO_PI;
    final float finalAngle;
    if(Math.abs(ccwAngle) < Math.abs(cwAngle)) {
      finalAngle = ccwAngle;
    } else {
      finalAngle = cwAngle;
    }

    //LAB_800d4fd0
    cameraAndLights.mapRotationStep_7c = -finalAngle / 6.0f;
  }

  /** This is used exclusively for the fade in/out movement, not zooming. */
  @Method(0x800d5018L)
  private void initCameraMovement() {
    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;
    cameraAndLights.mapRotationState_110 = MapRotationState.MAIN_LOOP_0;
    cameraAndLights.fadeOutZoomTick_10e = 0;
    cameraAndLights.originalRview2_c8.viewpoint_00.set(cameraAndLights.currRview2_00.viewpoint_00);
    cameraAndLights.originalRview2_c8.refpoint_0c.set(cameraAndLights.currRview2_00.refpoint_0c);
    cameraAndLights.originalRview2_c8.viewpointTwist_18 = cameraAndLights.currRview2_00.viewpointTwist_18;
    cameraAndLights.originalRview2_c8.super_1c = cameraAndLights.currRview2_00.super_1c;
    cameraAndLights.viewpointSwoopStepY_ec = (-100.0f - cameraAndLights.originalRview2_c8.viewpoint_00.y) / 16.0f;
    cameraAndLights.viewpointSwoopStepZ_f0 = (-600.0f - cameraAndLights.originalRview2_c8.viewpoint_00.z) / 16.0f;
    cameraAndLights.refpointSwoopStepY_f8 = (-90.0f - cameraAndLights.originalRview2_c8.refpoint_0c.y) / 16.0f;
    cameraAndLights.refpointSwoopStepZ_fc = -cameraAndLights.originalRview2_c8.refpoint_0c.z / 16.0f;
    cameraAndLights.originalCameraRotation_10a = cameraAndLights.currMapRotation_70.y;

    final float reversePlayerRotation = this.modelAndAnimData_800c66a8.playerRotation_a4.y + MathHelper.PI;
    cameraAndLights.finalCameraRotation_108 = reversePlayerRotation;

    final float cwAngle = cameraAndLights.currMapRotation_70.y - reversePlayerRotation;
    final float ccwAngle = cameraAndLights.currMapRotation_70.y - (reversePlayerRotation - MathHelper.TWO_PI);
    final float finalAngle;
    if(Math.abs(ccwAngle) < Math.abs(cwAngle)) {
      finalAngle = ccwAngle;
    } else {
      finalAngle = cwAngle;
    }

    //LAB_800d5244
    cameraAndLights.cameraRotationStep_10c = -finalAngle / 16.0f;
  }

  @Method(0x800d5288L)
  private void updateProjectionPlaneDistance() {
    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;

    switch(cameraAndLights.projectionDistanceState_11a) {
      case SELECT_0:
        if(cameraAndLights.locationDistances_154[0].distanceFromPlayer_04 < 8100.0f) {
          cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.INIT_VIEW_NEAR_1;
          //LAB_800d52e8
        } else if(
          this.modelAndAnimData_800c66a8.fadeAnimationType_05 == FadeAnimationType.NONE_0 ||
            cameraAndLights.cameraUpdateState_c5 != CameraUpdateState.ZOOM_IN_2
        ) {
          //LAB_800d5328
          cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.INIT_VIEW_FAR_3;
        } else {
          return;
        }

        break;

      case INIT_VIEW_NEAR_1:
        //LAB_800d5394
        cameraAndLights.projectionPlaneZoomTick_114 = 0;
        cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.MAIN_LOOP_NEAR_2;

      case MAIN_LOOP_NEAR_2:
        //LAB_800d53b4
        cameraAndLights.projectionPlaneZoomTick_114++;

        //LAB_800d5424
        cameraAndLights.projectionPlaneDistance_118 += Math.max(4, 64 - cameraAndLights.projectionPlaneZoomTick_114 * 2) / (3.0f / vsyncMode_8007a3b8);

        if(cameraAndLights.projectionPlaneDistance_118 >= 800.0f) {
          cameraAndLights.projectionPlaneDistance_118 = 800.0f;
          cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.SELECT_0;
        }

        break;

      case INIT_VIEW_FAR_3:
        //LAB_800d5494
        if(!cameraAndLights.zoomStateIsLocal_c4) {
          cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.SELECT_0;
          return;
        }

        //LAB_800d54c8
        cameraAndLights.projectionPlaneZoomTick_114 = 0;
        cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.MAIN_LOOP_FAR_4;

      case MAIN_LOOP_FAR_4:
        //LAB_800d54e8
        cameraAndLights.projectionPlaneZoomTick_114++;

        //LAB_800d5558
        cameraAndLights.projectionPlaneDistance_118 -= Math.max(4, 64 - cameraAndLights.projectionPlaneZoomTick_114 * 2) / (3.0f / vsyncMode_8007a3b8);

        if(cameraAndLights.projectionPlaneDistance_118 <= 600.0f) {
          cameraAndLights.projectionPlaneDistance_118 = 600.0f;
          cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.SELECT_0;
        }

        break;
    }

    setProjectionPlaneDistance(cameraAndLights.projectionPlaneDistance_118);
  }

  @Method(0x800d562cL)
  private void loadMapMcqToVram(final FileData data) {
    final McqHeader mcq = new McqHeader(data);

    //LAB_800d568c
    GPU.uploadData15(new Rect4i(320, 0, mcq.vramWidth_08, mcq.vramHeight_0a), mcq.imageData);
    this.mcqHeader_800c6768 = mcq;

    this.filesLoadedFlags_800c66b8.updateAndGet(val -> val | 0x1);
  }

  @Method(0x800d5768L)
  private void loadLocationThumbnailImage(final Tim tim) {
    this.loadLocationThumbnailImage(tim, 448, 256, 768, 508);
    this.filesLoadedFlags_800c66b8.updateAndGet(val -> val | 0x800);

    //LAB_800d5848
  }

  /** Loads general world map stuff (location text, doors, buttons, etc.), several blobs that may be smoke?, tons of terrain and terrain sprites */
  @Method(0x800d5858L)
  private void timsLoaded(final List<FileData> files, final int fileFlag) {
    //LAB_800d5874
    for(final FileData file : files) {
      //LAB_800d5898
      if(file.size() != 0) {
        //LAB_800d58c8
        new Tim(file).uploadToGpu();
      }
    }

    //LAB_800d5938
    this.filesLoadedFlags_800c66b8.updateAndGet(val -> val | fileFlag);

    //LAB_800d5970
  }

  @Method(0x800d5984L)
  private void loadTmdCallback(final String modelName, final FileData file) {
    final TmdWithId tmd = new TmdWithId(modelName, file);

    this.modelAndAnimData_800c66a8.tmdRendering_08 = this.loadTmd(tmd);
    this.initTmdTransforms(this.modelAndAnimData_800c66a8.tmdRendering_08, null);
    this.modelAndAnimData_800c66a8.tmdRendering_08.tmd_14 = tmd;
    this.setAllCoord2Attribs(this.modelAndAnimData_800c66a8.tmdRendering_08, 0);
    this.filesLoadedFlags_800c66b8.updateAndGet(val -> val | 0x4);
  }

  @Method(0x800d5a30L)
  private void loadPlayerAvatarModelFiles(final List<FileData> files, final int whichFile) {
    if(files.get(0).size() != 0) {
      this.modelAndAnimData_800c66a8.playerModelTmdFileData_b4[whichFile].extendedTmd_00 = new CContainer("DRGN0/" + (5714 + whichFile), files.get(0));
    }

    //LAB_800d5a48
    for(int i = 2; i < Math.min(16, files.size()); i++) {
      //LAB_800d5a6c
      if(files.get(i).size() != 0) {
        //LAB_800d5a9c
        //LAB_800d5ab8
        this.modelAndAnimData_800c66a8.playerModelTmdFileData_b4[whichFile].tmdAnim_08[i - 2] = new TmdAnimationFile(files.get(i));
      }
      //LAB_800d5b2c
    }

    //LAB_800d5b44
    if(whichFile == 0) {
      //LAB_800d5bb8
      this.filesLoadedFlags_800c66b8.updateAndGet(val -> val | 0x10);
    } else if(whichFile == 1) {
      //LAB_800d5bd8
      this.filesLoadedFlags_800c66b8.updateAndGet(val -> val | 0x40);
      //LAB_800d5b98
    } else if(whichFile == 2) {
      //LAB_800d5bf8
      this.filesLoadedFlags_800c66b8.updateAndGet(val -> val | 0x100);
    } else if(whichFile == 3) {
      //LAB_800d5c18
      this.filesLoadedFlags_800c66b8.updateAndGet(val -> val | 0x400);
    }
    //LAB_800d5c38
    //LAB_800d5c40
  }

  @Method(0x800d5c50L)
  private void loadLocationThumbnailImage(final Tim tim, final int imageX, final int imageY, final int clutX, final int clutY) {
    final Rect4i imageRect = tim.getImageRect();
    final Rect4i rect = new Rect4i(imageX, imageY, imageRect.w, imageRect.h);
    GPU.uploadData15(rect, tim.getImageData());

    if((tim.getFlags() & 0x8) != 0 && clutX != -1) {
      final Rect4i clutRect = tim.getClutRect();
      rect.set(clutX, clutY, clutRect.w, clutRect.h);
      GPU.uploadData15(rect, tim.getClutData());
    }
    //LAB_800d5d84
  }

  @Method(0x800d5e70L)
  private WaterAnimation20 prepareWaterAnimation() {
    final WaterAnimation20 anim = new WaterAnimation20();
    anim.x_00 = 448;
    anim.y_02 = 0;
    anim.w_04 = 16;
    anim.h_06 = 64;
    anim.imageData_08 = new FileData(new byte[2048]);
    anim.imageData_0c = new FileData(new byte[2048]);
    anim.currTick_1c = 2;
    return anim;
  }

  @Method(0x800d6080L)
  private void animateWater(final WaterAnimation20 anim) {
    //LAB_800d60b0
    anim.currTick_1c += 1.0f / (3.0f / vsyncMode_8007a3b8);

    if(anim.currTick_1c >= 2.0f) {
      final Rect4i src0 = new Rect4i();
      final Rect4i src1 = new Rect4i();
      final Rect4i dest0 = new Rect4i();
      final Rect4i dest1 = new Rect4i();

      //LAB_800d60f8
      anim.currTick_1c = 0.0f;

      //LAB_800d6460
      //LAB_800d6468
      src0.set(
        anim.x_00,
        anim.y_02 + anim.h_06 - 1,
        anim.w_04,
        1
      );

      src1.set(
        anim.x_00,
        anim.y_02,
        anim.w_04,
        anim.h_06 - 1
      );

      dest0.set(
        anim.x_00,
        anim.y_02,
        anim.w_04,
        1
      );

      dest1.set(
        anim.x_00,
        anim.y_02 + 1,
        anim.w_04,
        anim.h_06 - 1
      );

      //LAB_800d67a8
      GPU.downloadData15(src0, anim.imageData_0c);
      GPU.downloadData15(src1, anim.imageData_08);
      GPU.uploadData15(dest0, anim.imageData_0c);
      GPU.uploadData15(dest1, anim.imageData_08);

      //LAB_800d6804
    }
  }

  @Method(0x800d6880L)
  private void loadWmapTextures() {
    this.filesLoadedFlags_800c66b8.updateAndGet(val -> val & 0xffff_efff);
    loadDrgnDir(0, 5695, files -> this.timsLoaded(files, 0x1_1000));
    this.modelAndAnimData_800c66a8.mapTextureBrightness_20 = 0.0f;
  }

  /** Path, continent name, zoom level indicator */
  @Method(0x800d6900L)
  private void renderMapOverlay() {
    if((this.filesLoadedFlags_800c66b8.get() & 0x1000) == 0) {
      return;
    }

    //LAB_800d692c
    if(this.modelAndAnimData_800c66a8.fastTravelTransitionMode_250 == FastTravelTransitionMode.OPEN_COOLON_MAP_2) {
      return;
    }

    if(this.modelAndAnimData_800c66a8.mapContinentNameObj == null) {
      this.modelAndAnimData_800c66a8.mapContinentNameObj = new QuadBuilder("WmapName")
        .bpp(Bpp.BITS_4)
        .clut(640, 497)
        .vramPos(640, 256)
        .pos(GPU.getOffsetX() - 144.0f, GPU.getOffsetY() - 104.0f, 54.0f)
        .size(128.0f, 24.0f)
        .uv(128.0f, this.mapState_800c6798.continent_00.continentNum * 24.0f)
        .build();
    }

    //LAB_800d6950
    // Continent name
    this.modelAndAnimData_800c66a8.mapOverlayTransforms.identity();
    RENDERER.queueOrthoModel(this.modelAndAnimData_800c66a8.mapContinentNameObj, this.modelAndAnimData_800c66a8.mapOverlayTransforms, QueuedModelStandard.class)
      .monochrome(this.modelAndAnimData_800c66a8.mapTextureBrightness_20);

    this.modelAndAnimData_800c66a8.mapTextureBrightness_20 += 0.25f / (3.0f / vsyncMode_8007a3b8);

    if(this.modelAndAnimData_800c66a8.mapTextureBrightness_20 > 1.0f) {
      this.modelAndAnimData_800c66a8.mapTextureBrightness_20 = 1.0f;
    }

    //LAB_800d6b5c
    this.renderPath();

    if(this.mapState_800c6798.continent_00 == Continent.ENDINESS_7) {
      return;
    }

    //LAB_800d6b80
    if(this.mapState_800c6798.queenFuryForceMovementMode_d8 != ForcedMovementMode.NONE_0) {
      return;
    }

    // Render map zoom level pyramid thing
    this.modelAndAnimData_800c66a8.zoomOverlay.render(this.modelAndAnimData_800c66a8.zoomState_1f8);
  }

  @Method(0x800d7a34L)
  private void renderPath() {
    if(
      this.worldMapState_800c6698.state <= WorldMapState.INIT_MAP_ANIM_3.state ||
      this.playerState_800c669c.state <= PlayerState.INIT_PLAYER_MODEL_3.state
    ) {
      return;
    }

    if(this.mapState_800c6798.locationCount_08 == 0) {
      return;
    }

    //LAB_800d7a80
    final ZoomState zoomState = this.modelAndAnimData_800c66a8.zoomState_1f8;
    if(zoomState == ZoomState.TRANSITION_MODEL_OUT_2 || zoomState == ZoomState.WORLD_3 || zoomState == ZoomState.TRANSITION_MODEL_IN_4) {
      //LAB_800d7af8
      return;
    }

    //LAB_800d7b00
    //LAB_800d7b64
    //LAB_800d7b58
    //LAB_800d7b38

    final Vector3f intersectionPoint = new Vector3f();

    //LAB_800d7b84
    final int bigDotStateIndex = (int)(tickCount_800bb0fc / 5 / (3.0f / vsyncMode_8007a3b8) % 3);

    final float x = this.modelAndAnimData_800c66a8.coord2_34.coord.transfer.x;
    final float y = this.modelAndAnimData_800c66a8.coord2_34.coord.transfer.y;
    final float z = this.modelAndAnimData_800c66a8.coord2_34.coord.transfer.z;

    this.rotateCoord2(this.modelAndAnimData_800c66a8.tmdRendering_08.rotations_08[0], this.modelAndAnimData_800c66a8.tmdRendering_08.coord2s_04[0]);

    final MV lw = new MV();
    final MV ls = new MV();
    GsGetLws(this.modelAndAnimData_800c66a8.tmdRendering_08.coord2s_04[0], lw, ls);
    GTE.setTransforms(ls);

    //LAB_800d7d6c
    for(int i = 0; i < this.mapState_800c6798.locationCount_08; i++) {
      //LAB_800d7d90
      if(this.checkLocationIsValidAndOptionallySetPathStart(i, 1, intersectionPoint) == 0) {
        //LAB_800d7db4
        if(this.mapState_800c6798.continent_00 != Continent.ENDINESS_7 || i == 31 || i == 78) {
          this.mapState_800c6798.pathDots.transforms.set(lw)
            .rotateLocalX(-MathHelper.PI / 2.0f);
          if(zoomState == ZoomState.LOCAL_0) {
            this.mapState_800c6798.pathDots.transforms.scale(0.5f);
          } else {
            this.mapState_800c6798.pathDots.transforms.scale(0.25f);
          }
          this.mapState_800c6798.pathDots.transforms.transfer.add(intersectionPoint).y -= 1.0f;

          final QueuedModelStandard model = RENDERER.queueModel(this.mapState_800c6798.pathDots.dots, this.mapState_800c6798.pathDots.transforms, QueuedModelStandard.class)
            .vertices(bigDotStateIndex * 4, 4);

          //LAB_800d7df0
          if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.LOCAL_0) {
            final float dx = x - intersectionPoint.x;
            final float dy = y - intersectionPoint.y;
            final float dz = z - intersectionPoint.z;
            final float baseColour = Math.max(0, 0x200 - Math.sqrt(dx * dx + dy * dy + dz * dz)) / 2;

            model.colour(baseColour * 31 / 256 / 64.0f, baseColour * 63 / 256 / 64.0f, 0.0f);
          } else {
            //LAB_800d8048
            model.colour(31 / 64.0f, 63 / 64.0f, 0.0f);
          }
        }
      }
      //LAB_800d84c0
    }

    final boolean[] pathSegmentsRendered = new boolean[0xff];

    //LAB_800d852c
    //LAB_800d8540
    for(int i = 0; i < this.mapState_800c6798.locationCount_08; i++) {
      //LAB_800d8564
      if(this.checkLocationIsValidAndOptionallySetPathStart(i, 0, null) == 0) {
        //LAB_800d8584
        if(this.mapState_800c6798.continent_00 != Continent.ENDINESS_7 || i == 31 || i == 78) {
          //LAB_800d85c0
          final int pathIndexAndDirection = directionalPathSegmentData_800f2248[locations_800f0e34[i].directionalPathIndex_00].pathSegmentIndexAndDirection_00;
          final int pathSegmentIndex = Math.abs(pathIndexAndDirection) - 1;

          if(!pathSegmentsRendered[pathSegmentIndex]) {
            //LAB_800d863c
            pathSegmentsRendered[pathSegmentIndex] = true;
            final int pathPointCount = pathSegmentLengths_800f5810[pathSegmentIndex] - 1;

            final Vector3f[] dots = pathDotPosArr_800f591c[pathSegmentIndex];
            final int pathPointIndexBase = pathIndexAndDirection >= 0 ? 0 : pathPointCount - 1;

            //LAB_800d86d0
            //LAB_800d86d4
            for(int pathPointIndex = 0; pathPointIndex < pathPointCount; pathPointIndex++) {
              //LAB_800d86f4
              final Vector3f pathPoint;
              if(pathIndexAndDirection > 0) {
                pathPoint = dots[pathPointIndexBase + pathPointIndex];
              } else {
                //LAB_800d8784
                pathPoint = dots[pathPointIndexBase - pathPointIndex];
              }

              this.mapState_800c6798.pathDots.transforms.set(lw)
                .rotateLocalX(-MathHelper.PI / 2.0f)
                .scale(0.25f);
              this.mapState_800c6798.pathDots.transforms.transfer.add(pathPoint.x, pathPoint.y, pathPoint.z).y -= 1.0f;

              final QueuedModelStandard model = RENDERER.queueModel(this.mapState_800c6798.pathDots.dots, this.mapState_800c6798.pathDots.transforms, QueuedModelStandard.class)
                .vertices(12, 4);

              //LAB_800d87fc
              if(zoomState == ZoomState.LOCAL_0) {
                final float dx = x - pathPoint.x;
                final float dy = y - pathPoint.y;
                final float dz = z - pathPoint.z;
                final float baseColour = Math.max(0, 0x200 - Math.sqrt(dx * dx + dy * dy + dz * dz)) / 2.0f;

                model.colour(baseColour * 47 / 256 / 64.0f, baseColour * 39 / 256 / 64.0f, 0.0f);
              } else {
                //LAB_800d8b40
                model.colour(0x2f / 64.0f, 0x27 / 64.0f, 0.0f);
              }
            }
          }
        }
      }
      //LAB_800d8ce0
    }
    //LAB_800d8cf8
    //LAB_800d8d04
  }

  /** Used to initialize Coolon move prompt selector as well */
  @Method(0x800d8d18L)
  private void loadMapModelAssets() {
    this.loadMapModelAndTexture(this.mapState_800c6798.continent_00.continentNum);

    this.modelAndAnimData_800c66a8.zoomState_1f8 = ZoomState.LOCAL_0;
    this.modelAndAnimData_800c66a8.coolonWarpState_220 = CoolonWarpState.NONE_0;
  }

  @Method(0x800d8e4cL)
  private void loadMapModelAndTexture(final int index) {
    this.filesLoadedFlags_800c66b8.updateAndGet(val -> val & 0xffff_fffd);
    loadDrgnDir(0, 5697 + index, files -> this.timsLoaded(files, 0x2));
    loadDrgnFile(0, 5705 + index, files -> this.loadTmdCallback("Map model DRGN0/" + (5705 + index), files));
  }

  @Method(0x800d8efcL)
  private void initMapAnimation() {
    this.modelAndAnimData_800c66a8.textureAnimation_1c = this.prepareWaterAnimation();
    this.modelAndAnimData_800c66a8.clutYIndex_28 = 0.0f;

    if(this.mapState_800c6798.continent_00 == Continent.TIBEROA_2) {
      //LAB_800d8f94
      for(int i = 0; i < this.modelAndAnimData_800c66a8.tmdRendering_08.count_0c; i++) {
        //LAB_800d8fc4
        this.modelAndAnimData_800c66a8.tmdRendering_08.angles_10[i] = MathHelper.psxDegToRad(rand() % 4095);
      }
    }
    //LAB_800d9030
  }

  @Method(0x800d9044L)
  private void renderWorldMap() {
    final MV lw = new MV();

    this.renderAndHandleWorldMap();
    this.handleCoolonAndQueenFuryPrompts();

    if(
      this.modelAndAnimData_800c66a8.coolonWarpState_220.state >= 2 &&
        this.modelAndAnimData_800c66a8.coolonWarpState_220.state < 8
    ) {
      return;
    }

    //LAB_800d90a8
    if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.WORLD_3) {
      return;
    }

    //LAB_800d90cc
    //LAB_800d9150
    for(int i = 0; i < this.modelAndAnimData_800c66a8.tmdRendering_08.count_0c; i++) {
      final ModelPart10 dobj2 = this.modelAndAnimData_800c66a8.tmdRendering_08.dobj2s_00[i];
      final GsCOORDINATE2 coord2 = this.modelAndAnimData_800c66a8.tmdRendering_08.coord2s_04[i];
      final Vector3f rotation = this.modelAndAnimData_800c66a8.tmdRendering_08.rotations_08[i];

      //LAB_800d9180
      //LAB_800d9210
      this.rotateCoord2(rotation, coord2);

      if(this.mapState_800c6798.continent_00 == Continent.TIBEROA_2) {
        //LAB_800d9264
        if(i >= 2 && i < 9 || i >= 15 && i < 17) {
          //LAB_800d9294
          final float sin = MathHelper.sin(this.modelAndAnimData_800c66a8.tmdRendering_08.angles_10[i]) * 0x20;
          if((i & 0x1) != 0) {
            coord2.coord.transfer.y = sin;
          } else {
            //LAB_800d92d8
            coord2.coord.transfer.y = -sin;
          }

          //LAB_800d9304
          this.modelAndAnimData_800c66a8.tmdRendering_08.angles_10[i] += MathHelper.psxDegToRad(8) / (3.0f / vsyncMode_8007a3b8); // 1/512 of a degree
        }
      }

      //LAB_800d9320
      GsGetLw(dobj2.coord2_04, lw);

      if(i == 0) {
        lw.transfer.add(0.0f, 1.0f, 0.0f);
      }

      // Fix path/Dart's feet being underground (GH#864)
      if(this.mapState_800c6798.continent_00 == Continent.ILLISA_BAY_3 || this.mapState_800c6798.continent_00 == Continent.DEATH_FRONTIER_6) {
        lw.transfer.y += 6.0f;
      }

      final QueuedModelTmd model = RENDERER.queueModel(dobj2.obj, lw, QueuedModelTmd.class);

      if(i == 0) {
        if(this.mapState_800c6798.continent_00.continentNum < 9) {
          model.clutOverride(1008, waterClutYs_800ef348[(int)this.modelAndAnimData_800c66a8.clutYIndex_28]);
        }

        // Push water depth back so that the Queen Fury wake renders on top of it
        model.depthOffset(500.0f);
      }

      //LAB_800d93d4
    }

    //LAB_800d942c
    if(this.mapState_800c6798.continent_00.continentNum < 9) {
      this.animateWater(this.modelAndAnimData_800c66a8.textureAnimation_1c);
    }

    //LAB_800d945c
    this.modelAndAnimData_800c66a8.clutYIndex_28 += 1.0f / (3.0f / vsyncMode_8007a3b8);

    if(this.modelAndAnimData_800c66a8.clutYIndex_28 >= 14.0f) {
      this.modelAndAnimData_800c66a8.clutYIndex_28 = 0.0f;
    }
    //LAB_800d94b8
  }

  @Method(0x800d94ccL)
  private void renderAndHandleWorldMap() {
    if((this.filesLoadedFlags_800c66b8.get() & 0x1) == 0) {
      return;
    }

    //LAB_800d94f8
    if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.LOCAL_0) {
      return;
    }

    //LAB_800d951c
    if(this.modelAndAnimData_800c66a8.fastTravelTransitionMode_250 != FastTravelTransitionMode.NONE_0) {
      return;
    }

    //LAB_800d9540
    if(this.mapState_800c6798.continent_00 == Continent.ENDINESS_7) {
      return;
    }

    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;

    //LAB_800d955c
    switch(this.modelAndAnimData_800c66a8.zoomState_1f8) {
      case CONTINENT_1:
        if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_2)) { // Zoom out
          playSound(0, 4, (short)0, (short)0);
          this.shouldSetDestLabelMetrics = true;

          this.modelAndAnimData_800c66a8.mapPosition_1e8.set(cameraAndLights.coord2_20.coord.transfer);

          this.initMapModelZoom(1);

          this.modelAndAnimData_800c66a8.zoomState_1f8 = ZoomState.TRANSITION_MODEL_OUT_2;
          this.mcqBrightness_800ef1a4 = 0.0f;
        }

        //LAB_800d9674
        //LAB_800d9cc4
        break;

      case TRANSITION_MODEL_OUT_2:
        this.modelAndAnimData_800c66a8.zoomAnimationTick_1f9++;


        this.mcqBrightness_800ef1a4 += 0.125f / (3.0f / vsyncMode_8007a3b8);

        if(this.mcqBrightness_800ef1a4 > 1.0f) {
          this.mcqBrightness_800ef1a4 = 1.0f;
        }

        //LAB_800d96b8
        this.tickMapPositionDuringZoom();

        if(this.modelAndAnimData_800c66a8.zoomAnimationTick_1f9 >= 18.0f / vsyncMode_8007a3b8) {
          cameraAndLights.coord2_20.coord.transfer.set(mapPositions_800ef1a8[this.mapState_800c6798.continent_00.continentNum]);
          this.modelAndAnimData_800c66a8.mapArrow.setSize(8.0f);
          this.modelAndAnimData_800c66a8.zoomState_1f8 = ZoomState.WORLD_3;

          //LAB_800d97bc
          for(int i = 0; i < 7; i++) {
            //LAB_800d97d8
            setTextAndTextboxesToUninitialized(i, 0);
          }
        }

        //LAB_800d9808
        break;

      case WORLD_3:
        if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_2)) { // Can't zoom out more
          playSound(0, 40, (short)0, (short)0);
        }

        //LAB_800d9858
        //LAB_800d985c
        for(int i = 0; i < 6; i++) {
          //LAB_800d9878
          setTextAndTextboxesToUninitialized(i, 0);
        }

        //LAB_800d98a8
        if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_2)) { // Zoom in
          playSound(0, 4, (short)0, (short)0);
          this.initMapModelZoom(-1);

          this.modelAndAnimData_800c66a8.zoomState_1f8 = ZoomState.TRANSITION_MODEL_IN_4;

          //LAB_800d9900
          for(int i = 0; i < 3; i++) {
            //LAB_800d991c
            //LAB_800d996c
            //LAB_800d99c4
            //LAB_800d9a1c
            cameraAndLights.lights_11c[i].r_0c = cameraAndLights.lightsColours_8c[i].x / 4.0f / 0x100;
            cameraAndLights.lights_11c[i].g_0d = cameraAndLights.lightsColours_8c[i].y / 4.0f / 0x100;
            cameraAndLights.lights_11c[i].b_0e = cameraAndLights.lightsColours_8c[i].z / 4.0f / 0x100;

            GsSetFlatLight(i, cameraAndLights.lights_11c[i]);
          }

          //LAB_800d9a70
          if(Input.getButtonState(InputAction.BUTTON_CENTER_2)) {
            //LAB_800d9a8c
            for(int i = 0; i < 8; i++) {
              //LAB_800d9aa8
              this.startButtonLabelStages_800c86d4[i] = 0;
            }
          }
        }

        //LAB_800d9adc
        break;

      case TRANSITION_MODEL_IN_4:
        this.mcqBrightness_800ef1a4 -= 0.125f / (3.0f / vsyncMode_8007a3b8);

        if(this.mcqBrightness_800ef1a4 < 0.0f) {
          this.mcqBrightness_800ef1a4 = 0.0f;
        }

        //LAB_800d9b18
        this.tickMapPositionDuringZoom();

        this.modelAndAnimData_800c66a8.zoomAnimationTick_1f9++;

        if(this.modelAndAnimData_800c66a8.zoomAnimationTick_1f9 >= 18.0f / vsyncMode_8007a3b8) {
          cameraAndLights.coord2_20.coord.transfer.set(this.modelAndAnimData_800c66a8.mapPosition_1e8);
          this.modelAndAnimData_800c66a8.mapArrow.setSize(16.0f);
          this.modelAndAnimData_800c66a8.zoomState_1f8 = ZoomState.CONTINENT_1;
        }

        //LAB_800d9be8
        break;
    }

    //LAB_800d9ccc
    this.mcqTransforms.transfer.set(0.0f, -8.0f, 121);
    RENDERER.queueOrthoModel(this.mcqObj, this.mcqTransforms, QueuedModelStandard.class)
      .monochrome(this.mcqBrightness_800ef1a4);

    //LAB_800d9d10
  }

  /**
   * @param zoomDirection -1 or +1
   */
  @Method(0x800d9d24L)
  private void initMapModelZoom(final int zoomDirection) {
    final Vector3i mapPosition = mapPositions_800ef1a8[this.mapState_800c6798.continent_00.continentNum];
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    modelAndAnimData.mapZoomStep_1f0.x = (mapPosition.x - modelAndAnimData.mapPosition_1e8.x) * zoomDirection / 6.0f / (3.0f / vsyncMode_8007a3b8);
    modelAndAnimData.mapZoomStep_1f0.y = (mapPosition.y - modelAndAnimData.mapPosition_1e8.y) * zoomDirection / 6.0f / (3.0f / vsyncMode_8007a3b8);
    modelAndAnimData.mapZoomStep_1f0.z = (mapPosition.z - modelAndAnimData.mapPosition_1e8.z) * zoomDirection / 6.0f / (3.0f / vsyncMode_8007a3b8);
    modelAndAnimData.zoomAnimationTick_1f9 = 0;
  }

  @Method(0x800d9eb0L)
  private void tickMapPositionDuringZoom() {
    this.wmapCameraAndLights19c0_800c66b0.coord2_20.coord.transfer.add(this.modelAndAnimData_800c66a8.mapZoomStep_1f0);
  }

  private int findPlaceMatch(final int value) {
    for(int j = 0; j < placeIndices_800c84c8.length; j++) {
      if(value == placeIndices_800c84c8[j]) {
        return j;
      }
    }

    throw new RuntimeException("Could not find a place matching index " + value);
  }

  /** Handles Coolon travel, and also renders the square button prompts for Coolon and Queen Fury. */
  @Method(0x800da248L)
  private void handleCoolonAndQueenFuryPrompts() {
    if(this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc == PathSegmentEndpointType.TERMINAL_1) {
      return;
    }

    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;

    //LAB_800da270
    if(modelAndAnimData.fadeAnimationType_05 != FadeAnimationType.NONE_0) {
      return;
    }

    //LAB_800da294
    if(cameraAndLights.mapRotationState_110 != MapRotationState.MAIN_LOOP_0) {
      return;
    }

    //LAB_800da2b8
    if(modelAndAnimData.zoomState_1f8 != ZoomState.LOCAL_0) {
      return;
    }

    //LAB_800da2dc
    if(cameraAndLights.cameraUpdateState_c5 != CameraUpdateState.AWAIT_INPUT_0) {
      return;
    }

    //LAB_800da300
    if(!cameraAndLights.zoomStateIsLocal_c4) {
      return;
    }

    //LAB_800da324
    if((this.filesLoadedFlags_800c66b8.get() & 0x1) == 0) {
      return;
    }

    //LAB_800da344
    if(this.tickMainMenuOpenTransition_800c6690 != 0) {
      return;
    }

    //LAB_800da360
    // Show square prompt while sailing Queen Fury
    if(modelAndAnimData.modelIndex_1e4 == 1) {
      if(gameState_800babc8.scriptFlags2_bc.get(0x97) && this.mapState_800c6798.queenFuryForceMovementMode_d8 == ForcedMovementMode.NONE_0) {
        this.coolonQueenFuryOverlay.render(1);
      }

      //LAB_800da418
      return;
    }

    //LAB_800da420
    if(modelAndAnimData.fastTravelTransitionMode_250 == FastTravelTransitionMode.TELEPORT_1) {
      return;
    }

    //LAB_800da468
    //Flag for Coolon usable
    if(!gameState_800babc8.scriptFlags2_bc.get(0x15a)) {
      return;
    }

    //LAB_800da4ec
    this.coolonQueenFuryOverlay.render(0);

    if(Input.pressedThisFrame(InputAction.BUTTON_WEST)) { // Square
      this.destinationLabelStage_800c86f0 = 0;
      modelAndAnimData.fastTravelTransitionMode_250 = FastTravelTransitionMode.OPEN_COOLON_MAP_2;
    }

    //LAB_800da520
    if(modelAndAnimData.fastTravelTransitionMode_250 != FastTravelTransitionMode.OPEN_COOLON_MAP_2) {
      return;
    }

    //LAB_800da544
    switch(modelAndAnimData.coolonWarpState_220) {
      case NONE_0:
        playSound(0, 4, (short)0, (short)0);

        modelAndAnimData.mapPos_200.set(cameraAndLights.coord2_20.coord.transfer);
        modelAndAnimData.playerPos_208.set(modelAndAnimData.currPlayerPos_94);
        modelAndAnimData.playerRotation_21c = modelAndAnimData.playerRotation_a4.y;
        modelAndAnimData.mapRotation_21e = cameraAndLights.currMapRotation_70.y;
        modelAndAnimData.coolonPromptIndex_223 = 0;
        modelAndAnimData.coolonWarpState_220 = CoolonWarpState.ASCENT_1;
        modelAndAnimData.models_0c[2].coord2_14.transforms.rotate.set(0.0f, modelAndAnimData.playerRotation_a4.y, 0.0f);
        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x = 0.25f;
        modelAndAnimData.coord2_34.coord.transfer.set(modelAndAnimData.currPlayerPos_94);
        modelAndAnimData.models_0c[2].coord2_14.coord.transfer.set(modelAndAnimData.coord2_34.coord.transfer);

        //LAB_800da8a0
        for(int i = 0; i < 8; i++) {
          //LAB_800da8bc
          setTextAndTextboxesToUninitialized(i, 0);
        }

        //LAB_800da8ec
        //LAB_800da8f0
        for(int i = 0; i < 7; i++) {
          //LAB_800da90c
          this.startButtonLabelStages_800c86d4[i] = 0;
        }

        //LAB_800da940
        if(((int)(tickCount_800bb0fc / (3.0f / vsyncMode_8007a3b8)) & 0x3) == 0) {
          playSound(12, 1, (short)0, (short)0);
        }

        //LAB_800da978
        break;

      case ASCENT_1:
        this.renderFastTravelScreenDistortionEffect();

        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x += 0.015625f / (3.0f / vsyncMode_8007a3b8); // 1/64

        if(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x > 0.375f) { // 24/64
          modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x = 0.375f;
        }

        //LAB_800da9fc
        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.set(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x);
        modelAndAnimData.currPlayerPos_94.y -= 96.0f / (3.0f / vsyncMode_8007a3b8);

        cameraAndLights.coord2_20.coord.transfer.y -= 96.0f / (3.0f / vsyncMode_8007a3b8);

        if(cameraAndLights.coord2_20.coord.transfer.y < -1500) {
          cameraAndLights.coord2_20.coord.transfer.y = -1500;
        }

        //LAB_800daab8
        if(modelAndAnimData.currPlayerPos_94.y < -2500.0f) {
          modelAndAnimData.currPlayerPos_94.y = -2500.0f;
        }

        //LAB_800daaf0
        if(modelAndAnimData.currPlayerPos_94.y <= -2500.0f) {
          if(cameraAndLights.coord2_20.coord.transfer.y <= -1500) {
            modelAndAnimData.coolonWarpState_220 = CoolonWarpState.INIT_WORLD_MAP_2;
          }
        }

        //LAB_800dab44
        this.mcqBrightness_800ef1a4 += 0.0078125f / (3.0f / vsyncMode_8007a3b8);

        if(this.mcqBrightness_800ef1a4 > 0.25f) {
          this.mcqBrightness_800ef1a4 = 0.25f;
        }

        //LAB_800dab80
        break;

      case INIT_WORLD_MAP_2:
        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.zero();
        modelAndAnimData.models_0c[2].coord2_14.transforms.rotate.set(MathHelper.PI / 2.0f, MathHelper.PI, 0.0f);

        cameraAndLights.currMapRotation_70.y = 0.0f;
        cameraAndLights.coord2_20.coord.transfer.set(720, -1500, 628);
        cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.INIT_VIEW_FAR_3;

        //LAB_800dac80
        final List<Integer> possibleOriginIndices = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
          //LAB_800dac9c
          if(locations_800f0e34[coolonWarpDest_800ef228[i].locationIndex_10].continent_0e == this.mapState_800c6798.continent_00) {
            possibleOriginIndices.add(i);
          }
          //LAB_800dad14
        }

        //LAB_800dad2c
        if(possibleOriginIndices.size() == 1) {
          modelAndAnimData.coolonOriginIndex_221 = possibleOriginIndices.get(0);
        } else if(possibleOriginIndices.size() > 1) {
          int posIndex = this.findPlaceMatch(coolonWarpDest_800ef228[possibleOriginIndices.get(0)].locationIndex_10);
          Vector3f placePos = placePositionVectors_800c74b8[posIndex];
          float diffX = placePos.x - modelAndAnimData.currPlayerPos_94.x;
          float diffZ = placePos.z - modelAndAnimData.currPlayerPos_94.z;
          float minDistance = Math.sqrt(diffX * diffX + diffZ * diffZ);
          int index = possibleOriginIndices.get(0);
          modelAndAnimData.coolonOriginIndex_221 = index;

          float distance;
          for(int i = 1; i < possibleOriginIndices.size(); i++) {
            index = possibleOriginIndices.get(i);
            posIndex = this.findPlaceMatch(coolonWarpDest_800ef228[possibleOriginIndices.get(i)].locationIndex_10);

            placePos = placePositionVectors_800c74b8[posIndex];
            diffX = placePos.x - modelAndAnimData.currPlayerPos_94.x;
            diffZ = placePos.z - modelAndAnimData.currPlayerPos_94.z;
            distance = Math.sqrt(diffX * diffX + diffZ * diffZ);
            if(distance < minDistance) {
              modelAndAnimData.coolonOriginIndex_221 = index;
              minDistance = distance;
            }
          }
        } else {
          modelAndAnimData.coolonOriginIndex_221 = 8;
        }

        //LAB_800dad4c
        //LAB_800dadac
        modelAndAnimData.coolonDestIndex_222 = coolonWarpDest_800ef228[modelAndAnimData.coolonOriginIndex_221].defaultDestLocationIndex_14;
        modelAndAnimData.coolonWarpState_220 = CoolonWarpState.MAIN_LOOP_3;
        modelAndAnimData.currPlayerPos_94.set(coolonWarpDest_800ef228[modelAndAnimData.coolonOriginIndex_221].destPosition_00);
        this.shouldSetCoolonWarpDestLabelMetrics = true;
        break;

      case MAIN_LOOP_3:
        if(Input.pressedThisFrame(InputAction.BUTTON_EAST) || Input.pressedThisFrame(InputAction.BUTTON_WEST)) {
          this.shouldSetCoolonWarpDestLabelMetrics = false;
          this.coolonWarpDestLabelName = null;

          playSound(0, 3, (short)0, (short)0);

          //LAB_800daef8
          for(int i = 0; i < 8; i++) {
            //LAB_800daf14
            setTextAndTextboxesToUninitialized(i, 0);
          }

          //LAB_800daf44
          // Closing Coolon map when traveling out of Zenebatos
          if(modelAndAnimData.usingCoolonFromZenebatos_254) {
            this.mapState_800c6798.submapCutTo_c8 = locations_800f0e34[this.mapState_800c6798.locationIndex_10].submapCutTo_08;
            this.mapState_800c6798.submapSceneTo_ca = locations_800f0e34[this.mapState_800c6798.locationIndex_10].submapSceneTo_0a;
            submapCut_80052c30 = this.mapState_800c6798.submapCutTo_c8;
            submapScene_80052c34 = this.mapState_800c6798.submapSceneTo_ca;

            this.initTransitionAnimation(FadeAnimationType.FADE_OUT_2);
          } else {
            //LAB_800daff4
            modelAndAnimData.coolonWarpState_220 = CoolonWarpState.INIT_DESCENT_10;
          }

          //LAB_800db004
          break;
        }

        //LAB_800db00c
        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          playSound(0, 2, (short)0, (short)0);
          initTextbox(6, true, 240, 64, 9, 4);
          modelAndAnimData.coolonWarpState_220 = CoolonWarpState.INIT_PROMPT_4;
        }

        //LAB_800db07c
        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x += 0.125f / (3.0f / vsyncMode_8007a3b8);

        if(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x > 0.5f) {
          modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x = 0.5f;
        }

        //LAB_800db0f0
        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.set(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x);

        this.renderCoolonMapSymbols(true, false);
        break;

      case INIT_PROMPT_4:
        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.set(0.5f, 0.5f, 0.5f);

        if(isTextboxInState6(6)) {
          modelAndAnimData.coolonWarpState_220 = CoolonWarpState.PROMPT_LOOP_5;
          modelAndAnimData.coolonPromptIndex_223 = 0;
          this.coolonPromptPopup.getSelector().y_3a = modelAndAnimData.coolonPromptIndex_223 * 0x10; // Needed because of overlayTick
          modelAndAnimData.coolonTravelAnimationTick_218 = 0;
        }

        //LAB_800db1d8
        this.renderCoolonMapSymbols(false, true);
        break;

      case PROMPT_LOOP_5:
        textboxes_800be358[6].z_0c = 18;

        this.renderCoolonMapSymbols(false, true);

        if(Input.pressedThisFrame(InputAction.BUTTON_EAST)) {
          playSound(0, 3, (short)0, (short)0);
          setTextAndTextboxesToUninitialized(6, 1);
          modelAndAnimData.coolonWarpState_220 = CoolonWarpState.MAIN_LOOP_3;
        }

        //LAB_800db39c
        if(Input.pressedThisFrame(InputAction.DPAD_UP) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_UP) ||
          Input.pressedThisFrame(InputAction.DPAD_DOWN) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
          playSound(0, 1, (short)0, (short)0);
          modelAndAnimData.coolonPromptIndex_223 ^= 1;
        }

        //LAB_800db3f8
        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          if(modelAndAnimData.coolonPromptIndex_223 == 0) {
            playSound(0, 3, (short)0, (short)0);
            setTextAndTextboxesToUninitialized(6, 1);
            modelAndAnimData.coolonWarpState_220 = CoolonWarpState.MAIN_LOOP_3;
          } else {
            //LAB_800db474
            playSound(0, 2, (short)0, (short)0);
            setTextAndTextboxesToUninitialized(6, 1);
            modelAndAnimData.coolonWarpState_220 = CoolonWarpState.FLY_ANIM_6;
          }
        }

        //LAB_800db4b4
        this.coolonPromptPopup.getSelector().y_3a = modelAndAnimData.coolonPromptIndex_223 * 0x10;

        break;

      case FLY_ANIM_6:
        modelAndAnimData.coolonTravelAnimationTick_218++;

        if(modelAndAnimData.coolonTravelAnimationTick_218 > 36.0f / vsyncMode_8007a3b8) {
          modelAndAnimData.coolonTravelAnimationTick_218 = (int)(36.0f / vsyncMode_8007a3b8);
          modelAndAnimData.coolonWarpState_220 = CoolonWarpState.INIT_DEST_7;
        }

        //LAB_800db698
        this.lerp(modelAndAnimData.currPlayerPos_94, coolonWarpDest_800ef228[modelAndAnimData.coolonOriginIndex_221].destPosition_00, coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222].destPosition_00, modelAndAnimData.coolonTravelAnimationTick_218 / (36.0f / vsyncMode_8007a3b8));

        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x -= 0.041503906f / (3.0f / vsyncMode_8007a3b8); // ~1/24

        if(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x < 0.0f) {
          modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x = 0.0f;
        }

        //LAB_800db74c
        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.set(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x);

        this.renderCoolonMapSymbols(false, true);
        break;

      case INIT_DEST_7:
        stopSound(soundFiles_800bcf80[12], 1, 1);

        if(modelAndAnimData.coolonDestIndex_222 == 8) {
          gameState_800babc8.visitedLocations_17c.set(coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222].locationIndex_10, true);

          //LAB_800db8f4
          this.mapState_800c6798.submapCutTo_c8 = locations_800f0e34[coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222].locationIndex_10].submapCutTo_08;
          this.mapState_800c6798.submapSceneTo_ca = locations_800f0e34[coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222].locationIndex_10].submapSceneTo_0a;
          submapCut_80052c30 = this.mapState_800c6798.submapCutTo_c8;
          submapScene_80052c34 = this.mapState_800c6798.submapSceneTo_ca;
        } else {
          //LAB_800db9bc
          this.mapState_800c6798.submapCutTo_c8 = locations_800f0e34[coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222].locationIndex_10].submapCutFrom_04;
          this.mapState_800c6798.submapSceneTo_ca = locations_800f0e34[coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222].locationIndex_10].submapSceneFrom_06;
          submapCut_80052c30 = this.mapState_800c6798.submapCutTo_c8;
          collidedPrimitiveIndex_80052c38 = this.mapState_800c6798.submapSceneTo_ca;
          modelAndAnimData.fastTravelTransitionMode_250 = FastTravelTransitionMode.COOLON_ARRIVAL_3;
          previousEngineState_8004dd28 = null;
          this.coolonWarpDestLabelName = null;
        }

        //LAB_800dba98

        this.initTransitionAnimation(FadeAnimationType.FADE_OUT_2);
        this.renderCoolonMapSymbols(false, true);
        break;

      case INIT_DESCENT_10:
        cameraAndLights.coord2_20.coord.transfer.set(modelAndAnimData.mapPos_200);
        cameraAndLights.coord2_20.coord.transfer.y = -1500;
        modelAndAnimData.currPlayerPos_94.set(modelAndAnimData.playerPos_208);
        modelAndAnimData.currPlayerPos_94.y = -5000.0f;
        modelAndAnimData.playerRotation_a4.y = modelAndAnimData.playerRotation_21c;
        cameraAndLights.currMapRotation_70.y = modelAndAnimData.mapRotation_21e;
        modelAndAnimData.models_0c[2].coord2_14.transforms.rotate.set(0.0f, modelAndAnimData.playerRotation_a4.y, 0.0f);
        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.set(0.375f, 0.375f, 0.375f);
        modelAndAnimData.coolonWarpState_220 = CoolonWarpState.PAN_MAP_11;

        stopSound(soundFiles_800bcf80[12], 1, 1);

        // Fall through

      case PAN_MAP_11:
        this.renderFastTravelScreenDistortionEffect();

        cameraAndLights.coord2_20.coord.transfer.y += 112.0f / (3.0f / vsyncMode_8007a3b8);

        if(modelAndAnimData.mapPos_200.y < cameraAndLights.coord2_20.coord.transfer.y) {
          cameraAndLights.coord2_20.coord.transfer.y = modelAndAnimData.mapPos_200.y;
        }

        //LAB_800dbd6c
        if(modelAndAnimData.mapPos_200.y <= cameraAndLights.coord2_20.coord.transfer.y) {
          modelAndAnimData.coolonWarpState_220 = CoolonWarpState.DESCENT_12;
          modelAndAnimData.currPlayerPos_94.y = -400.0f;
        }

        //LAB_800dbdb8
        this.mcqBrightness_800ef1a4 -= 0.0078125f / (3.0f / vsyncMode_8007a3b8);

        if(this.mcqBrightness_800ef1a4 < 0.0f) {
          this.mcqBrightness_800ef1a4 = 0.0f;
        }

        //LAB_800dbdec
        break;

      case DESCENT_12:
        modelAndAnimData.currPlayerPos_94.y += 16.0f / (3.0f / vsyncMode_8007a3b8);

        if(modelAndAnimData.playerPos_208.y < modelAndAnimData.currPlayerPos_94.y) {
          modelAndAnimData.currPlayerPos_94.y = modelAndAnimData.playerPos_208.y;
        }

        //LAB_800dbe70
        if(modelAndAnimData.playerPos_208.y <= modelAndAnimData.currPlayerPos_94.y) {
          modelAndAnimData.coolonWarpState_220 = CoolonWarpState.RESTORE_DART_NEG_1;
        }

        //LAB_800dbeb4
        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x -= 0.00390625f / (3.0f / vsyncMode_8007a3b8); // 1/256

        if(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x < 0.25f) { // 64/256
          modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x = 0.25f;
        }

        //LAB_800dbf28
        modelAndAnimData.models_0c[2].coord2_14.transforms.scale.set(modelAndAnimData.models_0c[2].coord2_14.transforms.scale.x);

        this.mcqBrightness_800ef1a4 -= 0.0078125f / (3.0f / vsyncMode_8007a3b8);

        if(this.mcqBrightness_800ef1a4 < 0.0f) {
          this.mcqBrightness_800ef1a4 = 0.0f;
        }

        //LAB_800dbfa0
        break;

      case RESTORE_DART_NEG_1:
        this.mcqBrightness_800ef1a4 = 0.0f;

        cameraAndLights.coord2_20.coord.transfer.set(modelAndAnimData.mapPos_200);

        modelAndAnimData.currPlayerPos_94.set(modelAndAnimData.playerPos_208);
        modelAndAnimData.playerRotation_a4.y = modelAndAnimData.playerRotation_21c;

        cameraAndLights.currMapRotation_70.y = modelAndAnimData.mapRotation_21e;

        modelAndAnimData.fastTravelTransitionMode_250 = FastTravelTransitionMode.NONE_0;
        modelAndAnimData.coolonWarpState_220 = CoolonWarpState.NONE_0;
        return;
    }

    //LAB_800dc114
    this.mcqTransforms.transfer.set(0.0f, -8.0f, 60000.0f);
    RENDERER.queueOrthoModel(this.mcqObj, this.mcqTransforms, QueuedModelStandard.class)
      .monochrome(this.mcqBrightness_800ef1a4);

    //LAB_800dc164
  }

  @Method(0x800dc178L)
  private void renderCoolonMapSymbols(final boolean enableInput, final boolean destSelected) {
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;

    final CoolonWarpDestination20 warp1 = coolonWarpDest_800ef228[modelAndAnimData.coolonOriginIndex_221];
    final CoolonWarpDestination20 warp2 = coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222];

    int x = warp1.x_18 - warp2.x_18;
    int y = warp1.y_1a - warp2.y_1a;

    modelAndAnimData.playerRotation_a4.y = MathHelper.floorMod(MathHelper.atan2(y, x) + MathHelper.PI / 2.0f, MathHelper.TWO_PI);
    modelAndAnimData.models_0c[2].coord2_14.transforms.rotate.y += (modelAndAnimData.playerRotation_a4.y - modelAndAnimData.models_0c[2].coord2_14.transforms.rotate.y) / 8 / (3.0f / vsyncMode_8007a3b8);

    if(enableInput) {
      if(Input.pressedWithRepeatPulse(InputAction.DPAD_RIGHT) || Input.pressedWithRepeatPulse(InputAction.JOYSTICK_LEFT_BUTTON_RIGHT) || Input.pressedWithRepeatPulse(InputAction.DPAD_DOWN) || Input.pressedWithRepeatPulse(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
        playSound(0, 1, (short)0, (short)0);

        if(modelAndAnimData.coolonDestIndex_222 > 0) {
          modelAndAnimData.coolonDestIndex_222--;
        } else {
          modelAndAnimData.coolonDestIndex_222 = 8;
        }
      }

      //LAB_800dc384
      if(Input.pressedWithRepeatPulse(InputAction.DPAD_UP) || Input.pressedWithRepeatPulse(InputAction.JOYSTICK_LEFT_BUTTON_UP) || Input.pressedWithRepeatPulse(InputAction.DPAD_LEFT) || Input.pressedWithRepeatPulse(InputAction.JOYSTICK_LEFT_BUTTON_LEFT)) {
        playSound(0, 1, (short)0, (short)0);

        modelAndAnimData.coolonDestIndex_222++;
        if(modelAndAnimData.coolonDestIndex_222 > 8) {
          modelAndAnimData.coolonDestIndex_222 = 0;
        }
      }
    }

    //LAB_800dc410
    int u = (int)(tickCount_800bb0fc / 5 / (3.0f / vsyncMode_8007a3b8) % 3);
    final float z = (orderingTableSize_1f8003c8 - 4.0f) * 4.0f;

    //LAB_800dc468 - Location markers
    for(int i = 0; i < 9; i++) {
      //LAB_800dc484
      final int left = coolonWarpDest_800ef228[i].x_18;
      final int top = coolonWarpDest_800ef228[i].y_1a;
      modelAndAnimData.coolonPlaceMarker.render(u, 2, GPU.getOffsetX() + left, GPU.getOffsetY() + top, z);
    }

    //LAB_800dc734
    x = coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222].x_18 - 2;
    y = coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222].y_1a - 12;

    // Selection arrow
    u = (int)(tickCount_800bb0fc / (3.0f / vsyncMode_8007a3b8)) & 0x7;
    modelAndAnimData.mapArrow.render(u, 2, GPU.getOffsetX() + x, GPU.getOffsetY() + y, 68.0f);

    if(destSelected) {
      //LAB_800dcbf4
      setTextAndTextboxesToUninitialized(7, 0);
      this.destinationLabelStage_800c86f0 = 0;
    } else {
      x += 167;
      y += 116;

      final IntRef widthRef = new IntRef();
      final IntRef linesRef = new IntRef();
      this.measureText(coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222].placeName_1c, widthRef, linesRef);
      final int width = widthRef.get();
      final int lines = linesRef.get();

      final int destStage = this.destinationLabelStage_800c86f0;
      if(destStage == 0) {
        //LAB_800dc9e4
        initTextbox(7, false, x, y, width - 1, lines - 1);
        this.destinationLabelStage_800c86f0 = 1;

        //LAB_800dca40
        textZ_800bdf00 = 14;
        textboxes_800be358[7].z_0c = 14;
        textboxes_800be358[7].chars_18 = Math.max(width, 4);
        textboxes_800be358[7].lines_1a = lines;
        this.destinationLabelStage_800c86f0 = 2;
      } else if(destStage == 1) {
        textZ_800bdf00 = 14;
        textboxes_800be358[7].z_0c = 14;
        textboxes_800be358[7].chars_18 = Math.max(width, 4);
        textboxes_800be358[7].lines_1a = lines;
        this.destinationLabelStage_800c86f0 = 2;
        //LAB_800dc9d0
      } else if(destStage == 2) {
        //LAB_800dca9c
        textboxes_800be358[7].chars_18 = Math.max(width, 4);
        textboxes_800be358[7].lines_1a = lines;
        textboxes_800be358[7].width_1c = textboxes_800be358[7].chars_18 * 9 / 2;
        textboxes_800be358[7].height_1e = textboxes_800be358[7].lines_1a * 6 + 4;
        textboxes_800be358[7].x_14 = x;
        textboxes_800be358[7].y_16 = y - 4;
      }

      //LAB_800dcb48
      textboxes_800be358[7].z_0c = 19;
      if(this.shouldSetCoolonWarpDestLabelMetrics) {
        this.coolonWarpDestLabelName = coolonWarpDest_800ef228[modelAndAnimData.coolonDestIndex_222].placeName_1c;
        this.coolonWarpDestLabelX = x;
        this.coolonWarpDestLabelY = y - lines * 7 - 3;
      }
    }
    //LAB_800dcc0c
  }

  @Method(0x800dcc20L)
  private void lerp(final Vector3f out, final Vector3f a, final Vector3f b, final float ratio) {
    if(ratio == 0.0f) {
      out.set(a);
    } else if(ratio == 1.0f) {
      out.set(b);
    } else {
      //LAB_800dcca4
      out.x = (b.x - a.x) * ratio + a.x;
      out.y = (b.y - a.y) * ratio + a.y;
      out.z = (b.z - a.z) * ratio + a.z;
    }

    //LAB_800dcddc
  }

  @Method(0x800dcde8L)
  private void deallocateWorldMap() {
    for(int i = 0; i < this.modelAndAnimData_800c66a8.tmdRendering_08.dobj2s_00.length; i++) {
      this.modelAndAnimData_800c66a8.tmdRendering_08.dobj2s_00[i].obj.delete();
      this.modelAndAnimData_800c66a8.tmdRendering_08.dobj2s_00[i].obj = null;
    }
  }

  @Method(0x800dce64L)
  private void rotateCoord2(final Vector3f rotation, final GsCOORDINATE2 coord2) {
    coord2.flg = 0;
    coord2.coord.rotationXYZ(rotation);
  }

  @Method(0x800dfa70L)
  private void loadPlayerAvatarTextureAndModelFiles() {
    this.filesLoadedFlags_800c66b8.updateAndGet(val -> val & 0xffff_fd57);

    loadDrgnDir(0, 5713, files -> this.timsLoaded(files, 0x2a8));

    //LAB_800dfacc
    for(int i = 0; i < 4; i++) {
      //LAB_800dfae8
      this.modelAndAnimData_800c66a8.models_0c[i] = new Model124("Player " + i);
      final int finalI = i;
      loadDrgnDir(0, 5714 + i, files -> this.loadPlayerAvatarModelFiles(files, finalI));
      this.modelAndAnimData_800c66a8.models_0c[i].uvAdjustments_9d = tmdUvAdjustmentMetrics_800eee48[playerAvatarVramSlots_800ef694[i]];
    }

    //LAB_800dfbb4
    this.modelAndAnimData_800c66a8.teleportAnimationState_248 = TeleportAnimationState.INIT_ANIM_0;
  }

  @Method(0x800dfbd8L)
  private void initPlayerModelAndAnimation() {
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    modelAndAnimData.currPlayerPos_94.set(modelAndAnimData.coord2_34.coord.transfer);
    modelAndAnimData.prevPlayerPos_84.set(modelAndAnimData.currPlayerPos_94);

    //LAB_800dfca4
    for(int i = 0; i < 4; i++) {
      final Model124 model = modelAndAnimData.models_0c[i];

      //LAB_800dfcc0
      initModel(model, modelAndAnimData.playerModelTmdFileData_b4[i].extendedTmd_00, modelAndAnimData.playerModelTmdFileData_b4[i].tmdAnim_08[0]);
      loadModelStandardAnimation(model, modelAndAnimData.playerModelTmdFileData_b4[i].tmdAnim_08[0]);

      model.coord2_14.coord.transfer.set(modelAndAnimData.coord2_34.coord.transfer);
      model.coord2_14.transforms.rotate.set(0.0f, modelAndAnimData.playerRotation_a4.y, 0.0f);
      model.coord2_14.transforms.scale.zero();
    }

    //LAB_800dff4c
    modelAndAnimData.prevAnimIndex_ac = 2;
    modelAndAnimData.currAnimIndex_b0 = 2;

    final float shadowAngleDelta = MathHelper.TWO_PI / 8.0f;

    final PolyBuilder shadowBuilder = new PolyBuilder("Dart Shadow")
      .translucency(Translucency.B_MINUS_F);

    //LAB_800dff70
    for(int i = 0; i < 8; i++) {
      final float sin1 = MathHelper.sin(i * shadowAngleDelta);
      final float cos1 = MathHelper.cosFromSin(sin1, i * shadowAngleDelta);
      final float sin2 = MathHelper.sin((i + 1 & 0x7) * shadowAngleDelta);
      final float cos2 = MathHelper.cosFromSin(sin2, (i + 1 & 0x7) * shadowAngleDelta);

      shadowBuilder
        .addVertex(0.0f, 0.0f, 0.0f)
        .monochrome(0.5f)
        .addVertex(cos1 * 32.0f, 0.0f, sin1 * 32.0f)
        .monochrome(0.0f)
        .addVertex(cos2 * 32.0f, 0.0f, sin2 * 32.0f);
    }

    modelAndAnimData.shadowObj = shadowBuilder.build();

    //LAB_800e002c
    modelAndAnimData.modelIndex_1e4 = directionalPathSegmentData_800f2248[this.mapState_800c6798.directionalPathIndex_12].modelIndex_06;
    this.initQueenFuryWake(40 * (4 - vsyncMode_8007a3b8), 1);

    final int modelIndex = modelAndAnimData.modelIndex_1e4;
    final Model124 model = modelAndAnimData.models_0c[modelIndex];
    if(modelIndex == 0) {
      //LAB_800e00c4
      model.coord2_14.transforms.scale.set(0.5f, 0.4f, 0.5f);
    } else if(modelIndex == 1) {
      //LAB_800e0114
      if(this.mapState_800c6798.continent_00 == Continent.ENDINESS_7) {
        model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
      } else {
        model.coord2_14.transforms.scale.set(2.0f, 2.0f, 2.0f);
      }

      //LAB_800e01b8
      //LAB_800e00a4
    } else if(modelIndex == 2) {
      //LAB_800e01c0
      model.coord2_14.transforms.scale.zero();
    } else if(modelIndex == 3) {
      //LAB_800e0210
      model.coord2_14.transforms.scale.zero();
    }

    //LAB_800e0260
  }

  @Method(0x800e0274L)
  private void renderPlayer() {
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;

    if(modelAndAnimData.fastTravelTransitionMode_250 != FastTravelTransitionMode.OPEN_COOLON_MAP_2) {
      modelAndAnimData.modelIndex_1e4 = directionalPathSegmentData_800f2248[this.mapState_800c6798.directionalPathIndex_12].modelIndex_06;
    } else {
      //LAB_800e02d0
      modelAndAnimData.modelIndex_1e4 = 2;
    }

    //LAB_800e02e0
    applyModelRotationAndScale(modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4]);
    animateModel(modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4], 4 / vsyncMode_8007a3b8);

    final int modelIndex = modelAndAnimData.modelIndex_1e4;
    if(modelIndex == 0) {
      //LAB_800e03a0
      GTE.setBackgroundColour(0.78125f, 0.78125f, 0.78125f);

      modelAndAnimData.models_0c[0].coord2_14.transforms.scale.set(0.5f, 0.4f, 0.5f);
    } else if(modelIndex == 1) {
      //LAB_800e0404
      GTE.setBackgroundColour(0.5f, 0.5f, 0.5f);

      if(this.mapState_800c6798.continent_00 == Continent.ENDINESS_7) {
        modelAndAnimData.models_0c[1].coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
      } else {
        modelAndAnimData.models_0c[1].coord2_14.transforms.scale.set(2.0f, 2.0f, 2.0f);
      }

      //LAB_800e04bc
      //LAB_800e0380
    } else if(modelIndex == 2) {
      //LAB_800e04c4
      GTE.setBackgroundColour(0.5f, 0.5f, 0.5f);
    } else if(modelIndex == 3) {
      //LAB_800e04e0
      GTE.setBackgroundColour(0.5f, 0.5f, 0.5f);
    }

    //LAB_800e04fc
    this.renderWmapModel(modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4]);
    GTE.setBackgroundColour(this.wmapCameraAndLights19c0_800c66b0.ambientLight_14c.x, this.wmapCameraAndLights19c0_800c66b0.ambientLight_14c.y, this.wmapCameraAndLights19c0_800c66b0.ambientLight_14c.z);
    this.handlePlayerMovement();
    this.updatePlayerModelPosition();
  }

  @Method(0x800e05c4L)
  private void unloadWmapPlayerModels() {
    //LAB_800e05d8
    for(int i = 0; i < 4; i++) {
      //LAB_800e05f4
      this.modelAndAnimData_800c66a8.models_0c[i].deleteModelParts();
      this.modelAndAnimData_800c66a8.models_0c[i] = null;
    }

    this.modelAndAnimData_800c66a8.shadowObj.delete();
    this.modelAndAnimData_800c66a8.shadowObj = null;
  }

  /** Handles player movement for both travel on paths and teleportation sphere. */
  @Method(0x800e06d0L)
  private void handlePlayerMovement() {
    this.modelAndAnimData_800c66a8.prevPlayerPos_84.set(this.modelAndAnimData_800c66a8.currPlayerPos_94);

    if(this.modelAndAnimData_800c66a8.fastTravelTransitionMode_250 == FastTravelTransitionMode.NONE_0) {
      //LAB_800e0760
      this.handlePlayerMovementOnPath();
    } else if(this.modelAndAnimData_800c66a8.fastTravelTransitionMode_250 == FastTravelTransitionMode.TELEPORT_1) {
      //LAB_800e0770
      //LAB_800e0774
      int targetLocationIndex = 0;
      for(int i = 0; i < 6; i++) {
        //LAB_800e0790
        if(this.mapState_800c6798.locationIndex_10 == teleportationEndpointIndices_800ef698[i][0]) {
          targetLocationIndex = teleportationEndpointIndices_800ef698[i][1];
          break;
        }
      }

      //LAB_800e0810
      final Vector3f originTranslation = new Vector3f();
      final Vector3f targetTranslation = new Vector3f();
      this.getTeleportationLocationTranslation(this.mapState_800c6798.locationIndex_10, originTranslation);
      this.getTeleportationLocationTranslation(targetLocationIndex, targetTranslation);

      //LAB_800e0878
      final float scale;
      switch(this.modelAndAnimData_800c66a8.teleportAnimationState_248) {
        case INIT_ANIM_0:
          //LAB_800e0898
          this.modelAndAnimData_800c66a8.teleportAnimationTick_24c = 0;
          this.modelAndAnimData_800c66a8.teleportAnimationState_248 = TeleportAnimationState.RENDER_ANIM_1;

        case RENDER_ANIM_1:
          //LAB_800e08b8
          this.renderFastTravelScreenDistortionEffect();

          this.arcLerp(this.modelAndAnimData_800c66a8.currPlayerPos_94, originTranslation, targetTranslation, this.modelAndAnimData_800c66a8.teleportAnimationTick_24c / (96.0f / vsyncMode_8007a3b8));

          this.modelAndAnimData_800c66a8.teleportAnimationTick_24c++;
          if(this.modelAndAnimData_800c66a8.teleportAnimationTick_24c > 96.0f / vsyncMode_8007a3b8) {
            this.modelAndAnimData_800c66a8.teleportAnimationState_248 = TeleportAnimationState.INIT_FADE_2;
          }

          //LAB_800e0980
          scale = (this.modelAndAnimData_800c66a8.teleportAnimationTick_24c * 0.015625f) / (3.0f / vsyncMode_8007a3b8) + MathHelper.sin(this.modelAndAnimData_800c66a8.teleportAnimationTick_24c * (MathHelper.PI / 4.0f / (3.0f / vsyncMode_8007a3b8))) / 16.0f;
          this.modelAndAnimData_800c66a8.models_0c[3].coord2_14.transforms.scale.set(scale, scale, scale);
          this.modelAndAnimData_800c66a8.models_0c[this.modelAndAnimData_800c66a8.modelIndex_1e4].coord2_14.transforms.rotate.y = this.wmapCameraAndLights19c0_800c66b0.currMapRotation_70.y;
          this.modelAndAnimData_800c66a8.playerRotation_a4.y = this.wmapCameraAndLights19c0_800c66b0.currMapRotation_70.y;
          break;

        case INIT_FADE_2:
          //LAB_800e0a6c
          gameState_800babc8.visitedLocations_17c.set(targetLocationIndex, true);

          //LAB_800e0b64
          this.mapState_800c6798.submapCutTo_c8 = locations_800f0e34[targetLocationIndex].submapCutTo_08;
          this.mapState_800c6798.submapSceneTo_ca = locations_800f0e34[targetLocationIndex].submapSceneTo_0a;
          submapCut_80052c30 = this.mapState_800c6798.submapCutTo_c8;
          submapScene_80052c34 = this.mapState_800c6798.submapSceneTo_ca;

          this.initTransitionAnimation(FadeAnimationType.FADE_OUT_2);
          this.modelAndAnimData_800c66a8.teleportAnimationState_248 = TeleportAnimationState.FADE_OUT_3;
          break;

        case FADE_OUT_3:
          //LAB_800e0c00
          this.modelAndAnimData_800c66a8.models_0c[3].coord2_14.transforms.scale.x -= 0.25f / (3.0f / vsyncMode_8007a3b8);

          if(this.modelAndAnimData_800c66a8.models_0c[3].coord2_14.transforms.scale.x < 0.0f) {
            this.modelAndAnimData_800c66a8.models_0c[3].coord2_14.transforms.scale.x = 0.0f;
          }

          //LAB_800e0c70
          scale = this.modelAndAnimData_800c66a8.models_0c[3].coord2_14.transforms.scale.x;
          this.modelAndAnimData_800c66a8.models_0c[3].coord2_14.transforms.scale.y = scale;
          this.modelAndAnimData_800c66a8.models_0c[3].coord2_14.transforms.scale.z = scale;
          break;
      }
      //LAB_800e0cbc
    }

    //LAB_800e0cc4
    this.modelAndAnimData_800c66a8.playerRotation_a4.x = MathHelper.floorMod(this.modelAndAnimData_800c66a8.playerRotation_a4.x, MathHelper.TWO_PI);
    this.modelAndAnimData_800c66a8.playerRotation_a4.y = MathHelper.floorMod(this.modelAndAnimData_800c66a8.playerRotation_a4.y, MathHelper.TWO_PI);
    this.modelAndAnimData_800c66a8.playerRotation_a4.z = MathHelper.floorMod(this.modelAndAnimData_800c66a8.playerRotation_a4.z, MathHelper.TWO_PI);

    this.updateEncounterAndMovementAnimation();
  }

  @Method(0x800e0d70L)
  private void getTeleportationLocationTranslation(final int locationIndex, final Vector3f translation) {
    //LAB_800e0d84
    for(int i = 0; i < 6; i++) {
      final TeleportationLocation0c location = teleportationLocations_800ef6c8[i];

      //LAB_800e0da0
      if(locationIndex == location.locationIndex_00) {
        translation.set(location.translation_04);
        break;
      }
    }
    //LAB_800e0e3c
  }

  /** lerp, but it arcs in the y direction. Used for teleportation movement. */
  @Method(0x800e0e4cL)
  private void arcLerp(final Vector3f currPlayerPos, final Vector3f originPos, final Vector3f targetPos, final float ratio) {
    if(ratio == 0.0f) {
      currPlayerPos.set(originPos);
    } else if(ratio == 1.0f) {
      currPlayerPos.set(targetPos);
    } else {
      //LAB_800e0ed8
      currPlayerPos.x = originPos.x + (targetPos.x - originPos.x) * ratio;
      currPlayerPos.y = originPos.y + (targetPos.y - originPos.y) * ratio + MathHelper.sin(MathHelper.PI * ratio) * -200;
      currPlayerPos.z = originPos.z + (targetPos.z - originPos.z) * ratio;
    }
    //LAB_800e108c
  }

  @Method(0x800e10a0L)
  private void updateEncounterAndMovementAnimation() {
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;

    final int modelIndex = modelAndAnimData.modelIndex_1e4;
    modelAndAnimData.prevAnimIndex_ac = modelAndAnimData.currAnimIndex_b0;

    if(
      !flEq(modelAndAnimData.prevPlayerPos_84.x, modelAndAnimData.currPlayerPos_94.x) ||
        !flEq(modelAndAnimData.prevPlayerPos_84.y, modelAndAnimData.currPlayerPos_94.y) ||
        !flEq(modelAndAnimData.prevPlayerPos_84.z, modelAndAnimData.currPlayerPos_94.z)
    ) {
      final EncounterRateMode mode = CONFIG.getConfig(CoreMod.ENCOUNTER_RATE_CONFIG.get());

      //LAB_800e117c
      //LAB_800e11b0
      if(Input.getButtonState(InputAction.BUTTON_EAST) || analogMagnitude_800beeb4 >= 0x7f) { // World Map Running
        //LAB_800e11d0
        modelAndAnimData.currAnimIndex_b0 = 4;
        this.handleEncounters(mode.worldMapRunModifier);
      } else {
        //LAB_800e11f4
        modelAndAnimData.currAnimIndex_b0 = 3;
        this.handleEncounters(mode.worldMapWalkModifier);
      }

      //LAB_800e1210
      if(modelIndex == 1) {
        if(tickCount_800bb0fc % (4 * this.tickMultiplier()) == 0) {
          playSound(0xc, 0, (short)0, (short)0);
        }
      }
    } else {
      modelAndAnimData.currAnimIndex_b0 = 2;
    }

    //LAB_800e1264
    if(modelIndex != 0) {
      //LAB_800e1298
      modelAndAnimData.currAnimIndex_b0 = 2;
    }

    //LAB_800e12b0
    if(modelAndAnimData.prevAnimIndex_ac != modelAndAnimData.currAnimIndex_b0) {
      loadModelStandardAnimation(modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4], modelAndAnimData.playerModelTmdFileData_b4[modelAndAnimData.modelIndex_1e4].tmdAnim_08[modelAndAnimData.currAnimIndex_b0 - 2]);
    }
    //LAB_800e1354
  }

  @Method(0x800e1364L)
  private void updatePlayerModelPosition() {
    this.renderPlayerShadow();

    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    modelAndAnimData.coord2_34.coord.transfer.set(modelAndAnimData.currPlayerPos_94);
    modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4].coord2_14.coord.transfer.set(modelAndAnimData.coord2_34.coord.transfer);

    if(modelAndAnimData.fastTravelTransitionMode_250 == FastTravelTransitionMode.NONE_0) {
      final float cwAngle = modelAndAnimData.playerRotation_a4.y - modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4].coord2_14.transforms.rotate.y;
      final float ccwAngle = modelAndAnimData.playerRotation_a4.y - (modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4].coord2_14.transforms.rotate.y - MathHelper.TWO_PI);
      final float finalAngle;
      if(Math.abs(ccwAngle) < Math.abs(cwAngle)) {
        finalAngle = ccwAngle;
      } else {
        finalAngle = cwAngle;
      }

      //LAB_800e15e4
      modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4].coord2_14.transforms.rotate.y += finalAngle / 2.0f / (3.0f / vsyncMode_8007a3b8);
      modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4].coord2_14.transforms.rotate.x = modelAndAnimData.playerRotation_a4.x;
      modelAndAnimData.models_0c[modelAndAnimData.modelIndex_1e4].coord2_14.transforms.rotate.z = modelAndAnimData.playerRotation_a4.z;
    }

    //LAB_800e16f8
    this.rotateCoord2(modelAndAnimData.playerRotation_a4, modelAndAnimData.coord2_34);
  }

  @Method(0x800e1740L)
  private void renderDartShadow() {
    GsGetLw(this.modelAndAnimData_800c66a8.models_0c[this.modelAndAnimData_800c66a8.modelIndex_1e4].coord2_14, this.modelAndAnimData_800c66a8.shadowTransforms);
    RENDERER.queueModel(this.modelAndAnimData_800c66a8.shadowObj, this.modelAndAnimData_800c66a8.shadowTransforms, QueuedModelStandard.class);
  }

  @Method(0x800e1ac4L)
  private void renderQueenFuryWake() {
    final MV transforms = new MV();
    final Vector3f vertex0 = new Vector3f();
    final Vector3f vertex1 = new Vector3f();
    final Vector3f vertex2 = new Vector3f();
    final Vector3f vertex3 = new Vector3f();

    final FloatRef deltaScaleFactor = new FloatRef();
    final FloatRef colourScaleFactor = new FloatRef();

    final Vector3f pos0 = new Vector3f();
    final Vector3f pos1 = new Vector3f();
    final Vector3f spread0 = new Vector3f();
    final Vector3f spread1 = new Vector3f();

    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;

    final Vector3f playerPosDelta = new Vector3f(modelAndAnimData.prevPlayerPos_84).sub(modelAndAnimData.currPlayerPos_94);
    final Vector3f wakeSpread = new Vector3f();
    if(playerPosDelta.x == 0.0f && playerPosDelta.y == 0.0f && playerPosDelta.z == 0.0f) {
      wakeSpread.set(0.0f);
    } else {
      wakeSpread.set(playerPosDelta).normalize().cross(this.shipWakeCrossVector_800c87d8);
    }

    this.updateQueenFuryWakePositionAndSpread(wakeSpread, modelAndAnimData.currPlayerPos_94);
    this.rotateCoord2(modelAndAnimData.tmdRendering_08.rotations_08[0], modelAndAnimData.tmdRendering_08.coord2s_04[0]);
    GsGetLs(modelAndAnimData.tmdRendering_08.coord2s_04[0], transforms);
    GTE.setTransforms(transforms);

    final PolyBuilder builder = new PolyBuilder("Queen Fury wake", GL_TRIANGLES)
      .bpp(Bpp.BITS_4)
      .translucency(Translucency.B_PLUS_F);

    //LAB_800e1ccc
    for(int i = 0; i < modelAndAnimData.wakeSegmentCount - 1; i++) {
      //LAB_800e1ce8
      this.getQueenFuryWakeMetrics(i, spread0, pos0, colourScaleFactor, deltaScaleFactor);
      spread0.mul(deltaScaleFactor.get());
      vertex0.set(pos0).add(spread0);
      vertex1.set(pos0);

      float baseColour = 1.0f - colourScaleFactor.get() / modelAndAnimData.wakeSegmentCount;
      final float r0 = baseColour * 96 / 255.0f;
      final float g0 = baseColour * 96 / 255.0f;
      final float b0 = baseColour * 96 / 255.0f;
      final float r1 = 0;
      final float g1 = baseColour / 8.0f;
      final float b1 = baseColour * 96 / 255.0f;

      this.getQueenFuryWakeMetrics(i + 1, spread1, pos1, colourScaleFactor, deltaScaleFactor);
      spread1.mul(deltaScaleFactor.get());
      vertex2.set(pos1).add(spread1);
      vertex3.set(pos1);

      baseColour = 1.0f - colourScaleFactor.get() / modelAndAnimData.wakeSegmentCount;
      final float r2 = baseColour * 96 / 255.0f;
      final float g2 = baseColour * 96 / 255.0f;
      final float b2 = baseColour * 96 / 255.0f;
      final float r3 = 0;
      final float g3 = baseColour / 8.0f;
      final float b3 = baseColour * 96 / 255.0f;

      final Vector2f sxyz0 = new Vector2f();
      final Vector2f sxyz1 = new Vector2f();
      final Vector2f sxyz2 = new Vector2f();
      final Vector2f sxyz3 = new Vector2f();

      float z = RotTransPers4(vertex0, vertex1, vertex2, vertex3, sxyz0, sxyz1, sxyz2, sxyz3);

      // ship starboard wake
      if(z >= 3 && z < orderingTableSize_1f8003c8) {
        builder
          .addVertex(sxyz0.x, sxyz0.y, z * 4.0f)
          .clut(1008, waterClutYs_800ef348[(int)modelAndAnimData.clutYIndex_28])
          .vramPos(448, 0)
          .uv(0, 0)
          .rgb(r0, g0, b0)
          .addVertex(sxyz1.x, sxyz1.y, z * 4.0f)
          .uv(64, 0)
          .rgb(r1, g1, b1)
          .addVertex(sxyz2.x, sxyz2.y, z * 4.0f)
          .uv(0, 64)
          .rgb(r2, g2, b2)
          .addVertex(sxyz1.x, sxyz1.y, z * 4.0f)
          .uv(64, 0)
          .rgb(r1, g1, b1)
          .addVertex(sxyz2.x, sxyz2.y, z * 4.0f)
          .uv(0, 64)
          .rgb(r2, g2, b2)
          .addVertex(sxyz3.x, sxyz3.y, z * 4.0f)
          .uv(64, 64)
          .rgb(r3, g3, b3);
      }

      //LAB_800e2440
      vertex0.set(pos0).sub(spread0);
      vertex2.set(pos1).sub(spread1);
      z = RotTransPers4(vertex0, vertex1, vertex2, vertex3, sxyz0, sxyz1, sxyz2, sxyz3);

      // ship port wake
      if(z >= 3 && z < orderingTableSize_1f8003c8) {
        builder
          .addVertex(sxyz0.x, sxyz0.y, z * 4.0f)
          .uv(0, 0)
          .rgb(r0, g0, b0)
          .addVertex(sxyz1.x, sxyz1.y, z * 4.0f)
          .uv(64, 0)
          .rgb(r1, g1, b1)
          .addVertex(sxyz2.x, sxyz2.y, z * 4.0f)
          .uv(0, 64)
          .rgb(r2, g2, b2)
          .addVertex(sxyz1.x, sxyz1.y, z * 4.0f)
          .uv(64, 0)
          .rgb(r1, g1, b1)
          .addVertex(sxyz2.x, sxyz2.y, z * 4.0f)
          .uv(0, 64)
          .rgb(r2, g2, b2)
          .addVertex(sxyz3.x, sxyz3.y, z * 4.0f)
          .uv(64, 64)
          .rgb(r3, g3, b3);
      }
    }

    final Obj obj = builder.build();
    obj.delete();

    transforms.identity();
    transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), 400.0f);
    RENDERER.queueOrthoModel(obj, transforms, QueuedModelStandard.class);

    //LAB_800e2770
    //LAB_800e2774
    for(int i = 0; i < modelAndAnimData.wakeSegmentCount; i++) {
      //LAB_800e2790
      int wakeSegmentIndex = modelAndAnimData.currShipPositionIndex_230 - i * modelAndAnimData.wakeSegmentStride_23c;

      if(wakeSegmentIndex < 0) {
        wakeSegmentIndex += modelAndAnimData.shipPositionsCount_238;
      }

      //LAB_800e2808
      modelAndAnimData.wakeSegmentNumArray_22c[wakeSegmentIndex]++;
    }

    //LAB_800e289c
    modelAndAnimData.tickNum_240++;
  }

  @Method(0x800e28dcL)
  private void initQueenFuryWake(final int segmentCount, final int stride) {
    final int count = segmentCount * stride;

    this.modelAndAnimData_800c66a8.wakeSpreadsArray_224 = new Vector3f[count];
    this.modelAndAnimData_800c66a8.shipPositionsArray_228 = new Vector3f[count];

    this.modelAndAnimData_800c66a8.wakeSegmentNumArray_22c = new int[count];
    this.modelAndAnimData_800c66a8.currShipPositionIndex_230 = 0;
    this.modelAndAnimData_800c66a8.prevShipPositionIndex_234 = count - 1;
    this.modelAndAnimData_800c66a8.shipPositionsCount_238 = count;
    this.modelAndAnimData_800c66a8.wakeSegmentCount = segmentCount;
    this.modelAndAnimData_800c66a8.wakeSegmentStride_23c = stride;

    //NOTE: there's a bug in the original code, it just sets the first vector in the array over and over again
    Arrays.setAll(this.modelAndAnimData_800c66a8.wakeSpreadsArray_224, i -> new Vector3f());
    Arrays.setAll(this.modelAndAnimData_800c66a8.shipPositionsArray_228, i -> new Vector3f());

    this.modelAndAnimData_800c66a8.shipPositionsUninitialized_244 = true;
  }

  @Method(0x800e2ae4L)
  private void updateQueenFuryWakePositionAndSpread(final Vector3f wakeSpread, final Vector3f currPlayerPos) {
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;

    if(modelAndAnimData.shipPositionsUninitialized_244) {
      //LAB_800e2b14
      for(int i = 0; i < modelAndAnimData.shipPositionsCount_238; i++) {
        //LAB_800e2b3c
        modelAndAnimData.wakeSpreadsArray_224[i].set(wakeSpread);
        modelAndAnimData.shipPositionsArray_228[i].set(currPlayerPos);
      }

      //LAB_800e2ca4
      modelAndAnimData.shipPositionsUninitialized_244 = false;
      modelAndAnimData.tickNum_240 = 0;
    }

    //LAB_800e2cc4
    modelAndAnimData.wakeSpreadsArray_224[modelAndAnimData.currShipPositionIndex_230].set(wakeSpread);
    modelAndAnimData.shipPositionsArray_228[modelAndAnimData.currShipPositionIndex_230].set(currPlayerPos);

    modelAndAnimData.wakeSegmentNumArray_22c[modelAndAnimData.currShipPositionIndex_230] = 0;

    modelAndAnimData.prevShipPositionIndex_234 = modelAndAnimData.currShipPositionIndex_230;
    modelAndAnimData.currShipPositionIndex_230 = (modelAndAnimData.currShipPositionIndex_230 + 1) % modelAndAnimData.shipPositionsCount_238;
  }

  @Method(0x800e2e1cL)
  private void getQueenFuryWakeMetrics(final int index, final Vector3f spread, final Vector3f position, final FloatRef colourFadeFactor, final FloatRef spreadScaleFactor) {
    final float angle;
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    if(index == 0) {
      spread.set(modelAndAnimData.wakeSpreadsArray_224[modelAndAnimData.prevShipPositionIndex_234]);
      position.set(modelAndAnimData.shipPositionsArray_228[modelAndAnimData.prevShipPositionIndex_234]);
      colourFadeFactor.set(modelAndAnimData.wakeSegmentNumArray_22c[modelAndAnimData.prevShipPositionIndex_234]);
      angle = MathHelper.psxDegToRad((modelAndAnimData.wakeSegmentNumArray_22c[modelAndAnimData.prevShipPositionIndex_234] - modelAndAnimData.tickNum_240)) / (4.0f - vsyncMode_8007a3b8);
      spreadScaleFactor.set((modelAndAnimData.wakeSegmentNumArray_22c[modelAndAnimData.prevShipPositionIndex_234] + (MathHelper.sin(MathHelper.floorMod(angle * 256.0f, MathHelper.PI)) * modelAndAnimData.wakeSegmentNumArray_22c[modelAndAnimData.prevShipPositionIndex_234])) / (4.0f - vsyncMode_8007a3b8));
    } else {
      //LAB_800e3024
      int wakeSegmentIndex = modelAndAnimData.currShipPositionIndex_230 - index * modelAndAnimData.wakeSegmentStride_23c;

      if(wakeSegmentIndex < 0) {
        wakeSegmentIndex += modelAndAnimData.shipPositionsCount_238;
      }

      //LAB_800e3090
      spread.set(modelAndAnimData.wakeSpreadsArray_224[wakeSegmentIndex]);
      position.set(modelAndAnimData.shipPositionsArray_228[wakeSegmentIndex]);
      colourFadeFactor.set(modelAndAnimData.wakeSegmentNumArray_22c[wakeSegmentIndex]);
      angle = MathHelper.psxDegToRad((modelAndAnimData.wakeSegmentNumArray_22c[wakeSegmentIndex] - modelAndAnimData.tickNum_240)) / (4.0f - vsyncMode_8007a3b8);
      spreadScaleFactor.set((modelAndAnimData.wakeSegmentNumArray_22c[wakeSegmentIndex] + (MathHelper.sin(MathHelper.floorMod(angle * 256.0f, MathHelper.PI)) * modelAndAnimData.wakeSegmentNumArray_22c[wakeSegmentIndex])) / (4.0f - vsyncMode_8007a3b8));
    }
    //LAB_800e321c
  }

  @Method(0x800e32a8L)
  private void renderPlayerShadow() {
    this.shadowRenderers_800ef684[this.modelAndAnimData_800c66a8.modelIndex_1e4].run();
  }

  @Method(0x800e32fcL)
  private void renderNoOp() {
    // no-op
  }

  /** Some kind of full-screen effect during the Wingly teleportation between Aglis and Zenebatos */
  @Method(0x800e3304L)
  private void renderFastTravelScreenDistortionEffect() {
    this.fastTravelTransforms.transfer.set(0.0f, 0.0f, 20.0f);
    this.fastTravelTransforms.scaling(320.0f, 240.0f, 1.0f);

    RENDERER.queueOrthoModel(RENDERER.renderBufferQuad, this.fastTravelTransforms, QueuedModelStandard.class)
      .texture(RENDERER.getLastFrame())
      .translucency(Translucency.HALF_B_PLUS_HALF_F);
  }

  @Method(0x800e367cL)
  private void handleEncounters(final float encounterRateMultiplier) {
    if(
      Loader.getLoadingFileCount() != 0 ||
        this.worldMapState_800c6698 != WorldMapState.RENDER_5 ||
        this.playerState_800c669c != PlayerState.RENDER_5 ||
        this.modelAndAnimData_800c66a8.modelIndex_1e4 >= 2
    ) {
      return;
    }

    //LAB_800e3724
    if(this.modelAndAnimData_800c66a8.fadeAnimationType_05 != FadeAnimationType.NONE_0) {
      return;
    }

    //LAB_800e3748
    if(
      this.mapState_800c6798.shortForceMovementMode_d4 != ForcedMovementMode.NONE_0 ||
      this.mapState_800c6798.queenFuryForceMovementMode_d8 != ForcedMovementMode.NONE_0
    ) {
      //LAB_800e3778
      return;
    }

    //LAB_800e3780
    //LAB_800e3794
    final DirectionalPathSegmentData08 directionalPathSegment = directionalPathSegmentData_800f2248[this.mapState_800c6798.directionalPathIndex_12];
    this.encounterAccumulator_800c6ae8 += Math.round(directionalPathSegment.encounterRate_03 * encounterRateMultiplier * 70 / (3.0f / vsyncMode_8007a3b8));

    if(this.encounterAccumulator_800c6ae8 >= 5120) {
      this.encounterAccumulator_800c6ae8 = 0;

      if(directionalPathSegment.battleStage_04 == -1) {
        battleStage_800bb0f4 = 1;
      } else {
        //LAB_800e386c
        battleStage_800bb0f4 = directionalPathSegment.battleStage_04;
      }

      //LAB_800e3894
      final int encounterIndex = directionalPathSegment.encounterIndex_05;

      if(encounterIndex == -1) {
        encounterId_800bb0f8 = 0;
      } else {
        //LAB_800e38dc
        final int rand = simpleRand() % 100;
        if(rand < 35) {
          encounterId_800bb0f8 = encounterIds_800ef364[encounterIndex][0];
          //LAB_800e396c
        } else if(rand < 70) {
          encounterId_800bb0f8 = encounterIds_800ef364[encounterIndex][1];
          //LAB_800e39c0
        } else if(rand < 90) {
          encounterId_800bb0f8 = encounterIds_800ef364[encounterIndex][2];
        } else {
          //LAB_800e3a14
          encounterId_800bb0f8 = encounterIds_800ef364[encounterIndex][3];
        }
      }

      //LAB_800e3a38
      gameState_800babc8.directionalPathIndex_4de = this.mapState_800c6798.directionalPathIndex_12;
      gameState_800babc8.pathIndex_4d8 = this.mapState_800c6798.pathIndex_14;
      gameState_800babc8.dotIndex_4da = this.mapState_800c6798.dotIndex_16;
      gameState_800babc8.dotOffset_4dc = this.mapState_800c6798.dotOffset_18;
      gameState_800babc8.facing_4dd = this.mapState_800c6798.facing_1c;
      this.wmapState_800bb10c = WmapState.TRANSITION_TO_BATTLE_8;
    }
    //LAB_800e3a94
  }

  @Method(0x800e3aa8L)
  private WMapTmdRenderingData18 loadTmd(final TmdWithId tmd) {
    final WMapTmdRenderingData18 renderingData = new WMapTmdRenderingData18();
    renderingData.count_0c = this.allocateTmdRenderer(renderingData, tmd);

    //LAB_800e3b00
    return renderingData;
  }

  @Method(0x800e3bd4L)
  private int allocateTmdRenderer(final WMapTmdRenderingData18 renderingData, final TmdWithId tmd) {
    final int nobj = tmd.tmd.header.nobj;
    renderingData.dobj2s_00 = new ModelPart10[nobj];
    renderingData.coord2s_04 = new GsCOORDINATE2[nobj];
    renderingData.rotations_08 = new Vector3f[nobj];
    renderingData.angles_10 = new float[nobj];

    Arrays.setAll(renderingData.dobj2s_00, i -> new ModelPart10());
    Arrays.setAll(renderingData.coord2s_04, i -> new GsCOORDINATE2());
    Arrays.setAll(renderingData.rotations_08, i -> new Vector3f());

    //LAB_800e3d24
    for(int i = 0; i < nobj; i++) {
      //LAB_800e3d44
      renderingData.dobj2s_00[i].tmd_08 = tmd.tmd.objTable[i];
    }

    //LAB_800e3d80
    //LAB_800e3d94
    return nobj;
  }

  @Method(0x800e3da8L)
  private void initTmdTransforms(final WMapTmdRenderingData18 renderingData, @Nullable final GsCOORDINATE2 superCoord) {
    //LAB_800e3dfc
    for(int i = 0; i < renderingData.count_0c; i++) {
      final ModelPart10 dobj2 = renderingData.dobj2s_00[i];
      final GsCOORDINATE2 coord2 = renderingData.coord2s_04[i];
      final Vector3f rotation = renderingData.rotations_08[i];

      //LAB_800e3e20
      GsInitCoordinate2(superCoord, coord2);

      dobj2.coord2_04 = coord2;
      coord2.coord.transfer.set(0, 0, 0);
      rotation.set(0.0f, 0.0f, 0.0f);
    }
    //LAB_800e3ee8
  }

  @Method(0x800e3efcL)
  private void setAllCoord2Attribs(final WMapTmdRenderingData18 renderingData, final int attribute) {
    //LAB_800e3f24
    for(int i = 0; i < renderingData.count_0c; i++) {
      final ModelPart10 part = renderingData.dobj2s_00[i];

      //LAB_800e3f48
      part.attribute_00 = attribute;
    }
    //LAB_800e3f9c
  }

  @Method(0x800e3facL)
  private void initTransitionAnimation(final FadeAnimationType type) {
    this.modelAndAnimData_800c66a8.fadeAnimationTicks_00 = 0;
    this.modelAndAnimData_800c66a8.fadeState_04 = FadeState.START_FADE_0;
    this.modelAndAnimData_800c66a8.fadeAnimationType_05 = type;
  }

  @Method(0x800e3ff0L)
  private void tickTransitionAnimation() {
    if(this.modelAndAnimData_800c66a8.fadeAnimationType_05 != FadeAnimationType.NONE_0) {
      //LAB_800e4020
      this.fadeTransitionTickers_800f01fc[this.modelAndAnimData_800c66a8.fadeAnimationType_05.typeIndex - 1].run();
    }
    //LAB_800e4058
  }

  @Method(0x800e406cL)
  private void tickFadeInTransition() {
    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    if(modelAndAnimData.fastTravelTransitionMode_250 == FastTravelTransitionMode.TELEPORT_1) {
      //LAB_800e442c
      switch(modelAndAnimData.fadeState_04) {
        case START_FADE_0:
          if(
            this.worldMapState_800c6698.state >= WorldMapState.INIT_MAP_ANIM_3.state ||
              this.playerState_800c669c.state >= PlayerState.INIT_PLAYER_MODEL_3.state
          ) {
            //LAB_800e44b0
            startFadeEffect(2, 15);

            cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.INIT_VIEW_NEAR_1;
            cameraAndLights.coord2_20.coord.transfer.set(0, 0, 0);
            cameraAndLights.originalMapRotation_9a = 0;
            cameraAndLights.currMapRotation_70.y = 0.0f;

            this.initCameraZoomPositionAndRotationSteps(1);

            cameraAndLights.zoomStateIsLocal_c4 = true;
            modelAndAnimData.zoomState_1f8 = ZoomState.LOCAL_0;
            modelAndAnimData.fadeState_04 = FadeState.FADE_1;
          }
          break;

        case FADE_1:
          //LAB_800e4564
          modelAndAnimData.fadeAnimationTicks_00++;

          if(modelAndAnimData.fadeAnimationTicks_00 >= 45.0f / vsyncMode_8007a3b8) {
            modelAndAnimData.fadeState_04 = FadeState.END_FADE_2;
            modelAndAnimData.fadeAnimationTicks_00 = 0;
          }

          //LAB_800e45c0
          //LAB_800e4464
          break;

        case END_FADE_2:
          //LAB_800e45c8
          if(this.playerState_800c669c.state >= PlayerState.INIT_PLAYER_MODEL_3.state) {
            modelAndAnimData.fadeAnimationTicks_00++;

            if(modelAndAnimData.fadeAnimationTicks_00 >= 6.0f / vsyncMode_8007a3b8) {
              this.mapState_800c6798.shortForceMovementMode_d4 = ForcedMovementMode.NONE_0;
            }
          }

          //LAB_800e4624
          if(
            cameraAndLights.cameraUpdateState_c5 == CameraUpdateState.AWAIT_INPUT_0 &&
              this.mapState_800c6798.shortForceMovementMode_d4 == ForcedMovementMode.NONE_0
          ) {
            this.mapState_800c6798.disableInput_d0 = false;
            modelAndAnimData.fadeAnimationType_05 = FadeAnimationType.NONE_0;
          }
          //LAB_800e4478
          break;
      }
    } else if(
      modelAndAnimData.fastTravelTransitionMode_250 == FastTravelTransitionMode.NONE_0 ||
        modelAndAnimData.fastTravelTransitionMode_250 == FastTravelTransitionMode.OPEN_COOLON_MAP_2
    ) {
      switch(modelAndAnimData.fadeState_04) {
        case START_FADE_0:
          if(
            this.worldMapState_800c6698.state >= WorldMapState.INIT_MAP_ANIM_3.state ||
              this.playerState_800c669c.state >= PlayerState.INIT_PLAYER_MODEL_3.state) {
            //LAB_800e4144
            startFadeEffect(2, 15);

            cameraAndLights.currRview2_00.viewpoint_00.y = -9000.0f;
            cameraAndLights.currRview2_00.refpoint_0c.y = 9000.0f;
            cameraAndLights.projectionDistanceState_11a = ProjectionDistanceState.INIT_VIEW_NEAR_1;
            cameraAndLights.coord2_20.coord.transfer.set(0, 0, 0);
            cameraAndLights.finalCameraY_9e = -300;
            cameraAndLights.originalMapRotation_9a = 0;
            cameraAndLights.currMapRotation_70.y = 0.0f;

            this.initCameraZoomPositionAndRotationSteps(1);

            cameraAndLights.cameraZoomPosStep_a4.set(modelAndAnimData.coord2_34.coord.transfer).div(30.0f);

            cameraAndLights.zoomStateIsLocal_c4 = true;
            modelAndAnimData.zoomState_1f8 = ZoomState.LOCAL_0;
            cameraAndLights.cameraUpdateState_c5 = CameraUpdateState.ZOOM_IN_2;
            modelAndAnimData.fadeState_04 = FadeState.FADE_1;
          }
          break;

        case FADE_1:
          //LAB_800e4304
          modelAndAnimData.fadeAnimationTicks_00++;

          if(modelAndAnimData.fadeAnimationTicks_00 >= 45.0f / vsyncMode_8007a3b8) {
            modelAndAnimData.fadeState_04 = FadeState.END_FADE_2;
            modelAndAnimData.fadeAnimationTicks_00 = 0;
          }

          //LAB_800e4360
          break;

        case END_FADE_2:
          //LAB_800e4368
          if(this.playerState_800c669c.state >= PlayerState.INIT_PLAYER_MODEL_3.state) {
            modelAndAnimData.fadeAnimationTicks_00++;

            if(modelAndAnimData.fadeAnimationTicks_00 >= 6.0f / vsyncMode_8007a3b8) {
              this.mapState_800c6798.shortForceMovementMode_d4 = ForcedMovementMode.NONE_0;
            }
          }

          //LAB_800e43c4
          if(
            cameraAndLights.cameraUpdateState_c5 == CameraUpdateState.AWAIT_INPUT_0 &&
              this.mapState_800c6798.shortForceMovementMode_d4 == ForcedMovementMode.NONE_0
          ) {
            this.mapState_800c6798.disableInput_d0 = false;
            modelAndAnimData.fadeAnimationType_05 = FadeAnimationType.NONE_0;
          }
          //LAB_800e441c
          //LAB_800e4424
          //LAB_800e410c
          //LAB_800e42fc
          break;

      }
    }
  }

  @Method(0x800e469cL)
  private void tickFadeOutTransition() {
    final WMapCameraAndLights19c0 cameraAndLights = this.wmapCameraAndLights19c0_800c66b0;
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    switch(modelAndAnimData.fadeState_04) {
      case START_FADE_0:
        //LAB_800e46f0
        startFadeEffect(1, 30);
        cameraAndLights.mapRotationState_110 = MapRotationState.INIT_SUBMAP_ZOOM_1;
        modelAndAnimData.fadeState_04 = FadeState.FADE_1;
        break;

      case FADE_1:
        //LAB_800e4738
        cameraAndLights.mapRotationState_110 = MapRotationState.SUBMAP_ZOOM_2;
        this.mcqColour_800c6794 -= 0.125f / (3.0f / vsyncMode_8007a3b8);

        if(this.mcqColour_800c6794 < 0.0f) {
          this.mcqColour_800c6794 = 0.0f;
        }

        //LAB_800e477c
        modelAndAnimData.fadeAnimationTicks_00++;
        if(modelAndAnimData.fadeAnimationTicks_00 >= 90.0f / vsyncMode_8007a3b8) {
          modelAndAnimData.fadeState_04 = FadeState.END_FADE_2;
        }

        //LAB_800e47c8
        //LAB_800e46dc
        break;

      case END_FADE_2:
        //LAB_800e47d0
        modelAndAnimData.mapTextureBrightness_20 -= 0.50f / (3.0f / vsyncMode_8007a3b8);

        if(modelAndAnimData.mapTextureBrightness_20 < 0.0f) {
          modelAndAnimData.mapTextureBrightness_20 = 0.0f;
        }

        if(modelAndAnimData.mapTextureBrightness_20 == 0.0f) {
          modelAndAnimData.fadeAnimationType_05 = FadeAnimationType.NONE_0;

          if(submapCut_80052c30 != 999) {
            this.wmapState_800bb10c = WmapState.TRANSITION_TO_SUBMAP_7;
          } else {
            //LAB_800e48b8
            this.wmapState_800bb10c = WmapState.TRANSITION_TO_WORLD_MAP_9;
          }

          //LAB_800e48c4
          if(modelAndAnimData.fastTravelTransitionMode_250 == FastTravelTransitionMode.OPEN_COOLON_MAP_2) {
            this.wmapState_800bb10c = WmapState.TRANSITION_TO_SUBMAP_7;
            //LAB_800e48f4
          } else if(modelAndAnimData.fastTravelTransitionMode_250 == FastTravelTransitionMode.COOLON_ARRIVAL_3) {
            this.wmapState_800bb10c = WmapState.TRANSITION_TO_WORLD_MAP_9;
          }
        }
        //LAB_800e491c
        break;
    }
    //LAB_800e4924
  }

  @Method(0x800e4e1cL)
  private void loadMapMcq() {
    this.filesLoadedFlags_800c66b8.updateAndGet(val -> val & 0xffff_fffe);
    loadDrgnFile(0, 5696, this::loadMapMcqToVram);
    this.mcqColour_800c6794 = 0.0f;
  }

  @Method(0x800e4e84L)
  private void renderMapBackground() {
    if((this.filesLoadedFlags_800c66b8.get() & 0x1) == 0) {
      return;
    }

    //LAB_800e4eac
    if(this.modelAndAnimData_800c66a8.fadeAnimationType_05 != FadeAnimationType.FADE_OUT_2) {
      this.mcqColour_800c6794 += 0.125f / (3.0f / vsyncMode_8007a3b8);

      if(this.mcqColour_800c6794 > 0.25f) {
        this.mcqColour_800c6794 = 0.25f;
      }
    }

    //LAB_800e4f04
    this.mcqTransforms.transfer.set(0.0f, -8.0f, 60000.0f);
    RENDERER.queueOrthoModel(this.mcqObj, this.mcqTransforms, QueuedModelStandard.class)
      .monochrome(this.mcqColour_800c6794);

    //LAB_800e4f50
  }

  @Method(0x800e5150L)
  private void handleMapTransitions() {
    if(Loader.getLoadingFileCount() != 0 || this.tickMainMenuOpenTransition_800c6690 != 0) {
      return;
    }

    //LAB_800e5178
    //LAB_800e5194
    if(this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc != PathSegmentEndpointType.TERMINAL_1) {
      this.handleStartButtonLocationLabels();
      return;
    }

    //LAB_800e51b8
    //LAB_800e51dc
    //LAB_800e5200
    //LAB_800e5224
    if(
      this.wmapCameraAndLights19c0_800c66b0.cameraUpdateState_c5 != CameraUpdateState.AWAIT_INPUT_0 ||
        !this.wmapCameraAndLights19c0_800c66b0.zoomStateIsLocal_c4 ||
        this.modelAndAnimData_800c66a8.zoomState_1f8 != ZoomState.LOCAL_0 ||
        this.modelAndAnimData_800c66a8.coolonWarpState_220 != CoolonWarpState.NONE_0
    ) {
      return;
    }

    //LAB_800e5248
    final int placeIndex = locations_800f0e34[this.mapState_800c6798.locationIndex_10].placeIndex_02;
    switch(this.mapTransitionState_800c68a4) {
      case INIT_0:
        final int pathIndexAndReverseDirection = -directionalPathSegmentData_800f2248[this.mapState_800c6798.tempPathSegmentIndices_dc[0]].pathSegmentIndexAndDirection_00;

        //LAB_800e52cc
        int pathIndex;
        for(pathIndex = 0; pathIndex < this.mapState_800c6798.directionalPathCount_0c && directionalPathSegmentData_800f2248[pathIndex].pathSegmentIndexAndDirection_00 != pathIndexAndReverseDirection; pathIndex++) {
          // intentionally empty
        }

        //LAB_800e533c
        this.identifyNewPathSegmentAndSetStateInfo(pathIndex);

        this.mapState_800c6798.facing_1c = -this.mapState_800c6798.facing_1c;

        this.initDataForReversePath(this.mapState_800c6798.locationIndex_10);

        this.mapState_800c6798.disableInput_d0 = true;
        this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc = PathSegmentEndpointType.TERMINAL_1;

        //LAB_800e5394
        for(int i = 0; i < 8; i++) {
          //LAB_800e53b0
          setTextAndTextboxesToUninitialized(i, 0);
        }

        //LAB_800e53e0
        textZ_800bdf00 = 13;
        this.mapState_800c6798.submapCutTo_c8 = locations_800f0e34[this.mapState_800c6798.locationIndex_10].submapCutTo_08;
        this.mapState_800c6798.submapSceneTo_ca = locations_800f0e34[this.mapState_800c6798.locationIndex_10].submapSceneTo_0a;
        this.mapTransitionState_800c68a4 = MapTransitionState.LOAD_FILES_1;

        if(places_800f0234[locations_800f0e34[this.mapState_800c6798.locationIndex_10].placeIndex_02].name_00 == null) {
          this.mapTransitionState_800c68a4 = MapTransitionState.END_MOVEMENT_8;
        }

        //LAB_800e54c4
        break;

      case LOAD_FILES_1:
        this.filesLoadedFlags_800c66b8.updateAndGet(val -> val & 0xffff_f7ff);

        loadDrgnFileSync(0, 5655 + places_800f0234[locations_800f0e34[this.mapState_800c6798.locationIndex_10].placeIndex_02].fileIndex_04, data -> this.loadLocationThumbnailImage(new Tim(data)));
        initTextbox(6, true, 240, 120, 14, 16);

        this.mapTransitionState_800c68a4 = MapTransitionState.BUILD_PROMPT_2;

        playSound(0, 4, (short)0, (short)0);

        //LAB_800e55f0
        for(int i = 0; i < 4; i++) {
          //LAB_800e560c
          final int soundIndex = places_800f0234[locations_800f0e34[this.mapState_800c6798.locationIndex_10].placeIndex_02].soundIndices_06[i];

          if(soundIndex > 0) {
            playSound(0xc, soundIndex, (short)0, (short)0);
          }

          //LAB_800e5698
        }

        //LAB_800e56b0
        break;

      case BUILD_PROMPT_2:
        if(isTextboxInState6(6) && (this.filesLoadedFlags_800c66b8.get() & 0x800) != 0) {
          initTextbox(7, false, 240, 71, 13, 7);
          this.mapTransitionState_800c68a4 = MapTransitionState.MAIN_LOOP_3;

          // Build Objs
          this.wmapLocationPromptPopup = new WmapPromptPopup(Objects.requireNonNull(places_800f0234[placeIndex].name_00), textZ_800bdf00 * 4.0f)
            .addOptionText("Don't enter");

          if(this.mapState_800c6798.submapCutTo_c8 == 999) { // Going to a different region
            final String dest1 = regions_800f01ec[this.mapState_800c6798.submapSceneTo_ca >>> 4 & 0xffff];
            final String dest2 = regions_800f01ec[this.mapState_800c6798.submapSceneTo_ca & 0xf];

            this.wmapLocationPromptPopup
              .addOptionText(dest1)
              .addOptionText(dest2);
            this.wmapLocationPromptPopup.setOptionSpacing(18.0f);
            this.wmapLocationPromptPopup.setTranslation(WmapPromptPopup.ObjFields.OPTIONS, 240.0f, 164.0f, textZ_800bdf00 * 4.0f - 2.0f);
          } else {
            this.wmapLocationPromptPopup.addOptionText("Enter");
          }

          final int servicesFlag = places_800f0234[placeIndex].servicesFlag_05;
          int servicesCount = 0;
          for(int i = 0; i < 5; i++) {
            if((servicesFlag & 0x1 << i) != 0) {
              this.wmapLocationPromptPopup.addAltText(services_800f01cc[i]);
              servicesCount++;
            }
          }

          if(servicesCount == 0) {
            this.wmapLocationPromptPopup.addAltText("No facilities");
            this.wmapLocationPromptPopup.setTranslation(WmapPromptPopup.ObjFields.ALT_TEXT, 240.0f, 63.0f, textZ_800bdf00 * 4.0f - 2.0f);
          }

          this.wmapLocationPromptPopup.setHighlight(
            WmapPromptPopup.HighlightMode.SHADOW,
            new WmapMenuTextHighlight40(
              0.0f,
              new Vector3f(0.5f),
              new Rect4i(176, 120, 128, 40),
              8,
              8,
              4,
              true,
              Translucency.B_MINUS_F,
              55.0f
            )
          );

          this.wmapLocationPromptPopup.setHighlight(
            WmapPromptPopup.HighlightMode.SELECTOR,
            new WmapMenuTextHighlight40(
              0.5f,
              new Vector3f(1.0f, 0.0f, 0.0f),
              new Rect4i(176, 150, 128, 24),
              1,
              2,
              2,
              true,
              Translucency.B_PLUS_F,
              51.0f
            )
          );
        }

        //LAB_800e5700
        break;

      case MAIN_LOOP_3:
        this.wmapLocationPromptPopup.getShadow().currentBrightness_34 += 0.25f / (3.0f / vsyncMode_8007a3b8);

        if(Input.pressedThisFrame(InputAction.DPAD_UP) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
          this.wmapLocationPromptPopup.decrMenuSelectorOptionIndex();

          //LAB_800e5950
          playSound(0, 1, (short)0, (short)0);
        }

        //LAB_800e5970
        if(Input.pressedThisFrame(InputAction.DPAD_DOWN) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
          this.wmapLocationPromptPopup.incrMenuSelectorOptionIndex();

          //LAB_800e59c0
          playSound(0, 1, (short)0, (short)0);
        }

        //LAB_800e5b38
        final float optionOffset = this.mapState_800c6798.submapCutTo_c8 == 999 ? 8.0f : 14.0f;
        this.wmapLocationPromptPopup.getSelector().y_3a = this.wmapLocationPromptPopup.getMenuSelectorOptionIndex() * this.wmapLocationPromptPopup.getOptionSpacing() + optionOffset;


        //LAB_800e5b68
        float newBrightness;
        final float currentBrightness = this.wmapLocationPromptPopup.getThumbnailBrightness();
        if(
          gameState_800babc8.visitedLocations_17c.get(this.mapState_800c6798.locationIndex_10) ||
            locations_800f0e34[this.mapState_800c6798.locationIndex_10].thumbnailShouldUseFullBrightness_10
        ) {
          //LAB_800e5e98
          newBrightness = currentBrightness * 0.5f;
        } else {
          //LAB_800e5e18
          newBrightness = currentBrightness * 0.1875f;
        }

        //LAB_800e5f04
        this.wmapLocationPromptPopup.setImage(
          768,
          508,
          448,
          256,
          GPU.getOffsetX() + 20,
          GPU.getOffsetY() - 95,
          120,
          90,
          0,
          0,
          newBrightness
        );

        if(Input.pressedThisFrame(InputAction.BUTTON_WEST) && this.mapState_800c6798.submapCutTo_c8 != 999) { // Square
          playSound(0, 2, (short)0, (short)0);
        }

        //LAB_800e60d0
        if(Input.getButtonState(InputAction.BUTTON_WEST) && this.mapState_800c6798.submapCutTo_c8 != 999) { // Square
          newBrightness = currentBrightness - 0.5f / (3.0f / vsyncMode_8007a3b8);

          if(newBrightness < 0.5f) {
            newBrightness = 0.25f;
          }

          //LAB_800e6138
          //LAB_800e619c
          this.wmapLocationPromptPopup.setShowAltText(true);

          //LAB_800e6290
        } else {
          //LAB_800e6298
          this.wmapLocationPromptPopup.setShowAltText(false);
          newBrightness = currentBrightness + 0.25f / (3.0f / vsyncMode_8007a3b8);

          if(newBrightness > 1.0f) {
            newBrightness = 1.0f;
          }
        }

        this.wmapLocationPromptPopup.setThumbnailBrightness(newBrightness);
        this.wmapLocationPromptPopup.render();

        //LAB_800e62d4
        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          if(this.wmapLocationPromptPopup.getMenuSelectorOptionIndex() == 0) {
            setTextAndTextboxesToUninitialized(6, 1);
            setTextAndTextboxesToUninitialized(7, 0);
            this.mapTransitionState_800c68a4 = MapTransitionState.INIT_MOVEMENT_6;

            playSound(0, 3, (short)0, (short)0);

            //LAB_800e6350
            for(int i = 0; i < 4; i++) {
              //LAB_800e636c
              final int soundIndex = places_800f0234[locations_800f0e34[this.mapState_800c6798.locationIndex_10].placeIndex_02].soundIndices_06[i];

              if(soundIndex > 0) {
                stopSound(soundFiles_800bcf80[12], soundIndex, 1);
              }

              //LAB_800e63ec
            }

            //LAB_800e6404
          } else {
            //LAB_800e640c
            this.initTransitionAnimation(FadeAnimationType.FADE_OUT_2);
            setTextAndTextboxesToUninitialized(6, 1);
            setTextAndTextboxesToUninitialized(7, 0);
            this.mapTransitionState_800c68a4 = MapTransitionState.ANIMATE_PROMPT_OUT_5;

            playSound(0, 2, (short)0, (short)0);

            //LAB_800e6468
            for(int i = 0; i < 4; i++) {
              //LAB_800e6484
              final int soundIndex = places_800f0234[locations_800f0e34[this.mapState_800c6798.locationIndex_10].placeIndex_02].soundIndices_06[i];

              if(soundIndex > 0) {
                stopSound(soundFiles_800bcf80[12], soundIndex, 1);
              }
              //LAB_800e6504
            }
          }

          //LAB_800e651c
        } else {
          //LAB_800e6524
          if(Input.pressedThisFrame(InputAction.BUTTON_EAST)) {
            playSound(0, 3, (short)0, (short)0);

            //LAB_800e6560
            for(int i = 0; i < 4; i++) {
              //LAB_800e657c
              final int soundIndex = places_800f0234[locations_800f0e34[this.mapState_800c6798.locationIndex_10].placeIndex_02].soundIndices_06[i];

              if(soundIndex > 0) {
                stopSound(soundFiles_800bcf80[12], soundIndex, 1);
              }
              //LAB_800e65fc
            }

            //LAB_800e6614
            setTextAndTextboxesToUninitialized(6, 1);
            setTextAndTextboxesToUninitialized(7, 0);
            this.mapTransitionState_800c68a4 = MapTransitionState.INIT_MOVEMENT_6;
          }
        }

        //LAB_800e6640
        break;

      case ANIMATE_PROMPT_OUT_5:
        this.wmapLocationPromptPopup.getShadow().currentBrightness_34 -= 0.5f / (3.0f / vsyncMode_8007a3b8);
        this.wmapLocationPromptPopup.renderHighlight(WmapPromptPopup.HighlightMode.SHADOW);

        if(textboxes_800be358[6].state_00 == TextboxState.UNINITIALIZED_0 && textboxes_800be358[7].state_00 == TextboxState.UNINITIALIZED_0 && flEq(this.wmapLocationPromptPopup.getShadow().currentBrightness_34, 0.0f)) {
          this.mapTransitionState_800c68a4 = MapTransitionState.SET_DEST_9;
        }

        //LAB_800e66cc
        break;

      // Backing out of location entrance prompt, set up for forced movement
      case INIT_MOVEMENT_6:
        if(!flEq(this.mapState_800c6798.playerDestAngle_c0, 0.0f)) {
          this.mapState_800c6798.playerDestAngle_c0 = 0.0f;
          this.mapState_800c6798.facing_1c = 1;
        } else {
          //LAB_800e6704
          this.mapState_800c6798.playerDestAngle_c0 = MathHelper.PI;
          this.mapState_800c6798.facing_1c = -1;
        }

        //LAB_800e671c
        this.mapState_800c6798.shortForceMovementMode_d4 = ForcedMovementMode.WALK_1;
        this.cancelLocationEntryDelayTick_800c68a0 = 0;
        this.mapTransitionState_800c68a4 = MapTransitionState.WAIT_7;

      case WAIT_7:
        this.cancelLocationEntryDelayTick_800c68a0++;

        if(this.cancelLocationEntryDelayTick_800c68a0 >= 9.0f / vsyncMode_8007a3b8) {
          this.mapTransitionState_800c68a4 = MapTransitionState.END_MOVEMENT_8;
        }

        //LAB_800e6770
        break;

      case END_MOVEMENT_8:
        if(this.wmapLocationPromptPopup != null) {
          this.wmapLocationPromptPopup.deallocate();
        }
        this.mapTransitionState_800c68a4 = MapTransitionState.INIT_0;
        this.mapState_800c6798.disableInput_d0 = false;
        this.mapState_800c6798.shortForceMovementMode_d4 = ForcedMovementMode.NONE_0;
        this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc = PathSegmentEndpointType.NOT_AT_ENDPOINT_0;
        this.startLocationLabelsActive_800c68a8 = true;

        //LAB_800e67a8
        for(int i = 0; i < 7; i++) {
          //LAB_800e67c4
          this.startButtonLabelStages_800c86d4[i] = 0;
        }

        //LAB_800e67f8
        break;

      case SET_DEST_9:
        gameState_800babc8.visitedLocations_17c.set(this.mapState_800c6798.locationIndex_10, true);

        //LAB_800e6900
        if(this.mapState_800c6798.submapCutTo_c8 != 999) {
          submapCut_80052c30 = this.mapState_800c6798.submapCutTo_c8;
          submapScene_80052c34 = this.mapState_800c6798.submapSceneTo_ca;
        } else {
          //LAB_800e693c
          submapCut_80052c30 = locations_800f0e34[this.mapState_800c6798.locationIndex_10].submapCutFrom_04;

          final int continentIndex;
          if(this.wmapLocationPromptPopup.getMenuSelectorOptionIndex() == 1) {
            continentIndex = this.mapState_800c6798.submapSceneTo_ca >>> 4 & 0xffff;
          } else {
            //LAB_800e69a0
            continentIndex = this.mapState_800c6798.submapSceneTo_ca & 0xf;
          }

          //LAB_800e69b8
          collidedPrimitiveIndex_80052c38 = continentIndex;
        }

        //LAB_800e69c4
        this.mapState_800c6798.disableInput_d0 = false;
        this.wmapLocationPromptPopup.deallocate();
        break;
    }
    //LAB_800e69d4
  }

  @Method(0x800e69e8L)
  private void handleStartButtonLocationLabels() {
    if(this.tickMainMenuOpenTransition_800c6690 != 0) {
      return;
    }

    //LAB_800e6a10
    if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.WORLD_3) {
      return;
    }

    //LAB_800e6a34
    if(this.wmapState_800bb10c == WmapState.TRANSITION_TO_BATTLE_8) {
      return;
    }

    //LAB_800e6a50
    // World Map Name Info
    if(this.startLocationLabelsActive_800c68a8) {
      Arrays.fill(this.startLabelNames, null);

      //LAB_800e6b04
      if(!Input.getButtonState(InputAction.BUTTON_CENTER_2)) {
        //LAB_800e6b20
        for(int i = 0; i < 7; i++) {
          //LAB_800e6b3c
          setTextAndTextboxesToUninitialized(i, 0);
        }

        //LAB_800e6b6c
        this.startLocationLabelsActive_800c68a8 = false;
      }

      //LAB_800e6b74
      if(Input.getButtonState(InputAction.BUTTON_NORTH)) {
        //LAB_800e6b90
        for(int i = 0; i < 7; i++) {
          //LAB_800e6bac
          setTextAndTextboxesToUninitialized(i, 0);
        }

        //LAB_800e6bdc
        this.startLocationLabelsActive_800c68a8 = false;
      }
      //LAB_800e6afc
    } else {
      if(Input.pressedThisFrame(InputAction.BUTTON_CENTER_2)) {
        playSound(0, 2, (short)0, (short)0);
        this.startLocationLabelsActive_800c68a8 = true;

        //LAB_800e6aac
        for(int i = 0; i < 7; i++) {
          //LAB_800e6ac8
          this.startButtonLabelStages_800c86d4[i] = 0;
        }
      }
    }

    //LAB_800e6be4
    if(!this.startLocationLabelsActive_800c68a8) {
      return;
    }

    //LAB_800e6c00
    this.rotateCoord2(this.modelAndAnimData_800c66a8.tmdRendering_08.rotations_08[0], this.modelAndAnimData_800c66a8.tmdRendering_08.coord2s_04[0]);

    final List<WmapLocationLabelMetrics0c> labelList = new ArrayList<>();

    //LAB_800e6c38
    final MV transforms = new MV();
    for(int i = 0; i < this.placeCount_800c86cc; i++) {
      //LAB_800e6c5c
      if(places_800f0234[locations_800f0e34[placeIndices_800c84c8[i]].placeIndex_02].name_00 != null) {
        //LAB_800e6ccc
        GsGetLs(this.modelAndAnimData_800c66a8.tmdRendering_08.coord2s_04[0], transforms);
        GTE.setTransforms(transforms);

        GTE.perspectiveTransform(placePositionVectors_800c74b8[i]);
        final float sx = GTE.getScreenX(2);
        final float sy = GTE.getScreenY(2);
        final float z = GTE.getScreenZ(3) / 4.0f;
        final float x = sx + 160;
        final float y = sy + 104;

        //LAB_800e6e24
        if(x >= -32 && x < 353) {
          //LAB_800e6e2c
          //LAB_800e6e5c
          if(y >= -32 && y < 273) {
            //LAB_800e6e64
            if(z >= 6 && z < orderingTableSize_1f8003c8 - 1) {
              final WmapLocationLabelMetrics0c label = new WmapLocationLabelMetrics0c();
              label.z_00 = z;
              label.locationIndex_04 = placeIndices_800c84c8[i];
              label.xy_08.set(sx, sy);
              labelList.add(label);
            }
          }
        }
      }
    }

    // Render world map place names when start is held down

    //LAB_800e6f54
    labelList.sort(Comparator.comparingDouble(o -> o.z_00));

    //LAB_800e6fa0
    int i;
    for(i = 0; i < Math.min(7, labelList.size()); i++) {
      final WmapLocationLabelMetrics0c label = labelList.get(i);

      //LAB_800e6fec
      //LAB_800e6fec
      //LAB_800e6ff4
      final float x = label.xy_08.x + 160;
      final float y = label.xy_08.y + 104;
      final int place = locations_800f0e34[label.locationIndex_04].placeIndex_02;

      final String placeName = places_800f0234[place].name_00;
      if(placeName != null) {
        //LAB_800e70f4
        final IntRef width = new IntRef();
        final IntRef lines = new IntRef();
        this.measureText(placeName, width, lines);

        // labelStage == 2 uses code common to all conditions
        final int labelStage = this.startButtonLabelStages_800c86d4[i];
        if(labelStage == 0) {
          //LAB_800e7168
          initTextbox(i, false, x, y, width.get() - 1, lines.get() - 1);

          //LAB_800e71d8
          textboxes_800be358[i].z_0c = i + 14;
          this.startButtonLabelStages_800c86d4[i] = 1;
        } else if(labelStage == 1) {
          //LAB_800e71d8
          textboxes_800be358[i].z_0c = i + 14;
          this.startButtonLabelStages_800c86d4[i] = 2;
        }

        //LAB_800e72e8
        textboxes_800be358[i].chars_18 = Math.max(width.get(), 4);
        textboxes_800be358[i].lines_1a = lines.get();
        textboxes_800be358[i].width_1c = textboxes_800be358[i].chars_18 * 9 / 2;
        textboxes_800be358[i].height_1e = textboxes_800be358[i].lines_1a * 6 + 4;
        textboxes_800be358[i].x_14 = x;
        textboxes_800be358[i].y_16 = y - 2;

        //LAB_800e74d8
        textboxes_800be358[i].z_0c = i + 120;

        if(this.startLocationLabelsActive_800c68a8) {
          this.startLabelNames[i] = placeName;
          this.startLabelXs[i] = x;
          this.startLabelYs[i] = y - lines.get() * 7 - 1;
        }
      }
      //LAB_800e7590
    }

    //LAB_800e75a8
    for(; i < 7; i++) {
      //LAB_800e75c4
      setTextAndTextboxesToUninitialized(i, 0);
      this.startButtonLabelStages_800c86d4[i] = 0;
    }
    //LAB_800e7610
  }

  /** Completely replaced retail with modern String version. */
  @Method(0x800e7624L)
  public void measureText(final String text, final IntRef widthRef, final IntRef linesRef) {
    int longestLineWidth = 0;
    final String[] lines = text.split("\n");
    for(final String line : lines) {
      longestLineWidth = Math.max(longestLineWidth, line.length());
    }

    widthRef.set(longestLineWidth);
    linesRef.set(lines.length);
  }

  @Method(0x800e78c0L)
  private void initFlagsPathsCutsAndPlaces() {
    //LAB_800e7940
    //LAB_800e7944
    // Set all destination flags in the gameState
    for(int i = 0; i < 49; i++) {
      //LAB_800e7984
      if(gameState_800babc8.scriptFlags2_bc.get(wmapDestinationMarkers_800f5a6c[i].packedFlag_00)) {
        //LAB_800e7a38
        //LAB_800e7a3c
        for(int flagIndex = 0; flagIndex < 8; flagIndex++) {
          //LAB_800e7a58
          gameState_800babc8.wmapFlags_15c.setRaw(flagIndex, wmapDestinationMarkers_800f5a6c[i].flags_04[flagIndex]);
        }
      }
      //LAB_800e7acc
    }

    //LAB_800e7ae4
    this.mapState_800c6798.submapCutFrom_c4 = submapCut_80052c30;
    this.mapState_800c6798.submapSceneFrom_c6 = collidedPrimitiveIndex_80052c38;

    // If Debug Room Floor 1
    if(this.mapState_800c6798.submapCutFrom_c4 == 0 && this.mapState_800c6798.submapSceneFrom_c6 == 0) {
      this.mapState_800c6798.submapCutFrom_c4 = 13; // Hellena
      this.mapState_800c6798.submapSceneFrom_c6 = 17;
    }

    //LAB_800e7b44
    //LAB_800e7b54
    boolean locationExists = false;
    int locationIndex;
    for(locationIndex = 0; locationIndex < 0x100; locationIndex++) {
      //LAB_800e7b70
      if(
        locations_800f0e34[locationIndex].submapCutFrom_04 == this.mapState_800c6798.submapCutFrom_c4 &&
          locations_800f0e34[locationIndex].submapSceneFrom_06 == this.mapState_800c6798.submapSceneFrom_c6
      ) {
        locationExists = true;
        break;
      }
      //LAB_800e7bc0
    }

    //LAB_800e7be8
    //LAB_800e7c18
    if(!locationExists || !gameState_800babc8.wmapFlags_15c.get(locationIndex)) {
      this.mapState_800c6798.submapCutFrom_c4 = 13; // Hellena
      this.mapState_800c6798.submapSceneFrom_c6 = 17;
      locationIndex = 5;
    }

    this.mapState_800c6798.pathDots = new PathDots();

    //LAB_800e7cb8
    //LAB_800e7cbc
    //LAB_800e7d0c
    this.mapState_800c6798.locationCount_08 = locations_800f0e34.length;

    //LAB_800e7d1c
    int directionalPathSegmentIndex;
    for(directionalPathSegmentIndex = 0; directionalPathSegmentData_800f2248[directionalPathSegmentIndex].pathSegmentIndexAndDirection_00 != 0; directionalPathSegmentIndex++) {
      // intentionally empty
    }

    //LAB_800e7d64
    this.mapState_800c6798.directionalPathCount_0c = directionalPathSegmentIndex;

    GsInitCoordinate2(null, this.modelAndAnimData_800c66a8.coord2_34);

    this.mapState_800c6798.continent_00 = locations_800f0e34[locationIndex].continent_0e;
    continentIndex_800bf0b0 = this.mapState_800c6798.continent_00.continentNum;

    this.setNewPathSegmentStateInfo(locationIndex);

    this.mapState_800c6798.queenFuryForceMovementMode_d8 = ForcedMovementMode.NONE_0;

    boolean transitionFromCombatOrShip = previousEngineState_8004dd28 == EngineStateEnum.COMBAT_06 && this.mapState_800c6798.submapCutFrom_c4 != 999;

    //LAB_800e7e2c
    if(this.mapState_800c6798.submapSceneFrom_c6 == 31 && this.mapState_800c6798.submapCutFrom_c4 == 279) { // Exiting ship
      transitionFromCombatOrShip = true;
    }

    //LAB_800e7e5c
    //LAB_800e7e88
    if(!transitionFromCombatOrShip && !loadingNewGameState_800bdc34 || this.reinitializingWmap_80052c6c) {
      //LAB_800e844c
      // Transition from submap or other world map
      this.mapState_800c6798.shortForceMovementMode_d4 = ForcedMovementMode.WALK_1;
      this.mapState_800c6798.disableInput_d0 = true;
    } else {
      // Transition from title or combat to world map (or sailing Queen Fury)
      this.mapState_800c6798.directionalPathIndex_12 = gameState_800babc8.directionalPathIndex_4de;
      this.mapState_800c6798.pathIndex_14 = gameState_800babc8.pathIndex_4d8;
      this.mapState_800c6798.dotIndex_16 = gameState_800babc8.dotIndex_4da;
      this.mapState_800c6798.dotOffset_18 = gameState_800babc8.dotOffset_4dc;
      this.mapState_800c6798.facing_1c = gameState_800babc8.facing_4dd;
      this.mapState_800c6798.shortForceMovementMode_d4 = ForcedMovementMode.NONE_0;
      this.mapState_800c6798.disableInput_d0 = false;

      //LAB_800e7f00
      for(int i = 0; i < this.mapState_800c6798.locationCount_08; i++) {
        //LAB_800e7f24
        final int directionalPathIndex = locations_800f0e34[i].directionalPathIndex_00;
          //LAB_800e7f68
        if(this.checkLocationIsValidAndOptionallySetPathStart(i, -1, null) == 0) {
          //LAB_800e7f88
          if(directionalPathIndex == this.mapState_800c6798.directionalPathIndex_12) {
            locationIndex = i;
            break;
          }
        }
        //LAB_800e7fb4
      }

      //LAB_800e7fcc
      this.mapState_800c6798.locationIndex_10 = locationIndex;
      this.mapState_800c6798.continent_00 = locations_800f0e34[locationIndex].continent_0e;
      continentIndex_800bf0b0 = this.mapState_800c6798.continent_00.continentNum;

      final DirectionalPathSegmentData08 directionalPathSegment = directionalPathSegmentData_800f2248[this.mapState_800c6798.directionalPathIndex_12];

      //LAB_800e8064
      //LAB_800e8068
      final Vector3f[] dots = pathDotPosArr_800f591c[this.mapState_800c6798.pathIndex_14];

      final float dx;
      final float dz;
      if(directionalPathSegment.pathSegmentIndexAndDirection_00 >= 0) {
        this.modelAndAnimData_800c66a8.coord2_34.coord.transfer.set(dots[0].x, dots[0].y - 2, dots[0].z);

        dx = dots[0].x - dots[1].x;
        dz = dots[0].z - dots[1].z;

        this.mapState_800c6798.playerDestAngle_c0 = 0.0f;
      } else {
        //LAB_800e8190
        final int index = pathSegmentLengths_800f5810[Math.abs(directionalPathSegment.pathSegmentIndexAndDirection_00) - 1] - 1;
        dx = dots[index].x - dots[index - 1].x;
        dz = dots[index].z - dots[index - 1].z;

        this.modelAndAnimData_800c66a8.coord2_34.coord.transfer.set(dots[index].x, dots[index].y - 2, dots[index].z);

        this.mapState_800c6798.playerDestAngle_c0 = MathHelper.PI;
      }

      //LAB_800e838c
      this.modelAndAnimData_800c66a8.playerRotation_a4.set(0.0f, MathHelper.atan2(dx, dz), 0.0f);
      this.mapState_800c6798.previousPlayerRotation_c2 = this.modelAndAnimData_800c66a8.playerRotation_a4.y;
      this.modelAndAnimData_800c66a8.playerRotation_a4.y += this.mapState_800c6798.playerDestAngle_c0;

      this.mapState_800c6798.pathSegmentPlayerMovingInto_f8 = PathSegmentEntering.CURRENT_0;
      this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc = PathSegmentEndpointType.NOT_AT_ENDPOINT_0;
      loadingNewGameState_800bdc34 = false;
    }

    //LAB_800e8464
    if(previousEngineState_8004dd28 == EngineStateEnum.COMBAT_06 && this.mapState_800c6798.submapCutFrom_c4 == 999) {
      submapCut_80052c30 = 0;
    }

    //LAB_800e8494
    this.mapState_800c6798.submapCutTo_c8 = locations_800f0e34[locationIndex].submapCutTo_08;
    this.mapState_800c6798.submapSceneTo_ca = locations_800f0e34[locationIndex].submapSceneTo_0a;

    final Vector3f avg = new Vector3f();
    final Vector3f prevDotPos = new Vector3f();
    final Vector3f nextDotPos = new Vector3f();

    this.getPathPositions(prevDotPos, nextDotPos);
    this.weightedAvg(4.0f - this.mapState_800c6798.dotOffset_18, this.mapState_800c6798.dotOffset_18, avg, prevDotPos, nextDotPos);

    this.modelAndAnimData_800c66a8.coord2_34.coord.transfer.set(avg);
    this.modelAndAnimData_800c66a8.coord2_34.coord.transfer.y -= 2.0f;

    if(this.mapState_800c6798.submapCutFrom_c4 == 242 && this.mapState_800c6798.submapSceneFrom_c6 == 3) { // Donau
      // First time Queen Fury leaves Donau, with forced movement
      if(gameState_800babc8.scriptFlags2_bc.get(0x8f)) {
        this.mapState_800c6798.shortForceMovementMode_d4 = ForcedMovementMode.NONE_0;
        this.mapState_800c6798.queenFuryForceMovementMode_d8 = ForcedMovementMode.WALK_1;
        this.mapState_800c6798.disableInput_d0 = true;
      }

      //LAB_800e8684
      // Queen Fury leaving Donau after first time
      if(gameState_800babc8.scriptFlags2_bc.get(0x90)) {
        this.mapState_800c6798.disableInput_d0 = true;
        this.mapState_800c6798.shortForceMovementMode_d4 = ForcedMovementMode.WALK_1;
        this.mapState_800c6798.queenFuryForceMovementMode_d8 = ForcedMovementMode.NONE_0;
      }
    }

    //LAB_800e8720
    this.modelAndAnimData_800c66a8.fastTravelTransitionMode_250 = FastTravelTransitionMode.NONE_0;
    this.modelAndAnimData_800c66a8.usingCoolonFromZenebatos_254 = false;

    //LAB_800e8770
    //LAB_800e87a0
    //LAB_800e87d0
    //LAB_800e8800
    // Teleport
    if(
      this.mapState_800c6798.submapCutFrom_c4 == 528 && this.mapState_800c6798.submapSceneFrom_c6 == 13 ||    // Zenebatos to Mayfil (first time)
        this.mapState_800c6798.submapCutFrom_c4 == 528 && this.mapState_800c6798.submapSceneFrom_c6 == 14 ||  // Zenebatos to Aglis
        this.mapState_800c6798.submapCutFrom_c4 == 528 && this.mapState_800c6798.submapSceneFrom_c6 == 15 ||  // Zenebatos to Mayfil
        this.mapState_800c6798.submapCutFrom_c4 == 540 && this.mapState_800c6798.submapSceneFrom_c6 == 19 ||  // Mayfil to Zenebatos
        this.mapState_800c6798.submapCutFrom_c4 == 572 && this.mapState_800c6798.submapSceneFrom_c6 == 23     // Aglis to Zenebatos
    ) {
      //LAB_800e8830
      this.modelAndAnimData_800c66a8.fastTravelTransitionMode_250 = FastTravelTransitionMode.TELEPORT_1;
      //LAB_800e8848

      // Zenebatos to Coolon map
    } else if(this.mapState_800c6798.submapCutFrom_c4 == 529 && this.mapState_800c6798.submapSceneFrom_c6 == 41) {
      this.modelAndAnimData_800c66a8.fastTravelTransitionMode_250 = FastTravelTransitionMode.OPEN_COOLON_MAP_2;
      this.modelAndAnimData_800c66a8.usingCoolonFromZenebatos_254 = true;
      gameState_800babc8.visitedLocations_17c.set(this.mapState_800c6798.locationIndex_10, true);
    }

    //LAB_800e8990
    this.mapTransitionState_800c68a4 = MapTransitionState.INIT_0;
    this.startLocationLabelsActive_800c68a8 = false;

    //LAB_800e89a4
    for(int i = 0; i < 8; i++) {
      //LAB_800e89c0
      this.startButtonLabelStages_800c86d4[i] = 0;
    }

    //LAB_800e89f4

    this.setPositionsOfValidMapPlaces();
  }

  @Method(0x800e8a10L)
  private void handlePlayerMovementOnPath() {
    //LAB_800e8a38
    if(this.worldMapState_800c6698.state > WorldMapState.INIT_MAP_ANIM_3.state && this.playerState_800c669c.state > PlayerState.INIT_PLAYER_MODEL_3.state) {
      //LAB_800e8a58
      this.handleTravelAlongPathSegment();
      this.checkAndInitPathSegmentChange();
      this.selectNewPathAtIntersection();
      this.updatePlayer();
    }
    //LAB_800e8a80
  }

  @Method(0x800e8a90L)
  private void handleForcedMovement() {
    if(this.mapState_800c6798.queenFuryForceMovementMode_d8 != ForcedMovementMode.NONE_0) {
      this.mapState_800c6798.disableInput_d0 = true;

      if(this.modelAndAnimData_800c66a8.fadeAnimationType_05 != FadeAnimationType.NONE_0) {
        return;
      }

      //LAB_800e8ae0
    } else {
      //LAB_800e8ae8
      if(this.mapState_800c6798.shortForceMovementMode_d4 == ForcedMovementMode.NONE_0) {
        return;
      }

      //LAB_800e8b04
      if(this.modelAndAnimData_800c66a8.modelIndex_1e4 >= 2) {
        return;
      }
    }

    //LAB_800e8b2c
    int movement;
    if(directionalPathSegmentData_800f2248[this.mapState_800c6798.directionalPathIndex_12].pathSegmentIndexAndDirection_00 < 0) {
      movement = -1;
    } else {
      //LAB_800e8b64
      movement = 1;
    }

    //LAB_800e8b68
    //LAB_800e8b94
    //LAB_800e8bc0
    if(movement < 0 && this.mapState_800c6798.facing_1c > 0 || movement > 0 && this.mapState_800c6798.facing_1c < 0) {
      //LAB_800e8bec
      movement = -movement;
    }

    //LAB_800e8bfc
    // These values never actually set to RUN, but who knows, maybe someone will want it.
    if(
      this.mapState_800c6798.shortForceMovementMode_d4 == ForcedMovementMode.RUN_2 ||
        this.mapState_800c6798.queenFuryForceMovementMode_d8 == ForcedMovementMode.RUN_2
    ) {
      //LAB_800e8c2c
      movement *= 2;
    }

    //LAB_800e8c40
    if(movement < 0) {
      this.mapState_800c6798.playerDestAngle_c0 = MathHelper.PI;
      this.mapState_800c6798.facing_1c = -1;
    } else {
      //LAB_800e8c70
      this.mapState_800c6798.playerDestAngle_c0 = 0.0f;
      this.mapState_800c6798.facing_1c = 1;
    }

    //LAB_800e8c84
    this.mapState_800c6798.dotOffset_18 += movement / (3.0f / vsyncMode_8007a3b8);

    //LAB_800e8ca0
  }

  @Method(0x800e8cb0L)
  private void handleTravelAlongPathSegment() {
    if(this.mapState_800c6798.pathSegmentPlayerMovingInto_f8 != PathSegmentEntering.CURRENT_0) {
      return;
    }

    //LAB_800e8cd8
    this.handleForcedMovement();

    if(!this.mapState_800c6798.disableInput_d0) {
      this.processInput();
    }

    //LAB_800e8cfc
    if(this.mapState_800c6798.dotOffset_18 >= 4.0f) {
      this.mapState_800c6798.dotIndex_16++;

      //LAB_800e8d48
      this.mapState_800c6798.dotOffset_18 %= 4.0f;

      final int maxDotIndex = pathSegmentLengths_800f5810[Math.abs(directionalPathSegmentData_800f2248[this.mapState_800c6798.directionalPathIndex_12].pathSegmentIndexAndDirection_00) - 1] - 1;

      if(this.mapState_800c6798.dotIndex_16 >= maxDotIndex) {
        this.mapState_800c6798.dotIndex_16 = maxDotIndex - 1;
        this.mapState_800c6798.dotOffset_18 = 3.0f;
        this.mapState_800c6798.pathSegmentPlayerMovingInto_f8 = PathSegmentEntering.NEXT_2;
      }

      //LAB_800e8dfc
      //LAB_800e8e04
    } else if(this.mapState_800c6798.dotOffset_18 < 0.0f) {
      this.mapState_800c6798.dotIndex_16--;
      this.mapState_800c6798.dotOffset_18 += 4.0f;

      if(this.mapState_800c6798.dotIndex_16 < 0) {
        this.mapState_800c6798.dotIndex_16 = 0;
        this.mapState_800c6798.dotOffset_18 = 0.0f;
        this.mapState_800c6798.pathSegmentPlayerMovingInto_f8 = PathSegmentEntering.PREVIOUS_1;
      }
    }

    //LAB_800e8e78
    this.initEnterQueenFuryTransition();

    //LAB_800e8e80
  }

  @Method(0x800e8e94L)
  private void initEnterQueenFuryTransition() {
    if(gameState_800babc8.scriptFlags2_bc.get(0x97)) {
      //LAB_800e8f24
      if(this.modelAndAnimData_800c66a8.modelIndex_1e4 == 1) {
        //LAB_800e8f48
        if(this.mapState_800c6798.queenFuryForceMovementMode_d8 == ForcedMovementMode.NONE_0) {
          //LAB_800e8f64
          //LAB_800e8f88
          if(this.modelAndAnimData_800c66a8.fadeAnimationType_05 == FadeAnimationType.NONE_0) {
            //LAB_800e8fac
            if(this.wmapCameraAndLights19c0_800c66b0.mapRotationState_110 == MapRotationState.MAIN_LOOP_0) {
              //LAB_800e8fd0
              if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.LOCAL_0) {
                //LAB_800e8ff4
                if(this.wmapCameraAndLights19c0_800c66b0.cameraUpdateState_c5 == CameraUpdateState.AWAIT_INPUT_0) {
                  //LAB_800e9018
                  if(this.wmapCameraAndLights19c0_800c66b0.zoomStateIsLocal_c4) {
                    //LAB_800e903c
                    if((this.filesLoadedFlags_800c66b8.get() & 0x1) != 0) {
                      //LAB_800e905c
                      if(this.tickMainMenuOpenTransition_800c6690 == 0) {
                        //LAB_800e9078
                        if(Input.pressedThisFrame(InputAction.BUTTON_WEST)) { // Square
                          if(this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc != PathSegmentEndpointType.TERMINAL_1) {
                            this.mapState_800c6798.submapCutTo_c8 = locations_800f0e34[93].submapCutTo_08;
                            this.mapState_800c6798.submapSceneTo_ca = locations_800f0e34[93].submapSceneTo_0a;
                            submapCut_80052c30 = this.mapState_800c6798.submapCutTo_c8;
                            submapScene_80052c34 = this.mapState_800c6798.submapSceneTo_ca;
                            this.initTransitionAnimation(FadeAnimationType.FADE_OUT_2);
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    //LAB_800e90f0
  }

  @Method(0x800e9104L)
  private void processInput() {
    //LAB_800e912c
    if(Loader.getLoadingFileCount() != 0 || this.modelAndAnimData_800c66a8.fadeAnimationType_05 != FadeAnimationType.NONE_0) {
      return;
    }

    //LAB_800e9150
    if(this.modelAndAnimData_800c66a8.modelIndex_1e4 >= 2) {
      return;
    }

    //LAB_800e9178
    if(this.worldMapState_800c6698 != WorldMapState.RENDER_5) {
      return;
    }

    //LAB_800e9194
    if(this.playerState_800c669c != PlayerState.RENDER_5) {
      return;
    }

    //LAB_800e91b0
    if(this.mapState_800c6798.queenFuryForceMovementMode_d8 != ForcedMovementMode.NONE_0) {
      return;
    }

    //LAB_800e91cc
    // Calculates Dart's orientation on the map, reducing it to 8 headings, counting counter-clockwise.
    // This is used to index 2 arrays, one for input values counting from N and the other counting from S.
    // Whichever mask gives a non-zero result determines whether the movement direction is positive or
    // negative. If both are zero or both are non-zero, Dart moves whichever way he is facing.
    if(
      Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP) ||
      Input.getButtonState(InputAction.DPAD_RIGHT) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_RIGHT) ||
      Input.getButtonState(InputAction.DPAD_DOWN) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_DOWN) ||
      Input.getButtonState(InputAction.DPAD_LEFT) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_LEFT)
    ) {
      final int directionMaskIndex = MathHelper.radToPsxDeg(MathHelper.floorMod(this.wmapCameraAndLights19c0_800c66b0.currMapRotation_70.y - this.mapState_800c6798.previousPlayerRotation_c2 - 0.875f * MathHelper.PI, MathHelper.TWO_PI)) >> 9;
      final boolean positiveDirection = Arrays.stream(positiveDirectionMovementMask_800f0204[directionMaskIndex]).anyMatch(Input::getButtonState);
      final boolean negativeDirection = Arrays.stream(negativeDirectionMovementMask_800f0210[directionMaskIndex]).anyMatch(Input::getButtonState);

      int movement;
      if(positiveDirection && !negativeDirection) {
        movement = 1;
        //LAB_800e92d0
      } else if(!positiveDirection && negativeDirection) {
        movement = -1;
      } else {
        //LAB_800e9300
        movement = this.mapState_800c6798.facing_1c;
      }

      //LAB_800e9330
      //LAB_800e9364
      if(Input.getButtonState(InputAction.BUTTON_EAST) || analogMagnitude_800beeb4 >= 0x7f) {
        //LAB_800e9384
        movement *= 2; // Running
      }

      if(movement != 0) {
        //LAB_800e9398
        this.mapState_800c6798.dotOffset_18 += movement / (3.0f / vsyncMode_8007a3b8);

        if(movement < 0) {
          this.mapState_800c6798.playerDestAngle_c0 = MathHelper.PI;
          this.mapState_800c6798.facing_1c = -1;
        } else {
          //LAB_800e93f4
          this.mapState_800c6798.playerDestAngle_c0 = 0.0f;
          this.mapState_800c6798.facing_1c = 1;
        }
      }
    }
    //LAB_800e9408
  }

  @Method(0x800e9418L)
  private void getPathPositions(final Vector3f prevDotPos, final Vector3f nextDotPos) {
    final Vector3f[] dots = pathDotPosArr_800f591c[this.mapState_800c6798.pathIndex_14];
    dots[this.mapState_800c6798.dotIndex_16].get(prevDotPos);
    dots[this.mapState_800c6798.dotIndex_16 + 1].get(nextDotPos);
  }

  @Method(0x800e94f0L)
  private void weightedAvg(final float weight1, final float weight2, final Vector3f out, final Vector3f vec1, final Vector3f vec2) {
    out.x = (weight1 * vec1.x + weight2 * vec2.x) / (weight1 + weight2);
    out.y = (weight1 * vec1.y + weight2 * vec2.y) / (weight1 + weight2);
    out.z = (weight1 * vec1.z + weight2 * vec2.z) / (weight1 + weight2);
  }

  /** Updates player rotation to orient them along the current path segment */
  @Method(0x800e9648L)
  private void updatePlayerRotation() {
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    modelAndAnimData.playerRotation_a4.set(0.0f, MathHelper.atan2(this.mapState_800c6798.prevDotPos_20.x - this.mapState_800c6798.nextDotPos_30.x, this.mapState_800c6798.prevDotPos_20.z - this.mapState_800c6798.nextDotPos_30.z), 0.0f);
    this.mapState_800c6798.previousPlayerRotation_c2 = modelAndAnimData.playerRotation_a4.y;
    modelAndAnimData.playerRotation_a4.y += this.mapState_800c6798.playerDestAngle_c0;
  }

  @Method(0x800e975cL)
  private void checkAndInitPathSegmentChange() {
    if(this.mapState_800c6798.pathSegmentPlayerMovingInto_f8 == PathSegmentEntering.CURRENT_0) {
      return;
    }

    //LAB_800e9784
    //LAB_800e9788
    for(int i = 0; i < 7; i++) {
      //LAB_800e97a4
      this.mapState_800c6798.tempPathSegmentIndices_dc[i] = -1;
    }

    final Vector3f prevDotPos = new Vector3f();
    final Vector3f nextDotPos = new Vector3f();
    final Vector3f pos = new Vector3f();

    //LAB_800e97dc
    this.getPathPositions(prevDotPos, nextDotPos);

    if(this.mapState_800c6798.pathSegmentPlayerMovingInto_f8 == PathSegmentEntering.PREVIOUS_1) {
      pos.set(prevDotPos);
    } else {
      //LAB_800e9834
      pos.set(nextDotPos);
    }

    //LAB_800e985c
    int index = 0;

    //LAB_800e9864
    for(int i = 0; i < this.mapState_800c6798.locationCount_08; i++) {
      //LAB_800e9888
      if(this.checkLocationIsValidAndOptionallySetPathStart(i, 0, null) == 0) {
        //LAB_800e98a8
        if(locations_800f0e34[i].unknownIndex_0c != -1) {
          //LAB_800e98e0
          final int directionalPathIndex = locations_800f0e34[i].directionalPathIndex_00;
          final int pathIndexAndDirection = directionalPathSegmentData_800f2248[directionalPathIndex].pathSegmentIndexAndDirection_00;

          if(this.mapState_800c6798.facing_1c <= 0 || pathIndexAndDirection >= 0) {
            //LAB_800e995c
            if(this.mapState_800c6798.facing_1c >= 0 || pathIndexAndDirection <= 0) {
              //LAB_800e9988
              final int pathIndex = Math.abs(pathIndexAndDirection) - 1;
              final int dotCount = pathSegmentLengths_800f5810[pathIndex];
              final Vector3f[] dots = pathDotPosArr_800f591c[pathIndex];
              dots[dotCount - 1].get(prevDotPos);
              dots[0].get(nextDotPos);

              if(flEq(pos.x, prevDotPos.x) && flEq(pos.y, prevDotPos.y) && flEq(pos.z, prevDotPos.z)) {
                dots[dotCount - 2].get(this.mapState_800c6798.tempPathSegmentStartOffsets_40[index]);
                this.mapState_800c6798.tempPathSegmentIndices_dc[index] = directionalPathIndex;
                index++;
                //LAB_800e9bd8
              } else if(flEq(pos.x, nextDotPos.x) && flEq(pos.y, nextDotPos.y) && flEq(pos.z, nextDotPos.z)) {
                dots[1].get(this.mapState_800c6798.tempPathSegmentStartOffsets_40[index]);
                this.mapState_800c6798.tempPathSegmentIndices_dc[index] = directionalPathIndex;
                index++;
              }
            }
          }
        }
      }
      //LAB_800e9ce0
    }

    //LAB_800e9cf8
    this.mapState_800c6798.correctPathSegmentStartPos.set(pos);
    this.mapState_800c6798.pathSegmentPlayerMovingInto_f8 = PathSegmentEntering.CURRENT_0;

    if(index == 1) {
      this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc = PathSegmentEndpointType.TERMINAL_1;
    } else {
      //LAB_800e9d48
      this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc = PathSegmentEndpointType.INTERSECTION_2;
    }
    //LAB_800e9d54
  }

  /**
   * Uses the input direction and angles of the intersecting paths relative to player heading
   * to determine which path Dart should turn onto.
   */
  @Method(0x800e9d68L)
  private void selectNewPathAtIntersection() {
    if(this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc != PathSegmentEndpointType.INTERSECTION_2) {
      return;
    }

    //LAB_800e9da0
    // Transition to Queen Fury deck for Shana scene
    if(this.mapState_800c6798.queenFuryForceMovementMode_d8 != ForcedMovementMode.NONE_0) {
      if(this.mapState_800c6798.queenFuryForceMovementMode_d8.modeIndex < ForcedMovementMode.FADE_OUT_3.modeIndex) {
        this.initTransitionAnimation(FadeAnimationType.FADE_OUT_2);
        submapCut_80052c30 = 285; // A Queen Fury cut
        submapScene_80052c34 = 32;
        this.mapState_800c6798.queenFuryForceMovementMode_d8 = ForcedMovementMode.FADE_OUT_3;
      }

      //LAB_800e9dfc
      return;
    }

    //LAB_800e9e04
    if(this.mapState_800c6798.disableInput_d0) {
      return;
    }

    //LAB_800e9e20
    int movementX = 0;
    int movementY = 0;

    if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
      movementY++;
    }

    if(Input.getButtonState(InputAction.DPAD_LEFT) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_LEFT)) {
      movementX--;
    }

    if(Input.getButtonState(InputAction.DPAD_DOWN) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
      movementY--;
    }

    if(Input.getButtonState(InputAction.DPAD_RIGHT) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_RIGHT)) {
      movementX++;
    }

    final float angle = MathHelper.atan2(movementX, movementY);

    //LAB_800e9e90
    final Vector3f playerOffsetFromPathStart = new Vector3f();
    int newPathIndex = 0;
    boolean newPathSelected = false;
    float currentMovementToPathAngle = MathHelper.TWO_PI;
    for(int i = 0; i < 7; i++) {
      //LAB_800e9eac
      if(this.mapState_800c6798.tempPathSegmentIndices_dc[i] < 0) {
        break;
      }

      //LAB_800e9edc
      playerOffsetFromPathStart.set(this.modelAndAnimData_800c66a8.currPlayerPos_94).sub(this.mapState_800c6798.tempPathSegmentStartOffsets_40[i]);

      final float playerAngleRelativeToPath = (this.wmapCameraAndLights19c0_800c66b0.currMapRotation_70.y - MathHelper.atan2(playerOffsetFromPathStart.x, playerOffsetFromPathStart.z) + MathHelper.PI) % MathHelper.TWO_PI;

      final int movementMaskIndex = (MathHelper.radToPsxDeg(playerAngleRelativeToPath) + 0x100 & 0xfff) >> 9;
      if(Arrays.stream(positiveDirectionMovementMask_800f0204[movementMaskIndex]).anyMatch(Input::getButtonState)) {
        final float smallestMovementToPathAngle = currentMovementToPathAngle;
        final float pathAngleCwFromMovement = Math.abs(playerAngleRelativeToPath - angle);
        final float pathAngleCcwFromMovement = Math.abs(playerAngleRelativeToPath - angle - MathHelper.TWO_PI);

        currentMovementToPathAngle = Math.min(pathAngleCcwFromMovement, pathAngleCwFromMovement);

        //LAB_800ea118
        // If new angle currentMovementToPathAngle < smallestMovementToPathAngle (previous currentMovementToPathAngle),
        // then this is now the closest path to the movement direction
        if(smallestMovementToPathAngle >= currentMovementToPathAngle) {
          newPathIndex = i;
        }

        //LAB_800ea13c
        newPathSelected = true;
      }
      //LAB_800ea144
    }

    //LAB_800ea15c
    if(!newPathSelected) {
      return;
    }

    //LAB_800ea174
    this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc = PathSegmentEndpointType.NOT_AT_ENDPOINT_0;

    this.identifyNewPathSegmentAndSetStateInfo(this.mapState_800c6798.tempPathSegmentIndices_dc[newPathIndex]);

    final DirectionalPathSegmentData08 directionalPathSegment = directionalPathSegmentData_800f2248[this.mapState_800c6798.directionalPathIndex_12];

    //LAB_800ea1dc
    final Vector3f[] dots = pathDotPosArr_800f591c[this.mapState_800c6798.pathIndex_14];

    final Vector3f dot;
    if(directionalPathSegment.pathSegmentIndexAndDirection_00 >= 0) {
      dot = dots[0];
    } else {
      //LAB_800ea248
      dot = dots[pathSegmentLengths_800f5810[this.mapState_800c6798.pathIndex_14] - 1];
    }

    //LAB_800ea2a8
    // This corrects situations where for whatever reason Dart would end up teleporting
    // to the wrong end of a path segment he was entering. Not sure why this can happen yet.
    if(this.mapState_800c6798.correctPathSegmentStartPos.x != dot.x || this.mapState_800c6798.correctPathSegmentStartPos.y != dot.y || this.mapState_800c6798.correctPathSegmentStartPos.z != dot.z) {
      //LAB_800ea2f8
      if(directionalPathSegment.pathSegmentIndexAndDirection_00 >= 0) {
        this.mapState_800c6798.dotIndex_16 = pathSegmentLengths_800f5810[Math.abs(directionalPathSegment.pathSegmentIndexAndDirection_00) - 1] - 2;
        this.mapState_800c6798.dotOffset_18 = 2.0f;
        this.mapState_800c6798.facing_1c = -1;
        this.mapState_800c6798.playerDestAngle_c0 = MathHelper.PI;
      } else {
        //LAB_800ea39c
        this.mapState_800c6798.dotIndex_16 = 0;
        this.mapState_800c6798.dotOffset_18 = 1.0f;
        this.mapState_800c6798.facing_1c = 1;
        this.mapState_800c6798.playerDestAngle_c0 = 0.0f;
      }
    }
    //LAB_800ea3c4
  }

  /** Updates player position and rotation to orient with current path segment */
  @Method(0x800ea3d8L)
  private void updatePlayer() {
    final Vector3f prevDotPos = new Vector3f();
    final Vector3f nextDotPos = new Vector3f();

    this.getPathPositions(prevDotPos, nextDotPos);
    this.weightedAvg(4.0f - this.mapState_800c6798.dotOffset_18, this.mapState_800c6798.dotOffset_18, this.modelAndAnimData_800c66a8.currPlayerPos_94, prevDotPos, nextDotPos);
    this.modelAndAnimData_800c66a8.currPlayerPos_94.y -= 2.0f;
    this.mapState_800c6798.prevDotPos_20.set(prevDotPos);
    this.mapState_800c6798.nextDotPos_30.set(nextDotPos);

    this.updatePlayerRotation();
  }

  @Method(0x800ea4dcL)
  private void identifyNewPathSegmentAndSetStateInfo(final int directionalPathIndex) {
    this.mapState_800c6798.directionalPathIndex_12 = directionalPathIndex;

    //LAB_800ea4fc
    int i;
    for(i = 0; i < this.mapState_800c6798.locationCount_08; i++) {
      //LAB_800ea520
      //LAB_800ea558
      if(this.checkLocationIsValidAndOptionallySetPathStart(i, 0, null) == 0) {
        //LAB_800ea578
        //LAB_800ea5bc
        if(locations_800f0e34[i].directionalPathIndex_00 == directionalPathIndex) {
          break;
        }
      }
      //LAB_800ea5f8
    }

    //LAB_800ea610
    this.setNewPathSegmentStateInfo(i);
  }

  /** Sets relevant state fields for the path segment the player is either loading into or entering. */
  @Method(0x800ea630L)
  private void setNewPathSegmentStateInfo(final int locationIndex) {
    //LAB_800ea6bc
    if(this.checkLocationIsValidAndOptionallySetPathStart(locationIndex, 0, null) != 0) {
      return;
    }

    //LAB_800ea6dc
    this.mapState_800c6798.locationIndex_10 = locationIndex;
    this.mapState_800c6798.directionalPathIndex_12 = locations_800f0e34[locationIndex].directionalPathIndex_00;

    final DirectionalPathSegmentData08 directionalPathSegment = directionalPathSegmentData_800f2248[this.mapState_800c6798.directionalPathIndex_12];
    this.mapState_800c6798.pathIndex_14 = Math.abs(directionalPathSegment.pathSegmentIndexAndDirection_00) - 1; // Transition to a different path

    //LAB_800ea790
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    final Vector3f[] dots = pathDotPosArr_800f591c[this.mapState_800c6798.pathIndex_14];

    final float dx;
    final float dz;
    if(directionalPathSegment.pathSegmentIndexAndDirection_00 >= 0) {
      modelAndAnimData.coord2_34.coord.transfer.set(dots[0].x, dots[0].y - 2, dots[0].z);

      dx = dots[0].x - dots[1].x;
      dz = dots[0].z - dots[1].z;

      this.mapState_800c6798.dotIndex_16 = 0;
      this.mapState_800c6798.dotOffset_18 = 0.0f;
      this.mapState_800c6798.playerDestAngle_c0 = 0.0f;
      this.mapState_800c6798.facing_1c = 1;
    } else {
      //LAB_800ea8d4
      final int dotIndex = pathSegmentLengths_800f5810[Math.abs(directionalPathSegment.pathSegmentIndexAndDirection_00) - 1] - 1;
      dx = dots[dotIndex].x - dots[dotIndex - 1].x;
      dz = dots[dotIndex].z - dots[dotIndex - 1].z;

      modelAndAnimData.coord2_34.coord.transfer.set(dots[dotIndex].x, dots[dotIndex].y - 2, dots[dotIndex].z);

      this.mapState_800c6798.dotIndex_16 = dotIndex - 1;
      this.mapState_800c6798.dotOffset_18 = 3.0f;
      this.mapState_800c6798.playerDestAngle_c0 = MathHelper.PI;
      this.mapState_800c6798.facing_1c = -1;
    }

    //LAB_800eaafc
    modelAndAnimData.playerRotation_a4.set(0.0f, MathHelper.atan2(dx, dz), 0.0f);

    this.mapState_800c6798.previousPlayerRotation_c2 = modelAndAnimData.playerRotation_a4.y;
    this.mapState_800c6798.pathSegmentPlayerMovingInto_f8 = PathSegmentEntering.CURRENT_0;
    this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc = PathSegmentEndpointType.NOT_AT_ENDPOINT_0;

    //LAB_800eab80
  }

  @Method(0x800eab94L)
  private void initDataForReversePath(final int locationIndex) {
    //LAB_800eabdc
    //LAB_800eac20
    if(this.checkLocationIsValidAndOptionallySetPathStart(locationIndex, 0, null) != 0) {
      return;
    }

    //LAB_800eac40
    this.mapState_800c6798.locationIndex_10 = locationIndex;
    this.mapState_800c6798.directionalPathIndex_12 = locations_800f0e34[locationIndex].directionalPathIndex_00;

    final DirectionalPathSegmentData08 directionalPathSegment = directionalPathSegmentData_800f2248[this.mapState_800c6798.directionalPathIndex_12];
    this.mapState_800c6798.pathIndex_14 = Math.abs(directionalPathSegment.pathSegmentIndexAndDirection_00) - 1;

    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    final Vector3f[] dots = pathDotPosArr_800f591c[this.mapState_800c6798.pathIndex_14];

    final float dx;
    final float dz;
    if(this.mapState_800c6798.facing_1c > 0) {
      final int dotIndex = pathSegmentLengths_800f5810[Math.abs(directionalPathSegment.pathSegmentIndexAndDirection_00) - 1] - 1;
      modelAndAnimData.coord2_34.coord.transfer.set(dots[dotIndex].x, dots[dotIndex].y - 2, dots[dotIndex].z);
      this.mapState_800c6798.dotIndex_16 = dotIndex - 1;
      this.mapState_800c6798.dotOffset_18 = 3.0f;
      this.mapState_800c6798.playerDestAngle_c0 = 0.0f;
      dx = dots[dotIndex].x - dots[dotIndex - 1].x;
      dz = dots[dotIndex].z - dots[dotIndex - 1].z;
    } else {
      //LAB_800eaf14
      modelAndAnimData.coord2_34.coord.transfer.set(dots[0].x, dots[0].y - 2, dots[0].z);
      this.mapState_800c6798.dotIndex_16 = 0;
      this.mapState_800c6798.dotOffset_18 = 0.0f;
      this.mapState_800c6798.playerDestAngle_c0 = MathHelper.PI;
      dx = dots[0].x - dots[1].x;
      dz = dots[0].z - dots[1].z;
    }

    //LAB_800eb00c
    modelAndAnimData.playerRotation_a4.set(0.0f, MathHelper.atan2(dx, dz), 0.0f);
    this.mapState_800c6798.previousPlayerRotation_c2 = modelAndAnimData.playerRotation_a4.y;
    this.mapState_800c6798.pathSegmentEndpointTypeCrossed_fc = PathSegmentEndpointType.NOT_AT_ENDPOINT_0;

    //LAB_800eb088
  }

  /**
   * Mode used to be either 0, -1, or a VECTOR. If passing a VECTOR, pass it as dotPos and set mode to 1
   * @return state
   * <ol start="-3">
   *   <li>Path not found</li>
   *   <li>Location not on current continent</li>
   *   <li>Location does not exist</li>
   *   <li>Location and path valid</li>
   *   <li>Destination flags for location not set on gameState</li>
   * </ol>
   */
  @Method(0x800eb09cL)
  private int checkLocationIsValidAndOptionallySetPathStart(final int locationIndex, final int mode, @Nullable final Vector3f dotPos) {
    if(locations_800f0e34[locationIndex].directionalPathIndex_00 == -1) {
      return -1;
    }

    //LAB_800eb0ec
    if(mode != -1) {
      if(locations_800f0e34[locationIndex].continent_0e != this.mapState_800c6798.continent_00) {
        return -2;
      }
    }

    //LAB_800eb144
    if(!gameState_800babc8.wmapFlags_15c.get(locationIndex)) {
      return 1;
    }

    //LAB_800eb1d0
    if(mode == 0 || mode == -1) {
      //LAB_800eb1f8
      return 0;
    }

    //LAB_800eb204
    final int pathIndexAndDirection = directionalPathSegmentData_800f2248[locations_800f0e34[locationIndex].directionalPathIndex_00].pathSegmentIndexAndDirection_00;

    // This should only be the zeroed entry at the end of the array. Used to indicate its endpoint
    if(pathIndexAndDirection == 0) {
      return -3;
    }

    //LAB_800eb264
    final int pathIndex = Math.abs(pathIndexAndDirection) - 1;
    final Vector3f[] dots = pathDotPosArr_800f591c[pathIndex];

    assert dotPos != null;
    if(pathIndexAndDirection > 0) {
      dots[0].get(dotPos);
    } else {
      //LAB_800eb2fc
      dots[pathSegmentLengths_800f5810[pathIndex] - 1].get(dotPos);
    }

    //LAB_800eb3a8
    //LAB_800eb3b4
    return 0;
  }

  /** In this context, places are locations that have submaps and that the player has unlocked. */
  @Method(0x800eb3c8L)
  private void setPositionsOfValidMapPlaces() {
    final boolean[] locationsChecked = new boolean[0x101];
    int placeIndex = 0;

    final Vector3f pos0 = new Vector3f();
    final Vector3f pos1 = new Vector3f();
    final Vector3f avgPos = new Vector3f();
    final Vector3f[] matchPositions = new Vector3f[0x101];
    Arrays.setAll(matchPositions, i -> new Vector3f());

    //LAB_800eb420
    //LAB_800eb424
    for(int locationIndex0 = 0; locationIndex0 < this.mapState_800c6798.locationCount_08; locationIndex0++) {
      //LAB_800eb448
      if(this.checkLocationIsValidAndOptionallySetPathStart(locationIndex0, 0, null) == 0) {
        //LAB_800eb468
        if(!locationsChecked[locationIndex0]) {
          //LAB_800eb48c
          final int placeIndex0 = locations_800f0e34[locationIndex0].placeIndex_02;
          int matchCount = 0;

          //LAB_800eb4c8
          for(int locationIndex1 = locationIndex0; locationIndex1 < this.mapState_800c6798.locationCount_08; locationIndex1++) {
            //LAB_800eb4ec
            if(this.checkLocationIsValidAndOptionallySetPathStart(locationIndex1, 0, null) == 0) {
              //LAB_800eb50c
              if(!locationsChecked[locationIndex1]) {
                //LAB_800eb530
                final int placeIndex1 = locations_800f0e34[locationIndex1].placeIndex_02;

                final String placeName0 = places_800f0234[placeIndex0].name_00;
                final String placeName1 = places_800f0234[placeIndex1].name_00;
                if(placeName0 != null || placeName1 != null) {
                  // Added this check since these pointers can be null
                  if(placeName0 != null && placeName1 != null) {
                    //LAB_800eb5d8
                    if(strcmp(placeName0, placeName1) == 0) {
                      this.checkLocationIsValidAndOptionallySetPathStart(locationIndex1, 1, matchPositions[matchCount]);

                      matchCount++;
                      locationsChecked[locationIndex1] = true;
                    }
                  }
                } else {
                  locationsChecked[locationIndex1] = true;
                }
              }
            }

            //LAB_800eb67c
          }

          //LAB_800eb694
          if(matchCount == 1) {
            placePositionVectors_800c74b8[placeIndex].set(matchPositions[0]);
          } else {
            //LAB_800eb724
            pos0.set(matchPositions[0]);

            //LAB_800eb750
            for(int matchNum = 0; matchNum < matchCount - 1; matchNum++) {
              //LAB_800eb778
              pos1.set(matchPositions[matchNum + 1]);
              this.weightedAvg(1.0f, 1.0f, avgPos, pos0, pos1);
              pos0.set(avgPos);
            }

            //LAB_800eb828
            placePositionVectors_800c74b8[placeIndex].set(avgPos);
          }

          //LAB_800eb8ac
          placeIndices_800c84c8[placeIndex] = locationIndex0;

          placeIndex++;
        }
      }
      //LAB_800eb8dc
    }

    //LAB_800eb8f4
    this.placeCount_800c86cc = placeIndex;
  }

  @Method(0x800eb914L)
  private void allocateSmoke() {
    this.currentWmapEffect_800f6598 = (locations_800f0e34[this.mapState_800c6798.locationIndex_10].effectFlags_12 & 0x30) >>> 4;
    this.previousWmapEffect_800f659c = this.currentWmapEffect_800f6598;

    this.smokeInstances_800c86f8 = new WmapSmokeInstance60[48];

    Arrays.setAll(this.smokeInstances_800c86f8, i -> new WmapSmokeInstance60());

    //LAB_800eb9b8
    for(int i = 0; i < 48; i++) {
      final WmapSmokeInstance60 smoke = this.smokeInstances_800c86f8[i];

      //LAB_800eb9d4
      GsInitCoordinate2(null, smoke.coord2_00);

      //LAB_800eba0c
      //LAB_800ebaa0
      smoke.translationOffset_54.x =  rand() % 8 - 4;
      smoke.translationOffset_54.y = -rand() % 3 - 2;
      smoke.translationOffset_54.z =  rand() % 8 - 4;

      //LAB_800ebadc
      smoke.scaleAndColourFade_50 = rand() % 0x80;
    }
    //LAB_800ebb18
  }

  @Method(0x800ebb2cL)
  private void noOpAllocate() {
    // No-op
  }

  @Method(0x800ebb34L)
  private void noOpRender() {
    // No-op
  }

  @Method(0x800ebb44L)
  private void allocateClouds() {
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    modelAndAnimData.atmosphericEffectSprites = WMapAtmosphericEffectInstance60.buildCloudSprites();

    modelAndAnimData.atmosphericEffectInstances_24 = new WMapAtmosphericEffectInstance60[24];

    //LAB_800ebbb4
    final Vector3f translation = new Vector3f();
    for(int i = 0; i < 12; i++) {
      final WMapAtmosphericEffectInstance60 cloud = new WMapAtmosphericEffectInstance60();
      modelAndAnimData.atmosphericEffectInstances_24[i] = cloud;

      //LAB_800ebbd0
      GsInitCoordinate2(null, cloud.coord2_00);

      if((i & 0x1) == 0) {
        translation.set(
          700 - rand() % 1400,
          -70 - rand() %   40,
          700 - rand() % 1400
        );

        cloud.coord2_00.coord.transfer.set(translation);
      } else {
        //LAB_800ebd18
        cloud.coord2_00.coord.transfer.set(translation).sub(
          rand() % 200 - 100,
          rand() %  80 -  40,
          rand() %  50 -  25
        );
      }

      //LAB_800ebe24
      cloud.snowTick_50 = 0;
      cloud.translation_58.set((288 - rand() % 64) / 2.0f, (80 - rand() % 32) / 2.0f, 0.0f);
      cloud.brightness_5c = 0.0f;
    }

    //LAB_800ebf2c
    //LAB_800ebf30
    for(int i = 0; i < 12; i++) {
      final WMapAtmosphericEffectInstance60 cloud = new WMapAtmosphericEffectInstance60();
      modelAndAnimData.atmosphericEffectInstances_24[i + 12] = cloud;
      cloud.set(modelAndAnimData.atmosphericEffectInstances_24[i]);
      cloud.coord2_00.coord.transfer.y = 0.0f;
    }
  }

  @Method(0x800ebfc0L)
  private void renderClouds() {
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    final WMapAtmosphericEffectInstance60 cloud0 = modelAndAnimData.atmosphericEffectInstances_24[0];
    cloud0.coord2_00.flg = 0;

    //LAB_800ec028
    for(int i = 0; i < 24; i++) {
      final WMapAtmosphericEffectInstance60 cloud = modelAndAnimData.atmosphericEffectInstances_24[i];

      //LAB_800ec044
      cloud.translation_58.z += 1.0f / (3.0f / vsyncMode_8007a3b8);
      if((int)cloud.translation_58.z >> i % 3 + 4 != 0) {
        cloud.coord2_00.coord.transfer.x += 1.0f / (3.0f / vsyncMode_8007a3b8);
        cloud.translation_58.z = 0.0f;
      }

      //LAB_800ec288
      if(cloud.coord2_00.coord.transfer.x > 700) {
        cloud.coord2_00.coord.transfer.x = -700;
      }

      //LAB_800ec2b0
      if(!this.wmapCameraAndLights19c0_800c66b0.zoomStateIsLocal_c4) {
        cloud.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

        if(cloud.brightness_5c < 0.0f) {
          cloud.brightness_5c = 0.0f;
        }

        //LAB_800ec30c
      } else {
        //LAB_800ec314
        if(cloud.brightness_5c < 0.375f) {
          cloud.brightness_5c += 0.0625f / (3.0f / vsyncMode_8007a3b8);
        }

        //LAB_800ec34c
        if(modelAndAnimData.fadeAnimationType_05 == FadeAnimationType.FADE_OUT_2) {
          cloud.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

          if(cloud.brightness_5c < 0.0f) {
            cloud.brightness_5c = 0.0f;
          }
        }
      }

      //LAB_800ec3a8
      if(!flEq(cloud.brightness_5c, 0.0f)) {
        //LAB_800ec3c8
        GsGetLs(cloud.coord2_00, cloud.transforms);
        cloud.transforms.identity(); // NOTE: does not clear translation
        GTE.setTransforms(cloud.transforms);
        GTE.perspectiveTransform(-cloud.translation_58.x, -cloud.translation_58.y, 0.0f);
        final float sx0 = GTE.getScreenX(2);
        final float sy0 = GTE.getScreenY(2);
        float z = GTE.getScreenZ(3) / 4.0f;

        if(z >= 5 && z < orderingTableSize_1f8003c8 - 3) {
          //LAB_800ec534
          GTE.perspectiveTransform(cloud.translation_58.x, -cloud.translation_58.y, 0.0f);
          final float sx1 = GTE.getScreenX(2);
          final float sy1 = GTE.getScreenY(2);
          z = GTE.getScreenZ(3) / 4.0f;

          if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sx1 - sx0 <= 0x400) {
            //LAB_800ec5ec
            GTE.perspectiveTransform(-cloud.translation_58.x, cloud.translation_58.y, 0.0f);
            final float sx2 = GTE.getScreenX(2);
            final float sy2 = GTE.getScreenY(2);
            z = GTE.getScreenZ(3) / 4.0f;

            if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sy2 - sy0 <= 0x200) {
              //LAB_800ec670
              //LAB_800ec6a4
              if(sy2 > 0) {
                cloud.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

                if(cloud.brightness_5c < 0.0f) {
                  cloud.brightness_5c = 0.0f;
                }
                //LAB_800ec6fc
              } else {
                //LAB_800ec704
                if(cloud.brightness_5c < 0.375f) {
                  cloud.brightness_5c += 0.0625f / (3.0f / vsyncMode_8007a3b8);
                }

                //LAB_800ec73c
                if(modelAndAnimData.fadeAnimationType_05 == FadeAnimationType.FADE_OUT_2) {
                  cloud.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

                  if(cloud.brightness_5c < 0.0f) {
                    cloud.brightness_5c = 0.0f;
                  }
                }
              }

              //LAB_800ec798
              if(!flEq(cloud.brightness_5c, 0.0f)) {
                //LAB_800ec7b8
                GTE.perspectiveTransform(cloud.translation_58.x, cloud.translation_58.y, 0.0f);
                final float sx3 = GTE.getScreenX(2);
                final float sy3 = GTE.getScreenY(2);
                z = GTE.getScreenZ(3) / 4.0f;

                if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sx3 - sx2 <= 0x400 && sy3 - sy1 <= 0x200) {
                  //LAB_800ec83c
                  //LAB_800ec870
                  //LAB_800ec8a4
                  cloud.queueZ = i < 12 ? 556.0f : (orderingTableSize_1f8003c8 - 4.0f) * 4.0f;
                  cloud.transforms.scaling(sx1 - sx0, sy2 - sy0, 1.0f);
                  cloud.transforms.transfer.set(GPU.getOffsetX() + sx0, GPU.getOffsetY() + sy0, cloud.queueZ);
                  RENDERER.queueOrthoModel(modelAndAnimData.atmosphericEffectSprites[i % 3], cloud.transforms, QueuedModelStandard.class)
                    .monochrome(i < 12 ? cloud.brightness_5c : cloud.brightness_5c / 3.0f);
                }
              }
            }
          }
        }
      }
    }
    //LAB_800eca1c
  }

  @Method(0x800eca3cL)
  private void allocateSnow() {
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    modelAndAnimData.atmosphericEffectSprites = WMapAtmosphericEffectInstance60.buildSnowSprites();

    modelAndAnimData.atmosphericEffectInstances_24 = new WMapAtmosphericEffectInstance60[64];

    //LAB_800eca94
    for(int i = 0; i < 64; i++) {
      final WMapAtmosphericEffectInstance60 snowflake = new WMapAtmosphericEffectInstance60();
      modelAndAnimData.atmosphericEffectInstances_24[i] = snowflake;

      //LAB_800ecab0
      GsInitCoordinate2(null, snowflake.coord2_00);
      snowflake.coord2_00.coord.transfer.x = 500 - rand() % 1000;
      snowflake.coord2_00.coord.transfer.y =     - rand() %  200;
      snowflake.coord2_00.coord.transfer.z = 500 - rand() % 1000;
      snowflake.snowTick_50 = rand() % 12;
      snowflake.translation_58.set(rand() % 2 - 1, rand() % 2 + 1, rand() % 2 - 1);
      snowflake.brightness_5c = 0.0f;
    }
    //LAB_800eccfc
  }

  @Method(0x800ecd10L)
  private void renderSnow() {
    //LAB_800ecdb4
    final WMapModelAndAnimData258 modelAndAnimData = this.modelAndAnimData_800c66a8;
    for(int i = 0; i < 64; i++) {
      final WMapAtmosphericEffectInstance60 snowflake = modelAndAnimData.atmosphericEffectInstances_24[i];

      //LAB_800ecdd0
      if(!this.wmapCameraAndLights19c0_800c66b0.zoomStateIsLocal_c4) {
        snowflake.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

        if(snowflake.brightness_5c < 0.0f) {
          snowflake.brightness_5c = 0.0f;
        }

        //LAB_800ed0c8
      } else {
        //LAB_800ed0d0
        if(snowflake.brightness_5c < 0.375f) {
          snowflake.brightness_5c += 0.0625f / (3.0f / vsyncMode_8007a3b8);
        }

        //LAB_800ed108
        if(modelAndAnimData.fadeAnimationType_05 == FadeAnimationType.FADE_OUT_2) {
          snowflake.brightness_5c -= 0.125f / (3.0f / vsyncMode_8007a3b8);

          if(snowflake.brightness_5c < 0.0f) {
            snowflake.brightness_5c = 0.0f;
          }
        }
      }

      //LAB_800ed164
      if(!flEq(snowflake.brightness_5c, 0.0f)) {
        //LAB_800ed184
        snowflake.coord2_00.coord.transfer.x += snowflake.translation_58.x / (3.0f / vsyncMode_8007a3b8);
        snowflake.coord2_00.coord.transfer.y += snowflake.translation_58.y / (3.0f / vsyncMode_8007a3b8);
        snowflake.coord2_00.coord.transfer.z += snowflake.translation_58.z / (3.0f / vsyncMode_8007a3b8);

        if(snowflake.coord2_00.coord.transfer.y > 0.0f) {
          snowflake.coord2_00.coord.transfer.x =  500 - rand() % 1000;
          snowflake.coord2_00.coord.transfer.y = -200;
          snowflake.coord2_00.coord.transfer.z =  500 - rand() % 1000;
        }

        //LAB_800ed2bc
        snowflake.coord2_00.flg = 0;
        GsGetLs(snowflake.coord2_00, snowflake.transforms);
        snowflake.transforms.identity(); // NOTE: does not clear translation
        GTE.setTransforms(snowflake.transforms);
        GTE.perspectiveTransform(-2, -2, 0);

        final float sx0 = GTE.getScreenX(2);
        final float sy0 = GTE.getScreenY(2);
        float z = GTE.getScreenZ(3) / 4.0f;

        if(z >= 5 && z < orderingTableSize_1f8003c8 - 3) {
          //LAB_800ed37c
          GTE.perspectiveTransform(2, -2, 0);

          final float sx1 = GTE.getScreenX(2);
          final float sy1 = GTE.getScreenY(2);
          z = GTE.getScreenZ(3) / 4.0f;

          if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sx1 - sx0 <= 0x400) {
            //LAB_800ed400
            //LAB_800ed434
            GTE.perspectiveTransform(-2, 2, 0);

            final float sx2 = GTE.getScreenX(2);
            final float sy2 = GTE.getScreenY(2);
            z = GTE.getScreenZ(3) / 4.0f;

            if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sy2 - sy0 <= 0x200) {
              //LAB_800ed4b8
              //LAB_800ed4ec
              GTE.perspectiveTransform(2, 2, 0);

              final float sx3 = GTE.getScreenX(2);
              final float sy3 = GTE.getScreenY(2);
              z = GTE.getScreenZ(3) / 4.0f;

              if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sx3 - sx2 <= 0x400 && sy3 - sy1 <= 0x200) {
                //LAB_800ed570
                //LAB_800ed5a4
                //LAB_800ed5d8
                snowflake.snowTick_50 = (snowflake.snowTick_50 + 1.0f / (3.0f / vsyncMode_8007a3b8)) % 12;
                final int index = (int)(snowflake.snowTick_50 / 2.0f);
                snowflake.transforms.scaling(sx1 - sx0, sy2 - sy0, 1.0f);
                snowflake.transforms.transfer.set(GPU.getOffsetX() + sx0, GPU.getOffsetY() + sy0, 556.0f);
                RENDERER.queueOrthoModel(modelAndAnimData.atmosphericEffectSprites[index], snowflake.transforms, QueuedModelStandard.class)
                  .monochrome(snowflake.brightness_5c);
              }
            }
          }
        }
      }
    }
    //LAB_800ed93c
  }

  @Method(0x800ed95cL)
  private void handleSmokeAndAtmosphericEffects() {
    if(this.wmapCameraAndLights19c0_800c66b0.cameraUpdateState_c5 == CameraUpdateState.ZOOM_IN_2) {
      return;
    }

    //LAB_800ed98c
    switch(this.atmosphericEffectStage_800c66a4) {
      case 2 -> {
        if((this.filesLoadedFlags_800c66b8.get() & 0x1_0000) != 0 && (this.filesLoadedFlags_800c66b8.get() & 0x1000) != 0) {
          this.atmosphericEffectStage_800c66a4 = 3;
        }
      }

      //LAB_800eda18
      case 3 -> {
        this.atmosphericEffectAllocators_800f65a4[this.currentWmapEffect_800f6598].run();
        this.atmosphericEffectStage_800c66a4 = 4;
      }

      case 4 -> {
        if(
          this.worldMapState_800c6698.state >= WorldMapState.INIT_MAP_ANIM_3.state ||
            this.playerState_800c669c.state >= PlayerState.INIT_PLAYER_MODEL_3.state) {
          //LAB_800eda98
          this.atmosphericEffectStage_800c66a4 = 5;
        }
      }

      //LAB_800edaa4
      case 5 -> {
        this.previousWmapEffect_800f659c = this.currentWmapEffect_800f6598;
        this.currentWmapEffect_800f6598 = (locations_800f0e34[this.mapState_800c6798.locationIndex_10].effectFlags_12 & 0x30) >>> 4;
        if(this.currentWmapEffect_800f6598 != this.previousWmapEffect_800f659c) {
          this.deallocateAtmosphericEffect();
          this.atmosphericEffectStage_800c66a4 = 3;
        } else {
          //LAB_800edb5c
          this.atmosphericEffectRenderers_800f65b0[this.currentWmapEffect_800f6598].run();
        }
      }

      default -> throw new IllegalArgumentException("Invalid index " + this.atmosphericEffectStage_800c66a4);
    }

    //LAB_800edba4
    this.renderSmoke();

    //LAB_800edbac
  }

  @Method(0x800edbc0L)
  private void renderSmoke() {
    if((this.filesLoadedFlags_800c66b8.get() & 0x1000) == 0) {
      return;
    }

    //LAB_800edc04
    if(this.tickMainMenuOpenTransition_800c6690 != 0) {
      return;
    }

    //LAB_800edc20
    if(this.modelAndAnimData_800c66a8.zoomState_1f8 == ZoomState.WORLD_3) {
      return;
    }

    //LAB_800edc44
    if(this.worldMapState_800c6698.state <= WorldMapState.INIT_MAP_ANIM_3.state) {
      return;
    }

    //LAB_800edc64
    if(this.playerState_800c669c.state <= PlayerState.INIT_PLAYER_MODEL_3.state) {
      return;
    }

    //LAB_800edc84
    int smokeIndex = 0;

    //LAB_800edca8
    for(int i = 0; i < this.placeCount_800c86cc; i++) {
      //LAB_800edccc
      if(places_800f0234[locations_800f0e34[placeIndices_800c84c8[i]].placeIndex_02].name_00 == null) {
        continue;
      }

      //LAB_800edd3c
      final int mode = locations_800f0e34[placeIndices_800c84c8[i]].effectFlags_12 & 0xc;
      if(mode == 0) {
        continue;
      }

      //LAB_800edda0
      if(locations_800f0e34[placeIndices_800c84c8[i]].continent_0e == this.mapState_800c6798.continent_00) {
        //LAB_800eddfc
        if(i >= 9) {
          break;
        }

        //LAB_800ede18
        //LAB_800ede1c
        for(int j = 0; j < 6; j++) {
          //LAB_800ede38
          final WmapSmokeInstance60 smoke = this.smokeInstances_800c86f8[smokeIndex];

          final float size;
          if(mode == 8) {
            size = smoke.scaleAndColourFade_50 / 5.0f;
          } else {
            //LAB_800ede88
            size = smoke.scaleAndColourFade_50 / 3.0f;
          }

          //LAB_800edebc
          //LAB_800edf88
          smoke.coord2_00.coord.transfer.x = placePositionVectors_800c74b8[i].x + smoke.translationOffset_54.x * smoke.scaleAndColourFade_50 / 16;
          smoke.coord2_00.coord.transfer.y = placePositionVectors_800c74b8[i].y + smoke.translationOffset_54.y * smoke.scaleAndColourFade_50 / 4;
          smoke.coord2_00.coord.transfer.z = placePositionVectors_800c74b8[i].z + smoke.translationOffset_54.z * smoke.scaleAndColourFade_50 / 16;

          if(this.mapState_800c6798.continent_00 == Continent.SOUTH_SERDIO_0) {
            if(mode == 4) {
              //LAB_800ee0e4
              smoke.coord2_00.coord.transfer.x = placePositionVectors_800c74b8[i].x + smoke.translationOffset_54.x * smoke.scaleAndColourFade_50 / 16;
              smoke.coord2_00.coord.transfer.y = placePositionVectors_800c74b8[i].y + smoke.translationOffset_54.y * smoke.scaleAndColourFade_50 / 4;
              smoke.coord2_00.coord.transfer.z = placePositionVectors_800c74b8[i].z + smoke.translationOffset_54.z * smoke.scaleAndColourFade_50 / 16 + 80;
              //LAB_800ee1dc
            } else if(mode == 8) {
              //LAB_800ee238
              smoke.coord2_00.coord.transfer.x = placePositionVectors_800c74b8[i].x + smoke.translationOffset_54.x * smoke.scaleAndColourFade_50 / 16 + 48;
              smoke.coord2_00.coord.transfer.y = placePositionVectors_800c74b8[i].y + smoke.translationOffset_54.y * smoke.scaleAndColourFade_50 / 4;
              smoke.coord2_00.coord.transfer.z = placePositionVectors_800c74b8[i].z + smoke.translationOffset_54.z * smoke.scaleAndColourFade_50 / 16 + 48;
            }

            //LAB_800ee32c
            //LAB_800ee334
          } else if(this.mapState_800c6798.continent_00 == Continent.NORTH_SERDIO_1) {
            if(mode == 4) {
              //LAB_800ee3a4
              smoke.coord2_00.coord.transfer.x = placePositionVectors_800c74b8[i].x + smoke.translationOffset_54.x * smoke.scaleAndColourFade_50 / 16;
              smoke.coord2_00.coord.transfer.y = placePositionVectors_800c74b8[i].y + smoke.translationOffset_54.y * smoke.scaleAndColourFade_50 / 4 + 48;
              smoke.coord2_00.coord.transfer.z = placePositionVectors_800c74b8[i].z + smoke.translationOffset_54.z * smoke.scaleAndColourFade_50 / 16 - 100;
              //LAB_800ee4a0
            } else if(mode == 8) {
              //LAB_800ee4fc
              smoke.coord2_00.coord.transfer.x = placePositionVectors_800c74b8[i].x + smoke.translationOffset_54.x * smoke.scaleAndColourFade_50 / 16 - 48;
              smoke.coord2_00.coord.transfer.y = placePositionVectors_800c74b8[i].y + smoke.translationOffset_54.y * smoke.scaleAndColourFade_50 / 4;
              smoke.coord2_00.coord.transfer.z = placePositionVectors_800c74b8[i].z + smoke.translationOffset_54.z * smoke.scaleAndColourFade_50 / 16 + 32;
            }
          }

          //LAB_800ee5f0
          smoke.coord2_00.flg = 0;
          GsGetLs(smoke.coord2_00, smoke.transforms);
          smoke.transforms.identity(); // NOTE: does not clear translation
          GTE.setTransforms(smoke.transforms);

          GTE.perspectiveTransform(-size, -size, 0);
          final float sx0 = GTE.getScreenX(2);
          final float sy0 = GTE.getScreenY(2);
          float z = GTE.getScreenZ(3) / 4.0f;

          //LAB_800ee6cc
          if(z >= 5 || z < orderingTableSize_1f8003c8 - 3) {
            //LAB_800ee6d4
            GTE.perspectiveTransform(size, -size, 0);
            final float sx1 = GTE.getScreenX(2);
            final float sy1 = GTE.getScreenY(2);
            z = GTE.getScreenZ(3) / 4.0f;

            final float transformedSize = sx1 - sx0;

            //LAB_800ee750
            if(z >= 5 || z < orderingTableSize_1f8003c8 - 3 && sx1 - sx0 <= 0x400) {
              //LAB_800ee758
              //LAB_800ee78c
              GTE.perspectiveTransform(-size, size, 0);
              final float sx2 = GTE.getScreenX(2);
              final float sy2 = GTE.getScreenY(2);
              z = GTE.getScreenZ(3) / 4.0f;

              //LAB_800ee808
              if(z >= 5 && z < orderingTableSize_1f8003c8 - 3 && sy2 - sy0 <= 0x200) {
                //LAB_800ee810
                //LAB_800ee844
                GTE.perspectiveTransform(size, size, 0);
                final float sx3 = GTE.getScreenX(2);
                final float sy3 = GTE.getScreenY(2);
                z = GTE.getScreenZ(3) / 4.0f;

                //LAB_800ee8c0
                if(z >= 6 && z < orderingTableSize_1f8003c8 - 3 && sx3 - sx2 <= 0x400 && sy3 - sy1 <= 0x200) {
                  //LAB_800ee8c8
                  //LAB_800ee8fc
                  //LAB_800ee930
                  final Translucency translucency = mode == 8 ? Translucency.B_MINUS_F : Translucency.B_PLUS_F;

                  //LAB_800ee9b0
                  //LAB_800eea34
                  final int index = (int)(smoke.scaleAndColourFade_50 / 0x40);

                  if(smoke.objs[index] == null) {
                    smoke.objs[index] = new QuadBuilder("Smoke sprite " + index + " (index " + smokeIndex + ')')
                      .bpp(Bpp.BITS_4)
                      .vramPos(640, 256)
                      .pos(0.0f, 0.0f, 0.0f)
                      .size(1.0f, 1.0f)
                      .clut(640, 505)
                      .uv(96, index == 0 ? 48 : 80)
                      .uvSize(32, 32)
                      .translucency(translucency)
                      .build();
                  }

                  smoke.transforms.scaling(transformedSize);
                  smoke.transforms.transfer.set(GPU.getOffsetX() + sx0, GPU.getOffsetY() + sy0, z * 4.0f);
                  RENDERER.queueOrthoModel(smoke.objs[index], smoke.transforms, QueuedModelStandard.class)
                    .monochrome((0x80 - smoke.scaleAndColourFade_50) / 255.0f);

                  smoke.scaleAndColourFade_50 += 1.0f / (3.0f / vsyncMode_8007a3b8);

                  if(smoke.scaleAndColourFade_50 >= 0x80) {
                    smoke.scaleAndColourFade_50 = 0;
                  }
                  //LAB_800eeccc
                  smokeIndex++;
                }
              }
            }
          }
        }
      }
    }
    //LAB_800eed1c
    //LAB_800eed28
  }

  @Method(0x800eed3cL)
  private void deallocateAtmosphericEffect() {
    if(this.modelAndAnimData_800c66a8.atmosphericEffectSprites != null) {
      this.modelAndAnimData_800c66a8.deleteAtmosphericEffectObjs();
    }
  }

  @Method(0x800eede4L)
  private void deallocateSmoke() {
    for(final WmapSmokeInstance60 smoke : this.smokeInstances_800c86f8) {
      if(smoke.objs[0] != null) {
        smoke.objs[0].delete();
      }

      if(smoke.objs[1] != null) {
        smoke.objs[1].delete();
      }
    }

    this.smokeInstances_800c86f8 = null;
  }
}
