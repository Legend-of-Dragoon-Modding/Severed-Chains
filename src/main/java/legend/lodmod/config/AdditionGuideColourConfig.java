package legend.lodmod.config;

import legend.game.saves.ColourConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import org.joml.Vector3f;

public class AdditionGuideColourConfig extends ColourConfigEntry {
  public AdditionGuideColourConfig() {
    super(new Vector3f(0x48 / 255.0f, 0x60 / 255.0f, 1.0f), ConfigStorageLocation.CAMPAIGN, ConfigCategory.USER_INTERFACE, true);
  }
}
