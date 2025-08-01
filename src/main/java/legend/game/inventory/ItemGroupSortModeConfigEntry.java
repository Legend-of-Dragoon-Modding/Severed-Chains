package legend.game.inventory;

import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.EnumConfigEntry;

public class ItemGroupSortModeConfigEntry extends EnumConfigEntry<ItemGroupSortMode> {
  public ItemGroupSortModeConfigEntry() {
    super(ItemGroupSortMode.class, ItemGroupSortMode.ALPHABETICAL, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY);
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
