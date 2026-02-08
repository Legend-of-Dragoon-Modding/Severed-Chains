package legend.game;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.MainMenuScreen;
import legend.game.inventory.screens.MenuScreen;
import legend.game.tim.Tim;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.Translucency;
import legend.game.types.UiFile;
import legend.game.types.UiPart;
import legend.game.types.UiType;
import legend.game.unpacker.FileData;
import org.joml.Math;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Graphics.displayWidth_1f8003e0;
import static legend.game.Graphics.renderMode;
import static legend.game.Graphics.resizeDisplay;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.startMenuMusic;
import static legend.game.SItem.stopMenuMusic;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Text.textZ_800bdf00;
import static org.lwjgl.opengl.GL11C.GL_LEQUAL;

public final class Menus {
  private Menus() { }

  public static Renderable58 upArrow_800bdb94;
  public static Renderable58 downArrow_800bdb98;

  public static Renderable58 leftArrowRenderable_800bdba4;
  public static Renderable58 rightArrowRenderable_800bdba8;

  /**
   * 0xe - load game
   * 0x13 - also load game (maybe save game...?)
   * 0x18 - char swap
   *
   * Seems any other value shows the inventory
   */
  public static WhichMenu whichMenu_800bdc38 = WhichMenu.NONE_0;
  public static UiFile uiFile_800bdc3c;

  public static Renderable58 renderablePtr_800bdc5c;

  public static void initMenu(final WhichMenu destMenu, @Nullable final Supplier<MenuScreen> destScreen) {
    startMenuMusic();
    SCRIPTS.stop();

    renderablePtr_800bdc5c = null;
    resizeDisplay(384, 240);
    renderMode = EngineState.RenderMode.LEGACY;
    textZ_800bdf00 = 33;

    whichMenu_800bdc38 = destMenu;

    if(destScreen != null) {
      menuStack.pushScreen(destScreen.get());
    }
  }

  public static void initInventoryMenu() {
    initMenu(WhichMenu.RENDER_NEW_MENU, () -> new MainMenuScreen(() -> {
      menuStack.popScreen();
      whichMenu_800bdc38 = WhichMenu.UNLOAD;
    }));
  }

  @Method(0x80022590L)
  public static void loadAndRenderMenus() {
    switch(whichMenu_800bdc38) {
      case RENDER_NEW_MENU, RENDER_SAVE_GAME_MENU_19 -> menuStack.render();

      case UNLOAD, UNLOAD_SAVE_GAME_MENU_20, UNLOAD_POST_COMBAT_REPORT_30 -> {
        menuStack.reset();

        if(whichMenu_800bdc38 != WhichMenu.UNLOAD_SAVE_GAME_MENU_20 && whichMenu_800bdc38 != WhichMenu.UNLOAD_POST_COMBAT_REPORT_30) {
          stopMenuMusic();
        }

        SCRIPTS.start();
        whichMenu_800bdc38 = WhichMenu.NONE_0;

        deallocateRenderables(0xff);
        startFadeEffect(2, 10);
        currentEngineState_8004dd04.menuClosed();

        textZ_800bdf00 = 13;
      }

      case UNLOAD_QUIETLY -> {
        menuStack.reset();
        whichMenu_800bdc38 = WhichMenu.NONE_0;
        deallocateRenderables(0xff);
        textZ_800bdf00 = 13;
      }
    }
  }

  @Method(0x80022a94L)
  public static void loadMenuTexture(final FileData data) {
    final Tim tim = new Tim(data);
    final Rect4i imageRect = tim.getImageRect();

    if(imageRect.w != 0 || imageRect.h != 0) {
      imageRect.x -= 512;
      GPU.uploadData15(imageRect, tim.getImageData());
    }

    //LAB_80022acc
    if(tim.hasClut()) {
      final Rect4i clutRect = tim.getClutRect();
      clutRect.x -= 512;
      GPU.uploadData15(clutRect, tim.getClutData());
    }

    //LAB_80022aec
  }

  @Method(0x80023b54L)
  public static Renderable58 allocateRenderable(final UiType a0, @Nullable Renderable58 a1) {
    a1 = allocateManualRenderable(a0, a1);

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

  public static Renderable58 allocateManualRenderable() {
    return allocateManualRenderable(uiFile_800bdc3c.uiElements_0000(), null);
  }

  public static Renderable58 allocateManualRenderable(final UiType uiType, @Nullable Renderable58 renderable) {
    if(renderable == null) {
      renderable = new Renderable58();
    }

    //LAB_80023b7c
    renderable.flags_00 = 0;
    renderable.glyph_04 = 0;
    renderable.ticksPerFrame_08 = uiType.entries_08[0].ticksPerFrame();
    renderable.animationLoopsCompletedCount_0c = 0;
    renderable.startGlyph_10 = 0;
    renderable.endGlyph_14 = uiType.entries_08.length - 1;
    renderable.repeatStartGlyph_18 = 0;
    renderable.repeatEndGlyph_1c = 0;
    renderable.uiType_20 = uiType;

    renderable.deallocationGroup_28 = 0;
    renderable.tpage_2c = 0;
    renderable.widthScale = 1.0f;
    renderable.heightScale_38 = 1.0f;
    renderable.z_3c = 36;
    renderable.x_40 = 0;
    renderable.y_44 = 0;
    renderable.child_50 = null;

    return renderable;
  }

  @Method(0x80023c28L)
  public static void uploadRenderables() {
    uploadRenderable(renderablePtr_800bdc5c, 0, 0);
  }

  private static final List<Renderable58> renderables = new ArrayList<>();
  private static final Comparator<Renderable58> renderableDepthComparator = Comparator.comparingDouble((Renderable58 r) -> r.z_3c).reversed();

  public static void uploadRenderable(Renderable58 renderable, final int x, final int y) {
    while(renderable != null) {
      renderable.baseX = x;
      renderable.baseY = y;
      renderables.add(renderable);
      renderable = renderable.parent_54;
    }
  }

  public static void renderUi() {
    if(renderables.isEmpty()) {
      return;
    }

    renderables.sort(renderableDepthComparator);

    //LAB_80023c8c
    for(int i = 0; i < renderables.size(); i++) {
      final Renderable58 renderable = renderables.get(i);

      // If a glyph is unloaded after being queued
      if(renderable.glyph_04 == -1) {
        continue;
      }

      boolean forceUnload = false;
      final UiPart[] entries = renderable.uiType_20.entries_08;

      if((renderable.flags_00 & Renderable58.FLAG_NO_ANIMATION) == 0) {
        if(tickCount_800bb0fc % Math.max(1, 3 - vsyncMode_8007a3b8) == 0) {
          renderable.ticksPerFrame_08--;
        }

        if(renderable.ticksPerFrame_08 < 0) {
          if((renderable.flags_00 & Renderable58.FLAG_BACKWARDS_ANIMATION) != 0) {
            renderable.glyph_04--;

            if(renderable.glyph_04 < renderable.startGlyph_10) {
              if((renderable.flags_00 & Renderable58.FLAG_DELETE_AFTER_ANIMATION) != 0) {
                forceUnload = true;
                renderable.flags_00 |= Renderable58.FLAG_INVISIBLE;
              }

              //LAB_80023d0c
              if(renderable.repeatStartGlyph_18 != 0) {
                renderable.startGlyph_10 = renderable.repeatStartGlyph_18;

                if(renderable.repeatEndGlyph_1c != 0) {
                  renderable.endGlyph_14 = renderable.repeatEndGlyph_1c;
                } else {
                  //LAB_80023d34
                  renderable.endGlyph_14 = renderable.repeatStartGlyph_18;
                  renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
                }

                //LAB_80023d48
                renderable.repeatStartGlyph_18 = 0;
                renderable.flags_00 &= ~Renderable58.FLAG_BACKWARDS_ANIMATION;
              }

              //LAB_80023d5c
              //LAB_80023e00
              renderable.glyph_04 = renderable.endGlyph_14;
              renderable.animationLoopsCompletedCount_0c++;
            }
          } else {
            //LAB_80023d6c
            renderable.glyph_04++;

            if(renderable.glyph_04 > renderable.endGlyph_14) {
              if((renderable.flags_00 & Renderable58.FLAG_DELETE_AFTER_ANIMATION) != 0) {
                forceUnload = true;
                renderable.flags_00 |= Renderable58.FLAG_INVISIBLE;
              }

              //LAB_80023da4
              if(renderable.repeatStartGlyph_18 != 0) {
                renderable.startGlyph_10 = renderable.repeatStartGlyph_18;

                if(renderable.repeatEndGlyph_1c != 0) {
                  renderable.endGlyph_14 = renderable.repeatEndGlyph_1c;
                } else {
                  //LAB_80023dcc
                  renderable.endGlyph_14 = renderable.repeatStartGlyph_18;
                  renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
                }

                //LAB_80023de0
                renderable.repeatStartGlyph_18 = 0;
                renderable.flags_00 &= ~Renderable58.FLAG_BACKWARDS_ANIMATION;
              }

              //LAB_80023df4
              //LAB_80023e00
              renderable.glyph_04 = renderable.startGlyph_10;
              renderable.animationLoopsCompletedCount_0c++;
            }
          }

          //LAB_80023e08
          renderable.ticksPerFrame_08 = entries[renderable.glyph_04].ticksPerFrame() - 1;
        }
      }

      //LAB_80023e28
      if((renderable.flags_00 & Renderable58.FLAG_INVISIBLE) == 0) {
        final int centreX = displayWidth_1f8003e0 / 2 + 8;

        final RenderableMetrics14[] metricses = entries[renderable.glyph_04].metrics_00();

        //LAB_80023e94
        for(int metricsIndex = Math.min(metricses.length, renderable.metricsCount) - 1; metricsIndex >= 0; metricsIndex--) {
          final RenderableMetrics14 metrics = metricses[metricsIndex];

          final float x1;
          final float width;
          if(MathHelper.flEq(renderable.widthScale, 1.0f)) {
            if(metrics.widthScale_10 < 0) {
              width = -metrics.width_08;
              x1 = renderable.x_40 + metrics.x_02 - centreX + metrics.width_08;
            } else {
              //LAB_80023f20
              width = metrics.width_08;
              x1 = renderable.x_40 + metrics.x_02 - centreX;
            }
          } else {
            //LAB_80023f40
            final float widthScale = !MathHelper.flEq(renderable.widthScale, 0.0f) ? renderable.widthScale : metrics.widthScale_10;

            //LAB_80023f4c
            //LAB_80023f68
            final float scaledWidth = Math.abs(metrics.width_08 * widthScale);
            if(metrics.widthScale_10 < 0) {
              width = -scaledWidth;
              x1 = renderable.x_40 + metrics.width_08 / 2.0f + metrics.x_02 - centreX - scaledWidth / 2.0f + scaledWidth;
            } else {
              //LAB_80023fb4
              width = scaledWidth;
              x1 = renderable.x_40 + metrics.width_08 / 2.0f + metrics.x_02 - centreX - scaledWidth / 2.0f;
            }
          }

          //LAB_80023fe4
          final float y1;
          final float height;
          if(MathHelper.flEq(renderable.heightScale_38, 1.0f)) {
            if(metrics.heightScale_12 < 0) {
              height = -metrics.height_0a;
              y1 = renderable.y_44 + metrics.y_03 - 120.0f + metrics.height_0a;
            } else {
              //LAB_80024024
              height = metrics.height_0a;
              y1 = renderable.y_44 + metrics.y_03 - 120.0f;
            }
          } else {
            //LAB_80024044
            final float heightScale = !MathHelper.flEq(renderable.heightScale_38, 0.0f) ? renderable.heightScale_38 : metrics.heightScale_12;

            //LAB_80024050
            //LAB_8002406c
            final float scaledHeight = Math.abs(metrics.height_0a * heightScale);
            if(metrics.heightScale_12 < 0) {
              height = -scaledHeight;
              y1 = renderable.y_44 + metrics.height_0a / 2.0f + metrics.y_03 - scaledHeight / 2.0f - 120.0f + scaledHeight;
            } else {
              //LAB_800240b8
              height = scaledHeight;
              y1 = renderable.y_44 + metrics.height_0a / 2.0f + metrics.y_03 - scaledHeight / 2.0f - 120.0f;
            }
          }

          //LAB_800240e8
          //LAB_80024144
          //LAB_800241b4
          final int clut = renderable.clut_30 != 0 ? renderable.clut_30 : metrics.clut_04 & 0x7fff;
          final int tpage = renderable.tpage_2c != 0 ? metrics.tpage_06 & 0x60 | renderable.tpage_2c : metrics.tpage_06 & 0x7f;

          //LAB_8002424c
          if(renderable.uiType_20.obj != null) {
            final MV transforms = new MV();
            transforms.scaling(width, height, 1.0f);
            transforms.transfer.set(x1 + renderable.baseX + centreX - 8 + (width < 0 ? 1.0f : 0.0f), y1 + renderable.baseY + 120.0f + (height < 0 ? 1.0f : 0.0f), renderable.z_3c * 4.0f);

            int tpageX = (tpage & 0b1111) * 64;
            int clutX = (clut & 0b111111) * 16;

            if(!renderable.useOriginalTpage) {
              tpageX -= 512;
              clutX -= 512;
            }

            final QueuedModelStandard model = RENDERER
              .queueOrthoModel(renderable.uiType_20.obj, transforms, QueuedModelStandard.class)
              .vertices(metrics.vertexStart, 4)
              .tpageOverride(tpageX, (tpage & 0b10000) != 0 ? 256 : 0)
              .clutOverride(clutX, clut >>> 6)
              .colour(renderable.colour)
              .translucentDepthComparator(GL_LEQUAL)
              .opaqueDepthComparator(GL_LEQUAL)
            ;

            if((metrics.clut_04 & 0x8000) != 0) {
              model
                .translucency(Translucency.of(tpage >>> 5 & 0b11))
              ;
            }

            metrics.useTexture(model);

            if(renderable.widthCut != 0 || renderable.heightCut != 0) {
              final int y;
              final int h;
              if(renderable.heightCut == 0) {
                y = (int)transforms.transfer.y;
                h = (int)height;
              } else {
                y = (int)(transforms.transfer.y + height - renderable.heightCut);
                h = renderable.heightCut;
              }

              final int w;
              if(renderable.widthCut == 0) {
                w = (int)width;
              } else {
                w = (int)(width - renderable.widthCut);
              }

              model.scissor((int)transforms.transfer.x, y, w, h);
            }
          }
        }
      }

      //LAB_80024280
      if((renderable.flags_00 & Renderable58.FLAG_DELETE_AFTER_RENDER) != 0 || forceUnload) {
        //LAB_800242a8
        unloadRenderable(renderable);
      }
    }

    //LAB_800242b8
    renderables.clear();
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
  public static void deallocateRenderables(final int a0) {
    // Clear out UI render queue
    renderables.clear();

    Renderable58 s0 = renderablePtr_800bdc5c;

    if(s0 != null) {
      //LAB_800243b4
      while(s0.parent_54 != null) {
        final Renderable58 a0_0 = s0;
        s0 = s0.parent_54;

        if(a0_0.deallocationGroup_28 <= a0) {
          unloadRenderable(a0_0);
        }

        //LAB_800243d0
      }

      //LAB_800243e0
      if(s0.deallocationGroup_28 <= a0) {
        unloadRenderable(s0);
      }

      //LAB_800243fc
      if(a0 != 0) {
        upArrow_800bdb94 = null;
        downArrow_800bdb98 = null;
        leftArrowRenderable_800bdba4 = null;
        rightArrowRenderable_800bdba8 = null;
      }
    }

    //LAB_80024460
  }

  @Method(0x800e6d60L)
  public static void FUN_800e6d60() {
    renderablePtr_800bdc5c = null;
  }
}
