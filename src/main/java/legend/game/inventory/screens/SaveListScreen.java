package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;

import static legend.core.GameEngine.SAVES;
import static legend.game.SItem.FUN_801033cc;
import static legend.game.SItem.FUN_80103444;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.getSlotY;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.savedGamesGlyphs_80114258;
import static legend.game.SItem.saves;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public abstract class SaveListScreen extends MenuScreen {
  protected int loadingStage;

  private int selectedSlot;
  private int scroll;

  private double scrollAccumulator;

  private Renderable58 highlightLeftHalf;
  private Renderable58 highlightRightHalf;

  private final Runnable unload;

  protected SaveListScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  public void render() {
    switch(this.loadingStage) {
      case 0 -> {
        deallocateRenderables(0xff);
        scriptStartEffect(2, 10);

        saveListDownArrow_800bdb98 = null;
        saveListUpArrow_800bdb94 = null;
        this.scroll = 0;
        this.selectedSlot = 0;

        gameState_800babc8.isOnWorldMap_4e4 = mainCallbackIndex_8004dd20.get() == 8;

        saves.clear();
        saves.addAll(SAVES.loadAllSaves(gameState_800babc8.campaignName));

        this.highlightLeftHalf = allocateUiElement(129, 129, 16, getSlotY(this.selectedSlot));
        this.highlightRightHalf = allocateUiElement(130, 130, 192, getSlotY(this.selectedSlot));
        FUN_80104b60(this.highlightLeftHalf);
        FUN_80104b60(this.highlightRightHalf);
        this.renderSaveListArrows(this.scroll);

        deallocateRenderables(0);
        this.renderSavedGames(this.scroll, true);

        this.loadingStage++;
      }

      case 1 -> {
        this.renderSaveListArrows(this.scroll);
        this.renderSavedGames(this.scroll, false);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.scroll > 0) {
            this.scroll(this.scroll - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.scroll < this.menuCount() - 3) {
            this.scroll(this.scroll + 1);
          }
        }
      }

      case 2 -> {
        this.renderSavedGames(this.scroll, false);
        this.unload.run();
      }
    }
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 1) {
      return InputPropagation.PROPAGATE;
    }

    for(int i = 0; i < Math.min(3, this.menuCount() - this.scroll); i++) {
      final int slotWidth = 344;
      final int slotHeight = 64;
      final int slotX = 20;
      final int slotY = getSlotY(i);

      if(MathHelper.inBox(x, y, slotX, slotY, slotWidth, slotHeight)) {
        if(i != this.selectedSlot) {
          playSound(1);
          this.selectedSlot = i;
          this.highlightLeftHalf.y_44 = getSlotY(this.selectedSlot);
          this.highlightRightHalf.y_44 = getSlotY(this.selectedSlot);
        }

        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 1) {
      return InputPropagation.PROPAGATE;
    }

    for(int i = 0; i < Math.min(3, this.menuCount() - this.scroll); i++) {
      final int slotWidth = 344;
      final int slotHeight = 64;
      final int slotX = 20;
      final int slotY = getSlotY(i);

      if(MathHelper.inBox(x, y, slotX, slotY, slotWidth, slotHeight)) {
        this.selectedSlot = i;
        this.onSelect(this.scroll + i);
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  private void menuEscape() {
    playSound(3);
    this.loadingStage = 2;
  }

  private void menuNavigateUp() {
    playSound(1);

    if(this.selectedSlot > 0) {
      this.selectedSlot--;
    } else {
      this.scrollAccumulator++;
    }

    this.highlightLeftHalf.y_44 = getSlotY(this.selectedSlot);
    this.highlightRightHalf.y_44 = getSlotY(this.selectedSlot);
  }

  private void menuNavigateDown() {
    playSound(1);

    if(this.selectedSlot < 2) {
      this.selectedSlot++;
    } else {
      this.scrollAccumulator--;
    }

    this.highlightLeftHalf.y_44 = getSlotY(this.selectedSlot);
    this.highlightRightHalf.y_44 = getSlotY(this.selectedSlot);
  }

  private void menuSelect() {
    this.onSelect(this.scroll + this.selectedSlot);
  }

  @Override
  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    if(super.mouseScrollHighRes(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 1) {
      return InputPropagation.PROPAGATE;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
    return InputPropagation.HANDLED;
  }

  private void scroll(final int scroll) {
    playSound(1);
    this.scroll = scroll;
    this.highlightLeftHalf.y_44 = getSlotY(this.selectedSlot);
    this.highlightRightHalf.y_44 = getSlotY(this.selectedSlot);
    deallocateRenderables(0);
    this.renderSavedGames(this.scroll, true);
  }

  private void renderSavedGames(final int fileScroll, final boolean allocate) {
    if(allocate) {
      renderGlyphs(savedGamesGlyphs_80114258, 0, 0);
    }

    final int maxSaves = Math.min(3, this.menuCount() - fileScroll);
    for(int i = 0; i < maxSaves; i++) {
      this.renderSaveSlot(i, fileScroll + i, allocate);
    }
  }

  private void renderSaveListArrows(final int scroll) {
    FUN_80103444(saveListUpArrow_800bdb94, 194, 201, 202, 209);
    FUN_80103444(saveListDownArrow_800bdb98, 178, 185, 186, 193);

    if(scroll != 0) {
      if(saveListUpArrow_800bdb94 == null) {
        // Allocate up arrow
        final Renderable58 renderable = allocateUiElement(111, 108, 182, 16);
        renderable._18 = 194;
        renderable._1c = 201;
        saveListUpArrow_800bdb94 = renderable;
        FUN_801033cc(renderable);
      }
    } else if(saveListUpArrow_800bdb94 != null) {
      // Deallocate up arrow
      fadeOutArrow(saveListUpArrow_800bdb94);
      saveListUpArrow_800bdb94 = null;
    }

    if(scroll < this.menuCount() - 3 && (whichMenu_800bdc38 == WhichMenu.RENDER_SAVE_GAME_MENU_19 && saves.size() > 2 || saves.size() > 3)) {
      if(saveListDownArrow_800bdb98 == null) {
        // Allocate down arrow
        final Renderable58 renderable = allocateUiElement(111, 108, 182, 208);
        renderable._18 = 178;
        renderable._1c = 185;
        saveListDownArrow_800bdb98 = renderable;
        FUN_801033cc(renderable);
      }
    } else if(saveListDownArrow_800bdb98 != null) {
      // Deallocate down arrow
      fadeOutArrow(saveListDownArrow_800bdb98);
      saveListDownArrow_800bdb98 = null;
    }
  }

  protected abstract int menuCount();

  protected abstract void renderSaveSlot(final int slot, final int fileIndex, final boolean allocate);

  protected abstract void onSelect(final int slot);

  protected abstract void onOverwriteResult(final MessageBoxResult result);

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_SOUTH) {
      this.menuSelect();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
      this.menuNavigateUp();
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
      this.menuNavigateDown();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
