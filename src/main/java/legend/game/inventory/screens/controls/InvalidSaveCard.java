package legend.game.inventory.screens.controls;

import legend.core.lang.I18nText;
import legend.game.inventory.screens.HorizontalAlign;

public class InvalidSaveCard extends BlankSaveCard {
  public InvalidSaveCard() {
    final Label invalidSave = this.addControl(new Label(new I18nText("lod_core.ui.load_game.invalid_save")));
    invalidSave.setPos(258, 47);
    invalidSave.setWidth(0);
    invalidSave.getFontOptions().horizontalAlign(HorizontalAlign.CENTRE);
  }
}
