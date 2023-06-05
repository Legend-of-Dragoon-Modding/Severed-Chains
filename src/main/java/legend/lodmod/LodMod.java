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
import static legend.game.SItem.itemDescriptions_80117a10;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8005.itemCombatDescriptions_80051758;
import static legend.game.Scus94491BpeSegment_8005.spellCombatDescriptions_80052018;
import static legend.game.Scus94491BpeSegment_8005.spells_80052734;
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
      String name = equipment_8011972c.get(itemId + 0xc0).deref().get();
      if(name.isEmpty()) {
        name = "Item " + itemId;
      }

      if(itemStats_8004f2ac[itemId] == null) {
        itemStats_8004f2ac[itemId] = ItemStats0c.fromFile(name, itemDescriptions_80117a10.get(itemId + 0xc0).deref().get(), itemCombatDescriptions_80051758.get(itemId).deref().get(), Unpacker.loadFile("items/%d.ditm".formatted(itemId)));
      }

      final ItemStats0c itemStats = itemStats_8004f2ac[itemId];

      event.register(id(slug.slugify(name)), new Item(name, itemStats));
    }
  }

  @EventListener
  public static void registerEquipment(final EquipmentRegistryEvent event) {
    for(int equipmentId = 0; equipmentId < equipmentStats_80111ff0.length; equipmentId++) {
      final String name = equipment_8011972c.get(equipmentId).deref().get();

      if(equipmentStats_80111ff0[equipmentId] == null) {
        equipmentStats_80111ff0[equipmentId] = EquipmentStats1c.fromFile(name, itemDescriptions_80117a10.get(equipmentId).deref().get(), Unpacker.loadFile("equipment/%d.deqp".formatted(equipmentId)));
      }

      if(!name.isEmpty()) {
        final EquipmentStats1c equipmentStats = equipmentStats_80111ff0[equipmentId];
        event.register(id(slug.slugify(name)), new Equipment(name, equipmentStats));
      }
    }
  }

  @EventListener
  public static void registerSpells(final SpellRegistryEvent event) {
    for(int spellId = 0; spellId < spellStats_800fa0b8.length; spellId++) {
      if(spellStats_800fa0b8[spellId] == null) {
        final String name = spellId < 84 ? spells_80052734.get(spellId).deref().get() : "Spell " + spellId;
        final String desc = spellId < 84 ? spellCombatDescriptions_80052018.get(spellId).deref().get() : "";
        spellStats_800fa0b8[spellId] = SpellStats0c.fromFile(name, desc, Unpacker.loadFile("spells/%d.dspl".formatted(spellId)));
      }
    }
  }
}
