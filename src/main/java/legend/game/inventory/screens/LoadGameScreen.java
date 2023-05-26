package legend.game.inventory.screens;

import legend.game.SItem;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.BigList;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.inventory.screens.controls.SaveCard;
import legend.game.saves.Campaign;
import legend.game.saves.SavedGame;
import legend.game.types.LodString;
import legend.game.types.MessageBoxResult;

import java.util.function.Consumer;

import static legend.core.GameEngine.SAVES;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;

public class LoadGameScreen extends MenuScreen {
  private final Consumer<SavedGame> saveSelected;
  private final Runnable closed;

  public LoadGameScreen(final Consumer<SavedGame> saveSelected, final Runnable closed, final Campaign campaign) {
    this.saveSelected = saveSelected;
    this.closed = closed;

    deallocateRenderables(0xff);
    scriptStartEffect(2, 10);

    this.addControl(new Background());

    // Bottom line
    this.addControl(Glyph.glyph(78)).setPos(26, 155);
    this.addControl(Glyph.glyph(79)).setPos(192, 155);

    final SaveCard saveCard = this.addControl(new SaveCard());
    saveCard.setPos(16, 160);

    final BigList<SavedGame> saveList = this.addControl(new BigList<>(SavedGame::filename));
    saveList.setPos(16, 16);
    saveList.setSize(360, 144);
    saveList.onHighlight(saveCard::setSaveData);
    saveList.onSelection(this::onSelection);
    this.setFocus(saveList);

    for(final SavedGame save : SAVES.loadAllSaves(campaign.filename())) {
      saveList.addEntry(save);
    }
  }

  private void onSelection(final SavedGame save) {
    if(save.isValid()) {
      playSound(2);
      menuStack.pushScreen(new MessageBoxScreen(new LodString("Load this save?"), 2, result -> this.onMessageboxResult(result, save)));
    } else {
      playSound(4);
      menuStack.pushScreen(new MessageBoxScreen(new LodString("This save cannot be loaded"), 0, result -> { }));
    }
  }

  private void onMessageboxResult(final MessageBoxResult result, final SavedGame save) {
    if(result == MessageBoxResult.YES) {
      this.saveSelected.accept(save);
    }
  }

  @Override
  protected void render() {
    SItem.renderCentredText(new LodString("Load Game"), 188, 10, TextColour.BROWN);
  }

  private void menuEscape() {
    playSound(3);
    this.closed.run();
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
