package legend.game.inventory;

import legend.game.modding.registries.RegistryId;
import legend.game.types.ActiveStatsa0;
import legend.game.types.EquipmentStats1c;

import static legend.game.SItem.characterValidEquipment_80114284;

public class EquipmentItem extends Item {
  /**
   * <ul>
   *   <li>0x4 - can't be discarded</li>
   * </ul>
   */
  public final int flags;
  public final int type;
  public final int _02;
  public final int equips;
  public final int element;
  public final int _05;
  public final int eHalf;
  public final int eImmune;
  public final int statRes;
  public final int _09;
  public final int atk;
  /**
   * Half Physical
   * SP on Magic/Physical Hit
   * MP on Magic Physical Hit
   * SP Multiplier
   */
  public final int special1;
  /**
   * Half Magic
   * Revive
   * HP/MP/SP Regen
   */
  public final int special2;
  /** Amount for MP/SP per hit or SP multiplier */
  public final int specialAmount;
  public final int icon;
  public final int spd;
  public final int atkHi;
  public final int matk;
  public final int def;
  public final int mdef;
  public final int aHit;
  public final int mHit;
  public final int aAv;
  public final int mAv;
  public final int onStatusChance;
  public final int _19;
  public final int _1a;
  public final int onHitStatus;

  public EquipmentItem(final RegistryId id, final String name, final String description, final int price, final EquipmentStats1c stats) {
    this(
      id,
      name,
      description,
      price,
      stats._00.get(),
      stats.type_01.get(),
      stats._02.get(),
      stats.equips_03.get(),
      stats.element_04.get(),
      stats._05.get(),
      stats.eHalf_06.get(),
      stats.eImmune_07.get(),
      stats.statRes_08.get(),
      stats._09.get(),
      stats.atk_0a.get(),
      stats.special1_0b.get(),
      stats.special2_0c.get(),
      stats.specialAmount_0d.get(),
      stats.icon_0e.get(),
      stats.spd_0f.get(),
      stats.atkHi_10.get(),
      stats.matk_11.get(),
      stats.def_12.get(),
      stats.mdef_13.get(),
      stats.aHit_14.get(),
      stats.mHit_15.get(),
      stats.aAv_16.get(),
      stats.mAv_17.get(),
      stats.onStatusChance_18.get(),
      stats._19.get(),
      stats._1a.get(),
      stats.onHitStatus_1b.get()
    );
  }

  public EquipmentItem(final RegistryId id, final String name, final String description, final int price, final int flags, final int type, final int _02, final int equips, final int element, final int _05, final int eHalf, final int eImmune, final int statRes, final int _09, final int atk, final int special1, final int special2, final int specialAmount, final int icon, final int spd, final int atkHi, final int matk, final int def, final int mdef, final int aHit, final int mHit, final int aAv, final int mAv, final int onStatusChance, final int _19, final int _1a, final int onHitStatus) {
    super(id, name, description, price);
    this.flags = flags;
    this.type = type;
    this._02 = _02;
    this.equips = equips;
    this.element = element;
    this._05 = _05;
    this.eHalf = eHalf;
    this.eImmune = eImmune;
    this.statRes = statRes;
    this._09 = _09;
    this.atk = atk;
    this.special1 = special1;
    this.special2 = special2;
    this.specialAmount = specialAmount;
    this.icon = icon;
    this.spd = spd;
    this.atkHi = atkHi;
    this.matk = matk;
    this.def = def;
    this.mdef = mdef;
    this.aHit = aHit;
    this.mHit = mHit;
    this.aAv = aAv;
    this.mAv = mAv;
    this.onStatusChance = onStatusChance;
    this._19 = _19;
    this._1a = _1a;
    this.onHitStatus = onHitStatus;
  }

  @Override
  public int getIcon() {
    return this.icon;
  }

  @Override
  public boolean isEquippable() {
    return true;
  }

  @Override
  public boolean isEquippableBy(final int charId) {
    return (characterValidEquipment_80114284.offset(charId).get() & this.equips) != 0;
  }

  @Override
  public EquipmentSlot getEquipmentSlot() {
    for(int i = 0; i < 5; i++) {
      if((this.type & 0x80 >> i) != 0) {
        return EquipmentSlot.fromRetail(i);
      }
    }

    return null;
  }

  @Override
  public void applyEquipmentItemStats(final ActiveStatsa0 stats) {
    stats.specialEffectFlag_76.or(this.flags);
    stats.gearType_77.or(this.type);
    stats._78.or(this._02);
    stats.gearEquips_79.or(this.equips);
    stats.elementFlag_7a.or(this.element);
    stats._7b.or(this._05);
    stats.elementalResistanceFlag_7c.or(this.eHalf);
    stats.elementalImmunityFlag_7d.or(this.eImmune);
    stats.statusResistFlag_7e.or(this.statRes);
    stats._7f.or(this._09);
    stats.gearIcon_84.add(this.icon);
    stats.gearSpeed_86.add((short)this.spd);
    stats.gearAttack_88.add((short)this.atkHi);
    stats.gearMagicAttack_8a.add((short)this.matk);
    stats.gearDefence_8c.add((short)this.def);
    stats.gearMagicDefence_8e.add((short)this.mdef);
    stats.attackHit_90.add((short)this.aHit);
    stats.magicHit_92.add((short)this.mHit);
    stats.attackAvoid_94.add((short)this.aAv);
    stats.magicAvoid_96.add((short)this.mAv);
    stats.onHitStatusChance_98.add(this.onStatusChance);
    stats._99.add(this._19);
    stats._9a.add(this._1a);
    stats.onHitStatus_9b.or(this.onHitStatus);
    stats._80.add(this.atk);
    stats.gearAttack_88.add((short)this.atk);
    stats.special1_81.or(this.special1);

    int mask = 0x1;
    int bit;
    for(bit = 0; bit < 8; bit++) {
      if((this.special1 & mask) != 0) {
        if(mask == 0x1) {
          stats.mpPerMagicalHit_54.add((short)this.specialAmount);
        } else if(mask == 0x2) {
          stats.spPerMagicalHit_52.add((short)this.specialAmount);
        } else if(mask == 0x4) {
          stats.mpPerPhysicalHit_50.add((short)this.specialAmount);
        } else if(mask == 0x8) {
          stats.spPerPhysicalHit_4e.add((short)this.specialAmount);
        } else if(mask == 0x10) {
          stats.spMultiplier_4c.add((short)this.specialAmount);
        } else if(mask == 0x20) {
          stats.physicalResistance_4a.set(1);
        } else if(mask == 0x40) {
          stats.magicalImmunity_48.set(1);
        } else if(mask == 0x80) {
          stats.physicalImmunity_46.set(1);
        }
      }

      mask <<= 1;
    }

    stats.special2_82.or(this.special2);

    mask = 0x1;
    for(bit = 0; bit < 8; bit++) {
      if((this.special2 & mask) != 0) {
        if(mask == 0x1) {
          stats.mpMulti_64.add((short)this.specialAmount);
        } else if(mask == 0x2) {
          stats.hpMulti_62.add((short)this.specialAmount);
        } else if(mask == 0x4) {
          stats.magicalResistance_60.set(1);
        } else if(mask == 0x8) {
          stats.revive_5e.add((short)this.specialAmount);
        } else if(mask == 0x10) {
          stats.spRegen_5c.add((short)this.specialAmount);
        } else if(mask == 0x20) {
          stats.mpRegen_5a.add((short)this.specialAmount);
        } else if(mask == 0x40) {
          stats.hpRegen_58.add((short)this.specialAmount);
        } else if(mask == 0x80) {
          stats.specialAmount_56.add((short)this.specialAmount);
        }
      }

      mask <<= 1;
    }
  }

  @Override
  public boolean canBeDiscarded() {
    return (this.flags & 0x4) == 0;
  }
}
