package legend.game.saves;

import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.controls.RetailSaveCard;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryId;

public class RetailSavedGame extends SavedGame {
  public final String locationName;
  public final int maxHp;
  public final int maxMp;

  public RetailSavedGame(final String fileName, final String saveName, final String locationName, final RegistryId campaignType, final RegistryId engineState, final FileData engineStateData, final GameState52c gameState, final ConfigCollection config, final int maxHp, final int maxMp) {
    super(fileName, saveName, campaignType, engineState, engineStateData, gameState, config);
    this.locationName = locationName;
    this.maxHp = maxHp;
    this.maxMp = maxMp;
  }

  @Override
  public Control createSaveCard() {
    return new RetailSaveCard(this);
  }
}
