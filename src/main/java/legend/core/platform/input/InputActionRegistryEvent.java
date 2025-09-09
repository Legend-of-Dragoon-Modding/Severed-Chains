package legend.core.platform.input;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class InputActionRegistryEvent extends RegistryEvent.Register<InputAction> {
  public InputActionRegistryEvent(final MutableRegistry<InputAction> registry) {
    super(registry);
  }
}
