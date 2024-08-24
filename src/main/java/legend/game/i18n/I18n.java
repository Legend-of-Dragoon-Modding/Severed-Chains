package legend.game.i18n;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.IllegalFormatException;

import static legend.core.GameEngine.LANG;

public final class I18n {
  private I18n() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(I18n.class);

  public static String translate(final String key) {
    try {
      return LANG.getTranslation(key);
    } catch(final IllegalFormatException e) {
      LOGGER.error("Invalid String.format string %s", key);
      return key;
    }
  }

  public static String translate(final String key, final Object... args) {
    try {
      return LANG.getTranslation(key, args);
    } catch(final IllegalFormatException e) {
      LOGGER.error("Invalid String.format string %s", key);
      return key;
    }
  }

  public static String translate(final RegistryEntry key) {
    try {
      return LANG.getTranslation(key.getTranslationKey());
    } catch(final IllegalFormatException e) {
      LOGGER.error("Invalid String.format string %s", key.getTranslationKey());
      return key.getTranslationKey();
    }
  }

  public static String translate(final RegistryEntry key, final Object... args) {
    try {
      return LANG.getTranslation(key.getTranslationKey(), args);
    } catch(final IllegalFormatException e) {
      LOGGER.error("Invalid String.format string %s", key.getTranslationKey());
      return key.getTranslationKey();
    }
  }

  public static String translate(final RegistryDelegate<?> key) {
    try {
      return LANG.getTranslation(key.getTranslationKey());
    } catch(final IllegalFormatException e) {
      LOGGER.error("Invalid String.format string %s", key.getTranslationKey());
      return key.getTranslationKey();
    }
  }

  public static String translate(final RegistryDelegate<?> key, final Object... args) {
    try {
      return LANG.getTranslation(key.getTranslationKey(), args);
    } catch(final IllegalFormatException e) {
      LOGGER.error("Invalid String.format string %s", key.getTranslationKey());
      return key.getTranslationKey();
    }
  }
}
