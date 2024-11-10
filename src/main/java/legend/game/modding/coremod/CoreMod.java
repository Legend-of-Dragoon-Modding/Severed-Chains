package legend.game.modding.coremod;

import legend.core.GameEngine;
import legend.game.combat.DragoonAdditionMode;
import legend.game.combat.formula.Formula;
import legend.game.combat.formula.PhysicalDamageFormula;
import legend.game.input.InputAction;
import legend.game.modding.coremod.config.AdditionButtonModeConfigEntry;
import legend.game.modding.coremod.config.AdditionButtonStyleConfigEntry;
import legend.game.modding.coremod.config.AdditionDifficultyConfigEntry;
import legend.game.modding.coremod.config.AdditionGroupConfigEntry;
import legend.game.modding.coremod.config.DragoonAdditionGroupConfigEntry;
import legend.game.modding.coremod.config.DragoonAdditionModeConfigEntry;
import legend.game.modding.coremod.config.GeneralAdditionGroupConfigEntry;
import legend.game.modding.coremod.config.AdditionModeConfigEntry;
import legend.game.modding.coremod.config.AdditionOverlayConfigEntry;
import legend.game.modding.coremod.config.AdditionTimingModeConfigEntry;
import legend.game.modding.coremod.config.AdditionTimingOffsetConfigEntry;
import legend.game.modding.coremod.config.PreferredBattleCameraAngleConfigEntry;
import legend.game.modding.coremod.config.AllowWidescreenConfigEntry;
import legend.game.modding.coremod.config.AudioDeviceConfig;
import legend.game.modding.coremod.config.BattleTransitionModeConfigEntry;
import legend.game.modding.coremod.config.ControllerConfigEntry;
import legend.game.modding.coremod.config.ControllerDeadzoneConfigEntry;
import legend.game.modding.coremod.config.ControllerKeybindConfigEntry;
import legend.game.modding.coremod.config.ControllerKeybindsConfigEntry;
import legend.game.modding.coremod.config.AdditionSettingsConfigEntry;
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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

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
  public static final String MOD_ID = "lod_core";
  public static ArrayList<String> configOrder = new ArrayList<String>();

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> CONFIG_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.config, MOD_ID);

  // Global config
  public static final RegistryDelegate<ControllerConfigEntry> CONTROLLER_CONFIG = register("controller", ControllerConfigEntry::new);
  public static final RegistryDelegate<ControllerDeadzoneConfigEntry> CONTROLLER_DEADZONE_CONFIG = register("controller_deadzone", ControllerDeadzoneConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG = register("receive_input_on_inactive_window", () -> new BoolConfigEntry(false, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS));
  public static final RegistryDelegate<BoolConfigEntry> RUMBLE_CONFIG = register("rumble", () -> new BoolConfigEntry(true, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS));
  public static final RegistryDelegate<BoolConfigEntry> ALLOW_WIDESCREEN_CONFIG = register("allow_widescreen", AllowWidescreenConfigEntry::new);
  public static final RegistryDelegate<SubmapWidescreenModeConfig> SUBMAP_WIDESCREEN_MODE_CONFIG = register("submap_widescreen_mode", SubmapWidescreenModeConfig::new);
  public static final RegistryDelegate<BoolConfigEntry> HIGH_QUALITY_PROJECTION_CONFIG = register("high_quality_projection", HighQualityProjectionConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> FULLSCREEN_CONFIG = register("fullscreen", FullscreenConfigEntry::new);
  public static final RegistryDelegate<ResolutionConfig> RESOLUTION_CONFIG = register("resolution", ResolutionConfig::new);

  public static final RegistryDelegate<AudioDeviceConfig> AUDIO_DEVICE_CONFIG = register("audio_device", AudioDeviceConfig::new);
  public static final RegistryDelegate<MusicVolumeConfigEntry> MUSIC_VOLUME_CONFIG = register("music_volume", MusicVolumeConfigEntry::new);

  /** Config isn't actually used, but adds a button to the options screen to open the keybinds screen */
  public static final RegistryDelegate<ConfigEntry<Void>> CONTROLLER_KEYBINDS_CONFIG = register("controller_keybinds", ControllerKeybindsConfigEntry::new);
  public static final RegistryDelegate<ConfigEntry<Void>> ADDITION_SETTINGS_CONFIG = register("addition_settings", AdditionSettingsConfigEntry::new);

  public static final Map<InputAction, RegistryDelegate<ControllerKeybindConfigEntry>> KEYBIND_CONFIGS = new EnumMap<>(InputAction.class);
  static {
    KEYBIND_CONFIGS.put(InputAction.DPAD_UP, register("keybind_dpad_up", () -> new ControllerKeybindConfigEntry(GLFW_KEY_UP)));
    KEYBIND_CONFIGS.put(InputAction.DPAD_RIGHT, register("keybind_dpad_right", () -> new ControllerKeybindConfigEntry(GLFW_KEY_RIGHT)));
    KEYBIND_CONFIGS.put(InputAction.DPAD_DOWN, register("keybind_dpad_down", () -> new ControllerKeybindConfigEntry(GLFW_KEY_DOWN)));
    KEYBIND_CONFIGS.put(InputAction.DPAD_LEFT, register("keybind_dpad_left", () -> new ControllerKeybindConfigEntry(GLFW_KEY_LEFT)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_NORTH, register("keybind_triangle", () -> new ControllerKeybindConfigEntry(GLFW_KEY_W)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_EAST, register("keybind_circle", () -> new ControllerKeybindConfigEntry(GLFW_KEY_D, GLFW_KEY_ESCAPE)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SOUTH, register("keybind_cross", () -> new ControllerKeybindConfigEntry(GLFW_KEY_S, GLFW_KEY_ENTER)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_WEST, register("keybind_square", () -> new ControllerKeybindConfigEntry(GLFW_KEY_A)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_CENTER_1, register("keybind_select", () -> new ControllerKeybindConfigEntry(GLFW_KEY_SPACE)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_CENTER_2, register("keybind_start", () -> new ControllerKeybindConfigEntry(GLFW_KEY_ENTER)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_LEFT_1, register("keybind_l1", () -> new ControllerKeybindConfigEntry(GLFW_KEY_Q)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_RIGHT_1, register("keybind_r1", () -> new ControllerKeybindConfigEntry(GLFW_KEY_E)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_LEFT_2, register("keybind_l2", () -> new ControllerKeybindConfigEntry(GLFW_KEY_1)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_SHOULDER_RIGHT_2, register("keybind_r2", () -> new ControllerKeybindConfigEntry(GLFW_KEY_3)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_THUMB_1, register("keybind_l3", () -> new ControllerKeybindConfigEntry(GLFW_KEY_Z)));
    KEYBIND_CONFIGS.put(InputAction.BUTTON_THUMB_2, register("keybind_r3", () -> new ControllerKeybindConfigEntry(GLFW_KEY_C)));
  }

  // Per-campaign config
  public static final RegistryDelegate<EnabledModsConfigEntry> ENABLED_MODS_CONFIG = register("enabled_mods", EnabledModsConfigEntry::new);
  public static final RegistryDelegate<IndicatorModeConfigEntry> INDICATOR_MODE_CONFIG = register("indicator_mode", IndicatorModeConfigEntry::new);
  public static final RegistryDelegate<InventorySizeConfigEntry> INVENTORY_SIZE_CONFIG = register("inventory_size", InventorySizeConfigEntry::new);
  public static final RegistryDelegate<EncounterRateConfigEntry> ENCOUNTER_RATE_CONFIG = register("encounter_rate", EncounterRateConfigEntry::new);
  public static final RegistryDelegate<TransformationModeConfigEntry> TRANSFORMATION_MODE_CONFIG = register("transformation_mode", TransformationModeConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> QUICK_TEXT_CONFIG = register("quick_text", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<BoolConfigEntry> AUTO_TEXT_CONFIG = register("auto_text", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<BoolConfigEntry> SAVE_ANYWHERE_CONFIG = register("save_anywhere", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<BoolConfigEntry> DISABLE_STATUS_EFFECTS_CONFIG = register("disable_status_effects", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<BoolConfigEntry> ENEMY_HP_BARS_CONFIG = register("enemy_hp_bars", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<MashModeConfigEntry> MASH_MODE_CONFIG = register("mash_mode", MashModeConfigEntry::new);
  public static final RegistryDelegate<SecondaryCharacterXpMultiplierConfigEntry> SECONDARY_CHARACTER_XP_MULTIPLIER_CONFIG = register("secondary_character_xp_multiplier", SecondaryCharacterXpMultiplierConfigEntry::new);
  public static final RegistryDelegate<BattleTransitionModeConfigEntry> BATTLE_TRANSITION_MODE_CONFIG = register("battle_transition_mode", BattleTransitionModeConfigEntry::new);
  public static final RegistryDelegate<PreferredBattleCameraAngleConfigEntry> PREFERRED_BATTLE_CAMERA_ANGLE = register("preferred_battle_camera_angle", PreferredBattleCameraAngleConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> UNLOCK_PARTY_CONFIG = register("unlock_party", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));

  public static final RegistryDelegate<ConfigEntry<Void>> GENERAL_ADDITION_GROUP_CONFIG = register("general_addition_group", GeneralAdditionGroupConfigEntry::new);
  public static final RegistryDelegate<AdditionButtonStyleConfigEntry> ADDITION_BUTTON_STYLE_CONFIG = register("addition_button_style", AdditionButtonStyleConfigEntry::new);

  public static final RegistryDelegate<ConfigEntry<Void>> ADDITION_GROUP_CONFIG = register("addition_group", AdditionGroupConfigEntry::new);
  public static final RegistryDelegate<AdditionModeConfigEntry> ADDITION_MODE_CONFIG = register("addition_mode", AdditionModeConfigEntry::new);
  public static final RegistryDelegate<AdditionDifficultyConfigEntry> ADDITION_DIFFICULTY_CONFIG = register("addition_difficulty", AdditionDifficultyConfigEntry::new);
  public static final RegistryDelegate<AdditionTimingModeConfigEntry> ADDITION_TIMING_MODE_CONFIG = register("addition_timing_mode", AdditionTimingModeConfigEntry::new);
  public static final RegistryDelegate<AdditionTimingOffsetConfigEntry> ADDITION_TIMING_OFFSET_CONFIG = register("addition_timing_offset", AdditionTimingOffsetConfigEntry::new);
  public static final RegistryDelegate<AdditionOverlayConfigEntry> ADDITION_OVERLAY_CONFIG = register("addition_overlay_mode", AdditionOverlayConfigEntry::new);
  public static final RegistryDelegate<AdditionButtonModeConfigEntry> ADDITION_BUTTON_MODE_CONFIG = register("addition_button_mode", AdditionButtonModeConfigEntry::new);

  public static final RegistryDelegate<ConfigEntry<Void>> DRAGOON_ADDITION_GROUP_CONFIG = register("dragoon_addition_group", DragoonAdditionGroupConfigEntry::new);
  public static final RegistryDelegate<DragoonAdditionModeConfigEntry> DRAGOON_ADDITION_MODE_CONFIG = register("dragoon_addition_mode", DragoonAdditionModeConfigEntry::new);

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

  private static <T extends ConfigEntry<?>> RegistryDelegate<T> register(final String entryId, final Supplier<T> entry) {
    return register(entryId, entry, MOD_ID);
  }

  public static <T extends ConfigEntry<?>> RegistryDelegate<T> register(final String entryId, final Supplier<T> entry, final String modID) {
    configOrder.add(modID + ':' + entryId);
    return CONFIG_REGISTRAR.register(entryId, entry);
  }

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  @EventListener
  public static void registerConfig(final ConfigRegistryEvent event) {
    CONFIG_REGISTRAR.registryEvent(event);
  }
}
