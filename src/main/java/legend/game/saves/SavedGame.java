package legend.game.saves;

import legend.game.types.GameState52c;

public final class SavedGame {
  public String fileName;
  public String saveName;
  public final String locationName;
  public final GameState52c state;
  public final ConfigCollection config;
  public final int maxHp;
  public final int maxMp;

  public SavedGame(final String fileName, final String saveName, final String locationName, final GameState52c state, final ConfigCollection config, final int maxHp, final int maxMp) {
    this.fileName = fileName;
    this.saveName = saveName;
    this.locationName = locationName;
    this.state = state;
    this.config = config;
    this.maxHp = maxHp;
    this.maxMp = maxMp;
  }

  public static SavedGame invalid(final String fileName) {
    return new SavedGame(fileName, fileName, "<invalid save>", null, null, 0, 0);
  }

  public boolean isValid() {
    return this.state != null;
  }

  @Override
  public String toString() {
    return this.saveName;
  }
}
