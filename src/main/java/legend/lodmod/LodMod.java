package legend.lodmod;

import com.github.slugify.Slugify;
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.inventory.SpellRegistryEvent;
import legend.game.modding.Mod;
import legend.game.modding.events.EventListener;
import legend.game.modding.registries.RegistryId;
import legend.game.types.EquipmentStats1c;
import legend.game.types.ItemStats0c;
import legend.game.types.SpellStats0c;
import legend.game.unpacker.Unpacker;

import static legend.game.SItem.equipmentStats_80111ff0;
import static legend.game.SItem.equipment_8011972c;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.combat.Bttl_800c.spellStats_800fa0b8;

/** Will eventually contain standard LOD content. Will be able to be disabled for total overhaul mods. */
@Mod(id = LodMod.MOD_ID)
@EventListener
public class LodMod {
  public static final String MOD_ID = "lod";

  private static final Slugify slug = Slugify.builder().underscoreSeparator(true).customReplacement("'", "").build();

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  @EventListener
  public static void registerItems(final ItemRegistryEvent event) {
    for(int itemId = 0; itemId < itemStats_8004f2ac.length; itemId++) {
      itemStats_8004f2ac[itemId] = ItemStats0c.fromFile(Unpacker.loadFile("items/%d.ditm".formatted(itemId)));

      final String name = equipment_8011972c.get(itemId + 0xc0).deref().get();
      final ItemStats0c itemStats = itemStats_8004f2ac[itemId];

      event.register(id(slug.slugify(name)), new Item(name, itemStats));
    }
  }

  @EventListener
  public static void registerEquipment(final EquipmentRegistryEvent event) {
    for(int equipmentId = 0; equipmentId < equipmentStats_80111ff0.length; equipmentId++) {
      equipmentStats_80111ff0[equipmentId] = EquipmentStats1c.fromFile(Unpacker.loadFile("equipment/%d.deqp".formatted(equipmentId)));

      final String name = equipment_8011972c.get(equipmentId).deref().get();

      if(!name.isEmpty()) {
        final EquipmentStats1c equipmentStats = equipmentStats_80111ff0[equipmentId];
        event.register(id(slug.slugify(name)), new Equipment(name, equipmentStats));
      }
    }
  }

  @EventListener
  public static void registerSpells(final SpellRegistryEvent event) {
    for(int spellId = 0; spellId < spellStats_800fa0b8.length; spellId++) {
      spellStats_800fa0b8[spellId] = SpellStats0c.fromFile(Unpacker.loadFile("spells/%d.dspl".formatted(spellId)));
    }
  }
}
