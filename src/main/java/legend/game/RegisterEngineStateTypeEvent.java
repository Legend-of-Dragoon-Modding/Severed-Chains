package legend.game;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterEngineStateTypeEvent extends RegistryEvent.Register<EngineStateType<?>> {
  public RegisterEngineStateTypeEvent(final MutableRegistry<EngineStateType<?>> registry) {
    super(registry);
  }
}
