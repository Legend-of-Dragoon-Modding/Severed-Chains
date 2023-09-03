package legend.game.characters;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class StatTypeRegistryEvent extends RegistryEvent.Register<StatType<?>> {
  public StatTypeRegistryEvent(final MutableRegistry<StatType<?>> registry) {
    super(registry);
  }
}
