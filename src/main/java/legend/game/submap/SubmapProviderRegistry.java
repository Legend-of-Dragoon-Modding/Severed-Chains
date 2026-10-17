package legend.game.submap;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class SubmapProviderRegistry extends MutableRegistry<SubmapProvider> {
  public SubmapProviderRegistry() {
    super(new RegistryId("lod_core", "submap_providers"));
  }
}
