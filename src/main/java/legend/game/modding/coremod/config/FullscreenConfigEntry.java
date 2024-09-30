package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.RENDERER;

public class FullscreenConfigEntry extends BoolConfigEntry {
  public FullscreenConfigEntry() {
    super(false, ConfigStorageLocation.GLOBAL, ConfigCategory.GRAPHICS);
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final Boolean oldValue, final Boolean newValue) {
    if(newValue) {
      RENDERER.window().makeFullscreen();
    } else {
      RENDERER.window().makeWindowed();
    }
  }
}
