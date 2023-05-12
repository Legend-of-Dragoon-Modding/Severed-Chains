package legend.game.combat.bobj;

import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.types.Model124;

public abstract class BattleObject27c extends BattleScriptDataBase {
  public int hp_08;

  /**
   * <ul>
   *   <li>0x1 - Petrified</li>
   *   <li>0x2 - Bewitched</li>
   *   <li>0x4 - Confused</li>
   *   <li>0x8 - Fearful</li>
   *   <li>0x10 - Stunned</li>
   *   <li>0x20 - Weapon blocked</li>
   *   <li>0x40 - Dispirited</li>
   *   <li>0x80 - Poison</li>
   *   <li>0x800 - Don't apply elemental effects for this attack (cleared after damage is done)</li>
   *   <li>0x2000 - Can become dragoon</li>
   * </ul>
   */
  public int status_0e;
  public int maxHp_10;

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
   *   <li>0x40 - Dispirit</li>
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
   *   <li>0x40 - Dispirit</li>
   *   <li>0x80 - Poison</li>
   * </ul>
   */
  public int onHitStatus_4a;
  /** Determines turn order */
  public int turnValue_4c;
  public int spellId_4e;

  public int itemOrSpellId_52;
  public int guard_54;

  /** Enemy aggressiveness with counters; lower means it can counter tighter timings; 0 = cannot counter */
  public int hitCounterFrameThreshold_7e;
  public int _80;
  public int _82;
  public int _84;
  public int _86;
  public int _88;
  public int _8a;

  public int targetType_94;
  public int _96;
  public int specialEffect_98;
  public int spellDamage_9a;
  public int spellMulti_9c;
  public int spellAccuracy_9e;
  public int spellMp_a0;
  public int statusChance_a2;
  public int spellElement_a4;
  public int statusType_a6;
  public int buffType_a8;
  public int _aa;

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
  /**
   * <ul>
   *   <li>0x04 - cause status</li>
   *   <li>0x08 - cure status</li>
   *   <li>0x10 - revive</li>
   *   <li>0x20 - SP</li>
   *   <li>0x40 - MP</li>
   *   <li>0x80 - HP</li>
   * </ul>
   */
  public int itemType_ea;
  public int _ec;
  public int _ee;
  public int _f0;
  public int _f2;

  public int physicalImmunity_110;
  public int magicalImmunity_112;
  public int physicalResistance_114;
  public int magicalResistance_116;

  public int _142;
  public CombatantStruct1a8 combatant_144;
  public final Model124 model_148;
  public int combatantIndex_26c;
  public int animIndex_26e;
  public int animIndex_270;
  /** Also monster ID */
  public int charIndex_272;
  public int _274;
  public int charSlot_276;
  /** Has model? Used to be used to free model, no longer used since it's managed by java */
  public int _278;

  public BattleObject27c(final String name) {
    this.model_148 = new Model124(name);
  }

  public int getStat(final int statIndex) {
    return switch(statIndex) {
      case 2 -> this.hp_08;

      case 5 -> this.status_0e;
      case 6 -> this.maxHp_10;

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

      case 39 -> this.itemOrSpellId_52;
      case 40 -> this.guard_54;

      case 61 -> this.hitCounterFrameThreshold_7e;
      case 62 -> this._80;
      case 63 -> this._82;
      case 64 -> this._84;
      case 65 -> this._86;
      case 66 -> this._88;
      case 67 -> this._8a;

      case 72 -> this.targetType_94;
      case 73 -> this._96;
      case 74 -> this.specialEffect_98;
      case 75 -> this.spellDamage_9a;
      case 76 -> this.spellMulti_9c;
      case 77 -> this.spellAccuracy_9e;
      case 78 -> this.spellMp_a0;
      case 79 -> this.statusChance_a2;
      case 80 -> this.spellElement_a4;
      case 81 -> this.statusType_a6;
      case 82 -> this.buffType_a8;
      case 83 -> this._aa;

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

      case 159 -> this._142;

      default -> throw new IllegalArgumentException("Some other stat that I haven't implemented " + statIndex);
    };
  }

  public void setStat(final int statIndex, final int value) {
    switch(statIndex) {
      case 2 -> this.hp_08 = value;

      case 5 -> this.status_0e = value;
      case 6 -> this.maxHp_10 = value;

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

      case 39 -> this.itemOrSpellId_52 = value;
      case 40 -> this.guard_54 = value;

      case 61 -> this.hitCounterFrameThreshold_7e = value;
      case 62 -> this._80 = value;
      case 63 -> this._82 = value;
      case 64 -> this._84 = value;
      case 65 -> this._86 = value;
      case 66 -> this._88 = value;
      case 67 -> this._8a = value;

      case 72 -> this.targetType_94 = value;
      case 73 -> this._96 = value;
      case 74 -> this.specialEffect_98 = value;
      case 75 -> this.spellDamage_9a = value;
      case 76 -> this.spellMulti_9c = value;
      case 77 -> this.spellAccuracy_9e = value;
      case 78 -> this.spellMp_a0 = value;
      case 79 -> this.statusChance_a2 = value;
      case 80 -> this.spellElement_a4 = value;
      case 81 -> this.statusType_a6 = value;
      case 82 -> this.buffType_a8 = value;
      case 83 -> this._aa = value;

      case 88 -> {
        this.powerAttack_b4 = (byte)value;
        this.powerAttackTurns_b5 = value >>> 8 & 0xff;
      }
      case 89 -> {
        this.powerMagicAttack_b6 = (byte)value;
        this.powerMagicAttackTurns_b7 = value >>> 8 & 0xff;
      }
      case 90 -> {
        this.powerDefence_b8 = (byte)value;
        this.powerDefenceTurns_b9 = value >>> 8 & 0xff;
      }
      case 91 -> {
        this.powerMagicDefence_ba = (byte)value;
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

      case 159 -> this._142 = value;

      default -> throw new IllegalArgumentException("Some other stat that I haven't implemented " + statIndex);
    }
  }
}
