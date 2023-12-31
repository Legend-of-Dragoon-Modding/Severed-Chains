package legend.game.submap;

import legend.core.MathHelper;
import legend.core.RenderEngine;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdWithId;
import legend.core.memory.Method;
import legend.core.memory.types.IntRef;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.TmdObjLoader;
import legend.game.modding.events.submap.SubmapEnvironmentTextureEvent;
import legend.game.scripting.ScriptFile;
import legend.game.tim.Tim;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.CContainer;
import legend.game.types.GsRVIEW2;
import legend.game.types.Model124;
import legend.game.types.NewRootStruct;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static legend.core.Async.allLoaded;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.orderingTableBits_1f8003c0;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.animateModel;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsSetSmapRefView2L;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapEnvState_80052c44;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.projectionPlaneDistance_800bd810;
import static legend.game.Scus94491BpeSegment_800b.rview2_800bd7e8;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;

public class RetailSubmap extends Submap {
  private static final Logger LOGGER = LogManager.getFormatterLogger();

  public final int cut;
  private final NewRootStruct newRoot;
  private final Vector2i screenOffset;
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

  private final Vector3f[] _800cbb90 = new Vector3f[32];
  {
    Arrays.setAll(this._800cbb90, i -> new Vector3f());
  }
  private final float[] _800cbc90 = new float[32];

  private final GsRVIEW2 rview2_800cbd10 = new GsRVIEW2();
  private int _800cbd30;
  private int _800cbd34;

  public final MV screenToWorldMatrix_800cbd40 = new MV();

  private int minSobj_800cbd60;
  private int maxSobj_800cbd64;
  /** A copy of the WS matrix */
  private final MV worldToScreenMatrix_800cbd68 = new MV();

  private final Matrix4f submapCutMatrix_800d4bb0 = new Matrix4f();

  private TheEndStructB0 theEndStruct_800d4bd0;
  private FileData theEndClut_800d4bd4;

  private CContainer submapCutModel;
  private TmdAnimationFile submapCutAnim;
  private Tim theEndTim_800d4bf0;

  private final Model124 submapModel_800d4bf8 = new Model124("Submap");

  private final Rect4i theEndClutRect_800d6b48 = new Rect4i(576, 368, 16, 1);

  private boolean _800f7f0c;

  private final Vector2i tpage_800f9e5c = new Vector2i();
  private final Vector2i clut_800f9e5e = new Vector2i();

  private Tim[] envTextures;

  public RetailSubmap(final int cut, final NewRootStruct newRoot, final Vector2i screenOffset, final CollisionGeometry collisionGeometry) {
    this.cut = cut;
    this.newRoot = newRoot;

    this.hasRenderer_800c6968 = submapTypes_800f5cd4[cut] == 65;
    this.screenOffset = screenOffset;
    this.collisionGeometry = collisionGeometry;
  }

  @Override
  public void loadEnv(final Runnable onLoaded) {
    LOGGER.info("Loading submap cut %d environment", this.cut);

    final IntRef drgnIndex = new IntRef();
    final IntRef fileIndex = new IntRef();

    this.newRoot.getDrgnFile(this.cut, drgnIndex, fileIndex);

    drgnBinIndex_800bc058 = drgnIndex.get();
    loadDrgnDir(2, fileIndex.get(), files -> {
      this.loadBackground("DRGN2%d/%d".formatted(drgnIndex.get(), fileIndex.get()), files);
      this.prepareEnv();
      onLoaded.run();
    });
  }

  @Override
  public void loadAssets(final Runnable onLoaded) {
    LOGGER.info("Loading submap cut %d assets", this.cut);

    this.theEndStruct_800d4bd0 = null;
    this.theEndClut_800d4bd4 = null;
    this.theEndTim_800d4bf0 = null;

    if(this.cut == 673) { // End cutscene
      this.theEndStruct_800d4bd0 = new TheEndStructB0();
      this.theEndClut_800d4bd4 = new FileData(new byte[0x20]);
    }

    //LAB_800edeb4
    final IntRef drgnIndex = new IntRef();
    final IntRef fileIndex = new IntRef();

    this.newRoot.getDrgnFile(this.cut, drgnIndex, fileIndex);

    if(drgnIndex.get() == 1 || drgnIndex.get() == 2 || drgnIndex.get() == 3 || drgnIndex.get() == 4) {
      final int cutFileIndex = smapFileIndices_800f982c[this.cut];

      final AtomicInteger loadedCount = new AtomicInteger();
      final int expectedCount = cutFileIndex == 0 ? 1 : this.cut != 673 ? 2 : 3;

      // Load sobj assets
      final List<FileData> assets = new ArrayList<>();
      final List<FileData> scripts = new ArrayList<>();
      final List<FileData> textures = new ArrayList<>();
      final AtomicInteger assetsCount = new AtomicInteger();

      final Runnable prepareSobjs = () -> this.prepareSobjs(assets, scripts, textures);
      final Runnable prepareSobjsAndComplete = () -> allLoaded(loadedCount, expectedCount, prepareSobjs, onLoaded);

      loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 1, files -> allLoaded(assetsCount, 3, () -> assets.addAll(files), prepareSobjsAndComplete));
      loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 2, files -> allLoaded(assetsCount, 3, () -> scripts.addAll(files), prepareSobjsAndComplete));
      Unpacker.loadDirectory("SECT/DRGN%d.BIN/%d/textures".formatted(20 + drgnIndex.get(), fileIndex.get() + 1), files -> allLoaded(assetsCount, 3, () -> textures.addAll(files), prepareSobjsAndComplete));

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
            this.theEndTim_800d4bf0 = new Tim(file);
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
  public void draw() {
    if(!this.hasRenderer_800c6968) {
      return;
    }

    if(this.submapModel_800d4bf8.modelParts_00[0].obj == null) {
      for(int i = 0; i < this.submapModel_800d4bf8.modelParts_00.length; i++) {
        this.submapModel_800d4bf8.modelParts_00[i].obj = TmdObjLoader.fromObjTable("Submap model part " + i, this.submapModel_800d4bf8.modelParts_00[i].tmd_08);
      }
    }

    this.animateAndRenderSubmapModel(this.submapCutMatrix_800d4bb0);

    if(this.theEndStruct_800d4bd0 != null && this.theEndClut_800d4bd4 != null) {
      this.FUN_800ee9e0(this.theEndClut_800d4bd4, this.theEndStruct_800d4bd0, this.tpage_800f9e5c, this.clut_800f9e5e, Translucency.B_PLUS_F);
      GPU.uploadData15(this.theEndClutRect_800d6b48, this.theEndClut_800d4bd4);
    }
  }

  @Override
  public void unload() {
    this.theEndStruct_800d4bd0 = null;
    this.theEndClut_800d4bd4 = null;

    this.submapModel_800d4bf8.deleteModelParts();

    for(final EnvironmentRenderingMetrics24 environmentRenderingMetrics24 : this.envRenderMetrics_800cb710) {
      if(environmentRenderingMetrics24.obj != null) {
        environmentRenderingMetrics24.obj.delete();
        environmentRenderingMetrics24.obj = null;
      }
    }

    this.submapCutModel = null;
    this.submapCutAnim = null;
  }

  @Override
  public int getEncounterRate() {
    return encounterData_800f64c4[this.cut].rate_02;
  }

  @Override
  public void generateEncounter() {
    encounterId_800bb0f8 = sceneEncounterIds_800f74c4[encounterData_800f64c4[submapCut_80052c30].scene_00][this.randomEncounterIndex()];
    battleStage_800bb0f4 = encounterData_800f64c4[submapCut_80052c30].stage_03;
  }

  private void prepareEnv() {
    LOGGER.info("Submap cut %d preparing environment", this.cut);

    for(int i = 0; i < this.envTextureCount_800cb584; i++) {
      final EnvironmentRenderingMetrics24 renderPacket = this.envRenderMetrics_800cb710[i];
      for(int textureIndex = 0; textureIndex < this.envTextures.length; textureIndex++) {
        final Tim texture = this.envTextures[textureIndex];
        final Rect4i bounds = texture.getImageRect();

        final int tpX = (renderPacket.tpage_04 & 0b1111) * 64;
        final int tpY = (renderPacket.tpage_04 & 0b10000) != 0 ? 256 : 0;

        if(MathHelper.inBox(tpX, tpY, bounds.x, bounds.y, bounds.w, bounds.h)) {
          final SubmapEnvironmentTextureEvent event = EVENTS.postEvent(new SubmapEnvironmentTextureEvent(submapCut_80052c30, textureIndex + 3));
          renderPacket.texture = event.texture;
          break;
        }
      }
    }

    this.envTextures = null;
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
      if(file.real()) {
        this.pxls.add(new Tim(file));
      } else {
        this.pxls.add(null);
      }
    }

    this.loadTextures();
  }

  private void prepareMap(final Tim submapCutTexture, final MV submapCutMatrix) {
    LOGGER.info("Submap cut %d preparing map", this.cut);

    if(this.cut == 673) { // End cutscene
      this.uploadTheEndTim(this.theEndTim_800d4bf0, this.tpage_800f9e5c, this.clut_800f9e5e);
      GPU.downloadData15(this.theEndClutRect_800d6b48, this.theEndClut_800d4bd4);
      this.FUN_800eef6c(this.theEndClutRect_800d6b48, this.theEndClut_800d4bd4, this.theEndStruct_800d4bd0);
    }

    GPU.uploadData15(new Rect4i(1008, 256, submapCutTexture.getImageRect().w, submapCutTexture.getImageRect().h), submapCutTexture.getImageData());

    // The submap cut model is rendered without using the camera matrix, so we multiply its transforms
    // by the inverse of the camera matrix to cancel out the camera multiplication in the shader
    final Matrix4f inverseW2s = new Matrix4f(worldToScreenMatrix_800c3548).setTranslation(worldToScreenMatrix_800c3548.transfer)
      .invert();
    this.submapCutMatrix_800d4bb0
      .set(submapCutMatrix).setTranslation(submapCutMatrix.transfer)
      .mulLocal(inverseW2s);

    this.submapModel_800d4bf8.uvAdjustments_9d = new UvAdjustmentMetrics14(17, 1008, 256);
    initModel(this.submapModel_800d4bf8, this.submapCutModel, this.submapCutAnim);
  }

  @Override
  public void restoreAssets() {
    this.loadTextures();
  }

  private void loadTextures() {
    this.uvAdjustments.clear();

    int x = 576;
    int y = 256;
    for(int pxlIndex = 0; pxlIndex < this.pxls.size(); pxlIndex++) {
      final Tim tim = this.pxls.get(pxlIndex);

      if(tim != null) {
        final Rect4i imageRect = tim.getImageRect();
        final Rect4i clutRect = tim.getClutRect();

        imageRect.x = x;
        imageRect.y = y;
        clutRect.x = x;
        clutRect.y = y + imageRect.h;

        GPU.uploadData15(imageRect, tim.getImageData());
        GPU.uploadData15(clutRect, tim.getClutData());

        this.uvAdjustments.add(new UvAdjustmentMetrics14(pxlIndex + 1, x, y));

        x += tim.getImageRect().w;

        if(x >= 768) {
          x = 576;
          y += 128;
        }
      } else {
        this.uvAdjustments.add(UvAdjustmentMetrics14.NONE);
      }
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
  private void calculateSubmapBounds(final EnvironmentStruct[] envs, final int backgroundTextureCount) {
    int left = 0x7fff;
    int right = -0x8000;
    int top = 0x7fff;
    int bottom = -0x8000;

    //LAB_800e6dc8
    for(int i = 0; i < backgroundTextureCount; i++) {
      final EnvironmentStruct env = envs[i];

      if(env.s_06 == 0x4e) {
        if(right < env.pos_08.w + env.textureOffsetX_10) {
          right = env.pos_08.w + env.textureOffsetX_10;
        }

        if(left > env.textureOffsetX_10) {
          left = env.textureOffsetX_10;
        }

        if(bottom < env.pos_08.h + env.textureOffsetY_12) {
          bottom = env.pos_08.h + env.textureOffsetY_12;
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
  private void buildBackgroundRenderingPacket(final EnvironmentStruct[] env) {
    //LAB_800e6f9c
    for(int i = 0; i < this.envTextureCount_800cb584; i++) {
      final EnvironmentStruct s0 = env[i];

      int clutY = Math.abs(s0.clutY_22); // Negative means translucent
      if(i < this.envBackgroundTextureCount_800cb57c) { // It's a background texture
        if(clutY < 0x1f0 || clutY >= 0x200) {
          clutY = i + 0x1f0;
        }
      } else { // It's a foreground texture
        //LAB_800e7010
        this._800cbb90[i - this.envBackgroundTextureCount_800cb57c].set(s0.svec_14);
        this._800cbc90[i - this.envBackgroundTextureCount_800cb57c] = s0.ui_1c;
      }

      //LAB_800e7004
      //LAB_800e7074
      final EnvironmentRenderingMetrics24 renderPacket = this.envRenderMetrics_800cb710[i];

      float z;
      if(s0.s_06 == 0x4e) {
        //LAB_800e7148
        z = (0x1 << orderingTableBits_1f8003c0) - 1;
      } else if(s0.s_06 == 0x4f) {
        z = 40;
      } else {
        //LAB_800e7194
        z =
          worldToScreenMatrix_800c3548.m02 * s0.svec_00.x +
            worldToScreenMatrix_800c3548.m12 * s0.svec_00.y +
            worldToScreenMatrix_800c3548.m22 * s0.svec_00.z;
        z += worldToScreenMatrix_800c3548.transfer.z;
        z /= 1 << 16 - orderingTableBits_1f8003c0;
      }

      renderPacket.z_20 = Math.round(z);
      renderPacket.tpage_04 = s0.tpage_20;
      renderPacket.r_0c = 0x80;
      renderPacket.g_0d = 0x80;
      renderPacket.b_0e = 0x80;
      renderPacket.u_14 = s0.pos_08.x;
      renderPacket.v_15 = s0.pos_08.y;
      renderPacket.clut_16 = clutY << 6 | 0x30;
      renderPacket.w_18 = s0.pos_08.w;
      renderPacket.h_1a = s0.pos_08.h;

      //LAB_800e70ec
      renderPacket.offsetX_1c = s0.textureOffsetX_10;
      renderPacket.offsetY_1e = s0.textureOffsetY_12;

      //LAB_800e7210
      renderPacket.flags_22 &= 0x3fff;
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
    this.worldToScreenMatrix_800cbd68.set(worldToScreenMatrix_800c3548);
    this.worldToScreenMatrix_800cbd68.transpose(this.screenToWorldMatrix_800cbd40);
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
    if(x == 1024 && y == 1024) {
      this.envForegroundMetrics_800cb590[index].hidden_08 = true;
      return;
    }

    //LAB_800e76e8
    //LAB_800e76ec
    this.envForegroundMetrics_800cb590[index].x_00 = x;
    this.envForegroundMetrics_800cb590[index].y_04 = y;
    this.envForegroundMetrics_800cb590[index].hidden_08 = false;
  }

  @Method(0x800e770cL)
  public void resetSobjBounds() {
    this.minSobj_800cbd60 = 0;
    this.maxSobj_800cbd64 = this.objects.size();
  }

  @Method(0x800e7728L)
  public int FUN_800e7728(final int mode, final int foregroundTextureIndex, int z) {
    final int textureIndex = this.envBackgroundTextureCount_800cb57c + foregroundTextureIndex;

    if(mode == 1 && foregroundTextureIndex == -1) {
      //LAB_800e7780
      for(int i = 0; i < this.envForegroundTextureCount_800cb580; i++) {
        this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i].flags_22 &= 0x3fff;
        this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i].flags_22 |= 0x4000;
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
      this.envRenderMetrics_800cb710[textureIndex].flags_22 &= 0x3fff;
    } else if(mode == 1) {
      //LAB_800e77e8
      //LAB_800e7830
      this.envRenderMetrics_800cb710[textureIndex].flags_22 &= 0x3fff;
      this.envRenderMetrics_800cb710[textureIndex].flags_22 |= 0x4000;
      //LAB_800e7808
    } else if(mode == 2) {
      //LAB_800e788c
      this.envRenderMetrics_800cb710[textureIndex].flags_22 &= 0x3fff;
      this.envRenderMetrics_800cb710[textureIndex].flags_22 |= 0x8000;

      if(z < 40) {
        //LAB_800e78fc
        z = 40;
      } else if((0x1 << orderingTableBits_1f8003c0) - 1 < z) {
        z = (0x1 << orderingTableBits_1f8003c0) - 1;
      }

      //LAB_800e7900
      this.envRenderMetrics_800cb710[textureIndex].flags_22 &= 0xc000;
      this.envRenderMetrics_800cb710[textureIndex].flags_22 |= z & 0x3fff;
    } else if(mode == 5) {
      this.minSobj_800cbd60 = foregroundTextureIndex;
      this.maxSobj_800cbd64 = z + 1;
    }

    //LAB_800e7930
    //LAB_800e7934
    //LAB_800e7938
    return this.envRenderMetrics_800cb710[textureIndex].z_20;
  }

  @Method(0x800e7954L)
  public void renderEnvironment(final MV[] sobjMatrices, final int sobjCount) {
    final float[] sobjZs = new float[sobjCount];
    final float[] envZs = new float[this.envForegroundTextureCount_800cb580];

    //LAB_800e79b8
    // Render background
    for(int i = 0; i < this.envBackgroundTextureCount_800cb57c; i++) {
      final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[i];

      if(metrics.obj == null) {
        if(metrics.texture == null) {
          metrics.obj = new QuadBuilder("BackgroundTexture (index " + i + ')')
            .bpp(Bpp.of(metrics.tpage_04 >>> 7 & 0b11))
            .clut(768, metrics.clut_16 >>> 6)
            .vramPos((metrics.tpage_04 & 0b1111) * 64, (metrics.tpage_04 & 0b10000) != 0 ? 256 : 0)
            .pos(metrics.offsetX_1c, metrics.offsetY_1e, metrics.z_20 * 4.0f)
            .uv(metrics.u_14, metrics.v_15)
            .size(metrics.w_18, metrics.h_1a)
            .build();
        } else {
          metrics.obj = new QuadBuilder("BackgroundTexture (index " + i + ')')
            .bpp(Bpp.BITS_24)
            .pos(metrics.offsetX_1c, metrics.offsetY_1e, metrics.z_20 * 4.0f)
            .uv(metrics.u_14 * 8.0f, metrics.v_15 * 8.0f)
            .uvSize(metrics.w_18 * 8.0f, metrics.h_1a * 8.0f)
            .posSize(metrics.w_18, metrics.h_1a)
            .build();
        }
      }

      metrics.transforms.identity();
      metrics.transforms.transfer.set(GPU.getOffsetX() + this.submapOffsetX_800cb560 + this.screenOffset.x, GPU.getOffsetY() + this.submapOffsetY_800cb564 + this.screenOffset.y, 0.0f);
      final RenderEngine.QueuedModel model = RENDERER.queueOrthoModel(metrics.obj, metrics.transforms);

      if(metrics.texture != null) {
        model.texture(metrics.texture);
      }
    }

    //LAB_800e7a60
    //LAB_800e7a7c
    for(int i = 0; i < sobjCount; i++) {
      sobjZs[i] = (worldToScreenMatrix_800c3548.m02 * sobjMatrices[i].transfer.x +
        worldToScreenMatrix_800c3548.m12 * sobjMatrices[i].transfer.y +
        worldToScreenMatrix_800c3548.m22 * sobjMatrices[i].transfer.z + worldToScreenMatrix_800c3548.transfer.z) / (1 << 16 - orderingTableBits_1f8003c0);
    }

    //LAB_800e7b08
    //LAB_800e7b40
    for(int i = 0; i < this.envForegroundTextureCount_800cb580; i++) {
      final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i];

      final int flags = (metrics.flags_22 & 0xc000) >> 14;
      if(flags == 0x1) {
        //LAB_800e7bb4
        float minZ = Float.MAX_VALUE;
        float maxZ = 0;

        //LAB_800e7bbc
        int positiveZCount = 0;
        int negativeZCount = 0;

        //LAB_800e7c0c
        for(int sobjIndex = this.maxSobj_800cbd64 - 1; sobjIndex >= this.minSobj_800cbd60; sobjIndex--) {
          final float v1_0 = this._800cbc90[i] +
            this._800cbb90[i].x * sobjMatrices[sobjIndex].transfer.x +
            this._800cbb90[i].y * sobjMatrices[sobjIndex].transfer.y +
            this._800cbb90[i].z * sobjMatrices[sobjIndex].transfer.z;
          final float sobjZ = sobjZs[sobjIndex];

          if(sobjZ != 0xfffb) {
            if(v1_0 < 0) {
              negativeZCount++;
              if(minZ > sobjZ) {
                minZ = sobjZ;
              }
            } else {
              //LAB_800e7cac
              positiveZCount++;
              if(maxZ < sobjZ) {
                maxZ = sobjZ;
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
        envZs[i] = metrics.flags_22 & 0x3fff;
      } else {
        //LAB_800e7d78
        envZs[i] = metrics.z_20;
      }
    }

    //LAB_800e7d9c
    //LAB_800e7de0
    // Render overlays
    for(int i = 0; i < this.envForegroundTextureCount_800cb580; i++) {
      if(!this.envForegroundMetrics_800cb590[i].hidden_08) {
        final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i];

        if(metrics.obj == null) {
          if(metrics.texture == null) {
            metrics.obj = new QuadBuilder("CutoutTexture (index " + i + ')')
              .bpp(Bpp.of(metrics.tpage_04 >>> 7 & 0b11))
              .clut(768, metrics.clut_16 >>> 6)
              .vramPos((metrics.tpage_04 & 0b1111) * 64, (metrics.tpage_04 & 0b10000) != 0 ? 256 : 0)
              .pos(metrics.offsetX_1c, metrics.offsetY_1e, 0.0f)
              .uv(metrics.u_14, metrics.v_15)
              .size(metrics.w_18, metrics.h_1a)
              .build();
          } else {
            metrics.obj = new QuadBuilder("CutoutTexture (index " + i + ')')
              .bpp(Bpp.BITS_24)
              .pos(metrics.offsetX_1c, metrics.offsetY_1e, 0.0f)
              .uv(metrics.u_14 * 8.0f, metrics.v_15 * 8.0f)
              .uvSize(metrics.w_18 * 8.0f, metrics.h_1a * 8.0f)
              .posSize(metrics.w_18, metrics.h_1a)
              .build();
          }
        }

        // This was causing a problem when moving left from the room before Zackwell. Not sure if this is a retail issue or SC-specific. GH#332
        final float z = envZs[i];
        if(z < 0) {
          continue;
        }

        metrics.transforms.identity();
        metrics.transforms.transfer.set(GPU.getOffsetX() + this.submapOffsetX_800cb560 + this.screenOffset.x + this.envForegroundMetrics_800cb590[i].x_00, GPU.getOffsetY() + this.submapOffsetY_800cb564 + this.screenOffset.y + this.envForegroundMetrics_800cb590[i].y_04, z * 4.0f);
        final RenderEngine.QueuedModel model = RENDERER.queueOrthoModel(metrics.obj, metrics.transforms);

        if(metrics.texture != null) {
          model.texture(metrics.texture);
        }
      }
    }

    //LAB_800e7ed0
  }

  @Override
  @Method(0x800e7f68L)
  public void calcGoodScreenOffset(final float x, final float y) {
    if(x < -80) {
      this.screenOffset.x -= 80 + x;
      //LAB_800e7f80
    } else if(x > 80) {
      //LAB_800e7f9c
      this.screenOffset.x += 80 - x;
    }

    //LAB_800e7fa8
    if(y < -40) {
      this.screenOffset.y -= 40 + y;
      //LAB_800e7fbc
    } else if(y > 40) {
      //LAB_800e7fd4
      this.screenOffset.y += 40 - y;
    }

    //LAB_800e7fdc
    if(this._800f7f0c) {
      this.screenOffset.x += this._800cbd30;
      this.screenOffset.y += this._800cbd34;
      this._800f7f0c = false;
      return;
    }

    //LAB_800e8030
    if(this.screenOffset.x < this._800cb574) {
      //LAB_800e807c
      this.screenOffset.x = this._800cb574;
    } else {
      //LAB_800e8070
      this.screenOffset.x = Math.min(this._800cb570, this.screenOffset.x);
    }

    //LAB_800e8080
    //LAB_800e8088
    if(this.screenOffset.y < -this._800cb578) {
      this.screenOffset.y = -this._800cb578;
    } else {
      //LAB_800e80d0
      //LAB_800e80d8
      this.screenOffset.y = Math.min(this._800cb578, this.screenOffset.y);
    }

    //LAB_800e80dc
  }

  @Method(0x800e80e4L)
  public void FUN_800e80e4(final int x, final int y) {
    this._800cbd30 = x;
    this._800cbd34 = y;
    this._800f7f0c = true;
  }

  @Method(0x800ee9e0L)
  private void FUN_800ee9e0(final FileData a1, final TheEndStructB0 a2, final Vector2i tpage, final Vector2i clut, final Translucency transMode) {
    if(a2._08 == 500) {
      a2._00 = 1;
      a2._02 = 0;
      a2._06 = 1;
    }

    //LAB_800eea24
    if(a2._00 != 0) {
      if(a2._04 == 0) {
        if(a2._02 == 0) {
          a2._0c += 0x2_a800;

          if(a2._0c >>> 16 >= 0x100) {
            a2._0c = 0xff_0000;
            a2._02 = 1;
          }
        } else {
          //LAB_800eead8
          a2._0c -= 0x2_a800;

          if(a2._0c >>> 16 < 0x80) {
            a2._0c = 0x80_0000;
            a2._04 = 1;
          }
        }
      } else {
        //LAB_800eeb08
        a2._0c = 0x80_0000;
      }

      //LAB_800eeb0c
      GPU.queueCommand(40, new GpuCommandQuad()
        .vramPos(tpage.x, tpage.y >= 256 ? 256 : 0)
        .clut(clut.x, clut.y)
        .monochrome(a2._0c >> 16)
        .translucent(transMode)
        .pos(-188, 18, 192, 72)
        .uv(0, 128)
      );
    }

    //LAB_800eeb78
    if(a2._06 != 0) {
      this.FUN_800eec10(a1, a2);

      if(a2._08 == 561) {
        a2._06 = 0;
      }
    }

    //LAB_800eeba8
    //LAB_800eebac
    a2._08++;
  }

  @Method(0x800eec10L)
  private void FUN_800eec10(final FileData a1, final TheEndStructB0 a2) {
    //LAB_800eec1c
    for(int i = 0; i < 16; i++) {
      a2._50[i] += a2._10[i];

      final int v1 = a2._90[i];
      if(v1 < a2._50[i] >>> 16) {
        a2._50[i] = v1 << 16;
      }

      //LAB_800eec5c
      final int sp0 = a2._50[i] >> 16 << 10;
      final int sp2 = a2._50[i] >> 16 << 5;
      final int sp4 = a2._50[i] >> 16;
      a1.writeShort(i * 0x2, 0x8000 | sp0 | sp2 | sp4);
    }
  }

  @Method(0x800eece0L)
  private void animateAndRenderSubmapModel(final Matrix4f matrix) {
    this.submapModel_800d4bf8.coord2_14.coord.transfer.zero();
    this.submapModel_800d4bf8.coord2_14.transforms.rotate.zero();

    final int interpolationFrameCount = (2 - vsyncMode_8007a3b8) * 2 + 1;
    applyModelRotationAndScale(this.submapModel_800d4bf8);
    animateModel(this.submapModel_800d4bf8, interpolationFrameCount);
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

      RENDERER.queueModel(dobj2.obj, matrix, lw)
        .screenspaceOffset(this.screenOffset.x + 8, -this.screenOffset.y)
        .lightDirection(lightDirectionMatrix_800c34e8)
        .lightColour(lightColourMatrix_800c3508)
        .backgroundColour(GTE.backgroundColour);
    }

    //LAB_800eef0c
  }

  @Method(0x800eef6cL)
  private void FUN_800eef6c(final Rect4i imageRect, final FileData imageAddress, final TheEndStructB0 a2) {
    //LAB_800eef94
    for(int i = 0; i < 16; i++) {
      //LAB_800eefac
      a2._90[i] = imageAddress.readUShort(i * 0x2) & 0x1f;
      a2._10[i] = (a2._90[i] << 16) / 60;
      a2._50[i] = 0;
      imageAddress.writeShort(i * 0x2, 0x8000);
    }

    GPU.uploadData15(imageRect, imageAddress);
  }

  @Method(0x800f4244L)
  private void uploadTheEndTim(final Tim tim, final Vector2i tpageOut, final Vector2i clutOut) {
    //LAB_800f427c
    if(tim.hasClut()) {
      final Rect4i clutRect = tim.getClutRect();
      clutOut.set(clutRect.x, clutRect.y);
      GPU.uploadData15(clutRect, tim.getClutData());
    }

    //LAB_800f42d0
    final Rect4i imageRect = tim.getImageRect();
    tpageOut.set(imageRect.x, imageRect.y);
    GPU.uploadData15(imageRect, tim.getImageData());

    //LAB_800f4338
  }

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
