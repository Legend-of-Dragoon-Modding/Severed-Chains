package legend.game.saves;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterCampaignTypesEvent extends RegistryEvent.Register<CampaignType> {
  public RegisterCampaignTypesEvent(final MutableRegistry<CampaignType> registry) {
    super(registry);
  }
}
