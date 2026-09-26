package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

public class AdditionTimingWindowConfigEntry extends ConfigEntry<Float> {
  public AdditionTimingWindowConfigEntry() {
    super(1.0f, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY, AdditionTimingWindowConfigEntry::serializer, AdditionTimingWindowConfigEntry::deserializer);

    this.setEditControl((number, gameState) -> {
      final NumberSpinner<Float> spinner = NumberSpinner.percentSpinner(number, 0.25f, 1.0f, 1.0f, 5.0f);
      spinner.onChange(val -> gameState.setConfig(this, val));
      return spinner;
    });
  }

  @Override
  public boolean hasHelp() {
    return true;
  }

  private static byte[] serializer(final float val) {
    final byte[] data = new byte[2];
    MathHelper.set(data, 0, 2, (short)(Math.round(val * 100.0f)));
    return data;
  }

  private static float deserializer(final byte[] data) {
    if(data.length == 2) {
      return IoHelper.readUShort(data, 0) / 100.0f;
    }

    if(data.length == 1) {
      return IoHelper.readUByte(data, 0) / 100.0f;
    }

    return 1.0f;
  }
}
