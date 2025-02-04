package legend.game.saves;

import legend.core.IoHelper;

/** Convenience class for simple string-backed configs */
public class StringConfigEntry extends ConfigEntry<String> {
  public StringConfigEntry(final String defaultValue, final int lengthSize, final ConfigStorageLocation storageLocation, final ConfigCategory category) {
    super(
      defaultValue,
      storageLocation,
      category,
      string -> IoHelper.stringToBytes(string, lengthSize),
      bytes -> IoHelper.stringFromBytes(bytes, lengthSize, defaultValue)
    );
  }
}
