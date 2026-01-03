package legend.game.modding.coremod;

import legend.core.GameEngine;
import legend.game.EngineState;
import legend.game.RegisterEngineStatesEvent;
import legend.game.title.Ttle;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class CoreEngineStates {
  private CoreEngineStates() { }

  private static final Registrar<EngineState, RegisterEngineStatesEvent> REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.engineStates, CoreMod.MOD_ID);

  public static final RegistryDelegate<EngineState> TITLE = REGISTRAR.register("title", Ttle::new);

  static void register(final RegisterEngineStatesEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
