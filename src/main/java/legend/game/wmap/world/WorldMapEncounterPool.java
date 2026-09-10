package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.List;

/** Four weighted world-map encounter selections for one legacy pool index. */
public record WorldMapEncounterPool(int legacyIndex, List<RegistryId> encounters) {
  public WorldMapEncounterPool {
    encounters = List.copyOf(encounters);
    if(encounters.size() != 4) throw new IllegalArgumentException("World map encounter pool " + legacyIndex + " must contain four weighted encounters");
  }
}
