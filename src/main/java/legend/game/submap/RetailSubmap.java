package legend.game.submap;

import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.memory.Method;
import legend.core.memory.types.IntRef;
import legend.core.opengl.TmdObjLoader;
import legend.game.scripting.ScriptFile;
import legend.game.tim.Tim;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.CContainer;
import legend.game.types.Model124;
import legend.game.types.NewRootStruct;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static legend.core.Async.allLoaded;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.animateModel;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;

public class RetailSubmap extends Submap {
  public final int cut;
  private final NewRootStruct newRoot;
  private final Vector2i screenOffset;

  private final List<Tim> pxls = new ArrayList<>();

  private final boolean hasRenderer_800c6968;

  private final Matrix4f submapCutMatrix_800d4bb0 = new Matrix4f();

  private TheEndStructB0 theEndStruct_800d4bd0;
  private FileData theEndClut_800d4bd4;

  private CContainer submapCutModel;
  private TmdAnimationFile submapCutAnim;
  private Tim theEndTim_800d4bf0;

  private final Model124 submapModel_800d4bf8 = new Model124("Submap");

  private final Rect4i theEndClutRect_800d6b48 = new Rect4i(576, 368, 16, 1);

  private final Vector2i tpage_800f9e5c = new Vector2i();
  private final Vector2i clut_800f9e5e = new Vector2i();

  public RetailSubmap(final int cut, final NewRootStruct newRoot, final Vector2i screenOffset) {
    this.cut = cut;
    this.newRoot = newRoot;

    this.hasRenderer_800c6968 = submapTypes_800f5cd4[cut] == 65;
    this.screenOffset = screenOffset;
  }

  @Override
  public void loadAssets(final Runnable onLoaded) {
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

      final List<FileData> assets = new ArrayList<>();
      final List<FileData> scripts = new ArrayList<>();
      final List<FileData> textures = new ArrayList<>();
      final AtomicInteger assetsCount = new AtomicInteger();
      final AtomicInteger loadedCount = new AtomicInteger();
      final int expectedCount = cutFileIndex == 0 ? 1 : 2;

      final Runnable prepareSobjs = () -> this.prepareSobjs(assets, scripts, textures);
      final Runnable prepareSobjsAndComplete = () -> allLoaded(loadedCount, expectedCount, prepareSobjs, onLoaded);

      loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 1, files -> allLoaded(assetsCount, 3, () -> assets.addAll(files), prepareSobjsAndComplete));
      loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 2, files -> allLoaded(assetsCount, 3, () -> scripts.addAll(files), prepareSobjsAndComplete));
      Unpacker.loadDirectory("SECT/DRGN%d.BIN/%d/textures".formatted(20 + drgnIndex.get(), fileIndex.get() + 1), files -> allLoaded(assetsCount, 3, () -> textures.addAll(files), prepareSobjsAndComplete));

      if(cutFileIndex != 0) {
        final Tim[] submapCutTexture = new Tim[1];
        final MV[] submapCutMatrix = new MV[1];
        final AtomicInteger cutCount = new AtomicInteger();
        final int expectedCutCount = this.cut == 673 ? 3 : 2;

        final Runnable prepareMap = () -> this.prepareMap(submapCutTexture[0], submapCutMatrix[0]);
        final Runnable prepareMapAndComplete = () -> allLoaded(loadedCount, expectedCount, prepareMap, onLoaded);

        if(this.cut == 673) { // End cutscene, loads "The End" TIM
          loadDrgnFile(0, 7610, file -> allLoaded(cutCount, expectedCutCount, () -> {
            this.theEndTim_800d4bf0 = new Tim(file);
          }, prepareMapAndComplete));
        }

        // File example: 7508
        loadDrgnDir(0, cutFileIndex, files -> allLoaded(cutCount, expectedCutCount, () -> {
          this.submapCutModel = new CContainer("DRGN0/" + cutFileIndex, files.get(0));
          this.submapCutAnim = new TmdAnimationFile(files.get(1));
        }, prepareMapAndComplete));

        loadDrgnDir(0, cutFileIndex + 1, files -> allLoaded(cutCount, expectedCutCount, () -> {
          submapCutTexture[0] = new Tim(files.get(0));
          submapCutMatrix[0] = new MV();
          files.get(1).readMv(0, submapCutMatrix[0]);
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

  private void prepareSobjs(final List<FileData> assets, final List<FileData> scripts, final List<FileData> textures) {
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

    this.submapModel_800d4bf8.uvAdjustments_9d = new UvAdjustmentMetrics14(17, 0x5c3f0000, 0x3c0ffff, 0x1f0000, 0xffe0ffff, 0xc0); // 1008, 256, submap cut model
    this.submapModel_800d4bf8.uvAnimationSecondaryBank = true;
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

        this.uvAdjustments.add(this.createUvAdjustments(pxlIndex, x, y));

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

  private UvAdjustmentMetrics14 createUvAdjustments(final int index, final int x, final int y) {
    final int clutX = x / 16;
    final int clutY = y + 112;
    final int tpageX = x / 64;
    final int tpageY = y / 256;
    final int u = x % 64 * 4;
    final int v = y % 256;
    final int clut = clutX | clutY << 6;
    final int tpage = tpageX | tpageY << 4;
    final int uv = u | v << 8;

    return new UvAdjustmentMetrics14(index + 1, clut << 16, 0x3c0ffff, tpage << 16, 0xffe0ffff, uv);
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
