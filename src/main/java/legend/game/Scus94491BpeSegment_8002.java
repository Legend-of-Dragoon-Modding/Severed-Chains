package legend.game;

import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.Config;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MATRIX;
import legend.core.gte.ModelPart10;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.Transforms;
import legend.core.memory.Method;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.game.fmv.Fmv;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.UseItemResponse;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.CampaignSelectionScreen;
import legend.game.inventory.screens.CharSwapScreen;
import legend.game.inventory.screens.MenuScreen;
import legend.game.inventory.screens.NewCampaignScreen;
import legend.game.inventory.screens.OptionsScreen;
import legend.game.inventory.screens.SaveGameScreen;
import legend.game.inventory.screens.ShopScreen;
import legend.game.inventory.screens.TextColour;
import legend.game.inventory.screens.TooManyItemsScreen;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.inventory.TakeItemEvent;
import legend.game.saves.ConfigStorageLocation;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.sound.EncounterSoundEffects10;
import legend.game.sound.QueuedSound28;
import legend.game.sound.SoundFile;
import legend.game.tim.Tim;
import legend.game.tmd.Renderer;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CContainer;
import legend.game.types.CharacterData2c;
import legend.game.types.EngineState;
import legend.game.types.InventoryMenuState;
import legend.game.types.ItemStats0c;
import legend.game.types.LodString;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuItemStruct04;
import legend.game.types.Model124;
import legend.game.types.ModelPartTransforms0c;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.SubmapStruct80;
import legend.game.types.Textbox4c;
import legend.game.types.TextboxArrow0c;
import legend.game.types.TextboxChar08;
import legend.game.types.TextboxText84;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.types.UiPart;
import legend.game.types.UiType;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Matrix3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.equipmentStats_80111ff0;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.menuAssetsLoaded;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderMenus;
import static legend.game.SItem.renderPostCombatReport;
import static legend.game.SItem.textLength;
import static legend.game.SMap.FUN_800da114;
import static legend.game.SMap.FUN_800de004;
import static legend.game.SMap.FUN_800e3fac;
import static legend.game.SMap.FUN_800e4018;
import static legend.game.SMap.FUN_800e4708;
import static legend.game.SMap.FUN_800e4e5c;
import static legend.game.SMap.FUN_800e4f8c;
import static legend.game.SMap.FUN_800e828c;
import static legend.game.SMap.FUN_800e8e50;
import static legend.game.SMap.FUN_800ea4c8;
import static legend.game.SMap._800c68e8;
import static legend.game.SMap.adjustSmapUvs;
import static legend.game.SMap.encounterAccumulator_800c6ae8;
import static legend.game.SMap.getCollisionAndTransitionInfo;
import static legend.game.SMap.handleEncounters;
import static legend.game.SMap.mapTransition;
import static legend.game.SMap.positionTextboxAtSobj;
import static legend.game.SMap.renderEnvironment;
import static legend.game.SMap.renderSmapModel;
import static legend.game.SMap.renderSmapShadow;
import static legend.game.SMap.submapFlags_800f7e54;
import static legend.game.SMap.unloadSmap;
import static legend.game.Scus94491BpeSegment.FUN_8001ae90;
import static legend.game.Scus94491BpeSegment.FUN_8001d51c;
import static legend.game.Scus94491BpeSegment.FUN_8001e010;
import static legend.game.Scus94491BpeSegment._80010868;
import static legend.game.Scus94491BpeSegment._800108b0;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFileSync;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.rectArray28_80010770;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.soundBufferOffset;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment.stopAndResetSoundsAndSequences;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_Xyz;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_Zyx;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.freeSequence;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8004.stopMusicSequence;
import static legend.game.Scus94491BpeSegment_8005._8005027c;
import static legend.game.Scus94491BpeSegment_8005._8005039c;
import static legend.game.Scus94491BpeSegment_8005._800503b0;
import static legend.game.Scus94491BpeSegment_8005._800503d4;
import static legend.game.Scus94491BpeSegment_8005._800503f8;
import static legend.game.Scus94491BpeSegment_8005._80050424;
import static legend.game.Scus94491BpeSegment_8005._80052b40;
import static legend.game.Scus94491BpeSegment_8005._80052b68;
import static legend.game.Scus94491BpeSegment_8005._80052b88;
import static legend.game.Scus94491BpeSegment_8005._80052b8c;
import static legend.game.Scus94491BpeSegment_8005._80052ba8;
import static legend.game.Scus94491BpeSegment_8005._80052baa;
import static legend.game.Scus94491BpeSegment_8005._80052c20;
import static legend.game.Scus94491BpeSegment_8005._80052c40;
import static legend.game.Scus94491BpeSegment_8005._80052c44;
import static legend.game.Scus94491BpeSegment_8005._8005a1d8;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.monsterSoundFileIndices_800500e8;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c3c;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8005.textboxVramX_80052bc8;
import static legend.game.Scus94491BpeSegment_8005.textboxVramY_80052bf4;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bd7ac;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b._800bd7b4;
import static legend.game.Scus94491BpeSegment_800b._800bd7b8;
import static legend.game.Scus94491BpeSegment_800b._800bdb88;
import static legend.game.Scus94491BpeSegment_800b._800bdc58;
import static legend.game.Scus94491BpeSegment_800b._800bdf04;
import static legend.game.Scus94491BpeSegment_800b._800bdf08;
import static legend.game.Scus94491BpeSegment_800b._800bdf10;
import static legend.game.Scus94491BpeSegment_800b._800bdf18;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b._800bed28;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b.encounterSoundEffects_800bd610;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.hasNoEncounters_800bed58;
import static legend.game.Scus94491BpeSegment_800b.input_800bee90;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.repeat_800bee98;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.soundFiles_800bcf80;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.textU_800be5c0;
import static legend.game.Scus94491BpeSegment_800b.textV_800be5c8;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.textboxArrows_800bdea0;
import static legend.game.Scus94491BpeSegment_800b.textboxText_800bdf38;
import static legend.game.Scus94491BpeSegment_800b.textboxes_800be358;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800e.main;
import static legend.game.combat.Bttl_800e.renderBttlModel;
import static legend.game.combat.Bttl_800e.renderBttlShadow;
import static legend.game.wmap.WMap.adjustWmapUvs;
import static legend.game.wmap.WMap.renderWmapModel;
import static legend.game.wmap.WMap.renderWmapShadow;

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
    unloadEncounterSoundEffects();
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
    unloadEncounterSoundEffects();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Mark a character battle entity's sound files as no longer used")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c index")
  @Method(0x8002013cL)
  public static FlowControl scriptUnuseCharSoundFile(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800201c8L)
  public static void unloadEncounterSoundEffects() {
    final EncounterSoundEffects10 encounterSoundEffects = encounterSoundEffects_800bd610;

    if(encounterSoundEffects._00 != 0) {
      stopMusicSequence(encounterSoundEffects.sequenceData_0c, 1);
      freeSequence(encounterSoundEffects.sequenceData_0c);
      encounterSoundEffects.sssq_08 = null;
      encounterSoundEffects._00 = 0;
    }

    //LAB_80020220
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
    FUN_8001ae90();
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
    soundBufferOffset = 0;

    final int fileIndex = 1290 + script.params_20[0].get();
    for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
      final SoundFile file = soundFiles_800bcf80[monsterSoundFileIndices_800500e8.get(monsterSlot).get()];
      file.charId_02 = -1;
      file.used_00 = false;

      if(Unpacker.exists("SECT/DRGN0.BIN/%d/%d".formatted(fileIndex, monsterSlot))) {
        final int finalMonsterSlot = monsterSlot;
        loadDrgnDir(0, fileIndex + "/" + monsterSlot, files -> FUN_8001d51c(files, "Monster slot %d (file %d) (replaced)".formatted(finalMonsterSlot, fileIndex), finalMonsterSlot));
      }
    }

    loadedDrgnFiles_800bcf78.updateAndGet(val -> val & 0xffff_ffef);
    return FlowControl.CONTINUE;
  }

  @Method(0x80020468L)
  public static void adjustCombatUvs(final ModelPart10 dobj2, final int colourMap) {
    final TmdObjTable1c objTable = dobj2.tmd_08;

    for(final TmdObjTable1c.Primitive primitive : objTable.primitives_10) {
      final int command = primitive.header() & 0xff04_0000;

      if(command == 0x3c00_0000 || command == 0x3e00_0000) {
        FUN_800210c4(primitive, colourMap & 0x7f);
      } else if(command == 0x3d00_0000 || command == 0x3f00_0000) {
        FUN_8002117c(primitive, colourMap & 0x7f);
      } else if(command == 0x3500_0000 || command == 0x3700_0000) {
        FUN_80021120(primitive, colourMap & 0x7f);
      } else if(command == 0x3400_0000 || command == 0x3600_0000) {
        FUN_80021068(primitive, colourMap & 0x7f);
      }
    }
  }

  /** Very similar to {@link Scus94491BpeSegment_800e#FUN_800e6b3c(Model124, CContainer, TmdAnimationFile)} */
  @Method(0x80020718L)
  public static void FUN_80020718(final Model124 model, final CContainer cContainer, final TmdAnimationFile tmdAnimFile) {
    LOGGER.info("Loading scripted TMD %s (animation %s)", cContainer, tmdAnimFile);

    final int transferX = model.coord2_14.coord.transfer.getX();
    final int transferY = model.coord2_14.coord.transfer.getY();
    final int transferZ = model.coord2_14.coord.transfer.getZ();

    //LAB_80020760
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    if(engineState_8004dd20 == EngineState.SUBMAP_05) {
      FUN_800de004(model, cContainer);
    }

    //LAB_8002079c
    model.tpage_108 = (int)((cContainer.tmdPtr_00.id & 0xffff_0000L) >>> 11); // LOD uses the upper 16 bits of TMD IDs as tpage (sans VRAM X/Y)

    if(cContainer.ptr_08 != null) {
      model.ptr_a8 = cContainer.ptr_08;

      //LAB_800207d4
      for(int i = 0; i < 7; i++) {
        model.ptrs_d0[i] = model.ptr_a8._00[i];
        FUN_8002246c(model, i);
      }
    } else {
      //LAB_80020818
      model.ptr_a8 = null; //TODO was this needed? cContainer.ptr_08.getAddress();

      //LAB_80020828
      for(int i = 0; i < 7; i++) {
        model.ptrs_d0[i] = null;
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

    model.coord2_14.coord.transfer.set(transferX, transferY, transferZ);

    adjustModelUvs(model);

    //LAB_800209b0
    model.shadowType_cc = 0;
    model.modelPartWithShadowIndex_cd = -2;
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
    model.shadowSize_10c.set(1.0f, 1.0f, 1.0f);
    model.shadowOffset_118.set(0, 0, 0);
  }

  @Method(0x80020a00L)
  public static void initModel(final Model124 model, final CContainer CContainer, final TmdAnimationFile tmdAnimFile) {
    model.modelParts_00 = new ModelPart10[CContainer.tmdPtr_00.tmd.header.nobj];

    Arrays.setAll(model.modelParts_00, i -> new ModelPart10());

    FUN_80020718(model, CContainer, tmdAnimFile);
  }

  @Method(0x80020b98L)
  public static void animateModel(final Model124 model) {
    animateModel(model, 1);
  }

  public static void animateModel(final Model124 model, final int interpolationFrameCount) {
    if(engineState_8004dd20 == EngineState.SUBMAP_05) {
      FUN_800da114(model);
      return;
    }

    //LAB_80020be8
    //LAB_80020bf0
    for(int i = 0; i < 7; i++) {
      if(model.animateTextures_ec[i]) {
        animateModelTextures(model, i);
      }

      //LAB_80020c08
    }

    if(model.animationState_9c == 2) {
      return;
    }

    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    }

    //LAB_80020c3c
    if(model.animationState_9c == 0) {
      if(model.disableInterpolation_a2) {
        //LAB_80020c68
        model.remainingFrames_9e = model.totalFrames_9a / 2;
      } else {
        model.remainingFrames_9e = model.totalFrames_9a;
      }

      model.interpolationFrameIndex = 0;

      //LAB_80020c7c
      model.animationState_9c = 1;
      model.partTransforms_94 = model.partTransforms_90;
    }

    //LAB_80020c90
    final ModelPartTransforms0c[][] transforms = model.partTransforms_94;

    if(model.interpolationFrameIndex != interpolationFrameCount && !model.disableInterpolation_a2) { // Interpolation frame
      if(model.ub_a3 == 0) { // Only set to 1 sometimes on submaps?
        //LAB_80020ce0
        for(int i = 0; i < model.modelParts_00.length; i++) {
          final GsCOORDINATE2 coord2 = model.modelParts_00[i].coord2_04;
          final Transforms params = coord2.transforms;

          final float interpolationScale = (model.interpolationFrameIndex + 1) / (float)interpolationFrameCount;

          RotMatrix_Zyx(params.rotate, coord2.coord);
          params.trans.set(
            Math.lerp(params.trans.x, transforms[0][i].translate_06.x, interpolationScale),
            Math.lerp(params.trans.y, transforms[0][i].translate_06.y, interpolationScale),
            Math.lerp(params.trans.z, transforms[0][i].translate_06.z, interpolationScale)
          );
          coord2.coord.transfer.set(params.trans);
        }

        //LAB_80020d6c
      } else {
        //LAB_80020d74
        //LAB_80020d8c
        for(int i = 0; i < model.modelParts_00.length; i++) {
          final GsCOORDINATE2 coord2 = model.modelParts_00[i].coord2_04;
          final Transforms params = coord2.transforms;

          params.rotate.set(transforms[0][i].rotate_00);
          RotMatrix_Zyx(params.rotate, coord2.coord);

          params.trans.set(transforms[0][i].translate_06);
          coord2.coord.transfer.set(params.trans);
        }

        //LAB_80020dfc
      }

      if(model.interpolationFrameIndex == 0) {
        model.remainingFrames_9e--;
      }

      model.interpolationFrameIndex++;

      //LAB_80020e00
    } else {
      //LAB_80020e0c
      //LAB_80020e24
      for(int i = 0; i < model.modelParts_00.length; i++) {
        final GsCOORDINATE2 coord2 = model.modelParts_00[i].coord2_04;
        final Transforms params = coord2.transforms;

        params.rotate.set(transforms[0][i].rotate_00);
        RotMatrix_Zyx(params.rotate, coord2.coord);

        params.trans.set(transforms[0][i].translate_06);
        coord2.coord.transfer.set(params.trans);
      }

      //LAB_80020e94
      model.partTransforms_94 = Arrays.copyOfRange(transforms, 1, transforms.length);
      model.interpolationFrameIndex = 0;
      model.remainingFrames_9e--;
    }

    //LAB_80020e98

    //LAB_80020ea8
  }

  @Method(0x80020ed8L)
  public static void FUN_80020ed8() {
    if(_800bdb88 == EngineState.SUBMAP_05) {
      if(_800bd7b4.get() == 0x1L) {
        FUN_800e4708();
      }

      //LAB_80020f20
      FUN_8002aae8();
      FUN_800e4018();
    }

    //LAB_80020f30
    //LAB_80020f34
    final EngineState a0 = _800bdb88;
    _800bd7b4.setu(0);
    if(a0 != engineState_8004dd20) {
      _800bdb88 = engineState_8004dd20;

      if(engineState_8004dd20 == EngineState.SUBMAP_05) {
        _800bd7b0.set(2);
        _800bd7b8.setu(0);

        if(a0 == EngineState.TITLE_02) {
          _800bd7b0.set(9);
        }

        //LAB_80020f84
        if(a0 == EngineState.COMBAT_06) {
          _800bd7b0.set(-4);
          _800bd7b8.setu(0x1L);
        }

        //LAB_80020fa4
        if(a0 == EngineState.WORLD_MAP_08) {
          _800bd7b0.set(3);
        }
      }
    }

    //LAB_80020fb4
    //LAB_80020fb8
    if(_800bdb88 == EngineState.TITLE_02) {
      _800bd7ac.setu(0x1L);
    }

    //LAB_80020fd0
  }

  @Method(0x80021068L)
  public static void FUN_80021068(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _8005027c.offset(colourMap * 0x10L).getAddress();

    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get());
      MathHelper.set(data, 0x4, 4, MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get());
    }
  }

  @Method(0x800210c4L)
  public static void FUN_800210c4(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _8005027c.offset(colourMap * 0x10L).getAddress();

    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get());
      MathHelper.set(data, 0x4, 4, MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get());
    }
  }

  @Method(0x8002117cL)
  public static void FUN_8002117c(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _8005027c.offset(colourMap * 0x10L).getAddress();

    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get());
      MathHelper.set(data, 0x4, 4, MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get());
    }
  }

  @Method(0x80021120L)
  public static void FUN_80021120(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _8005027c.offset(colourMap * 0x10L).getAddress();

    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get());
      MathHelper.set(data, 0x4, 4, MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get());
    }
  }

  @Method(0x800211d8L)
  public static void renderModel(final Model124 model) {
    if(engineState_8004dd20 == EngineState.SUBMAP_05) {
      //LAB_80021230
      renderSmapModel(model);
    } else if(engineState_8004dd20 == EngineState.COMBAT_06) {
      //LAB_80021220
      renderBttlModel(model);
    } else if(engineState_8004dd20 == EngineState.WORLD_MAP_08) {
      //LAB_8002120c
      //LAB_80021240
      renderWmapModel(model);
    }

    //LAB_80021248
  }

  @Method(0x80021258L)
  public static void renderDobj2(final ModelPart10 dobj2) {
    if(engineState_8004dd20 == EngineState.SUBMAP_05) {
      //LAB_800212b0
      Renderer.renderDobj2(dobj2, false, 0);
      return;
    }

    if(engineState_8004dd20 == EngineState.COMBAT_06) {
      //LAB_800212a0
      Renderer.renderDobj2(dobj2, true, 0);
      return;
    }

    //LAB_8002128c
    if(engineState_8004dd20 == EngineState.WORLD_MAP_08) {
      //LAB_800212c0
      Renderer.renderDobj2(dobj2, false, 0);
    }

    //LAB_800212c8
  }

  @Method(0x800212d8L)
  public static void applyModelPartTransforms(final Model124 model) {
    if(model.modelParts_00.length == 0) {
      return;
    }

    final ModelPartTransforms0c[][] transforms = model.partTransforms_94;

    //LAB_80021320
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPart10 obj2 = model.modelParts_00[i];

      final GsCOORDINATE2 coord2 = obj2.coord2_04;
      final Transforms params = coord2.transforms;
      final MATRIX matrix = coord2.coord;

      params.rotate.set(transforms[0][i].rotate_00);
      params.trans.set(transforms[0][i].translate_06);

      RotMatrix_Zyx(params.rotate, matrix);
      matrix.transfer.set(params.trans);
    }

    //LAB_80021390
    model.partTransforms_94 = Arrays.copyOfRange(transforms, 1, transforms.length);
  }

  @Method(0x800213c4L)
  public static void applyInterpolationFrame(final Model124 model) {
    //LAB_80021404
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPartTransforms0c transforms = model.partTransforms_94[0][i];
      final GsCOORDINATE2 coord2 = model.modelParts_00[i].coord2_04;
      final MATRIX coord = coord2.coord;
      final Transforms params = coord2.transforms;
      RotMatrix_Zyx(params.rotate, coord);
      params.trans.x = (params.trans.x + transforms.translate_06.x) / 2.0f;
      params.trans.y = (params.trans.y + transforms.translate_06.y) / 2.0f;
      params.trans.z = (params.trans.z + transforms.translate_06.z) / 2.0f;
      coord.transfer.set(params.trans);
    }

    //LAB_80021490
    model.partTransforms_94 = Arrays.copyOfRange(model.partTransforms_94, 1, model.partTransforms_94.length);
  }

  @Method(0x800214bcL)
  public static void applyModelRotationAndScale(final Model124 model) {
    RotMatrix_Xyz(model.coord2_14.transforms.rotate, model.coord2_14.coord);
    model.coord2_14.coord.scale(model.coord2_14.transforms.scale);
    model.coord2_14.flg = 0;
  }

  @Method(0x80021520L)
  public static void FUN_80021520(final Model124 model, final CContainer a1, final TmdAnimationFile a2, final long a3) {
    FUN_80020718(model, a1, a2);
    FUN_8002155c(model, a3);
  }

  @Method(0x8002155cL)
  public static void FUN_8002155c(final Model124 model, final long a1) {
    final float scale = (int)_8005039c.offset(2, a1 * 0x2L).getSigned() / (float)0x1000;
    model.shadowSize_10c.set(scale, scale, scale);
  }

  @Method(0x80021584L)
  public static void loadModelStandardAnimation(final Model124 model, final TmdAnimationFile tmdAnimFile) {
    model.animType_90 = -1;
    model.partTransforms_90 = tmdAnimFile.partTransforms_10;
    model.partTransforms_94 = tmdAnimFile.partTransforms_10;
    model.partCount_98 = tmdAnimFile.modelPartCount_0c;
    model.totalFrames_9a = tmdAnimFile.totalFrames_0e;
    model.animationState_9c = 0;

    applyModelPartTransforms(model);

    if(model.disableInterpolation_a2) {
      //LAB_800215e8
      model.remainingFrames_9e = model.totalFrames_9a / 2;
    } else {
      model.remainingFrames_9e = model.totalFrames_9a;
    }

    model.interpolationFrameIndex = 0;

    //LAB_80021608
    model.animationState_9c = 1;
    model.partTransforms_94 = model.partTransforms_90;
  }

  @Method(0x80021628L)
  public static void adjustModelUvs(final Model124 model) {
    if(engineState_8004dd20 == EngineState.SUBMAP_05) {
      for(final ModelPart10 dobj2 : model.modelParts_00) {
        adjustSmapUvs(dobj2, model.colourMap_9d);
      }
    } else if(engineState_8004dd20 == EngineState.WORLD_MAP_08) {
      for(final ModelPart10 dobj2 : model.modelParts_00) {
        adjustWmapUvs(dobj2, model.colourMap_9d);
      }
    } else {
      for(final ModelPart10 dobj2 : model.modelParts_00) {
        adjustCombatUvs(dobj2, model.colourMap_9d);
      }
    }
  }

  @Method(0x80021724L)
  public static void renderShadow(final Model124 model) {
    if(engineState_8004dd20 == EngineState.SUBMAP_05) {
      renderSmapShadow(model);
    } else if(engineState_8004dd20 == EngineState.COMBAT_06) {
      renderBttlShadow(model);
    } else if(engineState_8004dd20 == EngineState.WORLD_MAP_08) {
      renderWmapShadow(model);
    }

    //LAB_80021794
  }

  @Method(0x800217a4L)
  public static void FUN_800217a4(final Model124 model) {
    model.coord2_14.transforms.rotate.y = MathHelper.psxDegToRad(FUN_800ea4c8(MathHelper.radToPsxDeg(model.coord2_14.transforms.rotate.y)));
    RotMatrix_Xyz(model.coord2_14.transforms.rotate, model.coord2_14.coord);
    model.coord2_14.coord.scale(model.coord2_14.transforms.scale);
    model.coord2_14.flg = 0;
  }

  @Method(0x800218f0L)
  public static void FUN_800218f0() {
    if(_800bd7ac.get() == 1) {
      _800bd7b0.set(9);
      _800bd7ac.setu(0);
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

      RotMatrix_Xyz(dobj2.coord2_04.transforms.rotate, dobj2.coord2_04.coord);
      dobj2.coord2_04.coord.scaleL(dobj2.coord2_04.transforms.scale);
      dobj2.coord2_04.coord.transfer.set(dobj2.coord2_04.transforms.trans);
    }

    //LAB_80021db4
  }

  @Method(0x80021edcL)
  public static void SetRotMatrix(final MATRIX m) {
    GTE.setRotationMatrix(m);
  }

  @Method(0x80021f0cL)
  public static void SetLightMatrix(final Matrix3f m) {
    GTE.setLightSourceMatrix(m);
  }

  @Method(0x80021f3cL)
  public static void SetColorMatrix(final Matrix3f m) {
    GTE.setLightColourMatrix(m);
  }

  @Method(0x80021f6cL)
  public static void SetTransMatrix(final MATRIX m) {
    GTE.setTranslationVector(m.transfer);
  }

  @Method(0x80021facL)
  public static void SetGeomOffset(final int x, final int y) {
    GTE.setScreenOffset(x, y);
  }

  @Method(0x80021fc4L)
  public static int SquareRoot0(final long n) {
    return (int)Math.sqrt(n);
  }

  /**
   * This method animates the fog in the first cutscene with Rose/Feyrbrand
   */
  @Method(0x80022018L)
  public static void animateModelTextures(final Model124 a0, final int index) {
    if(a0.ptrs_d0[index] == null) {
      a0.animateTextures_ec[index] = false;
      return;
    }

    //LAB_80022068
    final int x;
    final int y;
    if((a0.colourMap_9d & 0x80) == 0) {
      x = _800503b0.get(a0.colourMap_9d).get();
      y = _800503d4.get(a0.colourMap_9d).get();
    } else {
      //LAB_80022098
      if(a0.colourMap_9d == 0x80) {
        return;
      }

      x = _800503f8.get(a0.colourMap_9d & 0x7f).get();
      y = _80050424.get(a0.colourMap_9d & 0x7f).get();
    }

    //LAB_800220c0
    if(a0.usArr_ba[index] != 0x5678) {
      a0.usArr_ba[index]--;
      if((short)a0.usArr_ba[index] != 0) {
        return;
      }

      int s1 = 0;
      a0.usArr_ba[index] = a0.ptrs_d0[index][s1++] & 0x7fff;
      final int destX = a0.ptrs_d0[index][s1++] + x;
      final int destY = a0.ptrs_d0[index][s1++] + y;
      final short w = (short)(a0.ptrs_d0[index][s1++] / 4);
      final short h = a0.ptrs_d0[index][s1++];

      //LAB_80022154
      for(int i = 0; i < a0.usArr_ac[index]; i++) {
        s1 += 2;
      }

      //LAB_80022164
      final short x2 = (short)(a0.ptrs_d0[index][s1++] + x);
      final short y2 = (short)(a0.ptrs_d0[index][s1++] + y);

      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x2, y2, destX & 0xffff, destY & 0xffff, w, h));

      a0.usArr_ac[index]++;

      final int v1 = a0.ptrs_d0[index][s1];
      if(v1 == -2) {
        a0.animateTextures_ec[index] = false;
        a0.usArr_ac[index] = 0;
      }

      //LAB_800221f8
      if(v1 == -1) {
        a0.usArr_ac[index] = 0;
      }

      return;
    }

    //LAB_80022208
    int s1 = 1;
    final int s6 = a0.ptrs_d0[index][s1++] + x;
    final int s7 = a0.ptrs_d0[index][s1++] + y;
    final int s5 = a0.ptrs_d0[index][s1++] / 4;
    int s3 = a0.ptrs_d0[index][s1++];
    final int v1 = a0.ptrs_d0[index][s1++];
    int s0_0 = a0.ptrs_d0[index][s1];

    if((a0.usArr_ac[index] & 0xf) != 0) {
      a0.usArr_ac[index]--;

      if(a0.usArr_ac[index] == 0) {
        a0.usArr_ac[index] = s0_0;
        s0_0 = 16;
      } else {
        //LAB_80022278
        s0_0 = 0;
      }
    }

    //LAB_8002227c
    if((short)s0_0 == 0) {
      return;
    }

    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, s6 & 0xffff, s7 & 0xffff, s5, s3));

    s0_0 /= 16;
    s3 -= s0_0;

    if((short)v1 == 0) {
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(s6, s7 + s3, 960, 256, s5, s0_0));
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(s6, s7, 960, s0_0 + 256, s5, s3));
    } else {
      //LAB_80022358
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(s6, s7, 960, s3 + 256, s5, s0_0));
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(s6, s7 + s0_0, 960, 256, s5, s3));
    }

    //LAB_80022440
  }

  @Method(0x8002246cL)
  public static void FUN_8002246c(final Model124 a0, final int a1) {
    if(a0.ptrs_d0[a1] == null) {
      a0.animateTextures_ec[a1] = false;
      return;
    }

    //LAB_80022490
    a0.usArr_ac[a1] = 0;
    a0.usArr_ba[a1] = a0.ptrs_d0[a1][0] & 0x3fff;

    //LAB_800224d0
    a0.animateTextures_ec[a1] = (a0.ptrs_d0[a1][0] & 0x8000) != 0;

    //LAB_800224d8
    if((a0.ptrs_d0[a1][0] & 0x4000) != 0) {
      a0.usArr_ba[a1] = 0x5678;
      a0.usArr_ac[a1] = a0.ptrs_d0[a1][6];
      a0.animateTextures_ec[a1] = true;
    }

    //LAB_80022510
  }

  private static WhichMenu destMenu;
  private static Supplier<MenuScreen> destScreen;

  private static void initMenu(final WhichMenu destMenu, final Supplier<MenuScreen> destScreen) {
    if((getLoadedDrgnFiles() & 0x80L) == 0) {
      inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_0);
      whichMenu_800bdc38 = WhichMenu.WAIT_FOR_MUSIC_TO_LOAD_AND_LOAD_S_ITEM_2;
      FUN_8001e010(0);
      SCRIPTS.stop();
      Scus94491BpeSegment_8002.destMenu = destMenu;
      Scus94491BpeSegment_8002.destScreen = destScreen;
    }
  }

  @Method(0x80022590L)
  public static void loadAndRenderMenus() {
    switch(whichMenu_800bdc38) {
      case INIT_INVENTORY_MENU_1 -> initMenu(WhichMenu.RENDER_INVENTORY_MENU_4, null);
      case INIT_SHOP_MENU_6 -> initMenu(WhichMenu.RENDER_SHOP_MENU_9, ShopScreen::new);
      case INIT_CAMPAIGN_SELECTION_MENU -> initMenu(WhichMenu.RENDER_CAMPAIGN_SELECTION_MENU, CampaignSelectionScreen::new);
      case INIT_SAVE_GAME_MENU_16 -> initMenu(WhichMenu.RENDER_SAVE_GAME_MENU_19, () -> new SaveGameScreen(() -> whichMenu_800bdc38 = WhichMenu.UNLOAD_SAVE_GAME_MENU_20));
      case INIT_NEW_CAMPAIGN_MENU -> initMenu(WhichMenu.RENDER_NEW_CAMPAIGN_MENU, NewCampaignScreen::new);
      case INIT_OPTIONS_MENU -> initMenu(WhichMenu.RENDER_OPTIONS_MENU, () -> new OptionsScreen(CONFIG, Set.of(ConfigStorageLocation.GLOBAL), () -> whichMenu_800bdc38 = WhichMenu.UNLOAD_OPTIONS_MENU));
      case INIT_CHAR_SWAP_MENU_21 -> {
        loadCharacterStats();
        cacheCharacterSlots();
        initMenu(WhichMenu.RENDER_CHAR_SWAP_MENU_24, () -> new CharSwapScreen(() -> whichMenu_800bdc38 = WhichMenu.UNLOAD_CHAR_SWAP_MENU_25));
      }
      case INIT_TOO_MANY_ITEMS_MENU_31 -> initMenu(WhichMenu.RENDER_TOO_MANY_ITEMS_MENU_34, TooManyItemsScreen::new);

      case WAIT_FOR_MUSIC_TO_LOAD_AND_LOAD_S_ITEM_2 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38 = WhichMenu.WAIT_FOR_S_ITEM_TO_LOAD_3;

          renderablePtr_800bdc5c = null;
          uiFile_800bdc3c = null;
          resizeDisplay(384, 240);
          loadDrgnFileSync(0, 6665, data -> menuAssetsLoaded(data, 0));
          loadDrgnFileSync(0, 6666, data -> menuAssetsLoaded(data, 1));
          textZ_800bdf00.set(33);

          loadSupportOverlay(2, () -> {
            whichMenu_800bdc38 = destMenu;

            if(destScreen != null) {
              menuStack.pushScreen(destScreen.get());
              destScreen = null;
            }
          });
        }
      }

      case INIT_POST_COMBAT_REPORT_26 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_0);
          whichMenu_800bdc38 = WhichMenu.WAIT_FOR_POST_COMBAT_REPORT_MUSIC_TO_LOAD_AND_LOAD_S_ITEM_27;
        }
      }

      case WAIT_FOR_POST_COMBAT_REPORT_MUSIC_TO_LOAD_AND_LOAD_S_ITEM_27 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38 = WhichMenu.WAIT_FOR_S_ITEM_TO_LOAD_28;
          loadSupportOverlay(2, () -> whichMenu_800bdc38 = WhichMenu.RENDER_POST_COMBAT_REPORT_29);
        }
      }

      case RENDER_SHOP_MENU_9, RENDER_CAMPAIGN_SELECTION_MENU, RENDER_SAVE_GAME_MENU_19, RENDER_CHAR_SWAP_MENU_24, RENDER_TOO_MANY_ITEMS_MENU_34, RENDER_NEW_CAMPAIGN_MENU, RENDER_OPTIONS_MENU -> menuStack.render();
      case RENDER_INVENTORY_MENU_4, RENDER_SHOP_CARRIED_ITEMS_36 -> renderMenus();
      case RENDER_POST_COMBAT_REPORT_29 -> renderPostCombatReport();

      case UNLOAD_CAMPAIGN_SELECTION_MENU, UNLOAD_SAVE_GAME_MENU_20, UNLOAD_CHAR_SWAP_MENU_25, UNLOAD_NEW_CAMPAIGN_MENU, UNLOAD_OPTIONS_MENU -> {
        menuStack.popScreen();

        if(whichMenu_800bdc38 != WhichMenu.UNLOAD_SAVE_GAME_MENU_20) {
          FUN_8001e010(-1);
        }

        SCRIPTS.start();
        whichMenu_800bdc38 = WhichMenu.NONE_0;

        deallocateRenderables(0xff);
        uiFile_800bdc3c = null;

        startFadeEffect(2, 10);

        if(engineState_8004dd20 == EngineState.SUBMAP_05) {
          FUN_800e3fac();
        }

        textZ_800bdf00.set(13);
      }

      case UNLOAD_INVENTORY_MENU_5, UNLOAD_SHOP_MENU_10, UNLOAD_TOO_MANY_ITEMS_MENU_35 -> {
        FUN_8001e010(-1);
        SCRIPTS.start();
        whichMenu_800bdc38 = WhichMenu.NONE_0;
      }

      case UNLOAD_POST_COMBAT_REPORT_30 -> {
        SCRIPTS.start();
        whichMenu_800bdc38 = WhichMenu.NONE_0;
      }
    }
  }

  @Method(0x80022898L)
  public static boolean itemCantBeDiscarded(final int itemId) {
    if(itemId >= 0xc0) {
      return false;
    }

    return (equipmentStats_80111ff0[itemId].flags_00 & 0x4) != 0;
  }

  @Method(0x800228d0L)
  public static int getItemIcon(final int itemId) {
    if(itemId >= 0xc0) {
      return itemStats_8004f2ac[itemId - 0xc0].icon_07;
    }

    //LAB_80022908
    return equipmentStats_80111ff0[itemId].icon_0e;
  }

  @Method(0x80022928L)
  public static int getUnlockedDragoonSpells(final byte[] spellIndicesOut, final int charIndex) {
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
      final MagicStuff08 spellStuff = magicStuff_80111d20.get(charIndex).deref().get(dlevel);
      final byte spellIndex = spellStuff.spellIndex_02.get();

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
      if(magicStuff_80111d20.get(charIndex).deref().get(i).spellIndex_02.get() != -1) {
        unlockedSpells++;
      }

      //LAB_80022a7c
    }

    return unlockedSpells;
  }

  @Method(0x80022a94L)
  public static void FUN_80022a94(final FileData data) {
    final Tim tim = new Tim(data);
    final RECT imageRect = tim.getImageRect();

    if(imageRect.w.get() != 0 || imageRect.h.get() != 0) {
      LoadImage(imageRect, tim.getImageData());
    }

    //LAB_80022acc
    if(tim.hasClut()) {
      LoadImage(tim.getClutRect(), tim.getClutData());
    }

    //LAB_80022aec
  }

  @Method(0x80022afcL)
  public static int itemCanBeUsedInMenu(final int itemId) {
    if(itemId < 0xc0 || itemId == 0xff) {
      return 0;
    }

    final int target = itemStats_8004f2ac[itemId - 0xc0].target_00;

    if((target & 0x10L) == 0) {
      //LAB_80022b40
      return 0;
    }

    //LAB_80022b48
    return target & 0x12;
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

  @Method(0x80022d88L)
  public static UseItemResponse useItemInMenu(final UseItemResponse response, final int itemId, final int charIndex) {
    response._00 = 0;
    response.value_04 = 0;

    if(itemCanBeUsedInMenu(itemId) == 0) {
      //LAB_80022dd8
      return response;
    }

    //LAB_80022e0c
    response._00 = 1;

    if(itemId == 0xdf) { // Charm potion
      if(engineState_8004dd20 == EngineState.WORLD_MAP_08 || hasNoEncounters_800bed58.get() == 0) {
        //LAB_80022e40
        response._00 = 8;
        encounterAccumulator_800c6ae8.set(0);
      } else {
        //LAB_80022e50
        response._00 = 9;
      }

      //LAB_80022e54
      //LAB_80022e60
      return response;
    }

    //LAB_80022e94
    final ItemStats0c itemStats = itemStats_8004f2ac[itemId - 0xc0];
    final int percentage = itemStats.percentage_09;
    if((itemStats.type_0b & 0x80) != 0) {
      //LAB_80022edc
      response._00 = (itemStats.target_00 & 0x2) == 0 ? 2 : 3;

      final int amount;
      if(percentage == 100) {
        amount = -1;
      } else {
        //LAB_80022ef0
        amount = stats_800be5f8[charIndex].maxHp_66 * percentage / 100;
      }

      //LAB_80022f3c
      response.value_04 = addHp(charIndex, amount);
    }

    //LAB_80022f50
    if((itemStats.type_0b & 0x40) != 0) {
      //LAB_80022f98
      response._00 = (itemStats.target_00 & 0x2) == 0 ? 4 : 5;

      final int amount;
      if(percentage == 100) {
        amount = -1;
      } else {
        //LAB_80022fac
        amount = stats_800be5f8[charIndex].maxMp_6e * percentage / 100;
      }

      //LAB_80022ff8
      response.value_04 = addMp(charIndex, amount);
    }

    //LAB_8002300c
    if((itemStats.type_0b & 0x20) != 0) {
      response._00 = 6;

      final int amount;
      if(percentage == 100) {
        amount = -1;
      } else {
        amount = percentage;
      }

      //LAB_80023050
      response.value_04 = addSp(charIndex, amount);
    }

    //LAB_80023068
    if((itemStats.type_0b & 0x8) != 0) {
      final int status = gameState_800babc8.charData_32c[charIndex].status_10;

      if((itemStats.status_08 & status) != 0) {
        response.value_04 = status;
        gameState_800babc8.charData_32c[charIndex].status_10 &= ~status;
      }

      //LAB_800230ec
      response._00 = 7;
    }

    //LAB_800230f0
    //LAB_800230fc
    //LAB_8002312c
    return response;
  }

  @Method(0x80023264L)
  public static void checkForPsychBombX() {
    gameState_800babc8.scriptFlags2_bc.set(13, 18, gameState_800babc8.items_2e9.contains(0xfa)); // Psych Bomb X
  }

  public static int takeItemId(final int itemId) {
    final int itemSlot = gameState_800babc8.items_2e9.indexOf(itemId);

    if(itemSlot != -1) {
      return takeItem(itemSlot);
    }

    return 0xff;
  }

  @Method(0x800232dcL)
  public static int takeItem(final int itemSlot) {
    if(itemSlot >= gameState_800babc8.items_2e9.size()) {
      LOGGER.warn("Tried to take item index %d (out of bounds)".formatted(itemSlot));
      return 0xff;
    }

    final int itemId = gameState_800babc8.items_2e9.getInt(itemSlot);

    final TakeItemEvent takeItemEvent = EVENTS.postEvent(new TakeItemEvent(itemId, true));

    if(takeItemEvent.takeItem) {
      gameState_800babc8.items_2e9.removeInt(itemSlot);
    }

    return 0;
  }

  public static int takeEquipmentId(final int equipmentId) {
    final int equipmentSlot = gameState_800babc8.equipment_1e8.indexOf(equipmentId);

    if(equipmentSlot != 0xff) {
      return takeEquipment(equipmentSlot);
    }

    return 0xff;
  }

  @Method(0x800233d8L)
  public static int takeEquipment(final int equipmentIndex) {
    if(equipmentIndex >= gameState_800babc8.equipment_1e8.size()) {
      LOGGER.warn("Tried to take equipment index %d (out of bounds)".formatted(equipmentIndex));
      return 0xff;
    }

    gameState_800babc8.equipment_1e8.removeInt(equipmentIndex);

    return 0;
  }

  @Method(0x80023484L)
  public static int giveItem(final int itemId) {
    if(itemId == 0xff) {
      return 0xff;
    }

    if(itemId >= 0x100) {
      return 0;
    }

    if(itemId < 0xc0) {
      final int count = gameState_800babc8.equipment_1e8.size();

      if(count >= 255) {
        return 0xff;
      }

      gameState_800babc8.equipment_1e8.add(itemId);
      return 0;
    }

    //LAB_800234f4
    final int count = gameState_800babc8.items_2e9.size();

    if(count >= CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get())) {
      //LAB_8002350c
      return 0xff;
    }

    //LAB_80023514
    gameState_800babc8.items_2e9.add(itemId);

    //LAB_80023530
    //LAB_80023534
    return 0;
  }

  @Method(0x80023544L)
  public static int giveItems(final ArrayRef<IntRef> items, final IntRef itemCount) {
    int count = 0;
    //LAB_80023580
    for(int itemSlot = 0; itemSlot < itemCount.get(); itemSlot++) {
      if(giveItem(items.get(itemSlot).get()) != 0) {
        count++;
      } else {
        //LAB_800235a4
        //LAB_800235c0
        int i;
        for(i = itemSlot; i < itemCount.get() - 1; i++) {
          items.get(i).set(items.get(i + 1).get());
        }

        //LAB_800235e4
        items.get(i).set(0xff);
        itemCount.decr();
        itemSlot--;
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
  public static void playSound(final long soundIndex) {
    Scus94491BpeSegment.playSound(0, (int)soundIndex, 0, 0, (short)0, (short)0);
  }

  /**
   * Gets the highest priority button on the joypad that is currently pressed. "Priority" is likely arbitrary.
   */
  @Method(0x800238a4L)
  public static int getJoypadInputByPriority() {
    final int repeat = repeat_800bee98.get();

    if((repeat & 0x4) != 0) {
      return 0x4;
    }

    //LAB_800238c4
    if((repeat & 0x8) != 0) {
      return 0x8;
    }

    //LAB_800238d4
    if((repeat & 0x1) != 0) {
      return 0x1;
    }

    //LAB_800238e4
    if((repeat & 0x2) != 0) {
      return 0x2;
    }

    //LAB_800238f4
    if((repeat & 0x1000) != 0) {
      return 0x1000;
    }

    //LAB_80023904
    if((repeat & 0x4000) != 0) {
      return 0x4000;
    }

    //LAB_80023914
    if((repeat & 0x8000) != 0) {
      return 0x8000;
    }

    //LAB_80023924
    if((repeat & 0x2000) != 0) {
      return 0x2000;
    }

    //LAB_80023934
    final int press = press_800bee94.get();

    if((press & 0x10) != 0) {
      return 0x10;
    }

    //LAB_80023950
    if((press & 0x40) != 0) {
      return 0x40;
    }

    //LAB_80023960
    if((press & 0x80) != 0) {
      return 0x80;
    }

    //LAB_80023970
    return press & 0x20;
  }

  @Method(0x800239e0L)
  public static void setInventoryFromDisplay(final List<MenuItemStruct04> display, final IntList out, final int count) {
    out.clear();

    //LAB_800239ec
    for(int i = 0; i < count; i++) {
      if((display.get(i).flags_02 & 0x1000) == 0) {
        out.add(display.get(i).itemId_00);
      }
    }
  }

  @Method(0x80023a2cL)
  public static void sortItems(final List<MenuItemStruct04> display, final IntList items, final int count) {
    display.sort(menuItemComparator());
    setInventoryFromDisplay(display, items, count);
  }

  public static Comparator<MenuItemStruct04> menuItemComparator() {
    return Comparator
      .comparingInt((MenuItemStruct04 item) -> getItemIcon(item.itemId_00))
      .thenComparingInt(item -> item.itemId_00);
  }

  @Method(0x80023a88L)
  public static void FUN_80023a88() {
    final List<MenuItemStruct04> items = new ArrayList<>();

    for(int i = 0; i < gameState_800babc8.items_2e9.size(); i++) {
      final MenuItemStruct04 item = new MenuItemStruct04();
      item.itemId_00 = gameState_800babc8.items_2e9.getInt(i);
      items.add(item);
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
    renderable._08 = uiType.entries_08()[0]._02();
    renderable._0c = 0;
    renderable.startGlyph_10 = 0;
    renderable.endGlyph_14 = uiType.entries_08().length - 1;
    renderable._18 = 0;
    renderable._1c = 0;
    renderable.uiType_20 = uiType;

    renderable._28 = 0;
    renderable.tpage_2c = 0;
    renderable._34 = 0x1000;
    renderable._38 = 0x1000;
    renderable.z_3c = 36;
    renderable.x_40 = 0;
    renderable.y_44 = 0;
    renderable._48 = 0;
    renderable.child_50 = null;

    return renderable;
  }

  @Method(0x80023c28L)
  public static void uploadRenderables() {
    _800bdc58.addu(0x1L);

    uploadRenderable(renderablePtr_800bdc5c, 0, 0);
  }

  public static void uploadRenderable(Renderable58 renderable, final int x, final int y) {
    //LAB_80023c8c
    while(renderable != null) {
      boolean forceUnload = false;
      final UiPart[] entries = renderable.uiType_20.entries_08();

      if((renderable.flags_00 & 0x4) == 0) {
        renderable._08--;

        if(renderable._08 < 0) {
          if((renderable.flags_00 & 0x20) != 0) {
            renderable.glyph_04--;

            if(renderable.glyph_04 < renderable.startGlyph_10) {
              if((renderable.flags_00 & 0x10) != 0) {
                forceUnload = true;
                renderable.flags_00 |= 0x40;
              }

              //LAB_80023d0c
              if(renderable._18 != 0) {
                renderable.startGlyph_10 = renderable._18;

                if(renderable._1c != 0) {
                  renderable.endGlyph_14 = renderable._1c;
                } else {
                  //LAB_80023d34
                  renderable.endGlyph_14 = renderable._18;
                  renderable.flags_00 |= 0x4;
                }

                //LAB_80023d48
                renderable._18 = 0;
                renderable.flags_00 &= 0xffff_ffdf;
              }

              //LAB_80023d5c
              //LAB_80023e00
              renderable.glyph_04 = renderable.endGlyph_14;
              renderable._0c++;
            }
          } else {
            //LAB_80023d6c
            renderable.glyph_04++;

            if(renderable.endGlyph_14 < renderable.glyph_04) {
              if((renderable.flags_00 & 0x10) != 0) {
                forceUnload = true;
                renderable.flags_00 |= 0x40;
              }

              //LAB_80023da4
              if(renderable._18 != 0) {
                renderable.startGlyph_10 = renderable._18;

                if(renderable._1c != 0) {
                  renderable.endGlyph_14 = renderable._1c;
                } else {
                  //LAB_80023dcc
                  renderable.endGlyph_14 = renderable._18;
                  renderable.flags_00 |= 0x4;
                }

                //LAB_80023de0
                renderable._18 = 0;
                renderable.flags_00 &= 0xffff_ffdf;
              }

              //LAB_80023df4
              //LAB_80023e00
              renderable.glyph_04 = renderable.startGlyph_10;
              renderable._0c++;
            }
          }

          //LAB_80023e08
          renderable._08 = entries[renderable.glyph_04]._02() - 1;
        }
      }

      //LAB_80023e28
      if((renderable.flags_00 & 0x40) == 0) {
        final int centreX = displayWidth_1f8003e0.get() / 2 + 8;

        final RenderableMetrics14[] metricses = entries[renderable.glyph_04].metrics_00();

        //LAB_80023e94
        for(int i = metricses.length - 1; i >= 0; i--) {
          final RenderableMetrics14 metrics = metricses[i];

          final GpuCommandPoly cmd = new GpuCommandPoly(4)
            .monochrome(0x80);

          final int x1;
          final int x2;
          if(renderable._34 == 0x1000) {
            if(metrics._10() < 0) {
              x2 = renderable.x_40 + metrics.x_02() - centreX;
              x1 = x2 + metrics.width_08();
            } else {
              //LAB_80023f20
              x1 = renderable.x_40 + metrics.x_02() - centreX;
              x2 = x1 + metrics.width_08();
            }
          } else {
            //LAB_80023f40
            final int a0_0 = renderable._34 != 0 ? renderable._34 : metrics._10();

            //LAB_80023f4c
            //LAB_80023f68
            final int a1 = Math.abs(metrics.width_08() * a0_0 / 0x1000);
            if(metrics._10() < 0) {
              x2 = renderable.x_40 + metrics.width_08() / 2 + metrics.x_02() - centreX - a1 / 2;
              x1 = x2 + a1;
            } else {
              //LAB_80023fb4
              x1 = renderable.x_40 + metrics.width_08() / 2 + metrics.x_02() - centreX - a1 / 2;
              x2 = x1 + a1;
            }
          }

          //LAB_80023fe4
          final int y1;
          final int y2;
          if(renderable._38 == 0x1000) {
            if(metrics._12() < 0) {
              y2 = renderable.y_44 + metrics.y_03() - 120;
              y1 = y2 + metrics.height_0a();
            } else {
              //LAB_80024024
              y1 = renderable.y_44 + metrics.y_03() - 120;
              y2 = y1 + metrics.height_0a();
            }
          } else {
            //LAB_80024044
            final int a0_0 = renderable._38 != 0 ? renderable._38 : metrics._12();

            //LAB_80024050
            //LAB_8002406c
            final int a1 = Math.abs(metrics.height_0a() * a0_0 / 0x1000);
            if(metrics._12() < 0) {
              y2 = renderable.y_44 + metrics.height_0a() / 2 + metrics.y_03() - a1 / 2 - 120;
              y1 = y2 + a1;
            } else {
              //LAB_800240b8
              y1 = renderable.y_44 + metrics.height_0a() / 2 + metrics.y_03() - a1 / 2 - 120;
              y2 = y1 + a1;
            }
          }

          //LAB_800240e8
          cmd.pos(0, x1 + x, y1 + y + renderable.heightCut);
          cmd.pos(1, x2 + x, y1 + y + renderable.heightCut);
          cmd.pos(2, x1 + x, y2 + y);
          cmd.pos(3, x2 + x, y2 + y);

          //LAB_80024144
          //LAB_800241b4
          int v1 = metrics.u_00() + metrics.textureWidth();
          final int u = v1 < 255 ? v1 : v1 - 1;

          v1 = metrics.v_01() + metrics.textureHeight();
          final int v = v1 < 255 ? v1 : v1 - 1;

          cmd.uv(0, metrics.u_00(), metrics.v_01() + renderable.heightCut);
          cmd.uv(1, u, metrics.v_01() + renderable.heightCut);
          cmd.uv(2, metrics.u_00(), v);
          cmd.uv(3, u, v);

          final int clut = renderable.clut_30 != 0 ? renderable.clut_30 : metrics.clut_04() & 0x7fff;
          cmd.clut((clut & 0b111111) * 16, clut >>> 6);

          //LAB_80024214
          final int tpage = renderable.tpage_2c != 0 ? metrics.tpage_06() & 0x60 | renderable.tpage_2c : metrics.tpage_06() & 0x7f;
          cmd.vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0);
          cmd.bpp(Bpp.of(tpage >>> 7 & 0b11));

          if((metrics.clut_04() & 0x8000) != 0) {
            cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
          }

          //LAB_8002424c
          GPU.queueCommand(renderable.z_3c, cmd);
        }
      }

      //LAB_80024280
      if((renderable.flags_00 & 0x8) != 0 || forceUnload) {
        //LAB_800242a8
        unloadRenderable(renderable);
      }

      //LAB_800242b0
      renderable = renderable.parent_54;
    }

    //LAB_800242b8
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
    Renderable58 s0 = renderablePtr_800bdc5c;

    if(s0 != null) {
      //LAB_800243b4
      while(s0.parent_54 != null) {
        final Renderable58 a0_0 = s0;
        s0 = s0.parent_54;

        if(a0_0._28 <= a0) {
          unloadRenderable(a0_0);
        }

        //LAB_800243d0
      }

      //LAB_800243e0
      if(s0._28 <= a0) {
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
      default -> giveItem(script.params_20[0].get());
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
      script.params_20[1].set(takeEquipmentId(itemId));
    } else {
      script.params_20[1].set(takeItemId(itemId));
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
    final RECT[] rects = new RECT[28]; // image size, clut size, image size, clut size...

    for(int i = 0; i < 28; i++) {
      rects[i] = new RECT().set(rectArray28_80010770.get(i)); // Detach from the heap
    }

    rects[2].x.set((short)0);
    rects[2].y.set((short)0);
    rects[2].w.set((short)0x40);
    rects[2].h.set((short)0x10);
    rects[3].x.set((short)0);
    rects[3].y.set((short)0);
    rects[3].w.set((short)0);
    rects[3].h.set((short)0);

    final int[] indexOffsets = {0, 20, 22, 24, 26};

    //LAB_80024e88
    for(int i = 0; i < files.size(); i++) {
      final FileData data = files.get(i);

      if(data.size() != 0) {
        final Tim tim = new Tim(data);
        final int rectIndex = indexOffsets[i];

        if(i == 0 || i > 2) {
          GPU.uploadData(rects[rectIndex], tim.getImageData());
        }

        //LAB_80024efc
        if(i == 3) {
          //LAB_80024f2c
          GPU.uploadData(rects[indexOffsets[i] + 1], tim.getClutData());
        } else if(i < 4) {
          //LAB_80024fac
          for(int s0 = 0; s0 < 4; s0++) {
            final RECT rect = new RECT().set(rects[rectIndex + 1]);
            rect.x.set((short)(rect.x.get() + s0 * 16));
            GPU.uploadData(rect, tim.getClutData().slice(s0 * 0x80));
          }
          //LAB_80024f1c
        } else if(i == 4) {
          //LAB_80024f68
          GPU.uploadData(rects[rectIndex + 1], tim.getClutData());
        }
      }
    }
  }

  @Method(0x8002504cL)
  public static void loadBasicUiTexturesAndSomethingElse() {
    loadDrgnDir(0, 6669, Scus94491BpeSegment_8002::basicUiTexturesLoaded);

    textZ_800bdf00.set(13);
    _800bdf04.setu(0);
    _800bdf08.setu(0);
    clearCharacterStats();

    //LAB_800250c0
    //LAB_800250ec
    for(int i = 0; i < 8; i++) {
      textboxes_800be358[i] = new Textbox4c();
      textboxes_800be358[i].state_00 = 0;

      textboxText_800bdf38[i] = new TextboxText84();
      textboxText_800bdf38[i]._00 = 0;

      textboxArrows_800bdea0[i] = new TextboxArrow0c();

      setTextboxArrowPosition(i, 0);
    }

    //LAB_80025118
    for(int i = 0; i < 8; i++) {
      _800bdf18.offset(i * 0x4L).setu(0);
    }

    textU_800be5c0.setu(0);
    textV_800be5c8.setu(0);
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
    textboxText.flags_08 |= 0x1000;
    textboxText.str_24 = LodString.fromParam(script.params_20[2]);
    textboxText.chars_58 = new TextboxChar08[textboxText.chars_1c * (textboxText.lines_1e + 1)];
    Arrays.setAll(textboxText.chars_58, i -> new TextboxChar08());
    calculateAppropriateTextboxBounds(textboxIndex, textboxText.x_14, textboxText.y_16);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a textbox to a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "submapObjectIndex", description = "The submap object, but may also have the flag 0x1000 set (unknown meaning)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "packedData", description = "Unknown data, 3 nibbles")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The textbox width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The textbox height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.STRING, name = "text", description = "The textbox text")
  @Method(0x80025218L)
  public static FlowControl scriptAddSobjTextbox(final RunningScript<?> script) {
    if(script.params_20[2].get() == 0) {
      return FlowControl.CONTINUE;
    }

    final int textboxIndex = script.params_20[0].get();
    final int type = (int)_80052ba8.offset(((script.params_20[2].get() & 0xf00) >>> 8) * 0x2L).get();
    clearTextbox(textboxIndex);

    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    textbox._04 = (short)_80052b88.offset(((script.params_20[2].get() & 0xf0) >>> 4) * 0x2L).get();
    textbox._06 = (short)_80052b68.offset((script.params_20[2].get() & 0xf) * 0x2L).get();
    textbox.x_14 = 0;
    textbox.y_16 = 0;
    textbox.chars_18 = script.params_20[3].get() + 1;
    textbox.lines_1a = script.params_20[4].get() + 1;
    clearTextboxText(textboxIndex);

    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    textboxText.type_04 = type;
    textboxText.str_24 = LodString.fromParam(script.params_20[5]);

    if(type == 1 && (script.params_20[1].get() & 0x1000) > 0) {
      textboxText.flags_08 |= 0x20;
    }

    //LAB_80025370
    //LAB_80025374
    if(type == 3) {
      textboxText._6c = -1;
    }

    //LAB_800253a4
    if(type == 4) {
      textboxText.flags_08 |= 0x200;
    }

    //LAB_800253d4
    textboxText.flags_08 |= 0x1000;
    textboxText.chars_58 = new TextboxChar08[textboxText.chars_1c * (textboxText.lines_1e + 1)];
    Arrays.setAll(textboxText.chars_58, i -> new TextboxChar08());
    positionSobjTextbox(textboxIndex, script.params_20[1].get());

    if(type == 2) {
      textbox._38 = textbox.x_14;
      textbox._3c = textbox.y_16;
      textbox.x_14 = textbox._28;
      textbox.y_16 = textbox._2c;
      textbox._08 |= 0x2;
    }

    //LAB_80025494
    //LAB_80025498
    return FlowControl.CONTINUE;
  }

  /** Allocate textbox used in yellow-name textboxes and combat effect popups, maybe others */
  @ScriptDescription("Adds a textbox to a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "packedData", description = "Unknown data, 3 nibbles")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The textbox x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The textbox y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The textbox width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The textbox height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.STRING, name = "text", description = "The textbox text")
  @Method(0x800254bcL)
  public static FlowControl scriptAddTextbox(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();

    if(script.params_20[1].get() != 0) {
      final int a2 = script.params_20[1].get();
      final short s0 = (short)_80052b88.offset((a2 & 0xf0) >>> 3).get();
      final short s1 = (short)_80052b68.offset((a2 & 0xf) * 0x2L).get();
      final short type = (short)_80052ba8.offset((a2 & 0xf00) >>> 7).get();
      clearTextbox(textboxIndex);

      final Textbox4c textbox = textboxes_800be358[textboxIndex];
      final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

      textbox._04 = s0;
      textbox._06 = s1;
      textbox.x_14 = script.params_20[2].get();
      textbox.y_16 = script.params_20[3].get();
      textbox.chars_18 = script.params_20[4].get() + 1;
      textbox.lines_1a = script.params_20[5].get() + 1;
      textboxText.type_04 = type;
      textboxText.str_24 = LodString.fromParam(script.params_20[6]);

      // This is a stupid hack to allow inns to display 99,999,999 gold without the G falling down to the next line (see GH#546)
      if("?Funds ?G".equals(textboxText.str_24.get())) {
        textbox.chars_18++;
      }

      clearTextboxText(textboxIndex);

      if(type == 1 && (a2 & 0x1000) > 0) {
        textboxText.flags_08 |= 0x20;
      }

      //LAB_8002562c
      //LAB_80025630
      if(type == 3) {
        textboxText._6c = -1;
      }

      //LAB_80025660
      if(type == 4) {
        textboxText.flags_08 |= 0x200;
      }

      //LAB_80025690
      textboxText.flags_08 |= 0x1000;
      textboxText.chars_58 = new TextboxChar08[textboxText.chars_1c * (textboxText.lines_1e + 1)];
      Arrays.setAll(textboxText.chars_58, i -> new TextboxChar08());
      calculateAppropriateTextboxBounds(textboxIndex, textboxText.x_14, textboxText.y_16);
    }

    //LAB_800256f0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to textboxes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @Method(0x80025718L)
  public static FlowControl FUN_80025718(final RunningScript<?> script) {
    final TextboxText84 textboxText = textboxText_800bdf38[script.params_20[0].get()];

    textboxText._6c = -1;
    textboxText._70 = script.params_20[2].get();
    textboxText._72 = script.params_20[1].get();

    if(textboxText._00 == 13) {
      textboxText._00 = 23;
      textboxText._64 = 10;
      textboxText._78 = 22;
      Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
    }

    //LAB_800257bc
    textboxText.flags_08 |= 0x800;
    return FlowControl.CONTINUE;
  }

  /** Deallocate textbox used in yellow-name textboxes and combat effect popups, maybe others */
  @Method(0x800257e0L)
  public static void clearTextbox(final int textboxIndex) {
    if(textboxText_800bdf38[textboxIndex]._00 != 0) {
      textboxText_800bdf38[textboxIndex].chars_58 = null;
    }

    //LAB_80025824
    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    textbox.state_00 = 1;
    textbox._06 = 0;
    textbox._08 = 0;
    textbox.z_0c = 14;
    textbox._10 = 0;
    textbox.width_1c = 0;
    textbox.height_1e = 0;
    textbox._20 = 0x1000;
    textbox._22 = 0x1000;
    textbox._24 = 0;
    textbox._28 = 0;
    textbox._2c = 0;
    textbox._30 = 0;
    textbox._34 = 0;
    textbox._38 = 0;
    textbox._3c = 0;
    textbox._40 = 0;
    textbox._44 = 0;
    textbox._48 = 0;
  }

  @Method(0x800258a8L)
  public static void clearTextboxText(final int a0) {
    final TextboxText84 textboxText = textboxText_800bdf38[a0];
    textboxText._00 = 1;
    textboxText.flags_08 = 0;
    textboxText.z_0c = 13;
    textboxText._10 = 0;
    textboxText._20 = 0x1000;
    textboxText._22 = 0x1000;
    textboxText._28 = 0;
    textboxText._2a = 2;
    textboxText._2c = 0;
    textboxText._30 = 0;
    textboxText.charX_34 = 0;
    textboxText.charY_36 = 0;
    textboxText._38 = 0;
    textboxText._3a = 0;
    textboxText._3c = 0;
    textboxText._3e = 1;
    textboxText._40 = 0;
    textboxText._42 = 0;
    textboxText._44 = 0;

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
    textboxText._5c = 0;
    textboxText._60 = 0;
    textboxText._64 = 0;
    textboxText._68 = 0;
    textboxText._6c = 0;
    textboxText._70 = 0;
    textboxText._72 = 0;
    textboxText._74 = 0;
    textboxText._78 = 0;
    textboxText._7c = 0;
    textboxText._80 = 0;
  }

  @Method(0x80025a04L)
  public static void FUN_80025a04(final int textboxIndex) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    switch(textbox.state_00) {
      case 1 -> {
        if(textbox._04 == 0) {
          //LAB_80025ab8
          textbox.state_00 = 4;
          textbox._08 ^= 0x8000_0000;
          break;
          //LAB_80025aa8
        } else if(textbox._04 == 2) {
          //LAB_80025ad4
          textbox.state_00 = 2;
          textbox._08 |= 0x1;
          textbox._10 = 0;
          textbox._24 = 60 / vsyncMode_8007a3b8 / 4;

          if((textbox._08 & 0x2) != 0) {
            textbox._30 = (textbox._28 - textbox._38) / textbox._24;
            textbox._34 = (textbox._2c - textbox._3c) / textbox._24;
          }
          break;
        }

        //LAB_80025b54
        //LAB_80025b5c
        textbox.state_00 = 5;
        textbox.width_1c = textbox.chars_18 * 9 / 2;
        textbox.height_1e = textbox.lines_1a * 6;
        if((textbox._08 & 0x4) != 0) {
          textbox.state_00 = 6;
        }

        //LAB_80025bc0
        textbox._08 |= 0x8000_0000;
      }

      case 2 -> {
        textbox._08 |= 0x8000_0000;

        if(textbox._04 == 2) {
          textbox._20 = (textbox._10 << 12) / textbox._24;
          textbox._22 = (textbox._10 << 12) / textbox._24;
          textbox.width_1c = textbox.chars_18 * 9 / 2 * textbox._20 >> 12;
          textbox.height_1e = textbox._22 * 6 * textbox.lines_1a >> 12;
          textbox._10++;

          if((textbox._08 & 0x2) != 0) {
            textbox._28 -= textbox._30;
            textbox._2c -= textbox._34;
            textbox.x_14 = textbox._28;
            textbox.y_16 = textbox._2c;
          }

          //LAB_80025cf0
          if(textbox._10 >= textbox._24) {
            textbox.state_00 = 5;
            textbox._08 ^= 1;
            textbox.width_1c = textbox.chars_18 * 9 / 2;
            textbox.height_1e = textbox.lines_1a * 6;

            if((textbox._08 & 0x4) != 0) {
              textbox.state_00 = 6;
            }

            //LAB_80025d5c
            if((textbox._08 & 0x2) != 0) {
              textbox.x_14 = textbox._38;
              textbox.y_16 = textbox._3c;
            }
          }

          break;
        }

        //LAB_80025d84
        textbox.state_00 = 5;

        if((textbox._08 & 0x4) != 0) {
          textbox.state_00 = 6;
        }
      }

      case 3 -> {
        if(textbox._04 == 2) {
          textbox._20 = (textbox._10 << 12) / textbox._24;
          textbox._22 = (textbox._10 << 12) / textbox._24;
          textbox.width_1c = textbox.chars_18 * 9 / 2 * textbox._20 >> 12;
          textbox.height_1e = textbox._22 * 6 * textbox.lines_1a >> 12;
          textbox._10--;

          if(textbox._10 <= 0) {
            textbox.width_1c = 0;
            textbox.height_1e = 0;
            textbox.state_00 = 0;
            textbox._08 ^= 0x1;
          }
          break;
        }

        //LAB_80025e94
        textbox.state_00 = 0;
      }

      case 4, 5 -> {
        if(textboxText_800bdf38[textboxIndex]._00 == 0) {
          if(textbox._04 == 2) {
            textbox.state_00 = 3;
            textbox._08 |= 0x1;

            final int v0 = 60 / vsyncMode_8007a3b8 / 4;
            textbox._10 = v0;
            textbox._24 = v0;
          } else {
            //LAB_80025f30
            textbox.state_00 = 0;
          }

          //LAB_80025f34
          setTextboxArrowPosition(textboxIndex, 0);
        }
      }
    }

    //LAB_80025f3c
  }

  @Method(0x80025f4cL)
  public static void renderTextboxBackground(final int textboxIndex) {
    //LAB_80025f7c
    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    if(textbox._04 != 0) {
      if(textbox.state_00 != 1) {
        final int x = textbox.x_14 - centreScreenX_1f8003dc.get();
        final int y = textbox.y_16 - centreScreenY_1f8003de.get();

        if(Config.textBoxColour()) {
          if(Config.getTextBoxColourMode() == 0) {
            GPU.queueCommand(textbox.z_0c, new GpuCommandPoly(4)
              .translucent(Translucency.of(Config.getTextBoxTransparencyMode()))
              .rgb(0, Config.getTextBoxRgb(0))
              .pos(0, x - textbox.width_1c, y - textbox.height_1e)
              .rgb(1, Config.getTextBoxRgb(1))
              .pos(1, x + textbox.width_1c, y - textbox.height_1e)
              .rgb(2, Config.getTextBoxRgb(2))
              .pos(2, x - textbox.width_1c, y + textbox.height_1e)
              .rgb(3, Config.getTextBoxRgb(3))
              .pos(3, x + textbox.width_1c, y + textbox.height_1e)
            );
          } else if(Config.getTextBoxColourMode() == 1) {
            GPU.queueCommand(textbox.z_0c, new GpuCommandPoly(4)
              .translucent(Translucency.of(Config.getTextBoxTransparencyMode()))
              .rgb(0, Config.getTextBoxRgb(0))
              .pos(0, x - textbox.width_1c, y - textbox.height_1e)
              .rgb(1, Config.getTextBoxRgb(1))
              .pos(1, x, y - textbox.height_1e)
              .rgb(2, Config.getTextBoxRgb(2))
              .pos(2, x - textbox.width_1c, y + textbox.height_1e)
              .rgb(3, Config.getTextBoxRgb(3))
              .pos(3, x, y + textbox.height_1e)
            );
            GPU.queueCommand(textbox.z_0c, new GpuCommandPoly(4)
              .translucent(Translucency.of(Config.getTextBoxTransparencyMode()))
              .rgb(0, Config.getTextBoxRgb(4))
              .pos(0, x, y - textbox.height_1e)
              .rgb(1, Config.getTextBoxRgb(5))
              .pos(1, x + textbox.width_1c, y - textbox.height_1e)
              .rgb(2, Config.getTextBoxRgb(6))
              .pos(2, x, y + textbox.height_1e)
              .rgb(3, Config.getTextBoxRgb(7))
              .pos(3, x + textbox.width_1c, y + textbox.height_1e)
            );
          } else if(Config.getTextBoxColourMode() == 2) {
            GPU.queueCommand(textbox.z_0c, new GpuCommandPoly(4)
              .translucent(Translucency.of(Config.getTextBoxTransparencyMode()))
              .rgb(0, Config.getTextBoxRgb(0))
              .pos(0, x - textbox.width_1c, y - textbox.height_1e)
              .rgb(1, Config.getTextBoxRgb(1))
              .pos(1, x + textbox.width_1c, y - textbox.height_1e)
              .rgb(2, Config.getTextBoxRgb(2))
              .pos(2, x - textbox.width_1c, y)
              .rgb(3, Config.getTextBoxRgb(3))
              .pos(3, x + textbox.width_1c, y)
            );

            GPU.queueCommand(textbox.z_0c, new GpuCommandPoly(4)
              .translucent(Translucency.of(Config.getTextBoxTransparencyMode()))
              .rgb(0, Config.getTextBoxRgb(4))
              .pos(0, x - textbox.width_1c, y)
              .rgb(1, Config.getTextBoxRgb(5))
              .pos(1, x + textbox.width_1c, y)
              .rgb(2, Config.getTextBoxRgb(6))
              .pos(2, x - textbox.width_1c, y + textbox.height_1e)
              .rgb(3, Config.getTextBoxRgb(7))
              .pos(3, x + textbox.width_1c, y + textbox.height_1e)
            );
          }
        } else {
          GPU.queueCommand(textbox.z_0c, new GpuCommandPoly(4)
            .translucent(Translucency.HALF_B_PLUS_HALF_F)
            .monochrome(0, 0)
            .pos(0, x - textbox.width_1c, y - textbox.height_1e)
            .rgb(1, (int)_80010868.offset(0x0L).get(), (int)_80010868.offset(0x4L).get(), (int)_80010868.offset(0x8L).get())
            .pos(1, x + textbox.width_1c, y - textbox.height_1e)
            .rgb(2, (int)_80010868.offset(0x0L).get(), (int)_80010868.offset(0x4L).get(), (int)_80010868.offset(0x8L).get())
            .pos(2, x - textbox.width_1c, y + textbox.height_1e)
            .monochrome(3, 0)
            .pos(3, x + textbox.width_1c, y + textbox.height_1e)
          );
        }

        if(textbox._06 != 0) {
          renderTextboxBorder(textboxIndex, x - textbox.width_1c, y - textbox.height_1e, x + textbox.width_1c, y + textbox.height_1e);
        }
      }
    }

    //LAB_800261a0
  }

  @Method(0x800261c0L)
  public static void renderTextboxBorder(final int textboxIndex, final int boxLeft, final int boxTop, final int boxRight, final int boxBottom) {
    final short[] sp0x10 = new short[4];
    final short[] sp0x18 = new short[4];
    short v0 = (short)(boxLeft + 4);
    sp0x10[0] = v0;
    sp0x10[2] = v0;
    v0 = (short)(boxRight - 4);
    sp0x10[1] = v0;
    sp0x10[3] = v0;
    v0 = (short)(boxTop + 5);
    sp0x18[0] = v0;
    sp0x18[1] = v0;
    v0 = (short)(boxBottom - 5);
    sp0x18[2] = v0;
    sp0x18[3] = v0;

    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    //LAB_800262e4
    for(int s3 = 0; s3 < 8; s3++) {
      short a0 = (short)_800108b0.offset(s3 * 0xcL).offset(2, 0x8L).getSigned();
      short a1 = (short)_800108b0.offset(s3 * 0xcL).offset(2, 0xaL).getSigned();
      if((textbox._08 & 0x1) != 0) {
        a0 = (short)(textbox._20 * a0 >> 12);
        a1 = (short)(textbox._22 * a1 >> 12);
      }

      //LAB_8002637c
      final int u = (int)_800108b0.offset(s3 * 0xcL).offset(1, 0x4L).get();
      final int v = (int)_800108b0.offset(s3 * 0xcL).offset(1, 0x6L).get();
      final int left = sp0x10[(int)_800108b0.offset(s3 * 0xcL).offset(2, 0x0L).get()] - a0;
      final int right = sp0x10[(int)_800108b0.offset(s3 * 0xcL).offset(2, 0x2L).get()] + a0;
      final int top = sp0x18[(int)_800108b0.offset(s3 * 0xcL).offset(2, 0x0L).get()] - a1;
      final int bottom = sp0x18[(int)_800108b0.offset(s3 * 0xcL).offset(2, 0x2L).get()] + a1;

      GPU.queueCommand(textbox.z_0c, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .monochrome(0x80)
        .clut(832, 484)
        .vramPos(896, 256)
        .pos(0, left, top)
        .uv(0, u, v)
        .pos(1, right, top)
        .uv(1, u + 16, v)
        .pos(2, left, bottom)
        .uv(2, u, v + 16)
        .pos(3, right, bottom)
        .uv(3, u + 16, v + 16)
      );
    }
  }

  /** I think this method handles textboxes */
  @Method(0x800264b0L)
  public static void FUN_800264b0(final int textboxIndex) {
    long v0;
    long s1;
    long s3;

    final Textbox4c struct4c = textboxes_800be358[textboxIndex];
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    long v1 = textboxText._00;
    if(v1 == 1) {
      //LAB_8002663c
      if((textboxText.flags_08 & 0x1) == 0) {
        switch(textboxText.type_04) {
          case 0:
            textboxText._00 = 0xc;
            break;

          case 2:
            textboxText._00 = 10;
            textboxText.flags_08 |= 0x1;
            textboxText._2a = 1;
            textboxText.charX_34 = 0;
            textboxText.charY_36 = textboxText.lines_1e;
            break;

          case 3:
            textboxText._00 = 23;
            textboxText.flags_08 |= 0x1;
            textboxText._2a = 1;
            textboxText.charX_34 = 0;
            textboxText.charY_36 = 0;
            textboxText._64 = 10;
            textboxText._78 = 17;
            Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
            break;

          case 4:
            //LAB_80026780
            do {
              FUN_800274f0(textboxIndex);
            } while((textboxText.flags_08 & 0x400) == 0);

            textboxText.flags_08 ^= 0x400;
            // Fall through

          default:
            //LAB_800267a0
            textboxText._00 = 4;
            break;
        }
      }
    } else if(v1 == 2) {
      textboxText._00 = 4;
      //LAB_80026538
    } else if(v1 == 4) {
      //LAB_800267c4
      FUN_800274f0(textboxIndex);
    } else if(v1 == 5) {
      //LAB_800267d4
      if((textboxText.flags_08 & 0x1) != 0) {
        //LAB_800267f4
        if(textboxText._3a >= textboxText.lines_1e - ((textboxText.flags_08 & 0x200) == 0 ? 1 : 2)) {
          textboxText.flags_08 ^= 0x1;
          textboxText._3a = 0;
          setTextboxArrowPosition(textboxIndex, 1);
        } else {
          //LAB_80026828
          textboxText._00 = 9;
          textboxText._3a++;
          FUN_80028828(textboxIndex);
        }
      } else {
        //LAB_8002684c
        if((textboxText.flags_08 & 0x20) != 0) {
          textboxText._00 = 9;
          textboxText.flags_08 |= 0x1;
        } else {
          //LAB_8002686c
          if((press_800bee94.get() & 0x20) != 0 || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
            setTextboxArrowPosition(textboxIndex, 0);

            v1 = textboxText.type_04;
            if(v1 == 1 || v1 == 4) {
              //LAB_800268b4
              textboxText._00 = 9;
              textboxText.flags_08 |= 0x1;
            }

            if(v1 == 2) {
              //LAB_800268d0
              textboxText._00 = 10;
            }
          }
        }
      }
    } else if(v1 == 6) {
      //LAB_800268dc
      if((press_800bee94.get() & 0x20) != 0 || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
        textboxText._00 = 4;
      }
    } else if(v1 == 7) {
      //LAB_800268fc
      textboxText._40++;
      if(textboxText._40 >= textboxText._3e) {
        textboxText._40 = 0;
        textboxText._00 = 4;
      }

      //LAB_80026928
      if((textboxText.flags_08 & 0x20) == 0) {
        if((input_800bee90.get() & 0x20) != 0 || CONFIG.getConfig(CoreMod.QUICK_TEXT_CONFIG.get())) {
          s3 = 0;

          //LAB_80026954
          for(s1 = 0; s1 < 4; s1++) {
            FUN_800274f0(textboxIndex);

            v1 = textboxText._00;
            if(v1 < 7 || v1 == 15 || v1 == 11 || v1 == 13) {
              //LAB_8002698c
              s3 = 0x1L;
              break;
            }

            //LAB_80026994
          }

          //LAB_800269a0
          if(s3 == 0) {
            textboxText._40 = 0;
            textboxText._00 = 4;
          }
        }
      }
    } else if(v1 == 8) {
      //LAB_800269cc
      if(textboxText._44 > 0) {
        //LAB_800269e8
        textboxText._44--;
      } else {
        //LAB_800269e0
        textboxText._00 = 4;
      }
      //LAB_80026554
    } else if(v1 == 9) {
      //LAB_800269f0
      FUN_80028828(textboxIndex);
      //LAB_80026580
    } else if(v1 == 10) {
      //LAB_80026a00
      FUN_800288a4(textboxIndex);

      if((textboxText.flags_08 & 0x4) != 0) {
        textboxText.flags_08 ^= 0x4;
        if((textboxText.flags_08 & 0x2) == 0) {
          //LAB_80026a5c
          do {
            FUN_800274f0(textboxIndex);
            v1 = textboxText._00;
            if(v1 == 0xf) {
              textboxText._3a = 0;
              textboxText.flags_08 |= 0x2;
            }
          } while(v1 != 0x5 && v1 != 0xf);

          //LAB_80026a8c
          textboxText._00 = 10;
        } else {
          textboxText._3a++;

          if(textboxText._3a >= textboxText.lines_1e + 1) {
            textboxText.chars_58 = null;
            textboxText._00 = 0;
          }
        }
      }
    } else if(v1 == 11) {
      //LAB_80026a98
      if((press_800bee94.get() & 0x20) != 0 || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
        setTextboxArrowPosition(textboxIndex, 0);
        clearTextboxChars(textboxIndex);

        textboxText._00 = 4;
        textboxText.flags_08 ^= 0x1;
        textboxText.charX_34 = 0;
        textboxText.charY_36 = 0;
        textboxText._3a = 0;

        if((textboxText.flags_08 & 0x8) != 0) {
          textboxText._00 = 13;
        }
      }
    } else if(v1 == 12) {
      //LAB_80026af0
      if(struct4c.state_00 == 0) {
        textboxText.chars_58 = null;
        textboxText._00 = 0;
      }
    } else if(v1 == 13) {
      //LAB_80026b34
      textboxText.flags_08 |= 0x8;
      setTextboxArrowPosition(textboxIndex, 1);

      //LAB_80026b4c
      do {
        FUN_800274f0(textboxIndex);
        v1 = textboxText._00;
        if(v1 == 0x5) {
          //LAB_80026b28
          textboxText._00 = 11;
          break;
        }
      } while(v1 != 0xf);

      //LAB_80026b6c
      if((textboxText.flags_08 & 0x20) != 0) {
        setTextboxArrowPosition(textboxIndex, 0);
      }

      //LAB_80026ba0
      if(textboxText._3e != 0) {
        setTextboxArrowPosition(textboxIndex, 0);
        textboxText._5c = textboxText._00;
        textboxText._00 = 14;
      }

      //LAB_80026bc8
      if((textboxText.flags_08 & 0x800) != 0) {
        setTextboxArrowPosition(textboxIndex, 0);
        textboxText._00 = 23;
        textboxText._64 = 10;
        textboxText._78 = 22;
        textboxText._68 = textboxText._72;
        Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
      }
    } else if(v1 == 14) {
      //LAB_80026c18
      if((textboxText.flags_08 & 0x40) == 0) {
        textboxText._40--;

        if(textboxText._40 <= 0) {
          textboxText._40 = textboxText._3e;

          v1 = textboxText._5c;
          if(v1 == 11) {
            //LAB_80026c70
            clearTextboxChars(textboxIndex);
            textboxText.charX_34 = 0;
            textboxText.charY_36 = 0;
            textboxText._3a = 0;
            textboxText._00 = 13;
            textboxText.flags_08 ^= 0x1;
          } else if(v1 == 15) {
            //LAB_80026c98
            //LAB_80026c9c
            textboxText.chars_58 = null;
            textboxText._00 = 0;
          }
        }
      }
    } else if(v1 == 15) {
      //LAB_80026cb0
      if((textboxText.flags_08 & 0x20) != 0) {
        textboxText._00 = 16;
      } else {
        //LAB_80026cd0
        if((press_800bee94.get() & 0x20) != 0 || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
          textboxText.chars_58 = null;
          textboxText._00 = 0;
          setTextboxArrowPosition(textboxIndex, 0);
        }
      }
      //LAB_800265d8
    } else if(v1 == 16) {
      //LAB_80026cdc
      //LAB_80026ce8
      if((textboxText.flags_08 & 0x40) != 0) {
        textboxText.chars_58 = null;
        textboxText._00 = 0;
        setTextboxArrowPosition(textboxIndex, 0);
      }
    } else if(v1 == 17) {
      //LAB_80026d20
      textboxText.lines_1e++;

      //LAB_80026d30
      do {
        FUN_800274f0(textboxIndex);
        v1 = textboxText._00;
        if(v1 == 5) {
          //LAB_80026d14
          textboxText._00 = 18;
          break;
        }
        if(v1 == 0xfL) {
          textboxText._00 = 18;
          textboxText._3a = 0;
          textboxText.flags_08 |= 0x102;
          break;
        }
      } while(true);

      //LAB_80026d64
      textboxText._6c = -1;
      textboxText.lines_1e--;
      //LAB_8002659c
    } else if(v1 == 18) {
      //LAB_80026d94
      renderTextboxSelection(textboxIndex, (short)textboxText._60);

      if((press_800bee94.get() & 0x20) != 0) {
        Scus94491BpeSegment.playSound(0, 2, 0, 0, (short)0, (short)0);
        textboxText.chars_58 = null;
        textboxText._00 = 0;
        textboxText._6c = textboxText._68;
      } else {
        //LAB_80026df0
        if((input_800bee90.get() & 0x4000) == 0) {
          //LAB_80026ee8
          if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
            if((textboxText.flags_08 & 0x100) == 0 || textboxText._68 != 0) {
              //LAB_80026f38
              Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

              s3 = 0x3L;
              if(textboxText._60 > 0) {
                textboxText._00 = 19;
                textboxText._60--;
                textboxText._64 = 4;
                textboxText._68--;
              } else {
                //LAB_80026f88
                if((textboxText.flags_08 & 0x2) != 0) {
                  v1 = textboxText._3a;

                  // TODO not sure about this block of code
                  if(v1 == 1) {
                    s3 = 0x1L;
                  } else {
                    if(v1 == 0) {
                      //LAB_80026fbc
                      s3 = 0x2L;
                    }

                    //LAB_80026fc0
                    textboxText._3a = 0;
                    textboxText.flags_08 ^= 0x2;
                  }

                  //LAB_80026fe8
                  textboxText._3a--;
                }

                //LAB_80027014
                textboxText._68--;

                if(textboxText._68 < 0) {
                  textboxText._68 = 0;
                } else {
                  //LAB_80027044
                  textboxText._2c = 12;
                  FUN_800280d4(textboxIndex);

                  final LodString str = textboxText.str_24;

                  //LAB_80027068
                  s1 = 0;
                  do {
                    if(str.charAt(textboxText._30 - 1) >>> 8 == 0xa1) {
                      s1++;
                    }

                    //LAB_80027090
                    if(s1 == textboxText.lines_1e + s3) {
                      break;
                    }

                    textboxText._30--;
                  } while(textboxText._30 > 0);

                  //LAB_800270b0
                  textboxText.charX_34 = 0;
                  textboxText.charY_36 = 0;
                  textboxText.flags_08 |= 0x80;

                  //LAB_800270dc
                  do {
                    FUN_800274f0(textboxIndex);
                  } while(textboxText.charY_36 == 0 && textboxText._00 != 5);

                  //LAB_80027104
                  textboxText._00 = 21;
                  textboxText.flags_08 ^= 0x80;
                }
              }
            }
          }
        }

        textboxText._00 = 19;
        textboxText._60++;
        textboxText._64 = 4;
        textboxText._68++;
        if((textboxText.flags_08 & 0x100) == 0 || textboxText.charY_36 + 1 != textboxText._68) {
          //LAB_80026e68
          //LAB_80026e6c
          if(textboxText._60 < textboxText.lines_1e) {
            //LAB_80026ed0
            Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

            //LAB_80026ee8
            if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
              if((textboxText.flags_08 & 0x100) == 0 || textboxText._68 != 0) {
                //LAB_80026f38
                Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

                s3 = 0x3L;
                if(textboxText._60 > 0) {
                  textboxText._00 = 19;
                  textboxText._60--;
                  textboxText._64 = 4;
                  textboxText._68--;
                } else {
                  //LAB_80026f88
                  if((textboxText.flags_08 & 0x2) != 0) {
                    v1 = textboxText._3a;

                    // TODO not sure about this block of code
                    if(v1 == 1) {
                      s3 = 0x1L;
                    } else {
                      if(v1 == 0) {
                        //LAB_80026fbc
                        s3 = 0x2L;
                      }

                      //LAB_80026fc0
                      textboxText._3a = 0;
                      textboxText.flags_08 ^= 0x2;
                    }

                    //LAB_80026fe8
                    textboxText._3a--;
                  }

                  //LAB_80027014
                  textboxText._68--;

                  if(textboxText._68 < 0) {
                    textboxText._68 = 0;
                  } else {
                    //LAB_80027044
                    textboxText._2c = 12;
                    FUN_800280d4(textboxIndex);

                    final LodString str = textboxText.str_24;

                    //LAB_80027068
                    s1 = 0;
                    do {
                      if(str.charAt(textboxText._30 - 1) >>> 8 == 0xa1) {
                        s1++;
                      }

                      //LAB_80027090
                      if(s1 == textboxText.lines_1e + s3) {
                        break;
                      }

                      textboxText._30--;
                    } while(textboxText._30 > 0);

                    //LAB_800270b0
                    textboxText.charX_34 = 0;
                    textboxText.charY_36 = 0;
                    textboxText.flags_08 |= 0x80;

                    //LAB_800270dc
                    do {
                      FUN_800274f0(textboxIndex);
                    } while(textboxText.charY_36 == 0 && textboxText._00 != 5);

                    //LAB_80027104
                    textboxText._00 = 21;
                    textboxText.flags_08 ^= 0x80;
                  }
                }
              }
            }
          } else {
            textboxText._60 = textboxText_800bdf38[textboxIndex].lines_1e - 1;
            textboxText._00 = 0x14;
            textboxText._2c = (short)0;

            if(textboxText._3a == 1) {
              textboxText._00 = 18;
              textboxText._68--;
            }
          }
        } else {
          textboxText._00 = 3;
          textboxText._60--;
          textboxText._68--;
        }
      }
    } else if(v1 == 19) {
      //LAB_8002711c
      renderTextboxSelection(textboxIndex, (short)textboxText._68);
      textboxText._64--;

      if(textboxText._64 == 0) {
        textboxText._00 = 18;

        if((textboxText.flags_08 & 0x800) != 0) {
          textboxText._00 = 22;
        }
      }
    } else if(v1 == 20) {
      //LAB_8002715c
      textboxText._2c += 4;

      if(textboxText._2c >= 12) {
        FUN_80027eb4(textboxIndex);
        textboxText.flags_08 |= 0x4;
        textboxText._2c -= 12;
        textboxText.charY_36 = textboxText.lines_1e;
      }

      //LAB_800271a8
      if((textboxText.flags_08 & 0x4) != 0) {
        textboxText.flags_08 ^= 0x4;

        if((textboxText.flags_08 & 0x2) == 0) {
          //LAB_8002720c
          //LAB_80027220
          do {
            FUN_800274f0(textboxIndex);

            v1 = textboxText._00;
            if(v1 == 0xf) {
              textboxText._3a = 0;
              textboxText.flags_08 |= 0x2;
              break;
            }
          } while(v1 != 5);
        } else {
          textboxText._3a++;
          if(textboxText._3a >= textboxText.lines_1e + 1) {
            textboxText.chars_58 = null;
            textboxText._00 = 0;
          }
        }

        //LAB_80027250
        //LAB_80027254
        textboxText._00 = 18;
      }
      //LAB_800265f4
    } else if(v1 == 21) {
      //LAB_8002727c
      textboxText._2c -= 4;

      if(textboxText._2c <= 0) {
        textboxText.charY_36 = 0;
        textboxText._2c = 0;
        textboxText._00 = 18;
        textboxText.flags_08 |= 0x4;
      }

      //LAB_800272b0
      if((textboxText.flags_08 & 0x4) != 0) {
        final LodString str = textboxText.str_24;

        //LAB_800272dc
        s1 = 0;
        do {
          v0 = str.charAt(textboxText._30 + 1) >>> 8;
          if(v0 == 0xa0L) {
            //LAB_80027274
            textboxText._30--;
            break;
          }

          if(v0 == 0xa1) {
            s1++;
          }

          //LAB_8002730c
          textboxText._30++;
        } while(s1 != textboxText.lines_1e);

        //LAB_80027320
        textboxText._00 = 18;
        textboxText._30 += 2;
        textboxText.charX_34 = 0;
        textboxText.charY_36 = textboxText.lines_1e;
      }
    } else if(v1 == 0x16) {
      //LAB_80027354
      renderTextboxSelection(textboxIndex, (short)textboxText._68);

      if((press_800bee94.get() & 0x20) != 0) {
        Scus94491BpeSegment.playSound(0, 2, 0, 0, (short)0, (short)0);
        textboxText.chars_58 = null;
        textboxText._00 = 0;
        textboxText._6c = textboxText._68 - textboxText._72;
      } else {
        //LAB_800273bc
        if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
          textboxText._00 = 19;
          textboxText._64 = 4;
          textboxText._68--;

          if(textboxText._68 < textboxText._72) {
            textboxText._68 = textboxText._72;
            textboxText._00 = 22;
          } else {
            //LAB_80027404
            Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);
          }
        }

        //LAB_80027420
        if(Input.getButtonState(InputAction.DPAD_DOWN) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
          textboxText._00 = 19;
          textboxText._64 = 4;
          textboxText._68++;

          if(textboxText._70 >= textboxText._68) {
            //LAB_80027480
            //LAB_80027490
            Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);
          } else {
            textboxText._68 = textboxText._70;
            textboxText._00 = 22;
          }
        }
      }
      //LAB_80026620
    } else if(v1 == 23) {
      //LAB_800274a4
      textboxText._64--;
      if(textboxText._64 == 0) {
        textboxText._64 = 4;
        textboxText._00 = textboxText._78;
      }
    }

    //LAB_800274c8
    updateTextboxArrowSprite(textboxIndex);
  }

  @Method(0x800274f0L)
  public static void FUN_800274f0(final int textboxIndex) {
    int v1;

    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    final LodString str = textboxText.str_24;

    if((textboxText.flags_08 & 0x10) != 0) {
      final int s1 = (short)textboxText._80;
      FUN_8002a180(textboxIndex, textboxText.charX_34, textboxText.charY_36, textboxText._28, textboxText.digits_46[s1]);

      textboxText.charX_34++;
      textboxText._3c++;
      textboxText._80++;

      if(textboxText.charX_34 < textboxText.chars_1c) {
        //LAB_80027768
        if(textboxText.digits_46[s1 + 1] == -1) {
          textboxText.flags_08 ^= 0x10;
        }
      } else if(textboxText.charY_36 >= textboxText.lines_1e - 1) {
        if(textboxText.digits_46[s1 + 1] != -1) {
          textboxText._00 = 5;
          textboxText.charX_34 = 0;
          textboxText.charY_36++;
          setTextboxArrowPosition(textboxIndex, 1);
          return;
        }

        //LAB_80027618
        v1 = str.charAt(textboxText._30) >>> 8;

        if(v1 == 0xa0) {
          //LAB_800276f4
          textboxText._00 = 15;

          //LAB_80027704
          setTextboxArrowPosition(textboxIndex, 1);

          //LAB_80027740
          textboxText_800bdf38[textboxIndex].flags_08 ^= 0x10;
          return;
        }

        if(v1 == 0xa1) {
          textboxText._30++;
        }

        //LAB_8002764c
        textboxText._00 = 5;
        textboxText.charX_34 = 0;
        textboxText.charY_36++;

        //LAB_80027704
        setTextboxArrowPosition(textboxIndex, 1);
      } else {
        //LAB_80027688
        textboxText.charX_34 = 0;
        textboxText.charY_36++;

        if(textboxText.digits_46[s1 + 1] == -1) {
          v1 = str.charAt(textboxText._30) >>> 8;
          if(v1 == 0xa0) {
            //LAB_800276f4
            textboxText._00 = 15;

            //LAB_80027704
            setTextboxArrowPosition(textboxIndex, 1);
          } else {
            if(v1 == 0xa1) {
              //LAB_80027714
              textboxText._30++;
            }

            //LAB_80027724
            textboxText_800bdf38[textboxIndex]._00 = 7;
          }

          //LAB_80027740
          textboxText_800bdf38[textboxIndex].flags_08 ^= 0x10;
          return;
        }
      }

      //LAB_8002779c
      textboxText_800bdf38[textboxIndex]._00 = 7;
      return;
    }

    //LAB_800277bc
    int s1 = 1;

    //LAB_800277cc
    do {
      final int a0_0 = str.charAt(textboxText._30);

      switch(a0_0 >>> 8) {
        case 0xa0 -> {
          textboxText._00 = 15;
          setTextboxArrowPosition(textboxIndex, 1);
          s1 = 0;
        }

        case 0xa1 -> {
          textboxText.charX_34 = 0;
          textboxText.charY_36++;
          textboxText.flags_08 |= 0x400;

          if(textboxText.charY_36 >= textboxText.lines_1e || (textboxText.flags_08 & 0x80) != 0) {
            //LAB_80027880
            textboxText._00 = 5;

            if((textboxText.flags_08 & 0x1) == 0) {
              setTextboxArrowPosition(textboxIndex, 1);
            }

            s1 = 0;
          }
        }

        case 0xa2 -> {
          //LAB_80027d28
          textboxText._00 = 6;

          //LAB_80027d2c
          s1 = 0;
        }

        case 0xa3 -> {
          setTextboxArrowPosition(textboxIndex, 1);
          textboxText._00 = 11;

          if(str.charAt(textboxText._30 + 1) >>> 8 == 0xa1) {
            textboxText._30++;
          }

          s1 = 0;
        }

        case 0xa5 -> {
          textboxText._3e = a0_0 & 0xff;
          textboxText._40 = 0;
        }

        case 0xa6 -> {
          textboxText._00 = 8;
          textboxText._44 = 60 / vsyncMode_8007a3b8 * (a0_0 & 0xff);
          s1 = 0;
        }

        case 0xa7 -> {
          final int a2 = a0_0 & 0xf;

          //LAB_80027950
          textboxText._28 = a2 < 12 ? a2 : 0;
        }

        case 0xa8 -> {
          textboxText.flags_08 |= 0x10;

          //LAB_80027970
          Arrays.fill(textboxText.digits_46, -1);

          int a1 = (int)_800bdf10.offset((a0_0 & 0xff) * 0x4L).get();
          int a3 = 1_000_000_000;
          final long[] sp0x18 = new long[10]; //TODO LodString
          //LAB_800279dc
          for(int i = 0; i < sp0x18.length; i++) {
            sp0x18[i] = _80052b40.get(a1 / a3).deref().charAt(0);
            a1 = a1 % a3;
            a3 = a3 / 10;
          }

          //LAB_80027a34
          v1 = _80052b40.get(0).deref().charAt(0);

          //LAB_80027a54
          for(s1 = 0; s1 < 9; s1++) {
            if(sp0x18[s1] != v1) {
              break;
            }
          }

          //LAB_80027a84
          //LAB_80027a90
          for(int i = 0; i < textboxText.digits_46.length && s1 < sp0x18.length; i++, s1++) {
            textboxText.digits_46[i] = (int)sp0x18[s1];
          }

          //LAB_80027ae4
          textboxText._80 = 0;

          //LAB_80027d2c
          s1 = 0;
        }

        case 0xad -> {
          final int v1_0 = a0_0 & 0xff;

          if(v1_0 >= textboxText.chars_1c) {
            textboxText.charX_34 = textboxText.chars_1c - 1;
          } else {
            //LAB_80027b0c
            textboxText.charX_34 = v1_0;
          }
        }

        case 0xae ->
          //LAB_80027b38
          textboxText.charY_36 = Math.min(a0_0 & 0xff, textboxText.lines_1e - 1);

        case 0xb0 -> {
          textboxText._00 = 13;

          final int v0 = 60 / vsyncMode_8007a3b8 * (a0_0 & 0xff);
          textboxText._3e = v0;
          textboxText._40 = v0;

          if(str.charAt(textboxText._30 + 1) >>> 8 == 0xa1L) {
            textboxText._30++;
          }

          s1 = 0;
        }

        case 0xb1 -> textboxText._7c = a0_0 & 0xff;

        case 0xb2 -> {
          if((a0_0 & 0x1L) == 0x1L) {
            textboxText.flags_08 |= 0x1000;
          } else {
            //LAB_80027bd0
            textboxText.flags_08 ^= 0x1000;
          }
        }

        default -> {
          //LAB_80027be4
          FUN_8002a180(textboxIndex, textboxText.charX_34, textboxText.charY_36, textboxText._28, (short)a0_0);

          textboxText.charX_34++;
          textboxText._3c++;

          if(textboxText.charX_34 < textboxText.chars_1c) {
            //LAB_80027d28
            textboxText._00 = 7;
          } else if(textboxText.charY_36 >= textboxText.lines_1e - 1) {
            v1 = str.charAt(textboxText._30 + 1) >>> 8;

            if(v1 == 0xa0) {
              //LAB_80027c7c
              textboxText._00 = 15;
              setTextboxArrowPosition(textboxIndex, 1);
            } else {
              if(v1 == 0xa1) {
                //LAB_80027c98
                textboxText._30++;
              }

              //LAB_80027c9c
              textboxText._00 = 5;
              textboxText.flags_08 |= 0x400;
              textboxText.charX_34 = 0;
              textboxText.charY_36++;

              if((textboxText.flags_08 & 0x1) == 0) {
                setTextboxArrowPosition(textboxIndex, 1);
              }
            }
          } else {
            //LAB_80027ce0
            textboxText.flags_08 |= 0x400;
            textboxText.charX_34 = 0;
            textboxText.charY_36++;

            if(str.charAt(textboxText._30 + 1) >>> 8 == 0xa1) {
              textboxText._30++;
            }

            //LAB_80027d28
            textboxText._00 = 7;
          }

          //LAB_80027d2c
          s1 = 0;
        }
      }

      //LAB_80027d30
      textboxText._30++;
    } while(s1 != 0);

    //LAB_80027d44
  }

  @Method(0x80027d74L)
  public static void calculateAppropriateTextboxBounds(final int textboxIndex, final int x, final int y) {
    final int maxX;
    if(engineState_8004dd20 == EngineState.SUBMAP_05) {
      maxX = 350;
    } else {
      maxX = 310;
    }

    //LAB_80027d9c
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    final int width = textboxText.chars_1c * 9 / 2;
    final int height = textboxText.lines_1e * 6;
    final int v1 = x - width;
    final int t4 = y - height;

    int t2 = x;
    if((short)v1 < 10) {
      t2 = width + 10;
    }

    //LAB_80027dfc
    if(x + width > maxX) {
      t2 = maxX - width;
    }

    //LAB_80027e14
    int t3 = y;
    if((short)t4 < 18) {
      t3 = height + 18;
    }

    //LAB_80027e28
    if(222 < y + height) {
      t3 = 222 - height;
    }

    //LAB_80027e40
    final Textbox4c v0 = textboxes_800be358[textboxIndex];
    v0.x_14 = t2;
    textboxText.x_14 = t2;
    v0.y_16 = t3;
    textboxText.y_16 = t3;
    textboxText._18 = t2 - width;
    textboxText._1a = t3 - height;
  }

  @Method(0x80027eb4L)
  public static void FUN_80027eb4(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    //LAB_80027efc
    for(int i = (textboxText.flags_08 & 0x200) > 0 ? 1 : 0; i < textboxText.lines_1e; i++) {
      //LAB_80027f18
      for(int a1 = 0; a1 < textboxText.chars_1c; a1++) {
        final TextboxChar08 v0 = textboxText.chars_58[(i + 1) * textboxText.chars_1c + a1];
        final TextboxChar08 v1 = textboxText.chars_58[ i      * textboxText.chars_1c + a1];
        v1.x_00 = v0.x_00;
        v1.y_02 = v0.y_02 - 1;
        v1._04 = v0._04;
        v1.char_06 = v0.char_06;
      }
    }

    //LAB_8002804c
    //LAB_80028098
    for(int i = textboxText.chars_1c * textboxText.lines_1e; i < textboxText.chars_1c * (textboxText.lines_1e + 1); i++) {
      final TextboxChar08 chr = textboxText.chars_58[i];
      chr.x_00 = 0;
      chr.y_02 = 0;
      chr._04 = 0;
      chr.char_06 = 0;
    }

    //LAB_800280cc
  }

  @Method(0x800280d4L)
  public static void FUN_800280d4(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    //LAB_8002810c
    for(int lineIndex = textboxText.lines_1e; lineIndex > 0; lineIndex--) {
      //LAB_80028128
      for(int charIndex = 0; charIndex < textboxText.chars_1c; charIndex++) {
        final TextboxChar08 v0 = textboxText.chars_58[(lineIndex - 1) * textboxText.chars_1c + charIndex];
        final TextboxChar08 v1 = textboxText.chars_58[lineIndex * textboxText.chars_1c + charIndex];
        v1.x_00 = v0.x_00;
        v1.y_02 = v0.y_02 + 1;
        v1._04 = v0._04;
        v1.char_06 = v0.char_06;
      }
    }

    //LAB_80028254
    //LAB_80028280
    for(int charIndex = 0; charIndex < textboxText.chars_1c; charIndex++) {
      final TextboxChar08 chr = textboxText.chars_58[charIndex];
      chr.x_00 = 0;
      chr.y_02 = 0;
      chr._04 = 0;
      chr.char_06 = 0;
    }

    //LAB_800282a4
  }

  @Method(0x800282acL)
  public static void renderTextboxText(final int textboxIndex) {
    int s1;
    int s2;
    int s3;

    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    final int sp10;
    final int sp14;
    if((textboxText.flags_08 & 0x200) != 0) {
      sp10 = textboxText.chars_1c;
      sp14 = textboxText.chars_1c * 2;
    } else {
      sp10 = 0;
      sp14 = textboxText.chars_1c;
    }

    int sp38 = 0;

    //LAB_80028328
    //LAB_80028348
    for(int i = 0; i < textboxText.chars_1c * (textboxText.lines_1e + 1); i++) {
      final TextboxChar08 chr = textboxText.chars_58[i];

      if(chr.x_00 == 0) {
        sp38 = 0;
      }

      //LAB_8002835c
      if(chr.char_06 != 0) {
        sp38 = sp38 - FUN_8002a25c(chr.char_06);

        s1 = 0;
        s2 = 0;
        s3 = 0;
        if((textboxText.flags_08 & 0x1) != 0) {
          if(i >= sp10 && i < sp14) {
            final int v1 = textboxText._2c;
            s1 = -v1;
            s2 = -v1;
            s3 = v1;
          }

          //LAB_800283c4
          if(i >= textboxText.chars_1c * textboxText.lines_1e && i < textboxText.chars_1c * (textboxText.lines_1e + 1)) {
            s1 = 0;
            s2 = 0;
            s3 = 12 - textboxText._2c;
          }
        }

        //LAB_8002840c
        setCharMetrics(chr.char_06);
        if((short)s3 < 13) {
          final int s4 = (int)textU_800be5c0.get() & 0xffff;
          final int sp20 = (int)textV_800be5c8.get() & 0xffff;

          final GpuCommandQuad cmd = new GpuCommandQuad()
            .monochrome(0x80);

          final int x = textboxText._18 + chr.x_00 * 9 - centreScreenX_1f8003dc.get() - sp38;
          final int y;

          if((textboxText.flags_08 & 0x200) != 0 && i < textboxText.chars_1c) {
            y = textboxText._1a + chr.y_02 * 12 - centreScreenY_1f8003de.get() - s1;
          } else {
            y = textboxText._1a + chr.y_02 * 12 - centreScreenY_1f8003de.get() - s1 - textboxText._2c;
          }

          //LAB_80028544
          //LAB_80028564
          final int u = s4 * 16;
          final int v = sp20 * 12 - s2;

          cmd.uv(u, v);
          cmd.clut(832 + chr._04 * 16, 480);

          final int height = 12 - s3;
          cmd.pos(x, y, 8, height);
          cmd.bpp(Bpp.BITS_4);
          cmd.vramPos(textboxVramX_80052bc8.get(0).get(), textboxVramY_80052bf4.get(0).get() < 256 ? 0 : 256);
          GPU.queueCommand(textboxText.z_0c, cmd);

          GPU.queueCommand(textboxText.z_0c + 1, new GpuCommandQuad()
            .bpp(Bpp.BITS_4)
            .monochrome(0x80)
            .clut(976, 480)
            .vramPos(textboxVramX_80052bc8.get(0).get(), textboxVramY_80052bf4.get(0).get() < 256 ? 0 : 256)
            .pos(x + 1, y + 1, 8, height)
            .uv(u, v)
          );
        }

        sp38 = sp38 + FUN_8002a1fc(chr.char_06);
      }
    }

    //LAB_800287f8
  }

  @Method(0x80028828L)
  public static void FUN_80028828(final int a0) {
    final TextboxText84 textboxText = textboxText_800bdf38[a0];

    textboxText._2c += textboxText._2a;

    if(textboxText._2c >= 12) {
      FUN_80027eb4(a0);
      textboxText._00 = 4;
      textboxText._2c = 0;
      textboxText.charY_36--;
    }

    //LAB_80028894
  }

  @Method(0x800288a4L)
  public static void FUN_800288a4(final int textboxIndex) {
    if((tickCount_800bb0fc.get() & 0x1) == 0) {
      final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

      textboxText._2c += textboxText._2a & 0x7;

      if(textboxText._2c >= 12) {
        FUN_80027eb4(textboxIndex);
        textboxText.flags_08 |= 0x4;
        textboxText._2c -= 12;
        textboxText.charY_36 = textboxText.lines_1e;
      }
    }

    //LAB_80028928
  }

  /** Calculates a good position to place a textbox for a specific sobj */
  @Method(0x80028938L)
  public static void positionSobjTextbox(final int textboxIndex, final int sobjIndex) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    positionTextboxAtSobj(sobjIndex);
    final SubmapStruct80 struct = _800c68e8;
    final int s4 = struct.x2_70;
    textbox._28 = s4;
    final int sp10 = (struct.y2_74 - struct.y3_7c) / 2;
    final int sp18 = struct.y2_74 - sp10;
    textbox._2c = sp18;
    final int sp14 = textbox._28 - struct.x1_68;
    final int textWidth = textbox.chars_18 * 9 / 2;
    final int textHeight = textbox.lines_1a * 6;

    final int width;
    if(engineState_8004dd20 != EngineState.SUBMAP_05) {
      width = 320;
    } else {
      width = 360;
    }

    //LAB_80028a20
    if(textboxText.chars_1c >= 17) {
      if(sp18 >= 121) {
        //LAB_80028acc
        final int x = width / 2;
        final int y = sp18 - sp10 - textHeight;
        textbox.x_14 = x;
        textbox.y_16 = y;
        textbox._48 = 8;

        textboxText.x_14 = x;
        textboxText.y_16 = y;
        textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 9 / 2;
        textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6;
        return;
      }

      final int x = width / 2;
      final int y = sp18 + sp10 + textHeight;
      textbox.x_14 = x;
      textbox.y_16 = y;
      textbox._48 = 7;

      textboxText.x_14 = x;
      textboxText.y_16 = y;
      textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 9 / 2;
      textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6;
      return;
    }

    //LAB_80028b38
    int y = sp18 - sp10 - textHeight;
    if(textboxFits(textboxIndex, (short)s4, (short)y)) {
      textbox.x_14 = s4;
      textbox.y_16 = y;
      textbox._48 = 0;

      textboxText.x_14 = s4;
      textboxText.y_16 = y;
      textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 9 / 2;
      textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6;
      return;
    }

    //LAB_80028bc4
    y = sp18 + sp10 + textHeight;
    if(textboxFits(textboxIndex, (short)s4, (short)y)) {
      textbox.x_14 = s4;
      textbox.y_16 = y;
      textbox._48 = 1;

      textboxText.x_14 = s4;
      textboxText.y_16 = y;
      textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 9 / 2;
      textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6;
      return;
    }

    //LAB_80028c44
    if(width / 2 < s4) {
      //LAB_80028d58
      final int s2 = s4 - sp14 - textWidth;
      y = sp18 - sp10 - textHeight / 2;
      if(textboxFits(textboxIndex, (short)s2, (short)y)) {
        textbox.x_14 = s2;
        textbox.y_16 = y;
        textbox._48 = 4;

        textboxText.x_14 = s2;
        textboxText.y_16 = y;
        textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 9 / 2;
        textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6;
        return;
      }

      //LAB_80028df0
      y = sp18 + sp10 + textHeight / 2;
      if(textboxFits(textboxIndex, (short)s2, (short)y)) {
        textbox.x_14 = s2;
        textbox.y_16 = y;
        textbox._48 = 5;

        textboxText.x_14 = s2;
        textboxText.y_16 = y;
        textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 9 / 2;
        textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6;
        return;
      }
    } else {
      final int s2 = s4 + sp14 + textWidth;
      y = sp18 - sp10 - textHeight / 2;
      if(textboxFits(textboxIndex, (short)s2, (short)y)) {
        textbox.x_14 = s2;
        textbox.y_16 = y;
        textbox._48 = 2;

        textboxText.x_14 = s2;
        textboxText.y_16 = y;
        textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 9 / 2;
        textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6;
        return;
      }

      //LAB_80028ce4
      y = sp18 + sp10 + textHeight / 2;
      if(textboxFits(textboxIndex, (short)s2, (short)y)) {
        textbox.x_14 = s2;
        textbox.y_16 = y;
        textbox._48 = 3;

        textboxText.x_14 = s2;
        textboxText.y_16 = y;
        textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 9 / 2;
        textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6;
        return;
      }
    }

    //LAB_80028e68
    final int x;
    if(width / 2 >= s4) {
      x = s4 + sp14 + textWidth;
    } else {
      //LAB_80028e8c
      x = s4 - sp14 - textWidth;
    }

    //LAB_80028e9c
    calculateAppropriateTextboxBounds(textboxIndex, (short)x, (short)(sp18 + sp10 + textHeight));
    textboxes_800be358[textboxIndex]._48 = 6;

    //LAB_80028ef0
  }

  @Method(0x80028f20L)
  public static boolean textboxFits(final int textboxIndex, final int x, final int y) {
    final int maxX;
    if(engineState_8004dd20 == EngineState.SUBMAP_05) {
      maxX = 350;
    } else {
      maxX = 310;
    }

    //LAB_80028f40
    //LAB_80028fa8
    //LAB_80028fc0
    //LAB_80028fd4
    return
      x - textboxes_800be358[textboxIndex].chars_18 * 9 / 2 >= 10 &&
      x + textboxes_800be358[textboxIndex].chars_18 * 9 / 2 <= maxX &&
      y - textboxes_800be358[textboxIndex].lines_1a * 6 >= 18 &&
      y + textboxes_800be358[textboxIndex].lines_1a * 6 <= 222;

    //LAB_80028ff0
  }

  @ScriptDescription("Unknown, sets textbox 0 to hardcoded values")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "ret", description = "Always set to 0")
  @Method(0x80028ff8L)
  public static FlowControl FUN_80028ff8(final RunningScript<?> script) {
    clearTextbox(0);

    final Textbox4c struct4c = textboxes_800be358[0];
    struct4c._04 = (int)_80052b8c.get();
    struct4c.x_14 = 260;
    struct4c.y_16 = 120;
    struct4c.chars_18 = 6;
    struct4c.lines_1a = 8;
    clearTextboxText(0);

    final TextboxText84 textboxText = textboxText_800bdf38[0];
    textboxText.type_04 = (int)_80052baa.get();
    textboxText.str_24 = _80052c20;
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
  public static void renderTextboxSelection(final int textboxIndex, final short a1) {
    final Textbox4c s0 = textboxes_800be358[textboxIndex];
    final int width = (s0.chars_18 - 1) * 9;
    final int x = s0.x_14 - centreScreenX_1f8003dc.get();
    final int y = s0.y_16 - centreScreenY_1f8003de.get() + a1 * 12 - (s0.lines_1a - 1) * 6;

    GPU.queueCommand(s0.z_0c, new GpuCommandQuad()
      .translucent(Translucency.HALF_B_PLUS_HALF_F)
      .rgb(0x80, 0x32, 0x64)
      .pos(x - width / 2, y, width, 12)
    );
  }

  /**
   * @param trim Positive trims top, negative trims bottom
   */
  @Method(0x80029300L)
  public static void renderText(final LodString text, final int x, int y, final TextColour colour, int trim) {
    final int length = textLength(text);

    trim = MathHelper.clamp(trim, -12, 12);

    int lineIndex = 0;
    int glyphNudge = 0;

    for(int i = 0; i < length; i++) {
      final int c = text.charAt(i);

      if(c == 0xa1ff) {
        lineIndex = 0;
        glyphNudge = 0;
        y += 12;
      } else {
        if(lineIndex == 0) {
          glyphNudge = 0;
        }

        if(c == 0x45) { // m
          glyphNudge -= 1;
        } else if(c == 0x2) { // .
          glyphNudge -= 2;
        } else if(c >= 0x5 && c < 0x7) { // ?, !
          glyphNudge -= 3;
        }

        final int textU = c & 0xf;
        final int textV = c / 16;
        final int v1 = textV * 12;
        final int v = trim >= 0 ? v1 : v1 - trim;
        final int h = trim >= 0 ? 12 - trim : 12 + trim;

        GPU.queueCommand(textZ_800bdf00.get(), new GpuCommandQuad()
          .bpp(Bpp.BITS_4)
          .monochrome(0x80)
          .pos(x - centreScreenX_1f8003dc.get() + lineIndex * 8 - glyphNudge, y - centreScreenY_1f8003de.get(), 8, h)
          .uv(textU * 16, v)
          .clut(832, 480)
          .vramPos(textboxVramX_80052bc8.get(0).get(), textboxVramY_80052bf4.get(0).get() < 256 ? 0 : 256)
          .rgb(colour.r, colour.g, colour.b)
        );

        glyphNudge += switch(c) {
          case 0x5, 0x23, 0x24, 0x2a, 0x37, 0x38, 0x3a, 0x3b, 0x3c, 0x3d, 0x3f, 0x40, 0x43, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4d, 0x4e, 0x51, 0x52 -> 1;
          case 0x2, 0x8, 0x3e, 0x4c -> 2;
          case 0xb, 0xc, 0x42 -> 3;
          case 0x1, 0x3, 0x4, 0x9, 0x16, 0x41, 0x44 -> 4;
          case 0x6, 0x27 -> 5;
          default -> 0;
        };

        lineIndex++;
      }
    }
  }

  @Method(0x800297a0L)
  public static LodString intToStr(int val, final LodString out) {
    final LodString tmp = new LodString(11);

    //LAB_800297b4
    for(int i = 0; i < 11; i++) {
      tmp.charAt(i, 0xa0ff);
      out.charAt(i, 0xa0ff);
    }

    int divisor = 1_000_000_000;

    //LAB_8002980c
    for(int i = 0; i < 10; i++) {
      tmp.charAt(i, _80052b40.get(val / divisor).deref().charAt(0));
      val %= divisor;
      divisor /= 10;
    }

    //LAB_80029888
    int a1;
    for(a1 = 0; a1 < 9; a1++) {
      if(tmp.charAt(a1) != _80052b40.get(0).deref().charAt(0)) {
        break;
      }
    }

    //LAB_800298b8
    //LAB_800298c4
    for(int a2 = 0; a1 < 10 && a2 < 8; a1++, a2++) {
      out.charAt(a2, tmp.charAt(a1));
    }

    //LAB_80029914
    return out;
  }

  @Method(0x80029920L)
  public static void setTextboxArrowPosition(final int textboxIndex, final int a1) {
    final TextboxArrow0c arrow = textboxArrows_800bdea0[textboxIndex];

    if(a1 == 0) {
      arrow._00 = 0;
    } else {
      //LAB_80029948
      arrow._00 |= 1;
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

    if((arrow._00 & 0x1) != 0) {
      final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
      if((textboxText.flags_08 & 0x1000) != 0) {
        final int left = arrow.x_04 - centreScreenX_1f8003dc.get() - 8;
        final int right = arrow.x_04 - centreScreenX_1f8003dc.get() + 8;
        final int top = arrow.y_06 - centreScreenY_1f8003de.get() - 6;
        final int bottom = arrow.y_06 - centreScreenY_1f8003de.get() + 8;
        final int leftU = 64 + arrow.spriteIndex_08 * 16;
        final int rightU = 80 + arrow.spriteIndex_08 * 16;

        GPU.queueCommand(textboxText.z_0c, new GpuCommandPoly(4)
          .bpp(Bpp.BITS_4)
          .translucent(Translucency.HALF_B_PLUS_HALF_F)
          .monochrome(0x80)
          .clut(1008, 484)
          .vramPos(896, 256)
          .pos(0, left, top)
          .uv(0, leftU, 0)
          .pos(1, right, top)
          .uv(1, rightU, 0)
          .pos(2, left, bottom)
          .uv(2, leftU, 14)
          .pos(3, right, bottom)
          .uv(3, rightU, 14)
        );
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
      if(textboxes_800be358[i].state_00 == 0 && textboxText_800bdf38[i]._00 == 0) {
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
    struct4c._04 = script.params_20[1].get();
    struct4c.x_14 = script.params_20[2].get();
    struct4c.y_16 = script.params_20[3].get();
    struct4c.chars_18 = script.params_20[4].get() + 1;
    struct4c.lines_1a = script.params_20[5].get() + 1;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, gets textbox value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The return value")
  @Method(0x80029c98L)
  public static FlowControl FUN_80029c98(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    script.params_20[1].set(textboxes_800be358[textboxIndex].state_00 | textboxText_800bdf38[textboxIndex]._00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, gets textbox value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The return value")
  @Method(0x80029cf4L)
  public static FlowControl FUN_80029cf4(final RunningScript<?> script) {
    script.params_20[1].set(textboxes_800be358[script.params_20[0].get()].state_00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, gets textbox value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The return value")
  @Method(0x80029d34L)
  public static FlowControl FUN_80029d34(final RunningScript<?> script) {
    script.params_20[1].set(textboxText_800bdf38[script.params_20[0].get()]._00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates a textbox")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @Method(0x80029d6cL)
  public static FlowControl scriptDeallocateTextbox(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    if(textboxText._00 != 0) {
      textboxText.chars_58 = null;
    }

    //LAB_80029db8
    textboxText._00 = 0;
    textboxes_800be358[textboxIndex].state_00 = 0;
    setTextboxArrowPosition(textboxIndex, 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates all textboxes")
  @Method(0x80029e04L)
  public static FlowControl scriptDeallocateAllTextboxes(final RunningScript<?> script) {
    //LAB_80029e2c
    for(int i = 0; i < 8; i++) {
      final Textbox4c s2 = textboxes_800be358[i];
      final TextboxText84 textboxText = textboxText_800bdf38[i];

      if(textboxText._00 != 0) {
        textboxText.chars_58 = null;
      }

      //LAB_80029e48
      textboxText._00 = 0;
      s2.state_00 = 0;
      setTextboxArrowPosition(i, 0);
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to textboxes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value")
  @Method(0x80029e8cL)
  public static FlowControl FUN_80029e8c(final RunningScript<?> script) {
    _800bdf10.offset(Math.min(9, script.params_20[0].get()) * 0x4L).setu(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to textboxes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @Method(0x80029eccL)
  public static FlowControl FUN_80029ecc(final RunningScript<?> script) {
    final TextboxText84 textboxText = textboxText_800bdf38[script.params_20[0].get()];
    if(textboxText._00 == 16 && (textboxText.flags_08 & 0x20) != 0) {
      textboxText.flags_08 ^= 0x20;
    }

    //LAB_80029f18
    //LAB_80029f1c
    textboxText.flags_08 |= 0x40;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to textboxes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value")
  @Method(0x80029f48L)
  public static FlowControl FUN_80029f48(final RunningScript<?> script) {
    script.params_20[1].set(textboxText_800bdf38[script.params_20[0].get()]._6c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to textboxes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value")
  @Method(0x80029f80L)
  public static FlowControl FUN_80029f80(final RunningScript<?> script) {
    script.params_20[1].set(textboxText_800bdf38[script.params_20[0].get()]._7c);
    return FlowControl.CONTINUE;
  }

  @Method(0x8002a058L)
  public static void FUN_8002a058() {
    //LAB_8002a080
    for(int i = 0; i < 8; i++) {
      if(textboxes_800be358[i].state_00 != 0) {
        FUN_80025a04(i);
      }

      //LAB_8002a098
      if(textboxText_800bdf38[i]._00 != 0) {
        FUN_800264b0(i); // Animates the textbox arrow
      }
    }
  }

  @Method(0x8002a0e4L)
  public static void renderTextboxes() {
    //LAB_8002a10c
    for(int i = 0; i < 8; i++) {
      final Textbox4c struct4c = textboxes_800be358[i];

      if(struct4c.state_00 != 0 && struct4c._08 < 0) {
        renderTextboxBackground(i);
      }

      //LAB_8002a134
      if(textboxText_800bdf38[i]._00 != 0) {
        renderTextboxText(i);
        renderTextboxArrow(i);
      }

      //LAB_8002a154
    }
  }

  @Method(0x8002a180L)
  public static void FUN_8002a180(final int textboxIndex, final int charX, final int charY, int a3, final int lodChar) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    final int charIndex = textboxText.charY_36 * textboxText.chars_1c + textboxText.charX_34;
    final TextboxChar08 chr = textboxText.chars_58[charIndex];
    chr.x_00 = charX;
    chr.y_02 = charY;

    if((textboxText.flags_08 & 0x200) != 0 && charY == 0) {
      a3 = 8;
    }

    //LAB_8002a1e8
    //LAB_8002a1ec
    chr._04 = a3;

    // Hellena Prison has a retail bug (textbox name says Warden?iate)
    if(lodChar == 0x900 || lodChar == -1) {
      chr.char_06 = 0;
    } else {
      chr.char_06 = lodChar;
    }
  }

  @Method(0x8002a1fcL)
  public static int FUN_8002a1fc(final long a0) {
    //LAB_8002a254
    return switch((int)(a0 & 0xffff)) {
      case 0x5, 0x23, 0x24, 0x2a, 0x37, 0x38, 0x3a, 0x3b, 0x3c, 0x3d, 0x3f, 0x40, 0x43, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4d, 0x4e, 0x51, 0x52 -> 1;
      case 0x2, 0x8, 0x3e, 0x4c -> 2;
      case 0xb, 0xc, 0x42 -> 3;
      case 0x1, 0x3, 0x4, 0x9, 0x16, 0x41, 0x44 -> 4;
      case 0x6, 0x27 -> 5;
      default -> 0;
    };
  }

  @Method(0x8002a25cL)
  public static int FUN_8002a25c(long a0) {
    a0 = a0 & 0xffffL;

    //LAB_8002a288
    if(a0 == 0x45L) {
      return 1;
    }

    if(a0 == 0x2L) {
      //LAB_8002a29c
      return 2;
    }

    if((int)a0 >= 0x5L && (int)a0 < 0x7L) {
      //LAB_8002a2a4
      return 3;
    }

    //LAB_8002a2a8
    //LAB_8002a2ac
    return 0;
  }

  @Method(0x8002a2b4L)
  public static void clearTextboxChars(final int textboxIndex) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    //LAB_8002a2f0
    for(int charIndex = 0; charIndex < textboxText.chars_1c * (textboxText.lines_1e + 1); charIndex++) {
      final TextboxChar08 chr = textboxText.chars_58[charIndex];
      chr.x_00 = 0;
      chr.y_02 = 0;
      chr._04 = 0;
      chr.char_06 = 0;
    }

    //LAB_8002a324
  }

  @Method(0x8002a32cL)
  public static void initTextbox(final int textboxIndex, final int a1, final int x, final int y, final int chars, final int lines) {
    clearTextbox(textboxIndex);

    final Textbox4c struct = textboxes_800be358[textboxIndex];
    struct._04 = (a1 & 1) + 1;
    struct._06 = 1;
    struct._08 |= 0x4;

    struct.x_14 = x;
    struct.y_16 = y;
    struct.chars_18 = chars + 1;
    struct.lines_1a = lines + 1;
  }

  @Method(0x8002a3ecL)
  public static void FUN_8002a3ec(final int textboxIndex, final int mode) {
    if(mode == 0) {
      //LAB_8002a40c
      textboxText_800bdf38[textboxIndex]._00 = 0;
      textboxes_800be358[textboxIndex].state_00 = 0;
    } else {
      //LAB_8002a458
      textboxes_800be358[textboxIndex].state_00 = 3;
    }
  }

  /** Too bad I don't know what state 6 is */
  @Method(0x8002a488L)
  public static boolean isTextboxInState6(final int textboxIndex) {
    return textboxes_800be358[textboxIndex].state_00 == 6;
  }

  @Method(0x8002a4c4L)
  public static void updateTextboxArrowSprite(final int textboxIndex) {
    final TextboxArrow0c arrow = textboxArrows_800bdea0[textboxIndex];

    if((arrow._00 & 0x1) != 0) {
      if((textboxText_800bdf38[textboxIndex].flags_08 & 0x1000) != 0) {
        if((tickCount_800bb0fc.get() & 0x1) == 0) {
          arrow.spriteIndex_08++;
        }

        //LAB_8002a53c
        if(arrow.spriteIndex_08 >= 7) {
          arrow.spriteIndex_08 = 0;
        }
      }
    }

    //LAB_8002a554
  }

  @Method(0x8002a59cL)
  public static int textWidth(final LodString text) {
    return textWidth(text.get());
  }

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

  public static int charWidth(final char chr) {
    final int nudge = switch(chr) {
      case '?', 'E', 'F', 'L', 'Y', 'Z', 'b', 'c', 'd', 'e', 'g', 'h', 'k', 'n', 'o', 'p', 'q', 'r', 's', 'u', 'v', 'y', 'z' -> 1;
      case '.', '/', 'f', 't' -> 2;
      case '(', ')', 'j' -> 3;
      case ',', '·', ':', '\'', '1', 'i', 'l' -> 4;
      case '!', 'I' -> 5;
      default -> 0;
    };

    return 8 - nudge;
  }

  @Method(0x8002a63cL)
  public static void setCharMetrics(final int chr) {
    textU_800be5c0.setu(chr & 0xf);
    textV_800be5c8.setu(chr % 208 / 16);
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

      //LAB_8002a758
      for(int i = 0; i < 5; i++) {
        stats.equipment_30[i] = 0xff;
      }

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
      stats.equipmentSpecial2Flag80_56 = 0;
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

    _800be5d0.setu(0);
  }

  @Method(0x8002a86cL)
  public static void clearEquipmentStats(final int charIndex) {
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    stats.specialEffectFlag_76 = 0;
    stats.equipmentType_77 = 0;
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
  public static void FUN_8002a9c0() {
    submapCut_80052c30.set(675);
    submapScene_80052c34.set(4);
    index_80052c38.set(0);
    submapCut_80052c3c.set(-1);
    _80052c40.setu(0);
    _80052c44.setu(0x2L);
  }

  @Method(0x8002aa04L)
  public static void FUN_8002aa04() {
    _800bed28.setu(0);
  }

  @Method(0x8002aae8L)
  public static long FUN_8002aae8() {
    long s0 = 0;
    switch((int)_80052c44.get()) {
      case 0x1:
      case 0x2:
        return 0;

      case 0x5:
        renderEnvironment();
        FUN_800e8e50();
        FUN_800e828c();
        FUN_800e4f8c();
        unloadSmap();

        _80052c44.setu(0x2L);
        break;

      case 0x4:
        renderEnvironment();

      case 0x3:
        FUN_800e8e50();
        FUN_800e828c();
        FUN_800e4f8c();
        unloadSmap();
        FUN_800e4e5c();

        //LAB_8002ab98
        _80052c44.setu(0x2L);
        break;

      case 0x0:
        s0 = 0x1L;
        renderEnvironment();
        break;
    }

    //caseD_6
    if((submapFlags_800f7e54 & 0x1) == 0) {
      // If an encounter should start
      if(handleEncounters() != 0) {
        mapTransition(-1, 0);
      }
    }

    //LAB_8002abdc
    //LAB_8002abe0
    final int collisionAndTransitionInfo = getCollisionAndTransitionInfo(index_80052c38.get());
    if((collisionAndTransitionInfo & 0x10) != 0) {
      mapTransition(collisionAndTransitionInfo >>> 22, collisionAndTransitionInfo >>> 16 & 0x3f);
    }

    //LAB_8002ac10
    //LAB_8002ac14
    return s0;
  }

  @Method(0x8002bb38L)
  public static void FUN_8002bb38(final int joypadIndex, final int a1) {
    if(!gameState_800babc8.vibrationEnabled_4e1) {
      return;
    }

    LOGGER.info("Rumble 8002bb38 %x %x", joypadIndex, a1);
  }

  @Method(0x8002bcc8L)
  public static void FUN_8002bcc8(final int a0, final int a1) {
    if(!gameState_800babc8.vibrationEnabled_4e1) {
      return;
    }

    LOGGER.info("Rumble 8002bcc8 %x %x", a0, a1);
  }

  @Method(0x8002bda4L)
  public static void FUN_8002bda4(final int a0, final int a1, final int a2) {
    if(!gameState_800babc8.vibrationEnabled_4e1) {
      return;
    }

    LOGGER.info("Rumble 8002bda4 %x %x %x", a0, a1, a2);
  }

  @Method(0x8002c178L)
  public static void FUN_8002c178(final int a0) {
    LOGGER.info("Rumble 8002c178 %x", a0);
  }

  @Method(0x8002c184L)
  public static void FUN_8002c184() {
    LOGGER.info("Rumble 8002c184");
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
      //TODO don't do this.
      new Thread(() -> Fmv.playXa(xaArchiveIndex, xaFileIndex)).start();
//      final CdlLOC pos = CdlFILE_800bb4c8.get((int)MEMORY.ref(2, v1).offset(xaArchiveIndex * 0x8L).getSigned()).pos;

//      CDROM.playXaAudio(pos, 1, xaFileIndex, () -> _800bf0cf.setu(0));
      _800bf0cf.setu(4);
    }

    if(xaLoadingStage == 2) {
      _800bf0cf.setu(4);
    }

    return 0;
  }

  @Method(0x8002ced8L)
  public static void start() {
    MEMORY.memfill(_8005a1d8.getAddress(), 0x6c4b0, 0);
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
