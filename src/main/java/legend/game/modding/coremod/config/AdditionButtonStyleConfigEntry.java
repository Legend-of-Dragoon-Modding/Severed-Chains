package legend.game.modding.coremod.config;

import legend.game.combat.AdditionButtonStyle;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class AdditionButtonStyleConfigEntry extends EnumConfigEntry<AdditionButtonStyle> {
  public AdditionButtonStyleConfigEntry() {
    super(AdditionButtonStyle.class, AdditionButtonStyle.PLAYSTATION, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }
}
