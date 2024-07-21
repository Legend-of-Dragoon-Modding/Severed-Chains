package legend.lodmod;

import com.github.slugify.Slugify;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.core.GameEngine;
import legend.game.EngineStateType;
import legend.game.EngineStateTypeRegistryEvent;
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
import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntityType;
import legend.game.combat.bent.BattleEntityTypeRegistryEvent;
import legend.game.credits.Credits;
import legend.game.credits.FinalFmv;
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
import legend.game.saves.campaigns.CampaignType;
import legend.game.saves.campaigns.CampaignTypeRegistryEvent;
import legend.game.saves.types.EnhancedSaveDisplay;
import legend.game.saves.types.EnhancedSaveType;
import legend.game.saves.types.RetailSaveDisplay;
import legend.game.saves.types.RetailSaveType;
import legend.game.saves.types.SaveType;
import legend.game.saves.types.SaveTypeRegistryEvent;
import legend.game.submap.SMap;
import legend.game.title.GameOver;
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

import static legend.game.SItem.equipmentStats_80111ff0;
import static legend.game.SItem.itemDescriptions_80117a10;
import static legend.game.SItem.itemNames_8011972c;
import static legend.game.SItem.itemPrices_80114310;
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

  private static final Registrar<EngineStateType<?>, EngineStateTypeRegistryEvent> ENGINE_STATE_TYPE_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.engineStateTypes, MOD_ID);
  public static final RegistryDelegate<EngineStateType<SMap>> SUBMAP_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("submap", () -> new EngineStateType<>(SMap.class, SMap::new));
  public static final RegistryDelegate<EngineStateType<WMap>> WORLD_MAP_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("world_map", () -> new EngineStateType<>(WMap.class, WMap::new));
  public static final RegistryDelegate<EngineStateType<Battle>> BATTLE_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("battle", () -> new EngineStateType<>(Battle.class, Battle::new));
  public static final RegistryDelegate<EngineStateType<GameOver>> GAME_OVER_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("game_over", () -> new EngineStateType<>(GameOver.class, GameOver::new));
  public static final RegistryDelegate<EngineStateType<FinalFmv>> FINAL_FMV_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("final_fmv", () -> new EngineStateType<>(FinalFmv.class, FinalFmv::new));
  public static final RegistryDelegate<EngineStateType<Credits>> CREDITS_STATE_TYPE = ENGINE_STATE_TYPE_REGISTRAR.register("credits", () -> new EngineStateType<>(Credits.class, Credits::new));

  private static final Registrar<CampaignType, CampaignTypeRegistryEvent> CAMPAIGN_TYPE_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.campaignTypes, MOD_ID);
  public static final RegistryDelegate<CampaignType> LEGEND_OF_DRAGOON_CAMPAIGN_TYPE = CAMPAIGN_TYPE_REGISTRAR.register("legend_of_dragoon", LegendOfDragoonCampaign::new);

  private static final Slugify slug = Slugify.builder().locale(Locale.US).underscoreSeparator(true).customReplacement("'", "").customReplacement("-", "_").build();

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
  }

  @EventListener
  public static void registerSaveTypes(final SaveTypeRegistryEvent event) {
    SAVE_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerEngineStates(final EngineStateTypeRegistryEvent event) {
    ENGINE_STATE_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerCampaignTypes(final CampaignTypeRegistryEvent event) {
    CAMPAIGN_TYPE_REGISTRAR.registryEvent(event);
  }
}
