package legend.game.modding.coremod.config;

import legend.core.GameEngine;
import legend.core.lang.RawText;
import legend.core.memory.types.IntRef;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.ModContainer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static legend.core.GameEngine.LANG;
import static legend.core.GameEngine.MODS;
import static legend.game.Main.ORIGINAL_LOCALE;

public class LanguageConfigEntry extends ConfigEntry<Locale> {
  private static final Logger LOGGER = LogManager.getFormatterLogger(LanguageConfigEntry.class);

  public LanguageConfigEntry() {
    super(ORIGINAL_LOCALE, ConfigStorageLocation.GLOBAL, ConfigCategory.USER_INTERFACE, LanguageConfigEntry::serializer, LanguageConfigEntry::deserializer);

    this.setEditControl((locale, config) -> {
      final Set<Locale> locales = getAllLocales();
      final Dropdown<Locale> dropdown = new Dropdown<>((index, entry) -> new RawText(entry.getDisplayName()));
      dropdown.onSelection(index -> config.setConfig(this, dropdown.getOption(index)));

      locales.stream()
        .sorted(Comparator.comparing(l -> locale.getDisplayName()))
        .forEach(dropdown::addOption);

      dropdown.setSelected(locale);
      return dropdown;
    });
  }

  @Override
  public void onChange(final ConfigCollection config, final Locale oldValue, final Locale newValue) {
    super.onChange(config, oldValue, newValue);
    GameEngine.loadLang();
  }

  @Override
  public Locale getDefaultValue() {
    return getDefaultLocale();
  }

  private static Locale getDefaultLocale() {
    final Set<Locale> locales = getAllLocales();

    for(final Locale locale : locales) {
      if(locale.getLanguage().equals(ORIGINAL_LOCALE.getLanguage()) && locale.getCountry().equals(ORIGINAL_LOCALE.getCountry())) {
        return locale;
      }
    }

    for(final Locale locale : locales) {
      if(locale.getLanguage().equals(ORIGINAL_LOCALE.getLanguage())) {
        return locale;
      }
    }

    return Locale.ENGLISH;
  }

  private static Set<Locale> getAllLocales() {
    final Set<Locale> locales = new HashSet<>();

    for(final ModContainer mod : MODS.getLoadedMods()) {
      locales.addAll(mod.getSupportedLocales());
    }

    try {
      locales.addAll(LANG.findAvailableOverrideLocales(Path.of("lang")));
    } catch(final IOException e) {
      LOGGER.warn("Failed to get override locales", e);
    }

    return locales;
  }

  @Override
  public boolean hasHelp() {
    return true;
  }

  private static byte[] serializer(final Locale locale) {
    int size = locale.getLanguage().length() + 1;

    if(!locale.getCountry().isBlank()) {
      size += locale.getCountry().length() + 1;
    }

    final FileData data = new FileData(new byte[size]);
    final IntRef offset = new IntRef();

    data.writeAscii(offset, locale.getLanguage(), 1);

    if(!locale.getCountry().isBlank()) {
      data.writeAscii(offset, locale.getCountry(), 1);
    }

    return data.getBytes();
  }

  private static Locale deserializer(final byte[] bytes) {
    if(bytes.length > 2) {
      final FileData data = new FileData(bytes);
      final IntRef offset = new IntRef();

      final String language = data.readAscii(offset, 1);

      if(bytes.length > offset.get() + 1) {
        final String country = data.readAscii(offset, 1);
        return Locale.of(language, country);
      }

      return Locale.of(language);
    }

    return getDefaultLocale();
  }
}
