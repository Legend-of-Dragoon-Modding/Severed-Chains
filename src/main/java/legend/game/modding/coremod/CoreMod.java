package legend.game.modding.coremod;

import legend.core.GameEngine;
import legend.game.combat.formula.Formula;
import legend.game.combat.formula.PhysicalDamageFormula;
import legend.game.input.InputAction;
import legend.game.modding.coremod.config.AdditionModeConfigEntry;
import legend.game.modding.coremod.config.AdditionOverlayConfigEntry;
import legend.game.modding.coremod.config.AllowWidescreenConfigEntry;
import legend.game.modding.coremod.config.AudioDeviceConfig;
import legend.game.modding.coremod.config.BattleTransitionModeConfigEntry;
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
import legend.game.modding.coremod.config.MashModeConfigEntry;
import legend.game.modding.coremod.config.MusicVolumeConfigEntry;
import legend.game.modding.coremod.config.ResolutionConfig;
import legend.game.modding.coremod.config.SecondaryCharacterXpMultiplierConfigEntry;
import legend.game.modding.coremod.config.SubmapWidescreenModeConfig;
import legend.game.modding.coremod.config.TransformationModeConfigEntry;
import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistryEvent;
import legend.game.saves.ConfigStorageLocation;
import org.legendofdragoon.modloader.Mod;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

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
import static org.lwjgl.glfw.GLFW.GLFW_KEY_EQUAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F10;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F12;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS;
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
  public static final String MOD_ID = "lod_core";

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> CONFIG_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.config, MOD_ID);

  // Global config
  public static final RegistryDelegate<ControllerConfigEntry> CONTROLLER_CONFIG = CONFIG_REGISTRAR.register("controller", ControllerConfigEntry::new);
  public static final RegistryDelegate<ControllerDeadzoneConfigEntry> CONTROLLER_DEADZONE_CONFIG = CONFIG_REGISTRAR.register("controller_deadzone", ControllerDeadzoneConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG = CONFIG_REGISTRAR.register("receive_input_on_inactive_window", () -> new BoolConfigEntry(false, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS));
  public static final RegistryDelegate<BoolConfigEntry> RUMBLE_CONFIG = CONFIG_REGISTRAR.register("rumble", () -> new BoolConfigEntry(true, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS));
  public static final RegistryDelegate<BoolConfigEntry> ALLOW_WIDESCREEN_CONFIG = CONFIG_REGISTRAR.register("allow_widescreen", AllowWidescreenConfigEntry::new);
  public static final RegistryDelegate<SubmapWidescreenModeConfig> SUBMAP_WIDESCREEN_MODE_CONFIG = CONFIG_REGISTRAR.register("submap_widescreen_mode", SubmapWidescreenModeConfig::new);
  public static final RegistryDelegate<BoolConfigEntry> HIGH_QUALITY_PROJECTION_CONFIG = CONFIG_REGISTRAR.register("high_quality_projection", HighQualityProjectionConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> FULLSCREEN_CONFIG = CONFIG_REGISTRAR.register("fullscreen", FullscreenConfigEntry::new);
  public static final RegistryDelegate<ResolutionConfig> RESOLUTION_CONFIG = CONFIG_REGISTRAR.register("resolution", ResolutionConfig::new);

  public static final RegistryDelegate<AudioDeviceConfig> AUDIO_DEVICE_CONFIG = CONFIG_REGISTRAR.register("audio_device", AudioDeviceConfig::new);
  public static final RegistryDelegate<MusicVolumeConfigEntry> MUSIC_VOLUME_CONFIG = CONFIG_REGISTRAR.register("music_volume", MusicVolumeConfigEntry::new);

  /** Config isn't actually used, but adds a button to the options screen to open the keybinds screen */
  public static final RegistryDelegate<ConfigEntry<Void>> CONTROLLER_KEYBINDS_CONFIG = CONFIG_REGISTRAR.register("controller_keybinds", ControllerKeybindsConfigEntry::new);

  public static final Map<InputAction, RegistryDelegate<ControllerKeybindConfigEntry>> KEYBIND_CONFIGS = new EnumMap<>(InputAction.class);
  static {
    KEYBIND_CONFIGS.put(InputAction.DPAD_UP, CONFIG_REGISTRAR.register("keybind_dpad_up", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_UP)));
    KEYBIND_CONFIGS.put(InputAction.DPAD_RIGHT, CONFIG_REGISTRAR.register("keybind_dpad_right", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_RIGHT)));
    KEYBIND_CONFIGS.put(InputAction.DPAD_DOWN, CONFIG_REGISTRAR.register("keybind_dpad_down", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_DOWN)));
    KEYBIND_CONFIGS.put(InputAction.DPAD_LEFT, CONFIG_REGISTRAR.register("keybind_dpad_left", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_LEFT)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_NORTH, CONFIG_REGISTRAR.register("keybind_triangle", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_W)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_EAST, CONFIG_REGISTRAR.register("keybind_circle", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_D, GLFW_KEY_ESCAPE)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SOUTH, CONFIG_REGISTRAR.register("keybind_cross", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_S, GLFW_KEY_ENTER)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_WEST, CONFIG_REGISTRAR.register("keybind_square", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_A)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_CENTER_1, CONFIG_REGISTRAR.register("keybind_select", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_SPACE)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_CENTER_2, CONFIG_REGISTRAR.register("keybind_start", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_ENTER)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_LEFT_1, CONFIG_REGISTRAR.register("keybind_l1", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_Q)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_RIGHT_1, CONFIG_REGISTRAR.register("keybind_r1", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_E)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_LEFT_2, CONFIG_REGISTRAR.register("keybind_l2", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_1)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_RIGHT_2, CONFIG_REGISTRAR.register("keybind_r2", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_3)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_THUMB_1, CONFIG_REGISTRAR.register("keybind_l3", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_Z)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_THUMB_2, CONFIG_REGISTRAR.register("keybind_r3", () -> new ControllerKeybindConfigEntry(true, GLFW_KEY_C)));
    KEYBIND_CONFIGS.put(InputAction.BATTLE_DRAGOON, CONFIG_REGISTRAR.register("keybind_dragoon", () -> new ControllerKeybindConfigEntry(false)));
    KEYBIND_CONFIGS.put(InputAction.BATTLE_SPECIAL, CONFIG_REGISTRAR.register("keybind_special", () -> new ControllerKeybindConfigEntry(false)));
    KEYBIND_CONFIGS.put(InputAction.BATTLE_ESCAPE, CONFIG_REGISTRAR.register("keybind_escape", () -> new ControllerKeybindConfigEntry(false)));
    KEYBIND_CONFIGS.put(InputAction.BATTLE_GUARD, CONFIG_REGISTRAR.register("keybind_guard", () -> new ControllerKeybindConfigEntry(false)));
    KEYBIND_CONFIGS.put(InputAction.BATTLE_ITEMS, CONFIG_REGISTRAR.register("keybind_items", () -> new ControllerKeybindConfigEntry(false)));
    KEYBIND_CONFIGS.put(InputAction.SPEED_UP, CONFIG_REGISTRAR.register("keybind_speed_up", () -> new ControllerKeybindConfigEntry(false, GLFW_KEY_EQUAL)));
    KEYBIND_CONFIGS.put(InputAction.SLOW_DOWN, CONFIG_REGISTRAR.register("keybind_slow_down", () -> new ControllerKeybindConfigEntry(false, GLFW_KEY_MINUS)));
    KEYBIND_CONFIGS.put(InputAction.DEBUGGER, CONFIG_REGISTRAR.register("keybind_debugger", () -> new ControllerKeybindConfigEntry(false, GLFW_KEY_F12)));
    KEYBIND_CONFIGS.put(InputAction.PAUSE, CONFIG_REGISTRAR.register("keybind_pause", () -> new ControllerKeybindConfigEntry(false, GLFW_KEY_F11)));
    KEYBIND_CONFIGS.put(InputAction.FRAME_ADVANCE, CONFIG_REGISTRAR.register("keybind_frame_advance", () -> new ControllerKeybindConfigEntry(false, GLFW_KEY_F9)));
    KEYBIND_CONFIGS.put(InputAction.FRAME_ADVANCE_HOLD, CONFIG_REGISTRAR.register("keybind_frame_advance_hold", () -> new ControllerKeybindConfigEntry(false, GLFW_KEY_F10)));
    KEYBIND_CONFIGS.put(InputAction.KILL_STUCK_SOUNDS, CONFIG_REGISTRAR.register("keybind_kill_stuck_sounds", () -> new ControllerKeybindConfigEntry(false, GLFW_KEY_F4)));
  }

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
  public static final RegistryDelegate<MashModeConfigEntry> MASH_MODE_CONFIG = CONFIG_REGISTRAR.register("mash_mode", MashModeConfigEntry::new);
  public static final RegistryDelegate<SecondaryCharacterXpMultiplierConfigEntry> SECONDARY_CHARACTER_XP_MULTIPLIER_CONFIG = CONFIG_REGISTRAR.register("secondary_character_xp_multiplier", SecondaryCharacterXpMultiplierConfigEntry::new);
  public static final RegistryDelegate<BattleTransitionModeConfigEntry> BATTLE_TRANSITION_MODE_CONFIG = CONFIG_REGISTRAR.register("battle_transition_mode", BattleTransitionModeConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> UNLOCK_PARTY_CONFIG = CONFIG_REGISTRAR.register("unlock_party", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));

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
}
