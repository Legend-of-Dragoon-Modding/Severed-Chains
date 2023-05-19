package legend.game.saves;

import legend.game.types.GameState52c;

/**
 * @param locationType 1 - world map
 *                     3 - chapter title
 *                     other - submap
 */
public record SavedGame(String filename, int locationType, int locationIndex, GameState52c state, ConfigCollection config) {
  public static SavedGame invalid(final String filename) {
    return new SavedGame(filename, 0, 0, null, null);
  }

  public boolean isValid() {
    return this.state != null;
  }
}
