package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;

public class FrameSkipConfigEntry extends BoolConfigEntry {
  public FrameSkipConfigEntry() {
    super(true, ConfigStorageLocation.GLOBAL, ConfigCategory.GRAPHICS);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
