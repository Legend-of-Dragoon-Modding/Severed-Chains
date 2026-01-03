package legend.game;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class EngineStateRegistry extends MutableRegistry<EngineState> {
  public EngineStateRegistry() {
    super(new RegistryId("lod_core", "engine_states"));
  }
}
