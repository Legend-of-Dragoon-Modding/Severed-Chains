package legend.game.saves.types;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class SaveTypeRegistryEvent extends RegistryEvent.Register<SaveType<?>> {
  public SaveTypeRegistryEvent(final MutableRegistry<SaveType<?>> registry) {
    super(registry);
  }
}
