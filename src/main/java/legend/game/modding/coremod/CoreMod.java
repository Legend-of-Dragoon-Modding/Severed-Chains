package legend.game.modding.coremod;

import legend.core.GameEngine;
import legend.game.characters.StatType;
import legend.game.characters.StatTypeRegistryEvent;
import legend.game.combat.bobj.BattleObjectType;
import legend.game.combat.bobj.BattleObjectTypeRegistryEvent;
import legend.game.modding.Mod;
import legend.game.modding.coremod.config.EncounterRateConfigEntry;
import legend.game.modding.coremod.config.IndicatorModeConfigEntry;
import legend.game.modding.coremod.config.InventorySizeConfigEntry;
import legend.game.modding.events.EventListener;
import legend.game.modding.events.combat.RegisterBattleObjectStatsEvent;
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

  private static final Registrar<StatType, StatTypeRegistryEvent> STAT_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.stats, MOD_ID);
  public static final RegistryDelegate<StatType> HP_STAT = STAT_REGISTRAR.register("hp", StatType::new);
  public static final RegistryDelegate<StatType> MP_STAT = STAT_REGISTRAR.register("mp", StatType::new);
  public static final RegistryDelegate<StatType> SP_STAT = STAT_REGISTRAR.register("sp", StatType::new);

  private static final Registrar<BattleObjectType, BattleObjectTypeRegistryEvent> BOBJ_TYPE_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.battleObjectTypes, MOD_ID);
  public static final RegistryDelegate<BattleObjectType> PLAYER_TYPE = BOBJ_TYPE_REGISTRAR.register("player", BattleObjectType::new);
  public static final RegistryDelegate<BattleObjectType> MONSTER_TYPE = BOBJ_TYPE_REGISTRAR.register("monster", BattleObjectType::new);

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> CONFIG_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.config, MOD_ID);
  public static final RegistryDelegate<IndicatorModeConfigEntry> INDICATOR_MODE_CONFIG = CONFIG_REGISTRAR.register("indicator_mode", IndicatorModeConfigEntry::new);
  public static final RegistryDelegate<InventorySizeConfigEntry> INVENTORY_SIZE_CONFIG = CONFIG_REGISTRAR.register("inventory_size", InventorySizeConfigEntry::new);
  public static final RegistryDelegate<EncounterRateConfigEntry> ENCOUNTER_RATE_CONFIG = CONFIG_REGISTRAR.register("encounter_rate", EncounterRateConfigEntry::new);

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  @EventListener
  public static void registerConfig(final ConfigRegistryEvent event) {
    CONFIG_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerStatTypes(final StatTypeRegistryEvent event) {
    STAT_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerBobjTypes(final BattleObjectTypeRegistryEvent event) {
    BOBJ_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerBobjStats(final RegisterBattleObjectStatsEvent event) {
    event.addStat(HP_STAT.get());
  }
}
