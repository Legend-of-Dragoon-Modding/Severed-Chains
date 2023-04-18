package legend.game.types;

import legend.game.characters.Element;
import legend.game.characters.ElementSet;

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
  public final int[] equipment_30 = new int[5];
  /** Absolute addition index into the table of every addition for every character (byte) */
  public int selectedAddition_35;
  /** ubyte */
  public final int[] additionLevels_36 = new int[8];
  /** ubyte */
  public final int[] additionXp_3e = new int[8];
  /** ushort */
  public int physicalImmunity_46;
  /** ushort */
  public int magicalImmunity_48;
  /** ushort */
  public int physicalResistance_4a;
  /** short */
  public int spMultiplier_4c;
  /** short */
  public int spPerPhysicalHit_4e;
  /** short */
  public int mpPerPhysicalHit_50;
  /** short */
  public int spPerMagicalHit_52;
  /** short */
  public int mpPerMagicalHit_54;
  /** short */
  public int _56;
  /** short */
  public int hpRegen_58;
  /** short */
  public int mpRegen_5a;
  /** short */
  public int spRegen_5c;
  /** short */
  public int revive_5e;
  /** ushort */
  public int magicalResistance_60;
  public short hpMulti_62;
  public short mpMulti_64;
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
  public int _77;
  /** ubyte */
  public int _78;
  /** ubyte */
  public int _79;
  /** ubyte */
  public Element elementFlag_7a;
  /** ubyte */
  public int _7b;
  /** ubyte */
  public final ElementSet elementalResistanceFlag_7c = new ElementSet();
  /** ubyte */
  public final ElementSet elementalImmunityFlag_7d = new ElementSet();
  /** ubyte */
  public int statusResistFlag_7e;
  /** ubyte */
  public int _7f;
  /** ubyte */
  public int _80;
  /** ubyte */
  public int special1_81;
  /** ubyte */
  public int special2_82;
  /** ubyte */
  public int _83;
  /** byte */
  public int _84;

  /** short */
  public int gearSpeed_86;
  /** short */
  public int gearAttack_88;
  /** short */
  public int gearMagicAttack_8a;
  /** short */
  public int gearDefence_8c;
  /** short */
  public int gearMagicDefence_8e;
  /** short */
  public int attackHit_90;
  /** short */
  public int magicHit_92;
  /** short */
  public int attackAvoid_94;
  /** short */
  public int magicAvoid_96;
  /** ubyte */
  public int onHitStatusChance_98;
  /** ubyte */
  public int _99;
  /** ubyte */
  public int _9a;
  /** ubyte */
  public int onHitStatus_9b;
  /** ushort */
  public int _9c;
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
    this.selectedAddition_35 = other.selectedAddition_35;
    this.physicalImmunity_46 = other.physicalImmunity_46;
    this.magicalImmunity_48 = other.magicalImmunity_48;
    this.physicalResistance_4a = other.physicalResistance_4a;
    this.spMultiplier_4c = other.spMultiplier_4c;
    this.spPerPhysicalHit_4e = other.spPerPhysicalHit_4e;
    this.mpPerPhysicalHit_50 = other.mpPerPhysicalHit_50;
    this.spPerMagicalHit_52 = other.spPerMagicalHit_52;
    this.mpPerMagicalHit_54 = other.mpPerMagicalHit_54;
    this._56 = other._56;
    this.hpRegen_58 = other.hpRegen_58;
    this.mpRegen_5a = other.mpRegen_5a;
    this.spRegen_5c = other.spRegen_5c;
    this.revive_5e = other.revive_5e;
    this.magicalResistance_60 = other.magicalResistance_60;
    this.hpMulti_62 = other.hpMulti_62;
    this.mpMulti_64 = other.mpMulti_64;
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
    this._77 = other._77;
    this._78 = other._78;
    this._79 = other._79;
    this.elementFlag_7a = other.elementFlag_7a;
    this._7b = other._7b;
    this.statusResistFlag_7e = other.statusResistFlag_7e;
    this._7f = other._7f;
    this._80 = other._80;
    this.special1_81 = other.special1_81;
    this.special2_82 = other.special2_82;
    this._83 = other._83;
    this._84 = other._84;
    this.gearSpeed_86 = other.gearSpeed_86;
    this.gearAttack_88 = other.gearAttack_88;
    this.gearMagicAttack_8a = other.gearMagicAttack_8a;
    this.gearDefence_8c = other.gearDefence_8c;
    this.gearMagicDefence_8e = other.gearMagicDefence_8e;
    this.attackHit_90 = other.attackHit_90;
    this.magicHit_92 = other.magicHit_92;
    this.attackAvoid_94 = other.attackAvoid_94;
    this.magicAvoid_96 = other.magicAvoid_96;
    this.onHitStatusChance_98 = other.onHitStatusChance_98;
    this._99 = other._99;
    this._9a = other._9a;
    this.onHitStatus_9b = other.onHitStatus_9b;
    this._9c = other._9c;
    this.additionSpMultiplier_9e = other.additionSpMultiplier_9e;
    this.additionDamageMultiplier_9f = other.additionDamageMultiplier_9f;
  }
}
