package legend.lodmod;

import legend.core.gpu.Rect4i;
import legend.core.gpu.VramTextureLoader;
import legend.core.gpu.VramTextureSingle;
import legend.core.platform.input.AxisInputActivation;
import legend.core.platform.input.ButtonInputActivation;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputActionRegistryEvent;
import legend.core.platform.input.InputAxis;
import legend.core.platform.input.InputAxisDirection;
import legend.core.platform.input.InputButton;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.KeyInputActivation;
import legend.core.platform.input.ScancodeInputActivation;
import legend.game.RegisterEngineStateTypesEvent;
import legend.game.additions.AdditionRegistryEvent;
import legend.game.characters.Element;
import legend.game.characters.ElementRegistryEvent;
import legend.game.characters.FractionalStat;
import legend.game.characters.FractionalStatMod;
import legend.game.characters.FractionalStatModConfig;
import legend.game.characters.FractionalStatModType;
import legend.game.characters.RegisterCharacterTemplatesEvent;
import legend.game.characters.RegisterLevelUpActionsEvent;
import legend.game.characters.StatModType;
import legend.game.characters.StatModTypeRegistryEvent;
import legend.game.characters.StatType;
import legend.game.characters.StatTypeRegistryEvent;
import legend.game.characters.UnaryStat;
import legend.game.characters.UnaryStatMod;
import legend.game.characters.UnaryStatModConfig;
import legend.game.characters.UnaryStatModType;
import legend.game.characters.UnaryStatType;
import legend.game.characters.VitalsStat;
import legend.game.characters.VitalsStatType;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.BattleEntityType;
import legend.game.combat.bent.BattleEntityTypeRegistryEvent;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.deff.RegisterDeffsEvent;
import legend.game.combat.encounters.EncounterRegistryEvent;
import legend.game.combat.postbattleactions.RegisterPostBattleActionsEvent;
import legend.game.combat.ui.BattleAction;
import legend.game.combat.ui.GatherBattleActionsEvent;
import legend.game.combat.ui.RegisterBattleActionsEvent;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.EquipmentTypes;
import legend.game.inventory.GatherCharacterEquipmentTypesEvent;
import legend.game.inventory.GatherEquipmentTypesEvent;
import legend.game.inventory.GoodsRegistryEvent;
import legend.game.inventory.IconMapEvent;
import legend.game.inventory.IconSet;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.inventory.ItemStack;
import legend.game.inventory.ShopRegistryEvent;
import legend.game.inventory.SpellRegistryEvent;
import legend.game.modding.coremod.CoreMod;
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
import legend.game.modding.events.input.RegisterDefaultInputBindingsEvent;
import legend.game.modding.events.inventory.GatherAttackItemsEvent;
import legend.game.modding.events.inventory.GatherRecoveryItemsEvent;
import legend.game.saves.CampaignType;
import legend.game.saves.ConfigRegistryEvent;
import legend.game.saves.RegisterCampaignTypesEvent;
import legend.game.scripting.ScriptState;
import legend.game.textures.Image;
import legend.game.textures.RegisterAtlasTexturesEvent;
import legend.game.tim.Tim;
import legend.game.types.EquipmentSlot;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import org.apache.commons.lang3.stream.Streams;
import org.legendofdragoon.modloader.Mod;
import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.submapNames_8011c108;
import static legend.game.SItem.worldMapNames_8011c1ec;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_800b.encounter;
import static org.lwjgl.system.MemoryStack.stackPush;

/** Will eventually contain standard LOD content. Will be able to be disabled for total overhaul mods. */
@Mod(id = LodMod.MOD_ID, version = "^3.0.0")
@EventListener
public class LodMod {
  public static final String MOD_ID = "lod";

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  private static final Registrar<CampaignType, RegisterCampaignTypesEvent> CAMPAIGN_TYPE_REGISTRAR = new Registrar<>(REGISTRIES.campaignTypes, MOD_ID);

  public static final RegistryDelegate<CampaignType> RETAIL_CAMPAIGN_TYPE = CAMPAIGN_TYPE_REGISTRAR.register("retail", RetailCampaignType::new);

  private static final Registrar<InputAction, InputActionRegistryEvent> INPUT_ACTION_REGISTRAR = new Registrar<>(REGISTRIES.inputActions, MOD_ID);

  public static final RegistryDelegate<InputAction> INPUT_ACTION_GENERAL_OPEN_INVENTORY = INPUT_ACTION_REGISTRAR.register("general_open_inventory", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_GENERAL_MOVE_UP = INPUT_ACTION_REGISTRAR.register("general_move_up", InputAction.make().visible().editable().useMovementDeadzone().build());
  public static final RegistryDelegate<InputAction> INPUT_ACTION_GENERAL_MOVE_DOWN = INPUT_ACTION_REGISTRAR.register("general_move_down", InputAction.make().visible().editable().useMovementDeadzone().build());
  public static final RegistryDelegate<InputAction> INPUT_ACTION_GENERAL_MOVE_LEFT = INPUT_ACTION_REGISTRAR.register("general_move_left", InputAction.make().visible().editable().useMovementDeadzone().build());
  public static final RegistryDelegate<InputAction> INPUT_ACTION_GENERAL_MOVE_RIGHT = INPUT_ACTION_REGISTRAR.register("general_move_right", InputAction.make().visible().editable().useMovementDeadzone().build());
  public static final RegistryDelegate<InputAction> INPUT_ACTION_GENERAL_RUN = INPUT_ACTION_REGISTRAR.register("general_run", InputAction::editable);

  public static final RegistryDelegate<InputAction> INPUT_ACTION_WMAP_ROTATE_RIGHT = INPUT_ACTION_REGISTRAR.register("wmap_rotate_right", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_WMAP_ROTATE_LEFT = INPUT_ACTION_REGISTRAR.register("wmap_rotate_left", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_WMAP_ZOOM_OUT = INPUT_ACTION_REGISTRAR.register("wmap_zoom_out", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_WMAP_ZOOM_IN = INPUT_ACTION_REGISTRAR.register("wmap_zoom_in", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_WMAP_QUEEN_FURY_COOLON = INPUT_ACTION_REGISTRAR.register("wmap_queen_fury_coolon", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_WMAP_SERVICES = INPUT_ACTION_REGISTRAR.register("wmap_services", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_WMAP_DESTINATIONS = INPUT_ACTION_REGISTRAR.register("wmap_destinations", InputAction::editable);

  public static final RegistryDelegate<InputAction> INPUT_ACTION_SMAP_INTERACT = INPUT_ACTION_REGISTRAR.register("smap_interact", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_SMAP_TOGGLE_INDICATORS = INPUT_ACTION_REGISTRAR.register("smap_toggle_indicators", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_SMAP_SNOWFIELD_WARP = INPUT_ACTION_REGISTRAR.register("smap_snowfield_warp", InputAction::hidden);

  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_ATTACK = INPUT_ACTION_REGISTRAR.register("bttl_attack", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_COUNTER = INPUT_ACTION_REGISTRAR.register("bttl_counter", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_ROTATE_CAMERA = INPUT_ACTION_REGISTRAR.register("bttl_rotate_camera", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_ADDITIONS = INPUT_ACTION_REGISTRAR.register("bttl_additions", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_TRANSFORM = INPUT_ACTION_REGISTRAR.register("bttl_transform", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_SPECIAL = INPUT_ACTION_REGISTRAR.register("bttl_special", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_ESCAPE = INPUT_ACTION_REGISTRAR.register("bttl_escape", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_GUARD = INPUT_ACTION_REGISTRAR.register("bttl_guard", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_ITEMS = INPUT_ACTION_REGISTRAR.register("bttl_items", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_SPELLS = INPUT_ACTION_REGISTRAR.register("bttl_spells", InputAction::editable);
  public static final RegistryDelegate<InputAction> INPUT_ACTION_BTTL_OPTIONS = INPUT_ACTION_REGISTRAR.register("bttl_options", InputAction::editable);

  private static final Registrar<StatType<?>, StatTypeRegistryEvent> STAT_TYPE_REGISTRAR = new Registrar<>(REGISTRIES.statTypes, MOD_ID);
  public static final RegistryDelegate<StatType<VitalsStat>> HP_STAT = STAT_TYPE_REGISTRAR.register("hp", VitalsStatType::new);
  public static final RegistryDelegate<StatType<VitalsStat>> MP_STAT = STAT_TYPE_REGISTRAR.register("mp", VitalsStatType::new);
  public static final RegistryDelegate<StatType<VitalsStat>> SP_STAT = STAT_TYPE_REGISTRAR.register("sp", VitalsStatType::new);

  public static final RegistryDelegate<StatType<UnaryStat>> SPEED_STAT = STAT_TYPE_REGISTRAR.register("speed", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> ATTACK_STAT = STAT_TYPE_REGISTRAR.register("attack", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> MAGIC_ATTACK_STAT = STAT_TYPE_REGISTRAR.register("magic_attack", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> DEFENSE_STAT = STAT_TYPE_REGISTRAR.register("defense", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> MAGIC_DEFENSE_STAT = STAT_TYPE_REGISTRAR.register("magic_defense", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> ATTACK_HIT_STAT = STAT_TYPE_REGISTRAR.register("attack_hit", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> MAGIC_HIT_STAT = STAT_TYPE_REGISTRAR.register("magic_hit", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> ATTACK_AVOID_STAT = STAT_TYPE_REGISTRAR.register("attack_avoid", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> MAGIC_AVOID_STAT = STAT_TYPE_REGISTRAR.register("magic_avoid", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> DRAGOON_ATTACK_STAT = STAT_TYPE_REGISTRAR.register("dragoon_attack", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> DRAGOON_MAGIC_ATTACK_STAT = STAT_TYPE_REGISTRAR.register("dragoon_magic_attack", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> DRAGOON_DEFENSE_STAT = STAT_TYPE_REGISTRAR.register("dragoon_defense", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> DRAGOON_MAGIC_DEFENSE_STAT = STAT_TYPE_REGISTRAR.register("dragoon_magic_defense", UnaryStatType::new);
  public static final RegistryDelegate<StatType<UnaryStat>> GUARD_HEAL_STAT = STAT_TYPE_REGISTRAR.register("guard_heal", UnaryStatType::new);

  private static final Registrar<StatModType<?, ?, ?>, StatModTypeRegistryEvent> STAT_MOD_TYPE_REGISTRAR = new Registrar<>(REGISTRIES.statModTypes, MOD_ID);
  public static final RegistryDelegate<StatModType<UnaryStat, UnaryStatMod, UnaryStatModConfig>> UNARY_STAT_MOD_TYPE = STAT_MOD_TYPE_REGISTRAR.register("unary", UnaryStatModType::new);
  public static final RegistryDelegate<StatModType<FractionalStat, FractionalStatMod, FractionalStatModConfig>> FRACTIONAL_STAT_MOD_TYPE = STAT_MOD_TYPE_REGISTRAR.register("fractional", FractionalStatModType::new);

  private static final Registrar<Element, ElementRegistryEvent> ELEMENT_REGISTRAR = new Registrar<>(REGISTRIES.elements, MOD_ID);
  public static final RegistryDelegate<Element> NO_ELEMENT = ELEMENT_REGISTRAR.register("none", NoElement::new);
  public static final RegistryDelegate<Element> WATER_ELEMENT = ELEMENT_REGISTRAR.register("water", WaterElement::new);
  public static final RegistryDelegate<Element> EARTH_ELEMENT = ELEMENT_REGISTRAR.register("earth", EarthElement::new);
  public static final RegistryDelegate<Element> DARK_ELEMENT = ELEMENT_REGISTRAR.register("dark", DarkElement::new);
  public static final RegistryDelegate<Element> DIVINE_ELEMENT = ELEMENT_REGISTRAR.register("divine", DivineElement::new);
  public static final RegistryDelegate<Element> THUNDER_ELEMENT = ELEMENT_REGISTRAR.register("thunder", ThunderElement::new);
  public static final RegistryDelegate<Element> LIGHT_ELEMENT = ELEMENT_REGISTRAR.register("light", LightElement::new);
  public static final RegistryDelegate<Element> WIND_ELEMENT = ELEMENT_REGISTRAR.register("wind", WindElement::new);
  public static final RegistryDelegate<Element> FIRE_ELEMENT = ELEMENT_REGISTRAR.register("fire", FireElement::new);

  private static final Registrar<BattleEntityType, BattleEntityTypeRegistryEvent> BENT_TYPE_REGISTRAR = new Registrar<>(REGISTRIES.battleEntityTypes, MOD_ID);
  public static final RegistryDelegate<BattleEntityType> PLAYER_TYPE = BENT_TYPE_REGISTRAR.register("player", BattleEntityType::new);
  public static final RegistryDelegate<BattleEntityType> MONSTER_TYPE = BENT_TYPE_REGISTRAR.register("monster", BattleEntityType::new);

  public static final String[] ITEM_IDS = {
    "", "detonate_rock", "spark_net", "burn_out", "", "pellet", "spear_frost", "spinning_gale",
    "attack_ball", "trans_light", "dark_mist", "healing_potion", "depetrifier", "mind_purifier", "body_purifier", "thunderbolt",
    "meteor_fall", "gushing_magma", "dancing_ray", "spirit_potion", "panic_bell", "", "fatal_blizzard", "stunning_hammer",
    "black_rain", "poison_needle", "midnight_terror", "", "rave_twister", "total_vanishing", "angels_prayer", "charm_potion",
    "pandemonium", "recovery_ball", "", "magic_shield", "material_shield", "sun_rhapsody", "smoke_ball", "healing_fog",
    "magic_sig_stone", "healing_rain", "moon_serenade", "power_up", "power_down", "speed_up", "speed_down", "enemy_healing_potion",
    "sachet", "psyche_bomb", "burning_wave", "frozen_jet", "down_burst", "gravity_grabber", "spectral_flash", "night_raid",
    "flash_hall", "healing_breeze", "psyche_bomb_x", "", "", "", "", ""
  };

  public static final String[] EQUIPMENT_IDS = {
    "broad_sword", "bastard_sword", "heat_blade", "falchion", "mind_crush", "fairy_sword", "claymore", "soul_eater",
    "axe", "tomahawk", "battle_axe", "great_axe", "indoras_axe", "rapier", "shadow_cutter", "dancing_dagger",
    "flamberge", "gladius", "dragon_buster", "demon_stiletto", "spear", "lance", "glaive", "spear_of_terror",
    "partisan", "halberd", "twister_glaive", "short_bow", "sparkle_arrow", "long_bow", "bemusing_arrow", "virulent_arrow",
    "detonate_arrow", "arrow_of_force", "mace", "morning_star", "war_hammer", "heavy_mace", "basher", "pretty_hammer",
    "iron_knuckle", "beast_fang", "diamond_claw", "thunder_fist", "destroyer_mace", "brass_knuckle", "leather_armor", "scale_armor",
    "chain_mail", "plate_mail", "saint_armor", "red_dg_armor", "jade_dg_armor", "lion_fur", "breast_plate", "giganto_armor",
    "gold_dg_armor", "disciple_vest", "warrior_dress", "masters_vest", "energy_girdle", "violet_dg_armor", "clothes", "leather_jacket",
    "silver_vest", "sparkle_dress", "robe", "silver_dg_armor", "dark_dg_armor", "blue_dg_armor", "armor_of_yore", "satori_vest",
    "rainbow_dress", "angel_robe", "armor_of_legend", "", "bandana", "sallet", "armet", "knight_helm",
    "giganto_helm", "soul_headband", "felt_hat", "cape", "tiara", "jeweled_crown", "roses_hair_band", "",
    "phoenix_plume", "legend_casque", "dragon_helm", "magical_hat", "", "leather_boots", "iron_kneepiece", "combat_shoes",
    "leather_shoes", "soft_boots", "stardust_boots", "magical_greaves", "dancers_shoes", "bandits_shoes", "", "poison_guard",
    "active_ring", "protector", "panic_guard", "stun_guard", "bravery_amulet", "magic_ego_bell", "destone_amulet", "power_wrist",
    "knight_shield", "magical_ring", "spiritual_ring", "attack_badge", "guard_badge", "giganto_ring", "elude_cloak", "spirit_cloak",
    "sages_cloak", "physical_ring", "amulet", "wargods_sash", "spirit_ring", "therapy_ring", "mage_ring", "wargods_amulet",
    "talisman", "", "holy_ankh", "dancers_ring", "", "bandits_ring", "red_eye_stone", "jade_stone",
    "silver_stone", "darkness_stone", "blue_sea_stone", "violet_stone", "golden_stone", "", "ruby_ring", "sapphire_pin",
    "rainbow_earring", "", "emerald_earring", "", "platinum_collar", "phantom_shield", "dragon_shield", "angel_scarf",
    "bracelet", "fake_power_wrist", "fake_shield", "", "wargod_calling", "ultimate_wargod", "", "",
    "", "", "", "", "", "", "", "",
    "", "", "", "", "", "", "", "",
    "", "", "", "", "", "", "", "",
    "", "", "", "", "", "", "", ""
  };

  public static final String[] GOODS_IDS = {
    "red_dragoon_spirit", "blue_dragoon_spirit", "jade_dragoon_spirit", "gold_dragoon_spirit", "violet_dragoon_spirit",
    "silver_dragoon_spirit", "dark_dragoon_spirit", "divine_dragoon_spirit", "war_bulletin", "fathers_stone",
    "prison_key", "axe_from_shack", "good_spirits", "shiny_bag", "water_bottle",
    "life_water", "magic_oil", "yellow_stone", "blue_stone", "red_stone",
    "letter_from_lynn", "pass_for_valley", "kates_bouquet", "key_to_ship", "boat_license",
    "dragon_blocker", "moon_gem", "moon_dagger", "moon_mirror", "omega_bomb",
    "omega_master", "law_maker", "law_output", "gold_dragoon_spirit_2", "magic_shiny_bag",
    "vanishing_stone", "lavitzs_picture",
  };


  public static final String[] SPELL_IDS = {
    "flameshot", "explosion", "final_burst", "red_eyed_dragon", "divine_dg_cannon", "wing_blaster", "gaspless", "blossom_storm",
    "jade_dragon", "divine_dg_ball", "star_children", "moon_light", "gates_of_heaven", "white_silver_dragon", "wing_blaster", "astral_drain",
    "death_dimension", "gaspless", "demons_gate", "dark_dragon", "atomic_mind", "thunder_kid", "thunder_god", "violet_dragon",
    "freezing_ring", "rainbow_breath", "rose_storm", "diamond_dust", "blue_sea_dragon", "grand_stream", "meteor_strike", "golden_dragon",
    "spell32", "spell33", "spell34", "spell35", "spell36", "spell37", "spell38", "spell39",
    "spell40", "spell41", "spell42", "spell43", "spell44", "spell45", "spell46", "spell47",
    "spell48", "spell49", "spell50", "spell51", "spell52", "spell53", "spell54", "spell55",
    "spell56", "spell57", "spell58", "spell59", "spell60", "spell61", "spell62", "spell63",
    "spell64", "spell65", "spell66", "spell67", "spell68", "spell69", "spell70", "spell71",
    "spell72", "spell73", "spell74", "spell75", "spell76", "spell77", "spell78", "spell79",
    "spell80", "spell81", "spell82", "spell83", "spell84", "spell85", "spell86", "spell87",
    "spell88", "spell89", "spell90", "spell91", "spell92", "spell93", "spell94", "spell95",
    "spell96", "spell97", "spell98", "spell99", "spell100", "spell101", "spell102", "spell103",
    "spell104", "spell105", "spell106", "spell107", "spell108", "spell109", "spell110", "spell111",
    "spell112", "spell113", "spell114", "spell115", "spell116", "spell117", "spell118", "spell119",
    "spell120", "spell121", "spell122", "spell123", "spell124", "spell125", "spell126", "spell127",
  };

  public static final String[] SHOP_IDS = {
    "bale_equipment_shop", "serdio_item_shop", "lohan_equipment_shop", "lohan_item_shop",
    "kazas_equipment_shop", "kazas_fort_item_shop", "fletz_equipment_shop", "fletz_item_shop",
    "donau_equipment_shop", "donau_item_shop", "queen_fury_equipment_shop", "queen_fury_item_shop",
    "fueno_equipment_shop", "fueno_item_shop", "furni_equipment_shop", "furni_item_shop",
    "deningrad_equipment_shop", "deningrad_item_shop", "wingly_forest_equipment_shop", "wingly_forest_item_shop",
    "vellweb_equipment_shop", "vellweb_item_shop", "ulara_equipment_shop", "ulara_item_shop",
    "rouge_equipment_shop", "rouge_item_shop", "moon_equipment_shop", "moon_item_shop",
    "hellena_01_item_shop", "kashua_equipment_shop", "kashua_item_shop", "fletz_accessory_shop",
    "forest_item_shop", "kazas_fort_equipment_shop", "volcano_item_shop", "zenebatos_equipment_shop",
    "zenebatos_item_shop", "hellena_02_item_shop", "unknown_shop_01", "empty_shop", "empty_shop", "empty_shop"
    , "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop"
    , "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop",
    "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop", "empty_shop",
  };

  private static final RegistryDelegate<BattleAction>[] RETAIL_MENU_BLOCKS = new RegistryDelegate[] {
    LodBattleActions.ATTACK,
    LodBattleActions.GUARD,
    LodBattleActions.ITEMS,
    LodBattleActions.ESCAPE,
    LodBattleActions.TRANSFORM,
    LodBattleActions.D_ATTACK,
    LodBattleActions.SPELLS,
    LodBattleActions.SPECIAL,
  };

  public static int getShopIndex(final RegistryId shop) {
    for(int i = 0; i < SHOP_IDS.length; i++) {
      if(SHOP_IDS[i].equals(shop.entryId())) {
        return i;
      }
    }

    return -1;
  }

  public static String getLocationName(final int locationType, final int locationIndex) {
    final String[] locationNames;
    if(locationType == 1) {
      //LAB_80108b5c
      locationNames = worldMapNames_8011c1ec;
    } else if(locationType == 3) {
      //LAB_80108b78
      locationNames = chapterNames_80114248;
    } else {
      //LAB_80108b90
      locationNames = submapNames_8011c108;
    }

    //LAB_80108ba0
    if(locationIndex >= locationNames.length) {
      return "Unknown location";
    }

    return locationNames[locationIndex];
  }

  @EventListener
  public static void registerCampaignTypes(final RegisterCampaignTypesEvent event) {
    CAMPAIGN_TYPE_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerEngineStates(final RegisterEngineStateTypesEvent event) {
    LodEngineStateTypes.register(event);
  }

  @EventListener
  public static void registerInputActions(final InputActionRegistryEvent event) {
    INPUT_ACTION_REGISTRAR.registryEvent(event);
  }

  @EventListener
  public static void registerDefaultInputBindings(final RegisterDefaultInputBindingsEvent event) {
    event
      .add(INPUT_ACTION_GENERAL_OPEN_INVENTORY.get(), new ScancodeInputActivation(InputKey.E))
      .add(INPUT_ACTION_GENERAL_OPEN_INVENTORY.get(), new ButtonInputActivation(InputButton.Y))

      .add(INPUT_ACTION_GENERAL_MOVE_UP.get(), new ButtonInputActivation(InputButton.DPAD_UP))
      .add(INPUT_ACTION_GENERAL_MOVE_UP.get(), new AxisInputActivation(InputAxis.LEFT_Y, InputAxisDirection.NEGATIVE))
      .add(INPUT_ACTION_GENERAL_MOVE_UP.get(), new ScancodeInputActivation(InputKey.UP))
      .add(INPUT_ACTION_GENERAL_MOVE_UP.get(), new ScancodeInputActivation(InputKey.W))
      .add(INPUT_ACTION_GENERAL_MOVE_DOWN.get(), new ButtonInputActivation(InputButton.DPAD_DOWN))
      .add(INPUT_ACTION_GENERAL_MOVE_DOWN.get(), new AxisInputActivation(InputAxis.LEFT_Y, InputAxisDirection.POSITIVE))
      .add(INPUT_ACTION_GENERAL_MOVE_DOWN.get(), new ScancodeInputActivation(InputKey.DOWN))
      .add(INPUT_ACTION_GENERAL_MOVE_DOWN.get(), new ScancodeInputActivation(InputKey.S))
      .add(INPUT_ACTION_GENERAL_MOVE_LEFT.get(), new ButtonInputActivation(InputButton.DPAD_LEFT))
      .add(INPUT_ACTION_GENERAL_MOVE_LEFT.get(), new AxisInputActivation(InputAxis.LEFT_X, InputAxisDirection.NEGATIVE))
      .add(INPUT_ACTION_GENERAL_MOVE_LEFT.get(), new ScancodeInputActivation(InputKey.LEFT))
      .add(INPUT_ACTION_GENERAL_MOVE_LEFT.get(), new ScancodeInputActivation(InputKey.A))
      .add(INPUT_ACTION_GENERAL_MOVE_RIGHT.get(), new ButtonInputActivation(InputButton.DPAD_RIGHT))
      .add(INPUT_ACTION_GENERAL_MOVE_RIGHT.get(), new AxisInputActivation(InputAxis.LEFT_X, InputAxisDirection.POSITIVE))
      .add(INPUT_ACTION_GENERAL_MOVE_RIGHT.get(), new ScancodeInputActivation(InputKey.RIGHT))
      .add(INPUT_ACTION_GENERAL_MOVE_RIGHT.get(), new ScancodeInputActivation(InputKey.D))
      .add(INPUT_ACTION_GENERAL_RUN.get(), new ScancodeInputActivation(InputKey.LEFT_SHIFT))
      .add(INPUT_ACTION_GENERAL_RUN.get(), new ButtonInputActivation(InputButton.B))

      .add(INPUT_ACTION_WMAP_ROTATE_LEFT.get(), new ScancodeInputActivation(InputKey.R))
      .add(INPUT_ACTION_WMAP_ROTATE_LEFT.get(), new ButtonInputActivation(InputButton.LEFT_BUMPER))
      .add(INPUT_ACTION_WMAP_ROTATE_RIGHT.get(), new ScancodeInputActivation(InputKey.T))
      .add(INPUT_ACTION_WMAP_ROTATE_RIGHT.get(), new ButtonInputActivation(InputButton.RIGHT_BUMPER))
      .add(INPUT_ACTION_WMAP_ZOOM_OUT.get(), new ScancodeInputActivation(InputKey.Z))
      .add(INPUT_ACTION_WMAP_ZOOM_OUT.get(), new AxisInputActivation(InputAxis.LEFT_TRIGGER, InputAxisDirection.POSITIVE))
      .add(INPUT_ACTION_WMAP_ZOOM_IN.get(), new ScancodeInputActivation(InputKey.X))
      .add(INPUT_ACTION_WMAP_ZOOM_IN.get(), new AxisInputActivation(InputAxis.RIGHT_TRIGGER, InputAxisDirection.POSITIVE))
      .add(INPUT_ACTION_WMAP_QUEEN_FURY_COOLON.get(), new ScancodeInputActivation(InputKey.Q))
      .add(INPUT_ACTION_WMAP_QUEEN_FURY_COOLON.get(), new ButtonInputActivation(InputButton.X))
      .add(INPUT_ACTION_WMAP_SERVICES.get(), new ScancodeInputActivation(InputKey.Q))
      .add(INPUT_ACTION_WMAP_SERVICES.get(), new ButtonInputActivation(InputButton.X))
      .add(INPUT_ACTION_WMAP_DESTINATIONS.get(), new KeyInputActivation(InputKey.L))
      .add(INPUT_ACTION_WMAP_DESTINATIONS.get(), new ButtonInputActivation(InputButton.START))

      .add(INPUT_ACTION_SMAP_INTERACT.get(), new ScancodeInputActivation(InputKey.SPACE))
      .add(INPUT_ACTION_SMAP_INTERACT.get(), new ScancodeInputActivation(InputKey.RETURN))
      .add(INPUT_ACTION_SMAP_INTERACT.get(), new ButtonInputActivation(InputButton.A))
      .add(INPUT_ACTION_SMAP_TOGGLE_INDICATORS.get(), new ScancodeInputActivation(InputKey.Q))
      .add(INPUT_ACTION_SMAP_TOGGLE_INDICATORS.get(), new ButtonInputActivation(InputButton.RIGHT_BUMPER))
      .add(INPUT_ACTION_SMAP_SNOWFIELD_WARP.get(), new ButtonInputActivation(InputButton.START))

      .add(INPUT_ACTION_BTTL_ATTACK.get(), new ScancodeInputActivation(InputKey.SPACE))
      .add(INPUT_ACTION_BTTL_ATTACK.get(), new ButtonInputActivation(InputButton.A))
      .add(INPUT_ACTION_BTTL_COUNTER.get(), new ScancodeInputActivation(InputKey.LEFT_SHIFT))
      .add(INPUT_ACTION_BTTL_COUNTER.get(), new ButtonInputActivation(InputButton.B))
      .add(INPUT_ACTION_BTTL_ROTATE_CAMERA.get(), new ScancodeInputActivation(InputKey.Q))
      .add(INPUT_ACTION_BTTL_ROTATE_CAMERA.get(), new ButtonInputActivation(InputButton.RIGHT_BUMPER))
      .add(INPUT_ACTION_BTTL_ADDITIONS.get(), new ScancodeInputActivation(InputKey.E))
      .add(INPUT_ACTION_BTTL_ADDITIONS.get(), new ButtonInputActivation(InputButton.Y))
      .add(INPUT_ACTION_BTTL_OPTIONS.get(), new ScancodeInputActivation(InputKey.ESCAPE))
      .add(INPUT_ACTION_BTTL_OPTIONS.get(), new ButtonInputActivation(InputButton.START))
      .add(INPUT_ACTION_BTTL_GUARD.get(), new ScancodeInputActivation(InputKey.G))
    ;
  }

  @EventListener
  public static void registerConfig(final ConfigRegistryEvent event) {
    LodConfig.register(event);
  }

  @EventListener
  public static void registerCharacterTemplates(final RegisterCharacterTemplatesEvent event) {
    LodCharacterTemplates.register(event);
  }

  @EventListener
  public static void registerLevelUpActions(final RegisterLevelUpActionsEvent event) {
    LodLevelUpActions.register(event);
  }

  @EventListener
  public static void registerItems(final ItemRegistryEvent event) {
    LodItems.register(event);
  }

  @EventListener
  public static void registerEquipment(final EquipmentRegistryEvent event) {
    LodEquipment.register(event);
  }

  @EventListener
  public static void registerGoods(final GoodsRegistryEvent event) {
    LodGoods.register(event);
  }

  @EventListener
  public static void registerShops(final ShopRegistryEvent event) {
    LodShops.register(event);
  }

  @EventListener
  public static void registerEncounters(final EncounterRegistryEvent event) {
    LodEncounters.register(event);
  }

  @EventListener
  public static void registerSpells(final SpellRegistryEvent event) {
    LodSpells.register(event);
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
      event.addStat(GUARD_HEAL_STAT.get());
    }

    event.addStat(SPEED_STAT.get());
    event.addStat(ATTACK_STAT.get());
    event.addStat(MAGIC_ATTACK_STAT.get());
    event.addStat(DEFENSE_STAT.get());
    event.addStat(MAGIC_DEFENSE_STAT.get());
    event.addStat(ATTACK_AVOID_STAT.get());
    event.addStat(MAGIC_AVOID_STAT.get());
  }

  @EventListener
  public static void registerDeffs(final RegisterDeffsEvent event) {
    LodDeffs.register(event);
  }

  @EventListener
  public static void registerAdditions(final AdditionRegistryEvent event) {
    LodAdditions.register(event);
  }

  @EventListener
  public static void registerBattleActions(final RegisterBattleActionsEvent event) {
    LodBattleActions.register(event);
  }

  @EventListener
  public static void registerPostBattleActions(final RegisterPostBattleActionsEvent event) {
    LodPostBattleActions.register(event);
  }

  @EventListener
  public static void gatherEquipmentTypes(final GatherEquipmentTypesEvent event) {
    event.add(LodEquipment.ACTIVE_RING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.AMULET.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.ANGEL_ROBE.get(), EquipmentTypes.LIGHT);
    event.add(LodEquipment.ANGEL_SCARF.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.ARMET.get(), EquipmentTypes.MALE);
    event.add(LodEquipment.ARMOR_OF_LEGEND.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.ARMOR_OF_YORE.get(), EquipmentTypes.ARMOR_OF_YORE);
    event.add(LodEquipment.ARROW_OF_FORCE.get(), EquipmentTypes.BOW);
    event.add(LodEquipment.ATTACK_BADGE.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.AXE.get(), EquipmentTypes.AXE);
    event.add(LodEquipment.BANDANA.get(), EquipmentTypes.MALE);
    event.add(LodEquipment.BANDITS_RING.get(), EquipmentTypes.MALE);
    event.add(LodEquipment.BANDITS_SHOES.get(), EquipmentTypes.MALE);
    event.add(LodEquipment.BASHER.get(), EquipmentTypes.HAMMER);
    event.add(LodEquipment.BASTARD_SWORD.get(), EquipmentTypes.LONGSWORD);
    event.add(LodEquipment.BATTLE_AXE.get(), EquipmentTypes.AXE);
    event.add(LodEquipment.BEAST_FANG.get(), EquipmentTypes.HAND);
    event.add(LodEquipment.BEMUSING_ARROW.get(), EquipmentTypes.BOW);
    event.add(LodEquipment.BLUE_DG_ARMOR.get(), EquipmentTypes.MERU);
    event.add(LodEquipment.BLUE_SEA_STONE.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.BRACELET.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.BRASS_KNUCKLE.get(), EquipmentTypes.HAND);
    event.add(LodEquipment.BRAVERY_AMULET.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.BREAST_PLATE.get(), EquipmentTypes.KONGOL);
    event.add(LodEquipment.BROAD_SWORD.get(), EquipmentTypes.LONGSWORD);
    event.add(LodEquipment.CAPE.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.CHAIN_MAIL.get(), EquipmentTypes.HEAVY);
    event.add(LodEquipment.CLAYMORE.get(), EquipmentTypes.LONGSWORD);
    event.add(LodEquipment.CLOTHES.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.COMBAT_SHOES.get(), EquipmentTypes.MALE);
    event.add(LodEquipment.DANCERS_RING.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.DANCERS_SHOES.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.DANCING_DAGGER.get(), EquipmentTypes.SHORTSWORD);
    event.add(LodEquipment.DARK_DG_ARMOR.get(), EquipmentTypes.ROSE);
    event.add(LodEquipment.DARKNESS_STONE.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.DEMON_STILETTO.get(), EquipmentTypes.SHORTSWORD);
    event.add(LodEquipment.DESTONE_AMULET.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.DESTROYER_MACE.get(), EquipmentTypes.HAND);
    event.add(LodEquipment.DETONATE_ARROW.get(), EquipmentTypes.BOW);
    event.add(LodEquipment.DIAMOND_CLAW.get(), EquipmentTypes.HAND);
    event.add(LodEquipment.DISCIPLE_VEST.get(), EquipmentTypes.HASCHEL);
    event.add(LodEquipment.DRAGON_BUSTER.get(), EquipmentTypes.SHORTSWORD);
    event.add(LodEquipment.DRAGON_HELM.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.DRAGON_SHIELD.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.ELUDE_CLOAK.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.EMERALD_EARRING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.ENERGY_GIRDLE.get(), EquipmentTypes.HASCHEL);
    event.add(LodEquipment.FAIRY_SWORD.get(), EquipmentTypes.LONGSWORD);
    event.add(LodEquipment.FAKE_POWER_WRIST.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.FAKE_SHIELD.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.FALCHION.get(), EquipmentTypes.LONGSWORD);
    event.add(LodEquipment.FELT_HAT.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.FLAMBERGE.get(), EquipmentTypes.SHORTSWORD);
    event.add(LodEquipment.GIGANTO_ARMOR.get(), EquipmentTypes.KONGOL);
    event.add(LodEquipment.GIGANTO_HELM.get(), EquipmentTypes.KONGOL);
    event.add(LodEquipment.GIGANTO_RING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.GLADIUS.get(), EquipmentTypes.SHORTSWORD);
    event.add(LodEquipment.GLAIVE.get(), EquipmentTypes.POLEARM);
    event.add(LodEquipment.GOLD_DG_ARMOR.get(), EquipmentTypes.KONGOL);
    event.add(LodEquipment.GOLDEN_STONE.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.GREAT_AXE.get(), EquipmentTypes.AXE);
    event.add(LodEquipment.GUARD_BADGE.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.HALBERD.get(), EquipmentTypes.POLEARM);
    event.add(LodEquipment.HEAT_BLADE.get(), EquipmentTypes.LONGSWORD);
    event.add(LodEquipment.HEAVY_MACE.get(), EquipmentTypes.HAMMER);
    event.add(LodEquipment.HOLY_ANKH.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.INDORAS_AXE.get(), EquipmentTypes.AXE);
    event.add(LodEquipment.IRON_KNEEPIECE.get(), EquipmentTypes.MALE);
    event.add(LodEquipment.IRON_KNUCKLE.get(), EquipmentTypes.HAND);
    event.add(LodEquipment.JADE_DG_ARMOR.get(), EquipmentTypes.LAVITZ, EquipmentTypes.ALBERT);
    event.add(LodEquipment.JADE_STONE.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.JEWELED_CROWN.get(), EquipmentTypes.LIGHT);
    event.add(LodEquipment.KNIGHT_HELM.get(), EquipmentTypes.HEAVY);
    event.add(LodEquipment.KNIGHT_SHIELD.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.LANCE.get(), EquipmentTypes.POLEARM);
    event.add(LodEquipment.LEATHER_ARMOR.get(), EquipmentTypes.HEAVY);
    event.add(LodEquipment.LEATHER_BOOTS.get(), EquipmentTypes.MALE);
    event.add(LodEquipment.LEATHER_JACKET.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.LEATHER_SHOES.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.LEGEND_CASQUE.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.LION_FUR.get(), EquipmentTypes.KONGOL);
    event.add(LodEquipment.LONG_BOW.get(), EquipmentTypes.BOW);
    event.add(LodEquipment.MACE.get(), EquipmentTypes.HAMMER);
    event.add(LodEquipment.MAGE_RING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.MAGIC_EGO_BELL.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.MAGICAL_GREAVES.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.MAGICAL_HAT.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.MAGICAL_RING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.MASTERS_VEST.get(), EquipmentTypes.HASCHEL);
    event.add(LodEquipment.MIND_CRUSH.get(), EquipmentTypes.LONGSWORD);
    event.add(LodEquipment.MORNING_STAR.get(), EquipmentTypes.HAMMER);
    event.add(LodEquipment.PANIC_GUARD.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.PARTISAN.get(), EquipmentTypes.POLEARM);
    event.add(LodEquipment.PHANTOM_SHIELD.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.PHOENIX_PLUME.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.PHYSICAL_RING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.PLATE_MAIL.get(), EquipmentTypes.HEAVY);
    event.add(LodEquipment.PLATINUM_COLLAR.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.POISON_GUARD.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.POWER_WRIST.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.PRETTY_HAMMER.get(), EquipmentTypes.HAMMER);
    event.add(LodEquipment.PROTECTOR.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.RAINBOW_DRESS.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.RAINBOW_EARRING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.RAPIER.get(), EquipmentTypes.SHORTSWORD);
    event.add(LodEquipment.RED_DG_ARMOR.get(), EquipmentTypes.DART);
    event.add(LodEquipment.RED_EYE_STONE.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.ROBE.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.ROSES_HAIR_BAND.get(), EquipmentTypes.ROSE);
    event.add(LodEquipment.RUBY_RING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.SAGES_CLOAK.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.SAINT_ARMOR.get(), EquipmentTypes.HEAVY);
    event.add(LodEquipment.SALLET.get(), EquipmentTypes.MALE);
    event.add(LodEquipment.SAPPHIRE_PIN.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.SATORI_VEST.get(), EquipmentTypes.HASCHEL);
    event.add(LodEquipment.SCALE_ARMOR.get(), EquipmentTypes.HEAVY);
    event.add(LodEquipment.SHADOW_CUTTER.get(), EquipmentTypes.SHORTSWORD);
    event.add(LodEquipment.SHORT_BOW.get(), EquipmentTypes.BOW);
    event.add(LodEquipment.SILVER_DG_ARMOR.get(), EquipmentTypes.SHANA, EquipmentTypes.MIRANDA);
    event.add(LodEquipment.SILVER_STONE.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.SILVER_VEST.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.SOFT_BOOTS.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.SOUL_EATER.get(), EquipmentTypes.LONGSWORD);
    event.add(LodEquipment.SOUL_HEADBAND.get(), EquipmentTypes.HASCHEL);
    event.add(LodEquipment.SPARKLE_ARROW.get(), EquipmentTypes.BOW);
    event.add(LodEquipment.SPARKLE_DRESS.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.SPEAR.get(), EquipmentTypes.POLEARM);
    event.add(LodEquipment.SPEAR_OF_TERROR.get(), EquipmentTypes.POLEARM);
    event.add(LodEquipment.SPIRIT_CLOAK.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.SPIRIT_RING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.SPIRITUAL_RING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.STARDUST_BOOTS.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.STUN_GUARD.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.TALISMAN.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.THERAPY_RING.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.THUNDER_FIST.get(), EquipmentTypes.HAND);
    event.add(LodEquipment.TIARA.get(), EquipmentTypes.MEDIUM, EquipmentTypes.FEMALE);
    event.add(LodEquipment.TOMAHAWK.get(), EquipmentTypes.AXE);
    event.add(LodEquipment.TWISTER_GLAIVE.get(), EquipmentTypes.POLEARM);
    event.add(LodEquipment.ULTIMATE_WARGOD.get(), EquipmentTypes.ADDITIONS);
    event.add(LodEquipment.VIOLET_DG_ARMOR.get(), EquipmentTypes.HASCHEL);
    event.add(LodEquipment.VIOLET_STONE.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.VIRULENT_ARROW.get(), EquipmentTypes.BOW);
    event.add(LodEquipment.WAR_HAMMER.get(), EquipmentTypes.HAMMER);
    event.add(LodEquipment.WARGOD_CALLING.get(), EquipmentTypes.ADDITIONS);
    event.add(LodEquipment.WARGODS_AMULET.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.WARGODS_SASH.get(), EquipmentTypes.NEUTRAL);
    event.add(LodEquipment.WARRIOR_DRESS.get(), EquipmentTypes.HASCHEL);
  }

  @EventListener
  public static void gatherCharacterEquipmentTypes(final GatherCharacterEquipmentTypesEvent event) {
    event.add(LodCharacterTemplates.DART.get(), EquipmentTypes.DART, EquipmentTypes.LONGSWORD, EquipmentTypes.NEUTRAL, EquipmentTypes.MALE, EquipmentTypes.ADDITIONS, EquipmentTypes.HEAVY, EquipmentTypes.ARMOR_OF_YORE);
    event.add(LodCharacterTemplates.LAVITZ.get(), EquipmentTypes.LAVITZ, EquipmentTypes.POLEARM, EquipmentTypes.NEUTRAL, EquipmentTypes.MALE, EquipmentTypes.ADDITIONS, EquipmentTypes.HEAVY, EquipmentTypes.ARMOR_OF_YORE);
    event.add(LodCharacterTemplates.SHANA.get(), EquipmentTypes.SHANA, EquipmentTypes.BOW, EquipmentTypes.NEUTRAL, EquipmentTypes.FEMALE, EquipmentTypes.MEDIUM, EquipmentTypes.LIGHT);
    event.add(LodCharacterTemplates.ROSE.get(), EquipmentTypes.ROSE, EquipmentTypes.SHORTSWORD, EquipmentTypes.NEUTRAL, EquipmentTypes.FEMALE, EquipmentTypes.ADDITIONS, EquipmentTypes.MEDIUM);
    event.add(LodCharacterTemplates.HASCHEL.get(), EquipmentTypes.HASCHEL, EquipmentTypes.HAND, EquipmentTypes.NEUTRAL, EquipmentTypes.MALE, EquipmentTypes.ADDITIONS);
    event.add(LodCharacterTemplates.ALBERT.get(), EquipmentTypes.ALBERT, EquipmentTypes.POLEARM, EquipmentTypes.NEUTRAL, EquipmentTypes.MALE, EquipmentTypes.ADDITIONS, EquipmentTypes.HEAVY, EquipmentTypes.ARMOR_OF_YORE);
    event.add(LodCharacterTemplates.MERU.get(), EquipmentTypes.MERU, EquipmentTypes.HAMMER, EquipmentTypes.NEUTRAL, EquipmentTypes.FEMALE, EquipmentTypes.ADDITIONS);
    event.add(LodCharacterTemplates.KONGOL.get(), EquipmentTypes.KONGOL, EquipmentTypes.AXE, EquipmentTypes.NEUTRAL, EquipmentTypes.MALE, EquipmentTypes.ADDITIONS, EquipmentTypes.ARMOR_OF_YORE);
    event.add(LodCharacterTemplates.MIRANDA.get(), EquipmentTypes.MIRANDA, EquipmentTypes.BOW, EquipmentTypes.NEUTRAL, EquipmentTypes.FEMALE, EquipmentTypes.MEDIUM, EquipmentTypes.LIGHT);
  }

  @EventListener
  public static void gatherBattleActions(final GatherBattleActionsEvent event) {
    final PlayerBattleEntity player = event.player;
    int sort = 100;

    if(player.isDragoon()) {
      event.actions.put(LodBattleActions.D_ATTACK.get(), sort);
    } else {
      event.actions.put(LodBattleActions.ATTACK.get(), sort);
    }

    sort += 100;

    if(!player.isDragoon() || CONFIG.getConfig(LodConfig.EXTENDED_DRAGOON_ACTIONS.get())) {
      event.actions.put(LodBattleActions.GUARD.get(), sort);
      sort += 100;
      event.actions.put(LodBattleActions.ITEMS.get(), sort);
      sort += 100;

      if(!player.character.getUnlockedAdditions().isEmpty()) {
        event.actions.put(LodBattleActions.ADDITIONS.get(), sort);
        sort += 100;
      }
    }

    if(player.isDragoon()) {
      event.actions.put(LodBattleActions.SPELLS.get(), sort);
      sort += 100;

      if(CONFIG.getConfig(LodConfig.EXTENDED_DRAGOON_ACTIONS.get())) {
        event.actions.put(LodBattleActions.TRANSFORM.get(), sort);
        sort += 100;
      }
    }

    if(!player.isDragoon() || CONFIG.getConfig(LodConfig.EXTENDED_DRAGOON_ACTIONS.get())) {
      event.actions.put(LodBattleActions.ESCAPE.get(), sort);
      sort += 100;
    }

    if(!player.isDragoon() && player.stats.getStat(SP_STAT.get()).getCurrent() >= 100 && player.canBecomeDragoon()) {
      event.actions.put(LodBattleActions.TRANSFORM.get(), sort);
      sort += 100;

      if(canSpecialTransform()) {
        event.actions.put(LodBattleActions.SPECIAL.get(), sort);
      }
    }

    final int legacyFlags = battleState_8006e398.statusConditions_00[player.allBentSlot_274].menuBlockFlag_18 | battleState_8006e398.globalMenuBlocks_510;
    disableRetailBattleActions(legacyFlags, event.disabledActions);

    if((player.status_0e & 0x20) != 0) {
      event.disabledActions.add(LodBattleActions.ATTACK.get());
    }

    if(encounter.escapeChance == 0) {
      event.disabledActions.add(LodBattleActions.ESCAPE.get());
    }

    // This seems to be for the tutorial fights - disables all actions except for one
    if(battleState_8006e398._54c != 0) {
      for(final RegistryId id : REGISTRIES.battleActions) {
        event.disabledActions.add(REGISTRIES.battleActions.getEntry(id).get());
      }

      final ScriptState<MonsterBattleEntity> tutorialEnemy = battleState_8006e398.monsterBents_e50[0];

      if(battleState_8006e398._548 == 0) {
        event.disabledActions.remove(LodBattleActions.ATTACK.get());
        tutorialEnemy.setFlag(BattleEntity27c.FLAG_TAKE_FORCED_TURN);
        battleState_8006e398._54c++;
      } else if(battleState_8006e398._548 == 1) {
        event.disabledActions.remove(LodBattleActions.ITEMS.get());
        tutorialEnemy.setFlag(BattleEntity27c.FLAG_TAKE_FORCED_TURN);
        battleState_8006e398._54c++;
      } else if(battleState_8006e398._54c == 3) {
        event.disabledActions.remove(LodBattleActions.ATTACK.get());
      } else {
        if((battleState_8006e398._54c == 5 || battleState_8006e398._54c == 9) || battleState_8006e398._54c == 16) {
          event.disabledActions.remove(LodBattleActions.TRANSFORM.get());
        } else if(battleState_8006e398._54c == 12) {
          event.disabledActions.remove(LodBattleActions.D_ATTACK.get());
        } else {
          event.disabledActions.remove(LodBattleActions.SPELLS.get());
        }

        tutorialEnemy.setFlag(BattleEntity27c.FLAG_TAKE_FORCED_TURN);
        battleState_8006e398._54c++;
      }
    }
  }

  public static void disableRetailBattleActions(final int packedActions, final Set<BattleAction> disabled) {
    for(int i = 0; i < RETAIL_MENU_BLOCKS.length; i++) {
      if((packedActions & 0x1 << i) != 0) {
        disabled.add(RETAIL_MENU_BLOCKS[i].get());
      }
    }
  }

  private static boolean canSpecialTransform() {
    if(battleState_8006e398.getPlayerCount() < 3) {
      return false;
    }

    for(int i = 0; i < battleState_8006e398.getPlayerCount(); i++) {
      final ScriptState<PlayerBattleEntity> player = battleState_8006e398.playerBents_e40.get(i);

      // not dead or petrified
      if(player.hasFlag(BattleEntity27c.FLAG_DEAD) || (player.innerStruct_00.status_0e & 0x1) != 0) {
        return false;
      }
    }

    for(int i = 0; i < battleState_8006e398.getPlayerCount(); i++) {
      final ScriptState<PlayerBattleEntity> player = battleState_8006e398.playerBents_e40.get(i);
      final int sp = player.innerStruct_00.stats.getStat(SP_STAT.get()).getCurrent();
      final int dlevel = player.innerStruct_00.dlevel_06;

      if(dlevel == 0 || sp < dlevel * 100) {
        return false;
      }

    }

    for(int i = 0; i < battleState_8006e398.getPlayerCount(); i++) {
      if(battleState_8006e398.playerBents_e40.get(i).innerStruct_00.isDragoon()) {
        return false;
      }
    }

    for(int i = 0; i < battleState_8006e398.getPlayerCount(); i++) {
      if(!battleState_8006e398.playerBents_e40.get(i).innerStruct_00.canBecomeDragoon()) {
        return false;
      }
    }

    return true;
  }

  @EventListener
  public static void gatherAttackItems(final GatherAttackItemsEvent event) {
    event.add(new ItemStack(LodItems.SPARK_NET.get()));
    event.add(new ItemStack(LodItems.BURN_OUT.get()));
    event.add(new ItemStack(LodItems.PELLET.get()));
    event.add(new ItemStack(LodItems.SPEAR_FROST.get()));
    event.add(new ItemStack(LodItems.SPINNING_GALE.get()));
    event.add(new ItemStack(LodItems.TRANS_LIGHT.get()));
    event.add(new ItemStack(LodItems.DARK_MIST.get()));
    event.add(new ItemStack(LodItems.PANIC_BELL.get()));
    event.add(new ItemStack(LodItems.STUNNING_HAMMER.get()));
    event.add(new ItemStack(LodItems.POISON_NEEDLE.get()));
    event.add(new ItemStack(LodItems.MIDNIGHT_TERROR.get()));
    event.add(new ItemStack(LodItems.THUNDERBOLT.get()));
    event.add(new ItemStack(LodItems.METEOR_FALL.get()));
    event.add(new ItemStack(LodItems.GUSHING_MAGMA.get()));
    event.add(new ItemStack(LodItems.DANCING_RAY.get()));
    event.add(new ItemStack(LodItems.FATAL_BLIZZARD.get()));
    event.add(new ItemStack(LodItems.BLACK_RAIN.get()));
    event.add(new ItemStack(LodItems.RAVE_TWISTER.get()));
    event.add(new ItemStack(LodItems.BURNING_WAVE.get()));
    event.add(new ItemStack(LodItems.FROZEN_JET.get()));
    event.add(new ItemStack(LodItems.DOWN_BURST.get()));
    event.add(new ItemStack(LodItems.GRAVITY_GRABBER.get()));
    event.add(new ItemStack(LodItems.SPECTRAL_FLASH.get()));
    event.add(new ItemStack(LodItems.NIGHT_RAID.get()));
    event.add(new ItemStack(LodItems.FLASH_HALL.get()));
  }

  @EventListener
  public static void gatherRecoveryItems(final GatherRecoveryItemsEvent event) {
    event.add(new ItemStack(LodItems.SPIRIT_POTION.get()));
    event.add(new ItemStack(LodItems.SUN_RHAPSODY.get()));
    event.add(new ItemStack(LodItems.HEALING_POTION.get()));
    event.add(new ItemStack(LodItems.HEALING_FOG.get()));
    event.add(new ItemStack(LodItems.MOON_SERENADE.get()));
    event.add(new ItemStack(LodItems.HEALING_RAIN.get()));
    event.add(new ItemStack(LodItems.HEALING_BREEZE.get()));
  }

  private static final String[] DRAGOON_SPIRIT_ICONS = {
    FIRE_ELEMENT.getId().entryId(),
    WIND_ELEMENT.getId().entryId(),
    LIGHT_ELEMENT.getId().entryId(),
    DARK_ELEMENT.getId().entryId(),
    THUNDER_ELEMENT.getId().entryId(),
    WATER_ELEMENT.getId().entryId(),
    EARTH_ELEMENT.getId().entryId(),
    DIVINE_ELEMENT.getId().entryId(),
  };

  @EventListener
  public static void registerAtlasIcons(final RegisterAtlasTexturesEvent event) {
    // Dragoon spirit icons
    final List<FileData> files = Loader.loadDirectory("SECT/DRGN0.BIN/4113");
    final Tim tim0 = new Tim(files.get(0));
    final Tim tim5 = new Tim(files.get(5));
    final VramTextureSingle tex = VramTextureLoader.textureFromTim(tim0);
    final VramTextureSingle[] cluts = VramTextureLoader.palettesFromTim(tim5);

    try(final MemoryStack stack = stackPush()) {
      final ByteBuffer buffer = stack.malloc(16 * 48 * 4);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      final IntBuffer intBuffer = buffer.asIntBuffer();

      // Spirits
      for(int spiritIndex = 0; spiritIndex < 8; spiritIndex++) {
        tex.applyPalette(cluts[8 + spiritIndex], new Rect4i(80, 64, 16, 48), intBuffer);

        for(int frameIndex = 0; frameIndex < 3; frameIndex++) {
          final byte[] frame = new byte[16 * 16 * 4];
          buffer.get(frameIndex * frame.length, frame, 0, frame.length);
          event.add(id(DRAGOON_SPIRIT_ICONS[spiritIndex] + '_' + frameIndex), new Image(frame, 16, 16));
        }
      }

      // DD overlay
      for(int i = 0; i < 2; i++) {
        tex.applyPalette(cluts[8], new Rect4i(80 + i * 8, 112, 8, 16), intBuffer);

        final byte[] frame = new byte[8 * 16 * 4];
        buffer.get(0, frame);
        event.add(id(DIVINE_ELEMENT.getId().entryId() + "_overlay_" + i), new Image(frame, 8, 16));
      }
    }
  }

  @EventListener
  public static void createIconMapping(final IconMapEvent event) {
    if(CONFIG.getConfig(CoreMod.ICON_SET.get()) == IconSet.RETAIL) {
      // Remap all the expanded icons to retail icons
      event.addMapping(ItemIcon.AXE, ItemIcon.SWORD);
      event.addMapping(ItemIcon.HAMMER, ItemIcon.SWORD);
      event.addMapping(ItemIcon.SPEAR, ItemIcon.SWORD);
      event.addMapping(ItemIcon.BOW, ItemIcon.SWORD);
      event.addMapping(ItemIcon.MACE, ItemIcon.SWORD);
      event.addMapping(ItemIcon.KNUCKLE, ItemIcon.SWORD);
      event.addMapping(ItemIcon.BOXING_GLOVE, ItemIcon.SWORD);

      event.addMapping(ItemIcon.CLOTHES, ItemIcon.ARMOR);
      event.addMapping(ItemIcon.ROBE, ItemIcon.ARMOR);
      event.addMapping(ItemIcon.BREASTPLATE, ItemIcon.ARMOR);
      event.addMapping(ItemIcon.RED_DRESS, ItemIcon.ARMOR);
      event.addMapping(ItemIcon.LOINCLOTH, ItemIcon.ARMOR);
      event.addMapping(ItemIcon.WARRIOR_DRESS, ItemIcon.ARMOR);

      event.addMapping(ItemIcon.CAPE, ItemIcon.HELM);
      event.addMapping(ItemIcon.CROWN, ItemIcon.HELM);
      event.addMapping(ItemIcon.HAIRBAND, ItemIcon.HELM);
      event.addMapping(ItemIcon.BANDANA, ItemIcon.HELM);
      event.addMapping(ItemIcon.HAT, ItemIcon.HELM);

      event.addMapping(ItemIcon.SHOES, ItemIcon.BOOTS);
      event.addMapping(ItemIcon.KNEEPIECE, ItemIcon.BOOTS);

      event.addMapping(ItemIcon.BRACELET, ItemIcon.RING);
      event.addMapping(ItemIcon.AMULET, ItemIcon.RING);
      event.addMapping(ItemIcon.STONE, ItemIcon.RING);
      event.addMapping(ItemIcon.JEWELLERY, ItemIcon.RING);
      event.addMapping(ItemIcon.PIN, ItemIcon.RING);
      event.addMapping(ItemIcon.BELL, ItemIcon.RING);
      event.addMapping(ItemIcon.BAG, ItemIcon.RING);
      event.addMapping(ItemIcon.CLOAK, ItemIcon.RING);
      event.addMapping(ItemIcon.SCARF, ItemIcon.RING);
      event.addMapping(ItemIcon.GLOVE, ItemIcon.RING);
      event.addMapping(ItemIcon.HORN, ItemIcon.RING);
      event.addMapping(ItemIcon.SHIELD, ItemIcon.RING);
    }
  }
}
