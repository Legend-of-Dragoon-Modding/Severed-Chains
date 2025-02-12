package legend.game.submap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import legend.core.Config;
import legend.core.QueuedModel;
import legend.core.QueuedModelStandard;
import legend.core.QueuedModelTmd;
import legend.core.gpu.Bpp;
import legend.core.gpu.Rect4i;
import legend.core.gpu.VramTextureLoader;
import legend.core.gpu.VramTextureSingle;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdWithId;
import legend.core.memory.Method;
import legend.core.memory.types.IntRef;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.core.opengl.TmdObjLoader;
import legend.game.modding.events.submap.SubmapEncounterRateEvent;
import legend.game.modding.events.submap.SubmapEnvironmentTextureEvent;
import legend.game.modding.events.submap.SubmapGenerateEncounterEvent;
import legend.game.modding.events.submap.SubmapObjectTextureEvent;
import legend.game.scripting.ScriptFile;
import legend.game.tim.Tim;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.CContainer;
import legend.game.types.GsRVIEW2;
import legend.game.types.Model124;
import legend.game.types.MoonMusic08;
import legend.game.types.NewRootStruct;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static legend.core.Async.allLoaded;
import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadMusicPackage;
import static legend.game.Scus94491BpeSegment.loadSubmapSounds;
import static legend.game.Scus94491BpeSegment.orderingTableBits_1f8003c0;
import static legend.game.Scus94491BpeSegment.startCurrentMusicSequence;
import static legend.game.Scus94491BpeSegment.stopAndResetSoundsAndSequences;
import static legend.game.Scus94491BpeSegment.stopCurrentMusicSequence;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.animateModel;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsSetSmapRefView2L;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCutBeforeBattle_80052c3c;
import static legend.game.Scus94491BpeSegment_8005.submapEnvState_80052c44;
import static legend.game.Scus94491BpeSegment_8005.submapMusic_80050068;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.musicLoaded_800bd782;
import static legend.game.Scus94491BpeSegment_800b.previousSubmapCut_800bda08;
import static legend.game.Scus94491BpeSegment_800b.projectionPlaneDistance_800bd810;
import static legend.game.Scus94491BpeSegment_800b.rview2_800bd7e8;
import static legend.game.Scus94491BpeSegment_800b.soundFiles_800bcf80;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL12C.GL_UNSIGNED_INT_8_8_8_8_REV;

public class RetailSubmap extends Submap {
  private static final Logger LOGGER = LogManager.getFormatterLogger(RetailSubmap.class);

  public final int cut;
  private final NewRootStruct newRoot;
  private final Vector2f screenOffset;
  private final CollisionGeometry collisionGeometry;

  private final List<Tim> pxls = new ArrayList<>();

  private final boolean hasRenderer_800c6968;

  private int submapOffsetX_800cb560;
  private int submapOffsetY_800cb564;

  private int _800cb570;
  private int _800cb574;
  private int _800cb578;
  private int envBackgroundTextureCount_800cb57c;
  private int envForegroundTextureCount_800cb580;
  private int envTextureCount_800cb584;

  private final EnvironmentForegroundTextureMetrics[] envForegroundMetrics_800cb590 = new EnvironmentForegroundTextureMetrics[32];
  private final EnvironmentRenderingMetrics24[] envRenderMetrics_800cb710 = new EnvironmentRenderingMetrics24[32];
  {
    Arrays.setAll(this.envForegroundMetrics_800cb590, i -> new EnvironmentForegroundTextureMetrics());
    Arrays.setAll(this.envRenderMetrics_800cb710, i -> new EnvironmentRenderingMetrics24());
  }

  private final Vector3f[] cutoutWorldToScreenZTransformVectors_800cbb90 = new Vector3f[32];
  {
    Arrays.setAll(this.cutoutWorldToScreenZTransformVectors_800cbb90, i -> new Vector3f());
  }
  private final float[] cutoutScreenZs_800cbc90 = new float[32];

  private final GsRVIEW2 rview2_800cbd10 = new GsRVIEW2();
  private int _800cbd30;
  private int _800cbd34;

  private int minSobj_800cbd60;
  private int maxSobj_800cbd64;

  private final Matrix4f submapCutMatrix_800d4bb0 = new Matrix4f();

  private TheEndEffectDatab0 theEnd_800d4bd0;

  private CContainer submapCutModel;
  private TmdAnimationFile submapCutAnim;

  private final Model124 submapModel_800d4bf8 = new Model124("Submap");

  private boolean _800f7f0c;

  private Tim[] envTextures;
  private Texture backgroundTexture;
  private Rect4i backgroundRect;
  private Obj backgroundObj;
  private final MV backgroundTransforms = new MV();
  private Texture[] foregroundTextures;
  private final Int2ObjectMap<Consumer<Texture.Builder>> sobjTextureOverrides = new Int2ObjectOpenHashMap<>();

  public RetailSubmap(final int cut, final NewRootStruct newRoot, final Vector2f screenOffset, final CollisionGeometry collisionGeometry) {
    this.cut = cut;
    this.newRoot = newRoot;

    this.hasRenderer_800c6968 = submapTypes_800f5cd4[cut] == 65;
    this.screenOffset = screenOffset;
    this.collisionGeometry = collisionGeometry;

    this.loadCollisionAndTransitions();
  }

  @Override
  public void loadEnv(final Runnable onLoaded) {
    LOGGER.info("Loading submap cut %d environment", this.cut);

    final IntRef drgnIndex = new IntRef();
    final IntRef fileIndex = new IntRef();

    this.newRoot.getDrgnFile(this.cut, drgnIndex, fileIndex);

    drgnBinIndex_800bc058 = drgnIndex.get();
    loadDrgnDir(2, fileIndex.get(), files -> {
      this.loadBackground("DRGN2" + drgnIndex.get() + "/" + fileIndex.get(), files);
      onLoaded.run();
    });
  }

  @Override
  public void loadAssets(final Runnable onLoaded) {
    LOGGER.info("Loading submap cut %d assets", this.cut);

    this.theEnd_800d4bd0 = null;

    if(this.cut == 673) { // End cutscene
      this.theEnd_800d4bd0 = new TheEndEffectDatab0();
    }

    //LAB_800edeb4
    final IntRef drgnIndex = new IntRef();
    final IntRef fileIndex = new IntRef();

    this.newRoot.getDrgnFile(this.cut, drgnIndex, fileIndex);

    if(drgnIndex.get() == 1 || drgnIndex.get() == 2 || drgnIndex.get() == 3 || drgnIndex.get() == 4) {
      final int cutFileIndex = smapFileIndices_800f982c[this.cut];

      final AtomicInteger loadedCount = new AtomicInteger();
      final int expectedCount = cutFileIndex == 0 ? 1 : 2;

      // Load sobj assets
      final List<FileData> assets = new ArrayList<>();
      final List<FileData> scripts = new ArrayList<>();
      final List<FileData> textures = new ArrayList<>();
      final AtomicInteger assetsCount = new AtomicInteger();

      final Runnable prepareSobjs = () -> this.prepareSobjs(assets, scripts, textures);
      final Runnable prepareSobjsAndComplete = () -> allLoaded(loadedCount, expectedCount, prepareSobjs, onLoaded);

      loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 1, files -> allLoaded(assetsCount, 3, () -> assets.addAll(files), prepareSobjsAndComplete));
      loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 2, files -> allLoaded(assetsCount, 3, () -> scripts.addAll(files), prepareSobjsAndComplete));
      Loader.loadDirectory("SECT/DRGN" + (20 + drgnIndex.get()) + ".BIN/" + (fileIndex.get() + 1) + "/textures", files -> allLoaded(assetsCount, 3, () -> textures.addAll(files), prepareSobjsAndComplete));

      // Load 3D overlay
      if(cutFileIndex != 0) {
        // Using arrays here to have a constant pointer for the callbacks
        final Tim[] submapCutTexture = new Tim[1];
        final MV[] submapCutMatrix = new MV[1];
        final AtomicInteger cutCount = new AtomicInteger();
        final int expectedCutCount = this.cut == 673 ? 3 : 2;

        final Runnable prepareMap = () -> this.prepareMap(submapCutTexture[0], submapCutMatrix[0]);
        final Runnable prepareMapAndComplete = () -> allLoaded(loadedCount, expectedCount, prepareMap, onLoaded);

        if(this.cut == 673) { // End cutscene, loads "The End" TIM
          loadDrgnFile(0, 7610, file -> allLoaded(cutCount, expectedCutCount, () -> {
            LOGGER.info("Submap cut %d the end texture loaded", this.cut);
            this.theEnd_800d4bd0.setTim(new Tim(file));
          }, prepareMapAndComplete));
        }

        // File example: 7508
        LOGGER.info("Loading submap cut %d overlay model file %d", this.cut, cutFileIndex);
        loadDrgnDir(0, cutFileIndex, files -> allLoaded(cutCount, expectedCutCount, () -> {
          LOGGER.info("Submap cut %d overlay model loaded", this.cut);
          this.submapCutModel = new CContainer("DRGN0/" + cutFileIndex, files.get(0));
          this.submapCutAnim = new TmdAnimationFile(files.get(1));
        }, prepareMapAndComplete));

        LOGGER.info("Loading submap cut %d overlay texture and matrix file %d", this.cut, cutFileIndex + 1);
        loadDrgnDir(0, cutFileIndex + 1, files -> allLoaded(cutCount, expectedCutCount, () -> {
          LOGGER.info("Submap cut %d overlay texture and matrix loaded", this.cut);
          submapCutTexture[0] = new Tim(files.get(0));
          submapCutMatrix[0] = files.get(1).readMv(0, new MV());
        }, prepareMapAndComplete));
      }
    }
  }

  @Override
  public void loadMusicAndSounds() {
    final int oldSubmapId = submapId_800bd808;
    submapId_800bd808 = this.cutToSubmap_800d610c[this.cut];

    if(submapId_800bd808 != oldSubmapId) {
      stopAndResetSoundsAndSequences();
      unloadSoundFile(4);
      loadSubmapSounds(submapId_800bd808);
    }

    if(submapId_800bd808 != oldSubmapId || previousSubmapCut_800bda08 != this.cut) {
      musicLoaded_800bd782 = false;
      this.startMusic();
    }
  }

  @Override
  public void startMusic() {
    final int musicIndex = this.getSubmapMusicChange();
    if(musicIndex == -1) {
      stopCurrentMusicSequence();
      musicLoaded_800bd782 = true;
    } else if(musicIndex == -2) {
      startCurrentMusicSequence();
      musicLoaded_800bd782 = true;
    } else if(musicIndex == -3) {
      musicLoaded_800bd782 = true;
    } else {
      loadMusicPackage(musicIndex);
    }
  }

  @Method(0x800ea84cL)
  private void updateCollidedPrimitiveIndexAndCheckForWorldMapTransition(final SubmapObject210 sobj, final MapTransitionData4c mapTransitionData) {
    if(mapTransitionData.shouldUpdateCollidedWith_44) {
      collidedPrimitiveIndex_80052c38 = sobj.collidedPrimitiveIndex_16c;

      //LAB_800ea8d4
      for(int i = 0; i < mapTransitionData.worldMapExitCount_40; i++) {
        if(collidedPrimitiveIndex_80052c38 == mapTransitionData.worldMapExitPrimitiveIndices_00[i]) {
          mapTransitionData.shouldUpdateCollidedWith_44 = false;
          break;
        }

        //LAB_800ea8ec
      }
    }

    //LAB_800ea8fc
  }

  @Method(0x800ea90cL)
  private void updateCollidedPrimitiveIndex(final SubmapObject210 sobj, final MapTransitionData4c mapTransitionData) {
    collidedPrimitiveIndex_80052c38 = sobj.collidedPrimitiveIndex_16c;
  }

  @Override
  @Method(0x800ea974L)
  public void loadMapTransitionData(final MapTransitionData4c transitionData) {
    //LAB_800ea9a4
    transitionData.clear();

    //LAB_800ea9d8
    if(this.cut != 0) {
      for(final SubmapWorldMapExits exit : this.submapWorldMapExits_800f7f74) {
        if(exit.submapCut_04 == this.cut) {
          transitionData.worldMapExitPrimitiveIndices_00[transitionData.worldMapExitCount_40] = exit.primitiveIndex_06;
          transitionData.worldMapExitCount_40++;
        }
      }
    }

    //LAB_800eaa30
    if(transitionData.worldMapExitCount_40 != 0) {
      transitionData.collidedPrimitiveSetterCallback_48 = this::updateCollidedPrimitiveIndexAndCheckForWorldMapTransition;
      transitionData.shouldUpdateCollidedWith_44 = true;
    } else {
      //LAB_800eaa5c
      transitionData.collidedPrimitiveSetterCallback_48 = this::updateCollidedPrimitiveIndex;
    }
  }

  @Override
  void applyCollisionDebugColour(final int collisionPrimitiveIndex, final QueuedModel model) {
    for(int n = 0; n < this.submapWorldMapExits_800f7f74.length; n++) {
      final SubmapWorldMapExits worldMapExits = this.submapWorldMapExits_800f7f74[n];

      if(worldMapExits.submapCut_04 == this.cut && collisionPrimitiveIndex == worldMapExits.primitiveIndex_06) {
        model.colour(1.0f, 0.0f, 1.0f);
        break;
      }
    }
  }

  @Override
  public void draw() {
    if(!this.hasRenderer_800c6968) {
      return;
    }

    if(this.theEnd_800d4bd0 != null) {
      this.theEnd_800d4bd0.render();
      GPU.uploadData15(this.theEnd_800d4bd0.getClutRect(), this.theEnd_800d4bd0.getClutData());
    }
  }

  @Override
  public void unload() {
    previousSubmapCut_800bda08 = this.cut;

    if(this.theEnd_800d4bd0 != null) {
      this.theEnd_800d4bd0.deallocate();
      this.theEnd_800d4bd0 = null;
    }

    this.submapModel_800d4bf8.deleteModelParts();

    if(this.backgroundObj != null) {
      this.backgroundObj.delete();
      this.backgroundObj = null;
    }

    if(this.backgroundTexture != null) {
      this.backgroundTexture.delete();
      this.backgroundTexture = null;
    }

    for(int i = 0; i < this.foregroundTextures.length; i++) {
      if(this.foregroundTextures[i] != null) {
        this.foregroundTextures[i].delete();
      }
    }

    this.foregroundTextures = null;

    for(final EnvironmentRenderingMetrics24 metrics : this.envRenderMetrics_800cb710) {
      if(metrics.obj != null) {
        metrics.obj.delete();
        metrics.obj = null;
      }
    }

    this.submapCutModel = null;
    this.submapCutAnim = null;
  }

  @Override
  public int getEncounterRate() {
    final var encounterRate = encounterData_800f64c4[this.cut].rate_02;
    final var encounterRateEvent = EVENTS.postEvent(new SubmapEncounterRateEvent(encounterRate, this.cut));

    return encounterRateEvent.encounterRate;
  }

  @Override
  public void prepareEncounter(final int encounterId, final boolean useBattleStage) {
    final var sceneId = encounterData_800f64c4[this.cut].scene_00;
    final var scene = sceneEncounterIds_800f74c4[sceneId];
    final var battleStageId = useBattleStage ? battleStage_800bb0f4 : encounterData_800f64c4[this.cut].stage_03;

    final var generateEncounterEvent = EVENTS.postEvent(new SubmapGenerateEncounterEvent(encounterId, battleStageId, this.cut, sceneId, scene));
    encounterId_800bb0f8 = generateEncounterEvent.encounterId;
    battleStage_800bb0f4 = generateEncounterEvent.battleStageId;

    if(Config.combatStage()) {
      battleStage_800bb0f4 = Config.getCombatStage();
    }
  }

  @Override
  public void prepareEncounter(final boolean useBattleStage) {
    final var sceneId = encounterData_800f64c4[this.cut].scene_00;
    final var scene = sceneEncounterIds_800f74c4[sceneId];
    final var encounterId = scene[this.randomEncounterIndex()];
    this.prepareEncounter(encounterId, useBattleStage);
  }

  @Override
  public void storeStateBeforeBattle() {
    submapCutBeforeBattle_80052c3c = this.cut;
  }

  @Override
  public boolean isReturningToSameMapAfterBattle() {
    return submapCutBeforeBattle_80052c3c == this.cut;
  }

  private void prepareSobjs(final List<FileData> assets, final List<FileData> scripts, final List<FileData> textures) {
    LOGGER.info("Submap cut %d preparing sobjs", this.cut);

    final int objCount = scripts.size() - 2;

    this.script = new ScriptFile("Submap controller", scripts.get(0).getBytes());

    for(int objIndex = 0; objIndex < objCount; objIndex++) {
      final byte[] scriptData = scripts.get(objIndex + 1).getBytes();

      final FileData submapModel = assets.get(objIndex * 33);

      final IntRef drgnIndex = new IntRef();
      final IntRef fileIndex = new IntRef();
      this.newRoot.getDrgnFile(this.cut, drgnIndex, fileIndex);

      final SubmapObject obj = new SubmapObject();
      obj.script = new ScriptFile("Submap object %d (DRGN%d/%d/%d)".formatted(objIndex, drgnIndex.get(), fileIndex.get() + 2, objIndex + 1), scriptData);

      if(submapModel.hasVirtualSize() && submapModel.real()) {
        obj.model = new CContainer("Submap object %d (DRGN%d/%d/%d)".formatted(objIndex, drgnIndex.get(), fileIndex.get() + 1, objIndex * 33), new FileData(submapModel.getBytes()));
      } else {
        obj.model = null;
      }

      for(int animIndex = objIndex * 33 + 1; animIndex < (objIndex + 1) * 33; animIndex++) {
        final FileData data = assets.get(animIndex);

        // This is a stupid fix for a stupid retail bug where almost all
        // sobj animations in DRGN24.938 are symlinked to a PXL file
        // GH#292
        if(data.readInt(0) == 0x11) {
          obj.animations.add(null);
          continue;
        }

        obj.animations.add(new TmdAnimationFile(data));
      }

      this.objects.add(obj);
    }

    // Get models that are symlinked
    for(int objIndex = 0; objIndex < objCount; objIndex++) {
      final SubmapObject obj = this.objects.get(objIndex);

      if(obj.model == null) {
        final FileData submapModel = assets.get(objIndex * 33);

        obj.model = this.objects.get(submapModel.realFileIndex() / 33).model;
      }
    }

    for(final FileData file : textures) {
      if(file != null && file.real()) {
        this.pxls.add(new Tim(file));
      } else {
        this.pxls.add(null);
      }
    }

    for(int i = 0; i < textures.size(); i++) {
      if(this.pxls.get(i) == null && textures.get(i) != null) {
        this.pxls.set(i, this.pxls.get(textures.get(i).realFileIndex()));
      }
    }

    this.loadTextureOverrides();
    this.calculateTextureLocations();
    this.loadTextures();
  }

  private void prepareMap(final Tim submapCutTexture, final MV submapCutMatrix) {
    LOGGER.info("Submap cut %d preparing map", this.cut);

    if(this.cut == 673) { // End cutscene
      this.theEnd_800d4bd0.uploadTheEndTim();
    }

    GPU.uploadData15(new Rect4i(1008, 256, submapCutTexture.getImageRect().w, submapCutTexture.getImageRect().h), submapCutTexture.getImageData());

    // The submap cut model is rendered without using the camera matrix, so we multiply its transforms
    // by the inverse of the camera matrix to cancel out the camera multiplication in the shader
    final Matrix4f inverseW2s = new Matrix4f(worldToScreenMatrix_800c3548).setTranslation(worldToScreenMatrix_800c3548.transfer)
      .invert();
    this.submapCutMatrix_800d4bb0
      .set(submapCutMatrix).setTranslation(submapCutMatrix.transfer)
      .mulLocal(inverseW2s);

    this.submapModel_800d4bf8.uvAdjustments_9d = new UvAdjustmentMetrics14(17, 1008, 256, true);
    initModel(this.submapModel_800d4bf8, this.submapCutModel, this.submapCutAnim);
  }

  @Override
  public void prepareSobjModel(final SubmapObject210 sobj) {
    if(this.sobjTextureOverrides.containsKey(sobj.sobjIndex_12e)) {
      sobj.texture = Texture.create(this.sobjTextureOverrides.get(sobj.sobjIndex_12e));
      final Tim oldTexture = this.pxls.get(sobj.sobjIndex_12e);
      TmdObjLoader.fromModel("SobjModel (index " + sobj.sobjIndex_12e + ')', sobj.model_00, oldTexture.getImageRect().w * oldTexture.getBpp().widthDivisor, oldTexture.getImageRect().h);
    } else {
      TmdObjLoader.fromModel("SobjModel (index " + sobj.sobjIndex_12e + ')', sobj.model_00);
    }
  }

  private void loadTextureOverrides() {
    this.sobjTextureOverrides.clear();
    this.sobjTextureOverrides.putAll(EVENTS.postEvent(new SubmapObjectTextureEvent(drgnBinIndex_800bc058, this.cut)).textures);
  }

  private void calculateTextureLocations() {
    this.uvAdjustments.clear();

    final boolean[] usedSlots = new boolean[this.pxls.size() * 2];
    final Set<Tim> visited = new HashSet<>();

    outer:
    for(int pxlIndex = 0; pxlIndex < this.pxls.size(); pxlIndex++) {
      // sobj 16 uses the submap overlay texture
      if(pxlIndex == 16) {
        this.uvAdjustments.add(new UvAdjustmentMetrics14(pxlIndex + 1, 1008, 256, true));
        continue;
      }

      final Tim tim = this.pxls.get(pxlIndex);

      if(!visited.contains(tim)) {
        visited.add(tim);

        final int neededSlots = tim.getImageRect().w / 16;

        // We increment by neededSlots so that wide textures only land on even slots
        for(int slotIndex = 0; slotIndex < 20; slotIndex += neededSlots) {
          boolean free = true;
          for(int i = 0; i < neededSlots; i++) {
            if(usedSlots[slotIndex + i]) {
              free = false;
              break;
            }
          }

          if(free) {
            for(int i = 0; i < neededSlots; i++) {
              usedSlots[slotIndex + i] = true;
            }

            final int x = 576 + slotIndex % 12 * 16;
            final int y = 256 + slotIndex / 12 * 128;

            if(this.sobjTextureOverrides.containsKey(pxlIndex)) {
              this.uvAdjustments.add(UvAdjustmentMetrics14.PNG);
            } else {
              this.uvAdjustments.add(new UvAdjustmentMetrics14(pxlIndex + 1, x, y, pxlIndex != 17 && pxlIndex != 18));
            }

            continue outer;
          }
        }

        throw new RuntimeException("Failed to find available texture slot for sobj texture " + pxlIndex);
      } else {
        this.uvAdjustments.add(UvAdjustmentMetrics14.NONE);
      }
    }
  }

  private void loadTextures() {
    final Set<Tim> visited = new HashSet<>();

    for(final UvAdjustmentMetrics14 uvAdjustment : this.uvAdjustments) {
      if(uvAdjustment.index != 0) {
        final Tim tim = this.pxls.get(uvAdjustment.index - 1);

        if(tim != null && !visited.contains(tim)) {
          visited.add(tim);

          final Rect4i imageRect = tim.getImageRect();
          final Rect4i clutRect = tim.getClutRect();

          imageRect.x = uvAdjustment.tpageX;
          imageRect.y = uvAdjustment.tpageY;
          clutRect.x = uvAdjustment.clutX;
          clutRect.y = uvAdjustment.clutY;

          GPU.uploadData15(imageRect, tim.getImageData());
          GPU.uploadData15(clutRect, tim.getClutData());
        }
      }
    }

    if(this.cut == 673) {
      GPU.downloadData15(this.theEnd_800d4bd0.getClutRect(), this.theEnd_800d4bd0.getClutData());
      this.theEnd_800d4bd0.initFlameClutAnimation();
    }
  }

  @Method(0x800e49a4L)
  private int randomEncounterIndex() {
    final int rand = rand();

    if(rand < 0x2ccc) {
      return 0;
    }

    if(rand < 0x5999) {
      return 1;
    }

    if(rand < 0x7333) {
      return 2;
    }

    return 3;
  }

  @Method(0x800e5330L)
  private void loadBackground(final String mapName, final List<FileData> files) {
    this.envTextures = new Tim[files.size() - 3];

    //LAB_800e5374
    for(int i = 0; i < files.size() - 3; i++) {
      this.envTextures[i] = new Tim(files.get(i + 3));
      this.envTextures[i].uploadToGpu();
    }

    //LAB_800e5430
    this.loadEnvironment(new EnvironmentFile(files.get(0)));
    this.collisionGeometry.loadCollision(new TmdWithId("Background " + mapName, files.get(2)), files.get(1));

    submapEnvState_80052c44 = SubmapEnvState.CHECK_TRANSITIONS_1_2;
  }

  @Method(0x800e664cL)
  private void loadCollisionAndTransitions() {
    this.collisionGeometry.clearCollisionAndTransitionInfo();

    final SubmapCutInfo entry = this.newRoot.submapCutInfo_0000[this.cut];
    final short offset = entry.collisionAndTransitionOffset_04;

    if(offset < 0) {
      return;
    }

    //LAB_800e66dc
    for(int i = 0; i < entry.collisionAndTransitionCount_06; i++) {
      this.collisionGeometry.setCollisionAndTransitionInfo(this.newRoot.collisionAndTransitions_2000[offset / 4 + i]);
    }

    //LAB_800e671c
  }

  /** We stitch all the backgrounds together and make every foreground the full size of the background so that no lines appear between pieces */
  @Override
  public void prepareEnv() {
    LOGGER.info("Submap cut %d preparing environment", this.cut);

    final Tim[] tims = new Tim[this.envTextureCount_800cb584];
    final Rect4i[] rects = new Rect4i[this.envTextureCount_800cb584];

    for(int i = 0; i < this.envTextureCount_800cb584; i++) {
      final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[i];
      for(int textureIndex = 0; textureIndex < this.envTextures.length; textureIndex++) {
        final Tim texture = this.envTextures[textureIndex];
        final Rect4i bounds = texture.getImageRect();

        final int tpX = (metrics.tpage_04 & 0b1111) * 64;
        final int tpY = (metrics.tpage_04 & 0b10000) != 0 ? 256 : 0;

        if(bounds.contains(tpX, tpY)) {
          tims[i] = texture;
          rects[i] = new Rect4i(metrics.offsetX_1c, metrics.offsetY_1e, metrics.w_18, metrics.h_1a);
          break;
        }
      }

      if(rects[i] == null) {
        LOGGER.warn("Failed to find texture for env slot %d", i);
      }
    }

    final SubmapEnvironmentTextureEvent event = EVENTS.postEvent(new SubmapEnvironmentTextureEvent(drgnBinIndex_800bc058, this.cut, this.envForegroundTextureCount_800cb580));

    this.backgroundRect = Rect4i.bound(rects);
    final int[] empty = new int[this.backgroundRect.w * this.backgroundRect.h];

    if(event.background != null) {
      this.backgroundTexture = event.background;
    } else {
      this.backgroundTexture = Texture.create(builder -> {
        builder.data(empty, this.backgroundRect.w, this.backgroundRect.h);
        builder.internalFormat(GL_RGBA);
        builder.dataFormat(GL_RGBA);
        builder.dataType(GL_UNSIGNED_INT_8_8_8_8_REV);
      });

      // Arrange the segments of the background textures into one texture
      for(int i = 0; i < this.envBackgroundTextureCount_800cb57c; i++) {
        if(tims[i] != null) {
          final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[i];
          final VramTextureSingle texture = (VramTextureSingle)VramTextureLoader.textureFromTim(tims[i]);
          final VramTextureSingle palette = (VramTextureSingle)VramTextureLoader.palettesFromTim(tims[i])[0];

          final Rect4i rect = rects[i];
          final int[] data = texture.applyPalette(palette, new Rect4i(metrics.u_14, metrics.v_15, rect.w, rect.h));

          // Set alpha so the fragments don't get culled
          for(int n = 0; n < data.length; n++) {
            if(data[n] != 0) {
              data[n] |= 0xff << 24;
            }
          }

          this.backgroundTexture.data(metrics.offsetX_1c - this.backgroundRect.x, metrics.offsetY_1e - this.backgroundRect.y, rect.w, rect.h, data);
        }
      }
    }

    this.foregroundTextures = event.foregrounds;

    // Create one texture per foreground and position the foreground in the correct spot
    for(int i = 0; i < this.envForegroundTextureCount_800cb580; i++) {
      if(this.foregroundTextures[i] == null && tims[this.envBackgroundTextureCount_800cb57c + i] != null) {
        final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i];
        final VramTextureSingle texture = (VramTextureSingle)VramTextureLoader.textureFromTim(tims[this.envBackgroundTextureCount_800cb57c + i]);
        final VramTextureSingle palette = (VramTextureSingle)VramTextureLoader.palettesFromTim(tims[this.envBackgroundTextureCount_800cb57c + i])[0];

        final Rect4i rect = rects[this.envBackgroundTextureCount_800cb57c + i];
        final Rect4i appliedRect = new Rect4i(metrics.u_14, metrics.v_15, rect.w, rect.h);

        // Neet flashback in lumberjack's shack (DRGN21/712) has a busted cutout that's way taller than the texture
        if(appliedRect.right() > texture.rect.w) {
          appliedRect.w = texture.rect.w - appliedRect.x;
        }

        if(appliedRect.bottom() > texture.rect.h) {
          appliedRect.h = texture.rect.h - appliedRect.y;
        }

        final int[] data = texture.applyPalette(palette, appliedRect);

        // Set alpha so the fragments don't get culled
        for(int n = 0; n < data.length; n++) {
          if(data[n] != 0) {
            data[n] |= 0xff << 24;
          }
        }

        this.foregroundTextures[i] = Texture.create(builder -> {
          builder.data(empty, this.backgroundRect.w, this.backgroundRect.h);
          builder.internalFormat(GL_RGBA);
          builder.dataFormat(GL_RGBA);
          builder.dataType(GL_UNSIGNED_INT_8_8_8_8_REV);
        });

        this.foregroundTextures[i].data(metrics.offsetX_1c - this.backgroundRect.x, metrics.offsetY_1e - this.backgroundRect.y, appliedRect.w, appliedRect.h, data);
      }
    }

    this.envTextures = null;
  }

  @Override
  public void finishLoading() {
    this.resetSobjBounds();
  }

  @Method(0x800e6d58L)
  private boolean FUN_800e6d58() {
    for(int i = 0; i < 45; i++) {
      if(this._800f7e58[i] == this.cut) {
        return true;
      }
    }

    return false;
  }

  @Method(0x800e6d9cL)
  private void calculateSubmapBounds(final EnvironmentTextureMetrics24[] envs, final int backgroundTextureCount) {
    int left = 0x7fff;
    int right = -0x8000;
    int top = 0x7fff;
    int bottom = -0x8000;

    //LAB_800e6dc8
    for(int i = 0; i < backgroundTextureCount; i++) {
      final EnvironmentTextureMetrics24 env = envs[i];

      if(env.tileType_06 == 0x4e) {
        if(right < env.vramPos_08.w + env.textureOffsetX_10) {
          right = env.vramPos_08.w + env.textureOffsetX_10;
        }

        if(left > env.textureOffsetX_10) {
          left = env.textureOffsetX_10;
        }

        if(bottom < env.vramPos_08.h + env.textureOffsetY_12) {
          bottom = env.vramPos_08.h + env.textureOffsetY_12;
        }

        if(top > env.textureOffsetY_12) {
          top = env.textureOffsetY_12;
        }
      }

      //LAB_800e6e64
    }

    //LAB_800e6e74
    final int width = right - left;
    final int height = bottom - top;
    this.submapOffsetX_800cb560 = -width / 2;
    this.submapOffsetY_800cb564 = -height / 2;
    this._800cb570 = (width - 384) / 2;

    if(width == 384 && height == 256 || this.FUN_800e6d58()) {
      this._800cb574 = (376 - width) / 2;
    } else {
      this._800cb574 = -this._800cb570;
    }

    this._800cb578 = (height - 240) / 2;
  }

  @Method(0x800e6f38L)
  private void buildBackgroundRenderingPacket(final EnvironmentTextureMetrics24[] env) {
    //LAB_800e6f9c
    for(int i = 0; i < this.envTextureCount_800cb584; i++) {
      final EnvironmentTextureMetrics24 envTexture = env[i];

      int clutY = Math.abs(envTexture.clutY_22); // Negative means translucent
      if(i < this.envBackgroundTextureCount_800cb57c) { // It's a background texture
        if(clutY < 0x1f0 || clutY >= 0x200) {
          clutY = i + 0x1f0;
        }
      } else { // It's a foreground texture
        //LAB_800e7010
        this.cutoutWorldToScreenZTransformVectors_800cbb90[i - this.envBackgroundTextureCount_800cb57c].set(envTexture.worldToScreenZTransformVector_14);
        this.cutoutScreenZs_800cbc90[i - this.envBackgroundTextureCount_800cb57c] = envTexture.screenZ;
      }

      //LAB_800e7004
      //LAB_800e7074
      final EnvironmentRenderingMetrics24 renderPacket = this.envRenderMetrics_800cb710[i];

      float z;
      if(envTexture.tileType_06 == 0x4e) {
        //LAB_800e7148
        z = (0x1 << orderingTableBits_1f8003c0) - 1;
      } else if(envTexture.tileType_06 == 0x4f) {
        z = 40;
      } else {
        //LAB_800e7194
        z =
          worldToScreenMatrix_800c3548.m02 * envTexture.worldPosition_00.x +
          worldToScreenMatrix_800c3548.m12 * envTexture.worldPosition_00.y +
          worldToScreenMatrix_800c3548.m22 * envTexture.worldPosition_00.z;
        z += worldToScreenMatrix_800c3548.transfer.z;
        z /= 1 << 16 - orderingTableBits_1f8003c0;
      }

      renderPacket.z_20 = Math.round(z);
      renderPacket.tpage_04 = envTexture.tpage_20;
      renderPacket.r_0c = 0x80;
      renderPacket.g_0d = 0x80;
      renderPacket.b_0e = 0x80;
      renderPacket.u_14 = envTexture.vramPos_08.x;
      renderPacket.v_15 = envTexture.vramPos_08.y;
      renderPacket.clut_16 = clutY << 6 | 0x30;
      renderPacket.w_18 = envTexture.vramPos_08.w;
      renderPacket.h_1a = envTexture.vramPos_08.h;

      //LAB_800e70ec
      renderPacket.offsetX_1c = envTexture.textureOffsetX_10;
      renderPacket.offsetY_1e = envTexture.textureOffsetY_12;

      //LAB_800e7210
      renderPacket.zFlags_22 &= 0x3fff;
    }

    //LAB_800e724c
    this.calculateSubmapBounds(env, this.envBackgroundTextureCount_800cb57c);
  }

  @Method(0x800e728cL)
  private void clearSmallValuesFromMatrix(final MV matrix) {
    //LAB_800e72b4
    for(int x = 0; x < 3; x++) {
      //LAB_800e72c4
      for(int y = 0; y < 3; y++) {
        if(Math.abs(matrix.get(x, y)) < 0.015625f) {
          matrix.set(x, y, 0.0f);
        }

        //LAB_800e72e8
      }
    }
  }

  @Method(0x800e7328L)
  private void updateCamera() {
    setProjectionPlaneDistance(projectionPlaneDistance_800bd810);
    GsSetSmapRefView2L(this.rview2_800cbd10);
    this.clearSmallValuesFromMatrix(worldToScreenMatrix_800c3548);
    rview2_800bd7e8.set(this.rview2_800cbd10);
  }

  @Method(0x800e7418L)
  private void updateRview2(final Vector3f viewpoint, final Vector3f refpoint, final int rotation, final int projectionDistance) {
    this.rview2_800cbd10.viewpoint_00.set(viewpoint);
    this.rview2_800cbd10.refpoint_0c.set(refpoint);
    this.rview2_800cbd10.viewpointTwist_18 = (short)rotation << 12;
    this.rview2_800cbd10.super_1c = null;
    projectionPlaneDistance_800bd810 = projectionDistance;

    this.updateCamera();
  }

  @Method(0x800e7500L)
  private void loadEnvironment(final EnvironmentFile envFile) {
    this.envTextureCount_800cb584 = envFile.allTextureCount_14;
    this.envBackgroundTextureCount_800cb57c = envFile.backgroundTextureCount_15;
    this.envForegroundTextureCount_800cb580 = envFile.foregroundTextureCount_16;

    this.updateRview2(
      envFile.viewpoint_00,
      envFile.refpoint_08,
      envFile.rotation_12,
      envFile.projectionDistance_10
    );

    this.buildBackgroundRenderingPacket(envFile.environments_18);

    for(final EnvironmentForegroundTextureMetrics struct : this.envForegroundMetrics_800cb590) {
      struct.clear();
    }
  }

  @Method(0x800e76b0L)
  public void setEnvForegroundPosition(final int x, final int y, final int index) {
    final EnvironmentForegroundTextureMetrics foreground = this.envForegroundMetrics_800cb590[index];

    if(x == 1024 && y == 1024) {
      foreground.hidden_08 = true;
      return;
    }

    //LAB_800e76e8
    //LAB_800e76ec
    foreground.startX = foreground.destX;
    foreground.startY = foreground.destY;
    foreground.destX = x;
    foreground.destY = y;
    foreground.ticksTotal = 3 - vsyncMode_8007a3b8;
    foreground.ticks = 0;

    if(!foreground.positionWasSet) {
      foreground.startX = x;
      foreground.startY = y;
      foreground.positionWasSet = true;
    }

    foreground.hidden_08 = false;
  }

  @Method(0x800e770cL)
  public void resetSobjBounds() {
    this.minSobj_800cbd60 = 0;
    this.maxSobj_800cbd64 = this.objects.size();
  }

  /**
   * Mode:
   * <ul>
   *   <li>0 - Static overlay depth from struct</li>
   *   <li>1 - Overlay depth calculated to relative sobj positions</li>
   *   <li>2 - Scripted static overlay depth (within bounds)</li>
   *   <li>5 - Doesn't appear to do anything because the values set only used if zFlags are set to 0x4000</li>
   * </ul>
   */
  @Method(0x800e7728L)
  public int setEnvironmentOverlayDepthModeAndZ(final int mode, final int foregroundTextureIndex, int z) {
    final int textureIndex = this.envBackgroundTextureCount_800cb57c + foregroundTextureIndex;

    if(mode == 1 && foregroundTextureIndex == -1) {
      //LAB_800e7780
      for(int i = 0; i < this.envForegroundTextureCount_800cb580; i++) {
        this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i].zFlags_22 &= 0x3fff;
        this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i].zFlags_22 |= 0x4000;
      }

      //LAB_800e77b4
      return this.envRenderMetrics_800cb710[textureIndex].z_20;
    }

    //LAB_800e77d8
    if(textureIndex >= this.envRenderMetrics_800cb710.length) {
      return -1;
    }

    if(mode == 0) {
      //LAB_800e7860
      this.envRenderMetrics_800cb710[textureIndex].zFlags_22 &= 0x3fff;
    } else if(mode == 1) {
      //LAB_800e77e8
      //LAB_800e7830
      this.envRenderMetrics_800cb710[textureIndex].zFlags_22 &= 0x3fff;
      this.envRenderMetrics_800cb710[textureIndex].zFlags_22 |= 0x4000;
      //LAB_800e7808
    } else if(mode == 2) {
      //LAB_800e788c
      this.envRenderMetrics_800cb710[textureIndex].zFlags_22 &= 0x3fff;
      this.envRenderMetrics_800cb710[textureIndex].zFlags_22 |= 0x8000;

      if(z < 40) {
        //LAB_800e78fc
        z = 40;
      } else if((0x1 << orderingTableBits_1f8003c0) - 1 < z) {
        z = (0x1 << orderingTableBits_1f8003c0) - 1;
      }

      //LAB_800e7900
      this.envRenderMetrics_800cb710[textureIndex].zFlags_22 &= 0xc000;
      this.envRenderMetrics_800cb710[textureIndex].zFlags_22 |= z & 0x3fff;
    } else if(mode == 5) {
      this.minSobj_800cbd60 = foregroundTextureIndex;
      this.maxSobj_800cbd64 = z + 1;
    }

    //LAB_800e7930
    //LAB_800e7934
    //LAB_800e7938
    return this.envRenderMetrics_800cb710[textureIndex].z_20;
  }

  @Override
  public void preDraw() {
    RENDERER.scissorStack.push();
    RENDERER.scissorStack.setRescale(this.backgroundRect.x + Math.round(GPU.getOffsetX() + this.submapOffsetX_800cb560 + this.screenOffset.x), this.backgroundRect.y, this.backgroundRect.w, this.backgroundRect.h);
  }

  @Override
  @Method(0x800e7954L)
  public void drawEnv(final MV[] sobjMatrices) {
    this.animateAndRenderSubmapModel(this.submapCutMatrix_800d4bb0);

    final float[] sobjScreenZs = new float[sobjMatrices.length];
    final float[] envZs = new float[this.envForegroundTextureCount_800cb580];

    //LAB_800e79b8
    // Render background
    if(this.backgroundObj == null) {
      this.backgroundObj = new QuadBuilder("Submap background")
        .bpp(Bpp.BITS_24)
        .pos(this.backgroundRect.x, this.backgroundRect.y, ((0x1 << orderingTableBits_1f8003c0) - 1) * 4.0f)
        .posSize(this.backgroundRect.w, this.backgroundRect.h)
        .uvSize(1.0f, 1.0f)
        .build();
    }

    this.backgroundTransforms.identity();
    this.backgroundTransforms.transfer.set(GPU.getOffsetX() + this.submapOffsetX_800cb560 + this.screenOffset.x, GPU.getOffsetY() + this.submapOffsetY_800cb564 + this.screenOffset.y, 0.0f);

    RENDERER
      .queueOrthoModel(this.backgroundObj, this.backgroundTransforms, QueuedModelStandard.class)
      .texture(this.backgroundTexture);

    //LAB_800e7a60
    //LAB_800e7a7c
    for(int i = 0; i < sobjMatrices.length; i++) {
      sobjScreenZs[i] = (worldToScreenMatrix_800c3548.m02 * sobjMatrices[i].transfer.x +
        worldToScreenMatrix_800c3548.m12 * sobjMatrices[i].transfer.y +
        worldToScreenMatrix_800c3548.m22 * sobjMatrices[i].transfer.z + worldToScreenMatrix_800c3548.transfer.z) / (1 << 16 - orderingTableBits_1f8003c0);
    }

    //LAB_800e7b08
    //LAB_800e7b40
    for(int i = 0; i < this.envForegroundTextureCount_800cb580; i++) {
      final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i];

      final int flags = (metrics.zFlags_22 & 0xc000) >> 14;
      /*
      * Flag ==
      * 0 - Cutout uses the z value stored in its EnvironmentalRenderingMetrics24 struct.
      * 1 - The cutouts store information on their intended screen depth and a screen depth transformation vector
      * for sobj translations. For each sobj, the sobj translation is multiplied by the local WtS z vector of
      * the current cutout and added to the cutout's screen z to get the z delta value. These values are used
      * to determine which sobjs are in front of and behind the current cutout. If all sobjs are either in front
      * or behind, the cutout z is set to a value relative to the sobj with the min/max screen depth. If there
      * are sobjs both in front and behind, the cutout position is averaged between the ends of the sobj depth
      * range if either of the range ends fall outside the EnvironmentalRenderingMetrics24 struct's z value
      * (Observed that sometimes max can end up lower than min?); otherwise, it uses the struct's z value.
      * 2 - Uses the lower 14 bits of the EnvironmentRenderingMetrics24 zFlags value.
      */
      if(flags == 0x1) {
        //LAB_800e7bb4
        float minZ = Float.MAX_VALUE;
        float maxZ = 0;

        //LAB_800e7bbc
        int positiveZCount = 0;
        int negativeZCount = 0;

        //LAB_800e7c0c
        for(int sobjIndex = this.maxSobj_800cbd64 - 1; sobjIndex >= this.minSobj_800cbd60; sobjIndex--) {
          final float screenDeltaZ = this.cutoutScreenZs_800cbc90[i] +
            this.cutoutWorldToScreenZTransformVectors_800cbb90[i].x * sobjMatrices[sobjIndex].transfer.x +
            this.cutoutWorldToScreenZTransformVectors_800cbb90[i].y * sobjMatrices[sobjIndex].transfer.y +
            this.cutoutWorldToScreenZTransformVectors_800cbb90[i].z * sobjMatrices[sobjIndex].transfer.z;
          final float sobjScreenZ = sobjScreenZs[sobjIndex];

          if(sobjScreenZ != 0xfffb) {
            if(screenDeltaZ < 0) {
              negativeZCount++;
              if(minZ > sobjScreenZ) {
                minZ = sobjScreenZ;
              }
            } else {
              //LAB_800e7cac
              positiveZCount++;
              if(maxZ < sobjScreenZ) {
                maxZ = sobjScreenZ;
              }
            }
          }
        }

        //LAB_800e7cd8
        if(positiveZCount == 0) {
          //LAB_800e7cf8
          envZs[i] = Math.max(maxZ - 50, 40);
          continue;
        }

        //LAB_800e7d00
        if(negativeZCount == 0) {
          //LAB_800e7d3c
          envZs[i] = Math.min(maxZ + 50, (1 << orderingTableBits_1f8003c0) - 1);
          continue;
        }

        //LAB_800e7d50
        if(maxZ > metrics.z_20 || minZ < metrics.z_20) {
          //LAB_800e7d64
          envZs[i] = (maxZ + minZ) / 2;
        } else {
          //LAB_800e7d78
          envZs[i] = metrics.z_20;
        }

        //LAB_800e7d80
      } else if(flags == 0x2) {
        envZs[i] = metrics.zFlags_22 & 0x3fff;
      } else {
        //LAB_800e7d78
        envZs[i] = metrics.z_20;
      }
    }

    //LAB_800e7d9c
    //LAB_800e7de0
    // Render overlays
    for(int i = 0; i < this.envForegroundTextureCount_800cb580; i++) {
      final EnvironmentForegroundTextureMetrics foreground = this.envForegroundMetrics_800cb590[i];

      if(!foreground.hidden_08 && this.foregroundTextures[i] != null) {
        final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i];

        if(metrics.obj == null) {
          metrics.obj = new QuadBuilder("CutoutTexture (index " + i + ')')
            .bpp(Bpp.BITS_24)
            .pos(this.backgroundRect.x, this.backgroundRect.y, 0.0f)
            .posSize(this.backgroundRect.w, this.backgroundRect.h)
            .uvSize(1.0f, 1.0f)
            .build();
        }

        // This was causing a problem when moving left from the room before Zackwell. Not sure if this is a retail issue or SC-specific. GH#332
        final float z = envZs[i];
        if(z < 0) {
          continue;
        }

        final float x;
        final float y;

        if(foreground.ticks < foreground.ticksTotal) {
          x = Math.lerp(foreground.startX, foreground.destX, (foreground.ticks + 1.0f) / foreground.ticksTotal);
          y = Math.lerp(foreground.startY, foreground.destY, (foreground.ticks + 1.0f) / foreground.ticksTotal);
          foreground.ticks++;
        } else {
          x = foreground.destX;
          y = foreground.destY;
        }

        this.backgroundTransforms.identity();
        this.backgroundTransforms.transfer.set(GPU.getOffsetX() + this.submapOffsetX_800cb560 + this.screenOffset.x + x, GPU.getOffsetY() + this.submapOffsetY_800cb564 + this.screenOffset.y + y, z * 4.0f);
        RENDERER
          .queueOrthoModel(metrics.obj, this.backgroundTransforms, QueuedModelStandard.class)
          .texture(this.foregroundTextures[i]);

        // final int oldZ = textZ_800bdf00;
        // textZ_800bdf00 = 4;
        // renderCentredText(Integer.toHexString(metrics.zFlags_22), (int)(metrics.offsetX_1c + metrics.w_18 / 2.0f + this.backgroundTransforms.transfer.x), (int)(metrics.offsetY_1e + metrics.h_1a / 2.0f + this.backgroundTransforms.transfer.y), TextColour.WHITE);
        // textZ_800bdf00 = oldZ;
      }
    }
    //LAB_800e7ed0
  }

  @Override
  @Method(0x800e7f68L)
  public void calcGoodScreenOffset(final float x, final float y, final Vector2f out) {
    if(x < -80) {
      out.x -= 80 + x;
      //LAB_800e7f80
    } else if(x > 80) {
      //LAB_800e7f9c
      out.x += 80 - x;
    }

    //LAB_800e7fa8
    if(y < -40) {
      out.y -= 40 + y;
      //LAB_800e7fbc
    } else if(y > 40) {
      //LAB_800e7fd4
      out.y += 40 - y;
    }

    //LAB_800e7fdc
    if(this._800f7f0c) {
      out.x += this._800cbd30;
      out.y += this._800cbd34;
      this._800f7f0c = false;
      return;
    }

    //LAB_800e8030
    if(out.x < this._800cb574) {
      //LAB_800e807c
      out.x = this._800cb574;
    } else {
      //LAB_800e8070
      out.x = Math.min(this._800cb570, out.x);
    }

    //LAB_800e8080
    //LAB_800e8088
    if(out.y < -this._800cb578) {
      out.y = -this._800cb578;
    } else {
      //LAB_800e80d0
      //LAB_800e80d8
      out.y = Math.min(this._800cb578, out.y);
    }

    //LAB_800e80dc
  }

  @Method(0x800e80e4L)
  public void FUN_800e80e4(final int x, final int y) {
    this._800cbd30 = x;
    this._800cbd34 = y;
    this._800f7f0c = true;
  }

  @Method(0x800eece0L)
  private void animateAndRenderSubmapModel(final Matrix4f matrix) {
    if(!this.hasRenderer_800c6968) {
      return;
    }

    if(this.submapModel_800d4bf8.modelParts_00[0].obj == null) {
      TmdObjLoader.fromModel("Submap model", this.submapModel_800d4bf8);
    }

    this.submapModel_800d4bf8.coord2_14.coord.transfer.zero();
    this.submapModel_800d4bf8.coord2_14.transforms.rotate.zero();

    applyModelRotationAndScale(this.submapModel_800d4bf8);
    animateModel(this.submapModel_800d4bf8, 4 / vsyncMode_8007a3b8);
    this.renderSubmapModel(this.submapModel_800d4bf8, matrix);
  }

  @Method(0x800eee48L)
  private void renderSubmapModel(final Model124 model, final Matrix4f matrix) {
    zOffset_1f8003e8 = model.zOffset_a0;
    tmdGp0Tpage_1f8003ec = model.tpage_108;

    final MV lw = new MV();

    //LAB_800eee94
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPart10 dobj2 = model.modelParts_00[i];

      GsGetLw(dobj2.coord2_04, lw);

      RENDERER.queueModel(dobj2.obj, matrix, lw, QueuedModelTmd.class)
        .screenspaceOffset(GPU.getOffsetX() + GTE.getScreenOffsetX() - 184, GPU.getOffsetY() + GTE.getScreenOffsetY() - 120)
        .depthOffset(model.zOffset_a0)
        .lightDirection(lightDirectionMatrix_800c34e8)
        .lightColour(lightColourMatrix_800c3508)
        .backgroundColour(GTE.backgroundColour)
        .tmdTranslucency(tmdGp0Tpage_1f8003ec >>> 5 & 0b11);
    }
    //LAB_800eef0c
  }

  @Method(0x8001b3e4L)
  private int getSoundCharId() {
    if(soundFiles_800bcf80[11].used_00) {
      return soundFiles_800bcf80[11].id_02;
    }

    //LAB_8001b408
    return AUDIO_THREAD.getSongId();
  }

  @Method(0x8001c60cL)
  private int getSubmapMusicChange() {
    final int soundCharId = this.getSoundCharId();

    final int musicIndex;
    jmp_8001c7a0:
    {
      //LAB_8001c63c
      SubmapMusic08 a2;
      int a3;
      for(a3 = 0, a2 = _8004fb00[a3]; a2.submapId_00 != 99 || a2.musicIndex_02 != 99; a3++, a2 = _8004fb00[a3]) { // I think 99 is just a sentinel value that means "end of list"
        final int submapId = submapId_800bd808;

        if(submapId == a2.submapId_00) {
          //LAB_8001c680
          for(int v1 = 0; v1 < a2.submapCuts_04.length; v1++) {
            if(submapId == 57) { // Opening (Rose intro, Dart forest, horses)
              if(a2.submapCuts_04[v1] != this.cut) {
                continue;
              }

              if((gameState_800babc8._1a4[0] & 0x1) == 0) {
                //LAB_8001c7cc
                musicIndex = a2.musicIndex_02;
                break jmp_8001c7a0;
              }
            }

            //LAB_8001c6ac
            if(a2.submapCuts_04[v1] == this.cut && (gameState_800babc8._1a4[a3 >>> 5] & 0x1 << (a3 & 0x1f)) != 0) {
              //LAB_8001c7c0
              musicIndex = a2.musicIndex_02;
              break jmp_8001c7a0;
            }

            //LAB_8001c6e4
          }
        }

        //LAB_8001c700
      }

      //LAB_8001c728
      SubmapMusic08 a0;
      for(a3 = 0, a0 = _8004fa98[a3]; a0.submapId_00 != 99 || a0.musicIndex_02 != 99; a3++, a0 = _8004fa98[a3]) {
        if(submapId_800bd808 == a0.submapId_00) {
          //LAB_8001c748
          for(int v1 = 0; v1 < a0.submapCuts_04.length; v1++) {
            if(a0.submapCuts_04[v1] == this.cut) {
              //LAB_8001c7d8
              return this.FUN_8001c84c(soundCharId, a0.musicIndex_02);
            }
          }
        }

        //LAB_8001c76c
      }

      musicIndex = this.getCurrentSubmapMusic();
    }

    //LAB_8001c7a0
    final int v1 = this.FUN_8001c84c(soundCharId, musicIndex);
    if(v1 != -2) {
      return v1;
    }

    //LAB_8001c7ec
    if(!AUDIO_THREAD.isMusicPlaying()) {
      return -2;
    }

    //LAB_8001c808
    return -3;
  }

  @Method(0x8001c84cL)
  private int FUN_8001c84c(final int a0, final int a1) {
    if(a0 != a1) {
      return a1;
    }

    if(a0 == -1) {
      return -1;
    }

    return -2;
  }

  @Method(0x8001c874L)
  private int getCurrentSubmapMusic() {
    if(submapId_800bd808 == 56) { // Moon
      for(int i = 0; ; i++) {
        final MoonMusic08 moonMusic = moonMusic_8004ff10[i];

        if(moonMusic.submapCut_00 == this.cut) {
          return moonMusic.musicIndex_04;
        }
      }
    }

    //LAB_8001c8bc
    return submapMusic_80050068[submapId_800bd808];
  }

  private static final SubmapMusic08[] _8004fa98 = {
    new SubmapMusic08(8, 59, 83, 84),
    new SubmapMusic08(19, 59, 204, 214, 211),
    new SubmapMusic08(22, 59, 247),
    new SubmapMusic08(32, 59, 332),
    new SubmapMusic08(34, 59, 357),
    new SubmapMusic08(49, 59, 515, 525),
    new SubmapMusic08(8, 60, 78, 79, 80),
    new SubmapMusic08(19, 60, 210, 209),
    new SubmapMusic08(22, 60, 246),
    new SubmapMusic08(30, 60, 316, 317),
    new SubmapMusic08(32, 60, 335),
    new SubmapMusic08(34, 60, 356, 361),
    new SubmapMusic08(99, 99, 83, 84),
  };
  private static final SubmapMusic08[] _8004fb00 = {
    new SubmapMusic08(57, -1, 675, 676, 677),
    new SubmapMusic08(3, 70, 9, 10, 725),
    new SubmapMusic08(3, 72, 694, 13),
    new SubmapMusic08(3, 71, 695, 694, 742),
    new SubmapMusic08(2, 66, 5, 6, 7, 624, 625),
    new SubmapMusic08(4, 70, 14, 15),
    new SubmapMusic08(5, 70, 38, 39),
    new SubmapMusic08(5, 28, 697, 740, 741),
    new SubmapMusic08(6, 72, 53),
    new SubmapMusic08(10, 63, 95, 98, 99, 100, 101, 102, 103, 104, 105),
    new SubmapMusic08(10, -1, 96),
    new SubmapMusic08(10, -1, 657, 726),
    new SubmapMusic08(10, 70, 674),
    new SubmapMusic08(11, 70, 108),
    new SubmapMusic08(11, 68, 109),
    new SubmapMusic08(13, 68, 716),
    new SubmapMusic08(14, 72, 140, 647),
    new SubmapMusic08(14, -1, 149, 150, 151, 637, 638),
    new SubmapMusic08(14, 61, 152, 634, 636, 639),
    new SubmapMusic08(4, 67, 643, 27),
    new SubmapMusic08(10, 70, 96, 112, 113, 657),
    new SubmapMusic08(8, -1, 66),
    new SubmapMusic08(8, -1, 68),
    new SubmapMusic08(9, -1, 94),
    new SubmapMusic08(17, -1, 181),
    new SubmapMusic08(18, -1, 658, 659, 660),
    new SubmapMusic08(57, 115, 703, 704),
    new SubmapMusic08(5, -1, 41),
    new SubmapMusic08(3, -1, 11),
    new SubmapMusic08(3, 45, 696, 743),
    new SubmapMusic08(12, 76, 120),
    new SubmapMusic08(20, 81, 221, 223, 225, 226, 663, 665, 666, 667),
    new SubmapMusic08(20, 70, 236),
    new SubmapMusic08(20, -1, 236, 669),
    new SubmapMusic08(20, 64, 238, 698),
    new SubmapMusic08(20, 67, 702),
    new SubmapMusic08(22, -1, 240),
    new SubmapMusic08(23, 79, 255),
    new SubmapMusic08(20, -1, 227),
    new SubmapMusic08(46, 25, 496, 497, 498),
    new SubmapMusic08(44, -1, 458),
    new SubmapMusic08(49, 43, 512, 522),
    new SubmapMusic08(56, -1, 733),
    new SubmapMusic08(56, 75, 597, 598, 735, 736, 737),
    new SubmapMusic08(53, 81, 701),
    new SubmapMusic08(53, 70, 629, 700),
    new SubmapMusic08(53, 38, 629),
    new SubmapMusic08(34, 75, 348),
    new SubmapMusic08(35, 68, 370),
    new SubmapMusic08(35, -1, 372),
    new SubmapMusic08(35, 70, 373, 374),
    new SubmapMusic08(35, -1, 375),
    new SubmapMusic08(34, 70, 349, 350, 355, 356),
    new SubmapMusic08(34, 68, 389),
    new SubmapMusic08(32, -1, 338, 670),
    new SubmapMusic08(32, 114, 671),
    new SubmapMusic08(32, -1, 710),
    new SubmapMusic08(33, -1, 346),
    new SubmapMusic08(37, -1, 381, 382),
    new SubmapMusic08(36, 70, 692, 724),
    new SubmapMusic08(37, 70, 388),
    new SubmapMusic08(38, 70, 393),
    new SubmapMusic08(35, -1, 376),
    new SubmapMusic08(31, 71, 325, 324),
    new SubmapMusic08(24, 63, 266),
    new SubmapMusic08(14, -1, 647),
    new SubmapMusic08(31, -1, 325),
    new SubmapMusic08(34, 63, 354),
    new SubmapMusic08(35, -1, 371),
    new SubmapMusic08(54, -1, 711, 714),
    new SubmapMusic08(54, 68, 713),
    new SubmapMusic08(50, -1, 718, 719),
    new SubmapMusic08(43, -1, 446),
    new SubmapMusic08(43, 71, 447),
    new SubmapMusic08(43, 68, 447),
    new SubmapMusic08(24, -1, 266),
    new SubmapMusic08(15, 74, 161),
    new SubmapMusic08(3, 71, 696, 743),
    new SubmapMusic08(22, 78, 244, 248, 259),
    new SubmapMusic08(54, -1, 580),
    new SubmapMusic08(28, 74, 678),
    new SubmapMusic08(28, -1, 364),
    new SubmapMusic08(27, 76, 288),
    new SubmapMusic08(53, -1, 568),
    new SubmapMusic08(19, 78, 216),
    new SubmapMusic08(19, 81, 201, 202, 744),
    new SubmapMusic08(20, -1, 664, 702),
    new SubmapMusic08(30, -1, 312, 313, 314, 318),
    new SubmapMusic08(28, -1, 297, 298, 299, 300, 364, 365, 366, 630, 678),
    new SubmapMusic08(29, -1, 301, 302, 303, 304, 305, 308),
    new SubmapMusic08(14, -1, 647, 646),
    new SubmapMusic08(26, -1, 286),
    new SubmapMusic08(58, -1, 672, 673),
    new SubmapMusic08(40, -1, 423),
    new SubmapMusic08(40, -1, 419),
    new SubmapMusic08(40, 71, 424),
    new SubmapMusic08(36, 28, 692, 693, 723),
    new SubmapMusic08(43, 70, 446),
    new SubmapMusic08(43, 75, 445, 449, 451, 452),
    new SubmapMusic08(46, 74, 499, 500, 501, 502),
    new SubmapMusic08(4, 63, 24),
    new SubmapMusic08(4, -1, 27),
    new SubmapMusic08(4, 70, 27),
    new SubmapMusic08(4, 70, 15, 56),
    new SubmapMusic08(4, -1, 36),
    new SubmapMusic08(32, 63, 329),
    new SubmapMusic08(33, 81, 343),
    new SubmapMusic08(33, 68, 343),
    new SubmapMusic08(33, -1, 345),
    new SubmapMusic08(9, -1, 94),
    new SubmapMusic08(18, -1, 200),
    new SubmapMusic08(53, 70, 563, 564, 565, 566, 567, 568, 629, 595),
    new SubmapMusic08(51, 66, 715, 722),
    new SubmapMusic08(55, -1, 588),
    new SubmapMusic08(55, -1, 593),
    new SubmapMusic08(27, -1, 295),
    new SubmapMusic08(49, 39, 525),
    new SubmapMusic08(54, 63, 580),
    new SubmapMusic08(54, 75, 714),
    new SubmapMusic08(54, 81, 580, 581, 594),
    new SubmapMusic08(54, -1, 570, 571, 572, 573, 574, 576, 578, 579, 582, 711, 712),
    new SubmapMusic08(43, -1, 446),
    new SubmapMusic08(54, -1, 575, 577, 580),
    new SubmapMusic08(5, 20, 38, 39, 40, 41, 42, 43, 44),
    new SubmapMusic08(46, -1, 486, 488, 489, 491),
    new SubmapMusic08(56, -1, 607),
    new SubmapMusic08(56, -1, 561),
    new SubmapMusic08(27, -1, 322),
    new SubmapMusic08(3, -1, 743),
    new SubmapMusic08(99, 99, 675, 676, 677),
  };
  private static final MoonMusic08[] moonMusic_8004ff10 = {
    new MoonMusic08(561, 56),
    new MoonMusic08(596, 45),
    new MoonMusic08(597, 56),
    new MoonMusic08(598, -1),
    new MoonMusic08(599, 79),
    new MoonMusic08(600, 79),
    new MoonMusic08(601, 60),
    new MoonMusic08(602, 79),
    new MoonMusic08(603, 79),
    new MoonMusic08(604, 79),
    new MoonMusic08(605, 20),
    new MoonMusic08(606, 23),
    new MoonMusic08(607, 23),
    new MoonMusic08(608, 72),
    new MoonMusic08(609, 72),
    new MoonMusic08(610, 72),
    new MoonMusic08(611, 72),
    new MoonMusic08(612, 21),
    new MoonMusic08(613, 21),
    new MoonMusic08(614, 21),
    new MoonMusic08(615, 47),
    new MoonMusic08(616, 47),
    new MoonMusic08(617, 47),
    new MoonMusic08(618, 47),
    new MoonMusic08(619, 29),
    new MoonMusic08(620, 29),
    new MoonMusic08(621, 45),
    new MoonMusic08(622, 39),
    new MoonMusic08(699, 79),
    new MoonMusic08(727, 39),
    new MoonMusic08(728, 39),
    new MoonMusic08(729, 63),
    new MoonMusic08(730, -1),
    new MoonMusic08(731, -1),
    new MoonMusic08(732, 79),
    new MoonMusic08(733, 79),
    new MoonMusic08(734, 56),
    new MoonMusic08(735, 56),
    new MoonMusic08(736, 56),
    new MoonMusic08(737, 56),
    new MoonMusic08(738, 21),
    new MoonMusic08(739, 72),
    new MoonMusic08(-1, -1),
  };

  /** Maps submap cuts to their submap */
  private final int[] cutToSubmap_800d610c = {
    1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6,
    6, 6, 6, 6, 6, 6, 6, 1, 4, 3, 8, 7, 7, 7, 7, 7,
    7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
    8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10,
    10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11,
    10, 10, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
    12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14,
    14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 15, 15, 15, 15, 15, 15,
    15, 15, 15, 15, 15, 15, 15, 16, 16, 16, 16, 17, 17, 17, 17, 17,
    17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18,
    18, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19,
    19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20,
    20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 19, 19, 20, 20, 20, 22,
    22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23,
    23, 23, 23, 22, 20, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25,
    25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 27,
    27, 27, 27, 27, 27, 27, 27, 27, 27, 28, 28, 28, 28, 29, 29, 29,
    29, 29, 29, 29, 29, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30,
    30, 27, 27, 31, 31, 31, 31, 32, 32, 32, 32, 32, 32, 32, 32, 32,
    32, 32, 32, 33, 33, 33, 33, 33, 33, 33, 33, 34, 34, 34, 34, 34,
    34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 28, 28, 28, 35,
    35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 36, 36, 37, 37, 37,
    37, 37, 37, 37, 37, 34, 37, 37, 37, 38, 38, 38, 38, 38, 38, 38,
    38, 38, 38, 38, 38, 38, 39, 39, 39, 39, 39, 39, 39, 40, 40, 40,
    40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 41, 41, 41,
    41, 42, 42, 42, 42, 42, 42, 42, 42, 43, 43, 43, 43, 43, 43, 43,
    43, 43, 43, 43, 43, 43, 43, 43, 43, 44, 44, 44, 44, 44, 44, 44,
    44, 45, 45, 45, 58, 58, 58, 58, 58, 58, 58, 58, 58, 46, 46, 46,
    46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46,
    46, 46, 46, 46, 46, 46, 46, 46, 47, 58, 58, 58, 58, 58, 48, 48,
    49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49,
    50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 51, 51, 51, 51, 51,
    51, 51, 51, 51, 51, 51, 51, 51, 51, 48, 48, 48, 48, 48, 48, 48,
    48, 56, 52, 53, 53, 53, 53, 53, 53, 54, 54, 54, 54, 54, 54, 54,
    54, 54, 54, 54, 54, 54, 54, 55, 55, 55, 55, 55, 55, 55, 55, 55,
    55, 55, 54, 53, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56,
    56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 8,
    2, 2, 10, 10, 9, 53, 28, 8, 10, 10, 14, 14, 14, 14, 14, 14,
    4, 4, 4, 4, 4, 4, 14, 14, 49, 49, 58, 58, 58, 58, 58, 58,
    13, 10, 18, 18, 18, 53, 20, 20, 20, 20, 20, 20, 20, 20, 32, 32,
    58, 58, 10, 57, 57, 57, 28, 58, 58, 58, 58, 58, 58, 58, 58, 58,
    58, 58, 58, 58, 36, 36, 3, 3, 3, 5, 20, 56, 53, 53, 20, 57,
    57, 51, 51, 51, 51, 19, 32, 54, 54, 54, 54, 51, 13, 50, 50, 50,
    30, 51, 51, 36, 36, 3, 10, 56, 56, 56, 56, 56, 56, 56, 56, 56,
    56, 56, 56, 56, 5, 5, 3, 3, 19, 48, 48, 48, 48, 48, 48, 48,
    48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48,
    48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48,
    48, 48, 48, 48, 48, 48, 48, 0,
  };

  /**
   * These are indices into the above table
   *
   * <ul>
   *   <li>0 - submap has no model</li>
   *   <li>65 - submap has model</li>
   * </ul>
   */
  private static final int[] submapTypes_800f5cd4 = {
    0, 0, 0, 0, 0, 65, 65, 0, 0, 65, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65,
    65, 65, 65, 65, 0, 0, 65, 0, 65, 0, 0, 0, 0, 0, 0, 0, 65, 65, 0, 65, 65, 0, 65, 65, 65, 65, 0, 0, 65, 0, 65, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 0, 0, 0, 65, 65,
    0, 0, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 0, 0, 65, 65, 0, 65, 65, 65, 65, 0, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 0, 65,
    65, 65, 0, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 65, 65, 65, 0, 0, 0, 65, 0, 0, 65, 0, 65, 0, 0, 0, 0, 65, 65, 65, 65, 65, 0, 0, 0, 65, 65, 0, 65, 65,
    65, 65, 65, 0, 65, 0, 0, 0, 0, 0, 65, 65, 0, 65, 65, 0, 0, 65, 65, 0, 0, 0, 0, 65, 65, 0, 0, 0, 65, 65, 65, 0, 0, 0, 0, 65, 65, 0, 65, 65, 65, 0, 65, 0, 65, 0, 65, 0, 0, 0,
    0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 65, 0, 0, 65, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65, 65, 0, 0, 0, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0,
    0, 65, 65, 65, 65, 65, 0, 0, 65, 65, 65, 0, 0, 0, 0, 65, 0, 65, 0, 0, 0, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65,
    65, 0, 65, 65, 0, 0, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 65, 0, 65, 65, 65, 65, 0,
    65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 0, 0, 65, 65, 0, 65, 65, 0, 65, 65, 0,
    0, 65, 0, 0, 0, 0, 0, 0, 65, 0, 0, 65, 0, 65, 0, 0, 65, 65, 65, 0, 65, 65, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 65, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65,
    0, 65, 65, 0, 0, 0, 0, 65, 65, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
    65, 65, 65, 0, 0, 0, 0, 0, 0, 0, 0, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
    0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 65, 0, 0, 0, 65, 65, 65, 65, 65, 0, 0, 0, 0, 0, 65, 0, 65, 0, 65, 65, 65, 65, 0, 0, 65, 65,
    0, 0, 0, 0, 65, 65, 0, 0, 65, 65, 65, 65, 65, 65, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 0, 65, 0, 65, 65, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65,
    65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65, 0, 0, 0, 0, 0, 0, 0, 65, 65, 65, 65, 0, 0, 0, 65, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65, 65, 65, 65, 0, 0, 0, 0, 65, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
  };

  /** Indexed by submap cut */
  public static final SubmapEncounterData_04[] encounterData_800f64c4 = {
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 10, 1), new SubmapEncounterData_04(1, 7, 1), new SubmapEncounterData_04(2, 10, 1), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(10, 10, 6), new SubmapEncounterData_04(11, 10, 6), new SubmapEncounterData_04(12, 10, 6), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(14, 10, 6), new SubmapEncounterData_04(15, 14, 6), new SubmapEncounterData_04(16, 14, 6), new SubmapEncounterData_04(20, 10, 7), new SubmapEncounterData_04(21, 10, 7), new SubmapEncounterData_04(22, 10, 7),
    new SubmapEncounterData_04(23, 10, 7), new SubmapEncounterData_04(24, 10, 7), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(26, 10, 7), new SubmapEncounterData_04(27, 4, 7), new SubmapEncounterData_04(28, 10, 7), new SubmapEncounterData_04(29, 9, 7), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(30, 9, 10), new SubmapEncounterData_04(31, 14, 10), new SubmapEncounterData_04(32, 14, 10), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(34, 14, 10), new SubmapEncounterData_04(35, 14, 10),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(40, 9, 11), new SubmapEncounterData_04(41, 14, 11), new SubmapEncounterData_04(42, 14, 11), new SubmapEncounterData_04(43, 19, 11), new SubmapEncounterData_04(44, 19, 11), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(47, 9, 11), new SubmapEncounterData_04(48, 9, 11), new SubmapEncounterData_04(49, 9, 11), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(50, 14, 13), new SubmapEncounterData_04(51, 10, 13), new SubmapEncounterData_04(52, 7, 13), new SubmapEncounterData_04(53, 10, 13), new SubmapEncounterData_04(54, 10, 92), new SubmapEncounterData_04(55, 7, 92), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(60, 10, 15), new SubmapEncounterData_04(61, 10, 15), new SubmapEncounterData_04(62, 7, 15), new SubmapEncounterData_04(63, 7, 15), new SubmapEncounterData_04(64, 7, 15), new SubmapEncounterData_04(0, 0, 15), new SubmapEncounterData_04(66, 14, 15),
    new SubmapEncounterData_04(67, 17, 15), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(80, 8, 23), new SubmapEncounterData_04(81, 8, 23), new SubmapEncounterData_04(82, 5, 23), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(91, 8, 26), new SubmapEncounterData_04(92, 8, 26), new SubmapEncounterData_04(93, 8, 26), new SubmapEncounterData_04(94, 8, 26),
    new SubmapEncounterData_04(95, 8, 26), new SubmapEncounterData_04(96, 8, 26), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(100, 10, 28), new SubmapEncounterData_04(101, 17, 2), new SubmapEncounterData_04(102, 17, 28), new SubmapEncounterData_04(103, 10, 28), new SubmapEncounterData_04(104, 10, 29), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(120, 21, 33), new SubmapEncounterData_04(121, 10, 33), new SubmapEncounterData_04(122, 8, 33),
    new SubmapEncounterData_04(123, 8, 33), new SubmapEncounterData_04(124, 14, 33), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(130, 10, 36), new SubmapEncounterData_04(131, 10, 36), new SubmapEncounterData_04(132, 10, 36), new SubmapEncounterData_04(133, 10, 36), new SubmapEncounterData_04(134, 10, 36), new SubmapEncounterData_04(135, 14, 36), new SubmapEncounterData_04(136, 10, 36), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(140, 7, 37), new SubmapEncounterData_04(141, 7, 37), new SubmapEncounterData_04(142, 7, 37), new SubmapEncounterData_04(143, 7, 37), new SubmapEncounterData_04(144, 7, 37), new SubmapEncounterData_04(145, 7, 37), new SubmapEncounterData_04(146, 7, 37),
    new SubmapEncounterData_04(147, 7, 37), new SubmapEncounterData_04(148, 7, 37), new SubmapEncounterData_04(149, 7, 37), new SubmapEncounterData_04(150, 7, 37), new SubmapEncounterData_04(151, 7, 37), new SubmapEncounterData_04(152, 7, 37), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(106, 10, 39), new SubmapEncounterData_04(107, 10, 39), new SubmapEncounterData_04(108, 10, 39),
    new SubmapEncounterData_04(109, 10, 39), new SubmapEncounterData_04(110, 10, 39), new SubmapEncounterData_04(111, 10, 39), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(112, 10, 39), new SubmapEncounterData_04(113, 10, 39), new SubmapEncounterData_04(114, 10, 39), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(115, 10, 39), new SubmapEncounterData_04(116, 10, 39), new SubmapEncounterData_04(117, 10, 39), new SubmapEncounterData_04(118, 10, 39), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(160, 10, 42), new SubmapEncounterData_04(161, 10, 42), new SubmapEncounterData_04(162, 10, 42), new SubmapEncounterData_04(163, 7, 42), new SubmapEncounterData_04(164, 7, 42), new SubmapEncounterData_04(165, 10, 42), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(166, 10, 42), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 45),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(173, 14, 44), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(174, 14, 44), new SubmapEncounterData_04(0, 0, 44), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(180, 14, 46), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(182, 14, 46), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(184, 14, 46), new SubmapEncounterData_04(185, 14, 46), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(186, 14, 47), new SubmapEncounterData_04(187, 14, 47), new SubmapEncounterData_04(0, 0, 47), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(190, 14, 49), new SubmapEncounterData_04(191, 14, 49), new SubmapEncounterData_04(192, 10, 49),
    new SubmapEncounterData_04(193, 14, 49), new SubmapEncounterData_04(194, 14, 49), new SubmapEncounterData_04(195, 10, 49), new SubmapEncounterData_04(196, 10, 49), new SubmapEncounterData_04(197, 10, 49), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(198, 7, 49), new SubmapEncounterData_04(199, 7, 50), new SubmapEncounterData_04(200, 7, 50), new SubmapEncounterData_04(201, 7, 50),
    new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(203, 7, 50), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(231, 10, 53), new SubmapEncounterData_04(232, 10, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(234, 10, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(244, 21, 54), new SubmapEncounterData_04(245, 21, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(247, 14, 54),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(249, 14, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(251, 14, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(253, 10, 54), new SubmapEncounterData_04(254, 21, 54), new SubmapEncounterData_04(255, 21, 54), new SubmapEncounterData_04(256, 21, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(211, 17, 55), new SubmapEncounterData_04(212, 17, 55), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(214, 21, 55), new SubmapEncounterData_04(215, 21, 55), new SubmapEncounterData_04(0, 0, 2),
    new SubmapEncounterData_04(217, 21, 55), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(219, 21, 55), new SubmapEncounterData_04(220, 21, 55), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(222, 21, 55), new SubmapEncounterData_04(223, 21, 55), new SubmapEncounterData_04(260, 17, 56), new SubmapEncounterData_04(261, 17, 56), new SubmapEncounterData_04(262, 17, 56), new SubmapEncounterData_04(263, 14, 56), new SubmapEncounterData_04(264, 14, 56), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(265, 17, 56), new SubmapEncounterData_04(266, 17, 56), new SubmapEncounterData_04(267, 14, 56),
    new SubmapEncounterData_04(268, 17, 56), new SubmapEncounterData_04(0, 0, 56), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(269, 10, 58), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(270, 10, 58), new SubmapEncounterData_04(271, 14, 58), new SubmapEncounterData_04(272, 14, 66), new SubmapEncounterData_04(273, 14, 66), new SubmapEncounterData_04(274, 14, 80), new SubmapEncounterData_04(275, 14, 79),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(276, 10, 59), new SubmapEncounterData_04(277, 10, 59), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(278, 14, 81), new SubmapEncounterData_04(279, 14, 81), new SubmapEncounterData_04(280, 14, 81), new SubmapEncounterData_04(281, 8, 83), new SubmapEncounterData_04(282, 14, 83), new SubmapEncounterData_04(283, 8, 83), new SubmapEncounterData_04(284, 14, 84), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(285, 10, 61), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(3, 14, 1), new SubmapEncounterData_04(4, 10, 1), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(286, 10, 58), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(257, 21, 54), new SubmapEncounterData_04(258, 21, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(225, 21, 55), new SubmapEncounterData_04(226, 21, 55), new SubmapEncounterData_04(0, 0, 69), new SubmapEncounterData_04(259, 21, 54), new SubmapEncounterData_04(0, 0, 14), new SubmapEncounterData_04(241, 14, 53), new SubmapEncounterData_04(242, 10, 67), new SubmapEncounterData_04(0, 0, 53),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(287, 14, 61), new SubmapEncounterData_04(288, 14, 61), new SubmapEncounterData_04(289, 14, 62), new SubmapEncounterData_04(290, 14, 63), new SubmapEncounterData_04(291, 14, 63), new SubmapEncounterData_04(292, 10, 63), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2),
    new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(293, 10, 81), new SubmapEncounterData_04(294, 14, 59), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
  };

  private static final int[][] sceneEncounterIds_800f74c4 = {
    {7, 5, 2, 1}, {7, 8, 9, 2}, {9, 8, 7, 2}, {8, 6, 4, 0}, {4, 5, 6, 3}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {14, 15, 13, 12}, {16, 14, 18, 11}, {17, 18, 19, 10}, {0, 0, 0, 0}, {10, 13, 12, 11}, {15, 17, 19, 13}, {19, 18, 17, 10}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {24, 25, 26, 20}, {26, 27, 28, 21}, {25, 28, 29, 22}, {27, 29, 24, 23}, {25, 26, 27, 24}, {0, 0, 0, 0}, {26, 25, 24, 20}, {27, 26, 25, 21}, {28, 27, 26, 22}, {29, 28, 27, 23}, {34, 35, 36, 30}, {35, 36, 37, 31}, {36, 37, 34, 32}, {0, 0, 0, 0}, {38, 37, 39, 33}, {39, 38, 37, 30}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {44, 45, 46, 40}, {45, 46, 47, 41}, {46, 47, 48, 42}, {47, 48, 49, 43}, {48, 49, 44, 40}, {0, 0, 0, 0}, {0, 0, 0, 0}, {47, 46, 45, 41}, {48, 47, 46, 42}, {49, 48, 47, 43}, {54, 55, 56, 50}, {55, 56, 57, 51}, {56, 57, 58, 52}, {57, 58, 59, 53}, {56, 55, 54, 52}, {59, 58, 57, 53}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {65, 66, 60, 61}, {60, 66, 67, 62}, {66, 67, 68, 63}, {60, 65, 66, 64}, {67, 66, 65, 61}, {0, 0, 0, 0}, {68, 67, 69, 62}, {69, 68, 67, 63}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {85, 86, 87, 82}, {87, 86, 89, 84}, {89, 88, 82, 81}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {92, 95, 96, 90}, {95, 96, 97, 91}, {96, 97, 92, 93}, {97, 96, 95, 92}, {98, 97, 99, 92}, {99, 98, 97, 92}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {104, 105, 106, 100}, {105, 106, 107, 101}, {106, 107, 108, 102}, {107, 108, 109, 103}, {108, 109, 104, 102}, {0, 0, 0, 0}, {154, 155, 156, 150}, {155, 156, 157, 151}, {156, 157, 158, 152}, {157, 158, 159, 153}, {158, 159, 154, 150}, {159, 154, 155, 151}, {154, 155, 156, 152}, {155, 156, 157, 153}, {156, 157, 158, 150}, {157, 158, 159, 151}, {158, 159, 154, 152}, {159, 154, 155, 153}, {154, 155, 156, 150}, {0, 0, 0, 0},
    {125, 126, 127, 120}, {126, 127, 128, 121}, {128, 129, 125, 122}, {129, 125, 126, 123}, {129, 128, 127, 124}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {132, 135, 136, 130}, {135, 136, 137, 131}, {136, 137, 138, 133}, {137, 138, 139, 134}, {138, 139, 132, 130}, {139, 132, 135, 131}, {139, 138, 137, 133}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {145, 146, 147, 140}, {146, 147, 148, 141}, {147, 148, 149, 142}, {148, 149, 145, 143}, {149, 145, 146, 144}, {145, 146, 147, 140}, {146, 147, 148, 141}, {147, 148, 149, 142}, {148, 149, 145, 143}, {149, 145, 146, 144}, {145, 146, 147, 140}, {146, 147, 148, 141}, {149, 148, 147, 142}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {161, 165, 166, 160}, {165, 166, 167, 162}, {166, 167, 168, 163}, {167, 168, 169, 164}, {168, 169, 161, 160}, {169, 161, 165, 162}, {161, 165, 166, 163}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {174, 175, 176, 179}, {177, 178, 179, 176}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {185, 186, 187, 180}, {0, 0, 0, 0}, {186, 187, 188, 181}, {0, 0, 0, 0}, {187, 188, 189, 182}, {188, 189, 185, 183}, {189, 185, 186, 184}, {189, 188, 187, 184}, {0, 0, 0, 0}, {0, 0, 0, 0}, {195, 196, 197, 190}, {196, 197, 198, 191}, {197, 198, 199, 192}, {198, 199, 195, 193}, {199, 195, 196, 194}, {195, 196, 197, 190}, {196, 197, 198, 191}, {197, 198, 199, 192}, {198, 199, 195, 193}, {199, 195, 196, 194},
    {195, 196, 197, 190}, {196, 197, 198, 191}, {0, 0, 0, 0}, {199, 198, 197, 192}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {232, 235, 236, 230}, {235, 236, 237, 231}, {0, 0, 0, 0}, {236, 237, 238, 233}, {237, 238, 239, 234}, {0, 0, 0, 0}, {238, 239, 232, 230}, {0, 0, 0, 0}, {232, 235, 236, 231},
    {235, 236, 237, 232}, {0, 0, 0, 0}, {236, 237, 238, 233}, {237, 238, 239, 234}, {0, 0, 0, 0}, {238, 239, 232, 230}, {239, 232, 235, 231}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {245, 246, 247, 240}, {246, 247, 248, 241}, {0, 0, 0, 0}, {247, 248, 249, 242}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {0, 0, 0, 0}, {248, 249, 245, 243}, {249, 245, 246, 244}, {0, 0, 0, 0}, {255, 256, 257, 250}, {256, 257, 258, 251}, {0, 0, 0, 0}, {257, 258, 259, 252}, {0, 0, 0, 0}, {258, 259, 255, 253}, {0, 0, 0, 0}, {259, 255, 256, 254}, {255, 256, 257, 250}, {256, 257, 258, 251}, {257, 258, 259, 252}, {258, 259, 255, 253}, {259, 255, 256, 254}, {255, 256, 257, 250}, {256, 257, 258, 251}, {257, 258, 259, 252},
    {265, 266, 267, 260}, {266, 267, 268, 261}, {267, 268, 269, 262}, {268, 269, 265, 263}, {269, 265, 266, 264}, {265, 266, 267, 260}, {266, 267, 268, 261}, {267, 268, 269, 262}, {268, 269, 265, 263}, {290, 293, 294, 291}, {293, 294, 290, 291}, {294, 290, 293, 291}, {300, 301, 302, 303}, {301, 302, 303, 304}, {302, 303, 304, 301}, {303, 304, 300, 301}, {290, 293, 294, 292}, {293, 294, 290, 292}, {305, 306, 307, 308}, {306, 307, 308, 309},
    {307, 308, 309, 305}, {310, 311, 312, 313}, {311, 312, 313, 314}, {312, 313, 314, 310}, {313, 314, 310, 311}, {315, 316, 317, 318}, {294, 290, 293, 291}, {316, 317, 318, 319}, {317, 318, 319, 315}, {318, 319, 315, 316}, {295, 296, 297, 298}, {296, 297, 299, 298}, {297, 299, 295, 298}, {308, 309, 305, 306}, {290, 293, 294, 292}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
  };

  /** A hard-coded list of submap cuts, related to submap bounds for camera control */
  private final int[] _800f7e58 = {140, 142, 155, 193, 197, 253, 310, 434, 665, 747, 748, 749, 750, 751, 752, 753, 754, 755, 756, 757, 758, 760, 761, 762, 765, 766, 767, 768, 769, 770, 771, 772, 773, 774, 775, 776, 777, 778, 780, 781, 782, 783, 784, 785, 786};

  /** Submap exits that lead to the world map */
  private final SubmapWorldMapExits[] submapWorldMapExits_800f7f74 = {
    new SubmapWorldMapExits(7, 13), new SubmapWorldMapExits(624, 11), new SubmapWorldMapExits(7, 16), new SubmapWorldMapExits(11, 30), new SubmapWorldMapExits(9, 9), new SubmapWorldMapExits(696, 13), new SubmapWorldMapExits(695, 4), new SubmapWorldMapExits(13, 17),
    new SubmapWorldMapExits(38, 28), new SubmapWorldMapExits(39, 25), new SubmapWorldMapExits(44, 6), new SubmapWorldMapExits(45, 1), new SubmapWorldMapExits(54, 6), new SubmapWorldMapExits(66, 41), new SubmapWorldMapExits(95, 1), new SubmapWorldMapExits(96, 1),
    new SubmapWorldMapExits(106, 10), new SubmapWorldMapExits(106, 16), new SubmapWorldMapExits(106, 20), new SubmapWorldMapExits(111, 11), new SubmapWorldMapExits(114, 0), new SubmapWorldMapExits(122, 8), new SubmapWorldMapExits(130, 17), new SubmapWorldMapExits(133, 57),
    new SubmapWorldMapExits(132, 22), new SubmapWorldMapExits(140, 2), new SubmapWorldMapExits(153, 39), new SubmapWorldMapExits(171, 1), new SubmapWorldMapExits(201, 1), new SubmapWorldMapExits(201, 32), new SubmapWorldMapExits(744, 2), new SubmapWorldMapExits(224, 35),
    new SubmapWorldMapExits(231, 1), new SubmapWorldMapExits(233, 13), new SubmapWorldMapExits(232, 11), new SubmapWorldMapExits(239, 26), new SubmapWorldMapExits(242, 3), new SubmapWorldMapExits(251, 10), new SubmapWorldMapExits(258, 10), new SubmapWorldMapExits(256, 9),
    new SubmapWorldMapExits(261, 1), new SubmapWorldMapExits(264, 28), new SubmapWorldMapExits(297, 0), new SubmapWorldMapExits(297, 3), new SubmapWorldMapExits(301, 19), new SubmapWorldMapExits(302, 19), new SubmapWorldMapExits(301, 1), new SubmapWorldMapExits(302, 1),
    new SubmapWorldMapExits(309, 32), new SubmapWorldMapExits(311, 18), new SubmapWorldMapExits(328, 54), new SubmapWorldMapExits(330, 25), new SubmapWorldMapExits(330, 19), new SubmapWorldMapExits(339, 26), new SubmapWorldMapExits(344, 17), new SubmapWorldMapExits(344, 18),
    new SubmapWorldMapExits(341, 14), new SubmapWorldMapExits(0, 0), new SubmapWorldMapExits(342, 38), new SubmapWorldMapExits(347, 31), new SubmapWorldMapExits(349, 1), new SubmapWorldMapExits(379, 0), new SubmapWorldMapExits(0, 0), new SubmapWorldMapExits(413, 1),
    new SubmapWorldMapExits(433, 3), new SubmapWorldMapExits(434, 1), new SubmapWorldMapExits(457, 0), new SubmapWorldMapExits(459, 1), new SubmapWorldMapExits(477, 0), new SubmapWorldMapExits(787, 1), new SubmapWorldMapExits(513, 1), new SubmapWorldMapExits(526, 36),
    new SubmapWorldMapExits(528, 14), new SubmapWorldMapExits(528, 13), new SubmapWorldMapExits(539, 0), new SubmapWorldMapExits(540, 19), new SubmapWorldMapExits(572, 23), new SubmapWorldMapExits(528, 15), new SubmapWorldMapExits(563, 25), new SubmapWorldMapExits(0, 0),
    new SubmapWorldMapExits(0, 0), new SubmapWorldMapExits(15, 8), new SubmapWorldMapExits(346, 1), new SubmapWorldMapExits(529, 41), new SubmapWorldMapExits(38, 7), new SubmapWorldMapExits(994, 0), new SubmapWorldMapExits(996, 0), new SubmapWorldMapExits(993, 0),
    new SubmapWorldMapExits(992, 0), new SubmapWorldMapExits(992, 0), new SubmapWorldMapExits(990, 0), new SubmapWorldMapExits(991, 0), new SubmapWorldMapExits(285, 0), new SubmapWorldMapExits(279, 31), new SubmapWorldMapExits(527, 35), new SubmapWorldMapExits(528, 15),
    new SubmapWorldMapExits(327, 54), new SubmapWorldMapExits(97, 1), new SubmapWorldMapExits(12, 30), new SubmapWorldMapExits(0, 0), new SubmapWorldMapExits(999, 0), new SubmapWorldMapExits(999, 1), new SubmapWorldMapExits(999, 2), new SubmapWorldMapExits(0, 0),
  };

  /** Seems to be missing one element at the end, there are 792 cuts */
  private static final int[] smapFileIndices_800f982c = {
    0, 0, 0, 0, 0, 6674, 6676, 0, 0, 6678, 0, 0, 0, 6680, 6682, 6684, 6686, 6688, 6690, 6692, 6694, 6696, 6698, 6700, 6702, 6704, 6706, 6708, 6710, 6712, 6714, 6716, 6718, 6720, 6722, 6724, 6726, 6684, 6728, 0, 0, 0, 6730, 6732, 6734, 6736, 6738, 6740, 6742, 6744,
    6746, 6748, 6750, 6752, 0, 0, 6682, 0, 6754, 0, 0, 0, 0, 0, 0, 0, 6754, 6756, 0, 6760, 6762, 0, 6764, 6766, 6768, 6770, 0, 0, 6772, 0, 6774, 0, 0, 6776, 6778, 6780, 6782, 6784, 6786, 6788, 6790, 0, 0, 0, 0, 0, 0, 0, 6792, 6794,
    0, 0, 6796, 0, 6798, 6800, 6802, 6804, 6806, 6808, 6810, 6812, 0, 0, 6814, 6816, 6818, 6820, 6822, 6824, 6826, 6828, 6830, 6832, 0, 0, 0, 0, 0, 0, 6834, 6836, 0, 6838, 6840, 6842, 6844, 0, 0, 0, 0, 0, 6846, 6848, 6850, 6852, 6854, 6856, 0, 6858,
    6860, 6862, 0, 6864, 6866, 6868, 6870, 6872, 0, 6874, 6876, 6878, 6880, 6872, 6868, 6866, 6870, 0, 0, 0, 0, 6882, 6884, 6886, 0, 0, 0, 6888, 0, 0, 6890, 0, 6892, 0, 0, 0, 0, 6894, 6896, 6898, 6900, 6902, 0, 0, 0, 6904, 6906, 0, 6908, 6910,
    6912, 6914, 6916, 0, 6918, 0, 0, 0, 0, 0, 6920, 6922, 0, 6924, 6926, 0, 0, 6928, 6930, 0, 0, 0, 0, 6932, 6934, 0, 0, 0, 6936, 6938, 6940, 0, 0, 0, 0, 6942, 6940, 0, 6938, 6944, 6946, 0, 6948, 0, 6950, 0, 6952, 0, 0, 0,
    0, 6954, 6956, 6958, 6960, 6962, 6964, 6966, 6968, 6944, 0, 0, 6970, 0, 0, 6972, 6974, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6976, 6978, 0, 0, 0, 6980, 6982, 6984, 0, 6986, 6988, 6990, 6992, 6994, 6996, 6998, 7000, 7002, 7004, 7006, 0, 0,
    0, 7008, 7010, 7012, 7014, 7016, 0, 0, 7018, 7020, 7022, 0, 0, 0, 0, 7024, 0, 7026, 0, 0, 0, 7028, 7030, 7032, 7034, 7036, 0, 7038, 7038, 7040, 7042, 7044, 7046, 7048, 7050, 7052, 7054, 7056, 7058, 0, 0, 0, 0, 7060, 7062, 7064, 7062, 7066, 7068, 7070,
    7072, 0, 7074, 7076, 0, 0, 7078, 7080, 7082, 7084, 0, 7086, 7088, 7090, 7092, 7094, 7096, 7098, 7100, 7102, 7104, 7106, 7108, 7110, 7112, 7114, 7114, 0, 0, 0, 0, 7116, 7118, 7120, 7122, 7124, 7126, 7128, 7130, 0, 0, 0, 0, 7132, 0, 7134, 7136, 7138, 7140, 0,
    7142, 7144, 7146, 7148, 7150, 7152, 0, 0, 0, 0, 0, 0, 0, 7154, 7156, 7158, 7160, 7162, 7164, 7166, 7168, 7170, 7172, 7174, 7176, 7160, 7160, 7160, 0, 0, 0, 0, 0, 7178, 7180, 7182, 7184, 7186, 7188, 0, 0, 7190, 7192, 0, 7194, 7196, 0, 7198, 7200, 0,
    0, 7202, 0, 0, 0, 0, 0, 0, 7204, 0, 0, 7206, 0, 7138, 0, 0, 7208, 7210, 7212, 0, 7214, 7216, 0, 0, 0, 0, 7214, 7218, 7220, 7222, 7224, 7226, 7228, 7230, 7232, 0, 0, 7234, 7236, 7238, 7240, 7242, 7244, 0, 7246, 7248, 7250, 7252, 7254, 7256,
    0, 7258, 7260, 0, 0, 0, 0, 6782, 6782, 0, 0, 0, 7594, 7262, 7264, 7266, 7268, 7270, 7272, 7274, 7276, 7278, 7280, 7282, 7284, 7286, 0, 7288, 7290, 7292, 7294, 7296, 7298, 7300, 7302, 7304, 7306, 7308, 7310, 7312, 7314, 7316, 7318, 7320, 7322, 7324, 7326, 7328, 7330, 7332,
    7334, 7336, 7338, 0, 0, 0, 0, 0, 0, 0, 0, 7586, 0, 7340, 7342, 7344, 7346, 7348, 7350, 7352, 7354, 7356, 7358, 7360, 7362, 7364, 7366, 7368, 7370, 7372, 7374, 7376, 7378, 7380, 7382, 7384, 7386, 7388, 0, 7390, 7392, 7394, 7396, 7398, 7376, 7520, 7400, 7402, 7404, 7406,
    0, 7408, 7410, 7412, 7414, 7416, 7418, 7420, 7422, 7424, 7426, 7428, 7430, 7432, 7434, 7436, 7438, 7440, 7442, 7444, 7446, 7448, 7450, 0, 7452, 0, 0, 0, 7454, 7456, 7458, 6760, 7460, 0, 0, 0, 0, 0, 7462, 0, 7464, 0, 7466, 7468, 7470, 7472, 0, 0, 7474, 7476,
    0, 0, 0, 0, 7214, 7214, 0, 0, 7478, 7480, 7482, 7484, 7486, 7488, 0, 0, 7490, 7492, 7494, 7496, 7498, 7500, 7502, 7504, 7506, 7508, 7510, 0, 7512, 6914, 6984, 7514, 7516, 7518, 7268, 0, 7542, 0, 7520, 6782, 0, 0, 7522, 7524, 7468, 6698, 7482, 7522, 7530, 7526,
    7528, 7368, 7530, 7532, 0, 7534, 7536, 7538, 7540, 7542, 7544, 7546, 7548, 7550, 7552, 7554, 0, 7556, 7558, 7560, 7024, 7562, 7564, 7566, 7568, 0, 0, 7570, 7572, 7574, 7576, 0, 7578, 7580, 7582, 7584, 7586, 7588, 7590, 7592, 7524, 7566, 6680, 6896, 7542, 7594, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 7596, 0, 0, 0, 0, 7598, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7600, 0, 0, 0, 0, 0, 0, 0, 7602, 7604, 7606, 7608,
  };
}
