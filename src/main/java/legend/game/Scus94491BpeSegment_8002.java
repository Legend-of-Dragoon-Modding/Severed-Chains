package legend.game;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.Rect4i;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.Transforms;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.combat.types.EnemyDrop;
import legend.game.i18n.I18n;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.OverflowMode;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.MainMenuScreen;
import legend.game.inventory.screens.MenuScreen;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.inventory.GiveEquipmentEvent;
import legend.game.modding.events.inventory.GiveItemEvent;
import legend.game.modding.events.inventory.TakeEquipmentEvent;
import legend.game.modding.events.inventory.TakeItemEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.NotImplementedException;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptReadable;
import legend.game.sound.QueuedSound28;
import legend.game.sound.SoundFile;
import legend.game.submap.SubmapEnvState;
import legend.game.tim.Tim;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CContainer;
import legend.game.types.CharacterData2c;
import legend.game.types.LodString;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.Model124;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.SmallerStruct;
import legend.game.types.Textbox4c;
import legend.game.types.TextboxArrow0c;
import legend.game.types.TextboxBorderMetrics0c;
import legend.game.types.TextboxChar08;
import legend.game.types.TextboxState;
import legend.game.types.TextboxText84;
import legend.game.types.TextboxTextState;
import legend.game.types.BackgroundType;
import legend.game.types.TmdAnimationFile;
import legend.game.types.TmdSubExtension;
import legend.game.types.Translucency;
import legend.game.types.UiPart;
import legend.game.types.UiType;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.core.GameEngine.bootMods;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.startMenuMusic;
import static legend.game.SItem.stopMenuMusic;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.loadDir;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.monsterSoundLoaded;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.rectArray28_80010770;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment.stopAndResetSoundsAndSequences;
import static legend.game.Scus94491BpeSegment.stopCurrentMusicSequence;
import static legend.game.Scus94491BpeSegment.textboxBorderMetrics_800108b0;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.stopMusicSequence;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.digits_80052b40;
import static legend.game.Scus94491BpeSegment_8005.monsterSoundFileIndices_800500e8;
import static legend.game.Scus94491BpeSegment_8005.renderBorder_80052b68;
import static legend.game.Scus94491BpeSegment_8005.shadowScale_8005039c;
import static legend.game.Scus94491BpeSegment_8005.shouldRestoreCameraPosition_80052c40;
import static legend.game.Scus94491BpeSegment_8005.submapCutBeforeBattle_80052c3c;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapEnvState_80052c44;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8005.textboxMode_80052b88;
import static legend.game.Scus94491BpeSegment_8005.textboxTextType_80052ba8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bd7ac;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b.characterStatsLoaded_800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.previousEngineState_800bdb88;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.rumbleDampener_800bee80;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.soundFiles_800bcf80;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.submapFullyLoaded_800bd7b4;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.textboxArrows_800bdea0;
import static legend.game.Scus94491BpeSegment_800b.textboxText_800bdf38;
import static legend.game.Scus94491BpeSegment_800b.textboxVariables_800bdf10;
import static legend.game.Scus94491BpeSegment_800b.textboxes_800be358;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.transitioningFromCombatToSubmap_800bd7b8;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800e.main;
import static org.lwjgl.opengl.GL11C.GL_LEQUAL;

public final class Scus94491BpeSegment_8002 {
  private Scus94491BpeSegment_8002() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8002.class);

  private static Obj textboxBackgroundObj;
  private static final Obj[] textboxBorderObjs = new Obj[8];

  /** One per animation frame */
  private static final Obj[] textboxArrowObjs = new Obj[7];
  private static final MV textboxArrowTransforms = new MV();

  private static Obj textboxSelectionObj;
  private static final MV textboxSelectionTransforms = new MV();

  @Method(0x80020008L)
  public static void sssqResetStuff() {
    stopAndResetSoundsAndSequences();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
    unloadSoundFile(8);
    stopMusicSequence();
  }

  @ScriptDescription("Stops and unloads all music and sound sequences")
  @Method(0x80020060L)
  public static FlowControl scriptStopAndUnloadSequences(final RunningScript<?> script) {
    stopAndResetSoundsAndSequences();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
    unloadSoundFile(8);
    stopMusicSequence();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Mark a character battle entity's sound files as no longer used")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c index")
  @Method(0x8002013cL)
  public static FlowControl scriptUnuseCharSoundFile(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Stops and unloads the encounter's sound effects")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The sound effect index")
  @Method(0x80020230L)
  public static FlowControl scriptStopEncounterSoundEffects(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Frees the encounter's sound effects")
  @Method(0x800202a4L)
  public static FlowControl scriptFreeEncounterSoundEffects(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80020308L)
  public static void FUN_80020308() {
    stopCurrentMusicSequence();
    stopAndResetSoundsAndSequences();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
  }

  @Method(0x80020360L)
  public static void copyPlayingSounds(final Queue<QueuedSound28> sources, final Queue<QueuedSound28> dests) {
    //LAB_8002036c
    dests.clear();

    for(final QueuedSound28 queuedSound : sources) {
      dests.add(queuedSound);

      if(queuedSound.type_00 == 4 && queuedSound._1c != 0) {
        queuedSound.type_00 = 3;
      }
    }
  }

  @ScriptDescription("Replaces a monster's attack sounds (e.g. used when Melbu changes forms)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "monsterId", description = "The monster ID")
  @Method(0x800203f0L)
  public static FlowControl scriptReplaceMonsterSounds(final RunningScript<?> script) {
    unloadSoundFile(3);
    loadedDrgnFiles_800bcf78.updateAndGet(val -> val | 0x10);

    final int fileIndex = 1290 + script.params_20[0].get();

    final String path;
    switch(fileIndex) {
      case 1290 -> path = "monsters/phases/doel/0";
      case 1291 -> path = "monsters/phases/doel/1";
      case 1292 -> path = "monsters/phases/melbu/0";
      case 1293 -> path = "monsters/phases/melbu/1";
      case 1294 -> path = "monsters/phases/melbu/4";
      case 1295 -> path = "monsters/phases/melbu/6";
      case 1296 -> path = "monsters/phases/zackwell/0";
      case 1297 -> path = "monsters/phases/zackwell/1";
      default -> throw new IllegalArgumentException("Unknown battle phase file index " + fileIndex);
    }

    final AtomicInteger soundbankOffset = new AtomicInteger();
    final AtomicInteger count = new AtomicInteger(0);

    for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
      if(Loader.exists(path + '/' + monsterSlot)) {
        count.incrementAndGet();
      }
    }

    for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
      final SoundFile file = soundFiles_800bcf80[monsterSoundFileIndices_800500e8[monsterSlot]];
      file.id_02 = -1;
      file.used_00 = false;

      if(Loader.exists(path + '/' + monsterSlot)) {
        final int finalMonsterSlot = monsterSlot;
        loadDir(path + '/' + monsterSlot, files -> {
          final int offset = soundbankOffset.getAndUpdate(val -> val + MathHelper.roundUp(files.get(3).size(), 0x10));
          monsterSoundLoaded(files, "Monster slot %d (file %s) (replaced)".formatted(finalMonsterSlot, path), finalMonsterSlot, offset);

          if(count.decrementAndGet() == 0) {
            loadedDrgnFiles_800bcf78.updateAndGet(val -> val & ~0x10);
          }
        });
      }
    }

    return FlowControl.CONTINUE;
  }

  @Method(0x80020468L)
  public static void adjustPartUvs(final ModelPart10 dobj2, final UvAdjustmentMetrics14 metrics) {
    final TmdObjTable1c objTable = dobj2.tmd_08;

    for(final TmdObjTable1c.Primitive primitive : objTable.primitives_10) {
      final int command = primitive.header() & 0xff04_0000;

      if(command == 0x3400_0000 || command == 0x3500_0000 || command == 0x3600_0000 || command == 0x3700_0000 || command == 0x3c00_0000 || command == 0x3d00_0000 || command == 0x3e00_0000 || command == 0x3f00_0000) {
        metrics.apply(primitive);
      }
    }
  }

  @Method(0x80020718L)
  public static void loadModelAndAnimation(final Model124 model, final CContainer cContainer, final TmdAnimationFile tmdAnimFile) {
    LOGGER.info("Loading scripted TMD %s (animation %s)", cContainer, tmdAnimFile);

    final float x = model.coord2_14.coord.transfer.x;
    final float y = model.coord2_14.coord.transfer.y;
    final float z = model.coord2_14.coord.transfer.z;

    //LAB_80020760
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    if(currentEngineState_8004dd04 != null) {
      currentEngineState_8004dd04.modelLoaded(model, cContainer);
    }

    //LAB_8002079c
    model.tpage_108 = (int)((cContainer.tmdPtr_00.id & 0xffff_0000L) >>> 11); // LOD uses the upper 16 bits of TMD IDs as tpage (sans VRAM X/Y)

    if(cContainer.ptr_08 != null) {
      model.ptr_a8 = cContainer.ptr_08;

      //LAB_800207d4
      for(int i = 0; i < 7; i++) {
        model.animationMetrics_d0[i] = model.ptr_a8._00[i];
        FUN_8002246c(model, i);
      }
    } else {
      //LAB_80020818
      model.ptr_a8 = null;

      //LAB_80020828
      for(int i = 0; i < 7; i++) {
        model.animationMetrics_d0[i] = null;
      }
    }

    //LAB_80020838
    initObjTable2(model.modelParts_00);
    GsInitCoordinate2(null, model.coord2_14);
    prepareObjTable2(model.modelParts_00, cContainer.tmdPtr_00.tmd, model.coord2_14);

    model.zOffset_a0 = 0;
    model.disableInterpolation_a2 = false;
    model.ub_a3 = 0;
    model.partInvisible_f4 = 0;

    loadModelStandardAnimation(model, tmdAnimFile);

    //LAB_800209b0
    model.coord2_14.coord.transfer.set(x, y, z);
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
    model.shadowType_cc = 0;
    model.shadowSize_10c.set(1.0f, 1.0f, 1.0f);
    model.shadowOffset_118.zero();
  }

  @Method(0x80020a00L)
  public static void initModel(final Model124 model, final CContainer CContainer, final TmdAnimationFile tmdAnimFile) {
    model.modelParts_00 = new ModelPart10[CContainer.tmdPtr_00.tmd.header.nobj];

    Arrays.setAll(model.modelParts_00, i -> new ModelPart10());

    loadModelAndAnimation(model, CContainer, tmdAnimFile);
    adjustModelUvs(model);
    model.modelPartWithShadowIndex_cd = -2;
  }

  @Method(0x80020b98L)
  public static void animateModel(final Model124 model) {
    animateModel(model, 2);
  }

  public static void animateModel(final Model124 model, final int framesPerKeyframe) {
    //LAB_80020be8
    //LAB_80020bf0
    // Only apply texture animations for the keyframe of the middle interpolation frame
    if(model.subFrameIndex == 0 || model.subFrameIndex == framesPerKeyframe / 2) {
      if(model.smallerStructPtr_a4 != null) {
        //LAB_800da138
        for(int i = 0; i < 4; i++) {
          if(model.smallerStructPtr_a4.uba_04[i]) {
            animateSubmapModelClut(model, i);
          }
        }
      }

      for(int i = 0; i < 7; i++) {
        if(model.animateTextures_ec[i]) {
          animateModelTextures(model, i);
        }
      }
    }

    if(model.animationState_9c == 2) {
      return;
    }

    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    }

    //LAB_80020c3c
    if(model.animationState_9c == 0) {
      model.animationState_9c = 1;
      model.remainingFrames_9e = model.totalFrames_9a / 2;
      model.currentKeyframe_94 = 0;
      model.subFrameIndex = 0;
    }

    //LAB_80020c90
    if(model.subFrameIndex == framesPerKeyframe - 1 || model.disableInterpolation_a2 && model.subFrameIndex == framesPerKeyframe / 2 - 1) {
      applyKeyframe(model);
      model.currentKeyframe_94++;
      model.remainingFrames_9e--;
      model.subFrameIndex = 0;
    } else {
      applyInterpolationFrame(model, model.disableInterpolation_a2 ? framesPerKeyframe / 2 : framesPerKeyframe);
      model.subFrameIndex++;
    }
    //LAB_80020e98
    //LAB_80020ea8
  }

  /** (pulled from SMAP) Used in pre-Melbu submap cutscene, Prairie, new game Rose cutscene (animates the cloud flicker by changing CLUT, pretty sure this is CLUT animation) */
  @Method(0x800dde70L)
  private static void animateSubmapModelClut(final Model124 model, final int index) {
    final SmallerStruct smallerStruct = model.smallerStructPtr_a4;

    if(smallerStruct.tmdSubExtensionArr_20[index] == null) {
      smallerStruct.uba_04[index] = false;
    } else {
      //LAB_800ddeac
      final int x = model.uvAdjustments_9d.clutX;
      final int y = model.uvAdjustments_9d.clutY;

      final TmdSubExtension v = smallerStruct.tmdSubExtensionArr_20[index];
      int a1 = 0;

      //LAB_800ddef8
      for(int i = 0; i < smallerStruct.sa_08[index]; i++) {
        a1 += 2;
      }

      //LAB_800ddf08
      final int sourceYOffset = v.sa_04[a1];
      a1++;

      smallerStruct.sa_10[index]++;

      if(smallerStruct.sa_10[index] == v.sa_04[a1]) {
        smallerStruct.sa_10[index] = 0;

        if(v.sa_04[a1 + 1] == -1) {
          smallerStruct.sa_08[index] = 0;
        } else {
          //LAB_800ddf70
          smallerStruct.sa_08[index]++;
        }
      }

      //LAB_800ddf8c
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + sourceYOffset, x, y + smallerStruct.sa_18[index], 16, 1));
    }
    //LAB_800ddff4
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

  @Method(0x800212d8L)
  public static void applyKeyframe(final Model124 model) {
    //LAB_80021320
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final GsCOORDINATE2 coord2 = model.modelParts_00[i].coord2_04;
      final Transforms params = coord2.transforms;

      params.quat.set(model.keyframes_90[model.currentKeyframe_94][i].quat);
      params.trans.set(model.keyframes_90[model.currentKeyframe_94][i].translate_06);

      coord2.coord.rotation(params.quat);
      coord2.coord.transfer.set(params.trans);
    }
  }

  @Method(0x800213c4L)
  public static void applyInterpolationFrame(final Model124 model, final int framesPerKeyframe) {
    //LAB_80021404
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final GsCOORDINATE2 coord2 = model.modelParts_00[i].coord2_04;
      final Transforms params = coord2.transforms;

      final float interpolationScale = (model.subFrameIndex + 1.0f) / framesPerKeyframe;
      params.trans.lerp(model.keyframes_90[model.currentKeyframe_94][i].translate_06, interpolationScale, coord2.coord.transfer);
      params.quat.nlerp(model.keyframes_90[model.currentKeyframe_94][i].quat, interpolationScale, params.quat);
      coord2.coord.rotation(params.quat);
    }
  }

  @Method(0x800214bcL)
  public static void applyModelRotationAndScale(final Model124 model) {
    model.coord2_14.coord.scaling(model.coord2_14.transforms.scale);
    model.coord2_14.coord.rotateXYZ(model.coord2_14.transforms.rotate);
    model.coord2_14.flg = 0;
  }

  @Method(0x80021520L)
  public static void loadPlayerModelAndAnimation(final Model124 model, final CContainer tmd, final TmdAnimationFile anim, final int shadowSizeIndex) {
    loadModelAndAnimation(model, tmd, anim);
    adjustModelUvs(model);
    model.modelPartWithShadowIndex_cd = -2;

    setShadowSize(model, shadowSizeIndex);
  }

  @Method(0x8002155cL)
  public static void setShadowSize(final Model124 model, final int shadowSizeIndex) {
    final float scale = shadowScale_8005039c[shadowSizeIndex] / (float)0x1000;
    model.shadowSize_10c.set(scale, scale, scale);
  }

  @Method(0x80021584L)
  public static void loadModelStandardAnimation(final Model124 model, final TmdAnimationFile tmdAnimFile) {
    model.anim_08 = model.new StandardAnim(tmdAnimFile);
    model.keyframes_90 = tmdAnimFile.partTransforms_10;
    model.currentKeyframe_94 = 0;
    model.partCount_98 = tmdAnimFile.modelPartCount_0c;
    model.totalFrames_9a = tmdAnimFile.totalFrames_0e;
    model.animationState_9c = 0;

    applyKeyframe(model);

    model.remainingFrames_9e = model.totalFrames_9a / 2;
    model.subFrameIndex = 0;
    model.animationState_9c = 1;
    model.currentKeyframe_94 = 0;
  }

  @Method(0x80021628L)
  public static void adjustModelUvs(final Model124 model) {
    for(final ModelPart10 dobj2 : model.modelParts_00) {
      adjustPartUvs(dobj2, model.uvAdjustments_9d);
    }
  }

  @Method(0x800218f0L)
  public static void FUN_800218f0() {
    if(_800bd7ac) {
      _800bd7b0 = 9;
      _800bd7ac = false;
    }
  }

  @Method(0x80021b08L)
  public static void initObjTable2(final ModelPart10[] dobj2s) {
    for(final ModelPart10 dobj2 : dobj2s) {
      dobj2.attribute_00 = 0x8000_0000;
      dobj2.coord2_04 = new GsCOORDINATE2();
      dobj2.tmd_08 = null;
    }
  }

  @Method(0x80021bacL)
  public static void addNewDobj2(final ModelPart10 dobj2) {
    dobj2.attribute_00 = 0;
    GsInitCoordinate2(null, dobj2.coord2_04);
    dobj2.tmd_08 = null;
  }

  @Method(0x80021ca0L)
  public static void prepareObjTable2(final ModelPart10[] table, final Tmd tmd, final GsCOORDINATE2 coord2) {
    //LAB_80021d08
    for(final ModelPart10 dobj2 : table) {
      addNewDobj2(dobj2);
    }

    //LAB_80021d3c
    //LAB_80021d64
    for(int i = 0; i < table.length; i++) {
      final ModelPart10 dobj2 = table[i];

      dobj2.coord2_04.flg = 0;

      if(dobj2.coord2_04.super_ == null) {
        dobj2.coord2_04.super_ = coord2;
      }

      dobj2.tmd_08 = tmd.objTable[i];

      dobj2.coord2_04.coord.identity();

      // Dunno why but models are initialized with a 1-degree rotation on all axes
      final float oneDegree = (float)Math.toRadians(1.0);
      dobj2.coord2_04.transforms.rotate.set(oneDegree, oneDegree, oneDegree);
      dobj2.coord2_04.transforms.scale.set(1.0f, 1.0f, 1.0f);
      dobj2.coord2_04.transforms.trans.set(1.0f, 1.0f, 1.0f);

      dobj2.coord2_04.coord.scaling(dobj2.coord2_04.transforms.scale);
      dobj2.coord2_04.coord.rotateXYZ(dobj2.coord2_04.transforms.rotate);
      dobj2.coord2_04.coord.transfer.set(dobj2.coord2_04.transforms.trans);
    }

    //LAB_80021db4
  }

  /**
   * This method animates the fog in the first cutscene with Rose/Feyrbrand
   */
  @Method(0x80022018L)
  public static void animateModelTextures(final Model124 model, final int index) {
    if(model.animationMetrics_d0[index] == null) {
      model.animateTextures_ec[index] = false;
      return;
    }

    //LAB_80022068
    //LAB_80022098
    if(model.uvAdjustments_9d == UvAdjustmentMetrics14.NONE) {
      return;
    }

    final int vramX = model.uvAdjustments_9d.tpageX;
    final int vramY = model.uvAdjustments_9d.tpageY;

    //LAB_800220c0
    if(model.usArr_ba[index] != 0x5678) {
      model.usArr_ba[index]--;
      if((short)model.usArr_ba[index] != 0) {
        return;
      }

      int metricsIndex = 0;
      model.usArr_ba[index] = model.animationMetrics_d0[index][metricsIndex++] & 0x7fff;
      final int destX = model.animationMetrics_d0[index][metricsIndex++] + vramX;
      final int destY = model.animationMetrics_d0[index][metricsIndex++] + vramY;
      final short w = (short)(model.animationMetrics_d0[index][metricsIndex++] / 4);
      final short h = model.animationMetrics_d0[index][metricsIndex++];

      //LAB_80022154
      for(int i = 0; i < model.usArr_ac[index]; i++) {
        metricsIndex += 2;
      }

      //LAB_80022164
      final short x2 = (short)(model.animationMetrics_d0[index][metricsIndex++] + vramX);
      final short y2 = (short)(model.animationMetrics_d0[index][metricsIndex++] + vramY);

      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x2, y2, destX & 0xffff, destY & 0xffff, w, h));

      model.usArr_ac[index]++;

      final int v1 = model.animationMetrics_d0[index][metricsIndex];
      if(v1 == -2) {
        model.animateTextures_ec[index] = false;
        model.usArr_ac[index] = 0;
      }

      //LAB_800221f8
      if(v1 == -1) {
        model.usArr_ac[index] = 0;
      }

      return;
    }

    //LAB_80022208
    int metricsIndex = 1;
    final int x = model.animationMetrics_d0[index][metricsIndex++] + vramX;
    final int y = model.animationMetrics_d0[index][metricsIndex++] + vramY;
    final int w = model.animationMetrics_d0[index][metricsIndex++] / 4;
    int h = model.animationMetrics_d0[index][metricsIndex++];
    final int copyMode = model.animationMetrics_d0[index][metricsIndex++];
    int secondaryYOffsetH = model.animationMetrics_d0[index][metricsIndex];

    if((model.usArr_ac[index] & 0xf) != 0) {
      model.usArr_ac[index]--;

      if(model.usArr_ac[index] == 0) {
        model.usArr_ac[index] = secondaryYOffsetH;
        secondaryYOffsetH = 16;
      } else {
        //LAB_80022278
        secondaryYOffsetH = 0;
      }
    }

    //LAB_8002227c
    if((short)secondaryYOffsetH == 0) {
      return;
    }

    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, x & 0xffff, y & 0xffff, w, h));

    secondaryYOffsetH /= 16;
    h -= secondaryYOffsetH;

    if((short)copyMode == 0) {
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + h, 960, 256, w, secondaryYOffsetH));
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y, 960, secondaryYOffsetH + 256, w, h));
    } else {
      //LAB_80022358
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y, 960, h + 256, w, secondaryYOffsetH));
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + secondaryYOffsetH, 960, 256, w, h));
    }
    //LAB_80022440
  }

  @Method(0x8002246cL)
  public static void FUN_8002246c(final Model124 model, final int index) {
    if(model.animationMetrics_d0[index] == null) {
      model.animateTextures_ec[index] = false;
      return;
    }

    //LAB_80022490
    model.usArr_ac[index] = 0;
    model.usArr_ba[index] = model.animationMetrics_d0[index][0] & 0x3fff;

    //LAB_800224d0
    model.animateTextures_ec[index] = (model.animationMetrics_d0[index][0] & 0x8000) != 0;

    //LAB_800224d8
    if((model.animationMetrics_d0[index][0] & 0x4000) != 0) {
      model.usArr_ba[index] = 0x5678;
      model.usArr_ac[index] = model.animationMetrics_d0[index][6];
      model.animateTextures_ec[index] = true;
    }

    //LAB_80022510
  }

  public static void initMenu(final WhichMenu destMenu, @Nullable final Supplier<MenuScreen> destScreen) {
    startMenuMusic();
    SCRIPTS.stop();

    renderablePtr_800bdc5c = null;
    resizeDisplay(384, 240);
    textZ_800bdf00 = 33;

    whichMenu_800bdc38 = destMenu;

    if(destScreen != null) {
      menuStack.pushScreen(destScreen.get());
    }
  }

  public static void initInventoryMenu() {
    initMenu(WhichMenu.RENDER_NEW_MENU, () -> new MainMenuScreen(() -> {
      menuStack.popScreen();

      if(whichMenu_800bdc38 == WhichMenu.QUIT) {
        deallocateRenderables(0xff);
        startFadeEffect(2, 10);
        textZ_800bdf00 = 13;
      }

      whichMenu_800bdc38 = WhichMenu.UNLOAD;
    }));
  }

  @Method(0x80022590L)
  public static void loadAndRenderMenus() {
    switch(whichMenu_800bdc38) {
      case RENDER_NEW_MENU, RENDER_SAVE_GAME_MENU_19 -> menuStack.render();

      case UNLOAD, UNLOAD_SAVE_GAME_MENU_20, UNLOAD_POST_COMBAT_REPORT_30 -> {
        menuStack.reset();

        if(whichMenu_800bdc38 != WhichMenu.UNLOAD_SAVE_GAME_MENU_20 && whichMenu_800bdc38 != WhichMenu.UNLOAD_POST_COMBAT_REPORT_30) {
          stopMenuMusic();
        }

        SCRIPTS.start();
        whichMenu_800bdc38 = WhichMenu.NONE_0;

        deallocateRenderables(0xff);

        startFadeEffect(2, 10);

        textZ_800bdf00 = 13;
      }
    }
  }

  @Method(0x80022928L)
  public static int getUnlockedDragoonSpells(final int[] spellIndicesOut, final int charIndex) {
    //LAB_80022940
    for(int spellIndex = 0; spellIndex < 8; spellIndex++) {
      spellIndicesOut[spellIndex] = -1;
    }

    if(charIndex == -1) {
      //LAB_80022a08
      return 0;
    }

    // Hardcoded Divine Dragoon spells
    if(charIndex == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
      spellIndicesOut[0] = 9;
      spellIndicesOut[1] = 4;
      return 2;
    }

    //LAB_80022994
    //LAB_80022998
    //LAB_800229d0
    int spellCount = 0;
    for(int dlevel = 0; dlevel < stats_800be5f8[charIndex].dlevel_0f + 1; dlevel++) {
      final MagicStuff08 spellStuff = magicStuff_80111d20[charIndex][dlevel];
      final int spellIndex = spellStuff.spellIndex_02;

      if(spellIndex != -1) {
        spellIndicesOut[spellCount] = spellIndex;
        spellCount++;
      }

      //LAB_800229e8
    }

    //LAB_80022a00
    return spellCount;
  }

  @Method(0x80022a10L)
  public static int getUnlockedSpellCount(final int charIndex) {
    if(charIndex == -1) {
      return 0;
    }

    //LAB_80022a24
    // Divine dragoon
    if(charIndex == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
      return 2;
    }

    //LAB_80022a4c
    //LAB_80022a50
    //LAB_80022a64
    int unlockedSpells = 0;
    for(int i = 0; i < 6; i++) {
      if(magicStuff_80111d20[charIndex][i].spellIndex_02 != -1) {
        unlockedSpells++;
      }

      //LAB_80022a7c
    }

    return unlockedSpells;
  }

  @Method(0x80022a94L)
  public static void loadMenuTexture(final FileData data) {
    final Tim tim = new Tim(data);
    final Rect4i imageRect = tim.getImageRect();

    if(imageRect.w != 0 || imageRect.h != 0) {
      imageRect.x -= 512;
      GPU.uploadData15(imageRect, tim.getImageData());
    }

    //LAB_80022acc
    if(tim.hasClut()) {
      final Rect4i clutRect = tim.getClutRect();
      clutRect.x -= 512;
      GPU.uploadData15(clutRect, tim.getClutData());
    }

    //LAB_80022aec
  }

  /**
   * @param amount Amount of HP to restore, -1 restores all hP
   * @return The amount of HP restored, -1 if all HP is restored, or -2 if HP was already full
   */
  @Method(0x80022b50L)
  public static int addHp(final int charIndex, final int amount) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    if(charData.hp_08 == stats.maxHp_66) {
      return -2;
    }

    //LAB_80022bb4
    final int ret;
    if(amount == -1) {
      charData.hp_08 = stats.maxHp_66;
      ret = -1;
    } else {
      //LAB_80022bc8
      charData.hp_08 += amount;

      if(charData.hp_08 < stats.maxHp_66) {
        ret = amount;
      } else {
        charData.hp_08 = stats.maxHp_66;
        ret = -1;
      }
    }

    //LAB_80022bec
    loadCharacterStats();

    //LAB_80022bf8
    return ret;
  }

  /**
   * @param amount Amount of MP to restore, -1 restores all MP
   * @return The amount of MP restored, -1 if all MP is restored, or -2 if MP was already full
   */
  @Method(0x80022c08L)
  public static int addMp(final int charIndex, final int amount) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    if(stats.maxMp_6e == 0 || charData.mp_0a == stats.maxMp_6e) {
      return -2;
    }

    //LAB_80022c78
    final int ret;
    if(amount == -1) {
      charData.mp_0a = stats.maxMp_6e;
      ret = -1;
    } else {
      //LAB_80022c8c
      charData.mp_0a += amount;

      if(charData.mp_0a < stats.maxMp_6e) {
        ret = amount;
      } else {
        charData.mp_0a = stats.maxMp_6e;
        ret = -1;
      }
    }

    //LAB_80022cb4
    loadCharacterStats();

    //LAB_80022cc0
    return ret;
  }

  @Method(0x80022cd0L)
  public static int addSp(final int charIndex, final int amount) {
    assert false;
    return 0;
  }

  public static boolean takeItemId(final Item item) {
    final int itemSlot = gameState_800babc8.items_2e9.indexOf(item);

    if(itemSlot != -1) {
      return takeItem(itemSlot);
    }

    return false;
  }

  @Method(0x800232dcL)
  public static boolean takeItem(final int itemSlot) {
    if(itemSlot >= gameState_800babc8.items_2e9.size()) {
      LOGGER.warn("Tried to take item index %d (out of bounds)", itemSlot);
      return false;
    }

    final Item item = gameState_800babc8.items_2e9.get(itemSlot);
    final TakeItemEvent event = EVENTS.postEvent(new TakeItemEvent(item, itemSlot));

    if(event.isCanceled()) {
      return false;
    }

    gameState_800babc8.items_2e9.remove(event.itemSlot);
    return true;
  }

  public static boolean takeEquipmentId(final Equipment equipment) {
    final int equipmentSlot = gameState_800babc8.equipment_1e8.indexOf(equipment);

    if(equipmentSlot != -1) {
      return takeEquipment(equipmentSlot);
    }

    return false;
  }

  @Method(0x800233d8L)
  public static boolean takeEquipment(final int equipmentIndex) {
    if(equipmentIndex >= gameState_800babc8.equipment_1e8.size()) {
      LOGGER.warn("Tried to take equipment index %d (out of bounds)", equipmentIndex);
      return false;
    }

    final Equipment equipment = gameState_800babc8.equipment_1e8.get(equipmentIndex);
    final TakeEquipmentEvent event = EVENTS.postEvent(new TakeEquipmentEvent(equipment, equipmentIndex));

    if(event.isCanceled()) {
      return false;
    }

    gameState_800babc8.equipment_1e8.remove(equipmentIndex);
    return true;
  }

  @Method(0x80023484L)
  public static boolean giveItem(final Item item) {
    final GiveItemEvent event = EVENTS.postEvent(new GiveItemEvent(item, Collections.unmodifiableList(gameState_800babc8.items_2e9), CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get())));

    if(event.isCanceled() || event.givenItems.isEmpty()) {
      return false;
    }

    final boolean overflowed = event.currentItems.size() + event.givenItems.size() > event.maxInventorySize;

    if(event.overflowMode == OverflowMode.FAIL && overflowed) {
      return false;
    }

    if(event.overflowMode == OverflowMode.TRUNCATE && overflowed) {
      for(int i = 0; i < event.givenItems.size() && event.currentItems.size() <= event.maxInventorySize; i++) {
        gameState_800babc8.items_2e9.add(event.givenItems.get(i));
      }

      return true;
    }

    gameState_800babc8.items_2e9.addAll(event.givenItems);
    return true;
  }

  @Method(0x80023484L)
  public static boolean giveEquipment(final Equipment equipment) {
    final GiveEquipmentEvent event = EVENTS.postEvent(new GiveEquipmentEvent(equipment, Collections.unmodifiableList(gameState_800babc8.equipment_1e8), 255));

    if(event.isCanceled() || event.givenEquipment.isEmpty()) {
      return false;
    }

    final boolean overflowed = event.currentEquipment.size() + event.givenEquipment.size() > event.maxInventorySize;

    if(event.overflowMode == OverflowMode.FAIL && overflowed) {
      return false;
    }

    if(event.overflowMode == OverflowMode.TRUNCATE && overflowed) {
      for(int i = 0; i < event.givenEquipment.size() && event.currentEquipment.size() <= event.maxInventorySize; i++) {
        gameState_800babc8.equipment_1e8.add(event.givenEquipment.get(i));
      }

      return true;
    }

    gameState_800babc8.equipment_1e8.addAll(event.givenEquipment);
    return true;
  }

  /**
   * @param items the items to give
   * @return the number of items that could not be given
   */
  @Method(0x80023544L)
  public static int giveItems(final List<EnemyDrop> items) {
    int count = 0;

    //LAB_80023580
    final Iterator<EnemyDrop> it = items.iterator();
    while(it.hasNext()) {
      final EnemyDrop drop = it.next();

      if(!drop.performDrop()) {
        //LAB_800235a4
        //LAB_800235c0
        drop.overflow();
        it.remove();
        count++;
      }

      //LAB_80023604
    }

    //LAB_80023618
    return count;
  }

  @Method(0x8002363cL)
  public static int addGold(final int amount) {
    gameState_800babc8.gold_94 += amount;

    if(gameState_800babc8.gold_94 > 99999999) {
      gameState_800babc8.gold_94 = 99999999;
    }

    //LAB_8002366c
    return 0;
  }

  /**
   * @param part 0: second, 1: minute, 2: hour
   */
  @Method(0x80023674L)
  public static int getTimestampPart(int timestamp, final long part) {
    if(timestamp >= 216000000) { // Clamp to 1000 hours
      timestamp = 215999999;
    }

    // Hours
    if(part == 0) {
      return timestamp / 216000 % 1000;
    }

    // Minutes
    if(part == 1) {
      return timestamp / 3600 % 60;
    }

    // Seconds
    if(part == 2) {
      return timestamp / 60 % 60;
    }

    return 0;
  }

  @Method(0x80023870L)
  public static void playMenuSound(final int soundIndex) {
    playSound(0, soundIndex, (short)0, (short)0);
  }

  @Method(0x800239e0L)
  public static <T> void setInventoryFromDisplay(final List<MenuEntryStruct04<T>> display, final List<T> out, final int count) {
    out.clear();

    //LAB_800239ec
    for(int i = 0; i < count; i++) {
      if((display.get(i).flags_02 & 0x1000) == 0) {
        out.add(display.get(i).item_00);
      }
    }
  }

  @Method(0x80023a2cL)
  public static <T> void sortItems(final List<MenuEntryStruct04<T>> display, final List<T> items, final int count) {
    display.sort(menuItemComparator());
    setInventoryFromDisplay(display, items, count);
  }

  public static <T> Comparator<MenuEntryStruct04<T>> menuItemComparator() {
    return Comparator
      .comparingInt((MenuEntryStruct04<T> item) -> item.getIcon())
      .thenComparing(item -> I18n.translate(item.getNameTranslationKey()));
  }

  @Method(0x80023a88L)
  public static void sortItems() {
    final List<MenuEntryStruct04<Item>> items = new ArrayList<>();

    for(final Item item : gameState_800babc8.items_2e9) {
      items.add(MenuEntryStruct04.make(item));
    }

    sortItems(items, gameState_800babc8.items_2e9, gameState_800babc8.items_2e9.size());
  }

  @Method(0x80023b54L)
  public static Renderable58 allocateRenderable(final UiType a0, @Nullable Renderable58 a1) {
    a1 = allocateManualRenderable(a0, a1);

    if(renderablePtr_800bdc5c != null) {
      a1.parent_54 = renderablePtr_800bdc5c;
      renderablePtr_800bdc5c.child_50 = a1;
    } else {
      //LAB_80023c08
      a1.parent_54 = null;
    }

    //LAB_80023c0c
    renderablePtr_800bdc5c = a1;
    return a1;
  }

  public static Renderable58 allocateManualRenderable() {
    return allocateManualRenderable(uiFile_800bdc3c.uiElements_0000(), null);
  }

  public static Renderable58 allocateManualRenderable(final UiType uiType, @Nullable Renderable58 renderable) {
    if(renderable == null) {
      renderable = new Renderable58();
    }

    //LAB_80023b7c
    renderable.flags_00 = 0;
    renderable.glyph_04 = 0;
    renderable.ticksPerFrame_08 = uiType.entries_08[0].ticksPerFrame();
    renderable.animationLoopsCompletedCount_0c = 0;
    renderable.startGlyph_10 = 0;
    renderable.endGlyph_14 = uiType.entries_08.length - 1;
    renderable.repeatStartGlyph_18 = 0;
    renderable.repeatEndGlyph_1c = 0;
    renderable.uiType_20 = uiType;

    renderable.deallocationGroup_28 = 0;
    renderable.tpage_2c = 0;
    renderable.widthScale = 1.0f;
    renderable.heightScale_38 = 1.0f;
    renderable.z_3c = 36;
    renderable.x_40 = 0;
    renderable.y_44 = 0;
    renderable.child_50 = null;

    return renderable;
  }

  @Method(0x80023c28L)
  public static void uploadRenderables() {
    uploadRenderable(renderablePtr_800bdc5c, 0, 0);
  }

  private static final List<Renderable58> renderables = new ArrayList<>();
  private static final Comparator<Renderable58> renderableDepthComparator = Comparator.comparingDouble((Renderable58 r) -> r.z_3c).reversed();

  public static void uploadRenderable(Renderable58 renderable, final int x, final int y) {
    while(renderable != null) {
      renderable.baseX = x;
      renderable.baseY = y;
      renderables.add(renderable);
      renderable = renderable.parent_54;
    }
  }

  public static void renderUi() {
    if(renderables.isEmpty()) {
      return;
    }

    renderables.sort(renderableDepthComparator);

    //LAB_80023c8c
    for(int i = 0; i < renderables.size(); i++) {
      final Renderable58 renderable = renderables.get(i);

      // If a glyph is unloaded after being queued
      if(renderable.glyph_04 == -1) {
        continue;
      }

      boolean forceUnload = false;
      final UiPart[] entries = renderable.uiType_20.entries_08;

      if((renderable.flags_00 & Renderable58.FLAG_NO_ANIMATION) == 0) {
        if(tickCount_800bb0fc % Math.max(1, 3 - vsyncMode_8007a3b8) == 0) {
          renderable.ticksPerFrame_08--;
        }

        if(renderable.ticksPerFrame_08 < 0) {
          if((renderable.flags_00 & Renderable58.FLAG_BACKWARDS_ANIMATION) != 0) {
            renderable.glyph_04--;

            if(renderable.glyph_04 < renderable.startGlyph_10) {
              if((renderable.flags_00 & Renderable58.FLAG_DELETE_AFTER_ANIMATION) != 0) {
                forceUnload = true;
                renderable.flags_00 |= Renderable58.FLAG_INVISIBLE;
              }

              //LAB_80023d0c
              if(renderable.repeatStartGlyph_18 != 0) {
                renderable.startGlyph_10 = renderable.repeatStartGlyph_18;

                if(renderable.repeatEndGlyph_1c != 0) {
                  renderable.endGlyph_14 = renderable.repeatEndGlyph_1c;
                } else {
                  //LAB_80023d34
                  renderable.endGlyph_14 = renderable.repeatStartGlyph_18;
                  renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
                }

                //LAB_80023d48
                renderable.repeatStartGlyph_18 = 0;
                renderable.flags_00 &= ~Renderable58.FLAG_BACKWARDS_ANIMATION;
              }

              //LAB_80023d5c
              //LAB_80023e00
              renderable.glyph_04 = renderable.endGlyph_14;
              renderable.animationLoopsCompletedCount_0c++;
            }
          } else {
            //LAB_80023d6c
            renderable.glyph_04++;

            if(renderable.glyph_04 > renderable.endGlyph_14) {
              if((renderable.flags_00 & Renderable58.FLAG_DELETE_AFTER_ANIMATION) != 0) {
                forceUnload = true;
                renderable.flags_00 |= Renderable58.FLAG_INVISIBLE;
              }

              //LAB_80023da4
              if(renderable.repeatStartGlyph_18 != 0) {
                renderable.startGlyph_10 = renderable.repeatStartGlyph_18;

                if(renderable.repeatEndGlyph_1c != 0) {
                  renderable.endGlyph_14 = renderable.repeatEndGlyph_1c;
                } else {
                  //LAB_80023dcc
                  renderable.endGlyph_14 = renderable.repeatStartGlyph_18;
                  renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
                }

                //LAB_80023de0
                renderable.repeatStartGlyph_18 = 0;
                renderable.flags_00 &= ~Renderable58.FLAG_BACKWARDS_ANIMATION;
              }

              //LAB_80023df4
              //LAB_80023e00
              renderable.glyph_04 = renderable.startGlyph_10;
              renderable.animationLoopsCompletedCount_0c++;
            }
          }

          //LAB_80023e08
          renderable.ticksPerFrame_08 = entries[renderable.glyph_04].ticksPerFrame() - 1;
        }
      }

      //LAB_80023e28
      if((renderable.flags_00 & Renderable58.FLAG_INVISIBLE) == 0) {
        final int centreX = displayWidth_1f8003e0 / 2 + 8;

        final RenderableMetrics14[] metricses = entries[renderable.glyph_04].metrics_00();

        //LAB_80023e94
        for(int metricsIndex = metricses.length - 1; metricsIndex >= 0; metricsIndex--) {
          final RenderableMetrics14 metrics = metricses[metricsIndex];

          final float x1;
          final float width;
          if(MathHelper.flEq(renderable.widthScale, 1.0f)) {
            if(metrics.widthScale_10 < 0) {
              width = -metrics.width_08;
              x1 = renderable.x_40 + metrics.x_02 - centreX + metrics.width_08;
            } else {
              //LAB_80023f20
              width = metrics.width_08;
              x1 = renderable.x_40 + metrics.x_02 - centreX;
            }
          } else {
            //LAB_80023f40
            final float widthScale = !MathHelper.flEq(renderable.widthScale, 0.0f) ? renderable.widthScale : metrics.widthScale_10;

            //LAB_80023f4c
            //LAB_80023f68
            final float scaledWidth = Math.abs(metrics.width_08 * widthScale);
            if(metrics.widthScale_10 < 0) {
              width = -scaledWidth;
              x1 = renderable.x_40 + metrics.width_08 / 2.0f + metrics.x_02 - centreX - scaledWidth / 2.0f + scaledWidth;
            } else {
              //LAB_80023fb4
              width = scaledWidth;
              x1 = renderable.x_40 + metrics.width_08 / 2.0f + metrics.x_02 - centreX - scaledWidth / 2.0f;
            }
          }

          //LAB_80023fe4
          final float y1;
          final float height;
          if(MathHelper.flEq(renderable.heightScale_38, 1.0f)) {
            if(metrics.heightScale_12 < 0) {
              height = -metrics.height_0a;
              y1 = renderable.y_44 + metrics.y_03 - 120.0f + metrics.height_0a;
            } else {
              //LAB_80024024
              height = metrics.height_0a;
              y1 = renderable.y_44 + metrics.y_03 - 120.0f;
            }
          } else {
            //LAB_80024044
            final float heightScale = !MathHelper.flEq(renderable.heightScale_38, 0.0f) ? renderable.heightScale_38 : metrics.heightScale_12;

            //LAB_80024050
            //LAB_8002406c
            final float scaledHeight = Math.abs(metrics.height_0a * heightScale);
            if(metrics.heightScale_12 < 0) {
              height = -scaledHeight;
              y1 = renderable.y_44 + metrics.height_0a / 2.0f + metrics.y_03 - scaledHeight / 2.0f - 120.0f + scaledHeight;
            } else {
              //LAB_800240b8
              height = scaledHeight;
              y1 = renderable.y_44 + metrics.height_0a / 2.0f + metrics.y_03 - scaledHeight / 2.0f - 120.0f;
            }
          }

          //LAB_800240e8
          //LAB_80024144
          //LAB_800241b4
          final int clut = renderable.clut_30 != 0 ? renderable.clut_30 : metrics.clut_04 & 0x7fff;
          final int tpage = renderable.tpage_2c != 0 ? metrics.tpage_06 & 0x60 | renderable.tpage_2c : metrics.tpage_06 & 0x7f;

          //LAB_8002424c
          if(renderable.uiType_20.obj != null) {
            final MV transforms = new MV();
            transforms.scaling(width, height, 1.0f);
            transforms.transfer.set(x1 + renderable.baseX + centreX - 8 + (width < 0 ? 1.0f : 0.0f), y1 + renderable.baseY + 120.0f + (height < 0 ? 1.0f : 0.0f), renderable.z_3c * 4.0f);

            int tpageX = (tpage & 0b1111) * 64;
            int clutX = (clut & 0b111111) * 16;

            if(!renderable.useOriginalTpage) {
              tpageX -= 512;
              clutX -= 512;
            }

            final QueuedModelStandard model = RENDERER
              .queueOrthoModel(renderable.uiType_20.obj, transforms, QueuedModelStandard.class)
              .vertices(metrics.vertexStart, 4)
              .tpageOverride(tpageX, (tpage & 0b10000) != 0 ? 256 : 0)
              .clutOverride(clutX, clut >>> 6)
            ;

            if((metrics.clut_04 & 0x8000) != 0) {
              model
                .translucency(Translucency.of(tpage >>> 5 & 0b11))
                .translucentDepthComparator(GL_LEQUAL)
              ;
            }

            if(renderable.widthCut != 0 || renderable.heightCut != 0) {
              final int y;
              final int h;
              if(renderable.heightCut == 0) {
                y = (int)transforms.transfer.y;
                h = (int)height;
              } else {
                y = (int)(transforms.transfer.y + height - renderable.heightCut);
                h = renderable.heightCut;
              }

              final int w;
              if(renderable.widthCut == 0) {
                w = (int)width;
              } else {
                w = (int)(width - renderable.widthCut);
              }

              model.scissor((int)transforms.transfer.x, y, w, h);
            }
          }
        }
      }

      //LAB_80024280
      if((renderable.flags_00 & Renderable58.FLAG_DELETE_AFTER_RENDER) != 0 || forceUnload) {
        //LAB_800242a8
        unloadRenderable(renderable);
      }
    }

    //LAB_800242b8
    renderables.clear();
  }

  @Method(0x800242e8L)
  public static void unloadRenderable(final Renderable58 a0) {
    final Renderable58 v0 = a0.child_50;
    final Renderable58 v1 = a0.parent_54;

    if(v0 == null) {
      if(v1 == null) {
        renderablePtr_800bdc5c = null;
      } else {
        //LAB_80024320
        renderablePtr_800bdc5c = v1;
        v1.child_50 = null;
      }
      //LAB_80024334
    } else if(v1 == null) {
      v0.parent_54 = null;
    } else {
      //LAB_80024350
      v0.parent_54 = v1;
      v1.child_50 = a0.child_50;
    }
  }

  @Method(0x8002437cL)
  public static void deallocateRenderables(final int a0) {
    // Clear out UI render queue
    renderables.clear();

    Renderable58 s0 = renderablePtr_800bdc5c;

    if(s0 != null) {
      //LAB_800243b4
      while(s0.parent_54 != null) {
        final Renderable58 a0_0 = s0;
        s0 = s0.parent_54;

        if(a0_0.deallocationGroup_28 <= a0) {
          unloadRenderable(a0_0);
        }

        //LAB_800243d0
      }

      //LAB_800243e0
      if(s0.deallocationGroup_28 <= a0) {
        unloadRenderable(s0);
      }

      //LAB_800243fc
      if(a0 != 0) {
        saveListUpArrow_800bdb94 = null;
        saveListDownArrow_800bdb98 = null;
        renderablePtr_800bdba4 = null;
        renderablePtr_800bdba8 = null;
      }
    }

    //LAB_80024460
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
      script.params_20[1].set(takeItemId(REGISTRIES.items.getEntry(LodMod.id(LodMod.ITEM_IDS[itemId - 192])).get()) ? 0 : 0xff);
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
    loadDrgnDir(0, 6669, Scus94491BpeSegment_8002::basicUiTexturesLoaded);

    textZ_800bdf00 = 13;
    clearCharacterStats();

    //LAB_800250c0
    //LAB_800250ec
    for(int i = 0; i < 8; i++) {
      textboxes_800be358[i] = new Textbox4c();
      textboxes_800be358[i].state_00 = TextboxState.UNINITIALIZED_0;

      textboxText_800bdf38[i] = new TextboxText84();
      textboxText_800bdf38[i].state_00 = TextboxTextState.UNINITIALIZED_0;

      textboxArrows_800bdea0[i] = new TextboxArrow0c();

      setTextboxArrowPosition(i, false);
    }

    //LAB_80025118
    for(int i = 0; i < 10; i++) {
      textboxVariables_800bdf10[i] = 0;
    }
  }

  @ScriptDescription("Sets a textbox's text and type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The textbox type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.STRING, name = "text", description = "The textbox text")
  @Method(0x80025158L)
  public static FlowControl scriptSetTextboxContents(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    clearTextboxText(textboxIndex);

    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    textboxText.type_04 = script.params_20[1].get();
    textboxText.flags_08 |= TextboxText84.SHOW_ARROW;
    textboxText.str_24 = LodString.fromParam(script.params_20[2]);
    textboxText.chars_58 = new TextboxChar08[textboxText.chars_1c * (textboxText.lines_1e + 1)];
    Arrays.setAll(textboxText.chars_58, i -> new TextboxChar08());
    calculateAppropriateTextboxBounds(textboxIndex, textboxText.x_14, textboxText.y_16);
    return FlowControl.CONTINUE;
  }

  /** Allocate textbox used in yellow-name textboxes and combat effect popups, maybe others */
  @ScriptDescription("Adds a textbox to a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "packedData", description = "Bit flags for textbox properties")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The textbox x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The textbox y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The textbox width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The textbox height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.STRING, name = "text", description = "The textbox text")
  @Method(0x800254bcL)
  public static FlowControl scriptAddTextbox(final RunningScript<?> script) {
    final int packed = script.params_20[1].get();

    if(packed != 0) {
      final int textboxIndex = script.params_20[0].get();
      final int textType = textboxTextType_80052ba8[packed >>> 8 & 0xf];
      clearTextbox(textboxIndex);

      final Textbox4c textbox = textboxes_800be358[textboxIndex];
      final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

      textbox.backgroundType_04 = BackgroundType.fromInt(textboxMode_80052b88[packed >>> 4 & 0xf]);
      textbox.renderBorder_06 = renderBorder_80052b68[packed & 0xf];
      textbox.x_14 = script.params_20[2].get();
      textbox.y_16 = script.params_20[3].get();
      textbox.chars_18 = script.params_20[4].get() + 1;
      textbox.lines_1a = script.params_20[5].get() + 1;
      textboxText.type_04 = textType;
      textboxText.str_24 = LodString.fromParam(script.params_20[6]);

      // This is a stupid hack to allow inns to display 99,999,999 gold without the G falling down to the next line (see GH#546)
      if("?Funds ?G".equals(textboxText.str_24.get())) {
        textbox.chars_18++;
      }

      clearTextboxText(textboxIndex);

      if(textType == 1 && (packed & 0x1000) != 0) {
        textboxText.flags_08 |= TextboxText84.NO_INPUT;
      }

      //LAB_8002562c
      //LAB_80025630
      if(textType == 3) {
        textboxText.selectionIndex_6c = -1;
      }

      //LAB_80025660
      if(textType == 4) {
        textboxText.flags_08 |= TextboxText84.HAS_NAME;
      }

      //LAB_80025690
      /* Not a retail flag. Used to remove arrows from overlapping textboxes for Phantom Ship's code-locked chest. */
      if((packed & TextboxText84.NO_ARROW) == 0) {
        textboxText.flags_08 |= TextboxText84.SHOW_ARROW;
      }
      textboxText.chars_58 = new TextboxChar08[textboxText.chars_1c * (textboxText.lines_1e + 1)];
      Arrays.setAll(textboxText.chars_58, i -> new TextboxChar08());
      calculateAppropriateTextboxBounds(textboxIndex, textboxText.x_14, textboxText.y_16);
    }

    //LAB_800256f0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a textbox with selectable lines (like Yes/No, Enter/Don't Enter)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "minSelectionIndex", description = "The first selectable line index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "maxSelectionIndex", description = "The last selectable line index")
  @Method(0x80025718L)
  public static FlowControl scriptAddSelectionTextbox(final RunningScript<?> script) {
    final TextboxText84 textboxText = textboxText_800bdf38[script.params_20[0].get()];

    textboxText.selectionIndex_6c = -1;
    textboxText.minSelectionLine_72 = script.params_20[1].get();
    textboxText.maxSelectionLine_70 = script.params_20[2].get();

    if(textboxText.state_00 == TextboxTextState._13) {
      textboxText.state_00 = TextboxTextState.TRANSITION_AFTER_TIMEOUT_23;
      textboxText.ticksUntilStateTransition_64 = 10 * currentEngineState_8004dd04.tickMultiplier();
      textboxText.stateAfterTransition_78 = TextboxTextState.SELECTION_22;
      playSound(0, 4, (short)0, (short)0);
    }

    //LAB_800257bc
    textboxText.flags_08 |= TextboxText84.SELECTION;
    return FlowControl.CONTINUE;
  }

  public static void initTextboxGeometry() {
    textboxBackgroundObj = new QuadBuilder("TextboxBackground")
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .pos(-1.0f, -1.0f, 0.0f)
      .size(2.0f, 2.0f)
      .rgb(0.0f, 41.0f / 255.0f, 159.0f / 255.0f)
      .monochrome(0, 0.0f)
      .monochrome(3, 0.0f)
      .build();
    textboxBackgroundObj.persistent = true;

    for(int borderIndex = 0; borderIndex < 8; borderIndex++) {
      final TextboxBorderMetrics0c borderMetrics = textboxBorderMetrics_800108b0[borderIndex];
      final int u = borderMetrics.u_04;
      final int v = borderMetrics.v_06;

      textboxBorderObjs[borderIndex] = new QuadBuilder("TextboxBorder" + borderIndex)
        .bpp(Bpp.BITS_4)
        .clut(832, 484)
        .vramPos(896, 256)
        .size(16, 16)
        .uv(u, v)
        .build();
      textboxBorderObjs[borderIndex].persistent = true;
    }

    for(int i = 0; i < textboxArrowObjs.length; i++) {
      textboxArrowObjs[i] = new QuadBuilder("TextboxArrow" + i)
        .bpp(Bpp.BITS_4)
        .monochrome(0.5f)
        .clut(1008, 484)
        .vramPos(896, 256)
        .pos(-8.0f, -6.0f, 0.0f)
        .size(16.0f, 14.0f)
        .uv(64.0f + i * 16.0f, 0.0f)
        .build();
      textboxArrowObjs[i].persistent = true;
    }

    textboxSelectionObj = new QuadBuilder("TextboxSelection")
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .rgb(0.5f, 0.19607843f, 0.39215687f)
      .size(1.0f, 12.0f)
      .build();
    textboxSelectionObj.persistent = true;
  }

  /** Deallocate textbox used in yellow-name textboxes and combat effect popups, maybe others */
  @Method(0x800257e0L)
  public static void clearTextbox(final int textboxIndex) {
    if(textboxText_800bdf38[textboxIndex].state_00 != TextboxTextState.UNINITIALIZED_0) {
      textboxText_800bdf38[textboxIndex].delete();
    }

    //LAB_80025824
    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    textbox.state_00 = TextboxState._1;
    textbox.renderBorder_06 = false;
    textbox.flags_08 = 0;
    textbox.z_0c = 14;
    textbox.currentTicks_10 = 0;
    textbox.width_1c = 0;
    textbox.height_1e = 0;
    textbox.animationWidth_20 = 0x1000;
    textbox.animationHeight_22 = 0x1000;
    textbox.animationTicks_24 = 0;
    textbox.currentX_28 = 0;
    textbox.currentY_2c = 0;
    textbox.stepX_30 = 0;
    textbox.stepY_34 = 0;
    textbox._38 = 0;
    textbox._3c = 0;
  }

  @Method(0x800258a8L)
  public static void clearTextboxText(final int a0) {
    final TextboxText84 textboxText = textboxText_800bdf38[a0];
    textboxText.state_00 = TextboxTextState._1;
    textboxText.flags_08 = 0;
    textboxText.z_0c = 13;
    textboxText.textColour_28 = TextColour.WHITE;
    textboxText.scrollSpeed_2a = 2.0f / currentEngineState_8004dd04.tickMultiplier();
    textboxText.scrollAmount_2c = 0.0f;
    textboxText.charIndex_30 = 0;
    textboxText.charX_34 = 0;
    textboxText.charY_36 = 0;
    textboxText._3a = 0.0f;
    textboxText._3e = 1;
    textboxText._40 = 0;
    textboxText.pauseTimer_44 = 0;

    final Textbox4c struct4c = textboxes_800be358[a0];
    textboxText.x_14 = struct4c.x_14;
    textboxText.y_16 = struct4c.y_16;
    textboxText.chars_1c = struct4c.chars_18 - 1;
    textboxText.lines_1e = struct4c.lines_1a - 1;
    textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 9 / 2;
    textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6;

    //LAB_800259b4
    for(int i = 0; i < 8; i++) {
      textboxText.digits_46[i] = 0;
    }

    //LAB_800259e4
    textboxText._5c = TextboxTextState.UNINITIALIZED_0;
    textboxText.selectionLine_60 = 0;
    textboxText.ticksUntilStateTransition_64 = 0;
    textboxText.selectionLine_68 = 0;
    textboxText.selectionIndex_6c = 0;
    textboxText.maxSelectionLine_70 = 0;
    textboxText.minSelectionLine_72 = 0;
    textboxText.stateAfterTransition_78 = TextboxTextState.UNINITIALIZED_0;
    textboxText.element_7c = 0;
    textboxText.digitIndex_80 = 0;

    textboxText.waitTicks = 0;
  }

  @Method(0x80025a04L)
  public static void handleTextbox(final int textboxIndex) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    switch(textbox.state_00) {
      case _1 -> {
        switch(textbox.backgroundType_04) {
          case NO_BACKGROUND -> {
            //LAB_80025ab8
            textbox.state_00 = TextboxState._4;
            textbox.flags_08 ^= Textbox4c.RENDER_BACKGROUND;
          }

          case NORMAL -> {
            //LAB_80025b54
            //LAB_80025b5c
            textbox.width_1c = textbox.chars_18 * 9 / 2;
            textbox.height_1e = textbox.lines_1a * 6;

            if((textbox.flags_08 & Textbox4c.NO_ANIMATE_OUT) == 0) {
              textbox.state_00 = TextboxState._5;
            } else {
              textbox.state_00 = TextboxState._6;
            }

            //LAB_80025bc0
            textbox.flags_08 |= Textbox4c.RENDER_BACKGROUND;
          }

          case ANIMATE_IN_OUT -> {
            //LAB_80025ad4
            textbox.state_00 = TextboxState._2;
            textbox.flags_08 |= Textbox4c.ANIMATING;
            textbox.currentTicks_10 = 0;
            textbox.animationTicks_24 = 60 / vsyncMode_8007a3b8 / 4;

            if((textbox.flags_08 & 0x2) != 0) {
              textbox.stepX_30 = (textbox.currentX_28 - textbox._38) / textbox.animationTicks_24;
              textbox.stepY_34 = (textbox.currentY_2c - textbox._3c) / textbox.animationTicks_24;
            }
          }
        }
      }

      case _2 -> {
        textbox.flags_08 |= Textbox4c.RENDER_BACKGROUND;

        if(textbox.backgroundType_04 == BackgroundType.ANIMATE_IN_OUT) {
          textbox.animationWidth_20 = (textbox.currentTicks_10 << 12) / textbox.animationTicks_24;
          textbox.animationHeight_22 = (textbox.currentTicks_10 << 12) / textbox.animationTicks_24;
          textbox.width_1c = textbox.chars_18 * 9 / 2 * textbox.animationWidth_20 >> 12;
          textbox.height_1e = textbox.animationHeight_22 * 6 * textbox.lines_1a >> 12;
          textbox.currentTicks_10++;

          if((textbox.flags_08 & 0x2) != 0) {
            textbox.currentX_28 -= textbox.stepX_30;
            textbox.currentY_2c -= textbox.stepY_34;
            textbox.x_14 = textbox.currentX_28;
            textbox.y_16 = textbox.currentY_2c;
          }

          //LAB_80025cf0
          if(textbox.currentTicks_10 >= textbox.animationTicks_24) {
            textbox.flags_08 ^= Textbox4c.ANIMATING;
            textbox.width_1c = textbox.chars_18 * 9 / 2;
            textbox.height_1e = textbox.lines_1a * 6;

            if((textbox.flags_08 & Textbox4c.NO_ANIMATE_OUT) == 0) {
              textbox.state_00 = TextboxState._5;
            } else {
              textbox.state_00 = TextboxState._6;
            }

            //LAB_80025d5c
            if((textbox.flags_08 & 0x2) != 0) {
              textbox.x_14 = textbox._38;
              textbox.y_16 = textbox._3c;
            }
          }

          break;
        }

        //LAB_80025d84
        if((textbox.flags_08 & Textbox4c.NO_ANIMATE_OUT) == 0) {
          textbox.state_00 = TextboxState._5;
        } else {
          textbox.state_00 = TextboxState._6;
        }
      }

      case ANIMATE_OUT_3 -> {
        if(textbox.backgroundType_04 == BackgroundType.ANIMATE_IN_OUT) {
          textbox.animationWidth_20 = (textbox.currentTicks_10 << 12) / textbox.animationTicks_24;
          textbox.animationHeight_22 = (textbox.currentTicks_10 << 12) / textbox.animationTicks_24;
          textbox.width_1c = textbox.chars_18 * 9 / 2 * textbox.animationWidth_20 >> 12;
          textbox.height_1e = textbox.animationHeight_22 * 6 * textbox.lines_1a >> 12;
          textbox.currentTicks_10--;

          if(textbox.currentTicks_10 <= 0) {
            textbox.width_1c = 0;
            textbox.height_1e = 0;
            textbox.state_00 = TextboxState.UNINITIALIZED_0;
            textbox.flags_08 ^= Textbox4c.ANIMATING;
          }

          break;
        }

        //LAB_80025e94
        textbox.state_00 = TextboxState.UNINITIALIZED_0;
      }

      case _4, _5 -> {
        if(textboxText_800bdf38[textboxIndex].state_00 == TextboxTextState.UNINITIALIZED_0) {
          if(textbox.backgroundType_04 == BackgroundType.ANIMATE_IN_OUT) {
            textbox.state_00 = TextboxState.ANIMATE_OUT_3;
            textbox.flags_08 |= Textbox4c.ANIMATING;

            final int ticks = 60 / vsyncMode_8007a3b8 / 4;
            textbox.currentTicks_10 = ticks;
            textbox.animationTicks_24 = ticks;
          } else {
            //LAB_80025f30
            textbox.state_00 = TextboxState.UNINITIALIZED_0;
          }

          //LAB_80025f34
          setTextboxArrowPosition(textboxIndex, false);
        }
      }
    }

    //LAB_80025f3c
  }

  @Method(0x80025f4cL)
  public static void renderTextboxBackground(final int textboxIndex) {
    //LAB_80025f7c
    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    if(textbox.backgroundType_04 != BackgroundType.NO_BACKGROUND) {
      if(textbox.state_00 != TextboxState._1) {
        if(textbox.x_14 != textbox.oldX || textbox.y_16 != textbox.oldY || textbox.width_1c != textbox.oldW || textbox.height_1e != textbox.oldH) {
          textbox.backgroundTransforms.transfer.set(textbox.x_14, textbox.y_16, textbox.z_0c * 4.0f + 1.0f);
          textbox.backgroundTransforms.scaling(textbox.width_1c, textbox.height_1e, 1.0f);

          textbox.oldX = textbox.x_14;
          textbox.oldY = textbox.y_16;
          textbox.oldW = textbox.width_1c;
          textbox.oldH = textbox.height_1e;
          textbox.updateBorder = true;
        }

        RENDERER.queueOrthoModel(textboxBackgroundObj, textbox.backgroundTransforms, QueuedModelStandard.class);

        if(textbox.renderBorder_06) {
          renderTextboxBorder(textboxIndex, textbox.x_14 - textbox.width_1c, textbox.y_16 - textbox.height_1e, textbox.x_14 + textbox.width_1c, textbox.y_16 + textbox.height_1e);
        }
      }
    }

    //LAB_800261a0
  }

  @Method(0x800261c0L)
  public static void renderTextboxBorder(final int textboxIndex, final float boxLeft, final float boxTop, final float boxRight, final float boxBottom) {
    final float[] xs = {
      boxLeft + 4,
      boxRight - 4,
      boxLeft + 4,
      boxRight - 4,
    };

    final float[] ys = {
      boxTop + 5,
      boxTop + 5,
      boxBottom - 5,
      boxBottom - 5,
    };

    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    if(textbox.animationWidth_20 != textbox.oldScaleW || textbox.animationHeight_22 != textbox.oldScaleH) {
      textbox.updateBorder = true;
    }

    //LAB_800262e4
    for(int borderIndex = 0; borderIndex < 8; borderIndex++) {
      final TextboxBorderMetrics0c borderMetrics = textboxBorderMetrics_800108b0[borderIndex];

      int w = borderMetrics.w_08;
      int h = borderMetrics.h_0a;
      if((textbox.flags_08 & Textbox4c.ANIMATING) != 0) {
        w = w * textbox.animationWidth_20 >> 12;
        h = h * textbox.animationHeight_22 >> 12;
      }

      //LAB_8002637c
      final float left = xs[borderMetrics.topLeftVertexIndex_00] - w;
      final float right = xs[borderMetrics.bottomRightVertexIndex_02] + w;
      final float top = ys[borderMetrics.topLeftVertexIndex_00] - h;
      final float bottom = ys[borderMetrics.bottomRightVertexIndex_02] + h;

      if(textbox.updateBorder) {
        textbox.borderTransforms[borderIndex].transfer.set(left, top, textbox.z_0c * 4.0f);
        textbox.borderTransforms[borderIndex]
          .scaling((right - left) / 16.0f, (bottom - top) / 16.0f, 1.0f);
      }

      RENDERER.queueOrthoModel(textboxBorderObjs[borderIndex], textbox.borderTransforms[borderIndex], QueuedModelStandard.class);
    }

    textbox.oldScaleW = textbox.animationWidth_20;
    textbox.oldScaleH = textbox.animationHeight_22;
    textbox.updateBorder = false;
  }

  /** I think this method handles textboxes */
  @Method(0x800264b0L)
  public static void handleTextboxText(final int textboxIndex) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    switch(textboxText.state_00) {
      case _1 -> {
        //LAB_8002663c
        if((textbox.flags_08 & 0x1) == 0) {
          switch(textboxText.type_04) {
            case 0 -> textboxText.state_00 = TextboxTextState._12;

            case 2 -> {
              textboxText.state_00 = TextboxTextState.SCROLL_TEXT_UP_10;
              textboxText.flags_08 |= 0x1;
              textboxText.scrollSpeed_2a = 1.0f / currentEngineState_8004dd04.tickMultiplier();
              textboxText.charX_34 = 0;
              textboxText.charY_36 = textboxText.lines_1e;
            }

            case 3 -> {
              textboxText.state_00 = TextboxTextState.TRANSITION_AFTER_TIMEOUT_23;
              textboxText.flags_08 |= 0x1;
              textboxText.scrollSpeed_2a = 1.0f / currentEngineState_8004dd04.tickMultiplier();
              textboxText.charX_34 = 0;
              textboxText.charY_36 = 0;
              textboxText.ticksUntilStateTransition_64 = 10 * currentEngineState_8004dd04.tickMultiplier();
              textboxText.stateAfterTransition_78 = TextboxTextState._17;
              playSound(0, 4, (short)0, (short)0);
            }

            case 4 -> {
              //LAB_80026780
              do {
                processTextboxLine(textboxIndex);
              } while((textboxText.flags_08 & TextboxText84.PROCESSED_NEW_LINE) == 0);

              textboxText.flags_08 ^= TextboxText84.PROCESSED_NEW_LINE;
            }

            default ->
              //LAB_800267a0
              textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
          }
        }
      }

      case _2 -> textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;

      //LAB_80026538
      case PROCESS_TEXT_4 ->
        //LAB_800267c4
        processTextboxLine(textboxIndex);

      case SCROLL_TEXT_5 -> {
        //LAB_800267d4
        if((textboxText.flags_08 & 0x1) != 0) {
          //LAB_800267f4
          if(textboxText._3a < textboxText.lines_1e - ((textboxText.flags_08 & TextboxText84.HAS_NAME) == 0 ? 1 : 2)) {
            //LAB_80026828
            textboxText.state_00 = TextboxTextState.SCROLL_TEXT_DOWN_9;
            textboxText._3a += 1.0f / currentEngineState_8004dd04.tickMultiplier();
            scrollTextboxDown(textboxIndex);
          } else {
            textboxText.flags_08 ^= 0x1;
            textboxText._3a = 0.0f;
            setTextboxArrowPosition(textboxIndex, true);
          }
          //LAB_8002684c
        } else if((textboxText.flags_08 & TextboxText84.NO_INPUT) != 0) {
          textboxText.state_00 = TextboxTextState.SCROLL_TEXT_DOWN_9;
          textboxText.flags_08 |= 0x1;
        } else {
          //LAB_8002686c
          if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH) || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
            setTextboxArrowPosition(textboxIndex, false);

            if(textboxText.type_04 == 1 || textboxText.type_04 == 4) {
              //LAB_800268b4
              textboxText.state_00 = TextboxTextState.SCROLL_TEXT_DOWN_9;
              textboxText.flags_08 |= 0x1;
            }

            if(textboxText.type_04 == 2) {
              //LAB_800268d0
              textboxText.state_00 = TextboxTextState.SCROLL_TEXT_UP_10;
            }
          }
        }
      }

      case _6 -> {
        //LAB_800268dc
        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH) || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
          textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
        }
      }

      case _7 -> {
        //LAB_800268fc
        textboxText._40++;
        if(textboxText._40 >= textboxText._3e) {
          textboxText._40 = 0;
          textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
        }

        //LAB_80026928
        if((textboxText.flags_08 & TextboxText84.NO_INPUT) == 0) {
          if(Input.getButtonState(InputAction.BUTTON_SOUTH) || CONFIG.getConfig(CoreMod.QUICK_TEXT_CONFIG.get())) {
            boolean found = false;

            //LAB_80026954
            for(int lineIndex = 0; lineIndex < 4; lineIndex++) {
              processTextboxLine(textboxIndex);

              if(textboxText.state_00 == TextboxTextState.SCROLL_TEXT_5 || textboxText.state_00 == TextboxTextState._6 || textboxText.state_00 == TextboxTextState._15 || textboxText.state_00 == TextboxTextState._11 || textboxText.state_00 == TextboxTextState._13) {
                //LAB_8002698c
                found = true;
                break;
              }

              //LAB_80026994
            }

            //LAB_800269a0
            if(!found) {
              textboxText._40 = 0;
              textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
            }
          }
        }
      }

      case WAIT_FOR_PAUSE_8 -> {
        //LAB_800269cc
        if(textboxText.pauseTimer_44 > 0) {
          //LAB_800269e8
          textboxText.pauseTimer_44--;
        } else {
          //LAB_800269e0
          textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
        }
      }

      //LAB_80026554
      case SCROLL_TEXT_DOWN_9 ->
        //LAB_800269f0
        scrollTextboxDown(textboxIndex);

      //LAB_80026580
      case SCROLL_TEXT_UP_10 -> {
        //LAB_80026a00
        scrollTextboxUp(textboxIndex);

        if((textboxText.flags_08 & 0x4) != 0) {
          textboxText.flags_08 ^= 0x4;

          if((textboxText.flags_08 & 0x2) == 0) {
            //LAB_80026a5c
            do {
              processTextboxLine(textboxIndex);

              if(textboxText.state_00 == TextboxTextState._15) {
                textboxText._3a = 0.0f;
                textboxText.flags_08 |= 0x2;
                break;
              }
            } while(textboxText.state_00 != TextboxTextState.SCROLL_TEXT_5);

            //LAB_80026a8c
            textboxText.state_00 = TextboxTextState.SCROLL_TEXT_UP_10;
          } else {
            textboxText._3a += 1.0f / currentEngineState_8004dd04.tickMultiplier();

            if(MathHelper.flEq(textboxText._3a, textboxText.lines_1e + 1) || textboxText._3a > textboxText.lines_1e + 1) {
              textboxText.delete();
              textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
            }
          }
        }
      }

      case _11 -> {
        //LAB_80026a98
        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH) || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
          setTextboxArrowPosition(textboxIndex, false);
          clearTextboxChars(textboxIndex);

          textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
          textboxText.flags_08 ^= 0x1;
          textboxText.charX_34 = 0;
          textboxText.charY_36 = 0;
          textboxText._3a = 0.0f;

          if((textboxText.flags_08 & 0x8) != 0) {
            textboxText.state_00 = TextboxTextState._13;
          }
        }
      }

      case _12 -> {
        //LAB_80026af0
        if(textbox.state_00 == TextboxState.UNINITIALIZED_0) {
          textboxText.delete();
          textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
        }
      }

      case _13 -> {
        //LAB_80026b34
        textboxText.flags_08 |= 0x8;
        setTextboxArrowPosition(textboxIndex, true);

        //LAB_80026b4c
        do {
          processTextboxLine(textboxIndex);

          if(textboxText.state_00 == TextboxTextState.SCROLL_TEXT_5) {
            //LAB_80026b28
            textboxText.state_00 = TextboxTextState._11;
            break;
          }
        } while(textboxText.state_00 != TextboxTextState._15);

        //LAB_80026b6c
        if((textboxText.flags_08 & TextboxText84.NO_INPUT) != 0) {
          setTextboxArrowPosition(textboxIndex, false);
        }

        //LAB_80026ba0
        if(textboxText._3e != 0) {
          setTextboxArrowPosition(textboxIndex, false);
          textboxText._5c = textboxText.state_00;
          textboxText.state_00 = TextboxTextState._14;
        }

        //LAB_80026bc8
        if((textboxText.flags_08 & TextboxText84.SELECTION) != 0) {
          setTextboxArrowPosition(textboxIndex, false);
          textboxText.state_00 = TextboxTextState.TRANSITION_AFTER_TIMEOUT_23;
          textboxText.ticksUntilStateTransition_64 = 10 * currentEngineState_8004dd04.tickMultiplier();
          textboxText.stateAfterTransition_78 = TextboxTextState.SELECTION_22;
          textboxText.selectionLine_68 = textboxText.minSelectionLine_72;
          playSound(0, 4, (short)0, (short)0);
        }
      }

      case _14 -> {
        //LAB_80026c18
        if((textboxText.flags_08 & 0x40) == 0) {
          textboxText._40--;

          if(textboxText._40 <= 0) {
            textboxText._40 = textboxText._3e;

            if(textboxText._5c == TextboxTextState._11) {
              //LAB_80026c70
              clearTextboxChars(textboxIndex);
              textboxText.charX_34 = 0;
              textboxText.charY_36 = 0;
              textboxText._3a = 0.0f;
              textboxText.state_00 = TextboxTextState._13;
              textboxText.flags_08 ^= 0x1;
            } else if(textboxText._5c == TextboxTextState._15) {
              //LAB_80026c98
              //LAB_80026c9c
              textboxText.delete();
              textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
            }
          }
        }
      }

      case _15 -> {
        //LAB_80026cb0
        if((textboxText.flags_08 & TextboxText84.NO_INPUT) != 0) {
          textboxText.state_00 = TextboxTextState._16;
        } else {
          //LAB_80026cd0
          if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH) || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
            textboxText.delete();
            textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
            setTextboxArrowPosition(textboxIndex, false);
          }
        }
      }

      //LAB_800265d8
      case _16 -> {
        //LAB_80026cdc
        //LAB_80026ce8
        if((textboxText.flags_08 & 0x40) != 0) {
          textboxText.delete();
          textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
          setTextboxArrowPosition(textboxIndex, false);
        }
      }

      case _17 -> {
        //LAB_80026d20
        textboxText.lines_1e++;

        //LAB_80026d30
        do {
          processTextboxLine(textboxIndex);

          if(textboxText.state_00 == TextboxTextState._15) {
            textboxText._3a = 0.0f;
            textboxText.flags_08 |= 0x102;
            break;
          }
        } while(textboxText.state_00 != TextboxTextState.SCROLL_TEXT_5);

        //LAB_80026d64
        textboxText.state_00 = TextboxTextState._18;
        textboxText.selectionIndex_6c = -1;
        textboxText.lines_1e--;
      }

      //LAB_8002659c
      case _18 -> {
        //LAB_80026d94
        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          playSound(0, 2, (short)0, (short)0);
          textboxText.delete();
          textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
          textboxText.selectionIndex_6c = textboxText.selectionLine_68;
        } else {
          //LAB_80026df0
          if(!Input.getButtonState(InputAction.DPAD_DOWN) && !Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
            //LAB_80026ee8
            if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
              if((textboxText.flags_08 & 0x100) == 0 || textboxText.selectionLine_68 != 0) {
                //LAB_80026f38
                playSound(0, 1, (short)0, (short)0);

                int extraLines = 3;
                if(textboxText.selectionLine_60 > 0) {
                  textboxText.state_00 = TextboxTextState._19;
                  textboxText.selectionLine_60--;
                  textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
                  textboxText.selectionLine_68--;
                } else {
                  //LAB_80026f88
                  if((textboxText.flags_08 & 0x2) != 0) {
                    // TODO not sure about this block of code
                    if(MathHelper.flEq(textboxText._3a, 1.0f)) {
                      extraLines = 1;
                    } else {
                      if(MathHelper.flEq(textboxText._3a, 0.0f)) {
                        //LAB_80026fbc
                        extraLines = 2;
                      }

                      //LAB_80026fc0
                      textboxText._3a = 0.0f;
                      textboxText.flags_08 ^= 0x2;
                    }

                    //LAB_80026fe8
                    textboxText._3a -= 1.0f / currentEngineState_8004dd04.tickMultiplier();
                  }

                  //LAB_80027014
                  textboxText.selectionLine_68--;

                  if(textboxText.selectionLine_68 < 0) {
                    textboxText.selectionLine_68 = 0;
                  } else {
                    //LAB_80027044
                    textboxText.scrollAmount_2c = 12.0f;
                    rewindTextbox(textboxIndex);

                    final LodString str = textboxText.str_24;

                    //LAB_80027068
                    int lineIndex = 0;
                    do {
                      if(str.charAt(textboxText.charIndex_30 - 1) >>> 8 == TextboxText84.LINE) {
                        lineIndex++;
                      }

                      //LAB_80027090
                      if(lineIndex == textboxText.lines_1e + extraLines) {
                        break;
                      }

                      textboxText.charIndex_30--;
                    } while(textboxText.charIndex_30 > 0);

                    //LAB_800270b0
                    textboxText.charX_34 = 0;
                    textboxText.charY_36 = 0;
                    textboxText.flags_08 |= 0x80;

                    //LAB_800270dc
                    do {
                      processTextboxLine(textboxIndex);
                    } while(textboxText.charY_36 == 0 && textboxText.state_00 != TextboxTextState.SCROLL_TEXT_5);

                    //LAB_80027104
                    textboxText.state_00 = TextboxTextState._21;
                    textboxText.flags_08 ^= 0x80;
                  }
                }
              }
            }
          }

          textboxText.state_00 = TextboxTextState._19;
          textboxText.selectionLine_60++;
          textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
          textboxText.selectionLine_68++;

          if((textboxText.flags_08 & 0x100) == 0 || textboxText.charY_36 + 1 != textboxText.selectionLine_68) {
            //LAB_80026e68
            //LAB_80026e6c
            if(textboxText.selectionLine_60 < textboxText.lines_1e) {
              //LAB_80026ed0
              playSound(0, 1, (short)0, (short)0);

              //LAB_80026ee8
              if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
                if((textboxText.flags_08 & 0x100) == 0 || textboxText.selectionLine_68 != 0) {
                  //LAB_80026f38
                  playSound(0, 1, (short)0, (short)0);

                  int extraLines = 3;
                  if(textboxText.selectionLine_60 > 0) {
                    textboxText.state_00 = TextboxTextState._19;
                    textboxText.selectionLine_60--;
                    textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
                    textboxText.selectionLine_68--;
                  } else {
                    //LAB_80026f88
                    if((textboxText.flags_08 & 0x2) != 0) {
                      // TODO not sure about this block of code
                      if(MathHelper.flEq(textboxText._3a, 1.0f)) {
                        extraLines = 1;
                      } else {
                        if(MathHelper.flEq(textboxText._3a, 0.0f)) {
                          //LAB_80026fbc
                          extraLines = 2;
                        }

                        //LAB_80026fc0
                        textboxText._3a = 0.0f;
                        textboxText.flags_08 ^= 0x2;
                      }

                      //LAB_80026fe8
                      textboxText._3a -= 1.0f / currentEngineState_8004dd04.tickMultiplier();
                    }

                    //LAB_80027014
                    textboxText.selectionLine_68--;

                    if(textboxText.selectionLine_68 < 0) {
                      textboxText.selectionLine_68 = 0;
                    } else {
                      //LAB_80027044
                      textboxText.scrollAmount_2c = 12.0f;
                      rewindTextbox(textboxIndex);

                      final LodString str = textboxText.str_24;

                      //LAB_80027068
                      int lineIndex = 0;
                      do {
                        if(str.charAt(textboxText.charIndex_30 - 1) >>> 8 == TextboxText84.LINE) {
                          lineIndex++;
                        }

                        //LAB_80027090
                        if(lineIndex == textboxText.lines_1e + extraLines) {
                          break;
                        }

                        textboxText.charIndex_30--;
                      } while(textboxText.charIndex_30 > 0);

                      //LAB_800270b0
                      textboxText.charX_34 = 0;
                      textboxText.charY_36 = 0;
                      textboxText.flags_08 |= 0x80;

                      //LAB_800270dc
                      do {
                        processTextboxLine(textboxIndex);
                      } while(textboxText.charY_36 == 0 && textboxText.state_00 != TextboxTextState.SCROLL_TEXT_5);

                      //LAB_80027104
                      textboxText.state_00 = TextboxTextState._21;
                      textboxText.flags_08 ^= 0x80;
                    }
                  }
                }
              }
            } else {
              textboxText.selectionLine_60 = textboxText_800bdf38[textboxIndex].lines_1e - 1;
              textboxText.state_00 = TextboxTextState._20;
              textboxText.scrollAmount_2c = 0.0f;

              if(textboxText._3a == 1.0f) {
                textboxText.state_00 = TextboxTextState._18;
                textboxText.selectionLine_68--;
              }
            }
          } else {
            textboxText.state_00 = TextboxTextState._3;
            textboxText.selectionLine_60--;
            textboxText.selectionLine_68--;
          }
        }
      }

      case _19 -> {
        //LAB_8002711c
        textboxText.ticksUntilStateTransition_64--;

        if(textboxText.ticksUntilStateTransition_64 == 0) {
          textboxText.state_00 = TextboxTextState._18;

          if((textboxText.flags_08 & TextboxText84.SELECTION) != 0) {
            textboxText.state_00 = TextboxTextState.SELECTION_22;
          }
        }
      }

      case _20 -> {
        //LAB_8002715c
        textboxText.scrollAmount_2c += 4.0f / currentEngineState_8004dd04.tickMultiplier();

        if(textboxText.scrollAmount_2c >= 12.0f) {
          advanceTextbox(textboxIndex);
          textboxText.flags_08 |= 0x4;
          textboxText.scrollAmount_2c -= 12.0f;
          textboxText.charY_36 = textboxText.lines_1e;
        }

        //LAB_800271a8
        if((textboxText.flags_08 & 0x4) != 0) {
          textboxText.flags_08 ^= 0x4;

          if((textboxText.flags_08 & 0x2) == 0) {
            //LAB_8002720c
            //LAB_80027220
            do {
              processTextboxLine(textboxIndex);

              if(textboxText.state_00 == TextboxTextState._15) {
                textboxText._3a = 0.0f;
                textboxText.flags_08 |= 0x2;
                break;
              }
            } while(textboxText.state_00 != TextboxTextState.SCROLL_TEXT_5);
          } else {
            textboxText._3a += 1.0f / currentEngineState_8004dd04.tickMultiplier();

            if(MathHelper.flEq(textboxText._3a, textboxText.lines_1e + 1) || textboxText._3a > textboxText.lines_1e + 1) {
              textboxText.delete();
              textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
            }
          }

          //LAB_80027250
          //LAB_80027254
          textboxText.state_00 = TextboxTextState._18;
        }
      }

      //LAB_800265f4
      case _21 -> {
        //LAB_8002727c
        textboxText.scrollAmount_2c -= 4.0f / currentEngineState_8004dd04.tickMultiplier();
        if(textboxText.scrollAmount_2c <= 0.0f) {
          textboxText.charY_36 = 0;
          textboxText.scrollAmount_2c = 0.0f;
          textboxText.state_00 = TextboxTextState._18;
          textboxText.flags_08 |= 0x4;
        }

        //LAB_800272b0
        if((textboxText.flags_08 & 0x4) != 0) {
          final LodString str = textboxText.str_24;

          //LAB_800272dc
          int lineIndex = 0;
          do {
            final int control = str.charAt(textboxText.charIndex_30 + 1) >>> 8;
            if(control == TextboxText84.END) {
              //LAB_80027274
              textboxText.charIndex_30--;
              break;
            }

            if(control == TextboxText84.LINE) {
              lineIndex++;
            }

            //LAB_8002730c
            textboxText.charIndex_30++;
          } while(lineIndex != textboxText.lines_1e);

          //LAB_80027320
          textboxText.state_00 = TextboxTextState._18;
          textboxText.charIndex_30 += 2;
          textboxText.charX_34 = 0;
          textboxText.charY_36 = textboxText.lines_1e;
        }
      }

      case SELECTION_22 -> {
        //LAB_80027354
        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          playSound(0, 2, (short)0, (short)0);
          textboxText.delete();
          textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
          textboxText.selectionIndex_6c = textboxText.selectionLine_68 - textboxText.minSelectionLine_72;
        } else {
          //LAB_800273bc
          if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
            textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
            textboxText.selectionLine_68--;

            if(textboxText.selectionLine_68 < textboxText.minSelectionLine_72) {
              textboxText.selectionLine_68 = textboxText.minSelectionLine_72;
            } else {
              //LAB_80027404
              playSound(0, 1, (short)0, (short)0);
              textboxText.state_00 = TextboxTextState._19;
            }
          }

          //LAB_80027420
          if(Input.getButtonState(InputAction.DPAD_DOWN) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
            textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
            textboxText.selectionLine_68++;

            if(textboxText.selectionLine_68 > textboxText.maxSelectionLine_70) {
              textboxText.selectionLine_68 = textboxText.maxSelectionLine_70;
            } else {
              //LAB_80027480
              //LAB_80027490
              playSound(0, 1, (short)0, (short)0);
              textboxText.state_00 = TextboxTextState._19;
            }
          }
        }
      }

      //LAB_80026620
      case TRANSITION_AFTER_TIMEOUT_23 -> {  // Wait and then transition to another state
        //LAB_800274a4
        textboxText.ticksUntilStateTransition_64--;

        if(textboxText.ticksUntilStateTransition_64 == 0) {
          textboxText.ticksUntilStateTransition_64 = 4 * currentEngineState_8004dd04.tickMultiplier();
          textboxText.state_00 = textboxText.stateAfterTransition_78;
        }
      }
    }

    //LAB_800274c8
    updateTextboxArrowSprite(textboxIndex);
  }

  @Method(0x800274f0L)
  public static void processTextboxLine(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    // This code would be really tricky to make work at 60 FPS, but there isn't any harm in just slowing it down
    if(textboxText.waitTicks != 0) {
      textboxText.waitTicks--;
      return;
    }

    textboxText.waitTicks = currentEngineState_8004dd04.tickMultiplier() - 1;

    final LodString str = textboxText.str_24;

    if((textboxText.flags_08 & TextboxText84.SHOW_VAR) != 0) {
      final int digitIndex = textboxText.digitIndex_80;
      appendTextboxChar(textboxIndex, textboxText.charX_34, textboxText.charY_36, textboxText.textColour_28, textboxText.digits_46[digitIndex]);

      textboxText.charX_34++;
      textboxText.digitIndex_80++;

      if(textboxText.charX_34 < textboxText.chars_1c) {
        //LAB_80027768
        if(textboxText.digits_46[digitIndex + 1] == -1) {
          textboxText.flags_08 ^= TextboxText84.SHOW_VAR;
        }
      } else if(textboxText.charY_36 >= textboxText.lines_1e - 1) {
        if(textboxText.digits_46[digitIndex + 1] != -1) {
          textboxText.state_00 = TextboxTextState.SCROLL_TEXT_5;
          textboxText.charX_34 = 0;
          textboxText.charY_36++;
          setTextboxArrowPosition(textboxIndex, true);
          return;
        }

        //LAB_80027618
        final int control = str.charAt(textboxText.charIndex_30) >>> 8;

        if(control == TextboxText84.END) {
          //LAB_800276f4
          textboxText.state_00 = TextboxTextState._15;

          //LAB_80027704
          setTextboxArrowPosition(textboxIndex, true);

          //LAB_80027740
          textboxText.flags_08 ^= TextboxText84.SHOW_VAR;
          return;
        }

        if(control == TextboxText84.LINE) {
          textboxText.charIndex_30++;
        }

        //LAB_8002764c
        textboxText.state_00 = TextboxTextState.SCROLL_TEXT_5;
        textboxText.charX_34 = 0;
        textboxText.charY_36++;

        //LAB_80027704
        setTextboxArrowPosition(textboxIndex, true);
      } else {
        //LAB_80027688
        textboxText.charX_34 = 0;
        textboxText.charY_36++;

        if(textboxText.digits_46[digitIndex + 1] == -1) {
          final int control = str.charAt(textboxText.charIndex_30) >>> 8;

          if(control == TextboxText84.END) {
            //LAB_800276f4
            textboxText.state_00 = TextboxTextState._15;

            //LAB_80027704
            setTextboxArrowPosition(textboxIndex, true);
          } else {
            if(control == TextboxText84.LINE) {
              //LAB_80027714
              textboxText.charIndex_30++;
            }

            //LAB_80027724
            textboxText.state_00 = TextboxTextState._7;
          }

          //LAB_80027740
          textboxText.flags_08 ^= TextboxText84.SHOW_VAR;
          return;
        }
      }

      //LAB_8002779c
      textboxText.state_00 = TextboxTextState._7;
      return;
    }

    //LAB_800277bc
    boolean parseMore = true;

    //LAB_800277cc
    do {
      final int chr = str.charAt(textboxText.charIndex_30);

      switch(chr >>> 8) {
        case TextboxText84.END -> {
          textboxText.state_00 = TextboxTextState._15;
          setTextboxArrowPosition(textboxIndex, true);
          parseMore = false;
        }

        case TextboxText84.LINE -> {
          textboxText.charX_34 = 0;
          textboxText.charY_36++;
          textboxText.flags_08 |= TextboxText84.PROCESSED_NEW_LINE;

          if(textboxText.charY_36 >= textboxText.lines_1e || (textboxText.flags_08 & 0x80) != 0) {
            //LAB_80027880
            textboxText.state_00 = TextboxTextState.SCROLL_TEXT_5;

            if((textboxText.flags_08 & 0x1) == 0) {
              setTextboxArrowPosition(textboxIndex, true);
            }

            parseMore = false;
          }
        }

        case TextboxText84.BUTTON -> {
          //LAB_80027d28
          textboxText.state_00 = TextboxTextState._6;

          //LAB_80027d2c
          parseMore = false;
        }

        case TextboxText84.MUTLIBOX -> {
          setTextboxArrowPosition(textboxIndex, true);
          textboxText.state_00 = TextboxTextState._11;

          if(str.charAt(textboxText.charIndex_30 + 1) >>> 8 == TextboxText84.LINE) {
            textboxText.charIndex_30++;
          }

          parseMore = false;
        }

        case TextboxText84.SPEED -> {
          textboxText._3e = chr & 0xff;
          textboxText._40 = 0;
        }

        case TextboxText84.PAUSE -> {
          textboxText.state_00 = TextboxTextState.WAIT_FOR_PAUSE_8;
          textboxText.pauseTimer_44 = 60 / vsyncMode_8007a3b8 * (chr & 0xff);
          parseMore = false;
        }

        case TextboxText84.COLOUR ->
          //LAB_80027950
          textboxText.textColour_28 = TextColour.values()[chr & 0xf];

        case TextboxText84.VAR -> {
          textboxText.flags_08 |= TextboxText84.SHOW_VAR;

          //LAB_80027970
          Arrays.fill(textboxText.digits_46, -1);

          int variable = textboxVariables_800bdf10[chr & 0xff];
          int divisor = 1_000_000_000;
          final int[] varDigits = new int[10];
          //LAB_800279dc
          for(int i = 0; i < varDigits.length; i++) {
            varDigits[i] = digits_80052b40[variable / divisor].charAt(0);
            variable %= divisor;
            divisor /= 10;
          }

          //LAB_80027a34
          //LAB_80027a54
          int firstDigit;
          for(firstDigit = 0; firstDigit < 9; firstDigit++) {
            if(varDigits[firstDigit] != 0x15) { // 0
              break;
            }
          }

          //LAB_80027a84
          //LAB_80027a90
          for(int i = 0; i < textboxText.digits_46.length && firstDigit < varDigits.length; i++, firstDigit++) {
            textboxText.digits_46[i] = varDigits[firstDigit];
          }

          //LAB_80027ae4
          textboxText.digitIndex_80 = 0;

          //LAB_80027d2c
          parseMore = false;
        }

        case TextboxText84.X_OFFSET -> {
          final int v1_0 = chr & 0xff;

          if(v1_0 >= textboxText.chars_1c) {
            textboxText.charX_34 = textboxText.chars_1c - 1;
          } else {
            //LAB_80027b0c
            textboxText.charX_34 = v1_0;
          }
        }

        case TextboxText84.Y_OFFSET ->
          //LAB_80027b38
          textboxText.charY_36 = Math.min(chr & 0xff, textboxText.lines_1e - 1);

        case TextboxText84.SAUTO -> {
          textboxText.state_00 = TextboxTextState._13;

          final int v0 = 60 / vsyncMode_8007a3b8 * (chr & 0xff);
          textboxText._3e = v0;
          textboxText._40 = v0;

          if(str.charAt(textboxText.charIndex_30 + 1) >>> 8 == TextboxText84.LINE) {
            textboxText.charIndex_30++;
          }

          parseMore = false;
        }

        case TextboxText84.ELEMENT -> textboxText.element_7c = chr & 0xff;

        case TextboxText84.ARROW -> {
          if((chr & 0x1) != 0) {
            textboxText.flags_08 |= TextboxText84.SHOW_ARROW;
          } else {
            //LAB_80027bd0
            textboxText.flags_08 ^= TextboxText84.SHOW_ARROW;
          }
        }

        default -> {
          //LAB_80027be4
          appendTextboxChar(textboxIndex, textboxText.charX_34, textboxText.charY_36, textboxText.textColour_28, chr);

          textboxText.charX_34++;

          if(textboxText.charX_34 < textboxText.chars_1c) {
            //LAB_80027d28
            textboxText.state_00 = TextboxTextState._7;
          } else if(textboxText.charY_36 >= textboxText.lines_1e - 1) {
            final int control = str.charAt(textboxText.charIndex_30 + 1) >>> 8;

            if(control == TextboxText84.END) {
              //LAB_80027c7c
              textboxText.state_00 = TextboxTextState._15;
              setTextboxArrowPosition(textboxIndex, true);
            } else {
              if(control == TextboxText84.LINE) {
                //LAB_80027c98
                textboxText.charIndex_30++;
              }

              //LAB_80027c9c
              textboxText.state_00 = TextboxTextState.SCROLL_TEXT_5;
              textboxText.flags_08 |= TextboxText84.PROCESSED_NEW_LINE;
              textboxText.charX_34 = 0;
              textboxText.charY_36++;

              if((textboxText.flags_08 & 0x1) == 0) {
                setTextboxArrowPosition(textboxIndex, true);
              }
            }
          } else {
            //LAB_80027ce0
            textboxText.flags_08 |= TextboxText84.PROCESSED_NEW_LINE;
            textboxText.charX_34 = 0;
            textboxText.charY_36++;

            if(str.charAt(textboxText.charIndex_30 + 1) >>> 8 == TextboxText84.LINE) {
              textboxText.charIndex_30++;
            }

            //LAB_80027d28
            textboxText.state_00 = TextboxTextState._7;
          }

          //LAB_80027d2c
          parseMore = false;
        }
      }

      //LAB_80027d30
      textboxText.charIndex_30++;
    } while(parseMore);

    //LAB_80027d44
  }

  @Method(0x80027d74L)
  public static void calculateAppropriateTextboxBounds(final int textboxIndex, final float x, final float y) {
    final int maxX;
    if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05) {
      maxX = 350;
    } else {
      maxX = 310;
    }

    //LAB_80027d9c
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    final int width = textboxText.chars_1c * 9 / 2;
    final int height = textboxText.lines_1e * 6;

    float newX = x;
    if(x - width < 10) {
      newX = width + 10;
    }

    //LAB_80027dfc
    if(x + width > maxX) {
      newX = maxX - width;
    }

    //LAB_80027e14
    float newY = y;
    if(y - height < 18) {
      newY = height + 18;
    }

    //LAB_80027e28
    if(222 < y + height) {
      newY = 222 - height;
    }

    //LAB_80027e40
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    textbox.x_14 = newX;
    textbox.y_16 = newY;
    textboxText.x_14 = newX;
    textboxText.y_16 = newY;
    textboxText._18 = newX - width;
    textboxText._1a = newY - height;
  }

  @Method(0x80027eb4L)
  public static void advanceTextbox(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    //LAB_80027efc
    for(int lineIndex = (textboxText.flags_08 & TextboxText84.HAS_NAME) != 0 ? 1 : 0; lineIndex < textboxText.lines_1e; lineIndex++) {
      //LAB_80027f18
      for(int charIndex = 0; charIndex < textboxText.chars_1c; charIndex++) {
        final TextboxChar08 currentLine = textboxText.chars_58[lineIndex * textboxText.chars_1c + charIndex];
        final TextboxChar08 nextLine = textboxText.chars_58[(lineIndex + 1) * textboxText.chars_1c + charIndex];
        currentLine.set(nextLine);
        currentLine.y_02--;
      }
    }

    //LAB_8002804c
    //LAB_80028098
    for(int i = textboxText.chars_1c * textboxText.lines_1e; i < textboxText.chars_1c * (textboxText.lines_1e + 1); i++) {
      textboxText.chars_58[i].clear();
    }

    //LAB_800280cc
  }

  @Method(0x800280d4L)
  public static void rewindTextbox(final int textboxIndex) {
    assert false : "This won't delete Objs from the removed line";

    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    //LAB_8002810c
    for(int lineIndex = textboxText.lines_1e; lineIndex > 0; lineIndex--) {
      //LAB_80028128
      for(int charIndex = 0; charIndex < textboxText.chars_1c; charIndex++) {
        final TextboxChar08 previousLine = textboxText.chars_58[(lineIndex - 1) * textboxText.chars_1c + charIndex];
        final TextboxChar08 currentLine = textboxText.chars_58[lineIndex * textboxText.chars_1c + charIndex];
        currentLine.set(previousLine);
        currentLine.y_02++;
      }
    }

    //LAB_80028254
    //LAB_80028280
    for(int charIndex = 0; charIndex < textboxText.chars_1c; charIndex++) {
      textboxText.chars_58[charIndex].clear();
    }

    //LAB_800282a4
  }

  @Method(0x800282acL)
  public static void renderTextboxText(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    final int firstCharInLineIndex;
    final int lastCharInLineIndex;
    if((textboxText.flags_08 & TextboxText84.HAS_NAME) != 0) {
      firstCharInLineIndex = textboxText.chars_1c;
      lastCharInLineIndex = textboxText.chars_1c * 2;
    } else {
      firstCharInLineIndex = 0;
      lastCharInLineIndex = textboxText.chars_1c;
    }

    int nudgeX = 0;

    //LAB_80028328
    //LAB_80028348
    for(int i = 0; i < textboxText.chars_1c * (textboxText.lines_1e + 1); i++) {
      final TextboxChar08 chr = textboxText.chars_58[i];

      if(chr.x_00 == 0) {
        nudgeX = 0;
      }

      //LAB_8002835c
      if(chr.char_06 != 0) {
        nudgeX -= getCharShrink(chr.char_06);

        int scrollY = 0;
        int scrollH = 0;
        if((textboxText.flags_08 & 0x1) != 0) {
          if(i >= firstCharInLineIndex && i < lastCharInLineIndex) {
            final int scroll = Math.round(textboxText.scrollAmount_2c);
            scrollY = -scroll;
            scrollH = scroll;
          }

          //LAB_800283c4
          if(i >= textboxText.chars_1c * textboxText.lines_1e && i < textboxText.chars_1c * (textboxText.lines_1e + 1)) {
            scrollY = 0;
            scrollH = 12 - Math.round(textboxText.scrollAmount_2c);
          }
        }

        //LAB_8002840c
        if(scrollH < 13) {
          final int x = (int)(textboxText._18 + chr.x_00 * 9 - centreScreenX_1f8003dc - nudgeX);
          final int y;

          if((textboxText.flags_08 & TextboxText84.HAS_NAME) != 0 && i < textboxText.chars_1c) {
            y = (int)(textboxText._1a + chr.y_02 * 12 - centreScreenY_1f8003de - scrollY);
          } else {
            y = (int)(textboxText._1a + chr.y_02 * 12 - centreScreenY_1f8003de - scrollY - textboxText.scrollAmount_2c);
          }

          //LAB_80028544
          //LAB_80028564
          final int height = 12 - scrollH;

          textboxText.transforms.identity();
          textboxText.transforms.transfer.set(GPU.getOffsetX() + x + 1, GPU.getOffsetY() + y - scrollH + 1, (textboxText.z_0c + 1) * 4.0f);
          RENDERER.queueOrthoModel(RENDERER.chars, textboxText.transforms, QueuedModelStandard.class)
            .texture(RENDERER.textTexture)
            .vertices((LodString.fromLodChar(chr.char_06) - 33) * 4, 4)
            .monochrome(0.0f)
            .scissor(GPU.getOffsetX() + x, GPU.getOffsetY() + y, 8, height);

          textboxText.transforms.transfer.x--;
          textboxText.transforms.transfer.y--;
          textboxText.transforms.transfer.z -= 4.0f;
          RENDERER.queueOrthoModel(RENDERER.chars, textboxText.transforms, QueuedModelStandard.class)
            .texture(RENDERER.textTexture)
            .vertices((LodString.fromLodChar(chr.char_06) - 33) * 4, 4)
            .colour(chr.colour_04.r / 255.0f, chr.colour_04.g / 255.0f, chr.colour_04.b / 255.0f)
            .scissor(GPU.getOffsetX() + x, GPU.getOffsetY() + y, 8, height);
        }

        nudgeX += getCharWidth(chr.char_06);
      }
    }

    //LAB_800287f8
  }

  @Method(0x80028828L)
  public static void scrollTextboxDown(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    textboxText.scrollAmount_2c += textboxText.scrollSpeed_2a;

    if(textboxText.scrollAmount_2c >= 12.0f) {
      advanceTextbox(textboxIndex);
      textboxText.state_00 = TextboxTextState.PROCESS_TEXT_4;
      textboxText.scrollAmount_2c = 0.0f;
      textboxText.charY_36--;
    }

    //LAB_80028894
  }

  @Method(0x800288a4L)
  public static void scrollTextboxUp(final int textboxIndex) {
    if((tickCount_800bb0fc & 0x1) == 0) {
      final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

      textboxText.scrollAmount_2c += textboxText.scrollSpeed_2a;

      if(textboxText.scrollAmount_2c >= 12.0f) {
        advanceTextbox(textboxIndex);
        textboxText.flags_08 |= 0x4;
        textboxText.scrollAmount_2c -= 12.0f;
        textboxText.charY_36 = textboxText.lines_1e;
      }
    }

    //LAB_80028928
  }

  @Method(0x80028f20L)
  public static boolean textboxFits(final int textboxIndex, final float x, final float y) {
    final int maxX;
    if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05) {
      maxX = 350;
    } else {
      maxX = 310;
    }

    //LAB_80028f40
    //LAB_80028fa8
    //LAB_80028fc0
    //LAB_80028fd4
    return
      x - textboxes_800be358[textboxIndex].chars_18 * 4.5f >= 10 &&
      x + textboxes_800be358[textboxIndex].chars_18 * 4.5f <= maxX &&
      y - textboxes_800be358[textboxIndex].lines_1a * 6.0f >= 18 &&
      y + textboxes_800be358[textboxIndex].lines_1a * 6.0f <= 222;

    //LAB_80028ff0
  }

  @ScriptDescription("Unknown, sets textbox 0 to hardcoded values (garbage text? unused?")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "ret", description = "Always set to 0")
  @Method(0x80028ff8L)
  public static FlowControl FUN_80028ff8(final RunningScript<?> script) {
    clearTextbox(0);

    final Textbox4c struct4c = textboxes_800be358[0];
    struct4c.backgroundType_04 = BackgroundType.fromInt(textboxMode_80052b88[2]);
    struct4c.x_14 = 260;
    struct4c.y_16 = 120;
    struct4c.chars_18 = 6;
    struct4c.lines_1a = 8;
    clearTextboxText(0);

    final TextboxText84 textboxText = textboxText_800bdf38[0];
    textboxText.type_04 = textboxTextType_80052ba8[1];
    textboxText.str_24 = new LodString("Garbage text"); // _80052c20
    textboxText.flags_08 |= 0x40;

    textboxText.chars_58 = new TextboxChar08[textboxText.chars_1c * (textboxText.lines_1e + 1)];
    Arrays.setAll(textboxText.chars_58, i -> new TextboxChar08());

    //LAB_80029100
    calculateAppropriateTextboxBounds(0, textboxText.x_14, textboxText.y_16);
    script.params_20[0].set(0);
    return FlowControl.CONTINUE;
  }

  /** The purple bar used in inn dialogs, etc. */
  @Method(0x80029140L)
  public static void renderTextboxSelection(final int textboxIndex, final int selectionLine) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    final int width = (textbox.chars_18 - 1) * 9;
    final float x = textbox.x_14;
    final float y = textbox.y_16 + selectionLine * 12 - (textbox.lines_1a - 1) * 6;

    textboxSelectionTransforms.scaling(width, 1.0f, 1.0f);
    textboxSelectionTransforms.transfer.set(x - width / 2.0f, y, textbox.z_0c * 4.0f);
    RENDERER.queueOrthoModel(textboxSelectionObj, textboxSelectionTransforms, QueuedModelStandard.class);
  }

  private static final MV textTransforms = new MV();

  @Method(0x80029300L)
  public static void renderText(final String text, final float originX, final float originY, final FontOptions options) {
    final float height = 12.0f * options.getSize();
    final float trim = MathHelper.clamp(options.getTrim() * options.getSize(), -height, height);

    textTransforms.scaling(options.getSize());

    for(int i = 0; i < (options.hasShadow() ? 4 : 1); i++) {
      float x = switch(options.getHorizontalAlign()) {
        case LEFT -> originX;
        case CENTRE -> originX - lineWidth(text, 0) * options.getSize() / 2.0f;
        case RIGHT -> originX - lineWidth(text, 0) * options.getSize();
      };

      float y = originY;
      float glyphNudge = 0.0f;

      for(int charIndex = 0; charIndex < text.length(); charIndex++) {
        final char c = text.charAt(charIndex);

        if(c != ' ') {
          if(c == '\n') {
            x = switch(options.getHorizontalAlign()) {
              case LEFT -> originX;
              case CENTRE -> originX - lineWidth(text, charIndex + 1) * options.getSize() / 2.0f;
              case RIGHT -> originX - lineWidth(text, charIndex + 1) * options.getSize();
            };

            glyphNudge = 0.0f;
            y += height;
          } else {
            final float offsetX = (i & 1) * options.getSize();
            final float offsetY = (i >>> 1) * options.getSize();

            textTransforms.transfer.set(x + glyphNudge + offsetX, y + offsetY, textZ_800bdf00 * 4.0f);


            if(trim < 0) {
              textTransforms.transfer.y += trim;
            }

            final QueuedModelStandard model = RENDERER.queueOrthoModel(RENDERER.chars, textTransforms, QueuedModelStandard.class)
              .texture(RENDERER.textTexture)
              .vertices((c - 33) * 4, 4)
            ;

            if(i == 0) {
              model.colour(options.getRed(), options.getGreen(), options.getBlue());
            } else {
              model.colour(options.getShadowRed(), options.getShadowGreen(), options.getShadowBlue());
            }

            if(trim != 0) {
              if(trim < 0) {
                model.scissor(0, (int)y, displayWidth_1f8003e0, (int)(height + trim));
              } else {
                model.scissor(0, (int)(y - trim), displayWidth_1f8003e0, (int)height);
              }
            }
          }
        }

        glyphNudge += charWidth(c) * options.getSize();
      }
    }
  }

  @Method(0x80029920L)
  public static void setTextboxArrowPosition(final int textboxIndex, final boolean visible) {
    final TextboxArrow0c arrow = textboxArrows_800bdea0[textboxIndex];

    if(visible) {
      //LAB_80029948
      arrow.flags_00 |= TextboxArrow0c.ARROW_VISIBLE;
    } else {
      arrow.flags_00 = 0;
    }

    //LAB_80029970
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    arrow.x_04 = textbox.x_14;
    arrow.y_06 = textbox.y_16 + textbox.lines_1a * 6;
    arrow.spriteIndex_08 = 0;
  }

  @Method(0x800299d4L)
  public static void renderTextboxArrow(final int textboxIndex) {
    final TextboxArrow0c arrow = textboxArrows_800bdea0[textboxIndex];

    if((arrow.flags_00 & TextboxArrow0c.ARROW_VISIBLE) != 0) {
      final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
      if((textboxText.flags_08 & TextboxText84.SHOW_ARROW) != 0) {
        textboxArrowTransforms.scaling(1.0f, 0.875f, 1.0f);
        textboxArrowTransforms.transfer.set(arrow.x_04, arrow.y_06,  textboxText.z_0c * 4.0f);
        RENDERER.queueOrthoModel(textboxArrowObjs[arrow.spriteIndex_08], textboxArrowTransforms, QueuedModelStandard.class);
      }
    }

    //LAB_80029b50
  }

  @ScriptDescription("Gets the first free textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "textboxIndex", description = "Textbox index, or -1 if none are free")
  @Method(0x80029b68L)
  public static FlowControl scriptGetFreeTextboxIndex(final RunningScript<?> script) {
    //LAB_80029b7c
    for(int i = 0; i < 8; i++) {
      if(textboxes_800be358[i].state_00 == TextboxState.UNINITIALIZED_0 && textboxText_800bdf38[i].state_00 == TextboxTextState.UNINITIALIZED_0) {
        script.params_20[0].set(i);
        return FlowControl.CONTINUE;
      }

      //LAB_80029bac
      //LAB_80029bb0
    }

    script.params_20[0].set(-1);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Initialize a textbox")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textboxIndex", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The textbox x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The textbox y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The textbox width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The textbox height")
  @Method(0x80029bd4L)
  public static FlowControl scriptInitTextbox(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    clearTextbox(textboxIndex);

    final Textbox4c struct4c = textboxes_800be358[textboxIndex];
    struct4c.backgroundType_04 = BackgroundType.fromInt(script.params_20[1].get());
    struct4c.x_14 = script.params_20[2].get();
    struct4c.y_16 = script.params_20[3].get();
    struct4c.chars_18 = script.params_20[4].get() + 1;
    struct4c.lines_1a = script.params_20[5].get() + 1;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Returns whether or not a textbox is initialized")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "initialized", description = "0 for false, non-zero for true")
  @Method(0x80029c98L)
  public static FlowControl scriptIsTextboxInitialized(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    script.params_20[1].set(textboxes_800be358[textboxIndex].state_00.ordinal() | textboxText_800bdf38[textboxIndex].state_00.ordinal());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the textbox state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "state", description = "The textbox state")
  @Method(0x80029cf4L)
  public static FlowControl scriptGetTextboxState(final RunningScript<?> script) {
    script.params_20[1].set(textboxes_800be358[script.params_20[0].get()].state_00.ordinal());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the textbox text state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "state", description = "The textbox text state")
  @Method(0x80029d34L)
  public static FlowControl scriptGetTextboxTextState(final RunningScript<?> script) {
    script.params_20[1].set(textboxText_800bdf38[script.params_20[0].get()].state_00.ordinal());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates a textbox")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @Method(0x80029d6cL)
  public static FlowControl scriptDeallocateTextbox(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    if(textboxText.state_00 != TextboxTextState.UNINITIALIZED_0) {
      textboxText.delete();
    }

    //LAB_80029db8
    textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
    textbox.state_00 = TextboxState.UNINITIALIZED_0;

    setTextboxArrowPosition(textboxIndex, false);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates all textboxes")
  @Method(0x80029e04L)
  public static FlowControl scriptDeallocateAllTextboxes(final RunningScript<?> script) {
    //LAB_80029e2c
    for(int i = 0; i < 8; i++) {
      final Textbox4c textbox = textboxes_800be358[i];
      final TextboxText84 textboxText = textboxText_800bdf38[i];

      if(textboxText.state_00 != TextboxTextState.UNINITIALIZED_0) {
        textboxText.delete();
      }

      //LAB_80029e48
      textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
      textbox.state_00 = TextboxState.UNINITIALIZED_0;

      setTextboxArrowPosition(i, false);
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a variable used in a textbox's text (<var:n>)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The variable index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value")
  @Method(0x80029e8cL)
  public static FlowControl scriptSetTextboxVariable(final RunningScript<?> script) {
    textboxVariables_800bdf10[Math.min(9, script.params_20[0].get())] = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to textboxes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @Method(0x80029eccL)
  public static FlowControl FUN_80029ecc(final RunningScript<?> script) {
    final TextboxText84 textboxText = textboxText_800bdf38[script.params_20[0].get()];
    if(textboxText.state_00 == TextboxTextState._16 && (textboxText.flags_08 & TextboxText84.NO_INPUT) != 0) {
      textboxText.flags_08 ^= TextboxText84.NO_INPUT;
    }

    //LAB_80029f18
    //LAB_80029f1c
    textboxText.flags_08 |= 0x40;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the index of the currently-selected textbox line (i.e. do you want to stay at this inn? yes/no)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "selectionIndex", description = "The selected line index")
  @Method(0x80029f48L)
  public static FlowControl scriptGetTextboxSelectionIndex(final RunningScript<?> script) {
    script.params_20[1].set(textboxText_800bdf38[script.params_20[0].get()].selectionIndex_6c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Return's a textbox's element")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value")
  @Method(0x80029f80L)
  public static FlowControl scriptGetTextboxElement(final RunningScript<?> script) {
    script.params_20[1].set(textboxText_800bdf38[script.params_20[0].get()].element_7c);
    return FlowControl.CONTINUE;
  }

  @Method(0x8002a058L)
  public static void handleTextboxAndText() {
    //LAB_8002a080
    for(int i = 0; i < 8; i++) {
      if(textboxes_800be358[i].state_00 != TextboxState.UNINITIALIZED_0) {
        handleTextbox(i);
      }

      //LAB_8002a098
      if(textboxText_800bdf38[i].state_00 != TextboxTextState.UNINITIALIZED_0) {
        handleTextboxText(i); // Animates the textbox arrow
      }
    }
  }

  @Method(0x8002a0e4L)
  public static void renderTextboxes() {
    for(int i = 0; i < 8; i++) {
      //LAB_8002a10c
      final Textbox4c textbox4c = textboxes_800be358[i];
      if(textbox4c.state_00 != TextboxState.UNINITIALIZED_0 && (textbox4c.flags_08 & Textbox4c.RENDER_BACKGROUND) != 0) {
        renderTextboxBackground(i);
      }

      if(!currentEngineState_8004dd04.renderTextOnTopOfAllBoxes()) {
        renderTextboxOverlays(i);
      }
    }

    if(currentEngineState_8004dd04.renderTextOnTopOfAllBoxes()) {
      for(int i = 0; i < 8; i++) {
        renderTextboxOverlays(i);
      }
    }
  }

  private static void renderTextboxOverlays(final int textboxIndex) {
    final TextboxText84 text = textboxText_800bdf38[textboxIndex];
    if(text.state_00 != TextboxTextState.UNINITIALIZED_0) {
      switch(text.state_00) {
        case _18 -> renderTextboxSelection(textboxIndex, text.selectionLine_60);
        case _19, SELECTION_22 -> renderTextboxSelection(textboxIndex, text.selectionLine_68);
      }

      renderTextboxText(textboxIndex);
      renderTextboxArrow(textboxIndex);
    }
  }

  @Method(0x8002a180L)
  public static void appendTextboxChar(final int textboxIndex, final int charX, final int charY, TextColour colour, final int lodChar) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    final int charIndex = textboxText.charY_36 * textboxText.chars_1c + textboxText.charX_34;
    final TextboxChar08 chr = textboxText.chars_58[charIndex];
    chr.x_00 = charX;
    chr.y_02 = charY;

    if((textboxText.flags_08 & TextboxText84.HAS_NAME) != 0 && charY == 0) {
      colour = TextColour.YELLOW;
    }

    //LAB_8002a1e8
    //LAB_8002a1ec
    chr.colour_04 = colour;

    // Hellena Prison has a retail bug (textbox name says Warden?iate)
    if(lodChar == 0x900 || lodChar == -1) {
      chr.char_06 = 0;
    } else {
      chr.char_06 = lodChar;
    }
  }

  @Method(0x8002a1fcL)
  public static int getCharWidth(final int chr) {
    //LAB_8002a254
    return switch(chr) {
      case 0x5, 0x23, 0x24, 0x2a, 0x37, 0x38, 0x3a, 0x3b, 0x3c, 0x3d, 0x3f, 0x40, 0x43, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4d, 0x4e, 0x51, 0x52 -> 1;
      case 0x2, 0x8, 0x3e, 0x4c -> 2;
      case 0xb, 0xc, 0x42 -> 3;
      case 0x1, 0x3, 0x4, 0x9, 0x16, 0x41, 0x44 -> 4;
      case 0x6, 0x27 -> 5;
      default -> 0;
    };
  }

  /** This value is subtracted from the X position */
  @Method(0x8002a25cL)
  public static int getCharShrink(final int chr) {
    if(chr == 0x45) { // m
      return 1;
    }

    if(chr == 0x2) { // .
      return 2;
    }

    if(chr >= 0x5 && chr < 0x7) { // ?!
      return 3;
    }

    return 0;
  }

  @Method(0x8002a2b4L)
  public static void clearTextboxChars(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    //LAB_8002a2f0
    for(int charIndex = 0; charIndex < textboxText.chars_1c * (textboxText.lines_1e + 1); charIndex++) {
      textboxText.chars_58[charIndex].clear();
    }

    //LAB_8002a324
  }

  @Method(0x8002a32cL)
  public static void initTextbox(final int textboxIndex, final boolean animateInOut, final float x, final float y, final int chars, final int lines) {
    clearTextbox(textboxIndex);

    final Textbox4c struct = textboxes_800be358[textboxIndex];
    struct.backgroundType_04 = animateInOut ? BackgroundType.ANIMATE_IN_OUT : BackgroundType.NORMAL;
    struct.renderBorder_06 = true;
    struct.flags_08 |= Textbox4c.NO_ANIMATE_OUT;

    struct.x_14 = x;
    struct.y_16 = y;
    struct.chars_18 = chars + 1;
    struct.lines_1a = lines + 1;
  }

  @Method(0x8002a3ecL)
  public static void setTextAndTextboxesToUninitialized(final int textboxIndex, final int mode) {
    if(mode == 0) {
      //LAB_8002a40c
      textboxText_800bdf38[textboxIndex].state_00 = TextboxTextState.UNINITIALIZED_0;
      textboxes_800be358[textboxIndex].state_00 = TextboxState.UNINITIALIZED_0;
    } else {
      //LAB_8002a458
      textboxes_800be358[textboxIndex].state_00 = TextboxState.ANIMATE_OUT_3;
    }
  }

  /** Too bad I don't know what state 6 is */
  @Method(0x8002a488L)
  public static boolean isTextboxInState6(final int textboxIndex) {
    return textboxes_800be358[textboxIndex].state_00 == TextboxState._6;
  }

  @Method(0x8002a4c4L)
  public static void updateTextboxArrowSprite(final int textboxIndex) {
    final TextboxArrow0c arrow = textboxArrows_800bdea0[textboxIndex];

    if((arrow.flags_00 & TextboxArrow0c.ARROW_VISIBLE) != 0) {
      if((textboxText_800bdf38[textboxIndex].flags_08 & TextboxText84.SHOW_ARROW) != 0) {
        if(tickCount_800bb0fc % (2 * currentEngineState_8004dd04.tickMultiplier()) == 0) {
          arrow.spriteIndex_08++;
        }

        //LAB_8002a53c
        if(arrow.spriteIndex_08 > 6) {
          arrow.spriteIndex_08 = 0;
        }
      }
    }

    //LAB_8002a554
  }

  @Method(0x8002a59cL)
  public static int textWidth(final String text) {
    int width = 0;
    int currentWidth = 0;
    for(int index = 0; index < text.length(); index++) {
      if(text.charAt(index) == '\n') {
        currentWidth = 0;
      }

      currentWidth += charWidth(text.charAt(index));

      if(currentWidth > width) {
        width = currentWidth;
      }
    }

    return width;
  }

  public static int lineWidth(final String text, final int start) {
    int width = 0;
    for(int index = start; index < text.length(); index++) {
      if(text.charAt(index) == '\n') {
        break;
      }

      width += charWidth(text.charAt(index));
    }

    return width;
  }

  public static int charWidth(final char chr) {
    final int nudge = switch(chr) {
      case '?', 'E', 'F', 'L', 'Y', 'Z', 'b', 'c', 'd', 'e', 'g', 'h', 'k', 'n', 'o', 'p', 'q', 'r', 's', 'u', 'v', 'y', 'z' -> 1;
      case '.', '/', 'f', 't' -> 2;
      case '!', '(', ')', 'j' -> 3;
      case ',', '·', ':', '\'', '1', 'i', 'l' -> 4;
      case 'I' -> 5;
      case '\n' -> 8;
      default -> 0;
    };

    return 8 - nudge;
  }

  public static int textHeight(final String text) {
    int lines = 1;
    int newlinePos = -1;
    while((newlinePos = text.indexOf('\n', newlinePos + 1)) != -1) {
      lines++;
    }

    return lines * 12;
  }

  @Method(0x8002a6fcL)
  public static void clearCharacterStats() {
    //LAB_8002a730
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final ActiveStatsa0 stats = stats_800be5f8[charIndex];

      stats.xp_00 = 0;
      stats.hp_04 = 0;
      stats.mp_06 = 0;
      stats.sp_08 = 0;
      stats.dxp_0a = 0;
      stats.flags_0c = 0;
      stats.level_0e = 0;
      stats.dlevel_0f = 0;
      stats.equipment_30.clear();
      stats.selectedAddition_35 = 0;

      //LAB_8002a780;
      for(int i = 0; i < 8; i++) {
        stats.additionLevels_36[i] = 0;
        stats.additionXp_3e[i] = 0;
      }

      stats.equipmentPhysicalImmunity_46 = false;
      stats.equipmentMagicalImmunity_48 = false;
      stats.equipmentPhysicalResistance_4a = false;
      stats.equipmentSpMultiplier_4c = 0;
      stats.equipmentSpPerPhysicalHit_4e = 0;
      stats.equipmentMpPerPhysicalHit_50 = 0;
      stats.equipmentSpPerMagicalHit_52 = 0;
      stats.equipmentMpPerMagicalHit_54 = 0;
      stats.equipmentEscapeBonus_56 = 0;
      stats.equipmentHpRegen_58 = 0;
      stats.equipmentMpRegen_5a = 0;
      stats.equipmentSpRegen_5c = 0;
      stats.equipmentRevive_5e = 0;
      stats.equipmentMagicalResistance_60 = false;
      stats.equipmentHpMulti_62 = 0;
      stats.equipmentMpMulti_64 = 0;
      stats.maxHp_66 = 0;
      stats.addition_68 = 0;
      stats.bodySpeed_69 = 0;
      stats.bodyAttack_6a = 0;
      stats.bodyMagicAttack_6b = 0;
      stats.bodyDefence_6c = 0;
      stats.bodyMagicDefence_6d = 0;
      stats.maxMp_6e = 0;
      stats.spellId_70 = 0;
      stats._71 = 0;
      stats.dragoonAttack_72 = 0;
      stats.dragoonMagicAttack_73 = 0;
      stats.dragoonDefence_74 = 0;
      stats.dragoonMagicDefence_75 = 0;

      clearEquipmentStats(charIndex);

      stats.addition_00_9c = 0;
      stats.additionSpMultiplier_9e = 0;
      stats.additionDamageMultiplier_9f = 0;
    }

    characterStatsLoaded_800be5d0 = false;
  }

  @Method(0x8002a86cL)
  public static void clearEquipmentStats(final int charIndex) {
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    stats.specialEffectFlag_76 = 0;
//    stats.equipmentType_77 = 0;
    stats.equipment_02_78 = 0;
    stats.equipmentEquipableFlags_79 = 0;
    stats.equipmentAttackElements_7a.clear();
    stats.equipment_05_7b = 0;
    stats.equipmentElementalResistance_7c.clear();
    stats.equipmentElementalImmunity_7d.clear();
    stats.equipmentStatusResist_7e = 0;
    stats.equipment_09_7f = 0;
    stats.equipmentAttack1_80 = 0;
    stats._83 = 0;
    stats.equipmentIcon_84 = 0;

    stats.equipmentSpeed_86 = 0;
    stats.equipmentAttack_88 = 0;
    stats.equipmentMagicAttack_8a = 0;
    stats.equipmentDefence_8c = 0;
    stats.equipmentMagicDefence_8e = 0;
    stats.equipmentAttackHit_90 = 0;
    stats.equipmentMagicHit_92 = 0;
    stats.equipmentAttackAvoid_94 = 0;
    stats.equipmentMagicAvoid_96 = 0;
    stats.equipmentOnHitStatusChance_98 = 0;
    stats.equipment_19_99 = 0;
    stats.equipment_1a_9a = 0;
    stats.equipmentOnHitStatus_9b = 0;
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

  @Method(0x8002bb38L)
  public static void startRumbleMode(final int pad, final int mode) {
    LOGGER.debug("startRumbleMode %x %x", pad, mode);

    if(!CONFIG.getConfig(CoreMod.RUMBLE_CONFIG.get())) {
      return;
    }

    switch(mode) {
      case 0 -> stopRumble(pad);
      case 1 -> Input.rumble(0.25f, 0);
      case 2 -> {
        if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05) {
          Input.rumble(0.3f, 0);
        } else if(engineState_8004dd20 == EngineStateEnum.COMBAT_06) {
          Input.rumble(0.75f - rumbleDampener_800bee80, 0);
        } else {
          Input.rumble(0.75f, 0);
        }
      }
      case 3 -> {
        if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05) {
          Input.rumble(0.4f, 0);
        } else if(engineState_8004dd20 == EngineStateEnum.COMBAT_06) {
          Input.rumble(1.0f - rumbleDampener_800bee80, 0);
        } else {
          Input.rumble(1.0f, 0);
        }
      }
    }
  }

  @Method(0x8002bcc8L)
  public static void startRumbleIntensity(final int pad, int intensityIn) {
    LOGGER.debug("startRumbleIntensity %x %x", pad, intensityIn);

    if(!CONFIG.getConfig(CoreMod.RUMBLE_CONFIG.get())) {
      return;
    }

    if(intensityIn > 0x1ff) {
      intensityIn = 0x1ff;
    }

    float intensity = intensityIn / (float)0x1ff;

    if(intensity > 0.25f) {
      intensity -= rumbleDampener_800bee80;
    }

    Input.rumble(intensity, 0);
  }

  @Method(0x8002bda4L)
  public static void adjustRumbleOverTime(final int pad, int intensity, final int frames) {
    LOGGER.debug("adjustRumbleOverTime %x %x %x", pad, intensity, frames);

    if(!CONFIG.getConfig(CoreMod.RUMBLE_CONFIG.get())) {
      return;
    }

    intensity = MathHelper.clamp(intensity, 0, 0x1ff);

    if(frames == 0) {
      startRumbleIntensity(pad, intensity);
      return;
    }

    final int divisor;
    if(engineState_8004dd20 == EngineStateEnum.FMV_09) {
      divisor = 1;
    } else {
      divisor = vsyncMode_8007a3b8 * currentEngineState_8004dd04.tickMultiplier();
    }

    Input.adjustRumble(intensity / (float)0x1ff, frames / divisor * 50);
  }

  @Method(0x8002c150L)
  public static void stopRumble(final int pad) {
    LOGGER.debug("stopRumble");
    Input.stopRumble();
  }

  @Method(0x8002c178L)
  public static void setRumbleDampener(final int intensity) {
    LOGGER.debug("setRumbleDampener %x", intensity);
    rumbleDampener_800bee80 = intensity / (float)0x1ff;
  }

  @Method(0x8002c184L)
  public static void resetRumbleDampener() {
    LOGGER.debug("resetRumbleDampener");
    rumbleDampener_800bee80 = 0.0f;
  }

  @Method(0x8002c984L)
  public static long playXaAudio(final int xaLoadingStage, final int xaArchiveIndex, final int xaFileIndex) {
    //LAB_8002c9f0
    if(xaFileIndex == 0 || xaFileIndex >= 32 || xaLoadingStage >= 5) {
      return 0;
    }

    if(xaLoadingStage == 3) {
      LOGGER.info("Playing XA archive %d file %d", xaArchiveIndex, xaFileIndex);

      //LAB_8002c448
      AUDIO_THREAD.loadXa(Loader.loadFile("XA/LODXA0%d.XA/%d.opus".formatted(xaArchiveIndex, xaFileIndex)));
      _800bf0cf = 4;
    }

    if(xaLoadingStage == 2) {
      _800bf0cf = 4;
    }

    return 0;
  }

  @Method(0x8002ced8L)
  public static void start() {
    main();
  }

  @Method(0x8002d220L)
  public static int strcmp(final String s1, final String s2) {
    return s1.compareToIgnoreCase(s2);
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
}
