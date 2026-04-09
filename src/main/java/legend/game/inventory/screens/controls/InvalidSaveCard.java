package legend.game.inventory.screens.controls;

import legend.game.inventory.screens.HorizontalAlign;

public class InvalidSaveCard extends BlankSaveCard {
  public InvalidSaveCard() {
    final Label invalidSave = this.addControl(new Label("Invalid save"));
    invalidSave.setPos(258, 47);
    invalidSave.setWidth(0);
    invalidSave.getFontOptions().horizontalAlign(HorizontalAlign.CENTRE);
  }
}
