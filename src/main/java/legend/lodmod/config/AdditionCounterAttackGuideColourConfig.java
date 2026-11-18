package legend.lodmod.config;

import legend.game.saves.ColourConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import org.joml.Vector3f;

public class AdditionCounterAttackGuideColourConfig extends ColourConfigEntry {
  public AdditionCounterAttackGuideColourConfig() {
    super(new Vector3f(0xd8 / 255.0f, 0x60 / 255.0f, 0x20 / 255.0f), ConfigStorageLocation.CAMPAIGN, ConfigCategory.USER_INTERFACE, true);
  }
}
