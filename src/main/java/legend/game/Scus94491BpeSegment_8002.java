package legend.game;

import legend.core.GameEngine;
import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.font.Font;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.Rect4i;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.Transforms;
import legend.core.memory.Method;
import legend.core.platform.input.InputCodepoints;
import legend.game.combat.types.EnemyDrop;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.Item;
import legend.game.inventory.ItemGroupSortMode;
import legend.game.inventory.ItemStack;
import legend.game.inventory.OverflowMode;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.MainMenuScreen;
import legend.game.inventory.screens.MenuScreen;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.inventory.GiveEquipmentEvent;
import legend.game.modding.events.inventory.Inventory;
import legend.game.modding.events.inventory.TakeEquipmentEvent;
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
import legend.game.tmd.Tmd;
import legend.game.tmd.TmdObjTable1c;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CContainer;
import legend.game.types.CharacterData2c;
import legend.game.types.ClutAnimation;
import legend.game.types.ClutAnimations;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.Model124;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.TmdAnimationFile;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.core.GameEngine.bootMods;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.startMenuMusic;
import static legend.game.SItem.stopMenuMusic;
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
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.renderMode;
import static legend.game.Scus94491BpeSegment_8004.stopMusicSequence;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.monsterSoundFileIndices_800500e8;
import static legend.game.Scus94491BpeSegment_8005.shadowScale_8005039c;
import static legend.game.Scus94491BpeSegment_8005.shouldRestoreCameraPosition_80052c40;
import static legend.game.Scus94491BpeSegment_8005.submapCutBeforeBattle_80052c3c;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapEnvState_80052c44;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
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
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.transitioningFromCombatToSubmap_800bd7b8;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800e.main;
import static legend.game.Textboxes.initTextboxes;
import static legend.game.modding.coremod.CoreMod.ITEM_GROUP_SORT_MODE;
import static org.lwjgl.opengl.GL11C.GL_LEQUAL;

public final class Scus94491BpeSegment_8002 {
  private Scus94491BpeSegment_8002() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8002.class);

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
    final boolean tickAnimations = model.subFrameIndex == 0 || model.subFrameIndex == framesPerKeyframe / 2;

    if(model.clutAnimations_a4 != null) {
      //LAB_800da138
      for(int animIndex = 0; animIndex < 4; animIndex++) {
        if(model.clutAnimations_a4.used_04[animIndex]) {
          animateModelClut(model, animIndex, tickAnimations);
        }
      }
    }

    if(tickAnimations) {
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
  private static void animateModelClut(final Model124 model, final int animIndex, final boolean tickAnimations) {
    final ClutAnimations anims = model.clutAnimations_a4;

    if(anims.clutAnimation_20[animIndex] == null) {
      anims.used_04[animIndex] = false;
    } else {
      //LAB_800ddeac
      final int x = model.uvAdjustments_9d.clutX;
      final int y = model.uvAdjustments_9d.clutY;

      final ClutAnimation anim = anims.clutAnimation_20[animIndex];
      int dataOffset = anims.dataIndex_08[animIndex] * 2;

      //LAB_800ddf08
      final int sourceYOffset = anim.dataStream_04[dataOffset];
      dataOffset++;

      if(tickAnimations) {
        anims.frameIndex_10[animIndex]++;

        if(anims.frameIndex_10[animIndex] == anim.dataStream_04[dataOffset]) {
          anims.frameIndex_10[animIndex] = 0;

          if(anim.dataStream_04[dataOffset + 1] == -1) {
            anims.dataIndex_08[animIndex] = 0;
          } else {
            //LAB_800ddf70
            anims.dataIndex_08[animIndex]++;
          }
        }
      }

      //LAB_800ddf8c
      RENDERER.addClutAnimation(x, y + anims.clutIndex_18[animIndex], x, y + sourceYOffset);
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
    renderMode = EngineState.RenderMode.LEGACY;
    textZ_800bdf00 = 33;

    whichMenu_800bdc38 = destMenu;

    if(destScreen != null) {
      menuStack.pushScreen(destScreen.get());
    }
  }

  public static void initInventoryMenu() {
    initMenu(WhichMenu.RENDER_NEW_MENU, () -> new MainMenuScreen(() -> {
      menuStack.popScreen();
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

      case UNLOAD_QUIETLY -> {
        menuStack.reset();
        whichMenu_800bdc38 = WhichMenu.NONE_0;
        deallocateRenderables(0xff);
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

  public static boolean takeItem(final Item item) {
    return gameState_800babc8.items_2e9.take(item).isEmpty();
  }

  public static boolean takeItem(final ItemStack stack) {
    return gameState_800babc8.items_2e9.take(stack).isEmpty();
  }

  @Method(0x800232dcL)
  public static boolean takeItemFromSlot(final int itemSlot) {
    return takeItemFromSlot(itemSlot, gameState_800babc8.items_2e9.get(itemSlot).getSize());
  }

  @Method(0x800232dcL)
  public static boolean takeItemFromSlot(final int itemSlot, final int amount) {
    if(itemSlot >= gameState_800babc8.items_2e9.getSize()) {
      LOGGER.warn("Tried to take item index %d (out of bounds)", itemSlot);
      return false;
    }

    gameState_800babc8.items_2e9.takeFromSlot(itemSlot, amount);
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
    return gameState_800babc8.items_2e9.give(item).isEmpty();
  }

  /**
   * Note: does NOT consume the passed in item stack
   */
  @Method(0x80023484L)
  public static boolean giveItem(final ItemStack item) {
    return gameState_800babc8.items_2e9.give(new ItemStack(item)).isEmpty();
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
  public static <T extends InventoryEntry> void setInventoryFromDisplay(final List<MenuEntryStruct04<T>> display, final List<T> out, final int count) {
    out.clear();

    //LAB_800239ec
    for(int i = 0; i < count; i++) {
      if((display.get(i).flags_02 & 0x1000) == 0) {
        out.add(display.get(i).item_00);
      }
    }
  }

  @Method(0x800239e0L)
  public static void setInventoryFromDisplay(final List<MenuEntryStruct04<ItemStack>> display, final Inventory out, final int count) {
    out.clear();

    //LAB_800239ec
    for(int i = 0; i < count; i++) {
      if((display.get(i).flags_02 & 0x1000) == 0) {
        out.give(display.get(i).item_00, true);
      }
    }
  }

  @Method(0x80023a2cL)
  public static <T extends InventoryEntry> void sortItems(final List<MenuEntryStruct04<T>> display, final List<T> items, final int count, final List<String> retailSorting) {
    display.sort(menuItemIconComparator(retailSorting, InventoryEntry::getRegistryId));
    setInventoryFromDisplay(display, items, count);
  }

  @Method(0x80023a2cL)
  public static void sortItems(final List<MenuEntryStruct04<ItemStack>> display, final Inventory items, final int count, final List<String> retailSorting) {
    display.sort(menuItemIconComparator(retailSorting, stack -> stack.getItem().getRegistryId()));
    setInventoryFromDisplay(display, items, count);
  }

  public static <T extends InventoryEntry> Comparator<MenuEntryStruct04<T>> menuItemIconComparator(final List<String> retailSorting, final Function<T, RegistryId> idExtractor) {
    final boolean retail = CONFIG.getConfig(ITEM_GROUP_SORT_MODE.get()) == ItemGroupSortMode.RETAIL;

    Comparator<MenuEntryStruct04<T>> comparator = Comparator.comparingInt(item -> item.item_00.getIcon().resolve().icon);

    if(retail) {
      comparator = comparator.thenComparingInt(item -> {
        final RegistryId id = idExtractor.apply(item.item_00);

        if(!LodMod.MOD_ID.equals(id.modId()) || !retailSorting.contains(id.entryId())) {
          return Integer.MAX_VALUE;
        }

        return retailSorting.indexOf(id.entryId());
      });

      comparator = comparator.thenComparing(item -> {
        final RegistryId id = idExtractor.apply(item.item_00);

        if(LodMod.MOD_ID.equals(id.modId()) && retailSorting.contains(id.entryId())) {
          return "";
        }

        return I18n.translate(item.getNameTranslationKey());
      });
    } else {
      comparator = comparator.thenComparing(item -> I18n.translate(item.getNameTranslationKey()));
    }

    return comparator;
  }

  public static Comparator<MenuEntryStruct04<Equipment>> menuEquipmentSlotComparator() {
    return Comparator
      .comparingInt((MenuEntryStruct04<Equipment> equipment) -> equipment.item_00.slot.ordinal())
      .thenComparing(equipment -> I18n.translate(equipment.getNameTranslationKey()));
  }

  @Method(0x80023a88L)
  public static void sortItems() {
    final List<MenuEntryStruct04<ItemStack>> items = new ArrayList<>();

    for(final ItemStack stack : gameState_800babc8.items_2e9) {
      items.add(new MenuEntryStruct04<>(stack));
    }

    sortItems(items, gameState_800babc8.items_2e9, gameState_800babc8.items_2e9.getSize(), List.of(LodMod.ITEM_IDS));
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
        for(int metricsIndex = Math.min(metricses.length, renderable.metricsCount) - 1; metricsIndex >= 0; metricsIndex--) {
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
              .colour(renderable.colour)
            ;

            if((metrics.clut_04 & 0x8000) != 0) {
              model
                .translucency(Translucency.of(tpage >>> 5 & 0b11))
                .translucentDepthComparator(GL_LEQUAL)
              ;
            }

            metrics.useTexture(model);

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
    loadDrgnDir(0, 6669, Scus94491BpeSegment_8002::basicUiTexturesLoaded);

    textZ_800bdf00 = 13;
    clearCharacterStats();
    initTextboxes();
  }

  private static final MV textTransforms = new MV();

  @Method(0x80029300L)
  public static void renderText(final String text, final float originX, final float originY, final FontOptions options) {
    renderText(text, originX, originY, options, null);
  }

  @Method(0x80029300L)
  public static void renderText(final String text, final float originX, final float originY, final FontOptions options, @Nullable final Consumer<QueuedModelStandard> queueCallback) {
    renderText(GameEngine.DEFAULT_FONT, text, originX, originY, options, queueCallback);
  }

  @Method(0x80029300L)
  public static void renderText(final Font font, final String text, final float originX, final float originY, final FontOptions options) {
    renderText(font, text, originX, originY, options, null);
  }

  @Method(0x80029300L)
  public static void renderText(final Font font, final String text, final float originX, final float originY, final FontOptions options, @Nullable final Consumer<QueuedModelStandard> queueCallback) {
    font.init();

    final float height = 12.0f * options.getSize();
    final float trim = java.lang.Math.clamp(options.getTrim() * options.getSize(), -height, height);

    textTransforms.scaling(options.getSize());

    for(int i = 0; i < (options.hasShadow() ? 4 : 1); i++) {
      float x = switch(options.getHorizontalAlign()) {
        case LEFT -> originX;
        case CENTRE -> originX - font.lineWidth(text) * options.getSize() / 2.0f;
        case RIGHT -> originX - font.lineWidth(text) * options.getSize();
      };

      // I adjusted the texture so that glyphs start 1 pixel lower to fix bleeding - subtract 1 here to compensate
      float y = originY - 1;
      float glyphNudge = 0.0f;

      for(int charIndex = 0; charIndex < text.length(); charIndex++) {
        final char c = text.charAt(charIndex);

        if(c != ' ') {
          if(c == '\n') {
            x = switch(options.getHorizontalAlign()) {
              case LEFT -> originX;
              case CENTRE -> originX - font.lineWidth(text, charIndex + 1) * options.getSize() / 2.0f;
              case RIGHT -> originX - font.lineWidth(text, charIndex + 1) * options.getSize();
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

            if(i == 0 || font.usesColour(c)) {
              final QueuedModelStandard model = font.queueChar(InputCodepoints.getCodepoint(PLATFORM.getGamepadType(), c), textTransforms);

              if(font.usesColour(c)) {
                if(i == 0) {
                  model.colour(options.getRed(), options.getGreen(), options.getBlue());
                } else if(font.usesColour(c)) {
                  model.colour(options.getShadowRed(), options.getShadowGreen(), options.getShadowBlue());
                }
              }

              if(trim != 0) {
                if(trim < 0) {
                  model.scissor(0, (int)y + 1, displayWidth_1f8003e0, (int)(height + trim));
                } else {
                  model.scissor(0, (int)(y + 1 - trim), displayWidth_1f8003e0, (int)height);
                }
              }

              if(queueCallback != null) {
                queueCallback.accept(model);
              }
            }
          }
        }

        if(c != '\n') {
          glyphNudge += font.charWidth(c) * options.getSize();
        }
      }
    }
  }

  public static int charWidth(final char chr) {
    switch(chr) {
      case InputCodepoints.DPAD_LEFT, InputCodepoints.DPAD_RIGHT -> {
        return 5;
      }
      case InputCodepoints.DPAD_UP, InputCodepoints.DPAD_DOWN, InputCodepoints.B -> {
        return 7;
      }
      case InputCodepoints.XBOX_BUTTON_MENU, InputCodepoints.XBOX_BUTTON_VIEW, InputCodepoints.Y, InputCodepoints.PS_BUTTON_CROSS, InputCodepoints.PS_BUTTON_CIRCLE, InputCodepoints.PS_BUTTON_SQUARE, InputCodepoints.PS_BUTTON_TRIANGLE -> {
        return 8;
      }
      case InputCodepoints.LEFT_BUMPER, InputCodepoints.LEFT_STICK, InputCodepoints.RIGHT_STICK, InputCodepoints.A, InputCodepoints.X, InputCodepoints.START -> {
        return 9;
      }
      case InputCodepoints.LEFT_TRIGGER, InputCodepoints.RIGHT_BUMPER -> {
        return 10;
      }
      case InputCodepoints.RIGHT_TRIGGER -> {
        return 11;
      }
      case InputCodepoints.LEFT_AXIS_X, InputCodepoints.LEFT_AXIS_Y -> {
        return 12;
      }
      case InputCodepoints.RIGHT_AXIS_X, InputCodepoints.RIGHT_AXIS_Y -> {
        return 13;
      }
      case InputCodepoints.SELECT -> {
        return 15;
      }
    }

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
      case 1 -> PLATFORM.rumble(0.25f, 0);
      case 2 -> {
        if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05) {
          PLATFORM.rumble(0.3f, 0);
        } else if(engineState_8004dd20 == EngineStateEnum.COMBAT_06) {
          PLATFORM.rumble(0.75f - rumbleDampener_800bee80, 0);
        } else {
          PLATFORM.rumble(0.75f, 0);
        }
      }
      case 3 -> {
        if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05) {
          PLATFORM.rumble(0.4f, 0);
        } else if(engineState_8004dd20 == EngineStateEnum.COMBAT_06) {
          PLATFORM.rumble(1.0f - rumbleDampener_800bee80, 0);
        } else {
          PLATFORM.rumble(1.0f, 0);
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

    PLATFORM.rumble(intensity, 0);
  }

  @Method(0x8002bda4L)
  public static void adjustRumbleOverTime(final int pad, final int intensity, final int frames) {
    final int divisor = vsyncMode_8007a3b8 * currentEngineState_8004dd04.tickMultiplier();
    adjustRumbleOverTime(pad, intensity, frames, divisor);
  }

  @Method(0x8002bda4L)
  public static void adjustRumbleOverTime(final int pad, int intensity, final int frames, final int framesDivisor) {
    LOGGER.debug("adjustRumbleOverTime %x %x %x", pad, intensity, frames);

    if(!CONFIG.getConfig(CoreMod.RUMBLE_CONFIG.get())) {
      return;
    }

    intensity = java.lang.Math.clamp(intensity, 0, 0x1ff);

    if(frames == 0) {
      startRumbleIntensity(pad, intensity);
      return;
    }

    PLATFORM.adjustRumble(intensity / (float)0x1ff, frames / framesDivisor * 50);
  }

  @Method(0x8002c150L)
  public static void stopRumble(final int pad) {
    LOGGER.debug("stopRumble");
    PLATFORM.stopRumble();
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
  public static int playXaAudio(final int xaLoadingStage, final int xaArchiveIndex, final int xaFileIndex) {
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

  public static void stopXaAudio() {
    AUDIO_THREAD.stopXa();
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
