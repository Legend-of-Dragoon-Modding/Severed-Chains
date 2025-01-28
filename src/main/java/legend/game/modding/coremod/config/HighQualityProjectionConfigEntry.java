package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.RENDERER;

public class HighQualityProjectionConfigEntry extends BoolConfigEntry {
  public HighQualityProjectionConfigEntry() {
    super(true, ConfigStorageLocation.GLOBAL, ConfigCategory.GRAPHICS);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final Boolean oldValue, final Boolean newValue) {
    RENDERER.updateProjections();
  }
}
