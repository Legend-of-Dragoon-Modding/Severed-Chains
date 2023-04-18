package legend.game.inventory;

import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.modding.registries.RegistryEntry;
import legend.game.types.EquipmentStats1c;

public class Equipment extends RegistryEntry {
  public final String name;

  public final int _00;
  public final int type;
  public final int _02;
  public final int equips;
  public final Element element;
  public final int _05;
  public final ElementSet eHalf;
  public final ElementSet eImmune;
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

  public Equipment(final String name, final EquipmentStats1c stats) {
    this(
      name,
      stats.flags_00,
      stats.type_01,
      stats._02,
      stats.equipableFlags_03,
      stats.element_04,
      stats._05,
      stats.elementalResistance_06,
      stats.elementalImmunity_07,
      stats.statusResist_08,
      stats._09,
      stats.atk_0a,
      stats.special1_0b,
      stats.special2_0c,
      stats.specialAmount_0d,
      stats.icon_0e,
      stats.spd_0f,
      stats.atkHi_10,
      stats.matk_11,
      stats.def_12,
      stats.mdef_13,
      stats.aHit_14,
      stats.mHit_15,
      stats.aAv_16,
      stats.mAv_17,
      stats.onStatusChance_18,
      stats._19,
      stats._1a,
      stats.onHitStatus_1b
    );
  }

  public Equipment(final String name, final int _00, final int type, final int _02, final int equips, final Element element, final int _05, final ElementSet eHalf, final ElementSet eImmune, final int statRes, final int _09, final int atk, final int special1, final int special2, final int specialAmount, final int icon, final int spd, final int atkHi, final int matk, final int def, final int mdef, final int aHit, final int mHit, final int aAv, final int mAv, final int onStatusChance, final int _19, final int _1a, final int onHitStatus) {
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
