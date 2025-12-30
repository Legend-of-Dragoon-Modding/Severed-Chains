package legend.game.modding.coremod.config;

import legend.core.RenderEngine;
import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.RENDERER;

public class FrameSkipConfigEntry extends BoolConfigEntry {
  public FrameSkipConfigEntry() {
    super(true, ConfigStorageLocation.GLOBAL, ConfigCategory.GRAPHICS);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final Boolean oldValue, final Boolean newValue) {
    super.onChange(configCollection, oldValue, newValue);
    RENDERER.setFrameSkipOption(newValue);
  }
}
