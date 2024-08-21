package legend.game.saves;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class ConfigRegistry extends MutableRegistry<ConfigEntry<?>> {
  public ConfigRegistry() {
    super(new RegistryId("lod_core", "config"));
  }
}
