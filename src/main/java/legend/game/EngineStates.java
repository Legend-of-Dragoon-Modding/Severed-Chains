package legend.game;

import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.game.combat.Battle;
import legend.game.credits.Credits;
import legend.game.credits.FinalFmv;
import legend.game.modding.events.engine.EngineStateChangeEvent;
import legend.game.submap.SMap;
import legend.game.title.GameOver;
import legend.game.title.NewGame;
import legend.game.title.Ttle;
import legend.game.types.OverlayStruct;
import legend.game.wmap.WMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.Map;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.DISCORD;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Audio.FUN_8001aa90;
import static legend.game.Audio.loadMusicPackage;
import static legend.game.Audio.setMainVolume;
import static legend.game.Audio.soundEnv_800c6630;
import static legend.game.Audio.sssqResetStuff;
import static legend.game.Graphics.renderMode;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment_8004.engineStateFunctions_8004e29c;
import static legend.game.Scus94491BpeSegment_800b._800bd7ac;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.previousEngineState_800bdb88;
import static legend.game.Scus94491BpeSegment_800b.submapFullyLoaded_800bd7b4;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;
import static legend.game.Scus94491BpeSegment_800b.transitioningFromCombatToSubmap_800bd7b8;
import static legend.game.combat.SBtld.clearCombatVars;

public final class EngineStates {
  private EngineStates() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(EngineStates.class);

  /**
   * <ol start="0">
   *   <li>preload</li>
   *   <li>finalizePregameLoading</li>
   *   <li>{@link Ttle}</li>
   *   <li>{@link NewGame}</li>
   *   <li>{@link Credits}</li>
   *   <li>{@link SMap} Sets up rendering and loads scene</li>
   *   <li>{@link Battle}</li>
   *   <li>{@link GameOver}</li>
   *   <li>{@link WMap}</li>
   *   <li>startFmvLoadingStage</li>
   *   <li>swapDiskLoadingStage</li>
   *   <li>{@link FinalFmv}</li>
   *   <li>0x800c6eb8 (TODO)</li>
   *   <li>0x800cab8c (TODO)</li>
   *   <li>null</li>
   *   <li>0x800c6978 (TODO)</li>
   *   <li>null</li>
   *   <li>0x800cdcdc (TODO)</li>
   *   <li>0x800cabd4 (TODO)</li>
   *   <li>null</li>
   * </ol>
   */
  public static final Map<EngineStateEnum, OverlayStruct> gameStateOverlays_8004dbc0 = new EnumMap<>(EngineStateEnum.class);
  static {
    gameStateOverlays_8004dbc0.put(EngineStateEnum.TITLE_02, new OverlayStruct(Ttle.class, Ttle::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.TRANSITION_TO_NEW_GAME_03, new OverlayStruct(NewGame.class, NewGame::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.CREDITS_04, new OverlayStruct(Credits.class, Credits::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.SUBMAP_05, new OverlayStruct(SMap.class, SMap::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.COMBAT_06, new OverlayStruct(Battle.class, Battle::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.GAME_OVER_07, new OverlayStruct(GameOver.class, GameOver::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.WORLD_MAP_08, new OverlayStruct(WMap.class, WMap::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.FINAL_FMV_11, new OverlayStruct(FinalFmv.class, FinalFmv::new));
  }

  public static EngineState currentEngineState_8004dd04;

  /**
   * <ol>
   *   <li value="5">SMAP</li>
   *   <li value="6">Combat</li>
   *   <li value="7">Game over</li>
   *   <li value="8">WMAP</li>
   *   <li value="9">FMV?</li>
   *   <li value="11">Credits?</li>
   * </ol>
   */
  public static EngineStateEnum engineState_8004dd20 = EngineStateEnum.PRELOAD_00;
  /** When the overlay finishes loading, switch to this */
  public static EngineStateEnum engineStateOnceLoaded_8004dd24 = EngineStateEnum.PRELOAD_00;
  /** The previous state before the file finished loading */
  public static EngineStateEnum previousEngineState_8004dd28;
  /** The last savable state we were in, used for generating crash recovery saves */
  public static EngineStateEnum lastSavableEngineState;

  @Method(0x800128c4L)
  public static void loadQueuedOverlay() {
    //LAB_800129c0
    //LAB_800129c4
    if(engineStateOnceLoaded_8004dd24 != null) {
      pregameLoadingStage_800bb10c = 0;
      previousEngineState_8004dd28 = engineState_8004dd20;
      engineState_8004dd20 = engineStateOnceLoaded_8004dd24;
      engineStateOnceLoaded_8004dd24 = null;
      prepareOverlay();
      vsyncMode_8007a3b8 = 2;
      loadGameStateOverlay(engineState_8004dd20);
      menuStack.reset();

      if(engineState_8004dd20 == EngineStateEnum.COMBAT_06) { // Starting combat
        clearCombatVars();
      }

      EVENTS.postEvent(new EngineStateChangeEvent(previousEngineState_8004dd28, engineState_8004dd20, currentEngineState_8004dd04));
      currentEngineState_8004dd04.updateDiscordRichPresence(DISCORD.activity);
      DISCORD.updateActivity();
    }
  }

  @Method(0x80012a84L)
  public static void loadGameStateOverlay(final EngineStateEnum engineState) {
    LOGGER.info("Transitioning to engine state %s", engineState);

    SCRIPTS.setFramesPerTick(1);

    final OverlayStruct overlay = gameStateOverlays_8004dbc0.get(engineState);

    if(overlay.class_00.isInstance(currentEngineState_8004dd04)) {
      return;
    }

    Obj.clearObjList(false);

    //LAB_80012ad8
    currentEngineState_8004dd04 = overlay.constructor_00.get();
    engineStateFunctions_8004e29c = currentEngineState_8004dd04.getScriptFunctions();
    renderMode = currentEngineState_8004dd04.getRenderMode();
    RENDERER.setRenderMode(currentEngineState_8004dd04.getRenderMode());
    RENDERER.updateProjections();
  }

  @Method(0x80019710L)
  public static void prepareOverlay() {
    if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05 || engineState_8004dd20 == EngineStateEnum.WORLD_MAP_08) {
      lastSavableEngineState = engineState_8004dd20;
    }

    if(engineState_8004dd20 != EngineStateEnum.SUBMAP_05 && previousEngineState_8004dd28 == EngineStateEnum.SUBMAP_05) {
      sssqResetStuff();
    }

    //LAB_8001978c
    //LAB_80019790
    if(engineState_8004dd20 != EngineStateEnum.COMBAT_06 && previousEngineState_8004dd28 == EngineStateEnum.COMBAT_06) {
      sssqResetStuff();
    }

    //LAB_80019824
    //LAB_80019828
    switch(engineState_8004dd20) {
      case TITLE_02 -> {
        soundEnv_800c6630.fadingIn_2a = false;
        soundEnv_800c6630.fadingOut_2b = false;

        setMainVolume(0x7f, 0x7f);
        AUDIO_THREAD.setMainVolume(0x7f, 0x7f);
        sssqResetStuff();
        FUN_8001aa90();

        //LAB_80019a00
        if(drgnBinIndex_800bc058 == 1) {
          // Load main menu background music
          loadMusicPackage(1);
        } else {
          loadMusicPackage(98);
        }
      }

      case CREDITS_04, SUBMAP_05, COMBAT_06, GAME_OVER_07, WORLD_MAP_08, FMV_09, FINAL_FMV_11 -> sssqResetStuff();
    }

    //case 3, d, e
    //LAB_80019a20
    //LAB_80019a24
    if(engineState_8004dd20 != EngineStateEnum.SUBMAP_05) {
      submapId_800bd808 = -1;
    }

    //LAB_80019a3c
  }

  @Method(0x80020ed8L)
  public static void FUN_80020ed8() {
    //LAB_80020f30
    //LAB_80020f34
    submapFullyLoaded_800bd7b4 = false;

    final EngineStateEnum previousState = previousEngineState_800bdb88;
    if(previousState != engineState_8004dd20) {
      previousEngineState_800bdb88 = engineState_8004dd20;

      if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05) {
        _800bd7b0 = 2;
        transitioningFromCombatToSubmap_800bd7b8 = false;

        if(previousState == EngineStateEnum.TITLE_02) {
          _800bd7b0 = 9;
        }

        //LAB_80020f84
        if(previousState == EngineStateEnum.COMBAT_06) {
          _800bd7b0 = -4;
          transitioningFromCombatToSubmap_800bd7b8 = true;
        }

        //LAB_80020fa4
        if(previousState == EngineStateEnum.WORLD_MAP_08) {
          _800bd7b0 = 3;
        }
      }
    }

    //LAB_80020fb4
    //LAB_80020fb8
    if(previousEngineState_800bdb88 == EngineStateEnum.TITLE_02) {
      _800bd7ac = true;
    }

    //LAB_80020fd0
  }

  @Method(0x800218f0L)
  public static void FUN_800218f0() {
    if(_800bd7ac) {
      _800bd7b0 = 9;
      _800bd7ac = false;
    }
  }
}
