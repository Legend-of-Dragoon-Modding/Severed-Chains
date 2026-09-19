package legend.game.saves;

import legend.core.lang.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static legend.core.GameEngine.REGISTRIES;

/** Transient preset identity and immutable value baseline; never written to config files. */
final class ConfigPresetState {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ConfigPresetState.class);

  final TextComponent name;
  private final Map<RegistryId, byte[]> values = new HashMap<>();
  private long checkedRevision;
  private boolean modified;

  ConfigPresetState(final TextComponent name, final ConfigCollection config, final long revision) {
    this.name = name;
    this.checkedRevision = revision;

    for(final RegistryId id : REGISTRIES.config) {
      final ConfigEntry<?> entry = REGISTRIES.config.getEntry(id).get();

      try {
        this.values.put(id, serialize(config, entry).clone());
      } catch(final RuntimeException exception) {
        // An unreadable setting cannot establish an exact preset match.
        this.values.put(id, null);
        this.modified = true;
        LOGGER.warn("Unable to track preset setting %s", id, exception);
      }
    }
  }

  boolean isModified(final ConfigCollection config, final long revision) {
    if(this.checkedRevision != revision) {
      this.modified = !this.matches(config);
      this.checkedRevision = revision;
    }

    return this.modified;
  }

  private boolean matches(final ConfigCollection config) {
    if(this.values.size() != REGISTRIES.config.size()) return false;

    for(final RegistryId id : REGISTRIES.config) {
      final byte[] original = this.values.get(id);
      if(original == null) return false;

      try {
        if(!Arrays.equals(original, serialize(config, REGISTRIES.config.getEntry(id).get()))) return false;
      } catch(final RuntimeException exception) {
        return false;
      }
    }

    return true;
  }

  private static <T> byte[] serialize(final ConfigCollection config, final ConfigEntry<T> entry) {
    return entry.serializer.apply(config.getConfig(entry));
  }
}
