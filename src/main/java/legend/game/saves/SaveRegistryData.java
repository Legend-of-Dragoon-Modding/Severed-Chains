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
    return available(entry, new SaveSchemaCatalog(java.util.List.of()));
  }

  public static boolean available(final MapTag entry, final SaveSchemaCatalog schemas) {
    for(final String key : entry.keys()) {
      if(!(entry.get(key) instanceof final RegistryIdTag id)) continue;
      final boolean present = switch(key) {
        case "equipmentId" -> REGISTRIES.equipment.hasEntry(schemas.canonical(SaveSchema.Domain.EQUIPMENT, id.get()));
        case "itemId" -> REGISTRIES.items.hasEntry(schemas.canonical(SaveSchema.Domain.ITEM, id.get()));
        case "goodId" -> REGISTRIES.goods.hasEntry(schemas.canonical(SaveSchema.Domain.GOOD, id.get()));
        case "templateId" -> REGISTRIES.characterTemplates.hasEntry(schemas.canonical(SaveSchema.Domain.CHARACTER, id.get()));
        case "statTypeId" -> REGISTRIES.statTypes.hasEntry(schemas.canonical(SaveSchema.Domain.STAT, id.get()));
        case "additionId" -> REGISTRIES.additions.hasEntry(schemas.canonical(SaveSchema.Domain.ADDITION, id.get()));
        case "spellId" -> REGISTRIES.spells.hasEntry(schemas.canonical(SaveSchema.Domain.SPELL, id.get()));
        case "typeId" -> !entry.has("modId") || REGISTRIES.statModTypes.hasEntry(schemas.canonical(SaveSchema.Domain.STAT_MOD, id.get()));
        default -> true;
      };
      if(!present) return false;
    }
    return true;
  }

  private static String identity(final MapTag entry, final SaveSchemaCatalog schemas) {
    for(final String key : java.util.List.of("equipmentId", "itemId", "goodId", "templateId", "statTypeId", "additionId", "spellId", "modId")) {
      if(entry.has(key)) {
        final var id = entry.get(key).asRegistryId().get();
        return key + ':' + (key.equals("modId") ? id : schemas.canonical(domain(key), id));
      }
    }
    return null;
  }

  private static SaveSchema.Domain domain(final String key) {
    return switch(key) {
      case "itemId" -> SaveSchema.Domain.ITEM;
      case "equipmentId" -> SaveSchema.Domain.EQUIPMENT;
      case "goodId" -> SaveSchema.Domain.GOOD;
      case "templateId" -> SaveSchema.Domain.CHARACTER;
      case "statTypeId" -> SaveSchema.Domain.STAT;
      case "additionId" -> SaveSchema.Domain.ADDITION;
      case "spellId" -> SaveSchema.Domain.SPELL;
      case "modId" -> SaveSchema.Domain.STAT_MOD;
      default -> throw new IllegalArgumentException("Not a registry record identity: " + key);
    };
  }

  public static void merge(final MapTag current, final MapTag original) {
    merge(current, original, new SaveSchemaCatalog(java.util.List.of()));
  }

  public static void merge(final MapTag current, final MapTag original, final SaveSchemaCatalog schemas) {
    merge(current, original, schemas, entry -> available(entry, schemas));
  }

  /** The resolver can also be supplied by offline migration tools without booting game registries. */
  public static void merge(final MapTag current, final MapTag original, final SaveSchemaCatalog schemas, final java.util.function.Predicate<MapTag> available) {
    for(final String key : original.keys()) {
      final Tag old = original.get(key);
      if(!current.has(key)) {
        boolean preserve = true;
        for(final String identity : java.util.List.of("itemId", "equipmentId", "goodId", "templateId", "statTypeId", "additionId", "spellId", "modId")) {
          if(!current.has(identity)) continue;
          final var id = current.get(identity.equals("modId") && current.has("typeId") ? "typeId" : identity).asRegistryId().get();
          preserve = schemas.preserveAbsent(domain(identity), id, key);
          break;
        }
        if(!preserve) continue;
        current.set(key, old.clone());
      } else if(current.get(key) instanceof final MapTag map && old instanceof final MapTag oldMap) {
        merge(map, oldMap, schemas, available);
      } else if(current.get(key) instanceof final ListTag list && old instanceof final ListTag oldList) {
        final Set<Integer> matched = new HashSet<>();
        for(final Tag value : list) {
          if(!(value instanceof final MapTag entry)) continue;
          final String id = identity(entry, schemas);
          if(id == null) continue;
          for(int i = 0; i < oldList.size(); i++) {
            if(!matched.contains(i) && oldList.get(i) instanceof final MapTag oldEntry && id.equals(identity(oldEntry, schemas))) {
              merge(entry, oldEntry, schemas, available);
              matched.add(i);
              break;
            }
          }
        }
        for(int i = 0; i < oldList.size(); i++) {
          if(!matched.contains(i) && oldList.get(i) instanceof final MapTag entry && !available.test(entry)) list.add(entry.clone());
        }
      }
    }
  }
}
