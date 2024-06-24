package legend.game.modding.coremod;

import legend.core.GameEngine;
import legend.game.characters.Addition04;
import legend.game.characters.Element;
import legend.game.characters.ElementRegistryEvent;
import legend.game.characters.StatType;
import legend.game.characters.StatTypeRegistryEvent;
import legend.game.characters.UnaryStat;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.BattleEntityType;
import legend.game.combat.bent.BattleEntityTypeRegistryEvent;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.combat.formula.Formula;
import legend.game.combat.formula.PhysicalDamageFormula;
import legend.game.combat.types.AdditionHitProperties10;
import legend.game.combat.types.AdditionHits80;
import legend.game.input.InputAction;
import legend.game.modding.coremod.character.CharacterData;
import legend.game.modding.coremod.config.AdditionModeConfigEntry;
import legend.game.modding.coremod.config.AdditionOverlayConfigEntry;
import legend.game.modding.coremod.config.AllowWidescreenConfigEntry;
import legend.game.modding.coremod.config.AudioDeviceConfig;
import legend.game.modding.coremod.config.ControllerConfigEntry;
import legend.game.modding.coremod.config.ControllerDeadzoneConfigEntry;
import legend.game.modding.coremod.config.ControllerKeybindConfigEntry;
import legend.game.modding.coremod.config.ControllerKeybindsConfigEntry;
import legend.game.modding.coremod.config.EnabledModsConfigEntry;
import legend.game.modding.coremod.config.EncounterRateConfigEntry;
import legend.game.modding.coremod.config.FullscreenConfigEntry;
import legend.game.modding.coremod.config.HighQualityProjectionConfigEntry;
import legend.game.modding.coremod.config.IndicatorModeConfigEntry;
import legend.game.modding.coremod.config.InventorySizeConfigEntry;
import legend.game.modding.coremod.config.MusicVolumeConfigEntry;
import legend.game.modding.coremod.config.ResolutionConfig;
import legend.game.modding.coremod.config.TransformationModeConfigEntry;
import legend.game.modding.coremod.elements.DarkElement;
import legend.game.modding.coremod.elements.DivineElement;
import legend.game.modding.coremod.elements.EarthElement;
import legend.game.modding.coremod.elements.FireElement;
import legend.game.modding.coremod.elements.LightElement;
import legend.game.modding.coremod.elements.NoElement;
import legend.game.modding.coremod.elements.ThunderElement;
import legend.game.modding.coremod.elements.WaterElement;
import legend.game.modding.coremod.elements.WindElement;
import legend.game.modding.events.battle.RegisterBattleEntityStatsEvent;
import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistryEvent;
import legend.game.saves.ConfigStorageLocation;
import legend.game.types.LevelStuff08;
import legend.game.types.MagicStuff08;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.Mod;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

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

  private static final Registrar<BattleEntityType, BattleEntityTypeRegistryEvent> BENT_TYPE_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.battleEntityTypes, MOD_ID);
  public static final RegistryDelegate<BattleEntityType> PLAYER_TYPE = BENT_TYPE_REGISTRAR.register("player", BattleEntityType::new);
  public static final RegistryDelegate<BattleEntityType> MONSTER_TYPE = BENT_TYPE_REGISTRAR.register("monster", BattleEntityType::new);

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> CONFIG_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.config, MOD_ID);

  // Global config
  public static final RegistryDelegate<ControllerConfigEntry> CONTROLLER_CONFIG = CONFIG_REGISTRAR.register("controller", ControllerConfigEntry::new);
  public static final RegistryDelegate<ControllerDeadzoneConfigEntry> CONTROLLER_DEADZONE_CONFIG = CONFIG_REGISTRAR.register("controller_deadzone", ControllerDeadzoneConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG = CONFIG_REGISTRAR.register("receive_input_on_inactive_window", () -> new BoolConfigEntry(false, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS));
  public static final RegistryDelegate<BoolConfigEntry> RUMBLE_CONFIG = CONFIG_REGISTRAR.register("rumble", () -> new BoolConfigEntry(true, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS));
  public static final RegistryDelegate<BoolConfigEntry> ALLOW_WIDESCREEN_CONFIG = CONFIG_REGISTRAR.register("allow_widescreen", AllowWidescreenConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> HIGH_QUALITY_PROJECTION_CONFIG = CONFIG_REGISTRAR.register("high_quality_projection", HighQualityProjectionConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> FULLSCREEN_CONFIG = CONFIG_REGISTRAR.register("fullscreen", FullscreenConfigEntry::new);
  public static final RegistryDelegate<ResolutionConfig> RESOLUTION_CONFIG = CONFIG_REGISTRAR.register("resolution", ResolutionConfig::new);

  public static final RegistryDelegate<AudioDeviceConfig> AUDIO_DEVICE_CONFIG = CONFIG_REGISTRAR.register("audio_device", AudioDeviceConfig::new);
  public static final RegistryDelegate<MusicVolumeConfigEntry> MUSIC_VOLUME_CONFIG = CONFIG_REGISTRAR.register("music_volume", MusicVolumeConfigEntry::new);

  /** Config isn't actually used, but adds a button to the options screen to open the keybinds screen */
  public static final RegistryDelegate<ConfigEntry<Void>> CONTROLLER_KEYBINDS_CONFIG = CONFIG_REGISTRAR.register("controller_keybinds", ControllerKeybindsConfigEntry::new);

  public static final Map<InputAction, RegistryDelegate<ControllerKeybindConfigEntry>> KEYBIND_CONFIGS = new EnumMap<>(InputAction.class);
  static {
    KEYBIND_CONFIGS.put(InputAction.DPAD_UP, CONFIG_REGISTRAR.register("keybind_dpad_up", () -> new ControllerKeybindConfigEntry(GLFW_KEY_UP)));
    KEYBIND_CONFIGS.put(InputAction.DPAD_RIGHT, CONFIG_REGISTRAR.register("keybind_dpad_right", () -> new ControllerKeybindConfigEntry(GLFW_KEY_RIGHT)));
    KEYBIND_CONFIGS.put(InputAction.DPAD_DOWN, CONFIG_REGISTRAR.register("keybind_dpad_down", () -> new ControllerKeybindConfigEntry(GLFW_KEY_DOWN)));
    KEYBIND_CONFIGS.put(InputAction.DPAD_LEFT, CONFIG_REGISTRAR.register("keybind_dpad_left", () -> new ControllerKeybindConfigEntry(GLFW_KEY_LEFT)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_NORTH, CONFIG_REGISTRAR.register("keybind_triangle", () -> new ControllerKeybindConfigEntry(GLFW_KEY_W)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_EAST, CONFIG_REGISTRAR.register("keybind_circle", () -> new ControllerKeybindConfigEntry(GLFW_KEY_D, GLFW_KEY_ESCAPE)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SOUTH, CONFIG_REGISTRAR.register("keybind_cross", () -> new ControllerKeybindConfigEntry(GLFW_KEY_S, GLFW_KEY_ENTER)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_WEST, CONFIG_REGISTRAR.register("keybind_square", () -> new ControllerKeybindConfigEntry(GLFW_KEY_A)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_CENTER_1, CONFIG_REGISTRAR.register("keybind_select", () -> new ControllerKeybindConfigEntry(GLFW_KEY_SPACE)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_CENTER_2, CONFIG_REGISTRAR.register("keybind_start", () -> new ControllerKeybindConfigEntry(GLFW_KEY_ENTER)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_LEFT_1, CONFIG_REGISTRAR.register("keybind_l1", () -> new ControllerKeybindConfigEntry(GLFW_KEY_Q)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_RIGHT_1, CONFIG_REGISTRAR.register("keybind_r1", () -> new ControllerKeybindConfigEntry(GLFW_KEY_E)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_LEFT_2, CONFIG_REGISTRAR.register("keybind_l2", () -> new ControllerKeybindConfigEntry(GLFW_KEY_1)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_RIGHT_2, CONFIG_REGISTRAR.register("keybind_r2", () -> new ControllerKeybindConfigEntry(GLFW_KEY_3)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_THUMB_1, CONFIG_REGISTRAR.register("keybind_l3", () -> new ControllerKeybindConfigEntry(GLFW_KEY_Z)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_THUMB_2, CONFIG_REGISTRAR.register("keybind_r3", () -> new ControllerKeybindConfigEntry(GLFW_KEY_C)));
  }
  public static int MAX_CHARACTER_LEVEL = 60;
  public static int MAX_DRAGOON_LEVEL = 5;
  public static int MAX_ADDITION_LEVEL = 5;
  public static int ADDITIONS_PER_LEVEL = 20;
  public static CharacterData[] CHARACTER_DATA = new CharacterData[9];

  // Per-campaign config
  public static final RegistryDelegate<EnabledModsConfigEntry> ENABLED_MODS_CONFIG = CONFIG_REGISTRAR.register("enabled_mods", EnabledModsConfigEntry::new);
  public static final RegistryDelegate<IndicatorModeConfigEntry> INDICATOR_MODE_CONFIG = CONFIG_REGISTRAR.register("indicator_mode", IndicatorModeConfigEntry::new);
  public static final RegistryDelegate<InventorySizeConfigEntry> INVENTORY_SIZE_CONFIG = CONFIG_REGISTRAR.register("inventory_size", InventorySizeConfigEntry::new);
  public static final RegistryDelegate<EncounterRateConfigEntry> ENCOUNTER_RATE_CONFIG = CONFIG_REGISTRAR.register("encounter_rate", EncounterRateConfigEntry::new);
  public static final RegistryDelegate<AdditionModeConfigEntry> ADDITION_MODE_CONFIG = CONFIG_REGISTRAR.register("addition_mode", AdditionModeConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> AUTO_DRAGOON_ADDITION_CONFIG = CONFIG_REGISTRAR.register("auto_dragoon_addition", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<AdditionOverlayConfigEntry> ADDITION_OVERLAY_CONFIG = CONFIG_REGISTRAR.register("addition_overlay_mode", AdditionOverlayConfigEntry::new);
  public static final RegistryDelegate<TransformationModeConfigEntry> TRANSFORMATION_MODE_CONFIG = CONFIG_REGISTRAR.register("transformation_mode", TransformationModeConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> QUICK_TEXT_CONFIG = CONFIG_REGISTRAR.register("quick_text", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<BoolConfigEntry> AUTO_TEXT_CONFIG = CONFIG_REGISTRAR.register("auto_text", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<BoolConfigEntry> SAVE_ANYWHERE_CONFIG = CONFIG_REGISTRAR.register("save_anywhere", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<BoolConfigEntry> DISABLE_STATUS_EFFECTS_CONFIG = CONFIG_REGISTRAR.register("disable_status_effects", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<BoolConfigEntry> ENEMY_HP_BARS_CONFIG = CONFIG_REGISTRAR.register("enemy_hp_bars", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));

  public static final Formula<Integer, Integer> PHYSICAL_DAMAGE_FORMULA = Formula.make(PhysicalDamageFormula::calculatePhysicalDamage, builder -> builder
    .then(PhysicalDamageFormula::applyElementalInteractions)
    .then(PhysicalDamageFormula::applyPower)
    .then(PhysicalDamageFormula::applyDragoonSpace)
    .then(PhysicalDamageFormula.minimum(0))
    .then(PhysicalDamageFormula::applyDamageMultipliers)
    .then(PhysicalDamageFormula::applyAttackEffects)
    .then(PhysicalDamageFormula.minimum(1))
    .then(PhysicalDamageFormula::applyResistanceAndImmunity)
    .then(PhysicalDamageFormula::applyElementalResistanceAndImmunity)
  );

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
  public static void registerBentTypes(final BattleEntityTypeRegistryEvent event) {
    BENT_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerBentStats(final RegisterBattleEntityStatsEvent event) {
    event.addStat(HP_STAT.get());

    if(event.type == PLAYER_TYPE.get()) {
      event.addStat(MP_STAT.get());
      event.addStat(SP_STAT.get());
    }

    event.addStat(SPEED_STAT.get());
  }

  public static void loadCharacterXp(final int charIndex, final String charName) throws IOException {
    final FileData file = new FileData(Files.readAllBytes(Paths.get("./files/characters/" + charName + "/xp")));
    for(int i = 0; i < CoreMod.CHARACTER_DATA[charIndex].xpTable.length; i++) {
      CoreMod.CHARACTER_DATA[charIndex].xpTable[i] = file.readInt(i * 4);
    }
  }

  public static void loadCharacterStats(final int charIndex, final String charName) throws IOException {
    final FileData file = new FileData(Files.readAllBytes(Paths.get("./files/characters/" + charName + "/stats")));
    for(int i = 0; i < CoreMod.CHARACTER_DATA[charIndex].statsTable.length; i++) {
      CoreMod.CHARACTER_DATA[charIndex].statsTable[i] = new LevelStuff08(file.readUShort(i * 8), file.readByte(i * 8 + 2), file.readUByte(i * 8 + 3), file.readUByte(i * 8 + 4), file.readUByte(i * 8 + 5), file.readUByte(i * 8 + 6), file.readUByte(i * 8 + 7));
    }
  }

  public static void loadCharacterDragoonXp(final int charIndex, final String charName) throws IOException {
    final FileData file = new FileData(Files.readAllBytes(Paths.get("./files/characters/" + charName + "/dxp")));
    for(int i = 0; i < CoreMod.CHARACTER_DATA[charIndex].dxpTable.length; i++) {
      CoreMod.CHARACTER_DATA[charIndex].dxpTable[i] = file.readUShort(i * 2);
    }
  }

  public static void loadCharacterDragoonStats(final int charIndex, final String charName) throws IOException {
    final FileData file = new FileData(Files.readAllBytes(Paths.get("./files/characters/" + charName + "/dstats")));
    for(int i = 0; i < CoreMod.CHARACTER_DATA[charIndex].dragoonStatsTable.length; i++) {
      CoreMod.CHARACTER_DATA[charIndex].dragoonStatsTable[i] = new MagicStuff08(file.readUShort(i * 8), file.readByte(i * 8 + 2), file.readUByte(i * 8 + 3), file.readUByte(i * 8 + 4), file.readUByte(i * 8 + 5), file.readUByte(i * 8 + 6), file.readUByte(i * 8 + 7));
    }
  }

  public static void loadCharacterAdditions(final int charIndex, final String charName, final int additions) throws IOException {
    final FileData hit = new FileData(Files.readAllBytes(Paths.get("./files/characters/" + charName +"/additionhit")));
    final FileData multiplier = new FileData(Files.readAllBytes(Paths.get("./files/characters/" + charName + "/additionmultipler")));

    for(int i = 0; i < additions; i++) {
      final AdditionHitProperties10[] hits = new AdditionHitProperties10[8];
      final Addition04[] multipliers = new Addition04[6];
      for(int x = 0; x < 8; x++) {
        hits[x] = new AdditionHitProperties10();
        for(int y = 0; y < 16; y++) {
          if(y == 8) {
            final int panDistance = hit.readUByte((i * 128) + (x * 16) + y);
            hits[x].set(y, panDistance > 127 ? panDistance - 255 : panDistance);
          } else {
            hits[x].set(y, hit.readUByte((i * 128) + (x * 16) + y));
          }
        }
      }
      for(int y = 0; y < 6; y++) {
        multipliers[y] = new Addition04();
        multipliers[y]._00 = multiplier.readUByte((i * 24) + (y * 4));
        multipliers[y].spMultiplier_02 = multiplier.readUByte((i * 24) + (y * 4) + 2);
        multipliers[y].damageMultiplier_03 = multiplier.readUByte((i * 24) + (y * 4) + 3);
      }
      CoreMod.CHARACTER_DATA[charIndex].additions.add(new AdditionHits80(hits));
      CoreMod.CHARACTER_DATA[charIndex].additionsMultiplier.add(multipliers);
    }

    if(charIndex != 2 && charIndex != 8) {
      final AdditionHitProperties10[] hits = new AdditionHitProperties10[8];
      for(int x = 0; x < 8; x++) {
        hits[x] = new AdditionHitProperties10();
        for(int y = 0; y < 16; y++) {
          hits[x].set(y, hit.readByte((additions * 128) + (x * 16) + y));
        }
      }
      CoreMod.CHARACTER_DATA[charIndex].dragoonAddition.add(new AdditionHits80(hits));
    } else {
      CoreMod.CHARACTER_DATA[charIndex].dragoonAddition.add(new AdditionHits80(new AdditionHitProperties10[8]));
    }

    if(charIndex == 0) {
      final AdditionHitProperties10[] hits = new AdditionHitProperties10[8];
      for(int x = 0; x < 8; x++) {
        hits[x] = new AdditionHitProperties10();
        for(int y = 0; y < 16; y++) {
          hits[x].set(y, hit.readByte((additions * 128) + (x * 16) + y));
        }
        hits[x].set(4, hits[x].get(4) * 2);
      }
      CoreMod.CHARACTER_DATA[charIndex].dragoonAddition.add(new AdditionHits80(hits));
    }
  }
}
