package legend.game.modding.coremod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

public class DragoonAdditionGroupConfigEntry extends ConfigEntry<Void> {
  public DragoonAdditionGroupConfigEntry() {
    super(null, ConfigStorageLocation.CAMPAIGN, ConfigCategory.ADDITIONS, o -> new byte[0], bytes -> null);
  }
}
