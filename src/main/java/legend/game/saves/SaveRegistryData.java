package legend.game.saves;

import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.Tag;
import java.util.HashSet;
import java.util.Set;
import static legend.core.GameEngine.REGISTRIES;

/** Preserves unknown fields and unavailable registry records, never deleted available records. */
public final class SaveRegistryData {
  private SaveRegistryData() { }

  public static MapTag inventoryEntry(final String key, final org.legendofdragoon.modloader.registries.RegistryId id) {
    final MapTag entry = new MapTag();
    entry.set(key, new RegistryIdTag(id));
    return entry;
  }

  public static boolean available(final MapTag entry) {
    for(final String key : entry.keys()) {
      if(!(entry.get(key) instanceof final RegistryIdTag id)) continue;
      final boolean present = switch(key) {
        case "equipmentId" -> REGISTRIES.equipment.hasEntry(id.get());
        case "itemId" -> REGISTRIES.items.hasEntry(id.get());
        case "goodId" -> REGISTRIES.goods.hasEntry(id.get());
        case "templateId" -> REGISTRIES.characterTemplates.hasEntry(id.get());
        case "statTypeId" -> REGISTRIES.statTypes.hasEntry(id.get());
        case "additionId" -> REGISTRIES.additions.hasEntry(id.get());
        case "spellId" -> REGISTRIES.spells.hasEntry(id.get());
        case "typeId" -> !entry.has("modId") || REGISTRIES.statModTypes.hasEntry(id.get());
        default -> true;
      };
      if(!present) return false;
    }
    return true;
  }

  private static String identity(final MapTag entry) {
    for(final String key : java.util.List.of("equipmentId", "itemId", "goodId", "templateId", "statTypeId", "additionId", "spellId", "modId")) {
      if(entry.has(key)) return key + ':' + entry.get(key).asRegistryId().get();
    }
    return null;
  }

  public static void merge(final MapTag current, final MapTag original) {
    for(final String key : original.keys()) {
      final Tag old = original.get(key);
      if(!current.has(key)) {
        current.set(key, old.clone());
      } else if(current.get(key) instanceof final MapTag map && old instanceof final MapTag oldMap) {
        merge(map, oldMap);
      } else if(current.get(key) instanceof final ListTag list && old instanceof final ListTag oldList) {
        final Set<Integer> matched = new HashSet<>();
        for(final Tag value : list) {
          if(!(value instanceof final MapTag entry)) continue;
          final String id = identity(entry);
          if(id == null) continue;
          for(int i = 0; i < oldList.size(); i++) {
            if(!matched.contains(i) && oldList.get(i) instanceof final MapTag oldEntry && id.equals(identity(oldEntry))) {
              merge(entry, oldEntry);
              matched.add(i);
              break;
            }
          }
        }
        for(int i = 0; i < oldList.size(); i++) {
          if(!matched.contains(i) && oldList.get(i) instanceof final MapTag entry && !available(entry)) list.add(entry.clone());
        }
      }
    }
  }
}
