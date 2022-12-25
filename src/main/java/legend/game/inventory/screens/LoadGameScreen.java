package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.SaveManager;
import legend.game.types.InventoryMenuState;

import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.getSlotY;
import static legend.game.SItem.renderSaveListArrows;
import static legend.game.SItem.renderSavedGames;
import static legend.game.SItem.saves;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.highlightLeftHalf_800bdbe8;
import static legend.game.Scus94491BpeSegment_800b.highlightRightHalf_800bdbec;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;

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

          if(this.scroll < saves.size() - 3) {
            this.scroll(this.scroll + 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.scroll > 0) {
            this.scroll(this.scroll - 1);
          }
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

        inventoryMenuState_800bdc28.set(InventoryMenuState._44);

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
}
