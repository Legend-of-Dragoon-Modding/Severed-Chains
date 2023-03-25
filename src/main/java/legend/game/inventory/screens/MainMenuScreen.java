package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.CharacterCard;
import legend.game.inventory.screens.controls.DragoonSpirits;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.types.LodString;
import legend.game.types.Renderable58;

import java.util.ArrayList;
import java.util.List;

import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.Half_8011c82c;
import static legend.game.SItem.Mono_8011cf98;
import static legend.game.SItem.Morph_8011cfa4;
import static legend.game.SItem.Normal_8011cfb0;
import static legend.game.SItem.Note_8011c814;
import static legend.game.SItem.Off_8011c838;
import static legend.game.SItem.Off_8011cf6c;
import static legend.game.SItem.On_8011cf74;
import static legend.game.SItem.Short_8011cfc0;
import static legend.game.SItem.Sound_8011cf7c;
import static legend.game.SItem.Stay_8011c820;
import static legend.game.SItem.Stereo_8011cf88;
import static legend.game.SItem.Vibrate_8011cf58;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.canSave_8011dc88;
import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox;
import static legend.game.SItem.messageBox_8011dc90;
import static legend.game.SItem.renderCentredText;
import static legend.game.SItem.renderCharacter;
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
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MainMenuScreen extends MenuScreen {
  private int loadingStage;
  private final Runnable unload;

  private final List<Button> menuButtons = new ArrayList<>();

  private int selectedConfigOption;

  private Renderable58 selectedConfigMenuOptionRenderable;

  public MainMenuScreen(final Runnable unload) {
    this.unload = unload;

    this.addControl(new Background());
    this.addControl(Glyph.glyph(71)).setPos( 16,  16); // Chapter box
    this.addControl(Glyph.glyph(72)).setPos( 18,  60); // Menu box
    this.addControl(Glyph.glyph(73)).setPos( 19, 175); // Bottom box
    this.addControl(Glyph.glyph(75)).setPos(194,  83); // Line between char 0 and 1
    this.addControl(Glyph.glyph(75)).setPos(194, 155); // Line between char 1 and 2

    this.addControl(new DragoonSpirits(gameState_800babc8.goods_19c[0])).setPos(40, 197); // Dragoon spirits

    this.addButton("Status", this::showStatusScreen);
    this.addButton("Addition", this::showAdditionsScreen);
    this.addButton("Replace", this::showCharSwapScreen);
    this.addButton("Config", this::showOptionsScreen);
    this.addButton("Save", this::showSaveScreen).setDisabled(!canSave_8011dc88.get());
    this.addButton("Use Item", this::showUseItemScreen);
    this.addButton("Equipment", this::showEquipmentScreen);
    this.addButton("Inventory", this::showItemListScreen);
    this.addButton("Goods", this::showGoodsScreen);
    this.addButton("Diiig", this::showDabasScreen);

    for(int i = 0; i < 3; i++) {
      this.addCharCard(i);
    }

    this.setFocus(this.menuButtons.get(0));
  }

  private Button addButton(final String text, final Runnable onClick) {
    final int index = this.menuButtons.size();

    final Button button = this.addControl(new Button(text));
    button.setPos(30 + index / 5 * 65, 93 + (index % 5) * 13);

    button.onHoverIn(() -> this.setFocus(button));

    button.onGotFocus(() -> {
      this.menuButtons.forEach(b -> b.setTextColour(TextColour.BROWN));
      button.setTextColour(TextColour.RED);
    });

    button.onMouseClick((x, y, button1, mods) -> {
      if(button1 == GLFW_MOUSE_BUTTON_LEFT && mods == 0) {
        onClick.run();
      }
    });

    button.onPressedWithRepeatPulse(inputAction -> {
      switch(inputAction) {
        case DPAD_DOWN, JOYSTICK_LEFT_BUTTON_DOWN -> this.setFocus(this.menuButtons.get(Math.floorMod(index + 1, this.menuButtons.size())));
        case DPAD_UP, JOYSTICK_LEFT_BUTTON_UP -> this.setFocus(this.menuButtons.get(Math.floorMod(index - 1, this.menuButtons.size())));
        case DPAD_RIGHT, JOYSTICK_RIGHT_BUTTON_RIGHT -> this.setFocus(this.menuButtons.get(Math.floorMod(index + 5, this.menuButtons.size())));
        case DPAD_LEFT, JOYSTICK_RIGHT_BUTTON_LEFT -> this.setFocus(this.menuButtons.get(Math.floorMod(index - 5, this.menuButtons.size())));
        case BUTTON_SOUTH -> onClick.run();
      }
    });

    this.menuButtons.add(button);
    return button;
  }

  private void addCharCard(final int slot) {
    final int id = gameState_800babc8.charIds_88[slot];

    if(id != -1) {
      this.addControl(new CharacterCard(id)).setPos(186, 16 + slot * 72);
    }
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        cacheCharacterSlots();
        scriptStartEffect(2, 10);
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);

        this.selectedConfigMenuOptionRenderable = this.getRendererForHighlight();
        this.renderInventoryMenu(0xff);
        this.loadingStage++;
      }

      case 2 -> this.renderInventoryMenu(0);

      case 3 -> {
        messageBox(messageBox_8011dc90);
        if(messageBox_8011dc90.ticks_10 >= 2) {
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

        this.renderInventoryMenu(0);
      }

      // Fade out
      case 100 -> {
        this.renderInventoryMenu(0);
        scriptStartEffect(1, 10);
        this.loadingStage++;
      }

      // Unload
      case 101 -> {
        this.renderInventoryMenu(0);

        if(_800bb168.get() >= 0xff) {
          this.unload.run();
        }
      }
    }
  }

  private void renderInventoryMenu(final long a2) {
    final boolean allocate = a2 == 0xff;
    if(allocate) {
      renderCharacter(146, 184, 10);
      renderCharacter(164, 184, 10);
    }

    this.renderNumber( 67, 184, gameState_800babc8.gold_94, 8); // Gold
    this.renderNumber(166, 204, gameState_800babc8.stardust_9c, 2); // Stardust
    this.renderNumber(128, 184, getTimestampPart(gameState_800babc8.timestamp_a0, 0), 3);
    this.renderNumber(152, 184, getTimestampPart(gameState_800babc8.timestamp_a0, 1), 2, 0x1);
    this.renderNumber(170, 184, getTimestampPart(gameState_800babc8.timestamp_a0, 2), 2, 0x1);
    renderCentredText(chapterNames_80114248.get(gameState_800babc8.chapterIndex_98).deref(), 94, 24, TextColour.BROWN);

    final LodString name;
    if(mainCallbackIndex_8004dd20.get() == 5) {
      name = submapNames_8011c108.get(submapIndex_800bd808.get()).deref();
    } else {
      name = worldMapNames_8011c1ec.get(continentIndex_800bf0b0.get()).deref();
    }

    renderCentredText(name, 90, 38, TextColour.BROWN);
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

  private Renderable58 getRendererForHighlight() {
    final Renderable58 renderable = allocateUiElement(116, 116, -200, -2);
    FUN_80104b60(renderable);
    return renderable;
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    super.mouseClick(x, y, button, mods);

    if(this.loadingStage == 3) {
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

  private void showStatusScreen() {
    playSound(2);
    menuStack.pushScreen(new StatusScreen(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  private void showEquipmentScreen() {
    playSound(2);
    menuStack.pushScreen(new EquipmentScreen(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  private void showAdditionsScreen() {
    playSound(2);
    menuStack.pushScreen(new AdditionsScreen(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  private void showCharSwapScreen() {
    playSound(2);
    menuStack.pushScreen(new CharSwapScreen(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  private void showOptionsScreen() {
    playSound(4);
    setMessageBoxText(messageBox_8011dc90, null, 0x1);
    this.loadingStage = 3;
  }

  private void showSaveScreen() {
    if(canSave_8011dc88.get()) {
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

  private void showUseItemScreen() {
    playSound(2);
    menuStack.pushScreen(new UseItemScreen(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  private void showItemListScreen() {
    playSound(2);
    menuStack.pushScreen(new ItemListScreen(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  private void showGoodsScreen() {
    playSound(2);
    menuStack.pushScreen(new GoodsScreen(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  private void showDabasScreen() {
    playSound(2);
    menuStack.pushScreen(new DabasScreen(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  @Override
  public void pressedThisFrame(final InputAction inputAction) {
    super.pressedThisFrame(inputAction);

    if(this.loadingStage == 2) {
      if(inputAction == InputAction.BUTTON_EAST) {
        this.menuEscape();
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

    if(this.loadingStage == 3) {
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
