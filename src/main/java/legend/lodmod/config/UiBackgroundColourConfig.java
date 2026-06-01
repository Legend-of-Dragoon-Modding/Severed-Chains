package legend.lodmod.config;

import legend.game.saves.ColourConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import org.joml.Vector3f;

public class UiBackgroundColourConfig extends ColourConfigEntry {
  public UiBackgroundColourConfig() {
    super(new Vector3f(0.0f, 41 / 255.0f, 159 / 255.0f), ConfigStorageLocation.CAMPAIGN, ConfigCategory.GRAPHICS);
  }
}
