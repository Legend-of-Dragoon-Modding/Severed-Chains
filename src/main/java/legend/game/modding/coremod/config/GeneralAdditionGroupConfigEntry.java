package legend.game.modding.coremod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

public class GeneralAdditionGroupConfigEntry extends ConfigEntry<Void> {
  public GeneralAdditionGroupConfigEntry() {
    super(null, ConfigStorageLocation.CAMPAIGN, ConfigCategory.ADDITIONS, o -> new byte[0], bytes -> null);
  }
}
