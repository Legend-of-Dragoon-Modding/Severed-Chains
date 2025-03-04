package legend.game.modding.coremod;

import legend.core.GameEngine;
import legend.core.platform.input.AxisInputActivation;
import legend.core.platform.input.ButtonInputActivation;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputActionRegistryEvent;
import legend.core.platform.input.InputAxis;
import legend.core.platform.input.InputAxisDirection;
import legend.core.platform.input.InputButton;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.InputMod;
import legend.core.platform.input.KeyInputActivation;
import legend.core.platform.input.ScancodeInputActivation;
import legend.game.combat.formula.Formula;
import legend.game.combat.formula.PhysicalDamageFormula;
import legend.game.input.InputActionOld;
import legend.game.inventory.IconSetConfigEntry;
import legend.game.modding.coremod.config.AdditionModeConfigEntry;
import legend.game.modding.coremod.config.AdditionOverlayConfigEntry;
import legend.game.modding.coremod.config.AllowWidescreenConfigEntry;
import legend.game.modding.coremod.config.AudioDeviceConfig;
import legend.game.modding.coremod.config.BattleTransitionModeConfigEntry;
import legend.game.modding.coremod.config.ControllerDeadzoneConfigEntry;
import legend.game.modding.coremod.config.ControllerKeybindConfigEntry;
import legend.game.modding.coremod.config.ControllerKeybindsConfigEntry;
import legend.game.modding.coremod.config.CreateCrashSaveConfigEntry;
import legend.game.modding.coremod.config.DisableMouseInputConfigEntry;
import legend.game.modding.coremod.config.EnabledModsConfigEntry;
import legend.game.modding.coremod.config.EncounterRateConfigEntry;
import legend.game.modding.coremod.config.FmvVolumeConfigEntry;
import legend.game.modding.coremod.config.FullscreenConfigEntry;
import legend.game.modding.coremod.config.HighQualityProjectionConfigEntry;
import legend.game.modding.coremod.config.IndicatorModeConfigEntry;
import legend.game.modding.coremod.config.InventorySizeConfigEntry;
import legend.game.modding.coremod.config.LegacyWidescreenModeConfig;
import legend.game.modding.coremod.config.MashModeConfigEntry;
import legend.game.modding.coremod.config.MasterVolumeConfigEntry;
import legend.game.modding.coremod.config.MonitorConfigEntry;
import legend.game.modding.coremod.config.MusicEffectsOverTimeGranularityConfigEntry;
import legend.game.modding.coremod.config.MusicInterpolationPrecisionConfigEntry;
import legend.game.modding.coremod.config.MusicPitchResolutionConfigEntry;
import legend.game.modding.coremod.config.MusicSampleRateConfigEntry;
import legend.game.modding.coremod.config.MusicVolumeConfigEntry;
import legend.game.modding.coremod.config.ResolutionConfig;
import legend.game.modding.coremod.config.RunByDefaultConfig;
import legend.game.modding.coremod.config.SaveAnywhereConfig;
import legend.game.modding.coremod.config.SecondaryCharacterXpMultiplierConfigEntry;
import legend.game.modding.coremod.config.SfxVolumeConfigEntry;
import legend.game.modding.coremod.config.TransformationModeConfigEntry;
import legend.game.modding.coremod.config.UnlockPartyConfig;
import legend.game.modding.events.input.RegisterDefaultInputBindingsEvent;
import legend.game.saves.BoolConfigEntry;
import legend.game.saves.CampaignNameConfigEntry;
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

/** Core mod that contains engine-level content. Game can not run without it. */
@Mod(id = CoreMod.MOD_ID, version = "^3.0.0")
@EventListener
public class CoreMod {
  public static final String MOD_ID = "lod_core";

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> CONFIG_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.config, MOD_ID);

  // Global config
  public static final RegistryDelegate<ControllerDeadzoneConfigEntry> CONTROLLER_DEADZONE_CONFIG = CONFIG_REGISTRAR.register("controller_deadzone", ControllerDeadzoneConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> RECEIVE_INPUT_ON_INACTIVE_WINDOW_CONFIG = CONFIG_REGISTRAR.register("receive_input_on_inactive_window", () -> new BoolConfigEntry(false, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS));
  public static final RegistryDelegate<BoolConfigEntry> DISABLE_MOUSE_INPUT_CONFIG = CONFIG_REGISTRAR.register("disable_mouse_input", DisableMouseInputConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> RUMBLE_CONFIG = CONFIG_REGISTRAR.register("rumble", () -> new BoolConfigEntry(true, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS));
  public static final RegistryDelegate<BoolConfigEntry> ALLOW_WIDESCREEN_CONFIG = CONFIG_REGISTRAR.register("allow_widescreen", AllowWidescreenConfigEntry::new);
  public static final RegistryDelegate<LegacyWidescreenModeConfig> LEGACY_WIDESCREEN_MODE_CONFIG = CONFIG_REGISTRAR.register("submap_widescreen_mode", LegacyWidescreenModeConfig::new);
  public static final RegistryDelegate<BoolConfigEntry> HIGH_QUALITY_PROJECTION_CONFIG = CONFIG_REGISTRAR.register("high_quality_projection", HighQualityProjectionConfigEntry::new);
  public static final RegistryDelegate<BoolConfigEntry> FULLSCREEN_CONFIG = CONFIG_REGISTRAR.register("fullscreen", FullscreenConfigEntry::new);
  public static final RegistryDelegate<ResolutionConfig> RESOLUTION_CONFIG = CONFIG_REGISTRAR.register("resolution", ResolutionConfig::new);
  public static final RegistryDelegate<MonitorConfigEntry> MONITOR_CONFIG = CONFIG_REGISTRAR.register("monitor", MonitorConfigEntry::new);

  public static final RegistryDelegate<AudioDeviceConfig> AUDIO_DEVICE_CONFIG = CONFIG_REGISTRAR.register("audio_device", AudioDeviceConfig::new);
  public static final RegistryDelegate<MasterVolumeConfigEntry> MASTER_VOLUME_CONFIG = CONFIG_REGISTRAR.register("master_volume", MasterVolumeConfigEntry::new);
  public static final RegistryDelegate<MusicVolumeConfigEntry> MUSIC_VOLUME_CONFIG = CONFIG_REGISTRAR.register("music_volume", MusicVolumeConfigEntry::new);
  public static final RegistryDelegate<SfxVolumeConfigEntry> SFX_VOLUME_CONFIG = CONFIG_REGISTRAR.register("sfx_volume", SfxVolumeConfigEntry::new);
  public static final RegistryDelegate<FmvVolumeConfigEntry> FMV_VOLUME_CONFIG = CONFIG_REGISTRAR.register("fmv_volume", FmvVolumeConfigEntry::new);
  public static final RegistryDelegate<MusicInterpolationPrecisionConfigEntry> MUSIC_INTERPOLATION_PRECISION_CONFIG = CONFIG_REGISTRAR.register("music_interpolation_precision", MusicInterpolationPrecisionConfigEntry::new);
  public static final RegistryDelegate<MusicPitchResolutionConfigEntry> MUSIC_PITCH_RESOLUTION_CONFIG = CONFIG_REGISTRAR.register("music_pitch_resolution", MusicPitchResolutionConfigEntry::new);
  public static final RegistryDelegate<MusicSampleRateConfigEntry> MUSIC_SAMPLE_RATE_CONFIG = CONFIG_REGISTRAR.register("music_sample_rate", MusicSampleRateConfigEntry::new);
  public static final RegistryDelegate<MusicEffectsOverTimeGranularityConfigEntry> MUSIC_EFFECTS_OVER_TIME_GRANULARITY_CONFIG = CONFIG_REGISTRAR.register("music_effects_over_time_granularity", MusicEffectsOverTimeGranularityConfigEntry::new);
  public static final RegistryDelegate<CreateCrashSaveConfigEntry> CREATE_CRASH_SAVE_CONFIG = CONFIG_REGISTRAR.register("create_crash_save", CreateCrashSaveConfigEntry::new);

  /** Config isn't actually used, but adds a button to the options screen to open the keybinds screen */
  public static final RegistryDelegate<ConfigEntry<Void>> CONTROLLER_KEYBINDS_CONFIG = CONFIG_REGISTRAR.register("controller_keybinds", ControllerKeybindsConfigEntry::new);

  public static final Map<InputActionOld, RegistryDelegate<ControllerKeybindConfigEntry>> KEYBIND_CONFIGS = new EnumMap<>(InputActionOld.class);

  // Per-campaign config
  public static final RegistryDelegate<CampaignNameConfigEntry> CAMPAIGN_NAME = CONFIG_REGISTRAR.register("campaign_name", CampaignNameConfigEntry::new);
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
  public static final RegistryDelegate<SaveAnywhereConfig> SAVE_ANYWHERE_CONFIG = CONFIG_REGISTRAR.register("save_anywhere", SaveAnywhereConfig::new);
  public static final RegistryDelegate<BoolConfigEntry> DISABLE_STATUS_EFFECTS_CONFIG = CONFIG_REGISTRAR.register("disable_status_effects", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<BoolConfigEntry> ENEMY_HP_BARS_CONFIG = CONFIG_REGISTRAR.register("enemy_hp_bars", () -> new BoolConfigEntry(false, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY));
  public static final RegistryDelegate<MashModeConfigEntry> MASH_MODE_CONFIG = CONFIG_REGISTRAR.register("mash_mode", MashModeConfigEntry::new);
  public static final RegistryDelegate<SecondaryCharacterXpMultiplierConfigEntry> SECONDARY_CHARACTER_XP_MULTIPLIER_CONFIG = CONFIG_REGISTRAR.register("secondary_character_xp_multiplier", SecondaryCharacterXpMultiplierConfigEntry::new);
  public static final RegistryDelegate<BattleTransitionModeConfigEntry> BATTLE_TRANSITION_MODE_CONFIG = CONFIG_REGISTRAR.register("battle_transition_mode", BattleTransitionModeConfigEntry::new);
  public static final RegistryDelegate<UnlockPartyConfig> UNLOCK_PARTY_CONFIG = CONFIG_REGISTRAR.register("unlock_party", UnlockPartyConfig::new);
  public static final RegistryDelegate<IconSetConfigEntry> ICON_SET = CONFIG_REGISTRAR.register("icon_set", IconSetConfigEntry::new);
  public static final RegistryDelegate<RunByDefaultConfig> RUN_BY_DEFAULT = CONFIG_REGISTRAR.register("run_by_default", RunByDefaultConfig::new);

  private static final Registrar<InputAction, InputActionRegistryEvent> INPUT_ACTION_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.inputActions, MOD_ID);

  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_UP = INPUT_ACTION_REGISTRAR.register("menu_up", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_DOWN = INPUT_ACTION_REGISTRAR.register("menu_down", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_LEFT = INPUT_ACTION_REGISTRAR.register("menu_left", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_RIGHT = INPUT_ACTION_REGISTRAR.register("menu_right", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_HOME = INPUT_ACTION_REGISTRAR.register("menu_home", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_END = INPUT_ACTION_REGISTRAR.register("menu_end", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_PAGE_UP = INPUT_ACTION_REGISTRAR.register("menu_page_up", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_PAGE_DOWN = INPUT_ACTION_REGISTRAR.register("menu_page_down", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_CONFIRM = INPUT_ACTION_REGISTRAR.register("menu_confirm", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_BACK = INPUT_ACTION_REGISTRAR.register("menu_back", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_DELETE = INPUT_ACTION_REGISTRAR.register("menu_delete", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_SORT = INPUT_ACTION_REGISTRAR.register("menu_sort", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_HELP = INPUT_ACTION_REGISTRAR.register("menu_help", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_MENU_MODS = INPUT_ACTION_REGISTRAR.register("menu_mods", InputAction::new);

  public static final RegistryDelegate<InputAction> INPUT_ACTION_FMV_SKIP = INPUT_ACTION_REGISTRAR.register("fmv_skip", InputAction::new);

  public static final RegistryDelegate<InputAction> INPUT_ACTION_GENERAL_TOGGLE_FULLSCREEN = INPUT_ACTION_REGISTRAR.register("general_toggle_fullscreen", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_GENERAL_SPEED_UP = INPUT_ACTION_REGISTRAR.register("general_speed_up", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_GENERAL_SLOW_DOWN = INPUT_ACTION_REGISTRAR.register("general_slow_down", InputAction::new);

  public static final RegistryDelegate<InputAction> INPUT_ACTION_FREECAM_TOGGLE = INPUT_ACTION_REGISTRAR.register("freecam_toggle", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_FREECAM_FORWARD = INPUT_ACTION_REGISTRAR.register("freecam_forward", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_FREECAM_BACKWARD = INPUT_ACTION_REGISTRAR.register("freecam_backward", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_FREECAM_LEFT = INPUT_ACTION_REGISTRAR.register("freecam_left", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_FREECAM_RIGHT = INPUT_ACTION_REGISTRAR.register("freecam_right", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_FREECAM_UP = INPUT_ACTION_REGISTRAR.register("freecam_up", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_FREECAM_DOWN = INPUT_ACTION_REGISTRAR.register("freecam_down", InputAction::new);

  public static final RegistryDelegate<InputAction> INPUT_ACTION_DEBUG_OPEN_DEBUGGER = INPUT_ACTION_REGISTRAR.register("debug_open_debugger", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_DEBUG_PAUSE = INPUT_ACTION_REGISTRAR.register("debug_pause", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_DEBUG_FRAME_ADVANCE = INPUT_ACTION_REGISTRAR.register("debug_frame_advance", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_DEBUG_FRAME_ADVANCE_HOLD = INPUT_ACTION_REGISTRAR.register("debug_frame_advance_hold", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_DEBUG_TOGGLE_WIREFRAME = INPUT_ACTION_REGISTRAR.register("debug_toggle_wireframe", InputAction::new);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_DEBUG_RELOAD_SHADERS = INPUT_ACTION_REGISTRAR.register("debug_reload_shaders", InputAction::new);

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
  public static void registerInputActions(final InputActionRegistryEvent event) {
    INPUT_ACTION_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerDefaultInputBindings(final RegisterDefaultInputBindingsEvent event) {
    event
      .add(INPUT_ACTION_MENU_UP.get(), new ButtonInputActivation(InputButton.DPAD_UP))
      .add(INPUT_ACTION_MENU_UP.get(), new AxisInputActivation(InputAxis.LEFT_Y, InputAxisDirection.NEGATIVE, 0.5f, 0.6f))
      .add(INPUT_ACTION_MENU_UP.get(), new ScancodeInputActivation(InputKey.UP))
      .add(INPUT_ACTION_MENU_UP.get(), new ScancodeInputActivation(InputKey.W))
      .add(INPUT_ACTION_MENU_DOWN.get(), new ButtonInputActivation(InputButton.DPAD_DOWN))
      .add(INPUT_ACTION_MENU_DOWN.get(), new AxisInputActivation(InputAxis.LEFT_Y, InputAxisDirection.POSITIVE, 0.5f, 0.6f))
      .add(INPUT_ACTION_MENU_DOWN.get(), new ScancodeInputActivation(InputKey.DOWN))
      .add(INPUT_ACTION_MENU_DOWN.get(), new ScancodeInputActivation(InputKey.S))
      .add(INPUT_ACTION_MENU_LEFT.get(), new ButtonInputActivation(InputButton.DPAD_LEFT))
      .add(INPUT_ACTION_MENU_LEFT.get(), new AxisInputActivation(InputAxis.LEFT_X, InputAxisDirection.NEGATIVE, 0.5f, 0.6f))
      .add(INPUT_ACTION_MENU_LEFT.get(), new ScancodeInputActivation(InputKey.LEFT))
      .add(INPUT_ACTION_MENU_LEFT.get(), new ScancodeInputActivation(InputKey.A))
      .add(INPUT_ACTION_MENU_RIGHT.get(), new ButtonInputActivation(InputButton.DPAD_RIGHT))
      .add(INPUT_ACTION_MENU_RIGHT.get(), new AxisInputActivation(InputAxis.LEFT_X, InputAxisDirection.POSITIVE, 0.5f, 0.6f))
      .add(INPUT_ACTION_MENU_RIGHT.get(), new ScancodeInputActivation(InputKey.RIGHT))
      .add(INPUT_ACTION_MENU_RIGHT.get(), new ScancodeInputActivation(InputKey.D))
      .add(INPUT_ACTION_MENU_HOME.get(), new AxisInputActivation(InputAxis.LEFT_TRIGGER, InputAxisDirection.POSITIVE, 0.5f, 0.6f))
      .add(INPUT_ACTION_MENU_HOME.get(), new KeyInputActivation(InputKey.HOME))
      .add(INPUT_ACTION_MENU_END.get(), new AxisInputActivation(InputAxis.RIGHT_TRIGGER, InputAxisDirection.POSITIVE, 0.5f, 0.6f))
      .add(INPUT_ACTION_MENU_END.get(), new KeyInputActivation(InputKey.END))
      .add(INPUT_ACTION_MENU_PAGE_UP.get(), new ButtonInputActivation(InputButton.LEFT_BUMPER))
      .add(INPUT_ACTION_MENU_PAGE_UP.get(), new KeyInputActivation(InputKey.PAGE_UP))
      .add(INPUT_ACTION_MENU_PAGE_DOWN.get(), new ButtonInputActivation(InputButton.RIGHT_BUMPER))
      .add(INPUT_ACTION_MENU_PAGE_DOWN.get(), new KeyInputActivation(InputKey.PAGE_DOWN))
      .add(INPUT_ACTION_MENU_CONFIRM.get(), new ButtonInputActivation(InputButton.A))
      .add(INPUT_ACTION_MENU_CONFIRM.get(), new KeyInputActivation(InputKey.RETURN))
      .add(INPUT_ACTION_MENU_BACK.get(), new ButtonInputActivation(InputButton.B))
      .add(INPUT_ACTION_MENU_BACK.get(), new KeyInputActivation(InputKey.ESCAPE))
      .add(INPUT_ACTION_MENU_DELETE.get(), new ButtonInputActivation(InputButton.X))
      .add(INPUT_ACTION_MENU_DELETE.get(), new ScancodeInputActivation(InputKey.Q))
      .add(INPUT_ACTION_MENU_SORT.get(), new ButtonInputActivation(InputButton.Y))
      .add(INPUT_ACTION_MENU_SORT.get(), new KeyInputActivation(InputKey.X))
      .add(INPUT_ACTION_MENU_HELP.get(), new ButtonInputActivation(InputButton.START))
      .add(INPUT_ACTION_MENU_HELP.get(), new KeyInputActivation(InputKey.H))
      .add(INPUT_ACTION_MENU_MODS.get(), new ButtonInputActivation(InputButton.Y))
      .add(INPUT_ACTION_MENU_MODS.get(), new KeyInputActivation(InputKey.M))

      .add(INPUT_ACTION_FMV_SKIP.get(), new KeyInputActivation(InputKey.RETURN))
      .add(INPUT_ACTION_FMV_SKIP.get(), new ButtonInputActivation(InputButton.Y))

      .add(INPUT_ACTION_GENERAL_TOGGLE_FULLSCREEN.get(), new KeyInputActivation(InputKey.RETURN, InputMod.ALT))
      .add(INPUT_ACTION_GENERAL_SPEED_UP.get(), new ScancodeInputActivation(InputKey.EQUALS))
      .add(INPUT_ACTION_GENERAL_SPEED_UP.get(), new KeyInputActivation(InputKey.KP_PLUS))
      .add(INPUT_ACTION_GENERAL_SLOW_DOWN.get(), new ScancodeInputActivation(InputKey.MINUS))
      .add(INPUT_ACTION_GENERAL_SLOW_DOWN.get(), new KeyInputActivation(InputKey.KP_MINUS))

      .add(INPUT_ACTION_FREECAM_TOGGLE.get(), new KeyInputActivation(InputKey.M, InputMod.CTRL))
      .add(INPUT_ACTION_FREECAM_FORWARD.get(), new ScancodeInputActivation(InputKey.I))
      .add(INPUT_ACTION_FREECAM_BACKWARD.get(), new ScancodeInputActivation(InputKey.K))
      .add(INPUT_ACTION_FREECAM_LEFT.get(), new ScancodeInputActivation(InputKey.J))
      .add(INPUT_ACTION_FREECAM_RIGHT.get(), new ScancodeInputActivation(InputKey.L))
      .add(INPUT_ACTION_FREECAM_UP.get(), new ScancodeInputActivation(InputKey.O))
      .add(INPUT_ACTION_FREECAM_DOWN.get(), new ScancodeInputActivation(InputKey.U))

      .add(INPUT_ACTION_DEBUG_OPEN_DEBUGGER.get(), new KeyInputActivation(InputKey.F12))
      .add(INPUT_ACTION_DEBUG_PAUSE.get(), new KeyInputActivation(InputKey.F11))
      .add(INPUT_ACTION_DEBUG_FRAME_ADVANCE.get(), new KeyInputActivation(InputKey.F9))
      .add(INPUT_ACTION_DEBUG_FRAME_ADVANCE_HOLD.get(), new KeyInputActivation(InputKey.F10))
      .add(INPUT_ACTION_DEBUG_TOGGLE_WIREFRAME.get(), new KeyInputActivation(InputKey.F2))
      .add(INPUT_ACTION_DEBUG_RELOAD_SHADERS.get(), new KeyInputActivation(InputKey.F5))
    ;
  }
}
