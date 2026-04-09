package legend.game;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterEngineStateTypesEvent extends RegistryEvent.Register<EngineStateType<?>> {
  public RegisterEngineStateTypesEvent(final MutableRegistry<EngineStateType<?>> registry) {
    super(registry);
  }
}
