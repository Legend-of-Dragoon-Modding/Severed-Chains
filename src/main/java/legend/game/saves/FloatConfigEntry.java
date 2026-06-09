package legend.game.saves;

import legend.core.MathHelper;
import legend.game.inventory.screens.controls.NumberSpinner;

/** Convenience class for simple float-backed configs */
public class FloatConfigEntry extends ConfigEntry<Float> {
  public FloatConfigEntry(final float defaultValue, final float step, final float bigStep, final float minValue, final float maxValue, final ConfigStorageLocation storageLocation, final ConfigCategory category) {
    this(defaultValue, step, bigStep, minValue, maxValue, storageLocation, category, true);
  }

  public FloatConfigEntry(final float defaultValue, final float step, final float bigStep, final float minValue, final float maxValue, final ConfigStorageLocation storageLocation, final ConfigCategory category, final boolean editable) {
    super(
      defaultValue,
      storageLocation,
      category,
      FloatConfigEntry::serialize,
      bytes -> deserialize(bytes, defaultValue)
    );

    if(editable) {
      this.setEditControl((current, gameState) -> {
        final NumberSpinner<Float> spinner = NumberSpinner.floatSpinner(current, step, bigStep, minValue, maxValue);
        spinner.onChange(val -> gameState.setConfig(this, val));
        return spinner;
      });
    }
  }

  private static byte[] serialize(final float val) {
    final byte[] out = new byte[4];
    MathHelper.setInt(out, 0, Float.floatToIntBits(val));
    return out;
  }

  private static float deserialize(final byte[] bytes, final float defaultValue) {
    if(bytes.length == 4) {
      return Float.intBitsToFloat(MathHelper.getInt(bytes, 0));
    }

    return defaultValue;
  }
}
