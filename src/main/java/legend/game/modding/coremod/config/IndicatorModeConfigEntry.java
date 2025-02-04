package legend.game.modding.coremod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;
import legend.game.submap.IndicatorMode;

public class IndicatorModeConfigEntry extends EnumConfigEntry<IndicatorMode> {
  public IndicatorModeConfigEntry() {
    super(IndicatorMode.class, IndicatorMode.ON, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
