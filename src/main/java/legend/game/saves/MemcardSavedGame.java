package legend.game.saves;

import legend.game.types.GameState52c;

public final class MemcardSavedGame extends RetailSavedGame {
  public final int locationType;
  public final int locationIndex;

  public MemcardSavedGame(final String fileName, final String saveName, final int locationType, final int locationIndex, final String locationName, final GameState52c state, final ConfigCollection config, final int maxHp, final int maxMp) {
    super(fileName, saveName, locationName, state, config, maxHp, maxMp);
    this.locationType = locationType;
    this.locationIndex = locationIndex;
  }
}
