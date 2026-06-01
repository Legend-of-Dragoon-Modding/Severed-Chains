package legend.game.saves;

import legend.game.inventory.screens.Control;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

public abstract class SavedGame {
  public final Campaign campaign;
  public final String version;
  public final String fileName;
  public final String saveName;
  public final RegistryId campaignType;
  public final ConfigCollection config;

  public RegistryId engineState;
  public FileData engineStateData = new FileData(new byte[0]);

  public SavedGame(final Campaign campaign, final String version, final String fileName, final String saveName, final RegistryId campaignType, final ConfigCollection config) {
    this.campaign = campaign;
    this.version = version;
    this.fileName = fileName;
    this.saveName = saveName;
    this.campaignType = campaignType;
    this.config = config;
  }

  public abstract boolean isValid();
  public abstract Control createSaveCard();
  public abstract GameState52c createGameState();

  @Override
  public String toString() {
    return this.saveName + " [" + this.fileName + ']';
  }
}
