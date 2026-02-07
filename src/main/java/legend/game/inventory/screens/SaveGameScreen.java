package legend.game.inventory.screens;

import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.BigList;
import legend.game.inventory.screens.controls.BlankSaveCard;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.saves.SaveFailedException;
import legend.game.saves.SavedGame;
import legend.game.types.MessageBoxResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static legend.core.GameEngine.SAVES;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment_800b.campaignType;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Text.renderText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DELETE;
import static legend.game.sound.Audio.playMenuSound;

public class SaveGameScreen extends MenuScreen {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SaveGameScreen.class);

  private static final String Overwrite_save_8011c9e8 = "Overwrite save?";

  private final BigList<CompletableFuture<SavedGame>> saveList;
  private Control saveCard;
  private final List<CompletableFuture<SavedGame>> saves;

  private final Runnable unload;

  public SaveGameScreen(final Runnable unload) {
    this.unload = unload;

    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.addControl(new Background());

    // Bottom line
    this.addControl(Glyph.glyph(78)).setPos(26, 155);
    this.addControl(Glyph.glyph(79)).setPos(192, 155);

    this.saveList = this.addControl(new BigList<>(savedGame -> {
      if(savedGame == null) {
        return "<new save>";
      }

      if(!savedGame.isDone()) {
        return "Loading...";
      }

      return savedGame.resultNow().saveName;
    }));

    this.saveList.setPos(16, 16);
    this.saveList.setSize(360, 144);
    this.saveList.onHighlight(this::onHighlight);
    this.saveList.onSelection(save -> {
      if(save == null) {
        this.onSelection(null);
      } else if(save.isDone()) {
        this.onSelection(save.resultNow());
      } else {
        playMenuSound(40);
      }
    });
    this.setFocus(this.saveList);

    this.saveList.addEntry(null);

    this.saves = gameState_800babc8.campaign.loadAllSaves();
    for(final CompletableFuture<SavedGame> save : this.saves) {
      this.saveList.addEntry(save);
    }

    this.addHotkey(I18n.translate("lod_core.ui.save_game.delete"), INPUT_ACTION_MENU_DELETE, this::menuDelete);
    this.addHotkey(I18n.translate("lod_core.ui.save_game.back"), INPUT_ACTION_MENU_BACK, this::menuEscape);
  }

  private void onHighlight(final CompletableFuture<SavedGame> save) {
    synchronized(this.saveList) {
      if(this.saveCard != null) {
        this.removeControl(this.saveCard);
      }

      if(save != null && save.isDone()) {
        this.saveCard = this.addControl(save.resultNow().createSaveCard());
        this.saveCard.alwaysReceiveInput();
      } else {
        this.saveCard = this.addControl(new BlankSaveCard());

        if(save != null) {
          save.thenAcceptAsync(f -> {
            synchronized(this.saveList) {
              if(this.saveList.getSelected() == save) {
                this.onHighlight(save);
              }
            }
          });
        }
      }

      this.saveCard.setPos(16, 160);
    }
  }

  @Override
  public void setFocus(@Nullable final Control control) {
    super.setFocus(this.saveList);
  }

  @Override
  protected void render() {
    renderText("Save Game", 188, 10, UI_TEXT_CENTERED);
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

      try {
        SAVES.newSave(name, campaignType.get(), currentEngineState_8004dd04, gameState_800babc8, stats_800be5f8);
        this.unload.run();
      } catch(final SaveFailedException e) {
        menuStack.pushScreen(new MessageBoxScreen("Failed to save game", 0, r -> { }));
        LOGGER.error("Failed to save game", e);
      }
    }
  }

  private void onOverwriteResult(final MessageBoxResult result, final SavedGame save) {
    if(result == MessageBoxResult.YES) {
      try {
        SAVES.overwriteSave(save.fileName, save.saveName, campaignType.get(), currentEngineState_8004dd04, gameState_800babc8, stats_800be5f8);
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

    if(this.saveList.getSelected() != null && this.saveList.getSelected().isDone()) {
      menuStack.pushScreen(new MessageBoxScreen("Are you sure you want to\ndelete this save?", 2, result -> {
        if(result == MessageBoxResult.YES) {
          try {
            final SavedGame savedGame = this.saveList.getSelected().resultNow();
            savedGame.campaign.deleteSave(savedGame.fileName);
            this.saves.removeIf(save -> save.resultNow().fileName.equals(savedGame.fileName));
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
}
