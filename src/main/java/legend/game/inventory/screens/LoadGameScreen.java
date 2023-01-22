package legend.game.inventory.screens;

import legend.game.inventory.WhichMenu;
import legend.game.types.MessageBoxResult;

import static legend.game.SItem.Load_this_data_8011ca08;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.getSlotY;
import static legend.game.SItem.loadSaveFile;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderSaveGameSlot;
import static legend.game.SItem.saves;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8004.setMono;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class LoadGameScreen extends SaveListScreen {
  private int slot;

  public LoadGameScreen() {
    super(() -> whichMenu_800bdc38 = WhichMenu.UNLOAD_LOAD_GAME_MENU_15);
  }

  @Override
  protected int menuCount() {
    return saves.size();
  }

  @Override
  protected void onSelect(final int slot) {
    playSound(2);
    this.slot = slot;

    menuStack.pushScreen(new MessageBoxScreen(Load_this_data_8011ca08, 2, this::onMessageboxResult));

    if(saveListUpArrow_800bdb94 != null) {
      fadeOutArrow(saveListUpArrow_800bdb94);
      saveListUpArrow_800bdb94 = null;
    }

    //LAB_800ff3a4
    if(saveListDownArrow_800bdb98 != null) {
      fadeOutArrow(saveListDownArrow_800bdb98);
      saveListDownArrow_800bdb98 = null;
    }
  }

  @Override
  protected void onMessageboxResult(final MessageBoxResult result) {
    if(result == MessageBoxResult.YES) {
      loadSaveFile(this.slot);

      //LAB_800ff6ec
      _800bdc34.setu(0x1L);
      submapScene_80052c34.set(gameState_800babc8.submapScene_a4.get());
      submapCut_80052c30.set(gameState_800babc8.submapCut_a8.get());
      index_80052c38.set(gameState_800babc8.submapCut_a8.get());

      if(gameState_800babc8.submapCut_a8.get() == 264) { // Somewhere in Home of Giganto
        submapScene_80052c34.set(53);
      }

      setMono(gameState_800babc8.mono_4e0.get());

      this.loadingStage = 2;
    }
  }

  @Override
  protected void renderSaveSlot(final int slot, final int fileIndex, final boolean allocate) {
    renderSaveGameSlot(fileIndex, getSlotY(slot), allocate);
  }
}
