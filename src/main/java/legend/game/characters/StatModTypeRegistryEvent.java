package legend.game.characters;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class StatModTypeRegistryEvent extends RegistryEvent.Register<StatModType<?, ?, ?>> {
  public StatModTypeRegistryEvent(final MutableRegistry<StatModType<?, ?, ?>> registry) {
    super(registry);
  }
}
