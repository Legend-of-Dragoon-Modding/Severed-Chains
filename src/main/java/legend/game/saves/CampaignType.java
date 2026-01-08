package legend.game.saves;

import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class CampaignType extends RegistryEntry {
  public abstract void setUpNewCampaign(final GameState52c gameState);
  public abstract void transitionToNewCampaign(final GameState52c gameState);

  public abstract void setUpLoadedGame(final GameState52c gameState);
  public abstract void transitionToLoadedGame(final GameState52c gameState);
}
