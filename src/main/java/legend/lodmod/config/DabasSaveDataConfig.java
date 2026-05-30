package legend.lodmod.config;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

public class DabasSaveDataConfig extends ConfigEntry<byte[]> {
  public DabasSaveDataConfig() {
    super(new byte[0x260], ConfigStorageLocation.SAVE, ConfigCategory.OTHER, b -> b, b -> b);
  }
}
