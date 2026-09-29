package legend.game.wmap.world;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/** Keeps numeric slots at the legacy adapter boundary. Unassigned entries use registry ID order. */
final class WorldMapSlots {
  private WorldMapSlots() { }

  static int index(final Object value) {
    return switch(value) {
      case WorldMapPortal portal -> portal.legacyIndex();
      case WorldMapPlace place -> place.legacyIndex();
      case WorldMapRouteData route -> route.legacyIndex();
      case WorldMapGeometry geometry -> geometry.legacyIndex();
      case WorldMapEncounterPool pool -> pool.legacyIndex();
      default -> -1;
    };
  }

  @SuppressWarnings("unchecked")
  static <T> T assign(final T value, final int index) {
    return (T)switch(value) {
      case WorldMapPortal portal -> portal.withLegacyIndex(index);
      case WorldMapPlace place -> place.withLegacyIndex(index);
      case WorldMapRouteData route -> route.withLegacyIndex(index);
      case WorldMapGeometry geometry -> geometry.withLegacyIndex(index);
      case WorldMapEncounterPool pool -> pool.withLegacyIndex(index);
      default -> value;
    };
  }

  static <T> List<WorldMapRegistrySnapshot.Value<T>> allocate(final List<WorldMapRegistrySnapshot.Value<T>> values) {
    final HashSet<Integer> occupied = new HashSet<>();
    for(final WorldMapRegistrySnapshot.Value<T> value : values) {
      final int index = index(value.data());
      if(index < -1 || index >= values.size()) {
        throw new IllegalArgumentException("WMAP slot outside dense table: " + value.id() + " at " + index);
      }
      if(index >= 0 && !occupied.add(index)) {
        throw new IllegalArgumentException("Duplicate WMAP slot " + index + " at " + value.id());
      }
    }
    final List<WorldMapRegistrySnapshot.Value<T>> result = new ArrayList<>(values.size());
    int next = 0;
    for(final WorldMapRegistrySnapshot.Value<T> value : values.stream().sorted(Comparator.comparing(value -> value.id().toString())).toList()) {
      if(index(value.data()) >= 0) {
        result.add(value);
      } else {
        while(occupied.contains(next)) {
          next++;
        }
        occupied.add(next);
        result.add(new WorldMapRegistrySnapshot.Value<>(value.id(), assign(value.data(), next++)));
      }
    }
    result.sort(Comparator.comparingInt(value -> index(value.data())));
    return List.copyOf(result);
  }
}
