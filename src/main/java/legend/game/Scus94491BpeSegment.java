package legend.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import javafx.application.Application;
import javafx.application.Platform;
import legend.core.Config;
import legend.core.DebugHelper;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.Gpu;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.GpuCommandSetMaskBit;
import legend.core.gpu.Rect4i;
import legend.core.memory.Method;
import legend.core.opengl.MatrixStack;
import legend.core.opengl.Obj;
import legend.core.opengl.ScissorStack;
import legend.core.spu.Voice;
import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.combat.environment.StageData2c;
import legend.game.debugger.Debugger;
import legend.game.inventory.WhichMenu;
import legend.game.modding.events.RenderEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.Param;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.sound.EncounterSoundEffects10;
import legend.game.sound.PlayableSound0c;
import legend.game.sound.QueuedSound28;
import legend.game.sound.SoundFile;
import legend.game.sound.SoundFileIndices;
import legend.game.sound.SpuStruct08;
import legend.game.sound.Sshd;
import legend.game.sound.Sssq;
import legend.game.types.CharacterData2c;
import legend.game.types.Flags;
import legend.game.types.McqHeader;
import legend.game.types.OverlayStruct;
import legend.game.types.Struct0e;
import legend.game.types.Struct10;
import legend.game.types.TextboxBorderMetrics0c;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCREENS;
import static legend.core.GameEngine.SCRIPTS;
import static legend.core.GameEngine.SEQUENCER;
import static legend.core.GameEngine.SPU;
import static legend.core.GameEngine.legacyUi;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020ed8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bb38;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bda4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c178;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c184;
import static legend.game.Scus94491BpeSegment_8002.copyPlayingSounds;
import static legend.game.Scus94491BpeSegment_8002.handleTextboxAndText;
import static legend.game.Scus94491BpeSegment_8002.loadAndRenderMenus;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.renderTextboxes;
import static legend.game.Scus94491BpeSegment_8002.renderUi;
import static legend.game.Scus94491BpeSegment_8002.sssqResetStuff;
import static legend.game.Scus94491BpeSegment_8002.unloadEncounterSoundEffects;
import static legend.game.Scus94491BpeSegment_8003.GsInitGraph;
import static legend.game.Scus94491BpeSegment_8003.GsSetDrawBuffClip;
import static legend.game.Scus94491BpeSegment_8003.GsSetDrawBuffOffset;
import static legend.game.Scus94491BpeSegment_8003.GsSortClear;
import static legend.game.Scus94491BpeSegment_8003.GsSwapDispBuff;
import static legend.game.Scus94491BpeSegment_8003.setDrawOffset;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004._8004dd48;
import static legend.game.Scus94491BpeSegment_8004._8004f658;
import static legend.game.Scus94491BpeSegment_8004._8004f6e4;
import static legend.game.Scus94491BpeSegment_8004.battleStartDelayTicks_8004f6ec;
import static legend.game.Scus94491BpeSegment_8004.changeSequenceVolumeOverTime;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8004.engineStateFunctions_8004e29c;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.freeSequence;
import static legend.game.Scus94491BpeSegment_8004.gameStateOverlays_8004dbc0;
import static legend.game.Scus94491BpeSegment_8004.getSequenceFlags;
import static legend.game.Scus94491BpeSegment_8004.height_8004dd34;
import static legend.game.Scus94491BpeSegment_8004.initSpu;
import static legend.game.Scus94491BpeSegment_8004.loadSshdAndSoundbank;
import static legend.game.Scus94491BpeSegment_8004.loadSssq;
import static legend.game.Scus94491BpeSegment_8004.previousEngineState_8004dd28;
import static legend.game.Scus94491BpeSegment_8004.reinitOrderingTableBits_8004dd38;
import static legend.game.Scus94491BpeSegment_8004.setMainVolume;
import static legend.game.Scus94491BpeSegment_8004.setMaxSounds;
import static legend.game.Scus94491BpeSegment_8004.setSoundSequenceVolume;
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
import static legend.game.Scus94491BpeSegment_8004.stopSoundsAndSequences;
import static legend.game.Scus94491BpeSegment_8004.swapDisplayBuffer_8004dd40;
import static legend.game.Scus94491BpeSegment_8004.syncFrame_8004dd3c;
import static legend.game.Scus94491BpeSegment_8004.width_8004dd34;
import static legend.game.Scus94491BpeSegment_8005.charSlotSpuOffsets_80050190;
import static legend.game.Scus94491BpeSegment_8005.characterSoundFileIndices_800500f8;
import static legend.game.Scus94491BpeSegment_8005.combatMusicFileIndices_800501bc;
import static legend.game.Scus94491BpeSegment_8005.combatSoundEffectsTypes_8005019c;
import static legend.game.Scus94491BpeSegment_8005.monsterSoundFileIndices_800500e8;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.clearRed_8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bc9a8;
import static legend.game.Scus94491BpeSegment_800b._800bd0f0;
import static legend.game.Scus94491BpeSegment_800b._800bd710;
import static legend.game.Scus94491BpeSegment_800b._800bd714;
import static legend.game.Scus94491BpeSegment_800b._800bd740;
import static legend.game.Scus94491BpeSegment_800b.battleDissolveTicks;
import static legend.game.Scus94491BpeSegment_800b.battleFlags_800bc960;
import static legend.game.Scus94491BpeSegment_800b.clearBlue_800babc0;
import static legend.game.Scus94491BpeSegment_800b.clearGreen_800bb104;
import static legend.game.Scus94491BpeSegment_800b.currentSequenceData_800bd0f8;
import static legend.game.Scus94491BpeSegment_800b.dissolveDarkening_800bd700;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.encounterSoundEffects_800bd610;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.musicFileIndex_800bd0fc;
import static legend.game.Scus94491BpeSegment_800b.musicLoaded_800bd782;
import static legend.game.Scus94491BpeSegment_800b.playingSoundsBackup_800bca78;
import static legend.game.Scus94491BpeSegment_800b.postCombatMainCallbackIndex_800bc91c;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.queuedSounds_800bd110;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.sequenceVolume_800bd108;
import static legend.game.Scus94491BpeSegment_800b.soundFiles_800bcf80;
import static legend.game.Scus94491BpeSegment_800b.sssqTempoScale_800bd100;
import static legend.game.Scus94491BpeSegment_800b.sssqTempo_800bd104;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.sequenceData_800c4ac8;
import static legend.game.combat.environment.StageData.stageData_80109a98;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F12;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F5;

public final class Scus94491BpeSegment {
  private Scus94491BpeSegment() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment.class);

  public static int orderingTableBits_1f8003c0;
  public static int zShift_1f8003c4;
  public static int orderingTableSize_1f8003c8;
  public static int zMax_1f8003cc;
  public static int zMin;

  public static int centreScreenX_1f8003dc;
  public static int centreScreenY_1f8003de;
  public static int displayWidth_1f8003e0;
  public static int displayHeight_1f8003e4;
  public static int zOffset_1f8003e8;
  public static int tmdGp0Tpage_1f8003ec;
  public static int tmdGp0CommandId_1f8003ee;

  public static BattlePreloadedEntities_18cb0 battlePreloadedEntities_1f8003f4;
  public static float projectionPlaneDistance_1f8003f8;

  public static final int[] levelUpUs_8001032c = {0xc8, 0xd0, 0xd8, 0xd0, 0xc8, 0xe0, 0xe8, 0xf0};
  public static final int[] levelUpOffsets_80010334 = {8, 8, 8, 8, 15, 8, 8, 0};

  public static final Rect4i[] rectArray28_80010770 = {
    new Rect4i(832, 256, 64, 160), new Rect4i(832, 480, 16, 4), new Rect4i(64, 0, 64, 16), new Rect4i(0, 0, 0, 0),
    new Rect4i(128, 0, 64, 16), new Rect4i(0, 0, 0, 0), new Rect4i(192, 0, 64, 16), new Rect4i(0, 0, 0, 0),
    new Rect4i(256, 0, 64, 16), new Rect4i(0, 0, 0, 0), new Rect4i(0, 496, 64, 16), new Rect4i(0, 0, 0, 0),
    new Rect4i(64, 496, 64, 16), new Rect4i(0, 0, 0, 0), new Rect4i(128, 496, 64, 16), new Rect4i(0, 0, 0, 0),
    new Rect4i(192, 496, 64, 16), new Rect4i(0, 0, 0, 0), new Rect4i(256, 496, 64, 16), new Rect4i(0, 0, 0, 0),
    new Rect4i(832, 256, 64, 240), new Rect4i(896, 480, 16, 4), new Rect4i(832, 256, 64, 240), new Rect4i(960, 480, 16, 4),
    new Rect4i(896, 256, 16, 46), new Rect4i(832, 484, 16, 1), new Rect4i(912, 256, 28, 14), new Rect4i(1008, 484, 16, 1),
  };

  public static final TextboxBorderMetrics0c[] textboxBorderMetrics_800108b0 = {
    new TextboxBorderMetrics0c(0, 0, 0, 0, 6, 8),
    new TextboxBorderMetrics0c(1, 1, 48, 0, 6, 8),
    new TextboxBorderMetrics0c(2, 2, 0, 32, 6, 8),
    new TextboxBorderMetrics0c(3, 3, 48, 32, 6, 8),
    new TextboxBorderMetrics0c(0, 1, 16, 0, -4, 8),
    new TextboxBorderMetrics0c(0, 2, 0, 16, 6, -4),
    new TextboxBorderMetrics0c(1, 3, 48, 16, 6, -4),
    new TextboxBorderMetrics0c(2, 3, 16, 32, -4, 8),
  };

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

  private static final RenderEvent RENDER_EVENT = new RenderEvent();

  @Method(0x80011e1cL)
  public static void gameLoop() {
    RENDERER.events().onKeyPress((window, key, scancode, mods) -> {
      // Add killswitch in case sounds get stuck on
      if(key == GLFW_KEY_DELETE) {
        for(final Voice voice : SPU.voices) {
          voice.volumeLeft.set(0);
          voice.volumeRight.set(0);
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

      if(key == GLFW_KEY_F5 && currentEngineState_8004dd04 instanceof final Battle battle) {
        battle.endBattle();
      }
    });

    final MatrixStack matrixStack = new MatrixStack();
    final ScissorStack scissorStack = new ScissorStack(RENDERER.window());

    legacyUi = true;

    RENDERER.setRenderCallback(() -> {
      if(legacyUi) {
        GPU.startFrame();
      }

      if(engineState_8004dd20.isInGame()) {
        gameState_800babc8.timestamp_a0 += vsyncMode_8007a3b8;
      }

      final int frames = Math.max(1, vsyncMode_8007a3b8);
      RENDERER.window().setFpsLimit((60 / frames) * Config.getGameSpeedMultiplier());

      loadQueuedOverlay();

      renderUi();

      if(currentEngineState_8004dd04 != null) {
        currentEngineState_8004dd04.tick();
      }

      EVENTS.postEvent(RENDER_EVENT);

      SCREENS.render(RENDERER, matrixStack, scissorStack);

      SCRIPTS.tick();

      if(currentEngineState_8004dd04 != null) {
        currentEngineState_8004dd04.postScriptTick();
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
      endFrame();

      if(legacyUi) {
        GPU.endFrame();
      }
    });

    RENDERER.events().onShutdown(() -> {
      stopSound();
      SPU.stop();
      Platform.exit();
    });
  }

  private static final int SOUND_TPS = 60;
  private static final int NANOS_PER_TICK = 1_000_000_000 / SOUND_TPS;
  private static boolean soundRunning;

  public static void startSound() {
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

      if(engineState_8004dd20 == EngineStateEnum.COMBAT_06) { // Starting combat
        clearCombatVars();
      }
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
    RENDERER.allowWidescreen = currentEngineState_8004dd04.allowsWidescreen();
    RENDERER.allowHighQualityProjection = currentEngineState_8004dd04.allowsHighQualityProjection();
    RENDERER.updateProjections();
  }

  @Method(0x80012df8L)
  public static void endFrame() {
    GPU.queueCommand(3, new GpuCommandSetMaskBit(false, Gpu.DRAW_PIXELS.ALWAYS));
    GPU.queueCommand(orderingTableSize_1f8003c8 - 1, new GpuCommandSetMaskBit(true, Gpu.DRAW_PIXELS.ALWAYS));

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
    final int orderingTableBits = reinitOrderingTableBits_8004dd38;

    clearBlue_800babc0 = 0;
    clearGreen_800bb104 = 0;
    clearRed_8007a3a8 = 0;

    orderingTableBits_1f8003c0 = orderingTableBits;
    zShift_1f8003c4 = 14 - orderingTableBits;
    orderingTableSize_1f8003c8 = 1 << orderingTableBits;
    zMax_1f8003cc = (1 << orderingTableBits) - 2;
    GPU.updateOrderingTableSize(orderingTableSize_1f8003c8);

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
    GsSortClear(clearRed_8007a3a8, clearGreen_800bb104, clearBlue_800babc0);
    RENDERER.setClearColour(clearRed_8007a3a8 / 255.0f, clearGreen_800bb104 / 255.0f, clearBlue_800babc0 / 255.0f);
  }

  @Method(0x80013200L)
  public static void resizeDisplay(final int width, final int height) {
    if(width != displayWidth_1f8003e0) {
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
    if(orderingTableBits_1f8003c0 != orderingTableBits) {
      syncFrame_8004dd3c = Scus94491BpeSegment::syncFrame_reinit;
      reinitOrderingTableBits_8004dd38 = orderingTableBits;
    }

    //LAB_80013274
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

  /**
   * @param effectType 1 - fade out, 2 - fade in
   */
  @Method(0x800136dcL)
  public static void startFadeEffect(final int effectType, final int frames) {
    //LAB_800136f4
    fullScreenEffect_800bb140.type_00 = effectType;
    fullScreenEffect_800bb140.totalFrames_08 = frames > 0 ? frames : 15;
    fullScreenEffect_800bb140.startTime_04 = RENDERER.getVsyncCount();

    if(_8004dd48[effectType] == 2) {
      fullScreenEffect_800bb140.blue1_0c = 0;
      fullScreenEffect_800bb140.green1_10 = 0;
      fullScreenEffect_800bb140.blue0_14 = 0;
      fullScreenEffect_800bb140.red1_18 = 0;
      fullScreenEffect_800bb140.green0_1c = 0;
      fullScreenEffect_800bb140.red0_20 = 0;
    }

    fullScreenEffect_800bb140._24 = _8004dd48[effectType];

    //LAB_80013768
  }

  /**
   * This handles the lightning flashes/darkening, the scene fade-in, etc.
   */
  @Method(0x80013778L)
  public static void handleFullScreenEffects() {
    final int v1 = Math.min(fullScreenEffect_800bb140.totalFrames_08, (RENDERER.getVsyncCount() - fullScreenEffect_800bb140.startTime_04) / 2);

    //LAB_800137d0
    final int colour;
    if(fullScreenEffect_800bb140.totalFrames_08 == 0) {
      colour = 0;
    } else {
      final int a1 = fullScreenEffect_800bb140._24;
      if(a1 == 0) {
        colour = 0;
      } else if(a1 == 1) {
        //LAB_80013818
        colour = v1 * 255 / fullScreenEffect_800bb140.totalFrames_08;
        //LAB_80013808
      } else if(a1 == 2) { // a1 == 2
        //LAB_8001383c
        colour = v1 * 255 / fullScreenEffect_800bb140.totalFrames_08 ^ 0xff;

        if(colour == 0) {
          //LAB_80013874
          fullScreenEffect_800bb140._24 = 0;
        }
      } else {
        fullScreenEffect_800bb140.type_00 = 0;
        fullScreenEffect_800bb140._24 = 0;
        colour = 0;
      }
    }

    //LAB_80013880
    //LAB_80013884
    fullScreenEffect_800bb140.currentColour_28 = colour;

    if(colour != 0) {
      //LAB_800138f0
      //LAB_80013948
      switch(fullScreenEffect_800bb140.type_00) {
        case 1, 2 -> drawFullScreenRect(colour, Translucency.B_MINUS_F);
        case 3, 4 -> drawFullScreenRect(colour, Translucency.B_PLUS_F);

        case 5 -> {
          for(int s1 = 0; s1 < 8; s1++) {
            //LAB_800138f8
            for(int s0 = 0; s0 < 6; s0++) {
              drawBoxagonEffect(colour - (12 - (s0 + s1)) * 11, s1, s0);
            }
          }
        }

        case 6 -> {
          for(int s1 = 0; s1 < 8; s1++) {
            //LAB_80013950
            for(int s0 = 0; s0 < 6; s0++) {
              drawBoxagonEffect(colour - (s1 + s0) * 11, s1, s0);
            }
          }
        }
      }
    }

    //caseD_0
    //LAB_80013994
    // This causes the bright flash of light from the lightning, etc.
    if(fullScreenEffect_800bb140.red0_20 != 0 || fullScreenEffect_800bb140.green0_1c != 0 || fullScreenEffect_800bb140.blue0_14 != 0) {
      fullScreenEffect_800bb140.transforms.scaling(displayWidth_1f8003e0, displayHeight_1f8003e4, 1.0f);
      fullScreenEffect_800bb140.transforms.transfer.set(0.0f, 0.0f, 30.0f);

      //LAB_800139c4
      RENDERER.queueOrthoModel(RENDERER.plainQuads.get(Translucency.B_PLUS_F), fullScreenEffect_800bb140.transforms)
        .colour(fullScreenEffect_800bb140.red0_20 / 255.0f, fullScreenEffect_800bb140.green0_1c / 255.0f, fullScreenEffect_800bb140.blue0_14 / 255.0f);
    }

    //LAB_80013adc

    // This causes the screen darkening from the lightning, etc.
    if(fullScreenEffect_800bb140.red1_18 != 0 || fullScreenEffect_800bb140.green1_10 != 0 || fullScreenEffect_800bb140.blue1_0c != 0) {
      fullScreenEffect_800bb140.transforms.scaling(displayWidth_1f8003e0, displayHeight_1f8003e4, 1.0f);
      fullScreenEffect_800bb140.transforms.transfer.set(0.0f, 0.0f, 30.0f);

      //LAB_80013b10
      RENDERER.queueOrthoModel(RENDERER.plainQuads.get(Translucency.B_MINUS_F), fullScreenEffect_800bb140.transforms)
        .colour(fullScreenEffect_800bb140.red1_18 / 255.0f, fullScreenEffect_800bb140.green1_10 / 255.0f, fullScreenEffect_800bb140.blue1_0c / 255.0f);
    }

    //LAB_80013c20
  }

  @Method(0x80013c3cL)
  public static void drawFullScreenRect(final int colour, final Translucency transMode) {
    fullScreenEffect_800bb140.transforms.scaling(displayWidth_1f8003e0, displayHeight_1f8003e4, 1.0f);
    fullScreenEffect_800bb140.transforms.transfer.set(0.0f, 0.0f, 120.0f);

    RENDERER.queueOrthoModel(RENDERER.plainQuads.get(transMode), fullScreenEffect_800bb140.transforms)
      .monochrome(colour / 255.0f);
  }

  @Method(0x80013d78L)
  public static void drawBoxagonEffect(final int angle, final int x, final int y) {
    if(angle > 0) {
      final int sin;
      final int cos;
      if(angle >= 124) {
        sin = 20;
        cos = 0;
      } else {
        final int theta = angle * 0x400 / 123;
        sin = rsin(theta) * 20 / 0x1000;
        cos = rcos(theta) / 17;
      }

      //LAB_80013e18
      final int x0 = x * 40;
      final int y0 = y * 40;

      GPU.queueCommand(30, new GpuCommandPoly(4)
        .rgb(cos, cos, cos)
        .pos(0, x0 - (sin - 20) - centreScreenX_1f8003dc, y0 - (sin - 20) - centreScreenY_1f8003de)
        .pos(1, x0 - (centreScreenX_1f8003dc - 40), y0 - centreScreenY_1f8003de)
        .pos(2, x0 - centreScreenX_1f8003dc, y0 - (centreScreenY_1f8003de - 40))
        .pos(3, x0 + sin + 20 - centreScreenX_1f8003dc, y0 + sin + 20 - centreScreenY_1f8003de)
      );
    }

    //LAB_80013f1c
  }

  public static void loadFile(final String file, final Consumer<FileData> onCompletion) {
    final StackWalker.StackFrame frame = StackWalker.getInstance().walk(frames -> frames
      .skip(1)
      .findFirst())
      .get();

    LOGGER.info("Loading file %s from %s.%s(%s:%d)", file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    Unpacker.loadFile(file, onCompletion);
  }

  public static void loadDir(final String dir, final Consumer<List<FileData>> onCompletion) {
    final StackWalker.StackFrame frame = StackWalker.getInstance().walk(frames -> frames
      .skip(1)
      .findFirst())
      .get();

    LOGGER.info("Loading dir %s from %s.%s(%s:%d)", dir, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    Unpacker.loadDirectory(dir, onCompletion);
  }

  public static void loadDrgnFiles(int drgnBinIndex, final Consumer<List<FileData>> onCompletion, final String... files) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, String.join(", ", files), frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    final String[] paths = new String[files.length];
    for(int i = 0; i < files.length; i++) {
      paths[i] = "SECT/DRGN%d.BIN/%s".formatted(drgnBinIndex, files[i]);
    }

    Unpacker.loadFiles(onCompletion, paths);
  }

  public static void loadDrgnFile(final int drgnBinIndex, final int file, final Consumer<FileData> onCompletion) {
    loadDrgnFile(drgnBinIndex, String.valueOf(file), onCompletion);
  }

  public static void loadDrgnFileSync(final int drgnBinIndex, final int file, final Consumer<FileData> onCompletion) {
    loadDrgnFileSync(drgnBinIndex, String.valueOf(file), onCompletion);
  }

  public static void loadDrgnFile(int drgnBinIndex, final String file, final Consumer<FileData> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    Unpacker.loadFile("SECT/DRGN%d.BIN/%s".formatted(drgnBinIndex, file), onCompletion);
  }

  public static void loadDrgnFileSync(int drgnBinIndex, final String file, final Consumer<FileData> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Unpacker.loadFile("SECT/DRGN%d.BIN/%s".formatted(drgnBinIndex, file)));
  }

  public static void loadDrgnDir(int drgnBinIndex, final int directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %d from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    Unpacker.loadDirectory("SECT/DRGN%d.BIN/%d".formatted(drgnBinIndex, directory), onCompletion);
  }

  public static void loadDrgnDirSync(int drgnBinIndex, final String directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %s from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Unpacker.loadDirectory("SECT/DRGN%d.BIN/%s".formatted(drgnBinIndex, directory)));
  }

  public static void loadDrgnDirSync(int drgnBinIndex, final int directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %d from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Unpacker.loadDirectory("SECT/DRGN%d.BIN/%d".formatted(drgnBinIndex, directory)));
  }

  public static void loadDrgnDir(int drgnBinIndex, final String directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %s from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    Unpacker.loadDirectory("SECT/DRGN%d.BIN/%s".formatted(drgnBinIndex, directory), onCompletion);
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
      final CharacterData2c charData = gameState_800babc8.charData_32c[0];
      charData.dlevelXp_0e = 0x7fff;
      charData.dlevel_13 = 5;
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

  @ScriptDescription("Starts a full-screen fade effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The type of effect to start")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "frames", description = "The number of frames until the effect finishes")
  @Method(0x8001751cL)
  public static FlowControl scriptStartFadeEffect(final RunningScript<?> script) {
    startFadeEffect(script.params_20[0].get(), Math.max(1, script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x80017564L)
  @ScriptDescription("Rewinds if there are files currently loading; pauses otherwise")
  public static FlowControl scriptWaitForFilesToLoad(final RunningScript<?> script) {
    final int loadingCount = Unpacker.getLoadingFileCount();

    if(loadingCount != 0) {
      LOGGER.info("%d files still loading; pausing and rewinding", loadingCount);
      return FlowControl.PAUSE_AND_REWIND;
    }

    LOGGER.info("No files loading");
    return FlowControl.PAUSE;
  }

  @ScriptDescription("Something related to rumble")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "joypadIndex")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x80017584L)
  public static FlowControl FUN_80017584(final RunningScript<?> script) {
    FUN_8002bb38(script.params_20[0].get(), script.params_20[1].get());
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
    if((gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
      gameState_800babc8.charData_32c[0].dlevel_13 = 5;
      gameState_800babc8.charData_32c[0].dlevelXp_0e = 0x7fff;
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

  @ScriptDescription("Something related to rumble")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @Method(0x80017688L)
  public static FlowControl FUN_80017688(final RunningScript<?> script) {
    FUN_8002bda4(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related to rumble")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800176c0L)
  public static FlowControl FUN_800176c0(final RunningScript<?> script) {
    FUN_8002c178(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related to rumble")
  @Method(0x800176ecL)
  public static FlowControl FUN_800176ec(final RunningScript<?> script) {
    FUN_8002c184();
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
  public static void renderPostCombatScreen() {
    if(whichMenu_800bdc38 != WhichMenu.NONE_0) {
      loadAndRenderMenus();
    }

    // There used to be code to preload SMAP while the post-combat screen is still up. I removed it because it only takes a few milliseconds to load in SC.

    //LAB_8001852c
    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      pregameLoadingStage_800bb10c++;
    }

    //LAB_80018644
  }

  @Method(0x80018944L)
  public static void waitForFilesToLoad() {
    if(Unpacker.getLoadingFileCount() == 0) {
      pregameLoadingStage_800bb10c++;
    }
  }

  @Method(0x80018998L)
  public static void nextLoadingStage() {
    pregameLoadingStage_800bb10c++;
  }

  @Method(0x800189b0L)
  public static void transitionBackFromBattle() {
    if(Unpacker.getLoadingFileCount() == 0) {
      //LAB_800189e4
      //LAB_800189e8
      unloadEncounterSoundEffects();
      pregameLoadingStage_800bb10c = 0;
      vsyncMode_8007a3b8 = 2;
      engineStateOnceLoaded_8004dd24 = postCombatMainCallbackIndex_800bc91c;
    }

    //LAB_80018a4c
  }

  @Method(0x80018a5cL)
  public static void renderButtonPressHudElement(final int x, final int y, final int leftU, final int topV, final int rightU, final int bottomV, final int clutOffset, @Nullable final Translucency transMode, final int brightness, final int widthStretch, final int heightStretch) {
    final int w = Math.abs(rightU - leftU);
    final int h = Math.abs(bottomV - topV);

    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .monochrome(brightness);

    if(transMode != null) {
      cmd.translucent(transMode);
    }

    //LAB_80018b38
    if(widthStretch != 0x1000 || heightStretch != 0x1000) {
      //LAB_80018b90
      final int left = x + w / 2;
      final int top = y + h / 2;
      final int offsetX = w * widthStretch / 2 >> 12;
      final int offsetY = h * heightStretch / 2 >> 12;

      cmd
        .pos(0, left - offsetX, top - offsetY)
        .pos(1, left + offsetX, top - offsetY)
        .pos(2, left - offsetX, top + offsetY)
        .pos(3, left + offsetX, top + offsetY);
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

    final int clutOffsetX = clutOffset / 16;
    final int clutOffsetY = clutOffset % 16;
    final int clutY;
    final int clutX;
    if(clutOffsetX >= 4) {
      clutX = clutOffsetX * 16 + 832;
      clutY = clutOffsetY + 304;
    } else {
      clutX = clutOffsetX * 16 + 704;
      clutY = clutOffsetY + 496;
    }

    //LAB_80018cf0
    cmd
      .bpp(Bpp.BITS_4)
      .clut(clutX & 0x3f0, clutY)
      .vramPos(704, 256);

    GPU.queueCommand(2, cmd);
  }

  @Method(0x80018d60L)
  public static void renderButtonPressHudTexturedRect(final int x, final int y, final int u, final int v, final int width, final int height, final int clutOffset, @Nullable final Translucency transMode, final int brightness, final int stretch) {
    renderButtonPressHudElement(x, y, u, v, u + width, v + height, clutOffset, transMode, brightness, stretch, stretch);
  }

  @Method(0x80018decL)
  public static void renderDivineDragoonAdditionPressIris(final int x, final int y, final int u, final int v, final int width, final int height, final int clutOffset, @Nullable final Translucency transMode, final int brightness, final int widthStretch, final int heightStretch) {
    renderButtonPressHudElement(x, y, u, v, u + width, v + height, clutOffset, transMode, brightness, widthStretch, heightStretch);
  }

  @Method(0x80018e84L)
  public static void FUN_80018e84() {
    final int[] sp0x30 = {0x42, 0x43, 0x02000802, 0x000a0406}; // These last two entries must be wrong but they might be unused cause I don't see anything wrong

    //LAB_80018f04
    Struct10 s1 = _8004f658;
    while(s1 != null) {
      do {
        s1._03--;

        if(s1._03 != 0) {
          break;
        }

        if(s1.next_08 == null) {
          //LAB_80018f48
          _8004f658 = s1.prev_0c;
        } else {
          //LAB_80018f50
          s1.next_08.prev_0c = null;
        }

        //LAB_80018f54
        final Struct10 s0 = s1.prev_0c;

        if(s0 != null) {
          if(s1.next_08 == null) {
            s0.next_08 = null;
            _8004f658 = s1.prev_0c;
          } else {
            //LAB_80018f84
            s1.prev_0c.next_08 = s1.next_08;
            s1.next_08.prev_0c = s1.prev_0c;
          }
        }

        //LAB_80018fa0
        if(s0 == null) {
          return;
        }
        s1 = s0;
      } while(true);

      //LAB_80018fd0
      //LAB_80018fd4
      for(int s2 = 0; s2 < 8; s2++) {
        final Struct0e s0 = s1.ptr_04[s2];

        s0._08++;

        if(s0._08 >= 0) {
          final int v1 = s0.clutAndTranslucency_0c >>> 8 & 0xf;

          if(v1 == 0) {
            //LAB_80019040
            s0._09--;

            if(s0._04 != 0) {
              s0._04 -= 0x492;
              s0._06 -= 0x492;
            } else {
              //LAB_80019084
              s0.clutAndTranslucency_0c &= 0x8fff;
            }

            //LAB_80019094
            if(s0._09 == 0) {
              s0.clutAndTranslucency_0c &= 0x7fff;
              s0._08 = 0;
              s0.clutAndTranslucency_0c &= 0xf0ff;
              s0.clutAndTranslucency_0c |= ((s0.clutAndTranslucency_0c >>> 8 & 0xf) + 1 & 0xf) << 8;
            }
          } else if(v1 == 1) {
            //LAB_800190d4
            s0.clutAndTranslucency_0c = sp0x30[s0.clutAndTranslucency_0c >>> 15];

            if(s0._08 == 10) {
              s0._09 = (byte)(simpleRand() % 10 + 2);
              s0.clutAndTranslucency_0c &= 0xf0ff;
              s0.clutAndTranslucency_0c |= ((s0.clutAndTranslucency_0c >>> 8 & 0xf) + 1 & 0xf) << 8;
            }
            //LAB_80019028
          } else if(v1 == 2) {
            //LAB_80019164
            s0._09--;

            if(s0._09 == 0) {
              //LAB_80019180
              s0._09 = 8;
              s0.clutAndTranslucency_0c &= 0xf0ff;
              s0.clutAndTranslucency_0c |= ((s0.clutAndTranslucency_0c >>> 8 & 0xf) + 1 & 0xf) << 8;
            }
          } else if(v1 == 3) {
            //LAB_800191b8
            s0._09--;

            if((s0._09 & 0x1) != 0) {
              s0.y_02++;
            }

            //LAB_800191e4
            s0._06 -= 0x200;

            if(s0._09 == 0) {
              s0._08 = -100;
            }
          }

          //LAB_80019208
          renderButtonPressHudElement(
            s0.x_00,
            s0.y_02,
            s0.u_0b,
            64,
            s0.u_0b + 7,
            79,
            s0.clutAndTranslucency_0c,
            Translucency.of((s0.clutAndTranslucency_0c >>> 12 & 0x7) - 1),
            0xff,
            s0._04 + 0x1000,
            s0._06 + 0x1000
          );
        }
      }

      s1 = s1.prev_0c;
    }

    //LAB_800192b4
  }

  @Method(0x800192d8L)
  public static void drawLevelUp(int x, final int y) {
    final Struct10 s0 = new Struct10();
    s0._00 = 0x61;
    s0._01 = 0x6c;
    s0._02 = 0x64;
    s0._03 = 0x34;
    s0.prev_0c = _8004f658;

    if(s0.prev_0c != null) {
      s0.prev_0c.next_08 = s0;
    }

    //LAB_800193b4
    _8004f658 = s0;

    //LAB_800193dc
    for(int i = 0; i < 8; i++) {
      final Struct0e v1 = s0.ptr_04[i];
      v1.x_00 = (short)(x - 6);
      v1.y_02 = (short)(y - 16);
      v1._04 = 0x1ffe;
      v1._06 = 0x1ffe;
      v1._08 = (byte)~i;
      v1._09 = (byte)(20 - i);
      v1.u_0b = levelUpUs_8001032c[i];
      v1.clutAndTranslucency_0c = 0x204a;
      x += levelUpOffsets_80010334[i];
    }
  }

  @Method(0x80019470L)
  public static void FUN_80019470() {
    _8004f658 = null;
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
    FUN_8001aa90();

    queuedSounds_800bd110.clear();

    for(int i = 0; i < 13; i++) {
      soundFiles_800bcf80[i].used_00 = false;
    }

    encounterSoundEffects_800bd610._00 = 0;

    sssqTempoScale_800bd100 = 0x100;
  }

  @Method(0x80019710L)
  public static void prepareOverlay() {
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
        setMainVolume(0x7f, 0x7f);
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
        if((loadedDrgnFiles_800bcf78.get() & 0x2L) != 0 || engineState_8004dd20 != EngineStateEnum.SUBMAP_05) {
          return;
        }
      }

      case 9 -> {
        //LAB_80019bd4
        if((loadedDrgnFiles_800bcf78.get() & 0x4L) != 0 || engineState_8004dd20 != EngineStateEnum.COMBAT_06) {
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
        if((loadedDrgnFiles_800bcf78.get() & 0x8000L) != 0 || engineState_8004dd20 != EngineStateEnum.WORLD_MAP_08) {
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
  public static void stopSound(final SoundFile soundFile, final int soundIndex, final int mode) {
    //LAB_80019cc4
    for(int sequenceIndex = 0; sequenceIndex < 24; sequenceIndex++) {
      final SpuStruct08 s0 = _800bc9a8[sequenceIndex];

      if(s0.soundFile_02 == soundFile && s0.soundIndex_03 == soundIndex) {
        stopSoundSequence(sequenceData_800c4ac8[sequenceIndex], true);

        if((mode & 0x1) == 0) {
          break;
        }
      }
    }

    //LAB_80019d0c
    //LAB_80019d1c
    queuedSounds_800bd110.removeIf(queuedSound -> queuedSound.type_00 == 4 && queuedSound.soundIndex_0c == soundIndex && queuedSound.soundFile_08 == soundFile);

    if((mode & 0x2) != 0) {
      //LAB_80019d84
      queuedSounds_800bd110.removeIf(queuedSound -> (queuedSound.type_00 == 3 && (queuedSound.repeatDelayTotal_20 != 0 || queuedSound.initialDelay_24 != 0) || queuedSound._1c != 0) && queuedSound.soundIndex_0c == soundIndex && queuedSound.soundFile_08 == soundFile);
    }

    //LAB_80019dfc
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
      struct08.bent_04 = playingSound.bent_04;
    }

    //LAB_8001a704
  }

  @Method(0x8001a714L)
  public static void playSound(final int type, final SoundFile soundFile, final int soundIndex, final QueuedSound28 playingSound, final PlayableSound0c playableSound, final SoundFileIndices soundFileIndices, final int a6, final short pitchShiftVolRight, final short pitchShiftVolLeft, final short pitch, final short repeatDelay, final short initialDelay, @Nullable final BattleEntity27c bent) {
    playingSound.type_00 = type;
    playingSound.bent_04 = bent;
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
    _800bd0f0 = 0;
  }

  @Method(0x8001aa90L)
  public static void FUN_8001aa90() {
    //LAB_8001aaa4
    for(int i = 0; i < 24; i++) {
      _800bc9a8[i]._00 = 0xffff;
    }
  }

  @ScriptDescription("Play a sound")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundFileIndex", description = "The sound file index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundIndex", description = "The sound index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "a2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "a3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "initialDelay", description = "The initial delay before the sound starts")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "repeatDelay", description = "The delay before a sound repeats")
  @Method(0x8001ab34L)
  public static FlowControl scriptPlaySound(final RunningScript<?> script) {
    playSound(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), (short)script.params_20[4].get(), (short)script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Stop a sound")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundFileIndex", description = "The sound file index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundIndex", description = "The sound index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode")
  @Method(0x8001ab98L)
  public static FlowControl scriptStopSound(final RunningScript<?> script) {
    stopSound(soundFiles_800bcf80[script.params_20[0].get()], script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ad18L)
  public static void stopAndResetSoundsAndSequences() {
    //LAB_8001ad2c
    queuedSounds_800bd110.clear();
    stopSoundsAndSequences(true);
  }

  @ScriptDescription("Stops all sounds and sequences")
  @Method(0x8001ad5cL)
  public static FlowControl scriptStopSoundsAndSequences(final RunningScript<?> script) {
    //LAB_8001ad70
    stopAndResetSoundsAndSequences();
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ada0L)
  public static void startCurrentMusicSequence() {
    startMusicSequence(currentSequenceData_800bd0f8);
  }

  @ScriptDescription("Starts the current music sequence")
  @Method(0x8001adc8L)
  public static FlowControl scriptStartCurrentMusicSequence(final RunningScript<?> script) {
    startCurrentMusicSequence();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Stops the current music sequence")
  @Method(0x8001ae18L)
  public static FlowControl scriptStopCurrentMusicSequence(final RunningScript<?> script) {
    stopMusicSequence(currentSequenceData_800bd0f8, 2);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Stops the current music sequence")
  @Method(0x8001ae68L)
  public static FlowControl scriptStopCurrentMusicSequence2(final RunningScript<?> script) {
    return scriptStopCurrentMusicSequence(script);
  }

  @Method(0x8001ae90L)
  public static void FUN_8001ae90() {
    if(_800bd0f0 == 2) {
      stopMusicSequence(currentSequenceData_800bd0f8, 0);
    }
  }

  @ScriptDescription("Stops the current music sequence if 800bd0f0 is 2")
  @Method(0x8001aec8L)
  public static FlowControl FUN_8001aec8(final RunningScript<?> script) {
    FUN_8001ae90();
    return FlowControl.CONTINUE;
  }

  @Method(0x8001af00L)
  public static void startEncounterSounds() {
    startMusicSequence(encounterSoundEffects_800bd610.sequenceData_0c);
  }

  @ScriptDescription("Starts the encounter sounds sequence")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x8001af34L)
  public static FlowControl scriptStartEncounterSounds(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Stops the encounter sounds sequence")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x8001afa4L)
  public static FlowControl scriptStopEncounterSounds(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Stops the encounter sounds sequence")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x8001b014L)
  public static FlowControl scriptStopEncounterSounds2(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Stops the encounter sounds sequence if 800bd0f0 is 2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x8001b094L)
  public static FlowControl FUN_8001b094(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Gets the current sequence's tempo scale")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "tempoScale", description = "The tempo scale")
  @Method(0x8001b0f0L)
  public static FlowControl scriptGetSssqTempoScale(final RunningScript<?> script) {
    script.params_20[0].set(sssqTempoScale_800bd100);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the current sequence's tempo scale")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "tempoScale", description = "The tempo scale")
  @Method(0x8001b118L)
  public static FlowControl scriptSetSssqTempoScale(final RunningScript<?> script) {
    sssqTempoScale_800bd100 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x8001b134L)
  public static FlowControl FUN_8001b134(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x8001b13cL)
  public static FlowControl FUN_8001b13c(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x8001b144L)
  public static FlowControl FUN_8001b144(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the main volume")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "left", description = "The left volume")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "right", description = "The right volume")
  @Method(0x8001b14cL)
  public static FlowControl scriptSetMainVolume(final RunningScript<?> script) {
    setMainVolume((short)script.params_20[0].get(), (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the sequence volume")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "volume", description = "The volume")
  @Method(0x8001b17cL)
  public static FlowControl scriptSetSequenceVolume(final RunningScript<?> script) {
    setSequenceVolume((short)script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b1a8L)
  public static void setSequenceVolume(final int volume) {
    Scus94491BpeSegment_8004.setSequenceVolume(currentSequenceData_800bd0f8, volume);
    sequenceVolume_800bd108 = volume;
  }

  @ScriptDescription("Gets the sequence volume")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "volume", description = "The volume")
  @Method(0x8001b1ecL)
  public static FlowControl scriptGetSequenceVolume(final RunningScript<?> script) {
    script.params_20[0].set(sequenceVolume_800bd108);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the volume for all sequences")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "volume", description = "The volume")
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

  @ScriptDescription("Fade in all sequenced audio")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fadeTime", description = "The number of ticks to fade in")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "volume", description = "The volume once the fade in has finished")
  @Method(0x8001b27cL)
  public static FlowControl scriptSssqFadeIn(final RunningScript<?> script) {
    sssqFadeIn((short)script.params_20[0].get(), (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Start the current sequence and fade it in")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fadeTime", description = "The number of ticks to fade in")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "volume", description = "The volume once the fade in has finished")
  @Method(0x8001b2acL)
  public static FlowControl scriptStartSequenceAndChangeVolumeOverTime(final RunningScript<?> script) {
    startSequenceAndChangeVolumeOverTime(currentSequenceData_800bd0f8, (short)script.params_20[0].get(), (short)script.params_20[1].get());
    sequenceVolume_800bd108 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Fade out all sequenced audio")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fadeTime", description = "The number of ticks to fade out")
  @Method(0x8001b310L)
  public static FlowControl scriptSssqFadeOut(final RunningScript<?> script) {
    sssqFadeOut((short)script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adjust the current sequence's volume over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fadeTime", description = "The number of ticks to fade for")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "volume", description = "The volume once the fade in has finished")
  @Method(0x8001b33cL)
  public static FlowControl scriptChangeSequenceVolumeOverTime(final RunningScript<?> script) {
    changeSequenceVolumeOverTime(currentSequenceData_800bd0f8, (short)script.params_20[0].get(), (short)script.params_20[1].get());
    sequenceVolume_800bd108 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get the current sequence's flags")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "flags", description = "The current sequence's flags")
  @Method(0x8001b3a0L)
  public static FlowControl scriptGetSequenceFlags(final RunningScript<?> script) {
    script.params_20[0].set(getSequenceFlags(currentSequenceData_800bd0f8));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b410L)
  public static void tickAndRenderTransitionIntoBattle() {
    if(_8004f6e4 == -1) {
      return;
    }

    renderCombatDissolveEffect();

    if(_800bd740 >= 0) {
      _800bd740--;
      return;
    }

    //LAB_8001b460
    if(dissolveDarkening_800bd700.active_00) {
      tickBattleDissolveDarkening();
    }

    //LAB_8001b480
    if((battleFlags_800bc960 & 0x2) != 0) { // Combat controller script is loaded
      if(battleStartDelayTicks_8004f6ec == 0) {
        battleStartDelayTicks_8004f6ec = 1;
        setBattleDissolveDarkeningMetrics(true, 300 / vsyncMode_8007a3b8);
        startFadeEffect(1, 1);
      }
    }

    //LAB_8001b4c0
    if(battleStartDelayTicks_8004f6ec != 0) {
      //LAB_8001b4d4
      if(battleStartDelayTicks_8004f6ec >= 150 / vsyncMode_8007a3b8) {
        _8004f6e4 = -1;
        battleFlags_800bc960 |= 0x1;
      }

      //LAB_8001b518
      battleStartDelayTicks_8004f6ec++;
    }

    //LAB_8001b528
    //LAB_8001b53c
  }

  @Method(0x8001b54cL)
  public static void renderCombatDissolveEffect() {
    renderBattleStartingBorders();

    battleDissolveTicks += vsyncMode_8007a3b8;

    final int sp10 = -displayWidth_1f8003e0 / 2;
    final int sp14 = -displayHeight_1f8003e4 / 2;

    if((battleDissolveTicks & 0x1) == 0) {
      final int a0 = displayHeight_1f8003e4 / 8;
      final int v0 = 100 / a0;

      if(v0 == _800bd714) {
        _800bd714 = 0;
        _800bd710++;
        final int v1 = a0 - 1;
        if(v1 < _800bd710) {
          _800bd710 = v1;
        }
      }

      //LAB_8001b608
      int sp30 = 512;

      //LAB_8001b620
      for(int sp18 = 0; sp18 <= _800bd710; sp18++) {
        final int sp24 = sp30 >> 8;
        int sp2c = sp10;
        final int v = displayHeight_1f8003e4 - (_800bd710 + 1) * 8 + sp18 * 8;

        //LAB_8001b664
        for(int sp1c = 0; sp1c < displayWidth_1f8003e0 / 32 * 4; sp1c++) {
          final int u = sp1c * 8;

          //LAB_8001b6a4
          for(int s7 = 0; s7 <= 0; s7++) {
            int s3 = rand() % 4;
            if((rand() & 1) != 0) {
              s3 = -s3;
            }

            //LAB_8001b6dc
            final int s2 = rand() % 6;
            final int left = sp2c + s3;
            final int top = sp14 + v + s2 + sp24;

            //LAB_8001b734
            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .bpp(Bpp.BITS_15)
              .translucent(Translucency.HALF_B_PLUS_HALF_F)
              .monochrome(dissolveDarkening_800bd700.brightnessAccumulator_08 >> 8)
              .pos(0, left, top)
              .pos(1, left + 8, top)
              .pos(2, left, top + 8)
              .pos(3, left + 8, top + 8)
              .uv(0, u, v)
              .uv(1, u + 7, v)
              .uv(2, u, v + 8)
              .uv(3, u + 7, v + 8)
              .texture(GPU.getDisplayBuffer());

            //LAB_8001b868
            GPU.queueCommand(6, cmd);
          }

          sp2c += 8;
        }

        //LAB_8001b8b8
        sp30 += 512;
      }

      _800bd714++;
    }

    renderBattleStartingScreenDarkening(sp10, sp14);
  }

  @Method(0x8001b92cL)
  public static void renderBattleStartingBorders() {
    final int width = displayWidth_1f8003e0;
    final int height = displayHeight_1f8003e4;
    final int left = -width / 2;
    final int right = width / 2;
    final int top = -height / 2;
    final int bottom = height / 2;

    GPU.queueCommand(6, new GpuCommandQuad().monochrome(1).pos(left - 32, top - 32, width + 64, 36));
    GPU.queueCommand(6, new GpuCommandQuad().monochrome(1).pos(left - 32, bottom - 4, width + 64, 36));
    GPU.queueCommand(6, new GpuCommandQuad().monochrome(1).pos(left - 32, top, 36, height));
    GPU.queueCommand(6, new GpuCommandQuad().monochrome(1).pos(right - 4, top, 36, height));
  }

  /** The game doesn't continue rendering when battles are loading, this basically continues rendering the last frame that was rendered, but slightly darker each time */
  @Method(0x8001bbccL)
  public static void renderBattleStartingScreenDarkening(final int x, final int y) {
    renderBattleStartingBorders();

    GPU.queueCommand(6, new GpuCommandPoly(4)
      .bpp(Bpp.BITS_15)
      .monochrome(MathHelper.clamp((int)(dissolveDarkening_800bd700.brightnessAccumulator_08 * 1.1f) >> 8, 0x80 - 2 * vsyncMode_8007a3b8, 0x80))
      .pos(0, x, y)
      .pos(1, x + 384, y)
      .pos(2, x, y + displayHeight_1f8003e4 - 1)
      .pos(3, x + 384, y + displayHeight_1f8003e4 - 1)
      .uv(0, 0, 0)
      .uv(1, 384, 0)
      .uv(2, 0, 240)
      .uv(3, 384, 240)
      .texture(GPU.getDisplayBuffer())
    );
  }

  @Method(0x8001c4ecL)
  public static void clearCombatVars() {
    battleStartDelayTicks_8004f6ec = 0;
    playSound(0, 16, 0, 0, (short)0, (short)0);
    vsyncMode_8007a3b8 = 1;
    _800bd740 = 2;
    dissolveDarkening_800bd700.active_00 = false;
    dissolveDarkening_800bd700.framesRemaining_04 = 0;
    dissolveDarkening_800bd700.brightnessAccumulator_08 = 0x8000;
    _800bd714 = 0;
    _800bd710 = 0;
    battleDissolveTicks = 0;
    clearRed_8007a3a8 = 0;
    clearGreen_800bb104 = 0;
    clearBlue_800babc0 = 0;
    _8004f6e4 = 1;
  }

  @Method(0x8001c594L)
  public static void setBattleDissolveDarkeningMetrics(final boolean active, final int frames) {
    dissolveDarkening_800bd700.active_00 = active;
    dissolveDarkening_800bd700.framesRemaining_04 = frames;
    dissolveDarkening_800bd700.brightnessAccumulator_08 = 0x8000;
    dissolveDarkening_800bd700.brightnessStep_0c = 0x8000 / frames;
  }

  @Method(0x8001c5bcL)
  public static void tickBattleDissolveDarkening() {
    if(dissolveDarkening_800bd700.active_00) {
      dissolveDarkening_800bd700.framesRemaining_04--;
      dissolveDarkening_800bd700.brightnessAccumulator_08 -= dissolveDarkening_800bd700.brightnessStep_0c;

      if(dissolveDarkening_800bd700.framesRemaining_04 == 0) {
        dissolveDarkening_800bd700.active_00 = false;
      }
    }
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

  @Method(0x8001cae0L)
  public static void charSoundEffectsLoaded(final List<FileData> files, final int charSlot) {
    final int charId = gameState_800babc8.charIds_88[charSlot];

    //LAB_8001cb34
    final int index = characterSoundFileIndices_800500f8[charSlot];
    final SoundFile sound = soundFiles_800bcf80[index];

    sound.name = "Char slot %d sound effects".formatted(charSlot);
    sound.charId_02 = charId;
    sound.indices_08 = SoundFileIndices.load(files.get(1));

    //LAB_8001cc48
    //LAB_8001cc50
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), charSlotSpuOffsets_80050190[charSlot]);
    sound.used_00 = true;

    if(charSlot == 0 || charSlot == 1) {
      //LAB_8001cc30
      FUN_8001e8cc();
    } else {
      //LAB_8001cc38
      //LAB_8001cc40
      FUN_8001e8d4();
    }
  }

  @Method(0x8001cce8L)
  public static void loadCharAttackSounds(final int bentIndex, final int type) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[bentIndex].innerStruct_00;

    //LAB_8001cd3c
    int charSlot;
    for(charSlot = 0; charSlot < 3; charSlot++) {
      final SoundFile soundFile = soundFiles_800bcf80[characterSoundFileIndices_800500f8[charSlot]];

      if(soundFile.charId_02 == bent.charId_272) {
        break;
      }
    }

    //LAB_8001cd78
    final SoundFile soundFile = soundFiles_800bcf80[characterSoundFileIndices_800500f8[charSlot]];
    sssqUnloadPlayableSound(soundFile.playableSound_10);
    soundFile.used_00 = false;

    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x8);

    final int fileIndex;
    final String soundName;
    if(type != 0) {
      //LAB_8001ce44
      fileIndex = 1298 + bent.charId_272;
      soundName = "Char slot %d attack sounds".formatted(bent.charId_272);
    } else if(bent.charId_272 != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
      //LAB_8001ce18
      fileIndex = 1307 + bent.charId_272;
      soundName = "Char slot %d dragoon attack sounds".formatted(bent.charId_272);
    } else {
      fileIndex = 1307;
      soundName = "Divine dragoon attack sounds";
    }

    //LAB_8001ce70
    final int finalCharSlot = charSlot;
    loadDrgnDir(0, fileIndex, files -> charAttackSoundsLoaded(files, soundName, finalCharSlot));
  }

  @Method(0x8001ce98L)
  public static void charAttackSoundsLoaded(final List<FileData> files, final String soundName, final int charSlot) {
    final SoundFile sound = soundFiles_800bcf80[characterSoundFileIndices_800500f8[charSlot]];

    //LAB_8001cee8
    //LAB_8001cf2c
    sound.name = soundName;
    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.charId_02 = files.get(0).readShort(0);
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), charSlotSpuOffsets_80050190[charSlot]);
    cleanUpCharAttackSounds();
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
  public static void loadDeffSounds(final ScriptState<BattleEntity27c> bentState, final int type) {
    final BattleEntity27c bent = bentState.innerStruct_00;

    unloadSoundFile(3);
    unloadSoundFile(6);

    if(type == 0) {
      //LAB_8001d0e0
      loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x40);
      if(bent.charId_272 != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
        //LAB_8001d134
        // Regular dragoons
        loadDrgnDir(0, 1317 + bent.charId_272, files -> FUN_8001e98c(files, "%s dragoon transformation sounds (file %d)".formatted(getCharacterName(bent.charId_272), 1317 + bent.charId_272)));
      } else {
        // Divine dragoon
        loadDrgnDir(0, 1328, files -> FUN_8001e98c(files, "Divine dragoon transformation sounds (file 1328)"));
      }
    } else if(type == 1) {
      //LAB_8001d164
      FUN_8001d2d8();
    } else if(type == 2) {
      //LAB_8001d174
      loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x40);

      //LAB_8001d1a8
      loadDrgnDir(0, 1327, files -> FUN_8001e98c(files, "Item sounds? (file 1327)"));
    }

    //LAB_8001d1b0
  }

  @Method(0x8001d1c4L)
  public static void loadMonsterSounds() {
    final int encounterId = encounterId_800bb0f8;
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
      fileIndex = 778 + encounterId_800bb0f8;
    }

    //LAB_8001d2c0
    // Example file: 1017
    loadMonsterSounds(fileIndex);
  }

  @Method(0x8001d2d8L)
  public static void FUN_8001d2d8() {
    if(encounterId_800bb0f8 == 390) { // Doel
      //LAB_8001d330
      if(battleState_8006e398.stageProgression_eec == 0) {
        loadMonsterSounds(1290);
      } else {
        //LAB_8001d370
        loadMonsterSounds(1291);
      }
      //LAB_8001d31c
    } else if(encounterId_800bb0f8 == 431) { // Zackwell
      //LAB_8001d394
      if(battleState_8006e398.stageProgression_eec == 0) {
        loadMonsterSounds(1296);
      } else {
        //LAB_8001d3d0
        loadMonsterSounds(1297);
      }
    } else if(encounterId_800bb0f8 == 443) { // Melbu
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
      loadMonsterSounds(778 + encounterId_800bb0f8);
    }

    //LAB_8001d50c
  }

  public static int soundBufferOffset;

  /** TODO this isn't thread-safe */
  private static void loadMonsterSounds(final int fileIndex) {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x10);
    soundBufferOffset = 0;

    for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
      final SoundFile file = soundFiles_800bcf80[monsterSoundFileIndices_800500e8[monsterSlot]];
      file.charId_02 = -1;
      file.used_00 = false;

      if(Unpacker.exists("SECT/DRGN0.BIN/%d/%d".formatted(fileIndex, monsterSlot))) {
        final int finalMonsterSlot = monsterSlot;
        loadDrgnDir(0, fileIndex + "/" + monsterSlot, files -> FUN_8001d51c(files, "Monster slot %d (file %d)".formatted(finalMonsterSlot, fileIndex), finalMonsterSlot));
      }
    }

    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_ffef);
  }

  @Method(0x8001d51cL)
  public static void FUN_8001d51c(final List<FileData> files, final String soundName, final int monsterSlot) {
    //LAB_8001d698
    final int file3Size = files.get(3).size();

    if(file3Size > 48) {
      //LAB_8001d704
      //LAB_8001d718
      int v1 = 51024;
      for(int n = 3; n >= 0 && file3Size < v1; n--) {
        v1 = v1 - 17008;
      }

      //LAB_8001d72c
      final int soundFileIndex = monsterSoundFileIndices_800500e8[monsterSlot];
      final SoundFile soundFile = soundFiles_800bcf80[soundFileIndex];

      soundFile.name = soundName;
      soundFile.indices_08 = SoundFileIndices.load(files.get(1));
      soundFile.charId_02 = files.get(0).readShort(0);

      //LAB_8001d80c
      soundFile.playableSound_10 = loadSshdAndSoundbank(soundFile.name, files.get(3), new Sshd(files.get(2)), 0x5_a1e0 + soundBufferOffset);

      loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_ffef);

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

    soundFile.playableSound_10 = loadSshdAndSoundbank(soundFile.name, files.get(4), new Sshd(files.get(3)), 0x4_de90);
    loadExtraBattleCutsceneSoundbanks();
    setSoundSequenceVolume(soundFile.playableSound_10, 0x7f);
  }

  @Method(0x8001d9d0L)
  public static void loadEncounterMusic() {
    final StageData2c stageData = stageData_80109a98[encounterId_800bb0f8];

    if(stageData.musicIndex_04 != 0xff) {
      loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x80);

      final int fileIndex;
      final Consumer<List<FileData>> callback;
      if((stageData.musicIndex_04 & 0x1f) == 0x13) {
        unloadSoundFile(8);
        fileIndex = 732;
        callback = files -> musicPackageLoadedCallback(files, 732, true);
      } else {
        //LAB_8001da58
        fileIndex = combatMusicFileIndices_800501bc[stageData.musicIndex_04 & 0x1f];
        callback = files -> FUN_8001fb44(files, "Encounter music %d (file %d)".formatted(stageData.musicIndex_04 & 0x1f, fileIndex), 0);
      }

      //LAB_8001daa4
      loadDrgnDir(0, fileIndex, callback);
    }

    //LAB_8001daac
  }

  @Method(0x8001dabcL)
  public static void musicPackageLoadedCallback(final List<FileData> files, final int fileIndex, final boolean startSequence) {
    LOGGER.info("Music package %d loaded", fileIndex);
    musicFileIndex_800bd0fc = fileIndex;

    int i = 0;
    final SoundFile sound = soundFiles_800bcf80[11];
    sound.used_00 = true;
    sound.name = "Music package (file %d)".formatted(fileIndex);
    sound.charId_02 = files.get(i++).readShort(0);

    if(files.size() == 5) {
      sound.numberOfExtraSoundbanks_18 = files.get(i++).readUByte(0) - 1;
    }

    final Sssq sssq = new Sssq(files.get(i++));
    final Sshd sshd = new Sshd(files.get(i++));
    final FileData soundbank = files.get(i);
    sound.spuRamOffset_14 = soundbank.size();
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, soundbank, sshd, 0x2_1f70);
    currentSequenceData_800bd0f8 = loadSssq(sound.playableSound_10, sssq);

    if(files.size() == 5) {
      loadExtraSoundbanks(startSequence);
    } else {
      if(startSequence) {
        startMusicSequence(currentSequenceData_800bd0f8);
      }

      FUN_8001fa18();
    }

    //LAB_8001dda0
    setSequenceVolume(40);
    _800bd0f0 = 2;
  }

  @Method(0x8001ddd8L)
  public static void FUN_8001ddd8() {
    final EncounterSoundEffects10 encounterSoundEffects = encounterSoundEffects_800bd610;

    startMusicSequence(currentSequenceData_800bd0f8);
    encounterSoundEffects._00 = 2;
    encounterSoundEffects.sequenceData_0c = loadSssq(soundFiles_800bcf80[encounterSoundEffects.soundFileIndex].playableSound_10, encounterSoundEffects.sssq_08);
    musicLoaded_800bd782 = true;
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_ff7f);
  }

  @Method(0x8001de84L)
  public static void loadEncounterSoundsAndMusic() {
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);

    final StageData2c stageData = stageData_80109a98[encounterId_800bb0f8];

    if(stageData.musicIndex_04 != 0xff) {
      unloadEncounterSoundEffects();

      // Pulled this up from below since the methods below queue files which are now loaded synchronously. This code would therefore run before the files were loaded.
      //LAB_8001df8c
      unloadSoundFile(8);

      final int type = combatSoundEffectsTypes_8005019c[stageData.musicIndex_04 & 0x1f];
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
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x8);

    // Player combat sounds for current party composition (example file: 764)
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final int charIndex = gameState_800babc8.charIds_88[charSlot];

      if(charIndex != -1) {
        final String name = getCharacterName(charIndex).toLowerCase();
        final int finalCharSlot = charSlot;
        loadDir("characters/%s/sounds/combat".formatted(name), files -> charSoundEffectsLoaded(files, finalCharSlot));
      }
    }

    loadMonsterSounds();
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

  /** FUN_8001e010 with param 0 */
  @Method(0x8001e010L)
  public static void startMenuMusic() {
    //LAB_8001e054
    copyPlayingSounds(queuedSounds_800bd110, playingSoundsBackup_800bca78);
    stopAndResetSoundsAndSequences();
    unloadSoundFile(8);
    unloadSoundFile(8);

    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x80);
    loadDrgnDir(0, 5815, files -> musicPackageLoadedCallback(files, 5815, true));
  }

  /** FUN_8001e010 with param -1 */
  @Method(0x8001e010L)
  public static void stopMenuMusic() {
    //LAB_8001e044
    //LAB_8001e0f8
    if(loadingNewGameState_800bdc34) {
      if(engineState_8004dd20 == EngineStateEnum.WORLD_MAP_08 && gameState_800babc8.isOnWorldMap_4e4) {
        sssqResetStuff();
        unloadSoundFile(8);
        loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x80);
        loadDrgnDir(0, 5850, files -> musicPackageLoadedCallback(files, 5850, true));
      }
    } else {
      //LAB_8001e160
      unloadEncounterSoundEffects();
      unloadSoundFile(8);

      currentEngineState_8004dd04.restoreMusicAfterMenu();
    }

    //LAB_8001e26c
    stopAndResetSoundsAndSequences();
    copyPlayingSounds(playingSoundsBackup_800bca78, queuedSounds_800bd110);
    playingSoundsBackup_800bca78.clear();
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
          final int index = characterSoundFileIndices_800500f8[charSlot];

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
          final int index = monsterSoundFileIndices_800500e8[monsterIndex];

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
        if(_800bd0f0 != 0) {
          stopMusicSequence(currentSequenceData_800bd0f8, 1);
          freeSequence(currentSequenceData_800bd0f8);

          //LAB_8001e56c
          _800bd0f0 = 0;
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
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x1);
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
    final SoundFile sound = soundFiles_800bcf80[0];
    sound.used_00 = true;
    sound.name = "Menu sounds (file 5739)";

    sound.indices_08 = SoundFileIndices.load(files.get(1));

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x1010);
    unloadSoundbank_800bd778();
  }

  @Method(0x8001e780L)
  public static void unloadSoundbank_800bd778() {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_fffe);
  }

  @ScriptDescription("Loads the main menu sounds")
  @Method(0x8001e640L)
  public static FlowControl scriptLoadMenuSounds(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001e8ccL)
  public static void FUN_8001e8cc() {
    // empty
  }

  @Method(0x8001e8d4L)
  public static void FUN_8001e8d4() {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_fff7);
  }

  @ScriptDescription("Unused")
  @Method(0x8001e918L)
  public static FlowControl FUN_8001e918(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads attack sounds for a character")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The attack sounds type, 1 for regular, any other value for dragoon")
  @Method(0x8001e920L)
  public static FlowControl scriptLoadCharAttackSounds(final RunningScript<?> script) {
    loadCharAttackSounds(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001e950L)
  public static void cleanUpCharAttackSounds() {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_fff7);
  }

  @Method(0x8001e98cL)
  public static void FUN_8001e98c(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[4];
    sound.used_00 = true;
    sound.name = soundName;

    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.charId_02 = files.get(0).readShort(0);

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x5_a1e0);
    FUN_8001ea5c();
    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
  }

  @Method(0x8001ea5cL)
  public static void FUN_8001ea5c() {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_ffbf);
  }

  @Method(0x8001eadcL)
  public static void loadSubmapSounds(final int submapIndex) {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x2);
    loadDrgnDir(0, 5750 + submapIndex, files -> submapSoundsLoaded(files, "Submap %d sounds (file %d)".formatted(submapIndex, 5750 + submapIndex)));
  }

  @ScriptDescription("Unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @Method(0x8001eb30L)
  public static FlowControl FUN_8001eb30(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x8001eb38L)
  public static void submapSoundsLoaded(final List<FileData> files, final String soundName) {
    soundFiles_800bcf80[8].name = soundName;
    soundFiles_800bcf80[8].indices_08 = SoundFileIndices.load(files.get(2));
    soundFiles_800bcf80[8].ptr_0c = files.get(1);
    soundFiles_800bcf80[8].charId_02 = files.get(0).readShort(0);

    final Sshd sshd = new Sshd(files.get(3));
    if(files.get(4).size() != sshd.soundBankSize_04) {
      throw new RuntimeException("Size didn't match, need to resize array or something");
    }

    soundFiles_800bcf80[8].playableSound_10 = loadSshdAndSoundbank(soundName, files.get(4), sshd, 0x4_de90);
    submapSoundsCleanup();
    setSoundSequenceVolume(soundFiles_800bcf80[8].playableSound_10, 0x7f);
    soundFiles_800bcf80[8].used_00 = true;
  }

  @Method(0x8001ec18L)
  public static void submapSoundsCleanup() {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_fffd);
    musicLoaded_800bd782 = true;
  }

  @ScriptDescription("Load a battle cutscene's sounds")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "cutsceneIndex", description = "The cutscene index")
  @Method(0x8001ecccL)
  public static FlowControl scriptLoadBattleCutsceneSounds(final RunningScript<?> script) {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x4);
    sssqResetStuff();
    final int cutsceneIndex = script.params_20[0].get();
    final int dirIndex = 2437 + cutsceneIndex * 3;
    loadDrgnDir(0, dirIndex, fileData -> battleCutsceneSoundsLoaded(fileData, "Cutscene %d sounds (file %d)".formatted(cutsceneIndex, dirIndex)));
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
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_fffb);
    musicLoaded_800bd782 = true;
  }

  @Method(0x8001edfcL)
  public static void uploadExtraBattleCutsceneSoundbank(final FileData file) {
    final SoundFile soundFile = soundFiles_800bcf80[9];

    SPU.directWrite(0x4_de90 + soundFile.spuRamOffset_14, file.getBytes());
    soundFile.spuRamOffset_14 += file.size();
  }

  @Method(0x8001eea8L)
  public static void loadLocationMenuSoundEffects(final int index) {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x8000);
    loadDrgnDir(0, 5740 + index, files -> FUN_8001eefc(files, "Unknown WMAP sounds %d (file %d)".formatted(index, 5740 + index)));
  }

  @Method(0x8001eefcL)
  public static void FUN_8001eefc(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[12];
    sound.name = soundName;
    sound.used_00 = true;

    sound.indices_08 = SoundFileIndices.load(files.get(2));
    sound.charId_02 = files.get(0).readShort(0);

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(4), new Sshd(files.get(3)), 0x4_de90);
    FUN_8001efcc();

    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
  }

  @Method(0x8001efccL)
  public static void FUN_8001efcc() {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_7fff);
  }

  @ScriptDescription("Load a monster's sounds")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "monsterIndex", description = "The monster index")
  @Method(0x8001f070L)
  public static FlowControl scriptLoadMonsterAttackSounds(final RunningScript<?> script) {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x20);
    unloadSoundFile(6);
    final int monsterIndex = script.params_20[0].get();
    final int dirIndex = 1841 + monsterIndex;
    loadDrgnDir(0, dirIndex, files -> monsterAttackSoundsLoaded(files, "Monster %d sounds (file %d)".formatted(monsterIndex, dirIndex)));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f0dcL)
  public static void monsterAttackSoundsLoaded(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[10];
    sound.name = soundName;
    sound.used_00 = true;

    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.charId_02 = files.get(0).readShort(0);

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x6_6930);

    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_ffdf);
  }

  @ScriptDescription("Loads a character's attack sounds")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID")
  @Method(0x8001f250L)
  public static FlowControl scriptLoadCharacterAttackSounds(final RunningScript<?> script) {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x1_0000);
    unloadSoundFile(7);
    final int charId = script.params_20[0].get();
    final int dirIndex = 1897 + charId;
    loadDrgnDir(0, dirIndex, files -> characterAttackSoundsLoaded(files, "Addition index %d sounds (file %d)".formatted(charId, dirIndex)));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f2c0L)
  public static void characterAttackSoundsLoaded(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[10];
    sound.name = soundName;
    sound.used_00 = true;

    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.charId_02 = files.get(0).readShort(0);

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x6_6930);
    FUN_8001f390();

    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
  }

  @Method(0x8001f390L)
  public static void FUN_8001f390() {
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xfffe_ffff);
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
  public static void loadMusicPackage(final int index) {
    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x80);
    final int fileIndex = 5815 + index * 5;
    loadDrgnDir(0, fileIndex, files -> musicPackageLoadedCallback(files, fileIndex, true));
  }

  @ScriptDescription("Load a music package")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "musicIndex", description = "The music index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dontStartSequence", description = "If true, the sequence will not be started automatically")
  @Method(0x8001f450L)
  public static FlowControl scriptLoadMusicPackage(final RunningScript<?> script) {
    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x80);
    final int fileIndex = 5815 + script.params_20[0].get() * 5;
    final boolean startSequence = script.params_20[1].get() == 0;
    loadDrgnDir(0, fileIndex, files -> musicPackageLoadedCallback(files, fileIndex, startSequence));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads sounds for the final battle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "battleProgress", description = "The current stage of the multi-stage final fight (0-3)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dontStartSequence", description = "If true, the sequence will not be started automatically")
  @Method(0x8001f560L)
  public static FlowControl scriptLoadFinalBattleSounds(final RunningScript<?> script) {
    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x80);
    final int fileIndex = 732 + script.params_20[0].get() * 5;
    final boolean startSequence = script.params_20[1].get() == 0;
    loadDrgnDir(0, fileIndex, files -> musicPackageLoadedCallback(files, fileIndex, startSequence));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads sounds for a cutscene")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "cutsceneIndex", description = "The cutscene index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dontStartSequence", description = "If true, the sequence will not be started automatically")
  @Method(0x8001f674L)
  public static FlowControl scriptLoadCutsceneSounds(final RunningScript<?> script) {
    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x80);
    final int fileIndex = 2353 + script.params_20[0].get() * 6;
    final boolean startSequence = script.params_20[1].get() == 0;
    loadDrgnDir(0, fileIndex, files -> musicPackageLoadedCallback(files, fileIndex, startSequence));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f708L)
  public static void loadWmapMusic(final int chapterIndex) {
    unloadSoundFile(8);
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x80);
    final int fileIndex = 5850 + chapterIndex * 5;
    loadDrgnDir(0, fileIndex, files -> musicPackageLoadedCallback(files, fileIndex, true));
  }

  @Method(0x8001f810L)
  public static void loadExtraSoundbanks(final boolean startSequence) {
    final String[] files = new String[soundFiles_800bcf80[11].numberOfExtraSoundbanks_18];

    //LAB_8001f860
    for(int extraSoundbankIndex = 0; extraSoundbankIndex < soundFiles_800bcf80[11].numberOfExtraSoundbanks_18; extraSoundbankIndex++) {
      files[extraSoundbankIndex] = Integer.toString(musicFileIndex_800bd0fc + extraSoundbankIndex + 1);
    }

    loadDrgnFiles(0, f -> extraSoundbanksLoaded(f, startSequence), files);

    //LAB_8001f8b4
    musicLoaded_800bd782 = true;
  }

  private static void extraSoundbanksLoaded(final List<FileData> files, final boolean startSequence) {
    int offset = 0x2_1f70 + soundFiles_800bcf80[11].spuRamOffset_14;
    for(final FileData file : files) {
      SPU.directWrite(offset, file.getBytes());
      offset += file.size();
    }

    if(startSequence) {
      startMusicSequence(currentSequenceData_800bd0f8);
    }

    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_ff7f);
  }

  @Method(0x8001fa18L)
  public static void FUN_8001fa18() {
    sssqTempo_800bd104 = sssqGetTempo(currentSequenceData_800bd0f8) * sssqTempoScale_800bd100;
    sssqSetTempo(currentSequenceData_800bd0f8, sssqTempo_800bd104 >> 8);

    musicLoaded_800bd782 = true;
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_ff7f);
  }

  @Method(0x8001fb44L)
  public static void FUN_8001fb44(final List<FileData> files, final String soundName, final int param) {
    final SoundFile sound = soundFiles_800bcf80[11];
    sound.name = soundName;
    sound.used_00 = true;

    sound.charId_02 = files.get(0).readShort(0);

    //LAB_8001fbcc
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(files.get(2)), 0x2_1f70);

    currentSequenceData_800bd0f8 = loadSssq(sound.playableSound_10, new Sssq(files.get(1)));
    setSequenceVolume(40);
    _800bd0f0 = 2;

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
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x4000);
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
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_bfff);
  }

  @ScriptDescription("Load some kind of audio package")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "musicIndex", description = "The music index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x8001fe28L)
  public static FlowControl FUN_8001fe28(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001ffb0L)
  public static long getLoadedDrgnFiles() {
    return loadedDrgnFiles_800bcf78.get();
  }

  @ScriptDescription("Gets the currently-loaded sound files")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "flags", description = "The loaded file flags")
  @Method(0x8001ffc0L)
  public static FlowControl scriptGetLoadedSoundFiles(final RunningScript<?> script) {
    script.params_20[0].set(loadedDrgnFiles_800bcf78.get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unloads a sound file")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundType", description = "The sound type")
  @Method(0x8001ffdcL)
  public static FlowControl scriptUnloadSoundFile(final RunningScript<?> script) {
    unloadSoundFile(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }
}
