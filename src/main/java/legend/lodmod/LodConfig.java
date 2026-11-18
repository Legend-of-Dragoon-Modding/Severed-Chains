package legend.lodmod;

import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistryEvent;
import legend.lodmod.config.AdditionCounterAttackGuideColourConfig;
import legend.lodmod.config.AdditionCounterAttackShieldColourConfig;
import legend.lodmod.config.AdditionGuideColourConfig;
import legend.lodmod.config.DabasSaveDataConfig;
import legend.lodmod.config.ExtendedDragoonActionsConfig;
import legend.lodmod.config.ItemStackSizeConfig;
import legend.lodmod.config.MaxDragoonLevelConfig;
import legend.lodmod.config.MaxLevelConfig;
import legend.lodmod.config.UiBackgroundColourConfig;
import legend.lodmod.config.UiSelectionColourConfig;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.core.GameEngine.REGISTRIES;

public final class LodConfig {
  private LodConfig() { }

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> REGISTRAR = new Registrar<>(REGISTRIES.config, LodMod.MOD_ID);

  public static final RegistryDelegate<MaxLevelConfig> MAX_LEVEL = REGISTRAR.register("max_level", MaxLevelConfig::new);
  public static final RegistryDelegate<MaxDragoonLevelConfig> MAX_DRAGOON_LEVEL = REGISTRAR.register("max_dragoon_level", MaxDragoonLevelConfig::new);
  public static final RegistryDelegate<ItemStackSizeConfig> ITEM_STACK_SIZE = REGISTRAR.register("item_stack_size", ItemStackSizeConfig::new);
  public static final RegistryDelegate<ExtendedDragoonActionsConfig> EXTENDED_DRAGOON_ACTIONS = REGISTRAR.register("extended_dragoon_actions", ExtendedDragoonActionsConfig::new);
  public static final RegistryDelegate<UiBackgroundColourConfig> UI_BACKGROUND_COLOUR = REGISTRAR.register("ui_background_colour", UiBackgroundColourConfig::new);
  public static final RegistryDelegate<UiSelectionColourConfig> UI_SELECTION_COLOUR = REGISTRAR.register("ui_selection_colour", UiSelectionColourConfig::new);
  public static final RegistryDelegate<AdditionGuideColourConfig> ADDITION_GUIDE_COLOUR = REGISTRAR.register("addition_guide_colour", AdditionGuideColourConfig::new);
  public static final RegistryDelegate<AdditionCounterAttackGuideColourConfig> ADDITION_COUNTER_ATTACK_GUIDE_COLOUR = REGISTRAR.register("addition_counter_attack_guide_colour", AdditionCounterAttackGuideColourConfig::new);
  public static final RegistryDelegate<AdditionCounterAttackShieldColourConfig> ADDITION_COUNTER_ATTACK_SHIELD_COLOUR = REGISTRAR.register("addition_counter_attack_shield_colour", AdditionCounterAttackShieldColourConfig::new);

  public static final RegistryDelegate<DabasSaveDataConfig> DABAS_SAVE_DATA = REGISTRAR.register("dabas_save_data", DabasSaveDataConfig::new);

  static void register(final ConfigRegistryEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
