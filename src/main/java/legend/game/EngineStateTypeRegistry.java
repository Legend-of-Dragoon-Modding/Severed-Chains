package legend.game;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class EngineStateTypeRegistry extends MutableRegistry<EngineStateType<?>> {
  public EngineStateTypeRegistry() {
    super(new RegistryId("lod-core", "engine_state_types"));
  }
}
