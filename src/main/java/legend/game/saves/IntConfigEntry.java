package legend.game.saves;

import legend.core.MathHelper;
import legend.game.inventory.screens.controls.NumberSpinner;

/** Convenience class for simple int-backed configs */
public class IntConfigEntry extends ConfigEntry<Integer> {
  public IntConfigEntry(final int defaultValue, final int minValue, final int maxValue, final ConfigStorageLocation storageLocation, final ConfigCategory category) {
    this(defaultValue, minValue, maxValue, storageLocation, category, true);
  }

  public IntConfigEntry(final int defaultValue, final int minValue, final int maxValue, final ConfigStorageLocation storageLocation, final ConfigCategory category, final boolean editable) {
    super(
      defaultValue,
      storageLocation,
      category,
      IntConfigEntry::serialize,
      bytes -> deserialize(bytes, defaultValue)
    );

    if(editable) {
      this.setEditControl((current, gameState) -> {
        final NumberSpinner<Integer> spinner = NumberSpinner.intSpinner(current, minValue, maxValue);
        spinner.onChange(val -> gameState.setConfig(this, val));
        return spinner;
      });
    }
  }

  private static byte[] serialize(final int val) {
    final byte[] out = new byte[4];
    MathHelper.setInt(out, 0, val);
    return out;
  }

  private static int deserialize(final byte[] bytes, final int defaultValue) {
    if(bytes.length == 4) {
      return MathHelper.getInt(bytes, 0);
    }

    return defaultValue;
  }
}
