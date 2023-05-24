package legend.game.i18n;

import static legend.core.GameEngine.LANG;

public class I18n {
  public static String translate(final String key) {
    return LANG.getTranslation(key);
  }

  public static String translate(final String key, final Object... args) {
    return LANG.getTranslation(key, args);
  }
}
