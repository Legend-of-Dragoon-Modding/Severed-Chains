package legend.game.inventory.screens;

import static legend.game.SItem.renderSavedGames;
import static legend.game.SItem.selectedSlot_8011d740;
import static legend.game.SItem.slotScroll_8011d744;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;

public class LoadGameScreen extends MenuScreen {
  private int loadingStage;

  @Override
  public void render() {
    switch(this.loadingStage) {
      case 0 -> {
        deallocateRenderables(0xffL);
        scriptStartEffect(0x2L, 0xaL);
        this.loadingStage++;
      }

      case 1 -> {
        saveListDownArrow_800bdb98.clear();
        saveListUpArrow_800bdb94.clear();
        slotScroll_8011d744.set(0);
        selectedSlot_8011d740.set(0);
        renderSavedGames(0, false, 0);
        this.loadingStage++;
      }
    }
  }

  @Override
  public void handleInput() {

  }
}
