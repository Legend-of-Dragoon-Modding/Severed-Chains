package legend.game;

import javafx.application.Platform;
import legend.core.Config;
import legend.core.MathHelper;
import legend.core.gpu.Rect4i;
import legend.core.gte.ModelPart10;
import legend.core.memory.Method;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.InputMod;
import legend.game.combat.Battle;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.modding.events.RenderEvent;
import legend.game.modding.events.characters.DivineDragoonEvent;
import legend.game.modding.events.inventory.ScriptFlags1ChangedEvent;
import legend.game.modding.events.inventory.ScriptFlags2ChangedEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.NotImplementedException;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptReadable;
import legend.game.submap.SubmapEnvState;
import legend.game.tim.Tim;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.BattleUiParts;
import legend.game.types.CContainer;
import legend.game.types.CharacterData2c;
import legend.game.types.Flags;
import legend.game.types.McqHeader;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.DISCORD;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.core.GameEngine.bootMods;
import static legend.game.Audio._800bf0cf;
import static legend.game.Audio.initSound;
import static legend.game.Audio.loadMenuSounds;
import static legend.game.Audio.startQueuedSounds;
import static legend.game.Audio.stopSound;
import static legend.game.DrgnFiles.drgnBinIndex_800bc058;
import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.game.EngineStates.FUN_80020ed8;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.EngineStates.loadQueuedOverlay;
import static legend.game.FullScreenEffects.handleFullScreenEffects;
import static legend.game.Graphics.InitGeom;
import static legend.game.Graphics.ResetGraph;
import static legend.game.Graphics.clearBlue_800babc0;
import static legend.game.Graphics.clearGreen_800bb104;
import static legend.game.Graphics.clearRed_8007a3a8;
import static legend.game.Graphics.endFrame;
import static legend.game.Graphics.orderingTableBits_1f8003c0;
import static legend.game.Graphics.orderingTableSize_1f8003c8;
import static legend.game.Graphics.resizeDisplay;
import static legend.game.Graphics.setDrawOffset;
import static legend.game.Graphics.setProjectionPlaneDistance;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.Graphics.zMax_1f8003cc;
import static legend.game.Graphics.zShift_1f8003c4;
import static legend.game.Menus.FUN_800e6d60;
import static legend.game.Menus.loadAndRenderMenus;
import static legend.game.Menus.renderUi;
import static legend.game.Models.loadModelAndAnimation;
import static legend.game.SItem.addGold;
import static legend.game.SItem.clearCharacterStats;
import static legend.game.SItem.giveEquipment;
import static legend.game.SItem.giveItem;
import static legend.game.SItem.takeEquipmentId;
import static legend.game.SItem.takeItem;
import static legend.game.Scus94491BpeSegment_8004.simpleRandSeed_8004dd44;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.shouldRestoreCameraPosition_80052c40;
import static legend.game.Scus94491BpeSegment_8005.submapCutBeforeBattle_80052c3c;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapEnvState_80052c44;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.shadowModel_800bda10;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Text.handleTextboxAndText;
import static legend.game.Text.initTextboxes;
import static legend.game.Text.renderTextboxes;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.combat.SBtld.tickAndRenderTransitionIntoBattle;
import static legend.lodmod.LodGoods.DIVINE_DRAGOON_SPIRIT;

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
    // Moving this here instead of at the end because the frame isn't actually ending when this callback completes. The render engine
    // still has a bunch of work to do (rendering the actual game) and this could resize the display buffers leading to jank. Instead
    // we can just do that stuff before we actually start rendering anything.
    endFrame();

    GPU.startFrame();

    if(currentEngineState_8004dd04 != null && currentEngineState_8004dd04.advancesTime()) {
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
  }

  public static void bindRendererEvents() {
    RENDERER.events().onKeyPress((window, key, scancode, mods, repeat) -> {
      if(mods.contains(InputMod.CTRL) && !repeat && key == InputKey.W && currentEngineState_8004dd04 instanceof final Battle battle) {
        battle.allMonstersDead();
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

    RENDERER.setRenderCallback(Scus94491BpeSegment::gameLoop);

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
    final int packedIndex = script.params_20[0].get();
    boolean set = script.params_20[1].get() != 0;

    final int shift = packedIndex & 0x1f;
    final int index = packedIndex >>> 5;

    final Flags flags;
    if(index < 8) {
      set = EVENTS.postEvent(new ScriptFlags1ChangedEvent(packedIndex, set)).set;
      flags = gameState_800babc8.scriptFlags1_13c;
    } else if(index < 16) {
      flags = gameState_800babc8.wmapFlags_15c;
    } else {
      throw new RuntimeException("Are there more flags?");
    }

    flags.set(index % 8, shift, set);

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
    final int packedIndex = script.params_20[0].get();
    final boolean set = EVENTS.postEvent(new ScriptFlags2ChangedEvent(packedIndex, script.params_20[1].get() != 0)).set;

    gameState_800babc8.scriptFlags2_bc.set(packedIndex, set);

    //LAB_800174a4
    if(gameState_800babc8.goods_19c.has(DIVINE_DRAGOON_SPIRIT)) {
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
      if(gameState_800babc8.goods_19c.has(DIVINE_DRAGOON_SPIRIT)) {
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

  @ScriptDescription("Gives the player gold")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount of gold")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "out", description = "Always 0")
  @Method(0x80024480L)
  public static FlowControl scriptGiveGold(final RunningScript<?> script) {
    script.params_20[1].set(addGold(script.params_20[0].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives the player chest contents")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "itemId", description = "The item ID (0xfb = 20G, 0xfc = 50G, 0xfd = 100G, 0xfe = 200G, 0xff = nothing)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemGiven", description = "The item ID if an item was given (0xff = unable to give, 0 = gave gold)")
  @Method(0x800244c4L)
  public static FlowControl scriptGiveChestContents(final RunningScript<?> script) {
    final int a0 = switch(script.params_20[0].get()) {
      case 0xfb -> addGold(20);
      case 0xfc -> addGold(50);
      case 0xfd -> addGold(100);
      case 0xfe -> addGold(200);
      case 0xff -> 0xff;
      default -> {
        final int itemId = script.params_20[0].get();

        if(itemId < 0xc0) {
          yield giveEquipment(REGISTRIES.equipment.getEntry(LodMod.id(LodMod.EQUIPMENT_IDS[itemId])).get()) ? 0 : 0xff;
        }
        yield giveItem(REGISTRIES.items.getEntry(LodMod.id(LodMod.ITEM_IDS[itemId - 192])).get()) ? 0 : 0xff;
      }
    };

    //LAB_80024574
    script.params_20[1].set(a0);

    //LAB_80024580
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes an item from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "itemId", description = "The item ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemTaken", description = "The item ID taken, of 0xff if none could be taken")
  @Method(0x80024590L)
  public static FlowControl scriptTakeItem(final RunningScript<?> script) {
    final int itemId = script.params_20[0].get() & 0xff;

    if(itemId < 0xc0) {
      script.params_20[1].set(takeEquipmentId(REGISTRIES.equipment.getEntry(LodMod.id(LodMod.EQUIPMENT_IDS[itemId])).get()) ? 0 : 0xff);
    } else {
      script.params_20[1].set(takeItem(REGISTRIES.items.getEntry(LodMod.id(LodMod.ITEM_IDS[itemId - 192])).get()) ? 0 : 0xff);
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Reads a value from a registry entry")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "registryId", description = "The registry to read from")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "entryId", description = "The entry to access from the registry")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "entryVar", description = "The var to read from the entry")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.ANY, name = "value", description = "The value read from the entry")
  public static FlowControl scriptReadRegistryEntryVar(final RunningScript<?> script) {
    final RegistryId registryId = script.params_20[0].getRegistryId();
    final RegistryId entryId = script.params_20[1].getRegistryId();
    final int entryVar = script.params_20[2].get();

    final Registry<?> registry = REGISTRIES.get(registryId);
    final RegistryEntry entry = registry.getEntry(entryId).get();

    if(entry instanceof final ScriptReadable readable) {
      readable.read(entryVar, script.params_20[3]);
    } else {
      throw new NotImplementedException("Registry %s entry %s is not script-readable".formatted(registryId, entryId));
    }

    return FlowControl.CONTINUE;
  }

  /**
   * Loads DRGN0 MRG @ 77382 (basic UI textures)
   * <ol start="0">
   *   <li>Game font</li>
   *   <li>Japanese font</li>
   *   <li>Japanese text</li>
   *   <li>Dialog box border</li>
   *   <li>Red downward bobbing arrow</li>
   * </ol>
   */
  @Method(0x800249b4L)
  public static void basicUiTexturesLoaded(final List<FileData> files) {
    final Rect4i[] rects = new Rect4i[28]; // image size, clut size, image size, clut size...
    System.arraycopy(rectArray28_80010770, 0, rects, 0, 28);

    rects[2] = new Rect4i(0, 0, 64, 16);
    rects[3] = new Rect4i(0, 0, 0, 0);

    final int[] indexOffsets = {0, 20, 22, 24, 26};

    //LAB_80024e88
    for(int i = 0; i < files.size(); i++) {
      final FileData data = files.get(i);

      if(data.size() != 0) {
        final Tim tim = new Tim(data);
        final int rectIndex = indexOffsets[i];

        if(i == 0 || i > 2) {
          GPU.uploadData15(rects[rectIndex], tim.getImageData());
        }

        //LAB_80024efc
        if(i == 3) {
          //LAB_80024f2c
          GPU.uploadData15(rects[indexOffsets[i] + 1], tim.getClutData());
        } else if(i < 4) {
          //LAB_80024fac
          for(int s0 = 0; s0 < 4; s0++) {
            final Rect4i rect = new Rect4i(rects[rectIndex + 1]);
            rect.x += s0 * 16;
            GPU.uploadData15(rect, tim.getClutData().slice(s0 * 0x80));
          }
          //LAB_80024f1c
        } else if(i == 4) {
          //LAB_80024f68
          GPU.uploadData15(rects[rectIndex + 1], tim.getClutData());
        }
      }
    }
  }

  @Method(0x8002504cL)
  public static void loadBasicUiTexturesAndSomethingElse() {
    loadDrgnDir(0, 6669, Scus94491BpeSegment::basicUiTexturesLoaded);

    textZ_800bdf00 = 13;
    clearCharacterStats();
    initTextboxes();
  }

  @Method(0x8002a9c0L)
  public static void resetSubmapToNewGame() {
    submapCut_80052c30 = 675; // Opening
    submapScene_80052c34 = 4;
    collidedPrimitiveIndex_80052c38 = 0;
    submapCutBeforeBattle_80052c3c = -1;
    shouldRestoreCameraPosition_80052c40 = false;
    submapEnvState_80052c44 = SubmapEnvState.CHECK_TRANSITIONS_1_2;
    bootMods(MODS.getAllModIds());
  }

  private static int randSeed = 0x24040001;

  @Method(0x8002d260L)
  public static int rand() {
    randSeed = randSeed * 0x41c6_4e6d + 0x3039;
    return randSeed >>> 16 & 0x7fff;
  }

  @Method(0x8002d270L)
  public static void srand(final int seed) {
    randSeed = seed;
  }

  @Method(0x800e5d44L)
  public static void main() {
    gameInit();
    preload();
  }

  @Method(0x800e5d64L)
  public static void gameInit() {
    ResetGraph();

    orderingTableBits_1f8003c0 = 14;
    zShift_1f8003c4 = 0;
    orderingTableSize_1f8003c8 = 0x4000;
    zMax_1f8003cc = 0x3ffe;
    GPU.updateOrderingTableSize(orderingTableSize_1f8003c8);

    setDrawOffset();

    clearRed_8007a3a8 = 0;
    clearGreen_800bb104 = 0;
    clearBlue_800babc0 = 0;

    InitGeom();
    setProjectionPlaneDistance(640);
    initSound();

    pregameLoadingStage_800bb10c = 0;
    vsyncMode_8007a3b8 = 2;
    tickCount_800bb0fc = 0;

    loadSystemFont();
    SCRIPTS.clear();
    loadShadow();
    FUN_800e6d60();
    initFmvs();
  }

  @Method(0x800e6184L)
  public static void preload() {
    drgnBinIndex_800bc058 = 1;

    loadMenuSounds();
    resizeDisplay(320, 240);
    vsyncMode_8007a3b8 = 2;

    //LAB_800e600c
    loadBasicUiTexturesAndSomethingElse();
  }

  @Method(0x800e6524L)
  public static void loadSystemFont() {
    final Tim font = new Tim(Loader.loadFile("font.tim"));

    final Rect4i imageRect = new Rect4i(832, 424, 64, 56);
    GPU.uploadData15(imageRect, font.getImageData());

    if(font.hasClut()) {
      final Rect4i clutRect = new Rect4i(832, 422, 32, 1);
      GPU.uploadData15(clutRect, font.getClutData());
    }
  }

  @Method(0x800e6998L)
  public static void loadShadow() {
    submapId_800bd808 = 0;

    new Tim(Loader.loadFile("shadow.tim")).uploadToGpu();

    //LAB_800e6af0
    final CContainer container = new CContainer("Shadow", Loader.loadFile("shadow.ctmd"));
    final TmdAnimationFile animation = new TmdAnimationFile(Loader.loadFile("shadow.anim"));

    shadowModel_800bda10.modelParts_00 = new ModelPart10[animation.modelPartCount_0c];
    Arrays.setAll(shadowModel_800bda10.modelParts_00, i -> new ModelPart10());

    loadModelAndAnimation(shadowModel_800bda10, container, animation);

    shadowModel_800bda10.coord2_14.transforms.rotate.zero();
    shadowModel_800bda10.uvAdjustments_9d = UvAdjustmentMetrics14.NONE;
    shadowModel_800bda10.shadowType_cc = 0;
  }

  @Method(0x800e6e6cL)
  public static void initFmvs() {
    _800bf0cf = 0;
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
