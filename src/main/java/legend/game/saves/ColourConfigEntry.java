package legend.game.saves;

import legend.game.inventory.screens.controls.ColourPicker;
import org.joml.Vector3f;
import org.joml.Vector3i;

/** Convenience class for simple int-backed configs */
public class ColourConfigEntry extends ConfigEntry<Vector3f> {
  public ColourConfigEntry(final Vector3f defaultValue, final ConfigStorageLocation storageLocation, final ConfigCategory category) {
    super(
      defaultValue,
      storageLocation,
      category,
      ColourConfigEntry::serialize,
      bytes -> deserialize(bytes, defaultValue)
    );

    this.setEditControl((current, gameState) -> {
      final ColourPicker picker = new ColourPicker();
      picker.setColour(new Vector3i((int)(current.x * 255.0f), (int)(current.y * 255.0f), (int)(current.z * 255.0f)));
      picker.onChange((r, g, b) -> gameState.setConfig(this, new Vector3f(r / 255.0f, g / 255.0f, b / 255.0f)));
      return picker;
    });
  }

  private static byte[] serialize(final Vector3f val) {
    final byte[] out = new byte[3];
    out[0] = (byte)(val.x * 255);
    out[1] = (byte)(val.y * 255);
    out[2] = (byte)(val.z * 255);
    return out;
  }

  private static Vector3f deserialize(final byte[] bytes, final Vector3f defaultValue) {
    if(bytes.length == 3) {
      return new Vector3f((bytes[0] & 0xff) / 255.0f, (bytes[1] & 0xff) / 255.0f, (bytes[2] & 0xff) / 255.0f);
    }

    return defaultValue;
  }
}
