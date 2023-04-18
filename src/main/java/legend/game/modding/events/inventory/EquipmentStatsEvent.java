package legend.game.modding.events.inventory;

import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.modding.events.Event;
import legend.game.types.EquipmentStats1c;

import static legend.game.Scus94491BpeSegment_800b.equipmentStats_800be5d8;

public class EquipmentStatsEvent extends Event {
  public final int charId;
  public final int equipmentId;

  public int flags;
  public int type;
  public int _02;
  public int equipableFlags;
  public Element element;
  public int _05;
  public final ElementSet elementalResistance = new ElementSet();
  public final ElementSet elementalImmunity = new ElementSet();
  public int statusResist;
  public int _09;
  public int icon;
  public int speed;
  public int attack;
  public int magicAttack;
  public int defence;
  public int magicDefence;
  public int attackHit;
  public int magicHit;
  public int attackAvoid;
  public int magicAvoid;
  public int statusChance;
  public int _19;
  public int _1a;
  public int onHitStatus;

  public int mpPerPhysicalHit;
  public int spPerPhysicalHit;
  public int mpPerMagicalHit;
  public int spPerMagicalHit;
  public int hpMultiplier;
  public int mpMultiplier;
  public int spMultiplier;
  public boolean magicalResistance;
  public boolean physicalResistance;
  public boolean magicalImmunity;
  public boolean physicalImmunity;
  public int revive;
  /** Percentage */
  public int hpRegen;
  public int mpRegen;
  public int spRegen;
  public int _56;
  public int special1;
  public int special2;
  public int specialAmount;

  public EquipmentStatsEvent(final int charId, final int equipmentId) {
    this.charId = charId;
    this.equipmentId = equipmentId;

    final EquipmentStats1c equipmentStats = equipmentStats_800be5d8;

    this.flags = equipmentStats.flags_00;
    this.type = equipmentStats.type_01;
    this._02 = equipmentStats._02;
    this.equipableFlags = equipmentStats.equipableFlags_03;
    this.element = equipmentStats.element_04;
    this._05 = equipmentStats._05;
    this.elementalResistance.set(equipmentStats.elementalResistance_06);
    this.elementalImmunity.set(equipmentStats.elementalImmunity_07);
    this.statusResist = equipmentStats.statusResist_08;
    this._09 = equipmentStats._09;
    this.icon = equipmentStats.icon_0e;
    this.speed = equipmentStats.spd_0f;
    this.attack = equipmentStats.atkHi_10 + equipmentStats.atk_0a;
    this.magicAttack = equipmentStats.matk_11;
    this.defence = equipmentStats.def_12;
    this.magicDefence = equipmentStats.mdef_13;
    this.attackHit = equipmentStats.aHit_14;
    this.magicHit = equipmentStats.mHit_15;
    this.attackAvoid = equipmentStats.aAv_16;
    this.magicAvoid = equipmentStats.mAv_17;
    this.statusChance = equipmentStats.onStatusChance_18;
    this._19 = equipmentStats._19;
    this._1a = equipmentStats._1a;
    this.onHitStatus = equipmentStats.onHitStatus_1b;
    this.special1 = equipmentStats.special1_0b;
    this.special2 = equipmentStats.special2_0c;
    this.specialAmount = equipmentStats.specialAmount_0d;

    if((equipmentStats.special1_0b & 0x1) != 0) {
      this.mpPerMagicalHit = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special1_0b & 0x2) != 0) {
      this.spPerMagicalHit = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special1_0b & 0x4) != 0) {
      this.mpPerPhysicalHit = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special1_0b & 0x8) != 0) {
      this.spPerPhysicalHit = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special1_0b & 0x10) != 0) {
      this.spMultiplier = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special1_0b & 0x20) != 0) {
      this.physicalResistance = true;
    }

    if((equipmentStats.special1_0b & 0x40) != 0) {
      this.magicalImmunity = true;
    }

    if((equipmentStats.special1_0b & 0x80) != 0) {
      this.physicalImmunity = true;
    }

    if((equipmentStats.special2_0c & 0x1) != 0) {
      this.mpMultiplier = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special2_0c & 0x2) != 0) {
      this.hpMultiplier = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special2_0c & 0x4) != 0) {
      this.magicalResistance = true;
    }

    if((equipmentStats.special2_0c & 0x8) != 0) {
      this.revive = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special2_0c & 0x10) != 0) {
      this.spRegen = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special2_0c & 0x20) != 0) {
      this.mpRegen = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special2_0c & 0x40) != 0) {
      this.hpRegen = equipmentStats.specialAmount_0d;
    }

    if((equipmentStats.special2_0c & 0x80) != 0) {
      this._56 = equipmentStats.specialAmount_0d;
    }
  }
}
