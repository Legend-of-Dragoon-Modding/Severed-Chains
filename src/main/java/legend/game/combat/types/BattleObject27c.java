package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.game.types.Model124;

public class BattleObject27c extends BattleScriptDataBase {
  public int level_04;
  public int dlevel_06;
  public int hp_08;
  public int sp_0a;
  public int mp_0c;
  /**
   * <ul>
   *   <li>0x1 - Petrified</li>
   *   <li>0x2 - Bewitched</li>
   *   <li>0x4 - Confused</li>
   *   <li>0x8 - Fearful</li>
   *   <li>0x10 - Stunned</li>
   *   <li>0x20 - Weapon blocked</li>
   *   <li>0x40 - Dispirited</li>
   *   <li>0x80 - Also dispirited?</li>
   *   <li>0x2000 - Can become dragoon</li>
   * </ul>
   */
  public int status_0e;
  public int maxHp_10;
  public int maxMp_12;
  /**
   * <ul>
   *   <li>0x8 - Display "Attack All"</li>
   *   <li>0x40 - Chance to kill</li>
   *   <li>0x80 - Resistance</li>
   * </ul>
   */
  public int specialEffectFlag_14;
  public int _16;
  public int _18;
  public int _1a;
  /**
   * Either weapon element, or <i>display</i> element for monster (display element may be different from actual element)
   *
   * <ul>
   *   <li>0x1 - Water</li>
   *   <li>0x2 - Earth</li>
   *   <li>0x4 - Dark</li>
   *   <li>0x8 - None</li>
   *   <li>0x10 - Thunder</li>
   *   <li>0x20 - Wind</li>
   *   <li>0x40 - Light</li>
   *   <li>0x80 - Fire</li>
   * </ul>
   */
  public int elementFlag_1c;
  public int _1e;
  /**
   * <ul>
   *   <li>0x1 - Water</li>
   *   <li>0x2 - Earth</li>
   *   <li>0x4 - Dark</li>
   *   <li>0x8 - None</li>
   *   <li>0x10 - Thunder</li>
   *   <li>0x20 - Wind</li>
   *   <li>0x40 - Light</li>
   *   <li>0x80 - Fire</li>
   * </ul>
   */
  public int elementalResistanceFlag_20;
  /**
   * <ul>
   *   <li>0x1 - Water</li>
   *   <li>0x2 - Earth</li>
   *   <li>0x4 - Dark</li>
   *   <li>0x8 - None</li>
   *   <li>0x10 - Thunder</li>
   *   <li>0x20 - Wind</li>
   *   <li>0x40 - Light</li>
   *   <li>0x80 - Fire</li>
   * </ul>
   */
  public int elementalImmunityFlag_22;
  /**
   * <ul>
   *   <li>0x1 - Petrify</li>
   *   <li>0x2 - Bewitchment</li>
   *   <li>0x4 - Confusion</li>
   *   <li>0x8 - Fear</li>
   *   <li>0x10 - Stun</li>
   *   <li>0x20 - Arm Block</li>
   *   <li>0x40 - Despirit</li>
   *   <li>0x80 - Poison</li>
   * </ul>
   */
  public int statusResistFlag_24;
  public int _26;
  public int _28;
  public int _2a;
  public int _2c;
  public int _2e;
  public int _30;
  public int speed_32;
  public int attack_34;
  public int magicAttack_36;
  public int defence_38;
  public int magicDefence_3a;
  public int attackHit_3c;
  public int magicHit_3e;
  public int attackAvoid_40;
  public int magicAvoid_42;
  /**
   * Player only - if you have a weapon that inflicts a status, this will be a %
   * <p>
   * Also used if specialEffect has one-hit-KO enabled
   */
  public int onHitStatusChance_44;
  public int _46;
  public int _48;
  /**
   * Player only - if you have a weapon that inflicts a status, this will be set of statuses
   *
   * <ul>
   *   <li>0x1 - Petrify</li>
   *   <li>0x2 - Bewitchment</li>
   *   <li>0x4 - Confusion</li>
   *   <li>0x8 - Fear</li>
   *   <li>0x10 - Stun</li>
   *   <li>0x20 - Arm Block</li>
   *   <li>0x40 - Despirit</li>
   *   <li>0x80 - Poison</li>
   */
  public int onHitStatus_4a;
  /** Determines turn order */
  public int turnValue_4c;
  public int spellId_4e;

  public int weaponId_52;
  public int guard_54;
  public int additionHits_56;
  public int selectedAddition_58;

  /** Monsters only */
  public int originalHp_5c;
  /** Monsters only */
  public int originalMp_5e;
  /** Monsters only */
  public int originalAttack_60;
  /** Monsters only */
  public int originalMagicAttack_62;
  /** Monsters only */
  public int originalSpeed_64;
  /** Monsters only */
  public int originalDefence_66;
  /** Monsters only */
  public int originalMagicDefence_68;
  /** Monsters only */
  public int originalAttackAvoid_6a;
  /** Monsters only */
  public int originalMagicAvoid_6c;
  /**
   * <ul>
   *   <li>0x1 - special enemy - takes one damage from magical attacks</li>
   *   <li>0x2 - special enemy - takes one damage from physical attacks</li>
   *   <li>0x4 - magical immunity</li>
   *   <li>0x8 - physical immunity</li>
   * </ul>
   */
  public int damageReductionFlags_6e;
  public int _70;
  public int monsterElementFlag_72;
  public int monsterElementalImmunityFlag_74;
  public int monsterStatusResistFlag_76;
  public final SVECTOR _78 = new SVECTOR();
  public int _7e;
  public int _80;
  public int _82;
  public int _84;
  public int _86;
  public int _88;
  public int _8a;

  public int _94;
  public int _96;
  public int _98;
  public int spellDamage_9a;
  public int spellMulti_9c;
  public int spellAccuracy_9e;
  public int spellMp_a0;
  public int _a2;
  public int spellElement_a4;
  public int _a6;
  public int _a8;
  public int _aa;
  public int dragoonAttack_ac;
  public int dragoonMagic_ae;
  public int dragoonDefence_b0;
  public int dragoonMagicDefence_b2;
  public int powerAttack_b4;
  public int powerAttackTurns_b5;
  public int powerMagicAttack_b6;
  public int powerMagicAttackTurns_b7;
  public int powerDefence_b8;
  public int powerDefenceTurns_b9;
  public int powerMagicDefence_ba;
  public int powerMagicDefenceTurns_bb;
  public int tempAttackHit_bc;
  public int tempAttackHitTurns_bd;
  public int tempMagicHit_be;
  public int tempMagicHitTurns_bf;
  public int unknown_c0;
  public int unknownTurns_c1;
  public int unknown_c2;
  public int unknownTurns_c3;
  public int tempPhysicalImmunity_c4;
  public int tempPhysicalImmunityTurns_c5;
  public int tempMagicalImmunity_c6;
  public int tempMagicalImmunityTurns_c7;
  public int speedUpTurns_c8;
  public int speedDownTurns_ca;
  public int tempSpPerPhysicalHit_cc;
  public int tempSpPerPhysicalHitTurns_cd;
  public int tempMpPerPhysicalHit_ce;
  public int tempMpPerPhysicalHitTurns_cf;
  public int tempSpPerMagicalHit_d0;
  public int tempSpPerMagicalHitTurns_d1;
  public int tempMpPerMagicalHit_d2;
  public int tempMpPerMagicalHitTurns_d3;
  public int itemTarget_d4;
  public int itemElement_d6;
  public int itemDamage_d8;
  public int itemSpecial1_da;
  public int itemSpecial2_dc;
  public int itemDamage_de;
  public int itemSpecialAmount_e0;
  public int _e2;
  public int itemStatus_e4;
  public int itemPercentage_e6;
  public int itemUu2_e8;
  public int itemType_ea;
  public int _ec;
  public int _ee;
  public int _f0;
  public int _f2;

  public int physicalImmunity_110;
  public int magicalImmunity_112;
  public int physicalResistance_114;
  public int magicalResistance_116;
  public int _118;
  public int additionSpMultiplier_11a;
  public int additionDamageMultiplier_11c;
  public int equipment0_11e;
  public int equipment1_120;
  public int equipment2_122;
  public int equipment3_124;
  public int equipment4_126;
  public int spMultiplier_128;
  public int spPerPhysicalHit_12a;
  public int mpPerPhysicalHit_12c;
  public int itemSpPerMagicalHit_12e;
  public int mpPerMagicalHit_130;
  public int _132;
  public int hpRegen_134;
  public int mpRegen_136;
  public int spRegen_138;
  public int revive_13a;
  public int hpMulti_13c;
  public int mpMulti_13e;

  public int _142;
  public CombatantStruct1a8 combatant_144;
  public final Model124 model_148 = new Model124();
  public int combatantIndex_26c;
  public int animIndex_26e;
  public int animIndex_270;
  /** Also monster ID */
  public int charIndex_272;
  public int _274;
  public int charSlot_276;
  public int _278;

  public int getStat(final int statIndex) {
    return switch(statIndex) {
      case 0 -> this.level_04;
      case 1 -> this.dlevel_06;
      case 2 -> this.hp_08;
      case 3 -> this.sp_0a;
      case 4 -> this.mp_0c;
      case 5 -> this.status_0e;
      case 6 -> this.maxHp_10;
      case 7 -> this.maxMp_12;
      case 8 -> this.specialEffectFlag_14;
      case 9 -> this._16;
      case 10 -> this._18;
      case 11 -> this._1a;
      case 12 -> this.elementFlag_1c;
      case 13 -> this._1e;
      case 14 -> this.elementalResistanceFlag_20;
      case 15 -> this.elementalImmunityFlag_22;
      case 16 -> this.statusResistFlag_24;
      case 17 -> this._26;
      case 18 -> this._28;
      case 19 -> this._2a;
      case 20 -> this._2c;
      case 21 -> this._2e;
      case 22 -> this._30;
      case 23 -> this.speed_32;
      case 24 -> this.attack_34;
      case 25 -> this.magicAttack_36;
      case 26 -> this.defence_38;
      case 27 -> this.magicDefence_3a;
      case 28 -> this.attackHit_3c;
      case 29 -> this.magicHit_3e;
      case 30 -> this.attackAvoid_40;
      case 31 -> this.magicAvoid_42;
      case 32 -> this.onHitStatusChance_44;
      case 33 -> this._46;
      case 34 -> this._48;
      case 35 -> this.onHitStatus_4a;
      case 36 -> this.turnValue_4c;
      case 37 -> this.spellId_4e;

      case 39 -> this.weaponId_52;
      case 40 -> this.guard_54;
      case 41 -> this.additionHits_56;
      case 42 -> this.selectedAddition_58;

      case 44 -> this.originalHp_5c;
      case 45 -> this.originalMp_5e;
      case 46 -> this.originalAttack_60;
      case 47 -> this.originalMagicAttack_62;
      case 48 -> this.originalSpeed_64;
      case 49 -> this.originalDefence_66;
      case 50 -> this.originalMagicDefence_68;
      case 51 -> this.originalAttackAvoid_6a;
      case 52 -> this.originalMagicAvoid_6c;
      case 53 -> this.damageReductionFlags_6e;
      case 54 -> this._70;
      case 55 -> this.monsterElementFlag_72;
      case 56 -> this.monsterElementalImmunityFlag_74;
      case 57 -> this.monsterStatusResistFlag_76;
      case 58 -> this._78.getX();
      case 59 -> this._78.getY();
      case 60 -> this._78.getZ();
      case 61 -> this._7e;
      case 62 -> this._80;
      case 63 -> this._82;
      case 64 -> this._84;
      case 65 -> this._86;
      case 66 -> this._88;
      case 67 -> this._8a;

      case 72 -> this._94;
      case 73 -> this._96;
      case 74 -> this._98;
      case 75 -> this.spellDamage_9a;
      case 76 -> this.spellMulti_9c;
      case 77 -> this.spellAccuracy_9e;
      case 78 -> this.spellMp_a0;
      case 79 -> this._a2;
      case 80 -> this.spellElement_a4;
      case 81 -> this._a6;
      case 82 -> this._a8;
      case 83 -> this._aa;
      case 84 -> this.dragoonAttack_ac;
      case 85 -> this.dragoonMagic_ae;
      case 86 -> this.dragoonDefence_b0;
      case 87 -> this.dragoonMagicDefence_b2;
      case 88 -> (this.powerAttackTurns_b5 & 0xff) << 8 | this.powerAttack_b4 & 0xff;
      case 89 -> (this.powerMagicAttackTurns_b7 & 0xff) << 8 | this.powerMagicAttack_b6 & 0xff;
      case 90 -> (this.powerDefenceTurns_b9 & 0xff) << 8 | this.powerDefence_b8 & 0xff;
      case 91 -> (this.powerMagicDefenceTurns_bb & 0xff) << 8 | this.powerMagicDefence_ba & 0xff;
      case 92 -> (this.tempAttackHitTurns_bd & 0xff) << 8 | this.tempAttackHit_bc & 0xff;
      case 93 -> (this.tempMagicHitTurns_bf & 0xff) << 8 | this.tempMagicHit_be & 0xff;
      case 94 -> (this.unknownTurns_c1 & 0xff) << 8 | this.unknown_c0 & 0xff;
      case 95 -> (this.unknownTurns_c3 & 0xff) << 8 | this.unknown_c2 & 0xff;
      case 96 -> (this.tempPhysicalImmunityTurns_c5 & 0xff) << 8 | this.tempPhysicalImmunity_c4 & 0xff;
      case 97 -> (this.tempMagicalImmunityTurns_c7 & 0xff) << 8 | this.tempMagicalImmunity_c6 & 0xff;
      case 98 -> this.speedUpTurns_c8;
      case 99 -> this.speedDownTurns_ca;
      case 100 -> (this.tempSpPerPhysicalHitTurns_cd & 0xff) << 8 | this.tempSpPerPhysicalHit_cc & 0xff;
      case 101 -> (this.tempMpPerPhysicalHitTurns_cf & 0xff) << 8 | this.tempMpPerPhysicalHit_ce & 0xff;
      case 102 -> (this.tempSpPerMagicalHitTurns_d1 & 0xff) << 8 | this.tempSpPerMagicalHit_d0 & 0xff;
      case 103 -> (this.tempMpPerMagicalHitTurns_d3 & 0xff) << 8 | this.tempMpPerMagicalHit_d2 & 0xff;
      case 104 -> this.itemTarget_d4;
      case 105 -> this.itemElement_d6;
      case 106 -> this.itemDamage_d8;
      case 107 -> this.itemSpecial1_da;
      case 108 -> this.itemSpecial2_dc;
      case 109 -> this.itemDamage_de;
      case 110 -> this.itemSpecialAmount_e0;
      case 111 -> this._e2;
      case 112 -> this.itemStatus_e4;
      case 113 -> this.itemPercentage_e6;
      case 114 -> this.itemUu2_e8;
      case 115 -> this.itemType_ea;
      case 116 -> this._ec;
      case 117 -> this._ee;
      case 118 -> this._f0;
      case 119 -> this._f2;

      case 134 -> this.physicalImmunity_110;
      case 135 -> this.magicalImmunity_112;
      case 136 -> this.physicalResistance_114;
      case 137 -> this.magicalResistance_116;
      case 138 -> this._118;
      case 139 -> this.additionSpMultiplier_11a;
      case 140 -> this.additionDamageMultiplier_11c;
      case 141 -> this.equipment0_11e;
      case 142 -> this.equipment1_120;
      case 143 -> this.equipment2_122;
      case 144 -> this.equipment3_124;
      case 145 -> this.equipment4_126;
      case 146 -> this.spMultiplier_128;
      case 147 -> this.spPerPhysicalHit_12a;
      case 148 -> this.mpPerPhysicalHit_12c;
      case 149 -> this.itemSpPerMagicalHit_12e;
      case 150 -> this.mpPerMagicalHit_130;
      case 151 -> this._132;
      case 152 -> this.hpRegen_134;
      case 153 -> this.mpRegen_136;
      case 154 -> this.spRegen_138;
      case 155 -> this.revive_13a;
      case 156 -> this.hpMulti_13c;
      case 157 -> this.mpMulti_13e;

      case 159 -> this._142;

      default -> throw new IllegalArgumentException("Some other stat that I haven't implemented " + statIndex);
    };
  }

  public void setStat(final int statIndex, final int value) {
    switch(statIndex) {
      case 0 -> this.level_04 = value;
      case 1 -> this.dlevel_06 = value;
      case 2 -> this.hp_08 = value;
      case 3 -> this.sp_0a = value;
      case 4 -> this.mp_0c = value;
      case 5 -> this.status_0e = value;
      case 6 -> this.maxHp_10 = value;
      case 7 -> this.maxMp_12 = value;
      case 8 -> this.specialEffectFlag_14 = value;
      case 9 -> this._16 = value;
      case 10 -> this._18 = value;
      case 11 -> this._1a = value;
      case 12 -> this.elementFlag_1c = value;
      case 13 -> this._1e = value;
      case 14 -> this.elementalResistanceFlag_20 = value;
      case 15 -> this.elementalImmunityFlag_22 = value;
      case 16 -> this.statusResistFlag_24 = value;
      case 17 -> this._26 = value;
      case 18 -> this._28 = value;
      case 19 -> this._2a = value;
      case 20 -> this._2c = value;
      case 21 -> this._2e = value;
      case 22 -> this._30 = value;
      case 23 -> this.speed_32 = value;
      case 24 -> this.attack_34 = value;
      case 25 -> this.magicAttack_36 = value;
      case 26 -> this.defence_38 = value;
      case 27 -> this.magicDefence_3a = value;
      case 28 -> this.attackHit_3c = value;
      case 29 -> this.magicHit_3e = value;
      case 30 -> this.attackAvoid_40 = value;
      case 31 -> this.magicAvoid_42 = value;
      case 32 -> this.onHitStatusChance_44 = value;
      case 33 -> this._46 = value;
      case 34 -> this._48 = value;
      case 35 -> this.onHitStatus_4a = value;
      case 36 -> this.turnValue_4c = value;
      case 37 -> this.spellId_4e = value;

      case 39 -> this.weaponId_52 = value;
      case 40 -> this.guard_54 = value;
      case 41 -> this.additionHits_56 = value;
      case 42 -> this.selectedAddition_58 = value;

      case 44 -> this.originalHp_5c = value;
      case 45 -> this.originalMp_5e = value;
      case 46 -> this.originalAttack_60 = value;
      case 47 -> this.originalMagicAttack_62 = value;
      case 48 -> this.originalSpeed_64 = value;
      case 49 -> this.originalDefence_66 = value;
      case 50 -> this.originalMagicDefence_68 = value;
      case 51 -> this.originalAttackAvoid_6a = value;
      case 52 -> this.originalMagicAvoid_6c = value;
      case 53 -> this.damageReductionFlags_6e = value;
      case 54 -> this._70 = value;
      case 55 -> this.monsterElementFlag_72 = value;
      case 56 -> this.monsterElementalImmunityFlag_74 = value;
      case 57 -> this.monsterStatusResistFlag_76 = value;
      case 58 -> this._78.setX((short)value);
      case 59 -> this._78.setY((short)value);
      case 60 -> this._78.setZ((short)value);
      case 61 -> this._7e = value;
      case 62 -> this._80 = value;
      case 63 -> this._82 = value;
      case 64 -> this._84 = value;
      case 65 -> this._86 = value;
      case 66 -> this._88 = value;
      case 67 -> this._8a = value;

      case 72 -> this._94 = value;
      case 73 -> this._96 = value;
      case 74 -> this._98 = value;
      case 75 -> this.spellDamage_9a = value;
      case 76 -> this.spellMulti_9c = value;
      case 77 -> this.spellAccuracy_9e = value;
      case 78 -> this.spellMp_a0 = value;
      case 79 -> this._a2 = value;
      case 80 -> this.spellElement_a4 = value;
      case 81 -> this._a6 = value;
      case 82 -> this._a8 = value;
      case 83 -> this._aa = value;
      case 84 -> this.dragoonAttack_ac = value;
      case 85 -> this.dragoonMagic_ae = value;
      case 86 -> this.dragoonDefence_b0 = value;
      case 87 -> this.dragoonMagicDefence_b2 = value;
      case 88 -> {
        this.powerAttack_b4 = value & 0xff;
        this.powerAttackTurns_b5 = value >>> 8 & 0xff;
      }
      case 89 -> {
        this.powerMagicAttack_b6 = value & 0xff;
        this.powerMagicAttackTurns_b7 = value >>> 8 & 0xff;
      }
      case 90 -> {
        this.powerDefence_b8 = value & 0xff;
        this.powerDefenceTurns_b9 = value >>> 8 & 0xff;
      }
      case 91 -> {
        this.powerMagicDefence_ba = value & 0xff;
        this.powerMagicDefenceTurns_bb = value >>> 8 & 0xff;
      }
      case 92 -> {
        this.tempAttackHit_bc = value & 0xff;
        this.tempAttackHitTurns_bd = value >>> 8 & 0xff;
      }
      case 93 -> {
        this.tempMagicHit_be = value & 0xff;
        this.tempMagicHitTurns_bf = value >>> 8 & 0xff;
      }
      case 94 -> {
        this.unknown_c0 = value & 0xff;
        this.unknownTurns_c1 = value >>> 8 & 0xff;
      }
      case 95 -> {
        this.unknown_c2 = value & 0xff;
        this.unknownTurns_c3 = value >>> 8 & 0xff;
      }
      case 96 -> {
        this.tempPhysicalImmunity_c4 = value & 0xff;
        this.tempPhysicalImmunityTurns_c5 = value >>> 8 & 0xff;
      }
      case 97 -> {
        this.tempMagicalImmunity_c6 = value & 0xff;
        this.tempMagicalImmunityTurns_c7 = value >>> 8 & 0xff;
      }
      case 98 -> this.speedUpTurns_c8 = value;
      case 99 -> this.speedDownTurns_ca = value;
      case 100 -> {
        this.tempSpPerPhysicalHit_cc = value & 0xff;
        this.tempSpPerPhysicalHitTurns_cd = value >>> 8 & 0xff;
      }
      case 101 -> {
        this.tempMpPerPhysicalHit_ce = value & 0xff;
        this.tempMpPerPhysicalHitTurns_cf = value >>> 8 & 0xff;
      }
      case 102 -> {
        this.tempSpPerMagicalHit_d0 = value & 0xff;
        this.tempSpPerMagicalHitTurns_d1 = value >>> 8 & 0xff;
      }
      case 103 -> {
        this.tempMpPerMagicalHit_d2 = value & 0xff;
        this.tempMpPerMagicalHitTurns_d3 = value >>> 8 & 0xff;
      }
      case 104 -> this.itemTarget_d4 = value;
      case 105 -> this.itemElement_d6 = value;
      case 106 -> this.itemDamage_d8 = value;
      case 107 -> this.itemSpecial1_da = value;
      case 108 -> this.itemSpecial2_dc = value;
      case 109 -> this.itemDamage_de = value;
      case 110 -> this.itemSpecialAmount_e0 = value;
      case 111 -> this._e2 = value;
      case 112 -> this.itemStatus_e4 = value;
      case 113 -> this.itemPercentage_e6 = value;
      case 114 -> this.itemUu2_e8 = value;
      case 115 -> this.itemType_ea = value;
      case 116 -> this._ec = value;
      case 117 -> this._ee = value;
      case 118 -> this._f0 = value;
      case 119 -> this._f2 = value;

      case 134 -> this.physicalImmunity_110 = value;
      case 135 -> this.magicalImmunity_112 = value;
      case 136 -> this.physicalResistance_114 = value;
      case 137 -> this.magicalResistance_116 = value;
      case 138 -> this._118 = value;
      case 139 -> this.additionSpMultiplier_11a = value;
      case 140 -> this.additionDamageMultiplier_11c = value;
      case 141 -> this.equipment0_11e = value;
      case 142 -> this.equipment1_120 = value;
      case 143 -> this.equipment2_122 = value;
      case 144 -> this.equipment3_124 = value;
      case 145 -> this.equipment4_126 = value;
      case 146 -> this.spMultiplier_128 = value;
      case 147 -> this.spPerPhysicalHit_12a = value;
      case 148 -> this.mpPerPhysicalHit_12c = value;
      case 149 -> this.itemSpPerMagicalHit_12e = value;
      case 150 -> this.mpPerMagicalHit_130 = value;
      case 151 -> this._132 = value;
      case 152 -> this.hpRegen_134 = value;
      case 153 -> this.mpRegen_136 = value;
      case 154 -> this.spRegen_138 = value;
      case 155 -> this.revive_13a = value;
      case 156 -> this.hpMulti_13c = value;
      case 157 -> this.mpMulti_13e = value;

      case 159 -> this._142 = value;

      default -> throw new IllegalArgumentException("Some other stat that I haven't implemented " + statIndex);
    }
  }
}
