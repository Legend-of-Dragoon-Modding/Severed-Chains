package legend.game.characters;

import legend.game.modding.events.registries.RegistryEvent;
import legend.game.modding.registries.MutableRegistry;

public class StatTypeRegistryEvent extends RegistryEvent.Register<StatType<?>> {
  public StatTypeRegistryEvent(final MutableRegistry<StatType<?>> registry) {
    super(registry);
  }
}
