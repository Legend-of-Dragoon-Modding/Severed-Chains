package legend.game.modding.coremod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;
import legend.game.submap.EncounterRateMode;

public class EncounterRateConfigEntry extends EnumConfigEntry<EncounterRateMode> {
  public EncounterRateConfigEntry() {
    super(EncounterRateMode.class, EncounterRateMode.AVERAGE, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
