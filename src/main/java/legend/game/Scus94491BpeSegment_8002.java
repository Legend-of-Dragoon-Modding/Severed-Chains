package legend.game;

import legend.core.Hardware;
import legend.core.MathHelper;
import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.CdlLOC;
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
import legend.core.kernel.Bios;
import legend.core.kernel.Kernel;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.tmd.Renderer;
import legend.game.types.ActiveStatsa0;
import legend.game.types.Model124;
import legend.game.types.CharacterData2c;
import legend.game.types.DR_MOVE;
import legend.game.types.Drgn0_6666Entry;
import legend.game.types.Drgn0_6666Struct;
import legend.game.types.ExtendedTmd;
import legend.game.types.FileEntry08;
import legend.game.types.GameState52c;
import legend.game.types.InventoryMenuState;
import legend.game.types.ItemStats0c;
import legend.game.types.LodString;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuItemStruct04;
import legend.game.types.ModelPartTransforms;
import legend.game.types.MrgEntry;
import legend.game.types.MrgFile;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.RunningScript;
import legend.game.types.SpuStruct28;
import legend.game.types.Struct4c;
import legend.game.types.Struct84;
import legend.game.types.TexPageBpp;
import legend.game.types.TexPageTrans;
import legend.game.types.TexPageY;
import legend.game.types.TmdAnimationFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.Hardware.CDROM;
import static legend.core.Hardware.CPU;
import static legend.core.Hardware.GATE;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getBiFunctionAddress;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.core.Timers.TMR_DOTCLOCK_VAL;
import static legend.game.SInit.initFileEntries;
import static legend.game.SItem.FUN_800fcad4;
import static legend.game.SItem.FUN_8010a948;
import static legend.game.SItem.FUN_8010d614;
import static legend.game.SItem.FUN_8010f198;
import static legend.game.SItem.equipmentStats_80111ff0;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SMap.FUN_800d9e64;
import static legend.game.SMap.FUN_800da114;
import static legend.game.SMap.FUN_800da524;
import static legend.game.SMap.renderSmapModel;
import static legend.game.SMap.FUN_800de004;
import static legend.game.SMap.FUN_800e2220;
import static legend.game.SMap.FUN_800e2428;
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
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001ae90;
import static legend.game.Scus94491BpeSegment.FUN_8001e010;
import static legend.game.Scus94491BpeSegment._80010868;
import static legend.game.Scus94491BpeSegment._800108b0;
import static legend.game.Scus94491BpeSegment.mallocHead;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.fillMemory;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.queueGpuPacket;
import static legend.game.Scus94491BpeSegment.gpuPacketAddr_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadAndRunOverlay;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.qsort;
import static legend.game.Scus94491BpeSegment.rectArray28_80010770;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment_8003.CdMix;
import static legend.game.Scus94491BpeSegment_8003.DsSearchFile;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003fd80;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrix;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMove;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.TransposeMatrix;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setGp0_28;
import static legend.game.Scus94491BpeSegment_8003.setGp0_2c;
import static legend.game.Scus94491BpeSegment_8003.setGp0_38;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c390;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d034;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixX;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixY;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixZ;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004.fileLoadingCallbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8004.loadingSmapOvl_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
import static legend.game.Scus94491BpeSegment_8005._8005039c;
import static legend.game.Scus94491BpeSegment_8005._800503b0;
import static legend.game.Scus94491BpeSegment_8005._800503d4;
import static legend.game.Scus94491BpeSegment_8005._800503f8;
import static legend.game.Scus94491BpeSegment_8005._80050424;
import static legend.game.Scus94491BpeSegment_8005._80052ae0;
import static legend.game.Scus94491BpeSegment_8005._80052b40;
import static legend.game.Scus94491BpeSegment_8005._80052b68;
import static legend.game.Scus94491BpeSegment_8005._80052b88;
import static legend.game.Scus94491BpeSegment_8005._80052b8c;
import static legend.game.Scus94491BpeSegment_8005._80052ba8;
import static legend.game.Scus94491BpeSegment_8005._80052baa;
import static legend.game.Scus94491BpeSegment_8005._80052bc8;
import static legend.game.Scus94491BpeSegment_8005._80052bf4;
import static legend.game.Scus94491BpeSegment_8005._80052c20;
import static legend.game.Scus94491BpeSegment_8005._80052c34;
import static legend.game.Scus94491BpeSegment_8005._80052c3c;
import static legend.game.Scus94491BpeSegment_8005._80052c40;
import static legend.game.Scus94491BpeSegment_8005._80052c44;
import static legend.game.Scus94491BpeSegment_8005._80052c4c;
import static legend.game.Scus94491BpeSegment_8005._80052dbc;
import static legend.game.Scus94491BpeSegment_8005._80052dc0;
import static legend.game.Scus94491BpeSegment_8005._8005a1d8;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.lodXa00Xa_80052c74;
import static legend.game.Scus94491BpeSegment_8005.lodXa00Xa_80052c94;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.joypadRepeat_8007a3a0;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.CdlFILE_800bb4c8;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bd610;
import static legend.game.Scus94491BpeSegment_800b._800bd614;
import static legend.game.Scus94491BpeSegment_800b._800bd61c;
import static legend.game.Scus94491BpeSegment_800b._800bd7a0;
import static legend.game.Scus94491BpeSegment_800b._800bd7a4;
import static legend.game.Scus94491BpeSegment_800b._800bd7a8;
import static legend.game.Scus94491BpeSegment_800b._800bd7ac;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b._800bd7b4;
import static legend.game.Scus94491BpeSegment_800b._800bd7b8;
import static legend.game.Scus94491BpeSegment_800b._800bd80c;
import static legend.game.Scus94491BpeSegment_800b._800bdb88;
import static legend.game.Scus94491BpeSegment_800b._800bdb9c;
import static legend.game.Scus94491BpeSegment_800b._800bdba0;
import static legend.game.Scus94491BpeSegment_800b._800bdc58;
import static legend.game.Scus94491BpeSegment_800b._800bdea0;
import static legend.game.Scus94491BpeSegment_800b._800bdf00;
import static legend.game.Scus94491BpeSegment_800b._800bdf04;
import static legend.game.Scus94491BpeSegment_800b._800bdf08;
import static legend.game.Scus94491BpeSegment_800b._800bdf10;
import static legend.game.Scus94491BpeSegment_800b._800bdf18;
import static legend.game.Scus94491BpeSegment_800b._800bdf38;
import static legend.game.Scus94491BpeSegment_800b._800be358;
import static legend.game.Scus94491BpeSegment_800b._800be5b8;
import static legend.game.Scus94491BpeSegment_800b._800be5bc;
import static legend.game.Scus94491BpeSegment_800b._800be5c0;
import static legend.game.Scus94491BpeSegment_800b._800be5c4;
import static legend.game.Scus94491BpeSegment_800b._800be5c8;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b._800beb98;
import static legend.game.Scus94491BpeSegment_800b._800bed28;
import static legend.game.Scus94491BpeSegment_800b._800bf0c0;
import static legend.game.Scus94491BpeSegment_800b._800bf0c4;
import static legend.game.Scus94491BpeSegment_800b._800bf0c8;
import static legend.game.Scus94491BpeSegment_800b._800bf0cc;
import static legend.game.Scus94491BpeSegment_800b._800bf0cd;
import static legend.game.Scus94491BpeSegment_800b._800bf0ce;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b._800bf0d8;
import static legend.game.Scus94491BpeSegment_800b.currentText_800bdca0;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.equipmentStats_800be5d8;
import static legend.game.Scus94491BpeSegment_800b.fileLoadingInfoArray_800bbad8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.hasNoEncounters_800bed58;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdbe8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdbec;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdbf0;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc20;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.scriptsDisabled_800bc0b9;
import static legend.game.Scus94491BpeSegment_800b.selectedMenuOptionRenderablePtr_800bdbe0;
import static legend.game.Scus94491BpeSegment_800b.selectedMenuOptionRenderablePtr_800bdbe4;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.texPages_800bb110;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c._800c6688;
import static legend.game.Scus94491BpeSegment_800e.main;
import static legend.game.Scus94491BpeSegment_800e.ramSize_800e6f04;
import static legend.game.Scus94491BpeSegment_800e.stackSize_800e6f08;
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
    unloadSoundFile(7);
    FUN_800201c8(0x6L);
  }

  @Method(0x80020060L)
  public static long FUN_80020060(final RunningScript a0) {
    FUN_8001ad18();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
    unloadSoundFile(8);
    FUN_800201c8(6);
    return 0;
  }

  @Method(0x800201c8L)
  public static void FUN_800201c8(final long a0) {
    if(_800bd610.offset(a0 * 16).get() != 0) {
      FUN_8004d034((int)_800bd61c.offset(a0 * 16).get(), 0x1L);
      FUN_8004c390((int)_800bd61c.offset(a0 * 16).get());
      free(_800bd614.offset(a0 * 16).get());
      _800bd610.offset(a0 * 16).setu(0);
    }

    //LAB_80020220
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

      if(a1_0._00.get() == 4) {
        if(a1_0._1c.get() != 0) {
          a1_0._00.set(3);
        }
      }

      //LAB_800203d8
    }
  }

  @Method(0x800203f0L)
  public static long FUN_800203f0(final RunningScript script) {
    unloadSoundFile(3);
    //TODO GH#3
//    loadedDrgnFiles_800bcf78.oru(0x10L);
//    loadDrgnBinFile(0, 1290 + script.params_20.get(0).deref().get(), 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001d51c", long.class, long.class, long.class), 0, 0x4L);
    return 0;
  }

  @Method(0x80020460L)
  public static void FUN_80020460() {
    // empty
  }

  @Method(0x80020468L)
  public static void FUN_80020468(final GsDOBJ2 dobj2, final long a1) {
    final TmdObjTable objTable = dobj2.tmd_08.deref();
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
        FUN_800210c4(primitives, MEMORY.ref(2, primitives).get(), a1);
        count -= primitiveCount;
        primitives += primitiveCount * 0x24L;
      } else if(command == 0x3d00_0000L || command == 0x3f00_0000L) {
        FUN_8002117c(primitives, MEMORY.ref(2, primitives).get(), a1);
        count -= primitiveCount;
        primitives += primitiveCount * 0x2cL;
      } else if(command == 0x3804_0000L || command == 0x3a04_0000L) {
        FUN_80021060(primitives, MEMORY.ref(2, primitives).get());
        count -= primitiveCount;
        primitives += primitiveCount * 0x24L;
      } else if(command == 0x3800_0000L || command == 0x3a00_0000L) {
        FUN_80021050(primitives, MEMORY.ref(2, primitives).get());
        count -= primitiveCount;
        primitives += primitiveCount * 0x18L;
      } else if(command == 0x3500_0000L || command == 0x3700_0000L) {
        FUN_80021120(primitives, MEMORY.ref(2, primitives).get(), a1);
        count -= primitiveCount;
        primitives += primitiveCount * 0x24L;
      } else if(command == 0x3400_0000L || command == 0x3600_0000L) {
        FUN_80021068(primitives, MEMORY.ref(2, primitives).get(), a1);
        count -= primitiveCount;
        primitives += primitiveCount * 0x1cL;
      } else if(command == 0x3004_0000L || command == 0x3204_0000L) {
        FUN_80021058(primitives, MEMORY.ref(2, primitives).get());
        count -= primitiveCount;
        primitives += primitiveCount * 0x1cL;
      } else if(command == 0x3000_0000L || command == 0x3200_0000L) {
        FUN_80021048(primitives, MEMORY.ref(2, primitives).get());
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
      model.aub_ec.get(i).set(0);
    }

    final Tmd tmd = extendedTmd.tmdPtr_00.deref().tmd;
    model.tmd_8c.set(tmd);
    model.tmdNobj_ca.set((int)tmd.header.nobj.get());

    if(mainCallbackIndex_8004dd20.get() == 0x5L) { // SMAP
      FUN_800de004(model, extendedTmd);
    }

    //LAB_8002079c
    model.ui_108.set((extendedTmd.tmdPtr_00.deref().id.get() & 0xffff_0000L) >>> 11); //TODO reading the upper 16 bits of the TMD ID?

    final long v0 = extendedTmd.ptr_08.get();
    if(v0 == 0) {
      //LAB_80020818
      model.ptr_a8.set(extendedTmd.ptr_08.getAddress()); //TODO

      //LAB_80020828
      for(int i = 0; i < 7; i++) {
        model.aui_d0.get(i).set(0);
      }
    } else {
      model.ptr_a8.set(extendedTmd.getAddress() + v0 / 4 * 4);

      //LAB_800207d4
      for(int i = 0; i < 7; i++) {
        //TODO make aui_d0 array of pointers to unsigned ints (also pointers but to what?)
        //TODO also ui_a8 is a pointer to a relative pointer?
        model.aui_d0.get(i).set(model.ptr_a8.get() + MEMORY.ref(4, model.ptr_a8.get()).offset(i * 0x4L).get() / 4 * 4);
        FUN_8002246c(model, i);
      }
    }

    //LAB_80020838
    adjustTmdPointers(model.tmd_8c.deref());
    initObjTable2(model.ObjTable_0c, model.dobj2ArrPtr_00.deref(), model.coord2ArrPtr_04.deref(), model.coord2ParamArrPtr_08.deref(), model.count_c8.get());
    model.coord2_14.param.set(model.coord2Param_64);
    GsInitCoordinate2(null, model.coord2_14);
    FUN_80021ca0(model.ObjTable_0c, model.tmd_8c.deref(), model.coord2_14, model.count_c8.get(), (short)(model.tmdNobj_ca.get() + 1));

    model.zOffset_a0.set((short)0);
    model.ub_a2.set(0);
    model.ub_a3.set(0);
    model.ui_f4.set(0);
    model.ui_f8.set(0);

    FUN_80021584(model, tmdAnimFile);

    model.coord2_14.coord.transfer.setX(transferX);
    model.coord2_14.coord.transfer.setY(transferY);
    model.coord2_14.coord.transfer.setZ(transferZ);

    int s1 = 0;
    //LAB_80020940
    if(mainCallbackIndex_8004dd20.get() == 0x5L) { // SMAP
      //LAB_80020958
      for(int i = 0; i < model.ObjTable_0c.nobj.get(); i++) {
        FUN_800d9e64(model.ObjTable_0c.top.deref().get(s1++), model.ub_9d.get());
      }

      //LAB_80020978
    } else if(mainCallbackIndex_8004dd20.get() == 0x8L) { // WMAP
      //LAB_80020990
      for(int i = 0; i < model.ObjTable_0c.nobj.get(); i++) {
        FUN_800c8844(model.ObjTable_0c.top.deref().get(s1++), model.ub_9d.get());
      }

      //LAB_800209ac
    } else {
      //LAB_8002091c
      for(int i = 0; i < model.ObjTable_0c.nobj.get(); i++) {
        FUN_80020468(model.ObjTable_0c.top.deref().get(s1++), model.ub_9d.get());
      }
    }

    //LAB_800209b0
    model.b_cc.set(0);
    model.b_cd.set(-2);
    model.scaleVector_fc.set(0x1000, 0x1000, 0x1000);
    model.vector_10c.set(0x1000, 0x1000, 0x1000);
    model.vector_118.set(0, 0, 0);
  }

  @Method(0x80020a00L)
  public static void initModel(final Model124 model, final ExtendedTmd extendedTmd, final TmdAnimationFile tmdAnimFile) {
    model.count_c8.set((short)extendedTmd.tmdPtr_00.deref().tmd.header.nobj.get());

    final long address;
    if(mainCallbackIndex_8004dd20.get() != 0x6L || _800bd7a0.get() == 0) {
      //LAB_80020b00
      //LAB_80020b04
      address = mallocTail(model.count_c8.get() * 0x88L);
      model.dobj2ArrPtr_00.set(MEMORY.ref(4, address, UnboundedArrayRef.of(0x10, GsDOBJ2::new)));
    } else {
      free(_800bd7a0.get());

      address = mallocHead(model.count_c8.get() * 0x88L);
      model.dobj2ArrPtr_00.set(MEMORY.ref(4, address, UnboundedArrayRef.of(0x10, GsDOBJ2::new)));

      _800bd7a8.subu(0x1L);
      _800bd7a4.subu(model.count_c8.get() * 0x88L + _800bd7a8.get() * 0x100L);

      _800bd7a0.setu(mallocHead(_800bd7a4.get()));
    }

    //LAB_80020b40
    model.coord2ArrPtr_04.set(MEMORY.ref(4, address + model.count_c8.get() * 0x10L, UnboundedArrayRef.of(0x50, GsCOORDINATE2::new)));
    model.coord2ParamArrPtr_08.set(MEMORY.ref(4, address + model.count_c8.get() * 0x60L, UnboundedArrayRef.of(0x28, GsCOORD2PARAM::new)));
    FUN_80020718(model, extendedTmd, tmdAnimFile);
  }

  @Method(0x80020b98L)
  public static void animateModel(final Model124 model) {
    if(mainCallbackIndex_8004dd20.get() == 0x5L) { // SMAP
      FUN_800da114(model);
      return;
    }

    //LAB_80020be8
    //LAB_80020bf0
    for(int i = 0; i < 7; i++) {
      if(model.aub_ec.get(i).get() != 0) {
        FUN_80022018(model, i);
      }

      //LAB_80020c08
    }

    if(model.ub_9c.get() == 2) {
      return;
    }

    if(model.s_9e.get() == 0) {
      model.ub_9c.set(0);
    }

    //LAB_80020c3c
    if(model.ub_9c.get() == 0) {
      if(model.ub_a2.get() == 0) {
        model.s_9e.set(model.s_9a.get());
      } else {
        //LAB_80020c68
        model.s_9e.set((short)(model.s_9a.get() >> 1));
      }

      //LAB_80020c7c
      model.ub_9c.incr();
      model.partTransforms_94.set(model.partTransforms_90.deref());
    }

    //LAB_80020c90
    if((model.s_9e.get() & 0x1L) == 0 && model.ub_a2.get() == 0) {
      final UnboundedArrayRef<ModelPartTransforms> transforms = model.partTransforms_94.deref();

      if(model.ub_a3.get() == 0) {
        //LAB_80020ce0
        for(int i = 0; i < model.tmdNobj_ca.get(); i++) {
          final GsCOORDINATE2 coord2 = model.dobj2ArrPtr_00.deref().get(i).coord2_04.deref();
          final GsCOORD2PARAM params = coord2.param.deref();
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
        for(int i = 0; i < model.tmdNobj_ca.get(); i++) {
          final GsCOORDINATE2 coord2 = model.dobj2ArrPtr_00.deref().get(i).coord2_04.deref();
          final GsCOORD2PARAM params = coord2.param.deref();

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
      final UnboundedArrayRef<ModelPartTransforms> transforms = model.partTransforms_94.deref();

      //LAB_80020e24
      for(int i = 0; i < model.tmdNobj_ca.get(); i++) {
        final GsCOORDINATE2 coord2 = model.dobj2ArrPtr_00.deref().get(i).coord2_04.deref();
        final GsCOORD2PARAM params = coord2.param.deref();

        params.rotate.set(transforms.get(i).rotate_00);
        RotMatrix_80040010(params.rotate, coord2.coord);

        params.trans.set(transforms.get(i).translate_06);
        TransMatrix(coord2.coord, params.trans);
      }

      //LAB_80020e94
      model.partTransforms_94.set(transforms.slice(model.tmdNobj_ca.get()));
    }

    //LAB_80020e98
    model.s_9e.decr();

    //LAB_80020ea8
  }

  @Method(0x80020ed8L)
  public static void FUN_80020ed8() {
    if(_800bdb88.get() == 0x5L) {
      if(loadingSmapOvl_8004dd08.get() == 0) {
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
    final long a0 = _800bdb88.get();
    _800bd7b4.setu(0);
    if(a0 != mainCallbackIndex_8004dd20.get()) {
      _800bd80c.setu(a0);
      _800bdb88.setu(mainCallbackIndex_8004dd20);

      if(mainCallbackIndex_8004dd20.get() == 0x5L) {
        _800bd7b0.setu(0x2L);
        _800bd7b8.setu(0);

        if(a0 == 0x2L) {
          _800bd7b0.setu(0x9L);
        }

        //LAB_80020f84
        if(a0 == 0x6L) {
          _800bd7b0.setu(-4L);
          _800bd7b8.setu(0x1L);
        }

        //LAB_80020fa4
        if(a0 == 0x8L) {
          _800bd7b0.setu(0x3L);
        }
      }
    }

    //LAB_80020fb4
    //LAB_80020fb8
    if(_800bdb88.get() == 0x2L) {
      _800bd7ac.setu(0x1L);
    }

    //LAB_80020fd0
  }

  @Method(0x80020fe0L)
  public static void deallocateModel(final Model124 model) {
    if(!model.dobj2ArrPtr_00.isNull()) {
      free(model.dobj2ArrPtr_00.getPointer());
    }

    //LAB_80021008
    if(mainCallbackIndex_8004dd20.get() == 0x5L && !model.smallerStructPtr_a4.isNull()) {
      free(model.smallerStructPtr_a4.getPointer());
    }

    //LAB_80021034
    model.dobj2ArrPtr_00.clear();
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
  public static void FUN_80021068(long a0, long a1, long a2) {
    long v0;
    long v1;
    final long a3;
    if(a1 != 0) {
      v0 = 0x8005_0000L;
      v0 = v0 + 0x27cL;
      v1 = a2 << 4;
      a3 = v1 + v0;
      a2 = a0 + 0x8L;

      //LAB_80021080
      do {
        a1 = a1 - 0x1L;
        v0 = MEMORY.ref(4, a2).offset(-4L).get();
        v1 = MEMORY.ref(4, a3).offset(0xcL).get();
        a0 = MEMORY.ref(4, a3).offset(0x8L).get();
        v0 = v0 & v1;
        v0 = v0 | a0;
        MEMORY.ref(4, a2).offset(-4L).setu(v0);
        v0 = MEMORY.ref(4, a2).offset(0x0L).get();
        v1 = MEMORY.ref(4, a3).offset(0x4L).get();
        a0 = MEMORY.ref(4, a3).offset(0x0L).get();
        v0 = v0 & v1;
        v0 = v0 | a0;
        MEMORY.ref(4, a2).offset(0x0L).setu(v0);
        a2 = a2 + 0x1cL;
      } while(a1 != 0);
    }

    //LAB_800210bc
  }

  @Method(0x800210c4L)
  public static void FUN_800210c4(long a0, long a1, long a2) {
    long v0;
    long v1;
    final long a3;
    if(a1 != 0) {
      v0 = 0x8005_0000L;
      v0 = v0 + 0x27cL;
      v1 = a2 << 4;
      a3 = v1 + v0;
      a2 = a0 + 0x8L;

      //LAB_800210dc
      do {
        a1 = a1 - 0x1L;
        v0 = MEMORY.ref(4, a2).offset(-4L).get();
        v1 = MEMORY.ref(4, a3).offset(0xcL).get();
        a0 = MEMORY.ref(4, a3).offset(0x8L).get();
        v0 = v0 & v1;
        v0 = v0 | a0;
        MEMORY.ref(4, a2).offset(-4L).setu(v0);
        v0 = MEMORY.ref(4, a2).offset(0x0L).get();
        v1 = MEMORY.ref(4, a3).offset(0x4L).get();
        a0 = MEMORY.ref(4, a3).offset(0x0L).get();
        v0 = v0 & v1;
        v0 = v0 | a0;
        MEMORY.ref(4, a2).offset(0x0L).setu(v0);
        a2 = a2 + 0x24L;
      } while(a1 != 0);
    }

    //LAB_80021118
  }

  @Method(0x8002117cL)
  public static void FUN_8002117c(long a0, long a1, long a2) {
    long v0;
    long v1;
    final long a3;
    if(a1 != 0) {
      v0 = 0x8005_0000L;
      v0 = v0 + 0x27cL;
      v1 = a2 << 4;
      a3 = v1 + v0;
      a2 = a0 + 0x8L;

      //LAB_80021194
      do {
        a1 = a1 - 0x1L;
        v0 = MEMORY.ref(4, a2).offset(-4L).get();
        v1 = MEMORY.ref(4, a3).offset(0xcL).get();
        a0 = MEMORY.ref(4, a3).offset(0x8L).get();
        v0 = v0 & v1;
        v0 = v0 | a0;
        MEMORY.ref(4, a2).offset(-4L).setu(v0);
        v0 = MEMORY.ref(4, a2).offset(0x0L).get();
        v1 = MEMORY.ref(4, a3).offset(0x4L).get();
        a0 = MEMORY.ref(4, a3).offset(0x0L).get();
        v0 = v0 & v1;
        v0 = v0 | a0;
        MEMORY.ref(4, a2).offset(0x0L).setu(v0);
        a2 = a2 + 0x2cL;
      } while(a1 != 0);
    }

    //LAB_800211d0
  }

  @Method(0x80021120L)
  public static void FUN_80021120(long a0, long a1, long a2) {
    long v0;
    long v1;
    final long a3;
    if(a1 != 0) {
      v0 = 0x8005_0000L;
      v0 = v0 + 0x27cL;
      v1 = a2 << 4;
      a3 = v1 + v0;
      a2 = a0 + 0x8L;

      //LAB_80021138
      do {
        a1 = a1 - 0x1L;
        v0 = MEMORY.ref(4, a2).offset(-4L).get();
        v1 = MEMORY.ref(4, a3).offset(0xcL).get();
        a0 = MEMORY.ref(4, a3).offset(0x8L).get();
        v0 = v0 & v1;
        v0 = v0 | a0;
        MEMORY.ref(4, a2).offset(-4L).setu(v0);
        v0 = MEMORY.ref(4, a2).offset(0x0L).get();
        v1 = MEMORY.ref(4, a3).offset(0x4L).get();
        a0 = MEMORY.ref(4, a3).offset(0x0L).get();
        v0 = v0 & v1;
        v0 = v0 | a0;
        MEMORY.ref(4, a2).offset(0x0L).setu(v0);
        a2 = a2 + 0x24L;
      } while(a1 != 0);
    }

    //LAB_80021174
  }

  @Method(0x800211d8L)
  public static void renderModel(final Model124 model) {
    if(mainCallbackIndex_8004dd20.get() == 0x5L) {
      //LAB_80021230
      renderSmapModel(model);
    } else if(mainCallbackIndex_8004dd20.get() == 0x6L) {
      //LAB_80021220
      renderBttlModel(model);
    } else if(mainCallbackIndex_8004dd20.get() == 0x8L) {
      //LAB_8002120c
      //LAB_80021240
      renderWmapModel(model);
    }

    //LAB_80021248
  }

  @Method(0x80021258L)
  public static void renderDobj2(final GsDOBJ2 dobj2) {
    if(mainCallbackIndex_8004dd20.get() == 0x5L) {
      //LAB_800212b0
      Renderer.renderDobj2(dobj2, false);
      return;
    }

    if(mainCallbackIndex_8004dd20.get() == 0x6L) {
      //LAB_800212a0
      Renderer.renderDobj2(dobj2, true);
      return;
    }

    //LAB_8002128c
    if(mainCallbackIndex_8004dd20.get() == 0x8L) {
      //LAB_800212c0
      Renderer.renderDobj2(dobj2, false);
    }

    //LAB_800212c8
  }

  @Method(0x800212d8L)
  public static void applyModelPartTransforms(final Model124 a0) {
    final int count = a0.tmdNobj_ca.get();

    if(count == 0) {
      return;
    }

    final UnboundedArrayRef<ModelPartTransforms> transforms = a0.partTransforms_94.deref();

    //LAB_80021320
    for(int i = 0; i < count; i++) {
      final GsDOBJ2 obj2 = a0.dobj2ArrPtr_00.deref().get(i);

      final GsCOORDINATE2 coord2 = obj2.coord2_04.deref();
      final GsCOORD2PARAM params = coord2.param.deref();
      final MATRIX matrix = coord2.coord;

      params.rotate.set(transforms.get(i).rotate_00);
      RotMatrix_80040010(params.rotate, matrix);

      params.trans.set(transforms.get(i).translate_06);
      TransMatrix(matrix, params.trans);
    }

    //LAB_80021390
    a0.partTransforms_94.set(transforms.slice(count));
  }

  @Method(0x800213c4L)
  public static void FUN_800213c4(final Model124 a0) {
    //LAB_80021404
    for(int i = 0; i < a0.tmdNobj_ca.get(); i++) {
      final ModelPartTransforms transforms = a0.partTransforms_94.deref().get(i);
      final GsCOORDINATE2 coord2 = a0.dobj2ArrPtr_00.deref().get(i).coord2_04.deref();
      final MATRIX coord = coord2.coord;
      final GsCOORD2PARAM params = coord2.param.deref();
      RotMatrix_80040010(params.rotate, coord);
      params.trans.setX((params.trans.getX() + transforms.translate_06.getX()) / 2);
      params.trans.setY((params.trans.getY() + transforms.translate_06.getY()) / 2);
      params.trans.setZ((params.trans.getZ() + transforms.translate_06.getZ()) / 2);
      TransMatrix(coord, params.trans);
    }

    //LAB_80021490
    a0.partTransforms_94.set(a0.partTransforms_94.deref().slice(a0.tmdNobj_ca.get()));
  }

  @Method(0x800214bcL)
  public static void applyModelRotationAndScale(final Model124 model) {
    RotMatrix_8003faf0(model.coord2Param_64.rotate, model.coord2_14.coord);
    ScaleMatrix(model.coord2_14.coord, model.scaleVector_fc);
    model.coord2_14.flg.set(0);
  }

  @Method(0x80021520L)
  public static void FUN_80021520(final Model124 model, final ExtendedTmd a1, final TmdAnimationFile a2, final long a3) {
    FUN_80020718(model, a1, a2);
    FUN_8002155c(model, a3);
  }

  @Method(0x8002155cL)
  public static void FUN_8002155c(final Model124 a0, final long a1) {
    final int v0 = (int)_8005039c.offset(2, a1 * 0x2L).getSigned();
    a0.vector_10c.set(v0, v0, v0);
  }

  @Method(0x80021584L)
  public static void FUN_80021584(final Model124 bigStruct, final TmdAnimationFile tmdAnimFile) {
    bigStruct.partTransforms_90.set(tmdAnimFile.partTransforms_10);
    bigStruct.partTransforms_94.set(tmdAnimFile.partTransforms_10);
    bigStruct.animCount_98.set(tmdAnimFile.count_0c);
    bigStruct.s_9a.set(tmdAnimFile._0e);
    bigStruct.ub_9c.set(0);

    applyModelPartTransforms(bigStruct);

    if(bigStruct.ub_a2.get() == 0) {
      bigStruct.s_9e.set(bigStruct.s_9a);
    } else {
      //LAB_800215e8
      bigStruct.s_9e.set((short)(bigStruct.s_9a.get() / 2));
    }

    //LAB_80021608
    bigStruct.ub_9c.set(1);
    bigStruct.partTransforms_94.set(bigStruct.partTransforms_90.deref());
  }

  @Method(0x80021628L)
  public static void FUN_80021628(final Model124 a0) {
    if(mainCallbackIndex_8004dd20.get() != 0x5L && mainCallbackIndex_8004dd20.get() != 0x8L) {
      //LAB_80021678
      for(int i = 0; i < a0.ObjTable_0c.nobj.get(); i++) {
        FUN_80020468(a0.ObjTable_0c.top.deref().get(i), a0.ub_9d.get());
      }
      //LAB_8002169c
    } else if(mainCallbackIndex_8004dd20.get() == 0x5L) { // SMAP
      //LAB_800216b4
      for(int i = 0; i < a0.ObjTable_0c.nobj.get(); i++) {
        FUN_800d9e64(a0.ObjTable_0c.top.deref().get(i), a0.ub_9d.get());
      }
      //LAB_800216d4
    } else if(mainCallbackIndex_8004dd20.get() == 0x8L) { // WMAP
      //LAB_800216ec
      for(int i = 0; i < a0.ObjTable_0c.nobj.get(); i++) {
        FUN_800c8844(a0.ObjTable_0c.top.deref().get(i), a0.ub_9d.get());
      }
    }

    //LAB_80021708
  }

  @Method(0x80021724L)
  public static void FUN_80021724(final Model124 a0) {
    final long v1 = mainCallbackIndex_8004dd20.get();
    if(v1 == 0x5) {
      //LAB_8002177c
      FUN_800da524(a0);
      //LAB_80021758
    } else if(v1 == 0x6L) {
      //LAB_8002176c
      FUN_800ec258(a0);
    } else if(v1 == 0x8L) {
      //LAB_8002178c
      FUN_800c8d90(a0);
    }

    //LAB_80021794
  }

  @Method(0x800217a4L)
  public static void FUN_800217a4(final Model124 bigStruct) {
    if(bigStruct.coord2Param_64.rotate.pad.get() == -1) {
      final MATRIX mat = new MATRIX();
      RotMatrix_8003fd80(bigStruct.coord2Param_64.rotate, mat);
      TransposeMatrix(mat, bigStruct.coord2_14.coord);
      bigStruct.coord2Param_64.rotate.x.set((short)0);
      bigStruct.coord2Param_64.rotate.y.set((short)0);
      bigStruct.coord2Param_64.rotate.z.set((short)0);
      bigStruct.coord2Param_64.rotate.pad.set((short)0);
    } else {
      bigStruct.coord2Param_64.rotate.y.set(FUN_800ea4c8(bigStruct.coord2Param_64.rotate.y.get()));
      RotMatrix_8003faf0(bigStruct.coord2Param_64.rotate, bigStruct.coord2_14.coord);
    }

    ScaleMatrix(bigStruct.coord2_14.coord, bigStruct.scaleVector_fc);
    bigStruct.coord2_14.flg.set(0);
  }

  @Method(0x80021868L)
  public static void FUN_80021868() {
    if(_800bd7a0.get() != 0) {
      free(_800bd7a0.get());
      _800bd7a0.setu(0);
    }

    //LAB_80021894
  }

  @Method(0x800218a4L)
  public static void FUN_800218a4() {
    _800bd7a8.setu(0x7L);
    _800bd7a4.setu(0x4968L);
    _800bd7a0.setu(mallocHead(0x4968L)); //TODO struct
  }

  @Method(0x800218f0L)
  public static void FUN_800218f0() {
    if(_800bd7ac.get() == 1) {
      _800bd7b0.setu(9);
      _800bd7ac.setu(0);
    }
  }

  @Method(0x80021918L)
  public static void FUN_80021918(final GsOBJTABLE2 table, final Tmd tmd, final GsCOORDINATE2 coord2, final long maxSize, final long a4) {
    final long s2 = (short)a4 >> 8;
    final long dobj2Id = a4 & 0xffL;

    GsDOBJ2 dobj2 = getDObj2ById(table, dobj2Id);

    final Memory.TemporaryReservation temp;
    final MATRIX coord;
    final VECTOR scale;
    final SVECTOR rotation;
    final VECTOR translation;

    if(dobj2 == null) {
      temp = MEMORY.temp(0x10);
      dobj2 = new GsDOBJ2(temp.get()); //sp0x10;
      coord = new MATRIX(); //sp0x20;
      scale = new VECTOR();
      rotation = new SVECTOR(); //sp0x40;
      translation = new VECTOR();
    } else {
      temp = null;

      //LAB_80021984
      dobj2.coord2_04.deref().flg.set(0);

      scale = dobj2.coord2_04.deref().param.deref().scale;
      rotation = dobj2.coord2_04.deref().param.deref().rotate;
      translation = dobj2.coord2_04.deref().param.deref().trans;
      coord = dobj2.coord2_04.deref().coord;

      if(dobj2.coord2_04.deref().super_.isNull()) {
        dobj2.coord2_04.deref().super_.set(coord2);
      }
    }

    //LAB_800219ac
    //LAB_80021a98
    if(s2 == 0x2L && tmd != null) {
      updateTmdPacketIlen(getTmdObjTableOffset(tmd, dobj2Id), dobj2, 0);
    }

    //LAB_800219d8
    //LAB_80021ac0
    if(s2 == 0x8L && table != null) {
      FUN_80021bac(table, dobj2Id, maxSize);
    }

    if(s2 == 0x1L) {
      //LAB_800219ec
      rotation.x.set((short)11);
      rotation.y.set((short)11);
      rotation.z.set((short)11);

      dobj2.coord2_04.deref().coord.set(0, (short)0x1000);
      dobj2.coord2_04.deref().coord.set(1, (short)0);
      dobj2.coord2_04.deref().coord.set(2, (short)0);
      dobj2.coord2_04.deref().coord.set(3, (short)0);
      dobj2.coord2_04.deref().coord.set(4, (short)0x1000);
      dobj2.coord2_04.deref().coord.set(5, (short)0);
      dobj2.coord2_04.deref().coord.set(6, (short)0);
      dobj2.coord2_04.deref().coord.set(7, (short)0);
      dobj2.coord2_04.deref().coord.set(8, (short)0x1000);

      RotMatrixX(rotation.x.get(), coord);
      RotMatrixY(rotation.y.get(), coord);
      RotMatrixZ(rotation.z.get(), coord);

      scale.x.set(0x1000);
      scale.y.set(0x1000);
      scale.z.set(0x1000);

      RotMatrix_8003faf0(rotation, coord);
      ScaleMatrixL(coord, scale);

      translation.x.set(1);
      translation.y.set(1);
      translation.z.set(1);

      TransMatrix(coord, translation);
    }

    if(temp != null) {
      temp.release();
    }

    //LAB_80021ad8
  }

  @Method(0x80021b08L)
  public static void initObjTable2(final GsOBJTABLE2 table, final UnboundedArrayRef<GsDOBJ2> dobj2s, final UnboundedArrayRef<GsCOORDINATE2> coord2s, final UnboundedArrayRef<GsCOORD2PARAM> params, final long size) {
    table.top.set(dobj2s);
    table.nobj.set(0);

    //LAB_80021b2c
    for(int i = 0; i < size; i++) {
      dobj2s.get(i).attribute_00.set(0x8000_0000L);
      dobj2s.get(i).coord2_04.set(coord2s.get(i));
      dobj2s.get(i).tmd_08.clear();
      dobj2s.get(i).id_0c.set(0xffff_ffffL);

      coord2s.get(i).param.set(params.get(i));
    }

    //LAB_80021b5c
  }

  @Method(0x80021b64L)
  public static GsDOBJ2 getDObj2ById(final GsOBJTABLE2 table, final long id) {
    //LAB_80021b80
    for(int i = 0; i < table.nobj.get(); i++) {
      final GsDOBJ2 obj2 = table.top.deref().get(i);

      if(obj2.id_0c.get() == id) {
        return obj2;
      }

      //LAB_80021b98
    }

    //LAB_80021ba4
    return null;
  }

  @Method(0x80021bacL)
  public static void FUN_80021bac(final GsOBJTABLE2 table, final long id, final long maxSize) {
    final long size = table.nobj.get();

    //LAB_80021bd4
    GsDOBJ2 struct = table.top.deref().get(0);
    int i;
    for(i = 0; i < size; i++) {
      if((int)struct.id_0c.get() == -1) {
        break;
      }

      struct = table.top.deref().get(i);
    }

    //LAB_80021c08
    if(i >= maxSize) {
      return;
    }

    //LAB_80021bf4
    if(i >= size) {
      struct = table.top.deref().get((int)table.nobj.get());
      table.nobj.incr();
    }

    //LAB_80021c2c
    struct.id_0c.set(id);
    struct.attribute_00.set(0);
    GsInitCoordinate2(null, struct.coord2_04.deref());
    struct.tmd_08.clear();

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
  public static void FUN_80021ca0(final GsOBJTABLE2 table, final Tmd tmd, final GsCOORDINATE2 coord2, final long count, final long nobj) {
    long s1 = 0x801L;

    //LAB_80021d08
    for(int s0 = 1; s0 < nobj; s0++) {
      FUN_80021918(table, tmd, coord2, count, s1);
      s1++;
    }

    //LAB_80021d3c
    long s2 = 0x101L;
    s1 = 0x201L;

    //LAB_80021d64
    for(int i = 1; i < nobj; i++) {
      FUN_80021918(table, tmd, coord2, count, s1);
      FUN_80021918(table, tmd, coord2, count, s2);
      s1++;
      s2++;
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

    if(a0.aui_d0.get(index).get() == 0) {
      a0.aub_ec.get(index).set(0);
      return;
    }

    //LAB_80022068
    long v1 = a0.ub_9d.get();
    final long t2;
    long v0;
    if((v1 & 0x80L) == 0) {
      v1 <<= 1;
      t2 = _800503b0.offset(v1).getSigned();
      v0 = _800503d4.getAddress();
    } else {
      //LAB_80022098
      if(v1 == 0x80L) {
        return;
      }

      v1 &= 0x7fL;
      v1 <<= 1;
      t2 = _800503f8.offset(v1).getSigned();
      v0 = _80050424.getAddress();
    }

    //LAB_800220c0
    final long t1 = MEMORY.ref(2, v0).offset(v1).getSigned();
    long s1;
    if(a0.usArr_ba.get(index).get() != 0x5678L) {
      a0.usArr_ba.get(index).decr();
      if(a0.usArr_ba.get(index).get() != 0) {
        return;
      }

      s1 = a0.aui_d0.get(index).get();
      a0.usArr_ba.get(index).set((int)MEMORY.ref(2, s1).get(0x7fffL));
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
      for(int i = 0; i < a0.usArr_ac.get(index).get(); i++) {
        s1 += 0x4L;
      }

      //LAB_80022164
      rect.x.set((short)(MEMORY.ref(2, s1).getSigned() + t2));
      s1 += 0x2L;
      rect.y.set((short)(MEMORY.ref(2, s1).getSigned() + t1));
      SetDrawMove(gpuPacketAddr_1f8003d8.deref(4).cast(DR_MOVE::new), rect, a2 & 0xffffL, a0_1 & 0xffffL);
      queueGpuPacket(tags_1f8003d0.deref().get(1).getAddress(), gpuPacketAddr_1f8003d8.get());
      gpuPacketAddr_1f8003d8.addu(0x18L);

      s1 += 0x2L;
      a0.usArr_ac.get(index).incr();

      v1 = MEMORY.ref(2, s1).get();
      if(v1 == 0xfffeL) {
        a0.aub_ec.get(index).set(0);
        a0.usArr_ac.get(index).set(0);
      }

      //LAB_800221f8
      if(v1 == 0xffffL) {
        a0.usArr_ac.get(index).set(0);
      }

      return;
    }

    //LAB_80022208
    s1 = a0.aui_d0.get(index).get();
    final long a1_0 = a0.usArr_ac.get(index).get();
    s1 += 0x2L;
    final long a0_0 = MEMORY.ref(2, s1).getSigned();
    s1 += 0x2L;
    v0 = MEMORY.ref(2, s1).getSigned();
    s1 += 0x2L;
    v1 = MEMORY.ref(2, s1).getSigned();
    s1 += 0x2L;
    long s3 = MEMORY.ref(2, s1).getSigned();
    s1 += 0x2L;
    long s0_0 = MEMORY.ref(2, s1).offset(0x2L).get();
    final long s7 = v0 + t1;
    final long s5 = v1 >>> 2;
    v1 = MEMORY.ref(2, s1).getSigned();
    final long s6 = a0_0 + t2;

    if((a1_0 & 0xfL) != 0) {
      a0.usArr_ac.get(index).set((int)(a1_0 - 0x1L));

      if(a0.usArr_ac.get(index).get() == 0) {
        a0.usArr_ac.get(index).set((int)s0_0);
        s0_0 = 0x10L;
      } else {
        //LAB_80022278
        s0_0 = 0;
      }
    }

    //LAB_8002227c
    s0_0 = (int)(s0_0 << 16);
    if(s0_0 == 0) {
      return;
    }

    rect.set((short)960, (short)256, (short)s5, (short)s3);
    SetDrawMove(gpuPacketAddr_1f8003d8.deref(4).cast(DR_MOVE::new), rect, s6 & 0xffffL, s7 & 0xffffL);
    queueGpuPacket(tags_1f8003d0.deref().get(1).getAddress(), gpuPacketAddr_1f8003d8.get());
    gpuPacketAddr_1f8003d8.addu(0x18L);

    s0_0 = (int)s0_0 >> 20;
    s3 -= s0_0;

    final long a3;
    if((int)v1 == 0) {
      rect.set((short)s6, (short)(s7 + s3), (short)s5, (short)s0_0);
      SetDrawMove(gpuPacketAddr_1f8003d8.deref(4).cast(DR_MOVE::new), rect, 960L, 256L);
      queueGpuPacket(tags_1f8003d0.deref().get(1).getAddress(), gpuPacketAddr_1f8003d8.get());
      gpuPacketAddr_1f8003d8.addu(0x18L);

      a3 = s0_0 + 0x100L & 0xffffL;
      rect.set((short)s6, (short)s7, (short)s5, (short)s3);
    } else {
      //LAB_80022358

      rect.set((short)s6, (short)s7, (short)s5, (short)s0_0);
      SetDrawMove(gpuPacketAddr_1f8003d8.deref(4).cast(DR_MOVE::new), rect, 960L, s3 + 256L & 0xffffL);
      queueGpuPacket(tags_1f8003d0.deref().get(1).getAddress(), gpuPacketAddr_1f8003d8.get());
      gpuPacketAddr_1f8003d8.addu(0x18L);

      a3 = 0x100L;
      rect.set((short)s6, (short)(s0_0 + s7), (short)s5, (short)s3);
    }

    //LAB_8002241c
    SetDrawMove(gpuPacketAddr_1f8003d8.deref(4).cast(DR_MOVE::new), rect, 960L, a3);
    queueGpuPacket(tags_1f8003d0.deref().get(1).getAddress(), gpuPacketAddr_1f8003d8.get());
    gpuPacketAddr_1f8003d8.addu(0x18L);

    //LAB_80022440
  }

  @Method(0x8002246cL)
  public static void FUN_8002246c(final Model124 a0, final int a1) {
    if(a0.aui_d0.get(a1).get() == 0) {
      a0.aub_ec.get(a1).set(0);
      return;
    }

    //LAB_80022490
    a0.usArr_ac.get(a1).set(0);
    a0.usArr_ba.get(a1).set((int)MEMORY.ref(2, a0.aui_d0.get(a1).get()).get(0x3fffL));

    if(MEMORY.ref(2, a0.aui_d0.get(a1).get()).get(0x8000L) != 0) {
      a0.aub_ec.get(a1).set(1);
    } else {
      //LAB_800224d0
      a0.aub_ec.get(a1).set(0);
    }

    //LAB_800224d8
    if(MEMORY.ref(2, a0.aui_d0.get(a1).get()).get(0x4000L) != 0) {
      a0.usArr_ba.get(a1).set(0x5678);
      a0.usArr_ac.get(a1).set((int)MEMORY.ref(2, a0.aui_d0.get(a1).get()).offset(0xcL).get());
      a0.aub_ec.get(a1).set(1);
    }

    //LAB_80022510
  }

  @Method(0x80022520L)
  public static void FUN_80022520(final long unused) {
    whichMenu_800bdc38.setu(0x4L);
  }

  @Method(0x80022530L)
  public static void FUN_80022530(final long unused) {
    whichMenu_800bdc38.setu(0x9L);
  }

  @Method(0x80022540L)
  public static void FUN_80022540(final long unused) {
    whichMenu_800bdc38.setu(0xeL);
  }

  @Method(0x80022550L)
  public static void FUN_80022550(final long unused) {
    whichMenu_800bdc38.setu(0x13L);
  }

  @Method(0x80022560L)
  public static void FUN_80022560(final long unused) {
    whichMenu_800bdc38.setu(0x18L);
  }

  @Method(0x80022570L)
  public static void FUN_80022570(final long unused) {
    whichMenu_800bdc38.setu(0x1dL);
  }

  @Method(0x80022580L)
  public static void FUN_80022580(final long unused) {
    whichMenu_800bdc38.setu(0x22L);
  }

  @Method(0x80022590L)
  public static void FUN_80022590() {
    switch((int)whichMenu_800bdc38.get()) {
      case 0x1 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_0);
          whichMenu_800bdc38.setu(0x2L);
          FUN_8001e010(0);
          scriptsDisabled_800bc0b9.set(true);
        }
      }

      case 0x2 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          loadAndRunOverlay(2, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022520", long.class), 0);
        }
      }

      case 0x6 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_0);
          whichMenu_800bdc38.setu(0x7L);
          FUN_8001e010(0);
          scriptsDisabled_800bc0b9.set(true);
        }
      }

      case 0x7 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          loadAndRunOverlay(2, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022530", long.class), 0);
        }
      }

      case 0xb -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_0);
          whichMenu_800bdc38.setu(0xcL);
          FUN_8001e010(0);
          scriptsDisabled_800bc0b9.set(true);
        }
      }

      case 0xc -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          loadAndRunOverlay(2, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022540", long.class), 0);
        }
      }

      case 0x10 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_0);
          whichMenu_800bdc38.setu(0x11L);
          FUN_8001e010(0);
          scriptsDisabled_800bc0b9.set(true);
        }
      }

      case 0x11 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          loadAndRunOverlay(2, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022550", long.class), 0);
        }
      }

      case 0x15 -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_0);
          whichMenu_800bdc38.setu(0x16L);
          FUN_8001e010(0);
          scriptsDisabled_800bc0b9.set(true);
        }
      }

      case 0x16 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          loadAndRunOverlay(2, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022560", long.class), 0);
        }
      }

      case 0x1f -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_0);
          whichMenu_800bdc38.setu(0x20);
          FUN_8001e010(0);
          scriptsDisabled_800bc0b9.set(true);
        }
      }

      case 0x1a -> {
        if((getLoadedDrgnFiles() & 0x80L) == 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_0);
          whichMenu_800bdc38.setu(0x1bL);
        }
      }

      case 0x1b -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          loadAndRunOverlay(2, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022570", long.class), 0);
        }
      }

      case 0x20 -> {
        if((loadedDrgnFiles_800bcf78.get() & 0x80L) == 0) {
          whichMenu_800bdc38.addu(0x1L);
          loadAndRunOverlay(2, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80022580", long.class), 0);
        }
      }

      case 0xe, 0x13, 0x18, 0x24, 0x4 -> FUN_800fcad4();
      case 0x1d -> FUN_8010d614();
      case 0x9 -> FUN_8010a948();
      case 0x22 -> FUN_8010f198();

      case 0xa, 0xf, 0x19, 0x23, 0x5 -> {
        FUN_80012bb4();
        FUN_8001e010(-1L);
        scriptsDisabled_800bc0b9.set(false);
        whichMenu_800bdc38.setu(0);
      }

      case 0x1e, 0x14 -> {
        FUN_80012bb4();
        scriptsDisabled_800bc0b9.set(false);
        whichMenu_800bdc38.setu(0);
      }
    }
  }

  @Method(0x80022898L)
  public static int FUN_80022898(final int itemId) {
    if(itemId >= 0xc0) {
      return 0;
    }

    //LAB_800228b0
    return equipmentStats_80111ff0.get(itemId)._00.get() & 0x4;
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
  public static long FUN_80022afc(final int itemId) {
    if(itemId < 0xc0 || itemId == 0xff) {
      return 0;
    }

    final long target = itemStats_8004f2ac.get(itemId - 0xc0).target_00.get();

    if((target & 0x10L) == 0) {
      //LAB_80022b40
      return 0;
    }

    //LAB_80022b48
    return target & 0x12L;
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
  public static long FUN_80022cd0(final long a0, final long a1) {
    assert false;
    return 0;
  }

  @Method(0x80022d88L)
  public static long useItemInMenu(final long a0, final int itemId, final int charIndex) {
    MEMORY.ref(4, a0).offset(0x0L).setu(0);
    MEMORY.ref(4, a0).offset(0x4L).setu(0);

    if(FUN_80022afc(itemId) == 0) {
      //LAB_80022dd8
      return a0;
    }

    //LAB_80022e0c
    MEMORY.ref(4, a0).offset(0x0L).setu(0x1L);
    if(itemId == 0xdf) { // Charm potion
      if(mainCallbackIndex_8004dd20.get() == 0x8L || hasNoEncounters_800bed58.get() == 0) {
        //LAB_80022e40
        MEMORY.ref(4, a0).offset(0x0L).setu(0x8L);
        encounterAccumulator_800c6ae8.setu(0);
      } else {
        //LAB_80022e50
        MEMORY.ref(4, a0).offset(0x0L).setu(0x9L);
      }

      //LAB_80022e54
      //LAB_80022e60
      return a0;
    }

    //LAB_80022e94
    final ItemStats0c itemStats = itemStats_8004f2ac.get(itemId - 0xc0);
    final int percentage = itemStats.percentage_09.get();
    if((itemStats.type_0b.get() & 0x80) != 0) {
      //LAB_80022edc
      MEMORY.ref(4, a0).offset(0x0L).setu((itemStats.target_00.get() & 0x2) == 0 ? 0x2L : 0x3L);

      final int amount;
      if(percentage == 100) {
        amount = -1;
      } else {
        //LAB_80022ef0
        amount = stats_800be5f8.get(charIndex).maxHp_66.get() * percentage / 100;
      }

      //LAB_80022f3c
      MEMORY.ref(4, a0).offset(0x4L).setu(addHp(charIndex, amount));
    }

    //LAB_80022f50
    if((itemStats.type_0b.get() & 0x40) != 0) {
      //LAB_80022f98
      MEMORY.ref(4, a0).offset(0x0L).setu((itemStats.target_00.get() & 0x2) == 0 ? 0x4L : 0x5L);

      final int amount;
      if(percentage == 100) {
        amount = -1;
      } else {
        //LAB_80022fac
        amount = stats_800be5f8.get(charIndex).maxMp_6e.get() * percentage / 100;
      }

      //LAB_80022ff8
      MEMORY.ref(4, a0).offset(0x4L).setu(addMp(charIndex, amount));
    }

    //LAB_8002300c
    if((itemStats.type_0b.get() & 0x20) != 0) {
      MEMORY.ref(4, a0).offset(0x0L).setu(0x6L);

      final int amount;
      if(percentage == 100) {
        amount = -1;
      } else {
        amount = percentage;
      }

      //LAB_80023050
      MEMORY.ref(4, a0).offset(0x4L).setu(FUN_80022cd0(charIndex, amount));
    }

    //LAB_80023068
    if((itemStats.type_0b.get() & 0x8) != 0) {
      final int status = gameState_800babc8.charData_32c.get(charIndex).status_10.get();

      if((itemStats.status_08.get() & status) != 0) {
        MEMORY.ref(4, a0).offset(0x4L).setu(status);
        gameState_800babc8.charData_32c.get(charIndex).status_10.and(~status);
      }

      //LAB_800230ec
      MEMORY.ref(4, a0).offset(0x0L).setu(0x7L);
    }

    //LAB_800230f0
    //LAB_800230fc
    //LAB_8002312c
    return a0;
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
      if(gameState_800babc8.itemCount_1e6.get() >= 32) {
        break;
      }

      gameState_800babc8.itemCount_1e6.incr();
    }

    //LAB_80023224
    //LAB_80023248
    for(int i = gameState_800babc8.itemCount_1e6.get(); i <= 32; i++) {
      gameState_800babc8.items_2e9.get(i).set(0xff);
    }

    //LAB_8002325c
  }

  @Method(0x80023264L)
  public static void checkForPsychBombX() {
    gameState_800babc8.scriptFlags2_bc.get(13).and(0xfffb_ffffL);

    //LAB_800232a4
    for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
      if(gameState_800babc8.items_2e9.get(i).get() == 0xfa) { // Psych Bomb X
        gameState_800babc8.scriptFlags2_bc.get(13).or(0x4_0000L);
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

    if(itemIndex < 0x20) {
      if(gameState_800babc8.items_2e9.get(itemIndex).get() == 0xff) {
        return 0xff;
      }

      //LAB_80023334
      for(int i = itemIndex; i < 31; i++) {
        gameState_800babc8.items_2e9.get(i).set(gameState_800babc8.items_2e9.get(i + 1).get());
      }

      //LAB_80023358
      gameState_800babc8.items_2e9.get(31).set(0xff);
      gameState_800babc8.itemCount_1e6.decr();
      return 0;
    }

    //LAB_8002338c
    if(itemIndex >= 192) {
      //LAB_800233a4
      for(int i = 0; i < 32; i++) {
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

    if(count >= 32) {
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
  public static long FUN_80023544(final long a0, final long a1) {
    long s3 = 0;
    //LAB_80023580
    for(int s0 = 0; s0 < MEMORY.ref(4, a1).get(); s0++) {
      if(giveItem((int)MEMORY.ref(2, a0).offset(s0 * 0x4L).get()) != 0) {
        s3 = s3 + 0x1L;
      } else {
        //LAB_800235a4
        //LAB_800235c0
        int i;
        for(i = s0; i < MEMORY.ref(4, a1).get() - 0x1L; i++) {
          MEMORY.ref(4, a0).offset(i * 0x4L).setu(MEMORY.ref(4, a0).offset((i + 1) * 0x4L).get());
        }

        //LAB_800235e4
        MEMORY.ref(4, a0).offset(i * 0x4L).setu(0xffL);
        MEMORY.ref(4, a1).subu(0x1L);
        s0--;
      }

      //LAB_80023604
    }

    //LAB_80023618
    return s3;
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

  @Method(0x8002379cL)
  public static void FUN_8002379c() {
    // empty
  }

  @Method(0x800237a4L)
  public static long hasSavedGames() {
    return SaveManager.hasSavedGames() ? 1 : 2;
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

  @Method(0x80023978L)
  public static long compareItems(final MenuItemStruct04 item1, final MenuItemStruct04 item2) {
    final long s0 = getItemIcon(item1.itemId_00.get());
    final long v0 = getItemIcon(item2.itemId_00.get());

    if(s0 != v0) {
      return s0 - v0;
    }

    //LAB_800239c8
    return item1.itemId_00.get() - item2.itemId_00.get();
  }

  @Method(0x800239e0L)
  public static void FUN_800239e0(final ArrayRef<MenuItemStruct04> a0, final ArrayRef<UnsignedByteRef> a1, final long a2) {
    //LAB_800239ec
    int a3 = 0;
    for(int i = 0; i < a2; i++) {
      if((a0.get(i).price_02.get() & 0x1000) == 0) {
        a1.get(a3).set(a0.get(i).itemId_00.get());
        a3++;
      }

      //LAB_80023a0c
    }

    //LAB_80023a1c
    a1.get(a3).set(0xff);
  }

  @Method(0x80023a2cL)
  public static void FUN_80023a2c(final ArrayRef<MenuItemStruct04> a0, final ArrayRef<UnsignedByteRef> a1, final int count) {
    qsort(a0, count, 0x4, getBiFunctionAddress(Scus94491BpeSegment_8002.class, "compareItems", MenuItemStruct04.class, MenuItemStruct04.class, long.class));
    FUN_800239e0(a0, a1, count);
  }

  @Method(0x80023a88L)
  public static void FUN_80023a88() {
    final ArrayRef<MenuItemStruct04> s0 = MEMORY.ref(4, mallocTail(0x4c0L), ArrayRef.of(MenuItemStruct04.class, 0x130, 0x4, MenuItemStruct04::new));

    //LAB_80023ab4
    for(int i = 0; i < 0x130; i++) {
      s0.get(i).itemId_00.set(0xff);
      s0.get(i).price_02.set(0);
    }

    //LAB_80023aec
    for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
      s0.get(i).itemId_00.set(gameState_800babc8.items_2e9.get(i).get());
    }

    //LAB_80023b10
    FUN_80023a2c(s0, gameState_800babc8.items_2e9, gameState_800babc8.itemCount_1e6.get());
    free(s0.getAddress());
  }

  @Method(0x80023b54L)
  public static Renderable58 allocateRenderable(final Drgn0_6666Struct a0, @Nullable Renderable58 a1) {
    if(a1 == null) {
      a1 = MEMORY.ref(4, mallocTail(0x58L), Renderable58::new);
    }

    //LAB_80023b7c
    a1.flags_00.set(0);
    a1.glyph_04.set(0);
    a1._08.set(a0._0a.get());
    a1._0c.set(0);
    a1.startGlyph_10.set(0);
    a1.endGlyph_14.set(a0.entryCount_06.get() - 1);
    a1._18.set(0);
    a1._1c.set(0);
    a1.drgn0_6666_20.set(a0);
    a1.drgn0_6666_data_24.set(MEMORY.ref(4, a0.entries_08.get(a0.entryCount_06.get()).getAddress()).cast(UnboundedArrayRef.of(0x4, UnsignedIntRef::new)));
    a1._28.set(0);
    a1.tpage_2c.set(0);
    a1._34.set(0x1000L);
    a1._38.set(0x1000L);
    a1._3c.set(0x24);
    a1.x_40.set(0);
    a1.y_44.set(0);
    a1._48.set(0);
    a1.child_50.clear();

    if(!renderablePtr_800bdc5c.isNull()) {
      a1.parent_54.set(renderablePtr_800bdc5c.deref());
      renderablePtr_800bdc5c.deref().child_50.set(a1);
    } else {
      //LAB_80023c08
      a1.parent_54.clear();
      renderablePtr_800bdc5c.set(a1);
    }

    //LAB_80023c0c
    renderablePtr_800bdc5c.set(a1);
    return a1;
  }

  @Method(0x80023c28L)
  public static void uploadRenderables() {
    Renderable58 renderable = renderablePtr_800bdc5c.derefNullable();

    _800bdc58.addu(0x1L);

    //LAB_80023c8c
    while(renderable != null) {
      boolean forceUnload = false;
      final UnboundedArrayRef<Drgn0_6666Entry> entries = renderable.drgn0_6666_20.deref().entries_08;

      if((renderable.flags_00.get() & 0x4L) == 0) {
        renderable._08.decr();

        if(renderable._08.get() < 0) {
          if((renderable.flags_00.get() & 0x20L) != 0) {
            renderable.glyph_04.decr();

            if(renderable.glyph_04.get() < renderable.startGlyph_10.get()) {
              if((renderable.flags_00.get() & 0x10L) != 0) {
                forceUnload = true;
                renderable.flags_00.or(0x40L);
              }

              //LAB_80023d0c
              if(renderable._18.get() != 0) {
                renderable.startGlyph_10.set(renderable._18);

                if(renderable._1c.get() != 0) {
                  renderable.endGlyph_14.set(renderable._1c);
                } else {
                  //LAB_80023d34
                  renderable.endGlyph_14.set(renderable._18);
                  renderable.flags_00.or(0x4L);
                }

                //LAB_80023d48
                renderable._18.set(0);
                renderable.flags_00.and(0xffff_ffdfL);
              }

              //LAB_80023d5c
              //LAB_80023e00
              renderable.glyph_04.set(renderable.endGlyph_14);
              renderable._0c.incr();
            }
          } else {
            //LAB_80023d6c
            renderable.glyph_04.incr();

            if(renderable.endGlyph_14.get() < renderable.glyph_04.get()) {
              if((renderable.flags_00.get() & 0x10L) != 0) {
                forceUnload = true;
                renderable.flags_00.or(0x40L);
              }

              //LAB_80023da4
              if(renderable._18.get() != 0) {
                renderable.startGlyph_10.set(renderable._18);

                if(renderable._1c.get() != 0) {
                  renderable.endGlyph_14.set(renderable._1c);
                } else {
                  //LAB_80023dcc
                  renderable.endGlyph_14.set(renderable._18);
                  renderable.flags_00.or(0x4L);
                }

                //LAB_80023de0
                renderable._18.set(0);
                renderable.flags_00.and(0xffff_ffdfL);
              }

              //LAB_80023df4
              //LAB_80023e00
              renderable.glyph_04.set(renderable.startGlyph_10);
              renderable._0c.incr();
            }
          }

          //LAB_80023e08
          renderable._08.set(entries.get(renderable.glyph_04.get())._02.get() - 1);
        }
      }

      //LAB_80023e28
      if((renderable.flags_00.get() & 0x40L) == 0) {
        final long centreX = displayWidth_1f8003e0.get() / 2 + 0x8L;

        final ArrayRef<RenderableMetrics14> metricses = renderable.drgn0_6666_20.deref().getMetrics((int)renderable.drgn0_6666_data_24.deref().get(entries.get(renderable.glyph_04.get())._00.get()).get());

        //LAB_80023e94
        for(int i = metricses.length() - 1; i >= 0; i--) {
          final RenderableMetrics14 metrics = metricses.get(i);

          final long packet = gpuPacketAddr_1f8003d8.get();
          gpuPacketAddr_1f8003d8.addu(0x28L);
          MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
          MEMORY.ref(4, packet).offset(0x4L).setu(0x2c80_8080L);
          MEMORY.ref(1, packet).offset(0x4L).setu(0x80L); // R
          MEMORY.ref(1, packet).offset(0x5L).setu(0x80L); // G
          MEMORY.ref(1, packet).offset(0x6L).setu(0x80L); // B
          MEMORY.ref(1, packet).offset(0x7L).oru((metrics.clut_04.get() & 0x8000L) >>> 14); // Command

          final long x1;
          final long x2;
          if(renderable._34.get() == 0x1000L) {
            if(metrics._10.get() < 0) {
              x2 = renderable.x_40.get() + metrics.x_02.get() - centreX;
              x1 = x2 + metrics.width_08.get();
            } else {
              //LAB_80023f20
              x1 = renderable.x_40.get() + metrics.x_02.get() - centreX;
              x2 = x1 + metrics.width_08.get();
            }
          } else {
            //LAB_80023f40
            final long a0_0 = renderable._34.get() != 0 ? renderable._34.get() : metrics._10.get();

            //LAB_80023f4c
            //LAB_80023f68
            final long a1 = Math.abs((int)(metrics.width_08.get() * a0_0 / 0x1000));
            if(metrics._10.get() < 0) {
              x2 = renderable.x_40.get() + metrics.width_08.get() / 2 + metrics.x_02.get() - centreX - a1 / 2;
              x1 = x2 + a1;
            } else {
              //LAB_80023fb4
              x1 = renderable.x_40.get() + metrics.width_08.get() / 2 + metrics.x_02.get() - centreX - a1 / 2;
              x2 = x1 + a1;
            }
          }

          //LAB_80023fe4
          final long y1;
          final long y2;
          if(renderable._38.get() == 0x1000L) {
            if(metrics._12.get() < 0) {
              y2 = renderable.y_44.get() + metrics.y_03.get() - 120;
              y1 = y2 + metrics.height_0a.get();
            } else {
              //LAB_80024024
              y1 = renderable.y_44.get() + metrics.y_03.get() - 120;
              y2 = y1 + metrics.height_0a.get();
            }
          } else {
            //LAB_80024044
            final long a0_0 = renderable._38.get() != 0 ? renderable._38.get() : metrics._12.get();

            //LAB_80024050
            //LAB_8002406c
            final long a1 = Math.abs((int)(metrics.height_0a.get() * a0_0 / 0x1000));
            if(metrics._12.get() < 0) {
              y2 = renderable.y_44.get() + metrics.height_0a.get() / 2 + metrics.y_03.get() - a1 / 2 - 120;
              y1 = y2 + a1;
            } else {
              //LAB_800240b8
              y1 = renderable.y_44.get() + metrics.height_0a.get() / 2 + metrics.y_03.get() - a1 / 2 - 120;
              y2 = y1 + a1;
            }
          }

          //LAB_800240e8
          MEMORY.ref(2, packet).offset(0x08L).setu(x1); // V0 X
          MEMORY.ref(2, packet).offset(0x0aL).setu(y1); // V0 Y
          MEMORY.ref(1, packet).offset(0x0cL).setu(metrics.u_00.get()); // V0 U
          MEMORY.ref(1, packet).offset(0x0dL).setu(metrics.v_01.get()); // V0 V
          MEMORY.ref(2, packet).offset(0x10L).setu(x2); // V1 X
          MEMORY.ref(2, packet).offset(0x12L).setu(y1); // V1 Y
          MEMORY.ref(2, packet).offset(0x18L).setu(x1); // V2 X
          MEMORY.ref(2, packet).offset(0x1aL).setu(y2); // V2 Y
          MEMORY.ref(2, packet).offset(0x20L).setu(x2); // V3 X
          MEMORY.ref(2, packet).offset(0x22L).setu(y2); // V4 Y

          //LAB_80024144
          //LAB_800241b4
          long v1 = metrics.u_00.get() + metrics.width_08.get();
          final long u = v1 < 0xffL ? v1 : v1 - 0x1L;

          //LAB_80024188
          //LAB_800241b8
          //LAB_800241e0
          v1 = metrics.v_01.get() + metrics.height_0a.get();
          final long v = v1 < 0xffL ? v1 : v1 - 0x1L;

          //LAB_80024148
          MEMORY.ref(1, packet).offset(0x14L).setu(u); // V1 U
          MEMORY.ref(1, packet).offset(0x15L).setu(metrics.v_01.get()); // V1 V

          //LAB_8002418c
          MEMORY.ref(1, packet).offset(0x1cL).setu(metrics.u_00.get()); // V2 U
          MEMORY.ref(1, packet).offset(0x1dL).setu(v); // V2 V

          //LAB_800241e4
          MEMORY.ref(1, packet).offset(0x24L).setu(u); // V3 U
          MEMORY.ref(1, packet).offset(0x25L).setu(v); // V3 V

          if(renderable.clut_30.get() != 0) {
            MEMORY.ref(2, packet).offset(0xeL).setu(renderable.clut_30.get()); // CLUT
          } else {
            //LAB_80024204
            MEMORY.ref(2, packet).offset(0xeL).setu(metrics.clut_04.get() & 0x7fffL); // CLUT
          }

          //LAB_80024214
          if(renderable.tpage_2c.get() != 0) {
            MEMORY.ref(2, packet).offset(0x16L).setu(metrics.tpage_06.get() & 0x60L | renderable.tpage_2c.get()); // TPAGE
          } else {
            //LAB_8002423c
            MEMORY.ref(2, packet).offset(0x16L).setu(metrics.tpage_06.get() & 0x7fL); // TPAGE
          }

          //LAB_8002424c
          queueGpuPacket(tags_1f8003d0.deref().get(renderable._3c.get()).getAddress(), packet);
        }
      }

      //LAB_80024280
      if((renderable.flags_00.get() & 0x8L) != 0 || forceUnload) {
        //LAB_800242a8
        unloadRenderable(renderable);
      }

      //LAB_800242b0
      renderable = renderable.parent_54.derefNullable();
    }

    //LAB_800242b8
  }

  @Method(0x800242e8L)
  public static void unloadRenderable(final Renderable58 a0) {
    final Renderable58 v0 = a0.child_50.derefNullable();
    final Renderable58 v1 = a0.parent_54.derefNullable();

    if(v0 == null) {
      if(v1 == null) {
        renderablePtr_800bdc5c.clear();
      } else {
        //LAB_80024320
        renderablePtr_800bdc5c.set(v1);
        v1.child_50.clear();
      }
      //LAB_80024334
    } else if(v1 == null) {
      v0.parent_54.clear();
    } else {
      //LAB_80024350
      v0.parent_54.set(v1);
      v1.child_50.setNullable(a0.child_50.derefNullable());
    }

    //LAB_80024364
    free(a0.getAddress());
  }

  @Method(0x8002437cL)
  public static void deallocateRenderables(final long a0) {
    Renderable58 s0 = renderablePtr_800bdc5c.derefNullable();

    if(s0 != null) {
      //LAB_800243b4
      while(!s0.parent_54.isNull()) {
        final Renderable58 a0_0 = s0;
        s0 = s0.parent_54.deref();

        if(a0_0._28.get() <= a0) {
          unloadRenderable(a0_0);
        }

        //LAB_800243d0
      }

      //LAB_800243e0
      if(s0._28.get() <= a0) {
        unloadRenderable(s0);
      }

      //LAB_800243fc
      if(a0 != 0) {
        saveListUpArrow_800bdb94.clear();
        saveListDownArrow_800bdb98.clear();
        _800bdb9c.clear();
        _800bdba0.clear();
        renderablePtr_800bdba4.clear();
        renderablePtr_800bdba8.clear();

        selectedMenuOptionRenderablePtr_800bdbe0.clear();
        selectedMenuOptionRenderablePtr_800bdbe4.clear();
        renderablePtr_800bdbe8.clear();
        renderablePtr_800bdbec.clear();
        renderablePtr_800bdbf0.clear();

        renderablePtr_800bdc20.clear();
      }
    }

    //LAB_80024460
  }

  @Method(0x80024480L)
  public static long scriptGiveGold(final RunningScript script) {
    script.params_20.get(1).deref().set(addGold(script.params_20.get(0).deref().get()));
    return 0;
  }

  @Method(0x800244c4L)
  public static long scriptGiveChestContents(final RunningScript s0) {
    final int a0 = switch(s0.params_20.get(0).deref().get()) {
      case 0xfb -> addGold(20);
      case 0xfc -> addGold(50);
      case 0xfd -> addGold(100);
      case 0xfe -> addGold(200);
      case 0xff -> 0xff;
      default -> giveItem(s0.params_20.get(0).deref().get());
    };

    //LAB_80024574
    s0.params_20.get(1).deref().set(a0);

    //LAB_80024580
    return 0;
  }

  @Method(0x80024590L)
  public static long scriptTakeItem(final RunningScript script) {
    final int itemId = script.params_20.get(0).deref().get() & 0xff;

    final GameState52c state = gameState_800babc8;

    if(itemId < 0xc0) {
      //LAB_800245e0
      for(int i = 0; i < state.equipmentCount_1e4.get(); i++) {
        if(state.equipment_1e8.get(i).get() == itemId) {
          //LAB_8002460c
          script.params_20.get(1).deref().set(takeEquipment(i));
          return 0;
        }
      }

      //LAB_80024600
      script.params_20.get(1).deref().set(0xff);
    } else {
      //LAB_80024628
      script.params_20.get(1).deref().set(takeItem(itemId));
    }

    //LAB_8002463c
    return 0;
  }

  @Method(0x80024654L)
  public static void FUN_80024654() {
    initFileEntries(_80052ae0.reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
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
   *
   * <ol start="0">
   *   <li>Game font</li>
   *   <li>Japanese font</li>
   *   <li>Japanese text</li>
   *   <li>Dialog box border</li>
   *   <li>Red downward bobbing arrow</li>
   * </ol>
   */
  @Method(0x800249b4L)
  public static void basicUiTexturesLoaded(final long address, final long fileSize, final long unused) {
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

    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    //LAB_80024e88
    for(int i = 0; i < mrg.count.get(); i++) {
      final MrgEntry entry = mrg.entries.get(i);

      if(entry.size.get() != 0) {
        final TimHeader tim = parseTimHeader(MEMORY.ref(4, mrg.getFile(i)).offset(0x4L));
        final int rectIndex = indexOffsets[i];

        // Iteration 0 will count as >= 2
        if(MathHelper.unsign(i - 1, 4) >= 0x2L) {
          LoadImage(rects[rectIndex], tim.getImageAddress());
        }

        //LAB_80024efc
        if(i == 3) {
          //LAB_80024f2c
          LoadImage(rects[indexOffsets[i] + 1], tim.getClutAddress());
        } else if(i < 4) {
          //LAB_80024fac
          for(int s0 = 0; s0 < 4; s0++) {
            final RECT rect = new RECT().set(rects[rectIndex + 1]);
            rect.x.set((short)(rect.x.get() + s0 * 16));
            LoadImage(rect, tim.getClutAddress() + s0 * 0x80L);
          }
          //LAB_80024f1c
        } else if(i == 0x4L) {
          //LAB_80024f68
          LoadImage(rects[rectIndex + 1], tim.getClutAddress());
        }

        //LAB_80025000
      }

      //LAB_80025008
    }

    //LAB_80025018
    free(mrg.getAddress());
  }

  @Method(0x8002504cL)
  public static void loadBasicUiTexturesAndSomethingElse() {
    loadDrgnBinFile(0, 6669, 0, getMethodAddress(Scus94491BpeSegment_8002.class, "basicUiTexturesLoaded", long.class, long.class, long.class), 0, 0x4L);
    noop_8002498c();

    _800bdf00.setu(0xdL);
    _800bdf04.setu(0);
    _800bdf08.setu(0);
    _800be5c4.setu(0);
    clearCharacterStats();

    //LAB_800250c0
    for(int i = 0; i < 8; i++) {
      FUN_80029920(i, 0);
    }

    //LAB_800250ec
    for(int i = 0; i < 8; i++) {
      _800be358.get(i)._00.set(0);
      _800bdf38.get(i)._00.set(0);
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
  public static long FUN_80025158(final RunningScript a0) {
    final int s1 = a0.params_20.get(0).deref().get();
    FUN_800258a8(s1);

    final Struct84 struct84 = _800bdf38.get(s1);
    struct84._04.set((short)a0.params_20.get(1).deref().get());
    struct84._08.or(0x1000L);
    struct84.str_24.set(MEMORY.ref(2, a0.params_20.get(2).getPointer(), LodString::new));
    struct84.ptr_58.set(mallocHead(struct84._1c.get() * (struct84._1e.get() + 1) * 8));
    FUN_8002a2b4(s1);
    FUN_80027d74(s1, struct84._14.get(), struct84._16.get());
    return 0;
  }

  @Method(0x80025218L)
  public static long FUN_80025218(final RunningScript a0) {
    if(a0.params_20.get(2).deref().get() == 0) {
      return 0;
    }

    final int s2 = a0.params_20.get(0).deref().get();
    final long s3 = _80052ba8.offset(((a0.params_20.get(2).deref().get() & 0xf00L) >>> 8) * 0x2L).get();
    FUN_800257e0(s2);

    final Struct4c struct4c = _800be358.get(s2);
    struct4c._04.set((short)_80052b88.offset(((a0.params_20.get(2).deref().get() & 0xf0L) >>> 4) * 0x2L).get());
    struct4c._06.set((short)_80052b68.offset((a0.params_20.get(2).deref().get() & 0xfL) * 0x2L).get());
    struct4c._14.set((short)0);
    struct4c._16.set((short)0);
    struct4c.width_18.set((short)(a0.params_20.get(3).deref().get() + 0x1L));
    struct4c.lines_1a.set((short)(a0.params_20.get(4).deref().get() + 0x1L));
    FUN_800258a8(s2);

    final Struct84 struct84 = _800bdf38.get(s2);
    struct84._04.set((short)s3);
    struct84.str_24.setPointer(a0.params_20.get(5).getPointer());

    if((short)s3 == 0x1L && (a0.params_20.get(1).deref().get() & 0x1000L) > 0) {
      struct84._08.or(0x20L);
    }

    //LAB_80025370
    //LAB_80025374
    if((short)s3 == 0x3L) {
      struct84._6c.set(-1);
    }

    //LAB_800253a4
    if((short)s3 == 0x4L) {
      struct84._08.or(0x200L);
    }

    //LAB_800253d4
    struct84._08.or(0x1000L);
    struct84.ptr_58.set(mallocHead(struct84._1c.get() * (struct84._1e.get() + 0x1L) * 0x8L));
    FUN_8002a2b4(s2);
    FUN_80028938(s2, a0.params_20.get(1).deref().get());

    if(struct4c._04.get() == 0x2L) {
      struct4c._24.get(5).set(struct4c._14.get());
      struct4c._24.get(6).set(struct4c._16.get());
      struct4c._14.set((short)struct4c._24.get(1).get());
      struct4c._16.set((short)struct4c._24.get(2).get());
      struct4c._08.or(0x2L);
    }

    //LAB_80025494
    //LAB_80025498
    return 0;
  }

  @Method(0x800254bcL)
  public static long FUN_800254bc(final RunningScript a0) {
    final int s2 = a0.params_20.get(0).deref().get();

    if(a0.params_20.get(1).deref().get() != 0) {
      final int a2 = a0.params_20.get(1).deref().get();
      final short s0 = (short)_80052b88.offset((a2 & 0xf0) >>> 3).get();
      final short s1 = (short)_80052b68.offset((a2 & 0xf) * 0x2L).get();
      final short s4 = (short)_80052ba8.offset((a2 & 0xf00) >>> 7).get();
      FUN_800257e0(s2);

      final Struct4c struct4c = _800be358.get(s2);
      struct4c._04.set(s0);
      struct4c._06.set(s1);
      struct4c._14.set((short)a0.params_20.get(2).deref().get());
      struct4c._16.set((short)a0.params_20.get(3).deref().get());
      struct4c.width_18.set((short)(a0.params_20.get(4).deref().get() + 1));
      struct4c.lines_1a.set((short)(a0.params_20.get(5).deref().get() + 1));
      FUN_800258a8(s2);

      final Struct84 struct84 = _800bdf38.get(s2);

      struct84._04.set(s4);
      struct84.str_24.set(a0.params_20.get(6).deref().reinterpret(LodString::new));

      if(s4 == 1 && (a2 & 0x1000) > 0) {
        struct84._08.or(0x20);
      }

      //LAB_8002562c
      //LAB_80025630
      if(s4 == 3) {
        struct84._6c.set(-1);
      }

      //LAB_80025660
      if(s4 == 4) {
        struct84._08.or(0x200);
      }

      //LAB_80025690
      struct84._08.or(0x1000);
      struct84.ptr_58.set(mallocHead(struct84._1c.get() * (struct84._1e.get() + 1) * 0x8L));
      FUN_8002a2b4(s2);
      FUN_80027d74(s2, struct84._14.get(), struct84._16.get());
    }

    //LAB_800256f0
    return 0;
  }

  @Method(0x80025718L)
  public static long FUN_80025718(final RunningScript a0) {
    final Struct84 s0 = _800bdf38.get(a0.params_20.get(0).deref().get());

    s0._6c.set(-1);
    s0._70.set((short)a0.params_20.get(2).deref().get());
    s0._72.set((short)a0.params_20.get(1).deref().get());

    if(s0._00.get() == 0xdL) {
      s0._00.set(0x17);
      s0._64.set(0x0a);
      s0._78.set(0x16);
      Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
    }

    //LAB_800257bc
    s0._08.or(0x800L);
    return 0;
  }

  @Method(0x800257e0L)
  public static void FUN_800257e0(final int a0) {
    if(_800bdf38.get(a0)._00.get() != 0) {
      free(_800bdf38.get(a0).ptr_58.get());
    }

    //LAB_80025824
    final Struct4c struct = _800be358.get(a0);

    struct._00.set(0x1L);
    struct._06.set((short)0);
    struct._08.set(0);
    struct.z_0c.set(14);
    struct._10.set(0);
    struct._1c.set(0);
    struct._1e.set(0);
    struct._20.set((short)0x1000);
    struct._22.set((short)0x1000);

    //LAB_80025880
    for(int i = 0; i < 10; i++) {
      struct._24.get(i).set(0);
    }
  }

  @Method(0x800258a8L)
  public static void FUN_800258a8(final int a0) {
    final Struct84 struct84 = _800bdf38.get(a0);
    struct84._00.set(0x1);
    struct84._08.set(0);
    struct84._0c.set(0xdL);
    struct84._10.set(0);
    struct84._20.set((short)0x1000);
    struct84._22.set((short)0x1000);
    struct84._28.set(0);
    struct84._2a.set((short)2);
    struct84._2c.set((short)0);
    struct84._30.set(0);
    struct84._34.set((short)0);
    struct84._36.set((short)0);
    struct84._38.set((short)0);
    struct84._3a.set((short)0);
    struct84._3c.set((short)0);
    struct84._3e.set((short)1);
    struct84._40.set((short)0);
    struct84._42.set((short)0);
    struct84._44.set((short)0);

    final Struct4c struct4c = _800be358.get(a0);
    struct84._14.set(struct4c._14.get());
    struct84._16.set(struct4c._16.get());
    struct84._1c.set((short)(struct4c.width_18.get() - 1));
    struct84._1e.set((short)(struct4c.lines_1a.get() - 1));
    struct84._18.set((short)(struct84._14.get() - struct84._1c.get() * 9 / 2));
    struct84._1a.set((short)(struct84._16.get() - struct84._1e.get() * 6));

    //LAB_800259b4
    for(int i = 0; i < 8; i++) {
      struct84._46.get(i).set((short)0);
    }

    //LAB_800259e4
    for(int i = 0; i < 10; i++) {
      MEMORY.ref(4, struct84._5c.getAddress()).offset(i * 0x4L).setu(0); //TODO
    }
  }

  @Method(0x80025a04L)
  public static void FUN_80025a04(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long lo;

    v1 = 0x800c_0000L;
    v1 = v1 - 0x1ca8L;
    v0 = a0 << 2;
    v0 = v0 + a0;
    v0 = v0 << 2;
    v0 = v0 - a0;
    v0 = v0 << 2;
    v0 = v0 + v1;
    v0 = MEMORY.ref(4, v0).offset(0x0L).get();
    switch((int)v0) {
      case 1 -> {
        v0 = 0x800c_0000L;
        v0 = v0 - 0x1ca8L;
        v1 = a0 << 2;
        v1 = v1 + a0;
        v1 = v1 << 2;
        v1 = v1 - a0;
        v1 = v1 << 2;
        a2 = v1 + v0;
        a1 = MEMORY.ref(2, a2).offset(0x4L).getSigned();
        v0 = 0x1L;
        if(a1 != v0) {
          v0 = 0x2L;
          if((int)a1 < 0x2L) {
            if(a1 == 0) {
              //LAB_80025ab8
              v0 = 0x4L;
              MEMORY.ref(4, a2).offset(0x0L).setu(v0);
              v0 = MEMORY.ref(4, a2).offset(0x8L).get();
              v1 = 0x8000_0000L;
              v0 = v0 ^ v1;
              MEMORY.ref(4, a2).offset(0x8L).setu(v0);
              break;
            }
          } else {
            //LAB_80025aa8
            if(a1 == v0) {
              //LAB_80025ad4
              v0 = 0x8008_0000L;
              v1 = MEMORY.ref(4, v0).offset(-23624L).get();
              v0 = 0x3cL;
              lo = (int)v0 / (int)v1;
              a0 = lo;
              v0 = MEMORY.ref(4, a2).offset(0x8L).get();
              MEMORY.ref(4, a2).offset(0x10L).setu(0);
              v0 = v0 | 0x1L;
              MEMORY.ref(4, a2).offset(0x8L).setu(v0);
              if((int)a0 < 0) {
                a0 = a0 + 0x3L;
              }

              //LAB_80025b00
              v1 = MEMORY.ref(4, a2).offset(0x8L).get();
              v0 = (int)a0 >> 2;
              MEMORY.ref(4, a2).offset(0x24L).setu(v0);
              v1 = v1 & 0x2L;
              MEMORY.ref(4, a2).offset(0x0L).setu(a1);
              if(v1 != 0) {
                a0 = MEMORY.ref(4, a2).offset(0x28L).get();
                v1 = MEMORY.ref(4, a2).offset(0x38L).get();
                a0 = a0 - v1;
                lo = (int)a0 / (int)v0;
                a0 = lo;
                a1 = MEMORY.ref(4, a2).offset(0x3cL).get();
                v0 = MEMORY.ref(4, a2).offset(0x2cL).get();
                v1 = MEMORY.ref(4, a2).offset(0x24L).get();
                v0 = v0 - a1;
                lo = (int)v0 / (int)v1;
                v0 = lo;
                MEMORY.ref(4, a2).offset(0x30L).setu(a0);
                MEMORY.ref(4, a2).offset(0x34L).setu(v0);
              }
              break;
            }
          }
        }

        //LAB_80025b54
        v1 = 0x800c_0000L;
        v1 = v1 - 0x1ca8L;

        //LAB_80025b5c
        v0 = a0 << 2;
        v0 = v0 + a0;
        v0 = v0 << 2;
        v0 = v0 - a0;
        v0 = v0 << 2;
        a0 = v0 + v1;
        v1 = MEMORY.ref(2, a0).offset(0x18L).getSigned();
        v0 = 0x5L;
        MEMORY.ref(4, a0).offset(0x0L).setu(v0);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, a0).offset(0x1aL).getSigned();
        v0 = (int)v0 >> 1;
        MEMORY.ref(2, a0).offset(0x1cL).setu(v0);
        v0 = v1 << 1;
        v0 = v0 + v1;
        v1 = MEMORY.ref(4, a0).offset(0x8L).get();
        v0 = v0 << 1;
        v1 = v1 & 0x4L;
        MEMORY.ref(2, a0).offset(0x1eL).setu(v0);
        if(v1 != 0) {
          v0 = 0x6L;
          MEMORY.ref(4, a0).offset(0x0L).setu(v0);
        }

        //LAB_80025bc0
        v0 = MEMORY.ref(4, a0).offset(0x8L).get();
        v1 = 0x8000_0000L;
        v0 = v0 | v1;
        MEMORY.ref(4, a0).offset(0x8L).setu(v0);
      }
      case 2 -> {
        v1 = 0x800c_0000L;
        v1 = v1 - 0x1ca8L;
        v0 = a0 << 2;
        v0 = v0 + a0;
        v0 = v0 << 2;
        v0 = v0 - a0;
        v0 = v0 << 2;
        a2 = v0 + v1;
        v0 = MEMORY.ref(4, a2).offset(0x8L).get();
        v1 = 0x8000_0000L;
        v0 = v0 | v1;
        v1 = MEMORY.ref(2, a2).offset(0x4L).getSigned();
        MEMORY.ref(4, a2).offset(0x8L).setu(v0);
        v0 = 0x2L;
        if(v1 != v0) {
          v1 = 0x5L;
        } else {
          v1 = 0x5L;
          v0 = MEMORY.ref(4, a2).offset(0x10L).get();
          v1 = MEMORY.ref(4, a2).offset(0x24L).get();
          v0 = v0 << 12;
          lo = (int)v0 / (int)v1;
          v0 = lo;
          a0 = MEMORY.ref(4, a2).offset(0x10L).get();
          v1 = MEMORY.ref(4, a2).offset(0x24L).get();
          a0 = a0 << 12;
          lo = (int)a0 / (int)v1;
          a0 = lo;
          v1 = MEMORY.ref(2, a2).offset(0x18L).getSigned();
          MEMORY.ref(2, a2).offset(0x20L).setu(v0);
          v0 = v1 << 3;
          v0 = v0 + v1;
          v1 = v0 >>> 31;
          v0 = v0 + v1;
          v1 = MEMORY.ref(2, a2).offset(0x20L).getSigned();
          v0 = (int)v0 >> 1;
          lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
          a1 = lo;
          MEMORY.ref(2, a2).offset(0x22L).setu(a0);
          if((int)a1 < 0) {
            a1 = a1 + 0xfffL;
          }

          //LAB_80025c70
          v1 = MEMORY.ref(2, a2).offset(0x22L).getSigned();

          v0 = v1 << 1;
          v0 = v0 + v1;
          v1 = MEMORY.ref(2, a2).offset(0x1aL).getSigned();
          v0 = v0 << 1;
          lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
          v0 = (int)a1 >> 12;
          v1 = lo;
          MEMORY.ref(2, a2).offset(0x1cL).setu(v0);
          if((int)v1 < 0) {
            v1 = v1 + 0xfffL;
          }

          //LAB_80025ca0
          v0 = (int)v1 >> 12;
          MEMORY.ref(2, a2).offset(0x1eL).setu(v0);
          v0 = MEMORY.ref(4, a2).offset(0x10L).get();
          v1 = MEMORY.ref(4, a2).offset(0x8L).get();
          v0 = v0 + 0x1L;
          v1 = v1 & 0x2L;
          MEMORY.ref(4, a2).offset(0x10L).setu(v0);
          if(v1 != 0) {
            v0 = MEMORY.ref(4, a2).offset(0x28L).get();
            a0 = MEMORY.ref(4, a2).offset(0x30L).get();
            v1 = MEMORY.ref(4, a2).offset(0x2cL).get();
            a1 = MEMORY.ref(4, a2).offset(0x34L).get();
            v0 = v0 - a0;
            MEMORY.ref(4, a2).offset(0x28L).setu(v0);
            v0 = MEMORY.ref(2, a2).offset(0x28L).get();
            v1 = v1 - a1;
            MEMORY.ref(4, a2).offset(0x2cL).setu(v1);
            v1 = MEMORY.ref(2, a2).offset(0x2cL).get();
            MEMORY.ref(2, a2).offset(0x14L).setu(v0);
            MEMORY.ref(2, a2).offset(0x16L).setu(v1);
          }

          //LAB_80025cf0
          v0 = MEMORY.ref(4, a2).offset(0x10L).get();
          v1 = MEMORY.ref(4, a2).offset(0x24L).get();

          if((int)v0 < (int)v1) {
            break;
          }
          v0 = 0x5L;
          MEMORY.ref(4, a2).offset(0x0L).setu(v0);
          v0 = MEMORY.ref(4, a2).offset(0x8L).get();
          v1 = MEMORY.ref(2, a2).offset(0x18L).getSigned();
          v0 = v0 ^ 0x1L;
          MEMORY.ref(4, a2).offset(0x8L).setu(v0);
          v0 = v1 << 3;
          v0 = v0 + v1;
          v1 = v0 >>> 31;
          v0 = v0 + v1;
          v1 = MEMORY.ref(2, a2).offset(0x1aL).getSigned();
          v0 = (int)v0 >> 1;
          MEMORY.ref(2, a2).offset(0x1cL).setu(v0);
          v0 = v1 << 1;
          v0 = v0 + v1;
          v1 = MEMORY.ref(4, a2).offset(0x8L).get();
          v0 = v0 << 1;
          v1 = v1 & 0x4L;
          MEMORY.ref(2, a2).offset(0x1eL).setu(v0);
          if(v1 != 0) {
            v0 = 0x6L;
            MEMORY.ref(4, a2).offset(0x0L).setu(v0);
          }

          //LAB_80025d5c
          v0 = MEMORY.ref(4, a2).offset(0x8L).get();

          v0 = v0 & 0x2L;
          if(v0 == 0) {
            break;
          }

          v0 = MEMORY.ref(2, a2).offset(0x38L).get();
          v1 = MEMORY.ref(2, a2).offset(0x3cL).get();
          MEMORY.ref(2, a2).offset(0x14L).setu(v0);
          MEMORY.ref(2, a2).offset(0x16L).setu(v1);
          break;
        }

        //LAB_80025d84
        v0 = MEMORY.ref(4, a2).offset(0x8L).get();
        v0 = v0 & 0x4L;
        MEMORY.ref(4, a2).offset(0x0L).setu(v1);
        if(v0 != 0) {
          v0 = 0x6L;
          MEMORY.ref(4, a2).offset(0x0L).setu(v0);
        }
      }
      case 3 -> {
        v1 = 0x800c_0000L;
        v1 = v1 - 0x1ca8L;
        v0 = a0 << 2;
        v0 = v0 + a0;
        v0 = v0 << 2;
        v0 = v0 - a0;
        v0 = v0 << 2;
        a1 = v0 + v1;
        v1 = MEMORY.ref(2, a1).offset(0x4L).getSigned();
        v0 = 0x2L;
        if(v1 == v0) {
          v0 = MEMORY.ref(4, a1).offset(0x10L).get();
          v1 = MEMORY.ref(4, a1).offset(0x24L).get();
          v0 = v0 << 12;
          lo = (int)v0 / (int)v1;
          v0 = lo;
          a0 = MEMORY.ref(4, a1).offset(0x10L).get();
          v1 = MEMORY.ref(4, a1).offset(0x24L).get();
          a0 = a0 << 12;
          lo = (int)a0 / (int)v1;
          a0 = lo;
          v1 = MEMORY.ref(2, a1).offset(0x18L).getSigned();
          MEMORY.ref(2, a1).offset(0x20L).setu(v0);
          v0 = v1 << 3;
          v0 = v0 + v1;
          v1 = v0 >>> 31;
          v0 = v0 + v1;
          v1 = MEMORY.ref(2, a1).offset(0x20L).getSigned();
          v0 = (int)v0 >> 1;
          lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
          a2 = lo;
          MEMORY.ref(2, a1).offset(0x22L).setu(a0);
          if((int)a2 < 0) {
            a2 = a2 + 0xfffL;
          }

          //LAB_80025e30
          v1 = MEMORY.ref(2, a1).offset(0x22L).getSigned();

          v0 = v1 << 1;
          v0 = v0 + v1;
          v1 = MEMORY.ref(2, a1).offset(0x1aL).getSigned();
          v0 = v0 << 1;
          lo = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
          v0 = (int)a2 >> 12;
          v1 = lo;
          MEMORY.ref(2, a1).offset(0x1cL).setu(v0);
          if((int)v1 < 0) {
            v1 = v1 + 0xfffL;
          }

          //LAB_80025e60
          v0 = MEMORY.ref(4, a1).offset(0x10L).get();
          v1 = (int)v1 >> 12;
          MEMORY.ref(2, a1).offset(0x1eL).setu(v1);
          v0 = v0 - 0x1L;
          MEMORY.ref(4, a1).offset(0x10L).setu(v0);
          if((int)v0 <= 0) {
            v0 = MEMORY.ref(4, a1).offset(0x8L).get();
            MEMORY.ref(2, a1).offset(0x1eL).setu(0);
            MEMORY.ref(2, a1).offset(0x1cL).setu(0);
            MEMORY.ref(4, a1).offset(0x0L).setu(0);
            v0 = v0 ^ 0x1L;
            MEMORY.ref(4, a1).offset(0x8L).setu(v0);
          }
          break;
        }

        //LAB_80025e94
        MEMORY.ref(4, a1).offset(0x0L).setu(0);
      }
      case 4, 5 -> {
        v0 = 0x800c_0000L;
        v0 = v0 - 0x20c8L;
        v1 = a0 << 5;
        v1 = v1 + a0;
        v1 = v1 << 2;
        v1 = v1 + v0;
        v0 = MEMORY.ref(4, v1).offset(0x0L).get();
        if(v0 != 0) {
          break;
        }
        v1 = 0x800c_0000L;
        v1 = v1 - 0x1ca8L;
        v0 = a0 << 2;
        v0 = v0 + a0;
        v0 = v0 << 2;
        v0 = v0 - a0;
        v0 = v0 << 2;
        a1 = v0 + v1;
        v1 = MEMORY.ref(2, a1).offset(0x4L).getSigned();
        v0 = 0x2L;
        if(v1 == v0) {
          v0 = 0x8008_0000L;
          v1 = MEMORY.ref(4, v0).offset(-23624L).get();
          v0 = 0x3cL;
          lo = (int)v0 / (int)v1;
          v1 = lo;
          v0 = MEMORY.ref(4, a1).offset(0x8L).get();

          v0 = v0 | 0x1L;
          MEMORY.ref(4, a1).offset(0x8L).setu(v0);
          if((int)v1 < 0) {
            v1 = v1 + 0x3L;
          }

          //LAB_80025f18
          v0 = (int)v1 >> 2;
          MEMORY.ref(4, a1).offset(0x24L).setu(v0);
          MEMORY.ref(4, a1).offset(0x10L).setu(v0);
          v0 = 0x3L;
          MEMORY.ref(4, a1).offset(0x0L).setu(v0);
        } else {
          //LAB_80025f30
          MEMORY.ref(4, a1).offset(0x0L).setu(0);
        }

        //LAB_80025f34
        a1 = 0;
        FUN_80029920((int)a0, a1);
      }
    }

    //LAB_80025f3c
  }

  @Method(0x80025f4cL)
  public static void renderTextboxBackground(final int textboxIndex) {
    long v0;

    //LAB_80025f7c
    final byte[] sp0x10 = MEMORY.getBytes(_80010868.getAddress(), 0x48);
    final long s1 = _800be358.get(textboxIndex).getAddress(); //TODO
    if(MEMORY.ref(2, s1).offset(0x4L).getSigned() != 0) {
      if(MEMORY.ref(4, s1).offset(0x0L).get() != 0x1L) {
        final long s0 = gpuPacketAddr_1f8003d8.get();
        gpuPacketAddr_1f8003d8.addu(0x24L);
        setGp0_38(s0);
        gpuLinkedListSetCommandTransparency(s0, true);
        v0 = MEMORY.ref(2, s1).offset(0x14L).get() - MEMORY.ref(2, s1).offset(0x1cL).get() - centreScreenX_1f8003dc.get();
        MEMORY.ref(2, s0).offset(0x18L).setu(v0);
        MEMORY.ref(2, s0).offset(0x8L).setu(v0);
        v0 = MEMORY.ref(2, s1).offset(0x14L).get() + MEMORY.ref(2, s1).offset(0x1cL).get() - centreScreenX_1f8003dc.get();
        MEMORY.ref(2, s0).offset(0x20L).setu(v0);
        MEMORY.ref(2, s0).offset(0x10L).setu(v0);
        v0 = MEMORY.ref(2, s1).offset(0x16L).get() - MEMORY.ref(2, s1).offset(0x1eL).get() - centreScreenY_1f8003de.get();
        MEMORY.ref(2, s0).offset(0x12L).setu(v0);
        MEMORY.ref(2, s0).offset(0xaL).setu(v0);
        final long v1 = MEMORY.ref(2, s1).offset(0x16L).get() + MEMORY.ref(2, s1).offset(0x1eL).get() - centreScreenY_1f8003de.get();
        MEMORY.ref(2, s0).offset(0x22L).setu(v1);
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        v0 = _800be5c4.getSigned() * 0xcL;
        MEMORY.ref(1, s0).offset(0x14L).setu(sp0x10[(int)v0]);
        MEMORY.ref(1, s0).offset(0xcL).setu(sp0x10[(int)v0]);
        MEMORY.ref(1, s0).offset(0x15L).setu(sp0x10[(int)v0 + 4]);
        MEMORY.ref(1, s0).offset(0xdL).setu(sp0x10[(int)v0 + 4]);
        MEMORY.ref(1, s0).offset(0x1cL).setu(0);
        MEMORY.ref(1, s0).offset(0x16L).setu(sp0x10[(int)v0 + 8]);
        MEMORY.ref(1, s0).offset(0xeL).setu(sp0x10[(int)v0 + 8]);
        MEMORY.ref(1, s0).offset(0x4L).setu(0);
        MEMORY.ref(1, s0).offset(0x1dL).setu(0);
        MEMORY.ref(1, s0).offset(0x5L).setu(0);
        MEMORY.ref(1, s0).offset(0x1eL).setu(0);
        MEMORY.ref(1, s0).offset(0x6L).setu(0);

        if(MEMORY.ref(2, s1).offset(0x6L).getSigned() != 0) {
          renderTextboxBorder(textboxIndex, s0);
        }

        //LAB_80026144
        queueGpuPacket(tags_1f8003d0.getPointer() + MEMORY.ref(4, s1).offset(0xcL).get() * 0x4L, s0);
        final long a1 = gpuPacketAddr_1f8003d8.get();
        gpuPacketAddr_1f8003d8.addu(0x8L);
        MEMORY.ref(1, a1).offset(0x3L).setu(0x1L);
        MEMORY.ref(4, a1).offset(0x4L).setu(0xe100_0200L | (texPages_800bb110.get(TexPageBpp.BITS_4).get(TexPageTrans.HALF_B_PLUS_HALF_F).get(TexPageY.Y_256).get() | 0xdL) & 0x9ffL);
        queueGpuPacket(tags_1f8003d0.getPointer() + MEMORY.ref(4, s1).offset(0xcL).get() * 0x4L, a1);
      }
    }

    //LAB_800261a0
  }

  @Method(0x800261c0L)
  public static void renderTextboxBorder(final int textboxIndex, final long backgroundPacket) {
    final short[] sp0x10 = new short[4];
    final short[] sp0x18 = new short[4];
    short v0 = (short)(MEMORY.ref(2, backgroundPacket).offset(0x8L).get() + 4);
    sp0x10[0] = v0;
    sp0x10[2] = v0;
    v0 = (short)(MEMORY.ref(2, backgroundPacket).offset(0x10L).get() - 4);
    sp0x10[1] = v0;
    sp0x10[3] = v0;
    v0 = (short)(MEMORY.ref(2, backgroundPacket).offset(0xaL).get() + 5);
    sp0x18[0] = v0;
    sp0x18[1] = v0;
    v0 = (short)(MEMORY.ref(2, backgroundPacket).offset(0x1aL).get() - 5);
    sp0x18[2] = v0;
    sp0x18[3] = v0;

    final Struct4c s2 = _800be358.get(textboxIndex);

    //LAB_800262e4
    for(int s3 = 0; s3 < 8; s3++) {
      final long s0 = gpuPacketAddr_1f8003d8.get();
      gpuPacketAddr_1f8003d8.addu(0x28L);
      setGp0_2c(s0);
      gpuLinkedListSetCommandTransparency(s0, false);
      MEMORY.ref(1, s0).offset(0x6L).setu(0x80);
      MEMORY.ref(1, s0).offset(0x5L).setu(0x80);
      MEMORY.ref(1, s0).offset(0x4L).setu(0x80);
      short a0 = (short)_800108b0.offset(s3 * 0xcL).offset(2, 0x8L).getSigned();
      short a1 = (short)_800108b0.offset(s3 * 0xcL).offset(2, 0xaL).getSigned();
      if((s2._08.get() & 0x1) != 0) {
        a0 = (short)(s2._20.get() * a0 / 0x1000);
        a1 = (short)(s2._22.get() * a1 / 0x1000);
      }

      //LAB_8002637c
      final int u = (int)_800108b0.offset(s3 * 0xcL).offset(1, 0x4L).get();
      final int v = (int)_800108b0.offset(s3 * 0xcL).offset(1, 0x6L).get();
      final int left = sp0x10[(int)_800108b0.offset(s3 * 0xcL).offset(2, 0x0L).get()] - a0;
      final int right = sp0x10[(int)_800108b0.offset(s3 * 0xcL).offset(2, 0x2L).get()] + a0;
      final int top = sp0x18[(int)_800108b0.offset(s3 * 0xcL).offset(2, 0x0L).get()] - a1;
      final int bottom = sp0x18[(int)_800108b0.offset(s3 * 0xcL).offset(2, 0x2L).get()] + a1;
      MEMORY.ref(2, s0).offset(0x08L).setu(left); // X0
      MEMORY.ref(2, s0).offset(0x0aL).setu(top); // Y0
      MEMORY.ref(1, s0).offset(0x0cL).setu(u); // U0
      MEMORY.ref(1, s0).offset(0x0dL).setu(v); // V0
      MEMORY.ref(2, s0).offset(0x0eL).setu(0x7934L); // CLUT
      MEMORY.ref(2, s0).offset(0x10L).setu(right); // X1
      MEMORY.ref(2, s0).offset(0x12L).setu(top); // Y1
      MEMORY.ref(1, s0).offset(0x14L).setu(u + 16); // U1
      MEMORY.ref(1, s0).offset(0x15L).setu(v); // V1
      MEMORY.ref(2, s0).offset(0x16L).setu(GetTPage(TexPageBpp.BITS_4, TexPageTrans.HALF_B_PLUS_HALF_F, 896, 256)); // TPAGE
      MEMORY.ref(2, s0).offset(0x18L).setu(left); // X2
      MEMORY.ref(2, s0).offset(0x1aL).setu(bottom); // Y2
      MEMORY.ref(1, s0).offset(0x1cL).setu(u); // U2
      MEMORY.ref(1, s0).offset(0x1dL).setu(v + 16); // V2
      MEMORY.ref(2, s0).offset(0x20L).setu(right); // X3
      MEMORY.ref(2, s0).offset(0x22L).setu(bottom); // Y3
      MEMORY.ref(1, s0).offset(0x24L).setu(u + 16); // U3
      MEMORY.ref(1, s0).offset(0x25L).setu(v + 16); // V3
      queueGpuPacket(tags_1f8003d0.deref().get(s2.z_0c.get()).getAddress(), s0);
    }
  }

  /** I think this method handles textboxes */
  @Method(0x800264b0L)
  public static void FUN_800264b0(final int a0) {
    long v0;
    long v1;
    long s1;
    long s3;

    final Struct4c struct4c = _800be358.get(a0);
    final Struct84 struct84 = _800bdf38.get(a0);

    v1 = struct84._00.get();
    if(v1 == 0x1) {
      //LAB_8002663c
      if((struct84._08.get() & 0x1L) == 0) {
        switch(struct84._04.get()) {
          case 0:
            struct84._00.set(0xc);
            break;

          case 2:
            struct84._00.set(0xa);
            struct84._08.or(0x1L);
            struct84._2a.set((short)1);
            struct84._34.set((short)0);
            struct84._36.set(struct84._1e.get());
            break;

          case 3:
            struct84._00.set(0x17);
            struct84._08.or(0x1L);
            struct84._2a.set((short)1);
            struct84._34.set((short)0);
            struct84._36.set((short)0);
            struct84._64.set(0xa);
            struct84._78.set(0x11);
            Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
            break;

          case 4:
            //LAB_80026780
            do {
              FUN_800274f0(a0);
            } while((struct84._08.get() & 0x400L) == 0);

            struct84._08.xor(0x400L);
            // Fall through

          default:
            //LAB_800267a0
            struct84._00.set(0x4);
            break;
        }
      }
    } else if(v1 == 0x2) {
      struct84._00.set(0x4);
      //LAB_80026538
    } else if(v1 == 0x4) {
      //LAB_800267c4
      FUN_800274f0(a0);
    } else if(v1 == 0x5) {
      //LAB_800267d4
      if((struct84._08.get() & 0x1L) != 0) {
        //LAB_800267f4
        if(struct84._3a.get() >= struct84._1e.get() - ((struct84._08.get() & 0x200L) == 0 ? 1 : 2)) {
          struct84._08.xor(0x1L);
          struct84._3a.set((short)0);
          FUN_80029920(a0, 0x1L);
        } else {
          //LAB_80026828
          struct84._00.set(0x9);
          struct84._3a.incr();
          FUN_80028828(a0);
        }
      } else {
        //LAB_8002684c
        if((struct84._08.get() & 0x20L) != 0) {
          struct84._00.set(0x9);
          struct84._08.or(0x1L);
        } else {
          //LAB_8002686c
          if((joypadPress_8007a398.get() & 0x20L) != 0) {
            FUN_80029920(a0, 0);

            v1 = struct84._04.get();
            if(v1 == 1 || v1 == 4) {
              //LAB_800268b4
              struct84._00.set(0x9);
              struct84._08.or(0x1L);
            }

            if(v1 == 2) {
              //LAB_800268d0
              struct84._00.set(0xa);
            }
          }
        }
      }
    } else if(v1 == 0x6) {
      //LAB_800268dc
      if((joypadPress_8007a398.get() & 0x20L) != 0) {
        struct84._00.set(0x4);
      }
    } else if(v1 == 0x7) {
      //LAB_800268fc
      struct84._40.incr();
      if(struct84._40.get() >= struct84._3e.get()) {
        struct84._40.set((short)0);
        struct84._00.set(0x4);
      }

      //LAB_80026928
      if((struct84._08.get() & 0x20L) == 0) {
        if((joypadInput_8007a39c.get() & 0x20L) != 0) {
          s3 = 0;

          //LAB_80026954
          for(s1 = 0; s1 < 4; s1++) {
            FUN_800274f0(a0);

            v1 = struct84._00.get();
            if(v1 < 0x7L || v1 == 0xfL || v1 == 0xbL || v1 == 0xdL) {
              //LAB_8002698c
              s3 = 0x1L;
              break;
            }

            //LAB_80026994
          }

          //LAB_800269a0
          if(s3 == 0) {
            struct84._40.set((short)0);
            struct84._00.set(0x4);
          }
        }
      }
    } else if(v1 == 0x8) {
      //LAB_800269cc
      if(struct84._44.get() > 0) {
        //LAB_800269e8
        struct84._44.decr();
      } else {
        //LAB_800269e0
        struct84._00.set(0x4);
      }
      //LAB_80026554
    } else if(v1 == 0x9) {
      //LAB_800269f0
      FUN_80028828(a0);
      //LAB_80026580
    } else if(v1 == 0xa) {
      //LAB_80026a00
      FUN_800288a4(a0);

      if((struct84._08.get() & 0x4L) != 0) {
        struct84._08.xor(0x4L);
        if((struct84._08.get() & 0x2L) == 0) {
          //LAB_80026a5c
          do {
            FUN_800274f0(a0);
            v1 = struct84._00.get();
            if(v1 == 0xf) {
              struct84._3a.set((short)0);
              struct84._08.or(0x2L);
            }
          } while(v1 != 0x5 && v1 != 0xf);

          //LAB_80026a8c
          struct84._00.set(0xa);
        } else {
          struct84._3a.incr();

          if(struct84._3a.get() >= struct84._1e.get() + 1) {
            free(struct84.ptr_58.get());
            struct84._00.set(0);
          }
        }
      }
    } else if(v1 == 0xb) {
      //LAB_80026a98
      if((joypadPress_8007a398.get() & 0x20L) != 0) {
        FUN_80029920(a0, 0);
        FUN_8002a2b4(a0);

        struct84._00.set(0x4);
        struct84._08.xor(0x1L);
        struct84._34.set((short)0);
        struct84._36.set((short)0);
        struct84._3a.set((short)0);

        if((struct84._08.get() & 0x8L) != 0) {
          struct84._00.set(0xd);
        }
      }
    } else if(v1 == 0xc) {
      //LAB_80026af0
      if(struct4c._00.get() == 0) {
        free(struct84.ptr_58.get());
        struct84._00.set(0);
      }
    } else if(v1 == 0xd) {
      //LAB_80026b34
      struct84._08.or(0x8L);
      FUN_80029920(a0, 0x1L);

      //LAB_80026b4c
      do {
        FUN_800274f0(a0);
        v1 = struct84._00.get();
        if(v1 == 0x5) {
          //LAB_80026b28
          struct84._00.set(0xb);
          break;
        }
      } while(v1 != 0xf);

      //LAB_80026b6c
      if((struct84._08.get() & 0x20L) != 0) {
        FUN_80029920(a0, 0);
      }

      //LAB_80026ba0
      if(struct84._3e.get() != 0) {
        FUN_80029920(a0, 0);
        struct84._5c.set(struct84._00.get());
        struct84._00.set(0xe);
      }

      //LAB_80026bc8
      if((struct84._08.get() & 0x800L) != 0) {
        FUN_80029920(a0, 0);
        struct84._00.set(0x17);
        struct84._64.set(0xa);
        struct84._78.set(0x16);
        struct84._68.set(struct84._72.get());
        Scus94491BpeSegment.playSound(0, 4, 0, 0, (short)0, (short)0);
      }
    } else if(v1 == 0xe) {
      //LAB_80026c18
      if((struct84._08.get() & 0x40L) == 0) {
        struct84._40.decr();

        if(struct84._40.get() <= 0) {
          struct84._40.set(struct84._3e.get());

          v1 = struct84._5c.get();
          if(v1 == 0xbL) {
            //LAB_80026c70
            FUN_8002a2b4(a0);
            struct84._34.set((short)0);
            struct84._36.set((short)0);
            struct84._3a.set((short)0);
            struct84._00.set(0xd);
            struct84._08.xor(0x1L);
          } else if(v1 == 0xf) {
            //LAB_80026c98
            //LAB_80026c9c
            free(struct84.ptr_58.get());
            struct84._00.set(0);
          }
        }
      }
    } else if(v1 == 0xf) {
      //LAB_80026cb0
      if((struct84._08.get() & 0x20L) != 0) {
        struct84._00.set(0x10);
      } else {
        //LAB_80026cd0
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          free(struct84.ptr_58.get());
          struct84._00.set(0);
          FUN_80029920(a0, 0);
        }
      }
      //LAB_800265d8
    } else if(v1 == 0x10) {
      //LAB_80026cdc
      //LAB_80026ce8
      if((struct84._08.get() & 0x40L) != 0) {
        free(struct84.ptr_58.get());
        struct84._00.set(0);
        FUN_80029920(a0, 0);
      }
    } else if(v1 == 0x11) {
      //LAB_80026d20
      struct84._1e.incr();

      //LAB_80026d30
      do {
        FUN_800274f0(a0);
        v1 = struct84._00.get();
        if(v1 == 0x5L) {
          //LAB_80026d14
          struct84._00.set(0x12);
          break;
        }
        if(v1 == 0xfL) {
          struct84._00.set(0x12);
          struct84._3a.set((short)0);
          struct84._08.or(0x102L);
          break;
        }
      } while(true);

      //LAB_80026d64
      struct84._6c.set(-1);
      struct84._1e.decr();
      //LAB_8002659c
    } else if(v1 == 0x12) {
      //LAB_80026d94
      FUN_80029140(a0, (short)struct84._60.get());

      if((joypadPress_8007a398.get() & 0x20L) != 0) {
        Scus94491BpeSegment.playSound(0, 2, 0, 0, (short)0, (short)0);
        free(struct84.ptr_58.get());
        struct84._00.set(0);
        struct84._6c.set(struct84._68.get());
      } else {
        //LAB_80026df0
        if((joypadInput_8007a39c.get() & 0x4000L) == 0) {
          //LAB_80026ee8
          if((joypadInput_8007a39c.get() & 0x1000L) != 0) {
            if((struct84._08.get() & 0x100L) == 0 || struct84._68.get() != 0) {
              //LAB_80026f38
              Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

              s3 = 0x3L;
              if(struct84._60.get() > 0) {
                struct84._00.set(0x13);
                struct84._60.sub(0x1);
                struct84._64.set(0x4);
                struct84._68.decr();
              } else {
                //LAB_80026f88
                if((struct84._08.get() & 0x2L) != 0) {
                  v1 = struct84._3a.get();

                  // TODO not sure about this block of code
                  if(v1 == 1) {
                    s3 = 0x1L;
                  } else {
                    if(v1 == 0) {
                      //LAB_80026fbc
                      s3 = 0x2L;
                    }

                    //LAB_80026fc0
                    struct84._3a.set((short)0);
                    struct84._08.xor(0x2L);
                  }

                  //LAB_80026fe8
                  struct84._3a.sub((short)1);
                }

                //LAB_80027014
                struct84._68.decr();

                if(struct84._68.get() < 0) {
                  struct84._68.set(0);
                } else {
                  //LAB_80027044
                  struct84._2c.set((short)12);
                  FUN_800280d4(a0);

                  final LodString str = struct84.str_24.deref();

                  //LAB_80027068
                  s1 = 0;
                  do {
                    if(str.charAt(struct84._30.get() - 1) >>> 8 == 0xa1) {
                      s1++;
                    }

                    //LAB_80027090
                    if(s1 == struct84._1e.get() + s3) {
                      break;
                    }

                    struct84._30.decr();
                  } while(struct84._30.get() > 0);

                  //LAB_800270b0
                  struct84._34.set((short)0);
                  struct84._36.set((short)0);
                  struct84._08.or(0x80L);

                  //LAB_800270dc
                  do {
                    FUN_800274f0(a0);
                  } while(struct84._36.get() == 0 && struct84._00.get() != 0x5);

                  //LAB_80027104
                  struct84._00.set(0x15);
                  struct84._08.xor(0x80L);
                }
              }
            }
          }
        }

        struct84._00.set(0x13);
        struct84._60.add(0x1);
        struct84._64.set(0x4);
        struct84._68.incr();
        if((struct84._08.get() & 0x100L) == 0 || struct84._36.get() + 1 != struct84._68.get()) {
          //LAB_80026e68
          //LAB_80026e6c
          if(struct84._60.get() < struct84._1e.get()) {
            //LAB_80026ed0
            Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

            //LAB_80026ee8
            if((joypadInput_8007a39c.get() & 0x1000L) != 0) {
              if((struct84._08.get() & 0x100L) == 0 || struct84._68.get() != 0) {
                //LAB_80026f38
                Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);

                s3 = 0x3L;
                if(struct84._60.get() > 0) {
                  struct84._00.set(0x13);
                  struct84._60.sub(0x1);
                  struct84._64.set(0x4);
                  struct84._68.decr();
                } else {
                  //LAB_80026f88
                  if((struct84._08.get() & 0x2L) != 0) {
                    v1 = struct84._3a.get();

                    // TODO not sure about this block of code
                    if(v1 == 1) {
                      s3 = 0x1L;
                    } else {
                      if(v1 == 0) {
                        //LAB_80026fbc
                        s3 = 0x2L;
                      }

                      //LAB_80026fc0
                      struct84._3a.set((short)0);
                      struct84._08.xor(0x2L);
                    }

                    //LAB_80026fe8
                    struct84._3a.sub((short)1);
                  }

                  //LAB_80027014
                  struct84._68.decr();

                  if(struct84._68.get() < 0) {
                    struct84._68.set(0);
                  } else {
                    //LAB_80027044
                    struct84._2c.set((short)12);
                    FUN_800280d4(a0);

                    final LodString str = struct84.str_24.deref();

                    //LAB_80027068
                    s1 = 0;
                    do {
                      if(str.charAt(struct84._30.get() - 1) >>> 8 == 0xa1) {
                        s1++;
                      }

                      //LAB_80027090
                      if(s1 == struct84._1e.get() + s3) {
                        break;
                      }

                      struct84._30.decr();
                    } while(struct84._30.get() > 0);

                    //LAB_800270b0
                    struct84._34.set((short)0);
                    struct84._36.set((short)0);
                    struct84._08.or(0x80L);

                    //LAB_800270dc
                    do {
                      FUN_800274f0(a0);
                    } while(struct84._36.get() == 0 && struct84._00.get() != 0x5);

                    //LAB_80027104
                    struct84._00.set(0x15);
                    struct84._08.xor(0x80L);
                  }
                }
              }
            }
          } else {
            struct84._60.set(_800bdf38.get(a0)._1e.get() - 1);
            struct84._00.set(0x14);
            struct84._2c.set((short)0);

            if(struct84._3a.get() == 1) {
              struct84._00.set(0x12);
              struct84._68.decr();
            }
          }
        } else {
          struct84._00.set(0x3);
          struct84._60.decr();
          struct84._68.decr();
        }
      }
    } else if(v1 == 0x13) {
      //LAB_8002711c
      FUN_80029140(a0, (short)struct84._68.get());
      struct84._64.decr();

      if(struct84._64.get() == 0) {
        struct84._00.set(0x12);

        if((struct84._08.get() & 0x800L) != 0) {
          struct84._00.set(0x16);
        }
      }
    } else if(v1 == 0x14) {
      //LAB_8002715c
      struct84._2c.add((short)4);

      if(struct84._2c.get() >= 12) {
        FUN_80027eb4(a0);
        struct84._08.or(0x4L);
        struct84._2c.sub((short)0xc);
        struct84._36.set(struct84._1e.get());
      }

      //LAB_800271a8
      if((struct84._08.get() & 0x4L) != 0) {
        struct84._08.xor(0x4L);

        if((struct84._08.get() & 0x2L) == 0) {
          //LAB_8002720c
          //LAB_80027220
          do {
            FUN_800274f0(a0);

            v1 = struct84._00.get();
            if(v1 == 0xf) {
              struct84._3a.set((short)0);
              struct84._08.or(0x2L);
              break;
            }
          } while(v1 != 0x5);
        } else {
          struct84._3a.incr();
          if(struct84._3a.get() >= struct84._1e.get() + 1) {
            free(struct84.ptr_58.get());
            struct84._00.set(0);
          }
        }

        //LAB_80027250
        //LAB_80027254
        struct84._00.set(0x12);
      }
      //LAB_800265f4
    } else if(v1 == 0x15) {
      //LAB_8002727c
      struct84._2c.sub((short)4);

      if(struct84._2c.get() <= 0) {
        struct84._36.set((short)0);
        struct84._2c.set((short)0);
        struct84._00.set(0x12);
        struct84._08.or(0x4L);
      }

      //LAB_800272b0
      if((struct84._08.get() & 0x4L) != 0) {
        final LodString str = struct84.str_24.deref();

        //LAB_800272dc
        s1 = 0;
        do {
          v0 = str.charAt(struct84._30.get() + 1) >>> 8;
          if(v0 == 0xa0L) {
            //LAB_80027274
            struct84._30.decr();
            break;
          }

          if(v0 == 0xa1) {
            s1++;
          }

          //LAB_8002730c
          struct84._30.incr();
        } while(s1 != struct84._1e.get());

        //LAB_80027320
        struct84._00.set(0x12);
        struct84._30.add(2);
        struct84._34.set((short)0);
        struct84._36.set(struct84._1e.get());
      }
    } else if(v1 == 0x16) {
      //LAB_80027354
      FUN_80029140(a0, (short)struct84._68.get());

      if((joypadPress_8007a398.get() & 0x20L) != 0) {
        Scus94491BpeSegment.playSound(0, 2, 0, 0, (short)0, (short)0);
        free(struct84.ptr_58.get());
        struct84._00.set(0);
        struct84._6c.set(struct84._68.get() - struct84._72.get());
      } else {
        //LAB_800273bc
        if((joypadInput_8007a39c.get() & 0x1000L) != 0) {
          struct84._00.set(0x13);
          struct84._64.set(0x4);
          struct84._68.decr();

          if(struct84._68.get() < struct84._72.get()) {
            struct84._68.set(struct84._72.get());
            struct84._00.set(0x16);
          } else {
            //LAB_80027404
            Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);
          }
        }

        //LAB_80027420
        if((joypadInput_8007a39c.get() & 0x4000L) != 0) {
          struct84._00.set(0x13);
          struct84._64.set(0x4);
          struct84._68.incr();

          if(struct84._70.get() >= struct84._68.get()) {
            //LAB_80027480
            //LAB_80027490
            Scus94491BpeSegment.playSound(0, 1, 0, 0, (short)0, (short)0);
          } else {
            struct84._68.set(struct84._70.get());
            struct84._00.set(0x16);
          }
        }
      }
      //LAB_80026620
    } else if(v1 == 0x17) {
      //LAB_800274a4
      struct84._64.decr();
      if(struct84._64.get() == 0) {
        struct84._64.set(4);
        struct84._00.set(struct84._78.get());
      }
    }

    //LAB_800274c8
    FUN_8002a4c4(a0);
  }

  @Method(0x800274f0L)
  public static void FUN_800274f0(final long a0) {
    long v1;

    final Struct84 s0 = _800bdf38.get((int)a0);
    final LodString str = s0.str_24.deref();

    if((s0._08.get() & 0x10L) != 0) {
      final int s1 = (short)s0._80.get();
      FUN_8002a180(a0, s0._34.get(), s0._36.get(), s0._28.get(), s0._46.get(s1).get());

      s0._34.incr();
      s0._3c.incr();
      s0._80.incr();

      if(s0._34.get() < s0._1c.get()) {
        //LAB_80027768
        if(s0._46.get(s1 + 1).get() == -1) {
          s0._08.xor(0x10L);
        }
      } else if(s0._36.get() >= s0._1e.get() - 1) {
        if(s0._46.get(s1 + 1).get() != -1) {
          s0._00.set(0x5);
          s0._34.set((short)0);
          s0._36.incr();
          FUN_80029920((int)a0, 0x1L);
          return;
        }

        //LAB_80027618
        v1 = str.charAt(s0._30.get()) >>> 8;

        if(v1 == 0xa0) {
          //LAB_800276f4
          s0._00.set(0xf);

          //LAB_80027704
          FUN_80029920((int)a0, 0x1L);

          //LAB_80027740
          _800bdf38.get((int)a0)._08.xor(0x10L);
          return;
        }

        if(v1 == 0xa1) {
          s0._30.incr();
        }

        //LAB_8002764c
        s0._00.set(0x5);
        s0._34.set((short)0);
        s0._36.incr();

        //LAB_80027704
        FUN_80029920((int)a0, 0x1L);
      } else {
        //LAB_80027688
        s0._34.set((short)0);
        s0._36.incr();

        if(s0._46.get(s1 + 1).get() == -1) {
          v1 = str.charAt(s0._30.get()) >>> 8;
          if(v1 == 0xa0) {
            //LAB_800276f4
            s0._00.set(0xf);

            //LAB_80027704
            FUN_80029920((int)a0, 0x1L);
          } else {
            if(v1 == 0xa1) {
              //LAB_80027714
              s0._30.incr();
            }

            //LAB_80027724
            _800bdf38.get((int)a0)._00.set(0x7);
          }

          //LAB_80027740
          _800bdf38.get((int)a0)._08.xor(0x10L);
          return;
        }
      }

      //LAB_8002779c
      _800bdf38.get((int)a0)._00.set(0x7);
      return;
    }

    //LAB_800277bc
    long s1 = 0x1L;
    final long s7 = 0x1L;
    final long fp = 0x1L;

    //LAB_800277cc
    do {
      final int a0_0 = (int)str.charAt(s0._30.get());

      switch(a0_0 >>> 8) {
        case 0xa0 -> {
          s0._00.set(0xf);
          FUN_80029920((int)a0, 0x1L);
          s1 = 0;
        }

        case 0xa1 -> {
          s0._34.set((short)0);
          s0._36.incr();
          s0._08.or(0x400L);

          if(s0._36.get() >= s0._1e.get() || (s0._08.get() & 0x80L) != 0) {
            //LAB_80027880
            s0._00.set(0x5);

            if((s0._08.get() & 0x1L) == 0) {
              FUN_80029920((int)a0, 0x1L);
            }

            s1 = 0;
          }
        }

        case 0xa2 -> {
          //LAB_80027d28
          s0._00.set(0x6);

          //LAB_80027d2c
          s1 = 0;
        }

        case 0xa3 -> {
          FUN_80029920((int)a0, 0x1L);
          s0._00.set(0xb);

          if(str.charAt(s0._30.get() + 1) >>> 8 == 0xa1L) {
            s0._30.incr();
          }

          s1 = 0;
        }

        case 0xa5 -> {
          s0._3e.set((short)(a0_0 & 0xff));
          s0._40.set((short)0);
        }

        case 0xa6 -> {
          s0._00.set(0x8);
          s0._44.set((short)(60 / vsyncMode_8007a3b8.get() * (a0_0 & 0xff)));
          s1 = 0;
        }

        case 0xa7 -> {
          final long a2 = a0_0 & 0xf;

          //LAB_80027950
          s0._28.set((int)(a2 < 0xc ? a2 : 0));
        }

        case 0xa8 -> {
          s0._08.or(0x10L);

          //LAB_80027970
          for(int i = 0; i < 8; i++) {
            s0._46.get(i).set((short)-1);
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
            s0._46.get(i).set((short)sp0x18[(int)s1]);
          }

          //LAB_80027ae4
          s0._80.set(0);

          //LAB_80027d2c
          s1 = 0;
        }

        case 0xad -> {
          v1 = a0_0 & 0xff;

          if((int)v1 >= s0._1c.get()) {
            s0._34.set((short)(s0._1c.get() - 1));
          } else {
            //LAB_80027b0c
            s0._34.set((short)v1);
          }
        }

        case 0xae -> {
          v1 = a0_0 & 0xff;

          if((int)v1 >= s0._1e.get() - 1) {
            s0._36.set((short)(s0._1e.get() - 1));
          } else {
            //LAB_80027b38
            s0._36.set((short)v1);
          }
        }

        case 0xb0 -> {
          s0._00.set(0xd);

          final long v0 = 60 / (int)vsyncMode_8007a3b8.get() * (a0_0 & 0xff);
          s0._3e.set((short)v0);
          s0._40.set((short)v0);

          if(str.charAt(s0._30.get() + 1
          ) >>> 8 == 0xa1L) {
            s0._30.incr();
          }

          s1 = 0;
        }

        case 0xb1 -> s0._7c.set(a0_0 & 0xff);

        case 0xb2 -> {
          if((a0_0 & 0x1L) == 0x1L) {
            s0._08.or(0x1000L);
          } else {
            //LAB_80027bd0
            s0._08.xor(0x1000L);
          }
        }

        default -> {
          //LAB_80027be4
          FUN_8002a180(a0, s0._34.get(), s0._36.get(), s0._28.get(), (short)a0_0);

          s0._34.incr();
          s0._3c.incr();

          if(s0._34.get() < s0._1c.get()) {
            //LAB_80027d28
            s0._00.set(0x7);
          } else if(s0._36.get() >= s0._1e.get() - 1) {
            v1 = str.charAt(s0._30.get() + 1) >>> 8;

            if(v1 == 0xa0) {
              //LAB_80027c7c
              s0._00.set(0xf);
              FUN_80029920((int)a0, 0x1L);
            } else {
              if(v1 == 0xa1) {
                //LAB_80027c98
                s0._30.incr();
              }

              //LAB_80027c9c
              s0._00.set(0x5);
              s0._08.or(0x400L);
              s0._34.set((short)0);
              s0._36.incr();

              if((s0._08.get() & 0x1L) == 0) {
                FUN_80029920((int)a0, 0x1L);
              }
            }
          } else {
            //LAB_80027ce0
            s0._08.or(0x400L);
            s0._34.set((short)0);
            s0._36.incr();

            if(str.charAt(s0._30.get() + 1) >>> 8 == 0xa1L) {
              s0._30.incr();
            }

            //LAB_80027d28
            s0._00.set(0x7);
          }

          //LAB_80027d2c
          s1 = 0;
        }
      }

      //LAB_80027d30
      s0._30.incr();
    } while(s1 != 0);

    //LAB_80027d44
  }

  @Method(0x80027d74L)
  public static void FUN_80027d74(long a0, long a1, long a2) {
    long v0;
    long v1;
    final long a3;
    final long t0;
    long t1;
    long t2;
    long t3;
    final long t4;
    final long t5;

    t5 = a0;
    t2 = a1;
    t3 = a2;
    t1 = 0x136L;
    v0 = 0x8005_0000L;
    v1 = MEMORY.ref(4, v0).offset(-8928L).get();
    v0 = 0x5L;
    if(v1 != v0) {
      v1 = 0x800c_0000L;
    } else {
      v1 = 0x800c_0000L;
      t1 = 0x15eL;
    }

    //LAB_80027d9c
    v1 = v1 - 0x20c8L;
    v0 = t5 << 5;
    v0 = v0 + t5;
    v0 = v0 << 2;
    t0 = v0 + v1;
    v1 = MEMORY.ref(2, t0).offset(0x1cL).getSigned();
    a0 = MEMORY.ref(2, t0).offset(0x1eL).getSigned();
    v0 = v1 << 3;
    v0 = v0 + v1;
    v1 = v0 >>> 31;
    v0 = v0 + v1;
    a3 = (int)v0 >> 1;
    v1 = a1 - a3;
    a1 = a1 + a3;
    v0 = a0 << 1;
    v0 = v0 + a0;
    a0 = v0 << 1;
    t4 = a2 - a0;
    v1 = v1 << 16;
    v1 = (int)v1 >> 16;
    if((int)v1 >= 0xaL) {
      a2 = a2 + a0;
    } else {
      a2 = a2 + a0;
      t2 = a3 + 0xaL;
    }

    //LAB_80027dfc
    v0 = a1 << 16;
    v0 = (int)v0 >> 16;
    if((int)t1 >= (int)v0) {
      v0 = t4 << 16;
    } else {
      v0 = t4 << 16;
      t2 = t1 - a3;
    }

    //LAB_80027e14
    v0 = (int)v0 >> 16;
    if((int)v0 >= 0x12L) {
      v1 = a2 << 16;
    } else {
      v1 = a2 << 16;
      t3 = a0 + 0x12L;
    }

    //LAB_80027e28
    v1 = (int)v1 >> 16;
    v0 = 0xdeL;
    if((int)v0 >= (int)v1) {
      v0 = 0xdeL;
    } else {
      v0 = 0xdeL;
      t3 = v0 - a0;
    }

    //LAB_80027e40
    v1 = 0x800c_0000L;
    v1 = v1 - 0x1ca8L;
    v0 = t5 << 2;
    v0 = v0 + t5;
    v0 = v0 << 2;
    v0 = v0 - t5;
    v0 = v0 << 2;
    v0 = v0 + v1;
    MEMORY.ref(2, v0).offset(0x14L).setu(t2);
    MEMORY.ref(2, t0).offset(0x14L).setu(t2);
    MEMORY.ref(2, v0).offset(0x16L).setu(t3);
    v1 = MEMORY.ref(2, t0).offset(0x1cL).getSigned();
    a0 = MEMORY.ref(2, t0).offset(0x1eL).getSigned();
    MEMORY.ref(2, t0).offset(0x16L).setu(t3);
    v0 = v1 << 3;
    v0 = v0 + v1;
    v1 = v0 >>> 31;
    v0 = v0 + v1;
    v1 = MEMORY.ref(2, t0).offset(0x14L).get();
    v0 = (int)v0 >> 1;
    v1 = v1 - v0;
    v0 = a0 << 1;
    v0 = v0 + a0;
    MEMORY.ref(2, t0).offset(0x18L).setu(v1);
    v1 = t3;
    v0 = v0 << 1;
    v1 = v1 - v0;
    MEMORY.ref(2, t0).offset(0x1aL).setu(v1);
  }

  @Method(0x80027eb4L)
  public static void FUN_80027eb4(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    final long t2;
    final long t3;
    final long t4;
    final long t5;
    long t6;
    long lo;

    t3 = a0;
    v0 = 0x800c_0000L;
    t0 = v0 - 0x20c8L;
    a1 = t3 << 5;
    v0 = a1 + t3;
    v0 = v0 << 2;
    a0 = v0 + t0;
    v0 = MEMORY.ref(4, a0).offset(0x8L).get();
    v1 = MEMORY.ref(2, a0).offset(0x1eL).getSigned();
    a2 = MEMORY.ref(4, a0).offset(0x58L).get();
    v0 = v0 & 0x200L;
    a3 = v0 > 0 ? 1 : 0;
    if((int)a3 < (int)v1) {
      t4 = t0;
      t5 = a1;
      t2 = a0;

      //LAB_80027efc
      do {
        t1 = t5;
        v0 = MEMORY.ref(2, t2).offset(0x1cL).getSigned();

        if((int)v0 <= 0) {
          a1 = 0;
        } else {
          a1 = 0;
          t0 = a3 + 0x1L;
          a0 = t1 + t3;

          //LAB_80027f18
          do {
            a0 = a0 << 2;
            a0 = a0 + t4;
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x0L).get();
            v1 = v1 + a2;
            MEMORY.ref(2, v1).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x2L).get();
            v1 = v1 + a2;
            v0 = v0 - 0x1L;
            MEMORY.ref(2, v1).offset(0x2L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(1, v0).offset(0x4L).get();
            v1 = v1 + a2;
            MEMORY.ref(1, v1).offset(0x4L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x6L).get();
            v1 = v1 + a2;
            MEMORY.ref(2, v1).offset(0x6L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            a1 = a1 + 0x1L;
            a0 = t1 + t3;
          } while((int)a1 < (int)v0);
        }

        //LAB_80028038
        v0 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();
        a3 = a3 + 0x1L;
      } while((int)a3 < (int)v0);

    }
    v0 = 0x800c_0000L;

    //LAB_8002804c
    v0 = v0 - 0x20c8L;
    v1 = t3 << 5;
    v1 = v1 + t3;
    v1 = v1 << 2;
    v1 = v1 + v0;
    a0 = MEMORY.ref(2, v1).offset(0x1cL).getSigned();
    v0 = MEMORY.ref(2, v1).offset(0x1eL).getSigned();
    lo = (long)(int)a0 * (int)v0 & 0xffff_ffffL;
    a1 = lo;
    v0 = v0 + 0x1L;
    lo = (long)(int)a0 * (int)v0 & 0xffff_ffffL;
    v0 = a1 << 3;
    a0 = v0 + a2;
    a3 = lo;
    if((int)a1 < (int)a3) {
      a2 = v1;

      //LAB_80028098
      do {
        MEMORY.ref(2, a0).offset(0x0L).setu(0);
        MEMORY.ref(2, a0).offset(0x2L).setu(0);
        MEMORY.ref(1, a0).offset(0x4L).setu(0);
        MEMORY.ref(2, a0).offset(0x6L).setu(0);
        v0 = MEMORY.ref(2, a2).offset(0x1eL).getSigned();
        v1 = MEMORY.ref(2, a2).offset(0x1cL).getSigned();
        v0 = v0 + 0x1L;
        lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
        a1 = a1 + 0x1L;
        t6 = lo;
        a0 = a0 + 0x8L;
      } while((int)a1 < (int)t6);
    }

    //LAB_800280cc
  }

  @Method(0x800280d4L)
  public static void FUN_800280d4(long a0) {
    long v0;
    long v1;
    long a1;
    final long a2;
    long a3;
    long t0;
    long t1;
    final long t2;
    final long t3;
    final long t4;
    final long t5;
    long lo;

    t2 = a0;
    v0 = 0x800c_0000L;
    a0 = v0 - 0x20c8L;
    v1 = t2 << 5;
    v0 = v1 + t2;
    v0 = v0 << 2;
    v0 = v0 + a0;
    a3 = MEMORY.ref(2, v0).offset(0x1eL).getSigned();
    a2 = MEMORY.ref(4, v0).offset(0x58L).get();
    if((int)a3 > 0) {
      t3 = a0;
      t5 = v1;
      t4 = v0;

      //LAB_8002810c
      do {
        t1 = t5;
        v0 = MEMORY.ref(2, t4).offset(0x1cL).getSigned();

        if((int)v0 > 0) {
          a1 = 0;
          t0 = a3 - 0x1L;
          a0 = t1 + t2;

          //LAB_80028128
          do {
            a0 = a0 << 2;
            a0 = a0 + t3;
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x0L).get();
            v1 = v1 + a2;
            MEMORY.ref(2, v1).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x2L).get();
            v1 = v1 + a2;
            v0 = v0 + 0x1L;
            MEMORY.ref(2, v1).offset(0x2L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(1, v0).offset(0x4L).get();
            v1 = v1 + a2;
            MEMORY.ref(1, v1).offset(0x4L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            lo = (long)(int)a3 * (int)v0 & 0xffff_ffffL;
            v1 = lo;
            lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
            v1 = v1 + a1;
            v1 = v1 << 3;
            v0 = lo;
            v0 = v0 + a1;
            v0 = v0 << 3;
            v0 = v0 + a2;
            v0 = MEMORY.ref(2, v0).offset(0x6L).get();
            v1 = v1 + a2;
            MEMORY.ref(2, v1).offset(0x6L).setu(v0);
            v0 = MEMORY.ref(2, a0).offset(0x1cL).getSigned();
            a1 = a1 + 0x1L;
            a0 = t1 + t2;
          } while((int)a1 < (int)v0);
        }

        //LAB_80028248
        a3 = a3 - 0x1L;
      } while((int)a3 > 0);
    }

    //LAB_80028254
    v0 = 0x800c_0000L;
    v0 = v0 - 0x20c8L;
    v1 = t2 << 5;
    v1 = v1 + t2;
    v1 = v1 << 2;
    v1 = v1 + v0;
    v0 = MEMORY.ref(2, v1).offset(0x1cL).getSigned();

    if((int)v0 > 0) {
      a1 = 0;
      a0 = a2;

      //LAB_80028280
      do {
        MEMORY.ref(2, a0).offset(0x0L).setu(0);
        MEMORY.ref(2, a0).offset(0x2L).setu(0);
        MEMORY.ref(1, a0).offset(0x4L).setu(0);
        MEMORY.ref(2, a0).offset(0x6L).setu(0);
        v0 = MEMORY.ref(2, v1).offset(0x1cL).getSigned();
        a1 = a1 + 0x1L;
        a0 = a0 + 0x8L;
      } while((int)a1 < (int)v0);
    }

    //LAB_800282a4
  }

  @Method(0x800282acL)
  public static void FUN_800282ac(final int a0) {
    long v0;
    long v1;
    long a1;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long fp;
    long sp22;
    long sp44;
    final long sp10;
    long sp40;
    long sp20;
    long sp30;
    long sp18;
    final long sp14;
    long sp28;
    long sp38;

    final Struct84 s7 = _800bdf38.get(a0);

    if((s7._08.get() & 0x200L) != 0) {
      sp10 = s7._1c.get();
      sp14 = s7._1c.get() * 2;
    } else {
      sp10 = 0;
      sp14 = s7._1c.get();
    }

    sp38 = 0;

    //LAB_80028328
    fp = s7.ptr_58.get();

    //LAB_80028348
    for(s5 = 0; s5 < s7._1c.get() * (s7._1e.get() + 1); s5++) {
      if(MEMORY.ref(2, fp).offset(0x0L).getSigned() == 0) {
        sp38 = 0;
      }

      //LAB_8002835c
      if(MEMORY.ref(2, fp).offset(0x6L).getSigned() != 0) {
        s3 = 0;
        s2 = 0;
        s1 = 0;
        if((s7._08.get() & 0x1L) != 0) {
          if((int)s5 >= (int)sp10 && (int)s5 < (int)sp14) {
            v1 = s7._2c.get();
            s1 = -v1;
            s2 = -v1;
            s3 = v1;
          }

          //LAB_800283c4
          if((int)s5 >= s7._1c.get() * s7._1e.get() && (int)s5 < s7._1c.get() * (s7._1e.get() + 1)) {
            s1 = 0;
            s2 = 0;
            s3 = 0xcL - s7._2c.get();
          }
        }

        //LAB_8002840c
        FUN_8002a63c(MEMORY.ref(2, fp).offset(0x6L).get());
        s4 = _800be5c0.get() & 0xffff;
        s6 = gpuPacketAddr_1f8003d8.get();
        gpuPacketAddr_1f8003d8.addu(0x14L);
        sp18 = _800be5b8.get() & 0xffff;
        sp20 = _800be5c8.get() & 0xffff;
        sp22 = _800be5bc.get() & 0xffff;
        MEMORY.ref(1, s6).offset(0x3L).setu(0x4L);
        MEMORY.ref(4, s6).offset(0x4L).setu(0x6480_8080L);
        s0 = sp38 - FUN_8002a25c(MEMORY.ref(2, fp).offset(0x6L).get());
        MEMORY.ref(2, s6).offset(0x8L).setu(s7._18.get() + MEMORY.ref(2, fp).offset(0x0L).getSigned() * 9 - centreScreenX_1f8003dc.get() - s0);
        MEMORY.ref(2, s6).offset(0xaL).setu(s7._1a.get() + MEMORY.ref(2, fp).offset(0x2L).getSigned() * 12 - centreScreenY_1f8003de.get() - s7._2c.get() - s1);
        s0 = s0 + FUN_8002a1fc(MEMORY.ref(2, fp).offset(0x6L).get());
        sp38 = s0;
        if((s7._08.get() & 0x200L) != 0) {
          if((int)s5 >= 0) {
            if(s5 < s7._1c.get()) {
              MEMORY.ref(2, s6).offset(0xaL).setu(s7._1a.get() + MEMORY.ref(2, fp).offset(0x2L).getSigned() * 12 - centreScreenY_1f8003de.get() - s1);
            }
          }
        }

        //LAB_80028544
        //LAB_80028564
        v0 = s4 & 0xffffL;
        v0 = v0 << 4;
        sp40 = v0;

        MEMORY.ref(1, s6).offset(0xcL).setu(sp40);

        sp44 = (sp22 < 0x6L ? 0 : 0xf0L) + sp20 * 0xc - s2;

        MEMORY.ref(1, s6).offset(0xdL).setu(sp44);
        v1 = sp18 + 0x1e0L;
        s4 = v1 << 6;
        v0 = MEMORY.ref(1, fp).offset(0x4L).get() * 16;
        v0 = v0 + 0x340L;
        v0 = v0 & 0x3f0L;
        v0 = (int)v0 >> 4;
        v0 = s4 | v0;
        MEMORY.ref(2, s6).offset(0xeL).setu(v0);
        v0 = s3 << 16;
        v0 = (int)v0 >> 16;
        sp28 = MEMORY.ref(2, s6).offset(0x8L).get();
        if((int)v0 < 0xdL) {
          sp30 = MEMORY.ref(2, s6).offset(0xaL).get();
          s2 = 0xcL - s3;
          MEMORY.ref(2, s6).offset(0x10L).setu(0x8L);
          MEMORY.ref(2, s6).offset(0x12L).setu(s2);
          gpuLinkedListSetCommandTransparency(s6, false);
          queueGpuPacket(tags_1f8003d0.getPointer() + s7._0c.get() * 0x4L, s6);

          a1 = gpuPacketAddr_1f8003d8.get();
          gpuPacketAddr_1f8003d8.addu(0x8L);
          MEMORY.ref(1, a1).offset(0x3L).setu(0x1L);
          s1 = _80052bf4.offset(sp22 * 0x4L).getAddress();
          s0 = _80052bc8.offset(sp22 * 0x4L).getAddress();
          v0 = texPages_800bb110.get(TexPageBpp.BITS_4).get(TexPageTrans.HALF_B_PLUS_HALF_F).get(TexPageY.fromY((int)MEMORY.ref(4, s1).offset(0x0L).get())).get();
          v0 = v0 | (MEMORY.ref(4, s0).offset(0x0L).get() & 0x3c0L) >> 6;
          v0 = v0 & 0x9ffL;
          v0 = v0 | 0xe100_0200L;
          MEMORY.ref(4, a1).offset(0x4L).setu(v0);
          queueGpuPacket(tags_1f8003d0.getPointer() + s7._0c.get() * 0x4L, a1);

          s6 = gpuPacketAddr_1f8003d8.get();
          gpuPacketAddr_1f8003d8.addu(0x14L);
          MEMORY.ref(1, s6).offset(0x3L).setu(0x4L);
          MEMORY.ref(4, s6).offset(0x4L).setu(0x6480_8080L);
          MEMORY.ref(2, s6).offset(0x8L).setu(sp28 + 0x1L);
          MEMORY.ref(2, s6).offset(0xaL).setu(sp30 + 0x1L);
          MEMORY.ref(1, s6).offset(0xcL).setu(sp40);
          MEMORY.ref(2, s6).offset(0xeL).setu(s4 | 0x3dL);
          MEMORY.ref(2, s6).offset(0x12L).setu(s2);
          MEMORY.ref(1, s6).offset(0xdL).setu(sp44);
          MEMORY.ref(2, s6).offset(0x10L).setu(0x8L);
          gpuLinkedListSetCommandTransparency(s6, false);
          queueGpuPacket(tags_1f8003d0.getPointer() + s7._0c.get() * 0x4L + 0x4L, s6);
          a1 = gpuPacketAddr_1f8003d8.get();
          gpuPacketAddr_1f8003d8.addu(0x8L);
          MEMORY.ref(1, a1).offset(0x3L).setu(0x1L);
          v1 = MEMORY.ref(4, s0).offset(0x0L).get() & 0x3c0L;
          v0 = texPages_800bb110.get(TexPageBpp.BITS_4).get(TexPageTrans.HALF_B_PLUS_HALF_F).get(TexPageY.fromY((int)MEMORY.ref(4, s1).offset(0x0L).get())).get();
          v1 = (int)v1 >> 6;
          v0 = v0 | v1;
          v0 = v0 & 0x9ffL;
          v0 = v0 | 0xe100_0200L;
          MEMORY.ref(4, a1).offset(0x4L).setu(v0);
          queueGpuPacket(tags_1f8003d0.getPointer() + s7._0c.get() * 0x4L + 0x4L, a1);
        }
      }

      //LAB_800287d4
      fp = fp + 0x8L;
    }

    //LAB_800287f8
  }

  @Method(0x80028828L)
  public static void FUN_80028828(final int a0) {
    final long s0 = _800bdf38.get(a0).getAddress(); //TODO
    MEMORY.ref(2, s0).offset(0x2cL).addu(MEMORY.ref(2, s0).offset(0x2aL).get());
    if(MEMORY.ref(2, s0).offset(0x2cL).getSigned() >= 0xcL) {
      FUN_80027eb4(a0);
      MEMORY.ref(2, s0).offset(0x2cL).setu(0);
      MEMORY.ref(4, s0).offset(0x0L).setu(0x4L);
      MEMORY.ref(2, s0).offset(0x36L).subu(0x1L);
    }

    //LAB_80028894
  }

  @Method(0x800288a4L)
  public static void FUN_800288a4(final int a0) {
    if((_800bb0fc.get() & 0x1L) == 0) {
      final long s0 = _800bdf38.get(a0).getAddress(); //TODO
      MEMORY.ref(2, s0).offset(0x2cL).addu(MEMORY.ref(2, s0).offset(0x2aL).get() & 0x7L);

      if(MEMORY.ref(2, s0).offset(0x2cL).getSigned() >= 0xcL) {
        FUN_80027eb4(a0);
        MEMORY.ref(2, s0).offset(0x36L).setu(MEMORY.ref(2, s0).offset(0x1eL).get());
        MEMORY.ref(4, s0).offset(0x8L).oru(0x4L);
        MEMORY.ref(2, s0).offset(0x2cL).subu(0xcL);
      }
    }

    //LAB_80028928
  }

  @Method(0x80028938L)
  public static void FUN_80028938(long a0, long a1) {
    long v0;
    long v1;
    long a2;
    long a3;
    long t0;
    final long s0;
    long s1;
    long s2;
    final long s3;
    final long s4;
    final long s5;
    final long s6;
    final long s7;
    final long fp;
    final long sp10;
    final long sp24;
    final long sp20;
    final long sp18;
    final long sp58;
    final long sp14;
    final long sp1c;

    sp58 = a0;
    FUN_800e2428(a1);
    a0 = 0x800c_0000L;
    a0 = a0 + 0x68e8L;
    a1 = 0x800c_0000L;
    a1 = a1 - 0x1ca8L;
    v0 = MEMORY.ref(4, a0).offset(0x70L).get();
    a3 = sp58;
    a2 = MEMORY.ref(4, a0).offset(0x74L).get();
    v1 = MEMORY.ref(4, a0).offset(0x7cL).get();
    a0 = MEMORY.ref(4, a0).offset(0x68L).get();
    s4 = v0;
    v0 = a3 << 2;
    v0 = v0 + a3;
    v0 = v0 << 2;
    v0 = v0 - a3;
    v0 = v0 << 2;
    s3 = v0 + a1;
    v1 = a2 - v1;
    v0 = v1 >>> 31;
    v1 = v1 + v0;
    v1 = (int)v1 >> 1;
    a2 = a2 - v1;
    MEMORY.ref(4, s3).offset(0x28L).setu(s4);
    sp10 = v1;
    sp18 = a2;
    v1 = MEMORY.ref(2, s3).offset(0x18L).getSigned();
    a0 = s4 - a0;
    MEMORY.ref(4, s3).offset(0x2cL).setu(a2);
    sp14 = a0;
    v0 = v1 << 3;
    v0 = v0 + v1;
    v1 = v0 >>> 31;
    v0 = v0 + v1;
    v1 = MEMORY.ref(2, s3).offset(0x1aL).getSigned();
    v0 = (int)v0 >> 1;
    sp1c = v0;
    v0 = v1 << 1;
    v0 = v0 + v1;
    s7 = v0 << 1;
    v0 = 0x8005_0000L;
    v0 = MEMORY.ref(4, v0).offset(-8928L).get();
    t0 = 0x5L;
    if(v0 != t0) {
      fp = 0x140L;
    } else {
      fp = 0x168L;
    }

    //LAB_80028a20
    v0 = 0x800c_0000L;
    a3 = sp58;
    v0 = v0 - 0x20c8L;
    v1 = a3 << 5;
    v1 = v1 + a3;
    v1 = v1 << 2;
    s0 = v1 + v0;
    v0 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();

    if((int)v0 < 0x11L) {
      v0 = s4 << 16;
    } else {
      v0 = s4 << 16;
      t0 = sp18;

      if((int)t0 >= 0x79L) {
        //LAB_80028acc
        v0 = fp >>> 1;
        MEMORY.ref(2, s3).offset(0x14L).setu(v0);
        MEMORY.ref(2, s0).offset(0x14L).setu(v0);
        t0 = sp18;
        a3 = sp10;

        v0 = t0 - a3;
        v0 = v0 - s7;
        MEMORY.ref(2, s3).offset(0x16L).setu(v0);
        v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
        a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        MEMORY.ref(2, s0).offset(0x16L).setu(v0);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, s0).offset(0x14L).get();
        v0 = (int)v0 >> 1;
        v1 = v1 - v0;
        v0 = a0 << 1;
        v0 = v0 + a0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        v1 = MEMORY.ref(2, s0).offset(0x16L).get();
        v0 = v0 << 1;
        v1 = v1 - v0;
        v0 = 0x8L;

        //LAB_80028de4
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        MEMORY.ref(4, s3).offset(0x48L).setu(v0);
        return;
      }

      v0 = fp >>> 1;
      MEMORY.ref(2, s3).offset(0x14L).setu(v0);
      MEMORY.ref(2, s0).offset(0x14L).setu(v0);
      a3 = sp10;

      v0 = t0 + a3;
      v0 = v0 + s7;
      MEMORY.ref(2, s3).offset(0x16L).setu(v0);
      v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
      a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
      MEMORY.ref(2, s0).offset(0x16L).setu(v0);
      v0 = v1 << 3;
      v0 = v0 + v1;
      v1 = v0 >>> 31;
      v0 = v0 + v1;
      v1 = MEMORY.ref(2, s0).offset(0x14L).get();
      v0 = (int)v0 >> 1;
      v1 = v1 - v0;
      v0 = a0 << 1;
      v0 = v0 + a0;
      MEMORY.ref(2, s0).offset(0x18L).setu(v1);
      v1 = MEMORY.ref(2, s0).offset(0x16L).get();
      v0 = v0 << 1;
      v1 = v1 - v0;
      v0 = 0x7L;

      //LAB_80028de4
      MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
      MEMORY.ref(4, s3).offset(0x48L).setu(v0);
      return;
    }

    //LAB_80028b38
    s2 = (int)v0 >> 16;
    a1 = s2;
    t0 = sp18;
    a3 = sp10;
    a0 = sp58;
    t0 = t0 - a3;
    s1 = t0 - s7;
    a2 = s1 << 16;
    a2 = (int)a2 >> 16;
    sp20 = t0;
    v0 = FUN_80028f20(a0, a1, a2);
    a1 = s2;
    if(v0 != 0) {
      MEMORY.ref(2, s3).offset(0x14L).setu(s4);
      MEMORY.ref(2, s0).offset(0x14L).setu(s4);
      MEMORY.ref(2, s3).offset(0x16L).setu(s1);
      v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
      a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
      MEMORY.ref(2, s0).offset(0x16L).setu(s1);
      v0 = v1 << 3;
      v0 = v0 + v1;
      v1 = v0 >>> 31;
      v0 = v0 + v1;
      v1 = MEMORY.ref(2, s0).offset(0x14L).get();
      v0 = (int)v0 >> 1;
      v1 = v1 - v0;
      v0 = a0 << 1;
      v0 = v0 + a0;
      MEMORY.ref(2, s0).offset(0x18L).setu(v1);
      v1 = s1;
      v0 = v0 << 1;
      v1 = v1 - v0;
      MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
      MEMORY.ref(4, s3).offset(0x48L).setu(0);
      return;
    }

    //LAB_80028bc4
    t0 = sp18;
    a3 = sp10;
    a0 = sp58;
    t0 = t0 + a3;
    s1 = t0 + s7;
    a2 = s1 << 16;
    a2 = (int)a2 >> 16;
    sp24 = t0;
    v0 = FUN_80028f20(a0, a1, a2);
    if(v0 != 0) {
      MEMORY.ref(2, s3).offset(0x14L).setu(s4);
      MEMORY.ref(2, s0).offset(0x14L).setu(s4);
      MEMORY.ref(2, s3).offset(0x16L).setu(s1);
      v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
      a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
      MEMORY.ref(2, s0).offset(0x16L).setu(s1);
      v0 = v1 << 3;
      v0 = v0 + v1;
      v1 = v0 >>> 31;
      v0 = v0 + v1;
      v1 = MEMORY.ref(2, s0).offset(0x14L).get();
      v0 = (int)v0 >> 1;
      v1 = v1 - v0;
      v0 = a0 << 1;
      v0 = v0 + a0;
      MEMORY.ref(2, s0).offset(0x18L).setu(v1);
      v1 = s1;
      v0 = v0 << 1;
      v1 = v1 - v0;
      v0 = 0x1L;

      //LAB_80028de4
      MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
      MEMORY.ref(4, s3).offset(0x48L).setu(v0);
      return;
    }

    //LAB_80028c44
    v0 = fp >>> 1;
    if((int)v0 < (int)s4) {
      s5 = (int)s7 >> 1;
      //LAB_80028d58
      a0 = sp58;
      t0 = sp14;
      a3 = sp1c;
      v0 = s4 - t0;
      s2 = v0 - a3;
      v0 = s2 << 16;
      s6 = (int)v0 >> 16;
      t0 = sp20;
      a1 = s6;
      s1 = t0 - s5;
      a2 = s1 << 16;
      a2 = (int)a2 >> 16;
      v0 = FUN_80028f20(a0, a1, a2);
      a1 = s6;
      if(v0 != 0) {
        MEMORY.ref(2, s3).offset(0x14L).setu(s2);
        MEMORY.ref(2, s0).offset(0x14L).setu(s2);
        MEMORY.ref(2, s3).offset(0x16L).setu(s1);
        v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
        a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        MEMORY.ref(2, s0).offset(0x16L).setu(s1);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, s0).offset(0x14L).get();
        v0 = (int)v0 >> 1;
        v1 = v1 - v0;
        v0 = a0 << 1;
        v0 = v0 + a0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        v1 = s1;
        v0 = v0 << 1;
        v1 = v1 - v0;
        v0 = 0x4L;

        //LAB_80028de4
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        MEMORY.ref(4, s3).offset(0x48L).setu(v0);
        return;
      }

      //LAB_80028df0
      a3 = sp24;
      a0 = sp58;
      s1 = a3 + s5;
      a2 = s1 << 16;
      a2 = (int)a2 >> 16;
      v0 = FUN_80028f20(a0, a1, a2);
      t0 = 0x5L;
      if(v0 != 0) {
        MEMORY.ref(2, s3).offset(0x14L).setu(s2);
        MEMORY.ref(2, s0).offset(0x14L).setu(s2);
        MEMORY.ref(2, s3).offset(0x16L).setu(s1);
        v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
        a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        MEMORY.ref(2, s0).offset(0x16L).setu(s1);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, s0).offset(0x14L).get();
        v0 = (int)v0 >> 1;
        v1 = v1 - v0;
        v0 = a0 << 1;
        v0 = v0 + a0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        v1 = s1;
        v0 = v0 << 1;
        v1 = v1 - v0;
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        MEMORY.ref(4, s3).offset(0x48L).setu(t0);
        return;
      }
    } else {
      s5 = (int)s7 >> 1;
      a0 = sp58;
      t0 = sp14;
      a3 = sp1c;
      v0 = s4 + t0;
      s2 = v0 + a3;
      v0 = s2 << 16;
      s6 = (int)v0 >> 16;
      t0 = sp20;
      a1 = s6;
      s1 = t0 - s5;
      a2 = s1 << 16;
      a2 = (int)a2 >> 16;
      v0 = FUN_80028f20(a0, a1, a2);
      if(v0 == 0) {
        a1 = s6;
      } else {
        a1 = s6;
        MEMORY.ref(2, s3).offset(0x14L).setu(s2);
        MEMORY.ref(2, s0).offset(0x14L).setu(s2);
        MEMORY.ref(2, s3).offset(0x16L).setu(s1);
        v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
        a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        MEMORY.ref(2, s0).offset(0x16L).setu(s1);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, s0).offset(0x14L).get();
        v0 = (int)v0 >> 1;
        v1 = v1 - v0;
        v0 = a0 << 1;
        v0 = v0 + a0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        v1 = s1;
        v0 = v0 << 1;
        v1 = v1 - v0;
        v0 = 0x2L;

        //LAB_80028de4
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        MEMORY.ref(4, s3).offset(0x48L).setu(v0);
        return;
      }

      //LAB_80028ce4
      a3 = sp24;
      a0 = sp58;
      s1 = a3 + s5;
      a2 = s1 << 16;
      a2 = (int)a2 >> 16;
      v0 = FUN_80028f20(a0, a1, a2);
      if(v0 != 0) {
        MEMORY.ref(2, s3).offset(0x14L).setu(s2);
        MEMORY.ref(2, s0).offset(0x14L).setu(s2);
        MEMORY.ref(2, s3).offset(0x16L).setu(s1);
        v1 = MEMORY.ref(2, s0).offset(0x1cL).getSigned();
        a0 = MEMORY.ref(2, s0).offset(0x1eL).getSigned();
        MEMORY.ref(2, s0).offset(0x16L).setu(s1);
        v0 = v1 << 3;
        v0 = v0 + v1;
        v1 = v0 >>> 31;
        v0 = v0 + v1;
        v1 = MEMORY.ref(2, s0).offset(0x14L).get();
        v0 = (int)v0 >> 1;
        v1 = v1 - v0;
        v0 = a0 << 1;
        v0 = v0 + a0;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        v1 = s1;
        v0 = v0 << 1;
        v1 = v1 - v0;
        v0 = 0x3L;

        //LAB_80028de4
        MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
        MEMORY.ref(4, s3).offset(0x48L).setu(v0);
        return;
      }
    }

    //LAB_80028e68
    v0 = fp >>> 1;
    if((int)v0 >= (int)s4) {
      a3 = sp14;
      t0 = sp1c;
      v0 = s4 + a3;
      v0 = v0 + t0;
    } else {
      //LAB_80028e8c
      a3 = sp14;
      t0 = sp1c;
      v0 = s4 - a3;
      v0 = v0 - t0;
    }

    //LAB_80028e9c
    a1 = v0 << 16;
    a1 = (int)a1 >> 16;
    a3 = sp18;
    t0 = sp10;
    a0 = sp58;
    a2 = a3 + t0;
    a2 = a2 + s7;
    a2 = a2 << 16;
    a2 = (int)a2 >> 16;
    FUN_80027d74(a0, a1, a2);
    v1 = 0x800c_0000L;
    a3 = sp58;
    v1 = v1 - 0x1ca8L;
    v0 = a3 << 2;
    v0 = v0 + a3;
    v0 = v0 << 2;
    v0 = v0 - a3;
    v0 = v0 << 2;
    v0 = v0 + v1;
    v1 = 0x6L;
    MEMORY.ref(4, v0).offset(0x48L).setu(v1);

    //LAB_80028ef0
  }

  @Method(0x80028f20L)
  public static long FUN_80028f20(final long a0, final long a1, final long a2) {
    final long t1;
    if(mainCallbackIndex_8004dd20.get() == 0x5L) {
      t1 = 0x15eL;
    } else {
      t1 = 0x136L;
    }

    //LAB_80028f40
    //LAB_80028fa8
    //LAB_80028fc0
    //LAB_80028fd4
    if(
      a1 - _800be358.get((int)a0).width_18.get() * 9 / 2 < 0xaL ||
      a1 + _800be358.get((int)a0).width_18.get() * 9 / 2 > (int)t1 ||
      a2 - _800be358.get((int)a0).lines_1a.get() * 6 < 0x12L ||
      a2 + _800be358.get((int)a0).lines_1a.get() * 6 > 0xdeL
    ) {
      return 0;
    }

    //LAB_80028ff0
    return 0x1L;
  }

  @Method(0x80028ff8L)
  public static long FUN_80028ff8(final RunningScript a0) {
    FUN_800257e0(0);

    final Struct4c struct4c = _800be358.get(0);
    struct4c._04.set((short)_80052b8c.get());
    struct4c._14.set((short)260);
    struct4c._16.set((short)120);
    struct4c.width_18.set((short)6);
    struct4c.lines_1a.set((short)8);
    FUN_800258a8(0);

    final Struct84 struct84 = _800bdf38.get(0);
    struct84._04.set((short)_80052baa.get());
    struct84.str_24.set(_80052c20);
    struct84._08.or(0x40L);

    long addr = mallocHead(struct84._1c.get() * (struct84._1e.get() + 1) * 8);
    struct84.ptr_58.set(addr);
    //LAB_800290cc
    for(int i = 0; i < struct84._1c.get() * struct84._1e.get() + 1; i++) {
      MEMORY.ref(2, addr).offset(0x0L).setu(0);
      MEMORY.ref(2, addr).offset(0x2L).setu(0);
      MEMORY.ref(1, addr).offset(0x4L).setu(0);
      MEMORY.ref(2, addr).offset(0x6L).setu(0);
      addr = addr + 0x8L;
    }

    //LAB_80029100
    FUN_80027d74(0, struct84._14.get(), struct84._16.get());
    a0.params_20.get(0).deref().set(0);
    return 0;
  }

  @Method(0x80029140L)
  public static void FUN_80029140(long a0, long a1) {
    if(true)return;
    long v0;
    long v1;
    long a2;
    long a3;
    long t0;
    long s0;
    long s1;
    final long s2;
    long s3;
    final long s4;

    s4 = 0x1f80_0000L;
    s1 = MEMORY.ref(4, s4).offset(0x3d8L).get();
    s2 = a0;
    s3 = a1;
    a0 = s1;
    v0 = s1 + 0x18L;
    MEMORY.ref(4, s4).offset(0x3d8L).setu(v0);
    setGp0_28(a0);
    a0 = s1;
    v0 = 0x800c_0000L;
    v0 = v0 - 0x1ca8L;
    s0 = s2 << 2;
    s0 = s0 + s2;
    s0 = s0 << 2;
    s0 = s0 - s2;
    s0 = s0 << 2;
    s0 = s0 + v0;
    a2 = 0x1f80_0000L;
    s3 = s3 << 16;
    s3 = (int)s3 >> 16;
    v0 = MEMORY.ref(2, s0).offset(0x18L).getSigned();
    a3 = MEMORY.ref(2, a2).offset(0x3dcL).get();
    t0 = MEMORY.ref(2, s0).offset(0x1aL).getSigned();
    v0 = v0 - 0x1L;
    v1 = v0 << 3;
    v1 = v1 + v0;
    v0 = v1 >>> 31;
    v1 = v1 + v0;
    v1 = (int)v1 >> 0x1L;
    v0 = MEMORY.ref(2, s0).offset(0x14L).get();
    t0 = t0 - 0x1L;
    v0 = v0 - v1;
    v0 = v0 - a3;
    MEMORY.ref(2, s1).offset(0x8L).setu(v0);
    v0 = MEMORY.ref(2, s0).offset(0x14L).get();
    a3 = MEMORY.ref(2, a2).offset(0x3dcL).get();
    v0 = v0 - v1;
    v0 = v0 - a3;
    MEMORY.ref(2, s1).offset(0x10L).setu(v0);
    v0 = MEMORY.ref(2, s0).offset(0x14L).get();
    a3 = MEMORY.ref(2, a2).offset(0x3dcL).get();
    v0 = v0 + v1;
    v0 = v0 - a3;
    MEMORY.ref(2, s1).offset(0xcL).setu(v0);
    v0 = MEMORY.ref(2, s0).offset(0x14L).get();
    a3 = MEMORY.ref(2, a2).offset(0x3dcL).get();
    a2 = a2 + 0x3dcL;
    v0 = v0 + v1;
    v0 = v0 - a3;
    MEMORY.ref(2, s1).offset(0x14L).setu(v0);
    a3 = MEMORY.ref(2, s0).offset(0x16L).get();
    a2 = MEMORY.ref(2, a2).offset(0x2L).get();
    v0 = 0x80L;
    MEMORY.ref(1, s1).offset(0x4L).setu(v0);
    v0 = 0x32L;
    MEMORY.ref(1, s1).offset(0x5L).setu(v0);
    v0 = 0x64L;
    v1 = t0 << 0x1L;
    v1 = v1 + t0;
    v1 = v1 << 0x1L;
    MEMORY.ref(1, s1).offset(0x6L).setu(v0);
    v0 = s3 << 0x1L;
    v0 = v0 + s3;
    v0 = v0 << 2;
    a3 = a3 - v1;
    a3 = a3 - a2;
    v0 = v0 + a3;
    MEMORY.ref(2, s1).offset(0xaL).setu(v0);
    MEMORY.ref(2, s1).offset(0xeL).setu(v0);
    v0 = v0 + 0xcL;
    MEMORY.ref(2, s1).offset(0x12L).setu(v0);
    MEMORY.ref(2, s1).offset(0x16L).setu(v0);
    gpuLinkedListSetCommandTransparency(a0, true);
    a1 = s1;
    s1 = 0x1f80_0000L;
    a0 = MEMORY.ref(4, s0).offset(0xcL).get();
    v0 = MEMORY.ref(4, s1).offset(0x3d0L).get();
    a0 = a0 << 2;
    a0 = v0 + a0;
    queueGpuPacket(a0, a1);
    v1 = 0xe100_0000L;
    a1 = MEMORY.ref(4, s4).offset(0x3d8L).get();
    v1 = v1 | 0x200L;
    v0 = a1 + 0x8L;
    MEMORY.ref(4, s4).offset(0x3d8L).setu(v0);
    v0 = 0x1L;
    MEMORY.ref(1, a1).offset(0x3L).setu(v0);
    v0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, s1).offset(0x3d0L).get();
    v0 = MEMORY.ref(2, v0).offset(-20206L).get();
    a0 = a0 + 0x30L;
    v0 = v0 | 0xdL;
    v0 = v0 & 0x9ffL;
    v0 = v0 | v1;
    MEMORY.ref(4, a1).offset(0x4L).setu(v0);
    queueGpuPacket(a0, a1);
  }

  @Method(0x80029300L)
  public static void renderText(final LodString text, final int x, int y, final long a3, final long a4) {
    //LAB_80029358
    int length;
    for(length = 0; ; length++) {
      final long c = text.charAt(length);

      if(c == 0xa0ffL) {
        currentText_800bdca0.charAt(length, 0xffffL);
        break;
      }

      //LAB_80029374
      currentText_800bdca0.charAt(length, c);

      //LAB_80029384
    }

    final long s7 = MathHelper.clamp(a4, -12L, 0xcL);

    //LAB_800293bc
    //LAB_800293d8
    int lineIndex = 0;
    int glyphNudge = 0;

    for(int i = 0; i < length; i++) {
      final long c = currentText_800bdca0.charAt(i);

      if(c == 0xa1ff) {
        lineIndex = 0;
        glyphNudge = 0;
        y += 0xcL;
      } else {
        //LAB_80029404
        if(c < 0x340L) {
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
          _800be5c0.setu(c & 0xfL);
          _800be5c8.setu(0);
        }

        //LAB_800294b4
        final long fp = _800be5bc.get();

        final long packet1 = gpuPacketAddr_1f8003d8.get();
        gpuPacketAddr_1f8003d8.addu(0x14L);

        MEMORY.ref(1, packet1).offset(0x03L).setu(0x4L);
        MEMORY.ref(4, packet1).offset(0x04L).setu(0x6480_8080L); // Textured rect, variable size, opaque, texture-blending

        if(lineIndex == 0) {
          glyphNudge = 0;
        }

        //LAB_80029504
        //LAB_80029534
        if(c == 0x45L) {
          glyphNudge -= 1;
        } else if(c == 0x2L) {
          //LAB_80029548
          glyphNudge -= 2;
        } else if(c >= 0x5L && c < 0x7L) {
          //LAB_80029550
          glyphNudge -= 3;
        }

        //LAB_80029554
        //LAB_80029558
        MEMORY.ref(2, packet1).offset(0x08L).setu(x + lineIndex * 8 - centreScreenX_1f8003dc.get() - glyphNudge); // x

        glyphNudge += switch((int)c) {
          case 0x5, 0x23, 0x24, 0x2a, 0x37, 0x38, 0x3a, 0x3b, 0x3c, 0x3d, 0x3f, 0x40, 0x43, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4d, 0x4e, 0x51, 0x52 -> 0x1L;
          case 0x2, 0x8, 0x3e, 0x4c -> 0x2L;
          case 0xb, 0xc, 0x42 -> 0x3L;
          case 0x1, 0x3, 0x4, 0x9, 0x16, 0x41, 0x44 -> 0x4L;
          case 0x6, 0x27 -> 0x5L;
          default -> 0;
        };

        //LAB_800295d8
        lineIndex++;

        MEMORY.ref(2, packet1).offset(0x0aL).setu(y - centreScreenY_1f8003de.get()); // y

        final long v1;
        if(fp < 0x6L) {
          v1 = _800be5c8.get() * 0xcL;
        } else {
          v1 = 0xf0L + _800be5c8.get() * 0xcL;
        }

        //LAB_80029618
        if((short)s7 >= 0) {
          MEMORY.ref(1, packet1).offset(0x0dL).setu(v1); // v
          MEMORY.ref(2, packet1).offset(0x12L).setu(0xcL - s7); // height
        } else {
          //LAB_80029648
          MEMORY.ref(1, packet1).offset(0x0dL).setu(v1 - s7); // v
          MEMORY.ref(2, packet1).offset(0x12L).setu(s7 + 0xcL); // height
        }

        //LAB_80029658
        MEMORY.ref(1, packet1).offset(0x0cL).setu(_800be5c0.get() * 0x10L); // u
        MEMORY.ref(2, packet1).offset(0x0eL).setu((_800be5b8.get() + 0x1e0L) * 0x40L | ((a3 & 0xfL) * 0x10L + 0x340L & 0x3f0L) >> 4); // clut
        MEMORY.ref(2, packet1).offset(0x10L).setu(0x8L); // width
        gpuLinkedListSetCommandTransparency(packet1, false);
        queueGpuPacket(tags_1f8003d0.deref().get((int)_800bdf00.get()).getAddress(), packet1);

        final long packet2 = gpuPacketAddr_1f8003d8.get();
        gpuPacketAddr_1f8003d8.addu(0x8L);

        MEMORY.ref(1, packet2).offset(0x3L).setu(0x1L);
        MEMORY.ref(4, packet2).offset(0x4L).setu(0xe100_0200L | (texPages_800bb110.get(TexPageBpp.BITS_4).get(TexPageTrans.HALF_B_PLUS_HALF_F).get(TexPageY.fromY((int)_80052bf4.offset(fp * 0x4L).get())).get() | (_80052bc8.offset(fp * 0x4L).get() & 0x3c0L) >> 6) & 0x9ffL);
        queueGpuPacket(tags_1f8003d0.deref().get((int)_800bdf00.get()).getAddress(), packet2);
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
  public static void FUN_80029920(final int a0, final long a1) {
    if(a1 == 0) {
      _800bdea0.offset(a0 * 0xcL).setu(0);
    } else {
      //LAB_80029948
      _800bdea0.offset(a0 * 0xcL).oru(0x1L);
    }

    //LAB_80029970
    final Struct4c struct = _800be358.get(a0);
    final long v1 = _800bdea0.offset(a0 * 0xcL).getAddress();
    MEMORY.ref(2, v1).offset(0x4L).setu(struct._14.get());
    MEMORY.ref(2, v1).offset(0x8L).setu(0);
    MEMORY.ref(2, v1).offset(0x6L).setu(struct._16.get() + struct.lines_1a.get() * 6L);
  }

  @Method(0x800299d4L)
  public static void FUN_800299d4(final long a0) {
    long v0;
    long v1;
    final long s0;
    final long s1;
    final long s2;

    v0 = 0x800c_0000L;
    v0 = v0 - 0x2160L;
    v1 = a0 << 1;
    v1 = v1 + a0;
    v1 = v1 << 2;
    s1 = v1 + v0;
    v0 = MEMORY.ref(4, s1).offset(0x0L).get();

    v0 = v0 & 0x1L;
    if(v0 != 0) {
      v0 = 0x800c_0000L;
      v0 = v0 - 0x20c8L;
      v1 = a0 << 5;
      v1 = v1 + a0;
      v1 = v1 << 2;
      s2 = v1 + v0;
      if((MEMORY.ref(4, s2).offset(0x8L).get() & 0x1000) != 0) {
        s0 = gpuPacketAddr_1f8003d8.get();
        gpuPacketAddr_1f8003d8.addu(0x28L);
        setGp0_2c(s0);
        gpuLinkedListSetCommandTransparency(s0, true);
        MEMORY.ref(1, s0).offset(0x4L).setu(0x80);
        MEMORY.ref(1, s0).offset(0x5L).setu(0x80);
        MEMORY.ref(1, s0).offset(0x6L).setu(0x80);
        v1 = MEMORY.ref(2, s1).offset(0x4L).get() - centreScreenX_1f8003dc.get() - 8;
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        MEMORY.ref(2, s0).offset(0x8L).setu(v1);
        v1 = MEMORY.ref(2, s1).offset(0x4L).get() - centreScreenX_1f8003dc.get() + 8;
        MEMORY.ref(2, s0).offset(0x20L).setu(v1);
        MEMORY.ref(2, s0).offset(0x10L).setu(v1);
        v0 = MEMORY.ref(2, s1).offset(0x6L).get() - centreScreenY_1f8003de.get() - 6;
        MEMORY.ref(2, s0).offset(0x12L).setu(v0);
        MEMORY.ref(2, s0).offset(0xaL).setu(v0);
        v0 = MEMORY.ref(2, s1).offset(0x6L).get() - centreScreenY_1f8003de.get() + 8;
        MEMORY.ref(2, s0).offset(0x22L).setu(v0);
        MEMORY.ref(2, s0).offset(0x1aL).setu(v0);
        v0 = MEMORY.ref(2, s1).offset(0x8L).getSigned();
        v0 = v0 << 4;
        v0 = v0 + 0x40L;
        MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
        MEMORY.ref(1, s0).offset(0xcL).setu(v0);
        v1 = MEMORY.ref(2, s1).offset(0x8L).getSigned();
        MEMORY.ref(1, s0).offset(0x25L).setu(14);
        MEMORY.ref(1, s0).offset(0x1dL).setu(14);
        MEMORY.ref(1, s0).offset(0x15L).setu(0);
        MEMORY.ref(1, s0).offset(0xdL).setu(0);
        MEMORY.ref(2, s0).offset(0xeL).setu(0x793f);
        v1 = v1 << 4;
        v1 = v1 + 0x50L;
        MEMORY.ref(1, s0).offset(0x24L).setu(v1);
        MEMORY.ref(1, s0).offset(0x14L).setu(v1);
        MEMORY.ref(2, s0).offset(0x16L).setu(GetTPage(TexPageBpp.BITS_4, TexPageTrans.HALF_B_PLUS_HALF_F, 896, 256));
        queueGpuPacket(tags_1f8003d0.deref().get((int)MEMORY.ref(4, s2).offset(0xcL).get()).getAddress(), s0);
      }
    }

    //LAB_80029b50
  }

  @Method(0x80029b68L)
  public static long FUN_80029b68(final RunningScript a0) {
    //LAB_80029b7c
    for(int i = 0; i < 8; i++) {
      if(_800be358.get(i)._00.get() == 0 && _800bdf38.get(i)._00.get() == 0) {
        a0.params_20.get(0).deref().set(i);
        return 0;
      }

      //LAB_80029bac
      //LAB_80029bb0
    }

    a0.params_20.get(0).deref().set(-1);
    return 0;
  }

  @Method(0x80029bd4L)
  public static long FUN_80029bd4(final RunningScript a0) {
    final int s0 = a0.params_20.get(0).deref().get();
    FUN_800257e0(s0);

    final Struct4c struct4c = _800be358.get(s0);
    struct4c._04.set((short)a0.params_20.get(1).deref().get());
    struct4c._14.set((short)a0.params_20.get(2).deref().get());
    struct4c._16.set((short)a0.params_20.get(3).deref().get());
    struct4c.width_18.set((short)(a0.params_20.get(4).deref().get() + 1));
    struct4c.lines_1a.set((short)(a0.params_20.get(5).deref().get() + 1));
    return 0;
  }

  @Method(0x80029c98L)
  public static long FUN_80029c98(final RunningScript a0) {
    final int a2 = a0.params_20.get(0).deref().get();
    a0.params_20.get(1).deref().set((int)(_800be358.get(a2)._00.get() | _800bdf38.get(a2)._00.get()));
    return 0;
  }

  @Method(0x80029cf4L)
  public static long FUN_80029cf4(final RunningScript a0) {
    a0.params_20.get(1).deref().set((int)_800be358.get(a0.params_20.get(0).deref().get())._00.get());
    return 0;
  }

  @Method(0x80029d34L)
  public static long FUN_80029d34(final RunningScript a0) {
    a0.params_20.get(1).deref().set(_800bdf38.get(a0.params_20.get(0).deref().get())._00.get());
    return 0;
  }

  @Method(0x80029d6cL)
  public static long FUN_80029d6c(final RunningScript a0) {
    final int s1 = a0.params_20.get(0).deref().get();
    final Struct84 struct84 = _800bdf38.get(s1);

    if(struct84._00.get() != 0) {
      free(struct84.ptr_58.get());
    }

    //LAB_80029db8
    struct84._00.set(0);
    _800be358.get(s1)._00.set(0);
    FUN_80029920(s1, 0);
    return 0;
  }

  @Method(0x80029e04L)
  public static long FUN_80029e04(final RunningScript a0) {
    //LAB_80029e2c
    for(int i = 0; i < 8; i++) {
      final Struct4c s2 = _800be358.get(i);
      final Struct84 s0 = _800bdf38.get(i);

      if(s0._00.get() != 0) {
        free(s0.ptr_58.get());
      }

      //LAB_80029e48
      s0._00.set(0);
      s2._00.set(0);
      FUN_80029920(i, 0);
    }

    return 0;
  }

  @Method(0x80029e8cL)
  public static long FUN_80029e8c(final RunningScript a0) {
    _800bdf10.offset(Math.min(9, a0.params_20.get(0).deref().get()) * 0x4L).setu(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x80029eccL)
  public static long FUN_80029ecc(final RunningScript a0) {
    final Struct84 v1 = _800bdf38.get(a0.params_20.get(0).deref().get());
    if(v1._00.get() == 0x10L && (v1._08.get() & 0x20L) != 0) {
      v1._08.xor(0x20L);
    }

    //LAB_80029f18
    //LAB_80029f1c
    v1._08.or(0x40L);
    return 0;
  }

  @Method(0x80029f48L)
  public static long FUN_80029f48(final RunningScript a0) {
    a0.params_20.get(1).deref().set(_800bdf38.get(a0.params_20.get(0).deref().get())._6c.get());
    return 0;
  }

  @Method(0x80029f80L)
  public static long FUN_80029f80(final RunningScript a0) {
    a0.params_20.get(1).deref().set(_800bdf38.get(a0.params_20.get(0).deref().get())._7c.get());
    return 0;
  }

  @Method(0x8002a058L)
  public static void FUN_8002a058() {
    //LAB_8002a080
    for(int i = 0; i < 8; i++) {
      if(_800be358.get(i)._00.get() != 0) {
        FUN_80025a04(i);
      }

      //LAB_8002a098
      if(_800bdf38.get(i)._00.get() != 0) {
        FUN_800264b0(i); // Animates the textbox arrow
      }
    }

    FUN_80024994();
  }

  @Method(0x8002a0e4L)
  public static void FUN_8002a0e4() {
    //LAB_8002a10c
    for(int i = 0; i < 8; i++) {
      final Struct4c struct4c = _800be358.get(i);

      if(struct4c._00.get() != 0 && (int)struct4c._08.get() < 0) {
        renderTextboxBackground(i);
      }

      //LAB_8002a134
      if(_800bdf38.get(i)._00.get() != 0) {
        FUN_800282ac(i);
        FUN_800299d4(i);
      }

      //LAB_8002a154
    }
  }

  @Method(0x8002a180L)
  public static void FUN_8002a180(long a0, final long a1, final long a2, long a3, final long a4) {
    long v0;
    final long v1;

    v1 = _800bdf38.get((int)a0).getAddress();
    a0 = MEMORY.ref(2, v1).offset(0x36L).getSigned() * MEMORY.ref(2, v1).offset(0x1cL).getSigned() + MEMORY.ref(2, v1).offset(0x34L).getSigned();
    v0 = MEMORY.ref(4, v1).offset(0x58L).get() + a0 * 0x8L;
    MEMORY.ref(2, v0).offset(0x0L).setu(a1);
    MEMORY.ref(2, v0).offset(0x2L).setu(a2);

    if((MEMORY.ref(4, v1).offset(0x8L).get() & 0x200L) != 0 && (short)a2 == 0) {
      a3 = 0x8L;
    }

    //LAB_8002a1e8
    //LAB_8002a1ec
    v0 = MEMORY.ref(4, v1).offset(0x58L).get() + a0 * 0x8L;
    MEMORY.ref(1, v0).offset(0x4L).setu(a3);
    MEMORY.ref(2, v0).offset(0x6L).setu(a4);
  }

  @Method(0x8002a1fcL)
  public static long FUN_8002a1fc(final long a0) {
    //LAB_8002a254
    return switch((int)(a0 & 0xffff)) {
      case 0x5, 0x23, 0x24, 0x2a, 0x37, 0x38, 0x3a, 0x3b, 0x3c, 0x3d, 0x3f, 0x40, 0x43, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4d, 0x4e, 0x51, 0x52 -> 0x1L;
      case 0x2, 0x8, 0x3e, 0x4c -> 0x2L;
      case 0xb, 0xc, 0x42 -> 0x3L;
      case 0x1, 0x3, 0x4, 0x9, 0x16, 0x41, 0x44 -> 0x4L;
      case 0x6, 0x27 -> 0x5L;
      default -> 0;
    };
  }

  @Method(0x8002a25cL)
  public static long FUN_8002a25c(long a0) {
    a0 = a0 & 0xffffL;

    //LAB_8002a288
    if(a0 == 0x45L) {
      return 0x1L;
    }

    if(a0 == 0x2L) {
      //LAB_8002a29c
      return 0x2L;
    }

    if((int)a0 < 0x5L) {
      return 0;
    }

    if((int)a0 < 0x7L) {
      //LAB_8002a2a4
      return 0x3L;
    }

    //LAB_8002a2a8
    //LAB_8002a2ac
    return 0;
  }

  @Method(0x8002a2b4L)
  public static void FUN_8002a2b4(final int a0) {
    final Struct84 a2 = _800bdf38.get(a0);
    long a0_0 = a2.ptr_58.get();

    //LAB_8002a2f0
    for(int a1 = 0; a1 < a2._1c.get() * (a2._1e.get() + 1); a1++) {
      MEMORY.ref(2, a0_0).offset(0x0L).setu(0);
      MEMORY.ref(2, a0_0).offset(0x2L).setu(0);
      MEMORY.ref(1, a0_0).offset(0x4L).setu(0);
      MEMORY.ref(2, a0_0).offset(0x6L).setu(0);
      a0_0 = a0_0 + 0x8L;
    }

    //LAB_8002a324
  }

  @Method(0x8002a32cL)
  public static void FUN_8002a32c(final int a0, final long a1, final long a2, final long a3, final long a4, final long a5) {
    FUN_800257e0(a0);

    final Struct4c struct = _800be358.get(a0);
    struct._04.set((short)((a1 & 1) + 1));
    struct._06.set((short)1);
    struct._08.or(0x4L);

    struct._14.set((short)a2);
    struct._16.set((short)a3);
    struct.width_18.set((short)(a4 + 1));
    struct.lines_1a.set((short)(a5 + 1));
  }

  @Method(0x8002a3ecL)
  public static void FUN_8002a3ec(final int a0, final long a1) {
    if((a1 & 0x1L) == 0) {
      //LAB_8002a40c
      _800bdf38.get(a0)._00.set(0);
      _800be358.get(a0)._00.set(0);
    } else {
      //LAB_8002a458
      _800be358.get(a0)._00.set(0x3L);
    }
  }

  @Method(0x8002a488L)
  public static long FUN_8002a488(final long a0) {
    return _800be358.get((int)a0)._00.get() == 6 ? 1 : 0;
  }

  @Method(0x8002a4c4L)
  public static void FUN_8002a4c4(final long a0) {
    final long a1 = _800bdea0.offset(a0 * 0xcL).getAddress(); //TODO struct

    if((MEMORY.ref(4, a1).offset(0x0L).get() & 0x1L) != 0) {
      if((_800bdf38.get((int)a0)._08.get() & 0x1000L) != 0) {
        if((_800bb0fc.get() & 0x1L) == 0) {
          MEMORY.ref(2, a1).offset(0x8L).addu(0x1L);
        }

        //LAB_8002a53c
        if(MEMORY.ref(2, a1).offset(0x8L).getSigned() >= 0x7L) {
          MEMORY.ref(2, a1).offset(0x8L).setu(0);
        }
      }
    }

    //LAB_8002a554
  }

  @Method(0x8002a55cL)
  public static long textLen(final LodString str) {
    //LAB_8002a568
    int i;
    for(i = 0; str.charAt(i) < 0xa000L; i++) {
      // Empty
    }

    return i;
  }

  @Method(0x8002a59cL)
  public static int textWidth(final LodString a0) {
    //LAB_8002a5b4
    int a3 = 0;
    int v1;
    for(v1 = 0; a0.charAt(v1) <= 0x9fffL; v1++) {
      a3 += switch((int)a0.charAt(v1)) {
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
  public static void FUN_8002a63c(long a0) {
    long v0;
    long v1;
    final long a1;
    final long a2;
    final long a3;
    final long hi;

    a2 = a0;
    a0 = a2 & 0xffffL;
    if(a0 < 0x340L) {
      v1 = 0x800c_0000L;
      v0 = 0x4ec4_0000L;
      v0 = v0 | 0xec4fL;
      hi = (a0 & 0xffff_ffffL) * (v0 & 0xffff_ffffL) >>> 32;
      v0 = 0x800c_0000L;
      MEMORY.ref(4, v0).offset(-6724L).setu(0);
      v0 = a2 & 0xfL;
      MEMORY.ref(4, v1).offset(-6720L).setu(v0);
      a3 = hi;
      v1 = a3 >>> 6;
      v1 = v1 & 0xffffL;
      v0 = v1 << 1;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v0 = v0 + v1;
      v0 = v0 << 4;
      a0 = a0 - v0;
      v0 = 0x800c_0000L;
      MEMORY.ref(4, v0).offset(-6728L).setu(v1);
      if((int)a0 < 0) {
        a0 = a0 + 0xfL;
      }

      //LAB_8002a6a0
      v1 = (int)a0 >> 4;
      v0 = 0x800c_0000L;
      MEMORY.ref(4, v0).offset(-6712L).setu(v1);
      return;
    }

    //LAB_8002a6b0
    v0 = a0 - 0x340L;
    a1 = (int)v0 >> 4;
    if((int)a1 >= 0) {
      v0 = a1;
    } else {
      v0 = a1 + 0x3L;
    }

    //LAB_8002a6c4
    v0 = (int)v0 >> 2;
    a0 = v0 + 0x1L;
    v1 = 0x800c_0000L;
    MEMORY.ref(4, v1).offset(-6724L).setu(a0);
    v1 = 0x800c_0000L;
    v0 = v0 << 2;
    v0 = a1 - v0;
    MEMORY.ref(4, v1).offset(-6728L).setu(v0);
    v1 = 0x800c_0000L;
    v0 = a2 & 0xfL;
    MEMORY.ref(4, v1).offset(-6720L).setu(v0);
    v0 = 0x800c_0000L;
    MEMORY.ref(4, v0).offset(-6712L).setu(0);
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
    _80052c34.setu(0x4L);
    index_80052c38.set(0);
    _80052c3c.setu(-1L);
    _80052c40.setu(0);
    _80052c44.setu(0x2L);
  }

  @Method(0x8002aa04L)
  public static void FUN_8002aa04() {
    fillMemory(_800beb98.getAddress(), 0, 0x190);
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
        FUN_800e2220();

        _80052c44.setu(0x2L);
        break;

      case 0x4:
        FUN_800e519c();

      case 0x3:
        FUN_800e8e50();
        FUN_800e828c();
        FUN_800e4f8c();
        FUN_800e2220();
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

  @Method(0x8002ac24L)
  public static void FUN_8002ac24() {
    initFileEntries(_80052c4c.reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
  }

  @Method(0x8002ac48L)
  public static int loadDRGN2xBIN() {
    //LAB_8002ac6c
    for(int attempts = 0; attempts < 10; attempts++) {
      //LAB_8002ac74
      for(int i = 0; i < 4; ) {
        i++;

        if(DsSearchFile(new CdlFILE(), String.format("\\SECT\\DRGN2%d.BIN;1", i)) != null) {
          return i;
        }
      }
    }

    //LAB_8002acbc
    return -1;
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

  @Method(0x8002c86cL)
  public static void FUN_8002c86c() {
    if(_800bf0cd.get() != 0) {
      _800bf0c0.addu(_800bf0c4);
      _800bf0ce.setu(_800bf0c0).shra(0x10L);
      _800bf0c8.subu(0x1L);

      if(_800bf0c8.getSigned() <= 0) {
        _800bf0cd.setu(0);
        _800bf0ce.setu(_800bf0cc);
      }

      //LAB_8002c8c4
      setCdVolume((int)_800bf0ce.get(), (int)_800bf0ce.get());
      setCdMix(0x3f);
    }

    //LAB_8002c8dc
    if(_800bf0d8.get() == 0x1L) {
      fileLoadingCallbackIndex_8004ddc4.setu(0x15L);
    }

    //LAB_8002c8f4
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
    if(fileLoadingCallbackIndex_8004ddc4.get() != 0 || fileLoadingInfoArray_800bbad8.get(0).used.get() || xaFileIndex == 0 || xaFileIndex >= 32 || xaLoadingStage >= 5) {
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
      final CdlLOC pos = CdlFILE_800bb4c8.get((int)MEMORY.ref(2, v1).offset(xaArchiveIndex * 0x8L).getSigned()).pos;

      CDROM.playXaAudio(pos, 1, xaFileIndex, () -> _800bf0cf.setu(0));
      _800bf0cf.setu(4);
    }

    if(xaLoadingStage == 2) {
      _800bf0cf.setu(4);
    }

    return 0;
  }

  @Method(0x8002ced8L)
  public static void start(final int argc, final long argv) {
    for(int i = 0; i < 0x6c4b0; i += 4) {
      _8005a1d8.offset(i).setu(0);
    }

    _80052dc0.setu(ramSize_800e6f04.get() - 0x8L - stackSize_800e6f08.get() - _800c6688.getAddress());
    _80052dbc.setu(_800c6688.getAddress());

    main();

    assert !Hardware.isAlive() : "Shouldn't get here";
  }

  @Method(0x8002d12cL)
  public static long getTimerValue(final long timerIndex) {
    if(timerIndex >= 3) {
      return 0;
    }

    return TMR_DOTCLOCK_VAL.offset(timerIndex * 0x10L).get();
  }

  @Method(0x8002d220L)
  public static int strcmp(final String s1, final String s2) {
    return s1.compareToIgnoreCase(s2);
  }

  @Method(0x8002d230L)
  public static int strncmp(final String s1, final String s2, final int length) {
    return s1.substring(0, Math.min(s1.length(), length)).compareToIgnoreCase(s2.substring(0, Math.min(s2.length(), length)));
  }

  @Method(0x8002d240L)
  public static CString strcpy(final CString dest, final String src) {
    if(dest == null || src == null) {
      return null;
    }

    dest.set(src);
    return dest;
  }

  @Method(0x8002d260L)
  public static int rand() {
    GATE.acquire();
    final int res = Bios.rand_Impl_A2f();
    GATE.release();
    return res;
  }

  @Method(0x8002d270L)
  public static void srand(final long seed) {
    GATE.acquire();
    Bios.srand_Impl_A30(seed);
    GATE.release();
  }

  @Method(0x8002ff10L)
  public static long OpenEvent(final long desc, final int spec, final int mode, final long func) {
    GATE.acquire();
    final long res = Kernel.OpenEvent_Impl_B08(desc, spec, mode, func);
    GATE.release();
    return res;
  }

  @Method(0x8002ff40L)
  public static boolean EnableEvent(final long event) {
    GATE.acquire();
    final boolean res = Kernel.EnableEvent_Impl_B0c(event);
    GATE.release();
    return res;
  }
}
