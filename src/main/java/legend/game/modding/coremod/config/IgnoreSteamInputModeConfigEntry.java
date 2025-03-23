package legend.game.modding.coremod.config;

import legend.core.platform.input.IgnoreSteamInputMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class IgnoreSteamInputModeConfigEntry extends EnumConfigEntry<IgnoreSteamInputMode> {
  public IgnoreSteamInputModeConfigEntry() {
    super(IgnoreSteamInputMode.class, IgnoreSteamInputMode.IGNORE_WHEN_GAMEPAD_USED, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
