package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.SaveManager;
import legend.game.inventory.WhichMenu;
import legend.game.types.InventoryMenuState;
import legend.game.types.MessageBoxResult;

import static legend.game.SItem.FUN_800fca0c;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.Load_this_data_8011ca08;
import static legend.game.SItem._8011d7b8;
import static legend.game.SItem._8011d7bc;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.getSlotY;
import static legend.game.SItem.loadSaveFile;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderSaveListArrows;
import static legend.game.SItem.renderSavedGames;
import static legend.game.SItem.saves;
import static legend.game.SMap.FUN_800e3fac;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002379c;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8004._8004dd30;
import static legend.game.Scus94491BpeSegment_8004.loadingGameStateOverlay_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.setMono;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.highlightLeftHalf_800bdbe8;
import static legend.game.Scus94491BpeSegment_800b.highlightRightHalf_800bdbec;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class LoadGameScreen extends MenuScreen {
  private int loadingStage;

  private int selectedSlot;
  private int scroll;

  private double scrollAccumulator;

  @Override
  public void render() {
    switch(this.loadingStage) {
      case 0 -> {
        deallocateRenderables(0xff);
        scriptStartEffect(2, 10);

        saveListDownArrow_800bdb98.clear();
        saveListUpArrow_800bdb94.clear();
        this.scroll = 0;
        this.selectedSlot = 0;

        saves.clear();
        saves.addAll(SaveManager.loadAllDisplayData());

        highlightLeftHalf_800bdbe8.set(allocateUiElement(129, 129,  16, getSlotY(this.selectedSlot)));
        highlightRightHalf_800bdbec.set(allocateUiElement(130, 130, 192, getSlotY(this.selectedSlot)));
        FUN_80104b60(highlightLeftHalf_800bdbe8.deref());
        FUN_80104b60(highlightRightHalf_800bdbec.deref());
        renderSaveListArrows(this.scroll);

        deallocateRenderables(0);
        renderSavedGames(this.scroll, true, 0xff);

        this.loadingStage++;
      }

      case 1 -> {
        renderSaveListArrows(this.scroll);
        renderSavedGames(this.scroll, true, 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.scroll > 0) {
            this.scroll(this.scroll - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.scroll < saves.size() - 3) {
            this.scroll(this.scroll + 1);
          }
        }
      }

      case 2 -> {
        _8004dd30.setu(0);
        renderSavedGames(this.scroll, true, 0);
        FUN_800fca0c(InventoryMenuState._125, 12);
        scriptStartEffect(1, 10);
        this.loadingStage = 3;
      }

      case 3 -> {
        renderSavedGames(this.scroll, true, 0);

        if(_800bb168.get() >= 0xff) {
          deallocateRenderables(0xff);
          free(drgn0_6666FilePtr_800bdc3c.getPointer());

          scriptStartEffect(2, 10);
          whichMenu_800bdc38 = WhichMenu.UNLOAD_LOAD_GAME_MENU_15;

          if(mainCallbackIndex_8004dd20.get() == 5 && loadingGameStateOverlay_8004dd08.get() == 0) {
            FUN_800e3fac();
          }

          textZ_800bdf00.set(13);
        }
      }
    }
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    if(this.loadingStage != 1) {
      return;
    }

    for(int i = 0; i < 3; i++) {
      final int slotWidth = 344;
      final int slotHeight = 64;
      final int slotX = 20;
      final int slotY = getSlotY(i);

      if(MathHelper.inBox(x, y, slotX, slotY, slotWidth, slotHeight)) {
        if(i != this.selectedSlot) {
          playSound(1);
          this.selectedSlot = i;
          highlightLeftHalf_800bdbe8.deref().y_44.set(getSlotY(this.selectedSlot));
          highlightRightHalf_800bdbec.deref().y_44.set(getSlotY(this.selectedSlot));
        }
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.loadingStage != 1) {
      return;
    }

    for(int i = 0; i < 3; i++) {
      final int slotWidth = 344;
      final int slotHeight = 64;
      final int slotX = 20;
      final int slotY = getSlotY(i);

      if(MathHelper.inBox(x, y, slotX, slotY, slotWidth, slotHeight)) {
        playSound(2);
        this.selectedSlot = i;

        menuStack.pushScreen(new MessageBoxScreen(Load_this_data_8011ca08, 2, this::onMessageboxResult));

        if(!saveListUpArrow_800bdb94.isNull()) {
          fadeOutArrow(saveListUpArrow_800bdb94.deref());
          saveListUpArrow_800bdb94.clear();
        }

        //LAB_800ff3a4
        if(!saveListDownArrow_800bdb98.isNull()) {
          fadeOutArrow(saveListDownArrow_800bdb98.deref());
          saveListDownArrow_800bdb98.clear();
        }
      }
    }
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
  }

  private void scroll(final int scroll) {
    playSound(1);
    this.scroll = scroll;
    highlightLeftHalf_800bdbe8.deref().y_44.set(getSlotY(this.selectedSlot));
    highlightRightHalf_800bdbec.deref().y_44.set(getSlotY(this.selectedSlot));
    deallocateRenderables(0);
    renderSavedGames(this.scroll, true, 0xff);
  }

  private void onMessageboxResult(final MessageBoxResult result) {
    if(result == MessageBoxResult.YES) {
      _8011d7bc.set(0);
      _8011d7b8.set(0);

      loadSaveFile(this.scroll + this.selectedSlot);

      //LAB_800ff6ec
      _800bdc34.setu(0x1L);
      submapScene_80052c34.setu(gameState_800babc8.submapScene_a4.get());
      submapCut_80052c30.set(gameState_800babc8.submapCut_a8.get());
      index_80052c38.set(gameState_800babc8.submapCut_a8.get());

      if(gameState_800babc8.submapCut_a8.get() == 264) { // Somewhere in Home of Giganto
        submapScene_80052c34.setu(53);
      }

      FUN_8002379c();
      setMono(gameState_800babc8.mono_4e0.get());

      this.loadingStage = 2;
    }
  }
}
