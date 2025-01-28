package legend.game.saves;

import legend.game.inventory.screens.Control;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ConfigEntry<T> extends RegistryEntry {
  public final T defaultValue;
  public final ConfigStorageLocation storageLocation;
  public final ConfigCategory category;
  public final Function<T, byte[]> serializer;
  public final Function<byte[], T> deserializer;

  private BiFunction<T, ConfigCollection, Control> editControl;

  public ConfigEntry(final T defaultValue, final ConfigStorageLocation storageLocation, final ConfigCategory category, final Function<T, byte[]> serializer, final Function<byte[], T> deserializer) {
    this.defaultValue = defaultValue;
    this.storageLocation = storageLocation;
    this.category = category;
    this.serializer = serializer;
    this.deserializer = deserializer;
  }

  protected void setEditControl(final BiFunction<T, ConfigCollection, Control> editControl) {
    this.editControl = editControl;
  }

  public boolean hasEditControl() {
    return this.editControl != null;
  }

  public Control makeEditControl(final T value, final ConfigCollection config) {
    return this.editControl.apply(value, config);
  }

  public String getLabelTranslationKey() {
    return this.getTranslationKey("label");
  }

  public String getHelpTranslationKey() {
    return this.getTranslationKey("help");
  }

  public boolean hasHelp() {
    return false;
  }

  public void onChange(final ConfigCollection configCollection, final T oldValue, final T newValue) {

  }
}
