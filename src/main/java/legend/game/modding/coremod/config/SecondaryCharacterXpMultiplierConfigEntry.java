package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

public class SecondaryCharacterXpMultiplierConfigEntry extends ConfigEntry<Float> {
  public SecondaryCharacterXpMultiplierConfigEntry() {
    super(0.5f, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY, SecondaryCharacterXpMultiplierConfigEntry::serializer, SecondaryCharacterXpMultiplierConfigEntry::deserializer);

    this.setEditControl((number, gameState) -> {
      final NumberSpinner<Float> spinner = NumberSpinner.percentSpinner(number, 0.01f, 0.05f, 0.0f, 1.0f);
      spinner.onChange(val -> gameState.setConfig(this, val));
      return spinner;
    });
  }

  @Override
  public boolean hasHelp() {
    return true;
  }

  private static byte[] serializer(final float val) {
    return new byte[] {(byte)(Math.round(val * 100.0f))};
  }

  private static float deserializer(final byte[] data) {
    if(data.length == 1) {
      return IoHelper.readUByte(data, 0) / 100.0f;
    }

    return 0.5f;
  }
}
