package legend.lodmod;

import legend.core.GameEngine;
import legend.game.inventory.Good;
import legend.game.inventory.GoodsRegistryEvent;
import legend.game.inventory.ItemIcon;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class LodGoods {
  private LodGoods() { }

  private static final Registrar<Good, GoodsRegistryEvent> GOODS_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.goods, LodMod.MOD_ID);

  public static final RegistryDelegate<Good> RED_DRAGOON_SPIRIT = GOODS_REGISTRAR.register("red_dragoon_spirit", () -> new Good(10, ItemIcon.RED_DRAGOON_SPIRIT));
  public static final RegistryDelegate<Good> BLUE_DRAGOON_SPIRIT = GOODS_REGISTRAR.register("blue_dragoon_spirit", () -> new Good(20, ItemIcon.BLUE_DRAGOON_SPIRIT));
  public static final RegistryDelegate<Good> JADE_DRAGOON_SPIRIT = GOODS_REGISTRAR.register("jade_dragoon_spirit", () -> new Good(30, ItemIcon.JADE_DRAGOON_SPIRIT));
  public static final RegistryDelegate<Good> GOLD_DRAGOON_SPIRIT = GOODS_REGISTRAR.register("gold_dragoon_spirit", () -> new Good(40, ItemIcon.GOLD_DRAGOON_SPIRIT));
  public static final RegistryDelegate<Good> VIOLET_DRAGOON_SPIRIT = GOODS_REGISTRAR.register("violet_dragoon_spirit", () -> new Good(50, ItemIcon.VIOLET_DRAGOON_SPIRIT));
  public static final RegistryDelegate<Good> SILVER_DRAGOON_SPIRIT = GOODS_REGISTRAR.register("silver_dragoon_spirit", () -> new Good(60, ItemIcon.SILVER_DRAGOON_SPIRIT));
  public static final RegistryDelegate<Good> DARK_DRAGOON_SPIRIT = GOODS_REGISTRAR.register("dark_dragoon_spirit", () -> new Good(70, ItemIcon.DARK_DRAGOON_SPIRIT));
  public static final RegistryDelegate<Good> DIVINE_DRAGOON_SPIRIT = GOODS_REGISTRAR.register("divine_dragoon_spirit", () -> new Good(80, ItemIcon.DIVINE_DRAGOON_SPIRIT));

  public static final RegistryDelegate<Good> WAR_BULLETIN = GOODS_REGISTRAR.register("war_bulletin", () -> new Good(90));
  public static final RegistryDelegate<Good> FATHERS_STONE = GOODS_REGISTRAR.register("fathers_stone", () -> new Good(100));
  public static final RegistryDelegate<Good> PRISON_KEY = GOODS_REGISTRAR.register("prison_key", () -> new Good(110));
  public static final RegistryDelegate<Good> AXE_FROM_THE_SHACK = GOODS_REGISTRAR.register("axe_from_shack", () -> new Good(120, ItemIcon.AXE));
  public static final RegistryDelegate<Good> GOOD_SPIRITS = GOODS_REGISTRAR.register("good_spirits", () -> new Good(130, ItemIcon.BLUE_POTION));
  public static final RegistryDelegate<Good> SHINY_BAG = GOODS_REGISTRAR.register("shiny_bag", () -> new Good(140, ItemIcon.BAG));
  public static final RegistryDelegate<Good> WATER_BOTTLE = GOODS_REGISTRAR.register("water_bottle", () -> new Good(150, ItemIcon.BLUE_POTION));
  public static final RegistryDelegate<Good> LIFE_WATER = GOODS_REGISTRAR.register("life_water", () -> new Good(160, ItemIcon.BLUE_POTION));
  public static final RegistryDelegate<Good> MAGIC_OIL = GOODS_REGISTRAR.register("magic_oil", () -> new Good(170));
  public static final RegistryDelegate<Good> YELLOW_STONE = GOODS_REGISTRAR.register("yellow_stone", () -> new Good(180));
  public static final RegistryDelegate<Good> BLUE_STONE = GOODS_REGISTRAR.register("blue_stone", () -> new Good(190));
  public static final RegistryDelegate<Good> RED_STONE = GOODS_REGISTRAR.register("red_stone", () -> new Good(200));
  public static final RegistryDelegate<Good> LETTER_FROM_LYNN = GOODS_REGISTRAR.register("letter_from_lynn", () -> new Good(210));
  public static final RegistryDelegate<Good> PASS_FOR_VALLEY = GOODS_REGISTRAR.register("pass_for_valley", () -> new Good(220));
  public static final RegistryDelegate<Good> KATES_BOUQUET = GOODS_REGISTRAR.register("kates_bouquet", () -> new Good(230));
  public static final RegistryDelegate<Good> KEY_TO_SHIP = GOODS_REGISTRAR.register("key_to_ship", () -> new Good(240));
  public static final RegistryDelegate<Good> BOAT_LICENSE = GOODS_REGISTRAR.register("boat_license", () -> new Good(250));
  public static final RegistryDelegate<Good> DRAGON_BLOCKER = GOODS_REGISTRAR.register("dragon_blocker", () -> new Good(260));
  public static final RegistryDelegate<Good> MOON_GEM = GOODS_REGISTRAR.register("moon_gem", () -> new Good(270));
  public static final RegistryDelegate<Good> MOON_DAGGER = GOODS_REGISTRAR.register("moon_dagger", () -> new Good(280));
  public static final RegistryDelegate<Good> MOON_MIRROR = GOODS_REGISTRAR.register("moon_mirror", () -> new Good(290));
  public static final RegistryDelegate<Good> OMEGA_BOMB = GOODS_REGISTRAR.register("omega_bomb", () -> new Good(300, ItemIcon.MAGIC));
  public static final RegistryDelegate<Good> OMEGA_MASTER = GOODS_REGISTRAR.register("omega_master", () -> new Good(310, ItemIcon.MAGIC));
  public static final RegistryDelegate<Good> LAW_MAKER = GOODS_REGISTRAR.register("law_maker", () -> new Good(320));
  public static final RegistryDelegate<Good> LAW_OUTPUT = GOODS_REGISTRAR.register("law_output", () -> new Good(330));
  public static final RegistryDelegate<Good> GOLD_DRAGOON_SPIRIT_2 = GOODS_REGISTRAR.register("gold_dragoon_spirit_2", () -> new Good(340));
  public static final RegistryDelegate<Good> MAGIC_SHINY_BAG = GOODS_REGISTRAR.register("magic_shiny_bag", () -> new Good(350, ItemIcon.BAG));
  public static final RegistryDelegate<Good> VANISHING_STONE = GOODS_REGISTRAR.register("vanishing_stone", () -> new Good(360));
  public static final RegistryDelegate<Good> LAVITZS_PICTURE = GOODS_REGISTRAR.register("lavitzs_picture", () -> new Good(370));

  static void register(final GoodsRegistryEvent event) {
    GOODS_REGISTRAR.registryEvent(event);
  }
}
