package legend.game;

import legend.core.gpu.Bpp;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.ModelPart10;
import legend.core.memory.Method;
import legend.game.types.CContainer;
import legend.game.types.Model124;
import legend.game.types.TexPageY;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.SMap.loadTimImage;
import static legend.game.Scus94491BpeSegment.initSound;
import static legend.game.Scus94491BpeSegment.loadMenuSounds;
import static legend.game.Scus94491BpeSegment.orderingTableBits_1f8003c0;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.shadowAnimation_8001051c;
import static legend.game.Scus94491BpeSegment.shadowCContainer_800103d0;
import static legend.game.Scus94491BpeSegment.shadowTimFile_80010544;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002246c;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.loadBasicUiTexturesAndSomethingElse;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.prepareObjTable2;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.InitGeom;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.ResetGraph;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setDrawOffset;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8007.clearRed_8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b.clearBlue_800babc0;
import static legend.game.Scus94491BpeSegment_800b.clearGreen_800bb104;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.shadowModel_800bda10;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;
import static legend.game.Scus94491BpeSegment_800b.texPages_800bb110;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800c.timHeader_800c6748;

public final class Scus94491BpeSegment_800e {
  private Scus94491BpeSegment_800e() { }

  @Method(0x800e5d44L)
  public static void main() {
    gameInit();
    preload();
  }

  @Method(0x800e5d64L) //TODO can rename most of these functions
  public static void gameInit() {
    ResetGraph();

    orderingTableBits_1f8003c0.set(14);
    zShift_1f8003c4.set(0);
    orderingTableSize_1f8003c8.set(0x4000);
    zMax_1f8003cc.set(0x3ffe);
    GPU.updateOrderingTableSize(orderingTableSize_1f8003c8.get());

    setDrawOffset();

    clearRed_8007a3a8.set(0);
    clearGreen_800bb104.set(0);
    clearBlue_800babc0.set(0);

    InitGeom();
    setProjectionPlaneDistance(640);
    initSound();

    engineStateOnceLoaded_8004dd24 = EngineStateEnum.PRELOAD_00;
    pregameLoadingStage_800bb10c.set(0);
    vsyncMode_8007a3b8 = 2;
    tickCount_800bb0fc.set(0);

    precalculateTpages();
    loadSystemFont();
    SCRIPTS.clear();
    loadShadow();
    FUN_800e6d60();
    initFmvs();
  }

  @Method(0x800e60d8L)
  public static void precalculateTpages() {
    for(final Bpp bpp : Bpp.values()) {
      for(final Translucency trans : Translucency.values()) {
        texPages_800bb110.get(bpp).get(trans).get(TexPageY.Y_0).set(GetTPage(bpp, trans, 0, 0));
        texPages_800bb110.get(bpp).get(trans).get(TexPageY.Y_256).set(GetTPage(bpp, trans, 0, 256));
      }
    }
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
    final TimHeader header = parseTimHeader(timHeader_800c6748);

    final RECT imageRect = new RECT((short)832, (short)424, (short)64, (short)56);
    LoadImage(imageRect, header.getImageAddress());

    if(header.hasClut()) {
      final RECT clutRect = new RECT((short)832, (short)422, (short)32, (short)1);
      LoadImage(clutRect, header.getClutAddress());
    }
  }

  @Method(0x800e6998L)
  public static void loadShadow() {
    submapId_800bd808.set(0);

    loadTimImage(shadowTimFile_80010544.getAddress());

    //LAB_800e6af0
    final CContainer container = new CContainer("Shadow", new FileData(MEMORY.getBytes(shadowCContainer_800103d0.getAddress(), 0x14c)));
    final TmdAnimationFile animation = new TmdAnimationFile(new FileData(MEMORY.getBytes(shadowAnimation_8001051c.getAddress(), 0x28)));

    FUN_800e6b3c(shadowModel_800bda10, container, animation);

    shadowModel_800bda10.coord2_14.transforms.rotate.zero();
    shadowModel_800bda10.colourMap_9d = 0;
    shadowModel_800bda10.shadowType_cc = 0;
  }

  /** Very similar to {@link Scus94491BpeSegment_8002#FUN_80020718(Model124, CContainer, TmdAnimationFile)} */
  @Method(0x800e6b3cL)
  public static void FUN_800e6b3c(final Model124 model, final CContainer cContainer, final TmdAnimationFile tmdAnimFile) {
    final int x = model.coord2_14.coord.transfer.getX();
    final int y = model.coord2_14.coord.transfer.getY();
    final int z = model.coord2_14.coord.transfer.getZ();

    //LAB_800e6b7c
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    model.modelParts_00 = new ModelPart10[tmdAnimFile.modelPartCount_0c];

    Arrays.setAll(model.modelParts_00, i -> new ModelPart10());

    model.tpage_108 = (int)((cContainer.tmdPtr_00.id & 0xffff0000L) >>> 11);

    if(cContainer.ptr_08 != null) {
      model.ptr_a8 = cContainer.ptr_08;

      //LAB_800e6c00
      for(int i = 0; i < 7; i++) {
        model.ptrs_d0[i] = model.ptr_a8._00[i];
        FUN_8002246c(model, i);
      }
    } else {
      //LAB_800e6c44
      model.ptr_a8 = null; //TODO was this needed? cContainer.ptr_08.getAddress();

      //LAB_800e6c54
      for(int i = 0; i < 7; i++) {
        model.ptrs_d0[i] = null;
      }
    }

    //LAB_800e6c64
    initObjTable2(model.modelParts_00);
    GsInitCoordinate2(null, model.coord2_14);
    prepareObjTable2(model.modelParts_00, cContainer.tmdPtr_00.tmd, model.coord2_14);

    model.zOffset_a0 = 0;
    model.disableInterpolation_a2 = false;
    model.ub_a3 = 0;
    model.partInvisible_f4 = 0;

    loadModelStandardAnimation(model, tmdAnimFile);

    model.coord2_14.coord.transfer.set(x, y, z);
    model.shadowType_cc = 0;
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
    model.shadowSize_10c.set(1.0f, 1.0f, 1.0f);
    model.shadowOffset_118.set(0, 0, 0);
  }

  @Method(0x800e6d60L)
  public static void FUN_800e6d60() {
    renderablePtr_800bdc5c = null;
  }

  @Method(0x800e6e6cL)
  public static void initFmvs() {
    _800bf0cf.setu(0);
  }
}
