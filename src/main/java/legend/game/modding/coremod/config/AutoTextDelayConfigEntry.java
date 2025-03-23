package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;
import legend.game.scripting.ScriptReadable;

public class AutoTextDelayConfigEntry extends ConfigEntry<Float> implements ScriptReadable {
  public AutoTextDelayConfigEntry() {
    super(1.0f, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY, AutoTextDelayConfigEntry::serializer, AutoTextDelayConfigEntry::deserializer);

    this.setEditControl((number, gameState) -> {
      final NumberSpinner<Float> spinner = NumberSpinner.floatSpinner(number, 0.25f, 0, 15);
      spinner.onChange(val -> gameState.setConfig(this, val));
      return spinner;
    });
  }

  private static byte[] serializer(final float val) {
    return new byte[] {(byte)(Math.round(val))};
  }

  private static float deserializer(final byte[] data) {
    if(data.length == 1) {
      return IoHelper.readUByte(data, 0);
    }

    return 1.0f;
  }
}
