package legend.turnorder;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;

public class ShowTurnOrderConfig extends BoolConfigEntry {
  public ShowTurnOrderConfig() {
    super(true, ConfigStorageLocation.CAMPAIGN, ConfigCategory.USER_INTERFACE);
  }
}
