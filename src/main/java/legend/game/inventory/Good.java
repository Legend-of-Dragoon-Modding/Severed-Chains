package legend.game.inventory;

import org.legendofdragoon.modloader.registries.RegistryEntry;

public class Good extends RegistryEntry {
  public final int sortingIndex;

  public Good(final int sortingIndex) {
    this.sortingIndex = sortingIndex;
  }
}
