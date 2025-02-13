package legend.lodmod;

import legend.core.GameEngine;
import legend.game.inventory.Item;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemRegistryEvent;
import legend.lodmod.items.AngelsPrayerItem;
import legend.lodmod.items.AttackBallItem;
import legend.lodmod.items.AttackItem;
import legend.lodmod.items.BodyPurifierItem;
import legend.lodmod.items.BuffItem;
import legend.lodmod.items.CauseStatusItem;
import legend.lodmod.items.CharmPotionItem;
import legend.lodmod.items.DepetrifierItem;
import legend.lodmod.items.HealingBreezeItem;
import legend.lodmod.items.HealingFogItem;
import legend.lodmod.items.HealingPotionItem;
import legend.lodmod.items.HealingRainItem;
import legend.lodmod.items.MindPurifierItem;
import legend.lodmod.items.MoonSerenadeItem;
import legend.lodmod.items.PandemoniumItem;
import legend.lodmod.items.PsycheBombXItem;
import legend.lodmod.items.RecoveryBallItem;
import legend.lodmod.items.SachetItem;
import legend.lodmod.items.ShieldItem;
import legend.lodmod.items.SignetStoneItem;
import legend.lodmod.items.SmokeBallItem;
import legend.lodmod.items.SpiritPotionItem;
import legend.lodmod.items.SunRhapsodyItem;
import legend.lodmod.items.TotalVanishingItem;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class LodItems {
  private LodItems() { }

  private static final Registrar<Item, ItemRegistryEvent> ITEM_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.items, LodMod.MOD_ID);

  // Recovery items
  public static final RegistryDelegate<Item> ANGELS_PRAYER = ITEM_REGISTRAR.register("angels_prayer", AngelsPrayerItem::new);
  public static final RegistryDelegate<Item> HEALING_POTION = ITEM_REGISTRAR.register("healing_potion", HealingPotionItem::new);
  public static final RegistryDelegate<Item> HEALING_FOG = ITEM_REGISTRAR.register("healing_fog", HealingFogItem::new);
  public static final RegistryDelegate<Item> HEALING_BREEZE = ITEM_REGISTRAR.register("healing_breeze", HealingBreezeItem::new);
  public static final RegistryDelegate<Item> HEALING_RAIN = ITEM_REGISTRAR.register("healing_rain", HealingRainItem::new);
  public static final RegistryDelegate<Item> MOON_SERENADE = ITEM_REGISTRAR.register("moon_serenade", MoonSerenadeItem::new);
  public static final RegistryDelegate<Item> SUN_RHAPSODY = ITEM_REGISTRAR.register("sun_rhapsody", SunRhapsodyItem::new);
  public static final RegistryDelegate<Item> SPIRIT_POTION = ITEM_REGISTRAR.register("spirit_potion", SpiritPotionItem::new);
  public static final RegistryDelegate<Item> BODY_PURIFIER = ITEM_REGISTRAR.register("body_purifier", BodyPurifierItem::new);
  public static final RegistryDelegate<Item> DEPETRIFIER = ITEM_REGISTRAR.register("depetrifier", DepetrifierItem::new);
  public static final RegistryDelegate<Item> MIND_PURIFIER = ITEM_REGISTRAR.register("mind_purifier", MindPurifierItem::new);

  // Attack items
  public static final RegistryDelegate<Item> BLACK_RAIN = ITEM_REGISTRAR.register("black_rain", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.DARK_ELEMENT.get(), 0));
  public static final RegistryDelegate<Item> BURN_OUT = ITEM_REGISTRAR.register("burn_out", () -> new AttackItem(ItemIcon.MAGIC, 5, false, LodMod.FIRE_ELEMENT.get(), 0x40));
  public static final RegistryDelegate<Item> BURNING_WAVE = ITEM_REGISTRAR.register("burning_wave", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.FIRE_ELEMENT.get(), 0x10));
  public static final RegistryDelegate<Item> DANCING_RAY = ITEM_REGISTRAR.register("dancing_ray", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.LIGHT_ELEMENT.get(), 0));
  public static final RegistryDelegate<Item> DARK_MIST = ITEM_REGISTRAR.register("dark_mist", () -> new AttackItem(ItemIcon.MAGIC, 5, false, LodMod.DARK_ELEMENT.get(), 0x40));
  public static final RegistryDelegate<Item> DETONATE_ROCK = ITEM_REGISTRAR.register("detonate_rock", () -> new AttackItem(ItemIcon.MAGIC, 5, true, LodMod.NO_ELEMENT.get(), 0));
  public static final RegistryDelegate<Item> DOWN_BURST = ITEM_REGISTRAR.register("down_burst", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.WIND_ELEMENT.get(), 0x10));
  public static final RegistryDelegate<Item> FATAL_BLIZZARD = ITEM_REGISTRAR.register("fatal_blizzard", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.WATER_ELEMENT.get(), 0));
  public static final RegistryDelegate<Item> FLASH_HALL = ITEM_REGISTRAR.register("flash_hall", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.LIGHT_ELEMENT.get(), 0x10));
  public static final RegistryDelegate<Item> FROZEN_JET = ITEM_REGISTRAR.register("frozen_jet", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.WATER_ELEMENT.get(), 0x10));
  public static final RegistryDelegate<Item> GRAVITY_GRABBER = ITEM_REGISTRAR.register("gravity_grabber", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.EARTH_ELEMENT.get(), 0x10));
  public static final RegistryDelegate<Item> GUSHING_MAGMA = ITEM_REGISTRAR.register("gushing_magma", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.FIRE_ELEMENT.get(), 0));
  public static final RegistryDelegate<Item> METEOR_FALL = ITEM_REGISTRAR.register("meteor_fall", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.EARTH_ELEMENT.get(), 0));
  public static final RegistryDelegate<Item> NIGHT_RAID = ITEM_REGISTRAR.register("night_raid", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.DARK_ELEMENT.get(), 0x10));
  public static final RegistryDelegate<Item> PELLET = ITEM_REGISTRAR.register("pellet", () -> new AttackItem(ItemIcon.MAGIC, 5, false, LodMod.EARTH_ELEMENT.get(), 0x40));
  public static final RegistryDelegate<Item> PSYCHE_BOMB = ITEM_REGISTRAR.register("psyche_bomb", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.NO_ELEMENT.get(), 0x8));
  public static final RegistryDelegate<Item> PSYCHE_BOMB_X = ITEM_REGISTRAR.register("psyche_bomb_x", PsycheBombXItem::new);
  public static final RegistryDelegate<Item> RAVE_TWISTER = ITEM_REGISTRAR.register("rave_twister", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.WIND_ELEMENT.get(), 0));
  public static final RegistryDelegate<Item> SPARK_NET = ITEM_REGISTRAR.register("spark_net", () -> new AttackItem(ItemIcon.MAGIC, 5, false, LodMod.THUNDER_ELEMENT.get(), 0x40));
  public static final RegistryDelegate<Item> SPEAR_FROST = ITEM_REGISTRAR.register("spear_frost", () -> new AttackItem(ItemIcon.MAGIC, 5, false, LodMod.WATER_ELEMENT.get(), 0x40));
  public static final RegistryDelegate<Item> SPECTRAL_FLASH = ITEM_REGISTRAR.register("spectral_flash", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.LIGHT_ELEMENT.get(), 0x10));
  public static final RegistryDelegate<Item> SPINNING_GALE = ITEM_REGISTRAR.register("spinning_gale", () -> new AttackItem(ItemIcon.MAGIC, 5, false, LodMod.WIND_ELEMENT.get(), 0x40));
  public static final RegistryDelegate<Item> THUNDERBOLT = ITEM_REGISTRAR.register("thunderbolt", () -> new AttackItem(ItemIcon.MAGIC, 10, true, LodMod.THUNDER_ELEMENT.get(), 0));
  public static final RegistryDelegate<Item> TRANS_LIGHT = ITEM_REGISTRAR.register("trans_light", () -> new AttackItem(ItemIcon.MAGIC, 5, false, LodMod.LIGHT_ELEMENT.get(), 0x40));

  // Random items
  public static final RegistryDelegate<Item> RECOVERY_BALL = ITEM_REGISTRAR.register("recovery_ball", RecoveryBallItem::new);
  public static final RegistryDelegate<Item> ATTACK_BALL = ITEM_REGISTRAR.register("attack_ball", AttackBallItem::new);

  // Buffs/debuffs
  public static final RegistryDelegate<Item> POWER_DOWN = ITEM_REGISTRAR.register("power_down", () -> new BuffItem(1, ItemIcon.DOWN, 200, Item.TargetType.ENEMIES, -50, -50, -50, -50, 0, 0, 0, 0, false, false, 0, 0, 0, 0, 0, 0));
  public static final RegistryDelegate<Item> POWER_UP = ITEM_REGISTRAR.register("power_up", () -> new BuffItem(6, ItemIcon.UP, 200, Item.TargetType.ALLIES, 50, 50, 50, 50, 0, 0, 0, 0, false, false, 0, 0, 0, 0, 0, 0));
  public static final RegistryDelegate<Item> SPEED_DOWN = ITEM_REGISTRAR.register("speed_down", () -> new BuffItem(1, ItemIcon.DOWN, 200, Item.TargetType.ENEMIES, 0, 0, 0, 0, 0, 0, 0, 0, false, false, 0, -50, 0, 0, 0, 0));
  public static final RegistryDelegate<Item> SPEED_UP = ITEM_REGISTRAR.register("speed_up", () -> new BuffItem(6, ItemIcon.UP, 200, Item.TargetType.ALLIES, 0, 0, 0, 0, 0, 0, 0, 0, false, false, 100, 0, 0, 0, 0, 0));
  public static final RegistryDelegate<Item> MAGIC_SHIELD = ITEM_REGISTRAR.register("magic_shield", () -> new ShieldItem(7, false, true));
  public static final RegistryDelegate<Item> MATERIAL_SHIELD = ITEM_REGISTRAR.register("material_shield", () -> new ShieldItem(8, true, false));

  // Status items
  public static final RegistryDelegate<Item> CHARM_POTION = ITEM_REGISTRAR.register("charm_potion", CharmPotionItem::new);
  public static final RegistryDelegate<Item> MAGIC_SIG_STONE = ITEM_REGISTRAR.register("magic_sig_stone", SignetStoneItem::new);
  public static final RegistryDelegate<Item> MIDNIGHT_TERROR = ITEM_REGISTRAR.register("midnight_terror", () -> new CauseStatusItem(-12, ItemIcon.SKULL, 10, 0x8));
  public static final RegistryDelegate<Item> PANDEMONIUM = ITEM_REGISTRAR.register("pandemonium", PandemoniumItem::new);
  public static final RegistryDelegate<Item> PANIC_BELL = ITEM_REGISTRAR.register("panic_bell", () -> new CauseStatusItem(-11, ItemIcon.SKULL, 10, 0x4));
  public static final RegistryDelegate<Item> POISON_NEEDLE = ITEM_REGISTRAR.register("poison_needle", () -> new CauseStatusItem(-16, ItemIcon.SKULL, 10, 0x80));
  public static final RegistryDelegate<Item> SACHET = ITEM_REGISTRAR.register("sachet", SachetItem::new);
  public static final RegistryDelegate<Item> SMOKE_BALL = ITEM_REGISTRAR.register("smoke_ball", SmokeBallItem::new);
  public static final RegistryDelegate<Item> STUNNING_HAMMER = ITEM_REGISTRAR.register("stunning_hammer", () -> new CauseStatusItem(-13, ItemIcon.SKULL, 10, 0x10));
  public static final RegistryDelegate<Item> TOTAL_VANISHING = ITEM_REGISTRAR.register("total_vanishing", TotalVanishingItem::new);

  // Misc
  public static final RegistryDelegate<Item> ENEMY_HEALING_POTION = ITEM_REGISTRAR.register("enemy_healing_potion", () -> new HealingPotionItem(ItemIcon.BLUE_POTION, 5, false, 30));

  static void register(final ItemRegistryEvent event) {
    ITEM_REGISTRAR.registryEvent(event);
  }
}
