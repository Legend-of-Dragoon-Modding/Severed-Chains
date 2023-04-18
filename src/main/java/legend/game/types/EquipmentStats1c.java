package legend.game.types;

import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.unpacker.FileData;

public class EquipmentStats1c {
  /**
   * <ul>
   *   <li>0x4 - can't be discarded</li>
   * </ul>
   */
  public final int flags_00;
  public final int type_01;
  public final int _02;
  public final int equipableFlags_03;
  public final Element element_04;
  public final int _05;
  public final ElementSet elementalResistance_06 = new ElementSet();
  public final ElementSet elementalImmunity_07 = new ElementSet();
  public final int statusResist_08;
  public final int _09;
  public final int atk_0a;
  /**
   * Half Physical
   * SP on Magic/Physical Hit
   * MP on Magic Physical Hit
   * SP Multiplier
   */
  public final int special1_0b;
  /**
   * Half Magic
   * Revive
   * HP/MP/SP Regen
   */
  public final int special2_0c;
  /** Amount for MP/SP per hit or SP multiplier */
  public final int specialAmount_0d;
  public final int icon_0e;
  public final int spd_0f;
  public final int atkHi_10;
  public final int matk_11;
  public final int def_12;
  public final int mdef_13;
  public final int aHit_14;
  public final int mHit_15;
  public final int aAv_16;
  public final int mAv_17;
  public final int onStatusChance_18;
  public final int _19;
  public final int _1a;
  public final int onHitStatus_1b;

  public static EquipmentStats1c fromFile(final FileData data) {
    final int flags = data.readUByte(0x0);
    final int type = data.readUByte(0x1);
    final int _02 = data.readUByte(0x2);
    final int equipableFlags = data.readUByte(0x3);
    final Element element = Element.fromFlag(data.readUByte(0x4));
    final int _05 = data.readUByte(0x5);
    final ElementSet elementalResistance = new ElementSet().unpack(data.readUByte(0x6));
    final ElementSet elementalImmunity = new ElementSet().unpack(data.readUByte(0x7));
    final int statusResist = data.readUByte(0x8);
    final int _09 = data.readUByte(0x9);
    final int atk = data.readUByte(0xa);
    final int special1 = data.readUByte(0xb);
    final int special2 = data.readUByte(0xc);
    final int specialAmount = data.readUByte(0xd);
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

    return new EquipmentStats1c(flags, type, _02, equipableFlags, element, _05, elementalResistance, elementalImmunity, statusResist, _09, atk, special1, special2, specialAmount, icon, spd, atkHi, matk, def, mdef, aHit, mHit, aAv, mAv, onStatusChance, _19, _1a, onHitStatus);
  }

  public EquipmentStats1c(final int flags, final int type, final int _02, final int equipableFlags, final Element element, final int _05, final ElementSet elementalResistance, final ElementSet elementalImmunity, final int statusResist, final int _09, final int atk, final int special1, final int special2, final int specialAmount, final int icon, final int spd, final int atkHi, final int matk, final int def, final int mdef, final int aHit, final int mHit, final int aAv, final int mAv, final int onStatusChance, final int _19, final int _1a, final int onHitStatus) {
    this.flags_00 = flags;
    this.type_01 = type;
    this._02 = _02;
    this.equipableFlags_03 = equipableFlags;
    this.element_04 = element;
    this._05 = _05;
    this.elementalResistance_06.set(elementalResistance);
    this.elementalImmunity_07.set(elementalImmunity);
    this.statusResist_08 = statusResist;
    this._09 = _09;
    this.atk_0a = atk;
    this.special1_0b = special1;
    this.special2_0c = special2;
    this.specialAmount_0d = specialAmount;
    this.icon_0e = icon;
    this.spd_0f = spd;
    this.atkHi_10 = atkHi;
    this.matk_11 = matk;
    this.def_12 = def;
    this.mdef_13 = mdef;
    this.aHit_14 = aHit;
    this.mHit_15 = mHit;
    this.aAv_16 = aAv;
    this.mAv_17 = mAv;
    this.onStatusChance_18 = onStatusChance;
    this._19 = _19;
    this._1a = _1a;
    this.onHitStatus_1b = onHitStatus;
  }
}
