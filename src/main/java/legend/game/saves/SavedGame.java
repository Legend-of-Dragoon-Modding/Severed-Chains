package legend.game.saves;

import legend.game.types.GameState52c;

public final class SavedGame {
  public String fileName;
  public String saveName;
  public final int locationType;
  public final int locationIndex;
  public final GameState52c state;
  public final ConfigCollection config;
  public final int maxHp;
  public final int maxMp;

  /**
   * @param locationType 1 - world map
   *                     3 - chapter title
   *                     other - submap
   */
  public SavedGame(final String fileName, final String saveName, final int locationType, final int locationIndex, final GameState52c state, final ConfigCollection config, final int maxHp, final int maxMp) {
    this.fileName = fileName;
    this.saveName = saveName;
    this.locationType = locationType;
    this.locationIndex = locationIndex;
    this.state = state;
    this.config = config;
    this.maxHp = maxHp;
    this.maxMp = maxMp;
  }

  public static SavedGame invalid(final String fileName) {
    return new SavedGame(fileName, fileName, 0, 0, null, null, 0, 0);
  }

  public boolean isValid() {
    return this.state != null;
  }

  @Override
  public String toString() {
    return this.saveName;
  }
}
