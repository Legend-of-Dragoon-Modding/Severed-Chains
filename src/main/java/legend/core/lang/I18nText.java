package legend.core.lang;

import legend.game.i18n.I18n;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public class I18nText implements TextComponent {
  private final String translationKey;
  private final Object[] args;

  public I18nText(final String translationKey, final Object... args) {
    this.translationKey = translationKey;
    this.args = args;
  }

  public I18nText(final RegistryEntry entry, final Object... args) {
    this(entry.getTranslationKey(), args);
  }

  public I18nText(final RegistryDelegate<?> entry, final Object... args) {
    this(entry.getTranslationKey(), args);
  }

  @Override
  public String get() {
    if(this.args.length == 0) {
      return I18n.translate(this.translationKey);
    }

    return I18n.translate(this.translationKey, this.args);
  }

  @Override
  public String toString() {
    return this.get();
  }
}
