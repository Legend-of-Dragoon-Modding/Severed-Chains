package legend.lodmod.config;

import legend.game.saves.ColourConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import org.joml.Vector3f;

public class UiSelectionColourConfig extends ColourConfigEntry {
  public UiSelectionColourConfig() {
    super(new Vector3f(0.5f, 0.19607843f, 0.39215687f), ConfigStorageLocation.CAMPAIGN, ConfigCategory.GRAPHICS);
  }
}
