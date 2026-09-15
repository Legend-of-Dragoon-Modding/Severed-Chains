package legend.lodmod;

import legend.game.submap.RegisterSubmapProvidersEvent;
import legend.game.submap.RetailSubmapProvider;
import legend.game.submap.SubmapProvider;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.core.GameEngine.REGISTRIES;

public final class LodSubmapProviders {
  private static final Registrar<SubmapProvider, RegisterSubmapProvidersEvent> REGISTRAR = new Registrar<>(REGISTRIES.submapProviders, LodMod.MOD_ID);
  public static final RegistryDelegate<RetailSubmapProvider> RETAIL = REGISTRAR.register("retail_submap", RetailSubmapProvider::new);

  private LodSubmapProviders() { }

  static void register(final RegisterSubmapProvidersEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
