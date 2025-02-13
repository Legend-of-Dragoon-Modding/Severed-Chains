package legend.game.inventory;

import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;
import legend.game.types.EquipmentSlot;
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

  public final ItemIcon icon_0e;
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

  public Equipment(final int price, final int flags, final EquipmentSlot slot, final int _02, final int equipableFlags, final Element element, final int _05, final ElementSet elementalResistance, final ElementSet elementalImmunity, final int statusResist, final int _09, final int atk, final int mpPerPhysicalHit, final int spPerPhysicalHit, final int mpPerMagicalHit, final int spPerMagicalHit, final int hpMultiplier, final int mpMultiplier, final int spMultiplier, final boolean magicalResistance, final boolean physicalResistance, final boolean magicalImmunity, final boolean physicalImmunity, final int revive, final int hpRegen, final int mpRegen, final int spRegen, final int escapeBonus, final ItemIcon icon, final int spd, final int atkHi, final int matk, final int def, final int mdef, final int aHit, final int mHit, final int aAv, final int mAv, final int onStatusChance, final int _19, final int _1a, final int onHitStatus) {
    this.price = price;
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
  public ItemIcon getIcon() {
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
