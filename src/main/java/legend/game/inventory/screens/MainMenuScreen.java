package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.types.LodString;
import legend.game.types.Renderable58;

import static legend.game.SItem.Addition_8011cedc;
import static legend.game.SItem.Armed_8011ced0;
import static legend.game.SItem.Config_8011cf00;
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
import static legend.game.SItem.allocateOneFrameGlyph;
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

public class MainMenuScreen extends MenuScreen {
  private int loadingStage;
  private final Runnable unload;

  private int selectedMenuOption;
  private int selectedItemSubmenuOption;
  private int selectedConfigOption;
  private Renderable58 selectedMenuOptionRenderable;
  private Renderable58 selectedItemMenuOptionRenderable;

  private Renderable58 selectedConfigMenuOptionRenderable;

  private boolean onLeftMenu = true;

  public MainMenuScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        FUN_80103b10();
        scriptStartEffect(2, 10);
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);
        renderGlyphs(glyphs_80114130, 0, 0);
        this.selectedMenuOptionRenderable = allocateUiElement(115, 115, 29, getMenuOptionY(this.selectedMenuOption));
        this.selectedConfigMenuOptionRenderable = this.getRendererForHighlight();
        this.selectedItemMenuOptionRenderable = this.FUN_800fc900(this.selectedItemSubmenuOption);
        if(this.onLeftMenu) {
          this.selectedItemSubmenuOption = -1;
        }
        else{
          this.selectedItemMenuOptionRenderable.x_40 = 122;
        }
        FUN_80104b60(this.selectedMenuOptionRenderable);
        this.FUN_80102484(0);
        this.renderItemSubmenu(this.selectedItemSubmenuOption, TextColour.MIDDLE_BROWN);
        this.renderInventoryMenu(this.selectedMenuOption, TextColour.BROWN, 0xff);
        this.loadingStage++;
      }

      case 2 -> {
        this.FUN_80102484(0);
        this.renderItemSubmenu(this.selectedItemSubmenuOption, this.onLeftMenu ? TextColour.MIDDLE_BROWN : TextColour.BROWN);
        this.renderInventoryMenu(this.selectedMenuOption, this.onLeftMenu ? TextColour.BROWN : TextColour.MIDDLE_BROWN, 0);
      }

      case 3 -> {
         messageBox(messageBox_8011dc90);
        if(messageBox_8011dc90.ticks_10 >= 2) {
          //LOGGER.error("This will spam");
          if((joypadPress_8007a398.get() & 0x8000) != 0) {
            playSound(2);
            if(this.selectedConfigOption == 0) {
              gameState_800babc8.vibrationEnabled_4e1 = false;
            } else if(this.selectedConfigOption == 1) {
              gameState_800babc8.mono_4e0 = false;
              setMono(false);
            } else if(this.selectedConfigOption == 2) {
              gameState_800babc8.morphMode_4e2 = 0;
            } else if(this.selectedConfigOption == 3) {
              if(gameState_800babc8.indicatorMode_4e8 != 0) {
                gameState_800babc8.indicatorMode_4e8--;
              }
            }
          }

          if((joypadPress_8007a398.get() & 0x2000) != 0) {
            playSound(2);
            if(this.selectedConfigOption == 0) {
              gameState_800babc8.vibrationEnabled_4e1 = true;
              FUN_8002bcc8(0, 256);
              FUN_8002bda4(0, 0, 60);
            } else if(this.selectedConfigOption == 1) {
              gameState_800babc8.mono_4e0 = true;
              setMono(true);
            } else if(this.selectedConfigOption == 2) {
              gameState_800babc8.morphMode_4e2 = 1;
            } else if(this.selectedConfigOption == 3) {
              if(gameState_800babc8.indicatorMode_4e8 < 2) {
                gameState_800babc8.indicatorMode_4e8++;
              }
            }
          }

          this.renderOptionsMenu(gameState_800babc8.vibrationEnabled_4e1, gameState_800babc8.mono_4e0, gameState_800babc8.morphMode_4e2, gameState_800babc8.indicatorMode_4e8);
        }

        this.FUN_80102484(0);
        this.renderItemSubmenu(this.selectedItemSubmenuOption, TextColour.MIDDLE_BROWN);
        this.renderInventoryMenu(this.selectedMenuOption, TextColour.BROWN, 0);
      }

      // Fade out
      case 100 -> {
        this.renderInventoryMenu(this.selectedMenuOption, TextColour.BROWN, 0);
        scriptStartEffect(1, 10);
        this.loadingStage++;
      }

      // Unload
      case 101 -> {
        this.renderInventoryMenu(this.selectedMenuOption, TextColour.BROWN, 0);

        if(_800bb168.get() >= 0xff) {
          this.unload.run();
        }
      }
    }
  }

  private void renderInventoryMenu(final long selectedOption, final TextColour textColour, final long a2) {
    final TextColour saveColour = canSave_8011dc88.get() != 0 ? textColour : TextColour.MIDDLE_BROWN;

    final boolean allocate = a2 == 0xff;
    if(allocate) {
      renderDragoonSpirits(gameState_800babc8.goods_19c[0], 40, 197);
      renderEightDigitNumber(67, 184, gameState_800babc8.gold_94, 0); // Gold
      renderCharacter(146, 184, 10);
      renderCharacter(164, 184, 10);
      renderTwoDigitNumber(166, 204, gameState_800babc8.stardust_9c); // Stardust
    }

    renderThreeDigitNumber(128, 184, getTimestampPart(gameState_800babc8.timestamp_a0, 0), 0x3L);
    renderTwoDigitNumber(152, 184, getTimestampPart(gameState_800babc8.timestamp_a0, 1), 0x3L);
    renderTwoDigitNumber(170, 184, getTimestampPart(gameState_800babc8.timestamp_a0, 2), 0x3L);
    renderCharacterSlot(194, 16, gameState_800babc8.charIndex_88[0], allocate, false);
    renderCharacterSlot(194, 88, gameState_800babc8.charIndex_88[1], allocate, false);
    renderCharacterSlot(194, 160, gameState_800babc8.charIndex_88[2], allocate, false);
    renderCentredText(chapterNames_80114248.get(gameState_800babc8.chapterIndex_98).deref(), 94, 24, TextColour.BROWN);

    final LodString v1;
    if(mainCallbackIndex_8004dd20.get() == 5) {
      v1 = submapNames_8011c108.get(submapIndex_800bd808.get()).deref();
    } else {
      v1 = worldMapNames_8011c1ec.get(continentIndex_800bf0b0.get()).deref();
    }

    renderCentredText(v1, 90, 38, TextColour.BROWN);

    renderCentredText(Status_8011ceb4, 62, getMenuOptionY(0) + 2, selectedOption == 0 ? TextColour.RED : textColour);
    renderCentredText(Armed_8011ced0, 62, getMenuOptionY(1) + 2, selectedOption == 1 ? TextColour.RED : textColour);
    renderCentredText(Addition_8011cedc, 62, getMenuOptionY(2) + 2, selectedOption == 2 ? TextColour.RED : textColour);
    renderCentredText(Replace_8011cef0, 62, getMenuOptionY(3) + 2, selectedOption == 3 ? TextColour.RED : textColour);
    renderCentredText(Config_8011cf00, 62, getMenuOptionY(4) + 2, selectedOption == 4 ? TextColour.RED : textColour);
    renderCentredText(Save_8011cf10, 62, getMenuOptionY(5) + 2, selectedOption == 5 ? TextColour.RED : saveColour);
  }

  private void renderItemSubmenu(final int selectedIndex, final TextColour textColour) {
    allocateOneFrameGlyph(150, 20, 60);
    renderCentredText(Use_it_8011cf1c, 142, this.getItemSubmenuOptionY(0), selectedIndex == 0 ? TextColour.RED : textColour);
    renderCentredText(List_8011cf3c, 142, this.getItemSubmenuOptionY(1), selectedIndex == 1 ? TextColour.RED : textColour);
    renderCentredText(Goods_8011cf48, 142, this.getItemSubmenuOptionY(2), selectedIndex == 2 ? TextColour.RED : textColour);
    renderCentredText(new LodString("Diiig"), 142, this.getItemSubmenuOptionY(3), selectedIndex == 3 ? TextColour.RED : textColour);
  }

  private void renderOptionsMenu(final boolean vibrateMode, final boolean soundMode, final int morphMode, final int noteMode) {
    textZ_800bdf00.set(31);

    renderCentredText(Vibrate_8011cf58, this.FUN_800fc7bc(0) - 15, this.menuOptionY(0), this.selectedConfigOption == 0 ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Off_8011cf6c, this.FUN_800fc7bc(1), this.menuOptionY(0), !vibrateMode ? TextColour.RED : TextColour.BROWN);
    renderCentredText(On_8011cf74, this.FUN_800fc7bc(2), this.menuOptionY(0), vibrateMode ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Sound_8011cf7c, this.FUN_800fc7bc(0) - 15, this.menuOptionY(1), this.selectedConfigOption == 1 ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Stereo_8011cf88, this.FUN_800fc7bc(1), this.menuOptionY(1), !soundMode ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Mono_8011cf98, this.FUN_800fc7bc(2), this.menuOptionY(1), soundMode ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Morph_8011cfa4, this.FUN_800fc7bc(0) - 15, this.menuOptionY(2), this.selectedConfigOption == 2 ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Normal_8011cfb0, this.FUN_800fc7bc(1), this.menuOptionY(2), morphMode == 0 ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Short_8011cfc0, this.FUN_800fc7bc(2), this.menuOptionY(2), morphMode == 1 ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Note_8011c814, this.FUN_800fc7bc(0) - 15, this.menuOptionY(3), this.selectedConfigOption == 3 ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Off_8011c838, this.FUN_800fc7d0(1), this.menuOptionY(3), noteMode == 0 ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Half_8011c82c, this.FUN_800fc7d0(2), this.menuOptionY(3), noteMode == 1 ? TextColour.RED : TextColour.BROWN);
    renderCentredText(Stay_8011c820, this.FUN_800fc7d0(3), this.menuOptionY(3), noteMode == 2 ? TextColour.RED : TextColour.BROWN);

    this.selectedConfigMenuOptionRenderable.x_40 = 100;
    this.selectedConfigMenuOptionRenderable.y_44 = this.menuConfigOptionY(this.selectedConfigOption);
    this.selectedConfigMenuOptionRenderable.z_3c = 31;

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

  private int menuConfigOptionY(final int slot) {
    return 105 + slot * 13;
  }

  private void FUN_80102484(final int a0) {
    allocateOneFrameGlyph(a0 != 0 ? 23 : 24, 112, getMenuOptionY(1) + 3);
  }

  private Renderable58 FUN_800fc900(final int option) {
    final Renderable58 renderable = allocateUiElement(116, 116, -200, this.getItemSubmenuOptionY(option) - 2);
    FUN_80104b60(renderable);
    return renderable;
  }

  private Renderable58 getRendererForHighlight() {
    final Renderable58 renderable = allocateUiElement(116, 116, -200, -2);
    FUN_80104b60(renderable);
    return renderable;
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    super.mouseMove(x, y);

    if(this.loadingStage == 2) {
      for(int i = 0; i < 6; i++) {
        if(this.selectedMenuOption != i && MathHelper.inBox(x, y, 22, getMenuOptionY(i) + 2, 84, 13)) {
          playSound(1);
          this.onLeftMenu = true;
          this.selectedMenuOption = i;
          this.selectedItemSubmenuOption = -1;
          this.selectedItemMenuOptionRenderable.x_40 = -200;
          this.selectedMenuOptionRenderable.y_44 = getMenuOptionY(i);
        }
      }

      for(int i = 0; i < 4; i++) {
        if(this.selectedItemSubmenuOption != i && MathHelper.inBox(x, y, 114, this.getItemSubmenuOptionY(i), 55, 13)) {
          playSound(1);
          this.onLeftMenu = false;
          this.selectedItemSubmenuOption = i;
          this.selectedItemMenuOptionRenderable.x_40 = 122;
          this.selectedItemMenuOptionRenderable.y_44 = this.getItemSubmenuOptionY(i) - 2;
        }
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    super.mouseClick(x, y, button, mods);

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
        gameState_800babc8.vibrationEnabled_4e1 = false;
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7bc(2) - 28, this.menuOptionY(0), 56, 13)) {
        playSound(2);
        gameState_800babc8.vibrationEnabled_4e1 = true;
        FUN_8002bcc8(0, 256);
        FUN_8002bda4(0, 0, 60);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7bc(1) - 28, this.menuOptionY(1), 56, 13)) {
        playSound(2);
        gameState_800babc8.mono_4e0 = false;
        setMono(false);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7bc(2) - 28, this.menuOptionY(1), 56, 13)) {
        playSound(2);
        gameState_800babc8.mono_4e0 = true;
        setMono(true);
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7bc(1) - 28, this.menuOptionY(2), 56, 13)) {
        playSound(2);
        gameState_800babc8.morphMode_4e2 = 0;
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7bc(2) - 28, this.menuOptionY(2), 56, 13)) {
        playSound(2);
        gameState_800babc8.morphMode_4e2 = 1;
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7d0(1) - 28, this.menuOptionY(3), 56, 13)) {
        playSound(2);
        gameState_800babc8.indicatorMode_4e8 = 0;
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7d0(2) - 28, this.menuOptionY(3), 56, 13)) {
        playSound(2);
        gameState_800babc8.indicatorMode_4e8 = 1;
      } else if(MathHelper.inBox(x, y, this.FUN_800fc7d0(3) - 28, this.menuOptionY(3), 56, 13)) {
        playSound(2);
        gameState_800babc8.indicatorMode_4e8 = 2;
      }
    }
  }

  private void menuEscape() {
    playSound(3);
    this.loadingStage = 100;
  }

  private void menuNavigateUp() {
    playSound(1);

    if(this.onLeftMenu) {
      this.selectedMenuOption = this.selectedMenuOption > 0 ? --this.selectedMenuOption : 5;
      this.selectedMenuOptionRenderable.y_44 = getMenuOptionY(this.selectedMenuOption);
      return;
    }

    this.selectedItemSubmenuOption = this.selectedItemSubmenuOption > 0 ? --this.selectedItemSubmenuOption : 3;
    this.selectedItemMenuOptionRenderable.y_44 = this.getItemSubmenuOptionY(this.selectedItemSubmenuOption) - 2;
  }

  private void menuNavigateDown() {
    playSound(1);

    if(this.onLeftMenu) {
      this.selectedMenuOption = this.selectedMenuOption < 5 ? ++this.selectedMenuOption : 0;
      this.selectedMenuOptionRenderable.y_44 = getMenuOptionY(this.selectedMenuOption);
      return;
    }

    this.selectedItemSubmenuOption = this.selectedItemSubmenuOption < 3 ? ++this.selectedItemSubmenuOption : 0;
    this.selectedItemMenuOptionRenderable.y_44 = this.getItemSubmenuOptionY(this.selectedItemSubmenuOption) - 2;
  }

  private void menuNavigateLeft() {
    if(!this.onLeftMenu) {
      this.onLeftMenu = true;
      this.selectedItemSubmenuOption = -1;
      this.selectedItemMenuOptionRenderable.x_40 = -200;
      playSound(1);
    }
  }

  private void menuNavigateRight() {
    if(this.onLeftMenu) {
      playSound(1);
      this.onLeftMenu = false;
      this.selectedItemSubmenuOption = 0;
      this.selectedItemMenuOptionRenderable.x_40 = 122;
      this.selectedItemMenuOptionRenderable.y_44 = this.getItemSubmenuOptionY(0) - 2;
    }
  }

  private void menuSelect() {
    if(this.onLeftMenu) {
      this.openScreen(this.selectedMenuOption, true);
    } else {
      this.openScreen(this.selectedItemSubmenuOption, false);
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

  @Override
  public void pressedThisFrame(final InputAction inputAction) {
    super.pressedThisFrame(inputAction);

    if(this.loadingStage == 2) {
      if(inputAction == InputAction.DPAD_LEFT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_LEFT) {
        this.menuNavigateLeft();
      }
      if(inputAction == InputAction.DPAD_RIGHT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_RIGHT) {
        this.menuNavigateRight();
      }
      if(inputAction == InputAction.BUTTON_EAST) {
        this.menuEscape();
      }
      if(inputAction == InputAction.BUTTON_SOUTH) {
        this.menuSelect();
      }
    } else if(this.loadingStage == 3) {
      if(inputAction == InputAction.BUTTON_EAST) {
        playSound(3);
        messageBox_8011dc90.state_0c++;
        this.loadingStage = 1;
      }
    }
  }

  @Override
  public void pressedWithRepeatPulse(final InputAction inputAction) {
    super.pressedWithRepeatPulse(inputAction);

    if(this.loadingStage == 2) {
      if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
        this.menuNavigateUp();
      }
      if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
        this.menuNavigateDown();
      }
    } else if(this.loadingStage == 3) {
      if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
        this.selectedConfigOption--;
        playSound(1);
        if(this.selectedConfigOption < 0) {
          this.selectedConfigOption = 0;
        }
      }
      if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
        this.selectedConfigOption++;
        playSound(1);
        if(this.selectedConfigOption > 3) {
          this.selectedConfigOption = 3;
        }
      }
    }
  }
}
