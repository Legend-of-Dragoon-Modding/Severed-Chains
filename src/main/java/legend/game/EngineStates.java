package legend.game;

import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.game.modding.coremod.CoreEngineStateTypes;
import legend.game.modding.events.engine.EngineStateChangeEvent;
import legend.game.unpacker.FileData;
import legend.lodmod.LodEngineStateTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.GameEngine.DISCORD;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Graphics.renderMode;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment_8004.engineStateFunctions_8004e29c;
import static legend.game.Scus94491BpeSegment_800b._800bd7ac;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.submapFullyLoaded_800bd7b4;
import static legend.game.Scus94491BpeSegment_800b.transitioningFromCombatToSubmap_800bd7b8;

public final class EngineStates {
  private EngineStates() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(EngineStates.class);

  public static EngineState<?> currentEngineState_8004dd04;

  /** When the overlay finishes loading, switch to this */
  public static EngineStateType<?> engineStateOnceLoaded_8004dd24;
  public static FileData engineStateData;
  /** The previous state before the file finished loading */
  public static EngineStateType<?> previousEngineState_8004dd28;
  /** The last savable state we were in, used for generating crash recovery saves */
  public static EngineStateType<?> lastSavableEngineState;

  public static EngineStateType<?> postBattleEngineState_800bc91c;

  public static EngineStateType<?> previousEngineState_800bdb88;

  @Method(0x800128c4L)
  public static void loadQueuedOverlay() {
    //LAB_800129c0
    //LAB_800129c4
    if(engineStateOnceLoaded_8004dd24 != null) {
      previousEngineState_8004dd28 = currentEngineState_8004dd04 != null ? currentEngineState_8004dd04.type : null;
      vsyncMode_8007a3b8 = 2;
      loadGameStateOverlay(engineStateOnceLoaded_8004dd24, engineStateData);
      engineStateOnceLoaded_8004dd24 = null;
      engineStateData = null;
      menuStack.reset();

      EVENTS.postEvent(new EngineStateChangeEvent(previousEngineState_8004dd28, currentEngineState_8004dd04));
      currentEngineState_8004dd04.updateDiscordRichPresence(gameState_800babc8, DISCORD.activity);
      DISCORD.updateActivity();
    }
  }

  @Method(0x80012a84L)
  public static void loadGameStateOverlay(final EngineStateType<?> engineState, @Nullable final FileData saveData) {
    LOGGER.info("Transitioning to engine state %s", engineState);

    SCRIPTS.setFramesPerTick(1);

    if(currentEngineState_8004dd04 != null && currentEngineState_8004dd04.is(engineState)) {
      return;
    }

    Obj.clearObjList(false);

    //LAB_80012ad8
    if(currentEngineState_8004dd04 != null) {
      currentEngineState_8004dd04.destroy();
    }

    currentEngineState_8004dd04 = engineState.constructor_00.get();
    currentEngineState_8004dd04.init();

    if(saveData != null) {
      currentEngineState_8004dd04.readSaveData(gameState_800babc8, saveData);
    }

    engineStateFunctions_8004e29c = currentEngineState_8004dd04.getScriptFunctions();
    renderMode = currentEngineState_8004dd04.getRenderMode();
    RENDERER.setRenderMode(currentEngineState_8004dd04.getRenderMode());
    RENDERER.updateProjections();
  }

  @Method(0x80020ed8L)
  public static void FUN_80020ed8() {
    //LAB_80020f30
    //LAB_80020f34
    submapFullyLoaded_800bd7b4 = false;

    final EngineStateType<?> previousState = previousEngineState_800bdb88;
    if(!currentEngineState_8004dd04.is(previousState)) {
      previousEngineState_800bdb88 = currentEngineState_8004dd04.type;

      if(currentEngineState_8004dd04.is(LodEngineStateTypes.SUBMAP.get())) {
        _800bd7b0 = 2;
        transitioningFromCombatToSubmap_800bd7b8 = false;

        if(previousState == CoreEngineStateTypes.TITLE.get()) {
          _800bd7b0 = 9;
        }

        //LAB_80020f84
        if(previousState == LodEngineStateTypes.BATTLE.get()) {
          _800bd7b0 = -4;
          transitioningFromCombatToSubmap_800bd7b8 = true;
        }

        //LAB_80020fa4
        if(previousState == LodEngineStateTypes.WORLD_MAP.get()) {
          _800bd7b0 = 3;
        }
      }
    }

    //LAB_80020fb4
    //LAB_80020fb8
    if(previousEngineState_800bdb88 == CoreEngineStateTypes.TITLE.get()) {
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
