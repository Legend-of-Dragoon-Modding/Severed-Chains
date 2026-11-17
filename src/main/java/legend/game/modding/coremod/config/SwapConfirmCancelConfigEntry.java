package legend.game.modding.coremod.config;

import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.controls.Checkbox;
import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;

import static legend.core.GameEngine.PLATFORM;

public class SwapConfirmCancelConfigEntry extends BoolConfigEntry {
  public SwapConfirmCancelConfigEntry() {
    super(false, ConfigStorageLocation.GLOBAL, ConfigCategory.CONTROLS, false);

    this.setEditControl((current, config) -> {
      final Checkbox checkbox = new Checkbox();
      checkbox.setHorizontalAlign(HorizontalAlign.RIGHT);
      checkbox.setChecked(current);
      checkbox.onToggled(val -> {
        PLATFORM.resetActionStates();
        config.setConfig(this, val);
      });
      return checkbox;
    });
  }

  @Override
  public boolean hasHelp() {
    return true;
  }
}
