package legend.game.modding.events.inventory;

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
  public int element;
  public int _05;
  public int elementalResistance;
  public int elementalImmunity;
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

  public EquipmentStatsEvent(final int charId, final int equipmentId) {
    this.charId = charId;
    this.equipmentId = equipmentId;

    final EquipmentStats1c equipmentStats = equipmentStats_800be5d8;

    this.flags = equipmentStats.flags_00.get();
    this.type = equipmentStats.type_01.get();
    this._02 = equipmentStats._02.get();
    this.equipableFlags = equipmentStats.equipableFlags_03.get();
    this.element = equipmentStats.element_04.get();
    this._05 = equipmentStats._05.get();
    this.elementalResistance = equipmentStats.elementalResistance_06.get();
    this.elementalImmunity = equipmentStats.elementalImmunity_07.get();
    this.statusResist = equipmentStats.statusResist_08.get();
    this._09 = equipmentStats._09.get();
    this.icon = equipmentStats.icon_0e.get();
    this.speed = equipmentStats.spd_0f.get();
    this.attack = equipmentStats.atkHi_10.get() + equipmentStats.atk_0a.get();
    this.magicAttack = equipmentStats.matk_11.get();
    this.defence = equipmentStats.def_12.get();
    this.magicDefence = equipmentStats.mdef_13.get();
    this.attackHit = equipmentStats.aHit_14.get();
    this.magicHit = equipmentStats.mHit_15.get();
    this.attackAvoid = equipmentStats.aAv_16.get();
    this.magicAvoid = equipmentStats.mAv_17.get();
    this.statusChance = equipmentStats.onStatusChance_18.get();
    this._19 = equipmentStats._19.get();
    this._1a = equipmentStats._1a.get();
    this.onHitStatus = equipmentStats.onHitStatus_1b.get();

    if((equipmentStats.special1_0b.get() & 0x1) != 0) {
      this.mpPerMagicalHit = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special1_0b.get() & 0x2) != 0) {
      this.spPerMagicalHit = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special1_0b.get() & 0x4) != 0) {
      this.mpPerPhysicalHit = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special1_0b.get() & 0x8) != 0) {
      this.spPerPhysicalHit = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special1_0b.get() & 0x10) != 0) {
      this.spMultiplier = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special1_0b.get() & 0x20) != 0) {
      this.physicalResistance = true;
    }

    if((equipmentStats.special1_0b.get() & 0x40) != 0) {
      this.magicalImmunity = true;
    }

    if((equipmentStats.special1_0b.get() & 0x80) != 0) {
      this.physicalImmunity = true;
    }

    if((equipmentStats.special2_0c.get() & 0x1) != 0) {
      this.mpMultiplier = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special2_0c.get() & 0x2) != 0) {
      this.hpMultiplier = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special2_0c.get() & 0x4) != 0) {
      this.magicalResistance = true;
    }

    if((equipmentStats.special2_0c.get() & 0x8) != 0) {
      this.revive = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special2_0c.get() & 0x10) != 0) {
      this.spRegen = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special2_0c.get() & 0x20) != 0) {
      this.mpRegen = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special2_0c.get() & 0x40) != 0) {
      this.hpRegen = equipmentStats.specialAmount_0d.get();
    }

    if((equipmentStats.special2_0c.get() & 0x80) != 0) {
      this._56 = equipmentStats.specialAmount_0d.get();
    }
  }
}
