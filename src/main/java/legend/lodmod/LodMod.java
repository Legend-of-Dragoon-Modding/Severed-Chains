package legend.lodmod;

import com.github.slugify.Slugify;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.core.GameEngine;
import legend.game.EngineStateType;
import legend.game.RegisterEngineStateTypeEvent;
import legend.game.combat.Battle;
import legend.game.credits.Credits;
import legend.game.credits.FinalFmv;
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.inventory.SpellRegistryEvent;
import legend.game.modding.events.gamestate.NewGameEvent;
import legend.game.saves.types.EnhancedSaveDisplay;
import legend.game.saves.types.EnhancedSaveType;
import legend.game.saves.types.RetailSaveDisplay;
import legend.game.saves.types.RetailSaveType;
import legend.game.saves.types.SaveType;
import legend.game.saves.types.SaveTypeRegistryEvent;
import legend.game.submap.SMap;
import legend.game.title.GameOver;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.EquipmentStats1c;
import legend.game.types.ItemStats0c;
import legend.game.types.SpellStats0c;
import legend.game.unpacker.Unpacker;
import legend.game.wmap.WMap;
import legend.lodmod.items.CharmPotionItem;
import legend.lodmod.items.FileBasedItem;
import org.legendofdragoon.modloader.Mod;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Locale;
import java.util.Map;

import static legend.game.SItem.equipmentStats_80111ff0;
import static legend.game.SItem.itemDescriptions_80117a10;
import static legend.game.SItem.itemNames_8011972c;
import static legend.game.SItem.itemPrices_80114310;
import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.xpTables;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8005.itemCombatDescriptions_80051758;
import static legend.game.Scus94491BpeSegment_8005.spellCombatDescriptions_80052018;
import static legend.game.Scus94491BpeSegment_8005.spells_80052734;
import static legend.game.combat.Battle.spellStats_800fa0b8;

/** Will eventually contain standard LOD content. Will be able to be disabled for total overhaul mods. */
@Mod(id = LodMod.MOD_ID)
@EventListener
public class LodMod {
  public static final String MOD_ID = "lod";

  private static final Registrar<SaveType<?>, SaveTypeRegistryEvent> SAVE_TYPE_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.saveTypes, MOD_ID);
  public static final RegistryDelegate<SaveType<RetailSaveDisplay>> RETAIL_SAVE_TYPE = SAVE_TYPE_REGISTRAR.register("retail", RetailSaveType::new);
  public static final RegistryDelegate<SaveType<EnhancedSaveDisplay>> ENHANCED_SAVE_TYPE = SAVE_TYPE_REGISTRAR.register("enhanced", EnhancedSaveType::new);

  private static final Registrar<EngineStateType<?>, RegisterEngineStateTypeEvent> ENGINE_STATE_TYPE_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.engineStateTypes, MOD_ID);
  public static final RegistryDelegate<EngineStateType<SMap>> SUBMAP_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("submap", () -> new EngineStateType<>(SMap.class, SMap::new));
  public static final RegistryDelegate<EngineStateType<WMap>> WORLD_MAP_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("world_map", () -> new EngineStateType<>(WMap.class, WMap::new));
  public static final RegistryDelegate<EngineStateType<Battle>> BATTLE_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("battle", () -> new EngineStateType<>(Battle.class, Battle::new));
  public static final RegistryDelegate<EngineStateType<GameOver>> GAME_OVER_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("game_over", () -> new EngineStateType<>(GameOver.class, GameOver::new));
  public static final RegistryDelegate<EngineStateType<FinalFmv>> FINAL_FMV_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("final_fmv", () -> new EngineStateType<>(FinalFmv.class, FinalFmv::new));
  public static final RegistryDelegate<EngineStateType<Credits>> CREDITS_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("credits", () -> new EngineStateType<>(Credits.class, Credits::new));

  private static final Slugify slug = Slugify.builder().locale(Locale.US).underscoreSeparator(true).customReplacement("'", "").customReplacement("-", "_").build();

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
      String name = itemNames_8011972c[itemId + 0xc0];
      if(name.isEmpty()) {
        name = "Item " + itemId;
      }

      if(itemStats_8004f2ac[itemId] == null) {
        itemStats_8004f2ac[itemId] = ItemStats0c.fromFile(name, itemDescriptions_80117a10[itemId + 0xc0], itemCombatDescriptions_80051758[itemId], Unpacker.loadFile("items/" + itemId + ".ditm"));
      }

      final Item item;
      if(itemId != 0x1f) { // Charm Potion
        item = FileBasedItem.fromFile(name, itemDescriptions_80117a10[itemId + 0xc0], itemCombatDescriptions_80051758[itemId], itemPrices_80114310[itemId + 192], Unpacker.loadFile("items/" + itemId + ".ditm"));
      } else {
        item = new CharmPotionItem(name, itemDescriptions_80117a10[itemId + 0xc0], itemCombatDescriptions_80051758[itemId], itemPrices_80114310[itemId + 192]);
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
      final String name = itemNames_8011972c[equipmentId];

      if(equipmentStats_80111ff0[equipmentId] == null) {
        equipmentStats_80111ff0[equipmentId] = EquipmentStats1c.fromFile(name, itemDescriptions_80117a10[equipmentId], Unpacker.loadFile("equipment/" + equipmentId + ".deqp"));
      }

      if(!name.isEmpty()) {
        final Equipment equipment = event.register(id(slug.slugify(name)), Equipment.fromFile(name, itemDescriptions_80117a10[equipmentId], itemPrices_80114310[equipmentId], Unpacker.loadFile("equipment/" + equipmentId + ".deqp")));
        equipmentIdMap.put(equipmentId, equipment.getRegistryId());
        idEquipmentMap.put(equipment.getRegistryId(), equipmentId);
      }
    }
  }

  @EventListener
  public static void registerSpells(final SpellRegistryEvent event) {
    for(int spellId = 0; spellId < spellStats_800fa0b8.length; spellId++) {
      if(spellStats_800fa0b8[spellId] == null) {
        final String name = spellId < 84 ? spells_80052734[spellId] : "Spell " + spellId;
        final String desc = spellId < 84 ? spellCombatDescriptions_80052018[spellId] : "";
        spellStats_800fa0b8[spellId] = SpellStats0c.fromFile(name, desc, Unpacker.loadFile("spells/" + spellId + ".dspl"));
      }
    }
  }

  @EventListener
  public static void registerSaveTypes(final SaveTypeRegistryEvent event) {
    SAVE_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerEngineStates(final RegisterEngineStateTypeEvent event) {
    ENGINE_STATE_TYPE_REGISTRAR.registryEvent(event);
  }

  private static final int[] characterStartingLevels = {1, 3, 4, 8, 13, 15, 17, 19, 23};
  private static final int[] startingAddition_800ce758 = {0, 8, -1, 14, 29, 8, 23, 19, -1};

  @EventListener
  public static void newGame(final NewGameEvent event) {
    event.gameState.charIds_88[0] = 0;
    event.gameState.charIds_88[1] = -1;
    event.gameState.charIds_88[2] = -1;

    //LAB_800c723c
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final CharacterData2c charData = event.gameState.charData_32c[charIndex];
      final int level = characterStartingLevels[charIndex];
      charData.xp_00 = xpTables[charIndex][level];
      charData.hp_08 = levelStuff_80111cfc[charIndex][level].hp_00;
      charData.mp_0a = magicStuff_80111d20[charIndex][1].mp_00;
      charData.sp_0c = 0;
      charData.dlevelXp_0e = 0;
      charData.status_10 = 0;
      charData.level_12 = level;
      charData.dlevel_13 = 1;

      //LAB_800c7294
      for(int additionIndex = 0; additionIndex < 8; additionIndex++) {
        charData.additionLevels_1a[additionIndex] = 0;
        charData.additionXp_22[additionIndex] = 0;
      }

      charData.additionLevels_1a[0] = 1;

      //LAB_800c72d4
      for(int i = 1; i < level; i++) {
        final int index = levelStuff_80111cfc[charIndex][i].addition_02;

        if(index != -1) {
          final int offset = additionOffsets_8004f5ac[charIndex];
          charData.additionLevels_1a[index - offset] = 1;
        }

        //LAB_800c72fc
      }

      //LAB_800c730c
      charData.selectedAddition_19 = startingAddition_800ce758[charIndex];
    }

    event.gameState.charData_32c[0].partyFlags_04 = 0x3;

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
    shana.put(EquipmentSlot.ARMOUR, LodEquipment.CLOTHES.get());
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
