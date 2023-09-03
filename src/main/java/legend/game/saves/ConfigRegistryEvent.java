package legend.game.saves;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class ConfigRegistryEvent extends RegistryEvent.Register<ConfigEntry<?>> {
  public ConfigRegistryEvent(final MutableRegistry<ConfigEntry<?>> registry) {
    super(registry);
  }
}
