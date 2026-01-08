package legend.game.saves;

import legend.game.inventory.screens.Control;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

public abstract class SavedGame {
  public final String fileName;
  public final String saveName;
  public final RegistryId campaignType;
  public final RegistryId engineState;
  public final FileData engineStateData;
  public final GameState52c gameState;
  public final ConfigCollection config;

  public SavedGame(final String fileName, final String saveName, final RegistryId campaignType, final RegistryId engineState, final FileData engineStateData, final GameState52c gameState, final ConfigCollection config) {
    this.fileName = fileName;
    this.saveName = saveName;
    this.campaignType = campaignType;
    this.engineState = engineState;
    this.engineStateData = engineStateData;
    this.gameState = gameState;
    this.config = config;
  }

  public boolean isValid() {
    return this.gameState != null;
  }

  public abstract Control createSaveCard();

  @Override
  public String toString() {
    return this.saveName + " [" + this.fileName + ']';
  }
}
