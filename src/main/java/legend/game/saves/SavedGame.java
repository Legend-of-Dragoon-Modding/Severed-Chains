package legend.game.saves;

import legend.game.saves.types.SaveDisplay;
import legend.game.saves.types.SaveType;
import legend.game.types.GameState52c;

public final class SavedGame<Display extends SaveDisplay> {
  public String fileName;
  public String saveName;
  public final SaveType<Display> saveType;
  public final Display display;
  public final GameState52c state;
  public final ConfigCollection config;

  public SavedGame(final String fileName, final String saveName, final SaveType<Display> saveType, final Display display, final GameState52c state, final ConfigCollection config) {
    this.fileName = fileName;
    this.saveName = saveName;
    this.saveType = saveType;
    this.display = display;
    this.state = state;
    this.config = config;
  }

  public static SavedGame<?> invalid(final String fileName) {
    return new SavedGame<>(fileName, fileName, null, null, null, null);
  }

  public boolean isValid() {
    return this.state != null;
  }

  @Override
  public String toString() {
    return this.saveName;
  }
}
