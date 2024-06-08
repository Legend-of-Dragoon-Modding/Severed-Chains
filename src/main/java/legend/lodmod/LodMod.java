package legend.lodmod;

import com.github.slugify.Slugify;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.core.GameEngine;
import legend.game.EngineStateType;
import legend.game.EngineStateTypeRegistryEvent;
import legend.game.combat.Battle;
import legend.game.credits.Credits;
import legend.game.credits.FinalFmv;
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.inventory.SpellRegistryEvent;
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
  public static void registerEngineStates(final EngineStateTypeRegistryEvent event) {
    ENGINE_STATE_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerCampaignTypes(final CampaignTypeRegistryEvent event) {
    CAMPAIGN_TYPE_REGISTRAR.registryEvent(event);
  }
}
