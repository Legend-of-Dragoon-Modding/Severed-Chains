package legend.lodmod;

import legend.core.GameEngine;
import legend.game.EngineState;
import legend.game.RegisterEngineStatesEvent;
import legend.game.submap.SMap;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class LodEngineStates {
  private LodEngineStates() { }

  private static final Registrar<EngineState, RegisterEngineStatesEvent> REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.engineStates, LodMod.MOD_ID);

  public static final RegistryDelegate<EngineState> SUBMAP = REGISTRAR.register("submap", SMap::new);
  public static final RegistryDelegate<EngineState> WORLD_MAP = REGISTRAR.register("world_map", SMap::new);
  public static final RegistryDelegate<EngineState> BATTLE = REGISTRAR.register("battle", SMap::new);

  static void register(final RegisterEngineStatesEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
