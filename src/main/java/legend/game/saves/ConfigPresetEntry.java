package legend.game.saves;

import legend.core.lang.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConfigPresetEntry {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ConfigPresetEntry.class);

  public final Path path;
  /** The name to use while this preset is still loading */
  private final TextComponent temporaryName;
  private final CompletableFuture<ConfigPreset> resolver;
  public final boolean editable;

  public ConfigPresetEntry(@Nullable final Path path, final TextComponent temporaryName, final CompletableFuture<ConfigPreset> resolver, final boolean editable) {
    this.path = path;
    this.temporaryName = temporaryName;
    this.resolver = resolver;
    this.editable = editable;
  }

  public TextComponent getName() {
    if(this.resolver.isDone()) {
      try {
        return this.resolver.get().name;
      } catch(final InterruptedException | ExecutionException ignored) { }
    }

    return this.temporaryName;
  }

  public @Nullable ConfigPreset getPreset() {
    try {
      return this.resolver.get();
    } catch(final InterruptedException | ExecutionException e) {
      LOGGER.warn("Failed to load preset " + this.getName(), e);
      return null;
    }
  }
}
