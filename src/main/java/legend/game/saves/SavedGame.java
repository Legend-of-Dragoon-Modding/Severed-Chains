package legend.game.saves;

import legend.game.types.GameState52c;

/**
 * @param locationType 1 - world map
 *                     3 - chapter title
 *                     other - submap
 */
public record SavedGame(String fileName, String saveName, int locationType, int locationIndex, GameState52c state, ConfigCollection config, int maxHp, int maxMp) {
  public static SavedGame invalid(final String fileName) {
    return new SavedGame(fileName, fileName, 0, 0, null, null, 0, 0);
  }

  public boolean isValid() {
    return this.state != null;
  }
}
