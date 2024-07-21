package legend.game.saves.campaigns;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class CampaignTypeRegistry extends MutableRegistry<CampaignType> {
  public CampaignTypeRegistry() {
    super(new RegistryId("lod-core", "campaign_types"));
  }
}
