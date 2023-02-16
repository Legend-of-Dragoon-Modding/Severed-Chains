package legend.game.inventory;

import legend.game.modding.registries.RegistryEntry;
import legend.game.modding.registries.RegistryId;
import legend.game.types.EquipmentStats1c;

public class Equipment extends RegistryEntry {
  public final String name;

  public final int _00;
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

  public Equipment(final RegistryId id, final String name, final EquipmentStats1c stats) {
    this(
      id,
      name,
      stats.flags_00.get(),
      stats.type_01.get(),
      stats._02.get(),
      stats.equipableFlags_03.get(),
      stats.element_04.get(),
      stats._05.get(),
      stats.elementalResistance_06.get(),
      stats.elementalImmunity_07.get(),
      stats.statusResist_08.get(),
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

  public Equipment(final RegistryId id, final String name, int _00, int type, int _02, int equips, int element, int _05, int eHalf, int eImmune, int statRes, int _09, int atk, int special1, int special2, int specialAmount, int icon, int spd, int atkHi, int matk, int def, int mdef, int aHit, int mHit, int aAv, int mAv, int onStatusChance, int _19, int _1a, int onHitStatus) {
    super(id);
    this.name = name;
    this._00 = _00;
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
}
