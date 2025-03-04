package legend.game.inventory.screens;

import legend.core.platform.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.BigList;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.inventory.screens.controls.SaveCard;
import legend.game.saves.SaveFailedException;
import legend.game.saves.SavedGame;
import legend.game.types.MessageBoxResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

import static legend.core.GameEngine.SAVES;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCutForSave_800cb450;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DELETE;

public class SaveGameScreen extends MenuScreen {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SaveGameScreen.class);

  private static final String Overwrite_save_8011c9e8 = "Overwrite save?";

  private final BigList<SavedGame> saveList;
  private final List<SavedGame> saves;

  private final Runnable unload;

  public SaveGameScreen(final Runnable unload) {
    this.unload = unload;

    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.addControl(new Background());

    // Bottom line
    this.addControl(Glyph.glyph(78)).setPos(26, 155);
    this.addControl(Glyph.glyph(79)).setPos(192, 155);

    final SaveCard saveCard = this.addControl(new SaveCard());
    saveCard.setPos(16, 160);

    this.saveList = this.addControl(new BigList<>(savedGame -> savedGame != null ? savedGame.saveName : "<new save>"));
    this.saveList.setPos(16, 16);
    this.saveList.setSize(360, 144);
    this.saveList.onHighlight(saveCard::setSaveData);
    this.saveList.onSelection(this::onSelection);
    this.setFocus(this.saveList);

    this.saveList.addEntry(null);

    this.saves = gameState_800babc8.campaign.loadAllSaves();
    for(final SavedGame save : this.saves) {
      this.saveList.addEntry(save);
    }
  }

  @Override
  public void setFocus(@Nullable final Control control) {
    super.setFocus(this.saveList);
  }

  @Override
  protected void render() {
    renderText("Save Game", 188, 10, UI_TEXT_CENTERED);
    renderText("\u011f Delete", 297, 226, UI_TEXT);
  }

  private void onSelection(@Nullable final SavedGame save) {
    playMenuSound(2);

    if(save == null) {
      menuStack.pushScreen(new InputBoxScreen("Save name:", SAVES.generateSaveName(this.saves, gameState_800babc8), this::onNewSaveResult));
    } else {
      menuStack.pushScreen(new MessageBoxScreen(Overwrite_save_8011c9e8, 2, result -> this.onOverwriteResult(result, save)));
    }
  }

  private void onNewSaveResult(final MessageBoxResult result, final String name) {
    if(result == MessageBoxResult.YES) {
      if(gameState_800babc8.campaign.saveExists(name)) {
        menuStack.pushScreen(new MessageBoxScreen("Save name already\nin use", 0, result1 -> { }));
        return;
      }

      gameState_800babc8.submapScene_a4 = collidedPrimitiveIndex_80052c38;
      gameState_800babc8.submapCut_a8 = submapCutForSave_800cb450;

      try {
        SAVES.newSave(name, gameState_800babc8, stats_800be5f8);
        this.unload.run();
      } catch(final SaveFailedException e) {
        menuStack.pushScreen(new MessageBoxScreen("Failed to save game", 0, r -> { }));
        LOGGER.error("Failed to save game", e);
      }
    }
  }

  private void onOverwriteResult(final MessageBoxResult result, final SavedGame save) {
    if(result == MessageBoxResult.YES) {
      gameState_800babc8.submapScene_a4 = collidedPrimitiveIndex_80052c38;
      gameState_800babc8.submapCut_a8 = submapCutForSave_800cb450;

      try {
        SAVES.overwriteSave(save.fileName, save.saveName, gameState_800babc8, stats_800be5f8);
        this.unload.run();
      } catch(final SaveFailedException e) {
        menuStack.pushScreen(new MessageBoxScreen("Failed to save game", 0, r -> { }));
        LOGGER.error("Failed to save game", e);
      }
    }
  }

  private void menuDelete() {
    playMenuSound(40);

    if(this.saveList.size() == 1) {
      menuStack.pushScreen(new MessageBoxScreen("Can't delete last save", 0, result -> {}));
      return;
    }

    if(this.saveList.getSelected() != null) {
      menuStack.pushScreen(new MessageBoxScreen("Are you sure you want to\ndelete this save?", 2, result -> {
        if(result == MessageBoxResult.YES) {
          try {
            this.saveList.getSelected().state.campaign.deleteSave(this.saveList.getSelected().fileName);
            this.saves.removeIf(save -> save.fileName.equals(this.saveList.getSelected().fileName));
            this.saveList.removeEntry(this.saveList.getSelected());
          } catch(final IOException e) {
            LOGGER.error("Failed to delete save", e);
            this.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen("Failed to delete save", 0, result1 -> {})));
          }
        }
      }));
    }
  }

  private void menuEscape() {
    playMenuSound(3);
    this.unload.run();
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_DELETE.get()) {
      this.menuDelete();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_BACK.get()) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
