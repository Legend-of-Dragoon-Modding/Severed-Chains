package legend.game.combat.bobj;

import legend.core.memory.Method;
import legend.game.characters.Element;
import legend.game.characters.ElementSet;
import legend.game.characters.StatCollection;
import legend.game.characters.StatType;
import legend.game.combat.types.AttackType;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.EventManager;
import legend.game.modding.events.combat.RegisterBattleObjectStatsEvent;
import legend.game.types.ItemStats0c;
import legend.game.types.Model124;
import legend.game.types.SpellStats0c;

import java.util.HashSet;
import java.util.Set;

public abstract class BattleObject27c extends BattleScriptDataBase {
  public final BattleObjectType type;

  public final StatCollection stats;

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

  /**
   * <ul>
   *   <li>0x8 - Display "Attack All"</li>
   *   <li>0x40 - Chance to kill</li>
   *   <li>0x80 - Resistance</li>
   * </ul>
   */
  public int specialEffectFlag_14;
  public int equipmentType_16;
  public int equipment_02_18;
  public int equipmentEquipableFlags_1a;

  public int equipment_05_1e;
  public final ElementSet equipmentElementalResistance_20 = new ElementSet();
  public final ElementSet equipmentElementalImmunity_22 = new ElementSet();
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
  public int equipmentStatusResist_24;
  public int equipment_09_26;
  public int equipmentAttack1_28;

  public int _2e;
  public int equipmentIcon_30;
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
  public int equipment_19_46;
  public int equipment_1a_48;
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
   * </ul>
   */
  public int equipmentOnHitStatus_4a;
  /** Determines turn order */
  public int turnValue_4c;
  public int spellId_4e;

  public int itemOrSpellId_52;
  public int guard_54;

  public SpellStats0c spell_94;

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
  public int tempAttackAvoid_c0;
  public int tempAttackAvoidTurns_c1;
  public int tempMagicAvoid_c2;
  public int tempMagicAvoidTurns_c3;
  public int tempPhysicalImmunity_c4;
  public int tempPhysicalImmunityTurns_c5;
  public int tempMagicalImmunity_c6;
  public int tempMagicalImmunityTurns_c7;
  public int speedUpTurns_c8;
  public int speedDownTurns_ca;

  public ItemStats0c item_d4;
  public int _ec;
  public int _ee;
  public int _f0;
  public int _f2;

  public boolean physicalImmunity_110;
  public boolean magicalImmunity_112;
  public boolean physicalResistance_114;
  public boolean magicalResistance_116;

  public int _142;
  public CombatantStruct1a8 combatant_144;
  public final Model124 model_148;
  public int combatantIndex_26c;
  public int animIndex_26e;
  public int animIndex_270;
  /** Also monster ID */
  public int charId_272;
  public int _274;
  public int charSlot_276;
  /** Has model? Used to be used to free model, no longer used since it's managed by java */
  public int _278;

  public BattleObject27c(final BattleObjectType type, final String name) {
    this.type = type;
    this.model_148 = new Model124(name);

    final Set<StatType> stats = new HashSet<>();
    EventManager.INSTANCE.postEvent(new RegisterBattleObjectStatsEvent(type, stats));
    this.stats = new StatCollection(stats.toArray(StatType[]::new));
  }

  public int getEffectiveDefence() {
    return this.defence_38;
  }

  public int getEffectiveMagicDefence() {
    return this.magicDefence_3a;
  }

  public abstract ElementSet getAttackElements();
  public abstract Element getElement();

  public abstract int calculatePhysicalAttack(final BattleObject27c target);
  /**
   * @param magicType item (0), spell (1)
   */
  public abstract int calculateMagicAttack(final BattleObject27c target, final int magicType);

  public int applyPhysicalDamageMultipliers(final int damage) {
    return damage;
  }

  @Method(0x800f29d4L)
  public int applyDamageResistanceAndImmunity(final int damage, final AttackType attackType) {
    if(attackType.isPhysical()) {
      if(this.physicalImmunity_110) {
        return 0;
      }

      if(this.physicalResistance_114) {
        return damage / 2;
      }
    }

    if(attackType.isMagical()) {
      if(this.magicalImmunity_112) {
        return 0;
      }

      if(this.magicalResistance_116) {
        return damage / 2;
      }
    }

    return damage;
  }

  public int applyElementalResistanceAndImmunity(final int damage, final Element element) {
    if(this.equipmentElementalImmunity_22.contains(element)) {
      return 0;
    }

    return damage;
  }

  public void turnFinished() {

  }

  @Deprecated
  public int getStat(final int statIndex) {
    return switch(statIndex) {
      case 2 -> this.stats.getStat(CoreMod.HP_STAT.get()).getCurrent();

      case 5 -> this.status_0e;
      case 6 -> this.stats.getStat(CoreMod.HP_STAT.get()).getMax();

      case 8 -> this.specialEffectFlag_14;
      case 9 -> this.equipmentType_16;
      case 10 -> this.equipment_02_18;
      case 11 -> this.equipmentEquipableFlags_1a;
      case 13 -> this.equipment_05_1e;

      case 14 -> this.equipmentElementalResistance_20.pack();
      case 15 -> this.equipmentElementalImmunity_22.pack();
      case 16 -> this.equipmentStatusResist_24;
      case 17 -> this.equipment_09_26;
      case 18 -> this.equipmentAttack1_28;

      case 21 -> this._2e;
      case 22 -> this.equipmentIcon_30;
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
      case 33 -> this.equipment_19_46;
      case 34 -> this.equipment_1a_48;
      case 35 -> this.equipmentOnHitStatus_4a;
      case 36 -> this.turnValue_4c;
      case 37 -> this.spellId_4e;

      case 39 -> this.itemOrSpellId_52;
      case 40 -> this.guard_54;

      case 72 -> this.spell_94.targetType_00.get();
      case 73 -> this.spell_94.flags_01.get();
      case 74 -> this.spell_94.specialEffect_02.get();
      case 75 -> this.spell_94.damage_03.get();
      case 76 -> this.spell_94.multi_04.get();
      case 77 -> this.spell_94.accuracy_05.get();
      case 78 -> this.spell_94.mp_06.get();
      case 79 -> this.spell_94.statusChance_07.get();
      case 80 -> this.spell_94.element_08.get();
      case 81 -> this.spell_94.statusType_09.get();
      case 82 -> this.spell_94.buffType_0a.get();
      case 83 -> this.spell_94._0b.get();

      case 88 -> (this.powerAttackTurns_b5 & 0xff) << 8 | this.powerAttack_b4 & 0xff;
      case 89 -> (this.powerMagicAttackTurns_b7 & 0xff) << 8 | this.powerMagicAttack_b6 & 0xff;
      case 90 -> (this.powerDefenceTurns_b9 & 0xff) << 8 | this.powerDefence_b8 & 0xff;
      case 91 -> (this.powerMagicDefenceTurns_bb & 0xff) << 8 | this.powerMagicDefence_ba & 0xff;
      case 92 -> (this.tempAttackHitTurns_bd & 0xff) << 8 | this.tempAttackHit_bc & 0xff;
      case 93 -> (this.tempMagicHitTurns_bf & 0xff) << 8 | this.tempMagicHit_be & 0xff;
      case 94 -> (this.tempAttackAvoidTurns_c1 & 0xff) << 8 | this.tempAttackAvoid_c0 & 0xff;
      case 95 -> (this.tempMagicAvoidTurns_c3 & 0xff) << 8 | this.tempMagicAvoid_c2 & 0xff;
      case 96 -> (this.tempPhysicalImmunityTurns_c5 & 0xff) << 8 | this.tempPhysicalImmunity_c4 & 0xff;
      case 97 -> (this.tempMagicalImmunityTurns_c7 & 0xff) << 8 | this.tempMagicalImmunity_c6 & 0xff;
      case 98 -> this.speedUpTurns_c8;
      case 99 -> this.speedDownTurns_ca;

      case 104 -> this.item_d4.target_00;
      case 105 -> this.item_d4.element_01.flag;
      case 106 -> this.item_d4.damageMultiplier_02;
      case 109 -> this.item_d4.damage_05;
      case 110 -> this.item_d4.specialAmount_06;
      case 111 -> this.item_d4.icon_07;
      case 112 -> this.item_d4.status_08;
      case 113 -> this.item_d4.percentage_09;
      case 114 -> this.item_d4.uu2_0a;
      case 115 -> this.item_d4.type_0b;
      case 116 -> this._ec;
      case 117 -> this._ee;
      case 118 -> this._f0;
      case 119 -> this._f2;

      case 134 -> this.physicalImmunity_110 ? 1 : 0;
      case 135 -> this.magicalImmunity_112 ? 1 : 0;
      case 136 -> this.physicalResistance_114 ? 1 : 0;
      case 137 -> this.magicalResistance_116 ? 1 : 0;

      case 159 -> this._142;

      default -> throw new IllegalArgumentException("Some other stat that I haven't implemented " + statIndex);
    };
  }

  @Deprecated
  public void setStat(final int statIndex, final int value) {
    switch(statIndex) {
      case 2 -> this.stats.getStat(CoreMod.HP_STAT.get()).setCurrent(value);

      case 5 -> this.status_0e = value;

      case 8 -> this.specialEffectFlag_14 = value;
      case 9 -> this.equipmentType_16 = value;
      case 10 -> this.equipment_02_18 = value;
      case 11 -> this.equipmentEquipableFlags_1a = value;

      case 13 -> this.equipment_05_1e = value;
      case 14 -> this.equipmentElementalResistance_20.unpack(value);
      case 15 -> this.equipmentElementalImmunity_22.unpack(value);
      case 16 -> this.equipmentStatusResist_24 = value;
      case 17 -> this.equipment_09_26 = value;
      case 18 -> this.equipmentAttack1_28 = value;

      case 21 -> this._2e = value;
      case 22 -> this.equipmentIcon_30 = value;
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
      case 33 -> this.equipment_19_46 = value;
      case 34 -> this.equipment_1a_48 = value;
      case 35 -> this.equipmentOnHitStatus_4a = value;
      case 36 -> this.turnValue_4c = value;
      case 37 -> this.spellId_4e = value;

      case 39 -> this.itemOrSpellId_52 = value;
      case 40 -> this.guard_54 = value;

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
        this.tempAttackAvoid_c0 = value & 0xff;
        this.tempAttackAvoidTurns_c1 = value >>> 8 & 0xff;
      }
      case 95 -> {
        this.tempMagicAvoid_c2 = value & 0xff;
        this.tempMagicAvoidTurns_c3 = value >>> 8 & 0xff;
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

      case 116 -> this._ec = value;
      case 117 -> this._ee = value;
      case 118 -> this._f0 = value;
      case 119 -> this._f2 = value;

      case 134 -> this.physicalImmunity_110 = value != 0;
      case 135 -> this.magicalImmunity_112 = value != 0;
      case 136 -> this.physicalResistance_114 = value != 0;
      case 137 -> this.magicalResistance_116 = value != 0;

      case 159 -> this._142 = value;

      default -> throw new IllegalArgumentException("Some other stat that I haven't implemented " + statIndex);
    }
  }
}
