package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.game.combat.ui.AdditionOverlayMode;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigEntry;

public class AdditionOverlayConfigEntry extends ConfigEntry<AdditionOverlayMode> {
  public AdditionOverlayConfigEntry() {
    super(AdditionOverlayMode.FULL, AdditionOverlayConfigEntry::validator, AdditionOverlayConfigEntry::serializer, AdditionOverlayConfigEntry::deserializer);

    this.setEditControl((current, gameState) -> {
      final Dropdown dropdown = new Dropdown();
      dropdown.onSelection(index -> gameState.setConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get(),
        AdditionOverlayMode.values()[index]));

      for(final AdditionOverlayMode mode : AdditionOverlayMode.values()) {
        dropdown.addOption(mode.name);

        if(mode == current) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }

  private static boolean validator(final AdditionOverlayMode val) {
    return true;
  }

  private static byte[] serializer(final AdditionOverlayMode val) {
    return new byte[] {(byte)val.ordinal()};
  }

  private static AdditionOverlayMode deserializer(final byte[] data) {
    if(data.length == 1) {
      return AdditionOverlayMode.values()[IoHelper.readUByte(data, 0)];
    }

    return AdditionOverlayMode.FULL;
  }
}
