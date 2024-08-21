package legend.game.i18n;

import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import static legend.core.GameEngine.LANG;

public final class I18n {
  private I18n() { }

  public static String translate(final String key) {
    return LANG.getTranslation(key);
  }

  public static String translate(final String key, final Object... args) {
    return LANG.getTranslation(key, args);
  }

  public static String translate(final RegistryEntry key) {
    return LANG.getTranslation(key.getTranslationKey());
  }

  public static String translate(final RegistryEntry key, final Object... args) {
    return LANG.getTranslation(key.getTranslationKey(), args);
  }

  public static String translate(final RegistryDelegate<?> key) {
    return LANG.getTranslation(key.getTranslationKey());
  }

  public static String translate(final RegistryDelegate<?> key, final Object... args) {
    return LANG.getTranslation(key.getTranslationKey(), args);
  }
}
