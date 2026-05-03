package legend.game.debugger;

import javafx.util.StringConverter;
import legend.game.i18n.I18n;
import org.legendofdragoon.modloader.registries.RegistryEntry;

class RegistryEntryConverter<T extends RegistryEntry> extends StringConverter<T> {
  @Override
  public String toString(final T t) {
    if(t == null) {
      return "";
    }

    return t.getRegistryId() + " - " + I18n.translate(t);
  }

  @Override
  public T fromString(final String s) {
    return null;
  }
}
