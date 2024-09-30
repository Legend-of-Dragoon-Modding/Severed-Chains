package legend.game.inventory;

import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;
import legend.game.types.EquipmentSlot;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public class Equipment extends RegistryEntry implements InventoryEntry, ScriptReadable {
  public final int price;

  /**
   * <ul>
   *   <li>0x4 - can't be discarded</li>
   * </ul>
   */
  public final int flags_00;
  public final EquipmentSlot slot;
  public final int _02;
  /**
   * Which characters can wear this (bitset)
   */
  public final int equipableFlags_03;
  public final ElementSet attackElement_04 = new ElementSet();
  public final int _05;
  public final ElementSet elementalResistance_06 = new ElementSet();
  public final ElementSet elementalImmunity_07 = new ElementSet();
  public final int statusResist_08;
  public final int _09;
  public final int attack1_0a;

  public final int mpPerPhysicalHit;
  public final int spPerPhysicalHit;
  public final int mpPerMagicalHit;
  public final int spPerMagicalHit;
  public final int hpMultiplier;
  public final int mpMultiplier;
  public final int spMultiplier;
  public final boolean magicalResistance;
  public final boolean physicalResistance;
  public final boolean magicalImmunity;
  public final boolean physicalImmunity;
  public final int revive;
  /** Percentage */
  public final int hpRegen;
  /** Percentage */
  public final int mpRegen;
  /** Percentage */
  public final int spRegen;
  public final int escapeBonus;

  public final int icon_0e;
  public final int speed_0f;
  public final int attack2_10;
  public final int magicAttack_11;
  public final int defence_12;
  public final int magicDefence_13;
  public final int attackHit_14;
  public final int magicHit_15;
  public final int attackAvoid_16;
  public final int magicAvoid_17;
  public final int onHitStatusChance_18;
  public final int _19;
  public final int _1a;
  public final int onHitStatus_1b;

  public static Equipment fromFile(final int price, final FileData data) {
    final int flags = data.readUByte(0x0);
    final int type = data.readUByte(0x1);
    final int _02 = data.readUByte(0x2);
    final int equipableFlags = data.readUByte(0x3);
    final Element element = Element.fromFlag(data.readUByte(0x4)).get();
    final int _05 = data.readUByte(0x5);
    final ElementSet elementalResistance = new ElementSet().unpack(data.readUByte(0x6));
    final ElementSet elementalImmunity = new ElementSet().unpack(data.readUByte(0x7));
    final int statusResist = data.readUByte(0x8);
    final int _09 = data.readUByte(0x9);
    final int atk = data.readUByte(0xa);

    final int special1 = data.readUByte(0xb);
    final int special2 = data.readUByte(0xc);
    final int specialAmount = data.readUByte(0xd);

    final int mpPerMagicalHit = (special1 & 0x1) != 0 ? specialAmount : 0;
    final int spPerMagicalHit = (special1 & 0x2) != 0 ? specialAmount : 0;
    final int mpPerPhysicalHit = (special1 & 0x4) != 0 ? specialAmount : 0;
    final int spPerPhysicalHit = (special1 & 0x8) != 0 ? specialAmount : 0;
    final int spMultiplier = (special1 & 0x10) != 0 ? specialAmount : 0;
    final boolean physicalResistance = (special1 & 0x20) != 0;
    final boolean magicalImmunity = (special1 & 0x40) != 0;
    final boolean physicalImmunity = (special1 & 0x80) != 0;
    final int mpMultiplier = (special2 & 0x1) != 0 ? specialAmount : 0;
    final int hpMultiplier = (special2 & 0x2) != 0 ? specialAmount : 0;
    final boolean magicalResistance = (special2 & 0x4) != 0;
    final int revive = (special2 & 0x8) != 0 ? specialAmount : 0;
    final int spRegen = (special2 & 0x10) != 0 ? specialAmount : 0;
    final int mpRegen = (special2 & 0x20) != 0 ? specialAmount : 0;
    final int hpRegen = (special2 & 0x40) != 0 ? specialAmount : 0;
    final int escapeBonus = (special2 & 0x80) != 0 ? specialAmount : 0;

    final int icon = data.readUByte(0xe);
    final int spd = data.readByte(0xf);
    final int atkHi = data.readByte(0x10);
    final int matk = data.readByte(0x11);
    final int def = data.readByte(0x12);
    final int mdef = data.readByte(0x13);
    final int aHit = data.readByte(0x14);
    final int mHit = data.readByte(0x15);
    final int aAv = data.readByte(0x16);
    final int mAv = data.readByte(0x17);
    final int onStatusChance = data.readByte(0x18);
    final int _19 = data.readUByte(0x19);
    final int _1a = data.readUByte(0x1a);
    final int onHitStatus = data.readUByte(0x1b);

    return new Equipment(price, flags, type, _02, equipableFlags, element, _05, elementalResistance, elementalImmunity, statusResist, _09, atk, mpPerPhysicalHit, spPerPhysicalHit, mpPerMagicalHit, spPerMagicalHit, hpMultiplier, mpMultiplier, spMultiplier, magicalResistance, physicalResistance, magicalImmunity, physicalImmunity, revive, hpRegen, mpRegen, spRegen, escapeBonus, icon, spd, atkHi, matk, def, mdef, aHit, mHit, aAv, mAv, onStatusChance, _19, _1a, onHitStatus);
  }

  public Equipment(final int price, final int flags, final int type, final int _02, final int equipableFlags, final Element element, final int _05, final ElementSet elementalResistance, final ElementSet elementalImmunity, final int statusResist, final int _09, final int atk, final int mpPerPhysicalHit, final int spPerPhysicalHit, final int mpPerMagicalHit, final int spPerMagicalHit, final int hpMultiplier, final int mpMultiplier, final int spMultiplier, final boolean magicalResistance, final boolean physicalResistance, final boolean magicalImmunity, final boolean physicalImmunity, final int revive, final int hpRegen, final int mpRegen, final int spRegen, final int escapeBonus, final int icon, final int spd, final int atkHi, final int matk, final int def, final int mdef, final int aHit, final int mHit, final int aAv, final int mAv, final int onStatusChance, final int _19, final int _1a, final int onHitStatus) {
    this.price = price;

    EquipmentSlot slot = null;
    for(int i = 0; i < EquipmentSlot.values().length; i++) {
      if((type & 0x80 >> i) != 0) {
        slot = EquipmentSlot.values()[i];
        break;
      }
    }

    this.slot = slot;

    this.flags_00 = flags;
    this._02 = _02;
    this.equipableFlags_03 = equipableFlags;
    this.attackElement_04.add(element);
    this._05 = _05;
    this.mpPerPhysicalHit = mpPerPhysicalHit;
    this.spPerPhysicalHit = spPerPhysicalHit;
    this.mpPerMagicalHit = mpPerMagicalHit;
    this.spPerMagicalHit = spPerMagicalHit;
    this.hpMultiplier = hpMultiplier;
    this.mpMultiplier = mpMultiplier;
    this.spMultiplier = spMultiplier;
    this.magicalResistance = magicalResistance;
    this.physicalResistance = physicalResistance;
    this.magicalImmunity = magicalImmunity;
    this.physicalImmunity = physicalImmunity;
    this.revive = revive;
    this.hpRegen = hpRegen;
    this.mpRegen = mpRegen;
    this.spRegen = spRegen;
    this.escapeBonus = escapeBonus;
    this.elementalResistance_06.set(elementalResistance);
    this.elementalImmunity_07.set(elementalImmunity);
    this.statusResist_08 = statusResist;
    this._09 = _09;
    this.attack1_0a = atk;
    this.icon_0e = icon;
    this.speed_0f = spd;
    this.attack2_10 = atkHi;
    this.magicAttack_11 = matk;
    this.defence_12 = def;
    this.magicDefence_13 = mdef;
    this.attackHit_14 = aHit;
    this.magicHit_15 = mHit;
    this.attackAvoid_16 = aAv;
    this.magicAvoid_17 = mAv;
    this.onHitStatusChance_18 = onStatusChance;
    this._19 = _19;
    this._1a = _1a;
    this.onHitStatus_1b = onHitStatus;
  }

  public boolean canBeDiscarded() {
    return (this.flags_00 & 0x4) == 0;
  }

  @Override
  public int getIcon() {
    return this.icon_0e;
  }

  @Override
  public String getNameTranslationKey() {
    return this.getTranslationKey();
  }

  @Override
  public String getDescriptionTranslationKey() {
    return this.getTranslationKey("description");
  }

  @Override
  public int getPrice() {
    return this.price;
  }

  public void applyEffect(final BattleEntity27c wearer) {

  }

  @Override
  public void read(final int index, final Param out) {
    switch(index) {
      case 1000 -> out.set(0); //TODO temporary - is detonate arrow
      default -> throw new RuntimeException("Invalid equipment read");
    }
  }
}
