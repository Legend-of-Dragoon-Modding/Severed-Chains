package legend.game.additions;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class AdditionRegistry extends MutableRegistry<Addition> {
  public AdditionRegistry() {
    super(new RegistryId("lod_core", "additions"));
  }
}
