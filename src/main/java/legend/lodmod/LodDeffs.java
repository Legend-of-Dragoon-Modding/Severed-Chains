package legend.lodmod;

import legend.game.combat.deff.DeffPackage;
import legend.game.combat.deff.RegisterDeffsEvent;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.core.GameEngine.REGISTRIES;

public final class LodDeffs {
  private LodDeffs() { }

  private static final Registrar<DeffPackage, RegisterDeffsEvent> REGISTRAR = new Registrar<>(REGISTRIES.deff, LodMod.MOD_ID);

  public static final RegistryDelegate<DeffPackage> DETONATE_ROCK = REGISTRAR.register("detonate_rock", () -> new RetailDeffPackage(4309));
  public static final RegistryDelegate<DeffPackage> SPARK_NET = REGISTRAR.register("spark_net", () -> new RetailDeffPackage(4311));
  public static final RegistryDelegate<DeffPackage> BURN_OUT = REGISTRAR.register("burn_out", () -> new RetailDeffPackage(4313));
  public static final RegistryDelegate<DeffPackage> PELLET = REGISTRAR.register("pellet", () -> new RetailDeffPackage(4317));
  public static final RegistryDelegate<DeffPackage> SPEAR_FROST = REGISTRAR.register("spear_frost", () -> new RetailDeffPackage(4319));
  public static final RegistryDelegate<DeffPackage> SPINNING_GALE = REGISTRAR.register("spinning_gale", () -> new RetailDeffPackage(4321));
  public static final RegistryDelegate<DeffPackage> TRANS_LIGHT = REGISTRAR.register("trans_light", () -> new RetailDeffPackage(4325));
  public static final RegistryDelegate<DeffPackage> DARK_MIST = REGISTRAR.register("dark_mist", () -> new RetailDeffPackage(4327));
  public static final RegistryDelegate<DeffPackage> THUNDERBOLT = REGISTRAR.register("thunderbolt", () -> new RetailDeffPackage(4337));
  public static final RegistryDelegate<DeffPackage> METEOR_FALL = REGISTRAR.register("meteor_fall", () -> new RetailDeffPackage(4339));
  public static final RegistryDelegate<DeffPackage> GUSHING_MAGMA = REGISTRAR.register("gushing_magma", () -> new RetailDeffPackage(4341));
  public static final RegistryDelegate<DeffPackage> DANCING_RAY = REGISTRAR.register("dancing_ray", () -> new RetailDeffPackage(4343));
  public static final RegistryDelegate<DeffPackage> SPIRIT_POTION = REGISTRAR.register("spirit_potion", () -> new RetailDeffPackage(4345));
  public static final RegistryDelegate<DeffPackage> PANIC_BELL = REGISTRAR.register("panic_bell", () -> new RetailDeffPackage(4347));
  public static final RegistryDelegate<DeffPackage> FATAL_BLIZZARD = REGISTRAR.register("fatal_blizzard", () -> new RetailDeffPackage(4351));
  public static final RegistryDelegate<DeffPackage> STUNNING_HAMMER = REGISTRAR.register("stunning_hammer", () -> new RetailDeffPackage(4353));
  public static final RegistryDelegate<DeffPackage> BLACK_RAIN = REGISTRAR.register("black_rain", () -> new RetailDeffPackage(4355));
  public static final RegistryDelegate<DeffPackage> POISON_NEEDLE = REGISTRAR.register("poison_needle", () -> new RetailDeffPackage(4357));
  public static final RegistryDelegate<DeffPackage> MIDNIGHT_TERROR = REGISTRAR.register("midnight_terror", () -> new RetailDeffPackage(4359));
  public static final RegistryDelegate<DeffPackage> RAVE_TWISTER = REGISTRAR.register("rave_twister", () -> new RetailDeffPackage(4363));
  public static final RegistryDelegate<DeffPackage> ANGELS_PRAYER = REGISTRAR.register("angels_prayer", () -> new RetailDeffPackage(4367));
  public static final RegistryDelegate<DeffPackage> CHARM_POTION = REGISTRAR.register("charm_potion", () -> new RetailDeffPackage(4369));
  public static final RegistryDelegate<DeffPackage> PANDEMONIUM = REGISTRAR.register("pandemonium", () -> new RetailDeffPackage(4371));
  public static final RegistryDelegate<DeffPackage> SUN_RHAPSODY = REGISTRAR.register("sun_rhapsody", () -> new RetailDeffPackage(4381));
  public static final RegistryDelegate<DeffPackage> HEALING_POTION = REGISTRAR.register("healing_potion", () -> new RetailDeffPackage(4385));
  public static final RegistryDelegate<DeffPackage> MAGIC_SIG_STONE = REGISTRAR.register("magic_sig_stone", () -> new RetailDeffPackage(4387));
  public static final RegistryDelegate<DeffPackage> HEALING_RAIN = REGISTRAR.register("healing_rain", () -> new RetailDeffPackage(4389));
  public static final RegistryDelegate<DeffPackage> MOON_SERENADE = REGISTRAR.register("moon_serenade", () -> new RetailDeffPackage(4391));
  public static final RegistryDelegate<DeffPackage> POWER_UP = REGISTRAR.register("power_up", () -> new RetailDeffPackage(4393));
  public static final RegistryDelegate<DeffPackage> POWER_DOWN = REGISTRAR.register("power_down", () -> new RetailDeffPackage(4395));
  public static final RegistryDelegate<DeffPackage> SPEED_UP = REGISTRAR.register("speed_up", () -> new RetailDeffPackage(4397));
  public static final RegistryDelegate<DeffPackage> SPEED_DOWN = REGISTRAR.register("speed_down", () -> new RetailDeffPackage(4399));
  public static final RegistryDelegate<DeffPackage> ENEMY_HEALING_POTION = REGISTRAR.register("enemy_healing_potion", () -> new RetailDeffPackage(4401));
  public static final RegistryDelegate<DeffPackage> SACHET = REGISTRAR.register("sachet", () -> new RetailDeffPackage(4403));
  public static final RegistryDelegate<DeffPackage> PSYCHE_BOMB = REGISTRAR.register("psyche_bomb", () -> new RetailDeffPackage(4405));
  public static final RegistryDelegate<DeffPackage> BURNING_WAVE = REGISTRAR.register("burning_wave", () -> new RetailDeffPackage(4407));
  public static final RegistryDelegate<DeffPackage> FROZEN_JET = REGISTRAR.register("frozen_jet", () -> new RetailDeffPackage(4409));
  public static final RegistryDelegate<DeffPackage> DOWN_BURST = REGISTRAR.register("down_burst", () -> new RetailDeffPackage(4411));
  public static final RegistryDelegate<DeffPackage> GRAVITY_GRABBER = REGISTRAR.register("gravity_grabber", () -> new RetailDeffPackage(4413));
  public static final RegistryDelegate<DeffPackage> SPECTRAL_FLASH = REGISTRAR.register("spectral_flash", () -> new RetailDeffPackage(4415));
  public static final RegistryDelegate<DeffPackage> NIGHT_RAID = REGISTRAR.register("night_raid", () -> new RetailDeffPackage(4417));
  public static final RegistryDelegate<DeffPackage> FLASH_HALL = REGISTRAR.register("flash_hall", () -> new RetailDeffPackage(4419));
  public static final RegistryDelegate<DeffPackage> HEALING_BREEZE = REGISTRAR.register("healing_breeze", () -> new RetailDeffPackage(4421));
  public static final RegistryDelegate<DeffPackage> PSYCHE_BOMB_X = REGISTRAR.register("psyche_bomb_x", () -> new RetailDeffPackage(4423));

  static void register(final RegisterDeffsEvent event) {
    REGISTRAR.registryEvent(event);
  }
}
