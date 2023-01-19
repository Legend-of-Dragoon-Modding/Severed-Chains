package legend.game;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.GsOBJTABLE2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.game.inventory.UseItemResponse;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.CharSwapScreen;
import legend.game.inventory.screens.LoadGameScreen;
import legend.game.inventory.screens.MenuScreen;
import legend.game.inventory.screens.SaveGameScreen;
import legend.game.inventory.screens.ShopScreen;
import legend.game.inventory.screens.TooManyItemsScreen;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.tim.Tim;
import legend.game.tmd.Renderer;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.Drgn0_6666Entry;
import legend.game.types.Drgn0_6666Struct;
import legend.game.types.ExtendedTmd;
import legend.game.types.GameState52c;
import legend.game.types.InventoryMenuState;
import legend.game.types.ItemStats0c;
import legend.game.types.LodString;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuItemStruct04;
import legend.game.types.Model124;
import legend.game.types.ModelPartTransforms;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.SpuStruct10;
import legend.game.types.SpuStruct28;
import legend.game.types.Struct84;
import legend.game.types.Textbox4c;
import legend.game.types.TextboxArrow0c;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.SItem.FUN_80103b10;
import static legend.game.SItem.equipmentStats_80111ff0;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderMenus;
import static legend.game.SItem.renderPostCombatReport;
import static legend.game.SMap.FUN_800d9e64;
import static legend.game.SMap.FUN_800da114;
import static legend.game.SMap.FUN_800da524;
import static legend.game.SMap.FUN_800de004;
import static legend.game.SMap.FUN_800e2428;
import static legend.game.SMap.FUN_800e3fac;
import static legend.game.SMap.FUN_800e4018;
import static legend.game.SMap.FUN_800e4708;
import static legend.game.SMap.FUN_800e4e5c;
import static legend.game.SMap.FUN_800e4f8c;
import static legend.game.SMap.FUN_800e519c;
import static legend.game.SMap.FUN_800e5534;
import static legend.game.SMap.FUN_800e6730;
import static legend.game.SMap.FUN_800e828c;
import static legend.game.SMap.FUN_800e8e50;
import static legend.game.SMap.FUN_800ea4c8;
import static legend.game.SMap._800f7e54;
import static legend.game.SMap.encounterAccumulator_800c6ae8;
import static legend.game.SMap.handleEncounters;
import static legend.game.SMap.playerPos_800c68e8;
import static legend.game.SMap.renderSmapModel;
import static legend.game.SMap.unloadSmap;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001ae90;
import static legend.game.Scus94491BpeSegment.FUN_8001e010;
import static legend.game.Scus94491BpeSegment._80010868;
import static legend.game.Scus94491BpeSegment._800108b0;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.decrementOverlayCount;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.mallocHead;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.rectArray28_80010770;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment_8003.CdMix;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003fd80;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrix;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.TransposeMatrix;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c390;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d034;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixX;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixY;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixZ;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8004.loadingGameStateOverlay_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
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
import static legend.game.Scus94491BpeSegment_8005.lodXa00Xa_80052c74;
import static legend.game.Scus94491BpeSegment_8005.lodXa00Xa_80052c94;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c3c;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8005.textboxVramX_80052bc8;
import static legend.game.Scus94491BpeSegment_8005.textboxVramY_80052bf4;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.joypadRepeat_8007a3a0;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bd7ac;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b._800bd7b4;
import static legend.game.Scus94491BpeSegment_800b._800bd7b8;
import static legend.game.Scus94491BpeSegment_800b._800bd80c;
import static legend.game.Scus94491BpeSegment_800b._800bdb88;
import static legend.game.Scus94491BpeSegment_800b._800bdc58;
import static legend.game.Scus94491BpeSegment_800b._800bdf04;
import static legend.game.Scus94491BpeSegment_800b._800bdf08;
import static legend.game.Scus94491BpeSegment_800b._800bdf10;
import static legend.game.Scus94491BpeSegment_800b._800bdf18;
import static legend.game.Scus94491BpeSegment_800b._800bdf38;
import static legend.game.Scus94491BpeSegment_800b._800be5b8;
import static legend.game.Scus94491BpeSegment_800b._800be5bc;
import static legend.game.Scus94491BpeSegment_800b._800be5c0;
import static legend.game.Scus94491BpeSegment_800b._800be5c4;
import static legend.game.Scus94491BpeSegment_800b._800be5c8;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b._800beb98;
import static legend.game.Scus94491BpeSegment_800b._800bed28;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b.currentText_800bdca0;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.equipmentStats_800be5d8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.hasNoEncounters_800bed58;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.spu10Arr_800bd610;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.textboxArrows_800bdea0;
import static legend.game.Scus94491BpeSegment_800b.textboxes_800be358;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800e.main;
import static legend.game.WMap.FUN_800c8844;
import static legend.game.WMap.FUN_800c8d90;
import static legend.game.WMap.renderWmapModel;
import static legend.game.combat.Bttl_800e.FUN_800ec258;
import static legend.game.combat.Bttl_800e.renderBttlModel;

public final class Scus94491BpeSegment_8002 {
  private Scus94491BpeSegment_8002() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8002.class);

  @Method(0x80020008L)
  public static void sssqResetStuff() {
    FUN_8001ad18();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
    unloadSoundFile(8);
    FUN_800201c8(6);
  }

  @Method(0x80020060L)
  public static FlowControl FUN_80020060(final RunningScript<?> script) {
    FUN_8001ad18();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
    unloadSoundFile(8);
    FUN_800201c8(6);
    return FlowControl.CONTINUE;
  }

  @Method(0x8002013cL)
  public static FlowControl FUN_8002013c(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800201c8L)
  public static void FUN_800201c8(final int index) {
    final SpuStruct10 struct10 = spu10Arr_800bd610[index];

    if(struct10._00 != 0) {
      FUN_8004d034(struct10.channelIndex_0c, 1);
      FUN_8004c390(struct10.channelIndex_0c);
      free(struct10.sssq_08.getAddress());
      struct10.sssq_08 = null;
      struct10._00 = 0;
    }

    //LAB_80020220
  }

  @Method(0x80020230L)
  public static FlowControl FUN_80020230(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800202a4L)
  public static FlowControl FUN_800202a4(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80020308L)
  public static void FUN_80020308() {
    FUN_8001ae90();
    FUN_8001ad18();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
  }

  @Method(0x80020360L)
  public static void FUN_80020360(final ArrayRef<SpuStruct28> a0, final ArrayRef<SpuStruct28> a1) {
    //LAB_8002036c
    for(int i = 0; i < 32; i++) {
      final SpuStruct28 a0_0 = a0.get(i);
      final SpuStruct28 a1_0 = a1.get(i);

      //LAB_80020378
      memcpy(a1_0.getAddress(), a0_0.getAddress(), 0x28);

      if(a1_0.type_00.get() == 4) {
        if(a1_0._1c.get() != 0) {
          a1_0.type_00.set(3);
        }
      }

      //LAB_800203d8
    }
  }

  @Method(0x800203f0L)
  public static FlowControl FUN_800203f0(final RunningScript<?> script) {
    unloadSoundFile(3);
    //TODO GH#3
//    loadedDrgnFiles_800bcf78.oru(0x10L);
//    loadDrgnBinFile(0, 1290 + script.params_20.get(0).deref().get(), 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
    return FlowControl.CONTINUE;
  }

  @Method(0x80020460L)
  public static void FUN_80020460() {
    // empty
  }

  @Method(0x80020468L)
  public static void FUN_80020468(final GsDOBJ2 dobj2, final long a1) {
    final TmdObjTable objTable = dobj2.tmd_08;
    long primitives = objTable.primitives_10.getPointer();
    long count = objTable.n_primitive_14.get();

    //LAB_80020494
    while(count != 0) {
      final long command = MEMORY.ref(4, primitives).get() & 0xff04_0000L;
      final long primitiveCount = MEMORY.ref(2, primitives).get();

      //LAB_8002053c
      //LAB_8002058c
      //LAB_800205b0
      if(command == 0x3c00_0000L || command == 0x3e00_0000L) {
        //LAB_800206a0
        FUN_800210c4(primitives, primitiveCount, a1);
        count -= primitiveCount;
        primitives += primitiveCount * 0x24L;
      } else if(command == 0x3d00_0000L || command == 0x3f00_0000L) {
        FUN_8002117c(primitives, primitiveCount, a1);
        count -= primitiveCount;
        primitives += primitiveCount * 0x2cL;
      } else if(command == 0x3804_0000L || command == 0x3a04_0000L) {
        FUN_80021060(primitives, primitiveCount);
        count -= primitiveCount;
        primitives += primitiveCount * 0x24L;
      } else if(command == 0x3800_0000L || command == 0x3a00_0000L) {
        FUN_80021050(primitives, primitiveCount);
        count -= primitiveCount;
        primitives += primitiveCount * 0x18L;
      } else if(command == 0x3500_0000L || command == 0x3700_0000L) {
        FUN_80021120(primitives, primitiveCount, a1);
        count -= primitiveCount;
        primitives += primitiveCount * 0x24L;
      } else if(command == 0x3400_0000L || command == 0x3600_0000L) {
        FUN_80021068(primitives, primitiveCount, a1);
        count -= primitiveCount;
        primitives += primitiveCount * 0x1cL;
      } else if(command == 0x3004_0000L || command == 0x3204_0000L) {
        FUN_80021058(primitives, primitiveCount);
        count -= primitiveCount;
        primitives += primitiveCount * 0x1cL;
      } else if(command == 0x3000_0000L || command == 0x3200_0000L) {
        FUN_80021048(primitives, primitiveCount);
        count -= primitiveCount;
        primitives += primitiveCount * 0x14L;
      }

      //LAB_800206f8
    }

    //LAB_80020700
  }

  /** Very similar to {@link Scus94491BpeSegment_800e#FUN_800e6b3c(Model124, ExtendedTmd, TmdAnimationFile)} */
  @Method(0x80020718L)
  public static void FUN_80020718(final Model124 model, final ExtendedTmd extendedTmd, final TmdAnimationFile tmdAnimFile) {
    LOGGER.info("Loading scripted TMD %08x (animation %08x)", extendedTmd.getAddress(), tmdAnimFile.getAddress());

    final int transferX = model.coord2_14.coord.transfer.getX();
    final int transferY = model.coord2_14.coord.transfer.getY();
    final int transferZ = model.coord2_14.coord.transfer.getZ();

    //LAB_80020760
    for(int i = 0; i < 7; i++) {
      model.aub_ec[i] = 0;
    }

    final Tmd tmd = extendedTmd.tmdPtr_00.deref().tmd;
    model.tmd_8c = tmd;
    model.tmdNobj_ca = tmd.header.nobj.get();

    if(mainCallbackIndex_8004dd20.get() == 5) { // SMAP
      FUN_800de004(model, extendedTmd);
    }

    //LAB_8002079c
    model.tpage_108 = (int)((extendedTmd.tmdPtr_00.deref().id.get() & 0xffff_0000L) >>> 11); // LOD uses the upper 16 bits of TMD IDs as tpage (sans VRAM X/Y)

    final long v0 = extendedTmd.ptr_08.get();
    if(v0 == 0) {
      //LAB_80020818
      model.ptr_a8 = extendedTmd.ptr_08.getAddress(); //TODO

      //LAB_80020828
      for(int i = 0; i < 7; i++) {
        model.ptrs_d0[i] = 0;
      }
    } else {
      model.ptr_a8 = extendedTmd.getAddress() + v0 / 4 * 4;

      //LAB_800207d4
      for(int i = 0; i < 7; i++) {
        //TODO make aui_d0 array of pointers to unsigned ints (also pointers but to what?)
        //TODO also ui_a8 is a pointer to a relative pointer?
        model.ptrs_d0[i] = model.ptr_a8 + MEMORY.ref(4, model.ptr_a8).offset(i * 0x4L).get() / 4 * 4;
        FUN_8002246c(model, i);
      }
    }

    //LAB_80020838
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

    model.coord2_14.coord.transfer.setX(transferX);
    model.coord2_14.coord.transfer.setY(transferY);
    model.coord2_14.coord.transfer.setZ(transferZ);

    int s1 = 0;
    //LAB_80020940
    if(mainCallbackIndex_8004dd20.get() == 5) { // SMAP
      //LAB_80020958
      for(int i = 0; i < model.ObjTable_0c.nobj; i++) {
        FUN_800d9e64(model.ObjTable_0c.top[s1++], model.colourMap_9d);
      }

      //LAB_80020978
    } else if(mainCallbackIndex_8004dd20.get() == 8) { // WMAP
      //LAB_80020990
      for(int i = 0; i < model.ObjTable_0c.nobj; i++) {
        FUN_800c8844(model.ObjTable_0c.top[s1++], model.colourMap_9d);
      }

      //LAB_800209ac
    } else {
      //LAB_8002091c
      for(int i = 0; i < model.ObjTable_0c.nobj; i++) {
        FUN_80020468(model.ObjTable_0c.top[s1++], model.colourMap_9d);
      }
    }

    //LAB_800209b0
    model.b_cc = 0;
    model.b_cd = -2;
    model.scaleVector_fc.set(0x1000, 0x1000, 0x1000);
    model.vector_10c.set(0x1000, 0x1000, 0x1000);
    model.vector_118.set(0, 0, 0);
  }

  @Method(0x80020a00L)
  public static void initModel(final Model124 model, final ExtendedTmd extendedTmd, final TmdAnimationFile tmdAnimFile) {
    model.count_c8 = extendedTmd.tmdPtr_00.deref().tmd.header.nobj.get();

    model.dobj2ArrPtr_00 = new GsDOBJ2[model.count_c8];
    model.coord2ArrPtr_04 = new GsCOORDINATE2[model.count_c8];
    model.coord2ParamArrPtr_08 = new GsCOORD2PARAM[model.count_c8];

    Arrays.setAll(model.dobj2ArrPtr_00, i -> new GsDOBJ2());
    Arrays.setAll(model.coord2ArrPtr_04, i -> new GsCOORDINATE2());
    Arrays.setAll(model.coord2ParamArrPtr_08, i -> new GsCOORD2PARAM());

    FUN_80020718(model, extendedTmd, tmdAnimFile);
  }

  @Method(0x80020b98L)
  public static void animateModel(final Model124 model) {
    if(mainCallbackIndex_8004dd20.get() == 5) { // SMAP
      FUN_800da114(model);
      return;
    }

    //LAB_80020be8
    //LAB_80020bf0
    for(int i = 0; i < 7; i++) {
      if(model.aub_ec[i] != 0) {
        FUN_80022018(model, i);
      }

      //LAB_80020c08
    }

    if(model.ub_9c == 2) {
      return;
    }

    if(model.s_9e == 0) {
      model.ub_9c = 0;
    }

    //LAB_80020c3c
    if(model.ub_9c == 0) {
      if(model.ub_a2 == 0) {
        model.s_9e = model.s_9a;
      } else {
        //LAB_80020c68
        model.s_9e = model.s_9a / 2;
      }

      //LAB_80020c7c
      model.ub_9c++;
      model.partTransforms_94 = model.partTransforms_90;
    }

    //LAB_80020c90
    if((model.s_9e & 0x1) == 0 && model.ub_a2 == 0) {
      final UnboundedArrayRef<ModelPartTransforms> transforms = model.partTransforms_94;

      if(model.ub_a3 == 0) {
        //LAB_80020ce0
        for(int i = 0; i < model.tmdNobj_ca; i++) {
          final GsCOORDINATE2 coord2 = model.dobj2ArrPtr_00[i].coord2_04;
          final GsCOORD2PARAM params = coord2.param;
          RotMatrix_80040010(params.rotate, coord2.coord);
          params.trans.set(
            (params.trans.getX() + transforms.get(i).translate_06.getX()) / 2,
            (params.trans.getY() + transforms.get(i).translate_06.getY()) / 2,
            (params.trans.getZ() + transforms.get(i).translate_06.getZ()) / 2
          );
          TransMatrix(coord2.coord, params.trans);
        }

        //LAB_80020d6c
      } else {
        //LAB_80020d74
        //LAB_80020d8c
        for(int i = 0; i < model.tmdNobj_ca; i++) {
          final GsCOORDINATE2 coord2 = model.dobj2ArrPtr_00[i].coord2_04;
          final GsCOORD2PARAM params = coord2.param;

          params.rotate.set(transforms.get(i).rotate_00);
          RotMatrix_80040010(params.rotate, coord2.coord);

          params.trans.set(transforms.get(i).translate_06);
          TransMatrix(coord2.coord, params.trans);
        }

        //LAB_80020dfc
      }

      //LAB_80020e00
    } else {
      //LAB_80020e0c
      final UnboundedArrayRef<ModelPartTransforms> transforms = model.partTransforms_94;

      //LAB_80020e24
      for(int i = 0; i < model.tmdNobj_ca; i++) {
        final GsCOORDINATE2 coord2 = model.dobj2ArrPtr_00[i].coord2_04;
        final GsCOORD2PARAM params = coord2.param;

        params.rotate.set(transforms.get(i).rotate_00);
        RotMatrix_80040010(params.rotate, coord2.coord);

        params.trans.set(transforms.get(i).translate_06);
        TransMatrix(coord2.coord, params.trans);
      }

      //LAB_80020e94
      model.partTransforms_94 = transforms.slice(model.tmdNobj_ca);
    }

    //LAB_80020e98
    model.s_9e--;

    //LAB_80020ea8
  }

  @Method(0x80020ed8L)
  public static void FUN_80020ed8() {
    if(_800bdb88.get() == 5) {
      if(loadingGameStateOverlay_8004dd08.get() == 0) {
        if(_800bd7b4.get() == 0x1L) {
          FUN_800e4708();
        }

        //LAB_80020f20
        FUN_8002aae8();
        FUN_800e4018();
      }
    }

    //LAB_80020f30
    //LAB_80020f34
    final int a0 = _800bdb88.get();
    _800bd7b4.setu(0);
    if(a0 != mainCallbackIndex_8004dd20.get()) {
      _800bd80c.setu(a0);
      _800bdb88.set(mainCallbackIndex_8004dd20.get());

      if(mainCallbackIndex_8004dd20.get() == 5) {
        _800bd7b0.set(2);
        _800bd7b8.setu(0);

        if(a0 == 0x2L) {
          _800bd7b0.set(9);
        }

        //LAB_80020f84
        if(a0 == 0x6L) {
          _800bd7b0.set(-4);
          _800bd7b8.setu(0x1L);
        }

        //LAB_80020fa4
        if(a0 == 0x8L) {
          _800bd7b0.set(3);
        }
      }
    }

    //LAB_80020fb4
    //LAB_80020fb8
    if(_800bdb88.get() == 2) {
      _800bd7ac.setu(0x1L);
    }

    //LAB_80020fd0
  }

  @Method(0x80020fe0L)
  public static void deallocateModel(final Model124 model) {
    //LAB_80021008
    if(mainCallbackIndex_8004dd20.get() == 5 && model.smallerStructPtr_a4 != null) {
      free(model.smallerStructPtr_a4.getAddress());
    }

    //LAB_80021034
  }

  @Method(0x80021048L)
  public static void FUN_80021048(final long primitives, final long count) {
    // empty
  }

  @Method(0x80021050L)
  public static void FUN_80021050(final long primitives, final long count) {
    // empty
  }

  @Method(0x80021058L)
  public static void FUN_80021058(final long primitives, final long count) {
    // empty
  }

  @Method(0x80021060L)
  public static void FUN_80021060(final long primitives, final long count) {
    // empty
  }

  @Method(0x80021068L)
  public static void FUN_80021068(long primitives, final long count, final long a2) {
    final long a3 = _8005027c.offset(a2 * 0x10L).getAddress();

    //LAB_80021080
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, primitives).offset(0x4L).and(MEMORY.ref(4, a3).offset(0xcL).get()).oru(MEMORY.ref(4, a3).offset(0x8L).get());
      MEMORY.ref(4, primitives).offset(0x8L).and(MEMORY.ref(4, a3).offset(0x4L).get()).oru(MEMORY.ref(4, a3).offset(0x0L).get());
      primitives += 0x1cL;
    }

    //LAB_800210bc
  }

  @Method(0x800210c4L)
  public static void FUN_800210c4(long primitives, final long count, final long a2) {
    final long a3 = _8005027c.offset(a2 * 0x10L).getAddress();

    //LAB_800210dc
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, primitives).offset(0x4L).and(MEMORY.ref(4, a3).offset(0xcL).get()).oru(MEMORY.ref(4, a3).offset(0x8L).get());
      MEMORY.ref(4, primitives).offset(0x8L).and(MEMORY.ref(4, a3).offset(0x4L).get()).oru(MEMORY.ref(4, a3).offset(0x0L).get());
      primitives += 0x24L;
    }

    //LAB_80021118
  }

  @Method(0x8002117cL)
  public static void FUN_8002117c(long primitives, final long count, final long a2) {
    final long a3 = _8005027c.offset(a2 * 0x10L).getAddress();

    //LAB_80021194
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, primitives).offset(0x4L).and(MEMORY.ref(4, a3).offset(0xcL).get()).oru(MEMORY.ref(4, a3).offset(0x8L).get());
      MEMORY.ref(4, primitives).offset(0x8L).and(MEMORY.ref(4, a3).offset(0x4L).get()).oru(MEMORY.ref(4, a3).offset(0x0L).get());
      primitives += 0x2cL;
    }

    //LAB_800211d0
  }

  @Method(0x80021120L)
  public static void FUN_80021120(long primitives, final long count, final long a2) {
    final long a3 = _8005027c.offset(a2 * 0x10L).getAddress();

    //LAB_80021138
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, primitives).offset(0x4L).and(MEMORY.ref(4, a3).offset(0xcL).get()).oru(MEMORY.ref(4, a3).offset(0x8L).get());
      MEMORY.ref(4, primitives).offset(0x8L).and(MEMORY.ref(4, a3).offset(0x4L).get()).oru(MEMORY.ref(4, a3).offset(0x0L).get());
      primitives += 0x24L;
    }

    //LAB_80021174
  }

  @Method(0x800211d8L)
  public static void renderModel(final Model124 model) {
    if(mainCallbackIndex_8004dd20.get() == 5) {
      //LAB_80021230
      renderSmapModel(model);
    } else if(mainCallbackIndex_8004dd20.get() == 6) {
      //LAB_80021220
      renderBttlModel(model);
    } else if(mainCallbackIndex_8004dd20.get() == 8) {
      //LAB_8002120c
      //LAB_80021240
      renderWmapModel(model);
    }

    //LAB_80021248
  }

  @Method(0x80021258L)
  public static void renderDobj2(final GsDOBJ2 dobj2) {
    if(mainCallbackIndex_8004dd20.get() == 5) {
      //LAB_800212b0
      Renderer.renderDobj2(dobj2, false);
      return;
    }

    if(mainCallbackIndex_8004dd20.get() == 6) {
      //LAB_800212a0
      Renderer.renderDobj2(dobj2, true);
      return;
    }

    //LAB_8002128c
    if(mainCallbackIndex_8004dd20.get() == 8) {
      //LAB_800212c0
      Renderer.renderDobj2(dobj2, false);
    }

    //LAB_800212c8
  }

  @Method(0x800212d8L)
  public static void applyModelPartTransforms(final Model124 a0) {
    final int count = a0.tmdNobj_ca;

    if(count == 0) {
      return;
    }

    final UnboundedArrayRef<ModelPartTransforms> transforms = a0.partTransforms_94;

    //LAB_80021320
    for(int i = 0; i < count; i++) {
      final GsDOBJ2 obj2 = a0.dobj2ArrPtr_00[i];

      final GsCOORDINATE2 coord2 = obj2.coord2_04;
      final GsCOORD2PARAM params = coord2.param;
      final MATRIX matrix = coord2.coord;

      params.rotate.set(transforms.get(i).rotate_00);
      RotMatrix_80040010(params.rotate, matrix);

      params.trans.set(transforms.get(i).translate_06);
      TransMatrix(matrix, params.trans);
    }

    //LAB_80021390
    a0.partTransforms_94 = transforms.slice(count);
  }

  @Method(0x800213c4L)
  public static void FUN_800213c4(final Model124 a0) {
    //LAB_80021404
    for(int i = 0; i < a0.tmdNobj_ca; i++) {
      final ModelPartTransforms transforms = a0.partTransforms_94.get(i);
      final GsCOORDINATE2 coord2 = a0.dobj2ArrPtr_00[i].coord2_04;
      final MATRIX coord = coord2.coord;
      final GsCOORD2PARAM params = coord2.param;
      RotMatrix_80040010(params.rotate, coord);
      params.trans.setX((params.trans.getX() + transforms.translate_06.getX()) / 2);
      params.trans.setY((params.trans.getY() + transforms.translate_06.getY()) / 2);
      params.trans.setZ((params.trans.getZ() + transforms.translate_06.getZ()) / 2);
      TransMatrix(coord, params.trans);
    }

    //LAB_80021490
    a0.partTransforms_94 = a0.partTransforms_94.slice(a0.tmdNobj_ca);
  }

  @Method(0x800214bcL)
  public static void applyModelRotationAndScale(final Model124 model) {
    RotMatrix_8003faf0(model.coord2Param_64.rotate, model.coord2_14.coord);
    ScaleMatrix(model.coord2_14.coord, model.scaleVector_fc);
    model.coord2_14.flg = 0;
  }

  @Method(0x80021520L)
  public static void FUN_80021520(final Model124 model, final ExtendedTmd a1, final TmdAnimationFile a2, final long a3) {
    FUN_80020718(model, a1, a2);
    FUN_8002155c(model, a3);
  }

  @Method(0x8002155cL)
  public static void FUN_8002155c(final Model124 model, final long a1) {
    final int v0 = (int)_8005039c.offset(2, a1 * 0x2L).getSigned();
    model.vector_10c.set(v0, v0, v0);
  }

  @Method(0x80021584L)
  public static void loadModelStandardAnimation(final Model124 model, final TmdAnimationFile tmdAnimFile) {
    model.animType_90 = -1;
    model.partTransforms_90 = tmdAnimFile.partTransforms_10;
    model.partTransforms_94 = tmdAnimFile.partTransforms_10;
    model.animCount_98 = tmdAnimFile.count_0c.get();
    model.s_9a = tmdAnimFile._0e.get();
    model.ub_9c = 0;

    applyModelPartTransforms(model);

    if(model.ub_a2 == 0) {
      model.s_9e = model.s_9a;
    } else {
      //LAB_800215e8
      model.s_9e = model.s_9a / 2;
    }

    //LAB_80021608
    model.ub_9c = 1;
    model.partTransforms_94 = model.partTransforms_90;
  }

  @Method(0x80021628L)
  public static void FUN_80021628(final Model124 model) {
    if(mainCallbackIndex_8004dd20.get() == 5) { // SMAP
      //LAB_800216b4
      for(int i = 0; i < model.ObjTable_0c.nobj; i++) {
        FUN_800d9e64(model.ObjTable_0c.top[i], model.colourMap_9d);
      }
      //LAB_800216d4
    } else if(mainCallbackIndex_8004dd20.get() == 8) { // WMAP
      //LAB_800216ec
      for(int i = 0; i < model.ObjTable_0c.nobj; i++) {
        FUN_800c8844(model.ObjTable_0c.top[i], model.colourMap_9d);
      }
    } else {
      //LAB_80021678
      for(int i = 0; i < model.ObjTable_0c.nobj; i++) {
        FUN_80020468(model.ObjTable_0c.top[i], model.colourMap_9d);
      }
      //LAB_8002169c
    }

    //LAB_80021708
  }

  @Method(0x80021724L)
  public static void FUN_80021724(final Model124 model) {
    final int v1 = mainCallbackIndex_8004dd20.get();
    if(v1 == 5) {
      //LAB_8002177c
      FUN_800da524(model);
      //LAB_80021758
    } else if(v1 == 6) {
      //LAB_8002176c
      FUN_800ec258(model);
    } else if(v1 == 8) {
      //LAB_8002178c
      FUN_800c8d90(model);
    }

    //LAB_80021794
  }

  @Method(0x800217a4L)
  public static void FUN_800217a4(final Model124 model) {
    if(model.coord2Param_64.rotate.pad.get() == -1) {
      final MATRIX mat = new MATRIX();
      RotMatrix_8003fd80(model.coord2Param_64.rotate, mat);
      TransposeMatrix(mat, model.coord2_14.coord);
      model.coord2Param_64.rotate.x.set((short)0);
      model.coord2Param_64.rotate.y.set((short)0);
      model.coord2Param_64.rotate.z.set((short)0);
      model.coord2Param_64.rotate.pad.set((short)0);
    } else {
      model.coord2Param_64.rotate.y.set(FUN_800ea4c8(model.coord2Param_64.rotate.y.get()));
      RotMatrix_8003faf0(model.coord2Param_64.rotate, model.coord2_14.coord);
    }

    ScaleMatrix(model.coord2_14.coord, model.scaleVector_fc);
    model.coord2_14.flg = 0;
  }

  @Method(0x800218f0L)
  public static void FUN_800218f0() {
    if(_800bd7ac.get() == 1) {
      _800bd7b0.set(9);
      _800bd7ac.setu(0);
    }
  }

  @Method(0x80021918L)
  public static void prepareObjTable2Step(final GsOBJTABLE2 table, final Tmd tmd, final GsCOORDINATE2 coord2, final int maxSize, final int a4) {
    final int s2 = a4 >> 8;
    final int dobj2Id = a4 & 0xff;

    GsDOBJ2 dobj2 = getDObj2ById(table, dobj2Id);

    final MATRIX coord;
    final VECTOR scale;
    final SVECTOR rotation;
    final VECTOR translation;

    if(dobj2 == null) {
      dobj2 = new GsDOBJ2(); //sp0x10;
      coord = new MATRIX(); //sp0x20;
      scale = new VECTOR();
      rotation = new SVECTOR(); //sp0x40;
      translation = new VECTOR();
    } else {
      //LAB_80021984
      dobj2.coord2_04.flg = 0;

      scale = dobj2.coord2_04.param.scale;
      rotation = dobj2.coord2_04.param.rotate;
      translation = dobj2.coord2_04.param.trans;
      coord = dobj2.coord2_04.coord;

      if(dobj2.coord2_04.super_ == null) {
        dobj2.coord2_04.super_ = coord2;
      }
    }

    //LAB_800219ac
    //LAB_80021a98
    if(s2 == 2 && tmd != null) {
      updateTmdPacketIlen(getTmdObjTableOffset(tmd, dobj2Id), dobj2, 0);
    }

    //LAB_800219d8
    //LAB_80021ac0
    if(s2 == 8 && table != null) {
      addNewDobj2(table, dobj2Id, maxSize);
    }

    if(s2 == 1) {
      //LAB_800219ec
      rotation.set((short)11, (short)11, (short)11);

      dobj2.coord2_04.coord.set(0, (short)0x1000);
      dobj2.coord2_04.coord.set(1, (short)0);
      dobj2.coord2_04.coord.set(2, (short)0);
      dobj2.coord2_04.coord.set(3, (short)0);
      dobj2.coord2_04.coord.set(4, (short)0x1000);
      dobj2.coord2_04.coord.set(5, (short)0);
      dobj2.coord2_04.coord.set(6, (short)0);
      dobj2.coord2_04.coord.set(7, (short)0);
      dobj2.coord2_04.coord.set(8, (short)0x1000);

      RotMatrixX(rotation.x.get(), coord);
      RotMatrixY(rotation.y.get(), coord);
      RotMatrixZ(rotation.z.get(), coord);

      scale.set(0x1000, 0x1000, 0x1000);

      RotMatrix_8003faf0(rotation, coord);
      ScaleMatrixL(coord, scale);

      translation.set(1, 1, 1);

      TransMatrix(coord, translation);
    }

    //LAB_80021ad8
  }

  @Method(0x80021b08L)
  public static void initObjTable2(final GsOBJTABLE2 table, final GsDOBJ2[] dobj2s, final GsCOORDINATE2[] coord2s, final GsCOORD2PARAM[] params, final int size) {
    table.top = dobj2s;
    table.nobj = 0;

    //LAB_80021b2c
    for(int i = 0; i < size; i++) {
      dobj2s[i].attribute_00 = 0x8000_0000;
      dobj2s[i].coord2_04 = coord2s[i];
      dobj2s[i].tmd_08 = null;
      dobj2s[i].id_0c = -1;

      coord2s[i].param = params[i];
    }

    //LAB_80021b5c
  }

  @Method(0x80021b64L)
  public static GsDOBJ2 getDObj2ById(final GsOBJTABLE2 table, final int id) {
    //LAB_80021b80
    for(int i = 0; i < table.nobj; i++) {
      final GsDOBJ2 obj2 = table.top[i];

      if(obj2.id_0c == id) {
        return obj2;
      }

      //LAB_80021b98
    }

    //LAB_80021ba4
    return null;
  }

  @Method(0x80021bacL)
  public static void addNewDobj2(final GsOBJTABLE2 table, final int id, final int maxSize) {
    final int size = table.nobj;

    //LAB_80021bd4
    GsDOBJ2 dobj2 = table.top[0];
    int i;
    for(i = 0; i < size; i++) {
      if(dobj2.id_0c == -1) {
        break;
      }

      dobj2 = table.top[i];
    }

    //LAB_80021c08
    if(i >= maxSize) {
      return;
    }

    //LAB_80021bf4
    if(i >= size) {
      dobj2 = table.top[table.nobj];
      table.nobj++;
    }

    //LAB_80021c2c
    dobj2.id_0c = id;
    dobj2.attribute_00 = 0;
    GsInitCoordinate2(null, dobj2.coord2_04);
    dobj2.tmd_08 = null;

    //LAB_80021c48
  }

  @Method(0x80021c58L)
  public static UnboundedArrayRef<TmdObjTable> getTmdObjTableOffset(final Tmd tmd, final long objId) {
    long count = tmd.header.nobj.get();
    int id = 1;
    //LAB_80021c74
    while(count > 0) {
      if(id == objId) {
        //LAB_80021c8c
        return tmd.objTable.slice(id - 1);
      }

      count--;
      id++;
    }

    //LAB_80021c8c
    //LAB_80021c98
    return null;
  }

  @Method(0x80021ca0L)
  public static void prepareObjTable2(final GsOBJTABLE2 table, final Tmd tmd, final GsCOORDINATE2 coord2, final int maxSize, final int nobj) {
    //LAB_80021d08
    for(int i = 1; i < nobj; i++) {
      // Add new dobj2
      prepareObjTable2Step(table, tmd, coord2, maxSize, 0x800 | i);
    }

    //LAB_80021d3c
    //LAB_80021d64
    for(int i = 1; i < nobj; i++) {
      // Merge TMD packets
      prepareObjTable2Step(table, tmd, coord2, maxSize, 0x200 | i);
      // Set initial transforms
      prepareObjTable2Step(table, tmd, coord2, maxSize, 0x100 | i);
    }

    //LAB_80021db4
  }

  @Method(0x80021de4L)
  public static void FUN_80021de4(final MATRIX a0, final MATRIX a1, final MATRIX a2) {
    CPU.CTC2(a0.getPacked(0), 0);
    CPU.CTC2(a0.getPacked(2), 1);
    CPU.CTC2(a0.getPacked(4), 2);
    CPU.CTC2(a0.getPacked(6), 3);
    CPU.CTC2(a0.getPacked(8), 4);
    CPU.MTC2(a1.get(0),  9);
    CPU.MTC2(a1.get(3), 10);
    CPU.MTC2(a1.get(6), 11);
    CPU.COP2(0x49e012L);
    a2.set(0, (short)CPU.MFC2( 9));
    a2.set(3, (short)CPU.MFC2(10));
    a2.set(6, (short)CPU.MFC2(11));
    CPU.MTC2(a1.get(1),  9);
    CPU.MTC2(a1.get(4), 10);
    CPU.MTC2(a1.get(7), 11);
    CPU.COP2(0x49e012L);
    a2.set(1, (short)CPU.MFC2( 9));
    a2.set(4, (short)CPU.MFC2(10));
    a2.set(7, (short)CPU.MFC2(11));
    CPU.MTC2(a1.get(2),  9);
    CPU.MTC2(a1.get(5), 10);
    CPU.MTC2(a1.get(8), 11);
    CPU.COP2(0x49e012L);
    a2.set(2, (short)CPU.MFC2( 9));
    a2.set(5, (short)CPU.MFC2(10));
    a2.set(8, (short)CPU.MFC2(11));
  }

  @Method(0x80021edcL)
  public static void SetRotMatrix(final MATRIX m) {
    CPU.CTC2(m.getPacked(0), 0); //
    CPU.CTC2(m.getPacked(2), 1); //
    CPU.CTC2(m.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(m.getPacked(6), 3); //
    CPU.CTC2(m.getPacked(8), 4); //
  }

  @Method(0x80021f0cL)
  public static void SetLightMatrix(final MATRIX m) {
    CPU.CTC2(m.getPacked(0),  8); //
    CPU.CTC2(m.getPacked(2),  9); //
    CPU.CTC2(m.getPacked(4), 10); // Light source matrix
    CPU.CTC2(m.getPacked(6), 11); //
    CPU.CTC2(m.getPacked(8), 12); //
  }

  @Method(0x80021f3cL)
  public static void SetColorMatrix(final MATRIX m) {
    CPU.CTC2(m.getPacked(0), 16); //
    CPU.CTC2(m.getPacked(2), 17); //
    CPU.CTC2(m.getPacked(4), 18); // Light color matrix
    CPU.CTC2(m.getPacked(6), 19); //
    CPU.CTC2(m.getPacked(8), 20); //
  }

  @Method(0x80021f6cL)
  public static void SetTransMatrix(final MATRIX m) {
    CPU.CTC2(m.transfer.getX(), 5); // Translation X
    CPU.CTC2(m.transfer.getY(), 6); // Translation Y
    CPU.CTC2(m.transfer.getZ(), 7); // Translation Z
  }

  @Method(0x80021f8cL)
  public static void SetBackColor(final long r, final long g, final long b) {
    CPU.CTC2(r * 0x10L, 13); // Background colour R
    CPU.CTC2(g * 0x10L, 14); // Background colour G
    CPU.CTC2(b * 0x10L, 15); // Background colour B
  }

  @Method(0x80021facL)
  public static void SetGeomOffset(final int x, final int y) {
    // cop2r56, OFX - Screen offset X
    CPU.CTC2(x << 16, 24);
    // cop2r57, OFY - Screen offset Y
    CPU.CTC2(y << 16, 25);
  }

  @Method(0x80021fc4L)
  public static int SquareRoot0(final long n) {
    return (int)Math.sqrt(n);
  }

  /**
   * This method animates the fog in the first cutscene with Rose/Feyrbrand
   */
  @Method(0x80022018L)
  public static void FUN_80022018(final Model124 a0, final int index) {
    final RECT rect = new RECT();

    if(a0.ptrs_d0[index] == 0) {
      a0.aub_ec[index] = 0;
      return;
    }

    //LAB_80022068
    int v1 = a0.colourMap_9d;
    final long t2;
    long v0;
    if((v1 & 0x80) == 0) {
      t2 = _800503b0.offset(v1 * 0x2L).getSigned();
      v0 = _800503d4.getAddress();
    } else {
      //LAB_80022098
      if(v1 == 0x80) {
        return;
      }

      v1 &= 0x7f;
      t2 = _800503f8.offset(v1 * 0x2L).getSigned();
      v0 = _80050424.getAddress();
    }

    //LAB_800220c0
    final long t1 = MEMORY.ref(2, v0).offset(v1 * 0x2L).getSigned();
    long s1;
    if(a0.usArr_ba[index] != 0x5678) {
      a0.usArr_ba[index]--;
      if(a0.usArr_ba[index] != 0) {
        return;
      }

      s1 = a0.ptrs_d0[index];
      a0.usArr_ba[index] = (int)MEMORY.ref(2, s1).get() & 0x7fff;
      s1 += 0x2L;
      final long a2 = MEMORY.ref(2, s1).getSigned() + t2;
      s1 += 0x2L;
      final long a0_1 = MEMORY.ref(2, s1).getSigned() + t1;
      s1 += 0x2L;
      rect.w.set((short)(MEMORY.ref(2, s1).getSigned() / 4));
      s1 += 0x2L;
      rect.h.set((short)MEMORY.ref(2, s1).getSigned());
      s1 += 0x2L;

      //LAB_80022154
      for(int i = 0; i < a0.usArr_ac[index]; i++) {
        s1 += 0x4L;
      }

      //LAB_80022164
      rect.x.set((short)(MEMORY.ref(2, s1).getSigned() + t2));
      s1 += 0x2L;
      rect.y.set((short)(MEMORY.ref(2, s1).getSigned() + t1));

      GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x.get(), rect.y.get(), (int)a2 & 0xffff, (int)a0_1 & 0xffff, rect.w.get(), rect.h.get()));

      s1 += 0x2L;
      a0.usArr_ac[index]++;

      v1 = (int)MEMORY.ref(2, s1).get();
      if(v1 == 0xfffe) {
        a0.aub_ec[index] = 0;
        a0.usArr_ac[index] = 0;
      }

      //LAB_800221f8
      if(v1 == 0xffff) {
        a0.usArr_ac[index] = 0;
      }

      return;
    }

    //LAB_80022208
    s1 = a0.ptrs_d0[index];
    final int a1_0 = a0.usArr_ac[index];
    s1 += 0x2L;
    final long a0_0 = MEMORY.ref(2, s1).getSigned();
    s1 += 0x2L;
    v0 = MEMORY.ref(2, s1).getSigned();
    s1 += 0x2L;
    v1 = (int)MEMORY.ref(2, s1).getSigned();
    s1 += 0x2L;
    long s3 = MEMORY.ref(2, s1).getSigned();
    s1 += 0x2L;
    int s0_0 = (int)MEMORY.ref(2, s1).offset(0x2L).getSigned();
    final long s7 = v0 + t1;
    final long s5 = v1 >>> 2;
    v1 = (int)MEMORY.ref(2, s1).getSigned();
    final long s6 = a0_0 + t2;

    if((a1_0 & 0xf) != 0) {
      a0.usArr_ac[index] = a1_0 - 1;

      if(a0.usArr_ac[index] == 0) {
        a0.usArr_ac[index] = s0_0;
        s0_0 = 16;
      } else {
        //LAB_80022278
        s0_0 = 0;
      }
    }

    //LAB_8002227c
    if(s0_0 == 0) {
      return;
    }

    rect.set((short)960, (short)256, (short)s5, (short)s3);
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x.get(), rect.y.get(), (int)s6 & 0xffff, (int)s7 & 0xffff, rect.w.get(), rect.h.get()));

    s0_0 = s0_0 >> 4;
    s3 -= s0_0;

    final int a3;
    if(v1 == 0) {
      rect.set((short)s6, (short)(s7 + s3), (short)s5, (short)s0_0);
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x.get(), rect.y.get(), 960, 256, rect.w.get(), rect.h.get()));

      a3 = s0_0 + 256 & 0xffff;
      rect.set((short)s6, (short)s7, (short)s5, (short)s3);
    } else {
      //LAB_80022358
      rect.set((short)s6, (short)s7, (short)s5, (short)s0_0);
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x.get(), rect.y.get(), 960, (int)s3 + 256 & 0xffff, rect.w.get(), rect.h.get()));

      a3 = 256;
      rect.set((short)s6, (short)(s0_0 + s7), (short)s5, (short)s3);
    }

    //LAB_8002241c
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x.get(), rect.y.get(), 960, a3, rect.w.get(), rect.h.get()));

    //LAB_80022440
  }

  @Method(0x8002246cL)
  public static void FUN_8002246c(final Model124 a0, final int a1) {
    if(a0.ptrs_d0[a1] == 0) {
      a0.aub_ec[a1] = 0;
      return;
    }

    //LAB_80022490
    a0.usArr_ac[a1] = 0;
    a0.usArr_ba[a1] = (int)MEMORY.ref(2, a0.ptrs_d0[a1]).get() & 0x3fff;

    if((MEMORY.ref(2, a0.ptrs_d0[a1]).get() & 0x8000) != 0) {
      a0.aub_ec[a1] = 1;
    } else {
      //LAB_800224d0
      a0.aub_ec[a1] = 0;
    }

    //LAB_800224d8
    if((MEMORY.ref(2, a0.ptrs_d0[a1]).get() & 0x4000) != 0) {
      a0.usArr_ba[a1] = 0x5678;
      a0.usArr_ac[a1] = (int)MEMORY.ref(2, a0.ptrs_d0[a1]).offset(0xcL).get();
      a0.aub_ec[a1] = 1;
    }

    //LAB_80022510
  }

  private static WhichMenu destMenu;
  private static MenuScreen destScreen;

  private static void initMenu(final WhichMenu destMenu, final MenuScreen destScreen) {
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
      case INIT_SHOP_MENU_6 -> initMenu(WhichMenu.RENDER_SHOP_MENU_9, new ShopScreen());
      case INIT_LOAD_GAME_MENU_11 -> initMenu(WhichMenu.RENDER_LOAD_GAME_MENU_14, new LoadGameScreen());
      case INIT_SAVE_GAME_MENU_16 -> initMenu(WhichMenu.RENDER_SAVE_GAME_MENU_19, new SaveGameScreen(() -> whichMenu_800bdc38 = WhichMenu.UNLOAD_SAVE_GAME_MENU_20));
      case INIT_CHAR_SWAP_MENU_21 -> {
        loadCharacterStats(0);
        FUN_80103b10();
        initMenu(WhichMenu.RENDER_CHAR_SWAP_MENU_24, new CharSwapScreen(() -> whichMenu_800bdc38 = WhichMenu.UNLOAD_CHAR_SWAP_MENU_25));
      }
      case INIT_TOO_MANY_ITEMS_MENU_31 -> initMenu(WhichMenu.RENDER_TOO_MANY_ITEMS_MENU_34, new TooManyItemsScreen());

      case WAIT_FOR_MUSIC_TO_LOAD_AND_LOAD_S_ITEM_2 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38 = WhichMenu.WAIT_FOR_S_ITEM_TO_LOAD_3;

          renderablePtr_800bdc5c = null;
          drgn0_6666FilePtr_800bdc3c.clear();
          setWidthAndFlags(384);
          loadDrgnBinFile(0, 6665, 0, SItem::menuAssetsLoaded, 0, 0x5L);
          loadDrgnBinFile(0, 6666, 0, SItem::menuAssetsLoaded, 1, 0x3L);
          textZ_800bdf00.set(33);

          loadSupportOverlay(2, () -> {
            whichMenu_800bdc38 = destMenu;

            if(destScreen != null) {
              menuStack.pushScreen(destScreen);
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

      case RENDER_SHOP_MENU_9, RENDER_LOAD_GAME_MENU_14, RENDER_SAVE_GAME_MENU_19, RENDER_CHAR_SWAP_MENU_24, RENDER_TOO_MANY_ITEMS_MENU_34 -> menuStack.render();
      case RENDER_INVENTORY_MENU_4, RENDER_SHOP_CARRIED_ITEMS_36 -> renderMenus();
      case RENDER_POST_COMBAT_REPORT_29 -> renderPostCombatReport();

      case UNLOAD_LOAD_GAME_MENU_15, UNLOAD_SAVE_GAME_MENU_20, UNLOAD_CHAR_SWAP_MENU_25 -> {
        menuStack.popScreen();
        decrementOverlayCount();

        if(whichMenu_800bdc38 != WhichMenu.UNLOAD_SAVE_GAME_MENU_20) {
          FUN_8001e010(-1);
        }

        SCRIPTS.start();
        whichMenu_800bdc38 = WhichMenu.NONE_0;

        deallocateRenderables(0xff);
        free(drgn0_6666FilePtr_800bdc3c.getPointer());

        scriptStartEffect(2, 10);

        if(mainCallbackIndex_8004dd20.get() == 5 && loadingGameStateOverlay_8004dd08.get() == 0) {
          FUN_800e3fac();
        }

        textZ_800bdf00.set(13);
      }

      case UNLOAD_INVENTORY_MENU_5, UNLOAD_SHOP_MENU_10, UNLOAD_TOO_MANY_ITEMS_MENU_35 -> {
        decrementOverlayCount();
        FUN_8001e010(-1);
        SCRIPTS.start();
        whichMenu_800bdc38 = WhichMenu.NONE_0;
      }

      case UNLOAD_POST_COMBAT_REPORT_30 -> {
        decrementOverlayCount();
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

    return (equipmentStats_80111ff0.get(itemId)._00.get() & 0x4) != 0;
  }

  @Method(0x800228d0L)
  public static int getItemIcon(final int itemId) {
    if(itemId >= 0xc0) {
      return itemStats_8004f2ac.get(itemId - 0xc0).icon_07.get();
    }

    //LAB_80022908
    return equipmentStats_80111ff0.get(itemId).icon_0e.get();
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
    if(charIndex == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0) {
      spellIndicesOut[0] = 9;
      spellIndicesOut[1] = 4;
      return 2;
    }

    //LAB_80022994
    //LAB_80022998
    //LAB_800229d0
    int spellCount = 0;
    for(int dlevel = 0; dlevel < stats_800be5f8.get(charIndex).dlevel_0f.get() + 1; dlevel++) {
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
    if(charIndex == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0) {
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
  public static void FUN_80022a94(final Value address) {
    final TimHeader header = parseTimHeader(address.offset(0x4L));

    if(header.imageRect.w.get() != 0 || header.imageRect.h.get() != 0) {
      LoadImage(header.imageRect, header.imageAddress.get());
    }

    //LAB_80022acc
    if((header.flags.get() & 0x8L) != 0) {
      LoadImage(header.clutRect, header.clutAddress.get());
    }

    //LAB_80022aec
  }

  @Method(0x80022afcL)
  public static int itemCanBeUsedInMenu(final int itemId) {
    if(itemId < 0xc0 || itemId == 0xff) {
      return 0;
    }

    final int target = itemStats_8004f2ac.get(itemId - 0xc0).target_00.get();

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
    final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);
    final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);

    if(charData.hp_08.get() == stats.maxHp_66.get()) {
      return -2;
    }

    //LAB_80022bb4
    final int ret;
    if(amount == -1) {
      charData.hp_08.set(stats.maxHp_66.get());
      ret = -1;
    } else {
      //LAB_80022bc8
      charData.hp_08.add(amount);

      if(charData.hp_08.get() < stats.maxHp_66.get()) {
        ret = amount;
      } else {
        charData.hp_08.set(stats.maxHp_66.get());
        ret = -1;
      }
    }

    //LAB_80022bec
    loadCharacterStats(0);

    //LAB_80022bf8
    return ret;
  }

  /**
   * @param amount Amount of MP to restore, -1 restores all MP
   * @return The amount of MP restored, -1 if all MP is restored, or -2 if MP was already full
   */
  @Method(0x80022c08L)
  public static int addMp(final int charIndex, final int amount) {
    final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);
    final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);

    if(stats.maxMp_6e.get() == 0 || charData.mp_0a.get() == stats.maxMp_6e.get()) {
      return -2;
    }

    //LAB_80022c78
    final int ret;
    if(amount == -1) {
      charData.mp_0a.set(stats.maxMp_6e.get());
      ret = -1;
    } else {
      //LAB_80022c8c
      charData.mp_0a.add(amount);

      if(charData.mp_0a.get() < stats.maxMp_6e.get()) {
        ret = amount;
      } else {
        charData.mp_0a.set(stats.maxMp_6e.get());
        ret = -1;
      }
    }

    //LAB_80022cb4
    loadCharacterStats(0);

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
      if(mainCallbackIndex_8004dd20.get() == 8 || hasNoEncounters_800bed58.get() == 0) {
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
    final ItemStats0c itemStats = itemStats_8004f2ac.get(itemId - 0xc0);
    final int percentage = itemStats.percentage_09.get();
    if((itemStats.type_0b.get() & 0x80) != 0) {
      //LAB_80022edc
      response._00 = (itemStats.target_00.get() & 0x2) == 0 ? 2 : 3;

      final int amount;
      if(percentage == 100) {
        amount = -1;
      } else {
        //LAB_80022ef0
        amount = stats_800be5f8.get(charIndex).maxHp_66.get() * percentage / 100;
      }

      //LAB_80022f3c
      response.value_04 = addHp(charIndex, amount);
    }

    //LAB_80022f50
    if((itemStats.type_0b.get() & 0x40) != 0) {
      //LAB_80022f98
      response._00 = (itemStats.target_00.get() & 0x2) == 0 ? 4 : 5;

      final int amount;
      if(percentage == 100) {
        amount = -1;
      } else {
        //LAB_80022fac
        amount = stats_800be5f8.get(charIndex).maxMp_6e.get() * percentage / 100;
      }

      //LAB_80022ff8
      response.value_04 = addMp(charIndex, amount);
    }

    //LAB_8002300c
    if((itemStats.type_0b.get() & 0x20) != 0) {
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
    if((itemStats.type_0b.get() & 0x8) != 0) {
      final int status = gameState_800babc8.charData_32c.get(charIndex).status_10.get();

      if((itemStats.status_08.get() & status) != 0) {
        response.value_04 = status;
        gameState_800babc8.charData_32c.get(charIndex).status_10.and(~status);
      }

      //LAB_800230ec
      response._00 = 7;
    }

    //LAB_800230f0
    //LAB_800230fc
    //LAB_8002312c
    return response;
  }

  /** Recalculates item/equipment counts and removes invalid entries */
  @Method(0x80023148L)
  public static void recalcInventory() {
    gameState_800babc8.equipmentCount_1e4.set((short)0);

    //LAB_80023164
    while(gameState_800babc8.equipment_1e8.get(gameState_800babc8.equipmentCount_1e4.get()).get() != 0xff) {
      if(gameState_800babc8.equipmentCount_1e4.get() >= 255) {
        break;
      }

      gameState_800babc8.equipmentCount_1e4.incr();
    }

    //LAB_80023198
    //LAB_800231c0
    for(int i = gameState_800babc8.equipmentCount_1e4.get(); i <= 256; i++) {
      gameState_800babc8.equipment_1e8.get(i).set(0xff);
    }

    //LAB_800231d8
    gameState_800babc8.itemCount_1e6.set((short)0);

    //LAB_800231f0
    while(gameState_800babc8.items_2e9.get(gameState_800babc8.itemCount_1e6.get()).get() != 0xff) {
      if(gameState_800babc8.itemCount_1e6.get() >= Config.inventorySize()) {
        break;
      }

      gameState_800babc8.itemCount_1e6.incr();
    }

    //LAB_80023224
    //LAB_80023248
    for(int i = gameState_800babc8.itemCount_1e6.get(); i <= Config.inventorySize(); i++) {
      gameState_800babc8.items_2e9.get(i).set(0xff);
    }

    //LAB_8002325c
  }

  @Method(0x80023264L)
  public static void checkForPsychBombX() {
    gameState_800babc8.scriptFlags2_bc.get(13).and(0xfffb_ffff);

    //LAB_800232a4
    for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
      if(gameState_800babc8.items_2e9.get(i).get() == 0xfa) { // Psych Bomb X
        gameState_800babc8.scriptFlags2_bc.get(13).or(0x4_0000);
      }

      //LAB_800232c4
    }

    //LAB_800232d4
  }

  /** Pretty sure this moves all the items in the inv up one when you use one */
  @Method(0x800232dcL)
  public static int takeItem(final int itemIndex) {
    recalcInventory();

    if(gameState_800babc8.itemCount_1e6.get() == 0) {
      return 0xff;
    }

    if(itemIndex < Config.inventorySize()) {
      if(gameState_800babc8.items_2e9.get(itemIndex).get() == 0xff) {
        return 0xff;
      }

      //LAB_80023334
      for(int i = itemIndex; i < Config.inventorySize() - 1; i++) {
        gameState_800babc8.items_2e9.get(i).set(gameState_800babc8.items_2e9.get(i + 1).get());
      }

      //LAB_80023358
      gameState_800babc8.items_2e9.get(Config.inventorySize() - 1).set(0xff);
      gameState_800babc8.itemCount_1e6.decr();
      return 0;
    }

    //LAB_8002338c
    if(itemIndex >= 192) {
      //LAB_800233a4
      for(int i = 0; i < Config.inventorySize(); i++) {
        if(gameState_800babc8.items_2e9.get(i).get() == itemIndex) {
          //LAB_8002337c
          return takeItem(i);
        }
      }
    }

    //LAB_800233c4
    //LAB_800233c8
    return 0xff;
  }

  @Method(0x800233d8L)
  public static int takeEquipment(final int equipmentIndex) {
    recalcInventory();

    if(gameState_800babc8.equipmentCount_1e4.get() == 0) {
      return 0xff;
    }

    //LAB_8002340c
    if(gameState_800babc8.equipment_1e8.get(equipmentIndex).get() == 0xff) {
      return 0xff;
    }

    //LAB_80023430
    for(int s0 = equipmentIndex; s0 < 0xff; s0++) {
      gameState_800babc8.equipment_1e8.get(s0).set(gameState_800babc8.equipment_1e8.get(s0 + 1).get());
    }

    //LAB_80023454
    gameState_800babc8.equipment_1e8.get(0x99).set(0xff);
    gameState_800babc8.equipmentCount_1e4.decr();

    //LAB_80023474
    return 0;
  }

  @Method(0x80023484L)
  public static int giveItem(final int itemId) {
    recalcInventory();

    if(itemId == 0xff) {
      return 0xff;
    }

    if(itemId >= 0x100) {
      return 0;
    }

    if(itemId < 0xc0) {
      final int count = gameState_800babc8.equipmentCount_1e4.get();

      if(count >= 255) {
        return 0xff;
      }

      gameState_800babc8.equipment_1e8.get(count).set(itemId);
      gameState_800babc8.equipmentCount_1e4.incr();
      return 0;
    }

    //LAB_800234f4
    final int count = gameState_800babc8.itemCount_1e6.get();

    if(count >= Config.inventorySize()) {
      //LAB_8002350c
      return 0xff;
    }

    //LAB_80023514
    gameState_800babc8.items_2e9.get(count).set(itemId);
    gameState_800babc8.itemCount_1e6.incr();

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
    gameState_800babc8.gold_94.add(amount);

    if(gameState_800babc8.gold_94.get() > 99999999) {
      gameState_800babc8.gold_94.set(99999999);
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
  public static long getJoypadInputByPriority() {
    final long repeat = joypadRepeat_8007a3a0.get();

    if((repeat & 0x4L) != 0) {
      return 0x4L;
    }

    //LAB_800238c4
    if((repeat & 0x8L) != 0) {
      return 0x8L;
    }

    //LAB_800238d4
    if((repeat & 0x1L) != 0) {
      return 0x1L;
    }

    //LAB_800238e4
    if((repeat & 0x2L) != 0) {
      return 0x2L;
    }

    //LAB_800238f4
    if((repeat & 0x1000L) != 0) {
      return 0x1000L;
    }

    //LAB_80023904
    if((repeat & 0x4000L) != 0) {
      return 0x4000L;
    }

    //LAB_80023914
    if((repeat & 0x8000L) != 0) {
      return 0x8000L;
    }

    //LAB_80023924
    if((repeat & 0x2000L) != 0) {
      return 0x2000L;
    }

    //LAB_80023934
    final long press = joypadPress_8007a398.get();

    if((press & 0x10L) != 0) {
      return 0x10L;
    }

    //LAB_80023950
    if((press & 0x40L) != 0) {
      return 0x40L;
    }

    //LAB_80023960
    if((press & 0x80L) != 0) {
      return 0x80L;
    }

    //LAB_80023970
    return press & 0x20L;
  }

  @Method(0x800239e0L)
  public static void setInventoryFromDisplay(final List<MenuItemStruct04> display, final ArrayRef<UnsignedByteRef> a1, final int count) {
    //LAB_800239ec
    int itemIndex = 0;
    for(int i = 0; i < count; i++) {
      if((display.get(i).flags_02 & 0x1000) == 0) {
        a1.get(itemIndex).set(display.get(i).itemId_00);
        itemIndex++;
      }

      //LAB_80023a0c
    }

    //LAB_80023a1c
    a1.get(itemIndex).set(0xff);
  }

  @Method(0x80023a2cL)
  public static void sortItems(final List<MenuItemStruct04> display, final ArrayRef<UnsignedByteRef> items, final int count) {
    display.sort(Comparator.comparingInt(item -> getItemIcon(item.itemId_00)));
    setInventoryFromDisplay(display, items, count);
  }

  @Method(0x80023a88L)
  public static void FUN_80023a88() {
    final List<MenuItemStruct04> items = new ArrayList<>();

    for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
      final MenuItemStruct04 item = new MenuItemStruct04();
      item.itemId_00 = gameState_800babc8.items_2e9.get(i).get();
      items.add(item);
    }

    sortItems(items, gameState_800babc8.items_2e9, gameState_800babc8.itemCount_1e6.get());
  }

  @Method(0x80023b54L)
  public static Renderable58 allocateRenderable(final Drgn0_6666Struct a0, @Nullable Renderable58 a1) {
    if(a1 == null) {
      a1 = new Renderable58();
    }

    //LAB_80023b7c
    a1.flags_00 = 0;
    a1.glyph_04 = 0;
    a1._08 = a0._0a.get();
    a1._0c = 0;
    a1.startGlyph_10 = 0;
    a1.endGlyph_14 = a0.entryCount_06.get() - 1;
    a1._18 = 0;
    a1._1c = 0;
    a1.drgn0_6666_20 = a0;
    a1.metricsIndices_24 = new int[a0.entryCount_06.get()];
    for(int i = 0; i < a0.entryCount_06.get(); i++) {
      a1.metricsIndices_24[i] = (int)MEMORY.get(a0.entries_08.get(a0.entryCount_06.get()).getAddress() + i * 4, 4);
    }

    a1._28 = 0;
    a1.tpage_2c = 0;
    a1._34 = 0x1000;
    a1._38 = 0x1000;
    a1.z_3c = 36;
    a1.x_40 = 0;
    a1.y_44 = 0;
    a1._48 = 0;
    a1.child_50 = null;

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

  @Method(0x80023c28L)
  public static void uploadRenderables() {
    Renderable58 renderable = renderablePtr_800bdc5c;

    _800bdc58.addu(0x1L);

    //LAB_80023c8c
    while(renderable != null) {
      boolean forceUnload = false;
      final UnboundedArrayRef<Drgn0_6666Entry> entries = renderable.drgn0_6666_20.entries_08;

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
          renderable._08 = entries.get(renderable.glyph_04)._02.get() - 1;
        }
      }

      //LAB_80023e28
      if((renderable.flags_00 & 0x40) == 0) {
        final int centreX = displayWidth_1f8003e0.get() / 2 + 8;

        final ArrayRef<RenderableMetrics14> metricses = renderable.drgn0_6666_20.getMetrics(renderable.metricsIndices_24[entries.get(renderable.glyph_04).metricsIndicesIndex_00.get()]);

        //LAB_80023e94
        for(int i = metricses.length() - 1; i >= 0; i--) {
          final RenderableMetrics14 metrics = metricses.get(i);

          final GpuCommandPoly cmd = new GpuCommandPoly(4)
            .monochrome(0x80);

          final int x1;
          final int x2;
          if(renderable._34 == 0x1000) {
            if(metrics._10.get() < 0) {
              x2 = renderable.x_40 + metrics.x_02.get() - centreX;
              x1 = x2 + metrics.width_08.get();
            } else {
              //LAB_80023f20
              x1 = renderable.x_40 + metrics.x_02.get() - centreX;
              x2 = x1 + metrics.width_08.get();
            }
          } else {
            //LAB_80023f40
            final int a0_0 = renderable._34 != 0 ? renderable._34 : metrics._10.get();

            //LAB_80023f4c
            //LAB_80023f68
            final int a1 = Math.abs(metrics.width_08.get() * a0_0 / 0x1000);
            if(metrics._10.get() < 0) {
              x2 = renderable.x_40 + metrics.width_08.get() / 2 + metrics.x_02.get() - centreX - a1 / 2;
              x1 = x2 + a1;
            } else {
              //LAB_80023fb4
              x1 = renderable.x_40 + metrics.width_08.get() / 2 + metrics.x_02.get() - centreX - a1 / 2;
              x2 = x1 + a1;
            }
          }

          //LAB_80023fe4
          final int y1;
          final int y2;
          if(renderable._38 == 0x1000) {
            if(metrics._12.get() < 0) {
              y2 = renderable.y_44 + metrics.y_03.get() - 120;
              y1 = y2 + metrics.height_0a.get();
            } else {
              //LAB_80024024
              y1 = renderable.y_44 + metrics.y_03.get() - 120;
              y2 = y1 + metrics.height_0a.get();
            }
          } else {
            //LAB_80024044
            final int a0_0 = renderable._38 != 0 ? renderable._38 : metrics._12.get();

            //LAB_80024050
            //LAB_8002406c
            final int a1 = Math.abs(metrics.height_0a.get() * a0_0 / 0x1000);
            if(metrics._12.get() < 0) {
              y2 = renderable.y_44 + metrics.height_0a.get() / 2 + metrics.y_03.get() - a1 / 2 - 120;
              y1 = y2 + a1;
            } else {
              //LAB_800240b8
              y1 = renderable.y_44 + metrics.height_0a.get() / 2 + metrics.y_03.get() - a1 / 2 - 120;
              y2 = y1 + a1;
            }
          }

          //LAB_800240e8
          cmd.pos(0, x1, y1);
          cmd.pos(1, x2, y1);
          cmd.pos(2, x1, y2);
          cmd.pos(3, x2, y2);

          //LAB_80024144
          //LAB_800241b4
          int v1 = metrics.u_00.get() + metrics.width_08.get();
          final int u = v1 < 255 ? v1 : v1 - 1;

          v1 = metrics.v_01.get() + metrics.height_0a.get();
          final int v = v1 < 255 ? v1 : v1 - 1;

          cmd.uv(0, metrics.u_00.get(), metrics.v_01.get());
          cmd.uv(1, u, metrics.v_01.get());
          cmd.uv(2, metrics.u_00.get(), v);
          cmd.uv(3, u, v);

          final int clut = renderable.clut_30 != 0 ? renderable.clut_30 : metrics.clut_04.get() & 0x7fff;
          cmd.clut((clut & 0b111111) * 16, clut >>> 6);

          //LAB_80024214
          final int tpage = renderable.tpage_2c != 0 ? metrics.tpage_06.get() & 0x60 | renderable.tpage_2c : metrics.tpage_06.get() & 0x7f;
          cmd.vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0);
          cmd.bpp(Bpp.of(tpage >>> 7 & 0b11));

          if((metrics.clut_04.get() & 0x8000) != 0) {
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
  public static void deallocateRenderables(final long a0) {
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

  @Method(0x80024480L)
  public static FlowControl scriptGiveGold(final RunningScript<?> script) {
    script.params_20[1].set(addGold(script.params_20[0].get()));
    return FlowControl.CONTINUE;
  }

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

  @Method(0x80024590L)
  public static FlowControl scriptTakeItem(final RunningScript<?> script) {
    final int itemId = script.params_20[0].get() & 0xff;

    final GameState52c state = gameState_800babc8;

    if(itemId < 0xc0) {
      //LAB_800245e0
      for(int i = 0; i < state.equipmentCount_1e4.get(); i++) {
        if(state.equipment_1e8.get(i).get() == itemId) {
          //LAB_8002460c
          script.params_20[1].set(takeEquipment(i));
          return FlowControl.CONTINUE;
        }
      }

      //LAB_80024600
      script.params_20[1].set(0xff);
    } else {
      //LAB_80024628
      script.params_20[1].set(takeItem(itemId));
    }

    //LAB_8002463c
    return FlowControl.CONTINUE;
  }

  @Method(0x8002498cL)
  public static void noop_8002498c() {
    // empty
  }

  @Method(0x80024994L)
  public static void FUN_80024994() {
    // empty
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
  public static void basicUiTexturesLoaded(final List<byte[]> files) {
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
      final byte[] data = files.get(i);

      if(data.length != 0) {
        final Tim tim = new Tim(data, 0);
        final int rectIndex = indexOffsets[i];

        if(i == 0 || i > 2) {
          GPU.uploadData(rects[rectIndex], data, tim.getImageData());
        }

        //LAB_80024efc
        if(i == 3) {
          //LAB_80024f2c
          GPU.uploadData(rects[indexOffsets[i] + 1], data, tim.getClutData());
        } else if(i < 4) {
          //LAB_80024fac
          for(int s0 = 0; s0 < 4; s0++) {
            final RECT rect = new RECT().set(rects[rectIndex + 1]);
            rect.x.set((short)(rect.x.get() + s0 * 16));
            GPU.uploadData(rect, data,  tim.getClutData() + s0 * 0x80);
          }
          //LAB_80024f1c
        } else if(i == 4) {
          //LAB_80024f68
          GPU.uploadData(rects[rectIndex + 1], data, tim.getClutData());
        }
      }
    }
  }

  @Method(0x8002504cL)
  public static void loadBasicUiTexturesAndSomethingElse() {
    loadDrgnDir(0, 6669, Scus94491BpeSegment_8002::basicUiTexturesLoaded);
    noop_8002498c();

    textZ_800bdf00.set(13);
    _800bdf04.setu(0);
    _800bdf08.setu(0);
    _800be5c4.setu(0);
    clearCharacterStats();

    //LAB_800250c0
    //LAB_800250ec
    for(int i = 0; i < 8; i++) {
      textboxes_800be358[i] = new Textbox4c();
      textboxes_800be358[i]._00 = 0;

      _800bdf38[i] = new Struct84();
      _800bdf38[i]._00 = 0;

      textboxArrows_800bdea0[i] = new TextboxArrow0c();

      setTextboxArrowPosition(i, 0);
    }

    //LAB_80025118
    for(int i = 0; i < 8; i++) {
      _800bdf18.offset(i * 0x4L).setu(0);
    }

    _800be5b8.setu(0);
    _800be5bc.setu(0);
    _800be5c0.setu(0);
    _800be5c8.setu(0);
  }

  @Method(0x80025158L)
  public static FlowControl FUN_80025158(final RunningScript<?> script) {
    final int s1 = script.params_20[0].get();
    FUN_800258a8(s1);

    final Struct84 struct84 = _800bdf38[s1];
    struct84.type_04 = script.params_20[1].get();
    struct84._08 |= 0x1000;
    struct84.str_24 = LodString.fromParam(script.params_20[2]);
    struct84.ptr_58 = mallocHead(struct84.chars_1c * (struct84.lines_1e + 1) * 8);
    FUN_8002a2b4(s1);
    FUN_80027d74(s1, struct84.x_14, struct84.y_16);
    return FlowControl.CONTINUE;
  }

  @Method(0x80025218L)
  public static FlowControl scriptAddSobjTextbox(final RunningScript<?> script) {
    if(script.params_20[2].get() == 0) {
      return FlowControl.CONTINUE;
    }

    final int textboxIndex = script.params_20[0].get();
    final int type = (int)_80052ba8.offset(((script.params_20[2].get() & 0xf00) >>> 8) * 0x2L).get();
    clearTextbox(textboxIndex);

    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    textbox._04 = (int)_80052b88.offset(((script.params_20[2].get() & 0xf0) >>> 4) * 0x2L).get();
    textbox._06 = (int)_80052b68.offset((script.params_20[2].get() & 0xf) * 0x2L).get();
    textbox.x_14 = 0;
    textbox.y_16 = 0;
    textbox.chars_18 = script.params_20[3].get() + 1;
    textbox.lines_1a = script.params_20[4].get() + 1;
    FUN_800258a8(textboxIndex);

    final Struct84 struct84 = _800bdf38[textboxIndex];
    struct84.type_04 = type;
    struct84.str_24 = LodString.fromParam(script.params_20[5]);

    if(type == 1 && (script.params_20[1].get() & 0x1000) > 0) {
      struct84._08 |= 0x20;
    }

    //LAB_80025370
    //LAB_80025374
    if(type == 3) {
      struct84._6c = -1;
    }

    //LAB_800253a4
    if(type == 4) {
      struct84._08 |= 0x200;
    }

    //LAB_800253d4
    struct84._08 |= 0x1000;
    struct84.ptr_58 = mallocHead(struct84.chars_1c * (struct84.lines_1e + 1) * 0x8L);
    FUN_8002a2b4(textboxIndex);
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

  @Method(0x800254bcL)
  public static FlowControl FUN_800254bc(final RunningScript<?> a0) {
    final int textboxIndex = a0.params_20[0].get();

    if(a0.params_20[1].get() != 0) {
      final int a2 = a0.params_20[1].get();
      final short s0 = (short)_80052b88.offset((a2 & 0xf0) >>> 3).get();
      final short s1 = (short)_80052b68.offset((a2 & 0xf) * 0x2L).get();
      final short type = (short)_80052ba8.offset((a2 & 0xf00) >>> 7).get();
      clearTextbox(textboxIndex);

      final Textbox4c struct4c = textboxes_800be358[textboxIndex];
      struct4c._04 = s0;
      struct4c._06 = s1;
      struct4c.x_14 = a0.params_20[2].get();
      struct4c.y_16 = a0.params_20[3].get();
      struct4c.chars_18 = a0.params_20[4].get() + 1;
      struct4c.lines_1a = a0.params_20[5].get() + 1;
      FUN_800258a8(textboxIndex);

      final Struct84 struct84 = _800bdf38[textboxIndex];

      struct84.type_04 = type;
      struct84.str_24 = LodString.fromParam(a0.params_20[6]);

      if(type == 1 && (a2 & 0x1000) > 0) {
        struct84._08 |= 0x20;
      }

      //LAB_8002562c
      //LAB_80025630
      if(type == 3) {
        struct84._6c = -1;
      }

      //LAB_80025660
      if(type == 4) {
        struct84._08 |= 0x200;
      }

      //LAB_80025690
      struct84._08 |= 0x1000;
      struct84.ptr_58 = mallocHead(struct84.chars_1c * (struct84.lines_1e + 1) * 0x8L);
      FUN_8002a2b4(textboxIndex);
      FUN_80027d74(textboxIndex, struct84.x_14, struct84.y_16);
    }

    //LAB_800256f0
    return FlowControl.CONTINUE;
  }

  @Method(0x80025718L)
  public static FlowControl FUN_80025718(final RunningScript<?> a0) {
    final Struct84 s0 = _800bdf38[a0.params_20[0].get()];

    s0._6c = -1;
    s0._70 = a0.params_20[2].get();
    s0._72 = a0.params_20[1].get();

    if(s0._00 == 13) {
      s0._00 = 23;
      s0._64 = 10;
      s0._78 = 22;
      Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
    }

    //LAB_800257bc
    s0._08 |= 0x800;
    return FlowControl.CONTINUE;
  }

  @Method(0x800257e0L)
  public static void clearTextbox(final int textboxIndex) {
    if(_800bdf38[textboxIndex]._00 != 0) {
      free(_800bdf38[textboxIndex].ptr_58);
    }

    //LAB_80025824
    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    textbox._00 = 1;
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
  public static void FUN_800258a8(final int a0) {
    final Struct84 struct84 = _800bdf38[a0];
    struct84._00 = 1;
    struct84._08 = 0;
    struct84.z_0c = 13;
    struct84._10 = 0;
    struct84._20 = 0x1000;
    struct84._22 = 0x1000;
    struct84._28 = 0;
    struct84._2a = 2;
    struct84._2c = 0;
    struct84._30 = 0;
    struct84._34 = 0;
    struct84._36 = 0;
    struct84._38 = 0;
    struct84._3a = 0;
    struct84._3c = 0;
    struct84._3e = 1;
    struct84._40 = 0;
    struct84._42 = 0;
    struct84._44 = 0;

    final Textbox4c struct4c = textboxes_800be358[a0];
    struct84.x_14 = struct4c.x_14;
    struct84.y_16 = struct4c.y_16;
    struct84.chars_1c = struct4c.chars_18 - 1;
    struct84.lines_1e = struct4c.lines_1a - 1;
    struct84._18 = struct84.x_14 - struct84.chars_1c * 9 / 2;
    struct84._1a = struct84.y_16 - struct84.lines_1e * 6;

    //LAB_800259b4
    for(int i = 0; i < 8; i++) {
      struct84._46[i] = 0;
    }

    //LAB_800259e4
    struct84._5c = 0;
    struct84._60 = 0;
    struct84._64 = 0;
    struct84._68 = 0;
    struct84._6c = 0;
    struct84._70 = 0;
    struct84._72 = 0;
    struct84._74 = 0;
    struct84._78 = 0;
    struct84._7c = 0;
    struct84._80 = 0;
  }

  @Method(0x80025a04L)
  public static void FUN_80025a04(final int textboxIndex) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];

    switch(textbox._00) {
      case 1 -> {
        if(textbox._04 == 0) {
          //LAB_80025ab8
          textbox._00 = 4;
          textbox._08 ^= 0x8000_0000;
          break;
          //LAB_80025aa8
        } else if(textbox._04 == 2) {
          //LAB_80025ad4
          textbox._00 = 2;
          textbox._08 |= 0x1;
          textbox._10 = 0;
          textbox._24 = 60 / vsyncMode_8007a3b8.get() / 4;

          if((textbox._08 & 0x2) != 0) {
            textbox._30 = (textbox._28 - textbox._38) / textbox._24;
            textbox._34 = (textbox._2c - textbox._3c) / textbox._24;
          }
          break;
        }

        //LAB_80025b54
        //LAB_80025b5c
        textbox._00 = 5;
        textbox.width_1c = textbox.chars_18 * 9 / 2;
        textbox.height_1e = textbox.lines_1a * 6;
        if((textbox._08 & 0x4) != 0) {
          textbox._00 = 6;
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
            textbox._00 = 5;
            textbox._08 ^= 1;
            textbox.width_1c = textbox.chars_18 * 9 / 2;
            textbox.height_1e = textbox.lines_1a * 6;

            if((textbox._08 & 0x4) != 0) {
              textbox._00 = 6;
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
        textbox._00 = 5;

        if((textbox._08 & 0x4) != 0) {
          textbox._00 = 6;
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
            textbox._00 = 0;
            textbox._08 ^= 0x1;
          }
          break;
        }

        //LAB_80025e94
        textbox._00 = 0;
      }

      case 4, 5 -> {
        if(_800bdf38[textboxIndex]._00 == 0) {
          if(textbox._04 == 2) {
            textbox._00 = 3;
            textbox._08 |= 0x1;

            final int v0 = 60 / vsyncMode_8007a3b8.get() / 4;
            textbox._10 = v0;
            textbox._24 = v0;
          } else {
            //LAB_80025f30
            textbox._00 = 0;
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
      if(textbox._00 != 1) {
        final int x = textbox.x_14 - centreScreenX_1f8003dc.get();
        final int y = textbox.y_16 - centreScreenY_1f8003de.get();
        final int v0 = (int)_800be5c4.getSigned() * 0xc;

        GPU.queueCommand(textbox.z_0c, new GpuCommandPoly(4)
          .translucent(Translucency.HALF_B_PLUS_HALF_F)
          .monochrome(0, 0)
          .pos(0, x - textbox.width_1c, y - textbox.height_1e)
          .rgb(1, (int)_80010868.offset(v0).offset(0x0L).get(), (int)_80010868.offset(v0).offset(0x4L).get(), (int)_80010868.offset(v0).offset(0x8L).get())
          .pos(1, x + textbox.width_1c, y - textbox.height_1e)
          .rgb(2, (int)_80010868.offset(v0).offset(0x0L).get(), (int)_80010868.offset(v0).offset(0x4L).get(), (int)_80010868.offset(v0).offset(0x8L).get())
          .pos(2, x - textbox.width_1c, y + textbox.height_1e)
          .monochrome(3, 0)
          .pos(3, x + textbox.width_1c, y + textbox.height_1e)
        );

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
    final Struct84 struct84 = _800bdf38[textboxIndex];

    long v1 = struct84._00;
    if(v1 == 1) {
      //LAB_8002663c
      if((struct84._08 & 0x1) == 0) {
        switch(struct84.type_04) {
          case 0:
            struct84._00 = 0xc;
            break;

          case 2:
            struct84._00 = 10;
            struct84._08 |= 0x1;
            struct84._2a = 1;
            struct84._34 = 0;
            struct84._36 = struct84.lines_1e;
            break;

          case 3:
            struct84._00 = 23;
            struct84._08 |= 0x1;
            struct84._2a = 1;
            struct84._34 = 0;
            struct84._36 = 0;
            struct84._64 = 10;
            struct84._78 = 17;
            Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
            break;

          case 4:
            //LAB_80026780
            do {
              FUN_800274f0(textboxIndex);
            } while((struct84._08 & 0x400) == 0);

            struct84._08 ^= 0x400;
            // Fall through

          default:
            //LAB_800267a0
            struct84._00 = 4;
            break;
        }
      }
    } else if(v1 == 2) {
      struct84._00 = 4;
      //LAB_80026538
    } else if(v1 == 4) {
      //LAB_800267c4
      FUN_800274f0(textboxIndex);
    } else if(v1 == 5) {
      //LAB_800267d4
      if((struct84._08 & 0x1) != 0) {
        //LAB_800267f4
        if(struct84._3a >= struct84.lines_1e - ((struct84._08 & 0x200) == 0 ? 1 : 2)) {
          struct84._08 ^= 0x1;
          struct84._3a = 0;
          setTextboxArrowPosition(textboxIndex, 1);
        } else {
          //LAB_80026828
          struct84._00 = 9;
          struct84._3a++;
          FUN_80028828(textboxIndex);
        }
      } else {
        //LAB_8002684c
        if((struct84._08 & 0x20) != 0) {
          struct84._00 = 9;
          struct84._08 |= 0x1;
        } else {
          //LAB_8002686c
          if((joypadPress_8007a398.get() & 0x20L) != 0) {
            setTextboxArrowPosition(textboxIndex, 0);

            v1 = struct84.type_04;
            if(v1 == 1 || v1 == 4) {
              //LAB_800268b4
              struct84._00 = 9;
              struct84._08 |= 0x1;
            }

            if(v1 == 2) {
              //LAB_800268d0
              struct84._00 = 10;
            }
          }
        }
      }
    } else if(v1 == 6) {
      //LAB_800268dc
      if((joypadPress_8007a398.get() & 0x20) != 0) {
        struct84._00 = 4;
      }
    } else if(v1 == 7) {
      //LAB_800268fc
      struct84._40++;
      if(struct84._40 >= struct84._3e) {
        struct84._40 = 0;
        struct84._00 = 4;
      }

      //LAB_80026928
      if((struct84._08 & 0x20) == 0) {
        if((joypadInput_8007a39c.get() & 0x20) != 0) {
          s3 = 0;

          //LAB_80026954
          for(s1 = 0; s1 < 4; s1++) {
            FUN_800274f0(textboxIndex);

            v1 = struct84._00;
            if(v1 < 7 || v1 == 15 || v1 == 11 || v1 == 13) {
              //LAB_8002698c
              s3 = 0x1L;
              break;
            }

            //LAB_80026994
          }

          //LAB_800269a0
          if(s3 == 0) {
            struct84._40 = 0;
            struct84._00 = 4;
          }
        }
      }
    } else if(v1 == 8) {
      //LAB_800269cc
      if(struct84._44 > 0) {
        //LAB_800269e8
        struct84._44--;
      } else {
        //LAB_800269e0
        struct84._00 = 4;
      }
      //LAB_80026554
    } else if(v1 == 9) {
      //LAB_800269f0
      FUN_80028828(textboxIndex);
      //LAB_80026580
    } else if(v1 == 10) {
      //LAB_80026a00
      FUN_800288a4(textboxIndex);

      if((struct84._08 & 0x4) != 0) {
        struct84._08 ^= 0x4;
        if((struct84._08 & 0x2) == 0) {
          //LAB_80026a5c
          do {
            FUN_800274f0(textboxIndex);
            v1 = struct84._00;
            if(v1 == 0xf) {
              struct84._3a = 0;
              struct84._08 |= 0x2;
            }
          } while(v1 != 0x5 && v1 != 0xf);

          //LAB_80026a8c
          struct84._00 = 10;
        } else {
          struct84._3a++;

          if(struct84._3a >= struct84.lines_1e + 1) {
            free(struct84.ptr_58);
            struct84._00 = 0;
          }
        }
      }
    } else if(v1 == 11) {
      //LAB_80026a98
      if((joypadPress_8007a398.get() & 0x20L) != 0) {
        setTextboxArrowPosition(textboxIndex, 0);
        FUN_8002a2b4(textboxIndex);

        struct84._00 = 4;
        struct84._08 ^= 0x1;
        struct84._34 = 0;
        struct84._36 = 0;
        struct84._3a = 0;

        if((struct84._08 & 0x8) != 0) {
          struct84._00 = 13;
        }
      }
    } else if(v1 == 12) {
      //LAB_80026af0
      if(struct4c._00 == 0) {
        free(struct84.ptr_58);
        struct84._00 = 0;
      }
    } else if(v1 == 13) {
      //LAB_80026b34
      struct84._08 |= 0x8;
      setTextboxArrowPosition(textboxIndex, 1);

      //LAB_80026b4c
      do {
        FUN_800274f0(textboxIndex);
        v1 = struct84._00;
        if(v1 == 0x5) {
          //LAB_80026b28
          struct84._00 = 11;
          break;
        }
      } while(v1 != 0xf);

      //LAB_80026b6c
      if((struct84._08 & 0x20) != 0) {
        setTextboxArrowPosition(textboxIndex, 0);
      }

      //LAB_80026ba0
      if(struct84._3e != 0) {
        setTextboxArrowPosition(textboxIndex, 0);
        struct84._5c = struct84._00;
        struct84._00 = 14;
      }

      //LAB_80026bc8
      if((struct84._08 & 0x800) != 0) {
        setTextboxArrowPosition(textboxIndex, 0);
        struct84._00 = 23;
        struct84._64 = 10;
        struct84._78 = 22;
        struct84._68 = struct84._72;
        Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
      }
    } else if(v1 == 14) {
      //LAB_80026c18
      if((struct84._08 & 0x40) == 0) {
        struct84._40--;

        if(struct84._40 <= 0) {
          struct84._40 =struct84._3e;

          v1 = struct84._5c;
          if(v1 == 11) {
            //LAB_80026c70
            FUN_8002a2b4(textboxIndex);
            struct84._34 = 0;
            struct84._36 = 0;
            struct84._3a = 0;
            struct84._00 = 13;
            struct84._08 ^= 0x1;
          } else if(v1 == 15) {
            //LAB_80026c98
            //LAB_80026c9c
            free(struct84.ptr_58);
            struct84._00 = 0;
          }
        }
      }
    } else if(v1 == 15) {
      //LAB_80026cb0
      if((struct84._08 & 0x20) != 0) {
        struct84._00 = 16;
      } else {
        //LAB_80026cd0
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          free(struct84.ptr_58);
          struct84._00 = 0;
          setTextboxArrowPosition(textboxIndex, 0);
        }
      }
      //LAB_800265d8
    } else if(v1 == 16) {
      //LAB_80026cdc
      //LAB_80026ce8
      if((struct84._08 & 0x40) != 0) {
        free(struct84.ptr_58);
        struct84._00 = 0;
        setTextboxArrowPosition(textboxIndex, 0);
      }
    } else if(v1 == 17) {
      //LAB_80026d20
      struct84.lines_1e++;

      //LAB_80026d30
      do {
        FUN_800274f0(textboxIndex);
        v1 = struct84._00;
        if(v1 == 5) {
          //LAB_80026d14
          struct84._00 = 18;
          break;
        }
        if(v1 == 0xfL) {
          struct84._00 = 18;
          struct84._3a = 0;
          struct84._08 |= 0x102;
          break;
        }
      } while(true);

      //LAB_80026d64
      struct84._6c = -1;
      struct84.lines_1e--;
      //LAB_8002659c
    } else if(v1 == 18) {
      //LAB_80026d94
      renderTextboxSelection(textboxIndex, (short)struct84._60);

      if((joypadPress_8007a398.get() & 0x20) != 0) {
        Scus94491BpeSegment.playSound(0, 2, 0, 0, (short)0, (short)0);
        free(struct84.ptr_58);
        struct84._00 = 0;
        struct84._6c = struct84._68;
      } else {
        //LAB_80026df0
        if((joypadInput_8007a39c.get() & 0x4000L) == 0) {
          //LAB_80026ee8
          if((joypadInput_8007a39c.get() & 0x1000L) != 0) {
            if((struct84._08 & 0x100) == 0 || struct84._68 != 0) {
              //LAB_80026f38
              Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

              s3 = 0x3L;
              if(struct84._60 > 0) {
                struct84._00 = 19;
                struct84._60--;
                struct84._64 = 4;
                struct84._68--;
              } else {
                //LAB_80026f88
                if((struct84._08 & 0x2) != 0) {
                  v1 = struct84._3a;

                  // TODO not sure about this block of code
                  if(v1 == 1) {
                    s3 = 0x1L;
                  } else {
                    if(v1 == 0) {
                      //LAB_80026fbc
                      s3 = 0x2L;
                    }

                    //LAB_80026fc0
                    struct84._3a = 0;
                    struct84._08 ^= 0x2;
                  }

                  //LAB_80026fe8
                  struct84._3a--;
                }

                //LAB_80027014
                struct84._68--;

                if(struct84._68 < 0) {
                  struct84._68 = 0;
                } else {
                  //LAB_80027044
                  struct84._2c = 12;
                  FUN_800280d4(textboxIndex);

                  final LodString str = struct84.str_24;

                  //LAB_80027068
                  s1 = 0;
                  do {
                    if(str.charAt(struct84._30 - 1) >>> 8 == 0xa1) {
                      s1++;
                    }

                    //LAB_80027090
                    if(s1 == struct84.lines_1e + s3) {
                      break;
                    }

                    struct84._30--;
                  } while(struct84._30 > 0);

                  //LAB_800270b0
                  struct84._34 = 0;
                  struct84._36 = 0;
                  struct84._08 |= 0x80;

                  //LAB_800270dc
                  do {
                    FUN_800274f0(textboxIndex);
                  } while(struct84._36 == 0 && struct84._00 != 5);

                  //LAB_80027104
                  struct84._00 = 21;
                  struct84._08 ^= 0x80;
                }
              }
            }
          }
        }

        struct84._00 = 19;
        struct84._60++;
        struct84._64 = 4;
        struct84._68++;
        if((struct84._08 & 0x100) == 0 || struct84._36 + 1 != struct84._68) {
          //LAB_80026e68
          //LAB_80026e6c
          if(struct84._60 < struct84.lines_1e) {
            //LAB_80026ed0
            Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

            //LAB_80026ee8
            if((joypadInput_8007a39c.get() & 0x1000L) != 0) {
              if((struct84._08 & 0x100) == 0 || struct84._68 != 0) {
                //LAB_80026f38
                Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

                s3 = 0x3L;
                if(struct84._60 > 0) {
                  struct84._00 = 19;
                  struct84._60--;
                  struct84._64 = 4;
                  struct84._68--;
                } else {
                  //LAB_80026f88
                  if((struct84._08 & 0x2) != 0) {
                    v1 = struct84._3a;

                    // TODO not sure about this block of code
                    if(v1 == 1) {
                      s3 = 0x1L;
                    } else {
                      if(v1 == 0) {
                        //LAB_80026fbc
                        s3 = 0x2L;
                      }

                      //LAB_80026fc0
                      struct84._3a = 0;
                      struct84._08 ^= 0x2;
                    }

                    //LAB_80026fe8
                    struct84._3a--;
                  }

                  //LAB_80027014
                  struct84._68--;

                  if(struct84._68 < 0) {
                    struct84._68 = 0;
                  } else {
                    //LAB_80027044
                    struct84._2c = 12;
                    FUN_800280d4(textboxIndex);

                    final LodString str = struct84.str_24;

                    //LAB_80027068
                    s1 = 0;
                    do {
                      if(str.charAt(struct84._30 - 1) >>> 8 == 0xa1) {
                        s1++;
                      }

                      //LAB_80027090
                      if(s1 == struct84.lines_1e + s3) {
                        break;
                      }

                      struct84._30--;
                    } while(struct84._30 > 0);

                    //LAB_800270b0
                    struct84._34 = 0;
                    struct84._36 = 0;
                    struct84._08 |= 0x80;

                    //LAB_800270dc
                    do {
                      FUN_800274f0(textboxIndex);
                    } while(struct84._36 == 0 && struct84._00 != 5);

                    //LAB_80027104
                    struct84._00 = 21;
                    struct84._08 ^= 0x80;
                  }
                }
              }
            }
          } else {
            struct84._60 = _800bdf38[textboxIndex].lines_1e - 1;
            struct84._00 = 0x14;
            struct84._2c = (short)0;

            if(struct84._3a == 1) {
              struct84._00 = 18;
              struct84._68--;
            }
          }
        } else {
          struct84._00 = 3;
          struct84._60--;
          struct84._68--;
        }
      }
    } else if(v1 == 19) {
      //LAB_8002711c
      renderTextboxSelection(textboxIndex, (short)struct84._68);
      struct84._64--;

      if(struct84._64 == 0) {
        struct84._00 = 18;

        if((struct84._08 & 0x800) != 0) {
          struct84._00 = 22;
        }
      }
    } else if(v1 == 20) {
      //LAB_8002715c
      struct84._2c += 4;

      if(struct84._2c >= 12) {
        FUN_80027eb4(textboxIndex);
        struct84._08 |= 0x4;
        struct84._2c -= 12;
        struct84._36 = struct84.lines_1e;
      }

      //LAB_800271a8
      if((struct84._08 & 0x4) != 0) {
        struct84._08 ^= 0x4;

        if((struct84._08 & 0x2) == 0) {
          //LAB_8002720c
          //LAB_80027220
          do {
            FUN_800274f0(textboxIndex);

            v1 = struct84._00;
            if(v1 == 0xf) {
              struct84._3a = 0;
              struct84._08 |= 0x2;
              break;
            }
          } while(v1 != 5);
        } else {
          struct84._3a++;
          if(struct84._3a >= struct84.lines_1e + 1) {
            free(struct84.ptr_58);
            struct84._00 = 0;
          }
        }

        //LAB_80027250
        //LAB_80027254
        struct84._00 = 18;
      }
      //LAB_800265f4
    } else if(v1 == 21) {
      //LAB_8002727c
      struct84._2c -= 4;

      if(struct84._2c <= 0) {
        struct84._36 = 0;
        struct84._2c = 0;
        struct84._00 = 18;
        struct84._08 |= 0x4;
      }

      //LAB_800272b0
      if((struct84._08 & 0x4) != 0) {
        final LodString str = struct84.str_24;

        //LAB_800272dc
        s1 = 0;
        do {
          v0 = str.charAt(struct84._30 + 1) >>> 8;
          if(v0 == 0xa0L) {
            //LAB_80027274
            struct84._30--;
            break;
          }

          if(v0 == 0xa1) {
            s1++;
          }

          //LAB_8002730c
          struct84._30++;
        } while(s1 != struct84.lines_1e);

        //LAB_80027320
        struct84._00 = 18;
        struct84._30 += 2;
        struct84._34 = 0;
        struct84._36 = struct84.lines_1e;
      }
    } else if(v1 == 0x16) {
      //LAB_80027354
      renderTextboxSelection(textboxIndex, (short)struct84._68);

      if((joypadPress_8007a398.get() & 0x20L) != 0) {
        Scus94491BpeSegment.playSound(0, 2, 0, 0, (short)0, (short)0);
        free(struct84.ptr_58);
        struct84._00 = 0;
        struct84._6c = struct84._68 - struct84._72;
      } else {
        //LAB_800273bc
        if((joypadInput_8007a39c.get() & 0x1000L) != 0) {
          struct84._00 = 19;
          struct84._64 = 4;
          struct84._68--;

          if(struct84._68 < struct84._72) {
            struct84._68 = struct84._72;
            struct84._00 = 22;
          } else {
            //LAB_80027404
            Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);
          }
        }

        //LAB_80027420
        if((joypadInput_8007a39c.get() & 0x4000L) != 0) {
          struct84._00 = 19;
          struct84._64 = 4;
          struct84._68++;

          if(struct84._70 >= struct84._68) {
            //LAB_80027480
            //LAB_80027490
            Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);
          } else {
            struct84._68 = struct84._70;
            struct84._00 = 22;
          }
        }
      }
      //LAB_80026620
    } else if(v1 == 23) {
      //LAB_800274a4
      struct84._64--;
      if(struct84._64 == 0) {
        struct84._64 = 4;
        struct84._00 = struct84._78;
      }
    }

    //LAB_800274c8
    updateTextboxArrowSprite(textboxIndex);
  }

  @Method(0x800274f0L)
  public static void FUN_800274f0(final int textboxIndex) {
    long v1;

    final Struct84 s0 = _800bdf38[textboxIndex];
    final LodString str = s0.str_24;

    if((s0._08 & 0x10) != 0) {
      final int s1 = (short)s0._80;
      FUN_8002a180(textboxIndex, s0._34, s0._36, s0._28, s0._46[s1]);

      s0._34++;
      s0._3c++;
      s0._80++;

      if(s0._34 < s0.chars_1c) {
        //LAB_80027768
        if(s0._46[s1 + 1] == -1) {
          s0._08 ^= 0x10;
        }
      } else if(s0._36 >= s0.lines_1e - 1) {
        if(s0._46[s1 + 1] != -1) {
          s0._00 = 5;
          s0._34 = 0;
          s0._36++;
          setTextboxArrowPosition(textboxIndex, 1);
          return;
        }

        //LAB_80027618
        v1 = str.charAt(s0._30) >>> 8;

        if(v1 == 0xa0) {
          //LAB_800276f4
          s0._00 = 15;

          //LAB_80027704
          setTextboxArrowPosition(textboxIndex, 1);

          //LAB_80027740
          _800bdf38[textboxIndex]._08 ^= 0x10;
          return;
        }

        if(v1 == 0xa1) {
          s0._30++;
        }

        //LAB_8002764c
        s0._00 = 5;
        s0._34 = 0;
        s0._36++;

        //LAB_80027704
        setTextboxArrowPosition(textboxIndex, 1);
      } else {
        //LAB_80027688
        s0._34 = 0;
        s0._36++;

        if(s0._46[s1 + 1] == -1) {
          v1 = str.charAt(s0._30) >>> 8;
          if(v1 == 0xa0) {
            //LAB_800276f4
            s0._00 = 15;

            //LAB_80027704
            setTextboxArrowPosition(textboxIndex, 1);
          } else {
            if(v1 == 0xa1) {
              //LAB_80027714
              s0._30++;
            }

            //LAB_80027724
            _800bdf38[textboxIndex]._00 = 7;
          }

          //LAB_80027740
          _800bdf38[textboxIndex]._08 ^= 0x10;
          return;
        }
      }

      //LAB_8002779c
      _800bdf38[textboxIndex]._00 = 7;
      return;
    }

    //LAB_800277bc
    long s1 = 0x1L;
    final long s7 = 0x1L;
    final long fp = 0x1L;

    //LAB_800277cc
    do {
      final int a0_0 = str.charAt(s0._30);

      switch(a0_0 >>> 8) {
        case 0xa0 -> {
          s0._00 = 15;
          setTextboxArrowPosition(textboxIndex, 1);
          s1 = 0;
        }

        case 0xa1 -> {
          s0._34 = 0;
          s0._36++;
          s0._08 |= 0x400;

          if(s0._36 >= s0.lines_1e || (s0._08 & 0x80) != 0) {
            //LAB_80027880
            s0._00 = 5;

            if((s0._08 & 0x1) == 0) {
              setTextboxArrowPosition(textboxIndex, 1);
            }

            s1 = 0;
          }
        }

        case 0xa2 -> {
          //LAB_80027d28
          s0._00 = 6;

          //LAB_80027d2c
          s1 = 0;
        }

        case 0xa3 -> {
          setTextboxArrowPosition(textboxIndex, 1);
          s0._00 = 11;

          if(str.charAt(s0._30 + 1) >>> 8 == 0xa1L) {
            s0._30++;
          }

          s1 = 0;
        }

        case 0xa5 -> {
          s0._3e = a0_0 & 0xff;
          s0._40 = 0;
        }

        case 0xa6 -> {
          s0._00 = 8;
          s0._44 = 60 / vsyncMode_8007a3b8.get() * (a0_0 & 0xff);
          s1 = 0;
        }

        case 0xa7 -> {
          final int a2 = a0_0 & 0xf;

          //LAB_80027950
          s0._28 = a2 < 12 ? a2 : 0;
        }

        case 0xa8 -> {
          s0._08 |= 0x10;

          //LAB_80027970
          for(int i = 0; i < 8; i++) {
            s0._46[i] = -1;
          }

          long a1 = _800bdf10.offset((a0_0 & 0xff) * 0x4L).get();
          long a3 = 1_000_000_000L;
          final long[] sp0x18 = new long[10]; //TODO LodString
          if(s7 != 0) {
            //LAB_800279dc
            for(int i = 0; i < 10; i++) {
              sp0x18[i] = _80052b40.get((int)(a1 / a3)).deref().charAt(0);
              a1 = a1 % a3;
              a3 = a3 / 10;
            }
          }

          //LAB_80027a34
          if(fp != 0) {
            v1 = _80052b40.get(0).deref().charAt(0);

            //LAB_80027a54
            for(s1 = 0; s1 < 9; s1++) {
              if(sp0x18[(int)s1] != v1) {
                break;
              }
            }
          } else {
            s1 = 0;
          }

          //LAB_80027a84
          //LAB_80027a90
          for(int i = 0; i < 8 && s1 < 10; i++, s1++) {
            s0._46[i] = (int)sp0x18[(int)s1];
          }

          //LAB_80027ae4
          s0._80 = 0;

          //LAB_80027d2c
          s1 = 0;
        }

        case 0xad -> {
          final int v1_0 = a0_0 & 0xff;

          if(v1_0 >= s0.chars_1c) {
            s0._34 = s0.chars_1c - 1;
          } else {
            //LAB_80027b0c
            s0._34 = v1_0;
          }
        }

        case 0xae ->
          //LAB_80027b38
          s0._36 = Math.min(a0_0 & 0xff, s0.lines_1e - 1);

        case 0xb0 -> {
          s0._00 = 13;

          final int v0 = 60 / vsyncMode_8007a3b8.get() * (a0_0 & 0xff);
          s0._3e = v0;
          s0._40 = v0;

          if(str.charAt(s0._30 + 1) >>> 8 == 0xa1L) {
            s0._30++;
          }

          s1 = 0;
        }

        case 0xb1 -> s0._7c = a0_0 & 0xff;

        case 0xb2 -> {
          if((a0_0 & 0x1L) == 0x1L) {
            s0._08 |= 0x1000;
          } else {
            //LAB_80027bd0
            s0._08 ^= 0x1000;
          }
        }

        default -> {
          //LAB_80027be4
          FUN_8002a180(textboxIndex, s0._34, s0._36, s0._28, (short)a0_0);

          s0._34++;
          s0._3c++;

          if(s0._34 < s0.chars_1c) {
            //LAB_80027d28
            s0._00 = 7;
          } else if(s0._36 >= s0.lines_1e - 1) {
            v1 = str.charAt(s0._30 + 1) >>> 8;

            if(v1 == 0xa0) {
              //LAB_80027c7c
              s0._00 = 15;
              setTextboxArrowPosition(textboxIndex, 1);
            } else {
              if(v1 == 0xa1) {
                //LAB_80027c98
                s0._30++;
              }

              //LAB_80027c9c
              s0._00 = 5;
              s0._08 |= 0x400;
              s0._34 = 0;
              s0._36++;

              if((s0._08 & 0x1) == 0) {
                setTextboxArrowPosition(textboxIndex, 1);
              }
            }
          } else {
            //LAB_80027ce0
            s0._08 |= 0x400;
            s0._34 = 0;
            s0._36++;

            if(str.charAt(s0._30 + 1) >>> 8 == 0xa1L) {
              s0._30++;
            }

            //LAB_80027d28
            s0._00 = 7;
          }

          //LAB_80027d2c
          s1 = 0;
        }
      }

      //LAB_80027d30
      s0._30++;
    } while(s1 != 0);

    //LAB_80027d44
  }

  @Method(0x80027d74L)
  public static void FUN_80027d74(final int textboxIndex, final int x, final int y) {
    final int maxX;
    if(mainCallbackIndex_8004dd20.get() == 5) {
      maxX = 350;
    } else {
      maxX = 310;
    }

    //LAB_80027d9c
    final Struct84 t0 = _800bdf38[textboxIndex];
    final int width = t0.chars_1c * 9 / 2;
    final int height = t0.lines_1e * 6;
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
    t0.x_14 = t2;
    v0.y_16 = t3;
    t0.y_16 = t3;
    t0._18 = t2 - width;
    t0._1a = t3 - height;
  }

  @Method(0x80027eb4L)
  public static void FUN_80027eb4(final int textboxIndex) {
    final Struct84 a0 = _800bdf38[textboxIndex];
    final long a2 = a0.ptr_58;

    //LAB_80027efc
    for(int i = (a0._08 & 0x200) > 0 ? 1 : 0; i < a0.lines_1e; i++) {
      //LAB_80027f18
      for(int a1 = 0; a1 < a0.chars_1c; a1++) {
        final long v0 = a2 + ((i + 1) * a0.chars_1c + a1) * 0x8L;
        final long v1 = a2 + (i * a0.chars_1c + a1) * 0x8L;
        MEMORY.ref(2, v1).offset(0x0L).setu(MEMORY.ref(2, v0).offset(0x0L).get());
        MEMORY.ref(2, v1).offset(0x2L).setu(MEMORY.ref(2, v0).offset(0x2L).get() - 1);
        MEMORY.ref(1, v1).offset(0x4L).setu(MEMORY.ref(1, v0).offset(0x4L).get());
        MEMORY.ref(2, v1).offset(0x6L).setu(MEMORY.ref(2, v0).offset(0x6L).get());
      }
    }

    //LAB_8002804c
    //LAB_80028098
    for(int i = a0.chars_1c * a0.lines_1e; i < a0.chars_1c * (a0.lines_1e + 1); i++) {
      final long a0_0 = a2 + i * 0x8L;
      MEMORY.ref(2, a0_0).offset(0x0L).setu(0);
      MEMORY.ref(2, a0_0).offset(0x2L).setu(0);
      MEMORY.ref(1, a0_0).offset(0x4L).setu(0);
      MEMORY.ref(2, a0_0).offset(0x6L).setu(0);
    }

    //LAB_800280cc
  }

  @Method(0x800280d4L)
  public static void FUN_800280d4(final int textboxIndex) {
    final Struct84 t4 = _800bdf38[textboxIndex];
    final long a2 = t4.ptr_58;

    //LAB_8002810c
    for(int lineIndex = t4.lines_1e; lineIndex > 0; lineIndex--) {
      //LAB_80028128
      for(int charIndex = 0; charIndex < t4.chars_1c; charIndex++) {
        final long v0 = a2 + ((lineIndex - 1) * t4.chars_1c + charIndex) * 8;
        final long v1 = a2 + (lineIndex * t4.chars_1c + charIndex) * 8;
        MEMORY.ref(2, v1).offset(0x0L).setu(MEMORY.ref(2, v0).offset(0x0L).get());
        MEMORY.ref(2, v1).offset(0x2L).setu(MEMORY.ref(2, v0).offset(0x2L).get() + 1);
        MEMORY.ref(1, v1).offset(0x4L).setu(MEMORY.ref(1, v0).offset(0x4L).get());
        MEMORY.ref(2, v1).offset(0x6L).setu(MEMORY.ref(2, v0).offset(0x6L).get());
      }
    }

    //LAB_80028254
    //LAB_80028280
    for(int charIndex = 0; charIndex < t4.chars_1c; charIndex++) {
      final long a0_0 = a2 + charIndex * 0x8L;
      MEMORY.ref(2, a0_0).offset(0x0L).setu(0);
      MEMORY.ref(2, a0_0).offset(0x2L).setu(0);
      MEMORY.ref(1, a0_0).offset(0x4L).setu(0);
      MEMORY.ref(2, a0_0).offset(0x6L).setu(0);
    }

    //LAB_800282a4
  }

  @Method(0x800282acL)
  public static void renderTextboxText(final int textboxIndex) {
    int s1;
    int s2;
    int s3;

    final Struct84 s7 = _800bdf38[textboxIndex];

    final int sp10;
    final int sp14;
    if((s7._08 & 0x200) != 0) {
      sp10 = s7.chars_1c;
      sp14 = s7.chars_1c * 2;
    } else {
      sp10 = 0;
      sp14 = s7.chars_1c;
    }

    int sp38 = 0;

    //LAB_80028328
    long fp = s7.ptr_58;

    //LAB_80028348
    for(int i = 0; i < s7.chars_1c * (s7.lines_1e + 1); i++) {
      if(MEMORY.ref(2, fp).offset(0x0L).getSigned() == 0) {
        sp38 = 0;
      }

      //LAB_8002835c
      if(MEMORY.ref(2, fp).offset(0x6L).getSigned() != 0) {
        sp38 = sp38 - FUN_8002a25c(MEMORY.ref(2, fp).offset(0x6L).get());

        s1 = 0;
        s2 = 0;
        s3 = 0;
        if((s7._08 & 0x1) != 0) {
          if(i >= sp10 && i < sp14) {
            final int v1 = s7._2c;
            s1 = -v1;
            s2 = -v1;
            s3 = v1;
          }

          //LAB_800283c4
          if(i >= s7.chars_1c * s7.lines_1e && i < s7.chars_1c * (s7.lines_1e + 1)) {
            s1 = 0;
            s2 = 0;
            s3 = 12 - s7._2c;
          }
        }

        //LAB_8002840c
        FUN_8002a63c((int)MEMORY.ref(2, fp).offset(0x6L).get());
        if((short)s3 < 13) {
          final int s4 = (int)_800be5c0.get() & 0xffff;
          final int sp18 = (int)_800be5b8.get() & 0xffff;
          final int sp20 = (int)_800be5c8.get() & 0xffff;
          final int sp22 = (int)_800be5bc.get() & 0xffff;

          final GpuCommandQuad cmd = new GpuCommandQuad()
            .monochrome(0x80);

          final int x = s7._18 + (int)MEMORY.ref(2, fp).offset(0x0L).getSigned() * 9 - centreScreenX_1f8003dc.get() - sp38;
          final int y;

          if((s7._08 & 0x200) != 0 && i < s7.chars_1c) {
            y = s7._1a + (int)MEMORY.ref(2, fp).offset(0x2L).getSigned() * 12 - centreScreenY_1f8003de.get() - s1;
          } else {
            y = s7._1a + (int)MEMORY.ref(2, fp).offset(0x2L).getSigned() * 12 - centreScreenY_1f8003de.get() - s1 - s7._2c;
          }

          //LAB_80028544
          //LAB_80028564
          final int u = s4 * 16;
          final int v = (sp22 < 6 ? 0 : 240) + sp20 * 12 - s2;
          final int clutY = sp18 + 480;

          cmd.uv(u, v);
          cmd.clut(832 + (int)MEMORY.ref(1, fp).offset(0x4L).get() * 16, clutY);

          final int height = 12 - s3;
          cmd.pos(x, y, 8, height);
          cmd.bpp(Bpp.BITS_4);
          cmd.vramPos(textboxVramX_80052bc8.get(sp22).get(), textboxVramY_80052bf4.get(sp22).get() < 256 ? 0 : 256);
          GPU.queueCommand(s7.z_0c, cmd);

          GPU.queueCommand(s7.z_0c + 1, new GpuCommandQuad()
            .bpp(Bpp.BITS_4)
            .monochrome(0x80)
            .clut(976, clutY)
            .vramPos(textboxVramX_80052bc8.get(sp22).get(), textboxVramY_80052bf4.get(sp22).get() < 256 ? 0 : 256)
            .pos(x + 1, y + 1, 8, height)
            .uv(u, v)
          );
        }

        sp38 = sp38 + FUN_8002a1fc(MEMORY.ref(2, fp).offset(0x6L).get());
      }

      //LAB_800287d4
      fp = fp + 0x8L;
    }

    //LAB_800287f8
  }

  @Method(0x80028828L)
  public static void FUN_80028828(final int a0) {
    final Struct84 s0 = _800bdf38[a0];

    s0._2c += s0._2a;

    if(s0._2c >= 12) {
      FUN_80027eb4(a0);
      s0._00 = 4;
      s0._2c = 0;
      s0._36--;
    }

    //LAB_80028894
  }

  @Method(0x800288a4L)
  public static void FUN_800288a4(final int textboxIndex) {
    if((tickCount_800bb0fc.get() & 0x1) == 0) {
      final Struct84 s0 = _800bdf38[textboxIndex];

      s0._2c += s0._2a & 0x7;

      if(s0._2c >= 12) {
        FUN_80027eb4(textboxIndex);
        s0._08 |= 0x4;
        s0._2c -= 12;
        s0._36 = s0.lines_1e;
      }
    }

    //LAB_80028928
  }

  /** Calculates a good position to place a textbox for a specific sobj */
  @Method(0x80028938L)
  public static void positionSobjTextbox(final int textboxIndex, final int sobjIndex) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    final Struct84 s0 = _800bdf38[textboxIndex];

    FUN_800e2428(sobjIndex);
    final long struct = playerPos_800c68e8.getAddress();
    final int s4 = (int)MEMORY.ref(4, struct).offset(0x70L).get();
    textbox._28 = s4;
    final int sp10 = ((int)MEMORY.ref(4, struct).offset(0x74L).getSigned() - (int)MEMORY.ref(4, struct).offset(0x7cL).getSigned()) / 2;
    final int sp18 = (int)MEMORY.ref(4, struct).offset(0x74L).getSigned() - sp10;
    textbox._2c = sp18;
    final int sp14 = textbox._28 - (int)MEMORY.ref(4, struct).offset(0x68L).get();
    final int textWidth = textbox.chars_18 * 9 / 2;
    final int textHeight = textbox.lines_1a * 6;

    final int width;
    if(mainCallbackIndex_8004dd20.get() != 5) {
      width = 320;
    } else {
      width = 360;
    }

    //LAB_80028a20
    if(s0.chars_1c >= 17) {
      if(sp18 >= 121) {
        //LAB_80028acc
        final int x = width / 2;
        final int y = sp18 - sp10 - textHeight;
        textbox.x_14 = x;
        textbox.y_16 = y;
        textbox._48 = 8;

        s0.x_14 = x;
        s0.y_16 = y;
        s0._18 = s0.x_14 - s0.chars_1c * 9 / 2;
        s0._1a = s0.y_16 - s0.lines_1e * 6;
        return;
      }

      final int x = width / 2;
      final int y = sp18 + sp10 + textHeight;
      textbox.x_14 = x;
      textbox.y_16 = y;
      textbox._48 = 7;

      s0.x_14 = x;
      s0.y_16 = y;
      s0._18 = s0.x_14 - s0.chars_1c * 9 / 2;
      s0._1a = s0.y_16 - s0.lines_1e * 6;
      return;
    }

    //LAB_80028b38
    int y = sp18 - sp10 - textHeight;
    if(textboxFits(textboxIndex, (short)s4, (short)y)) {
      textbox.x_14 = s4;
      textbox.y_16 = y;
      textbox._48 = 0;

      s0.x_14 = s4;
      s0.y_16 = y;
      s0._18 = s0.x_14 - s0.chars_1c * 9 / 2;
      s0._1a = s0.y_16 - s0.lines_1e * 6;
      return;
    }

    //LAB_80028bc4
    y = sp18 + sp10 + textHeight;
    if(textboxFits(textboxIndex, (short)s4, (short)y)) {
      textbox.x_14 = s4;
      textbox.y_16 = y;
      textbox._48 = 1;

      s0.x_14 = s4;
      s0.y_16 = y;
      s0._18 = s0.x_14 - s0.chars_1c * 9 / 2;
      s0._1a = s0.y_16 - s0.lines_1e * 6;
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

        s0.x_14 = s2;
        s0.y_16 = y;
        s0._18 = s0.x_14 - s0.chars_1c * 9 / 2;
        s0._1a = s0.y_16 - s0.lines_1e * 6;
        return;
      }

      //LAB_80028df0
      y = sp18 + sp10 + textHeight / 2;
      if(textboxFits(textboxIndex, (short)s2, (short)y)) {
        textbox.x_14 = s2;
        textbox.y_16 = y;
        textbox._48 = 5;

        s0.x_14 = s2;
        s0.y_16 = y;
        s0._18 = s0.x_14 - s0.chars_1c * 9 / 2;
        s0._1a = s0.y_16 - s0.lines_1e * 6;
        return;
      }
    } else {
      final int s2 = s4 + sp14 + textWidth;
      y = sp18 - sp10 - textHeight / 2;
      if(textboxFits(textboxIndex, (short)s2, (short)y)) {
        textbox.x_14 = s2;
        textbox.y_16 = y;
        textbox._48 = 2;

        s0.x_14 = s2;
        s0.y_16 = y;
        s0._18 = s0.x_14 - s0.chars_1c * 9 / 2;
        s0._1a = s0.y_16 - s0.lines_1e * 6;
        return;
      }

      //LAB_80028ce4
      y = sp18 + sp10 + textHeight / 2;
      if(textboxFits(textboxIndex, (short)s2, (short)y)) {
        textbox.x_14 = s2;
        textbox.y_16 = y;
        textbox._48 = 3;

        s0.x_14 = s2;
        s0.y_16 = y;
        s0._18 = s0.x_14 - s0.chars_1c * 9 / 2;
        s0._1a = s0.y_16 - s0.lines_1e * 6;
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
    FUN_80027d74(textboxIndex, (short)x, (short)(sp18 + sp10 + textHeight));
    textboxes_800be358[textboxIndex]._48 = 6;

    //LAB_80028ef0
  }

  @Method(0x80028f20L)
  public static boolean textboxFits(final int textboxIndex, final int x, final int y) {
    final int maxX;
    if(mainCallbackIndex_8004dd20.get() == 5) {
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

  @Method(0x80028ff8L)
  public static FlowControl FUN_80028ff8(final RunningScript<?> script) {
    clearTextbox(0);

    final Textbox4c struct4c = textboxes_800be358[0];
    struct4c._04 = (int)_80052b8c.get();
    struct4c.x_14 = 260;
    struct4c.y_16 = 120;
    struct4c.chars_18 = 6;
    struct4c.lines_1a = 8;
    FUN_800258a8(0);

    final Struct84 struct84 = _800bdf38[0];
    struct84.type_04 = (int)_80052baa.get();
    struct84.str_24 = _80052c20;
    struct84._08 |= 0x40;

    long addr = mallocHead(struct84.chars_1c * (struct84.lines_1e + 1) * 8);
    struct84.ptr_58 = addr;
    //LAB_800290cc
    for(int i = 0; i < struct84.chars_1c * struct84.lines_1e + 1; i++) {
      MEMORY.ref(2, addr).offset(0x0L).setu(0);
      MEMORY.ref(2, addr).offset(0x2L).setu(0);
      MEMORY.ref(1, addr).offset(0x4L).setu(0);
      MEMORY.ref(2, addr).offset(0x6L).setu(0);
      addr = addr + 0x8L;
    }

    //LAB_80029100
    FUN_80027d74(0, struct84.x_14, struct84.y_16);
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

  @Method(0x80029300L)
  public static void renderText(final LodString text, final int x, int y, final int a3, final int a4) {
    //LAB_80029358
    int length;
    for(length = 0; ; length++) {
      final int c = text.charAt(length);

      if(c == 0xa0ff) {
        currentText_800bdca0.charAt(length, 0xffff);
        break;
      }

      //LAB_80029374
      currentText_800bdca0.charAt(length, c);

      //LAB_80029384
    }

    final int s7 = MathHelper.clamp(a4, -12, 12);

    //LAB_800293bc
    //LAB_800293d8
    int lineIndex = 0;
    int glyphNudge = 0;

    for(int i = 0; i < length; i++) {
      final int c = currentText_800bdca0.charAt(i);

      if(c == 0xa1ff) {
        lineIndex = 0;
        glyphNudge = 0;
        y += 12;
      } else {
        //LAB_80029404
        if(c < 0x340) {
          //LAB_8002945c
          _800be5b8.setu(c / 208);
          _800be5bc.setu(0);
          _800be5c0.setu(c & 0xfL);
          _800be5c8.setu(c % 208 / 16);
        } else {
          //LAB_8002946c
          final long a0_0 = (c - 832) / 16;

          //LAB_80029480
          _800be5b8.setu(a0_0 % 4);
          _800be5bc.setu(a0_0 / 4 + 1);
          _800be5c0.setu(c & 0xf);
          _800be5c8.setu(0);
        }

        //LAB_800294b4
        final int fp = (int)_800be5bc.get();

        if(lineIndex == 0) {
          glyphNudge = 0;
        }

        //LAB_80029504
        //LAB_80029534
        if(c == 0x45) {
          glyphNudge -= 1;
        } else if(c == 0x2) {
          //LAB_80029548
          glyphNudge -= 2;
        } else if(c >= 0x5 && c < 0x7) {
          //LAB_80029550
          glyphNudge -= 3;
        }

        //LAB_80029554
        //LAB_80029558
        //LAB_800295d8
        final int v1 = (int)_800be5c8.get() * 12 + (fp < 6 ? 0 : 240);

        //LAB_80029618
        final int v = s7 >= 0 ? v1 : v1 - s7;
        final int h = s7 >= 0 ? 12 - s7 : 12 + s7;

        GPU.queueCommand(textZ_800bdf00.get(), new GpuCommandQuad()
          .bpp(Bpp.BITS_4)
          .monochrome(0x80)
          .pos(x + lineIndex * 8 - centreScreenX_1f8003dc.get() - glyphNudge, y - centreScreenY_1f8003de.get(), 8, h)
          .uv((int)_800be5c0.get() * 16, v)
          .clut((a3 & 0xf) * 16 + 832 & 0x3f0, (int)_800be5b8.get() + 480)
          .vramPos(textboxVramX_80052bc8.get(fp).get(), textboxVramY_80052bf4.get(fp).get() < 256 ? 0 : 256)
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

      //LAB_80029760
    }

    //LAB_80029770
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
      final Struct84 s2 = _800bdf38[textboxIndex];
      if((s2._08 & 0x1000) != 0) {
        final int left = arrow.x_04 - centreScreenX_1f8003dc.get() - 8;
        final int right = arrow.x_04 - centreScreenX_1f8003dc.get() + 8;
        final int top = arrow.y_06 - centreScreenY_1f8003de.get() - 6;
        final int bottom = arrow.y_06 - centreScreenY_1f8003de.get() + 8;
        final int leftU = 64 + arrow.spriteIndex_08 * 16;
        final int rightU = 80 + arrow.spriteIndex_08 * 16;

        GPU.queueCommand(s2.z_0c, new GpuCommandPoly(4)
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

  @Method(0x80029b68L)
  public static FlowControl FUN_80029b68(final RunningScript<?> script) {
    //LAB_80029b7c
    for(int i = 0; i < 8; i++) {
      if(textboxes_800be358[i]._00 == 0 && _800bdf38[i]._00 == 0) {
        script.params_20[0].set(i);
        return FlowControl.CONTINUE;
      }

      //LAB_80029bac
      //LAB_80029bb0
    }

    script.params_20[0].set(-1);
    return FlowControl.CONTINUE;
  }

  @Method(0x80029bd4L)
  public static FlowControl FUN_80029bd4(final RunningScript<?> script) {
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

  @Method(0x80029c98L)
  public static FlowControl FUN_80029c98(final RunningScript<?> script) {
    final int textboxIndex = script.params_20[0].get();
    script.params_20[1].set(textboxes_800be358[textboxIndex]._00 | _800bdf38[textboxIndex]._00);
    return FlowControl.CONTINUE;
  }

  @Method(0x80029cf4L)
  public static FlowControl FUN_80029cf4(final RunningScript<?> script) {
    script.params_20[1].set(textboxes_800be358[script.params_20[0].get()]._00);
    return FlowControl.CONTINUE;
  }

  @Method(0x80029d34L)
  public static FlowControl FUN_80029d34(final RunningScript<?> script) {
    script.params_20[1].set(_800bdf38[script.params_20[0].get()]._00);
    return FlowControl.CONTINUE;
  }

  @Method(0x80029d6cL)
  public static FlowControl FUN_80029d6c(final RunningScript<?> script) {
    final int s1 = script.params_20[0].get();
    final Struct84 struct84 = _800bdf38[s1];

    if(struct84._00 != 0) {
      free(struct84.ptr_58);
    }

    //LAB_80029db8
    struct84._00 = 0;
    textboxes_800be358[s1]._00 = 0;
    setTextboxArrowPosition(s1, 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x80029e04L)
  public static FlowControl FUN_80029e04(final RunningScript<?> script) {
    //LAB_80029e2c
    for(int i = 0; i < 8; i++) {
      final Textbox4c s2 = textboxes_800be358[i];
      final Struct84 s0 = _800bdf38[i];

      if(s0._00 != 0) {
        free(s0.ptr_58);
      }

      //LAB_80029e48
      s0._00 = 0;
      s2._00 = 0;
      setTextboxArrowPosition(i, 0);
    }

    return FlowControl.CONTINUE;
  }

  @Method(0x80029e8cL)
  public static FlowControl FUN_80029e8c(final RunningScript<?> script) {
    _800bdf10.offset(Math.min(9, script.params_20[0].get()) * 0x4L).setu(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80029eccL)
  public static FlowControl FUN_80029ecc(final RunningScript<?> script) {
    final Struct84 v1 = _800bdf38[script.params_20[0].get()];
    if(v1._00 == 16 && (v1._08 & 0x20) != 0) {
      v1._08 ^= 0x20;
    }

    //LAB_80029f18
    //LAB_80029f1c
    v1._08 |= 0x40;
    return FlowControl.CONTINUE;
  }

  @Method(0x80029f48L)
  public static FlowControl FUN_80029f48(final RunningScript<?> script) {
    script.params_20[1].set(_800bdf38[script.params_20[0].get()]._6c);
    return FlowControl.CONTINUE;
  }

  @Method(0x80029f80L)
  public static FlowControl FUN_80029f80(final RunningScript<?> script) {
    script.params_20[1].set(_800bdf38[script.params_20[0].get()]._7c);
    return FlowControl.CONTINUE;
  }

  @Method(0x8002a058L)
  public static void FUN_8002a058() {
    //LAB_8002a080
    for(int i = 0; i < 8; i++) {
      if(textboxes_800be358[i]._00 != 0) {
        FUN_80025a04(i);
      }

      //LAB_8002a098
      if(_800bdf38[i]._00 != 0) {
        FUN_800264b0(i); // Animates the textbox arrow
      }
    }

    FUN_80024994();
  }

  @Method(0x8002a0e4L)
  public static void renderTextboxes() {
    //LAB_8002a10c
    for(int i = 0; i < 8; i++) {
      final Textbox4c struct4c = textboxes_800be358[i];

      if(struct4c._00 != 0 && struct4c._08 < 0) {
        renderTextboxBackground(i);
      }

      //LAB_8002a134
      if(_800bdf38[i]._00 != 0) {
        renderTextboxText(i);
        renderTextboxArrow(i);
      }

      //LAB_8002a154
    }
  }

  @Method(0x8002a180L)
  public static void FUN_8002a180(final int textboxIndex, final long a1, final long a2, long a3, final long lodChar) {
    final Struct84 v1 = _800bdf38[textboxIndex];
    final int a0 = v1._36 * v1.chars_1c + v1._34;
    final long v0 = v1.ptr_58 + a0 * 0x8L;
    MEMORY.ref(2, v0).offset(0x0L).setu(a1);
    MEMORY.ref(2, v0).offset(0x2L).setu(a2);

    if((v1._08 & 0x200) != 0 && (short)a2 == 0) {
      a3 = 0x8L;
    }

    //LAB_8002a1e8
    //LAB_8002a1ec
    MEMORY.ref(1, v0).offset(0x4L).setu(a3);

    // Hellena Prison has a retail bug (textbox name says Warden?iate)
    if(lodChar == 0x900) {
      MEMORY.ref(2, v0).offset(0x6L).setu(0);
    } else {
      MEMORY.ref(2, v0).offset(0x6L).setu(lodChar);
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
  public static void FUN_8002a2b4(final int textboxIndex) {
    final Struct84 a2 = _800bdf38[textboxIndex];
    long a0_0 = a2.ptr_58;

    //LAB_8002a2f0
    for(int a1 = 0; a1 < a2.chars_1c * (a2.lines_1e + 1); a1++) {
      MEMORY.ref(2, a0_0).offset(0x0L).setu(0);
      MEMORY.ref(2, a0_0).offset(0x2L).setu(0);
      MEMORY.ref(1, a0_0).offset(0x4L).setu(0);
      MEMORY.ref(2, a0_0).offset(0x6L).setu(0);
      a0_0 = a0_0 + 0x8L;
    }

    //LAB_8002a324
  }

  @Method(0x8002a32cL)
  public static void FUN_8002a32c(final int textboxIndex, final int a1, final int x, final int y, final int chars, final int lines) {
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
  public static void FUN_8002a3ec(final int textboxIndex, final long a1) {
    if((a1 & 0x1L) == 0) {
      //LAB_8002a40c
      _800bdf38[textboxIndex]._00 = 0;
      textboxes_800be358[textboxIndex]._00 = 0;
    } else {
      //LAB_8002a458
      textboxes_800be358[textboxIndex]._00 = 3;
    }
  }

  @Method(0x8002a488L)
  public static long FUN_8002a488(final int textboxIndex) {
    return textboxes_800be358[textboxIndex]._00 == 6 ? 1 : 0;
  }

  @Method(0x8002a4c4L)
  public static void updateTextboxArrowSprite(final int textboxIndex) {
    final TextboxArrow0c arrow = textboxArrows_800bdea0[textboxIndex];

    if((arrow._00 & 0x1) != 0) {
      if((_800bdf38[textboxIndex]._08 & 0x1000) != 0) {
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
  public static int textWidth(final LodString a0) {
    //LAB_8002a5b4
    int a3 = 0;
    int v1;
    for(v1 = 0; a0.charAt(v1) <= 0x9fffL; v1++) {
      a3 += switch(a0.charAt(v1)) {
        case 0x5, 0x23, 0x24, 0x2a, 0x37, 0x38, 0x3a, 0x3b, 0x3c, 0x3d, 0x3f, 0x40, 0x43, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4d, 0x4e, 0x51, 0x52 -> 1;
        case 0x2, 0x8, 0x3e, 0x4c -> 2;
        case 0xb, 0xc, 0x42 -> 3;
        case 0x1, 0x3, 0x4, 0x9, 0x16, 0x41, 0x44 -> 4;
        case 0x6, 0x27 -> 5;
        default -> 0;
      };

      //LAB_8002a618
      //LAB_8002a624
    }

    return v1 * 8 - (short)a3;
  }

  @Method(0x8002a63cL)
  public static void FUN_8002a63c(final int a0) {
    if(a0 > 0 && a0 < 832) {
      _800be5bc.setu(0);
      _800be5c0.setu(a0 & 0xf);
      _800be5b8.setu(a0 / 208);
      _800be5c8.setu(a0 % 208 / 16);
      return;
    }

    //LAB_8002a6b0
    final int a1 = (a0 - 832) / 16;
    _800be5bc.setu(a1 / 4 + 1);
    _800be5b8.setu(a1 % 4);
    _800be5c0.setu(a0 & 0xf);
    _800be5c8.setu(0);
  }

  @Method(0x8002a6fcL)
  public static void clearCharacterStats() {
    //LAB_8002a730
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);

      stats.xp_00.set(0);
      stats.hp_04.set(0);
      stats.mp_06.set(0);
      stats.sp_08.set(0);
      stats._0a.set(0);
      stats.dragoonFlag_0c.set(0);
      stats.level_0e.set(0);
      stats.dlevel_0f.set(0);

      //LAB_8002a758
      for(int i = 0; i < 5; i++) {
        stats.equipment_30.get(i).set(0xff);
      }

      stats.selectedAddition_35.set(0);

      //LAB_8002a780;
      for(int i = 0; i < 8; i++) {
        stats.additionLevels_36.get(i).set(0);
        stats.additionXp_3e.get(i).set(0);
      }

      stats.physicalImmunity_46.set(0);
      stats.magicalImmunity_48.set(0);
      stats.physicalResistance_4a.set(0);
      stats.spMultiplier_4c.set((short)0);
      stats.spPerPhysicalHit_4e.set((short)0);
      stats.mpPerPhysicalHit_50.set((short)0);
      stats.spPerMagicalHit_52.set((short)0);
      stats.mpPerMagicalHit_54.set((short)0);
      stats._56.set((short)0);
      stats.hpRegen_58.set((short)0);
      stats.mpRegen_5a.set((short)0);
      stats.spRegen_5c.set((short)0);
      stats.revive_5e.set((short)0);
      stats.magicalResistance_60.set(0);
      stats.hpMulti_62.set((short)0);
      stats.mpMulti_64.set((short)0);
      stats.maxHp_66.set(0);
      stats.addition_68.set(0);
      stats.bodySpeed_69.set(0);
      stats.bodyAttack_6a.set(0);
      stats.bodyMagicAttack_6b.set(0);
      stats.bodyDefence_6c.set(0);
      stats.bodyMagicDefence_6d.set(0);
      stats.maxMp_6e.set(0);
      stats.spellIndex_70.set(0);
      stats._71.set(0);
      stats.dragoonAttack_72.set(0);
      stats.dragoonMagicAttack_73.set(0);
      stats.dragoonDefence_74.set(0);
      stats.dragoonMagicDefence_75.set(0);

      FUN_8002a86c(charIndex);

      stats._9c.set(0);
      stats.additionSpMultiplier_9e.set(0);
      stats.additionDamageMultiplier_9f.set(0);
    }

    FUN_8002a8f8();
    _800be5d0.setu(0);
  }

  @Method(0x8002a86cL)
  public static void FUN_8002a86c(final int charIndex) {
    final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);

    stats.specialEffectFlag_76.set(0);
    stats._77.set(0);
    stats._78.set(0);
    stats._79.set(0);
    stats.elementFlag_7a.set(0);
    stats._7b.set(0);
    stats.elementalResistanceFlag_7c.set(0);
    stats.elementalImmunityFlag_7d.set(0);
    stats.statusResistFlag_7e.set(0);
    stats._7f.set(0);
    stats._80.set(0);
    stats._81.set(0);
    stats._82.set(0);
    stats._83.set(0);
    stats._84.set(0);

    stats.gearSpeed_86.set((short)0);
    stats.gearAttack_88.set((short)0);
    stats.gearMagicAttack_8a.set((short)0);
    stats.gearDefence_8c.set((short)0);
    stats.gearMagicDefence_8e.set((short)0);
    stats.attackHit_90.set((short)0);
    stats.magicHit_92.set((short)0);
    stats.attackAvoid_94.set((short)0);
    stats.magicAvoid_96.set((short)0);
    stats.onHitStatusChance_98.set(0);
    stats._99.set(0);
    stats._9a.set(0);
    stats.onHitStatus_9b.set(0);
  }

  @Method(0x8002a8f8L)
  public static void FUN_8002a8f8() {
    bzero(equipmentStats_800be5d8.getAddress(), 0x1c);
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
    MEMORY.memfill(_800beb98.getAddress(), 0x190, 0);
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
        FUN_800e519c();
        FUN_800e8e50();
        FUN_800e828c();
        FUN_800e4f8c();
        unloadSmap();

        _80052c44.setu(0x2L);
        break;

      case 0x4:
        FUN_800e519c();

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
        FUN_800e519c();
        break;
    }

    //caseD_6
    if(_800f7e54.get(0x1L) == 0) {
      // If an encounter should start
      if(handleEncounters() != 0) {
        FUN_800e5534(-1, 0);
      }
    }

    //LAB_8002abdc
    //LAB_8002abe0
    final int a0 = FUN_800e6730(index_80052c38.get());
    if((a0 & 0x10) != 0) {
      FUN_800e5534(a0 >>> 22, a0 >>> 16 & 0x3f);
    }

    //LAB_8002ac10
    //LAB_8002ac14
    return s0;
  }

  @Method(0x8002bb38L)
  public static void FUN_8002bb38(final int joypadIndex, final long a1) {
    if(gameState_800babc8.vibrationEnabled_4e1.get() == 0) {
      return;
    }

    LOGGER.info("Rumble 8002bb38 %x %x", joypadIndex, a1);
  }

  @Method(0x8002bcc8L)
  public static void FUN_8002bcc8(final long a0, final long a1) {
    if(gameState_800babc8.vibrationEnabled_4e1.get() == 0) {
      return;
    }

    LOGGER.info("Rumble 8002bcc8 %x %x", a0, a1);
  }

  @Method(0x8002bda4L)
  public static void FUN_8002bda4(final long a0, final long a1, final long a2) {
    if(gameState_800babc8.vibrationEnabled_4e1.get() == 0) {
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

  @Method(0x8002c904L)
  public static void setCdMix(final int volume) {
    if(gameState_800babc8.mono_4e0.get() == 0) {
      //LAB_8002c95c
      CdMix(volume, 0, volume, 0);
    } else {
      final int mixedVol = volume * 70 / 100;
      CdMix(mixedVol, mixedVol, mixedVol, mixedVol);
    }
  }

  @Method(0x8002c984L)
  public static long playXaAudio(final int xaLoadingStage, final int xaArchiveIndex, final int xaFileIndex) {
    //LAB_8002c9f0
    if(xaFileIndex == 0 || xaFileIndex >= 32 || xaLoadingStage >= 5) {
      return 0;
    }

    if(xaLoadingStage == 3) {
      LOGGER.info("Playing XA archive %d file %d", xaArchiveIndex, xaFileIndex);

      setCdVolume(0x7f, 0x7f);
      setCdMix(0x3f);

      final long v1;
      if(drgnBinIndex_800bc058.get() == 0x4L) {
        v1 = lodXa00Xa_80052c94.getAddress();
      } else {
        //LAB_8002c438
        v1 = lodXa00Xa_80052c74.getAddress();
      }

      //LAB_8002c448
      LOGGER.info("PLAY XA %d", MEMORY.ref(2, v1).offset(xaArchiveIndex * 0x8L).getSigned());
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
    bzero(_8005a1d8.getAddress(), 0x6c4b0);
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
