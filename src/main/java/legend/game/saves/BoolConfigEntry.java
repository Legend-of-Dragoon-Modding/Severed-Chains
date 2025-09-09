package legend.game.saves;

import legend.game.inventory.screens.controls.Checkbox;
import legend.game.inventory.screens.HorizontalAlign;

/** Convenience class for simple enum-backed configs */
public class BoolConfigEntry extends ConfigEntry<Boolean> {
  public BoolConfigEntry(final boolean defaultValue, final ConfigStorageLocation storageLocation, final ConfigCategory category) {
    super(
      defaultValue,
      storageLocation,
      category,
      BoolConfigEntry::serialize,
      bytes -> deserialize(bytes, defaultValue)
    );

    this.setEditControl((current, gameState) -> {
      final Checkbox checkbox = new Checkbox();
      checkbox.setHorizontalAlign(HorizontalAlign.RIGHT);
      checkbox.setChecked(current);
      checkbox.onToggled(val -> gameState.setConfig(this, val));
      return checkbox;
    });
  }

  private static byte[] serialize(final boolean val) {
    return new byte[] {(byte)(val ? 1 : 0)};
  }

  private static boolean deserialize(final byte[] bytes, final boolean defaultValue) {
    if(bytes.length == 1) {
      if(bytes[0] == 1) {
        return true;
      }

      if(bytes[0] == 0) {
        return false;
      }
    }

    return defaultValue;
  }
}
