package legend.game;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterEngineStatesEvent extends RegistryEvent.Register<EngineState> {
  public RegisterEngineStatesEvent(final MutableRegistry<EngineState> registry) {
    super(registry);
  }
}
