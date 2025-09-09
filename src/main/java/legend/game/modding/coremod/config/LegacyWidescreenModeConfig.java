package legend.game.modding.coremod.config;

import legend.core.opengl.SubmapWidescreenMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

import static legend.core.GameEngine.RENDERER;

public class LegacyWidescreenModeConfig extends EnumConfigEntry<SubmapWidescreenMode> {
  public LegacyWidescreenModeConfig() {
    super(SubmapWidescreenMode.class, SubmapWidescreenMode.EXPANDED, ConfigStorageLocation.GLOBAL, ConfigCategory.GRAPHICS);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final SubmapWidescreenMode oldValue, final SubmapWidescreenMode newValue) {
    RENDERER.updateProjections();
  }
}
