package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.List;

/** Four world-map encounter slots with whole-number percentages, retaining retail defaults. */
public record WorldMapEncounterPool(int legacyIndex, List<RegistryId> encounters, List<Integer> percentages) {
  public static final List<Integer> DEFAULT_PERCENTAGES = List.of(35, 35, 20, 10);

  public WorldMapEncounterPool(final int legacyIndex, final List<RegistryId> encounters) {
    this(legacyIndex, encounters, DEFAULT_PERCENTAGES);
  }

  public WorldMapEncounterPool(final List<RegistryId> encounters) {
    this(-1, encounters);
  }

  public WorldMapEncounterPool withLegacyIndex(final int legacyIndex) {
    return new WorldMapEncounterPool(legacyIndex, this.encounters, this.percentages);
  }

  public WorldMapEncounterPool {
    encounters = List.copyOf(encounters);
    if(encounters.size() != 4) throw new IllegalArgumentException("World map encounter pool " + legacyIndex + " must contain four weighted encounters");
    percentages = percentages == null ? DEFAULT_PERCENTAGES : List.copyOf(percentages);
    if(percentages.size() != 4 || percentages.stream().anyMatch(value -> value < 0 || value > 100) || percentages.stream().mapToInt(Integer::intValue).sum() != 100) {
      throw new IllegalArgumentException("World map encounter pool " + legacyIndex + " requires four integer percentages between 0 and 100 totalling 100");
    }
  }

  /** The caller supplies the existing 0..99 roll; zero-percent slots are never selected. */
  public RegistryId select(final int roll) {
    if(roll < 0 || roll >= 100) throw new IllegalArgumentException("World map encounter roll must be between 0 and 99");
    int upperBound = 0;
    for(int slot = 0; slot < this.encounters.size(); slot++) {
      upperBound += this.percentages.get(slot);
      if(roll < upperBound) return this.encounters.get(slot);
    }
    throw new IllegalStateException("World map encounter percentages did not cover roll " + roll);
  }
}
