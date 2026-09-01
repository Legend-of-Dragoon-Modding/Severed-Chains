package legend.game.modding.coremod.config;

import legend.core.renderer.PixelateMode;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class PixelateModeConfigEntry extends EnumConfigEntry<PixelateMode> {
  public PixelateModeConfigEntry() {
    super(PixelateMode.class, PixelateMode.NONE, ConfigStorageLocation.GLOBAL, ConfigCategory.POSTPROCESSING);
  }
}
