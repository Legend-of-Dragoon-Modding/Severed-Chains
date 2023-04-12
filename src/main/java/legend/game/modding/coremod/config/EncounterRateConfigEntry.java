package legend.game.modding.coremod.config;

import legend.game.modding.coremod.CoreMod;
import legend.core.IoHelper;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigEntry;
import legend.game.submap.EncounterRateMode;

public class EncounterRateConfigEntry extends ConfigEntry<EncounterRateMode> {
  public EncounterRateConfigEntry() {
    super(EncounterRateMode.RETAIL, EncounterRateConfigEntry::validator, EncounterRateConfigEntry::serializer, EncounterRateConfigEntry::deserializer);

    this.setEditControl((current, gameState) -> {
      final Dropdown dropdown = new Dropdown();
      dropdown.onSelection(index -> gameState.setConfig(CoreMod.ENCOUNTER_RATE_CONFIG.get(), EncounterRateMode.values()[index]));

      for(final EncounterRateMode mode : EncounterRateMode.values()) {
        dropdown.addOption(mode.name);

        if(mode == current) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }

  private static boolean validator(final EncounterRateMode val) {
    return true;
  }

  private static byte[] serializer(final EncounterRateMode val) {
    return new byte[] {(byte)val.ordinal()};
  }

  private static EncounterRateMode deserializer(final byte[] data) {
    if(data.length == 1) {
      return EncounterRateMode.values()[IoHelper.readUByte(data, 0)];
    }

    return EncounterRateMode.RETAIL;
  }
}
