package legend.game.saves;

import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.controls.InvalidSaveCard;
import legend.game.types.GameState52c;

public class InvalidSavedGame extends SavedGame {
  public InvalidSavedGame(final Campaign campaign, final String fileName) {
    super(campaign, "", fileName, fileName, null, null);
  }

  @Override
  public Control createSaveCard() {
    return new InvalidSaveCard();
  }

  @Override
  public GameState52c createGameState() {
    throw new InvalidSaveException("Attempted to load invalid save " + this.fileName);
  }

  @Override
  public boolean isValid() {
    return false;
  }
}
