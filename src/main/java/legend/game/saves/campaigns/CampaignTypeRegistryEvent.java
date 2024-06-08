package legend.game.saves.campaigns;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class CampaignTypeRegistryEvent extends RegistryEvent.Register<CampaignType> {
  public CampaignTypeRegistryEvent(final MutableRegistry<CampaignType> registry) {
    super(registry);
  }
}
