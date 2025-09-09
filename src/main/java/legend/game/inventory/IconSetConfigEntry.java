package legend.game.inventory;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class IconSetConfigEntry extends EnumConfigEntry<IconSet> {
  public IconSetConfigEntry() {
    super(IconSet.class, IconSet.ENHANCED, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final IconSet oldValue, final IconSet newValue) {
    super.onChange(configCollection, oldValue, newValue);
    ItemIcon.loadIconMap();
  }
}
