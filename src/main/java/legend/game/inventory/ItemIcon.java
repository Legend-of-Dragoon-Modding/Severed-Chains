package legend.game.inventory;

import legend.game.types.Renderable58;
import legend.game.types.UiType;

import java.util.HashMap;
import java.util.Map;

import static legend.core.GameEngine.EVENTS;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;

public class ItemIcon {
  public static final ItemIcon SWORD = new ItemIcon(0);
  public static final ItemIcon AXE = new ItemIcon(1);
  public static final ItemIcon HAMMER = new ItemIcon(2);
  public static final ItemIcon SPEAR = new ItemIcon(3);
  public static final ItemIcon BOW = new ItemIcon(4);
  public static final ItemIcon MACE = new ItemIcon(5);
  public static final ItemIcon KNUCKLE = new ItemIcon(6);
  public static final ItemIcon BOXING_GLOVE = new ItemIcon(7);
  public static final ItemIcon CLOTHES = new ItemIcon(8);
  public static final ItemIcon ROBE = new ItemIcon(9);
  public static final ItemIcon ARMOR = new ItemIcon(10);
  public static final ItemIcon BREASTPLATE = new ItemIcon(11);
  public static final ItemIcon RED_DRESS = new ItemIcon(12);
  public static final ItemIcon LOINCLOTH = new ItemIcon(13);
  public static final ItemIcon WARRIOR_DRESS = new ItemIcon(14);
  public static final ItemIcon CROWN = new ItemIcon(15);
  public static final ItemIcon HAIRBAND = new ItemIcon(16);
  public static final ItemIcon BANDANA = new ItemIcon(16);
  public static final ItemIcon HAT = new ItemIcon(17);
  public static final ItemIcon HELM = new ItemIcon(18);
  public static final ItemIcon SHOES = new ItemIcon(19);
  public static final ItemIcon KNEEPIECE = new ItemIcon(20);
  public static final ItemIcon SHIELD = new ItemIcon(20);
  public static final ItemIcon BOOTS = new ItemIcon(21);
  public static final ItemIcon BRACELET = new ItemIcon(22);
  public static final ItemIcon RING = new ItemIcon(23);
  public static final ItemIcon AMULET = new ItemIcon(24);
  public static final ItemIcon STONE = new ItemIcon(25);
  public static final ItemIcon JEWELLERY = new ItemIcon(26);
  public static final ItemIcon PIN = new ItemIcon(27);
  public static final ItemIcon BELL = new ItemIcon(28);
  public static final ItemIcon BAG = new ItemIcon(29);
  public static final ItemIcon CAPE = new ItemIcon(30);
  public static final ItemIcon CLOAK = new ItemIcon(30);
  public static final ItemIcon SCARF = new ItemIcon(30);
  public static final ItemIcon GLOVE = new ItemIcon(31);
  public static final ItemIcon HORN = new ItemIcon(32);
  public static final ItemIcon BLUE_POTION = new ItemIcon(33);
  public static final ItemIcon YELLOW_POTION = new ItemIcon(34);
  public static final ItemIcon RED_POTION = new ItemIcon(35);
  public static final ItemIcon ANGELS_PRAYER = new ItemIcon(36);
  public static final ItemIcon GREEN_POTION = new ItemIcon(37);
  public static final ItemIcon MAGIC = new ItemIcon(38);
  public static final ItemIcon SKULL = new ItemIcon(39);
  public static final ItemIcon UP = new ItemIcon(40);
  public static final ItemIcon DOWN = new ItemIcon(41);
  public static final ItemIcon SHIELD_ITEM = new ItemIcon(42);
  public static final ItemIcon SMOKE_BALL = new ItemIcon(43);
  public static final ItemIcon SIG_STONE = new ItemIcon(44);
  public static final ItemIcon CHARM = new ItemIcon(45);
  public static final ItemIcon SACK = new ItemIcon(46);
  public static final ItemIcon INVALID = new ItemIcon(57);
  public static final ItemIcon WARNING = new ItemIcon(58);
  public static final ItemIcon NONE = new ItemIcon(64);

  private static final Map<ItemIcon, ItemIcon> ICON_MAP = new HashMap<>();

  public static void loadIconMap() {
    ICON_MAP.clear();
    EVENTS.postEvent(new IconMapEvent(ICON_MAP));
  }

  public final int icon;

  protected ItemIcon(final int icon) {
    this.icon = icon;
  }

  public ItemIcon resolve() {
    return ICON_MAP.getOrDefault(this, this);
  }

  protected UiType getUiType() {
    return uiFile_800bdc3c.itemIcons_c6a4();
  }

  public Renderable58 render(final int x, final int y, final int flags) {
    final Renderable58 renderable = allocateRenderable(this.getUiType(), null);
    renderable.flags_00 |= flags | Renderable58.FLAG_NO_ANIMATION;
    renderable.glyph_04 = this.resolve().icon;
    renderable.startGlyph_10 = renderable.glyph_04;
    renderable.endGlyph_14 = renderable.glyph_04;
    renderable.tpage_2c = 0x19;
    renderable.clut_30 = 0;
    renderable.x_40 = x;
    renderable.y_44 = y;
    return renderable;
  }
}
