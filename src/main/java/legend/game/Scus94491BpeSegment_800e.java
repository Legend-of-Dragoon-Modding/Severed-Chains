package legend.game;

import legend.core.gpu.Rect4i;
import legend.core.gte.ModelPart10;
import legend.core.memory.Method;
import legend.game.tim.Tim;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.CContainer;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.Loader;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.initSound;
import static legend.game.Scus94491BpeSegment.loadMenuSounds;
import static legend.game.Scus94491BpeSegment.orderingTableBits_1f8003c0;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8002.loadBasicUiTexturesAndSomethingElse;
import static legend.game.Scus94491BpeSegment_8002.loadModelAndAnimation;
import static legend.game.Scus94491BpeSegment_8003.InitGeom;
import static legend.game.Scus94491BpeSegment_8003.ResetGraph;
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
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;

public final class Scus94491BpeSegment_800e {
  private Scus94491BpeSegment_800e() { }

  @Method(0x800e5d44L)
  public static void main() {
    gameInit();
    preload();
  }

  @Method(0x800e5d64L)
  public static void gameInit() {
    ResetGraph();

    orderingTableBits_1f8003c0 = 14;
    zShift_1f8003c4 = 0;
    orderingTableSize_1f8003c8 = 0x4000;
    zMax_1f8003cc = 0x3ffe;
    GPU.updateOrderingTableSize(orderingTableSize_1f8003c8);

    setDrawOffset();

    clearRed_8007a3a8 = 0;
    clearGreen_800bb104 = 0;
    clearBlue_800babc0 = 0;

    InitGeom();
    setProjectionPlaneDistance(640);
    initSound();

    engineStateOnceLoaded_8004dd24 = EngineStateEnum.PRELOAD_00;
    pregameLoadingStage_800bb10c = 0;
    vsyncMode_8007a3b8 = 2;
    tickCount_800bb0fc = 0;

    loadSystemFont();
    SCRIPTS.clear();
    loadShadow();
    FUN_800e6d60();
    initFmvs();
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
    final Tim font = new Tim(Loader.loadFile("font.tim"));

    final Rect4i imageRect = new Rect4i(832, 424, 64, 56);
    GPU.uploadData15(imageRect, font.getImageData());

    if(font.hasClut()) {
      final Rect4i clutRect = new Rect4i(832, 422, 32, 1);
      GPU.uploadData15(clutRect, font.getClutData());
    }
  }

  @Method(0x800e6998L)
  public static void loadShadow() {
    submapId_800bd808 = 0;

    new Tim(Loader.loadFile("shadow.tim")).uploadToGpu();

    //LAB_800e6af0
    final CContainer container = new CContainer("Shadow", Loader.loadFile("shadow.ctmd"));
    final TmdAnimationFile animation = new TmdAnimationFile(Loader.loadFile("shadow.anim"));

    shadowModel_800bda10.modelParts_00 = new ModelPart10[animation.modelPartCount_0c];
    Arrays.setAll(shadowModel_800bda10.modelParts_00, i -> new ModelPart10());

    loadModelAndAnimation(shadowModel_800bda10, container, animation);

    shadowModel_800bda10.coord2_14.transforms.rotate.zero();
    shadowModel_800bda10.uvAdjustments_9d = UvAdjustmentMetrics14.NONE;
    shadowModel_800bda10.shadowType_cc = 0;
  }

  @Method(0x800e6d60L)
  public static void FUN_800e6d60() {
    renderablePtr_800bdc5c = null;
  }

  @Method(0x800e6e6cL)
  public static void initFmvs() {
    _800bf0cf = 0;
  }
}
