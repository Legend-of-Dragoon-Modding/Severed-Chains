package legend.game.types;

import legend.game.characters.Element;
import legend.game.unpacker.FileData;

public class ItemStats0c {
  /**
   * <ul>
   *   <li>0x2 - target all</li>
   * </ul>
   *
   * ubyte
   */
  public final int target_00;
  /** ubyte */
  public final Element element_01;
  /** ubyte */
  public final int damageMultiplier_02;
  /**
   * Read left to right
   *
   * <ul>
   *   <li>0x80 - power defence</li>
   *   <li>0x40 - power magic defence</li>
   *   <li>0x20 - power attack</li>
   *   <li>0x10 - power magic attack</li>
   *   <li>0x8 - attack hit</li>
   *   <li>0x4 - magic attack hit</li>
   *   <li>0x2 - attack avoid</li>
   *   <li>0x1 - magic attack avoid</li>
   * </ul>
   *
   * ubyte
   */
  public final int special1_03;
  /**
   * Read left to right
   *
   * <ul>
   *   <li>0x80 - physical immunity</li>
   *   <li>0x40 - magical immunity</li>
   *   <li>0x20 - speed up</li>
   *   <li>0x10 - speed down</li>
   *   <li>0x8 - SP per physical hit</li>
   *   <li>0x4 - MP per physical hit</li>
   *   <li>0x2 - SP per magical hit</li>
   *   <li>0x1 - MP per magical hit</li>
   * </ul>
   *
   * ubyte
   */
  public final int special2_04;
  /** ubyte */
  public final int damage_05;
  /** byte */
  public final int specialAmount_06;
  /** byte */
  public final int icon_07;
  /** ubyte */
  public final int status_08;
  /** ubyte */
  public final int percentage_09;
  /** ubyte */
  public final int uu2_0a;
  /**
   * <ul>
   *   <li>0x04 - cause status</li>
   *   <li>0x08 - cure status</li>
   *   <li>0x10 - revive</li>
   *   <li>0x20 - SP</li>
   *   <li>0x40 - MP</li>
   *   <li>0x80 - HP</li>
   * </ul>
   *
   * ubyte
   */
  public final int type_0b;

  public static ItemStats0c fromFile(final FileData data) {
    final int target = data.readUByte(0x0);
    final Element element = Element.fromFlag(data.readUByte(0x1));
    final int damageMultiplier = data.readUByte(0x2);
    final int special1 = data.readUByte(0x3);
    final int special2 = data.readUByte(0x4);
    final int damage = data.readUByte(0x5);
    final int specialAmount = data.readByte(0x6);
    final int icon = data.readByte(0x7);
    final int status = data.readUByte(0x8);
    final int percentage = data.readUByte(0x9);
    final int uu2 = data.readUByte(0xa);
    final int type = data.readUByte(0xb);
    return new ItemStats0c(target, element, damageMultiplier, special1, special2, damage, specialAmount, icon, status, percentage, uu2, type);
  }

  public ItemStats0c(final int target, final Element element, final int damageMultiplier, final int special1, final int special2, final int damage, final int specialAmount, final int icon, final int status, final int percentage, final int uu2, final int type) {
    this.target_00 = target;
    this.element_01 = element;
    this.damageMultiplier_02 = damageMultiplier;
    this.special1_03 = special1;
    this.special2_04 = special2;
    this.damage_05 = damage;
    this.specialAmount_06 = specialAmount;
    this.icon_07 = icon;
    this.status_08 = status;
    this.percentage_09 = percentage;
    this.uu2_0a = uu2;
    this.type_0b = type;
  }
}
