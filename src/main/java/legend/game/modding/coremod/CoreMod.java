package legend.game.modding.coremod;

import legend.core.GameEngine;
import legend.game.characters.Element;
import legend.game.characters.ElementRegistryEvent;
import legend.game.characters.StatType;
import legend.game.characters.StatTypeRegistryEvent;
import legend.game.characters.UnaryStat;
import legend.game.characters.VitalsStat;
import legend.game.combat.bobj.BattleObjectType;
import legend.game.combat.bobj.BattleObjectTypeRegistryEvent;
import legend.game.modding.Mod;
import legend.game.modding.coremod.config.AdditionOverlayConfigEntry;
import legend.game.modding.coremod.config.AutoAdditionConfigEntry;
import legend.game.modding.coremod.config.EncounterRateConfigEntry;
import legend.game.modding.coremod.config.IndicatorModeConfigEntry;
import legend.game.modding.coremod.config.InventorySizeConfigEntry;
import legend.game.modding.coremod.config.RenderScaleConfigEntry;
import legend.game.modding.coremod.elements.DarkElement;
import legend.game.modding.coremod.elements.DivineElement;
import legend.game.modding.coremod.elements.EarthElement;
import legend.game.modding.coremod.elements.FireElement;
import legend.game.modding.coremod.elements.LightElement;
import legend.game.modding.coremod.elements.NoElement;
import legend.game.modding.coremod.elements.ThunderElement;
import legend.game.modding.coremod.elements.WaterElement;
import legend.game.modding.coremod.elements.WindElement;
import legend.game.modding.events.EventListener;
import legend.game.modding.events.combat.RegisterBattleObjectStatsEvent;
import legend.game.modding.events.gamestate.GameLoadedEvent;
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

  private static final Registrar<StatType<?>, StatTypeRegistryEvent> STAT_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.stats, MOD_ID);
  public static final RegistryDelegate<StatType<VitalsStat>> HP_STAT = STAT_REGISTRAR.register("hp", () -> new StatType<>(VitalsStat::new));
  public static final RegistryDelegate<StatType<VitalsStat>> MP_STAT = STAT_REGISTRAR.register("mp", () -> new StatType<>(VitalsStat::new));
  public static final RegistryDelegate<StatType<VitalsStat>> SP_STAT = STAT_REGISTRAR.register("sp", () -> new StatType<>(VitalsStat::new));

  public static final RegistryDelegate<StatType<UnaryStat>> SPEED_STAT = STAT_REGISTRAR.register("speed", () -> new StatType<>(UnaryStat::new));

  private static final Registrar<Element, ElementRegistryEvent> ELEMENT_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.elements, MOD_ID);
  public static final RegistryDelegate<Element> NO_ELEMENT = ELEMENT_REGISTRAR.register("none", NoElement::new);
  public static final RegistryDelegate<Element> WATER_ELEMENT = ELEMENT_REGISTRAR.register("water", WaterElement::new);
  public static final RegistryDelegate<Element> EARTH_ELEMENT = ELEMENT_REGISTRAR.register("earth", EarthElement::new);
  public static final RegistryDelegate<Element> DARK_ELEMENT = ELEMENT_REGISTRAR.register("dark", DarkElement::new);
  public static final RegistryDelegate<Element> DIVINE_ELEMENT = ELEMENT_REGISTRAR.register("divine", DivineElement::new);
  public static final RegistryDelegate<Element> THUNDER_ELEMENT = ELEMENT_REGISTRAR.register("thunder", ThunderElement::new);
  public static final RegistryDelegate<Element> LIGHT_ELEMENT = ELEMENT_REGISTRAR.register("light", LightElement::new);
  public static final RegistryDelegate<Element> WIND_ELEMENT = ELEMENT_REGISTRAR.register("wind", WindElement::new);
  public static final RegistryDelegate<Element> FIRE_ELEMENT = ELEMENT_REGISTRAR.register("fire", FireElement::new);

  private static final Registrar<BattleObjectType, BattleObjectTypeRegistryEvent> BOBJ_TYPE_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.battleObjectTypes, MOD_ID);
  public static final RegistryDelegate<BattleObjectType> PLAYER_TYPE = BOBJ_TYPE_REGISTRAR.register("player", BattleObjectType::new);
  public static final RegistryDelegate<BattleObjectType> MONSTER_TYPE = BOBJ_TYPE_REGISTRAR.register("monster", BattleObjectType::new);

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> CONFIG_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.config, MOD_ID);
  public static final RegistryDelegate<IndicatorModeConfigEntry> INDICATOR_MODE_CONFIG = CONFIG_REGISTRAR.register("indicator_mode", IndicatorModeConfigEntry::new);
  public static final RegistryDelegate<InventorySizeConfigEntry> INVENTORY_SIZE_CONFIG = CONFIG_REGISTRAR.register("inventory_size", InventorySizeConfigEntry::new);
  public static final RegistryDelegate<EncounterRateConfigEntry> ENCOUNTER_RATE_CONFIG = CONFIG_REGISTRAR.register("encounter_rate", EncounterRateConfigEntry::new);
  public static final RegistryDelegate<RenderScaleConfigEntry> RENDER_SCALE_CONFIG = CONFIG_REGISTRAR.register("render_scale", RenderScaleConfigEntry::new);
  public static final RegistryDelegate<AutoAdditionConfigEntry> AUTO_ADDITION_CONFIG = CONFIG_REGISTRAR.register("auto_addition", AutoAdditionConfigEntry::new);
  public static final RegistryDelegate<AdditionOverlayConfigEntry> ADDITION_OVERLAY_CONFIG = CONFIG_REGISTRAR.register("addition_overlay_mode", AdditionOverlayConfigEntry::new);

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
  public static void registerElements(final ElementRegistryEvent event) {
    ELEMENT_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerBobjTypes(final BattleObjectTypeRegistryEvent event) {
    BOBJ_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerBobjStats(final RegisterBattleObjectStatsEvent event) {
    event.addStat(HP_STAT.get());

    if(event.type == PLAYER_TYPE.get()) {
      event.addStat(MP_STAT.get());
      event.addStat(SP_STAT.get());
    }

    event.addStat(SPEED_STAT.get());
  }

  public static void handleGameLoaded(final GameLoadedEvent event) {
    GameEngine.GPU.rescale(event.gameState.getConfig(RENDER_SCALE_CONFIG.get()));
  }
}
