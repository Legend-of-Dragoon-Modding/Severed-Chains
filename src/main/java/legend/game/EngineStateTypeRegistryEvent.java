package legend.game;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class EngineStateTypeRegistryEvent extends RegistryEvent.Register<EngineStateType<?>> {
  public EngineStateTypeRegistryEvent(final MutableRegistry<EngineStateType<?>> registry) {
    super(registry);
  }
}
