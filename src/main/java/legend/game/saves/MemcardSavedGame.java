package legend.game.saves;

import org.legendofdragoon.modloader.registries.RegistryId;

public final class MemcardSavedGame extends RetailSavedGame {
  public int locationType;
  public int locationIndex;

  public MemcardSavedGame(final Campaign campaign, final String fileName, final String saveName, final RegistryId campaignType, final ConfigCollection config) {
    super(campaign, "OG", fileName, saveName, campaignType, config);
  }
}
