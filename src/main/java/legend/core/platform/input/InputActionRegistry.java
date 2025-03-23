package legend.core.platform.input;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class InputActionRegistry extends MutableRegistry<InputAction> {
  public InputActionRegistry() {
    super(new RegistryId("lod_core", "input_actions"));
  }
}
