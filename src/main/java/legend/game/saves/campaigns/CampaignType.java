package legend.game.saves.campaigns;

import legend.core.memory.types.IntRef;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class CampaignType extends RegistryEntry {
  public abstract void saveGameState(final FileData data, final IntRef offset, final GameState52c gameState);
  public abstract GameState52c loadGameState(final FileData data, final IntRef offset);

  public abstract void setUpNewCampaign(final GameState52c state);
  public abstract void transitionToNewCampaign(final GameState52c state);

  public abstract void setUpLoadedGame(final GameState52c state);
  public abstract void transitionToLoadedGame(final GameState52c state);
}
