package legend.game;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
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
import legend.game.fmv.Fmv;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
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
import legend.game.types.InventoryMenuState;
import legend.game.types.LodString;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.Model124;
import legend.game.types.ModelPartTransforms0c;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.Textbox4c;
import legend.game.types.TextboxArrow0c;
import legend.game.types.TextboxBorderMetrics0c;
import legend.game.types.TextboxChar08;
import legend.game.types.TextboxState;
import legend.game.types.TextboxText84;
import legend.game.types.TextboxTextState;
import legend.game.types.TextboxType;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.types.UiPart;
import legend.game.types.UiType;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.menuAssetsLoaded;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderMenus;
import static legend.game.SItem.renderPostCombatReport;
import static legend.game.SItem.textLength;
import static legend.game.Scus94491BpeSegment.FUN_8001ae90;
import static legend.game.Scus94491BpeSegment.FUN_8001d51c;
import static legend.game.Scus94491BpeSegment.FUN_8001e010;
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
import static legend.game.Scus94491BpeSegment.textboxBorderMetrics_800108b0;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.freeSequence;
import static legend.game.Scus94491BpeSegment_8004.stopMusicSequence;
import static legend.game.Scus94491BpeSegment_8005._8005027c;
import static legend.game.Scus94491BpeSegment_8005._8005039c;
import static legend.game.Scus94491BpeSegment_8005._800503b0;
import static legend.game.Scus94491BpeSegment_8005._800503d4;
import static legend.game.Scus94491BpeSegment_8005._800503f8;
import static legend.game.Scus94491BpeSegment_8005._80050424;
import static legend.game.Scus94491BpeSegment_8005._80052c20;
import static legend.game.Scus94491BpeSegment_8005._80052c40;
import static legend.game.Scus94491BpeSegment_8005._8005a1d8;
import static legend.game.Scus94491BpeSegment_8005.digits_80052b40;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.monsterSoundFileIndices_800500e8;
import static legend.game.Scus94491BpeSegment_8005.renderBorder_80052b68;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c3c;
import static legend.game.Scus94491BpeSegment_8005.submapEnvState_80052c44;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8005.textboxMode_80052b88;
import static legend.game.Scus94491BpeSegment_8005.textboxTextType_80052ba8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bd7ac;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b._800bdc58;
import static legend.game.Scus94491BpeSegment_800b._800bdf04;
import static legend.game.Scus94491BpeSegment_800b._800bdf08;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b.encounterSoundEffects_800bd610;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.input_800bee90;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.previousEngineState_800bdb88;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.repeat_800bee98;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.soundFiles_800bcf80;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.submapFullyLoaded_800bd7b4;
import static legend.game.Scus94491BpeSegment_800b.textCol_800be5c0;
import static legend.game.Scus94491BpeSegment_800b.textRow_800be5c8;
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

public final class Scus94491BpeSegment_8002 {
  private Scus94491BpeSegment_8002() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8002.class);

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
  public static void adjustPartUvs(final ModelPart10 dobj2, final int colourMap) {
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

    final float transferX = model.coord2_14.coord.transfer.x;
    final float transferY = model.coord2_14.coord.transfer.y;
    final float transferZ = model.coord2_14.coord.transfer.z;

    //LAB_80020760
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    currentEngineState_8004dd04.modelLoaded(model, cContainer);

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
    model.shadowOffset_118.zero();
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

          final float interpolationScale = (model.interpolationFrameIndex + 1.0f) / (interpolationFrameCount + 1.0f);

          params.trans.set(
            Math.lerp(params.trans.x, transforms[0][i].translate_06.x, interpolationScale),
            Math.lerp(params.trans.y, transforms[0][i].translate_06.y, interpolationScale),
            Math.lerp(params.trans.z, transforms[0][i].translate_06.z, interpolationScale)
          );

          coord2.coord.transfer.set(params.trans);
          coord2.coord.rotationZYX(params.rotate);
        }

        //LAB_80020d6c
      } else {
        //LAB_80020d74
        //LAB_80020d8c
        for(int i = 0; i < model.modelParts_00.length; i++) {
          final GsCOORDINATE2 coord2 = model.modelParts_00[i].coord2_04;
          final Transforms params = coord2.transforms;

          params.rotate.set(transforms[0][i].rotate_00);
          params.trans.set(transforms[0][i].translate_06);
          coord2.coord.transfer.set(params.trans);
          coord2.coord.rotationZYX(params.rotate);
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
        coord2.coord.rotationZYX(params.rotate);

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
    //LAB_80020f30
    //LAB_80020f34
    submapFullyLoaded_800bd7b4.set(false);

    final EngineStateEnum previousState = previousEngineState_800bdb88;
    if(previousState != engineState_8004dd20) {
      previousEngineState_800bdb88 = engineState_8004dd20;

      if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05) {
        _800bd7b0.set(2);
        transitioningFromCombatToSubmap_800bd7b8.set(false);

        if(previousState == EngineStateEnum.TITLE_02) {
          _800bd7b0.set(9);
        }

        //LAB_80020f84
        if(previousState == EngineStateEnum.COMBAT_06) {
          _800bd7b0.set(-4);
          transitioningFromCombatToSubmap_800bd7b8.set(true);
        }

        //LAB_80020fa4
        if(previousState == EngineStateEnum.WORLD_MAP_08) {
          _800bd7b0.set(3);
        }
      }
    }

    //LAB_80020fb4
    //LAB_80020fb8
    if(previousEngineState_800bdb88 == EngineStateEnum.TITLE_02) {
      _800bd7ac.set(true);
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

  @Method(0x80021258L)
  public static void renderDobj2(final ModelPart10 dobj2) {
    if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05) {
      //LAB_800212b0
      Renderer.renderDobj2(dobj2, false, 0);
      return;
    }

    if(engineState_8004dd20 == EngineStateEnum.COMBAT_06) {
      //LAB_800212a0
      Renderer.renderDobj2(dobj2, true, 0);
      return;
    }

    //LAB_8002128c
    if(engineState_8004dd20 == EngineStateEnum.WORLD_MAP_08) {
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
      final MV matrix = coord2.coord;

      params.rotate.set(transforms[0][i].rotate_00);
      params.trans.set(transforms[0][i].translate_06);

      matrix.rotationZYX(params.rotate);
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
      final MV coord = coord2.coord;
      final Transforms params = coord2.transforms;
      coord.rotationZYX(params.rotate);
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
    model.coord2_14.coord.rotationXYZ(model.coord2_14.transforms.rotate);
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
    for(final ModelPart10 dobj2 : model.modelParts_00) {
      currentEngineState_8004dd04.adjustModelPartUvs(model, dobj2);
    }
  }

  @Method(0x800218f0L)
  public static void FUN_800218f0() {
    if(_800bd7ac.get()) {
      _800bd7b0.set(9);
      _800bd7ac.set(false);
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

      dobj2.coord2_04.coord.rotationXYZ(dobj2.coord2_04.transforms.rotate);
      dobj2.coord2_04.coord.scaleLocal(dobj2.coord2_04.transforms.scale);
      dobj2.coord2_04.coord.transfer.set(dobj2.coord2_04.transforms.trans);
    }

    //LAB_80021db4
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

        currentEngineState_8004dd04.menuClosed();

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

  public static int takeItemId(final Item item) {
    final int itemSlot = gameState_800babc8.items_2e9.indexOf(item);

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

    final Item item = gameState_800babc8.items_2e9.get(itemSlot);

    final TakeItemEvent takeItemEvent = EVENTS.postEvent(new TakeItemEvent(item, true));

    if(takeItemEvent.takeItem) {
      gameState_800babc8.items_2e9.remove(itemSlot);
    }

    return 0;
  }

  public static int takeEquipmentId(final Equipment equipment) {
    final int equipmentSlot = gameState_800babc8.equipment_1e8.indexOf(equipment);

    if(equipmentSlot != -1) {
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

    gameState_800babc8.equipment_1e8.remove(equipmentIndex);

    return 0;
  }

  @Method(0x80023484L)
  public static boolean giveItem(final Item item) {
    if(gameState_800babc8.items_2e9.size() >= CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get())) {
      return false;
    }

    gameState_800babc8.items_2e9.add(item);
    return true;
  }

  @Method(0x80023484L)
  public static boolean giveEquipment(final Equipment equipment) {
    if(gameState_800babc8.equipment_1e8.size() >= 255) {
      return false;
    }

    gameState_800babc8.equipment_1e8.add(equipment);
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
      .thenComparing(MenuEntryStruct04::getName);
  }

  @Method(0x80023a88L)
  public static void FUN_80023a88() {
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
      default -> {
        final int itemId = script.params_20[0].get();

        if(itemId < 0xc0) {
          yield giveEquipment(REGISTRIES.equipment.getEntry(LodMod.equipmentIdMap.get(itemId)).get()) ? 0 : 0xff;
        } else {
          yield giveItem(REGISTRIES.items.getEntry(LodMod.itemIdMap.get(itemId - 192)).get()) ? 0 : 0xff;
        }
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
      script.params_20[1].set(takeEquipmentId(REGISTRIES.equipment.getEntry(LodMod.equipmentIdMap.get(itemId)).get()));
    } else {
      script.params_20[1].set(takeItemId(REGISTRIES.items.getEntry(LodMod.itemIdMap.get(itemId - 192)).get()));
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
      textboxes_800be358[i].state_00 = TextboxState.UNINITIALIZED_0;

      textboxText_800bdf38[i] = new TextboxText84();
      textboxText_800bdf38[i].state_00 = TextboxTextState.UNINITIALIZED_0;

      textboxArrows_800bdea0[i] = new TextboxArrow0c();

      setTextboxArrowPosition(i, false);
    }

    //LAB_80025118
    for(int i = 0; i < 10; i++) {
      textboxVariables_800bdf10.get(i).set(0);
    }

    textCol_800be5c0 = 0;
    textRow_800be5c8 = 0;
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
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "packedData", description = "Unknown data, 3 nibbles, boolean in 12th bit")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The textbox x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The textbox y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The textbox width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The textbox height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.STRING, name = "text", description = "The textbox text")
  @Method(0x800254bcL)
  public static FlowControl scriptAddTextbox(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();

    if(script.params_20[1].get() != 0) {
      final int packed = script.params_20[1].get();
      final TextboxType mode = TextboxType.fromInt(textboxMode_80052b88.get(packed >>> 4 & 0xf).get());
      final boolean renderBorder = renderBorder_80052b68.get(packed & 0xf).get();
      final short type = textboxTextType_80052ba8.get(packed >>> 8 & 0xf).get();
      clearTextbox(textboxIndex);

      final Textbox4c textbox = textboxes_800be358[textboxIndex];
      final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

      textbox.type_04 = mode;
      textbox.renderBorder_06 = renderBorder;
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

      if(type == 1 && (packed & 0x1000) != 0) {
        textboxText.flags_08 |= 0x20;
      }

      //LAB_8002562c
      //LAB_80025630
      if(type == 3) {
        textboxText.selectionIndex_6c = -1;
      }

      //LAB_80025660
      if(type == 4) {
        textboxText.flags_08 |= TextboxText84.HAS_NAME;
      }

      //LAB_80025690
      textboxText.flags_08 |= TextboxText84.SHOW_ARROW;
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
      textboxText.ticksUntilStateTransition_64 = 10;
      textboxText.stateAfterTransition_78 = TextboxTextState.SELECTION_22;
      Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
    }

    //LAB_800257bc
    textboxText.flags_08 |= TextboxText84.SELECTION;
    return FlowControl.CONTINUE;
  }

  public static void initTextboxes() {
    for(int i = 0; i < textboxArrowObjs.length; i++) {
      textboxArrowObjs[i] = new QuadBuilder()
        .bpp(Bpp.BITS_4)
        .monochrome(1.0f)
        .clut(1008, 484)
        .vramPos(896, 256)
        .pos(-8.0f, -6.0f, 0.0f)
        .size(16.0f, 14.0f)
        .uv(64.0f + i * 16.0f, 0.0f)
        .build();
    }

    textboxSelectionObj = new QuadBuilder()
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .rgb(0.5f, 0.19607843f, 0.39215687f)
      .size(1.0f, 12.0f)
      .build();
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

    textbox.delete();
  }

  @Method(0x800258a8L)
  public static void clearTextboxText(final int a0) {
    final TextboxText84 textboxText = textboxText_800bdf38[a0];
    textboxText.state_00 = TextboxTextState._1;
    textboxText.flags_08 = 0;
    textboxText.z_0c = 13;
    textboxText.textColour_28 = 0;
    textboxText.scrollSpeed_2a = 2;
    textboxText.scrollAmount_2c = 0;
    textboxText.charIndex_30 = 0;
    textboxText.charX_34 = 0;
    textboxText.charY_36 = 0;
    textboxText._3a = 0;
    textboxText._3c = 0;
    textboxText._3e = 1;
    textboxText._40 = 0;
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
  }

  @Method(0x80025a04L)
  public static void handleTextbox(final int textboxIndex) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    switch(textbox.state_00) {
      case _1 -> {
        switch(textbox.type_04) {
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

        if(textbox.type_04 == TextboxType.ANIMATE_IN_OUT) {
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

      case _3 -> {
        if(textbox.type_04 == TextboxType.ANIMATE_IN_OUT) {
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
          if(textbox.type_04 == TextboxType.ANIMATE_IN_OUT) {
            textbox.state_00 = TextboxState._3;
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

    if(textbox.type_04 != TextboxType.NO_BACKGROUND) {
      if(textbox.state_00 != TextboxState._1) {
        if(textbox.backgroundObj == null || textbox.x_14 != textbox.oldX || textbox.y_16 != textbox.oldY || textbox.width_1c != textbox.oldW || textbox.height_1e != textbox.oldH) {
          if(textbox.backgroundObj != null) {
            textbox.delete();
          }

          textbox.backgroundObj = new QuadBuilder()
            .translucency(Translucency.HALF_B_PLUS_HALF_F)
            .pos(textbox.x_14 - textbox.width_1c, textbox.y_16 - textbox.height_1e, textbox.z_0c * 4.0f)
            .size(textbox.width_1c * 2.0f, textbox.height_1e * 2.0f)
            .rgb(0.0f, 41.0f / 255.0f, 159.0f / 255.0f)
            .monochrome(0, 0.0f)
            .monochrome(3, 0.0f)
            .build();

          textbox.backgroundTransforms.identity();

          textbox.oldX = textbox.x_14;
          textbox.oldY = textbox.y_16;
          textbox.oldW = textbox.width_1c;
          textbox.oldH = textbox.height_1e;
          textbox.updateBorder = true;
        }

        RENDERER.queueOrthoOverlayModel(textbox.backgroundObj, textbox.backgroundTransforms);

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
      final TextboxBorderMetrics0c borderMetrics = textboxBorderMetrics_800108b0.get(borderIndex);

      int w = borderMetrics.w_08.get();
      int h = borderMetrics.h_0a.get();
      if((textbox.flags_08 & Textbox4c.ANIMATING) != 0) {
        w = w * textbox.animationWidth_20 >> 12;
        h = h * textbox.animationHeight_22 >> 12;
      }

      //LAB_8002637c
      final int u = borderMetrics.u_04.get();
      final int v = borderMetrics.v_06.get();
      final float left = xs[borderMetrics.topLeftVertexIndex_00.get()] - w;
      final float right = xs[borderMetrics.bottomRightVertexIndex_02.get()] + w;
      final float top = ys[borderMetrics.topLeftVertexIndex_00.get()] - h;
      final float bottom = ys[borderMetrics.bottomRightVertexIndex_02.get()] + h;

      if(textbox.updateBorder) {
        if(textbox.borderObjs[borderIndex] != null) {
          textbox.borderObjs[borderIndex].delete();
        }

        textbox.borderObjs[borderIndex] = new QuadBuilder()
          .bpp(Bpp.BITS_4)
          .clut(832, 484)
          .vramPos(896, 256)
          .pos(0.0f, 0.0f, textbox.z_0c * 4.0f)
          .size(16, 16)
          .uv(u, v)
          .build();

        textbox.borderTransforms[borderIndex]
          .scaling((right - left) / 16.0f, (bottom - top) / 16.0f, 1.0f);
        textbox.borderTransforms[borderIndex].transfer.set(left, top, 0.0f);
      }

      RENDERER.queueOrthoOverlayModel(textbox.borderObjs[borderIndex], textbox.borderTransforms[borderIndex]);
    }

    textbox.oldScaleW = textbox.animationWidth_20;
    textbox.oldScaleH = textbox.animationHeight_22;
    textbox.updateBorder = false;
  }

  /** I think this method handles textboxes */
  @Method(0x800264b0L)
  public static void handleTextboxText(final int textboxIndex) {
    final Textbox4c struct4c = textboxes_800be358[textboxIndex];
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    switch(textboxText.state_00) {
      case _1 -> {
        //LAB_8002663c
        if((textboxText.flags_08 & 0x1) == 0) {
          switch(textboxText.type_04) {
            case 0 -> textboxText.state_00 = TextboxTextState._12;

            case 2 -> {
              textboxText.state_00 = TextboxTextState._10;
              textboxText.flags_08 |= 0x1;
              textboxText.scrollSpeed_2a = 1;
              textboxText.charX_34 = 0;
              textboxText.charY_36 = textboxText.lines_1e;
            }

            case 3 -> {
              textboxText.state_00 = TextboxTextState.TRANSITION_AFTER_TIMEOUT_23;
              textboxText.flags_08 |= 0x1;
              textboxText.scrollSpeed_2a = 1;
              textboxText.charX_34 = 0;
              textboxText.charY_36 = 0;
              textboxText.ticksUntilStateTransition_64 = 10;
              textboxText.stateAfterTransition_78 = TextboxTextState._17;
              Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
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
              textboxText.state_00 = TextboxTextState._4;
          }
        }
      }

      case _2 -> textboxText.state_00 = TextboxTextState._4;

      //LAB_80026538
      case _4 ->
        //LAB_800267c4
        processTextboxLine(textboxIndex);

      case _5 -> {
        //LAB_800267d4
        if((textboxText.flags_08 & 0x1) != 0) {
          //LAB_800267f4
          if(textboxText._3a >= textboxText.lines_1e - ((textboxText.flags_08 & TextboxText84.HAS_NAME) == 0 ? 1 : 2)) {
            textboxText.flags_08 ^= 0x1;
            textboxText._3a = 0;
            setTextboxArrowPosition(textboxIndex, true);
          } else {
            //LAB_80026828
            textboxText.state_00 = TextboxTextState._9;
            textboxText._3a++;
            scrollTextboxDown(textboxIndex);
          }
          //LAB_8002684c
        } else if((textboxText.flags_08 & 0x20) != 0) {
          textboxText.state_00 = TextboxTextState._9;
          textboxText.flags_08 |= 0x1;
        } else {
          //LAB_8002686c
          if((press_800bee94.get() & 0x20) != 0 || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
            setTextboxArrowPosition(textboxIndex, false);

            if(textboxText.type_04 == 1 || textboxText.type_04 == 4) {
              //LAB_800268b4
              textboxText.state_00 = TextboxTextState._9;
              textboxText.flags_08 |= 0x1;
            }

            if(textboxText.type_04 == 2) {
              //LAB_800268d0
              textboxText.state_00 = TextboxTextState._10;
            }
          }
        }
      }

      case _6 -> {
        //LAB_800268dc
        if((press_800bee94.get() & 0x20) != 0 || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
          textboxText.state_00 = TextboxTextState._4;
        }
      }

      case _7 -> {
        //LAB_800268fc
        textboxText._40++;
        if(textboxText._40 >= textboxText._3e) {
          textboxText._40 = 0;
          textboxText.state_00 = TextboxTextState._4;
        }

        //LAB_80026928
        if((textboxText.flags_08 & 0x20) == 0) {
          if((input_800bee90.get() & 0x20) != 0 || CONFIG.getConfig(CoreMod.QUICK_TEXT_CONFIG.get())) {
            boolean found = false;

            //LAB_80026954
            for(int lineIndex = 0; lineIndex < 4; lineIndex++) {
              processTextboxLine(textboxIndex);

              if(textboxText.state_00 == TextboxTextState.UNINITIALIZED_0 || textboxText.state_00 == TextboxTextState._1 || textboxText.state_00 == TextboxTextState._2 || textboxText.state_00 == TextboxTextState._3 || textboxText.state_00 == TextboxTextState._4 || textboxText.state_00 == TextboxTextState._5 || textboxText.state_00 == TextboxTextState._6 || textboxText.state_00 == TextboxTextState._15 || textboxText.state_00 == TextboxTextState._11 || textboxText.state_00 == TextboxTextState._13) {
                //LAB_8002698c
                found = true;
                break;
              }

              //LAB_80026994
            }

            //LAB_800269a0
            if(!found) {
              textboxText._40 = 0;
              textboxText.state_00 = TextboxTextState._4;
            }
          }
        }
      }

      case _8 -> {
        //LAB_800269cc
        if(textboxText._44 > 0) {
          //LAB_800269e8
          textboxText._44--;
        } else {
          //LAB_800269e0
          textboxText.state_00 = TextboxTextState._4;
        }
      }

      //LAB_80026554
      case _9 ->
        //LAB_800269f0
        scrollTextboxDown(textboxIndex);

      //LAB_80026580
      case _10 -> {
        //LAB_80026a00
        scrollTextboxUp(textboxIndex);

        if((textboxText.flags_08 & 0x4) != 0) {
          textboxText.flags_08 ^= 0x4;

          if((textboxText.flags_08 & 0x2) == 0) {
            //LAB_80026a5c
            do {
              processTextboxLine(textboxIndex);

              if(textboxText.state_00 == TextboxTextState._15) {
                textboxText._3a = 0;
                textboxText.flags_08 |= 0x2;
              }
            } while(textboxText.state_00 != TextboxTextState._5 && textboxText.state_00 != TextboxTextState._15);

            //LAB_80026a8c
            textboxText.state_00 = TextboxTextState._10;
          } else {
            textboxText._3a++;

            if(textboxText._3a >= textboxText.lines_1e + 1) {
              textboxText.delete();
              textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
            }
          }
        }
      }

      case _11 -> {
        //LAB_80026a98
        if((press_800bee94.get() & 0x20) != 0 || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
          setTextboxArrowPosition(textboxIndex, false);
          clearTextboxChars(textboxIndex);

          textboxText.state_00 = TextboxTextState._4;
          textboxText.flags_08 ^= 0x1;
          textboxText.charX_34 = 0;
          textboxText.charY_36 = 0;
          textboxText._3a = 0;

          if((textboxText.flags_08 & 0x8) != 0) {
            textboxText.state_00 = TextboxTextState._13;
          }
        }
      }

      case _12 -> {
        //LAB_80026af0
        if(struct4c.state_00 == TextboxState.UNINITIALIZED_0) {
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

          if(textboxText.state_00 == TextboxTextState._5) {
            //LAB_80026b28
            textboxText.state_00 = TextboxTextState._11;
            break;
          }
        } while(textboxText.state_00 != TextboxTextState._15);

        //LAB_80026b6c
        if((textboxText.flags_08 & 0x20) != 0) {
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
          textboxText.ticksUntilStateTransition_64 = 10;
          textboxText.stateAfterTransition_78 = TextboxTextState.SELECTION_22;
          textboxText.selectionLine_68 = textboxText.minSelectionLine_72;
          Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
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
              textboxText._3a = 0;
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
        if((textboxText.flags_08 & 0x20) != 0) {
          textboxText.state_00 = TextboxTextState._16;
        } else {
          //LAB_80026cd0
          if((press_800bee94.get() & 0x20) != 0 || CONFIG.getConfig(CoreMod.AUTO_TEXT_CONFIG.get())) {
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

          if(textboxText.state_00 == TextboxTextState._5) {
            //LAB_80026d14
            textboxText.state_00 = TextboxTextState._18;
            break;
          }

          if(textboxText.state_00 == TextboxTextState._15) {
            textboxText.state_00 = TextboxTextState._18;
            textboxText._3a = 0;
            textboxText.flags_08 |= 0x102;
            break;
          }
        } while(true);

        //LAB_80026d64
        textboxText.selectionIndex_6c = -1;
        textboxText.lines_1e--;
      }

      //LAB_8002659c
      case _18 -> {
        //LAB_80026d94
        renderTextboxSelection(textboxIndex, textboxText.selectionLine_60);

        if((press_800bee94.get() & 0x20) != 0) {
          Scus94491BpeSegment.playSound(0, 2, 0, 0, (short)0, (short)0);
          textboxText.delete();
          textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
          textboxText.selectionIndex_6c = textboxText.selectionLine_68;
        } else {
          //LAB_80026df0
          if((input_800bee90.get() & 0x4000) == 0) {
            //LAB_80026ee8
            if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
              if((textboxText.flags_08 & 0x100) == 0 || textboxText.selectionLine_68 != 0) {
                //LAB_80026f38
                Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

                int extraLines = 3;
                if(textboxText.selectionLine_60 > 0) {
                  textboxText.state_00 = TextboxTextState._19;
                  textboxText.selectionLine_60--;
                  textboxText.ticksUntilStateTransition_64 = 4;
                  textboxText.selectionLine_68--;
                } else {
                  //LAB_80026f88
                  if((textboxText.flags_08 & 0x2) != 0) {
                    // TODO not sure about this block of code
                    if(textboxText._3a == 1) {
                      extraLines = 1;
                    } else {
                      if(textboxText._3a == 0) {
                        //LAB_80026fbc
                        extraLines = 2;
                      }

                      //LAB_80026fc0
                      textboxText._3a = 0;
                      textboxText.flags_08 ^= 0x2;
                    }

                    //LAB_80026fe8
                    textboxText._3a--;
                  }

                  //LAB_80027014
                  textboxText.selectionLine_68--;

                  if(textboxText.selectionLine_68 < 0) {
                    textboxText.selectionLine_68 = 0;
                  } else {
                    //LAB_80027044
                    textboxText.scrollAmount_2c = 12;
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
                    } while(textboxText.charY_36 == 0 && textboxText.state_00 != TextboxTextState._5);

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
          textboxText.ticksUntilStateTransition_64 = 4;
          textboxText.selectionLine_68++;

          if((textboxText.flags_08 & 0x100) == 0 || textboxText.charY_36 + 1 != textboxText.selectionLine_68) {
            //LAB_80026e68
            //LAB_80026e6c
            if(textboxText.selectionLine_60 < textboxText.lines_1e) {
              //LAB_80026ed0
              Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

              //LAB_80026ee8
              if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
                if((textboxText.flags_08 & 0x100) == 0 || textboxText.selectionLine_68 != 0) {
                  //LAB_80026f38
                  Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

                  int extraLines = 3;
                  if(textboxText.selectionLine_60 > 0) {
                    textboxText.state_00 = TextboxTextState._19;
                    textboxText.selectionLine_60--;
                    textboxText.ticksUntilStateTransition_64 = 4;
                    textboxText.selectionLine_68--;
                  } else {
                    //LAB_80026f88
                    if((textboxText.flags_08 & 0x2) != 0) {
                      // TODO not sure about this block of code
                      if(textboxText._3a == 1) {
                        extraLines = 1;
                      } else {
                        if(textboxText._3a == 0) {
                          //LAB_80026fbc
                          extraLines = 2;
                        }

                        //LAB_80026fc0
                        textboxText._3a = 0;
                        textboxText.flags_08 ^= 0x2;
                      }

                      //LAB_80026fe8
                      textboxText._3a--;
                    }

                    //LAB_80027014
                    textboxText.selectionLine_68--;

                    if(textboxText.selectionLine_68 < 0) {
                      textboxText.selectionLine_68 = 0;
                    } else {
                      //LAB_80027044
                      textboxText.scrollAmount_2c = 12;
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
                      } while(textboxText.charY_36 == 0 && textboxText.state_00 != TextboxTextState._5);

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
              textboxText.scrollAmount_2c = (short)0;

              if(textboxText._3a == 1) {
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
        renderTextboxSelection(textboxIndex, textboxText.selectionLine_68);
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
        textboxText.scrollAmount_2c += 4;

        if(textboxText.scrollAmount_2c >= 12) {
          advanceTextbox(textboxIndex);
          textboxText.flags_08 |= 0x4;
          textboxText.scrollAmount_2c -= 12;
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
                textboxText._3a = 0;
                textboxText.flags_08 |= 0x2;
                break;
              }
            } while(textboxText.state_00 != TextboxTextState._5);
          } else {
            textboxText._3a++;

            if(textboxText._3a >= textboxText.lines_1e + 1) {
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
        textboxText.scrollAmount_2c -= 4;
        if(textboxText.scrollAmount_2c <= 0) {
          textboxText.charY_36 = 0;
          textboxText.scrollAmount_2c = 0;
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
        renderTextboxSelection(textboxIndex, textboxText.selectionLine_68);

        if((press_800bee94.get() & 0x20) != 0) {
          Scus94491BpeSegment.playSound(0, 2, 0, 0, (short)0, (short)0);
          textboxText.delete();
          textboxText.state_00 = TextboxTextState.UNINITIALIZED_0;
          textboxText.selectionIndex_6c = textboxText.selectionLine_68 - textboxText.minSelectionLine_72;
        } else {
          //LAB_800273bc
          if(Input.getButtonState(InputAction.DPAD_UP) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
            textboxText.ticksUntilStateTransition_64 = 4;
            textboxText.selectionLine_68--;

            if(textboxText.selectionLine_68 < textboxText.minSelectionLine_72) {
              textboxText.selectionLine_68 = textboxText.minSelectionLine_72;
            } else {
              //LAB_80027404
              Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);
              textboxText.state_00 = TextboxTextState._19;
            }
          }

          //LAB_80027420
          if(Input.getButtonState(InputAction.DPAD_DOWN) || Input.getButtonState(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
            textboxText.ticksUntilStateTransition_64 = 4;
            textboxText.selectionLine_68++;

            if(textboxText.selectionLine_68 > textboxText.maxSelectionLine_70) {
              textboxText.selectionLine_68 = textboxText.maxSelectionLine_70;
            } else {
              //LAB_80027480
              //LAB_80027490
              Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);
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
          textboxText.ticksUntilStateTransition_64 = 4;
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
    final LodString str = textboxText.str_24;

    if((textboxText.flags_08 & TextboxText84.SHOW_VAR) != 0) {
      final int digitIndex = textboxText.digitIndex_80;
      appendTextboxChar(textboxIndex, textboxText.charX_34, textboxText.charY_36, textboxText.textColour_28, textboxText.digits_46[digitIndex]);

      textboxText.charX_34++;
      textboxText._3c++;
      textboxText.digitIndex_80++;

      if(textboxText.charX_34 < textboxText.chars_1c) {
        //LAB_80027768
        if(textboxText.digits_46[digitIndex + 1] == -1) {
          textboxText.flags_08 ^= TextboxText84.SHOW_VAR;
        }
      } else if(textboxText.charY_36 >= textboxText.lines_1e - 1) {
        if(textboxText.digits_46[digitIndex + 1] != -1) {
          textboxText.state_00 = TextboxTextState._5;
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
        textboxText.state_00 = TextboxTextState._5;
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
            textboxText.state_00 = TextboxTextState._5;

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
          textboxText.state_00 = TextboxTextState._8;
          textboxText._44 = 60 / vsyncMode_8007a3b8 * (chr & 0xff);
          parseMore = false;
        }

        case TextboxText84.COLOUR -> {
          final int colour = chr & 0xf;

          //LAB_80027950
          textboxText.textColour_28 = colour < 12 ? colour : 0;
        }

        case TextboxText84.VAR -> {
          textboxText.flags_08 |= TextboxText84.SHOW_VAR;

          //LAB_80027970
          Arrays.fill(textboxText.digits_46, -1);

          int variable = textboxVariables_800bdf10.get(chr & 0xff).get();
          int divisor = 1_000_000_000;
          final int[] varDigits = new int[10];
          //LAB_800279dc
          for(int i = 0; i < varDigits.length; i++) {
            varDigits[i] = digits_80052b40.get(variable / divisor).deref().charAt(0);
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
          textboxText._3c++;

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
              textboxText.state_00 = TextboxTextState._5;
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
        final TextboxChar08 nextLine = textboxText.chars_58[(lineIndex + 1) * textboxText.chars_1c + charIndex];
        final TextboxChar08 currentLine = textboxText.chars_58[lineIndex * textboxText.chars_1c + charIndex];
        currentLine.set(nextLine);
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
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    //LAB_8002810c
    for(int lineIndex = textboxText.lines_1e; lineIndex > 0; lineIndex--) {
      //LAB_80028128
      for(int charIndex = 0; charIndex < textboxText.chars_1c; charIndex++) {
        final TextboxChar08 previousLine = textboxText.chars_58[(lineIndex - 1) * textboxText.chars_1c + charIndex];
        final TextboxChar08 currentLine = textboxText.chars_58[lineIndex * textboxText.chars_1c + charIndex];
        currentLine.set(previousLine);
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
        int scrollV = 0;
        int scrollH = 0;
        if((textboxText.flags_08 & 0x1) != 0) {
          if(i >= firstCharInLineIndex && i < lastCharInLineIndex) {
            final int scroll = textboxText.scrollAmount_2c;
            scrollY = -scroll;
            scrollV = -scroll;
            scrollH = scroll;
          }

          //LAB_800283c4
          if(i >= textboxText.chars_1c * textboxText.lines_1e && i < textboxText.chars_1c * (textboxText.lines_1e + 1)) {
            scrollY = 0;
            scrollV = 0;
            scrollH = 12 - textboxText.scrollAmount_2c;
          }
        }

        //LAB_8002840c
        setCharMetrics(chr.char_06);
        if(scrollH < 13) {
          final float x = textboxText._18 + chr.x_00 * 9 - centreScreenX_1f8003dc.get() - nudgeX;
          final float y;

          if((textboxText.flags_08 & TextboxText84.HAS_NAME) != 0 && i < textboxText.chars_1c) {
            y = textboxText._1a + chr.y_02 * 12 - centreScreenY_1f8003de.get() - scrollY;
          } else {
            y = textboxText._1a + chr.y_02 * 12 - centreScreenY_1f8003de.get() - scrollY - textboxText.scrollAmount_2c;
          }

          //LAB_80028544
          //LAB_80028564
          final int u = textCol_800be5c0 * 16;
          final int v = textRow_800be5c8 * 12 - scrollV;
          final int height = 12 - scrollH;

          textboxText.transforms.identity();
          textboxText.transforms.transfer.set(GPU.getOffsetX() + x, GPU.getOffsetY() + y, textboxText.z_0c * 4.0f);
          RENDERER.queueOrthoOverlayModel(chr.obj, textboxText.transforms);

          GPU.queueCommand(textboxText.z_0c + 1, new GpuCommandQuad()
            .bpp(Bpp.BITS_4)
            .monochrome(0x80)
            .clut(976, 480)
            .vramPos(832, 256)
            .pos(x + 1, y + 1, 8, height)
            .uv(u, v)
          );
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

    if(textboxText.scrollAmount_2c >= 12) {
      advanceTextbox(textboxIndex);
      textboxText.state_00 = TextboxTextState._4;
      textboxText.scrollAmount_2c = 0;
      textboxText.charY_36--;
    }

    //LAB_80028894
  }

  @Method(0x800288a4L)
  public static void scrollTextboxUp(final int textboxIndex) {
    if((tickCount_800bb0fc.get() & 0x1) == 0) {
      final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

      textboxText.scrollAmount_2c += textboxText.scrollSpeed_2a;

      if(textboxText.scrollAmount_2c >= 12) {
        advanceTextbox(textboxIndex);
        textboxText.flags_08 |= 0x4;
        textboxText.scrollAmount_2c -= 12;
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
    struct4c.type_04 = TextboxType.fromInt(textboxMode_80052b88.get(2).get());
    struct4c.x_14 = 260;
    struct4c.y_16 = 120;
    struct4c.chars_18 = 6;
    struct4c.lines_1a = 8;
    clearTextboxText(0);

    final TextboxText84 textboxText = textboxText_800bdf38[0];
    textboxText.type_04 = textboxTextType_80052ba8.get(1).get();
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
  public static void renderTextboxSelection(final int textboxIndex, final int selectionLine) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    final int width = (textbox.chars_18 - 1) * 9;
    final float x = textbox.x_14;
    final float y = textbox.y_16 + selectionLine * 12 - (textbox.lines_1a - 1) * 6;

    textboxSelectionTransforms.scaling(width, 1.0f, 1.0f);
    textboxSelectionTransforms.transfer.set(x - width / 2.0f, y, textbox.z_0c);
    RENDERER.queueOrthoOverlayModel(textboxSelectionObj, textboxSelectionTransforms);
  }

  /**
   * @param trim Positive trims top, negative trims bottom
   */
  @Method(0x80029300L)
  public static void renderText(final LodString text, final float x, float y, final TextColour colour, int trim) {
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
          .vramPos(832, 256)
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
      tmp.charAt(i, digits_80052b40.get(val / divisor).deref().charAt(0));
      val %= divisor;
      divisor /= 10;
    }

    //LAB_80029888
    int a1;
    for(a1 = 0; a1 < 9; a1++) {
      if(tmp.charAt(a1) != 0x15) { // 0
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
        textboxArrowTransforms.transfer.set(arrow.x_04, arrow.y_06,  textboxText.z_0c);
        RENDERER.queueOrthoOverlayModel(textboxArrowObjs[arrow.spriteIndex_08], textboxArrowTransforms);
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
    struct4c.type_04 = TextboxType.fromInt(script.params_20[1].get());
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

    textbox.delete();

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

      textbox.delete();

      setTextboxArrowPosition(i, false);
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a variable used in a textbox's text (<var:n>)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The variable index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value")
  @Method(0x80029e8cL)
  public static FlowControl scriptSetTextboxVariable(final RunningScript<?> script) {
    textboxVariables_800bdf10.get(Math.min(9, script.params_20[0].get())).set(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to textboxes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @Method(0x80029eccL)
  public static FlowControl FUN_80029ecc(final RunningScript<?> script) {
    final TextboxText84 textboxText = textboxText_800bdf38[script.params_20[0].get()];
    if(textboxText.state_00 == TextboxTextState._16 && (textboxText.flags_08 & 0x20) != 0) {
      textboxText.flags_08 ^= 0x20;
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
    //LAB_8002a10c
    for(int i = 0; i < 8; i++) {
      final Textbox4c struct4c = textboxes_800be358[i];

      if(struct4c.state_00 != TextboxState.UNINITIALIZED_0 && (struct4c.flags_08 & Textbox4c.RENDER_BACKGROUND) != 0) {
        renderTextboxBackground(i);
      }
    }

    for(int i = 0; i < 8; i++) {
      final TextboxText84 text = textboxText_800bdf38[i];

      //LAB_8002a134
      if(text.state_00 != TextboxTextState.UNINITIALIZED_0) {
        switch(text.state_00) {
          case _18 -> renderTextboxSelection(i, text.selectionLine_60);
          case _19, SELECTION_22 -> renderTextboxSelection(i, text.selectionLine_68);
        }

        renderTextboxText(i);
        renderTextboxArrow(i);
      }

      //LAB_8002a154
    }
  }

  @Method(0x8002a180L)
  public static void appendTextboxChar(final int textboxIndex, final int charX, final int charY, int colour, final int lodChar) {
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    final int charIndex = textboxText.charY_36 * textboxText.chars_1c + textboxText.charX_34;
    final TextboxChar08 chr = textboxText.chars_58[charIndex];
    chr.delete();
    chr.x_00 = charX;
    chr.y_02 = charY;

    if((textboxText.flags_08 & TextboxText84.HAS_NAME) != 0 && charY == 0) {
      colour = 8;
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

    setCharMetrics(chr.char_06);
    final int u = textCol_800be5c0 * 16;
    final int v = textRow_800be5c8 * 12;

    chr.obj = new QuadBuilder()
      .bpp(Bpp.BITS_4)
      .clut(832 + chr.colour_04 * 16, 480)
      .vramPos(832, 256)
      .uv(u, v)
      .size(8, 12)
      .build();
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
    struct.type_04 = animateInOut ? TextboxType.ANIMATE_IN_OUT : TextboxType.NORMAL;
    struct.renderBorder_06 = true;
    struct.flags_08 |= Textbox4c.NO_ANIMATE_OUT;

    struct.x_14 = x;
    struct.y_16 = y;
    struct.chars_18 = chars + 1;
    struct.lines_1a = lines + 1;
  }

  @Method(0x8002a3ecL)
  public static void FUN_8002a3ec(final int textboxIndex, final int mode) {
    if(mode == 0) {
      //LAB_8002a40c
      textboxText_800bdf38[textboxIndex].state_00 = TextboxTextState.UNINITIALIZED_0;
      textboxes_800be358[textboxIndex].state_00 = TextboxState.UNINITIALIZED_0;
    } else {
      //LAB_8002a458
      textboxes_800be358[textboxIndex].state_00 = TextboxState._3;
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
        if((tickCount_800bb0fc.get() & 0x1) == 0) {
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
    textCol_800be5c0 = chr & 0xf;
    textRow_800be5c8 = chr % 208 / 16;
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
  public static void FUN_8002a9c0() {
    submapCut_80052c30.set(675);
    submapScene_80052c34.set(4);
    index_80052c38.set(0);
    submapCut_80052c3c.set(-1);
    _80052c40.set(false);
    submapEnvState_80052c44.set(2);
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
