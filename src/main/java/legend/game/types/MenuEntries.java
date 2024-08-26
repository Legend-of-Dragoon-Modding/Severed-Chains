package legend.game.types;

import legend.game.i18n.I18n;

import java.util.ArrayList;
import java.util.Comparator;

/** This class is mostly just to get around stupid generics restrictions */
public class MenuEntries<T> extends ArrayList<MenuEntryStruct04<T>> {
  public void sort() {
    this.sort(Comparator.comparingInt((MenuEntryStruct04<T> e) -> e.getIcon()).thenComparing(item -> I18n.translate(item.getNameTranslationKey())));
  }
}
