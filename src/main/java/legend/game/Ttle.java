package legend.game;

import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.CharacterData2c;
import legend.game.types.GsRVIEW2;
import legend.game.types.TmdRenderingStruct;
import legend.game.types.Translucency;

import javax.annotation.Nullable;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.GPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getConsumerAddress;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.Scus94491BpeSegment.decrementOverlayCount;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.renderMcq;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022590;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002379c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a9c0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bcc8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bda4;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.hasSavedGames;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.diskNum_8004ddc0;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndexOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.setMonoOrStereo;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc05c;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bf0dc;
import static legend.game.Scus94491BpeSegment_800b._800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.gameOverMcq_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.identityMatrix_800c3568;

public final class Ttle {
  private Ttle() { }

  public static final Value _800c66c8 = MEMORY.ref(4, 0x800c66c8L);
  public static final Pointer<TmdRenderingStruct> _800c66d0 = MEMORY.ref(4, 0x800c66d0L, Pointer.deferred(4, TmdRenderingStruct::new));
  public static final ArrayRef<UnsignedIntRef> _800c66d4 = MEMORY.ref(4, 0x800c66d4L, ArrayRef.of(UnsignedIntRef.class, 4, 4, UnsignedIntRef::new));
  public static final Value hasSavedGames_800c66e4 = MEMORY.ref(4, 0x800c66e4L);
  public static final Value menuLoadingStage_800c66e8 = MEMORY.ref(4, 0x800c66e8L);
  public static final IntRef logoFadeInAmount_800c66ec = MEMORY.ref(4, 0x800c66ecL, IntRef::new);
  public static final Value logoFlashStage_800c66f0 = MEMORY.ref(4, 0x800c66f0L);
  public static final Value _800c66f4 = MEMORY.ref(4, 0x800c66f4L);
  public static final Value _800c66f8 = MEMORY.ref(4, 0x800c66f8L);
  public static final Value _800c66fc = MEMORY.ref(4, 0x800c66fcL);
  public static final Value _800c6700 = MEMORY.ref(2, 0x800c6700L);
  public static final Value _800c6702 = MEMORY.ref(2, 0x800c6702L);
  public static final Value backgroundInitialized_800c6704 = MEMORY.ref(4, 0x800c6704L);
  public static final IntRef backgroundScrollAmount_800c6708 = MEMORY.ref(4, 0x800c6708L, IntRef::new);
  public static final IntRef backgroundFadeInAmount_800c670c = MEMORY.ref(4, 0x800c670cL, IntRef::new);
  public static final Value copyrightInitialized_800c6710 = MEMORY.ref(4, 0x800c6710L);
  public static final IntRef copyrightFadeInAmount_800c6714 = MEMORY.ref(4, 0x800c6714L, IntRef::new);
  public static final Value logoFireInitialized_800c6718 = MEMORY.ref(4, 0x800c6718L);
  public static final Value _800c671c = MEMORY.ref(4, 0x800c671cL);
  public static final Value menuIdleTime_800c6720 = MEMORY.ref(4, 0x800c6720L);
  public static final Value _800c6724 = MEMORY.ref(4, 0x800c6724L);
  public static final Value _800c6728 = MEMORY.ref(4, 0x800c6728L);
  public static final Value _800c672c = MEMORY.ref(4, 0x800c672cL);
  public static final ArrayRef<ShortRef> menuOptionTransparency_800c6730 = MEMORY.ref(2, 0x800c6730L, ArrayRef.of(ShortRef.class, 3, 2, ShortRef::new));

  public static final Value _800c6738 = MEMORY.ref(4, 0x800c6738L);
  public static final Value _800c673c = MEMORY.ref(2, 0x800c673cL);

  public static final Value _800c6748 = MEMORY.ref(4, 0x800c6748L);

  public static final Value _800c6754 = MEMORY.ref(4, 0x800c6754L);
  public static final Value _800c6758 = MEMORY.ref(4, 0x800c6758L);

  public static final GsRVIEW2 GsRVIEW2_800c6760 = MEMORY.ref(4, 0x800c6760L, GsRVIEW2::new);

  /**
   * <ol start="0">
   *   <li>{@link Ttle#mainMenuStateSetUpNewGame()}</li>
   *   <li>{@link Ttle#FUN_800c74bc()}</li>
   *   <li>{@link Ttle#waitForTtleFilesToLoad()}</li>
   *   <li>{@link Ttle#FUN_800c7500()}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<RunnableRef>> loadingStageArray_800c6898 = MEMORY.ref(4, 0x800c6898L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 4, 4, Pointer.deferred(4, RunnableRef::new)));

  public static final SVECTOR _800c68f0 = MEMORY.ref(2, 0x800c68f0L, SVECTOR::new);
  public static final VECTOR _800c68f8 = MEMORY.ref(4, 0x800c68f8L, VECTOR::new);

  public static final ArrayRef<UnsignedShortRef> characterStartingLevels_800ce6c4 = MEMORY.ref(2, 0x800ce6c4L, ArrayRef.of(UnsignedShortRef.class, 9, 2, UnsignedShortRef::new));

  public static final Value _800ce6d8 = MEMORY.ref(4, 0x800ce6d8L);

  public static final Value _800ce6fc = MEMORY.ref(1, 0x800ce6fcL);

  public static final Value _800ce758 = MEMORY.ref(1, 0x800ce758L);

  public static final Value _800ce76c = MEMORY.ref(2, 0x800ce76cL);

  public static final Value selectedMenuOption_800ce774 = MEMORY.ref(4, 0x800ce774L);
  public static final Value _800ce778 = MEMORY.ref(4, 0x800ce778L);
  /**
   * <ol start="0">
   *   <li>{@link Ttle#initializeMainMenu()}</li>
   *   <li>{@link Ttle#loadMainMenuGfx()}</li>
   *   <li>{@link Ttle#FUN_800c7df0()}</li>
   *   <li>{@link Ttle#renderMainMenu()}</li>
   *   <li>{@link Ttle#FUN_800c7e50()}</li>
   *   <li>{@link Ttle#FUN_800c7fa0()}</li>
   *   <li>{@link Ttle#fadeOutMainMenu()}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<RunnableRef>> loadingStageArray_800ce77c = MEMORY.ref(4, 0x800ce77cL, ArrayRef.of(Pointer.classFor(RunnableRef.class), 7, 4, Pointer.deferred(4, RunnableRef::new)));

  public static final ArrayRef<RECT> rectArray_800ce798 = MEMORY.ref(2, 0x800ce798L, ArrayRef.of(RECT.class, 3, 8, RECT::new));

  public static final ArrayRef<ByteRef> _800ce7b0 = MEMORY.ref(1, 0x800ce7b0L, ArrayRef.of(ByteRef.class, 4, 1, ByteRef::new));
  public static final ArrayRef<Pointer<ConsumerRef<Long>>> callbacks_800ce7b4 = MEMORY.ref(4, 0x800ce7b4L, ArrayRef.of(Pointer.classFor(ConsumerRef.classFor(Long.class)), 17, 4, Pointer.deferred(4, ConsumerRef::new)));

  public static final Value _800ce7f8 = MEMORY.ref(1, 0x800ce7f8L);

  public static final Value _800ce840 = MEMORY.ref(1, 0x800ce840L);

  public static final Value _800ce8ac = MEMORY.ref(2, 0x800ce8acL);

  public static final ArrayRef<UnsignedIntRef> _800ce8f4 = MEMORY.ref(4, 0x800ce8f4L, ArrayRef.of(UnsignedIntRef.class, 8, 4, UnsignedIntRef::new));

  public static final Value _800ce914 = MEMORY.ref(2, 0x800ce914L);

  public static final Value _800ce91c = MEMORY.ref(2, 0x800ce91cL);

  @Method(0x800c7194L)
  public static void setUpNewGameData(final int unused) {
    final int oldVibration = gameState_800babc8.vibrationEnabled_4e1.get();
    final int oldMono = gameState_800babc8.mono_4e0.get();

    bzero(gameState_800babc8.getAddress(), 0x52c);

    gameState_800babc8.vibrationEnabled_4e1.set(oldVibration);
    gameState_800babc8.mono_4e0.set(oldMono);
    gameState_800babc8.indicatorMode_4e8.set(0x2L);
    gameState_800babc8.charIndex_88.get(0).set(0);
    gameState_800babc8.charIndex_88.get(1).set(-1);
    gameState_800babc8.charIndex_88.get(2).set(-1);

    //LAB_800c723c
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);
      final int level = characterStartingLevels_800ce6c4.get(charIndex).get();
      charData.xp_00.set((int)_800ce6d8.offset(charIndex * 0x4L).deref(4).offset(level * 0x4L).get());
      charData.hp_08.set(levelStuff_80111cfc.get(charIndex).deref().get(level).hp_00.get());
      charData.mp_0a.set(magicStuff_80111d20.get(charIndex).deref().get(1).mp_00.get());
      charData.sp_0c.set(0);
      charData.dlevelXp_0e.set(0);
      charData.status_10.set(0);
      charData.level_12.set(level);
      charData.dlevel_13.set(1);

      //LAB_800c7294
      for(int additionIndex = 0; additionIndex < 8; additionIndex++) {
        charData.additionLevels_1a.get(additionIndex).set(0);
        charData.additionXp_22.get(additionIndex).set(0);
      }

      charData.additionLevels_1a.get(0).set(1);

      //LAB_800c72d4
      for(int i = 1; i < level; i++) {
        final int index = levelStuff_80111cfc.get(charIndex).deref().get(i).addition_02.get();

        if(index != -1) {
          final int offset = additionOffsets_8004f5ac.get(charIndex).get();
          charData.additionLevels_1a.get(index - offset).set(1);
        }

        //LAB_800c72fc
      }

      //LAB_800c730c
      charData.selectedAddition_19.set((int)_800ce758.offset(charIndex * 0x2L).get());

      //LAB_800c7334
      for(int i = 0; i < 5; i++) {
        charData.equipment_14.get(i).set((int)_800ce6fc.offset(charIndex * 0xaL).offset(i * 0x2L).get());
      }
    }

    gameState_800babc8.charData_32c.get(0).partyFlags_04.set(35);

    //LAB_800c7398
    for(int i = 0x100; i >= 0; i--) {
      gameState_800babc8.equipment_1e8.get(i).set(0xff);
    }

    gameState_800babc8.equipmentCount_1e4.set((short)0);

    //LAB_800c73b8
    for(int i = 0x20; i >= 0; i--) {
      gameState_800babc8.items_2e9.get(i).set(0xff);
    }

    //LAB_800c73d8
    for(int i = 0; i < 0x21; i++) {
      final int itemId = (int)_800ce76c.offset(i * 0x2L).get();
      if(itemId == 0xff) {
        gameState_800babc8.itemCount_1e6.set((short)i);
        break;
      }

      //LAB_800c73f0
      gameState_800babc8.items_2e9.get(i).set(itemId);
    }

    //LAB_800c7404
    gameState_800babc8.gold_94.set(20);
    decrementOverlayCount();
  }

  @Method(0x800c7424L)
  public static void executeTtleUnloadingStage() {
    loadingStageArray_800c6898.get((int)pregameLoadingStage_800bb10c.get()).deref().run();
  }

  @Method(0x800c7488L)
  public static void mainMenuStateSetUpNewGame() {
    setUpNewGameData();
    vsyncMode_8007a3b8.set(1);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c74bcL)
  public static void FUN_800c74bc() {
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c74d4L)
  public static void waitForTtleFilesToLoad() {
    if(fileCount_8004ddc8.get() == 0) {
      pregameLoadingStage_800bb10c.addu(0x1L);
    }
  }

  @Method(0x800c7500L)
  public static void FUN_800c7500() {
    mainCallbackIndexOnceLoaded_8004dd24.setu(0x5L);
    vsyncMode_8007a3b8.set(2);
    pregameLoadingStage_800bb10c.setu(0);
  }

  @Method(0x800c7524L)
  public static void setUpNewGameData() {
    loadSupportOverlay(2, getConsumerAddress(Ttle.class, "setUpNewGameData", int.class), 0);
  }

  @Method(0x800c7558L)
  public static void FUN_800c7558(final long address, final long fileSize, final long param) {
    final RECT rect = new RECT().set((short)640, (short)0, (short)MEMORY.ref(2, address).offset(0x8L).get(), (short)MEMORY.ref(2, address).offset(0xaL).get());
    gameOverMcq_800bdc3c.setPointer(address);
    LoadImage(rect, address + MEMORY.ref(4, address).offset(0x4L).get());
    pregameLoadingStage_800bb10c.setu(0x3L);
  }

  @Method(0x800c75b4L)
  public static void FUN_800c75b4() {
    renderMcq(gameOverMcq_800bdc3c.deref(), 640, 0, -320, -108, 36, 128);
  }

  @Method(0x800c75fcL)
  public static void FUN_800c75fc() {
    switch((int)pregameLoadingStage_800bb10c.get()) {
      case 0 -> {
        if(fileCount_8004ddc8.get() == 0) {
          FUN_8002a9c0();
          setWidthAndFlags(640, 0);
          pregameLoadingStage_800bb10c.setu(0x1L);
        }
      }

      case 1 -> {
        loadDrgnBinFile(0, 6667, 0, getMethodAddress(Ttle.class, "FUN_800c7558", long.class, long.class, long.class), 0, 0x2L);
        pregameLoadingStage_800bb10c.setu(0x2L);
      }

      case 3 -> {
        deallocateRenderables(0xffL);
        scriptStartEffect(2, 10);
        _800c66c8.setu(0xe10L);
        pregameLoadingStage_800bb10c.setu(0x4L);
      }

      case 4 -> {
        _800c66c8.subu(0x1L);

        if((joypadPress_8007a398.get() & 0x820) != 0) {
          Scus94491BpeSegment_8002.playSound(2);
          pregameLoadingStage_800bb10c.setu(0x5L);
          scriptStartEffect(1, 10);
        }
      }

      case 5 -> {
        if(_800bb168.get() >= 0xff) {
          pregameLoadingStage_800bb10c.setu(0x6L);
        }

        //LAB_800c7740
        FUN_800c75b4();
      }

      case 6 -> {
        deallocateRenderables(0xffL);
        free(drgn0_6666FilePtr_800bdc3c.getPointer());
        mainCallbackIndexOnceLoaded_8004dd24.setu(0x2L);
        pregameLoadingStage_800bb10c.setu(0);
        vsyncMode_8007a3b8.set(2);
      }
    }

    //LAB_800c7788
  }

  @Method(0x800c7798L)
  public static void executeTtleLoadingStage() {
    loadingStageArray_800ce77c.get((int)pregameLoadingStage_800bb10c.get()).deref().run();
  }

  @Method(0x800c77e4L)
  public static void initializeMainMenu() {
    menuLoadingStage_800c66e8.setu(0);
    menuIdleTime_800c6720.setu(0);
    _800c6728.setu(0);
    _800c672c.setu(0);
    _800c6738.setu(0);
    logoFadeInAmount_800c66ec.set(0);
    backgroundInitialized_800c6704.setu(0);
    backgroundScrollAmount_800c6708.set(-176);
    copyrightInitialized_800c6710.setu(0);
    logoFireInitialized_800c6718.setu(0);
    logoFlashStage_800c66f0.setu(0);
    _800c6754.setu(0);
    _800c6758.setu(100L);

    hasSavedGames();

    hasSavedGames_800c66e4.setu(0);
    selectedMenuOption_800ce774.setu(0);
    _800ce778.setu(0);

    //LAB_800c7888
    for(int i = 0; i < 0x3L; i++) {
      //LAB_800c78a4
      _800c6748.offset(i * 4L).setu(mallocTail(rectArray_800ce798.get(i).w.get() * rectArray_800ce798.get(i).h.get() * 2L));
      StoreImage(rectArray_800ce798.get(i), _800c6748.offset(i * 4L).get());
    }

    //LAB_800c7978
    setWidthAndFlags(384, 0);
    setProjectionPlaneDistance(320);
    GsRVIEW2_800c6760.viewpoint_00.setX(0);
    GsRVIEW2_800c6760.viewpoint_00.setY(0);
    GsRVIEW2_800c6760.viewpoint_00.setZ(2000);
    GsRVIEW2_800c6760.refpoint_0c.setX(0);
    GsRVIEW2_800c6760.refpoint_0c.setY(0);
    GsRVIEW2_800c6760.refpoint_0c.setZ(-4000);
    GsRVIEW2_800c6760.viewpointTwist_18.set(0);
    GsRVIEW2_800c6760.super_1c.clear();
    GsSetRefView2(GsRVIEW2_800c6760);

    vsyncMode_8007a3b8.set(2);
    pregameLoadingStage_800bb10c.setu(0x1L);
  }

  @Method(0x800c7a18L)
  public static void loadTimImage(final long ptr) {
    final TimHeader tim = parseTimHeader(MEMORY.ref(4, ptr).offset(0x4L));
    LoadImage(tim.getImageRect(), tim.getImageAddress());

    if(tim.hasClut()) {
      LoadImage(tim.getClutRect(), tim.getClutAddress());
    }
  }

  /**
   * Loads the MRG file @ sector 61510. All files are TIMs.
   *
   * <ol start="0">
   *   <li>Menu background (upper portion)</li>
   *   <li>Logo fire 1</li>
   *   <li>Logo</li>
   *   <li>Menu text</li>
   *   <li>TM</li>
   *   <li>Copyright (left half)</li>
   *   <li>Copyright (right half)</li>
   *   <li>Logo fire 2</li>
   *   <li>Logo fire 3 (same as 4)</li>
   *   <li>Logo fire 4 (same as 3)</li>
   *   <li>Menu background (lower portion)</li>
   * </ol>
   */
  @Method(0x800c7af0L)
  public static void menuTexturesMrgLoaded(final long transferDest, final long fileSize, final long unknown) {
    for(int i = 0; i < MEMORY.ref(4, transferDest).offset(0x4L).get(); i++) {
      if(MEMORY.ref(4, transferDest).offset(i * 8L).offset(0xcL).get() != 0) {
        loadTimImage(MEMORY.ref(4, transferDest).offset(MEMORY.ref(4, transferDest).offset(i * 8L).offset(0x8L)).getAddress());
      }
    }

    //LAB_800c7bd0
    free(transferDest);
    _800c6724.addu(1);
  }

  @Method(0x800c7c18L)
  public static void menuFireTmdLoaded(final long tmdAddressPtr, final long fileSize, final long unknown) {
    final TmdWithId tmd = MEMORY.ref(4, tmdAddressPtr).cast(TmdWithId::new);
    _800c66d0.set(parseTmdFile(tmd));
    FUN_800cc0b0(_800c66d0.deref(), null);
    _800c66d0.deref().tmd_0c.set(tmd);
    setDobjAttributes(_800c66d0.deref(), 0);
    _800c6724.addu(1L);
  }

  @Method(0x800c7cacL)
  public static void loadMainMenuGfx() {
    _800c6724.setu(0);

    // MRG @ sector 61510
    loadDrgnBinFile(0, 5718, 0, getMethodAddress(Ttle.class, "menuTexturesMrgLoaded", long.class, long.class, long.class), 0, 0x4L);

    // TMD @ sector 61622
    loadDrgnBinFile(0, 5719, 0, getMethodAddress(Ttle.class, "menuFireTmdLoaded", long.class, long.class, long.class), 0, 0x2L);

    pregameLoadingStage_800bb10c.setu(0x2L);

    // Prepare fire animation struct

    //LAB_800c7d30
    for(int i = 0; i < 4; i++) {
      //LAB_800c7d4c
      final RECT rect = new RECT((short)(944 + i * 16), (short)256, (short)64, (short)64);
      _800c66d4.get(i).set(FUN_800cdaa0(rect, 0, 0x1L, _800ce7b0.get(i).getUnsigned()));
    }

    //LAB_800c7ddc
  }

  @Method(0x800c7df0L)
  public static void FUN_800c7df0() {
    if(_800c6724.get() == 0x2L) {
      scriptStartEffect(0x2L, 0xfL);
      SetGeomOffset(0, 0);
      pregameLoadingStage_800bb10c.setu(0x3L);
    }
  }

  @Method(0x800c7e50L)
  public static void FUN_800c7e50() {
    if(_800c6754.get() == 0) {
      scriptStartEffect(0x1L, 0xfL);
    }

    //LAB_800c7e7c
    renderMenuLogo();
    renderMenuOptions();
    FUN_800c959c();
    renderMenuLogoFire();
    renderMenuBackground();
    renderCopyright();

    _800c6754.addu(0x1L);
    if(_800c6754.get() >= 0x10L) {
      FUN_800cb5c4();
      deallocateFire();

      if(drgnBinIndex_800bc058.get() == 0x1L) {
        mainCallbackIndexOnceLoaded_8004dd24.setu(0x9L);
      } else {
        //LAB_800c7f40
        mainCallbackIndexOnceLoaded_8004dd24.setu(0xaL);
        diskNum_8004ddc0.set(1);
        _800bc05c.setu(0x9L);
      }

      _800bf0dc.setu(0x2L);
      _800bf0ec.setu(0x3L);
      vsyncMode_8007a3b8.set(2);

      pregameLoadingStage_800bb10c.setu(0);
    }

    //LAB_800c7f90
  }

  @Method(0x800c7fa0L)
  public static void FUN_800c7fa0() {
    if(_800c6754.get() == 0) {
      scriptStartEffect(0x1L, 0xfL);
    }

    //LAB_800c7fcc
    _800c6754.addu(0x1L);

    if(_800c6754.get() >= 0x10L) {
      if(_800c6728.get() == 0x2L) {
        whichMenu_800bdc38.setu(0xbL);
        FUN_800cb5c4();
        deallocateFire();
        _800c6728.setu(0x3L);
      }
    }

    //LAB_800c8038
    FUN_80022590();

    if(whichMenu_800bdc38.get() != 0) {
      return;
    }

    if(_800bdc34.get() != 0) {
      if(gameState_800babc8._4e4.get() != 0) {
        mainCallbackIndexOnceLoaded_8004dd24.setu(0x8L);
      } else {
        //LAB_800c80a4
        mainCallbackIndexOnceLoaded_8004dd24.setu(0x5L);
      }

      pregameLoadingStage_800bb10c.setu(0);
      vsyncMode_8007a3b8.set(2);

      //LAB_800c80c4
      return;
    }

    //LAB_800c80cc
    if(_800c6728.get() == 0x3L) {
      mainCallbackIndexOnceLoaded_8004dd24.setu(0x2L);
      pregameLoadingStage_800bb10c.setu(0);
      vsyncMode_8007a3b8.set(2);
    } else {
      //LAB_800c8108
      renderMenuLogo();
      renderMenuOptions();
      FUN_800c959c();
      renderMenuLogoFire();
      renderMenuBackground();
      renderCopyright();
    }

    //LAB_800c8138
  }

  @Method(0x800c8148L)
  public static void fadeOutMainMenu() {
    if(_800c6754.get() == 0) {
      scriptStartEffect(0x1L, 0xfL);
    }

    //LAB_800c8174
    renderMenuLogo();
    renderMenuOptions();
    FUN_800c959c();
    renderMenuLogoFire();
    renderMenuBackground();
    renderCopyright();

    _800c6754.addu(0x1L);
    if(_800c6754.get() > 0xfL) {
      FUN_800cb5c4();
      deallocateFire();

      _800bf0dc.setu(0);
      _800bf0ec.setu(0x2L);
      mainCallbackIndexOnceLoaded_8004dd24.setu(0x9L);
      vsyncMode_8007a3b8.set(2);

      pregameLoadingStage_800bb10c.setu(0);
    }

    //LAB_800c8218
  }

  @Method(0x800c8298L)
  public static void renderMainMenu() {
    final long menuLoadingStage = menuLoadingStage_800c66e8.get();

    if(menuLoadingStage == 0) {
      //LAB_800c82f0
      if(backgroundScrollAmount_800c6708.get() > -40) {
        menuLoadingStage_800c66e8.setu(0x1L);
      }

      //LAB_800c82d0
      //LAB_800c8314
    } else if(menuLoadingStage == 0x1L) {
      //LAB_800c831c
      renderMenuLogo();

      if(logoFadeInAmount_800c66ec.get() >= 0x80L) {
        menuLoadingStage_800c66e8.setu(0x2L);
      }

      //LAB_800c8348
    } else if(menuLoadingStage == 0x2L) {
      //LAB_800c8350
      renderMenuLogo();
      renderLogoFlash();

      if(logoFlashStage_800c66f0.get() == 0x2L) {
        menuLoadingStage_800c66e8.setu(0x3L);
      }

      //LAB_800c8380
    } else if(menuLoadingStage == 0x3L) {
      //LAB_800c8388
      FUN_800c93b0();
      FUN_800c8484();
      renderMenuLogo();
      renderMenuOptions();
      FUN_800c959c();
      renderMenuLogoFire();
      renderCopyright();
    }

    //LAB_800c83c8
    renderMenuBackground();

    if(_800c6728.get() != 0x1L) {
      menuIdleTime_800c6720.addu(0x2L);

      if(menuIdleTime_800c6720.get() > 0x690L) {
        if(fileCount_8004ddc8.get() == 0) {
          if(drgnBinIndex_800bc058.get() == 0x1L) {
            pregameLoadingStage_800bb10c.setu(0x6L);
          }
        }
      }
    }

    //LAB_800c8448
    if(joypadInput_8007a39c.get(0xf9ffL) != 0) {
      menuLoadingStage_800c66e8.setu(0x3L);
      menuIdleTime_800c6720.setu(0);
    }

    //LAB_800c8474
  }

  @Method(0x800c8484L)
  public static void FUN_800c8484() {
    if(_800c672c.get() < 3) {
      if(joypadPress_8007a398.get(0x20L) == 0 || fileCount_8004ddc8.get() != 0) {
        if(joypadPress_8007a398.get(0x1000L) != 0) { // Menu button up
          playSound(0, 1, 0, 0, (short)0, (short)0);

          selectedMenuOption_800ce774.subu(1);
          if(selectedMenuOption_800ce774.getSigned() < 0) {
            selectedMenuOption_800ce774.setu(2);
          }

          if(selectedMenuOption_800ce774.get() == 1 && hasSavedGames_800c66e4.get() != 1) {
            selectedMenuOption_800ce774.subu(1);
          }

          _800c672c.setu(2);
        }

        if(joypadPress_8007a398.get(0x4000L) != 0) { // Menu button down
          playSound(0, 1, 0, 0, (short)0, (short)0);

          selectedMenuOption_800ce774.addu(1);
          if(selectedMenuOption_800ce774.get() > 2) {
            selectedMenuOption_800ce774.setu(0);
          }

          if(selectedMenuOption_800ce774.get() == 1 && hasSavedGames_800c66e4.get() != 1) {
            selectedMenuOption_800ce774.addu(1);
          }

          _800c672c.setu(2);
        }
      } else { // Menu button X
        playSound(0, 2, 0, 0, (short)0, (short)0);

        _800c672c.setu(3);
        if(selectedMenuOption_800ce774.get() == 2) {
          _800c6738.setu(0);
          _800ce778.setu(0);
          _800c6728.setu(1);
        }
      }
    }
  }

  @Method(0x800c8634L)
  public static void renderMenuOptions() {
    if(hasSavedGames_800c66e4.get() == 0) {
      hasSavedGames_800c66e4.setu(hasSavedGames());
      selectedMenuOption_800ce774.setu(hasSavedGames_800c66e4.get() == 1 ? 1 : 0);
      return;
    }

    //LAB_800c868c
    switch((int)_800c672c.get()) {
      case 0 -> {
        //LAB_800c86d8
        for(int i = 0; i < 0x3L; i++) {
          //LAB_800c86f4
          menuOptionTransparency_800c6730.get(i).set((short)0);
        }

        //LAB_800c8728
        _800c672c.setu(0x1L);
      }

      case 1 -> {
        //LAB_800c8740
        //LAB_800c886c
        for(int i = 0; i < 0x3L; i++) {
          //LAB_800c875c
          menuOptionTransparency_800c6730.get(i).add((short)0x4);
          if(selectedMenuOption_800ce774.get() == i) {
            if(menuOptionTransparency_800c6730.get(i).get() > 0xb0) {
              menuOptionTransparency_800c6730.get(i).set((short)0xb0);
            }

            //LAB_800c8800
            //LAB_800c8808
          } else if(menuOptionTransparency_800c6730.get(i).get() > 0x40) {
            menuOptionTransparency_800c6730.get(i).set((short)0x40);
          }

          //LAB_800c8854
        }
      }

      case 2 -> {
        //LAB_800c8878
        //LAB_800c89e4
        for(int i = 0; i < 0x3L; i++) {
          //LAB_800c8894
          if(selectedMenuOption_800ce774.get() == i) {
            // Fade in selected item

            menuOptionTransparency_800c6730.get(i).add((short)0x8);
            if(menuOptionTransparency_800c6730.get(i).get() > 0xa0) {
              menuOptionTransparency_800c6730.get(i).set((short)0xa0);
            }

            //LAB_800c8938
          } else {
            // Fade out unselected items

            //LAB_800c8940
            menuOptionTransparency_800c6730.get(i).sub((short)0x10);
            if(menuOptionTransparency_800c6730.get(i).get() < 0x40) {
              menuOptionTransparency_800c6730.get(i).set((short)0x40);
            }
          }

          //LAB_800c89cc
        }
      }

      case 3 -> {
        _800c672c.setu(0x4L);
        if(selectedMenuOption_800ce774.get() == 0) {
          pregameLoadingStage_800bb10c.setu(0x4L);
          //LAB_800c8a20
        } else if(selectedMenuOption_800ce774.get() == 0x1L) {
          _800c6728.setu(0x2L);
          pregameLoadingStage_800bb10c.setu(0x5L);
        }
      }

      //LAB_800c8a4c
      case 4 -> {
        return;
      }
    }

    //LAB_800c8a70
    for(int i = 0; i < 3; i++) {
      final int x = (int)_800ce8ac.offset(i * 2 * 4).getSigned();
      final int y = (int)_800ce8ac.offset((i * 2 + 1) * 4).getSigned();

      //LAB_800c8a8c
      GPU.queueCommand(100, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .monochrome(menuOptionTransparency_800c6730.get(i).get())
        .pos(0, x, y)
        .uv(0, 0, (int)_800ce7f8.offset(i * 2 * 4).get())
        .pos(1, x + (int)_800ce7f8.offset(2, (i * 2 + 1) * 4).get(), y)
        .uv(1, (int)_800ce7f8.offset((i * 2 + 1) * 4).get(), (int)_800ce7f8.offset(i * 2 * 4).get())
        .pos(2, x, y + 16)
        .uv(2, 0, (int)_800ce7f8.offset(i * 2 * 4).get() + 16)
        .pos(3, x + (int)_800ce7f8.offset(2, (i * 2 + 1) * 4).get(), y + 16)
        .uv(3, (int)_800ce7f8.offset((i * 2 + 1) * 4).get(), (int)_800ce7f8.offset(i * 2 * 4).get() + 16)
        .clut(640, selectedMenuOption_800ce774.get() == i ? 5 : 2)
        .vramPos(576, 0)
      );

      GPU.queueCommand(100, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .monochrome(menuOptionTransparency_800c6730.get(i).get())
        .pos(0, x - 8, y - 8)
        .uv(0, (int)_800ce840.offset(i * 3 * 4).get(), (int)_800ce840.offset((i * 3 + 1) * 4).get())
        .clut(640, 4)
        .pos(1, x - 8 + (int)_800ce840.offset((i * 3 + 2) * 4).get(), y - 8)
        .uv(1, (int)(_800ce840.offset(i * 3 * 4).get() + _800ce840.offset((i * 3 + 2) * 4).get()), (int)_800ce840.offset((i * 3 + 1) * 4).get())
        .vramPos(576, 0)
        .pos(2, x - 8, y + 23)
        .uv(2, (int)_800ce840.offset(i * 3 * 4).get(), (int)_800ce840.offset((i * 3 + 1) * 4).get() + 32)
        .pos(3, x - 8 + (int)_800ce840.offset((i * 3 + 2) * 4).get(), y + 23)
        .uv(3, (int)(_800ce840.offset(i * 3 * 4).get() + _800ce840.offset((i * 3 + 2) * 4).get()), (int)_800ce840.offset((i * 3 + 1) * 4).get() + 32)
      );
    }

    //LAB_800c9390
    //LAB_800c939c
  }

  @Method(0x800c93b0L)
  public static void FUN_800c93b0() {
    if(_800c6728.get() == 1 && _800c6738.get() < 3) {
      if(joypadPress_8007a398.get(0x5000L) != 0) {
        playSound(0, 1, 0, 0, (short)0, (short)0);
        _800ce778.xoru(0b11);
        _800c6738.setu(2);
      }

      if(joypadPress_8007a398.get(0x40L) != 0) {
        playSound(0, 3, 0, 0, (short)0, (short)0);
        _800c6738.setu(3);
        _800c672c.setu(0);
      }

      if(joypadPress_8007a398.get(0xa000L) != 0) {
        playSound(0, 1, 0, 0, (short)0, (short)0);

        if(_800ce778.get() == 0) {
          gameState_800babc8.mono_4e0.xor(0b1);
          setMonoOrStereo(gameState_800babc8.mono_4e0.get());
        } else {
          gameState_800babc8.vibrationEnabled_4e1.xor(0b1);
          FUN_8002379c();

          if(gameState_800babc8.vibrationEnabled_4e1.get() != 0) {
            FUN_8002bcc8(0, 0x100);
            FUN_8002bda4(0, 0, 0x3c);
          }
        }

        _800c6738.setu(2);
      }
    }
  }

  @Method(0x800c959cL)
  public static void FUN_800c959c() {
    if(_800c6728.get() != 0x1L) {
      return;
    }

    //LAB_800c95c4
    long sp18 = gameState_800babc8.mono_4e0.get() + 0x1L;

    long sp1c;
    if(gameState_800babc8.vibrationEnabled_4e1.get() == 0) {
      //LAB_800c95f8
      sp1c = 0x5L;
    } else {
      sp1c = 0x4L;
    }

    //LAB_800c95fc
    switch((int)_800c6738.get()) {
      case 0 -> {
        //LAB_800c964c
        for(int i = 0; i < 0x6L; i++) {
          //LAB_800c9668
          _800c673c.offset(i * 2L).setu(0);
        }

        //LAB_800c969c
        _800c6738.setu(0x1L);
      }

      case 0x1 -> {
        //LAB_800c96b4
        for(int i = 0; i < 0x6L; i++) {
          //LAB_800c96d0
          _800c673c.offset(i * 2L).addu(0x10L);
          if(_800ce778.get() == i || sp18 == i || sp1c == i) {
            //LAB_800c9758
            if(_800c673c.offset(i * 2L).get() > 0xb0L) {
              _800c673c.offset(i * 2L).setu(0xb0L);
            }

            //LAB_800c97a4
          } else {
            //LAB_800c97ac
            if(_800c673c.offset(i * 2L).get() > 0x40L) {
              _800c673c.offset(i * 2L).setu(0x40L);
            }
          }

          //LAB_800c97f8
        }

        //LAB_800c9810
      }

      case 0x2 -> {
        //LAB_800c981c
        for(int i = 0; i < 0x6L; i++) {
          //LAB_800c9838
          if(_800ce778.get() == i || sp18 == i || sp1c == i) {
            //LAB_800c9880
            _800c673c.offset(i * 2L).addu(0x8L);
            if(_800c673c.offset(i * 2L).get() > 0xa0L) {
              _800c673c.offset(i * 2L).setu(0xa0L);
            }

            //LAB_800c990c
          } else {
            //LAB_800c9914
            _800c673c.offset(i * 2L).subu(0x10L);
            if(_800c673c.offset(i * 2L).get() < 0x40L) {
              _800c673c.offset(i * 2L).setu(0x40L);
            }
          }

          //LAB_800c99a0
        }

        //LAB_800c99b8
      }

      case 0x3 -> {
        //LAB_800c99c4
        for(int i = 0; i < 0x6L; i++) {
          //LAB_800c99e0
          if(_800ce778.get() == i || sp18 == i || sp1c == i) {
            //LAB_800c9a28
            _800c673c.offset(i * 2L).subu(0x20L);
          } else {
            //LAB_800c9a70
            _800c673c.offset(i * 2L).subu(0x8L);
          }

          //LAB_800c9ab0
          if(_800c673c.offset(i * 2L).getSigned() < 0) {
            _800c673c.offset(i * 2L).setu(0);
          }

          //LAB_800c9af4
          if(_800c673c.offset(_800ce778.get() * 2).get() == 0) {
            _800c6738.setu(0x4L);
            _800c6728.setu(0);
          }

          //LAB_800c9b34
        }

        //LAB_800c9b4c
      }

      case 0x4 -> {
        return;
      }
    }

    //LAB_800c9b70
    for(int i = 0; i < 6; i++) {
      GPU.queueCommand(100, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .monochrome((int)_800c673c.offset(i * 2).get())
        .clut(640, selectedMenuOption_800ce774.get() == i ? 5 : 2)
        .vramPos(576, 0)
        .pos(0, (int)_800ce8ac.offset((i + 3) * 8).getSigned(), (int)_800ce8ac.offset(((i + 3) * 2 + 1) * 4).getSigned())
        .uv(0, 0, (int)_800ce7f8.offset((i + 3) * 8).get())
        .pos(1, (int)(_800ce8ac.offset((i + 3) * 8).getSigned() + _800ce7f8.offset(((i + 3) * 2 + 1) * 4).get()), (int)_800ce8ac.offset(((i + 3) * 2 + 1) * 4).getSigned())
        .uv(1, (int)_800ce7f8.offset(((i + 3) * 2 + 1) * 4).get(), (int)_800ce7f8.offset((i + 3) * 8).get())
        .pos(2, (int)_800ce8ac.offset((i + 3) * 8).getSigned(), (int)_800ce8ac.offset(((i + 3) * 2 + 1) * 4).getSigned() + 16)
        .uv(2, 0, (int)_800ce7f8.offset((i + 3) * 8).get() + 16)
        .pos(3, (int)(_800ce8ac.offset((i + 3) * 8).getSigned() + _800ce7f8.offset(((i + 3) * 2 + 1) * 4).get()), (int)_800ce8ac.offset(((i + 3) * 2 + 1) * 4).getSigned() + 16)
        .uv(3, (int)_800ce7f8.offset(((i + 3) * 2 + 1) * 4).get(), (int)_800ce7f8.offset((i + 3) * 8).get() + 16)
      );
    }

    //LAB_800c9ff8
    //LAB_800c9ffc
    for(int i = 0; i < 2; i++) {
      //LAB_800ca018
      sp18 = i * 3;

      GPU.queueCommand(100, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .monochrome((int)_800c673c.offset(sp18 * 2).get())
        .clut(640, 4)
        .vramPos(576, 0)
        .pos(0, (int)_800ce8ac.offset((sp18 + 3) * 8).getSigned() - 8, (int)_800ce8ac.offset(((sp18 + 3) * 2 + 1) * 4).getSigned() - 9)
        .uv(0, (int)_800ce840.offset((sp18 + 3) * 3 * 4).get(), (int)_800ce840.offset(((sp18 + 3) * 3 + 1) * 4).get())
        .pos(1, (int)(_800ce8ac.offset((sp18 + 3) * 8).getSigned() + _800ce840.offset(((sp18 + 3) * 3 + 2) * 4).get() - 8), (int)_800ce8ac.offset(((sp18 + 3) * 2 + 1) * 4).getSigned() - 9)
        .uv(1, (int)(_800ce840.offset((sp18 + 3) * 3 * 4).get() + _800ce840.offset(((sp18 + 3) * 3 + 2) * 4).get()), (int)_800ce840.offset(((sp18 + 3) * 3 + 1) * 4).get())
        .pos(2, (int)_800ce8ac.offset((sp18 + 3) * 8).getSigned() - 8, (int)_800ce8ac.offset(((sp18 + 3) * 2 + 1) * 4).getSigned() + 23)
        .uv(2, (int)_800ce840.offset((sp18 + 3) * 3 * 4).get(), (int)_800ce840.offset(((sp18 + 3) * 3 + 1) * 4).get() + 31)
        .pos(3, (int)(_800ce8ac.offset((sp18 + 3) * 8).getSigned() + _800ce840.offset(((sp18 + 3) * 3 + 2) * 4).get() - 8), (int)_800ce8ac.offset(((sp18 + 3) * 2 + 1) * 4).getSigned() + 23)
        .uv(3, (int)(_800ce840.offset((sp18 + 3) * 3 * 4).get() + _800ce840.offset(((sp18 + 3) * 3 + 2) * 4).get()), (int)_800ce840.offset(((sp18 + 3) * 3 + 1) * 4).get() + 31)
      );
    }

    //LAB_800ca57c
    //LAB_800ca580
    for(int i = 0; i < 2; i++) {
      //LAB_800ca59c
      final long sp14;
      if(i == 0) {
        sp14 = gameState_800babc8.mono_4e0.get() + 0x4L;
        sp1c = gameState_800babc8.mono_4e0.get() + 0x1L;
      } else {
        //LAB_800ca5dc
        if(gameState_800babc8.vibrationEnabled_4e1.get() == 0) {
          //LAB_800ca5fc
          sp14 = 0x8L;
        } else {
          sp14 = 0x7L;
        }

        //LAB_800ca600
        if(gameState_800babc8.vibrationEnabled_4e1.get() == 0) {
          //LAB_800ca624
          sp1c = 0x5L;
        } else {
          sp1c = 0x4L;
        }

        //LAB_800ca628
      }

      //LAB_800ca62c
      GPU.queueCommand(100, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .monochrome((int)_800c673c.offset(sp1c * 2).get())
        .clut(640, 4)
        .vramPos(576, 0)
        .pos(0, (int)_800ce8ac.offset(sp14 * 8).getSigned() - 8, (int)_800ce8ac.offset((sp14 * 2 + 1) * 4).getSigned() - 9)
        .uv(0, (int)_800ce840.offset(sp14 * 3 * 4).get(), (int)_800ce840.offset((sp14 * 3 + 1) * 4).get())
        .pos(1, (int)(_800ce8ac.offset(sp14 * 8).getSigned() + _800ce840.offset((sp14 * 3 + 2) * 4).get() - 8), (int)_800ce8ac.offset((sp14 * 2 + 1) * 4).getSigned() - 9)
        .uv(1, (int)(_800ce840.offset(sp14 * 3 * 4).get() + _800ce840.offset((sp14 * 3 + 2) * 4).get()), (int)_800ce840.offset((sp14 * 3 + 1) * 4).get())
        .pos(2, (int)_800ce8ac.offset(sp14 * 8).getSigned() - 8, (int)_800ce8ac.offset((sp14 * 2 + 1) * 4).getSigned() + 23)
        .uv(2, (int)_800ce840.offset(sp14 * 3 * 4).get(), (int)_800ce840.offset((sp14 * 3 + 1) * 4).get() + 31)
        .pos(3, (int)(_800ce8ac.offset(sp14 * 8).getSigned() + _800ce840.offset((sp14 * 3 + 2) * 4).get() - 8), (int)_800ce8ac.offset((sp14 * 2 + 1) * 4).getSigned() + 23)
        .uv(3, (int)(_800ce840.offset(sp14 * 3 * 4).get() + _800ce840.offset((sp14 * 3 + 2) * 4).get()), (int)_800ce840.offset((sp14 * 3 + 1) * 4).get() + 31)
      );
    }

    //LAB_800cab28
    //LAB_800cab34
  }

  @Method(0x800cab48L)
  public static void renderCopyright() {
    if(copyrightInitialized_800c6710.get() == 0) {
      copyrightFadeInAmount_800c6714.set(0);
      copyrightInitialized_800c6710.setu(0x1L);
    }

    //LAB_800cab7c
    copyrightFadeInAmount_800c6714.add(4);
    if(copyrightFadeInAmount_800c6714.get() > 0x80) {
      copyrightFadeInAmount_800c6714.set(0x80);
    }

    //LAB_800cabb8
    //LAB_800cabcc
    for(int sp44 = 0; sp44 < 2; sp44++) {
      //LAB_800cabe8
      renderQuad(
        Bpp.BITS_4,
        896, 0,
        0x1028,
        copyrightFadeInAmount_800c6714.get(), copyrightFadeInAmount_800c6714.get(), copyrightFadeInAmount_800c6714.get(),
        (int)_800ce8f4.get(sp44 * 4).get(), (int)_800ce8f4.get(sp44 * 4 + 1).get(),
        (int)_800ce8f4.get(sp44 * 4 + 2).get(), (int)_800ce8f4.get(sp44 * 4 + 3).get(),
        (int)_800ce914.offset(4, sp44 * 4).get(), 80,
        (int)_800ce8f4.get(sp44 * 4 + 2).get(), (int)_800ce8f4.get(sp44 * 4 + 3).get(),
        100,
        Translucency.B_PLUS_F
      );
    }
  }

  @Method(0x800cadd0L)
  public static void renderMenuLogo() {
    logoFadeInAmount_800c66ec.add(4);
    if(logoFadeInAmount_800c66ec.get() > 0x80) {
      logoFadeInAmount_800c66ec.set(0x80);
    }

    //LAB_800cae18
    //LAB_800cae2c
    for(int i = 0; i < 2; i++) {
      //LAB_800cae48
      renderQuad(
        Bpp.BITS_4,
        576 + i * 64, 256,
        0x68,
        logoFadeInAmount_800c66ec.get(), logoFadeInAmount_800c66ec.get(), logoFadeInAmount_800c66ec.get(),
        0, 0,
        (int)_800ce91c.offset(i * 2).get(), 88,
        i * 255 - 184, -80,
        (int)_800ce91c.offset(i * 2).get(), 88,
        orderingTableSize_1f8003c8.get() - 4,
        Translucency.B_PLUS_F
      );
    }

    renderQuad(
      Bpp.BITS_4,
      896, 0,
      0x1428,
      logoFadeInAmount_800c66ec.get(), logoFadeInAmount_800c66ec.get(), logoFadeInAmount_800c66ec.get(),
      0, 240,
      16, 8,
      134, -14,
      16, 8,
      orderingTableSize_1f8003c8.get() - 4,
      Translucency.B_PLUS_F
    );
  }

  @Method(0x800cb070L)
  public static void renderMenuBackground() {
    if(backgroundInitialized_800c6704.get() == 0) {
      backgroundScrollAmount_800c6708.set(-176);
      backgroundFadeInAmount_800c670c.set(0);
      backgroundInitialized_800c6704.setu(0x1L);
    }

    //LAB_800cb0b0
    backgroundFadeInAmount_800c670c.add(2);
    if(backgroundFadeInAmount_800c670c.get() > 0x80) {
      backgroundFadeInAmount_800c670c.set(0x80);
    }

    //LAB_800cb0ec
    //LAB_800cb100
    renderQuad(
      Bpp.BITS_8,
      384, 0,
      0x28,
      backgroundFadeInAmount_800c670c.get(), backgroundFadeInAmount_800c670c.get(), backgroundFadeInAmount_800c670c.get(),
      0, 0,
      384, 424,
      -192, -120 + backgroundScrollAmount_800c6708.get(),
      384, 424,
      orderingTableSize_1f8003c8.get() - 3,
      null
    );

    //LAB_800cb370
    backgroundScrollAmount_800c6708.incr();
    if(backgroundScrollAmount_800c6708.get() > 0) {
      backgroundScrollAmount_800c6708.set(0);
    }

    //LAB_800cb3b0
  }

  /** TODO clut split */
  @Method(0x800cb4c4L)
  public static void renderQuad(final Bpp bpp, final int vramX, final int vramY, final int clut, final int r, final int g, final int b, final int u, final int v, final int tw, final int th, final int x, final int y, final int w, final int h, final int z, @Nullable final Translucency translucency) {
    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .bpp(bpp)
      .rgb(r, g, b)
      .pos(0, x, y)
      .pos(1, x + w, y)
      .pos(2, x, y + h)
      .pos(3, x + w, y + h)
      .uv(0, u, v)
      .uv(1, u + tw, v)
      .uv(2, u, v + th)
      .uv(3, u + tw, v + th)
      .clut((clut & 0b111111) * 16, clut >>> 6)
      .vramPos(vramX, vramY);

    if(translucency != null) {
      cmd.translucent(translucency);
    }

    GPU.queueCommand(z, cmd);
  }

  @Method(0x800cb5c4L)
  public static void FUN_800cb5c4() {
    //LAB_800cb5d8
    for(int i = 0; i < 3; i++) {
      //LAB_800cb5f4
      LoadImage(rectArray_800ce798.get(i), _800c6748.offset(i * 0x4L).get());
      free(_800c6748.offset(i * 0x4L).get());
    }

    //LAB_800cb688
  }

  @Method(0x800cb69cL)
  public static void deallocateFire() {
    deallocateTmdRenderer(_800c66d0.deref());

    //LAB_800cb6bc
    for(int i = 0; i < 4; i++) {
      //LAB_800cb6d8
      FUN_800ce448(_800c66d4.get(i).get());
    }

    //LAB_800cb714
  }

  @Method(0x800cb728L)
  public static void renderMenuLogoFire() {
    final SVECTOR sp58 = new SVECTOR().set(_800c68f0);
    final VECTOR sp60 = new VECTOR().set(_800c68f8);

    if(logoFireInitialized_800c6718.get() == 0) {
      logoFireInitialized_800c6718.setu(0x1L);
      _800c671c.setu(0);
    }

    //LAB_800cb7b4
    _800c671c.addu(0x2L);
    if(_800c671c.get() > 0xffL) {
      _800c671c.setu(0xffL);
    }

    //LAB_800cb7f0
    GsSetRefView2(GsRVIEW2_800c6760);

    final UnboundedArrayRef<GsDOBJ2> dobj2s = _800c66d0.deref().dobj2s_00.deref();
    final UnboundedArrayRef<GsCOORDINATE2> coord2s = _800c66d0.deref().coord2s_04.deref();

    //LAB_800cb834
    for(int i = 0; i < _800c66d0.deref().count_08.get(); i++) {
      final MATRIX sp10 = new MATRIX();
      final MATRIX sp30 = new MATRIX();

      //LAB_800cb85c
      FUN_800cc26c(sp58, coord2s.get(i));
      GsGetLws(dobj2s.get(i).coord2_04.deref(), sp10, sp30);
      GsSetLightMatrix(sp10);
      ScaleMatrixL(sp30, sp60);
      setRotTransMatrix(sp30);
      FUN_800cc388(dobj2s.get(i));
    }

    //LAB_800cb904
    //LAB_800cb908
    for(int i = 0; i < 0x4L; i++) {
      //LAB_800cb924
      animateFire(_800c66d4.get(i).get());
    }

    //LAB_800cb960
  }

  @Method(0x800cb974L)
  public static void renderLogoFlash() {
    final int sp24 = doubleBufferFrame_800bb108.get() ^ 1;

    if(logoFlashStage_800c66f0.get() == 0x2L) {
      return;
    }

    //LAB_800cb9b0
    if(logoFlashStage_800c66f0.get() == 0) {
      logoFlashStage_800c66f0.setu(0x1L);
      _800c66f4.setu(0);
      _800c66f8.setu(0);
      _800c66fc.setu(0);

      //LAB_800cb9ec
      for(int i = 0; i < 1; i++) {
        //LAB_800cba04
        _800c6700.offset(i * 2L).setu(0);
        _800c6702.offset(i * 2L).setu(0);
      }
    }

    //LAB_800cba54
    _800c66fc.addu(0x60L);
    if(_800c66fc.get() > 0x800L) {
      _800c66fc.setu(0x800L);
    }

    //LAB_800cba90
    final int colour = (rsin(_800c66fc.get()) * 160) >> 12;

    // GP0.66 Textured quad, variable size, translucent, blended
    final GpuCommandQuad cmd = new GpuCommandQuad()
      .translucent(Translucency.B_PLUS_F)
      .bpp(Bpp.BITS_15)
      .monochrome(colour)
      .pos(-192, -120, 384, 240)
      .uv(0, sp24 * 16)
      .vramPos(0, doubleBufferFrame_800bb108.get() == 0 ? 0 : 256);

    GPU.queueCommand(5, cmd);

    if(_800c66fc.get() == 0x800L) {
      logoFlashStage_800c66f0.setu(0x2L);
    }

    //LAB_800cbe34
  }

  @Method(0x800cbe48L)
  public static TmdRenderingStruct parseTmdFile(final TmdWithId tmd) {
    final TmdRenderingStruct tmdRenderer = MEMORY.ref(4, mallocTail(0x10L), TmdRenderingStruct::new);
    tmdRenderer.count_08.set(prepareTmdRenderer(tmdRenderer, tmd));
    return tmdRenderer;
  }

  @Method(0x800cbeb4L)
  public static void deallocateTmdRenderer(final TmdRenderingStruct renderer) {
    free(renderer.coord2s_04.getPointer());
    free(renderer.dobj2s_00.getPointer());
    free(renderer.tmd_0c.getPointer());
    free(renderer.getAddress());
  }

  @Method(0x800cbf3cL)
  public static long prepareTmdRenderer(final TmdRenderingStruct tmdRenderer, final TmdWithId tmd) {
    adjustTmdPointers(tmd.tmd);

    tmdRenderer.dobj2s_00.set(MEMORY.ref(4, mallocTail(tmd.tmd.header.nobj.get() * 0x10L), UnboundedArrayRef.of(4, GsDOBJ2::new)));
    tmdRenderer.coord2s_04.set(MEMORY.ref(4, mallocTail(tmd.tmd.header.nobj.get() * 0x50L), UnboundedArrayRef.of(4, GsCOORDINATE2::new)));

    //LAB_800cc02c
    for(int objIndex = 0; objIndex < tmd.tmd.header.nobj.get(); objIndex++) {
      //LAB_800cc04c
      updateTmdPacketIlen(tmd.tmd.objTable, tmdRenderer.dobj2s_00.deref().get(objIndex), objIndex);
    }

    //LAB_800cc088
    //LAB_800cc09c
    return tmd.tmd.header.nobj.get();
  }

  @Method(0x800cc0b0L)
  public static void FUN_800cc0b0(final TmdRenderingStruct renderer, @Nullable final GsCOORDINATE2 superCoord2) {
    //LAB_800cc0f0
    for(int i = 0; i < renderer.count_08.get(); i++) {
      final GsCOORDINATE2 coord2 = renderer.coord2s_04.deref().get(i);
      final GsDOBJ2 dobj2 = renderer.dobj2s_00.deref().get(i);

      //LAB_800cc114
      GsInitCoordinate2(superCoord2, coord2);

      dobj2.coord2_04.set(coord2);
      coord2.coord.transfer.setX(100);
      coord2.coord.transfer.setY(-430);
      coord2.coord.transfer.setZ(-2048);
    }

    //LAB_800cc1a8
  }

  @Method(0x800cc1bcL)
  public static void setDobjAttributes(final TmdRenderingStruct renderer, final long dobjAttribute) {
    //LAB_800cc1e4
    for(int i = 0; i < renderer.count_08.get(); i++) {
      //LAB_800cc208
      renderer.dobj2s_00.deref().get(i).attribute_00.set(dobjAttribute);
    }

    //LAB_800cc25c
  }

  @Method(0x800cc26cL)
  public static void FUN_800cc26c(final SVECTOR a0, final GsCOORDINATE2 a1) {
    final MATRIX m = new MATRIX();
    m.set(identityMatrix_800c3568);
    m.transfer.set(a1.coord.transfer);
    RotMatrix_8003faf0(a0, m);
    a1.coord.set(m);
    a1.flg.set(0);
  }

  @Method(0x800cc388L)
  public static void FUN_800cc388(final GsDOBJ2 dobj2) {
    final UnboundedArrayRef<SVECTOR> vertices = dobj2.tmd_08.deref().vert_top_00.deref();
    final long normals = dobj2.tmd_08.deref().normal_top_08.get();
    long primitives = dobj2.tmd_08.deref().primitives_10.getPointer();
    long primitiveCount = dobj2.tmd_08.deref().n_primitive_14.get();

    //LAB_800cc408
    while(primitiveCount != 0) {
      final long primitive = MEMORY.ref(4, primitives).get();
      final long command = primitive & 0xff04_0000L;
      final long len = primitive & 0xffffL;

      //LAB_800cc420
      primitiveCount -= len;

      if(command == 0x3400_0000L) {
        //LAB_800cc528
        primitives = FUN_800cd72c(primitives, vertices, normals, len);
      } else if(command == 0x3700_0000L) {
        //LAB_800cc4a0
        primitives = FUN_800cc57c(primitives, vertices, len);
      } else if(command == 0x3c00_0000L) {
        //LAB_800cc4f8
        primitives = FUN_800cd2d8(primitives, vertices, normals, len);
      } else if(command == 0x3f00_0000L) {
        //LAB_800cc4cc
        primitives = FUN_800ccb78(primitives, vertices, len);
      }

      //LAB_800cc558
      //LAB_800cc560
    }

    //LAB_800cc568
  }

  @Method(0x800cc57cL)
  public static long FUN_800cc57c(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long count) {
    final long sp00 = _800c671c.get();

    //LAB_800cc5b0
    for(int i = 0; i < count; i++) {
      final GpuCommandPoly cmd = new GpuCommandPoly(3)
        .translucent(Translucency.of(((int)MEMORY.ref(2, primitives).offset(0x0aL).get() & 0b1100000) >>> 5));

      //LAB_800cc5c8
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1cL).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x20L).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x280030L); // Perspective transformation triple

      cmd.uv(0, (int)MEMORY.ref(1, primitives).offset(0x04L).get(), (int)MEMORY.ref(1, primitives).offset(0x05L).get());
      cmd.uv(1, (int)MEMORY.ref(1, primitives).offset(0x08L).get(), (int)MEMORY.ref(1, primitives).offset(0x09L).get());
      cmd.uv(2, (int)MEMORY.ref(1, primitives).offset(0x0cL).get(), (int)MEMORY.ref(1, primitives).offset(0x0dL).get());

      cmd.clut(((int)MEMORY.ref(2, primitives).offset(0x06L).get() & 0b111111) * 16, (int)MEMORY.ref(2, primitives).offset(0x06L).get() >>> 6);
      cmd.vramPos(((int)MEMORY.ref(2, primitives).offset(0x0aL).get() & 0b1111) * 64, (MEMORY.ref(2, primitives).offset(0x0aL).get() & 0b10000) != 0 ? 256 : 0);

      if((int)CPU.CFC2(31) >= 0) { // No errors
        //LAB_800cc674
        CPU.COP2(0x1400006L); // Normal clipping

        if(CPU.MFC2(24L) != 0) { // Is visible
          //LAB_800cc6b0
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          cmd.pos(0, v0.getX(), v0.getY());
          cmd.pos(1, v1.getX(), v1.getY());
          cmd.pos(2, v2.getX(), v2.getY());

          if((int)CPU.CFC2(31) >= 0) { // No errors
            //LAB_800cc6e8
            if(v0.getX() >= -0xc0 || v1.getX() >= -0xc0 || v2.getX() >= -0xc0) {
              //LAB_800cc72c
              if(v0.getY() >= -0x78 || v1.getY() >= -0x78 || v2.getY() >= -0x78) {
                //LAB_800cc770
                if(v0.getX() <= 0xc0 || v1.getX() <= 0xc0 || v2.getX() <= 0xc0) {
                  //LAB_800cc7b4
                  if(v0.getY() <= 0x78 || v1.getY() <= 0x78 || v2.getY() <= 0x78) {
                    //LAB_800cc7f8
                    CPU.COP2(0x158002dL); // Average of three Z values

                    cmd.rgb(0, (int)(MEMORY.ref(1, primitives).offset(0x10L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x11L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x12L).get() * sp00 / 0xff));
                    cmd.rgb(1, (int)(MEMORY.ref(1, primitives).offset(0x14L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x15L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x16L).get() * sp00 / 0xff));
                    cmd.rgb(2, (int)(MEMORY.ref(1, primitives).offset(0x18L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x19L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x1aL).get() * sp00 / 0xff));

                    // OTZ - Average Z value (for ordering table)
                    final int z = (int)Math.min(CPU.MFC2(7) + _800c6758.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                    GPU.queueCommand(z, cmd);
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800ccb34
      primitives += 0x24L;
    }

    //LAB_800ccb4c
    //LAB_800ccb68
    return primitives;
  }

  @Method(0x800ccb78L)
  public static long FUN_800ccb78(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long count) {
    final long sp00 = _800c671c.get();

    //LAB_800ccbcc
    for(int i = 0; i < count; i++) {
      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .translucent(Translucency.of(((int)MEMORY.ref(2, primitives).offset(0x0aL).get() & 0b1100000) >>> 5));

      //LAB_800ccbe4
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x24L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x26L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x28L).get());
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VY1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      cmd.uv(0, (int)MEMORY.ref(1, primitives).offset(0x04L).get(), (int)MEMORY.ref(1, primitives).offset(0x05L).get());
      cmd.uv(1, (int)MEMORY.ref(1, primitives).offset(0x08L).get(), (int)MEMORY.ref(1, primitives).offset(0x09L).get());
      cmd.uv(2, (int)MEMORY.ref(1, primitives).offset(0x0cL).get(), (int)MEMORY.ref(1, primitives).offset(0x0dL).get());
      cmd.uv(3, (int)MEMORY.ref(1, primitives).offset(0x10L).get(), (int)MEMORY.ref(1, primitives).offset(0x11L).get());

      cmd.clut(((int)MEMORY.ref(2, primitives).offset(0x06L).get() & 0b111111) * 16, (int)MEMORY.ref(2, primitives).offset(0x06L).get() >>> 6);
      cmd.vramPos(((int)MEMORY.ref(2, primitives).offset(0x0aL).get() & 0b1111) * 64, (MEMORY.ref(2, primitives).offset(0x0aL).get() & 0b10000) != 0 ? 256 : 0);

      if((int)CPU.CFC2(31) >= 0) { // No errors
        //LAB_800ccc90
        CPU.COP2(0x140_0006L); // Normal clipping

        if(CPU.MFC2(24) != 0) { // Is visible
          //LAB_800ccccc
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          cmd.pos(0, v0.getX(), v0.getY());
          cmd.pos(1, v1.getX(), v1.getY());
          cmd.pos(2, v2.getX(), v2.getY());

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x2aL).get());
          CPU.MTC2(vert3.getXY(), 0); // VXY0
          CPU.MTC2(vert3.getZ(),  1); // VZ0
          CPU.COP2(0x18_0001L); // Perspective transform single

          if((int)CPU.CFC2(31) >= 0) { // No errors
            //LAB_800ccd54
            final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

            cmd.pos(3, v3.getX(), v3.getY());

            if(v0.getX() >= -0xc0 || v1.getX() >= -0xc0 || v2.getX() >= -0xc0 || v3.getX() >= -0xc0) {
              //LAB_800ccdb4
              if(v0.getY() >= -0x78 || v1.getY() >= -0x78 || v2.getY() >= -0x78 || v3.getY() >= -0x78) {
                //LAB_800cce0c
                if(v0.getX() <= 0xc0 || v1.getX() <= 0xc0 || v2.getX() <= 0xc0 || v3.getX() <= 0xc0) {
                  //LAB_800cce64
                  if(v0.getY() <= 0x78 || v1.getY() <= 0x78 || v2.getY() <= 0x78 || v3.getY() <= 0x78) {
                    //LAB_800ccebc
                    cmd.rgb(0, (int)(MEMORY.ref(1, primitives).offset(0x14L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x15L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x16L).get() * sp00 / 0xff));
                    cmd.rgb(1, (int)(MEMORY.ref(1, primitives).offset(0x18L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x19L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x1aL).get() * sp00 / 0xff));
                    cmd.rgb(2, (int)(MEMORY.ref(1, primitives).offset(0x1cL).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x1cL).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x1dL).get() * sp00 / 0xff));
                    cmd.rgb(3, (int)(MEMORY.ref(1, primitives).offset(0x20L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x21L).get() * sp00 / 0xff), (int)(MEMORY.ref(1, primitives).offset(0x22L).get() * sp00 / 0xff));

                    GPU.queueCommand((int)_800c6758.get(), cmd);
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800cd294
      primitives += 0x2cL;
    }

    //LAB-800cd2ac
    //LAB_800cd2c8
    return primitives;
  }

  @Method(0x800cd2d8L)
  public static long FUN_800cd2d8(final long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    assert false;
    return 0;
  }

  @Method(0x800cd72cL)
  public static long FUN_800cd72c(final long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    assert false;
    return 0;
  }

  @Method(0x800cdaa0L)
  public static long FUN_800cdaa0(final RECT rect, final long widthSomething, final long a2, final long a3) {
    final long addr0 = mallocTail(0x20L);
    final long addr1 = mallocTail(rect.w.get() / (2 - widthSomething) * rect.h.get());
    final long addr2 = mallocTail(rect.w.get() / (2 - widthSomething) * rect.h.get());

    MEMORY.ref(2, addr0).offset(0x00L).setu(rect.x.get());
    MEMORY.ref(2, addr0).offset(0x02L).setu(rect.y.get());
    MEMORY.ref(2, addr0).offset(0x04L).setu(rect.w.get() / (4 - widthSomething * 2));
    MEMORY.ref(2, addr0).offset(0x06L).setu(rect.h.get());
    MEMORY.ref(4, addr0).offset(0x08L).setu(addr1);
    MEMORY.ref(4, addr0).offset(0x0cL).setu(addr2);
    MEMORY.ref(4, addr0).offset(0x10L).setu(a2);
    MEMORY.ref(4, addr0).offset(0x14L).setu(widthSomething);
    MEMORY.ref(2, addr0).offset(0x18L).setu(a3);
    MEMORY.ref(2, addr0).offset(0x1aL).setu(a2 / 2 * 2);
    MEMORY.ref(2, addr0).offset(0x1cL).setu(a2 / 2 * 2);
    return addr0;
  }

  @Method(0x800cdcb0L)
  public static void animateFire(final long a0) {
    final long a1;
    final long v1;
    final RECT sp10 = new RECT();
    final RECT sp18 = new RECT();
    final RECT sp20 = new RECT();
    final RECT sp28 = new RECT();

    if(MEMORY.ref(2, a0).offset(0x18L).get() == 0) {
      return;
    }

    //LAB_800cdce0
    MEMORY.ref(2, a0).offset(0x1cL).addu(0x1L);
    if(MEMORY.ref(2, a0).offset(0x1cL).get() < MEMORY.ref(2, a0).offset(0x1aL).get()) {
      return;
    }

    //LAB_800cdd28
    MEMORY.ref(2, a0).offset(0x1cL).setu(0);
    if(MEMORY.ref(4, a0).offset(0x10L).get(0x1L) == 0) {
      v1 = MEMORY.ref(2, a0).offset(0x18L).get();
      a1 = MEMORY.ref(2, a0).offset(0x4L).get();
      MEMORY.ref(2, a0).offset(0x18L).setu(v1 % a1);
      if(MEMORY.ref(2, a0).offset(0x18L).getSigned() > 0) {
        sp10.x.set((short)(MEMORY.ref(2, a0).get() + MEMORY.ref(2, a0).offset(0x4L).get() - MEMORY.ref(2, a0).offset(0x18L).get()));
        sp10.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp10.w.set((short)MEMORY.ref(2, a0).offset(0x18L).get());
        sp10.h.set((short)MEMORY.ref(2, a0).offset(0x6L).get());
        sp18.x.set((short)MEMORY.ref(2, a0).get());
        sp18.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp18.w.set((short)(MEMORY.ref(2, a0).offset(0x4L).get() - MEMORY.ref(2, a0).offset(0x18L).get()));
        sp18.h.set((short)MEMORY.ref(2, a0).offset(0x6L).get());
        sp20.x.set((short)MEMORY.ref(2, a0).get());
        sp20.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp20.w.set((short)MEMORY.ref(2, a0).offset(0x18L).get());
        sp20.h.set((short)MEMORY.ref(2, a0).offset(0x6L).get());
        sp28.x.set((short)(MEMORY.ref(2, a0).get() + MEMORY.ref(2, a0).offset(0x18L).get()));
        sp28.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp28.w.set((short)(MEMORY.ref(2, a0).offset(0x4L).get() - MEMORY.ref(2, a0).offset(0x18L).get()));
        sp28.h.set((short)MEMORY.ref(2, a0).offset(0x6L).get());
      } else {
        //LAB_800cdf14
        sp10.x.set((short)MEMORY.ref(2, a0).get());
        sp10.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp10.w.set((short)-MEMORY.ref(2, a0).offset(0x18L).get());
        sp10.h.set((short)MEMORY.ref(2, a0).offset(0x6L).get());
        sp18.x.set((short)(MEMORY.ref(2, a0).get() - MEMORY.ref(2, a0).offset(0x18L).get()));
        sp18.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp18.w.set((short)(MEMORY.ref(2, a0).offset(0x4L).get() + MEMORY.ref(2, a0).offset(0x18L).get()));
        sp18.h.set((short)MEMORY.ref(2, a0).offset(0x6L).get());
        sp20.x.set((short)(MEMORY.ref(2, a0).get() + MEMORY.ref(2, a0).offset(0x4L).get() + MEMORY.ref(2, a0).offset(0x18L).get()));
        sp20.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp20.w.set((short)-MEMORY.ref(2, a0).offset(0x18L).get());
        sp20.h.set((short)MEMORY.ref(2, a0).offset(0x6L).get());
        sp28.x.set((short)MEMORY.ref(2, a0).get());
        sp28.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp28.w.set((short)(MEMORY.ref(2, a0).offset(0x4L).get() + MEMORY.ref(2, a0).offset(0x18L).get()));
        sp28.h.set((short)MEMORY.ref(2, a0).offset(0x6L).get());
      }

      //LAB_800ce090
    } else {
      //LAB_800ce098
      v1 = MEMORY.ref(2, a0).offset(0x18L).get();
      a1 = MEMORY.ref(2, a0).offset(0x6L).get();
      MEMORY.ref(2, a0).offset(0x18L).setu(v1 % a1);
      if((int)MEMORY.ref(2, a0).offset(0x18L).get() > 0) {
        sp10.x.set((short)MEMORY.ref(2, a0).get());
        sp10.y.set((short)(MEMORY.ref(2, a0).offset(0x2L).get() + MEMORY.ref(2, a0).offset(0x6L).get() - MEMORY.ref(2, a0).offset(0x18L).get()));
        sp10.w.set((short)MEMORY.ref(2, a0).offset(0x4L).get());
        sp10.h.set((short)MEMORY.ref(2, a0).offset(0x18L).get());
        sp18.x.set((short)MEMORY.ref(2, a0).get());
        sp18.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp18.w.set((short)MEMORY.ref(2, a0).offset(0x4L).get());
        sp18.h.set((short)(MEMORY.ref(2, a0).offset(0x6L).get() - MEMORY.ref(2, a0).offset(0x18L).get()));
        sp20.x.set((short)MEMORY.ref(2, a0).get());
        sp20.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp20.w.set((short)MEMORY.ref(2, a0).offset(0x4L).get());
        sp20.h.set((short)MEMORY.ref(2, a0).offset(0x18L).get());
        sp28.x.set((short)MEMORY.ref(2, a0).get());
        sp28.y.set((short)(MEMORY.ref(2, a0).offset(0x2L).get() + MEMORY.ref(2, a0).offset(0x18L).get()));
        sp28.w.set((short)MEMORY.ref(2, a0).offset(0x4L).get());
        sp28.h.set((short)(MEMORY.ref(2, a0).offset(0x6L).get() - MEMORY.ref(2, a0).offset(0x18L).get()));
      } else {
        //LAB_800ce25c
        sp10.x.set((short)MEMORY.ref(2, a0).get());
        sp10.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp10.w.set((short)MEMORY.ref(2, a0).offset(0x4L).get());
        sp10.h.set((short)-MEMORY.ref(2, a0).offset(0x18L).get());
        sp18.x.set((short)MEMORY.ref(2, a0).get());
        sp18.y.set((short)(MEMORY.ref(2, a0).offset(0x2L).get() - MEMORY.ref(2, a0).offset(0x18L).get()));
        sp18.w.set((short)MEMORY.ref(2, a0).offset(0x4L).get());
        sp18.h.set((short)(MEMORY.ref(2, a0).offset(0x6L).get() + MEMORY.ref(2, a0).offset(0x18L).get()));
        sp20.x.set((short)MEMORY.ref(2, a0).get());
        sp20.y.set((short)(MEMORY.ref(2, a0).offset(0x2L).get() + MEMORY.ref(2, a0).offset(0x6L).get() + MEMORY.ref(2, a0).offset(0x18L).get()));
        sp20.w.set((short)MEMORY.ref(2, a0).offset(0x4L).get());
        sp20.h.set((short)-MEMORY.ref(2, a0).offset(0x18L).get());
        sp28.x.set((short)MEMORY.ref(2, a0).get());
        sp28.y.set((short)MEMORY.ref(2, a0).offset(0x2L).get());
        sp28.w.set((short)MEMORY.ref(2, a0).offset(0x4L).get());
        sp28.h.set((short)(MEMORY.ref(2, a0).offset(0x6L).get() + MEMORY.ref(2, a0).offset(0x18L).get()));
      }
    }

    //LAB_800ce3d8
    StoreImage(sp10, MEMORY.ref(4, a0).offset(0xcL).get());
    StoreImage(sp18, MEMORY.ref(4, a0).offset(0x8L).get());
    LoadImage(sp20, MEMORY.ref(4, a0).offset(0xcL).get());
    LoadImage(sp28, MEMORY.ref(4, a0).offset(0x8L).get());

    //LAB_800ce434
  }

  @Method(0x800ce448L)
  public static void FUN_800ce448(final long a0) {
    free(MEMORY.ref(4, a0).offset(0xcL).get());
    free(MEMORY.ref(4, a0).offset(0x8L).get());
    free(a0);
  }
}
