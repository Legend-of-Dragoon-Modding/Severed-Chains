package legend.game.modding.coremod;

import legend.core.GameEngine;
import legend.game.EngineStateType;
import legend.game.RegisterEngineStateTypesEvent;
import legend.game.title.Ttle;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class CoreEngineStateTypes {
  private CoreEngineStateTypes() { }

  private static final Registrar<EngineStateType<?>, RegisterEngineStateTypesEvent> REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.engineStateTypes, CoreMod.MOD_ID);

  public static final RegistryDelegate<EngineStateType<Ttle>> TITLE = REGISTRAR.register("title", () -> new EngineStateType<>(Ttle.class, Ttle::new));

  static void register(final RegisterEngineStateTypesEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
