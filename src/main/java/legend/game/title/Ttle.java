package legend.game.title;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.ModelLoader;
import legend.core.gpu.Rect4i;
import legend.core.gpu.Renderable;
import legend.core.gpu.VramTexture;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MATRIX;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdWithId;
import legend.core.memory.Method;
import legend.core.opengl.Window;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.fmv.Fmv;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.tim.Tim;
import legend.game.types.GsRVIEW2;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SAVES;
import static legend.core.gpu.VramTextureLoader.palettesFromTim;
import static legend.core.gpu.VramTextureLoader.palettesFromTims;
import static legend.core.gpu.VramTextureLoader.stitch;
import static legend.core.gpu.VramTextureLoader.stitchHorizontal;
import static legend.core.gpu.VramTextureLoader.stitchVertical;
import static legend.core.gpu.VramTextureLoader.textureFromPngOneChannelBlue;
import static legend.core.gpu.VramTextureLoader.textureFromTim;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.loadAndRenderMenus;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2L;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_Xyz;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.savedGameSelected_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class Ttle extends EngineState {
  private TmdRenderingStruct _800c66d0;
  private final FireAnimationData20[] fireAnimation_800c66d4 = new FireAnimationData20[4];
  private int hasSavedGames;
  private int menuLoadingStage;
  private int logoFadeInAmount;
  private int logoFlashStage;
  private int logoFlashColour;
  private boolean backgroundInitialized;
  private int backgroundScrollAmount;
  private int backgroundFadeInAmount;
  private boolean copyrightInitialized;
  private int copyrightFadeInAmount;
  private boolean logoFireInitialized;
  private int flameColour;
  private int menuIdleTime;

  private int _800c6728;
  private int _800c672c;
  private final int[] menuOptionTransparency = {0, 0, 0};

  private int fadeOutTimer_800c6754;

  private final GsRVIEW2 GsRVIEW2_800c6760 = new GsRVIEW2();

  private int selectedMenuOption;

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
  private static boolean texturesLoaded;
  private static boolean fireLoaded;
  private static boolean renderablesLoaded;

  private final int[] _800ce7b0 = {255, 1, 255, 255};
  private final int[] _800ce7f8 = {195, 131, 128, 80, 0, 64, 16, 56, 32, 64, 48, 96, 64, 80, 80, 32, 96, 32};
  private final int[] _800ce840 = {76, 211, 140, 128, 128, 96, 0, 144, 80, 0, 176, 72, 0, 208, 72, 128, 0, 112, 128, 32, 88, 128, 64, 48, 173, 64, 48};
  private final int[] _800ce8ac = {-65, 16, -40, 34, -32, 52, -128, 34, -32, 34, 48, 34, -128, 52, -32, 52, 48, 52};

  private static Window.Events.Cursor onMouseMove;
  private static Window.Events.Click onMouseRelease;
  private static Window.Events.OnPressedWithRepeatPulse onPressedWithRepeatPulse;

  @Override
  @Method(0x800c7798L)
  public void tick() {
    switch(pregameLoadingStage_800bb10c.get()) {
      case 0 -> this.initializeMainMenu();
      case 3 -> this.renderMainMenu();
      case 4 -> this.fadeOutForNewGame();
      case 5 -> this.waitForSaveSelection();
      case 6 -> this.fadeOutMainMenu();
      case 7 -> this.fadeOutForOptions();
    }
  }

  @Method(0x800c77e4L)
  private void initializeMainMenu() {
//    SCREENS.pushScreen(new TitleScreen());
//    GameEngine.legacyUi = false;

    this.menuLoadingStage = 0;
    this.menuIdleTime = 0;
    this._800c6728 = 0;
    this._800c672c = 0;
    this.logoFadeInAmount = 0;
    this.backgroundInitialized = false;
    this.backgroundScrollAmount = -176;
    this.copyrightInitialized = false;
    this.logoFireInitialized = false;
    this.logoFlashStage = 0;
    this.fadeOutTimer_800c6754 = 0;

    texturesLoaded = false;
    fireLoaded = false;
    renderablesLoaded = false;

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
      final Rect4i rect = new Rect4i(944 + i * 16, 256, 256, 64);
      this.fireAnimation_800c66d4[i] = this.FUN_800cdaa0(rect, this._800ce7b0[i]);
    }

    startFadeEffect(2, 15);
    SetGeomOffset(0, 0);
    pregameLoadingStage_800bb10c.set(3);

    this.addInputHandlers();
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

    texturesLoaded = true;
  }

  @Method(0x800c7c18L)
  private void menuFireTmdLoaded(final String modelName, final FileData file) {
    final TmdWithId tmd = new TmdWithId(modelName, file);
    this._800c66d0 = this.parseTmdFile(tmd);
    this.FUN_800cc0b0(this._800c66d0, null);
    this.setDobjAttributes(this._800c66d0, 0);
    fireLoaded = true;
  }

  private void prepareRenderables() {
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
          this._800ce8ac[i * 2], this._800ce8ac[i * 2 + 1] + 10, 100,
          this._800ce7f8[i * 2 + 1], 16,
        i == 0 ? 79 : 0, this._800ce7f8[i * 2],
          this._800ce7f8[i * 2 + 1], 16,
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
          this._800ce8ac[i * 2] - 8, this._800ce8ac[i * 2 + 1] + 10 - 8, 100,
          this._800ce840[i * 3 + 2], 31,
          this._800ce840[i * 3], this._800ce840[i * 3 + 1],
          this._800ce840[i * 3 + 2], 32,
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

    fireRenderable = new Renderable[this._800c66d0.dobj2s_00.length];
    for(int i = 0; i < this._800c66d0.dobj2s_00.length; i++) {
      fireRenderable[i] = ModelLoader
        .fromTmd("Fire " + i, this._800c66d0.dobj2s_00[i].tmd_08)
        .texture(fireTexture)
        .palettes(firePalettes)
        .build();
    }
  }

  @Method(0x800c7e50L)
  private void fadeOutForNewGame() {
    if(this.fadeOutTimer_800c6754 == 0) {
      startFadeEffect(1, 15);
    }

    //LAB_800c7fcc
    this.fadeOutTimer_800c6754++;

    if(this.fadeOutTimer_800c6754 >= 16) {
      if(this._800c6728 == 2) {
        whichMenu_800bdc38 = WhichMenu.INIT_NEW_CAMPAIGN_MENU;
        removeInputHandlers();
        this.deallocateFire();
        this._800c6728 = 3;
      }
    }

    //LAB_800c8038
    loadAndRenderMenus();

    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      if(savedGameSelected_800bdc34.get()) {
        removeInputHandlers();
        this.deallocateFire();

        Fmv.playCurrentFmv(2, EngineStateEnum.TRANSITION_TO_NEW_GAME_03);

        pregameLoadingStage_800bb10c.set(0);
        return;
      }

      if(this._800c6728 == 3) {
        engineStateOnceLoaded_8004dd24 = EngineStateEnum.TITLE_02;
        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8 = 2;
      } else {
        this.renderMenuLogo();
        this.renderMenuOptions();
        this.renderMenuLogoFire();
        this.renderMenuBackground();
        this.renderCopyright();
      }
    }
  }

  private void fadeOutForOptions() {
    if(this.fadeOutTimer_800c6754 == 0) {
      startFadeEffect(1, 15);
    }

    //LAB_800c7fcc
    this.fadeOutTimer_800c6754++;

    if(this.fadeOutTimer_800c6754 >= 16) {
      if(this._800c6728 == 2) {
        whichMenu_800bdc38 = WhichMenu.INIT_OPTIONS_MENU;
        removeInputHandlers();
        this.deallocateFire();
        this._800c6728 = 3;
      }
    }

    //LAB_800c8038
    loadAndRenderMenus();

    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      if(this._800c6728 == 3) {
        ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));
        engineStateOnceLoaded_8004dd24 = EngineStateEnum.TITLE_02;
        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8 = 2;
      } else {
        this.renderMenuLogo();
        this.renderMenuOptions();
        this.renderMenuLogoFire();
        this.renderMenuBackground();
        this.renderCopyright();
      }
    }
  }

  @Method(0x800c7fa0L)
  private void waitForSaveSelection() {
    if(this.fadeOutTimer_800c6754 == 0) {
      startFadeEffect(1, 15);
    }

    //LAB_800c7fcc
    this.fadeOutTimer_800c6754++;

    if(this.fadeOutTimer_800c6754 >= 16) {
      if(this._800c6728 == 2) {
        whichMenu_800bdc38 = WhichMenu.INIT_CAMPAIGN_SELECTION_MENU;
        removeInputHandlers();
        this.deallocateFire();
        this._800c6728 = 3;
      }
    }

    //LAB_800c8038
    loadAndRenderMenus();

    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      if(savedGameSelected_800bdc34.get()) {
        if(gameState_800babc8.isOnWorldMap_4e4) {
          engineStateOnceLoaded_8004dd24 = EngineStateEnum.WORLD_MAP_08;
        } else {
          //LAB_800c80a4
          engineStateOnceLoaded_8004dd24 = EngineStateEnum.SUBMAP_05;
        }

        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8 = 2;

        //LAB_800c80c4
        return;
      }

      //LAB_800c80cc
      if(this._800c6728 == 3) {
        engineStateOnceLoaded_8004dd24 = EngineStateEnum.TITLE_02;
        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8 = 2;
      } else {
        //LAB_800c8108
        this.renderMenuLogo();
        this.renderMenuOptions();
        this.renderMenuLogoFire();
        this.renderMenuBackground();
        this.renderCopyright();
      }
    }

    //LAB_800c8138
  }

  @Method(0x800c8148L)
  private void fadeOutMainMenu() {
    if(this.fadeOutTimer_800c6754 == 0) {
      startFadeEffect(1, 15);
    }

    //LAB_800c8174
    this.renderMenuLogo();
    this.renderMenuOptions();
    this.renderMenuLogoFire();
    this.renderMenuBackground();
    this.renderCopyright();

    this.fadeOutTimer_800c6754++;
    if(this.fadeOutTimer_800c6754 > 15) {
      removeInputHandlers();
      this.deallocateFire();

      Fmv.playCurrentFmv(0, EngineStateEnum.TITLE_02);

      pregameLoadingStage_800bb10c.set(0);
    }

    //LAB_800c8218
  }

  @Method(0x800c8298L)
  private void renderMainMenu() {
    if(!renderablesLoaded) {
      if(!texturesLoaded || !fireLoaded) {
        return;
      }

      this.prepareRenderables();
    }

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

      if(this.logoFadeInAmount >= 128) {
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
        if(this.menuLoadingStage == 3) {
          this.handleMainInput();
        }

        this.renderMenuLogo();
        this.renderMenuOptions();
        this.renderMenuLogoFire();
        this.renderCopyright();

        if(this.menuLoadingStage == 4) {
          this.menuLoadingStage = 3;
        }
      }
    }

    //LAB_800c83c8
    this.renderMenuBackground();

    if(this._800c6728 != 1) {
      this.menuIdleTime += 2;

      if(this.menuIdleTime > 1680) {
        pregameLoadingStage_800bb10c.set(6);
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
      final float aspect = 4.0f / 3.0f;

      float w = window.getWidth();
      float h = w / aspect;

      if(h > window.getHeight()) {
        h = window.getHeight();
        w = h * aspect;
      }

      final float scaleX = w / GPU.getDisplayTextureWidth();
      final float scaleY = h / GPU.getDisplayTextureHeight();

      if(this.menuLoadingStage == 3) {
        if(this._800c672c < 3) {
          for(int i = 0; i < menuTextRenderables.length; i++) {
            if(i == 1 && this.hasSavedGames != 1) {
              continue;
            }

            final int menuWidth = (int)(155 * scaleX);
            final int menuHeight = (int)(16 * scaleY);
            final int menuX = (window.getWidth() - menuWidth) / 2;
            final int menuY = (int)((this._800ce8ac[i * 2 + 1] + 10) * scaleY) + window.getHeight() / 2;

            if(MathHelper.inBox((int)x, (int)y, menuX, menuY, menuWidth, menuHeight)) {
              if(this.selectedMenuOption != i) {
                playSound(0, 1, 0, 0, (short)0, (short)0);
                this.selectedMenuOption = i;
              }

              break;
            }
          }
        }
      }

      this.menuIdleTime = 0;
    });

    onMouseRelease = RENDERER.events().onMouseRelease((window, x, y, button, mods) -> {
      if(button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
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

        final float scaleX = w / GPU.getDisplayTextureWidth();
        final float scaleY = h / GPU.getDisplayTextureHeight();

        if(this._800c672c < 3) {
          for(int i = 0; i < menuTextRenderables.length; i++) {
            if(i == 1 && this.hasSavedGames != 1) {
              continue;
            }

            final int menuWidth = (int)(155 * scaleX);
            final int menuHeight = (int)(16 * scaleY);
            final int menuX = (window.getWidth() - menuWidth) / 2;
            final int menuY = (int)((this._800ce8ac[i * 2 + 1] + 10) * scaleY) + window.getHeight() / 2;

            if(MathHelper.inBox((int)x, (int)y, menuX, menuY, menuWidth, menuHeight)) {
              playSound(0, 2, 0, 0, (short)0, (short)0);
              this.selectedMenuOption = i;

              this._800c672c = 3;
              break;
            }
          }
        }
      }
    });

    onPressedWithRepeatPulse = RENDERER.events().onPressedWithRepeatPulse((window, inputAction) -> this.resetIdleTime());
  }

  private static void removeInputHandlers() {
    RENDERER.events().removeMouseMove(onMouseMove);
    RENDERER.events().removeMouseRelease(onMouseRelease);
    RENDERER.events().removePressedWithRepeatPulse(onPressedWithRepeatPulse);
    onMouseMove = null;
    onMouseRelease = null;
    onPressedWithRepeatPulse = null;
  }

  @Method(0x800c8484L)
  private void handleMainInput() {
    if(this._800c672c < 3) {
      if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) { // Menu button X
        playSound(0, 2, 0, 0, (short)0, (short)0);

        this._800c672c = 3;
      } else if(Input.pressedThisFrame(InputAction.DPAD_UP) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_UP)) { // Menu button up
        playSound(0, 1, 0, 0, (short)0, (short)0);

        this.selectedMenuOption--;
        if(this.selectedMenuOption < 0) {
          this.selectedMenuOption = menuTextRenderables.length - 1;
        }

        if(this.selectedMenuOption == 1 && this.hasSavedGames != 1) {
          this.selectedMenuOption--;
        }

        this._800c672c = 2;
      } else if(Input.pressedThisFrame(InputAction.DPAD_DOWN) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) { // Menu button down
        playSound(0, 1, 0, 0, (short)0, (short)0);

        this.selectedMenuOption++;
        if(this.selectedMenuOption >= menuTextRenderables.length) {
          this.selectedMenuOption = 0;
        }

        if(this.selectedMenuOption == 1 && this.hasSavedGames != 1) {
          this.selectedMenuOption++;
        }

        this._800c672c = 2;
      }
    }
  }

  @Method(0x800c8634L)
  private void renderMenuOptions() {
    if(this.hasSavedGames == 0) {
      SAVES.updateUncategorizedSaves();

      this.hasSavedGames = SAVES.hasCampaigns() ? 1 : 2;
      this.selectedMenuOption = this.hasSavedGames == 1 ? 1 : 0;
      return;
    }

    //LAB_800c868c
    switch(this._800c672c) {
      case 0 -> {
        //LAB_800c86d8
        for(int i = 0; i < menuTextRenderables.length; i++) {
          //LAB_800c86f4
          this.menuOptionTransparency[i] = 0;
        }

        //LAB_800c8728
        this._800c672c = 1;
      }

      case 1 -> {
        //LAB_800c8740
        //LAB_800c886c
        for(int i = 0; i < menuTextRenderables.length; i++) {
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
      }

      case 2 -> {
        //LAB_800c8878
        //LAB_800c89e4
        for(int i = 0; i < menuTextRenderables.length; i++) {
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
        this._800c672c = 4;
        if(this.selectedMenuOption == 0) {
          this._800c6728 = 2;
          pregameLoadingStage_800bb10c.set(4);
          //LAB_800c8a20
        } else if(this.selectedMenuOption == 1) {
          this._800c6728 = 2;
          pregameLoadingStage_800bb10c.set(5);
        } else if(this.selectedMenuOption == 2) {
          this._800c6728 = 2;
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
      if(i != 1 || this.hasSavedGames == 1) {
        colour = this.menuOptionTransparency[i];
      } else {
        colour = this.menuOptionTransparency[i] / 2;
      }

      //LAB_800c8a8c
      menuTextRenderables[i]
        .recolourMono(colour)
        .palette(this.selectedMenuOption == i ? 5 : 2)
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
  private void renderCopyright() {
    if(!this.copyrightInitialized) {
      this.copyrightFadeInAmount = 0;
      this.copyrightInitialized = true;
    }

    //LAB_800cab7c
    this.copyrightFadeInAmount += 4;
    if(this.copyrightFadeInAmount > 0x80) {
      this.copyrightFadeInAmount = 0x80;
    }

    //LAB_800cabb8
    //LAB_800cabcc
    //LAB_800cabe8
    copyrightRenderable.recolourMono(this.copyrightFadeInAmount).render();
  }

  @Method(0x800cadd0L)
  private void renderMenuLogo() {
    this.logoFadeInAmount += 4;
    if(this.logoFadeInAmount > 0x80) {
      this.logoFadeInAmount = 0x80;
    }

    //LAB_800cae18
    //LAB_800cae2c
    //LAB_800cae48
    logoRenderable.recolourMono(this.logoFadeInAmount).render();
    tmRenderable.recolourMono(this.logoFadeInAmount).render();
  }

  @Method(0x800cb070L)
  private void renderMenuBackground() {
    if(!this.backgroundInitialized) {
      this.backgroundScrollAmount = -176;
      this.backgroundFadeInAmount = 0;
      this.backgroundInitialized = true;
    }

    //LAB_800cb0b0
    this.backgroundFadeInAmount += 2;
    if(this.backgroundFadeInAmount > 0x80) {
      this.backgroundFadeInAmount = 0x80;
    }

    //LAB_800cb0ec
    //LAB_800cb100
    backgroundRenderable
      .recolourMono(this.backgroundFadeInAmount)
      .render(0, this.backgroundScrollAmount);

    //LAB_800cb370
    this.backgroundScrollAmount++;
    if(this.backgroundScrollAmount > 0) {
      this.backgroundScrollAmount = 0;
    }

    //LAB_800cb3b0
  }

  @Method(0x800cb69cL)
  private void deallocateFire() {
    this._800c66d0 = null;

    for(int i = 0; i < 4; i++) {
      this.fireAnimation_800c66d4[i] = null;
    }
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

    //LAB_800cb834
    for(int i = 0; i < this._800c66d0.count_08; i++) {
      final MATRIX sp10 = new MATRIX();
      final MATRIX sp30 = new MATRIX();

      //LAB_800cb85c
      this.FUN_800cc26c(rotation, coord2s[i]);
      GsGetLws(dobj2s[i].coord2_04, sp10, sp30);
      GsSetLightMatrix(sp10);
      sp30.scaleL(scale);
      setRotTransMatrix(sp30);
      zOffset_1f8003e8.set(100);
      fireRenderable[i]
        .colourMultiplier(this.flameColour)
        .render();
      zOffset_1f8003e8.set(0);
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

    // GP0.66 Textured quad, variable size, translucent, blended
    final GpuCommandQuad cmd = new GpuCommandQuad()
      .translucent(Translucency.B_PLUS_F)
      .bpp(Bpp.BITS_15)
      .monochrome(colour)
      .pos(-192, -120, 384, 240)
      .texture(GPU.getDisplayBuffer());

    GPU.queueCommand(5, cmd);

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
  private void FUN_800cc26c(final Vector3f a0, final GsCOORDINATE2 a1) {
    final MATRIX m = new MATRIX().identity();
    m.transfer.set(a1.coord.transfer);
    RotMatrix_Xyz(a0, m);
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
    fireTexture.getRegion(sp10, fireAnimation.pixels_0c);
    fireTexture.getRegion(sp18, fireAnimation.pixels_08);
    fireTexture.setRegion(sp20, fireAnimation.pixels_0c);
    fireTexture.setRegion(sp28, fireAnimation.pixels_08);
  }
}
