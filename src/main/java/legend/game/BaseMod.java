package legend.game;

import com.github.slugify.Slugify;
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.modding.Mod;
import legend.game.modding.events.EventListener;
import legend.game.modding.registries.RegistryId;
import legend.game.types.EquipmentStats1c;
import legend.game.types.ItemStats0c;

import static legend.game.SItem.equipmentStats_80111ff0;
import static legend.game.SItem.equipment_8011972c;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;

@Mod(id = BaseMod.MOD_ID)
@EventListener
public class BaseMod {
  public static final String MOD_ID = "lod";

  private static final Slugify slug = Slugify.builder().underscoreSeparator(true).customReplacement("'", "").build();

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  @EventListener
  public static void registerItems(final ItemRegistryEvent event) {
    for(int itemId = 0; itemId < itemStats_8004f2ac.length(); itemId++) {
      if(itemId == 0) {
        continue;
      }

      final String name = equipment_8011972c.get(itemId + 0xc0).deref().get();
      final ItemStats0c itemStats = itemStats_8004f2ac.get(itemId);

      event.register(new Item(id(slug.slugify(name)), name, itemStats));
    }
  }

  @EventListener
  public static void registerEquipment(final EquipmentRegistryEvent event) {
    for(int equipmentId = 0; equipmentId < Math.min(158, equipmentStats_80111ff0.length()); equipmentId++) {
      final String name = equipment_8011972c.get(equipmentId).deref().get();

      if(!name.isEmpty()) {
        final EquipmentStats1c equipmentStats = equipmentStats_80111ff0.get(equipmentId);
        event.register(new Equipment(id(slug.slugify(name)), name, equipmentStats));
      }
    }
  }
}
