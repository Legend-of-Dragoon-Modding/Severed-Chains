package legend.game;

import legend.core.gpu.Bpp;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.Tmd;
import legend.core.memory.Method;
import legend.game.types.ExtendedTmd;
import legend.game.types.Model124;
import legend.game.types.TexPageY;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.FUN_80019500;
import static legend.game.Scus94491BpeSegment._1f8003fc;
import static legend.game.Scus94491BpeSegment.allocateHeap;
import static legend.game.Scus94491BpeSegment.extendedTmd_800103d0;
import static legend.game.Scus94491BpeSegment.heap_8011e210;
import static legend.game.Scus94491BpeSegment.loadMenuSounds;
import static legend.game.Scus94491BpeSegment.orderingTableBits_1f8003c0;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.ovalBlobTimHeader_80010548;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.tmdAnimFile_8001051c;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002246c;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.loadBasicUiTexturesAndSomethingElse;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.prepareObjTable2;
import static legend.game.Scus94491BpeSegment_8002.setCdMix;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c5e0;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsDefDispBuff;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsInitGraph;
import static legend.game.Scus94491BpeSegment_8003.InitGeom;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.ResetGraph;
import static legend.game.Scus94491BpeSegment_8003.SetGraphDebug;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004._8004dd30;
import static legend.game.Scus94491BpeSegment_8004.enableAudioSource;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndexOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
import static legend.game.Scus94491BpeSegment_8007._8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bb104;
import static legend.game.Scus94491BpeSegment_800b._800bb228;
import static legend.game.Scus94491BpeSegment_800b._800bb348;
import static legend.game.Scus94491BpeSegment_800b._800bdb90;
import static legend.game.Scus94491BpeSegment_800b._800bdc24;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b._800bf0d0;
import static legend.game.Scus94491BpeSegment_800b.afterFmvLoadingStage_800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.array_800bb198;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.fmvIndex_800bf0dc;
import static legend.game.Scus94491BpeSegment_800b.fmvStage_800bf0d8;
import static legend.game.Scus94491BpeSegment_800b.model_800bda10;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.submapIndex_800bd808;
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
    ResetGraph(0);
    SetGraphDebug(2);

    GsInitGraph((short)640, (short)480, 0b110100);
    GsDefDispBuff((short)0, (short)16, (short)0, (short)16);

    orderingTableBits_1f8003c0.set(14);
    zShift_1f8003c4.set(0);
    orderingTableSize_1f8003c8.set(0x4000);
    zMax_1f8003cc.set(0x3ffe);
    GPU.updateOrderingTableSize(orderingTableSize_1f8003c8.get());

    FUN_8003c5e0();

    _8007a3a8.set(0);
    _800bb104.set(0);
    _800babc0.set(0);

    InitGeom();
    setProjectionPlaneDistance(640);
    FUN_80019500();

    mainCallbackIndexOnceLoaded_8004dd24.set(0);
    pregameLoadingStage_800bb10c.set(0);
    vsyncMode_8007a3b8.set(2);
    tickCount_800bb0fc.set(0);

    precalculateTpages();
    loadSystemFont();
    SCRIPTS.clear();
    allocateHeap(heap_8011e210.getAddress(), 0x3d_edf0L);
    loadOvalBlobTexture();
    FUN_800e6d60();
    initFmvs();
  }

  @Method(0x800e5fc0L)
  public static void finalizePregameLoading() {
    throw new RuntimeException("No longer used");
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
    drgnBinIndex_800bc058.set(1);

    loadMenuSounds();
    setWidthAndFlags(320);
    vsyncMode_8007a3b8.set(2);

    //LAB_800e600c
    loadBasicUiTexturesAndSomethingElse();

    //LAB_800e6040
    _8004dd30.setu(0);
    fmvIndex_800bf0dc.setu(0);
    afterFmvLoadingStage_800bf0ec.set(2);
  }

  @Method(0x800e6524L)
  public static void loadSystemFont() {
    final TimHeader header = parseTimHeader(timHeader_800c6748);

    final RECT imageRect = new RECT((short)832, (short)424, (short)64, (short)56);
    LoadImage(imageRect, header.getImageAddress());

    _800bb348.setu(texPages_800bb110.get(Bpp.BITS_4).get(Translucency.HALF_B_PLUS_HALF_F).get(TexPageY.Y_256).get()).oru(0xdL);

    if(header.hasClut()) {
      final RECT clutRect = new RECT((short)832, (short)422, (short)32, (short)1);
      LoadImage(clutRect, header.getClutAddress());
    }

    //LAB_800e65c4
    _1f8003fc.setu(_800bb228.getAddress());

    //LAB_800e65e8
    for(int i = 2; i < 37; i++) {
      long v1 = 0xffff_ffffL;
      long a1 = 0x1L;

      //LAB_800e65fc
      while(v1 >= i) {
        a1 *= i;
        v1 /= i;
      }

      //LAB_800e6620
      array_800bb198.get(i - 2).set(a1);
    }
  }

  @Method(0x800e6998L)
  public static void loadOvalBlobTexture() {
    submapIndex_800bd808.set(0);

    final TimHeader header = parseTimHeader(ovalBlobTimHeader_80010548);
    LoadImage(header.getImageRect(), header.getImageAddress());

    if(header.hasClut()) {
      LoadImage(header.getClutRect(), header.getClutAddress());
    }

    //LAB_800e6af0
    FUN_800e6b3c(model_800bda10, extendedTmd_800103d0, tmdAnimFile_8001051c);

    model_800bda10.coord2Param_64.rotate.x.set((short)0);
    model_800bda10.coord2Param_64.rotate.y.set((short)0);
    model_800bda10.coord2Param_64.rotate.z.set((short)0);
    model_800bda10.colourMap_9d = 0;
    model_800bda10.b_cc = 0;
  }

  /** Very similar to {@link Scus94491BpeSegment_8002#FUN_80020718(Model124, ExtendedTmd, TmdAnimationFile)} */
  @Method(0x800e6b3cL)
  public static void FUN_800e6b3c(final Model124 model, final ExtendedTmd extendedTmd, final TmdAnimationFile tmdAnimFile) {
    final int x = model.coord2_14.coord.transfer.getX();
    final int y = model.coord2_14.coord.transfer.getY();
    final int z = model.coord2_14.coord.transfer.getZ();

    //LAB_800e6b7c
    for(int i = 0; i < 7; i++) {
      model.aub_ec[i] = 0;
    }

    model.dobj2ArrPtr_00 = new GsDOBJ2[tmdAnimFile.count_0c.get()];
    model.coord2ArrPtr_04 = new GsCOORDINATE2[tmdAnimFile.count_0c.get()];
    model.coord2ParamArrPtr_08 = new GsCOORD2PARAM[tmdAnimFile.count_0c.get()];
    model.count_c8 = tmdAnimFile.count_0c.get();

    Arrays.setAll(model.dobj2ArrPtr_00, i -> new GsDOBJ2());
    Arrays.setAll(model.coord2ArrPtr_04, i -> new GsCOORDINATE2());
    Arrays.setAll(model.coord2ParamArrPtr_08, i -> new GsCOORD2PARAM());

    final Tmd tmd = extendedTmd.tmdPtr_00.deref().tmd;
    model.tmd_8c = tmd;
    model.tmdNobj_ca = tmd.header.nobj.get();
    model.scaleVector_fc.setPad((int)((extendedTmd.tmdPtr_00.deref().id.get() & 0xffff0000L) >>> 11));

    final long v0 = extendedTmd.ptr_08.get();
    if(v0 == 0) {
      //LAB_800e6c44
      model.ptr_a8 = extendedTmd.ptr_08.getAddress();

      //LAB_800e6c54
      for(int i = 0; i < 7; i++) {
        model.ptrs_d0[i] = 0;
      }
    } else {
      model.ptr_a8 = extendedTmd.getAddress() + v0 / 4 * 4;

      //LAB_800e6c00
      for(int i = 0; i < 7; i++) {
        model.ptrs_d0[i] = model.ptr_a8 + MEMORY.ref(4, model.ptr_a8).offset(i * 0x4L).get() / 4 * 4;
        FUN_8002246c(model, i);
      }
    }

    //LAB_800e6c64
    adjustTmdPointers(model.tmd_8c);
    initObjTable2(model.ObjTable_0c, model.dobj2ArrPtr_00, model.coord2ArrPtr_04, model.coord2ParamArrPtr_08, model.count_c8);
    model.coord2_14.param = model.coord2Param_64;
    GsInitCoordinate2(null, model.coord2_14);
    prepareObjTable2(model.ObjTable_0c, model.tmd_8c, model.coord2_14, model.count_c8, model.tmdNobj_ca + 1);

    model.zOffset_a0 = 0;
    model.ub_a2 = 0;
    model.ub_a3 = 0;
    model.ui_f4 = 0;

    loadModelStandardAnimation(model, tmdAnimFile);

    model.coord2_14.coord.transfer.setX(x);
    model.coord2_14.coord.transfer.setY(y);
    model.coord2_14.coord.transfer.setZ(z);
    model.b_cc = 0;
    model.scaleVector_fc.setX(0x1000);
    model.scaleVector_fc.setY(0x1000);
    model.scaleVector_fc.setZ(0x1000);
    model.vector_10c.setX(0x1000);
    model.vector_10c.setY(0x1000);
    model.vector_10c.setZ(0x1000);
    model.vector_118.setX(0);
    model.vector_118.setY(0);
    model.vector_118.setZ(0);
  }

  @Method(0x800e6d60L)
  public static void FUN_800e6d60() {
    _800bdb90.setu(0);
    _800bdc24.setu(0);
    renderablePtr_800bdc5c = null;
  }

  @Method(0x800e6e6cL)
  public static void initFmvs() {
    enableAudioSource(0x1L, 0x1L);
    setCdVolume(0x7f, 0x7f);
    setCdMix(0x3f);

    _800bf0cf.setu(0);
    _800bf0d0.setu(0);
    fmvStage_800bf0d8.setu(0);
  }
}
