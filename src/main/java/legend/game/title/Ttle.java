package legend.game.title;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.QueuedModelTmd;
import legend.core.Updater;
import legend.core.Version;
import legend.core.gpu.Bpp;
import legend.core.gpu.Rect4i;
import legend.core.gpu.VramTexture;
import legend.core.gpu.VramTextureSingle;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdWithId;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.SubmapWidescreenMode;
import legend.core.opengl.Texture;
import legend.core.opengl.TmdObjLoader;
import legend.core.platform.Window;
import legend.core.platform.WindowEvents;
import legend.core.platform.input.InputAction;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.fmv.Fmv;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.CampaignSelectionScreen;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.FullScreenInputScreen;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.LinksScreen;
import legend.game.inventory.screens.MenuScreen;
import legend.game.inventory.screens.MessageBoxScreen;
import legend.game.inventory.screens.NewCampaignScreen;
import legend.game.inventory.screens.OptionsCategoryScreen;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.InvalidSaveException;
import legend.game.saves.SaveFailedException;
import legend.game.tim.Tim;
import legend.game.types.GsRVIEW2;
import legend.game.types.MessageBoxResult;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SAVES;
import static legend.core.GameEngine.getUpdate;
import static legend.core.gpu.VramTextureLoader.palettesFromTim;
import static legend.core.gpu.VramTextureLoader.stitchHorizontal;
import static legend.core.gpu.VramTextureLoader.stitchVertical;
import static legend.core.gpu.VramTextureLoader.textureFromTim;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.initMenu;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2L;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;
import static legend.game.modding.coremod.CoreMod.REDUCE_MOTION_FLASHING_CONFIG;

public class Ttle extends EngineState {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Ttle.class);

  private static final int MENU_OPTIONS = 5;

  private TmdRenderingStruct _800c66d0;
  private final FireAnimationData20[] fireAnimation_800c66d4 = new FireAnimationData20[4];
  private int hasSavedGames;
  private int menuLoadingStage;
  private float logoFadeInAmount;
  private int logoFlashStage;
  private int logoFlashColour;
  private boolean backgroundInitialized;
  private int backgroundScrollAmount;
  private float backgroundFadeInAmount;
  private boolean copyrightInitialized;
  private float copyrightFadeInAmount;
  private boolean logoFireInitialized;
  private int flameColour;
  private int menuIdleTime;

  private int menuTransitionState_800c6728;
  private int menuState_800c672c;
  private final int[] menuOptionTransparency = new int[MENU_OPTIONS];
  private int menuUpdateTransparency;

  private int fadeOutTimer_800c6754;

  private final GsRVIEW2 GsRVIEW2_800c6760 = new GsRVIEW2();

  private int loadingStage;
  private int selectedMenuOption;
  private boolean memcardConversionShown;
  private boolean saveCategorizationShown;

  private Texture backgroundTex;
  private Texture logoTex;
  private Texture trademarkTex;
  private Texture menuTextTex;
  private Texture copyrightTex;
  private Obj backgroundObj;
  private Obj logoObj;
  private Obj trademarkObj;
  private Obj menuTextObj;
  private int updateAvailableIndex;
  private int updateAvailableShadowIndex;
  private int updateIconIndex;
  private Obj copyrightObj;
  private final Matrix4f flashTransforms = new Matrix4f();

  private VramTexture backgroundTexture;
  private VramTexture[] backgroundPalettes;
  private VramTexture tmTexture;
  private VramTexture[] tmPalettes;
  private VramTexture copyrightTexture;
  private VramTexture[] copyrightPalettes;
  private boolean texturesLoaded;
  private boolean fireLoaded;
  private boolean renderablesLoaded;

  private final int[] _800ce7b0 = {255, 1, 255, 255};
  private final int[] menuTextWidth = {407, 257, 227, 169, 141};

  public static final FontOptions VERSION_FONT = new FontOptions().size(0.5f).colour(TextColour.LIGHT_BROWN).noShadow().horizontalAlign(HorizontalAlign.RIGHT);

  private Updater.Release update;

  private static WindowEvents.Cursor onMouseMove;
  private static WindowEvents.Click onMouseRelease;
  private static WindowEvents.KeyPressed onKeyPressed;
  private static WindowEvents.ButtonPressed onButtonPressed;
  private static WindowEvents.InputActionPressed onInputActionPressed;

  @Override
  public RenderMode getRenderMode() {
    return RenderMode.LEGACY;
  }

  @Override
  @Method(0x800c7798L)
  public void tick() {
    super.tick();

    switch(this.loadingStage) {
      case 0 -> this.initializeMainMenu();
      case 1 -> this.loadTextures();
      case 3 -> this.renderMainMenu();
      case 4 -> this.fadeOutForNewGame();
      case 5 -> this.waitForSaveSelection();
      case 6 -> this.fadeOutMainMenu();
      case 7 -> this.fadeOutForOptions();
      case 8 -> this.fadeOutForQuit();
      case 9 -> this.fadeOutForCategorizeSave();
      case 10 -> this.fadeOutForMemcard();
      case 11 -> this.fadeOutForLinks();
    }
  }

  @Method(0x800c77e4L)
  private void initializeMainMenu() {
//    SCREENS.pushScreen(new TitleScreen());
//    GameEngine.legacyUi = false;

    this.menuLoadingStage = 0;
    this.menuIdleTime = 0;
    this.menuTransitionState_800c6728 = 0;
    this.menuState_800c672c = 0;
    this.logoFadeInAmount = 0.0f;
    this.backgroundInitialized = false;
    this.backgroundScrollAmount = -176;
    this.copyrightInitialized = false;
    this.logoFireInitialized = false;
    this.logoFlashStage = 0;
    this.fadeOutTimer_800c6754 = 0;

    this.texturesLoaded = false;
    this.fireLoaded = false;
    this.renderablesLoaded = false;

    this.hasSavedGames = 0;
    this.selectedMenuOption = 0;

    resizeDisplay(384, 240);
    setProjectionPlaneDistance(320);
    this.GsRVIEW2_800c6760.viewpoint_00.set(0.0f, 0.0f, 2000.0f);
    this.GsRVIEW2_800c6760.refpoint_0c.set(0.0f, 0.0f, -4000.0f);
    this.GsRVIEW2_800c6760.viewpointTwist_18 = 0;
    this.GsRVIEW2_800c6760.super_1c = null;
    GsSetRefView2L(this.GsRVIEW2_800c6760);

    vsyncMode_8007a3b8 = 2;

    loadDrgnDir(0, 5718, this::menuTexturesMrgLoaded);
    loadDrgnFile(0, 5719, file -> this.menuFireTmdLoaded("DRGN0/5719", file));

    // Prepare fire animation struct
    //LAB_800c7d30
    for(int i = 0; i < 4; i++) {
      //LAB_800c7d4c
      final Rect4i rect = new Rect4i(944 + i * 16, 256, 64, 64);
      this.fireAnimation_800c66d4[i] = this.FUN_800cdaa0(rect, this._800ce7b0[i]);
    }

    startFadeEffect(2, 15);
    GTE.setScreenOffset(0, 0);
    this.loadingStage = 1;

    this.addInputHandlers();

    this.update = getUpdate();
  }

  private void loadTextures() {
    if(!this.texturesLoaded) {
      return;
    }

    this.backgroundTex = ((VramTextureSingle)this.backgroundTexture).createOpenglTexture((VramTextureSingle)this.backgroundPalettes[0]);
    this.backgroundObj = new QuadBuilder("Title Screen Background")
      .pos(0.0f, 0.0f, 60000.0f)
      .posSize(368.0f, 424.0f)
      .uvSize(1.0f, 1.0f)
      .bpp(Bpp.BITS_24)
      .build();

    this.logoTex = Texture.filteredPng(Path.of("gfx/ui/logo.png"));
    this.logoObj = new QuadBuilder("Title Screen Logo")
      .posSize(352.0f, 198.0f)
      .uvSize(1.0f, 1.0f)
      .bpp(Bpp.BITS_24)
      .build();

    this.trademarkTex = ((VramTextureSingle)this.tmTexture).createOpenglTexture((VramTextureSingle)this.tmPalettes[0]);
    this.trademarkObj = new QuadBuilder("Title Screen Trademark")
      .pos(326.0f, 86.0f, 0.0f)
      .posSize(16.0f, 8.0f)
      .uvSize(1.0f, 1.0f)
      .bpp(Bpp.BITS_24)
      .translucency(Translucency.B_PLUS_F)
      .build();

    this.menuTextTex = Texture.filteredPng(Path.of("gfx/ui/title_menu.png"));
    final QuadBuilder menuTextBuilder = new QuadBuilder("Title Screen Menu Text");
    for(int i = 0; i < MENU_OPTIONS; i++) {
      menuTextBuilder
        .add()
        .bpp(Bpp.BITS_24)
        .posSize(512.0f, 64.0f)
        .uvSize(512.0f / this.menuTextTex.width, 64.0f / this.menuTextTex.height)
        .uv(0, i * 64.0f / this.menuTextTex.height)
      ;
    }

    // Blurred backgrounds
    for(int i = 0; i < MENU_OPTIONS; i++) {
      menuTextBuilder
        .add()
        .bpp(Bpp.BITS_24)
        .posSize(512.0f, 64.0f)
        .uvSize(512.0f / this.menuTextTex.width, 64.0f / this.menuTextTex.height)
        .uv(512.0f / this.menuTextTex.width, i * 64.0f / this.menuTextTex.height)
      ;
    }

    this.updateAvailableIndex = menuTextBuilder.currentQuadIndex();
    menuTextBuilder
      .add()
      .bpp(Bpp.BITS_24)
      .posSize(512.0f, 64.0f)
      .uvSize(512.0f / this.menuTextTex.width, 64.0f / this.menuTextTex.height)
      .uv(0, 6 * 64.0f / this.menuTextTex.height)
    ;

    this.updateAvailableShadowIndex = menuTextBuilder.currentQuadIndex();
    menuTextBuilder
      .add()
      .bpp(Bpp.BITS_24)
      .posSize(512.0f, 64.0f)
      .uvSize(512.0f / this.menuTextTex.width, 64.0f / this.menuTextTex.height)
      .uv(512.0f / this.menuTextTex.width, 6 * 64.0f / this.menuTextTex.height)
    ;

    this.updateIconIndex = menuTextBuilder.currentQuadIndex();
    menuTextBuilder
      .add()
      .bpp(Bpp.BITS_24)
      .posSize(64.0f, 64.0f)
      .uvSize(64.0f / this.menuTextTex.width, 64.0f / this.menuTextTex.height)
      .uv(0, 7 * 64.0f / this.menuTextTex.height)
    ;

    this.menuTextObj = menuTextBuilder.build();

    this.copyrightTex = ((VramTextureSingle)this.copyrightTexture).createOpenglTexture((VramTextureSingle)this.copyrightPalettes[0]);
    this.copyrightObj = new QuadBuilder("Title Screen Copyright")
      .pos(92.0f, 220.0f, 0.0f)
      .size(184.0f, 16.0f)
      .uvSize(1.0f, 1.0f)
      .bpp(Bpp.BITS_24)
      .translucency(Translucency.B_PLUS_F)
      .build();

    this.loadingStage = 3;
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
  private void menuTexturesMrgLoaded(final List<FileData> files) {
    this.backgroundTexture = stitchVertical(
      textureFromTim(new Tim(files.get(0))),
      textureFromTim(new Tim(files.get(1)))
    );

    this.backgroundPalettes = palettesFromTim(new Tim(files.get(0)));

    this.tmTexture = textureFromTim(new Tim(files.get(4)));
    this.tmPalettes = palettesFromTim(new Tim(files.get(4)));

    this.copyrightTexture = stitchHorizontal(
      textureFromTim(new Tim(files.get(5))),
      textureFromTim(new Tim(files.get(6)))
    );

    this.copyrightPalettes = palettesFromTim(new Tim(files.get(5)));

    for(int i = 7; i < 11; i++) {
      new Tim(files.get(i)).uploadToGpu();
    }

    this.texturesLoaded = true;
  }

  @Method(0x800c7c18L)
  private void menuFireTmdLoaded(final String modelName, final FileData file) {
    final TmdWithId tmd = new TmdWithId(modelName, file);
    this._800c66d0 = this.parseTmdFile(tmd);
    this.FUN_800cc0b0(this._800c66d0, null);
    this.setDobjAttributes(this._800c66d0, 0);
    this.fireLoaded = true;
  }

  private void prepareRenderables() {
    for(int i = 0; i < this._800c66d0.dobj2s_00.length; i++) {
      this._800c66d0.dobj2s_00[i].obj = TmdObjLoader.fromObjTable("Title Screen Fire " + i, this._800c66d0.dobj2s_00[i].tmd_08);
    }

    this.renderablesLoaded = true;
  }

  @Method(0x800c7e50L)
  private void fadeOutForNewGame() {
    this.fadeOutToMenu(NewCampaignScreen::new, () -> {
      if(loadingNewGameState_800bdc34) {
        removeInputHandlers();
        this.deallocate();

        Fmv.playCurrentFmv(2, EngineStateEnum.TRANSITION_TO_NEW_GAME_03);
        return true;
      }

      return false;
    });
  }

  private void fadeOutForOptions() {
    this.fadeOutToMenu(() -> new OptionsCategoryScreen(CONFIG, Set.of(ConfigStorageLocation.GLOBAL), () -> whichMenu_800bdc38 = WhichMenu.UNLOAD), () -> {
      ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));
      return false;
    });
  }

  private void fadeOutForLinks() {
    this.fadeOutToMenu(() -> new LinksScreen(() -> whichMenu_800bdc38 = WhichMenu.UNLOAD), () -> false);
  }

  private void fadeOutForCategorizeSave() {
    this.fadeOutToMenu(() -> new FullScreenInputScreen("Uncategorized saves found. Please enter a name for your campaign.", "Campaign name:", SAVES.generateCampaignName(), (result, name) -> {
      this.saveCategorizationShown = true;
      if(result == MessageBoxResult.YES) {
        if(SAVES.campaignExists(name)) {
          menuStack.pushScreen(new MessageBoxScreen("Campaign name already\nin use", 0, result1 -> {
            whichMenu_800bdc38 = WhichMenu.UNLOAD;
          }));
          return;
        }

        try {
          SAVES.moveCategorizedSaves(name);
        } catch(final IOException e) {
          LOGGER.error("Failed to categorize saves", e);
        }
      }
      whichMenu_800bdc38 = WhichMenu.UNLOAD;
    }), () -> false);
  }

  private void fadeOutForMemcard() {
    this.fadeOutToMenu(() -> new FullScreenInputScreen("PS1 memory card found. Please enter a name for your campaign.", "Campaign name:", SAVES.generateCampaignName(), (result, name) -> {
      this.memcardConversionShown = true;
      if(result == MessageBoxResult.YES) {
        if(SAVES.campaignExists(name)) {
          menuStack.pushScreen(new MessageBoxScreen("Campaign name already\nin use", 0, result1 -> {
            whichMenu_800bdc38 = WhichMenu.UNLOAD;
          }));
          return;
        }

        menuStack.pushScreen(new MessageBoxScreen("Delete the memory card file?", 2, result1 -> {
          try {
            SAVES.splitMemcards(name, result1 == MessageBoxResult.YES);
          } catch(final IOException | InvalidSaveException | SaveFailedException e) {
            LOGGER.error("Failed to convert memcard", e);
          }
          whichMenu_800bdc38 = WhichMenu.UNLOAD;
        }));
      } else {
        whichMenu_800bdc38 = WhichMenu.UNLOAD;
      }
    }), () -> false);
  }

  private void fadeOutToMenu(final Supplier<MenuScreen> destScreen, final BooleanSupplier transition) {
    if(this.fadeOutTimer_800c6754 == 0) {
      startFadeEffect(1, 15);
    }

    //LAB_800c7fcc
    this.fadeOutTimer_800c6754++;

    if(this.fadeOutTimer_800c6754 >= 16 && this.menuTransitionState_800c6728 == 2) {
      initMenu(WhichMenu.RENDER_NEW_MENU, destScreen);
      removeInputHandlers();
      this.deallocate();
      this.menuTransitionState_800c6728 = 3;
    }

    //LAB_800c8038
    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      if(this.menuTransitionState_800c6728 == 3) {
        if(!transition.getAsBoolean()) {
          engineStateOnceLoaded_8004dd24 = EngineStateEnum.TITLE_02;
          this.loadingStage = 0;
          vsyncMode_8007a3b8 = 2;
        }
      } else {
        this.renderMenuBackground();
        this.renderMenuOptions();
        this.renderMenuLogo();
        this.renderMenuLogoFire();
        this.renderCopyright();
      }
    }
  }

  private void fadeOutForQuit() {
    if(this.fadeOutTimer_800c6754 == 0) {
      startFadeEffect(1, 15);
    }

    //LAB_800c7fcc
    this.fadeOutTimer_800c6754++;

    if(this.fadeOutTimer_800c6754 < 16) {
      this.renderMenuBackground();
      this.renderMenuOptions();
      this.renderMenuLogo();
      this.renderMenuLogoFire();
      this.renderCopyright();
    } else {
      removeInputHandlers();
      this.deallocate();
      RENDERER.window().close();
    }
  }

  @Method(0x800c7fa0L)
  private void waitForSaveSelection() {
    this.fadeOutToMenu(CampaignSelectionScreen::new, () -> {
      if(loadingNewGameState_800bdc34) {
        if(gameState_800babc8.isOnWorldMap_4e4) {
          engineStateOnceLoaded_8004dd24 = EngineStateEnum.WORLD_MAP_08;
        } else {
          //LAB_800c80a4
          engineStateOnceLoaded_8004dd24 = EngineStateEnum.SUBMAP_05;
        }

        vsyncMode_8007a3b8 = 2;

        //LAB_800c80c4
        return true;
      }

      return false;
    });
  }

  @Method(0x800c8148L)
  private void fadeOutMainMenu() {
    if(this.fadeOutTimer_800c6754 == 0) {
      startFadeEffect(1, 15);
    }

    //LAB_800c8174
    this.renderMenuBackground();
    this.renderMenuOptions();
    this.renderMenuLogo();
    this.renderMenuLogoFire();
    this.renderCopyright();

    this.fadeOutTimer_800c6754++;
    if(this.fadeOutTimer_800c6754 > 15) {
      removeInputHandlers();
      this.deallocate();

      Fmv.playCurrentFmv(0, EngineStateEnum.TITLE_02);

      this.loadingStage = 0;
    }

    //LAB_800c8218
  }

  @Method(0x800c8298L)
  private void renderMainMenu() {
    if(!this.renderablesLoaded) {
      if(!this.texturesLoaded || !this.fireLoaded) {
        return;
      }

      this.prepareRenderables();
    }

    //LAB_800c83c8
    this.renderMenuBackground();

    if(this.menuLoadingStage == 0) {
      //LAB_800c82f0
      if(this.backgroundScrollAmount > -40) {
        this.menuLoadingStage = 1;
      }

      //LAB_800c82d0
      //LAB_800c8314
    } else if(this.menuLoadingStage == 1) {
      //LAB_800c831c
      this.renderMenuLogo();

      if(this.logoFadeInAmount >= 1.0f) {
        this.menuLoadingStage = 2;
      }

      //LAB_800c8348
    } else if(this.menuLoadingStage == 2) {
      //LAB_800c8350
      this.renderMenuLogo();
      this.renderLogoFlash();

      if(this.logoFlashStage == 2) {
        this.menuLoadingStage = 3;
      }

      //LAB_800c8380
    } else {
      if(this.menuLoadingStage == 3 || this.menuLoadingStage == 4) {
        //LAB_800c8388
        this.renderMenuOptions();
        this.renderMenuLogo();
        this.renderMenuLogoFire();
        this.renderCopyright();

        if(this.menuLoadingStage == 4) {
          this.menuLoadingStage = 3;
        }
      }
    }

    if(this.menuTransitionState_800c6728 != 1) {
//      this.menuIdleTime += 2;

      if(this.menuIdleTime > 1680) {
        this.loadingStage = 6;
      }
    }

    //LAB_800c8448
    //LAB_800c8474
  }

  private void resetIdleTime() {
    if(this.menuLoadingStage != 3) {
      this.menuLoadingStage = 4;
      this.menuIdleTime = 0;
    }
  }

  private void addInputHandlers() {
    onMouseMove = RENDERER.events().onMouseMove((window, x, y) -> {
      if(CONFIG.getConfig(CoreMod.DISABLE_MOUSE_INPUT_CONFIG.get()) && PLATFORM.hasGamepad()) {
        return;
      }

      final float aspect = 4.0f / 3.0f;

      float w = window.getWidth();
      float h = w / aspect;

      if(h > window.getHeight()) {
        h = window.getHeight();
        w = h * aspect;
      }

      final float left;
      final float top;
      final float scaleX;
      final float scaleY;

      if(CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.EXPANDED) {
        scaleX = w / RENDERER.getNativeWidth();
        scaleY = h / RENDERER.getNativeHeight();
        left = (window.getWidth() - w) / 2;
        top = (window.getHeight() - h) / 2;
      } else {
        scaleX = 1.0f;
        scaleY = 1.0f;
        left = 0.0f;
        top = 0.0f;
        x = x / window.getWidth() * RENDERER.getNativeWidth();
        y = y / window.getHeight() * RENDERER.getNativeHeight();
      }

      if(this.menuLoadingStage == 3) {
        if(this.menuState_800c672c < 3) {
          for(int i = 0; i < MENU_OPTIONS; i++) {
            if(i == 1 && this.hasSavedGames != 1) {
              continue;
            }

            final int menuWidth = (int)(155 * scaleX);
            final int menuHeight = (int)(16 * scaleY);
            final int menuX = (int)(left + (RENDERER.getNativeWidth() * scaleX - menuWidth) / 2.0f);
            final int menuY = (int)(top + (134.0f + i * 16.0f) * scaleY);

            if(MathHelper.inBox((int)x, (int)y, menuX, menuY, menuWidth, menuHeight)) {
              if(this.selectedMenuOption != i) {
                playSound(0, 1, (short)0, (short)0);
                this.selectedMenuOption = i;
              }

              break;
            }
          }

          if(this.update != null) {
            if(MathHelper.inBox((int)(x / scaleX), (int)(y / scaleY), (int)(left / scaleX + 6), (int)(top / scaleY + 5), 105, 14)) {
              RENDERER.window().usePointerCursor();
            } else {
              RENDERER.window().useNormalCursor();
            }
          }
        }
      }

      this.menuIdleTime = 0;
    });

    onMouseRelease = RENDERER.events().onMouseRelease((window, x, y, button, mods) -> {
      if(CONFIG.getConfig(CoreMod.DISABLE_MOUSE_INPUT_CONFIG.get()) && PLATFORM.hasGamepad()) {
        return;
      }

      if(button != PLATFORM.getMouseButton(0)) {
        return;
      }

      if(this.menuLoadingStage < 3) {
        this.menuLoadingStage = 3;
      } else {
        final float aspect = 4.0f / 3.0f;

        float w = window.getWidth();
        float h = w / aspect;

        if(h > window.getHeight()) {
          h = window.getHeight();
          w = h * aspect;
        }

        final float left;
        final float top;
        final float scaleX;
        final float scaleY;

        if(CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.EXPANDED) {
          scaleX = w / RENDERER.getNativeWidth();
          scaleY = h / RENDERER.getNativeHeight();
          left = (window.getWidth() - w) / 2;
          top = (window.getHeight() - h) / 2;
        } else {
          scaleX = 1.0f;
          scaleY = 1.0f;
          left = 0.0f;
          top = 0.0f;
          x = x / window.getWidth() * RENDERER.getNativeWidth();
          y = y / window.getHeight() * RENDERER.getNativeHeight();
        }

        if(this.menuState_800c672c < 3) {
          for(int i = 0; i < MENU_OPTIONS; i++) {
            if(i == 1 && this.hasSavedGames != 1) {
              continue;
            }

            final int menuWidth = (int)(155 * scaleX);
            final int menuHeight = (int)(16 * scaleY);
            final int menuX = (int)(left + (RENDERER.getNativeWidth() * scaleX - menuWidth) / 2.0f);
            final int menuY = (int)(top + (134.0f + i * 16.0f) * scaleY);

            if(MathHelper.inBox((int)x, (int)y, menuX, menuY, menuWidth, menuHeight)) {
              playSound(0, 2, (short)0, (short)0);
              this.selectedMenuOption = i;

              this.menuState_800c672c = 3;
              break;
            }
          }

          if(this.update != null) {
            if(MathHelper.inBox((int)(x / scaleX), (int)(y / scaleY), (int)(left / scaleX + 6), (int)(top / scaleY + 5), 105, 14)) {
              PLATFORM.openUrl(this.update.uri);
            }
          }
        }
      }
    });

    onKeyPressed = RENDERER.events().onKeyPress((window, key, scancode, mods, repeat) -> this.resetIdleTime());
    onButtonPressed = RENDERER.events().onButtonPress((window, button, repeat) -> this.resetIdleTime());

    onInputActionPressed = RENDERER.events().onInputActionPressed(this::handleMainInput);
  }

  private static void removeInputHandlers() {
    RENDERER.events().removeMouseMove(onMouseMove);
    RENDERER.events().removeMouseRelease(onMouseRelease);
    RENDERER.events().removeKeyPress(onKeyPressed);
    RENDERER.events().removeButtonPress(onButtonPressed);
    RENDERER.events().removeInputActionPressed(onInputActionPressed);
    onMouseMove = null;
    onMouseRelease = null;
    onKeyPressed = null;
    onButtonPressed = null;
  }

  @Method(0x800c8484L)
  private void handleMainInput(final Window window, final InputAction action, final boolean repeat) {
    if(this.menuLoadingStage == 3) {
      if(this.menuState_800c672c < 3) {
        if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
          playSound(0, 2, (short)0, (short)0);

          this.menuState_800c672c = 3;
        } else if(action == INPUT_ACTION_MENU_UP.get()) {
          playSound(0, 1, (short)0, (short)0);

          this.selectedMenuOption--;
          if(this.selectedMenuOption < 0) {
            this.selectedMenuOption = MENU_OPTIONS - 1;
          }

          if(this.selectedMenuOption == 1 && this.hasSavedGames != 1) {
            this.selectedMenuOption--;
          }

          this.menuState_800c672c = 2;
        } else if(action == INPUT_ACTION_MENU_DOWN.get()) {
          playSound(0, 1, (short)0, (short)0);

          this.selectedMenuOption++;
          if(this.selectedMenuOption >= MENU_OPTIONS) {
            this.selectedMenuOption = 0;
          }

          if(this.selectedMenuOption == 1 && this.hasSavedGames != 1) {
            this.selectedMenuOption++;
          }

          this.menuState_800c672c = 2;
        }
      }
    }
  }

  @Method(0x800c8634L)
  private void renderMenuOptions() {
    if(this.hasSavedGames == 0) {
      if(!this.saveCategorizationShown && !SAVES.findUncategorizedSaves().isEmpty()) {
        this.menuState_800c672c = 4;
        this.menuTransitionState_800c6728 = 2;
        this.loadingStage = 9;
      }

      if(!this.memcardConversionShown && !SAVES.findMemcards().isEmpty()) {
        this.menuState_800c672c = 4;
        this.menuTransitionState_800c6728 = 2;
        this.loadingStage = 10;
      }

      this.hasSavedGames = SAVES.hasCampaigns() ? 1 : 2;
      this.selectedMenuOption = this.hasSavedGames == 1 ? 1 : 0;
      return;
    }

    //LAB_800c868c
    switch(this.menuState_800c672c) {
      case 0 -> {
        //LAB_800c86d8
        //LAB_800c86f4
        Arrays.fill(this.menuOptionTransparency, 0);
        this.menuUpdateTransparency = 0;

        //LAB_800c8728
        this.menuState_800c672c = 1;
      }

      case 1 -> {
        //LAB_800c8740
        //LAB_800c886c
        for(int i = 0; i < MENU_OPTIONS; i++) {
          //LAB_800c875c
          this.menuOptionTransparency[i] += 4;
          if(this.selectedMenuOption == i) {
            if(this.menuOptionTransparency[i] > 176) {
              this.menuOptionTransparency[i] = 176;
            }

            //LAB_800c8800
            //LAB_800c8808
          } else if(this.menuOptionTransparency[i] > 64) {
            this.menuOptionTransparency[i] = 64;
          }

          //LAB_800c8854
        }

        this.menuUpdateTransparency += 4;
        if(this.menuUpdateTransparency > 160) {
          this.menuUpdateTransparency = 160;
        }
      }

      case 2 -> {
        //LAB_800c8878
        //LAB_800c89e4
        for(int i = 0; i < MENU_OPTIONS; i++) {
          //LAB_800c8894
          if(this.selectedMenuOption == i) {
            // Fade in selected item

            this.menuOptionTransparency[i] += 8;
            if(this.menuOptionTransparency[i] > 160) {
              this.menuOptionTransparency[i] = 160;
            }

            //LAB_800c8938
          } else {
            // Fade out unselected items

            //LAB_800c8940
            this.menuOptionTransparency[i] -= 16;
            if(this.menuOptionTransparency[i] < 64) {
              this.menuOptionTransparency[i] = 64;
            }
          }

          //LAB_800c89cc
        }
      }

      case 3 -> { // Clicked on menu option
        this.menuState_800c672c = 4;
        if(this.selectedMenuOption == 0) {
          this.menuTransitionState_800c6728 = 2;
          this.loadingStage = 4;
          //LAB_800c8a20
        } else if(this.selectedMenuOption == 1) {
          this.menuTransitionState_800c6728 = 2;
          this.loadingStage = 5;
        } else if(this.selectedMenuOption == 2) {
          this.menuTransitionState_800c6728 = 2;
          this.loadingStage = 7;
        } else if(this.selectedMenuOption == 3) {
          this.menuTransitionState_800c6728 = 2;
          this.loadingStage = 11;
        } else if(this.selectedMenuOption == 4) {
          this.menuTransitionState_800c6728 = 2;
          this.loadingStage = 8;
        }
      }

      //LAB_800c8a4c
      case 4 -> {
        return;
      }
    }

    final Matrix4f transforms = new Matrix4f();
    final float scale = 1.0f / 3.0f;

    final Vector3f normalColour = new Vector3f(0xf0 / 255.0f, 0xf0 / 255.0f, 0xf0 / 255.0f);
    final Vector3f highlightColour = new Vector3f(0xc0 / 255.0f, 0x38 / 255.0f, 0x10 / 255.0f);
    final Vector3f tempColour = new Vector3f();

    //LAB_800c8a70
    for(int i = 0; i < MENU_OPTIONS; i++) {
      final int alpha;
      if(i != 1 || this.hasSavedGames == 1) {
        alpha = this.menuOptionTransparency[i];
      } else {
        alpha = this.menuOptionTransparency[i] / 2;
      }

      transforms
        .translation(184.0f - this.menuTextWidth[i] * scale / 2.0f + RENDERER.getWidescreenOrthoOffsetX(), 130.0f + i * 16.0f, 100.0f)
        .scale(scale, scale, 1.0f)
      ;

      //LAB_800c8a8c
      RENDERER
        .queueOrthoModel(this.menuTextObj, transforms, QueuedModelStandard.class)
        .translucency(Translucency.B_PLUS_F)
        .colour(0xf8 / 255.0f, 0x80 / 255.0f, 0x10 / 255.0f)
        .alpha(alpha / 128.0f)
        .texture(this.menuTextTex)
        .useTextureAlpha()
        .vertices((i + MENU_OPTIONS) * 4, 4);

      transforms
        .translation(184.0f - this.menuTextWidth[i] * scale / 2.0f + RENDERER.getWidescreenOrthoOffsetX(), 130.0f + i * 16.0f, 100.1f)
        .scale(scale, scale, 1.0f)
      ;

      RENDERER
        .queueOrthoModel(this.menuTextObj, transforms, QueuedModelStandard.class)
        .translucency(Translucency.B_PLUS_F)
        .colour(normalColour.lerp(highlightColour, (alpha - 0x40) / (float)0x60, tempColour))
        .alpha(alpha / 128.0f)
        .texture(this.menuTextTex)
        .useTextureAlpha()
        .vertices(i * 4, 4);
    }

    if(this.update != null) {
      transforms
        .translation(20.0f + RENDERER.getWidescreenOrthoOffsetX(), 5.0f, 100.0f)
        .scale(0.2f, 0.2f, 1.0f)
      ;

      RENDERER
        .queueOrthoModel(this.menuTextObj, transforms, QueuedModelStandard.class)
        .translucency(Translucency.B_PLUS_F)
        .colour(0xf8 / 255.0f, 0x80 / 255.0f, 0x10 / 255.0f)
        .alpha(this.menuUpdateTransparency / 128.0f)
        .texture(this.menuTextTex)
        .useTextureAlpha()
        .vertices(this.updateAvailableIndex * 4, 4);

      transforms
        .translation(20.0f + RENDERER.getWidescreenOrthoOffsetX(), 5.0f, 100.1f)
        .scale(0.2f, 0.2f, 1.0f)
      ;

      RENDERER
        .queueOrthoModel(this.menuTextObj, transforms, QueuedModelStandard.class)
        .translucency(Translucency.B_PLUS_F)
        .colour(normalColour)
        .alpha(this.menuUpdateTransparency / 128.0f)
        .texture(this.menuTextTex)
        .useTextureAlpha()
        .vertices(this.updateAvailableShadowIndex * 4, 4);

      transforms
        .translation(6.0f + RENDERER.getWidescreenOrthoOffsetX(), 5.0f, 100.1f)
        .scale(0.2f, 0.2f, 1.0f)
      ;

      RENDERER
        .queueOrthoModel(this.menuTextObj, transforms, QueuedModelStandard.class)
        .translucency(Translucency.B_PLUS_F)
        .colour(normalColour)
        .alpha(this.menuUpdateTransparency / 128.0f)
        .texture(this.menuTextTex)
        .useTextureAlpha()
        .vertices(this.updateIconIndex * 4, 4);
    }

    renderText(Version.FULL_VERSION, 364, 4, VERSION_FONT, model -> model.alpha(this.menuUpdateTransparency / 128.0f).translucency(Translucency.HALF_B_PLUS_HALF_F));

    //LAB_800c9390
    //LAB_800c939c
  }

  @Method(0x800cab48L)
  private void renderCopyright() {
    if(!this.copyrightInitialized) {
      this.copyrightFadeInAmount = 0.0f;
      this.copyrightInitialized = true;
    }

    //LAB_800cab7c
    this.copyrightFadeInAmount += 8.0f / 255.0f;
    if(this.copyrightFadeInAmount > 1.0f) {
      this.copyrightFadeInAmount = 1.0f;
    }

    final Matrix4f transforms = new Matrix4f().translation(RENDERER.getWidescreenOrthoOffsetX(), 0.0f, 200.0f);

    //LAB_800cabb8
    //LAB_800cabcc
    //LAB_800cabe8
    RENDERER
      .queueOrthoModel(this.copyrightObj, transforms, QueuedModelStandard.class)
      .monochrome(this.copyrightFadeInAmount)
      .texture(this.copyrightTex);
  }

  @Method(0x800cadd0L)
  private void renderMenuLogo() {
    this.logoFadeInAmount += 8.0f / 255.0f;
    if(this.logoFadeInAmount > 1.0f) {
      this.logoFadeInAmount = 1.0f;
    }

    //LAB_800cae18
    //LAB_800cae2c
    //LAB_800cae48
    final Matrix4f transforms = new Matrix4f()
      .scaling(1.06f, 0.82f, 1.0f)
      .translate(-4.0f + RENDERER.getWidescreenOrthoOffsetX(), -4.0f, 10000.0f)
    ;

    RENDERER
      .queueOrthoModel(this.logoObj, transforms, QueuedModelStandard.class)
      .translucency(Translucency.B_PLUS_F)
      .alpha(this.logoFadeInAmount)
      .useTextureAlpha()
      .texture(this.logoTex);

    transforms.translation(RENDERER.getWidescreenOrthoOffsetX(), 0.0f, 200.0f);

    RENDERER
      .queueOrthoModel(this.trademarkObj, transforms, QueuedModelStandard.class)
      .monochrome(this.logoFadeInAmount)
      .texture(this.trademarkTex);
  }

  @Method(0x800cb070L)
  private void renderMenuBackground() {
    if(!this.backgroundInitialized) {
      this.backgroundScrollAmount = -176;
      this.backgroundFadeInAmount = 0.0f;
      this.backgroundInitialized = true;
    }

    //LAB_800cb0b0
    this.backgroundFadeInAmount += 4.0f / 255.0f;
    if(this.backgroundFadeInAmount > 1.0f) {
      this.backgroundFadeInAmount = 1.0f;
    }

    //LAB_800cb0ec
    //LAB_800cb100
    RENDERER
      .queueOrthoModel(this.backgroundObj, QueuedModelStandard.class)
      .texture(this.backgroundTex)
      .screenspaceOffset(0.0f, this.backgroundScrollAmount)
      .monochrome(this.backgroundFadeInAmount);

    //LAB_800cb370
    this.backgroundScrollAmount++;
    if(this.backgroundScrollAmount > 0) {
      this.backgroundScrollAmount = 0;
    }

    //LAB_800cb3b0
  }

  @Method(0x800cb69cL)
  private void deallocate() {
    if(this._800c66d0 != null) {
      for(int i = 0; i < this._800c66d0.dobj2s_00.length; i++) {
        this._800c66d0.dobj2s_00[i].delete();
      }

      this._800c66d0 = null;
    }

    for(int i = 0; i < 4; i++) {
      this.fireAnimation_800c66d4[i] = null;
    }

    this.backgroundObj.delete();
    this.logoObj.delete();
    this.trademarkObj.delete();
    this.menuTextObj.delete();
    this.copyrightObj.delete();

    this.backgroundTex.delete();
    this.logoTex.delete();
    this.trademarkTex.delete();
    this.menuTextTex.delete();
    this.copyrightTex.delete();
  }

  @Method(0x800cb728L)
  private void renderMenuLogoFire() {
    final Vector3f rotation = new Vector3f(0.0f, -MathHelper.TWO_PI / 2.0f, 0.0f);
    final Vector3f scale = new Vector3f(0.8544922f, 1.0f, 1.0f);

    if(!this.logoFireInitialized) {
      this.logoFireInitialized = true;
      this.flameColour = 0;
    }

    //LAB_800cb7b4
    this.flameColour += 2;
    if(this.flameColour > 0xff) {
      this.flameColour = 0xff;
    }

    //LAB_800cb7f0
    GsSetRefView2L(this.GsRVIEW2_800c6760);

    final ModelPart10[] dobj2s = this._800c66d0.dobj2s_00;
    final GsCOORDINATE2[] coord2s = this._800c66d0.coord2s_04;
    final MV lw = new MV();

    //LAB_800cb834
    for(int i = 0; i < this._800c66d0.count_08; i++) {
      //LAB_800cb85c
      this.FUN_800cc26c(rotation, coord2s[i]);
      GsGetLw(dobj2s[i].coord2_04, lw);
      lw.scaleLocal(scale);
      lw.transfer.y -= 250.0f;

      RENDERER.queueModel(this._800c66d0.dobj2s_00[i].obj, lw, QueuedModelTmd.class)
        .monochrome(this.flameColour / 128.0f)
        .screenspaceOffset(8.0f, 0.0f)
        .lightDirection(lightDirectionMatrix_800c34e8)
        .lightColour(lightColourMatrix_800c3508)
        .backgroundColour(GTE.backgroundColour);
    }

    //LAB_800cb904
    //LAB_800cb908
    for(int i = 0; i < 4; i++) {
      //LAB_800cb924
      this.animateFire(this.fireAnimation_800c66d4[i]);
    }

    //LAB_800cb960
  }

  @Method(0x800cb974L)
  private void renderLogoFlash() {
    if(CONFIG.getConfig(REDUCE_MOTION_FLASHING_CONFIG.get())) {
      this.logoFlashStage = 2;
    }

    if(this.logoFlashStage == 2) {
      return;
    }

    //LAB_800cb9b0
    if(this.logoFlashStage == 0) {
      this.logoFlashStage = 1;
      this.logoFlashColour = 0;
    }

    //LAB_800cba54
    this.logoFlashColour += 0x60;
    if(this.logoFlashColour > 0x800) {
      this.logoFlashColour = 0x800;
    }

    //LAB_800cba90
    final int colour = rsin(this.logoFlashColour) * 160 >> 12;

    this.flashTransforms
      .scaling(320.0f * (((float)RENDERER.getRenderWidth() / RENDERER.getRenderHeight()) / RENDERER.getNativeAspectRatio()), 240.0f, 1.0f)
      .translate(0.0f, 0.0f, 30.0f)
    ;

    RENDERER.queueOrthoModel(RENDERER.renderBufferQuad, this.flashTransforms, QueuedModelStandard.class)
      .texture(RENDERER.getLastFrame())
      .translucency(Translucency.B_PLUS_F)
      .monochrome(colour / 128.0f);

    if(this.logoFlashColour == 0x800) {
      this.logoFlashStage = 2;
    }

    //LAB_800cbe34
  }

  @Method(0x800cbe48L)
  private TmdRenderingStruct parseTmdFile(final TmdWithId tmd) {
    final TmdRenderingStruct tmdRenderer = new TmdRenderingStruct();
    tmdRenderer.count_08 = this.prepareTmdRenderer(tmdRenderer, tmd);
    return tmdRenderer;
  }

  @Method(0x800cbf3cL)
  private int prepareTmdRenderer(final TmdRenderingStruct tmdRenderer, final TmdWithId tmd) {
    tmdRenderer.dobj2s_00 = new ModelPart10[tmd.tmd.header.nobj];
    tmdRenderer.coord2s_04 = new GsCOORDINATE2[tmd.tmd.header.nobj];

    Arrays.setAll(tmdRenderer.dobj2s_00, i -> new ModelPart10());
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
  private void FUN_800cc0b0(final TmdRenderingStruct renderer, @Nullable final GsCOORDINATE2 superCoord2) {
    //LAB_800cc0f0
    for(int i = 0; i < renderer.count_08; i++) {
      final GsCOORDINATE2 coord2 = renderer.coord2s_04[i];
      final ModelPart10 dobj2 = renderer.dobj2s_00[i];

      //LAB_800cc114
      GsInitCoordinate2(superCoord2, coord2);

      dobj2.coord2_04 = coord2;
      coord2.coord.transfer.set(100, -430, -2048);
    }

    //LAB_800cc1a8
  }

  @Method(0x800cc1bcL)
  private void setDobjAttributes(final TmdRenderingStruct renderer, final int dobjAttribute) {
    //LAB_800cc1e4
    for(int i = 0; i < renderer.count_08; i++) {
      //LAB_800cc208
      renderer.dobj2s_00[i].attribute_00 = dobjAttribute;
    }

    //LAB_800cc25c
  }

  @Method(0x800cc26cL)
  private void FUN_800cc26c(final Vector3f rotation, final GsCOORDINATE2 a1) {
    final MV m = new MV();
    m.transfer.set(a1.coord.transfer);
    m.rotationXYZ(rotation);
    a1.coord.set(m);
    a1.flg = 0;
  }

  @Method(0x800cdaa0L)
  private FireAnimationData20 FUN_800cdaa0(final Rect4i rect, final int a3) {
    final int[] addr1 = new int[rect.w() / 2 * rect.h()];
    final int[] addr2 = new int[rect.w() / 2 * rect.h()];

    return new FireAnimationData20(
      new Rect4i(rect.x(), rect.y(), rect.w() / 4, rect.h()),
      addr1, addr2,
      a3
    );
  }

  @Method(0x800cdcb0L)
  private void animateFire(final FireAnimationData20 fireAnimation) {
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
    GPU.downloadData15(sp10, fireAnimation.pixels_0c);
    GPU.downloadData15(sp18, fireAnimation.pixels_08);
    GPU.uploadData15(sp20, fireAnimation.pixels_0c);
    GPU.uploadData15(sp28, fireAnimation.pixels_08);
  }
}
