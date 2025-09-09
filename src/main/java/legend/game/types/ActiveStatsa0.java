package legend.game.types;

import legend.game.characters.ElementSet;
import legend.game.inventory.Equipment;

import java.util.EnumMap;
import java.util.Map;

public class ActiveStatsa0 {
  public int xp_00;
  /** ushort */
  public int hp_04;
  /** ushort */
  public int mp_06;
  /** ushort */
  public int sp_08;
  /** ushort */
  public int dxp_0a;
  /** Status, dragoon, etc. (ushort) */
  public int flags_0c;
  /** ubyte */
  public int level_0e;
  /** ubyte */
  public int dlevel_0f;

  /** ubyte */
  public final Map<EquipmentSlot, Equipment> equipment_30 = new EnumMap<>(EquipmentSlot.class);
  /** Absolute addition index into the table of every addition for every character (byte) */
  public int selectedAddition_35;
  /** ubyte */
  public final int[] additionLevels_36 = new int[8];
  /** ubyte */
  public final int[] additionXp_3e = new int[8];
  /** ushort */
  public boolean equipmentPhysicalImmunity_46;
  /** ushort */
  public boolean equipmentMagicalImmunity_48;
  /** ushort */
  public boolean equipmentPhysicalResistance_4a;
  /** short */
  public int equipmentSpMultiplier_4c;
  /** short */
  public int equipmentSpPerPhysicalHit_4e;
  /** short */
  public int equipmentMpPerPhysicalHit_50;
  /** short */
  public int equipmentSpPerMagicalHit_52;
  /** short */
  public int equipmentMpPerMagicalHit_54;
  /** short */
  public int equipmentEscapeBonus_56;
  /** short */
  public int equipmentHpRegen_58;
  /** short */
  public int equipmentMpRegen_5a;
  /** short */
  public int equipmentSpRegen_5c;
  /** short */
  public int equipmentRevive_5e;
  /** ushort */
  public boolean equipmentMagicalResistance_60;
  public short equipmentHpMulti_62;
  public short equipmentMpMulti_64;
  /** ushort */
  public int maxHp_66;
  /** byte */
  public int addition_68;
  /** ubyte */
  public int bodySpeed_69;
  /** ubyte */
  public int bodyAttack_6a;
  /** ubyte */
  public int bodyMagicAttack_6b;
  /** ubyte */
  public int bodyDefence_6c;
  /** ubyte */
  public int bodyMagicDefence_6d;
  /** ushort */
  public int maxMp_6e;
  /** byte */
  public int spellId_70;
  /** ubyte */
  public int _71;
  /** ubyte */
  public int dragoonAttack_72;
  /** ubyte */
  public int dragoonMagicAttack_73;
  /** ubyte */
  public int dragoonDefence_74;
  /** ubyte */
  public int dragoonMagicDefence_75;
  /** ubyte */
  public int specialEffectFlag_76;
  /** ubyte */
//  public int equipmentType_77;
  /** ubyte */
  public int equipment_02_78;
  /** ubyte */
  public int equipmentEquipableFlags_79;
  /** ubyte */
  public final ElementSet equipmentAttackElements_7a = new ElementSet();
  /** ubyte */
  public int equipment_05_7b;
  /** ubyte */
  public final ElementSet equipmentElementalResistance_7c = new ElementSet();
  /** ubyte */
  public final ElementSet equipmentElementalImmunity_7d = new ElementSet();
  /** ubyte */
  public int equipmentStatusResist_7e;
  /** ubyte */
  public int equipment_09_7f;
  /** ubyte */
  public int equipmentAttack1_80;
  /** ubyte */
  public int _83;
  /** byte */
  public int equipmentIcon_84;

  /** short */
  public int equipmentSpeed_86;
  /** short */
  public int equipmentAttack_88;
  /** short */
  public int equipmentMagicAttack_8a;
  /** short */
  public int equipmentDefence_8c;
  /** short */
  public int equipmentMagicDefence_8e;
  /** short */
  public int equipmentAttackHit_90;
  /** short */
  public int equipmentMagicHit_92;
  /** short */
  public int equipmentAttackAvoid_94;
  /** short */
  public int equipmentMagicAvoid_96;
  /** ubyte */
  public int equipmentOnHitStatusChance_98;
  /** ubyte */
  public int equipment_19_99;
  /** ubyte */
  public int equipment_1a_9a;
  /** ubyte */
  public int equipmentOnHitStatus_9b;
  /** ushort */
  public int addition_00_9c;
  /** ubyte */
  public int additionSpMultiplier_9e;
  /** ubyte */
  public int additionDamageMultiplier_9f;

  public ActiveStatsa0() {

  }

  public ActiveStatsa0(final ActiveStatsa0 other) {
    this.xp_00 = other.xp_00;
    this.hp_04 = other.hp_04;
    this.mp_06 = other.mp_06;
    this.sp_08 = other.sp_08;
    this.dxp_0a = other.dxp_0a;
    this.flags_0c = other.flags_0c;
    this.level_0e = other.level_0e;
    this.dlevel_0f = other.dlevel_0f;
    this.equipment_30.clear();
    this.equipment_30.putAll(other.equipment_30);
    this.selectedAddition_35 = other.selectedAddition_35;
    System.arraycopy(other.additionLevels_36, 0, this.additionLevels_36, 0, this.additionLevels_36.length);
    System.arraycopy(other.additionXp_3e, 0, this.additionXp_3e, 0, this.additionXp_3e.length);
    this.equipmentPhysicalImmunity_46 = other.equipmentPhysicalImmunity_46;
    this.equipmentMagicalImmunity_48 = other.equipmentMagicalImmunity_48;
    this.equipmentPhysicalResistance_4a = other.equipmentPhysicalResistance_4a;
    this.equipmentSpMultiplier_4c = other.equipmentSpMultiplier_4c;
    this.equipmentSpPerPhysicalHit_4e = other.equipmentSpPerPhysicalHit_4e;
    this.equipmentMpPerPhysicalHit_50 = other.equipmentMpPerPhysicalHit_50;
    this.equipmentSpPerMagicalHit_52 = other.equipmentSpPerMagicalHit_52;
    this.equipmentMpPerMagicalHit_54 = other.equipmentMpPerMagicalHit_54;
    this.equipmentEscapeBonus_56 = other.equipmentEscapeBonus_56;
    this.equipmentHpRegen_58 = other.equipmentHpRegen_58;
    this.equipmentMpRegen_5a = other.equipmentMpRegen_5a;
    this.equipmentSpRegen_5c = other.equipmentSpRegen_5c;
    this.equipmentRevive_5e = other.equipmentRevive_5e;
    this.equipmentMagicalResistance_60 = other.equipmentMagicalResistance_60;
    this.equipmentHpMulti_62 = other.equipmentHpMulti_62;
    this.equipmentMpMulti_64 = other.equipmentMpMulti_64;
    this.maxHp_66 = other.maxHp_66;
    this.addition_68 = other.addition_68;
    this.bodySpeed_69 = other.bodySpeed_69;
    this.bodyAttack_6a = other.bodyAttack_6a;
    this.bodyMagicAttack_6b = other.bodyMagicAttack_6b;
    this.bodyDefence_6c = other.bodyDefence_6c;
    this.bodyMagicDefence_6d = other.bodyMagicDefence_6d;
    this.maxMp_6e = other.maxMp_6e;
    this.spellId_70 = other.spellId_70;
    this._71 = other._71;
    this.dragoonAttack_72 = other.dragoonAttack_72;
    this.dragoonMagicAttack_73 = other.dragoonMagicAttack_73;
    this.dragoonDefence_74 = other.dragoonDefence_74;
    this.dragoonMagicDefence_75 = other.dragoonMagicDefence_75;
    this.specialEffectFlag_76 = other.specialEffectFlag_76;
//    this.equipmentType_77 = other.equipmentType_77;
    this.equipment_02_78 = other.equipment_02_78;
    this.equipmentEquipableFlags_79 = other.equipmentEquipableFlags_79;
    this.equipmentAttackElements_7a.set(other.equipmentAttackElements_7a);
    this.equipment_05_7b = other.equipment_05_7b;
    this.equipmentStatusResist_7e = other.equipmentStatusResist_7e;
    this.equipment_09_7f = other.equipment_09_7f;
    this.equipmentAttack1_80 = other.equipmentAttack1_80;
    this._83 = other._83;
    this.equipmentIcon_84 = other.equipmentIcon_84;
    this.equipmentSpeed_86 = other.equipmentSpeed_86;
    this.equipmentAttack_88 = other.equipmentAttack_88;
    this.equipmentMagicAttack_8a = other.equipmentMagicAttack_8a;
    this.equipmentDefence_8c = other.equipmentDefence_8c;
    this.equipmentMagicDefence_8e = other.equipmentMagicDefence_8e;
    this.equipmentAttackHit_90 = other.equipmentAttackHit_90;
    this.equipmentMagicHit_92 = other.equipmentMagicHit_92;
    this.equipmentAttackAvoid_94 = other.equipmentAttackAvoid_94;
    this.equipmentMagicAvoid_96 = other.equipmentMagicAvoid_96;
    this.equipmentOnHitStatusChance_98 = other.equipmentOnHitStatusChance_98;
    this.equipment_19_99 = other.equipment_19_99;
    this.equipment_1a_9a = other.equipment_1a_9a;
    this.equipmentOnHitStatus_9b = other.equipmentOnHitStatus_9b;
    this.addition_00_9c = other.addition_00_9c;
    this.additionSpMultiplier_9e = other.additionSpMultiplier_9e;
    this.additionDamageMultiplier_9f = other.additionDamageMultiplier_9f;
  }
}
