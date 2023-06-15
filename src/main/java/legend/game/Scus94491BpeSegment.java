package legend.game;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import javafx.application.Application;
import javafx.application.Platform;
import legend.core.Config;
import legend.core.DebugHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.Gpu;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.GpuCommandSetMaskBit;
import legend.core.gpu.GpuCommandUntexturedQuad;
import legend.core.gpu.RECT;
import legend.core.gte.COLOUR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.core.spu.Voice;
import legend.game.combat.Bttl_800c;
import legend.game.combat.Bttl_800d;
import legend.game.combat.Bttl_800e;
import legend.game.combat.Bttl_800f;
import legend.game.combat.SBtld;
import legend.game.combat.SEffe;
import legend.game.combat.bobj.BattleObject27c;
import legend.game.combat.bobj.MonsterBattleObject;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.combat.environment.StageData10;
import legend.game.debugger.Debugger;
import legend.game.input.Input;
import legend.game.inventory.WhichMenu;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.coremod.config.RenderScaleConfigEntry;
import legend.game.scripting.FlowControl;
import legend.game.scripting.Param;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptState;
import legend.game.sound.EncounterSoundEffects10;
import legend.game.sound.PlayableSound0c;
import legend.game.sound.QueuedSound28;
import legend.game.sound.SoundFile;
import legend.game.sound.SoundFileIndices;
import legend.game.sound.SpuStruct08;
import legend.game.sound.Sshd;
import legend.game.sound.Sssq;
import legend.game.title.Ttle;
import legend.game.types.CharacterData2c;
import legend.game.types.DeferredReallocOrFree0c;
import legend.game.types.FileEntry08;
import legend.game.types.FileLoadedCallback;
import legend.game.types.LoadingOverlay;
import legend.game.types.McqHeader;
import legend.game.types.MoonMusic08;
import legend.game.types.SubmapMusic08;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.core.GameEngine.SEQUENCER;
import static legend.core.GameEngine.SPU;
import static legend.game.SMap.FUN_800e5934;
import static legend.game.SMap.chapterTitleCardMrg_800c6710;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020ed8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a058;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bb38;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bda4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c178;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c184;
import static legend.game.Scus94491BpeSegment_8002.copyPlayingSounds;
import static legend.game.Scus94491BpeSegment_8002.loadAndRenderMenus;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.renderTextboxes;
import static legend.game.Scus94491BpeSegment_8002.sssqResetStuff;
import static legend.game.Scus94491BpeSegment_8002.unloadEncounterSoundEffects;
import static legend.game.Scus94491BpeSegment_8003.GsInitGraph;
import static legend.game.Scus94491BpeSegment_8003.GsSetDrawBuffClip;
import static legend.game.Scus94491BpeSegment_8003.GsSetDrawBuffOffset;
import static legend.game.Scus94491BpeSegment_8003.GsSortClear;
import static legend.game.Scus94491BpeSegment_8003.GsSwapDispBuff;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.setDrawOffset;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d91c;
import static legend.game.Scus94491BpeSegment_8004._8004dd00;
import static legend.game.Scus94491BpeSegment_8004._8004dd0c;
import static legend.game.Scus94491BpeSegment_8004._8004dd48;
import static legend.game.Scus94491BpeSegment_8004._8004f2a8;
import static legend.game.Scus94491BpeSegment_8004._8004f5d4;
import static legend.game.Scus94491BpeSegment_8004._8004f658;
import static legend.game.Scus94491BpeSegment_8004._8004f6e4;
import static legend.game.Scus94491BpeSegment_8004._8004f6e8;
import static legend.game.Scus94491BpeSegment_8004._8004f6ec;
import static legend.game.Scus94491BpeSegment_8004._8004fa98;
import static legend.game.Scus94491BpeSegment_8004._8004fb00;
import static legend.game.Scus94491BpeSegment_8004.changeSequenceVolumeOverTime;
import static legend.game.Scus94491BpeSegment_8004.currentlyLoadingFileEntry_8004dd04;
import static legend.game.Scus94491BpeSegment_8004.freeSequence;
import static legend.game.Scus94491BpeSegment_8004.gameStateCallbacks_8004dbc0;
import static legend.game.Scus94491BpeSegment_8004.getSequenceFlags;
import static legend.game.Scus94491BpeSegment_8004.height_8004dd34;
import static legend.game.Scus94491BpeSegment_8004.initSpu;
import static legend.game.Scus94491BpeSegment_8004.loadSshdAndSoundbank;
import static legend.game.Scus94491BpeSegment_8004.loadSssq;
import static legend.game.Scus94491BpeSegment_8004.loadedOverlayIndex_8004dd10;
import static legend.game.Scus94491BpeSegment_8004.loadingGameStateOverlay_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.loadingOverlay_8004dd1e;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndexOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.moonMusic_8004ff10;
import static legend.game.Scus94491BpeSegment_8004.overlaysLoadedCount_8004dd1c;
import static legend.game.Scus94491BpeSegment_8004.overlays_8004db88;
import static legend.game.Scus94491BpeSegment_8004.preloadingAudioAssets_8004ddcc;
import static legend.game.Scus94491BpeSegment_8004.previousMainCallbackIndex_8004dd28;
import static legend.game.Scus94491BpeSegment_8004.reinitOrderingTableBits_8004dd38;
import static legend.game.Scus94491BpeSegment_8004.setMainVolume;
import static legend.game.Scus94491BpeSegment_8004.setMaxSounds;
import static legend.game.Scus94491BpeSegment_8004.setSoundSequenceVolume;
import static legend.game.Scus94491BpeSegment_8004.setSpuDmaCompleteCallback;
import static legend.game.Scus94491BpeSegment_8004.simpleRandSeed_8004dd44;
import static legend.game.Scus94491BpeSegment_8004.sssqFadeIn;
import static legend.game.Scus94491BpeSegment_8004.sssqFadeOut;
import static legend.game.Scus94491BpeSegment_8004.sssqGetTempo;
import static legend.game.Scus94491BpeSegment_8004.sssqSetReverbType;
import static legend.game.Scus94491BpeSegment_8004.sssqSetReverbVolume;
import static legend.game.Scus94491BpeSegment_8004.sssqSetTempo;
import static legend.game.Scus94491BpeSegment_8004.sssqUnloadPlayableSound;
import static legend.game.Scus94491BpeSegment_8004.startMusicSequence;
import static legend.game.Scus94491BpeSegment_8004.startPitchShiftedSound;
import static legend.game.Scus94491BpeSegment_8004.startRegularSound;
import static legend.game.Scus94491BpeSegment_8004.startSequenceAndChangeVolumeOverTime;
import static legend.game.Scus94491BpeSegment_8004.stopMusicSequence;
import static legend.game.Scus94491BpeSegment_8004.stopSoundSequence;
import static legend.game.Scus94491BpeSegment_8004.swapDisplayBuffer_8004dd40;
import static legend.game.Scus94491BpeSegment_8004.syncFrame_8004dd3c;
import static legend.game.Scus94491BpeSegment_8004.width_8004dd34;
import static legend.game.Scus94491BpeSegment_8005.charSlotSpuOffsets_80050190;
import static legend.game.Scus94491BpeSegment_8005.characterSoundFileIndices_800500f8;
import static legend.game.Scus94491BpeSegment_8005.combatMusicFileIndices_800501bc;
import static legend.game.Scus94491BpeSegment_8005.combatSoundEffectsTypes_8005019c;
import static legend.game.Scus94491BpeSegment_8005.deferredReallocOrFree_8005a1e0;
import static legend.game.Scus94491BpeSegment_8005.heapHead_8005a2a0;
import static legend.game.Scus94491BpeSegment_8005.heapTail_8005a2a4;
import static legend.game.Scus94491BpeSegment_8005.loadingOverlay_8005a2a8;
import static legend.game.Scus94491BpeSegment_8005.monsterSoundFileIndices_800500e8;
import static legend.game.Scus94491BpeSegment_8005.sin_cos_80054d0c;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapMusic_80050068;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.clearRed_8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.joypadRepeat_8007a3a0;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc94c;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b._800bc9a8;
import static legend.game.Scus94491BpeSegment_800b._800bca68;
import static legend.game.Scus94491BpeSegment_800b._800bca6c;
import static legend.game.Scus94491BpeSegment_800b._800bd0f0;
import static legend.game.Scus94491BpeSegment_800b._800bd0fc;
import static legend.game.Scus94491BpeSegment_800b._800bd6f8;
import static legend.game.Scus94491BpeSegment_800b._800bd700;
import static legend.game.Scus94491BpeSegment_800b._800bd704;
import static legend.game.Scus94491BpeSegment_800b._800bd708;
import static legend.game.Scus94491BpeSegment_800b._800bd70c;
import static legend.game.Scus94491BpeSegment_800b._800bd710;
import static legend.game.Scus94491BpeSegment_800b._800bd714;
import static legend.game.Scus94491BpeSegment_800b._800bd740;
import static legend.game.Scus94491BpeSegment_800b._800bd774;
import static legend.game.Scus94491BpeSegment_800b.clearBlue_800babc0;
import static legend.game.Scus94491BpeSegment_800b.clearGreen_800bb104;
import static legend.game.Scus94491BpeSegment_800b.currentSequenceData_800bd0f8;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.encounterSoundEffects_800bd610;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.input_800bee90;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.melbuMusicLoaded_800bd781;
import static legend.game.Scus94491BpeSegment_800b.melbuSoundsLoaded_800bd780;
import static legend.game.Scus94491BpeSegment_800b.musicLoaded_800bd782;
import static legend.game.Scus94491BpeSegment_800b.playingSoundsBackup_800bca78;
import static legend.game.Scus94491BpeSegment_800b.postBattleAction_800bc974;
import static legend.game.Scus94491BpeSegment_800b.postCombatMainCallbackIndex_800bc91c;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.queuedSounds_800bd110;
import static legend.game.Scus94491BpeSegment_800b.repeat_800bee98;
import static legend.game.Scus94491BpeSegment_800b.savedGameSelected_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.scriptEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.sequenceVolume_800bd108;
import static legend.game.Scus94491BpeSegment_800b.soundFiles_800bcf80;
import static legend.game.Scus94491BpeSegment_800b.sssqTempoScale_800bd100;
import static legend.game.Scus94491BpeSegment_800b.sssqTempo_800bd104;
import static legend.game.Scus94491BpeSegment_800b.submapIndex_800bd808;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.sequenceData_800c4ac8;
import static legend.game.combat.Bttl_800c.FUN_800c882c;
import static legend.game.combat.Bttl_800c.FUN_800c8cf0;
import static legend.game.combat.Bttl_800c.FUN_800c90b0;
import static legend.game.combat.Bttl_800c.cacheLivingBobjs;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.monsterCount_800c6768;
import static legend.game.combat.Bttl_800d.FUN_800d8f10;
import static legend.game.combat.SBtld.stageData_80109a98;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_EQUAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F12;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL;

public final class Scus94491BpeSegment {
  private Scus94491BpeSegment() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment.class);
  private static final Marker MALLOC_MARKER = MarkerManager.getMarker("MALLOC");

  public static final IntRef orderingTableBits_1f8003c0 = MEMORY.ref(4, 0x1f8003c0L, IntRef::new);
  public static final IntRef zShift_1f8003c4 = MEMORY.ref(4, 0x1f8003c4L, IntRef::new);
  public static final IntRef orderingTableSize_1f8003c8 = MEMORY.ref(4, 0x1f8003c8L, IntRef::new);
  public static final IntRef zMax_1f8003cc = MEMORY.ref(4, 0x1f8003ccL, IntRef::new);
  public static int zMin;

  public static final ShortRef centreScreenX_1f8003dc = MEMORY.ref(2, 0x1f8003dcL, ShortRef::new);
  public static final ShortRef centreScreenY_1f8003de = MEMORY.ref(2, 0x1f8003deL, ShortRef::new);
  public static final IntRef displayWidth_1f8003e0 = MEMORY.ref(4, 0x1f8003e0L, IntRef::new);
  public static final IntRef displayHeight_1f8003e4 = MEMORY.ref(4, 0x1f8003e4L, IntRef::new);
  public static final IntRef zOffset_1f8003e8 = MEMORY.ref(4, 0x1f8003e8L, IntRef::new);
  public static final UnsignedShortRef tmdGp0Tpage_1f8003ec = MEMORY.ref(2, 0x1f8003ecL, UnsignedShortRef::new);
  public static final UnsignedShortRef tmdGp0CommandId_1f8003ee = MEMORY.ref(2, 0x1f8003eeL, UnsignedShortRef::new);

  public static BattlePreloadedEntities_18cb0 battlePreloadedEntities_1f8003f4;
  public static final IntRef projectionPlaneDistance_1f8003f8 = MEMORY.ref(4, 0x1f8003f8L, IntRef::new);
  public static final Value _1f8003fc = MEMORY.ref(4, 0x1f8003fcL);

  public static final Value _80010000 = MEMORY.ref(4, 0x80010000L);
  public static final Value _80010004 = MEMORY.ref(4, 0x80010004L);

  public static final Value _80010320 = MEMORY.ref(4, 0x80010320L);

  public static final COLOUR colour_80010328 = MEMORY.ref(1, 0x80010328L, COLOUR::new);
  public static final Value _8001032c = MEMORY.ref(1, 0x8001032cL);
  public static final Value _80010334 = MEMORY.ref(1, 0x80010334L);

  public static final Value extendedTmd_800103d0 = MEMORY.ref(4, 0x800103d0L);
  public static final Value tmdAnimFile_8001051c = MEMORY.ref(4, 0x8001051cL);
  /** TIM */
  public static final Value _80010544 = MEMORY.ref(4, 0x80010544L);

  public static final Value ovalBlobTimHeader_80010548 = MEMORY.ref(4, 0x80010548L);

  public static final ArrayRef<RECT> rectArray28_80010770 = MEMORY.ref(4, 0x80010770L, ArrayRef.of(RECT.class, 28, 8, RECT::new));

  public static final Value _80010868 = MEMORY.ref(4, 0x80010868L);

  /** TODO 0x60-byte struct */
  public static final Value _800108b0 = MEMORY.ref(4, 0x800108b0L);

  public static final Value heap_8011e210 = MEMORY.ref(4, 0x8011e210L);

  public static boolean[] scriptLog = new boolean[0x48];

  public static final Int2ObjectMap<Function<RunningScript<?>, String>> scriptFunctionDescriptions = new Int2ObjectOpenHashMap<>();

  static {
    scriptFunctionDescriptions.put(0, r -> "pause;");
    scriptFunctionDescriptions.put(1, r -> "rewind;");
    scriptFunctionDescriptions.put(2, r -> {
      final int waitFrames = r.params_20[0].get();

      if(waitFrames != 0) {
        return "wait %d (p0) frames;".formatted(waitFrames);
      } else {
        return "wait complete - continue;";
      }
    });
    scriptFunctionDescriptions.put(3, r -> {
      final int operandA = r.params_20[0].get();
      final int operandB = r.params_20[1].get();
      final int op = r.opParam_18;

      return (switch(op) {
        case 0 -> "if 0x%x (p0) <= 0x%x (p1)? %s;";
        case 1 -> "if 0x%x (p0) < 0x%x (p1)? %s;";
        case 2 -> "if 0x%x (p0) == 0x%x (p1)? %s;";
        case 3 -> "if 0x%x (p0) != 0x%x (p1)? %s;";
        case 4 -> "if 0x%x (p0) > 0x%x (p1)? %s;";
        case 5 -> "if 0x%x (p0) >= 0x%x (p1)? %s;";
        case 6 -> "if 0x%x (p0) & 0x%x (p1)? %s;";
        case 7 -> "if 0x%x (p0) !& 0x%x (p1)? %s;";
        default -> "illegal cmp 3";
      }).formatted(operandA, operandB, r.scriptState_04.scriptCompare(operandA, operandB, op) ? "yes - continue" : "no - rewind");
    });
    scriptFunctionDescriptions.put(4, r -> {
      final int operandB = r.params_20[0].get();
      final int op = r.opParam_18;

      return (switch(op) {
        case 0 -> "if 0 <= 0x%x (p0)? %s;";
        case 1 -> "if 0 < 0x%x (p0)? %s;";
        case 2 -> "if 0 == 0x%x (p0)? %s;";
        case 3 -> "if 0 != 0x%x (p0)? %s;";
        case 4 -> "if 0 > 0x%x (p0)? %s;";
        case 5 -> "if 0 >= 0x%x (p0)? %s;";
        case 6 -> "if 0 & 0x%x (p0)? %s;";
        case 7 -> "if 0 !& 0x%x (p0)? %s;";
        default -> "illegal cmp 4";
      }).formatted(operandB, r.scriptState_04.scriptCompare(0, operandB, op) ? "yes - continue" : "no - rewind");
    });
    scriptFunctionDescriptions.put(8, r -> "*%s (p1) = 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(9, r -> "tmp = 0x%x (p0); *%s (p1) = tmp; *%s (p0) = tmp; // Broken swap".formatted(r.params_20[0].get(), r.params_20[1], r.params_20[0]));
    scriptFunctionDescriptions.put(10, r -> "memcpy(%s (p1), %s (p2), %d (p0));".formatted(r.params_20[1], r.params_20[2], r.params_20[0].get()));
    scriptFunctionDescriptions.put(12, r -> "*%s (p0) = 0;".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(16, r -> "*%s (p1) &= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(17, r -> "*%s (p1) |= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(18, r -> "*%s (p1) ^= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(19, r -> "*%s (p2) &|= 0x%x (p0), 0x%x (p1);".formatted(r.params_20[2], r.params_20[0].get(), r.params_20[1].get()));
    scriptFunctionDescriptions.put(20, r -> "~*%s (p0);".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(21, r -> "*%s (p1) <<= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(22, r -> "*%s (p1) >>= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(24, r -> "*%s (p1) += 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(25, r -> "*%s (p1) -= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(26, r -> "*%s (p1) = 0x%x (p0) - 0x%x (p1);".formatted(r.params_20[1], r.params_20[0].get(), r.params_20[1].get()));
    scriptFunctionDescriptions.put(27, r -> "*%s (p0) ++;".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(28, r -> "*%s (p0) --;".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(29, r -> "-*%s (p0);".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(30, r -> "|*%s| (p0);".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(32, r -> "*%s (p1) *= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(33, r -> "*%s (p1) /= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(34, r -> "*%s (p1) = 0x%x (p0) / 0x%x (p1);".formatted(r.params_20[1], r.params_20[0].get(), r.params_20[1].get()));
    scriptFunctionDescriptions.put(35, r -> "*%s (p1) %%= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(36, r -> "*%s (p1) = 0x%x (p0) %% 0x%x (p1);".formatted(r.params_20[1], r.params_20[0].get(), r.params_20[1].get()));
    scriptFunctionDescriptions.put(43, scriptFunctionDescriptions.get(35));
    scriptFunctionDescriptions.put(44, scriptFunctionDescriptions.get(36));
    scriptFunctionDescriptions.put(48, r -> "*%s (p1) = sqrt(0x%x (p0));".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(50, r -> "*%s (p1) = sin(0x%x (p0));".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(51, r -> "*%s (p1) = cos(0x%x (p0));".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(52, r -> "*%s (p2) = ratan2(0x%x (p0), 0x%x (p1));".formatted(r.params_20[2], r.params_20[0].get(), r.params_20[1].get()));
    scriptFunctionDescriptions.put(56, r -> "subfunc(%d (pp));".formatted(r.opParam_18));
    scriptFunctionDescriptions.put(64, r -> "jmp %s (p0);".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(65, r -> {
      final int operandA = r.params_20[0].get();
      final int operandB = r.params_20[1].get();
      final int op = r.opParam_18;
      final Param dest = r.params_20[2];

      return (switch(op) {
        case 0 -> "if 0x%x (p0) <= 0x%x (p1)? %s;";
        case 1 -> "if 0x%x (p0) < 0x%x (p1)? %s;";
        case 2 -> "if 0x%x (p0) == 0x%x (p1)? %s;";
        case 3 -> "if 0x%x (p0) != 0x%x (p1)? %s;";
        case 4 -> "if 0x%x (p0) > 0x%x (p1)? %s;";
        case 5 -> "if 0x%x (p0) >= 0x%x (p1)? %s;";
        case 6 -> "if 0x%x (p0) & 0x%x (p1)? %s;";
        case 7 -> "if 0x%x (p0) !& 0x%x (p1)? %s;";
        default -> "illegal cmp 65";
      }).formatted(operandA, operandB, r.scriptState_04.scriptCompare(operandA, operandB, op) ? "yes - jmp %s (p2)".formatted(dest) : "no - continue");
    });
    scriptFunctionDescriptions.put(66, r -> {
      final int operandB = r.params_20[0].get();
      final int op = r.opParam_18;
      final Param dest = r.params_20[1];

      return (switch(op) {
        case 0 -> "if 0 <= 0x%x (p0)? %s;";
        case 1 -> "if 0 < 0x%x (p0)? %s;";
        case 2 -> "if 0 == 0x%x (p0)? %s;";
        case 3 -> "if 0 != 0x%x (p0)? %s;";
        case 4 -> "if 0 > 0x%x (p0)? %s;";
        case 5 -> "if 0 >= 0x%x (p0)? %s;";
        case 6 -> "if 0 & 0x%x (p0)? %s;";
        case 7 -> "if 0 !& 0x%x (p0)? %s;";
        default -> "illegal cmp 66";
      }).formatted(operandB, r.scriptState_04.scriptCompare(0, operandB, op) ? "yes - jmp %s (p1)".formatted(dest) : "no - continue");
    });
    scriptFunctionDescriptions.put(67, r -> "if(--%s (p0) != 0) jmp %s (p1)".formatted(r.params_20[0], r.params_20[1]));
    scriptFunctionDescriptions.put(72, r -> "gosub %s (p0);".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(73, r -> "return;");
    scriptFunctionDescriptions.put(74, r -> {
      final Param a = r.params_20[1];
      final Param b = r.params_20[0];
      final Param ptr = a.array(a.array(b.get()).get());
      return "gosub %s (p1[p1[p0]]);".formatted(ptr);
    });

    scriptFunctionDescriptions.put(80, r -> "deallocate; pause; rewind;");

    scriptFunctionDescriptions.put(82, r -> "deallocate children; pause; rewind;");
    scriptFunctionDescriptions.put(83, r -> "deallocate %s (p0);%s".formatted(r.params_20[0], r.scriptState_04.index == r.params_20[0].get() ? "; pause; rewind;" : ""));
  }
  private static boolean inputPulse;
  public static final Int2IntMap keyRepeat = new Int2IntOpenHashMap();

  private static boolean paused;

  @Method(0x80011e1cL)
  public static void gameLoop() {
    GPU.events().onKeyPress((window, key, scancode, mods) -> {
      // Add killswitch in case sounds get stuck on
      if(key == GLFW_KEY_DELETE) {
        for(final Voice voice : SPU.voices) {
          voice.volumeLeft.set(0);
          voice.volumeRight.set(0);
        }
      }

      if(key == GLFW_KEY_F11) {
        paused = !paused;

        if(paused) {
          LOGGER.info("Pausing");
        } else {
          LOGGER.info("Unpausing");
        }
      }

      if(key == GLFW_KEY_EQUAL) {
        if(mods == 0) {
          Config.setGameSpeedMultiplier(Config.getGameSpeedMultiplier() + 1);
        } else if((mods & GLFW_MOD_CONTROL) != 0 && gameState_800babc8 != null) {
          final RenderScaleConfigEntry config = CoreMod.RENDER_SCALE_CONFIG.get();
          final int scale = CONFIG.getConfig(config) + 1;

          if(scale <= RenderScaleConfigEntry.MAX) {
            CONFIG.setConfig(config, scale);
            GPU.rescale(scale);
          }
        }
      }

      if(key == GLFW_KEY_MINUS) {
        if(mods == 0) {
          Config.setGameSpeedMultiplier(Config.getGameSpeedMultiplier() - 1);
        } else if((mods & GLFW_MOD_CONTROL) != 0 && gameState_800babc8 != null) {
          final RenderScaleConfigEntry config = CoreMod.RENDER_SCALE_CONFIG.get();
          final int scale = CONFIG.getConfig(config) - 1;

          if(scale >= 1) {
            CONFIG.setConfig(config, scale);
            GPU.rescale(scale);
          }
        }
      }

      if(key == GLFW_KEY_F12) {
        if(!Debugger.isRunning()) {
          try {
            Platform.setImplicitExit(false);
            new Thread(() -> Application.launch(Debugger.class)).start();
          } catch(final Exception e) {
            LOGGER.info("Failed to start script debugger", e);
          }
        } else {
          Platform.runLater(Debugger::show);
        }
      }
    });

    Input.init();

    GPU.subRenderer = () -> {
      EVENTS.clearStaleRefs();

      if(paused) {
        return;
      }

      if(!soundRunning) {
        startSound();
      }

      joypadPress_8007a398.setu(press_800bee94.get());
      joypadInput_8007a39c.setu(input_800bee90.get());
      joypadRepeat_8007a3a0.setu(repeat_800bee98.get());

      if(mainCallbackIndex_8004dd20.get() > 4) {
        gameState_800babc8.timestamp_a0 += vsyncMode_8007a3b8.get();
      }

      final int frames = Math.max(1, vsyncMode_8007a3b8.get());
      GPU.window().setFpsLimit((60 / frames) * Config.getGameSpeedMultiplier());

      tickDeferredReallocOrFree();
      executeLoadersAndScripts();
      FUN_8001b410();
      FUN_80013778();

      // SPU stuff
      startQueuedSounds();

      // Textboxes? Other things?
      FUN_8002a058();
      renderTextboxes();

      FUN_80020ed8();
      tickCount_800bb0fc.incr();
      endFrame();

      press_800bee94.set(0);
      repeat_800bee98.set(0);

      if(inputPulse) {
        for(final var entry : keyRepeat.int2IntEntrySet()) {
          if(entry.getIntValue() >= 2) { //TODO adjust for frame rate
            repeat_800bee98.or(entry.getIntKey());
          }

          entry.setValue(entry.getIntValue() + 1);
        }
      }

      inputPulse = !inputPulse;
    };

    GPU.window().events.onShutdown(() -> {
      stopSound();
      SPU.stop();
      Platform.exit();
    });
  }

  private static final int SOUND_TPS = 60;
  private static final int NANOS_PER_TICK = 1_000_000_000 / SOUND_TPS;
  private static boolean soundRunning;

  private static void startSound() {
    soundRunning = true;
    new Thread(Scus94491BpeSegment::soundLoop).start();
  }

  private static void stopSound() {
    soundRunning = false;
  }

  private static void soundLoop() {
    long time = System.nanoTime();

    while(soundRunning) {
      try {
        SEQUENCER.tick();
      } catch(final Throwable t) {
        LOGGER.error("Sound thread crashed!", t);
        soundRunning = false;
      }


      long interval = System.nanoTime() - time;

      // Failsafe if we run too far behind (also applies to pausing in IDE)
      if(interval >= NANOS_PER_TICK * 3) {
        LOGGER.warn("Sequencer running behind, skipping ticks to catch up");
        interval = NANOS_PER_TICK;
        time = System.nanoTime() - interval;
      }

      final int toSleep = (int)Math.max(0, NANOS_PER_TICK - interval) / 1_000_000;
      DebugHelper.sleep(toSleep);
      time += NANOS_PER_TICK;
    }
  }

  @Method(0x80011ec8L)
  public static void executeLoadersAndScripts() {
    if(loadQueuedOverlay() != 0) {
      gameStateCallbacks_8004dbc0.get(mainCallbackIndex_8004dd20.get()).callback_00.deref().run();
      SCRIPTS.tick();
    }
  }

  @Method(0x80011f6cL)
  public static void tickDeferredReallocOrFree() {
    //LAB_80011f88
    for(int i = 0; i < 16; i++) {
      final DeferredReallocOrFree0c struct = deferredReallocOrFree_8005a1e0.get(i);

      if(struct.frames_0a.get() != 0) {
        struct.frames_0a.decr();

        if(struct.frames_0a.get() == 0) {
          if(struct.size_04.get() == 0) {
            //LAB_80011fdc
            free(struct.ptr_00.get());
          } else {
            realloc2(struct.ptr_00.get(), struct.size_04.get());
          }
        }
      }

      //LAB_80011ffc
      //LAB_80012000
    }
  }

  @Method(0x80012094L)
  public static void allocateHeap(long address, int size) {
    LOGGER.info("Allocating memory manager at %08x (0x%x bytes)", address, size);

    size = size - 0x18 & 0xffff_fffc;
    address = address + 0x3L & 0xffff_fffcL;

    MEMORY.ref(4, address).offset(0x00L).setu(0);
    MEMORY.ref(4, address).offset(0x04L).setu(0xcL);
    MEMORY.ref(2, address).offset(0x08L).setu(0x3L);
    MEMORY.ref(4, address).offset(0x0cL).setu(address);
    MEMORY.ref(4, address).offset(0x10L).setu(size);
    MEMORY.ref(2, address).offset(0x14L).setu(0);
    MEMORY.ref(4, address).offset(size).offset(0x0cL).setu(address).addu(0xcL);
    MEMORY.ref(4, address).offset(size).offset(0x10L).setu(0);
    MEMORY.ref(2, address).offset(size).offset(0x14L).setu(0x3L);

    heapHead_8005a2a0.setu(address).addu(0xcL);
    heapTail_8005a2a4.setu(address).addu(0xcL).addu(size);
  }

  private static int allocations;

  public static void dumpHeap() {
    Value entry = heapHead_8005a2a0.deref(4);
    long entryType = entry.offset(2, 0x8L).get();

    do {
      final long size = entry.offset(4, 0x4L).get();

      final String type = switch((int)entryType) {
        case 0 -> "free space";
        case 1 -> "allocated on head";
        case 2 -> "allocated on tail";
        case 3 -> "end of list";
        default -> "unknown type " + entryType;
      };

      LOGGER.error("%08x (0x%x bytes), %s", entry.getAddress() + 0xcL, size, type);

      entry = entry.offset(size);
      entryType = entry.offset(2, 0x8L).get();
    } while(entryType != 3);
  }

  @Method(0x800120f0L)
  public static long mallocHead(long size) {
    size = size + 0xfL & 0xffff_fffcL;

    long currentEntry = heapHead_8005a2a0.get();
    long entryType = MEMORY.ref(2, currentEntry).offset(0x8L).get();

    //LAB_80012120
    while(entryType != 0x3L) {
      final long spaceAvailable = MEMORY.ref(4, currentEntry).offset(0x4L).get();

      if(entryType == 0) {
        if(spaceAvailable >= size) {
          MEMORY.ref(2, currentEntry).offset(0x8L).setu(0x1L);

          if(size + 0xcL < spaceAvailable) {
            MEMORY.ref(2, currentEntry).offset(size).offset(0x8L).setu(0);
            MEMORY.ref(4, currentEntry).offset(0x4L).setu(size);
            MEMORY.ref(4, currentEntry).offset(size).offset(0x0L).setu(currentEntry);
            MEMORY.ref(4, currentEntry).offset(size).offset(0x4L).setu(spaceAvailable - size);
            MEMORY.ref(4, currentEntry).offset(spaceAvailable).setu(currentEntry + size);
          }

          allocations++;
          LOGGER.info(MALLOC_MARKER, "Allocating 0x%x bytes on head @ %08x (%d total)", size, currentEntry + 0xcL, allocations);
          return currentEntry + 0xcL;
        }
      }

      //LAB_80012170
      currentEntry += MEMORY.ref(4, currentEntry).offset(0x4L).get();
      entryType = MEMORY.ref(2, currentEntry).offset(0x8L).get();
    }

    //LAB_8001218c
    dumpHeap();
    throw new RuntimeException("Failed to allocate entry on linked list (size 0x" + Long.toHexString(size) + ')');
  }

  @Method(0x80012194L)
  public static long mallocTail(long size) {
    size = size + 0xfL & 0xffff_fffcL;

    Value currentEntry = heapTail_8005a2a4;
    Value nextEntry = heapTail_8005a2a4.deref(4);
    long entryType = nextEntry.deref(2).offset(0x8L).get();
    // Known entry types:
    // 0: empty space?
    // 2: used?
    // 3: end of list?

    //LAB_800121cc
    while(entryType != 0x3L) {
      final long spaceAvailable = nextEntry.deref(4).offset(0x4L).get();
      if(entryType == 0 && spaceAvailable >= size) {
        if(spaceAvailable > size + 0xcL) {
          currentEntry.deref(2).offset(-size).offset(0x8L).setu(0x2L); // Mark as used
          nextEntry.deref(2).offset(0x8L).setu(0); // Mark as empty space
          nextEntry.deref(4).offset(0x4L).setu(spaceAvailable - size);
          nextEntry.deref(4).offset(-size).offset(spaceAvailable).setu(nextEntry);
          currentEntry.deref(4).offset(-size).offset(0x4L).setu(size);
          currentEntry.deref(4).setu(currentEntry.get() - size);

          allocations++;
          LOGGER.info(MALLOC_MARKER, "Allocating 0x%x bytes on tail @ %08x (%d total)", size, currentEntry.get() - size + 0xcL, allocations);
          return currentEntry.get() - size + 0xcL;
        }

        //LAB_80012214
        nextEntry.deref(2).offset(0x8L).setu(0x2L); // Mark as used

        allocations++;
        LOGGER.info(MALLOC_MARKER, "Allocating 0x%x bytes on tail @ %08x (%d total)", size, nextEntry.get() + 0xcL, allocations);
        return nextEntry.get() + 0xcL;
      }

      //LAB_80012220
      currentEntry = nextEntry;
      nextEntry = nextEntry.deref(4);
      entryType = nextEntry.deref(2).offset(0x8L).get();
    }

    //LAB_8001223c
    dumpHeap();
    throw new RuntimeException("Failed to allocate entry on linked list (size 0x" + Long.toHexString(size) + ')');
  }

  @Method(0x80012444L)
  public static long realloc2(long address, int newSize) {
    address = address - 0xcL;
    newSize = newSize + 0xf & 0xffff_fffc; // Add 0xc and 4-byte align
    int blockSize = (int)MEMORY.ref(4, address).offset(0x4L).get();

    //LAB_80012494
    final int size = Math.min(newSize, blockSize);
    final long s0 = mallocTail(newSize - 0xc) - 0xcL;

    if(address < s0) {
      //LAB_800124d0
      memcpy(s0 + 0xcL, address + 0xcL, size - 0xc);
      free(address + 0xcL);

      //LAB_80012740
      return s0 + 0xcL;
    }

    //LAB_800124f8
    free(s0 + 0xcL);

    //LAB_80012508
    // Merge with previous memory block if possible
    final long previousBlock = MEMORY.ref(4, address).offset(0x0L).get();
    final long dest;
    if(MEMORY.ref(2, previousBlock).offset(0x8L).get() == 0) { // If free space
      dest = previousBlock;
      blockSize += (int)MEMORY.ref(4, dest).offset(0x4L).get();
    } else { // If not free space
      dest = address;
    }

    //LAB_80012530
    // Merge with next memory block if possible
    final long nextBlock = address + MEMORY.ref(4, address).offset(0x4L).get();
    if(MEMORY.ref(2, nextBlock).offset(0x8L).get() == 0) { // If free space
      blockSize += (int)MEMORY.ref(4, nextBlock).offset(0x4L).get();
    }

    //LAB_8001255c
    // If it doesn't fit, allocate a new block
    if(blockSize < newSize) {
      //LAB_800126d8
      final long newBlock = mallocTail(newSize - 0xc) - 0xcL;

      //LAB_80012710
      memcpy(newBlock + 0xcL, address + 0xcL, size - 0xc);
      free(address + 0xcL);

      //LAB_80012740
      return newBlock + 0xcL;
    }

    // If there's more than enough space, split the block and only use what's needed
    final int deltaSize = blockSize - newSize;
    if(deltaSize >= 0xc) {
      final long t0 = dest + deltaSize;
      memcpy(t0 + 0xcL, address + 0xcL, size - 0xc);

      //LAB_80012604
      MEMORY.ref(4, dest).offset(0x0L).offset(deltaSize).setu(dest);
      MEMORY.ref(4, dest).offset(0x4L).setu(deltaSize);
      MEMORY.ref(2, dest).offset(0x8L).setu(0);

      MEMORY.ref(4, t0).offset(0x0L).offset(newSize).setu(t0);
      MEMORY.ref(4, t0).offset(0x4L).setu(newSize);
      MEMORY.ref(2, t0).offset(0x8L).setu(1);
      return t0 + 0xcL;
    }

    //LAB_80012630
    // Otherwise, the data just fits
    memcpy(dest + 0xcL, address + 0xcL, size - 0xc);

    //LAB_800126c0
    //LAB_800126c4
    MEMORY.ref(4, dest).offset(0x0L).offset(blockSize).setu(dest);
    MEMORY.ref(4, dest).offset(0x4L).setu(blockSize);
    MEMORY.ref(2, dest).offset(0x8L).setu(2);
    return dest + 0xcL;
  }

  @Method(0x80012764L)
  public static void free(long address) {
    allocations--;
    LOGGER.info(MALLOC_MARKER, "Deallocating %08x (%d remaining)", address, allocations);

    if(allocations < 0) {
      LOGGER.error("Negative allocations!", new Throwable());
    }

    address -= 0xcL;
    long a1 = MEMORY.ref(4, address).offset(0x4L).get(); // Remaining size?
    MEMORY.ref(2, address).offset(0x8L).setu(0); // Entry type?

    if(MEMORY.ref(2, address).offset(a1).offset(0x8L).get() == 0) {
      a1 += MEMORY.ref(4, address).offset(a1).offset(0x4L).get();
    }

    //LAB_80012794
    final long v1 = MEMORY.ref(4, address).get();
    if(MEMORY.ref(2, v1).offset(0x8L).get() == 0) {
      a1 += MEMORY.ref(4, v1).offset(0x4L).get();
      address = v1;
    }

    //LAB_800127bc
    MEMORY.ref(4, address).offset(0x4L).setu(a1);
    MEMORY.ref(4, address).offset(a1).setu(address);
  }

  /**
   * Realloc or free an address after a number of frames
   *
   * @param size    Realloc size, or 0 to free
   * @param frames  The number of frames to wait
   */
  @Method(0x800127ccL)
  public static int deferReallocOrFree(final long address, final int size, int frames) {
    _8004dd00.incr();

    if(_8004dd00.get() >= 16) {
      _8004dd00.set(0);
    }

    //LAB_800127f0
    //LAB_8001281c
    for(int i = _8004dd00.get(); i < 16; i++) {
      final DeferredReallocOrFree0c v1 = deferredReallocOrFree_8005a1e0.get(i);

      if(v1.size_04.get() == 0) {
        //LAB_80012888
        if(frames <= 0) {
          frames = 1;
        }

        //LAB_80012894
        v1.ptr_00.set(address);
        v1.size_04.set(size);
        v1.frames_0a.set(frames);
        return i;
      }
    }

    //LAB_80012840
    //LAB_80012860
    for(int i = 0; i < _8004dd00.get(); i++) {
      final DeferredReallocOrFree0c v1 = deferredReallocOrFree_8005a1e0.get(i);

      if(v1.size_04.get() == 0) {
        //LAB_80012888
        if(frames <= 0) {
          frames = 1;
        }

        //LAB_80012894
        v1.ptr_00.set(address);
        v1.size_04.set(size);
        v1.frames_0a.set(frames);
        return i;
      }
    }

    //LAB_80012880
    return -1;
  }

  @Method(0x800128a8L)
  public static int getMallocSize(final long address) {
    if(address != 0) {
      return (int)MEMORY.ref(4, address).offset(-0x8L).get() - 0xc;
    }

    //LAB_800128bc
    return 0;
  }

  @Method(0x800128c4L)
  public static long loadQueuedOverlay() {
    if(!loadingOverlay_8004dd1e) {
      if(loadingOverlay_8005a2a8 != null) {
        loadingOverlay_8004dd1e = true;
        loadedOverlayIndex_8004dd10 = loadingOverlay_8005a2a8.overlayIndex();
        loadFile(overlays_8004db88.get(loadedOverlayIndex_8004dd10), _80010004.get(), Scus94491BpeSegment::overlayLoadedCallback, 0, 0x10L);
      }
    }

    //LAB_800129c0
    //LAB_800129c4
    if(mainCallbackIndexOnceLoaded_8004dd24.get() != -1) {
      if(loadingGameStateOverlay_8004dd08.get() != 0) {
        return 0;
      }

      pregameLoadingStage_800bb10c.set(0);
      previousMainCallbackIndex_8004dd28.set(mainCallbackIndex_8004dd20.get());
      mainCallbackIndex_8004dd20.set(mainCallbackIndexOnceLoaded_8004dd24.get());
      mainCallbackIndexOnceLoaded_8004dd24.set(-1);
      FUN_80019710();
      vsyncMode_8007a3b8.set(2);
      loadGameStateOverlay(mainCallbackIndex_8004dd20.get());

      if(mainCallbackIndex_8004dd20.get() == 6) { // Starting combat
        FUN_8001c4ec();
      }
    }

    //LAB_80012a34
    //LAB_80012a38
    if(loadingGameStateOverlay_8004dd08.get() == 0 || (gameStateCallbacks_8004dbc0.get(mainCallbackIndex_8004dd20.get()).uint_0c.get() & 0xff00L) != 0) {
      //LAB_80012a6c
      return 1;
    }

    //LAB_80012a70
    return 0;
  }

  @Method(0x80012a84L)
  public static long loadGameStateOverlay(final int callbackIndex) {
    LOGGER.info("Loading game state overlay %d", callbackIndex);

    final FileEntry08 entry = gameStateCallbacks_8004dbc0.get(callbackIndex).entry_04.derefNullable();

    if(entry == null || entry.getAddress() == currentlyLoadingFileEntry_8004dd04.getPointer()) {
      //LAB_80012ac0
      FUN_80012bd4(callbackIndex);
      loadingGameStateOverlay_8004dd08.setu(0);
      return 0x1L;
    }

    //LAB_80012ad8
    currentlyLoadingFileEntry_8004dd04.set(entry);
    loadingGameStateOverlay_8004dd08.setu(0x1L);
    loadFile(entry, _80010000.get(), Scus94491BpeSegment::gameStateOverlayLoadedCallback, callbackIndex, 0x10L);
    return 0;
  }

  /**
   * Supporting overlays that can be loaded at any time like S_ITEM, etc.
   *
   * @param overlayIndex <ol start="0">
   *                       <li>S_INIT (no longer used)</li>
   *                       <li>S_BTLD</li>
   *                       <li>S_ITEM</li>
   *                       <li>S_EFFE</li>
   *                       <li>S_STRM</li>
   *                     </ol>
   */
  @Method(0x80012b1cL)
  public static void loadSupportOverlay(final int overlayIndex, final Runnable overlayMethod) {
    LOGGER.info("Loading support overlay %d", overlayIndex);

    if(loadedOverlayIndex_8004dd10 == overlayIndex && !loadingOverlay_8004dd1e) {
      overlaysLoadedCount_8004dd1c++;
      overlayMethod.run();
      return;
    }

    //LAB_80012b6c
    //LAB_80012b70
    loadingOverlay_8005a2a8 = new LoadingOverlay(overlayIndex, overlayMethod);

    //LAB_80012ba4
  }

  @Method(0x80012bb4L)
  public static void decrementOverlayCount() {
    if(overlaysLoadedCount_8004dd1c > 0) {
      overlaysLoadedCount_8004dd1c--;
    }
  }

  @Method(0x80012bd4L)
  public static void FUN_80012bd4(final int callbackIndex) {
    if(_8004dd0c.get() != 0) {
      _8004dd0c.setu(0);
      return;
    }

    //LAB_80012bf0
    final long v0 = gameStateCallbacks_8004dbc0.get(callbackIndex).ptr_08.get();
    if(v0 != 0) {
      bzero(MEMORY.ref(4, v0).offset(0x0L).get(), (int)MEMORY.ref(4, v0).offset(0x4L).get());
    }
  }

  @Method(0x80012c48L)
  public static void gameStateOverlayLoadedCallback(final long address, final int fileSize, final int callbackIndex) {
    loadingGameStateOverlay_8004dd08.setu(0);
  }

  @Method(0x80012c54L)
  public static void overlayLoadedCallback(final long address, final int fileSize, final int unused) {
    loadingOverlay_8004dd1e = false;
    removeLoadedOverlayFromQueue();
  }

  @Method(0x80012c7cL)
  public static void removeLoadedOverlayFromQueue() {
    final LoadingOverlay loadingOverlay = loadingOverlay_8005a2a8;
    if(loadingOverlay_8005a2a8 != null) {
      if(loadedOverlayIndex_8004dd10 == loadingOverlay_8005a2a8.overlayIndex()) {
        if(loadingOverlay.loadedCallback() != null) {
          overlaysLoadedCount_8004dd1c++;
          loadingOverlay_8005a2a8.loadedCallback().run();
        }
      }

      loadingOverlay_8005a2a8 = null;
    }

    //LAB_80012d30
  }

  @Method(0x80012df8L)
  public static void endFrame() {
    GPU.queueCommand(3, new GpuCommandSetMaskBit(false, Gpu.DRAW_PIXELS.ALWAYS));
    GPU.queueCommand(orderingTableSize_1f8003c8.get() - 1, new GpuCommandSetMaskBit(true, Gpu.DRAW_PIXELS.ALWAYS));

    //LAB_80012e8c
    syncFrame_8004dd3c.run();
    swapDisplayBuffer_8004dd40.run();
  }

  @Method(0x80012eccL)
  public static void syncFrame() {
    // No-op
  }

  /**
   * {@link Scus94491BpeSegment_8004#syncFrame_8004dd3c} is changed to this for one frame end then switched back when the graphics mode changes
   */
  @Method(0x80012f24L)
  public static void syncFrame_reinit() {
    //LAB_80012f5c
    final int orderingTableBits = reinitOrderingTableBits_8004dd38.get();

    clearBlue_800babc0.set(0);
    clearGreen_800bb104.set(0);
    clearRed_8007a3a8.set(0);

    orderingTableBits_1f8003c0.set(orderingTableBits);
    zShift_1f8003c4.set(14 - orderingTableBits);
    orderingTableSize_1f8003c8.set(1 << orderingTableBits);
    zMax_1f8003cc.set((1 << orderingTableBits) - 2);
    GPU.updateOrderingTableSize(orderingTableSize_1f8003c8.get());

    //LAB_80013040
    GsSetDrawBuffClip();
    GsSetDrawBuffOffset();

    //LAB_80013060
    GsInitGraph(width_8004dd34, height_8004dd34);

    //LAB_80013080
    setDrawOffset();
    setProjectionPlaneDistance(320);

    syncFrame_8004dd3c = Scus94491BpeSegment::syncFrame;
    swapDisplayBuffer_8004dd40 = Scus94491BpeSegment::swapDisplayBuffer;
  }

  @Method(0x80013148L)
  public static void swapDisplayBuffer() {
    GsSwapDispBuff();
    GsSortClear(clearRed_8007a3a8.get(), clearGreen_800bb104.get(), clearBlue_800babc0.get());
  }

  @Method(0x80013200L)
  public static void resizeDisplay(final int width, final int height) {
    if(width != displayWidth_1f8003e0.get()) {
      final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
      LOGGER.info("Changing resolution to (%d, %d) from %s.%s(%s:%d)", width, height, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

      // Change the syncFrame callback to the reinitializer for a frame to reinitialize everything with the new size/flags
      syncFrame_8004dd3c = Scus94491BpeSegment::syncFrame_reinit;
      width_8004dd34 = width;
      height_8004dd34 = height;
    }
  }

  @Method(0x8001324cL)
  public static void setDepthResolution(final int orderingTableBits) {
    if(orderingTableBits_1f8003c0.get() != orderingTableBits) {
      syncFrame_8004dd3c = Scus94491BpeSegment::syncFrame_reinit;
      reinitOrderingTableBits_8004dd38.set(orderingTableBits);
    }

    //LAB_80013274
  }

  @Method(0x800133acL)
  public static int simpleRand() {
    final long v1;
    if(simpleRandSeed_8004dd44.get(0xfffL) == 0) {
      v1 = GPU.getVsyncCount() * 9; // If seed is 0, seed with vblanks
    } else {
      v1 = simpleRandSeed_8004dd44.get();
    }

    //LAB_800133dc
    simpleRandSeed_8004dd44.setu(v1 * 9 + 0x3711);
    return (int)(simpleRandSeed_8004dd44.get() / 2 & 0xffff);
  }

  @Method(0x80013404L)
  public static int FUN_80013404(final int a0, final int a1, final int a2) {
    if(a2 == 0) {
      return 0;
    }

    return (a1 * 2 + a0 * a2 * (1 - a2)) / a2 / 2;
  }

  @Method(0x80013434L)
  public static <T extends MemoryRef> void qsort(final ArrayRef<T> data, final int count, final int stride, final BiFunctionRef<T, T, Long> comparator) {
    //LAB_8001347c
    // Find the first Mersenne number >= count
    int s4 = 0;
    while(s4 * 2 < count) {
      s4 += s4 + 1;
    }

    //LAB_80013490
    //LAB_800134a4
    while(s4 > 0) {
      final int s5 = s4;
      int s3 = s4;

      //LAB_800134b8
      while(s3 < count) {
        int s2 = s3 - s5;

        //LAB_800134c4
        while(s2 >= 0) {
          final T s0 = data.get(s2);
          final T s1 = data.get(s2 + s5);

          if(comparator.run(s0, s1) <= 0) {
            break;
          }

          //LAB_80013500
          // Swap values
          for(int i = 0; i < stride; i += 4) {
            final long v1 = MEMORY.get(s0.getAddress() + i, 4);
            final long v0 = MEMORY.get(s1.getAddress() + i, 4);
            MEMORY.set(s0.getAddress() + i, 4, v0);
            MEMORY.set(s1.getAddress() + i, 4, v1);
          }

          //LAB_80013524
          s2 -= s5;
        }

        //LAB_80013530
        s3++;
      }

      //LAB_80013540
      s4 >>= 1;
    }

    //LAB_8001354c
  }

  @Method(0x80013598L)
  public static short rsin(final long theta) {
    return (short)sin_cos_80054d0c.offset(2, 0x0L).offset((theta & 0xfffL) * 4).getSigned();
  }

  @Method(0x800135b8L)
  public static short rcos(final long theta) {
    return (short)sin_cos_80054d0c.offset(2, 0x2L).offset((theta & 0xfffL) * 4).getSigned();
  }

  /**
   * 800135d8
   *
   * Copies the first n bytes of src to dest.
   *
   * @param dest Pointer to copy destination memory block
   * @param src Pointer to copy source memory block
   * @param length Number of bytes copied
   *
   * @return Pointer to destination (dest)
   */
  @Method(0x800135d8L)
  public static long memcpy(final long dest, final long src, final int length) {
    MEMORY.memcpy(dest, src, length);
    return dest;
  }

  /**
   * @param effectType 1 - fade out, 2 - fade in
   */
  @Method(0x800136dcL)
  public static void scriptStartEffect(final int effectType, final int frames) {
    //LAB_800136f4
    scriptEffect_800bb140.type_00.set(effectType);
    scriptEffect_800bb140.totalFrames_08.set(frames > 0 ? frames : 15);
    scriptEffect_800bb140.startTime_04.set(GPU.getVsyncCount());

    if(_8004dd48.offset(effectType * 2).get() == 2) {
      scriptEffect_800bb140.blue1_0c.set(0);
      scriptEffect_800bb140.green1_10.set(0);
      scriptEffect_800bb140.blue0_14.set(0);
      scriptEffect_800bb140.red1_18.set(0);
      scriptEffect_800bb140.green0_1c.set(0);
      scriptEffect_800bb140.red0_20.set(0);
    }

    scriptEffect_800bb140._24.set((int)_8004dd48.offset(effectType * 2).get());

    //LAB_80013768
  }

  /**
   * This handles the lightning flashes/darkening, the scene fade-in, etc.
   */
  @Method(0x80013778L)
  public static void FUN_80013778() {
    final int v1 = Math.min(scriptEffect_800bb140.totalFrames_08.get(), (GPU.getVsyncCount() - scriptEffect_800bb140.startTime_04.get()) / 2);

    //LAB_800137d0
    final int colour;
    if(scriptEffect_800bb140.totalFrames_08.get() == 0) {
      colour = 0;
    } else {
      final int a1 = scriptEffect_800bb140._24.get();
      if(a1 == 0) {
        colour = 0;
      } else if(a1 == 1) {
        //LAB_80013818
        colour = v1 * 255 / scriptEffect_800bb140.totalFrames_08.get();
        //LAB_80013808
      } else if(a1 == 2) { // a1 == 2
        //LAB_8001383c
        colour = v1 * 255 / scriptEffect_800bb140.totalFrames_08.get() ^ 0xff;

        if(colour == 0) {
          //LAB_80013874
          scriptEffect_800bb140._24.set(0);
        }
      } else {
        scriptEffect_800bb140.type_00.set(0);
        scriptEffect_800bb140._24.set(0);
        colour = 0;
      }
    }

    //LAB_80013880
    //LAB_80013884
    _800bb168.set(colour);

    if(colour != 0) {
      //LAB_800138f0
      //LAB_80013948
      switch(scriptEffect_800bb140.type_00.get()) {
        case 1, 2 -> drawFullScreenRect(colour, Translucency.B_MINUS_F);
        case 3, 4 -> drawFullScreenRect(colour, Translucency.B_PLUS_F);

        case 5 -> {
          for(int s1 = 0; s1 < 8; s1++) {
            //LAB_800138f8
            for(int s0 = 0; s0 < 6; s0++) {
              FUN_80013d78(colour - (0xcL - (s0 + s1)) * 11, s1, s0);
            }
          }
        }

        case 6 -> {
          for(int s1 = 0; s1 < 8; s1++) {
            //LAB_80013950
            for(int s0 = 0; s0 < 6; s0++) {
              FUN_80013d78(colour - (s1 + s0) * 11, s1, s0);
            }
          }
        }
      }
    }

    //caseD_0
    //LAB_80013994

    // This causes the bright flash of light from the lightning, etc.
    if(scriptEffect_800bb140.red0_20.get() != 0 || scriptEffect_800bb140.green0_1c.get() != 0 || scriptEffect_800bb140.blue0_14.get() != 0) {
      //LAB_800139c4
      GPU.queueCommand(39, new GpuCommandQuad()
        .translucent(Translucency.B_PLUS_F)
        .rgb(scriptEffect_800bb140.red0_20.get(), scriptEffect_800bb140.green0_1c.get(), scriptEffect_800bb140.blue0_14.get())
        .pos(-centreScreenX_1f8003dc.get(), -centreScreenY_1f8003de.get(), displayWidth_1f8003e0.get() + 1, displayHeight_1f8003e4.get() + 1)
      );
    }

    //LAB_80013adc

    // This causes the screen darkening from the lightning, etc.
    if(scriptEffect_800bb140.red1_18.get() != 0 || scriptEffect_800bb140.green1_10.get() != 0 || scriptEffect_800bb140.blue1_0c.get() != 0) {
      //LAB_80013b10
      GPU.queueCommand(39, new GpuCommandQuad()
        .translucent(Translucency.B_MINUS_F)
        .rgb(scriptEffect_800bb140.red1_18.get(), scriptEffect_800bb140.green1_10.get(), scriptEffect_800bb140.blue1_0c.get())
        .pos(-centreScreenX_1f8003dc.get(), -centreScreenY_1f8003de.get(), displayWidth_1f8003e0.get() + 1, displayHeight_1f8003e4.get() + 1)
      );
    }

    //LAB_80013c20
  }

  @Method(0x80013c3cL)
  public static void drawFullScreenRect(final long colour, final Translucency transMode) {
    final GpuCommandUntexturedQuad packet = new GpuCommandUntexturedQuad();
    packet.translucent(transMode);
    packet.monochrome((int)colour);
    packet.pos(-centreScreenX_1f8003dc.get(), -centreScreenY_1f8003de.get(), displayWidth_1f8003e0.get() + 1, displayHeight_1f8003e4.get() + 1);
    GPU.queueCommand(30, packet);
  }

  @Method(0x80013d78L)
  public static void FUN_80013d78(final long a0, final long a1, final long a2) {
    assert false;
  }

  @Deprecated
  @Method(0x8001524cL)
  public static <Param> long loadFile(final FileEntry08 entry, final long fileTransferDest, @Nullable final FileLoadedCallback<Param> callback, final Param callbackParam, final long flags) {
    final String name = entry.name_04.deref().get();
    final int type = FUN_800155b8(fileTransferDest, (int)flags);

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();

    // Insta-load
    final FileData data = Unpacker.loadFile(name);

    LOGGER.info("Loading file %s, size %d from %s.%s(%s:%d)", name, data.size(), frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    final long transferDest;
    if((type & 0x2) != 0) {
      transferDest = mallocTail(data.size());
    } else if((type & 0x4) != 0) {
      transferDest = mallocHead(data.size());
    } else {
      transferDest = fileTransferDest;
    }

    LOGGER.info("Loading file %s to %08x", name, transferDest);
    MEMORY.setBytes(transferDest, data.getBytes());

    switch(name) {
      case "\\OVL\\SMAP.OV_" -> MEMORY.addFunctions(SMap.class);
      case "\\OVL\\TTLE.OV_" -> MEMORY.addFunctions(Ttle.class);
      case "\\OVL\\S_ITEM.OV_" -> MEMORY.addFunctions(SItem.class);
      case "\\OVL\\WMAP.OV_" -> MEMORY.addFunctions(WMap.class);
      case "\\OVL\\BTTL.OV_" -> {
        MEMORY.addFunctions(Bttl_800c.class);
        MEMORY.addFunctions(Bttl_800d.class);
        MEMORY.addFunctions(Bttl_800e.class);
        MEMORY.addFunctions(Bttl_800f.class);
      }
      case "\\OVL\\S_BTLD.OV_" -> MEMORY.addFunctions(SBtld.class);
      case "\\OVL\\S_EFFE.OV_" -> MEMORY.addFunctions(SEffe.class);
      case "\\SUBMAP\\NEWROOT.RDT" -> { }
      default -> throw new RuntimeException("Loaded unknown file " + name);
    }

    if(callback != null) {
      LOGGER.info("Executing file callback (param %08x)", callbackParam);
      callback.onLoad(transferDest, data.size(), callbackParam);
    }

    return 0;
  }

  public static void loadFile(final String file, final Consumer<FileData> onCompletion) {
    final StackWalker.StackFrame frame = StackWalker.getInstance().walk(frames -> frames
      .skip(1)
      .findFirst())
      .get();

    LOGGER.info("Loading file %s from %s.%s(%s:%d)", file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Unpacker.loadFile(file));
  }

  public static void loadDir(final String dir, final Consumer<List<FileData>> onCompletion) {
    final StackWalker.StackFrame frame = StackWalker.getInstance().walk(frames -> frames
      .skip(1)
      .findFirst())
      .get();

    LOGGER.info("Loading dir %s from %s.%s(%s:%d)", dir, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Unpacker.loadDirectory(dir));
  }

  public static void loadDrgnFiles(int drgnBinIndex, final Consumer<List<FileData>> onCompletion, final String... files) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058.get();
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, String.join(", ", files), frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    final List<FileData> fileData = new ArrayList<>();
    for(final String file : files) {
      final String path = "SECT/DRGN%d.BIN/%s".formatted(drgnBinIndex, file);
      final FileData data = Unpacker.loadFile(path);
      fileData.add(data);
    }

    onCompletion.accept(fileData);
  }

  public static void loadDrgnFile(final int drgnBinIndex, final int file, final Consumer<FileData> onCompletion) {
    loadDrgnFile(drgnBinIndex, String.valueOf(file), onCompletion);
  }

  public static void loadDrgnFile(int drgnBinIndex, final String file, final Consumer<FileData> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058.get();
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    final String path = "SECT/DRGN%d.BIN/%s".formatted(drgnBinIndex, file);
    final FileData data = Unpacker.loadFile(path);
    onCompletion.accept(data);
  }

  public static void loadDrgnDir(int drgnBinIndex, final int directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058.get();
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %d from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Unpacker.loadDirectory("SECT/DRGN%d.BIN/%d".formatted(drgnBinIndex, directory)));
  }

  public static void loadDrgnDir(int drgnBinIndex, final String directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058.get();
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %s from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Unpacker.loadDirectory("SECT/DRGN%d.BIN/%s".formatted(drgnBinIndex, directory)));
  }

  @Deprecated
  @Method(0x80015310L)
  public static <Param> long loadDrgnBinFile(final int index, final int fileIndex, final long fileTransferDest, @Nullable final FileLoadedCallback<Param> callback, final Param callbackParam, final long flags) {
    final int drgnIndex;
    if(index < 2) {
      drgnIndex = index;
    } else {
      drgnIndex = 20 + drgnBinIndex_800bc058.get();
    }

    //LAB_80015388
    //LAB_8001538c
    final int type = FUN_800155b8(fileTransferDest, (int)flags);

    final String path = "SECT/DRGN%d.BIN/%d".formatted(drgnIndex, fileIndex);

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading %s from %s.%s(%s:%d)", path, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    // Insta-load
    final FileData data = Unpacker.loadFile(path);

    final long transferDest;
    if((type & 0x3) != 0) {
      transferDest = mallocTail(data.size());
    } else if((type & 0x4) != 0) {
      transferDest = mallocHead(data.size());
    } else {
      transferDest = fileTransferDest;
    }

    MEMORY.setBytes(transferDest, data.getBytes());

    if(callback != null) {
      LOGGER.info("Executing file callback (param %08x)", callbackParam);
      callback.onLoad(transferDest, data.size(), callbackParam);
    }

    //LAB_80015424
    return 0;
  }

  @Method(0x800155b8L)
  public static int FUN_800155b8(final long fileTransferDest, int flags) {
    flags = flags & 0xffff_ffef | 0x8;

    //LAB_800155d0
    if(fileTransferDest == 0 && (flags & 0x8000) == 0) {
      //LAB_800155ec
      if((flags & 0x6) == 0) {
        flags |= 0x2;
      }

      return flags;
    }

    //LAB_800155e4
    //LAB_800155fc
    return flags & 0xffff_fff9;
  }

  @Method(0x800156f4L)
  public static void setPreloadingAudioAssets(final boolean loading) {
    preloadingAudioAssets_8004ddcc.set(loading);
  }

  @Method(0x8001734cL)
  public static FlowControl scriptRewindAndPause2(final RunningScript<?> script) {
    return FlowControl.PAUSE_AND_REWIND;
  }

  /**
   * Forces disabling/enabling of indicators during scripted movement (not during normal play)
   */
  @Method(0x80017354L)
  public static FlowControl scriptSetIndicatorsDisabled(final RunningScript<?> script) {
    gameState_800babc8.indicatorsDisabled_4e3 = script.params_20[0].get() != 0;
    return FlowControl.CONTINUE;
  }

  /**
   * Reads indicator status during some scripted movement (only identified instance so far is Bale boat ride)
   */
  @Method(0x80017374L)
  public static FlowControl scriptReadIndicatorsDisabled(final RunningScript<?> script) {
    script.params_20[0].set(gameState_800babc8.indicatorsDisabled_4e3 ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  /**
   * <p>Sets or clears a bit in the flags 1 array at {@link Scus94491BpeSegment_800b#gameState_800babc8#scriptFlags1_13c}.</p>
   * <p>If work array element 1 is non-zero, the bit is set. If it's 0, the bit is cleared.</p>
   * <p>The lower 5 bits of work array element 0 is what bit to set (i.e. 1 << n), and the upper 3 bits is the index into the array.</p>
   */
  @Method(0x80017390L)
  public static FlowControl scriptSetGlobalFlag1(final RunningScript<?> script) {
    final int shift = script.params_20[0].get() & 0x1f;
    final int index = script.params_20[0].get() >>> 5;

    final int[] flags;
    if(index < 8) {
      flags = gameState_800babc8.scriptFlags1_13c;
    } else if(index < 16) {
      flags = gameState_800babc8._15c;
    } else {
      throw new RuntimeException("Are there more flags?");
    }

    if(script.params_20[1].get() != 0) {
      flags[index % 8] |= 0x1 << shift;
    } else {
      //LAB_800173dc
      flags[index % 8] &= ~(0x1 << shift);
    }

    //LAB_800173f4
    return FlowControl.CONTINUE;
  }

  /**
   * <p>Reads a bit in the flags 1 array at {@link Scus94491BpeSegment_800b#gameState_800babc8#scriptFlags1_13c}.</p>
   * <p>If the flag is set, a 1 is stored as the value of the work array element 1; otherwise, 0 is stored.</p>
   * <p>The lower 5 bits of work array element 0 is what bit to read (i.e. 1 << n), and the upper 3 bits is the index into the array.</p>
   */
  @Method(0x800173fcL)
  public static FlowControl scriptReadGlobalFlag1(final RunningScript<?> script) {
    final int value = script.params_20[0].get();

    // This is a fix for a bug in one of the game scripts - it ends up reading a flag that's massively out of bounds
    if(value == -1) {
      script.params_20[1].set(0);
      return FlowControl.CONTINUE;
    }

    final int shift = value & 0x1f;
    final int index = value >>> 5;

    script.params_20[1].set((gameState_800babc8.scriptFlags1_13c[index] & 0x1 << shift) != 0 ? 1 : 0);

    return FlowControl.CONTINUE;
  }

  @Method(0x80017440L)
  public static FlowControl scriptSetGlobalFlag2(final RunningScript<?> script) {
    final int shift = script.params_20[0].get() & 0x1f;
    final int index = script.params_20[0].get() >>> 5;

    if(script.params_20[1].get() != 0) {
      gameState_800babc8.scriptFlags2_bc[index] |= 0x1 << shift;
    } else {
      //LAB_8001748c
      gameState_800babc8.scriptFlags2_bc[index] &= ~(0x1 << shift);
    }

    //LAB_800174a4
    if((gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
      final CharacterData2c charData = gameState_800babc8.charData_32c[0];
      charData.dlevelXp_0e = 0x7fff;
      charData.dlevel_13 = 5;
    }

    //LAB_800174d0
    return FlowControl.CONTINUE;
  }

  /**
   * <p>Reads a bit in the flags 2 array at {@link Scus94491BpeSegment_800b#gameState_800babc8#scriptFlags2_bc}.</p>
   * <p>If the flag is set, a 1 is stored as the value of the work array element 1; otherwise, 0 is stored.</p>
   * <p>The lower 5 bits of work array element 0 is what bit to read (i.e. 1 << n), and the upper 3 bits is the index into the array.</p>
   */
  @Method(0x800174d8L)
  public static FlowControl scriptReadGlobalFlag2(final RunningScript<?> script) {
    final int val = script.params_20[0].get();

    // This is a sentinel value used by the Hoax submap controller retail patch. See Unpacker#drgn21_693_0_patcherDiscriminator for details.
    if(val == -1) {
      script.params_20[1].set(0);
      return FlowControl.CONTINUE;
    }

    final int shift = val & 0x1f;
    final int index = val >>> 5;

    script.params_20[1].set((gameState_800babc8.scriptFlags2_bc[index] & 0x1L << shift) != 0 ? 1 : 0);

    return FlowControl.CONTINUE;
  }

  @Method(0x8001751cL)
  public static FlowControl scriptStartEffect(final RunningScript<?> script) {
    scriptStartEffect(script.params_20[0].get(), Math.max(1, script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x80017564L)
  public static FlowControl scriptWaitForFilesToLoad(final RunningScript<?> script) {
    return FlowControl.PAUSE; // Don't need to do anything here, file loading is synchronous
  }

  @Method(0x80017584L)
  public static FlowControl FUN_80017584(final RunningScript<?> script) {
    FUN_8002bb38(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800175b4L)
  public static FlowControl FUN_800175b4(final RunningScript<?> script) {
    final int shift = script.params_20[1].get() & 0x1f;
    final int index = script.params_20[1].get() >>> 5;

    if(script.params_20[2].get() != 0) {
      script.params_20[0].array(index).or(1 << shift);
    } else {
      //LAB_800175fc
      script.params_20[0].array(index).and(~(1 << shift));
    }

    //LAB_80017614
    if((gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
      gameState_800babc8.charData_32c[0].dlevel_13 = 5;
      gameState_800babc8.charData_32c[0].dlevelXp_0e = 0x7fff;
    }

    //LAB_80017640
    return FlowControl.CONTINUE;
  }

  @Method(0x80017648L)
  public static FlowControl FUN_80017648(final RunningScript<?> script) {
    final int shift = script.params_20[1].get() & 0x1f;
    final int index = script.params_20[1].get() >>> 5;

    script.params_20[2].set((script.params_20[0].array(index).get() & 1 << shift) != 0 ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x80017688L)
  public static FlowControl FUN_80017688(final RunningScript<?> script) {
    FUN_8002bda4(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800176c0L)
  public static FlowControl FUN_800176c0(final RunningScript<?> script) {
    FUN_8002c178(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800176ecL)
  public static FlowControl FUN_800176ec(final RunningScript<?> script) {
    FUN_8002c184();
    return FlowControl.CONTINUE;
  }

  @Method(0x800180c0L)
  public static long loadMcq(final McqHeader mcq, final long x, final long y) {
    if((x & 0x3fL) != 0 || (y & 0xffL) != 0) {
      //LAB_800180e0
      throw new RuntimeException("Invalid MCQ");
    }

    //LAB_800180e8
    if(mcq.magic_00 != McqHeader.MAGIC_1 && mcq.magic_00 != McqHeader.MAGIC_2) {
      throw new RuntimeException("Invalid MCQ");
    }

    //LAB_80018104
    if(mcq.vramHeight_0a != 256) {
      throw new RuntimeException("Invalid MCQ");
    }

    LoadImage(new RECT((short)x, (short)y, (short)mcq.vramWidth_08, (short)mcq.vramHeight_0a), mcq.imageData);

    //LAB_8001813c
    return 0;
  }

  @Method(0x8001814cL)
  public static void renderMcq(final McqHeader mcq, final int vramOffsetX, final int vramOffsetY, int x, int y, final int z, final int colour) {
    final int width = mcq.screenWidth_14;
    final int height = mcq.screenHeight_16;
    int clutX = mcq.clutX_0c + vramOffsetX;
    int clutY = mcq.clutY_0e + vramOffsetY;
    int u = mcq.u_10 + vramOffsetX;
    int v = mcq.v_12 + vramOffsetY;
    int vramX = u & 0x3c0;
    final int vramY = v & 0x100;
    u = u * 4 & 0xfc;

    if(mcq.magic_00 == McqHeader.MAGIC_2) {
      x += mcq.screenOffsetX_28;
      y += mcq.screenOffsetY_2a;
    }

    //LAB_800181e4
    //LAB_80018350
    //LAB_8001836c
    for(int chunkX = 0; chunkX < width; chunkX += 16) {
      //LAB_80018380
      for(int chunkY = 0; chunkY < height; chunkY += 16) {
        GPU.queueCommand(z, new GpuCommandQuad()
          .bpp(Bpp.BITS_4)
          .clut(clutX, clutY)
          .vramPos(vramX, vramY)
          .monochrome(colour)
          .pos(x + chunkX, y + chunkY, 16, 16)
          .uv(u, v)
        );

        v = v + 16 & 0xf0;

        if(v == 0) {
          u = u + 16 & 0xf0;

          if(u == 0) {
            vramX = vramX + 64;
          }
        }

        //LAB_80018434
        clutY = clutY + 1 & 0xff;

        if(clutY == 0) {
          clutX = clutX + 16;
        }

        //LAB_80018444
        clutY = clutY | vramY;
      }
    }

    //LAB_80018464
    //LAB_8001846c
  }

  @Method(0x80018508L)
  public static void FUN_80018508() {
    if(whichMenu_800bdc38 != WhichMenu.NONE_0) {
      loadAndRenderMenus();
    }

    //LAB_8001852c
    if(postCombatMainCallbackIndex_800bc91c.get() != 5 || postBattleAction_800bc974.get() == 3) {
      //LAB_80018550
      if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
        pregameLoadingStage_800bb10c.incr();
      }
    } else {
      //LAB_80018574
      final long v1 = _8004f2a8.get();

      if(v1 == 0xaL) {
        //LAB_800185a8
        loadGameStateOverlay(5);
        _8004f2a8.addu(0x1L);
      } else if(v1 == 0xbL) {
        //LAB_800185c4
        if(loadingGameStateOverlay_8004dd08.get() == 0) {
          _8004f2a8.setu(0xcL);
        }
      } else if(v1 == 0xcL) {
        //LAB_800185e0
        FUN_800e5934();

        if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
          _8004dd0c.setu(0x1L);
          _8004f2a8.setu(0);
          pregameLoadingStage_800bb10c.incr();
        }
      } else {
        //LAB_80018618
        //LAB_80018630
        _8004f2a8.addu(0x1L);
      }
    }

    //LAB_80018644
  }

  @Method(0x80018658L)
  public static void FUN_80018658() {
    FUN_800186a0();
  }

  @Method(0x800186a0L)
  public static void FUN_800186a0() {
    if(_800bc94c.get() != 0) {
      FUN_80018744();
      _8004f5d4.get(pregameLoadingStage_800bb10c.get()).deref().run();

      if(_800bc94c.get() != 0) {
        FUN_8001890c();
      }
    } else {
      //LAB_8001870c
      _8004f5d4.get(pregameLoadingStage_800bb10c.get()).deref().run();
    }

    //LAB_80018734
  }

  @Method(0x80018744L)
  public static void FUN_80018744() {
    if((_800bc960.get() & 0x400) != 0) {
      if((_800bc960.get() & 0x8) == 0 && FUN_800187cc() != 0) {
        _800bc960.or(0x8);
      }

      //LAB_80018790
      if((_800bc960.get() & 0x4) == 0 && FUN_8001886c() != 0) {
        _800bc960.or(0x4);
      }
    }

    //LAB_800187b0
    cacheLivingBobjs();
  }

  @Method(0x800187ccL)
  public static long FUN_800187cc() {
    //LAB_80018800
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(FUN_800c90b0(battleState_8006e398.charBobjs_e40[charSlot].innerStruct_00.combatantIndex_26c) == 0) {
        return 0;
      }
    }

    //LAB_80018850
    //LAB_80018854
    return 1;
  }

  @Method(0x8001886cL)
  public static long FUN_8001886c() {
    //LAB_800188a09
    for(int i = 0; i < monsterCount_800c6768.get(); i++) {
      if(FUN_800c90b0(battleState_8006e398.monsterBobjs_e50[i].innerStruct_00.combatantIndex_26c) == 0) {
        return 0;
      }
    }

    //LAB_800188f0
    //LAB_800188f4
    return 1;
  }

  @Method(0x8001890cL)
  public static void FUN_8001890c() {
    FUN_800d8f10();
    cacheLivingBobjs();
    FUN_800c8cf0();
    FUN_800c882c();
  }

  @Method(0x80018944L)
  public static void waitForFilesToLoad() {
    if(!loadingOverlay_8004dd1e && loadingGameStateOverlay_8004dd08.get() == 0) {
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_80018990
  }

  @Method(0x80018998L)
  public static void FUN_80018998() {
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800189b0L)
  public static void FUN_800189b0() {
    if(postCombatMainCallbackIndex_800bc91c.get() == 5 && postBattleAction_800bc974.get() != 3) {
      FUN_800e5934();
    }

    //LAB_800189e4
    //LAB_800189e8
    if(!loadingOverlay_8004dd1e && loadingGameStateOverlay_8004dd08.get() == 0) {
      unloadEncounterSoundEffects();
      pregameLoadingStage_800bb10c.set(0);
      vsyncMode_8007a3b8.set(2);
      mainCallbackIndexOnceLoaded_8004dd24.set(postCombatMainCallbackIndex_800bc91c.get());
    }

    //LAB_80018a4c
  }

  @Method(0x80018a5cL)
  public static void FUN_80018a5c(final int x, final int y, final int leftU, final int topV, final int rightU, final int bottomV, final long a6, @Nullable final Translucency transMode, final COLOUR colour, final long a9, final long a10) {
    final int w = Math.abs(rightU - leftU);
    final int h = Math.abs(bottomV - topV);

    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .rgb(colour.getR(), colour.getG(), colour.getB());

    if(transMode != null) {
      cmd.translucent(transMode);
    }

    //LAB_80018b38
    if((short)a9 != 0x1000 || (short)a10 != (short)a9) {
      //LAB_80018b90
      final int sp10 = x + (short)w / 2;
      final int sp12 = y + (short)h / 2;
      final int a2 = (short)w * (short)a9 >> 13;
      final int a1 = (short)h * (short)a10 >> 13;

      cmd
        .pos(0, sp10 - a2, sp12 - a1)
        .pos(1, sp10 + a2, sp12 - a1)
        .pos(2, sp10 - a2, sp12 + a1)
        .pos(3, sp10 + a2, sp12 + a1);
    } else {
      //LAB_80018c38
      cmd
        .pos(0, x, y)
        .pos(1, x + w, y)
        .pos(2, x, y + h)
        .pos(3, x + w, y + h);
    }

    //LAB_80018c60
    cmd
      .uv(0, leftU, topV)
      .uv(1, rightU, topV)
      .uv(2, leftU, bottomV)
      .uv(3, rightU, bottomV);

    final int a2 = (short)a6 / 16;
    final int a1 = (short)a6 % 16;
    final int clutY;
    final int clutX;
    if(a2 >= 4) {
      clutX = (a2 << 4) + 832;
      clutY = a1 + 304;
    } else {
      clutX = (a2 << 4) + 704;
      clutY = a1 + 496;
    }

    //LAB_80018cf0
    cmd
      .bpp(Bpp.BITS_4)
      .clut(clutX & 0x3f0, clutY)
      .vramPos(704, 256);

    GPU.queueCommand(2, cmd);
  }

  /** Some kind of intermediate rect render method **/
  @Method(0x80018d60L)
  public static void FUN_80018d60(final int displayX, final int displayY, final int originU, final int originV, final int width, final int height, final long a6, @Nullable final Translucency transMode, final COLOUR colour, final long a9) {
    FUN_80018a5c((short)displayX, (short)displayY, originU & 0xff, originV & 0xff, originU + width & 0xff, originV + height & 0xff, (short)a6, transMode, colour, (short)a9, (short)a9);
  }

  @Method(0x80018decL)
  public static void FUN_80018dec(final int x, final int y, final int u, final int v, final int width, final int height, final long a6, @Nullable final Translucency transMode, final COLOUR colour, final long a9, final long a10) {
    FUN_80018a5c(x, y, u, v, u + width, v + height, a6, transMode, colour, a9, a10);
  }

  /** TODO */
  @Method(0x80018e84L)
  public static void FUN_80018e84() {
    final long[] sp0x30 = {0x42L, 0x43L, _80010320.offset(0x0L).get(), _80010320.offset(0x4L).get()};

    //LAB_80018f04
    long s1 = _8004f658.get();
    while(s1 != 0) {
      do {
        MEMORY.ref(1, s1).offset(0x3L).subu(0x1L);

        if(MEMORY.ref(1, s1).offset(0x3L).get() != 0) {
          break;
        }

        final long v0 = MEMORY.ref(4, s1).offset(0x8L).get();

        if(v0 == 0) {
          //LAB_80018f48
          _8004f658.setu(MEMORY.ref(4, s1).offset(0xcL).get());
        } else {
          //LAB_80018f50
          MEMORY.ref(4, v0).offset(0xcL).setu(0);
        }

        //LAB_80018f54
        final long s0 = MEMORY.ref(4, s1).offset(0xcL).get();

        if(s0 != 0) {
          if(MEMORY.ref(4, s1).offset(0x8L).get() == 0) {
            MEMORY.ref(4, s0).offset(0x8L).setu(0);
            _8004f658.setu(MEMORY.ref(4, s1).offset(0xcL).get());
          } else {
            //LAB_80018f84
            MEMORY.ref(4, s1).offset(0xcL).deref(4).offset(0x8L).setu(MEMORY.ref(4, s1).offset(0x8L).get());
            MEMORY.ref(4, s1).offset(0x8L).deref(4).offset(0xcL).setu(MEMORY.ref(4, s1).offset(0xcL).get());
          }
        }

        //LAB_80018fa0
        free(MEMORY.ref(4, s1).offset(0x4L).get());
        free(s1);
        if(s0 == 0) {
          return;
        }
        s1 = s0;
      } while(true);

      //LAB_80018fd0
      long s0 = MEMORY.ref(4, s1).offset(0x4L).get();

      //LAB_80018fd4
      for(int s2 = 0; s2 < 8; s2++) {
        MEMORY.ref(1, s0).offset(0x8L).addu(0x1L);

        if(MEMORY.ref(1, s0).offset(0x8L).getSigned() >= 0) {
          final int v1 = (int)MEMORY.ref(2, s0).offset(0xcL).get() >>> 8 & 0xf;

          if(v1 == 0) {
            //LAB_80019040
            MEMORY.ref(1, s0).offset(0x9L).subu(1);

            if(MEMORY.ref(2, s0).offset(0x4L).get() != 0) {
              MEMORY.ref(2, s0).offset(0x4L).subu(0x492);
              MEMORY.ref(2, s0).offset(0x6L).subu(0x492);
            } else {
              //LAB_80019084
              MEMORY.ref(2, s0).offset(0xcL).and(0x8fff);
            }

            //LAB_80019094
            if(MEMORY.ref(1, s0).offset(0x9L).getSigned() == 0) {
              MEMORY.ref(2, s0).offset(0xcL).and(0x7fff);
              MEMORY.ref(1, s0).offset(0x8L).setu(0);
              MEMORY.ref(2, s0).offset(0xcL).and(0xf0ff).oru(((MEMORY.ref(2, s0).offset(0xcL).get() >>> 8 & 0xf) + 1 & 0xf) << 8);
            }
          } else if(v1 == 1) {
            //LAB_800190d4
            final int a0 = (int)MEMORY.ref(2, s0).offset(0xcL).get() >>> 15;
            MEMORY.ref(2, s0).offset(0xcL).and(0x7fff).oru((a0 + 1 & 0xff) << 15);
            MEMORY.ref(1, s0).offset(0xcL).setu(sp0x30[a0]);

            if(MEMORY.ref(1, s0).offset(0x8L).getSigned() == 10) {
              MEMORY.ref(1, s0).offset(0x9L).setu(simpleRand() % 10 + 2);
              MEMORY.ref(2, s0).offset(0xcL).and(0xf0ff).oru(((MEMORY.ref(2, s0).offset(0xcL).get() >>> 8 & 0xf) + 1 & 0xf) << 8);
            }
            //LAB_80019028
          } else if(v1 == 2) {
            //LAB_80019164
            MEMORY.ref(1, s0).offset(0x9L).subu(1);

            if(MEMORY.ref(1, s0).offset(0x9L).get() == 0) {
              //LAB_80019180
              MEMORY.ref(1, s0).offset(0x9L).setu(8);
              MEMORY.ref(2, s0).offset(0xcL).and(0xf0ff).oru(((MEMORY.ref(2, s0).offset(0xcL).get() >>> 8 & 0xf) + 1 & 0xf) << 8);
            }
          } else if(v1 == 3) {
            //LAB_800191b8
            MEMORY.ref(1, s0).offset(0x9L).subu(1);

            if((MEMORY.ref(1, s0).offset(0x9L).get() & 0x1) != 0) {
              MEMORY.ref(2, s0).offset(0x2L).addu(1);
            }

            //LAB_800191e4
            MEMORY.ref(2, s0).offset(0x6L).subu(0x200);

            if(MEMORY.ref(1, s0).offset(0x9L).getSigned() == 0) {
              MEMORY.ref(1, s0).offset(0x8L).setu(-100);
            }
          }

          //LAB_80019208
          FUN_80018a5c(
            (int)MEMORY.ref(2, s0).offset(0x0L).getSigned(),
            (int)MEMORY.ref(2, s0).offset(0x2L).getSigned(),
            (int)MEMORY.ref(1, s0).offset(0xbL).get(),
            64,
            (int)MEMORY.ref(1, s0).offset(0xbL).get() + 7,
            79,
            MEMORY.ref(1, s0).offset(0xcL).get(),
            Translucency.of(((int)MEMORY.ref(2, s0).offset(0xcL).get() >>> 12 & 0x7) - 1),
            colour_80010328,
            MEMORY.ref(2, s0).offset(0x4L).get() + 0x1000,
            MEMORY.ref(2, s0).offset(0x6L).get() + 0x1000
          );
        }

        //LAB_80019294
        s0 = s0 + 0xeL;
      }

      s1 = MEMORY.ref(4, s1).offset(0xcL).get();
    }

    //LAB_800192b4
  }

  /** TODO */
  @Method(0x800192d8L)
  public static void FUN_800192d8(long a0, final long a1) {
    final long s0 = mallocTail(0x10L);
    MEMORY.ref(1, s0).offset(0x3L).setu(0x34L);
    MEMORY.ref(4, s0).offset(0x4L).setu(mallocTail(0x70L));
    MEMORY.ref(4, s0).offset(0x8L).setu(0);
    MEMORY.ref(4, s0).offset(0xcL).setu(_8004f658.get());
    MEMORY.ref(1, s0).offset(0x0L).setu(0x61L);
    MEMORY.ref(1, s0).offset(0x1L).setu(0x6cL);
    MEMORY.ref(1, s0).offset(0x2L).setu(0x64L);

    final long v0 = MEMORY.ref(4, s0).offset(0xcL).get();
    if(v0 != 0) {
      MEMORY.ref(4, v0).offset(0x8L).setu(s0);
    }

    //LAB_800193b4
    _8004f658.setu(s0);

    //LAB_800193dc
    long v1 = MEMORY.ref(4, s0).offset(0x4L).get();
    for(int i = 0; i < 8; i++) {
      MEMORY.ref(2, v1).offset(0x0L).setu(a0 - 0x6L);
      MEMORY.ref(2, v1).offset(0x2L).setu(a1 - 0x10L);
      MEMORY.ref(2, v1).offset(0x4L).setu(0x1ffeL);
      MEMORY.ref(2, v1).offset(0x6L).setu(0x1ffeL);
      MEMORY.ref(1, v1).offset(0x8L).setu(~i);
      MEMORY.ref(1, v1).offset(0x9L).setu(0x14L - i);
      MEMORY.ref(1, v1).offset(0xbL).setu(_8001032c.offset(i).get());
      MEMORY.ref(2, v1).offset(0xcL).and(0xf0ffL);
      MEMORY.ref(1, v1).offset(0xcL).setu(0x4aL);
      MEMORY.ref(2, v1).offset(0xcL).oru(0x2000L).and(0x2fffL);
      a0 = a0 + _80010334.offset(i).get();
      v1 = v1 + 0xeL;
    }
  }

  /** TODO */
  @Method(0x80019470L)
  public static void FUN_80019470() {
    long s0 = _8004f658.get();

    //LAB_80019494
    while(s0 != 0) {
      final long s1 = MEMORY.ref(4, s0).offset(0xcL).get();
      free(MEMORY.ref(4, s0).offset(0x4L).get());
      free(s0);
      s0 = s1;
    }

    _8004f658.setu(0);

    //LAB_800194c8
  }

  @Method(0x80019500L)
  public static void initSound() {
    initSpu();
    sssqFadeIn(0, 0);
    setMaxSounds(8);
    sssqSetReverbType(3);
    sssqSetReverbVolume(0x30, 0x30);

    for(int i = 0; i < 13; i++) {
      unuseSoundFile(i);
    }

    FUN_8001aa64();
    FUN_8001aa78();
    FUN_8001aa90();

    queuedSounds_800bd110.clear();

    for(int i = 0; i < 13; i++) {
      soundFiles_800bcf80[i].used_00 = false;
    }

    encounterSoundEffects_800bd610._00 = 0;

    sssqTempoScale_800bd100.set(0x100);
    melbuSoundsLoaded_800bd780.set(false);
    melbuMusicLoaded_800bd781.set(false);
  }

  @Method(0x80019610L)
  public static void reinitSound() {
    setMainVolume(0, 0);
    setMaxSounds(8);
    sssqSetReverbType(3);
    sssqSetReverbVolume(0x30, 0x30);

    for(int i = 0; i < 13; i++) {
      unuseSoundFile(i);
    }

    FUN_8001aa64();
    FUN_8001aa78();
    FUN_8001aa90();

    queuedSounds_800bd110.clear();

    for(int i = 0; i < 13; i++) {
      soundFiles_800bcf80[i].used_00 = false;
    }

    encounterSoundEffects_800bd610._00 = 0;

    sssqTempoScale_800bd100.set(0x100);
    melbuSoundsLoaded_800bd780.set(false);
    melbuMusicLoaded_800bd781.set(false);
  }

  @Method(0x80019710L)
  public static void FUN_80019710() {
    if(mainCallbackIndex_8004dd20.get() != 5 && previousMainCallbackIndex_8004dd28.get() == 5) {
      sssqResetStuff();
      melbuSoundsLoaded_800bd780.set(false);
    }

    //LAB_8001978c
    //LAB_80019790
    if(mainCallbackIndex_8004dd20.get() != 6 && previousMainCallbackIndex_8004dd28.get() == 6) {
      sssqResetStuff();

      //LAB_800197c0
      if(melbuSoundsLoaded_800bd780.get()) {
        melbuSoundsLoaded_800bd780.set(false);
      }
    }

    //LAB_80019824
    //LAB_80019828
    switch(mainCallbackIndex_8004dd20.get()) {
      case 2 -> { // ?
        setMainVolume(0x7f, 0x7f);
        sssqResetStuff();
        FUN_8001aa90();

        //LAB_80019a00
        if(drgnBinIndex_800bc058.get() == 1) {
          // Load main menu background music
          loadMusicPackage(1, 0);
        } else {
          loadMusicPackage(98, 0);
        }
      }

      case 5 -> { // SMAP
        sssqResetStuff();

        if(!melbuSoundsLoaded_800bd780.get()) {
          //LAB_80019978
          melbuSoundsLoaded_800bd780.set(true);
        }
      }

      case 6 -> { // Combat
        sssqResetStuff();

        //LAB_800198e8
        if(!melbuSoundsLoaded_800bd780.get() && encounterId_800bb0f8.get() == 443) { // Melbu
          //LAB_80019978
          melbuSoundsLoaded_800bd780.set(true);
        }
      }

      case 15 -> { // ?
        FUN_8004d91c(true);
        stopMusicSequence(currentSequenceData_800bd0f8, 0);
        freeSequence(currentSequenceData_800bd0f8);
        sssqUnloadPlayableSound(soundFiles_800bcf80[11].playableSound_10);
        sssqUnloadPlayableSound(soundFiles_800bcf80[0].playableSound_10);
      }

      case 12 -> { // ?
        sssqResetStuff();

        //LAB_80019a00
        loadMusicPackage(60, 0);
      }

      case 10 -> { // ?
        unloadSoundFile(0);
        sssqResetStuff();
      }

      case 4, 7, 8, 9, 11 -> // ?, game over, wmap, fmv, credits
        sssqResetStuff();
    }

    //case 3, d, e
    //LAB_80019a20
    //LAB_80019a24
    if(mainCallbackIndex_8004dd20.get() != 5) {
      submapIndex_800bd808.set(-1);
    }

    //LAB_80019a3c
  }

  /**
   * @param soundIndex 1: up/down, 2: choose menu option, 3: ...
   */
  @Method(0x80019a60L)
  public static void playSound(final int soundFileIndex, final int soundIndex, final long a2, final long a3, final short initialDelay, final short repeatDelay) {
    final SoundFile soundFile = soundFiles_800bcf80[soundFileIndex];

    if(!soundFile.used_00 || soundFile.playableSound_10 == null) {
      return;
    }

    switch(soundFileIndex) {
      case 0 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x1L) != 0) {
          return;
        }
      }

      case 8 -> {
        //LAB_80019bd4
        if((loadedDrgnFiles_800bcf78.get() & 0x2L) != 0 || mainCallbackIndex_8004dd20.get() != 5) {
          return;
        }
      }

      case 9 -> {
        //LAB_80019bd4
        if((loadedDrgnFiles_800bcf78.get() & 0x4L) != 0 || mainCallbackIndex_8004dd20.get() != 6) {
          return;
        }
      }

      case 0xa -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x20L) != 0) {
          return;
        }
      }

      case 0xc -> {
        //LAB_80019bd4
        if((loadedDrgnFiles_800bcf78.get() & 0x8000L) != 0 || mainCallbackIndex_8004dd20.get() != 8) {
          return;
        }
      }
    }

    //LAB_80019be0
    //LAB_80019c00
    //LAB_80019b54
    final QueuedSound28 queuedSound = new QueuedSound28();
    queuedSounds_800bd110.add(queuedSound);
    playSound(3, soundFile, soundIndex, queuedSound, soundFile.playableSound_10, soundFile.indices_08[soundIndex], soundFileIndex == 8 ? soundFile.ptr_0c.readUByte(soundIndex) : 0, (short)-1, (short)-1, (short)-1, repeatDelay, initialDelay, null);

    //LAB_80019c70
  }

  @Method(0x80019c80L)
  public static void FUN_80019c80(final SoundFile soundFile, final int soundIndex, final int a2) {
    //LAB_80019cc4
    for(int sequenceIndex = 0; sequenceIndex < 24; sequenceIndex++) {
      final SpuStruct08 s0 = _800bc9a8[sequenceIndex];

      if(s0.soundFile_02 == soundFile && s0.soundIndex_03 == soundIndex) {
        stopSoundSequence(sequenceData_800c4ac8[sequenceIndex], true);

        if((a2 & 0x1) == 0) {
          break;
        }
      }
    }

    //LAB_80019d0c
    //LAB_80019d1c
    queuedSounds_800bd110.removeIf(queuedSound -> queuedSound.type_00 == 4 && queuedSound.soundIndex_0c == soundIndex && queuedSound.soundFile_08 == soundFile);

    if((a2 & 0x2) != 0) {
      //LAB_80019d84
      queuedSounds_800bd110.removeIf(queuedSound -> (queuedSound.type_00 == 3 && (queuedSound.repeatDelayTotal_20 != 0 || queuedSound.initialDelay_24 != 0) || queuedSound._1c != 0) && queuedSound.soundIndex_0c == soundIndex && queuedSound.soundFile_08 == soundFile);
    }

    //LAB_80019dfc
  }

  /**
   * @param type 1 - player, 2 - monster
   */
  @Method(0x80019e24L)
  public static void playBobjSound(final int type, final ScriptState<BattleObject27c> state, final int soundIndex, final int a3, final int a4, final int initialDelay, final int repeatDelay) {
    final BattleObject27c bobj = state.innerStruct_00;

    int soundFileIndex = 0;
    if(type == 1) {
      //LAB_80019e68
      for(int charSlot = 0; charSlot < 3; charSlot++) {
        final int index = characterSoundFileIndices_800500f8.get(charSlot).get();
        if(soundFiles_800bcf80[index].charId_02 == bobj.charId_272) {
          //LAB_80019ea4
          soundFileIndex = index;
          break;
        }
      }
    } else {
      //LAB_80019f18
      //LAB_80019f30
      for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
        final int index = monsterSoundFileIndices_800500e8.get(monsterSlot).get();
        if(soundFiles_800bcf80[index].charId_02 == bobj.charId_272) {
          //LAB_80019ea4
          soundFileIndex = index;
          break;
        }

        if(monsterSlot == 3) {
          return;
        }
      }
    }

    //LAB_80019f70
    //LAB_80019f74
    //LAB_80019f7c
    //LAB_80019eac
    final SoundFile soundFile = soundFiles_800bcf80[soundFileIndex];

    // Retail bug: one of the Divine Dragon Spirit's attack scripts tries to play soundIndex 10 but there are only 10 elements in the patch/sequence file (DRGN0.1225.1.1)
    if(soundIndex < soundFile.indices_08.length) {
      final QueuedSound28 queuedSound = new QueuedSound28();
      queuedSounds_800bd110.add(queuedSound);

      playSound(type, soundFile, soundIndex, queuedSound, soundFile.playableSound_10, soundFile.indices_08[soundIndex], 0, (short)-1, (short)-1, (short)-1, (short)repeatDelay, (short)initialDelay, state);
    }

    //LAB_80019f9c
  }

  /** Same as playBobjSound, but looks up bobj by combatant index */
  @Method(0x80019facL)
  public static void playCombatantSound(final int type, final int charOrMonsterIndex, final int soundIndex, final short initialDelay, final short repeatDelay) {
    int soundFileIndex = 0;
    ScriptState<? extends BattleObject27c> state = null;

    //LAB_80019fdc
    for(int i = 0; i < monsterCount_800c6768.get(); i++) {
      final ScriptState<MonsterBattleObject> monster = battleState_8006e398.monsterBobjs_e50[i];

      if(monster.innerStruct_00.charId_272 == charOrMonsterIndex) {
        //LAB_8001a070
        state = monster;
        break;
      }
    }

    //LAB_8001a018
    if(type == 1) {
      //LAB_8001a034
      for(int charSlot = 0; charSlot < 3; charSlot++) {
        final int index = characterSoundFileIndices_800500f8.get(charSlot).get();

        if(soundFiles_800bcf80[index].charId_02 == charOrMonsterIndex) {
          soundFileIndex = index;
          break;
        }
      }
    } else {
      //LAB_8001a0e4
      //LAB_8001a0f4
      for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
        final int index = monsterSoundFileIndices_800500e8.get(monsterSlot).get();

        if(soundFiles_800bcf80[index].charId_02 == charOrMonsterIndex) {
          //LAB_8001a078
          soundFileIndex = index;
          break;
        }
      }
    }

    //LAB_8001a128
    //LAB_8001a12c
    //LAB_8001a134
    //LAB_8001a080
    final QueuedSound28 queuedSound = new QueuedSound28();
    queuedSounds_800bd110.add(queuedSound);

    final SoundFile soundFile = soundFiles_800bcf80[soundFileIndex];
    playSound(type, soundFile, soundIndex, queuedSound, soundFile.playableSound_10, soundFile.indices_08[soundIndex], 0, (short)-1, (short)-1, (short)-1, repeatDelay, initialDelay, state);

    //LAB_8001a154
  }

  @Method(0x8001a164L)
  public static void stopBobjSound(final long a0, final BattleObject27c bobj, final long soundIndex, final long a3) {
    //LAB_8001a1a8
    for(int sequenceIndex = 0; sequenceIndex < 24; sequenceIndex++) {
      final SpuStruct08 s0 = _800bc9a8[sequenceIndex];

      if(s0.soundIndex_03 == soundIndex && s0.bobj_04 == bobj) {
        stopSoundSequence(sequenceData_800c4ac8[sequenceIndex], true);

        if((a3 & 0x1) == 0) {
          break;
        }
      }

      //LAB_8001a1e0
    }

    //LAB_8001a1f4
    if((a3 & 0x2) != 0) {
      //LAB_8001a208
      queuedSounds_800bd110.removeIf(queuedSound -> queuedSound.type_00 != 0 && (queuedSound.repeatDelayTotal_20 != 0 || queuedSound.initialDelay_24 != 0) && queuedSound.soundIndex_0c == soundIndex && queuedSound.bobj_04 == bobj);
    }

    //LAB_8001a270
  }

  @Method(0x8001a4e8L)
  public static void startQueuedSounds() {
    //LAB_8001a50c
    final Iterator<QueuedSound28> it = queuedSounds_800bd110.iterator();

    while(it.hasNext()) {
      final QueuedSound28 queuedSound = it.next();

      if(queuedSound.type_00 != 0 && queuedSound.type_00 != 4) {
        if(queuedSound.initialDelay_24 != 0) {
          queuedSound.initialDelay_24--;

          if(queuedSound.initialDelay_24 <= 0) {
            queuedSound.initialDelay_24 = 0;

            startQueuedSound(queuedSound);

            if(queuedSound.repeatDelayTotal_20 == 0) {
              it.remove();
            }
          }
          //LAB_8001a564
        } else if(queuedSound.repeatDelayTotal_20 != 0) {
          queuedSound.repeatDelayCurrent_22--;

          if(queuedSound.repeatDelayCurrent_22 <= 0) {
            startQueuedSound(queuedSound);

            if(queuedSound.repeatDelayTotal_20 != 0) {
              queuedSound.repeatDelayCurrent_22 = queuedSound.repeatDelayTotal_20;
            }
          }
        } else {
          //LAB_8001a5b0
          startQueuedSound(queuedSound);

          if(queuedSound._1c != 0) {
            queuedSound.type_00 = 4;
          } else {
            //LAB_8001a5d0
            it.remove();
          }
        }
      }

      //LAB_8001a5d4
    }
  }

  /** Actually starts playing the sound */
  @Method(0x8001a5fcL)
  public static void startQueuedSound(final QueuedSound28 playingSound) {
    final int sequenceIndex;

    if(playingSound.pitchShiftVolRight_16 == -1 && playingSound.pitchShiftVolLeft_18 == -1 && playingSound.pitch_1a == -1) {
      sequenceIndex = startRegularSound(
        playingSound.playableSound_10,
        playingSound.patchIndex_12,
        playingSound.sequenceIndex_14
      );
    } else {
      sequenceIndex = startPitchShiftedSound(
        playingSound.playableSound_10,
        playingSound.patchIndex_12,
        playingSound.sequenceIndex_14,
        playingSound.pitchShiftVolLeft_18,
        playingSound.pitchShiftVolRight_16,
        playingSound.pitch_1a
      );
    }

    if(sequenceIndex != -1) {
      final SpuStruct08 struct08 = _800bc9a8[sequenceIndex];
      struct08.soundFile_02 = playingSound.soundFile_08;
      struct08.soundIndex_03 = playingSound.soundIndex_0c;
      struct08.bobj_04 = playingSound.bobj_04;
    }

    //LAB_8001a704
  }

  @Method(0x8001a714L)
  public static void playSound(final int type, final SoundFile soundFile, final int soundIndex, final QueuedSound28 playingSound, final PlayableSound0c playableSound, final SoundFileIndices soundFileIndices, final int a6, final short pitchShiftVolRight, final short pitchShiftVolLeft, final short pitch, final short repeatDelay, final short initialDelay, @Nullable final ScriptState<? extends BattleObject27c> state) {
    playingSound.type_00 = type;
    playingSound.bobj_04 = state != null ? state.innerStruct_00 : null;
    playingSound.soundFile_08 = soundFile;
    playingSound.soundIndex_0c = soundIndex;
    playingSound.playableSound_10 = playableSound;
    playingSound.patchIndex_12 = soundFileIndices.patchIndex_00;
    playingSound.sequenceIndex_14 = soundFileIndices.sequenceIndex_01;
    playingSound.pitchShiftVolRight_16 = pitchShiftVolRight;
    playingSound.pitchShiftVolLeft_18 = pitchShiftVolLeft;
    playingSound.pitch_1a = pitch;
    playingSound._1c = a6;
    playingSound.repeatDelayTotal_20 = repeatDelay;
    playingSound.repeatDelayCurrent_22 = 1;
    playingSound.initialDelay_24 = initialDelay;
  }

  @Method(0x8001aa44L)
  public static void unuseSoundFile(final int index) {
    soundFiles_800bcf80[index].used_00 = false;
  }

  @Method(0x8001aa64L)
  public static void FUN_8001aa64() {
    _800bd0f0.setu(0);
    _800bd6f8.setu(0);
  }

  @Method(0x8001aa78L)
  public static void FUN_8001aa78() {
    _800bca68.setu(0);
    _800bca6c.setu(0x7f00L);
  }

  @Method(0x8001aa90L)
  public static void FUN_8001aa90() {
    //LAB_8001aaa4
    for(int i = 0; i < 24; i++) {
      _800bc9a8[i]._00 = 0xffff;
    }
  }

  @Method(0x8001ab34L)
  public static FlowControl scriptPlaySound(final RunningScript<?> script) {
    playSound(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), (short)script.params_20[4].get(), (short)script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ab98L)
  public static FlowControl FUN_8001ab98(final RunningScript<?> script) {
    FUN_80019c80(soundFiles_800bcf80[script.params_20[0].get()], script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001abd0L)
  public static FlowControl scriptPlayBobjSound(final RunningScript<?> script) {
    playBobjSound(script.params_20[0].get(), (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[script.params_20[1].get()], script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ac48L)
  public static FlowControl FUN_8001ac48(final RunningScript<?> script) {
    stopBobjSound(script.params_20[0].get(), (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00, script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ac88L)
  public static FlowControl scriptPlayCombatantSound(final RunningScript<?> script) {
    playCombatantSound(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), (short)script.params_20[3].get(), (short)script.params_20[4].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001acd8L)
  public static FlowControl scriptStopBobjSound(final RunningScript<?> script) {
    stopBobjSound(script.params_20[0].get(), (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00, script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ad18L)
  public static void FUN_8001ad18() {
    //LAB_8001ad2c
    queuedSounds_800bd110.clear();
    FUN_8004d91c(true);
  }

  @Method(0x8001ad5cL)
  public static FlowControl FUN_8001ad5c(final RunningScript<?> script) {
    //LAB_8001ad70
    FUN_8001ad18();
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ada0L)
  public static void FUN_8001ada0() {
    startMusicSequence(currentSequenceData_800bd0f8);
  }

  @Method(0x8001adc8L)
  public static FlowControl FUN_8001adc8(final RunningScript<?> script) {
    startMusicSequence(currentSequenceData_800bd0f8);
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ae18L)
  public static FlowControl FUN_8001ae18(final RunningScript<?> script) {
    stopMusicSequence(currentSequenceData_800bd0f8, 2);
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ae68L)
  public static FlowControl FUN_8001ae68(final RunningScript<?> script) {
    stopMusicSequence(currentSequenceData_800bd0f8, 2);
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ae90L)
  public static void FUN_8001ae90() {
    if(_800bd0f0.get() == 0x2L) {
      stopMusicSequence(currentSequenceData_800bd0f8, 0);
    }
  }

  @Method(0x8001aec8L)
  public static FlowControl FUN_8001aec8(final RunningScript<?> script) {
    if(_800bd0f0.getSigned() == 0x2L) {
      stopMusicSequence(currentSequenceData_800bd0f8, 0);
    }

    //LAB_8001aef0
    return FlowControl.CONTINUE;
  }

  @Method(0x8001af00L)
  public static void FUN_8001af00() {
    startMusicSequence(encounterSoundEffects_800bd610.sequenceData_0c);
  }

  @Method(0x8001af34L)
  public static FlowControl FUN_8001af34(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001afa4L)
  public static FlowControl FUN_8001afa4(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001b014L)
  public static FlowControl FUN_8001b014(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001b094L)
  public static FlowControl FUN_8001b094(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001b0f0L)
  public static FlowControl scriptGetSssqTempoScale(final RunningScript<?> script) {
    script.params_20[0].set(sssqTempoScale_800bd100.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b118L)
  public static FlowControl scriptSetSssqTempoScale(final RunningScript<?> script) {
    sssqTempoScale_800bd100.set(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b134L)
  public static FlowControl FUN_8001b134(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b13cL)
  public static FlowControl FUN_8001b13c(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b144L)
  public static FlowControl FUN_8001b144(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b14cL)
  public static FlowControl scriptSetMainVolume(final RunningScript<?> script) {
    setMainVolume((short)script.params_20[0].get(), (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b17cL)
  public static FlowControl scriptSetSequenceVolume(final RunningScript<?> script) {
    setSequenceVolume((short)script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b1a8L)
  public static void setSequenceVolume(final int volume) {
    Scus94491BpeSegment_8004.setSequenceVolume(currentSequenceData_800bd0f8, volume);
    sequenceVolume_800bd108.setu(volume);
  }

  @Method(0x8001b1ecL)
  public static FlowControl scriptGetSequenceVolume(final RunningScript<?> script) {
    script.params_20[0].set((int)sequenceVolume_800bd108.getSigned());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b208L)
  public static FlowControl scriptSetAllSoundSequenceVolumes(final RunningScript<?> script) {
    //LAB_8001b22c
    for(int i = 0; i < 13; i++) {
      final SoundFile soundFile = soundFiles_800bcf80[i];

      // hasSubfile check added because music loads faster than retail. In Kongol I cutscene when Rose wakes up
      // Dart's dragoon spirit, the current music is unloaded and a new one is loaded. This method got called by
      // the script after the "load new music" method was called, but it was quick enough that it happened after
      // the current music was unloaded, but before the new music finished loading.
      if(soundFile.used_00 && soundFile.playableSound_10.sshdPtr_04.hasSubfile(4)) {
        setSoundSequenceVolume(soundFile.playableSound_10, (short)script.params_20[0].get());
      }

      //LAB_8001b250
    }

    return FlowControl.CONTINUE;
  }

  @Method(0x8001b27cL)
  public static FlowControl scriptSssqFadeIn(final RunningScript<?> script) {
    sssqFadeIn((short)script.params_20[0].get(), (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b2acL)
  public static FlowControl scriptStartSequenceAndChangeVolumeOverTime(final RunningScript<?> script) {
    startSequenceAndChangeVolumeOverTime(currentSequenceData_800bd0f8, (short)script.params_20[0].get(), (short)script.params_20[1].get());
    sequenceVolume_800bd108.setu(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b310L)
  public static FlowControl scriptSssqFadeOut(final RunningScript<?> script) {
    sssqFadeOut((short)script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  /**
   * Something to do with sequenced audio
   */
  @Method(0x8001b33cL)
  public static FlowControl scriptChangeSequenceVolumeOverTime(final RunningScript<?> script) {
    changeSequenceVolumeOverTime(currentSequenceData_800bd0f8, (short)script.params_20[0].get(), (short)script.params_20[1].get());
    sequenceVolume_800bd108.setu(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b3a0L)
  public static FlowControl scriptGetSequenceFlags(final RunningScript<?> script) {
    script.params_20[0].set(getSequenceFlags(currentSequenceData_800bd0f8));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b3e4L)
  public static int FUN_8001b3e4() {
    if(soundFiles_800bcf80[11].used_00) {
      return soundFiles_800bcf80[11].charId_02;
    }

    //LAB_8001b408
    return -1;
  }

  @Method(0x8001b410L)
  public static void FUN_8001b410() {
    if(_8004f6e4.getSigned() == -1) {
      return;
    }

    renderCombatDissolveEffect();

    if(loadingGameStateOverlay_8004dd08.get() == 1) {
      return;
    }

    if(_800bd740.getSigned() >= 0) {
      _800bd740.sub(0x1L);
      return;
    }

    //LAB_8001b460
    if(_800bd700.get() != 0) {
      FUN_8001c5bc();
    }

    //LAB_8001b480
    if((_800bc960.get() & 0x2) != 0) {
      if(_8004f6ec.get() == 0) {
        _8004f6ec.setu(0x1L);
        FUN_8001c594(0x1L, 0x6L);
        scriptStartEffect(1, 1);
      }
    }

    //LAB_8001b4c0
    if(_8004f6ec.get() != 0) {
      //LAB_8001b4d4
      if(_8004f6ec.get() >= 0x7L) {
        if(loadingGameStateOverlay_8004dd08.get() == 0) {
          _8004f6e4.setu(-0x1L);
          _800bc960.or(0x1);
        }
      }

      //LAB_8001b518
      _8004f6ec.addu(0x1L);
    }

    //LAB_8001b528
    _8004f6e8.addu(0x1L);

    //LAB_8001b53c
  }

  @Method(0x8001b54cL)
  public static void renderCombatDissolveEffect() {
    FUN_8001b92c();

    final int sp10 = -displayWidth_1f8003e0.get() / 2;
    final int sp14 = -displayHeight_1f8003e4.get() / 2;
    final int a0 = displayHeight_1f8003e4.get() / 8;
    final int v0 = 100 / a0;

    if(v0 == _800bd714.get()) {
      _800bd714.setu(0);
      _800bd710.addu(0x1L);
      final int v1 = a0 - 1;
      if(v1 < _800bd710.get()) {
        _800bd710.setu(v1);
      }
    }

    //LAB_8001b608
    final long fp = _800bd700.getAddress();
    int sp30 = 512;

    //LAB_8001b620
    for(int sp18 = 0; sp18 <= _800bd710.get(); sp18++) {
      final int sp24 = sp30 >> 8;
      int sp2c = sp10;
      final int s5 = displayHeight_1f8003e4.get() - ((int)_800bd710.get() + 1) * 8 + sp18 * 8;

      //LAB_8001b664
      for(int sp1c = 0; sp1c < displayWidth_1f8003e0.get() / 32 * 4; sp1c++) {
        final int s6 = sp1c * 8;

        //LAB_8001b6a4
        for(int s7 = 0; s7 <= 0; s7++) {
          int s3 = rand() % 4;
          if((rand() & 1) != 0) {
            s3 = -s3;
          }

          //LAB_8001b6dc
          final int s2 = rand() % 6;
          final int left = sp2c + s3;
          final int top = sp14 + s5 + s2 + sp24;

          //LAB_8001b734
          final GpuCommandPoly cmd = new GpuCommandPoly(4)
            .bpp(Bpp.BITS_15)
            .translucent(Translucency.HALF_B_PLUS_HALF_F)
            .monochrome((int)MEMORY.ref(4, fp).offset(0x8L).get() >> 8)
            .pos(0, left, top)
            .pos(1, left + 8, top)
            .pos(2, left, top + 8)
            .pos(3, left + 8, top + 8)
            .uv(0, s6, s5)
            .uv(1, s6 + 7, s5)
            .uv(2, s6, s5 + 8)
            .uv(3, s6 + 7, s5 + 8)
            .texture(GPU.getDisplayBuffer());

          //LAB_8001b868
          GPU.queueCommand(6, cmd);
        }

        sp2c += 8;
      }

      //LAB_8001b8b8
      sp30 += 512;
    }

    _800bd714.addu(0x1L);
    FUN_8001bbcc(sp10, sp14);
  }

  @Method(0x8001b92cL)
  public static void FUN_8001b92c() {
    final int width = displayWidth_1f8003e0.get();
    final int height = displayHeight_1f8003e4.get();
    final int left = -width / 2;
    final int right = width / 2;
    final int top = -height / 2;
    final int bottom = height / 2;

    GPU.queueCommand(6, new GpuCommandQuad().monochrome(1).pos(left - 32, top - 32, width + 64, 36));
    GPU.queueCommand(6, new GpuCommandQuad().monochrome(1).pos(left - 32, bottom - 4, width + 64, 36));
    GPU.queueCommand(6, new GpuCommandQuad().monochrome(1).pos(left - 32, top, 36, height));
    GPU.queueCommand(6, new GpuCommandQuad().monochrome(1).pos(right - 4, top, 36, height));
  }

  @Method(0x8001bbccL)
  public static void FUN_8001bbcc(final int x, final int y) {
    FUN_8001b92c();

    GPU.queueCommand(6, new GpuCommandPoly(4)
      .bpp(Bpp.BITS_15)
      .monochrome((int)_800bd708.get() >> 8)
      .pos(0, x, y)
      .pos(1, x + 384, y)
      .pos(2, x, y + displayHeight_1f8003e4.get() - 1)
      .pos(3, x + 384, y + displayHeight_1f8003e4.get() - 1)
      .uv(0, 0, 0)
      .uv(1, 384, 0)
      .uv(2, 0, 240)
      .uv(3, 384, 240)
      .texture(GPU.getDisplayBuffer())
    );
  }

  @Method(0x8001c4ecL)
  public static void FUN_8001c4ec() {
    chapterTitleCardMrg_800c6710 = null;
    _8004f6ec.setu(0);
    playSound(0, 16, 0, 0, (short)0, (short)0);
    vsyncMode_8007a3b8.set(2);
    _800bd740.setu(0x2L);
    _800bd700.setu(0);
    _800bd704.setu(0);
    _800bd708.setu(0x8000L);
    _800bd714.setu(0);
    _800bd710.setu(0);
    clearRed_8007a3a8.set(0);
    clearGreen_800bb104.set(0);
    clearBlue_800babc0.set(0);
    _8004f6e4.setu(0x1L);
    _8004f6e8.setu(0);
  }

  @Method(0x8001c594L)
  public static void FUN_8001c594(final long a0, final long a1) {
    _800bd700.setu(a0);
    _800bd704.setu(a1);
    _800bd708.setu(0x8000L);
    _800bd70c.setu(0x8000L / a1);
  }

  @Method(0x8001c5bcL)
  public static void FUN_8001c5bc() {
    if(_800bd700.get() == 0x1L) {
      _800bd704.subu(0x1L);
      _800bd708.subu(_800bd70c);

      if(_800bd704.get() == 0) {
        _800bd700.setu(0);
      }
    }
  }

  @Method(0x8001c5fcL)
  public static FlowControl FUN_8001c5fc(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001c604L)
  public static FlowControl FUN_8001c604(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001c60cL)
  public static int getSubmapMusicChange() {
    final int s0 = FUN_8001b3e4();

    final int musicIndex;
    jmp_8001c7a0:
    {
      //LAB_8001c63c
      SubmapMusic08 a2;
      int a3;
      for(a3 = 0, a2 = _8004fb00.get(a3); a2.submapCut_00.get() != 99 || a2.musicIndex_02.get() != 99; a3++, a2 = _8004fb00.get(a3)) { // I think 99 is just a sentinel value that means "end of list"
        final int submapIndex = submapIndex_800bd808.get();

        if(submapIndex == a2.submapCut_00.get()) {
          int v1 = 0;

          //LAB_8001c680
          do {
            if(submapIndex == 57) { // Opening (Rose intro, Dart forest, horses)
              if(a2.submapCuts_04.deref().get(v1).get() != submapCut_80052c30.get()) {
                continue;
              }

              if((gameState_800babc8._1a4[0] & 0x1) == 0) {
                //LAB_8001c7cc
                musicIndex = a2.musicIndex_02.get();
                break jmp_8001c7a0;
              }
            }

            //LAB_8001c6ac
            if(a2.submapCuts_04.deref().get(v1).get() == submapCut_80052c30.get() && (gameState_800babc8._1a4[a3 >>> 5] & 0x1 << (a3 & 0x1f)) != 0) {
              //LAB_8001c7c0
              musicIndex = a2.musicIndex_02.get();
              break jmp_8001c7a0;
            }

            //LAB_8001c6e4
          } while(a2.submapCuts_04.deref().get(++v1).get() != -1);
        }

        //LAB_8001c700
      }

      //LAB_8001c728
      SubmapMusic08 a0;
      for(a3 = 0, a0 = _8004fa98.get(a3); a0.submapCut_00.get() != 99 || a0.musicIndex_02.get() != 99; a3++, a0 = _8004fa98.get(a3)) {
        if(submapIndex_800bd808.get() == a0.submapCut_00.get()) {
          int v1 = 0;

          //LAB_8001c748
          do {
            if(a0.submapCuts_04.deref().get(v1).get() == submapCut_80052c30.get()) {
              //LAB_8001c7d8
              return FUN_8001c84c(s0, a0.musicIndex_02.get());
            }
          } while(a0.submapCuts_04.deref().get(++v1).get() != -1);
        }

        //LAB_8001c76c
      }

      musicIndex = getCurrentSubmapMusic();
    }

    //LAB_8001c7a0
    final int v1 = FUN_8001c84c(s0, musicIndex);
    if(v1 != -2) {
      return v1;
    }

    //LAB_8001c7ec
    if(!currentSequenceData_800bd0f8.musicPlaying_028) {
      return -2;
    }

    //LAB_8001c808
    return -3;
  }

  @Method(0x8001c84cL)
  public static int FUN_8001c84c(final int a0, final int a1) {
    if(a0 != a1) {
      return a1;
    }

    if(a0 == -1) {
      return -1;
    }

    return -2;
  }

  @Method(0x8001c874L)
  public static int getCurrentSubmapMusic() {
    if(submapIndex_800bd808.get() == 56) { // Moon
      for(int i = 0; ; i++) {
        final MoonMusic08 moonMusic = moonMusic_8004ff10.get(i);

        if(moonMusic.submapCut_00.get() == submapCut_80052c30.get()) {
          return moonMusic.musicIndex_04.get();
        }
      }
    }

    //LAB_8001c8bc
    return submapMusic_80050068.get(submapIndex_800bd808.get()).get();
  }

  @Method(0x8001cae0L)
  public static void charSoundEffectsLoaded(final List<FileData> files, final int charSlot) {
    final int charId = gameState_800babc8.charIds_88[charSlot];

    //LAB_8001cb34
    final int index = characterSoundFileIndices_800500f8.get(charSlot).get();
    final SoundFile sound = soundFiles_800bcf80[index];

    sound.name = "Char slot %d sound effects".formatted(charSlot);
    sound.charId_02 = charId;
    sound.indices_08 = SoundFileIndices.load(files.get(1));

    if(charSlot == 0 || charSlot == 1) {
      //LAB_8001cc30
      setSpuDmaCompleteCallback(Scus94491BpeSegment::FUN_8001e8cc);
    } else {
      //LAB_8001cc38
      //LAB_8001cc40
      setSpuDmaCompleteCallback(Scus94491BpeSegment::FUN_8001e8d4);
    }

    //LAB_8001cc48
    //LAB_8001cc50
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), charSlotSpuOffsets_80050190.get(charSlot).get());
    sound.used_00 = true;
  }

  @Method(0x8001cce8L)
  public static void FUN_8001cce8(final int bobjIndex, final int type) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[bobjIndex].innerStruct_00;

    //LAB_8001cd3c
    int charSlot;
    for(charSlot = 0; charSlot < 3; charSlot++) {
      final SoundFile soundFile = soundFiles_800bcf80[characterSoundFileIndices_800500f8.get(charSlot).get()];

      if(soundFile.charId_02 == bobj.charId_272) {
        break;
      }
    }

    //LAB_8001cd78
    final SoundFile soundFile = soundFiles_800bcf80[characterSoundFileIndices_800500f8.get(charSlot).get()];
    sssqUnloadPlayableSound(soundFile.playableSound_10);
    soundFile.used_00 = false;

    loadedDrgnFiles_800bcf78.oru(0x8L);

    final int fileIndex;
    final String soundName;
    if(type != 0) {
      //LAB_8001ce44
      fileIndex = 1298 + bobj.charId_272;
      soundName = "Char slot %d attack sounds".formatted(fileIndex);
    } else if(bobj.charId_272 != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
      //LAB_8001ce18
      fileIndex = 1307 + bobj.charId_272;
      soundName = "Char slot %d dragoon attack sounds".formatted(fileIndex);
    } else {
      fileIndex = 1307;
      soundName = "Char slot %d dragoon attack sounds".formatted(fileIndex);
    }

    //LAB_8001ce70
    final int finalCharSlot = charSlot;
    loadDrgnDir(0, fileIndex, files -> Scus94491BpeSegment.FUN_8001ce98(files, soundName, finalCharSlot));
  }

  @Method(0x8001ce98L)
  public static void FUN_8001ce98(final List<FileData> files, final String soundName, final int charSlot) {
    final SoundFile sound = soundFiles_800bcf80[characterSoundFileIndices_800500f8.get(charSlot).get()];

    //LAB_8001cee8
    //LAB_8001cf2c
    sound.name = soundName;
    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.charId_02 = files.get(0).readShort(0);
    setSpuDmaCompleteCallback(Scus94491BpeSegment::FUN_8001e950);
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), charSlotSpuOffsets_80050190.get(charSlot).get());
    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
    sound.used_00 = true;
  }

  /**
   * <ol start="0">
   *   <li>Dragoon transformation sounds</li>
   *   <li>Encounter sound effects</li>
   *   <li>Maybe item magic?</li>
   * </ol>
   */
  @Method(0x8001d068L)
  public static void loadDeffSounds(final ScriptState<BattleObject27c> bobjState, final int type) {
    final BattleObject27c bobj = bobjState.innerStruct_00;

    unloadSoundFile(3);
    unloadSoundFile(6);

    if(type == 0) {
      //LAB_8001d0e0
      loadedDrgnFiles_800bcf78.oru(0x40L);
      if(bobj.charId_272 != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
        //LAB_8001d134
        // Regular dragoons
        loadDrgnDir(0, 1317 + bobj.charId_272, files -> FUN_8001e98c(files, "%s dragoon transformation sounds (file %d)".formatted(getCharacterName(bobj.charId_272), 1317 + bobj.charId_272)));
      } else {
        // Divine dragoon
        loadDrgnDir(0, 1328, files -> FUN_8001e98c(files, "Divine dragoon transformation sounds (file 1328)"));
      }
    } else if(type == 1) {
      //LAB_8001d164
      FUN_8001d2d8();
    } else if(type == 2) {
      //LAB_8001d174
      loadedDrgnFiles_800bcf78.oru(0x40L);

      //LAB_8001d1a8
      loadDrgnDir(0, 1327, files -> FUN_8001e98c(files, "Item sounds? (file 1327)"));
    }

    //LAB_8001d1b0
  }

  @Method(0x8001d1c4L)
  public static void loadMonsterSounds() {
    final int encounterId = encounterId_800bb0f8.get();
    final int fileIndex;
    if(encounterId == 390) { // Doel
      //LAB_8001d21c
      fileIndex = 1290;
    } else if(encounterId == 431) { // Zackwell
      //LAB_8001d244
      fileIndex = 1296;
      //LAB_8001d208
    } else if(encounterId == 443) { // Melbu
      //LAB_8001d270
      fileIndex = 1292;
    } else {
      //LAB_8001d298
      fileIndex = 778 + encounterId_800bb0f8.get();
    }

    //LAB_8001d2c0
    // Example file: 1017
    loadMonsterSounds(fileIndex);
  }

  @Method(0x8001d2d8L)
  public static void FUN_8001d2d8() {
    final int encounterId = encounterId_800bb0f8.get();
    if(encounterId == 390) { // Doel
      //LAB_8001d330
      if(battleState_8006e398.stageProgression_eec == 0) {
        loadMonsterSounds(1290);
      } else {
        //LAB_8001d370
        loadMonsterSounds(1291);
      }
      //LAB_8001d31c
    } else if(encounterId == 431) { // Zackwell
      //LAB_8001d394
      if(battleState_8006e398.stageProgression_eec == 0) {
        loadMonsterSounds(1296);
      } else {
        //LAB_8001d3d0
        loadMonsterSounds(1297);
      }
    } else if(encounterId == 443) { // Melbu
      //LAB_8001d3f8
      final int stageProgression = battleState_8006e398.stageProgression_eec;
      if(stageProgression == 0) {
        //LAB_8001d43c
        loadMonsterSounds(1292);
        //LAB_8001d424
      } else if(stageProgression == 1) {
        //LAB_8001d464
        loadMonsterSounds(1293);
      } else if(stageProgression == 4) {
        //LAB_8001d490
        loadMonsterSounds(1294);
      } else if(stageProgression == 6) {
        //LAB_8001d4b8
        loadMonsterSounds(1295);
      }
    } else {
      //LAB_8001d4dc
      loadMonsterSounds(778 + encounterId);
    }

    //LAB_8001d50c
  }

  public static int soundBufferOffset;

  /** TODO this isn't thread-safe */
  private static void loadMonsterSounds(final int fileIndex) {
    loadedDrgnFiles_800bcf78.oru(0x10L);
    soundBufferOffset = 0;

    for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
      final SoundFile file = soundFiles_800bcf80[monsterSoundFileIndices_800500e8.get(monsterSlot).get()];
      file.charId_02 = -1;
      file.used_00 = false;

      if(Unpacker.exists("SECT/DRGN0.BIN/%d/%d".formatted(fileIndex, monsterSlot))) {
        final int finalMonsterSlot = monsterSlot;
        loadDrgnDir(0, fileIndex + "/" + monsterSlot, files -> FUN_8001d51c(files, "Monster slot %d (file %d)".formatted(finalMonsterSlot, fileIndex), finalMonsterSlot));
      }
    }

    loadedDrgnFiles_800bcf78.and(0xffff_ffefL);
  }

  @Method(0x8001d51cL)
  public static void FUN_8001d51c(final List<FileData> files, final String soundName, final int monsterSlot) {
    //LAB_8001d698
    final int file3Size = files.get(3).size();

    if(file3Size > 48) {
      //LAB_8001d704
      _800bd774.setu(file3Size);

      //LAB_8001d718
      int v1 = 51024;
      for(int n = 3; n >= 0 && file3Size < v1; n--) {
        v1 = v1 - 17008;
      }

      //LAB_8001d72c
      final int soundFileIndex = monsterSoundFileIndices_800500e8.get(monsterSlot).get();
      final SoundFile soundFile = soundFiles_800bcf80[soundFileIndex];

      soundFile.name = soundName;
      soundFile.indices_08 = SoundFileIndices.load(files.get(1));
      soundFile.charId_02 = files.get(0).readShort(0);

      setSpuDmaCompleteCallback(null);

      //LAB_8001d80c
      soundFile.playableSound_10 = loadSshdAndSoundbank(soundFile.name, files.get(3), new Sshd(files.get(2)), 0x5_a1e0 + soundBufferOffset);

      loadedDrgnFiles_800bcf78.and(0xffff_ffefL);

      setSoundSequenceVolume(soundFile.playableSound_10, 0x7f);
      soundFile.used_00 = true;
      soundBufferOffset += file3Size + (file3Size & 0xf);
    }

    //LAB_8001d8ac
  }

  @Method(0x8001d8d8L)
  public static void battleCutsceneSoundsLoaded(final List<FileData> files, final String soundName) {
    final SoundFile soundFile = soundFiles_800bcf80[9];
    soundFile.name = soundName;
    soundFile.used_00 = true;
    soundFile.charId_02 = files.get(0).readUShort(0);
    soundFile.indices_08 = SoundFileIndices.load(files.get(2));
    soundFile.spuRamOffset_14 = files.get(4).size();
    soundFile.numberOfExtraSoundbanks_18 = files.get(1).readUShort(0) - 1;

    setSpuDmaCompleteCallback(Scus94491BpeSegment::loadExtraBattleCutsceneSoundbanks);

    soundFile.playableSound_10 = loadSshdAndSoundbank(soundFile.name, files.get(4), new Sshd(files.get(3)), 0x4_de90);
    setSoundSequenceVolume(soundFile.playableSound_10, 0x7f);
  }

  @Method(0x8001d9d0L)
  public static void loadEncounterMusic() {
    final StageData10 stageData = stageData_80109a98.get(encounterId_800bb0f8.get());

    if(stageData.musicIndex_01.get() != 0xff) {
      loadedDrgnFiles_800bcf78.oru(0x80L);

      final int fileIndex;
      final Consumer<List<FileData>> callback;
      if((stageData.musicIndex_01.get() & 0x1f) == 0x13) {
        unloadSoundFile(8);
        fileIndex = 732;
        callback = files -> musicPackageLoadedCallback(files, 732 << 8);
      } else {
        //LAB_8001da58
        fileIndex = (int)combatMusicFileIndices_800501bc.get(stageData.musicIndex_01.get() & 0x1f).get();
        callback = files -> FUN_8001fb44(files, "Encounter music %d (file %d)".formatted(stageData.musicIndex_01.get() & 0x1f, fileIndex), 0);
      }

      //LAB_8001daa4
      loadDrgnDir(0, fileIndex, callback);
    }

    //LAB_8001daac
  }

  @Method(0x8001dabcL)
  public static void musicPackageLoadedCallback(final List<FileData> files, final int a2) {
    LOGGER.info("Music package %d loaded", a2 >> 8);

    final SoundFile sound = soundFiles_800bcf80[11];
    sound.used_00 = true;
    sound.name = "Music package (file %d)".formatted(a2 >> 8);

    if(mainCallbackIndex_8004dd20.get() == 5 || mainCallbackIndex_8004dd20.get() == 6 && encounterId_800bb0f8.get() == 443) { // Melbu
      //LAB_8001db1c
      _800bd0fc.setu(a2);
      sound.charId_02 = files.get(0).readShort(0);
      sound.numberOfExtraSoundbanks_18 = files.get(1).readUByte(0) - 1;
      sound.spuRamOffset_14 = files.get(4).size();
      setSpuDmaCompleteCallback(null);
      sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(4), new Sshd(files.get(3)), 0x2_1f70);
      currentSequenceData_800bd0f8 = loadSssq(sound.playableSound_10, new Sssq(files.get(2)));
      loadExtraSoundbanks();
      melbuMusicLoaded_800bd781.set(true);
    } else {
      //LAB_8001dbf0
      if(files.size() == 5) {
        _800bd0fc.setu(a2);
        sound.charId_02 = files.get(0).readShort(0);
        sound.numberOfExtraSoundbanks_18 = files.get(1).readUByte(0) - 1;
        sound.spuRamOffset_14 = files.get(4).size();
        setSpuDmaCompleteCallback(null);
        sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(4), new Sshd(files.get(3)), 0x2_1f70);
        currentSequenceData_800bd0f8 = loadSssq(sound.playableSound_10, new Sssq(files.get(2)));
        loadExtraSoundbanks();
      } else {
        //LAB_8001dcdc
        _800bd0fc.setu(a2);
        sound.charId_02 = files.get(0).readShort(0);

        //LAB_8001dd44
        setSpuDmaCompleteCallback(null);
        sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x2_1f70);
        currentSequenceData_800bd0f8 = loadSssq(sound.playableSound_10, new Sssq(files.get(1)));

        if((a2 & 1) == 0) {
          Scus94491BpeSegment.FUN_8001fa18();
        } else {
          //LAB_8001dd3c
          Scus94491BpeSegment.FUN_8001fab4();
        }
      }

      //LAB_8001dd98
      melbuMusicLoaded_800bd781.set(false);
    }

    //LAB_8001dda0
    setSequenceVolume(40);
    _800bd0f0.setu(0x2L);
  }

  @Method(0x8001ddd8L)
  public static void FUN_8001ddd8() {
    final EncounterSoundEffects10 encounterSoundEffects = encounterSoundEffects_800bd610;

    startMusicSequence(currentSequenceData_800bd0f8);
    encounterSoundEffects._00 = 2;
    encounterSoundEffects.sequenceData_0c = loadSssq(soundFiles_800bcf80[encounterSoundEffects.soundFileIndex].playableSound_10, encounterSoundEffects.sssq_08);
    musicLoaded_800bd782.incr();
    loadedDrgnFiles_800bcf78.and(0xffff_ff7fL);
  }

  @Method(0x8001de84L)
  public static void loadEncounterSoundsAndMusic() {
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);

    final StageData10 stageData = stageData_80109a98.get(encounterId_800bb0f8.get());

    if(stageData.musicIndex_01.get() != 0xff) {
      unloadEncounterSoundEffects();

      // Pulled this up from below since the methods below queue files which are now loaded synchronously. This code would therefore run before the files were loaded.
      //LAB_8001df8c
      unloadSoundFile(8);

      final int type = combatSoundEffectsTypes_8005019c.get(stageData.musicIndex_01.get() & 0x1f).get();
      if(type == 0xc) {
        loadEncounterSoundEffects(696);
      } else if(type == 0xd) {
        //LAB_8001df68
        loadEncounterSoundEffects(697);
      } else if(type == 0xe) {
        //LAB_8001df70
        loadEncounterSoundEffects(698);
      } else if(type == 0xf) {
        //LAB_8001df78
        loadEncounterSoundEffects(699);
        //LAB_8001df44
      } else if(type == 0x56) {
        //LAB_8001df84
        loadEncounterSoundEffects(700);
      } else if(type == 0x58) {
        //LAB_8001df80
        loadEncounterSoundEffects(701);
      }

      loadEncounterMusic();
    }

    //LAB_8001df9c
    loadedDrgnFiles_800bcf78.oru(0x8L);

    // Player combat sounds for current party composition (example file: 764)
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final int charIndex = gameState_800babc8.charIds_88[charSlot];

      if(charIndex != -1) {
        final String name = getCharacterName(charIndex).toLowerCase();
        final int finalCharSlot = charSlot;
        loadDir("characters/%s/sounds/combat".formatted(name), files -> Scus94491BpeSegment.charSoundEffectsLoaded(files, finalCharSlot));
      }
    }

    loadMonsterSounds();
    decrementOverlayCount();
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

  @Method(0x8001e010L)
  public static void FUN_8001e010(final int a0) {
    if(a0 == 0) {
      //LAB_8001e054
      copyPlayingSounds(queuedSounds_800bd110, playingSoundsBackup_800bca78);
      FUN_8001ad18();
      unloadSoundFile(8);
      unloadSoundFile(8);

      loadedDrgnFiles_800bcf78.oru(0x80L);
      loadDrgnDir(0, 5815, files -> Scus94491BpeSegment.musicPackageLoadedCallback(files, 5815 << 8));
      //LAB_8001e044
    } else if(a0 == 1) {
      //LAB_8001e094
      FUN_8001ad18();
      unloadSoundFile(8);
      unloadSoundFile(8);

      //LAB_8001e0bc
      loadedDrgnFiles_800bcf78.oru(0x80L);
      loadDrgnDir(0, 5900, files -> Scus94491BpeSegment.musicPackageLoadedCallback(files, 5900 << 8));
    } else if(a0 == -1) {
      //LAB_8001e0f8
      if(savedGameSelected_800bdc34.get()) {
        if(mainCallbackIndex_8004dd20.get() == 8 && gameState_800babc8.isOnWorldMap_4e4) {
          sssqResetStuff();
          unloadSoundFile(8);
          loadedDrgnFiles_800bcf78.oru(0x80L);
          loadDrgnDir(0, 5850, files -> Scus94491BpeSegment.musicPackageLoadedCallback(files, 5850 << 8));
        }
      } else {
        //LAB_8001e160
        unloadEncounterSoundEffects();
        unloadSoundFile(8);

        final long callbackIndex = mainCallbackIndex_8004dd20.get();
        if(callbackIndex == 5) {
          //LAB_8001e1ac
          final int musicIndex = getSubmapMusicChange();
          //LAB_8001e1dc
          if(musicIndex == -1) {
            FUN_8001ae90();
            musicLoaded_800bd782.set(1);
          } else if(musicIndex == -2) {
            //LAB_8001e1f4
            FUN_8001ada0();

            //LAB_8001e200
            musicLoaded_800bd782.set(1);
          } else if(musicIndex == -3) {
            musicLoaded_800bd782.set(1);
          } else {
            //LAB_8001e1dc
            //LAB_8001e20c
            unloadSoundFile(8);
            final int fileIndex = 5815 + musicIndex * 5;

            //LAB_8001e23c
            loadedDrgnFiles_800bcf78.oru(0x80L);
            loadDrgnDir(0, fileIndex, files -> Scus94491BpeSegment.musicPackageLoadedCallback(files, fileIndex << 8));
          }
        } else if(callbackIndex == 6) {
          //LAB_8001e264
          sssqResetStuff();
        } else if(callbackIndex == 8) {
          unloadSoundFile(8);
        }
      }

      //LAB_8001e26c
      FUN_8001ad18();
      copyPlayingSounds(playingSoundsBackup_800bca78, queuedSounds_800bd110);
      playingSoundsBackup_800bca78.clear();
    }

    //LAB_8001e288
  }

  /**
   * <ul>
   *   <li>0 - menu sounds</li>
   *   <li>1 - players</li>
   *   <li>2 - dragoon/spells/items</li>
   *   <li>3 - monsters</li>
   *   <li>4 - submap sounds</li>
   *   <li>5 - battle cutscene sounds</li>
   *   <li>6 - monster sounds</li>
   *   <li>7 - player sounds</li>
   *   <li>8 - music</li>
   *   <li>9 - world map sounds</li>
   * </ul>
   */
  @Method(0x8001e29cL)
  public static void unloadSoundFile(final int soundType) {
    switch(soundType) {
      case 0 -> {
        if(soundFiles_800bcf80[0].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[0].playableSound_10);
          soundFiles_800bcf80[0].used_00 = false;
        }
      }

      case 1 -> {
        //LAB_8001e324
        for(int charSlot = 0; charSlot < 3; charSlot++) {
          final int index = characterSoundFileIndices_800500f8.get(charSlot).get();

          if(soundFiles_800bcf80[index].used_00) {
            sssqUnloadPlayableSound(soundFiles_800bcf80[index].playableSound_10);
            soundFiles_800bcf80[index].used_00 = false;
          }

          //LAB_8001e374
        }
      }

      case 2 -> {
        if(soundFiles_800bcf80[4].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[4].playableSound_10);
          soundFiles_800bcf80[4].used_00 = false;
        }
      }

      case 3 -> {
        //LAB_8001e3dc
        for(int monsterIndex = 0; monsterIndex < 4; monsterIndex++) {
          final int index = monsterSoundFileIndices_800500e8.get(monsterIndex).get();

          if(soundFiles_800bcf80[index].used_00) {
            sssqUnloadPlayableSound(soundFiles_800bcf80[index].playableSound_10);
            soundFiles_800bcf80[index].used_00 = false;
          }

          //LAB_8001e450
        }
      }

      case 4 -> {
        if(soundFiles_800bcf80[8].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[8].playableSound_10);
          soundFiles_800bcf80[8].used_00 = false;
        }
      }

      case 5 -> {
        if(soundFiles_800bcf80[9].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[9].playableSound_10);
          soundFiles_800bcf80[9].used_00 = false;
        }
      }

      case 6, 7 -> {
        if(soundFiles_800bcf80[10].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[10].playableSound_10);
          soundFiles_800bcf80[10].used_00 = false;
        }
      }

      case 8 -> {
        if(_800bd0f0.getSigned() != 0) {
          stopMusicSequence(currentSequenceData_800bd0f8, 1);
          freeSequence(currentSequenceData_800bd0f8);

          //LAB_8001e56c
          _800bd0f0.setu(0);
        }

        //LAB_8001e570
        if(soundFiles_800bcf80[11].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[11].playableSound_10);
          soundFiles_800bcf80[11].used_00 = false;
        }
      }

      case 9 -> {
        if(soundFiles_800bcf80[12].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[12].playableSound_10);
          soundFiles_800bcf80[12].used_00 = false;
        }
      }
    }

    //LAB_8001e5d4
  }

  @Method(0x8001e5ecL)
  public static void loadMenuSounds() {
    loadedDrgnFiles_800bcf78.oru(0x1L);
    loadDrgnFiles(0, Scus94491BpeSegment::menuSoundsLoaded, "5739/0", "5739/1", "5739/2", "5739/3");
  }

  /**
   * 0: unknown, 2-byte file (00 00)
   * 1: unknown, 0x64 byte file, counts up from 0000 to 0033
   * 2: SShd file
   * 3: Soundbank (has some map and battle sounds)
   */
  @Method(0x8001e694L)
  public static void menuSoundsLoaded(final List<FileData> files) {
    setPreloadingAudioAssets(true);

    final SoundFile sound = soundFiles_800bcf80[0];
    sound.used_00 = true;
    sound.name = "Menu sounds (file 5739)";

    sound.indices_08 = SoundFileIndices.load(files.get(1));
    setSpuDmaCompleteCallback(Scus94491BpeSegment::unloadSoundbank_800bd778);

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x1010);
  }

  @Method(0x8001e780L)
  public static void unloadSoundbank_800bd778() {
    setPreloadingAudioAssets(false);
    loadedDrgnFiles_800bcf78.and(0xffff_fffeL);
  }

  @Method(0x8001e640L)
  public static FlowControl FUN_8001e640(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001e8ccL)
  public static void FUN_8001e8cc() {
    // empty
  }

  @Method(0x8001e8d4L)
  public static void FUN_8001e8d4() {
    loadedDrgnFiles_800bcf78.and(0xffff_fff7L);
  }

  @Method(0x8001e918L)
  public static FlowControl FUN_8001e918(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x8001e920L)
  public static FlowControl FUN_8001e920(final RunningScript<?> script) {
    FUN_8001cce8(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001e950L)
  public static void FUN_8001e950() {
    loadedDrgnFiles_800bcf78.and(0xffff_fff7L);
  }

  @Method(0x8001e98cL)
  public static void FUN_8001e98c(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[4];
    sound.used_00 = true;
    sound.name = soundName;

    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.charId_02 = files.get(0).readShort(0);
    setSpuDmaCompleteCallback(Scus94491BpeSegment::FUN_8001ea5c);

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x5_a1e0);
    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
  }

  @Method(0x8001ea5cL)
  public static void FUN_8001ea5c() {
    loadedDrgnFiles_800bcf78.and(0xffff_ffbfL);
  }

  @Method(0x8001eadcL)
  public static void loadSubmapSounds(final int submapIndex) {
    loadedDrgnFiles_800bcf78.oru(0x2L);
    loadDrgnDir(0, 5750 + submapIndex, files -> submapSoundsLoaded(files, "Submap %d sounds (file %d)".formatted(submapIndex, 5750 + submapIndex)));
  }

  @Method(0x8001eb30L)
  public static FlowControl FUN_8001eb30(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001eb38L)
  public static void submapSoundsLoaded(final List<FileData> files, final String soundName) {
    soundFiles_800bcf80[8].name = soundName;
    soundFiles_800bcf80[8].indices_08 = SoundFileIndices.load(files.get(2));
    soundFiles_800bcf80[8].ptr_0c = files.get(1);
    soundFiles_800bcf80[8].charId_02 = files.get(0).readShort(0);
    setSpuDmaCompleteCallback(Scus94491BpeSegment::submapSoundsCleanup);

    final Sshd sshd = new Sshd(files.get(3));
    if(files.get(4).size() != sshd.soundBankSize_04) {
      throw new RuntimeException("Size didn't match, need to resize array or something");
    }

    soundFiles_800bcf80[8].playableSound_10 = loadSshdAndSoundbank(soundName, files.get(4), sshd, 0x4_de90);
    setSoundSequenceVolume(soundFiles_800bcf80[8].playableSound_10, 0x7f);
    soundFiles_800bcf80[8].used_00 = true;
  }

  @Method(0x8001ec18L)
  public static void submapSoundsCleanup() {
    loadedDrgnFiles_800bcf78.and(0xffff_fffdL);
    musicLoaded_800bd782.incr();
  }

  @Method(0x8001ecccL)
  public static FlowControl scriptLoadBattleCutsceneSounds(final RunningScript<?> script) {
    loadedDrgnFiles_800bcf78.oru(0x4L);
    sssqResetStuff();
    loadDrgnDir(0, 2437 + script.params_20[0].get() * 3, fileData -> battleCutsceneSoundsLoaded(fileData, "Cutscene %d sounds (file %d)".formatted(script.params_20[0].get(), 2437 + script.params_20[0].get() * 3)));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ed3cL)
  public static void loadExtraBattleCutsceneSoundbanks() {
    final SoundFile soundFile = soundFiles_800bcf80[9];

    //LAB_8001ed88
    for(int i = 1; i <= soundFile.numberOfExtraSoundbanks_18; i++) {
      loadDrgnFile(0, 2437 + soundFile.charId_02 * 3 + i, Scus94491BpeSegment::uploadExtraBattleCutsceneSoundbank);
    }

    //LAB_8001edd4
    loadedDrgnFiles_800bcf78.and(0xffff_fffbL);
    musicLoaded_800bd782.incr();
  }

  @Method(0x8001edfcL)
  public static void uploadExtraBattleCutsceneSoundbank(final FileData file) {
    final SoundFile soundFile = soundFiles_800bcf80[9];
    setSpuDmaCompleteCallback(null);

    SPU.directWrite(0x4_de90 + soundFile.spuRamOffset_14, file.getBytes());
    soundFile.spuRamOffset_14 += file.size();
  }

  @Method(0x8001eea8L)
  public static void FUN_8001eea8(final int index) {
    loadedDrgnFiles_800bcf78.oru(0x8000L);
    loadDrgnDir(0, 5740 + index, files -> FUN_8001eefc(files, "Unknown WMAP sounds %d (file %d)".formatted(index, 5740 + index)));
  }

  @Method(0x8001eefcL)
  public static void FUN_8001eefc(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[12];
    sound.name = soundName;
    sound.used_00 = true;

    sound.indices_08 = SoundFileIndices.load(files.get(2));
    sound.charId_02 = files.get(0).readShort(0);

    setSpuDmaCompleteCallback(Scus94491BpeSegment::FUN_8001efcc);
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(4), new Sshd(files.get(3)), 0x4_de90);

    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
  }

  @Method(0x8001efccL)
  public static void FUN_8001efcc() {
    loadedDrgnFiles_800bcf78.and(0xffff_7fffL);
  }

  @Method(0x8001f070L)
  public static FlowControl scriptLoadMonsterAttackSounds(final RunningScript<?> script) {
    loadedDrgnFiles_800bcf78.oru(0x20L);
    unloadSoundFile(6);
    loadDrgnDir(0, 1841 + script.params_20[0].get(), files -> monsterAttackSoundsLoaded(files, "Monster %d sounds (file %d)".formatted(script.params_20[0].get(), 1841 + script.params_20[0].get())));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f0dcL)
  public static void monsterAttackSoundsLoaded(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[10];
    sound.name = soundName;
    sound.used_00 = true;

    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.charId_02 = files.get(0).readShort(0);

    setSpuDmaCompleteCallback(null);
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x6_6930);

    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
    loadedDrgnFiles_800bcf78.and(0xffff_ffdfL);
  }

  @Method(0x8001f250L)
  public static FlowControl scriptLoadPlayerAttackSounds(final RunningScript<?> script) {
    loadedDrgnFiles_800bcf78.oru(0x1_0000L);
    unloadSoundFile(7);
    loadDrgnDir(0, 1897 + script.params_20[0].get(), files -> playerAttackSoundsLoaded(files, "Addition index %d sounds (file %d)".formatted(script.params_20[0].get(), 1897 + script.params_20[0].get())));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f2c0L)
  public static void playerAttackSoundsLoaded(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[10];
    sound.name = soundName;
    sound.used_00 = true;

    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.charId_02 = files.get(0).readShort(0);

    setSpuDmaCompleteCallback(Scus94491BpeSegment::FUN_8001f390);
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x6_6930);

    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
  }

  @Method(0x8001f390L)
  public static void FUN_8001f390() {
    loadedDrgnFiles_800bcf78.and(0xfffe_ffffL);
  }

  /**
   * Loads an audio MRG from DRGN0. File index is 5815 + index * 5.
   *
   * @param index <ol start="0">
   *   <li>Inventory music</li>
   *   <li>Main menu music</li>
   *   <li>Seems to be the previous one and the next one combined?</li>
   *   <li>Battle music, more?</li>
   *   <li>Same as previous</li>
   *   <li>...</li>
   * </ol>
   */
  @Method(0x8001f3d0L)
  public static void loadMusicPackage(final int index, final int a1) {
    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.oru(0x80L);
    final int fileIndex = 5815 + index * 5;
    loadDrgnDir(0, fileIndex, files -> Scus94491BpeSegment.musicPackageLoadedCallback(files, fileIndex << 8 | a1));
  }

  @Method(0x8001f450L)
  public static FlowControl scriptLoadMusicPackage(final RunningScript<?> script) {
    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.oru(0x80L);
    final int fileIndex = 5815 + script.params_20[0].get() * 5;
    loadDrgnDir(0, fileIndex, files -> Scus94491BpeSegment.musicPackageLoadedCallback(files, fileIndex << 8 | script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f560L)
  public static FlowControl FUN_8001f560(final RunningScript<?> script) {
    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.oru(0x80L);
    final int fileIndex = 732 + script.params_20[0].get() * 5;
    loadDrgnDir(0, fileIndex, files -> Scus94491BpeSegment.musicPackageLoadedCallback(files, fileIndex << 8 | script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f674L)
  public static FlowControl FUN_8001f674(final RunningScript<?> script) {
    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.oru(0x80L);
    final int fileIndex = 2353 + script.params_20[0].get() * 6;
    loadDrgnDir(0, fileIndex, files -> Scus94491BpeSegment.musicPackageLoadedCallback(files, fileIndex << 8 | script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f708L)
  public static void FUN_8001f708(final int chapterIndex, final int a1) {
    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.oru(0x80L);
    final int fileIndex = 5850 + chapterIndex * 5;
    loadDrgnDir(0, fileIndex, files -> Scus94491BpeSegment.musicPackageLoadedCallback(files, fileIndex << 8 | a1));
  }

  @Method(0x8001f810L)
  public static void loadExtraSoundbanks() {
    //LAB_8001f860
    for(int extraSoundbankIndex = 0; extraSoundbankIndex < soundFiles_800bcf80[11].numberOfExtraSoundbanks_18; extraSoundbankIndex++) {
      final Consumer<FileData> callback;
      if(extraSoundbankIndex < soundFiles_800bcf80[11].numberOfExtraSoundbanks_18 - 1) {
        callback = Scus94491BpeSegment::FUN_8001f8e0;
      } else {
        //LAB_8001f87c
        callback = Scus94491BpeSegment::FUN_8001f968;
      }

      //LAB_8001f88c
      loadDrgnFile(0, ((int)_800bd0fc.get() >> 8) + extraSoundbankIndex + 1, callback);
    }

    //LAB_8001f8b4
    musicLoaded_800bd782.incr();
  }

  @Method(0x8001f8e0L)
  public static void FUN_8001f8e0(final FileData data) {
    setSpuDmaCompleteCallback(null);

    SPU.directWrite(0x2_1f70 + soundFiles_800bcf80[11].spuRamOffset_14, data.getBytes());

    soundFiles_800bcf80[11].spuRamOffset_14 += data.size();
  }

  @Method(0x8001f968L)
  public static void FUN_8001f968(final FileData data) {
    setSpuDmaCompleteCallback(null);

    SPU.directWrite(0x2_1f70 + soundFiles_800bcf80[11].spuRamOffset_14, data.getBytes());

    if(_800bd0fc.get(0x1L) == 0) {
      startMusicSequence(currentSequenceData_800bd0f8);
    }

    loadedDrgnFiles_800bcf78.and(0xffff_ff7fL);
  }

  @Method(0x8001fa18L)
  public static void FUN_8001fa18() {
    startMusicSequence(currentSequenceData_800bd0f8);
    sssqTempo_800bd104.set(sssqGetTempo(currentSequenceData_800bd0f8) * sssqTempoScale_800bd100.get());
    sssqSetTempo(currentSequenceData_800bd0f8, sssqTempo_800bd104.get() >> 8);

    musicLoaded_800bd782.incr();
    loadedDrgnFiles_800bcf78.and(0xffff_ff7fL);
  }

  @Method(0x8001fab4L)
  public static void FUN_8001fab4() {
    throw new RuntimeException("Not yet implemented");
  }

  @Method(0x8001fb44L)
  public static void FUN_8001fb44(final List<FileData> files, final String soundName, final int param) {
    final SoundFile sound = soundFiles_800bcf80[11];
    sound.name = soundName;
    sound.used_00 = true;

    sound.charId_02 = files.get(0).readShort(0);

    //LAB_8001fbcc
    setSpuDmaCompleteCallback(null);
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x2_1f70);

    currentSequenceData_800bd0f8 = loadSssq(sound.playableSound_10, new Sssq(files.get(1)));
    setSequenceVolume(40);
    melbuMusicLoaded_800bd781.set(false);
    _800bd0f0.setu(0x2L);

    if(param == 0) {
      FUN_8001ddd8();
    } else {
      //LAB_8001fbc4
      FUN_8001fc54();
    }
  }

  @Method(0x8001fc54L)
  public static void FUN_8001fc54() {
    throw new RuntimeException("Not yet implemented");
  }

  @Method(0x8001fcf4L)
  public static void loadEncounterSoundEffects(final int fileIndex) {
    loadedDrgnFiles_800bcf78.oru(0x4000L);
    // Example file: 700
    loadDrgnDir(0, fileIndex, Scus94491BpeSegment::encounterSoundEffectsLoaded);
  }

  @Method(0x8001fd4cL)
  public static void encounterSoundEffectsLoaded(final List<FileData> files) {
    final EncounterSoundEffects10 encounterSoundEffects = encounterSoundEffects_800bd610;
    encounterSoundEffects._00 = 2;

    encounterSoundEffects.unknown = files.get(0).readUShort(0);
    encounterSoundEffects.soundFileIndex = files.get(1).readUShort(0);
    encounterSoundEffects.sssq_08 = new Sssq(files.get(2));

    Scus94491BpeSegment_8004.setSequenceVolume(encounterSoundEffects.sequenceData_0c, 40);
    encounterSoundEffects._00 = 2;
    loadedDrgnFiles_800bcf78.and(0xffff_bfffL);
  }

  @Method(0x8001fe28L)
  public static FlowControl FUN_8001fe28(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001ff74L)
  public static void btldLoadEncounterSoundEffectsAndMusic() {
    loadSupportOverlay(1, Scus94491BpeSegment::loadEncounterSoundsAndMusic);
  }

  @Method(0x8001ffb0L)
  public static long getLoadedDrgnFiles() {
    return loadedDrgnFiles_800bcf78.get();
  }

  @Method(0x8001ffc0L)
  public static FlowControl scriptGetLoadedSoundFiles(final RunningScript<?> script) {
    script.params_20[0].set((int)loadedDrgnFiles_800bcf78.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ffdcL)
  public static FlowControl scriptUnloadSoundFile(final RunningScript<?> script) {
    unloadSoundFile(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }
}
