package legend.game.saves;

import legend.game.inventory.screens.Control;
import legend.game.modding.registries.RegistryEntry;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConfigEntry<T> extends RegistryEntry {
  public final T defaultValue;
  public final Predicate<T> validator;
  public final ConfigStorageLocation storageLocation;
  public final Function<T, byte[]> serializer;
  public final Function<byte[], T> deserializer;

  private BiFunction<T, ConfigCollection, Control> editControl;

  public ConfigEntry(final T defaultValue, final Predicate<T> validator, final ConfigStorageLocation storageLocation, final Function<T, byte[]> serializer, final Function<byte[], T> deserializer) {
    this.defaultValue = defaultValue;
    this.validator = validator;
    this.storageLocation = storageLocation;
    this.serializer = serializer;
    this.deserializer = deserializer;
  }

  protected void setEditControl(final BiFunction<T, ConfigCollection, Control> editControl) {
    this.editControl = editControl;
  }

  public boolean hasEditControl() {
    return this.editControl != null;
  }

  public Control makeEditControl(final T value, final ConfigCollection gameState) {
    return this.editControl.apply(value, gameState);
  }

  public void onChange(final T oldValue, final T newValue) {

  }
}
