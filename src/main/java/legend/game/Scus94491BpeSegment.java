package legend.game;

import javafx.application.Platform;
import legend.core.Config;
import legend.core.MathHelper;
import legend.core.gpu.Rect4i;
import legend.core.memory.Method;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.InputMod;
import legend.game.combat.Battle;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.modding.events.RenderEvent;
import legend.game.modding.events.characters.DivineDragoonEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.types.BattleUiParts;
import legend.game.types.CharacterData2c;
import legend.game.types.Flags;
import legend.game.types.McqHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.DISCORD;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Audio.startQueuedSounds;
import static legend.game.Audio.stopSound;
import static legend.game.EngineStates.FUN_80020ed8;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.EngineStates.engineState_8004dd20;
import static legend.game.EngineStates.loadQueuedOverlay;
import static legend.game.FullScreenEffects.handleFullScreenEffects;
import static legend.game.Graphics.endFrame;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.Menus.loadAndRenderMenus;
import static legend.game.Menus.renderUi;
import static legend.game.Scus94491BpeSegment_8004.simpleRandSeed_8004dd44;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Textboxes.handleTextboxAndText;
import static legend.game.Textboxes.renderTextboxes;
import static legend.game.combat.SBtld.tickAndRenderTransitionIntoBattle;

public final class Scus94491BpeSegment {
  private Scus94491BpeSegment() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment.class);

  public static BattlePreloadedEntities_18cb0 battlePreloadedEntities_1f8003f4;

  public static final BattleUiParts battleUiParts = new BattleUiParts();

  public static final int[] levelUpUs_8001032c = {200, 208, 216, 208, 200, 224, 232, 240};

  public static final Rect4i[] rectArray28_80010770 = {
    new Rect4i(832, 256, 64, 160), new Rect4i(832, 480, 16, 4), new Rect4i(64, 0, 64, 16), new Rect4i(0, 0, 0, 0),
    new Rect4i(128, 0, 64, 16), new Rect4i(0, 0, 0, 0), new Rect4i(192, 0, 64, 16), new Rect4i(0, 0, 0, 0),
    new Rect4i(256, 0, 64, 16), new Rect4i(0, 0, 0, 0), new Rect4i(0, 496, 64, 16), new Rect4i(0, 0, 0, 0),
    new Rect4i(64, 496, 64, 16), new Rect4i(0, 0, 0, 0), new Rect4i(128, 496, 64, 16), new Rect4i(0, 0, 0, 0),
    new Rect4i(192, 496, 64, 16), new Rect4i(0, 0, 0, 0), new Rect4i(256, 496, 64, 16), new Rect4i(0, 0, 0, 0),
    new Rect4i(832, 256, 64, 240), new Rect4i(896, 480, 16, 4), new Rect4i(832, 256, 64, 240), new Rect4i(960, 480, 16, 4),
    new Rect4i(896, 256, 16, 46), new Rect4i(832, 484, 16, 1), new Rect4i(912, 256, 28, 14), new Rect4i(1008, 484, 16, 1),
  };

  private static final RenderEvent RENDER_EVENT = new RenderEvent();

  @Method(0x80011e1cL)
  public static void gameLoop() {
    RENDERER.events().onKeyPress((window, key, scancode, mods, repeat) -> {
      if(mods.contains(InputMod.CTRL) && !repeat && key == InputKey.W && currentEngineState_8004dd04 instanceof final Battle battle) {
        battle.endBattle();
      }

      if(mods.contains(InputMod.CTRL) && !repeat && key == InputKey.Q) {
        if(Config.getGameSpeedMultiplier() == 1) {
          Config.setGameSpeedMultiplier(Config.getLoadedGameSpeedMultiplier());
        } else {
          Config.setGameSpeedMultiplier(1);
        }
      }
    });

    RENDERER.events().onInputActionPressed((window, action, repeat) -> {
      if(currentEngineState_8004dd04 != null) {
        currentEngineState_8004dd04.inputActionPressed(action, repeat);
      }
    });

    RENDERER.events().onInputActionReleased((window, action) -> {
      if(currentEngineState_8004dd04 != null) {
        currentEngineState_8004dd04.inputActionReleased(action);
      }
    });

    RENDERER.setRenderCallback(() -> {
      // Moving this here instead of at the end because the frame isn't actually ending when this callback completes. The render engine
      // still has a bunch of work to do (rendering the actual game) and this could resize the display buffers leading to jank. Instead
      // we can just do that stuff before we actually start rendering anything.
      endFrame();

      GPU.startFrame();

      if(engineState_8004dd20.isInGame()) {
        gameState_800babc8.timestamp_a0 += vsyncMode_8007a3b8;
      }

      final int frames = Math.max(1, vsyncMode_8007a3b8);
      final int hz = 60 / frames * Config.getGameSpeedMultiplier();
      RENDERER.window().setFpsLimit(hz);
      PLATFORM.setInputTickRate(hz);

      loadQueuedOverlay();

      renderUi();

      if(currentEngineState_8004dd04 != null) {
        currentEngineState_8004dd04.tick();
      }

      EVENTS.postEvent(RENDER_EVENT);

      loadAndRenderMenus();

      final boolean scriptsTicked = SCRIPTS.tick();

      if(currentEngineState_8004dd04 != null) {
        currentEngineState_8004dd04.postScriptTick(scriptsTicked);
      }

      tickAndRenderTransitionIntoBattle();
      handleFullScreenEffects();

      // SPU stuff
      startQueuedSounds();

      // Textboxes
      handleTextboxAndText();
      renderTextboxes();

      if(currentEngineState_8004dd04 != null) {
        currentEngineState_8004dd04.overlayTick();
      }

      FUN_80020ed8();
      tickCount_800bb0fc++;

      GPU.endFrame();

      DISCORD.tick();
    });

    RENDERER.events().onClose(() -> {
      stopSound();
      AUDIO_THREAD.stop();
      Platform.exit();
    });
  }

  @Method(0x800133acL)
  public static int simpleRand() {
    final int v1;
    if((simpleRandSeed_8004dd44 & 0xfff) == 0) {
      v1 = RENDERER.getVsyncCount() * 9; // If seed is 0, seed with vblanks
    } else {
      v1 = simpleRandSeed_8004dd44;
    }

    //LAB_800133dc
    simpleRandSeed_8004dd44 = v1 * 9 + 0x3711;
    return simpleRandSeed_8004dd44 / 2 & 0xffff;
  }

  @Method(0x80013404L)
  public static float FUN_80013404(final float a0, final float a1, final int a2) {
    if(a2 == 0) {
      return 0;
    }

    //TODO what is this doing? More readable: a1 / a2 - a0 * (a2 - 1) / 2
    return (a1 * 2 + a0 * a2 * (1 - a2)) / a2 / 2;
  }

  /** ALWAYS returns a positive value even for negative angles */
  @Method(0x80013598L)
  public static short rsin(final int theta) {
    return (short)(MathHelper.sin(MathHelper.psxDegToRad(theta)) * 0x1000);
  }

  /** ALWAYS returns a positive value even for negative angles */
  @Method(0x800135b8L)
  public static short rcos(final int theta) {
    return (short)(MathHelper.cos(MathHelper.psxDegToRad(theta)) * 0x1000);
  }

  @ScriptDescription("Does nothing")
  @Method(0x8001734cL)
  public static FlowControl scriptRewindAndPause2(final RunningScript<?> script) {
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Forces disabling/enabling of indicators during scripted movement (not during normal play)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "disabled", description = "True to disable indicators")
  @Method(0x80017354L)
  public static FlowControl scriptSetIndicatorsDisabled(final RunningScript<?> script) {
    gameState_800babc8.indicatorsDisabled_4e3 = script.params_20[0].get() != 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Reads indicator status during some scripted movement (only identified instance so far is Bale boat ride)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "disabled", description = "True if indicators are disabled")
  @Method(0x80017374L)
  public static FlowControl scriptReadIndicatorsDisabled(final RunningScript<?> script) {
    script.params_20[0].set(gameState_800babc8.indicatorsDisabled_4e3 ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets or clears a bit in the flags 1 array at GameState#scriptFlags1_13c")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "flag", description = "Which flag to set")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "value", description = "True to set the flag, false to unset")
  @Method(0x80017390L)
  public static FlowControl scriptSetGlobalFlag1(final RunningScript<?> script) {
    final int shift = script.params_20[0].get() & 0x1f;
    final int index = script.params_20[0].get() >>> 5;

    final Flags flags;
    if(index < 8) {
      flags = gameState_800babc8.scriptFlags1_13c;
    } else if(index < 16) {
      flags = gameState_800babc8.wmapFlags_15c;
    } else {
      throw new RuntimeException("Are there more flags?");
    }

    flags.set(index % 8, shift, script.params_20[1].get() != 0);

    //LAB_800173f4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Reads a bit in the flags 1 array at GameState#scriptFlags1_13c")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "flag", description = "Which flag to read")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "value", description = "True the flag was set, false if unset")
  @Method(0x800173fcL)
  public static FlowControl scriptReadGlobalFlag1(final RunningScript<?> script) {
    final int value = script.params_20[0].get();

    // This is a fix for a bug in one of the game scripts - it ends up reading a flag that's massively out of bounds
    if(value == -1) {
      script.params_20[1].set(0);
      return FlowControl.CONTINUE;
    }

    script.params_20[1].set(gameState_800babc8.scriptFlags1_13c.get(value) ? 1 : 0);

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets or clears a bit in the flags 2 array at GameState#scriptFlags2_bc")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "flag", description = "Which flag to set")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "value", description = "True to set the flag, false to unset")
  @Method(0x80017440L)
  public static FlowControl scriptSetGlobalFlag2(final RunningScript<?> script) {
    gameState_800babc8.scriptFlags2_bc.set(script.params_20[0].get(), script.params_20[1].get() != 0);

    //LAB_800174a4
    if((gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
      final DivineDragoonEvent divineEvent = EVENTS.postEvent(new DivineDragoonEvent());
      if(!divineEvent.bypassOverride) {
        final CharacterData2c charData = gameState_800babc8.charData_32c[0];
        charData.dlevelXp_0e = 0x7fff;
        charData.dlevel_13 = 5;
      }
    }

    //LAB_800174d0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Reads a bit in the flags 2 array at GameState#scriptFlags2_bc")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "flag", description = "Which flag to read")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "value", description = "True the flag was set, false if unset")
  @Method(0x800174d8L)
  public static FlowControl scriptReadGlobalFlag2(final RunningScript<?> script) {
    final int val = script.params_20[0].get();

    // This is a sentinel value used by the Hoax submap controller retail patch. See Unpacker#drgn21_693_0_patcherDiscriminator for details.
    if(val == -1) {
      script.params_20[1].set(0);
      return FlowControl.CONTINUE;
    }

    script.params_20[1].set(gameState_800babc8.scriptFlags2_bc.get(val) ? 1 : 0);

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets or clears a bit in a flags array")
  @ScriptParam(direction = ScriptParam.Direction.BOTH, type = ScriptParam.Type.INT_ARRAY, name = "flags", description = "The array of flags")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "flag", description = "Which flag to set")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "value", description = "True to set the flag, false to unset")
  @Method(0x800175b4L)
  public static FlowControl scriptSetFlag(final RunningScript<?> script) {
    final int shift = script.params_20[1].get() & 0x1f;
    final int index = script.params_20[1].get() >>> 5;

    if(script.params_20[2].get() != 0) {
      script.params_20[0].array(index).or(1 << shift);
    } else {
      //LAB_800175fc
      script.params_20[0].array(index).and(~(1 << shift));
    }

    //LAB_80017614
    final DivineDragoonEvent divineEvent = EVENTS.postEvent(new DivineDragoonEvent());
    if(!divineEvent.bypassOverride) {
      if((gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
        gameState_800babc8.charData_32c[0].dlevel_13 = 5;
        gameState_800babc8.charData_32c[0].dlevelXp_0e = 0x7fff;
      }
    }

    //LAB_80017640
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Reads a bit in a flags array")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "flags", description = "The array of flags")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "flag", description = "Which flag to read")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "value", description = "True the flag was set, false if unset")
  @Method(0x80017648L)
  public static FlowControl scriptReadFlag(final RunningScript<?> script) {
    final int shift = script.params_20[1].get() & 0x1f;
    final int index = script.params_20[1].get() >>> 5;

    script.params_20[2].set((script.params_20[0].array(index).get() & 1 << shift) != 0 ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800180c0L)
  public static void loadMcq(final McqHeader mcq, final int x, final int y) {
    if((x & 0x3f) != 0 || (y & 0xff) != 0) {
      //LAB_800180e0
      throw new RuntimeException("X/Y");
    }

    //LAB_800180e8
    if(mcq.magic_00 != McqHeader.MAGIC_1 && mcq.magic_00 != McqHeader.MAGIC_2) {
      throw new RuntimeException("Invalid MCQ magic");
    }

    //LAB_80018104
    if(mcq.vramHeight_0a != 256) {
      throw new RuntimeException("Invalid MCQ height");
    }

    GPU.uploadData15(new Rect4i(x, y, mcq.vramWidth_08, mcq.vramHeight_0a), mcq.imageData);
  }

  @Method(0x80018998L)
  public static void nextLoadingStage() {
    pregameLoadingStage_800bb10c++;
  }

  @ScriptDescription("Not implemented in retail")
  @Method(0x8001c5fcL)
  public static FlowControl FUN_8001c5fc(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Not implemented in retail")
  @Method(0x8001c604L)
  public static FlowControl FUN_8001c604(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  public static String getCharacterName(final int id) {
    return switch(id) {
      case 0 -> "Dart";
      case 1 -> "Lavitz";
      case 2 -> "Shana";
      case 3 -> "Rose";
      case 4 -> "Haschel";
      case 5 -> "Albert";
      case 6 -> "Meru";
      case 7 -> "Kongol";
      case 8 -> "Miranda";
      case 9, 10 -> "Divine";
      default -> throw new IllegalArgumentException("Invalid character ID " + id);
    };
  }
}
