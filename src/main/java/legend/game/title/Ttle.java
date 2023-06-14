package legend.game.title;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.ModelLoader;
import legend.core.gpu.RECT;
import legend.core.gpu.Rect4i;
import legend.core.gpu.Renderable;
import legend.core.gpu.VramTexture;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.opengl.Window;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.fmv.Fmv;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.tim.Tim;
import legend.game.types.CharacterData2c;
import legend.game.types.EngineState;
import legend.game.types.GsRVIEW2;
import legend.game.types.McqHeader;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.SAVES;
import static legend.core.GameEngine.bootMods;
import static legend.core.gpu.VramTextureLoader.palettesFromTim;
import static legend.core.gpu.VramTextureLoader.palettesFromTims;
import static legend.core.gpu.VramTextureLoader.stitch;
import static legend.core.gpu.VramTextureLoader.stitchHorizontal;
import static legend.core.gpu.VramTextureLoader.stitchVertical;
import static legend.core.gpu.VramTextureLoader.textureFromPngOneChannelBlue;
import static legend.core.gpu.VramTextureLoader.textureFromTim;
import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.xpTables;
import static legend.game.Scus94491BpeSegment.decrementOverlayCount;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.renderMcq;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a9c0;
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
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.afterFmvLoadingStage_800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.fmvIndex_800bf0dc;
import static legend.game.Scus94491BpeSegment_800b.gameOverMcq_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.savedGameSelected_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.scriptEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
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

  public static int fadeOutTimer_800c6754;
  public static int flamesZ;

  public static final GsRVIEW2 GsRVIEW2_800c6760 = new GsRVIEW2();

  public static final int[] characterStartingLevels = {1, 3, 4, 8, 13, 15, 17, 19, 23};

  public static int selectedMenuOption;

  private static VramTexture backgroundTexture;
  private static VramTexture[] backgroundPalettes;
  private static Renderable backgroundRenderable;
  private static VramTexture logoTexture;
  private static VramTexture[] logoPalettes;
  private static Renderable logoRenderable;
  private static VramTexture menuTextTexture;
  private static VramTexture[] menuTextPalettes;
  private static final Renderable[] menuTextRenderables = new Renderable[3];
  private static final Renderable[] menuTextBlurRenderables = new Renderable[3];
  private static VramTexture tmTexture;
  private static VramTexture[] tmPalettes;
  private static Renderable tmRenderable;
  private static VramTexture copyrightTexture;
  private static VramTexture[] copyrightPalettes;
  private static Renderable copyrightRenderable;
  private static VramTexture fireTexture;
  private static VramTexture[] firePalettes;
  private static Renderable[] fireRenderable;

  public static final int[][] startingEquipment_800ce6fc = {
    { 0, 76, 46, 93, 152},
    {20, 77, 47, 93, 152},
    {27, 82, 62, 96, 152},
    {13, 82, 63, 96, 152},
    {40, 78, 57, 94, 152},
    {20, 77, 47, 93, 152},
    {34, 84, 64, 97, 152},
    { 8, 78, 53, 94, 152},
    {27, 82, 62, 96, 152},
  };
  public static final int[] startingAddition_800ce758 = {0, 8, -1, 14, 29, 8, 23, 19, -1};
  public static final int[] startingItems_800ce76c = {195, 203, 203};

  public static final int[] _800ce7b0 = {255, 1, 255, 255};
  public static final int[] _800ce7f8 = {195, 131, 128, 80, 0, 64, 16, 56, 32, 64, 48, 96, 64, 80, 80, 32, 96, 32};
  public static final int[] _800ce840 = {76, 211, 140, 128, 128, 96, 0, 144, 80, 0, 176, 72, 0, 208, 72, 128, 0, 112, 128, 32, 88, 128, 64, 48, 173, 64, 48};
  public static final int[] _800ce8ac = {-65, 16, -40, 34, -32, 52, -128, 34, -32, 34, 48, 34, -128, 52, -32, 52, 48, 52};

  private static Window.Events.Cursor onMouseMove;
  private static Window.Events.Click onMouseRelease;
  private static Window.Events.OnPressedWithRepeatPulse onPressedWithRepeatPulse;

  @Method(0x800c7194L)
  public static void setUpNewGameData() {
    gameState_800babc8.charIds_88[0] = 0;
    gameState_800babc8.charIds_88[1] = -1;
    gameState_800babc8.charIds_88[2] = -1;

    //LAB_800c723c
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
      final int level = characterStartingLevels[charIndex];
      charData.xp_00 = xpTables[charIndex][level];
      charData.hp_08 = levelStuff_80111cfc.get(charIndex).deref().get(level).hp_00.get();
      charData.mp_0a = magicStuff_80111d20.get(charIndex).deref().get(1).mp_00.get();
      charData.sp_0c = 0;
      charData.dlevelXp_0e = 0;
      charData.status_10 = 0;
      charData.level_12 = level;
      charData.dlevel_13 = 1;

      //LAB_800c7294
      for(int additionIndex = 0; additionIndex < 8; additionIndex++) {
        charData.additionLevels_1a[additionIndex] = 0;
        charData.additionXp_22[additionIndex] = 0;
      }

      charData.additionLevels_1a[0] = 1;

      //LAB_800c72d4
      for(int i = 1; i < level; i++) {
        final int index = levelStuff_80111cfc.get(charIndex).deref().get(i).addition_02.get();

        if(index != -1) {
          final int offset = additionOffsets_8004f5ac.get(charIndex).get();
          charData.additionLevels_1a[index - offset] = 1;
        }

        //LAB_800c72fc
      }

      //LAB_800c730c
      charData.selectedAddition_19 = startingAddition_800ce758[charIndex];

      //LAB_800c7334
      System.arraycopy(startingEquipment_800ce6fc[charIndex], 0, charData.equipment_14, 0, 5);
    }

    gameState_800babc8.charData_32c[0].partyFlags_04 = 0x3;

    //LAB_800c7398
    //LAB_800c73d8
    for(final int itemId : startingItems_800ce76c) {
      gameState_800babc8.items_2e9.add(itemId);
    }

    //LAB_800c7404
    gameState_800babc8.gold_94 = 20;
    decrementOverlayCount();
  }

  @Method(0x800c7424L)
  public static void executeTtleUnloadingStage() {
    loadSItemAndSetUpNewGameData();
    engineStateOnceLoaded_8004dd24 = EngineState.SUBMAP_05;
    vsyncMode_8007a3b8.set(2);
    pregameLoadingStage_800bb10c.set(0);
  }

  @Method(0x800c7524L)
  public static void loadSItemAndSetUpNewGameData() {
    loadSupportOverlay(2, Ttle::setUpNewGameData);
  }

  @Method(0x800c7558L)
  public static void FUN_800c7558(final FileData data) {
    final McqHeader mcq = new McqHeader(data);

    final RECT rect = new RECT().set((short)640, (short)0, (short)mcq.vramWidth_08, (short)mcq.vramHeight_0a);
    gameOverMcq_800bdc3c = mcq;
    LoadImage(rect, mcq.imageData);
    pregameLoadingStage_800bb10c.set(3);
  }

  @Method(0x800c75b4L)
  public static void renderGameOver() {
    renderMcq(gameOverMcq_800bdc3c, 640, 0, -320, -108, 36, 128);
  }

  @Method(0x800c75fcL)
  public static void gameOver() {
    switch(pregameLoadingStage_800bb10c.get()) {
      case 0 -> {
        bootMods(MODS.getAllModIds());

        FUN_8002a9c0();
        resizeDisplay(640, 240);
        pregameLoadingStage_800bb10c.set(1);
      }

      case 1 -> {
        pregameLoadingStage_800bb10c.set(2);
        loadDrgnFile(0, 6667, Ttle::FUN_800c7558);
      }

      case 3 -> {
        deallocateRenderables(0xffL);
        scriptStartEffect(2, 10);
        pregameLoadingStage_800bb10c.set(4);
      }

      // Game Over Screen
      case 4 -> {
        if(Input.pressedThisFrame(InputAction.BUTTON_CENTER_2) || Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          Scus94491BpeSegment_8002.playSound(2);
          pregameLoadingStage_800bb10c.set(5);
          scriptStartEffect(1, 10);
        }

        renderGameOver();
      }

      case 5 -> {
        if(scriptEffect_800bb140.currentColour_28.get() >= 0xff) {
          pregameLoadingStage_800bb10c.set(6);
        }

        //LAB_800c7740
        renderGameOver();
      }

      case 6 -> {
        deallocateRenderables(0xffL);
        uiFile_800bdc3c = null;
        gameOverMcq_800bdc3c = null;
        engineStateOnceLoaded_8004dd24 = EngineState.TITLE_02;
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
      case 7 -> fadeOutForOptions();
    }
  }

  @Method(0x800c77e4L)
  public static void initializeMainMenu() {
    menuLoadingStage = 0;
    menuIdleTime = 0;
    _800c6728 = 0;
    _800c672c = 0;
    logoFadeInAmount = 0;
    backgroundInitialized = false;
    backgroundScrollAmount = -176;
    copyrightInitialized = false;
    logoFireInitialized = false;
    logoFlashStage = 0;
    fadeOutTimer_800c6754 = 0;
    flamesZ = 100;

    hasSavedGames = 0;
    selectedMenuOption = 0;

    resizeDisplay(384, 240);
    setProjectionPlaneDistance(320);
    GsRVIEW2_800c6760.viewpoint_00.set(0, 0, 2000);
    GsRVIEW2_800c6760.refpoint_0c.set(0, 0, -4000);
    GsRVIEW2_800c6760.viewpointTwist_18 = 0;
    GsRVIEW2_800c6760.super_1c = null;
    GsSetRefView2(GsRVIEW2_800c6760);

    vsyncMode_8007a3b8.set(2);

    loadDrgnDir(0, 5718, Ttle::menuTexturesMrgLoaded);
    loadDrgnFile(0, 5719, file -> menuFireTmdLoaded("DRGN0/5719", file));

    prepareRenderables();

    // Prepare fire animation struct
    //LAB_800c7d30
    for(int i = 0; i < 4; i++) {
      //LAB_800c7d4c
      final Rect4i rect = new Rect4i(944 + i * 16, 256, 256, 64);
      fireAnimation_800c66d4[i] = FUN_800cdaa0(rect, _800ce7b0[i]);
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
      textureFromTim(new Tim(files.get(0))),
      textureFromTim(new Tim(files.get(1)))
    );

    backgroundPalettes = palettesFromTim(new Tim(files.get(0)));

    logoTexture = textureFromTim(new Tim(files.get(2)));
    logoPalettes = palettesFromTim(new Tim(files.get(2)));

    menuTextTexture = textureFromPngOneChannelBlue(Paths.get("gfx", "ui", "5718_3.png"));
    menuTextPalettes = palettesFromTim(new Tim(files.get(3)));

    tmTexture = textureFromTim(new Tim(files.get(4)));
    tmPalettes = palettesFromTim(new Tim(files.get(4)));

    copyrightTexture = stitchHorizontal(
      textureFromTim(new Tim(files.get(5))),
      textureFromTim(new Tim(files.get(6)))
    );

    copyrightPalettes = palettesFromTim(new Tim(files.get(5)));

    fireTexture = stitch(
      textureFromTim(new Tim(files.get(10))),
      textureFromTim(new Tim(files.get(7))),
      textureFromTim(new Tim(files.get(8))),
      textureFromTim(new Tim(files.get(9)))
    );

    firePalettes = palettesFromTims(new Tim(files.get(10)), new Tim(files.get(7)), new Tim(files.get(8)), new Tim(files.get(9)));
  }

  @Method(0x800c7c18L)
  public static void menuFireTmdLoaded(final String modelName, final FileData file) {
    final TmdWithId tmd = new TmdWithId(modelName, file);
    _800c66d0 = parseTmdFile(tmd);
    FUN_800cc0b0(_800c66d0, null);
    setDobjAttributes(_800c66d0, 0);

    fireRenderable = new Renderable[_800c66d0.dobj2s_00.length];
    for(int i = 0; i < _800c66d0.dobj2s_00.length; i++) {
      fireRenderable[i] = ModelLoader
        .fromTmd("Fire " + i, _800c66d0.dobj2s_00[i].tmd_08)
        .texture(fireTexture)
        .palettes(firePalettes)
        .build();
    }
  }

  private static void prepareRenderables() {
    backgroundRenderable = ModelLoader.quad(
      "Background",
      -192, -120, orderingTableSize_1f8003c8.get() - 3,
      384, 424,
      0, 0,
      384, 424,
      0,
      384, 0,
      0, 0, 0,
      null
    )
      .texture(backgroundTexture)
      .palettes(backgroundPalettes)
      .build();

    logoRenderable = ModelLoader.quad(
      "Logo",
      -184, -80, orderingTableSize_1f8003c8.get() - 4,
      352, 88,
      0, 0,
      352, 88,
      1,
      576, 256,
      0, 0, 0,
      Translucency.B_PLUS_F
    )
      .texture(logoTexture)
      .palettes(logoPalettes)
      .build();

    tmRenderable = ModelLoader.quad(
      "TM",
      134, -14, orderingTableSize_1f8003c8.get() - 4,
      16, 8,
      0, 0,
      16, 8,
      80,
      896, 240,
      0, 0, 0,
      Translucency.B_PLUS_F
    )
      .texture(tmTexture)
      .palettes(tmPalettes)
      .build();

    Arrays.setAll(
      menuTextRenderables,
      i -> ModelLoader.quad(
        "Text " + i,
        _800ce8ac[i * 2], _800ce8ac[i * 2 + 1] + 10, 100,
        _800ce7f8[i * 2 + 1], 16,
        i == 0 ? 79 : 0, _800ce7f8[i * 2],
         _800ce7f8[i * 2 + 1], 16,
        0,
        0, 0,
        0x80, 0x80, 0x80,
        Translucency.B_PLUS_F
      )
        .texture(menuTextTexture)
        .palettes(menuTextPalettes)
        .build()
    );

    Arrays.setAll(
      menuTextBlurRenderables,
      i -> ModelLoader.quad(
        "Text blur " + i,
        _800ce8ac[i * 2] - 8, _800ce8ac[i * 2 + 1] + 10 - 8, 100,
        _800ce840[i * 3 + 2], 31,
        _800ce840[i * 3], _800ce840[i * 3 + 1],
        _800ce840[i * 3 + 2], 32,
        0,
        0, 0,
        0x80, 0x80, 0x80,
        Translucency.B_PLUS_F
      )
      .texture(menuTextTexture)
      .palettes(menuTextPalettes)
      .build()
    );

    copyrightRenderable = ModelLoader.quad(
      "Copyright",
      -188, 80, 100,
      368, 32,
      0, 0,
      368, 32,
      64,
      896, 0,
      0, 0, 0,
      Translucency.B_PLUS_F
    )
      .texture(copyrightTexture)
      .palettes(copyrightPalettes)
      .build();
  }

  @Method(0x800c7e50L)
  public static void fadeOutForNewGame() {
    if(fadeOutTimer_800c6754 == 0) {
      scriptStartEffect(1, 15);
    }

    //LAB_800c7fcc
    fadeOutTimer_800c6754++;

    if(fadeOutTimer_800c6754 >= 16) {
      if(_800c6728 == 2) {
        whichMenu_800bdc38 = WhichMenu.INIT_NEW_CAMPAIGN_MENU;
        removeInputHandlers();
        deallocateFire();
        _800c6728 = 3;
      }
    }

    //LAB_800c8038
    loadAndRenderMenus();

    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      if(savedGameSelected_800bdc34.get()) {
        removeInputHandlers();
        deallocateFire();

        fmvIndex_800bf0dc.setu(0x2L);
        afterFmvLoadingStage_800bf0ec = EngineState.TRANSITION_TO_NEW_GAME_03;
        Fmv.playCurrentFmv();

        pregameLoadingStage_800bb10c.set(0);
        return;
      }

      if(_800c6728 == 3) {
        engineStateOnceLoaded_8004dd24 = EngineState.TITLE_02;
        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8.set(2);
      } else {
        renderMenuLogo();
        renderMenuOptions();
        renderMenuLogoFire();
        renderMenuBackground();
        renderCopyright();
      }
    }
  }

  public static void fadeOutForOptions() {
    if(fadeOutTimer_800c6754 == 0) {
      scriptStartEffect(1, 15);
    }

    //LAB_800c7fcc
    fadeOutTimer_800c6754++;

    if(fadeOutTimer_800c6754 >= 16) {
      if(_800c6728 == 2) {
        whichMenu_800bdc38 = WhichMenu.INIT_OPTIONS_MENU;
        removeInputHandlers();
        deallocateFire();
        _800c6728 = 3;
      }
    }

    //LAB_800c8038
    loadAndRenderMenus();

    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      if(_800c6728 == 3) {
        ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));
        engineStateOnceLoaded_8004dd24 = EngineState.TITLE_02;
        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8.set(2);
      } else {
        renderMenuLogo();
        renderMenuOptions();
        renderMenuLogoFire();
        renderMenuBackground();
        renderCopyright();
      }
    }
  }

  @Method(0x800c7fa0L)
  public static void waitForSaveSelection() {
    if(fadeOutTimer_800c6754 == 0) {
      scriptStartEffect(1, 15);
    }

    //LAB_800c7fcc
    fadeOutTimer_800c6754++;

    if(fadeOutTimer_800c6754 >= 16) {
      if(_800c6728 == 2) {
        whichMenu_800bdc38 = WhichMenu.INIT_CAMPAIGN_SELECTION_MENU;
        removeInputHandlers();
        deallocateFire();
        _800c6728 = 3;
      }
    }

    //LAB_800c8038
    loadAndRenderMenus();

    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      if(savedGameSelected_800bdc34.get()) {
        if(gameState_800babc8.isOnWorldMap_4e4) {
          engineStateOnceLoaded_8004dd24 = EngineState.WORLD_MAP_08;
        } else {
          //LAB_800c80a4
          engineStateOnceLoaded_8004dd24 = EngineState.SUBMAP_05;
        }

        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8.set(2);

        //LAB_800c80c4
        return;
      }

      //LAB_800c80cc
      if(_800c6728 == 3) {
        engineStateOnceLoaded_8004dd24 = EngineState.TITLE_02;
        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8.set(2);
      } else {
        //LAB_800c8108
        renderMenuLogo();
        renderMenuOptions();
        renderMenuLogoFire();
        renderMenuBackground();
        renderCopyright();
      }
    }

    //LAB_800c8138
  }

  @Method(0x800c8148L)
  public static void fadeOutMainMenu() {
    if(fadeOutTimer_800c6754 == 0) {
      scriptStartEffect(1, 15);
    }

    //LAB_800c8174
    renderMenuLogo();
    renderMenuOptions();
    renderMenuLogoFire();
    renderMenuBackground();
    renderCopyright();

    fadeOutTimer_800c6754++;
    if(fadeOutTimer_800c6754 > 15) {
      removeInputHandlers();
      deallocateFire();

      fmvIndex_800bf0dc.setu(0);
      afterFmvLoadingStage_800bf0ec = EngineState.TITLE_02;
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
    } else {
      if(menuLoadingStage == 3 || menuLoadingStage == 4) {
        //LAB_800c8388
        if(menuLoadingStage == 3) {
          handleMainInput();
        }

        renderMenuLogo();
        renderMenuOptions();
        renderMenuLogoFire();
        renderCopyright();

        if(menuLoadingStage == 4) {
          menuLoadingStage = 3;
        }
      }
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
    //LAB_800c8474
  }

  private static void resetIdleTime() {
    if(menuLoadingStage != 3) {
      menuLoadingStage = 4;
      menuIdleTime = 0;
    }
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
          for(int i = 0; i < menuTextRenderables.length; i++) {
            if(i == 1 && hasSavedGames != 1) {
              continue;
            }

            final int menuWidth = (int)(155 * scaleX);
            final int menuHeight = (int)(16 * scaleY);
            final int menuX = (window.getWidth() - menuWidth) / 2;
            final int menuY = (int)((_800ce8ac[i * 2 + 1] + 10) * scaleY) + window.getHeight() / 2;

            if(MathHelper.inBox((int)x, (int)y, menuX, menuY, menuWidth, menuHeight)) {
              if(selectedMenuOption != i) {
                playSound(0, 1, 0, 0, (short)0, (short)0);
                selectedMenuOption = i;
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
          for(int i = 0; i < menuTextRenderables.length; i++) {
            if(i == 1 && hasSavedGames != 1) {
              continue;
            }

            final int menuWidth = (int)(155 * scaleX);
            final int menuHeight = (int)(16 * scaleY);
            final int menuX = (window.getWidth() - menuWidth) / 2;
            final int menuY = (int)((_800ce8ac[i * 2 + 1] + 10) * scaleY) + window.getHeight() / 2;

            if(MathHelper.inBox((int)x, (int)y, menuX, menuY, menuWidth, menuHeight)) {
              playSound(0, 2, 0, 0, (short)0, (short)0);
              selectedMenuOption = i;

              _800c672c = 3;
              break;
            }
          }
        }
      }
    });

    onPressedWithRepeatPulse = GPU.window().events.onPressedWithRepeatPulse((window, inputAction) -> resetIdleTime());
  }

  private static void removeInputHandlers() {
    GPU.window().events.removeMouseMove(onMouseMove);
    GPU.window().events.removeMouseRelease(onMouseRelease);
    GPU.window().events.removePressedWithRepeatPulse(onPressedWithRepeatPulse);
    onMouseMove = null;
    onMouseRelease = null;
    onPressedWithRepeatPulse = null;
  }

  @Method(0x800c8484L)
  public static void handleMainInput() {
    if(_800c672c < 3) {
      if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) { // Menu button X
        playSound(0, 2, 0, 0, (short)0, (short)0);

        _800c672c = 3;
      } else if(Input.pressedThisFrame(InputAction.DPAD_UP) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_UP)) { // Menu button up
        playSound(0, 1, 0, 0, (short)0, (short)0);

        selectedMenuOption--;
        if(selectedMenuOption < 0) {
          selectedMenuOption = menuTextRenderables.length - 1;
        }

        if(selectedMenuOption == 1 && hasSavedGames != 1) {
          selectedMenuOption--;
        }

        _800c672c = 2;
      } else if(Input.pressedThisFrame(InputAction.DPAD_DOWN) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) { // Menu button down
        playSound(0, 1, 0, 0, (short)0, (short)0);

        selectedMenuOption++;
        if(selectedMenuOption >= menuTextRenderables.length) {
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
      SAVES.updateUncategorizedSaves();

      hasSavedGames = SAVES.hasCampaigns() ? 1 : 2;
      selectedMenuOption = hasSavedGames == 1 ? 1 : 0;
      return;
    }

    //LAB_800c868c
    switch(_800c672c) {
      case 0 -> {
        //LAB_800c86d8
        for(int i = 0; i < menuTextRenderables.length; i++) {
          //LAB_800c86f4
          menuOptionTransparency[i] = 0;
        }

        //LAB_800c8728
        _800c672c = 1;
      }

      case 1 -> {
        //LAB_800c8740
        //LAB_800c886c
        for(int i = 0; i < menuTextRenderables.length; i++) {
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
        for(int i = 0; i < menuTextRenderables.length; i++) {
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

      case 3 -> { // Clicked on menu option
        _800c672c = 4;
        if(selectedMenuOption == 0) {
          _800c6728 = 2;
          pregameLoadingStage_800bb10c.set(4);
          //LAB_800c8a20
        } else if(selectedMenuOption == 1) {
          _800c6728 = 2;
          pregameLoadingStage_800bb10c.set(5);
        } else if(selectedMenuOption == 2) {
          _800c6728 = 2;
          pregameLoadingStage_800bb10c.set(7);
        }
      }

      //LAB_800c8a4c
      case 4 -> {
        return;
      }
    }

    //LAB_800c8a70
    for(int i = 0; i < menuTextRenderables.length; i++) {
      final int colour;
      if(i != 1 || hasSavedGames == 1) {
        colour = menuOptionTransparency[i];
      } else {
        colour = menuOptionTransparency[i] / 2;
      }

      //LAB_800c8a8c
      menuTextRenderables[i]
        .recolourMono(colour)
        .palette(selectedMenuOption == i ? 5 : 2)
        .render();

      menuTextBlurRenderables[i]
        .recolourMono(colour)
        .palette(4)
        .render();
    }

    //LAB_800c9390
    //LAB_800c939c
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
    copyrightRenderable.recolourMono(copyrightFadeInAmount).render();
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
    logoRenderable.recolourMono(logoFadeInAmount).render();
    tmRenderable.recolourMono(logoFadeInAmount).render();
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
    backgroundRenderable
      .recolourMono(backgroundFadeInAmount)
      .render(0, backgroundScrollAmount);

    //LAB_800cb370
    backgroundScrollAmount++;
    if(backgroundScrollAmount > 0) {
      backgroundScrollAmount = 0;
    }

    //LAB_800cb3b0
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
      zOffset_1f8003e8.set(100);
      fireRenderable[i]
        .colourMultiplier(flameColour)
        .render();
      zOffset_1f8003e8.set(0);
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
      .texture(GPU.getDisplayBuffer());

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

  @Method(0x800cdaa0L)
  public static FireAnimationData20 FUN_800cdaa0(final Rect4i rect, final int a3) {
    final int[] addr1 = new int[rect.w() / 2 * rect.h()];
    final int[] addr2 = new int[rect.w() / 2 * rect.h()];

    return new FireAnimationData20(
      new Rect4i(rect.x(), rect.y(), rect.w() / 4, rect.h()),
      addr1, addr2,
      a3
    );
  }

  @Method(0x800cdcb0L)
  public static void animateFire(final FireAnimationData20 fireAnimation) {
    if(fireAnimation._18 == 0) {
      return;
    }

    //LAB_800cdce0
    //LAB_800cdd28
    final Rect4i sp10;
    final Rect4i sp18;
    final Rect4i sp20;
    final Rect4i sp28;

    //LAB_800ce098
    final int h = fireAnimation.rect_00.h();
    fireAnimation._18 %= h;
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

    //LAB_800ce3d8
    fireTexture.getRegion(sp10, fireAnimation.pixels_0c);
    fireTexture.getRegion(sp18, fireAnimation.pixels_08);
    fireTexture.setRegion(sp20, fireAnimation.pixels_0c);
    fireTexture.setRegion(sp28, fireAnimation.pixels_08);
  }
}
