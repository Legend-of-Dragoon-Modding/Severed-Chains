package legend.game;

import com.github.slugify.Slugify;
import legend.core.GameEngine;
import legend.game.inventory.ConsumableItem;
import legend.game.inventory.EquipmentItem;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistry;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.modding.events.EventListener;
import legend.game.modding.registries.RegistryId;
import legend.game.types.EquipmentStats1c;
import legend.game.types.ItemStats0c;

import static legend.game.SItem._80117a10;
import static legend.game.SItem.equipmentStats_80111ff0;
import static legend.game.SItem.itemNames_8011972c;
import static legend.game.SItem.itemPrices_80114310;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;

@EventListener
public class BaseMod {
  public static final String MOD_ID = "lod";

  private static final Slugify slug = Slugify.builder().underscoreSeparator(true).customReplacement("'", "").customReplacement("-", "_").build();

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  @EventListener
  public static void registerItems(final ItemRegistryEvent event) {
    for(int equipmentId = 0; equipmentId < Math.min(158, equipmentStats_80111ff0.length()); equipmentId++) {
      final String name = itemNames_8011972c.get(equipmentId).deref().get();

      if(!name.isEmpty()) {
        final EquipmentStats1c equipmentStats = equipmentStats_80111ff0.get(equipmentId);
        final String description = _80117a10.get(equipmentId).deref().get();
        final int price = itemPrices_80114310.get(equipmentId).get();

        final Item equipment = new EquipmentItem(id(slug.slugify(name)), name, description, price, equipmentStats);
        event.register(equipment);

        ((ItemRegistry)GameEngine.REGISTRIES.items).mapId(equipmentId, equipment.id);
      }
    }

    for(int itemId = 1; itemId < itemStats_8004f2ac.length(); itemId++) {
      String name = itemNames_8011972c.get(itemId + 0xc0).deref().get();
      if(name.isEmpty()) {
        name = "item_" + itemId;
      }

      final ItemStats0c itemStats = itemStats_8004f2ac.get(itemId);
      final String description = _80117a10.get(itemId + 192).deref().get();
      final int price = itemPrices_80114310.get(itemId).get();

      final Item item = new ConsumableItem(id(slug.slugify(name)), name, description, price, itemStats);
      event.register(item);

      ((ItemRegistry)GameEngine.REGISTRIES.items).mapId(itemId + 192, item.id);
    }
  }
}
