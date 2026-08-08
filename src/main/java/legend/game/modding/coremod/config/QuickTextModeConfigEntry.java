package legend.game.modding.coremod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class QuickTextModeConfigEntry extends EnumConfigEntry<QuickTextMode> {
  public QuickTextModeConfigEntry() {
    super(QuickTextMode.class, QuickTextMode.HOLD, ConfigStorageLocation.CAMPAIGN, ConfigCategory.USER_INTERFACE);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
