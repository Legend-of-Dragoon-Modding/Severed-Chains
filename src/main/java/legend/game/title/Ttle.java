package legend.game.title;

import legend.core.Config;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
import legend.core.gpu.Rect4i;
import legend.core.gpu.VramTexture;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.core.opengl.Window;
import legend.game.SaveManager;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.fmv.Fmv;
import legend.game.inventory.WhichMenu;
import legend.game.tim.Tim;
import legend.game.types.CharacterData2c;
import legend.game.types.GsRVIEW2;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.gpu.VramTextureLoader.fromTim;
import static legend.core.gpu.VramTextureLoader.stitchHorizontal;
import static legend.core.gpu.VramTextureLoader.stitchVertical;
import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.Scus94491BpeSegment.decrementOverlayCount;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.renderMcq;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a9c0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bcc8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bda4;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.loadAndRenderMenus;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_Xyz;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndexOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.setMono;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b.afterFmvLoadingStage_800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.fmvIndex_800bf0dc;
import static legend.game.Scus94491BpeSegment_800b.gameOverMcq_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.identityMatrix_800c3568;

public final class Ttle {
  private Ttle() { }

  public static TmdRenderingStruct _800c66d0;
  public static final FireAnimationData20[] fireAnimation_800c66d4 = new FireAnimationData20[4];
  public static int hasSavedGames;
  public static int menuLoadingStage;
  public static int logoFadeInAmount;
  public static int logoFlashStage;
  public static int logoFlashColour;
  public static boolean backgroundInitialized;
  public static int backgroundScrollAmount;
  public static int backgroundFadeInAmount;
  public static boolean copyrightInitialized;
  public static int copyrightFadeInAmount;
  public static boolean logoFireInitialized;
  public static int flameColour;
  public static int menuIdleTime;

  public static int _800c6728;
  public static int _800c672c;
  public static final int[] menuOptionTransparency = {0, 0, 0};

  public static int _800c6738;
  public static final int[] configColours = {0, 0, 0, 0, 0, 0};

  public static int _800c6754;
  public static int flamesZ;

  public static final GsRVIEW2 GsRVIEW2_800c6760 = new GsRVIEW2();

  public static final int[] characterStartingLevels = {1, 3, 4, 8, 13, 15, 17, 19, 23};

  public static int selectedMenuOption;
  public static int selectedConfigCategory;

  private static VramTexture backgroundTexture;
  private static VramTexture logoTexture;
  private static VramTexture menuTextTexture;
  private static VramTexture tmTexture;
  private static VramTexture copyrightTexture;
  private static VramTexture fireTexture;

  // This is all data stored in the overlay rom
  public static final ArrayRef<Pointer<ArrayRef<IntRef>>> characterXpPtrs_800ce6d8 = MEMORY.ref(4, 0x800ce6d8L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(IntRef.class)), 9, 4, Pointer.deferred(4, ArrayRef.of(IntRef.class, 61, 4, IntRef::new))));
  public static final ArrayRef<ArrayRef<UnsignedShortRef>> startingEquipment_800ce6fc = MEMORY.ref(2, 0x800ce6fcL, ArrayRef.of(ArrayRef.classFor(UnsignedShortRef.class), 9, 0xa, ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new)));
  public static final ArrayRef<ShortRef> startingAddition_800ce758 = MEMORY.ref(2, 0x800ce758L, ArrayRef.of(ShortRef.class, 9, 2, ShortRef::new));
  public static final ArrayRef<UnsignedShortRef> startingItems_800ce76c = MEMORY.ref(2, 0x800ce76cL, ArrayRef.of(UnsignedShortRef.class, 9, 2, UnsignedShortRef::new));

  public static final ArrayRef<UnsignedByteRef> _800ce7b0 = MEMORY.ref(1, 0x800ce7b0L, ArrayRef.of(UnsignedByteRef.class, 4, 1, UnsignedByteRef::new));
  public static final Value _800ce7f8 = MEMORY.ref(1, 0x800ce7f8L);
  public static final Value _800ce840 = MEMORY.ref(1, 0x800ce840L);
  public static final Value _800ce8ac = MEMORY.ref(2, 0x800ce8acL);

  private static Window.Events.Cursor onMouseMove;
  private static Window.Events.Click onMouseRelease;
  private static Window.Events.Key onKeyPress;

  @Method(0x800c7194L)
  public static void setUpNewGameData() {
    final int oldVibration = gameState_800babc8.vibrationEnabled_4e1.get();
    final int oldMono = gameState_800babc8.mono_4e0.get();

    bzero(gameState_800babc8.getAddress(), 0x52c);

    gameState_800babc8.vibrationEnabled_4e1.set(oldVibration);
    gameState_800babc8.mono_4e0.set(oldMono);
    gameState_800babc8.indicatorMode_4e8.set(2);
    gameState_800babc8.charIndex_88.get(0).set(0);
    gameState_800babc8.charIndex_88.get(1).set(-1);
    gameState_800babc8.charIndex_88.get(2).set(-1);

    //LAB_800c723c
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);
      final int level = characterStartingLevels[charIndex];
      charData.xp_00.set(characterXpPtrs_800ce6d8.get(charIndex).deref().get(level).get());
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
      charData.selectedAddition_19.set(startingAddition_800ce758.get(charIndex).get());

      //LAB_800c7334
      for(int i = 0; i < 5; i++) {
        charData.equipment_14.get(i).set(startingEquipment_800ce6fc.get(charIndex).get(i).get());
      }
    }

    gameState_800babc8.charData_32c.get(0).partyFlags_04.set(0x3);

    //LAB_800c7398
    for(int i = 0x100; i >= 0; i--) {
      gameState_800babc8.equipment_1e8.get(i).set(0xff);
    }

    gameState_800babc8.equipmentCount_1e4.set((short)0);

    //LAB_800c73b8
    for(int i = Config.inventorySize(); i >= 0; i--) {
      gameState_800babc8.items_2e9.get(i).set(0xff);
    }

    //LAB_800c73d8
    for(int i = 0; i < Config.inventorySize() + 1; i++) {
      final int itemId = startingItems_800ce76c.get(i).get();
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
    loadSItemAndSetUpNewGameData();
    mainCallbackIndexOnceLoaded_8004dd24.set(5);
    vsyncMode_8007a3b8.set(2);
    pregameLoadingStage_800bb10c.set(0);
  }

  @Method(0x800c7524L)
  public static void loadSItemAndSetUpNewGameData() {
    loadSupportOverlay(2, Ttle::setUpNewGameData);
  }

  @Method(0x800c7558L)
  public static void FUN_800c7558(final long address, final int fileSize, final int param) {
    final RECT rect = new RECT().set((short)640, (short)0, (short)MEMORY.ref(2, address).offset(0x8L).get(), (short)MEMORY.ref(2, address).offset(0xaL).get());
    gameOverMcq_800bdc3c.setPointer(address);
    LoadImage(rect, address + MEMORY.ref(4, address).offset(0x4L).get());
    pregameLoadingStage_800bb10c.set(3);
  }

  @Method(0x800c75b4L)
  public static void renderGameOver() {
    renderMcq(gameOverMcq_800bdc3c.deref(), 640, 0, -320, -108, 36, 128);
  }

  @Method(0x800c75fcL)
  public static void FUN_800c75fc() {
    switch(pregameLoadingStage_800bb10c.get()) {
      case 0 -> {
        FUN_8002a9c0();
        setWidthAndFlags(640);
        pregameLoadingStage_800bb10c.set(1);
      }

      case 1 -> {
        pregameLoadingStage_800bb10c.set(2);
        loadDrgnBinFile(0, 6667, 0, Ttle::FUN_800c7558, 0, 0x2L);
      }

      case 3 -> {
        deallocateRenderables(0xffL);
        scriptStartEffect(2, 10);
        pregameLoadingStage_800bb10c.set(4);
      }

      case 4 -> {
        if((joypadPress_8007a398.get() & 0x820) != 0) {
          Scus94491BpeSegment_8002.playSound(2);
          pregameLoadingStage_800bb10c.set(5);
          scriptStartEffect(1, 10);
        }

        renderGameOver();
      }

      case 5 -> {
        if(_800bb168.get() >= 0xff) {
          pregameLoadingStage_800bb10c.set(6);
        }

        //LAB_800c7740
        renderGameOver();
      }

      case 6 -> {
        deallocateRenderables(0xffL);
        free(drgn0_6666FilePtr_800bdc3c.getPointer());
        mainCallbackIndexOnceLoaded_8004dd24.set(2);
        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8.set(2);
      }
    }

    //LAB_800c7788
  }

  @Method(0x800c7798L)
  public static void executeTtleLoadingStage() {
    switch(pregameLoadingStage_800bb10c.get()) {
      case 0 -> initializeMainMenu();
      case 3 -> renderMainMenu();
      case 4 -> fadeOutForNewGame();
      case 5 -> waitForSaveSelection();
      case 6 -> fadeOutMainMenu();
    }
  }

  @Method(0x800c77e4L)
  public static void initializeMainMenu() {
    menuLoadingStage = 0;
    menuIdleTime = 0;
    _800c6728 = 0;
    _800c672c = 0;
    _800c6738 = 0;
    logoFadeInAmount = 0;
    backgroundInitialized = false;
    backgroundScrollAmount = -176;
    copyrightInitialized = false;
    logoFireInitialized = false;
    logoFlashStage = 0;
    _800c6754 = 0;
    flamesZ = 100;

    hasSavedGames = 0;
    selectedMenuOption = 0;
    selectedConfigCategory = 0;

    setWidthAndFlags(384);
    setProjectionPlaneDistance(320);
    GsRVIEW2_800c6760.viewpoint_00.set(0, 0, 2000);
    GsRVIEW2_800c6760.refpoint_0c.set(0, 0, -4000);
    GsRVIEW2_800c6760.viewpointTwist_18 = 0;
    GsRVIEW2_800c6760.super_1c = null;
    GsSetRefView2(GsRVIEW2_800c6760);

    vsyncMode_8007a3b8.set(2);

    loadDrgnDir(0, 5718, Ttle::menuTexturesMrgLoaded);
    loadDrgnFile(0, 5719, file -> menuFireTmdLoaded("DRGN0/5719", file));

    // Prepare fire animation struct
    //LAB_800c7d30
    for(int i = 0; i < 4; i++) {
      //LAB_800c7d4c
      final Rect4i rect = new Rect4i(944 + i * 16, 0, 64, 64);
      fireAnimation_800c66d4[i] = FUN_800cdaa0(rect, 0, 1, _800ce7b0.get(i).get());
    }

    scriptStartEffect(2, 15);
    SetGeomOffset(0, 0);
    pregameLoadingStage_800bb10c.set(3);

    addInputHandlers();
  }

  /**
   * Loads the MRG file @ sector 61510. All files are TIMs.
   * <ol start="0">
   *   <li>Menu background (upper portion)</li>
   *   <li>Menu background (lower portion)</li>
   *   <li>Logo</li>
   *   <li>Menu text</li>
   *   <li>TM</li>
   *   <li>Copyright (left half)</li>
   *   <li>Copyright (right half)</li>
   *   <li>Logo fire 2</li>
   *   <li>Logo fire 3 (same as 4)</li>
   *   <li>Logo fire 4 (same as 3)</li>
   *   <li>Logo fire 1</li>
   * </ol>
   */
  @Method(0x800c7af0L)
  public static void menuTexturesMrgLoaded(final List<FileData> files) {
    backgroundTexture = stitchVertical(
      fromTim(new Tim(files.get(0))),
      fromTim(new Tim(files.get(1)))
    );

    logoTexture = fromTim(new Tim(files.get(2)));
    menuTextTexture = fromTim(new Tim(files.get(3)));
    tmTexture = fromTim(new Tim(files.get(4)));

    copyrightTexture = stitchHorizontal(
      fromTim(new Tim(files.get(5))),
      fromTim(new Tim(files.get(6)))
    );

    fireTexture = stitchHorizontal(
      fromTim(new Tim(files.get(10))),
      fromTim(new Tim(files.get(7))),
      fromTim(new Tim(files.get(8))),
      fromTim(new Tim(files.get(9)))
    );
  }

  @Method(0x800c7c18L)
  public static void menuFireTmdLoaded(final String modelName, final FileData file) {
    final TmdWithId tmd = new TmdWithId(modelName, file);
    _800c66d0 = parseTmdFile(tmd);
    FUN_800cc0b0(_800c66d0, null);
    _800c66d0.tmd_0c = tmd;
    setDobjAttributes(_800c66d0, 0);
  }

  @Method(0x800c7e50L)
  public static void fadeOutForNewGame() {
    if(_800c6754 == 0) {
      scriptStartEffect(1, 15);
    }

    //LAB_800c7e7c
    renderMenuLogo();
    renderMenuOptions();
    renderOptionsMenu();
    renderMenuLogoFire();
    renderMenuBackground();
    renderCopyright();

    _800c6754++;
    if(_800c6754 >= 16) {
      removeInputHandlers();
      deallocateFire();

      fmvIndex_800bf0dc.setu(0x2L);
      afterFmvLoadingStage_800bf0ec.set(3);
      Fmv.playCurrentFmv();

      pregameLoadingStage_800bb10c.set(0);
    }

    //LAB_800c7f90
  }

  @Method(0x800c7fa0L)
  public static void waitForSaveSelection() {
    if(_800c6754 == 0) {
      scriptStartEffect(1, 15);
    }

    //LAB_800c7fcc
    _800c6754++;

    if(_800c6754 >= 16) {
      if(_800c6728 == 2) {
        whichMenu_800bdc38 = WhichMenu.INIT_LOAD_GAME_MENU_11;
        removeInputHandlers();
        deallocateFire();
        _800c6728 = 3;
      }
    }

    //LAB_800c8038
    loadAndRenderMenus();

    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      if(_800bdc34.get() != 0) {
        if(gameState_800babc8.isOnWorldMap_4e4.get() != 0) {
          mainCallbackIndexOnceLoaded_8004dd24.set(8); // WMAP
        } else {
          //LAB_800c80a4
          mainCallbackIndexOnceLoaded_8004dd24.set(5); // SMAP
        }

        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8.set(2);

        //LAB_800c80c4
        return;
      }

      //LAB_800c80cc
      if(_800c6728 == 3) {
        mainCallbackIndexOnceLoaded_8004dd24.set(2);
        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8.set(2);
      } else {
        //LAB_800c8108
        renderMenuLogo();
        renderMenuOptions();
        renderOptionsMenu();
        renderMenuLogoFire();
        renderMenuBackground();
        renderCopyright();
      }
    }

    //LAB_800c8138
  }

  @Method(0x800c8148L)
  public static void fadeOutMainMenu() {
    if(_800c6754 == 0) {
      scriptStartEffect(1, 15);
    }

    //LAB_800c8174
    renderMenuLogo();
    renderMenuOptions();
    renderOptionsMenu();
    renderMenuLogoFire();
    renderMenuBackground();
    renderCopyright();

    _800c6754++;
    if(_800c6754 > 15) {
      removeInputHandlers();
      deallocateFire();

      fmvIndex_800bf0dc.setu(0);
      afterFmvLoadingStage_800bf0ec.set(2);
      Fmv.playCurrentFmv();

      pregameLoadingStage_800bb10c.set(0);
    }

    //LAB_800c8218
  }

  @Method(0x800c8298L)
  public static void renderMainMenu() {
    if(menuLoadingStage == 0) {
      //LAB_800c82f0
      if(backgroundScrollAmount > -40) {
        menuLoadingStage = 1;
      }

      //LAB_800c82d0
      //LAB_800c8314
    } else if(menuLoadingStage == 1) {
      //LAB_800c831c
      renderMenuLogo();

      if(logoFadeInAmount >= 128) {
        menuLoadingStage = 2;
      }

      //LAB_800c8348
    } else if(menuLoadingStage == 2) {
      //LAB_800c8350
      renderMenuLogo();
      renderLogoFlash();

      if(logoFlashStage == 2) {
        menuLoadingStage = 3;
      }

      //LAB_800c8380
    } else if(menuLoadingStage == 3) {
      //LAB_800c8388
      handleOptionsInput();
      handleMainInput();
      renderMenuLogo();
      renderMenuOptions();
      renderOptionsMenu();
      renderMenuLogoFire();
      renderCopyright();
    }

    //LAB_800c83c8
    renderMenuBackground();

    if(_800c6728 != 1) {
      menuIdleTime += 2;

      if(menuIdleTime > 1680) {
        pregameLoadingStage_800bb10c.set(6);
      }
    }

    //LAB_800c8448
    if(joypadInput_8007a39c.get(0xf9ffL) != 0) {
      menuLoadingStage = 3;
      menuIdleTime = 0;
    }

    //LAB_800c8474
  }

  private static void addInputHandlers() {
    onMouseMove = GPU.window().events.onMouseMove((window, x, y) -> {
      final float aspect = (float)GPU.getDisplayTextureWidth() / GPU.getDisplayTextureHeight();

      float w = window.getWidth();
      float h = w / aspect;

      if(h > window.getHeight()) {
        h = window.getHeight();
        w = h * aspect;
      }

      final float scaleX = w / GPU.getDisplayTextureWidth();
      final float scaleY = h / GPU.getDisplayTextureHeight();

      if(menuLoadingStage == 3) {
        if(_800c672c < 3) {
          for(int i = 0; i < 3; i++) {
            if(i == 1 && hasSavedGames != 1) {
              continue;
            }

            final int menuWidth = (int)(130 * scaleX);
            final int menuHeight = (int)(16 * scaleY);
            final int menuX = (window.getWidth() - menuWidth) / 2;
            final int menuY = (int)(_800ce8ac.offset((i * 2 + 1) * 4).getSigned() * scaleY) + window.getHeight() / 2;

            if(MathHelper.inBox((int)x, (int)y, menuX, menuY, menuWidth, menuHeight)) {
              if(selectedMenuOption != i) {
                playSound(0, 1, 0, 0, (short)0, (short)0);
                selectedMenuOption = i;
              }

              break;
            }
          }
        } else if(_800c6728 == 1 && _800c6738 < 3) {
          for(int row = 0; row < 2; row++) {
            final int menuWidth = (int)(300 * scaleX);
            final int menuHeight = (int)(16 * scaleY);
            final int menuX = (window.getWidth() - menuWidth) / 2;
            final int menuY = (int)(_800ce8ac.offset(((row * 3 + 3) * 2 + 1) * 4).getSigned() * scaleY) + window.getHeight() / 2;

            if(MathHelper.inBox((int)x, (int)y, menuX, menuY, menuWidth, menuHeight)) {
              if(selectedConfigCategory != row * 3) {
                playSound(0, 1, 0, 0, (short)0, (short)0);
                selectedConfigCategory = row * 3;
                _800c6738 = 2;
              }

              break;
            }
          }
        }
      }

      menuIdleTime = 0;
    });

    onMouseRelease = GPU.window().events.onMouseRelease((window, x, y, button, mods) -> {
      if(button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
        return;
      }

      if(menuLoadingStage < 3) {
        menuLoadingStage = 3;
      } else {
        final float aspect = (float)GPU.getDisplayTextureWidth() / GPU.getDisplayTextureHeight();

        float w = window.getWidth();
        float h = w / aspect;

        if(h > window.getHeight()) {
          h = window.getHeight();
          w = h * aspect;
        }

        final float scaleX = w / GPU.getDisplayTextureWidth();
        final float scaleY = h / GPU.getDisplayTextureHeight();

        if(_800c672c < 3) {
          for(int i = 0; i < 3; i++) {
            if(i == 1 && hasSavedGames != 1) {
              continue;
            }

            final int menuWidth = (int)(130 * scaleX);
            final int menuHeight = (int)(16 * scaleY);
            final int menuX = (window.getWidth() - menuWidth) / 2;
            final int menuY = (int)(_800ce8ac.offset((i * 2 + 1) * 4).getSigned() * scaleY) + window.getHeight() / 2;

            if(MathHelper.inBox((int)x, (int)y, menuX, menuY, menuWidth, menuHeight)) {
              playSound(0, 2, 0, 0, (short)0, (short)0);
              selectedMenuOption = i;

              _800c672c = 3;
              if(selectedMenuOption == 2) {
                _800c6738 = 0;
                selectedConfigCategory = 0;
                _800c6728 = 1;
              }

              break;
            }
          }
        } else if(_800c6728 == 1 && _800c6738 < 3) {
          for(int i = 0; i < 6; i++) {
            if(i % 3 != 0) {
              final int menuWidth = (int)(_800ce7f8.offset(((i + 3) * 2 + 1) * 4).get() * scaleX);
              final int menuHeight = (int)(16 * scaleY);
              final int menuX = (int)(_800ce8ac.offset((i + 3) * 8).getSigned() * scaleX) + window.getWidth() / 2;
              final int menuY = (int)(_800ce8ac.offset(((i + 3) * 2 + 1) * 4).getSigned() * scaleY) + window.getHeight() / 2;

              if(MathHelper.inBox((int)x, (int)y, menuX, menuY, menuWidth, menuHeight)) {
                playSound(0, 2, 0, 0, (short)0, (short)0);

                switch(i) {
                  case 1 -> gameState_800babc8.mono_4e0.set(0);
                  case 2 -> gameState_800babc8.mono_4e0.set(1);
                  case 4 -> {
                    gameState_800babc8.vibrationEnabled_4e1.set(1);

                    if(gameState_800babc8.vibrationEnabled_4e1.get() != 0) {
                      FUN_8002bcc8(0, 0x100);
                      FUN_8002bda4(0, 0, 0x3c);
                    }
                  }
                  case 5 -> {
                    gameState_800babc8.vibrationEnabled_4e1.set(0);

                    if(gameState_800babc8.vibrationEnabled_4e1.get() != 0) {
                      FUN_8002bcc8(0, 0x100);
                      FUN_8002bda4(0, 0, 0x3c);
                    }
                  }
                }
              }
            }
          }
        }
      }
    });

    onKeyPress = GPU.window().events.onKeyPress((window, key, scancode, mods) -> {
      if(_800c6728 == 1 && _800c6738 < 3) {
        if(key == GLFW.GLFW_KEY_ESCAPE) {
          playSound(0, 3, 0, 0, (short)0, (short)0);
          _800c6738 = 3;
          _800c672c = 0;
        }
      }
    });
  }

  private static void removeInputHandlers() {
    GPU.window().events.removeMouseMove(onMouseMove);
    GPU.window().events.removeMouseRelease(onMouseRelease);
    GPU.window().events.removeKeyPress(onKeyPress);
    onMouseMove = null;
    onMouseRelease = null;
    onKeyPress = null;
  }

  @Method(0x800c8484L)
  public static void handleMainInput() {
    if(_800c672c < 3) {
      if(joypadPress_8007a398.get(0x20L) != 0) { // Menu button X
        playSound(0, 2, 0, 0, (short)0, (short)0);

        _800c672c = 3;
        if(selectedMenuOption == 2) {
          _800c6738 = 0;
          selectedConfigCategory = 0;
          _800c6728 = 1;
        }
      } else if(joypadPress_8007a398.get(0x1000L) != 0) { // Menu button up
        playSound(0, 1, 0, 0, (short)0, (short)0);

        selectedMenuOption--;
        if(selectedMenuOption < 0) {
          selectedMenuOption = 2;
        }

        if(selectedMenuOption == 1 && hasSavedGames != 1) {
          selectedMenuOption--;
        }

        _800c672c = 2;
      } else if(joypadPress_8007a398.get(0x4000L) != 0) { // Menu button down
        playSound(0, 1, 0, 0, (short)0, (short)0);

        selectedMenuOption++;
        if(selectedMenuOption > 2) {
          selectedMenuOption = 0;
        }

        if(selectedMenuOption == 1 && hasSavedGames != 1) {
          selectedMenuOption++;
        }

        _800c672c = 2;
      }
    }
  }

  @Method(0x800c8634L)
  public static void renderMenuOptions() {
    if(hasSavedGames == 0) {
      hasSavedGames = SaveManager.hasSavedGames() ? 1 : 2;
      selectedMenuOption = hasSavedGames == 1 ? 1 : 0;
      return;
    }

    //LAB_800c868c
    switch(_800c672c) {
      case 0 -> {
        //LAB_800c86d8
        for(int i = 0; i < 3; i++) {
          //LAB_800c86f4
          menuOptionTransparency[i] = 0;
        }

        //LAB_800c8728
        _800c672c = 1;
      }

      case 1 -> {
        //LAB_800c8740
        //LAB_800c886c
        for(int i = 0; i < 3; i++) {
          //LAB_800c875c
          menuOptionTransparency[i] += 4;
          if(selectedMenuOption == i) {
            if(menuOptionTransparency[i] > 176) {
              menuOptionTransparency[i] = 176;
            }

            //LAB_800c8800
            //LAB_800c8808
          } else if(menuOptionTransparency[i] > 64) {
            menuOptionTransparency[i] = 64;
          }

          //LAB_800c8854
        }
      }

      case 2 -> {
        //LAB_800c8878
        //LAB_800c89e4
        for(int i = 0; i < 3; i++) {
          //LAB_800c8894
          if(selectedMenuOption == i) {
            // Fade in selected item

            menuOptionTransparency[i] += 8;
            if(menuOptionTransparency[i] > 160) {
              menuOptionTransparency[i] = 160;
            }

            //LAB_800c8938
          } else {
            // Fade out unselected items

            //LAB_800c8940
            menuOptionTransparency[i] -= 16;
            if(menuOptionTransparency[i] < 64) {
              menuOptionTransparency[i] = 64;
            }
          }

          //LAB_800c89cc
        }
      }

      case 3 -> {
        _800c672c = 4;
        if(selectedMenuOption == 0) {
          pregameLoadingStage_800bb10c.set(4);
          //LAB_800c8a20
        } else if(selectedMenuOption == 1) {
          _800c6728 = 2;
          pregameLoadingStage_800bb10c.set(5);
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

      final int colour;
      if(i != 1 || hasSavedGames == 1) {
        colour = menuOptionTransparency[i];
      } else {
        colour = menuOptionTransparency[i] / 2;
      }

      //LAB_800c8a8c
      GPU.queueCommand(100, new GpuCommandPoly(4)
        .texture(menuTextTexture)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .monochrome(colour)
        .pos(0, x, y)
        .uv(0, 0, (int)_800ce7f8.offset(i * 2 * 4).get())
        .pos(1, x + (int)_800ce7f8.offset(2, (i * 2 + 1) * 4).get(), y)
        .uv(1, (int)_800ce7f8.offset((i * 2 + 1) * 4).get(), (int)_800ce7f8.offset(i * 2 * 4).get())
        .pos(2, x, y + 16)
        .uv(2, 0, (int)_800ce7f8.offset(i * 2 * 4).get() + 16)
        .pos(3, x + (int)_800ce7f8.offset(2, (i * 2 + 1) * 4).get(), y + 16)
        .uv(3, (int)_800ce7f8.offset((i * 2 + 1) * 4).get(), (int)_800ce7f8.offset(i * 2 * 4).get() + 16)
        .clut(0, selectedMenuOption == i ? 5 : 2)
      );

      GPU.queueCommand(100, new GpuCommandPoly(4)
        .texture(menuTextTexture)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .monochrome(colour)
        .pos(0, x - 8, y - 8)
        .uv(0, (int)_800ce840.offset(i * 3 * 4).get(), (int)_800ce840.offset((i * 3 + 1) * 4).get())
        .clut(0, 4)
        .pos(1, x - 8 + (int)_800ce840.offset((i * 3 + 2) * 4).get(), y - 8)
        .uv(1, (int)(_800ce840.offset(i * 3 * 4).get() + _800ce840.offset((i * 3 + 2) * 4).get()), (int)_800ce840.offset((i * 3 + 1) * 4).get())
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
  public static void handleOptionsInput() {
    if(_800c6728 == 1 && _800c6738 < 3) {
      if(joypadPress_8007a398.get(0x5000) != 0) { // Up, down
        playSound(0, 1, 0, 0, (short)0, (short)0);
        selectedConfigCategory ^= 0b11;
        _800c6738 = 2;
      }

      if(joypadPress_8007a398.get(0x40) != 0) { // Back
        playSound(0, 3, 0, 0, (short)0, (short)0);
        _800c6738 = 3;
        _800c672c = 0;
      }

      if(joypadPress_8007a398.get(0xa000) != 0) { // Left, right
        playSound(0, 1, 0, 0, (short)0, (short)0);

        if(selectedConfigCategory == 0) {
          gameState_800babc8.mono_4e0.xor(0b1);
          setMono(gameState_800babc8.mono_4e0.get());
        } else {
          gameState_800babc8.vibrationEnabled_4e1.xor(0b1);

          if(gameState_800babc8.vibrationEnabled_4e1.get() != 0) {
            FUN_8002bcc8(0, 0x100);
            FUN_8002bda4(0, 0, 0x3c);
          }
        }

        _800c6738 = 2;
      }
    }
  }

  @Method(0x800c959cL)
  public static void renderOptionsMenu() {
    if(_800c6728 != 1) {
      return;
    }

    //LAB_800c95c4
    int sp18 = gameState_800babc8.mono_4e0.get() + 1;

    int sp1c;
    if(gameState_800babc8.vibrationEnabled_4e1.get() == 0) {
      //LAB_800c95f8
      sp1c = 5;
    } else {
      sp1c = 4;
    }

    //LAB_800c95fc
    switch(_800c6738) {
      case 0 -> {
        //LAB_800c964c
        for(int i = 0; i < 6; i++) {
          //LAB_800c9668
          configColours[i] = 0;
        }

        //LAB_800c969c
        _800c6738 = 1;
      }

      case 0x1 -> {
        //LAB_800c96b4
        for(int i = 0; i < 6; i++) {
          //LAB_800c96d0
          configColours[i] += 16;
          if(selectedConfigCategory == i || sp18 == i || sp1c == i) {
            //LAB_800c9758
            if(configColours[i] > 176) {
              configColours[i] = 176;
            }

            //LAB_800c97a4
          } else {
            //LAB_800c97ac
            if(configColours[i] > 64) {
              configColours[i] = 64;
            }
          }

          //LAB_800c97f8
        }

        //LAB_800c9810
      }

      case 0x2 -> {
        //LAB_800c981c
        for(int i = 0; i < 6; i++) {
          //LAB_800c9838
          if(selectedConfigCategory == i || sp18 == i || sp1c == i) {
            //LAB_800c9880
            configColours[i] += 8;
            if(configColours[i] > 160) {
              configColours[i] = 160;
            }

            //LAB_800c990c
          } else {
            //LAB_800c9914
            configColours[i] = 16;
            if(configColours[i] < 64) {
              configColours[i] = 64;
            }
          }

          //LAB_800c99a0
        }

        //LAB_800c99b8
      }

      case 0x3 -> {
        //LAB_800c99c4
        for(int i = 0; i < 6; i++) {
          //LAB_800c99e0
          if(selectedConfigCategory == i || sp18 == i || sp1c == i) {
            //LAB_800c9a28
            configColours[i] -= 32;
          } else {
            //LAB_800c9a70
            configColours[i] -= 8;
          }

          //LAB_800c9ab0
          if(configColours[i] < 0) {
            configColours[i] = 0;
          }

          //LAB_800c9af4
          if(configColours[selectedConfigCategory] == 0) {
            _800c6738 = 4;
            _800c6728 = 0;
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
        .texture(menuTextTexture)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .monochrome(configColours[i])
        .clut(0, selectedConfigCategory == i ? 5 : 2)
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

    // Render glowing overlay for Sound/Vibrate options
    for(int i = 0; i < 2; i++) {
      //LAB_800ca018
      sp18 = i * 3;

      GPU.queueCommand(100, new GpuCommandPoly(4)
        .texture(menuTextTexture)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .monochrome(configColours[sp18])
        .clut(0, 4)
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

    // Render glowing overlay for selected Sound/Vibration option
    for(int i = 0; i < 2; i++) {
      //LAB_800ca59c
      final int sp14;
      if(i == 0) {
        sp14 = gameState_800babc8.mono_4e0.get() + 4;
        sp1c = gameState_800babc8.mono_4e0.get() + 1;
      } else {
        //LAB_800ca5dc
        if(gameState_800babc8.vibrationEnabled_4e1.get() == 0) {
          //LAB_800ca5fc
          sp14 = 8;
        } else {
          sp14 = 7;
        }

        //LAB_800ca600
        if(gameState_800babc8.vibrationEnabled_4e1.get() == 0) {
          //LAB_800ca624
          sp1c = 5;
        } else {
          sp1c = 4;
        }

        //LAB_800ca628
      }

      //LAB_800ca62c
      GPU.queueCommand(100, new GpuCommandPoly(4)
        .texture(menuTextTexture)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .monochrome(configColours[sp1c])
        .clut(0, 4)
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
    if(!copyrightInitialized) {
      copyrightFadeInAmount = 0;
      copyrightInitialized = true;
    }

    //LAB_800cab7c
    copyrightFadeInAmount += 4;
    if(copyrightFadeInAmount > 0x80) {
      copyrightFadeInAmount = 0x80;
    }

    //LAB_800cabb8
    //LAB_800cabcc
    //LAB_800cabe8
    renderQuad(
      copyrightTexture,
      Bpp.BITS_4,
      64,
      copyrightFadeInAmount, copyrightFadeInAmount, copyrightFadeInAmount,
      0, 0,
      368, 32,
      -188, 80,
      368, 32,
      100,
      Translucency.B_PLUS_F
    );
  }

  @Method(0x800cadd0L)
  public static void renderMenuLogo() {
    logoFadeInAmount += 4;
    if(logoFadeInAmount > 0x80) {
      logoFadeInAmount = 0x80;
    }

    //LAB_800cae18
    //LAB_800cae2c
    //LAB_800cae48
    renderQuad(
      logoTexture,
      Bpp.BITS_4,
      1,
      logoFadeInAmount, logoFadeInAmount, logoFadeInAmount,
      0, 0,
      352, 88,
      -184, -80,
      352, 88,
      orderingTableSize_1f8003c8.get() - 4,
      Translucency.B_PLUS_F
    );

    renderQuad(
      tmTexture,
      Bpp.BITS_4,
      80,
      logoFadeInAmount, logoFadeInAmount, logoFadeInAmount,
      0, 0,
      16, 8,
      134, -14,
      16, 8,
      orderingTableSize_1f8003c8.get() - 4,
      Translucency.B_PLUS_F
    );
  }

  @Method(0x800cb070L)
  public static void renderMenuBackground() {
    if(!backgroundInitialized) {
      backgroundScrollAmount = -176;
      backgroundFadeInAmount = 0;
      backgroundInitialized = true;
    }

    //LAB_800cb0b0
    backgroundFadeInAmount += 2;
    if(backgroundFadeInAmount > 0x80) {
      backgroundFadeInAmount = 0x80;
    }

    //LAB_800cb0ec
    //LAB_800cb100
    renderQuad(
      backgroundTexture,
      Bpp.BITS_8,
      0,
      backgroundFadeInAmount, backgroundFadeInAmount, backgroundFadeInAmount,
      0, 0,
      384, 424,
      -192, -120 + backgroundScrollAmount,
      384, 424,
      orderingTableSize_1f8003c8.get() - 3,
      null
    );

    //LAB_800cb370
    backgroundScrollAmount++;
    if(backgroundScrollAmount > 0) {
      backgroundScrollAmount = 0;
    }

    //LAB_800cb3b0
  }

  @Method(0x800cb4c4L)
  public static void renderQuad(@Nullable VramTexture texture, final Bpp bpp, final int clutY, final int r, final int g, final int b, final int u, final int v, final int tw, final int th, final int x, final int y, final int w, final int h, final int z, @Nullable final Translucency translucency) {
    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .texture(texture)
      .bpp(bpp)
      .clut(0, clutY)
      .rgb(r, g, b)
      .pos(0, x, y)
      .pos(1, x + w, y)
      .pos(2, x, y + h)
      .pos(3, x + w, y + h)
      .uv(0, u, v)
      .uv(1, u + tw, v)
      .uv(2, u, v + th)
      .uv(3, u + tw, v + th);

    if(translucency != null) {
      cmd.translucent(translucency);
    }

    GPU.queueCommand(z, cmd);
  }

  @Method(0x800cb69cL)
  public static void deallocateFire() {
    _800c66d0 = null;

    for(int i = 0; i < 4; i++) {
      fireAnimation_800c66d4[i] = null;
    }
  }

  @Method(0x800cb728L)
  public static void renderMenuLogoFire() {
    final SVECTOR rotation = new SVECTOR().set((short)0, (short)-0x800, (short)0);
    final VECTOR scale = new VECTOR().set(0xdac, 0x1000, 0x1000);

    if(!logoFireInitialized) {
      logoFireInitialized = true;
      flameColour = 0;
    }

    //LAB_800cb7b4
    flameColour += 2;
    if(flameColour > 0xff) {
      flameColour = 0xff;
    }

    //LAB_800cb7f0
    GsSetRefView2(GsRVIEW2_800c6760);

    final GsDOBJ2[] dobj2s = _800c66d0.dobj2s_00;
    final GsCOORDINATE2[] coord2s = _800c66d0.coord2s_04;

    //LAB_800cb834
    for(int i = 0; i < _800c66d0.count_08; i++) {
      final MATRIX sp10 = new MATRIX();
      final MATRIX sp30 = new MATRIX();

      //LAB_800cb85c
      FUN_800cc26c(rotation, coord2s[i]);
      GsGetLws(dobj2s[i].coord2_04, sp10, sp30);
      GsSetLightMatrix(sp10);
      ScaleMatrixL(sp30, scale);
      setRotTransMatrix(sp30);
      renderTmd(dobj2s[i], fireTexture);
    }

    //LAB_800cb904
    //LAB_800cb908
    for(int i = 0; i < 4; i++) {
      //LAB_800cb924
      animateFire(fireAnimation_800c66d4[i]);
    }

    //LAB_800cb960
  }

  @Method(0x800cb974L)
  public static void renderLogoFlash() {
    final int sp24 = doubleBufferFrame_800bb108.get() ^ 1;

    if(logoFlashStage == 2) {
      return;
    }

    //LAB_800cb9b0
    if(logoFlashStage == 0) {
      logoFlashStage = 1;
      logoFlashColour = 0;
    }

    //LAB_800cba54
    logoFlashColour += 0x60;
    if(logoFlashColour > 0x800) {
      logoFlashColour = 0x800;
    }

    //LAB_800cba90
    final int colour = rsin(logoFlashColour) * 160 >> 12;

    // GP0.66 Textured quad, variable size, translucent, blended
    final GpuCommandQuad cmd = new GpuCommandQuad()
      .translucent(Translucency.B_PLUS_F)
      .bpp(Bpp.BITS_15)
      .monochrome(colour)
      .pos(-192, -120, 384, 240)
      .uv(0, sp24 * 16)
      .vramPos(0, doubleBufferFrame_800bb108.get() == 0 ? 0 : 256);

    GPU.queueCommand(5, cmd);

    if(logoFlashColour == 0x800) {
      logoFlashStage = 2;
    }

    //LAB_800cbe34
  }

  @Method(0x800cbe48L)
  public static TmdRenderingStruct parseTmdFile(final TmdWithId tmd) {
    final TmdRenderingStruct tmdRenderer = new TmdRenderingStruct();
    tmdRenderer.count_08 = prepareTmdRenderer(tmdRenderer, tmd);
    return tmdRenderer;
  }

  @Method(0x800cbf3cL)
  public static int prepareTmdRenderer(final TmdRenderingStruct tmdRenderer, final TmdWithId tmd) {
    tmdRenderer.dobj2s_00 = new GsDOBJ2[tmd.tmd.header.nobj];
    tmdRenderer.coord2s_04 = new GsCOORDINATE2[tmd.tmd.header.nobj];

    Arrays.setAll(tmdRenderer.dobj2s_00, i -> new GsDOBJ2());
    Arrays.setAll(tmdRenderer.coord2s_04, i -> new GsCOORDINATE2());

    //LAB_800cc02c
    for(int objIndex = 0; objIndex < tmd.tmd.header.nobj; objIndex++) {
      //LAB_800cc04c
      tmdRenderer.dobj2s_00[objIndex].tmd_08 = tmd.tmd.objTable[objIndex];
    }

    //LAB_800cc088
    //LAB_800cc09c
    return tmd.tmd.header.nobj;
  }

  @Method(0x800cc0b0L)
  public static void FUN_800cc0b0(final TmdRenderingStruct renderer, @Nullable final GsCOORDINATE2 superCoord2) {
    //LAB_800cc0f0
    for(int i = 0; i < renderer.count_08; i++) {
      final GsCOORDINATE2 coord2 = renderer.coord2s_04[i];
      final GsDOBJ2 dobj2 = renderer.dobj2s_00[i];

      //LAB_800cc114
      GsInitCoordinate2(superCoord2, coord2);

      dobj2.coord2_04 = coord2;
      coord2.coord.transfer.set(100, -430, -2048);
    }

    //LAB_800cc1a8
  }

  @Method(0x800cc1bcL)
  public static void setDobjAttributes(final TmdRenderingStruct renderer, final int dobjAttribute) {
    //LAB_800cc1e4
    for(int i = 0; i < renderer.count_08; i++) {
      //LAB_800cc208
      renderer.dobj2s_00[i].attribute_00 = dobjAttribute;
    }

    //LAB_800cc25c
  }

  @Method(0x800cc26cL)
  public static void FUN_800cc26c(final SVECTOR a0, final GsCOORDINATE2 a1) {
    final MATRIX m = new MATRIX();
    m.set(identityMatrix_800c3568);
    m.transfer.set(a1.coord.transfer);
    RotMatrix_Xyz(a0, m);
    a1.coord.set(m);
    a1.flg = 0;
  }

  @Method(0x800cc388L)
  public static void renderTmd(final GsDOBJ2 dobj2, final VramTexture texture) {
    final SVECTOR[] vertices = dobj2.tmd_08.vert_top_00;

    //LAB_800cc408
    for(final TmdObjTable1c.Primitive primitive : dobj2.tmd_08.primitives_10) {
      final int command = primitive.header() & 0xff04_0000;

      if(command == 0x3700_0000) {
        renderPrimitive37(primitive, vertices, texture);
      } else if(command == 0x3f00_0000) {
        renderPrimitive3f(primitive, vertices, texture);
      }
    }
  }

  @Method(0x800cc57cL)
  public static void renderPrimitive37(final TmdObjTable1c.Primitive primitive, final SVECTOR[] vertices, final VramTexture texture) {
    //LAB_800cc5b0
    for(final byte[] data : primitive.data()) {
      final GpuCommandPoly cmd = new GpuCommandPoly(3)
        .texture(texture)
        .translucent(Translucency.of(IoHelper.readUShort(data, 0x6) >>> 5 & 0x3));

      //LAB_800cc5c8
      final SVECTOR vert0 = vertices[IoHelper.readUShort(data, 0x18)];
      final SVECTOR vert1 = vertices[IoHelper.readUShort(data, 0x1a)];
      final SVECTOR vert2 = vertices[IoHelper.readUShort(data, 0x1c)];
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VZ1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x280030L); // Perspective transformation triple

      cmd.uv(0, IoHelper.readUByte(data, 0x0), IoHelper.readUByte(data, 0x1));
      cmd.uv(1, IoHelper.readUByte(data, 0x4), IoHelper.readUByte(data, 0x5));
      cmd.uv(2, IoHelper.readUByte(data, 0x8), IoHelper.readUByte(data, 0x9));

      cmd.clut((IoHelper.readUShort(data, 0x2) & 0b111111) * 16, IoHelper.readUShort(data, 0x2) >>> 6);
      cmd.vramPos((IoHelper.readUShort(data, 0x6) & 0b1111) * 64, (IoHelper.readUShort(data, 0x6) & 0b10000) != 0 ? 256 : 0);

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

                    cmd.rgb(0, IoHelper.readUByte(data, 0x0c) * flameColour / 0xff, IoHelper.readUByte(data, 0x0d) * flameColour / 0xff, IoHelper.readUByte(data, 0x0e) * flameColour / 0xff);
                    cmd.rgb(1, IoHelper.readUByte(data, 0x10) * flameColour / 0xff, IoHelper.readUByte(data, 0x11) * flameColour / 0xff, IoHelper.readUByte(data, 0x12) * flameColour / 0xff);
                    cmd.rgb(2, IoHelper.readUByte(data, 0x14) * flameColour / 0xff, IoHelper.readUByte(data, 0x15) * flameColour / 0xff, IoHelper.readUByte(data, 0x16) * flameColour / 0xff);

                    // OTZ - Average Z value (for ordering table)
                    final int z = (int)Math.min(CPU.MFC2(7) + flamesZ >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

                    GPU.queueCommand(z, cmd);
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  @Method(0x800ccb78L)
  public static void renderPrimitive3f(final TmdObjTable1c.Primitive primitive, final SVECTOR[] vertices, final VramTexture texture) {
    //LAB_800ccbcc
    for(final byte[] data : primitive.data()) {
      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .texture(texture)
        .translucent(Translucency.of(IoHelper.readUShort(data, 0x6) >>> 5 & 0x3));

      //LAB_800ccbe4
      final SVECTOR vert0 = vertices[IoHelper.readUShort(data, 0x20)];
      final SVECTOR vert1 = vertices[IoHelper.readUShort(data, 0x22)];
      final SVECTOR vert2 = vertices[IoHelper.readUShort(data, 0x24)];
      CPU.MTC2(vert0.getXY(), 0); // VXY0
      CPU.MTC2(vert0.getZ(),  1); // VZ0
      CPU.MTC2(vert1.getXY(), 2); // VXY1
      CPU.MTC2(vert1.getZ(),  3); // VY1
      CPU.MTC2(vert2.getXY(), 4); // VXY2
      CPU.MTC2(vert2.getZ(),  5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      cmd.uv(0, IoHelper.readUByte(data, 0x0), IoHelper.readUByte(data, 0x1));
      cmd.uv(1, IoHelper.readUByte(data, 0x4), IoHelper.readUByte(data, 0x5));
      cmd.uv(2, IoHelper.readUByte(data, 0x8), IoHelper.readUByte(data, 0x9));
      cmd.uv(3, IoHelper.readUByte(data, 0xc), IoHelper.readUByte(data, 0xd));

      cmd.clut((IoHelper.readUShort(data, 0x2) & 0b111111) * 16, IoHelper.readUShort(data, 0x2) >>> 6);
      cmd.vramPos((IoHelper.readUShort(data, 0x6) & 0b1111) * 64, (IoHelper.readUShort(data, 0x6) & 0b10000) != 0 ? 256 : 0);

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

          final SVECTOR vert3 = vertices[IoHelper.readUShort(data, 0x26)];
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
                    cmd.rgb(0, IoHelper.readUByte(data, 0x10) * flameColour / 0xff, IoHelper.readUByte(data, 0x11) * flameColour / 0xff, IoHelper.readUByte(data, 0x12) * flameColour / 0xff);
                    cmd.rgb(1, IoHelper.readUByte(data, 0x14) * flameColour / 0xff, IoHelper.readUByte(data, 0x15) * flameColour / 0xff, IoHelper.readUByte(data, 0x16) * flameColour / 0xff);
                    cmd.rgb(2, IoHelper.readUByte(data, 0x18) * flameColour / 0xff, IoHelper.readUByte(data, 0x19) * flameColour / 0xff, IoHelper.readUByte(data, 0x1a) * flameColour / 0xff);
                    cmd.rgb(3, IoHelper.readUByte(data, 0x1c) * flameColour / 0xff, IoHelper.readUByte(data, 0x1d) * flameColour / 0xff, IoHelper.readUByte(data, 0x1e) * flameColour / 0xff);

                    GPU.queueCommand(flamesZ, cmd);
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  @Method(0x800cdaa0L)
  public static FireAnimationData20 FUN_800cdaa0(final Rect4i rect, final int widthSomething, final int a2, final int a3) {
    final int[] addr1 = new int[rect.w() / (2 - widthSomething) * rect.h()];
    final int[] addr2 = new int[rect.w() / (2 - widthSomething) * rect.h()];

    return new FireAnimationData20(
      new Rect4i(rect.x(), rect.y(), rect.w() / (4 - widthSomething * 2), rect.h()),
      addr1, addr2,
      a2, widthSomething,
      a3, a2 / 2 * 2, a2 / 2 * 2
    );
  }

  @Method(0x800cdcb0L)
  public static void animateFire(final FireAnimationData20 fireAnimation) {
    if(fireAnimation._18 == 0) {
      return;
    }

    //LAB_800cdce0
    fireAnimation._1c++;
    if(fireAnimation._1c < fireAnimation._1a) {
      return;
    }

    //LAB_800cdd28
    final Rect4i sp10;
    final Rect4i sp18;
    final Rect4i sp20;
    final Rect4i sp28;

    fireAnimation._1c = 0;
    if((fireAnimation._10 & 0x1) == 0) {
      final int v1 = fireAnimation._18;
      final int w = fireAnimation.rect_00.w();
      fireAnimation._18 = v1 % w;

      if(fireAnimation._18 > 0) {
        sp10 = new Rect4i(fireAnimation.rect_00.x() + w - fireAnimation._18, fireAnimation.rect_00.y(), fireAnimation._18, fireAnimation.rect_00.h());
        sp18 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y(), w - fireAnimation._18, fireAnimation.rect_00.h());
        sp20 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y(), fireAnimation._18, fireAnimation.rect_00.h());
        sp28 = new Rect4i(fireAnimation.rect_00.x() + fireAnimation._18, fireAnimation.rect_00.y(), w - fireAnimation._18, fireAnimation.rect_00.h());
      } else {
        //LAB_800cdf14
        sp10 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y(), -fireAnimation._18, fireAnimation.rect_00.h());
        sp18 = new Rect4i(fireAnimation.rect_00.x() - fireAnimation._18, fireAnimation.rect_00.y(), w + fireAnimation._18, fireAnimation.rect_00.h());
        sp20 = new Rect4i(fireAnimation.rect_00.x() + w + fireAnimation._18, fireAnimation.rect_00.y(), -fireAnimation._18, fireAnimation.rect_00.h());
        sp28 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y(), w + fireAnimation._18, fireAnimation.rect_00.h());
      }

      //LAB_800ce090
    } else {
      //LAB_800ce098
      final int v1 = fireAnimation._18;
      final int h = fireAnimation.rect_00.h();
      fireAnimation._18 = v1 % h;
      if(fireAnimation._18 > 0) {
        sp10 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y() + h - fireAnimation._18, fireAnimation.rect_00.w(), fireAnimation._18);
        sp18 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y(), fireAnimation.rect_00.w(), h - fireAnimation._18);
        sp20 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y(), fireAnimation.rect_00.w(), fireAnimation._18);
        sp28 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y() + fireAnimation._18, fireAnimation.rect_00.w(), h - fireAnimation._18);
      } else {
        //LAB_800ce25c
        sp10 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y(), fireAnimation.rect_00.w(), -fireAnimation._18);
        sp18 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y() - fireAnimation._18, fireAnimation.rect_00.w(), fireAnimation.rect_00.h() + fireAnimation._18);
        sp20 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y() + fireAnimation.rect_00.h() + fireAnimation._18, fireAnimation.rect_00.w(), -fireAnimation._18);
        sp28 = new Rect4i(fireAnimation.rect_00.x(), fireAnimation.rect_00.y(), fireAnimation.rect_00.w(), fireAnimation.rect_00.h() + fireAnimation._18);
      }
    }

    //LAB_800ce3d8
    fireTexture.getRegion(0, sp10, fireAnimation.pixels_0c);
    fireTexture.getRegion(0, sp18, fireAnimation.pixels_08);
    fireTexture.setRegion(0, sp20, fireAnimation.pixels_0c);
    fireTexture.setRegion(0, sp28, fireAnimation.pixels_08);
  }
}
