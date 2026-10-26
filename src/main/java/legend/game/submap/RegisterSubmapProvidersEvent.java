package legend.game.submap;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterSubmapProvidersEvent extends RegistryEvent.Register<SubmapProvider> {
  public RegisterSubmapProvidersEvent(final MutableRegistry<SubmapProvider> registry) {
    super(registry);
  }
}
