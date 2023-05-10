package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigEntry;

public class InventorySizeConfigEntry extends ConfigEntry<Integer> {
  public InventorySizeConfigEntry() {
    super(32, InventorySizeConfigEntry::validator, InventorySizeConfigEntry::serializer, InventorySizeConfigEntry::deserializer);

    this.setEditControl((number, gameState) -> {
      final NumberSpinner spinner = new NumberSpinner(number);
      spinner.setMin(1);
      spinner.setMax(9999);
      spinner.onChange(val -> gameState.setConfig(this, val));
      return spinner;
    });
  }

  private static boolean validator(final int val) {
    return val > 0 && val <= 9999;
  }

  private static byte[] serializer(final int val) {
    final byte[] data = new byte[4];
    MathHelper.set(data, 0, 4, val);
    return data;
  }

  private static int deserializer(final byte[] data) {
    if(data.length == 4) {
      return IoHelper.readInt(data, 0);
    }

    return 32;
  }
}
