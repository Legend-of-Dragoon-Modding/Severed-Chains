package legend.lodmod;

import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistryEvent;
import legend.lodmod.config.ItemStackSizeConfig;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.core.GameEngine.REGISTRIES;

public final class LodConfig {
  private LodConfig() { }

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> REGISTRAR = new Registrar<>(REGISTRIES.config, LodMod.MOD_ID);

  public static final RegistryDelegate<ItemStackSizeConfig> ITEM_STACK_SIZE = REGISTRAR.register("item_stack_size", ItemStackSizeConfig::new);

  static void register(final ConfigRegistryEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
