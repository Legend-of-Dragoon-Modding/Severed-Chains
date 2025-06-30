package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

public class AdditionOverlaySizeConfigEntry extends ConfigEntry<Float> {
  public AdditionOverlaySizeConfigEntry() {
    super(1.25f, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY, AdditionOverlaySizeConfigEntry::serializer, AdditionOverlaySizeConfigEntry::deserializer);

    this.setEditControl((number, gameState) -> {
      final NumberSpinner<Float> spinner = NumberSpinner.percentSpinner(number, 0.05f, 0.25f, 0.25f, 2.0f);
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

    return 1.25f;
  }
}
