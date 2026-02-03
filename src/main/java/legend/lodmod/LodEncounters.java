package legend.lodmod;

import legend.core.GameEngine;
import legend.game.combat.encounters.ArenaEncounter;
import legend.game.combat.encounters.Encounter;
import legend.game.combat.encounters.EncounterRegistryEvent;
import legend.game.combat.encounters.MelbuEncounter;
import legend.game.combat.encounters.PhasedEncounter;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class LodEncounters {
  private LodEncounters() { }

  private static final Registrar<Encounter, EncounterRegistryEvent> ENCOUNTER_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.encounters, LodMod.MOD_ID);

  public static final RegistryDelegate<Encounter> ENCOUNTER_BERSERK_MOUSE = ENCOUNTER_REGISTRAR.register("berserk_mouse", () -> new Encounter(224, 90, 1, 20, 8, 6, 21, 42, 65535, 255, new Encounter.Monster(24, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ASSASSIN_COCK = ENCOUNTER_REGISTRAR.register("assassin_cock", () -> new Encounter(224, 90, 2, 21, 8, 1, 21, 42, 65535, 255, new Encounter.Monster(21, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TRENT = ENCOUNTER_REGISTRAR.register("trent", () -> new Encounter(224, 90, 3, 22, 8, 5, 22, 42, 65535, 255, new Encounter.Monster(38, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GOBLIN = ENCOUNTER_REGISTRAR.register("goblin", () -> new Encounter(224, 90, 14, 15, 21, 1, 43, 8, 65535, 255, new Encounter.Monster(8, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GOBLIN_ASSASSIN_COCK = ENCOUNTER_REGISTRAR.register("goblin_assassin_cock", () -> new Encounter(224, 90, 14, 16, 22, 0, 47, 8, 65535, 255, new Encounter.Monster(8, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(21, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TRENT_ASSASSIN_COCK = ENCOUNTER_REGISTRAR.register("trent_assassin_cock", () -> new Encounter(224, 90, 15, 16, 22, 5, 39, 8, 65535, 255, new Encounter.Monster(38, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(21, new Vector3f(-3840.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BERSERK_MOUSE_ASSASSIN_COCK = ENCOUNTER_REGISTRAR.register("berserk_mouse_assassin_cock", () -> new Encounter(224, 90, 15, 13, 22, 0, 42, 8, 65535, 255, new Encounter.Monster(24, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(21, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ASSASSIN_COCK_ASSASSIN_COCK = ENCOUNTER_REGISTRAR.register("assassin_cock_assassin_cock", () -> new Encounter(224, 90, 16, 14, 44, 22, 0, 30, 65535, 255, new Encounter.Monster(21, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(21, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BERSERK_MOUSE_BERSERK_MOUSE = ENCOUNTER_REGISTRAR.register("berserk_mouse_berserk_mouse", () -> new Encounter(224, 90, 14, 15, 13, 0, 46, 10, 65535, 255, new Encounter.Monster(24, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(24, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GOBLIN_TRENT = ENCOUNTER_REGISTRAR.register("goblin_trent", () -> new Encounter(224, 90, 13, 16, 8, 0, 11, 12, 65535, 255, new Encounter.Monster(8, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(38, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_VAMPIRE_KIWI = ENCOUNTER_REGISTRAR.register("vampire_kiwi", () -> new Encounter(225, 80, 1, 2, 16, 1, 21, 19, 65535, 255, new Encounter.Monster(27, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MOLE = ENCOUNTER_REGISTRAR.register("mole", () -> new Encounter(225, 80, 1, 3, 1, 9, 42, 21, 65535, 255, new Encounter.Monster(45, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CRESCENT_BEE = ENCOUNTER_REGISTRAR.register("crescent_bee", () -> new Encounter(225, 80, 1, 4, 6, 14, 43, 16, 65535, 255, new Encounter.Monster(59, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MANTIS = ENCOUNTER_REGISTRAR.register("mantis", () -> new Encounter(225, 80, 2, 3, 6, 21, 24, 19, 65535, 255, new Encounter.Monster(0, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_VAMPIRE_KIWI_VAMPIRE_KIWI = ENCOUNTER_REGISTRAR.register("vampire_kiwi_vampire_kiwi", () -> new Encounter(225, 80, 2, 4, 3, 19, 22, 47, 65535, 255, new Encounter.Monster(27, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(27, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_VAMPIRE_KIWI_CRESCENT_BEE = ENCOUNTER_REGISTRAR.register("vampire_kiwi_crescent_bee", () -> new Encounter(225, 80, 3, 4, 1, 22, 19, 25, 65535, 255, new Encounter.Monster(27, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(59, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MANTIS_CRESCENT_BEE = ENCOUNTER_REGISTRAR.register("mantis_crescent_bee", () -> new Encounter(225, 80, 3, 1, 19, 42, 16, 44, 65535, 255, new Encounter.Monster(0, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(59, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MOLE_MOLE = ENCOUNTER_REGISTRAR.register("mole_mole", () -> new Encounter(225, 80, 4, 2, 3, 11, 18, 25, 65535, 255, new Encounter.Monster(45, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(45, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_VAMPIRE_KIWI_MOLE_VAMPIRE_KIWI = ENCOUNTER_REGISTRAR.register("vampire_kiwi_mole_vampire_kiwi", () -> new Encounter(225, 80, 2, 3, 1, 19, 42, 29, 65535, 255, new Encounter.Monster(27, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(45, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(27, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MANTIS_VAMPIRE_KIWI = ENCOUNTER_REGISTRAR.register("mantis_vampire_kiwi", () -> new Encounter(225, 80, 1, 4, 7, 22, 17, 44, 65535, 255, new Encounter.Monster(0, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(27, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCREAMING_BAT_SCREAMING_BAT = ENCOUNTER_REGISTRAR.register("screaming_bat_screaming_bat", () -> new Encounter(225, 70, 2, 19, 8, 4, 22, 33, 65535, 255, new Encounter.Monster(46, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(46, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SLIME = ENCOUNTER_REGISTRAR.register("slime", () -> new Encounter(225, 70, 3, 20, 21, 1, 14, 39, 65535, 255, new Encounter.Monster(104, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UGLY_BALLOON = ENCOUNTER_REGISTRAR.register("ugly_balloon", () -> new Encounter(225, 70, 4, 21, 14, 3, 25, 34, 65535, 255, new Encounter.Monster(64, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ORC = ENCOUNTER_REGISTRAR.register("orc", () -> new Encounter(225, 70, 14, 15, 15, 6, 17, 43, 65535, 255, new Encounter.Monster(94, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_EVIL_SPIDER = ENCOUNTER_REGISTRAR.register("evil_spider", () -> new Encounter(225, 70, 14, 16, 6, 9, 21, 46, 65535, 255, new Encounter.Monster(13, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCREAMING_BAT_UGLY_BALLOON = ENCOUNTER_REGISTRAR.register("screaming_bat_ugly_balloon", () -> new Encounter(225, 70, 15, 16, 7, 11, 31, 27, 65535, 255, new Encounter.Monster(46, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(64, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCREAMING_BAT_ORC_SCREAMING_BAT = ENCOUNTER_REGISTRAR.register("screaming_bat_orc_screaming_bat", () -> new Encounter(225, 70, 15, 13, 8, 23, 33, 29, 65535, 255, new Encounter.Monster(46, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(94, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(46, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SLIME_UGLY_BALLOON = ENCOUNTER_REGISTRAR.register("slime_ugly_balloon", () -> new Encounter(225, 70, 16, 14, 10, 17, 26, 35, 65535, 255, new Encounter.Monster(104, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(64, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SLIME_SLIME = ENCOUNTER_REGISTRAR.register("slime_slime", () -> new Encounter(225, 70, 14, 15, 0, 8, 31, 44, 65535, 255, new Encounter.Monster(104, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(148, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ORC_UGLY_BALLOON_ORC = ENCOUNTER_REGISTRAR.register("orc_ugly_balloon_orc", () -> new Encounter(225, 70, 13, 16, 4, 18, 23, 40, 65535, 255, new Encounter.Monster(94, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(64, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(94, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SEA_DRAGON = ENCOUNTER_REGISTRAR.register("sea_dragon", () -> new Encounter(224, 60, 19, 2, 1, 8, 16, 43, 65535, 255, new Encounter.Monster(4, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CROCODILE = ENCOUNTER_REGISTRAR.register("crocodile", () -> new Encounter(224, 60, 19, 3, 6, 15, 25, 42, 65535, 255, new Encounter.Monster(68, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MYCONIDO = ENCOUNTER_REGISTRAR.register("myconido", () -> new Encounter(224, 60, 19, 4, 1, 16, 24, 42, 65535, 255, new Encounter.Monster(28, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MERMAN = ENCOUNTER_REGISTRAR.register("merman", () -> new Encounter(224, 60, 20, 3, 0, 9, 15, 27, 65535, 255, new Encounter.Monster(22, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MYCONIDO_MYCONIDO = ENCOUNTER_REGISTRAR.register("myconido_myconido", () -> new Encounter(224, 60, 20, 4, 5, 17, 25, 46, 65535, 255, new Encounter.Monster(28, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(28, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SEA_DRAGON_MYCONIDO = ENCOUNTER_REGISTRAR.register("sea_dragon_myconido", () -> new Encounter(224, 60, 21, 4, 5, 10, 26, 45, 65535, 255, new Encounter.Monster(4, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(28, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MERMAN_MERMAN = ENCOUNTER_REGISTRAR.register("merman_merman", () -> new Encounter(224, 60, 21, 1, 5, 8, 18, 25, 65535, 255, new Encounter.Monster(22, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(22, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MYCONIDO_MERMAN_MYCONIDO = ENCOUNTER_REGISTRAR.register("myconido_merman_myconido", () -> new Encounter(224, 60, 22, 2, 5, 8, 17, 33, 65535, 255, new Encounter.Monster(28, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(22, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(28, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CROCODILE_CROCODILE = ENCOUNTER_REGISTRAR.register("crocodile_crocodile", () -> new Encounter(224, 60, 20, 3, 0, 11, 23, 28, 65535, 255, new Encounter.Monster(68, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(68, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SEA_DRAGON_MERMAN_SEA_DRAGON = ENCOUNTER_REGISTRAR.register("sea_dragon_merman_sea_dragon", () -> new Encounter(224, 60, 19, 4, 0, 8, 17, 29, 65535, 255, new Encounter.Monster(4, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(22, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(4, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAGMA_FISH = ENCOUNTER_REGISTRAR.register("magma_fish", () -> new Encounter(225, 50, 25, 26, 15, 63, 34, 43, 65535, 255, new Encounter.Monster(74, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_RED_HOT = ENCOUNTER_REGISTRAR.register("red_hot", () -> new Encounter(225, 50, 25, 27, 10, 6, 22, 33, 65535, 255, new Encounter.Monster(100, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SALAMANDER = ENCOUNTER_REGISTRAR.register("salamander", () -> new Encounter(225, 50, 25, 28, 22, 1, 12, 42, 65535, 255, new Encounter.Monster(92, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FIRE_SPIRIT = ENCOUNTER_REGISTRAR.register("fire_spirit", () -> new Encounter(225, 50, 26, 27, 18, 1, 13, 28, 65535, 255, new Encounter.Monster(33, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAGMA_FISH_MAGMA_FISH = ENCOUNTER_REGISTRAR.register("magma_fish_magma_fish", () -> new Encounter(225, 50, 26, 28, 2, 8, 25, 34, 65535, 255, new Encounter.Monster(74, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(157, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_RED_HOT_SALAMANDER = ENCOUNTER_REGISTRAR.register("red_hot_salamander", () -> new Encounter(225, 50, 27, 28, 8, 66, 17, 39, 65535, 255, new Encounter.Monster(100, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(92, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SALAMANDER_SALAMANDER = ENCOUNTER_REGISTRAR.register("salamander_salamander", () -> new Encounter(225, 50, 25, 28, 11, 5, 19, 33, 65535, 255, new Encounter.Monster(92, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(92, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FIRE_SPIRIT_FIRE_SPIRIT = ENCOUNTER_REGISTRAR.register("fire_spirit_fire_spirit", () -> new Encounter(225, 50, 26, 27, 1, 19, 34, 28, 65535, 255, new Encounter.Monster(33, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(150, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FIRE_SPIRIT_SALAMANDER = ENCOUNTER_REGISTRAR.register("fire_spirit_salamander", () -> new Encounter(225, 50, 27, 26, 11, 5, 18, 25, 65535, 255, new Encounter.Monster(33, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(92, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_RED_HOT_RED_HOT = ENCOUNTER_REGISTRAR.register("red_hot_red_hot", () -> new Encounter(225, 50, 28, 25, 0, 18, 23, 44, 65535, 255, new Encounter.Monster(100, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(146, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MANDRAKE = ENCOUNTER_REGISTRAR.register("mandrake", () -> new Encounter(225, 50, 1, 22, 78, 12, 42, 23, 65535, 255, new Encounter.Monster(90, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_RUN_FAST = ENCOUNTER_REGISTRAR.register("run_fast", () -> new Encounter(225, 50, 1, 19, 21, 1, 14, 34, 65535, 255, new Encounter.Monster(35, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LIZARD_MAN = ENCOUNTER_REGISTRAR.register("lizard_man", () -> new Encounter(225, 50, 1, 21, 14, 1, 21, 43, 65535, 255, new Encounter.Monster(19, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAN_EATING_BUD = ENCOUNTER_REGISTRAR.register("man_eating_bud", () -> new Encounter(225, 50, 2, 21, 10, 63, 15, 42, 65535, 255, new Encounter.Monster(88, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TRICKY_BAT_TRICKY_BAT = ENCOUNTER_REGISTRAR.register("tricky_bat_tricky_bat", () -> new Encounter(225, 50, 2, 22, 8, 0, 33, 46, 65535, 255, new Encounter.Monster(115, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(115, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MANDRAKE_RUN_FAST = ENCOUNTER_REGISTRAR.register("mandrake_run_fast", () -> new Encounter(225, 50, 3, 22, 4, 78, 8, 28, 65535, 255, new Encounter.Monster(90, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(35, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MANDRAKE_LIZARD_MAN = ENCOUNTER_REGISTRAR.register("mandrake_lizard_man", () -> new Encounter(225, 50, 3, 19, 3, 8, 28, 40, 65535, 255, new Encounter.Monster(90, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(19, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TRICKY_BAT_MANDRAKE_TRICKY_BAT = ENCOUNTER_REGISTRAR.register("tricky_bat_mandrake_tricky_bat", () -> new Encounter(225, 50, 4, 20, 2, 19, 44, 29, 65535, 255, new Encounter.Monster(115, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(90, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(115, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_RUN_FAST_LIZARD_MAN_RUN_FAST = ENCOUNTER_REGISTRAR.register("run_fast_lizard_man_run_fast", () -> new Encounter(225, 50, 2, 21, 5, 19, 23, 56, 65535, 255, new Encounter.Monster(35, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(19, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(35, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LIZARD_MAN_LIZARD_MAN = ENCOUNTER_REGISTRAR.register("lizard_man_lizard_man", () -> new Encounter(225, 50, 1, 22, 60, 12, 23, 52, 65535, 255, new Encounter.Monster(19, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(19, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CRYSTAL_GOLEM = ENCOUNTER_REGISTRAR.register("crystal_golem", () -> new Encounter(225, 40, 1, 2, 84, 1, 19, 36, 65535, 255, new Encounter.Monster(66, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PLAGUE_RAT_PLAGUE_RAT = ENCOUNTER_REGISTRAR.register("plague_rat_plague_rat", () -> new Encounter(225, 40, 1, 3, 36, 3, 19, 40, 65535, 255, new Encounter.Monster(107, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(107, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_STRONG_MAN = ENCOUNTER_REGISTRAR.register("strong_man", () -> new Encounter(225, 40, 1, 4, 22, 6, 16, 26, 65535, 255, new Encounter.Monster(15, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GARGOYLE = ENCOUNTER_REGISTRAR.register("gargoyle", () -> new Encounter(225, 40, 2, 3, 6, 21, 19, 62, 65535, 255, new Encounter.Monster(58, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LIVING_STATUE = ENCOUNTER_REGISTRAR.register("living_statue", () -> new Encounter(225, 40, 2, 4, 5, 62, 22, 40, 65535, 255, new Encounter.Monster(55, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_STRONG_MAN_GARGOYLE = ENCOUNTER_REGISTRAR.register("strong_man_gargoyle", () -> new Encounter(225, 40, 3, 4, 19, 66, 22, 66, 65535, 255, new Encounter.Monster(15, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(58, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GARGOYLE_GARGOYLE = ENCOUNTER_REGISTRAR.register("gargoyle_gargoyle", () -> new Encounter(225, 40, 3, 1, 7, 22, 19, 69, 65535, 255, new Encounter.Monster(58, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(58, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PLAGUE_RAT_STRONG_MAN_PLAGUE_RAT = ENCOUNTER_REGISTRAR.register("plague_rat_strong_man_plague_rat", () -> new Encounter(225, 40, 4, 2, 5, 19, 40, 29, 65535, 255, new Encounter.Monster(107, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(15, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(107, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GARGOYLE_LIVING_STATUE_GARGOYLE = ENCOUNTER_REGISTRAR.register("gargoyle_living_statue_gargoyle", () -> new Encounter(225, 40, 2, 3, 40, 19, 60, 69, 65535, 255, new Encounter.Monster(58, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(55, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(58, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LIVING_STATUE_STRONG_MAN_LIVING_STATUE = ENCOUNTER_REGISTRAR.register("living_statue_strong_man_living_statue", () -> new Encounter(225, 40, 1, 4, 5, 22, 19, 69, 65535, 255, new Encounter.Monster(55, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(15, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(55, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CURSED_JAR = ENCOUNTER_REGISTRAR.register("cursed_jar", () -> new Encounter(224, 100, 0, 0, 1, 21, 14, 34, 65535, 255, new Encounter.Monster(87, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TREASURE_JAR = ENCOUNTER_REGISTRAR.register("treasure_jar", () -> new Encounter(224, 100, 0, 0, 1, 5, 16, 19, 65535, 255, new Encounter.Monster(124, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LUCKY_JAR = ENCOUNTER_REGISTRAR.register("lucky_jar", () -> new Encounter(224, 100, 0, 0, 14, 1, 23, 47, 65535, 255, new Encounter.Monster(139, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_OOPARTS = ENCOUNTER_REGISTRAR.register("ooparts", () -> new Encounter(224, 100, 0, 0, 6, 16, 19, 62, 65535, 255, new Encounter.Monster(42, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS = ENCOUNTER_REGISTRAR.register("scissorhands", () -> new Encounter(224, 100, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_RAINBOW_BIRD = ENCOUNTER_REGISTRAR.register("rainbow_bird", () -> new Encounter(224, 100, 12, 24, 44, 22, 27, 45, 65535, 255, new Encounter.Monster(54, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BLUE_BIRD = ENCOUNTER_REGISTRAR.register("blue_bird", () -> new Encounter(224, 100, 0, 0, 8, 6, 15, 33, 65535, 255, new Encounter.Monster(136, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_RED_BIRD = ENCOUNTER_REGISTRAR.register("red_bird", () -> new Encounter(224, 100, 0, 0, 14, 1, 15, 34, 65535, 255, new Encounter.Monster(137, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_YELLOW_BIRD = ENCOUNTER_REGISTRAR.register("yellow_bird", () -> new Encounter(224, 100, 0, 0, 52, 1, 21, 34, 65535, 255, new Encounter.Monster(138, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_1 = ENCOUNTER_REGISTRAR.register("scissorhands_1", () -> new Encounter(224, 100, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FRILLED_LIZARD = ENCOUNTER_REGISTRAR.register("frilled_lizard", () -> new Encounter(225, 40, 1, 8, 14, 4, 25, 43, 65535, 255, new Encounter.Monster(69, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_2 = ENCOUNTER_REGISTRAR.register("scissorhands_2", () -> new Encounter(225, 40, 1, 9, 14, 1, 12, 42, 65535, 255, new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ARROW_SHOOTER = ENCOUNTER_REGISTRAR.register("arrow_shooter", () -> new Encounter(225, 40, 1, 10, 6, 21, 42, 14, 65535, 255, new Encounter.Monster(99, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_STINGER = ENCOUNTER_REGISTRAR.register("stinger", () -> new Encounter(225, 40, 2, 9, 9, 6, 18, 43, 65535, 255, new Encounter.Monster(108, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_EARTH_SHAKER = ENCOUNTER_REGISTRAR.register("earth_shaker", () -> new Encounter(225, 40, 2, 10, 13, 14, 42, 18, 65535, 255, new Encounter.Monster(26, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FRILLED_LIZARD_STINGER = ENCOUNTER_REGISTRAR.register("frilled_lizard_stinger", () -> new Encounter(225, 40, 3, 10, 0, 8, 22, 25, 65535, 255, new Encounter.Monster(69, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(108, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FRILLED_LIZARD_ARROW_SHOOTER = ENCOUNTER_REGISTRAR.register("frilled_lizard_arrow_shooter", () -> new Encounter(225, 40, 3, 7, 5, 8, 18, 25, 65535, 255, new Encounter.Monster(69, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(99, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_FRILLED_LIZARD_SCISSORHANDS = ENCOUNTER_REGISTRAR.register("scissorhands_frilled_lizard_scissorhands", () -> new Encounter(225, 40, 4, 8, 0, 12, 29, 10, 65535, 255, new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(69, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_STINGER_FRILLED_LIZARD_STINGER = ENCOUNTER_REGISTRAR.register("stinger_frilled_lizard_stinger", () -> new Encounter(225, 40, 2, 9, 2, 10, 18, 44, 65535, 255, new Encounter.Monster(108, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(69, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(108, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_EARTH_SHAKER_EARTH_SHAKER = ENCOUNTER_REGISTRAR.register("earth_shaker_earth_shaker", () -> new Encounter(225, 40, 1, 10, 5, 8, 18, 28, 65535, 255, new Encounter.Monster(26, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(26, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ERUPTING_CHICK = ENCOUNTER_REGISTRAR.register("erupting_chick", () -> new Encounter(225, 40, 13, 26, 8, 6, 13, 43, 65535, 255, new Encounter.Monster(52, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROC = ENCOUNTER_REGISTRAR.register("roc", () -> new Encounter(225, 40, 13, 27, 0, 13, 21, 25, 65535, 255, new Encounter.Monster(29, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DRAGONFLY = ENCOUNTER_REGISTRAR.register("dragonfly", () -> new Encounter(225, 40, 13, 28, 8, 6, 18, 39, 65535, 255, new Encounter.Monster(16, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SPIDER_URCHIN = ENCOUNTER_REGISTRAR.register("spider_urchin", () -> new Encounter(225, 40, 14, 27, 0, 8, 13, 24, 65535, 255, new Encounter.Monster(14, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KILLER_BIRD = ENCOUNTER_REGISTRAR.register("killer_bird", () -> new Encounter(225, 40, 14, 28, 5, 8, 18, 47, 65535, 255, new Encounter.Monster(106, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KILLER_BIRD_ERUPTING_CHICK_KILLER_BIRD = ENCOUNTER_REGISTRAR.register("killer_bird_erupting_chick_killer_bird", () -> new Encounter(225, 40, 15, 28, 5, 18, 27, 19, 65535, 255, new Encounter.Monster(106, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(52, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(106, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SPIDER_URCHIN_ROC = ENCOUNTER_REGISTRAR.register("spider_urchin_roc", () -> new Encounter(225, 40, 15, 28, 8, 27, 17, 13, 65535, 255, new Encounter.Monster(14, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(29, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ERUPTING_CHICK_SPIDER_URCHIN_ERUPTING_CHICK = ENCOUNTER_REGISTRAR.register("erupting_chick_spider_urchin_erupting_chick", () -> new Encounter(225, 40, 16, 27, 5, 8, 13, 29, 65535, 255, new Encounter.Monster(52, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(14, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(52, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROC_ROC = ENCOUNTER_REGISTRAR.register("roc_roc", () -> new Encounter(225, 40, 14, 26, 13, 5, 18, 41, 65535, 255, new Encounter.Monster(29, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(29, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KILLER_BIRD_SPIDER_URCHIN_KILLER_BIRD = ENCOUNTER_REGISTRAR.register("killer_bird_spider_urchin_killer_bird", () -> new Encounter(225, 40, 13, 25, 5, 11, 86, 25, 65535, 255, new Encounter.Monster(106, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(14, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(106, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CRAFTY_THIEF = ENCOUNTER_REGISTRAR.register("crafty_thief", () -> new Encounter(224, 40, 231, 232, 14, 6, 21, 47, 65535, 255, new Encounter.Monster(83, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GANGSTER = ENCOUNTER_REGISTRAR.register("gangster", () -> new Encounter(224, 40, 232, 4, 14, 1, 21, 46, 65535, 255, new Encounter.Monster(82, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BERSERKER = ENCOUNTER_REGISTRAR.register("berserker", () -> new Encounter(224, 40, 233, 231, 16, 6, 19, 34, 65535, 255, new Encounter.Monster(79, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PIGGY = ENCOUNTER_REGISTRAR.register("piggy", () -> new Encounter(224, 40, 231, 233, 14, 6, 18, 34, 65535, 255, new Encounter.Monster(109, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PIGGY_PIGGY = ENCOUNTER_REGISTRAR.register("piggy_piggy", () -> new Encounter(224, 40, 232, 233, 8, 42, 18, 66, 65535, 255, new Encounter.Monster(109, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(109, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BERSERKER_PIGGY = ENCOUNTER_REGISTRAR.register("berserker_piggy", () -> new Encounter(224, 40, 233, 4, 2, 8, 44, 10, 65535, 255, new Encounter.Monster(79, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(109, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CRAFTY_THIEF_BERSERKER = ENCOUNTER_REGISTRAR.register("crafty_thief_berserker", () -> new Encounter(224, 40, 231, 232, 5, 18, 22, 44, 65535, 255, new Encounter.Monster(83, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(79, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BERSERKER_PIGGY_BERSERKER = ENCOUNTER_REGISTRAR.register("berserker_piggy_berserker", () -> new Encounter(224, 40, 232, 231, 0, 8, 18, 25, 65535, 255, new Encounter.Monster(79, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(109, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(79, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CRAFTY_THIEF_CRAFTY_THIEF_CRAFTY_THIEF = ENCOUNTER_REGISTRAR.register("crafty_thief_crafty_thief_crafty_thief", () -> new Encounter(224, 40, 233, 232, 8, 0, 19, 25, 65535, 255, new Encounter.Monster(83, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(83, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(83, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PIGGY_GANGSTER_PIGGY = ENCOUNTER_REGISTRAR.register("piggy_gangster_piggy", () -> new Encounter(224, 40, 4, 231, 42, 8, 52, 29, 65535, 255, new Encounter.Monster(109, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(82, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(109, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_3 = ENCOUNTER_REGISTRAR.register("scissorhands_3", () -> new Encounter(225, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_4 = ENCOUNTER_REGISTRAR.register("scissorhands_4", () -> new Encounter(225, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_5 = ENCOUNTER_REGISTRAR.register("scissorhands_5", () -> new Encounter(225, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_6 = ENCOUNTER_REGISTRAR.register("scissorhands_6", () -> new Encounter(225, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_7 = ENCOUNTER_REGISTRAR.register("scissorhands_7", () -> new Encounter(225, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_8 = ENCOUNTER_REGISTRAR.register("scissorhands_8", () -> new Encounter(225, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-3840.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_9 = ENCOUNTER_REGISTRAR.register("scissorhands_9", () -> new Encounter(225, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4352.000000f, 0.000000f, 256.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_10 = ENCOUNTER_REGISTRAR.register("scissorhands_10", () -> new Encounter(225, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-3072.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_11 = ENCOUNTER_REGISTRAR.register("scissorhands_11", () -> new Encounter(225, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_12 = ENCOUNTER_REGISTRAR.register("scissorhands_12", () -> new Encounter(225, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GLARE = ENCOUNTER_REGISTRAR.register("glare", () -> new Encounter(225, 30, 2, 22, 47, 1, 14, 22, 65535, 255, new Encounter.Monster(23, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCREW_SHELL = ENCOUNTER_REGISTRAR.register("screw_shell", () -> new Encounter(225, 30, 3, 19, 14, 28, 21, 67, 65535, 255, new Encounter.Monster(76, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MERMAID = ENCOUNTER_REGISTRAR.register("mermaid", () -> new Encounter(225, 30, 6, 6, 14, 1, 28, 40, 65535, 255, new Encounter.Monster(91, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SEA_PIRANHA = ENCOUNTER_REGISTRAR.register("sea_piranha", () -> new Encounter(225, 30, 2, 15, 9, 34, 17, 67, 65535, 255, new Encounter.Monster(110, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FLABBY_TROLL = ENCOUNTER_REGISTRAR.register("flabby_troll", () -> new Encounter(225, 30, 2, 16, 5, 12, 23, 26, 65535, 255, new Encounter.Monster(12, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCREW_SHELL_MERMAID_SCREW_SHELL = ENCOUNTER_REGISTRAR.register("screw_shell_mermaid_screw_shell", () -> new Encounter(225, 30, 3, 16, 5, 10, 22, 25, 65535, 255, new Encounter.Monster(76, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(91, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(76, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SEA_PIRANHA_SEA_PIRANHA_SEA_PIRANHA = ENCOUNTER_REGISTRAR.register("sea_piranha_sea_piranha_sea_piranha", () -> new Encounter(225, 30, 3, 13, 0, 8, 18, 28, 65535, 255, new Encounter.Monster(110, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(110, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(110, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SEA_PIRANHA_MERMAID_SEA_PIRANHA = ENCOUNTER_REGISTRAR.register("sea_piranha_mermaid_sea_piranha", () -> new Encounter(225, 30, 4, 14, 5, 8, 23, 26, 65535, 255, new Encounter.Monster(110, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(91, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(110, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MERMAID_FLABBY_TROLL = ENCOUNTER_REGISTRAR.register("mermaid_flabby_troll", () -> new Encounter(225, 30, 2, 15, 7, 10, 19, 39, 65535, 255, new Encounter.Monster(91, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(12, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GLARE_SCREW_SHELL_GLARE = ENCOUNTER_REGISTRAR.register("glare_screw_shell_glare", () -> new Encounter(225, 30, 1, 16, 5, 8, 23, 25, 65535, 255, new Encounter.Monster(23, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(76, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(23, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FLYING_RAT = ENCOUNTER_REGISTRAR.register("flying_rat", () -> new Encounter(224, 30, 13, 14, 21, 1, 16, 34, 65535, 255, new Encounter.Monster(53, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FOREST_RUNNER = ENCOUNTER_REGISTRAR.register("forest_runner", () -> new Encounter(224, 30, 13, 15, 8, 6, 21, 34, 65535, 255, new Encounter.Monster(73, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WOUNDED_BEAR = ENCOUNTER_REGISTRAR.register("wounded_bear", () -> new Encounter(224, 30, 13, 16, 1, 8, 18, 24, 65535, 255, new Encounter.Monster(51, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DARK_ELF = ENCOUNTER_REGISTRAR.register("dark_elf", () -> new Encounter(224, 30, 14, 15, 21, 6, 17, 43, 65535, 255, new Encounter.Monster(95, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MOSS_DRESSER = ENCOUNTER_REGISTRAR.register("moss_dresser", () -> new Encounter(224, 30, 14, 16, 4, 10, 22, 40, 65535, 255, new Encounter.Monster(117, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MOSS_DRESSER_MOSS_DRESSER = ENCOUNTER_REGISTRAR.register("moss_dresser_moss_dresser", () -> new Encounter(224, 30, 15, 16, 5, 8, 18, 25, 65535, 255, new Encounter.Monster(117, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(117, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FOREST_RUNNER_FLYING_RAT = ENCOUNTER_REGISTRAR.register("forest_runner_flying_rat", () -> new Encounter(224, 30, 15, 13, 2, 18, 19, 29, 65535, 255, new Encounter.Monster(73, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(53, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DARK_ELF_FOREST_RUNNER = ENCOUNTER_REGISTRAR.register("dark_elf_forest_runner", () -> new Encounter(224, 30, 16, 14, 7, 8, 18, 44, 65535, 255, new Encounter.Monster(95, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(73, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FLYING_RAT_DARK_ELF_FLYING_RAT = ENCOUNTER_REGISTRAR.register("flying_rat_dark_elf_flying_rat", () -> new Encounter(224, 30, 14, 15, 0, 30, 12, 25, 65535, 255, new Encounter.Monster(53, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(95, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(53, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FOREST_RUNNER_MOSS_DRESSER_FOREST_RUNNER = ENCOUNTER_REGISTRAR.register("forest_runner_moss_dresser_forest_runner", () -> new Encounter(224, 30, 13, 16, 5, 18, 23, 33, 65535, 255, new Encounter.Monster(73, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(117, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(73, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PUCK = ENCOUNTER_REGISTRAR.register("puck", () -> new Encounter(226, 30, 13, 2, 6, 15, 9, 34, 65535, 255, new Encounter.Monster(44, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FAIRY = ENCOUNTER_REGISTRAR.register("fairy", () -> new Encounter(226, 30, 13, 3, 4, 8, 16, 46, 65535, 255, new Encounter.Monster(43, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GNOME = ENCOUNTER_REGISTRAR.register("gnome", () -> new Encounter(226, 30, 13, 4, 14, 1, 21, 42, 65535, 255, new Encounter.Monster(30, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SPINNINGHEAD = ENCOUNTER_REGISTRAR.register("spinninghead", () -> new Encounter(226, 30, 14, 3, 0, 21, 16, 46, 65535, 255, new Encounter.Monster(2, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TOAD_STOOL = ENCOUNTER_REGISTRAR.register("toad_stool", () -> new Encounter(226, 30, 14, 4, 3, 18, 25, 43, 65535, 255, new Encounter.Monster(111, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TOAD_STOOL_PUCK = ENCOUNTER_REGISTRAR.register("toad_stool_puck", () -> new Encounter(226, 30, 15, 4, 0, 10, 18, 25, 65535, 255, new Encounter.Monster(111, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(44, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TOAD_STOOL_GNOME_TOAD_STOOL = ENCOUNTER_REGISTRAR.register("toad_stool_gnome_toad_stool", () -> new Encounter(226, 30, 15, 1, 0, 12, 18, 47, 65535, 255, new Encounter.Monster(111, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(30, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(111, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GNOME_GNOME = ENCOUNTER_REGISTRAR.register("gnome_gnome", () -> new Encounter(226, 30, 16, 2, 7, 8, 22, 33, 65535, 255, new Encounter.Monster(30, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(30, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PUCK_SPINNINGHEAD_PUCK = ENCOUNTER_REGISTRAR.register("puck_spinninghead_puck", () -> new Encounter(226, 30, 14, 3, 2, 10, 23, 25, 65535, 255, new Encounter.Monster(44, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(2, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(44, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FAIRY_PUCK_FAIRY = ENCOUNTER_REGISTRAR.register("fairy_puck_fairy", () -> new Encounter(226, 30, 13, 28, 0, 19, 44, 29, 65535, 255, new Encounter.Monster(43, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(44, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(43, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BABY_DRAGON = ENCOUNTER_REGISTRAR.register("baby_dragon", () -> new Encounter(224, 30, 13, 14, 14, 1, 15, 34, 65535, 255, new Encounter.Monster(96, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BEASTIE_DRAGON = ENCOUNTER_REGISTRAR.register("beastie_dragon", () -> new Encounter(224, 30, 13, 15, 6, 14, 23, 34, 65535, 255, new Encounter.Monster(6, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MEGA_SEA_DRAGON = ENCOUNTER_REGISTRAR.register("mega_sea_dragon", () -> new Encounter(224, 30, 13, 16, 14, 1, 15, 33, 65535, 255, new Encounter.Monster(112, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DEADLY_SPIDER = ENCOUNTER_REGISTRAR.register("deadly_spider", () -> new Encounter(224, 30, 14, 15, 4, 15, 10, 25, 65535, 255, new Encounter.Monster(113, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WYVERN = ENCOUNTER_REGISTRAR.register("wyvern", () -> new Encounter(224, 30, 14, 16, 13, 6, 23, 39, 65535, 255, new Encounter.Monster(37, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MEGA_SEA_DRAGON_DEADLY_SPIDER = ENCOUNTER_REGISTRAR.register("mega_sea_dragon_deadly_spider", () -> new Encounter(224, 30, 15, 16, 0, 10, 19, 42, 65535, 255, new Encounter.Monster(112, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(113, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BEASTIE_DRAGON_BEASTIE_DRAGON = ENCOUNTER_REGISTRAR.register("beastie_dragon_beastie_dragon", () -> new Encounter(224, 30, 15, 13, 7, 31, 19, 45, 65535, 255, new Encounter.Monster(6, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(6, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DEADLY_SPIDER_BEASTIE_DRAGON = ENCOUNTER_REGISTRAR.register("deadly_spider_beastie_dragon", () -> new Encounter(224, 30, 16, 14, 5, 10, 24, 29, 65535, 255, new Encounter.Monster(113, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(6, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MEGA_SEA_DRAGON_DEADLY_SPIDER_MEGA_SEA_DRAGON = ENCOUNTER_REGISTRAR.register("mega_sea_dragon_deadly_spider_mega_sea_dragon", () -> new Encounter(224, 30, 14, 15, 0, 11, 23, 26, 65535, 255, new Encounter.Monster(112, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(113, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(112, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BABY_DRAGON_BABY_DRAGON_BABY_DRAGON = ENCOUNTER_REGISTRAR.register("baby_dragon_baby_dragon_baby_dragon", () -> new Encounter(224, 30, 13, 16, 1, 11, 13, 45, 65535, 255, new Encounter.Monster(96, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(96, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(96, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FREEZE_KNIGHT = ENCOUNTER_REGISTRAR.register("freeze_knight", () -> new Encounter(224, 30, 1, 26, 31, 1, 15, 34, 65535, 255, new Encounter.Monster(32, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAMMOTH = ENCOUNTER_REGISTRAR.register("mammoth", () -> new Encounter(224, 30, 1, 27, 19, 6, 17, 41, 65535, 255, new Encounter.Monster(48, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ICICLE_BALL = ENCOUNTER_REGISTRAR.register("icicle_ball", () -> new Encounter(224, 30, 1, 28, 14, 1, 22, 42, 65535, 255, new Encounter.Monster(114, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LAND_SKATER = ENCOUNTER_REGISTRAR.register("land_skater", () -> new Encounter(224, 30, 2, 27, 0, 8, 21, 46, 65535, 255, new Encounter.Monster(5, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROCKY_TURTLE = ENCOUNTER_REGISTRAR.register("rocky_turtle", () -> new Encounter(224, 30, 14, 16, 0, 10, 14, 33, 65535, 255, new Encounter.Monster(67, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FREEZE_KNIGHT_ICICLE_BALL = ENCOUNTER_REGISTRAR.register("freeze_knight_icicle_ball", () -> new Encounter(224, 30, 15, 16, 5, 11, 25, 19, 65535, 255, new Encounter.Monster(32, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(114, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LAND_SKATER_ROCKY_TURTLE = ENCOUNTER_REGISTRAR.register("land_skater_rocky_turtle", () -> new Encounter(224, 30, 16, 13, 3, 10, 23, 26, 65535, 255, new Encounter.Monster(5, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(67, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROCKY_TURTLE_ROCKY_TURTLE = ENCOUNTER_REGISTRAR.register("rocky_turtle_rocky_turtle", () -> new Encounter(224, 30, 15, 14, 7, 8, 23, 29, 65535, 255, new Encounter.Monster(67, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(67, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FREEZE_KNIGHT_LAND_SKATER_FREEZE_KNIGHT = ENCOUNTER_REGISTRAR.register("freeze_knight_land_skater_freeze_knight", () -> new Encounter(224, 30, 14, 15, 5, 12, 26, 27, 65535, 255, new Encounter.Monster(32, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(5, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(32, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LAND_SKATER_ICICLE_BALL_LAND_SKATER = ENCOUNTER_REGISTRAR.register("land_skater_icicle_ball_land_skater", () -> new Encounter(224, 30, 13, 16, 2, 11, 26, 19, 65535, 255, new Encounter.Monster(5, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(114, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(5, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_METAL_FANG = ENCOUNTER_REGISTRAR.register("metal_fang", () -> new Encounter(226, 30, 19, 20, 9, 1, 15, 34, 65535, 255, new Encounter.Monster(49, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DRAGON_SOLDIER = ENCOUNTER_REGISTRAR.register("dragon_soldier", () -> new Encounter(226, 30, 19, 21, 8, 6, 22, 39, 65535, 255, new Encounter.Monster(25, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BASILISK = ENCOUNTER_REGISTRAR.register("basilisk", () -> new Encounter(226, 30, 19, 22, 9, 1, 15, 42, 65535, 255, new Encounter.Monster(11, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MADMAN = ENCOUNTER_REGISTRAR.register("madman", () -> new Encounter(226, 30, 20, 21, 5, 14, 18, 42, 65535, 255, new Encounter.Monster(56, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_METAL_FANG_MADMAN = ENCOUNTER_REGISTRAR.register("metal_fang_madman", () -> new Encounter(226, 30, 20, 22, 1, 23, 28, 19, 65535, 255, new Encounter.Monster(49, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(56, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MADMAN_BASILISK = ENCOUNTER_REGISTRAR.register("madman_basilisk", () -> new Encounter(226, 30, 21, 22, 9, 5, 13, 33, 65535, 255, new Encounter.Monster(56, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(11, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DRAGON_SOLDIER_DRAGON_SOLDIER = ENCOUNTER_REGISTRAR.register("dragon_soldier_dragon_soldier", () -> new Encounter(226, 30, 21, 19, 5, 8, 11, 25, 65535, 255, new Encounter.Monster(25, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(25, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_METAL_FANG_DRAGON_SOLDIER_METAL_FANG = ENCOUNTER_REGISTRAR.register("metal_fang_dragon_soldier_metal_fang", () -> new Encounter(226, 30, 22, 20, 0, 25, 19, 33, 65535, 255, new Encounter.Monster(49, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(25, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(49, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BASILISK_BASILISK = ENCOUNTER_REGISTRAR.register("basilisk_basilisk", () -> new Encounter(226, 30, 20, 21, 2, 13, 18, 29, 65535, 255, new Encounter.Monster(11, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(11, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MADMAN_MADMAN = ENCOUNTER_REGISTRAR.register("madman_madman", () -> new Encounter(226, 30, 19, 22, 5, 8, 18, 40, 65535, 255, new Encounter.Monster(56, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(144, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WILDMAN = ENCOUNTER_REGISTRAR.register("wildman", () -> new Encounter(224, 30, 1, 2, 8, 6, 15, 33, 65535, 255, new Encounter.Monster(57, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BOWLING = ENCOUNTER_REGISTRAR.register("bowling", () -> new Encounter(224, 30, 1, 3, 0, 16, 8, 44, 65535, 255, new Encounter.Monster(31, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WINDY_WEASEL = ENCOUNTER_REGISTRAR.register("windy_weasel", () -> new Encounter(224, 30, 1, 4, 12, 1, 21, 42, 65535, 255, new Encounter.Monster(50, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WHITE_APE = ENCOUNTER_REGISTRAR.register("white_ape", () -> new Encounter(224, 30, 2, 3, 14, 6, 21, 42, 65535, 255, new Encounter.Monster(20, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MR_BONE = ENCOUNTER_REGISTRAR.register("mr_bone", () -> new Encounter(224, 30, 26, 28, 0, 11, 23, 44, 65535, 255, new Encounter.Monster(116, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WINDY_WEASEL_BOWLING = ENCOUNTER_REGISTRAR.register("windy_weasel_bowling", () -> new Encounter(224, 30, 27, 28, 8, 4, 30, 46, 65535, 255, new Encounter.Monster(50, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(31, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WILDMAN_BOWLING = ENCOUNTER_REGISTRAR.register("wildman_bowling", () -> new Encounter(224, 30, 25, 25, 5, 8, 18, 34, 65535, 255, new Encounter.Monster(57, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(31, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WHITE_APE_BOWLING_WHITE_APE = ENCOUNTER_REGISTRAR.register("white_ape_bowling_white_ape", () -> new Encounter(224, 30, 26, 26, 2, 19, 44, 74, 65535, 255, new Encounter.Monster(20, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(31, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(20, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WINDY_WEASEL_MR_BONE_WINDY_WEASEL = ENCOUNTER_REGISTRAR.register("windy_weasel_mr_bone_windy_weasel", () -> new Encounter(224, 30, 27, 27, 5, 10, 23, 44, 65535, 255, new Encounter.Monster(50, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(116, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(50, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MR_BONE_MR_BONE_MR_BONE = ENCOUNTER_REGISTRAR.register("mr_bone_mr_bone_mr_bone", () -> new Encounter(224, 30, 28, 28, 4, 19, 23, 24, 65535, 255, new Encounter.Monster(116, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(116, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(116, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SPRING_HITTER = ENCOUNTER_REGISTRAR.register("spring_hitter", () -> new Encounter(224, 30, 13, 26, 1, 8, 11, 34, 65535, 255, new Encounter.Monster(39, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SUCCUBUS = ENCOUNTER_REGISTRAR.register("succubus", () -> new Encounter(224, 30, 13, 27, 14, 1, 31, 42, 65535, 255, new Encounter.Monster(47, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAXIMUM_VOLT = ENCOUNTER_REGISTRAR.register("maximum_volt", () -> new Encounter(224, 30, 13, 28, 6, 16, 13, 39, 65535, 255, new Encounter.Monster(65, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WITCH = ENCOUNTER_REGISTRAR.register("witch", () -> new Encounter(224, 30, 14, 27, 8, 1, 14, 25, 65535, 255, new Encounter.Monster(63, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TERMINATOR = ENCOUNTER_REGISTRAR.register("terminator", () -> new Encounter(224, 30, 14, 28, 5, 57, 10, 33, 65535, 255, new Encounter.Monster(41, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SPRING_HITTER_SUCCUBUS = ENCOUNTER_REGISTRAR.register("spring_hitter_succubus", () -> new Encounter(224, 30, 15, 28, 4, 8, 23, 40, 65535, 255, new Encounter.Monster(39, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(47, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SUCCUBUS_SPRING_HITTER = ENCOUNTER_REGISTRAR.register("succubus_spring_hitter", () -> new Encounter(224, 30, 15, 28, 7, 10, 44, 36, 65535, 255, new Encounter.Monster(47, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(39, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TERMINATOR_WITCH_TERMINATOR = ENCOUNTER_REGISTRAR.register("terminator_witch_terminator", () -> new Encounter(224, 30, 16, 27, 5, 11, 23, 25, 65535, 255, new Encounter.Monster(41, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(63, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(41, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAXIMUM_VOLT_MAXIMUM_VOLT = ENCOUNTER_REGISTRAR.register("maximum_volt_maximum_volt", () -> new Encounter(224, 30, 14, 26, 0, 8, 12, 28, 65535, 255, new Encounter.Monster(65, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(65, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SPRING_HITTER_TERMINATOR_SPRING_HITTER = ENCOUNTER_REGISTRAR.register("spring_hitter_terminator_spring_hitter", () -> new Encounter(224, 30, 13, 25, 19, 23, 26, 10, 65535, 255, new Encounter.Monster(39, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(41, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(39, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROCKY_TURTLE_1 = ENCOUNTER_REGISTRAR.register("rocky_turtle_1", () -> new Encounter(224, 30, 25, 2, 6, 9, 18, 25, 65535, 255, new Encounter.Monster(67, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BASILISK_1 = ENCOUNTER_REGISTRAR.register("basilisk_1", () -> new Encounter(224, 30, 25, 3, 1, 9, 10, 27, 65535, 255, new Encounter.Monster(11, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN = ENCOUNTER_REGISTRAR.register("unicorn", () -> new Encounter(224, 30, 25, 4, 6, 9, 18, 33, 65535, 255, new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MR_BONE_1 = ENCOUNTER_REGISTRAR.register("mr_bone_1", () -> new Encounter(224, 30, 26, 3, 1, 9, 15, 34, 65535, 255, new Encounter.Monster(116, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TRAP_PLANT = ENCOUNTER_REGISTRAR.register("trap_plant", () -> new Encounter(224, 30, 26, 4, 5, 13, 23, 28, 65535, 255, new Encounter.Monster(120, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BASILISK_ROCKY_TURTLE = ENCOUNTER_REGISTRAR.register("basilisk_rocky_turtle", () -> new Encounter(224, 30, 27, 4, 2, 10, 13, 26, 65535, 255, new Encounter.Monster(11, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(67, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROCKY_TURTLE_ROCKY_TURTLE_1 = ENCOUNTER_REGISTRAR.register("rocky_turtle_rocky_turtle_1", () -> new Encounter(224, 30, 25, 1, 3, 12, 13, 45, 65535, 255, new Encounter.Monster(67, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(67, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MR_BONE_BASILISK = ENCOUNTER_REGISTRAR.register("mr_bone_basilisk", () -> new Encounter(224, 30, 26, 2, 5, 11, 18, 27, 65535, 255, new Encounter.Monster(116, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(11, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BASILISK_UNICORN_ROCKY_TURTLE = ENCOUNTER_REGISTRAR.register("basilisk_unicorn_rocky_turtle", () -> new Encounter(224, 30, 27, 3, 2, 11, 28, 19, 65535, 255, new Encounter.Monster(11, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(93, new Vector3f(-2176.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(67, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MR_BONE_MR_BONE_MR_BONE_1 = ENCOUNTER_REGISTRAR.register("mr_bone_mr_bone_mr_bone_1", () -> new Encounter(224, 30, 28, 4, 0, 10, 18, 29, 65535, 255, new Encounter.Monster(116, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(116, new Vector3f(-2176.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(116, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_13 = ENCOUNTER_REGISTRAR.register("scissorhands_13", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_14 = ENCOUNTER_REGISTRAR.register("scissorhands_14", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_15 = ENCOUNTER_REGISTRAR.register("scissorhands_15", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_16 = ENCOUNTER_REGISTRAR.register("scissorhands_16", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_17 = ENCOUNTER_REGISTRAR.register("scissorhands_17", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4352.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_18 = ENCOUNTER_REGISTRAR.register("scissorhands_18", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_19 = ENCOUNTER_REGISTRAR.register("scissorhands_19", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_20 = ENCOUNTER_REGISTRAR.register("scissorhands_20", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_21 = ENCOUNTER_REGISTRAR.register("scissorhands_21", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_22 = ENCOUNTER_REGISTRAR.register("scissorhands_22", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_23 = ENCOUNTER_REGISTRAR.register("scissorhands_23", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_24 = ENCOUNTER_REGISTRAR.register("scissorhands_24", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_25 = ENCOUNTER_REGISTRAR.register("scissorhands_25", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_26 = ENCOUNTER_REGISTRAR.register("scissorhands_26", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_27 = ENCOUNTER_REGISTRAR.register("scissorhands_27", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_28 = ENCOUNTER_REGISTRAR.register("scissorhands_28", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_29 = ENCOUNTER_REGISTRAR.register("scissorhands_29", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_30 = ENCOUNTER_REGISTRAR.register("scissorhands_30", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_31 = ENCOUNTER_REGISTRAR.register("scissorhands_31", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_32 = ENCOUNTER_REGISTRAR.register("scissorhands_32", () -> new Encounter(224, 30, 0, 0, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCUD_SHARK = ENCOUNTER_REGISTRAR.register("scud_shark", () -> new Encounter(226, 30, 13, 26, 0, 8, 17, 24, 65535, 255, new Encounter.Monster(75, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_JELLY = ENCOUNTER_REGISTRAR.register("jelly", () -> new Encounter(226, 30, 13, 27, 6, 8, 10, 28, 65535, 255, new Encounter.Monster(60, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_STERN_FISH = ENCOUNTER_REGISTRAR.register("stern_fish", () -> new Encounter(226, 30, 13, 28, 8, 6, 21, 42, 65535, 255, new Encounter.Monster(72, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MINOTAUR = ENCOUNTER_REGISTRAR.register("minotaur", () -> new Encounter(226, 30, 14, 27, 14, 1, 30, 27, 65535, 255, new Encounter.Monster(9, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_AQUA_KING = ENCOUNTER_REGISTRAR.register("aqua_king", () -> new Encounter(226, 30, 14, 28, 5, 8, 15, 25, 65535, 255, new Encounter.Monster(121, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_JELLY_SCUD_SHARK = ENCOUNTER_REGISTRAR.register("jelly_scud_shark", () -> new Encounter(226, 30, 15, 28, 7, 8, 11, 46, 65535, 255, new Encounter.Monster(60, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(75, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_AQUA_KING_MINOTAUR = ENCOUNTER_REGISTRAR.register("aqua_king_minotaur", () -> new Encounter(226, 30, 15, 28, 5, 11, 30, 39, 65535, 255, new Encounter.Monster(121, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(9, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_JELLY_JELLY = ENCOUNTER_REGISTRAR.register("jelly_jelly", () -> new Encounter(226, 30, 16, 27, 5, 8, 39, 52, 65535, 255, new Encounter.Monster(60, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(60, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCUD_SHARK_AQUA_KING = ENCOUNTER_REGISTRAR.register("scud_shark_aqua_king", () -> new Encounter(226, 30, 14, 26, 5, 12, 31, 39, 65535, 255, new Encounter.Monster(75, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(121, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_JELLY_MINOTAUR = ENCOUNTER_REGISTRAR.register("jelly_minotaur", () -> new Encounter(226, 30, 13, 25, 4, 12, 28, 29, 65535, 255, new Encounter.Monster(60, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(9, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PROFESSOR = ENCOUNTER_REGISTRAR.register("professor", () -> new Encounter(226, 30, 13, 14, 23, 6, 18, 41, 65535, 255, new Encounter.Monster(80, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GUILLOTINE = ENCOUNTER_REGISTRAR.register("guillotine", () -> new Encounter(226, 30, 13, 15, 9, 75, 30, 41, 65535, 255, new Encounter.Monster(40, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_HARPY = ENCOUNTER_REGISTRAR.register("harpy", () -> new Encounter(226, 30, 13, 16, 6, 11, 35, 98, 65535, 255, new Encounter.Monster(97, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SKY_CHASER = ENCOUNTER_REGISTRAR.register("sky_chaser", () -> new Encounter(226, 30, 14, 15, 5, 8, 23, 24, 65535, 255, new Encounter.Monster(122, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DEATH_PURGER = ENCOUNTER_REGISTRAR.register("death_purger", () -> new Encounter(226, 30, 14, 16, 1, 13, 33, 89, 65535, 255, new Encounter.Monster(118, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GUILLOTINE_HARPY = ENCOUNTER_REGISTRAR.register("guillotine_harpy", () -> new Encounter(226, 30, 15, 16, 7, 31, 41, 85, 65535, 255, new Encounter.Monster(40, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(97, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DEATH_PURGER_GUILLOTINE = ENCOUNTER_REGISTRAR.register("death_purger_guillotine", () -> new Encounter(226, 30, 16, 13, 78, 11, 33, 19, 65535, 255, new Encounter.Monster(118, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(40, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PROFESSOR_SKY_CHASER_PROFESSOR = ENCOUNTER_REGISTRAR.register("professor_sky_chaser_professor", () -> new Encounter(226, 30, 15, 14, 19, 1, 26, 33, 65535, 255, new Encounter.Monster(80, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(122, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(80, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_HARPY_HARPY = ENCOUNTER_REGISTRAR.register("harpy_harpy", () -> new Encounter(226, 30, 14, 15, 7, 13, 33, 52, 65535, 255, new Encounter.Monster(97, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(97, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DEATH_PURGER_PROFESSOR_DEATH_PURGER = ENCOUNTER_REGISTRAR.register("death_purger_professor_death_purger", () -> new Encounter(226, 30, 13, 16, 5, 19, 41, 29, 65535, 255, new Encounter.Monster(118, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(80, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(118, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNDEAD = ENCOUNTER_REGISTRAR.register("undead", () -> new Encounter(226, 30, 13, 26, 1, 21, 17, 24, 65535, 255, new Encounter.Monster(84, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LONER_KNIGHT = ENCOUNTER_REGISTRAR.register("loner_knight", () -> new Encounter(226, 30, 13, 27, 0, 14, 24, 42, 65535, 255, new Encounter.Monster(78, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_HYPER_SKELETON = ENCOUNTER_REGISTRAR.register("hyper_skeleton", () -> new Encounter(226, 30, 13, 28, 8, 24, 16, 80, 65535, 255, new Encounter.Monster(10, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SPECTER = ENCOUNTER_REGISTRAR.register("specter", () -> new Encounter(226, 30, 14, 27, 0, 21, 1, 42, 65535, 255, new Encounter.Monster(101, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_HUMAN_HUNTER = ENCOUNTER_REGISTRAR.register("human_hunter", () -> new Encounter(226, 30, 14, 28, 5, 10, 21, 27, 65535, 255, new Encounter.Monster(125, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNDEAD_SPECTER = ENCOUNTER_REGISTRAR.register("undead_specter", () -> new Encounter(226, 30, 15, 28, 2, 8, 23, 44, 65535, 255, new Encounter.Monster(84, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(101, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LONER_KNIGHT_HUMAN_HUNTER = ENCOUNTER_REGISTRAR.register("loner_knight_human_hunter", () -> new Encounter(226, 30, 15, 28, 5, 8, 28, 86, 65535, 255, new Encounter.Monster(78, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(125, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SPECTER_SPECTER = ENCOUNTER_REGISTRAR.register("specter_specter", () -> new Encounter(226, 30, 16, 27, 12, 4, 23, 46, 65535, 255, new Encounter.Monster(101, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(152, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LONER_KNIGHT_HYPER_SKELETON_LONER_KNIGHT = ENCOUNTER_REGISTRAR.register("loner_knight_hyper_skeleton_loner_knight", () -> new Encounter(226, 30, 14, 26, 5, 27, 12, 81, 65535, 255, new Encounter.Monster(78, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(10, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(78, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNDEAD_UNDEAD_UNDEAD = ENCOUNTER_REGISTRAR.register("undead_undead_undead", () -> new Encounter(226, 30, 13, 25, 17, 45, 24, 63, 65535, 255, new Encounter.Monster(84, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(84, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(84, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_POTBELLY = ENCOUNTER_REGISTRAR.register("potbelly", () -> new Encounter(224, 30, 25, 26, 6, 8, 16, 34, 65535, 255, new Encounter.Monster(77, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SLUG = ENCOUNTER_REGISTRAR.register("slug", () -> new Encounter(224, 30, 25, 27, 5, 10, 15, 34, 65535, 255, new Encounter.Monster(62, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CUTE_CAT = ENCOUNTER_REGISTRAR.register("cute_cat", () -> new Encounter(224, 30, 25, 28, 16, 6, 21, 43, 65535, 255, new Encounter.Monster(98, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MANTICORE = ENCOUNTER_REGISTRAR.register("manticore", () -> new Encounter(224, 30, 26, 27, 5, 10, 16, 24, 65535, 255, new Encounter.Monster(7, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MOUNTAIN_APE = ENCOUNTER_REGISTRAR.register("mountain_ape", () -> new Encounter(224, 30, 26, 28, 1, 8, 12, 42, 65535, 255, new Encounter.Monster(126, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SLUG_SLUG = ENCOUNTER_REGISTRAR.register("slug_slug", () -> new Encounter(224, 30, 27, 28, 7, 10, 36, 25, 65535, 255, new Encounter.Monster(62, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(62, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MANTICORE_CUTE_CAT = ENCOUNTER_REGISTRAR.register("manticore_cute_cat", () -> new Encounter(224, 30, 25, 28, 0, 8, 12, 27, 65535, 255, new Encounter.Monster(7, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(98, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_POTBELLY_SLUG_POTBELLY = ENCOUNTER_REGISTRAR.register("potbelly_slug_potbelly", () -> new Encounter(224, 30, 26, 27, 8, 29, 45, 78, 65535, 255, new Encounter.Monster(77, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(62, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(77, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MANTICORE_MANTICORE = ENCOUNTER_REGISTRAR.register("manticore_manticore", () -> new Encounter(224, 30, 27, 26, 5, 8, 26, 85, 65535, 255, new Encounter.Monster(7, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(7, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MOUNTAIN_APE_CUTE_CAT_MOUNTAIN_APE = ENCOUNTER_REGISTRAR.register("mountain_ape_cute_cat_mountain_ape", () -> new Encounter(224, 30, 28, 25, 0, 8, 28, 78, 65535, 255, new Encounter.Monster(126, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(98, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(126, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN_1 = ENCOUNTER_REGISTRAR.register("unicorn_1", () -> new Encounter(224, 30, 7, 8, 10, 6, 19, 39, 65535, 255, new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAD_SKULL = ENCOUNTER_REGISTRAR.register("mad_skull", () -> new Encounter(224, 30, 7, 9, 19, 1, 23, 41, 65535, 255, new Encounter.Monster(34, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SWIFT_DRAGON = ENCOUNTER_REGISTRAR.register("swift_dragon", () -> new Encounter(224, 30, 7, 10, 16, 6, 10, 39, 65535, 255, new Encounter.Monster(128, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TRAP_PLANT_1 = ENCOUNTER_REGISTRAR.register("trap_plant_1", () -> new Encounter(224, 30, 8, 9, 19, 5, 23, 28, 65535, 255, new Encounter.Monster(120, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN_MAD_SKULL = ENCOUNTER_REGISTRAR.register("unicorn_mad_skull", () -> new Encounter(224, 30, 8, 10, 36, 2, 10, 40, 65535, 255, new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(34, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN_UNICORN = ENCOUNTER_REGISTRAR.register("unicorn_unicorn", () -> new Encounter(224, 30, 9, 10, 4, 10, 19, 44, 65535, 255, new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAD_SKULL_MAD_SKULL = ENCOUNTER_REGISTRAR.register("mad_skull_mad_skull", () -> new Encounter(224, 30, 9, 7, 7, 10, 23, 29, 65535, 255, new Encounter.Monster(34, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(34, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SWIFT_DRAGON_TRAP_PLANT = ENCOUNTER_REGISTRAR.register("swift_dragon_trap_plant", () -> new Encounter(224, 30, 10, 8, 10, 5, 23, 44, 65535, 255, new Encounter.Monster(128, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(120, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN_MAD_SKULL_UNICORN = ENCOUNTER_REGISTRAR.register("unicorn_mad_skull_unicorn", () -> new Encounter(224, 30, 8, 9, 19, 0, 12, 40, 65535, 255, new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(34, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SWIFT_DRAGON_SWIFT_DRAGON_SWIFT_DRAGON = ENCOUNTER_REGISTRAR.register("swift_dragon_swift_dragon_swift_dragon", () -> new Encounter(224, 30, 7, 10, 5, 19, 23, 33, 65535, 255, new Encounter.Monster(128, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(128, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(128, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID = ENCOUNTER_REGISTRAR.register("psyche_druid", () -> new Encounter(224, 30, 1, 2, 10, 6, 19, 39, 65535, 255, new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TRICERATOPS = ENCOUNTER_REGISTRAR.register("triceratops", () -> new Encounter(224, 30, 1, 3, 12, 1, 23, 45, 65535, 255, new Encounter.Monster(71, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROULETTE_FACE = ENCOUNTER_REGISTRAR.register("roulette_face", () -> new Encounter(224, 30, 1, 4, 16, 5, 36, 39, 65535, 255, new Encounter.Monster(127, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_AIR_COMBAT = ENCOUNTER_REGISTRAR.register("air_combat", () -> new Encounter(224, 30, 2, 3, 19, 6, 28, 98, 65535, 255, new Encounter.Monster(123, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID_PSYCHE_DRUID = ENCOUNTER_REGISTRAR.register("psyche_druid_psyche_druid", () -> new Encounter(224, 30, 2, 4, 2, 12, 40, 28, 65535, 255, new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID_ROULETTE_FACE = ENCOUNTER_REGISTRAR.register("psyche_druid_roulette_face", () -> new Encounter(224, 30, 3, 4, 4, 10, 25, 44, 65535, 255, new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(127, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROULETTE_FACE_ROULETTE_FACE = ENCOUNTER_REGISTRAR.register("roulette_face_roulette_face", () -> new Encounter(224, 30, 0, 2, 7, 10, 29, 41, 65535, 255, new Encounter.Monster(127, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(127, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROULETTE_FACE_ROULETTE_FACE_ROULETTE_FACE = ENCOUNTER_REGISTRAR.register("roulette_face_roulette_face_roulette_face", () -> new Encounter(224, 30, 4, 0, 12, 0, 19, 44, 65535, 255, new Encounter.Monster(127, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(127, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(127, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_AIR_COMBAT_AIR_COMBAT = ENCOUNTER_REGISTRAR.register("air_combat_air_combat", () -> new Encounter(224, 30, 0, 1, 7, 19, 28, 98, 65535, 255, new Encounter.Monster(123, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(123, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID_PSYCHE_DRUID_PSYCHE_DRUID = ENCOUNTER_REGISTRAR.register("psyche_druid_psyche_druid_psyche_druid", () -> new Encounter(224, 30, 3, 0, 19, 5, 23, 33, 65535, 255, new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(81, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SWIFT_DRAGON_1 = ENCOUNTER_REGISTRAR.register("swift_dragon_1", () -> new Encounter(224, 30, 25, 26, 14, 1, 24, 42, 65535, 255, new Encounter.Monster(128, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_RAINBOW_BIRD_1 = ENCOUNTER_REGISTRAR.register("rainbow_bird_1", () -> new Encounter(224, 100, 26, 27, 15, 6, 26, 44, 65535, 255, new Encounter.Monster(54, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LUCKY_JAR_1 = ENCOUNTER_REGISTRAR.register("lucky_jar_1", () -> new Encounter(224, 100, 27, 25, 16, 3, 28, 47, 65535, 255, new Encounter.Monster(139, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN_2 = ENCOUNTER_REGISTRAR.register("unicorn_2", () -> new Encounter(224, 30, 13, 14, 10, 6, 31, 36, 65535, 255, new Encounter.Monster(93, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN_SWIFT_DRAGON = ENCOUNTER_REGISTRAR.register("unicorn_swift_dragon", () -> new Encounter(224, 30, 15, 16, 8, 66, 23, 40, 65535, 255, new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(128, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROULETTE_FACE_1 = ENCOUNTER_REGISTRAR.register("roulette_face_1", () -> new Encounter(224, 30, 7, 14, 11, 1, 30, 39, 65535, 255, new Encounter.Monster(127, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAD_SKULL_1 = ENCOUNTER_REGISTRAR.register("mad_skull_1", () -> new Encounter(224, 30, 8, 15, 8, 63, 13, 23, 65535, 255, new Encounter.Monster(34, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID_1 = ENCOUNTER_REGISTRAR.register("psyche_druid_1", () -> new Encounter(224, 30, 9, 16, 16, 6, 35, 33, 65535, 255, new Encounter.Monster(81, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_OOPARTS_1 = ENCOUNTER_REGISTRAR.register("ooparts_1", () -> new Encounter(224, 100, 10, 13, 15, 5, 32, 43, 65535, 255, new Encounter.Monster(42, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID_MAD_SKULL = ENCOUNTER_REGISTRAR.register("psyche_druid_mad_skull", () -> new Encounter(224, 30, 20, 21, 11, 0, 23, 39, 65535, 255, new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(34, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TRAP_PLANT_2 = ENCOUNTER_REGISTRAR.register("trap_plant_2", () -> new Encounter(224, 30, 1, 26, 10, 1, 36, 39, 65535, 255, new Encounter.Monster(120, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN_3 = ENCOUNTER_REGISTRAR.register("unicorn_3", () -> new Encounter(224, 30, 2, 27, 72, 19, 36, 28, 65535, 255, new Encounter.Monster(93, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SWIFT_DRAGON_2 = ENCOUNTER_REGISTRAR.register("swift_dragon_2", () -> new Encounter(224, 30, 3, 28, 1, 16, 39, 47, 65535, 255, new Encounter.Monster(128, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN_SWIFT_DRAGON_1 = ENCOUNTER_REGISTRAR.register("unicorn_swift_dragon_1", () -> new Encounter(224, 30, 4, 25, 69, 71, 23, 28, 65535, 255, new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(128, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN_UNICORN_1 = ENCOUNTER_REGISTRAR.register("unicorn_unicorn_1", () -> new Encounter(224, 30, 19, 21, 78, 40, 80, 28, 65535, 255, new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID_2 = ENCOUNTER_REGISTRAR.register("psyche_druid_2", () -> new Encounter(224, 30, 1, 2, 14, 6, 24, 34, 65535, 255, new Encounter.Monster(81, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAD_SKULL_2 = ENCOUNTER_REGISTRAR.register("mad_skull_2", () -> new Encounter(224, 30, 2, 3, 2, 89, 57, 17, 65535, 255, new Encounter.Monster(34, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TRAP_PLANT_3 = ENCOUNTER_REGISTRAR.register("trap_plant_3", () -> new Encounter(224, 30, 3, 4, 30, 1, 11, 27, 65535, 255, new Encounter.Monster(120, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID_PSYCHE_DRUID_1 = ENCOUNTER_REGISTRAR.register("psyche_druid_psyche_druid_1", () -> new Encounter(224, 30, 7, 8, 8, 3, 36, 33, 65535, 255, new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID_MAD_SKULL_1 = ENCOUNTER_REGISTRAR.register("psyche_druid_mad_skull_1", () -> new Encounter(224, 30, 9, 10, 19, 2, 26, 33, 65535, 255, new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(34, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SWIFT_DRAGON_3 = ENCOUNTER_REGISTRAR.register("swift_dragon_3", () -> new Encounter(224, 30, 2, 13, 16, 6, 22, 43, 65535, 255, new Encounter.Monster(128, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_AIR_COMBAT_1 = ENCOUNTER_REGISTRAR.register("air_combat_1", () -> new Encounter(224, 30, 3, 14, 11, 1, 25, 39, 65535, 255, new Encounter.Monster(123, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_TRICERATOPS_1 = ENCOUNTER_REGISTRAR.register("triceratops_1", () -> new Encounter(224, 100, 4, 15, 10, 6, 26, 45, 65535, 255, new Encounter.Monster(71, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SWIFT_DRAGON_SWIFT_DRAGON_SWIFT_DRAGON_1 = ENCOUNTER_REGISTRAR.register("swift_dragon_swift_dragon_swift_dragon_1", () -> new Encounter(224, 30, 1, 16, 19, 4, 28, 47, 65535, 255, new Encounter.Monster(128, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(128, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(128, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_AIR_COMBAT_SWIFT_DRAGON = ENCOUNTER_REGISTRAR.register("air_combat_swift_dragon", () -> new Encounter(224, 30, 20, 22, 78, 7, 11, 34, 65535, 255, new Encounter.Monster(123, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(128, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID_3 = ENCOUNTER_REGISTRAR.register("psyche_druid_3", () -> new Encounter(224, 30, 1, 2, 9, 79, 87, 34, 65535, 255, new Encounter.Monster(81, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROULETTE_FACE_2 = ENCOUNTER_REGISTRAR.register("roulette_face_2", () -> new Encounter(224, 30, 2, 3, 17, 67, 77, 34, 65535, 255, new Encounter.Monster(127, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UNICORN_4 = ENCOUNTER_REGISTRAR.register("unicorn_4", () -> new Encounter(224, 30, 3, 4, 13, 68, 89, 27, 65535, 255, new Encounter.Monster(93, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROULETTE_FACE_UNICORN = ENCOUNTER_REGISTRAR.register("roulette_face_unicorn", () -> new Encounter(224, 30, 1, 3, 17, 66, 31, 34, 65535, 255, new Encounter.Monster(127, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(93, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_PSYCHE_DRUID_ROULETTE_FACE_1 = ENCOUNTER_REGISTRAR.register("psyche_druid_roulette_face_1", () -> new Encounter(224, 30, 2, 4, 88, 66, 27, 34, 65535, 255, new Encounter.Monster(81, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(127, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_33 = ENCOUNTER_REGISTRAR.register("scissorhands_33", () -> new Encounter(224, 30, 1, 1, 0, 1, 2, 3, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_34 = ENCOUNTER_REGISTRAR.register("scissorhands_34", () -> new Encounter(224, 30, 2, 2, 4, 5, 6, 7, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_35 = ENCOUNTER_REGISTRAR.register("scissorhands_35", () -> new Encounter(224, 30, 3, 3, 59, 60, 63, 53, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_36 = ENCOUNTER_REGISTRAR.register("scissorhands_36", () -> new Encounter(224, 30, 4, 4, 67, 68, 79, 84, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_37 = ENCOUNTER_REGISTRAR.register("scissorhands_37", () -> new Encounter(224, 30, 7, 7, 57, 74, 75, 78, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_38 = ENCOUNTER_REGISTRAR.register("scissorhands_38", () -> new Encounter(224, 30, 8, 8, 71, 66, 12, 19, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_39 = ENCOUNTER_REGISTRAR.register("scissorhands_39", () -> new Encounter(224, 30, 9, 9, 8, 10, 14, 69, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_40 = ENCOUNTER_REGISTRAR.register("scissorhands_40", () -> new Encounter(224, 30, 10, 10, 15, 16, 18, 62, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_41 = ENCOUNTER_REGISTRAR.register("scissorhands_41", () -> new Encounter(224, 30, 13, 13, 9, 11, 13, 17, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_42 = ENCOUNTER_REGISTRAR.register("scissorhands_42", () -> new Encounter(224, 30, 14, 14, 50, 51, 52, 53, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_43 = ENCOUNTER_REGISTRAR.register("scissorhands_43", () -> new Encounter(224, 30, 15, 15, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_44 = ENCOUNTER_REGISTRAR.register("scissorhands_44", () -> new Encounter(224, 30, 16, 16, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_45 = ENCOUNTER_REGISTRAR.register("scissorhands_45", () -> new Encounter(224, 30, 19, 19, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_46 = ENCOUNTER_REGISTRAR.register("scissorhands_46", () -> new Encounter(224, 30, 20, 20, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_47 = ENCOUNTER_REGISTRAR.register("scissorhands_47", () -> new Encounter(224, 30, 21, 21, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_48 = ENCOUNTER_REGISTRAR.register("scissorhands_48", () -> new Encounter(224, 30, 22, 22, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_49 = ENCOUNTER_REGISTRAR.register("scissorhands_49", () -> new Encounter(224, 30, 25, 25, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_50 = ENCOUNTER_REGISTRAR.register("scissorhands_50", () -> new Encounter(224, 30, 26, 26, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_51 = ENCOUNTER_REGISTRAR.register("scissorhands_51", () -> new Encounter(224, 30, 27, 27, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_52 = ENCOUNTER_REGISTRAR.register("scissorhands_52", () -> new Encounter(224, 30, 28, 28, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_53 = ENCOUNTER_REGISTRAR.register("scissorhands_53", () -> new Encounter(224, 30, 231, 231, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_54 = ENCOUNTER_REGISTRAR.register("scissorhands_54", () -> new Encounter(224, 30, 232, 232, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_55 = ENCOUNTER_REGISTRAR.register("scissorhands_55", () -> new Encounter(224, 30, 233, 233, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_56 = ENCOUNTER_REGISTRAR.register("scissorhands_56", () -> new Encounter(224, 30, 234, 234, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_57 = ENCOUNTER_REGISTRAR.register("scissorhands_57", () -> new Encounter(224, 30, 235, 235, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_58 = ENCOUNTER_REGISTRAR.register("scissorhands_58", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_59 = ENCOUNTER_REGISTRAR.register("scissorhands_59", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_60 = ENCOUNTER_REGISTRAR.register("scissorhands_60", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_61 = ENCOUNTER_REGISTRAR.register("scissorhands_61", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_62 = ENCOUNTER_REGISTRAR.register("scissorhands_62", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_63 = ENCOUNTER_REGISTRAR.register("scissorhands_63", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_64 = ENCOUNTER_REGISTRAR.register("scissorhands_64", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_65 = ENCOUNTER_REGISTRAR.register("scissorhands_65", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_66 = ENCOUNTER_REGISTRAR.register("scissorhands_66", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_67 = ENCOUNTER_REGISTRAR.register("scissorhands_67", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_68 = ENCOUNTER_REGISTRAR.register("scissorhands_68", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_69 = ENCOUNTER_REGISTRAR.register("scissorhands_69", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_70 = ENCOUNTER_REGISTRAR.register("scissorhands_70", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_71 = ENCOUNTER_REGISTRAR.register("scissorhands_71", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_72 = ENCOUNTER_REGISTRAR.register("scissorhands_72", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_73 = ENCOUNTER_REGISTRAR.register("scissorhands_73", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_74 = ENCOUNTER_REGISTRAR.register("scissorhands_74", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_75 = ENCOUNTER_REGISTRAR.register("scissorhands_75", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_76 = ENCOUNTER_REGISTRAR.register("scissorhands_76", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_77 = ENCOUNTER_REGISTRAR.register("scissorhands_77", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_78 = ENCOUNTER_REGISTRAR.register("scissorhands_78", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_79 = ENCOUNTER_REGISTRAR.register("scissorhands_79", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_80 = ENCOUNTER_REGISTRAR.register("scissorhands_80", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_81 = ENCOUNTER_REGISTRAR.register("scissorhands_81", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_82 = ENCOUNTER_REGISTRAR.register("scissorhands_82", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_83 = ENCOUNTER_REGISTRAR.register("scissorhands_83", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_84 = ENCOUNTER_REGISTRAR.register("scissorhands_84", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_85 = ENCOUNTER_REGISTRAR.register("scissorhands_85", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_86 = ENCOUNTER_REGISTRAR.register("scissorhands_86", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_87 = ENCOUNTER_REGISTRAR.register("scissorhands_87", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_88 = ENCOUNTER_REGISTRAR.register("scissorhands_88", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_89 = ENCOUNTER_REGISTRAR.register("scissorhands_89", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_90 = ENCOUNTER_REGISTRAR.register("scissorhands_90", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_91 = ENCOUNTER_REGISTRAR.register("scissorhands_91", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_92 = ENCOUNTER_REGISTRAR.register("scissorhands_92", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MASTER_TASMAN = ENCOUNTER_REGISTRAR.register("master_tasman", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(254, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MERCHANT = ENCOUNTER_REGISTRAR.register("merchant", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(253, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROSE = ENCOUNTER_REGISTRAR.register("rose", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(255, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_93 = ENCOUNTER_REGISTRAR.register("scissorhands_93", () -> new Encounter(224, 30, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KNIGHT_OF_SANDORA_KNIGHT_OF_SANDORA_COMMANDER = ENCOUNTER_REGISTRAR.register("knight_of_sandora_knight_of_sandora_commander", () -> new Encounter(240, 0, 0, 0, 22, 58, 58, 58, 65535, 255, new Encounter.Monster(257, new Vector3f(-2560.000000f, 0.000000f, -1536.000000f)), new Encounter.Monster(257, new Vector3f(-2560.000000f, 0.000000f, 1536.000000f)), new Encounter.Monster(256, new Vector3f(-4224.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SANDORA_ELITE_SANDORA_ELITE_SANDORA_ELITE = ENCOUNTER_REGISTRAR.register("sandora_elite_sandora_elite_sandora_elite", () -> new Encounter(240, 0, 9, 9, 0, 76, 87, 76, 112, 0, new Encounter.Monster(258, new Vector3f(-2944.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(273, new Vector3f(-2944.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(273, new Vector3f(-2944.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FRUEGEL_HELLENA_WARDEN_HELLENA_WARDEN_SENIOR_WARDEN_SENIOR_WARDEN = ENCOUNTER_REGISTRAR.register("fruegel_hellena_warden_hellena_warden_senior_warden_senior_warden", () -> new Encounter(241, 0, 5, 5, 2, 14, 60, 21, 65535, 255, new Encounter.Monster(261, new Vector3f(-3712.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(259, new Vector3f(-2176.000000f, 0.000000f, 1664.000000f)), new Encounter.Monster(259, new Vector3f(-2176.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(260, new Vector3f(-2176.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(260, new Vector3f(-2176.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FRUEGEL_RODRIGUEZ_GUFTAS = ENCOUNTER_REGISTRAR.register("fruegel_rodriguez_guftas", () -> new Encounter(241, 0, 5, 5, 5, 23, 51, 51, 12, 5, new Encounter.Monster(262, new Vector3f(-4112.000000f, 0.000000f, 296.000000f)), new Encounter.Monster(263, new Vector3f(-3072.000000f, 0.000000f, -1792.000000f)), new Encounter.Monster(264, new Vector3f(-3072.000000f, 0.000000f, 2080.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KONGOL = ENCOUNTER_REGISTRAR.register("kongol", () -> new Encounter(241, 0, 6, 6, 7, 16, 59, 87, 65535, 255, new Encounter.Monster(265, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KONGOL_1 = ENCOUNTER_REGISTRAR.register("kongol_1", () -> new Encounter(241, 0, 23, 23, 63, 19, 63, 34, 65535, 255, new Encounter.Monster(266, new Vector3f(-3840.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_EMPEROR_DOEL_DRAGOON_DOEL = ENCOUNTER_REGISTRAR.register("emperor_doel_dragoon_doel", () -> new PhasedEncounter("doel", 242, 0, 17, 17, 62, 63, 62, 89, 200, 7, new Encounter.Monster(267, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(268, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LLOYD = ENCOUNTER_REGISTRAR.register("lloyd", () -> new ArenaEncounter(241, 0, 23, 23, 20, 58, 20, 58, 65535, 255, new Encounter.Monster(269, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LLOYD_DUMMY_LLOYD = ENCOUNTER_REGISTRAR.register("lloyd_dummy_lloyd", () -> new Encounter(242, 0, 31, 31, 2, 19, 95, 91, 446, 4, new Encounter.Monster(270, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(277, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FEYRBRAND_GREHAM = ENCOUNTER_REGISTRAR.register("feyrbrand_greham", () -> new Encounter(241, 0, 5, 5, 56, 68, 56, 68, 716, 0, new Encounter.Monster(275, new Vector3f(-5248.000000f, 0.000000f, 0.000000f)), new Encounter.Monster(287, new Vector3f(-3840.000000f, -1500.000000f, 3840.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DIVINE_DRAGON_DIVINE_CANNON_DIVINE_BALL = ENCOUNTER_REGISTRAR.register("divine_dragon_divine_cannon_divine_ball", () -> new Encounter(241, 0, 30, 30, 54, 75, 74, 54, 424, 12, 120, new Encounter.Monster(283, new Vector3f(-216.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(284, new Vector3f(-11584.000000f, 0.000000f, -2120.000000f)), new Encounter.Monster(285, new Vector3f(-6784.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SHIRLEY_SHANA_ALBERT = ENCOUNTER_REGISTRAR.register("shirley_shana_albert", () -> new Encounter(240, 0, 17, 17, 56, 5, 9, 46, 65535, 255, new Encounter.Monster(288, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(289, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(290, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LENUS = ENCOUNTER_REGISTRAR.register("lenus", () -> new Encounter(240, 0, 5, 5, 81, 63, 14, 99, 236, 0, new Encounter.Monster(293, new Vector3f(-6016.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LENUS_REGOLE = ENCOUNTER_REGISTRAR.register("lenus_regole", () -> new Encounter(241, 0, 227, 227, 72, 78, 72, 74, 65535, 255, new Encounter.Monster(294, new Vector3f(-4800.000000f, 0.000000f, 800.000000f)), new Encounter.Monster(279, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DAMIA = ENCOUNTER_REGISTRAR.register("damia", () -> new Encounter(242, 0, 17, 17, 0, 16, 23, 97, 65535, 255, new Encounter.Monster(295, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SYUVEIL = ENCOUNTER_REGISTRAR.register("syuveil", () -> new Encounter(242, 0, 17, 17, 0, 16, 88, 97, 65535, 255, new Encounter.Monster(296, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_BELZAC = ENCOUNTER_REGISTRAR.register("belzac", () -> new Encounter(242, 0, 17, 17, 75, 16, 23, 97, 65535, 255, new Encounter.Monster(297, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KANZAS = ENCOUNTER_REGISTRAR.register("kanzas", () -> new Encounter(242, 0, 17, 17, 0, 16, 23, 97, 65535, 255, new Encounter.Monster(298, new Vector3f(-3840.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAPPI_CRAFTY_THIEF_CRAFTY_THIEF = ENCOUNTER_REGISTRAR.register("mappi_crafty_thief_crafty_thief", () -> new Encounter(240, 0, 5, 5, 0, 10, 25, 47, 65535, 255, new Encounter.Monster(299, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(274, new Vector3f(-2944.000000f, 0.000000f, -1152.000000f)), new Encounter.Monster(274, new Vector3f(-3072.000000f, 0.000000f, 1280.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAPPI_GEHRICH = ENCOUNTER_REGISTRAR.register("mappi_gehrich", () -> new Encounter(241, 0, 5, 5, 5, 9, 62, 43, 65535, 255, new Encounter.Monster(300, new Vector3f(-2944.000000f, 0.000000f, 896.000000f)), new Encounter.Monster(301, new Vector3f(-3072.000000f, 0.000000f, -1024.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GORGAGA = ENCOUNTER_REGISTRAR.register("gorgaga", () -> new ArenaEncounter(240, 0, 18, 6, 27, 64, 64, 64, 65535, 255, new Encounter.Monster(302, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SERFIUS = ENCOUNTER_REGISTRAR.register("serfius", () -> new ArenaEncounter(240, 0, 18, 6, 27, 64, 64, 64, 65535, 255, new Encounter.Monster(303, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DANTON = ENCOUNTER_REGISTRAR.register("danton", () -> new ArenaEncounter(240, 0, 18, 6, 27, 64, 64, 64, 65535, 255, new Encounter.Monster(304, new Vector3f(-3840.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ATLOW = ENCOUNTER_REGISTRAR.register("atlow", () -> new ArenaEncounter(240, 0, 18, 6, 27, 64, 64, 64, 65535, 255, new Encounter.Monster(305, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_VIRAGE_HEAD_VIRAGE_BODY_VIRAGE_ARM_ = ENCOUNTER_REGISTRAR.register("virage_head_virage_body_virage_arm_", () -> new Encounter(242, 0, 5, 5, 83, 83, 74, 79, 119, 54, new Encounter.Monster(308, new Vector3f(-7552.000000f, 0.000000f, 0.000000f)), new Encounter.Monster(309, new Vector3f(-7552.000000f, 0.000000f, 0.000000f)), new Encounter.Monster(310, new Vector3f(-7041.000000f, -1855.000000f, 2858.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_VIRAGE_HEAD_VIRAGE_BODY_VIRAGE_ARM_VIRAGE_ARM_ = ENCOUNTER_REGISTRAR.register("virage_head_virage_body_virage_arm_virage_arm_", () -> new Encounter(242, 0, 228, 228, 54, 90, 57, 71, 256, 0, new Encounter.Monster(311, new Vector3f(-6784.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(313, new Vector3f(-6784.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(312, new Vector3f(-2688.000000f, -3538.000000f, 3968.000000f)), new Encounter.Monster(312, new Vector3f(-3226.000000f, -3454.000000f, -2642.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_S_VIRAGE_HEAD_S_VIRAGE_BODY_S_VIRAGE_ARM_ = ENCOUNTER_REGISTRAR.register("s_virage_head_s_virage_body_s_virage_arm_", () -> new Encounter(242, 0, 29, 29, 35, 66, 54, 90, 400, 6, new Encounter.Monster(316, new Vector3f(-6016.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(317, new Vector3f(-5152.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(318, new Vector3f(-4480.000000f, 0.000000f, -4476.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_S_VIRAGE_HEAD_S_VIRAGE_ARM_S_VIRAGE_BODY_ = ENCOUNTER_REGISTRAR.register("s_virage_head_s_virage_arm_s_virage_body_", () -> new Encounter(242, 0, 17, 17, 84, 54, 56, 57, 65535, 255, new Encounter.Monster(320, new Vector3f(-8320.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(321, new Vector3f(-7320.000000f, 0.000000f, -2944.000000f)), new Encounter.Monster(322, new Vector3f(-6320.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DRAKE_THE_BANDIT_WIRE_BURSTING_BALL_BURSTING_BALL_BURSTING_BALL = ENCOUNTER_REGISTRAR.register("drake_the_bandit_wire_bursting_ball_bursting_ball_bursting_ball", () -> new Encounter(240, 0, 23, 5, 69, 69, 69, 57, 65535, 255, new Encounter.Monster(325, new Vector3f(-6016.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(326, new Vector3f(-6016.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(327, new Vector3f(-6016.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(327, new Vector3f(-6016.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(327, new Vector3f(-6016.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_JIANGO = ENCOUNTER_REGISTRAR.register("jiango", () -> new Encounter(240, 0, 5, 5, 57, 18, 23, 39, 65535, 255, new Encounter.Monster(329, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_UROBOLUS = ENCOUNTER_REGISTRAR.register("urobolus", () -> new Encounter(240, 0, 17, 12, 50, 52, 54, 66, 65535, 255, new Encounter.Monster(332, new Vector3f(-9216.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FIRE_BIRD_VOLCANO_BALL_VOLCANO_BALL_VOLCANO_BALL_VOLCANO_BALL = ENCOUNTER_REGISTRAR.register("fire_bird_volcano_ball_volcano_ball_volcano_ball_volcano_ball", () -> new Encounter(240, 0, 5, 5, 51, 85, 67, 98, 65535, 255, new Encounter.Monster(333, new Vector3f(-3712.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(334, new Vector3f(-3712.000000f, 0.000000f, 1152.000000f)), new Encounter.Monster(334, new Vector3f(-3712.000000f, 0.000000f, 2176.000000f)), new Encounter.Monster(334, new Vector3f(-3712.000000f, 0.000000f, -896.000000f)), new Encounter.Monster(334, new Vector3f(-3712.000000f, 0.000000f, -1920.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GRAND_JEWEL = ENCOUNTER_REGISTRAR.register("grand_jewel", () -> new Encounter(240, 0, 5, 5, 88, 78, 51, 13, 65535, 255, new Encounter.Monster(335, new Vector3f(-8320.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GHOST_COMMANDER_GHOST_KNIGHT_GHOST_KNIGHT_GHOST_KNIGHT_GHOST_KNIGHT = ENCOUNTER_REGISTRAR.register("ghost_commander_ghost_knight_ghost_knight_ghost_knight_ghost_knight", () -> new Encounter(242, 0, 234, 234, 2, 12, 44, 56, 65535, 255, new Encounter.Monster(340, new Vector3f(-6144.000000f, 0.000000f, 0.000000f)), new Encounter.Monster(341, new Vector3f(-6016.000000f, 0.000000f, -2176.000000f)), new Encounter.Monster(341, new Vector3f(-6016.000000f, 0.000000f, 2432.000000f)), new Encounter.Monster(341, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(341, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KAMUY_KAMUYS_TREE = ENCOUNTER_REGISTRAR.register("kamuy_kamuys_tree", () -> new Encounter(240, 0, 5, 5, 54, 63, 78, 84, 65535, 255, new Encounter.Monster(343, new Vector3f(-7552.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(339, new Vector3f(-7552.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_94 = ENCOUNTER_REGISTRAR.register("scissorhands_94", () -> new Encounter(240, 0, 5, 5, 56, 60, 67, 74, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAGICIAN_FAUST_MAZO_MAZO_MAZO_MAZO = ENCOUNTER_REGISTRAR.register("magician_faust_mazo_mazo_mazo_mazo", () -> new Encounter(242, 0, 11, 11, 12, 13, 85, 69, 65535, 255, new Encounter.Monster(344, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(342, new Vector3f(-5376.000000f, 0.000000f, 3072.000000f)), new Encounter.Monster(342, new Vector3f(-5248.000000f, 0.000000f, -2944.000000f)), new Encounter.Monster(342, new Vector3f(-2944.000000f, 0.000000f, 2432.000000f)), new Encounter.Monster(342, new Vector3f(-3072.000000f, 0.000000f, -2304.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAGICIAN_FAUST = ENCOUNTER_REGISTRAR.register("magician_faust", () -> new Encounter(242, 100, 23, 23, 9, 1, 17, 99, 65535, 255, new Encounter.Monster(345, new Vector3f(-4992.000000f, 0.000000f, 640.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WINDIGO_SNOW_CANNON_SNOW_CANNON_HEART = ENCOUNTER_REGISTRAR.register("windigo_snow_cannon_snow_cannon_heart", () -> new Encounter(240, 0, 17, 17, 85, 54, 89, 1, 65535, 255, new Encounter.Monster(346, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(347, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(347, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(348, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_POLTER_HELM_POLTER_ARMOR_POLTER_SWORD = ENCOUNTER_REGISTRAR.register("polter_helm_polter_armor_polter_sword", () -> new Encounter(240, 0, 29, 29, 9, 14, 43, 67, 65535, 255, new Encounter.Monster(349, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(350, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(351, new Vector3f(-3780.000000f, 0.000000f, -472.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MISSING_MISSING_MISSING_MISSING = ENCOUNTER_REGISTRAR.register("missing_missing_missing_missing", () -> new Encounter(240, 0, 24, 24, 92, 92, 92, 92, 65535, 255, new Encounter.Monster(355, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(356, new Vector3f(-4480.000000f, 0.000000f, 2432.000000f)), new Encounter.Monster(356, new Vector3f(-4480.000000f, 0.000000f, -2176.000000f)), new Encounter.Monster(357, new Vector3f(1664.000000f, 0.000000f, -1408.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_95 = ENCOUNTER_REGISTRAR.register("scissorhands_95", () -> new Encounter(240, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_96 = ENCOUNTER_REGISTRAR.register("scissorhands_96", () -> new Encounter(240, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_97 = ENCOUNTER_REGISTRAR.register("scissorhands_97", () -> new Encounter(240, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_98 = ENCOUNTER_REGISTRAR.register("scissorhands_98", () -> new Encounter(240, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_99 = ENCOUNTER_REGISTRAR.register("scissorhands_99", () -> new Encounter(240, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_VECTOR_SELEBUS_KUBILA = ENCOUNTER_REGISTRAR.register("vector_selebus_kubila", () -> new Encounter(241, 0, 11, 11, 5, 88, 26, 77, 65535, 255, new Encounter.Monster(360, new Vector3f(-6016.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(361, new Vector3f(-4480.000000f, 0.000000f, 2432.000000f)), new Encounter.Monster(362, new Vector3f(-4480.000000f, 0.000000f, -2176.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ZACKWELL_LAVITZS_SPIRIT = ENCOUNTER_REGISTRAR.register("zackwell_lavitzs_spirit", () -> new PhasedEncounter("zackwell", 241, 0, 17, 17, 41, 12, 63, 80, 721, 4, new Encounter.Monster(363, new Vector3f(-5120.000000f, 0.000000f, 0.000000f)), new Encounter.Monster(364, new Vector3f(-5120.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_LAST_KRAKEN_CLEONE_CLEONE = ENCOUNTER_REGISTRAR.register("last_kraken_cleone_cleone", () -> new Encounter(241, 0, 17, 17, 52, 78, 68, 81, 65535, 255, new Encounter.Monster(365, new Vector3f(-7460.000000f, 0.000000f, 768.000000f)), new Encounter.Monster(366, new Vector3f(-3712.000000f, 0.000000f, -1664.000000f)), new Encounter.Monster(366, new Vector3f(-3584.000000f, 0.000000f, 3072.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CATERPILLAR_PUPA_IMAGO = ENCOUNTER_REGISTRAR.register("caterpillar_pupa_imago", () -> new Encounter(240, 0, 29, 29, 6, 24, 56, 78, 65535, 255, new Encounter.Monster(368, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(369, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(370, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DEATH_ROSE = ENCOUNTER_REGISTRAR.register("death_rose", () -> new Encounter(242, 0, 23, 23, 44, 18, 61, 61, 65535, 255, new Encounter.Monster(371, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CLAIRE_CLAIRE = ENCOUNTER_REGISTRAR.register("claire_claire", () -> new Encounter(242, 0, 23, 23, 9, 43, 13, 65, 65535, 255, new Encounter.Monster(373, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(374, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_INDORA = ENCOUNTER_REGISTRAR.register("indora", () -> new Encounter(242, 0, 23, 23, 6, 9, 64, 55, 65535, 255, new Encounter.Monster(375, new Vector3f(-5248.000000f, 0.000000f, 640.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MICHAEL_MICHAEL_CORE_ = ENCOUNTER_REGISTRAR.register("michael_michael_core_", () -> new Encounter(242, 0, 5, 17, 50, 52, 66, 80, 65535, 255, new Encounter.Monster(378, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(380, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DARK_DOEL_LIGHT_SWORD_SHADOW_BLADE = ENCOUNTER_REGISTRAR.register("dark_doel_light_sword_shadow_blade", () -> new Encounter(242, 0, 17, 17, 60, 70, 70, 87, 65535, 255, new Encounter.Monster(381, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(376, new Vector3f(-3712.000000f, 0.000000f, -640.000000f)), new Encounter.Monster(377, new Vector3f(-3712.000000f, 0.000000f, 896.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ARCHANGEL = ENCOUNTER_REGISTRAR.register("archangel", () -> new Encounter(242, 0, 17, 17, 73, 73, 89, 59, 65535, 255, new Encounter.Monster(382, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_100 = ENCOUNTER_REGISTRAR.register("scissorhands_100", () -> new Encounter(240, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_101 = ENCOUNTER_REGISTRAR.register("scissorhands_101", () -> new Encounter(240, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ZIEG_FELD_MISSING = ENCOUNTER_REGISTRAR.register("zieg_feld_missing", () -> new Encounter(241, 0, 31, 31, 13, 43, 89, 79, 735, 0, new Encounter.Monster(387, new Vector3f(-4480.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(386, new Vector3f(-9088.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MELBU_FRAHMA_MELBU_FRAHMA_MELBU_FRAHMA_MELBU_FRAHMA_MELBU_FRAHMA = ENCOUNTER_REGISTRAR.register("melbu_frahma_melbu_frahma_melbu_frahma_melbu_frahma_melbu_frahma", MelbuEncounter::new);
  public static final RegistryDelegate<Encounter> ENCOUNTER_MELBU_FRAHMA = ENCOUNTER_REGISTRAR.register("melbu_frahma", () -> new Encounter(243, 0, 17, 17, 13, 71, 91, 53, 65535, 255, new Encounter.Monster(390, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MELBU_FRAHMA_MELBU_FRAHMA_MELBU_FRAHMA_MELBU_FRAHMA_MELBU_FRAHMA_1 = ENCOUNTER_REGISTRAR.register("melbu_frahma_melbu_frahma_melbu_frahma_melbu_frahma_melbu_frahma_1", () -> new Encounter(243, 0, 17, 17, 66, 74, 53, 57, 65535, 255, new Encounter.Monster(391, new Vector3f(-5376.000000f, 0.000000f, 0.000000f)), new Encounter.Monster(392, new Vector3f(-2944.000000f, 0.000000f, -2176.000000f)), new Encounter.Monster(392, new Vector3f(-2944.000000f, 0.000000f, -640.000000f)), new Encounter.Monster(392, new Vector3f(-2944.000000f, 0.000000f, 896.000000f)), new Encounter.Monster(392, new Vector3f(-2944.000000f, 0.000000f, 2432.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MELBU_FRAHMA_TENTACLE = ENCOUNTER_REGISTRAR.register("melbu_frahma_tentacle", () -> new Encounter(243, 0, 17, 17, 57, 66, 57, 88, 65535, 255, new Encounter.Monster(393, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(394, new Vector3f(-2944.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(394, new Vector3f(-2944.000000f, 0.000000f, 1664.000000f)), new Encounter.Monster(395, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_DIVINE_DRAGOON_GHOST_DRAGON_SPIRIT = ENCOUNTER_REGISTRAR.register("divine_dragoon_ghost_dragon_spirit", () -> new Encounter(242, 0, 5, 5, 62, 68, 23, 42, 65535, 255, new Encounter.Monster(352, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(383, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GHOST_REGOLE_DRAGON_SPIRIT = ENCOUNTER_REGISTRAR.register("ghost_regole_dragon_spirit", () -> new Encounter(242, 0, 11, 11, 62, 68, 22, 39, 65535, 255, new Encounter.Monster(353, new Vector3f(128.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(384, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_GHOSTFB_DRAGON_SPIRIT = ENCOUNTER_REGISTRAR.register("ghostfb_dragon_spirit", () -> new Encounter(242, 0, 6, 6, 62, 68, 27, 41, 65535, 255, new Encounter.Monster(354, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(385, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MASTER_TASMAN_1 = ENCOUNTER_REGISTRAR.register("master_tasman_1", () -> new Encounter(224, 0, 11, 11, 58, 32, 96, 14, 65535, 255, new Encounter.Monster(254, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MERCHANT_1 = ENCOUNTER_REGISTRAR.register("merchant_1", () -> new Encounter(224, 0, 17, 17, 55, 96, 96, 96, 65535, 255, new Encounter.Monster(253, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_ROSE_1 = ENCOUNTER_REGISTRAR.register("rose_1", () -> new Encounter(224, 0, 6, 6, 58, 32, 58, 9, 65535, 255, new Encounter.Monster(255, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WILL_O_WISP_DEATH_WILL_O_WISP = ENCOUNTER_REGISTRAR.register("will_o_wisp_death_will_o_wisp", () -> new Encounter(224, 30, 235, 234, 11, 12, 30, 40, 65535, 255, new Encounter.Monster(86, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(18, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(154, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_WILL_O_WISP_SKELETON_WILL_O_WISP = ENCOUNTER_REGISTRAR.register("will_o_wisp_skeleton_will_o_wisp", () -> new Encounter(224, 30, 235, 234, 5, 8, 13, 30, 65535, 255, new Encounter.Monster(86, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(85, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(154, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SPINNINGHEAD_1 = ENCOUNTER_REGISTRAR.register("spinninghead_1", () -> new Encounter(226, 30, 0, 6, 8, 6, 24, 40, 65535, 255, new Encounter.Monster(2, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CACTUS_CACTUS = ENCOUNTER_REGISTRAR.register("cactus_cactus", () -> new Encounter(224, 30, 0, 18, 0, 8, 51, 24, 65535, 255, new Encounter.Monster(89, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(89, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SPIKY_BEETLE_SANDWORM = ENCOUNTER_REGISTRAR.register("spiky_beetle_sandworm", () -> new Encounter(224, 30, 0, 18, 2, 8, 23, 19, 65535, 255, new Encounter.Monster(61, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(3, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CANBRIA_DAYFLY_SCORPION = ENCOUNTER_REGISTRAR.register("canbria_dayfly_scorpion", () -> new Encounter(224, 30, 0, 18, 5, 23, 41, 8, 65535, 255, new Encounter.Monster(36, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(119, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SANDWORM = ENCOUNTER_REGISTRAR.register("sandworm", () -> new Encounter(224, 30, 0, 18, 0, 19, 28, 33, 65535, 255, new Encounter.Monster(3, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CANBRIA_DAYFLY = ENCOUNTER_REGISTRAR.register("canbria_dayfly", () -> new Encounter(224, 30, 0, 18, 63, 8, 28, 19, 65535, 255, new Encounter.Monster(36, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SANDORA_ELITE = ENCOUNTER_REGISTRAR.register("sandora_elite", () -> new Encounter(224, 0, 6, 231, 4, 8, 13, 75, 65535, 255, new Encounter.Monster(102, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_VIRAGE_HEAD_VIRAGE_BODY_VIRAGE_ARM_VIRAGE_ARM__1 = ENCOUNTER_REGISTRAR.register("virage_head_virage_body_virage_arm_virage_arm__1", () -> new Encounter(224, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(336, new Vector3f(-6784.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(337, new Vector3f(-6784.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(338, new Vector3f(-2688.000000f, -3538.000000f, 3968.000000f)), new Encounter.Monster(338, new Vector3f(-3226.000000f, -3454.000000f, -2642.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_102 = ENCOUNTER_REGISTRAR.register("scissorhands_102", () -> new Encounter(224, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_103 = ENCOUNTER_REGISTRAR.register("scissorhands_103", () -> new Encounter(224, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_104 = ENCOUNTER_REGISTRAR.register("scissorhands_104", () -> new Encounter(224, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_105 = ENCOUNTER_REGISTRAR.register("scissorhands_105", () -> new Encounter(224, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_106 = ENCOUNTER_REGISTRAR.register("scissorhands_106", () -> new Encounter(224, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_107 = ENCOUNTER_REGISTRAR.register("scissorhands_107", () -> new Encounter(224, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_108 = ENCOUNTER_REGISTRAR.register("scissorhands_108", () -> new Encounter(224, 0, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_HELLENA_WARDEN = ENCOUNTER_REGISTRAR.register("hellena_warden", () -> new Encounter(224, 0, 24, 24, 0, 9, 14, 17, 65535, 255, new Encounter.Monster(134, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SANDORA_SOLDIER_SANDORA_SOLDIER = ENCOUNTER_REGISTRAR.register("sandora_soldier_sandora_soldier", () -> new Encounter(224, 0, 0, 0, 6, 9, 27, 47, 113, 0, new Encounter.Monster(130, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(130, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_HELLENA_WARDEN_1 = ENCOUNTER_REGISTRAR.register("hellena_warden_1", () -> new Encounter(224, 0, 29, 0, 0, 8, 15, 25, 65535, 255, new Encounter.Monster(129, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KNIGHT_OF_SANDORA = ENCOUNTER_REGISTRAR.register("knight_of_sandora", () -> new Encounter(224, 0, 0, 18, 6, 9, 25, 19, 65535, 255, new Encounter.Monster(103, new Vector3f(-3712.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAGMA_FISH_MAGMA_FISH_1 = ENCOUNTER_REGISTRAR.register("magma_fish_magma_fish_1", () -> new Encounter(224, 30, 29, 29, 5, 8, 18, 46, 65535, 255, new Encounter.Monster(74, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(157, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_CRAFTY_THIEF_GANGSTER_CRAFTY_THIEF = ENCOUNTER_REGISTRAR.register("crafty_thief_gangster_crafty_thief", () -> new Encounter(224, 0, 232, 233, 63, 9, 54, 44, 65535, 255, new Encounter.Monster(83, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(82, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(83, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SKELETON_SKELETON_SKELETON = ENCOUNTER_REGISTRAR.register("skeleton_skeleton_skeleton", () -> new Encounter(224, 0, 235, 235, 8, 13, 8, 25, 65535, 255, new Encounter.Monster(85, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(85, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(85, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KNIGHT_OF_SANDORA_KNIGHT_OF_SANDORA = ENCOUNTER_REGISTRAR.register("knight_of_sandora_knight_of_sandora", () -> new Encounter(224, 0, 0, 0, 8, 8, 8, 20, 65535, 255, new Encounter.Monster(257, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(257, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_HELLENA_WARDEN_HELLENA_WARDEN_HELLENA_WARDEN = ENCOUNTER_REGISTRAR.register("hellena_warden_hellena_warden_hellena_warden", () -> new Encounter(224, 0, 18, 0, 4, 8, 17, 25, 65535, 255, new Encounter.Monster(134, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(134, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(134, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SANDORA_SOLDIER_SANDORA_SOLDIER_1 = ENCOUNTER_REGISTRAR.register("sandora_soldier_sandora_soldier_1", () -> new Encounter(224, 100, 0, 0, 4, 8, 18, 42, 65535, 255, new Encounter.Monster(131, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(132, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KNIGHT_OF_SANDORA_HELL_HOUND = ENCOUNTER_REGISTRAR.register("knight_of_sandora_hell_hound", () -> new Encounter(224, 0, 232, 0, 4, 8, 9, 34, 65535, 255, new Encounter.Monster(103, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(17, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_KNIGHT_OF_SANDORA_KNIGHT_OF_SANDORA_1 = ENCOUNTER_REGISTRAR.register("knight_of_sandora_knight_of_sandora_1", () -> new Encounter(224, 0, 233, 18, 7, 8, 15, 39, 65535, 255, new Encounter.Monster(103, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(103, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_HELLENA_WARDEN_HELLENA_WARDEN = ENCOUNTER_REGISTRAR.register("hellena_warden_hellena_warden", () -> new Encounter(224, 0, 24, 24, 13, 8, 25, 30, 65535, 255, new Encounter.Monster(129, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(129, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_HELLENA_WARDEN_SENIOR_WARDEN = ENCOUNTER_REGISTRAR.register("hellena_warden_senior_warden", () -> new Encounter(224, 0, 29, 5, 8, 10, 11, 15, 65535, 255, new Encounter.Monster(129, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(133, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_FOWL_FIGHTER_HELLENA_WARDEN_FOWL_FIGHTER = ENCOUNTER_REGISTRAR.register("fowl_fighter_hellena_warden_fowl_fighter", () -> new Encounter(224, 0, 24, 29, 0, 22, 16, 46, 65535, 255, new Encounter.Monster(105, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(129, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(105, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SKELETON_SKELETON_SKELETON_1 = ENCOUNTER_REGISTRAR.register("skeleton_skeleton_skeleton_1", () -> new Encounter(224, 0, 235, 234, 0, 9, 10, 11, 65535, 255, new Encounter.Monster(85, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(85, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(85, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_HELLENA_WARDEN_HELLENA_WARDEN_1 = ENCOUNTER_REGISTRAR.register("hellena_warden_hellena_warden_1", () -> new Encounter(224, 0, 29, 5, 8, 17, 16, 25, 65535, 255, new Encounter.Monster(134, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(134, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SANDORA_SOLDIER_COMMANDER_SANDORA_SOLDIER = ENCOUNTER_REGISTRAR.register("sandora_soldier_commander_sandora_soldier", () -> new Encounter(224, 100, 5, 5, 0, 18, 25, 46, 65535, 255, new Encounter.Monster(131, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(135, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(131, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SKELETON_MAGICIAN_BOGY_SKELETON = ENCOUNTER_REGISTRAR.register("skeleton_magician_bogy_skeleton", () -> new Encounter(224, 0, 235, 231, 0, 8, 10, 18, 65535, 255, new Encounter.Monster(85, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(70, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(85, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_MAGICIAN_BOGY_MAGICIAN_BOGY_MAGICIAN_BOGY = ENCOUNTER_REGISTRAR.register("magician_bogy_magician_bogy_magician_bogy", () -> new Encounter(224, 0, 234, 234, 2, 8, 18, 44, 65535, 255, new Encounter.Monster(70, new Vector3f(-3712.000000f, 0.000000f, -1408.000000f)), new Encounter.Monster(70, new Vector3f(-5248.000000f, 0.000000f, 128.000000f)), new Encounter.Monster(70, new Vector3f(-3712.000000f, 0.000000f, 1664.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_109 = ENCOUNTER_REGISTRAR.register("scissorhands_109", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_110 = ENCOUNTER_REGISTRAR.register("scissorhands_110", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_111 = ENCOUNTER_REGISTRAR.register("scissorhands_111", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_112 = ENCOUNTER_REGISTRAR.register("scissorhands_112", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_113 = ENCOUNTER_REGISTRAR.register("scissorhands_113", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_114 = ENCOUNTER_REGISTRAR.register("scissorhands_114", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_115 = ENCOUNTER_REGISTRAR.register("scissorhands_115", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_116 = ENCOUNTER_REGISTRAR.register("scissorhands_116", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_117 = ENCOUNTER_REGISTRAR.register("scissorhands_117", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_118 = ENCOUNTER_REGISTRAR.register("scissorhands_118", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_119 = ENCOUNTER_REGISTRAR.register("scissorhands_119", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-5248.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_120 = ENCOUNTER_REGISTRAR.register("scissorhands_120", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4608.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_121 = ENCOUNTER_REGISTRAR.register("scissorhands_121", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_122 = ENCOUNTER_REGISTRAR.register("scissorhands_122", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_123 = ENCOUNTER_REGISTRAR.register("scissorhands_123", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_124 = ENCOUNTER_REGISTRAR.register("scissorhands_124", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_125 = ENCOUNTER_REGISTRAR.register("scissorhands_125", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(0.000000f, 0.000000f, 0.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_126 = ENCOUNTER_REGISTRAR.register("scissorhands_126", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_127 = ENCOUNTER_REGISTRAR.register("scissorhands_127", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_128 = ENCOUNTER_REGISTRAR.register("scissorhands_128", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_129 = ENCOUNTER_REGISTRAR.register("scissorhands_129", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));
  public static final RegistryDelegate<Encounter> ENCOUNTER_SCISSORHANDS_130 = ENCOUNTER_REGISTRAR.register("scissorhands_130", () -> new Encounter(224, 10, 4, 4, 8, 8, 8, 8, 65535, 255, new Encounter.Monster(1, new Vector3f(-4480.000000f, 0.000000f, 128.000000f))));

  public static final String[] LEGACY = {
    "berserk_mouse",
    "assassin_cock",
    "trent",
    "goblin",
    "goblin_assassin_cock",
    "trent_assassin_cock",
    "berserk_mouse_assassin_cock",
    "assassin_cock_assassin_cock",
    "berserk_mouse_berserk_mouse",
    "goblin_trent",
    "vampire_kiwi",
    "mole",
    "crescent_bee",
    "mantis",
    "vampire_kiwi_vampire_kiwi",
    "vampire_kiwi_crescent_bee",
    "mantis_crescent_bee",
    "mole_mole",
    "vampire_kiwi_mole_vampire_kiwi",
    "mantis_vampire_kiwi",
    "screaming_bat_screaming_bat",
    "slime",
    "ugly_balloon",
    "orc",
    "evil_spider",
    "screaming_bat_ugly_balloon",
    "screaming_bat_orc_screaming_bat",
    "slime_ugly_balloon",
    "slime_slime",
    "orc_ugly_balloon_orc",
    "sea_dragon",
    "crocodile",
    "myconido",
    "merman",
    "myconido_myconido",
    "sea_dragon_myconido",
    "merman_merman",
    "myconido_merman_myconido",
    "crocodile_crocodile",
    "sea_dragon_merman_sea_dragon",
    "magma_fish",
    "red_hot",
    "salamander",
    "fire_spirit",
    "magma_fish_magma_fish",
    "red_hot_salamander",
    "salamander_salamander",
    "fire_spirit_fire_spirit",
    "fire_spirit_salamander",
    "red_hot_red_hot",
    "mandrake",
    "run_fast",
    "lizard_man",
    "man_eating_bud",
    "tricky_bat_tricky_bat",
    "mandrake_run_fast",
    "mandrake_lizard_man",
    "tricky_bat_mandrake_tricky_bat",
    "run_fast_lizard_man_run_fast",
    "lizard_man_lizard_man",
    "crystal_golem",
    "plague_rat_plague_rat",
    "strong_man",
    "gargoyle",
    "living_statue",
    "strong_man_gargoyle",
    "gargoyle_gargoyle",
    "plague_rat_strong_man_plague_rat",
    "gargoyle_living_statue_gargoyle",
    "living_statue_strong_man_living_statue",
    "cursed_jar",
    "treasure_jar",
    "lucky_jar",
    "ooparts",
    "scissorhands",
    "rainbow_bird",
    "blue_bird",
    "red_bird",
    "yellow_bird",
    "scissorhands_1",
    "frilled_lizard",
    "scissorhands_2",
    "arrow_shooter",
    "stinger",
    "earth_shaker",
    "frilled_lizard_stinger",
    "frilled_lizard_arrow_shooter",
    "scissorhands_frilled_lizard_scissorhands",
    "stinger_frilled_lizard_stinger",
    "earth_shaker_earth_shaker",
    "erupting_chick",
    "roc",
    "dragonfly",
    "spider_urchin",
    "killer_bird",
    "killer_bird_erupting_chick_killer_bird",
    "spider_urchin_roc",
    "erupting_chick_spider_urchin_erupting_chick",
    "roc_roc",
    "killer_bird_spider_urchin_killer_bird",
    "crafty_thief",
    "gangster",
    "berserker",
    "piggy",
    "piggy_piggy",
    "berserker_piggy",
    "crafty_thief_berserker",
    "berserker_piggy_berserker",
    "crafty_thief_crafty_thief_crafty_thief",
    "piggy_gangster_piggy",
    "scissorhands_3",
    "scissorhands_4",
    "scissorhands_5",
    "scissorhands_6",
    "scissorhands_7",
    "scissorhands_8",
    "scissorhands_9",
    "scissorhands_10",
    "scissorhands_11",
    "scissorhands_12",
    "glare",
    "screw_shell",
    "mermaid",
    "sea_piranha",
    "flabby_troll",
    "screw_shell_mermaid_screw_shell",
    "sea_piranha_sea_piranha_sea_piranha",
    "sea_piranha_mermaid_sea_piranha",
    "mermaid_flabby_troll",
    "glare_screw_shell_glare",
    "flying_rat",
    "forest_runner",
    "wounded_bear",
    "dark_elf",
    "moss_dresser",
    "moss_dresser_moss_dresser",
    "forest_runner_flying_rat",
    "dark_elf_forest_runner",
    "flying_rat_dark_elf_flying_rat",
    "forest_runner_moss_dresser_forest_runner",
    "puck",
    "fairy",
    "gnome",
    "spinninghead",
    "toad_stool",
    "toad_stool_puck",
    "toad_stool_gnome_toad_stool",
    "gnome_gnome",
    "puck_spinninghead_puck",
    "fairy_puck_fairy",
    "baby_dragon",
    "beastie_dragon",
    "mega_sea_dragon",
    "deadly_spider",
    "wyvern",
    "mega_sea_dragon_deadly_spider",
    "beastie_dragon_beastie_dragon",
    "deadly_spider_beastie_dragon",
    "mega_sea_dragon_deadly_spider_mega_sea_dragon",
    "baby_dragon_baby_dragon_baby_dragon",
    "freeze_knight",
    "mammoth",
    "icicle_ball",
    "land_skater",
    "rocky_turtle",
    "freeze_knight_icicle_ball",
    "land_skater_rocky_turtle",
    "rocky_turtle_rocky_turtle",
    "freeze_knight_land_skater_freeze_knight",
    "land_skater_icicle_ball_land_skater",
    "metal_fang",
    "dragon_soldier",
    "basilisk",
    "madman",
    "metal_fang_madman",
    "madman_basilisk",
    "dragon_soldier_dragon_soldier",
    "metal_fang_dragon_soldier_metal_fang",
    "basilisk_basilisk",
    "madman_madman",
    "wildman",
    "bowling",
    "windy_weasel",
    "white_ape",
    "mr_bone",
    "windy_weasel_bowling",
    "wildman_bowling",
    "white_ape_bowling_white_ape",
    "windy_weasel_mr_bone_windy_weasel",
    "mr_bone_mr_bone_mr_bone",
    "spring_hitter",
    "succubus",
    "maximum_volt",
    "witch",
    "terminator",
    "spring_hitter_succubus",
    "succubus_spring_hitter",
    "terminator_witch_terminator",
    "maximum_volt_maximum_volt",
    "spring_hitter_terminator_spring_hitter",
    "rocky_turtle_1",
    "basilisk_1",
    "unicorn",
    "mr_bone_1",
    "trap_plant",
    "basilisk_rocky_turtle",
    "rocky_turtle_rocky_turtle_1",
    "mr_bone_basilisk",
    "basilisk_unicorn_rocky_turtle",
    "mr_bone_mr_bone_mr_bone_1",
    "scissorhands_13",
    "scissorhands_14",
    "scissorhands_15",
    "scissorhands_16",
    "scissorhands_17",
    "scissorhands_18",
    "scissorhands_19",
    "scissorhands_20",
    "scissorhands_21",
    "scissorhands_22",
    "scissorhands_23",
    "scissorhands_24",
    "scissorhands_25",
    "scissorhands_26",
    "scissorhands_27",
    "scissorhands_28",
    "scissorhands_29",
    "scissorhands_30",
    "scissorhands_31",
    "scissorhands_32",
    "scud_shark",
    "jelly",
    "stern_fish",
    "minotaur",
    "aqua_king",
    "jelly_scud_shark",
    "aqua_king_minotaur",
    "jelly_jelly",
    "scud_shark_aqua_king",
    "jelly_minotaur",
    "professor",
    "guillotine",
    "harpy",
    "sky_chaser",
    "death_purger",
    "guillotine_harpy",
    "death_purger_guillotine",
    "professor_sky_chaser_professor",
    "harpy_harpy",
    "death_purger_professor_death_purger",
    "undead",
    "loner_knight",
    "hyper_skeleton",
    "specter",
    "human_hunter",
    "undead_specter",
    "loner_knight_human_hunter",
    "specter_specter",
    "loner_knight_hyper_skeleton_loner_knight",
    "undead_undead_undead",
    "potbelly",
    "slug",
    "cute_cat",
    "manticore",
    "mountain_ape",
    "slug_slug",
    "manticore_cute_cat",
    "potbelly_slug_potbelly",
    "manticore_manticore",
    "mountain_ape_cute_cat_mountain_ape",
    "unicorn_1",
    "mad_skull",
    "swift_dragon",
    "trap_plant_1",
    "unicorn_mad_skull",
    "unicorn_unicorn",
    "mad_skull_mad_skull",
    "swift_dragon_trap_plant",
    "unicorn_mad_skull_unicorn",
    "swift_dragon_swift_dragon_swift_dragon",
    "psyche_druid",
    "triceratops",
    "roulette_face",
    "air_combat",
    "psyche_druid_psyche_druid",
    "psyche_druid_roulette_face",
    "roulette_face_roulette_face",
    "roulette_face_roulette_face_roulette_face",
    "air_combat_air_combat",
    "psyche_druid_psyche_druid_psyche_druid",
    "swift_dragon_1",
    "rainbow_bird_1",
    "lucky_jar_1",
    "unicorn_2",
    "unicorn_swift_dragon",
    "roulette_face_1",
    "mad_skull_1",
    "psyche_druid_1",
    "ooparts_1",
    "psyche_druid_mad_skull",
    "trap_plant_2",
    "unicorn_3",
    "swift_dragon_2",
    "unicorn_swift_dragon_1",
    "unicorn_unicorn_1",
    "psyche_druid_2",
    "mad_skull_2",
    "trap_plant_3",
    "psyche_druid_psyche_druid_1",
    "psyche_druid_mad_skull_1",
    "swift_dragon_3",
    "air_combat_1",
    "triceratops_1",
    "swift_dragon_swift_dragon_swift_dragon_1",
    "air_combat_swift_dragon",
    "psyche_druid_3",
    "roulette_face_2",
    "unicorn_4",
    "roulette_face_unicorn",
    "psyche_druid_roulette_face_1",
    "scissorhands_33",
    "scissorhands_34",
    "scissorhands_35",
    "scissorhands_36",
    "scissorhands_37",
    "scissorhands_38",
    "scissorhands_39",
    "scissorhands_40",
    "scissorhands_41",
    "scissorhands_42",
    "scissorhands_43",
    "scissorhands_44",
    "scissorhands_45",
    "scissorhands_46",
    "scissorhands_47",
    "scissorhands_48",
    "scissorhands_49",
    "scissorhands_50",
    "scissorhands_51",
    "scissorhands_52",
    "scissorhands_53",
    "scissorhands_54",
    "scissorhands_55",
    "scissorhands_56",
    "scissorhands_57",
    "scissorhands_58",
    "scissorhands_59",
    "scissorhands_60",
    "scissorhands_61",
    "scissorhands_62",
    "scissorhands_63",
    "scissorhands_64",
    "scissorhands_65",
    "scissorhands_66",
    "scissorhands_67",
    "scissorhands_68",
    "scissorhands_69",
    "scissorhands_70",
    "scissorhands_71",
    "scissorhands_72",
    "scissorhands_73",
    "scissorhands_74",
    "scissorhands_75",
    "scissorhands_76",
    "scissorhands_77",
    "scissorhands_78",
    "scissorhands_79",
    "scissorhands_80",
    "scissorhands_81",
    "scissorhands_82",
    "scissorhands_83",
    "scissorhands_84",
    "scissorhands_85",
    "scissorhands_86",
    "scissorhands_87",
    "scissorhands_88",
    "scissorhands_89",
    "scissorhands_90",
    "scissorhands_91",
    "scissorhands_92",
    "master_tasman",
    "merchant",
    "rose",
    "scissorhands_93",
    "knight_of_sandora_knight_of_sandora_commander",
    "sandora_elite_sandora_elite_sandora_elite",
    "fruegel_hellena_warden_hellena_warden_senior_warden_senior_warden",
    "fruegel_rodriguez_guftas",
    "kongol",
    "kongol_1",
    "emperor_doel_dragoon_doel",
    "lloyd",
    "lloyd_dummy_lloyd",
    "feyrbrand_greham",
    "divine_dragon_divine_cannon_divine_ball",
    "shirley_shana_albert",
    "lenus",
    "lenus_regole",
    "damia",
    "syuveil",
    "belzac",
    "kanzas",
    "mappi_crafty_thief_crafty_thief",
    "mappi_gehrich",
    "gorgaga",
    "serfius",
    "danton",
    "atlow",
    "virage_head_virage_body_virage_arm_",
    "virage_head_virage_body_virage_arm_virage_arm_",
    "s_virage_head_s_virage_body_s_virage_arm_",
    "s_virage_head_s_virage_arm_s_virage_body_",
    "drake_the_bandit_wire_bursting_ball_bursting_ball_bursting_ball",
    "jiango",
    "urobolus",
    "fire_bird_volcano_ball_volcano_ball_volcano_ball_volcano_ball",
    "grand_jewel",
    "ghost_commander_ghost_knight_ghost_knight_ghost_knight_ghost_knight",
    "kamuy_kamuys_tree",
    "scissorhands_94",
    "magician_faust_mazo_mazo_mazo_mazo",
    "magician_faust",
    "windigo_snow_cannon_snow_cannon_heart",
    "polter_helm_polter_armor_polter_sword",
    "missing_missing_missing_missing",
    "scissorhands_95",
    "scissorhands_96",
    "scissorhands_97",
    "scissorhands_98",
    "scissorhands_99",
    "vector_selebus_kubila",
    "zackwell_lavitzs_spirit",
    "last_kraken_cleone_cleone",
    "caterpillar_pupa_imago",
    "death_rose",
    "claire_claire",
    "indora",
    "michael_michael_core_",
    "dark_doel_light_sword_shadow_blade",
    "archangel",
    "scissorhands_100",
    "scissorhands_101",
    "zieg_feld_missing",
    "melbu_frahma_melbu_frahma_melbu_frahma_melbu_frahma_melbu_frahma",
    "melbu_frahma",
    "melbu_frahma_melbu_frahma_melbu_frahma_melbu_frahma_melbu_frahma_1",
    "melbu_frahma_tentacle",
    "divine_dragoon_ghost_dragon_spirit",
    "ghost_regole_dragon_spirit",
    "ghostfb_dragon_spirit",
    "master_tasman_1",
    "merchant_1",
    "rose_1",
    "will_o_wisp_death_will_o_wisp",
    "will_o_wisp_skeleton_will_o_wisp",
    "spinninghead_1",
    "cactus_cactus",
    "spiky_beetle_sandworm",
    "canbria_dayfly_scorpion",
    "sandworm",
    "canbria_dayfly",
    "sandora_elite",
    "virage_head_virage_body_virage_arm_virage_arm__1",
    "scissorhands_102",
    "scissorhands_103",
    "scissorhands_104",
    "scissorhands_105",
    "scissorhands_106",
    "scissorhands_107",
    "scissorhands_108",
    "hellena_warden",
    "sandora_soldier_sandora_soldier",
    "hellena_warden_1",
    "knight_of_sandora",
    "magma_fish_magma_fish_1",
    "crafty_thief_gangster_crafty_thief",
    "skeleton_skeleton_skeleton",
    "knight_of_sandora_knight_of_sandora",
    "hellena_warden_hellena_warden_hellena_warden",
    "sandora_soldier_sandora_soldier_1",
    "knight_of_sandora_hell_hound",
    "knight_of_sandora_knight_of_sandora_1",
    "hellena_warden_hellena_warden",
    "hellena_warden_senior_warden",
    "fowl_fighter_hellena_warden_fowl_fighter",
    "skeleton_skeleton_skeleton_1",
    "hellena_warden_hellena_warden_1",
    "sandora_soldier_commander_sandora_soldier",
    "skeleton_magician_bogy_skeleton",
    "magician_bogy_magician_bogy_magician_bogy",
    "scissorhands_109",
    "scissorhands_110",
    "scissorhands_111",
    "scissorhands_112",
    "scissorhands_113",
    "scissorhands_114",
    "scissorhands_115",
    "scissorhands_116",
    "scissorhands_117",
    "scissorhands_118",
    "scissorhands_119",
    "scissorhands_120",
    "scissorhands_121",
    "scissorhands_122",
    "scissorhands_123",
    "scissorhands_124",
    "scissorhands_125",
    "scissorhands_126",
    "scissorhands_127",
    "scissorhands_128",
    "scissorhands_129",
    "scissorhands_130",
  };

  static void register(final EncounterRegistryEvent event) {
    ENCOUNTER_REGISTRAR.registryEvent(event);
  }
}
