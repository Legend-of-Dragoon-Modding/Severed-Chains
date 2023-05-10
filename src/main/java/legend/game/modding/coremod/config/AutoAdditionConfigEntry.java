package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.game.combat.AutoAdditionMode;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigEntry;

public class AutoAdditionConfigEntry extends ConfigEntry<AutoAdditionMode> {
  public AutoAdditionConfigEntry() {
    super(AutoAdditionMode.OFF, AutoAdditionConfigEntry::validator, AutoAdditionConfigEntry::serializer, AutoAdditionConfigEntry::deserializer);

    this.setEditControl((current, gameState) -> {
      final Dropdown dropdown = new Dropdown();
      dropdown.onSelection(index -> gameState.setConfig(CoreMod.AUTO_ADDITION_CONFIG.get(), AutoAdditionMode.values()[index]));

      for(final AutoAdditionMode mode : AutoAdditionMode.values()) {
        dropdown.addOption(mode.name);

        if(mode == current) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }

  private static boolean validator(final AutoAdditionMode val) {
    return true;
  }

  private static byte[] serializer(final AutoAdditionMode val) {
    return new byte[] {(byte)val.ordinal()};
  }

  private static AutoAdditionMode deserializer(final byte[] data) {
    if(data.length == 1) {
      return AutoAdditionMode.values()[IoHelper.readUByte(data, 0)];
    }

    return AutoAdditionMode.OFF;
  }
}
