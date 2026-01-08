package legend.game.saves;

import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class MemcardSavedGame extends RetailSavedGame {
  public final int locationType;
  public final int locationIndex;

  public MemcardSavedGame(final String fileName, final String saveName, final int locationType, final int locationIndex, final String locationName, final RegistryId campaignType, final RegistryId engineState, final FileData engineStateData, final GameState52c gameState, final ConfigCollection config, final int maxHp, final int maxMp) {
    super(fileName, saveName, locationName, campaignType, engineState, engineStateData, gameState, config, maxHp, maxMp);
    this.locationType = locationType;
    this.locationIndex = locationIndex;
  }
}
