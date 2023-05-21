package legend.game.i18n;

import legend.game.modding.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import static legend.core.GameEngine.MODS;

public class LangManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger();

  private final Map<String, String> translations = new HashMap<>();

  public LangManager(final Consumer<Access> access) {
    access.accept(new Access());
  }

  public String getTranslation(final String key) {
    return this.translations.getOrDefault(key, key);
  }

  public String getTranslation(final String key, final Object... args) {
    return this.translations.getOrDefault(key, key).formatted(args);
  }

  public class Access {
    private Access() { }

    public void initialize(final Locale locale) {
      for(final ModContainer mod : MODS.getLoadedMods()) {
        try {
          LangManager.this.translations.putAll(mod.loadLang(locale));
        } catch(final IOException e) {
          LOGGER.warn("Failed to load %s for mod %d", locale, mod.modId);
        }
      }
    }

    public void reset() {
      LangManager.this.translations.clear();
    }
  }
}
