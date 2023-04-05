package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigEntry;
import legend.game.submap.IndicatorMode;

public class IndicatorModeConfigEntry extends ConfigEntry<IndicatorMode> {
  public IndicatorModeConfigEntry() {
    super(IndicatorMode.ON, IndicatorModeConfigEntry::validator, IndicatorModeConfigEntry::serializer, IndicatorModeConfigEntry::deserializer);

    this.setEditControl((current, gameState) -> {
      final Dropdown dropdown = new Dropdown();
      dropdown.onSelection(index -> gameState.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.values()[index]));

      for(final IndicatorMode mode : IndicatorMode.values()) {
        dropdown.addOption(mode.name);

        if(mode == current) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }

  private static boolean validator(final IndicatorMode val) {
    return true;
  }

  private static byte[] serializer(final IndicatorMode val) {
    return new byte[] {(byte)val.ordinal()};
  }

  private static IndicatorMode deserializer(final byte[] data) {
    if(data.length == 1) {
      return IndicatorMode.values()[IoHelper.readUByte(data, 0)];
    }

    return IndicatorMode.ON;
  }
}
