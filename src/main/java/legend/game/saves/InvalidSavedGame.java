package legend.game.saves;

import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.controls.InvalidSaveCard;

public class InvalidSavedGame extends SavedGame {
  public InvalidSavedGame(final String fileName) {
    super(fileName, fileName, null, null);
  }

  @Override
  public Control createSaveCard() {
    return new InvalidSaveCard();
  }
}
