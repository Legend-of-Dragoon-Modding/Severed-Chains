package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.types.LodString;
import legend.game.types.Renderable58;

import static legend.game.SItem.Addition_8011cedc;
import static legend.game.SItem.Armed_8011ced0;
import static legend.game.SItem.Config_8011cf00;
import static legend.game.SItem.FUN_801038d4;
import static legend.game.SItem.FUN_80103b10;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.Goods_8011cf48;
import static legend.game.SItem.Half_8011c82c;
import static legend.game.SItem.List_8011cf3c;
import static legend.game.SItem.Mono_8011cf98;
import static legend.game.SItem.Morph_8011cfa4;
import static legend.game.SItem.Normal_8011cfb0;
import static legend.game.SItem.Note_8011c814;
import static legend.game.SItem.Off_8011c838;
import static legend.game.SItem.Off_8011cf6c;
import static legend.game.SItem.On_8011cf74;
import static legend.game.SItem.Replace_8011cef0;
import static legend.game.SItem.Save_8011cf10;
import static legend.game.SItem.Short_8011cfc0;
import static legend.game.SItem.Sound_8011cf7c;
import static legend.game.SItem.Status_8011ceb4;
import static legend.game.SItem.Stay_8011c820;
import static legend.game.SItem.Stereo_8011cf88;
import static legend.game.SItem.Use_it_8011cf1c;
import static legend.game.SItem.Vibrate_8011cf58;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.canSave_8011dc88;
import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.getMenuOptionY;
import static legend.game.SItem.glyphs_80114130;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox;
import static legend.game.SItem.messageBox_8011dc90;
import static legend.game.SItem.renderCentredText;
import static legend.game.SItem.renderCharacter;
import static legend.game.SItem.renderCharacterSlot;
import static legend.game.SItem.renderDragoonSpirits;
import static legend.game.SItem.renderEightDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderThreeDigitNumber;
import static legend.game.SItem.renderTwoDigitNumber;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.SItem.submapNames_8011c108;
import static legend.game.SItem.worldMapNames_8011c1ec;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bcc8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bda4;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getTimestampPart;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.recalcInventory;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.setMono;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.submapIndex_800bd808;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class MainMenuScreen extends MenuScreen {
  private int loadingStage;
  private final Runnable unload;

  private int selectedMenuOption;
  private int selectedItemSubmenuOption;
  private Renderable58 selectedMenuOptionRenderable;
  private Renderable58 selectedItemMenuOptionRenderable;

  private boolean onLeftMenu = true;

  public MainMenuScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        recalcInventory();
        FUN_80103b10();
        scriptStartEffect(2, 10);
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);
        renderGlyphs(glyphs_80114130, 0, 0);
        this.selectedMenuOptionRenderable = allocateUiElement(115, 115, 29, getMenuOptionY(this.selectedMenuOption));
        this.selectedItemMenuOptionRenderable = this.FUN_800fc900(this.selectedItemSubmenuOption);
        FUN_80104b60(this.selectedMenuOptionRenderable);
        this.FUN_80102484(0);
        this.renderItemSubmenu(this.selectedItemSubmenuOption, 4);
        this.renderInventoryMenu(this.selectedMenuOption, 4, 0xff);
        this.loadingStage++;
      }

      case 2 -> {
        this.FUN_80102484(0);
        this.renderItemSubmenu(this.selectedItemSubmenuOption, 4);
        this.renderInventoryMenu(this.selectedMenuOption, 4, 0);
      }

      case 3 -> {
        messageBox(messageBox_8011dc90);

        if(messageBox_8011dc90.ticks_10 >= 2) {
          if((joypadPress_8007a398.get() & 0x8000) != 0) {
            playSound(2);

            if(this.selectedItemSubmenuOption == 0) {
              gameState_800babc8.vibrationEnabled_4e1.set(0);
            } else if(this.selectedItemSubmenuOption == 1) {
              gameState_800babc8.mono_4e0.set(0);
              setMono(0);
            } else if(this.selectedItemSubmenuOption == 2) {
              gameState_800babc8.morphMode_4e2.set(0);
            } else if(this.selectedItemSubmenuOption == 3) {
              if(gameState_800babc8.indicatorMode_4e8.get() != 0) {
                gameState_800babc8.indicatorMode_4e8.decr();
              }
            }
          }

          if((joypadPress_8007a398.get() & 0x2000) != 0) {
            playSound(2);

            if(this.selectedItemSubmenuOption == 0) {
              gameState_800babc8.vibrationEnabled_4e1.set(1);
              FUN_8002bcc8(0, 256);
              FUN_8002bda4(0, 0, 60);
            } else if(this.selectedItemSubmenuOption == 1) {
              gameState_800babc8.mono_4e0.set(1);
              setMono(1);
            } else if(this.selectedItemSubmenuOption == 2) {
              gameState_800babc8.morphMode_4e2.set(1);
            } else if(this.selectedItemSubmenuOption == 3) {
              if(gameState_800babc8.indicatorMode_4e8.get() < 2) {
                gameState_800babc8.indicatorMode_4e8.incr();
              }
            }
          }

          this.renderOptionsMenu(gameState_800babc8.vibrationEnabled_4e1.get(), gameState_800babc8.mono_4e0.get(), gameState_800babc8.morphMode_4e2.get(), gameState_800babc8.indicatorMode_4e8.get());
        }

        this.FUN_80102484(0);
        this.renderItemSubmenu(this.selectedItemSubmenuOption, 4);
        this.renderInventoryMenu(this.selectedMenuOption, 4, 0);
      }

      // Fade out
      case 100 -> {
        this.renderInventoryMenu(this.selectedMenuOption, 4, 0);
        scriptStartEffect(1, 10);
        this.loadingStage++;
      }

      // Unload
      case 101 -> {
        this.renderInventoryMenu(this.selectedMenuOption, 4, 0);

        if(_800bb168.get() >= 0xff) {
          this.unload.run();
        }
      }
    }
  }

  private void renderInventoryMenu(final long selectedOption, final int a1, final long a2) {
    final int s5 = canSave_8011dc88.get() != 0 ? a1 : 6;

    final boolean allocate = a2 == 0xff;
    if(allocate) {
      renderDragoonSpirits(gameState_800babc8.dragoonSpirits_19c.get(0).get(), 40, 197);
      renderEightDigitNumber(67, 184, gameState_800babc8.gold_94.get(), 0); // Gold
      renderCharacter(146, 184, 10);
      renderCharacter(164, 184, 10);
      renderTwoDigitNumber(166, 204, gameState_800babc8.stardust_9c.get()); // Stardust
    }

    renderThreeDigitNumber(128, 184, getTimestampPart(gameState_800babc8.timestamp_a0.get(), 0), 0x3L);
    renderTwoDigitNumber(152, 184, getTimestampPart(gameState_800babc8.timestamp_a0.get(), 1), 0x3L);
    renderTwoDigitNumber(170, 184, getTimestampPart(gameState_800babc8.timestamp_a0.get(), 2), 0x3L);
    renderCharacterSlot(194, 16, gameState_800babc8.charIndex_88.get(0).get(), allocate, false);
    renderCharacterSlot(194, 88, gameState_800babc8.charIndex_88.get(1).get(), allocate, false);
    renderCharacterSlot(194, 160, gameState_800babc8.charIndex_88.get(2).get(), allocate, false);
    renderCentredText(chapterNames_80114248.get(gameState_800babc8.chapterIndex_98.get()).deref(), 94, 24, 4);

    final LodString v1;
    if(mainCallbackIndex_8004dd20.get() == 5) {
      v1 = submapNames_8011c108.get(submapIndex_800bd808.get()).deref();
    } else {
      v1 = worldMapNames_8011c1ec.get(continentIndex_800bf0b0.get()).deref();
    }

    renderCentredText(v1, 90, 38, 4);

    renderCentredText(Status_8011ceb4, 62, getMenuOptionY(0) + 2, selectedOption == 0 ? 5 : a1);
    renderCentredText(Armed_8011ced0, 62, getMenuOptionY(1) + 2, selectedOption == 1 ? 5 : a1);
    renderCentredText(Addition_8011cedc, 62, getMenuOptionY(2) + 2, selectedOption == 2 ? 5 : a1);
    renderCentredText(Replace_8011cef0, 62, getMenuOptionY(3) + 2, selectedOption == 3 ? 5 : a1);
    renderCentredText(Config_8011cf00, 62, getMenuOptionY(4) + 2, selectedOption == 4 ? 5 : a1);
    renderCentredText(Save_8011cf10, 62, getMenuOptionY(5) + 2, selectedOption == 5 ? 5 : s5);

    uploadRenderables();
  }

  private void renderItemSubmenu(final int selectedIndex, final int a1) {
    FUN_801038d4(150, 20, 60);
    renderCentredText(Use_it_8011cf1c, 142, this.getItemSubmenuOptionY(0), selectedIndex == 0 ? 5 : a1);
    renderCentredText(List_8011cf3c, 142, this.getItemSubmenuOptionY(1), selectedIndex == 1 ? 5 : a1);
    renderCentredText(Goods_8011cf48, 142, this.getItemSubmenuOptionY(2), selectedIndex == 2 ? 5 : a1);
    renderCentredText(new LodString("Diiig"), 142, this.getItemSubmenuOptionY(3), selectedIndex == 3 ? 5 : a1);
  }

  private void renderOptionsMenu(final long vibrateMode, final long soundMode, final long morphMode, final long noteMode) {
    textZ_800bdf00.set(32);

    renderCentredText(Vibrate_8011cf58, this.FUN_800fc7bc(0) - 15, this.menuOptionY(0), 4);
    renderCentredText(Off_8011cf6c, this.FUN_800fc7bc(1), this.menuOptionY(0), vibrateMode == 0 ? 5 : 4);
    renderCentredText(On_8011cf74, this.FUN_800fc7bc(2), this.menuOptionY(0), vibrateMode == 1 ? 5 : 4);
    renderCentredText(Sound_8011cf7c, this.FUN_800fc7bc(0) - 15, this.menuOptionY(1), 4);
    renderCentredText(Stereo_8011cf88, this.FUN_800fc7bc(1), this.menuOptionY(1), soundMode == 0 ? 5 : 4);
    renderCentredText(Mono_8011cf98, this.FUN_800fc7bc(2), this.menuOptionY(1), soundMode == 1 ? 5 : 4);
    renderCentredText(Morph_8011cfa4, this.FUN_800fc7bc(0) - 15, this.menuOptionY(2), 4);
    renderCentredText(Normal_8011cfb0, this.FUN_800fc7bc(1), this.menuOptionY(2), morphMode == 0 ? 5 : 4);
    renderCentredText(Short_8011cfc0, this.FUN_800fc7bc(2), this.menuOptionY(2), morphMode == 1 ? 5 : 4);
    renderCentredText(Note_8011c814, this.FUN_800fc7bc(0) - 15, this.menuOptionY(3), 4);
    renderCentredText(Off_8011c838, this.FUN_800fc7d0(1), this.menuOptionY(3), noteMode == 0 ? 5 : 4);
    renderCentredText(Half_8011c82c, this.FUN_800fc7d0(2), this.menuOptionY(3), noteMode == 1 ? 5 : 4);
    renderCentredText(Stay_8011c820, this.FUN_800fc7d0(3), this.menuOptionY(3), noteMode == 2 ? 5 : 4);

    textZ_800bdf00.set(33);
  }

  private int getItemSubmenuOptionY(final int option) {
    return 80 + option * 13;
  }

  private int FUN_800fc7bc(final int slot) {
    return 130 + slot * 56;
  }

  private int FUN_800fc7d0(final int slot) {
    return 130 + slot * 46;
  }

  private int menuOptionY(final int slot) {
    return 107 + slot * 13;
  }

  private void FUN_80102484(final int a0) {
    FUN_801038d4(a0 != 0 ? 23 : 24, 112, getMenuOptionY(1) + 3);
  }

  private Renderable58 FUN_800fc900(final int option) {
    final Renderable58 renderable = allocateUiElement(116, 116, 122, this.getItemSubmenuOptionY(option) - 2);
    FUN_80104b60(renderable);
    return renderable;
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    if(this.loadingStage == 2) {
      for(int i = 0; i < 6; i++) {
        if(this.selectedMenuOption != i && MathHelper.inBox(x, y, 22, getMenuOptionY(i) + 2, 84, 13)) {
          playSound(1);
          this.selectedMenuOption = i;
          this.selectedMenuOptionRenderable.y_44 = getMenuOptionY(i);
        }
      }

      for(int i = 0; i < 4; i++) {
        if(this.selectedItemSubmenuOption != i && MathHelper.inBox(x, y, 114, this.getItemSubmenuOptionY(i), 55, 13)) {
          playSound(1);
          this.selectedItemSubmenuOption = i;
          this.selectedItemMenuOptionRenderable.y_44 = this.getItemSubmenuOptionY(i) - 2;
        }
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.loadingStage == 2) {
      for(int i = 0; i < 6; i++) {
        if(MathHelper.inBox(x, y, 22, getMenuOptionY(i) + 2, 84, 13)) {
          this.selectedMenuOption = i;
          this.selectedMenuOptionRenderable.y_44 = getMenuOptionY(i);

          this.openScreen(i, true);
        }
      }

      for(int i = 0; i < 4; i++) {
        if(MathHelper.inBox(x, y, 114, this.getItemSubmenuOptionY(i), 55, 13)) {
          this.selectedItemSubmenuOption = i;
          this.selectedItemMenuOptionRenderable.y_44 = this.getItemSubmenuOptionY(i) - 2;
          this.openScreen(i, false);
        }
      }
    } else if(this.loadingStage == 3) {
      if(MathHelper.inBox(x, y, this.FUN_800fc7bc(1) - 28, this.menuOptionY(0), 56, 13)) {
        playSound(2);
        gameState_800babc8.vibrationEnabled_4e1.set(0);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7bc(2) - 28, this.menuOptionY(0), 56, 13)) {
        playSound(2);
        gameState_800babc8.vibrationEnabled_4e1.set(1);
        FUN_8002bcc8(0, 256);
        FUN_8002bda4(0, 0, 60);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7bc(1) - 28, this.menuOptionY(1), 56, 13)) {
        playSound(2);
        gameState_800babc8.mono_4e0.set(0);
        setMono(0);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7bc(2) - 28, this.menuOptionY(1), 56, 13)) {
        playSound(2);
        gameState_800babc8.mono_4e0.set(1);
        setMono(1);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7bc(1) - 28, this.menuOptionY(2), 56, 13)) {
        playSound(2);
        gameState_800babc8.morphMode_4e2.set(0);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7bc(2) - 28, this.menuOptionY(2), 56, 13)) {
        playSound(2);
        gameState_800babc8.morphMode_4e2.set(1);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7d0(1) - 28, this.menuOptionY(3), 56, 13)) {
        playSound(2);
        gameState_800babc8.indicatorMode_4e8.set(0);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7d0(2) - 28, this.menuOptionY(3), 56, 13)) {
        playSound(2);
        gameState_800babc8.indicatorMode_4e8.set(1);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7d0(3) - 28, this.menuOptionY(3), 56, 13)) {
        playSound(2);
        gameState_800babc8.indicatorMode_4e8.set(2);
      }
    }
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {
    if(this.loadingStage == 2) {
      switch(key) {
        case GLFW_KEY_ESCAPE -> {
          playSound(3);
          this.loadingStage = 100;
        }

        case GLFW_KEY_UP -> {
          if(this.onLeftMenu) {
            if(this.selectedMenuOption > 0) {
              playSound(1);
              this.selectedMenuOption--;
              this.selectedMenuOptionRenderable.y_44 = getMenuOptionY(this.selectedMenuOption);
            }
          } else if(this.selectedItemSubmenuOption > 0) {
            playSound(1);
            this.selectedItemSubmenuOption--;
            this.selectedItemMenuOptionRenderable.y_44 = this.getItemSubmenuOptionY(this.selectedItemSubmenuOption) - 2;
          }
        }

        case GLFW_KEY_DOWN -> {
          if(this.onLeftMenu) {
            if(this.selectedMenuOption < 5) {
              playSound(1);
              this.selectedMenuOption++;
              this.selectedMenuOptionRenderable.y_44 = getMenuOptionY(this.selectedMenuOption);
            }
          } else if(this.selectedItemSubmenuOption < 3) {
            playSound(1);
            this.selectedItemSubmenuOption++;
            this.selectedItemMenuOptionRenderable.y_44 = this.getItemSubmenuOptionY(this.selectedItemSubmenuOption) - 2;
          }
        }

        case GLFW_KEY_LEFT -> {
          if(!this.onLeftMenu) {
            this.onLeftMenu = true;
            playSound(1);
          }
        }

        case GLFW_KEY_RIGHT -> {
          if(this.onLeftMenu) {
            playSound(1);
            this.onLeftMenu = false;
            this.selectedItemSubmenuOption = 0;
            this.selectedItemMenuOptionRenderable.y_44 = this.getItemSubmenuOptionY(0) - 2;
          }
        }

        case GLFW_KEY_ENTER, GLFW_KEY_S -> {
          if(this.onLeftMenu) {
            this.openScreen(this.selectedMenuOption, true);
          } else {
            this.openScreen(this.selectedItemSubmenuOption, false);
          }
        }
      }
    } else if(this.loadingStage == 3) {
      playSound(2);
      messageBox_8011dc90.state_0c++;
      this.loadingStage = 1;
    }
  }

  private void fadeOutArrows() {
    if(renderablePtr_800bdba4 != null) {
      fadeOutArrow(renderablePtr_800bdba4);
      renderablePtr_800bdba4 = null;
    }

    //LAB_800fca40
    if(renderablePtr_800bdba8 != null) {
      fadeOutArrow(renderablePtr_800bdba8);
      renderablePtr_800bdba8 = null;
    }

    //LAB_800fca60
    if(saveListUpArrow_800bdb94 != null) {
      fadeOutArrow(saveListUpArrow_800bdb94);
      saveListUpArrow_800bdb94 = null;
    }

    //LAB_800fca80
    if(saveListDownArrow_800bdb98 != null) {
      fadeOutArrow(saveListDownArrow_800bdb98);
      saveListDownArrow_800bdb98 = null;
    }
  }

  private void openScreen(final int index, final boolean isLeft) {
    if(isLeft) {
      switch(index) {
        case 0 -> {
          playSound(2);

          menuStack.pushScreen(new StatusScreen(() -> {
            menuStack.popScreen();
            this.loadingStage = 0;
          }));
        }

        case 1 -> {
          playSound(2);

          menuStack.pushScreen(new EquipmentScreen(() -> {
            menuStack.popScreen();
            this.loadingStage = 0;
          }));
        }

        case 2 -> {
          playSound(2);

          menuStack.pushScreen(new AdditionsScreen(() -> {
            menuStack.popScreen();
            this.loadingStage = 0;
          }));
        }

        case 3 -> {
          playSound(2);

          menuStack.pushScreen(new CharSwapScreen(() -> {
            menuStack.popScreen();
            this.loadingStage = 0;
          }));
        }

        case 4 -> {
          playSound(4);
          this.selectedItemSubmenuOption = 0;
          setMessageBoxText(messageBox_8011dc90, null, 0x1);
          this.loadingStage = 3;
        }

        case 5 -> {
          if(canSave_8011dc88.get() != 0) {
            playSound(2);

            menuStack.pushScreen(new SaveGameScreen(() -> {
              menuStack.popScreen();
              this.fadeOutArrows();
              this.loadingStage = 0;
            }));
          } else {
            playSound(40);
          }
        }
      }
    } else {
      switch(index) {
        case 0 -> {
          playSound(2);
          menuStack.pushScreen(new UseItemScreen(() -> {
            menuStack.popScreen();
            this.loadingStage = 0;
          }));
        }

        case 1 -> {
          playSound(2);
          menuStack.pushScreen(new ItemListScreen(() -> {
            menuStack.popScreen();
            this.loadingStage = 0;
          }));
        }

        case 2 -> {
          playSound(2);
          menuStack.pushScreen(new GoodsScreen(() -> {
            menuStack.popScreen();
            this.loadingStage = 0;
          }));
        }

        case 3 -> {
          playSound(2);
          menuStack.pushScreen(new DabasScreen(() -> {
            menuStack.popScreen();
            this.loadingStage = 0;
          }));
        }
      }
    }
  }
}
