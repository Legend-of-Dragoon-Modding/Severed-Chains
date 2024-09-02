package legend.lodmod;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.core.GameEngine;
import legend.game.characters.Element;
import legend.game.characters.ElementRegistryEvent;
import legend.game.characters.FractionalStat;
import legend.game.characters.FractionalStatMod;
import legend.game.characters.FractionalStatModConfig;
import legend.game.characters.FractionalStatModType;
import legend.game.characters.StatModType;
import legend.game.characters.StatModTypeRegistryEvent;
import legend.game.characters.StatType;
import legend.game.characters.StatTypeRegistryEvent;
import legend.game.characters.UnaryStat;
import legend.game.characters.UnaryStatMod;
import legend.game.characters.UnaryStatModConfig;
import legend.game.characters.UnaryStatModType;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.BattleEntityType;
import legend.game.combat.bent.BattleEntityTypeRegistryEvent;
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.inventory.SpellRegistryEvent;
import legend.game.modding.coremod.elements.DarkElement;
import legend.game.modding.coremod.elements.DivineElement;
import legend.game.modding.coremod.elements.EarthElement;
import legend.game.modding.coremod.elements.FireElement;
import legend.game.modding.coremod.elements.LightElement;
import legend.game.modding.coremod.elements.NoElement;
import legend.game.modding.coremod.elements.ThunderElement;
import legend.game.modding.coremod.elements.WaterElement;
import legend.game.modding.coremod.elements.WindElement;
import legend.game.modding.events.battle.RegisterBattleEntityStatsEvent;
import legend.game.modding.events.gamestate.NewGameEvent;
import legend.game.types.EquipmentSlot;
import legend.game.types.SpellStats0c;
import legend.game.unpacker.Unpacker;
import legend.lodmod.items.CharmPotionItem;
import legend.lodmod.items.FileBasedItem;
import org.legendofdragoon.modloader.Mod;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Map;

import static legend.game.SItem.itemPrices_80114310;
import static legend.game.Scus94491BpeSegment_8005.spellCombatDescriptions_80052018;
import static legend.game.Scus94491BpeSegment_8005.spells_80052734;
import static legend.game.combat.Battle.spellStats_800fa0b8;

/** Will eventually contain standard LOD content. Will be able to be disabled for total overhaul mods. */
@Mod(id = LodMod.MOD_ID)
@EventListener
public class LodMod {
  public static final String MOD_ID = "lod";

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  private static final Registrar<StatType<?>, StatTypeRegistryEvent> STAT_TYPE_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.statTypes, MOD_ID);
  public static final RegistryDelegate<StatType<VitalsStat>> HP_STAT = STAT_TYPE_REGISTRAR.register("hp", () -> new StatType<>(VitalsStat::new));
  public static final RegistryDelegate<StatType<VitalsStat>> MP_STAT = STAT_TYPE_REGISTRAR.register("mp", () -> new StatType<>(VitalsStat::new));
  public static final RegistryDelegate<StatType<VitalsStat>> SP_STAT = STAT_TYPE_REGISTRAR.register("sp", () -> new StatType<>(VitalsStat::new));

  public static final RegistryDelegate<StatType<UnaryStat>> SPEED_STAT = STAT_TYPE_REGISTRAR.register("speed", () -> new StatType<>(UnaryStat::new));

  private static final Registrar<StatModType<?, ?, ?>, StatModTypeRegistryEvent> STAT_MOD_TYPE_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.statModTypes, MOD_ID);
  public static final RegistryDelegate<StatModType<UnaryStat, UnaryStatMod, UnaryStatModConfig>> UNARY_STAT_MOD_TYPE = STAT_MOD_TYPE_REGISTRAR.register("unary", UnaryStatModType::new);
  public static final RegistryDelegate<StatModType<FractionalStat, FractionalStatMod, FractionalStatModConfig>> FRACTIONAL_STAT_MOD_TYPE = STAT_MOD_TYPE_REGISTRAR.register("fractional", FractionalStatModType::new);

  private static final Registrar<Element, ElementRegistryEvent> ELEMENT_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.elements, MOD_ID);
  public static final RegistryDelegate<Element> NO_ELEMENT = ELEMENT_REGISTRAR.register("none", NoElement::new);
  public static final RegistryDelegate<Element> WATER_ELEMENT = ELEMENT_REGISTRAR.register("water", WaterElement::new);
  public static final RegistryDelegate<Element> EARTH_ELEMENT = ELEMENT_REGISTRAR.register("earth", EarthElement::new);
  public static final RegistryDelegate<Element> DARK_ELEMENT = ELEMENT_REGISTRAR.register("dark", DarkElement::new);
  public static final RegistryDelegate<Element> DIVINE_ELEMENT = ELEMENT_REGISTRAR.register("divine", DivineElement::new);
  public static final RegistryDelegate<Element> THUNDER_ELEMENT = ELEMENT_REGISTRAR.register("thunder", ThunderElement::new);
  public static final RegistryDelegate<Element> LIGHT_ELEMENT = ELEMENT_REGISTRAR.register("light", LightElement::new);
  public static final RegistryDelegate<Element> WIND_ELEMENT = ELEMENT_REGISTRAR.register("wind", WindElement::new);
  public static final RegistryDelegate<Element> FIRE_ELEMENT = ELEMENT_REGISTRAR.register("fire", FireElement::new);

  private static final Registrar<BattleEntityType, BattleEntityTypeRegistryEvent> BENT_TYPE_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.battleEntityTypes, MOD_ID);
  public static final RegistryDelegate<BattleEntityType> PLAYER_TYPE = BENT_TYPE_REGISTRAR.register("player", BattleEntityType::new);
  public static final RegistryDelegate<BattleEntityType> MONSTER_TYPE = BENT_TYPE_REGISTRAR.register("monster", BattleEntityType::new);

  @Deprecated
  public static final Int2ObjectMap<RegistryId> itemIdMap = new Int2ObjectOpenHashMap<>();
  @Deprecated
  public static final Object2IntMap<RegistryId> idItemMap = new Object2IntOpenHashMap<>();

  private static final String[] ITEM_IDS = {
    "", "detonate_rock", "spark_net", "burn_out", "", "pellet", "spear_frost", "spinning_gale",
    "attack_ball", "trans_light", "dark_mist", "healing_potion", "depetrifier", "mind_purifier", "body_purifier", "thunderbolt",
    "meteor_fall", "gushing_magma", "dancing_ray", "spirit_potion", "panic_bell", "", "fatal_blizzard", "stunning_hammer",
    "black_rain", "poison_needle", "midnight_terror", "", "rave_twister", "total_vanishing", "angels_prayer", "charm_potion",
    "pandemonium", "recovery_ball", "", "magic_shield", "material_shield", "sun_rhapsody", "smoke_ball", "healing_fog",
    "magic_sig_stone", "healing_rain", "moon_serenade", "power_up", "power_down", "speed_up", "speed_down", "enemy_healing_potion",
    "sachet", "psyche_bomb", "burning_wave", "frozen_jet", "down_burst", "gravity_grabber", "spectral_flash", "night_raid",
    "flash_hall", "healing_breeze", "psyche_bomb_x", "", "", "", "", ""
  };

  @EventListener
  public static void registerItems(final ItemRegistryEvent event) {
    for(int itemId = 0; itemId < 64; itemId++) {
      String name = ITEM_IDS[itemId];
      if(name.isEmpty()) {
        name = "item_" + itemId;
      }

      final Item item;
      if(itemId != 0x1f) { // Charm Potion
        item = FileBasedItem.fromFile(itemPrices_80114310[itemId + 192], Unpacker.loadFile("items/" + itemId + ".ditm"));
      } else {
        item = new CharmPotionItem(itemPrices_80114310[itemId + 192]);
      }

      event.register(id(name), item);
      itemIdMap.put(itemId, item.getRegistryId());
      idItemMap.put(item.getRegistryId(), itemId);
    }
  }

  @Deprecated
  public static final Int2ObjectMap<RegistryId> equipmentIdMap = new Int2ObjectOpenHashMap<>();
  @Deprecated
  public static final Object2IntMap<RegistryId> idEquipmentMap = new Object2IntOpenHashMap<>();

  private static final String[] EQUIPMENT_IDS = {"broad_sword", "bastard_sword", "heat_blade", "falchion", "mind_crush", "fairy_sword", "claymore", "soul_eater", "axe", "tomahawk", "battle_axe", "great_axe", "indoras_axe", "rapier", "shadow_cutter", "dancing_dagger", "flamberge", "gladius", "dragon_buster", "demon_stiletto", "spear", "lance", "glaive", "spear_of_terror", "partisan", "halberd", "twister_glaive", "short_bow", "sparkle_arrow", "long_bow", "bemusing_arrow", "virulent_arrow", "detonate_arrow", "arrow_of_force", "mace", "morning_star", "war_hammer", "heavy_mace", "basher", "pretty_hammer", "iron_knuckle", "beast_fang", "diamond_claw", "thunder_fist", "destroyer_mace", "brass_knuckle", "leather_armor", "scale_armor", "chain_mail", "plate_mail", "saint_armor", "red_dg_armor", "jade_dg_armor", "lion_fur", "breast_plate", "giganto_armor", "gold_dg_armor", "disciple_vest", "warrior_dress", "masters_vest", "energy_girdle", "violet_dg_armor", "clothes", "leather_jacket", "silver_vest", "sparkle_dress", "robe", "silver_dg_armor", "dark_dg_armor", "blue_dg_armor", "armor_of_yore", "satori_vest", "rainbow_dress", "angel_robe", "armor_of_legend", "", "bandana", "sallet", "armet", "knight_helm", "giganto_helm", "soul_headband", "felt_hat", "cape", "tiara", "jeweled_crown", "roses_hair_band", "", "phoenix_plume", "legend_casque", "dragon_helm", "magical_hat", "", "leather_boots", "iron_kneepiece", "combat_shoes", "leather_shoes", "soft_boots", "stardust_boots", "magical_greaves", "dancers_shoes", "bandits_shoes", "", "poison_guard", "active_ring", "protector", "panic_guard", "stun_guard", "bravery_amulet", "magic_ego_bell", "destone_amulet", "power_wrist", "knight_shield", "magical_ring", "spiritual_ring", "attack_badge", "guard_badge", "giganto_ring", "elude_cloak", "spirit_cloak", "sages_cloak", "physical_ring", "amulet", "wargods_sash", "spirit_ring", "therapy_ring", "mage_ring", "wargods_amulet", "talisman", "", "holy_ankh", "dancers_ring", "", "bandits_ring", "red_eye_stone", "jade_stone", "silver_stone", "darkness_stone", "blue_sea_stone", "violet_stone", "golden_stone", "", "ruby_ring", "sapphire_pin", "rainbow_earring", "", "emerald_earring", "", "platinum_collar", "phantom_shield", "dragon_shield", "angel_scarf", "bracelet", "fake_power_wrist", "fake_shield", "", "wargod_calling", "ultimate_wargod", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

  @EventListener
  public static void registerEquipment(final EquipmentRegistryEvent event) {
    equipmentIdMap.clear();
    idEquipmentMap.clear();

    for(int equipmentId = 0; equipmentId < 192; equipmentId++) {
      final String name = EQUIPMENT_IDS[equipmentId];

      if(!name.isEmpty()) {
        final Equipment equipment = event.register(id(name), Equipment.fromFile(itemPrices_80114310[equipmentId], Unpacker.loadFile("equipment/" + equipmentId + ".deqp")));
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
  public static void registerStatTypes(final StatTypeRegistryEvent event) {
    STAT_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerStatModTypes(final StatModTypeRegistryEvent event) {
    STAT_MOD_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerElements(final ElementRegistryEvent event) {
    ELEMENT_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerBentTypes(final BattleEntityTypeRegistryEvent event) {
    BENT_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerBentStats(final RegisterBattleEntityStatsEvent event) {
    event.addStat(HP_STAT.get());

    if(event.type == PLAYER_TYPE.get()) {
      event.addStat(MP_STAT.get());
      event.addStat(SP_STAT.get());
    }

    event.addStat(SPEED_STAT.get());
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
