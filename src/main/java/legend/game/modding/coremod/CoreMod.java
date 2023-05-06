package legend.game.modding.coremod;

import legend.core.GameEngine;
import legend.game.modding.Mod;
import legend.game.modding.coremod.config.AdditionOverlayConfigEntry;
import legend.game.modding.coremod.config.AutoAdditionConfigEntry;
import legend.game.modding.coremod.config.EncounterRateConfigEntry;
import legend.game.modding.coremod.config.IndicatorModeConfigEntry;
import legend.game.modding.coremod.config.InventorySizeConfigEntry;
import legend.game.modding.events.EventListener;
import legend.game.modding.registries.Registrar;
import legend.game.modding.registries.RegistryDelegate;
import legend.game.modding.registries.RegistryId;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistryEvent;

/** Core mod that contains engine-level content. Game can not run without it. */
@Mod(id = CoreMod.MOD_ID)
@EventListener
public class CoreMod {
  public static final String MOD_ID = "lod-core";

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> CONFIG_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.config, MOD_ID);
  public static final RegistryDelegate<IndicatorModeConfigEntry> INDICATOR_MODE_CONFIG = CONFIG_REGISTRAR.register("indicator_mode", IndicatorModeConfigEntry::new);
  public static final RegistryDelegate<InventorySizeConfigEntry> INVENTORY_SIZE_CONFIG = CONFIG_REGISTRAR.register("inventory_size", InventorySizeConfigEntry::new);
  public static final RegistryDelegate<EncounterRateConfigEntry> ENCOUNTER_RATE_CONFIG = CONFIG_REGISTRAR.register("encounter_rate", EncounterRateConfigEntry::new);
  public static final RegistryDelegate<AutoAdditionConfigEntry> AUTO_ADDITION_CONFIG = CONFIG_REGISTRAR.register("auto_addition", AutoAdditionConfigEntry::new);
  public static final RegistryDelegate<AdditionOverlayConfigEntry> ADDITION_OVERLAY_CONFIG = CONFIG_REGISTRAR.register("addition_overlay_mode", AdditionOverlayConfigEntry::new);

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  @EventListener
  public static void registerConfig(final ConfigRegistryEvent event) {
    CONFIG_REGISTRAR.registryEvent(event);
  }
}
