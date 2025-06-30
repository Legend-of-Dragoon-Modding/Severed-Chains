package legend.game.inventory;

import java.util.EnumMap;
import java.util.Map;

import static legend.core.GameEngine.EVENTS;

public enum ItemIcon {
  SWORD(0),
  AXE(1),
  HAMMER(2),
  SPEAR(3),
  BOW(4),
  MACE(5),
  KNUCKLE(6),
  BOXING_GLOVE(7),
  CLOTHES(8),
  ROBE(9),
  ARMOR(10),
  BREASTPLATE(11),
  RED_DRESS(12),
  LOINCLOTH(13),
  WARRIOR_DRESS(14),
  CROWN(15),
  HAIRBAND(16),
  BANDANA(16),
  HAT(17),
  HELM(18),
  SHOES(19),
  KNEEPIECE(20),
  SHIELD(20),
  BOOTS(21),
  BRACELET(22),
  RING(23),
  AMULET(24),
  STONE(25),
  JEWELLERY(26),
  PIN(27),
  BELL(28),
  BAG(29),
  CAPE(30),
  CLOAK(30),
  SCARF(30),
  GLOVE(31),
  HORN(32),
  BLUE_POTION(33),
  YELLOW_POTION(34),
  RED_POTION(35),
  ANGELS_PRAYER(36),
  GREEN_POTION(37),
  MAGIC(38),
  SKULL(39),
  UP(40),
  DOWN(41),
  SHIELD_ITEM(42),
  SMOKE_BALL(43),
  SIG_STONE(44),
  CHARM(45),
  SACK(46),
  INVALID(57),
  WARNING(58),
  NONE(64),
  ;

  private static final Map<ItemIcon, ItemIcon> ICON_MAP = new EnumMap<>(ItemIcon.class);

  public static void loadIconMap() {
    ICON_MAP.clear();
    EVENTS.postEvent(new IconMapEvent(ICON_MAP));
  }

  public final int icon;

  ItemIcon(final int icon) {
    this.icon = icon;
  }

  public ItemIcon resolve() {
    return ICON_MAP.getOrDefault(this, this);
  }
}
