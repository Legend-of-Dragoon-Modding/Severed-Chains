package legend.game.modding.coremod.config;

import legend.core.opengl.Resolution;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

import static legend.core.GameEngine.RENDERER;

public class ResolutionConfig extends EnumConfigEntry<Resolution> {
  public ResolutionConfig() {
    super(Resolution.class, Resolution.NATIVE, ConfigStorageLocation.GLOBAL);
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final Resolution oldValue, final Resolution newValue) {
    RENDERER.updateResolution();
  }
}
