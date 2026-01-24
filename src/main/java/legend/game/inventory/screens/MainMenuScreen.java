package legend.game.inventory.screens;

import legend.core.platform.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.CharacterCard;
import legend.game.inventory.screens.controls.DragoonSpirits;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.types.MessageBoxResult;
import legend.game.types.Translucency;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SAVES;
import static legend.game.sound.Audio.playMenuSound;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.FullScreenEffects.fullScreenEffect_800bb140;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.Menus.downArrow_800bdb98;
import static legend.game.Menus.renderablePtr_800bdba4;
import static legend.game.Menus.renderablePtr_800bdba8;
import static legend.game.Menus.upArrow_800bdb94;
import static legend.game.Menus.whichMenu_800bdc38;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.getTimestampPart;
import static legend.game.SItem.initHighlight;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderCharacter;
import static legend.game.Scus94491BpeSegment_800b._800bd7ac;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class MainMenuScreen extends MenuScreen {
  private int loadingStage;
  private final Runnable unload;

  private final CharacterCard[] charCards = new CharacterCard[3];
  private final List<Button> menuButtons = new ArrayList<>();

  private final Button saveButton;
  private final Button loadButton;

  private Button lastSelectedButton;
  private final Glyph charHighlight;
  private int charScroll;
  private int charIndex;
  private double charScrollAccumulator;

  private final Glyph upArrow;
  private final Glyph downArrow;

  public MainMenuScreen(final Runnable unload) {
    this.unload = unload;

    loadingNewGameState_800bdc34 = false;
    loadCharacterStats();

    this.addControl(new Background());
    this.addControl(Glyph.glyph(71)).setPos( 16,  16); // Chapter box
    this.addControl(Glyph.glyph(72)).setPos( 18,  60); // Menu box
    this.addControl(Glyph.glyph(73)).setPos( 19, 175); // Bottom box
    this.addControl(Glyph.glyph(75)).setPos(194,  83); // Line between char 0 and 1
    this.addControl(Glyph.glyph(75)).setPos(194, 155); // Line between char 1 and 2

    this.addControl(new DragoonSpirits(gameState_800babc8.goods_19c)).setPos(40, 197); // Dragoon spirits

    this.addButton("Use Item", this::showUseItemScreen);
    this.addButton("Equipment", this::showEquipmentScreen);
    this.addButton("Inventory", this::showItemListScreen);
    this.addButton("Goods", this::showGoodsScreen);
    this.addButton("Diiig", this::showDabasScreen);
    this.addButton("", () -> { }).hide();
    this.addButton("Quit", () -> menuStack.pushScreen(new MessageBoxScreen("Quit to main menu?", 2, result -> {
      if(result == MessageBoxResult.YES) {
        this.menuEscape();
        whichMenu_800bdc38 = WhichMenu.QUIT;
      }
    })));
    this.addButton("Status", this::showStatusScreen);
    this.addButton("Addition", this::showAdditionsScreen);
    this.addButton("Replace", this::showCharSwapScreen);
    this.addButton("Options", this::showOptionsScreen);
    this.addButton("", () -> { }).hide();
    this.loadButton = this.addButton("Load", this::showLoadScreen);
    this.saveButton = this.addButton("Save", this::showSaveScreen);

    this.loadButton.setDisabled(gameState_800babc8.campaign.loadAllSaves().isEmpty());
    this.saveButton.setDisabled(!currentEngineState_8004dd04.canSave());

    for(int i = 0; i < 3; i++) {
      this.addCharCard(i);
    }

    this.charHighlight = this.addControl(Glyph.uiElement(0x7f, 0x7f));
    initHighlight(this.charHighlight.getRenderable());
    this.charHighlight.hide();

    this.upArrow = this.addControl(Glyph.blueSpinnerUp());
    this.upArrow.setVisibility(false);
    this.upArrow.setPos(this.charCards[0].getX() + this.charCards[0].getWidth() - 1, this.charCards[0].getY());
    this.downArrow = this.addControl(Glyph.blueSpinnerDown());
    this.downArrow.setVisibility(gameState_800babc8.charIds_88.size() > this.charCards.length);
    this.downArrow.setPos(this.charCards[this.charCards.length - 1].getX() + this.charCards[this.charCards.length - 1].getWidth() - 1, this.charCards[this.charCards.length - 1].getY() + this.charCards[this.charCards.length - 1].getHeight() - 18);

    this.setFocus(this.menuButtons.getFirst());
  }

  private Button addButton(final String text, final Runnable onClick) {
    final int index = this.menuButtons.size();

    final Button button = this.addControl(new Button(text));
    button.setPos(21 + index / 7 * 74, 79 + (index % 7) * 13);
    button.setWidth(72);

    button.onHoverIn(() -> {
      playMenuSound(1);
      button.focus();
    });

    button.onLostFocus(() -> button.setTextColour(TextColour.BROWN));
    button.onGotFocus(() -> {
      button.setTextColour(TextColour.RED);
      this.lastSelectedButton = button;
    });

    button.onPressed(onClick::run);

    button.onInputActionPressed((action, repeat) -> {
      if(action == INPUT_ACTION_MENU_DOWN.get()) {
        for(int i = 1; i < this.menuButtons.size(); i++) {
          final Button otherButton = this.menuButtons.get(Math.floorMod(index + i, this.menuButtons.size()));

          if(!otherButton.isDisabled() && otherButton.isVisible()) {
            playMenuSound(1);
            otherButton.focus();
            break;
          }
        }

        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_UP.get()) {
        for(int i = 1; i < this.menuButtons.size(); i++) {
          final Button otherButton = this.menuButtons.get(Math.floorMod(index - i, this.menuButtons.size()));

          if(!otherButton.isDisabled() && otherButton.isVisible()) {
            playMenuSound(1);
            otherButton.focus();
            break;
          }
        }

        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_RIGHT.get()) {
        int newIndex = index;

        for(int i = 0; i < 3; i++) {
          if(newIndex >= this.menuButtons.size() / 2) {
            newIndex += 1;
          }

          if(newIndex < this.menuButtons.size()) {
            newIndex += this.menuButtons.size() / 2;
          }

          newIndex = Math.floorMod(newIndex, this.menuButtons.size());

          final Button otherButton = this.menuButtons.get(newIndex);

          // Pressing right on the right column, switch to char cards
          if(otherButton.getX() < button.getX()) {
            this.charCards[this.charIndex].hoverIn();
            return InputPropagation.HANDLED;
          }

          if(!otherButton.isDisabled() && otherButton.isVisible()) {
            playMenuSound(1);
            otherButton.focus();
            return InputPropagation.HANDLED;
          }
        }

        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_LEFT.get()) {
        int newIndex = index;

        for(int i = 0; i < 3; i++) {
          if(newIndex < this.menuButtons.size() / 2) {
            newIndex -= 1;
          }

          if(newIndex >= 0) {
            newIndex -= this.menuButtons.size() / 2;
          }

          newIndex = Math.floorMod(newIndex, this.menuButtons.size());

          final Button otherButton = this.menuButtons.get(newIndex);

          // Pressing left on the left column, switch to char cards
          if(otherButton.getX() > button.getX()) {
            this.charCards[this.charIndex].hoverIn();
            return InputPropagation.HANDLED;
          }

          if(!otherButton.isDisabled() && otherButton.isVisible()) {
            playMenuSound(1);
            otherButton.focus();
            return InputPropagation.HANDLED;
          }
        }

        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });

    this.menuButtons.add(button);
    return button;
  }

  private void addCharCard(final int slot) {
    final int id = slot < gameState_800babc8.charIds_88.size() ? gameState_800babc8.charIds_88.getInt(slot) : -1;
    this.charCards[slot] = this.addControl(new CharacterCard(id));
    this.charCards[slot].setPos(186, 16 + slot * 72);

    this.charCards[slot].onHoverIn(() -> {
      if(this.charCards[slot].getCharId() != -1) {
        this.charCards[slot].focus();
      }
    });

    this.charCards[slot].onGotFocus(() -> {
      playMenuSound(1);
      this.charHighlight.setPos(this.charCards[slot].getX() + 8, this.charCards[slot].getY());
      this.charHighlight.setVisibility(true);
      this.charIndex = slot;
    });

    this.charCards[slot].onLostFocus(() -> this.charHighlight.setVisibility(false));

    this.charCards[slot].onInputActionPressed((action, repeat) -> {
      if(action == INPUT_ACTION_MENU_UP.get()) {
        if(this.charIndex == 0) {
          if(this.charScroll > 0) {
            playMenuSound(1);
            this.scroll(this.charScroll - 1);
          } else if(!repeat) {
            this.charIndex = Math.min(this.charCards.length, gameState_800babc8.charIds_88.size()) - 1;
            this.scroll(gameState_800babc8.charIds_88.size() - this.charCards.length);
            this.charCards[this.charIndex].focus();
          }
        } else if(this.charIndex > 0) {
          this.charIndex--;
          this.charCards[this.charIndex].focus();
        }

        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_DOWN.get()) {
        if(this.charIndex == Math.min(this.charCards.length, gameState_800babc8.charIds_88.size()) - 1) {
          if(this.charScroll < gameState_800babc8.charIds_88.size() - this.charCards.length) {
            playMenuSound(1);
            this.scroll(this.charScroll + 1);
          } else if(!repeat) {
            this.charIndex = 0;
            this.scroll(0);
            this.charCards[this.charIndex].focus();
          }
        } else if(this.charIndex < Math.min(this.charCards.length, gameState_800babc8.charIds_88.size() - this.charScroll) - 1) {
          this.charIndex++;
          this.charCards[this.charIndex].focus();
        }
      }

      if(action == INPUT_ACTION_MENU_LEFT.get() || action == INPUT_ACTION_MENU_RIGHT.get()) {
        playMenuSound(1);
        this.lastSelectedButton.focus();
      }

      if(action == INPUT_ACTION_MENU_CONFIRM.get()) {
        this.showStatusScreenForSelectedCharacter();
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });

    this.charCards[slot].onMouseClick((x, y, button, mods) -> {
      if(this.charCards[slot].getCharId() != -1) {
        this.showStatusScreenForSelectedCharacter();
      }

      return InputPropagation.HANDLED;
    });

    this.charCards[slot].onMouseScrollHighRes((deltaX, deltaY) -> {
      if(this.loadingStage != 2) {
        return InputPropagation.PROPAGATE;
      }

      if(this.charScrollAccumulator < 0 && deltaY > 0 || this.charScrollAccumulator > 0 && deltaY < 0) {
        this.charScrollAccumulator = 0;
      }

      this.charScrollAccumulator += deltaY;
      return InputPropagation.HANDLED;
    });
  }

  private void showStatusScreenForSelectedCharacter() {
    for(int i = 0; i < characterIndices_800bdbb8.length; i++) {
      if(characterIndices_800bdbb8[i] == this.charCards[this.charIndex].getCharId()) {
        playMenuSound(2);
        this.showStatusScreen(i);
        return;
      }
    }

    // Couldn't find char for some reason
    playMenuSound(40);
  }

  private void scroll(final int scroll) {
    this.charScroll = Math.max(0, scroll);

    for(int slot = 0; slot < this.charCards.length; slot++) {
      final int charId = slot < gameState_800babc8.charIds_88.size() ? gameState_800babc8.charIds_88.getInt(this.charScroll + slot) : -1;
      this.charCards[slot].setCharId(charId);

      if(charId != -1) {
        this.charCards[slot].acceptInput();
      } else {
        this.charCards[slot].ignoreInput();
      }
    }

    this.upArrow.setVisibility(this.charScroll > 0);
    this.downArrow.setVisibility(scroll < gameState_800babc8.charIds_88.size() - this.charCards.length);
  }

  @Override
  public void setFocus(@Nullable final Control control) {
    // Don't allow complete unfocusing
    if(control instanceof Button || control instanceof CharacterCard) {
      super.setFocus(control);
    }
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        cacheCharacterSlots();
        startFadeEffect(2, 10);

        for(int i = 0; i < this.charCards.length; i++) {
          if(this.charScroll + i < gameState_800babc8.charIds_88.size()) {
            this.charCards[i].setCharId(gameState_800babc8.charIds_88.getInt(this.charScroll + i));
          } else {
            this.charCards[i].setCharId(-1);
          }
        }

        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);

        this.renderInventoryMenu(0xff);
        this.loadingStage++;
      }

      case 2 -> this.renderInventoryMenu(0);

      // Fade out
      case 100 -> {
        this.renderInventoryMenu(0);
        startFadeEffect(1, 10);
        this.loadingStage++;
      }

      // Unload
      case 101 -> {
        this.renderInventoryMenu(0);

        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          this.unload.run();
        }
      }
    }
  }

  private void renderInventoryMenu(final long a2) {
    if(this.charScrollAccumulator >= 1.0d) {
      this.charScrollAccumulator -= 1.0d;

      if(this.charScroll > 0) {
        playMenuSound(1);
        this.scroll(this.charScroll - 1);
      }
    }

    if(this.charScrollAccumulator <= -1.0d) {
      this.charScrollAccumulator += 1.0d;

      if(this.charScroll < gameState_800babc8.charIds_88.size() - this.charCards.length) {
        playMenuSound(1);
        this.scroll(this.charScroll + 1);
      }
    }

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
    renderText(chapterNames_80114248[gameState_800babc8.chapterIndex_98], 94, 24, UI_TEXT_CENTERED);

    // The retail lines between the buttons are too short, so we just draw more line where the texture ends
    final Matrix4f transforms = new Matrix4f();
    for(int i = 0; i < 6; i++) {
      final int x = 106;
      final int y = 93 + i * 13;
      RENDERER.queueLine(transforms, textZ_800bdf00 * 4.0f + 5, new Vector2f(x, y), new Vector2f(x + 59, y))
        .translucency(Translucency.B_MINUS_F)
        .colour(2.0f / 256, 20.0f / 256, 12.0f / 256)
      ;
    }

    renderText(currentEngineState_8004dd04.getLocation(gameState_800babc8), 90, 38, UI_TEXT_CENTERED);
  }

  private void menuEscape() {
    playMenuSound(3);
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
    if(upArrow_800bdb94 != null) {
      fadeOutArrow(upArrow_800bdb94);
      upArrow_800bdb94 = null;
    }

    //LAB_800fca80
    if(downArrow_800bdb98 != null) {
      fadeOutArrow(downArrow_800bdb98);
      downArrow_800bdb98 = null;
    }
  }

  private StatusScreen showStatusScreen() {
    return this.showScreen(StatusScreen::new);
  }

  private StatusScreen showStatusScreen(final int charSlot) {
    return this.showScreen(unload -> new StatusScreen(charSlot, unload));
  }

  private EquipmentScreen showEquipmentScreen() {
    return this.showScreen(EquipmentScreen::new);
  }

  private AdditionsScreen showAdditionsScreen() {
    return this.showScreen(AdditionsScreen::new);
  }

  private CharSwapScreen showCharSwapScreen() {
    return this.showScreen(CharSwapScreen::new);
  }

  private OptionsCategoryScreen showOptionsScreen() {
    return menuStack.pushScreen(new OptionsCategoryScreen(CONFIG, EnumSet.allOf(ConfigStorageLocation.class), () -> {
      ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));
      ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.CAMPAIGN, gameState_800babc8.campaign.path.resolve("campaign_config.dcnf"));
      menuStack.popScreen();
      this.loadingStage = 0;

      this.saveButton.setDisabled(!currentEngineState_8004dd04.canSave());
    }));
  }

  private LoadGameScreen showLoadScreen() {
    startFadeEffect(2, 10);

    return menuStack.pushScreen(new LoadGameScreen(gameState_800babc8.campaign.loadAllSaves(), save -> {
      _800bd7ac = true;
      SAVES.loadGameState(save, false);
      currentEngineState_8004dd04.loadSaveFromMenu(save);
    }, () -> {
      startFadeEffect(2, 5);
      menuStack.popScreen();
      this.fadeOutArrows();
      this.loadingStage = 0;
    }, gameState_800babc8.campaign));
  }

  private void showSaveScreen() {
    if(currentEngineState_8004dd04.canSave()) {
      menuStack.pushScreen(new SaveGameScreen(() -> {
        menuStack.popScreen();
        this.fadeOutArrows();
        this.loadingStage = 0;
        this.loadButton.setDisabled(gameState_800babc8.campaign.loadAllSaves().isEmpty());
      }));
    } else {
      playMenuSound(40);
    }
  }

  private UseItemScreen showUseItemScreen() {
    return this.showScreen(UseItemScreen::new);
  }

  private ItemListScreen showItemListScreen() {
    return this.showScreen(ItemListScreen::new);
  }

  private GoodsScreen showGoodsScreen() {
    return this.showScreen(GoodsScreen::new);
  }

  private DabasScreen showDabasScreen() {
    return this.showScreen(DabasScreen::new);
  }

  private <T extends MenuScreen> T showScreen(final Function<Runnable, T> screen) {
    return menuStack.pushScreen(screen.apply(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
        this.menuEscape();
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }
}
