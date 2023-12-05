package legend.lodmod;

import com.github.slugify.Slugify;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.inventory.SpellRegistryEvent;
import legend.game.modding.events.gamestate.NewGameEvent;
import legend.game.types.EquipmentSlot;
import legend.game.types.EquipmentStats1c;
import legend.game.types.ItemStats0c;
import legend.game.types.SpellStats0c;
import legend.game.unpacker.Unpacker;
import legend.lodmod.items.CharmPotionItem;
import legend.lodmod.items.FileBasedItem;
import org.legendofdragoon.modloader.Mod;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Map;

import static legend.game.SItem.equipmentStats_80111ff0;
import static legend.game.SItem.itemDescriptions_80117a10;
import static legend.game.SItem.itemNames_8011972c;
import static legend.game.SItem.itemPrices_80114310;
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

  private static final Slugify slug = Slugify.builder().underscoreSeparator(true).customReplacement("'", "").customReplacement("-", "_").build();

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  @Deprecated
  public static final Int2ObjectMap<RegistryId> itemIdMap = new Int2ObjectOpenHashMap<>();
  @Deprecated
  public static final Object2IntMap<RegistryId> idItemMap = new Object2IntOpenHashMap<>();

  @EventListener
  public static void registerItems(final ItemRegistryEvent event) {
    for(int itemId = 0; itemId < itemStats_8004f2ac.length; itemId++) {
      String name = itemNames_8011972c[itemId + 0xc0].get();
      if(name.isEmpty()) {
        name = "Item " + itemId;
      }

      if(itemStats_8004f2ac[itemId] == null) {
        itemStats_8004f2ac[itemId] = ItemStats0c.fromFile(name, itemDescriptions_80117a10[itemId + 0xc0].get(), itemCombatDescriptions_80051758[itemId].get(), Unpacker.loadFile("items/%d.ditm".formatted(itemId)));
      }

      final Item item;
      if(itemId != 0x1f) { // Charm Potion
        item = FileBasedItem.fromFile(name, itemDescriptions_80117a10[itemId + 0xc0].get(), itemCombatDescriptions_80051758[itemId].get(), itemPrices_80114310[itemId + 192], Unpacker.loadFile("items/%d.ditm".formatted(itemId)));
      } else {
        item = new CharmPotionItem(name, itemDescriptions_80117a10[itemId + 0xc0].get(), itemCombatDescriptions_80051758[itemId].get(), itemPrices_80114310[itemId + 192]);
      }

      event.register(id(slug.slugify(name)), item);
      itemIdMap.put(itemId, item.getRegistryId());
      idItemMap.put(item.getRegistryId(), itemId);
    }
  }

  @Deprecated
  public static final Int2ObjectMap<RegistryId> equipmentIdMap = new Int2ObjectOpenHashMap<>();
  @Deprecated
  public static final Object2IntMap<RegistryId> idEquipmentMap = new Object2IntOpenHashMap<>();

  @EventListener
  public static void registerEquipment(final EquipmentRegistryEvent event) {
    equipmentIdMap.clear();
    idEquipmentMap.clear();

    for(int equipmentId = 0; equipmentId < equipmentStats_80111ff0.length; equipmentId++) {
      final String name = itemNames_8011972c[equipmentId].get();

      if(equipmentStats_80111ff0[equipmentId] == null) {
        equipmentStats_80111ff0[equipmentId] = EquipmentStats1c.fromFile(name, itemDescriptions_80117a10[equipmentId].get(), Unpacker.loadFile("equipment/%d.deqp".formatted(equipmentId)));
      }

      if(!name.isEmpty()) {
        final Equipment equipment = event.register(id(slug.slugify(name)), Equipment.fromFile(name, itemDescriptions_80117a10[equipmentId].get(), itemPrices_80114310[equipmentId], Unpacker.loadFile("equipment/%d.deqp".formatted(equipmentId))));
        equipmentIdMap.put(equipmentId, equipment.getRegistryId());
        idEquipmentMap.put(equipment.getRegistryId(), equipmentId);
      }
    }
  }

  @EventListener
  public static void registerSpells(final SpellRegistryEvent event) {
    for(int spellId = 0; spellId < spellStats_800fa0b8.length; spellId++) {
      if(spellStats_800fa0b8[spellId] == null) {
        final String name = spellId < 84 ? spells_80052734[spellId].get() : "Spell " + spellId;
        final String desc = spellId < 84 ? spellCombatDescriptions_80052018[spellId].get() : "";
        spellStats_800fa0b8[spellId] = SpellStats0c.fromFile(name, desc, Unpacker.loadFile("spells/%d.dspl".formatted(spellId)));
      }
    }
  }

  @EventListener
  public static void newGame(final NewGameEvent event) {
    event.gameState.items_2e9.add(LodItems.BURN_OUT.get());
    event.gameState.items_2e9.add(LodItems.HEALING_POTION.get());
    event.gameState.items_2e9.add(LodItems.HEALING_POTION.get());

    final Map<EquipmentSlot, Equipment> dart = event.gameState.charData_32c[0].equipment_14;
    dart.put(EquipmentSlot.WEAPON, LodEquipment.BROAD_SWORD.get());
    dart.put(EquipmentSlot.HELMET, LodEquipment.BANDANA.get());
    dart.put(EquipmentSlot.ARMOUR, LodEquipment.LEATHER_ARMOR.get());
    dart.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    dart.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> lavitz = event.gameState.charData_32c[1].equipment_14;
    lavitz.put(EquipmentSlot.WEAPON, LodEquipment.SPEAR.get());
    lavitz.put(EquipmentSlot.HELMET, LodEquipment.SALLET.get());
    lavitz.put(EquipmentSlot.ARMOUR, LodEquipment.SCALE_ARMOR.get());
    lavitz.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    lavitz.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> shana = event.gameState.charData_32c[2].equipment_14;
    shana.put(EquipmentSlot.WEAPON, LodEquipment.SHORT_BOW.get());
    shana.put(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    shana.put(EquipmentSlot.ARMOUR, LodEquipment.LEATHER_JACKET.get());
    shana.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    shana.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> rose = event.gameState.charData_32c[3].equipment_14;
    rose.put(EquipmentSlot.WEAPON, LodEquipment.RAPIER.get());
    rose.put(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    rose.put(EquipmentSlot.ARMOUR, LodEquipment.LEATHER_JACKET.get());
    rose.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    rose.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> haschel = event.gameState.charData_32c[4].equipment_14;
    haschel.put(EquipmentSlot.WEAPON, LodEquipment.IRON_KNUCKLE.get());
    haschel.put(EquipmentSlot.HELMET, LodEquipment.ARMET.get());
    haschel.put(EquipmentSlot.ARMOUR, LodEquipment.DISCIPLE_VEST.get());
    haschel.put(EquipmentSlot.BOOTS, LodEquipment.IRON_KNEEPIECE.get());
    haschel.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> albert = event.gameState.charData_32c[5].equipment_14;
    albert.put(EquipmentSlot.WEAPON, LodEquipment.SPEAR.get());
    albert.put(EquipmentSlot.HELMET, LodEquipment.SALLET.get());
    albert.put(EquipmentSlot.ARMOUR, LodEquipment.SCALE_ARMOR.get());
    albert.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_BOOTS.get());
    albert.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> meru = event.gameState.charData_32c[6].equipment_14;
    meru.put(EquipmentSlot.WEAPON, LodEquipment.MACE.get());
    meru.put(EquipmentSlot.HELMET, LodEquipment.TIARA.get());
    meru.put(EquipmentSlot.ARMOUR, LodEquipment.SILVER_VEST.get());
    meru.put(EquipmentSlot.BOOTS, LodEquipment.SOFT_BOOTS.get());
    meru.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> kongol = event.gameState.charData_32c[7].equipment_14;
    kongol.put(EquipmentSlot.WEAPON, LodEquipment.AXE.get());
    kongol.put(EquipmentSlot.HELMET, LodEquipment.ARMET.get());
    kongol.put(EquipmentSlot.ARMOUR, LodEquipment.LION_FUR.get());
    kongol.put(EquipmentSlot.BOOTS, LodEquipment.IRON_KNEEPIECE.get());
    kongol.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    final Map<EquipmentSlot, Equipment> miranda = event.gameState.charData_32c[8].equipment_14;
    miranda.put(EquipmentSlot.WEAPON, LodEquipment.SHORT_BOW.get());
    miranda.put(EquipmentSlot.HELMET, LodEquipment.FELT_HAT.get());
    miranda.put(EquipmentSlot.ARMOUR, LodEquipment.CLOTHES.get());
    miranda.put(EquipmentSlot.BOOTS, LodEquipment.LEATHER_SHOES.get());
    miranda.put(EquipmentSlot.ACCESSORY, LodEquipment.BRACELET.get());

    event.gameState.gold_94 = 20;
  }
}
