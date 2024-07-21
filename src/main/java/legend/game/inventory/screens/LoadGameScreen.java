package legend.game.inventory.screens;

import legend.game.SItem;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.BigList;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.inventory.screens.controls.RetailSaveCard;
import legend.game.inventory.screens.controls.SaveCard;
import legend.game.saves.Campaign;
import legend.game.saves.SavedGame;
import legend.game.saves.types.SaveType;
import legend.game.types.MessageBoxResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.SAVES;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;

public class LoadGameScreen extends MenuScreen {
  private static final Logger LOGGER = LogManager.getFormatterLogger(LoadGameScreen.class);

  private SaveCard<?> saveCard;
  private final BigList<SavedGame> saveList;
  private final Consumer<SavedGame<?>> saveSelected;
  private final Runnable closed;

  public LoadGameScreen(final Consumer<SavedGame<?>> saveSelected, final Runnable closed, final Campaign campaign) {
    this.saveSelected = saveSelected;
    this.closed = closed;

    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.addControl(new Background());

    // Bottom line
    this.addControl(Glyph.glyph(78)).setPos(26, 155);
    this.addControl(Glyph.glyph(79)).setPos(192, 155);

    this.saveCard = this.addControl(new RetailSaveCard());
    this.saveCard.setPos(16, 160);
    this.saveCard.alwaysReceiveInput();

    this.saveList = this.addControl(new BigList<>(SavedGame::toString));
    this.saveList.setPos(16, 16);
    this.saveList.setSize(360, 144);
    this.saveList.onHighlight(saveData -> {
      this.removeControl(this.saveCard);
      final SaveType<?> saveType = (SaveType<?>)saveData.saveType.get();
      this.saveCard = this.addControl(saveType.makeSaveCard());
      this.saveCard.setPos(16, 160);
      this.saveCard.setSaveData(saveData);
      this.saveCard.alwaysReceiveInput();
    });
    this.saveList.onSelection(this::onSelection);
    this.setFocus(this.saveList);

    for(final SavedGame<?> save : SAVES.loadAllSaves(campaign.filename())) {
      this.saveList.addEntry(save);
    }
  }

  private void onSelection(final SavedGame save) {
    if(save.isValid()) {
      final Map<RegistryId, Set<RegistryId>> missingIds = new HashMap<>();
      this.findMissingRegistryIds(save, missingIds);

      if(missingIds.isEmpty()) {
        this.showLoadGameBox(save);
      } else {
        this.showMissingIdsScreen(missingIds, () -> this.onMessageboxResult(MessageBoxResult.YES, save));
      }
    } else {
      playMenuSound(4);
      menuStack.pushScreen(new MessageBoxScreen("This save cannot be loaded", 0, result -> { }));
    }
  }

  private void showLoadGameBox(final SavedGame save) {
    playMenuSound(2);
    menuStack.pushScreen(new MessageBoxScreen("Load this save?", 2, result -> this.onMessageboxResult(result, save)));
  }

  private void showMissingIdsScreen(final Map<RegistryId, Set<RegistryId>> missingIds, final Runnable onConfirm) {
    playMenuSound(4);
    menuStack.pushScreen(new MissingRegistryIdsScreen(missingIds, result -> {
      if(result == MessageBoxResult.YES) {
        onConfirm.run();
      }
    }));
  }

  private void onMessageboxResult(final MessageBoxResult result, final SavedGame save) {
    if(result == MessageBoxResult.YES) {
      this.saveSelected.accept(save);
    }
  }

  @Override
  protected void render() {
    SItem.renderCentredText("Load Game", 188, 10, TextColour.BROWN);
    SItem.renderText("\u011f Delete", 297, 226, TextColour.BROWN);
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
            SAVES.deleteSave(this.saveList.getSelected().state.campaignName, this.saveList.getSelected().fileName);
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
    this.closed.run();
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_WEST) {
      this.menuDelete();
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  private void findMissingRegistryIds(final SavedGame<?> save, final Map<RegistryId, Set<RegistryId>> missingIds) {
    for(final var entry : save.ids.entrySet()) {
      final Registry<?> registry = REGISTRIES.get(entry.getKey());

      if(registry == null) {
        missingIds.put(entry.getKey(), entry.getValue());
        continue;
      }

      for(final RegistryId id : entry.getValue()) {
        if(!registry.hasEntry(id)) {
          missingIds.computeIfAbsent(entry.getKey(), k -> new HashSet<>()).add(id);
        }
      }
    }
  }
}
