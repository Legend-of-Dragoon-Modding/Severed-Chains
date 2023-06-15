package legend.game.inventory.screens;

import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.CharacterCard;
import legend.game.inventory.screens.controls.DragoonSpirits;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.types.LodString;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

import static legend.core.GameEngine.CONFIG;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.canSave_8011dc88;
import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderCentredText;
import static legend.game.SItem.renderCharacter;
import static legend.game.SItem.submapNames_8011c108;
import static legend.game.SItem.worldMapNames_8011c1ec;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getTimestampPart;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.submapIndex_800bd808;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MainMenuScreen extends MenuScreen {
  private int loadingStage;
  private final Runnable unload;

  private final CharacterCard[] charCards = new CharacterCard[3];
  private final List<Button> menuButtons = new ArrayList<>();

  public MainMenuScreen(final Runnable unload) {
    this.unload = unload;

    this.addControl(new Background());
    this.addControl(Glyph.glyph(71)).setPos( 16,  16); // Chapter box
    this.addControl(Glyph.glyph(72)).setPos( 18,  60); // Menu box
    this.addControl(Glyph.glyph(73)).setPos( 19, 175); // Bottom box
    this.addControl(Glyph.glyph(75)).setPos(194,  83); // Line between char 0 and 1
    this.addControl(Glyph.glyph(75)).setPos(194, 155); // Line between char 1 and 2

    this.addControl(new DragoonSpirits(gameState_800babc8.goods_19c[0])).setPos(40, 197); // Dragoon spirits

    this.addButton("Use Item", this::showUseItemScreen);
    this.addButton("Equipment", this::showEquipmentScreen);
    this.addButton("Inventory", this::showItemListScreen);
    this.addButton("Goods", this::showGoodsScreen);
    this.addButton("Diiig", this::showDabasScreen);
    this.addButton("Status", this::showStatusScreen);
    this.addButton("Addition", this::showAdditionsScreen);
    this.addButton("Replace", this::showCharSwapScreen);
    this.addButton("Options", this::showOptionsScreen);
    this.addButton("Save", this::showSaveScreen).setDisabled(!canSave_8011dc88.get());

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

    button.onLostFocus(() -> button.setTextColour(TextColour.BROWN));
    button.onGotFocus(() -> button.setTextColour(TextColour.RED));

    button.onMouseClick((x, y, button1, mods) -> {
      if(button1 == GLFW_MOUSE_BUTTON_LEFT && mods == 0) {
        onClick.run();
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });

    button.onPressedWithRepeatPulse(inputAction -> {
      switch(inputAction) {
        case DPAD_DOWN, JOYSTICK_LEFT_BUTTON_DOWN -> {
          for(int i = 1; i < this.menuButtons.size(); i++) {
            final Button otherButton = this.menuButtons.get(Math.floorMod(index + i, this.menuButtons.size()));

            if(!otherButton.isDisabled()) {
              this.setFocus(otherButton);
              break;
            }
          }
        }
        case DPAD_UP, JOYSTICK_LEFT_BUTTON_UP -> {
          for(int i = 1; i < this.menuButtons.size(); i++) {
            final Button otherButton = this.menuButtons.get(Math.floorMod(index - i, this.menuButtons.size()));

            if(!otherButton.isDisabled()) {
              this.setFocus(otherButton);
              break;
            }
          }
        }
        case DPAD_RIGHT, JOYSTICK_LEFT_BUTTON_RIGHT -> {
          final Button otherButton = this.menuButtons.get(Math.floorMod(index + 5, this.menuButtons.size()));

          if(!otherButton.isDisabled()) {
            this.setFocus(otherButton);
          }
        }
        case DPAD_LEFT, JOYSTICK_LEFT_BUTTON_LEFT -> {
          final Button otherButton = this.menuButtons.get(Math.floorMod(index - 5, this.menuButtons.size()));

          if(!otherButton.isDisabled()) {
            this.setFocus(otherButton);
          }
        }
        case BUTTON_SOUTH -> onClick.run();
      }

      return InputPropagation.HANDLED;
    });

    this.menuButtons.add(button);
    return button;
  }

  private void addCharCard(final int slot) {
    final int id = gameState_800babc8.charIds_88[slot];
    this.charCards[slot] = this.addControl(new CharacterCard(id));
    this.charCards[slot].setPos(186, 16 + slot * 72);
  }

  @Override
  public void setFocus(@Nullable final Control control) {
    // Don't allow complete unfocusing
    if(control instanceof Button) {
      super.setFocus(control);
    }
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        cacheCharacterSlots();
        scriptStartEffect(2, 10);

        for(int i = 0; i < 3; i++) {
          this.charCards[i].setCharId(gameState_800babc8.charIds_88[i]);
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
    this.showScreen(StatusScreen::new);
  }

  private void showEquipmentScreen() {
    this.showScreen(EquipmentScreen::new);
  }

  private void showAdditionsScreen() {
    this.showScreen(AdditionsScreen::new);
  }

  private void showCharSwapScreen() {
    this.showScreen(CharSwapScreen::new);
  }

  private void showOptionsScreen() {
    playSound(2);
    menuStack.pushScreen(new OptionsScreen(CONFIG, EnumSet.allOf(ConfigStorageLocation.class), () -> {
      ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));
      ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.CAMPAIGN, Path.of("saves", gameState_800babc8.campaignName, "campaign_config.dcnf"));
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
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
    this.showScreen(UseItemScreen::new);
  }

  private void showItemListScreen() {
    this.showScreen(ItemListScreen::new);
  }

  private void showGoodsScreen() {
    this.showScreen(GoodsScreen::new);
  }

  private void showDabasScreen() {
    this.showScreen(DabasScreen::new);
  }

  private void showScreen(final Function<Runnable, MenuScreen> screen) {
    playSound(2);
    menuStack.pushScreen(screen.apply(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      if(inputAction == InputAction.BUTTON_EAST) {
        this.menuEscape();
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }
}
