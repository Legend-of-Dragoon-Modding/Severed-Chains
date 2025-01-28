package legend.game.saves;

import legend.core.IoHelper;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Dropdown;

/** Convenience class for simple enum-backed configs */
public class EnumConfigEntry<T extends Enum<T>> extends ConfigEntry<T> {
  private final Class<T> cls;

  public EnumConfigEntry(final Class<T> cls, final T defaultValue, final ConfigStorageLocation storageLocation, final ConfigCategory category) {
    super(
      defaultValue,
      storageLocation,
      category,
      IoHelper::enumToBytes,
      bytes -> IoHelper.enumFromBytes(cls, bytes, defaultValue)
    );

    this.cls = cls;

    this.setEditControl((current, gameState) -> {
      final Dropdown<String> dropdown = new Dropdown<>();
      dropdown.onSelection(index -> gameState.setConfig(this, this.cls.getEnumConstants()[index]));

      for(final T mode : this.cls.getEnumConstants()) {
        dropdown.addOption(I18n.translate(this.getRegistryId().modId() + ".config." + this.getRegistryId().entryId() + '.' + mode.name()));

        if(mode == current) {
          dropdown.setSelectedIndex(dropdown.size() - 1);
        }
      }

      return dropdown;
    });
  }
}
