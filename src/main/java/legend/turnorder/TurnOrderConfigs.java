package legend.turnorder;

import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistryEvent;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.core.GameEngine.REGISTRIES;

public final class TurnOrderConfigs {
  private TurnOrderConfigs() { }

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> REGISTRAR = new Registrar<>(REGISTRIES.config, TurnOrderMod.MOD_ID);

  public static final RegistryDelegate<ShowTurnOrderConfig> SHOW_TURN_ORDER = REGISTRAR.register("show_turn_order", ShowTurnOrderConfig::new);

  static void register(final ConfigRegistryEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
