package legend.game.saves;

import legend.game.inventory.screens.Control;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConfigEntry<T> extends RegistryEntry {
  private final T defaultValue;
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

  public T getDefaultValue() {
    return this.defaultValue;
  }

  /**
   * Stable preset value category, independent of Java implementation class names.
   * Override for custom values or more specific collection/enum contracts.
   * This describes compatibility; it does not validate the serialized bytes.
   */
  public String getPresetValueType() {
    return switch(this.defaultValue) {
      case Boolean ignored -> "boolean";
      case Byte ignored -> "int8";
      case Short ignored -> "int16";
      case Integer ignored -> "int32";
      case Long ignored -> "int64";
      case Float ignored -> "float32";
      case Double ignored -> "float64";
      case Character ignored -> "char";
      case String ignored -> "string";
      case Enum<?> ignored -> "enum";
      case byte[] ignored -> "bytes";
      case String[] ignored -> "string_array";
      case List<?> ignored -> "list";
      case Set<?> ignored -> "set";
      case Map<?, ?> ignored -> "map";
      case null, default -> "opaque";
    };
  }

  /**
   * Positive version of this entry's preset encoding. Override and increment when
   * changing its binary layout or meaning, including collection elements or enum domains.
   * Readers require an exact match. Legacy presets without metadata cannot be checked.
   */
  public int getPresetSchemaVersion() {
    return 1;
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

  public boolean isAdvanced() {
    return false;
  }

  /**
   * Whether this config entry will appear in the battle options menu
   */
  public boolean availableInBattle() {
    return true;
  }

  public void onChange(final ConfigCollection configCollection, final T oldValue, final T newValue) {

  }
}
