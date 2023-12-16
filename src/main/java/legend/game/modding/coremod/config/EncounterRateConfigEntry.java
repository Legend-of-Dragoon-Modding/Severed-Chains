package legend.game.modding.coremod.config;

import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;
import legend.game.submap.EncounterRateMode;

public class EncounterRateConfigEntry extends EnumConfigEntry<EncounterRateMode> {
  public EncounterRateConfigEntry() {
    super(EncounterRateMode.class, EncounterRateMode.AVERAGE, ConfigStorageLocation.CAMPAIGN);
  }
}
