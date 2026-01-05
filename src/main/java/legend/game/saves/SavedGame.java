package legend.game.saves;

import legend.game.inventory.screens.Control;
import legend.game.types.GameState52c;

public abstract class SavedGame {
  public final String fileName;
  public final String saveName;
  public final GameState52c state;
  public final ConfigCollection config;

  public SavedGame(final String fileName, final String saveName, final GameState52c state, final ConfigCollection config) {
    this.fileName = fileName;
    this.saveName = saveName;
    this.state = state;
    this.config = config;
  }

  public boolean isValid() {
    return this.state != null;
  }

  public abstract Control createSaveCard();

  @Override
  public String toString() {
    return this.saveName + " [" + this.fileName + ']';
  }
}
