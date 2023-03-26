package legend.game.saves;

import legend.game.modding.events.registries.RegistryEvent;
import legend.game.modding.registries.MutableRegistry;

public class ConfigRegistryEvent extends RegistryEvent.Register<ConfigEntry<?>> {
  public ConfigRegistryEvent(final MutableRegistry<ConfigEntry<?>> registry) {
    super(registry);
  }
}
