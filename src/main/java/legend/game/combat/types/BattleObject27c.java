package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.Model124;

public class BattleObject27c extends BattleScriptDataBase {
  public final ArrayRef<ShortRef> all_04; // Note: overlaps all the way to _144
  public final UnsignedShortRef level_04;
  public final UnsignedShortRef dlevel_06;
  public final UnsignedShortRef hp_08;
  public final ShortRef sp_0a;
  public final UnsignedShortRef mp_0c;
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
  public final UnsignedShortRef status_0e;
  public final UnsignedShortRef maxHp_10;
  public final UnsignedShortRef maxMp_12;
  /**
   * <ul>
   *   <li>0x8 - Display "Attack All"</li>
   *   <li>0x40 - Chance to kill</li>
   *   <li>0x80 - Resistance</li>
   * </ul>
   */
  public final UnsignedShortRef specialEffectFlag_14;
  public final UnsignedShortRef _16;
  public final UnsignedShortRef _18;
  public final UnsignedShortRef _1a;
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
  public final ShortRef elementFlag_1c;
  public final UnsignedShortRef _1e;
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
  public final UnsignedShortRef elementalResistanceFlag_20;
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
  public final UnsignedShortRef elementalImmunityFlag_22;
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
  public final UnsignedShortRef statusResistFlag_24;
  public final UnsignedShortRef _26;
  public final UnsignedShortRef _28;
  public final UnsignedShortRef _2a;
  public final UnsignedShortRef _2c;
  public final UnsignedShortRef _2e;
  public final UnsignedShortRef _30;
  public final UnsignedShortRef speed_32;
  public final UnsignedShortRef attack_34;
  public final UnsignedShortRef magicAttack_36;
  public final UnsignedShortRef defence_38;
  public final UnsignedShortRef magicDefence_3a;
  public final ShortRef attackHit_3c;
  public final ShortRef magicHit_3e;
  public final ShortRef attackAvoid_40;
  public final ShortRef magicAvoid_42;
  /**
   * Player only - if you have a weapon that inflicts a status, this will be a %
   * <p>
   * Also used if specialEffect has one-hit-KO enabled
   */
  public final UnsignedShortRef onHitStatusChance_44;
  public final UnsignedShortRef _46;
  public final UnsignedShortRef _48;
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
  public final UnsignedShortRef onHitStatus_4a;
  /** Determines turn order */
  public final ShortRef turnValue_4c;
  public final ShortRef spellId_4e;

  public final ShortRef weaponId_52;
  public final BoolRef guard_54;

  public final ShortRef additionHits_56;
  public final ShortRef selectedAddition_58;

  /** Monsters only */
  public final UnsignedShortRef originalHp_5c;
  /** Monsters only */
  public final UnsignedShortRef originalMp_5e;
  /** Monsters only */
  public final UnsignedShortRef originalAttack_60;
  /** Monsters only */
  public final UnsignedShortRef originalMagicAttack_62;
  /** Monsters only */
  public final UnsignedShortRef originalSpeed_64;
  /** Monsters only */
  public final UnsignedShortRef originalDefence_66;
  /** Monsters only */
  public final UnsignedShortRef originalMagicDefence_68;
  /** Monsters only */
  public final UnsignedShortRef originalAttackAvoid_6a;
  /** Monsters only */
  public final UnsignedShortRef originalMagicAvoid_6c;
  /**
   * <ul>
   *   <li>0x1 - special enemy - takes one damage from magical attacks</li>
   *   <li>0x2 - special enemy - takes one damage from physical attacks</li>
   *   <li>0x4 - magical immunity</li>
   *   <li>0x8 - physical immunity</li>
   * </ul>
   */
  public final UnsignedShortRef damageReductionFlags_6e;
  public final UnsignedShortRef _70;
  public final UnsignedShortRef monsterElementFlag_72;
  public final UnsignedShortRef monsterElementalImmunityFlag_74;
  public final UnsignedShortRef monsterStatusResistFlag_76;
  public final SVECTOR _78;
  public final UnsignedShortRef _7e;
  public final UnsignedShortRef _80;
  public final UnsignedShortRef _82;
  public final ShortRef _84;
  public final ShortRef _86;
  public final ShortRef _88;
  public final ShortRef _8a;

  public final UnsignedShortRef _94;
  public final UnsignedShortRef _96;
  public final ShortRef _98;
  public final ShortRef spellDamage_9a;
  public final ShortRef spellMulti_9c;
  public final ShortRef spellAccuracy_9e;
  public final ShortRef spellMp_a0;
  public final ShortRef _a2;
  public final UnsignedShortRef spellElement_a4;
  public final ShortRef _a6;
  public final ShortRef _a8;
  public final ShortRef _aa;
  public final UnsignedShortRef dragoonAttack_ac;
  public final UnsignedShortRef dragoonMagic_ae;
  public final UnsignedShortRef dragoonDefence_b0;
  public final UnsignedShortRef dragoonMagicDefence_b2;
  public final ByteRef powerAttack_b4;
  public final UnsignedByteRef powerAttackTurns_b5;
  public final ByteRef powerMagicAttack_b6;
  public final UnsignedByteRef powerMagicAttackTurns_b7;
  public final ByteRef powerDefence_b8;
  public final UnsignedByteRef powerDefenceTurns_b9;
  public final ByteRef powerMagicDefence_ba;
  public final UnsignedByteRef powerMagicDefenceTurns_bb;
  public final ByteRef tempAttackHit_bc;
  public final UnsignedByteRef tempAttackHitTurns_bd;
  public final ByteRef tempMagicHit_be;
  public final UnsignedByteRef tempMagicHitTurns_bf;

  public final ByteRef tempPhysicalImmunity_c4;
  public final UnsignedByteRef tempPhysicalImmunityTurns_c5;
  public final ByteRef tempMagicalImmunity_c6;
  public final UnsignedByteRef tempMagicalImmunityTurns_c7;
  public final ShortRef speedUpTurns_c8;
  public final ShortRef speedDownTurns_ca;
  public final ByteRef tempSpPerPhysicalHit_cc;
  public final UnsignedByteRef tempSpPerPhysicalHitTurns_cd;
  public final ByteRef tempMpPerPhysicalHit_ce;
  public final UnsignedByteRef tempMpPerPhysicalHitTurns_cf;
  public final ByteRef tempSpPerMagicalHit_d0;
  public final UnsignedByteRef tempSpPerMagicalHitTurns_d1;
  public final ByteRef tempMpPerMagicalHit_d2;
  public final UnsignedByteRef tempMpPerMagicalHitTurns_d3;
  public final ShortRef itemTarget_d4;
  public final ShortRef itemElement_d6;
  public final ShortRef itemDamage_d8;
  public final ShortRef itemSpecial1_da;
  public final ShortRef itemSpecial2_dc;
  public final ShortRef itemDamage_de;
  public final UnsignedByteRef itemSpecialAmount_e0;
  public final ShortRef _e2;
  public final ShortRef itemStatus_e4;
  public final ShortRef itemPercentage_e6;
  public final ShortRef itemUu2_e8;
  public final ShortRef itemType_ea;

  public final UnsignedShortRef physicalImmunity_110;
  public final UnsignedShortRef magicalImmunity_112;
  public final UnsignedShortRef physicalResistance_114;
  public final UnsignedShortRef magicalResistance_116;
  public final UnsignedShortRef _118;
  public final ShortRef additionSpMultiplier_11a;
  public final ShortRef additionDamageMultiplier_11c;
  public final UnsignedShortRef equipment0_11e;
  public final UnsignedShortRef equipment1_120;
  public final UnsignedShortRef equipment2_122;
  public final UnsignedShortRef equipment3_124;
  public final UnsignedShortRef equipment4_126;
  public final ShortRef spMultiplier_128;
  public final ShortRef spPerPhysicalHit_12a;
  public final ShortRef mpPerPhysicalHit_12c;
  public final ShortRef itemSpPerMagicalHit_12e;
  public final ShortRef mpPerMagicalHit_130;
  public final ShortRef _132;
  public final ShortRef hpRegen_134;
  public final ShortRef mpRegen_136;
  public final ShortRef spRegen_138;
  public final UnsignedShortRef revive_13a;
  public final UnsignedShortRef hpMulti_13c;
  public final UnsignedShortRef mpMulti_13e;

  public final UnsignedShortRef _142;
  public final Pointer<CombatantStruct1a8> combatant_144;
  public final Model124 model_148;
  public final ShortRef combatantIndex_26c;
  public final ShortRef animIndex_26e;
  public final ShortRef animIndex_270;
  /** Also monster ID */
  public final ShortRef charIndex_272;
  public final ShortRef _274;
  public final ShortRef charSlot_276;
  public final UnsignedByteRef _278;

  public BattleObject27c(final Value ref) {
    super(ref);

    this.all_04 = ref.offset(2, 0x04L).cast(ArrayRef.of(ShortRef.class, 0xa0, 2, ShortRef::new));
    this.level_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.dlevel_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this.hp_08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this.sp_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this.mp_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this.status_0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this.maxHp_10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this.maxMp_12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this.specialEffectFlag_14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this._16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
    this._18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);
    this._1a = ref.offset(2, 0x1aL).cast(UnsignedShortRef::new);
    this.elementFlag_1c = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(UnsignedShortRef::new);
    this.elementalResistanceFlag_20 = ref.offset(2, 0x20L).cast(UnsignedShortRef::new);
    this.elementalImmunityFlag_22 = ref.offset(2, 0x22L).cast(UnsignedShortRef::new);
    this.statusResistFlag_24 = ref.offset(2, 0x24L).cast(UnsignedShortRef::new);
    this._26 = ref.offset(2, 0x26L).cast(UnsignedShortRef::new);
    this._28 = ref.offset(2, 0x28L).cast(UnsignedShortRef::new);
    this._2a = ref.offset(2, 0x2aL).cast(UnsignedShortRef::new);
    this._2c = ref.offset(2, 0x2cL).cast(UnsignedShortRef::new);
    this._2e = ref.offset(2, 0x2eL).cast(UnsignedShortRef::new);
    this._30 = ref.offset(2, 0x30L).cast(UnsignedShortRef::new);
    this.speed_32 = ref.offset(2, 0x32L).cast(UnsignedShortRef::new);
    this.attack_34 = ref.offset(2, 0x34L).cast(UnsignedShortRef::new);
    this.magicAttack_36 = ref.offset(2, 0x36L).cast(UnsignedShortRef::new);
    this.defence_38 = ref.offset(2, 0x38L).cast(UnsignedShortRef::new);
    this.magicDefence_3a = ref.offset(2, 0x3aL).cast(UnsignedShortRef::new);
    this.attackHit_3c = ref.offset(2, 0x3cL).cast(ShortRef::new);
    this.magicHit_3e = ref.offset(2, 0x3eL).cast(ShortRef::new);
    this.attackAvoid_40 = ref.offset(2, 0x40L).cast(ShortRef::new);
    this.magicAvoid_42 = ref.offset(2, 0x42L).cast(ShortRef::new);
    this.onHitStatusChance_44 = ref.offset(2, 0x44L).cast(UnsignedShortRef::new);
    this._46 = ref.offset(2, 0x46L).cast(UnsignedShortRef::new);
    this._48 = ref.offset(2, 0x48L).cast(UnsignedShortRef::new);
    this.onHitStatus_4a = ref.offset(2, 0x4aL).cast(UnsignedShortRef::new);
    this.turnValue_4c = ref.offset(2, 0x4cL).cast(ShortRef::new);
    this.spellId_4e = ref.offset(2, 0x4eL).cast(ShortRef::new);

    this.weaponId_52 = ref.offset(2, 0x52L).cast(ShortRef::new);
    this.guard_54 = ref.offset(1, 0x54L).cast(BoolRef::new);

    this.additionHits_56 = ref.offset(2, 0x56L).cast(ShortRef::new);
    this.selectedAddition_58 = ref.offset(2, 0x58L).cast(ShortRef::new);

    this.originalHp_5c = ref.offset(2, 0x5cL).cast(UnsignedShortRef::new);
    this.originalMp_5e = ref.offset(2, 0x5eL).cast(UnsignedShortRef::new);
    this.originalAttack_60 = ref.offset(2, 0x60L).cast(UnsignedShortRef::new);
    this.originalMagicAttack_62 = ref.offset(2, 0x62L).cast(UnsignedShortRef::new);
    this.originalSpeed_64 = ref.offset(2, 0x64L).cast(UnsignedShortRef::new);
    this.originalDefence_66 = ref.offset(2, 0x66L).cast(UnsignedShortRef::new);
    this.originalMagicDefence_68 = ref.offset(2, 0x68L).cast(UnsignedShortRef::new);
    this.originalAttackAvoid_6a = ref.offset(2, 0x6aL).cast(UnsignedShortRef::new);
    this.originalMagicAvoid_6c = ref.offset(2, 0x6cL).cast(UnsignedShortRef::new);
    this.damageReductionFlags_6e = ref.offset(2, 0x6eL).cast(UnsignedShortRef::new);
    this._70 = ref.offset(2, 0x70L).cast(UnsignedShortRef::new);
    this.monsterElementFlag_72 = ref.offset(2, 0x72L).cast(UnsignedShortRef::new);
    this.monsterElementalImmunityFlag_74 = ref.offset(2, 0x74L).cast(UnsignedShortRef::new);
    this.monsterStatusResistFlag_76 = ref.offset(2, 0x76L).cast(UnsignedShortRef::new);
    this._78 = ref.offset(2, 0x78L).cast(SVECTOR::new);
    this._7e = ref.offset(2, 0x7eL).cast(UnsignedShortRef::new);
    this._80 = ref.offset(2, 0x80L).cast(UnsignedShortRef::new);
    this._82 = ref.offset(2, 0x82L).cast(UnsignedShortRef::new);
    this._84 = ref.offset(2, 0x84L).cast(ShortRef::new);
    this._86 = ref.offset(2, 0x86L).cast(ShortRef::new);
    this._88 = ref.offset(2, 0x88L).cast(ShortRef::new);
    this._8a = ref.offset(2, 0x8aL).cast(ShortRef::new);

    this._94 = ref.offset(2, 0x94L).cast(UnsignedShortRef::new);
    this._96 = ref.offset(2, 0x96L).cast(UnsignedShortRef::new);
    this._98 = ref.offset(2, 0x98L).cast(ShortRef::new);
    this.spellDamage_9a = ref.offset(2, 0x9aL).cast(ShortRef::new);
    this.spellMulti_9c = ref.offset(2, 0x9cL).cast(ShortRef::new);
    this.spellAccuracy_9e = ref.offset(2, 0x9eL).cast(ShortRef::new);
    this.spellMp_a0 = ref.offset(2, 0xa0L).cast(ShortRef::new);
    this._a2 = ref.offset(2, 0xa2L).cast(ShortRef::new);
    this.spellElement_a4 = ref.offset(2, 0xa4L).cast(UnsignedShortRef::new);
    this._a6 = ref.offset(2, 0xa6L).cast(ShortRef::new);
    this._a8 = ref.offset(2, 0xa8L).cast(ShortRef::new);
    this._aa = ref.offset(2, 0xaaL).cast(ShortRef::new);
    this.dragoonAttack_ac = ref.offset(2, 0xacL).cast(UnsignedShortRef::new);
    this.dragoonMagic_ae = ref.offset(2, 0xaeL).cast(UnsignedShortRef::new);
    this.dragoonDefence_b0 = ref.offset(2, 0xb0L).cast(UnsignedShortRef::new);
    this.dragoonMagicDefence_b2 = ref.offset(2, 0xb2L).cast(UnsignedShortRef::new);
    this.powerAttack_b4 = ref.offset(1, 0xb4L).cast(ByteRef::new);
    this.powerAttackTurns_b5 = ref.offset(1, 0xb5L).cast(UnsignedByteRef::new);
    this.powerMagicAttack_b6 = ref.offset(1, 0xb6L).cast(ByteRef::new);
    this.powerMagicAttackTurns_b7 = ref.offset(1, 0xb7L).cast(UnsignedByteRef::new);
    this.powerDefence_b8 = ref.offset(1, 0xb8L).cast(ByteRef::new);
    this.powerDefenceTurns_b9 = ref.offset(1, 0xb9L).cast(UnsignedByteRef::new);
    this.powerMagicDefence_ba = ref.offset(1, 0xbaL).cast(ByteRef::new);
    this.powerMagicDefenceTurns_bb = ref.offset(1, 0xbbL).cast(UnsignedByteRef::new);
    this.tempAttackHit_bc = ref.offset(1, 0xbcL).cast(ByteRef::new);
    this.tempAttackHitTurns_bd = ref.offset(1, 0xbdL).cast(UnsignedByteRef::new);
    this.tempMagicHit_be = ref.offset(1, 0xbeL).cast(ByteRef::new);
    this.tempMagicHitTurns_bf = ref.offset(1, 0xbfL).cast(UnsignedByteRef::new);

    this.tempPhysicalImmunity_c4 = ref.offset(1, 0xc4L).cast(ByteRef::new);
    this.tempPhysicalImmunityTurns_c5 = ref.offset(1, 0xc5L).cast(UnsignedByteRef::new);
    this.tempMagicalImmunity_c6 = ref.offset(1, 0xc6L).cast(ByteRef::new);
    this.tempMagicalImmunityTurns_c7 = ref.offset(1, 0xc7L).cast(UnsignedByteRef::new);
    this.speedUpTurns_c8 = ref.offset(2, 0xc8L).cast(ShortRef::new);
    this.speedDownTurns_ca = ref.offset(2, 0xcaL).cast(ShortRef::new);
    this.tempSpPerPhysicalHit_cc = ref.offset(1, 0xccL).cast(ByteRef::new);
    this.tempSpPerPhysicalHitTurns_cd = ref.offset(1, 0xcdL).cast(UnsignedByteRef::new);
    this.tempMpPerPhysicalHit_ce = ref.offset(1, 0xceL).cast(ByteRef::new);
    this.tempMpPerPhysicalHitTurns_cf = ref.offset(1, 0xcfL).cast(UnsignedByteRef::new);
    this.tempSpPerMagicalHit_d0 = ref.offset(1, 0xd0L).cast(ByteRef::new);
    this.tempSpPerMagicalHitTurns_d1 = ref.offset(1, 0xd1L).cast(UnsignedByteRef::new);
    this.tempMpPerMagicalHit_d2 = ref.offset(1, 0xd2L).cast(ByteRef::new);
    this.tempMpPerMagicalHitTurns_d3 = ref.offset(1, 0xd3L).cast(UnsignedByteRef::new);
    this.itemTarget_d4 = ref.offset(2, 0xd4L).cast(ShortRef::new);
    this.itemElement_d6 = ref.offset(2, 0xd6L).cast(ShortRef::new);
    this.itemDamage_d8 = ref.offset(2, 0xd8L).cast(ShortRef::new);
    this.itemSpecial1_da = ref.offset(2, 0xdaL).cast(ShortRef::new);
    this.itemSpecial2_dc = ref.offset(2, 0xdcL).cast(ShortRef::new);
    this.itemDamage_de = ref.offset(2, 0xdeL).cast(ShortRef::new);
    this.itemSpecialAmount_e0 = ref.offset(1, 0xe0L).cast(UnsignedByteRef::new);
    this._e2 = ref.offset(2, 0xe2L).cast(ShortRef::new);
    this.itemStatus_e4 = ref.offset(2, 0xe4L).cast(ShortRef::new);
    this.itemPercentage_e6 = ref.offset(2, 0xe6L).cast(ShortRef::new);
    this.itemUu2_e8 = ref.offset(2, 0xe8L).cast(ShortRef::new);
    this.itemType_ea = ref.offset(2, 0xeaL).cast(ShortRef::new);

    this.physicalImmunity_110 = ref.offset(2, 0x110L).cast(UnsignedShortRef::new);
    this.magicalImmunity_112 = ref.offset(2, 0x112L).cast(UnsignedShortRef::new);
    this.physicalResistance_114 = ref.offset(2, 0x114L).cast(UnsignedShortRef::new);
    this.magicalResistance_116 = ref.offset(2, 0x116L).cast(UnsignedShortRef::new);
    this._118 = ref.offset(2, 0x118L).cast(UnsignedShortRef::new);
    this.additionSpMultiplier_11a = ref.offset(2, 0x11aL).cast(ShortRef::new);
    this.additionDamageMultiplier_11c = ref.offset(2, 0x11cL).cast(ShortRef::new);
    this.equipment0_11e = ref.offset(2, 0x11eL).cast(UnsignedShortRef::new);
    this.equipment1_120 = ref.offset(2, 0x120L).cast(UnsignedShortRef::new);
    this.equipment2_122 = ref.offset(2, 0x122L).cast(UnsignedShortRef::new);
    this.equipment3_124 = ref.offset(2, 0x124L).cast(UnsignedShortRef::new);
    this.equipment4_126 = ref.offset(2, 0x126L).cast(UnsignedShortRef::new);
    this.spMultiplier_128 = ref.offset(2, 0x128L).cast(ShortRef::new);
    this.spPerPhysicalHit_12a = ref.offset(2, 0x12aL).cast(ShortRef::new);
    this.mpPerPhysicalHit_12c = ref.offset(2, 0x12cL).cast(ShortRef::new);
    this.itemSpPerMagicalHit_12e = ref.offset(2, 0x12eL).cast(ShortRef::new);
    this.mpPerMagicalHit_130 = ref.offset(2, 0x130L).cast(ShortRef::new);
    this._132 = ref.offset(2, 0x132L).cast(ShortRef::new);
    this.hpRegen_134 = ref.offset(2, 0x134L).cast(ShortRef::new);
    this.mpRegen_136 = ref.offset(2, 0x136L).cast(ShortRef::new);
    this.spRegen_138 = ref.offset(2, 0x138L).cast(ShortRef::new);
    this.revive_13a = ref.offset(2, 0x13aL).cast(UnsignedShortRef::new);
    this.hpMulti_13c = ref.offset(2, 0x13cL).cast(UnsignedShortRef::new);
    this.mpMulti_13e = ref.offset(2, 0x13eL).cast(UnsignedShortRef::new);

    this._142 = ref.offset(2, 0x142L).cast(UnsignedShortRef::new);
    this.combatant_144 = ref.offset(4, 0x144L).cast(Pointer.deferred(4, CombatantStruct1a8::new));
    this.model_148 = ref.offset(4, 0x148L).cast(Model124::new);

    this.combatantIndex_26c = ref.offset(2, 0x26cL).cast(ShortRef::new);
    this.animIndex_26e = ref.offset(2, 0x26eL).cast(ShortRef::new);
    this.animIndex_270 = ref.offset(2, 0x270L).cast(ShortRef::new);
    this.charIndex_272 = ref.offset(2, 0x272L).cast(ShortRef::new);
    this._274 = ref.offset(2, 0x274L).cast(ShortRef::new);
    this.charSlot_276 = ref.offset(2, 0x276L).cast(ShortRef::new);
    this._278 = ref.offset(1, 0x278L).cast(UnsignedByteRef::new);
  }
}
