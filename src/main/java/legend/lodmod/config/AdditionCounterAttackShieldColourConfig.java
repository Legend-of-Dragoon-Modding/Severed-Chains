package legend.lodmod.config;

import legend.game.saves.ColourConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import org.joml.Vector3f;

public class AdditionCounterAttackShieldColourConfig extends ColourConfigEntry {
  public AdditionCounterAttackShieldColourConfig() {
    super(new Vector3f(1.0f, 0.0f, 0.0f), ConfigStorageLocation.CAMPAIGN, ConfigCategory.USER_INTERFACE, true);
  }
}
