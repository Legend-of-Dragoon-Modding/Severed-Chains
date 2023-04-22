package legend.game.types;

import legend.game.characters.Element;
import legend.game.unpacker.FileData;

public class ItemStats0c {
  /**
   * <ul>
   *   <li>0x2 - target all</li>
   *   <li>0x4 - target all</li>
   *   <li>0x10 - can be used in menu</li>
   * </ul>
   *
   * ubyte
   */
  public final int target_00;
  /** ubyte */
  public final Element element_01;
  /** ubyte */
  public final int damageMultiplier_02;
  public final int powerDefence;
  public final int powerMagicDefence;
  public final int powerAttack;
  public final int powerMagicAttack;
  public final int powerAttackHit;
  public final int powerMagicAttackHit;
  public final int powerAttackAvoid;
  public final int powerMagicAttackAvoid;
  public final boolean physicalImmunity;
  public final boolean magicalImmunity;
  public final int speedUp;
  public final int speedDown;
  public final int spPerPhysicalHit;
  public final int mpPerPhysicalHit;
  public final int spPerMagicalHit;
  public final int mpPerMagicalHit;
  /** ubyte */
  public final int damage_05;
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

    final int specialAmount = data.readByte(0x6);

    final int special1 = data.readUByte(0x3);
    final int powerDefence = (special1 & 0x80) != 0 ? specialAmount : 0;
    final int powerMagicDefence = (special1 & 0x40) != 0 ? specialAmount : 0;
    final int powerAttack = (special1 & 0x20) != 0 ? specialAmount : 0;
    final int powerMagicAttack = (special1 & 0x10) != 0 ? specialAmount : 0;
    final int powerAttackHit = (special1 & 0x8) != 0 ? specialAmount : 0;
    final int powerMagicAttackHit = (special1 & 0x4) != 0 ? specialAmount : 0;
    final int powerAttackAvoid = (special1 & 0x2) != 0 ? specialAmount : 0;
    final int powerMagicAttackAvoid = (special1 & 0x1) != 0 ? specialAmount : 0;

    final int special2 = data.readUByte(0x4);
    final boolean physicalImmunity = (special2 & 0x80) != 0;
    final boolean magicalImmunity = (special2 & 0x40) != 0;
    final int speedUp = (special2 & 0x20) != 0 ? 100 : 0;
    final int speedDown = (special2 & 0x10) != 0 ? -50 : 0;
    final int spPerPhysicalHit = (special2 & 0x8) != 0 ? specialAmount : 0;
    final int mpPerPhysicalHit = (special2 & 0x4) != 0 ? specialAmount : 0;
    final int spPerMagicalHit = (special2 & 0x2) != 0 ? specialAmount : 0;
    final int mpPerMagicalHit = (special2 & 0x1) != 0 ? specialAmount : 0;

    final int damage = data.readUByte(0x5);
    final int icon = data.readByte(0x7);
    final int status = data.readUByte(0x8);
    final int percentage = data.readUByte(0x9);
    final int uu2 = data.readUByte(0xa);
    final int type = data.readUByte(0xb);
    return new ItemStats0c(target, element, damageMultiplier, powerDefence, powerMagicDefence, powerAttack, powerMagicAttack, powerAttackHit, powerMagicAttackHit, powerAttackAvoid, powerMagicAttackAvoid, physicalImmunity, magicalImmunity, speedUp, speedDown, spPerPhysicalHit, mpPerPhysicalHit, spPerMagicalHit, mpPerMagicalHit, damage, icon, status, percentage, uu2, type);
  }

  public ItemStats0c(final int target, final Element element, final int damageMultiplier, final int powerDefence, final int powerMagicDefence, final int powerAttack, final int powerMagicAttack, final int powerAttackHit, final int powerMagicAttackHit, final int powerAttackAvoid, final int powerMagicAttackAvoid, final boolean physicalImmunity, final boolean magicalImmunity, final int speedUp, final int speedDown, final int spPerPhysicalHit, final int mpPerPhysicalHit, final int spPerMagicalHit, final int mpPerMagicalHit, final int damage, final int icon, final int status, final int percentage, final int uu2, final int type) {
    this.target_00 = target;
    this.element_01 = element;
    this.damageMultiplier_02 = damageMultiplier;
    this.powerDefence = powerDefence;
    this.powerMagicDefence = powerMagicDefence;
    this.powerAttack = powerAttack;
    this.powerMagicAttack = powerMagicAttack;
    this.powerAttackHit = powerAttackHit;
    this.powerMagicAttackHit = powerMagicAttackHit;
    this.powerAttackAvoid = powerAttackAvoid;
    this.powerMagicAttackAvoid = powerMagicAttackAvoid;
    this.physicalImmunity = physicalImmunity;
    this.magicalImmunity = magicalImmunity;
    this.speedUp = speedUp;
    this.speedDown = speedDown;
    this.spPerPhysicalHit = spPerPhysicalHit;
    this.mpPerPhysicalHit = mpPerPhysicalHit;
    this.spPerMagicalHit = spPerMagicalHit;
    this.mpPerMagicalHit = mpPerMagicalHit;
    this.damage_05 = damage;
    this.icon_07 = icon;
    this.status_08 = status;
    this.percentage_09 = percentage;
    this.uu2_0a = uu2;
    this.type_0b = type;
  }
}
